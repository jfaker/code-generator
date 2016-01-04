/*
 * $RCSfile: TableHandlerTest.java,v $$
 * $Revision: 1.1 $
 * $Date: 2013-12-17 $
 *
 * Copyright (C) 2008 WanMei, Inc. All rights reserved.
 *
 * This software is the proprietary information of WanMei, Inc.
 * Use is subject to license terms.
 */
package test.com.skin.finder;

import java.sql.Connection;
import java.sql.SQLException;

import com.skin.config.ConnectionConfig;
import com.skin.config.ConnectionConfigFactory;
import com.skin.database.handler.TableHandler;
import com.skin.database.sql.Table;
import com.skin.datasource.ConnectionManager;

/**
 * <p>Title: TableHandlerTest</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @version 1.0
 */
public class TableHandlerTest {
    /**
     * @param args
     */
    public static void main(String[] args) {
        Connection connection = null;
        ConnectionConfig connectionConfig = ConnectionConfigFactory.getByName("root:***@fmbak//localhost");

        try {
            connection = ConnectionManager.getConnection(connectionConfig);
            TableHandler tableHandler = new TableHandler(connection);
            String tableName = "forum";
            Table table = tableHandler.getTable(null, null, tableName, "TABLE");
            System.out.println("tableName: " + table.getTableName());
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        finally {
            ConnectionManager.close(connection);
        }
    }
}