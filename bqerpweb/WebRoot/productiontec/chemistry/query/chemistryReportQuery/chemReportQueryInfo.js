Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

var currentRecord = parent.currentRecord;
var errorMsg = "";
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

	s = s.substring(0, 10)
	return s;
}
Ext.onReady(function() {

	var ffid;
	if (currentRecord != null) {
		ffid = currentRecord.get('phj.zxybybzbId');
	}

	function numberFormat(value) {
		if (value == null || value == "") {
			return "0.0%";
		} else if (value.toString() == "NaN") {
			return "";
		}else if( value.toString() == "Infinity")
		{
			return "";
		} else {
			value = (Math.round((value - 0) * 100)) / 100;
			value = (value == Math.floor(value))
				? value + ".00"
				: ((value * 10 == Math.floor(value * 10))
						? value + "0"
						: value);
			value = String(value);
			var ps = value.split(".");
			var whole = ps[0];
			var sub = ps[1] ? '.' + ps[1].substring(0, 2) : '.00';
			var v = whole + sub;
		
			var forvalue = (v + 0) * 100;
			var tovalue = forvalue.toString() + ".0%";
			return tovalue;
		}

	}

	// 从session取登录人编码姓名部门相关信息
	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
			success : function(action) {
				var result = eval("(" + action.responseText + ")");
				if (result.workerCode) {
					// 设定默认工号，赋给全局变量
					Ext.get('fillBy').dom.value = result.workerCode;
					Ext.get('fillName').dom.value = result.workerName;
				}
			}
		});
	}
	getWorkCode();
	// 月报主表编号
	var zxybybzbId = {
		id : "zxybybzbId",
		xtype : "hidden",
		fieldLabel : 'zxybybzbId',
		value : '',
		readOnly : true,
		name : 'phj.zxybybzbId'
	}
	// 企业编号
	var enterpriseCode = {
		id : "enterpriseCode",
		xtype : "hidden",
		readOnly : true,
		name : 'phj.enterpriseCode'
	}
	// 月份
	var reportTime = {
		id : "reportTime",
		xtype : "hidden",
		readOnly : true,
		name : 'phj.reportTime'
	}

	// 所属机组
	var storeCharge = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'workticket/getDetailEquList.action'
		}),
		reader : new Ext.data.JsonReader({
			root : 'list'
		}, [{
			name : 'blockCode',
			mapping : 'blockCode'
		}, {
			name : 'blockName',
			mapping : 'blockName'
		}])
	});
	storeCharge.load();
	var cbxCharge = new Ext.form.ComboBox({
		id : 'cbxCharge',
		fieldLabel : "机组",
		store : storeCharge,
		displayField : "blockName",
		valueField : "blockCode",
		hiddenName : 'phj.deviceCode',
		mode : 'local',
		triggerAction : 'all',
		value : '',
		readOnly : true,
		width : 130,
		anchor : '80%',
		hideTrigger : true,
		listeners : {
			select : function() {

			}
		}
	})

	// 月份
	var month = new Ext.form.TextField({
		fieldLabel : "月份",
		id : 'month',
		readOnly : true,
		width : 100,
		anchor : '59%',
		name : 'month'
	});

	// 备注
	var memo = {
		id : "memo",
		name : 'phj.memo',
		height : 90,
		xtype : "textarea",
		fieldLabel : '备注',
		readOnly : true,
		anchor : "75%"
	}

	// 填报人
	var fillName = new Ext.form.TextField({
		id : 'fillName',
		fieldLabel : '填报人',
		name : 'fillName',
		xtype : 'textfield',
		anchor : '80%',
		readOnly : true

	});
	// 填报人编码
	var fillBy = new Ext.form.Hidden({
		hidden : false,
		id : "fillBy",
		name : 'phj.fillBy'
	});
	// 填报时间
	var fillDate = new Ext.form.TextField({
		id : 'fillDate',
		fieldLabel : '填报时间',
		name : 'phj.fillDate',
		xtype : 'textfield',
		readOnly : true,
		anchor : '59%',
		value : getDate()

	});
	// 定义grid
	var MyRecord = Ext.data.Record.create([{
		// 月报主表编号
		name : 'phj.zxybybzbId'
	}, {
		// 机组 编码
		name : 'phj.deviceCode'
	}, {
		// 月份 日期型
		name : 'phj.reportTime'
	}, {
		// 备注
		name : 'phj.memo'
	}, {
		// 填报人 编码
		name : 'phj.fillBy'
	}, {
		// 填报日期
		name : 'phj.fillDate'
	}, {
		// 工作流序号
		name : 'phj.workFlowNo'
	}, {
		// 工作流状态
		name : 'phj.workFlowStatus'
	}, {
		// 单位编码
		name : 'phj.enterpriseCode'
	}, {
		// 机组名
		name : 'deviceName'
	}, {
		// 月份
		name : 'month'
	}, {
		// 填报人姓名
		name : 'fillName'
	}, {
		// 填报时间
		name : 'fillDate'
	}, {
		// 月报编号
		name : 'zxybybId'
	}, {
		// 仪表ID
		name : 'meterId'
	}, {
		// 必投台数
		name : 'mustThrowNum'
	}, {
		// 配备台数
		name : 'equipNum'

	}, {
		// 投运台数
		name : 'throwNum'
	}, {
		// 配备率
		name : 'equipRate'

	}, {
		// 投入率
		name : 'throwRate'
	}, {
		// 准确率
		name : 'searchRate'
	}, {
		// 仪表名称
		name : 'meterName'
	}]);
	var dataProxy = new Ext.data.HttpProxy({
		url : 'productionrec/findChemistryDetailsList.action'
	});
	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	}, MyRecord);
	var store = new Ext.data.Store({
		proxy : dataProxy,
		reader : theReader
	});

	function queryRecord() {
		store.load({
			params : {
//				start : 0,
//				limit : 18,
				zxybybzbId : (currentRecord == null || currentRecord == "")
						? ""
						: currentRecord.get('phj.zxybybzbId')
			}
		});
	}

	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true
	});

	// 明细grid
	var detailsGrid = new Ext.grid.EditorGridPanel({
		region : "center",
		frame : false,
		border : false,
		height : 330,
		width : 700,
		autoScroll : true,
		enableColumnMove : false,
		sm : sm,
		store : store,
		columns : [new Ext.grid.RowNumberer({
			header : '行号',
			width : 35,
			align : 'left'
		}), {
			header : "月报主表编号",
			width : 100,
			sortable : true,
			dataIndex : 'phj.zxybybzbId',
			hidden : true
		}, {
			header : "机组",
			width : 80,
			sortable : true,
			hidden : true,
			dataIndex : 'deviceName'
		}, {
			header : "月份",
			width : 80,
			sortable : true,
			hidden : true,
			dataIndex : 'month'
		}, {
			header : "备注",
			width : 80,
			sortable : true,
			hidden : true,
			dataIndex : 'phj.memo'
		}, {
			header : "填报人",
			width : 80,
			sortable : true,
			hidden : true,
			dataIndex : 'fillName'
		}, {
			header : "填报日期",
			width : 130,
			sortable : true,
			hidden : true,
			dataIndex : 'fillDate'
		}, {
			header : "工作流序号",
			width : 100,
			sortable : true,
			align : 'left',
			hidden : true,
			dataIndex : 'phj.workFlowNo'
		}, {
			header : "工作流状态",
			width : 100,
			sortable : true,
			hidden : true,
			align : 'center',
			dataIndex : 'phj.workFlowStatus'
		}, {
			header : "单位编码",
			width : 100,
			sortable : true,
			align : 'center',
			hidden : true,
			dataIndex : 'phj.enterpriseCode'
		}, {
			header : "月报编号",
			width : 100,
			sortable : true,
			align : 'center',
			hidden : true,
			dataIndex : 'zxybybId'
		}, {
			header : "仪表ID",
			width : 100,
			sortable : true,
			align : 'center',
			hidden : true,
			dataIndex : 'meterId'
		}, {
			header : "仪表名称",
			width : 80,
			sortable : true,
			align : 'center',
			dataIndex : 'meterName'
		}, {
			header : "应配台数",
//			css : CSS_GRID_INPUT_COL,
			width : 80,
			sortable : true,
			align : 'center',
//			editor : new Ext.form.NumberField({
//				maxValue : 99999999999,
//				minValue : 0,
//				allowDecimals : false,
//				allowNegative : false
//			}),
			dataIndex : 'mustThrowNum'
		}, {
			header : "实配台数",
//			css : CSS_GRID_INPUT_COL,
			width : 80,
			sortable : true,
			align : 'center',
//			editor : new Ext.form.NumberField({
//				maxValue : 99999999999,
//				minValue : 0,
//				allowDecimals : false,
//				allowNegative : false
//			}),
			dataIndex : 'equipNum'
		}, {
			header : "投运台数",
//			css : CSS_GRID_INPUT_COL,
			width : 80,
			sortable : true,
			align : 'center',
//			editor : new Ext.form.NumberField({
//				maxValue : 99999999999,
//				minValue : 0,
//				allowDecimals : false,
//				allowNegative : false
//			}),
			dataIndex : 'throwNum'
		}, {
			header : "配备率",
			width : 80,
			sortable : true,
			align : 'center',
			renderer : function(value, cellmeta, record, rowIndex, columnIndex,
					store) {
						if(record.data.equipNum == null 
							|| record.data.mustThrowNum == null )
							{
								return "";
							}
						if(record.data.equipNum == 0 && record.data.mustThrowNum != 0)
						{
							return "0.0%";
						}
				var subRate = record.data.equipNum / record.data.mustThrowNum;
				return numberFormat(subRate);

			},
			dataIndex : 'equipRate'
		}, {
			header : "投入率",
			width : 80,
			sortable : true,
			align : 'center',
			renderer : function(value, cellmeta, record,
									rowIndex, columnIndex, store) {

								if (record.data.throwNum == null
										|| record.data.equipNum == null) {
									return "";
								}
								if (record.data.throwNum == 0
										&& record.data.equipNum != 0) {
									return "0.0%";
								}
								var subRate = record.data.throwNum
										/ record.data.equipNum;
								return numberFormat(subRate);

							},
			dataIndex : 'throwRate'
		}, {
			header : "准确率",
//			css : CSS_GRID_INPUT_COL,
			width : 80,
			sortable : true,
			align : 'center',
			renderer : function(value, cellmeta, record, rowIndex, columnIndex,
					store) {
				return numberFormat(value)
			},
//			editor : new Ext.form.NumberField({
//				maxValue : 99999999999.99,
//				minValue : 0,
//				decimalPrecision : 2
//			}),
			dataIndex : 'searchRate'
		}]
//		,
//		bbar : new Ext.PagingToolbar({
//			pageSize : 18,
//			store : store,
//			displayInfo : true,
//			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
//			emptyMsg : "没有记录"
//		})
	});
	var workApplyField = new Ext.Panel({
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
			layout : "column",
			border : false,
			items : [{
				columnWidth : 1,
				layout : "form",
				border : false,
				items : [zxybybzbId,enterpriseCode,reportTime]
			}]
		}, {
			layout : "column",
			border : false,
			items : [{
				columnWidth : 0.4,
				layout : "form",
				border : false,
				items : [cbxCharge]
			}, {
				columnWidth : 0.6,
				layout : "form",
				border : false,
				items : [month]
			}, {
				columnWidth : 1,
				layout : "form",
				border : false,
				items : [memo]
			}, {
				columnWidth : 0.4,
				layout : "form",
				border : false,
				items : [fillName, fillBy]
			}, {
				columnWidth : 0.6,
				layout : "form",
				border : false,
				items : [fillDate]
			}]
		}]
	});

	var form = new Ext.form.FormPanel({
		border : false,
		frame : true,
		layout : 'form',

		items : [workApplyField, detailsGrid],
		bodyStyle : {
			'padding-top' : '5px'
		},
		defaults : {
			labelAlign : 'right'
		}
	});

	var workApply = new Ext.form.FieldSet({
		border : true,
		labelWidth : 80,
		labelAlign : 'right',

		layout : 'form',
		title : '化学在线仪表月报信息',
		autoHeight : true,
		items : [form]
	});

	// 清除所有Field
	function clearAllFields() {
		// form.getForm().reset();
	}

	var workApplyViewport = new Ext.Viewport({
		layout : "fit",
		border : false,
		autoScroll : true,
		height : 150,
		items : [workApply]

	}).show();
	if (ffid != null && ffid != "") {
		Ext.getCmp('zxybybzbId').setValue(currentRecord.get("phj.zxybybzbId"));
		Ext.get('cbxCharge').dom.value = currentRecord.get('deviceName');
		
		Ext.get('phj.deviceCode').dom.value = currentRecord
				.get('phj.deviceCode');
		Ext.get('month').dom.value = currentRecord.get("month");
		Ext.getCmp('memo').setValue(currentRecord.get("phj.memo"));
		Ext.getCmp('fillBy').setValue(currentRecord.get("phj.fillBy"));
		Ext.getCmp('fillName').setValue(currentRecord.get("fillName"));
		Ext.getCmp('fillDate').setValue(currentRecord.get("fillDate"));
		Ext.getCmp('fillBy').setValue(currentRecord.get("phj.fillBy"));
		
		Ext.get('phj.enterpriseCode').dom.value = currentRecord.get('phj.enterpriseCode');
		Ext.get('phj.reportTime').dom.value = currentRecord.get('phj.reportTime');
		
		queryRecord()

	} else {
		queryRecord()
	}

	
})
