Ext.onReady(function() {

	// 系统当前时间
	var enddate = new Date();
	// 系统当前时间前七天
	var startdate = new Date();
	var currentCode = parent.document.getElementById("workerCode").value;
	var methods = "send";
	startdate.setDate(enddate.getDate() - 7);
	function ChangeDateToString(DateIn) {
		var Year = 0;
		var Month = 0;
		var Day = 0;
		var CurrentDate = "";
		Year = DateIn.getYear();
		Month = DateIn.getMonth() + 1;
		Day = DateIn.getDate();
		CurrentDate = Year + "-";
		if (Month >= 10) {
			CurrentDate = CurrentDate + Month + "-";
		} else {
			CurrentDate = CurrentDate + "0" + Month + "-";
		}
		if (Day >= 10) {
			CurrentDate = CurrentDate + Day;
		} else {
			CurrentDate = CurrentDate + "0" + Day;
		}
		return CurrentDate;
	};
	var fromDate = new Ext.form.DateField({
		format : 'Y-m-d',
		name : 'startDate',
		id : 'fromDate',
		itemCls : 'sex-left',
		clearCls : 'allow-float',
		checked : true,
		fieldLabel : "",
		allowBlank : false,
		readOnly : true,
		value : startdate,
		emptyText : '请选择',
		anchor : '100%'
	});
	var toDate = new Ext.form.DateField({
		format : 'Y-m-d',
		name : 'endDate',
		value : enddate,
		id : 'toDate',
		itemCls : 'sex-left',
		clearCls : 'allow-float',
		fieldLabel : "至",
		allowBlank : false,
		readOnly : true,
		emptyText : '请选择',
		anchor : '80%'
	});
	var Border = Ext.Viewport;
	var rn = new Ext.grid.RowNumberer({
		header : "序列号",
		selectMode : 'true',
		width : 70
	});
	var messageSm = new Ext.grid.RowSelectionModel({
		singleSelect : true,
		listeners : {
			rowselect : function(sm, row, rec) {
				if (rec.get("messageStatus") == 1 || rec.get("messageStatus") == 2 ) {
					Ext.get("sendmessage").dom.disabled = true
					Ext.get("delmessage").dom.disabled = true
				} else {
					Ext.get("sendmessage").dom.disabled = false
					Ext.get("delmessage").dom.disabled = false
				}
			}
		}
	});
	var cm = new Ext.grid.ColumnModel([rn, {
		header : "",
		dataIndex : "id",
		hidden : true
	}, {
		header : "状态",
		dataIndex : "statusFlag",
		sortable : true,
		allowblank : false,
		width : 100
	}, {
		header : "标题",
		dataIndex : "title",
		sortable : true,
		allowblank : false,
		width : 100
	}, {
		header : "接收人",
		dataIndex : "receiverName",
		allowblank : false,
		width : 100
	}, {
		header : "接收时间",
		dataIndex : "sendDate",
		allowblank : false,
		width : 100
	}, {
		header : "是否有文档",
		dataIndex : "docName",
		allowblank : false,
		width : 100,
		renderer : function(v) {
			if (v != null) {
				return '是'
			} else {
				return "否";
			}
		}
	}]);

	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'message/getMessageList.action'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : "totalProperty",
			root : "root"
		}, [{
			name : "id"
		}, {
			name : "messageId"
		}, {
			name : "sendById"
		}, {
			name : "receiveById"
		}, {
			name : "sendDate"
		}, {
			name : "messageStatus"
		}, {
			name : "senderName"
		}, {
			name : "receiverName"
		}, {
			name : "statusFlag"
		}, {
			name : "receiverName"
		}, {
			name : "text"
		}, {
			name : "title"
		}, {
			name : "docTypeId"
		}, {
			name : "docName"
		}, {
			name : "zbbmtxName"
		}])
	});
	var bbar = new Ext.PagingToolbar({
		pageSize : 18,
		store : ds,
		displayInfo : true,
		displayMsg : '共 {2} 条',
		emptyMsg : "没有记录"
	})
	ds.load({
		params : {
			starttime : ChangeDateToString(fromDate.getValue()),
			endtime : ChangeDateToString(toDate.getValue()),
			statusFlag : "0,1,2",
			start : 0,
			limit : 18
		}
	});
	ds.on('beforeload', function() {
		Ext.apply(this.baseParams, {
			starttime : ChangeDateToString(fromDate.getValue()),
			endtime : ChangeDateToString(toDate.getValue()),
			statusFlag : "0,1,2"
		})
	});
	// 分页用

	var grid = new Ext.grid.GridPanel({
		ds : ds,
		cm : cm,
		bbar : bbar,
		sm : messageSm,
		viewConfig : {
			forceFit : true
		},
		tbar : ['消息发送时间', fromDate, '至', toDate, {
			id : "query",
			text : "查询",
			iconCls : 'query',
			handler : function() {
				ds.load({
					params : {
						starttime : ChangeDateToString(fromDate.getValue()),
						endtime : ChangeDateToString(toDate.getValue()),
						statusFlag : "0,1,2",
						start : 0,
						limit : 18
					}
				});
				
			}
		}, {
			id : "addmessage",
			text : "消息增加",
			iconCls : 'add',
			handler : function() {
				var conn = Ext.lib.Ajax.getConnectionObject().conn;
				conn.open("POST", "comm/checkWorkerIsAdmin.action", false);
				conn.send(null);
				win.setTitle("消息增加");
				win.show();
				Ext.getCmp("save").setVisible(true);
				if (conn.status == "200") {
					if (conn.responseText != "true") {
						zbbmtxComboBox1.setValue("管理员");
						zbbmtxComboBox1.setDisabled(true);
						chsNameComboBox2.setDisabled(false);
						docTypeNameComboBox3.setDisabled(false);
						Ext.getCmp("title").setDisabled(false);
						Ext.get("title").dom.value = '';
						Ext.getCmp("text").setDisabled(false);
						Ext.get("text").dom.value = '';
						Ext.getCmp("docName").setDisabled(false);
						Ext.getCmp("view").setVisible(false);
						Ext.get("docName").dom.value = '';
						Ext.get("sendName").dom.value = '';
						docTypeNameComboBox3.setValue("请选择消息类型");
					} else {
						zbbmtxComboBox1.setDisabled(false);
						chsNameComboBox2.setDisabled(false);
						docTypeNameComboBox3.setDisabled(false);
						Ext.getCmp("title").setDisabled(false);
						Ext.get("title").dom.value = '';
						Ext.getCmp("text").setDisabled(false);
						Ext.get("text").dom.value = '';
						Ext.getCmp("docName").setDisabled(false);
						Ext.getCmp("view").setVisible(false);
						Ext.get("docName").dom.value = '';
						Ext.get("sendName").dom.value = '';
						docTypeNameComboBox3.setValue("请选择消息类型");
						zbbmtxComboBox1.setValue("");
						zbbmtxComboBox1.setDisabled(false);
					}
				}
			}
		}, {
			id : "delmessage",
			text : "消息删除",
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
						if (member.id) {
							ids.push(member.id);
						} else {
							store.remove(store.getAt(i));
						}
						Ext.Msg.confirm("删除", "是否确定删除的记录？",
								function(buttonobj) {
									if (buttonobj == "yes") {
										Ext.lib.Ajax.request('POST',
												'message/deleteMessage.action',
												{
													success : function(action) {
														Ext.Msg.alert("提示",
																"删除成功！")
														ds.reload();
													},
													failure : function() {
														Ext.Msg.alert('错误',
																'删除时出现未知错误.');
													}
												}, 'id=' + ids.join(","));
									}
								});
					}
				}
			}
		}, {
			id : "sendmessage",
			text : "消息发送",
			iconCls : 'send',
			handler : function() {
				methods = "update";
				var selrows = grid.getSelectionModel().getSelections();
				if (selrows.length > 0) {
					var rec = selrows[0].data;
					if (rec.messageStatus == 0) {
						win.show();
						Ext.getCmp("save").setVisible(false);
						zbbmtxComboBox1.setDisabled(true);
						chsNameComboBox2.setDisabled(true);
						docTypeNameComboBox3.setDisabled(true);
						Ext.getCmp("title").setDisabled(true);
						Ext.getCmp("text").setDisabled(true);
						Ext.getCmp("docName").setDisabled(true);
						if(rec.docName == null){
							Ext.getCmp("view").setVisible(false);
						}else{
							Ext.getCmp("view").setVisible(true);
						}
//						Ext.getCmp("view").setVisible(true);
						zbbmtxComboBox1.setValue(rec.zbbmtxName);
						Ext.get("sendName").dom.value = rec.receiverName;
						var _rec = eval('(' + "{data:" + Ext.encode(rec) + "}"
								+ ')');
						Ext.get('dName').dom.value = rec.docName;
						blockForm.getForm().loadRecord(_rec);
					} else {
						Ext.Msg.alert('提示信息', '此条消息已发送!');
					}
				} else {
					Ext.Msg.alert('提示信息', '请选择一条要发送的消息!');
				}
			}
		}]
	});

	// 设置管理员

	var storeCbx1 = new Ext.data.JsonStore({
		root : 'root',
		url : "message/getCustomerCompanyList.action",
		fields : ['zbbmtxCode', 'zbbmtxName']
	})
	storeCbx1.on('load', function() {
		storeCbx1
	})
	storeCbx1.load();

	var zbbmtxComboBox1 = new Ext.form.ComboBox({
		id : "zbbmtxCode",
		fieldLabel : "消息接收公司<font color='red'>*</font>",
		emptyText : '请选择客户公司',
		store : storeCbx1,
		hiddenName : 'comCode',
		displayField : "zbbmtxName",
		valueField : "zbbmtxCode",
		triggerAction : 'all',
		allowBlank : false,
		readOnly : true,
		width : 200
	});
	// 联系人显示
	var User = Ext.data.Record.create([{
		name : 'workerCode',
		hidden : true
	}, {
		name : 'workerName'
	}]);

	// l联系人数据源
	var role_store = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'message/findContacterByCode.action'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'data.totalCount',
			root : 'data.list'
		}, User)
	});
	// 消息接受人
	var chsNameComboBox2 = new Ext.form.ComboBox({
		id : "sendName",
		// hiddenName : 'sysmessageemp.receiveById',
		fieldLabel : '消息接收人',
		xtype : 'trigger',
		displayField : "workerName",
		valueField : "workerCode",
		triggerAction : 'all',
		mode : 'local',
		readOnly : true,
		width : 200,
		onTriggerClick : function(e) {
			if (zbbmtxComboBox1.getValue() == "") {
				Ext.Msg.alert('提示', '请选择消息接收公司');
				return;
			}
			role_store.load({
				params : {
					zbbmtxCode : zbbmtxComboBox1.getValue()
				}
			});
			rolewin.show();
		}
	})

	// 联系人页面头部
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false
	});
	var role_cm = new Ext.grid.ColumnModel([sm, {
		header : '联系人名称',
		dataIndex : 'workerName',
		align : 'left',
		width : 200
	}]);
	// 联系人页面表格
	var rolegrid = new Ext.grid.GridPanel({
		ds : role_store,
		cm : role_cm,
		sm : sm,
		region : 'center',
		title : '该公司所属联系人列表',
		fitToFrame : true,
		border : false
	})

	// 联系人窗口
	var rolewin = new Ext.Window({
		closeAction : 'hide',
		width : 300,
		height : 400,
		plain : true,
		layout : 'border',
		modal : true,
		items : [rolegrid],
		buttons : [{
			text : '确定',
			handler : function() {
				var selectedRows = rolegrid.getSelectionModel().getSelections();
				var workerCodes = "";
				var workerNames = "";
				if (selectedRows.length < 1)
					Ext.Msg.alert('提示信息', "请选择发送联系人！");
				else {
					for (i = 0; i < selectedRows.length; i++) {
						workerCodes = workerCodes
								+ selectedRows[i].data.workerCode + ",";
						workerNames = workerNames
								+ selectedRows[i].data.workerName + ",";
					}
				}
				if (workerCodes.length > 0) {
					workerCodes = workerCodes.substring(0, workerCodes.length
							- 1);
					workerNames = workerNames.substring(0, workerNames.length
							- 1);
				}
				Ext.get("workerCode").dom.value = workerCodes;
				Ext.get("sendName").dom.value = workerNames;
				rolewin.hide();
			}
		}, {
			text : '取消',
			handler : function() {
				// Ext.get("docName").dom.select();
				// document.selection.clear();
				rolewin.hide();
			}
		}]
	})

	// 文档类型
	storeCbx3 = new Ext.data.JsonStore({
		root : "root",
		url : "message/getWordTypeList.action",
		fields : ['docTypeId', 'docTypeName']
	})

	storeCbx3.load();

	var docTypeNameComboBox3 = new Ext.form.ComboBox({
		id : "docTypeId",
		hiddenName : "sysmessage.docTypeId",
		fieldLabel : "消息类型<font color='red'>*</font>",
		emptyText : '请选择消息类型',
		store : storeCbx3,
		displayField : "docTypeName",
		valueField : "docTypeId",
		triggerAction : 'all',
		allowBlank : false,
		readOnly : true,
		width : 200
	});

	var title = {
		id : "title",
		xtype : "textfield",
		fieldLabel : '标题',
		name : 'sysmessage.title',
		readOnly : false,
		width : 200
	}

	var text = {
		id : "text",
		xtype : "textarea",
		fieldLabel : '正文',
		name : 'sysmessage.text',
		readOnly : false,
		width : 200
	}
	var docName = new Ext.form.TextField({
		id : "docName",
		// xtype : "textfield",
		inputType : 'file',
		fieldLabel : '文档',
		name : 'docName',
		readOnly : false,
		height : 20,
		handler : function() {
			if (docTypeNameComboBox3.getValue() == "") {
				Ext.Msg.alert("提示", "请选择文档类型");
			}
		}
	})

	var btnView = new Ext.Button({
		id : "view",
		text : "查看",
		iconCls : 'view',
		handler : function() {
			window.open('power/message/downloadFile.action?messageId='
					+ Ext.get("messageId").dom.value);
		}
	})

	// 定义一个记录
	var RoleRecord = Ext.data.Record.create([{
		name : 'title'
	}, {
		name : 'text'
	}
			// , {
			// name : 'docName'
			// }
			]);

	// 表单对象
	var blockForm = new Ext.FormPanel({
		labelAlign : 'center',
		labelWidth : 100,
		// title : '新增/修改',
		frame : true,
		fileUpload : true,
		items : [zbbmtxComboBox1, {
			id : 'messageId',
			xtype : 'hidden'
		}, {
			id : 'workerCode',
			xtype : 'hidden',
			name : 'codes'
		}, {
			id : 'id',
			xtype : 'hidden'
		}, {
			id : 'dName',
			xtype : 'hidden'
		}, chsNameComboBox2, title, docTypeNameComboBox3, text, {
			layout : "column",
			border : false,
			items : [{
				columnWidth : 0.8,
				layout : "form",
				border : false,
				items : [docName]
			}, {
				columnWidth : 0.2,
				layout : "form",
				border : false,
				items : [btnView]
			}]
		}],
		reader : new Ext.data.JsonReader({
			root : 'root'
		}, RoleRecord)
	});
	function ShowSize(files) {
		var fso, f;
		fso = new ActiveXObject("Scripting.FileSystemObject");
		f = fso.GetFile(files);
		return f.size;
	}
	function checkType(str) {
		var fl = false;
		if (str.indexOf(".") == -1 && str != "") {
			fl = true;
		} else if (str.indexOf(".") != -1) {
			var s = str.substring(str.lastIndexOf(".") + 1, str.length);
			if (s == "doc" || s == "txt" || s == "docx" || s == "exce"
					|| s == "vsd" || s == "zip" || s == "rar" || str == "") {
				fl = false
			} else {
				fl = true;
			}
		}
		return fl;
	}

	// 窗体对象
	var win = new Ext.Window({
		el : 'window-win',
		width : 400,
		height : 300,
		closeAction : 'hide',
		modal : true,
		items : [blockForm],
		buttons : [{
			id : 'save',
			text : '保存',
			hidden : true,
			iconCls : 'save',
			handler : function() {
				var url = 'message/saveMessage.action';
				if (blockForm.getForm().isValid()) {
//					if (Ext.get('docName').dom.value != "") {
//						if (checkType(Ext.get('docName').dom.value)) {
//							Ext.Msg.alert('提示信息', '不能上传该类型')
//							return;
//						}
//						if (ShowSize(docName.value) > 200000000) {
//							Ext.Msg.alert('提示信息', '上传文件过大')
//							return;
//						}
//					}
					blockForm.getForm().submit({
						waitMsg : '保存中,请稍后...',
						url : url,
						method : 'post',
						params : {
							filePath : Ext.get('docName').dom.value,
							codes : Ext.get("codes").dom.value
						},
						success : function(form, action) {
							var msg = eval('(' + action.response.responseText
									+ ')');
							Ext.Msg.alert('提示', msg.msg);
							// Ext.get("docName").dom.select();
							// document.selection.clear();
							win.hide();
							ds.reload();
							parent.receive.document.getElementById("query")
									.click();
						},
						failure : function(form, action) {
							Ext.Msg.alert('错误', "保存失败！");
							// Ext.get("docName").dom.select();
							// document.selection.clear();
							win.hide();
						}
					});
				}
			}
		}, {
			text : "发送",
			iconCls : 'send',
			handler : function() {
				var url = 'message/sendMessage.action';
				if ("update" == methods) {
					url = 'message/updateMessage.action';
				}
				if (blockForm.getForm().isValid()) {
//					if (Ext.get('docName').dom.value != "") {
//						if (checkType(Ext.get('docName').dom.value)) {
//							Ext.Msg.alert('提示信息', '不能上传该类型')
//							return;
//						}
//						if (ShowSize(docName.value) > 200000000) {
//							Ext.Msg.alert('提示信息', '上传文件过大')
//							return;
//						}
//					}
					blockForm.getForm().submit({
						waitMsg : '发送中,请稍后...',
						url : url,
						method : 'post',
						params : {
							filePath : Ext.get('docName').dom.value,
							codes : Ext.get("codes").dom.value,
							id : Ext.get("id").dom.value
						},
						success : function(form, action) {
							var msg = eval('(' + action.response.responseText
									+ ')');
							Ext.Msg.alert('提示', msg.msg);
							// Ext.get("docName").dom.select();
							// document.selection.clear();
							win.hide();
							ds.reload();
							parent.receive.document.getElementById("query")
									.click();
						},
						failure : function(form, action) {
							Ext.Msg.alert('错误', "发送失败");
							// Ext.get("docName").dom.select();
							// document.selection.clear();
							win.hide();
						}
					});
				}
			}
		}, {
			text : '取消',
			iconCls : 'cancer',
			handler : function() {
				// Ext.get("docName").dom.select();
				// document.selection.clear();
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

	grid.on("dblclick", function() {
		Ext.get("sendmessage").dom.click();
	});

})