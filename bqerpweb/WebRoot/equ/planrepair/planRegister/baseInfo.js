// 工单信息
Ext.onReady(function() {
	// 系统当前时间
	function getDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate();
		s += (t > 9 ? "" : "0") + t + " ";
		// t = d.getHours();
		// s += (t > 9 ? "" : "0") + t + ":";
		// t = d.getMinutes();
		// s += (t > 9 ? "" : "0") + t
		// // + ":";
		// t = d.getSeconds();
		// s += (t > 9 ? "" : "0") + t;
		return s;
	}
	var workerCode;
	var workerName;
	var repairId = "";
	var Planstatus = "";
	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						if (result.workerCode) {
							// 设定默认工号
							workerCode = result.workerCode;
							workerName = result.workerName;
							Ext.get("fillBy").dom.value = workerName;
							fillById.setValue(workerCode)
						}
					}
				});
	}
	// 计划检修编号
	var repairCode = new Ext.form.TextField({
		id : "repairCode",
		fieldLabel : "计划检修编号 <font color='red'>*</font>",
		name : 'prMain.repairCode',
		anchor : "70%",
		allowBlank : false
			// width : 300
		})

	// 计划检修名称
	var repairName = new Ext.form.TextField({
				id : "repairName",
				fieldLabel : "计划检修名称<font color='red'>*</font>",
				name : 'prMain.repairName',
				allowBlank : false,
				anchor : "70%"
			})

	var alltypeStore = new Ext.data.Store({
				autoLoad : true,
				proxy : new Ext.data.HttpProxy({
							url : 'equplan/findEquPlanTypeList.action'
						}),
				reader : new Ext.data.JsonReader({
							root : 'list'
						}, [{
									name : 'planTypeId',
									mapping : 'planTypeId'
								}, {
									name : 'planTypeName',
									mapping : 'planTypeName'
								}])
			});
	alltypeStore.load();

	// 计划类型
	var planType = new Ext.form.ComboBox({
				id : "planType",
				store : alltypeStore,
				fieldLabel : "计划类型",
				displayField : "planTypeName",
				valueField : "planTypeId",
				hiddenName : 'prMain.planTypeId',
				mode : 'remote',
				triggerAction : 'all',
				value : '',
				readOnly : true,
				anchor : '70%'
			});

	// 状态
	var status = new Ext.form.TextField({
				id : 'status',
				fieldLabel : '状态',
				// disabled : true,
				readOnly : true,
				anchor : "70%",
				name : 'statusName'
			})
	// 计划工期
	var planStartTime = new Ext.form.TextField({
		style : 'cursor:pointer',
		id : "planStartTime",
		name : 'prMain.planStartTime',
		readOnly : true,
		anchor : "70%",
		// allowBlank : false,
		fieldLabel : '计划工期',
		listeners : {
			focus : function() {
				WdatePicker({
							startDate : '%y-%M-%d',
							alwaysUseStartDate : false,
							dateFmt : 'yyyy-MM-dd HH:mm:ss',
							onpicked : function() {
								if (planEndTime.getValue() != "") {
									if (planStartTime.getValue() == ""
											|| planStartTime.getValue() > planEndTime
													.getValue()) {
										Ext.Msg.alert("提示", "必须小于计划结束时间");
										planStartTime.setValue("");
										return;
									}
								}
							},
							onclearing : function() {
								planStartDate.markInvalid();
							}
						});
			}
		}
	});

	var planEndTime = new Ext.form.TextField({
		style : 'cursor:pointer',
		id : "planEndTime",
		name : 'prMain.planEndTime',
		readOnly : true,
		anchor : "70%",
		fieldLabel : '至',
		listeners : {
			focus : function() {
				WdatePicker({
							startDate : '%y-%M-%d',
							alwaysUseStartDate : false,
							dateFmt : 'yyyy-MM-dd HH:mm:ss',
							onpicked : function() {
								if (planStartTime.getValue() == ""
										|| planStartTime.getValue() > planEndTime
												.getValue()) {
									Ext.Msg.alert("提示", "必须大于计划开始时间");
									planEndTime.setValue("")
									return;
								}
							},
							onclearing : function() {
								planEndDate.markInvalid();
							}
						});
			}
		}
	})

	// 计划费用
	var fare = new Ext.form.NumberField({
				id : "fare",
				xtype : "numberfield",
				fieldLabel : '计划费用(万元)',
				boxLabel : '计划费用(万元)',
				name : 'prMain.fare',
				readOnly : false,
				decimalPrecision : 4,
				value : '',
				anchor : "70%"
			})

	// 费用来源
	var fareSoruce = new Ext.form.TriggerField({
				fieldLabel : "费用来源",
				id : 'fareSoruce',
				displayField : 'text',
				valueField : 'id',
				anchor : "70%",
				onTriggerClick : function() {
					selectSoruce();
				}
			});

	var fareSoruceId = new Ext.form.Hidden({
				id : "fareSoruceId",
				name : "prMain.fareSoruce"
			})

	function selectSoruce() {
		var rec = window.showModalDialog(
				'../../../manage/budget/maint/budgetItem/budgetItemTree.jsp',
				window, 'dialogWidth=300px;dialogHeight=500px;status=no');
		if (typeof(rec) != "undefined") {
			fareSoruce.setValue(rec.itemName);
			fareSoruceId.setValue(rec.itemId);
		}
	}
	// 内容概要
	var content = new Ext.form.TextArea({
				id : "content",
				xtype : "textarea",
				// allowBlank : false,
				fieldLabel : '内容概要',
				name : 'prMain.content',
				readOnly : false,
				anchor : "85%"
			})
	// 备注
	var remark = new Ext.form.TextArea({
				id : "remark",
				xtype : "textarea",
				// allowBlank : false,
				fieldLabel : '备注',
				name : 'prMain.remark',
				readOnly : false,
				anchor : "85%"
			})
	// 登记人
	var fillBy = new Ext.form.TextField({
				id : "fillBy",
				fieldLabel : '登记人',
				value : '',
				name : 'fillByName',
				readOnly : true,
				anchor : "70%"
			})
	var fillById = new Ext.form.Hidden({
				id : "fillById",
				name : "prMain.fillBy"
			});
	// 登记时间
	var fillDate = {
		id : "fillDate",
		xtype : "textfield",
		fieldLabel : '登记时间',
		name : 'prMain.fillDate',
		readOnly : true,
		anchor : "70%",
		value : getDate()
	};

	// 项目信息
	var saveProject = new Ext.form.FieldSet({
				bodyStyle : "padding:10px 0px 0px 0px",
				title : '',
				height : '100%',
				collapsible : true,
				layout : 'form',
				items : [{
							layout : 'column',
							border : false,
							items : [{
										columnWidth : 0.5,
										border : false,
										layout : 'form',
										items : [repairCode]
									}, {
										columnWidth : 0.5,
										border : false,
										layout : 'form',
										items : [repairName]
									}]
						}, {
							layout : 'column',
							border : false,
							items : [{
										columnWidth : 0.5,
										border : false,
										layout : 'form',
										items : [planType]
									}, {
										columnWidth : 0.5,
										border : false,
										layout : 'form',
										items : [status]
									}]
						}, {
							layout : 'column',
							border : false,
							items : [{
										columnWidth : 0.5,
										border : false,
										layout : "form",
										items : [planStartTime]
									}, {
										columnWidth : 0.5,
										border : false,
										layout : "form",
										items : [planEndTime]
									}]
						}, {
							layout : 'column',
							border : false,
							items : [{
										columnWidth : 0.5,
										border : false,
										layout : "form",
										items : [fare]
									}, {
										columnWidth : 0.5,
										border : false,
										layout : "form",
										items : [fareSoruce, fareSoruceId]
									}]

						}, {
							layout : 'column',
							border : false,
							items : [{
										columnWidth : 1,
										border : false,
										layout : "form",
										items : [content]
									}]

						}, {
							layout : 'column',
							border : false,
							items : [{
										columnWidth : 1,
										border : false,
										layout : "form",
										items : [remark]
									}]
						}, {
							layout : 'column',
							border : false,
							items : [{
										columnWidth : 0.5,
										border : false,
										layout : "form",
										items : [fillBy, fillById]
									}, {
										columnWidth : 0.5,
										border : false,
										layout : "form",
										items : [fillDate]
									}]
						}]
			});

	var myPanel = new Ext.FormPanel({
				bodyStyle : "padding:30px 10px 10px 10px",
				region : "center",
				layout : 'form',
				autoheight : true,
				autoScroll : true,
				fileUpload : true,
				containerScroll : true,
				tbar : [{
							id : 'btnAdd',
							text : '保存',
							iconCls : 'add',
							handler : AddPlan
						}, '-', {
							id : 'btnPre',
							text : '准备',
							iconCls : 'add',
							handler : Prepare
						}, '-', {
							id : 'btnCom',
							text : '完成',
							iconCls : 'add',
							handler : complete
						}, '-', {
							id : 'btnRep',
							text : '上报',
							iconCls : 'add',
							handler : Report
						}],
				items : [{
							layout : 'column',
							border : false,
							items : [{
										border : false,
										columnWidth : 1,
										align : 'center',
										anchor : '100%',
										layout : 'form',
										items : [saveProject]
									}]
						}]
			});

	function UpdateStatus() {
		Ext.Ajax.request({
					url : 'equplan/updatePlanStatus.action',
					method : 'post',
					params : {
						repairId : repairId,
						status : Planstatus
					},
					success : function(result, request) {
					}
				})
	}
	// 准备
	function Prepare() {
		if (Planstatus == "0") {
			Ext.Msg.confirm("提示", "进入准备阶段后，数据不允许修改，是否继续？", function(button,
							text) {
						if (button == "yes") {
							Planstatus = 1;
							UpdateStatus();
							status.setValue("准备中");
						}
					})
		} else {
			Ext.Msg.alert('提示', '该状态不可准备!');
		}
	}
	// 完成
	function complete() {
		if (Planstatus == "1") {
			Ext.Msg.confirm("提示", "进入完成阶段后，是否继续？", function(button, text) {
						if (button == "yes") {
							Planstatus = 2;
							UpdateStatus();
							status.setValue("完成");
						}
					})
		} else {
			Ext.Msg.alert('提示', '该状态不可完成!');
		}
	}
	// 上报
	function Report() {
		if (Planstatus == "2") {
			var url = 'reportsign.jsp'
			var args = new Object();
			args.repairId = repairId;
			args.workflowType = "bpPlanRepair";
			var o = window.showModalDialog(url, args,
					'status:no;dialogWidth=800px;dialogHeight=550px');
			if (o) {
				parent.iframe1.document.getElementById("btnQuery").click();
				myPanel.getForm().reset();
				// Ext.MessageBox.alert('提示', '上报成功!');
			}
		} else {
			Ext.Msg.alert('提示', '该状态不可上报!');
		}
	}
	// 保存
	function AddPlan() {
		if (Planstatus == "0" || Planstatus == "5" || Planstatus == "") {
			var url;
			if (repairId != "") {
				url = "equplan/updatePlanRepair.action";
			} else {
				url = "equplan/savePlanRepair.action";
			}
			if (myPanel.getForm().isValid()) {
				myPanel.getForm().submit({
					url : url,
					method : 'post',
					params : {
						repairId : repairId
					},
					success : function(form, action) {
						var msg = eval('(' + action.response.responseText + ')');
						// Ext.Msg.alert('提示', "操作成功!");
						repairId = msg.repairId;
						parent.repair = repairId;
						status.setValue(msg.status);
						parent.iframe1.document.getElementById("btnQuery")
								.click();
					},
					failure : function(form, action) {
						var o = eval("(" + action.response.responseText + ")");
						Ext.Msg.alert('提示', o.msg);
					}
				});
			}
		} else {
			Ext.Msg.alert('提示', '该状态不可保存、修改!');
		}
	}
	// 设定布局器及面板
	new Ext.Viewport({
				enableTabScroll : true,
				layout : 'border',
				height : 2000,
				items : [myPanel]
			});
	getWorkCode();
	alltypeStore.on("load", function() {
				if (parent.myRecord != null && parent.myRecord != "") {
					myPanel.getForm().loadRecord(parent.myRecord);
					repairId = parent.myRecord.get("prMain.repairId");
					Planstatus = parent.myRecord.get("prMain.status");
					fareSoruce.setValue(parent.myRecord.get("fareSoruceName"));
					fareSoruceId.setValue(parent.myRecord
							.get("prMain.fareSoruce"))
				}
			})

})
