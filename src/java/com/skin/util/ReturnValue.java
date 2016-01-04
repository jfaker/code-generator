/*
 * $RCSfile: ReturnValue.java,v $$
 * $Revision: 1.1  $
 * $Date: 2009-01-22  $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.util;

/**
 * <p>Title: ReturnValue</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class ReturnValue<T> {
    private int code = 0;
    private String message;
    private T value;

    public ReturnValue() {
    }

    /**
     * @param code
     * @param message
     */
    public ReturnValue(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * @param code
     * @param message
     * @param value
     */
    public ReturnValue(int code, String message, T value) {
        this.code = code;
        this.message = message;
        this.value = value;
    }

    /**
     * @return the code
     */
    public int getCode() {
        return this.code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * @return the value
     */
    public T getValue() {
        return this.value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(T value) {
        this.value = value;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
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

    /**
     * @return String
     */
    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("{");
        buffer.append(" \"code\": ").append(this.code).append(",");
        buffer.append(" \"message\": \"").append(this.escape(this.message)).append("\",");

        if(this.message != null) {
            buffer.append(" \"value\": \"").append(this.escape(this.value.toString())).append("\"");
        }
        else {
            buffer.append(" \"value\": null");
        }

        buffer.append("}");
        return buffer.toString();
    }
}
