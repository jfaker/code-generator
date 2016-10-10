/**
 * Copyright (C) 2003-2008 xuesong.net
 * http://www.j2x.org
 * Author: xuesong.net
 *
 **/
var Util = {version: "1.0.0.0"};

Util.trim = function(s){
    return (s != null ? new String(s).replace(/(^\s*)|(\s*$)/g, "") : "");
};

/**
 * @param source
 * @param length
 * @param pad
 * @return String
 */
Util.padding = function(source, length, pad){
    var buffer = [source];
    var count = source.length;

    while(count < length)
    {
        buffer[buffer.length] = pad;
        count += pad.length;
    }

    var result = buffer.join("");

    if(result.length > length)
    {
        return result.substring(0, length);
    }

    return result;
};

Util.isEmpty = function(s){
    return (Util.trim(s).length < 1);
};

Util.equals = function(){
    var args = Util.equals.arguments;

    if(args.length > 0)
    {
        var s = args[0];

        for(var i = 1; i < args.length; i++)
        {
            if(s != args[i])
            {
                return false;
            }
        }

        return true;
    }

    return false;
};

Util.isNumeric = function(s){
    return (isNaN(parseFloat(s)) == false);
};

Util.numberFormat = function(_n, _p){
    var _number = parseFloat(_n, 10);
    var _precision = null;

    if(_p == null)
    {
        _precision = 2;
    }
    else
    {
        _precision = parseFloat(_p);
    }

    if(isNaN(_number))
    {
        return _number;
    }

    if(isNaN(_precision))
    {
        return _number;
    }

    if(_precision < 0)
    {
        _precision = 0;
    }

    var _r = Math.pow(10, _precision)

    var _f = _number * _r;

    _f = Math.round(_f) / _r;
        
    return _f;
};

Util.num = function(a, b, p){
    var x = parseFloat(a, 10);
    var y = parseFloat(b, 10);

    var v = (isNaN(x) ? y : x);

    if(p != null && !isNaN(parseInt(p, 10)))
    {
        v = Util.numberFormat(v, parseInt(p, 10));
    }

    return v;
};

Util.sum = function(a, b, p){
    var x = Util.num(a, 0);
    var y = Util.num(b, 0);
    var v = x + y;

    if(p != null && !isNaN(parseInt(p, 10)))
    {
        v = Util.numberFormat(v, parseInt(p, 10));
    }

    return v;
};

Util.sub = function(a, b, p)
{
    var x = Util.num(a, 0);
    var y = Util.num(b, 0);

    var v = x - y;

    if(p != null && !isNaN(parseInt(p, 10)))
    {
        v = Util.numberFormat(v, parseInt(p, 10));
    }

    return v;
};

Util.mul = function(a, b, p){
    var x = Util.num(a, 0);
    var y = Util.num(b, 0);
    var v = x * y;

    if(p != null && !isNaN(parseInt(p, 10)))
    {
        v = Util.numberFormat(v, parseInt(p, 10));
    }

    return v;
};

Util.div = function(a, b, p){
    var x = Util.num(a, 0);
    var y = Util.num(b, 0);

    if(y == 0.00)
    {
        return 0.00;
    }
    else
    {
        var v = x / y;

        if(p != null && !isNaN(parseInt(p, 10)))
        {
            v = Util.numberFormat(v, parseInt(p, 10));
        }

        return v;
    }
};

Util.mod = function(a, b, p){
    var x = Util.num(a, 0);
    var y = Util.num(b, 0);

    if(y == 0.00)
    {
        return 0.00;
    }
    else
    {
        var v = x % y;

        if(p != null && !isNaN(parseInt(p, 10)))
        {
            v = Util.numberFormat(v, parseInt(p, 10));
        }

        return v;
    }
};

Util.getJso = function(e){
    return (e != null ? Util.eval(e.getAttribute("jso")) : null);
};

Util.eval = function(s){
    var o = null;

    if(s != null)
    {
        try
        {
            if(typeof(s) == "string")
            {
                o = window.eval("(" + s + ")");
            }
            else if(typeof(s) == "object")
            {
                o = window.eval("(" + s.getAttribute("jso") + ")");
            }
        }
        catch(e)
        {
        }
    }

    return o;
};

Util.getChildNode = function(oNode, sPath){
    var _name  = "";
    var _index = 0;

    var _node  = oNode;
    for(var i = 0; i < sPath.length; i++)
    {
        var ch = sPath.charAt(i);
        if(("a" <= ch && ch <= "z") || ("A" <= ch && ch <= "Z"))
        {
            sPath = sPath.substring(i);
            break;
        }
    }

    for(var i = 0; _node != null && i < sPath.length; i++)
    {
        var ch = sPath.charAt(i);

        if(ch == " ")
        {
            continue;
        }
        else if(ch == "[")
        {
            var sIndex = "";

            for(i++; i < sPath.length; i++)
            {
                ch = sPath.charAt(i);

                if(ch == " ")
                {
                    continue;
                }
                else if(ch == "]")
                {
                    break;
                }

                sIndex += ch;
            }

            if(ch != "]")
            {
                throw {name: "XPathParseException", message: "XPathParseException", description: "XPathParseException"};
            }

            _index = parseInt(sIndex);

            if(isNaN(_index) == true)
            {
                throw {name: "XPathParseException", message: "XPathParseException", description: "XPathParseException"};
            }
        }
        else if(ch == "{")
        {
            var expression = "";

            for(i++; i < sPath.length; i++)
            {
                ch = sPath.charAt(i);

                if(ch == " ")
                {
                    continue;
                }
                else if(ch == "}")
                {
                    break;
                }

                expression += ch;
            }

            if(ch != "}")
            {
                throw {name: "XPathParseException", message: "XPathParseException", description: "XPathParseException"};
            }

            _node = this.getChildByExpression(_node, _name, expression);
            _name  = "";
            _index = 0;
        }
        else if(ch == "/")
        {
            _node = this.getChild(_node, _name, _index);
            _name  = "";
            _index = 0;
        }
        else if(ch == ".")
        {
            _node = this.getChild(_node, _name, _index);
            _name  = "";
            _index = 0;
            var sName = "";

            for(i++; i < sPath.length; i++)
            {
                ch = sPath.charAt(i);

                if(("a" <= ch && ch <= "z") || ("A" <= ch && ch <= "Z"))
                {
                    sName += ch;
                }
                else
                {
                    break;
                }
            }

            if(_node != null && sName != "")
            {
                _node = _node.getAttribute(sName);
            }
        }
        else
        {
            _name += ch;
        }
    }

    if(_node != null && _name != null && _name != "")
    {
        _node = this.getChild(_node, _name, _index);
    }

    return _node;
};

Util.getChild = function(element, nodeName, index){
    try
    {
        var i = index;
        var list = element.getElementsByTagName(nodeName);

        if(i < 0)
        {
            i = list.length + i;
        }

        if(i >= 0 && i < list.length)
        {
            return list[i];
        }
    }
    catch(e)
    {
    }

    return null;
};

Util.getChildByExpression = function(obj, childName, expression){
    if(expression != null)
    {
        try
        {
            var attr = Util.Trim(expression.substring(0, expression.indexOf("=")));
            var valu = Util.Trim(expression.substring(expression.indexOf("=") + 1));

            if(valu.length > 0 && valu.charAt(0) == "\"")
            {
                valu = valu.substring(1);
            }

            if(valu.length > 0 && valu.charAt(valu.length - 1) == "\"")
            {
                valu = valu.substring(0, valu.length - 1);
            }

            var list = obj.childNodes;
            var size = list.length;

            for(var i = 0; i < size; i++)
            {
                if(list[i].nodeType == 1 && list[i].nodeName.toLowerCase() == childName.toLowerCase())
                {
                    if(list[i].getAttribute(attr) == valu)
                    {
                        return list[i];
                    }
                }
            }
        }
        catch(e){}
    }
    else
    {
        return this.getChild(obj, childName, 0);
    }

    return null;
};

Util.getParentElement = function(_e, _n, _i, _c){
    _n = _n.toLowerCase();

    if(_e != null)
    {
        while(_e != null && (_e != _e.parentNode) && (_e = _e.parentNode) != null && _e.nodeName.toLowerCase() != _n);
    }

    if(_e != null && _i != null)
    {
        if(_c == null)
        {
            _c = 1;
        }
        
        if(_c < _i)
        {
            _e = this.getParentElement(_e, _n, _i, _c + 1);
        }
    }

    return _e;
};

Util.getPreviousSibling = function(_e, _n){
    _n = _n.toLowerCase();
    while ((_e != _e.previousSibling) && (_e = _e.previousSibling) != null && _e.nodeName.toLowerCase() != _n);
    return _e;
};

Util.getChildNodes = function(id, name){
    var result = [];
    var element = document.getElementById(id);

    if(element != null)
    {
        var list = element.childNodes;

        for(var i = 0; i < list.length; i++)
        {
            var e = list[i];

            if(list[i].nodeType == 1 && e.nodeName.toLowerCase() == name)
            {
                result[result.length] = list[i];
            }
        }
    }

    return result;
};

Util.checkChildNodes = function(id, src, checkedEvent){
    var e = document.getElementById(id);

    if(e != null)
    {
        var tmp = src.getAttribute("checked");
        var checked = (tmp != null && tmp.toString() == "true" ? true : false);

        var list = e.getElementsByTagName("input");

        for(var i = 0; i < list.length; i++)
        {
            list[i].checked = checked;

            if(checkedEvent != null)
            {
                checkedEvent(list[i]);
            }
        }

        var type = src.getAttribute("type");
        e.setAttribute("checked", !checked);

        if(type != null && type.toLowerCase() != "checkbox")
        {
            src.setAttribute("checked", !checked);
        }
    }
};

Util.check = function(name, checked){
    var list = document.getElementsByName(name);

    if(list == null || list == undefined)
    {
        return false;
    }

    if(list.length == null || list.length == undefined)
    {
        list.checked = checked;
        return true;
    }

    for(var i=0; i < list.length; i++)
    {
        list[i].checked = checked;
    }
};
Util.show = function(id){
    try
    {
        var e = document.getElementById(id);

        if(e != null)
        {
            e.style.display = "block";
        }
    }
    catch(e){}
};

Util.hide = function(id){
    try
    {
        var e = document.getElementById(id);

        if(e != null)
        {
            e.style.display = "none";
        }
    }
    catch(e){}
};

Util.addEventListener = function(target, type, handler) {
    var method = function(event){
        return handler.call(target, (event || window.event));
    }

    if(target.attachEvent) {
        target.attachEvent("on" + type, method);
    }
    else if(target.addEventListener) {
        target.addEventListener(type, method, false);
    }
    else {
        target["on" + type] = method;
    }
};

Util.stopPropagation = function(event) {
    if(event != null) {
        if(event.stopPropagation) {
            event.stopPropagation();
        }
        else {
            event.cancelBubble = true;
        }

        if(event.preventDefault) {
            event.preventDefault();
        }
        event.cancel = true;
        event.returnValue = false;
    }
    return false;
};

Util.setCookie = function(cookie) {
    var expires = "";

    if(cookie.expires != null) {
        var date;

        if(typeof(cookie.expires) == "number") {
            date = new Date();
            date.setTime(date.getTime() + (cookie.expires * 1000));
        }
        else if(cookie.expires.toUTCString != null)
        {
            date = cookie.expires;
        }

        // use expires attribute, max-age is not supported by IE
        if(date != null) {
            expires = "; expires=" + date.toUTCString();
        }
    }
    var path = cookie.path ? "; path=" + (cookie.path) : "";
    var domain = cookie.domain ? "; domain=" + (cookie.domain) : "";
    var secure = cookie.secure ? "; secure" : "";
    document.cookie = [cookie.name, "=", encodeURIComponent(cookie.value), expires, path, domain, secure].join("");
};

Util.getCookie = function(name){
    var value = null;

    if(document.cookie && document.cookie != "") {
        var cookies = document.cookie.split(';');

        for(var i = 0; i < cookies.length; i++) {
            var cookie = Util.trim(cookies[i]);

            if(cookie.substring(0, name.length + 1) == (name + "=")) {
                value = decodeURIComponent(cookie.substring(name.length + 1));
                break;
            }
        }
    }
    return value;
};

var HtmlUtil = {};

HtmlUtil.replacement = {
    "<": "&lt;",
    ">": "&gt;",
    "\"": "&quot;",
    "\u00ae": "&reg;",
    "\u00a9": "&copy;"
};

HtmlUtil.replace = function(e){return HtmlUtil.replacement[e];};

HtmlUtil.encode = function(source, crlf) {
    if(source == null) {
        return "";
    }

    if(crlf == null){
        crlf = "";
    }

    if(typeof(source) != "string") {
        source = source.toString();
    }
    return source.replace(new RegExp("[<>\"\\u00ae\\u00a9]", "g"), HtmlUtil.replace).replace(new RegExp("\\r?\\n", "g"), crlf);
};

var Response = {};

Response.error = function(result) {
    alert("系统错误，请稍后再试！");
};

Response.success = function(result, success){
    if(result == null){
        alert("系统错误，请稍后再试！");
        return false;
    }

    if(result.code == 0 || result.status == 200){
        if(success != null){
            success(result.value);
        }
        return true;
    }

    if(result.message != null){
        alert(result.message);
    }
    else{
        alert("系统错误，请稍后再试！");
    }
    return false;
};

var PageContext = {};

PageContext.getContextPath = function(){
    if(this.contextPath != null){
        return this.contextPath;
    }

    var contextPath = this.getAttribute("contextPath");

    if(contextPath == null || contextPath == "/"){
        contextPath = "";
    }
    return (this.contextPath = contextPath);
};

PageContext.getAttribute = function(name, defaultValue){
    var e = document.getElementById("pageContext");

    if(e == null) {
        return null;
    }
    var value = e.getAttribute(name);
    return (value != null ? value : defaultValue);
};

PageContext.getObject = function(name){
    var data = this.getAttribute(name);

    if(data == null){
        return null;
    }

    try {
        return window.eval("(" + data + ")");
    }
    catch(e) {
    }
    return null;
};

PageContext.getInt = function(name){
    var value = this.getAttribute(name);
    if(value == null){
        return 0;
    }
    value = parseInt(value);
    return (isNaN(value) ? 0 : value);
};

PageContext.getLong = function(name){
    var value = this.getAttribute(name);
    if(value == null){
        return 0;
    }
    value = parseInt(value);
    return (isNaN(value) ? 0 : value);
};

Util.addEventListener(window, "load", function() {
    Util.addEventListener(document.body, "keydown", function(e) {
        var event = (e || window.event);
        var keyCode = event.keyCode;

        if(keyCode == 116) {
            window.location.reload();
            return Util.stopPropagation(event);
        }
        return true;
    });
});