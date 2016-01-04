/*
 * $RCSfile: SqlFileAction.java,v $$
 * $Revision: 1.1  $
 * $Date: 2014-7-20  $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.generator.action;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.database.dialect.Dialect;
import com.skin.database.dialect.MySQLDialect;
import com.skin.database.sql.Column;
import com.skin.database.sql.Table;
import com.skin.database.sql.parser.CreateParser;
import com.skin.generator.Template;
import com.skin.generator.TemplateConfig;
import com.skin.generator.TemplateParser;
import com.skin.j2ee.action.BaseAction;
import com.skin.j2ee.annotation.UrlPattern;
import com.skin.j2ee.util.Response;
import com.skin.util.HtmlUtil;
import com.skin.util.IO;

/**
 * <p>Title: SqlFileAction</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class SqlFileAction extends BaseAction {
    private static Logger logger = LoggerFactory.getLogger(SqlFileAction.class);

    /**
     * @throws IOException
     * @throws ServletException
     */
    @UrlPattern("/sql/list.html")
    public void list() throws IOException, ServletException {
        String fileName = this.request.getParameter("fileName");
        String templateConfig = this.request.getParameter("templateConfig");

        if(templateConfig != null) {
            templateConfig = templateConfig.trim();
        }

        if(fileName != null) {
            File file = new File(this.servletContext.getRealPath("/WEB-INF/sqls/" + fileName));
            String source = getSource(file.getAbsolutePath());
            Dialect dialect = new MySQLDialect();
            CreateParser sqlParser = new CreateParser(dialect);
            List<Table> tableList = sqlParser.parse(source);

            this.request.setAttribute("templateConfig", templateConfig);
            this.request.setAttribute("fileName", fileName);
            this.request.setAttribute("tableList", tableList);
        }
        this.forward("/template/generator/table.jsp");
    }

    /**
     * @throws IOException
     */
    @UrlPattern("/sql/getTableXml.html")
    public void getTableXml() throws IOException {
        String fileName = this.request.getParameter("fileName");
        StringBuilder buffer = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        buffer.append("<tree>");
        logger.info("fileName: {}", fileName);

        if(fileName != null) {
            File file = new File(this.servletContext.getRealPath("/WEB-INF/sqls/" + fileName));
            String source = SqlFileAction.getSource(file.getAbsolutePath());
            Dialect dialect = new MySQLDialect();
            CreateParser sqlParser = new CreateParser(dialect);
            List<Table> tableList = sqlParser.parse(source);
            logger.debug("source: {}", source);

            if(tableList != null && tableList.size() > 0) {
                for(Table table : tableList) {
                    buffer.append("<treeNode");
                    buffer.append(" title=\"");
                    buffer.append(HtmlUtil.encode(table.getTableName()));
                    buffer.append("\"");
                    buffer.append(" href=\"");
                    buffer.append("/sql/edit.html?fileName=");
                    buffer.append(HtmlUtil.encode(fileName));
                    buffer.append("&amp;tableName=");
                    buffer.append(HtmlUtil.encode(table.getTableName()));
                    buffer.append("\"/>");
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
     * @throws ServletException
     */
    @UrlPattern("/sql/edit.html")
    public void edit() throws IOException, ServletException {
        String fileName = this.request.getParameter("fileName");
        String tableName = this.request.getParameter("tableName");
        String templateConfig = this.request.getParameter("templateConfig");

        if(fileName != null) {
            if(templateConfig == null || templateConfig.trim().length() < 1) {
                templateConfig = "template.default.xml";
            }

            String configFile = this.getServletContext().getRealPath("/config/" + templateConfig);
            File file = new File(this.servletContext.getRealPath("/WEB-INF/sqls/" + fileName));
            String source = SqlFileAction.getSource(file.getAbsolutePath());
            Dialect dialect = new MySQLDialect();
            CreateParser sqlParser = new CreateParser(dialect);
            List<Table> tableList = sqlParser.parse(source);
            Map<String, Table> tableMap = this.getTableMap(tableList);
            Table table = tableMap.get(tableName);

            List<Column> columns = table.listColumns();
            List<Template> templates = TemplateParser.parseTemplates(new File(configFile));
            List<String> templateConfigList = TemplateConfig.getTemplateConfigList(this.getServletContext().getRealPath("/config"));

            if(columns != null) {
                // JsonUtil.toJson(column);
                for(Column column : columns) {
                    column.setTable(null);
                }
            }

            this.request.setAttribute("table", table);
            this.request.setAttribute("columns", columns);
            this.request.setAttribute("templates", templates);
            this.request.setAttribute("tableName", tableName);
            this.request.setAttribute("fileName", fileName);
            this.request.setAttribute("templateConfig", templateConfig);
            this.request.setAttribute("templateConfigList", templateConfigList);
        }
        this.forward("/template/generator/editor.jsp");
    }

    /**
     * @throws IOException
     * @throws ServletException
     */
    @UrlPattern("/sql/insert.html")
    public void insert() throws IOException, ServletException {
        String fileName = this.request.getParameter("fileName");
        String tableName = this.request.getParameter("tableName");
        String templateConfig = this.request.getParameter("templateConfig");

        if(fileName != null) {
            if(templateConfig == null || templateConfig.trim().length() < 1) {
                templateConfig = "template.default.xml";
            }

            String configFile = this.getServletContext().getRealPath("/config/" + templateConfig);
            File file = new File(this.servletContext.getRealPath("/WEB-INF/sqls/" + fileName));
            String source = SqlFileAction.getSource(file.getAbsolutePath());
            Dialect dialect = new MySQLDialect();
            CreateParser sqlParser = new CreateParser(dialect);
            List<Table> tableList = sqlParser.parse(source);
            Map<String, Table> tableMap = this.getTableMap(tableList);
            Table table = tableMap.get(tableName);

            List<Column> columns = table.listColumns();
            List<Template> templates = TemplateParser.parseTemplates(new File(configFile));
            List<String> templateConfigList = TemplateConfig.getTemplateConfigList(this.getServletContext().getRealPath("/config"));

            if(columns != null) {
                // JsonUtil.toJson(column);
                for(Column column : columns) {
                    column.setTable(null);
                }
            }

            this.request.setAttribute("table", table);
            this.request.setAttribute("columns", columns);
            this.request.setAttribute("templates", templates);
            this.request.setAttribute("tableName", tableName);
            this.request.setAttribute("fileName", fileName);
            this.request.setAttribute("templateConfig", templateConfig);
            this.request.setAttribute("templateConfigList", templateConfigList);
        }
        this.forward("/template/generator/insert.jsp");
    }

    /**
     * @param tableList
     * @return Map<String, Table>
     */
    public Map<String, Table> getTableMap(List<Table> tableList) {
        Map<String, Table> map = new HashMap<String, Table>();

        if(tableList != null && tableList.size() > 0) {
            for(Table table : tableList) {
                map.put(table.getTableName(), table);
            }
        }
        return map;
    }

    /**
     * @param fileName
     * @return String
     */
    public static String getSource(String filePath) {
        try {
            if(filePath.endsWith(".link.sql")) {
                String content = IO.read(new File(filePath), "UTF-8", 4096);
                String[] list = content.split("\\n");
                StringBuilder buffer = new StringBuilder();

                for(String line : list) {
                    line = line.trim();

                    if(line.startsWith("#")) {
                        continue;
                    }

                    if(line.endsWith(".sql")) {
                        String sql = IO.read(new File(line), "UTF-8", 4096);
                        buffer.append(sql);
                    }
                }
                return buffer.toString();
            }
            else if(filePath.endsWith(".sql")) {
                return IO.read(new File(filePath), "UTF-8", 4096);
            }
            else {
                return "";
            }
        }
        catch(IOException e) {
            logger.warn(e.getMessage(), e);
        }
        return "";
    }
}
