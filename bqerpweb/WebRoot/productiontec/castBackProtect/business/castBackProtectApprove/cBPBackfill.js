Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	Ext.QuickTips.init();
	var arg = window.dialogArguments;
	var status = arg.status;
	var applyId = arg.applyId;
	var workflowNo = arg.workflowNo;
	
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
	};

	var applyCode = new Ext.form.TextField({
				id : 'applyCode',
				name : 'protectApply.applyCode',
				fieldLabel : '编号',
				readOnly : true,
				anchor : '95%'
			});

	var inOut = new Ext.form.TextField({
				id : 'inOut',
				name : 'protectApply.inOut',
				fieldLabel : '投/退',
				readOnly : true,
				anchor : '90%'
			});

	var protectionType = new Ext.form.TextField({
				id : 'protectionType',
				name : 'protectApply.protectionType',
				fieldLabel : '投退保护类型',
				readOnly : true,
				anchor : '95%'
			});

	var applyDep = new Ext.form.TextField({
				id : 'applyDep',
				name : 'deptName',
				editable : false,
				readOnly : true,
				fieldLabel : '申请部门',
				anchor : "95%"
			});

	var applyBy = new Ext.form.Hidden({
				id : 'applyBy',
				name : 'protectApply.applyBy'

			});
	var applyName = new Ext.form.TextField({
				id : 'applyName',
				fieldLabel : '申请人',
				allowBlank : false,
				readOnly : true,
				name : 'applyName',
				anchor : "90%"
			});

	var applyTime = new Ext.form.TextField({
				id : 'applyTime',
				fieldLabel : "申请时间",
				name : 'protectApply.applyTime',
				style : 'cursor:pointer',
				anchor : "95%",
				readOnly : true
			});

	var protectionName = new Ext.form.TextArea({
				id : 'protectionName',
				name : 'protectApply.protectionName',
				fieldLabel : '保护名称',
				readOnly : true,
				anchor : '95%'
			});

	var protectionReason = new Ext.form.TextArea({
				id : 'protectionReason',
				name : 'protectApply.protectionReason',
				fieldLabel : '退出原因',
				readOnly : true,
				anchor : '95%'
			});

	var measures = new Ext.form.TextArea({
				id : 'measures',
				name : 'protectApply.measures',
				fieldLabel : '安全、技术措施',
				readOnly : true,
				anchor : '95%'
			});

	var memo = new Ext.form.TextArea({
				id : 'memo',
				name : 'protectApply.memo',
				fieldLabel : '备注',
				readOnly : true,
				anchor : '95%'
			});

	var executor = new Ext.form.Hidden({
				id : 'executor',
				name : 'protectApply.executor'

			});
	var executorName = new Ext.form.TextField({
				id : 'executorName',
				fieldLabel : '执行人',
				allowBlank : false,
				readOnly : true,
				name : 'executorName',
				anchor : "90%"
			});

	var classLeader = new Ext.form.Hidden({
				id : 'executor',
				name : 'protectApply.classLeader'

			})
	var classLeaderName = new Ext.form.TriggerField({
		id : 'classLeaderName',
		fieldLabel : '班长',
		allowBlank : false,
		name : 'classLeaderName',
		anchor : "95%",
		onTriggerClick : function() {
			var url = "/power/comm/jsp/hr/workerByDept/workerByDept2.jsp";
			var args = {
				selectModel : 'single',
				rootNode : {
					id : '1',
					text : '灞桥热电厂'
				}
			}
			var emp = window
					.showModalDialog(
							url,
							args,
							'dialogWidth:600px;dialogHeight:420px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
			if (typeof(emp) != "undefined") {
				classLeader.setValue(emp.workerCode);
				classLeaderName.setValue(emp.workerName);
			}
		}
	})

	var guardian = new Ext.form.Hidden({
				id : 'guardian',
				name : 'protectApply.guardian'

			})
	var guardianName = new Ext.form.TriggerField({
		id : 'guardianName',
		fieldLabel : '监护人',
		allowBlank : false,
		name : 'guardianName',
		anchor : "95%",
		onTriggerClick : function() {
			var url = "/power/comm/jsp/hr/workerByDept/workerByDept2.jsp";
			var args = {
				selectModel : 'single',
				rootNode : {
					id : '1',
					text : '灞桥热电厂'
				}
			}
			var emp = window
					.showModalDialog(
							url,
							args,
							'dialogWidth:600px;dialogHeight:420px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
			if (typeof(emp) != "undefined") {
				Ext.getCmp('guardian').setValue(emp.workerCode);
				Ext.getCmp('guardianName').setValue(emp.workerName);
			}
		}
	})
	var executeTime = new Ext.form.TextField({
				id : 'executeTime',
				fieldLabel : "投退时间",
				name : 'protectApply.executeTime',
				style : 'cursor:pointer',
				anchor : "90%",
				allowBlank : false,
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d HH:mm:ss',
									dateFmt : 'yyyy-MM-dd HH:mm:ss',
									alwaysUseStartDate : true
								});
					}
				}
			});
	var baseInfoField = new Ext.form.FieldSet({
				autoHeight : true,
				labelWidth : 70,
				style : {
					"padding-top" : '15',
					"padding-left" : '15'
				},
				bodyStyle : Ext.isIE
						? 'padding:0 0 5px 15px;'
						: 'padding:5px 15px;',

				anchor : '-20',
				border : false,
				buttonAlign : 'center',
				items : [{
							border : false,
							layout : 'column',
							items : [{
										columnWidth : .5,
										layout : 'form',
										border : false,
										items : [applyCode]
									}, {
										columnWidth : .5,
										layout : 'form',
										border : false,
										items : [inOut]
									}]
						}, {
							border : false,
							layout : 'column',
							items : [{
										columnWidth : 1,
										layout : 'form',
										border : false,
										items : [protectionType]
									}]
						}, {
							border : false,
							layout : 'column',
							items : [{
										columnWidth : .5,
										layout : 'form',
										border : false,
										items : [applyDep]
									}, {
										columnWidth : .5,
										layout : 'form',
										border : false,
										items : [applyName]
									}]
						}, {
							border : false,
							layout : 'column',
							items : [{
										columnWidth : .5,
										layout : 'form',
										border : false,
										items : [applyTime]
									}]
						}, protectionName, protectionReason, measures, memo, {
							border : false,
							layout : 'column',
							items : [{
								columnWidth : .5,
								layout : 'form',
								border : false,
								items : [classLeader, classLeaderName,
										guardian, guardianName]
							}, {
								columnWidth : .5,
								layout : 'form',
								border : false,
								items : [executorName, executeTime]
							}]
						}]
			});

	var form = new Ext.form.FormPanel({
				layout : 'fit',
				border : false,
				frame : true,
				fileUpload : true,
				items : [baseInfoField],
				buttons : [{
					text : '保存',
					iconCls : 'save',
					handler : function() {
						var url = "";
						url = "productionrec/saveCastBackProtect.action"
						form.getForm().submit({
							method : 'POST',
							url : url,
							params : {
								applyId : applyId,
								status : status,
								entryId : workflowNo
							},
							success : function(formt, action) {
								var o = eval("(" + action.response.responseText
										+ ")");
									Ext.Msg.confirm("注意", o.msg, function(button) {

									if (button == 'yes') {
										if (o.msg.indexOf("成功") != -1) {
											window.close();
										}
									}
								});
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
						window.close();
					}
				}],
				bodyStyle : {
					'padding-top' : '5px'
				},
				defaults : {
					labelAlign : 'center'
				}
			});
  function init(){
		Ext.Ajax.request({
					method : 'post',
					url : 'productionrec/getCastBackProtectApproveList.action',
					params : {
						applyId : applyId
					},
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						
						if (result.list.length > 0) {
							var str = Ext.encode(result.list);
							var ob = eval("("
									+ str.substring(1, str.length - 1) + ")")

							applyCode.setValue(ob[1]);
							if (ob[2] == 'I')
								inOut.setValue("投入申请单");
							else if (ob[2] == 'O')
								inOut.setValue("退出申请单");
							if (ob[3] == '1')
								protectionType.setValue("其它继电保护及安全自动装置、调度自动化设备投退");
							else if (ob[3] == '2')
								protectionType.setValue("380V电压等级及以下设备继电保护及安全自动装置投退");
							else if (ob[3] == '3')
								protectionType.setValue("热控保护投退");
							applyDep.setValue(ob[5]);
							applyName.setValue(ob[7]);
							applyTime.setValue(ob[8]);
							protectionName.setValue(ob[9]);
							protectionReason.setValue(ob[10]);
							measures.setValue(ob[11]);
							memo.setValue(ob[22]);
							classLeaderName.setValue(ob[16]);
							executorName.setValue(ob[18]);
							guardianName.setValue(ob[20]);
							executeTime.setValue(ob[21]);
						} 
					}
				});
	}
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		items : [{
					region : "center",
					layout : 'fit',
					border : true,
					collapsible : true,
					split : true,
					margins : '0 0 0 0',
					items : [form]
				}]
	});
init();
})