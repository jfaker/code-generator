var KeyCode = {
    F5: 116,
    ESC: 27,
    TAB: 9,
    SHIFT: 16,
    CTRL: 17,
    ENTER: 13,
    BLANKSPACE: 32,
    BACKSPACE: 8,
    LEFT: 37,
    UP: 38,
    RIGHT: 39,
    DOWN: 40,
    DELETE: 46,
    Q: 81
};

KeyCode.isLetter = function(keyCode){
    return (keyCode >= 65 && keyCode <= 90);
};

KeyCode.isLetter = function(keyCode){
    return (keyCode >= 48 && keyCode <= 57);
};

var EventUtil = {};

EventUtil.addEventListener = function(target, type, handler){
    if(target.attachEvent)
    {
        target.attachEvent("on" + type, handler);
    }
    else if(target.addEventListener)
    {
        target.addEventListener(type, handler, false);
    }
    else
    {
        target["on" + type] = handler;
    }
};

EventUtil.stop = function(event, returnValue){
    if(event != null)
    {
        if(event.stopPropagation)
        {
            event.stopPropagation();
        }
        else
        {
            event.cancelBubble = true;
        }

        if(event.preventDefault)
        {
            event.preventDefault();
        }

        if(returnValue == true)
        {
            event.cancel = false;
            event.returnValue = true;
        }
        else
        {
            event.cancel = true;
            event.returnValue = false;
        }
    }

    return false;
};

var StringUtil = {};
StringUtil.trim = function(s){return (s != null ? new String(s).replace(/(^\s*)|(\s*$)/g, "") : "");}
StringUtil.startsWith = function(source, search){
    if(source.length >= search.length)
    {
        return (source.substring(0, search.length) == search);
    }

    return false;
};

/**
 * @param source
 * @param search
 * @param replacement
 * @return String
 */
StringUtil.replace = function(source, search, replacement){
    if(source == null)
    {
        return "";
    }

    if(search == null)
    {
        return source;
    }

    var s = 0;
    var e = 0;
    var d = search.length;
    var content = source;
    var buffer = [];

    while(true)
    {
        while(true)
        {
            e = content.indexOf(search, s);

            if(e == -1)
            {
                buffer[buffer.length] = content.substring(s);
                break;
            }
            else
            {
                buffer[buffer.length] = content.substring(s, e);
                buffer[buffer.length] = replacement;
                s = e + d;
            }
        }

        content = buffer.join("");
        e = content.indexOf(search, 0);

        if(e > -1)
        {
            s = 0;
            buffer.length = 0;
        }
        else
        {
            break;
        }
    }

    return content;
};

var HtmlUtil = {};

HtmlUtil.replacement = {
    "<": "&lt;",
    ">": "&gt;",
    "\"": "&quot;",
    "\u00ae": "&reg;",
    "\u00a9": "&copy;"
};

HtmlUtil.replace = function(e){return com.skin.util.Html.replacement[e];};

HtmlUtil.encode = function(source, crlf){
    if(crlf == null){
        crlf = "";
    }

    return source.replace(new RegExp("[<>\"\\u00ae\\u00a9]", "g"), HtmlUtil.replace).replace(new RegExp("\\r?\\n", "g"), crlf);
};

var DispatchAction = {};

DispatchAction.listeners = [];

DispatchAction.dispatch = function(pattern, action){
    var listeners = this.listeners;
    var object = {"pattern": pattern, "action": action};
    listeners[listeners.length] = object;
    return object;
};

DispatchAction.call = function(hash, rule){
    var pattern = rule.pattern;
    var action = rule.action;

    if(pattern == "*")
    {
        action.apply(null, []);
        return true;
    }

    var regExp = new RegExp(pattern);
    var arr = regExp.exec(hash);

    if(arr != null)
    {
        var args = [];

        for(var i = 1; i < arr.length; i++)
        {
            args[args.length] = arr[i];
        }

        action.apply(null, args);
        return true;
    }

    return false;
};

DispatchAction.execute = function(hash){
    var hash = (hash != null ? hash : window.location.hash);

    if(hash == null || hash == undefined){
        hash = "";
    }

    hash = hash.replace(/(^\s*)|(\s*$)/g, "");

    if(hash.charAt(0) == "#"){
        hash = hash.substring(1);
    }

    var listeners = this.listeners;

    for(var i = 0, length = listeners.length; i < length; i++){
        DispatchAction.call(hash, listeners[i]);
    }
};

var FileType = {};

/**
 * @param path
 * @return String
 */
FileType.getType = function(path){
    return FileType.getExtension(path).toLowerCase();
};

/**
 * @param path
 * @return
 */
FileType.getName = function(path){
    if(path != null && path.length > 0)
    {
        var c = null;
        var i = path.length - 1;
        var buffer = [];

        for(; i > -1; i--)
        {
            c = path.charAt(i);

            if(c == "/" || c == "\\" || c == ":")
            {
                break;
            }
        }

        buffer[buffer.length] = path.substring(i + 1);
        return buffer.join("");
    }

    return "";
};

/**
 * @param path
 * @return String
 */
FileType.getExtension = function(path){
    if(path != null && path.length > 0)
    {
        var c = null;
        var i = path.length - 1;
        var buffer = [];

        for(; i > -1; i--)
        {
            c = path.charAt(i);

            if(c == ".")
            {
                break;
            }
            else if(c == "/" || c == "\\" || c == ":")
            {
                break;
            }
        }

        if(c == ".")
        {
            buffer[buffer.length] = path.substring(i + 1);
        }

        return buffer.join("");
    }

    return "";
};

var FileSort = {};

FileSort.marge = function(folderList, fileList, files){
    var index = 0;

    for(var i = 0; i < folderList.length; i++)
    {
        files[index] = folderList[i];
        index++;
    }

    for(var i = 0; i < fileList.length; i++)
    {
        files[index] = fileList[i];
        index++;
    }
};

FileSort.byName = function(files){
    var fileList = [];
    var folderList = [];

    for(var i = 0; i < files.length; i++)
    {
        if(files[i].isFile)
        {
            fileList[fileList.length] = files[i];
        }
        else
        {
            folderList[folderList.length] = files[i];
        }
    }

    var comparator = function(f1, f2){
        var s1 = f1.fileName.toLowerCase();
        var s2 = f2.fileName.toLowerCase();

        if(s1 == s2)
        {
            return 0;
        }

        if(s1 > s2)
        {
            return 1;
        }
        else
        {
            return -1;
        }
    };

    fileList.sort(comparator);
    FileSort.marge(folderList, fileList, files);
};

FileSort.byType = function(files){
    var fileList = [];
    var folderList = [];

    for(var i = 0; i < files.length; i++)
    {
        if(files[i].isFile)
        {
            fileList[fileList.length] = files[i];
        }
        else
        {
            folderList[folderList.length] = files[i];
        }
    }

    var comparator = function(f1, f2){
        var s1 = FileType.getType(f1.fileName).toLowerCase();
        var s2 = FileType.getType(f2.fileName).toLowerCase();

        if(s1 == s2)
        {
            return 0;
        }

        if(s1 > s2)
        {
            return 1;
        }
        else
        {
            return -1;
        }
    };

    fileList.sort(comparator);
    FileSort.marge(folderList, fileList, files);
};

FileSort.bySize = function(files){
    var fileList = [];
    var folderList = [];

    for(var i = 0; i < files.length; i++)
    {
        if(files[i].isFile)
        {
            fileList[fileList.length] = files[i];
        }
        else
        {
            folderList[folderList.length] = files[i];
        }
    }

    var comparator = function(f1, f2){
        return f1.fileSize - f2.fileSize;
    };

    fileList.sort(comparator);
    FileSort.marge(folderList, fileList, files);
};

FileSort.byLastModified = function(files){
    var fileList = [];
    var folderList = [];

    for(var i = 0; i < files.length; i++)
    {
        if(files[i].isFile)
        {
            fileList[fileList.length] = files[i];
        }
        else
        {
            folderList[folderList.length] = files[i];
        }
    }

    var comparator = function(f1, f2){
        return f1.lastModified - f2.lastModified;
    };

    fileList.sort(comparator);
    folderList.sort(comparator);
    FileSort.marge(folderList, fileList, files);
};

var Finder = {};

Finder.setCookie = function(cookie){
    var expires = "";
    if(cookie.value == null)
    {
        cookie.value = "";
        cookie.expires = -1;
    }

    if(cookie.expires != null)
    {
        var date = null;
        if(typeof(cookie.expires) == "number")
        {
            date = new Date();
            date.setTime(date.getTime() + cookie.expires);
        }
        else if(cookie.expires.toUTCString != null)
        {
            date = cookie.expires;
        }
        expires = "; expires=" + date.toUTCString();
    }

    var path = cookie.path ? "; path=" + (cookie.path) : "";
    var domain = cookie.domain ? "; domain=" + (cookie.domain) : "";
    var secure = cookie.secure ? "; secure" : "";
    document.cookie = [cookie.name, "=", (cookie.value != null ? encodeURIComponent(cookie.value) : ""), expires, path, domain, secure].join("");
};

Finder.getCookie = function(name){
    var value = null;
    if(document.cookie && document.cookie != "")
    {
        var cookies = document.cookie.split(';');
        for(var i = 0; i < cookies.length; i++)
        {
            var cookie = StringUtil.trim(cookies[i]);
            if(cookie.substring(0, name.length + 1) == (name + "="))
            {
                value = decodeURIComponent(cookie.substring(name.length + 1));
                break;
            }
        }
    }

    return value;
};

Finder.getContextPath = function(){
    if(this.contextPath == null || this.contextPath == undefined)
    {
        var contextPath = document.body.getAttribute("contextPath");

        if(contextPath == null || contextPath == "/")
        {
            contextPath = "";
        }

        this.contextPath = contextPath;
    }

    return this.contextPath;
};

Finder.format = function(date){
    if(date == null)
    {
        date = new Date();
    }

    if(typeof(date) == "number")
    {
        var temp = new Date();
        temp.setTime(date);
        date = temp;
    }

    var y = date.getFullYear();
    var M = date.getMonth() + 1;
    var d = date.getDate();
    var h = date.getHours();
    var m = date.getMinutes();
    var a = [];

    a[a.length] = y;
    a[a.length] = "-";

    if(M < 10)
    {
        a[a.length] = "0";
    }

    a[a.length] = M.toString();
    a[a.length] = "-";

    if(d < 10)
    {
        a[a.length] = "0";
    }

    a[a.length] = d.toString();

    a[a.length] = " ";
    
    if(h < 10)
    {
        a[a.length] = "0";
    }

    a[a.length] = h.toString();
    a[a.length] = ":";

    if(m < 10)
    {
        a[a.length] = "0";
    }

    a[a.length] = m.toString();
    return a.join("");
};

Finder.getFile = function(element){
    var parent = this.getParent(element);
    var isFile = parent.getAttribute("isFile");
    var fileIcon = parent.getAttribute("fileIcon");
    var fileName = parent.getAttribute("fileName");
    var lastModified = parent.getAttribute("lastModified");
    var file = {"fileName": fileName};

    if(isFile != "false")
    {
        file.isFile = true;
        file.fileIcon = fileIcon;
        file.fileSize = parseInt(parent.getAttribute("fileSize"));
        file.lastModified = parseInt(lastModified);
    }
    else
    {
        file.lastModified = parseInt(lastModified);
    }

    return file;
};

Finder.open = function(file, options){
    var workspace = document.body.getAttribute("workspace");
    var path = document.body.getAttribute("path") + "/" + file.fileName;

    var params = [];
    params[params.length] = "workspace=" + encodeURIComponent(workspace);
    params[params.length] = "path=" + encodeURIComponent(path);

    if(options.download != null)
    {
        params[params.length] = "download=true";
    }

    if(options.target != null)
    {
        window.open(this.getContextPath() + "/finder/display.html?" + params.join("&"), options.target);
    }
    else
    {
         window.location.href = this.getContextPath() + "/finder/display.html?" + params.join("&");
    }

    return true;
};

Finder.tail = function(file, options){
    var workspace = document.body.getAttribute("workspace");
    var path = document.body.getAttribute("path") + "/" + file.fileName;

    var params = [];
    params[params.length] = "workspace=" + encodeURIComponent(workspace);
    params[params.length] = "path=" + encodeURIComponent(path);

    if(options.download != null)
    {
        params[params.length] = "download=true";
    }

    if(options.target != null)
    {
        window.open(this.getContextPath() + "/finder/tail.html?" + params.join("&"), options.target);
    }
    else
    {
         window.location.href = this.getContextPath() + "/finder/tail.html?" + params.join("&");
    }

    return true;
};

Finder.download = function(file, options){
    return this.open(file, {"download": "true"});
};

Finder.remove = function(files, options){
    if(files.length < 1)
    {
        return;
    }

    var workspace = document.body.getAttribute("workspace");
    var path = document.body.getAttribute("path");

    if(options.force != true)
    {
        var message = null;

        if(files.length == 1)
        {
            message = "确实要删除\"" + (path + "/" + files[0].fileName) + "\"吗？";
        }
        else
        {
            message = "确实要删除这 " + files.length + " 项吗？";
        }

        if(window.confirm(message) == false)
        {
            return;
        }
    }

    var params = {};
    params.workspace = workspace;
    params.path = [];

    for(var i = 0; i < files.length; i++)
    {
        params.path[params.path.length] = path + "/" + files[i].fileName;
    }

    Ajax.request({
        "method": "post",
        "url": this.getContextPath() + "/finder/delete.html",
        "data": params,
        "success": function(response){
            var json = null;

            try
            {
                json = window.eval("(" + response.responseText + ")");
            }
            catch(e)
            {
            }

            var callback = options.callback;

            if(callback != null)
            {
                callback((json || {}));
            }
        }
    });
};

Finder.rename = function(file, options){
    var params = [];
    var workspace = document.body.getAttribute("workspace");
    var path = document.body.getAttribute("path") + "/" + file.fileName;
    params[params.length] = "workspace=" + encodeURIComponent(workspace);
    params[params.length] = "path=" + encodeURIComponent(path);
    params[params.length] = "newName=" + encodeURIComponent(file.newName);

    Ajax.request({
        "method": "get",
        "url": this.getContextPath() + "/finder/rename.html?" + params.join("&"),
        "success": function(response){
            var json = null;

            try
            {
                json = window.eval("(" + response.responseText + ")");
            }
            catch(e)
            {
            }

            var callback = options.callback;

            if(callback != null)
            {
                callback((json || {}));
            }
        }
    });
};

Finder.back = function(){
    var name = document.body.getAttribute("workspace");
    var parent = document.body.getAttribute("parent");

    if(parent != null && parent.length > 0)
    {
        window.location.href = this.getContextPath() + "/finder/display.html?workspace=" + encodeURIComponent(name) + "&path=" + encodeURIComponent(parent);
    }
};

Finder.forward = function(workspace, path){
    if(path != null)
    {
        window.location.href = this.getContextPath() + "/finder/display.html?workspace=" + encodeURIComponent(workspace) + "&path=" + encodeURIComponent(path);
    }
    else
    {
        window.location.href = this.getContextPath() + "/finder/display.html?workspace=" + encodeURIComponent(workspace);
    }
};

Finder.refresh = function(event){
    var e = (event || window.event);

    if(e.ctrlKey == true)
    {
        window.open(window.location.href, "_blank");
    }
    else
    {
        window.location.reload();
    }
};

Finder.getFirst = function(handler){
    var parent = document.getElementById("file-list");

    if(parent != null)
    {
        var list = parent.childNodes;

        for(var i = 0; i < list.length; i++)
        {
            var e = list[i];

            if(e.nodeType == 1 && e.className != null)
            {
                var className = e.className;

                if(className.indexOf("item") > -1)
                {
                     return e;
                }
            }
        }
    }

    return null;
};

Finder.getLast = function(handler){
    var parent = document.getElementById("file-list");

    if(parent != null)
    {
        var list = parent.childNodes;

        for(var i = list.length - 1; i > -1; i--)
        {
            var e = list[i];

            if(e.nodeType == 1 && e.className != null)
            {
                var className = e.className;

                if(className.indexOf("item") > -1)
                {
                     return e;
                }
            }
        }
    }

    return null;
};

Finder.getSelected = function(){
    var list = [];

    Finder.each(function(e){
        if(e.className.indexOf("selected") > -1)
        {
            list[list.length] = e;
        }
    });

    return list;
};

Finder.getNext = function(e){
    var next = e;

    while((next = next.nextSibling) != null)
    {
        if(next.nodeType == 1)
        {
            return next;
        }
    }

    return null;
};

Finder.getPrevious = function(e){
    var previous = e;

    while((previous = previous.previousSibling) != null)
    {
        if(previous.nodeType == 1)
        {
            return previous;
        }
    }

    return null;
};

Finder.each = function(handler){
    var parent = document.getElementById("file-list");

    if(parent != null)
    {
        var list = parent.childNodes;

        for(var i = 0; i < list.length; i++)
        {
            var e = list[i];

            if(e.nodeType == 1 && e.className != null)
            {
                if(e.className.indexOf("item") > -1)
                {
                     handler(e);
                }
            }
        }
    }
};

Finder.setVisible = function(element, target, center){
    var height = target.clientHeight;
    var scrollTop = target.scrollTop;
    var offsetTop = element.offsetTop;
    var clientHeight = element.clientHeight;

    if(target == document.body)
    {
        height = document.documentElement.clientHeight;
    }

    if(scrollTop > offsetTop)
    {
        var top = offsetTop;

        if(center == true)
        {
            top = top - Math.floor(top - (height / 2));
        }

        if(target == document.body)
        {
            document.body.scrollTop = top;
            document.documentElement.scrollTop = top;
        }
        else
        {
            target.scrollTop = top;
        }
    }

    if(offsetTop > (height + scrollTop - clientHeight))
    {
        var top = offsetTop - height + clientHeight;

        if(center == true)
        {
            top = top - Math.floor(top - (height / 2));
        }

        if(target == document.body)
        {
            document.body.scrollTop = top;
            document.documentElement.scrollTop = top;
        }
        else
        {
            target.scrollTop = top;
        }
    }
};

Finder.scroll = function(offset, multiple){
    var prev = null;
    var next = null;
    var active = this.active;

    if(active == null)
    {
        active = this.active = this.getFirst();

        if(active != null)
        {
            active.className = "item active selected";
        }

        return;
    }

    if(multiple == true)
    {
        if(offset > 0)
        {
            next = this.getNext(active);

            if(next != null)
            {
                if(next.className.indexOf("selected") > -1)
                {
                    active.className = "item";
                }

                next.className = "item active selected";
                this.active = next;
            }
        }
        else
        {
            prev = this.getPrevious(active);

            if(prev != null)
            {
                if(prev.className.indexOf("selected") > -1)
                {
                    active.className = "item";
                }

                prev.className = "item active selected";
                this.active = prev;
            }
        }

        if(active != this.active)
        {
            if(active.className.indexOf("selected") > -1)
            {
                active.className = "item selected";
            }
            else
            {
                active.className = "item";
            }
        }

        return;
    }

    var list = Finder.getSelected();

    if(list.length > 0)
    {
        for(var i = 0; i < list.length; i++)
        {
            list[i].className = "item";
        }
    }

    if(offset > 0)
    {
        next = this.getNext(active);

        if(next == null)
        {
            next = this.getFirst();
        }
    }
    else
    {
        next = this.getPrevious(active);

        if(next == null)
        {
            next = this.getLast();
        }
    }

    if(next != null)
    {
        next.className = "item active selected";
        this.active = next;
        this.setVisible(next, document.body);
    }
    else
    {
        this.active = null;
    }
};

Finder.getFileList = function(){
    if(this.fileList == null)
    {
        var fileList = [];

        Finder.each(function(e){
            var file = Finder.getFile(e);

            if(file != null)
            {
                fileList[fileList.length] = file;
            }
        });

        Finder.fileList = fileList;
    }

    return Finder.fileList;
};

Finder.getTooltip = function(work, path, file){
    if(file.isFile)
    {
        return HtmlUtil.encode(work + path + "/" + file.fileName) + "&#10;类型: " + FileType.getType(file.fileName) + "文件&#10;修改日期: " + Finder.format(file.lastModified) + "&#10;大小: " + file.fileSize + " 字节";
    }
    else
    {
        return HtmlUtil.encode(work + path + "/" + file.fileName);
    }
};

Finder.list = function(fileList, force){
    var container = document.getElementById("file-list");

    if(container == null)
    {
        return;
    }

    if(force != true)
    {
        var mode = container.getAttribute("view-mode");

        if(mode == null || mode == undefined)
        {
            container.parentNode.style.display = "block";
            return;
        }

        if(mode == "detail")
        {
            return;
        }
    }

    var index = 1;
    var buffer = [];
    var work = document.body.getAttribute("work");
    var path = document.body.getAttribute("path");

    for(var i = 0; i < fileList.length; i++)
    {
        var file = fileList[i];

        if(file.isFile != true)
        {
            buffer[buffer.length] = "<li class=\"item\" isFile=\"false\" fileName=\"" + HtmlUtil.encode(file.fileName) + "\" lastModified=\"" + file.lastModified + "\">";
            buffer[buffer.length] = "   <span class=\"icon\"><img src=\"" + this.getContextPath() + "/resource/finder/images/folder.gif\"/></span>";
            buffer[buffer.length] = "   <span class=\"fileName\"><a class=\"file\" href=\"javascript:void(0)\" title=\"" + Finder.getTooltip(work, path, file) + "\">" + HtmlUtil.encode(file.fileName) + "</a></span>";
            buffer[buffer.length] = "   <span class=\"fileSize\">&nbsp;</span>";
            buffer[buffer.length] = "   <span class=\"fileType\">文件夹</span>";
            buffer[buffer.length] = "   <span class=\"lastModified\">" + this.format(file.lastModified) + "</span>";
            buffer[buffer.length] = "   <span class=\"w200\">";
            buffer[buffer.length] = "       <a action=\"finder-open\" href=\"javascript:void(0)\" target=\"_blank\">open</a>";
            buffer[buffer.length] = "       <a action=\"finder-remove\" href=\"javascript:void(0)\">delete</a>";
            buffer[buffer.length] = "   </span>";
            buffer[buffer.length] = "</li>";
        }
        else
        {
            buffer[buffer.length] = "<li class=\"item\" fileIcon=\"" + file.fileIcon + "\" fileName=\"" + HtmlUtil.encode(file.fileName) + "\" fileSize=\"" + file.fileSize + "\" lastModified=\"" + file.lastModified + "\">";
            buffer[buffer.length] = "   <span class=\"icon\"><img src=\"" + this.getContextPath() + "/resource/finder/type/" + file.fileIcon + "\"/></span>";
            buffer[buffer.length] = "   <span class=\"fileName\"><a class=\"file\" href=\"javascript:void(0)\" title=\"" + Finder.getTooltip(work, path, file) + "\">" + HtmlUtil.encode(file.fileName) + "</a></span>";
            buffer[buffer.length] = "   <span class=\"fileSize\">" + Math.floor(file.fileSize / 1024) + "KB</span>";
            buffer[buffer.length] = "   <span class=\"fileType\">" + FileType.getType(file.fileName) + "文件</span>";
            buffer[buffer.length] = "   <span class=\"lastModified\">" + Finder.format(file.lastModified) + "</span>";
            buffer[buffer.length] = "   <span class=\"w200\">";
            buffer[buffer.length] = "       <a action=\"finder-tail\" href=\"javascript:void(0)\" target=\"_blank\">tail</a>";
            buffer[buffer.length] = "       <a action=\"finder-open\" href=\"javascript:void(0)\" target=\"_blank\">open</a>";
            buffer[buffer.length] = "       <a action=\"finder-download\" href=\"javascript:void(0)\">download</a>";
            buffer[buffer.length] = "       <a action=\"finder-remove\" href=\"javascript:void(0)\">delete</a>";
            buffer[buffer.length] = "   </span>";
            buffer[buffer.length] = "</li>";
        }
    }

    container.innerHTML = buffer.join("");
    container.parentNode.className = "detail-view";
    container.parentNode.style.display = "block";
    container.setAttribute("view-mode", "detail");
    document.getElementById("head-view").style.display = "block";
    Finder.setCookie({"name": "view_mode", "value": "detail", "path": "/", "expires": 365 * 24 * 60 * 60 * 1000});
};

Finder.detail = function(){
    this.list(Finder.getFileList());
};

Finder.resize = function(image){
    if(image.readyState != "complete")
    {
    }

    var maxWidth = 98;
    var maxHeight = 98;
    var heightWidth = (image.offsetHeight / image.offsetWidth);
    var widthHeight = (image.offsetWidth / image.offsetHeight);

    if(image.offsetHeight > 1)
    {
    }

    if(image.offsetWidth > maxWidth){
        image.style.width = maxWidth + "px";
        image.style.height = parseInt(maxWidth * heightWidth) + "px";
    }

    if(image.offsetHeight > maxHeight){
        image.style.height = maxHeight + "px";
        image.style.width = parseInt(maxHeight * widthHeight) + "px";
    }
};

Finder.outline = function(){
    var container = document.getElementById("file-list");

    if(container == null)
    {
        return;
    }

    if(container.getAttribute("view-mode") == "outline")
    {
        return;
    }

    var fileList = Finder.getFileList();
    var workspace = document.body.getAttribute("workspace");
    var work = document.body.getAttribute("work");
    var path = document.body.getAttribute("path");

    var map = {
        "ico":  "ico",
        "jpg":  "jpg",
        "jpeg": "jpeg",
        "gif":  "gif",
        "bmp":  "bmp",
        "png":  "png"
    };

    var b = [];

    for(var i = 0; i < fileList.length; i++)
    {
        var file = fileList[i];

        if(file.isFile)
        {
            b[b.length] = "<li class=\"item\" onclick=\"Finder.click(event);\" isFile=\"true\" fileName=\"" + HtmlUtil.encode(file.fileName) + "\" fileIcon=\"" + file.fileIcon + "\" fileSize=\"" + file.fileSize + "\" lastModified=\"" + file.lastModified + "\">";
            b[b.length] = "<div class=\"box\">";

            var type = FileType.getType(file.fileName).toLowerCase();

            if(map[type] != null)
            {
                b[b.length] = "<img onload=\"Finder.resize(this)\" src=\"" + this.getContextPath() + "/finder/display.html?workspace=" + encodeURIComponent(workspace) + "&path=" + encodeURIComponent(path + "/" + file.fileName) + "\"/>";
            }
            else
            {
                b[b.length] = "<img class=\"icon\" src=\"" + this.getContextPath() + "/resource/finder/type/" + file.fileIcon + "\"/>";
            }
        }
        else
        {
            b[b.length] = "<li class=\"item\" isFile=\"false\" fileName=\"" + HtmlUtil.encode(file.fileName) + "\" lastModified=\"" + file.lastModified + "\">";
            b[b.length] = "<div class=\"box\">";
            b[b.length] = "<img class=\"icon\" src=\"" + this.getContextPath() + "/resource/finder/images/folder.gif\"/>";
        }

        b[b.length] = "</div>";
        b[b.length] = "<div class=\"filename\" title=\"" + Finder.getTooltip(work, path, file) + "\">";
        b[b.length] = "    <a class=\"file\" href=\"javascript:void(0)\" onclick=\"Finder.click(event);\">" + file.fileName + "</a>";
        b[b.length] = "    <a action=\"finder-open\" href=\"javascript:void(0)\" target=\"_blank\" style=\"display: none;\">open</a>";
        b[b.length] = "    <a action=\"finder-download\" href=\"javascript:void(0)\" style=\"display: none;\">download</a>";
        b[b.length] = "    <a action=\"finder-remove\" href=\"javascript:void(0)\" style=\"display: none;\">delete</a>";
        b[b.length] = "</div>";
        b[b.length] = "</li>";
    }

    container.innerHTML = b.join("");
    container.parentNode.className = "outline-view";
    container.setAttribute("view-mode", "outline");
    document.getElementById("head-view").style.display = "none";
    document.getElementById("file-view").style.display = "block";
    Finder.setCookie({"name": "view_mode", "value": "outline", "path": "/", "expires": 365 * 24 * 60 * 60 * 1000});
};

Finder.getParent = function(src){
    var parent = src;

    while(parent != null && (parent.className == null || parent.className.indexOf("item") < 0))
    {
        parent = parent.parentNode;
    };

    return parent;
};

Finder.click = function(event){
    var e = (event || window.event);
    var src = (e.srcElement || e.target);

    if(src == null)
    {
        return;
    }

    Finder.select(src, (e.ctrlKey == true), (e.altKey == true));

    if(src.nodeName.toLowerCase() != "a")
    {
        return;
    }

    var options = {};
    var file = Finder.getFile(src);
    var parent = Finder.getParent(src);
    var action = src.getAttribute("action");
    var target = src.getAttribute("target");

    if(event.ctrlKey == true)
    {
        options.target = "_blank";
    }

    if(target != null)
    {
        options.target = target;
    }

    if(action == "finder-download")
    {
        Finder.download(file, options);
    }
    else if(action == "finder-remove")
    {
        options.callback = function(json){
            if(json == null || json.code != 0)
            {
                alert("文件不存在或者已经被删除！");
                return;
            }

            if(json.count == 1)
            {
                Finder.scroll(1);
                parent.parentNode.removeChild(parent);
                Finder.fileList = null;
            }
            else
            {
                alert("删除文件失败！请稍后再试！");
            }
        };

        Finder.remove([file], options);
    }
    else if(action == "finder-tail")
    {
        Finder.tail(file, options);
    }
    else
    {
        Finder.open(file, options);
    }

    return true;
};

Finder.select = function(src, multiple, shift){
    if(multiple != true && shift != true)
    {
        this.each(function(e){
            e.className = "item";
        });
    }

    var parent = Finder.getParent(src);

    if(parent != null)
    {
        if(this.active == null)
        {
            this.active = this.getFirst();
        }

        if(shift == true)
        {
            var j = -1;
            var k = -1;
            var list = [];
            var active = this.active;

            this.each(function(e){
                list[list.length] = e;

                if(e == parent)
                {
                    j = list.length - 1;
                }

                if(e == active)
                {
                    k = list.length - 1;
                }
            });


            if(j > -1 && k > -1)
            {
                var min = Math.min(j, k);
                var max = Math.max(j, k);

                for(var i = 0; i < list.length; i++)
                {
                    if(i < min || i > max)
                    {
                        list[i].className = "item";
                    }
                    else
                    {
                        list[i].className = "item selected";
                    }
                }
            }
        }
        else
        {
            this.active = parent;
        }

        parent.className = "item selected";

        if(this.active != null)
        {
            if(this.active.className.indexOf("selected") > -1)
            {
                this.active.className = "item active selected";
            }
            else
            {
                this.active.className = "item";
            }
        }
    }
};

Finder.keydown = function(event){
    var e = (event || window.event);
    var src = (e.srcElement || e.target);
    var keyCode = (event.keyCode || event.which);
    var path = StringUtil.trim(src.value);

    switch(keyCode)
    {
        case KeyCode.BACKSPACE:
        {
            if(e.ctrlKey == true)
            {
                var i = path.lastIndexOf("/", path.length - 2);

                if(i > -1)
                {
                    src.value = path.substring(0, i + 1);
                }
                else
                {
                    src.value = "/";
                }

                return false;
            }
            else
            {
                return true;
            }
        }
        case KeyCode.UP:
        {
            FinderSuggestDialog.scroll(-1);
            return false;
        }
        case KeyCode.DOWN:
        {
            FinderSuggestDialog.scroll(+1);
            return false;
        }
    }

    return true;
};

Finder.keyup = function(event){
    var e = (event || window.event);
    var src = (e.srcElement || e.target);
    var keyCode = (event.keyCode || event.which);
    var path = src.value;

    switch(keyCode)
    {
        case KeyCode.ESC:
        {
            Finder.back();
            return false;
        }
        case KeyCode.ENTER:
        {
            Finder.forward(document.body.getAttribute("workspace"), path);
            return false;
        }
        case KeyCode.UP:
        {
            EventUtil.stop(e);
            return false;
        }
        case KeyCode.DOWN:
        {
            EventUtil.stop(e);
            return false;
        }
    }

    Finder.suggest(document.body.getAttribute("workspace"), path);
    return false;
};

Finder.contextmenu = function(event){
    var e = (event || window.event);
    var src = (e.srcElement || e.target);
    var parent = this.getParent(src);

    if(parent != null)
    {
        if(src.nodeName.toLowerCase() == "a")
        {
            Finder.select(src.parentNode);
        }
        else
        {
            Finder.select(src);
        }

        var panel = document.getElementById("finder-contextmenu");
        panel.style.top = (document.body.scrollTop + event.clientY) + "px";
        panel.style.left = (document.body.scrollLeft + event.clientX) + "px";
        panel.style.display = "block";
        return false;
    }
    else
    {
        return true;
    }
};

Finder.getSuggest = function(workspace, path){
    if(Finder.cache == null)
    {
        Finder.cache = {};
    }

    var i = path.lastIndexOf("/");
    var key = path.substring(0, i + 1);
    var prefix = path.substring(i + 1).toLowerCase();
    var json = Finder.cache[key];

    if(json != null)
    {
        var list = [];

        for(var i = 0; i < json.length; i++)
        {
            if(StringUtil.startsWith(json[i].toLowerCase(), prefix))
            {
                list[list.length] = json[i];
            }
        }

        return list;
    }

    return null;
};

Finder.suggest = function(workspace, path){
    path = StringUtil.trim(path);
    path = StringUtil.replace(path, "\\", "/");
    path = StringUtil.replace(path, "//", "/");

    if(path.length < 1)
    {
        path = "/";
    }

    if(path.charAt(0) != "/")
    {
        path = "/" + path;
    }

    var json = Finder.getSuggest(workspace, path);

    if(json != null)
    {
        FinderSuggestDialog.open(json);
        return;
    }

    Ajax.request({
        "method": "get",
        "url": this.getContextPath() + "/finder/suggest.html?workspace=" + encodeURIComponent(workspace) + "&path=" + encodeURIComponent(path),
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
                Finder.cache[path] = json;
                FinderSuggestDialog.open(json);
            }
        }
    });
};

var FinderSuggestDialog = {"id": "finder-suggest", "handler": null};

/**
 * @Override
 */
FinderSuggestDialog.handler = function(value, action){
    var e = document.getElementById("address");

    if(e != null)
    {
        var path = e.value;
        path = StringUtil.trim(path);
        path = StringUtil.replace(path, "\\", "/");
        path = StringUtil.replace(path, "//", "/");

        if(path.length < 1)
        {
            e.value = "/" + value;
        }
        else
        {
            var i = path.lastIndexOf("/");

            if(i > -1)
            {
                e.value = path.substring(0, i + 1) + value;
            }
            else
            {
                e.value = "/" + value;
            }
        }

        if(action == true)
        {
            Finder.forward(document.body.getAttribute("workspace"), e.value);
        }
    }
};

FinderSuggestDialog.click = function(event){
    var e = (event || window.event);
    var src = (e.srcElement || e.target);
    var list = this.list();
    var length = list.length;

    for(var i = 0; i < length; i++)
    {
        if(list[i].className == "selected")
        {
            list[i].className = "";
        }
    }

    src.parentNode.className = "selected";

    if(this.handler != null)
    {
        this.handler.apply(null, [src.parentNode.getAttribute("option-value"), true]);
    }
};

FinderSuggestDialog.open = function(json){
    var e = document.getElementById(this.id);

    if(e != null)
    {
        var b = [];
        b[b.length] = "<ul>";

        if(json == null || json.length == null || json.length < 1)
        {
            b[b.length] = "<li option-value=\"\" index=\"0\" class=\"empty\"><a href=\"javascript:void(0)\" style=\"color: #c0c0c0;\">无结果</a></li>";
        }
        else
        {
            for(var i = 0; i < json.length; i++)
            {
                b[b.length] = "<li option-value=\"" + HtmlUtil.encode(json[i]) + "\" index=\"0\"><a href=\"javascript:void(0)\" onclick=\"FinderSuggestDialog.click(event)\">" + HtmlUtil.encode(json[i]) + "</a></li>";
            }
        }

        b[b.length] = "</ul>";
        e.innerHTML = b.join("");

        if(json.length < 1)
        {
            e.style.height = "24px";
        }
        else if(json.length < 20)
        {
            e.style.height = (json.length * 20 + 4) + "px";
        }
        else
        {
            e.style.height = "480px";
        }

        e.setAttribute("status", "0");
        e.style.display = "block";
    }
};

FinderSuggestDialog.close = function(){
    var e = document.getElementById(this.id);

    if(e != null && e.getAttribute("status") != "1")
    {
        e.style.display = "none";
    }
};

FinderSuggestDialog.list = function(){
    var list = [];
    var e = document.getElementById(this.id);

    if(e != null)
    {
        var elements = e.getElementsByTagName("ul");

        if(elements != null && elements.length > 0)
        {
            var temp = elements[0].childNodes;

            for(var i = 0, length = temp.length; i < length; i++)
            {
                if(temp[i].nodeType == 1 && temp[i].nodeName.toLowerCase() == "li")
                {
                    if(temp[i].className != "empty")
                    {
                        list[list.length] = temp[i];
                    }
                }
            }
        }
    }

    return list;
};

FinderSuggestDialog.scroll = function(offset){
    var selected = -1;
    var list = this.list();
    var length = list.length;

    if(length < 1)
    {
        return;
    }

    for(var i = 0; i < length; i++)
    {
        if(list[i].className == "selected")
        {
            selected = i;
            list[i].className = "";
        }
    }

    selected = selected + offset;

    if(selected < 0)
    {
        selected = list.length - 1;
    }

    if(selected >= list.length)
    {
        selected = 0;
    }

    if(selected < length)
    {
        var e = list[selected];
        var p = document.getElementById(this.id);
        var value = e.getAttribute("option-value");
        e.className = "selected";
        p.scrollTop = e.offsetTop;

        if(value != null && value.length > 0)
        {
            if(this.handler != null)
            {
                this.handler(value);
            }
        }
    }
};

/* event */
jQuery(function(){
    /*
    jQuery(document).bind("contextmenu", function(event){
        return Finder.contextmenu(event);
    });
    */

    jQuery(document).keydown(function(event){
        var e = (event || window.event);
        var src = (e.srcElement || e.target);
        var keyCode = (e.keyCode || e.which);
        var nodeName = src.nodeName.toLowerCase();

        if(nodeName == "input" || nodeName == "textarea")
        {
            return true;
        }

        switch(keyCode){
            case KeyCode.F5:
            {
                window.location.reload();
                return false;
            }
            case KeyCode.Q:
            case KeyCode.ESC:
            {
                if(window.parent == window)
                {
                    window.close();
                }

                return false;
            }
            case KeyCode.BACKSPACE:
            {
                Finder.back();
                return false;
            }
            case KeyCode.LEFT:
            case KeyCode.UP:
            {
                Finder.scroll(-1, (event.shiftKey == true));
                return false;
            }
            case KeyCode.RIGHT:
            case KeyCode.DOWN:
            {
                Finder.scroll(+1, (event.shiftKey == true));
                return false;
            }
            case KeyCode.ENTER:
            {
                var list = Finder.getSelected();

                if(list.length > 0)
                {
                    if(event.ctrlKey == true)
                    {
                        jQuery(list[0]).find("a[action=finder-open]").click();
                    }
                    else
                    {
                        jQuery(list[0]).find("a.file").click();
                    }
                }

                return false;
            }
            case KeyCode.DELETE:
            {
                var files = [];
                var list = Finder.getSelected();

                for(var i = 0; i < list.length; i++)
                {
                    files[files.length] = Finder.getFile(list[i]);
                }

                Finder.remove(files, {"callback": function(json){
                    if(json == null || json.code != 0)
                    {
                        alert("文件不存在或者已经被删除！");
                        return;
                    }

                    if(json.count == files.length)
                    {
                        for(var i = 0; i < list.length; i++)
                        {
                            list[i].parentNode.removeChild(list[i]);
                        }

                        Finder.scroll(1);
                        Finder.fileList = null;
                    }
                    else
                    {
                        alert("删除文件失败！请稍后再试！");
                        window.location.reload();
                    }
                }});

                return false;
            }
        };

        return true;
    });

    jQuery("#uiTypeOption").change(function(){
        var workspace = jQuery("body").attr("workspace");
        var path = StringUtil.trim(jQuery("body").attr("path"));
        var type = StringUtil.trim(jQuery("#uiTypeOption").val());
        var theme = StringUtil.trim(jQuery("#uiThemeOption").val());
        var encoding = StringUtil.trim(jQuery("#uiEncodingOption").val());
        window.location.href = Finder.getContextPath() + "/finder/display.html?workspace=" + encodeURIComponent(workspace) + "&path=" + encodeURIComponent(path) + "&type=" + encodeURIComponent(type) + "&theme=" + encodeURIComponent(theme) + "&encoding=" + encodeURIComponent(encoding);
    });

    jQuery("#uiThemeOption").change(function(){
        jQuery("#uiTypeOption").change();
    });

    jQuery("#uiEncodingOption").change(function(){
        jQuery("#uiTypeOption").change();
    });
});

jQuery(function(){
    jQuery("ul.file-list li a.file").each(function(){
        var work = document.body.getAttribute("work");
        var path = document.body.getAttribute("path");
        var file = Finder.getFile(this);
        var tooltip = Finder.getTooltip(work, path, file);
        jQuery(this).attr("title", tooltip.replace(/&#10;/g, "\r\n"));
    });

    jQuery("ul.file-list").click(function(event){
        Finder.click(event);
        return false;
    });

    jQuery("a.button span.back").click(function(event){
        Finder.back();
    });

    jQuery("a.button span.refresh").click(function(event){
        if(event.ctrlKey == true)
        {
            window.open(window.location.href, "_blank");
        }
        else
        {
            window.location.reload();
        }
    });

    jQuery("a.button span.view").click(function(event){
        jQuery("#view-options").show();
        return false;
    });

    jQuery("a.button span.help").click(function(event){
        window.open("/help/index.html", "_blank");
        return false;
    });

    jQuery("#address").keydown(function(event){
        return Finder.keydown(event);
    });

    jQuery("#address").keyup(function(event){
        return Finder.keyup(event);
    });

    jQuery("#address").click(function(event){
        return Finder.keyup(event);
    });

    jQuery(document).click(function(event){
        FinderSuggestDialog.close();
        jQuery("#view-options").hide();
        jQuery("#finder-contextmenu").hide();
        return true;
    });

    jQuery("#view-options li").click(function(){
        jQuery("#view-options li").attr("class", "");
        jQuery(this).attr("class", "selected");

        if(jQuery(this).attr("option-value") == "detail")
        {
            Finder.detail();
        }
        else
        {
            Finder.outline();
        }
    });
});

/* 排序 */
jQuery(function(){
    jQuery("#file-view span.orderable").click(function(){
        var src = jQuery(this).find("em.order");

        if(src.size() < 1)
        {
            return;
        }

        var asc = "asc";
        var orderBy = jQuery(this).attr("orderBy");
        var className = src.attr("class");

        jQuery("#file-view span.orderable em.order").attr("class", "order");

        if(className.indexOf("asc") > -1)
        {
            asc = "desc";
            src.attr("class", "order desc");
        }
        else
        {
            src.attr("class", "order asc");
        }

        var fileList = Finder.getFileList();

        if(orderBy == "file-name")
        {
            FileSort.byName(fileList);
        }
        else if(orderBy == "file-type")
        {
            FileSort.byType(fileList);
        }
        else if(orderBy == "file-size")
        {
            FileSort.bySize(fileList);
        }
        else if(orderBy == "last-modified")
        {
            FileSort.byLastModified(fileList);
        }

        if(asc == "desc")
        {
            fileList.reverse();
        }

        Finder.list(fileList, true);
        window.location.href = "#orderby_" + orderBy + "_" + asc;
    });
});

jQuery(function(){
    var viewMode = Finder.getCookie("view_mode");

    if(viewMode == "outline")
    {
        jQuery("#view-options li[option-value=outline]").click();
    }
    else
    {
        jQuery("#file-view").show();

        DispatchAction.dispatch("orderby_([^_]+)_([^_]+)", function(field, asc){
            if(field == "file-name" && asc == "asc")
            {
                return;
            }

            var src = jQuery("#fileList tr.head td.orderable[orderBy=" + field + "]");

            if(asc == "asc")
            {
                src.find("span.order").attr("class", "order desc");
            }
            else
            {
                src.find("span.order").attr("class", "order asc");
            }

            src.click();
        });
    }

    DispatchAction.execute(window.location.hash);

    var leftFrame = window.top.leftFrame;

    if(leftFrame != null && leftFrame.expand != null)
    {
        leftFrame.expand(document.body.getAttribute("path"));
    }
});
