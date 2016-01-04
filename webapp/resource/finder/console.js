/**
 * Skin JavaScript Library v1.0.0
 * Copyright (c) 2010 xuesong.net
 * 
 * mailto: xuesong.net@163.com
 * Date: 2010-04-28 10:24:21
 * Revision: 1012
 */
var logger = {};
var Console = {};

Console.count = 0;
Console.scroll = true;
Console.setting = {};
Console.setting.maxCacheSize = 9000;

Console.getDateTime = function(date){
    if(date == null)
    {
        date = new Date();
    }

    if(typeof(date) == "number")
    {
        var d = new Date();
        d.setTime(date);
        date = d;
    }

    var y = date.getFullYear();
    var M = date.getMonth() + 1;
    var d = date.getDate();
    var h = date.getHours();
    var m = date.getMinutes();
    var s = date.getSeconds();
    var S = date.getTime() % 1000;

    var a = [];

    a[a.length] = y.toString();
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
    a[a.length] = ":";

    if(s < 10)
    {
        a[a.length] = "0";
    }

    a[a.length] = s.toString();
    a[a.length] = " ";

    if(S < 100)
    {
        a[a.length] = "0";
    }

    if(S < 10)
    {
        a[a.length] = "0";
    }

    a[a.length] = S.toString();

    return a.join("");
};

Console.select = function(){
    var e = Console.container;

    if(e == null)
    {
        e = document.getElementById("console");
    }

    if(e.nodeName.toLowerCase() == "textarea")
    {
        e.select();
    }

    if(document.all)
    {
        /* for IE */
        var range = document.body.createTextRange();
        range.moveToElementText(e);
        range.select();
    }
    else
    {
        var selection = window.getSelection();

        if(document.createRange)
        {
            /* for FF, Opera */
            var range = document.createRange();
            range.selectNodeContents(e);
            selection.removeAllRanges();
            selection.addRange(range);
            window.focus();
        }
        else
        {
            /* for Safari */
            selection.setBaseAndExtent(e, 0, e, 1);
        }
    }
};

Console.println = function(s, b){
    var e = Console.container;

    if(e == null)
    {
        e = document.getElementById("console");
        var target = e.getAttribute("scroll");

        if(target != null)
        {
            if(target == "window" || "document")
            {
                Console.target = document.documentElement;
            }
            else
            {
                Console.target = document.getElementById(target);
            }
        }
        else
        {
            Console.target = e;
        }

        Console.container = e;
    }

    if(e != null)
    {
        var nodeName = e.nodeName.toLowerCase();
        if(this.count >= Console.setting.maxCacheSize)
        {
            this.count = 0;
            e.innerHTML = "";
        }

        if(s != null)
        {
            if(typeof(s) != "string")
            {
                s = s.toString();
            }
        }
        else
        {
            s = "null";
        }

        if(nodeName == "textarea")
        {
            e.appendChild(document.createTextNode(s));
        }
        else
        {
            var p = document.createElement("pre");
            p.appendChild(document.createTextNode(s));
            e.appendChild(p);
        }

        if(Console.scroll == true)
        {
            if(b != false && Console.target != null)
            {
                if(Console.timer == null)
                {
                    Console.timer = setTimeout(function(){Console.target.scrollTop = Console.target.scrollHeight; Console.timer = null;}, 500);
                }
            }
        }

        this.count++;
    }
};

Console.setScroll = function(scroll){
    if(scroll == true)
    {
        this.scroll = true;
        return;
    }

    if(scroll == false)
    {
        this.scroll = false;
        return;
    }

    if(this.scroll == true)
    {
        this.scroll = false;
    }
    else
    {
        this.scroll = true;
    }
};

Console.clear = function(){
    var e = document.getElementById("console");

    if(e != null)
    {
        this.count = 0;
        e.innerHTML = "";
    }
};

Console.open = function(){
    var e = document.getElementById("console");

    if(e != null)
    {
        e.style.display = "block";
    }
};

Console.close = function(){
    var e = document.getElementById("console");

    if(e != null)
    {
        e.style.display = "none";
    }
};

logger.debug = function(s, b){
    Console.println(s, b);
};

logger.getDateTime = function(date){
    return Console.getDateTime(date);
};

logger.test = function(){
    var e = document.getElementById("console");

    if(e != null)
    {
        this.debug(e.scrollTop + ": " + e.scrollHeight);
    }
};
