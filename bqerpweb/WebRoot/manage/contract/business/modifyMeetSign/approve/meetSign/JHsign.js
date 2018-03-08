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
Ext.onReady(function() {
	// var arg = window.dialogArguments;
	// var entryId = arg.entryId;
	var entryId = getParameter("entryId");
	var conid = getParameter("id");
	var sum = getParameter("sum");
	var workerCode;
	var stepId;
	// 默认工号
	function getworCode() {
		Ext.Ajax.request({
					url : 'managecontract/getSessionInfo.action',
					params : {},
					method : 'post',
					waitMsg : '正在加载数据...',
					success : function(result, request) {
						var responseArray = Ext.util.JSON
								.decode(result.responseText);
						if (responseArray.success == true) {
							var tt = eval('(' + result.responseText + ')');
							o = tt.data;
							workerCode = o[0];
						} else {
							Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
						}
					},
					failure : function(result, request) {
						Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
					}
				});
	}
	getworCode();
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
					var url = "/power/workflow/manager/show/show.jsp?entryId="
							+ entryId;
					window.open(url);
				}
			});

	// 查看审批记录
	var btnRecord = new Ext.Button({
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

	// 标题label
	var lblApprove = new Ext.form.Label({
		width : '100%',
		id : 'lblApprove',
		text : '相关领导审批',
		height : 10,
		style : "color:blue;line-height:100px;padding-left:175px;font-size:30px"
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
		// alert(workerCode)
		// var sumBelow1w = false;
		// var sumBelow5w = false;
		// if (sum != null && sum != "") {
		// if (sum <= 10000) {
		// sumBelow1w = true;
		// sumBelow5w = false;
		// } else if (sum <= 50000 && sum > 10000) {
		// sumBelow1w = false;
		// sumBelow5w = true;
		// }
		// }
		// var
		// xx="{'sumBelow1w':"+sumBelow1w+",'sumBelow5w':"+sumBelow5w+",'is12Failure':true,'isYSZC':true}";
		Ext.Ajax.request({
					url : "MAINTWorkflow.do?action=getCurrentSteps",
					method : 'post',
					params : {
						entryId : entryId,
						workerCode : workerCode
						// jsonArgs : "{'sumBelow1w':" + sumBelow1w
						// + ",'sumBelow5w':" + sumBelow5w
						// + ",'is12Failure':true,'isYSZC':true}"
					},
					success : function(result, request) {
						var radio = eval('(' + result.responseText + ')');
						radioAddHandler(radio[0].actions);
						lblApprove.setText(radio[0].stepName);
						stepId = radio[0].stepId;
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
		var url = "/power/workflow/manager/appointNextRole/appointNextRole.jsp";
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
				radioSet.items.add(new Ext.form.Radio({
							boxLabel : "<font size='2.6px'>"
									+ radio[0].actionName + "</font>",
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
		radioSet.doLayout();
	}

	// 备注
	var remark = new Ext.form.TextArea({
				id : "remark",
				width : '98%',
				autoScroll : true,
				border : false,
				height : 110,
				value : ''
			})
	// 备注label
	var lblRemarks = new Ext.form.Label({
				text : " 备注：",
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
	// ↓↓****************员工验证窗口****************
	// 工号
	var workCode = new Ext.form.TextField({
				id : "workerCode",
				value : '999999',
				fieldLabel : '工号<font color ="red">*</font>',
				readOnly : true,
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
		modal : true,
		title : "请输入工号和密码",
		buttonAlign : "center",
		resizable : false,
		items : [workerPanel],
		buttons : [{
			text : '确定',
			id : 'btnSign',
			handler : function() {
				var actionId = Ext.ComponentMgr.get('approve-radio')
						.getGroupValue();
				// 工号确认
				Ext.lib.Ajax.request('POST',
						'managecontract/contractApproveCheckUser.action', {
							success : function(action) {
								var result = eval(action.responseText);
								if (result) {
									Ext.Ajax.request({
										url : 'managecontract/contractModifyApprove.action',
										waitMsg : '正在保存数据...',
										method : 'post',
										params : {
											entryId : entryId,
											actionId : actionId,
											approveText : remark.getValue(),
											nextRoles : nrs,
											workerCode : workCode.getValue(),
											conModId : conid,
											approveDepts : approveDept,
											stepId : stepId
										},
										success : function(result, request) {
											var responseArray = Ext.util.JSON
													.decode(result.responseText);
											if (responseArray.success == true) {
												Ext.MessageBox.alert('提示',
														responseArray.data,
														function() {
															validateWin.hide();
															window.returnValue = true;
															window.close();
														});
											} else {
												var o = eval('('
														+ result.responseText
														+ ')');
												Ext.MessageBox.alert('错误',
														o.errorMsg, function() {
															validateWin.hide();
															window.returnValue = false;
															window.close();
														});
											}
										},
										failure : function() {
											var o = eval('('
													+ result.responseText + ')');
											Ext.MessageBox.alert('错误',
													o.errorMsg, function() {
														validateWin.hide();
														window.returnValue = false;
														window.close();
													});
										}
									});
								} else {
									Ext.Msg.alert('错误', '用户名或密码输入不正确！');
								}
							}
						}, "workerCode=" + workCode.getValue() + "&loginPwd="
								+ workPwd.getValue());
			}
		}, {
			text : '取消',
			handler : function() {
				approveDept = "";
				window.location.reload();
				validateWin.hide();
			}
		}],
		closeAction : 'hide'
	});

	// 部门选择
	checkBox1 = new Ext.form.Checkbox({
				boxLabel : "设备部 "
			})
	checkBox2 = new Ext.form.Checkbox({
				boxLabel : "检修部门"
			})
	checkBox3 = new Ext.form.Checkbox({
				boxLabel : "安环部"
			})
	checkBox4 = new Ext.form.Checkbox({
				boxLabel : "发电部"
			})
	checkBox5 = new Ext.form.Checkbox({
				boxLabel : "人力资源部"
			})
	checkBox6 = new Ext.form.Checkbox({
				boxLabel : "思想政治工作部"
			})
	checkBox7 = new Ext.form.Checkbox({
				boxLabel : "公安部"
			})
	checkBox8 = new Ext.form.Checkbox({
				boxLabel : "燃料部 "
			})
	checkBox9 = new Ext.form.Checkbox({
				boxLabel : "运营公司"
			})
	checkBox10 = new Ext.form.Checkbox({
				boxLabel : "多经公司"
			})
	checkBox11 = new Ext.form.Checkbox({
				boxLabel : "基建工程部"
			})
	// 选择改合同审批部门
	showApproveDept = new Ext.form.Label({
		text : " 选择合同审批部门:",
			 style : 'font-size:12px'
		})
	var approveSetDept = new Ext.Panel({
				height : 95,
				border : false,
				layout : 'column',
				style : "padding-top:10;border-width:1px 0 0 0;",
				items : [{
							columnWidth : 1,
							layout : "form",
							hideLabels : true,
							border : false,
							items : [showApproveDept]
						}, {
							columnWidth : 0.2,
							layout : "form",
							hideLabels : true,
							border : false,
							items : [checkBox1]
						}, {
							columnWidth : 0.2,
							layout : "form",
							hideLabels : true,
							border : false,
							items : [checkBox2]
						}, {
							columnWidth : 0.2,
							layout : "form",
							hideLabels : true,
							border : false,
							items : [checkBox3]
						}, {
							columnWidth : 0.2,
							layout : "form",
							hideLabels : true,
							border : false,
							items : [checkBox4]
						}, {
							columnWidth : 0.2,
							layout : "form",
							hideLabels : true,
							border : false,
							items : [checkBox5]
						}, {
							columnWidth : 0.2,
							layout : "form",
							hideLabels : true,
							border : false,
							items : [checkBox6]
						}, {
							columnWidth : 0.2,
							layout : "form",
							hideLabels : true,
							border : false,
							items : [checkBox7]
						}, {
							columnWidth : 0.2,
							layout : "form",
							hideLabels : true,
							border : false,
							items : [checkBox8]
						}, {
							columnWidth : 0.2,
							layout : "form",
							hideLabels : true,
							border : false,
							items : [checkBox9]
						},{
						columnWidth : 0.2,
							layout : "form",
							hideLabels : true,
							border : false,
							items : [checkBox10]
						},{
						columnWidth : 0.2,
							layout : "form",
							hideLabels : true,
							border : false,
							items : [checkBox11]
						}]
			});
	// ↑↑****************员工验证窗口****************

	// 审批签字
	var approveDept = "";
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
							items : [lblApprove, approveField,
									showAppointNextRoles, stylePanel,
									approveSetDept, remarkSet],
							buttons : [{
										text : '确定',
										handler : function() {
											if (checkBox1.checked) {
												// approveDept.push("5");
												approveDept += ",22";
											}
											if (checkBox2.checked) {
												// approveDept.push("4");
												approveDept += ",23";
											}
											if (checkBox3.checked) {
												// approveDept.push("6");
												approveDept += ",24";
											}
											if (checkBox4.checked) {
												approveDept += ",25";
											}
											if (checkBox5.checked) {
												approveDept += ",26";
											}
											if (checkBox6.checked) {
												approveDept += ",27";
											}
											if (checkBox7.checked) {
												approveDept += ",28";
											}
											if (checkBox8.checked) {
												approveDept += ",29";
											}
											if (checkBox9.checked) {
												approveDept += ",30";
											}
											if (checkBox10.checked) {
												approveDept += ",31";
											}
											if (checkBox11.checked) {
												approveDept += ",32";
											}
//											if (approveDept == "" && 	Ext.ComponentMgr.get('approve-radio').getGroupValue()=='2122') {
//												Ext.Msg.alert("提示", "请选择部门!");
//												return false;
//											}
											// 显示用户验证窗口
											workerPanel.getForm().reset();
											validateWin.show();
										}
									}, {
										text : '取消',
										handler : function() {
											approveDept = "";
											window.location.reload();
											window.close();
										}
									}]

						}, {
							columnWidth : 0.1,
							border : false
						}]
			});
	var viewport = new Ext.Viewport({
				layout : "fit",
				border : false,
				items : [approvePanel]
			});
	getCurrentSteps();
	// 签字时的Enter
	document.onkeydown = function() {
		if (event.keyCode == 13 && document.getElementById('btnSign')) {
			document.getElementById('btnSign').click();
		}
	}
});