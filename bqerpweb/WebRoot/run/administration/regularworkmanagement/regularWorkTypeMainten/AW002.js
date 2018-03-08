Ext.QuickTips.init();
Ext.onReady(function() {

	// 隐藏ID
	var hdnId = new Ext.form.Hidden({
				id : "worktype.id",
				name : "worktype.id",
				value : ""
			});
	// 隐藏修改时间
	var hdnUpdateTime = new Ext.form.Hidden({
				id : "worktype.updateTime",
				name : "worktype.updateTime",
				value : ""
			});
	// 工作类别名
	var txtWorktypeName = new Ext.form.TextField({
				id : "worktype.worktypeName",
				readOnly : true,
				fieldLabel : "工作类别名称",
				width : 120
			});
	// 子工作类别名
	var tfSubWorktypeName = new Ext.form.TextField({
				id : "worktype.subWorktypeName",
				fieldLabel : "子工作类别名称<font color='red'>*</font>",
				name : "worktype.subWorktypeName",
				allowBlank : false,
				width : 120,
				maxLength : 20
			});
	// 检索码
	var tfRetrieveCode = new Ext.form.TextField({
		id : "worktype.retrieveCode",
		fieldLabel : "检索码",
		name : "worktype.retrieveCode",
		onlyLetter : true,
		style : "ime-mode:disabled",
		width : 120,
		maxLength : 8
	});

	var fs = new Ext.Panel({
		height : "100%",
		layout : "form",
		style : "padding-top:5px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
		items : [{
					border : false,
					layout : "column",
					items : [{
								columnWidth : 0.5,
								border : false,
								layout : "form",
								items : [hdnId, hdnUpdateTime, txtWorktypeName,
										{
											border : false,
											height : 10
										}, tfSubWorktypeName]
							}, {
								columnWidth : 0.5,
								border : false,
								layout : "form",
								items : [{
											border : false,
											height : 35
										}, tfRetrieveCode]
							}]
				}]
	});

	var fp = new Ext.form.FormPanel({
				id : "form",
				region : "center",
				labelAlign : "right",
				labelWidth : 100,
				frame : true,
				autoHeight : true,
				items : [fs]
			});
	// 保存按钮处理函数
	function save() {
		if(tfSubWorktypeName.getValue() == ""){
			Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, String.format(
							MessageConstants.COM_E_002, "子工作类别名称"));
			return;
		}
		Ext.MessageBox.confirm(MessageConstants.SYS_CONFIRM_MSG,
				MessageConstants.COM_C_001, function(button, text) {
					if (button == "yes") {

						fp.getForm().submit({
							url : "administration/"
									+ (Ext.get("worktype.id").dom.value == ""
											? "addWorktype"
											: "modifyWorktype") + ".action",
							method : "post",
							params : {
								"worktype.worktypeCode" : drpWorkTypeCbx.getValue(),
								"worktype.updateTime" : hdnUpdateTime.getValue()
							},
							success : function(form, action) {
								var result = eval('('
										+ action.response.responseText + ')');
								// 排他
								if (result.msg == 'U') {
									Ext.Msg.alert(Constants.ERROR,
											MessageConstants.COM_I_002);
									return;
								}
								store.baseParams.strWorktypeCode = drpWorkTypeCbx.getValue();
								// 显示成功信息
								Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
										MessageConstants.COM_I_004, function() {
											store.load({
												params : {
													start : 0,
													limit : Constants.PAGE_SIZE,
													strWorktypeCode : drpWorkTypeCbx
															.getValue()
												}
											});
											win.hide();
										});

								grid.getView().refresh();
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
	// 工作类别名
	var drpWorkTypeCbx = new Ext.form.CmbWorkType({
		        value : "01", 
				width : 80
			});
	// 工具栏
	var tbar = new Ext.Toolbar({
				items : ["定期工作类别:", drpWorkTypeCbx, "-", {
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
	// 工作类别名注册选择事件
	drpWorkTypeCbx.on("select", function(combo, record, index) {
		        store.baseParams.strWorktypeCode = this.getValue();
				Ext.Ajax.request({
							url : "administration/workType.action",
							method : "post",
							params : {
								start : 0,
								limit : Constants.PAGE_SIZE,
								strWorktypeCode : this.getValue()
							},
							success : function(result, request) {
								var gridData = eval('(' + result.responseText
										+ ')');
								store.loadData(gridData);
							},
							failure : function(result, request) {
								Ext.Msg.alert(Constants.NOTICE, MessageConstants.UNKNOWN_ERR);
							}
						});
			});
	// 新增按钮处理函数
	function add() {
		win.setTitle("新增定期工作类别");
		fp.getForm().reset();
		txtWorktypeName.setValue(drpWorkTypeCbx.getRawValue());
		win.show();
	}
	// 修改按钮处理函数
	function modify() {
		win.setTitle("修改定期工作类别");
		fp.getForm().reset();
		var selectedRows = grid.getSelectionModel().getSelections();
		if (selectedRows.length < 1) {
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG, MessageConstants.COM_I_001);
			return;
		}
		var lastSelected = selectedRows[selectedRows.length - 1];
		win.show();
		Ext.get("worktype.id").dom.value = lastSelected.get("id") == null
				? ""
				: lastSelected.get("id");
		txtWorktypeName.setValue(drpWorkTypeCbx.getRawValue());
		Ext.get("worktype.subWorktypeName").dom.value = lastSelected
				.get("subWorktypeName") == null ? "" : lastSelected
				.get("subWorktypeName");
		Ext.get("worktype.retrieveCode").dom.value = lastSelected
				.get("retrieveCode") == null ? "" : lastSelected
				.get("retrieveCode");
		Ext.get("worktype.updateTime").dom.value = lastSelected
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
							url : "administration/deleteWorktype.action",
							method : 'post',
							params : {
								"worktype.id" : lastSelected.get("id"),
								"worktype.updateTime" : lastSelected.get("updateTime") == null
										? ""
										: lastSelected.get("updateTime")
							},
							success : function(result, request) {
								store.baseParams.strWorktypeCode = drpWorkTypeCbx.getValue();
								var result = eval('(' + result.responseText + ')');
								// 排他
								if (result.msg == 'U') {
									Ext.Msg.alert(Constants.ERROR,
											MessageConstants.COM_I_002);
									return;
								}
								Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
										MessageConstants.COM_I_005, function() {
											store.load({
												params : {
													start : 0,
													limit : Constants.PAGE_SIZE,
													strWorktypeCode : drpWorkTypeCbx.getValue()
												}
											});
										});
							},
							failure : function(result, request) {
								Ext.Msg.alert(Constants.ERROR, MessageConstants.COM_I_002);
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
				header : "工作类别名称",
				dataIndex : "worktypeName",
				align : "left"
			}, {
				header : "子工作类别编码",
				dataIndex : "subWorktypeCode",
				align : "left"
			}, {
				header : "子工作类别名称",
				dataIndex : "subWorktypeName",
				align : "left"
			}, {
				header : "检索码",
				dataIndex : "retrieveCode",
				align : "left"
			}]);
	cm.defaultSortable = true;
	// grid中的数据
	var WorkType = Ext.data.Record.create([{
				name : "id"
			}, {
				name : "updateTime"
			}, {
				name : "worktypeName"
			}, {
				name : "subWorktypeCode"
			}, {
				name : "subWorktypeName"
			}, {
				name : "retrieveCode"
			}]);
	// grid中的store
	var store = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : "administration/workType.action"
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : "totalCount",
							root : "list"
						}, WorkType)
			});
	store.baseParams = ({
		strWorktypeCode : drpWorkTypeCbx.getValue()
	});
	store.load({
				params : {
					start : 0,
					limit : Constants.PAGE_SIZE,
					strWorktypeCode : drpWorkTypeCbx.getValue()
				}
			});
	// 底部工具栏
	var bbar = new Ext.PagingToolbar({
				pageSize : Constants.PAGE_SIZE,
				store : store,
				displayInfo : true,
				displayMsg : MessageConstants.DISPLAY_MSG,
				emptyMsg : MessageConstants.EMPTY_MSG
			})
	// grid主体
	var grid = new Ext.grid.GridPanel({
				region : "center",
				sm : sm,
				layout : "fit",
				colModel : cm,
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