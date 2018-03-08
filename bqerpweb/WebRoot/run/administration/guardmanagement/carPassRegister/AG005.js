Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {
	// 增加按钮
	var btnAdd = new Ext.Button({
				id : 'add',
				text : Constants.BTN_ADD,
				iconCls : Constants.CLS_ADD,
				handler : addHander
			});

	// 修改按钮
	var btnEdit = new Ext.Button({
				id : 'edit',
				text : Constants.BTN_UPDATE,
				iconCls : Constants.CLS_UPDATE,
				handler : editMsg
			});

	// 删除按钮
	var btnDelete = new Ext.Button({
				id : 'delete',
				text : Constants.BTN_DELETE,
				iconCls : Constants.CLS_DELETE,
				handler : deleteHander
			});

	// 类定义
	var carGridList = new Ext.data.Record.create([{
				name : 'id'
			}, {
				name : 'passcode'
			}, {
				name : 'passtime'
			}, {
				name : 'carNo'
			}, {
				name : 'paperId'
			}, {
				name : 'firm'
			}, {
				name : 'preman'
			}, {
				name : 'papertypeCd'
			}, {
				name : 'giveDate'
			}, {
				name : 'postman'
			}, {
				name : 'updateTime'
			}]);
	// grid的store
	var carStore = new Ext.data.JsonStore({
				url : 'administration/searchCar.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : carGridList
				, listeners : {
                    loadexception : function(ds, records, o) {
                        var o = eval("(" + o.responseText + ")");
                        var succ = o.msg;
                        if (succ == Constants.SQL_FAILURE) {
                            Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
                                    MessageConstants.COM_E_014);
                        } else if (succ == Constants.DATE_FAILURE){
                            Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
                                    MessageConstants.COM_E_023);
                        }
                    }

                }
			});

	// 排序
	carStore.setDefaultSort('id', 'ASC');
	carStore.load({
				params : {
					start : 0,
					limit : Constants.PAGE_SIZE
				}
			});
	// 分页工具栏
	var pagebar = new Ext.PagingToolbar({
				pageSize : Constants.PAGE_SIZE,
				store : carStore,
				displayInfo : true,
				displayMsg : MessageConstants.DISPLAY_MSG,
				emptyMsg : MessageConstants.EMPTY_MSG
			});
	// --gridpanel显示格式定义
	var carGrid = new Ext.grid.GridPanel({
				store : carStore,
				columns : [new Ext.grid.RowNumberer({
									header : '行号',
									width : 35
								}), {
							header : "通行证号",
							width : 80,
							sortable : true,
							dataIndex : 'passcode'
						}, {
							header : "进出时间",
							width : 100,
							sortable : true,
							dataIndex : 'passtime',
							renderer : renderDate
						}, {
							header : "车辆所属单位",
							width : 150,
							sortable : true,
							dataIndex : 'firm'
						}],
				viewConfig : {
					forceFit : false
				},
				sm : new Ext.grid.RowSelectionModel({
							singleSelect : true
						}),
				tbar : [btnAdd, btnEdit, btnDelete],
				bbar : pagebar,
				frame : false,
				border : false

			});
	// 设定布局器及面板
	var layout = new Ext.Viewport({
				layout : "border",
				border : false,
				items : [{
							title : "",
							region : 'center',
							layout : 'fit',
							border : false,
							margins : '0 0 0 0',
							split : true,
							collapsible : false,
							items : [carGrid]
						}]
			});
	// 双击编辑
	carGrid.on("rowdblclick", editMsg);

	// 组件默认宽度
	var width = 240;
	// 新增或修改,flag='0'更新;flag='1'增加
	var flag = '0';
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
		name : 'objADJCarpass.id'
	};
	// 通行证号数据源
	var passCodeData = [{
				value : 'a',
				text : 'A'
			}, {
				value : 'b',
				text : 'B'
			}, {
				value : 'c',
				text : 'C'
			}];
	var passCodeCbo = new Ext.form.ComboBox({
				fieldLabel : "通行证号<font color='red'>*</font>",
				id : 'passcode',
				store : new Ext.data.JsonStore({
							fields : ['value', 'text'],
							data : passCodeData
						}),
				emptyText : '',
				mode : 'local',
				readOnly : true,
				allowBlank : false,
				triggerAction : 'all',
				valueField : 'value',
				displayField : 'text',
				anchor : '100%'
			});
	passCodeCbo.setValue('a');
	// 前经办人
	var premanField = new Ext.form.TextField({
				fieldLabel : "前经办人",
				id : "preman",
				name : "objADJCarpass.preman",
				maxLength : 10,
				width : width,
				anchor : '90%'
			});

	// 取日期时间
	var sd = getCurrentDate();
	// 通行时间
	var passDate = new Ext.form.TextField({
				format : 'Y-m-d',
				fieldLabel : "进出时间",
				itemCls : 'sex-left',
				readOnly : true,
				clearCls : 'allow-float',
				checked : true,
				width : 130,
				renderer : renderDate,
				id : "passtime",
				anchor : '100%',
				style : 'cursor:pointer',
				listeners : {
					focus : function() {
						WdatePicker({
									// 时间格式
									startDate : '%y-%M-%d 00:00',
									dateFmt : 'yyyy-MM-dd HH:mm'
								});

					}

				}

			});
	// 退证时间
	var givePaperDate = new Ext.form.TextField({
				format : 'Y-m-d',
				fieldLabel : "退证时间",
				itemCls : 'sex-left',
				readOnly : true,
				clearCls : 'allow-float',
				checked : true,
				width : 130,
				renderer : renderDate,
				id : "giveDate",
				anchor : '100%',
				style : 'cursor:pointer',
				listeners : {
					focus : function() {
						WdatePicker({
									// 时间格式
									startDate : '%y-%M-%d 00:00',
									dateFmt : 'yyyy-MM-dd HH:mm'
								});

					}

				}
			});
	var hideGiveDate = new Ext.form.Hidden({
				id : 'hideGiveDate',
				name : "objADJCarpass.giveDate"
			});
	var hidePassTime = new Ext.form.Hidden({
				id : 'hidePassTime',
				name : "objADJCarpass.passtime"
			});

	// 车牌号
	var carNoField = new Ext.form.TextField({
				fieldLabel : "车牌号<font color='red'>*</font>",
				id : "carNo",
				name : "objADJCarpass.carNo",
				allowBlank : false,
				maxLength : 10,
				width : width,
				anchor : '90%'
			});
	// 证件号
	var paperIdField = new Ext.form.TextField({
				fieldLabel : "证件号",
				id : "paperId",
				name : "objADJCarpass.paperId",
				maxLength : 50,
				width : width,
				anchor : '90%'
			});
	// 车辆所属单位
	var firmField = new Ext.form.TextField({
				fieldLabel : "车辆所属单位",
				id : "firm",
				name : "objADJCarpass.firm",
				maxLength : 25,
				width : 400,
				anchor : '90%'
			});
	// 后经办人
	var postManField = new Ext.form.TextField({
				fieldLabel : "后经办人",
				id : "postman",
				name : "objADJCarpass.postman",
				maxLength : 10,
				width : width,
				anchor : '90%'
			});
	// 证件类别下拉框
	var papertypeCdCbo = new Ext.form.CmbPaperType({
				fieldLabel : "证件类别",
				id : "papertypeCd",
				triggerAction : 'all',
				mode : 'local',
				readOnly : true,
				width : width,
				anchor : '100%'
			});
	papertypeCdCbo.store.load();
	// 第一行
	var firstLine = new Ext.Panel({
		border : false,
		labelAlign : 'right',
		height : 30,
		layout : "column",
		style : "padding-top:5px;padding-right:0px;padding-left:30px;padding-bottom:0px;margin-bottom:0px",
		anchor : '100%',
		items : [{ // 通行证号
			columnWidth : 0.45,
			layout : "form",
			border : false,
			items : [passCodeCbo, hiddenlastModifiedDate, hiddenId,
					hideGiveDate, hidePassTime]
		}, {	// 前经办人
					columnWidth : 0.47,
					layout : "form",
					border : false,
					items : [premanField]
				}]
	});
	// 第二行
	var secondLine = new Ext.Panel({
		border : false,
		labelAlign : 'right',
		height : 30,
		layout : "column",
		style : "padding-top:5px;padding-right:0px;padding-left:30px;padding-bottom:0px;margin-bottom:0px",
		anchor : '100%',
		items : [{ // 通行时间
			columnWidth : 0.45,
			layout : "form",
			border : false,
			items : [passDate]
		}, {	// 车牌号
					columnWidth : 0.47,
					layout : "form",
					border : false,
					items : [carNoField]
				}]
	});
	// 第三行
	var thirdLine = new Ext.Panel({
		border : false,
		labelAlign : 'right',
		height : 30,
		layout : "column",
		style : "padding-top:5px;padding-right:0px;padding-left:30px;padding-bottom:0px;margin-bottom:0px",
		anchor : '100%',
		items : [{ // 通行证类别编码
			columnWidth : 0.45,
			layout : "form",
			border : false,
			items : [papertypeCdCbo]
		}, {	// 证件号
					columnWidth : 0.47,
					layout : "form",
					border : false,
					items : [paperIdField]
				}]
	});
	// 第四行
	var fourLine = new Ext.Panel({
		border : false,
		labelAlign : 'right',
		height : 30,
		layout : "column",
		style : "padding-top:5px;padding-right:0px;padding-left:30px;padding-bottom:0px;margin-bottom:0px",
		anchor : '100%',
		items : [{ // 单位
			columnWidth : 0.97,
			layout : "form",
			border : false,
			items : [firmField]
		}]
	});
	// 第五行
	var fifthLine = new Ext.Panel({
		border : false,
		labelAlign : 'right',
		height : 30,
		layout : "column",
		style : "padding-top:5px;padding-right:0px;padding-left:30px;padding-bottom:0px;margin-bottom:0px",
		anchor : '100%',
		items : [{ // 退证时间
			columnWidth : 0.45,
			layout : "form",
			border : false,
			items : [givePaperDate]
		}, {	// 后经办人
					columnWidth : 0.47,
					layout : "form",
					border : false,
					items : [postManField]
				}]
	});
	// panel
	var myPaner = new Ext.FormPanel({
				region : 'center',
				id : 'paner',
				frame : true,
				border : false,
				labelAlign : 'right',
				height : 150,
				labelPad : 15,
				labelWidth : 80,
				items : [firstLine, secondLine, thirdLine, fourLine, fifthLine]
			});
	// 弹出窗口
	var win = new Ext.Window({
				width : 490,
				height : 230,
				modal : true,
				buttonAlign : "center",
				resizable : false,
				items : [myPaner],
				title : '',
				buttons : [{
							// 保存按钮
							text : Constants.BTN_SAVE,
							iconCls : Constants.CLS_SAVE,
							handler : confirmRecord
						}, { // 取消按钮
							text : Constants.BTN_CANCEL,
							iconCls : Constants.CLS_CANCEL,
							handler : function() {
								Ext.Msg.confirm(
										MessageConstants.SYS_CONFIRM_MSG,
										MessageConstants.COM_C_005, function(
												buttonobj) {
											if (buttonobj == 'yes') {
												win.hide();
											}
										})
							}
						}],
				layout : 'fit',
				closeAction : 'hide'
			});
	// 保存button压下的操作
	function confirmRecord() {
		if (isNotNull()) {
			Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG,
					MessageConstants.COM_C_001, function(buttonobj) {
						if (buttonobj == 'yes') {
							var myurl = "";
							if (Ext.get("passtime").dom.value) {
								hidePassTime.setValue(passDate.getValue()
										+ ':00');
							} else {
								hidePassTime.setValue("");
							}
							if (Ext.get("giveDate").dom.value) {
								hideGiveDate.setValue(givePaperDate.getValue()
										+ ':00');
							} else {
								hideGiveDate.setValue("");
							}
							// 证件类别为空判断
							if (Ext.get("papertypeCd").dom.value == ''
									|| Ext.get("papertypeCd").dom.value == null) {
								papertypeCdCbo.setValue(null);
							}
							if (flag == "1") {
								myurl = 'administration/addCar.action';
								myPaner.getForm().submit({
									method : Constants.POST,
									url : myurl,
									params : {
										passcodeValue : Ext.get("passcode").dom.value,
										papertypeCdValue : papertypeCdCbo
												.getValue()
									},
									success : function(form, action) {
										var result = eval('('
												+ action.response.responseText
												+ ')');
										if (result && result.msg) {
											// sql异常
											if (result.msg == Constants.SQL_FAILURE) {
												Ext.Msg.alert(
																MessageConstants.SYS_ERROR_MSG,
																MessageConstants.COM_E_014);

											} else
												// 显示成功信息
												Ext.Msg.alert(
																MessageConstants.SYS_REMIND_MSG,
																MessageConstants.COM_I_004,
																function() {
																	carStore.load(
																					{
																						params : {
																							start : 0,
																							limit : Constants.PAGE_SIZE
																						}
																					});
																	carGrid.getView().refresh();
																	win.hide();
																});
										}

									},
									failure : function() {
									}
								});
							}
							if (flag == "0") {
								myurl = 'administration/updateCar.action';
								myPaner.getForm().submit({
									method : Constants.POST,
									url : myurl,
									params : {
										updateTime : Ext.get('updateTime').dom.value,
										passcodeValue : Ext.get("passcode").dom.value,
										papertypeCdValue : papertypeCdCbo
												.getValue()
									},
									success : function(form, action) {
										var result = eval('('
												+ action.response.responseText
												+ ')');
										if (result && result.msg) {
											// sql异常
											if (result.msg == Constants.SQL_FAILURE) {
												Ext.Msg.alert(
																MessageConstants.SYS_ERROR_MSG,
																MessageConstants.COM_E_014);
											} else
											// 排他异常
											if (result.msg == Constants.DATA_USING) {
												Ext.Msg.alert(
																MessageConstants.SYS_ERROR_MSG,
																MessageConstants.COM_I_002);
											} else
												// 显示成功信息
												Ext.Msg.alert(
																MessageConstants.SYS_REMIND_MSG,
																MessageConstants.COM_I_004,
																function() {
																	carStore.load(
																					{
																						params : {
																							start : 0,
																							limit : Constants.PAGE_SIZE
																						}
																					});
																	carGrid.getView().refresh();
																	win.hide();
																});
										}
									},
									failure : function() {
									}
								});
							}
						}
					});
		}
	}
	// 修改处理
	function editMsg() {
		flag = '0';
		// 判断是否已选择了数据
		if (carGrid.selModel.hasSelection()) {
			var record = carGrid.getSelectionModel().getSelected();
			win.setTitle("修改进出车辆信息");
			win.show();
			myPaner.getForm().loadRecord(record);
			var renderPasstime = renderDate(record.get("passtime"));
			passDate.setValue(renderPasstime);
			var renderGivedate = renderDate(record.get("giveDate"));
			givePaperDate.setValue(renderGivedate);
			passCodeCbo.setValue(record.get("passcode"));
			papertypeCdCbo.setValue(record.get("papertypeCd"), true);
			Ext.get('updateTime').dom.value = record.get('updateTime') == null
					? ""
					: record.data.updateTime;
		} else {
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
					MessageConstants.COM_I_001);
		}
	}
	// 增加处理
	function addHander() {
		flag = '1'
		myPaner.getForm().reset();
		win.setTitle("新增进出车辆信息")
		passDate.setValue(new Date().format("Y-m-d H:i"));
		win.show();
	}
	// 删除处理
	function deleteHander() {
		// 判断是否已选择了数据
		if (carGrid.selModel.hasSelection()) {
			var record = carGrid.getSelectionModel().getSelected();
			// 向后台传数据
			var carIdValue = record.get("id");
			var updateTime = record.get('updateTime');
			Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG,
					MessageConstants.COM_C_002, function(buttonObj) {
						if (buttonObj == "yes") {
							Ext.Ajax.request({
								method : Constants.POST,
								url : 'administration/deleteCar.action',
								params : {
									carIdValue : carIdValue,
									updateTime : updateTime
								},
								success : function(result, request) {
									var result = eval("(" + result.responseText
											+ ")");
									if (result && result.msg) {
										// sql异常
										if (result.msg == Constants.SQL_FAILURE) {
											Ext.Msg.alert(
															MessageConstants.SYS_ERROR_MSG,
															MessageConstants.COM_E_014);
										} else
										// 排他异常
										if (result.msg == Constants.DATA_USING) {
											Ext.Msg.alert(
															MessageConstants.SYS_ERROR_MSG,
															MessageConstants.COM_I_002);
										} else {
											// 显示成功
											Ext.Msg.alert(
															MessageConstants.SYS_REMIND_MSG,
															MessageConstants.COM_I_005,
															function() {
																carStore.load({
																	params : {
																		start : 0,
																		limit : Constants.PAGE_SIZE
																	}
																});
																carGrid.getView().refresh();
															});
										}
									}
								},
								failure : function() {
								}
							});
						}
					});
		} else {
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
					MessageConstants.COM_I_001);
		}
	}
	// 非空check
	function isNotNull() {
		var msg = "";
		// 判断数据是否为空
		if (passCodeCbo.getValue() == "") {
			msg = String.format(MessageConstants.COM_E_002, "通行证号");
			Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, msg);
			return false;
		}
		if (carNoField.getValue() == "") {
			msg = String.format(MessageConstants.COM_E_002, '车牌号');
			Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, msg);
			return false;
		}
		// 比较进出时间与退证时间
		// 获取进出时间值
		var passTimeValue = Ext.get("passtime").dom.value;
		// 获取退证时间值
		var giveDateValue = Ext.get("giveDate").dom.value;
		// 比较进出时间与退证时间
		if ((passTimeValue != "") && (giveDateValue != "")) {
			var date1 = Date.parseDate(passTimeValue, 'Y-m-d H:i');
			var date2 = Date.parseDate(giveDateValue, 'Y-m-d H:i');
			if (date1.getTime() > date2.getTime()) {
				Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, String.format(
								MessageConstants.COM_E_009, "进出时间", "退证时间"));

				return false;
			}
		}
		return true;
	}
	// 获取系统时间
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
	// 去掉时间中T和秒
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
});