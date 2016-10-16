/*
 * $RCSfile: Preference.java,v $$
 * $Revision: 1.1  $
 * $Date: 2010-6-7  $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.generator.config;

/**
 * <p>Title: Preference</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class Preference {
    private String editPage;

    /**
     * @param editPage
     */
    public void setEditPage(String editPage) {
        this.editPage = editPage;
    }

    /**
     * @return String
     */
    public String getEditPage() {
        return this.editPage;
    }
}
