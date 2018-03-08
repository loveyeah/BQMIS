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
	var awaitTypeComboBox = new Ext.form.ComboBox({
		id : 'awaitType-combobox',
		store : new Ext.data.SimpleStore({
			fields : ["retrunValue", "displayText"],
			data : [['1', '无备品'], ['2', '等待停机处理'], ['3', '其它']]
		}),
		fieldLabel : "延期类别<font color='red'>*</font>",
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
	var delayTime = new Ext.form.TextField({
		fieldLabel : "申请延长日期到<font color='red'>*</font>",
		id : 'delayTime',
		name : "failurehis.delayDate",
		cls : 'Wdate',
		style : 'cursor:pointer',
		allowBlank : false,
		anchor : "90%",
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
	var awaitField = new Ext.form.FieldSet({
		title : '申请延期',
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
					fieldLabel : "缺陷编号",
					name : "failureCode",
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
				items : [awaitTypeComboBox]
			}, {
				columnWidth : 0.5,
				layout : "form",
				border : false,
				items : [{
					xtype : "textfield",
					fieldLabel : "申请人",
					name : "approvePeopleName",
					readOnly : true,
					allowBlank : false,
					value : document.getElementById("workName").value,
					anchor : "90%"
				}]
			}]
		}, {
			layout : "column",
			border : false,
			items : [{
				columnWidth : .5,
				layout : "form",
				border : false,
				items : [delayTime]
			}]
		},{
			layout : "column",
			border : false,
			items : [{
				columnWidth : 1,
				layout : "form",
				border : false,
				items : [{
					xtype : "textarea",
					fieldLabel : "延期原因<font color='red'>*</font>",
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
			items : [awaitField],
			buttons : [{
				id : 'btnFailureHandle',
				xtype : 'button',
				text : "申请延期",
				iconCls : 'save',
				handler : function() {
					if (failureApproveFormPanel.getForm().isValid()) {
						failureApproveFormPanel.getForm().submit({
							url : 'bqfailure/applyAwaitFailure.action',
							waitMsg : '正在处理数据...',
							method : 'post',
							params : {
								actionId : actionId,
								nextRoles : parent.nrs,
								"failure.id" : formJson.data.id
							},
							success : function(result, request) {
								Ext.Msg.alert("申请成功！");
								window.close();
								ds.load();
							},
							failure : function(form, action) {
								Ext.Msg.alert("申请失败",
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
	Ext.get("failureCode").dom.value = formJson.data.failureCode;

});