/**
 * 物料盘点调整
 * @author 黄维杰 081217
 */
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

function numberFormat(value){
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
			    if(sub.length > 3){
			    	sub = sub.substring(0,3);
			    }
		    }
            var r = /(\d+)(\d{3})/;
            while (r.test(whole)){
                whole = whole.replace(r, '$1' + ',' + '$2');
            }
            v = whole + sub;
            return v;	
	}
// 序列化为JSON字符串,不包括函数
Object.prototype.toJSONString = function() {
	var str = '';
	var obj = this;
	if (obj instanceof Array) {
		// 数组
		str = '[';
		for (var i = 0; i < obj.length; i++) {
			str += obj[i].toJSONString() + ',';
		}
		if (obj.length > 0) {
			// 去除最后的逗号
			str = str.slice(0, -1);
		}
		str += ']';
		return str;
	}

	str = '{';
	var sub = null;
	for (var prop in obj) {
		sub = obj[prop];
		if (sub == null || sub == undefined) {
			// 为NULL
			str += '"' + prop + '":null,';
		} else if (typeof sub == 'object') {
			if (sub instanceof Date) {
				// 转换时间格式
				str += '"' + prop + '":"' + renderDate(sub) + '",';
			} else {
				str += '"' + prop + '":' + sub.toJSONString() + ',';
			}
		} else if (typeof sub == 'number' || typeof sub == 'boolean') {
			// 布尔型或者数字
			str += '"' + prop + '":' + sub + ',';
		} else if (typeof sub != 'function') {
			str += '"' + prop + '":"' + sub + '",';
		}
	}
	if (str != '{') {
		// 去除最后的逗号
		str = str.slice(0, -1);
	}
	str += '}';
	return str;
}
Ext.onReady(function() {
	
	var strBookNo = "";
		
	function test(obj) {
		var temp = '';
		for(var prop in obj) {
			temp += prop + ': ' + obj[prop] + '\n'
		}
		alert(temp);
	}
	Ext.QuickTips.init();
	// 修改标志
	var modifyFlag = false;
	// 盘点单号
	var strBookId = "";

	//   //*****弹出窗口*******//
	var txtReasonPop = new Ext.form.TextArea({
				id : "reason",
				name : "reason",
				maxLength : 100
			});

	var win = new Ext.Window({
				width : 350,
				modal : true,
				height : 200,
				title : '详细信息录入窗口',
				buttonAlign : "center",
				items : [txtReasonPop],
				layout : 'fit',
				closeAction : 'hide',
				resizable : false,
				buttons : [{
					text : "保存",
					id : "back",
					handler : function() {
						if (rungrid.selModel.hasSelection()) {
							var records = rungrid.selModel.getSelections();
							var recordslen = records.length;
							// 提示选择了多项记录
							if (recordslen > 1) {
								Ext.Msg.alert(Constants.SYS_REMIND_MSG,
										Constants.SELECT_COMPLEX_MSG);
							} else {
								// 取得选中的记录
								var record = rungrid.getSelectionModel()
										.getSelected();
								if (!("" == txtReasonPop.getValue())) {
									if (txtReasonPop.getValue().length > 100) {
										return;
									} else {
										// 设置盈亏原因
										record.set("reason", txtReasonPop
														.getValue());
									}
								} else {
									// 设置盈亏原因为空
									record.set("reason", "");
								}
								win.hide();
							}
						} else {
							Ext.Msg.alert(Constants.SYS_REMIND_MSG,
									Constants.SELECT_NULL_UPDATE_MSG);
						}
					}
				},{
					text : "取消",
					id : "cancel",
					handler : function() {
						win.hide();
					}
				}]
			});

	//********** 主画面*******//
	// 盘点单号文本框
			
	    var checkNo = new Ext.form.TextField({
        id : "checkNo",
        maxLength : 30,
        readOnly : true,
        listeners : {
            focus : selectBookNo
        }
    }); 
     function selectBookNo() {
        this.blur();
        var mate = window.showModalDialog('bookNoSelect.jsp', window,'dialogWidth=200px;dialogHeight=300px;status=no');
        if (typeof(mate) != "undefined") {
            checkNo.setValue(mate.bookNo);
        }
    }
//	var checkNo = new Ext.form.TextField({
//				id : "checkNo",
//				name : "checkNo",
//		        maxLength : 30,
//				style :{
//		         'ime-mode' : 'disabled'
//		        },
//		        codeField : "yes"
//			});

	// 确认按钮
	var searchBtn = new Ext.Button({
				id : 'search',
				text : "查询",
				iconCls : Constants.CLS_QUERY
			});
	// 保存按钮
	var saveBtn = new Ext.Button({
				text : Constants.BTN_SAVE,
				id : "add",
				iconCls : Constants.CLS_SAVE
			});
	saveBtn.disable();

	// grid中的数据bean
	var rungridlist = new Ext.data.Record.create([{
				name : 'materialNo'
			}, {
				name : 'materialName'
			}, {
				name : 'specNo'
			}, {
				name : 'stockUmName'
			}, {
				name : 'whsName'
			}, {
				name : 'locationName'
			}, {
				name : 'lotNo'
			}, {
				name : 'bookQty'
			}, {
				name : 'physicalQty'
			}, {
				name : 'balance'
			}, {
				name : 'reason'
			}, {
				name : 'whsNo'
			}, {
				name : 'locationNo'
			}, {
				name : 'bookId'
			}, {
				name : 'bookDetailId'
			}, {
				name : 'materialId'
			}, {
				name : 'modifyDate'
			}]);

	// grid的store
	var searchStore = new Ext.data.JsonStore({
				url : 'resource/getBookCheckList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : rungridlist
			});
//	searchStore.setDefaultSort('materialNo', 'ASC');

	var mycolumn = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
						header : '行号',
						sortable : true,
						width : 35,
						align : 'left'
					}), {
				header : "物料编码",
				width : 70,
				sortable : true,
				dataIndex : 'materialNo'
			}, {
				header : "物料名称",
				width : 70,
				sortable : true,
				dataIndex : 'materialName'
			}, {
				header : "规格型号",
				width : 60,
				sortable : true,
				dataIndex : 'specNo'
			}, {
				header : "单位",
				width : 50,
				sortable : true,
				dataIndex : 'stockUmName'
			}, {
				header : "仓库",
				width : 70,
				sortable : true,
				dataIndex : 'whsName'
			}, {
				header : "库位",
				width : 70,
				sortable : true,
				dataIndex : 'locationName'
			}, {
				header : "批号",
				width : 50,
				sortable : true,
				dataIndex : 'lotNo'
			}, {
				header : "账面数量",
				id : 'recordQty',
				width : 90,
				sortable : true,
				dataIndex : 'bookQty',
				align : 'right',
				renderer : numberFormat
			}, {
				header : "实盘数量<font color='red'>*</font>",
				width : 90,
				dataIndex : 'physicalQty',
				sortable : true,
				renderer : function(value) {
					if ((("" == value || null == value))) {
						if ("0"==value) {
							return "0.00";
						}
//						return 0.00;
					} else {
						return numberFormat(value);
					}
				},
            	css:CSS_GRID_INPUT_COL,
				editor : new Ext.form.NumberField({
							maxValue : 99999999999.99,
							allowNegative : false,
							decimalPrecision : 2
						}),
				align : 'right'
			}, {
				header : "盈亏数量",
				id : 'result',
				width : 90,
				sortable : true,
				dataIndex : 'balance',
				align : 'right',
				renderer : function(value) {
					if ("" == value || null == value) {
						return "";
					} else {
						return numberFormat(value);
					}
				}
			}, {
				header : "差异原因",
				id : 'reason',
				width : 200,
				sortable : true,
            	css:CSS_GRID_INPUT_COL,
				editor : new Ext.form.TextArea({
					listeners:{
					    "render" : function() {
					        this.el.on("dblclick", function(){ 
					          var record = rungrid.getSelectionModel().getSelected();
                              var value = record.get('reason');
                              txtReasonPop.setValue(value);
                              win.show();
					        })
					    }
					}
				}),
				dataIndex : 'reason'
			}, {
				header : "仓库编号",
				id : 'whoNo',
				hidden : true,
				sortable : true,
				dataIndex : 'whsNo'
			}, {
				header : "库位编号",
				id : 'locationNo',
				hidden : true,
				sortable : true,
				dataIndex : 'locationNo'
			}, {
				header : "盘点单流水号",
				id : 'bookId',
				hidden : true,
				sortable : true,
				dataIndex : 'bookId'
			}, {
				header : "盘点明细表流水号",
				id : 'bookDetailId',
				hidden : true,
				sortable : true,
				dataIndex : 'bookDetailId'
			}, {
				header : "物料ID",
				id : 'materialId',
				hidden : true,
				sortable : true,
				dataIndex : 'materialId'
			}, {
				header : "修改时间",
				id : 'modifyDate',
				hidden : true,
				sortable : true,
				dataIndex : 'modifyDate'
			}]);

	mycolumn.isCellEditable = function(columnIndex, rowIndex) {
		if (11 == columnIndex) {
			return true;
		} else if (9 == columnIndex){
			return true;
		}
	};
		 // grid工具栏
    var gridTbar = new Ext.Toolbar({
    	height:25,
        items:["盘点单号<font color='red'>*</font>:", checkNo, '-',
						searchBtn, '-', saveBtn]
    })
	// 页面的Grid主体
	var rungrid = new Ext.grid.EditorGridPanel({
				id : 'rungrid',
				clicksToEdit : 1,
				store : searchStore,
				sm : new Ext.grid.RowSelectionModel({
							singleSelect : true
						}),
				cm : mycolumn,
				viewConfig : {
					forceFit : false
				},
				// 响应editGrid的编辑后事件，改变盈亏数量
				listeners : {
					// 编辑后触发事件
					afteredit : function(e) {
						var field = e.field;
						// 实盘数量字段
						if (field == "physicalQty") {
							var record = rungrid.getStore().getAt(e.row);
							// 获得账面数量
							var recordQty = record.get("bookQty");
							// 获得实盘数量
							var realQty = record.get("physicalQty");
							if (!("" == realQty || null == realQty) || "0" == realQty.toString()) {
								// 设置修改标志为已被修改
								modifyFlag = true;
								// 设置盈亏数量
								var balanceQty = parseFloat(realQty)
										- parseFloat(recordQty);
								record.set("balance", balanceQty.toFixed(2));
							} else {
								record.set("balance", "");
							}
						} else if (field == "reason") {
							var record = rungrid.getStore().getAt(e.row); // Get the Record
							// 获得账面数量
							var reason = record.get("reason");
							if (!("" == reason || null == reason)) {
								// 设置修改标志为已被修改
								modifyFlag = true;
							}
						}
					}
					,
					celldblclick : function(grid, row, column) {
                        if (11 == column) {
                        	  var record = grid.getStore().getAt(row);
                              var value = record.get('reason');
                              txtReasonPop.setValue(value);
                              win.show();
                              if(grid.activeEditor){
					          grid.activeEditor.completeEdit();
					        }
                        }
                    }
				},
				tbar : gridTbar,
				border : false,
				enableColumnHide : true,
				enableColumnMove : false
			});
	// 设定布局器及面板
	var layout = new Ext.Viewport({
				layout : 'fit',
				border : false,
				frame : true,
				items : [{
							layout : 'fit',
							border : false,
							items : [rungrid]
						}]
			});
			
	//***************************处理**************************//
	// 查询处理
	searchBtn.handler = function() {
		// 判断grid中的数据是否已被修改过，如是，
		// 则询问用户是否要保存所修改的数据
		if (true == modifyFlag) {
			Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_C_004, function(
					buttonobj) {
				// 用户选择“是”，即放弃修改过的数据，执行查询操作
				if (buttonobj == "yes") {
					// 做输入单号为空的check
					if (null == checkNo.getValue() || "" == checkNo.getValue()) {
						Ext.Msg.alert(Constants.SYS_REMIND_MSG, String.format(
										Constants.COM_E_002, "盘点单号"));
						return;
					} else {
						// 设置修改标志为false，即grid中的数据未被修改过
						modifyFlag = false;
						// 用输入的盘点单号到数据库中查找数据
						searchStore.baseParams = {
							bookNo : checkNo.getValue()
						};
						searchStore.load({
									// 查询后的回调函数，判断是否记录为空
									callback : function() {
										// 如果有记录，保存按钮可用
										if (searchStore.getCount() > 0) {
											saveBtn.enable();
											// 保存当前盘点单号
											strBookId = searchStore.getAt(0)
													.get("bookId");
													
											strBookNo = checkNo.getValue();
											// 没有记录，保存按钮不可用，且提醒用户没有数据
										} else {
											saveBtn.disable();
											Ext.Msg.alert(Constants.SYS_REMIND_MSG,
													Constants.COM_I_003);
										}
									}
								});
					}
				}
			})
		} else {
			// 做输入单号为空的check
			if (null == checkNo.getValue() || "" == checkNo.getValue()) {
				Ext.Msg.alert(Constants.SYS_REMIND_MSG, String.format(
								Constants.COM_E_002, "盘点单号"));
				return;
			} else {
				// 未被修改过，执行查询操作
				// 用输入的盘点单号到数据库中查找数据
				searchStore.baseParams = {
					bookNo : checkNo.getValue()
				};
				searchStore.load({
							// 查询后的回调函数，判断是否记录为空
							callback : function() {
								// 如果有记录，保存按钮可用
								if (searchStore.getCount() > 0) {
									saveBtn.enable();
									strBookNo = checkNo.getValue();
									
									// 没有记录，保存按钮不可用，且提醒用户没有数据
								} else {
									saveBtn.disable();
									Ext.Msg.alert(Constants.SYS_REMIND_MSG,
											Constants.COM_I_003);
								}
							}
						});
			}
		}
	}
	// 保存处理
	saveBtn.handler = function() {
		var sm = rungrid.getSelectionModel();
		var selected = sm.getSelections();
		var saveInfo = [];
		var phy = "";
		for (var i = 0; i < searchStore.getCount(); i++) {
			phy = searchStore.getAt(i).get("physicalQty");
			if ((null == phy || "" == phy)&&"0"!=phy) {
				Ext.Msg.alert(Constants.SYS_REMIND_MSG, String.format(
								Constants.COM_E_002, "实盘数量"));
				return;
			}
			saveInfo.push(searchStore.getAt(i).data);
		}
		strBookId = searchStore.getAt(0).get("bookId");
		if (saveInfo.length > 0) {
			// 确认是否要保存
			Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_C_001, function(
							buttonobj) {
						if (buttonobj == "yes") {
							// 保存
							Ext.Ajax.request({
										method : Constants.POST,
										url : 'resource/updateBookCheckList.action',
										success : function(result, request) {
											// 成功，显示修改成功信息
											var o = eval("("
													+ result.responseText + ")");
											// 正在结账
											if (o.msg == "on") {
												Ext.Msg.alert(Constants.SYS_REMIND_MSG,
														Constants.COM_E_001);
											// 排他异常
											} else if(o.msg == "USING") {
												Ext.Msg.alert(Constants.SYS_REMIND_MSG,
														Constants.COM_E_015);
											} else if(o.msg == "sql") {
												Ext.Msg.alert(Constants.SYS_REMIND_MSG,
														Constants.COM_E_014);
											} else {
												// 保存成功
												Ext.Msg.alert(Constants.SYS_REMIND_MSG,
														Constants.COM_I_004);
												// 更新信息
												searchStore.baseParams = {
													bookNo : strBookId
												};
												searchStore.load();
												// 把盘点单号设为空
												checkNo.setValue("");
												// 设置修改标志为false，即grid中的数据未被修改过
												modifyFlag = false;
												// 保存按钮设为disable
												saveBtn.disable();
											}
										},
										failure : function() {
											Ext.Msg.alert(Constants.SYS_REMIND_MSG,
													Constants.UNKNOWN_ERR);
										},
										params : {
											bookNo :  strBookNo,
											saveInfo : saveInfo.toJSONString()
										}
									});
						}
					})
		}
	}
});