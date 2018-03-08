Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var formJson = window.dialogArguments;
	var actionId = parent.actionId;
	var nrs = parent.nrs;
	var workerCode = document.getElementById("workCode").value;
	var workerName = document.getElementById("workName").value;
	var invalidForm = new Ext.form.FieldSet({
		id : 'failure-invalid-form',
		title:'缺陷作废',
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
					fieldLabel : "作废原因<font color='red'>*</font>",
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
			items : [invalidForm],
			buttons : [{
				text : '作废',
				iconCls : 'save',
				handler : function() {
					if (failureApproveFormPanel.getForm().isValid()) {
						if (confirm("确定要作废该缺陷吗？")) {
							failureApproveFormPanel.getForm().submit({
								url : 'bqfailure/invalidFailure.action',
								params : {
									actionId : actionId,
									nextRoles : nrs,
									"failure.id" : formJson.data.id
								},
								waitMsg : '正在处理数据...',
								method : 'post',
								success : function(form, action) {
									Ext.Msg.alert("提示","处理成功！");
								  failureApproveFormPanel.getForm().reset();
									window.close();
								},
								failure : function(form, action) {
									Ext.Msg.alert("提示",
											action.result.errorMessage);
								}
							});
						}

					}
				}
			}, {
				text : '取消',
				iconCls : 'cancer',
				handler : function() {
					invalidWin.hide();
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
