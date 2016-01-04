/*
 * $RCSfile: FinderServlet.java,v $$
 * $Revision: 1.1 $
 * $Date: 2010-04-28 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.FileType;
import com.skin.finder.FinderManager;
import com.skin.finder.config.Workspace;
import com.skin.j2ee.util.JsonUtil;
import com.skin.j2ee.util.MimeType;
import com.skin.j2ee.util.Response;
import com.skin.util.GMTUtil;
import com.skin.util.HtmlUtil;
import com.skin.util.IO;

/**
 * <p>Title: FinderServlet</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @version 1.0
 */
public class FinderServlet {
    private ServletContext servletContext;
    private static final Map<String, String> map = new HashMap<String, String>();
    private static final Logger logger = LoggerFactory.getLogger(FinderServlet.class);

    static{
        map.put("exe",    "exe");
        map.put("bin",    "bin");
        map.put("class",  "class");
        map.put("swf",    "swf");
        map.put("ico",    "ico");
        map.put("jpg",    "jpg");
        map.put("jpeg",   "jpeg");
        map.put("gif",    "gif");
        map.put("bmp",    "bmp");
        map.put("png",    "png");
        map.put("pdf",    "pdf");
        map.put("doc",    "doc");
        map.put("zip",    "zip");
        map.put("rar",    "rar");
        map.put("jar",    "jar");
        map.put("ear",    "ear");
        map.put("war",    "war");
    }

    public FinderServlet() {
    }

    /**
     * @param servletContext
     */
    public FinderServlet(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.error(request, response, 404, "Not Found !");
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void hello(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.forward(request, response, "/template/finder/hello.jsp");
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void blank(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.forward(request, response, "/template/finder/blank.jsp");
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void index(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String workspace = request.getParameter("workspace");

        if(workspace == null || workspace.trim().length() < 1) {
            Set<String> workspaces = Workspace.getInstance().keySet();
            request.setAttribute("workspaces", workspaces);
            this.forward(request, response, "/template/finder/workspace.jsp");
        }
        else {
            request.setAttribute("workspace", workspace.trim());
            this.forward(request, response, "/template/finder/index.jsp");
        }
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void tree(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String workspace = request.getParameter("workspace");
        request.setAttribute("workspace", workspace);
        this.forward(request, response, "/template/finder/tree.jsp");
    }

    /**
     * @param request
     * @param response
     * @param listUrl
     * @param xmlUrl
     * @throws ServletException
     * @throws IOException
     */
    public void getFolderXml(HttpServletRequest request, HttpServletResponse response, String listUrl, String xmlUrl) throws ServletException, IOException {
        String workspace = request.getParameter("workspace");
        String path = request.getParameter("path");
        String home = this.getWorkspace(workspace);
        FinderManager finderManager = new FinderManager(home);
        String xml = finderManager.getFolderXml(workspace, path, listUrl, xmlUrl);

        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
        Response.write(request, response, "text/xml; charset=UTF-8", xml);
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void display(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String workspace = request.getParameter("workspace");
        String path = request.getParameter("path");
        String home = this.getWorkspace(workspace);
        FinderManager finderManager = new FinderManager(home);
        String filePath = finderManager.getPath(path);

        if(filePath == null) {
            throw new ServletException("Can't access !");
        }

        File file = new File(filePath);

        if(file.isDirectory()) {
            request.setAttribute("workspace", workspace);
            Map<String, Object> context = finderManager.list(path);

            if(context == null) {
                Response.write(request, response, "<h1>" + path + " not exists !</h1>");
                return;
            }

            for(Map.Entry<String, Object> entry : context.entrySet()) {
                request.setAttribute(entry.getKey(), entry.getValue());
            }

            this.forward(request, response, "/template/finder/finder.jsp");
            return;
        }

        String type = request.getParameter("type");
        String encoding = request.getParameter("encoding");
        String theme = request.getParameter("theme");
        String download = request.getParameter("download");
        String parent = finderManager.getParent(file);
        String temp = filePath.substring(finderManager.getWork().length()).replace('\\', '/');

        if(type == null || type.length() < 1) {
            type = FileType.getExtension(path).toLowerCase();
        }
        else {
            type = type.toLowerCase();
        }

        if(download == null || download.trim().length() < 1) {
            download = "false";
        }

        long length = file.length();

        if(download.equals("true") || map.get(type) != null) {
            if(file.exists() == false) {
                this.error(request, response, 404, "Not Found");
                return;
            }

            long lastModified = file.lastModified();
            String contentType = MimeType.getMimeType(file.getName());
            String etag = this.getETag(lastModified, 0L, length);

            // cache
            response.setHeader("Cache-Control", "private");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", System.currentTimeMillis() + 5000);
            response.setHeader("Last-Modified", GMTUtil.format(lastModified));
            response.setHeader("ETag", etag);
            response.setHeader("Date", GMTUtil.format(lastModified));
            response.setHeader("Content-Type", contentType);

            // attachment
            if(contentType.equals("application/octet-stream") || download.equals("true")) {
                response.setHeader("Content-Disposition", "attachment; filename=\"" + finderManager.urlEncode(FileType.getName(path), "UTF-8") + "\"");
            }

            InputStream inputStream = null;
            response.setContentLength((int)(file.length()));

            try {
                inputStream = new FileInputStream(file);
                IO.copy(inputStream, response.getOutputStream());
            }
            catch(IOException e) {
            }
            finally {
                IO.close(inputStream);
            }
        }
        else {
            long offset = 0;
            String content = null;

            if(file.exists()) {
                String charset = encoding;

                if(charset == null || charset.trim().length() < 1) {
                    charset = "UTF-8";
                }

                if(length > 512L * 1024L) {
                    offset = file.length() - 512L * 1024L;
                    length = file.length() - offset;
                    content = this.read(file, offset, charset);
                }
                else if(length > 0L) {
                    content = IO.read(file, charset, 4096);
                }
                else {
                    content = "";
                }

                content = HtmlUtil.encode(content);
            }

            request.setAttribute("workspace", workspace);
            request.setAttribute("work", finderManager.getWork());
            request.setAttribute("path", temp);
            request.setAttribute("parent", parent);
            request.setAttribute("content", content);
            request.setAttribute("encoding", encoding);
            request.setAttribute("fileExists", (content == null ? false : true));
            request.setAttribute("type", type);
            request.setAttribute("theme", theme);
            request.setAttribute("offset", offset);
            request.setAttribute("length", length);
            this.forward(request, response, "/template/finder/display.jsp");
        }
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void suggest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String workspace = request.getParameter("workspace");
        String path = request.getParameter("path");
        String home = this.getWorkspace(workspace);
        FinderManager finderManager = new FinderManager(home);
        Object json = finderManager.suggest(workspace, path);
        JsonUtil.callback(request, response, json);
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void rename(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String workspace = request.getParameter("workspace");
        String path = request.getParameter("path");
        String nameName = request.getParameter("newName");
        String home = this.getWorkspace(workspace);
        FinderManager finderManager = new FinderManager(home);
        int count = finderManager.rename(path, nameName);
        Response.write(request, response, "text/javascript; charset=UTF-8", "{\"code\": 0, \"count\": " + count + "}");
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int count = 0;
        String workspace = request.getParameter("workspace");
        String[] path = request.getParameterValues("path");
        String home = this.getWorkspace(workspace);
        FinderManager finderManager = new FinderManager(home);

        for(int i = 0; i < path.length; i++) {
            count += finderManager.delete(path[i]);
        }

        Response.write(request, response, "text/javascript; charset=UTF-8", "{\"code\": 0, \"count\": " + count + "}");
    }

    /**
     * @param name
     * @return String
     */
    public String getWorkspace(String name) {
        if(name == null) {
            throw new NullPointerException("workspace must be not null !");
        }

        Workspace workspace = Workspace.getInstance();
        String work = workspace.getValue(name.trim());

        if(work == null) {
            throw new NullPointerException("workspace must be not null !");
        }

        if(work.startsWith("file:")) {
            return new File(work.substring(5)).getAbsolutePath();
        }

        if(work.startsWith("contextPath:")) {
            return this.getServletContext().getRealPath(work.substring(12));
        }

        throw new NullPointerException("work directory error: " + work);
    }

    /**
     * @param file
     * @param offset
     * @param charset
     * @return String
     */
    public String read(File file, long offset, String charset) {
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;

        try {
            inputStream = new FileInputStream(file);

            if(charset == null || charset.length() < 1) {
                inputStreamReader = new InputStreamReader(inputStream);
            }
            else {
                inputStreamReader = new InputStreamReader(inputStream, charset);
            }

            inputStream.skip(offset);

            int length = 0;
            int bufferSize = 4096;
            char[] buffer = new char[bufferSize];
            StringBuilder result = new StringBuilder();

            while((length = inputStreamReader.read(buffer, 0, bufferSize)) > -1) {
                result.append(buffer, 0, length);
            }

            return result.toString();
        }
        catch(IOException e) {
            logger.warn(e.getMessage(), e);
        }
        finally {
            IO.close(inputStreamReader);
            IO.close(inputStream);
        }

        return null;
    }

    /**
     * @param lastModified
     * @param start
     * @param end
     * @return String
     */
    protected String getETag(long lastModified, long start, long end) {
        return ("W/\"f-" + lastModified + "\"");
    }

    /**
     * @param request
     * @param response
     * @param path
     * @throws ServletException
     * @throws IOException
     */
    public void forward(HttpServletRequest request, HttpServletResponse response, String path) throws ServletException, IOException {
        request.getRequestDispatcher(path).forward(request, response);
    }

    /**
     * @param request
     * @param response
     * @param status
     * @param message
     * @throws ServletException
     * @throws IOException
     */
    public void error(HttpServletRequest request, HttpServletResponse response, int status, String message) throws ServletException, IOException {
        request.setAttribute("javax_servlet_error", message);
        response.sendError(status);
    }

    /**
     * @return the servletContext
     */
    public ServletContext getServletContext() {
        return this.servletContext;
    }

    /**
     * @param servletContext the servletContext to set
     */
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
