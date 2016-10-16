/*
 * $RCSfile: ConnectionManager.java,v $
 * $Revision: 1.1  $
 * $Date: 2009-3-1  $
 *
 * Copyright (C) 2005 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.generator.datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Title: ConnectionManager</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class ConnectionManager {
    private static Logger logger = LoggerFactory.getLogger(ConnectionManager.class);

    private ConnectionManager() {
    }

    /**
     * @param connectionConfig
     * @return Connection
     * @throws SQLException
     */
    public static Connection getConnection(ConnectionConfig connectionConfig) throws SQLException {
        return connect(connectionConfig);
    }

    /**
     * @param connectionConfig
     * @return Connection
     * @throws SQLException
     */
    public static Connection connect(ConnectionConfig connectionConfig) throws SQLException {
        String url = connectionConfig.getUrl();
        String driverClass = connectionConfig.getDriverClass();
        String userName = connectionConfig.getUserName();
        String password = connectionConfig.getPassword();
        String catalog = connectionConfig.getCatalog();
        String schema = connectionConfig.getSchema();

        if(logger.isDebugEnabled()) {
            logger.debug("");
            logger.debug("Connect To: " + url);
            logger.debug("Driver    : " + driverClass);
            logger.debug("UserName  : " + userName);
            logger.debug("Password  : " + password);
            logger.debug("Catalog   : " + catalog);
            logger.debug("Schema    : " + schema);
            logger.debug("");
        }

        if(url == null || (url = url.trim()).length() < 1) {
            throw new NullPointerException("URL is null");
        }

        if(driverClass == null || (driverClass = driverClass.trim()).length() < 1) {
            throw new NullPointerException("driverClass is null");
        }

        if(userName == null || (userName = userName.trim()).length() < 1) {
            userName = "";
        }

        if(password == null || (password = password.trim()).length() < 1) {
            password = "";
        }

        try {
            Class.forName(driverClass);
        }
        catch(ClassNotFoundException e) {
            throw new SQLException(driverClass + " Not Found !");
        }

        Properties properties = new Properties();
        properties.put("user", userName);
        properties.put("password", password);
        properties.put("remarksReporting", "true");
        return DriverManager.getConnection(url, properties);
    }

    /**
     * @param connection
     */
    public static void close(Connection connection) {
        if(connection != null) {
            try {
                if(connection.isClosed() == false) {
                    connection.close();
                }
            }
            catch(SQLException e) {
            }
        }
    }
}
