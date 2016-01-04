/**
 * Skin JavaScript Library v1.0.0
 * Copyright (c) 2010 xuesong.net
 * 
 * mailto: xuesong.net@163.com
 * Date: 2010-04-28 10:24:21
 * Revision: 1012
 */
var BindUtil = {};

BindUtil.bind = function(object, handler)
{
    return function(){
        return handler.apply(object, arguments);
    }
};

BindUtil.bindAsEventListener = function(object, handler)
{
    return function(event){
        return handler.call(object, (event || window.event));
    }
};

var EventUtil = {};

EventUtil.bind = function(target, type, handler)
{
    EventUtil.addEventListener(target, type, handler);
};

EventUtil.unbind = function(target, type, handler)
{
    EventUtil.removeEventListener(target, type, handler);
};

EventUtil.addEventListener = function(target, type, handler)
{
    /*
    var method = function(event){
        return handler.call(target, (event || window.event));
    }

    if(target.attachEvent)
    {
        target.attachEvent("on" + type, method);
    }
    else if(target.addEventListener)
    {
        target.addEventListener(type, method, false);
    }
    else
    {
        target["on" + type] = method;
    }
    */
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

EventUtil.removeEventListener = function(target, type, handler)
{
    if(target.detachEvent)
    {
        target.detachEvent("on" + type, handler);
    }
    else if(target.removeEventListener)
    {
        target.removeEventListener(type, handler, false);
    }
    else
    {
        target["on" + type] = null;
    }
};

EventUtil.stopPropagation = function(event)
{
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

        event.cancel = true;
        event.returnValue = false;
    }

    return false;
};

function Resizeable(source, target)
{
    this.x = 0;
    this.y = 0;

    this.source = source;
    this.target = target;
    this.frame = document.getElementById("drag_frame");

    this.start = function(event){
        var src = (event.srcElement || event.target);

        if(src.nodeName.toLowerCase() != this.source.nodeName.toLowerCase())
        {
            return false;
        }

        this.y = event.clientY - this.target.offsetTop;
        this.x = event.clientX - this.target.offsetLeft;

        var x = this.target.offsetLeft;
        var y = this.target.offsetTop;
        var w = this.target.offsetWidth;
        var h = this.target.offsetHeight;

        this.frame.style.top = y + "px";
        this.frame.style.left = x + "px";
        this.frame.style.width = w + "px";
        this.frame.style.height = h + "px";
        this.frame.style.display = "block";
        this.frame.style.zIndex = 99999;

        this.target.style.zIndex = (Resizeable.zIndex++);
        EventUtil.addEventListener(document, "mouseup", this.stopHandler);
        EventUtil.addEventListener(document, "mousemove", this.moveHandler);
        return this.stopPropagation(event);
    };

    this.move = function(event){
        var x = event.clientX - this.x;
        var y = event.clientY - this.y;
        // this.frame.style.top = y + "px";
        // this.frame.style.left = x + "px";
        this.frame.style.width = (event.offsetX + 4) + "px";
        return this.stopPropagation(event);
    };

    this.stop = function(event){
        /*
        var y = this.frame.offsetTop;
        var x = this.frame.offsetLeft;

        this.target.style.marginTop = "0px";
        this.target.style.marginLeft = "0px";
        this.target.style.top = y + "px";
        this.target.style.left = x + "px";
        */
        this.target.style.width = this.frame.style.width;
        this.frame.style.zIndex = -1;
        this.frame.style.display = "none";
        EventUtil.removeEventListener(document, "mouseup", this.stopHandler);
        EventUtil.removeEventListener(document, "mousemove", this.moveHandler);
        return this.stopPropagation(event);
    };

    this.stopPropagation = function(event){
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

        event.cancel = true;
        event.returnValue = false;

        return false;
    };

    this.stopHandler = BindUtil.bindAsEventListener(this, this.stop);
    this.moveHandler = BindUtil.bindAsEventListener(this, this.move);

    if(this.frame == null)
    {
        this.frame = document.createElement("div");
        this.frame.id = "drag_frame";

        if(document.all)
        {
             // #bbccdd
            this.frame.innerHTML = "<div style=\"width: 100%; height: 100%; background-color: #bbccdd; filter: alpha(opacity=10); cursor: default;\"></div>";
        }
        else
        {
            this.frame.innerHTML = "<div style=\"width: 100%; height: 100%; background-color: #0E89E6; opacity: 0.1; cursor: default;\"></div>";
        }

        document.body.appendChild(this.frame);
    }

    if(this.target != null)
    {
        this.target.style.position = "absolute";
        var y = this.target.offsetTop;
        var x = this.target.offsetLeft;
        var w = this.target.offsetWidth - 0;
        var h = this.target.offsetHeight - 0;

        /* border: 1px solid #93b7dc; background-color: #bbccdd; */
        /* border: 1px solid #3399FF; background-color: #0E89E6; #A8CAEC */
        var cssText = "position: absolute; display: none;"
            + " width: " + w + "px; height: " + h + "px; top:" + y + "px; left:" + x + "px;"
            + " border: 1px dashed #3399FF; background-color: transparent; cursor: default; z-index: -1";

        this.frame.style.cssText = cssText;
    }

    if(this.source != null)
    {
        EventUtil.addEventListener(this.source, "mousedown", BindUtil.bindAsEventListener(this, this.start));
    }
};

Resizeable.zIndex = 1001;

Resizeable.register = function(source, target)
{
    var e = document.getElementById(source);

    if(e != null)
    {
        new Resizeable(e, document.getElementById(target));
    }
};

Resizeable.dragable = function(source, target, event)
{
    var e = source;

    if(e != null)
    {
        if(e.getAttribute("dragable") != "1")
        {
            e.setAttribute("dragable", "1");

            if(e.style.zIndex != Resizeable.zIndex)
            {
                Resizeable.zIndex = Resizeable.zIndex + 2;
                e.style.zIndex = Resizeable.zIndex;
            }

            new Resizeable(e, target);
        }
    }
};
