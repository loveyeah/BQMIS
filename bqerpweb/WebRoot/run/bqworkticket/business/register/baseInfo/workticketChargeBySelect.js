//  工作票负责人选择
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
	Ext.onReady(function() {
    var watcher=window.dialogArguments.watcher;
    Ext.QuickTips.init();
    
   // 工号或姓名输入框
   var userName = new Ext.form.TextField({
		id : "userName",
		name : "userName",
		width : 80,
		maxLength : 40
	});
  //===============grid设置===============
  // 所属部门树型结构
//   var deptIdChoose = new Ext.ux.ComboBoxTree({
//		fieldLabel : '所属部门',
//		id : 'deptId',
//		displayField : 'text',
//		width : 180,
//		valueField : 'id',
//		hiddenName : 'empinfo.deptId',
//		blankText : '请选择',
//		emptyText : '请选择',
//		readOnly : true,
//		tree : {
//			xtype : 'treepanel',
//			loader : new Ext.tree.TreeLoader({
//						dataUrl : 'empInfoManage.action?method=getDep'
//					}),
//			root : new Ext.tree.AsyncTreeNode({
//						id : '0',
//						name : '合肥电厂',
//						text : '合肥电厂'
//					})
//		},
//		selectNodeModel : 'exceptRoot' 
//
//	})

					
					
    // 工作负责人数据来源
    var MyRecord = Ext.data.Record.create([{
		name : 'empCode'
	}, {
		name : 'chsName'
	}, {
		name : 'deptCode'
	} ,{
		name : 'deptName'
	}]);

    var dataProxy = new Ext.data.HttpProxy({
		url : 'workticket/getDetailCharger.action'
	});

    var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	}, MyRecord);

	var store = new Ext.data.Store({
		proxy : dataProxy,
		reader : theReader
	});
   
    // grid列模式设置
    var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
            	header : '行号',
    			sortable : true,
    			width : 40
        	}),  {
		header : '姓名',
		sortable : true,
		dataIndex : 'chsName'
	},  {
		header : '工号',
		sortable : true,
		dataIndex : 'empCode'
	},  {
		hidden : true,
		dataIndex : 'deptCode'
	},  {
		hidden : true,
		dataIndex : 'deptName'
	}]);
    
    // grid 框架
    var grid = new Ext.grid.GridPanel({
		region : "center",
		layout : 'fit',
		store : store,
		cm : cm,
		autoSizeColumns : true,
		enableColumnMove : false ,
		viewConfig : {
			forceFit : true
		},
		sm : new Ext.grid.RowSelectionModel({
			singleSelect : true
		}),
		tbar : ['模糊查找(姓名或工号):', userName, 
//		'-','  所属部门:', deptIdChoose,
			{
				text : "查找",
				iconCls : 'query',
				handler : queryRecord
			}]
//			,
//		// 分页
//		bbar : new Ext.PagingToolbar({
////			pageSize : Constants.PAGE_SIZE,
////			pageSize : 10,
//			store : store,
//			displayInfo : true,
//			displayMsg : Constants.DISPLAY_MSG,
//			emptyMsg : Constants.EMPTY_MSG
//		})
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
//        var deptText = deptIdChoose.value;
//        if (!deptText) {
////			Ext.Msg.alert(Constants.SYS_REMIND_MSG, "所属部门类型不能为空！")
//        	deptText = '0';
//		}
		
		store.baseParams = {
			userName : userNametext,
//			deptId : deptText,
			workticketTypeName : window.dialogArguments.workticketTypeName
		};
		store.load({
			params : {
				start : 0,
				limit : Constants.PAGE_SIZE
			}
		});
    }
    
    /* 双击grid时，返回一条记录 */
     function returnRecord() { 
       // 得到该行记录		
     	var record = grid.getSelectionModel().getSelected();
     	
     	if (record != null) {
     		if(watcher!=""&&watcher==record.data.empCode)
     		{
     			Ext.Msg.alert("提示","工作负责人和工作监护人不能为同一人，请重新选择！");
     		}
     		else
     		{
     		var object = new Object();
     		object.chsName = record.data.chsName;
     		object.empCode = record.data.empCode;
     		object.deptCode = record.data.deptCode;
     		object.deptName = record.data.deptName;
			window.returnValue = object;
    		window.close();
     		}
		} 
    }
    
    //add by fyyang 090226
    queryRecord();
    });