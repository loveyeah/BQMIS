Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
    Ext.QuickTips.init();

    // 自定义的节点类型
    function CustomNode(node) {
        // 当前节点
        this._currentNode = node;
        // 当前父节点
        this._parentNode = node.parentNode;
        if (!this._parentNode) {
            this._parentNode = node;
        }
        // 是否选中节点
        this.isCurrentClick = false;
        if (typeof CustomNode._initialized == "undefined") {
            CustomNode._initialized = true;
            // 设置当前节点
            CustomNode.prototype.setCurrentNode = function(argNode) {
                this._currentNode = argNode;
                this._parentNode = argNode.parentNode;
                if (!this._parentNode) {
                    this._parentNode = argNode;
                }
            }
        }

    }

    // 物料类别树
    var Tree = Ext.tree;
    var treeLoader = new Tree.TreeLoader({
        dataUrl : 'comm/getDeptsByPid.action?needBanzuCheck=yes' 
    })

    // treePanel
    var deptTree = new Tree.TreePanel({
        region : 'center',
        animate : true,
        allowDomMove : false,
        autoScroll : true,
        collapsible : true,
        split : true,
        border : false,
        rootVisible : false,
        root : root,
        animate : true,
        enableDD : false,
        containerScroll : true,
        loader : treeLoader
    });

    // 根结点
    var root = new Tree.AsyncTreeNode({
        text : '皖能合肥电厂',
        isRoot : true,
        id : '0'

    });
    deptTree.on("click", treeClick);
    deptTree.setRootNode(root);
    root.expand();
    root.select();
    var customNode = new CustomNode(root);

    /**
     * 点击树时
     */
    function treeClick(node, e) {
    	e.stopEvent();
        if (node.isLeaf()) {
            e.stopEvent();
        }
        // 选中节点
        customNode.isCurrentClick = true;
        // 设置为现在的节点
        customNode.setCurrentNode(node);
        node.toggle();
        
    }
    // 数据源--------------------------------
    var MyRecord = Ext.data.Record.create([{
        /** 班组id */
        name : 'deptId'
    }, {
        /** 班组名称 */
        name : 'deptName'
    }]);
    // 定义获取数据源
    var dataProxy = new Ext.data.HttpProxy({
        url : 'hr/getBanzuInfo.action'
    });

    // 定义格式化数据
    var theReader = new Ext.data.JsonReader({
        root : "list",
        totalProperty : "totalCount",
        sortInfo : {
            field : "deptId",
            direction : "ASC"
        }
    }, MyRecord);
    // 定义封装缓存数据的对象
    var queryStore = new Ext.data.Store({
        // 访问的对象
        proxy : dataProxy,
        // 处理数据的对象
        reader : theReader
    });

    // 定义选择列
    var sm = new Ext.grid.RowSelectionModel({
        singleSelect : true
    });

    // GridPanel
    var recordGrid = new Ext.grid.GridPanel({
        store : queryStore,
        border : false,
        columns : [
        // 自动行号
        new Ext.grid.RowNumberer({
            header : "行号",
            width : 31
        }), {
            header : "班组名称",
            sortable : true,
            width : 120,
            dataIndex : 'deptName'
        }, {
            header : "班组id",
            sortable : true,
            hidden : true,
            dataIndex : 'deptId'
        }],
        sm : sm,
        autoScroll : true,
        enableColumnMove : false,
        autoSizeColumns : true,
        viewConfig : {
            forceFit : false
        }
    });
    queryStore.load();

    // gridPanel
    var panelRight = new Ext.Panel({
        frame : false,
        border:true,
     	containerScroll : true,
        autoScroll : true,
        items : [recordGrid]
    });
    // ↓↓**********************中间按钮************↓↓//

    // 添加成员button
    var grantBar = new Ext.Button({
        id : 'btnGrant',
        height : 80,
        minWidth : 58,
        text : Constants.BTN_TO_RIGHT,
        iconCls:Constants.CLS_RIGHT_MOVE,
        layout : 'form',
        handler : function() {
            var id = customNode._currentNode.attributes.id;
            var name = customNode._currentNode.text;
            if (customNode.isCurrentClick) {
                if (customNode._currentNode.isLeaf()) {
                    if (check(id)) {
                        var data = new MyRecord({
                            'deptId' : id,
                            'deptName' : name
                        });
                        queryStore.add(data);
                        recordGrid.getView().refresh();
                    } else
                        Ext.Msg.alert(Constants.ERROR, Constants.PD023_E_001);
                } else
                    Ext.Msg.alert(Constants.ERROR, Constants.PD023_E_002);
            } else
                Ext.Msg.alert(Constants.REMIND, Constants.PD023_I_001);
        }
    });

    /**
     * check选择的部门已是班组 return ture,false
     */
    function check(id) {
        for (i = 0; i < queryStore.getCount(); i++) {
            if (queryStore.getAt(i).data.deptId == id) {
                return false;
            }
        }
        return true;
    }

    // 去除成员button
    var revokeBar = new Ext.Button({
        id : 'btnRevoke',
        text : Constants.BTN_TO_LEFT,
        iconCls:Constants.CLS_LEFT_MOVE,
        minWidth : 58,
        border : true,
        style : {
            'margin-top' : '30'
        },
        handler : function() {
            if (recordGrid.selModel.hasSelection()) {
                var selectedRows = recordGrid.getSelectionModel().getSelected();
                Ext.Msg.confirm(Constants.CONFIRM, String.format(Constants.PD023_C_001, selectedRows
                .get("deptName")), function(buttonobj) {
                    if (buttonobj == "yes") {
                        queryStore.remove(selectedRows);
                        recordGrid.getView().refresh();
                    }
                })
            } else
                Ext.Msg.alert(Constants.REMIND, Constants.PD023_I_004);
        }
    });

    // ↑↑**********************中间按钮************↑↑//
    // 确定按钮
    var btnSubmit = new Ext.Button({
        style : {
            'margin-top' : '30'
        },
        text : Constants.BTN_CONFIRM,
         iconCls : Constants.CLS_OK,
        handler : function() {
            var ids = "";
            // 获取现在grid里的部门ID
            for (i = 0; i < queryStore.getCount(); i++) {
                if (ids != "")
                    ids += ",";
                ids += queryStore.getAt(i).data.deptId;
            }
            Ext.Msg.confirm(Constants.NOTICE_CONFIRM, Constants.COM_C_001, function(buttonobj) {
                // 如果选择是
                if (buttonobj == "yes") {
                    Ext.Ajax.request({
                        method : Constants.POST,
                        url : 'hr/updateDeptBanzuData.action',
                        params : {
                            ids : ids
                        },
                        success : function(result, request) {
                            var o = eval("(" + result.responseText + ")");
                            var succ = o.msg;
                            // 如果更新失败，弹出提示
                            if (succ == Constants.SQL_FAILURE) {
                                Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                            } else
                                Ext.Msg.alert(Constants.REMIND, Constants.COM_I_004)
                            // 重新加载数据
                            queryStore.load();
                        }
                    });
                }
            })
        }
    });

    // panel
    var intop = new Ext.Panel({
        region : 'north',
        layout : 'fit',
        items : "",
        height : 180,
        border : false
    })

    // panel
    var incenter = new Ext.Panel({
        region : 'center',
        layout : 'form',
        style : 'padding-left:18px',
        items : [grantBar, revokeBar, btnSubmit],
        border : false
    })

    // panel
    var inwest = new Ext.Panel({
        region : 'west',
        layout : 'form',
        width : 8,
        items : "",
        border : false
    })

    // panel
    var mid = new Ext.Panel({
        frame : false,
        border : false,
        items : [intop, inwest, incenter]
    });

    // panel
    var top = new Ext.Panel({
        region : 'north',
        border : false,
        items : ""
    })

    var panelmid = new Ext.Panel({
		region : 'east',
		layout : 'fit',
		width:95,
		border:true,
		items : [mid]
	});
	
	var treePanel = new Ext.Panel({
		region : "center",
		layout : 'fit',
		containerScroll : true,
		autoScroll : true,
		border:false,
		items : [deptTree]
	});
	
    // 主panel
    var panelMain = new Ext.Panel({
    	enableTabScroll : true,
    	border:false,
    	containerScroll : true,
    	autoScroll : true,
		layout : "border",
		items : [treePanel, panelmid]
    })

	var panelleft = new Ext.Panel({
		region : 'west',
		layout : 'fit',
		autoScroll : true,
		enableTabScroll : true,
		containerScroll : true,
		width:360,
		border:false,
		
		items : [panelMain]
	});
	
	var registerPanel = new Ext.Panel({
		region : "center",
		layout : 'fit',
		containerScroll : true,
		autoScroll : true,
		border:false,
		items : [recordGrid]
	});
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		items : [panelleft, registerPanel]
	});
})