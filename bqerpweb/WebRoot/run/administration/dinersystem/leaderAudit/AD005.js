// 值长审核
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {
	// 订餐日期声明
	var menuDate = new Date();
	menuDate = menuDate.format('Y-m-d');
	// 订餐日期
	var txtMenuDate = new Ext.form.TextField({
		id : 'menuDate',
		name : 'strMenuDate',
		hiddenName : 'strMenuDate',
		isFormField : true,
		style : 'cursor:pointer',
		value : menuDate,
		width : 120,
		readOnly : true,
		listeners : {
			focus : function() {
	            WdatePicker({
					startDate : '%y-%M-%d',
					dateFmt : 'yyyy-MM-dd',
					alwaysUseStartDate : false,
					isShowClear : false
				});
			}
		}
	});
	// 订餐类别
	var drpMenuType = new Ext.form.CmbMealType({
		id : 'menuType',
		width : 120,
		name : 'strMenuType',
		isFormField : true,
		hiddenName : 'strMenuType',
		value : '1'
	});
	// 订餐日期,订餐类别变量定义，供打印使用
	var hidMenuDate = "";
	var hidMenuType = "";
	// 是否是查询操作
	var queryFlag = false;
   
	// 数据源--------------------------------
	var queryStore = new Ext.data.JsonStore({
		url : 'administration/getLeaderAuditInfoList.action',
		root : 'list',
		totalProperty : 'totalCount',
		fields : [{
				name : 'menuId'
			}, {
				name : 'menuHidId'
			}, {
				name : 'menuType'
			}, {
				name : 'workerName'
			}, {
				name : 'depName'
			}, {
				name : 'insertDate'
			}, {
				name : 'place'
			}, {
				name : 'menuName'
			}, {
				name : 'menuTypeName'
			}, {
				name : 'menuAmount'
			}, {
				name : 'menuPrice'
		    }, {
				name : 'memo'
		    }, {
		        name : 'updateTime'
		    }],
	   listeners : {
			loadexception : function(ds, records, o) {
				var result = eval("(" + o.responseText + ")");
				var succ = result.msg;
				if (succ == Constants.SQL_FAILURE) {
					Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
							MessageConstants.COM_E_014);
				} else if (succ == Constants.IO_FAILURE) {
					Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
							MessageConstants.COM_E_022);
				} else if (succ == Constants.DATE_FAILURE){
					Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
							MessageConstants.COM_E_023);
				}
			}

		}
	});
    
	// --gridpanel显示格式定义-----开始-------------------
	// 按钮5个
	var btnQuery = new Ext.Button({
		text : Constants.BTN_QUERY,
		iconCls : Constants.CLS_QUERY,
		handler : queryRecord
	})
    var btnBack = new Ext.Button({
		text : Constants.BTN_UNTREAD,
		iconCls : Constants.CLS_UNTREAD,
		disabled : false,
		handler : backRecord
	})
	var btnAudit = new Ext.Button({
		text : Constants.BTN_APPROVE,
		iconCls : Constants.CLS_APPROVE,
		disabled : false,
		handler : auditRecord
	})
	var btnPrint = new Ext.Button({
		text : Constants.BTN_PRINT,
		iconCls : Constants.CLS_PRINT,
		disabled : true,
		handler : printRecord
	})
	var btnExport = new Ext.Button({
		text : Constants.BTN_EXPORT,
		iconCls : Constants.CLS_EXPORT,
		disabled : true,
		handler : exportRecord
	})
    

	var gridTbar = new Ext.Toolbar({
		border:false,
		height:25,
		items:[ "订餐日期:", txtMenuDate,"-" ,"订餐类别:", drpMenuType, '-', btnQuery, btnBack, btnAudit, 
		        btnPrint, btnExport]
	});

	queryStore.on("load", function() {
		if (queryStore.getCount() > 0) {
			btnExport.setDisabled(false);
			btnPrint.setDisabled(false);
//			btnAudit.setDisabled(false);
//			btnBack.setDisabled(false);
		}else {
		    btnExport.setDisabled(true);
		    btnPrint.setDisabled(true);
//		    btnAudit.setDisabled(true);
//		    btnBack.setDisabled(true);
		    // 没有检索到任何信息(仅在查询处理后显示)
		    if(queryFlag){
			    Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
						MessageConstants.COM_I_003);
		    }
		}
	});

	// 定义选择列
	var sm = new Ext.grid.RowSelectionModel({
		singleSelect : true
	});
	var grid = new Ext.grid.GridPanel({
		store : queryStore,
		columns : [
		// 自动行号
		new Ext.grid.RowNumberer({
			header : "行号",
			width : 31
		}), {
		    header : '订单号',
			width : 50,
			sortable : false,
//			align : 'right',
			dataIndex : 'menuId'
		}, {
			header : "订餐类别",
			sortable : false,
			renderer : menuTypeFormat,
			dataIndex : 'menuType'
		}, {
			header : "订餐人",
			sortable : false,
			dataIndex : 'workerName'
		}, {
			header : "所属部门",
			sortable : false,
			dataIndex : 'depName',
			width : 200
		}, {
			header : "填单日期",
			sortable : false,
			dataIndex : 'insertDate'
		}, {
		    header : "就餐地点",
			sortable : false,
			renderer : placeFormat,
			dataIndex : 'place'
		},{
			header : "菜谱名称",
			sortable : false,
			dataIndex : 'menuName'
		}, {
			header : "菜谱类别",
			sortable : false,
			dataIndex : 'menuTypeName'
		}, {
			header : "份数",
			sortable : false,
			align : 'right',
			dataIndex : 'menuAmount',
			width : 50
		}, {
			header : "单价",
			sortable : false,
			align : 'right',
			renderer : numberFormat,
			dataIndex : 'menuPrice'
		}, {
			header : "备注",
			sortable : false,
			dataIndex : 'memo',
			width : 180
		}, {
		    header : '修改时间',
			hidden : true,
			dataIndex : 'updateTime'
		}, {
			header : '订单号',
			hidden : true,
			dataIndex : 'menuHidId'
		}],
		sm : sm,
		autoScroll : true,
		enableColumnMove : false,
		autoSizeColumns : true,
		viewConfig : {
			forceFit : false
		},
		// 头部工具栏
		tbar : gridTbar
	});
	// 注册双击事件
 	//grid.on("rowdblclick", printRecord);
	// --gridpanel显示格式定义-----结束--------------------
	grid.on('celldblclick',cellDbClick);  
	// 备注-弹出窗口
    var memoText = new Ext.form.TextArea({
                id : "memoText",
                maxLength : 127,
                readOnly :true,
                width : 180
            });		
    // 弹出画面
    var win = new Ext.Window({
                height : 170,
                width : 350,
                layout : 'fit',
                resizable : false,
                modal : true,
                closeAction : 'hide',
                items : [memoText],
                buttonAlign : "center",
                title : '详细信息查看窗口',
                buttons : [{
                    text : Constants.BTN_CLOSE,
                    iconCls : Constants.CLS_CANCEL,
                    handler : function() {
                        win.hide();
                    }
                }]
            });
	
    new Ext.Viewport({
		enableTabScroll : true,
		layout : 'fit',
		items : [grid]
	});
	
	// 函数处理
	/**
	 * 单元格双击处理函数
	 */
	function cellDbClick(grid, rowIndex, columnIndex, e){
	       if(rowIndex < queryStore.getCount()){
	       	if(columnIndex == 11){
		       memoText.setValue(queryStore.getAt(rowIndex).get('memo'));
		       win.show();
		     }
	       }
		    
	
	}
    /**
     * 查询处理
     */
	function queryRecord() {
		queryFlag = true;
		// 订餐日期
    	hidMenuDate = txtMenuDate.getValue();
    	// 订餐类别code
    	hidMenuType = drpMenuType.getValue();
	    queryStore.load({
				params : {
					menuDate : txtMenuDate.getValue(),
					menuType : drpMenuType.getValue()
				}
			});
	}
	
	/**
	 * 退回处理
	 */
	function backRecord() {
		upDateRecord(true);
	}
	/**
	 * 审核处理
	 */
	function auditRecord() {
		upDateRecord(false);
	}
	/**
	 * 审核处理or退回处理
	 */
	function upDateRecord(isBack) {
		// action 地址
		var url = "";
		var urlBack = 'administration/doBackRecord.action';
		var urlAudit = 'administration/doAuditRecord.action';
		// 提示信息
		var msgRemind = "";
		// 确认信息
		var msgOk = "";
		if(isBack){
			url = urlBack;
			msgRemind = MessageConstants.AD005_C_001;
			msgOk = MessageConstants.AD005_C_002;
		}else {
			url = urlAudit;
			msgRemind = MessageConstants.AD005_C_003;
			msgOk = MessageConstants.AD005_C_004;
		}
		// 是否有被选项
		if (grid.selModel.hasSelection()) {
			var record = grid.getSelectionModel().getSelected();
			// 订单号
		    var menuId = record.get('menuHidId');
		    // 上次修改时间
		    var updateTime = record.get('updateTime');
			// 是否退回/审核
			Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG, String.format(msgRemind,menuId), function(
			        buttonobj) {
				if (buttonobj == "yes") {
			        Ext.lib.Ajax.request(Constants.POST,url, {
					    success : function(action) {
					    	var result = eval("("
											+ action.responseText + ")");
	                        if (result.msg == Constants.DATA_USING) {
								// 排他错误信息
								Ext.Msg.alert(
										MessageConstants.SYS_ERROR_MSG,
										MessageConstants.COM_I_002);
								return;
							}
							if (result.msg == Constants.SQL_FAILURE) {
								// SQL执行错误
								Ext.Msg.alert(
										MessageConstants.SYS_ERROR_MSG,
										MessageConstants.COM_E_014);
								return;
							}
							// 成功
							Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
										String.format(msgOk,menuId));
								
							// 重新加载数据
							queryFlag = false;
							// 订餐日期
					    	hidMenuDate = txtMenuDate.getValue();
					    	// 订餐类别code
					    	hidMenuType = drpMenuType.getValue();
						    queryStore.load({
									params : {
										menuDate : txtMenuDate.getValue(),
										menuType : drpMenuType.getValue()
									}
								});
						}
				    }, "menuId=" + menuId
					    +"&updateTime=" + updateTime);
				}
			})
		}else {
		    Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
					MessageConstants.COM_I_001);
		}
	}
	/**
	 * 导出处理
	 */
	function exportRecord() {
		Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG, MessageConstants.COM_C_007, function(
			    buttonobj) {
			if (buttonobj == "yes") {
			    document.all.blankFrame.src = "administration/exportLeaderAuditInfoFile.action";
			}
		})
	}
	/**
	 *  打印处理
	 */
    function printRecord() {
        //调出值长审核订餐信息表，传递参数定：订餐日期、订餐类别code
        window.open("/power/report/webfile/administration/ARF03.jsp?menuType="+hidMenuType+"&menuDate="+hidMenuDate);
    }

    /**
	 * 大数字中间用','分隔 小数点后3位
	 */
	function numberFormat(value){
		value = value*1;
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
        return v + "元";
	}
	/**
     * 订餐类型初始化
     */
    function menuTypeFormat(value){
    	if(value == CodeConstants.MEAL_TYPE_1){
    		value = "早餐";
    	}else if(value == CodeConstants.MEAL_TYPE_2){
    		value = "中餐";
    	}else if(value == CodeConstants.MEAL_TYPE_3){
    		value = "晚餐";
    	}else if(value == CodeConstants.MEAL_TYPE_4){
    		value = "宵夜";
    	}
    	return value;
    }
    /**
     * 就餐地点初始化
     */
    function placeFormat(value){
    	if(value == CodeConstants.MEAL_PLACE_1){
    		value = "餐厅";
    	}else if(value == CodeConstants.MEAL_PLACE_2){
    		value = "工作地";
    	}
    	return value;
    }
})