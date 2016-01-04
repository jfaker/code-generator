/*
 * $RCSfile: InsertAction.java,v $$
 * $Revision: 1.1 $
 * $Date: 2013-10-21 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.generator.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import com.skin.database.sql.Record;
import com.skin.database.sql.parser.InsertParser;
import com.skin.j2ee.action.BaseAction;
import com.skin.j2ee.annotation.UrlPattern;
import com.skin.j2ee.util.JsonUtil;

/**
 * <p>Title: InsertAction</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class InsertAction extends BaseAction {
    /**
     * @throws IOException
     * @throws ServletException
     */
    @UrlPattern("/editor/insert.html")
    public void list() throws IOException, ServletException {
        this.forward("/template/generator/data/insert.jsp");
    }

    @UrlPattern("/ajax/editor/insertParse.html")
    public void parse() throws IOException {
        String sql = this.getParameter("sql", "");
        InsertParser insertParser = new InsertParser();
        List<Record> recordList = insertParser.parse(sql);

        if(recordList != null && recordList.size() > 0) {
            Record record = recordList.get(0);
            JsonUtil.success(this.request, this.response, record);
        }
        JsonUtil.success(this.request, this.response, null);
    }
}
