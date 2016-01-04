/*
 * $RCSfile: Template.java,v $$
 * $Revision: 1.1  $
 * $Date: 2010-5-23  $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.generator;

import java.util.HashMap;
import java.util.Map;

import com.skin.config.Getter;
import com.skin.config.Parameter;

/**
 * <p>Title: Template</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class Template extends Getter {
    private String name;
    private String path;
    private String output;
    private Map<String, Parameter> parameters;
    private boolean enabled;

    public Template() {
        this.parameters = new HashMap<String, Parameter>();
    }

    /**
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the template
     */
    public String getPath() {
        return this.path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return the output
     */
    public String getOutput() {
        return this.output;
    }

    /**
     * @param output the output to set
     */
    public void setOutput(String output) {
        this.output = output;
    }

    /**
     * @param name
     * @param value
     */
    public void setParameter(String name, String value) {
        this.setParameter(name, value, null);
    }

    /**
     * @param name
     * @param value
     * @param description
     */
    public void setParameter(Parameter parameter) {
        this.parameters.put(parameter.getName(), parameter);
    }

    /**
     * @param name
     * @param value
     * @param description
     */
    public void setParameter(String name, String value, String description) {
        Parameter parameter = this.getParameter(name);

        if(parameter == null) {
            parameter = new Parameter(name, value, description);
            this.parameters.put(name, parameter);
        }
        else {
            parameter.setValue(value);
            parameter.setDescription(description);
        }
    }

    /**
     * @param name
     * @return Parameter
     */
    public Parameter getParameter(String name) {
        return this.parameters.get(name);
    }

    /**
     * @return the parameters
     */
    public Map<String, Parameter> getParameters() {
        return this.parameters;
    }

    /**
     * @param parameters the parameters to set
     */
    public void setParameters(Map<String, Parameter> parameters) {
        this.parameters = parameters;
    }

    /**
     * @return the enabled
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * @param enabled the enabled to set
     */
    public boolean getEnabled() {
        return this.enabled;
    }

    /**
     * @return String
     */
    @Override
    public String toString() {
        return toString("    ");
    }

    /**
     * @param indent
     * @return
     */
    public String toString(String indent) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(indent).append("<template");
        buffer.append(" name=\"").append((this.name != null ? this.name : "")).append("\"");
        buffer.append(" path=\"").append((this.path != null ? this.path : "")).append("\"");
        buffer.append(" output=\"").append((this.output != null ? this.output : "")).append("\"");
        buffer.append(" enabled=\"").append(this.enabled).append("\"");
        buffer.append(">\r\n");

        for(Map.Entry<String, Parameter> entry : this.getParameters().entrySet()) {
            Parameter parameter = entry.getValue();
            buffer.append(parameter.toString(indent));
        }

        buffer.append(indent);
        buffer.append("</template>\r\n");
        return buffer.toString();
    }

    @Override
    public String getValue() {
        return null;
    }

    @Override
    public String getValue(String name) {
        Parameter parameter = this.parameters.get(name);

        return (parameter != null ? parameter.getValue() : null);
    }
}
