/*
 * $RCSfile: Generator.java,v $
 * $Revision: 1.1  $
 * $Date: 2009-2-16  $
 *
 * Copyright (C) 2005 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */

package com.skin.generator;

import com.skin.generator.database.Table;

/**
 * <p>Title: Generator</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public interface Generator {
    /**
     * @param table
     * @throws Exception
     */
    public void generate(Table table) throws Exception;
}
