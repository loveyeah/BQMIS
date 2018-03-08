Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {

	var budgetItemId = getParameter("budgetItemId");
	var changeId = getParameter("changeId");

	if (changeId != null) {
		Ext.Ajax.request({
			url : 'managebudget/getBudgetModifyById.action',
			params : {
				changeId : changeId,
				budgetItemId : budgetItemId
			},
			method : 'post',
			waitMsg : '正在加载数据...',
			success : function(action) {
				var result = eval("(" + action.responseText + ")");
				// alert(action.responseText);
				if (result.centerName != null) {
					Ext.get('centerName').dom.value = result.centerName;
				}
				if (result.topicName != null) {
					Ext.get('topicName').dom.value = result.topicName;
				}
				if (result.budgetTime != null) {
					Ext.get('budgetTime').dom.value = result.budgetTime;
				}
				if (result.itemName != null) {
					Ext.get('itemName').dom.value = result.itemName;
				}
				if (result.originBudget != null) {
					Ext.get('originBudget').dom.value = result.originBudget;
				}
				if (result.budgetChange != null) {
					Ext.get('budgetChange').dom.value = result.budgetChange;
				} else {
					Ext.get('budgetChange').dom.value = "";
				}
				if (result.newBudget != null) {
					Ext.get('newBudget').dom.value = result.newBudget;
				}
				if (result.changeReason != null) {
					Ext.get('changeReason').dom.value = result.changeReason;
				} else {
					Ext.get('changeReason').dom.value = "";
				}
				if (result.btnChange == Ext.get('budgetChange').dom.value) {
					ryRadio.setValue(true);
				} else {
					rnRadio.setValue(true);
				}
				if (result.changeDate != null) {
					Ext.get('changeDate').dom.value = result.changeDate;
				}
			},
			failure : function(action) {
				Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
			}
		});
	}

	// 从session取登录人编码姓名部门相关信息
	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
			success : function(action) {
				var result = eval("(" + action.responseText + ")");
				if (result.workerCode) {
					// 设定默认工号，赋给全局变量
					Ext.get('changeBy').dom.value = result.workerCode;
					Ext.get('changeName').dom.value = result.workerName;
				}
			}
		});
	}
	getWorkCode();

	var centerName = new Ext.form.TextField({
		id : 'centerName',
		fieldLabel : '预算部门',
		readOnly : true,
		anchor : "85%",
		name : 'centerName'
	});

	var topicName = new Ext.form.TextField({
		id : 'topicName',
		fieldLabel : '预算主题',
		readOnly : true,
		anchor : "85%",
		name : 'topicName'
	});

	var budgetTime = new Ext.form.TextField({
		id : 'budgetTime',
		fieldLabel : '预算时间',
		readOnly : true,
		anchor : "85%",
		name : 'change.budgetTime'
	});

	var itemName = new Ext.form.TextField({
		id : 'itemName',
		fieldLabel : '预算指标',
		readOnly : true,
		anchor : "85%",
		name : 'itemName'
	});

	var originBudget = new Ext.form.NumberField({
		id : 'originBudget',
		fieldLabel : '原预算值',
		readOnly : true,
		anchor : "85%",
		name : 'change.originBudget'
	});

	var newBudget = new Ext.form.NumberField({
		id : 'newBudget',
		fieldLabel : '现预算值',
		readOnly : true,
		anchor : "85%",
		name : 'change.newBudget'
	});

	var ryRadio = new Ext.form.Radio({
		id : 'ay',
		boxLabel : '追加预算',
		name : 'rs',
		inputValue : 'Y',
		checked : true
	});

	var rnRadio = new Ext.form.Radio({
		id : 'an',
		boxLabel : '减少预算',
		name : 'rs',
		inputValue : 'N'
	});

	var ifBudget = {
		id : 'ifBudget',
		layout : 'column',
		isFormField : true,
		style : 'cursor:hand',
		fieldLabel : '变更方式',
		anchor : '85%',
		border : false,
		items : [{
			columnWidth : .4,
			border : false,
			items : [ryRadio]
		}, {
			columnWidth : .4,
			border : false,
			items : [rnRadio]
		}]
	};

	var budgetChange = new Ext.form.NumberField({
		id : 'budgetChange',
		fieldLabel : '预算变更',
		anchor : "85%",
		readOnly : true,
		name : 'change.budgetChange'
	});

	var changeReason = new Ext.form.TextArea({
		id : "changeReason",
		height : 50,
		fieldLabel : '变更事由',
		readOnly : true,
		name : 'change.changeReason',
		anchor : "93%"
	});

	var changeName = new Ext.form.TextField({
		id : 'changeName',
		fieldLabel : '变更申请人',
		name : 'changeName',
		anchor : '85%',
		readOnly : true

	});
	var changeBy = new Ext.form.Hidden({
		hidden : false,
		id : "changeBy",
		name : 'change.changeBy'
	});

	var changeDate = new Ext.form.TextField({
		id : 'changeDate',
		fieldLabel : '变更时间',
		name : 'change.changeDate',
		readOnly : true,
		anchor : "85%"
	});

	var eastField = new Ext.Panel({
		border : true,
		labelWidth : 80,
		region : 'north',
		labelAlign : 'right',
		autoHeight : true,
		style : {
			"margin-top" : "20px",
			"margin-left" : "10px",
			"margin-right" : Ext.isIE6
					? (Ext.isStrict ? "-10px" : "-13px")
					: "0"
		},
		items : [{
			layout : 'column',
			items : [{
				columnWidth : 0.5,
				layout : 'form',
				items : [centerName]
			}, {
				columnWidth : 0.5,
				layout : 'form',
				items : [topicName]
			}]
		}, {
			layout : 'column',
			items : [{
				columnWidth : 0.5,
				layout : 'form',
				items : [budgetTime]
			}, {
				columnWidth : 0.5,
				layout : 'form',
				items : [itemName]
			}]
		}, {
			layout : 'column',
			items : [{
				columnWidth : 0.5,
				layout : 'form',
				items : [originBudget]
			}, {
				columnWidth : 0.5,
				layout : 'form',
				items : [newBudget]
			}]
		}, {
			layout : 'column',
			items : [{
				columnWidth : 0.5,
				border : false,
				layout : "form",
				items : [ifBudget]
			}, {
				columnWidth : 0.5,
				border : false,
				layout : "form",
				items : [budgetChange]
			}]
		}, {
			layout : 'column',
			items : [{
				columnWidth : 1,
				layout : 'form',
				items : [changeReason]
			}]
		}, {
			layout : 'column',
			items : [{
				columnWidth : 0.5,
				layout : 'form',
				items : [changeName, changeBy]
			}, {
				columnWidth : 0.5,
				layout : 'form',
				items : [changeDate]
			}]
		}]
	});

	var form = new Ext.form.FormPanel({
		border : false,
		frame : true,
		layout : 'form',
		items : [eastField],
		bodyStyle : {
			'padding-top' : '5px'
		},
		defaults : {
			labelAlign : 'right'
		}
	});

	var eastForm = new Ext.form.FieldSet({
		border : true,
		labelWidth : 80,
		labelAlign : 'right',
		layout : 'form',
		title : '变更信息',
		autoHeight : true,
		items : [form]
	});

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		border : false,
		frame : false,
		autoScroll : true,
		items : [{
			region : "center",
			border : false,
			autoScroll : true,
			frame : false,
			layout : 'fit',
			items : [eastForm]
		}]
	});

	//遍历所有的REDIO获得ID
	function getChooseQueryType() {
		var list = document.getElementsByName("queryWayRadio");
		for (var i = 0; i < list.length; i++) {
			if (list[i].checked) {
				return list[i].id;
			}
		}
	}
});