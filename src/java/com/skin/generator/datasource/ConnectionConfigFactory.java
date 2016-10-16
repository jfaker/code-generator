/*
 * $RCSfile: SiteConfigFactory.java,v $$
 * $Revision: 1.1  $
 * $Date: 2009-7-14  $
 *
 * Copyright (C) 2008 Drising, Inc. All rights reserved.
 *
 * This software is the proprietary information of Drising, Inc.
 * Use is subject to license terms.
 */
package com.skin.generator.datasource;

import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.skin.util.ClassUtil;

/**
 * <p>Title: ConfigFactory</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class ConnectionConfigFactory {
    private static Logger logger = LoggerFactory.getLogger(ConnectionConfigFactory.class);

    /**
     * @param name
     * @return ConnectionConfig
     */
    public static ConnectionConfig getByName(String name) {
        List<ConnectionConfig> connectionConfigList = ConnectionConfigFactory.getConnectionConfigs();

        if(connectionConfigList != null) {
            for(ConnectionConfig connectionConfig : connectionConfigList) {
                if(connectionConfig.getName().equals(name)) {
                    return connectionConfig;
                }
            }
        }

        return null;
    }

    /**
     * @return List<ConnectionConfig>
     */
    public static List<ConnectionConfig> getConnectionConfigs() {
        InputStream inputStream = ConnectionConfigFactory.class.getClassLoader().getResourceAsStream("META-INF/conf/datasource.xml");

        try {
            return ConnectionConfigFactory.getConnectionConfigs(new InputStreamReader(inputStream, "UTF-8"));
        }
        catch(Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * @param file
     * @return List<ConnectionConfig>
     */
    public static List<ConnectionConfig> getConnectionConfigs(File file) {
        FileReader reader = null;

        try {
            reader = new FileReader(file);
            List<ConnectionConfig> list = getConnectionConfigs(reader);
            return list;
        }
        catch(Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * @param reader
     * @return List<ConnectionConfig>
     * @throws Exception
     */
    public static List<ConnectionConfig> getConnectionConfigs(Reader reader) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(new InputSource(reader));
        Element element = document.getDocumentElement();
        NodeList childNodes = element.getChildNodes();
        List<ConnectionConfig> list = new ArrayList<ConnectionConfig>();

        for(int i = 0, length = childNodes.getLength(); i < length; i++) {
            Node node = childNodes.item(i);
            int nodeType = node.getNodeType();

            if(nodeType == Node.ELEMENT_NODE) {
                String nodeName = node.getNodeName();

                if(nodeName.equals("connection")) {
                    ConnectionConfig connectionConfig = new ConnectionConfig();
                    mapping(node, connectionConfig, false);
                    if(connectionConfig.getName() != null && connectionConfig.getName().trim().length() > 0) {
                        list.add(connectionConfig);
                    }
                }
            }
        }

        return list;
    }
    
    /**
     * @param node
     * @param object
     * @param capitalization
     */
    public static void mapping(Node node, Object object, boolean capitalization) {
        String name = null;
        String methodName = null;
        Class<?> type = object.getClass();
        Method[] methods = type.getMethods();
        NodeList childNodes = node.getChildNodes();

        for(int i = 0; i < methods.length; i++) {
            methodName = methods[i].getName();
            Class<?>[] parameterTypes = methods[i].getParameterTypes();

            if(methodName.startsWith("set") && parameterTypes.length == 1) {
                name = methodName.substring(3);
                name = (capitalization ? name : java.beans.Introspector.decapitalize(name));
                Object value = ClassUtil.cast(parameterTypes[0], getNodeContext(childNodes, name));

                if(value != null) {
                    try {
                        methods[i].invoke(object, new Object[]{value});
                    }
                    catch(IllegalArgumentException exception) {
                    }
                    catch(IllegalAccessException exception) {
                    }
                    catch(InvocationTargetException exception) {
                    }
                }
            }
        }
    }

    /**
     * @param childNodes
     * @param name
     * @return String
     */
    public static String getNodeContext(NodeList childNodes, String name) {
        for(int i = 0, length = childNodes.getLength(); i < length; i++) {
            Node node = childNodes.item(i);
            int nodeType = node.getNodeType();

            if(nodeType == Node.ELEMENT_NODE) {
                String nodeName = node.getNodeName();
                
                if(nodeName.equals(name)) {
                    return node.getTextContent();
                }
            }
        }

        return null;
    }
}
