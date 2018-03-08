// 值班日志登记
// author:sufeiyu

Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.QuickTips.init();
Ext.onReady(function() {
	// 记事弹出窗口标题
	var strRecord;
	// 人员弹出窗口标题
	var strPerson;
	var today;

	// 值班记事--------------------------------------------------
	// 定义选择列
	var smDuty = new Ext.grid.RowSelectionModel({
				singleSelect : true
			});

	var cmDuty = new Ext.grid.ColumnModel([
			// 自动行号
			new Ext.grid.RowNumberer({
						header : "行号",
						width : 31
					}), {
				header : "序号",
				hidden : true,
				dataIndex : 'noteId'
			}, {
				header : "登记时间",
				sortable : true,
				dataIndex : 'regTime'
			}, {
				header : "登记内容",
				sortable : true,
				dataIndex : 'regText'
			}, {
				header : "工作类别Code",
				hidden : true,
				dataIndex : 'worktypeCode'
			}, {
				header : "工作类别Code",
				hidden : true,
				dataIndex : 'subWorktypeCode'
			}, {
				header : "工作类别",
				sortable : true,
				dataIndex : 'subWorktypeName'
			}, {
				header : "值别Code",
				hidden : true,
				dataIndex : 'dutyType'
			}, {
				header : "值别",
				sortable : true,
				dataIndex : 'dutyTypeName'
			}, {
				header : "上次修改时间",
				hidden : true,
				dataIndex : 'updateTime'
			}, {
				header : "值班人",
				sortable : true,
				dataIndex : 'dutyman'
			}])

	// 数据源--------------------------------
	var recordDuty = Ext.data.Record.create([{
				name : 'noteId'
			}, {
				name : 'regTime'
			}, {
				name : 'regText'
			}, {
				name : 'worktypeCode'
			}, {
				name : 'subWorktypeCode'
			}, {
				name : 'subWorktypeName'
			}, {
				name : 'dutyType'
			}, {
				name : 'dutyTypeName'
			}, {
				name : 'updateTime'
			}, {
				name : 'dutyman'
			}]);

	// 定义获取数据源
	var proxyDuty = new Ext.data.HttpProxy({
				url : 'administration/getRecord.action'
			});

	// 定义格式化数据
	var readerDuty = new Ext.data.JsonReader({
				root : "list",
				totalProperty : "totalCount"
			}, recordDuty);

	// 定义封装缓存数据的对象
	var storeDuty = new Ext.data.Store({
				// 访问的对象
				proxy : proxyDuty,
				// 处理数据的对象
				reader : readerDuty
			});

	storeDuty.load({
				params : {
					start : 0,
					limit : Constants.PAGE_SIZE
				}
			});

	// --gridpanel显示格式定义-----开始-------------------
	var btnAddRecord = new Ext.Button({
				text : Constants.BTN_ADD,
				iconCls : Constants.CLS_ADD,
				handler : addRecord
			});

	var btnUpdateRecord = new Ext.Button({
				text : Constants.BTN_UPDATE,
				iconCls : Constants.CLS_UPDATE,
				handler : updateRecord
			});

	var btnDeleteRecord = new Ext.Button({
				text : Constants.BTN_DELETE,
				iconCls : Constants.CLS_DELETE,
				handler : deleteRecord
			});

	storeDuty.on('load', function() {
//				if (storeDuty.getCount() > 0) {
//					btnUpdateRecord.setDisabled(false);
//					btnDeleteRecord.setDisabled(false);
//				} else {
//					btnUpdateRecord.setDisabled(true);
//					btnDeleteRecord.setDisabled(true);
//				}
			});

	var gridDuty = new Ext.grid.GridPanel({
				layout : 'fit',
				store : storeDuty,
				cm : cmDuty,
				sm : smDuty,
				autoScroll : true,
				enableColumnMove : false,
				autoSizeColumns : true,
				viewConfig : {
					forceFit : false
				},
				// 头部工具栏
				tbar : [btnAddRecord, btnUpdateRecord, btnDeleteRecord],

				// 分页
				bbar : new Ext.PagingToolbar({
							pageSize : Constants.PAGE_SIZE,
							store : storeDuty,
							displayInfo : true,
							displayMsg : MessageConstants.DISPLAY_MSG,
							emptyMsg : MessageConstants.EMPTY_MSG
						})
			});
	// --gridpanel显示格式定义-----结束--------------------

	// 注册双击事件
	gridDuty.on("rowdblclick", updateRecord);

	// 弹出窗口
	// 记事ID
	var hdId = {
		id : "noteId",
		name : "onduty.id",
		xtype : 'hidden',
		readOnly : true,
		hidden : true,
		value : '0'
	}

	// 工作类别
	var hdWorktypeCode = {
		id : "worktypeCode",
		name : "onduty.worktypeCode",
		xtype : 'hidden',
		readOnly : true,
		hidden : true,
		value : '0'
	}

	// 上次修改时间
	var hdUpdateTime = {
		id : "updateTime",
		name : "updateTime",
		xtype : 'hidden',
		readOnly : true,
		hidden : true,
		value : '0'
	}

	// 子工作类别
	var hdSubWorktypeCode = {
		id : "subWorktypeCode",
		name : "onduty.subWorktypeCode",
		xtype : 'hidden',
		readOnly : true,
		hidden : true,
		value : '0'
	}

	// 子工作类别
	var drpWorktypeName = new Ext.form.CmbSubWorkType({
				id : 'subWorktypeName',
				name : 'subWorktypeName',
				fieldLabel : '工作类别<font color ="red">*</font>',
				allowBlank : false,
				width : 120,
				listeners : {
					"select" : function() {
						Ext.get("subWorktypeCode").dom.value = drpWorktypeName
								.getValue();
					}
				}
			});
	drpWorktypeName.store.load();

	// 值别
	var hdDutyType = {
		id : "dutyType",
		name : "onduty.dutyType",
		xtype : 'hidden',
		readOnly : true,
		hidden : true,
		value : '0'
	}

	// 值别
	var drpDutytype = new Ext.form.CmbDuty({
				id : 'dutyTypeName',
				name : 'dutyTypeName',
				fieldLabel : '值别',
				width : 120,
				listeners : {
					"select" : function() {
						Ext.get("dutyType").dom.value = drpDutytype.getValue();
					}
				}
			});
	drpDutytype.store.load();

	// 第一行
	var fldSetFirstLine = new Ext.form.FieldSet({
				border : false,
				height : 25,
				layout : 'column',
				style : "padding-bottom:0px;border:0px",
				anchor : '100%',
				items : [{
							columnWidth : 0.5,
							border : false,
							layout : 'form',
							items : [drpWorktypeName]
						}, {
							columnWidth : 0.5,
							border : false,
							layout : 'form',
							items : [drpDutytype]
						}]
			})

	// 值班人
	var txtDutyMan = new Ext.form.TextField({
				id : 'dutyman',
				name : 'onduty.dutyman',
				fieldLabel : '值班人<font color ="red">*</font>',
				allowBlank : false,
				isFormField : true,
				width : 120,
				height : 20,
				maxLength : 10
			})

	// 值班时间
	var txtRegTime = new Ext.form.TextField({
				id : 'regTime',
				name : 'onduty.regTime',
				fieldLabel : '登记时间',
				style : 'cursor:pointer',
				readOnly : true,
				width : 120,
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd HH:mm',
									alwaysUseStartDate : false,
									onpicked : function() {
										txtRegTime.clearInvalid();
									},
									onclearing : function() {
										txtRegTime.markInvalid();
									}
								});
					}
				}
			});

	// 第二行
	var fldSetSecondLine = new Ext.form.FieldSet({
				border : false,
				height : 25,
				layout : 'column',
				style : "padding-bottom:0px;border:0px",
				anchor : '100%',
				items : [{
							columnWidth : 0.5,
							border : false,
							layout : 'form',
							items : [txtDutyMan]
						}, {
							columnWidth : 0.5,
							border : false,
							layout : 'form',
							items : [txtRegTime]
						}]
			})

	// 值班内容
	var txaRecord = new Ext.form.TextArea({
				fieldLabel : '登记内容',
				id : 'regText',
				name : 'onduty.regText',
				isFormField : true,
				maxLength : 700,
				width : 345,
				height : 130,
				name : 'onduty.regText'
			});

	// 第三行
	var fldSetThirdLine = new Ext.form.FieldSet({
				border : false,
				height : 130,
				width : 500,
				layout : 'column',
				style : "padding-bottom:0px;border:0px",
				anchor : '100%',
				items : [{
							border : false,
							layout : 'form',
							items : [txaRecord]
						}]
			})

	// Panel
	var formpanelDuty = new Ext.FormPanel({
				frame : true,
				labelAlign : 'right',
				labelWidth : 65,
				style : "padding-bottom:0px;border:0px",
				items : [fldSetFirstLine, fldSetSecondLine, fldSetThirdLine,
						hdWorktypeCode, hdSubWorktypeCode, hdDutyType,
						hdUpdateTime, hdId]
			})

	// 保存按钮
	var btnConfirmDuty = new Ext.Button({
				text : Constants.BTN_SAVE,
				iconCls : Constants.CLS_SAVE,
				buttonAlign : 'center',
				handler : confirmRecord
			})

	// 取消按钮
	var btnCancelDuty = new Ext.Button({
				text : Constants.BTN_CANCEL,
				iconCls : Constants.CLS_CANCEL,
				buttonAlign : 'center',
				handler : cancelRecord
			})

	// 弹出窗口
	var winDuty = new Ext.Window({
				width : 500,
				height : 300,
				modal : true,
				buttonAlign : 'center',
				items : [formpanelDuty],
				layout : 'fit',
				closeAction : 'hide',
				resizable : false,
				buttons : [btnConfirmDuty, btnCancelDuty]
			});

	// 记事结束--------------------------------------------------

	// --------------------------TAB2----------------------------

	// 值班人员--------------------------------------------------
	// 定义选择列
	var smPerson = new Ext.grid.RowSelectionModel({
				singleSelect : true
			});

	var cmPerson = new Ext.grid.ColumnModel([
			// 自动行号
			new Ext.grid.RowNumberer({
						header : "行号",
						width : 31
					}), {
				header : "序号",
				hidden : true,
				dataIndex : 'personId'
			}, {
				header : "值班人",
				sortable : true,
				width : 70,
				dataIndex : 'dutyman'
			}, {
				header : "值班时间",
				sortable : true,
				dataIndex : 'dutytime'
			}, {
				header : "工作类别Code",
				hidden : true,
				dataIndex : 'worktypeCode'
			}, {
				header : "子工作类别Code",
				hidden : true,
				dataIndex : 'subWorktypeCode'
			}, {
				header : "工作类别",
				sortable : true,
				dataIndex : 'subWorktypeName'
			}, {
				header : "岗位名称",
				sortable : true,
				dataIndex : 'position'
			}, {
				header : "值别Code",
				hidden : true,
				dataIndex : 'dutytype'
			}, {
				header : "值别",
				sortable : true,
				dataIndex : 'dutytypeName'
			}, {
				header : "替班人员",
				sortable : true,
				width : 70,
				dataIndex : 'replaceman'
			}, {
				header : "缺勤人员",
				sortable : true,
				width : 70,
				dataIndex : 'leaveman'
			}, {
				header : "修改时间",
				hidden : true,
				dataIndex : 'updateTime'
			}, {
				header : "缺勤原因",
				sortable : true,
				dataIndex : 'reason'
			}])

	// 数据源--------------------------------
	var recordPerson = Ext.data.Record.create([{
				name : 'personId'
			}, {
				name : 'dutytime'
			}, {
				name : 'worktypeCode'
			}, {
				name : 'subWorktypeCode'
			}, {
				name : 'subWorktypeName'
			}, {
				name : 'position'
			}, {
				name : 'dutytype'
			}, {
				name : 'dutytypeName'
			}, {
				name : 'replaceman'
			}, {
				name : 'leaveman'
			}, {
				name : 'reason'
			}, {
				name : 'updateTime'
			}, {
				name : 'dutyman'
			}]);

	// 定义获取数据源
	var proxyPerson = new Ext.data.HttpProxy({
				url : 'administration/getPerson.action'
			});

	// 定义格式化数据
	var readerPerson = new Ext.data.JsonReader({
				root : "list",
				totalProperty : "totalCount"
			}, recordPerson);

	// 定义封装缓存数据的对象
	var storePerson = new Ext.data.Store({
				// 访问的对象
				proxy : proxyPerson,
				// 处理数据的对象
				reader : readerPerson
			});

	storePerson.load({
				params : {
					start : 0,
					limit : Constants.PAGE_SIZE
				}
			});

	// --gridpanel显示格式定义-----开始-------------------
	var btnAddPerson = new Ext.Button({
				text : Constants.BTN_ADD,
				iconCls : Constants.CLS_ADD,
				handler : addPerson
			});

	var btnUpdatePerson = new Ext.Button({
				text : Constants.BTN_UPDATE,
				iconCls : Constants.CLS_UPDATE,
				handler : updatePerson
			});

	var btnDeletePerson = new Ext.Button({
				text : Constants.BTN_DELETE,
				iconCls : Constants.CLS_DELETE,
				handler : deletePerson
			});

	storePerson.on('load', function() {
//				if (storePerson.getCount() > 0) {
//					btnUpdatePerson.setDisabled(false);
//					btnDeletePerson.setDisabled(false);
//				} else {
//					btnUpdatePerson.setDisabled(true);
//					btnDeletePerson.setDisabled(true);
//				}
			});

	var gridPerson = new Ext.grid.GridPanel({
				layout : 'fit',
				store : storePerson,
				cm : cmPerson,
				sm : smPerson,
				autoScroll : true,
				enableColumnMove : false,
				autoSizeColumns : true,
				viewConfig : {
					forceFit : false
				},
				// 头部工具栏
				tbar : [btnAddPerson, btnUpdatePerson, btnDeletePerson],

				// 分页
				bbar : new Ext.PagingToolbar({
							pageSize : Constants.PAGE_SIZE,
							store : storePerson,
							displayInfo : true,
							displayMsg : MessageConstants.DISPLAY_MSG,
							emptyMsg : MessageConstants.EMPTY_MSG
						})
			});
	// --gridpanel显示格式定义-----结束--------------------

	// 注册双击事件
	gridPerson.on("rowdblclick", updatePerson);

	// 弹出窗口
	// 记事ID
	var hdPersonId = {
		id : "personId",
		name : "dutyman.id",
		xtype : 'hidden',
		readOnly : true,
		hidden : true,
		value : '0'
	}

	// 工作类别
	var hdWorktypeCodePerson = {
		id : 'worktypeCodePerson',
		name : "dutyman.worktypeCode",
		xtype : 'hidden',
		readOnly : true,
		hidden : true,
		value : '0'
	}

	// 上次修改时间
	var hdUpdateTimePerson = {
		id : "updateTimePerson",
		name : "updateTime",
		xtype : 'hidden',
		readOnly : true,
		hidden : true,
		value : '0'
	}

	// 子工作类别
	var hdSubWorktypeCodePerson = {
		id : "subWorktypeCodePerson",
		name : "dutyman.subWorktypeCode",
		xtype : 'hidden',
		readOnly : true,
		hidden : true,
		value : '0'
	}

	// 子工作类别
	var drpWorktypeNamePerson = new Ext.form.CmbSubWorkType({
		id : 'subWorktypeNamePerson',
		name : 'subWorktypeNamePerson',
		allowBlank : false,
		fieldLabel : '工作类别<font color ="red">*</font>',
		width : 120,
		listeners : {
			"select" : function() {
				Ext.get("subWorktypeCodePerson").dom.value = drpWorktypeNamePerson
						.getValue();
			}
		}
	});
	drpWorktypeNamePerson.store.load();

	// 第一行
	var fldSetFirstLinePerson = new Ext.form.FieldSet({
				border : false,
				height : 25,
				layout : 'column',
				style : "padding-bottom:0px;border:0px",
				anchor : '100%',
				items : [{
							columnWidth : 0.5,
							border : false,
							layout : 'form',
							items : [drpWorktypeNamePerson]
						}]
			})

	// 值别
	var hdDutyTypePerson = {
		id : "dutyTypePerson",
		name : "dutyman.dutytype",
		xtype : 'hidden',
		readOnly : true,
		hidden : true,
		value : '0'
	}

	// 值别
	var drpDutytypePerson = new Ext.form.CmbDuty({
				id : 'dutyTypeNamePerson',
				name : 'dutyTypeNamePerson',
				allowBlank : false,
				fieldLabel : '值别<font color ="red">*</font>',
				width : 120,
				listeners : {
					"select" : function() {
						Ext.get("dutyTypePerson").dom.value = drpDutytypePerson
								.getValue();
					}
				}
			});
	drpDutytypePerson.store.load();

	// 值班时间
	var txtDutytime = new Ext.form.TextField({
				id : 'dutytime',
				name : 'dutyman.dutytime',
				fieldLabel : '值班日期',
				style : 'cursor:pointer',
				readOnly : true,
				width : 120,
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd HH:mm',
									alwaysUseStartDate : false,
									onpicked : function() {
										txtDutytime.clearInvalid();
									},
									onclearing : function() {
										txtDutytime.markInvalid();
									}
								});
					}
				}
			});

	// 第二行
	var fldSetSecondLinePerson = new Ext.form.FieldSet({
				border : false,
				height : 25,
				layout : 'column',
				style : "padding-bottom:0px;border:0px",
				anchor : '100%',
				items : [{
							columnWidth : 0.5,
							border : false,
							layout : 'form',
							items : [drpDutytypePerson]
						}, {
							columnWidth : 0.5,
							border : false,
							layout : 'form',
							items : [txtDutytime]
						}]
			})

	// 值班人
	var txtDutyManPerson = new Ext.form.TextField({
				id : 'dutymanPerson',
				name : 'dutyman.dutyman',
				fieldLabel : '值班人<font color ="red">*</font>',
				allowBlank : false,
				isFormField : true,
				width : 120,
				height : 20,
				maxLength : 10
			})

	// 岗位名称
	var txtPosition = new Ext.form.TextField({
				id : 'position',
				name : 'dutyman.position',
				fieldLabel : '岗位名称',
				isFormField : true,
				width : 120,
				height : 20,
				maxLength : 15
			})

	// 第三行
	var fldSetThirdLinePerson = new Ext.form.FieldSet({
				border : false,
				height : 25,
				layout : 'column',
				style : "padding-bottom:0px;border:0px",
				anchor : '100%',
				items : [{
							columnWidth : 0.5,
							border : false,
							layout : 'form',
							items : [txtDutyManPerson]
						}, {
							columnWidth : 0.5,
							border : false,
							layout : 'form',
							items : [txtPosition]
						}]
			})

	// 替班人
	var txtReplaceman = new Ext.form.TextField({
				id : 'replaceman',
				name : 'dutyman.replaceman',
				fieldLabel : '替班人员',
				isFormField : true,
				width : 120,
				height : 20,
				maxLength : 10
			})

	// 缺勤人
	var txtLeaveman = new Ext.form.TextField({
				id : 'leaveman',
				name : 'dutyman.leaveman',
				fieldLabel : '缺勤人员',
				isFormField : true,
				width : 120,
				height : 20,
				maxLength : 10
			})

	// 第四行
	var fldSetFouthLinePerson = new Ext.form.FieldSet({
				border : false,
				height : 25,
				layout : 'column',
				style : "padding-bottom:0px;border:0px",
				anchor : '100%',
				items : [{
							columnWidth : 0.5,
							border : false,
							layout : 'form',
							items : [txtReplaceman]
						}, {
							columnWidth : 0.5,
							border : false,
							layout : 'form',
							items : [txtLeaveman]
						}]
			})

	// 缺勤原因
	var txaReason = new Ext.form.TextArea({
				fieldLabel : '缺勤原因',
				id : 'reason',
				isFormField : true,
				maxLength : 50,
				width : 345,
				height : 35,
				name : 'dutyman.reason'
			});

	// 第五行
	var fldSetFifthLinePerson = new Ext.form.FieldSet({
				border : false,
				height : 130,
				width : 500,
				layout : 'column',
				style : "padding-bottom:0px;border:0px",
				anchor : '100%',
				items : [{
							border : false,
							layout : 'form',
							items : [txaReason]
						}]
			})

	// Panel
	var formpanelPerson = new Ext.FormPanel({
				frame : true,
				labelAlign : 'right',
				labelWidth : 65,
				style : "padding-bottom:0px;border:0px",
				items : [fldSetFirstLinePerson, fldSetSecondLinePerson,
						fldSetThirdLinePerson, fldSetFouthLinePerson,
						fldSetFifthLinePerson, hdPersonId,
						hdWorktypeCodePerson, hdUpdateTimePerson,
						hdSubWorktypeCodePerson, hdDutyTypePerson]
			})

	// 保存按钮
	var btnConfirmPerson = new Ext.Button({
				text : Constants.BTN_SAVE,
				iconCls : Constants.CLS_SAVE,
				buttonAlign : 'center',
				handler : confirmPerson
			})

	// 取消按钮
	var btnCancelPerson = new Ext.Button({
				text : Constants.BTN_CANCEL,
				iconCls : Constants.CLS_CANCEL,
				buttonAlign : 'center',
				handler : cancelPerson
			})

	// 弹出窗口
	var winPerson = new Ext.Window({
				width : 500,
				height : 270,
				modal : true,
				buttonAlign : 'center',
				items : [formpanelPerson],
				layout : 'fit',
				closeAction : 'hide',
				resizable : false,
				buttons : [btnConfirmPerson, btnCancelPerson]
			});

	// 人员结束--------------------------------------------------
	// 值班记事 Panel
	var tabRecord = new Ext.Panel({
				title : '值班记事',
				layout : 'fit',
				border : false,
				items : [gridDuty]
			})
	// 值班人员 Panel
	var tabPerson = new Ext.Panel({
				title : '值班人员',
				layout : 'fit',
				border : false,
				items : [gridPerson]
			})

	var tabpanel = new Ext.TabPanel({
				activeTab : 0,
				tabPosition : 'bottom',
				border : false,
				items : [tabRecord, tabPerson]
			})

	// **********主画面********** //
	new Ext.Viewport({
				enableTabScroll : true,
				layout : "fit",
				items : [tabpanel]
			});

	// ************事件处理********************

	// ---------值班记事事件---------------------
	// 增加
	function addRecord() {
		strRecord = 'add';
		formpanelDuty.getForm().reset();
		winDuty.setTitle("新增值班记事");
		dateInit();
		txtRegTime.setValue(today);
		winDuty.show();
		if (drpWorktypeName.store.getCount() == 0){
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG, String.format(
											MessageConstants.COM_I_013,
											"工作类别"), function(){
											    winDuty.hide();
											});
											
					return;
		}
	}

	// 修改
	function updateRecord() {
		var rec = gridDuty.getSelectionModel().getSelected();
		if (!rec) {
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
					MessageConstants.COM_I_001);
			return false;
		} else {
			strRecord = "update";
			formpanelDuty.getForm().reset();
			winDuty.setTitle("修改值班记事");
			winDuty.show();
			formpanelDuty.getForm().loadRecord(rec);
			drpWorktypeName.setValue(rec.data.subWorktypeCode);
			if(drpWorktypeName.getValue() == Ext.get("subWorktypeName").dom.value) {
				Ext.get("subWorktypeCode").dom.value = "";
			    Ext.get("subWorktypeName").dom.value = "";
			}
			drpDutytype.setValue(rec.data.dutyType);
			if(drpDutytype.getValue() == Ext.get("dutyTypeName").dom.value) {
				Ext.get("dutyType").dom.value = "";
			    Ext.get("dutyTypeName").dom.value = "";
			}
		}
	}

	// 删除
	function deleteRecord() {
		var rec = gridDuty.getSelectionModel().getSelected();
		if (rec) {
			var lngNoteId = rec.get('noteId');
			var strUpdateTime = rec.get('updateTime');
			Ext.Msg.confirm(Constants.BTN_CONFIRM, MessageConstants.COM_C_002,
					function(buttonobj) {
						if (buttonobj == 'yes') {
							// 刪除
							Ext.Ajax.request({
								method : Constants.POST,
								url : 'administration/deleteRecord.action',
								params : {
									lngNoteId : lngNoteId,
									strUpdateTime : strUpdateTime
								},
								success : function(result, request) {
									var o = eval("(" + result.responseText
											+ ")");
									// 排他
									if (o.msg == Constants.DATA_USING) {
										Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
												MessageConstants.COM_I_002,
												function() {
													storeDuty.load({
														params : {
															start : 0,
															limit : Constants.PAGE_SIZE
														}
													});
													gridDuty.getView()
															.refresh();
												});
										return;
									}
									// Sql错误
									if (o.msg == Constants.SQL_FAILURE) {
										Ext.Msg.alert(Constants.ERROR,
												MessageConstants.COM_E_014);
										return;
									}
									// IO错误
									if (o.msg == Constants.IO_FAILURE) {
										Ext.Msg.alert(Constants.ERROR,
												MessageConstants.COM_E_022);
										return;
									}
									// 数据格式化错误
									if (o.msg == Constants.DATE_FAILURE) {
										Ext.Msg.alert(Constants.ERROR,
												MessageConstants.COM_E_023);
										return;
									} else {
										Ext.Msg
												.alert(
														MessageConstants.SYS_REMIND_MSG,
														MessageConstants.COM_I_005,
														function() {
															storeDuty.load({
																params : {
																	start : 0,
																	limit : Constants.PAGE_SIZE
																}
															});
															gridDuty.getView()
																	.refresh();
														});
									}
								},
								failure : function() {
									Ext.Msg.alert(Constants.ERROR,
											MessageConstants.DEL_ERROR);
								}
							});
						}
					});
		} else {
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
					MessageConstants.COM_I_001);
		}
	}

	// 非空判断
	function isnotNull() {
		// 工作类别
		var subWorktypeName = Ext.get('subWorktypeName').dom.value;
		if (subWorktypeName == "" || subWorktypeName == null) {
			Ext.Msg.alert(Constants.ERROR, String.format(
							MessageConstants.COM_E_003, '工作类别'));
			return false;
		}

		// 值班人
		var dutyman = Ext.get('dutyman').dom.value;
		if (dutyman == "" || dutyman == null) {
			Ext.Msg.alert(Constants.ERROR, String.format(
							MessageConstants.COM_E_002, '值班人'));
			return false;
		}
		return true;
	}

	// 保存
	function confirmRecord() {
		if (isnotNull()) {
			// 增加操作
			if (strRecord == "add") {
				recordUrl = 'administration/addRecord.action';
				Ext.Msg.confirm(Constants.BTN_CONFIRM, MessageConstants.COM_C_001,
						function(buttonobj) {
							if (buttonobj == 'yes') {
								formpanelDuty.getForm().submit({
									method : Constants.POST,
									params : {
									     strRegTime : Ext.get('regTime').dom.value
									},
									url : recordUrl,
									success : function(form, action) {
										// 显示成功信息
										Ext.Msg
												.alert(
														MessageConstants.SYS_REMIND_MSG,
														MessageConstants.COM_I_004,
														function() {
															storeDuty.load({
																params : {
																	start : 0,
																	limit : Constants.PAGE_SIZE
																}
															});
															gridDuty.getView()
																	.refresh();
															winDuty.hide();
														});
									},
									failure : function() {
									}
								});
							}
						})
			}

			// 修改操作
			if (strRecord == "update") {
				recordUrl = 'administration/updateRecord.action';
                Ext.Msg.confirm(Constants.BTN_CONFIRM, MessageConstants.COM_C_001,
						function(buttonobj) {
							if (buttonobj == 'yes') {
								formpanelDuty.getForm().submit({
									method : Constants.POST,
									params : {
									     strRegTime : Ext.get('regTime').dom.value,
									     strUpdateTime : Ext.get('updateTime').dom.value
									},
									url : recordUrl,
									success : function(form, action) {
										var o = eval('('
												+ action.response.responseText
												+ ')');
										// 排他
										if (o.msg == Constants.DATA_USING) {
											Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
													MessageConstants.COM_I_002);
											return;
										}
										// Sql错误
										if (o.msg == Constants.SQL_FAILURE) {
											Ext.Msg.alert(Constants.ERROR,
													MessageConstants.COM_E_014);
											return;
										}
										// IO错误
										if (o.msg == Constants.IO_FAILURE) {
											Ext.Msg.alert(Constants.ERROR,
													MessageConstants.COM_E_022);
											return;
										}
										// 数据格式化错误
										if (o.msg == Constants.DATE_FAILURE) {
											Ext.Msg.alert(Constants.ERROR,
													MessageConstants.COM_E_023);
											return;
										}
										// 显示成功信息
										Ext.Msg
												.alert(
														MessageConstants.SYS_REMIND_MSG,
														MessageConstants.COM_I_004,
														function() {
															storeDuty.load({
																params : {
																	start : 0,
																	limit : Constants.PAGE_SIZE
																}
															});
															gridDuty.getView()
																	.refresh();
															winDuty.hide();
														});
									},
									failure : function() {
									}
								});
							}
						})
			}
		}
	}

	// 取消
	function cancelRecord() {
		Ext.Msg.confirm(Constants.BTN_CONFIRM, MessageConstants.COM_C_005,
				function(buttonobj) {
					if (buttonobj == 'yes') {
						// storeDuty.load({
						// params : {
						// start : 0,
						// limit : Constants.PAGE_SIZE
						// }
						// });
						// gridDuty.getView().refresh();
						winDuty.hide();
					}
				})
	}
	// ---------值班记事事件结束---------------------

	// ---------值班人员事件---------------------
	// 增加
	function addPerson() {
		strPerson = 'add';
		formpanelPerson.getForm().reset();
		winPerson.setTitle("新增值班人员");
		dateInit();
		txtDutytime.setValue(today);
		winPerson.show();
		if (drpWorktypeNamePerson.store.getCount() == 0){
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG, String.format(
											MessageConstants.COM_I_013,
											"工作类别"), function(){
											    winPerson.hide();
											});
											
					return;
		}
		if (drpDutytypePerson.store.getCount() == 0){
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG, String.format(
											MessageConstants.COM_I_013,
											"值别"), function(){
											    winPerson.hide();
											});
											
					return;
		}
	}

	// 修改
	function updatePerson() {
		var rec = gridPerson.getSelectionModel().getSelected();
		if (!rec) {
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
					MessageConstants.COM_I_001);
			return false;
		} else {
			strPerson = "update";
			formpanelPerson.getForm().reset();
			winPerson.setTitle("修改值班人员");
			winPerson.show();
			formpanelPerson.getForm().loadRecord(rec);
			Ext.get('worktypeCodePerson').dom.value = rec.get('worktypeCode') == null
					? ""
					: rec.data.worktypeCode;
			Ext.get('updateTimePerson').dom.value = rec.get('updateTime') == null
					? ""
					: rec.data.updateTime;
			Ext.get('subWorktypeCodePerson').dom.value = rec
					.get('subWorktypeCode') == null
					? ""
					: rec.data.subWorktypeCode;
			drpWorktypeNamePerson.setValue(rec.data.subWorktypeCode);
			if(drpWorktypeNamePerson.getValue() == Ext.get("subWorktypeNamePerson").dom.value) {
				Ext.get("subWorktypeNamePerson").dom.value = "";
			}
//			Ext.get('subWorktypeNamePerson').dom.value = rec
//					.get('subWorktypeName') == null
//					? ""
//					: rec.data.subWorktypeName;
			Ext.get('dutyTypePerson').dom.value = rec.get('dutytype') == null
					? ""
					: rec.data.dutytype;
			drpDutytypePerson.setValue(rec.data.dutytype);	
			if(drpDutytypePerson.getValue() == Ext.get("dutyTypeNamePerson").dom.value) {
				Ext.get("dutyTypeNamePerson").dom.value = "";
			}
//			Ext.get('dutyTypeNamePerson').dom.value = rec.get('dutytypeName') == null
//					? ""
//					: rec.data.dutytypeName;
			Ext.get('dutymanPerson').dom.value = rec.get('dutyman') == null
					? ""
					: rec.data.dutyman;
		}
	}

	// 删除
	function deletePerson() {
		var rec = gridPerson.getSelectionModel().getSelected();
		if (rec) {
			var lngPersonId = rec.get('personId');
			var strUpdateTime = rec.get('updateTime');
			Ext.Msg.confirm(Constants.BTN_CONFIRM, MessageConstants.COM_C_002,
					function(buttonobj) {
						if (buttonobj == 'yes') {
							// 刪除
							Ext.Ajax.request({
								method : Constants.POST,
								url : 'administration/deletePerson.action',
								params : {
									lngPersonId : lngPersonId,
									strUpdateTime : strUpdateTime
								},
								success : function(result, request) {
									var o = eval("(" + result.responseText
											+ ")");
									// 排他
									if (o.msg == Constants.DATA_USING) {
										Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
												MessageConstants.COM_I_002,
												function() {
													storePerson.load({
														params : {
															start : 0,
															limit : Constants.PAGE_SIZE
														}
													});
													gridPerson.getView()
															.refresh();
												});
										return;
									}
									// Sql错误
									if (o.msg == Constants.SQL_FAILURE) {
										Ext.Msg.alert(Constants.ERROR,
												MessageConstants.COM_E_014);
										return;
									}
									// IO错误
									if (o.msg == Constants.IO_FAILURE) {
										Ext.Msg.alert(Constants.ERROR,
												MessageConstants.COM_E_022);
										return;
									}
									// 数据格式化错误
									if (o.msg == Constants.DATE_FAILURE) {
										Ext.Msg.alert(Constants.ERROR,
												MessageConstants.COM_E_023);
										return;
									} else {
										Ext.Msg
												.alert(
														MessageConstants.SYS_REMIND_MSG,
														MessageConstants.COM_I_005,
														function() {
															storePerson.load({
																params : {
																	start : 0,
																	limit : Constants.PAGE_SIZE
																}
															});
															gridPerson
																	.getView()
																	.refresh();
														});

									}
								},
								failure : function() {
									Ext.Msg.alert(Constants.ERROR,
											MessageConstants.DEL_ERROR);
								}
							});
						}
					});
		} else {
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
					MessageConstants.COM_I_001);
		}
	}

	// 非空判断
	function isnotPersonNull() {
		// 工作类别
		var subWorktypeName = Ext.get('subWorktypeNamePerson').dom.value;
		if (subWorktypeName == "" || subWorktypeName == null) {
			Ext.Msg.alert(Constants.ERROR, String.format(
							MessageConstants.COM_E_003, '工作类别'));
			return false;
		}

		// 值别
		var dutyman = Ext.get('dutyTypeNamePerson').dom.value;
		if (dutyman == "" || dutyman == null) {
			Ext.Msg.alert(Constants.ERROR, String.format(
							MessageConstants.COM_E_002, '值别'));
			return false;
		}

		// 值班人
		var dutyman = Ext.get('dutymanPerson').dom.value;
		if (dutyman == "" || dutyman == null) {
			Ext.Msg.alert(Constants.ERROR, String.format(
							MessageConstants.COM_E_002, '值班人'));
			return false;
		}
		return true;
	}

	// 保存
	function confirmPerson() {
		if (isnotPersonNull()) {
			// 增加操作
			if (strPerson == "add") {
				personUrl = 'administration/addPersons.action';
				Ext.Msg.confirm(Constants.BTN_CONFIRM, MessageConstants.COM_C_001,
						function(buttonobj) {
							if (buttonobj == 'yes') {
								formpanelPerson.getForm().submit({
									method : Constants.POST,
									params : {
									     strDutytime : Ext.get('dutytime').dom.value
									},
									url : personUrl,
									success : function(form, action) {
										// 显示成功信息
										Ext.Msg
												.alert(
														MessageConstants.SYS_REMIND_MSG,
														MessageConstants.COM_I_004,
														function() {
															storePerson.load({
																params : {
																	start : 0,
																	limit : Constants.PAGE_SIZE
																}
															});
															gridPerson
																	.getView()
																	.refresh();
															winPerson.hide();
														});
									},
									failure : function() {
									}
								});
							}
						})
			}

			// 修改操作
			if (strPerson == "update") {
				personUrl = 'administration/updatePerson.action';
				Ext.Msg.confirm(Constants.BTN_CONFIRM, MessageConstants.COM_C_001,
						function(buttonobj) {
							if (buttonobj == 'yes') {
								formpanelPerson.getForm().submit({
									method : Constants.POST,
									params : {
									     strDutytime : Ext.get('dutytime').dom.value,
									     strUpdateTime : Ext
									     .get('updateTimePerson').dom.value
//												.get('updateTime').dom.value
									     // modify by liuyi 090925 14:43
												
									},
									url : personUrl,
									success : function(form, action) {
										var o = eval('('
												+ action.response.responseText
												+ ')');
										// 排他
										if (o.msg == Constants.DATA_USING) {
											Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
													MessageConstants.COM_I_002);
											return;
										}
										// Sql错误
										if (o.msg == Constants.SQL_FAILURE) {
											Ext.Msg.alert(Constants.ERROR,
													MessageConstants.COM_E_014);
											return;
										}
										// IO错误
										if (o.msg == Constants.IO_FAILURE) {
											Ext.Msg.alert(Constants.ERROR,
													MessageConstants.COM_E_022);
											return;
										}
										// 数据格式化错误
										if (o.msg == Constants.DATE_FAILURE) {
											Ext.Msg.alert(Constants.ERROR,
													MessageConstants.COM_E_023);
											return;
										}
										// 显示成功信息
										Ext.Msg
												.alert(
														MessageConstants.SYS_REMIND_MSG,
														MessageConstants.COM_I_004,
														function() {
															storePerson.load({
																params : {
																	start : 0,
																	limit : Constants.PAGE_SIZE
																}
															});
															gridPerson
																	.getView()
																	.refresh();
															winPerson.hide();
														});
									},
									failure : function() {
									}
								});
							}
						})
			}
		}
	}

	// 取消
	function cancelPerson() {
		Ext.Msg.confirm(Constants.BTN_CONFIRM, MessageConstants.COM_C_005,
				function(buttonobj) {
					if (buttonobj == 'yes') {
						// storePerson.load({
						// params : {
						// start : 0,
						// limit : Constants.PAGE_SIZE
						// }
						// });
						// gridPerson.getView().refresh();
						winPerson.hide();
					}
				})
	}
	
	// 日期初值
	function dateInit() {
		today = new Date();
		today = dateFormat(today);
	}

	function dateFormat(value) {
		var year;
		var month;
		var day;
		var hour;
		var minute;
		minute = value.getMinutes();
		if (minute < 10) {
			minute = '0' + minute;
		}
		hour = value.getHours();
		if (hour < 10) {
			hour = '0' + hour;
		}
		day = value.getDate();
		if (day < 10) {
			day = '0' + day;
		}
		month = value.getMonth() + 1;
		if (month < 10) {
			month = '0' + month;
		}
		year = value.getYear();
		value = year + "-" + month + "-" + day + " " + hour + ":" + minute;
		return value;
	}

	// 排他
	function haita() {
		Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, MessageConstants.COM_I_002);
		return;
	}
		// ---------值班人员事件结束---------------------	
})