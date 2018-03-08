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

	var strBusiNo = arg.busiNo;
	var workticketType = arg.workticketType;
	var entryId = arg.entryId;
	var flowCode = arg.flowCode;
	var mytitle = arg.title;
	var actionId = "";
	var eventIdentify = "";
	var workerCode = "";

	var busiStatus = arg.busiStatus;// add
	var ifSeeDanger = false;
	var fireLevelId = arg.fireLevelId;
	var blockCode = arg.blockCode;
	var isSeePrint = false;

	// ---add by fyyang 090407 --增加滚动字幕--
	var noticeInfoText = getApproveNoticeInfo(blockCode);
	var noticeInfoLabel = new Ext.form.Label({
				id : "label",
				html : '<marquee scrollamount="2" direction="Left" >'
						+ noticeInfoText + '</marquee >',
				style : "color:red;font-size:20px;font-weight:bold"
			});
	var noticeField = new Ext.Panel({
				border : true,
				layout : 'form',
				height : 40,
				items : [noticeInfoLabel]
			});
	// ----------------

	// ↓↓*******************工作票审批**************************************

	var label = new Ext.form.Label({
		id : "label",
		text : mytitle,
		style : "color:blue;font-size:25px;line-height:50px;padding-left:280px;font-weight:bold"
	});

	var printDangerButton = new Ext.Button({
		columnWidth : 0.5,
		text : '打印危险点控制措施票',
		align : 'right',
		handler : function() {
			if (confirm("打印工作票和控制措施票完成后，请点击确定许可。")) {

				var url = '/powerrpt/report/webfile/bqmis/workticketDangerControl.jsp?workticketNo='
						+ strBusiNo;
				window.open(encodeURI(url));
			}
		}
	})

	var btnPrintWorkticket = new Ext.Button({
				columnWidth : 0.5,
				text : '打印工作票',
				align : 'right',
				handler : showPrintWindow
			})

	var printPanel = new Ext.Toolbar({
				hidden : true,
				items : [printDangerButton, '-', btnPrintWorkticket]
			});

	function showPrintWindow() {
		if (confirm("打印工作票和控制措施票完成后，请点击确定许可。")) {
			var strReportAdds = getReportUrl(workticketType, strBusiNo,
					fireLevelId);
			if (strReportAdds != "") {
				window.open(strReportAdds);
			} else {
				Ext.Msg.alert("目前没有该种工作票票面预览");
			}

		}
	}

	// 标题
	var titleField = new Ext.Panel({
				border : false,
				layout : 'form',
				height : 60,
				items : [label],
				buttons : [printPanel],
				defaults : {
					buttonAlign : 'right'
				}
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
				style : "padding-top:10;padding-bottom:10;border-width:1px 0 0 0;",
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
							boxLabel : "<font size='2.6px'>"
									+ radio[0].actionName + "</font>",
							id : 'approve-radio' + radio[0].actionId,
							name : 'rb-approve',
							inputValue : radio[0].actionId,
							url : radio[0].url,
							eventIdentify : radio[0].changeBusiStateTo,
							listeners : {
								'check' : function() {
									var _actionId = Ext.ComponentMgr
											.get('approve-radio'
													+ radio[0].actionId)
											.getGroupValue();
									var obj = Ext.getCmp('approve-radio'
											+ _actionId);
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
								boxLabel : "<font size='2.6px'>"
										+ radio[i].actionName + "</font>",
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
	var approveText = new Ext.form.TextArea({
				width : '100%',
				autoScroll : true,
				name : 'power.approveText',
				isFormField : true,
				border : false,
				value : "同意",
				height : 90
			})
	// 第三行
	var remarkSet = new Ext.Panel({
				height : 120,
				border : false,
				style : "padding-top:10;border-width:1px 0 0 0;",
				items : [backLabel, approveText]
			});

	var memoLabel = new Ext.form.Label({
				text : " 备注:",
				style : 'font-size:12px',
				height : 20
			});

	var btnOpticketSelect = new Ext.Button({
		text : "选择操作票",
		handler : function() {

			Ext.Ajax.request({
						url : 'bqworkticket/getRefOpticketNoForWorkticket.action',
						params : {
							workticketNo : strBusiNo
						},
						method : 'post',
						success : function(result, request) {
							var content = eval("(" + result.responseText + ")");
							if (content.opticketNos == null
									|| content.opticketNos == '') {
								alert("您还没有填写相关联的操作票，请填写操作票！");
								return;
							} else {
								workticketMemo.setValue(workticketMemo
										.getValue()
										+ content.opticketNos);
							}
						},
						failure : function(result, request) {
							Ext.MessageBox.alert('错误', '操作失败!');
						}
					});

		}
	});

	// 工作票主表的备注
	var workticketMemo = new Ext.form.TextArea({
				// fieldLabel : '备注',
				autoScroll : true,
				isFormField : true,
				name : 'workticketMemo',
				width : '100%',
				border : false,
				height : 80
			});

	var memoPanel = new Ext.Panel({
				height : 40,
				border : false,
				layout : "column",
				style : "padding-top:10;border-width:1px 0 0 0;",
				items : [{
							columnWidth : 0.1,
							layout : "form",
							border : false,
							items : [memoLabel]
						}, {
							columnWidth : 0.2,
							layout : "form",
							border : false,
							items : [btnOpticketSelect]
						}]
			});

	var memoInfoPanel = new Ext.Panel({
				height : 60,
				border : false,
				style : "padding-top:10;border-width:1px 0 0 0;",
				items : [workticketMemo]
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
								// if(workticketType!="4")
								// {
								// 动火票无危险点，不需判断
								if (!ifSeeDanger) {
									Ext.Msg.alert("提示", "请查看危险点！");
									return;
								}
								if (!isSeePrint) {
									Ext.Msg.alert("提示", "请查看票面！");
									return;
								}
								//
								if (mytitle == "消防监护人填写消防部门应采取的安措") {
									try {
										if (!tabSafety.hasXiaFangSafety()) {
											Ext.Msg.alert("提示",
													"请填写消防部门应采取的安措！");
											return;
										}
										// 没有查看安措
									} catch (err) {
										Ext.Msg.alert("提示", "请查看安措信息！");
										return;
									}
								}

								// }
								workerPanel.getForm().reset();
								validateWin.show();

							}
						}, {
							text : "取消",
							handler : function() {
								// layout.hidden = true;
								window.close();
							}
						}],
				items : [noticeField, titleField, tableField,
						showAppointNextRoles, approveSet, memoPanel,
						memoInfoPanel, remarkSet]
			});

	// 显示区域
	// var layout = new Ext.Viewport({
	// layout : 'fit',
	// border : false,
	// height : 950,
	// width : 600,
	// items : [{
	// layout : 'fit',
	// margins : '0 0 0 0',
	// region : 'center',
	// autoScroll : true,
	// items : [signPanel]
	// }],
	// defaults : {
	// autoScroll : true
	// }
	// });
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
									// ----------审批------------------------
									form.getForm().submit({
										url : 'bqworkticket/workticketCommApprove.action',
										waitMsg : '正在保存数据...',
										method : 'post',
										params : {
											workticketNo : strBusiNo,
											workerCode : workCode.getValue(),
											// workflowNo:entryId,
											nextRoles : nrs,
											actionId : actionId,
											eventIdentify : eventIdentify,
											responseDate : datePicker
													.getValue(),
											approveText : approveText
													.getValue(),
											workticketMemo:workticketMemo.getValue()		
										},
										success : function(form, action) {
											var o = eval("("
													+ action.response.responseText
													+ ")");
											Ext.Msg.alert("提示", o.msg,
													function() {
														if (o.msg.indexOf('成功') != -1) {
															window.returnValue = true;
															window.close();

														}
														validateWin.hide();
													});

										},
										faliue : function() {
											Ext.Msg.alert("提示", "未知错误");
										}

									});
									// ------------

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
						// jsonArgs :
						// "{'isDBP':false,'is12Failure':true,'isYSZC':true}"
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
						// jsonArgs :
						// "{'isDBP':false,'is12Failure':true,'isYSZC':true}"
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
		getFirstSteps();

	} else {
		getCurrentSteps();
	}

	// -------------------------布局-------------------
	var signPanel = new Ext.Panel({
				id : "tab1",
				layout : "column",
				autoHeight : false,
				title : '签字审批',
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

	// 票面浏览panel

	var tab4Href = getReportUrl(workticketType, strBusiNo, fireLevelId); // "/powerrpt/report/webfile/ElectricOne.jsp?no="+strBusiNo;

	var tab4Html = "<iframe name='tabReport' src='" + tab4Href
			+ "'style='width:100%;height:100%;border:0px;'></iframe>";
	var ticketPanel = new Ext.Panel({
				title : "票面浏览",
				id : 'tab4',
				html : tab4Html
			});

	// ----------安措信息控制-------------------------------------
	var showSafetyTyep = "";
	var ifdosafety = "";
	if (workticketType != "4") {
		if (busiStatus == "2" || busiStatus == "3") {
			// 已上报和已签发
			showSafetyTyep = "N";
		} else if (busiStatus == "17" || busiStatus == "18"
				|| busiStatus == "4") {
			// 4已接票17点检已审核签发18值长已审核
			showSafetyTyep = "Y";
		} else if (busiStatus == "5") {
			// 5已批准 即安措办理
			showSafetyTyep = "Y";
			ifdosafety = true;
		} else {
			showSafetyTyep = "";
		}

		if (workticketType == "2" && busiStatus == "4") {
			ifdosafety = true;
		}

	} else {
		// 动火票
		if (busiStatus == "2")
			showSafetyTyep = "repair"; // 签发时可修改检修安措
		else if (busiStatus == "3")
			showSafetyTyep = "run"; // 许可人填写运行时安措
		else if (busiStatus == "18")
			showSafetyTyep = "fire"; // 消防监护人填写消防安措
		else if (busiStatus == "23") {
			showSafetyTyep = "repair";
			ifdosafety = true;
		} // 工作负责人办理检修安措
		else if (busiStatus == "24") {
			showSafetyTyep = "run";
			ifdosafety = true;
		} // 工作许可人办理运行安措
		else {
			showSafetyTyep = "";
		}
	}
	// ----------------------------------------------------------------------------------------------------------------
	var tab2Href = "run/bqworkticket/business/approve/safety/workticketSafety.jsp?workticketNo="
			+ strBusiNo
			+ "&workticketType="
			+ workticketType
			+ "&showSafetyTyep=" + showSafetyTyep + "&ifdosafety=" + ifdosafety;
	var tab2Html = "<iframe name='tabSafety' src='" + tab2Href
			+ "'style='width:100%;height:100%;border:0px;'></iframe>";

	// 危险点
	var tab3Href = "run/bqworkticket/business/approve/danger/danger.jsp?workticketNo="
			+ strBusiNo;
	var tab3Html = "<iframe name='tabDanger' src='" + tab3Href
			+ "'style='width:100%;height:100%;border:0px;'></iframe>";

	// tabpanel
	var tabPanel;
	// -------------tabPanel------------------------------------
	// if(workticketType!="4")
	// {
	// 非动火票有危险点
	tabPanel = new Ext.TabPanel({
				activeTab : 0,
				layoutOnTabChange : true,
				border : false,
				items : [signPanel, {
							title : '安措信息',
							id : 'tab2',
							html : tab2Html
						}, {
							title : '危险点信息',
							id : 'tab3',
							html : tab3Html
						}, ticketPanel]
			})

	// }
	// else
	// {
	// //动火票无危险点
	// tabPanel= new Ext.TabPanel({
	// activeTab : 0,
	// layoutOnTabChange : true,
	// border : false,
	// items : [signPanel, {
	// title : '安措信息',
	// id:'tab2',
	// html : tab2Html
	// },ticketPanel]
	// })
	// }

	tabPanel.on("tabchange", function() {
				if (tabPanel.getActiveTab().getId() == "tab3") {
					ifSeeDanger = true;
				}
				if (tabPanel.getActiveTab().getId() == "tab4") {
					isSeePrint = true;
				}
			});
	// -----------------------------------------------

	new Ext.Viewport({
				enableTabScroll : true,
				layout : "fit",
				border : false,
				items : [{
							layout : 'fit',
							border : false,
							margins : '0 0 0 0',
							region : 'center',
							autoScroll : true,
							items : [tabPanel]
						}]
			});

	// 一种票,且状态为已办理 || //二种票,且状态为已接票
	if (((workticketType == "1" || workticketType == "3" || workticketType == "5") && busiStatus == "6")
			|| ((workticketType == "2" || workticketType == "7" || workticketType == "8") && busiStatus == "4")
			|| (workticketType == "4" && busiStatus == "27" && fireLevelId == "1")) {
		printPanel.setVisible(true);
		if (workticketType == "7" || workticketType == "8") {
			printDangerButton.setVisible(false);
		}
	}

	// 滚动字幕现场情况
	if (("1,2,3,5,6,7").indexOf(workticketType) != -1) {
		if (busiStatus == "2" || busiStatus == "3" || busiStatus == "7")
			noticeField.setVisible(false);
		else
			noticeField.setVisible(true);

	} else if (workticketType == "4") {
		if (fireLevelId == 1) {
			// 许可人填写、测量人填写、值长审核、工作许可人、值长批准
			if (busiStatus == "3" || busiStatus == "20" || busiStatus == "29"
					|| busiStatus == "24" || busiStatus == "26")
				noticeField.setVisible(true);
			else
				noticeField.setVisible(false);
		}
		if (fireLevelId == 2) {
			// 工作许可人、班长审核、领导批准、工作许可人、值班负责人
			if (busiStatus == "3" || busiStatus == "20" || busiStatus == "11"
					|| busiStatus == "24" || busiStatus == "26")
				noticeField.setVisible(true);
			else
				noticeField.setVisible(false);
		}

	} else {
		noticeField.setVisible(false);
	}

});
