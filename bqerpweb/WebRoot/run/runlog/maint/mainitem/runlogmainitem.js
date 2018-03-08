Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	var gridForm = new Ext.FormPanel({
		id : 'mainItem-form',
		frame : true,
		autoWidth : true,
		autoHeight : true,
		align : 'center',
		labelAlign : 'left',
		items : [{
			xtype : 'fieldset',
			labelAlign : 'right',
			title : '重点事项维护',
			defaultType : 'textfield',			
			autoHeight : true,
			border : true,
			items : [{
				name : 'item.itemId',
				xtype : 'hidden'
			}, {
				fieldLabel : '重点事项编码',
				allowBlank : false,
				anchor : '80%',
				name : 'item.mainItemCode'
			}, {
				fieldLabel : '重点事项名称',
				allowBlank : false,
				anchor : '80%',
				name : 'item.mainItemName'
			}, {
				fieldLabel : '显示顺序',
				xtype : 'numberfield',
				allowBlank : false,
				anchor : '80%',
				name : 'item.diaplayNo'
			}],
			buttons : [{
				text : '保存',
				iconCls : 'save',
				handler : function() {
					if (gridForm.getForm().isValid()) {
						gridForm.getForm().submit({
							url : 'runlog/'
									+ (Ext.get("item.itemId").dom.value == ""
											? "addRunLogMainItem"
											: "updateRunLogMainItem")
									+ '.action',
							waitMsg : '正在保存数据...',
							method : 'post',
							success : function(form, action) {
						var o = eval("(" + action.response.responseText + ")");
						Ext.Msg.alert("注意", o.msg);
						ds.load();
						win.hide(); 
					},
					faliue : function() {
						Ext.Msg.alert('错误', '出现未知错误.');
					}
				});

					}
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

	var mainItem = new Ext.data.Record.create([{
		name : 'itemId'
	}, {
		name : 'mainItemCode'
	}, {
		name : 'mainItemName'
	}, {
		name : 'diaplayNo'
	}, {
		name : 'isUse'
	}, {
		name : 'enterpriseCode'
	}]);

	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'runlog/findRunLogMainItem.action',
			method : 'post'
		}),
		reader : new Ext.data.JsonReader({
				root : "list",
		totalProperty : "totalCount"
		}, mainItem)
	});
	ds.load();

	var box = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false,
		listeners : {
			rowselect : function(sm, row, rec) {
			}
		}
	});

	var colModel = new Ext.grid.ColumnModel([box, {
		id : 'itemId',
		header : 'ID',
		dataIndex : 'itemId',
		sortable : true,
		hidden : true,
		width : 200
	}, {
		id : 'mainItemCode',
		header : '重点事项编码',
		dataIndex : 'mainItemCode',
		width : 150,
		sortable : true
	}, {
		header : '重点事项名称',
		dataIndex : 'mainItemName',
		width : 250
	}, {
		header : '使用状态',
		dataIndex : 'isUse',
		width : 150,
		renderer : function changeIt(val) {
			return (val == "Y") ? "使用" : "停用";
		}
	}, {
		header : '显示顺序',
		dataIndex : 'diaplayNo',
		width : 200
	}]);

	var fuuzy = new Ext.form.TextField({
		id : "fuzzy",
		name : "fuzzy"
	});

	colModel.defaultSortable = true;
	var tbar = new Ext.Toolbar({
		items : [fuuzy, {
			text : "查询",
			iconCls : 'query',
			handler : function() {
				var fuzzytext = fuuzy.getValue();
				ds.baseParams = {
					fuzzy : fuzzytext
				};
				ds.load();
			}
		}, '-', {
			id : 'btnAdd',
			text : "新增",
			iconCls : 'add',
			handler : function() {
				win.show();
				gridForm.getForm().reset();
			}
		}, '-', {
			id : 'btnDelete',
			text : "删除",
			iconCls : 'delete',
			handler : function() {
				var rec = mainItemGrid.getSelectionModel().getSelections();
				var names = "";
				if (rec.length == 0) {
					Ext.Msg.alert("提示", "请选择要删除的记录！");
				} else {
					for (i = 0; i < rec.length; i++) {
						names += rec[i].data.mainItemName + ",";
					}
					names = names.substring(0, names.length - 1);

					if (confirm("确定要删除\"" + names + "\"重点事项吗？")) {
						for (i = 0; i < rec.length; i++) {
							Ext.Ajax.request({
								url : 'runlog/deleteRunLogMainItem.action?item.itemId='
										+ rec[i].get("itemId"),
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
		}, '-', {
			id : 'btnUpdate',
			text : "修改",
			iconCls : 'update',
			handler : function() {
				var rec = mainItemGrid.getSelectionModel().getSelections();
				if (rec.length != 1) {
					Ext.Msg.alert("请选择一行！");
					return false;
				} else {
					win.show();
					var rec = mainItemGrid.getSelectionModel().getSelected();
					Ext.get("item.itemId").dom.value = rec.get("itemId");
					Ext.get("item.mainItemCode").dom.value = rec
							.get("mainItemCode");
					Ext.get("item.mainItemName").dom.value = rec
							.get("mainItemName");
					Ext.get("item.diaplayNo").dom.value = rec.get("diaplayNo");
				}
			}
		}]
	});

	var mainItemGrid = new Ext.grid.GridPanel({
		id : 'mainItem-grid',
		autoScroll : true,
		ds : ds,
		cm : colModel,
		sm : box,
		tbar : tbar,
		border : true
	});
	mainItemGrid.on('rowdblclick', function(grid, rowIndex, e) {
		win.show();
		var rec = mainItemGrid.getStore().getAt(rowIndex);
		Ext.get("item.itemId").dom.value = rec.get("itemId");
		Ext.get("item.mainItemCode").dom.value = rec.get("mainItemCode");
		Ext.get("item.mainItemName").dom.value = rec.get("mainItemName");
		Ext.get("item.diaplayNo").dom.value = rec.get("diaplayNo");
	});

	var win = new Ext.Window({
		el : 'form-div',
		width : 400,
		height : 200,
		modal:true,
		closeAction : 'hide',
		items : [gridForm]
	});
	var viewport = new Ext.Viewport({
		region : "center",
		layout : 'fit',
		autoWidth : true,
		autoHeight : true,
		fitToFrame : true,
		items : [mainItemGrid]
	});

	setTimeout(function() {
		Ext.get('loading').remove();
		Ext.get('loading-mask').fadeOut({
			remove : true
		});
	}, 250);
});