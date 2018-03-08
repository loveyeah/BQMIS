Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
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
var hisInfo;
Ext.form.Radio.prototype.url = "";
function removeAppointNextRoles() {
	if (confirm("确定要清除指定下一步人吗？")) {
		nrs = "";
		Ext.get("showAppointNextRoles").dom.innerHTML = "";
	}
}
Ext.form.Label.prototype.setText = function(argText) {
	this.el.dom.innerHTML = argText;
}
Ext.onReady(function() {
	var formJson = window.dialogArguments;
	var entryId = formJson.data.workFlowNo;
	var workercode = document.getElementById('workCode').value;
	var eventIdentify;
	var day;
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

	// 图形展示
	var btnPictureDisplay = new Ext.Button({
		text : "图形展示",
		handler : function() {
			if (entryId == "" || entryId == null) {
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
	var btnRecord = new Ext.Button({
		text : "查看审批记录",
		handler : function() {
			if (entryId == "" || entryId == null) {
				Ext.Msg.alert("提示", "流程还未启动!");
			} else {
				var url = "/power/workflow/manager/approveInfo/approveInfo.jsp?entryId="
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
		handler : getNextSteps
	});

	// 标题label
	var lblApprove = new Ext.form.Label({
		width : '100%',
		id : 'lblApprove',
		text : '发电部待处理审批',
		height : 5,
		style : "font-size:30px;color:blue;line-height:50px;text-align:center"
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
	var delayTime = new Ext.form.TextField({
		fieldLabel : "申请延长日期到<font color='red'>*</font>",
		id : 'delayTime',
		name : "failurehis.delayDate",
		cls : 'Wdate',
		style : 'cursor:pointer',
		allowBlank : false,
		readOnly : true,
		value : getDate(),
		listeners : {
			focus : function() {
				WdatePicker({
					dateFmt : 'yyyy-MM-dd HH:mm:ss'
				});
			}
		}
	});
	var stylePanel = new Ext.Panel({
		height : 60,
		border : false,
		style : "padding-top:10;padding-left:2px;border-width:1px 0 0 0;",
		items : [lblApproveStyle, radioSet]
	});

	// string.property.trim =

	// 获取审批方式
	function getCurrentSteps() {
		Ext.Ajax.request({
			url : "MAINTWorkflow.do?action=getCurrentSteps",
			method : 'post',
			params : {
				entryId : entryId,
				workerCode : workercode
			// jsonArgs : "{'delayDays':'" + delayDays + "'}"
			},
			success : function(result, request) {
				var radio = eval('(' + result.responseText + ')');
				var actions = radio[0].actions;
				radioAddHandler(actions);
			},
			failure : function() {
			}
		});

	};
	function getNextSteps() {
		// var actionId = Ext.ComponentMgr.get('approve-radio').getGroupValue();
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
				var _radio = new Ext.form.Radio({
					boxLabel : "<font size='2.6px'>" + radio[0].actionName
							+ "</font>",
					id : 'approve-radio' + radio[0].actionId,
					name : 'rb-approve',
					inputValue : radio[0].actionId,
					eventIdentify : radio[0].changeBusiStateTo,
					url : radio[0].url,
					listeners : {
						'check' : function() {
							var _actionId = Ext.ComponentMgr
									.get('approve-radio' + radio[0].actionId)
									.getGroupValue();
							var obj = Ext.getCmp('approve-radio' + _actionId);
							actionId = _actionId;
							eventIdentify = obj.eventIdentify;
							if (eventIdentify == "TH") {
								resultComboBox.setValue("2");
								delayTime.setDisabled(true);
								delayTime.setValue("");
							} else {
								resultComboBox.setValue("1");
								delayTime.setDisabled(false);
								delayTime.setValue(hisInfo.data.delayDate);
							}
						}
					},
					checked : true
				});
				// _radio.setUrl(radio[0].url);
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

					// _radio.setUrl(radio[i].url);
					radioSet.items.add(_radio);
					// 添加隐藏域，为了使radio正常显示
					radioSet.items.add(new Ext.form.Hidden());
				}
			}

		}
		radioSet.doLayout();
	}
	var approveField = new Ext.Panel({
		id : "approveField",
		autoWidth : true,
		height : 50,
		style : "padding-top:10;padding-bottom:10;border-width:1px 0 0 0;",
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
	var appField = new Ext.Panel({
		autoHeight : true,
		labelAlign : 'right',
		anchor : '100%',
		border : false,
		items : [{
			layout : 'column',
			border : true,
			items : [{
				layout : 'form',
				columnWidth : 1,
				border : false,
				style : "color:blue;line-height:50px;font-size:30px;text-align:center;",
				items : [lblApprove]
			}, {
				columnWidth : 0.185,
				id : "tiemcheck",
				layout : "form",
				hideLabels : true,
				style : "padding-top:10;padding-bottom:10;border-width:1px 0 0 0;",
				height : 50,
				border : false,
				items : [timeSet]
			}, {
				columnWidth : 0.265,
				hideLabels : true,
				style : "padding-top:10;padding-bottom:10;border-width:1px 0 0 0;",
				height : 50,
				layout : "form",
				border : false,
				items : [timeDisplay]
			}, {
				columnWidth : 0.15,
				layout : "form",
				height : 50,
				hideLabels : true,
				style : "padding-top:10;padding-bottom:10;border-width:1px 0 0 0;",
				border : false,
				items : [btnPictureDisplay]
			}, {
				columnWidth : 0.19,
				layout : "form",
				height : 50,
				hideLabels : true,
				style : "padding-top:10;padding-bottom:10;border-width:1px 0 0 0;",
				border : false,
				items : [btnRecord]
			}, {
				columnWidth : 0.21,
				layout : "form",
				height : 50,
				hideLabels : true,
				style : "padding-top:10;padding-bottom:10;border-width:1px 0 0 0;",
				border : false,
				items : [btnNextDisplay]
			}, {
				columnWidth : 1,
				layout : "form",
				hideLabels : true,
				border : false,
				items : [showAppointNextRoles]
			}, {
				columnWidth : 1,
				layout : 'form',
				border : false,
				anchor : '95%',
				items : [stylePanel]
			}]
		}]
	});
	var awaitTypeComboBox = new Ext.form.ComboBox({
		id : 'awaitType-combobox',
		store : new Ext.data.SimpleStore({
			fields : ["retrunValue", "displayText"],
			data : [['1', '无备品'], ['2', '等待停机处理'], ['3', '其它']]
		}),
		fieldLabel : '延期待处理类别',
		valueField : "retrunValue",
		displayField : "displayText",
		mode : 'local',
		forceSelection : true,
		hiddenName : 'failurehis.awaitType',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : false,
		anchor : '90%'
	});
	awaitTypeComboBox.on('beforequery', function() {
		return false
	});
	var resultComboBox = new Ext.form.ComboBox({
		id : 'resultComboBox',
		store : new Ext.data.SimpleStore({
			fields : ["retrunValue", "displayText"],
			data : [['1', '同意延期'], ['2', '不同意延期，退回']]
		}),
		fieldLabel : '处理结果',
		valueField : "retrunValue",
		displayField : "displayText",
		mode : 'local',
		forceSelection : true,
		hiddenName : 'failurehis.tackleResult',
		value : '1',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : false,
		anchor : '90%'
	});
	resultComboBox.on('beforequery', function() {
		return false
	});
	var delayTime = new Ext.form.TextField({
		fieldLabel : "申请延长日期到",
		id : 'delayTime',
		name : "failurehis.delayDate",
		cls : 'Wdate',
		style : 'cursor:pointer',
		anchor : "90%",
		readOnly : true,
		listeners : {
			focus : function() {
				WdatePicker({
					dateFmt : 'yyyy-MM-dd HH:mm:ss'
				});
			}
		}
	});
	var awaitField = new Ext.form.FieldSet({
		title : '缺陷延期待处理',
		autoWidth : true,
		autoHeight : true,
		anchor : '100%',
		items : [{
			layout : "column",
			border : false,
			items : [{
				columnWidth : 0.5,
				layout : "form",
				border : false,
				items : [{
					xtype : "textfield",
					fieldLabel : "申请人",
					name : "approve_people_name",
					readOnly : true,
					anchor : "90%"
				}]
			}, {
				columnWidth : 0.5,
				layout : "form",
				border : false,
				items : [{
					xtype : "textfield",
					fieldLabel : "申请日期",
					name : "approve_time",
					readOnly : true,
					anchor : "90%"
				}]
			}]
		}, {
			layout : "column",
			border : false,
			items : [{
				columnWidth : 0.5,
				layout : "form",
				border : false,
				items : [{
					xtype : "textfield",
					fieldLabel : "延期待处理日期",
					name : "bugCode_text",
					readOnly : true,
					value : getDate(),
					anchor : "90%"
				}]
			}, {
				columnWidth : 0.5,
				layout : "form",
				border : false,
				items : [awaitTypeComboBox]
			}]
		}, {
			layout : "column",
			border : false,
			items : [{
				columnWidth : 0.5,
				layout : "form",
				border : false,
				items : [resultComboBox]
			}, {
				columnWidth : 0.5,
				layout : "form",
				border : false,
				items : [delayTime]
			}]
		}, {
			layout : "column",
			border : false,
			items : [{
				columnWidth : 1,
				layout : "form",
				border : false,
				items : [{
					xtype : "textarea",
					fieldLabel : "申请延期原因",
					name : "apply_reason",
					anchor : "94%",
					readOnly : true,
					height : 80
				}]
			}]
		}, {
			layout : "column",
			border : false,
			items : [{
				columnWidth : 1,
				layout : "form",
				border : false,
				items : [{
					xtype : "textarea",
					fieldLabel : "延期待处理意见<font color='red'>*</font>",
					name : "failurehis.approveOpinion",
					anchor : "94%",
					allowBlank : false,
					height : 80
				}]
			}]
		}, {
			layout : "column",
			border : false,
			items : [{
				columnWidth : 0.5,
				layout : "form",
				border : false,
				items : [{
					xtype : 'textfield',
					fieldLabel : '工号',
					name : 'failurehis.approvePeople',
					allowBlank : false,
					readOnly : true,
					value : workercode,
					anchor : "90%"
				}]
			}, {
				columnWidth : 0.5,
				layout : "form",
				border : false,
				items : [{
					xtype : 'textfield',
					fieldLabel : "密码<font color='red'>*</font>",
					name : 'confirmPwd',
					inputType : 'password',
					allowBlank : false,
					anchor : "90%"
				}]
			}]
		}]
	});
	var failureApproveFormPanel = new Ext.FormPanel({
		layout : "column",
		title : "缺陷延期待处理",
		border : false,
		items : [{
			columnWidth : 0.1,
			border : false
		}, {
			columnWidth : 0.8,
			border : false,
			layout : 'form',
			buttonAlign : 'center',
			items : [appField, awaitField],
			buttons : [{
				id : 'btnFailureHandle',
				xtype : 'button',
				text : "确定",
				iconCls : 'save',
				handler : function() {
					if (failureApproveFormPanel.getForm().isValid()) {
						failureApproveFormPanel.getForm().submit({
							url : 'bqfailure/shiftawaitFailure.action?',
							params : {
								actionId : actionId,
								nextRoles : nrs,
								"failure.id" : formJson.data.id,
								eventIdentify : eventIdentify
							},
							waitMsg : '正在处理数据...',
							method : 'post',
							success : function(result, request) {
								Ext.Msg.alert("处理成功！");
								window.close();
							},
							failure : function(form, action) {
								Ext.Msg.alert("处理失败",
										action.result.errorMessage);
							}
						});
					}
				}
			}, {
				id : 'btnFailureCancel',
				xtype : 'button',
				text : "取消",
				iconCls : 'cancer',
				handler : function() {
					window.close();
				}
			}]
		}, {
			columnWidth : 0.1,
			border : false
		}]
	});
	// **************************************************************************************//
	var failField = new Ext.form.FieldSet({
		title : '缺陷审批',
		autoHeight : true,
		anchor : '100%',
		items : [{
			layout : "column",
			border : false,
			items : [{
				name : 'id',
				xtype : 'hidden'
			}, {
				columnWidth : 0.3,
				layout : "form",
				border : false,
				items : [{
					xtype : "textfield",
					fieldLabel : "发现人",
					name : "findByName",
					readOnly : true,
					anchor : "95%"
				}]
			}, {
				columnWidth : 0.3,
				layout : "form",
				border : false,
				items : [{
					xtype : "textfield",
					fieldLabel : "发现部门",
					name : "findDeptName",
					readOnly : true,
					anchor : "95%"
				}]
			}, {
				columnWidth : 0.3,
				layout : "form",
				border : false,
				items : [{
					xtype : "textfield",
					fieldLabel : "发现时间",
					name : "findDate",
					readOnly : true,
					anchor : "95%"
				}]
			}]
		}, {
			layout : "column",
			border : false,
			items : [{
				columnWidth : 0.9,
				layout : "form",
				border : false,
				items : [{
					xtype : "textfield",
					fieldLabel : "设备名称",
					name : "equName",
					allowBlank : false,
					readOnly : true,
					anchor : "98.2%"
				}]
			}]
		}, {
			layout : "column",
			border : false,
			items : [{
				columnWidth : 0.3,
				layout : "form",
				border : false,
				items : [{
					xtype : "textfield",
					fieldLabel : "所属系统",
					name : "belongSystemName",
					allowBlank : false,
					readOnly : true,
					anchor : "95%"
				}]
			}, {
				columnWidth : 0.3,
				layout : "form",
				border : false,
				items : [{
					xtype : "textfield",
					fieldLabel : "故障类型",
					name : "bugName",
					readOnly : true,
					allowBlank : false,
					anchor : "95%"
				}]
			}, {
				columnWidth : 0.3,
				layout : "form",
				border : false,
				items : [{
					xtype : "textfield",
					fieldLabel : "当前状态",
					value : '未上报',
					name : "woStatusName",
					readOnly : true,
					anchor : "95%"
				}]
			}]
		}, {
			layout : "column",
			border : false,
			items : [{
				columnWidth : 0.3,
				layout : "form",
				border : false,
				items : [{
					xtype : "textfield",
					fieldLabel : "运行专业",
					name : "runProfessionName",
					readOnly : true,
					anchor : "95%"
				}]
			}, {
				columnWidth : 0.3,
				layout : "form",
				border : false,
				items : [{
					xtype : "textfield",
					fieldLabel : "是否限制运行",
					name : "ifStopRunName",
					readOnly : true,
					value : '否',
					anchor : "95%"
				}]
			}, {
				columnWidth : 0.3,
				layout : "form",
				border : false,
				items : [{
					xtype : "textfield",
					fieldLabel : "管辖专业",
					name : "dominationProfessionName",
					readOnly : true,
					value : '否',
					anchor : "95%"
				}]
			}]
		}, {
			layout : "column",
			border : false,
			items : [{
				columnWidth : 0.3,
				layout : "form",
				border : false,
				items : [{
					xtype : "textfield",
					fieldLabel : "缺陷类别",
					name : "failuretypeName",
					readOnly : true,
					anchor : "95%"
				}]
			}, {
				columnWidth : 0.3,
				layout : "form",
				border : false,
				items : [{
					xtype : "textfield",
					fieldLabel : "优先级",
					name : "failurePri",
					readOnly : true,
					anchor : "95%"
				}]
			}, {
				columnWidth : 0.3,
				layout : "form",
				border : false,
				items : [{
					xtype : "textfield",
					fieldLabel : "检修部门",
					name : "repairDepName",
					readOnly : true,
					anchor : "95%"
				}]
			}]
		}, {
			layout : "column",
			border : false,
			items : [{
				columnWidth : 0.9,
				layout : "form",
				border : false,
				items : [{
					xtype : "textarea",
					fieldLabel : "未消除前措施",
					name : "preContent",
					readOnly : true,
					anchor : "98%",
					height : 55
				}]
			}]
		}, {
			layout : "column",
			border : false,
			items : [{
				columnWidth : 0.9,
				layout : "form",
				border : false,
				items : [{
					xtype : "textarea",
					fieldLabel : "缺陷内容",
					name : "failureContent",
					anchor : "98%",
					allowBlank : false,
					readOnly : true,
					height : 55
				}]
			}]
		}, {
			layout : "column",
			border : false,
			items : [{
				columnWidth : 0.9,
				layout : "form",
				border : false,
				items : [{
					xtype : "textarea",
					fieldLabel : "可能原因",
					name : "likelyReason",
					anchor : "98%",
					readOnly : true,
					height : 55
				}]
			}]
		}, {
			layout : "column",
			border : false,
			items : [{
				columnWidth : 0.3,
				layout : "form",
				border : false,
				items : [{
					xtype : "textfield",
					fieldLabel : "填写人",
					name : "writeByName",
					readOnly : true,
					anchor : "95%"
				}]
			}, {
				layout : "column",
				border : false,
				items : [{
					columnWidth : 0.3,
					layout : "form",
					border : false,
					items : [{
						xtype : "textfield",
						fieldLabel : "是否电话通知",
						id : 'isTel',
						name : "isTel",
						readOnly : true,
						anchor : "95%"
					}]
				}, {
					columnWidth : 0.3,
					layout : "form",
					border : false,
					items : [{
						xtype : "textfield",
						fieldLabel : "电话通知人",
						id : 'telManName',
						name : "telManName",
						readOnly : true,
						anchor : "95%"
					}]
				}, {
					columnWidth : 0.3,
					layout : "form",
					border : false,
					items : [{
						xtype : "textfield",
						fieldLabel : "电话通知时间",
						id : 'telTime',
						name : "telTime",
						readOnly : true,
						anchor : "95%"
					}]
				}]
			}, {
				layout : "column",
				border : false,
				items : [{
					columnWidth : 0.3,
					layout : "form",
					border : false,
					items : [{
						xtype : "textfield",
						fieldLabel : "是否短信通知",
						id : 'isMessage',
						name : "isMessage",
						readOnly : true,
						anchor : "95%"
					}]
				}, {
					columnWidth : 0.3,
					layout : "form",
					border : false,
					items : [{
						xtype : "textfield",
						fieldLabel : "是否产生工单",
						id : 'ifOpenWorkorderName',
						name : "ifOpenWorkorderName",
						readOnly : true,
						anchor : "95%"
					}]
				}, {
					columnWidth : 0.3,
					layout : "form",
					border : false,
					items : [{
						xtype : "textfield",
						fieldLabel : "是否点检系统产生",
						id : 'isCheck',
						name : "isCheck",
						readOnly : true,
						anchor : "95%"
					}]
				}]
			}, {
				columnWidth : 0.3,
				layout : "form",
				border : false,
				items : [{
					xtype : "textfield",
					fieldLabel : "填写部门",
					name : "writeDeptName",
					readOnly : true,
					anchor : "95%"
				}]
			}, {
				columnWidth : 0.3,
				layout : "form",
				border : false,
				items : [{
					xtype : "textfield",
					fieldLabel : "填写时间",
					name : "writeDate",
					readOnly : true,
					anchor : "95%"
				}]
			}]
		}, {
			layout : "column",
			border : false,
			items : [{
				columnWidth : 0.3,
				layout : "form",
				border : false,
				items : [{
					xtype : "textfield",
					fieldLabel : "认领人",
					name : "cliamByName",
					readOnly : true,
					anchor : "95%"
				}]
			}, {
				columnWidth : 0.3,
				layout : "form",
				border : false,
				items : [{
					xtype : "textfield",
					fieldLabel : "认领时间",
					name : "cliamDate",
					readOnly : true,
					anchor : "95%"
				}]
			}]
		}]
	});

	var failureFormPanel = new Ext.FormPanel({
		title : '缺陷信息',
		border : false,
		items : [failField]
	});
	var Historyoperation = Ext.data.Record.create([{
		name : 'id'
	}, {
		name : 'entryId'
	}, {
		name : 'stepId'
	}, {
		name : 'stepName'
	}, {
		name : 'actionId'
	}, {
		name : 'actionName'
	}, {
		name : 'caller'
	}, {
		name : 'callerName'
	}, {
		name : 'opinion'
	}, {
		name : 'opinionTime'
	}]);

	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'MAINTWorkflow.do?action=getApproveList&entryId='
					+ formJson.data.workFlowNo,
			method : 'post'
		}),
		reader : new Ext.data.JsonReader({}, Historyoperation)
	});
	ds.load();

	var cm = new Ext.grid.ColumnModel([{
		header : '状态',
		dataIndex : 'stepName',
		align : 'left',
		width : 100
	}, {
		header : '执行动作',
		dataIndex : 'actionName',
		align : 'left',
		width : 100
	}, {
		header : '执行人',
		dataIndex : 'callerName',
		align : 'left',
		width : 100
	}, {
		header : '执行时间',
		dataIndex : 'opinionTime',
		align : 'left',
		width : 100
	}, {
		header : '审批意见',
		dataIndex : 'opinion',
		align : 'left',
		width : 200,
		renderer : function renderName(value, metadata, record) {
			metadata.attr = 'style="white-space:normal;"';
			return value;
		}
	}]);
	cm.defaultSortable = false;
	var grid = new Ext.grid.GridPanel({
		ds : ds,
		cm : cm,
		title : '审批信息列表',
		autoWidth : true,
		layout : 'fit',
		height : 380,
		fitToFrame : true,
		border : false
	});
	grid.enableColumnHide = false;

	var failureTab = new Ext.TabPanel({
		activeTab : 0,
		layoutOnTabChange : true,
		items : [failureFormPanel, grid, failureApproveFormPanel]
	})

	var viewport = new Ext.Viewport({
		layout : "fit",
		items : [failureTab]
	});
	failureFormPanel.getForm().loadRecord(formJson);
	Ext.get('isMessage').dom.value=formJson.data.isMessage == "Y"? "是":"否";
	Ext.get('isTel').dom.value=formJson.data.isTel == "Y"? "是":"否";
	Ext.get('isCheck').dom.value=formJson.data.isCheck == "Y"? "是":"否";
	failureTab.activate(failureApproveFormPanel);
	Ext.Ajax.request({
		url : 'bqfailure/getApplyType.action',
		params : {
			applyType : '11',
			'failure.id' : formJson.data.id
		},
		method : 'post',
		waitMsg : '正在加载数据...',
		success : function(result, request) {
			// var responseArray = Ext.util.JSON.decode(result.responseText);
			var responseArray = eval('(' + result.responseText + ')');
			if (responseArray.success == true) {
				hisInfo = responseArray;
				if (hisInfo.data == null || hisInfo.data == "") {
					Ext.MessageBox.alert('提示', '历史信息获取错误!');
					return false;
				} else {
					getCurrentSteps();
					Ext.get('approve_people_name').dom.value = hisInfo.data.approvePeopleName;
					Ext.get('approve_time').dom.value = hisInfo.data.approveTime;
					Ext.get('apply_reason').dom.value = hisInfo.data.approveOpinion;
					Ext.get('delayTime').dom.value = hisInfo.data.delayDate;
					awaitTypeComboBox.setValue(hisInfo.data.awaitType);
					var claimDate = formJson.data.cliamDate;
					var delayDate = hisInfo.data.delayDate;
					var type = formJson.data.failuretypeCode;
					var applyDate=hisInfo.data.approveTime;
					day=getBetweenDays(applyDate ,delayDate);
//					if (type == "2") {
//						day = getBetweenDays(claimDate, delayDate) - 3;
//					} else if (type == "3") {
//						day = getBetweenDays(claimDate, delayDate) - 1;
//					} else {
//						day = getBetweenDays(claimDate, delayDate);
//					}
				}

			} else {
				Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
			}
		},
		failure : function(result, request) {
			Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
		}
	});

});