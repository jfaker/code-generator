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

    var getTypeName = function(javaTypeName) {
        var a = javaTypeName;

        if(a == "boolean" || a == "byte" || a == "short" || a == "int" || a == "float" || a == "double"|| a == "long") {
            return "number";
        }

        if(a == "char" || a == "String") {
            return "string";
        }

        if(a == "java.util.Date" || a == "java.sql.Date" || a == "java.sql.Timestamp") {
            return "datatime";
        }
        return "unknown";
    }

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
            var columnName = column.columnName;
            var columnValue = column.columnValue;
            var columnType = getTypeName(column.javaTypeName);
            var typeName = "data-type-string";

            if(columnType == "number") {
                typeName = "data-type-int";
            }
            else if (columnType == "string"){
                typeName = "data-type-string";
            }
            else if (columnType == "datatime"){
                typeName = "data-type-date";
            }
            else{
                typeName = "data-type-unknown";
            }

            td1.className = "w30";
            td2.className = "w200";
            td3.className = "w100";
            td4.className = "w500";
            td5.className = "center";

            td1.innerHTML = i + 1;
            td2.innerHTML = "<input class=\"" + typeName + "\" readonly=\"true\" value=\"" + columnName + "\"/>";
            td3.innerHTML = getOptionsHtml("dataType", columnType);
            td4.innerHTML = "<input name=\"" + columnName + "\" type=\"text\" class=\"column-value\" value=\"" + HtmlUtil.encode(columnValue, "\r\n") + "\"/>";
            td5.innerHTML = "<a class=\"edit\" href=\"javascript:void(0)\">编 辑</a>&nbsp;&nbsp;<a class=\"delete\" href=\"javascript:void(0)\">删 除</a>";

            tr.setAttribute("columnType", columnType);
            tr.setAttribute("columnName", columnName);
            tr.setAttribute("columnValue", (columnValue || ("[" + columnName + "]")));
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

            if(columnType == "unknown") {
                columnType = "string";
            }

            if(columnType == "number") {
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
            "url": "/ajax/editor/createParse.html",
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
            <textarea id="source" class="text" style="width: 888px; height: 60px;" placeholder="请输入SQL语句">
create table tv_app(
    id bigint(20) unsigned not null auto_increment comment '主键',
    name varchar(64) NOT NULL COMMENT '应用名称, 不可修改',
    display_name varchar(64) DEFAULT NULL COMMENT '应用的显示名称，用户可以修改',
    abbr varchar(32) not null comment '应用名称首字母',
    type int(11) not null comment '应用类型',
    is_game int(11) not null default 0 comment '是否游戏',
    is_sarft int(11) not null default 0 comment '是否运营商',
    tag varchar(128) not null comment '应用标签，多个使用空格分隔',
    summary varchar(2048) default null comment '应用简介',
    dev_nick varchar(64) default null COMMENT '开发者昵称',
    pay_type int(11) not null default '0' comment '付费类型, 0: 免费, 1: 内置收费, 2: 付费下载',
    price int(11) not null default '0' comment '价格',
    top_app_id bigint(20) unsigned default '0' comment 'top的app_id',
    app_key varchar(64) not null comment 'app key',
    app_secret varchar(128) comment 'app secret',
    baodian_secret varchar(128) default null comment '宝点的secret',
    status int(11) not null default '1' comment '状态 1：开发中 2：审核中 3：审核通过 4：审核不通过 5：撤销',
    apk_url varchar(255) default null comment '应用下载地址（母包）',
    ver_name varchar(64) default null comment '版本名称',
    ver_code int(11) default null comment '版本编号',
    package_name varchar(128) default null comment '应用包名',
    package_size varchar(128) default null comment '应用包大小',
    md5 varchar(128) default null comment 'md5',
    sha1 varchar(128) default null comment 'sha1',
    channel_ids varchar(1024) default null comment '下发渠道',
    tb_uid bigint(20) not null comment '淘宝用户id',
    deg_uid bigint(20) not null comment '数娱用户id',
    wlc_status varchar(32) default '0' comment '无量尺扫描状态',
    mtl_status varchar(32) default '0' comment '摩天轮自动测试结果',
    online_time date default null comment '期望的上线时间',
    sdk_version varchar(32) default null comment 'sdk版本',
    tech_person varchar(32) default null comment '技术接口人',
    tech_person_phone varchar(32) default null comment '技术接口人电话',
    biz_person varchar(32) default null comment '客服接口人',
    biz_person_phone varchar(32) default null comment '客服接口人电话',
    current_step int(11) default '0' comment '当前第几步, 记录用户填写步骤',
    contract_status int(11) default '0' comment '合同签署状态, 0-未签署合同, 1-已签署合同',
    operator_id bigint(20) default null comment '审核者id',
    operator_name varchar(64) default null comment '审核者',
    data_from varchar(64) default 'openplatform' comment '数据来源，tvappstore是导的数据',
    gmt_create datetime not null comment '创建时间',
    gmt_modified datetime not null comment '更新时间',
    primary key (id),
    unique key index_name (name),
    unique key index_appkey (app_key),
    key index_abbr (abbr)
) engine=innodb default charset=utf8 comment='开发者应用表';
            </textarea>
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