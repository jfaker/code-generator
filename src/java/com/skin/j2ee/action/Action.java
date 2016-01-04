/*
 * $RCSfile: Action.java,v $$
 * $Revision: 1.1 $
 * $Date: 2009-7-14 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.j2ee.action;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>Title: Action</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public interface Action {
    /**
     * @throws ServletException
     * @throws IOException
     */
    public void init() throws ServletException, IOException;

    /**
     * @param servletContext
     */
    public void setServletContext(ServletContext servletContext);

    /**
     * @param request
     */
    public void setRequest(HttpServletRequest request);

    /**
     * @param response
     */
    public void setResponse(HttpServletResponse response);

    /**
     */
    public void release();
}
