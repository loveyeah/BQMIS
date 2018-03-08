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
	var mytitle = "工作负责人终结";
	var actionId = "";
	var eventIdentify = "";
	var workerCode = "";

	var ifSeeDanger = false;
	var fireLevelId = arg.fireLevelId;
	// ↓↓*******************停送电申请单上报页面**************************************

	// 停送电申请单上报label
	var label = new Ext.form.Label({
		id : "label",
		text : mytitle,
		style : "color:blue;font-size:30px;line-height:80px;padding-left:150px;font-weight:bold"
	});
	// 标题
	var titleField = new Ext.Panel({
		border : false,
		height : 60,
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
		// height : 70,
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
				// 加作废审批状态-----

				radioSet.items.add(new Ext.form.Hidden());
				_radio = new Ext.form.Radio({
					boxLabel : "<font size='2.6px'>未执行/作废</font>",
					id : 'approve-radio0',
					name : 'rb-approve',
					url : '',
					eventIdentify : 'ZF',
					inputValue : '0'
				});

				radioSet.items.add(_radio);
				// 添加隐藏域，为了使radio正常显示
				radioSet.items.add(new Ext.form.Hidden());
				// -----------------------

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
		name : 'endHisModel.approveText',
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
		items : [backLabel, approveText]
	});

	// 安措办理
	var safetyExeLabel = new Ext.form.Label({
		text : " 安措办理：",
		style : 'color:red'
	});

	var safetyExePermitBy = {
		fieldLabel : '工作许可人',
		name : 'safetyExePermitBy',
		xtype : 'combo',
		id : 'safetyExePermitBy',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		hiddenName : 'safetyExeHisModel.oldChargeBy',
		editable : false,
		anchor : "80%",
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
			var args = {
				selectModel : 'single',
				notIn : "'999999'",
				rootNode : {
					id : '-1',
					text : '合肥电厂'
				}
			}
			var emp = window
					.showModalDialog(
							url,
							args,
							'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');

			if (typeof(emp) != "undefined") {
				Ext.getCmp('safetyExePermitBy').setValue(emp.workerCode);
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('safetyExePermitBy'), emp.workerName);
			}
		}
	};

	var safetyExeDutyBy = {
		fieldLabel : '值班负责人',
		name : 'safetyExeDutyBy',
		xtype : 'combo',
		id : 'safetyExeDutyBy',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		hiddenName : 'safetyExeHisModel.dutyChargeBy',
		editable : false,
		anchor : "80%",
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
			var args = {
				selectModel : 'single',
				notIn : "'999999'",
				rootNode : {
					id : '-1',
					text : '合肥电厂'
				}
			}
			var emp = window
					.showModalDialog(
							url,
							args,
							'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');

			if (typeof(emp) != "undefined") {
				Ext.getCmp('safetyExeDutyBy').setValue(emp.workerCode);
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('safetyExeDutyBy'), emp.workerName);
			}
		}
	};

	var safetyExePanel = new Ext.Panel({
		// height : 40,
		id : 'safetyExePanel',
		border : false,
		style : "padding-top:10;border-width:1px 0 0 0;",
		layout : "column",
		labelWidth : 80,
		labelAlign : "right",
		items : [{
			columnWidth : 1,
			layout : "form",
			border : false,
			items : [safetyExeLabel]
		}, {
			columnWidth : 0.5,
			layout : "form",
			border : false,
			items : [safetyExePermitBy]
		}, {
			columnWidth : 0.5,
			layout : "form",
			border : false,
			items : [safetyExeDutyBy]
		}]
	});
	// -------------许可开工---------
	var permitLabel = new Ext.form.Label({
		text : " 许可开工：",
		style : 'color:red'
	});
	// 许可开工

	var permitBy = {
		fieldLabel : '许可人',
		name : 'permitBy',
		xtype : 'combo',
		id : 'permitBy',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		hiddenName : 'pemitHisModel.approveBy',
		editable : false,
		anchor : "80%",
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
			var args = {
				selectModel : 'single',
				notIn : "'999999'",
				rootNode : {
					id : '-1',
					text : '合肥电厂'
				}
			}
			var emp = window
					.showModalDialog(
							url,
							args,
							'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');

			if (typeof(emp) != "undefined") {
				Ext.getCmp('permitBy').setValue(emp.workerCode);
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('permitBy'), emp.workerName);
			}
		}
	};

	var permitChargeBy = {
		fieldLabel : '工作负责人',
		name : 'permitChargeBy',
		xtype : 'combo',
		id : 'permitChargeBy',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		hiddenName : 'pemitHisModel.oldChargeBy',
		editable : false,
		anchor : "80%",
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
			var args = {
				selectModel : 'single',
				notIn : "'999999'",
				rootNode : {
					id : '-1',
					text : '合肥电厂'
				}
			}
			var emp = window
					.showModalDialog(
							url,
							args,
							'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');

			if (typeof(emp) != "undefined") {
				Ext.getCmp('permitChargeBy').setValue(emp.workerCode);
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('permitChargeBy'), emp.workerName);
			}
		}
	};

	var permitDutyBy = {
		fieldLabel : '值班负责人',
		name : 'permitDutyBy',
		xtype : 'combo',
		id : 'permitDutyBy',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		hiddenName : 'pemitHisModel.dutyChargeBy',
		editable : false,
		anchor : "80%",
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
			var args = {
				selectModel : 'single',
				notIn : "'999999'",
				rootNode : {
					id : '-1',
					text : '合肥电厂'
				}
			}
			var emp = window
					.showModalDialog(
							url,
							args,
							'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');

			if (typeof(emp) != "undefined") {
				Ext.getCmp('permitDutyBy').setValue(emp.workerCode);
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('permitDutyBy'), emp.workerName);
			}
		}
	};

	var permitDate = new Ext.form.TextField({
		id : 'permitDate',
		fieldLabel : "许可开工时间",
		name : 'pemitHisModel.oldApprovedFinishDate',
		style : 'cursor:pointer',
		anchor : "80%",
		value : getDate(),
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

	var permitPanel = new Ext.Panel({
		// height : 40,
		border : false,
		style : "padding-top:10;border-width:1px 0 0 0;",
		layout : "column",
		labelWidth : 80,
		labelAlign : "right",
		items : [{
			columnWidth : 1,
			layout : "form",
			border : false,
			items : [permitLabel]
		}, {
			columnWidth : 0.5,
			layout : "form",
			border : false,
			items : [permitBy, permitChargeBy]
		}, {
			columnWidth : 0.5,
			layout : "form",
			border : false,
			items : [permitDate, permitDutyBy]
		}]
	});
	// ----------------------
	// -----------工作负责人变更--------
	// var changeLabel = new Ext.form.Label({
	// text : " 工作负责人变更：",
	// style : 'color:red'
	// });

	var changeLabel = new Ext.form.Checkbox({
		id : 'changeLabel',
		fieldLabel : '<font color="red" size=3>工作负责人变更</font>',
		listeners : {
			check : function() {
				// 选中状态
				if (changeLabel.checked) {

					Ext.get("oldChargeBy").dom.parentNode.parentNode.parentNode.style.display = '';
					Ext.get("changeSignBy").dom.parentNode.parentNode.parentNode.style.display = '';
					Ext.get("changeDate").dom.parentNode.parentNode.parentNode.style.display = '';
					Ext.get("newChargeBy").dom.parentNode.parentNode.parentNode.style.display = '';
					Ext.get("changeDutyBy").dom.parentNode.parentNode.parentNode.style.display = '';
					// 未选中状态
				} else {
					Ext.get("oldChargeBy").dom.parentNode.parentNode.parentNode.style.display = 'none';
					Ext.get("changeSignBy").dom.parentNode.parentNode.parentNode.style.display = 'none';
					Ext.get("changeDate").dom.parentNode.parentNode.parentNode.style.display = 'none';
					Ext.get("newChargeBy").dom.parentNode.parentNode.parentNode.style.display = 'none';
					Ext.get("changeDutyBy").dom.parentNode.parentNode.parentNode.style.display = 'none';
				}
			}
		}
	})

	var oldChargeBy = {
		fieldLabel : '原工作负责人',
		name : 'oldChargeBy',
		xtype : 'combo',
		id : 'oldChargeBy',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		hiddenName : 'changeChargeModel.oldChargeBy',
		editable : false,
		anchor : "80%",
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
			var args = {
				selectModel : 'single',
				notIn : "'999999'",
				rootNode : {
					id : '-1',
					text : '合肥电厂'
				}
			}
			var emp = window
					.showModalDialog(
							url,
							args,
							'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');

			if (typeof(emp) != "undefined") {
				Ext.getCmp('oldChargeBy').setValue(emp.workerCode);
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('oldChargeBy'), emp.workerName);
			}
		}
	};

	var newChargeBy = {
		fieldLabel : '现工作负责人',
		name : 'newChargeBy',
		xtype : 'combo',
		id : 'newChargeBy',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		hiddenName : 'changeChargeModel.newChargeBy',
		editable : false,
		anchor : "80%",
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
			var args = {
				selectModel : 'single',
				notIn : "'999999'",
				rootNode : {
					id : '-1',
					text : '合肥电厂'
				}
			}
			var emp = window
					.showModalDialog(
							url,
							args,
							'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');

			if (typeof(emp) != "undefined") {
				Ext.getCmp('newChargeBy').setValue(emp.workerCode);
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('newChargeBy'), emp.workerName);
			}
		}
	};

	var changeSignBy = {
		fieldLabel : '签发人',
		name : 'changeSignBy',
		xtype : 'combo',
		id : 'changeSignBy',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		hiddenName : 'changeChargeModel.approveBy',
		editable : false,
		anchor : "80%",
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
			var args = {
				selectModel : 'single',
				notIn : "'999999'",
				rootNode : {
					id : '-1',
					text : '合肥电厂'
				}
			}
			var emp = window
					.showModalDialog(
							url,
							args,
							'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');

			if (typeof(emp) != "undefined") {
				Ext.getCmp('changeSignBy').setValue(emp.workerCode);
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('changeSignBy'), emp.workerName);
			}
		}
	};

	var changeDutyBy = {
		fieldLabel : '值班负责人',
		name : 'changeDutyBy',
		xtype : 'combo',
		id : 'changeDutyBy',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		hiddenName : 'changeChargeModel.dutyChargeBy',
		editable : false,
		anchor : "80%",
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
			var args = {
				selectModel : 'single',
				notIn : "'999999'",
				rootNode : {
					id : '-1',
					text : '合肥电厂'
				}
			}
			var emp = window
					.showModalDialog(
							url,
							args,
							'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');

			if (typeof(emp) != "undefined") {
				Ext.getCmp('changeDutyBy').setValue(emp.workerCode);
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('changeDutyBy'), emp.workerName);
			}
		}
	};
	var changeDate = new Ext.form.TextField({
		id : 'changeDate',
		fieldLabel : "变更时间",
		name : 'changeChargeModel.oldApprovedFinishDate',
		style : 'cursor:pointer',
		anchor : "80%",
		value : getDate(),
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

	var changePanel = new Ext.Panel({
		// height : 40,
		id : 'changePanel',
		border : false,
		style : "padding-top:10;border-width:1px 0 0 0;",
		layout : "column",
		labelWidth : 80,
		labelAlign : "right",
		items : [{
			columnWidth : 1,
			layout : "form",
			border : false,
			labelWidth : 130,
			items : [changeLabel]
		}, {
			columnWidth : 0.5,
			layout : "form",
			border : false,
			items : [oldChargeBy, changeSignBy, changeDate]
		}, {
			columnWidth : 0.5,
			layout : "form",
			border : false,
			items : [newChargeBy, changeDutyBy]
		}]
	});
	// ------------------------------
	// -----工作票延期----------
	// var delayLabel = new Ext.form.Label({
	// text : " 工作票延期：",
	// style : 'color:red'
	// });

	var delayLabel = new Ext.form.Checkbox({
		id : 'delayLabel',
		fieldLabel : '<font color="red" size=3>工作票延期</font>',
		listeners : {
			check : function() {
				// 选中状态
				if (delayLabel.checked) {
					// delayOldDate,delayChargeBy,delayDutyBy,delayToDate,delayRunBy
					Ext.get("delayOldDate").dom.parentNode.parentNode.parentNode.style.display = '';
					Ext.get("delayChargeBy").dom.parentNode.parentNode.parentNode.style.display = '';
					Ext.get("delayDutyBy").dom.parentNode.parentNode.parentNode.style.display = '';
					Ext.get("delayToDate").dom.parentNode.parentNode.parentNode.style.display = '';
					Ext.get("delayRunBy").dom.parentNode.parentNode.parentNode.style.display = '';
					// 未选中状态
				} else {
					Ext.get("delayOldDate").dom.parentNode.parentNode.parentNode.style.display = 'none';
					Ext.get("delayChargeBy").dom.parentNode.parentNode.parentNode.style.display = 'none';
					Ext.get("delayDutyBy").dom.parentNode.parentNode.parentNode.style.display = 'none';
					Ext.get("delayToDate").dom.parentNode.parentNode.parentNode.style.display = 'none';
					Ext.get("delayRunBy").dom.parentNode.parentNode.parentNode.style.display = 'none';
				}
			}
		}
	})

	var delayOldDate = new Ext.form.TextField({
		id : 'delayOldDate',
		fieldLabel : "原时间",
		name : 'delayHisModel.oldApprovedFinishDate',
		style : 'cursor:pointer',
		anchor : "80%"
			// listeners : {
			// focus : function() {
			// WdatePicker({
			// startDate : '%y-%M-%d 00:00:00',
			// dateFmt : 'yyyy-MM-dd HH:mm:ss',
			// alwaysUseStartDate : true
			// });
			// }
			// }
	})
	var delayToDate = new Ext.form.TextField({
		id : 'delayToDate',
		fieldLabel : "延期到",
		name : 'delayHisModel.newApprovedFinishDate',
		style : 'cursor:pointer',
		anchor : "80%",
		value : getDate(),
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
	var delayChargeBy = {
		fieldLabel : '工作负责人',
		name : 'delayChargeBy',
		xtype : 'combo',
		id : 'delayChargeBy',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		hiddenName : 'delayHisModel.oldChargeBy',
		editable : false,
		anchor : "80%",
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
			var args = {
				selectModel : 'single',
				notIn : "'999999'",
				rootNode : {
					id : '-1',
					text : '合肥电厂'
				}
			}
			var emp = window
					.showModalDialog(
							url,
							args,
							'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');

			if (typeof(emp) != "undefined") {
				Ext.getCmp('delayChargeBy').setValue(emp.workerCode);
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('delayChargeBy'), emp.workerName);
			}
		}
	};

	var delayRunBy = {
		fieldLabel : '运行值班负责人',
		name : 'delayRunBy',
		xtype : 'combo',
		id : 'delayRunBy',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		hiddenName : 'delayHisModel.dutyChargeBy',
		editable : false,
		anchor : "80%",
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
			var args = {
				selectModel : 'single',
				notIn : "'999999'",
				rootNode : {
					id : '-1',
					text : '合肥电厂'
				}
			}
			var emp = window
					.showModalDialog(
							url,
							args,
							'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');

			if (typeof(emp) != "undefined") {
				Ext.getCmp('delayRunBy').setValue(emp.workerCode);
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('delayRunBy'), emp.workerName);
			}
		}
	};

	var delayDutyBy = {
		fieldLabel : '值长',
		name : 'delayDutyBy',
		xtype : 'combo',
		id : 'delayDutyBy',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		hiddenName : 'delayHisModel.newChargeBy',
		editable : false,
		anchor : "80%",
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
			var args = {
				selectModel : 'single',
				notIn : "'999999'",
				rootNode : {
					id : '-1',
					text : '合肥电厂'
				}
			}
			var emp = window
					.showModalDialog(
							url,
							args,
							'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');

			if (typeof(emp) != "undefined") {
				Ext.getCmp('delayDutyBy').setValue(emp.workerCode);
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('delayDutyBy'), emp.workerName);
			}
		}
	};

	var delayPanel = new Ext.Panel({
		// height : 40,
		id : 'delayPanel',
		border : false,
		style : "padding-top:10;border-width:1px 0 0 0;",
		layout : "column",
		labelWidth : 100,
		labelAlign : "right",
		items : [{
			columnWidth : 1,
			layout : "form",
			border : false,
			items : [delayLabel]
		}, {
			columnWidth : 0.5,
			layout : "form",
			border : false,
			items : [delayOldDate, delayChargeBy, delayDutyBy]
		}, {
			columnWidth : 0.5,
			layout : "form",
			border : false,
			items : [delayToDate, delayRunBy]
		}]
	});
	// --------------------------
	// ---------工作终结 ------------

	var endLabel = new Ext.form.Label({
		text : " 工作票终结：",
		style : 'color:red'
	});

	var endDate = new Ext.form.TextField({
		id : 'endDate',
		fieldLabel : "工作结束时间",
		name : 'endHisModel.oldApprovedFinishDate',
		style : 'cursor:pointer',
		anchor : "80%",
		value : getDate(),
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-%d 00:00:00',
					dateFmt : 'yyyy-MM-dd HH:mm:ss',
					alwaysUseStartDate : true
				});
			}
		}
	});
	var endChargeBy = {
		fieldLabel : '工作负责人',
		name : 'endChargeBy',
		xtype : 'combo',
		id : 'endChargeBy',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		hiddenName : 'endHisModel.oldChargeBy',
		editable : false,
		anchor : "80%",
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
			var args = {
				selectModel : 'single',
				notIn : "'999999'",
				rootNode : {
					id : '-1',
					text : '合肥电厂'
				}
			}
			var emp = window
					.showModalDialog(
							url,
							args,
							'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');

			if (typeof(emp) != "undefined") {
				Ext.getCmp('endChargeBy').setValue(emp.workerCode);
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('endChargeBy'), emp.workerName);
			}
		}
	};

	var endAcceptBy = {
		fieldLabel : '点检验收人',
		name : 'endAcceptBy',
		xtype : 'combo',
		id : 'endAcceptBy',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		hiddenName : 'endHisModel.newChargeBy',
		editable : false,
		anchor : "80%",
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
			var args = {
				selectModel : 'single',
				notIn : "'999999'",
				rootNode : {
					id : '-1',
					text : '合肥电厂'
				}
			}
			var emp = window
					.showModalDialog(
							url,
							args,
							'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');

			if (typeof(emp) != "undefined") {
				Ext.getCmp('endAcceptBy').setValue(emp.workerCode);
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('endAcceptBy'), emp.workerName);
			}
		}
	};
	var endPemitBy = {
		fieldLabel : '工作许可人',
		name : 'endPemitBy',
		xtype : 'combo',
		id : 'endPemitBy',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		hiddenName : 'endHisModel.fireBy',
		editable : false,
		anchor : "80%",
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
			var args = {
				selectModel : 'single',
				notIn : "'999999'",
				rootNode : {
					id : '-1',
					text : '合肥电厂'
				}
			}
			var emp = window
					.showModalDialog(
							url,
							args,
							'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');

			if (typeof(emp) != "undefined") {
				Ext.getCmp('endPemitBy').setValue(emp.workerCode);
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('endPemitBy'), emp.workerName);
			}
		}
	};

	var endDutyBy = {
		fieldLabel : '值班负责人',
		name : 'endDutyBy',
		xtype : 'combo',
		id : 'endDutyBy',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		hiddenName : 'endHisModel.dutyChargeBy',
		editable : false,
		anchor : "80%",
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
			var args = {
				selectModel : 'single',
				notIn : "'999999'",
				rootNode : {
					id : '-1',
					text : '合肥电厂'
				}
			}
			var emp = window
					.showModalDialog(
							url,
							args,
							'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');

			if (typeof(emp) != "undefined") {
				Ext.getCmp('endDutyBy').setValue(emp.workerCode);
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('endDutyBy'), emp.workerName);
			}
		}
	};

	var endTotalLine = new Ext.form.NumberField({
		fieldLabel : '接地线总数',
		id : 'endTotalLine',
		anchor : "80%",
		name : 'endHisModel.totalLine',
		border : false
	});
	var endNoBackoutLine = new Ext.form.NumberField({
		fieldLabel : '未拆除数',
		id : 'endNoBackoutLine',
		anchor : "80%",
		name : 'endHisModel.nobackoutLine',
		border : false
	});
	var endNoBackoutNum = new Ext.form.TextField({
		fieldLabel : '未拆除编号',
		id : 'endNoBackoutNum',
		anchor : "80%",
		name : 'endHisModel.nobackoutNum',
		border : false
	});

	var endMemo = new Ext.form.TextArea({
		fieldLabel : '备注',
		autoScroll : true,
		isFormField : true,
		anchor : "90%",
		name : 'endMemo',
		border : false
	});

	var endPanel = new Ext.Panel({
		// height : 40,
		border : false,
		style : "padding-top:10;border-width:1px 0 0 0;",
		layout : "column",
		labelWidth : 100,
		labelAlign : "right",
		items : [{
			columnWidth : 1,
			layout : "form",
			border : false,
			items : [endLabel]
		}, {
			columnWidth : 0.5,
			layout : "form",
			border : false,
			items : [endDate, endPemitBy, endTotalLine, endNoBackoutNum]
		}, {
			columnWidth : 0.5,
			layout : "form",
			border : false,
			items : [endChargeBy, endAcceptBy, endDutyBy, endNoBackoutLine]
		}, {
			columnWidth : 1,
			layout : "form",
			border : false,
			items : [endMemo]
		}]
	});
	// --------------------------
	var form = new Ext.FormPanel({

		columnWidth : 0.8,
		border : true,
		labelAlign : 'center',
		buttonAlign : 'right',
		buttons : [{
			text : "确定",
			handler : function() {
				// 显示用户验证窗口

				// if (!ifSeeDanger) {
				// Ext.Msg.alert("提示", "请查看危险点！");
				// return;
				// }
				if(eventIdentify == 'ZF')
				{
				}
				else
				{
//				if (Ext.get('safetyExePermitBy').dom.value == "") {
//					Ext.Msg.alert("提示", "请输入{工作许可人}！");
//					return false;
//				}
//				if (Ext.get('safetyExeDutyBy').dom.value== "") {
//					Ext.Msg.alert("提示", "请输入{值班负责人}！");
//					return false;
//				}
//				if (Ext.get('permitBy').dom.value == "") {
//					Ext.Msg.alert("提示", "请输入{许可人}！");
//					return false;
//				}
//				if (Ext.get('permitChargeBy').dom.value == "") {
//					Ext.Msg.alert("提示", "请输入{工作负责人}！");
//					return false;
//				}
//				if (Ext.get('endChargeBy').dom.value == "") {
//					Ext.Msg.alert("提示", "请输入{终结时工作负责人}！");
//					return false;
//				}
//				if (Ext.get('endPemitBy').dom.value == "") {
//					Ext.Msg.alert("提示", "请输入{工作许可人}！");
//					return false;
//				}
//				if(Ext.get('endDutyBy').dom.value==""){
//					Ext.Msg.alert("提示", "请输入{值班负责人}！");
//					return false;
//				}
//				if (changeLabel.checked) {
//					if (Ext.get("newChargeBy").dom.value == "") {
//
//						Ext.Msg.alert("提示", "请输入{现工作负责人}！");
//						return false;
//					}
//
//				}
				if (delayLabel.checked) {
					if (Ext.get("delayToDate").dom.value <= Ext
							.get("delayOldDate").dom.value) {
						Ext.Msg.alert("提示", "延期时间必须大于原批准结束时间！");
						return false;
					}
				}
				if(endDate.getValue()<=permitDate.getValue()){
					Ext.Msg.alert("提示", "工作结束时间必须大于许可开工时间！");
					return false;
				}
				}
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
		items : [titleField, safetyExePanel, permitPanel, endPanel,
				changePanel, delayPanel, tableField, showAppointNextRoles,
				approveSet, remarkSet]
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
										url : 'bqworkticket/endApprove.action',
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
											workticketMemo : endMemo.getValue(),
											changeLabelStatus : changeLabel.checked,
											delayLabelStatus : delayLabel.checked
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
				// validateWin.hide();
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
		id : 'tab1',
		autoScroll : true,
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

	var tab4Href = getReportUrl(workticketType, strBusiNo, fireLevelId);// "/powerrpt/report/webfile/bqmis/ElectricOne.jsp?no="+strBusiNo;
	var tab4Html = "<iframe name='tabSafety' src='" + tab4Href
			+ "'style='width:100%;height:100%;border:0px;'></iframe>";

	var ticketPanel = new Ext.Panel({
		title : "票面浏览",
		id : 'tab4',
		html : tab4Html
	});

	var tab2Href = "run/bqworkticket/business/approve/safety/workticketSafety.jsp?workticketNo="
			+ strBusiNo
			+ "&workticketType="
			+ workticketType
			+ "&showSafetyTyep=";
	var tab2Html = "<iframe name='tabSafety' src='" + tab2Href
			+ "'style='width:100%;height:100%;border:0px;'></iframe>";

	// 危险点
	var tab3Href = "run/bqworkticket/business/approve/danger/danger.jsp?workticketNo="
			+ strBusiNo;
	var tab3Html = "<iframe name='tabDanger' src='" + tab3Href
			+ "'style='width:100%;height:100%;border:0px;'></iframe>";

	// 交回/恢复
	var tab4Href = "run/bqworkticket/business/approve/end/returnBack.jsp?workticketNo="
			+ strBusiNo;
	var tab4Html = "<iframe name='tabDanger' src='" + tab4Href
			+ "'style='width:100%;height:100%;border:0px;'></iframe>";

	// tabpanel
	var tabPanel = new Ext.TabPanel({
		activeTab : 0,
		autoScroll : true,
		layoutOnTabChange : true,
		border : false,
		items : [signPanel, {
			title : '交回/恢复',
			html : tab4Html
		}, {
			title : '安措信息',
			id : 'tab2',
			html : tab2Html
		}, {
			title : '危险点信息',
			id : 'tab3',
			html : tab3Html
		}, ticketPanel]
	})

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

	// 字段显隐控制------------------------
	if (workticketType == "1") {
		Ext.get("permitDutyBy").dom.parentNode.parentNode.parentNode.style.display = 'none';
	} else if (workticketType == "2") {
		// 电二种票
		Ext.get("endDutyBy").dom.parentNode.parentNode.parentNode.style.display = 'none';
		Ext.get("endNoBackoutLine").dom.parentNode.parentNode.style.display = 'none';
		Ext.get("endNoBackoutNum").dom.parentNode.parentNode.style.display = 'none';
		Ext.get("delayPanel").dom.style.display = 'none';
		Ext.get("changePanel").dom.style.display = 'none';
		Ext.get("safetyExePanel").dom.style.display = 'none';

	} else if (workticketType == "3") {
		Ext.get("endDutyBy").dom.parentNode.parentNode.parentNode.style.display = 'none';
		Ext.get("endNoBackoutLine").dom.parentNode.parentNode.style.display = 'none';
		Ext.get("endNoBackoutNum").dom.parentNode.parentNode.style.display = 'none';
		Ext.get("endTotalLine").dom.parentNode.parentNode.style.display = 'none';
		Ext.get("safetyExePanel").dom.style.display = 'none';
	} else if (workticketType == "5") {
		Ext.get("endDutyBy").dom.parentNode.parentNode.parentNode.style.display = 'none';
		Ext.get("endNoBackoutLine").dom.parentNode.parentNode.style.display = 'none';
		Ext.get("endNoBackoutNum").dom.parentNode.parentNode.style.display = 'none';
		Ext.get("endTotalLine").dom.parentNode.parentNode.style.display = 'none';
		Ext.get("endAcceptBy").dom.parentNode.parentNode.parentNode.style.display = 'none';
		Ext.get("safetyExePanel").dom.style.display = 'none';
	} else if (workticketType == "7") {
		// 热力机械二
		Ext.get("endDutyBy").dom.parentNode.parentNode.parentNode.style.display = 'none';
		Ext.get("endNoBackoutLine").dom.parentNode.parentNode.style.display = 'none';
		Ext.get("endNoBackoutNum").dom.parentNode.parentNode.style.display = 'none';
		Ext.get("delayPanel").dom.style.display = 'none';
		Ext.get("changePanel").dom.style.display = 'none';
		Ext.get("endTotalLine").dom.parentNode.parentNode.style.display = 'none';
		Ext.get("safetyExePanel").dom.style.display = 'none';
	} else if (workticketType == "8") {
		// 热控二
		Ext.get("endDutyBy").dom.parentNode.parentNode.parentNode.style.display = 'none';
		Ext.get("endNoBackoutLine").dom.parentNode.parentNode.style.display = 'none';
		Ext.get("endNoBackoutNum").dom.parentNode.parentNode.style.display = 'none';
		Ext.get("delayPanel").dom.style.display = 'none';
		Ext.get("changePanel").dom.style.display = 'none';
		Ext.get("endTotalLine").dom.parentNode.parentNode.style.display = 'none';
		Ext.get("endAcceptBy").dom.parentNode.parentNode.parentNode.style.display = 'none';
		Ext.get("safetyExePanel").dom.style.display = 'none';
	} else {
	}
	// ----------------------------------------------------------------
	function initData() {
		Ext.lib.Ajax.request('POST',
				'bqworkticket/getWorkticketBaseInfoByNo.action', {
					success : function(result, request) {
						var record = eval('(' + result.responseText + ')');
						var chargeByCode = record.data.model.chargeBy;
						var chargeByName = record.data.chargeByName;
						// 许可：工作负责人
						Ext.getCmp('permitChargeBy').setValue(chargeByCode);
						Ext.form.ComboBox.superclass.setValue.call(Ext
								.getCmp('permitChargeBy'), chargeByName);
						// 工作负责人变更
						Ext.getCmp('oldChargeBy').setValue(chargeByCode);
						Ext.form.ComboBox.superclass.setValue.call(Ext
								.getCmp('oldChargeBy'), chargeByName);
						// 工作票延期
						Ext.getCmp('delayChargeBy').setValue(chargeByCode);
						Ext.form.ComboBox.superclass.setValue.call(Ext
								.getCmp('delayChargeBy'), chargeByName);
						if (record.data.approveEndDate != null) {
							delayOldDate.setValue(record.data.approveEndDate
									.substring(0, 19));
						}
						// 终结
						Ext.getCmp('endChargeBy').setValue(chargeByCode);
						Ext.form.ComboBox.superclass.setValue.call(Ext
								.getCmp('endChargeBy'), chargeByName);
						endMemo.setValue(record.data.model.workticketMemo);
						// 工作负责人变更默认不展开
						Ext.get("oldChargeBy").dom.parentNode.parentNode.parentNode.style.display = 'none';
						Ext.get("changeSignBy").dom.parentNode.parentNode.parentNode.style.display = 'none';
						Ext.get("changeDate").dom.parentNode.parentNode.parentNode.style.display = 'none';
						Ext.get("newChargeBy").dom.parentNode.parentNode.parentNode.style.display = 'none';
						Ext.get("changeDutyBy").dom.parentNode.parentNode.parentNode.style.display = 'none';
						// 延期默认不展开
						Ext.get("delayOldDate").dom.parentNode.parentNode.parentNode.style.display = 'none';
						Ext.get("delayChargeBy").dom.parentNode.parentNode.parentNode.style.display = 'none';
						Ext.get("delayDutyBy").dom.parentNode.parentNode.parentNode.style.display = 'none';
						Ext.get("delayToDate").dom.parentNode.parentNode.parentNode.style.display = 'none';
						Ext.get("delayRunBy").dom.parentNode.parentNode.parentNode.style.display = 'none';
					},
					failure : function() {
						btnModify.setVisible(false);
						Ext.Msg.alert("提示", "错误");
					}
				}, 'workticketNo=' + strBusiNo);
	}
	initData();
});
