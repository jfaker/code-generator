/*
 * $RCSfile: TemplateFactory.java,v $
 * $Revision: 1.1  $
 * $Date: 2010-05-28  $
 *
 * Copyright (C) 2005 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.generator;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.skin.config.Parameter;
import com.skin.database.sql.Column;
import com.skin.database.sql.Table;

/**
 * <p>Title: TemplateFactory</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class TemplateParser {
    private static Logger logger = LoggerFactory.getLogger(TemplateParser.class);

    private TemplateParser() {
    }

    /**
     * @param xml
     * @return TableDefinition
     */
    public static TableDefinition parse(String xml) {
        return parse(new StringReader(xml));
    }

    /**
     * @param file
     * @return TableDefinition
     */
    public static TableDefinition parse(File file) {
        Reader reader = null;

        try {
            reader = new FileReader(file);
            return parse(reader);
        }
        catch(IOException e) {
            logger.warn(e.getMessage(), e);
        }
        finally {
            if(reader != null) {
                try {
                    reader.close();
                }
                catch(IOException e) {
                }
            }
        }
        return null;
    }

    /**
     * @param reader
     * @return TableDefinition
     */
    public static TableDefinition parse(Reader reader) {
        TableDefinition tableDefinition = new TableDefinition();

        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setNamespaceAware(true);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(new InputSource(reader));
            Element element = document.getDocumentElement();
            NodeList childNodes = element.getChildNodes();

            Table table = null;
            List<Template> templates = null;
            String encoding = element.getAttribute("encoding");

            for(int i = 0, length = childNodes.getLength(); i < length; i++) {
                Node node = childNodes.item(i);
                int nodeType = node.getNodeType();

                if(nodeType == Node.ELEMENT_NODE) {
                    String nodeName = node.getNodeName();

                    if(nodeName.equals("table")) {
                        table = TemplateParser.parseTable(node);
                    }
                    else if(nodeName.equals("templates")) {
                        templates = TemplateParser.parseTemplates(node);
                    }
                }
            }

            tableDefinition.setEncoding(encoding);
            tableDefinition.setTable(table);
            tableDefinition.setTemplates(templates);
        }
        catch(Exception e) {
            logger.warn(e.getMessage(), e);
        }
        finally {
        }

        return tableDefinition;
    }

    /**
     * @param element
     * @return Table
     */
    public static Table parseTable(Node node) {
        Table table = new Table();
        table.setAlias(getAttribute(node, "alias", ""));
        table.setTableName(getAttribute(node, "tableName", ""));
        table.setTableCode(getAttribute(node, "tableCode", ""));
        table.setTableType(getAttribute(node, "tableType", ""));
        table.setRemarks(getAttribute(node, "remarks", ""));
        table.setQueryName(getAttribute(node, "queryName", ""));
        table.setClassName(getAttribute(node, "className", ""));
        table.setVariableName(getAttribute(node, "variableName", ""));
        NodeList childNodes = node.getChildNodes();

        for(int i = 0, length = childNodes.getLength(); i < length; i++) {
            Node item = childNodes.item(i);
            int nodeType = node.getNodeType();

            if(nodeType == Node.ELEMENT_NODE) {
                Column column = new Column();
                column.setAlias(getAttribute(item, "alias", ""));
                column.setColumnName(getAttribute(item, "columnName", ""));
                column.setColumnCode(getAttribute(item, "columnCode", ""));
                column.setDataType(getIntAttribute(item, "dataType", 0));
                column.setTypeName(getAttribute(item, "typeName", ""));
                column.setColumnSize(getIntAttribute(item, "columnSize", 0));
                column.setAutoIncrement(getIntAttribute(item, "autoIncrement", 0));
                column.setDecimalDigits(getIntAttribute(item, "decimalDigits", 0));
                column.setRemarks(getAttribute(item, "remarks", ""));
                column.setNullable(getIntAttribute(item, "mullable", 0));
                column.setPrecision(getIntAttribute(item, "precision", 0));
                column.setVariableName(getAttribute(item, "variableName", ""));
                column.setJavaTypeName(getAttribute(item, "javaTypeName", ""));
                column.setMethodSetter(getAttribute(item, "methodSetter", ""));
                column.setMethodGetter(getAttribute(item, "methodGetter", ""));
                table.addColumn(column);
            }
        }

        return table;
    }

    /**
     * @param file
     * @return List<Template>
     */
    public static List<Template> parseTemplates(File file) {
        Reader reader = null;

        try {
            reader = new FileReader(file);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setNamespaceAware(true);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(new InputSource(reader));
            Element element = document.getDocumentElement();
            return TemplateParser.parseTemplates(element);
        }
        catch(Exception e) {
            e.printStackTrace();
            logger.warn(e.getMessage(), e);
        }
        finally {
            if(reader != null) {
                try {
                    reader.close();
                }
                catch(IOException e) {
                }
            }
        }
        return null;
    }

    /**
     * @param file
     * @return List<Template>
     */
    public static List<Template> parseTemplates(InputStream inputStream) {
        try {
            return parseTemplates(new InputStreamReader(inputStream));
        }
        catch(Exception e) {
            logger.warn(e.getMessage(), e);
        }

        return null;
    }

    /**
     * @param reader
     * @return List<Template>
     */
    public static List<Template> parseTemplates(Reader reader) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setNamespaceAware(true);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(new InputSource(reader));
            Element element = document.getDocumentElement();
            return TemplateParser.parseTemplates(element);
        }
        catch(Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return null;
    }

    /**
     * @param element
     * @return List<Template>
     */
    public static List<Template> parseTemplates(Node node) {
        String name = null;
        String path = null;
        String output = null;
        String enabled = null;
        List<Template> list = new ArrayList<Template>();
        NodeList childNodes = node.getChildNodes();

        for(int i = 0, length = childNodes.getLength(); i < length; i++) {
            Node item = childNodes.item(i);
            int nodeType = item.getNodeType();
            String nodeName = item.getNodeName();

            if(nodeType != Node.ELEMENT_NODE || !nodeName.equals("template")) {
                continue;
            }

            name = getAttribute(item, "name", "");
            path = getAttribute(item, "path", "");
            output = getAttribute(item, "output", "");
            enabled = getAttribute(item, "enabled", "");

            if(name == null || (name = name.trim()).length() < 1) {
                continue;
            }

            if(path == null || (path = path.trim()).length() < 1) {
                continue;
            }

            Template template = new Template();
            template.setName(name);
            template.setPath(path);
            template.setOutput(output);
            template.setEnabled((enabled != null && enabled.equals("true")));
            List<Parameter> parameters = getParameterList(item);

            for(Parameter parameter : parameters) {
                template.setParameter(parameter);
            }

            list.add(template);
        }

        return list;
    }

    /**
     * @param node
     * @return List<Parameter>
     */
    private static List<Parameter> getParameterList(Node node) {
        NodeList childNodes = node.getChildNodes();
        List<Parameter> list = new ArrayList<Parameter>();

        for(int i = 0, length = childNodes.getLength(); i < length; i++) {
            Node item = childNodes.item(i);
            int nodeType = item.getNodeType();
            String nodeName = item.getNodeName();

            if(nodeType == Node.ELEMENT_NODE && nodeName.equals("parameter")) {
                String name = getAttribute(item, "name", null);
                String value = getAttribute(item, "value", null);
                String description = getAttribute(item, "description", null);

                if(name != null && name.trim().length() > 0) {
                    list.add(new Parameter(name.trim(), value, description));
                }
            }
        }
        return list;
    }

    /**
     * @param node
     * @param name
     * @param defaultValue
     * @return String
     */
    private static String getAttribute(Node node, String name, String defaultValue) {
        NamedNodeMap attributes = node.getAttributes();

        if(attributes != null) {
            Node item = attributes.getNamedItem(name);

            if(item != null) {
                return item.getTextContent();
            }
        }

        return defaultValue;
    }

    /**
     * @param element
     * @param name
     * @param defaultValue
     * @return int
     */
    private static int getIntAttribute(Node node, String name, int defaultValue) {
        String value = getAttribute(node, name, null);

        if(value != null) {
            try {
                return Integer.parseInt(value);
            }
            catch(NumberFormatException e) {
            }
        }

        return defaultValue;
    }
}
