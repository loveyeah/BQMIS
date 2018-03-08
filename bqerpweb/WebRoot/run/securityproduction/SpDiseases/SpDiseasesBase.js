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
	//	singleSelect : true
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
			text : "新增",
			iconCls : 'add',
			handler : addRecord
		}, {
			text : "修改",
			iconCls : 'update',
			handler : updateRecord
		}, {
			text : "删除",
			iconCls : 'delete',
			handler : deleteRecord
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
	var workerCode = {
		fieldLabel : '职工姓名',
		name : 'workerCode',
		xtype : 'combo',
		id : 'workerCode',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		hiddenName : 'disease.workerCode',
		allowBlank : false,
		editable : false,
		anchor : "85%",
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
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
				Ext.getCmp('workerCode').setValue(emp.workerCode);
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('workerCode'), emp.workerName);
				Ext.Ajax.request({
					url : 'security/getInfoByWorkCode.action',
					method : 'post',
					params : {
						workCode : emp.workerCode
					},
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						// alert(action.responseText);
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
	};
	var contactHarm = new Ext.form.TextField({
		id : 'contactHarm',
		xtype : "textfield",
		fieldLabel : "接触危害",
		name : 'disease.contactHarm',
		anchor : "85%"
	});

	var contactYear = new Ext.form.NumberField({
		id : 'contactYear',
		xtype : "NumberField",
		fieldLabel : "接触年数",
		name : 'disease.contactYear',
		maxLength : 2,
		maxLengthText : '最多输入2个数字！',
		anchor : "85%"
	});

	var checkDate = new Ext.form.TextField({
		id : 'checkDate',
		fieldLabel : "检查时间",
		name : 'disease.checkDate',
		style : 'cursor:pointer',
		anchor : "85%",
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
	var hospital = new Ext.form.TextField({
		id : "hospital",
		// height:90,
		xtype : "textfield",
		fieldLabel : '医院',
		name : 'disease.hospital',
		anchor : "92.3%"
	});
	var content = new Ext.form.TextArea({
		id : "content",
		height : 50,
		fieldLabel : '体检内容',
		name : 'disease.content',
		anchor : "92.3%"
	});

	var checkResult = new Ext.form.TextArea({
		id : "checkResult",
		height : 50,
		fieldLabel : '体检结果',
		name : 'disease.checkResult',
		anchor : "92.3%"
	});

	var memo = new Ext.form.TextArea({
		id : "memo",
		height : 50,
		fieldLabel : '备注',
		name : 'disease.memo',
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
		title : '职业病检查增加/修改',
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
			text : '保存',
			iconCls : 'save',
			handler : function() {
				var myurl = "";
				if (Ext.get("medicalId").dom.value == ""
						|| Ext.get("medicalId").dom.value == null) {
					myurl = "security/addWorkDiseasesInfo.action";
				} else {
					myurl = "security/updateWorkDiseasesInfo.action";
				}
				myaddpanel.getForm().submit({
					method : 'POST',
					url : myurl,
					success : function(form, action) {
						var o = eval("(" + action.response.responseText + ")");
						Ext.Msg.alert("提示", o.msg);
						workerName.setValue("");
						store.load({
							params : {
								start : 0,
								limit : 18
							}
						});
						win.hide();
					},
					faliue : function() {
						Ext.Msg.alert('错误', '出现未知错误.');
					}
				});
			}
		}, {
			text : '取消',
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

	function addRecord() {
		myaddpanel.getForm().reset();
		win.setPosition(100, 50);
		win.show();
		myaddpanel.setTitle("增加职业病检查登记");
		
	}
	function updateRecord() {
        if (grid.selModel.hasSelection()) {
            var records = grid.selModel.getSelections();
            var recordslen = records.length;
            if (recordslen > 1) {
                Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑！");
            } else {
                var record = grid.getSelectionModel().getSelected();
				win.show();
                myaddpanel.getForm().loadRecord(record);
                myaddpanel.setTitle("修改职业病检查登记");
				Ext.Ajax.request({
                	url : 'security/getInfoByWorkCode.action',
					method : 'post',
	                params : {
						workCode : Ext.get('workerCode').dom.value
					},
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
					//	Ext.get('workerCode').dom.value = result[0];
						if(result.sex != null)
						{
						Ext.get('sex').dom.value = result.sex;
						}
						if(result.deptName != null){
						Ext.get('dept').dom.value = result.deptName;
						}
						if(result.workStationName != null){
						Ext.get('station').dom.value = result.workStationName;
						}
						if(result.empType != null){
						Ext.get('type').dom.value = result.empType;
						}
						if(result.socialInsuranceId != null){
						Ext.get('socialInsurance').dom.value = result.socialInsuranceId;
						}
						if(result.curriculumVitae != null)
						{
						Ext.get('curriculumVitae').dom.value = result.curriculumVitae;
						}
						if(result.empName != null){
							Ext.get('workerCode').dom.value = result.empName;
						}
					}
				});
                }
        } else {
           Ext.Msg.alert("提示", "请先选择要编辑的行!");
        }
    }

	function deleteRecord() {
		var sm = grid.getSelectionModel();
		var selected = sm.getSelections();
		var ids = [];
		var names = [];
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i].data;
				if (member.medicalId) {
					ids.push(member.medicalId);
					names.push(member.workerName);
				} else {
					store.remove(store.getAt(i));
				}
			}
			Ext.Msg.confirm("删除", "是否确定删除名称为'" + names + "'的记录？", function(
					buttonobj) {
				if (buttonobj == "yes") {
					Ext.lib.Ajax.request('POST',
							'security/deleteWorkDiseasesInfo.action', {
								success : function(action) {
									Ext.Msg.alert("提示", "删除成功！")
									store.load({
										params : {
											start : 0,
											limit : 18
										}
									});
								},
								failure : function() {
									Ext.Msg.alert('错误', '删除时出现未知错误.');
								}
							}, 'ids=' + ids);
				}
			});
		}

	}
});