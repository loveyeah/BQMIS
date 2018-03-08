Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
	var balanceId = getParameter("balanceId");
	var method = "update";
	var count = 0;
	function ChangeDateToString(DateIn) {
		var Year = 0;
		var Month = 0;
		var Day = 0;
		var CurrentDate = "";
		// 初始化时间
		Year = DateIn.getYear();
		Month = DateIn.getMonth() + 1;
		Day = DateIn.getDate();
		hour = DateIn.getHours();
		min = DateIn.getMinutes();
		se = DateIn.getSeconds();
		CurrentDate = Year + "-";
		if (Month >= 10) {
			CurrentDate = CurrentDate + Month + "-";
		} else {
			CurrentDate = CurrentDate + "0" + Month + "-";
		}
		if (Day >= 10) {
			CurrentDate = CurrentDate + Day;
		} else {
			CurrentDate = CurrentDate + "0" + Day;
		}
		CurrentDate += " "
		if (hour >= 10) {
			CurrentDate = CurrentDate + hour + ":";
		} else {
			CurrentDate = CurrentDate + "0" + hour + ":";
		}
		if (min >= 10) {
			CurrentDate = CurrentDate + min + ":";
		} else {
			CurrentDate = CurrentDate + "0" + min + ":";
		}
		if (se >= 10) {
			CurrentDate = CurrentDate + se + ":";
		} else {
			CurrentDate = CurrentDate + "0" + se + ":";
		}
		return CurrentDate;
	}
	var date = new Date();
	var currentdate = ChangeDateToString(date);
	var opman;
	var opmanName;
	var sessWorname;
	var sessWorcode;
	var flag;
	Ext.Ajax.request({
		url : 'managecontract/getSessionInfo.action',
		params : {},
		method : 'post',
		waitMsg : '正在加载数据...',
		success : function(result, request) {
			var responseArray = Ext.util.JSON.decode(result.responseText);
			if (responseArray.success == true) {
				var tt = eval('(' + result.responseText + ')');
				o = tt.data;
				sessWorcode = o[0];
				sessWorname = o[1];
			} else {
				Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
			}
		},
		failure : function(result, request) {
			Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
		}
	});
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
				opman = o.data.operateBy;
				opmanName = o.data.operateName
				flag = o.data.balaFlag;
			},
			failure : function(result, request) {
				Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
			}
		});
	}
	function renderDate(value) {
		return value ? value.dateFormat('Y-m-d H:i:s') : '';
	}
	var dateColumn = ({
		header : "开票日期",
		sortable : true,
		width : 180,
		dataIndex : 'invoiceDate',
		renderer : function(value) {
			if (!value)
				return '';
			if (value instanceof Date)
				return renderDate(value);
			var reDate = /\d{4}\-\d{2}\-\d{2}/gi;
			var reTime = /\d{2}:\d{2}:\d{2}/gi;
			var strDate = value.match(reDate);
			var strTime = value.match(reTime);
			if (!strDate)
				return "";
			strTime = strTime ? strTime : '00:00:00';
			return strDate + " " + strTime;
		}
	})

	var invoiceItem = Ext.data.Record.create([{
		name : 'invoiceNo'
	}, {
		name : 'balanceId'
	}, {
		name : 'invoiceCode'
	}, {
		name : 'invoiceId'
	}, {
		name : 'invoiceDate'
	}, {
		name : 'invoiceName'
	}, {
		name : 'invoicePrice'
	}, {
		name : 'drawerBy'
	}, {
		name : 'operateBy'
	}, {
		name : 'memo'
	}, {
		name : 'drawerName'
	}, {
		name : 'operateName'
	}]);
	var invoice_sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false,
		listeners : {
			rowselect : function(sm, row, rec) {

			}
		}
	});

	var invoice_cm = new Ext.grid.ColumnModel([invoice_sm,
			new Ext.grid.RowNumberer({
				header : '项次号',
				width : 50,
				align : 'center'
			}), {
				header : '票据编号',
				dataIndex : 'invoiceNo',
				align : 'center'
			},
			{
				header : '票据类型',
				dataIndex : 'invoiceId',
				align : 'center'
//				,
//				renderer : function(v) {
//					if ("1" == v)
//						return "aa";
//					if ("2" == v)
//						return "bb";
//				}
			},  {
				header : '票据说明',
				dataIndex : 'invoiceName',
				align : 'center'
			}, {
				header : '票据金额',
				dataIndex : 'invoicePrice',
				align : 'center'
			}, {
				header : '开票人',
				dataIndex : 'drawerBy',
				align : 'center',
				renderer : function(v) {
					return sessWorname
				}
			},dateColumn, {
				header : '经办人',
				dataIndex : 'operateBy',
				align : 'center',
				hidden : true,
				renderer : function(v) {
					return opmanName
				}
			}]);
	invoice_cm.defaultSortable = true;
	var invoice_ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'managecontract/findBalInvoice.action'
		}),
		reader : new Ext.data.JsonReader({}, invoiceItem)
	});

	if (balanceId != "") {
		invoice_ds.load({
			params : {
				balanceId : balanceId
			}
		});
	}
	var invoiceGrid = new Ext.grid.GridPanel({
		store : invoice_ds,
		cm : invoice_cm,
		sm : invoice_sm,
		frame : false,
		border : false,
		autoWidth : true,
		autoScroll : true
	});

	var layout = new Ext.Viewport({
		layout : 'fit',
		border : true,
		items : [{
			layout : 'fit',
			margins : '0 0 0 0',
			region : 'center',
			autoScroll : true,
			items : invoiceGrid
		}]
	});
});