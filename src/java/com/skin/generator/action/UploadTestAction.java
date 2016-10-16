/*
 * $RCSfile: SqlFileAction.java,v $$
 * $Revision: 1.1  $
 * $Date: 2014-7-20  $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.generator.action;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.j2ee.action.BaseAction;
import com.skin.j2ee.annotation.UrlPattern;
import com.skin.j2ee.util.JsonUtil;
import com.skin.security.Digest;
import com.skin.util.Hex;

/**
 * <p>Title: SqlFileAction</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class UploadTestAction extends BaseAction {
    private static Logger logger = LoggerFactory.getLogger(UploadTestAction.class);

    /**
     * @throws IOException
     * @throws ServletException
     */
    @UrlPattern("/upload/getToken.html")
    public void test() throws IOException, ServletException {
    	Map<String, Object> result = new HashMap<String, Object>();
    	logger.info("method: " + this.request.getMethod() + " " + this.request.getRequestURI() + " " + this.request.getQueryString());
		String token = UUID.randomUUID().toString();
		result.put("token", token);
		result.put("success", true);
		result.put("message", "");

		logger.info(JsonUtil.stringify(result));
		JsonUtil.callback(this.request, this.response, result);
    }

    /**
     * @throws IOException
     * @throws ServletException
     */
    @UrlPattern("/upload/test1.html")
    public void test1() throws IOException, ServletException {
        logger.info("method: " + this.request.getMethod() + " " + this.request.getRequestURI() + " " + this.request.getQueryString());
        String home = this.servletContext.getRealPath("/WEB-INF/tmp");
        Map<String, Object> result = new HashMap<String, Object>();
        RandomAccessFile raf = null;

        try {
            Map<String, Object> map = this.parse(this.request);
            FileItem fileItem = (FileItem)map.get("fileData");
            fileItem.write(new File(home, fileItem.getName()));
            result.put("status", 200);
            result.put("message", "上传成功！");
            result.put("start", fileItem.getSize());
            JsonUtil.callback(this.request, this.response, result);
            return;
        }
        catch(Exception e) {
            logger.error(e.getMessage(), e);
            result.put("status", 500);
            result.put("message", "上传失败！");
            JsonUtil.callback(this.request, this.response, result);
            return;
        }
        finally {
            close(raf);
        }
    }

    /**
     * @throws IOException
     * @throws ServletException
     */
    @UrlPattern("/upload/test2.html")
    public void test2() throws IOException, ServletException {
    	logger.info("method: " + this.request.getMethod() + " " + this.request.getRequestURI() + " " + this.request.getQueryString());

        /**
         * cross domain support
         */
        this.doOptions(this.request, this.response);

    	/**
         * preflight
    	 */
    	if(this.request.getMethod().equalsIgnoreCase("OPTIONS")) {
    	    logger.info("options: " + this.request.getHeader("Origin"));
    	    return;
    	}

        String home = this.servletContext.getRealPath("/WEB-INF/tmp");
    	Map<String, Object> result = new HashMap<String, Object>();
    	RandomAccessFile raf = null;
    	InputStream inputStream = null;

        try {
        	Map<String, Object> map = this.parse(this.request);
        	int start = Integer.parseInt((String)map.get("start"));
        	int end = Integer.parseInt((String)map.get("end"));
        	int length = Integer.parseInt((String)map.get("length"));
        	FileItem fileItem = (FileItem)map.get("fileData");

        	inputStream = fileItem.getInputStream();
        	raf = new RandomAccessFile(new File(home, fileItem.getName()), "rw");
        	raf.seek(start);
        	String partMd5 = copy(inputStream, raf, 4096);

        	if(end >= length) {
        		String fileMd5 = Hex.encode(Digest.md5(new File(home, fileItem.getName())));
            	result.put("fileMd5", fileMd5);
        	}

        	result.put("status", 200);
        	result.put("message", "上传成功！");
        	result.put("start", end);
        	result.put("partMD5", partMd5);
            JsonUtil.callback(this.request, this.response, result);
            return;
        }
        catch(Exception e) {
        	logger.error(e.getMessage(), e);
        	result.put("status", 500);
        	result.put("message", "上传失败！");
            JsonUtil.callback(this.request, this.response, result);
            return;
        }
        finally {
        	close(raf);
            close(inputStream);
        }
    }
    
    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=utf-8");
        response.setHeader("Access-Control-Allow-Headers", "Content-Range,Content-Type");
        response.setHeader("Access-Control-Allow-Origin", "*"); // www.mytest.com 
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
    }

    /**
     * @param request
     * @return Map<String, Object>
     * @throws Exception
     */
    public Map<String, Object> parse(HttpServletRequest request) throws Exception {
    	String repository = System.getProperty("java.io.tmpdir");
    	int maxFileSize = 1024 * 1024 * 1024;
    	DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setRepository(new File(repository));
        factory.setSizeThreshold(1024 * 1024);
        ServletFileUpload servletFileUpload = new ServletFileUpload(factory);
        servletFileUpload.setFileSizeMax(maxFileSize);
        servletFileUpload.setSizeMax(maxFileSize);
        Map<String, Object> map = new HashMap<String, Object>();
    	List<?> list = servletFileUpload.parseRequest(request);

        if(list != null && list.size() > 0) {
            for(Iterator<?> iterator = list.iterator(); iterator.hasNext();) {
                FileItem item = (FileItem)iterator.next();

                if(item.isFormField()) {
                	logger.debug("form field: {}, {}", item.getFieldName(), item.toString());
                    map.put(item.getFieldName(), item.getString("utf-8"));
                }
                else {
                	logger.debug("file field: {}", item.getFieldName());
                	map.put(item.getFieldName(), item);
                }
            }
        }
        return map;
    }

    /**
     * 
     * @param inputStream
     * @param raf
     * @param bufferSize
     * @return String
     * @throws Exception
     */
    public static String copy(InputStream inputStream, RandomAccessFile raf, int bufferSize) throws Exception {
        int length = 0;
        byte[] bytes = new byte[Math.max(bufferSize, bufferSize)];
        MessageDigest messageDigest = MessageDigest.getInstance("md5");

        while((length = inputStream.read(bytes)) > -1) {
        	raf.write(bytes, 0, length);
        	messageDigest.update(bytes, 0, length);
        }
        return Hex.encode(messageDigest.digest());
    }

    /**
     * @param closeable
     */
    public static void close(Closeable closeable) {
        if(closeable != null) {
            try {
            	closeable.close();
            }
            catch(IOException e) {
            }
        }
    }
}
