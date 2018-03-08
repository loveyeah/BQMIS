Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
var nrs = "";
function removeAppointNextRoles() {
	if (confirm("确定要清除指定下一步人吗？")) {
		nrs = "";
		Ext.get("showAppointNextRoles").dom.innerHTML = "";
	}
}
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
var prjNo;
Ext.onReady(function() {
	var arg = window.dialogArguments
	// 取得项目编号
	prjNo = arg.prjNo;
	var prjNos=arg.prjNos;// add by sychen 20100707
	var entryId = arg.entryId;
	//update by sychen 20100628
	var selectFlag = arg.selectFlag;
	
	if (selectFlag == "many"){
	
		var entryId2 = entryId.substring(0, 6);// add by sychen 20100407
	}
	else{
		var entryId2 = entryId;
		}
	//update by sychen 20100628
	var workflowType = arg.workflowType;
	var prjTypeId = arg.prjTypeId;
	var ifDiversify = arg.ifDiversify;//add by sychen 20100329
	var ifHeating = arg.ifHeating;//add by sychen 20100329
//	var isEqu = arg.isEqu;
	var prjStatus = arg.prjStatus ? arg.prjStatus : '';

	var workerCode = "";
	// 定义url，根据工作流类型选择所走的action
	var urlString;
	function getUrl(workflowType) {
		switch (workflowType) {
//			case 'bqDeptJobplan' :
			case 'bqDeptWorkPlanApprove' : //update by sychen 20100327
				return "manageplan/workPlanApprove.action";

			case 'bqDeptJobplangather' :
				return "manageplan/workPlanGatherApprove.action";
			default :
				return "";
		}

	}

	urlString = getUrl(workflowType);

	// ↓↓*******************工程项目上报**************************************
	// 显示指定下一步角色
	var showAppointNextRoles = new Ext.form.Label({
				border : false,
				id : 'showAppointNextRoles',
				style : "color:red;font-size:12px"
			});

	var label = new Ext.form.Label({
		id : "label",
		text : '相关领导审批',
		style : "color:blue;font-size:30px;line-height:100px;padding-left:155px;font-weight:bold"
	});

	var titleField = new Ext.Panel({
				border : false,
				height : 120,
				items : [label],
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
							Ext.getCmp('responseDate').setVisible(true);
						} else {
							Ext.getCmp('responseDate').setVisible(false);
						}
					}
				}
			});

	// 时间选择
	var datePicker = new Ext.form.TextField({
				style : 'cursor:pointer',
				id : "responseDate",
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
					var url;//update by sychen 20100407
					if (entryId2 == null || entryId2 == "")
						url = "/power/workflow/manager/flowshow/flowshow.jsp?flowCode="
								+ workflowType;
					else
						url = url = "/power/workflow/manager/show/show.jsp?entryId="
								+ entryId2;
					window.open(url);
				}
			});

	// 查看审批记录
	var approveHistoryButton = new Ext.Button({
		text : "查看审批记录",
		handler : function() {
			var url = "/power/workflow/manager/approveInfo/approveInfo.jsp?entryId="
					+ entryId2;//update by sychen 20100407
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
		args.flowCode = workflowType;
		args.actionId = actionId;
		args.entryId = entryId2;//update by sychen 20100407
		var url = "";
		if (entryId2 == null || entryId2 == "") {
			url = "/power/workflow/manager/apponitNextPerson/appointNextPersonForReport.jsp";
		} else {
			url = "/power/workflow/manager/apponitNextPerson/apponitNextPerson.jsp";
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
				autoWidth : true,
				anchor : '100%',
				style : "padding-top:20;padding-bottom:20;border-width:1px 0 0 0;",
				layout : "column",
				items : [ // 设置响应时间checkbox
				{
							columnWidth : 0.15,
							id : "tiemcheck",
							layout : "form",
							hideLabels : true,
							border : false,
							items : [timeCheck]
						},// 时间选择
						{
							columnWidth : 0.23,
							hideLabels : true,
							layout : "form",
							border : false,
							items : [datePicker]
						}, {
							columnWidth : 0.02,
							border : false
						},// 图形显示按钮
						{
							columnWidth : 0.1,
							layout : "form",
							hideLabels : true,
							border : false,
							items : [graphButton]
						},// 查看审批记录按钮
						{
							columnWidth : 0.16,
							layout : "form",
							hideLabels : true,
							border : false,
							items : [approveHistoryButton]
						},// 下一步按钮
						{
							columnWidth : 0.16,
							layout : "form",
							hideLabels : true,
							border : false,
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
				//update by sychen 20100707
				
				//add by sychen 20100628
//				if(selectFlag=="single"){
//				  rdFlag=false;
//				  disFlag=true;
//				}
//				else{
//				  rdFlag=true;
//				  disFlag=false;
//				  }
				// add by sychen 20100628
				
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
									eventIdentify = obj.eventIdentify;
								}
							},
							checked : true
//							checked : rdFlag
//							,//add by sychen 20100628
//							disabled:disFlag//add by sychen 20100628
						});
				radioSet.items.add(_radio);
				// 添加隐藏域，为了使radio正常显示
				radioSet.items.add(new Ext.form.Hidden());

				for (var i = 1; i < radio.length; i++) {
				//update by sychen 20100707
					
				//add by sychen 20100628	
//				if(selectFlag=="single"){
//				  rdFlag=true;
//				  if(radio[i].changeBusiStateTo=="TH")
//				   disFlag=false;
//				  else 
//				   disFlag=true;
//				}
//				else{
//				  rdFlag=false;
//				  if(radio[i].changeBusiStateTo=="TH")
//				     disFlag=true;
//				  else 
//				     disFlag=false;
//				}
				//add by sychen 20100628 end 
				
					// 添加隐藏域，为了使radio正常显示
					radioSet.items.add(new Ext.form.Hidden());
					radioSet.items.add(new Ext.form.Radio({
								boxLabel : "<font size='2.6px'>"
										+ radio[i].actionName + "</font>",
								id : 'approve-radio' + radio[i].actionId,
								name : 'rb-approve',
								eventIdentify : radio[i].changeBusiStateTo,
								inputValue : radio[i].actionId,
								listeners : {
									check :  function() {
									var _actionId = Ext.ComponentMgr
											.get('approve-radio'
													+ radio[0].actionId)
											.getGroupValue();
									var obj = Ext.getCmp('approve-radio'
											+ _actionId);
									actionId = _actionId;
									eventIdentify = obj.eventIdentify;
								}
									
								}
//								,	//update by sychen 20100707
//							checked : rdFlag
//							,//add by sychen 20100628
//							disabled:disFlag//add by sychen 20100628
							}));
//					var _radio = new Ext.form.Radio({
//								boxLabel : "<font size='2.6px'>"
//										+ radio[i].actionName + "</font>",
//								id : 'approve-radio' + radio[i].actionId,
//								name : 'rb-approve',
//								url : radio[i].url,
//								eventIdentify : radio[i].changeBusiStateTo,
//								inputValue : radio[i].actionId
//							});
//
//					radioSet.items.add(_radio);
					// 添加隐藏域，为了使radio正常显示
					radioSet.items.add(new Ext.form.Hidden());
				}
			}

		}
		radioSet.doLayout();
	};

	// 载入审批方式
	function getCurrentSteps() {
//--------update by sychen 20100329----------//
//		var equFlg = false;
//		if (isEqu != null && isEqu != "") {
//			if (isEqu == "Y") {
//				equFlg = true;
//			}
//		}
		var flg = true;  
		if (ifDiversify == 'N') {
			flg = false;
		}
		var flag = true;
		if (ifHeating == 'N') {
			flag = false;
		}
//--------------------------------------//		

		Ext.Ajax.request({
					url : "MAINTWorkflow.do?action=getCurrentSteps",
					method : 'post',
					params : {
						entryId : entryId2,//update by sychen 20100407
						workerCode : workerCode,
//						jsonArgs : "{'SB':" + equFlg + "}"   //update by sychen 20100329
						jsonArgs : "{'IF_DIVERSIFY':" + flg + ",'IF_HEATING':" + flag + "}"
					},
					success : function(result, request) {
						var radio = eval('(' + result.responseText + ')');
						radioAddHandler(radio[0].actions);
						signPanel.setTitle(radio[0].stepName);
						label.setText(radio[0].stepName);
					},
					failure : function() {
					}
				});
	};

	getCurrentSteps();

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
				name : 'approveText',
				isFormField : true,
				border : false,
				value : "",
				height : 90
			})
	// 第三行
	var remarkSet = new Ext.Panel({
				height : 120,
				border : false,
				style : "padding-top:10;border-width:1px 0 0 0;",
				items : [backLabel, remark]
			});

	// 工作票终结签字tab表单
	var form = new Ext.form.FormPanel({
				columnWidth : 0.8,
				border : true,
				labelAlign : 'center',
				buttonAlign : 'right',
				buttons : [{
							text : Constants.CONFIRM,
							handler : function() {//update by sychen 20100407
							//add by sychen 20100707 单个退回方法
							if(eventIdentify==('TH')&& selectFlag == "many"){
				               Ext.Msg.alert("提示", "请选择要退回的数据行!");
				               return;
							}
							if(eventIdentify==('TH')&& selectFlag == "single"){
				               prjNos=prjNo;
							}
						    // add by sychen 20100707 end 				
								
								if(prjStatus==1){
									Ext.Msg.confirm('提示', '本部门月度计划确认已完善？', function(id) {
			                         if (id == 'yes') {// 显示用户验证窗口
			                         	if(eventIdentify==('TY（ZZGCLD）')){//add by sychen 20100413
			                         	  if (nrs == "") {
										    Ext.Msg.alert("提示", "请选择主管厂领导具体人员!");
										    return false;
									       }
									      else{
								          workerPanel.getForm().reset();
								          validateWin.show();
			                           }   
									 }
			                         else{
								          workerPanel.getForm().reset();
								          validateWin.show();
			                           }
								      }
								    })
								}
								else if(prjStatus==2){
									Ext.Msg.confirm('提示', '本公司月度计划确认已完善？', function(id) {
			                         if (id == 'yes') {// 显示用户验证窗口
								         workerPanel.getForm().reset();
								         validateWin.show();
								        }
								     })
								}
								else if(prjStatus==6){
									Ext.Msg.confirm('提示', '生产月度计划确认已完善？', function(id) {
			                         if (id == 'yes') {// 显示用户验证窗口
								         workerPanel.getForm().reset();
								         validateWin.show();
								        }
								     })
								}else {
								// 显示用户验证窗口
								workerPanel.getForm().reset();
								validateWin.show();
								}
							}
						}, {
							text : Constants.BTN_CANCEL,
							handler : function() {
								layout.hidden = true;
								window.close();
							}
						}],
				items : [titleField, tableField, approveSet,
						showAppointNextRoles, remarkSet]
			});

	// 工作票终结签字tab
	var signPanel = new Ext.Panel({
				layout : "column",
				autoHeight : false,
				width : 950,
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

	// 工作票状态选择
	var checkSet = new Ext.form.FieldSet({
				layout : 'column',
				autoHeight : true,
				items : [{
							items : new Ext.form.Radio({
										id : 'printMove',

										boxLabel : '运行联',
										name : 'printChoice',
										inputValue : 'm',
										checked : true
									})
						}, {
							items : new Ext.form.Radio({
										id : 'printRepair',
										boxLabel : '检修联',
										name : 'printChoice',
										inputValue : 'r'
									})
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
								if (result) {
									form.getForm().submit({
										url : urlString,
										waitMsg : '正在保存数据...',
										method : 'post',
										params : {
//											prjNo : prjNo,//update by sychen 20100707
											prjNo : prjNos,
											nextRoles : nrs,
											actionId : actionId,
											workflowType : workflowType,
											eventIdentify : eventIdentify,
											stepId : prjStatus,
											ifDiversify:ifDiversify,//add by sychen 20100329
											ifHeating:ifHeating //add by sychen 20100329
										},
										success : function(form, action) {
											var result = eval("("
													+ action.response.responseText
													+ ")");
											// 弹出返回信息
											Ext.Msg.alert(Constants.NOTICE,
													result.msg, function() {
														// add by fyyang 090106
														// 审批成功时才关闭审批窗口
														if (result.msg
																.indexOf('成功') != -1) {
															window.returnValue = true;
															window.close();
														}

														validateWin.hide();
													});
										},
										failure : function() {
											Ext.Msg.alert(Constants.ERROR,
													Constants.UNKNOWN_ERR);
										}

									});

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
				// 取得默认工号
				Ext.lib.Ajax.request('POST',
						'comm/getCurrentSessionEmployee.action', {
							success : function(action) {
								var result = eval("(" + action.responseText
										+ ")")
								if (result.workerCode) {
									// 设定默认工号
									workCode.setValue(result.workerCode);
								}
							}
						});
			}
		},
		closeAction : 'hide'
	});
	// ↑↑****************员工验证窗口****************

	// tab
	var tabPanel = new Ext.TabPanel({
				renderTo : document.body,
				activeTab : 0,
				tabPosition : 'top',
				id : "tabPanel",
				layoutOnTabChange : true,
				plain : true,
				frame : false,
				border : false,
				items : [signPanel]
			});

	// 显示区域
	var layout = new Ext.Viewport({
		layout : 'fit',
		border : false,
		height : 1050,
		width : 600,
		items : [{
					layout : 'fit',
					margins : '0 0 0 0',
					region : 'center',
					// autoScroll : true,
					items : [tabPanel]
				}]
			// ,
			// defaults : {
			// autoScroll : true
			// }
		});

	// add by fyyang 090106
	// 签字时的Enter
	document.onkeydown = function() {
		if (event.keyCode == 13 && document.getElementById('btnSign')) {
			document.getElementById('btnSign').click();
		}
	}
});
