Ext.QuickTips.init();
Ext.onReady(function() {

	// 工作类别名
	var txtWorkType = new Ext.form.TextField({
				width : 80,
				readOnly : true,
				name : "tfWorkType",
				id : "tfWorkType"
			});
	// 取得用户工作类别名
	Ext.Ajax.request({
				url : "administration/userWorktypeName.action",
				method : "post",
				success : function(result, request) {
					var data = eval("(" + result.responseText + ")");
					txtWorkType.setValue(data.tfWorkType);
					if(data.tfWorkType == ""){
						Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
						MessageConstants.AW006_I_001,function(){
							        var parentTab = parent.Ext.getCmp("mainTabPanel");
							        parentTab.remove(parentTab.activeTab);
						        }
						    );
						}
				},
				failure : function(result, request){}
			});
	// 隐藏ID
	var hdnId = new Ext.form.Hidden({
				id : "dutyType.id",
				name : "dutyType.id",
				value : ""
			});
	// 隐藏修改时间
	var hdnUpdateTime = new Ext.form.Hidden({
				id : "dutyType.updateTime",
				name : "dutyType.updateTime",
				value : ""
			});
	// 工作类别名
	var txtWorkTypeName = new Ext.form.TextField({
				id : "workTypeName",
				fieldLabel : "工作类别",
				readOnly : true,
				width : 120
			});
	// 开始时间
	var txtStartTime = new Ext.form.TextField({
				id : 'formStartTime',
				fieldLabel : "开始时间<font color='red'>*</font>",
				isFormField : true,
				width : 120,
				style : 'cursor:pointer',
				readOnly : true,
				listeners : {
					focus : function() {
						WdatePicker({
							        isShowClear : false,
									startDate : '%y-%M-%d 00:00',
									dateFmt : 'yyyy-MM-dd HH:mm'
								});
					}
				}
			});
	var hdnfStartTime = new Ext.form.Hidden({
	    id : "hidfStartTime",
	    name : 'dutyType.startTime'
	})
	var hdnfEndTime = new Ext.form.Hidden({
	    id : "hidfEndTime",
	    name : 'dutyType.endTime'
	})
	// 值别名称
	var txtDutyTypeName = new Ext.form.TextField({
				fieldLabel : "值别名称<font color='red'>*</font>",
				id : "dutyType.dutyTypeName",
				name : "dutyType.dutyTypeName",
				allowBlank : false,
				width : 120,
				maxLength : 20
			});
	// 结束时间
	var txtEndTime = new Ext.form.TextField({
				id : 'formEndTime',
				fieldLabel : "结束时间<font color='red'>*</font>",
				isFormField : true,
				width : 120,
				style : 'cursor:pointer',
				readOnly : true,
				listeners : {
					focus : function() {
						WdatePicker({
							        isShowClear : false,
									startDate : '%y-%M-%d 00:00',
									dateFmt : 'yyyy-MM-dd HH:mm'
								});
					}
				}
			});

	var fs = new Ext.Panel({
		height : "100%",
		layout : "form",
		style : "padding-top:5px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
		items : [{
					border : false,
					layout : "column",
					anchor : '100%',
					items : [{
								columnWidth : 0.5,
								border : false,
								layout : "form",
								items : [hdnId, hdnUpdateTime,hdnfStartTime,hdnfEndTime, txtWorkTypeName, txtDutyTypeName]
							}, {
								columnWidth : 0.5,
								border : false,
								layout : "form",
								items : [txtStartTime, txtEndTime]
							}]
				}]
	});

	var fp = new Ext.form.FormPanel({
				id : "form",
				region : "center",
				labelAlign : "right",
				labelWidth : 70,
				frame : true,
				autoHeight : true,
				items : [fs]
			});
	// 保存按钮处理函数
	function save() {
		// 值别名称不能为空
		if (txtDutyTypeName.getValue() == null
				|| txtDutyTypeName.getValue() == "") {
			txtDutyTypeName.focus();
			Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, String.format(
							MessageConstants.COM_E_002, "值别名称"));
			return;
		}
		// 开始时间必须大于结束时间
		if (txtStartTime.getValue() != "" && txtEndTime.getValue() != "") {
			if (txtStartTime.getValue() > txtEndTime.getValue()) {
				Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, String.format(
								MessageConstants.COM_E_009, "开始时间", "结束时间"));
				return;
			}
		}
		Ext.MessageBox.confirm(MessageConstants.SYS_CONFIRM_MSG,
				MessageConstants.COM_C_001, function(button, text) {
					if (button == "yes") {
						if (Ext.get("formStartTime").dom.value != "") {
							hdnfStartTime.setValue(txtStartTime.getValue()
									+ ':00');
						}
						if (Ext.get("formEndTime").dom.value != "") {
							hdnfEndTime.setValue(txtEndTime.getValue() + ':00');
						}
						fp.getForm().submit({
							url : "administration/"
									+ (Ext.get("dutyType.id").dom.value == ""
											? "addDutyType"
											: "modifyDutyType") + ".action",
							methos : "post",
							success : function(form, action) {
                                var result = eval('('
										+ action.response.responseText + ')');
								if ((result.msg != null)
										&& (result.msg == Constants.SQL_FAILURE)) {
									Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
											MessageConstants.COM_E_014);
									return;
								}
								Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
										MessageConstants.COM_I_004, function() {
											win.hide();
											store.load({
														params : {
															start : 0,
															limit : 18
														}
													});
										});

							},
							failure : function(form, action) {}
						});
					}
				});
	}
	// 取消按钮处理函数
	function cancel() {
		Ext.MessageBox.confirm(MessageConstants.SYS_CONFIRM_MSG,
				MessageConstants.COM_C_005, function(button, text) {
					if (button == "yes") {
						win.hide();
					}
				});
	}
	// 编辑窗口
	var win = new Ext.Window({
		        buttonAlign : "center",
				modal : true,
				width : 500,
				autoHeight : true,
				closeAction : "hide",
				resizable : false,
				items : [fp],
				buttons : [{
							text : Constants.BTN_SAVE,
							iconCls : Constants.CLS_SAVE,
							handler : save
						}, {
							text : Constants.BTN_CANCEL,
							iconCls : Constants.CLS_CANCEL,
							handler : cancel
						}]
			});

	// 工具栏
	var tbar = new Ext.Toolbar({
				items : ["工作类别:", txtWorkType, "-", {
							id : "add",
							text : Constants.BTN_ADD,
							iconCls : Constants.CLS_ADD,
							handler : add
						}, {
							id : "modify",
							text : Constants.BTN_UPDATE,
							iconCls : Constants.CLS_UPDATE,
							handler : modify
						}, {
							id : "deleteIt",
							text : Constants.BTN_DELETE,
							iconCls : Constants.CLS_DELETE,
							handler : deleteIt
						}]
			});
	// 新增按钮处理函数
	function add() {
		win.setTitle("新增值别");
		fp.getForm().reset();
		txtStartTime.setValue(new Date().format("Y-m-d H:i"));
		txtEndTime.setValue(new Date().format("Y-m-d H:i"));
		win.show();
		txtWorkTypeName.setValue(txtWorkType.getValue());
	}
	// 修改按钮处理函数
	function modify() {
		win.setTitle("修改值别");
		fp.getForm().reset();
		txtWorkTypeName.setValue(txtWorkType.getValue());
		var selectedRows = grid.getSelectionModel().getSelections();
		if (selectedRows.length < 1) {
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG, MessageConstants.COM_I_001);
			return;
		}
		var lastSelected = selectedRows[selectedRows.length - 1];
		win.show();
		Ext.get("dutyType.id").dom.value = lastSelected.get("id") == null
				? ""
				: lastSelected.get("id");
		Ext.get("workTypeName").dom.value = txtWorkTypeName.getValue() == null
				? ""
				: txtWorkTypeName.getValue();
		Ext.get("dutyType.dutyTypeName").dom.value = lastSelected
				.get("dutyTypeName") == null ? "" : lastSelected
				.get("dutyTypeName");
		Ext.get("formStartTime").dom.value = lastSelected.get("startTime") == null
				? ""
				: renderDate(lastSelected.get("startTime"));
		Ext.get("formEndTime").dom.value = lastSelected.get("endTime") == null
				? ""
				: renderDate(lastSelected.get("endTime"));
		Ext.get("dutyType.updateTime").dom.value = lastSelected
				.get("updateTime") == null ? "" : lastSelected
				.get("updateTime");
	}
	// 删除按钮处理函数
	function deleteIt() {
		var selectedRows = grid.getSelectionModel().getSelections();
		if (selectedRows.length < 1) {
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
					MessageConstants.COM_I_001);
			return;
		}
		var lastSelected = selectedRows[selectedRows.length - 1];
		Ext.MessageBox.confirm(MessageConstants.SYS_CONFIRM_MSG,
				MessageConstants.COM_C_002, function(button, text) {
					if (button == "yes") {
						Ext.Ajax.request({
							url : "administration/deleteDutyType.action",
							method : 'post',
							params : {
								"dutyType.id" : lastSelected.get("id")
							},
							success : function(result, request) {
								var data = eval("(" + result.responseText + ")");
								if ((result.msg != null)
										&& (result.msg == Constants.SQL_FAILURE)) {
									Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
											MessageConstants.COM_E_014);
									return;
								}
								Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
										MessageConstants.COM_I_005, function() {
											store.load({
														params : {
															start : 0,
															limit : 18
														}
													});
										});
							},
							failure : function(result, request) {
								Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
										"删除失败!");
							}
						});
					}
				});
	}
	// grid选择模式设为单行选择模式
    var sm = new Ext.grid.RowSelectionModel({
	       singleSelect : true
	});
	// grid中的列定义
	var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
						header : "行号",
						width : 35
					}), {
				header : "值别编码",
				dataIndex : "dutyType",
				align : "left"
			}, {
				header : "值别名称",
				dataIndex : "dutyTypeName",
				align : "left"
			}, {
				header : "开始时间",
				dataIndex : "startTime",
				align : "left",
				renderer : renderDate
			}, {
				header : "结束时间",
				dataIndex : "endTime",
				align : "left",
				renderer : renderDate
			}]);
	cm.defaultSortable = true;
	// 去掉时间中T和秒
	function renderDate(value) {
		if (!value)
			return "";
		var reDate = /\d{4}\-\d{2}\-\d{2}/gi;
		var reTime = /\d{2}:\d{2}/gi;

		var strDate = value.match(reDate);
		var strTime = value.match(reTime);
		if (!strDate)
			return "";
		return strTime ? strDate + " " + strTime : strDate;
	}
	// grid中的数据
	var DutyType = Ext.data.Record.create([{
				name : "id"
			}, {
				name : "updateTime"
			}, {
				name : "dutyType"
			}, {
				name : "dutyTypeName"
			}, {
				name : "startTime"
			}, {
				name : "endTime"
			}]);
	// grid中的store
	var store = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "administration/dutyTypeQuery.action"
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : "totalCount",
							root : "list"
						}, DutyType)
			});
	store.load({
				params : {
					start : 0,
					limit : 18
				}
			});
	// 底部工具栏
	var bbar = new Ext.PagingToolbar({
				pageSize : 18,
				store : store,
				displayInfo : true,
				displayMsg : MessageConstants.DISPLAY_MSG,
				emptyMsg : MessageConstants.EMPTY_MSG
			})
	// grid主体
	var grid = new Ext.grid.GridPanel({
				region : "center",
				layout : "fit",
				colModel : cm,
				sm : sm,
				tbar : tbar,
				bbar : bbar,
				enableColumnMove : false,
				store : store
			});
	// 注册双击grid事件
	grid.on("rowdblclick", gridDb);

	function gridDb() {
		Ext.get("modify").dom.click();
	}
	// 设定布局器及面板
	var layout = new Ext.Viewport({
				layout : "border",
				border : false,
				enableTabScroll : true,
				items : [grid]
			});
})