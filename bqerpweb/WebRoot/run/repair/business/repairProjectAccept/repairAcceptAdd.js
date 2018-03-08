Ext.ns("repairMaint.approveList");

repairMaint.approveList = function() {

	function getYear() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10);
		return s;
	}
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

		Ext.QuickTips.init();
		Ext.form.Field.prototype.msgTarget = 'side';
	}
	var id = "";
	var method = "add";
	var entryId = "";
	var specialityName = "";
	var sessWorcode;
	var sessWorname;
	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						if (result.workerCode) {
							// 设定默认工号
							sessWorcode = result.workerCode;
							sessWorname = result.workerName;
							loadData();
						}
					}
				});
	}
	function loadData() {
		Ext.get('fillBy').dom.value = sessWorcode;
		Ext.get('fillName').dom.value = sessWorname;
	};

	var fillName = new Ext.form.TextField({
				id : 'fillName',
				fieldLabel : '填写人',
				name : 'fillName',
				anchor : "85%",
				readOnly : true

			});
	var fillBy = new Ext.form.Hidden({
				id : "fillBy",
				name : 'main.fillBy'
			});
	var fillTime = new Ext.form.TextField({
				id : 'fillTime',
				fieldLabel : '填写时间',
				name : 'main.fillDate',
				readOnly : true,
				value : getDate(),
				anchor : "85%"
			});

	var repairProjectName = new Ext.form.TriggerField({
		id : 'repairProjectName',
		name : 'main.repairProjectName',
		fieldLabel : '项目名称',
		readOnly : true,
		anchor : "92%",
		onTriggerClick : function() {
			var url = "selectRepairAcc.jsp";
			var args = {
				workflowStatus : '7'
			}
			var emp = window
					.showModalDialog(
							url,
							args,
							'dialogWidth:900px;dialogHeight:500px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');

			if (typeof(emp) != "undefined") {
				Ext.getCmp('specialityId').setValue(emp.specialityId);
				Ext.getCmp('repairProjectName').setValue(emp.repairProjectName);
				Ext.getCmp('workingCharge').setValue(emp.workingCharge);
				Ext.getCmp('workingChargeName').setValue(emp.workingChargeName);
				Ext.getCmp('workingMenbers').setValue(emp.workingMenbers);
				Ext.getCmp('startDate').setValue(emp.startDate);
				Ext.getCmp('endDate').setValue(emp.endDate);
				Ext.getCmp('planStartDate').setValue(emp.startDate);
				Ext.getCmp('planEndDate').setValue(emp.endDate);
				Ext.getCmp('acceptanceLevel').setValue(emp.acceptanceLevel);
			}
			getWorkCode()
		}
	})

	var workingMenbers = new Ext.form.TextField({
				id : 'workingMenbers',
				name : 'main.workingMenbers',
				fieldLabel : '工作成员',
				readOnly : true,
				anchor : "85%"
			})
	var workingCharge = new Ext.form.Hidden({
				id : 'workingCharge',
				name : 'main.workingCharge',
				fieldLabel : '工作负责人id',
				readOnly : true,
				anchor : "85%"
			})
	var workingChargeName = new Ext.form.TextField({
				id : 'workingChargeName',
				name : 'workingChargeName',
				fieldLabel : '工作负责人',
				readOnly : true,
				anchor : "85%"
			})
	var startDate = new Ext.form.TextField({
				id : 'startDate',
				fieldLabel : '实际开始时间',
				name : 'main.startTime',
				anchor : "85%",
				readOnly : true,
				listeners : {
					focus : function() {
						WdatePicker({
									isShowClear : true,
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd'
								});
					}
				}

			});
	var endDate = new Ext.form.TextField({
				id : 'endDate',
				fieldLabel : '实际结束时间',
				name : 'main.endTime',
				anchor : "85%",
				readOnly : true,
				listeners : {
					focus : function() {
						WdatePicker({
									isShowClear : true,
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd'
								});
					}
				}
			});
	var planStartDate = new Ext.form.Hidden({
				id : 'planStartDate',
				name : 'main.planStartDate',
				fieldLabel : '计划开始时间'
			})
	var planEndDate = new Ext.form.Hidden({
				id : 'planEndDate',
				name : 'main.planEndDate',
				fieldLabel : '计划结束时间'
			})
	var storeRepairSpecail = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'manageplan/getRepairSpecialityType.action'
						}),
				reader : new Ext.data.JsonReader({
							root : 'list'
						}, [{
									name : 'specialityId',
									mapping : 'specialityId'
								}, {
									name : 'specialityName',
									mapping : 'specialityName'
								}])
			});
	storeRepairSpecail.load()
	var cbxRepairSpecail = new Ext.form.ComboBox({
				id : 'specialityId',
				fieldLabel : "检修专业",
				store : storeRepairSpecail,
				displayField : "specialityName",
				valueField : "specialityId",
				hiddenName : 'main.specialityId',
				mode : 'local',
				triggerAction : 'all',
				value : '',
				readOnly : true,
				anchor : "85%"
			})

	var finish = new Ext.form.TextArea({
				id : 'finish',
				fieldLabel : "完成情况填写",
				name : 'main.completion',
				height : 70,
				anchor : "92%"
			});
	var memo = new Ext.form.TextArea({
				id : 'memo',
				fieldLabel : "备注",
				name : 'main.memo',
				height : 70,
				anchor : "92%"
			});
	var acceptanceLevel = new Ext.form.Hidden({
				id : 'acceptanceLevel',
				name : 'main.acceptanceLevel',
				fieldLabel : '验收级别'
			})
	var acceptId = new Ext.form.Hidden({
				id : 'acceptId',
				name : 'main.acceptId',
				fieldLabel : '验收ID'
			})
	var formtbar = new Ext.Toolbar({
		items : [{
					id : 'btnAdd',
					text : "新增",
					iconCls : 'add',
					handler : function() {
						form.getForm().reset();
						Ext.get('btnSave').dom.disabled = false
						Ext.get('btnDelete').dom.disabled = false;
						Ext.get('btnReport').dom.disabled = false;
						loadData();
						method = "add";
						id = "";
						entryId = "";
						getWorkCode();
					}
				}, '-', {
					id : 'btnSave',
					text : "保存",
					iconCls : 'save',
					handler : function() {
						if (!form.getForm().isValid()) {
							return false;
						}
						if (repairProjectName.getValue() == null
								|| repairProjectName.getValue() == "") {
							Ext.Msg.alert('提示', '请选择项目名称！');
							return;
						}
						if (startDate.getValue() == null
								|| startDate.getValue() == "") {
							Ext.Msg.alert('提示', '请选择实际开始时间！');
							return;
						}
						if (endDate.getValue() == null
								|| endDate.getValue() == "") {
							Ext.Msg.alert('提示', '请选择实际结束时间！');
							return;
						}
						if (cbxRepairSpecail.getValue() == null
								|| cbxRepairSpecail.getValue() == "") {
							Ext.Msg.alert('提示', '请选择填写专业！');
							return;
						}
						if (startDate.getValue() > endDate.getValue()) {
							Ext.Msg.alert('提示', '实际开始时间必须早于实际结束时间！');
							return;
						}

						form.getForm().submit({
							url : 'manageplan/saveRepairAcceptList.action',
							method : 'post',
							params : {
								method : method
							},
							success : function(form, action) {
								if (method == "add") {
									var message = eval('('
											+ action.response.responseText
											+ ')');
									id = message.data.acceptId;
									Ext.get('acceptId').dom.value = message.data.acceptId;
									method = "update";
									Ext.Msg.alert("成功", message.msg);
									
								} else {
									Ext.Msg.alert("成功", "保存成功");
								}
							},
							failure : function() {
								Ext.Msg.alert('提示', '操作失败，请联系管理员！');
							}
						})
					}
				}, '-', {
					id : 'btnDelete',
					text : "删除",
					iconCls : 'delete',
					handler : function() {
						if (id == "") {
							Ext.MessageBox.alert('提示', '请选择需删除的记录!');
							return false;
						}
						Ext.Msg.confirm('提示', '删除的数据您将不能恢复,确定要删除吗?',
								function(b) {
									if (b == "yes") {
										Ext.Ajax.request({
											url : 'manageplan/deleteRepairAcceptList.action',
											params : {
												ids : id
											},
											method : 'post',
											waitMsg : '正在删除数据...',
											success : function(result, request) {
												loadData();
												getWorkCode();
												form.getForm().reset();
												method = "add";
												id = "";
												entryId = "";
												Ext.MessageBox.alert('提示',
														'删除成功!');
											},
											failure : function(result, request) {
												Ext.MessageBox.alert('错误',
														'操作失败,请联系管理员!');
											}
										});
									}

								});
					}
				}, '-', {
					id : 'btnReport',
					text : "上报",
					iconCls : 'upcommit',
					handler : function() {
						if (id == "") {
							Ext.MessageBox.alert('提示', '请从列表中选择一条需上报的记录!');
							return false;
						}
						var url = 'acceptReportSign.jsp'
						var args = new Object();
						args.acceptId = id;
						args.entryId = entryId;
						args.flowCode = "bqRepairAcceptApprove";
						args.specialityName=Ext.get('specialityId').dom.value;
						var o = window
								.showModalDialog(url, args,
										'status:no;dialogWidth=800px;dialogHeight=550px');
						if (o) {
							form.getForm().reset();
							method = "add";
							id = "";
							entryId = "";
							getWorkCode();
						}
					}
				}]
	});
	var form = new Ext.form.FormPanel({
				bodyStyle : "padding:5px 5px 0",
				labelAlign : 'right',
				autoHeight : true,
				region : 'center',
				border : false,
				tbar : formtbar,
				items : [new Ext.form.FieldSet({
							title : '检修项目基本信息',
							collapsible : true,
							height : '100%',
							layout : 'form',
							items : [{
								border : false,
								layout : 'column',
								items : [{
									columnWidth : 1,
									layout : 'form',
									border : false,
									labelWidth : 110,
									items : [acceptId, acceptanceLevel,
											repairProjectName]
								}, {
									columnWidth : .5,
									layout : 'form',
									labelWidth : 110,
									border : false,
									items : [workingMenbers]
								}, {
									columnWidth : .5,
									layout : 'form',
									border : false,
									labelWidth : 110,
									items : [workingCharge, workingChargeName]
								}, {
									columnWidth : .5,
									layout : 'form',
									labelWidth : 110,
									border : false,
									items : [startDate, planStartDate]
								}, {
									columnWidth : .5,
									layout : 'form',
									border : false,
									labelWidth : 110,
									items : [endDate, planEndDate]
								}, {
									columnWidth : .5,
									layout : 'form',
									labelWidth : 110,
									border : false,
									items : [cbxRepairSpecail]
								}, {
									columnWidth : .5,
									layout : 'form',
									labelWidth : 110,
									border : false,
									items : [fillName, fillBy]
								}, {
									border : false,
									layout : 'form',
									columnWidth : .5,
									labelWidth : 110,
									items : [fillTime]
								}, {
									border : false,
									layout : 'form',
									columnWidth : 1,
									labelWidth : 110,
									items : [finish]
								}, {
									border : false,
									layout : 'form',
									columnWidth : 1,
									labelWidth : 110,
									items : [memo]
								}]
							}]
						})]
			});

	var layout = new Ext.Panel({
				autoWidth : true,
				autoHeight : true,
				border : false,
				autoScroll : true,
				split : true,
				items : [form]
			});

	return {
		grid : layout,
		setFormRec : function(v) {
			var rec = v;
			form.getForm().loadRecord(v);
			if (v.get('startTime') != null && v.get('startTime') != "") {
				startDate.setValue(v.get('startTime').substring(0, 10));
			}
			if (v.get('endTime') != null && v.get('endTime') != "") {
				endDate.setValue(v.get('endTime').substring(0, 10));
			}
			finish.setValue(v.get('completion'));
			id = v.get('acceptId');
			entryId = v.get('workflowNo');
			//specialityName = v.get('specialityName');
			getWorkCode();
			method = "update";
		}
	}
}