var DataType = {};

DataType.getJavaType = function(dataType) {
    if(dataType == "VARCHAR") {
        return "String";
    }
    else if(dataType == "SMALLINT") {
        return "Integer";
    }
    else if(dataType == "TINYINT") {
        return "Integer";
    }
    else if(dataType == "INT") {
        return "Integer";
    }
    else if(dataType == "INTEGER") {
        return "Integer";
    }
    else if(dataType == "FLOAT") {
        return "Float";
    }
    else if(dataType == "DOUBLE") {
        return "Double";
    }
    else if(dataType == "LONG") {
        return "Long";
    }
    else if(dataType == "DATE") {
        return "Date";
    }
    else if(dataType == "TIME") {
        return "Date";
    }
    else if(dataType == "DATETIME") {
        return "Timestamp";
    }
    else if(dataType == "TIMESTAMP") {
        return "Timestamp";
    }
    else if(dataType == "CLOB") {
        return "String";
    }
    else if(dataType == "BLOB") {
        return "InputStream";
    }
    else {
        return "";
    }
};

DataType.getSqlType = function(javaType){
    if(javaType == "String") {
        return "VARCHAR(32)";
    }
    else if(javaType == "int" || javaType == "Integer") {
        return "INTEGER";
    }
    else if(javaType == "float" || javaType == "Float") {
        return "INTEGER";
    }
    else if(javaType == "double" || javaType == "Double") {
        return "DOUBLE";
    }
    else if(javaType == "long" || javaType == "Long") {
        return "LONG";
    }
    else if(javaType == "Date") {
        return "DATE|DATETIME|TIMESTAMP";
    }
    else if(javaType == "Timestamp") {
        return "TIMESTAMP";
    }
    else if(javaType == "Reader") {
        return "CLOB";
    }
    else if(javaType == "InputStream") {
        return "BLOB";
    }
    else if(javaType == "Clob") {
        return "TEXT|MEMO|CLOB";
    }
    else if(javaType == "Blob") {
        return "BLOB";
    }
    else {
        return "";
    }
};

DataType.getPrimitiveName = function(type) {
    if(type == "Character" || type == "java.lang.Character") {
        return "char";
    }
    else if(type == "Boolean" || type == "java.lang.Boolean") {
        return "boolean";
    }
    else if(type == "Byte" || type == "java.lang.Byte") {
        return "byte";
    }
    else if(type == "Short" || type == "java.lang.Short") {
        return "short";
    }
    else if(type == "Integer" || type == "java.lang.Integer") {
        return "int";
    }
    else if(type == "Float" || type == "java.lang.Float") {
        return "float";
    }
    else if(type == "Double" || type == "java.lang.Double") {
        return "double";
    }
    else if(type == "Long" || type == "java.lang.Long") {
        return "long";
    }
    return type;
};

DataType.getWrapName = function(type) {
    if(type == "char") {
        return "Character";
    }
    else if(type == "boolean") {
        return "Boolean";
    }
    else if(type == "byte") {
        return "Byte";
    }
    else if(type == "short") {
        return "Short";
    }
    else if(type == "int") {
        return "Integer";
    }
    else if(type == "float") {
        return "Float";
    }
    else if(type == "double") {
        return "Double";
    }
    else if(type == "long") {
        return "Long";
    }
    return type;
};

function TableEditor(id)
{
    this.tableId = id;
}

TableEditor.prototype.capitalize = function(name){
    if(name == null || name.length < 1) {
        return "";
    }

    return name.charAt(0).toUpperCase() + name.substring(1);
};

TableEditor.prototype.camel = function(name){
    var s = Util.trim(name);

    if(s == "") {
        return s;
    }

    var a = s.split("_");
    var result = [];

    for(var i = 0; i < a.length; i++) {
        result[result.length] = a[i].charAt(0).toUpperCase() + a[i].substring(1).toLowerCase();
    }

    return result.join("");
};

TableEditor.prototype.ontableNameChange = function(src){
    if(src != null && false) {
        var regex = /[^\x00-\xff]/ig

        var s = src.value.replace(regex, "");

        if(s.length < src.value.length) {
            return;
        }

        document.tableForm.tableCode.value = src.value;
        this.ontableCodeChange(document.tableForm.tableCode);
    }
};

TableEditor.prototype.ontableCodeChange = function(src){
    if(src == null || Util.trim(src.value) == "") {
        return;
    }

    var tableCode = Util.trim(src.value);

    src.value = tableCode;
    var oForm = document.tableForm;
    oForm.alias.value = tableCode;
    oForm.className.value = this.camel(tableCode);
    oForm.remarks.value = tableCode;
};

TableEditor.prototype.addColumn = function(){
    var table = document.getElementById(this.tableId);

    if(table != null) {
        var index = table.getAttribute("SelectedRowIndex");

        if(index != null) {
            index = parseInt(index);
        }
        else {
            index = -1;
        }

        var tr = table.insertRow(index);

        for(var i = 0; i < 10; i++) {
            tr.insertCell(-1);
        }

        tr.cells[0].className = "c1";
        tr.cells[0].innerHTML = tr.rowIndex;
        tr.cells[1].innerHTML = "<input type=\"text\" class=\"text\" style=\"width: 120px; height: 20px;\" value=\"\" onfocus=\"defaultTableEditor.onCellFocus(this)\" onchange=\"defaultTableEditor.onColumnNameChange(this)\"    onkeyup=\"defaultTableEditor.onCellKeyDown(this)\"/>";
        tr.cells[2].innerHTML = "<input type=\"text\" class=\"text\" style=\"width: 120px; height: 20px;\" value=\"\" onfocus=\"defaultTableEditor.onCellFocus(this)\" onchange=\"defaultTableEditor.onColumnNameChange(this)\"    onkeyup=\"defaultTableEditor.onCellKeyDown(this)\"/>";
        tr.cells[3].innerHTML = "<input type=\"text\" class=\"text\" style=\"width: 100px; height: 20px;\" value=\"\" onfocus=\"defaultTableEditor.onCellFocus(this)\" onchange=\"defaultTableEditor.onDataTypeChange(this)\"      onkeyup=\"defaultTableEditor.onCellKeyDown(this)\"/>";
        tr.cells[4].innerHTML = "<input type=\"text\" class=\"text\" style=\"width: 100px; height: 20px;\" value=\"\" onfocus=\"defaultTableEditor.onCellFocus(this)\" onchange=\"defaultTableEditor.onJavaNameChange(this)\"      onkeyup=\"defaultTableEditor.onCellKeyDown(this)\"/>";
        tr.cells[5].innerHTML = "<input type=\"text\" class=\"text\" style=\"width: 100px; height: 20px;\" value=\"\" onfocus=\"defaultTableEditor.onCellFocus(this)\" onchange=\"defaultTableEditor.onJavaTypeChange(this)\"      onkeyup=\"defaultTableEditor.onCellKeyDown(this)\"/>";
        tr.cells[6].innerHTML = "<input type=\"text\" class=\"text\" style=\"width: 60px; height: 20px;\" value=\"\"  onfocus=\"defaultTableEditor.onCellFocus(this)\" onchange=\"defaultTableEditor.onDataLengthChange(this)\"    onkeyup=\"defaultTableEditor.onCellKeyDown(this)\"/>";
        tr.cells[7].innerHTML = "<input type=\"text\" class=\"text\" style=\"width: 60px; height: 20px;\" value=\"\"  onfocus=\"defaultTableEditor.onCellFocus(this)\" onchange=\"defaultTableEditor.onDataPrecisionChange(this)\" onkeyup=\"defaultTableEditor.onCellKeyDown(this)\"/>";
        tr.cells[8].innerHTML = "<input type=\"checkbox\" value=\"0\" onclick=\"defaultTableEditor.onPrimaryKeyClick(this)\"/>";
        tr.cells[9].innerHTML = "<input type=\"checkbox\" value=\"0\"/>";
        tr.cells[8].className = "c1";
        tr.cells[9].className = "c1";

        var instance = this;
        tr.onclick = function(){
            instance.onColumnClick(this);
        };

        if(index > -1) {
            table.setAttribute("SelectedRowIndex", index + 1);

            for(var i = 0; i < table.rows.length; i++) {
                table.rows[i].cells[0].innerHTML = (i + 1);
            }

            this.onColumnClick(table.rows[index]);
        }
        else {
            this.onColumnClick(table.rows[table.rows.length - 1]);
        }
    }
};

TableEditor.prototype.onCellFocus = function(src, event){
    this.onColumnClick(Util.getParentElement(src, "tr"));
};

TableEditor.prototype.onCellKeyDown = function(src, event){
    if(src != null) {
        var e = (window.event != null ? window.event : evt);

        if(e != null) {
            var keyCode = (e.keyCode || e.which);

            if(keyCode == 37) {
                if(e.ctrlKey == true) {
                    var t = document.getElementById(this.tableId);

                    if(t != null) {
                        var r = Util.getParentElement(src, "tr");
                        var i = Util.getParentElement(src, "td").cellIndex;

                        if(i > 1) {
                            var c = Util.getChildNode(t.rows[r.rowIndex], "/td[" + (i - 1) + "]/input");
                            this.onColumnClick(Util.getParentElement(c, "tr"));
                            c.focus();
                            c.select();
                        }
                    }
                }
            }
            else if(keyCode == 39) {
                if(e.ctrlKey == true) {
                    var t = document.getElementById(this.tableId);

                    if(t != null) {
                        var r = Util.getParentElement(src, "tr");
                        var i = Util.getParentElement(src, "td").cellIndex;

                        if(i < r.cells.length - 3) {
                            var c = Util.getChildNode(t.rows[r.rowIndex], "/td[" + (i + 1) + "]/input");
                            this.onColumnClick(Util.getParentElement(c, "tr"));
                            c.focus();
                            c.select();
                        }
                    }
                }
            }
            else if(keyCode == 38) {
                if(e.ctrlKey == true) {
                    if(this.moveUp()) {
                        var t = document.getElementById(this.tableId);
                        var r = Util.getParentElement(src, "tr");
                        var c = Util.getParentElement(src, "td");
                        var d = t.rows[r.rowIndex - 1].cells[c.cellIndex];
                        Util.getChildNode(d, "input").focus();
                    }
                    return;
                }

                var t = document.getElementById(this.tableId);

                if(t != null) {
                    var r = Util.getParentElement(src, "tr");
                    var i = Util.getParentElement(src, "td").cellIndex;

                    if(r.rowIndex > 0) {
                        var c = Util.getChildNode(t.rows[r.rowIndex - 1], "/td[" + i + "]/input");
                        this.onColumnClick(Util.getParentElement(c, "tr"));
                        c.focus();
                        c.select();
                    }
                }
            }
            else if(keyCode == 40) {
                if(e.ctrlKey == true) {
                    if(this.moveDown()) {
                        var t = document.getElementById(this.tableId);
                        var r = Util.getParentElement(src, "tr");
                        var c = Util.getParentElement(src, "td");
                        var d = t.rows[r.rowIndex + 1].cells[c.cellIndex];
                        Util.getChildNode(d, "input").focus();
                        Util.getChildNode(d, "input").select();
                    }
                    return;
                }

                var t = document.getElementById(this.tableId);

                if(t != null) {
                    var r = Util.getParentElement(src, "tr");
                    var i = Util.getParentElement(src, "td").cellIndex;

                    if(r.rowIndex < t.rows.length - 1) {
                        var c = Util.getChildNode(t.rows[r.rowIndex + 1], "/td[" + i + "]/input");
                        this.onColumnClick(Util.getParentElement(c, "tr"));
                        c.focus();
                        c.select();
                    }
                }
            }
        }
    }
};

TableEditor.prototype.onColumnClick = function(src){
    if(src != null) {
        var r = src;
        var t = document.getElementById(this.tableId);

        if(t != null) {
            var index = t.getAttribute("SelectedRowIndex");

            if(r.rowIndex != index) {
                if(index != null) {
                    t.rows[parseInt(index)].style.backgroundColor = "#ffffff";
                }

                // #00ffff #316ac5 #d5c906 #e8f2fe #fcf35e
                t.setAttribute("SelectedRowIndex", r.rowIndex);
                r.style.backgroundColor = "#a6caf0";
            }
        }
    }
};

TableEditor.prototype.onColumnNameChange = function(src){
    if(src != null && false) {
        var regex = /[^\x00-\xff]/ig

        var s = src.value.replace(regex, "");

        if(s.length < src.value.length) {
            return;
        }

        Util.getChildNode(Util.getParentElement(src, "tr"), "/td[2]/input").value = src.value;
        Util.getChildNode(Util.getParentElement(src, "tr"), "/td[4]/input").value = this.camel(src.value);
    }
};

TableEditor.prototype.onColumnCodeChange = function(src){
    var fieldName = this.camel(src.value);

    if(Util.trim(fieldName) != "") {
        fieldName = fieldName.charAt(0).toLowerCase() + fieldName.substring(1);
        Util.getChildNode(Util.getParentElement(src, "tr"), "/td[4]/input").value = fieldName;
    }
};

TableEditor.prototype.onDataTypeClick = function(src){
    if(Util.trim(src.value) == "") {
        src.value = "VARCHAR(32)";
        this.onDataTypeChange(src);
        src.select();
    }
};

TableEditor.prototype.onDataTypeChange = function(src){
    if(src != null && !Util.isEmpty(src.value)) {
        var t = Util.trim(src.value)
        var s = Util.trim(src.value);
        var i = s.indexOf("(");
        var j = s.indexOf(")");

        if(i > -1 && j > -1) {
            var b = s.substring(i + 1, j).split(",");
            var a = [];

            for(var k = 0; k < 2 && k < b.length; k++) {
                a[k] = Util.num(b[k], 0, 0);
            }

            t = s.substring(0, i);
            s = t + "(" + a.join(", ") + ")";

            Util.getChildNode(Util.getParentElement(src, "tr"), "/td[6]/input").value = a[0];

            if(a.length == 2) {
                Util.getChildNode(Util.getParentElement(src, "tr"), "/td[7]/input").value = a[1];
            }
        }
        else {
            Util.getChildNode(Util.getParentElement(src, "tr"), "/td[6]/input").value = "";
            Util.getChildNode(Util.getParentElement(src, "tr"), "/td[7]/input").value = "";
        }

        t = t.toUpperCase();
        s = s.toUpperCase();
        src.value = s;
        Util.getChildNode(Util.getParentElement(src, "tr"), "/td[5]/input").value = DataType.getJavaType(t);
    }
};

TableEditor.prototype.onJavaNameChange = function(src){
};

TableEditor.prototype.onJavaTypeChange = function(src){
};

TableEditor.prototype.onDataLengthChange = function(src){
    if(src != null && !Util.isEmpty(src.value)) {
        if(!Util.IsNumeric(src.value)) {
            alert("DataLength must be a numeric !");
            src.focus();
            src.select();
        }
    }
};

TableEditor.prototype.onDataPrecisionChange = function(src){
    if(src != null && !Util.IsEmpty(src.value)) {
        if(!Util.IsNumeric(src.value)) {
            alert("DataPrecision must be a numeric !");
            src.focus();
            src.select();
        }
    }
};

TableEditor.prototype.onPrimaryKeyClick = function(src){
    if(src.checked == true) {
        Util.getChildNode(Util.getParentElement(src, "tr"), "/td[9]/input").checked = true;
    }
};

TableEditor.prototype.deleteColumn = function(){
    var t = document.getElementById(this.tableId);

    if(t != null) {
        var index = t.getAttribute("SelectedRowIndex");

        if(index != null) {
            // IE: t.rows[parseInt(index)].removeNode(true);
            // Chrome: t.rows[parseInt(index)].remove(true);
            var node = t.rows[parseInt(index)];
            node.parentNode.removeChild(node);
            t.removeAttribute("SelectedRowIndex");

            for(var i = 0; i < t.rows.length; i++) {
                t.rows[i].cells[0].innerHTML = (i + 1);
            }

            if(index < t.rows.length) {
                this.onColumnClick(t.rows[index]);
            }
            else {
                if(t.rows.length > 1) {
                    this.onColumnClick(t.rows[index - 1]);
                }
            }
        }
    }
};

TableEditor.prototype.deleteCheckedColumn = function(){
    this.deleteColumn();
};

TableEditor.prototype.setColumnValue = function(r, column){
    r.setAttribute("jso", JSON.stringify(column));
    r.cells[1].childNodes[0].value = Util.trim(column.columnName);
    r.cells[2].childNodes[0].value = Util.trim(column.columnCode);
    r.cells[3].childNodes[0].value = Util.trim(column.typeName);
    r.cells[4].childNodes[0].value = Util.trim(column.variableName);
    r.cells[5].childNodes[0].value = Util.trim(column.javaTypeName);
    r.cells[6].childNodes[0].value = Util.trim(column.columnSize);
    r.cells[7].childNodes[0].value = Util.trim(column.dataPrecision);
};

TableEditor.prototype.getColumnValue = function(r){
    var column = Util.getJso(r);

    if(column == null) {
        column = {};
    }

    column.columnName = Util.trim(r.cells[1].childNodes[0].value);
    column.columnCode = Util.trim(r.cells[2].childNodes[0].value);
    column.typeName = Util.trim(r.cells[3].childNodes[0].value);
    column.variableName = Util.trim(r.cells[4].childNodes[0].value);
    column.javaTypeName = Util.trim(r.cells[5].childNodes[0].value);
    column.columnSize = Util.trim(r.cells[6].childNodes[0].value);
    column.dataPrecision = Util.trim(r.cells[7].childNodes[0].value);
    column.primaryKey = (r.cells[8].childNodes[0].checked == true ? 1 : 0);
    column.nullable = (r.cells[9].childNodes[0].checked == true ? 0 : 1);
    column.methodSetter = "set" + this.capitalize(column.variableName);
    column.methodGetter = "get" + this.capitalize(column.variableName);
    column.alias = column.columnCode;
    column.orderNo = r.rowIndex;
    return column;
};

TableEditor.prototype.swapRow = function(r, n){
    var c1 = this.getColumnValue(r);
    var c2 = this.getColumnValue(n);
    this.setColumnValue(r, c2);
    this.setColumnValue(n, c1);

    var t = document.getElementById(this.tableId);

    if(t != null) {
        for(var i = 0; i < t.rows.length; i++) {
            t.rows[i].cells[0].innerHTML = (i + 1);
        }
    }
};

TableEditor.prototype.moveUp = function(){
    var t = document.getElementById(this.tableId);

    if(t != null) {
        var index = t.getAttribute("SelectedRowIndex");

        if(index != null) {
            var i = parseInt(index);

            if(i > 0) {
                this.swapRow(t.rows[i], t.rows[i - 1]);
                this.onColumnClick(t.rows[i - 1]);
                return true;
            }
        }
    }

    return false;
};

TableEditor.prototype.moveTop = function(){
    var t = document.getElementById(this.tableId);

    if(t != null) {
        var index = t.getAttribute("SelectedRowIndex");

        if(index != null) {
            var i = parseInt(index);

            if(i > 0) {
                for(var j = i; j > 1; j--) {
                    this.swapRow(t.rows[j], t.rows[j - 1]);
                }

                this.onColumnClick(t.rows[0]);
                return true;
            }
        }
    }

    return false;
};

TableEditor.prototype.moveDown = function(){
    var t = document.getElementById(this.tableId);

    if(t != null) {
        var index = t.getAttribute("SelectedRowIndex");

        if(index != null) {
            var i = parseInt(index);

            if(i < t.rows.length - 1) {
                this.swapRow(t.rows[i], t.rows[i + 1]);
                this.onColumnClick(t.rows[i + 1]);
                return true;
            }
        }
    }

    return false;
};

TableEditor.prototype.moveBottom = function(){
    var t = document.getElementById(this.tableId);

    if(t != null) {
        var index = t.getAttribute("SelectedRowIndex");

        if(index != null) {
            var i = parseInt(index);

            if(i < t.rows.length - 1) {
                for(var j = i; j < t.rows.length - 1; j++) {
                    this.swapRow(t.rows[j], t.rows[j + 1]);
                }

                this.onColumnClick(t.rows[t.rows.length - 1]);
                return true;
            }
        }
    }

    return false;
};

TableEditor.prototype.getTable = function(){
    var table = {};
    table.alias = Util.trim(document.tableForm.alias.value);
    table.tableName = Util.trim(document.tableForm.tableName.value);
    table.tableCode = Util.trim(document.tableForm.tableCode.value);
    table.className = Util.trim(document.tableForm.className.value);
    table.variableName = table.className.charAt(0).toLowerCase() + table.className.substring(1);
    table.remarks = Util.trim(document.tableForm.remarks.value);

    if(Util.trim(table.queryName) == "") {
        table.queryName = table.tableName;
    }
    return table;
};

TableEditor.prototype.getColumnList = function(filter){
    var columns = [];
    var table = document.getElementById(this.tableId);

    if(table != null) {
        var rows = table.rows;

        for(var i = 0; i < rows.length; i++) {
            var column = this.getColumnValue(rows[i]);

            if(Util.trim(column.columnCode) == "") {
                continue;
            }

            if(filter == null || filter(column)) {
                columns[columns.length] = column;
            }
        }
    }
    return columns;
};

TableEditor.prototype.getTemplate = function(r){
    var template = Util.getJso(r);

    if(template != null) {
        template.enabled = Util.getChildNode(r, "/td[0]/input[0]").checked;
        template.path = Util.getChildNode(r, "/td[1]/input[0].value");
        template.output = Util.getChildNode(r, "/td[1]/input[1].value");
        template.parameters = this.getParameters(r);
    }

    return template;
};

TableEditor.prototype.getParameters = function(r){
    var t = Util.getParentElement(r, "table");
    var n = r.rowIndex + 1;

    if(n < t.rows.length) {
        var t2 = Util.getChildNode(t.rows[n], "/td[1]/table");

        if(t2 != null && t2.rows.length > 0) {
            var list = [];
            var rows = t2.rows;

            for(var i = 0; i < rows.length; i++) {
                var parameter = Util.getJso(rows[i]);

                if(parameter != null) {
                    parameter.Value = Util.getChildNode(rows[i], "/td[1]/input[0].value");
                    list[list.length] = parameter;
                }
            }

            return list;
        }
    }

    return null;
};

TableEditor.prototype.getCreateSql = function(pattern){
    var table = this.getTable();

    if(table != null) {
        var buffer = [];
        var maxLength = 0;
        var columns = this.getColumnList();
        buffer[buffer.length] = "create table " + pattern.replace("%s", table.tableName) + "(\r\n";

        for(var i = 0; i < columns.length; i++) {
            var column = columns[i];

            if(column.columnName.length > maxLength) {
                maxLength = column.columnName.length;
            }
        }

        maxLength = maxLength + 4;

        for(var i = 0; i < columns.length; i++) {
            var column = columns[i];
            var columnName = pattern.replace("%s", column.columnName);
            buffer[buffer.length] = "    " + Util.padding(columnName, maxLength, " ") + " " + column.typeName;

            if(column.columnSize > 0) {
                buffer[buffer.length] = "(" + column.columnSize + ")";
            }

            if(column.autoIncrement == 1) {
                buffer[buffer.length] = " auto_increment";
            }

            if(column.primaryKey == 1 || column.nullable == 0) {
                buffer[buffer.length] = " not null";
            }

            if(!Util.isEmpty(column.remarks)) {
                buffer[buffer.length] = " comment '" + column.remarks + "'";
            }

            if(i < columns.length - 1) {
                buffer[buffer.length] = ",\r\n";
            }
            else {
                buffer[buffer.length] = "\r\n";
            }
        }
        buffer[buffer.length] = ");";
        return buffer.join("");
    }

    return "";
};

TableEditor.prototype.getInsertSql = function(pattern){
    var table = this.getTable();

    if(table != null) {
        var buffer = [];
        var columns = this.getColumnList();
        buffer[buffer.length] = "insert into " + pattern.replace("%s", table.tableName) + "(";

        for(var i = 0; i < columns.length; i++) {
            var column = columns[i];
            var columnName = pattern.replace("%s", column.columnName);
            buffer[buffer.length] = columnName;

            if(i < columns.length - 1) {
                buffer[buffer.length] = ", ";
            }
        }

        buffer[buffer.length] = ") values (";

        for(var i = 0; i < columns.length; i++) {
            var column = columns[i];
            var columnName = column.columnName;

            if(i < columns.length - 1) {
                buffer[buffer.length] = "'" + columnName + "', ";
            }
            else {
                buffer[buffer.length] = "'" + columnName + "'";
            }
        }
        buffer[buffer.length] = ");";
        return buffer.join("");
    }
    return "";
};

TableEditor.prototype.getUpdateSql = function(pattern){
    var table = this.getTable();

    if(table != null) {
        var buffer = [];
        var columns = this.getColumnList(function(column) {console.log(column.columnName + ": " + column.primaryKey); return (column.primaryKey == 0);});
        buffer[buffer.length] = "update " + pattern.replace("%s", table.tableName) + " set\r\n";

        for(var i = 0; i < columns.length; i++) {
            var column = columns[i];
            var columnName = pattern.replace("%s", column.columnName);

            if(i < columns.length - 1) {
                buffer[buffer.length] = "    " + columnName + "='xxx',\r\n";
            }
            else {
                buffer[buffer.length] = "    " + columnName + "='xxx'\r\n";
            }
        }
        buffer[buffer.length] = "where id=?";
        return buffer.join("");
    }

    return "";
};

TableEditor.prototype.getTableXml = function(){
    var table = this.getTable();

    if(table != null) {
        var xmlDocument = Ajax.createXmlDocument("table-definition", true);
        var root = xmlDocument.documentElement;
        var e1 = xmlDocument.createElement("table");
        root.appendChild(e1);
        e1.setAttribute("alias", Util.trim(table.alias));
        e1.setAttribute("tableName", Util.trim(table.tableName));
        e1.setAttribute("tableCode", Util.trim(table.tableCode));
        e1.setAttribute("tableType", Util.trim(table.tableType));
        e1.setAttribute("queryName", Util.trim(table.queryName));
        e1.setAttribute("className", Util.trim(table.className));
        e1.setAttribute("variableName", Util.trim(table.variableName));
        e1.setAttribute("remarks", Util.trim(table.remarks));
        root.setAttribute("encoding", Util.trim(document.tableForm.encoding.value));

        var t1 = document.getElementById(this.tableId);

        if(t1 != null) {
            var rows = t1.rows;

            for(var i = 0; i < rows.length; i++) {
                var column = this.getColumnValue(rows[i]);

                if(Util.trim(column.columnCode) != "") {
                    var e = xmlDocument.createElement("column");
                    e.setAttribute("alias", Util.trim(column.alias));
                    e.setAttribute("columnCode", Util.trim(column.columnCode));
                    e.setAttribute("columnName", Util.trim(column.columnName));
                    e.setAttribute("dataType", Util.trim(column.dataType));
                    e.setAttribute("typeName", Util.trim(column.typeName));
                    e.setAttribute("columnSize", Util.trim(column.columnSize));
                    e.setAttribute("autoIncrement", Util.trim(column.autoIncrement));
                    e.setAttribute("decimalDigits", Util.trim(column.decimalDigits));
                    e.setAttribute("columnDef", Util.trim(column.columnDef));
                    e.setAttribute("dataPrecision", Util.trim(column.dataPrecision));
                    e.setAttribute("primaryKey", Util.trim(column.primaryKey));
                    e.setAttribute("nullable", Util.trim(column.nullable));
                    e.setAttribute("variableName", Util.trim(column.variableName));
                    e.setAttribute("javaTypeName", Util.trim(column.javaTypeName));
                    e.setAttribute("methodSetter", Util.trim(column.methodSetter));
                    e.setAttribute("methodGetter", Util.trim(column.methodGetter));
                    e.setAttribute("remarks", Util.trim(column.remarks));
                    e.setAttribute("orderNo", Util.trim(column.orderNo));
                    e1.appendChild(e);
                }
            }
        }

        var t2 = document.getElementById("templateTable");

        if(t2 != null) {
            var rows = t2.rows;
            var e2 = xmlDocument.createElement("templates");
            root.appendChild(e2);

            for(var i = 1; i < rows.length; i += 2) {
                var template = this.getTemplate(rows[i]);

                if(template != null && Util.trim(template.name) != "") {
                    var e = xmlDocument.createElement("template");
                    e.setAttribute("name", Util.trim(template.name));
                    e.setAttribute("path", Util.trim(template.path));
                    e.setAttribute("output", Util.trim(template.output));
                    e.setAttribute("enabled", (template.enabled == true ? "true" : "false"));
                    e2.appendChild(e);

                    var parameters = template.parameters;

                    if(parameters != null && parameters.length > 0) {
                        for(var j = 0; j < parameters.length; j++) {
                            var parameter = parameters[j];

                            if(Util.trim(parameter.name) != "" && parameter.value != null) {
                                var ele = xmlDocument.createElement("parameter");
                                ele.setAttribute("name", Util.trim(parameter.name));
                                ele.setAttribute("value", Util.trim(parameter.value));
                                ele.setAttribute("description", Util.trim(parameter.description));
                                e.appendChild(ele);
                            }
                        }
                    }
                }
            }
        }

        if(typeof(ActiveXObject) != "undefined" && typeof(ActiveXObject) != "null") {
            return xmlDocument.xml;
        }
        else {
            return new XMLSerializer().serializeToString(xmlDocument);
        }
    }

    return "";
};

TableEditor.prototype.validate = function(){
    var form = document.tableForm;

    if(Util.isEmpty(form.tableCode.value)) {
        DefaultTabView.Activate("TabLabel_1");
        alert("请填写表名 !");
        form.tableCode.focus();
        form.tableCode.select();
        return false;
    }

    if(form.tableCode.getAttribute("Used") == "1") {
        DefaultTabView.Activate("TabLabel_1");
        alert(oForm.tableCode.value + "已经存在, 请使用其他的名字 !");
        form.tableCode.focus();
        form.tableCode.select();
        return false;
    }

    return true;
};

TableEditor.prototype.wrap = function(checked) {
    var table = document.getElementById(this.tableId);
    var rows = table.rows;

    for(var i = 0; i < rows.length; i++) {
        var c = Util.getChildNode(rows[i], "/td[5]/input");

        if(c != null) {
            if(checked) {
                c.value = DataType.getWrapName(c.value);
            }
            else {
                c.value = DataType.getPrimitiveName(c.value);
            }
        }
    }
};

TableEditor.prototype.test = function(checked) {
    var table = document.getElementById(this.tableId);

    if(table != null) {
        var r = table.rows.length;

        for(var i = 0; i < 11 - r; i++) {
            this.addColumn();
        }

        this.onColumnClick(table.rows[0]);

        if(r == 1) {
            var c1 = {columnName: "USER_NAME", columnCode: "USER_NAME", dataType: "VARCHAR(64)", variableName: "userName", javaTypeName: "String", columnSize: 64};
            var c2 = {columnName: "USER_PASSWORD", columnCode: "USER_PASSWORD", dataType: "VARCHAR(64)", variableName: "userPassword", javaTypeName: "String", columnSize: 64};
            this.setColumnValue(t.rows[1], c1);
            this.setColumnValue(t.rows[2], c2);
            alert("注意, 前两列是测试数据 !");
        }
    }
};
