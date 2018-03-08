// 工单信息
Ext.onReady(function() {
	// var register = parent.Ext.getCmp('tabPanel').register;

	var projectNo;
	var bview;
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
	function getParameter(psName) {
		var url = self.location;
		var result = "";
		var str = self.location.search.substring(0);
		if (str.indexOf(psName) != -1
				&& (str.substr(str.indexOf(psName) - 1, 1) == "?" || str
						.substr(str.indexOf(psName) - 1, 1) == "&")) {
			if (str.substring(str.indexOf(psName), str.length).indexOf("&") != -1) {
				var Test = str.substring(str.indexOf(psName), str.length);
				result = Test.substr(Test.indexOf(psName), Test.indexOf("&")
								- Test.indexOf(psName));
				result = result.substring(result.indexOf("=") + 1,
						result.length);
			} else {
				result = str.substring(str.indexOf(psName), str.length);
				result = result.substring(result.indexOf("=") + 1,
						result.length);
			}
		}
		return result;
	}
	// var editwoCode = getParameter("woCode");
	var type = getParameter("type");
	var workerCode;
	var workerName;
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
	// new Ext.form.TextField({
	// id : "woCodeShow",
	// xtype : "textfield",
	// fieldLabel : "工单编号 <font color='red'>*</font>",
	// name : 'equJWo.woCodeShow',
	// readOnly : true,
	// // allowBlank : false,
	// anchor : "70%",
	// value : '自动生成'
	// // width : 300
	// })
	// 项目编号
	var prjNo = new Ext.form.TextField({
		id : "prjNo",
		xtype : "textfield",
		fieldLabel : "项目编号 <font color='red'>*</font>",
		name : 'prjjInfo.prjNoShow',
		readOnly : true,
		// allowBlank : false,
		anchor : "70%",
		value : '自动生成'
			// width : 300
		})

	// 项目类别
	var prjTypeName = new Ext.ux.ComboBoxTree({
				fieldLabel : "项目类别<font color='red'>*</font>",
				// anchor : '100%',
				displayField : 'text',
				valueField : 'id',
				id : 'prjTypeName',
				allowBlank : false,
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
				selectNodeModel : 'all',
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
				xtype : "textfield",
				fieldLabel : "项目名称<font color='red'>*</font>",
				name : 'prjjInfo.prjName',
				readOnly : false,
				allowBlank : false,
				anchor : "70%"
			})
	// 项目年份
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
				readOnly : false,
				value : '',
				anchor : "70%"
			})

	// 费用来源
	var itemName = new Ext.form.ComboBox({
		id : "itemName",
		fieldLabel : "费用来源<font color='red'>*</font>",
		triggerAction : 'all',
		emptyText : '请选择...',
		mode : 'local',
		readOnly : true,
		allowBlank : false,
		anchor : "70%",
		onTriggerClick : function(e) {
			var args = {
				selectModel : 'single',
				rootNode : {
					id : "",
					text : ""
				}
			}
			var rvo = window
					.showModalDialog(
							'../../../contract/maint/conitemsourceSelect/conitemsourceSelect.jsp',
							args,
							'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
			if (typeof(rvo) != "undefined") {
				hiditemId.setValue(rvo.itemCode);
				itemName.setValue(rvo.itemName);

			}
		}
	});
	var hiditemId = new Ext.form.Hidden({
				id : "hiditemId",
				name : "prjjInfo.itemId"
			})
	// 项目内容
	var prjContent = new Ext.form.TextArea({
				id : "prjContent",
				xtype : "textarea",
				// allowBlank : false,
				fieldLabel : '项目内容',
				name : 'prjjInfo.prjContent',
				readOnly : false,
				anchor : "85%"
			})

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
								if (planEndDate.getValue() != "") {
									if (planStartDate.getValue() == ""
											|| planStartDate.getValue() > planEndDate
													.getValue()) {
										Ext.Msg.alert("提示", "必须小于计划结束时间");
										planStartDate.setValue("");
										return;
									}
									Ext.get("planTimeLimit").dom.value = DateDiff(
											planStartDate.getValue(),
											planEndDate.getValue())
											+ 1;
									planStartDate.clearInvalid();
								}
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
												.getValue())
										+ 1;
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
	var planTimeLimit = new Ext.form.NumberField({
				id : "planTimeLimit",
				// xtype : "textfield",
				fieldLabel : '计划工日(天)',
				name : 'prjjInfo.planTimeLimit',
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
					selectPersonWin();
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
	var annex = {
		id : "annex",
		xtype : 'fileuploadfield',
		name : "annex",
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
	}

	// 查看
	var btnView = new Ext.Button({
				id : 'btnView',
				text : '查看',
				handler : function() {
					window.open(bview);
				}
			});
	btnView.setVisible(false);
	// 备注
	var memo = new Ext.form.TextArea({
				id : "memo",
				xtype : "textarea",
				// allowBlank : false,
				fieldLabel : '备注',
				name : 'prjjInfo.memo',
				readOnly : false,
				anchor : "85%"
			})
	// 申请人
	var entryBy = new Ext.form.TextField({
		id : "entryBy",
		xtype : "textfield",
		fieldLabel : '申请人',
		// name : 'prjjInfo.entryBy',
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
	var entryDate = {
		id : "entryDate",
		xtype : "textfield",
		fieldLabel : '申请时间',
		name : 'prjjInfo.entryDate',
		readOnly : true,
		anchor : "70%",
		value : getDate()
		// width : 300
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
										items : [itemName, hiditemId]
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
										columnWidth : 0.75,
										bodyStyle : "padding:2px 0px 1px 0px",
										border : false,
										// labelHeight : 4160,
										labelWidth : 90,
										layout : "form",
										items : [annex]
									}, {
										columnWidth : 0.25,
										// html : '<a
										// style=\"cursor:hand;color:red\"
										// onClick=\"window.open();\"/>查看附件</a>',
										bodyStyle : "padding:2px 0px 1px 0px",
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
							id : 'btnFirst',
							text : '新增',
							iconCls : 'add',
							handler : firstSave
						}, '-', {
							id : 'btnAdd',
							text : '保存',
							iconCls : 'add',
							handler : AddProject
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
	// 新增
	function firstSave() {
		myPanel.getForm().reset();
		// parent.document.all.iframe2.src="manage/project/bussiness/projectapply/baseInfo.jsp"
		// parent.Ext.getCmp("maintab").setActiveTab(1);

		// Ext.get("entryBy").dom.value = workerName;
		// Ext.get("hdnentryBy").dom.value = workerCode;
		// Ext.get("btnAdd").dom.disabled = false;
		btnView.setVisible(false);
		// // saveProject.remove("btnView");
		// // btnView.setVisible(false);
		// // Ext.get("btnReport").dom.disabled = false;
		// // Ext.get("btnDel").dom.disabled = false;
		parent.myRecord = null;
		projectNo = null;
		Ext.get("entryBy").dom.value = workerName;
		Ext.get("prjNo").dom.readOnly = true;
	}
	// 保存
	function AddProject() {
		var url;
		if (projectNo != null) {
			// alert(projectNo)
			url = "manageproject/updateProjectApply.action";
		} else {
			url = "manageproject/saveProjectApply.action";
			// alert(Ext.get("annex").dom.value)
		}
		Ext.get("prjjInfo.prjTypeId").dom.value = prjTypeName.value;
		if (myPanel.getForm().isValid()) {
			myPanel.getForm().submit({
				waitMsg : '保存中,请稍后...',
				url : url,
				method : 'post',
				params : {
					filePath : Ext.get("annex").dom.value,
					projectNo : projectNo
				},
				success : function(form, action) {
					var msg = eval('(' + action.response.responseText + ')');
					Ext.Msg.alert('提示', msg.msg, function(b) {
								projectNo = msg.PrjNo;
								bview = msg.annex;
								prjNo.setValue(msg.PrjNoShow);
								parent.iframe1.document
										.getElementById("btnQuery").click();
								firstSave();
								parent.Ext.getCmp("maintab").setActiveTab(0);
							});

				},
				failure : function(form, action) {
					Ext.Msg.alert('提示', "保存失败！");
					var o = eval("(" + action.response.responseText + ")");
					Ext.Msg.alert('提示', o.errMsg);
				}
			});
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

	if (parent.myRecord != null) {
		myPanel.getForm().loadRecord(parent.myRecord);
		var obj = new Object();
		obj.text = parent.myRecord.get("prjTypeName");
		obj.id = parent.myRecord.get("prjjInfo.prjTypeId");
		prjTypeName.setValue(obj);
		chargeBy.setValue(parent.myRecord.get("chargeByName"))
		Ext.get("prjNo").dom.readOnly = false;
		bview = parent.myRecord.get("prjjInfo.annex");
		// alert(parent.myRecord.get("prjjInfo.annex").replace('/power/upload_dir/project/',''));
		// 定义项目编号
		projectNo = parent.myRecord.get("prjjInfo.prjNo");
		// prjNo.setValue(parent.myRecord.get("prjjInfo.prjNoShow"));
		if (parent.myRecord.get("prjjInfo.annex") != null) {
			Ext.get("annex").dom.value = (parent.myRecord.get("prjjInfo.annex"))
					.replace('/power/upload_dir/project/', '');
			btnView.setVisible(true);
		}
		if (parent.myRecord.get("prjStatusId") == "0"
				|| parent.myRecord.get("prjStatusId") == "3") {
			Ext.get("btnAdd").dom.disabled = false;
		} else {
			Ext.get("btnAdd").dom.disabled = true;
		}
		if(parent.myRecord.get("prjjInfo.itemId") != null && parent.myRecord.get("prjjInfo.itemId") != '')
		{
			Ext.Ajax.request({
				url : 'manageproject/getItemSourceInfo.action',
				method : 'post',
				params : {
					code : parent.myRecord.get("prjjInfo.itemId")
				},
				success : function(result,request)
				{
					var ob = eval('(' + result.responseText + ')');
					if(ob.itemName != null && ob.itemName != '')
					{
						Ext.get('itemName').dom.value = ob.itemName;
					}
					else
						Ext.get('itemName').dom.value = '';
				}
			})
		}
//		itemName.setValue((parent.myRecord.get("prjjInfo.itemId") == "zzfy")
//				? "生产成本"
//				: "劳务成本");
		hiditemId.setValue(parent.myRecord.get("prjjInfo.itemId"))
	}
	if (type == 1) {
		firstSave()
	}
})
