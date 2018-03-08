Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	Ext.QuickTips.init();
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
	// 定义grid
	var MyRecord = Ext.data.Record.create([{
		name : 'medicalId'
	}, {
		name : 'workerCode'
	}, {
		name : 'workerName'
	}, {
		name : 'contactHarm'
	}, {
		name : 'contactYear'
	}, {
		name : 'checkDate'
	}, {
		name : 'hospital'
	}, {
		name : 'content'
	}, {
		name : 'checkResult'
	}, {
		name : 'memo'
	}]);
	var dataProxy = new Ext.data.HttpProxy({
				url : 'security/findWorkDiseasesList.action'
			});

	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	}, MyRecord);
	var store = new Ext.data.Store({
		proxy : dataProxy,
		reader : theReader
	});
	//分页
	store.load({
		params : {
			start : 0,
			limit : 18
		}
	});
	// 职工姓名
	var workerName = new Ext.form.TextField({
		id : 'workerName',
		xtype : 'textField',
	   fieldLabel : "职工姓名",
        name : 'workerName',
        anchor : "75%"
	});
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true
	});
	var grid = new Ext.grid.GridPanel({
		region : "center",
		store : store,
		columns : [sm, new Ext.grid.RowNumberer({
			header : '序号',
			width : 35,
			align : 'left'
		}),{
			header : "体检编号",
			width : 110,
			sortable : true,
			align : 'left',
			dataIndex : 'medicalId'
		}, {
			header : "职工编码",
			width : 80,
			sortable : true,
			hidden : true,
			align : 'left',
			dataIndex : 'workerCode'
		}, {
			header : "职工名称",
			width : 130,
			sortable : true,
			align : 'left',
			dataIndex : 'workerName'
		}, {
			header : "接触危害",
			width : 80,
			sortable : true,
			align : 'left',
			dataIndex : 'contactHarm'
		}, {
			header : "接触年数",
			width : 120,
			sortable : true,
			align : 'left',
			dataIndex : 'contactYear'
		}, {
			header : "检查时间",
			width : 120,
			sortable : true,
			align : 'left',
			dataIndex : 'checkDate'
		}, {
			header : "医院",
			width : 120,
			sortable : true,
			align : 'left',
			dataIndex : 'hospital'
		}, {
			header : "体检内容",
			width : 130,
			sortable : true,
			align : 'left',
			dataIndex : 'content'
		}, {
			header : "体检结果",
			width : 130,
			sortable : true,
			align : 'left',
			dataIndex : 'checkResult'
		}, {
			header : "备注",
			width : 130,
			sortable : true,
			align : 'left',
			dataIndex : 'memo'
		}],
		sm : sm,
		tbar : ["职工名称:", workerName, {
			text : "查询",
			iconCls : 'query',
			handler : queryRecord
		}, {
			text : "查看详细信息",
			iconCls : 'update',
			handler : updateRecord
		}],
		//分页
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : store,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		})
	});

	grid.on("rowdblclick", updateRecord);
	
	// ---------------------------------------
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		items : [grid]
	});
	// -------------------
	//定义FORM
	 var medicalId = new Ext.form.TextField({
		id : "medicalId",
		fieldLabel : '体检编号',
		xtype : "textfield",
		readOnly : true,
		name : 'disease.medicalId',
		anchor : "85%"
	});
	
	var workerCode = new Ext.form.TextField({
		fieldLabel : '职工姓名',
		name : 'disease.workerCode',
		xtype : 'textfield',
		id : 'workerCode',
		readOnly : true,
		anchor : "85%"
	});

	var contactHarm = new Ext.form.TextField({
		id : 'contactHarm',
		xtype : "textfield",
		fieldLabel : "接触危害",
		name : 'disease.contactHarm',
		readOnly : true,
		anchor : "85%"
	});

	var contactYear = new Ext.form.NumberField({
		id : 'contactYear',
		xtype : "NumberField",
		fieldLabel : "接触年数",
		name : 'disease.contactYear',
		readOnly : true,
		maxLength : 2,
		maxLengthText : '最多输入2个数字！',
		anchor : "85%"
	});

	var checkDate = new Ext.form.TextField({
		id : 'checkDate',
		fieldLabel : "检查时间",
		name : 'disease.checkDate',
		style : 'cursor:pointer',
		readOnly : true,
		anchor : "85%",
		value : getDate()
//		listeners : {
//			focus : function() {
//				WdatePicker({
//					startDate : '%y-%M-%d 00:00:00',
//					dateFmt : 'yyyy-MM-dd HH:mm:ss',
//					alwaysUseStartDate : true
//				});
//			}
//		}
	});
	var hospital = new Ext.form.TextField({
		id : "hospital",
		// height:90,
		xtype : "textfield",
		fieldLabel : '医院',
		name : 'disease.hospital',
		readOnly : true,
		anchor : "92.3%"
	});
	var content = new Ext.form.TextArea({
		id : "content",
		height : 50,
		fieldLabel : '体检内容',
		name : 'disease.content',
		readOnly : true,
		anchor : "92.3%"
	});

	var checkResult = new Ext.form.TextArea({
		id : "checkResult",
		height : 50,
		fieldLabel : '体检结果',
		name : 'disease.checkResult',
		readOnly : true,
		anchor : "92.3%"
	});

	var memo = new Ext.form.TextArea({
		id : "memo",
		height : 50,
		fieldLabel : '备注',
		name : 'disease.memo',
		readOnly : true,
		anchor : "92.3%"
	});
	
	var sexBox = new Ext.form.TextField({
		id : "sex",
		fieldLabel : '性别',
		readOnly : true,
		name : 'emp.sex',
		anchor : "85%"	
	});

	var deptBox = new Ext.form.TextField({
		id : "dept",
		fieldLabel : '部门',
		name : 'emp.deptName',
		readOnly : true,
		anchor : "85%"
	});
	var stationBox = new Ext.form.TextField({
		id : "station",
		fieldLabel : '岗位',
		name : 'emp.workStationName',
		readOnly : true,
		anchor : "85%"
	});
	var socialInsuranceBox = {
		id : "socialInsurance",
		xtype : "textfield",
		fieldLabel : '社保卡号',
		name : 'emp.socialInsuranceId',
		readOnly : true,
		anchor : "85%"
	};
var empType = {
		id : "type",
		xtype : "textfield",
		fieldLabel : '员工类别',
		name : 'emp.empTypeId',
		readOnly : true,
		anchor : "85%"
	};
	var curriculumVitae = new Ext.form.TextArea({
		id : "curriculumVitae",
		height : 50,
		fieldLabel : '员工简历',
		name : 'emp.curriculumVitae',
		readOnly : true,
		anchor : "92.3%"
	});
	var myaddpanel = new Ext.FormPanel({
		title : '职业病检查信息',
		height : '100%',
		layout : 'form',
		frame : true,
		labelAlign : 'center',
		items : [{
			border : false,
			layout : 'column',
			items : [{
				columnWidth : .5,
				layout : 'form',
				border : false,
				labelWidth : 70,
				items : [medicalId,workerCode, deptBox]
			}, {
				columnWidth : .5,
				layout : 'form',
				labelWidth : 70,
				border : false,
				items : [sexBox, stationBox, socialInsuranceBox]
			}]
		}, {
			border : false,
			layout : 'form',
			columnWidth : 1,
			labelWidth : 70,
			items : [curriculumVitae]
		},{
			border : false,
			layout : 'column',
			items : [{
				columnWidth : .5,
				layout : 'form',
				border : false,
				labelWidth : 70,
				items : [empType,contactHarm]
				
			}, {
				columnWidth : .5,
				layout : 'form',
				labelWidth : 70,
				border : false,
				items : [checkDate,contactYear]
			}]
		}, {
			border : false,
			layout : 'form',
			columnWidth : 1,
			labelWidth : 70,
			items : [hospital]
		}, {
			border : false,
			layout : 'form',
			columnWidth : 1,
			labelWidth : 70,
			items : [content]
		}, {
			border : false,
			layout : 'form',
			columnWidth : 1,
			labelWidth : 70,
			items : [checkResult]
		}, {
			border : false,
			layout : 'form',
			columnWidth : 1,
			labelWidth : 70,
			items : [memo]
		}]
		});
var win = new Ext.Window({
		width : 550,
		height : 460,
		buttonAlign : "center",
		items : [myaddpanel],
		layout : 'fit',
		closeAction : 'hide',
		draggable : true,
		modal : true,
		buttons : [{
			text : '关闭',
			iconCls : 'cancer',
			handler : function() {
				win.hide();
			}
		}]
	});

	// 查询
	function queryRecord() {
		store.load({
			params : {
				start : 0,
				limit : 18,
				workerName : workerName.getValue()
			}
		});
	}

	function updateRecord() {
		if (grid.selModel.hasSelection()) {
			var record = grid.getSelectionModel().getSelected();
			win.show();
			myaddpanel.getForm().loadRecord(record);
			myaddpanel.setTitle("详细信息");
			Ext.Ajax.request({
				url : 'security/getInfoByWorkCode.action',
				method : 'post',
				params : {
					workCode : Ext.get('workerCode').dom.value
				},
				success : function(action) {
					var result = eval("(" + action.responseText + ")");
					//	Ext.get('workerCode').dom.value = result[0];
					if (result.sex != null) {
						Ext.get('sex').dom.value = result.sex;
					}
					if (result.deptName != null) {
						Ext.get('dept').dom.value = result.deptName;
					}
					if (result.workStationName != null) {
						Ext.get('station').dom.value = result.workStationName;
					}
					if (result.empType != null) {
						Ext.get('type').dom.value = result.empType;
					}
					if (result.socialInsuranceId != null) {
						Ext.get('socialInsurance').dom.value = result.socialInsuranceId;
					}
					if (result.curriculumVitae != null) {
						Ext.get('curriculumVitae').dom.value = result.curriculumVitae;
					}
					if (result.empName != null) {
						Ext.get('workerCode').dom.value = result.empName;
					}
				}
			});
		}
	}
});