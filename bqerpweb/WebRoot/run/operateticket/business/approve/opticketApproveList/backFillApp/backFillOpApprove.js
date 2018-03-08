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
 var isSeePrint=false;
 var isItemPrint=false;
 var isFinishWorkPrint=false;
 
Ext.onReady(function() {
	var arg = window.dialogArguments;
	var opticketCode = arg.opticketCode;
	var opticketStatus = arg.opticketStatus;
	var opticketType = arg.opticketType;
	var isSingle = arg.isSingle;
	var entryId = arg.entryId;
	var flowCode = arg.flowCode;
	var actionId = "";
	var eventIdentify = "";
	var workerCode = "";
	var nrs = "";
	var dfPlanStartTime = new Ext.form.TextField({
		id : 'planStartTime',
		fieldLabel : "操作开始时间",
		name : 'opticket.planStartTime',
		style : 'cursor:pointer',
		anchor : "98%",
		readOnly : true,
		// allowBlank : false,
		value : new Date().dateFormat('Y-m-d H:i:s'),
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-%d 00:00:00',
					dateFmt : 'yyyy-MM-dd HH:mm:ss',
					alwaysUseStartDate : true
				});
			}
		}
	})
	var printOP = new Ext.Button({
		id : 'printOP',
		text : "打印",
		iconCls : 'print',
		handler : function() {
			viewOperateTicketDQBirt(opticketCode);
			isSeePrint = true;
			Ext.Ajax.request({
				url : 'opticket/createTicket.action',
				params : {
					opticketCode :  opticketCode 
				},
				method : 'post',
				success : function(result, request) {
					
				}
			});
		}
	})
	var printBEF = new Ext.Button({
		id : 'printBEF',
		text : "检查项目打印",
		iconCls : 'print',
		handler : function() {
			viewOperateTicketBEFBirt(opticketCode);
			isItemPrint = true;
		}
	})
	var dfPlanEndTime = new Ext.form.TextField({
		id : 'planEndTime',
		fieldLabel : "操作终结时间",
		name : 'opticket.planEndTime',
		style : 'cursor:pointer',
		anchor : "98%",
		readOnly : true,
		allowBlank : false,
		value : new Date().dateFormat('Y-m-d H:i:s'),
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-%d 00:00:00',
					dateFmt : 'yyyy-MM-dd HH:mm:ss',
					alwaysUseStartDate : true
				});
			}
		}
	})
	var completeTime = new Ext.form.TextField({
		id : 'completeTime',
		fieldLabel : "完成准备工作时间",
		name : 'opticket.orderTime',
		style : 'cursor:pointer',
		anchor : "98%",
		readOnly : true,
		allowBlank : false,
		value : new Date().dateFormat('Y-m-d H:i:s'),
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-%d 00:00:00',
					dateFmt : 'yyyy-MM-dd HH:mm:ss',
					alwaysUseStartDate : true
				});
			}
		}
	})
	var aftTime = new Ext.form.TextField({
		id : 'aftTime',
		fieldLabel : "闸后完成工作时间",
		style : 'cursor:pointer',
		anchor : "98%",
		readOnly : true,
		allowBlank : false,
		value : new Date().dateFormat('Y-m-d H:i:s'),
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-%d 00:00:00',
					dateFmt : 'yyyy-MM-dd HH:mm:ss',
					alwaysUseStartDate : true
				});
			}
		}
	})
	var printAFT = new Ext.Button({
		id : 'printAFT',
		text : "完成工作打印",
		iconCls : 'print',
		handler : function() {
			viewOperateTicketAFTBirt(opticketCode);
			isFinishWorkPrint = true;
		}
	})
	function removeAppointNextRoles() {
		if (confirm("确定要清除指定下一步人吗？")) {
			nrs = "";
			Ext.get("showAppointNextRoles").dom.innerHTML = "";
		}
	}

	var label = new Ext.form.Label({
		id : "label",
		// text : getName(opticketStatus, opticketType,isSingle),
		// text:'上报',
		style : "color:blue;font-size:30px;line-height:80px;padding-left:150px;font-weight:bold"
	});
	// 标题
	var titleField = new Ext.Panel({
		border : false,
		height : 80,
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
	// 显示下步角色

	var btnNextDisplay = new Ext.Button({
		text : "显示下步角色",
		handler : getNextSteps
	});
	// 显示指定下一步角色
	var showAppointNextRoles = new Ext.form.Label({
		border : false,
		id : 'showAppointNextRoles',
		style : "color:red;font-size:12px"
	});
	var tbar = new Ext.Toolbar({
		items : [printOP, '-', printBEF, '-', printAFT]
	})
	function getNextSteps() {
		// var actionId = Ext.ComponentMgr.get('approve-radio').getGroupValue();
		var args = new Object();
		args.flowCode = flowCode;
		args.actionId = actionId;
		args.entryId = entryId;
		var url = "";
		if (entryId == null || entryId == "") {
			url = "/power/workflow/manager/appointNextRole/appointNextRoleForReport.jsp";
		} else {
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
		height : 55,
		border : false,
		layout : 'form',
		style : "padding-top:20;border-width:1px 0 0 0;",
		items : [approveLabel, radioSet]
	});
 
	/**
	 * 审批方式radio button生成
	 */ 
	function radioAddHandler(radio) {
		radio.push({actionId:radio[0].actionId+"0",actionName:'未执行/作废',eventIdentify:'WZXZF',url:''});
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
							actionId = radio[0].actionId;
							eventIdentify = obj.eventIdentify==undefined?"WZXZF":obj.eventIdentify
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
		id : 'rem',
		width : '100%',
		autoScroll : true,
		name : 'remark',
		isFormField : true,
		border : false,
		value : "请填写审批意见！",
		height : 90
	})
	// 第三行
	// var startLable = new Ext.form.Label({
	// style : 'font-size:12px',
	// height : 20,
	// text : "操作开始时间:"
	// })
	// var endLable = new Ext.form.Label({
	// style : 'font-size:12px',
	// height : 20,
	// text : "操作终结时间:"
	// })
	// var completeLable = new Ext.form.Label({
	// style : 'font-size:12px',
	// height : 20,
	// text : "完成准备工作时间:"
	// })
	// var afttTime = new Ext.form.Label({
	// style : 'font-size:12px',
	// height : 20,
	// text : "闸后完成工作时间:"
	// })
	var planDate = new Ext.Panel({
		border : false,
		height : 50,
		layout : 'column',
		items : [{
			columnWidth : 0.5,
			border : false,
			layout : 'form',
			items : [dfPlanStartTime, completeTime]
		}, {
			columnWidth : 0.5,
			border : false,
			layout : 'form',
			items : [dfPlanEndTime, aftTime]
		}]
	})
	var remarkSet = new Ext.Panel({
		height : 120,
		border : false,
		style : "padding-top:10;border-width:1px 0 0 0;",
		items : [backLabel, remark]
	});
	var form = new Ext.FormPanel({
		columnWidth : 0.8,
		border : true,
		labelAlign : 'center',
		buttonAlign : 'right',
		buttons : [{
			text : "确定",
			handler : function() {
				// 显示用户验证窗口
				// if (opticketStatus == "4") {
				//----------add by drdu 20100202-----------------------------
				if(!isSeePrint)
				{
				 Ext.Msg.alert('提示', '请先打印!');
					return; 	
				}
				if(!isItemPrint)
				{
					 Ext.Msg.alert('提示', '请先打印检查项目!');
					return; 	
				}
				if(!isFinishWorkPrint)
				{
					 Ext.Msg.alert('提示', '请先打印完成工作!');
					return; 	
				}
				//-----------------------------------end----------------------
				if (dfPlanStartTime.getValue() >= dfPlanEndTime.getValue()) {
					Ext.Msg.alert('提示', '结束时间大于开始时间');
					return
				}
				workerPanel.getForm().reset();
				validateWin.show();
				// } else {
				// workerPanel.getForm().reset();
				// validateWin.show();
				// }
			}
		}, {
			text : "取消",
			handler : function() {
				layout.hidden = true;
				window.close();
			}
		}],
		items : [tbar, titleField, tableField, showAppointNextRoles,
				approveSet, planDate, remarkSet]
	});

	var signPanel = new Ext.Panel({
		layout : "column",
		autoHeight : false,
		height : 500,
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

									var params = new Array();

									params.push("opticketCode=" + opticketCode);
									// 备注
									params
											.push("&approveText="
													+ form.getForm()
															.getValues()['remark']
													+ "");
									// 工号
									params.push("&workerCode="
											+ workCode.getValue());
									// 审批方式
									params.push("&actionId=" + actionId); // form.getForm().getValues()['rb-approve']);
									// 响应时间
									params.push("&responseDate="
											+ datePicker.getValue());
									params.push("&flowCode=" + flowCode);
									params.push("&eventIdentify="
											+ eventIdentify);
									params.push("&planStartTime="
											+ dfPlanStartTime.getValue());
									params.push("&planEndTime="
											+ dfPlanEndTime.getValue())
									params.push("&completeTime="
											+ completeTime.getValue())
									params.push("&aftTime="
											+ aftTime.getValue())
									Ext.lib.Ajax.request('POST',
											"opticket/endSign.action", {
												success : function(action) {
													var result = eval("("
															+ action.responseText
															+ ")");
													// 弹出返回信息
													validateWin.hide();
													Ext.Msg.alert("提示",
															result.msg,
															function() {
																// add by fyyang
																// 090106
																// 审批成功时才关闭审批窗口
																if (result.msg
																		.indexOf('成功') != -1) {
																	window
																			.close();
																}
																// window.close();
															});
												},
												failure : function() {
													Ext.Msg.alert("提示", "未知错误");
												}
											}, params.join(""));

								} else {
									Ext.Msg.alert("提示", "密码错误");
								}
							}
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
				entryId : entryId,
				workerCode : workerCode
			// jsonArgs : "{'isDBP':false,'is12Failure':true,'isYSZC':true}"
			},
			success : function(result, request) {
				var radio = eval('(' + result.responseText + ')');
				label.setText(radio[0].stepName);
				// alert(radio[0].stepName);
				// alert(document.innerHTML);
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
		getFirstSteps();

	} else {
		getCurrentSteps();
	}
});
