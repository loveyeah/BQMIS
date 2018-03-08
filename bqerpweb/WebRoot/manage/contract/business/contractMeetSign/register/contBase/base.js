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
	var method = "add";
	var docmethod;
	var sessWorname;
	var sessWorcode;
	var sessDeptCode;
	var sessDeptName;
	var operateBy;
	var entryBy;
	var operateDepCode;
	var filetype;
	var entryId;
	var deptId = "";
	var prosy_by; // 委托人
	// Ext.Ajax.request({
	// url : 'managecontract/getSessionInfo.action',
	// params : {},
	// method : 'post',
	// waitMsg : '正在加载数据...',
	// success : function(result, request) {
	// var responseArray = Ext.util.JSON.decode(result.responseText);
	// if (responseArray.success == true) {
	// var tt = eval('(' + result.responseText + ')');
	// o = tt.data;
	// sessWorname = o[1];
	// sessWorcode = o[0];
	// sessDeptCode = o[2];
	// sessDeptName = o[3];
	// loadData();
	// } else {
	// Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
	// }
	// },
	// failure : function(result, request) {
	// Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
	// }
	// });
	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						if (result.workerCode) {
							// 设定默认工号
							sessWorcode = result.workerCode;
							sessWorname = result.workerName;
							sessDeptCode = result.deptCode;
							deptId = result.deptId;
							sessDeptName = result.deptName;
							loadData();
						}
						typeBox.setValue(2);
					}
				});
	}
	function loadData() {
		entryBy = sessWorcode;
		operateBy = sessWorcode;
		operateDepCode = sessDeptCode;
		Ext.get('entryName').dom.value = sessWorname;
		Ext.get('operateName').dom.value = sessWorname;
		Ext.get('operateDepName').dom.value = sessDeptName;
		// unitStore.load({
		// params : {
		// deptId : sessDeptCode
		// }
		// })
		annextbar.setDisabled(true);
		Credenttbar.setDisabled(true);
		Materialtbar.setDisabled(true);
	};
	// 币别 modify by drdu 2009/05/05
	// var addRec = new Ext.data.Record.create([
	// {
	// name : 'currencyId'
	// }, {
	// name : 'currencyName'
	// }
	// ])
	var typeStore = new Ext.data.JsonStore({
				url : 'managecontract/getConCurrencyList.action',
				root : 'list',
				fields : ['currencyId', 'currencyName']
			})
	typeStore.load();
	// typeStore.on('load', function() {
	// this.insert(0, new addRec({
	// currencyId : "00",
	// currencyName : "人民币"
	// }));
	//
	// })
	var id = getParameter("id");
	var revoke = getParameter("revoke");
	var contractNo = getParameter("contractNo");
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
	var issum = 'Y';
	var issign = 'Y';
	var isinstant = "N";

	var typeBox = new Ext.form.ComboBox({
				fieldLabel : '币别',
				store : typeStore,
				id : 'currencyType',
				valueField : "currencyId",
				displayField : "currencyName",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'con.currencyType',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : false,
				// emptyText : '请选择',
				anchor : '75.5%'
			});

	// 合同类别 modify by drdu 20090423
	var contypeBox = new Ext.form.ComboBox({
				fieldLabel : '合同类别',
				store : [['CL', '材料'], ['BP', '备品'], ['SB', '设备']],
				id : 'conType',
				name : 'conTypetext',
				valueField : "value",
				displayField : "text",
				// value : '1',
				mode : 'local',
				typeAhead : true,
				allowBlank : false,
				forceSelection : true,
				hiddenName : 'con.conCode',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				emptyText : '请选择',
				anchor : '85%'
			});
	// contypeBox.on('beforequery', function() {
	// return false
	// });

	// add by bjxu 091116
	var maifang = new Ext.form.TextField({
				id : 'maifang',
				name : 'con.operateAdvice',
				fieldLabel : "合同买方",
				anchor : '85%',
				value : "大唐陕西发电有限公司灞桥热电厂"
			})
	// add by bjxu 20091016
	// 经办人联系电话
	var operateTel = new Ext.form.TextField({
				fieldLabel : '联系电话',
				id : 'operateTel',
				name : 'con.operateTel',
				anchor : '85%'
			})
	// 委托人 add by bjxu 091116
	var prosy_by = new Ext.form.ComboBox({
		id : 'prosy_by',
		fieldLabel : '本项目委托代理人',
		readOnly : true,
		allowBlank : false,
		anchor : '85%',
		onTriggerClick : function(e) {
			var url = "apponitNextPerson.jsp";
			var args = {
				flowCode : 'bqCGContract-v1.0',
				actionId : "38"
			}
			var o = window.showModalDialog(url, args,
					'dialogWidth=450px;dialogHeight=370px;status=no');
			if (typeof(o) == "object") {
				prosy_by.setValue(o.nrsname)
				hidprosy_by.setValue(o.nrs)
			}
		}
	})
	var hidprosy_by = new Ext.form.Hidden({
				id : 'hidprosy_by',
				name : 'con.prosy_by'
			})

	// 委托开始日期
	var prosyStartDate = new Ext.form.TextField({
				id : 'prosyStartDate',
				fieldLabel : "委托开始时间",
				name : 'con.prosyStartDate',
				style : 'cursor:pointer',
				anchor : "85%",
				// allowBlank : false,
				// value : getDate(),
				listeners : {
					focus : function() {
						var pkr = WdatePicker({
									startDate : '%y-%M-%d 00:00:00',
									dateFmt : 'yyyy-MM-dd HH:mm:ss',
									alwaysUseStartDate : true
								});
					}
				}
			});

	// 委托开始日期
	var prosyEndDate = new Ext.form.TextField({
				id : 'prosyEndDate',
				fieldLabel : "委托结束时间",
				name : 'con.prosyEndDate',
				style : 'cursor:pointer',
				anchor : "85%",
				// allowBlank : false,
				// value : getDate(),
				listeners : {
					focus : function() {
						var pkr = WdatePicker({
									startDate : '%y-%M-%d 00:00:00',
									dateFmt : 'yyyy-MM-dd HH:mm:ss',
									alwaysUseStartDate : true
								});
					}
				},
				validator : function() {
					var sd = prosyStartDate.getValue();
					var ed = prosyEndDate.getValue();
					if (sd >= ed) {
						return "委托结束时间应大于开始时间！";
					} else {
						return true;
					}
				}
			});

	// 合同年份
	var conyear = new Ext.form.TextField({
				// style : 'cursor:pointer',
				id : "conyear",
				name : 'con.conYear',
				readOnly : true,
				anchor : "85%",
				// value : Year,
				allowBlank : false,
				fieldLabel : '合同年份',
				listeners : {
					focus : function() {
						WdatePicker({
							startDate : ' ',
							alwaysUseStartDate : true,
							dateFmt : 'yyyy'
								// ,
								// onpicked : function() {
								// prjYear.clearInvalid();
								// }
								// ,
								// onclearing : function() {
								// prjYear.markInvalid();
								// }
							});
						this.blur();
					}
				}
			});
	// 总金额
	var actAmount = new Ext.form.NumberField({
				id : 'actAmount',
				name : 'con.actAmount',
				fieldLabel : '总金额',
				readOnly : false,
				// allowBlank : false,
				anchor : '85.5%'
			});
	// 有无总金额
	var isTotal = new Ext.form.Checkbox({
				id : 'isTotal',
				// name : 'con.isSum',
				fieldLabel : '有无总金额',
				checked : true,
				anchor : "20%",
				listeners : {
					check : isTotalCheck
				}
			});
	function isTotalCheck() {
		if (isTotal.checked) {
			actAmount.setDisabled(false);
			issum = "Y";
			isTotal.blur();
		} else {
			actAmount.setDisabled(true);
			Ext.get('actAmount').dom.value = "";
			issum = "N";
			isTotal.blur();
		}
		isTotal.blur();
	}

	// 要求会签
	var isMeet = new Ext.form.Checkbox({
				id : 'isMeet',
				fieldLabel : '要求会签',
				checked : true,
				// hideLabel : true,
				anchor : "20%",
				listeners : {
					check : isMeetCheck
				}
			});
	function isMeetCheck() {
		if (isMeet.checked) {
			issign = "Y";
		} else {
			issign = "N";
		}
	}
	// 要求紧急采购
	var isEmergency = new Ext.form.Checkbox({
				id : 'isEmergency',
				fieldLabel : '要求紧急采购',
				// hideLabel : true,
				anchor : "20%",
				checked : false,
				listeners : {
					check : isEmergencyCheck
				}
			});
	function isEmergencyCheck() {
		if (isEmergency.checked) {
			isinstant = "Y";
		} else {
			isinstant = "N";
		}
	};

	// 部门负责人
	var unitBox = new Ext.form.ComboBox({
		fieldLabel : '部门负责人',
		id : 'operateLeadBy',
		// name : 'con.operateLeadBy',
		// store : unitStore,
		// valueField : "empCode",
		// displayField : "chsName",
		mode : 'local',
		triggerAction : 'all',
		forceSelection : true,
		// hiddenName : 'con.operateLeadBy',
		editable : false,
		// allowBlank : false,
		selectOnFocus : true,
		anchor : "85%",
		onTriggerClick : function(e) {
			var url = "../../../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
			var args = {
				selectModel : 'single',
				notIn : "",
				rootNode : {
					id : deptId,
					text : Ext.get('operateDepName').dom.value
				}
			}
			var o = window.showModalDialog(url, args,
					'dialogWidth=650px;dialogHeight=500px;status=no');
			if (typeof(o) == "object") {
				unitBox.setValue(o.workerName);
				hidunitBox.setValue(o.workerCode);
				// Ext.get('operateName').dom.value = o.workerName;
				// Ext.get('operateDepName').dom.value = o.deptName;
				// unitBox.setValue("");
				// unitStore.load({
				// params : {
				// deptId : o.deptCode
				// }
				// })
				// operateBy = o.workerCode;
				// operateDepCode = o.deptCode;
			}
		}
	});
	var hidunitBox = new Ext.form.Hidden({
				id : "hidunitBox",
				name : "con.operateLeadBy"

			})
	// 供应商
	var cliendBox = new Ext.form.ComboBox({
		name : 'cliendId',
		id : 'cliendId',
		fieldLabel : '供应商',
		mode : 'remote',
		editable : false,
		anchor : '92.5%',
		allowBlank : false,
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../../../../manage/client/business/clientSelect/clientSelect.jsp?approveFlag=2";
			var client = window
					.showModalDialog(
							url,
							null,
							'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');

			if (typeof(client) != "undefined") {
				cliendBox.setValue(client.clientName);
				hidcliendBox.setValue(client.cliendId);
			}
		}
	});
	var hidcliendBox = new Ext.form.Hidden({
				id : "hidcliendBox",
				name : "con.cliendId"
			})
	// 费用来源
	var itemBox = new Ext.form.ComboBox({
		fieldLabel : '费用来源',
		// store : [['1', '电力供应'], ['2', '公司']],
		id : 'itemId',
		// name : 'itemId',
		// valueField : "value",
		// displayField : "text",
		mode : 'local',
		typeAhead : true,
		forceSelection : true,
		// hiddenName : 'con.itemId',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : false,
		emptyText : '请选择',
		anchor : '85%',
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
							'../../../../../contract/maint/conitemsourceSelect/conitemsourceSelect.jsp',
							args,
							'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
			if (typeof(rvo) != "undefined") {
				hiditemId.setValue(rvo.itemCode);
				itemBox.setValue(rvo.itemName);

			}
		}
	});
	var hiditemId = new Ext.form.Hidden({
				id : 'hiditemId',
				name : 'con.itemId'
			})
	// 合同开始时间
	var startDate = new Ext.form.TextField({
				id : 'startDate',
				fieldLabel : "合同履行开始时间",
				name : 'con.startDate',
				style : 'cursor:pointer',
				forceSelection : true,
				selectOnFocus : true,
				anchor : "85%",
				readOnly : true,
				// allowBlank : false,
				// value : getDate(),
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d 00:00:00',
									dateFmt : 'yyyy-MM-dd HH:mm:ss',
									alwaysUseStartDate : true,
									onpicked : function() {
										if (endDate.getValue() != "") {
											if (startDate.getValue() == ""
													|| startDate.getValue() > endDate
															.getValue()) {
												Ext.Msg.alert("提示",
														"必须小于合同结束时间");
												startDate.setValue("");
												return;
											}
											startDate.clearInvalid();
										}
									},
									onclearing : function() {
										startDate.markInvalid();
									}
								});
					}
				}

			});
	// 合同结束时间
	var endDate = new Ext.form.TextField({
				id : 'endDate',
				fieldLabel : "合同履行结束时间",
				name : 'con.endDate',
				style : 'cursor:pointer',
				anchor : "85%",
				// allowBlank : false,
				forceSelection : true,
				selectOnFocus : true,
				readOnly : true,
				// value : getDate(),
				listeners : {
					focus : function() {
						var pkr = WdatePicker({
									startDate : '%y-%M-%d 00:00:00',
									dateFmt : 'yyyy-MM-dd HH:mm:ss',
									alwaysUseStartDate : true,
									onpicked : function() {
										if (startDate.getValue() == ""
												|| startDate.getValue() > endDate
														.getValue()) {
											Ext.Msg.alert("提示", "必须大于合同开始时间");
											endDate.setValue("")
											return;
										}
										endDate.clearInvalid();
									},
									onclearing : function() {
										endDate.markInvalid();
									}
								});
					}
				}
			});
	// 起草人
	var entryName = new Ext.form.TextField({
				id : 'entryName',
				// name : 'con.entryBy',
				xtype : 'textfield',
				fieldLabel : '起草人',
				readOnly : true,
				allowBlank : false,
				anchor : '85.5%'
			});
	// 起草时间
	var entryDate = new Ext.form.TextField({
				id : 'entryDate',
				fieldLabel : "起草日期",
				name : 'con.entryDate',
				style : 'cursor:pointer',
				readOnly : true,
				anchor : "85%",
				value : getDate()
			});
	// 合同文本
	var oriFileName = {
		id : "conFile",
		xtype : "fileuploadfield",
		fieldLabel : '合同文本',
		// inputType : "file",
		name : 'conFile',
		height : 22,
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
				disabled : true,
				handler : function() {
					if (id == "") {
						Ext.Msg.alert('提示', '请选择合同');
						return false;
					} 
//					window.open("/power/managecontract/showConFile.action?conid="
//									+ id + "&type=CON"); 
					var viewUrl = "managecontract/showConFile.action?conid="+ id + "&type=CON"; 
				    var params = {
				    	id:id,       //con_j_con_doc表文档主键
				    	type:'CON',
				    	url:viewUrl, //文档地址
				    	readOnly:false, 
				    	title:'修改合同文本',
				    	showTrack:0 // 1,0,4
				    };
				    var sheight = screen.height-70;
					var swidth = screen.width-10;  
				    var winoption ="dialogHeight:"+sheight+"px;dialogWidth:"+ swidth +"px;status:yes;scroll:no;resizable:yes;center:yes";
                   window.showModalDialog("/power/comm/dsoframer/viewDocument.jsp",params,winoption); 
				}
			});
			
	var formtbar = new Ext.Toolbar({
		items : [{
					id : 'btnAdd',
					text : "增加",
					iconCls : 'add',
					handler : function() {
						form.getForm().reset();
						Ext.get('btnSave').dom.disabled = false
						Ext.get('btnDelete').dom.disabled = false;
						Ext.get('btnReport').dom.disabled = false;
						btnView.setDisabled(true);
						typeBox.setValue(2);
						loadData();
						method = "add";
						id = "";
						entryId = "";
						annex_ds.load();
						Credent_ds.load();
						getWorkCode();
						// var url3 =
						// "manage/contract/business/contractMeetSign/register/payPlan/payPlan.jsp";
						// parent.document.all.iframe3.src = url3;
					}
				}, '-', {
					id : 'btnSave',
					text : "保存",
					iconCls : 'save',
					handler : function() {
						if (isTotal.checked && actAmount.getValue() == "") {
							Ext.Msg.alert('提示', '请填写总金额!');
							actAmount.setDisabled(false);
							return false;
						}
						if (!form.getForm().isValid()) {
							return false;
						}
						form.getForm().submit({
							url : 'managecontract/addMeetConInfo.action',
							method : 'POST',
							params : {
								filePath : Ext.get("conFile").dom.value,
								'con.isSum' : issum,
								'con.isSign' : issign,
								'con.isInstant' : isinstant,
								'con.entryBy' : entryBy,
								'con.operateBy' : operateBy,
								'con.operateDepCode' : operateDepCode,
								'con.conTypeId' : "1",
								// // 'con.conTypeId':
								// Ext.get('conTypeId').dom.value,
								// filePath : Ext.get('filePath').dom.value,
								method : method
							},
							success : function(form, action) {
								parent.iframe1.document
										.getElementById("btnQuery").click();
								if (method == "add") {
									var message = eval('('
											+ action.response.responseText
											+ ')');
									parent.iframe1.document
											.getElementById("btnQuery").click();
									id = message.data.conId;
									Ext.get('conId').dom.value = message.data.conId;
									method = "update";
									var currencyType = message.data.currencyType;
									var actAmount = message.data.actAmount;
									contractNo = message.data.conttreesNo;
									annextbar.setDisabled(false);
									Credenttbar.setDisabled(false);
									Materialtbar.setDisabled(false);
									if (message.path != null
											&& message.path != "null") {
										btnView.setDisabled(false);
									} else {
										btnView.setDisabled(true);
									}
									// var url3 =
									// "manage/contract/business/contractMeetSign/register/payPlan/payPlan.jsp";
									// parent.document.all.iframe3.src = url3 +
									// "?id="
									// + id + "&currencyType=" + currencyType
									// + "&actAmount=" + actAmount;
									Ext.Msg.alert("成功", "保存成功");
								} else {
									annextbar.setDisabled(false);
									Credenttbar.setDisabled(false);
									Materialtbar.setDisabled(false);
									var message = eval('('
											+ action.response.responseText
											+ ')');
									if (message.path != null
											&& message.path != "null") {
										btnView.setDisabled(false);
									} else {
										btnView.setDisabled(true);
									}
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
											url : 'managecontract/deleteMeetConInfo.action',
											params : {
												conid : id
											},
											method : 'post',
											waitMsg : '正在删除数据...',
											success : function(result, request) {
												parent.iframe1.document
														.getElementById("btnQuery")
														.click();
												typeBox.setValue(2);
												loadData();
												form.getForm().reset();
												method = "add";
												id = "";
												contractNo = "";
												annex_ds.load();
												Credent_ds.load();
												Material_ds.load();
												getWorkCode()
												btnView.setDisabled(true);
												// var url3 =
												// "manage/contract/business/contractMeetSign/register/payPlan/payPlan.jsp";
												// parent.document.all.iframe3.src
												// = url3;
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
							Ext.MessageBox.alert('提示', '请选择需上报的记录!');
							return false;
						}
						if (isTotal.checked && actAmount.getValue() == "") {
							Ext.Msg.alert('提示', '请填写总金额!');
							actAmount.setDisabled(false);
							return false;
						}
						var url = 'reportsign.jsp'
						var args = new Object();
						args.entryId = entryId;
						args.conId = id;
						args.workflowType = "bqCGContract";
						args.sum = actAmount.getValue()
						var o = window
								.showModalDialog(url, args,
										'status:no;dialogWidth=800px;dialogHeight=550px');
						if (o) {
							// parent.iframe1.document.getElementById("btnQuery").click();
							form.getForm().reset();
							method = "add";
							id = "";
							entryId = "";
							contractNo = "";
							annex_ds.load();
							Credent_ds.load();
							Material_ds.load();
							getWorkCode()
							// var url3 =
							// "manage/contract/business/contractMeetSign/register/payPlan/payPlan.jsp";
							// parent.Ext.getCmp("maintab").setActiveTab(2);
							// parent.document.all.iframe3.src = url3;
							var _url1 = "manage/contract/business/contractMeetSign/register/meetList/meetList.jsp"
							parent.Ext.getCmp("maintab").setActiveTab(0);
							parent.document.all.iframe1.src = _url1;
							// Ext.MessageBox.alert('提示', '上报成功!');
						}
					}
				},
				// '-', {
				// id : 'btnMeet',
				// text : "会签表",
				// iconCls : 'pdfview',
				// handler : function() {
				// CheckRptPreview();
				// }
				// },
				'-', {
					id : 'btnQuery',
					text : "会签查询",
					iconCls : 'view',
					handler : function() {
						if (id == null || id == "") {
							Ext.Msg.alert('提示', '请选择一个合同！');
							return false;
						}
						if (entryId == null || entryId == "") {
							url = "/power/workflow/manager/flowshow/flowshow.jsp?flowCode="
									+ "bqCGContract";
							window.open(url);
						} else {
							var url = "/power/workflow/manager/show/show.jsp?entryId="
									+ entryId;
							window.open(url);
						}
					}
				}]
	});

	// 会签票面浏览
	function CheckRptPreview() {
		if (id == null || id == "") {
			Ext.Msg.alert('提示', '请选择一个合同！');
			return false;
		}
		var url = "/powerrpt/report/webfile/bqmis/CGConMeetSign.jsp?conId="
				+ id;
		window.open(url);

	};
	var form = new Ext.form.FormPanel({
		bodyStyle : "padding:5px 5px 0",
		labelAlign : 'right',
		width : Ext.get('div_lay').getWidth(),
		autoHeight : true,
		fileUpload : true,
		region : 'center',
		border : false,
		tbar : formtbar,
		items : [new Ext.form.FieldSet({
			title : '合同基本信息',
			collapsible : true,
			height : '100%',
			layout : 'form',
			items : [{
				border : false,
				layout : 'column',
				items : [{
							border : false,
							layout : 'form',
							columnWidth : 1,
							labelWidth : 110,
							items : [{
										id : 'contractName',
										name : 'con.contractName',
										xtype : 'textfield',
										fieldLabel : '合同名称',
										readOnly : false,
										allowBlank : false,
										anchor : '92.5%'
									}]
						}, {
							columnWidth : .5,
							layout : 'form',
							border : false,
							labelWidth : 110,
							items : [contypeBox]
						}, {
							columnWidth : .5,
							layout : 'form',
							labelWidth : 110,
							border : false,
							items : [conyear]
						}, {
							border : false,
							layout : 'form',
							columnWidth : 1,
							labelWidth : 110,
							items : [{
										id : 'conttreesNo',
										// name : 'con.conttreesNo',
										xtype : 'textfield',
										fieldLabel : '合同编号',
										value : '自动生成',
										readOnly : true,
										allowBlank : true,
										anchor : '92.5%'
									}]
						}, {
							border : false,
							layout : 'form',
							columnWidth : 1,
							labelWidth : 110,
							items : [cliendBox, hidcliendBox]
						}, {
							border : false,
							layout : 'form',
							columnWidth : .5,
							labelWidth : 110,
							items : [itemBox, hiditemId, {
										id : 'operateDepName',
										// name : 'con.operateDepCode',
										xtype : 'textfield',
										fieldLabel : '承办部门',
										readOnly : true,
										allowBlank : false,
										anchor : '85%'
									}]
						}, {
							border : false,
							layout : 'form',
							columnWidth : 0.5,
							labelWidth : 110,
							items : [maifang]
						}, {
							border : false,
							layout : 'form',
							columnWidth : .5,
							labelWidth : 110,
							items : [{
								id : 'operateName',
								// name : 'con.operateBy',
								xtype : 'trigger',
								fieldLabel : '承办人',
								readOnly : true,
								allowBlank : false,
								anchor : '85%',
								onTriggerClick : function(e) {
									var url = "../../../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
									var args = {
										selectModel : 'single',
										notIn : "",
										rootNode : {
											id : '0',
											text : '灞桥电厂'
										}
									}
									var o = window
											.showModalDialog(url, args,
													'dialogWidth=650px;dialogHeight=500px;status=no');
									if (typeof(o) == "object") {
										Ext.get('operateName').dom.value = o.workerName;
										Ext.get('operateDepName').dom.value = o.deptName;
										// unitBox.setValue("");
										// unitStore.load({
										// params : {
										// deptId : o.deptCode
										// }
										// })
										operateBy = o.workerCode;
										operateDepCode = o.deptCode;
										deptId = o.deptId;
									}
								}
							}
							// , unitBox, hidunitBox
							]
						}, {
							border : false,
							layout : 'form',
							columnWidth : 0.2,
							labelWidth : 100,
							items : [isTotal]
						}, {
							border : false,
							layout : 'form',
							columnWidth : 0.3,
							labelWidth : 80,
							items : [typeBox]
						}, {
							border : false,
							layout : 'form',
							columnWidth : 0.5,
							labelWidth : 110,
							items : [actAmount]
						}
						// , {
						// columnWidth : .5,
						// layout : 'form',
						// labelWidth : 110,
						// hidden : true,
						// border : false,
						// items : [startDate]
						// }
						// , {
						// columnWidth : .5,
						// layout : 'form',
						// hidden : true,
						// labelWidth : 110,
						// border : false,
						// items : [endDate]
						// }
						// , {
						// columnWidth : .5,
						// layout : 'form',
						// labelWidth : 110,
						// hidden : true,
						// border : false,
						// items : [prosyStartDate]
						// }, {
						// columnWidth : .5,
						// layout : 'form',
						// labelWidth : 110,
						// hidden : true,
						// border : false,
						// items : [prosyEndDate]
						// }
						, {
							border : false,
							layout : 'form',
							columnWidth : 1,
							labelWidth : 110,
							items : [{
										id : 'conAbstract',
										name : 'con.conAbstract',
										xtype : 'textarea',
										fieldLabel : '合同简介',
										readOnly : false,
										height : 120,
										anchor : '92.5%'
									}]
						}
						// , {
						// columnWidth : .5,
						// layout : 'form',
						// border : false,
						// labelWidth : 110,
						// hidden : true,
						// items : [isMeet]
						// }
						// , {
						// columnWidth : .5,
						// layout : 'form',
						// border : false,
						// hidden : true,
						// labelWidth : 110,
						// items : [isEmergency]
						// }
						, {
							columnWidth : .5,
							layout : 'form',
							labelWidth : 110,
							border : false,
							items : [entryName]
						}, {
							columnWidth : .5,
							layout : 'form',
							labelWidth : 110,
							border : false,
							items : [entryDate]
						}, {
							border : false,
							layout : 'form',
							columnWidth : 0.5,
							labelWidth : 110,
							items : [operateTel]
						}, {
							border : false,
							layout : 'form',
							columnWidth : 0.5,
							labelWidth : 110,
							items : [prosy_by, hidprosy_by]
						}, {
							border : false,
							layout : 'form',
							columnWidth : 0.4,
							labelWidth : 110,
							items : [oriFileName]
						}, {
							border : false,
							layout : 'form',
							columnWidth : 0.2,
							labelWidth : 80,
							items : [btnView]
						}, {
							border : false,
							layout : 'form',
							columnWidth : 1,
							labelWidth : 110,
							items : [{
										id : 'conId',
										name : 'con.conId',
										xtype : 'numberfield',
										fieldLabel : '合同id',
										readOnly : false,
										hidden : true,
										hideLabel : true,
										anchor : '92.5%'
									}]
						}]
			}]
		})]
	});
	var annextbar = new Ext.Toolbar({
		id : 'annextbar',
		items : ['合同附件：', {
					id : 'btnAnnexAdd',
					text : "增加",
					iconCls : 'add',
					handler : function() {
						if (id == "") {
							Ext.Msg.alert('提示', '请先从列表中选择合同！');
							return false;
						}
						docwin.setTitle("增加合同附件");
						docform.getForm().reset();
						docwin.show();
						// Ext.get("fjdocFile").dom.select();
						// document.selection.clear();
						Ext.get('docType').dom.value = "CONATT";
						Ext.get('keyId').dom.value = id;
						Ext.get('lastModifiedName').dom.value = sessWorname;
						docmethod = 'add';
					}
				},
				// '-', {
				// id : 'btnAnnexSave',
				// text : "修改",
				// iconCls : 'update',
				// handler : function() {
				// docmethod = 'update';
				// var selrows = annexGrid.getSelectionModel().getSelections();
				// if (selrows.length > 0) {
				// var record = annexGrid.getSelectionModel().getSelected();
				// docform.getForm().reset();
				// docwin.show(Ext.get('btnAnnexSave'));
				// docform.getForm().loadRecord(record);
				// docwin.setTitle("修改合同附件");
				// } else {
				// Ext.Msg.alert('提示', '请选择您要修改的合同附件！');
				// }
				// }
				// },
				'-', {
					id : 'btnAnnexDelete',
					text : "删除",
					iconCls : 'delete',
					handler : function() {
						var selrows = annexGrid.getSelectionModel()
								.getSelections();
						if (selrows.length > 0) {
							Ext.Msg.confirm('提示', '删除的数据您将不能恢复,确定要删除吗?',
									function(b) {
										if (b == "yes") {
											var record = annexGrid
													.getSelectionModel()
													.getSelected();
											Ext.Ajax.request({
												url : 'managecontract/deleteConDoc.action',
												params : {
													docid : record.data.conDocId
												},
												method : 'post',
												waitMsg : '正在删除数据...',
												success : function(result,
														request) {
													Ext.MessageBox.alert('提示',
															'删除成功!');
													annex_ds.reload();
												},
												failure : function(result,
														request) {
													Ext.MessageBox.alert('错误',
															'操作失败,请联系管理员!');
												}
											});
										}

									});

						} else {
							Ext.Msg.alert('提示', '请选择您要删除的合同附件！');
						}
					}
				}]
	});
	var annex_item = Ext.data.Record.create([{
				name : 'conDocId'
			}, {
				name : 'keyId'
			}, {
				name : 'docName'
			}, {
				name : 'docMemo'
			}, {
				name : 'oriFileName'
			}, {
				name : 'oriFileExt'
			}, {
				name : 'lastModifiedDate'
			}, {
				name : 'lastModifiedBy'
			}, {
				name : 'lastModifiedName'
			}, {
				name : 'docType'
			}, {
				name : 'oriFile'
			}]);
	var annex_sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			});
	var annex_item_cm = new Ext.grid.ColumnModel([annex_sm,
			new Ext.grid.RowNumberer({
						header : '项次号',
						width : 50,
						align : 'center'
					}), {
				header : '名称',
				dataIndex : 'docName',
				align : 'center'
			}, {
				header : '备注',
				dataIndex : 'docMemo',
				align : 'center'
			}, {
				header : '原始文件',
				dataIndex : 'oriFile',
				align : 'center'
			}, {
				header : '上传日期',
				dataIndex : 'lastModifiedDate',
				width : 120,
				align : 'center'
			}, {
				header : '上传人',
				dataIndex : 'lastModifiedName',
				align : 'center'
			}, {
				header : '查看附件',
				dataIndex : 'conDocId',
				align : 'center',
				renderer : function(val) {
					// var val = record.get("fileCode")
					// + record.get("fileType");

					if (val != "" && val != null) {
						return "<a style=\"cursor:hand;color:red\" onClick=\"window.open('/power/managecontract/showConFile.action?conid="
								+ id
								+ "&conDocId="
								+ val
								+ "&type=CONATT');\"/>查看附件</a>"
					} else {
						return "";
					}
				}
			}]);

	annex_item_cm.defaultSortable = true;
	var annex_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'managecontract/findDocList.action'
						}),
				reader : new Ext.data.JsonReader({}, annex_item)
			});

	var annexGrid = new Ext.grid.GridPanel({
				ds : annex_ds,
				cm : annex_item_cm,
				sm : annex_sm,
				// title : '合同附件',
				width : Ext.get('div_lay').getWidth(),
				split : true,
				// autoHeight:true,
				height : 150,
				autoScroll : true,
				// collapsible : true,
				tbar : annextbar,
				border : false
			});
	annexGrid.on('rowdblclick', function(grid, rowIndex, e) {
				var record = annexGrid.getSelectionModel().getSelected();
				docmethod = 'update';
				docform.getForm().reset();
				docwin.show(Ext.get('btnAnnexSave'));
				docform.getForm().loadRecord(record);
				docwin.setTitle("修改合同附件");

			});
	var Credenttbar = new Ext.Toolbar({
		items : ['合同凭据：', {
					id : 'btnCredentAdd',
					text : "增加",
					iconCls : 'add',
					handler : function() {
						if (id == "") {
							Ext.Msg.alert('提示', '请先从列表中选择合同!');
							return false;
						}
						docwin.setTitle("增加合同凭据");
						docform.getForm().reset();
						docwin.show();
						// Ext.get("fjdocFile").dom.select();
						// document.selection.clear();
						Ext.get('docType').dom.value = "CONEVI";
						Ext.get('keyId').dom.value = id;
						Ext.get('lastModifiedName').dom.value = sessWorname;
						docmethod = 'add';
					}
				}, '-',
				// {
				// id : 'btnCredentSave',
				// text : "修改",
				// iconCls : 'update',
				// handler : function() {
				// docmethod = 'update';
				// var selrows =
				// CredentGrid.getSelectionModel().getSelections();
				// if (selrows.length > 0) {
				// var record = CredentGrid.getSelectionModel().getSelected();
				// docform.getForm().reset();
				// docwin.show(Ext.get('btnCredentSave'));
				// docform.getForm().loadRecord(record);
				// docwin.setTitle("修改合同凭据");
				// } else {
				// Ext.Msg.alert('提示', '请选择您要修改的合同凭据！');
				// }
				// }
				// }, '-',
				{
					id : 'btnCredentDelete',
					text : "删除",
					iconCls : 'delete',
					handler : function() {
						var selrows = CredentGrid.getSelectionModel()
								.getSelections();
						if (selrows.length > 0) {
							Ext.Msg.confirm('提示', '删除的数据您将不能恢复,确定要删除吗?',
									function(b) {
										if (b == "yes") {
											var record = CredentGrid
													.getSelectionModel()
													.getSelected();
											Ext.Ajax.request({
												url : 'managecontract/deleteConDoc.action',
												params : {
													docid : record.data.conDocId
												},
												method : 'post',
												waitMsg : '正在删除数据...',
												success : function(result,
														request) {
													Credent_ds.reload();
												},
												failure : function(result,
														request) {
													Ext.MessageBox.alert('错误',
															'操作失败,请联系管理员!');
												}
											});
										}

									});

						} else {
							Ext.Msg.alert('提示', '请选择您要删除的合同凭据！');
						}
					}
				}]
	});
	var Credent_item = Ext.data.Record.create([{
				name : 'conDocId'
			}, {
				name : 'keyId'
			}, {
				name : 'docName'
			}, {
				name : 'docMemo'
			}, {
				name : 'oriFileName'
			}, {
				name : 'oriFileExt'
			}, {
				name : 'lastModifiedDate'
			}, {
				name : 'lastModifiedBy'
			}, {
				name : 'lastModifiedName'
			}, {
				name : 'docType'
			}, {
				name : 'oriFile'
			}]);
	var Credent_sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			});
	var Credent_item_cm = new Ext.grid.ColumnModel([Credent_sm,
			new Ext.grid.RowNumberer({
						header : '项次号',
						width : 50,
						align : 'center'
					}), {
				header : '名称',
				dataIndex : 'docName',
				align : 'center'
			}, {
				header : '备注',
				dataIndex : 'docMemo',
				align : 'center'
			}, {
				header : '原始文件',
				dataIndex : 'oriFile',
				align : 'center'
			}, {
				header : '上传日期',
				dataIndex : 'lastModifiedDate',
				width : 120,
				align : 'center'
			}, {
				header : '上传人',
				dataIndex : 'lastModifiedName',
				align : 'center'
			}, {
				header : '查看附件',
				dataIndex : 'conDocId',
				align : 'center',
				renderer : function(val) {
					// var val = record.get("fileCode")
					// + record.get("fileType");

					if (val != "" && val != null) {
						return "<a style=\"cursor:hand;color:red\" onClick=\"window.open('/power/managecontract/showConFile.action?conid="
								+ id
								+ "&conDocId="
								+ val
								+ "&type=CONEVI');\"/>查看附件</a>"
					} else {
						return "";
					}
				}
			}]);
	Credent_item_cm.defaultSortable = true;
	var Credent_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'managecontract/findDocList.action'
						}),
				reader : new Ext.data.JsonReader({}, Credent_item)
			});

	var CredentGrid = new Ext.grid.GridPanel({
				ds : Credent_ds,
				cm : Credent_item_cm,
				sm : Credent_sm,
				split : true,
				height : 150,
				width : Ext.get('div_lay').getWidth(),
				// title : '合同凭据',
				// collapsible : true,
				tbar : Credenttbar,
				border : false
			});
	CredentGrid.on('rowdblclick', function(grid, rowIndex, e) {
				var record = CredentGrid.getSelectionModel().getSelected();
				docform.getForm().reset();
				docwin.show(Ext.get('btnCredentSave'));
				docform.getForm().loadRecord(record);
				docwin.setTitle("修改合同凭据");

			});
	var lastModifiedDate = new Ext.form.TextField({
		id : 'lastModifiedDate',
		fieldLabel : "上传时间",
		name : 'doc.lastModifiedDate',
		type : 'textfield',
		readOnly : true,
		style : 'cursor:pointer',
		anchor : "95%",
		value : getDate()
			// listeners : {
			// focus : function() {
			// var pkr = WdatePicker({
			// startDate : '%y-%M-%d 00:00:00',
			// dateFmt : 'yyyy-MM-dd HH:mm:ss',
			// alwaysUseStartDate : true
			// });
			// }
			// }
		});
	var fjdocFile = {
		id : "oriFile",
		xtype : "fileuploadfield",
		// inputType : "file",
		fieldLabel : '选择附件',
		// allowBlank : false,
		anchor : "95%",
		height : 22,
		name : 'fjdocFile',
		buttonCfg : {
			text : '浏览...',
			iconCls : 'upload-icon'
		},
		listeners : {
			'fileselected' : function() {
				var url = Ext.get('oriFile').dom.value;
				var format = url.substring(url.lastIndexOf("\\") + 1,
						url.length - 4);
				Ext.get('docName').dom.value = format;
			}
		}
	}
	var doccontent = new Ext.form.FieldSet({
				height : '100%',
				layout : 'form',
				items : [{
							id : 'conDocId',
							name : 'doc.conDocId',
							xtype : 'hidden',
							fieldLabel : '序号',
							readOnly : false,
							hidden : true,
							hideLabel : true,
							anchor : '95%'
						}, {
							id : 'keyId',
							name : 'doc.keyId',
							xtype : 'numberfield',
							fieldLabel : '序号',
							readOnly : false,
							hidden : true,
							hideLabel : true,
							anchor : '95%'
						}, fjdocFile, {
							id : 'docName',
							name : 'doc.docName',
							xtype : 'textfield',
							fieldLabel : '附件名称',
							allowBlank : false,
							readOnly : false,
							anchor : '95%'
						}, {
							id : 'docMemo',
							name : 'doc.docMemo',
							xtype : 'textarea',
							fieldLabel : '备注',
							readOnly : false,
							allowBlank : true,
							anchor : '94.5%'
						}, lastModifiedDate, {
							id : 'lastModifiedName',
							xtype : 'textfield',
							fieldLabel : '上传人',
							readOnly : true,
							anchor : '95%'
						}, {
							id : 'docType',
							name : 'doc.docType',
							xtype : 'textfield',
							fieldLabel : '类型',
							readOnly : false,
							hidden : true,
							hideLabel : true,
							anchor : '95%'
						}]
			});

	var docform = new Ext.form.FormPanel({
				bodyStyle : "padding:5px 5px 0",
				labelAlign : 'right',
				id : 'shift-form',
				labelWidth : 80,
				autoHeight : true,
				fileUpload : true,
				region : 'center',
				border : false,
				items : [doccontent],
				buttons : [{
					text : '保存',
					iconCls : 'save',
					handler : function() {
						var url = '';
						if (docmethod == "add") {
							url = 'managecontract/addConDoc.action';

						} else {
							url = 'managecontract/updateConDoc.action';
						}
						if (!docform.getForm().isValid()) {
							return false;
						}
						docform.getForm().submit({
							url : url,
							method : 'post',
							params : {
								filePath : Ext.get('oriFile').dom.value
							},
							success : function(form, action) {
								var message = eval('('
										+ action.response.responseText + ')');
								Ext.Msg.alert("成功", message.data);
								if (Ext.get('docType').dom.value == "CONATT") {
									annex_ds.load({
												params : {
													conid : id,
													type : "CONATT"
												}
											});
								} else {
									Credent_ds.load({
												params : {
													conid : id,
													type : "CONEVI"
												}
											});
								}
								docwin.hide();
							},
							failure : function(form, action) {
								Ext.Msg.alert('错误', '出现未知错误.');
							}
						})
					}

				}, {
					text : '取消',
					iconCls : 'cancer',
					handler : function() {
						docform.getForm().reset();
						docwin.hide();
					}
				}]
			});
	var docwin = new Ext.Window({
				title : '新增',
				el : 'win',
				modal : true,
				autoHeight : true,
				width : 450,
				closeAction : 'hide',
				items : [docform]
			});
	// 物资明细确定
	var Materialtbar = new Ext.Toolbar({
				items : ['物资明细：', {
							id : 'btnCredentSel',
							text : "采购单选择",
							iconCls : 'add',
							handler : function() {
								selectList();
							}
						}, '-', {
							id : 'btnCredentDelete',
							text : "删除",
							iconCls : 'delete',
							handler : function() {
								deleteList();
							}
						}]
			});

	var Material_item = Ext.data.Record.create([{
				name : 'purNo'
			}, {
				name : 'materialId'
			}, {
				name : 'unitPrice'
			}, {
				name : 'purqty'
			}, {
				name : 'memo'
			}, {
				name : 'materialName'
			}, {
				name : 'specModel'
			}, {
				name : 'total'
			}]);
	var Material_sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			});
	var Material_item_cm = new Ext.grid.ColumnModel([Material_sm,
			new Ext.grid.RowNumberer({
						header : '项次号',
						width : 50,
						align : 'center'
					}), {
				header : '采购单号',
				dataIndex : 'purNo',
				align : 'center'
			}, {
				header : '物资名称',
				dataIndex : 'materialId',
				hidden : true,
				align : 'center'
			}, {
				header : '物资名称',
				dataIndex : 'materialName',
				align : 'center'
			}, {
				header : '规格型号',
				dataIndex : 'specModel',
				align : 'center'
			}, {
				header : '单价',
				dataIndex : 'unitPrice',
				// width : 120,
				align : 'center'
			}, {
				header : '数量',
				dataIndex : 'purqty',
				align : 'center'
			}, {
				header : '总价',
				dataIndex : 'total',
				align : 'center'
			}, {
				header : '备注',
				dataIndex : 'memo',
				align : 'center'
			}]);
	Material_item_cm.defaultSortable = true;
	var Material_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'managecontract/findAllMaterialByContractNo.action'
						}),
				reader : new Ext.data.JsonReader({
							root : "list"
						}, Material_item)
			});

	var MaterialGrid = new Ext.grid.GridPanel({
				ds : Material_ds,
				cm : Material_item_cm,
				sm : Material_sm,
				split : true,
				height : 150,
				width : Ext.get('div_lay').getWidth(),
				// title : '合同凭据',
				// collapsible : true,
				tbar : Materialtbar,
				border : false
			});
	function selectList() {
		var args = {
			selectModel : 'multiple'
		}
		var rvo = window
				.showModalDialog(
						'purchaseBillList.jsp',
						args,
						'dialogWidth:900px;dialogHeight:600px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (rvo != undefined) {
			var purCode = rvo.codes;
			Ext.Ajax.request({
						url : 'managecontract/updateMaterial.action',
						method : 'post',
						params : {
							purNo : purCode,
							contractNo : contractNo,
							method : "select"
						},
						success : function(result, request) {
							Ext.Msg.alert("提示", "添加成功!");
							Material_ds.load({
										params : {
											contractNo : contractNo
										}
									});

						},
						failure : function(result, request) {
							Ext.Msg.alert("提示", "操作失败");
						}
					});
		}
	}
	function deleteList() {
		var record = MaterialGrid.getSelectionModel().getSelected();
		var selected = MaterialGrid.getSelectionModel().getSelections();
		if (selected.length != 0) {
			var purCode = record.data.purNo;
			Ext.Ajax.request({
						url : 'managecontract/updateMaterial.action',
						method : 'post',
						params : {
							purNo : purCode,
							contractNo : contractNo,
							method : "delete"
						},
						success : function(result, request) {
							Ext.Msg.alert("提示", "删除成功!");
							Material_ds.load({
										params : {
											contractNo : contractNo
										}
									});

						},
						failure : function(result, request) {
							Ext.Msg.alert("提示", "操作失败");
						}
					});

		} else {
			Ext.Msg.alert("提示", "请选择你要删除的记录")
		}
	}
	getWorkCode();
	if (id != "") {
		Ext.Ajax.request({
					url : 'managecontract/findMeetConModel.action',
					params : {
						conid : id
					},
					method : 'post',
					waitMsg : '正在加载数据...',
					success : function(result, request) {
						var o = eval('(' + result.responseText + ')');
						form.getForm().loadRecord(o);
						method = "update";
						 Ext.get ("conFile").dom.value = (o.data.filePath ==
						 null
						 ? ""
						 : o.data.filePath);
						 if (o.data.filePath == null || o.data.filePath == "")
						 {
						 Ext.getCmp("btnView").setDisabled(true);
						 } else {
						 btnView.setDisabled(false);
						 }
						cliendBox.setValue((o.data.cliendName == null)
								? ""
								: o.data.cliendName);
						hidcliendBox.setValue((o.data.cliendId == null)
								? ""
								: o.data.cliendId);
						conyear.setValue((o.data.conYear == null)
								? ""
								: o.data.conYear);
						entryBy = o.data.entryBy;
						operateBy = o.data.operateBy;
						operateDepCode = o.data.operateDepCode;
						entryId = o.data.workflowNo;
						if (o.data.itemId != null) {
							hiditemId.setValue(o.data.itemId)
							itemBox.setValue(o.data.itemName);
						}
						contypeBox.setValue(o.data.conCode)
						// if (o.data.operateLeadBy != null) {
						// hidunitBox.setValue(o.data.operateLeadBy)
						// Ext.getCmp('operateLeadBy')
						// .setValue(o.data.operateLeadName);
						// // Ext.form.ComboBox.superclass.setValue.call(Ext
						// // .getCmp('operateLeadBy'),
						// // o.data.operateLeadName);
						// }
						if (o.data.prosy_by != null) {
							hidprosy_by.setValue(o.data.prosy_by);
							prosy_by.setValue(o.data.prosy_byName);

						}
						if (o.data.isSum == "Y") {
							Ext.get('isTotal').dom.checked = true;
							isTotal.checked = true;
						} else {
							Ext.get('isTotal').dom.checked = false;
							isTotal.checked = false;
							actAmount.setDisabled(true);
						}
						// if (o.data.isSign == "Y") {
						// Ext.get('isMeet').dom.checked = true;
						// } else {
						// Ext.get('isMeet').dom.checked = false;
						// }
						// if (o.data.isInstant == "Y") {
						// Ext.get('isEmergency').dom.checked = true;
						// } else {
						// Ext.get('isEmergency').dom.checked = false;
						// }
						annex_ds.load({
									params : {
										conid : id,
										type : "CONATT"
									}
								});
						Credent_ds.load({
									params : {
										conid : id,
										type : "CONEVI"
									}
								});
						Material_ds.load({
									params : {
										contractNo : contractNo
									}
								});
						if (revoke != "") {

							Ext.get('btnSave').dom.disabled = true
							Ext.get('btnDelete').dom.disabled = true;
							Ext.get('btnReport').dom.disabled = true;
						} else {
							annextbar.setDisabled(false);
							Credenttbar.setDisabled(false);
							Materialtbar.setDisabled(false);
						}
					},
					failure : function(result, request) {
						Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
					}
				});
		method = "update";
	} else {
		parent.Ext.getCmp("maintab").setActiveTab(2);
		parent.Ext.getCmp("maintab").setActiveTab(1);
	}
	var layout = new Ext.Panel({
				autoWidth : true,
				autoHeight : true,
				border : false,
				autoScroll : true,
				split : true,
				items : [form, annexGrid, CredentGrid, MaterialGrid]
			});
	layout.render(Ext.getBody());
})