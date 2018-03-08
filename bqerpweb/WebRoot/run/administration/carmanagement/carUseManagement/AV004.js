Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {
	// 系统当天日期
	var dteFrom = new Date();
	var dteTo = new Date();
	// 系统当天前15天的日期
	dteFrom.setDate(dteFrom.getDate() - 15);
	dteFrom = dteFrom.format('Y-m-d');
	// 系统当天后15天的日期
	dteTo.setDate(dteTo.getDate());
	dteTo = dteTo.format('Y-m-d');
	// 定义查询起始时间
	var startDate = new Ext.form.TextField({
				id : 'startDate',
				name : 'startDate',
				style : 'cursor:pointer',
				value : dteFrom,
				width : 80,
				readOnly : true,
				listeners : {
					focus : function() {
						WdatePicker({
									isShowClear : false,
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd',
									alwaysUseStartDate : false
								});
					}
				}
			});
	// 定义查询结束时间
	var endDate = new Ext.form.TextField({
				id : 'endDate',
				name : 'endDate',
				style : 'cursor:pointer',
				width : 80,
				value : dteTo,
				readOnly : true,
				listeners : {
					focus : function() {
						WdatePicker({
									isShowClear : false,
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd',
									alwaysUseStartDate : false
								});
					}
				}
			});
	// 用车部门
	var txtDepName = new Ext.form.TextField({
				id : "DepName",
				name : "DepName",
				readOnly : true,
				width : 100
			});
	txtDepName.onClick(selectDep);
	// 用车申请人
	var drpName = new Ext.form.CmbWorkerByDept({
				id : "strName",
				name : "strName",
				fieldLabel : "申请人",
				maxLength : 15,
				width : 80
			});
	// 司机
	var drpDriver = new Ext.form.CmbDriver({
				id : 'Driver',
				width : 80
			});
	drpDriver.store.load();
	// 出车状态数据源
	var dataStatus = [ {
				value : '',
				text : ''
			},
		    {
				value : CodeConstants.USE_CAR_1,
				text : '未出车'
			}, {
				value : CodeConstants.USE_CAR_2,
				text : '已出车'
			}
			];
	// 出车状态下拉框
	var drpCarStatus = new Ext.form.ComboBox({
				fieldLabel : "出车状态",
				id : 'CarStatus',
				store : new Ext.data.JsonStore({
							fields : ['value', 'text'],
							data : dataStatus
						}),
				emptyText : '',
				mode : 'local',
				readOnly : true,
				triggerAction : 'all',
				valueField : 'value',
				displayField : 'text',
				width : 70
			});
	// 部门编码隐藏
	var hdnDepCode = new Ext.form.Hidden({
				value : ""
			});
	// 查询按钮
	var btnQuery = new Ext.Button({
				text : Constants.BTN_QUERY,
				iconCls : Constants.CLS_QUERY,
				handler : queryHandler
			});
	// 明细按钮
	var btnOver = new Ext.Button({
				text : Constants.BTN_DETAIL,
			    iconCls : Constants.CLS_DETAIL,
				handler : overHandler
			});
	// 删除按钮
	var btnDelete = new Ext.Button({
				text : Constants.BTN_DELETE,
				iconCls : Constants.CLS_DELETE,
				handler : deleteHandler
			});
	btnDelete.setDisabled(false);
	// 派车单按钮
	var btnform = new Ext.Button({
				text : '派车单',
				iconCls : Constants.CLS_PRINT,
				handler : formHandler
			});
	// 类定义recordCarGridList
	var recordCar = new Ext.data.Record.create([{
				name : 'id'
			}, {
				name : 'reqName'
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
			},{
				name : 'driverCode'
			}, {
				name : 'driverName'
			}, {
				name : 'deptName'
			}, {
				name : 'updateTime'
			}, {
				name : 'carFileUpdateTime'
			}, {
				name : 'startTime'
			}, {
				name : 'endTime'
			}, {
				name : 'goMile'
			}, {
				name : 'carId'
			}, {
				name : 'carNo'
			}, {
				name : 'comeMile'
			}, {
				name : 'useOil'
			}, {
				name : 'lqPay'
			}, {
				name : 'distance'
			}, {
				name : 'carApplyId'
			}
//			, {
//				name : 'isUse'
//			}
			]);
	// 定义获取数据源
	var storeCar = new Ext.data.JsonStore({
				url : 'administration/getCarApplyBy.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : recordCar
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
	// 定义grid的ColumnModel
	var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
						header : "行号",
						width : 35
					}), {
				header : "申请单号",
				width : 100,
				sortable : true,
				dataIndex : 'carApplyId'
			},{
                header : "申请人",
                width : 150,
                sortable : true,
                dataIndex : 'reqName'
            },{
				header : "用车部门",
				width : 150,
				sortable : true,
				dataIndex : 'deptName'
			}, {
				header : "到达地点",
				width : 150,
				sortable : true,
				dataIndex : 'aim'
			}, {
				header : "司机",
				width : 100,
				sortable : true,
				dataIndex : 'driverName'
			}, {
				header : "用车日期",
				width : 100,
				sortable : true,
				dataIndex : 'useDate'
			}, {
				header : "发车时间",
				width : 100,
				sortable : true,
				dataIndex : 'startTime'
			}
//			, {
//				header : "收车时间",
//				width : 100,
//				sortable : true,
//				dataIndex : 'endTime'
//			}
			]);
	var headTbar = new Ext.Toolbar({
		region : 'north',
    	border:false,
        height:25,
        items:['用车日期: ', '从', startDate, '到', endDate, '-', '用车部门: ',
						txtDepName, '-', '申请人: ', drpName,'司机: ',drpDriver, '-', '出车状态: ', drpCarStatus]
    });
    var gridTbar = new Ext.Toolbar({
		border:false,
		height:25,
		items:[ btnQuery,btnOver, btnDelete, btnform, hdnDepCode]
	});
	// --gridpanel显示格式定义-----开始-------------------
	var grid = new Ext.grid.GridPanel({
		        region : "center",
				store : storeCar,
				cm : cm,
				sm : new Ext.grid.RowSelectionModel({
							singleSelect : true
						}),
				// 顶部工具栏
				tbar : gridTbar,
				// 底部工具栏
				bbar : new Ext.PagingToolbar({
							pageSize : Constants.PAGE_SIZE,
							store : storeCar,
							displayInfo : true,
							displayMsg : MessageConstants.DISPLAY_MSG,
							emptyMsg : MessageConstants.EMPTY_MSG
						}),
				// 不允许移动列
		        enableColumnMove : false,
		        autoScroll : true,
		        autoSizeColumns : true,		
				viewConfig : {
					forceFit : false
				}
			});
	// grid单击选择
	grid.on('rowclick',statusHandler);
	// grid双击选择
	grid.on('rowdblclick',overHandler);


	// --gridpanel显示格式定义-----结束-------------------
	// 页面加载显示数据
	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				items : [headTbar,grid]
			});

	// ↑↑********弹出窗口*********↑↑//
	var width = 100;
	// 申请单号
	var txtApplyId = new Ext.form.TextField({
				id : "carApplyId",
				name : "entity.carApplyId",
				isFormField : true,
				fieldLabel : "申请单号",
				width : width,
				readOnly : true,
				anchor : '95%'
			});
	// 隐藏流水号
	var hideId = new Ext.form.Hidden({
				id : "id",
				name : "entity.id",
				allowBlank : false,
				width : width,
				readOnly : true,
				maxLength : 25,
				anchor : '95%'
			});		
	// 时间隐藏域
	// 用车申请时间隐藏域
	var hideCarAppUpdateTime = new Ext.form.Hidden({
	       id : 'updateTime',
	      name : 'entity.updateTime'		
	});
	// 车辆档案时间隐藏域
	var hideCarFileUpdateTime = new Ext.form.Hidden({
	       id : 'carFileUpdateTime',
	       name : 'entity.carFileUpdateTime'
	});
	// 第0行
	var ZeroLine = new Ext.Panel({
		border : false,
		width : 430,
		layout : "column",
		style : "padding-top:5px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
		items : [{
					columnWidth : 0.46,
					layout : "form",
					border : false,
					labelWidth : 76,
					items : [txtApplyId,hideId,hideCarAppUpdateTime,hideCarFileUpdateTime]
				}, {
					columnWidth : 0.5,
					layout : "form",
					border : false
				}]
	});
	// firstfldSet相关控件
	// 申请人
	var txtName = new Ext.form.TextField({
		fieldLabel : "申请人",
		id : "reqName",
		isFormField : true,
		readOnly : true,
		width : width,
		anchor : '95%'
			});
	// 用车部门
	var txtDeptName = new Ext.form.TextField({
	    fieldLabel : "用车部门",
	    readOnly : true,
	    width : width,
	    id : "deptName",
		anchor : '95%'
	});
	// 第一行
	var firstLine = new Ext.Panel({
		border : false,
		width : 430,
		layout : "column",
		style : "padding-top:5px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
		items : [{
					columnWidth : 0.46,
					layout : "form",
					border : false,
					items : [txtName]
				}, {
					columnWidth : 0.46,
					layout : "form",
					border : false,
					items : [txtDeptName]
				}]
	});
	// 用车人数
	var txtUseNum = new Ext.form.TextField({
		fieldLabel : "用车人数",
		style : 'text-align:right',
		id : 'useNum',
		width : width,
		readOnly : true,
		anchor : '95%'
	})

	// 用车日期
	var tfdUseDate = new Ext.form.TextField({
	    fieldLabel : "用车日期",
	    readOnly : true,
	    width : width,
	    id : "useDate",
		anchor : '95%'
	});
	// 第二行
	var secondLine = new Ext.Panel({
		border : false,
		width : 430,
		layout : "column",
		style : "padding-top:5px;padding-right:0px;padding-left:0px;padding-bottom:0px;margin-bottom:0px",
		items : [{ // 用车人数
			columnWidth : 0.46,
			layout : "form",
			border : false,
			items : [txtUseNum]
		}, {	// 用车日期
					columnWidth : 0.46,
					layout : "form",
					border : false,
					items : [tfdUseDate]
				}]
	});
	// 是否出省
    var txtIfOut = new Ext.form.TextField({
            fieldLabel : "是否出省",
            id :'ifOut',
            readOnly : true,
			width : width,
			anchor : '95%'
    });
	// 用车天数
	var txtUseDays = new Ext.form.TextField({
	    fieldLabel : "用车天数",
		id : "useDays",
		style : 'text-align:right',
		readOnly : true,
		renderer : divide2,
		width : width,
		//appendChar : "天",
		anchor : '95%'
	});
			
	
	
	// 第三行
	var thirdLine = new Ext.Panel({
		border : false,
		width : 430,
		layout : "column",
		style : "padding-top:5px;padding-right:0px;padding-left:0px;padding-bottom:0px;margin-bottom:0px",
		items : [{ // 是否出省
			columnWidth : 0.46,
			layout : "form",
			border : false,
			items : [txtIfOut]
		}, {	// 用车天数
					columnWidth : 0.46,
					layout : "form",
					border : false,
					items : [txtUseDays]
				}]
	});
	// 到达地点
	var txtAim = new Ext.form.TextField({
				fieldLabel : "到达地点",
				id : "aim",
				maxLength : 50,
				readOnly : true,
				anchor : '95%'
			});
	// 第四行
	var fourLine = new Ext.Panel({
		border : false,
		width : 430,
		layout : "column",
		style : "padding-top:5px;padding-right:0px;padding-left:0px;padding-bottom:0px;margin-bottom:0px",
		items : [{ // 到达地点
			columnWidth : 0.943,
			layout : "form",
			border : false,
			items : [txtAim]
		}]
	});
	// 用车事由
	var txaReason = new Ext.form.TextArea({
				fieldLabel : "用车事由",
				id : "reason",
				maxLength : 127,
				height : 50,
				readOnly : true,
				anchor : '95%'
			});
	// 第五行
	var fifthLine = new Ext.Panel({
		border : false,
		width : 430,
		layout : "column",
		style : "padding-top:5px;padding-right:0px;padding-left:0px;padding-bottom:0px;margin-bottom:0px",
		items : [{ // 用车事由
			columnWidth : 0.94,
			layout : "form",
			border : false,
			items : [txaReason]
		}]
	});
	// 申请内容field
	var firstfldSet = new Ext.form.FieldSet({
				title : '申请内容',
				labelAlign : 'right',
				id : 'firstfld',
				items : [firstLine, secondLine, thirdLine, fourLine, fifthLine]
			});
	// secondfldSet相关控件
	// 车牌号
	var txtCarNo = new Ext.form.TextField({
				id : "carNo",
				fieldLabel : '车牌号<font color="red">*</font>',
				readOnly : true,
				allowBlank : false,
				width : width,
				anchor : "95%",
				name : 'entity.carNo'
			});
	txtCarNo.onClick(popupSelect);
	// 车id
	var hideCarId = new Ext.form.Hidden({
	     id : "carId",
		 name :"entity.carId"
	})
	// 司机
	var drpFormDriver = new Ext.form.CmbDriver({
		        fieldLabel :'司机',
				id : 'formDriver',
				width : width,
				anchor : '95%'
			});
	drpFormDriver.store.load();
	var hideDriverCode = new Ext.form.Hidden({
	     id : "hideFormDriver",
	     name :"entity.driverCode"
	})
	// 第一行
	var SfirstLine = new Ext.Panel({
		border : false,
		width : 430,
		layout : "column",
		style : "padding-top:5px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
		items : [{
					columnWidth : 0.46,
					layout : "form",
					border : false,
					items : [txtCarNo,hideCarId]
				}, {
					columnWidth : 0.46,
					layout : "form",
					border : false,
					items : [drpFormDriver,hideDriverCode]
				}]
	});
	// 发车时间
	var txtStartTime = new Ext.form.TextField({
				format : 'Y-m-d',
				fieldLabel : "发车时间<font color='red'>*</font>",
				itemCls : 'sex-left',
				readOnly : true,
				clearCls : 'allow-float',
				width : width,
				id : "startTime",
				anchor : '95%',
				style : 'cursor:pointer',
				listeners : {
					focus : function() {
						WdatePicker({
							        isShowClear : false,
							        // 时间格式
									startDate : '%y-%M-%d 00:00',
									dateFmt : 'yyyy-MM-dd HH:mm',
									onpicked : function() {
										txtStartTime.allowBlank = false;
										//timeCompare();
									}
								});
					}
				}
				
			});	
	var hideStartTime = new Ext.form.Hidden({
		id : 'hideStartTime',
		name : "entity.startTime"
	}); 
	// 发车里程
	var txtGoMile = new Ext.form.MoneyField({
				id : "goMile",
				fieldLabel : "发车里程<font color='red'>*</font>",
				allowNegative : false,
				width : width,
				allowBlank : false,
				style : 'text-align:right',
				isFormField : true,
				appendChar : "公里",
				padding : 2,
				minValue : "0.00",
				maxValue : "9999999999999.99",
				maxLength : 16,
				anchor : '95%'
			});			
	var hideGoMile = new Ext.form.Hidden({
		id : "hideGoMile",
		name : "entity.goMile"
	});
	var SsecondLine = new Ext.Panel({
		border : false,
		width : 430,
		layout : "column",
		style : "padding-top:5px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
		items : [{
					columnWidth : 0.46,
					layout : "form",
					border : false,
					items : [txtStartTime,hideStartTime]
				}, {
					columnWidth : 0.46,
					layout : "form",
					border : false,
					items : [txtGoMile,hideGoMile]
				}]
	});

	// 发车前field
	var SecondfldSet = new Ext.form.FieldSet({
				title : '发车前',
				id : 'Secondfld',
				items : [SfirstLine, SsecondLine]
			});
	// thirdfldSet相关控件
	// 收车时间
	var txtEndTime = new Ext.form.TextField({
				format : 'Y-m-d',
				fieldLabel : "收车时间<font color='red'>*</font>",
				itemCls : 'sex-left',
				isFormField : true,
				clearCls : 'allow-float',
				readOnly : true,
				checked : true,
				allowBlank : false,
				width : width,
				id : "endTime",
				anchor : '95%',
				style : 'cursor:pointer',
				listeners : {
					focus : function() {
						WdatePicker({
							        isShowClear : true,
							        // 时间格式
									startDate : '%y-%M-%d 00:00',
									dateFmt : 'yyyy-MM-dd HH:mm',
									onpicked : function() {
										txtEndTime.clearInvalid();
										checkAllBlank();
										txtEndTime.clearInvalid();
									},
									oncleared : function() {
										
										checkAllBlank();
									}
								});

					}

				}

			});
	txtEndTime.on('blur', checkAllBlank);
	var hideEndTime = new Ext.form.Hidden({
		id : 'hideEndTime',
		name : "entity.endTime"
	}); 
	
	// 收车里程
	var txtComeMile = new Ext.form.MoneyField({
				id : "comeMile",
				fieldLabel : "收车里程<font color='red'>*</font>",
				allowNegative : false,
				width : width,
				allowBlank : false,
				isFormField : true,
				style : 'text-align:right',
				padding : 2,
				appendChar : "公里",
				maxValue : "9999999999999.99",
				maxLength : 16,
				anchor : '95%'
			});
	txtComeMile.on("blur", checkAllBlank);
	var hideComeMile = new Ext.form.Hidden({
		id : "hideComeMile",
		name : "entity.comeMile"
	});
	var TfirstLine = new Ext.Panel({
		border : false,
		width : 430,
		layout : "column",
		style : "padding-top:5px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
		items : [{
					columnWidth : 0.46,
					layout : "form",
					border : false,
					items : [txtEndTime,hideEndTime]
				}, {
					columnWidth : 0.46,
					layout : "form",
					border : false,
					items : [txtComeMile,hideComeMile]
				}]
	});
	// 油费
	var txtUseOil = new Ext.form.MoneyField({
				id : "useOil",
				fieldLabel : "油费",
				width : width,
				style : 'text-align:right',
				maxValue : "999999999999.99",
				appendChar : "元",
				padding : 2,
				anchor : '95%'
			});
	txtUseOil.on("blur", checkAllBlank);
	var hideUseOil = new Ext.form.Hidden({
	   name : "entity.useOil"
	});
    var txtLqPay = new Ext.form.MoneyField({
				id : "lqPay",
				fieldLabel : "路桥费",
				width : width,
				style : 'text-align:right',
				maxValue : "999999999999.99",
				appendChar : "元",
				padding : 2,
				anchor : '95%'
			});
	txtLqPay.on("blur", checkAllBlank);
	var hideLqPay = new Ext.form.Hidden({
		name : "entity.lqPay"
	});
	var TsecondLine = new Ext.Panel({
		border : false,
		width : 430,
		layout : "column",
		style : "padding-top:5px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
		items : [{
					columnWidth : 0.46,
					layout : "form",
					border : false,
					items : [txtUseOil,hideUseOil]
				}, {
					columnWidth : 0.46,
					layout : "form",
					border : false,
					items : [txtLqPay,hideLqPay]
				}]
	});
	// 行车里程
	var txtDistance = new Ext.form.MoneyField({
				id : "distance",
				fieldLabel : "行车里程",
				allowNegative : false,
				width : width,
				readOnly :true,
				isFormField : true,
				style : 'text-align:right',
				appendChar : "公里",
				padding : 2,
				maxValue : "9999999999999.99",
				maxLength : 16,
				anchor : '95%'
			});
    txtDistance.on("blur", checkAllBlank);
			
	
	var hideDistance = new Ext.form.Hidden({
		id : "hideDistance",
		name : "entity.distance"
	});
	// 发车里程change
	txtGoMile.on('change',goMileChange);
     function goMileChange(t, n, o) {
		if (!txtComeMile.getValue()) {
			txtDistance.setValue('');
		} else {
			txtDistance.setValue(txtComeMile.getValue() - txtGoMile.getValue());
		}
	}
	// 收车里程change
	txtComeMile.on('change',comeMileChange);
	 function comeMileChange(t, n, o){
	 	if (!txtComeMile.getValue()) {
			txtDistance.setValue('');
		}
		else {
			txtDistance.setValue(txtComeMile.getValue() - txtGoMile.getValue());
		}
	 }
	 	
			
	var TthirdLine = new Ext.Panel({
		border : false,
		width : 430,
		layout : "column",
		style : "padding-top:5px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
		items : [{
					columnWidth : 0.46,
					layout : "form",
					border : false,
					items : [txtDistance,hideDistance]
				}, {
					columnWidth : 0.46,
					layout : "form",
					border : false
				}]
	});
	// 收车后field
	var ThirdfldSet = new Ext.form.FieldSet({
				title : '收车后',
				id : 'Thirdfld',
				items : [TfirstLine,TsecondLine, TthirdLine]
			});
	// 定义弹出窗体中的form
	var uppanel = new Ext.form.FormPanel({
				id : "form",
				labelAlign : 'right',
				autoHeight : true,
				autoScroll : true,
				labelWidth : 65,
				width : 468,
				frame : true,
				items : [ZeroLine, firstfldSet, SecondfldSet, ThirdfldSet]
			});
	// 定义弹出窗体
	var win = new Ext.Window({
				width : 500,
				height : 320,
				title : "",
				autoScroll : true,
				buttonAlign : "center",
				items : [uppanel],
				layout : 'form',
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
                            handler : hideChildWin
                        }]
			});
	// ↑↑********弹出窗口*********↑↑//
	// 选择部门处理函数
	function selectDep() {
		var args = new Object();
		args.selectModel = "single";
		args.rootNode = {
			id : '-1',
			text : '合肥电厂'
		};
		var object = window
				.showModalDialog(
						'../../../../comm/jsp/hr/dept/dept.jsp',
						args,
						'dialogWidth=800px;dialogHeight=520px;center=yes;help=no;resizable=no;status=no;');
		// 根据返回值设置画面的值
		if (object) {
			drpName.store.removeAll();
			drpName.setValue("");
			if (typeof(object.names) != "undefined") {
				txtDepName.setValue(object.names);
			}
			if (typeof(object.codes) != "undefined") {
				hdnDepCode.setValue(object.codes);
				drpName.store.load({
							params : {
								strDeptCode : object.codes
							}
						});
			}
		}
	}
	// 查询操作
	function queryHandler() {
		// 获取开始时间值
		var startDateValue = Ext.get("startDate").dom.value;
		// 获取结束时间值
		var endDateValue = Ext.get("endDate").dom.value;
		// 比较开始时间与结束时间

		if ((startDateValue != "") && (endDateValue != "")) {

			var date1 = Date.parseDate(startDateValue, 'Y-m-d');
			var date2 = Date.parseDate(endDateValue, 'Y-m-d');
			if (date1.getTime() > date2.getTime()) {
				Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, String.format(
								MessageConstants.COM_E_009, "开始日期", "结束日期"));

				return;
			}
		}
		storeCar.baseParams = {
			strStartDate : startDate.getValue(),
			strEndDate : endDate.getValue(),
			strDepCode : hdnDepCode.getValue(),
			strWorkerCode : drpName.getValue(),
			strDriverCode : drpDriver.getValue(),
			strdrpCarStatus : drpCarStatus.getValue()
		};
		storeCar.load({
					params : {
						start : 0,
						limit : Constants.PAGE_SIZE
					}
				});
	}
	storeCar.on("load", function() {
	    // 没有检索到任何信息
		if (storeCar.getCount() <= 0) {
		    Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
					MessageConstants.COM_I_003);
		}
	});

	// 明细操作
	function overHandler() {
		// 判断是否已选择了数据
        if (grid.selModel.hasSelection()) {
        	var record = grid.getSelectionModel().getSelected();
            win.setTitle("用车管理明细");
		    win.show();
            uppanel.focus();
		    uppanel.getForm().loadRecord(record);
		    Ext.get('carFileUpdateTime').dom.value = record.get('carFileUpdateTime') == null
					? ""
					: record.data.carFileUpdateTime;
		    Ext.get('updateTime').dom.value = record.get('updateTime') == null
					? ""
					: record.data.updateTime;
			// 设置是否出省
		    if(record.get('ifOut')=='Y'){
		       Ext.get('ifOut').dom.value = '是'
		    }
		    if(record.get('ifOut')=='N'){
		       Ext.get('ifOut').dom.value = '否'
		    }
		    // 用车人数为0时单独处理
		    if(record.get('useNum') == 0){
		          Ext.get('useNum').dom.value = '0 人';
		       }
		    // 设置用车人数
		    if(record.get('useNum')){
		       Ext.get('useNum').dom.value = record.get('useNum')+' 人';
		    }
		    // 用车天数为0时单独处理
		    if(record.get('useDays') == 0){
		          Ext.get('useDays').dom.value = '0.00 天';
		       }
		    // 设置用车天数
		    if(record.get('useDays')){
		      	Ext.get('useDays').dom.value = divide2(record.get('useDays'))+' 天';
		       
		    }
		    // 将申请单号中，车不存在的重置为空
		    if(record.get('carId')==null){
		        txtCarNo.setValue("");
		        drpFormDriver.setValue("");
		        txtStartTime.setValue("");
		        txtGoMile.setValue("");
		        txtCarNo.clearInvalid();
		       txtStartTime.clearInvalid();
    	       txtGoMile.clearInvalid(); 
		    }else{
		    	txtCarNo.setValue(record.get('carNo'));
		        drpFormDriver.setValue(record.get('driverCode'));
		        txtStartTime.setValue(record.get('startTime'));
		        txtGoMile.setValue(record.get('goMile'));
		    }
		    // 设置司机
//		    drpFormDriver.setValue(record.get('driverCode'),true);
		    // 设置车ID
		    if(record.get('carId')){
		       Ext.get('carId').dom.value = record.get('carId');
		    };
		    if(record.get('carNo')==null){
		       txtCarNo.clearInvalid();
		       txtStartTime.clearInvalid();
    	       txtGoMile.clearInvalid(); 
		    	
		    }
		    checkAllBlank(); 
        }else{
        	Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
                    MessageConstants.COM_I_001);
        }
        


	}
	// 删除操作
	function deleteHandler() {
		if (grid.selModel.hasSelection()) {
        var record = grid.getSelectionModel().getSelected();
		//向后台传数据
		var carAppIdText = record.get("id");
		var updateTime = (record.get('updateTime') == null
					? ""
					: record.get('updateTime'));
		Ext.Msg.confirm(Constants.NOTICE_CONFIRM, MessageConstants.COM_C_002,
		    function(buttonObj) {
		      if (buttonObj == "yes") {
		         Ext.Ajax.request({
                    method : Constants.POST,
                    url : 'administration/delCarApp.action', 
                    params : {
						carAppIdValue : carAppIdText,
						updateTime : updateTime
						},                           
		            success : function(result, request) {
                            var o = eval("(" + result.responseText
											+ ")");
							if (o && o.msg){
								// 排他
							  if (o.msg == Constants.DATA_USING) {
                                        Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
                                                MessageConstants.COM_I_002);
                              }else
                              // SQL异常
                              if(o.msg == Constants.SQL_FAILURE){
                              	       Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
												MessageConstants.COM_E_014);
                              }else{
                              // 成功
                              	Ext.Msg.alert(
		                          MessageConstants.SYS_REMIND_MSG,
		                             MessageConstants.DEL_SUCCESS,
		                          function(){
		                           storeCar.load(
                                    {
                                      params : {
                                      start : 0,
                                      limit : Constants.PAGE_SIZE
                                    }
                                  });
                                  grid.getView().refresh();
		                          })
                              }
							}
		                   },
		            failure : function() {
		              }
		            });
		       }
		 });			
		}else{
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,MessageConstants.COM_I_001);
		}
		
	}
	// 派车单操作
	function formHandler() {
		if (grid.selModel.hasSelection()) {
			var record = grid.getSelectionModel().getSelected();
			var carApplyId = record.get('carApplyId');
			window.open("/power/report/webfile/administration/ARF06.jsp?applyNo=" +carApplyId);
		} else {
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
					MessageConstants.COM_I_001);
		}
	}
	function confirmRecord() {
		checkAllBlank();
		if( isNotNull()) {
		//alert(Ext.encode(uppanel.getForm().getValues()));
		// 取得司机编码
		Ext.get('hideFormDriver').dom.value = drpFormDriver.getValue();
		Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG,
				MessageConstants.COM_C_001, function(buttonobj) {
				    if (buttonobj == 'yes') {
				    var myurl = "administration/updateCarAppCarFile.action";
				    //设置隐藏域发车时间值
					hideStartTime.setValue(txtStartTime.getValue()+ ':00');
					//设置隐藏域发车里程值
					hideGoMile.setValue(txtGoMile.getValue());
					//设置隐藏域收车时间值
					if (Ext.get("endTime").dom.value) {
								hideEndTime.setValue(txtEndTime.getValue()+ ':00');
					} else {
						hideEndTime.setValue("");
						}
					//hideEndTime.setValue(txtEndTime.getValue()+ ':00');
					//设置隐藏域收车里程值
					hideComeMile.setValue(txtComeMile.getValue());
					//设置隐藏域油费值
					hideUseOil.setValue(txtUseOil.getValue());
					//设置隐藏域路桥费值
					hideLqPay.setValue(txtLqPay.getValue());
					hideDistance.setValue(txtDistance.getValue());
					uppanel.getForm().submit({
									
									method : Constants.POST,
									url : myurl,
									params : {
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
											}else 
											// 排他异常
											if(o.msg == Constants.DATA_USING){
												Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
																MessageConstants.COM_I_002);
											} else 
											// 数据转换错误
											if(o.msg == Constants.DATA_FAILURE){
												Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
																MessageConstants.COM_E_023);
											}else{
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
																	grid.getView().refresh();
																	win.hide();
																});
											}
										}
									},
									failure : function() {
									}
								});
					
				    }
				})
       }
	}
	function hideChildWin() {
			Ext.Msg.confirm(
					MessageConstants.SYS_CONFIRM_MSG,
					MessageConstants.COM_C_005,
					function(buttonobj) {
							if (buttonobj == 'yes') {
								win.hide();
							}
			})	
	}
	// 按钮状态设置
	function statusHandler(){
		// 判断是否已选择了数据
        if (grid.selModel.hasSelection()) {
        	var record = grid.getSelectionModel().getSelected();
        	var carStartDate = record.get("startTime");
        	var carEndDate = record.get("endTime");
        	if(!carStartDate){
        		btnform.setDisabled(true);
        		btnDelete.setDisabled(false);
        	}
        	if(carStartDate){
        		btnform.setDisabled(false);
        		btnDelete.setDisabled(true);
        	}
//        	if(carStartDate&&carEndDate){
//        		btnform.setDisabled(false);
//        		btnDelete.setDisabled(true);
//        	}        
        }else {
            Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
                    MessageConstants.COM_I_001);
        }
	}
	
	function popupSelect() {
		this.blur();
		var args = 0;
		// 弹出车辆选择共通画面
		var object = window
				.showModalDialog(
						'../../../../comm/jsp/administration/selectCar.jsp',
						null,
						'dialogWidth=400px;dialogHeight=400px;center=yes;help=no;resizable=no;status=no;');
		if (object) {
			// 返回值
			// 车牌号
			if (typeof(object.carNo) != "undefined") {
				txtCarNo.setValue(object.carNo);
			} else {
				txtCarNo.reset();
			}
			// 车ID
			if (typeof(object.id) != "undefined") {
				hideCarId.setValue(object.id);
			} else {
				hideCarId.reset();
			}
			// 发车里程
			if (typeof(object.runMile) != "undefined") {
				txtGoMile.setValue(object.runMile);
			} else {
				txtGoMile.reset();
			}
			// 司机 
			if (typeof(object.driver) != "undefined") {
				drpFormDriver.setValue(object.driver,true);
			} else {
				drpFormDriver.reset();
			// 车辆档案更新时间
			}
			if (typeof(object.updateTime)!= "undefined"){
			   hideCarFileUpdateTime.setValue(object.updateTime);
			} else{
			   hideCarFileUpdateTime.reset();
			}
			goMileChange();
			comeMileChange();
		}
	}
	// 非空check
    function isNotNull() {
    	var msg = "";
    	// 判断数据是否为空
    	if (txtCarNo.getValue() == "") {
    		msg = String.format(MessageConstants.COM_E_002, '车牌号');
    		Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, msg);
    		return false;
    	}
    	if ( txtStartTime.getValue() == "" ) {
    		msg = String.format(MessageConstants.COM_E_002,"发车时间");
    		Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, msg);
    		return false;
    	}
    	if (txtGoMile.getValue() === "") {
    		msg = String.format(MessageConstants.COM_E_002, '发车里程');
    		Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, msg);
    		return false;
    	}
    	
    	if (txtEndTime.getValue()!=""||txtComeMile.getValue()!==""
    	     ||txtUseOil.getValue()!==""||txtLqPay.getValue()!==""
    	     ||txtDistance.getValue()!==""){
    	  if (txtEndTime.getValue() == "") {
    		msg = String.format(MessageConstants.COM_E_002, '收车时间');
    		Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, msg, function(){txtEndTime.validate();});
    		return false;
    	  }
    	  if (txtComeMile.getValue() == "") {
    		msg = String.format(MessageConstants.COM_E_002, '收车里程');
    		Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, msg, function(){txtComeMile.validate();});
    		return false;
    	  }
    	  if (txtGoMile.getValue()>txtComeMile.getValue()) {
            Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, MessageConstants.AV004_E_001);
                    return false;
		  }
    	  // 比较发车时间与收车时间
		  // 获取发车时间值
		  var startTimeValue = Ext.get("startTime").dom.value;
		  // 获取收车时间值
		  var endTimeValue = Ext.get("endTime").dom.value;
		  // 比较开始时间与结束时间
		  if ((startTimeValue != "") && (endTimeValue != "")) {
			var date1 = Date.parseDate(startTimeValue, 'Y-m-d H:i');
			var date2 = Date.parseDate(endTimeValue, 'Y-m-d H:i');
			if (date1.getTime() > date2.getTime()) {
				Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, String.format(
								MessageConstants.COM_E_009, "发车时间", "收车时间"));

				return  false;
			}
		}
    	}
    	return true;
    }
    // 收车后全空check
    function checkAllBlank(){
       if (txtEndTime.getValue()===""&&txtComeMile.getValue()===""
    	     &&txtUseOil.getValue()===""&&txtLqPay.getValue()===""
    	     &&txtDistance.getValue()===""){
    	     	txtEndTime.setLabel("收车时间");
    	     	txtComeMile.setLabel("收车里程");
    	     	txtEndTime.clearInvalid();
    	     	txtComeMile.clearInvalid();
    	     	txtEndTime.allowBlank = true;
    	     	txtComeMile.allowBlank = true;
    	     } else {
    	     	txtEndTime.setLabel("收车时间<font color='red'>*</font>");
    	     	txtComeMile.setLabel("收车里程<font color='red'>*</font>");
    	     	txtEndTime.allowBlank  = false;
    	     	txtComeMile.allowBlank = false;
    	     }
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

});

