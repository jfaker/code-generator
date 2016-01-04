/*
 * $RCSfile: GenericGenerator.java,v $$
 * $Revision: 1.1  $
 * $Date: 2010-5-23  $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.generator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ognl.Ognl;
import ognl.OgnlException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.ayada.io.ChunkWriter;
import com.skin.ayada.template.TemplateContext;
import com.skin.ayada.template.TemplateManager;
import com.skin.database.sql.Column;
import com.skin.database.sql.Table;
import com.skin.util.IO;

/**
 * <p>Title: GenericGenerator</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class GenericGenerator implements Generator {
    private String home;
    private String work;
    private String encoding;
    private List<Template> templates;
    private static final Logger logger = LoggerFactory.getLogger(GenericGenerator.class);

    public GenericGenerator() {
        this.templates = new ArrayList<Template>();
    }

    public GenericGenerator(String home, String work) {
        this.home = home;
        this.work = work;
        this.templates = new ArrayList<Template>();
    }

    /**
     * @param templates
     */
    public GenericGenerator(List<Template> templates) {
        this.templates = new ArrayList<Template>();
        this.templates.addAll(templates);
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
        this.templates.clear();
        this.templates.addAll(templates);
    }

    @Override
    public void generate(Table table) throws Exception {
        this.generate(table, false);
    }

    /**
     * @param table
     * @param force
     * @throws Exception
     */
    public void generate(Table table, boolean force) throws Exception {
        if(this.templates != null && this.templates.size() > 0) {
            Map<String, Object> context = this.getContext(table);

            for(Template template : this.templates) {
                if(force || template.getEnabled()) {
                    this.generate(table, template, context);
                }
                else {
                    if(logger.isDebugEnabled()) {
                        logger.debug("Template: [" + template.getName() + "] disabled !");
                    }
                }
            }
        }
        else {
            if(logger.isDebugEnabled()) {
                logger.debug("no template available !");
            }
        }
    }

    /**
     * @param table
     * @param template
     * @throws Exception
     */
    public void generate(Table table, Template template, Map<String, Object> context) throws Exception {
        if(this.exists(new File(this.home, template.getPath()))) {
            String content = this.execute(template.getPath(), context);
            String output = this.evaluate(template.getOutput(), context);
            File file = new File(this.work, output);
            this.write(file, content, this.encoding);

            if(logger.isInfoEnabled()) {
                logger.info("OK " + table.getTableName() + ": " + file.getCanonicalPath());
            }
        }
        else {
            if(logger.isDebugEnabled()) {
                logger.debug("template: " + template.getPath() + " not found !");
            }
        }
    }

    /**
     * @param path
     * @param charset
     * @param context
     */
    public String execute(String path, Map<String, Object> context) {
        ChunkWriter writer = new ChunkWriter(4096);

        try {
            File file = new File(this.home, path);
            File parent = file.getCanonicalFile().getParentFile();
            String work = parent.getAbsolutePath();
            TemplateContext templateContext = TemplateManager.create(work);
            templateContext.execute(file.getName(), context, writer);
            templateContext.destory();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            IO.close(writer);
        }

        return this.ltrim(writer.toString());
    }

    /**
     * @param table
     * @return Map<String, Object>
     */
    public Map<String, Object> getContext(Table table) {
        if(table.getPrimaryKeys() == null || table.getPrimaryKeys().isEmpty()) {
            if(table.getColumns() != null && !table.getColumns().isEmpty()) {
                Column column = table.getColumns().get(0);
                table.addPrimaryKey(column);
                table.removeColumn(column);
            }
        }

        Map<String, Object> context = new HashMap<String, Object>();
        java.text.DateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = dateFormat.format(new java.util.Date(System.currentTimeMillis()));

        context.put("table", table);
        context.put("date", timestamp.substring(0, 10));
        context.put("timestamp", timestamp);
        context.put("description", table.getRemarks());
        context.put("author", System.getProperty("user.name"));

        if(table.getPrimaryKey() == null) {
            context.put("primaryKeyColumnName",    "NULL");
            context.put("primaryKeyDataTypeName",  "VARCHAR");
            context.put("primaryKeyJavaTypeName",  "String");
            context.put("primaryKeyVariableName",  "NULL");
            context.put("primaryKeyParameterName", "NULL");
            context.put("primaryKeyMethodSetter",  "setNull");
            context.put("primaryKeyMethodGetter",  "getNull");
        }
        else {
            String primaryKeyColumnName = table.getPrimaryKey().getColumnName();
            String primaryKeyDataTypeName = table.getPrimaryKey().getTypeName();
            String primaryKeyJavaTypeName = table.getPrimaryKey().getJavaTypeName();
            String primaryKeyVariableName = table.getPrimaryKey().getVariableName();
            String primaryKeyParameterName = table.getPrimaryKey().getVariableName();
            String primaryKeyMethodSetter = table.getPrimaryKey().getMethodSetter();
            String primaryKeyMethodGetter = table.getPrimaryKey().getMethodGetter();

            if(primaryKeyParameterName != null) {
                primaryKeyParameterName = Character.toUpperCase(primaryKeyVariableName.charAt(0)) + primaryKeyVariableName.substring(1);
            }

            context.put("primaryKeyColumnName",    primaryKeyColumnName);
            context.put("primaryKeyDataTypeName",  primaryKeyDataTypeName);
            context.put("primaryKeyJavaTypeName",  primaryKeyJavaTypeName);
            context.put("primaryKeyVariableName",  primaryKeyVariableName);
            context.put("primaryKeyParameterName", primaryKeyParameterName);
            context.put("primaryKeyMethodSetter",  primaryKeyMethodSetter);
            context.put("primaryKeyMethodGetter",  primaryKeyMethodGetter);
        }

        if(this.templates != null && !this.templates.isEmpty()) {
            String name = null;

            for(Template template : this.templates) {
                name = template.getName();

                if(name != null) {
                    context.put(name, template);
                }
            }
        }

        return context;
    }

    /**
     * @param content
     * @return String
     */
    protected String ltrim(String content) {
        int i = 0;
        int length = content.length();

        for(; i < length; i++) {
            char c = content.charAt(i);

            if(c > ' ') {
                break;
            }
        }

        if(i < 1) {
            return content;
        }
        return content.substring(i);
    }

    /**
     * @param file
     * @param content
     */
    protected void write(File file, String content, String encoding) {
        File parent = file.getParentFile();

        if(parent.exists() == false) {
            parent.mkdirs();
        }

        OutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(file);

            if(encoding == null || encoding.trim().length() < 1) {
                outputStream.write(content.getBytes("UTF-8"));
            }
            else {
                outputStream.write(content.getBytes(encoding));
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        finally {
            if(outputStream != null) {
                try {
                    outputStream.close();
                }
                catch(IOException e) {
                }
            }
        }
    }

    /**
     * @param file
     * @return boolean
     */
    protected boolean exists(File file) {
        if(file.exists() == false) {
            return false;
        }

        if(file.isFile() == false) {
            return false;
        }

        if(file.canRead() == false) {
            return false;
        }

        return true;
    }

    /**
     * @param context
     */
    protected void print(Map<String, Object> context) {
        for(Map.Entry<String, Object> entry : context.entrySet()) {
            String name = entry.getKey();
            Object value = entry.getValue();

            if(value instanceof String) {
                System.out.println(name + ": " + value);
            }
            else {
                System.out.println(name + ": " + (value != null ? value.getClass().getName() : "null"));
            }
        }
    }

    /**
     * @param source
     * @param context
     * @return String
     */
    public String evaluate(String source, Map<String, Object> context) {
        char c;
        char[] cbuf = source.toCharArray();
        StringBuilder buffer = new StringBuilder();

        for(int i = 0, length = cbuf.length; i < length; i++) {
            c = cbuf[i];

            if(c == '$' && (i + 1) < length && cbuf[i + 1] == '{') {
                StringBuilder expression = new StringBuilder();

                for(i = i + 2; i < length; i++) {
                    if(cbuf[i] == '}') {
                        break;
                    }
                    expression.append(cbuf[i]);
                }

                String content = expression.toString().trim();

                if(content.length() > 0) {
                    if(content.startsWith("?")) {
                        buffer.append("${");
                        buffer.append(content.substring(1));
                        buffer.append("}");
                    }
                    else {
                        try {
                            Object value = Ognl.getValue(expression.toString(), context, context);

                            if(value != null) {
                                buffer.append(value.toString());
                            }
                        }
                        catch(OgnlException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            else {
                buffer.append(c);
            }
        }

        return buffer.toString();
    }


    /**
     * @return the home
     */
    public String getHome() {
        return this.home;
    }

    /**
     * @param home the home to set
     */
    public void setHome(String home) {
        this.home = home;
    }

    /**
     * @return the work
     */
    public String getWork() {
        return this.work;
    }

    /**
     * @param work the work to set
     */
    public void setWork(String work) {
        this.work = work;
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

    public static void main(String[] args) {
        TableDefinition tableDefinition = TemplateParser.parse(new File("webapp/definition.xml"));
        GenericGenerator generator = new GenericGenerator();
        generator.setTemplates(tableDefinition.getTemplates());

        try {
            generator.generate(tableDefinition.getTable());
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
