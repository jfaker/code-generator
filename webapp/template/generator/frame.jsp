<%@ page contentType="text/html; charset=UTF-8;"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Generator v1.0</title>
<script type="text/javascript">
<!--
window.onload = function() {
    document.body.onkeydown = function(e) {
        var event = (e || window.event);
        var keyCode = event.keyCode;

        if(keyCode == 116) {
            window.mainFrame.location.reload();
            return false;
        }
        return true;
    }
};

/*
window.onbeforeunload = function(e) {
    return "确定要重新加载此页面吗？";
}
*/
//-->
</script>
</head>
<frameset id="topFrame" name="topFrame" cols="240, *" border="5" frameborder="yes" bordercolor="#dfe0df" framespacing="5" topmargin="0" leftmargin="0" marginheight="0" marginwidth="0">
    <frame id="leftFrame" name="leftFrame" src="/generator/database.html" scrolling="auto"></frame>
    <frame id="mainFrame" name="mainFrame" src="/blank.html" scrolling="auto"></frame>
</frameset>
<noframes><body style="margin: 0px; padding: 0px;"><h1>对不起，您的浏览器不支持框架</h1></body></noframes>
</html>