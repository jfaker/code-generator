/*
 * $RCSfile: CreateAction.java,v $$
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

import com.skin.database.dialect.Dialect;
import com.skin.database.dialect.MySQLDialect;
import com.skin.database.sql.Table;
import com.skin.database.sql.parser.CreateParser;
import com.skin.j2ee.action.BaseAction;
import com.skin.j2ee.annotation.UrlPattern;
import com.skin.j2ee.util.JsonUtil;

/**
 * <p>Title: CreateAction</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class CreateAction extends BaseAction {
    /**
     * @throws IOException
     * @throws ServletException
     */
    @UrlPattern("/editor/create.html")
    public void list() throws IOException, ServletException {
        this.forward("/template/generator/data/create.jsp");
    }

    @UrlPattern("/ajax/editor/createParse.html")
    public void parse() throws IOException {
        String sql = this.getParameter("sql", "");
        Dialect dialect = new MySQLDialect();
        CreateParser createParser = new CreateParser(dialect);
        List<Table> tableList = createParser.parse(sql);

        if(tableList != null && tableList.size() > 0) {
            Table table = tableList.get(0);
            JsonUtil.success(this.request, this.response, table);
        }
        JsonUtil.success(this.request, this.response, null);
    }
}
