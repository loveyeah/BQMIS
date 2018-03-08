Ext.onReady(function() {
    
    // 选择模式,'one'代表单选，'many'代表多选
    var method="one";
    // 是否显示check框
    var show=true;
    // 取得参数值
    if(getParameter("op")!="")
    {
         method=getParameter("op");
     
    }
    // 单选
    if(method!="one")
    {
        show=false;
    }
    
    //    // -----------定义tree-----------------
    var root = new Ext.tree.AsyncTreeNode({
        id : "0",
        text : "保护压板结构树"
    });
    // tree
    var mytree = new Ext.tree.TreePanel({        
        height : 900,
        region:"center",
        root : root,
        enableDD:true,
        enableDrag:true,        
        requestMethod : 'GET',
        loader : new Ext.tree.TreeLoader({
            // 载入数据
            url : "workticket/getTreeForSelect.action",
            baseParams : {
                id : '0',
                method:method
            }
        })
    });

    // --------------树选择操作-------------------------
     function treeselect()
     {
         if(method=="many")
         {// 多选
         var b = mytree.getChecked();
        var checkid = new Array;
        var checkname=new Array;
        for (var i = 0; i < b.length; i++) {
                // code
                checkid.push(b[i].text.substring(0,b[i].text.indexOf(' ')));
                // name
                checkname.push(b[i].text.substring(b[i].text.indexOf(' ') + 1,
                                        b[i].text.length));
        }        
        var pressboard = new Object();
        pressboard.code=checkid.toString();
        pressboard.name=checkname.toString();
        window.returnValue = pressboard;
        window.close();
         }
         
     }
    
    //-----------树的事件----------------------
    //树的单击事件
    mytree.on("click", clickTree, this);
    mytree.on('beforeload', function(node){
        // 指定某个节点的子节点
        mytree.loader.dataUrl = 'workticket/getTreeForSelect.action?method='+method+'&id=' + node.id;
    }, this);    

    // 单击事件
    function clickTree(node) {
    var pressboard = new Object();
    // code
    pressboard.code=node.text.substring(0,node.text.indexOf(' '))
    // name
    pressboard.name=node.text.substring(node.text.indexOf(' ') + 1,
                                        node.text.length);
    // 返回值                        
      window.returnValue = pressboard;
      window.close();
    }
    
    root.expand();//展开根节点

    // panel--------------------------------------    
  var panel = new Ext.Panel({
        layout : 'border',
        title : '树形方式查询',    
        collapsible : true,
        bbar:[{text:'确定',
               hidden:show,
               handler:treeselect}],        
        items : [mytree]
    });    
    // 显示区域
    new Ext.Viewport({
        enableTabScroll : true,
        layout : "fit",
        items : [panel]
    });

});