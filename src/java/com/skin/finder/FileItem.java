/*
 * $RCSfile: FileItem.java,v $$
 * $Revision: 1.1 $
 * $Date: 2013-11-1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder;

/**
 * <p>Title: FileItem</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @version 1.0
 */
public class FileItem {
    private String name;
    private long length;
    private long lastModified;
    private boolean isFile;

    /**
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the length
     */
    public long length() {
        return this.length;
    }

    /**
     * @param length the length to set
     */
    public void setLength(long length) {
        this.length = length;
    }

    /**
     * @return the lastModified
     */
    public long lastModified() {
        return this.lastModified;
    }

    /**
     * @param lastModified the lastModified to set
     */
    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    /**
     * @return the isFile
     */
    public boolean isFile() {
        return this.isFile;
    }

    /**
     * @param isFile the isFile to set
     */
    public void setFile(boolean isFile) {
        this.isFile = isFile;
    }

    /**
     * @return the isFile
     */
    public boolean isDirectory() {
        return (this.isFile == false);
    }
}
