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

package com.skin.database.sql;

import java.util.Iterator;

/**
 * <p>Title: View</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class View extends Table {
    public View() {
        super();
        this.setTableType("VIEW");
    }

    public View(Table t) {
        super(t);
        this.setTableType("VIEW");
    }

    public void setTable(Table t) {
        if(t != null) {
            if(t.getPrimaryKeys() != null && !t.getPrimaryKeys().isEmpty()) {
                for(Iterator<Column> iterator = this.getPrimaryKeys().iterator(); iterator.hasNext();) {
                    Column c = new Column(iterator.next());
                    c.setTable(t);
                }
            }

            if(t.getColumns() != null && !t.getColumns().isEmpty()) {
                for(Iterator<Column> iterator = this.getColumns().iterator(); iterator.hasNext();) {
                    Column c = new Column(iterator.next());
                    c.setTable(t);
                }
            }
        }
    }
}
