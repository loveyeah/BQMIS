// 画面：物资出门登记
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {
	// 主画面

	var strMethod;
	var strMsg;
	// 物资单位
	var storeUnit = new Ext.data.JsonStore({
				url : 'administration/getUnit.action',
				root : 'list',
				fields : [{
							name : 'strUnitName'
						}, {
							name : 'strUnitID'
						}]
			})
	storeUnit.load();
	// 物资单位下拉框
	var drpUnitHidden2 = new Ext.form.ComboBox({
				fieldLabel : "物资单位",
				id : "unit2",
				triggerAction : 'all',
				store : storeUnit,
				displayField : 'strUnitName',
				valueField : 'strUnitID',
				mode : 'local',
				readOnly : true,
				hidden : true,
				name : 'unit2',
				anchor : '100%'
			})
	// 新增按钮
	var btnAdd = new Ext.Button({
				id : 'add',
				text : Constants.BTN_ADD,
				iconCls : Constants.CLS_ADD,
				handler : addRecord
			});
	// 修改按钮
	var btnUpdate = new Ext.Button({
				id : 'update',
				text : Constants.BTN_UPDATE,
				iconCls : Constants.CLS_UPDATE,
				handler : updateRecord
			});
	// 删除按钮
	var btnDelete = new Ext.Button({
				id : 'delete',
				text : Constants.BTN_DELETE,
				iconCls : Constants.CLS_DELETE,
				handler : deleteRecord
			});
	// 上报按钮
	var btnSend = new Ext.Button({
				id : 'send',
				text : Constants.BTN_REPOET,
				iconCls : Constants.CLS_REPOET,
				handler : sendRecord
			});
	// grid中的数据
	var recordInfoGridList = new Ext.data.Record.create([{
				name : 'agent'
			}, {
				name : 'firm'
			}, {
				name : 'outDate'
			}, {
				name : 'wpName'
			}, {
				name : 'standard'
			}, {
				name : 'unit'
			}, {
				name : 'num'
			}, {
				name : 'note'
			}, {
				name : 'reason'
			}, {
				name : 'updateTime'
			}, {
				name : 'id'
			}]);

	// grid中的store
	var storeInfoGrid = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'administration/getInfoList.action'
						}),
				reader : new Ext.data.JsonReader({
							root : 'list',
							totalProperty : 'totalCount'
						}, recordInfoGridList)
			});
	// 初始化时,显示所有数据
	storeInfoGrid.load({
				params : {
					start : 0,
					limit : Constants.PAGE_SIZE
				}
			});

	// grid中的列
	var cm = new Ext.grid.ColumnModel([
			// 自动生成行号
			new Ext.grid.RowNumberer({
						header : '行号',
						width : 35
					}), {
				header : '经办人',
				width : 100,
				align : 'left',
				sortable : true,
				dataIndex : 'agent'
			}, {
				header : '经办人所在单位',
				width : 180,
				align : 'left',
				sortable : true,
				dataIndex : 'firm'
			}, {
				header : '出门时间',
				width : 180,
				align : 'left',
				sortable : true,
				renderer : renderDate,
				dataIndex : 'outDate'
			}, {				
				header : '物资名称',
				width : 180,
				align : 'left',
				sortable : true,
				dataIndex : 'wpName'
			}, {
				hidden : true,
				header : '规格',
				dataIndex : 'standard'
			}, {
				hidden : true,
				header : '单位',
				dataIndex : 'unit'
			}, {				
				header : '数量',
				width : 80,
				align : 'right',
				sortable : true,
				dataIndex : 'num',
				renderer: format
			}, {
				hidden : true,
				header : '备注',
				dataIndex : 'note'
			}, {
				hidden : true,
				header : '物资出入原因',
				dataIndex : 'reason'
			}, {
				hidden : true,
				header : '更新时间',
				dataIndex : 'updateTime'
			}, {
				hidden : true,
				header : '序号',
				dataIndex : 'id'
			}])
	// 定义选择列
	var sm = new Ext.grid.RowSelectionModel({
				singleSelect : true
			});

	// 运行执行的Grid主体
	var gridInfo = new Ext.grid.GridPanel({
				store : storeInfoGrid,
				cm : cm,
				viewConfig : {
					forceFit : false
				},
				tbar : [btnAdd, btnUpdate, btnDelete, btnSend,drpUnitHidden2],
				// 分页
				bbar : new Ext.PagingToolbar({
							pageSize : Constants.PAGE_SIZE,
							store : storeInfoGrid,
							displayInfo : true,
							displayMsg : MessageConstants.DISPLAY_MSG,
							emptyMsg : MessageConstants.EMPTY_MSG
						}),
				sm : sm,
				frame : false,
				border : false,
				enableColumnHide : false,
				enableColumnMove : false
			});

	// 注册双击事件
	gridInfo.on("rowdblclick", updateRecord);

	// 设定布局器及面板
	new Ext.Viewport({
				enableTabScroll : true,
				layout : "fit",
				items : [gridInfo]
			});

	// 弹出窗口

	// 定义form中的经办人
	var txtAgent = new Ext.form.TextField({
				id : 'agent',
				fieldLabel : "经办人<font color='red'>*</font>",
				isFormField : true,
				allowBlank : false,
				maxLength : 10,
				anchor : '100%',
				name : 'adJOutreg.agent'
			});
	// 定义form中的物资规格
	var txtStandard = new Ext.form.TextField({
				id : 'standard',
				fieldLabel : '物资规格',
				isFormField : true,
				maxLength : 12,
				anchor : '100%',
				name : 'adJOutreg.standard'
			});
	// 第一行
	var firstLine = new Ext.Panel({
		border : false,
		layout : 'column',
		style : "padding-top:5px;padding-right:5px;padding-bottom:0px;margin-bottom:0px",
		anchor : '100%',
		items : [{
					columnWidth : 0.5,
					layout : "form",
					border : false,
					items : [txtAgent]
				}, {
					columnWidth : 0.5,
					layout : "form",
					border : false,
					items : [txtStandard]
				}]
	});
	// 定义form中的经办人所在单位
	var txtFrim = new Ext.form.TextField({
				id : 'firm',
				fieldLabel : "经办人所在单位",
				allowBlank : true,
				maxLength : 20,
				anchor : '100%',
				name : 'adJOutreg.firm'
			});
	// 物资单位
	var storeUnit = new Ext.data.JsonStore({
				url : 'administration/getUnit.action',
				root : 'list',
				fields : [{
							name : 'strUnitName'
						}, {
							name : 'strUnitID'
						}]
			})
	storeUnit.load();
	// 物资单位下拉框
	var drpUnit = new Ext.form.ComboBox({
				fieldLabel : "物资单位",
				id : "unit",
				triggerAction : 'all',
				store : storeUnit,
				displayField : 'strUnitName',
				valueField : 'strUnitID',
				mode : 'local',
				readOnly : true,
				name : 'unit',
				anchor : '100%'
			})
	// 隐藏物资单位
	var hiddenUnit = new Ext.form.Hidden({
				id : 'hiddenUnit',
				name : 'adJOutreg.unit'
			});
	// 第二行
	var secondLine = new Ext.Panel({
		border : false,
		layout : 'column',
		style : "padding-top:5px;padding-right:5px;padding-bottom:0px;margin-bottom:0px",
		anchor : '100%',
		items : [{
					columnWidth : 0.5,
					layout : 'form',
					border : false,
					items : [txtFrim]
				}, {
					columnWidth : 0.5,
					layout : 'form',
					border : false,
					items : [drpUnit,hiddenUnit]
				}]
	});
	// 定义form中的物资出门时间
	var dteInit = getCurrentDate();
	var txtOutDate = new Ext.form.TextField({
				id : 'outDate',
				fieldLabel : "出门时间<font color='red'>*</font>",
				name : 'adJOutreg.outDate',
				style : 'cursor:pointer',
				anchor : '100%',
				readOnly : true,
				renderer : renderDate,
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d 00:00',
									dateFmt : 'yyyy-MM-dd HH:mm'
								});
					}
				}
			})
	txtOutDate.setValue(dteInit);
	// 定义form中的物资数量
	var txtNum = new Powererp.form.NumField({
				id : 'num',
				fieldLabel : '物资数量',
				isFormField : true,
				labelAlign : 'right',
				// 精度
				decimalPrecision : 2,
				minValue : .00,
				maxValue : 99999999999.99,
				maxLength : 17,
				baseChars : '0123456789.',
				allowNegative : false,
				style : 'text-align:right',
				anchor : '100%',
				padding : 2,
				name : 'num'
			});
	// 物资数量隐藏
	var hdtxtNum = {
		id : "hdnum",
		name : "adJOutreg.num",
		xtype : 'hidden',
		readOnly : true,
		hidden : true,
		value : '0'
	}
	// 第三行
	var thirdLine = new Ext.Panel({
		border : false,
		layout : 'column',
		style : "padding-top:5px;padding-right:5px;padding-bottom:0px;margin-bottom:0px",
		anchor : '100%',
		items : [{
					columnWidth : 0.5,
					layout : 'form',
					border : false,
					items : [txtOutDate]
				}, {
					columnWidth : 0.5,
					layout : 'form',
					border : false,
					items : [txtNum]
				}]
	});
	// 定义form中的物资名称
	var txtName = new Ext.form.TextField({
				id : 'wpName',
				fieldLabel : "物资名称<font color='red'>*</font>",
				isFormField : true,
				allowBlank : false,
				maxLength : 25,
				anchor : '100%',
				name : 'adJOutreg.wpName'
			});
	// 第四行
	var forthLine = new Ext.Panel({
		border : false,
		layout : 'column',
		style : "padding-top:5px;padding-right:5px;padding-bottom:0px;margin-bottom:0px",
		anchor : '100%',
		items : [{
					columnWidth : 1,
					layout : 'form',
					border : false,
					items : [txtName]
				}]
	});
	// 定义form中的备注
	var txtNote = new Ext.form.TextArea({
				id : 'note',
				fieldLabel : '备注',
				maxLength : 500,
				anchor : '99%',
				name : 'adJOutreg.note'
			});
	// 第五行
	var fiveLine = new Ext.Panel({
		border : false,
		layout : 'column',
		style : "padding-top:5px;padding-right:4px;padding-bottom:0px;margin-bottom:0px",
		anchor : '100%',
		items : [{
					columnWidth : 1,
					layout : 'form',
					border : false,
					items : [txtNote]
				}]
	});
	// 定义form中的物资出入原因
	var txtReason = new Ext.form.TextArea({
				id : 'reason',
				fieldLabel : '物资出入原因',
				maxLength : 50,
				anchor : '99%',
				name : 'adJOutreg.reason'
			});
	// 第六行
	var sixLine = new Ext.Panel({
		border : false,
		layout : 'column',
		style : "padding-top:5px;padding-right:4px;padding-bottom:0px;margin-bottom:0px",
		anchor : '100%',
		items : [{
					columnWidth : 1,
					layout : 'form',
					border : false,
					items : [txtReason]
				}]
	});
	// 隐藏的上次修改时间
	var hiddenlastModifiedDate = {
		id : 'updateTime',
		xtype : 'hidden',
		readOnly : true,
		hidden : true,
		value : '0',
		name : 'updateTime'
	};
	// 隐藏序号
	var hiddenId = {
		id : 'id',
		xtype : 'hidden',
		readOnly : true,
		hidden : true,
		value : '0',
		name : 'adJOutreg.id'
	};
	// 第六行
	var sevenLine = new Ext.Panel({
		border : false,
		// height : 30,
		layout : "column",
		style : "padding-top:5px;padding-right:5px;padding-bottom:0px;margin-bottom:0px",
		anchor : '100%',
		items : [{
					columnWidth : 1,
					layout : "form",
					border : false,
					items : [hiddenlastModifiedDate, hiddenId, hdtxtNum]
				}]
	});
	// 定义弹出窗体中的form
	var myPanel = new Ext.FormPanel({
				labelAlign : 'right',
				autoHeight : true,
				frame : true,
				items : [firstLine, secondLine, thirdLine, forthLine, fiveLine,
						sixLine, sevenLine]
			});

	// 定义弹出窗体
	var win = new Ext.Window({
				width : 500,
				autoHeight : true,
				buttonAlign : 'center',
				items : [myPanel],
				layout : 'fit',
				closeAction : 'hide',
				modal : true,
				resizable : false,
				buttons : [{
							text : Constants.BTN_SAVE,
							iconCls : Constants.CLS_SAVE,
							handler : confirmRecord

						}, {
							text : Constants.BTN_CANCEL,
							iconCls : Constants.CLS_CANCEL,
							handler : function() {
								Ext.Msg.confirm(Constants.BTN_CONFIRM,
										MessageConstants.COM_C_005, function(
												buttonobj) {
											if (buttonobj == 'yes') {
												win.hide();
											} else {
												return
											}
										})

							}
						}]
			});

	// 处理
	/**
	 * 增加函数
	 */
	function addRecord() {
		strMethod = "add";
		myPanel.getForm().reset();
		win.setTitle("新增物资出门登记");
		win.show();
	}
	/**
	 * 上报函数
	 */
	function sendRecord() {
		var rec = gridInfo.getSelectionModel().getSelected();
		if (!rec) {
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
					MessageConstants.COM_I_001);
			return false;
		} else {
			var id = rec.get('id');
			var updateTime = rec.get('updateTime');
			Ext.Msg.confirm(Constants.BTN_CONFIRM, MessageConstants.COM_C_006,
					function(buttonobj) {
						if (buttonobj == 'yes') {
							// 上报操作（共通）
							// TODO
							myurl = 'administration/sendData.action';
							Ext.Ajax.request({
								method : Constants.POST,
								url : myurl,
								params : {									
									id : id,
									updateTime : updateTime
								},
								success : function(result, action) {
									var result = eval('('
											+ result.responseText
											+ ')');
									// 排他								
									if (result.msg == Constants.DATA_USING) {
										Ext.Msg.alert(Constants.ERROR,
												MessageConstants.COM_I_002);
										return;
									}
									// SQL异常
									if (result.msg == Constants.SQL_FAILURE) {
										Ext.Msg.alert(Constants.ERROR,
												MessageConstants.COM_E_014);
										return;
									}
									// 显示成功信息
									Ext.Msg.alert(
											MessageConstants.SYS_REMIND_MSG,
											MessageConstants.COM_I_007,
											function() {
												// 重新载入
												storeInfoGrid.load({
													params : {
														start : 0,
														limit : Constants.PAGE_SIZE
													}
												});
												// 页面初始化
												gridInfo.getView().refresh();
											});
								},
								failure : function() {
								}
							});
							return;
						}
					})
		}

	}
	/**
	 * 保存button压下的操作
	 */
	function confirmRecord() {
		// check是否为空
		if (!checkform()) {
			Ext.Msg.alert(Constants.ERROR, strMsg);
			return;
		}	
        hiddenUnit.setValue(drpUnit.getValue());
		Ext.Msg.confirm(Constants.BTN_CONFIRM, MessageConstants.COM_C_001,
				function(buttonobj) {
					if (buttonobj == 'yes') {
						// check 字段过长
						if (isNotLong()) {
							var myurl = "";
							// 增加的情况下
							if (strMethod == "add") {
								myurl = 'administration/addData.action';
								Ext.get('hdnum').dom.value = ((txtNum.value == null || txtNum.value == "")
										? 0
										: parseFloat(Ext.get('num').dom.value
												.replace(/,/g, '')));
								myPanel.getForm().submit({
									method : Constants.POST,
									url : myurl,
									params : {
										outDate : Ext.get('outDate').dom.value
									},
									success : function(form, action) {
										var result = eval('('
												+ action.response.responseText
												+ ')');
                                        // SQL异常
										if (result.msg == Constants.SQL_FAILURE) {
											Ext.Msg.alert(Constants.ERROR,
													MessageConstants.COM_E_014);
											return;
										}
										// 显示成功信息
										Ext.Msg
												.alert(
														MessageConstants.SYS_REMIND_MSG,
														MessageConstants.COM_I_004,
														function() {
															// 重新载入
															storeInfoGrid.load(
																	{
																		params : {
																			start : 0,
																			limit : Constants.PAGE_SIZE
																		}
																	});
															// 页面初始化
															gridInfo.getView()
																	.refresh();
															win.hide();
														});
									},
									failure : function() {										
									}
								});
								return;
							}
							// 修改时的情况
							if (strMethod == "update") {
								myurl = 'administration/updateData.action';
								Ext.get('hdnum').dom.value = ((Ext.get('num').dom.value == null || Ext.get('num').dom.value == "")
										? 0
										: parseFloat(Ext.get('num').dom.value
												.replace(/,/g, '')));								
								myPanel.getForm().submit({
									method : Constants.POST,
									url : myurl,
									params : {
										outDate : Ext.get('outDate').dom.value,
										updateTime : Ext.get('updateTime').dom.value
									},

									success : function(form, action) {
										var result = eval('('
												+ action.response.responseText
												+ ')');
										// 排他
										if (result.msg == Constants.DATA_USING) {
											Ext.Msg.alert(Constants.ERROR,
													MessageConstants.COM_I_002);
											return;
										}
										// SQL异常
										if (result.msg == Constants.SQL_FAILURE) {
											Ext.Msg.alert(Constants.ERROR,
													MessageConstants.COM_E_014);
											return;
										}
										// 显示成功信息
										Ext.Msg
												.alert(
														MessageConstants.SYS_REMIND_MSG,
														MessageConstants.COM_I_004,
														function() {
															// 重新载入
															storeInfoGrid.load(
																	{
																		params : {
																			start : 0,
																			limit : Constants.PAGE_SIZE
																		}
																	});
															// 页面初始化
															gridInfo.getView()
																	.refresh();
															win.hide();
														});
									},
									failure : function() {										
									}
								});

								return;
							}
						} else {
							return;
						}
					}
				});
	}

	/**
	 * 在增加数据前的非空检查
	 */
	function checkform() {
		strMsg = '';
		var agent = Ext.get('agent').dom.value;
		if (agent == "" || agent == null) {
			strMsg += String.format(MessageConstants.COM_E_002, '经办人')
					+ "<br/>";
			return false;
		}

		var outDate = Ext.get('outDate').dom.value;
		if (outDate == "" || outDate == null) {
			strMsg += String.format(MessageConstants.COM_E_002, '物资出门时间')
					+ "<br/>";
			return false;
		}

		var wpName = Ext.get('wpName').dom.value;
		if (wpName == "" || wpName == null) {
			strMsg += String.format(MessageConstants.COM_E_002, '物资名称')
					+ "<br/>";
			return false;
		}

		if (strMsg == "") {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 修改函数
	 */
	function updateRecord() {
		var rec = gridInfo.getSelectionModel().getSelected();
		if (!rec) {
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
					MessageConstants.COM_I_001);
			return false;
		} else {
			strMethod = "update";
			myPanel.getForm().reset();
			win.setTitle("修改物资出门登记");
			win.show();
			Ext.get('agent').dom.value = (rec.get('agent') == null
					? ""
					: rec.data.agent);
			Ext.get('standard').dom.value = (rec.get('standard') == null
					? ""
					: rec.data.standard);
			Ext.get('firm').dom.value = (rec.get('firm') == null
					? ""
					: rec.data.firm);
//			Ext.get('unit').dom.value = (rec.get('unit') == null
//					? ""
//					: rec.data.unit);
			Ext.get('outDate').dom.value = (rec.get('outDate') == null
					? ""
					: renderDate(rec.data.outDate));
			Ext.get('num').dom.value = (rec.get('num') == null
					? ""
					: divide2(divide(rec.data.num)));		    
			Ext.get('wpName').dom.value = (rec.get('wpName') == null
					? ""
					: rec.data.wpName);
			Ext.get('note').dom.value = (rec.get('note') == null
					? ""
					: rec.data.note);
			Ext.get('reason').dom.value = (rec.get('reason') == null
					? ""
					: rec.data.reason);
			Ext.get('updateTime').dom.value = (rec.get('updateTime') == null
					? ""
					: rec.data.updateTime);
			Ext.get('id').dom.value = rec.get('id');
			drpUnit.setValue(rec.get("unit"));			
			if (drpUnit.getValue() == Ext.get('unit').dom.value) {				
				drpUnit.setValue("");
				Ext.get('unit').dom.value = "";
			}
		}
	}
	/**
	 * 删除函数
	 */
	function deleteRecord() {
		var rec = gridInfo.getSelectionModel().getSelected();
		if (rec) {
			var id = rec.get('id');
			var updateTime = rec.get('updateTime');
			Ext.Msg.confirm(Constants.BTN_CONFIRM, MessageConstants.COM_C_002,
					function(buttonobj) {
						if (buttonobj == 'yes') {
							// 刪除
							Ext.Ajax.request({
								method : Constants.POST,
								url : 'administration/deleteData.action',
								params : {
									id : id,
									updateTime : updateTime
								},
								success : function(result, request) {
									var o = eval("(" + result.responseText
											+ ")");
									// 排他
									if (o.msg == 'U') {
										Ext.Msg.alert(Constants.ERROR,
												MessageConstants.COM_I_002);
									} else {
										// 显示成功信息
										Ext.Msg
												.alert(
														MessageConstants.SYS_REMIND_MSG,
														MessageConstants.COM_I_005,
														function() {
															// 重新载入数据
															storeInfoGrid.load(
																	{
																		params : {
																			start : 0,
																			limit : Constants.PAGE_SIZE
																		}
																	});
															// 画面初期化
															gridInfo.getView()
																	.refresh();
														});
									}
								},
								failure : function() {
									Ext.Msg.alert(Constants.ERROR,
											MessageConstants.COM_E_014);
								}
							});
						}
					});
		} else {
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
					MessageConstants.COM_I_001);
		}
	}
	/**
	 * 数字加逗号
	 */
	function divide(value) {
		if (value == null) {
			return "";
		}
		var svalue = value + "";
		var decimal = "";
		var negative = false;
		var tempV = "";
		// 如果有小数
		if (svalue.indexOf(".") > 0) {
			decimal = svalue.substring(svalue.indexOf("."), svalue.length);
		}
		// 如果是负数
		if (svalue.indexOf("-") >= 0) {
			negative = true;
			svalue = svalue.substring(1, svalue.length);
		}
		tempV = svalue.substring(0, svalue.length - decimal.length);
		svalue = "";
		while (Div(tempV, 1000) > 0) {
			var temp = Div(tempV, 1000);
			var oddment = tempV - temp * 1000;
			var soddment = "";
			tempV = Div(tempV, 1000);
			soddment += (0 == Div(oddment, 100)) ? "0" : Div(oddment, 100);
			oddment -= Div(oddment, 100) * 100;
			soddment += (0 == Div(oddment, 10)) ? "0" : Div(oddment, 10);
			oddment -= Div(oddment, 10) * 10;
			soddment += (0 == Div(oddment, 1)) ? "0" : Div(oddment, 1);
			oddment -= Div(oddment, 1) * 1;
			svalue = soddment + "," + svalue;
		}
		svalue = tempV + "," + svalue;
		svalue = svalue.substring(0, svalue.length - 1);
		svalue += decimal;
		if (true == negative) {
			svalue = "-" + svalue;
		}
		return svalue;
	}
	/**
	 * 整除
	 */
	function Div(exp1, exp2) {
		var n1 = Math.round(exp1); // 四舍五入
		var n2 = Math.round(exp2); // 四舍五入

		var rslt = n1 / n2; // 除

		if (rslt >= 0) {
			rslt = Math.floor(rslt); // 返回值为小于等于其数值参数的最大整数值。
		} else {
			rslt = Math.ceil(rslt); // 返回值为大于等于其数字参数的最小整数。
		}

		return rslt;
	}
	/**
	 * 获取当前时间
	 */
	function getCurrentDate() {
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
		s += (t > 9 ? "" : "0") + t;
		return s;
	}
	/**
	 * 去掉时间中T和秒
	 */
	function renderDate(value) {
		if (!value)
			return "";
		var reDate = /\d{4}\-\d{2}\-\d{2}/gi;
		var reTime = /\d{2}:\d{2}/gi;

		var strDate = value.match(reDate);
		var strTime = value.match(reTime);
		if (!strDate)
			return "";
		return strTime ? strDate + " " + strTime : strDate;
	}
	/**
	 * 两位数字处理
	 */
	function divide2(value) {
		if (value == null) {
			return "";
		}
		value = String(value);
		// 整数部分
		var whole = value;
		// 小数部分
		var sub = ".00";
		// 如果有小数
		if (value.indexOf(".") > 0) {
			whole = value.substring(0, value.indexOf("."));
			sub = value.substring(value.indexOf("."), value.length);
			sub = sub + "00";
			if (sub.length > 3) {
				sub = sub.substring(0, 3);
			}
		}
		var r = /(\d+)(\d{3})/;
		while (r.test(whole)) {
			whole = whole.replace(r, '$1' + ',' + '$2');
		}
		v = whole + sub;
		return v;
	}
	// 判断入力框是否过长
	function isNotLong() {
		if (check(Ext.get('agent').dom.value.RTrim()) > 10
				|| check(Ext.get('standard').dom.value.RTrim()) > 12
				|| check(Ext.get('firm').dom.value.RTrim()) > 20
				|| check(Ext.get('num').dom.value.RTrim()) > 17
				|| check(Ext.get('wpName').dom.value.RTrim()) > 25
				|| check(Ext.get('note').dom.value.RTrim()) > 500
				|| check(Ext.get('reason').dom.value.RTrim()) > 50) {
			return false;
		}
		return true;
	}
	// 去掉右空格
	String.prototype.RTrim = function() {
		return this.replace(/(\s*$)/g, "");
	};
	// check字段的长度return 字符串单字节的长度
	function check(str) {
		var reg = /[^\x00-\xff]/
		var s = 0;
		var ts;
		for (i = 0; i < str.length; i++) {
			ts = str.substring(i, i + 1);
			if (reg.test(ts)) {
				s = s + 2;
			} else {
				s = s + 1;
			}
		}
		return s;
	}
	// 渲染
	function format(v, argDecimal,r) {
		if (v) {			
			if (typeof argDecimal != 'number') {                
				argDecimal = 2;                
			}
			v = Number(v).toFixed(argDecimal);
			var t = '';
			v = String(v);
			while ((t = v.replace(/^(-?[0-9]+)([0-9]{3}.*)$/, '$1,$2')) !== v)
				v = t;
			drpUnitHidden2.setValue(r.data.unit);			
			if (r.data.unit == Ext.get('unit2').dom.value) {
				return v + '';
			} else {
				return v + Ext.get('unit2').dom.value;
			}
		} else
			return '';
	}

});