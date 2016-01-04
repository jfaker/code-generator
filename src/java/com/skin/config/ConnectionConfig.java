/*
 * $RCSfile: ConnectionConfig.java,v $$
 * $Revision: 1.1  $
 * $Date: 2010-6-3  $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.config;

/**
 * <p>Title: ConnectionConfig</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class ConnectionConfig {
    private String name = null;
    private String url = null;
    private String driverClass = null;
    private String userName = null;
    private String password = null;
    private String schema = null;
    private String catalog = null;

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
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return this.url;
    }

    /**
     * @param driverClass the driverClass to set
     */
    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    /**
     * @return the driverClass
     */
    public String getDriverClass() {
        return this.driverClass;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return this.userName;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * @param schema the schema to set
     */
    public void setSchema(String schema) {
        this.schema = schema;
    }

    /**
     * @return the schema
     */
    public String getSchema() {
        return this.schema;
    }

    /**
     * @param catalog the catalog to set
     */
    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    /**
     * @return the catalog
     */
    public String getCatalog() {
        return this.catalog;
    }

    /**
     * @return String
     */
    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("url: ").append(this.url).append("\r\n");
        buffer.append("driverClass: ").append(this.driverClass).append("\r\n");
        buffer.append("userName: ").append(this.userName).append("\r\n");
        buffer.append("password: ").append(this.password).append("\r\n");
        return buffer.toString();
    }
}
