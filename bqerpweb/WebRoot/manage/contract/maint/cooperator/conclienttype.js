Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
var date = new Date();
	var dateTime = new Ext.form.DateField({
		fieldLabel : '修改日期',
		format : 'Y-m-d H:i:s',
		name : 'type.lastModifiedDate',
		// value : '2008-11',
		id : 'lastModifiedDate',
		itemCls : 'sex-left',
		clearCls : 'allow-float',
		checked : true,
		readOnly:true, 
		value : date,
		readOnly : true,
		anchor : '90%'
	});
	var gridForm = new Ext.FormPanel({
		id : 'conclienttype-form',
		frame : true,
		autoWidth : true,
		autoHeight : true,
		align : 'center',
		labelAlign : 'left',
		items : [{
			xtype : 'fieldset',
			labelAlign : 'right',
			title : '合作伙伴类型维护',
			defaultType : 'textfield',
			autoHeight : true,
			border : true,
			items : [{
				name : 'type.clientTypeId',
				xtype : 'hidden'
			}, {
				fieldLabel : '合作伙伴类型编码',
				allowBlank : false,
				anchor : '90%',
				name : 'type.clientTypeCode',
				xtype : 'hidden'
			}, {
				fieldLabel : '合作伙伴类型名称',
				allowBlank : false,
				anchor : '90%',
				name : 'type.typeName'
			}, {
				xtype : 'hidden',
				anchor : '90%',
				name : 'type.lastModifiedBy'
			}, {
				fieldLabel : '修改人',
				readOnly : true,
				anchor : '90%',
				name : 'lastModifiedName'
			}, dateTime, {
				fieldLabel : '备注',
				anchor : '90%',
				xtype : "textarea",
				name : 'type.memo'
			}],
			buttons : [{
				text : '确定',
				iconCls : 'save',
				handler : function() {
					if (gridForm.getForm().isValid()) {
						gridForm.getForm().submit({
							url : 'managecontract/'
									+ (Ext.get("type.clientTypeId").dom.value == ""
											? "managecontract/addConClientType"
											: "managecontract/updateConClientType")
									+ '.action',
							waitMsg : '正在保存数据...',
							method : 'post',
							success : function(form, action) {
								if (Ext.get("type.clientTypeId").dom.value == "") {
									win.hide();
									ds.load();
								} else {
									win.hide();
									ds.load();
									Ext.Msg.alert('提示', '修改成功!');
								}
								
							},
							failure : function(form, action) {
								var o = eval("("
											+ action.response.responseText
											+ ")");
									Ext.Msg.alert("注意", o.errMsg);
							}
						});
					}
				}
			}, {
				text : '关闭',
				iconCls : 'cancer',
				handler : function() {
					gridForm.getForm().reset();
					win.hide();
				}
			}]
		}]
	});

	var conInvoice = new Ext.data.Record.create([{
		name : 'clientTypeId'
	}, {
		name : 'clientTypeCode'
	}, {
		name : 'typeName'
	}, {
		name : 'isUse'
	}, {
		name : 'memo'
	}, {
		name : 'lastModifiedName'
	}, {
		name : 'lastModifiedDate'
	}]);
	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'managecontract/findConClientType.action',
			method : 'post'
		}),
		reader : new Ext.data.JsonReader({
				 root : 'list'
				}, conInvoice)
	});
	ds.load();
	var box2 = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false,
		listeners : {
			rowselect : function(sm, row, rec) {
			}
		}
	});

	var colModel = new Ext.grid.ColumnModel([box2, {
		id : 'clientTypeId',
		header : 'ID',
		dataIndex : 'clientTypeId',
		sortable : true,
		hidden : true,
		width : 200
	}, {
		id : 'clientTypeCode',
		header : '合作伙伴类型编码',
		dataIndex : 'clientTypeCode',
		width : 200,
		hidden : true,
		sortable : true
	}, {
		id : 'typeName',
		header : '合作伙伴类型名称',
		dataIndex : 'typeName',
		align:'center',
		width : 200
	}, 
		{
		header : '备注',
		dataIndex : 'memo',
		align:'center',
		width : 200
	}, {
		header : '修改人',
		dataIndex : 'lastModifiedName',
		align:'center',
		//hidden : true,
		width : 200
	}, {
		header : '修改日期',
		dataIndex : 'lastModifiedDate',
		align:'center',
		//hidden : true,
		width : 200
	}]);
	// 排序
	colModel.defaultSortable = true;
	var tbar = new Ext.Toolbar({
		items : [{
			id : 'btnAdd',
			text : "增加",
			iconCls : 'add',
			handler : function() {
				gridForm.getForm().reset();
				win.show();
				Ext.get("type.typeName").dom.focus();
			}
		}, '-', {
			id : 'btnUpdate',
			text : "修改",
			iconCls : 'update',
			handler : function() {
				var rec = conInvoiceGrid.getSelectionModel().getSelections();
				if (rec.length != 1) {
					Ext.Msg.alert("提示", "请选择一行！");
					return false;
				} else {
					win.show();
					var rec = conInvoiceGrid.getSelectionModel().getSelected();
					
					Ext.get("type.clientTypeId").dom.value = rec.get("clientTypeId");
					Ext.get("type.clientTypeCode").dom.value = rec.get("clientTypeCode");
					Ext.get("type.typeName").dom.value = rec.get("typeName");
					Ext.get("type.lastModifiedBy").dom.value = rec.get("lastModifiedBy");
					Ext.get("lastModifiedName").dom.value = rec.get("lastModifiedName");
					Ext.get("type.lastModifiedDate").dom.value = rec.get("lastModifiedDate");
					if(rec.get("memo")!=null)
					{
					Ext.get("type.memo").dom.value = rec.get("memo");
					}
				}
			}
		}, '-', {
			id : 'btnDelete',
			text : "删除",
			iconCls : 'delete',
			handler : function() {
				var rec = conInvoiceGrid.getSelectionModel().getSelections();

				var names = "";
				if (rec.length == 0) {
					Ext.Msg.alert("提示", "请选择要删除的记录！");
				} else {
					for (i = 0; i < rec.length; i++) {
						names += rec[i].data.typeName + ",";
					}
					names = names.substring(0, names.length - 1);

					if (confirm("确定要删除\"" + names + "\"记录吗？")) {
						for (i = 0; i < rec.length; i++) {
							Ext.Ajax.request({
								url : 'managecontract/deleteConClientType.action?type.clientTypeId='
										+ rec[i].get("clientTypeId"),
								method : 'post',
								waitMsg : '正在删除数据...',
								success : function(result, request) {
									Ext.Msg.alert('提示', '删除成功!');
									ds.load();
								},
								failure : function(result, request) {
									Ext.Msg.alert('提示', '删除失败!');
								}
							});
						}
					}
				}
			}
		}]

	});

	var conInvoiceGrid = new Ext.grid.GridPanel({
		id : 'conclienttype-grid',
		autoScroll : true,
		title : '合作伙伴类型列表',
		ds : ds,
		cm : colModel,
		sm : box2,
		tbar : tbar,
		border : true
	});
	conInvoiceGrid.on('rowdblclick', function(grid, rowIndex, e) {
		win.show();
		var rec = conInvoiceGrid.getStore().getAt(rowIndex);
					Ext.get("type.clientTypeId").dom.value = rec.get("clientTypeId");
					Ext.get("type.clientTypeCode").dom.value = rec.get("clientTypeCode");
					Ext.get("type.typeName").dom.value = rec.get("typeName");
					Ext.get("type.lastModifiedBy").dom.value = rec.get("lastModifiedBy");
					Ext.get("lastModifiedName").dom.value = rec.get("lastModifiedName");
					Ext.get("type.lastModifiedDate").dom.value = rec.get("lastModifiedDate");
					if(rec.get("memo")!=null)
					{
					Ext.get("type.memo").dom.value = rec.get("memo");
					}
	});
	var win = new Ext.Window({
		el : 'form-div',
		width : 400,
		height : 260,
		modal:true,
		closeAction : 'hide',
		items : [gridForm]
	});
	var viewport = new Ext.Viewport({
		region : "center",
		layout : 'fit',
		// layout : "border",
		autoWidth : true,
		autoHeight : true,
		fitToFrame : true,
		items : [conInvoiceGrid]
	});

	setTimeout(function() {
		Ext.get('loading').remove();
		Ext.get('loading-mask').fadeOut({
			remove : true
		});
	}, 250);
});