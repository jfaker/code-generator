/*
 * $RCSfile: TemplateAction.java,v $$
 * $Revision: 1.1 $
 * $Date: 2013-4-2 $
 *
 * Copyright (C) 2008 WanMei, Inc. All rights reserved.
 *
 * This software is the proprietary information of WanMei, Inc.
 * Use is subject to license terms.
 */
package com.skin.generator.action;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import com.skin.generator.Template;
import com.skin.generator.TemplateParser;
import com.skin.j2ee.action.BaseAction;
import com.skin.j2ee.annotation.UrlPattern;

/**
 * <p>Title: TemplateAction</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author chenyankui
 * @version 1.0
 */
public class TemplateAction extends BaseAction {
    /**
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("/template/getTemplate.html")
    public void getTemplate() throws ServletException, IOException {
        String path = this.request.getParameter("path");
        String file = this.getServletContext().getRealPath(path);

        try {
            File f = new File(file);

            if(f.getCanonicalPath().startsWith(this.getServletContext().getRealPath("/config"))) {
                List<Template> templates = TemplateParser.parseTemplates(f);
                this.request.setAttribute("templates", templates);
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        this.forward("/template/generator/template.jsp");
    }
}
