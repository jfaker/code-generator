/*
 * $RCSfile: Parameter.java,v $$
 * $Revision: 1.1  $
 * $Date: 2010-5-28  $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.config;

/**
 * <p>Title: Parameter</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class Parameter {
    private String name;
    private String value;
    private String description;

    public Parameter() {
    }

    /**
     * @param name
     * @param value
     */
    public Parameter(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * @param name
     * @param value
     * @param description
     */
    public Parameter(String name, String value, String description) {
        this.name = name;
        this.value = value;
        this.description = description;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return this.value;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * @return String
     */
    @Override
    public String toString() {
        return toString("    ");
    }

    /**
     * @param indent
     * @return
     */
    public String toString(String indent) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(indent);
        buffer.append(indent).append("<parameter");

        if(this.description != null && this.description.trim().length() > 0) {
            buffer.append(">\r\n");
            buffer.append(indent).append(indent).append(indent);
            buffer.append("<name>").append((this.name != null ? this.name : "")).append("</name>\r\n");
            buffer.append(indent).append(indent).append(indent);
            buffer.append("<value>").append((this.value != null ? this.value : "")).append("</value>\r\n");
            buffer.append(indent).append(indent).append(indent);
            buffer.append("<description>").append((this.description != null ? this.description : "")).append("</description>\r\n");
            buffer.append(indent);
            buffer.append(indent).append("</parameter>\r\n");
        }
        else {
            buffer.append(" name=\"").append((this.name != null ? this.name : "")).append("\"");
            buffer.append(" value=\"").append((this.value != null ? this.value : "")).append("\"");
            buffer.append(" description=\"").append((this.description != null ? this.description : "")).append("\"");
            buffer.append("/>\r\n");
        }

        return buffer.toString();
    }
}
