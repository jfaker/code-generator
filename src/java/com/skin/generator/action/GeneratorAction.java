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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.config.ConnectionConfig;
import com.skin.config.ConnectionConfigFactory;
import com.skin.database.dialect.Dialect;
import com.skin.database.dialect.MySQLDialect;
import com.skin.database.handler.TableHandler;
import com.skin.database.sql.Table;
import com.skin.database.sql.parser.CreateParser;
import com.skin.datasource.ConnectionManager;
import com.skin.generator.GenericGenerator;
import com.skin.generator.TableDefinition;
import com.skin.generator.Template;
import com.skin.generator.TemplateConfig;
import com.skin.generator.TemplateParser;
import com.skin.j2ee.action.BaseAction;
import com.skin.j2ee.annotation.UrlPattern;
import com.skin.j2ee.util.Response;

/**
 * <p>Title: GeneratorAction</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class GeneratorAction extends BaseAction {
    private static Logger logger = LoggerFactory.getLogger(GeneratorAction.class);

    /**
     * @param request
     * @param response
     */
    @UrlPattern({"/", "/generator/index.html", "/generator/frame.html"})
    public void frame() throws IOException, ServletException {
        this.forward("/template/generator/frame.jsp");
    }

    /**
     * @param request
     * @param response
     */
    @UrlPattern("/generator/database.html")
    public void database() throws IOException, ServletException {
        String templateConfig = this.request.getParameter("templateConfig");
        List<String> templateConfigList = TemplateConfig.getTemplateConfigList(this.getServletContext().getRealPath("/config"));

        if(templateConfig != null) {
            templateConfig = templateConfig.trim();

            if(templateConfig.length() > 0) {
                this.setAttribute("templateConfig", templateConfig);
            }
        }

        this.setAttribute("templateConfigList", templateConfigList);
        this.forward("/template/generator/database.jsp");
    }

    /**
     * @param request
     * @param response
     */
    @UrlPattern("/generator/generate.html")
    public void generate() throws IOException {
        String xml = this.request.getParameter("tableDefinition");

        if(logger.isDebugEnabled()) {
            logger.debug("tableDefinition: " + xml);
        }

        String message = null;
        TableDefinition tableDefinition = TemplateParser.parse(xml);

        if(tableDefinition != null) {
            Table table = tableDefinition.getTable();

            if(table != null) {
                String home = this.servletContext.getRealPath("/config/template");
                String work = this.servletContext.getRealPath("/gen");

                try {
                    this.writeUTF8(new File(work, table.getClassName() + ".xml").getCanonicalFile(), xml);
                    GenericGenerator generator = new GenericGenerator(home, work);
                    generator.setTemplates(tableDefinition.getTemplates());
                    generator.setEncoding(tableDefinition.getEncoding());
                    generator.generate(table);
                }
                catch(Exception e) {
                    logger.warn(e.getMessage(), e);
                }
            }
            else {
                message = "Can't get Table !";
            }
        }
        else {
            message = "Can't get TableDefinition !";
        }

        if(message != null) {
            Response.write(this.request, this.response, "{\"code\": 1, \"message\": \"" + message + "\"}");
        }
        else {
            Response.write(this.request, this.response, "{\"code\": 0, \"message\": \"\u751f\u6210\u6210\u529f\uff01\"}");
        }
    }

    /**
     * @param request
     * @param response
     */
    @UrlPattern("/generator/batch.html")
    public void batch() throws IOException {
        String[] tableNames = this.request.getParameterValues("tableName");
        String fileName = this.request.getParameter("fileName");
        String templateConfig = this.request.getParameter("templateConfig");

        if(templateConfig == null || templateConfig.trim().length() < 1) {
            templateConfig = "template.default.xml";
        }

        if(fileName != null && fileName.length() > 0) {
            this.batch(fileName, tableNames, templateConfig);
            return;
        }

        Connection connection = null;
        ConnectionConfig connectionConfig = null;
        String connectionName = this.request.getParameter("connectionName");

        if(connectionName != null) {
            connectionConfig = ConnectionConfigFactory.getByName(connectionName);
        }

        if(connectionConfig == null) {
            this.error(500, "\"connectionName\" must be not null !");
            return;
        }

        StringBuilder message = new StringBuilder();

        try {
            connection = ConnectionManager.getConnection(connectionConfig);
            TableHandler tableHandler = new TableHandler(connection);

            String home = this.servletContext.getRealPath("/config/template");
            String work = this.servletContext.getRealPath("/gen");
            String configFile = this.getServletContext().getRealPath("/config/" + templateConfig);
            List<Template> templates = TemplateParser.parseTemplates(new File(configFile));
            GenericGenerator generator = new GenericGenerator(home, work);
            generator.setTemplates(templates);

            for(String tableName : tableNames) {
                if(tableName == null) {
                    continue;
                }

                tableName = tableName.trim();

                if(tableName.length() < 1) {
                    continue;
                }

                String type = "TABLE";
                int k = tableName.indexOf(":");

                if(k > -1) {
                    type = tableName.substring(0, k);
                    tableName = tableName.substring(k + 1);
                }

                Table table = tableHandler.getTable(null, null, tableName, type);

                if(table != null) {
                    try {
                        generator.generate(table);
                    }
                    catch(Exception e) {
                        e.printStackTrace();
                        message.append(e.getMessage());
                    }
                }
                else {
                    message.append(tableName + " not exists!");
                }
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        finally {
            ConnectionManager.close(connection);
        }

        if(message.length() > 0) {
            Response.write(this.request, this.response, "{\"code\": 1, \"message\": \"" + message.toString() + "\"}");
        }
        else {
            Response.write(this.request, this.response, "{\"code\": 0, \"message\": \"\u751f\u6210\u6210\u529f\uff01\"}");
        }
    }

    /**
     * @param request
     * @param response
     */
    protected void batch(String fileName, String[] tableNames, String templateConfig) throws IOException {
        StringBuilder message = new StringBuilder();

        if(fileName != null) {
            File file = new File(this.servletContext.getRealPath("/WEB-INF/sqls/" + fileName));
            String source = SqlFileAction.getSource(file.getAbsolutePath());
            Dialect dialect = new MySQLDialect();
            CreateParser sqlParser = new CreateParser(dialect);
            List<Table> tableList = sqlParser.parse(source);
            Map<String, Table> tableMap = new HashMap<String, Table>();

            if(tableList != null && tableList.size() > 0) {
                for(Table table : tableList) {
                    tableMap.put(table.getTableName(), table);
                }
            }

            String home = this.servletContext.getRealPath("/config/template");
            String work = this.servletContext.getRealPath("/gen");
            String configFile = this.getServletContext().getRealPath("/config/" + templateConfig);
            List<Template> templates = TemplateParser.parseTemplates(new File(configFile));
            GenericGenerator generator = new GenericGenerator(home, work);
            generator.setTemplates(templates);

            for(String tableName : tableNames) {
                if(tableName == null) {
                    continue;
                }

                tableName = tableName.trim();

                if(tableName.length() < 1) {
                    continue;
                }

                int k = tableName.indexOf(":");

                if(k > -1) {
                    tableName = tableName.substring(k + 1);
                }

                Table table = tableMap.get(tableName);

                if(table != null) {
                    try {
                        generator.generate(table);
                    }
                    catch(Exception e) {
                        e.printStackTrace();
                        message.append(e.getMessage());
                    }
                }
                else {
                    message.append(tableName + " not exists!");
                }
            }
        }
        else {
            message.append("fileName == null");
        }

        if(message.length() > 0) {
            Response.write(this.request, this.response, "{\"code\": 1, \"message\": \"" + message.toString() + "\"}");
        }
        else {
            Response.write(this.request, this.response, "{\"code\": 0, \"message\": \"\u751f\u6210\u6210\u529f\uff01\"}");
        }
    }

    /**
     * @param file
     * @param text
     */
    protected void writeUTF8(File file, String text) {
        if(file.exists()) {
            // return;
        }

        File dir = file.getParentFile();

        if(!dir.exists()) {
            dir.mkdirs();
        }

        OutputStream outputStream = null;
        OutputStreamWriter writer = null;

        try {
            outputStream = new FileOutputStream(file);
            writer = new OutputStreamWriter(outputStream, "UTF-8");
            writer.write(text);
            writer.flush();
            outputStream.flush();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        finally {
            if(writer != null) {
                try {
                    writer.close();
                }
                catch(IOException e) {
                }
            }

            if(outputStream != null) {
                try {
                    outputStream.close();
                }
                catch(IOException e) {
                }
            }
        }
    }
}
