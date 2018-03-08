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
	var arbitrateTypeComboBox = new Ext.form.ComboBox({
				id : 'arbitrateType-combobox',
				store : new Ext.data.SimpleStore({
							fields : ["retrunValue", "displayText"],
							data : [['1', '管辖专业(部门内)仲裁'], ['2', '检修部门（部门间）仲裁'],
									['3', '验收仲裁'], ['4', '其它'], ['5', '类别仲裁'],
									['6', '停机消除仲裁']]
						}),
				fieldLabel : "仲裁类别<font color='red'>*</font>",
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

	var arbitrateField = new Ext.form.FieldSet({
		title : '申请仲裁',
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
											fieldLabel : "原检修部门",
											name : "repairDep",
											readOnly : true,
											value : '999999',
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
											xtype : "textfield",
											fieldLabel : "原管辖专业",
											name : "dominationProfession",
											readOnly : true,
											value : '222',
											anchor : "90%"
										}]
							}, {
								columnWidth : 0.5,
								layout : "form",
								border : false,
								items : [{
											xtype : "textfield",
											fieldLabel : "原类别",
											name : "failuretypeName",
											value : '222',
											readOnly : true,
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
								items : [arbitrateTypeComboBox]
							}, {
								columnWidth : 0.5,
								layout : "form",
								border : false,
								items : [{
									xtype : "textfield",
									fieldLabel : "申请仲裁人",
									name : "peopleName",
									readOnly : true,
									allowBlank : false,
									value : document.getElementById('workName').value,
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
									fieldLabel : "申请仲裁原因<font color='red'>*</font>",
									name : "failurehis.approveOpinion",
									anchor : "94%",
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
									anchor : "90%",
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
					items : [arbitrateField],
					buttons : [{
						id : 'btnFailureHandle',
						xtype : 'button',
						text : "申请仲裁",
						iconCls : 'save',
						handler : function() {
							if (failureApproveFormPanel.getForm().isValid()) {
								if (parent.nrs == "") {
									if(Ext.get("arbitrateType-combobox").dom.value == '管辖专业(部门内)仲裁' ||
									Ext.get("arbitrateType-combobox").dom.value == '检修部门（部门间）仲裁')
									{
										
									Ext.Msg.alert("提示", "请选择下步角色!");
									return;}
									
								}
								
									//----------------------------------------
								failureApproveFormPanel.getForm().submit({
								url : 'bqfailure/applyArbitrateFailure.action',
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
							// ----------------------------------------
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
	Ext.get("dominationProfession").dom.value = formJson.data.dominationProfessionName;
	Ext.get("repairDep").dom.value = formJson.data.repairDepName;
	Ext.get("failuretypeName").dom.value = formJson.data.failuretypeName;

});