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
<script type="text/javascript" src="${domainConfig.resource}/resource/finder/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="${domainConfig.resource}/resource/finder/ajax.js"></script>
<script type="text/javascript" src="${domainConfig.resource}/resource/finder/finder.js"></script>
</head>
<!-- count: ${util.size(fileList)} -->
<body jsp="<%=(1+2)%>" contextPath="${contextPath}" workspace="${workspace}" work="${work}" parent="${parent}" path="${(path != '' ? path : '/')}">
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
            <a class="button" href="javascript:void(0)" title="缩略图"><span class="view"></span></a>
            <div id="view-options" class="list view-menu">
                <ul>
                    <li index="0" option-value="outline"><a href="javascript:void(0)">缩略图</a></li>
                    <li index="1" option-value="detail" class="selected"><a href="javascript:void(0)">详细信息</a></li>
                </ul>
            </div>
        </div>
        <div style="float: right; width: 40px;">
            <a class="button" href="/finder/help.html" title="帮助"><span class="help"></span></a>
        </div>
    </div>
    <div id="file-view" class="detail-view">
        <div id="head-view" class="head">
            <span class="icon">&nbsp;</span>
            <span class="fileName orderable" orderBy="file-name" unselectable="on" onselectstart="return false;"><em class="title">名称</em><em class="order asc"></em></span>
            <span class="fileSize orderable" orderBy="file-size" unselectable="on" onselectstart="return false;"><em class="title">大小</em><em class="order"></em></span>
            <span class="fileType orderable" orderBy="file-type" unselectable="on" onselectstart="return false;"><em class="title">类型</em><em class="order"></em></span>
            <span class="lastModified orderable" orderBy="last-modified" unselectable="on" onselectstart="return false;"><em class="title">修改日期</em><em class="order"></em></span>
            <span class="w200"><em class="title">操作</em></span>
        </div>
        <ul id="file-list" class="file-list">
            <!-- ${path} -->
            <!-- Folder -->
            <c:forEach items="${fileList}" var="file" varStatus="status"><c:if test="${file.isDirectory()}">
                <li class="item" isFile="false" fileName="${file.name}" lastModified="${file.lastModified()}">
                    <span class="icon"><img src="${domainConfig.resource}/resource/finder/images/folder.gif"/></span>
                    <span class="fileName"><a class="file" href="javascript:void(0)">${file.name}</a></span>
                    <span class="fileSize">&nbsp;</span>
                    <span class="fileType">文件夹</span>
                    <span class="lastModified">${DateUtil.format(file.lastModified(), "yyyy-MM-dd HH:mm")}</span>
                    <span class="w200">
                        <a action="finder-open" href="javascript:void(0)" target="_blank">open</a>
                        <a action="finder-remove" href="javascript:void(0)">delete</a>
                    </span>
                </li>
            </c:if></c:forEach>
            <c:forEach items="${fileList}" var="file" varStatus="status"><c:if test="${file.isFile()}">
                <li class="item" fileIcon="${FileType.getIcon(file.name)}.gif" fileName="${file.name}" fileSize="${file.length()}" lastModified="${file.lastModified()}">
                    <span class="icon"><img src="${domainConfig.resource}/resource/finder/type/${FileType.getIcon(file.name)}.gif"/></span>
                    <span class="fileName"><a class="file" href="javascript:void(0)">${file.name}</a></span>
                    <span class="fileSize">${file.length() / 1024}KB</span>
                    <span class="fileType">${FileType.getType(file.name)}文件</span>
                    <span class="lastModified">${DateUtil.format(file.lastModified(), "yyyy-MM-dd HH:mm")}</span>
                    <span class="w200">
                        <a action="finder-tail" href="javascript:void(0)" target="_blank">tail</a>
                        <a action="finder-open" href="javascript:void(0)" target="_blank">open</a>
                        <a action="finder-download" href="javascript:void(0)">download</a>
                        <a action="finder-remove" href="javascript:void(0)">delete</a>
                    </span>
                </li>
            </c:if></c:forEach>
        </u>
    </div>
</div>

<div id="finder-contextmenu" class="contextmenu">
    <div class="menu">
        <div class="item" onmouseover="this.className='item hover'" onmouseout="this.className='item'">
            <span class="icon"></span><span class="command">复制</span>
        </div>
        <div class="item" onmouseover="this.className='item hover'" onmouseout="this.className='item'">
            <span class="icon"></span><span class="command">粘贴</span>
        </div>
        <div class="item" onmouseover="this.className='item hover'" onmouseout="this.className='item'">
            <span class="icon"></span><span class="command">删除</span>
        </div>
        <div class="item" onmouseover="this.className='item hover'" onmouseout="this.className='item'">
            <span class="icon"></span><span class="command">刷新</span>
        </div>
    </div>
</div>
</body>
</html>