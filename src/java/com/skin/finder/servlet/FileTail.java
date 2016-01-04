/*
 * $RCSfile: FileTail.java,v $$
 * $Revision: 1.1 $
 * $Date: 2010-04-28 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.servlet;

/**
 * <p>Title: FileTail</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class FileTail {
    private int length;
    private long position;
    private byte[] buffer;

    /**
     * @return the length
     */
    public int getLength() {

        return this.length;
    }

    /**
     * @param length the length to set
     */
    public void setLength(int length) {

        this.length = length;
    }

    /**
     * @return the position
     */
    public long getPosition() {
        return this.position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(long position) {
        this.position = position;
    }

    /**
     * @return the buffer
     */
    public byte[] getBuffer() {
        return this.buffer;
    }

    /**
     * @param buffer the buffer to set
     */
    public void setBuffer(byte[] buffer) {

        this.buffer = buffer;
    }

    /**
     * @return String
     */
    public String getContent() {
        if(this.buffer == null) {

            this.length = -1;
            return "";
        }
        return new String(this.buffer, 0, this.length);
    }
}
