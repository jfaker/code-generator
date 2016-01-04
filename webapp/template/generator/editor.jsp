<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content="no-cache"/>
<meta http-equiv="Expires" content="0"/>
<link rel="shortcut icon" href="/resource/style/default/images/favicon.ico" type="image/x-icon"/>
<link rel="stylesheet" type="text/css" href="/resource/style/style.css"/>
<link rel="stylesheet" type="text/css" href="/resource/tabview/css/tabview.css"/>
<script type="text/javascript" src="/resource/js/ajax.js"></script>
<script type="text/javascript" src="/resource/js/util.js"></script>
<script type="text/javascript" src="/resource/js/json2.js"></script>
<script type="text/javascript" src="/resource/js/TableEditor.js"></script>
<script type="text/javascript" src="/resource/js/drag.js"></script>
<script type="text/javascript" src="/resource/tabview/tabview.js"></script>
<script type="text/javascript">
//<![CDATA[
var defaultTableEditor = new TableEditor("columnTable");

defaultTableEditor.submit = function(){
    var tableDefinition = null;

    if(defaultTableEditor.validate() == true)
    {
        tableDefinition = this.getTableXml();
    }
    else{
        return false;
    }

    Ajax.request({
        "method": "post",
        "url": "/generator/generate.html",
        "data": {"tableDefinition": tableDefinition},
        "success": function(response){
            var json = null;

            try
            {
                json = window.eval("(" + response.responseText + ")");
            }
            catch(e)
            {
            }

            if(json != null)
            {
                if(json.message != null)
                {
                    alert(json.message);
                }
                else
                {
                    alert("系统错误，请稍后再试！");
                }
            }
            else
            {
                alert("系统错误，请稍后再试！");
            }
        }
    });
};

var UI = {};

UI.toggle = function(src) {
    if(src != null) {
        var t = Util.getParentElement(src, "table");
        var r = Util.getParentElement(src, "tr");
        var n = r.rowIndex + 1;

        if(n < t.rows.length)
        {
            var tr = t.rows[n];

            if(tr.style.display == "")
            {
                tr.style.display = "none";
                src.src = "/resource/images/plus.gif";
            }
            else
            {
                tr.style.display = "";
                src.src = "/resource/images/minus.gif";
            }
        }
    }
};

UI.openCreateDialog = function(){
    var quote = document.getElementById("quote").value;
    document.getElementById("source").value = defaultTableEditor.getCreateSql(quote);
    document.getElementById("form_dialog").style.display = "block";

    document.getElementById("quote").onchange = function() {
        document.getElementById("source").value = defaultTableEditor.getCreateSql(quote);
    };
};

UI.openInsertDialog = function(){
    var quote = document.getElementById("quote").value;
    document.getElementById("source").value = defaultTableEditor.getInsertSql(quote);
    document.getElementById("form_dialog").style.display = "block";

    document.getElementById("quote").onchange = function() {
        document.getElementById("source").value = defaultTableEditor.getInsertSql(quote);
    };
};

UI.openUpdateDialog = function(){
    var quote = document.getElementById("quote").value;
    document.getElementById("source").value = defaultTableEditor.getUpdateSql(quote);
    document.getElementById("form_dialog").style.display = "block";

    document.getElementById("quote").onchange = function() {
        document.getElementById("source").value = defaultTableEditor.getUpdateSql(quote);
    };
};

window.onload = function(){
    SimpleDrag.register("form_dialog_source", "form_dialog");

    Util.addEventListener(document.tableForm.wrapper, "click", function() {
        defaultTableEditor.wrap(this.checked);

        if(this.checked) {
            Util.setCookie({"name": "javaType", "value": "wrap", "expires": 365 * 24 * 3600});
        }
        else {
            Util.setCookie({"name": "javaType", "value": "primitive", "expires": 365 * 24 * 3600});
        }
    });

    Util.addEventListener(document.tableForm.encoding, "change", function() {
        var encoding = Util.trim(this.value);

        if(encoding.length < 1) {
            return;
        }

        Util.setCookie({"name": "javaFileEncoding", "value": encoding, "expires": 3600});
    });

    var javaType = Util.getCookie("javaType");
    var javaFileEncoding = Util.getCookie("javaFileEncoding");

    if(javaType == "wrap") {
        defaultTableEditor.wrap(true);
        document.tableForm.wrapper.checked = true;
    }

    if(javaFileEncoding != null) {
        document.tableForm.encoding.value = javaFileEncoding;
    }

    var resize = function(){
        var e = document.getElementById("columnPanel");

        if(e != null) {
            var offset = parseInt(e.getAttribute("offset-top"));

            if(isNaN(offset)) {
                offset = 210;
            }

            var height = document.documentElement.clientHeight - offset;
            e.style.height = height + "px";
        }
    };

    Util.addEventListener(window, "resize", resize);
    resize();
};
//]]>
</script>
</head>
<body>
<div style="margin: 4px;">
    <div id="defaultTabView" class="tabview">
        <ul>
            <li tabId="1" class="tabshow" style="-moz-user-select:none;-webkit-user-select:none;" unselectable="on" onselectstart="return false;" onclick="defaultTabView.active(this);">General</li>
            <li tabId="2" class="tabhide" style="-moz-user-select:none;-webkit-user-select:none;" unselectable="on" onselectstart="return false;" onclick="defaultTabView.active(this);">Template</li>
        </ul>
    </div>
    <div class="panel tabpanel" style="margin: 0px; border-top: 0px solid #ffffff;" tabId="1">
        <div class="menu-bar">
            <ul>
                <li onclick="window.location.href='/tools.html'"><h4 class="back" style="-moz-user-select:none;-webkit-user-select:none;" unselectable="on" onselectstart="return false;" title="返 回">返 回</h4></li>
                <li onclick="defaultTableEditor.addColumn();"><h4 class="add" style="-moz-user-select:none;-webkit-user-select:none;" unselectable="on" onselectstart="return false;" title="插入列">插入列</h4></li>
                <li onclick="defaultTableEditor.deleteCheckedColumn();"><h4 class="delete" style="-moz-user-select:none;-webkit-user-select:none;" unselectable="on" onselectstart="return false;" title="删除列">删除列</h4></li>
                <li onclick="defaultTableEditor.submit();"><h4 class="save" style="-moz-user-select:none;-webkit-user-select:none;" unselectable="on" onselectstart="return false;" title="生成代码">生成代码</h4></li>
                <li onclick="window.open('/finder/index.html', '_blank');"><h4 class="preview" style="-moz-user-select:none;-webkit-user-select:none;" unselectable="on" onselectstart="return false;" title="查看文件">查看文件</h4></li>
                <li onclick="UI.openCreateDialog();"><h4 class="preview" style="-moz-user-select:none;-webkit-user-select:none;" unselectable="on" onselectstart="return false;" title="创建脚本">创建脚本</h4></li>
                <li onclick="UI.openInsertDialog();"><h4 class="preview" style="-moz-user-select:none;-webkit-user-select:none;" unselectable="on" onselectstart="return false;" title="插入脚本">插入脚本</h4></li>
                <li onclick="UI.openUpdateDialog();"><h4 class="preview" style="-moz-user-select:none;-webkit-user-select:none;" unselectable="on" onselectstart="return false;" title="更新脚本">更新脚本</h4></li>
            </ul>
        </div>
        <div class="panel-content">
            <div class="form-panel" style="border-bottom: none; background-color: #eeeeee;">
                <form name="tableForm" method="post" onsubmit="return defaultTableEditor.validate();">
                <input type="hidden" name="alias" value="${table.alias}"/>
                <input type="hidden" name="tableType" value="${table.tableType}"/>
                <input type="hidden" name="queryName" value="${table.queryName}"/>
                <table class="form">
                    <tr>
                        <td class="w80">Name:</td>
                        <td class="w200">
                            <input type="text" name="tableName" class="required" maxlength="64"
                                onchange="defaultTableEditor.onTableNameChange(this)" value="${table.tableName}"/>
                            <span class="required">*</span>
                        </td>
                        <td class="w80">Code:</td>
                        <td>
                            <input type="text" name="tableCode" class="required" maxlength="255"
                                onchange="defaultTableEditor.onTableCodeChange(this)" value="${table.tableName}"/>
                            <span class="required">*</span>
                        </td>
                        <td>Encoding:</td>
                        <td><input type="text" name="encoding" class="text" maxlength="255" value="UTF-8"/></td>
                    </tr>
                    <tr>
                        <td>ClassName:</td>
                        <td><input type="text" name="className" class="required" maxlength="255" value="${table.className}"/></td>
                        <td>Remarks:</td>
                        <td><input name="remarks" type="text" class="text w200" value="${table.remarks}"/></td>
                        <td>Wrapper:</td>
                        <td><input name="wrapper" type="checkbox" value=""/></td>
                    </tr>
                </table>
                </form>
                <div style="height: 10px;"></div>
            </div>
            <div class="form-panel">
                <div>
                    <table class="table" style="width: 900px;">
                        <tr class="thead">
                            <td class="c1">&nbsp;</td>
                            <td columnIndex="1" style="width: 126px;">Name</td>
                            <td columnIndex="2" style="width: 126px;">Code</td>
                            <td columnIndex="3" style="width: 106px;">DataType</td>
                            <td columnIndex="4" style="width: 106px;">JavaName</td>
                            <td columnIndex="5" style="width: 106px;">JavaType</td>
                            <td columnIndex="6" style="width: 66px;">Length</td>
                            <td ColumnIndex="7" style="width: 66px;">Precision</td>
                            <td columnIndex="8" class="c1">P</td>
                            <td columnIndex="9" class="c1">M</td>
                        </tr>
                    </table>
                </div>
                <div id="columnPanel" style="height: 420px; padding-top: 0px; overflow-x: auto; overflow-y: auto;">
                    <table id="columnTable" class="table " style="width: 900px;">
                        <c:forEach items="${columns}" var="column" varStatus="status">
                        <tr onclick="defaultTableEditor.onColumnClick(this)" jso="${JsonUtil.stringify(column)}" title="${column.remarks}">
                            <td class="c2">${status.index + 1}</td>
                            <td style="width: 126px;"><input type="text" class="text" style="width: 120px; height: 20px;" value="${column.columnName}" onfocus="defaultTableEditor.onCellFocus(this)" onkeyup="defaultTableEditor.onCellKeyDown(this)" onchange="defaultTableEditor.onColumnNameChange(this)"/></td>
                            <td style="width: 126px;"><input type="text" class="text" style="width: 120px; height: 20px;" value="${column.columnCode}" onfocus="defaultTableEditor.onCellFocus(this)" onkeyup="defaultTableEditor.onCellKeyDown(this)" onchange="defaultTableEditor.onColumnCodeChange(this)"/></td>
                            <td style="width: 106px;"><input type="text" class="text" style="width: 100px; height: 20px;" value="${column.typeName}" onfocus="defaultTableEditor.onCellFocus(this)" onkeyup="defaultTableEditor.onCellKeyDown(this)" onchange="defaultTableEditor.onDataTypeChange(this)"/></td>
                            <td style="width: 106px;"><input type="text" class="text" style="width: 100px; height: 20px;" value="${column.variableName}" onfocus="defaultTableEditor.onCellFocus(this)" onkeyup="defaultTableEditor.onCellKeyDown(this)" onchange="defaultTableEditor.onJavaNameChange(this)"/></td>
                            <td style="width: 106px;"><input type="text" class="text" style="width: 100px; height: 20px;" value="${column.javaTypeName}" onfocus="defaultTableEditor.onCellFocus(this)" onkeyup="defaultTableEditor.onCellKeyDown(this)" onchange="defaultTableEditor.onJavaTypeChange(this)"/></td>
                            <td style="width: 66px;"><input type="text" class="text" style="width: 60px; height: 20px;"  value="${column.columnSize}" onfocus="defaultTableEditor.onCellFocus(this)" onkeyup="defaultTableEditor.onCellKeyDown(this)" onchange="defaultTableEditor.onDataLengthChange(this)"/></td>
                            <td style="width: 66px;"><input type="text" class="text" style="width: 60px; height: 20px;"  value="${column.decimalDigits}" onfocus="defaultTableEditor.onCellFocus(this)" onkeyup="defaultTableEditor.onCellKeyDown(this)" onchange="defaultTableEditor.onDataPrecisionChange(this)"/></td>
                            <td class="c1"><input type="checkbox" value="true" primaryKey="${column.primaryKey}" <c:if test="${column.primaryKey}">checked="true"</c:if> onclick="defaultTableEditor.onPrimaryKeyClick(this)"/></td>
                            <td class="c1"><input type="checkbox" value="true" nullable="${column.nullable}" <c:if test="${column.nullable == 1}">checked="true"</c:if>/></td>
                        </tr>
                        </c:forEach>
                    </table>
                </div>
            </div>
        </div>
        <div class="status-bar">
            <div><h4 class="movetop" onclick="defaultTableEditor.moveTop()"></h4></div>
            <div><h4 class="moveup" onclick="defaultTableEditor.moveUp()"></h4></div>
            <div><h4 class="movedown" onclick="defaultTableEditor.moveDown()"></h4></div>
            <div><h4 class="movebottom" onclick="defaultTableEditor.moveBottom()"></h4></div>
        </div>
    </div>
    <div class="panel tabpanel" style="margin: 0px; border-top: 0px solid #ffffff; display: none;" tabId="2">
        <div class="menu-bar">
            <span class="bb" style="padding-left: 4px;">选择模板:</span>
            <c:if test="${util.notEmpty(connectionName)"}>
            <select onchange="window.location.href='edit.html?connectionName=${URLUtil.encode(connectionName)}&tableName=${URLUtil.encode(tableName)}&templateConfig='+encodeURIComponent(this.value);">
            </c:if>
            <c:if test="${util.notEmpty(fileName)}">
            <select onchange="window.location.href='edit.html?fileName=${URLUtil.encode(fileName)}&tableName=${URLUtil.encode(tableName)}&templateConfig='+encodeURIComponent(this.value);">
            </c:if>
            <c:forEach items="${templateConfigList}" var="current" varStatus="status">
                <c:choose>
                    <c:when test="${current == templateConfig}"><option value="${current}" selected="true">${current}</option></c:when>
                    <c:otherwise><option value="${current}">${current}</option></c:otherwise>
                </c:choose>
            </c:forEach>
            </select>
        </div>
        <div class="panel-content">
            <div class="form-panel">
                <table id="templateTable" class="table">
                    <tr class="thead">
                        <td class="w60 center"><a href="javascript:void(0)" onclick="Util.check('templateCheckbox', this)">全 选</a></td>
                        <td class="center">模 板</td>
                    </tr>
                    <c:set var="j" value="1"/>
                    <c:forEach items="${templates}" var="template" varStatus="status">
                    <tr jso="${JsonUtil.stringify(template)}">
                        <td class="w80 center"><img src="/resource/images/minus.gif" onclick="UI.toggle(this)"/><input type="checkbox" name="templateCheckbox" value="1" <c:if test="${template.enabled}">checked="true"</c:if>/></td>
                        <td><p style="height: 26px; line-height: 26px;">模板文件：<input type="text" class="text w500" value="${template.path}"/></p>
                            <p style="height: 26px; line-height: 26px;">输出路径：<input type="text" class="text w500" value="${template.output}"/></p>
                        </td>
                    </tr>
                    <tr>
                        <td class="w80 center">&nbsp;</td>
                        <td class="left">
                            <c:if test="${util.notEmpty(template.parameters)}">
                            <table class="table" style="margin: 4px 4px 4px 0px; width: 640px; border: 1px solid #c0c0c0;">
                                <c:set var="k" value="1"/>
                                <tr>
                                    <td class="w120 center gray bb">参数名</td>
                                    <td class="center gray bb">参数值</td>
                                </tr>
                                <c:forEach items="${template.parameters.values()}" var="parameter" varStatus="status">
                                <tr jso="${JsonUtil.stringify(parameter)}">
                                    <td class="w120 right gray bb">${parameter.name}:&nbsp;</td>
                                    <td class="gray"><input type="text" name="v${j * k}" class="text w400" value="${parameter.value}"/></td>
                                </tr>
                                <c:set var="k" value="${k + 1}"/>
                                </c:forEach>
                            </table>
                            </c:if>
                            <c:if test="${util.isEmpty(template.parameters)}">无</c:if>
                        </td>
                    </tr>
                    <c:set var="j" value="${j + 1}"/>
                    </c:forEach>
                </table>
            </div>
        </div>
        <div class="status-bar"></div>
    </div>
</div>
<div class="hide">
    <form name="xform" method="post" action="/generator/generate.html" onsubmit="return false;">
        <textarea name="tableDefinition" rows="10" cols="80"></textarea>
    </form>
</div>

<div id="form_dialog" class="panel hide" style="position: absolute; top: 20px; left: 40px;">
    <div class="panel-title">
        <h4 id="form_dialog_source">
            <span class="icon-table"></span>创建脚本
            <span class="button close" onclick="Util.hide('form_dialog')"></span>
        </h4>
    </div>
    <div class="menu-bar">
        <div style="padding-left: 4px;">quote: <input id="quote" type="text" class="w60" value="%s"/></div>
    </div>
    <div class="panel-content">
        <div class="form-panel" style="width: 600px; height: 400px; overflow: hidden; cursor: default;">
            <textarea id="source" style="width: 596px; height: 396px; border: none; outline: none; resize: none; font-size: 14px; word-break: keep-all; overflow: scroll;"></textarea>
        </div>
    </div>
    <div style="height: 30px; line-height: 40px;">
        <img style="float: right; margin-right: 20px;" src="/resource/buttons/close.gif" onclick="Util.hide('form_dialog')"/>
    </div>
</div>
<!--
<c:out value="${table.getInsertString()}"/>
<c:out value="${table.getUpdateString()}"/>
<c:out value="${table.getCreateString()}"/>
-->
</body>
</html>
