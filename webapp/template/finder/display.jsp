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
<title>${path}</title>
<link rel="stylesheet" type="text/css" href="${domainConfig.resource}/resource/finder/css/finder.css"/>
<c:choose>
    <c:when test="${util.isEmpty(theme)}"><c:set var="theme" value="RDark"/></c:when>
    <c:otherwise></c:otherwise>
</c:choose>
<link type="text/css" rel="stylesheet" href="${domainConfig.resource}/resource/sh/style/shCore${theme}.css"/>
<link type="text/css" rel="stylesheet" href="${domainConfig.resource}/resource/sh/style/shTheme${theme}.css"/>
<script type="text/javascript" src="${domainConfig.resource}/resource/finder/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="${domainConfig.resource}/resource/finder/ajax.js"></script>
<script type="text/javascript" src="${domainConfig.resource}/resource/finder/finder.js"></script>
<script type="text/javascript" src="${domainConfig.resource}/resource/sh/shCore.js"></script>
<script type="text/javascript" src="${domainConfig.resource}/resource/sh/shAutoloader.js"></script>
</head>
<!-- range: ${offset} - ${offset + length} -->
<body contextPath="${contextPath}" workspace="${workspace}" work="${work}" parent="${parent}" path="${(path != '' ? path : '/')}">
<div class="finder">
    <div class="menubar">
        <div style="float: left; width: 80px;">
            <c:if test="${util.isEmpty(parent)}"><a class="button disabled" href="javascript:void(0)"><span class="back-disabled"></span></a></c:if>
            <c:if test="${util.notEmpty(parent)}"><a class="button" href="javascript:void(0)" title="后退"><span class="back"></span></a></c:if>
            <a class="button" href="javascript:void(0)" title="刷新"><span class="refresh"></span></a>
        </div>
        <div style="float: left; height: 28px; position: relative;">
            <div style="float: left;"><input id="address" type="text" class="address" autocomplete="off" value="${(path != '' ? path : '/')}"/></div>
            <div id="finder-suggest" class="list"></div>
            <span>theme:</span>
            <select id="uiThemeOption" style="height: 24px; line-height: 24px;">
                <option value="Default" <c:if test="${util.isEmpty(theme)}">selected="true"</c:if>>Default</option>
                <c:forEach items="Django, Eclipse, Emacs, FadeToGrey, MDUltra, Midnight, RDark" var="current" varStatus="status">
                    <option value="${current}" <c:if test="${util.equals(theme, current)}">selected="true"</c:if>>${current}</option></c:forEach>
            </select>

            <span>type:</span>
            <select id="uiTypeOption" style="height: 24px; line-height: 24px;">
                <option value="" <c:if test="${util.isEmpty(type)}">selected="true"</c:if>>default</option>
                <c:forEach items="as, sh, bsh, bash, log, shell, cpp, cs, css, dpi, diff, erl, erlang, groovy, java, js, pl, php, txt, text, py, ruby, sass, scala, sql, vb, vbs, xml, xhtml, xslt, html, htm, asp, jsp, jspf, asp, php" var="current" varStatus="status">
                    <option value="${current}" <c:if test="${util.equals(type, current)}">selected="true"</c:if>>${current}</option></c:forEach>
            </select>

            <span>encoding:</span>
            <select id="uiEncodingOption" style="height: 24px; line-height: 24px;">
                <option value="" <c:if test="${util.isEmpty(encoding)}">selected="true"</c:if>>default</option>
                <option value="UTF-8" <c:if test="${util.equals(encoding, 'UTF-8')}">selected="true"</c:if>>UTF-8</option>
                <option value="GBK" <c:if test="${util.equals(encoding, 'GBK')}">selected="true"</c:if>>GBK</option>
                <option value="GB2312" <c:if test="${util.equals(encoding, 'GB2312')}">selected="true"</c:if>>GB2312</option>
                <option value="ISO-8859-1" <c:if test="${util.equals(encoding, 'ISO-8859-1')}">selected="true"</c:if>>ISO-8859-1</option>
            </select>
        </div>
        <div style="float: right; width: 40px;">
            <a class="button" href="/finder/help.html" title="帮助"><span class="help"></span></a>
        </div>
    </div>
    <c:if test="${offset > 0}"><div style="height: 24px; line-height: 24px; background-color: #efefef; font-size: 12px;">range: ${offset} - ${offset + length}</div></c:if>
    <div id="content">
        <c:choose>
            <c:when test="${util.equals(fileExists, true)}">
                <c:choose>
                    <c:when test="${util.equals(type, '??')}"><pre class="brush: bash;"></c:when>
                    <c:when test="${util.equals(type, 'as')}"><pre class="brush: actionscript3;"></c:when>
                    <c:when test="${util.equals(type, 'bsh')}"><pre class="brush: bash;"></c:when>
                    <c:when test="${util.equals(type, 'log')}"><pre class="brush: bash;"></c:when>
                    <c:when test="${util.equals(type, 'cpp')}"><pre class="brush: cpp;"></c:when>
                    <c:when test="${util.equals(type, 'cs')}"><pre class="brush: cs;"></c:when>
                    <c:when test="${util.equals(type, 'css')}"><pre class="brush: css;"></c:when>
                    <c:when test="${util.equals(type, 'dhi')}"><pre class="brush: dpi;"></c:when>
                    <c:when test="${util.equals(type, 'diff')}"><pre class="brush: diff;"></c:when>
                    <c:when test="${util.equals(type, 'erl')}"><pre class="brush: erl;"></c:when>
                    <c:when test="${util.equals(type, 'erlang')}"><pre class="brush: erlang;"></c:when>
                    <c:when test="${util.equals(type, 'groovy')}"><pre class="brush: groovy;"></c:when>
                    <c:when test="${util.equals(type, 'java')}"><pre class="brush: java;"></c:when>
                    <c:when test="${util.equals(type, 'js')}"><pre class="brush: javascript;"></c:when>
                    <c:when test="${util.equals(type, 'pl')}"><pre class="brush: perl;"></c:when>
                    <c:when test="${util.equals(type, 'php')}"><pre class="brush: php;"></c:when>
                    <c:when test="${util.equals(type, 'plain')}"><pre class="brush: plain;"></c:when>
                    <c:when test="${util.equals(type, 'sh')}"><pre class="brush: bash;"></c:when>
                    <c:when test="${util.equals(type, 'py')}"><pre class="brush: python;"></c:when>
                    <c:when test="${util.equals(type, 'ruby')}"><pre class="brush: ruby;"></c:when>
                    <c:when test="${util.equals(type, 'sass')}"><pre class="brush: sass;"></c:when>
                    <c:when test="${util.equals(type, 'scala')}"><pre class="brush: scala;"></c:when>
                    <c:when test="${util.equals(type, 'sql')}"><pre class="brush: sql;"></c:when>
                    <c:when test="${util.equals(type, 'vb') || util.equals(type, 'vbs')}"><pre class="brush: vbscript;"></c:when>
                    <c:when test="${util.equals(type, 'xml')}"><pre class="brush: xml;"></c:when>
                    <c:when test="${util.equals(type, 'xhtml')}"><pre class="brush: xml;"></c:when>
                    <c:when test="${util.equals(type, 'xslt')}"><pre class="brush: xml;"></c:when>
                    <c:when test="${util.equals(type, 'html')}"><pre class="brush: xml;"></c:when>
                    <c:when test="${util.equals(type, 'htm')}"><pre class="brush: xml;"></c:when>
                    <c:when test="${util.equals(type, 'jsp')}"><pre class="brush: xml;"></c:when>
                    <c:when test="${util.equals(type, 'jspf')}"><pre class="brush: xml;"></c:when>
                    <c:when test="${util.equals(type, 'asp')}"><pre class="brush: xml;"></c:when>
                    <c:when test="${util.equals(type, 'php')}"><pre class="brush: xml;"></c:when>
                    <c:otherwise><pre class="brush: plain;"></c:otherwise>
                </c:choose><c:out value="${content}"/></pre>
            </c:when>
            <c:otherwise><div class="h20"></div><div><h3>Error: ${path} not exists !</h3></div></c:otherwise>
        </c:choose>
    </div>
</div>
<script type="text/javascript">
<!--
function path() {
    var args = arguments;
    var result = [];

    for(var i = 0; i < args.length; i++) {
        result.push(args[i].replace("@", "${domainConfig.resource}/resource/sh/"));
    }
    return result;
};

var args = path(
    "applescript            @shBrushAppleScript.js",
    "actionscript3 as3      @shBrushAS3.js",
    "bash shell             @shBrushBash.js",
    "coldfusion cf          @shBrushColdFusion.js",
    "cpp c                  @shBrushCpp.js",
    "c# c-sharp csharp      @shBrushCSharp.js",
    "css                    @shBrushCss.js",
    "delphi pascal          @shBrushDelphi.js",
    "diff patch pas         @shBrushDiff.js",
    "erl erlang             @shBrushErlang.js",
    "groovy                 @shBrushGroovy.js",
    "java                   @shBrushJava.js",
    "jfx javafx             @shBrushJavaFX.js",
    "js jscript javascript  @shBrushJScript.js",
    "perl pl                @shBrushPerl.js",
    "php                    @shBrushPhp.js",
    "text plain             @shBrushPlain.js",
    "py python              @shBrushPython.js",
    "ruby rails ror rb      @shBrushRuby.js",
    "sass scss              @shBrushSass.js",
    "scala                  @shBrushScala.js",
    "sql                    @shBrushSql.js",
    "vb vbnet               @shBrushVb.js",
    "xml xhtml xslt html    @shBrushXml.js"
);

SyntaxHighlighter.autoloader.apply(null, args);
SyntaxHighlighter.all();

/*
jQuery(function() {
    var resize = function(){
        var div = jQuery("#content .syntaxhighlighter");

        if(div.size() < 1) {
            return;
        }

        var e = div.get(0);
        var offset = parseInt(e.getAttribute("offset-top"));

        if(isNaN(offset)) {
            offset = 48;
        }

        var height = document.documentElement.clientHeight - offset;
        e.style.height = height + "px";
    };

    jQuery(window).bind("resize", resize);
    setTimeout(resize, 500);
});
*/
//-->
</script>
</body>
</html>