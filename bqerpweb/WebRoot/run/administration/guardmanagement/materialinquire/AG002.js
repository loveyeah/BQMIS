// 物资出门查询
// author:sufeiyu

Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.QuickTips.init();
Ext.onReady(function() {
	// 控件-----------------------------------------

	// 声明变量。
	var strStart = "";
	var strEnd = "";

	// 取得开始日期
	dateInit();

	// 日期开始控件
	var txtStartDate = new Ext.form.TextField({
				id : 'startDate',
				name : 'startDate',
				style : 'cursor:pointer',
				value : strStart,
				readOnly : true,
				width : 120,
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd',
									isShowClear : false,
									alwaysUseStartDate : false
								});
					}
				}
			});

	// 日期结束控件
	var txtEndDate = new Ext.form.TextField({
				id : 'endDate',
				name : 'endDate',
				style : 'cursor:pointer',
				value : strEnd,
				readOnly : true,
				width : 120,
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd',
									isShowClear : false,
									alwaysUseStartDate : false
								});
					}
				}
			});

	// 所在单位
	var txtCompany = new Ext.form.TextField({
				fieldLabel : '经办人所在单位',
				id : 'firm',
				isFormField : true,
				labelAlign : 'right',
				width : 120,
				maxLength : 40,
				name : 'firm'
			});

	// 上报状态
	var drpStatus = new Ext.form.CmbReportStatus({
				id : 'dcmStatus',
				labelAlign : 'right',
				width : 75,
				emptyText : '',
				name : 'dcmStatus'
			});

	// 定义选择列
	var sm = new Ext.grid.RowSelectionModel({
				singleSelect : false
			});

	// 备注
	var memoText = new Ext.form.TextArea({
				id : "memoText",
				maxLength : 127,
				width : 180,
				readOnly : true
			});

	// 弹出画面
	var winMemo = new Ext.Window({
				height : 170,
				width : 350,
				layout : 'fit',
				modal : true,
				resizable : false,
				closeAction : 'hide',
				items : [memoText],
				buttonAlign : "center",
				title : '详细信息查看窗口',
				buttons : [{
							text : Constants.BTN_CLOSE,
							iconCls : Constants.CLS_CANCEL,
							id : "cancel",
							handler : function() {
								winMemo.hide();
							}
						}]
			});

	// 定义列属性
	var cm = new Ext.grid.ColumnModel([
			// 自动行号
			new Ext.grid.RowNumberer({
						header : "行号",
						width : 31
					}), {
				header : "序号",
				hidden : true,
				dataIndex : 'id'
			}, {
				header : "经办人",
				sortable : true,
				dataIndex : 'agent',
				width : 70
			}, {
				header : "经办人所在单位",
				sortable : true,
				dataIndex : 'firm'
			}, {
				header : "出门时间",
				sortable : true,
				dataIndex : 'outDate',
				renderer : renderDate,
				width : 100
			}, {
				header : "物资名称",
				sortable : true,
				dataIndex : 'wpName'
			}, {
				header : "物资规格",
				sortable : true,
				dataIndex : 'standard'
			}, {
				header : "物资数量",
				sortable : true,
				align : 'right',
				renderer : divide2,
				dataIndex : 'num',
				width : 70
			}, {
				header : "物资单位",
				sortable : true,
				dataIndex : 'unit',
				width : 70
			}, {
				header : "备注",
				sortable : true,
				dataIndex : 'note'
			}, {
				header : "物资出入原因",
				sortable : true,
				dataIndex : 'reason'
			}, {
				header : "上报状态",
				sortable : true,
				dataIndex : 'dcmStatus',
				width : 65
			}, {
				header : "值班保安",
				sortable : true,
				hidden : true,
				dataIndex : 'onduty',
				width : 70
			}])
	// 数据源--------------------------------
	var recordMaterial = Ext.data.Record.create([{
				name : 'id'
			}, {
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
				name : 'onduty'
			}, {
				name : 'dcmStatus'
			}]);

	// 定义获取数据源
	var dataProxy = new Ext.data.HttpProxy({
				url : 'administration/getData.action'
			});

	// 定义格式化数据
	var theReader = new Ext.data.JsonReader({
				root : "list",
				totalProperty : "totalCount"
			}, recordMaterial);

	// 定义封装缓存数据的对象
	var storeQuery = new Ext.data.Store({
				// 访问的对象
				proxy : dataProxy,
				// 处理数据的对象
				reader : theReader
			});

	// --gridpanel显示格式定义-----开始-------------------
	// 查询按钮
	var btnQUERY = new Ext.Button({
				text : Constants.BTN_QUERY,
				iconCls : Constants.CLS_QUERY,
				handler : queryRecord
			})

	// 导出按钮
	var btnOutput = new Ext.Button({
				text : "导出",
				iconCls : Constants.CLS_EXPORT,
				handler : outputRecord
			})

	// 设置不可用
	btnOutput.setDisabled(true);

	storeQuery.on("load", function() {
				if (storeQuery.getCount() > 0) {
					btnOutput.setDisabled(false);
				} else {
					btnOutput.setDisabled(true);
					Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
							MessageConstants.COM_I_003)
				}
			});

	// 头部工具栏
	var tbarMaterial = new Ext.Toolbar(["出门日期:", "从", txtStartDate, "到",
			txtEndDate, "-", "经办人所在单位:", txtCompany, "-", "上报状态:", drpStatus,
			"-", btnQUERY, btnOutput])

	// 分页
	var bbarMaterial = new Ext.PagingToolbar({
				pageSize : Constants.PAGE_SIZE,
				store : storeQuery,
				displayInfo : true,
				displayMsg : MessageConstants.DISPLAY_MSG,
				emptyMsg : MessageConstants.EMPTY_MSG
			})

	var gridMaterial = new Ext.grid.GridPanel({
				region : "center",
				layout : 'fit',
				store : storeQuery,
				cm : cm,
				sm : sm,
				autoScroll : true,
				enableColumnMove : false,
				autoSizeColumns : true,
				viewConfig : {
					forceFit : false
				},
				// 头部工具栏
				tbar : tbarMaterial,

				// 分页
				bbar : bbarMaterial
			});

	gridMaterial.on('celldblclick', function(grid, row, column) {
				if (9 == column) {
					showMemoWin();
				}
			})
	// --gridpanel显示格式定义-----结束--------------------

	// 日期初值
	function dateInit() {
		//
		var dateEnd = new Date();
		var dateStart = new Date(dateEnd.getYear(), dateEnd.getMonth(), dateEnd
						.getDate()
						- 15);
		strStart = dateFormat(dateStart);
		strEnd = dateFormat(dateEnd);
	}

	// 日期格式化
	function dateFormat(value) {
		var intYear;
		var intMonth;
		var intDay;
		intDay = value.getDate();
		if (intDay < 10) {
			intDay = '0' + intDay;
		}
		intMonth = value.getMonth() + 1;
		if (intMonth < 10) {
			intMonth = '0' + intMonth;
		}
		intYear = value.getYear();
		value = intYear + "-" + intMonth + "-" + intDay;
		return value;
	}

	// 时间的有效性检查
	function checkDate() {
		var strStartDateValue = Ext.get("startDate").dom.value;
		var strEndDateValue = Ext.get("endDate").dom.value;

		if (strStartDateValue != "" && strEndDateValue != "") {
			var dteStartDate = Date.parseDate(strStartDateValue, 'Y-m-d');
			var dteEndDate = Date.parseDate(strEndDateValue, 'Y-m-d');
			if (dteStartDate != 'undefined' && dteEndDate != 'undefined') {
				if (dteStartDate.getTime() > dteEndDate.getTime()) {
					Ext.Msg
							.alert(Constants.ERROR, String.format(
											MessageConstants.COM_E_009, "开始日期",
											"结束日期"));
					return false;
				} else {
					return true;
				}
			}
		} else {
			return true;
		}
	}

	// 查询按下处理
	function queryRecord() {
		if (checkDate()) {
			storeQuery.baseParams = {
				strFirm : Ext.get('firm').dom.value,
				lngDcmStatus : drpStatus.getValue(),
				dteStartDate : txtStartDate.getValue(),
				dteEndDate : txtEndDate.getValue()
			};
			storeQuery.load({
						params : {
							start : 0,
							limit : Constants.PAGE_SIZE
						},
						callback : function() {
							if (storeQuery.getCount() == 0) {
								Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
										MessageConstants.COM_I_003);
							}
						}
					});
			gridMaterial.getView().refresh();
		}
	}

	// 导出按下处理
	function outputRecord() {
		Ext.Msg.confirm(Constants.BTN_CONFIRM, MessageConstants.COM_C_007,
				function(buttonobj) {
					if (buttonobj == 'yes') {
						var urlAction = "administration/materialExportFile.action?strFirm="
								+ Ext.get('firm').dom.value
								+ "&lngDcmStatus="
								+ drpStatus.getValue()
								+ "&dteStartDate="
								+ txtStartDate.getValue()
								+ "&dteEndDate="
								+ txtEndDate.getValue();
						document.all.blankFrame.src = urlAction;
					}
				})
	}

	// 两位数字处理
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

	/**
	 * 显示备注窗口
	 */
	function showMemoWin() {
		var record = gridMaterial.getSelectionModel().getSelected();
		memoText.setValue(record.data.note);
		winMemo.show(record);

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

	new Ext.Viewport({
				enableTabScroll : true,
				layout : "fit",
				items : [gridMaterial]
			});
})