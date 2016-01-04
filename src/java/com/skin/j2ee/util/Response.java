/*
 * $RCSfile: Response.java,v $$
 * $Revision: 1.1 $
 * $Date: 2012-11-4 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.j2ee.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>Title: Response</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class Response {
    /**
     * @param request
     * @param response
     * @param content
     */
    public static void write(HttpServletRequest request, HttpServletResponse response, String content) throws IOException {
        Response.write(request, response, "text/html; charset=UTF-8", content);
    }

    /**
     * @param request
     * @param response
     * @param content
     */
    public static void write(HttpServletRequest request, HttpServletResponse response, String contentType, String content) throws IOException {
        byte[] buffer = content.getBytes("UTF-8");
        response.setContentType(contentType);
        response.setContentLength(buffer.length);
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(buffer);
        outputStream.flush();
    }

    /**
     * @param request
     * @param response
     */
    public static void download(HttpServletRequest request, HttpServletResponse response, String fileName, File file) throws IOException {
        InputStream inputStream = null;

        try {
            inputStream = new FileInputStream(file);
            download(request, response, fileName, inputStream, file.length());
        }
        finally {
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
     * @param request
     * @param response
     * @throws IOException
     */
    public static void download(HttpServletRequest request, HttpServletResponse response, String fileName, InputStream inputStream, long contentLength) throws IOException {
        String contentType = MimeType.getMimeType(fileName);
        response.setContentType(contentType);
        response.setContentLength((int)contentLength);
        response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"");

        int length = 0;
        byte[] buffer = new byte[4096];
        OutputStream outputStream = response.getOutputStream();

        while((length = inputStream.read(buffer, 0, 4096)) > -1) {
            outputStream.write(buffer, 0, length);
        }

        outputStream.flush();
    }
}
