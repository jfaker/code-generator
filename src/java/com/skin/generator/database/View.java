/*
 * $RCSfile: View.java,v $
 * $Revision: 1.1  $
 * $Date: 2009-2-16  $
 *
 * Copyright (C) 2005 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */

package com.skin.generator.database;

import java.util.Iterator;

import com.skin.database.sql.Column;
import com.skin.database.sql.Table;

/**
 * <p>Title: View</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class View extends Table {
    /**
     * default
     */
    public View() {
        super();
        this.setTableType("VIEW");
    }

    /**
     * @param table
     */
    public View(Table table) {
        super(table);
        this.setTableType("VIEW");
    }

    /**
     * @param table
     */
    public void setTable(Table table) {
        if(table != null) {
            if(table.getPrimaryKeys() != null && !table.getPrimaryKeys().isEmpty()) {
                for(Iterator<Column> iterator = this.getPrimaryKeys().iterator(); iterator.hasNext();) {
                    Column c = new Column(iterator.next());
                    c.setTable(table);
                }
            }

            if(table.getColumns() != null && !table.getColumns().isEmpty()) {
                for(Iterator<Column> iterator = this.getColumns().iterator(); iterator.hasNext();) {
                    Column c = new Column(iterator.next());
                    c.setTable(table);
                }
            }
        }
    }
}
