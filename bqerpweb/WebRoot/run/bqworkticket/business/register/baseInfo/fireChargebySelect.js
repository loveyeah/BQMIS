// 选择动火执行人页面 
    Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
	Ext.onReady(function() {
    //==============  定义grid ===============
     Ext.QuickTips.init(); 
    
    // 员工或姓名输入框
    var userName = new Ext.form.TextField({
        id : "userName",
        name : "userName",
        width : 80,
        maxLength :25
    });
    
    
     //所属部门树型结构
    var deptIdChoose= new Ext.ux.ComboBoxTree({
        fieldLabel : '所属部门',
        id:'deptId',
        displayField:'text',
        width : 180,
        valueField:'id',
        hiddenName:'empinfo.deptId', 
        blankText : '请选择',
        emptyText : '请选择',    
        readOnly : true,
        tree : {
            xtype : 'treepanel', 
            loader : new Ext.tree.TreeLoader({
                dataUrl :  'empInfoManage.action?method=getDep'
            }),
            root : new Ext.tree.AsyncTreeNode({
                id : '0',
                name : '合肥电厂',
                text:'合肥电厂'
            }
            )
        },
        selectNodeModel : 'exceptRoot'
        
    })
    // 动火执行人数据来源
    var MyRecord = Ext.data.Record.create([
        {name : 'empCode'},
        {name : 'chsName'}
    ]);

    var dataProxy = new Ext.data.HttpProxy({
        url:'workticket/getDetailFireCharge.action'
    });

    var theReader = new Ext.data.JsonReader({
        root : "list",
        totalProperty : "totalCount"
    }, MyRecord);

    var store = new Ext.data.Store({
        proxy : dataProxy,
        reader : theReader
    });
   
    // grid列显示模式
    var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
            	header : '行号',
    			sortable : true,
    			width : 40
        	}), {
        header : '姓名',
        sortable : true,
        dataIndex : 'chsName'
    }, {
        header : '工号',
        sortable : true,
        dataIndex : 'empCode'
    }]);
    
    // 显示主体
    var grid = new Ext.grid.GridPanel({
        region : "center",
        layout : 'fit',
        store : store,
        cm : cm,
        enableColumnMove : false ,
        autoSizeColumns : true,
        viewConfig : {
            forceFit : true
        },
        sm : new Ext.grid.RowSelectionModel({
            singleSelect : true
        }),

        tbar : ['模糊查找(姓名或工号):', userName, '-', '  所属部门:',
            deptIdChoose, {
                text : Constants.BTN_QUERY,
                iconCls :Constants.CLS_QUERY,
                handler : queryRecord
            }],
        // 分页
        bbar : new Ext.PagingToolbar({
            pageSize : Constants.PAGE_SIZE,
            store : store,
            displayInfo : true,
            displayMsg : Constants.DISPLAY_MSG,
            emptyMsg : Constants.EMPTY_MSG
        })
    });



    new Ext.Viewport({
        enableTabScroll : true,   
        layout : "border",
        items : [{
            xtype : "panel",
            region : 'center',
            layout : 'fit',
            border : false,
            items : [grid]
        }]
    });
    
    // 双击grid执行操作
    grid.on("rowdblclick", returnRecord);
    
        
     /* 查询 */
    function queryRecord() {
        var userNametext = userName.getValue();
        var deptText = deptIdChoose.value;
        if (!deptText) {
//            Ext.Msg.alert(Constants.SYS_REMIND_MSG, "所属部门选择不能为空！")
        	deptText = '0';
        }
        store.baseParams = {
            userName : userNametext,
            deptId : deptText
        };
        store.load({
            params : {
                start : 0,
                limit : Constants.PAGE_SIZE
            }
        });

    }
    
    /* 返回一条记录 */
     function returnRecord() {
        // 得到该行记录
        var record;
        record = grid.getSelectionModel().getSelected();
        if (record != null) {
            var object = new Object();
            object.chsName = record.data.chsName;
            object.empCode = record.data.empCode;
            window.returnValue = object;
            window.close();
        }
    }
    });