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
Ext.onReady(function() {
	var formJson = window.dialogArguments;
	var actionId = parent.actionId;
	var nrs = parent.nrs;
	var eventIdentify = parent.eventIdentify;
	var hisInfo = parent.hisInfo;
	//-----------add by fyyang 091214-------------------------
	var cbNeedStop = new Ext.form.ComboBox({
		id : 'cbStop',
		store : new Ext.data.SimpleStore({
			fields : ["retrunValue", "displayText"],
			data : [['N', '否'], ['Y', '是']]
		}),
		fieldLabel : '是否需停机',
		valueField : "retrunValue",
		displayField : "displayText",
		mode : 'local',
		forceSelection : true,
		hiddenName : 'failurehis.approveRemark',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : false,
		anchor : '90%',
		value : ''
	});
	//--------------------------------------------------------
	
	var failureTypeUrl = "bqfailure/findEquFailureTypelist.action";
	var failureTypeconn = Ext.lib.Ajax.getConnectionObject().conn;
	failureTypeconn.open("POST", failureTypeUrl, false);
	failureTypeconn.send(null);
	var dsfailureTypeGrid = eval('(' + failureTypeconn.responseText + ')').failureTypelist;
	var dsfailureTypeComboBoxList = new Ext.data.Record.create([{
		name : 'failuretypeCode'
	}, {
		name : 'failuretypeName'
	}]);

	var dsfailureTypeComboBoxGrids = new Ext.data.JsonStore({
		data : dsfailureTypeGrid,
		fields : dsfailureTypeComboBoxList
	});
	var failureTypeComboBox = new Ext.form.ComboBox({
		id : 'failureType-combobox',
		store : dsfailureTypeComboBoxGrids,
		fieldLabel : '仲裁后缺陷类别',
		valueField : "failuretypeCode",
		displayField : "failuretypeName",
		mode : 'local',
		forceSelection : true,
		hiddenName : 'failurehis.arbitrateKind',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : false,
		anchor : '90%',
		value : formJson.data.failuretypeCode, 
		disabled : true
	});

	var dominationProfessionUrl = "bqfailure/dominationProfessionList.action";
	var dominationProfessionconn = Ext.lib.Ajax.getConnectionObject().conn;
	dominationProfessionconn.open("POST", dominationProfessionUrl, false);
	dominationProfessionconn.send(null);
	var dominationProfessionGrid = eval('('
			+ dominationProfessionconn.responseText + ')').list;
	var dominationProfessionComboBoxList = new Ext.data.Record.create([{
		name : 'specialityCode'
	}, {
		name : 'specialityName'
	}]);

	var dominationProfessionComboBoxGrids = new Ext.data.JsonStore({
		data : dominationProfessionGrid,
		fields : dominationProfessionComboBoxList
	});
	var dominationProfessionComboBox = new Ext.form.ComboBox({
		id : 'dominationProfession-combobox',
		store : dominationProfessionComboBoxGrids,
		fieldLabel : '仲裁后管辖专业',
		valueField : "specialityCode",
		displayField : "specialityName",
		mode : 'local',
		forceSelection : true,
		hiddenName : 'failurehis.arbitrateProfession',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : false,
		anchor : '90%',
		value : formJson.data.dominationProfession,
		disabled : true
	});

	var repairDeptUrl = "bqfailure/repairDept.action";
	var repairDeptconn = Ext.lib.Ajax.getConnectionObject().conn;
	repairDeptconn.open("POST", repairDeptUrl, false);
	repairDeptconn.send(null);
	var repairDeptGrid = eval('(' + repairDeptconn.responseText + ')').list;
	var repairDeptComboBoxList = new Ext.data.Record.create([{
		name : 'deptCode'
	}, {
		name : 'deptName'
	}]);

	var repairDeptComboBoxGrids = new Ext.data.JsonStore({
		data : repairDeptGrid,
		fields : repairDeptComboBoxList
	});
	var repairDepComboBox = new Ext.form.ComboBox({
		id : 'repairDep-combobox',
		store : repairDeptComboBoxGrids,
		fieldLabel : '仲裁后检修部门',
		valueField : "deptCode",
		displayField : "deptName",
		mode : 'local',
		forceSelection : true,
		hiddenName : 'failurehis.arbitrateDept',
		editable : true,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : false,
		anchor : '90%',
		value : formJson.data.repairDep,
		disabled : true
	});
	var checkarbitrateTypeComboBox = new Ext.form.ComboBox({
		id : 'checkarbitrateType-combobox',
		store : new Ext.data.SimpleStore({
			fields : ["retrunValue", "displayText"],
			data : [['1', '已消除可验收'], ['2', '未消除继续消缺']]
		}),
		fieldLabel : '验收仲裁类别',
		valueField : "retrunValue",
		displayField : "displayText",
		mode : 'local',
		forceSelection : true,
		hiddenName : 'failurehis.checkarbitrateTyp',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : false,
		anchor : '90%',
		value : '2',
		disabled : true
	});
	checkarbitrateTypeComboBox.on('beforequery', function() {
		return false
	});
	var arbitrateType = new Ext.form.ComboBox({
		id : 'arbitrateType',
		store : new Ext.data.SimpleStore({
			fields : ["retrunValue", "displayText"],
			data : [['1', '管辖专业（部门内）仲裁'], ['2', '检修部门（部门间）仲裁'], ['3', '验收仲裁'],
					['4', '其它'], ['5', '类别仲裁'],['6','停机消除仲裁']]
		}),
		fieldLabel : '仲裁类别',
		valueField : "retrunValue",
		displayField : "displayText",
		mode : 'local',
		forceSelection : true,
		hiddenName : 'failurehis.arbitrateType',
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : false,
		anchor : '90%'
	});
	arbitrateType.on('beforequery', function() {
		return false
	});
	var arbitrateField = new Ext.form.FieldSet({
		title : '缺陷仲裁',
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
				items : [cbNeedStop]
			}]
		},{
			layout : "column",
			border : false,
			items : [{
				columnWidth : 0.5,
				layout : "form",
				border : false,
				items : [arbitrateType]
			}, {
				columnWidth : 0.5,
				layout : "form",
				border : false,
				items : [{
					xtype : "textfield",
					fieldLabel : "仲裁申请人",
					id : "approve_people_name",
					readOnly : true,
					anchor : "90%"
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
					fieldLabel : "仲裁原因",
					id : "approve_remark",
					anchor : "94%",
					readOnly : true,
					height : 60
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
					fieldLabel : "仲裁意见<font color='red'>*</font>",
					id : "failurehis.approveOpinion",
					anchor : "94%",
					allowBlank : false,
					height : 60
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
					fieldLabel : "原检修部门",
					name : "repairDep",
					readOnly : true,
					anchor : "90%"
				}]
			}, {
				columnWidth : 0.5,
				layout : "form",
				border : false,
				items : [repairDepComboBox]
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
					fieldLabel : "原管辖专业",
					name : "dominationProfession",
					readOnly : true,
					anchor : "90%"
				}]
			}, {
				columnWidth : 0.5,
				layout : "form",
				border : false,
				items : [dominationProfessionComboBox]
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
					fieldLabel : "仲裁时间",
					name : "failurehis.approveTime",
					readOnly : true,
					value : getDate(),
					anchor : "90%"
				}]
			}, {
				columnWidth : 0.5,
				layout : "form",
				border : false,
				items : [checkarbitrateTypeComboBox]
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
					fieldLabel : "缺陷类别",
					name : "failuretypeName",
					readOnly : true,
					anchor : "90%"
				}]
			}, {
				columnWidth : 0.5,
				layout : "form",
				border : false,
				items : [failureTypeComboBox]
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
					value : document.getElementById('workCode').value,
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
		border : false,
		layout : "column",
		border : false,
		items : [{
			columnWidth : 0.1,
			border : false
		}, {
			columnWidth : 0.8,
			border : false,
			buttonAlign : 'center',
			layout : 'form',
			items : [arbitrateField],
			buttons : [{
				id : 'btnFailureHandle',
				xtype : 'button',
				text : "仲裁",
				iconCls : 'save',
				handler : function() {
					if (failureApproveFormPanel.getForm().isValid()) {
						failureApproveFormPanel.getForm().submit({
							url : 'bqfailure/arbitrateFailure.action',
							params : {
								actionId : actionId,
								eventIdentify : eventIdentify,
								actionId : actionId,
								nextRoles : parent.nrs,
								"failure.id" : formJson.data.id
							},
							waitMsg : '正在处理数据...',
							method : 'post',
							success : function(result, request) {
								Ext.Msg.alert("仲裁成功！");
								window.close();
								ds.load();
							},
							failure : function(form, action) {
								Ext.Msg.alert("仲裁失败",
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

	var viewport = new Ext.Viewport({
		layout : "fit",
		items : [failureApproveFormPanel]
	});

	arbitrateType.setValue(hisInfo.data.arbitrateType);
	Ext.get('approve_people_name').dom.value = hisInfo.data.approvePeopleName;
	Ext.get('approve_remark').dom.value = hisInfo.data.approveOpinion;
	if (hisInfo.data.arbitrateType == '1') {
		dominationProfessionComboBox.enable();
	}
	if (hisInfo.data.arbitrateType == '2') {
		repairDepComboBox.enable();
	}
	if (hisInfo.data.arbitrateType == '3') {
		checkarbitrateTypeComboBox.enable();
	}
	if (hisInfo.data.arbitrateType == '4') {
		dominationProfessionComboBox.enable();
		repairDepComboBox.enable();
		failureTypeComboBox.enable();
		checkarbitrateTypeComboBox.enable();
	}
	if (hisInfo.data.arbitrateType == '5') {
		failureTypeComboBox.enable();
	}
	failureApproveFormPanel.getForm().loadRecord(formJson);
	Ext.get("dominationProfession").dom.value = formJson.data.dominationProfessionName;
	Ext.get("repairDep").dom.value = formJson.data.repairDepName;
	Ext.get("failuretypeName").dom.value = formJson.data.failuretypeName;
});