<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content="no-cache"/>
<meta http-equiv="Expires" content="0"/>
<title>Finder - ${workspace}</title>
<style type="text/css">
html,body{margin: 0px; padding: 0px; border: 0px;}
</style>
</head>
<frameset id="topFrame" name="topFrame" cols="240, *" border="5" frameborder="yes" bordercolor="#c0c0c0" framespacing="5" topmargin="0" leftmargin="0" marginheight="0" marginwidth="0">
    <frame id="leftFrame" name="leftFrame" src="/finder/tree.html?workspace=${workspace}" scrolling="auto"></frame>
    <frame id="mainFrame" name="mainFrame" src="/finder/display.html?workspace=${workspace}" scrolling="auto"></frame>
</frameset>
<noframes>
<body style="margin: 0px; padding: 0px;">
<h1>对不起，您的浏览器不支持框架</h1>
</body>
</noframes>
</html>