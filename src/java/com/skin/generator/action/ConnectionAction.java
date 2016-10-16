/*
 * $RCSfile: GeneratorAction.java,v $$
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

import com.skin.generator.TableType;
import com.skin.generator.database.Table;
import com.skin.generator.database.handler.TableHandler;
import com.skin.generator.datasource.ConnectionConfig;
import com.skin.generator.datasource.ConnectionConfigFactory;
import com.skin.generator.datasource.ConnectionManager;
import com.skin.j2ee.action.BaseAction;
import com.skin.j2ee.annotation.UrlPattern;
import com.skin.j2ee.util.Response;
import com.skin.util.HtmlUtil;

/**
 * <p>Title: GeneratorAction</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class ConnectionAction extends BaseAction {
    /**
     * @throws IOException 
     */
    @UrlPattern("/database/getDatabaseXml.html")
    public void getDatabaseXml() throws IOException {
        StringBuilder buffer = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        buffer.append("<tree>");
        List<ConnectionConfig> connectionConfigList = ConnectionConfigFactory.getConnectionConfigs();

        if(connectionConfigList != null && connectionConfigList.size() > 0) {
            for(ConnectionConfig connectionConfig : connectionConfigList) {
                String connectionName = HtmlUtil.encode(connectionConfig.getName());
                
                buffer.append("<treeNode");
                buffer.append(" title=\"");
                buffer.append(connectionName);
                buffer.append("\" href=\"javascript:void(0)");
                buffer.append("\">");

                buffer.append("<treeNode");
                buffer.append(" icon=\"table.gif\"");
                buffer.append(" title=\"Tables\"");
                buffer.append(" href=\"/table/list.html?connectionName=");
                buffer.append(connectionName);
                buffer.append("&amp;type=TABLE\"");
                buffer.append(" nodeXmlSrc=\"");
                buffer.append("/database/getTableXml.html?connectionName=");
                buffer.append(connectionName);
                buffer.append("&amp;type=TABLE\"");
                buffer.append("/>");

                buffer.append("<treeNode");
                buffer.append(" icon=\"view.gif\"");
                buffer.append(" title=\"Views\"");
                buffer.append(" href=\"/table/list.html?connectionName=");
                buffer.append(connectionName);
                buffer.append("&amp;type=VIEW\"");
                buffer.append(" nodeXmlSrc=\"");
                buffer.append("/database/getTableXml.html?connectionName=");
                buffer.append(connectionName);
                buffer.append("&amp;type=VIEW\"");
                buffer.append("/>");
                buffer.append("</treeNode>");
            }
        }

        File file = new File(this.servletContext.getRealPath("/WEB-INF/sqls"));

        if(file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();

            if(files != null && files.length > 0) {
                for(File f : files) {
                    String name = f.getName().toLowerCase();

                    if(f.isFile() && name.endsWith(".sql")) {
                        String fileName = HtmlUtil.encode(f.getName());
                        buffer.append("<treeNode");
                        buffer.append(" icon=\"script.gif\"");
                        buffer.append(" title=\"");
                        buffer.append(fileName);
                        buffer.append("\"");
                        buffer.append(" href=\"/sql/list.html?fileName=");
                        buffer.append(fileName);
                        buffer.append("&amp;type=TABLE\"");
                        buffer.append(" nodeXmlSrc=\"");
                        buffer.append("/sql/getTableXml.html?fileName=");
                        buffer.append(fileName);
                        buffer.append("&amp;type=TABLE\"");
                        buffer.append("/>");
                    }
                }
            }
        }

        buffer.append("</tree>");
        this.response.setHeader("Cache-Control", "no-cache");
        this.response.setHeader("Pragma", "no-cache");
        this.response.setHeader("Expires", "0");
        Response.write(this.request, this.response, "text/xml; charset=UTF-8", buffer.toString());
    }

    /**
     * @throws IOException 
     */
    @UrlPattern("/database/getTableXml.html")
    public void getTableXml() throws IOException {
        String connectionName = this.request.getParameter("connectionName");
        String[] types = TableType.getTypes(this.request.getParameterValues("type"));
        ConnectionConfig connectionConfig = null;

        if(connectionName != null) {
            connectionConfig = ConnectionConfigFactory.getByName(connectionName);
        }

        StringBuilder buffer = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        buffer.append("<tree>");

        if(connectionConfig != null) {
            Connection connection = null;

            try {
                connection = ConnectionManager.getConnection(connectionConfig);
                TableHandler tableHandler = new TableHandler(connection);
                List<Table> tableList = tableHandler.listTable(null, null, "%", types);

                if(tableList != null && tableList.size() > 0) {
                    for(Table table : tableList) {
                        buffer.append("<treeNode");
                        buffer.append(" title=\"");
                        buffer.append(HtmlUtil.encode(table.getTableName()));
                        buffer.append("\"");
                        buffer.append(" href=\"");
                        buffer.append("/table/edit.html?connectionName=");
                        buffer.append(HtmlUtil.encode(connectionName));
                        buffer.append("&amp;tableName=");
                        buffer.append(HtmlUtil.encode(table.getTableName()));
                        buffer.append("\"/>");
                    }
                }
            }
            catch(SQLException e) {
                e.printStackTrace();
            }
            finally {
                ConnectionManager.close(connection);
            }
        }

        buffer.append("</tree>");
        this.response.setHeader("Cache-Control", "no-cache");
        this.response.setHeader("Pragma", "no-cache");
        this.response.setHeader("Expires", "0");
        Response.write(this.request, this.response, "text/xml; charset=UTF-8", buffer.toString());
    }
}
