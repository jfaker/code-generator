/*
 * $RCSfile: LeftJoin.java,v $
 * $Revision: 1.1  $
 * $Date: 2009-3-1  $
 *
 * Copyright (C) 2005 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.generator.database;

import com.skin.database.sql.Column;
import com.skin.database.sql.Table;


/**
 * <p>Title: LeftJoin</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public interface RightJoin extends Joinable {
    /**
     * @param table
     * @param column
     */
    @Override
    public void join(Table table, Column column);
}
