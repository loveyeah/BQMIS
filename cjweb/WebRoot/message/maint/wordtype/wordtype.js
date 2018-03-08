Ext.onReady(function() {

	var layout;
	var Border = Ext.Viewport;
	var method = "insert";
	Ext.QuickTips.init();// 支持tips提示
	Ext.form.Field.prototype.msgTarget = 'side';// 提示的方式
	function getDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate();
		s += (t > 9 ? "" : "0") + t + " ";
		t = d.getHours();
		s += (t > 9 ? "" : "0") + t + ":";
		t = d.getMinutes();
		s += (t > 9 ? "" : "0") + t + ":";
		t = d.getSeconds();
		s += (t > 9 ? "" : "0") + t;
		return s;
	}
	// Ext.state.Manager.setProvider(new Ext.state.CookieProvider());

	var rn = new Ext.grid.RowNumberer({
		header : "序列号",
		selectMode : 'true',
		width : 70
	});
	var cm = new Ext.grid.ColumnModel([rn, {
		header : "",
		dataIndex : "docTypeId",
		hidden : true
	}, {
		header : "文档类型名称",
		dataIndex : "docTypeName",
		sortable : true,
		width : 100
	}, {
		header : "修改人",
		dataIndex : "lastModifiedBy",
		hidden : true
	}, {
		header : "修改日期",
		dataIndex : "lastModifiedDate",
		hidden : true
	}]);

	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'message/getWordTypeList.action'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : "totalProperty",
			root : "root"
		}, [{
			name : "docTypeId"
		}, {
			name : "docTypeName"
		}, {
			name : "lastModifiedBy"
		}, {
			name : "lastModifiedDate"
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
				win.setTitle("新增文档类型");
				blockForm.getForm().reset();
				win.show(); // 显示表单所在窗体
				Ext.get("lastModifiedBy").dom.value = document
						.getElementById("workerName").value
				Ext.get("lastModifiedDate").dom.value = getDate();
			}
		}, {
			id : "updatecus",
			text : "修改",
			iconCls : 'update',
			handler : function() {
				method = "update";
				win.setTitle("修改文档类型");
				var selrows = grid.getSelectionModel().getSelections();
				if (selrows.length > 0) {
					var rec = selrows[0].data;
					win.show();
					Ext.get("docTypeId").dom.value = rec.docTypeId;
					Ext.get("docTypeName").dom.value = rec.docTypeName;
					Ext.get("lastModifiedBy").dom.value = rec.lastModifiedBy;
					Ext.get("lastModifiedDate").dom.value = getDate(rec.lastModifiedDate);
				} else {
					Ext.Msg.alert('提示信息', '请选择一个文档类型!');
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
						if (member.docTypeId) {
							ids.push(member.docTypeId);
						} else {
							store.remove(store.getAt(i));
						}
						Ext.Msg.confirm("删除", "是否确定删除的记录？",
								function(buttonobj) {
									if (buttonobj == "yes") {
										Ext.lib.Ajax
												.request(
														'POST',
														'message/deleteWordType.action',
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
														}, 'docTypeId='
																+ ids.join(","));
									}
								});
					}
				}
			}
		}]
	});

	ds.load();
	grid.on("dblclick", function() {
		Ext.get("updatecus").dom.click();
	});

	/**
	 * 以下是表单 __________________________________________________
	 */
	var docTypeId = {
		id : "docTypeId",
		xtype : "hidden",
		anchor : '90%',
		name : 'syscmessage.docTypeId'
	}
	var docTypeName = {
		id : "docTypeName",
		xtype : "textfield",
		allowBlank : false,
		fieldLabel : "文档类型名称<font color='red'>*</font>",
		name : 'syscmessage.docTypeName',
		anchor : '90%',
		readOnly : false
	}
	var lastModifiedBy = {
		id : "lastModifiedBy",
		xtype : "textfield",
		fieldLabel : '修改人',
		name : 'syscmessage.lastModifiedBy',
		anchor : '90%',
		readOnly : true
	}
	var lastModifiedDate = {
		id : "lastModifiedDate",
		xtype : "textfield",
		fieldLabel : '修改日期',
		name : 'syscmessage.lastModifiedDate',
		anchor : '90%',
		readOnly : true
	}

	// 定义一个记录
	var RoleRecord = Ext.data.Record.create([{
		name : 'docTypeId'
	}, {
		name : 'docTypeName'
	}, {
		name : 'lastModifiedBy'
	}, {
		name : 'lastModifiedDate'
	},

	]);
	// 表单对象
	var blockForm = new Ext.FormPanel({
		labelAlign : 'center',
		labelWidth : 90,
//		title : '新增/修改',
		frame : true,
		items : [docTypeId, docTypeName, lastModifiedBy, lastModifiedDate],
		reader : new Ext.data.JsonReader({
			root : 'root'
		}, RoleRecord)
	});
	// blockForm.render();
	// 窗体对象
	var win = new Ext.Window({
		el : 'window-win',
		width : 400,
		height : 150,
		closeAction : 'hide',
		modal : true,
		items : [blockForm],
		buttons : [{
			text : '保存',
			iconCls : 'save',
			handler : function() {
				var url = "";
				if (method == "insert") {
					url = "message/addWordType.action";
				} else if (method == "update") {
					url = "message/updateWordType.action"
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
