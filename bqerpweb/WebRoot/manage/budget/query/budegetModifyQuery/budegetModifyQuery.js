Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

function getDateDay() {
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

// 系统当前时间
function getMonth() {
	var d, s, t;
	d = new Date();
	s = d.getFullYear().toString(10) + "-";
	t = d.getMonth() + 1;
	s += (t > 9 ? "" : "0") + t;
	return s;
}
function getDate() {
	var d, s, t;
	d = new Date();
	s = d.getFullYear().toString(10)
	return s;
}
Ext.onReady(function() {
	
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

	var formatType;
	var yearRadio = new Ext.form.Radio({
		id : 'year',
		name : 'queryWayRadio',
		hideLabel : true,
		boxLabel : '年份'
	});
	var monthRadio = new Ext.form.Radio({
		id : 'month',
		name : 'queryWayRadio',
		hideLabel : true,
		boxLabel : '月份',
		checked : true,
		listeners : {
			check : function() {
				var queryType = getChooseQueryType();
				switch (queryType) {
					case 'year' : {
						formatType = 1;
						time.setValue(getDate());
						break;
					}
					case 'month' : {
						time.setValue(getMonth());
						formatType = 2;
						break;
					}
				}
			}
		}
	});
	var time = new Ext.form.TextField({
		id : 'time',
		allowBlank : true,
		width : 70,
		listeners : {
			focus : function() {
				var format = '';
				if (formatType == 1)
					format = 'yyyy';
				if (formatType == 2)
					format = 'yyyy-MM';
				WdatePicker({
					startDate : '%y-%M-%d',
					dateFmt : format,
					alwaysUseStartDate : true,
					onclearing : function() {
						planStartDate.markInvalid();
					}
				});
			}
		}
	});
	// 查询按钮
	var btnquery = new Ext.Button({
		text : '查询',
		iconCls : 'query',
		handler : function query() {
			queryRecord();
		}
	});

	function queryRecord() {
		var msg = "";
		if (deptComboBox.getValue() == "")
			msg = "'预算部门'";
		if (topicComboBox.getValue() == "")
			msg = "'预算主题'";
		if (time.getValue() == "")
			msg += "'时间'";
		if (msg != "") {
			Ext.Msg.alert("提示", "请选择" + msg + "!");
			return;
		}
		westgrids.baseParams = {
			topicId : topicComboBox.getValue(),
			budgetTime : time.getValue(),
			formatType : formatType,
			deptCode : deptComboBox.getValue()
		};
		westgrids.load({
			params : {
				start : 0,
				limit : 18
			}
		});
		westgrids.rejectChanges();
	}
	//定义部门
	var deptData = new Ext.data.JsonStore({
		root : 'list',
		url : "managebudget/findBudgetDeptList.action",
		fields : ['centerId','depCode', 'depName']
	});
	deptData.on('load',function(e,records){
		var record1 = new Ext.data.Record({
			depName : '',
			depCode : '',
			centerId : ''
		});
		deptData.insert(0, record1);
		deptComboBox.setValue('');
	});
	deptData.load();
	
	var deptComboBox = new Ext.form.ComboBox({
		id : "deptComboBox",
		store : deptData,
		displayField : "depName",
		valueField : "depCode",
		mode : 'local',
		width : 100,
		triggerAction : 'all',
		readOnly : true
	});
	
	// 定义主题
	var topicData = new Ext.data.JsonStore({
		root : 'list',
		url : "managebudget/getThemeList.action",
		fields : ['topicId', 'topicName']
	});
	topicData.on('load', function(e, records) {
		var record1 = new Ext.data.Record({
//			topicName : '所有',
			topicName : '',
			topicId : ''
		});
		topicData.insert(0, record1);
		topicComboBox.setValue('');
	});
	topicData.load();

	var topicComboBox = new Ext.form.ComboBox({
		id : "topicComboBox",
		store : topicData,
		displayField : "topicName",
		valueField : "topicId",
		mode : 'local',
		width : 100,
		triggerAction : 'all',
		readOnly : true
	});

	var westsm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true
	});
	
	// 左边列表中的数据
	var westdatalist = new Ext.data.Record.create([{
		name : 'budgetItemId'
	}, {
		name : 'itemId'
	}, {
		name : 'itemAlias'
	}, {
		name : 'unitName'
	}, {
		name : 'adviceBudget'
	}, {
		name : 'ensureBudget'
	}, {
		name : 'topicId'
	}, {
		name : 'budgetTime'
	}, {
		name : 'changeId'
	}, {
		name : 'changeWorkFlowNo'
	}, {
		name : 'changeStatus'
	}, {
		name : 'changeDate'
	}, {
		name : 'itemCode'
	}]);

	var westgrids = new Ext.data.JsonStore({
		url : 'managebudget/findModifyItemQuery.action',
		root : 'list',
		totalProperty : 'totalCount',
		fields : westdatalist
	});
//	westgrids.load();

	// 左边列表
	var westgrid = new Ext.grid.GridPanel({
		// width : 200,
		split : true,
		autoScroll : true,
		ds : westgrids,
		tbar : ['部门:',deptComboBox,'-','主题：', topicComboBox, {
			xtype : "tbseparator"
		}, monthRadio, yearRadio, time, {
			xtype : "tbseparator"
		}, btnquery],
		columns : [westsm, new Ext.grid.RowNumberer({
			header : "行号",
			width : 31
		}), {
			header : "指标ID",
			width : 110,
			hidden : true,
			align : "center",
			sortable : true,
			dataIndex : 'itemId'
		}, {
			header : "指标名称",
			width : 130,
			align : "lift",
			sortable : true,
			dataIndex : 'itemAlias',
			//add by ltong 分层次显示 按itemCode（zbbmtx_code）排序
			renderer : function(value, cellmeta, record, rowIndex, columnIndex, store){
						var level=0;
						if(record.get("itemCode")!=null||record.get("itemCode")!="")
						{
							level= (record.get("itemCode").length/3)-2;
						}
						 if(level>0)
						 {
						 var levelNo="";
						 for(var i=0;i<level;i++)
						{
							levelNo="  "+levelNo;
						}
						
						value=levelNo+value;
						 }
					return "<pre>"+value+"</pre>";
				}
		}, {
			header : "计量单位",
			width : 110,
			align : "center",
			sortable : true,
			dataIndex : 'unitName'
		}, {
			header : "建议预算",
			width : 90,
			align : "center",
			sortable : true,
			dataIndex : 'adviceBudget'
		}, {
			header : "审定预算",
			width : 90,
			align : "center",
			sortable : true,
			dataIndex : 'ensureBudget'
		}, {
			header : "changeId",
			width : 70,
			align : "center",
			sortable : true,
			hidden : true,
			dataIndex : 'changeId'
		}, {
			header : "changeWorkFlowNo",
			width : 70,
			align : "center",
			sortable : true,
		    hidden : true,
			dataIndex : 'changeWorkFlowNo'
		}, {
			header : "changeStatus",
			width : 70,
			align : "center",
			sortable : true,
			hidden : true,
			dataIndex : 'changeStatus'
		}],
		sm : westsm,
		// frame : true,
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : westgrids,
			displayInfo : true,
			displayMsg : "共 {2} 条",
			emptyMsg : "没有记录"
		}),
		border : true
	});

	westsm.on('rowselect', function() {
		if (westsm.hasSelection()) {
			Ext.Ajax.request({
				url : 'managebudget/getBudgetModifyById.action',
				method : 'post',
				params : {
					budgetItemId : westsm.getSelected().get('budgetItemId'),
					changeId : westsm.getSelected().get('changeId')
				},
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
					if (westsm.getSelected().get('changeId') == null) {
						if (result.ensureBudget != null) {
							Ext.get('originBudget').dom.value = result.ensureBudget;
						} else {
							Ext.get('originBudget').dom.value = "";
						}
					} else {
						if (result.originBudget != null) {
							Ext.get('originBudget').dom.value = result.originBudget;
						} else {
							Ext.get('originBudget').dom.value = "";
						}
					}
						if (result.budgetChange != null) {
							Ext.get('budgetChange').dom.value = result.budgetChange;
						} else {
							Ext.get('budgetChange').dom.value = "";
						}
					if (result.newBudget != null) {
						Ext.get('newBudget').dom.value = result.newBudget;
					} else {
						Ext.get('newBudget').dom.value = "";
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
					if(result.changeDate != null)
					{
						Ext.get('changeDate').dom.value = result.changeDate;
					}
				}
			});
		}
	});

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
		boxLabel : '追加',
		name : 'rs',
		inputValue : 'Y',
		checked : true
	});

	var rnRadio = new Ext.form.Radio({
		id : 'an',
		boxLabel : '减少',
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
		//value : getDateDay(),
		anchor : "85%"
	});

	var formtbar = new Ext.Toolbar({
		items : [{
			id : 'btnModifyInfo',
			text : "变更信息",
			iconCls : 'list',
			handler : showModifyInfo
		}]
	});

	var eastField = new Ext.Panel({
		border : true,
		labelWidth : 70,
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
		width : 500,
		labelWidth : 80,
		labelAlign : 'right',
		layout : 'form',
		title : '变更信息',
		autoHeight : true,
		items : [form]
	});

	function showModifyInfo() {
		var selrows = westgrid.getSelectionModel().getSelections();
		if (selrows.length > 0) {
			var record = westgrid.getSelectionModel().getSelected();
			var url;
			var budgetItemId = record.data.budgetItemId;
			var changeId = record.data.changeId;
			//var changeWorkFlowNo = record.data.changeWorkFlowNo;
			if (changeId == null || changeId == "") {
				Ext.Msg.alert('提示', '所选记录没有变更信息!');
				return;
			} else if ((changeId != null) && (changeId != "")) {
				url = "../budegetModifyQuery/budegetModifyQuerySelect/budgetModifyQuerySelect.jsp?budgetItemId="
						+ budgetItemId + "&changeId=" + changeId;
			}
			var o = window.showModalDialog(url, '',
					'dialogWidth=700px;dialogHeight=400px;status=no');
		} else {
			Ext.Msg.alert('提示', '请先选择左边的一条记录!');
		}
	}

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		border : false,
		frame : false,
		items : [{
			layout : 'fit',
			border : false,
			split : true,
			frame : false,
			region : "west",
			width : 510,
			items : [westgrid]
		}, {
			region : "center",
			border : false,
			autoScroll : true,
			frame : false,
			tbar : formtbar,
			items : [eastForm]
		}]
	});

	// 遍历所有的REDIO获得ID
	function getChooseQueryType() {
		var list = document.getElementsByName("queryWayRadio");
		for (var i = 0; i < list.length; i++) {
			if (list[i].checked) {
				return list[i].id;
			}
		}
	}

});