// 设置树的点击事件
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var gridForm = new Ext.FormPanel({
		id : 'params',
		frame : true,
		autoWidth : true,
		autoHeight : true,
		align : 'center',
		labelAlign : 'left',
		items : [{
			xtype : 'fieldset',
			labelAlign : 'right',
			title : '参数信息',
			defaultType : 'textfield',
			autoHeight : true,
			border : true,
			defaults : {
				width : 240
			},
			items : [{
				id : 'parmNo',
				name : 'params.parmNo',
				readOnly : true,
				fieldLabel : '参数编码'
			}, {
				id : 'parmName',
				name : 'params.parmName',
				readOnly : true,
				fieldLabel : '参数名称'
			}, {
				id : 'parmValue',
				fieldLabel : '参数值',
				name : 'params.parmValue'
			}],
			buttons : [{
				text : '保存',
				iconCls : 'save',
				handler : function() {
					Ext.Ajax.request({
						url : 'comm/updateParamsValue.action',
						method : 'post',
						params : {
							parmNo : Ext.get('parmNo').dom.value,
							parmValue : Ext.get('parmValue').dom.value
						},
						success : function(request, result) {
							ds.reload();
						},
						failure : function(request, result) {
							Ext.Msg.alert('提示信息', '出现未知错误！');
						}
					});
					win.hide();
				}
			}, {
				text : '取消',
				iconCls : 'cancer',
				handler : function() {
					win.hide();
				}
			}]
		}]
	});
	var param = new Ext.data.Record.create([{
		name : 'parmNo'
	}, {
		name : 'parmName'
	}, {
		name : 'parmValue'
	}, {
		name : 'enterpriseCode'
	}]);
	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'comm/getParamsList.action',
			method : 'post'
		}),
		reader : new Ext.data.JsonReader({}, param)
	});
	ds.load();
	var colModel = new Ext.grid.ColumnModel([
			new Ext.grid.CheckboxSelectionModel({
				singleSelect : false,
				listeners : {
					rowselect : function(sm, row, rec) {
					}
				}
			}), {
				id : 'parmNo',
				header : '参数编号',
				dataIndex : 'parmNo',
				sortable : true
			}, {
				header : '参数名称',
				dataIndex : 'parmName'
			}, {
				header : '参数值',
				dataIndex : 'parmValue'
			}]);
	colModel.defaultSortable = true;
	var tbar = new Ext.Toolbar({
		items : [{
			id : 'btnUpdate',
			text : "修改",
			iconCls : 'update',
			handler : modify
		}, {
			id : 'rmb',
			text : '获取本币',
			handler : function() {
				Ext.Ajax.request({
					url : 'comm/getOriCurrency.action',
					method : 'post',
					success : function(result,request ) {
						var o = eval('(' + result.responseText + ')');
						alert(o.data);
					},
					failure : function(result,request ) {
						Ext.Msg.alert('提示信息', '出现未知错误！');
					}
				})
			}
		}, {
			id : 'type',
			text : '获取领用方式',
			handler : function() {
				Ext.Ajax.request({
					url : 'comm/getIssueType.action',
					method : 'post',
					success : function(result,request) {
						var o = eval('(' + result.responseText + ')');
						alert(o.data);
					},
					failure : function(result,request) {
						Ext.Msg.alert('提示信息', '出现未知错误！');
					}
				})
			}
		}]
	});
	function modify() {
		var rec = paramGrid.getSelectionModel().getSelections();
		if (rec.length != 1) {
			Ext.Msg.alert('提示信息', "请选择一行！");
			return false;
		} else {
			win.show();
			gridForm.getForm().loadRecord(rec[0]);
		}

	}
	var paramGrid = new Ext.grid.GridPanel({
		id : 'param',
		ds : ds,
		cm : colModel,
		tbar : tbar,
		border : false,
		autoWidth : true,
		autoHeight : true,
		fitToFrame : true
	});

	paramGrid.on('rowdblclick', function(grid, rowIndex, e) {
		win.show();
		var rec = paramGrid.getStore().getAt(rowIndex);
		gridForm.getForm().loadRecord(rec);

	});

	var win = new Ext.Window({
		el : 'form-div',
		width : 400,
		height : 250,
		modal : true,
		closeAction : 'hide',
		items : [gridForm]
	});

	var viewport = new Ext.Viewport({
		layout : 'fit',
		autoWidth : true,
		autoHeight : true,
		fitToFrame : true,
		items : [paramGrid]
	});
	setTimeout(function() {
		Ext.get('loading').remove();
		Ext.get('loading-mask').fadeOut({
			remove : true
		});
	}, 250);
});