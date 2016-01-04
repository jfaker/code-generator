/*
 * $RCSfile: TailServlet.java,v $$
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
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>Title: TailServlet</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2014</p>
 * @author xuesong.net
 * @version 1.0
 */
public class TailServlet {
    protected ServletContext servletContext;

    public TailServlet() {
    }

    /**
     * @param servletContext
     */
    public TailServlet(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void service(HttpServletRequest request, HttpServletResponse response, File file) throws ServletException, IOException {
        int line = this.getInt(request, "line", -1);
        long position = this.getLong(request, "position", -1);
        FileTail tail = null;

        if(line < 0 && position < 0L) {
           line = 30;
        }

        if(line > -1) {
            tail = this.read(file, line);
        }
        else {
            tail = this.read(file, position);
        }

        this.callback(request, response, request.getParameter("callback"), tail);
    }

    /**
     * @param file
     * @param line
     * @param position
     * @return FileTail
     * @throws IOException
     */
    public FileTail read(File file, int line, long position) throws IOException {
        int count = line;

        if(count < 0 && position < 0L) {
            count = 30;
        }

        if(count > -1) {
            return this.read(file, count);
        }
        else {
            FileTail tail = this.read(file, position);

            if(tail.getPosition() < 0L) {
                return this.read(file, 30);
            }
            else {
                return tail;
            }
        }
    }

    /**
     * @param file
     * @param line
     * @return FileTail
     * @throws IOException
     */
    protected FileTail read(File file, int line) throws IOException {
        RandomAccessFile randomAccessFile = null;

        try {
            randomAccessFile = new RandomAccessFile(file, "r");
            byte LF = 0x0A;
            long length = randomAccessFile.length();
            long position = length;
            int count = 0;
            int bufferSize = 4096;
            byte[] buffer = new byte[bufferSize];

            while(true) {
                position = Math.max(position - bufferSize, 0);

                randomAccessFile.seek(position);
                int readBytes = randomAccessFile.read(buffer, 0, bufferSize);

                for(int i = readBytes - 1; i > -1; i--) {
                    if(buffer[i] == LF) {
                        count++;

                        if(count >= line) {
                            position += (i + 1);
                            break;
                        }
                    }
                }

                if(position < 1L || count >= line) {
                    break;
                }
            }

            return this.read(file, position);
        }
        finally {
            if(randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                }
                catch(IOException e) {
                }
            }
        }
    }

    /**
     * @param file
     * @param position
     * @return FileTail
     * @throws IOException
     */
    protected FileTail read(File file, long position) throws IOException {
        RandomAccessFile randomAccessFile = null;

        try {
            randomAccessFile = new RandomAccessFile(file, "r");
            long length = randomAccessFile.length();

            if(length < position) {
                FileTail tail = new FileTail();
                tail.setPosition(-1);
                tail.setLength(0);
                tail.setBuffer(null);
                return tail;
            }

            if(position > 0 && position <= length) {
                randomAccessFile.seek(position);
            }

            int bufferSize = (int)(length - position);

            if(length - position > 512L * 1024L) {
                bufferSize = 512 * 1024;
            }

            byte[] buffer = new byte[bufferSize];
            int readBytes = randomAccessFile.read(buffer, 0, bufferSize);

            FileTail tail = new FileTail();
            tail.setPosition(randomAccessFile.getFilePointer());
            tail.setLength(readBytes);
            tail.setBuffer(buffer);
            return tail;
        }
        finally {
            if(randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                }
                catch(IOException e) {
                }
            }
        }
    }

    /**
     * @param request
     * @param name
     * @param defaultValue
     * @return int
     */
    protected int getInt(HttpServletRequest request, String name, int defaultValue) {
        String value = request.getParameter(name);

        try {
            return Integer.parseInt(value);
        }
        catch(NumberFormatException e) {
        }

        return defaultValue;
    }

    /**
     * @param request
     * @param name
     * @param defaultValue
     * @return long
     */
    protected long getLong(HttpServletRequest request, String name, long defaultValue) {
        String value = request.getParameter(name);

        try {
            return Long.parseLong(value);
        }
        catch(NumberFormatException e) {
        }

        return defaultValue;
    }

    /**
     * @param request
     * @param response
     * @param callback
     * @param value
     */
    public void callback(HttpServletRequest request, HttpServletResponse response, String callback, FileTail fileTail) throws IOException {
        if(callback != null) {
            StringBuilder buffer = new StringBuilder();
            buffer.append(callback);
            buffer.append("(");
            buffer.append(this.toJson(fileTail));
            buffer.append(");");
            this.write(request, response, "text/javascript; charset=UTF-8", buffer.toString());
        }
        else {
            this.write(request, response, "text/javascript; charset=UTF-8", this.toJson(fileTail));
        }
    }

    /**
     * @param fileTail
     * @return String
     */
    public String toJson(FileTail fileTail) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("{");
        buffer.append("\"length\": ");
        buffer.append(fileTail.getLength());
        buffer.append(", \"position\": ");
        buffer.append(fileTail.getPosition());
        buffer.append(", \"content\": \"");
        buffer.append(this.escape(fileTail.getContent()));
        buffer.append("\"}");
        return buffer.toString();
    }

    /**
     * @param request
     * @param response
     * @param content
     */
    public void write(HttpServletRequest request, HttpServletResponse response, String contentType, String content) throws IOException {
        byte[] buffer = content.getBytes("UTF-8");
        response.setContentType(contentType);
        response.setContentLength(buffer.length);
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(buffer);
        outputStream.flush();
    }

    /**
     * @param source
     * @return String
     */
    public String escape(String source) {
        if(source == null) {
            return "";
        }

        char c;
        StringBuilder buffer = new StringBuilder();

        for(int i = 0, length = source.length(); i < length; i++) {
            c = source.charAt(i);

            switch (c) {
                case '\'': {
                    buffer.append("\\\'");break;
                }
                case '"': {
                    buffer.append("\\\"");break;
                }
                case '\r': {
                    buffer.append("\\r");break;
                }
                case '\n': {
                    buffer.append("\\n");break;
                }
                case '\t': {
                    buffer.append("\\t");break;
                }
                case '\b': {
                    buffer.append("\\b");break;
                }
                case '\f': {
                    buffer.append("\\f");break;
                }
                case '\\': {
                    buffer.append("\\\\");break;
                }
                default : {
                    buffer.append(c);break;
                }
            }
        }

        return buffer.toString();
    }
}
