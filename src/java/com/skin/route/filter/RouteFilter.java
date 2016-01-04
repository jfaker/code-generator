/*
 * $RCSfile: RouteFilter.java,v $$
 * $Revision: 1.1 $
 * $Date: 2013-10-21 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.route.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>Title: RouteFilter</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class RouteFilter implements Filter {
    private String page;

    public RouteFilter() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.page = filterConfig.getInitParameter("page");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        if(!(servletRequest instanceof HttpServletRequest) || !(servletResponse instanceof HttpServletResponse)) {
            throw new ServletException("TemplateFilter just supports HTTP requests");
        }
        servletRequest.getRequestDispatcher(this.page).forward(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }
}
