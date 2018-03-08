//  工作票负责人选择
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
	Ext.onReady(function() {  
	var chargeBy=window.dialogArguments.chargeBy;
		
   // 工号或姓名输入框
   var userName = new Ext.form.TextField({
		id : "userName",
		name : "userName",
		width : 80,
		maxLength : 40
	});  		
    // 工作负责人数据来源 
	var MyRecord = new Ext.data.Record.create([{
		name : 'workerCode'
	}, {
		name : 'workerName'
	}  ]);

	// grid的store
	var store = new Ext.data.JsonStore({
		url : 'workticket/getWatcher.action',
		root : '',
		totalProperty : '',
		fields : MyRecord
	}); 
    // grid列模式设置
    var cm = new Ext.grid.ColumnModel([
       new Ext.grid.RowNumberer({
            	header : '行号',
    			sortable : true,
    			width : 40
        	}),  {
		header : '工号',
		sortable : true,
		dataIndex : 'workerCode'
	},  {
		header : '姓名',
		sortable : true,
		dataIndex : 'workerName'
	}]);
    
    // grid 框架
    var grid = new Ext.grid.GridPanel({
		region : "center",
		layout : 'fit',
		store : store,
				viewConfig : {
			forceFit : true 
		},
		cm : cm,
		autoSizeColumns : true,
		enableColumnMove : false ,
		sm : new Ext.grid.RowSelectionModel({
			singleSelect : true
		}),
		tbar : ['模糊查找(姓名或工号):', userName,  
			{
				text : "查找",
				iconCls : 'query',
				handler : queryRecord
			}] 
	});


    // 总视图
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
	
    // 双击事件
    grid.on("rowdblclick", returnRecord);
    
	/* 查询 */
    function queryRecord() { 
    	var userNametext = userName.getValue();  
		store.baseParams = {
			userName : userNametext
		};
		store.load();
    }
    
    /* 双击grid时，返回一条记录 */
     function returnRecord() { 
       // 得到该行记录		
     	var record = grid.getSelectionModel().getSelected();   
     	if (record != null) {  
     		if(chargeBy==record.get("workerCode"))
     		{
     			Ext.Msg.alert("提示","工作负责人和工作监护人不能为同一人，请重新选择！");
     		}
     		else
     		{
     			var obj=new Object();
     			obj.workerCode=record.get("workerCode");
     			obj.workerName=record.get("workerName");
			window.returnValue =obj;
    		window.close();
     		}
		} 
    } 
    queryRecord();
    });