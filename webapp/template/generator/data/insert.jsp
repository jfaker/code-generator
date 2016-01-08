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
<script type="text/javascript" src="/resource/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript">
//<![CDATA[
jQuery(function() {
    var getTableName = function(result) {
        var table = document.getElementById("columnList");

        if(table == null) {
            return;
        }
        return table.getAttribute("tableName");
    };

    var getOptionsHtml = function(name, dataType) {
        var b = [];
        var t = ["unknown", "number", "string", "datatime"];
        b[b.length] = "<select name=\"" + name + "\">";

        for(var i = 0; i < t.length; i++) {
            if(t[i] == dataType) {
                b[b.length] = "<option value=\"" + t[i] + "\" selected=\"true\">" + t[i] + "</option>";
            }
            else {
                b[b.length] = "<option value=\"" + t[i] + "\">" + t[i] + "</option>";
            }
        }
        return b.join("");
    };

    var showColumnList = function(result) {
        var table = document.getElementById("columnList");

        if(table == null) {
            return;
        }
        var rows = table.rows;
        var size = rows.length;

        for(var i = size - 1; i > -1; i--) {
            var e = rows[i];
            e.parentNode.removeChild(e);
        }

        var list = result.columns;
        var tableName = result.tableName;
        table.setAttribute("tableName", tableName);

        for(var i = 0; i < list.length; i++) {
            var column = list[i];
            var tr = table.insertRow(-1);
            var td1 = tr.insertCell(-1);
            var td2 = tr.insertCell(-1);
            var td3 = tr.insertCell(-1);
            var td4 = tr.insertCell(-1);
            var td5 = tr.insertCell(-1);
            var value = column.value;
            var className = typeof(value);
            var typeName = "data-type-string";

            if(value == null) {
                value = "";
                typeName = "data-type-unknown";
                className = "unknown";
            }
            else if(className == "string") {
                value = HtmlUtil.encode(column.value, "\r\n");
            }
            else {
                typeName = "data-type-int";
            }

            td1.className = "w30";
            td2.className = "w200";
            td3.className = "w100";
            td4.className = "w500";
            td5.className = "center";

            td1.innerHTML = i + 1;
            td2.innerHTML = "<input class=\"" + typeName + "\" readonly=\"true\" value=\"" + column.name + "\"/>";
            td3.innerHTML = getOptionsHtml("dataType", className);
            td4.innerHTML = "<input name=\"" + column.name + "\" type=\"text\" class=\"column-value\" value=\"" + value + "\"/>";
            td5.innerHTML = "<a class=\"edit\" href=\"javascript:void(0)\">编 辑</a>&nbsp;&nbsp;<a class=\"delete\" href=\"javascript:void(0)\">删 除</a>";

            tr.setAttribute("columnType", className);
            tr.setAttribute("columnName", column.name);
            tr.setAttribute("columnValue", column.value);
        }
    };

    var setColumnValue = function(name, value) {
        var table = document.getElementById("columnList");

        if(table != null) {
            var rows = table.rows;
            var size = rows.length;

            for(var i = 0; i < size; i++) {
                var e = rows[i];

                if(e.getAttribute("columnName") == name) {
                    e.setAttribute("columnValue", value);
                    jQuery(e).find("td input[name=" + name + "]").val(value);
                    break;
                }
            }
        }
    };

    var resetOrderNum = function() {
        var table = document.getElementById("columnList");

        if(table != null) {
            var rows = table.rows;
            var size = rows.length;

            for(var i = 0; i < size; i++) {
                var e = rows[i];
                e.cells[0].innerHTML = (e.rowIndex + 1);
            }
        }
    };

    var showEditPanel = function(column) {
        jQuery("#editPanelTitle").html("<span class=\"icon-table\"></span>" + column.name + "<span class=\"button close\"></span>");
        jQuery("#editPanel").attr("columnName", column.name);
        jQuery("#editPanel textarea[name=content]").val(column.value);
        Util.show("editPanel");
        jQuery("#editPanel textarea[name=content]").select();
    };

    var getRecord = function() {
        var record = [];
        var table = document.getElementById("columnList");

        if(table != null) {
            var rows = table.rows;
            var size = rows.length;

            for(var i = 0; i < size; i++) {
                var e = rows[i];
                var columnType = e.getAttribute("columnType");
                var columnName = e.getAttribute("columnName");
                var columnValue = e.getAttribute("columnValue");
                record[record.length] = {"columnType": columnType, "columnName": columnName, "columnValue": columnValue};
            }
        }
        return record;
    };

    var sqlEscape = function(source) {
        if(source == null) {
            return "";
        }
        var buffer = [];

        for(var i = 0, size = source.length; i < size; i++) {
            c = source.charAt(i);

            switch (c) {
                case '\\': {
                    buffer[buffer.length] = "\\\\"; break;
                }
                case '\'': {
                    buffer[buffer.length] = "\\\'"; break;
                }
                case '"': {
                    buffer[buffer.length] = "\\\""; break;
                }
                case '\r': {
                    buffer[buffer.length] = "\\r"; break;
                }
                case '\n': {
                    buffer[buffer.length] = "\\n"; break;
                }
                case '\t': {
                    buffer[buffer.length] = "\\t"; break;
                }
                case '\b': {
                    buffer[buffer.length] = "\\b"; break;
                }
                case '\f': {
                    buffer[buffer.length] = "\\f"; break;
                }
                default:  {
                    buffer[buffer.length] = c; break;
                }
            }   
        }
        return buffer.join("");
    };

    var getInsertSql = function() {
        var record = getRecord();
        var buffer = ["insert into "];
        buffer[buffer.length] = getTableName() + "(";

        for(var i = 0; i < record.length; i++) {
            var column = record[i];
            buffer[buffer.length] = column.columnName;

            if(i + 1 < record.length) {
                buffer[buffer.length] = ", ";
            }
        }
        buffer[buffer.length] = ") values (";

        for(var i = 0; i < record.length; i++) {
            var column = record[i];
            var columnType = column.columnType;
            var columnValue = column.columnValue;

            if(columnValue == "NULL" || columnValue == "null") {
                buffer[buffer.length] = "null";
            }
            else if(columnType == "number") {
                buffer[buffer.length] = columnValue;
            }
            else if(columnType == "string") {
                buffer[buffer.length] = "'" + sqlEscape(columnValue) + "'";
            }
            else{
                buffer[buffer.length] = "'" + sqlEscape(columnValue) + "'";
            }

            if(i + 1 < record.length) {
                buffer[buffer.length] = ", ";
            }
        }
        buffer[buffer.length] = ");";
        return buffer.join("");
    };

    SimpleDrag.register("editPanelTitle", "editPanel");

    jQuery("#source").change(function() {
        jQuery("#parseBtn").click();
    });

    jQuery("#columnList select[name=dataType]").live("change", function() {
        var tr = jQuery(this).closest("tr");
        tr.attr("columnType", this.value);
    });

    jQuery("#columnList input.column-value").live("change", function() {
        var tr = jQuery(this).closest("tr");
        var columnType = tr.attr("columnType");

        if(columnType == "number" && isNaN(this.value)) {
            this.value = "0";
            tr.attr("columnValue", "0");
        }
        else {
            tr.attr("columnValue", this.value);
        }
        jQuery("#result").val(getInsertSql());
    });

    jQuery("a.edit").live("click", function() {
        var tr = jQuery(this).closest("tr");
        var name = tr.attr("columnName");
        var value = tr.attr("columnValue");
        showEditPanel({"name": name, "value": value});
    });

    jQuery("a.delete").live("click", function() {
        jQuery(this).closest("tr").remove();
        resetOrderNum();
        jQuery("#result").val(getInsertSql());
    });

    jQuery("#editPanel span.close").live("click", function() {
        jQuery("#editPanel").hide();
    });

    jQuery("#editPanel input[name=ensure]").live("click", function() {
        var name = jQuery("#editPanel").attr("columnName");
        var value = jQuery("#editPanel textarea[name=content]").val();
        setColumnValue(name, value);
        jQuery("#result").val(getInsertSql());
        jQuery("#editPanel").hide();
    });

    jQuery("#editPanel input[name=cancel]").live("click", function() {
        jQuery("#editPanel").hide();
    });

    jQuery("#toolsBtn").click(function() {
        window.location.href = "/tools.html";
    });

    jQuery("#parseBtn").click(function() {
        var sql = jQuery("#source").val();

        jQuery.ajax({
            "type": "post",
            "url": "/ajax/editor/insertParse.html",
            "data": {"sql": sql},
            "dataType": "json",
            "error": function() {
            },
            "success": function(returnValue) {
                Response.success(returnValue, function(result) {
                    showColumnList(result);
                });
            }
        });
    });

    jQuery("#insertBtn").click(function() {
        jQuery("#result").val(getInsertSql());
    });

    jQuery(window).bind("resize", function() {
        var e = document.getElementById("columnPanel");

        if(e != null) {
            var offset = parseInt(e.getAttribute("offset-top"));

            if(isNaN(offset)) {
                offset = 280;
            }

            var height = document.documentElement.clientHeight - offset;
            e.style.height = height + "px";
        }
    });
    jQuery(window).trigger("resize");
});
//]]>
</script>
</head>
<body>
<div class="panel">
    <div class="panel-title"><h4><span class="icon"></span>Sql Editor</h4></div>
    <div class="menu-bar">
        <ul>
            <li><h4 id="toolsBtn" class="save" unselectable="on" title="工具箱">工具箱</h4></li>
            <li><h4 id="parseBtn" class="save" unselectable="on" title="解 析">解 析</h4></li>
            <li><h4 id="insertBtn" class="save" unselectable="on" title="生 成">生 成</h4></li>
        </ul>
    </div>
    <div class="panel-content">
        <div class="top-form" style="padding-top: 4px; padding-left: 4px;
            border-top: 1px solid #99bbe8;
            border-left: 1px solid #99bbe8;
            border-bottom: 0px solid #99bbe8;
            border-right: 1px solid #99bbe8;
            background-color: #dfe8f6;">
            <textarea id="source" class="text" style="width: 888px; height: 60px;" placeholder="请输入SQL语句"></textarea>
            <textarea id="result" class="text" style="width: 888px; height: 60px;" readonly="true"></textarea>
        </div>
        <div class="form-panel">
            <div>
                <table class="table" style="width: 1000px;">
                    <tr class="thead">
                        <td class="w30">&nbsp;</td>
                        <td class="w200">列名</td>
                        <td class="w100">类型</td>
                        <td class="w500">值</td>
                        <td class="center">操 作</td>
                    </tr>
                </table>
            </div>
            <div id="columnPanel" style="height: 360px; padding-top: 0px; overflow-x: hidden; overflow-y: auto;">
                <table id="columnList" class="table" style="width: 1000px;"></table>
            </div>
        </div>
    </div>
    <div class="status-bar"></div>
</div>

<div id="editPanel" class="panel hide" style="position: absolute; top: 20px; left: 240px;">
    <div class="panel-title">
        <h4 id="editPanelTitle">
            <span class="icon-table"></span>
            <span class="button close"></span>
        </h4>
    </div>
    <div class="panel-content">
        <div class="form-panel" style="width: 600px; height: 300px; overflow: auto; cursor: default;">
            <textarea name="content" class="text" style="width: 592px; height: 290px; border: none; overflow: auto;"></textarea>
        </div>
    </div>
    <div style="margin: 20px 10px 20px 0px; text-align: right;">
        <input name="ensure" type="image" src="/resource/images/ensure.gif"/>
        <input name="cancel" type="image" src="/resource/images/cancel.gif"/>
    </div>
</div>
</body>
</html>