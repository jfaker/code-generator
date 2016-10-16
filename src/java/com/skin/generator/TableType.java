/*
 * $RCSfile: TablePattern.java,v $$
 * $Revision: 1.1  $
 * $Date: 2013-3-30  $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.generator;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * <p>Title: TablePattern</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class TableType {
    /**
     * @param args
     * @return String[]
     */
    public static String[] getTypes(String[] args) {
        String[] types = null;

        if(args != null && args.length > 0) {
            Map<String, String> map = new HashMap<String, String>();

            for(String type : args) {
                type = type.toUpperCase();

                if(type.equals("TABLE") || type.equals("VIEW")) {
                    map.put(type, "1");
                }
            }
            
            if(map.size() > 0) {
                Set<String> set = map.keySet();
                types = new String[set.size()];
                set.toArray(types);
            }
            else {
                types = new String[]{"TABLE"};
            }
        }

        if(types == null || types.length < 1) {
            types = new String[]{"TABLE"};
        }
        return types;
    }
}
