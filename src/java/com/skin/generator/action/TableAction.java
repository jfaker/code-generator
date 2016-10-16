/*
 * $RCSfile: TableAction.java,v $$
 * $Revision: 1.1 $
 * $Date: 2013-3-26 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.generator.action;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;

import com.skin.generator.TableType;
import com.skin.generator.Template;
import com.skin.generator.TemplateConfig;
import com.skin.generator.TemplateParser;
import com.skin.generator.database.Column;
import com.skin.generator.database.Table;
import com.skin.generator.database.handler.TableHandler;
import com.skin.generator.datasource.ConnectionConfig;
import com.skin.generator.datasource.ConnectionConfigFactory;
import com.skin.generator.datasource.ConnectionManager;
import com.skin.j2ee.action.BaseAction;
import com.skin.j2ee.annotation.UrlPattern;

/**
 * <p>Title: TableAction</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class TableAction extends BaseAction {
    /**
     * @throws IOException 
     * @throws ServletException 
     */
    @UrlPattern("/table/list.html")
    public void list() throws IOException, ServletException {
        String templateConfig = this.request.getParameter("templateConfig");
        String connectionName = this.request.getParameter("connectionName");
        String[] types = TableType.getTypes(this.request.getParameterValues("type"));

        if(types.length < 1) {
            this.error(500, "\"type\" must be not null !");
            return;
        }

        if(templateConfig != null) {
            templateConfig = templateConfig.trim();
        }

        Connection connection = null;
        ConnectionConfig connectionConfig = null;

        if(connectionName != null) {
            connectionConfig = ConnectionConfigFactory.getByName(connectionName);
        }

        if(connectionConfig == null) {
            this.error(500, "\"connectionName\" must be not null !");
            return;
        }

        try {
            connection = ConnectionManager.getConnection(connectionConfig);
            TableHandler tableHandler = new TableHandler(connection);
            List<Table> tableList = tableHandler.getTableList(null, null, "%", types);
            this.request.setAttribute("templateConfig", templateConfig);
            this.request.setAttribute("connectionName", connectionName);
            this.request.setAttribute("tableList", tableList);
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        finally {
            ConnectionManager.close(connection);
        }

        this.forward("/template/generator/table.jsp");
    }

    /**
     * @throws IOException
     * @throws ServletException
     */
    @UrlPattern("/table/edit.html")
    public void edit() throws IOException, ServletException {
        String connectionName = this.request.getParameter("connectionName");
        String tableName = this.request.getParameter("tableName");
        String templateConfig = this.request.getParameter("templateConfig");
        ConnectionConfig connectionConfig = ConnectionConfigFactory.getByName(connectionName);

        if(connectionConfig != null) {
            Connection connection = null;

            try {
                if(templateConfig == null || templateConfig.trim().length() < 1) {
                    templateConfig = "template.default.xml";
                }

                String file = this.getServletContext().getRealPath("/config/" + templateConfig);
                connection = ConnectionManager.getConnection(connectionConfig);
                TableHandler tableHandler = new TableHandler(connection);
                Table table = tableHandler.getTable(tableName);
                List<Column> columns = table.listColumns();
                List<Template> templates = TemplateParser.parseTemplates(new File(file));
                List<String> templateConfigList = TemplateConfig.getTemplateConfigList(this.getServletContext().getRealPath("/config"));

                if(columns != null) {
                    for(Column column : columns) {
                        column.setTable(null);
                    }
                }

                this.request.setAttribute("table", table);
                this.request.setAttribute("columns", columns);
                this.request.setAttribute("templates", templates);
                this.request.setAttribute("connectionName", connectionName);
                this.request.setAttribute("tableName", tableName);
                this.request.setAttribute("templateConfig", templateConfig);
                this.request.setAttribute("templateConfigList", templateConfigList);
            }
            catch(SQLException e) {
                e.printStackTrace();
            }
            finally {
                ConnectionManager.close(connection);
            }
        }
        this.forward("/template/generator/editor.jsp");
    }
}
