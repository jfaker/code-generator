/*
 * $RCSfile: Request.java,v $$
 * $Revision: 1.1  $
 * $Date: 2009-11-12  $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.j2ee.util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.util.ClassUtil;

/**
 * <p>Title: Request</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class Request {
    private static final Logger logger = LoggerFactory.getLogger(Request.class);

    /**
     * @param request
     * @return String
     */
    public static String getHost(HttpServletRequest request) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(request.getScheme());
        buffer.append("://");
        buffer.append(request.getServerName());

        int port = request.getServerPort();

        if(port != 80) {
            buffer.append(port);
        }

        String contextPath = request.getContextPath();

        if(contextPath != null && contextPath.equals("/") == false) {
            buffer.append(contextPath);
        }

        return buffer.toString();
    }


    /**
     * @param request
     * @return String
     */
    public static String getContextPath(HttpServletRequest request) {
        String contextPath = request.getContextPath();

        if(contextPath == null || contextPath.equals("/")) {
            return "";
        }

        return contextPath;
    }

    /**
     * @param request
     * @return String
     */
    public static String getRequestURL(HttpServletRequest request) {
        String requestURL = request.getRequestURL().toString();
        String queryString = request.getQueryString();

        if(queryString == null || (queryString = queryString.trim()).length() < 1) {
            return requestURL;
        }

        char c = queryString.charAt(0);
        StringBuilder url = new StringBuilder(requestURL);

        if(c != '?' && c != '&' && c != '#') {
            url.append("?");
        }

        url.append(queryString);
        return url.toString();
    }

    /**
     * @param request
     * @return String
     */
    public static String getRemoteAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forward-For");

        if(isEmpty(ip) || equalsIgnoreCase(ip, "unknown")) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if(isEmpty(ip) || equalsIgnoreCase(ip, "unknown")) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if(isEmpty(ip) || equalsIgnoreCase(ip, "unknown")) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }

        if(isEmpty(ip) || equalsIgnoreCase(ip, "unknown")) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }

        if(isEmpty(ip) || equalsIgnoreCase(ip, "unknown")) {
            ip = request.getRemoteAddr();
        }

        if("127.0.0.1".equals(ip) || "loalhost".equalsIgnoreCase(ip)) {
            ip = Request.getLocalHostAddress();
        }

        return ip;
    }

    /**
     * @param request
     * @return String
     */
    public static String getProxyAddress(HttpServletRequest request) {
        String ip = Request.getRemoteAddress(request);

        if(!isEmpty(ip) && !ip.equalsIgnoreCase(request.getRemoteAddr())) {
            return request.getRemoteAddr();
        }

        return null;
    }

    /**
     * @return String
     */
    public static String getLocalHostAddress() {
        try {
            StringBuilder buffer = new StringBuilder();
            byte[] bytes = java.net.InetAddress.getLocalHost().getAddress();

            for(int i = 0; i < bytes.length; i++) {
                buffer.append(bytes[i] & 0xFF).append(".");
            }

            if(buffer.length() > 0) {
                buffer.deleteCharAt(buffer.length() - 1);
            }

            return buffer.toString();
        }
        catch(UnknownHostException e) {
            logger.warn(e.getMessage(), e);
        }

        return "127.0.0.1";
    }

    /**
     * @param source
     * @return String
     */
    public static String encode(String source) {
        try {
            return URLEncoder.encode(source, "UTF-8");
        }
        catch(UnsupportedEncodingException e) {
        }

        return "";
    }

    /**
     * @param <T>
     * @param request
     * @param bean
     * @return <T>
     */
    public static <T> T parse(HttpServletRequest request, T bean) {
        String name = null;
        String value = null;
        Method[] methods = bean.getClass().getMethods();

        for(int i = 0; i < methods.length; i++) {
            name = methods[i].getName();
            if(name.length() > 3 && name.startsWith("set")) {
                try {
                    name = Character.toLowerCase(name.charAt(3)) + name.substring(4);
                    value = request.getParameter(name);

                    if(value != null) {
                        Class<?>[] types = methods[i].getParameterTypes();

                        if(types != null && types.length == 1) {
                            Object object = ClassUtil.cast(types[0], value);

                            if(object != null) {
                                methods[i].invoke(bean, new Object[]{object});
                            }
                        }
                    }
                }
                catch(SecurityException e) {
                }
                catch(IllegalArgumentException e) {
                }
                catch(IllegalAccessException e) {
                }
                catch(InvocationTargetException e) {
                }
            }
        }
        return bean;
    }

    /**
     * @param request
     * @param name
     * @return String
     */
    public static String getString(HttpServletRequest request, String name) {
        return request.getParameter(name);
    }

    /**
     * @param request
     * @param name
     * @param defaultValue
     * @return String
     */
    public static String getString(HttpServletRequest request, String name, String defaultValue) {
        String value = request.getParameter(name);
        return (value != null)? value : defaultValue;
    }

    /**
     * @param request
     * @param name
     * @return Byte
     */
    public static Byte getByte(HttpServletRequest request, String name) {
        String value = request.getParameter(name);

        if(value != null) {
            try {
                return Byte.parseByte(value);
            }
            catch(NumberFormatException e){}
        }

        return null;
    }

    /**
     * @param request
     * @param name
     * @param defaultValue
     * @return Byte
     */
    public static Byte getByte(HttpServletRequest request, String name, byte defaultValue) {
        String value = request.getParameter(name);

        if(value != null) {
            try {
                return Byte.parseByte(value);
            }
            catch(NumberFormatException e){}
        }

        return Byte.valueOf(defaultValue);
    }

    /**
     * @param request
     * @param name
     * @return Short
     */
    public static Short getShort(HttpServletRequest request, String name) {
        String value = request.getParameter(name);

        if(value != null) {
            try {
                return Short.parseShort(value);
            }
            catch(NumberFormatException e){}
        }

        return null;
    }

    /**
     * @param request
     * @param name
     * @param defaultValue
     * @return Short
     */
    public static Short getShort(HttpServletRequest request, String name, short defaultValue) {
        String value = request.getParameter(name);

        if(value != null) {
            try {
                return Short.parseShort(value);
            }
            catch(NumberFormatException e){}
        }

        return Short.valueOf(defaultValue);
    }

    /**
     * @param request
     * @param name
     * @return Boolean
     */
    public static Boolean getBoolean(HttpServletRequest request, String name) {
        String value = request.getParameter(name);

        if(value != null) {
            boolean b = ("1".equalsIgnoreCase(value) || "y".equalsIgnoreCase(value) || "on".equalsIgnoreCase(value) || "yes".equalsIgnoreCase(value) || "true".equalsIgnoreCase(value));
            return Boolean.valueOf(b);
        }

        return Boolean.FALSE;
    }

    /**
     * @param request
     * @param name
     * @param defaultValue
     * @return Boolean
     */
    public static Boolean getBoolean(HttpServletRequest request, String name, boolean defaultValue) {
        String value = request.getParameter(name);

        if(value != null) {
            boolean b = ("1".equalsIgnoreCase(value) || "y".equalsIgnoreCase(value) || "on".equalsIgnoreCase(value) || "yes".equalsIgnoreCase(value) || "true".equalsIgnoreCase(value));
            return Boolean.valueOf(b);
        }

        return Boolean.valueOf(defaultValue);
    }

    /**
     * @param request
     * @param name
     * @return Character
     */
    public static Character getCharacter(HttpServletRequest request, String name) {
        String value = request.getParameter(name);

        if(value != null && value.length() > 0) {
            return Character.valueOf(value.charAt(0));
        }

        return null;
    }

    /**
     * @param request
     * @param name
     * @param defaultValue
     * @return Character
     */
    public static Character getCharacter(HttpServletRequest request, String name, char defaultValue) {
        String value = request.getParameter(name);

        if(value != null && value.length() > 0) {
            return Character.valueOf(value.charAt(0));
        }

        return Character.valueOf(defaultValue);
    }

    /**
     * @param request
     * @param name
     * @return Integer
     */
    public static Integer getInteger(HttpServletRequest request, String name) {
        String value = request.getParameter(name);

        if(value != null) {
            try {
                return Integer.parseInt(value);
            }
            catch(NumberFormatException e){}
        }

        return null;
    }

    /**
     * @param request
     * @param name
     * @param defaultValue
     * @return Integer
     */
    public static Integer getInteger(HttpServletRequest request, String name, int defaultValue) {
        String value = request.getParameter(name);

        if(value != null) {
            try {
                return Integer.parseInt(value);
            }
            catch(NumberFormatException e){}
        }

        return Integer.valueOf(defaultValue);
    }

    /**
     * @param request
     * @param name
     * @param defaultValue
     * @return Integer
     */
    public static int[] getIntegerValues(HttpServletRequest request, String name) {
        String[] values = request.getParameterValues(name);

        if(values != null && values.length > 0) {
            int index = 0;
            int[] result = new int[values.length];

            for(String value : values) {
                try {
                    result[index] = Integer.parseInt(value);
                    index++;
                }
                catch(NumberFormatException e) {
                }
            }

            if(index >= result.length) {
                return result;
            }

            int[] temp = new int[index];
            System.arraycopy(result, 0, temp, 0, index);
            return temp;
        }

        return new int[0];
    }

    /**
     * @param request
     * @param name
     * @return Float
     */
    public static Float getFloat(HttpServletRequest request, String name) {
        String value = request.getParameter(name);

        if(value != null) {
            try {
                return Float.parseFloat(value);
            }
            catch(NumberFormatException e){}
        }

        return null;
    }

    /**
     * @param request
     * @param name
     * @param defaultValue
     * @return Float
     */
    public static Float getFloat(HttpServletRequest request, String name, float defaultValue) {
        String value = request.getParameter(name);

        if(value != null) {
            try {
                return Float.parseFloat(value);
            }
            catch(NumberFormatException e){}
        }

        return Float.valueOf(defaultValue);
    }

    /**
     * @param request
     * @param name
     * @return Double
     */
    public static Double getDouble(HttpServletRequest request, String name) {
        String value = request.getParameter(name);

        if(value != null) {
            try {
                return Double.parseDouble(value);
            }
            catch(NumberFormatException e){}
        }

        return null;
    }

    /**
     * @param request
     * @param name
     * @param defaultValue
     * @return Double
     */
    public static Double getDouble(HttpServletRequest request, String name, double defaultValue) {
        String value = request.getParameter(name);

        if(value != null) {
            try {
                return Double.parseDouble(value);
            }
            catch(NumberFormatException e){}
        }

        return Double.valueOf(defaultValue);
    }

    /**
     * @param request
     * @param name
     * @return Double
     */
    public static Long getLong(HttpServletRequest request, String name) {
        String value = request.getParameter(name);

        if(value != null) {
            try {
                return Long.parseLong(value);
            }
            catch(NumberFormatException e){}
        }

        return null;
    }

    /**
     * @param request
     * @param name
     * @param defaultValue
     * @return Long
     */
    public static Long getLong(HttpServletRequest request, String name, long defaultValue) {
        String value = request.getParameter(name);

        if(value != null) {
            try {
                return Long.parseLong(value);
            }
            catch(NumberFormatException e){}
        }

        return Long.valueOf(defaultValue);
    }

    /**
     * @param request
     * @param name
     * @param defaultValue
     * @return Long
     */
    public static long[] getLongValues(HttpServletRequest request, String name) {
        String[] values = request.getParameterValues(name);

        if(values != null && values.length > 0) {
            int index = 0;
            long[] result = new long[values.length];

            for(String value : values) {
                try {
                    result[index] = Long.parseLong(value);
                    index++;
                }
                catch(NumberFormatException e) {
                }
            }

            if(index >= result.length) {
                return result;
            }

            long[] temp = new long[index];
            System.arraycopy(result, 0, temp, 0, index);
            return temp;
        }

        return new long[0];
    }

    /**
     * @param request
     * @param name
     * @return java.util.Date
     */
    public static Date getDate(HttpServletRequest request, String name) {
        String date = request.getParameter(name);
        String format = getFormat(date);

        if(date != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);

            try {
                return dateFormat.parse(date);
            }
            catch(ParseException e) {
            }
        }

        return null;
    }

    /**
     * @param request
     * @param name
     * @param format
     * @return Date
     */
    public static Date getDate(HttpServletRequest request, String name, String format) {
        String date = request.getParameter(name);

        if(date != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);

            try {
                return dateFormat.parse(date);
            }
            catch(ParseException e) {
            }
        }

        return null;
    }

    /**
     * @param request
     * @param name
     * @return Timestamp
     */
    public static Timestamp getTimestamp(HttpServletRequest request, String name) {
        java.util.Date date = getDate(request, name);

        if(date != null) {
            return new Timestamp(date.getTime());
        }

        return null;
    }

    /**
     * @param request
     * @param name
     * @param format
     * @return Timestamp
     */
    public static Timestamp getTimestamp(HttpServletRequest request, String name, String format) {
        java.util.Date date = getDate(request, name, format);

        if(date != null) {
            return new Timestamp(date.getTime());
        }

        return null;
    }

    /**
     * @param date
     * @return String
     */
    public static String getFormat(String date) {
        int length = date.length();

        String f1 = "HH:mm:ss";
        String f2 = "yyyy-MM-dd";
        String f3 = "HH:mm:ss SSS";
        String f4 = "yyyy-MM-dd HH:mm:ss";
        String f5 = "yyyy-MM-dd HH:mm:ss SSS";

        if(length <= f1.length()) {
            return f1;
        }
        else if(length <= f2.length()) {
            return f2;
        }
        else if(length <= f3.length()) {
            return f3;
        }
        else if(length <= f4.length()) {
            return f4;
        }
        else if(length <= f5.length()) {
            return f5;
        }
        return f3;
    }

    /**
     * @param request
     * @return Map<String, String[]>
     */
    public static Map<String, String[]> getParameterMap(HttpServletRequest request) {
        String name = null;
        String[] values = null;
        java.util.Enumeration<?> enumeration = request.getParameterNames();
        Map<String, String[]> parameters = new LinkedHashMap<String, String[]>();

        while(enumeration.hasMoreElements()) {
            name = (String)(enumeration.nextElement());
            values = request.getParameterValues(name);

            if(values != null) {
                parameters.put(name, values);
            }
        }

        return parameters;
    }

    /**
     * @param args
     * @return boolean
     */
    private static boolean isEmpty(String source) {
        return (source == null || source.trim().length() < 1);
    }

    /**
     * @param s1
     * @param s2
     * @return boolean
     */
    private static boolean equalsIgnoreCase(String s1, String s2) {
        if(s1 != null) {
            return s1.equalsIgnoreCase(s2);
        }

        return false;
    }
}
