Ext.onReady(function() {

	var layout;
	var Border = Ext.Viewport;
	var method = "insert";
	Ext.QuickTips.init();// 支持tips提示
	Ext.form.Field.prototype.msgTarget = 'side';// 提示的方式
	// Ext.state.Manager.setProvider(new Ext.state.CookieProvider());

	var rn = new Ext.grid.RowNumberer({
		header : "序列号",
		selectMode : 'true',
		width : 70
	});
	var cm = new Ext.grid.ColumnModel([rn, {
		header : "",
		dataIndex : "zbbmtxCode",
		hidden : true
	}, {
		header : "客户公司简称",
		dataIndex : "zbbmtxName",
		sortable : true,
		width : 100
	}, {
		header : "客户公司名称",
		dataIndex : "zbbmtxAlias",
		width : 100
	}]);

	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'message/getCustomerCompanyList.action'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : "totalProperty",
			root : "root"
		}, [{
			name : "zbbmtxCode"
		}, {
			name : "zbbmtxName"
		}, {
			name : "zbbmHaveData"
		}, {
			name : "itemtype"
		}, {
			name : "itemCode"
		}, {
			name : "zbbmtxAlias"
		}, {
			name : "zbbmtxAlias1"
		}, {
			name : "isUse"
		}])
	});

	var grid = new Ext.grid.GridPanel({
		ds : ds,
		cm : cm,
		viewConfig : {
			forceFit : true
		},
		tbar : [{
			id : "addcus",
			text : "新增",
			iconCls : 'add',
			handler : function() {
				method = "insert";
				win.setTitle("新增客户公司");
				blockForm.getForm().reset();
				win.show(); // 显示表单所在窗体
			}
		}, {
			id : "updatecus",
			text : "修改",
			iconCls : 'update',
			handler : function() {
				method = "update";
				var selrows = grid.getSelectionModel().getSelections();
				if (selrows.length > 0) {
					win.setTitle("修改客户公司");
					var rec = selrows[0].data;
					win.show();
					Ext.get("zbbmtxCode").dom.value = rec.zbbmtxCode;
					Ext.get("zbbmtxName").dom.value = rec.zbbmtxName;
					Ext.get("zbbmtxAlias").dom.value = rec.zbbmtxAlias;
				} else {
					Ext.Msg.alert('提示信息', '请选择一个公司信息!');
				}
			}
		}, {
			id : "deletecus",
			text : "删除",
			iconCls : 'delete',
			handler : function() {
				var sm = grid.getSelectionModel();
				var selected = sm.getSelections();
				var ids = [];
				if (selected.length == 0 || selected.length < 0) {
					Ext.Msg.alert("提示", "请选择要删除的记录！");
				} else {
					for (var i = 0; i < selected.length; i += 1) {
						var member = selected[i].data;
						if (member.zbbmtxCode) {
							ids.push(member.zbbmtxCode);
						} else {
							store.remove(store.getAt(i));
						}
						Ext.Msg.confirm("删除", "是否确定删除的记录？",
								function(buttonobj) {

									if (buttonobj == "yes") {

										Ext.lib.Ajax
												.request(
														'POST',
														'message/deleteCusCompany.action?',
														{
															success : function(
																	action) {
																Ext.Msg
																		.alert(
																				"提示",
																				"删除成功！")
																ds.reload();
															},
															failure : function() {
																Ext.Msg
																		.alert(
																				'错误',
																				'删除时出现未知错误.');
															}
														}, 'zbbmtxCode='
																+ ids.join(","));
									}

								});
					}
				}
			}
		}]
	});

	ds.load();

	// 表单窗体
	var blockAddWindow;
	grid.on("dblclick", function() {
		Ext.get("updatecus").dom.click();
	});

	/**
	 * 以下是表单 __________________________________________________
	 */
	var zbbmtxCode = {
		id : "zbbmtxCode",
		xtype : "hidden",
		anchor : '90%',
		name : 'jljfcobject.zbbmtxCode'
	}
	var zbbmtxName = {
		id : "zbbmtxName",
		xtype : "textfield",
		allowBlank : false,
		fieldLabel : "客户公司简称<font color='red'>*</font>",
		name : 'jljfcobject.zbbmtxName',
		anchor : '90%',
		readOnly : false
	}
	var zbbmtxAlias = {
		id : "zbbmtxAlias",
		xtype : "textfield",
		allowBlank : false,
		fieldLabel : "客户公司名称<font color='red'>*</font>",
		name : 'jljfcobject.zbbmtxAlias',
		anchor : '90%',
		readOnly : false
	}

	// 定义一个记录
	var RoleRecord = Ext.data.Record.create([{
		name : 'zbbmtxCode'
	}, {
		name : 'zbbmtxName'
	}, {
		name : 'zbbmtxAlias'
	},

	]);
	// 表单对象
	var blockForm = new Ext.FormPanel({
		labelAlign : 'center',
		labelWidth : 90,
		frame : true,
		items : [zbbmtxCode, zbbmtxName, zbbmtxAlias],
		reader : new Ext.data.JsonReader({
			root : 'root'
		}, RoleRecord)
	});
	// blockForm.render();
	// 窗体对象
	var win = new Ext.Window({
		el : 'window-win',
		width : 400,
		// height : 150,
		autoHeight : true,
		closeAction : 'hide',
		modal : true,
		items : [blockForm],
		buttons : [{
			text : '保存',
			iconCls : 'save',
			handler : function() {
				var url = "";
				if (method == "insert") {
					url = "message/addCusCompany.action";
				} else if (method == "update") {
					url = "message/updateCusCompany.action"
				} else {
					url = "undifine";
				}
				if (blockForm.getForm().isValid()) {
					blockForm.getForm().submit({
						waitMsg : '保存中,请稍后...',
						url : url,
						params : {
							method : method
						},
						success : function() {
							Ext.Msg.alert("注意", "操作成功！");
							blockForm.getForm().reset();
							win.hide();
							ds.reload();
						},
						failure : function(form, action) {
							var o = eval("(" + action.response.responseText
										+ ")");
								Ext.Msg.alert('错误', o.errMsg);
						}
					});
					win.hide();
				}
			}
		}, {
			text : '取消',
			iconCls : 'cancer',
			handler : function() {
				blockForm.getForm().reset();
				win.hide();
			}
		}]
	});

	// 设定布局器及面板
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		items : [grid]
	});

});
