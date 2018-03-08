Ext.onReady(function() {
	// 系统当前时间
	var enddate = new Date();
	// 系统到现在前七天
	var startdate = new Date();
	startdate.setDate(enddate.getDate() - 7);
	// 起始查询时间设定
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
	// 将所传页面时间转换为字符串格式
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
	// 终止查询时间设定
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
	// 自动生成每行的序列号
	var rn = new Ext.grid.RowNumberer({
		header : "序列号",
		selectMode : 'true',
		width : 80
	})
	// 设置grid的每列名称
	var cm = new Ext.grid.ColumnModel([rn, {
		header : "状态",
		dataIndex : "statusFlag",
		sortable : true,
		width : 100
	}, {
		header : "标题",
		dataIndex : "title",
		sortable : true,
		width : 100
	}, {
		header : "发送人",
		dataIndex : "senderName",
		width : 100
	}, {
		header : "发送时间",
		dataIndex : "sendDate",
		width : 100
	}, {
		header : "是否有文档",
		dataIndex : "docName",
		renderer : function(v) {
			if (v != null) {
				return '是'
			} else {
				return "否"
			}
		},
		width : 100
	}]);

	// 设置对每行的监听事件
	var sm = new Ext.grid.RowSelectionModel({
		singleSelect : true,
		listeners : {
			rowselect : function(sm, row, rec) {
			}
		}
	});
	// 对应表单中每列字段进行数据保存
	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'message/getReceiveMessageList.action'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : "data.totalCount",
			root : "data.list"
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
			name : "zbbmtxName"
		}, {
			name : "senderName"
		}, {
			name : "receiverName"
		}, {
			name : "statusFlag"
		}, {
			name : "text"
		}, {
			name : "title"
		}, {
			name : "docTypeId"
		}, {
			name : "docName"
		}])
	});
	
	
	ds.load({
		params : {
			startDate : ChangeDateToString(fromDate.getValue()),
			end : ChangeDateToString(toDate.getValue()),
			statusFlag : "1,2",
			start : 0,
			limit : 18
		}
	});
	ds.on('beforeload', function() {
		Ext.apply(this.baseParams, {
			startDate : ChangeDateToString(fromDate.getValue()),
			end : ChangeDateToString(toDate.getValue()),
			statusFlag : "1,2"
		})
	});
	

	// 表单的横头条
	var rtbar = new Ext.Toolbar({
		items : ['消息发送时间', "-", fromDate, "-", '至', "-", toDate, "-", {
			id : "query",
			text : "查询",
			iconCls : 'query',
			handler : function() {
				
				ds.load({
					params : {
						start : 0,
						limit : 18,
						startDate : ChangeDateToString(fromDate.getValue()),
						end : ChangeDateToString(toDate.getValue()),
						statusFlag : "1,2"
					}
				});
			}
		}, "-", {
			id : "lookmessage",
			text : "消息查看",
			iconCls : 'view',
			handler : lookview
		}, "-", {
			id : "sendmessage",
			text : "消息回复",
			iconCls : 'send',
			handler : function() {
				var rec = grid.getSelectionModel().getSelections();
				if (rec.length != 1) {
					Ext.Msg.alert('提示信息', "请选择你要回复的消息！");
					return false;
				} else {
					win.show();
					// alert(Ext.getCmp("zbbmtxName").getValue());
					Ext.getCmp("recive").setVisible(false);
					Ext.getCmp("save").setVisible(true);
					Ext.getCmp("cancer").setVisible(true);
					Ext.getCmp("send").setVisible(true);
					Ext.getCmp("docTypeId").setDisabled(false);
					Ext.get("title").dom.readOnly = false;
					Ext.get("text").dom.readOnly = false;
//					if(rec[0].data.docName == null){
						Ext.getCmp("view").setVisible(false);
//					}else{
//						Ext.getCmp("view").setVisible(true);
//					}
					Ext.get("docName").dom.readOnly = false;
					Ext.get("docTypeId").dom.readOnly = false;
					var _rec = eval('(' + "{data:" + Ext.encode(rec[0].data)
							+ "}" + ')');
					blockForm.getForm().loadRecord(_rec);
					if (Ext.getCmp("zbbmtxName").getValue() == ""
							|| Ext.getCmp("zbbmtxName").getValue() == null) {
						Ext.get("zbbmtxName").dom.value = "管理员";
					}
				}
			}
		}]
	});
	// 设置页面分页
	var bbar = new Ext.PagingToolbar({
		pageSize : 18,
		store : ds,
		displayInfo : true,
		displayMsg : '共 {2} 条',
		emptyMsg : "没有记录"
	})
	// 页面网格
	var grid = new Ext.grid.GridPanel({
		ds : ds,
		sm : sm,
		cm : cm,
		viewConfig : {
			forceFit : true
		},
		tbar : rtbar,
		bbar : bbar
	});

	// 设置网格单行双击事件监控
	grid.on('rowdblclick', function(grid, rowIndex, e) {
		lookview();
	});

	storeCbx3 = new Ext.data.JsonStore({
		root : "root",
		url : "message/getWordTypeList.action",
		fields : ['docTypeId', 'docTypeName']
	})
	storeCbx3.load();

	var formRecord = new Ext.data.Record.create([{
		name : 'zbbmtxName'
	}, {
		name : 'sendById'
	}, {
		name : 'title'
	}, {
		name : 'text'
	}, {
		name : 'docTypeId'
	}, {
		name : 'docName'
	}]);
	var btnView = new Ext.Button({
		id : "view",
		text : '查看',
		iconCls : 'view',
		handler : function() {
			// var dname = Ext.get("dName").dom.value;
			// window.open(uploadUrl + "message/" + dname);
			window.open('power/message/downloadFile.action?messageId='
					+ Ext.get("messageId").dom.value);
		}
	})

	var blockForm = new Ext.FormPanel({
		labelWidth : 80,
		labelAlign : 'center',
		// title : '消息接收',
		frame : true,
		fileUpload : true,
		items : [{
			id : "zbbmtxName",
			xtype : "textfield",
			fieldLabel : '消息发送公司',
			name : 'sysmessage.zbbmtxName',
			anchor : '90%',
			readOnly : true
		}, {
			id : 'messageId',
			xtype : 'hidden'
		}, {
			id : "senderName",
			xtype : "textfield",
			isFormField : true,
			fieldLabel : '消息发送人',
			name : 'senderName',
			anchor : '90%',
			readOnly : true
		}, {
			id : "dName",
			hidden : true
		}, {
			id : "sendById",
			xtype : "hidden",
			isFormField : true,
			name : 'sysmessageemp.receiveById',
			anchor : '90%',
			readOnly : true
		}, {
			id : "title",
			xtype : "textfield",
			fieldLabel : '标题',
			name : 'sysmessage.title',
			anchor : '90%',
			readOnly : false
		}, {
			id : "text",
			xtype : "textarea",
			fieldLabel : '正文',
			name : 'sysmessage.text',
			anchor : '90%',
			readOnly : false

		}, new Ext.form.ComboBox({
			id : 'docTypeId',
			fieldLabel : "消息类型<font color='red'>*</font>",
			emptyText : '请选择文档类型',
			store : storeCbx3,
			displayField : "docTypeName",
			valueField : "docTypeId",
			hiddenName : 'sysmessage.docTypeId',
			allowblank : false,
			mode : 'local',
			triggerAction : 'all',
			readOnly : true,
			anchor : "85%"
		}), {
			layout : "column",
			border : false,
			items : [{
				columnWidth : 0.8,
				colunHeight : 2,
				layout : "form",
				border : false,
				items : [{
					id : "docName",
					xtype : "textfield",
					inputType : 'file',
					fieldLabel : '文档',
					name : 'docName',
					anchor : '90%',
					readOnly : false
				}]
			}, {
				columnWidth : 0.2,
				layout : "form",
				border : false,
				items : [btnView]
			}]
		}],
		reader : new Ext.data.JsonReader(formRecord)
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
	var win = new Ext.Window({
		el : 'window-win',
		width : 400,
		height : 300,
		title : '消息接收',
		closeAction : 'hide',
		modal : true,
		items : [blockForm],
		buttons : [{
			id : 'recive',
			text : '消息回复',
			iconCls : 'save',
			handler : function() {
				if (Ext.getCmp("zbbmtxName").getValue() == "") {
					Ext.get("zbbmtxName").dom.value = "管理员";
				}
				Ext.getCmp("recive").setVisible(false);
				Ext.getCmp("save").setVisible(true);
				Ext.getCmp("cancer").setVisible(true);
				Ext.getCmp("send").setVisible(true);
				Ext.getCmp("view").setVisible(false);
				Ext.getCmp("docTypeId").setDisabled(false);
				Ext.get("title").dom.readOnly = false;
				Ext.get("text").dom.readOnly = false;
				Ext.get("docName").dom.readOnly = false;
				Ext.get("docTypeId").dom.readOnly = false;
			}
		}, {
			text : '保存',
			id : 'save',
			iconCls : 'save',
			handler : function() {
				if (Ext.getCmp('docTypeId').getValue() == null) {
					Ext.Msg.alert("提示", "请选择消息类型");

				} else {
					var url = 'message/saveMessage.action';
					if (blockForm.getForm().isValid()) {
						// if (Ext.get('docName').dom.value != "") {
						// if (checkType(Ext.get('docName').dom.value)) {
						// Ext.Msg.alert('提示信息', '不能上传该类型')
						// return;
						// }
						// if (ShowSize(Ext.getCmp('docName').value) >
						// 200000000) {
						// Ext.Msg.alert('提示信息', '上传文件过大')
						// return;
						// }
						// }

						blockForm.getForm().submit({
							waitMsg : '保存中,请稍后...',
							url : url,
							method : 'post',
							params : {
								filePath : Ext.get('docName').dom.value,
								codes : Ext.get("sysmessageemp.receiveById").dom.value
							},
							success : function(form, action) {
								var msg = eval('('
										+ action.response.responseText + ')');
								Ext.Msg.alert('提示', msg.msg);
//								Ext.get("docName").dom.select();
//								document.selection.clear();
								win.hide();
								ds.reload();
//								parent.send.document.getElementById("query")
//										.click();
							},
							failure : function(form, action) {
								Ext.Msg.alert('错误', "保存失败！");
								win.hide();
							}
						});
					}
				}
			}
		}, {
			text : "发送",
			id : 'send',
			iconCls : 'send',
			handler : function() {
				if (Ext.getCmp('docTypeId').getValue() == null) {
					Ext.Msg.alert("提示", "请选择消息类型");
				} else {
					var url = 'message/sendMessage.action'
					if (blockForm.getForm().isValid()) {
						// if (Ext.get('docName').dom.value != "") {
						// if (checkType(Ext.get('docName').dom.value)) {
						// Ext.Msg.alert('提示信息', '不能上传该类型')
						// return;
						// }
						// if (ShowSize(Ext.getCmp('docName').value) >
						// 200000000) {
						// Ext.Msg.alert('提示信息', '上传文件过大')
						// return;
						// }
						// }
						blockForm.getForm().submit({
							waitMsg : '发送中,请稍后...',
							url : url,
							params : {
								filePath : Ext.get('docName').dom.value,
								codes : Ext.get("sysmessageemp.receiveById").dom.value
							},
							success : function(form, action) {
								var msg = eval('('
										+ action.response.responseText + ')');
								Ext.Msg.alert('提示', msg.msg);
//								 Ext.get("docName").dom.select();
//								 document.selection.clear();
								win.hide();
								ds.reload();
//								parent.send.document.getElementById("query")
//										.click();
							},
							failure : function(form, action) {
								Ext.Msg.alert('错误', "发送失败");
								win.hide();
							}
						});
					}
				}
			}
		}, {
			text : '取消',
			id : 'cancer',
			iconCls : 'cancer',
			handler : function() {
//				 Ext.get("docName").dom.select();
//				 document.selection.clear();
				win.hide();
				blockForm.getForm().reset();
			}
		}]
	});
	// 点击查看在form中显示所列信息
	function lookview() {
		var rec = grid.getSelectionModel().getSelections();
		if (rec.length != 1) {
			Ext.Msg.alert('提示信息', "请选择你要查看的消息！");
			return false;
		} else {
			if (rec[0].data.messageStatus == 1) {
				Ext.Ajax.request({
					url : 'message/watchMessage.action',
					method : 'post',
					params : {
						id : rec[0].data.id
					},
					success : function(result, request) {
						ds.reload();
					},
					failure : function(result, request) {
					}
				})
			}
			win.show();
			if(rec[0].data.docName == null){
				Ext.getCmp("view").setVisible(false);
			}else{
				Ext.getCmp("view").setVisible(true);
			}
			Ext.getCmp("recive").setVisible(true);
			Ext.getCmp("save").setVisible(false);
			Ext.getCmp("cancer").setVisible(true);
			Ext.getCmp("send").setVisible(false);
			Ext.get("title").dom.readOnly = true;
			Ext.get("text").dom.readOnly = true;
			if(rec[0].data.docName == null){
					Ext.getCmp("view").setVisible(false);
				}else{
					Ext.getCmp("view").setVisible(true);
			}
			Ext.get("docName").dom.readOnly = true;
			Ext.get("docTypeId").dom.readOnly = true;
			Ext.getCmp("docTypeId").setDisabled(true);
			var _rec = eval('(' + "{data:" + Ext.encode(rec[0].data) + "}"
					+ ')');
			Ext.get("dName").dom.value = rec[0].data.docName
			blockForm.getForm().loadRecord(_rec)
			if (Ext.getCmp("zbbmtxName").getValue() == ""
					|| Ext.getCmp("zbbmtxName").getValue() == null) {
				Ext.get("zbbmtxName").dom.value = "管理员";
			}
		}
	}
	// 页面布局器面板
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		items : [grid]
	});
})