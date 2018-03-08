Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.form.Label.prototype.setText = function(argText) {
	this.el.dom.innerHTML = argText;
}
// 获取时间
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
var nrs = "";
function removeAppointNextRoles() {
	if (confirm("确定要清除指定下一步人吗？")) {
		nrs = "";
		Ext.get("showAppointNextRoles").dom.innerHTML = "";
	}
}
Ext.onReady(function() {
	var arg = window.dialogArguments

	var depmainid = arg.depmainid;
	var entryId = arg.entryId;
	var flowCode = arg.flowCode;
	var deptId=arg.deptId;
	var mytitle = "检修周本部门上报";
	var actionId = arg.actionId; //add by sychen 20100325
//	var actionId = "";
	var eventIdentify = "";
	var workerCode = "";

	// ↓↓*******************保护投退申请单上报页面**************************************

	var label = new Ext.form.Label({
		id : "label",
		text : mytitle,
		style : "color:blue;font-size:30px;line-height:100px;padding-left:150px;font-weight:bold"
	});
	// 标题
	var titleField = new Ext.Panel({
		border : false,
		height : 100,
		items : [label]
	})

	// 设置响应时间
	var timeCheck = new Ext.form.Checkbox({
		boxLabel : "设置响应时间",
		style : "padding-top:120;border-width:1px 0 0 0;",
		listeners : {
			check : function(box, checked) {
				if (checked) {
					// 如果checkbox选中,显示时间选择textfield
					Ext.getCmp('datePicker').setVisible(true);
				} else {
					Ext.getCmp('datePicker').setVisible(false);
				}
			}
		}
	});
	// 时间选择
	var datePicker = new Ext.form.TextField({
		style : 'cursor:pointer',
		id : "datePicker",
		hidden : true,
		value : getDate(),
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-01 00:00:00',
					dateFmt : 'yyyy-MM-dd HH:mm:ss',
					alwaysUseStartDate : true
				});
			}
		}
	});
	// 图形显示
	var graphButton = new Ext.Button({
		text : "图形展示",
		handler : function() {
			var url = "";
			if (entryId == null || entryId == "") {
				url = "/power/workflow/manager/flowshow/flowshow.jsp?flowCode="
						+ flowCode;
			} else {
				url = "/power/workflow/manager/show/show.jsp?entryId="
						+ entryId;
			}
			window.open(url);
		}
	});
	// 查看审批记录
	var approveHistoryButton = new Ext.Button({
		text : "查看审批记录",
		handler : function() {
			var url = "/power/workflow/manager/approveInfo/approveInfo.jsp?entryId="
					+ entryId;
			window
					.showModalDialog(
							url,
							null,
							"dialogWidth:650px;dialogHeight:400px;;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;");
		}
	});
	// 选择下步角色

	var btnNextDisplay = new Ext.Button({
		text : "选择下步角色",
		handler : getNextSteps
	});
	// 显示指定下一步角色
	var showAppointNextRoles = new Ext.form.Label({
		border : false,
		id : 'showAppointNextRoles',
		style : "color:red;font-size:12px"
	});
	function getNextSteps() {
		// var actionId = Ext.ComponentMgr.get('approve-radio').getGroupValue();
		var args = new Object();
		args.flowCode = flowCode;
		args.actionId = actionId;
		args.deptId = deptId;
		args.entryId = entryId;
		args.deptParm = "deptParm";
		var url = "";
		if (entryId == null || entryId == "") {
//				url = "/power/workflow/manager/apponitNextPerson/appointNextPersonForReport.jsp";
			url = "/power/workflow/manager/appointNextRole/appointNextRoleForReport.jsp";
			} else {    
//				url = "/power/workflow/manager/apponitNextPerson/apponitNextPerson.jsp";
				url = "/power/workflow/manager/appointNextRole/appointNextRole.jsp";
			}
		var ro = window
				.showModalDialog(
						url,
						args,
						"dialogWidth:400px;dialogHeight:350px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;");
		if (typeof(ro) != "undefined") {
			if (ro) {
				nrs = ro.nrs;
				showAppointNextRoles
						.setText("<span style=\"cursor:hand;color:blue\"  onclick=\"removeAppointNextRoles();\">[取消指定]</span>  您指定下一步角色为：[ "
								+ ro.nrsname + " ]");
			}
		}
	};
	
	// 第二行
	var tableField = new Ext.Panel({
		border : false,
		height : 70,
		autoWidth : false,
		anchor : '100%',
		style : "padding-top:20;padding-bottom:20;border-width:1px 0 0 0;",
		layout : "column",
		items : [
				// 设置响应时间checkbox
				{
					columnWidth : 0.185,
					id : "tiemcheck",
					layout : "form",
					hideLabels : true,
					border : false,
					items : [timeCheck]
				},
				// 时间选择
				{
					columnWidth : 0.265,
					hideLabels : true,
					id : "datepickerPanel",
					layout : "form",
					border : false,
					items : [datePicker]
				}, {
					columnWidth : 0.02,
					border : false
				},
				// 图形显示按钮
				{
					columnWidth : 0.145,
					layout : "form",
					hideLabels : true,
					border : false,
					items : [graphButton]
				},
				// 查看审批记录按钮
				{
					columnWidth : 0.19,
					layout : "form",
					hideLabels : true,
					border : false,
					id : 'approve_his',
					items : [approveHistoryButton]
				},
				// 下一步按钮
				{
					columnWidth : 0.19,
					layout : "form",
					hideLabels : true,
					border : false,
					id : 'approve_next',
					items : [btnNextDisplay]
				}]

	});
	// 审批方式label
	var approveLabel = new Ext.form.Label({
		text : " 审批方式：",
		style : 'font-size:12px'
	});
	// 单选按钮组
	var radioSet = new Ext.Panel({
		height : 50,
		border : false,
		layout : "column"
	});
	// 审批方式panel
	var approveSet = new Ext.Panel({
		height : 80,
		border : false,
		layout : 'form',
		style : "padding-top:20;border-width:1px 0 0 0;",
		items : [approveLabel, radioSet]
	});

	/**
	 * 审批方式radio button生成
	 */

	function radioAddHandler(radio) {

		if (radio instanceof Array) {
			if (!radioSet.items) {
				radioSet.items = new Ext.util.MixedCollection();
			}
			if (radio.length > 0) {
				var _radio = new Ext.form.Radio({
					boxLabel : "<font size='2.6px'>" + radio[0].actionName
							+ "</font>",
					id : 'approve-radio' + radio[0].actionId,
					name : 'rb-approve',
					inputValue : radio[0].actionId,
					url : radio[0].url,
					eventIdentify : radio[0].changeBusiStateTo,
					listeners : {
						'check' : function() {
							var _actionId = Ext.ComponentMgr
									.get('approve-radio' + radio[0].actionId)
									.getGroupValue();
							var obj = Ext.getCmp('approve-radio' + _actionId);
							actionId = _actionId;
							eventIdentify = obj.eventIdentify

						}
					},
					checked : true
				});
				radioSet.items.add(_radio);
				// 添加隐藏域，为了使radio正常显示
				radioSet.items.add(new Ext.form.Hidden());

				for (var i = 1; i < radio.length; i++) {
					// 添加隐藏域，为了使radio正常显示
					radioSet.items.add(new Ext.form.Hidden());
					var _radio = new Ext.form.Radio({
						boxLabel : "<font size='2.6px'>" + radio[i].actionName
								+ "</font>",
						id : 'approve-radio' + radio[i].actionId,
						name : 'rb-approve',
						url : radio[i].url,
						eventIdentify : radio[i].changeBusiStateTo,
						inputValue : radio[i].actionId
					});

					radioSet.items.add(_radio);
					// 添加隐藏域，为了使radio正常显示
					radioSet.items.add(new Ext.form.Hidden());
				}
			}

		}
		radioSet.doLayout();
	}

	// *****************************备注******************
	// 备注label
	var backLabel = new Ext.form.Label({
		text : " 备注：",
		style : 'font-size:12px',
		height : 20
	});
	// 备注输入框
	var remark = new Ext.form.TextArea({
		width : '100%',
		autoScroll : true,
		name : 'remark',
		isFormField : true,
		border : false,
		value : "请填写审批意见！",
		height : 90
	})
	// 第三行
	var remarkSet = new Ext.Panel({
		height : 120,
		border : false,
		style : "padding-top:10;border-width:1px 0 0 0;",
		items : [backLabel, remark]
	});

	var form = new Ext.form.FormPanel({
		columnWidth : 0.8,
		border : true,
		labelAlign : 'center',
		buttonAlign : 'right',
		buttons : [{
			text : Constants.CONFIRM,
			handler : function() {
				// 显示用户验证窗口  //update by sychen 20100326
//				if (nrs == "") {
//					Ext.Msg.alert("提示", "请选择下一步角色！");
//					return;
//				}
				workerPanel.getForm().reset();
				validateWin.show();
			}
		}, {
			text : Constants.BTN_CANCEL,
			handler : function() {
				layout.hidden = true;
				window.close();
			}
		}],
		items : [titleField, tableField, approveSet, showAppointNextRoles,
				remarkSet]
	});

	var signPanel = new Ext.Panel({
		layout : "column",
		autoHeight : false,
		height : 600,
		border : true,
		items : [{
			columnWidth : 0.1,
			border : false
		}, form, {
			columnWidth : 0.1,
			border : false
		}],
		defaults : {
			labelAlign : 'center'
		}
	});
	// 显示区域
	var layout = new Ext.Viewport({
		layout : 'fit',
		border : false,
		height : 950,
		width : 600,
		items : [{
			layout : 'fit',
			margins : '0 0 0 0',
			region : 'center',
			autoScroll : true,
			items : [signPanel]
		}],
		defaults : {
			autoScroll : true
		}
	});
	// ↓↓****************员工验证窗口****************

	// 工号
	var workCode = new Ext.form.TextField({
		id : "workCode",
		fieldLabel : '工号<font color ="red">*</font>',
		allowBlank : false,
		width : 120
	});

	// 密码
	var workPwd = new Ext.form.TextField({
		id : "workPwd",
		fieldLabel : '密码<font color ="red">*</font>',
		allowBlank : false,
		inputType : "password",
		width : 120
	});
	// 弹出窗口panel
	var workerPanel = new Ext.FormPanel({
		frame : true,
		labelAlign : 'right',
		height : 120,
		items : [workCode, workPwd]
	});

	// 弹出窗口
	var validateWin = new Ext.Window({
		width : 300,
		height : 140,
		title : "请输入工号和密码",
		buttonAlign : "center",
		resizable : false,
		modal : true,// add
		items : [workerPanel],
		buttons : [{
			text : '确定',
			id : 'btnSign',
			handler : function() {
				// 工号确认
				Ext.lib.Ajax.request('POST',
						'comm/workticketApproveCheckUser.action', {
							success : function(action) {
								var result = eval(action.responseText);
								// 如果验证成功，进行签字操作
								if (result) {// update by sychen 20100326
									if (Ext.get("showAppointNextRoles").dom.innerHTML == "") {
										Ext.Ajax.request({
											url : "MAINTWorkflow.do?action=getFirstNextStepGroups",
											method : 'post',
											params : {
												flowCode : flowCode,
												actionId : actionId,
												deptId : deptId
											},
											success : function(result, request) {
												var radio = eval('('
														+ result.responseText
														+ ')');

												var nrs = "";
												var nrsname = "";
												for (var i = 0; i < radio.length; i++) {
													nrs += radio[i].groupId
															+ ",";
													nrsname += radio[i].groupName
															+ ",";

												}
												form.getForm().submit({
													url : 'manageplan/repairReport.action',
													waitMsg : '正在保存数据...',
													method : 'post',
													params : {
														depmainid : depmainid,
														nextRoles : nrs,
														actionId : actionId,
														approveText : remark
																.getValue(),
														flowCode : flowCode
													},
													success : function(form,
															action) {
														var result = eval("("
																+ action.response.responseText
																+ ")");
														// 弹出返回信息
														Ext.Msg
																.alert(
																		Constants.NOTICE,
																		result.msg,
																		function() {
																			// add
																			// by
																			// fyyang
																			// 090106
																			// 审批成功时才关闭审批窗口
																			if (result.msg
																					.indexOf('成功') != -1) {
																				window
																						.close();
																				window.returnValue = true;
																			}
																			validateWin
																					.hide();
																		});
													},
													failure : function() {
														Ext.Msg
																.alert(
																		Constants.ERROR,
																		Constants.UNKNOWN_ERR);
													}

												});
											},
											failure : function() {
												Ext.Msg.alert("提示", "错误");
											}
										});
									} else {
										selectRole();
									}
								} else {
									Ext.Msg.alert(Constants.SYS_REMIND_MSG,
											Constants.USER_CHECK_ERROR);
								}
							}
						}, "workerCode=" + workCode.getValue() + "&loginPwd="
								+ workPwd.getValue());
			}
		}, {
			// 取消按钮
			text : Constants.BTN_CANCEL,
			iconCls : Constants.CLS_CANCEL,
			handler : function() {
				validateWin.hide();
			}
		}],
		listeners : {
			show : function(com) {
				// 取得默认工号
				workCode.setValue(workerCode);
			}
		},
		closeAction : 'hide'
	});

	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
			success : function(action) {
				var result = eval("(" + action.responseText + ")");
				if (result.workerCode) {
					// 设定默认工号
					workerCode = result.workerCode;

				}
			}
		});
	}
	getWorkCode();
	// ↑↑****************员工验证窗口****************

	function getFirstSteps() {

		Ext.Ajax.request({
			url : "MAINTWorkflow.do?action=getFirstStep",
			method : 'post',
			params : {
				flowCode : flowCode
			// jsonArgs : "{'isDBP':false,'is12Failure':true,'isYSZC':true}"
			},
			success : function(result, request) {

				var radio = eval('(' + result.responseText + ')');

				radioAddHandler(radio.actions);
			},
			failure : function() {
				Ext.Msg.alert("提示", "错误");
			}
		});
	};

	function getCurrentSteps() {
		Ext.Ajax.request({
			url : "MAINTWorkflow.do?action=getCurrentSteps",
			method : 'post',
			params : {
				entryId : entryId
			// workerCode : workerCode,
			// jsonArgs : "{'isDBP':false,'is12Failure':true,'isYSZC':true}"
			},
			success : function(result, request) {
				var radio = eval('(' + result.responseText + ')');
				radioAddHandler(radio[0].actions);
			},
			failure : function() {

			}
		});
	};

	// 签字时的Enter
	document.onkeydown = function() {
		if (event.keyCode == 13) {
			document.getElementById('btnSign').click();
		}
	}
	if (entryId == null || entryId == "") {
		Ext.get("approve_his").dom.style.display = 'none';

	}
	
	//add by sychen 20100326
	function selectRole(){
		form.getForm().submit({
					url : 'manageplan/repairReport.action',
					waitMsg : '正在保存数据...',
					method : 'post',
					params : {
						depmainid : depmainid,
						nextRoles : nrs,
						actionId : actionId,
						approveText : remark.getValue(),
						flowCode : flowCode
					},
					success : function(form, action) {
						var result = eval("(" + action.response.responseText
								+ ")");
						// 弹出返回信息
						Ext.Msg.alert(Constants.NOTICE, result.msg, function() {
									
									// 审批成功时才关闭审批窗口
									if (result.msg.indexOf('成功') != -1) {
										window.close();
										window.returnValue = true;
									}
									validateWin.hide();
								});
					},
					failure : function() {
						Ext.Msg.alert(Constants.ERROR, Constants.UNKNOWN_ERR);
					}

				});
	}
	
	getFirstSteps();

});
