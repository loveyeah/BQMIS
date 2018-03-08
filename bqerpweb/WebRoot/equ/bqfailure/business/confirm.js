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
	//alert(formJson.data.repairDep);
	//alert(formJson.data.failuretypeCode);
	var repairDeptStore = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'bqfailure/repairDept.action',
			method : 'post'
		}),
		reader : new Ext.data.JsonReader({
			root : 'list'
		}, [{
			name : 'deptCode',
			mapping : 'deptCode'
		}, {
			name : 'deptName',
			mapping : 'deptName'
		}])
	}); 
	repairDeptStore.load();
	var repairDepComboBox = new Ext.form.ComboBox({
		id : 'repairDep-combobox',
		store : repairDeptStore,
		fieldLabel : "检修部门<font color='red'>*</font>",
		valueField : "deptCode",
		displayField : "deptName",
		mode : 'local',
		forceSelection : true,
		hiddenName : 'failure.repairDep',
		editable : true,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : false,
		blankText : '请选择...',
		emptyText : '请选择...',
		anchor : '95%',
		typeAhead : true
	});
	repairDeptStore.on("load",function(){
		repairDepComboBox.setValue(formJson.data.repairDep); 
	});
	
	
	
	var nrs = parent.nrs;
	var workerCode = document.getElementById("workCode").value;
	var workerName = document.getElementById("workName").value;
	var sendbackForm = new Ext.form.FieldSet({
		id : 'failure-sendback-form',
		title : '缺陷确认',
		autoWidth : true,
		autoHeight : true,
		anchor : '100%',
		items : [{
			layout : "column",
			border : false,
			items : [{
				columnWidth : 1,
				layout : "form",
				border : false,
				items : [{
					xtype : "textarea",
					fieldLabel : "确认意见<font color='red'>*</font>",
					name : "failurehis.approveOpinion",
					anchor : "94.5%",
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
					xtype : 'textfield',
					fieldLabel : '确认人',
					name : 'approvePeopleName',
					allowBlank : false,
					readOnly : true,
					value : document.getElementById("workName").value,
					anchor : "90%"
				}]
			}, {
				columnWidth : 0.5,
				layout : "form",
				border : false,
				items : [{
					xtype : "textfield",
					fieldLabel : "确认时间",
					name : "approveTime",
					readOnly : true,
					value : getDate(),
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
					xtype : 'textfield',
					fieldLabel : '工号',
					name : 'failurehis.approvePeople',
					allowBlank : false,
					readOnly : true,
					value : document.getElementById("workCode").value,
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
			layout : 'form',
			buttonAlign : 'center',
			items : [sendbackForm],
			buttons : [{
				text : '确认',
				iconCls : 'save',
				handler : function() { 
					if (failureApproveFormPanel.getForm().isValid()) {
						failureApproveFormPanel.getForm().submit({
							url : 'bqfailure/confirmFailure.action',
							params : {
								actionId : actionId,
								nextRoles : parent.nrs,
								"failure.id" : formJson.data.id,
								"repairDept":repairDepComboBox.getValue()
							},
							waitMsg : '正在处理数据...',
							method : 'post',
							success : function(result, request) {
								Ext.Msg.alert("提示", "确认成功！");
								failureApproveFormPanel.getForm().reset();
								window.close();
							},
							failure : function(result, request) {
								Ext.Msg.alert("提示",
										action.result.errorMessage);
							}
						});
					}
				}
			}, {
				text : '取消',
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
})
