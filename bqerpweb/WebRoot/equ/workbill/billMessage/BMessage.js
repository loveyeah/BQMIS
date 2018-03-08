// 工单信息
Ext.onReady(function() {
	var register = parent.Ext.getCmp('tabPanel').register;

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
		s += (t > 9 ? "" : "0") + t + ":";
		t = d.getSeconds();
		s += (t > 9 ? "" : "0") + t;
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
	// 编辑
	var editwoCode = getParameter("woCode");
	var editFaWoCode = getParameter("faWoCode");
	// var editWoCodeShow = getParameter("woCodeShow");
	// 生产子工单
	var woCodeadd = getParameter("woCodeadd");
	var woCodeShowAdd = getParameter("woCodeShowAdd");

	// 新建释放wocode
	var refurbish = "";
	// alert(addwoCode)
	// alert(addWoCodeShow)
	// 当前登陆人、部门
	var faWoCode;

	var Status;
	var woCode;
	if (editwoCode != null) {
		woCode = editwoCode;
	}
	var stdwoCode;
	var workChargeCode;

	var workerName;
	var DeptCode;
	var DeptName;
	var workerCode;
	var deptId;
	
	
	var flag = getParameter("flag");
	
	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						if (result.workerCode) {
							// 设定默认工号
							workerCode = result.workerCode;
							workerName = result.workerName;

							Ext.get("requireManCode").dom.value = workerName;
							DeptCode = result.deptCode;
							deptId =  result.deptId;
							DeptName = result.deptName;
							Ext.get("repairDepartment").dom.value = DeptName;
							repairdepart.value = DeptCode;
						}
					}
				});
	}

	// if(addwoCode != null && woCodeShow != null){
	// woCode = addwoCode;
	// Ext.get("woCodeShow").dom.value = woCodeShow ;
	// 
	// }
	// 工单编号
	// var woCode = {
	// id : "woCode",
	// xtype : "textfield",
	// fieldLabel : '工单编号',
	// name : 'equJWo.woCode',
	// hidden : true,
	// value : ''
	// }

	// 工单显示编号
	var woCodeShow = new Ext.form.TextField({
		id : "woCodeShow",
		xtype : "textfield",
		fieldLabel : "工单编号 <font color='red'>*</font>",
		name : 'equJWo.woCodeShow',
		readOnly : true,
		// allowBlank : false,
		anchor : "70%",
		value : '自动生成'
			// width : 300
		})

	var faWoCode = new Ext.form.ComboBox({
				id : "faWoCode",
				// hiddenName : "equJWo.faWoCode",
				fieldLabel : "父工单编号",
				emptyText : '',
				// displayField : "faWoCode",
				// valueField : "faWoCode",
				readOnly : true,
				anchor : "80%",
				onTriggerClick : function(e) {
					var url = "faBillSelect.jsp?woCode=" + woCode;
					// var url ="faBillSelect.jsp";
					var fo = window.showModalDialog(url, null,
							'dialogWidth=800px;dialogHeight=700px;status=no');
					if (typeof(fo) != "undefined") {
						Ext.get("faWoCode").dom.value = fo.woCodeShow
						// faWoCode.setValue(fo.woCodeShow);
						faWoCode = fo.woCode;
					}
				}
			});

	// 工单内容
	var content = new Ext.form.TextArea({
				id : "workorderContent",
				xtype : "textarea",
				allowBlank : false,
				fieldLabel : '工单内容<font color="red">*</font>',
				name : 'equJWo.workorderContent',
				readOnly : false,
				anchor : "90%"
			})
	// 工单类型
	var billType = new Ext.form.ComboBox({
				id : "workorderType",
				name : "workorderType",
				fieldLabel : "工单类型",
				emptyText : '请选择...',
				store : [['Q', '消缺'],['D', '大修'],['Z', '中修'], ['X', '小修'], 
				['L', '临修'], ['J', '计划检修'], ['G', '项目']],
				displayField : "workorderType",
				valueField : "workorderType",
				hiddenName : "equJWo.workorderType",
				// allowBlank : false,
				 readOnly : true,
				triggerAction : 'all',
				anchor : "90%"
			});
	// 状态
	var workorderStatus = new Ext.form.TextField({
				id : "workorderStatus",
				xtype : "textfield",
				fieldLabel : '状态',
				// name : 'equJWo.workorderStatus',
				readOnly : true,
				value : '',
				anchor : "100%"
			})

	// 专业
	var url = "timework/useprolist.action";
	var conn = Ext.lib.Ajax.getConnectionObject().conn;
	conn.open("POST", url, false);
	conn.send(null);
	var search_data4 = eval('(' + conn.responseText + ')');

	var profession = new Ext.form.ComboBox({
				id : "professionCode",
				name : "professionCode",
				hiddenName : "equJWo.professionCode",
				fieldLabel : "专业<font color='red'>*</font>",
				triggerAction : 'all',
				store : new Ext.data.SimpleStore({
							fields : ['name', 'id'],
							data : search_data4
						}),
				displayField : "name",
				valueField : "id",
				emptyText : '请选择...',
				mode : 'local',
				readOnly : true,
				allowBlank : false,
				anchor : "90%"
			});

	// 维修模式
	var url1 = "equstandard/getEquCStandardRepairmodeListToUse.action";
	var conn = Ext.lib.Ajax.getConnectionObject().conn;
	conn.open("POST", url1, false);
	conn.send(null);
	var search_data5 = eval('(' + conn.responseText + ')');

	var repairModel = new Ext.form.ComboBox({
				id : "repairModel",
				// name : "professionCode",
				hiddenName : "equJWo.repairModel",
				fieldLabel : "维修模式",
				triggerAction : 'all',
				store : new Ext.data.SimpleStore({
							fields : ['repairmodeName', 'repairmodeCode'],
							data : search_data5
						}),
				displayField : "repairmodeName",
				valueField : "repairmodeCode",
				emptyText : '请选择...',
				mode : 'local',
				readOnly : true,
				anchor : "90%"
			});

	// 项目编号
	var projectNum = new Ext.form.ComboBox({
		id : "projectNum",
		 hiddenName : "equJWo.projectNum",
		fieldLabel : "项目编号",
		emptyText : '',
		displayField : "projectNum",
		valueField : "projectNum",
		readOnly : true,
		anchor : "90%",
		onTriggerClick : function(e) {
			var args = {
				selectModel : 'single',
				rootNode : {
					id : "0",
					text : '合同类别'
				}
			}
			var url = "ProjectByCode.jsp";
			var rvo = window
					.showModalDialog(
							url,
							args,
							'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
			if (typeof(rvo) != "undefined") {
				Ext.get("projectNum").dom.value = rvo.prjNoShow;
				Ext.get("projectName").dom.value = rvo.prjName;
				Ext.get("hdnprojectNum").dom.value = rvo.prjNo;
			}
		}
	});
	var hdnprojectNum = new Ext.form.Hidden({
				id : 'hdnprojectNum',
				name : 'equJWo.projectNum'
			});
	// 项目名称
	var projectName = new Ext.form.TextField({
				id : "projectName",
				xtype : "textfield",
				fieldLabel : '项目名称',
				name : '',
				readOnly : true,
				anchor : "90%"
			})
	// 缺陷编号
	var failureCode = new Ext.form.ComboBox({
		fieldLabel : '缺陷单号',
		hiddenname : 'failureCode',
		id : 'failureCode',
		mode : 'remote',
		// hiddenName : 'workticketBaseInfo.failureCode',
		triggerAction : 'all',
		readOnly : true,
		onTriggerClick : function() {
			if (!failureCode.disabled) {
				// 页面地址不一样 midified by liuyi 091117
//				var url = "../../failure/query/failureSelect.jsp";
				var url = "../../bqfailure/query/failureSelect.jsp";
				var no = window
						.showModalDialog(
								url,
								null,
								'dialogWidth:700px;dialogHeight:500px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
				if (no != null) {
					failureCode.setValue(no.ro);  //modify by wpzhu
					Ext.get('failureContent').dom.value = no.failcontent;
					billType.setValue('Q');
					//Ext.get('workorderType').dom.readOnly = "true";
					//billType.setDisabled(true);
				}

			}
		},
		anchor : "90%"
			// disabled : true
	});

	var kksCode1;
	// KKS编码
	var kksCode = new Ext.form.ComboBox({
		fieldLabel : 'KKS编码<font color="red">*</font>',
		 hiddenname : 'kksCode',
		id : 'kksCode',
		store : new Ext.data.SimpleStore({
					fields : ['id', 'name'],
					data : [[]]
				}),
		mode : 'remote',
		hiddenName : "equJWo.kksCode",
		// editable : false,
		triggerAction : 'all',
		allowBlank : false,
		readOnly : true,
		onTriggerClick : function() {
			var url = "../../../comm/jsp/equselect/selectAttribute.jsp";
			var no = window
					.showModalDialog(
							url,
							null,
							'dialogWidth:700px;dialogHeight:500px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
			if (no != null) {
				kksCode1 = no.code;
				// alert(no.code)
				kksCode.setValue(no.code);
				Ext.getCmp('equName').setValue(no.name)
			}

		},
		displayField : "kksCode",
		valueField : "kksCode",
		readOnly : true,
		anchor : "90%"
	});
	// 设备名称
	var equName = new Ext.form.TextField({
				id : "equName",
				xtype : "textfield",
				fieldLabel : '设备名称',
				name : '',
				readOnly : true,
				anchor : "90%"
			})
	// 位置编码
	var equPostionCode = new Ext.form.ComboBox({
				id : "equPostionCode",
				hiddenName : "equJWo.equPostionCode",
				fieldLabel : "位置编码",
				emptyText : '',
				readOnly : true,
				anchor : "90%",
				onTriggerClick : function() {
					var url = "../../base/business/kksselect/selectLocation.jsp";
					var po = window.showModalDialog(url, '',
							'dialogWidth=400px;dialogHeight=400px;status=no');
					if (typeof(po) != "undefined") {
						equPostionCode.setValue(po.code);
						Ext.getCmp('postion').setValue(po.name)
					}

				}
			});
	// 位置描述
	var postionDescription = new Ext.form.TextField({
				id : "postion",
				xtype : "textfield",
				fieldLabel : '位置描述',
				name : '',
				readOnly : true,
				anchor : "90%"
			})
	// 缺陷内容
	var failureContent = new Ext.form.TextField({
				id : "failureContent",
				xtype : "textfield",
				fieldLabel : '缺陷内容',
				name : '',
				readOnly : true,
				anchor : "90%"
			})
	// 备注
	var remark = new Ext.form.TextArea({
				id : "remark",
				xtype : "textarea",
				fieldLabel : '备注',
				name : 'equJWo.remark',
				readOnly : false,
				anchor : "90%"
			})
	// 工单基本信息
	var SaveCBill = new Ext.form.FieldSet({
				bodyStyle : "padding:10px 0px 0px 0px",
				title : '',
				height : '100%',
				collapsible : true,
				layout : 'form',
				items : [{
							layout : 'column',
							border : false,
							items : [
									// {
									// hidden : true,
									// items : [woCode]
									// },
									{
								columnWidth : 0.5,
								border : false,
								labelWidth : 80,
								layout : 'form',
								items : [woCodeShow]
							}, {
								columnWidth : 0.499,
								border : false,
								layout : 'form',
								items : [faWoCode]
							}]
						}, {
							layout : 'column',
							border : false,
							items : [{
										columnWidth : 0.996,
										border : false,
										labelWidth : 80,
										layout : 'form',
										items : [content]
									}]
						}, {
							layout : 'column',
							border : false,
							items : [{
										columnWidth : 0.3,
										border : false,
										labelWidth : 80,
										layout : "form",
										items : [billType]
									}, {

										columnWidth : 0.21,
										border : false,
										labelWidth : 60,
										layout : "form",
										items : [workorderStatus]
									}, {
										columnWidth : 0.203,
										border : false,
										labelWidth : 50,
										layout : "form",
										items : [profession]
									}, {
										columnWidth : 0.21,
										border : false,
										labelWidth : 55,
										layout : "form",
										items : [repairModel]
									}]
						}, {
							layout : 'column',
							border : false,
							items : [{
										columnWidth : 0.3,
										border : false,
										labelWidth : 80,
										layout : "form",
										items : [projectNum, hdnprojectNum]
									}, {
										columnWidth : 0.67,
										border : false,
										layout : "form",
										items : [projectName]
									}]

						}, {
							layout : 'column',
							border : false,
							items : [{
										columnWidth : 0.3,
										border : false,
										labelWidth : 80,
										layout : "form",
										items : [kksCode]
									}, {
										columnWidth : 0.67,
										border : false,
										layout : "form",
										items : [equName]
									}]

						}, {
							layout : 'column',
							border : false,
							items : [{
										columnWidth : 0.3,
										border : false,
										labelWidth : 80,
										layout : "form",
										items : [equPostionCode]
									}, {
										columnWidth : 0.67,
										border : false,
										layout : "form",
										items : [postionDescription]
									}]
						}, {
							layout : 'column',
							border : false,
							items : [{
										columnWidth : 0.3,
										border : false,
										labelWidth : 80,
										layout : "form",
										items : [failureCode]
									}, {
										columnWidth : 0.67,
										border : false,
										layout : "form",
										items : [failureContent]
									}]

						}, {
							layout : 'column',
							border : false,
							items : [{
										columnWidth : 1,
										border : false,
										labelWidth : 80,
										layout : 'form',
										items : [remark]
									}]
						}]
			});

	var applyfromDate = new Ext.form.TextField({
				id : 'applyfromDate',
				fieldLabel : "申请工期",
				name : 'equJWo.requireStarttime',
				style : 'cursor:pointer',
				anchor : "100%",
				readOnly : false,
				value : getDate(),
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d ',
									dateFmt : 'yyyy-MM-dd',
									alwaysUseStartDate : true
								});
					}
				}
			})

	var applytoDate = new Ext.form.TextField({
		id : 'applytoDate',
		fieldLabel : "至",
		name : 'equJWo.requireEndtime',
		style : 'cursor:pointer',
		anchor : "100%",
		readOnly : false,
		value : getDate(),
		listeners : {
			focus : function() {
				WdatePicker({
							startDate : '%y-%M-%d',
							dateFmt : 'yyyy-MM-dd',
							alwaysUseStartDate : true,
							onpicked : function() {
								if (applyfromDate.getValue() == ""
										|| applyfromDate.getValue() > applytoDate
												.getValue()) {
									Ext.Msg.alert("提示", "必须大于或等于开始时间");
									applytoDate.setValue("")
									return;
								}
							}
						});
			}
		}
	})
	// name : 'equJWo.factStarttime',实际工期
	var factfromDate = new Ext.form.TextField({
				id : 'factfromDate',
				fieldLabel : "实际工期",
				name : 'equJWo.factStarttime',
				style : 'cursor:pointer',
				anchor : "100%",
				readOnly : false,//modify by wpzhu
				value : '',
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd',
									alwaysUseStartDate : true
									
								});
					}
				}
			})
	// name : 'equJWo.factEndtime',至
	var facttoDate = new Ext.form.TextField({
				id : 'facttoDate',
				fieldLabel : "至",
				name : 'equJWo.factEndtime',
				style : 'cursor:pointer',
				anchor : "100%",
				readOnly : false,//modify by wpzhu
				value : '',
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd',
									alwaysUseStartDate : true,
									onpicked : function() {//modify by wpzhu
								if (factfromDate.getValue() == ""
										|| factfromDate.getValue() > facttoDate
												.getValue()) {
									Ext.Msg.alert("提示", "必须大于或等于实际工期开始时间");
									facttoDate.setValue("")
									return;
								}
							}
								});
					}
				}
			})

	var SaveTime = new Ext.form.FieldSet({
				title : '工期',
				height : '100%',
				// collapsible : true,
				layout : 'form',
				// border : false,
				items : [{
							layout : 'column',
							border : false,
							items : [{
										columnWidth : 0.5,
										border : false,
										labelWidth : 60,
										layout : 'form',
										items : [applyfromDate]
									}, {
										columnWidth : 0.5,
										border : false,
										labelWidth : 50,
										layout : 'form',
										items : [applytoDate]
									}]
						}, {
							layout : 'column',
							border : false,
							items : [{
										columnWidth : 0.5,
										border : false,
										labelWidth : 60,
										layout : 'form',
										items : [factfromDate]
									}, {
										columnWidth : 0.5,
										border : false,
										labelWidth : 50,
										layout : 'form',
										items : [facttoDate]
									}]
						}]
			});

	// 工期
	// var SaveTime = new Ext.Panel({});
	// 检修部门
	var repairdepart = new Ext.form.TextField({
		id : "repairDepartment",
		xtype : "textfield",
		fieldLabel : '检修部门',
//		 name : 'equJWo.repairDepartment',
		readOnly : true,
		value : '',
		anchor : "100%"
			// width : 300
		})
		
		var repairDepHidden = new Ext.form.TextField({
			name: 'equJWo.repairDepartment'
		})
		
		
	// 工作负责人
	var workman = new Ext.form.ComboBox({
		id : "cbworkChargeCode",
		name : "cbworkChargeCode",
		hiddenName : "equJWo.workChargeCode",
		fieldLabel : "工作负责人<font color='red'>*</font>",
		emptyText : '',
		displayField : "workChargeCode",
		valueField : "workChargeCode",
		allowBlank : false,
		readOnly : true,
		anchor : "100%",
		onTriggerClick : function(e) {
			var args = {
				selectModel : 'single',
				rootNode : {
					id : deptId,
					text : DeptName
				}
			}
			var rvo = window
					.showModalDialog(
							'workerByDept.jsp',
							args,
							'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
			if (typeof(rvo) != "undefined") {
				workChargeCode = rvo.workerCode;
				document.getElementById("cbworkChargeCode").value = rvo.workerName;
			}
		}
	});

	var repairDeptStore = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : '',
							method : 'post'
						}),
				reader : new Ext.data.JsonReader({
							root : 'list'
						}, [{
									name : 'workCode',
									mapping : 'workCode'
								}, {
									name : 'workName',
									mapping : 'workName'
								}, {

								}, {

								}])
			});

	// 申请人
	var requireman = new Ext.form.TextField({
		id : "requireManCode",
		xtype : "textfield",
		fieldLabel : '申请人',
		name : 'equJWo.requireManCode',
		value : '',
		readOnly : true,
		anchor : "100%"
			// width : 300
		})

	// 申请时间
	var requirdate = new Ext.form.TextField({
		id : "requirdate",
		xtype : "textfield",
		fieldLabel : '申请时间',
		 name : 'equJWo.requireTime',
		readOnly : true,
		anchor : "100%",
		value : getDate()
			// width : 300
		});

	// 相关角色
	var SaveRole = new Ext.form.FieldSet({
				title : '相关角色',
				height : '100%',
				// collapsible : true,
				// border : false,
				layout : 'form',
				items : [{
							layout : 'column',
							border : false,
							items : [{
										columnWidth : 0.5,
										border : false,
										labelWidth : 60,
										layout : 'form',
										items : [repairdepart]
									},{
										columnWidth : 0.5,
										border : false,
										hidden:true,
										labelWidth : 60,
										layout : 'form',
										items : [repairDepHidden]
									}, {
										columnWidth : 0.5,
										border : false,
										labelWidth : 80,
										layout : 'form',
										items : [workman]
									}]
						}, {
							layout : 'column',
							border : false,
							items : [{
										columnWidth : 0.5,
										border : false,
										labelWidth : 60,
										layout : 'form',
										items : [requireman]
									}, {
										columnWidth : 0.5,
										border : false,
										labelWidth : 80,
										layout : 'form',
										items : [requirdate]
									}]
						}]
			});

	var myPanel = new Ext.FormPanel({
		bodyStyle : "padding:30px 10px 10px 10px",
		region : "center",
		layout : 'form',
		autoheight : true,
		autoScroll : true,
		containerScroll : true,
		tbar : [{
					id : 'btnFirst',
					text : '新建工单',
					iconCls : 'add',
					handler : firstSave
				}, '-', {
					id : 'btnAdd',
					text : '保存工单',
					// iconCls : 'add',
					handler : saveBill
				}, '-', {
					id : 'btnComplete',
					text : '工作完成',
					handler : workComplete
				}, '-', {
					id : 'btnstdpage',
					text : '标准工作包引用',
					handler : function() {
						var ro = window
								.showModalDialog(
										"stdPkageBykks.jsp?kksCode=" + kksCode1,
										'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;')
						if (ro != null) {
							kksCode.setValue(ro.kksCode);
							Ext.get("equName").dom.value = ro.equName;
							stdwoCode = ro.stdwoCode;
							profession.setValue(ro.professionCode);
							repairModel.setValue(ro.repairModel);

							// alert(stdwoCode)
						}
						register.getstdPage('', stdwoCode, '');

						// parent.Ext.getCmp("tabPanel").setActiveTab(2);
						//
						// var _url13 =
						// "equ/workbill/plannedCost/plannedCost.jsp";
						// parent.document.all.iframe3.src = _url13
						// + "?faWoCode=" + stdwoCode;
						//
						// parent.Ext.getCmp("tabPanel").setActiveTab(1);

					}
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
								items : [SaveCBill]
							}, {
								border : false,
								labelWidth : 70,
								columnWidth : 0.5,
								align : 'center',
								layout : 'form',
								anchor : '100%',
								items : [SaveTime]
							}, {
								border : false,
								columnWidth : 0.5,
								align : 'center',
								layout : 'form',
								anchor : '100%',
								items : [SaveRole]
							}]
				}]
	});
	
	function firstSave() {

		register.edit('', '', '');

		var _url2 = "equ/workbill/billMessage/BMessage.jsp";
		parent.Ext.getCmp("tabPanel").setActiveTab(1);
		parent.document.all.iframe2.src = _url2;
		// // myPanel.getForm().reset();
		// Ext.get("woCodeShow").dom.value = "";
		// Ext.get("faWoCode").dom.value = "";
		// Ext.get("workorderContent").dom.value = "";
		// Ext.get("workorderType").dom.value = "";
		// Ext.get("workorderStatus").dom.value = "";
		// Ext.get("professionCode").dom.value = "";
		// Ext.get("repairModel").dom.value = "";
		// Ext.get("projectNum").dom.value = "";
		// Ext.get("projectName").dom.value = "";
		// Ext.get("failureCode").dom.value = "";
		// Ext.get("kksCode").dom.value = "";
		// Ext.get("equName").dom.value = "";
		// Ext.get("equPostionCode").dom.value = "";
		// Ext.get("postion").dom.value = "";
		// Ext.get("remark").dom.value = "";
		// // Ext.destroy(editwoCode.valueOf());
		// // editwoCode = "1";
		// // alert(editwoCode)
		// // applyfromDate.setValue(gateDate());
		// // applytoDate.setValue(gateDate());
		// factfromDate.setValue('');
		// facttoDate.setValue('');
		// // Ext.get("factfromDate").dom.value = " ";
		// // Ext.get("facttoDate").dom.value = " ";
		// // Ext.get("repairDepartment").dom.value = " ";
		// Ext.get("workChargeCode").dom.value = "";
		// // Ext.get("requireManCode").dom.value = " ";
		// Ext.get("requirdate").dom.value = getDate();
		// Ext.get("btnAdd").dom.disabled = false;
		// Ext.get("applyfromDate").dom.disabled = false;
		// Ext.get("applytoDate").dom.disabled = false;
		// Ext.get("factfromDate").dom.disabled = true;
		// Ext.get("facttoDate").dom.disabled = true;
		// var refurbish = "refurbish";
	}

	function saveBill() {
		
		if (Ext.get('workorderType').dom.value == "消缺工单"
				&& Ext.get('failureCode').dom.value == "") {
			Ext.Msg.alert("提示", "消缺工单时缺陷单号不能为空");
			return;
		}
		if (Ext.get("woCodeShow").dom.value == " ") {
			Ext.Msg.alert("提示", "工单号不能为空");
			return;
		}
		var url;
		if (Status == 0 || workorderStatus.value == "工作开始"
				|| Ext.get("workorderStatus").dom.value == "开始工作") {
			url = 'workbill/updateWorkBill.action';
		} else {
			url = 'workbill/addWorkBill.action';
		}
		if (myPanel.getForm().isValid()) {
			Ext.get("requireManCode").dom.value = workerCode;
			//alert(Ext.get("kkscode").getValue());
			myPanel.getForm().submit({
						waitMsg : '保存中,请稍后...',
						url : url,
						method : 'post',
						params : {
							deptCode : DeptCode,
							workChargeCode : workChargeCode,
							kkscode:Ext.get("kkscode").getValue(),
//							faWoCode : faWoCode,
							faWoCode : Ext.get("faWoCode").dom.value,
							woCode : woCode,
							stdWoCode : stdwoCode
						},
						success : function(form, action) {

							var msg = eval('(' + action.response.responseText
									+ ')');
							Ext.Msg.alert('提示', msg.msg);
							woCode = msg.woCode;

							Status = msg.Status;

							// 设置

							Ext.get("workorderStatus").dom.value = "工作开始"
//							Ext.get("applyfromDate").dom.disabled = true;
//							Ext.get("applytoDate").dom.disabled = true;
							Ext.get("factfromDate").dom.disabled = false;
							Ext.get("facttoDate").dom.disabled = false;
							Ext.get("btnComplete").dom.disabled = false;
							billType.setDisabled(false);
							// 保存后调用标准包不可用
							Ext.get("btnstdpage").dom.disabled = true;

							// var _url3 =
							// "equ/workbill/plannedCost/plannedCost.jsp";
							// var _url4 =
							// "equ/workbill/relateworktickets/reworktickets.jsp";
							// var _url5 =
							// "equ/workbill/relatemateriel/RMateriel.jsp";
							// // var _url6 =
							// "equ/workbill/factualCost/factualCost.jsp";
							// var _url7 = "equ/workbill/wolog/wolog.jsp";
							// var _url8 =
							// "equ/workbill/finishedRpt/finishedRpt.jsp";

							// parent.Ext.getCmp("tabPanel").setActiveTab(2);
							// parent.document.all.iframe3.src = _url3 +
							// "?stdWoCode="
							// + stdwoCode + "&woCode=" + woCode
							// + "&workorderStatus=" + Status;
							//							
							// parent.Ext.getCmp("tabPanel").setActiveTab(3);
							// parent.document.all.iframe4.src = _url4 +
							// "?woCode="
							// + woCode;
							// parent.Ext.getCmp("tabPanel").setActiveTab(4);
							// parent.document.all.iframe5.src = _url5 +
							// "?woCode="
							// + woCode;
							// // parent.Ext.getCmp("tabPanel").setActiveTab(5);
							// // parent.document.all.iframe6.src = _url6 +
							// "?woCode="
							// // + woCode + "&workorderStatus=" + Status;
							// parent.Ext.getCmp("tabPanel").setActiveTab(6);
							// parent.document.all.iframe7.src = _url7 +
							// "?woCode="
							// + woCode;
							// // parent.Ext.getCmp("tabPanel").setActiveTab(7);
							// // parent.document.all.iframe8.src = _url8 +
							// "?woCode="
							// // + woCode;
							// parent.Ext.getCmp("tabPanel").setActiveTab(1);
							//modified by liuyi 20100519 
//							register.edit(woCode, Ext.get("faWoCode").dom.value, Status);
							register.edit(woCode, stdwoCode, Status);
						},
						failure : function(form, action) {
							Ext.Msg.alert('错误', "保存失败！");
							/*var o = eval("(" + action.response.responseText
									+ ")");
							Ext.Msg.alert('错误', o.errMsg);*/
						}
					});
		}
	}
	// 工作完成
	function workComplete() {
		// alert(Ext.get("requireManCode").dom.value)
		var url = 'workbill/finWorkbill.action';
		if (factfromDate.getValue() == "" || facttoDate.getValue() == "") {
			Ext.Msg.alert("提示", "实际工期没有填写!");
			return;
		}       
		if (myPanel.getForm().isValid()) {
			Ext.get("requireManCode").dom.value = workerCode;
			myPanel.getForm().submit({
						waitMsg : '保存中,请稍后...',
						url : url,
						method : 'post',
						params : {
							deptCode : DeptCode,
							woCode : woCode,
							workChargeCode : workChargeCode,
//							faWoCode : faWoCode
							faWoCode : Ext.get("faWoCode").dom.value
						},
						success : function(form, action) {
							var msg = eval('(' + action.response.responseText
									+ ')');
							Ext.Msg.alert('提示', msg.msg);
							woCode = msg.woCode;
							Status = msg.Status;

							Ext.get("workorderStatus").dom.value = "工作完成"
							Ext.get("btnAdd").dom.disabled = true
							Ext.get("btnComplete").dom.disabled = true
							Ext.get("btnstdpage").dom.disabled = true

							//modified by liuyi 20100519 
//							register.edit(woCode, Ext.get("faWoCode").dom.value, Status);
							register.edit(woCode, stdwoCode, Status);
						},
						failure : function(form, action) {
							var msg1 = eval('(' + action.response.responseText
									+ ')');
							Ext.Msg.alert('提示', msg1.msg);
						}
					});
		}

	}
	// 设定布局器及面板
	new Ext.Viewport({
				enableTabScroll : true,
				layout : "fit",
				items : [myPanel]
			});
	getWorkCode();
	// 按钮状态设置
	if (workorderStatus.value == null || workorderStatus.value == '') {
		Ext.get("btnComplete").dom.disabled = true;
		Ext.get("factfromDate").dom.disabled = true;
		Ext.get("facttoDate").dom.disabled = true;
	}
	if (workorderStatus.value == "0" || workorderStatus.value == "工作开始") {
		Ext.get("btnAdd").dom.disabled = true
		Ext.get("btnComplete").dom.disabled = false
	}
	if (workorderStatus.value == "工作完成") {
		Ext.get("btnAdd").dom.disabled = true

	}
	// 生成子工单
	if (woCodeadd != null && woCodeShowAdd != null) {
		faWoCode = woCodeadd;
		
		Ext.get("faWoCode").dom.value = woCodeShowAdd;
		
	}
	// 通过工单编号编辑工单
	// alert(refurbish)
	if (editwoCode.length > 3 && woCodeadd == "" && refurbish == "") {
		Ext.lib.Ajax.request('POST',
				'workbill/findByFaWoCode.action?editWoCode=' + editwoCode, {
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						var str = Ext.encode(result.list);
						var ob = eval("(" + str.substring(1, str.length - 1)
								+ ")")
						faWoCode = (editFaWoCode == null) ? "" : editFaWoCode;
						Ext.get("faWoCode").dom.value = (ob.faCodeShow == null)
								? ""
								: ob.faCodeShow;

						Ext.get("factfromDate").dom.disabled = false;
						Ext.get("facttoDate").dom.disabled = false;
						Ext.get("woCodeShow").dom.value = (ob.equJWo.woCodeShow == null)
								? ""
								: ob.equJWo.woCodeShow;
						Ext.get("woCodeShow").dom.readOnly = true;
						woCode = ob.equJWo.woCode;
						Ext.get("workorderContent").dom.value = (ob.equJWo.workorderContent == null)
								? ""
								: ob.equJWo.workorderContent;
						billType.setValue((ob.equJWo.workorderType == null)
								? ""
								: ob.equJWo.workorderType);
						if (ob.equJWo.workorderStatus == "0") {
							Ext.get("workorderStatus").dom.value = "开始工作";
							Ext.get("btnComplete").dom.disabled = false
//							Ext.get("applyfromDate").dom.disabled = true;
//							Ext.get("applytoDate").dom.disabled = true;
						}
						if (ob.equJWo.workorderStatus == "1") {
							Ext.get("workorderStatus").dom.value = "工作完成";
							Ext.get("btnAdd").dom.disabled = true;
							disable();
						}
						profession.setValue((ob.equJWo.professionCode == null)
								? ""
								: ob.equJWo.professionCode);
						repairModel.setValue((ob.equJWo.repairModel == null)
								? ""
								: ob.equJWo.repairModel);
						kksCode.setValue((ob.equJWo.kksCode == null)
								? ""
								: ob.equJWo.kksCode);
						Ext.get("equName").dom.value = (ob.equName == null)
								? ""
								: ob.equName;
						equPostionCode
								.setValue((ob.equJWo.equPostionCode == null)
										? ""
										: ob.equJWo.equPostionCode)
						Ext.get("postion").dom.value = (ob.postionName == null)
								? ""
								: ob.postionName;
						Ext.get("failureCode").dom.value = (ob.failureCode == null)
								? ""
								: ob.failureCode;

						Ext.get("projectNum").dom.value = (ob.prjNoShow == null)
								? ""
								: ob.prjNoShow;
						Ext.get("hdnprojectNum").dom.value = (ob.equJWo.projectNum == null)
								? ""
								: ob.equJWo.projectNum;
						Ext.get("projectName").dom.value = (ob.prjName == null)
								? ""
								: ob.prjName;
						Ext.get("remark").dom.value = (ob.equJWo.remark == null)
								? ""
								: ob.equJWo.remark;
						Ext.get("cbworkChargeCode").dom.value = (ob.workChargeName == null)
								? ""
								: ob.workChargeName;
						workChargeCode = ob.equJWo.workChargeCode;
						
						
						Ext.get("repairDepartment").dom.value = (ob.deptName == null)
								? ""
								: ob.deptName;
								
								repairDepHidden.setValue(ob.equJWo.repairDepartment);
						
//						Ext.get("equJWo.workChargeCode").dom.value='123';
//							Ext.getCmp('cbworkChargeCode').setValue("123");
//
//						Ext.form.ComboBox.superclass.setValue.call(Ext
//								.getCmp('cbworkChargeCode'), "测试");
						// alert(ob.equJWo.requireStarttime.substring(0,10)+"
						// "+ob.equJWo.requireStarttime.substring(11,19));
						Ext.get("applyfromDate").dom.value = (ob.requireSFormatDate == null)
								? ""
								: ob.requireSFormatDate.substring(0,10);
						Ext.get("applytoDate").dom.value = (ob.requrieEFormatDate == null)
								? ""
								: ob.requrieEFormatDate.substring(0,10);
						Ext.get("factfromDate").dom.value = (ob.factSFormatDate == null)
								? ""
								: ob.factSFormatDate.substring(0,10);
						Ext.get("facttoDate").dom.value = (ob.factEFormatDate == null)
								? ""
								: ob.factEFormatDate.substring(0,10);
						Ext.get("requirdate").dom.value = (ob.requireFormatDate == null)
								? ""
								: ob.requireFormatDate;
						Ext.get("failureContent").dom.value = (ob.failureContent == null)
								? ""
								: ob.failureContent;

					}
				});

	}
	function disable() {
		woCodeShow.setDisabled(true);
		Ext.getCmp("faWoCode").setDisabled(true);
		content.setDisabled(true);
		billType.setDisabled(true);
		workorderStatus.setDisabled(true);
		profession.setDisabled(true);
		repairModel.setDisabled(true);
		Ext.getCmp("projectNum").setDisabled(true);
		Ext.getCmp("projectNum").setDisabled(true);
		hdnprojectNum.setDisabled(true);
		projectName.setDisabled(true);
		kksCode.setDisabled(true);
		equName.setDisabled(true);
		equPostionCode.setDisabled(true);
		postionDescription.setDisabled(true);
		failureCode.setDisabled(true);
		remark.setDisabled(true);
//		applyfromDate.setDisabled(true);
//		applytoDate.setDisabled(true);
		factfromDate.setDisabled(true);
		facttoDate.setDisabled(true);
		repairdepart.setDisabled(true);
		workman.setDisabled(true);
		requireman.setDisabled(true);
		requirdate.setDisabled(true);
		failureContent.setDisabled(true);
	}
	// 工单保存后调用标准包不可用
	if (editwoCode != null && editwoCode != "" && editwoCode != "undefined")

		Ext.get("btnstdpage").dom.disabled = true;
	// 生成子工单时调用标准工作包可用
	if (woCodeadd != null && woCodeadd != "" && woCodeadd != "undefined")

		Ext.get("btnstdpage").dom.disabled = false;
		
		if(flag=='approve'){
		Ext.getCmp("btnFirst").setVisible(false);
	}

})
