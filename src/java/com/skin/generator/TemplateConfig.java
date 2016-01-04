/*
 * $RCSfile: TemplateConfig.java,v $$
 * $Revision: 1.1 $
 * $Date: 2013-12-17 $
 *
 * Copyright (C) 2008 WanMei, Inc. All rights reserved.
 *
 * This software is the proprietary information of WanMei, Inc.
 * Use is subject to license terms.
 */
package com.skin.generator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title: TemplateConfig</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @version 1.0
 */
public class TemplateConfig {
    /**
     * @param location
     * @return List<String>
     */
    public static List<String> getTemplateConfigList(String path) {
        File dir = new File(path);
        File[] files = dir.listFiles();
        List<String> list = new ArrayList<String>();

        if(files != null) {
            for(File file : files) {
                if(file.isFile()) {
                    String name = file.getName();

                    if(name.toLowerCase().endsWith(".xml")) {
                        list.add(name);
                    }
                }
            }
        }

        return list;
    }
}
