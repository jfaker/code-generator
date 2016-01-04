<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content="no-cache"/>
<meta http-equiv="Expires" content="0"/>
<link rel="shortcut icon" type="image/x-icon" href="/resource/images/favicon.ico"/>
<link rel="stylesheet" type="text/css" href="/resource/style/style.css"/>
<script type="text/javascript" src="/resource/js/ajax.js"></script>
<script type="text/javascript" src="/resource/js/util.js"></script>
<script type="text/javascript" src="/resource/js/json2.js"></script>
<script type="text/javascript" src="/resource/js/drag.js"></script>
<script type="text/javascript" src="/resource/js/resize.js"></script>
<script type="text/javascript">
//<![CDATA[
function getTableNameList(){
    var a = [];
    var list = document.getElementsByName("tableName");

    if(list != null) {
        if(list.length != null && list.length > 0) {
            for(var i = 0; i < list.length; i++) {
                if(list[i].checked == true) {
                    a[a.length] = list[i].getAttribute("tableType") + ":" + list[i].value;
                }
            }
        }
        else {
            if(list.nodeName != null && list.nodeName.toLowerCase() == "input") {
                a[a.length] = list.getAttribute("tableType") + ":" + list.value;
            }
        }
    }

    return a;
}

function batchSubmit(){
    var a = getTableNameList();

    if(a.length < 1) {
        alert("请选择项目！");
        return false;
    }

    var fileName = document.body.getAttribute("fileName");
    var connectionName = document.body.getAttribute("connectionName");
    var templateConfig = document.body.getAttribute("templateConfig");

    Ajax.request({
        "method": "post",
        "url": "/generator/batch.html",
        "data": {"fileName": fileName, "connectionName": connectionName, "templateConfig": templateConfig, "tableName": a},
        "error": function(){
            alert("系统错误，请稍后再试！");
        },
        "success": function(response){
            var json = null;

            try {
                json = window.eval("(" + response.responseText + ")");
            }
            catch(e) {
            }

            if(json != null) {
                if(json.message != null) {
                    alert(json.message);
                }
                else {
                    alert("系统错误，请稍后再试！");
                }
            }
            else {
                alert("系统错误，请稍后再试！");
            }
        }
    });
};

function exportSql(){
    var fileName = document.body.getAttribute("fileName");
    var connectionName = document.body.getAttribute("connectionName");

    if(Util.trim(fileName).length > 0) {
        window.location.href = "/database/script.html?fileName=" + encodeURIComponent(fileName);
        return;
    }

    if(Util.trim(connectionName).length > 0) {
        window.location.href = "/database/script.html?connectionName=" + encodeURIComponent(connectionName);
        return;
    }
}

function exportData(){
    var connectionName = document.body.getAttribute("connectionName");

    if(Util.trim(connectionName).length < 1) {
        alert("该连接不支持导出数据！");
        return;
    }

    var tableNameList = getTableNameList();

    if(tableNameList.length < 1) {
        alert("请选择项目！");
        return false;
    }

    var html = [];

    for(var i = 0; i < tableNameList.length; i++) {
        var tableName = tableNameList[i];
        var k = tableName.indexOf(":");

        if(k > -1) {
            tableName = tableName.substring(k + 1);
        }

        var text = document.createElement("input");
        text.name = "tableName";
        text.value = tableName;
        document.exportForm.appendChild(text);
    }

    var text = document.createElement("input");
    text.name = "connectionName";
    text.value = connectionName;
    document.exportForm.appendChild(text);

    document.exportForm.method = "get";
    document.exportForm.action = "/database/export.html";
    document.exportForm.submit();
}
//]]>
</script>
</head>
<c:if test="${util.isEmpty(fileName)}">
    <c:set var="targetUrl" value="/table/edit.html?connectionName=${connectionName}"/>
</c:if>
<c:if test="${util.notEmpty(fileName)}">
    <c:set var="targetUrl" value="/sql/edit.html?fileName=${URLUtil.encode(fileName)}"/>
</c:if>
<body fileName="${fileName}" connectionName="${connectionName}" templateConfig="${templateConfig}">
<div class="panel">
    <div class="panel-title"><h4><span class="icon"></span>批量生成</h4></div>
    <div class="menu-bar">
        <ul>
            <li onclick="window.location.href='/tools.html'"><h4 class="back" style="-moz-user-select:none;-webkit-user-select:none;" unselectable="on" onselectstart="return false;" title="返 回">返 回</h4></li>
            <li onclick="batchSubmit()"><h4 class="save" style="-moz-user-select:none;-webkit-user-select:none;" unselectable="on" onselectstart="return false;" title="批量生成">批量生成</h4></li>
            <li onclick="window.open('/finder/index.html', '_blank');"><h4 class="preview" style="-moz-user-select:none;-webkit-user-select:none;" unselectable="on" onselectstart="return false;" title="查看文件">查看文件</h4></li>
            <li onclick="exportSql();"><h4 class="export" style="-moz-user-select:none;-webkit-user-select:none;" unselectable="on" onselectstart="return false;" title="导出脚本">导出脚本</h4></li>
            <c:if test="${util.isEmpty(fileName)}">
                <li onclick="exportData();"><h4 class="export" style="-moz-user-select:none;-webkit-user-select:none;" unselectable="on" onselectstart="return false;" title="导出数据">导出数据</h4></li>
            </c:if>
        </ul>
    </div>
    <div class="panel-content">
        <div class="form-panel">
            <table class="table">
                <tr class="thead">
                    <td class="w80"><input type="checkbox" checked="true" title="全 选" onclick="Util.check('tableName', this.checked)"/></td>
                    <td class="w400">Table & View</td>
                    <td>操 作</td>
                </tr>
                <c:forEach items="${tableList}" var="table" varStatus="status">
                <c:set var="tableId" value="drag_target_${table.tableName}"/>
                <tr>
                    <td class="w60 center"><input type="checkbox" name="tableName" value="${table.tableName}" tableType="${table.tableType}" checked="true"/></td>
                    <td>
                        <c:if test="${table.tableType == 'TABLE'}"><img src="/resource/images/table.gif"/></c:if><c:if test="${table.tableType == 'VIEW'}"><img src="/resource/images/view.gif"/></c:if>
                        <!-- a href="${targetUrl}&templateConfig=${URLUtil.encode(templateConfig)}&tableName=${URLUtil.encode(table.tableName)}" title="${connectionConfig.url}">${table.tableName}</a -->
                        <a href="javascript:void(0)" onclick="Util.show('${tableId}');document.getElementById('${tableId}').style.zIndex = (SimpleDrag.zIndex++);" title="表结构">${table.tableName}</a -->
                    </td>
                    <td>
                        <a href="/sql/insert.html?fileName=${URLUtil.encode(fileName)}&tableName=${URLUtil.encode(table.tableName)}&templateConfig=${URLUtil.encode(templateConfig)}">Insert</a>
                    </td>
                </tr>
                </c:forEach>
            </table>
        </div>
    </div>
    <div class="status-bar"></div>
</div>

<c:forEach items="${tableList}" var="table" varStatus="status">
    <c:set var="tableId" value="drag_target_${table.tableName}"/>
    <div id="${tableId}" class="panel hide" style="position: absolute; top: 20px; left: 40px;">
        <div class="panel-title">
            <h4 id="drag_source_${table.tableName}">
                <span class="icon-table"></span>${table.tableName}
                <span class="button close" onclick="Util.hide('${tableId}')"></span>
            </h4>
        </div>
        <div class="panel-content">
            <div class="form-panel" style="width: 600px; height: 400px; overflow: auto; cursor: default;">
                <table class="table" style="width: 800px;">
                    <tr class="thead">
                        <td class="cc w60 bb">index</td>
                        <td class="w200 bb">columnName</td>
                        <td class="w200 bb">columnType</td>
                        <td class="bb">remarks</td>
                    </tr>
                    <c:forEach items="${table.listColumns()}" var="column" varStatus="status">
                    <tr title="${column.typeName}: ${column.remarks}">
                        <td class="cc bb">${status.index + 1}</td>
                        <td class="bb">${column.columnName}</td>
                        <td>${column.columnName}(${column.precision})</td>
                        <td>${column.remarks}</td>
                    </tr>
                    </c:forEach>
                </table>
            </div>
        </div>
        <div class="status-bar"></div>
    </div>
    <script type="text/javascript">
    <!--
    (function(){
        SimpleDrag.register("drag_source_${table.tableName}", "drag_target_${table.tableName}");
    })();
    //-->
    </script>
</c:forEach>
<div class="hide">
    <form name="exportForm"></form>
</div>
</body>
</html>