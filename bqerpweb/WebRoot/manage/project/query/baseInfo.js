// 工单信息
Ext.onReady(function() {
	// var register = parent.Ext.getCmp('tabPanel').register;

	var projectNo;
	var bview;
	var entryId;
	var workflowType;
//验收实体号
	var entryIdAprove;
	
	function DateDiff(sDate1, sDate2) {
		// sDate1和sDate2是年-月-日格式
		var arrDate, objDate1, objDate2, intDays;
		arrDate = sDate1.split("-");
		objDate1 = new Date(arrDate[1] + '-' + arrDate[2] + '-' + arrDate[0]);// 转换为月-日-年格式
		arrDate = sDate2.split("-");
		objDate2 = new Date(arrDate[1] + '-' + arrDate[2] + '-' + arrDate[0]);
		intDays = parseInt(Math.abs(objDate1 - objDate2) / 1000 / 60 / 60 / 24); // 把相差的毫秒数转换为天数
		return intDays;
	}
	// alert(DateDiff("2007-1-1", "2008-1-1"));
	// 系统当前时间
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
		s += (t > 9 ? "" : "0") + t
		// + ":";
		// t = d.getSeconds();
		// s += (t > 9 ? "" : "0") + t;
		return s;
	}

	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						if (result.workerCode) {
							// 设定默认工号
							workerCode = result.workerCode;
							workerName = result.workerName;

							Ext.get("entryBy").dom.value = workerName;
							Ext.get("hdnentryBy").dom.value = workerCode;
							// DeptCode = result.deptCode;
							// alert(DeptCode)
							// DeptName = result.deptName;
							// Ext.get("repairDepartment").dom.value = DeptName;
							// repairdepart.value = DeptCode;
						}
					}
				});
	}

	// 项目编号
	var prjNo = new Ext.form.TextField({
				id : "prjNo",
				// xtype : "textfield",
				fieldLabel : '项目编号',
				name : 'prjjInfo.prjNoShow',
				readOnly : true,
				anchor : "70%"
			})

	// 项目类别
	// var prjTypeId = new Ext.form.ComboBox({
	// id : "prjTypeId",
	// hiddenName : "prjjInfo.prjTypeId",
	// fieldLabel : "项目类别",
	// emptyText : '',
	// store : [['1', '项目类别1'], ['2', '项目类别2'], ['3', '项目类别3'], ['4', '项目类别4']],
	// // displayField : "faWoCode",
	// // valueField : "faWoCode",
	// triggerAction : 'all',
	// mode : 'local',
	// readOnly : true,
	// anchor : "70%"
	// });
	var prjTypeName = new Ext.ux.ComboBoxTree({
				fieldLabel : '项目类别',
				// anchor : '100%',
				displayField : 'text',
				valueField : 'id',
				id : 'prjTypeName',
				// hiddenName : 'head.planOriginalId',
				// hiddenName : 'head.txtMrOriginal',
				blankText : '请选择',
				emptyText : '请选择',
				readOnly : true,
				anchor : "70%",
				tree : {
					xtype : 'treepanel',
					// 虚拟节点,不能显示
					rootVisible : false,
					loader : new Ext.tree.TreeLoader({
								dataUrl : 'manageproject/findByPId.action'
							}),
					root : new Ext.tree.AsyncTreeNode({
								id : '0',
								name : '合肥电厂',
								text : '合肥电厂'
							})
					// listeners : {
					// click : setTxtMrCostSpecial
					// }
				},
				selectNodeModel : 'leaf',
				listeners : {
					'select' : function(combo, record, index) {
						// Ext.get("txtMrOriginalH").dom.value = record
						// .get('planOriginalId');
						// Ext.get("txtMrOriginalH").dom.value =
						// record.get('planOriginalId');
						prjTypeName.setValue(record.get('prjTypeName'));
					}
				}
			});
	var prjTypeId = new Ext.form.Hidden({
				id : 'prjTypeId',
				name : 'prjjInfo.prjTypeId'
			});
	// 项目名称
	var prjName = new Ext.form.TextField({
				id : "prjName",
				fieldLabel : '项目名称',
				name : 'prjjInfo.prjName',
				readOnly : true,
				anchor : "70%"
			})
	// 项目年份
	// var prjYear = {
	// id : "prjYear",
	// xtype : "textfield",
	// fieldLabel : '项目年份',
	// name : 'prjjInfo.prjYear',
	// readOnly : true,
	// value : '',
	// anchor : "70%"
	// }

	var prjYear = new Ext.form.TextField({
				style : 'cursor:pointer',
				id : "prjYear",
				name : 'prjjInfo.prjYear',
				readOnly : true,
				anchor : "70%",
				// value : Year,
				// allowBlank : false,
				fieldLabel : '项目年份',
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y',
									alwaysUseStartDate : true,
									dateFmt : 'yyyy',
									onpicked : function() {
										prjYear.clearInvalid();
									},
									onclearing : function() {
										prjYear.markInvalid();
									}
								});
					}
				}
			});
	// 预算费用
	var planAmount = new Ext.form.NumberField({
				id : "planAmount",
				xtype : "numberfield",
				fieldLabel : '预算费用(万元)',
				boxLabel : '预算费用(万元)',
				name : 'prjjInfo.planAmount',
				readOnly : true,
				value : '',
				anchor : "70%"
			})

	// 费用来源
	var itemId = new Ext.form.ComboBox({
		id : "itemId",
		store : [['1', '费用来源1'], ['2', '费用来源2'], ['3', '费用来源3'], ['4', '费用来源4']],
		hiddenName : "prjjInfo.itemId",
		fieldLabel : "费用来源",
		// displayField : "repairmodeName",
		// valueField : "repairmodeCode",
		triggerAction : 'all',
		emptyText : '请选择...',
		mode : 'local',
		readOnly : true,
		anchor : "70%"
	});

	// 项目内容
	var prjContent = new Ext.form.TextArea({
				id : "prjContent",
				// xtype : "textarea",
				// allowBlank : false,
				fieldLabel : '项目内容',
				name : 'prjjInfo.prjContent',
				readOnly : true,
				anchor : "85%"
			})
	// 施工单位
	var constructionUnit = new Ext.form.TextField({

				id : "constructionUnit",
				name : 'constructionUnit',
				readOnly : true,
				anchor : "70%%",
				// allowBlank : false,
				fieldLabel : '施工单位'
			});
	// 施工负责人
	var constructionChargeBy = new Ext.form.TextField({

				id : "constructionChargeBy",
				name : 'prjjInfo.constructionChargeBy',
				readOnly : true,
				anchor : "70%%",
				// allowBlank : false,
				fieldLabel : '施工负责人'
			});

	// 计划工期
	var planStartDate = new Ext.form.TextField({
				style : 'cursor:pointer',
				id : "planStartDate",
				name : 'prjjInfo.planStartDate',
				readOnly : true,
				anchor : "80%",
				// allowBlank : false,
				fieldLabel : '计划工期',
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									alwaysUseStartDate : false,
									dateFmt : 'yyyy-MM-dd',
									onpicked : function() {
										planStartDate.clearInvalid();
									},
									onclearing : function() {
										planStartDate.markInvalid();
									}
								});
					}
				}
			});

	var planEndDate = new Ext.form.TextField({
		style : 'cursor:pointer',
		id : "planEndDate",
		name : 'prjjInfo.planEndDate',
		readOnly : true,
		anchor : "70%",
		// allowBlank : false,
		fieldLabel : '至',
		listeners : {
			focus : function() {
				WdatePicker({
							startDate : '%y-%M-%d',
							alwaysUseStartDate : false,
							dateFmt : 'yyyy-MM-dd',
							onpicked : function() {
								if (planStartDate.getValue() == ""
										|| planStartDate.getValue() > planEndDate
												.getValue()) {
									Ext.Msg.alert("提示", "必须大于计划开始时间");
									planEndDate.setValue("")
									return;
								}
								Ext.get("planTimeLimit").dom.value = DateDiff(
										planStartDate.getValue(), planEndDate
												.getValue());
								planEndDate.clearInvalid();
							},
							onclearing : function() {
								planEndDate.markInvalid();
							}

						});
			}
		}
	})

	// 计划工日
	var planTimeLimit = new Ext.form.TextField({
		id : "planTimeLimit",
		// xtype : "textfield",
		fieldLabel : '计划工日(天)',
		name : 'prjjInfo.planTimeLimit',
		readOnly : true,
		value : '',
		anchor : "64%"
			// ,
			// listeners : {
			// focus : function() {
			// Ext.get("planTimeLimit").dom.value =
			// DateDiff(planStartDate.getValue(),planEndDate.getValue());
			// }

			// }
		})

	// 实际工期
	var factStartDate = new Ext.form.TextField({
				style : 'cursor:pointer',
				id : "factStartDate",
				name : 'prjjInfo.factStartDate',
				readOnly : true,
				anchor : "80%",
				// allowBlank : false,
				fieldLabel : '实际工期',
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									alwaysUseStartDate : false,
									dateFmt : 'yyyy-MM-dd',
									onpicked : function() {
										factStartDate.clearInvalid();
									},
									onclearing : function() {
										factStartDate.markInvalid();
									}
								});
					}
				}
			});

	var factEndDate = new Ext.form.TextField({
		style : 'cursor:pointer',
		id : "factEndDate",
		name : 'prjjInfo.factEndDate',
		readOnly : true,
		anchor : "70%",
		// allowBlank : false,
		fieldLabel : '至',
		listeners : {
			focus : function() {
				WdatePicker({
							startDate : '%y-%M-%d',
							alwaysUseStartDate : false,
							dateFmt : 'yyyy-MM-dd',
							onpicked : function() {
								if (factStartDate.getValue() == ""
										|| factStartDate.getValue() > factEndDate
												.getValue()) {
									Ext.Msg.alert("提示", "必须大于计划开始时间");
									factEndDate.setValue("")
									return;
								}
								Ext.get("factTimeLimit").dom.value = DateDiff(
										factStartDate.getValue(), factEndDate
												.getValue());
								factEndDate.clearInvalid();
							},
							onclearing : function() {
								factEndDate.markInvalid();
							}

						});
			}
		}
	})

	// 实际工日
	var factTimeLimit = new Ext.form.TextField({
				id : "factTimeLimit",
				// xtype : "textfield",
				fieldLabel : '实际工日(天)',
				name : 'prjjInfo.factTimeLimit',
				readOnly : true,
				value : '',
				anchor : "64%"

			})

	// 工程负责人
	var chargeBy = new Ext.form.TriggerField({

				isFormField : true,
				fieldLabel : "工程负责人",
				readOnly : true,
				name : "chargeByName",
				anchor : "70%",
				onTriggerClick : function() {

				}
			});
	// 工程负责人隐藏
	var hdnchargeBy = new Ext.form.Hidden({
				id : "chargeBy",
				isFormField : true,
				name : "prjjInfo.chargeBy"
			});

	// 工程负责部门
	var chargeDep = new Ext.form.TextField({
				// width : wd,
				fieldLabel : "负责部门",
				name : "changeDepName",
				anchor : "70%",
				readOnly : true
			});
	// 隐藏
	var hdnchargeDep = new Ext.form.Hidden({
				id : "chargeDep",
				name : "prjjInfo.chargeDep"
			});
	/**
	 * 负责人选择画面处理
	 */
	function selectPersonWin() {
		var args = {
			selectModel : 'signal',
			notIn : "",
			rootNode : {
				id : '-1',
				text : '合肥电厂'
			}
		}
		var person = window
				.showModalDialog(
						'../../../../comm/jsp/hr/workerByDept/workerByDept.jsp',
						args,
						'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (typeof(person) != "undefined") {
			chargeBy.setValue(person.workerName);
			hdnchargeBy.setValue(person.workerCode);
			chargeDep.setValue(person.deptName)
			hdnchargeDep.setValue(person.deptCode);
		}
	}
	// 附件
	var annex = new Ext.form.FileUploadField({
				id : "annex",
				// xtype : 'fileuploadfield',
				name : "prjjInfo.annex",
				// xtype : "textfield",
				fieldLabel : '附件',
				// inputType : "file",
				// readOnly : true,
				height : 21,
				anchor : "95%",
				buttonCfg : {
					text : '浏览...',
					iconCls : 'upload-icon'
				}
			})

	// 查看
	var viewLabel = new Ext.form.Label({
		html : '<font style="font-size:13px">附件:</font>'
			// "<font style=\"font-size:12px\"> 附件:</font> "
			// width :80

		})
	var btnView = new Ext.Button({
				id : 'btnView',
				text : '查看',
				handler : function() {
					window.open(bview);
				}
			});

	// 备注
	var memo = new Ext.form.TextArea({
				id : "memo",
				// xtype : "textarea",
				// allowBlank : false,
				fieldLabel : '备注',
				name : 'prjjInfo.memo',
				// readOnly : true,
				anchor : "85%"
			})
	// 申请人
	var entryBy = new Ext.form.TextField({
		id : "entryBy",
		// xtype : "textfield",
		fieldLabel : '申请人',
		name : 'enterByName',
		value : '',
		readOnly : true,
		anchor : "70%"
			// width : 300
		})
	var hdnentryBy = new Ext.form.Hidden({
				id : "hdnentryBy",
				name : "prjjInfo.entryBy"
			});
	// 申请时间
	var entryDate = new Ext.form.TextField({
		id : "entryDate",
		// xtype : "textfield",
		fieldLabel : '申请时间',
		name : 'prjjInfo.entryDate',
		// readOnly : true,
		anchor : "70%",
		value : getDate()
			// width : 300
		});

	// 竣工评价
	var cmlAppraisal = new Ext.form.TextArea({
				id : "cmlAppraisal",
				fieldLabel : '竣工评价',
				name : 'prjjInfo.cmlAppraisal',
				// readOnly : false,
				anchor : "85%"
			})

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
										labelWidth : 90,
										layout : 'form',
										items : [prjNo]
									}, {
										columnWidth : 0.5,
										border : false,
										layout : 'form',
										items : [prjTypeName, prjTypeId]
									}]
						}, {
							layout : 'column',
							border : false,
							items : [{
										columnWidth : 0.5,
										border : false,
										labelWidth : 90,
										layout : 'form',
										items : [prjName]
									}, {
										columnWidth : 0.5,
										border : false,
										layout : 'form',
										items : [prjYear]
									}]
						}, {
							layout : 'column',
							border : false,
							items : [{
										columnWidth : 0.5,
										border : false,
										labelWidth : 90,
										layout : "form",
										items : [planAmount]
									}, {
										columnWidth : 0.5,
										border : false,
										layout : "form",
										items : [itemId]
									}]
						}, {
							layout : 'column',
							border : false,
							items : [{
										columnWidth : 0.5,
										border : false,
										labelWidth : 90,
										layout : "form",
										items : [constructionUnit]
									}, {
										columnWidth : 0.5,
										border : false,
										layout : "form",
										items : [constructionChargeBy]
									}]
						}, {
							layout : 'column',
							border : false,
							items : [{
										columnWidth : 1,
										border : false,
										labelWidth : 90,
										layout : "form",
										items : [prjContent]
									}]

						}, {
							layout : 'column',
							border : false,
							items : [{
										columnWidth : 0.3,
										border : false,
										labelWidth : 90,
										layout : "form",
										items : [planStartDate]
									}, {
										columnWidth : 0.3,
										border : false,
										labelWidth : 70,
										layout : "form",
										items : [planEndDate]
									}, {
										columnWidth : 0.4,
										border : false,
										layout : "form",
										items : [planTimeLimit]
									}]

						}, {
							layout : 'column',
							border : false,
							items : [{
										columnWidth : 0.3,
										border : false,
										labelWidth : 90,
										layout : "form",
										items : [factStartDate]
									}, {
										columnWidth : 0.3,
										border : false,
										labelWidth : 70,
										layout : "form",
										items : [factEndDate]
									}, {
										columnWidth : 0.4,
										border : false,
										layout : "form",
										items : [factTimeLimit]
									}]

						}, {
							layout : 'column',
							border : false,
							items : [{
										columnWidth : 0.5,
										border : false,
										labelWidth : 90,
										layout : "form",
										items : [chargeBy, hdnchargeBy]
									}, {
										columnWidth : 0.5,
										border : false,
										layout : "form",
										items : [chargeDep, hdnchargeDep]
									}]
						}, {
							layout : 'column',
							border : false,
							items : [{
										columnWidth : 0.14,
										bodyStyle : "padding:3px 0px 5px 0px",
										border : false,
										// labelHeight : 4160,
										labelWidth : 90,
										layout : "form",
										items : [viewLabel]
									}, {
										columnWidth : 0.86,
										// html : '<a
										// style=\"cursor:hand;color:red\"
										// onClick=\"window.open();\"/>查看附件</a>',
										bodyStyle : "padding:3px 0px 5px 0px",
										labelWidth : 90,
										border : false,
										layout : "form",
										items : [btnView]

									}]
						}, {
							layout : 'column',
							border : false,
							items : [{
										columnWidth : 1,
										border : false,
										labelWidth : 90,
										layout : 'form',
										items : [memo]
									}]
						}, {
							layout : 'column',
							border : false,
							items : [{
										columnWidth : 0.5,
										border : false,
										labelWidth : 90,
										layout : "form",
										items : [entryBy, hdnentryBy]
									}, {
										columnWidth : 0.5,
										border : false,
										layout : "form",
										items : [entryDate]
									}]
						}, {
							layout : 'column',
							border : false,
							items : [{
										bodyStyle : "padding:20px 0px 0px 0px",
										columnWidth : 1,
										border : false,
										labelWidth : 90,
										layout : 'form',
										items : [cmlAppraisal]
									}]
						}]
			});
	// 按钮
	var approveList = new Ext.Button({
		id : 'approveList',
		text : '查看立项审批记录',
		// cls:"list",
		minWidth : 80,
		handler : function() {
			var url = "/power/workflow/manager/approveInfo/approveInfo.jsp?entryId="
					+ entryId;
			window
					.showModalDialog(
							url,
							null,
							"dialogWidth:650px;dialogHeight:400px;;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;");
		}
	});

	var projectCheck = new Ext.Button({
				id : 'projectCheck',
				text : '查看验收审批记录',
				minWidth : 80,
				handler : function() {
			var url = "/power/workflow/manager/approveInfo/approveInfo.jsp?entryId="
					+ entryIdAprove;
			window
					.showModalDialog(
							url,
							null,
							"dialogWidth:650px;dialogHeight:400px;;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;");
				}
			})
			
	var approveCheck = new Ext.Button({
				id : 'approveCheck',
				text : '验收图形显示',
				iconCls : 'view',
				minWidth : 80,
				handler : function() {
			var url;
					if (entryIdAprove == null || entryIdAprove == "")
						url = "/power/workflow/manager/flowshow/flowshow.jsp?flowCode="
								+"hfPCheck";
					else
						url = "/power/workflow/manager/show/show.jsp?entryId="
								+ entryIdAprove;
					window.open(url);
				}
			})
	var approveQuery = new Ext.Button({
				id : 'approveQuery',
				text : '立项图形展示',
				iconCls : 'view',
				minWidth : 80,
				handler : function() {

					var url;
					if (entryId == null || entryId == "")
						url = "/power/workflow/manager/flowshow/flowshow.jsp?flowCode="
								+ workflowType;
					else
						url = "/power/workflow/manager/show/show.jsp?entryId="
								+ entryId;
					window.open(url);

				}
			});
	var myPanel = new Ext.FormPanel({
				bodyStyle : "padding:30px 10px 10px 10px",
				region : "center",
				layout : 'form',
				autoheight : true,
				autoScroll : true,
				fileUpload : true,
				containerScroll : true,
				tbar : [approveList, approveQuery,projectCheck,approveCheck],
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

	// 设定布局器及面板
	new Ext.Viewport({
				enableTabScroll : true,
				layout : 'border',
				height : 2000,
				items : [myPanel]

			});
	// getWorkCode();

	if (parent.myRecord != null) {

		myPanel.getForm().loadRecord(parent.myRecord);

		var obj = new Object();
		obj.text = parent.myRecord.get("prjTypeName");
		obj.id = parent.myRecord.get("prjjInfo.prjTypeId");
		prjTypeName.setValue(obj);
		chargeBy.setValue(parent.myRecord.get("chargeByName"))
		// 定义项目编号
		projectNo = parent.myRecord.get("prjjInfo.prjNo");
		bview = parent.myRecord.get("prjjInfo.annex");
		entryId = parent.myRecord.get("prjjInfo.accWorkFlowNo");
		entryIdAprove = parent.myRecord.get("prjjInfo.proWorkFlowNo");
		workflowType = parent.workflowType;
		
		constructionUnit.setValue(parent.myRecord.get("constructionUnitName"));
		itemId.setValue((parent.myRecord.get("prjjInfo.itemId") == "zzfy")
				? "生产成本"
				: "劳务成本");

	}
	function disable() {

		prjNo.setDisabled(true);
		prjTypeName.setDisabled(true);
		prjName.setDisabled(true);
		prjYear.setDisabled(true);
		planAmount.setDisabled(true);
		itemId.setDisabled(true);
		prjContent.setDisabled(true);
		planStartDate.setDisabled(true);
		planEndDate.setDisabled(true);
		planTimeLimit.setDisabled(true);
		factStartDate.setDisabled(true);
		factEndDate.setDisabled(true);
		factTimeLimit.setDisabled(true);
		chargeBy.setDisabled(true);
		if (bview == null) {
			btnView.setDisabled(true);
		}
		memo.setDisabled(true);
		entryBy.setDisabled(true);
		entryDate.setDisabled(true);
		chargeDep.setDisabled(true);
		cmlAppraisal.setDisabled(true);
		constructionUnit.setDisabled(true);
		constructionChargeBy.setDisabled(true);

	}

	disable();

})
