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
package com.skin.generator.database;

import java.util.ArrayList;
import java.util.List;

import com.skin.database.sql.Column;
import com.skin.database.sql.Table;

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

    /**
     * @return the tableName
     */
    public String getTableName() {
        return this.tableName;
    }

    /**
     * @param tableName the tableName to set
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * @return the tableType
     */
    public String getTableType() {
        return this.tableType;
    }

    /**
     * @param tableType the tableType to set
     */
    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    /**
     * @return the remarks
     */
    public String getRemarks() {
        return this.remarks;
    }

    /**
     * @param remarks the remarks to set
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * @return the primaryKeys
     */
    public List<Column> getPrimaryKeys() {
        return this.primaryKeys;
    }

    /**
     * @param primaryKeys the primaryKeys to set
     */
    public void setPrimaryKeys(List<Column> primaryKeys) {
        this.primaryKeys = primaryKeys;
    }

    /**
     * @return the columns
     */
    public List<Column> getColumns() {
        return this.columns;
    }

    /**
     * @param columns the columns to set
     */
    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    /**
     * @return Column
     */
    public Column getPrimaryKey() {
        if(this.primaryKeys != null && !this.primaryKeys.isEmpty()) {
            return this.primaryKeys.get(0);
        }
        return null;
    }

    /**
     * @return int
     */
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
     * @return List<Column>
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

    /**
     * @param table
     * @param column
     */
    @Override
    public void join(Table table, Column column) {
    }
}
