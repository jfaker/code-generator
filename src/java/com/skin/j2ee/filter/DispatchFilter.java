/*
 * $RCSfile: DispatchFilter.java,v $$
 * $Revision: 1.1 $
 * $Date: 2009-7-13 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.j2ee.filter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.j2ee.action.Action;
import com.skin.j2ee.annotation.Namespace;
import com.skin.j2ee.annotation.UrlPattern;

/**
 * <p>Title: DispatchFilter</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class DispatchFilter implements Filter {
    private String packages = null;
    private Map<String, Method> actionsMap = null;
    private ClassLoader classLoader = null;
    private FilterConfig filterConfig = null;
    private ServletContext servletContext = null;
    private String contextPath = null;
    private static final Class<Action> ACTION = Action.class;
    private static final Logger logger = LoggerFactory.getLogger(DispatchFilter.class);

    /**
     * @param filterConfig
     */
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        this.servletContext = filterConfig.getServletContext();
        this.packages = this.filterConfig.getInitParameter("packages");
        this.contextPath = this.servletContext.getContextPath();

        if(this.contextPath == null || this.contextPath.equals("/")) {
            this.contextPath = "";
        }

        if(this.packages != null) {
            char c;
            StringBuilder buffer = new StringBuilder();

            for(int i = 0, length = this.packages.length(); i < length; i++) {
                c = this.packages.charAt(i);

                if(Character.isWhitespace(c) || Character.isISOControl(c)) {
                    continue;
                }

                buffer.append(c);
            }

            this.packages = buffer.toString();
        }

        if(logger.isInfoEnabled()) {
            logger.info("packages: " + this.packages);
        }

        try {
            this.initActionMap();
        }
        catch(ServletException e) {
            throw e;
        }
        this.info();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            throw new ServletException("DispatchFilter just supports HTTP requests");
        }
        this.dispatch((HttpServletRequest)request, (HttpServletResponse)response, filterChain);
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void dispatch(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        request.setAttribute("DispatchFilter$requestURI", requestURI);

        if(this.contextPath.length() > 0) {
            requestURI = requestURI.substring(this.contextPath.length());
        }

        requestURI = this.replace(requestURI, "//", "/");
        Method method = this.getMethod(requestURI);

        if(method == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if(logger.isDebugEnabled()) {
            logger.debug("requestURI: " + requestURI + ", dispatch: " + method.getDeclaringClass().getName() + "." + method.getName() + "()");
        }

        // int status = 500;
        Action action = null;
        Throwable throwable = null;

        try {
            Class<?> type = method.getDeclaringClass();
            action = this.getActionInstance(type);

            if(action == null) {
                // status = 404;
                throw new ServletException("There is no Action mapped for url " + requestURI);
            }

            action.setServletContext(this.servletContext);
            action.setRequest(request);
            action.setResponse(response);
            action.init();

            long t1 = System.currentTimeMillis();
            method.invoke(action, new Object[]{});
            long t2 = System.currentTimeMillis();

            if(logger.isDebugEnabled()) {
                logger.debug("execute time: " + (t2 - t1) + " - " + requestURI);
            }
        }
        catch(SecurityException e) {
            throwable = e;
        }
        catch(IllegalArgumentException e) {
            throwable = e;
        }
        catch(IllegalAccessException e) {
            throwable = e;
        }
        catch(InvocationTargetException e) {
            throwable = e;
        }
        catch(Throwable t) {
            throwable = t;
        }
        finally {
            if(action != null) {
                try {
                    action.release();
                }
                catch(Throwable t) {
                    t.printStackTrace();
                }
            }
        }

        if(throwable != null) {
            Throwable t = throwable.getCause();

            if(t != null) {
                throwable = t;
            }

            if(throwable instanceof ServletException) {
                throw (ServletException)throwable;
            }
            throw new ServletException(throwable);
        }
    }

    /**
     * @param requestURI
     * @return Method
     */
    public Method getMethod(String requestURI) {
        return this.actionsMap.get(requestURI);
    }

    /**
     * @param className
     * @return Class<?>
     * @throws ClassNotFoundException
     */
    public Class<?> getClass(String className) throws ClassNotFoundException {
        Class<?> clazz = null;

        try {
            clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
        }
        catch(Exception e) {
        }

        if(clazz != null) {
            return clazz;
        }

        try {
            clazz = DispatchFilter.class.getClassLoader().loadClass(className);
        }
        catch(ClassNotFoundException e) {
        }

        if(clazz != null) {
            return clazz;
        }

        return Class.forName(className);
    }

    /**
     * @param type
     * @return Action
     */
    public Action getActionInstance(Class<?> type) {
        if(ACTION.isAssignableFrom(type)) {
            try {
                return (Action)(type.newInstance());
            }
            catch(Exception e) {
                logger.warn(e.getMessage(), e);
            }
        }

        return null;
    }

    /**
     * @param packages
     */
    public void setPackages(String packages) {
        this.packages = packages;
    }

    /**
     * @return String
     */
    public String getPackages() {
        return this.packages;
    }

    /**
     * @return ClassLoader
     */
    public ClassLoader getClassLoader() {
        return (this.classLoader != null ? this.classLoader : Thread.currentThread().getContextClassLoader());
    }

    /**
     * @param classLoader
     */
    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    /**
     * @throws ServletException
     */
    public void initActionMap() throws ServletException {
        this.actionsMap = new HashMap<String, Method>();

        if(this.packages != null) {
            String[] names = this.packages.split("\\s*[,]\\s*");

            for(int i = 0; i < names.length; i++) {
                if(logger.isInfoEnabled()) {
                    logger.info("Find In: " + names[i]);
                }

                this.findInPackage(names[i]);
            }
        }
    }

    /**
     * @param packageName
     */
    private void findInPackage(String packageName) throws ServletException {
        String path = packageName.replace('.', '/');
        java.util.Enumeration<URL> urls = null;

        try {
            urls = this.getClassLoader().getResources(path);
        }
        catch(IOException e) {
            logger.warn(e.getMessage(), e);
            return;
        }

        while(urls.hasMoreElements()) {
            String urlPath = urls.nextElement().getFile();

            try {
                urlPath = URLDecoder.decode(urlPath, "UTF-8");
            }
            catch(UnsupportedEncodingException e) {
                throw new ServletException(e);
            }

            if(urlPath.startsWith("file:")) {
                urlPath = urlPath.substring(5);
            }

            if(urlPath.indexOf('!') > 0) {
                urlPath = urlPath.substring(0, urlPath.indexOf('!'));
            }

            if(logger.isInfoEnabled()) {
                logger.info("Scanning for classes in [" + urlPath + "]");
            }

            File file = new File(urlPath);

            if(file.isDirectory()) {
                loadImplementationsInDirectory(path, file);
            }
            else {
                loadImplementationsInJar(path, file);
            }
        }
    }

    /**
     * @param parent
     * @param location
     */
    private void loadImplementationsInDirectory(String parent, File location) throws ServletException {
        File files[] = location.listFiles();

        for(int i = 0, length = files.length; i < length; i++) {
            File file = files[i];
            StringBuilder buffer = new StringBuilder(100);
            buffer.append(parent).append("/").append(file.getName());
            String packageOrClass = parent != null ? buffer.toString() : file.getName();

            if(file.isDirectory()) {
                loadImplementationsInDirectory(packageOrClass, file);
                continue;
            }

            if(file.getName().endsWith(".class")) {
                addIfMatching(packageOrClass);
            }
        }
    }

    /**
     * @param parent
     * @param jarfile
     */
    private void loadImplementationsInJar(String parent, File jarfile) throws ServletException {
        InputStream inputStream = null;
        JarInputStream jarInputStream = null;

        try {
            JarEntry entry = null;
            inputStream = new FileInputStream(jarfile);
            jarInputStream = new JarInputStream(new FileInputStream(jarfile));

            while((entry = jarInputStream.getNextJarEntry()) != null) {
                String name = entry.getName();

                if(!entry.isDirectory() && name.startsWith(parent) && name.endsWith(".class")) {
                    addIfMatching(name);
                }
            }
        }
        catch(IOException e) {
            logger.error(e.getMessage());
            logger.error("Could not search jar file '" + jarfile + "'");
        }
        finally {
            if(jarInputStream != null) {
                try {
                    jarInputStream.close();
                }
                catch(IOException e) {
                }
            }

            if(inputStream != null) {
                try {
                    inputStream.close();
                }
                catch(IOException e) {
                }
            }
        }
    }

    /**
     * @param fqn
     */
    private void addIfMatching(String fqn) throws ServletException {
        String path = null;
        Class<?> type = null;
        String externalName = fqn.substring(0, fqn.indexOf('.')).replace('/', '.');

        try {
            type = getClassLoader().loadClass(externalName);
        }
        catch(ClassNotFoundException e) {
            throw new ServletException(e);
        }

        if(Modifier.isAbstract(type.getModifiers())) {
            return;
        }

        if(Modifier.isInterface(type.getModifiers())) {
            return;
        }

        if(ACTION.isAssignableFrom(type)) {
            Namespace namespace = type.getAnnotation(Namespace.class);

            if(namespace != null) {
                path = namespace.value();
            }

            if(path == null) {
                path = "";
            }

            Method[] methods = type.getMethods();

            for(Method method : methods) {
                if(Modifier.isPublic(method.getModifiers())) {
                    UrlPattern urlPattern = method.getAnnotation(UrlPattern.class);

                    if(urlPattern != null) {
                        String[] values = urlPattern.value();

                        if(values != null && values.length > 0) {
                            for(String url : values) {
                                if(this.actionsMap.get(url) != null) {
                                    throw new ServletException(url + " already exists: " + type.getName());
                                }

                                this.actionsMap.put(url, method);
                            }
                        }
                    }
                }
            }
        }
        else {
            if(logger.isDebugEnabled()) {
                logger.debug("Ignore Type: " + type.getName());
            }
        }
    }

    /**
     * @param source
     * @param search
     * @param replacement
     * @return String
     */
    private String replace(String source, String search, String replacement) {
        if(source == null) {
            return "";
        }

        if(search == null) {
            return source;
        }

        int s = 0;
        int e = 0;
        int d = search.length();
        StringBuilder buffer = new StringBuilder();

        do {
            e = source.indexOf(search, s);

            if(e == -1) {
                buffer.append(source.substring(s));
                break;
            }
            buffer.append(source.substring(s, e)).append(replacement);
            s = e + d;
        }
        while(true);

        return buffer.toString();
    }

    public void info() {
        if(logger.isInfoEnabled()) {
            for(Map.Entry<String, Method> entry : this.actionsMap.entrySet()) {
                Method method = entry.getValue();
                logger.info("ActionMapping: " + entry.getKey() + " - " + method.getDeclaringClass().getName() + "." + method.getName() + "()");
            }
        }
    }

    /**
     * @param request
     * @param response
     * @param code
     * @param throwable
     * @throws IOException
     */
    public void sendError(HttpServletRequest request, HttpServletResponse response, int code, Throwable throwable) throws IOException {
        if(code == 500) {
            request.setAttribute("javax.servlet.error.message", throwable.getMessage());
            request.setAttribute("javax.servlet.error.exception", throwable.getCause());
            request.setAttribute("javax.servlet.jsp.jspException", throwable.getCause());
        }

        response.sendError(500);
    }

    @Override
    public void destroy() {
        this.packages = null;
        this.actionsMap.clear();
        this.actionsMap = null;
    }
}
