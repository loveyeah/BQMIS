Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
// 获取时间
function getSetDate() {
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

Ext.form.Label.prototype.setText = function(argText) {
	this.el.dom.innerHTML = argText;
}
var nrs = "";
function removeAppointNextRoles() {
	if (confirm("确定要清除指定下一步人吗？")) {
		nrs = "";
		Ext.get("showAppointNextRoles").dom.innerHTML = "";
	}
}
var flowCode = "hfResourceGetSC";
var entryId = getParameter("entryId");
var issueHeadId = getParameter("issueHeadId");
var orginalId = getParameter("orginalId");
function gettype() {
	if (orginalId == "4"||orginalId == "5"||orginalId == "12" ) {// modify by ywliu 20091023
		flowCode = "hfResourceGetSC";
	}else if (orginalId == "1"||orginalId=="2") {
		flowCode = "hfResourceGetGDZC";
	}
//	else if (orginalId == "5") {
//		flowCode = "hfResourceGetXZ";
//	}
	else
	{
		flowCode = "hfResourceGetSC";
	}
	// Ext.lib.Ajax.request('POST', 'resource/getIssueOrginalId.action', {
	// success : function(action) {
	// var orginalId = action.responseText;
	// if (orginalId == "6" || orginalId == "7") {
	// flowCode = "hfResourceGetGDZC";
	// } else if (orginalId == "1") {
	// flowCode = "hfResourceGetXZ";
	// }
	// }
	// }, 'issueHeadId=' + issueHeadId);
}

Ext.onReady(function() {
	gettype();
	// 设置响应时间
	var timeSet = new Ext.form.Checkbox({
		name : 'reponseTime',
		boxLabel : "设置响应时间",
		listeners : {
			check : function(box, checked) {
				if (checked) {
					// 如果checkbox选中,显示时间选择textfield
					Ext.getCmp('datePicker').setVisible(true);
					Ext.getCmp('approveField').doLayout();
				} else {
					Ext.getCmp('datePicker').setVisible(false);
					Ext.getCmp('approveField').doLayout();
				}
			}
		}
	});

	// 时间选择
	var timeDisplay = new Ext.form.TextField({
		id : 'datePicker',
		hidden : true,
		style : 'cursor:pointer',
		value : getSetDate(),
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

	// 图形展示
	var btnPictureDisplay = new Ext.Button({
		text : "图形展示",
		handler : function() {
			if (entryId == "" || entryId == "null" || entryId == null) {
				url =application_base_path + "workflow/manager/flowshow/flowshow.jsp?flowCode="
						+ flowCode;
			} else {
				url =application_base_path + "workflow/manager/show/show.jsp?entryId="
						+ entryId;
			}
			self.open(url);
		}
	});

	// 查看审批记录
	var btnRecord = new Ext.Button({
		text : "查看审批记录",
		// hidden:true,
		handler : function() {
			if (entryId == "" || entryId == "null" || entryId == null) {
				Ext.Msg.alert("提示", "流程还未启动!");
			} else {
				var url = application_base_path + "workflow/manager/approveInfo/approveInfo.jsp?entryId="
						+ entryId;
				window
						.showModalDialog(
								url,
								null,
								"dialogWidth:650px;dialogHeight:400px;;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;");

			}
		}
	});
	// 显示下步角色
	var btnNextDisplay = new Ext.Button({
		text : "显示下步角色",
		hidden : true,
		handler : getNextSteps
	});

	// 标题label
	var lblApprove = new Ext.form.Label({
		width : '100%',
		id : 'lblApprove',
		text : '物资领用上报',
		height : 10,
		style : "font-size:30px;color:blue;line-height:100px;padding-left:200px"
			// style :
			// "color:blue;line-height:100px;padding-left:175px;font-size:30px"
	});

	// 显示指定下一步角色
	var showAppointNextRoles = new Ext.form.Label({
		border : false,
		id : 'showAppointNextRoles',
		style : "color:red;font-size:12px"
	});

	// 审批方式label
	var lblApproveStyle = new Ext.form.Label({
		text : " 审批方式：",
		style : 'font-size:12px',
		border : false
	});

	// 单选按钮组
	var radioSet = new Ext.Panel({
		height : 30,
		border : false,
		layout : "column"
	});

	var stylePanel = new Ext.Panel({
		height : 65,
		border : false,
		style : "padding-top:20;padding-left:2px;border-width:1px 0 0 0;",
		items : [lblApproveStyle, radioSet]
	});

	// 获取审批方式
	function getCurrentSteps() {
		Ext.Ajax.request({
			url : "MAINTWorkflow.do?action=getFirstStep",
			method : 'post',
			params : {
				flowCode : flowCode,
				jsonArgs : "{'is12Failure':true,'isYSZC':true}"
			},
			success : function(result, request) {
				var radio = eval('(' + result.responseText + ')');
				radioAddHandler(radio.actions);
			},
			failure : function() {

			}
		});
	};

	function getNextSteps() {
		var actionId = Ext.ComponentMgr.get('approve-radio').getGroupValue();
		var args = new Object();
		args.actionId = actionId;
		args.entryId = entryId;
		var url = application_base_path + "workflow/manager/appointNextRole/appointNextRoleForReport.jsp";
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

	/**
	 * 审批方式radio button生成
	 */

	function radioAddHandler(radio) {
		// radio = radio ||
		// [{actionId:1,actionName:'上报'},{actionId:2,actionName:'紧急上报'}];
		if (radio instanceof Array) {
			if (!radioSet.items) {
				radioSet.items = new Ext.util.MixedCollection();
			}
			if (radio.length > 0) {
				if (entryId == "null" || entryId == "" || radio.length == 1) {
					radioSet.items.add(new Ext.form.Radio({
						boxLabel : "<font size='2.6px'>" + radio[0].actionName
								+ "</font>",
						id : 'approve-radio',
						name : 'rb-approve',
						inputValue : radio[0].actionId,
						checked : true
					}));
				} else if (radio.length > 1) {
					radioSet.items.add(new Ext.form.Radio({
						boxLabel : "<font size='2.6px'>" + radio[0].actionName
								+ "</font>",
						id : 'approve-radio',
						name : 'rb-approve',
						inputValue : radio[0].actionId,
						checked : true
					}));
					// 添加隐藏域，为了使radio正常显示
					radioSet.items.add(new Ext.form.Hidden());

					for (var i = 1; i < radio.length; i++) {
						// 添加隐藏域，为了使radio正常显示
						radioSet.items.add(new Ext.form.Hidden());
						radioSet.items.add(new Ext.form.Radio({
							boxLabel : "<font size='2.6px'>"
									+ radio[i].actionName + "</font>",
							name : 'rb-approve',
							inputValue : radio[i].actionId
						}));
						// 添加隐藏域，为了使radio正常显示
						radioSet.items.add(new Ext.form.Hidden());
					}
				}
			}

		}
		radioSet.doLayout();
	}

	// 备注
	var remark = new Ext.form.TextArea({
		id : "remark",
		width : '98%',
		autoScroll : true,
		border : false,
		height : 110,
	    allowBlank:false,
		value : '上报！'
	})
	// 备注label
	var lblRemarks = new Ext.form.Label({
		text : " 审批意见：",
		border : false,
		style : 'font-size:12px'
	});
	var remarkSet = new Ext.Panel({
		autoHeight : true,
		border : false,
		style : "padding-top:10;padding-left:2px;padding-bottom:2px;border-width:1px 0 0 0;",
		items : [lblRemarks, remark]
	});

	var approveField = new Ext.Panel({
		id : "approveField",
		autoWidth : true,
		height : 70,
		style : "padding-top:20;padding-bottom:20;border-width:1px 0 0 0;",
		border : false,
		layout : "column",
		anchor : '100%',
		items : [
				// 设置响应时间checkbox
				{
					columnWidth : 0.185,
					id : "tiemcheck",
					layout : "form",
					hideLabels : true,
					border : false,
					items : [timeSet]
				},
				// 时间选择
				{
					columnWidth : 0.265,
					hideLabels : true,
					layout : "form",
					border : false,
					items : [timeDisplay]
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
					items : [btnPictureDisplay]
				},
				// 查看审批记录按钮
				{
					columnWidth : 0.19,
					layout : "form",
					hideLabels : true,
					border : false,
					items : [btnRecord]
				},
				// 下一步按钮
				{
					columnWidth : 0.19,
					layout : "form",
					hideLabels : true,
					border : false,
					items : [btnNextDisplay]
				}]

	});

	
	// 审批签字
	var approvePanel = new Ext.form.FormPanel({
		border : false,
		labelAlign : 'right',
		layout : "column",
		items : [{
			columnWidth : 0.1,
			border : false
		}, {
			columnWidth : 0.8,
			border : true,
			layout : 'form',
			buttonAlign : 'center',
			items : [lblApprove, approveField, showAppointNextRoles,
					stylePanel, remarkSet],
			buttons : [{
				text : '确定',
				handler : function() {
					
					if(remark.getValue()=="")
				      {
                           Ext.Msg.alert("提示","请填写审批意见！");
                           return;
				      }
					// var actionId = Ext.ComponentMgr.get('approve-radio')
					// .getGroupValue();
					// Ext.Msg.confirm(Constants.SYS_REMIND_MSG,
					// Constants.COM_C_006, function(buttonobj) {
					// if (buttonobj == "yes") {
					// var params = "issueHeadId=" + issueHeadId
					// + "&actionId=" + actionId
					// + "&approveText="
					// + Ext.get('remark').dom.value;
					// // 上报
					// Ext.lib.Ajax
					// .request(
					// 'POST',
					// 'resource/reportIssueHeadRecord.action',
					// {
					// success : function(
					// action) {
					// var result = eval("("
					// + action.responseText
					// + ")");
					// if (!result.success) {
					// Ext.Msg
					// .alert(
					// Constants.SYS_REMIND_MSG,
					// result.msg);
					// window.returnValue = false;
					// window.close();
					// } else {
					// Ext.Msg
					// .alert(
					// Constants.SYS_REMIND_MSG,
					// Constants.COM_I_007);
					// window.returnValue = true;
					// window.close();
					// }
					// },
					// faliure : function(
					// action) {
					// Ext.Msg
					// .alert(
					// Constants.SYS_REMIND_MSG,
					// Constants.COM_E_014);
					// window.returnValue = false;
					// window.close();
					// }
					// }, params);
					// }
					// });
					workerPanel.getForm().reset();
					validateWin.show();
				}
			}, {
				text : '取消',
				handler : function() {
					window.close();
				}
			}]
		}, {
			columnWidth : 0.1,
			border : false
		}]
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
		modal : true,
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
								if (result) {
									// 参数
									var actionId = Ext.ComponentMgr
											.get('approve-radio')
											.getGroupValue();
									// Ext.Msg.confirm(Constants.SYS_REMIND_MSG,
									// Constants.COM_C_006, function(buttonobj)
									// {
									// if (buttonobj == "yes") {
									var params = "issueHeadId=" + issueHeadId
											+ "&actionId=" + actionId
											+ "&approveText="
											+ Ext.get('remark').dom.value;
									// 上报
									Ext.lib.Ajax
											.request(
													'POST',
													'resource/reportIssueHeadRecord.action',
													{
														success : function(
																action) {
															var result = eval("("
																	+ action.responseText
																	+ ")");
															if (!result.success) {
																Ext.Msg
																		.alert(
																				Constants.SYS_REMIND_MSG,
																				result.msg);
																window.returnValue = false;
																window.close();
															} else {
																Ext.Msg
																		.alert(
																				Constants.SYS_REMIND_MSG,
																				Constants.COM_I_007);
																window.returnValue = true;
																window.close();
															}
														},
														faliure : function(
																action) {
															Ext.Msg
																	.alert(
																			Constants.SYS_REMIND_MSG,
																			Constants.COM_E_014);
															window.returnValue = false;
															window.close();
														}
													}, params);
								} else {
									Ext.Msg.alert("提示", "密码错误");
								}
							}
						// var params = new Array();
						//                            
						// params.push("mrNo=" + strBusiNo);
						// // 备注
						// params.push("&approveText=" +
						// form.getForm().getValues()['remark'] + "");
						// // 工号
						// params.push("&workerCode=" + workCode.getValue());
						// // 审批方式
						// params.push("&actionId=" +actionId);
						// //form.getForm().getValues()['rb-approve']);
						// // 响应时间
						// params.push("&responseDate=" +
						// datePicker.getValue());
						// params.push("&workflowType="+flowCode);
						// params.push("&eventIdentify="+eventIdentify);
						// // params.push("&factoryType=bq");
						// params.push("&nextRoles="+nrs);
						// Ext.lib.Ajax.request(
						// 'POST',
						// 'resource/planReport.action',
						// {
						// success : function(action) {
						// var result = eval("(" + action.responseText + ")");
						// // 弹出返回信息
						// validateWin.hide();
						// Ext.Msg.alert("提示", result.msg,function(){
						//                                        
						// //审批成功时才关闭审批窗口
						// if(result.msg.indexOf('成功')!=-1)
						// {
						// window.returnValue=true;
						// window.close();
						// }
						// // window.close();
						// });
						// },
						// failure : function() {
						// Ext.Msg.alert("提示","未知错误");
						// }
						// }, params.join(""));
						//    
						// } else {
						// Ext.Msg.alert("提示","密码错误");
						// }
						// }
						}, "workerCode=" + workCode.getValue() + "&loginPwd="
								+ workPwd.getValue());
			}
		}, {
			// 取消按钮
			text : "取消",
			iconCls : "cancer",
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

	// 签字时的Enter
	document.onkeydown = function() {
		if (event.keyCode == 13) {
			document.getElementById('btnSign').click();
		}
	}
	if (entryId == null || entryId == "") {
		Ext.get("approve_his").dom.style.display = 'none';

	}

	var viewport = new Ext.Viewport({
		layout : "fit",
		border : false,
		items : [approvePanel]
	});
	getCurrentSteps();
});