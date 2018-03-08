Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
var date = new Date();
	var dateTime = new Ext.form.DateField({
		fieldLabel : '修改日期',
		format : 'Y-m-d H:i:s',
		name : 'invoice.lastModifiedDate',
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
		id : 'conInvoice-form',
		frame : true,
		autoWidth : true,
		autoHeight : true,
		align : 'center',
		labelAlign : 'left',
		items : [{
			xtype : 'fieldset',
			labelAlign : 'right',
			title : '发票类型维护',
			defaultType : 'textfield',
			autoHeight : true,
			border : true,
			items : [{
				name : 'invoice.invoiceId',
				xtype : 'hidden'
			}, {
				fieldLabel : '发票类型编码',
				allowBlank : false,
				anchor : '90%',
				name : 'invoice.invoiceCode',
				xtype : 'hidden'
			}, {
				fieldLabel : '发票类型名称',
				allowBlank : false,
				anchor : '90%',
				name : 'invoice.invoiceName'
			}, {
				fieldLabel : '税率(%)',
				xtype : 'numberfield',
				allowBlank : false,
				value :'0',
				anchor : '90%',
				name : 'invoice.tax'
			}, {
				xtype : 'hidden',
				anchor : '90%',
				name : 'invoice.lastModifiedBy'
			}, {
				fieldLabel : '修改人',
				readOnly : true,
				anchor : '90%',
				name : 'lastModifiedName'
			}, dateTime, {
				fieldLabel : '备注',
				anchor : '90%',
				xtype : "textarea",
				name : 'invoice.memo'
			}],
			buttons : [{
				text : '确定',
				iconCls : 'save',
				handler : function() {
					if (gridForm.getForm().isValid()) {
						gridForm.getForm().submit({
							url :  (Ext.get("invoice.invoiceId").dom.value == ""
											? "managecontract/addInvoice"
											: "managecontract/updateInvoice")
									+ '.action',
							waitMsg : '正在保存数据...',
							method : 'post',
							success : function(form, action) {
								if (Ext.get("invoice.invoiceId").dom.value == "") {
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
		name : 'invoiceId'
	}, {
		name : 'invoiceCode'
	}, {
		name : 'invoiceName'
	}, {
		name : 'tax'
	}, {
		name : 'memo'
	}, {
		name : 'lastModifiedName'
	}, {
		name : 'lastModifiedDate'
	}]);
	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'managecontract/findAllInvoices.action',
			method : 'post'
		}),
		reader : new Ext.data.JsonReader({
				// root : 'runwayList'
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
		id : 'invoiceId',
		header : 'ID',
		dataIndex : 'invoiceId',
		sortable : true,
		hidden : true,
		width : 200
	}, {
		id : 'invoiceCode',
		header : '发票类型编码',
		dataIndex : 'invoiceCode',
		width : 200,
		hidden : true,
		sortable : true
	}, {
		id : 'invoiceName',
		header : '发票类型名称',
		dataIndex : 'invoiceName',
		width : 200
	}, {
		header : '税率',
		dataIndex : 'tax',
		width : 200
	}, {
		header : '备注',
		dataIndex : 'memo',
		width : 200
	}, {
		header : '修改人',
		dataIndex : 'lastModifiedName',
		hidden : true,
		width : 200
	}, {
		header : '修改日期',
		dataIndex : 'lastModifiedDate',
		hidden : true,
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
				Ext.get("invoice.invoiceName").dom.focus();
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
					Ext.get("invoice.lastModifiedBy").dom.value = rec
							.get("lastModifiedBy");
					Ext.get("lastModifiedName").dom.value = rec
							.get("lastModifiedName");
					Ext.get("invoice.lastModifiedDate").dom.value = rec
							.get("lastModifiedDate");
					Ext.get("invoice.invoiceId").dom.value = rec
							.get("invoiceId");
					Ext.get("invoice.invoiceCode").dom.value = rec
							.get("invoiceCode");
					Ext.get("invoice.invoiceName").dom.value = rec
							.get("invoiceName");
					Ext.get("invoice.tax").dom.value = rec.get("tax");
					if(rec.get("memo") != null)
					{
					Ext.get("invoice.memo").dom.value = rec.get("memo");
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
						names += rec[i].data.invoiceName + ",";
					}
					names = names.substring(0, names.length - 1);

					if (confirm("确定要删除\"" + names + "\"发票类型吗？")) {
						for (i = 0; i < rec.length; i++) {
							Ext.Ajax.request({
								url : 'managecontract/deleteInvoice.action?invoice.invoiceId='
										+ rec[i].get("invoiceId"),
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
		id : 'conInvoice-grid',
		autoScroll : true,
		title : '发票类型列表',
		ds : ds,
		cm : colModel,
		sm : box2,
		tbar : tbar,
		border : true
	});
	conInvoiceGrid.on('rowdblclick', function(grid, rowIndex, e) {
		win.show();
		var rec = conInvoiceGrid.getStore().getAt(rowIndex);
		Ext.get("invoice.invoiceId").dom.value = rec.get("invoiceId");
		Ext.get("invoice.invoiceCode").dom.value = rec.get("invoiceCode");
		Ext.get("invoice.invoiceName").dom.value = rec.get("invoiceName");
		Ext.get("invoice.tax").dom.value = rec.get("tax");
		if(rec.get("memo") != null)
		{
		Ext.get("invoice.memo").dom.value = rec.get("memo");
		}
		Ext.get("invoice.lastModifiedBy").dom.value = rec.get("lastModifiedBy");
		Ext.get("lastModifiedName").dom.value = rec.get("lastModifiedName");
		Ext.get("invoice.lastModifiedDate").dom.value = rec
				.get("lastModifiedDate");
	});
	var win = new Ext.Window({
		el : 'form-div',
		width : 400,
		height : 300,
		modal :true,
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