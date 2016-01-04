/*
 * $RCSfile: DomainConfig.java,v $$
 * $Revision: 1.1 $
 * $Date: 2013-5-3 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.config;

import com.skin.config.Config;
import com.skin.config.ConfigFactory;

/**
 * <p>Title: DomainConfig</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class DomainConfig extends Config {
    private static final long serialVersionUID = 1L;
    private static final DomainConfig instance = ConfigFactory.getConfig("META-INF/conf/domain.conf", DomainConfig.class);

    public static DomainConfig getInstance() {
        return instance;
    }
}
