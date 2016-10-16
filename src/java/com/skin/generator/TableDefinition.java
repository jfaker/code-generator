/*
 * $RCSfile: TableDefinition.java,v $$
 * $Revision: 1.1  $
 * $Date: 2010-5-30  $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.generator;

import java.util.List;

import com.skin.generator.database.Table;

/**
 * <p>Title: TableDefinition</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class TableDefinition {
    private Table table;
    private String encoding;
    private List<Template> templates;

    /**
     * @return the table
     */
    public Table getTable() {
        return this.table;
    }

    /**
     * @param table the table to set
     */
    public void setTable(Table table) {
        this.table = table;
    }

    /**
     * @return the encoding
     */
    public String getEncoding() {
        return this.encoding;
    }

    /**
     * @param encoding the encoding to set
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     * @return the templates
     */
    public List<Template> getTemplates() {
        return this.templates;
    }

    /**
     * @param templates the templates to set
     */
    public void setTemplates(List<Template> templates) {
        this.templates = templates;
    }
}
