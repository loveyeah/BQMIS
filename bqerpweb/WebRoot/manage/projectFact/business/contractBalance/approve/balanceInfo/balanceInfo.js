Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	// 币别 modify by drdu 2009/05/05
	var typeStore = new Ext.data.JsonStore({
				url : 'managecontract/getConCurrencyList.action',
				root : 'list',
				fields : ['currencyId', 'currencyName']
			})
	typeStore.load();
	
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
	var operateBy;
	var operateDepCode;
	var contractId = getParameter("conId");
	var currencyName = getParameter("currencyName");
	var balanceId = getParameter("balanceId");
	var method = "add";
	var entryId;
	var status;
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

	var operateBox = new Ext.form.ComboBox({
				fieldLabel : '经办人',
				id : 'operateBy',
				name : 'operateBy',
				// // store : unitStore,
				// store : [['999999', '管理员'], ['1198', '徐常胜']],
				// valueField : "empCode",
				// // displayField : "chsName",
				// valueField : "value",
				// displayField : "text",
				// mode : 'local',
				// triggerAction : 'all',
				// forceSelection : true,
				hiddenName : 'bal.operateBy',
				readOnly : true,
				anchor : "100%"
			});
	operateBox.on("beforequery", function() {
				return false;
			});
	// 币别
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
				emptyText : '请选择',
				anchor : '100%'
			});
		typeBox.on("beforequery",function(){
		return false;
		})	
	var applicatDate = new Ext.form.TextField({
				id : 'applicatDate',
				fieldLabel : "申请日期",
				readOnly : true,
				name : 'bal.applicatDate',
				style : 'cursor:pointer',
				anchor : "100%"
			});
	var passDate = new Ext.form.TextField({
		id : 'passDate',
		fieldLabel : "批准日期",
		name : 'bal.passDate',
		style : 'cursor:pointer',
		readOnly : true,
		anchor : "85%",
		value : getDate()
			// listeners : {
			// focus : function() {
			// WdatePicker({
			// startDate : '%y-%M-%d 00:00:00',
			// dateFmt : 'yyyy-MM-dd HH:mm:ss',
			// alwaysUseStartDate : true
			// });
			// }
			// }
		});
	var balaDate = new Ext.form.TextField({
		id : 'balaDate',
		fieldLabel : "财务付款日期",
		name : 'bal.balaDate',
		style : 'cursor:pointer',
		readOnly : true,
		anchor : "85%",
		value : getDate()
			// listeners : {
			// focus : function() {
			// WdatePicker({
			// startDate : '%y-%M-%d 00:00:00',
			// dateFmt : 'yyyy-MM-dd HH:mm:ss',
			// alwaysUseStartDate : true
			// });
			// }
			// }
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
				readOnly : true,
				allowBlank : false,
				emptyText : '请选择',
				anchor : '100%'
			});
	balaMethod.on("beforequery", function() {
				return false;
			});
	var btnSign = new Ext.Toolbar.Button({
		id : 'btnSign',
		// style : "margin-left:200px",
		text : "签字",
		iconCls : 'write',
		menuAlign : 'center',
		handler : function() {
			if (balanceId == null || balanceId == "") {
				Ext.Msg.alert('提示', '请选择一条结算记录!');
				return false;
			}
			if (entryId == null || entryId == "" || entryId == undefined) {
				Ext.Msg.alert('提示', '流程还未启动');
			} else if (status == "2") {
				Ext.Msg.alert('提示', '审批流程已结束');
			} else {
				var url = "/power/manage/projectFact/business/contractBalance/approve/balanceInfo/sign.jsp?id="
						+ balanceId + "&entryId=" + entryId;
				var o = window.showModalDialog(url, '',
						'dialogWidth=700px;dialogHeight=500px;status=no');
				if (o) {
					balform.getForm().reset();
					contractId = "";
					balanceId = "";
					entryId = "";
					parent.parent.Ext.getCmp("maintab").setActiveTab(0);
					var url = "manage/projectFact/business/contractBalance/approve/balanceList/list.jsp";
					parent.parent.document.all.iframe1.src = url;
				}
			}
		}
	});
	// 票面浏览
	function CheckRptPreview() {
		if (balanceId == null || balanceId == "") {
			Ext.Msg.alert('提示', '请选择一条结算记录!');
		} else {
			var url = "/powerrpt/report/webfile/bqmis/GCContractBalance.jsp?balanceId="
					+ balanceId;
			window.open(url);
		}

	};

	var formtbar = new Ext.Toolbar({
		id : 'formtbar',
		items : [{
					disable : true
				}, btnSign, '-', {
					id : 'btnQuery',
					text : "会签查询",
					iconCls : 'view',
					handler : function() {
						// add by drdu 09/04/27
						if (balanceId == null || balanceId == "") {
							Ext.Msg.alert('提示', '请选择一条结算记录!');
						} else {
							var url = "/power/workflow/manager/show/show.jsp?entryId="
									+ entryId;
							window.open(url);
						}
					}
				}, '-', {
					id : 'btnApply',
					text : "结算审批表",
					iconCls : 'pdfview',
					handler : function() {
						CheckRptPreview();
					}
				}]
	});

	var balform = new Ext.form.FormPanel({
				bodyStyle : "padding:0 2px 0",
				labelAlign : 'right',
				// collapsible : true,
				// title : '本次结算申请',
				autoHeight : true,
				region : 'center',
				border : false,
				tbar : formtbar,
				items : [
						// new Ext.form.FieldSet({
						// title : '本次结算申请',
						// //collapsible : true,
						// height : '100%',
						// layout : 'form',
						// items : [
						{
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
											readOnly : true,
											height : 80,
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
											readOnly : true,
											height : 80,
											anchor : '99%'
										}]
							}, {
								columnWidth : .5,
								layout : 'form',
								labelWidth : 110,
								border : false,
								items : [{
											id : 'applicatPrice',
											name : 'bal.applicatPrice',
											xtype : 'numberfield',
											fieldLabel : '申请付款',
											readOnly : true,
											anchor : '100%'
										}]
							}, {
								border : false,
								layout : 'form',
								columnWidth : .5,
								labelWidth : 110,
								items : [{
											id : 'paymentId',
											name : 'bal.paymentId',
											xtype : 'hidden',
											readOnly : false,
											allowBlank : true,
											anchor : '92.5%'
										}]
							}, {
								border : false,
								layout : 'form',
								columnWidth : .5,
								labelWidth : 110,
								items : [operateBox]
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
							}, {
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
								columnWidth : .5,
								layout : 'form',
								labelWidth : 110,
								border : false,
								readOnly : true,
								hidden : true,
								items : [passDate]
							}
							// ,{
							// columnWidth : .5,
							// layout : 'form',
							// labelWidth : 110,
							// border : false,
							// items : [{
							// id : 'balancePrice',
							// name : 'bal.balancePrice',
							// xtype : 'numberfield',
							// fieldLabel : '财务付款',
							// readOnly : false,
							// hidden : true,
							// allowBlank : true,
							// anchor : '85%'
							// }]
							// }
//							, {
//								border : false,
//								layout : 'form',
//								columnWidth : .5,
//								labelWidth : 110,
//								items : [{
//											id : 'balanceName',
//											name : 'bal.balanceName',
//											xtype : 'textfield',
//											fieldLabel : '财务部经办人',
//											readOnly : true,
//											allowBlank : true,
//											anchor : '99%'
//										}]
//							}
							, {
								border : false,
								layout : 'form',
								columnWidth : .5,
								readOnly : true,
								labelWidth : 110,
								hidden : true,
								items : [balaDate]
							}, {
								columnWidth : .5,
								layout : 'form',
								labelWidth : 110,
								border : false,
								items : [typeBox]
							}
//							, {
//								columnWidth : 1,
//								layout : 'form',
//								labelWidth : 110,
//								border : false,
//								items : [{
//											id : 'chequeNo',
//											name : 'bal.chequeNo',
//											xtype : 'numberfield',
//											fieldLabel : '支票号',
//											readOnly : true,
//											allowBlank : true,
//											anchor : '99%'
//										}]
//							}
							]
				}
				// ]
				// })
				]
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
	var annexGrid = new Ext.grid.GridPanel({
		ds : annex_ds,
		cm : annex_item_cm,
		sm : annex_sm,
		autoWidth : true,
		split : true,
		height : 100,
		width : 450,
		autoScroll : true,
		style : "padding-top:10;padding-left:10px;padding-bottom:2px;border-width:1 0 0 0;",
		border : false,
		viewConfig : {
			forceFit : false
		}
	});

	var conSet = new Ext.form.FormPanel({
				bodyStyle : "padding:25px 5px 0",
				labelAlign : 'left',
				autoHeight : true,
				border : false,
				items : [{
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
									}
									// , {
									// columnWidth : 1,
									// layout : 'form',
									// labelWidth : 110,
									// height : 40,
									// border : false,
									// items : [cliendBox]
									// }
									, {
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
							// hidden : true,
							// readOnly : true,
							// allowBlank : true,
							// anchor : '92.5%'
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
		Ext.Ajax.request({
					url : 'managecontract/findBalanceInfo.action',
					params : {
						balanceId : balanceId
					},
					method : 'post',
					waitMsg : '正在加载数据...',
					success : function(result, request) {
						var o = eval('(' + result.responseText + ')');
						balform.getForm().loadRecord(o);
						entryId = o.data.workflowNo
						status = o.data.workflowStatus;
						operateBox.setValue((o.data.operateName == null)
								? ""
								: o.data.operateName);
//						 Ext.get("currencyType").dom.value = currencyName;
						// null ?"":o.data.balanceName;
						annex_ds.load({
									params : {
										balanceId : balanceId,
										type : "CONPAY"
									}
								});
						if (status == 1) {
							btnSign.setDisabled(false);
						}
					},
					failure : function(result, request) {
						Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
					}
				});
	};
})