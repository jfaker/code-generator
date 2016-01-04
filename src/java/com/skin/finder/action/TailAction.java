/*
 * $RCSfile: TailAction.java,v $$
 * $Revision: 1.1 $
 * $Date: 2013-10-14 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.action;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;

import com.skin.finder.FinderManager;
import com.skin.finder.config.Workspace;
import com.skin.finder.servlet.TailServlet;
import com.skin.j2ee.action.BaseAction;
import com.skin.j2ee.annotation.UrlPattern;

/**
 * <p>Title: TailAction</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @version 1.0
 */
public class TailAction extends BaseAction {
    /**
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("/finder/tail.html")
    public void index() throws ServletException, IOException {
        String workspace = this.request.getParameter("workspace");
        String path = this.request.getParameter("path");
        String home = this.getWorkspace(workspace);
        FinderManager finderManager = new FinderManager(home);
        String filePath = finderManager.getPath(path);

        if(filePath == null) {
            throw new ServletException("Can't access !");
        }

        File file = new File(filePath);
        String encoding = this.request.getParameter("encoding");
        String parent = finderManager.getParent(file);
        String temp = filePath.substring(finderManager.getWork().length()).replace('\\', '/');

        this.setAttribute("workspace", workspace);
        this.setAttribute("work", finderManager.getWork());
        this.setAttribute("path", temp);
        this.setAttribute("parent", parent);
        this.setAttribute("encoding", encoding);
        this.forward("/template/finder/tail.jsp");
    }

    /**
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("/finder/getTail.html")
    public void getTail() throws ServletException, IOException {
        String workspace = this.request.getParameter("workspace");
        String path = this.request.getParameter("path");
        String home = this.getWorkspace(workspace);
        FinderManager finderManager = new FinderManager(home);
        String filePath = finderManager.getPath(path);

        if(filePath == null) {
            throw new ServletException("Can't access !");
        }

        File file = new File(filePath);
        new TailServlet(this.getServletContext()).service(this.getRequest(), this.getResponse(), file);
    }

    /**
     * @param name
     * @return String
     */
    public String getWorkspace(String name) {
        if(name == null) {
            throw new NullPointerException("workspace must be not null !");
        }

        Workspace workspace = Workspace.getInstance();
        String work = workspace.getValue(name.trim());

        if(work == null) {
            throw new NullPointerException("workspace must be not null !");
        }

        if(work.startsWith("file:")) {
            return new File(work.substring(5)).getAbsolutePath();
        }

        if(work.startsWith("contextPath:")) {
            return this.getServletContext().getRealPath(work.substring(12));
        }

        throw new NullPointerException("work directory error: " + work);
    }
}
