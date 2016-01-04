function TabView(containerId)
{
    this.containerId = containerId;
    this.current = null;
}

TabView.prototype.getTabLabelContainer = function(){
    var e = document.getElementById(this.containerId);

    if(e != null)
    {
        var c = null;

        if(e.nodeName.toLowerCase() == "div")
        {
            var list = e.childNodes;

            for(var i = 0, size = list.length; i < size; i++)
            {
                if(list[i].nodeType == 1 && list[i].nodeName.toLowerCase() == "ul")
                {
                    c = list[i];
                    break;
                }
            }
        }
        else if(e.nodeName.toLowerCase() == "ul")
        {
            c = e;
        }

        return c;
    }

    return null;
};

TabView.prototype.active = function(src){
    if(this.current == src)
    {
        return;
    }

    var tabId = src.getAttribute("tabId");

    if(tabId == null)
    {
        return;
    }

    var c = this.getTabLabelContainer();

    if(c == null)
    {
        return;
    }

    var list = c.childNodes;

    for(var i = 0, size = list.length; i < size; i++)
    {
        if(list[i].nodeType == 1 && list[i].nodeName.toLowerCase() == "li")
        {
            list[i].className = "tabhide";
        }
    }

    this.current = src;
    this.current.className = "tabshow";

    var e = c.parentNode.parentNode;

    if(e != null)
    {
        var node = null;
        var list = e.childNodes;

        for(var i = 0, size = list.length; i < size; i++)
        {
            node = list[i];

            if(node.nodeType != 1 || node.nodeName.toLowerCase() != "div")
            {
                continue;
            }

            var className = node.className;

            if(className != null && className.indexOf("tabpanel") > -1)
            {
                if(tabId == node.getAttribute("tabId"))
                {
                    node.style.display = "block";
                }
                else
                {
                    node.style.display = "none";
                }
            }
        }
    }
};

TabView.prototype.getCurrent = function(){
    return this.current;
};

var defaultTabView = new TabView("defaultTabView");