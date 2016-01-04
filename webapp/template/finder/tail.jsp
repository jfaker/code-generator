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
<title>WebTail</title>
<link rel="stylesheet" type="text/css" href="${domainConfig.resource}/resource/finder/css/finder.css"/>
<script type="text/javascript" src="${domainConfig.resource}/resource/finder/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="${domainConfig.resource}/resource/finder/console.js"></script>
<script type="text/javascript">
<!--
var TailClient = {"running": false, "position": -1};

TailClient.getTimeout = function(){
    var e = document.getElementById("reloadInterval");

    if(e != null)
    {
        var timeout = parseInt(e.value);

        if(isNaN(timeout) == false && timeout >= 1)
        {
            return timeout * 1000;
        }
    }

    return 2000;
};

TailClient.start = function(){
    this.stop();
    this.running = true;
    TailClient.poll();
};

TailClient.stop = function(){
    this.running = false;

    if(this.timer != null)
    {
        clearTimeout(this.timer);
    }
};

TailClient.poll = function(){
    if(this.running)
    {
        var parameters = [];
        parameters[parameters.length] = "workspace=" + encodeURIComponent(this.workspace);
        parameters[parameters.length] = "work=" + encodeURIComponent(this.work);
        parameters[parameters.length] = "parent=" + encodeURIComponent(this.parent);
        parameters[parameters.length] = "path=" + encodeURIComponent(this.path);
        parameters[parameters.length] = "position=" + encodeURIComponent(this.position);

        jQuery.ajax({
            type: "GET",
            url: "/finder/getTail.html?" + parameters.join("&"),
            dataType: "jsonp",
            error: function(req){
                TailClient.print(null);
            },
            success: function(result){
                TailClient.print(result);
            }
        });
    }
};

TailClient.print = function(result){
    if(result != null && result.content != null)
    {
        logger.debug(result.content);
        this.position = result.position;
    }

    var timeout = this.getTimeout(); // Math.floor(2 + Math.random() * 5000);
    this.timer = setTimeout(function(){TailClient.poll();}, timeout);
};

jQuery(function(){
    jQuery("#tailButton").toggle(function(){
        this.value = " 启 动 ";
        logger.debug("console stoped !\r\n");
        TailClient.stop();
    }, function(){
        this.value = " 停 止 ";
        logger.debug("console started !\r\n");
        TailClient.start();
    });

    jQuery(window).bind("resize", function(){
        jQuery("#console").width(jQuery(window).width() - 4);
        jQuery("#console").height(jQuery(window).height() - 46);
    });

    jQuery(window).trigger("resize");

    TailClient.workspace = document.body.getAttribute("workspace");
    TailClient.work = document.body.getAttribute("work");
    TailClient.parent = document.body.getAttribute("parent");
    TailClient.path = document.body.getAttribute("path");
    TailClient.encoding = document.body.getAttribute("encoding");
    TailClient.start();

    setTimeout(function(){window.location.reload();}, 60 * 1000);
});
//-->
</script>
</head>
<body workspace="${workspace}" work="${work}" parent="${parent}" path="${path}" encoding="${encoding}">
<!-- 设置-语言-语言和输入设置-去掉勾选 启用拼写检查 -->
<div class="console-menubar">
    <input id="tailButton" type="button" class="button" value=" 停 止 " onclick="TailClient.stop();"/>
    <input type="button" class="button" value=" 全 选 " onclick="Console.select();"/>
    <input type="button" class="button" value=" 刷 新 " onclick="window.location.reload();"/>

    <span style="font-size: 12px;">重载时间：</span><input id="reloadInterval" type="text" class="text w30" value="1"/> 秒
    <input type="checkbox" checked="true" onclick="Console.setScroll(this.checked);"/><span style="font-size: 12px;">自动滚动</span>
</div>
<div id="console" class="console" style="width: 100%; height: 400px;" contenteditable="true"></div>
</body>
</html>