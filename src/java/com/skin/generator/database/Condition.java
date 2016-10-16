/*
 * $RCSfile: Condition.java,v $
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

/**
 * <p>Title: Condition</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public interface Condition {
    /**
     * @param c1
     * @param c2
     * @param operator
     * @return String
     */
    String expression(Column c1, Column c2, Operator operator);
}
