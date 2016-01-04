/*
 * $RCSfile: BaseAction.java,v $$
 * $Revision: 1.1 $
 * $Date: 2009-7-19 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.j2ee.action;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.skin.j2ee.util.Request;

/**
 * <p>Title: BaseAction</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public abstract class BaseAction implements Action {
    protected HttpServletRequest request = null;
    protected HttpServletResponse response = null;
    protected ServletContext servletContext = null;
    protected String defaultUrlEncoding = "UTF-8";

    /**
     * @throws ServletException
     * @throws IOException
     */
    public void init() throws ServletException, IOException {
    }

    /**
     * @return String
     */
    public String getDefaultUrlEncoding() {
        return this.defaultUrlEncoding;
    }

    /**
     * @param defaultUrlEncoding
     */
    public void setDefaultUrlEncoding(String defaultUrlEncoding) {
        this.defaultUrlEncoding = defaultUrlEncoding;
    }

    /**
     * @return HttpServletRequest
     */
    public HttpServletRequest getRequest() {
        return this.request;
    }

    /**
     * @param request
     */
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * @return HttpServletResponse
     */
    public HttpServletResponse getResponse() {
        return this.response;
    }

    /**
     * @param response
     */
    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    /**
     * @return ServletContext
     */
    public ServletContext getServletContext() {
        return this.servletContext;
    }

    /**
     * @param servletContext
     */
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /**
     * @return HttpSession
     */
    public HttpSession getHttpSession() {
        return this.request.getSession();
    }

    /**
     * @param create
     * @return HttpSession
     */
    public HttpSession getHttpSession(boolean create) {
        return this.request.getSession(create);
    }

    /**
     * @return String
     */
    public String getContextPath() {
        String contextPath = this.request.getContextPath();

        if(contextPath == null || contextPath.equals("/")) {
            return "";
        }
        return contextPath;
    }

    /**
     * @return String
     */
    public String getContentType() {
        return this.request.getContentType();
    }

    /**
     * @return int
     */
    public int getContentLength() {
        return this.request.getContentLength();
    }

    /**
     * @param key
     * @return Object
     */
    public Object getAttribute(String key) {
        return this.request.getAttribute(key);
    }

    /**
     * @param name
     * @param value
     */
    public void setAttribute(String name, Object value) {
        if(name != null && name.length() > 0) {
            this.request.setAttribute(name, value);
        }
    }

    /**
     * @param path
     * @throws ServletException
     * @throws IOException
     */
    public void forward(String path) throws ServletException, IOException {
        this.request.getRequestDispatcher(path).forward(this.request, this.response);
    }

    /**
     * @param path
     * @throws ServletException
     * @throws IOException
     */
    public void redirect(String path) throws ServletException, IOException {
        int k = path.indexOf("${contextPath}");

        if(k > -1)
        {
            String contextPath = this.getContextPath();
            this.response.sendRedirect(path.substring(0, k) + contextPath + path.substring(k + 14));
        }
        else
        {
            this.response.sendRedirect(path);
        }
    }

    /**
     * @param path
     * @return String
     */
    public String getRealPath(String path) {
        return this.getServletContext().getRealPath(path);
    }

    /**
     * @param path
     * @return InputStream
     */
    public InputStream getResource(String path) {
        return this.getServletContext().getResourceAsStream(path);
    }

    /**
     * @return Map
     */
    public Map<String, String[]> getParameterMap() {
        return Request.getParameterMap(this.request);
    }

    /**
     * @param <T>
     * @return <T>
     */
    public <T> T parse(T bean) {
        return Request.parse(this.request, bean);
    }

    /**
     * @return int
     */
    public int getPageSize() {
        return this.getInteger("pageSize", 15).intValue();
    }

    /**
     * @return int
     * @throws Exception
     */
    public int getPageNum() {
        return this.getPageNum(1);
    }

    /**
     * @param defaultValue
     * @return int
     * @throws Exception
     */
    public int getPageNum(int defaultValue) {
        return this.getInteger("pageNum", defaultValue).intValue();
    }

    /**
     * @param name
     * @return String
     */
    public String getParameter(String name) {
        return this.request.getParameter(name);
    }

    /**
     * @param name
     * @return String[]
     */
    public String[] getParameterValues(String name) {
        return this.request.getParameterValues(name);
    }

    /**
     * @param name
     * @param defalutValue
     * @return String
     */
    public String getParameter(String name, String defalutValue) {
        String value = this.request.getParameter(name);
        return (value != null)? value.trim() : defalutValue;
    }

    /**
     * @param name
     * @return String
     */
    public String getString(String name) {
        return this.request.getParameter(name);
    }

    /**
     * @param name
     * @param defalutValue
     * @return String
     */
    public String getString(String name, String defalutValue) {
        return this.getParameter(name, defalutValue);
    }

    /**
     * @param name
     * @param defalutValue
     * @return String
     */
    public String getTrimString(String name) {
        String value = this.getParameter(name);
        return (value != null ? value.trim() : "");
    }

    /**
     * @param name
     * @param defalutValue
     * @return String
     */
    public String getTrimString(String name, String defalutValue) {
        String value = this.getParameter(name, defalutValue);
        return (value != null ? value.trim() : "");
    }

    /**
     * @param name
     * @return Byte
     */
    public Byte getByte(String name) {
        return Request.getByte(this.request, name);
    }

    /**
     * @param name
     * @param defalutValue
     * @return Byte
     */
    public Byte getByte(String name, byte defalutValue) {
        return Request.getByte(this.request, name, defalutValue);
    }

    /**
     * @param name
     * @return Short
     */
    public Short getShort(String name) {
        return Request.getShort(this.request, name);
    }

    /**
     * @param name
     * @param defalutValue
     * @return Short
     */
    public Short getShort(String name, short defalutValue) {
        return Request.getShort(this.request, name, defalutValue);
    }

    /**
     * @param name
     * @return Boolean
     */
    public Boolean getBoolean(String name) {
        return Request.getBoolean(this.request, name);
    }

    /**
     * @param name
     * @param name
     * @return Boolean
     */
    public Boolean getBoolean(String name, boolean defalutValue) {
        return Request.getBoolean(this.request, name, defalutValue);
    }

    /**
     * @param name
     * @return Character
     */
    public Character getCharacter(String name) {
        return Request.getCharacter(this.request, name);
    }

    /**
     * @param name
     * @param defaultValue
     * @return Character
     */
    public Character getCharacter(String name, char defalutValue) {
        return Request.getCharacter(this.request, name, defalutValue);
    }

    /**
     * @param name
     * @return Integer
     */
    public Integer getInteger(String name) {
        return Request.getInteger(this.request, name);
    }

    /**
     * @param name
     * @param defalutValue
     * @return Integer
     */
    public Integer getInteger(String name, int defalutValue) {
        return Request.getInteger(this.request, name, defalutValue);
    }

    /**
     * @param name
     * @return Integer[]
     */
    public int[] getIntegerValues(String name) {
        return Request.getIntegerValues(this.request, name);
    }

    /**
     * @param name
     * @return Float
     */
    public Float getFloat(String name) {
        return Request.getFloat(this.request, name);
    }

    /**
     * @param name
     * @param defalutValue
     * @return Float
     */
    public Float getFloat(String name, float defalutValue) {
        return Request.getFloat(this.request, name, defalutValue);
    }

    /**
     * @param name
     * @return Double
     */
    public Double getDouble(String name) {
        return Request.getDouble(this.request, name);
    }

    /**
     * @param name
     * @param defalutValue
     * @return Double
     */
    public Double getDouble(String name, double defalutValue) {
        return Request.getDouble(this.request, name, defalutValue);
    }

    /**
     * @param name
     * @return Long
     */
    public Long getLong(String name) {
        return Request.getLong(this.request, name);
    }

    /**
     * @param name
     * @param defalutValue
     * @return Long
     */
    public Long getLong(String name, long defalutValue) {
        return Request.getLong(this.request, name, defalutValue);
    }

    /**
     * @param name
     * @param defalutValue
     * @return long[]
     */
    public long[] getLongValues(String name) {
        return Request.getLongValues(this.request, name);
    }

    /**
     * @param name
     * @return Date
     */
    public java.util.Date getDate(String name) {
        return Request.getDate(this.request, name);
    }

    /**
     * @param name
     * @return Timestamp
     */
    public Timestamp getTimestamp(String name) {
        return Request.getTimestamp(this.request, name);
    }

    /**
     * @param name
     * @param format
     * @return Date
     */
    public java.util.Date getDate(String name, String format) {
        return Request.getDate(this.request, name, format);
    }

    /**
     * @param name
     * @param format
     * @return Timestamp
     */
    public Timestamp getTimestamp(String name, String format) {
        return Request.getTimestamp(this.request, name, format);
    }

    /**
     * @param type
     * @param fieldName
     * @param name
     */
    public Object getValue(Class<?> clazz, String fieldName, String name) throws Exception {
        Field field = clazz.getDeclaredField(fieldName);
        Class<?> type = field.getType();

        if(type == boolean.class || type == Boolean.class) {
            return this.getBoolean(name);
        }
        else if(type == byte.class || type == Byte.class) {
            return this.getByte(name);
        }
        else if(type == short.class || type == Short.class) {
            return this.getShort(name);
        }
        else if(type == int.class || type == Integer.class) {
            return this.getInteger(name);
        }
        else if(type == float.class || type == Float.class) {
            return this.getFloat(name);
        }
        else if(type == double.class || type == Double.class) {
            return this.getDouble(name);
        }
        else if(type == long.class || type == Long.class) {
            return this.getLong(name);
        }
        else if(type == String.class) {
            return this.getString(name);
        }
        else if(type == Date.class) {
            return this.getDate(name);
        }
        return null;
    }

    /**
     * @param minutes
     */
    protected void setCache(int minutes) {
        this.setCache(this.getResponse(), minutes);
    }

    /**
     * @param minutes
     */
    protected void setCache(HttpServletResponse response, int minutes) {
        if(minutes < 1) {
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Expires", "0");
        }
        else {
            response.addHeader("Cache-Control", "max-age=" + (minutes * 60));
            response.addHeader("Cache-Control", "public");
            long currentTimeMillis = System.currentTimeMillis();
            response.setDateHeader("Expires", currentTimeMillis + (minutes * 60 * 1000));
        }
    }

    /**
     * @param throwable
     * @return ServletException
     */
    public ServletException error(Throwable throwable) {
        if(throwable instanceof ServletException) {
            return (ServletException)throwable;
        }
        return new ServletException(throwable);
    }

    /**
     * @param status
     * @param message
     * @throws IOException
     */
    public void error(int status, String message) throws IOException {
        this.request.setAttribute("javax_servlet_error", message);
        this.response.sendError(status);
    }

    @Override
    public void release() {
    }
}
