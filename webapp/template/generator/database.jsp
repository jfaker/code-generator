<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content="no-cache"/>
<meta http-equiv="Expires" content="0"/>
<title>Generator v1.0</title>
<link rel="stylesheet" type="text/css" href="/resource/htree/css/htree.css"/>
<style type="text/css">
body{background-color: #e8eef6;}
</style>
<script type="text/javascript" src="/resource/htree/htree.js"></script>
<script type="text/javascript" src="/resource/js/util.js"></script>
<script type="text/javascript">
//<![CDATA[
HTree.treeNodeOnClick = function(url) {
    if(url == null || url.length < 1) {
        return;
    }
    var templateConfig = document.getElementById("templateConfig").value;

    if(templateConfig.length < 1) {
        window.top.mainFrame.location.href = url;
        return;
    }

    if(url.indexOf("?") > -1 || url.indexOf("&") > -1) {
        url = url + "&templateConfig=" + encodeURIComponent(templateConfig);
    }
    else {
        var k = url.indexOf("#");

        if(k > -1) {
            url = url.substring(0, k) + "?templateConfig=" + encodeURIComponent(templateConfig) + url.substring(k);
        }
        else {
            url = url + "?templateConfig=" + encodeURIComponent(templateConfig);
        }
    }
    window.top.mainFrame.location.href = url;
};

window.onload = function() {
    HTree.config.stylePath = "/resource/htree/database/";

    setTimeout(function(){
        var url = "/database/getDatabaseXml.html";
        var tree = new HTree.TreeNode({text: "数据库", href: "javascript:void(0)"});
        tree.load(url, function(){
            this.render(document.getElementById("htree"));
        });
    }, 100);
}
//]]>
</script>
</head>
<body style="margin: 0px; padding: 4px; background-color: #ffffff;">
<div style="margin: 4px 0px 4px 0px;">
    <select id="templateConfig">
        <c:forEach items="${templateConfigList}" var="templateConfig" varStatus="status">
            <option value="${templateConfig}">${templateConfig}</option>
        </c:forEach>
    </select>
</div>
<div id="htree" class="htree" style="white-space: nowrap;"></div>
</body>
</html>
