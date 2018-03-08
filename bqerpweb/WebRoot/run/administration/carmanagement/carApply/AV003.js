Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {
	// 增加按钮
	var btnAdd = new Ext.Button({
				id : 'add',
				text : Constants.BTN_ADD,
				iconCls : Constants.CLS_ADD,
				handler : addHandler
			});

	// 修改按钮
	var btnEdit = new Ext.Button({
				id : 'edit',
				text : Constants.BTN_UPDATE,
				iconCls : Constants.CLS_UPDATE,
				handler : editHandler
			});

	// 删除按钮
	var btnDelete = new Ext.Button({
				id : 'delete',
				text : Constants.BTN_DELETE,
				iconCls : Constants.CLS_DELETE,
				handler : deleteHandler
			});
	// 上报按钮
	var btnReport = new Ext.Button({
				id : 'report',
				text : Constants.BTN_REPOET,
				iconCls : Constants.CLS_REPOET,
				handler : reportHandler
			});
	// 取得登陆用户信息
	Ext.Ajax.request({
				url : "administration/getUserInfo.action",
				method : "post",
				success : function(result, request) {
					var data = eval("(" + result.responseText + ")");
					hiddenMan.setValue(data.userCode);
					if(data.userName){
						hiddenName.setValue(data.userName);
					    
					}else{
						hiddenName.setValue("");
					}
					if(data.depName){
						hiddenDep.setValue(data.depName);   
					}else{
						hiddenDep.setValue("");
					}					
				}
			});
	// 类定义recordCarGridList
	var recordCarGridList = new Ext.data.Record.create([{
				name : 'id'
			}, {
				name : 'applyMan'
			}, {
				name : 'ifOut'
			}, {
				name : 'useNum'
			}, {
				name : 'useDate'
			}, {
				name : 'useDays'
			}, {
				name : 'aim'
			}, {
				name : 'reason'
			}, {
				name : 'name'
			}, {
				name : 'depName'
			}, {
				name : 'updateTime'
			}]);
            
	// grid的store
	var storeCar = new Ext.data.JsonStore({
				url : 'administration/searchCarApply.action',
				root : 'data.list',
				totalProperty : 'data.totalCount',
				fields : recordCarGridList
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
	// 隐藏姓名
	var hiddenName = new Ext.form.Hidden({
				id : 'hiddName',
				value : '0'
			});
	// 隐藏部门
	var hiddenDep = new Ext.form.Hidden({
				id : 'hiddDep',
				value : '0'
			});
	// 隐藏申请人编码
	var hiddenMan = new Ext.form.Hidden({
				id : 'hiddMan',
				value : '0'
			});
	// 排序
	storeCar.setDefaultSort('id', 'ASC');
	storeCar.load({
				params : {
					start : 0,
					limit : Constants.PAGE_SIZE
				}
			});
	// 分页工具栏
	var pagebar = new Ext.PagingToolbar({
				pageSize : Constants.PAGE_SIZE,
				store : storeCar,
				displayInfo : true,
				displayMsg : MessageConstants.DISPLAY_MSG,
				emptyMsg : MessageConstants.EMPTY_MSG
			});
	// --gridpanel显示格式定义
	var gridCar = new Ext.grid.GridPanel({
				store : storeCar,
				columns : [new Ext.grid.RowNumberer({
									header : '行号',
									width : 35
								}), {
							header : "申请人",
							width : 80,
							sortable : true,
							dataIndex : 'name'
						}, {
							header : "用车部门",
							width : 100,
							sortable : true,
							dataIndex : 'depName'
						}, {
							header : "用车日期",
							width : 80,
							sortable : true,
							dataIndex : 'useDate'
						}, {
							header : "用车天数",
							width : 80,
							sortable : true,
							dataIndex : 'useDays',
							renderer : divide
						}, {
							header : "到达地点",
							width : 200,
							sortable : true,
							dataIndex : 'aim'
						}],
				sm : new Ext.grid.RowSelectionModel({
							singleSelect : true
						}),
				tbar : [btnAdd, btnEdit, btnDelete, btnReport, hiddenName,
						hiddenDep, hiddenMan],
				bbar : pagebar,
				frame : false,
				border : false,
				enableColumnMove : false
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
							items : [gridCar]
						}]
			});
	// 双击编辑
	gridCar.on("rowdblclick", editHandler);

	// 组件默认宽度
	var width = 180;
	// 新增或修改,flag='0'更新;flag='1'增加
	var flag = '0';
	// 隐藏序号
	var hiddenId = {
		id : 'id',
		xtype : 'hidden',
		readOnly : true,
		hidden : true,
		value : '0',
		name : 'applyCar.id'
	};
	// 隐藏申请人编码
	var hiddenApplyMan = {
		id : 'applyMan',
		xtype : 'hidden',
		readOnly : true,
		hidden : true,
		value : '0',
		name : 'applyCar.applyMan'
	};

	// 申请人
	var txtName = new Ext.form.TextField({
				fieldLabel : "申请人",
				id : "name",
				name : "name",
				width : width,
				anchor : '90%'
			});
	// 用车部门
	var txtDepName = new Ext.form.TextField({
				fieldLabel : "用车部门",
				id : "depName",
				name : "depName",
				width : width,
				anchor : '90%'
			});
	// 用车人数
	var txtUseNum = new Ext.form.MoneyField({
				fieldLabel : "用车人数",
				id : 'useNum',
				isFormField : true,
				width : width,
				labelAlign : 'right',
				maxLength : 3,
				appendChar : '人',
				allowNegative : false,
				allowDecimals : false,
				style : 'text-align:right',
				anchor : '90%'
			});
			
	var txtUseNumHide = new Ext.form.Hidden({
				id : 'useNumHide',
				name : "applyCar.useNum"
			});

	// 取系统日期
	var nowDate = new Date();
	nowDate.setDate(nowDate.getDate());
	nowDate = nowDate.format('Y-m-d');

	// 用车日期
	var tfdUseDate = new Ext.form.TextField({
				format : 'Y-m-d',
				fieldLabel : "用车日期",
				itemCls : 'sex-left',
				readOnly : true,
				clearCls : 'allow-float',
				width : width,
				checked : true,
				id : "useDate",
				name : 'applyCar.useDate',
				value : nowDate,
				anchor : '90%',
				style : 'cursor:pointer',
				listeners : {
					focus : function() {
						WdatePicker({
									// 时间格式
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd'
								});
					}
				}
			});
	// 是否出省数据源
	var storeIfOut = [{
				value : 'Y',
				text : '出省'
			}, {
				value : 'N',
				text : '不出省'
			}];
	// 是否出省下拉框
	var drpIfOut = new Ext.form.ComboBox({
				fieldLabel : "是否出省",
				id : 'ifOut',
				store : new Ext.data.JsonStore({
							fields : ['value', 'text'],
							data : storeIfOut
						}),
				emptyText : '',
				mode : 'local',
				readOnly : true,
				width : width,
				triggerAction : 'all',
				valueField : 'value',
				displayField : 'text',
				anchor : '90%'
			});
	drpIfOut.setValue('Y');

	// 用车天数
	var txtUseDays = new Ext.form.MoneyField({
				fieldLabel : "用车天数",
				id : "useDays",
				isFormField : true,
				labelAlign : 'right',
				decimalPrecision : 2,
				padding : 2,
				minValue : 0.01,
				maxValue : 99999999.99,
				appendChar : '天',
				maxLength : 13,
				style : 'text-align:right',
				width : width,
				anchor : '90%'
			});
	var txtUseDaysHide = new Ext.form.Hidden({
				id : 'useDaysHide',
				name : "applyCar.useDays"
			});

	// 到达地点
	var txtAim = new Ext.form.TextField({
				fieldLabel : "到达地点",
				id : "aim",
				name : "applyCar.aim",
				maxLength : 50,
				anchor : '90%'
			});
            
//	// 响应双击事件，打开录入窗口
//	txtAim.onDblClick(function() {
//				txtPop.setValue(txtAim.getValue());
//				txtPop.maxLength = 50;
//				winInput.show();
//			})
//	// *****详细信息录入窗口*******//
//	var txtPop = new Ext.form.TextArea({
//				id : "pop",
//				name : "pop",
//				maxLength : 100
//			});
//
//	var winInput = new Ext.Window({
//				width : 350,
//				modal : true,
//				height : 200,
//				title : '详细信息录入窗口',
//				buttonAlign : "center",
//				items : [txtPop],
//				layout : 'fit',
//				closeAction : 'hide',
//				resizable : false,
//				buttons : [{
//							text : "保存",
//							id : "back",
//							handler : function() {
//								if (!txtPop.validate()) {
//									return;
//								}
//								var tempValue = txtPop.getValue().replace(
//										/\n/g, '');
//								txtAim.setValue(tempValue);
//								winInput.hide();
//							}
//						}, {
//							text : "取消",
//							id : "cancel",
//							handler : function() {
//								winInput.hide();
//							}
//						}]
//			});

	
	// 用车事由
	var txaReason = new Ext.form.TextArea({
				fieldLabel : "用车事由",
				id : "reason",
				name : "applyCar.reason",
				maxLength : 50,
				height : 120,
				anchor : '90%'
			});

	// 第一行
	var firstLine = new Ext.Panel({
		border : false,
		labelAlign : 'right',
		layout : "column",
		style : "padding-top:5px;padding-right:0px;padding-left:0px;padding-bottom:0px;margin-bottom:0px",
		anchor : '100%',
		items : [{ // 申请人
			columnWidth : 0.45,
			layout : "form",
			border : false,
			items : [txtName, hiddenId, hiddenApplyMan]
		}, {	// 用车部门
					columnWidth : 0.47,
					layout : "form",
					border : false,
					items : [txtDepName]
				}]
	});
	// 第二行
	var secondLine = new Ext.Panel({
		border : false,
		labelAlign : 'right',
		layout : "column",
		style : "padding-top:5px;padding-right:0px;padding-left:0px;padding-bottom:0px;margin-bottom:0px",
		anchor : '100%',
		items : [{ // 用车人数
			columnWidth : 0.45,
			layout : "form",
			border : false,
			items : [txtUseNum, txtUseNumHide]
		}, {	// 用车日期
					columnWidth : 0.47,
					layout : "form",
					border : false,
					items : [tfdUseDate]
				}]
	});
	// 第三行
	var thirdLine = new Ext.Panel({
		border : false,
		labelAlign : 'right',
		layout : "column",
		style : "padding-top:5px;padding-right:0px;padding-left:0px;padding-bottom:0px;margin-bottom:0px",
		anchor : '100%',
		items : [{ // 是否出省
			columnWidth : 0.45,
			layout : "form",
			border : false,
			items : [drpIfOut]
		}, {	// 用车天数
					columnWidth : 0.47,
					layout : "form",
					border : false,
					items : [txtUseDays, txtUseDaysHide]
				}]
	});
	// 第四行
	var fourLine = new Ext.Panel({
		border : false,
		labelAlign : 'right',
		layout : "column",
		style : "padding-top:5px;padding-right:0px;padding-left:0px;padding-bottom:0px;margin-bottom:0px",
		anchor : '100%',
		items : [{ // 到达地点
			columnWidth : 0.97,
			layout : "form",
			border : false,
			items : [txtAim]
		}]
	});
	// 第五行
	var fifthLine = new Ext.Panel({
		border : false,
		labelAlign : 'right',
		layout : "column",
		style : "padding-top:5px;padding-right:0px;padding-left:0px;padding-bottom:0px;margin-bottom:0px",
		anchor : '100%',
		items : [{ // 用车事由
			columnWidth : 0.97,
			layout : "form",
			border : false,
			items : [txaReason]
		}]
	});
	// panel
	var myPanel = new Ext.FormPanel({
				region : 'center',
				frame : true,
				id : 'panel',
				border : false,
				labelAlign : 'right',
				labelPad : 15,
				labelWidth : 80,
				items : [firstLine, secondLine, thirdLine, fourLine, fifthLine]
			});
	// 弹出窗口
	var win = new Ext.Window({
				width : 500,
				height : 350,
				modal : true,
				buttonAlign : "center",
				resizable : false,
				items : [myPanel],
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
		Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG,
				MessageConstants.COM_C_001, function(buttonobj) {
					if (buttonobj == 'yes') {
							var myurl = "";
							if (Ext.get("useDays").dom.value) {
								txtUseDaysHide.setValue(txtUseDays.getValue());
							} else {
								txtUseDaysHide.setValue("");
							}
							if (Ext.get("useNum").dom.value) {
								txtUseNumHide.setValue(txtUseNum.getValue());
							} else {
								txtUseNumHide.setValue("");
							}
							if (flag == "1") {
								myurl = 'administration/addCarApply.action';
								myPanel.getForm().submit({
									method : Constants.POST,
									url : myurl,
									params : {
										drpIfOutValue : drpIfOut.getValue()
									},
									success : function(form, action) {
										var o = eval('('
												+ action.response.responseText
												+ ')');
										if (o && o.msg) {
											// sql 异常
											if (o.msg == Constants.SQL_FAILURE) {
												Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
																MessageConstants.COM_E_014);
											} else {
												// 显示成功信息
												Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
																MessageConstants.COM_I_004,
																function() {
																	storeCar.load(
																					{
																						params : {
																							start : 0,
																							limit : Constants.PAGE_SIZE
																						}
																					});
																	gridCar.getView().refresh();
																	win.hide();
																});
											}
										}
									},
									failure : function() {
									}
								});
							}
							if (flag == "0") {
								myurl = 'administration/updateCarApply.action';
								myPanel.getForm().submit({
									method : Constants.POST,
									url : myurl,
									params : {
										drpIfOutValue : drpIfOut.getValue()
									},
									success : function(form, action) {
										var o = eval('('
												+ action.response.responseText
												+ ')');
										if (o && o.msg) {
											// sql 异常
											if (o.msg == Constants.SQL_FAILURE) {
												Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
																MessageConstants.COM_E_014);
											} else {
												// 显示成功信息
												Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
																MessageConstants.COM_I_004,
																function() {
																	storeCar.load(
																					{
																						params : {
																							start : 0,
																							limit : Constants.PAGE_SIZE
																						}
																					});
																	gridCar.getView().refresh();
																	win.hide();
																});
											}
										}
									},
									failure : function() {
									}
								});
							}
					}
				});

	}
	// 修改处理
	function editHandler() {
		flag = '0';
		// 判断是否已选择了数据
		if (gridCar.selModel.hasSelection()) {
			var record = gridCar.getSelectionModel().getSelected();
			win.setTitle("修改用车申请");
			win.show();
			myPanel.getForm().loadRecord(record);
			Ext.get("name").dom.readOnly = true;
			Ext.get("depName").dom.readOnly = true;
			drpIfOut.setValue(record.get("ifOut"));
		} else {
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
					MessageConstants.COM_I_001);
		}
	}
	// 增加处理
	function addHandler() {
		flag = '1'
		myPanel.getForm().reset();
		win.setTitle("新增用车申请")
		win.show();
		Ext.get('name').dom.value = hiddenName.getValue();
		Ext.get("name").dom.readOnly = true;
		Ext.get('depName').dom.value = hiddenDep.getValue();
		Ext.get("depName").dom.readOnly = true;
		Ext.get('applyMan').dom.value = hiddenMan.getValue();
	}
	// 删除处理
	function deleteHandler() {
		// 判断是否已选择了数据
		if (gridCar.selModel.hasSelection()) {
			var record = gridCar.getSelectionModel().getSelected();
			// 向后台传数据
			var carIdValue = record.get("id");
			var updateTime = record.get('updateTime');
			Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG,
					MessageConstants.COM_C_002, function(buttonObj) {
						if (buttonObj == "yes") {
							Ext.Ajax.request({
								method : Constants.POST,
								url : 'administration/deleteCarApply.action',
								params : {
									carIdValue : carIdValue,
									updateTime : updateTime
								},
                                success : function(result, request) {
                                    var o = eval("(" + result.responseText
                                            + ")");
                                        if (o && o.msg) {
                                            // sql 异常
                                            if (o.msg == Constants.SQL_FAILURE) {
                                                Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
                                                                MessageConstants.COM_E_014);
                                            } else {
                                                // 显示成功信息
                                                Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
                                                                MessageConstants.COM_I_005,
                                                                function() {
                                                                    storeCar.load(
                                                                                    {
                                                                                        params : {
                                                                                            start : 0,
                                                                                            limit : Constants.PAGE_SIZE
                                                                                        }
                                                                                    });
                                                                    gridCar.getView().refresh();
                                                                });
                                            }
                                        }
                                    },
								failure : function() {
									Ext.Msg.alert(
											MessageConstants.SYS_ERROR_MSG,
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

	// 上报处理
	function reportHandler() {
		// 判断是否已选择了数据
		if (gridCar.selModel.hasSelection()) {
			var record = gridCar.getSelectionModel().getSelected();
			// 向后台传数据
			var carIdValue = record.get("id");
			Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG,
					MessageConstants.COM_C_006, function(buttonObj) {
						if (buttonObj == "yes") {
							Ext.Ajax.request({
								method : Constants.POST,
								url : 'administration/reportCarApply.action',
								params : {
									carIdValue : carIdValue
								},
                                success : function(result, request) {
                                    var o = eval("(" + result.responseText
                                            + ")");
										if (o && o.msg) {
											// sql 异常
											if (o.msg == Constants.SQL_FAILURE) {
												Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
																MessageConstants.COM_E_014);
											} else {
												// 显示成功信息
												Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
																MessageConstants.REPORT_SUCCESS,
																function() {
																	storeCar.load(
																					{
																						params : {
																							start : 0,
																							limit : Constants.PAGE_SIZE
																						}
																					});
																	gridCar.getView().refresh();
																});
											}
										}
									},
								failure : function() {
									Ext.Msg.alert(
											MessageConstants.SYS_ERROR_MSG,
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
	// 渲染用车天数
	function divide(v, argDecimal) {
		if (v) {
			if (typeof argDecimal != 'number') {
                // xsTan 修改开始 2009-02-06 设定2位小数
				argDecimal = 2;
                // xsTan 修改结束 2009-02-06
			}
			v = Number(v).toFixed(argDecimal);
			var t = '';
			v = String(v);
			while ((t = v.replace(/^(-?[0-9]+)([0-9]{3}.*)$/, '$1,$2')) !== v)
				v = t;
			
			return v + ' 天' ;
		} else
			return '';
	}
});