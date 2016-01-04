/*
 * $RCSfile: XmlConfigFactory.java,v $$
 * $Revision: 1.1  $
 * $Date: 2009-7-14  $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

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

/**
 * <p>Title: XmlConfigFactory</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class XmlConfigFactory {
    private static final Logger logger = LoggerFactory.getLogger(XmlConfigFactory.class);

    /**
     * @return T
     */
    public static <T extends Config> T getConfig(String resource, Class<T> type) {
        InputStream inputStream = null;

        try {
            inputStream = XmlConfigFactory.class.getClassLoader().getResourceAsStream(resource);

            if(inputStream == null) {
                throw new IOException("The file \"classpath: " + resource + "\" not found !");
            }

            T config = type.newInstance();
            load(inputStream, config);
            return config;
        }
        catch(Exception e) {
            logger.warn(e.getMessage(), e);
        }
        finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
                }
                catch(IOException e) {
                }
            }
        }
        return null;
    }


    public static Map<String, String> load(InputStream inputStream, Map<String, String> map) throws Exception {
        try {
            Reader reader = new InputStreamReader(inputStream, "UTF-8");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setNamespaceAware(true);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(new InputSource(reader));
            Element element = document.getDocumentElement();
            NodeList childNodes = element.getChildNodes();

            for(int i = 0, length = childNodes.getLength(); i < length; i++) {
                Node node = childNodes.item(i);
                int nodeType = node.getNodeType();

                if(nodeType == Node.ELEMENT_NODE) {
                    String nodeName = node.getNodeName();

                    if(nodeName.equals("parameter")) {
                        String name = getAttribute(node, "name");

                        if(name != null) {
                            map.put(name, node.getTextContent());
                        }
                    }
                }
            }

        }
        catch(Exception e) {
            throw e;
        }

        return map;
    }

    /**
     * @param node
     * @param name
     * @return String
     */
    public static String getAttribute(Node node, String name) {
        NamedNodeMap map = node.getAttributes();

        if(map != null) {
            for(int i = 0, len = map.getLength(); i < len; i++) {
                Node n = map.item(i);

                if(name.equals(n.getNodeName())) {
                    return n.getNodeValue();
                }
            }
        }

        return null;
    }
}
