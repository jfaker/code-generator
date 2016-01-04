/*
 * $RCSfile: FinderAction.java,v $$
 * $Revision: 1.1 $
 * $Date: 2013-10-14 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.action;

import java.io.IOException;

import javax.servlet.ServletException;

import com.skin.finder.servlet.FinderServlet;
import com.skin.j2ee.action.BaseAction;
import com.skin.j2ee.annotation.UrlPattern;

/**
 * <p>Title: FinderAction</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @version 1.0
 */
public class FinderAction extends BaseAction {
    /**
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("/finder/hello.html")
    public void hello() throws ServletException, IOException {
        new FinderServlet(this.getServletContext()).hello(this.getRequest(), this.getResponse());
    }

    /**
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("/finder/blank.html")
    public void blank() throws ServletException, IOException {
        new FinderServlet(this.getServletContext()).blank(this.getRequest(), this.getResponse());
    }

    /**
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("/finder/index.html")
    public void index() throws ServletException, IOException {
        new FinderServlet(this.getServletContext()).index(this.getRequest(), this.getResponse());
    }

    /**
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("/finder/tree.html")
    public void tree() throws ServletException, IOException {
        new FinderServlet(this.getServletContext()).tree(this.getRequest(), this.getResponse());
    }

    /**
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("/finder/getFolderXml.html")
    public void getFolderXml() throws ServletException, IOException {
        String listUrl = this.getContextPath() + "/finder/display.html?a=1";
        String xmlUrl = this.getContextPath() + "/finder/getFolderXml.html?a=1";
        new FinderServlet(this.getServletContext()).getFolderXml(this.getRequest(), this.getResponse(), listUrl, xmlUrl);
    }

    /**
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("/finder/display.html")
    public void display() throws ServletException, IOException {
        new FinderServlet(this.getServletContext()).display(this.getRequest(), this.getResponse());
    }

    /**
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("/finder/suggest.html")
    public void suggest() throws ServletException, IOException {
        new FinderServlet(this.getServletContext()).suggest(this.getRequest(), this.getResponse());
    }

    /**
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("/finder/rename.html")
    public void rename() throws ServletException, IOException {
        new FinderServlet(this.getServletContext()).rename(this.getRequest(), this.getResponse());
    }

    /**
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("/finder/delete.html")
    public void delete() throws ServletException, IOException {
        new FinderServlet(this.getServletContext()).delete(this.getRequest(), this.getResponse());
    }
}
