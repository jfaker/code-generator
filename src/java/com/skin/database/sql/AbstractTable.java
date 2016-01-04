/*
 * $RCSfile: AbstractTable.java,v $
 * $Revision: 1.1  $
 * $Date: 2009-3-1  $
 *
 * Copyright (C) 2005 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.database.sql;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title: AbstractTable</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public abstract class AbstractTable implements Joinable {
    private String tableName;
    private String tableType;
    private String remarks;

    private List<Column> primaryKeys = new ArrayList<Column>();
    private List<Column> columns = new ArrayList<Column>();

    public String getTableName() {
        return this.tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableType() {
        return this.tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<Column> getPrimaryKeys() {
        return this.primaryKeys;
    }

    public void setPrimaryKeys(List<Column> primaryKeys) {
        this.primaryKeys = primaryKeys;
    }

    public List<Column> getColumns() {
        return this.columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public void addPrimaryKey(Column column) {
        this.primaryKeys.add(column);
    }

    public void addColumn(Column column) {
        this.columns.add(column);
    }

    public Column getPrimaryKey() {
        if(this.primaryKeys != null && !this.primaryKeys.isEmpty()) {
            return this.primaryKeys.get(0);
        }

        return null;
    }

    public int getColumnCount() {
        int count = 0;

        if(this.primaryKeys != null && !this.primaryKeys.isEmpty()) {
            count = count + this.primaryKeys.size();
        }

        if(this.columns != null && !this.columns.isEmpty()) {
            count = count + this.columns.size();
        }

        return count;
    }

    /**
     * @return
     */
    public List<Column> listColumns() {
        List<Column> list = new ArrayList<Column>();

        if(this.primaryKeys != null && !this.primaryKeys.isEmpty()) {
            list.addAll(this.primaryKeys);
        }

        if(this.columns != null && !this.columns.isEmpty()) {
            list.addAll(this.columns);
        }

        return list;
    }

    @Override
    public void join(Table table, Column column) {

    }

    public String toSqlString() {
        return null;
    }

    /**
     * @return String
     */
    @Override
    public String toString() {
        return this.toSqlString();
    }
}
