/*
 * $RCSfile: JsonUtil.java,v $$
 * $Revision: 1.1 $
 * $Date: 2013-5-21 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.j2ee.jstl.util;

/**
 * <p>Title: JsonUtil</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class JsonUtil
{
	/***
	 * @param value
	 * @return String
	 */
    public String stringify(Object value)
    {
        return com.skin.util.JsonUtil.stringify(value);
    }
}
