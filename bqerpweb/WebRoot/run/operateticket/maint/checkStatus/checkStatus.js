Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var method;
	var checkBefFlag = new Ext.form.ComboBox({
		fieldLabel : '操作前后标识',
		id : 'flag',
		name : 'flag',
		store : [['B', '操作前'], ['A', '操作后'], ['C', '共用']],
		valueField : "value",
		displayField : "text",
		mode : 'local',
		triggerAction : 'all',
		forceSelection : true,
		hiddenName : 'cs.checkBefFlag',
		listeners:{
			select : function(){
				ds.load({
					params : {
						checkBefFlag : checkBefFlag.getValue(),
						isRunning: CB_isRunning.getValue()
					}
				});
			}
		},
		editable : false,
		allowBlank : false,
		readOnly : true,
		selectOnFocus : true,
		value:'B',
		anchor : "85%"
	});
	 
	var CB_isRunning = new Ext.form.ComboBox({
		fieldLabel : '使用标识',
		id : 'isRunFlag',
		name : 'isRunFlag',
		store : [['N', '登记时'], ['Y', '执行时'] ],
		valueField : "value",
		displayField : "text",
		mode : 'local',
		triggerAction : 'all',
		forceSelection : true,
		hiddenName : 'cs.isRunFlag',
		listeners:{
			select : function(){
				ds.load({
					params : {
						checkBefFlag : checkBefFlag.getValue(),
						isRunning: CB_isRunning.getValue()
					}
				});
			}
		},
		editable : false,
		allowBlank : false,
		readOnly : true,
		selectOnFocus : true,
		value : 'N',
		anchor : "85%"
	});  
	var form = new Ext.form.FormPanel({
		id : 'check-form',
		el : 'form',
		defaultType : 'textfield',
		labelAlign : 'left',
		title : '增加/修改核实内容',
		labelWidth : 100,
		frame : true,
		layout : 'form',
		autoWidth : true,
		autoHeight : true,
		waitMsgTarget : true,
		items : [{
			name : "cs.checkStatueId",
			xtype : 'hidden',
			id : "checkStatueId"
		}, {
			fieldLabel : '核实情况内容',
			name : "cs.checkStatue",
			xtype:'textarea',
			anchor : "90%",
			id : "checkStatue"
		},
		{
			fieldLabel : '显示顺序',
			name : "cs.displayNo",
			xtype:'numberfield',
			anchor : "90%",
			id : "displayNo" 
		} 
		],
		buttons : [{
			text : '保存',
			iconCls:'save',
			handler : function() {
				if (method == "add") {
					actionUrl = "opticket/addCheckStatus.action";
				} else {
					actionUrl = "opticket/updateCheckStatus.action";
				}
				form.getForm().submit({
					url : actionUrl,
					method : 'post',
					waitMsg : '正在操作，请稍等...',
					params:{
						'cs.checkBefFlag' : checkBefFlag.getValue(),
						'cs.isRunFlag'    :  CB_isRunning.getValue()
					},
					success : function(form, action) {
						Ext.get("query").dom.click();
					},
					failure : function(form, action) {
						Ext.Msg.alert('提示信息', '操作失败！');
					}
				})
				win.hide();
			}
		}, {
			text : '取消',
			iconCls:'cancer',
			handler : function() {
				win.hide();
			}
		}]
	});
	var checkStatus = Ext.data.Record.create([{
		name : 'checkStatueId'
	}, {
		name : 'checkStatue'
	}, {
		name : 'checkBefFlag'
	}, {
		name : 'isRunFlag' 
	}]);
	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'opticket/findCheckStatus.action'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'total',
			root : 'list'
		}, checkStatus)
	});
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false,
		listeners : {
			rowselect : function(sm, row, rec) {
			}
		}
	});
	ds.load({
		params : {
			checkBefFlag : checkBefFlag.getValue(),
			isRunning: CB_isRunning.getValue()
		}
	});
	var cm = new Ext.grid.ColumnModel([sm, {
		dataIndex : 'checkStatueId',
		align : 'left',
		hidden : true 
	}, {
		header : '核实情况内容',
		dataIndex : 'checkStatue',
		width : 500,
		align : 'left'
	}, {
		header : '操作前后标识',
		dataIndex : 'checkBefFlag',
		align : 'left',
		renderer : function(v){
			if(v=="C"){
				return "公共"
			}
			if(v=="B"){
				return "操作前"
			}if(v=="A"){
				return "操作后"
			}
		}
	}, {
		header : '运行时标识',
		dataIndex : 'isRunFlag',
		align : 'left',
		renderer : function(v){
			if(v=="Y"){
				return "执行时"
			}else{
				return "登记时"
			}
		}
	}]);
	cm.defaultSortable = true;
	var tbar = new Ext.Toolbar({
		items : ['操作前后标识', checkBefFlag, CB_isRunning,{
			id : 'query',
			iconCls : 'query',
			text : "查询",
			handler : function() { 
				ds.load({
					params : {
						checkBefFlag : checkBefFlag.getValue(),
						isRunning: CB_isRunning.getValue()
					}
				});
//				Ext.Ajax.request({
//					url : 'opticket/findCheckStatus.action?checkBefFlag='
//							+ checkBefFlag.getValue()+"&isRunning="+CB_isRunning.getValue(),
//					method : 'post',
//					waitMsg : '正在查询数据...',
//					success : function(result, request) {
//						var gridData = eval('(' + result.responseText + ')');
//						ds.loadData(gridData);
//					},
//					failure : function(result, request) {
//						Ext.Msg.alert('提示信息', '操作失败！');
//					}
//				})
			}
		}, '-', {
			id : 'btnAdd',
			iconCls : 'add',
			text : "新增",
			handler : function() {
				form.getForm().reset();
				method = 'add';
				win.show();
			}
		}, '-', {
			id : 'btnDelete',
			iconCls : 'delete',
			text : "删除",
			handler : function() {
				var selectedRows = grid.getSelectionModel().getSelections();
				if (selectedRows.length > 0) {
					var ids = "";
					Ext.Msg.confirm('提示', '删除的数据您将不能恢复,确定要删除吗?', function(b) {
						if (b == 'yes') {
							for (i = 0; i < selectedRows.length; i++) {
								ids += selectedRows[i].data.checkStatueId
										+ ",";
							}
							if (ids.length > 0) {
								ids = ids.substring(0, ids.length - 1);
							}
							Ext.Ajax.request({
								url : 'opticket/deleteCheckStatus.action',
								params : {
									ids : ids
								},
								method : 'post',
								waitMsg : '正在删除数据...',
								success : function(result, request) {
									Ext.get("query").dom.click();
								},
								failure : function(result, request) {
									Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
								}
							});
						}
					});
				} else {
					Ext.Msg.alert('提示', '请选择您要删除行!');
				}
			}
		}, '-', {
			id : 'btnUpdate',
			iconCls : 'update',
			text : "修改",
			handler : function() {
				method = "update";
				var selectedRows = grid.getSelectionModel().getSelections();
				if (selectedRows.length < 1) {
					Ext.Msg.alert('提示信息', "请选择一行!");
					return;
				}
				var lastSelected = selectedRows[selectedRows.length - 1];
				Ext.Ajax.request({
					url : 'opticket/findCheckStatusById.action',
					params : {
						id : lastSelected.data.checkStatueId
					},
					method : 'post',
					success : function(result, request) {
						var data = eval('(' + result.responseText + ')');
						win.show();
						form.getForm().loadRecord(data);
						
					},
					failure : function(result, request) {
						Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
					}
				});
			}
		}]
	})
	var grid = new Ext.grid.EditorGridPanel({
		ds : ds,
		cm : cm,
		sm : sm,
		tbar : tbar,
		autoWidth : true,
		fitToFrame : true,
		border : false,
		selModel : new Ext.grid.RowSelectionModel({
			singleSelect : false
		}),
		viewConfig : {
			forceFit : true
		},
		loadMask : {
			msg : '<img src="comm/images/extanim32.gif" width="32" height="32" style="margin-right: 8px;" align="absmiddle" />读取数据中 ...'
		}
	});
	grid.enableColumnHide = false;
	grid.on("rowdblclick", function() {
		Ext.get("btnUpdate").dom.click();
	});
	var win = new Ext.Window({
		el : 'window-win',
		width : 400,
		height : 250,
		closeAction : 'hide',
		modal : true,
		items : [form],
		buttons : []
	});
	var viewport = new Ext.Viewport({
		layout : 'fit',
		autoWidth : true,
		autoHeight : true,
		fitToFrame : true,
		items : [grid]
	});
	setTimeout(function() {
		Ext.get('loading').remove();
		Ext.get('loading-mask').fadeOut({
			remove : true
		});
	}, 250);
});