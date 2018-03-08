Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var method;
	var form = new Ext.form.FormPanel({
		id : 'user-form',
		 el : 'form',
		defaultType : 'textfield',
		labelAlign : 'right',
		title : '重设密码',
		labelWidth : 120,
		frame : true,
		layout : 'form',
		autoWidth : true,
		autoHeight : true,
		waitMsgTarget : true,
		items : [{
			name : "user.workerId",
			xtype : 'hidden'
		}, {
			fieldLabel : '工号',
			name : 'user.workerCode',
			readOnly : true
		}, {
			fieldLabel : '姓名',
			name : "user.workerName",
			readOnly : true
		}, {
			fieldLabel : '密码',
			name : "user.loginPwd", 
			value:'123',
			readOnly : true
		}],
		buttons : [{
			text : '保存',
			handler : function() {
					if (confirm("确定要重置工号为\""
							+ Ext.get("user.workerCode").dom.value
							+ "\"的系统登录密码吗？")) {
						form.getForm().submit({
							url : 'system/modifyPassByManager.action',
							methods : 'post',
							success : function(form, action) {
								Ext.Msg.alert('提示信息', '密码修改成功');
							},
							failure : function(form, action) {
								Ext.Msg.alert('提示信息', '密码修改失败');
							}
						})
						win.hide();
					}
			}
		}, {
			text : '取消',
			handler : function() {
				win.hide();
			}
		}]
	});
	 form.render();

	var User = Ext.data.Record.create([{
		name : 'workerId'
	}, {
		name : 'workerCode'
	}, {
		name : 'ifUse'
	}, {
		name : 'style'

	}, {
		name : 'workerName'
	}, {
		name : 'email'
	}, {
		name : 'modifyBy'
	}, {
		name : 'modifyDate'
	}, {
		name : 'enterpriseCode'
	}, {
		name : 'loginCode'
	}]);
	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'system/findUsersByProperty.action'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'data.totalCount',
			root : 'data.list'
		}, User)
	});
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false,
		listeners : {
			rowselect : function(sm, row, rec) {
				Ext.getCmp("user-form").getForm().loadRecord(rec);
			}
		}
	});
	ds.load({
		params : {
			start : 0,
			limit : 18,
			userlike : ''
		}
	});

	var cm = new Ext.grid.ColumnModel([sm, {
		header : '编号',
		dataIndex : 'workerId',
		align : 'left',
		width : 50
	}, {
		header : '工号',
		dataIndex : 'workerCode',
		align : 'left',
		width : 150
	}, {
		header : '姓名',
		dataIndex : 'workerName',
		align : 'left',
		width : 150
	}, {
		header : '邮箱',
		dataIndex : 'email',
		align : 'left',
		width : 150
	}, {
		header : '修改人',
		dataIndex : 'modifyBy',
		align : 'left'

	}, {
		header : '修改时间',
		dataIndex : 'modifyDate',
		renderer : function formatDate(value) {
			return value ? value.substr(0, 10) : '';
		},
		align : 'left'
	}]);

	cm.defaultSortable = true;
	/* 设置分页的工具条 */
	var bbar = new Ext.PagingToolbar({
		pageSize : 18,
		store : ds,
		displayInfo : true,
		displayMsg : '共 {2} 条',
		emptyMsg : "没有记录"
	})
	var tbar = new Ext.Toolbar({
		items : ['工号/姓名', {
			name : 'userlike',
			xtype : 'textfield'
		}, {
			id : 'query',
			iconCls : 'query',
			text : "模糊查询",
			handler : function() {
				Ext.Ajax.request({
					url : 'system/findUsersByProperty.action',
					method : 'post',
					params : {
						start : 0,
						limit : 18,
						userlike : Ext.get("userlike").dom.value
					},
					waitMsg : '正在查询数据...',
					success : function(result, request) {
						var gridData = eval('(' + result.responseText + ')');
						ds.loadData(gridData);
					},
					failure : function(result, request) {
						Ext.Msg.alert('提示信息', "数据库出错，请联系管理员!");
					}
				})
			}
		}, '-', {
			id : 'btnUpdate',
			iconCls : 'update',
			text : "修改密码",
			handler : modifyPass
		}]
	})

	function modifyPass() {
		var selectedRows = grid.getSelectionModel().getSelections();
		if (selectedRows.length < 1) {
			Ext.Msg.alert('提示信息', "请选择一行!");
			return;
		}
		var lastSelected = selectedRows[selectedRows.length - 1];
		Ext.get("user.workerId").dom.value = lastSelected.get("workerId");
		
		Ext.get("user.workerCode").dom.value = lastSelected.get("workerCode");
		Ext.get("user.workerName").dom.value = lastSelected.get("workerName") == null
				? ''
				: lastSelected.get("workerName");
		win.show();
	}

	var grid = new Ext.grid.EditorGridPanel({
		ds : ds,
		cm : cm,
		sm : sm,
		bbar : bbar,
		tbar : tbar,
		autoWidth : true,
		fitToFrame : true,
		border : false,
		selModel : new Ext.grid.RowSelectionModel({
			singleSelect : false
		}),
		viewConfig : {
			forceFit : false
		}
	});
	grid.on("dblclick", function() {
		modifyPass();
	});
	ds.on('beforeload', function() {
		Ext.apply(this.baseParams, {
			userlike : Ext.get("userlike").dom.value
		});
	});
	grid.enableColumnHide = false;
	grid.on("dblclick", function() {
		Ext.get("btnUpdate").dom.click();

	});
	var win = new Ext.Window({
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