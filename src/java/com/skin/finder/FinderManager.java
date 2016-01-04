/*
 * $RCSfile: FinderManager.java,v $$
 * $Revision: 1.1 $
 * $Date: 2013-4-2 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Title: FinderManager</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class FinderManager {
    private String work;
    private static final Logger logger = LoggerFactory.getLogger(FinderManager.class);

    public FinderManager(String work) {
        this.work = work;
    }

    /**
     * @param request
     * @param response
     */
    public Map<String, Object> list(String src) {
        String path = this.getPath(src);

        if(path == null) {
            return null;
        }

        File dir = new File(path);
        String parent = this.getParent(dir);
        String temp = path.substring(this.work.length()).replace('\\', '/');
        File[] fileList = dir.listFiles();

        if(fileList != null) {
            if(System.getProperty("os.name").indexOf("Windows") < 0) {
                Arrays.sort(fileList, FileComparator.getInstance());
            }
        }

        Map<String, Object> context = new HashMap<String, Object>();
        context.put("work", this.work);
        context.put("path", temp);
        context.put("parent", parent);
        context.put("fileList", fileList);
        return context;
    }

    /**
     * @param workspace
     * @param src
     * @param listUrl
     * @param xmlUrl
     * @return String
     */
    public String getFolderXml(String workspace, String src, String listUrl, String xmlUrl) {
        String path = this.getPath(src);
        StringBuilder buffer = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        buffer.append("<tree>");

        if(path != null) {
            File dir = new File(path);
            File[] fileList = dir.listFiles();

            if(fileList != null) {
                if(System.getProperty("os.name").indexOf("Windows") < 0) {
                    Arrays.sort(fileList, FileComparator.getInstance());
                }

                String temp = path.substring(this.work.length()).replace('\\', '/');

                for(File file : fileList) {
                    if(file.isDirectory()) {
                        String url = this.encode(this.urlEncode(temp + "/" + file.getName(), "UTF-8"));
                        buffer.append("<treeNode");
                        buffer.append(" icon=\"folder.gif\"");
                        buffer.append(" value=\"");
                        buffer.append(this.encode(file.getName()));
                        buffer.append("\"");
                        buffer.append(" title=\"");
                        buffer.append(this.encode(file.getName()));
                        buffer.append("\"");
                        buffer.append(" href=\"");
                        buffer.append(listUrl);
                        buffer.append("&amp;workspace=");
                        buffer.append(this.encode(workspace));
                        buffer.append("&amp;path=");
                        buffer.append(url);
                        buffer.append("\"");

                        if(this.hasChildFolder(file)) {
                            buffer.append(" nodeXmlSrc=\"");
                            buffer.append(xmlUrl);
                            buffer.append("&amp;workspace=");
                            buffer.append(this.encode(workspace));
                            buffer.append("&amp;path=");
                            buffer.append(url);
                            buffer.append("\"");
                        }

                        buffer.append("/>");
                    }
                }
            }
        }
        buffer.append("</tree>");
        return buffer.toString();
    }

    /**
     * @param src
     */
    public int rename(String src, String name) {
        String path = this.getPath(src);

        if(path == null) {
            return 0;
        }

        File file = new File(path);
        File newFile = new File(file.getParent(), name);

        if(newFile.exists()) {
            return 0;
        }

        file.renameTo(newFile);
        return (newFile.exists() ? 1 : 0);
    }

    /**
     * @param src
     */
    public int delete(String src) {
        String path = this.getPath(src);

        if(path == null) {
            return 0;
        }

        File file = new File(path);

        if(file.isFile()) {
            try {
                boolean flag = file.delete();

                if(!flag) {
                    logger.warn("delete file " + file.getAbsolutePath() + " failed !");
                }
            }
            catch(Exception e) {
                logger.warn(e.getMessage(), e);
            }
        }
        else {
            this.delete(file);
        }

        return (file.exists() ? 0 : 1);
    }

    /**
     * @param file
     */
    public void delete(File file) {
        if(file.isDirectory()) {
            File[] files = file.listFiles();

            for(File f : files) {
                if(f.isDirectory()) {
                    delete(f);
                }
                else {
                    boolean flag = f.delete();

                    if(!flag) {
                        logger.warn("delete file " + file.getAbsolutePath() + " failed !");
                    }
                }
            }
        }

        boolean flag = file.delete();

        if(!flag) {
            logger.warn("delete file " + file.getAbsolutePath() + " failed !");
        }
    }

    /**
     * @param workspace
     * @param src
     * @return Object
     */
    public Object suggest(String workspace, String src) {
        String path = this.getPath(src);
        List<String> json = new ArrayList<String>();

        if(path != null) {
            File file = new File(path);
            File[] result = null;
            List<File> list = new ArrayList<File>();

            if(path.equals(this.work) || src.endsWith("/")) {
                result = file.listFiles();
            }
            else {
                String prefix = file.getName().toLowerCase();
                String encoding = System.getProperty("file.encoding");
                File[] fileList = file.getParentFile().listFiles();

                if(encoding.equals("UTF-8")) {
                    for(File f : fileList) {
                        String fileName = f.getName();

                        if(fileName.toLowerCase().startsWith(prefix)) {
                            list.add(f);
                        }
                    }
                }
                else {
                    try {
                        for(File f : fileList) {
                            String fileName = new String(f.getName().getBytes("UTF-8"), "UTF-8");

                            if(fileName.toLowerCase().startsWith(prefix)) {
                                list.add(f);
                            }
                        }
                    }
                    catch(UnsupportedEncodingException e) {
                    }
                }

                result = new File[list.size()];
                list.toArray(result);
            }

            if(result != null) {
                if(System.getProperty("os.name").indexOf("Windows") < 0) {
                    Arrays.sort(result, FileComparator.getInstance());
                }

                for(int i = 0; i < result.length; i++) {
                    json.add(result[i].getName());
                }
            }
        }

        return json;
    }

    /**
     * @param workspace
     * @param src
     * @param searchment
     * @return Object
     */
    public Object find(String workspace, String src, String searchment) {
        String path = this.getPath(src);
        List<File> result = new ArrayList<File>();

        if(path == null) {
            return result;
        }

        File file = new File(path);

        if(file.isDirectory() == false) {
            return result;
        }

        List<String> stack = new ArrayList<String>();
        stack.add(file.getAbsolutePath());

        for(int i = 0; i < stack.size(); i++) {
            File[] list = new File(stack.get(i)).listFiles();

            if(list != null) {
                for(File f : list) {
                    if(f.isDirectory()) {
                        stack.add(f.getAbsolutePath());
                    }

                    if(f.getName().toLowerCase().indexOf(searchment) > -1) {
                        result.add(f);
                    }
                }
            }
        }

        for(int i = stack.size() - 1; i > -1; i--) {
            File f = new File(stack.get(i));

            if(f.getName().toLowerCase().indexOf(searchment) > -1) {
                result.add(f);
            }
        }

        if(System.getProperty("os.name").indexOf("Windows") < 0) {
            File[] list = new File[result.size()];
            result.toArray(list);
            Arrays.sort(list, FileComparator.getInstance());
            return list;
        }

        return result;
    }

    /**
     * @param file
     * @return boolean
     */
    private boolean hasChildFolder(File file) {
        File[] fileList = file.listFiles();

        for(File f : fileList) {
            if(f.isDirectory()) {
                return true;
            }
        }

        return false;
    }

    /**
     * @param path
     * @return String
     */
    public String getPath(String path) {
        String temp = "";

        if(path != null) {
            temp = path.trim();
            temp = this.replace(temp, "\\", "/");
            temp = this.replace(temp, "//", "/");
        }

        if(temp.length() < 1 || temp.equals("/")) {
            return this.work;
        }

        try {
            File file = new File(this.work, temp);
            temp = file.getCanonicalPath();

            if(temp.startsWith(this.work) == false) {
                return null;
            }
        }
        catch(IOException e) {
        }

        return temp;
    }

    /**
     * @param file
     * @return String
     */
    public String getParent(File file) {
        String parent = this.normalize(file.getParent(), true);
        String temp = this.normalize(this.work, true);

        if(parent.length() >= temp.length()) {
            parent = parent.substring(temp.length());

            if(parent.length() < 1) {
                parent = "/";
            }
        }
        else {
            parent = null;
        }

        return parent;
    }

    /**
     * @param path
     * @param isDirectory
     * @return String
     */
    private String normalize(String path, boolean isDirectory) {
        if(path == null) {
            return "";
        }

        String temp = path.trim();
        temp = this.replace(temp, "\\", "/");
        temp = this.replace(temp, "//", "/");

        if(isDirectory) {
            if(temp.endsWith("/") == false) {
                temp = temp + "/";
            }
        }

        return temp;
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
        String temp = source;
        StringBuilder buffer = new StringBuilder();

        while(true) {
            while(true) {
                e = temp.indexOf(search, s);

                if(e == -1) {
                    buffer.append(temp.substring(s));
                    break;
                }
                buffer.append(temp.substring(s, e)).append(replacement);
                s = e + d;
            }

            String result = buffer.toString();
            e = result.indexOf(search, 0);

            if(e > -1) {
                s = 0;
                temp = result;
                buffer.setLength(0);
            }
            else {
                break;
            }
        }

        return buffer.toString();
    }

    /**
     * @param source
     * @return String
     */
    private String encode(String source) {
        if(source == null) {
            return "";
        }

        StringBuilder buffer = new StringBuilder();

        for(int i = 0, length = source.length(); i < length; i++) {
            char c = source.charAt(i);

            switch(c) {
                case '"':
                    buffer.append("&quot;");
                    break;
                case '<':
                    buffer.append("&lt;");
                    break;
                case '>':
                    buffer.append("&gt;");
                    break;
                case '&':
                    buffer.append("&amp;");
                    break;
                case '\'':
                    buffer.append("&#39;");
                    break;
                default:
                    buffer.append(c);
                    break;
            }
        }

        return buffer.toString();
    }

    /**
     * @param source
     * @param encoding
     * @return String
     */
    public String urlEncode(String source, String encoding) {
        try {
            return URLEncoder.encode(source, encoding);
        }
        catch(UnsupportedEncodingException e) {
        }

        return "";
    }

    /**
     * @return the work
     */
    public String getWork() {
        return this.work;
    }

    /**
     * @param work the work to set
     */
    public void setWork(String work) {
        this.work = work;
    }
}
