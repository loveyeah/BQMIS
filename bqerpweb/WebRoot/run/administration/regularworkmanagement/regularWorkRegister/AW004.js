// 画面：定期工作登记
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {
	// ↓↓********** 主画面*******↓↓//
	var strMethod;

	// 登记当天工作按钮
	var btnRregister = new Ext.Button({
				id : 'register',
				text : "登记当天工作",
				handler : function() {
					storeInfoGrid.load({
								params : {
									start : 0,
									limit : Constants.PAGE_SIZE,
									flag : 1
								}
							});
					// 初始化时,隐藏的store数据
					storeInfoGridHidden.load({
								params : {
									start : 0,
									limit : 10000,
									flag : 0
								}
							});
					storeInfoGrid.on("load", function() {
								// 初始化时,隐藏的store数据
								storeInfoGridHidden.load({
											params : {
												start : 0,
												limit : 10000,
												flag : 0
											}
										});
							});
					// 页面初始化
					gridInfo.getView().refresh();

				}
			});
	// 取消当天工作登记按钮
	var btnCancel = new Ext.Button({
				id : 'cancel',
				text : "取消当天工作登记",
				handler : cancelRecord
			});
	// 全部定期工作按钮
	var btnAll = new Ext.Button({
				id : 'all',
				text : "全部定期工作",
				handler : allRegularWork
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
	// grid中的数据
	var recordInfoGridList = new Ext.data.Record.create([{
				name : 'id'
			}, {
				name : 'workItemName'
			}, {
				name : 'subWorkTypeName'
			}, {
				name : 'workDate'
			}, {
				name : 'workWeek'
			}, {
				name : 'workExplain'
			}, {
				name : 'result'
			}, {
				name : 'mark'
			}, {
				name : 'operator'
			}, {
				name : 'memo'
			}, {
				name : 'updateTime'
			}]);

	// grid中的store
	var storeInfoGrid = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'administration/getRegisterList.action'
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
					limit : Constants.PAGE_SIZE,
					flag : 0
				}
			});
	// 隐藏的store
	var storeInfoGridHidden = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'administration/getRegisterList.action'
						}),
				reader : new Ext.data.JsonReader({
							root : 'list',
							totalProperty : 'totalCount'
						}, recordInfoGridList)
			});
	// 初始化时,隐藏的store数据
	storeInfoGridHidden.load({
				params : {
					start : 0,
					limit : 10000,
					flag : 0
				}
			});
	// 设置可用
	btnRregister.setDisabled(false);

	storeInfoGrid.on("load", function() {
				if (storeInfoGrid.getCount() > 0) {
					btnRregister.setDisabled(true);
					btnCancel.setDisabled(false);
					btnUpdate.setDisabled(false);
					btnDelete.setDisabled(false);
				} else {
					btnRregister.setDisabled(false);
					btnCancel.setDisabled(true);
					btnUpdate.setDisabled(true);
					btnDelete.setDisabled(true);
				}
			});

	// 运行执行的Grid主体
	var gridInfo = new Ext.grid.GridPanel({
				store : storeInfoGrid,
				columns : [
						// 自动生成行号
						new Ext.grid.RowNumberer({
									header : '行号',
									width : 35
								}), {
							header : '工作名称',
							width : 100,
							align : 'left',
							sortable : true,
							dataIndex : 'workItemName'
						}, {
							header : '类别',
							width : 100,
							align : 'left',
							sortable : true,
							dataIndex : 'subWorkTypeName'
						}, {
							header : '工作日期',
							width : 100,
							align : 'left',
							sortable : true,
							dataIndex : 'workDate'
						}, {
							header : '星期',
							width : 80,
							align : 'left',
							sortable : true,
							dataIndex : 'workWeek'
						}, {
							header : '具体工作内容',
							width : 140,
							align : 'left',
							sortable : true,
							dataIndex : 'workExplain'
						}, {
							header : '工作结果',
							width : 100,
							align : 'left',
							sortable : true,
							dataIndex : 'result'
						}, {
							header : '完成标志',
							width : 100,
							align : 'left',
							sortable : true,
							dataIndex : 'mark'
						}, {
							header : '操作人',
							width : 100,
							align : 'left',
							sortable : true,
							dataIndex : 'operator'
						}, {
							header : '备注',
							width : 140,
							align : 'left',
							sortable : true,
							dataIndex : 'memo'
						}, {
							hidden : true,
							header : '序号',
							align : 'left',
							sortable : true,
							dataIndex : 'id'
						}, {
							hidden : true,
							header : '更新时间',
							align : 'left',
							sortable : true,
							dataIndex : 'updateTime'
						}],
				viewConfig : {
					forceFit : false
				},
				tbar : [btnRregister, btnCancel, btnAll, btnUpdate, btnDelete],
				// 分页
				bbar : new Ext.PagingToolbar({
							pageSize : Constants.PAGE_SIZE,
							store : storeInfoGrid,
							displayInfo : true,
							displayMsg : MessageConstants.DISPLAY_MSG,
							emptyMsg : MessageConstants.EMPTY_MSG
						}),
				sm : new Ext.grid.RowSelectionModel({
							singleSelect : true
						}),
				frame : false,
				border : false,
				enableColumnHide : true,
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


	// ↑↑********** 主画面*******↑↑//

	// ↓↓*****弹出窗口*******↓↓//

	// 定义form中的工作名称
	var txtWorkName = new Ext.form.TextField({
				id : "workItemName",
				fieldLabel : "工作名称",
				isFormField : true,				
				readOnly : true,
				maxLength : 20,
				anchor : '100%',
				name : 'regularWorkRegisterInfo.workItemName'
			});
	// 定义form中的工作日期
	var txtWorkDate = new Ext.form.TextField({
				id : "workDate",
				fieldLabel : "工作日期",
				isFormField : true,				
				readOnly : true,
				maxLength : 25,
				anchor : '100%',
				name : 'regularWorkRegisterInfo.workDate'
			});
	// 第一行
	var firstLine = new Ext.Panel({
		border : false,	
		layout : "column",
		style : "padding-top:5px;padding-right:5px;padding-bottom:0px;margin-bottom:0px",
		anchor : '100%',
		items : [{
					columnWidth : 0.5,
					layout : "form",
					border : false,
					items : [txtWorkName]
				}, {
					columnWidth : 0.5,
					layout : "form",
					border : false,
					items : [txtWorkDate]
				}]
	});
	// 定义form中的具体工作内容
	var txtWorkExplain = new Ext.form.TextArea({
				id : "workExplain",
				fieldLabel : "具体工作内容",
				maxLength : 200,
				anchor : '99%',
				name : 'regularWorkRegisterInfo.workExplain'
			});
	// 第二行
	var secondLine = new Ext.Panel({
		border : false,	
		layout : "column",
		style : "padding-top:5px;padding-right:5px;padding-bottom:0px;margin-bottom:0px",
		anchor : '100%',
		items : [{
					columnWidth : 1,
					layout : "form",
					border : false,
					items : [txtWorkExplain]
				}]
	});
	// 定义form中的工作结果
	var txtResult = new Ext.form.TextArea({
				id : "result",
				fieldLabel : "工作结果",
				maxLength : 100,
				anchor : '99%',
				name : 'regularWorkRegisterInfo.result'
			});
	// 第三行
	var thirdLine = new Ext.Panel({
		border : false,		
		layout : "column",
		style : "padding-top:5px;padding-right:5px;padding-bottom:0px;margin-bottom:0px",
		anchor : '100%',
		items : [{
					columnWidth : 1,
					layout : "form",
					border : false,
					items : [txtResult]
				}]
	});
	var cbMarkName = new Ext.form.CmbAchieveFlg({
				id : 'mark',
				fieldLabel : "完成标志",
				labelAlign : 'right',			
				anchor : '100%',
				emptyText : '',
				name : 'regularWorkRegisterInfo.mark'
			});
	// 定义form中的操作人
	var txtOperator = new Ext.form.TextField({
				id : "operator",
				fieldLabel : "操作人",
				isFormField : true,
				maxLength : 6,
				anchor : '100%',
				name : 'regularWorkRegisterInfo.operator'
			});
	// 第四行
	var fourthLine = new Ext.Panel({
		border : false,	
		layout : "column",
		style : "padding-top:5px;padding-right:5px;padding-bottom:0px;margin-bottom:0px",
		anchor : '100%',
		items : [{
					columnWidth : 0.5,
					layout : "form",
					border : false,
					items : [cbMarkName]
				}, {
					columnWidth : 0.5,
					layout : "form",
					border : false,
					items : [txtOperator]
				}]
	});
	// 定义form中的备注
	var txtMemo = new Ext.form.TextArea({
				id : "memo",
				fieldLabel : "备注",
				maxLength : 200,
				anchor : '99%',
				name : 'regularWorkRegisterInfo.memo'
			});
	// 第五行
	var fiveLine = new Ext.Panel({
		border : false,
		// height : 30,
		layout : "column",
		style : "padding-top:5px;padding-right:5px;padding-bottom:0px;margin-bottom:0px",
		anchor : '100%',
		items : [{
					columnWidth : 1,
					layout : "form",
					border : false,
					items : [txtMemo]
				}]
	});
	// 隐藏的上次修改时间
	var hiddenlastModifiedDate = {
		id : 'updateTime',
		xtype : 'hidden',
		readOnly : true,
		hidden : true,
		value : '0',
		name : 'regularWorkRegisterInfo.updateTime'
	};
	// 隐藏序号
	var hiddenId = {
		id : 'id',
		xtype : 'hidden',
		readOnly : true,
		hidden : true,
		value : '0',
		name : 'regularWorkRegisterInfo.id'
	};
	// 第六行
	var sixLine = new Ext.Panel({
		border : false,
		layout : "column",
		style : "padding-top:5px;padding-right:5px;padding-bottom:0px;margin-bottom:0px",
		anchor : '100%',
		items : [{
					columnWidth : 1,
					layout : "form",
					border : false,
					items : [hiddenlastModifiedDate, hiddenId]
				}]
	});
	// 定义弹出窗体中的form
	var mypanel = new Ext.FormPanel({
				labelAlign : 'right',
				autoHeight : true,
				frame : true,
				items : [firstLine, secondLine, thirdLine, fourthLine,
						fiveLine, sixLine]
			});

	// 定义弹出窗体
	var win = new Ext.Window({
				width : 500,
				autoHeight : true,
				buttonAlign : "center",
				items : [mypanel],
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

	// ↑↑********弹出窗口*********↑↑//

	// ↓↓*******************************处理****************************************
	/**
	 * 
	 * 全部定期工作登记
	 */
	function allRegularWork() {
		var mate = window.showModalDialog('AW004S1.jsp', window,
				'dialogWidth=800px;dialogHeight=455px;status=no');
	}

	/**
	 * 取消当天工作登记函数
	 */
	function cancelRecord() {
		Ext.Msg.confirm(Constants.NOTICE_CONFIRM,
				MessageConstants.COM_C_005, function(buttonobj) {
					if (buttonobj == 'yes') {
						// 操作开始
						var strUpdate = [];
						for (i = 0; i < storeInfoGridHidden.getCount(); i++) {
							// 序列化数据
							strUpdate.push(storeInfoGridHidden.getAt(i).data);
						}
						Ext.Ajax.request({
							method : Constants.POST,
							url : 'administration/cancelRegisterData.action',
							params : {
								strUpdate : Ext.util.JSON.encode(strUpdate)
							},
							success : function(result, request) {
								var o = eval("(" + result.responseText + ")");
								// 排他
								if (o.msg == Constants.DATA_USING) {
									Ext.Msg.alert(
											Constants.ERROR,
											MessageConstants.COM_I_002);
								} else {
									// 成功
									Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
											MessageConstants.COM_I_004,
											function() {
												// 重新载入
												storeInfoGrid.load({
													params : {
														start : 0,
														limit : Constants.PAGE_SIZE,
														flag : 0
													}
												});
												storeInfoGridHidden.load({
															params : {
																start : 0,
																limit : 10000,
																flag : 0
															}
														});
												// 页面初始化
												gridInfo.getView().refresh();												
											});
								}
							},
							failure : function() {
							}
						});

					}
				})
	}

	/**
	 * 保存button压下的操作
	 */
	function confirmRecord() {
		// check 字段过长
		if (!isNotLong()) {
			return;
		}
		Ext.Msg.confirm(Constants.NOTICE_CONFIRM, MessageConstants.COM_C_001,
				function(buttonobj) {
					if (buttonobj == 'yes') {
						var myurl = 'administration/updateRegisterData.action';
						mypanel.getForm().submit({
							method : Constants.POST,
							url : myurl,							
							success : function(form, action) {
								var result = eval('('
										+ action.response.responseText + ')');
								// 排他
								if (result.msg == 'U') {
									Ext.Msg.alert(Constants.ERROR,
											MessageConstants.COM_I_002);
									return;
								}
								// 显示成功信息
								Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
										MessageConstants.COM_I_004, function() {
											// 重新载入
											storeInfoGrid.load({
												params : {
													start : 0,
													limit : Constants.PAGE_SIZE,
													flag : 0
												}
											});
											// 页面初始化
											gridInfo.getView().refresh();
											win.hide();
										});
							},
							failure : function() {
								Ext.Msg.alert(Constants.ERROR,
										MessageConstants.COM_E_014);
							}
						});
						return;
					}
				});
	}

	/**
	 * 修改函数
	 */
	function updateRecord() {
		var rec = gridInfo.getSelectionModel().getSelected();
		if (!rec) {
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG, MessageConstants.COM_I_001);
			return false;
		} else {
			strMethod = "update";
			mypanel.getForm().reset();
			win.setTitle("修改工作登记");
			win.show();
			Ext.get('workItemName').dom.value = (rec.get('workItemName') == null
					? ""
					: rec.data.workItemName);
			Ext.get('workDate').dom.value = (rec.get('workDate') == null
					? ""
					: rec.data.workDate);
			Ext.get('workExplain').dom.value = (rec.get('workExplain') == null
					? ""
					: rec.data.workExplain);
			Ext.get('result').dom.value = (rec.get('result') == null
					? ""
					: rec.data.result);
			Ext.get('mark').dom.value = (rec.get('mark') == null
					? ""
					: rec.data.mark);
			Ext.get('operator').dom.value = (rec.get('operator') == null
					? ""
					: rec.data.operator);
			Ext.get('memo').dom.value = (rec.get('memo') == null
					? ""
					: rec.data.memo);
			Ext.get('updateTime').dom.value = (rec.get('updateTime') == null
					? ""
					: rec.data.updateTime);
			Ext.get('id').dom.value = rec.get('id');
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
			Ext.Msg.confirm(Constants.NOTICE_CONFIRM, MessageConstants.COM_C_002,
					function(buttonobj) {
						if (buttonobj == 'yes') {
							// 刪除
							Ext.Ajax.request({
								method : Constants.POST,
								url : 'administration/deleteRegisterData.action',
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
										Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
												MessageConstants.COM_I_005,
												function() {
													// 重新载入数据
													storeInfoGrid.load({
														params : {
															start : 0,
															limit : Constants.PAGE_SIZE,
															flag : 0
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
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG, MessageConstants.COM_I_001);
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
	// 判断入力框是否过长
	function isNotLong() {
		if (check(Ext.get('workExplain').dom.value.RTrim()) > 200
				|| check(Ext.get('result').dom.value.RTrim()) > 100
				|| check(Ext.get('operator').dom.value.RTrim()) > 6
				|| check(Ext.get('memo').dom.value.RTrim()) > 200
			) {
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
			

});