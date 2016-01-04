/*
 * $RCSfile: JsonUtil.java,v $$
 * $Revision: 1.1  $
 * $Date: 2013-4-5  $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.util;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Title: JsonUtil</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class JsonUtil
{
    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    /**
     * @param value
     * @return String
     */
    public static String stringify(Object value)
    {
        ObjectMapper mapper = new ObjectMapper();

        try
        {
            return mapper.writeValueAsString(value);
        }
        catch(Exception e)
        {
            logger.warn(e.getMessage(), e);
        }

        return "";
    }
}
