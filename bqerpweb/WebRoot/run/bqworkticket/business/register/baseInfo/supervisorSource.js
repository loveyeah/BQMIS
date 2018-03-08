// 工作票监工选择
	
	Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
	Ext.onReady(function() {
    //==============  定义grid ===============
     Ext.QuickTips.init(); 
   
    //工号姓名输入框
    var userName = new Ext.form.TextField({
        id : "userName",
        name : "userName",
        maxLength : 25
    });
     
    // 工作监工人员来源
    var MyRecord = Ext.data.Record.create([
        {name : 'empCode'},
        {name : 'chsName'},
        {name : 'deptName'}
    ]);
	
    var dataProxy = new Ext.data.HttpProxy({
        url:'workticket/getDetailSupervisor.action'
    });

    var theReader = new Ext.data.JsonReader({
        root : "list",
        totalProperty : "totalCount"
    }, MyRecord);

    var store = new Ext.data.Store({
        proxy : dataProxy,
        reader : theReader
    });
    // 排序
    store.setDefaultSort('empCode', 'asc');
    
    store.load({
		params : {
			start : 0,
			limit : Constants.PAGE_SIZE
		}
	});
	
	// GRID显示列属性
    var cm = new Ext.grid.ColumnModel([
    new Ext.grid.RowNumberer({
            	header : '行号',
    			sortable : true,
    			width : 40
        	}),
    {header:'姓名',dataIndex:'chsName',sortable : true},
    {header:'工号',dataIndex:'empCode',sortable : true},
    {hidden : true,dataIndex:'deptName'}
    ]);
    

    var grid = new Ext.grid.GridPanel({
        region : "center",
        layout : 'fit',
        store : store,
        cm:cm,
        enableColumnMove : false ,
        autoSizeColumns : true,
        viewConfig : {
            forceFit : true
        },
        sm: new Ext.grid.RowSelectionModel({singleSelect:true}),
        tbar : ['模糊查找(姓名或工号)', userName, {
									text : Constants.BTN_QUERY,
									iconCls : Constants.CLS_QUERY,
									handler : queryRecord
								}]
//								,
//        // 分页
//		bbar : new Ext.PagingToolbar({
//									pageSize : Constants.PAGE_SIZE,
//									store : store,
//									displayInfo : true,
//									displayMsg : Constants.DISPLAY_MSG,
//									emptyMsg : Constants.EMPTY_MSG
//								})
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
    
    // 双击grid时，返回员工姓名
    grid.on("rowdblclick", returnRecord);
    
	
	 /* 查询 */
    function queryRecord() { 
        var userNametext = userName.getValue();
        store.baseParams = {
            userName : userNametext
        };
		store.load({
			params : {
				start : 0,
				limit : Constants.PAGE_SIZE
			}
		});
    }
    /* 返回一条记录(员工姓名) */
     function returnRecord() { 
       // 得到该行记录		
     	var record = grid.getSelectionModel().getSelected();
     	if (record != null) {
     		var object = new Object();
     		object.chsName = record.data.chsName;
     		object.empCode = record.data.empCode;
			window.returnValue = object;
    		window.close();
		} 
    }
    });