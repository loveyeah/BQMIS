Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
/**
 * diable后将TriggerClick事件禁用
 */
Ext.override(Ext.form.TriggerField, {
			onEnable : function() {
				Ext.form.TriggerField.superclass.onEnable.call(this);

				this.trigger.removeClass('x-item-disabled');
			},
			onDisable : function() {
				Ext.form.TriggerField.superclass.onDisable.call(this);

				this.trigger.addClass('x-item-disabled');
			}
		});
Ext.onReady(function() {
	// var edit1 = parent.Ext.getCmp('maintab').edit1;
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
	var operateBy;
	var operateDepCode;
	var contractId = getParameter("conId");
	var balanceId = getParameter("balanceId");
	var balaFlag = getParameter("balaFlag");
	var currencyName = getParameter("currencyName");
	var balanceName = getParameter("balanceName");
	var balanceby = getParameter("balanceBy");
	var entryId;
	var method = "add";
	var docmethod = "";
	// 判断会签表为质保金、非质保金
	var memo;
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
		// s += (t > 9 ? "" : "0") + t + ":";
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
							lastModifiedName.setValue(workerName);
						}
					}
				});
	}

	var operateBox = new Ext.form.TextField({
				fieldLabel : '经办人',
				id : 'operateBy',
				readOnly : true,
				anchor : "100%"
			});
	var hidoperateBox = new Ext.form.Hidden({
				id : 'hidoperateBox',
				name : 'bal.operateBy'
			})

	// 币别
	var currencyType = new Ext.form.TextField({
				fieldLabel : '币别',
				id : 'currencyType',
				readOnly : true,
				anchor : "100%"

			})
	var applicatDate = new Ext.form.TextField({
				id : 'applicatDate',
				fieldLabel : "申请日期",
				readOnly : true,
				name : 'bal.applicatDate',
				style : 'cursor:pointer',
				anchor : "100%",
				value : getDate()
			});
	var balaMethod = new Ext.form.ComboBox({
				fieldLabel : '付款方式',
				store : [['1', '银行转帐'], ['2', '现金支付'], ['3', '支票支付'],
						['4', '物品抵押'], ['5', '其他方式']],
				id : 'balaMethod',
				name : 'balaMethod',
				valueField : "value",
				displayField : "text",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'bal.balaMethod',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : false,
				emptyText : '请选择',
				anchor : '100%',
				// addBy LiuYingwen 09/04/23
				listeners : {
					'select' : function(combo, record, index) {
						if (balaMethod.getValue() != "3") {
							Ext.get("chequeNo").dom.readOnly = true;
							Ext.get("chequeNo").dom.value = "";
						} else
							Ext.get("chequeNo").dom.readOnly = false;

					}
				}
			});
	// balaMethod.addListener('change', function(box, newv, oldv) {
	// if (balaMethod.getValue() != "3")
	// Ext.get("chequeNo").dom.readOnly = true;
	// else
	// Ext.get("chequeNo").dom.readOnly = false;
	// })
	function clickByName() {
		var url = "../../../../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
		var args = {
			selectModel : 'single',
			notIn : "",
			rootNode : {
				id : '0',
				text : '灞桥电厂'
			}
		}
		var o = window.showModalDialog(url, args,
				'dialogWidth=650px;dialogHeight=500px;status=no');
		if (typeof(o) == "object") {
			Ext.get('balanceName').dom.value = o.workerName;
			balanceBy.setValue(o.workerCode);
			// operateBy = o.workerCode;
		}

	}
	// 财务部经办人
	var balanceByName = new Ext.form.ComboBox({
				name : 'balanceName',
				id : 'balanceName',
//				fieldLabel : '财务部经办人',
				hidden : true,
				// mode : 'remote',
				editable : false,
				allowBlank : false,
				emptyText : '请选择',
				anchor : '100%',
				triggerAction : 'all',
				onTriggerClick : function(e) {
					if (!this.disabled) {
						clickByName()
					}
					this.blur();
				}

			})
	// 财务部经办人编码
	var balanceBy = new Ext.form.Hidden({
				id : 'balanceBy',
				name : 'bal.balanceBy'
			})

	var cliendBox = new Ext.form.ComboBox({
				fieldLabel : '供应商',
				store : [['1', '燃料供应商'], ['2', '设备供应商']],
				id : 'cliendId',
				name : 'cliendId',
				valueField : "value",
				displayField : "text",
				mode : 'local',
				typeAhead : true,
				// forceSelection : true,
				hiddenName : 'con.cliendId',
				editable : false,
				readOnly : true,
				// triggerAction : 'all',
				// selectOnFocus : true,
				allowBlank : false,
				emptyText : '请选择',
				anchor : '92.5%'
			});
	var itemBox = new Ext.form.ComboBox({
				fieldLabel : '费用来源',
				store : [['1', '电力供应'], ['2', '公司']],
				id : 'itemId',
				name : 'itemId',
				valueField : "value",
				displayField : "text",
				mode : 'local',
				typeAhead : true,
				// forceSelection : true,
				hiddenName : 'con.itemId',
				editable : false,
				readOnly : true,
				// triggerAction : 'all',
				// selectOnFocus : true,
				allowBlank : false,
				emptyText : '请选择',
				anchor : '85%'
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
	// 票面浏览report/webfile/bqmis/
	function CheckRptPreview() {
		if (balanceId == null || balanceId == "") {
			Ext.Msg.alert('提示', '请选择一条结算记录');
			return false;
		}
		var url;
		if (memo == "YES") {
			url = "/powerrpt/report/webfile/prjConQuality.jsp?balanceId="
					+ balanceId;
		} else {
			url = "/powerrpt/report/webfile/bqmis/GCContractBalance.jsp?balanceId="
					+ balanceId;
		}
		window.open(url);

	};

	// -------add by fyyang ----090728 质保金上报弹出填写质保期限窗口----------
	var txtMyContractId = new Ext.form.Hidden({
				name : 'myContractId',
				value : contractId

			});
	var txtWarrantyPeriod = new Ext.form.NumberField({
				id : 'txtWarrantyPeriod',
				name : 'warrantyPeriod',
				fieldLabel : '质保期限'
			});

	var myWarrantyPeriodPanel = new Ext.FormPanel({
				frame : true,
				labelAlign : 'center',
				labelWidth : 60,
				closeAction : 'hide',
				title : '填写质保期限',
				items : [txtMyContractId, txtWarrantyPeriod]
			});

	var winWarrantyPeriod = new Ext.Window({
		width : 300,
		height : 150,
		buttonAlign : "center",
		items : [myWarrantyPeriodPanel],
		layout : 'fit',
		closeAction : 'hide',
		resizable : false,
		modal : true,
		buttons : [{
			text : '保存',
			iconCls : 'save',
			handler : function() {
				if (txtWarrantyPeriod.getValue() == "") {
					Ext.Msg.alert("提示", "请输入质保期限！");
					return;
				}
				var myurl = "";
				myurl = "managecontract/saveWarrantyPeriod.action";
				myWarrantyPeriodPanel.getForm().submit({
					method : 'POST',
					url : myurl,
					success : function(form, action) {
						var o = eval("(" + action.response.responseText + ")");
						if (o.msg.indexOf("成功") != -1) {
							winWarrantyPeriod.hide();
							var url = 'reportsign.jsp'
							var args = new Object();
							args.entryId = entryId;
							args.balanceId = balanceId;
							args.passPrice = Ext.get("passPrice").dom.value;
							args.workflowType = "prjConQuality";
							var o = window
									.showModalDialog(url, args,
											'status:no;dialogWidth=800px;dialogHeight=550px');

							if (o) {
								balaFlag = 1;
								btnSetDisabled();// addBy
								balform.getForm().reset();
								parent.parent.edit('', '', '', '', '', '');
								parent.parent.iframe1.document
										.getElementById("btnQuery").click();
								parent.parent.iframe1.document
										.getElementById("blancebar").click();
								parent.parent.Ext.getCmp("maintab")
										.setActiveTab(0);
							}

						}
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
				winWarrantyPeriod.hide();
			}
		}]

	});

	// ---------add by fyyang end-----------------------------------------
	var formtbar = new Ext.Toolbar({
		items : [{
			id : 'btnAdd',
			text : "增加",
			iconCls : 'add',
			handler : function() {
				if (Ext.get("btnSave").dom.disabled) {
					balform.getForm().reset();
					btnFreeDisabled();
					Ext.get("paymentId").dom.value = "";
					method = "add";
					annex_ds.load({
								params : {
									balanceId : null,
									type : "CONPAY"
								}
							});
				} else {
					if (!balform.getForm().isValid()) {
						return false;
					}
					if (Ext.get("balanceId").dom.value != ""
							&& Ext.get("balanceId").dom.value.length > 0) {
						Ext.Msg.confirm('提示', '是否保存数据?', function(b) {
							if (b == "yes") {
								balform.getForm().submit({
									url : "managecontract/updateBalance.action",
									method : 'post',
									waitMsg : '操作中，请稍等！',
									success : function(action, form) {
										balform.getForm().reset();
										btnFreeDisabled();
										Ext.get("paymentId").dom.value = "";
										method = "add";
										annex_ds.load({
													params : {
														balanceId : null,
														type : "CONPAY"
													}
												});
									},
									failure : function(action, form) {
										Ext.Msg.alert('提示信息', '操作失败，请联系管理员！');
									}
								})
							} else {
								balform.getForm().reset();
								btnFreeDisabled();
								Ext.get("paymentId").dom.value = "";
								method = "add";
								annex_ds.load({
											params : {
												balanceId : null,
												type : "CONPAY"
											}
										});
							}
						})
					} else {
						balform.getForm().reset();
						btnFreeDisabled();
						Ext.get("paymentId").dom.value = "";
						method = "add";
						annex_ds.load({
									params : {
										balanceId : null,
										type : "CONPAY"
									}
								});
					}
				}
			}
		}, '-', {
			id : 'btnDelete',
			text : "删除",
			iconCls : 'delete',
			handler : function() {
				if (Ext.get("balanceId").dom.value != ""
						&& Ext.get("balanceId").dom.value.length > 0) {
					Ext.Msg.confirm('提示', '删除的数据您将不能恢复,确定要删除吗?', function(b) {
						if (b == "yes") {
							balform.getForm().submit({
								url : 'managecontract/deleteBal.action',
								method : 'post',
								waitMsg : '操作中，请稍等！',
								success : function(action, form) {
									parent.parent.iframe1.document
											.getElementById("btnQuery").click();
									parent.parent.iframe1.document
											.getElementById("blancebar")
											.click();
									balanceId = "";
									parent.parent.edit('', '', '', '', '', '');
									annex_ds.load({
												params : {
													balanceId : balanceId,
													type : "CONPAY"
												}
											});
									Ext.Msg.alert('提示信息', '删除成功！');
									balanceByName.setValue("");
									balform.getForm().reset();
								},
								failure : function(action, form) {
									Ext.Msg.alert('提示信息', '数据库异常！');
								}
							})

						}
					})
				}

			}
		}, '-', {
			id : 'btnSave',
			text : "保存",
			iconCls : 'save',
			handler : function() {
				if (!balform.getForm().isValid()) {
					return false;
				}
				var url = "";
				if (method == "add") {
					balform.getForm().submit({
						url : "managecontract/addBalance.action",
						method : 'post',
						waitMsg : '操作中，请稍等！',
						success : function(form, action) {
							parent.parent.iframe1.document
									.getElementById("btnQuery").click();
							parent.parent.iframe1.document
									.getElementById("blancebar").click();
							var o = eval('(' + action.response.responseText
									+ ')');
							Ext.get("balanceId").dom.value = o.balId;
							balanceId = o.balId;// addBy LiuYingwen 09/04/23
							var balaFlag = o.balaFlag;
							parent.edit1(balaFlag, balanceId);
							parent.parent.edit('', '', '', '', '', '');
							// parent.parent.Ext.getCmp("maintab").setActiveTab(0);
							parent.Ext.getCmp("maintab").setActiveTab(1);
							var url = "manage/projectFact/business/contractBalance/register/conBalance/balInvoice/balInvoice.jsp";
							url += "?balanceId=" + balanceId;
							url += "&balaFlag=" + balaFlag;
//							parent.document.all.iframe2.src = url;
							parent.Ext.getCmp("maintab").setActiveTab(0);
							Ext.Msg.alert('提示信息', '保存成功！');
						},
						failure : function(form, action) {
							Ext.Msg.alert('提示信息', '操作失败，请联系管理员！');
						},
						params : {
							conId : contractId
						}
					})
					method = "update";
				} else {
					balform.getForm().submit({
						url : "managecontract/updateBalance.action",
						method : 'post',
						waitMsg : '操作中，请稍等！',
						success : function(form, action) {
							parent.parent.iframe1.document
									.getElementById("btnQuery").click();
							parent.parent.iframe1.document
									.getElementById("blancebar").click();
							// var o = eval('(' + action.response.responseText
							// + ')');
							parent.parent.edit('', '', '', '', '', '');
							Ext.Msg.alert('提示信息', '操作成功！');
						},
						failure : function(form, action) {
							Ext.Msg.alert('提示信息', '操作失败，请联系管理员！');
						}
					})
				}

			}
		}, '-', {
			id : 'btnReport',
			text : "上报",
			iconCls : 'upcommit',
			handler : function() {
				if (balanceId == null || balanceId == "") {
					Ext.Msg.alert('提示', '请选择一条结算记录');
					return false;
				}
				var url = 'reportsign.jsp'
				var args = new Object();
				args.entryId = entryId;
				args.balanceId = balanceId;
				args.passPrice = Ext.get("passPrice").dom.value;
				args.workflowType = "prjConBalance";
				var o = window.showModalDialog(url, args,
						'status:no;dialogWidth=800px;dialogHeight=550px');
				if (o) {
					balaFlag = 1;
					btnSetDisabled();// addBy
					balform.getForm().reset();
					parent.parent.edit('', '', '', '', '', '');
					parent.parent.iframe1.document.getElementById("btnQuery")
							.click();
					parent.parent.iframe1.document.getElementById("blancebar")
							.click();
					parent.parent.Ext.getCmp("maintab").setActiveTab(0);
				}

			}
		},
				// -------------add by fyyang
				// ---090728------增加质保金上报-----------------
				'-', {
					id : 'btnQualityReport',
					text : '质保金上报',
					iconCls : 'upcommit',
					hidden : true,
					handler : function() {
						if (balanceId == null || balanceId == "") {
							Ext.Msg.alert('提示', '请选择一条结算记录');
							return false;
						}
						var url = 'reportsign.jsp'
						var args = new Object();
						args.entryId = entryId;
						args.balanceId = balanceId;
						args.passPrice = Ext.get("passPrice").dom.value;
						args.workflowType = "prjConQuality";
						var o = window
								.showModalDialog(url, args,
										'status:no;dialogWidth=800px;dialogHeight=550px');

						if (o) {
							balaFlag = 1;
							btnSetDisabled();// addBy
							balform.getForm().reset();
							parent.parent.edit('', '', '', '', '', '');
							parent.parent.iframe1.document
									.getElementById("btnQuery").click();
							parent.parent.iframe1.document
									.getElementById("blancebar").click();
							parent.parent.Ext.getCmp("maintab").setActiveTab(0);
						}
					}
				},
				// -------------add end
				// ----------------------------------------------------------
				'-', {
					id : 'btnQuery',
					text : "会签查询",
					iconCls : 'view',
					handler : function() { // add by drdu 09/04/27
						if (balanceId == null || balanceId == "") {
							Ext.Msg.alert('提示', '请选择一条结算记录');
							return false;
						}
						if (entryId == null || entryId == "") {
							url = "/power/workflow/manager/flowshow/flowshow.jsp?flowCode="
									+ "prjConBalance";
							window.open(url);
						} else {
							var url = "/power/workflow/manager/show/show.jsp?entryId="
									+ entryId;
							window.open(url);
						}
					}
				}, '-', {
					id : 'balSignTable',
					text : "结算审批表",
					iconCls : 'pdfview',
					handler : function() {
						CheckRptPreview();
					}
				}]
	});
	var mybtn = new Ext.Button({
		id : 'but',
		// name : 'but',
		text : '选择付款计划',
		listeners : {
			'click' : function() {
				var url = "payPlan.jsp?conId=" + contractId;
				var o = window
						.showModalDialog(url, "",
								"dialogHeight:600px;dialogWidth:800px;status:no;scroll:yes;help:no");
				if (o != null) {
					Ext.get("paymentId").dom.value = o.paymentId;
					Ext.get("applicatPrice").dom.value = o.pay;
					Ext.get("passPrice").dom.value = o.pay;
					operateBox.setValue(o.operateName);
					hidoperateBox.setValue(o.operateBy);
					currencyType.setValue(o.currencyName);
				}
			}
		}

	})

	var balform = new Ext.form.FormPanel({
		bodyStyle : "padding:5px 5px 0",
		labelAlign : 'right',
		title : '本次结算申请',
		// autoHeight : true,
		// Height : 100,
		region : 'center',
		border : false,
		tbar : formtbar,
		items : [{
					border : false,
					layout : 'column',
					items : [{
								border : false,
								layout : 'form',
								columnWidth : 1,
								labelWidth : 110,
								items : [{
											id : 'balanceId',
											name : 'bal.balanceId',
											fieldLabel : 'ID',
											xtype : 'hidden',
											anchor : '92.5%'
										}]
							}, {
								columnWidth : 1,
								layout : 'form',
								border : false,
								labelWidth : 110,
								items : [{
											id : 'balaCause',
											name : 'bal.balaCause',
											xtype : 'textarea',
											fieldLabel : '付款原因',
											readOnly : false,
											height : 50,
											anchor : '99%'
										}]
							}, {
								columnWidth : 1,
								layout : 'form',
								border : false,
								labelWidth : 110,
								items : [{
											id : 'balaBatch',
											name : 'bal.balaBatch',
											xtype : 'textarea',
											fieldLabel : '付款说明',
											height : 50,
											readOnly : false,
											anchor : '99%'
										}]
							}, {
								layout : 'column',
								border : false,
								items : [{
											columnWidth : 0.8,
											border : false,
											layout : 'form',
											labelWidth : 110,
											items : [{
														id : 'applicatPrice',
														name : 'bal.applicatPrice',
														xtype : 'numberfield',
														fieldLabel : '申请付款',
														readOnly : true,
														allowBlank : false,
														anchor : '85%'
													}]
										}, {
											id : 'paymentId',
											name : 'bal.paymentId',
											xtype : 'hidden'
										}, {
											columnWidth : 0.2,
											labelWidth : 110,
											layout : 'form',
											items : [mybtn]
										}]
							}, {
								border : false,
								// bodyStyle : "padding:0px 5px 0",
								layout : 'form',
								columnWidth : 0.5,
								labelWidth : 110,
								items : [currencyType]
							}, {
								border : false,
								layout : 'form',
								columnWidth : 0.5,
								labelWidth : 110,
								items : [operateBox, hidoperateBox]
							}, {
								columnWidth : .5,
								layout : 'form',
								labelWidth : 110,
								border : false,
								items : [applicatDate]
							}, {
								border : false,
								layout : 'form',
								columnWidth : .5,
								labelWidth : 110,
								items : [balaMethod]
							}
//							, {
//								columnWidth : .5,
//								layout : 'form',
//								labelWidth : 110,
//								border : false,
//								items : [balanceByName, balanceBy]
//							}
							, {
								columnWidth : .5,
								layout : 'form',
								labelWidth : 110,
								border : false,
								items : [{
											id : 'passPrice',
											name : 'bal.passPrice',
											xtype : 'numberfield',
											fieldLabel : '批准付款',
											readOnly : true,
											allowBlank : true,
											anchor : '100%'
										}]
							}, {
								border : false,
								layout : 'form',
								columnWidth : 1,
								labelWidth : 110,
								items : [{
											id : 'chequeNo',
											name : 'bal.chequeNo',
											// xtype : 'numberfield',
											// fieldLabel : '支票号',
											readOnly : false,
											hidden : true,
											allowBlank : true,
											anchor : '100%'
										}]
							}]
				}
		// ]
		// })
		]
	});
	var annextbar = new Ext.Toolbar({
		items : ['付款附件：', {
					id : 'btnAnnexAdd',
					text : "增加",
					iconCls : 'add',
					handler : function() {
						if (Ext.get("balanceId").dom.value != "") {
							docmethod = "add";
							docform.getForm().reset();
							docwin.show();
						} else {
							Ext.Msg.alert('提示信息', '请先保存结算申请！')
						}
					}
				},
				// '-', {
				// id : 'btnAnnexSave',
				// text : "修改",
				// iconCls : 'update',
				// handler : function() {
				// if (Ext.get("balanceId").dom.value != "") {
				// docmethod = "update";
				// var seldocs = annexGrid.getSelectionModel().getSelections();
				// if (seldocs.length > 0) {
				// var rec = annexGrid.getSelectionModel().getSelected();
				// docform.getForm().reset();
				// docwin.show();
				// docform.getForm().loadRecord(rec);
				// docwin.setTitle("修改付款附件");
				// } else {
				// Ext.Msg.alert('提示', '请选择您要修改的付款附件！');
				// }
				// } else {
				// Ext.Msg.alert('提示信息', '请先保存结算申请！')
				// }
				// }
				// },
				'-', {
					id : 'btnAnnexDelete',
					text : "删除",
					iconCls : 'delete',
					handler : function() {
						var seldocs = annexGrid.getSelectionModel()
								.getSelections();
						if (seldocs.length > 0) {
							Ext.Msg.confirm('提示', '删除的数据您将不能恢复,确定要删除吗?',
									function(b) {
										if (b == "yes") {
											var rec = annexGrid
													.getSelectionModel()
													.getSelected();
											Ext.Ajax.request({
												url : 'managecontract/deleteBalDoc.action',
												params : {
													docid : rec.data.conDocId
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
							Ext.Msg.alert('提示', '请选择您要删除的付款附件！');
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
								+ balanceId
								+ "&conDocId="
								+ val
								+ "&type=CONPAY');\"/>查看附件</a>"
					} else {
						return "";
					}
				}

			}]);
	annex_item_cm.defaultSortable = true;
	var annex_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'managecontract/findBalDocList.action'
						}),
				reader : new Ext.data.JsonReader({
						// root : 'data'
						}, annex_item)
			});

	// annex_ds.load({
	// params : {
	// balanceId : balanceId,
	// type : "CONPAY"
	// }
	// });
	var annexGrid = new Ext.grid.GridPanel({
				ds : annex_ds,
				cm : annex_item_cm,
				sm : annex_sm,
				tbar : annextbar,
				split : true,
				height : 100,
				// width : 450,
				autoScroll : true,
				border : false,
				viewConfig : {
					forceFit : false
				}
			});
	annexGrid.on('rowdblclick', function(grid, rowIndex, e) {
				var record = annexGrid.getSelectionModel().getSelected();
				docmethod = 'update';
				docform.getForm().reset();
			});
	var conSet = new Ext.form.FormPanel({
				bodyStyle : "padding:25px 5px 0",
				labelAlign : 'left',
				autoHeight : true,
				// title : '合同基本信息',
				border : false,
				items : [
						// new Ext.form.FieldSet({
						// title : '合同基本信息',
						// collapsible : true,
						// height : '100%',
						// layout : 'form',
						//			
						// items : [
						{
					border : false,
					layout : 'column',
					items : [{
								border : false,
								layout : 'form',
								columnWidth : 1,
								labelWidth : 110,
								height : 40,
								items : [{
											id : 'conttreesNo',
											name : 'con.conttreesNo',
											xtype : 'textfield',
											fieldLabel : '合同编号',
											readOnly : true,
											allowBlank : true,
											anchor : '92.5%'
										}]
							}, {
								border : false,
								layout : 'form',
								columnWidth : 1,
								labelWidth : 110,
								height : 40,
								items : [{
											id : 'contractName',
											name : 'con.contractName',
											xtype : 'textfield',
											fieldLabel : '合同名称',
											readOnly : true,
											allowBlank : false,
											anchor : '92.5%'
										}]
							},
							// {
							// columnWidth : 1,
							// layout : 'form',
							// labelWidth : 110,
							// height : 40,
							// border : false,
							// items : [cliendBox]
							// },
							{
								border : false,
								layout : 'form',
								columnWidth : 1,
								labelWidth : 110,
								height : 40,
								items : [{
											id : 'bankAccount',
											name : 'con.bankAccount',
											xtype : 'textfield',
											fieldLabel : '开户银行',
											readOnly : true,
											allowBlank : true,
											anchor : '92.5%'
										}]
							}, {
								border : false,
								layout : 'form',
								columnWidth : 1,
								labelWidth : 110,
								height : 40,
								items : [{
											id : 'account',
											name : 'con.account',
											xtype : 'numberfield',
											fieldLabel : '银行帐号',
											readOnly : true,
											allowBlank : true,
											anchor : '92.5%'
										}]
							},
							// {
							// columnWidth : 1,
							// layout : 'form',
							// labelWidth : 110,
							// border : false,
							// height : 40,
							// items : [itemBox]
							// },
							{
								columnWidth : 1,
								layout : 'form',
								labelWidth : 110,
								height : 40,
								border : false,
								items : [{
											id : 'actAmount',
											name : 'con.actAccount',
											xtype : 'numberfield',
											fieldLabel : '合同总额',
											readOnly : true,
											allowBlank : false,
											anchor : '92.5%'
										}]
							}, {
								columnWidth : 1,
								layout : 'form',
								labelWidth : 110,
								height : 40,
								border : false,
								items : [{
											id : 'appliedAmount',
											name : 'con.appliedAccount',
											xtype : 'numberfield',
											fieldLabel : '累计申请付款',
											readOnly : true,
											allowBlank : true,
											anchor : '92.5%'
										}]
							}, {
								columnWidth : 1,
								layout : 'form',
								labelWidth : 110,
								height : 40,
								border : false,
								items : [{
											id : 'approvedAmount',
											name : 'con.approvedAccount',
											xtype : 'numberfield',
											fieldLabel : '累计批准付款',
											readOnly : true,
											allowBlank : true,
											anchor : '92.5%'
										}]
							}
					// , {
					// columnWidth : 1,
					// layout : 'form',
					// labelWidth : 110,
					// height : 40,
					// border : false,
					// items : [{
					// id : 'payedAmount',
					// name : 'con.payedAccount',
					// xtype : 'numberfield',
					// fieldLabel : '累计财务付款',
					// readOnly : true,
					// allowBlank : true,
					// anchor : '85%'
					// }]
					// }
					]
				}
				// ]
				// })
				]
			});
	var layout = new Ext.Viewport({
				layout : "border",
				border : false,
				// autoScroll : true,
				items : [{
							region : 'center',
							layout : 'border',
							border : true,
							items : [{
										region : 'center',
										layout : 'form',
										border : false,
										height : 270,
										items : [balform]
									}, {
										region : 'south',
										layout : 'fit',
										border : false,
										autoScroll : true,
										// autoHeight : true,
										height : 240,
										split : true,
//										hidden : true,
										collapsible : false,
										items : [annexGrid]
									}]
						}, {
							region : "east",
							collapsible : true,
							title : '合同基本信息',
							layout : 'fit',
							border : true,
							margins : '0 0 0 0',
							width : 300,
							split : true,
							collapsible : true,
							items : [conSet]
						}]
			});

			
			
		var docFile = {
		id : "docFile",
		xtype : "fileuploadfield",
		// inputType : "file",
		fieldLabel : '选择附件',
		// allowBlank : false,
		anchor : "95%",
		height : 22,
		name : 'docFile',
		buttonCfg : {
			text : '浏览...',
			iconCls : 'upload-icon'
		},
			listeners : {
			'fileselected' : function() {
				var url = Ext.get('docFile').dom.value;
				var format = url.substring(url.lastIndexOf("\\") + 1,
						url.length - 4);
				Ext.get('docName').dom.value = format;
			}
		}

	}
	
//	var docFile = {
//		id : "docFile",
//		xtype : "textfield",
//		inputType : "file",
//		fieldLabel : '选择1附件',
//		allowBlank : false,
//		anchor : "95%",
//		height : 22,
//		// readOnly : true,
//		name : 'docFile',
//		listeners : {
//			'change' : function() {
//				alert()
//				var url = Ext.get('oriFile').dom.value;
//				alert()
//				var format = url.substring(url.lastIndexOf("\\") + 1,
//						url.length - 4);
//				Ext.get('docName').dom.value = format;
//			}
//		}
//	}

	var lastModifiedName = new Ext.form.TextField({
				id : 'lastModifiedName',
				name : 'lastModifiedName',
				// xtype : 'textfield',
				fieldLabel : '上传人',
				readOnly : true,
				anchor : '95%'
			});

	var doccontent = new Ext.form.FieldSet({
				title : '付款附件',
				height : '100%',
				layout : 'form',
				items : [{
					id : 'conDocId',
					name : 'doc.conDocId',
					// xtype : 'numberfield',
					// fieldLabel : '序号',
					readOnly : false,
					hidden : true
						// ,
						// hideLabel : true,
						// anchor : '95%'
					}, {
					id : 'keyId',
					name : 'doc.keyId',
					// xtype : 'numberfield',
					// fieldLabel : '序号',
					readOnly : false,
					hidden : true
						// ,
						// hideLabel : true,
						// anchor : '95%'
					}, docFile, {
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
				}, lastModifiedDate, lastModifiedName, {
					id : 'docType',
					name : 'doc.docType',
					xtype : 'textfield',
					fieldLabel : '类型',
					readOnly : false,
					hidden : true,
					hideLabel : true,
					anchor : '95%'
				}],
				buttons : [{
					text : '保存',
					iconCls : 'save',
					handler : function() {
						var url = '';
						if (docmethod == "add") {
							url = 'managecontract/addBalDoc.action';

						} else {
							url = 'managecontract/updateBalDoc.action';
						}
						if (!docform.getForm().isValid()) {
							return false;
						}
						docform.getForm().submit({
							url : url,
							method : 'post',
							params : {
								filePath : Ext.get('docFile').dom.value,
								balanceId : balanceId,
								type : "CONPAY"
							},
							success : function(form, action) {
								var message = eval('('
										+ action.response.responseText + ')');
								Ext.Msg.alert("成功", message.data);
								annex_ds.load({
											params : {
												balanceId : balanceId,
												type : "CONPAY"
											}
										});
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
	var docform = new Ext.form.FormPanel({
				bodyStyle : "padding:0px 0px 0",
				labelAlign : 'right',
				id : 'shift-form',
//				hidden : true,
				labelWidth : 80,
				autoHeight : true,
				fileUpload : true,
				region : 'center',
				border : false,
				items : [doccontent]
			});
	if (contractId != "") {
		Ext.Ajax.request({
					url : 'managecontract/findConFullInfo.action',
					params : {
						conId : contractId
					},
					method : 'post',
					waitMsg : '正在加载数据...',
					success : function(result, request) {
						var o = eval('(' + result.responseText + ')');
						conSet.getForm().loadRecord(o);
					},
					failure : function(result, request) {
						Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
					}
				});
	}
	if (balanceId != "") {
		method = "update";
		Ext.Ajax.request({
					url : 'managecontract/findBalanceInfo.action',
					params : {
						balanceId : balanceId
					},
					method : 'post',
					waitMsg : '正在加载数据...',
					success : function(result, request) {
						var o = eval('(' + result.responseText + ')');
						entryId = o.data.workflowNo;
						balform.getForm().loadRecord(o);
						memo = o.data.memo;
						if (o.data.memo == "NO") {
							Ext.get("btnQualityReport").dom.disabled = true;
						}
						if (o.data.memo == "YES") {
							Ext.get("btnReport").dom.disabled = true;
						}
						annex_ds.load({
									params : {
										balanceId : balanceId,
										type : "CONPAY"
									}
								});
						if (currencyName != "undefined") {
							currencyType.setValue(currencyName);
						} else {
							currencyType.setValue("");
						}
						if (o.data.balaMethod == 3) {
							Ext.get("chequeNo").dom.readOnly = false;
						} else {
							Ext.get("chequeNo").dom.readOnly = true;
						}
						if (balanceby != "undefined" && balanceby != "null") {
							balanceBy.setValue(balanceby);
						} else {
							balanceBy.setValue("");
						}
//						if (balanceName != "undefined" && balanceName != "null") {
//							Ext.get('balanceName').dom.value = balanceName;
//						} else {
//							Ext.get('balanceName').dom.value = ""
//						}
						hidoperateBox.setValue((o.data.operateBy == null)
								? ""
								: o.data.operateBy);
						operateBox.setValue((o.data.operateName == null)
								? ""
								: o.data.operateName);
						if (o.data.balaFlag == "1" || o.data.balaFlag == "2") {
							balaMethod.on('beforequery', function() {
										return false
									});
							balanceByName.disable();
							Ext.get("but").dom.disabled = true;
						} else {
							Ext.get("but").dom.disabled = false;
						}
						if (Ext.get("but").dom.disabled) {
							btnSetDisabled()
						}
					},
					failure : function(result, request) {
						Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
					}
				});
	};
	function btnSetDisabled() {
		if (balaFlag == "0" || balaFlag == "" || balaFlag == 3) {
			return
		} else {
			Ext.get("but").dom.disabled = true;
			Ext.get("btnSave").dom.disabled = true;
			Ext.get("btnReport").dom.disabled = true;
			Ext.get("btnQualityReport").dom.disabled = true; // add by fyyang
																// 090729保证金上报
			Ext.get("btnAnnexAdd").dom.disabled = true;
			// Ext.get("btnAnnexSave").dom.disabled = true;
			Ext.get("btnAnnexDelete").dom.disabled = true;
			Ext.get("btnDelete").dom.disabled = true;
		}
	};
	function btnFreeDisabled() {
		Ext.get("but").dom.disabled = false;
		Ext.get("btnSave").dom.disabled = false;
		Ext.get("btnReport").dom.disabled = false;
		Ext.get("btnQualityReport").dom.disabled = false; // add by fyyang
															// 090729保证金上报
		Ext.get("btnAnnexAdd").dom.disabled = false;
		// Ext.get("btnAnnexSave").dom.disabled = false;
		Ext.get("btnAnnexDelete").dom.disabled = false;
		Ext.get("btnDelete").dom.disabled = false;
	}
	var docwin = new Ext.Window({
				title : '新增',
				el : 'win',
				modal : true,
				autoHeight : true,
				width : 450,
//				height : 450,
				closeAction : 'hide',
				items : [docform]
			})
	getWorkCode();
})