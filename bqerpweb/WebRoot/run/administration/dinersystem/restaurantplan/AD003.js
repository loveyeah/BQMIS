// 餐厅计划维护
// author:sufeiyu

Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.QuickTips.init();
Ext.onReady(function() {

	// 声明变量。
	var today = "";
	var record = null;
	var temp = "";
	var flag = false;
	

	dateInit();
	var tempDate = today;
	// 计划维护--------------------------------------------------
	// 定义选择列
	var smPlan = new Ext.grid.RowSelectionModel({
				singleSelect : true
			});

	smPlan.on('rowselect', isInDB);

	// 数据源--------------------------------
	var recordPlan = Ext.data.Record.create([{
				name : 'id'
			}, {
				name : 'menuCode'
			}, {
				name : 'menuName'
			}, {
				name : 'planDate'
			}, {
				name : 'retrieveCode'
			}, {
				name : 'menuType'
			}, {
				name : 'menuTypeCode'
			}, {
				name : 'menuTypeName'
			}, {
				name : 'menuPrice'
			}, {
				name : 'memo'
			}, {
				name : 'updateTime'
			}]);

	// 定义获取数据源
	var proxyPlan = new Ext.data.HttpProxy({
				url : 'administration/getRestaurantPlan.action'
			});
	
	// 定义获取数据源
	var proxyPlanCopy = new Ext.data.HttpProxy({
				url : 'administration/getRestaurantPlan.action'
			});		

	// 定义格式化数据
	var readerPlan = new Ext.data.JsonReader({
				root : "list",
				totalProperty : "totalCount"
			}, recordPlan);
			
	// 定义格式化数据
	var readerPlanCopy = new Ext.data.JsonReader({
				root : "list",
				totalProperty : "totalCount"
			}, recordPlan);		

	// 定义封装缓存数据的对象
	var storePlan = new Ext.data.Store({
				// 访问的对象
				proxy : proxyPlan,
				// 处理数据的对象
				reader : readerPlan
			});
			
	// 定义封装缓存数据的对象
	var storePlanCopy = new Ext.data.Store({
				// 访问的对象
				proxy : proxyPlanCopy,
				// 处理数据的对象
				reader : readerPlanCopy
			});		

	// --gridpanel显示格式定义-----开始-------------------
	var texPlanDate = new Ext.form.TextField({
				id : 'planDate',
				name : 'planDate',
				style : 'cursor:pointer',
				value : today,
				readOnly : true,
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd',
									isShowClear : false,
									onpicked : function() {
										isPlanChanged();
										if (flag == true) {
											Ext.Msg.confirm(
													Constants.BTN_CONFIRM,
													MessageConstants.COM_C_004,
													function(buttonobj) {
														if (buttonobj == 'yes') {
															initPlan();
															flag = false;
														} else {
															texPlanDate.setValue(tempDate);
														}
													})
										} else {
											initPlan();
											flag = false;
										}
									}
								});
					}
				}
			});

	// 增加按钮
	var btnAdd = new Ext.Button({
				text : Constants.BTN_ADD,
				iconCls : Constants.CLS_ADD,
				handler : addPlan
			});

	// 删除按钮
	var btnDelete = new Ext.Button({
				text : Constants.BTN_DELETE,
				iconCls : Constants.CLS_DELETE,
				handler : deletePlan
			});

	// 保存按钮
	var btnSave = new Ext.Button({
				text : Constants.BTN_SAVE,
				iconCls : Constants.CLS_SAVE,
				handler : savePlan
			});

	// 取消按钮
	var btnCancelPlan = new Ext.Button({
				text : Constants.BTN_CANCEL,
				iconCls : Constants.CLS_CANCEL,
				handler : cancelPlan
			});

	// 用餐类别
	var drpMeal = new Ext.form.CmbMealType({
				valueField : 'value',
				displayField : 'text'
			});

	// 单价
	var txtPrice = new Ext.form.MoneyField({
				// 精度
				decimalPrecision : 2,
				minValue : 0.01,
				maxValue : 99999999999.99,
				allowNegative : false,
				style : 'text-align:right'
			});

	// 备注
	var txtMemo = new Ext.form.TextField({
	    id : 'memo',
	    name : 'memo'
	});
	
	// ↓↓*******************查看备注************************
    // 备注
    var memoText = new Ext.form.TextArea({
         id : "memoText",
         maxLength : 127,
         width : 180
    });
    
    // 弹出画面
     var winMemo = new Ext.Window({
        height : 170,
        width : 350,
        layout : 'fit',
        modal:true,
        resizable : false,
        closeAction : 'hide',
        items : [memoText],
        buttonAlign : "center",
        title : '详细信息录入窗口',
        buttons : [{
					text : Constants.BTN_CONFIRM,
				    iconCls : 'ok',
					id : "back",
					handler : function() {
						var record = gridPlan.getSelectionModel().getSelected();
						var tempValue = memoText.getValue().replace(/\n/g, '');
						record.data.memo = tempValue;
						gridPlan.getView().refresh();
						winMemo.hide();
					}
				},{
					text : Constants.BTN_CANCEL,
				    iconCls : Constants.CLS_CANCEL,
					id : "cancel",
					handler : function() {
						winMemo.hide();
					}
				}]
    }); 
    

	storePlan.baseParams = {
		dtePlanDate : texPlanDate.getValue()
	}
	storePlan.load({
				params : {
					start : 0,
					limit : Constants.PAGE_SIZE
				}
			});
			
	storePlanCopy.baseParams = {
		dtePlanDate : texPlanDate.getValue()
	}
	storePlanCopy.load({
				params : {
					start : 0,
					limit : Constants.PAGE_SIZE
				}
			});		

	var cmPlan = new Ext.grid.ColumnModel([
			// 自动行号
			new Ext.grid.RowNumberer({
						header : "行号",
						width : 31
					}), {
				header : "序号",
				hidden : true,
				dataIndex : 'id'
			}, {
				header : "日期",
				sortable : true,
				dataIndex : 'planDate'
			}, {
				header : "用餐类别",
				sortable : true,
				dataIndex : 'menuType',
				renderer : typeFormat,
				editor : drpMeal
			}, {
				header : "菜谱Code",
				hidden : true,
				dataIndex : 'menuCode'
			}, {
				header : "菜谱名称",
				sortable : true,
				dataIndex : 'menuName'
			}, {

				header : "类别Code",
				hidden : true,
				dataIndex : 'menuTypeCode'
			}, {
				header : "菜谱类别",
				sortable : true,
				dataIndex : 'menuTypeName'
			}, {
				header : "上次修改时间",
				hidden : true,
				dataIndex : 'updateTime'
			}, {
				header : "价格",
				sortable : true,
				dataIndex : 'menuPrice',
				align : 'right',
				renderer : divide2,
				editor : txtPrice
			}, {
				header : "备注",
				sortable : true,
				dataIndex : 'memo',
				editor : txtMemo
			}])

	// 头部工具栏
	var tbarPlan = new Ext.Toolbar(['日期: ', texPlanDate, '-', btnAdd,
			btnDelete, btnSave, btnCancelPlan])

//	// 分页
//	var bbarPlan = new Ext.PagingToolbar({
//				store : storePlan,
//				displayInfo : true,
//				displayMsg : MessageConstants.DISPLAY_MSG,
//				emptyMsg : MessageConstants.EMPTY_MSG
//			})

	var gridPlan = new Ext.grid.EditorGridPanel({
				layout : 'fit',
				store : storePlan,
				cm : cmPlan,
				sm : smPlan,
				autoScroll : true,
				clicksToEdit : 1,
				enableColumnMove : false,
				autoSizeColumns : true,
				viewConfig : {
					forceFit : false
				},
				// 头部工具栏
				tbar : tbarPlan

//				// 分页
//				bbar : bbarPlan
			});
	// --gridpanel显示格式定义-----结束--------------------
			
	// ↑↑*******************查看备注************************
	txtMemo.onDblClick(showMemoWin);
//	txtMemo.onClick(showMemoWin);

	// ----------弹出窗口-----------------------------------

	// 数据源--------------------------------
	var recordMenu = Ext.data.Record.create([{
				name : 'id'
			}, {
				name : 'menuCode'
			}, {
				name : 'menuName'
			}, {
				name : 'planDate'
			}, {
				name : 'retrieveCode'
			}, {
				name : 'menuType'
			}, {
				name : 'menuTypeCode'
			}, {
				name : 'menuTypeName'
			}, {
				name : 'menuPrice'
			}, {
				name : 'memo'
			}, {
				name : 'updateTime'
			}]);

	// 定义获取数据源
	var proxyMenu = new Ext.data.HttpProxy({
				url : 'administration/getAllMenu.action'
			});

	// 定义格式化数据
	var readerMenu = new Ext.data.JsonReader({
				root : "list",
				totalProperty : "totalCount"
			}, recordMenu);

	// 定义封装缓存数据的对象
	var storeMenu = new Ext.data.Store({
				// 访问的对象
				proxy : proxyMenu,
				// 处理数据的对象
				reader : readerMenu
			});

	var drpMenu = new Ext.form.CmbCMenuType({
				id : 'typeMenu'
			});
	drpMenu.on('select', drpSelect);

	// 选择按钮
	var btnSelectMeal = new Ext.Button({
				text : Constants.BTN_CONFIRM,
				iconCls : 'ok',
				buttonAlign : 'center',
				handler : selectMeal
			})

	// 取消按钮
	var btnCancelMeal = new Ext.Button({
				text : Constants.BTN_CANCEL,
				iconCls : Constants.CLS_CANCEL,
				buttonAlign : 'center',
				handler : cancelMeal
			})

	// 头部工具栏
	var tbarMenu = new Ext.Toolbar(['菜谱类别: ', drpMenu])

	// 定义选择列
	var smMenu = new Ext.grid.CheckboxSelectionModel({
				header : "选择",
				id : "check",
				width : 35,
				singleSelect : false,
				onMouseDown : function(e, t) {
					if (e.button === 0) {														// left-click
						e.stopEvent();
						var row = e.getTarget('.x-grid3-row');
						if (row) {
							var rowIndex = row.rowIndex;
							var isSelected = this.isSelected(rowIndex);
                            if (isSelected) {
                                this.deselectRow(rowIndex);
                            } else if (!isSelected || this.getCount() > 1) {
                                this.selectRow(rowIndex, true);
                                  var view = this.grid.getView();
                                view.focusRow(rowIndex);
                            }
						}
					}
				}
			})

	var cmMenu = new Ext.grid.ColumnModel([
			// 自动行号
			new Ext.grid.RowNumberer({
						header : "行号",
						width : 31
					}), {
				header : "序号",
				hidden : true,
				dataIndex : 'id'
			}, {
				header : "菜谱编码",
				sortable : true,
				dataIndex : 'menuCode'
			}, {
				header : "菜谱名称",
				sortable : true,
				dataIndex : 'menuName'
			}, {
				header : "价格",
				sortable : true,
				dataIndex : 'menuPrice',
				renderer : divide2
			}, {
				header : "检索码",
				sortable : true,
				dataIndex : 'retrieveCode'
			}, smMenu])

	// 分页
	var bbarMenu = new Ext.PagingToolbar({
				pageSize : Constants.PAGE_SIZE,
				store : storeMenu,
				displayInfo : true,
				displayMsg : MessageConstants.DISPLAY_MSG,
				emptyMsg : MessageConstants.EMPTY_MSG
			})

	var gridMenu = new Ext.grid.GridPanel({
				layout : 'fit',
				store : storeMenu,
				cm : cmMenu,
				sm : smMenu,
				autoScroll : true,
				enableColumnMove : false,
				autoSizeColumns : true,
				height : 255,
				width : 400,
				viewConfig : {
					forceFit : false
				},
				// 头部工具栏
				tbar : tbarMenu,
				// 分页
				bbar : bbarMenu
			});

	// Panel
	var formpanelMenu = new Ext.FormPanel({
				layout : 'fit',
				border : false,
				enableTabScroll : true,
				items : [gridMenu]
			})

	// 弹出窗口
	var win = new Ext.Window({
				width : 502,
				height : 320,
				modal : true,
				title : '菜谱选择',
				buttonAlign : 'center',
				items : [formpanelMenu],
				autoScroll : true,
				layout : 'fit',
				closeAction : 'hide',
				resizable : false,
				buttons : [btnSelectMeal, btnCancelMeal]
			});

	// 记事结束--------------------------------------------------

	// **********主画面********** //
	new Ext.Viewport({
				enableTabScroll : true,
				layout : "fit",
				items : [gridPlan]
			});

	// **********画面事件处理********** //
	// 增加
	function addPlan() {
		formpanelMenu.getForm().reset();
		win.show();
//		// 菜谱类别数据源	
//		var storeMenuType = new Ext.data.JsonStore({
//					url : 'administration/getAllCMenuType.action',
//					root : 'list',
//					fields : [{
//								name : 'strMenuTypeName'
//							}, {
//								name : 'strMenuTypeCode'
//							}]
//				})
//		storeMenuType.load();
		//初始化菜谱类别下拉框并查询填充gridMenu
		drpMenu.store.on("load",function(){
			if (drpMenu.store.getCount() == 0) {
				Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG, String.format(
											MessageConstants.COM_I_013,
											"菜谱类别"), function(){
											    win.hide();
											});
											
					return;
			}
			//取数据
		    var initData = drpMenu.store.getAt(0).get('strMenuTypeCode');
		    //设初始值
			drpMenu.setValue(initData);
			temp =  drpMenu.getValue();
			//查询并填充gridMenu
			storeMenu.baseParams = {
				strMenutypeCode : drpMenu.store.getAt(0).get('strMenuTypeCode')
			}
			storeMenu.load({
						params : {
							start : 0,
							limit : Constants.PAGE_SIZE
						}
					});
	 	});
	 	drpMenu.store.load();
//	 	temp =  drpMenu.getValue();
//		storeMenu.removeAll();
//		storeMenu.commitChanges();
//		gridMenu.getBottomToolbar().updateInfo();
//		win.show();
	}

	// 删除
	function deletePlan() {
		var rec = gridPlan.getSelectionModel().getSelected();
		if (rec) {
			Ext.Msg.confirm(Constants.BTN_CONFIRM, MessageConstants.COM_C_002,
					function(buttonobj) {
						if (buttonobj == 'yes') {
							// 如果是DB中没有的数据 增加
							if (rec.data.id == "") {
								Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
										MessageConstants.COM_I_005, function() {
											storePlan.remove(rec);
											storePlan.totalLength = storePlan
													.getTotalCount()
													- 1;
//											storePlan.commitChanges();
											gridPlan.getView().refresh();
//											gridPlan.getBottomToolbar()
//													.updateInfo();
										});
							} else {
								// Db中的数据更新
								var lngId = rec.data.id;
								var strUpdateTime = rec.data.updateTime;
								// 刪除
								Ext.Ajax.request({
									method : Constants.POST,
									url : 'administration/deletePlan.action',
									params : {
										lngId : lngId,
										strUpdateTime : strUpdateTime
									},
									success : function(result, request) {
										var o = eval("(" + result.responseText
												+ ")");
										// 排他
										if (o.msg == 'U') {
											Ext.Msg
													.alert(
															MessageConstants.SYS_ERROR_MSG,
															MessageConstants.COM_I_002);
										} // Sql错误
										if (o.msg == Constants.SQL_FAILURE) {
											Ext.Msg.alert(Constants.ERROR,
													MessageConstants.COM_E_014);
											return;
										}
										// IO错误
										if (o.msg == Constants.IO_FAILURE) {
											Ext.Msg.alert(Constants.ERROR,
													MessageConstants.COM_E_022);
											return;
										}
										// 数据格式化错误
										if (o.msg == Constants.DATE_FAILURE) {
											Ext.Msg.alert(Constants.ERROR,
													MessageConstants.COM_E_023);
											return;
										} else {
											// 成功
											Ext.Msg
													.alert(
															MessageConstants.SYS_REMIND_MSG,
															MessageConstants.COM_I_005,
															function() {
																storePlan
																		.remove(rec);
																storePlan.totalLength = storePlan
																		.getTotalCount()
																		- 1;
																for(i = 1; i < storePlanCopy.getCount(); i++) {
																	if(storePlanCopy.getAt(i).data.id == lngId) {
																		storePlanCopy.remove(storePlanCopy.getAt(i));
																	}
																}		
//																storePlan
//																		.commitChanges();
																gridPlan
																		.getView()
																		.refresh();
//																gridPlan
//																		.getBottomToolbar()
//																		.updateInfo();
															});
										}
									},
									failure : function() {
										Ext.Msg.alert(Constants.ERROR,
												Constants.DEL_ERROR);
									}
								});

							}
						}
					})
		} else {
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
					MessageConstants.COM_I_001);
		}
	}

	// 保存
	function savePlan() {
		Ext.Msg.confirm(Constants.BTN_CONFIRM, MessageConstants.COM_C_001,
				function(buttonobj) {
					if (buttonobj == 'yes') {
						// 保存操作开始
						var strAdd = [];
						var strUpdate = [];
						for (i = 0; i < storePlan.getCount(); i++) {
							// 序列化数据
							if (storePlan.getAt(i).data.id == "") {
								strAdd.push(storePlan.getAt(i).data);
							} else {
								strUpdate.push(storePlan.getAt(i).data);
							}
						}
						Ext.Ajax.request({
									method : Constants.POST,
									url : 'administration/dealData.action',
									params : {
										strAdd : Ext.util.JSON.encode(strAdd),
										strUpdate : Ext.util.JSON
												.encode(strUpdate)
									},
									success : function(result, request) {
										var o = eval("(" + result.responseText
												+ ")");
										// 排他
										if (o.msg == Constants.DATA_USING) {
											Ext.Msg
													.alert(
															MessageConstants.SYS_ERROR_MSG,
															MessageConstants.COM_I_002);
											return;
										}
										// Sql错误
										if (o.msg == Constants.SQL_FAILURE) {
											Ext.Msg.alert(Constants.ERROR,
													MessageConstants.COM_E_014);
											return;
										}
										// IO错误
										if (o.msg == Constants.IO_FAILURE) {
											Ext.Msg.alert(Constants.ERROR,
													MessageConstants.COM_E_022);
											return;
										}
										// 数据格式化错误
										if (o.msg == Constants.DATE_FAILURE) {
											Ext.Msg.alert(Constants.ERROR,
													MessageConstants.COM_E_023);
											return;
										} else {
											// 成功
											Ext.Msg
													.alert(
															MessageConstants.SYS_REMIND_MSG,
															MessageConstants.COM_I_004,
															initPlan);
										}
									},
									failure : function() {
									}
								});

					}
				})
	}

	// 取消
	function cancelPlan() {
		Ext.Msg.confirm(Constants.BTN_CONFIRM, MessageConstants.COM_C_005,
				function(buttonobj) {
					if (buttonobj == 'yes') {
						texPlanDate.setValue(today);
						tempDate = texPlanDate.getValue();
						storePlan.baseParams = {
							dtePlanDate : texPlanDate.getValue()
						}
						storePlan.load({
									params : {
										start : 0,
										limit : Constants.PAGE_SIZE
									}
								});
						gridPlan.getView().refresh();
					}
				})
	}

	// 选择
	function selectMeal() {
		// 选中行
		var rec = gridMenu.getSelectionModel().getSelections();
		if (rec.length > 0) {
			// 有数据选中时
			for (i = 0; i < rec.length; i++) {
				var recordAdd = new recordPlan({
							id : "",
							menuCode : rec[i].data.menuCode == null
									? ""
									: rec[i].data.menuCode,
							menuName : rec[i].data.menuName == null
									? ""
									: rec[i].data.menuName,
							planDate : Ext.get('planDate').dom.value,
							retrieveCode : rec[i].data.retrieveCode == null
									? ""
									: rec[i].data.retrieveCode,
							menuType : CodeConstants.MEAL_TYPE_1,
							menuTypeCode : rec[i].data.menuTypeCode == null
									? ""
									: rec[i].data.menuTypeCode,
							menuTypeName : rec[i].data.menuTypeCode == null
									? ""
									: Ext.get('typeMenu').dom.value,
							menuPrice : rec[i].data.menuPrice == null
									? ""
									: rec[i].data.menuPrice,
							memo : "",
							updateTime : ""
						});
				storePlan.insert(storePlan.getCount(), recordAdd);
				storePlan.totalLength = storePlan.getTotalCount() + 1;
			}
//			storePlan.commitChanges();
//			gridPlan.getBottomToolbar().updateInfo();
			gridPlan.getView().refresh();
			win.hide();
		} else {
			// 提示信息
			Ext.Msg.confirm(Constants.BTN_CONFIRM,
					MessageConstants.AD003_C_001, function(buttonobj) {
						if (buttonobj == 'yes') {
						} else {
							win.hide();
						}
					})
		}
	}

	// 取消选择
	function cancelMeal() {
		Ext.Msg.confirm(Constants.BTN_CONFIRM, MessageConstants.COM_C_005,
				function(buttonobj) {
					if (buttonobj == 'yes') {
						win.hide();
					}
				})

	}

	// 日期初值
	function dateInit() {
		today = new Date();
		today = dateFormat(today);
	}

	function dateFormat(value) {
		var year;
		var month;
		var day;
		day = value.getDate();
		if (day < 10) {
			day = '0' + day;
		}
		month = value.getMonth() + 1;
		if (month < 10) {
			month = '0' + month;
		}
		year = value.getYear();
		value = year + "-" + month + "-" + day;
		return value;
	}

	// 两位数字处理
	function divide2(value) {
		if (value == null) {
			return "";
		}
		if (value == "") {
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

	// 用餐时间处理
	function typeFormat(value) {
		if (value == null) {
			return "";
		}
		var v = "";
		if (value == CodeConstants.MEAL_TYPE_1) {
			v = "早餐";
		}
		if (value == CodeConstants.MEAL_TYPE_2) {
			v = "中餐";
		}
		if (value == CodeConstants.MEAL_TYPE_3) {
			v = "晚餐";
		}
		if (value == CodeConstants.MEAL_TYPE_4) {
			v = "宵夜";
		}
		return v;
	}

	// 根据菜谱类别下拉框初始化grid
	function initGrid() {
		storeMenu.baseParams = {
			strMenutypeCode : drpMenu.getValue()
		}
		storeMenu.load({
					params : {
						start : 0,
						limit : Constants.PAGE_SIZE
					}
				});
	}

	// 初始化计划grid
	function initPlan() {
		storePlan.baseParams = {
			dtePlanDate : Ext.get('planDate').dom.value
		}
		storePlan.load({
					params : {
						start : 0,
						limit : Constants.PAGE_SIZE
					}
				});

		storePlanCopy.baseParams = {
			dtePlanDate : Ext.get('planDate').dom.value
		}
		storePlanCopy.load({
					params : {
						start : 0,
						limit : Constants.PAGE_SIZE
					}
				});
		gridPlan.getView().refresh();
		tempDate = Ext.get('planDate').dom.value
	}

	// 判断数据是否在DB中
	function isInDB() {
		var rec = gridPlan.getSelectionModel().getSelected();
		if (rec.get('id') == "") {
			drpMeal.setDisabled(false);
		} else {
			drpMeal.setDisabled(true);
		}
	}
	
	 /**
    * 显示备注窗口
    */
   function showMemoWin(){
   	    var record = gridPlan.getSelectionModel().getSelected();
   	    memoText.setValue(txtMemo.getValue());
        winMemo.show(record);
                
    }
		
   function drpSelect(){
   	   var recs = gridMenu.getSelectionModel().getSelections();
   	   if (recs.length == 0) {
   	   	    temp =  drpMenu.getValue();
   	   	    initGrid();
   	   } else {
   	   	    Ext.Msg.confirm(Constants.BTN_CONFIRM, MessageConstants.AD003_C_002,
				function(buttonobj) {
					if (buttonobj == 'yes') {
						temp =  drpMenu.getValue();
						initGrid();
					} else {
						drpMenu.setValue(temp);
					}
				})
   	   }
   } 
   
    // 餐厅计划是否改变
    function isPlanChanged() {
		if (storePlanCopy.getCount() != 0) {
			if (storePlanCopy.getCount() == storePlan.getCount()) {
				for (var i = 0; i < storePlanCopy.getCount(); i++) {
					// 判断有没有改变
					if (!(storePlan.getAt(i).get("menuPrice") == "" && storePlanCopy
							.getAt(i).get("menuPrice") == "")) {

						if (storePlan.getAt(i).get("menuPrice") != storePlanCopy
								.getAt(i).get("menuPrice")) {
							flag = true;
						}
					}
					if (!(storePlan.getAt(i).get("memo") == "" && storePlanCopy
							.getAt(i).get("memo") == "")) {

						if (storePlan.getAt(i).get("memo") != storePlanCopy
								.getAt(i).get("memo")) {
							flag = true;
						}
					}
				}
			} else {
                flag = true;
			}
		}
	}
});
