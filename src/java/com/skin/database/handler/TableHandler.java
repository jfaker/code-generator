/*
 * $RCSfile: TableHandler.java,v $
 * $Revision: 1.1  $
 * $Date: 2009-2-16  $
 *
 * Copyright (C) 2005 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */

package com.skin.database.handler;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.database.dialect.Dialect;
import com.skin.database.sql.Column;
import com.skin.database.sql.Table;
import com.skin.util.ClassUtil;

/**
 * <p>Title: TableHandler</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class TableHandler {
    private static Logger logger = LoggerFactory.getLogger(TableHandler.class);
    private static boolean DEBUG = logger.isDebugEnabled();
    public static final String[] TABLE_TYPES = new String[]{"TABLE", "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS", "SYNONYM"};
    private Connection connection;
    private Dialect dialect;

    public static void main(String[] args) {
        System.out.println(" a b c ".replaceAll("[ ]", ""));
    }

    /**
     * @param connection
     */
    public TableHandler(Connection connection) {
        this.connection = connection;
        this.setDialect(this.getDialect(connection));
    }

    /**
     * @param connection
     */
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     * @param dialect
     */
    public void setDialect(Dialect dialect) {
        this.dialect = dialect;
    }

    /**
     * @return
     */
    public Dialect getDialect() {
        return this.dialect;
    }

    /**
     * @param t1
     * @param t2
     */
    public void addColumns(Table t1, Table t2) {
        if(t1.getPrimaryKey() == null) {
            t1.getPrimaryKeys().addAll(t2.getPrimaryKeys());
        }

        if(t1.getColumns() != null) {
            t1.getColumns().addAll(t2.getColumns());
        }
    }

    /**
     * @param tableName
     * @return
     */
    public Table getTable(String tableName) {
        return this.getTable(null, null, tableName);
    }

    /**
     * @param catalog
     * @param schemaPattern
     * @param tableName
     * @return
     */
    public Table getTable(String catalog, String schemaPattern, String tableName) {
        List<Table> list = this.getTableList(catalog, schemaPattern, tableName, new String[]{"TABLE", "VIEW"});

        if(list != null && !list.isEmpty()) {
            return list.get(0);
        }

        return null;
    }

    /**
     * @param catalog
     * @param schemaPattern
     * @param tableName
     * @param types
     * @return Table
     */
    public Table getTable(String catalog, String schemaPattern, String tableName, String types) {
        List<Table> list = this.getTableList(catalog, schemaPattern, tableName, new String[]{types});

        if(list != null && !list.isEmpty()) {
            return list.get(0);
        }

        return null;
    }

    /**
     * @param tableNamePattern
     * @param types
     * @return
     */
    public List<Table> listTable(String tableNamePattern, String[] types) {
        return listTable(null, null, tableNamePattern, types);
    }

    /**
     * @param catalog
     * @param schemaPattern
     * @param tableNamePattern
     * @param types
     * @return
     */
    public List<Table> listTable(String catalog, String schemaPattern, String tableNamePattern, String[] types) {
        List<Table> list = new ArrayList<Table>();
        ResultSet rs = null;

        try {
            DatabaseMetaData metaData = this.connection.getMetaData();

            if(DEBUG) {
                logger.debug("Catalog      : " + catalog);
                logger.debug("Schema       : " + schemaPattern);
                logger.debug("TablePattern : " + tableNamePattern);
                logger.debug("Types        : " + Arrays.toString(types) + "\r\n");
            }

            rs = metaData.getTables(catalog, schemaPattern, tableNamePattern, types);

            while(rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                String tableType = rs.getString("TABLE_TYPE");
                String tableRemark = rs.getString("REMARKS");
                String className = this.toCamel(tableName);
                String variableName = Character.toLowerCase(className.charAt(0)) + className.substring(1);

                if(tableName.toUpperCase().startsWith("JBPM_")) {
                    continue;
                }

                if(tableType.equals("TABLE")) {
                    logger.debug("TableName: " + tableName);
                }
                else if(tableType.equals("VIEW")) {
                    logger.debug("ViewName: " + tableName);
                }

                Table table = new Table();
                table.setTableCode(tableName);
                table.setTableName(tableName);
                table.setTableType(tableType);
                table.setRemarks(tableRemark);
                table.setQueryName(tableName);
                table.setClassName(className);
                table.setVariableName(variableName);

                list.add(table);
            }

            if(DEBUG) {
                logger.debug("-----------------------------------\r\n");
            }

            rs.close();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        finally {
            if(rs != null) {
                try {
                    rs.close();
                }
                catch(SQLException e){}
            }
        }

        return list;
    }

    /**
     * @param tableNamePattern
     * @param types
     * @return
     */
    public List<Table> listAllTable(String tableNamePattern, String[] types) {
        return listAllTable(null, null, tableNamePattern, types);
    }

    /**
     * @param catalog
     * @param schemaPattern
     * @param tableNamePattern
     * @param types
     * @return
     */
    public List<Table> listAllTable(String catalog, String schemaPattern, String tableNamePattern, String[] types) {
        List<Table> list = new ArrayList<Table>();

        ResultSet rs = null;

        try {
            DatabaseMetaData metaData = this.connection.getMetaData();

            if(DEBUG) {
                logger.debug("Catalog      : " + catalog);
                logger.debug("Schema       : " + schemaPattern);
                logger.debug("TablePattern : " + tableNamePattern);
                logger.debug("Types        : " + Arrays.toString(types) + "\r\n");
            }

            rs = metaData.getTables(catalog, schemaPattern, tableNamePattern, types);

            while(rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                String tableType = rs.getString("TABLE_TYPE");
                String tableRemark = rs.getString("REMARKS");
                String className = this.toCamel(tableName);
                String variableName = Character.toLowerCase(className.charAt(0)) + className.substring(1);

                if(tableType.equals("TABLE")) {
                    logger.debug("TableName: " + tableName);
                }
                else if(tableType.equals("VIEW")) {
                    logger.debug("ViewName: " + tableName);
                }

                Table table = new Table();
                table.setTableCode(tableName);
                table.setTableName(tableName);
                table.setTableType(tableType);
                table.setRemarks(tableRemark);
                table.setQueryName(tableName);
                table.setClassName(className);
                table.setVariableName(variableName);
                list.add(table);
            }

            if(DEBUG) {
                logger.debug("-----------------------------------\r\n");
            }

            rs.close();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        finally {
            if(rs != null) {
                try {
                    rs.close();
                }
                catch(SQLException e){}
            }
        }

        return list;
    }

    /**
     * @param catalog
     * @param schemaPattern
     * @param tableNamePattern
     * @param types
     * @return
     */
    public List<Table> getTableList(String catalog, String schemaPattern, String tableNamePattern, String[] types) {
        logger.debug("Catalog      : " + catalog);
        logger.debug("Schema       : " + schemaPattern);
        logger.debug("TablePattern : " + tableNamePattern);

        ResultSet rs = null;
        List<Table> list = new ArrayList<Table>();

        try {
            DatabaseMetaData metaData = this.connection.getMetaData();
            rs = metaData.getTables(catalog, schemaPattern, tableNamePattern, types);

            while(rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                String tableType = rs.getString("TABLE_TYPE");
                String tableRemark = rs.getString("REMARKS");
                String className = this.toCamel(tableName);
                String variableName = Character.toLowerCase(className.charAt(0)) + className.substring(1);

                if(tableName.toUpperCase().startsWith("JBPM_")) {
                    logger.debug("Ignore: " + tableName);
                    continue;
                }

                logger.debug("TableName    : " + tableName);

                Table table = new Table();
                table.setTableCode(tableName);
                table.setTableName(tableName);
                table.setTableType(tableType);
                table.setRemarks(tableRemark);
                table.setQueryName(tableName);
                table.setClassName(className);
                table.setVariableName(variableName);

                ResultSet rs2 = null;
                List<String> primaryKeys = new ArrayList<String>();

                try {
                    rs2 = metaData.getPrimaryKeys(null, null, tableName);

                    while(rs2.next()) {
                        String columnName = rs2.getString("COLUMN_NAME");
                        primaryKeys.add(columnName);
                    }
                }
                catch(SQLException e) {
                    // logger.debug("Warnning: " + e.getMessage());
                }
                finally {
                    if(rs2 != null) {
                        rs2.close();
                    }
                }

                rs2 = metaData.getColumns(null, schemaPattern, tableName, "%");

                List<String> columns = this.getColumns(rs2);
                boolean hasAutoIncreMent = columns.contains("IS_AUTOINCREMENT");

                while(rs2.next()) {
                    String columnName = rs2.getString("COLUMN_NAME");
                    String variable = java.beans.Introspector.decapitalize(this.toCamel(columnName));

                    if("ID".equals(variable)) {
                    }
                    else {
                        variable = Character.toLowerCase(variable.charAt(0)) + variable.substring(1);
                    }

                    int dataType = rs2.getInt("DATA_TYPE");
                    String typeName = rs2.getString("TYPE_NAME");

                    int columnSize = rs2.getInt("COLUMN_SIZE");
                    int decimalDigits = rs2.getInt("DECIMAL_DIGITS");

                    String columnDef = rs2.getString("COLUMN_DEF");
                    String columnRemark = rs2.getString("REMARKS");
                    logger.debug("Column       : " + columnName);

                    int nullable = rs2.getInt("NULLABLE");
                    String autoIncrement = (hasAutoIncreMent ? rs2.getString("IS_AUTOINCREMENT") : "NO");
                    Column column = new Column();
                    column.setTable(table);
                    column.setColumnCode(columnName);
                    column.setColumnName(columnName);
                    column.setDataType(dataType);
                    column.setTypeName(typeName);
                    column.setAutoIncrement(("NO".equals(autoIncrement) ? 0 : 1));
                    column.setColumnSize(columnSize);
                    column.setDecimalDigits(decimalDigits);
                    column.setNullable(nullable);
                    column.setRemarks(columnRemark);
                    column.setColumnDef(columnDef);
                    column.setJavaTypeName(this.dialect.convert(column));
                    column.setVariableName(variable);
                    column.setMethodSetter("set" + this.toCamel(columnName));
                    column.setMethodGetter("get" + this.toCamel(columnName));

                    if(primaryKeys.contains(columnName)) {
                        column.setPrimaryKey(true);
                        table.addPrimaryKey(column);
                    }
                    else {
                        column.setPrimaryKey(false);
                        table.addColumn(column);
                    }
                }

                if(table.getPrimaryKeys() == null || table.getPrimaryKeys().isEmpty()) {
                    if(table.getColumns() != null && !table.getColumns().isEmpty()) {
                        Column c = table.getColumns().get(0);

                        table.addPrimaryKey(c);
                        table.removeColumn(c);
                    }
                }

                if(DEBUG) {
                    logger.debug("-----------------------------------\r\n");
                }

                list.add(table);
                rs2.close();
            }

            rs.close();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        finally {
            if(rs != null) {
                try {
                    rs.close();
                }
                catch(SQLException e){}
            }
        }

        return list;
    }

    /**
     * @param connection
     * @return
     */
    public Dialect getDialect(Connection connection) {
        String productName = null;

        try {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            productName = databaseMetaData.getDatabaseProductName();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }

        if(productName == null) {
            return null;
        }

        String className = null;

        if(DEBUG) {
            logger.debug("DataBase ProductName: " + productName);
        }

        if((productName = productName.trim()).length() > 1) {
            productName = productName.replaceAll("[ ]", "").toLowerCase();
            String[] names = {"DB2", "HSQL", "MySQL", "Oracle", "Oracle11g", "Oracle10g", "Oracle9", "SQLServer", "Access"};

            for(int i = 0; i < names.length; i++) {
                if(productName.indexOf(names[i].toLowerCase()) > -1) {
                    className = Dialect.class.getName();
                    className = className.substring(0, className.length() - Dialect.class.getSimpleName().length());
                    className = className + names[i] + "Dialect";
                    break;
                }
            }
        }

        if(DEBUG) {
            logger.debug("Dialect: " + className + "\r\n");
        }

        Dialect dialect = null;

        try {
            dialect = (Dialect)(ClassUtil.getInstance(className, Dialect.class));
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return dialect;
    }

    /**
     * @param resultSet
     * @return List<String>
     * @throws SQLException
     */
    public List<String> getColumns(ResultSet resultSet) throws SQLException {
        List<String> columns = new ArrayList<String>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int count = metaData.getColumnCount();

        for(int i = 1; i <= count; i++) {
            columns.add(metaData.getColumnName(i).toUpperCase());
        }

        return columns;
    }

    /**
     * @param name
     * @return
     */
    public String toCamel(String name) {
        if(null == name || name.trim().length() < 1) {
            return "";
        }

        String[] subs = name.split("_");
        StringBuilder buffer = new StringBuilder();

        if(name.startsWith("_")) {
            buffer.append("_");
        }

        if(subs.length == 1) {
            String s = subs[0];

            if("ID".equals(s)) {
                buffer.append("Id");
            }
            else if(s.toUpperCase().equals(s)) {
                buffer.append(Character.toUpperCase(s.charAt(0)));
                buffer.append(s.substring(1).toLowerCase());
            }
            else {
                buffer.append(Character.toUpperCase(s.charAt(0))).append(s.substring(1));
            }
        }
        else {
            for(String s : subs) {
                if(s.length() > 0) {
                    if("ID".equals(s)) {
                        buffer.append(s);
                    }
                    else if(s.toUpperCase().equals(s)) {
                        buffer.append(Character.toUpperCase(s.charAt(0)));
                        buffer.append(s.substring(1).toLowerCase());
                    }
                    else {
                        buffer.append(Character.toUpperCase(s.charAt(0))).append(s.substring(1));
                    }
                }
            }
        }

        if(name.endsWith("_")) {
            buffer.append("_");
        }

        return buffer.toString();
    }

    public void print(List<Table> list) {
        for(Iterator<Table> iterator = list.iterator(); iterator.hasNext();) {
            Table table = iterator.next();
            System.out.println(table.toString());
        }
    }

    public void print(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        for(int i = 1; i <= columnCount; i++) {
            System.out.println(metaData.getColumnName(i));
        }
    }
}
