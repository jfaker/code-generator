/*
 * $RCSfile: Workspace.java,v $$
 * $Revision: 1.1 $
 * $Date: 2013-10-15 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.config;

import com.skin.config.Config;
import com.skin.config.XmlConfigFactory;

/**
 * <p>Title: Workspace</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @version 1.0
 */
public class Workspace extends Config {
    private static final long serialVersionUID = 1L;
    private static final Workspace instance = XmlConfigFactory.getConfig("META-INF/conf/workspace.xml", Workspace.class);

    public static Workspace getInstance() {
        return instance;
    }
}
