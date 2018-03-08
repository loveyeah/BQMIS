Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif'; 
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
Ext.onReady(function() {
	var actionId = parent.actionId;
	var nrs = parent.nrs;
	var formJson = window.dialogArguments;
	var eliminateTypeComboBox = new Ext.form.ComboBox({
		id : 'eliminateType-combobox',
		store : new Ext.data.SimpleStore({
			fields : ["retrunValue", "displayText"],
			data : [['1', '正常消缺'], ['2', '低谷消缺'], ['3', '降负荷消缺'], ['4', '停机消缺']]
		}),
		fieldLabel : "消缺方式<font color='red'>*</font>",
		valueField : "retrunValue",
		displayField : "displayText",
		mode : 'local',
		forceSelection : true,
		hiddenName : 'failurehis.eliminateType',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : false,
		anchor : '95%'
	});

	var resultComboBox = new Ext.form.ComboBox({
		id : 'result-combobox',
		store : new Ext.data.SimpleStore({
			fields : ["retrunValue", "displayText"],
			data : [['1', '正常'], ['2', '不正常']]
		}),
		fieldLabel : "处理结果<font color='red'>*</font>",
		valueField : "retrunValue",
		displayField : "displayText",
		mode : 'local',
		forceSelection : true,
		hiddenName : 'failurehis.tackleResult',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : false,
		anchor : '95%'
	})

	var eliminateField = new Ext.form.FieldSet({
		title : '消缺',
		autoWidth : true,
		// autoHeight : true,
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
					fieldLabel : "管辖专业",
					name : "belongProfession_text",
					anchor : "95%",
					readOnly : true
				}, {
					xtype : 'hidden',
					name : 'belongProfession'
				}]
			}, {
				columnWidth : 0.5,
				layout : "form",
				border : false,
				items : [{
					xtype : "textfield",
					fieldLabel : "检修部门",
					name : "chargeClass_text",
					anchor : "95%",
					readOnly : true
				}, {
					xtype : "hidden",
					name : "chargeClass"
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
					fieldLabel : "交代内容<font color='red'>*</font>",
					name : "failurehis.approveOpinion",
					anchor : "97%",
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
				items : [eliminateTypeComboBox]
			}, {
				columnWidth : 0.43,
				layout : "form",
				border : false,
				items : [{
					xtype : "textfield",
					fieldLabel : "工作负责人<font color='red'>*</font>",
					name : "chargeMan_text",
					anchor : "100%",
					readOnly : true,
					allowBlank : false
				}, {
					xtype : "hidden",
					name : "failurehis.chargeMan"
				}]
			}, {
				columnWidth : 0.05,
				layout : "form",
				border : false,
				items : [{
					xtype : "button",
					text : '..',
					name : 'select',
					handler : function() {
						var args = {
							selectModel : 'single',
							notIn : "'999999'",
							rootNode : {
								id : '-1',
								text : '灞桥电厂'
							}
						};
						var obj = window
								.showModalDialog(
										'../../../comm/jsp/hr/workerByDept/workerByDept2.jsp',
										args,
										'dialogWidth=600px;dialogHeight=420px;center=yes;help=no;resizable=no;status=no;');
						if (typeof(obj) == 'object') {
							Ext.get("failurehis.chargeMan").dom.value = obj.workerCode;
							Ext.get("chargeMan_text").dom.value = obj.workerName;
						}
					}
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
					fieldLabel : "消缺人",
					name : "cleanMan_text",
					readOnly : true,
					value : document.getElementById('workName').value,
					anchor : "95%"
				}, {
					xtype : "hidden",
					fieldLabel : "消缺人",
					name : "failurehis.cleanMan",
					readOnly : true,
					value : document.getElementById('workCode').value,
					anchor : "95%"
				}]
			}, {
				columnWidth : 0.5,
				layout : "form",
				border : false,
				items : [resultComboBox]
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
					fieldLabel : "消缺班组",
					name : "cleanClass_text",
					readOnly : true,
					value : document.getElementById('deptName').value,
					anchor : "95%"
				}, {
					xtype : "hidden",
					fieldLabel : "消缺班组",
					name : "failurehis.eliminateClass",
					readOnly : true,
					value : document.getElementById('deptCode').value
				}]
			}, {
				columnWidth : 0.5,
				layout : "form",
				border : false,
				items : [{
					xtype : "textfield",
					fieldLabel : "消缺时间",
					name : "cleanTime",
					readOnly : true,
					value : getDate(),
					anchor : "95%"
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
					anchor : "95%",
					value : document.getElementById('workCode').value
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
					anchor : "95%"
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
			items : [eliminateField],
			buttons : [{
				id : 'btnFailureHandle',
				xtype : 'button',
				text : "消缺",
				iconCls : 'save',
				handler : function() {
					if (failureApproveFormPanel.getForm().isValid()) {
						failureApproveFormPanel.getForm().submit({
								url : 'bqfailure/eliminateFailure.action',
								waitMsg : '正在处理数据...',
								method : 'post',
								params : {
									actionId : actionId,
									nextRoles : parent.nrs,
									"failure.id" : formJson.data.id
								},
								success : function(result, request) {
									Ext.Msg.alert("提示", "消缺成功！");
									window.close();
									ds.load();
								},
								failure : function(form, action) {
									Ext.Msg.alert("消缺失败",
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
					ds.load();
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
		items : [failureApproveFormPanel]
	});
	Ext.get("belongProfession_text").dom.value = formJson.data.dominationProfessionName;
	Ext.get("belongProfession").dom.value = formJson.data.dominationProfession;
	Ext.get("chargeClass_text").dom.value = formJson.data.repairDepName;
	Ext.get("chargeClass").dom.value = formJson.data.repairDep;
});