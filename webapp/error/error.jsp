<%@ page language="java" isErrorPage="true" pageEncoding="UTF-8"%>
<%@ page import="org.slf4j.Logger"%>
<%@ page import="org.slf4j.LoggerFactory"%>
<%
    String message = null;
    Logger log = LoggerFactory.getLogger(getClass());

    if(exception != null)
    {
        log.error("page error", exception);
        exception.printStackTrace(System.out);
    }
    else
    {
        Exception ex = (Exception)(request.getAttribute("exception"));

        if(ex != null)
        {
            log.error("page error", ex);
            ex.printStackTrace(System.out);
        }
        else if(request.getAttribute("javax.servlet.error.exception") != null)
        {
            log.error("page error", ((Exception)request.getAttribute("javax.servlet.error.exception")));
        }
        else
        {
            message = (String)(request.getAttribute("error"));
        }
    }

    if(message == null || message.length() < 1)
    {
        message = "未知错误, 请联系系统管理员 !";
    }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>页面出错</title>
<base target="_blank"/>
</head>
<body>
<div class="page_error">
    <div class="tit">
        <h4>您访问的页面出错</h4>
    </div>
    <div class="con">
        <div class="error_block">
            <h2><%=message%></h2>
        </div>
    </div>
</div>
</body>
</html>