Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	function ChangeDateToString(DateIn) {
		var Year = 0;
		var Month = 0;
		var Day = 0;
		var CurrentDate = "";
		// 初始化时间
		Year = DateIn.getYear();
		Month = DateIn.getMonth() + 1;
		Day = DateIn.getDate();
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
		return CurrentDate;
	}
	var enddate = new Date();
	var startdate = enddate.add(Date.MONTH, -12);
	startdate = startdate.getFirstDateOfMonth();
	var sdate = ChangeDateToString(startdate);
	var edate = ChangeDateToString(enddate);
	var fromDate = new Ext.form.DateField({
		format : 'Y-m-d',
		name : 'startDate',
		id : 'fromDate',
		itemCls : 'sex-left',
		clearCls : 'allow-float',
		checked : true,
		allowBlank : false,
		readOnly : true,
		value : sdate,
		emptyText : '请选择',
		anchor : '100%'
	});

	var toDate = new Ext.form.DateField({
		format : 'Y-m-d',
		name : 'endDate',
		value : edate,
		id : 'toDate',
		itemCls : 'sex-left',
		clearCls : 'allow-float',
		allowBlank : false,
		readOnly : true,
		emptyText : '请选择',
		anchor : '80%'
	});

	// 定义grid
	var MyRecord = Ext.data.Record.create([{
		name : 'repairId',
		mapping : 0
	}, {
		name : 'carId',
		mapping : 1
	}, {
		name : 'carNo',
		mapping : 2
	}, {
		name : 'nowKmNum',
		mapping : 3
	}, {
		name : 'sendPerson',
		mapping : 4
	}, {
		name : 'sendPersonName',
		mapping : 5
	}, {
		name : 'repairDate',
		mapping : 6
	}, {
		name : 'repairContend',
		mapping : 7
	}]);
	var dataProxy = new Ext.data.HttpProxy({
		url : 'security/findCarRepairList.action'
	});
	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	}, MyRecord);
	var store = new Ext.data.Store({
		proxy : dataProxy,
		reader : theReader
	});

	var carNo = new Ext.form.TextField({
		id : 'carNo',
		name : 'carNo',
		anchor : "75%"
	});
	var sm = new Ext.grid.CheckboxSelectionModel({
			// singleSelect : true
	});
	var grid = new Ext.grid.GridPanel({
		region : "center",
		store : store,
		columns : [sm, new Ext.grid.RowNumberer({
			header : '序号',
			width : 35,
			align : 'left'
		}), {
			header : "ID",
			width : 110,
			hidden : true,
			align : 'left',
			dataIndex : 'repairId'
		}, {
			header : "牌照",
			width : 80,
			sortable : true,
			align : 'left',
			dataIndex : 'carNo'
		}, {
			header : "维修日期",
			width : 120,
			sortable : true,
			align : 'left',
			dataIndex : 'repairDate'
		}, {
			header : "当前公里数",
			width : 80,
			sortable : true,
			align : 'left',
			dataIndex : 'nowKmNum'
		}, {
			header : "送修人",
			width : 80,
			sortable : true,
			align : 'left',
			dataIndex : 'sendPersonName'
		}, {
			header : "维修内容",
			width : 150,
			sortable : true,
			align : 'left',
			dataIndex : 'repairContend'
		}],
		sm : sm,
		tbar : ["维修日期:", fromDate, "~", toDate, "-", "牌照:", carNo, {
			text : "查询",
			iconCls : 'query',
			handler : queryRecord
		}, {
			text : "导出",
			iconCls : 'export',
			handler : myExport
		}],
		// 分页
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : store,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		})
	});

	// 查询
	function queryRecord() {
		var ftime = Ext.get('fromDate').dom.value;
		var ttime = Ext.get('toDate').dom.value;
		if (ftime > ttime) {
			Ext.Msg.alert('提示', '选择后一日期应比前一日期大！');
			return false;
		}
		store.baseParams = {
			sDate : Ext.get('fromDate').dom.value,
			eDate : Ext.get('toDate').dom.value,
			carNo : Ext.get('carNo').dom.value
		}
		store.load({
			params : {
				start : 0,
				limit : 18
			}
		});
	}

	// 导出
	function tableToExcel(tableHTML) {
		window.clipboardData.setData("Text", tableHTML);
		try {
			var ExApp = new ActiveXObject("Excel.Application");
			var ExWBk = ExApp.workbooks.add();
			var ExWSh = ExWBk.worksheets(1);
			ExApp.DisplayAlerts = false;
			ExApp.visible = true;
		} catch (e) {
			if (e.number != -2146827859)
				alert("您的电脑没有安装Microsoft Excel软件！");
			return false;
		}
		ExWBk.worksheets(1).Paste;
	}
	function myExport() {
		Ext.Ajax.request({
			url : 'security/findCarRepairList.action',
			params : {
//				start : 0,
//				limit : 18,
				sDate : Ext.get('fromDate').dom.value,
				eDate : Ext.get('toDate').dom.value,
				carNo : Ext.get('carNo').dom.value
			},
			success : function(response) {
				var json = eval('(' + response.responseText.trim() + ')');
				var records = json.list;
				// alert(records.length);
				var html = ['<table border=1><tr><th>牌照</th><th>维修日期</th><th>当前公里数</th><th>送修人</th><th>维修内容</th></tr>'];
				for (var i = 0; i < records.length; i += 1) {
					var rc = records[i];
					html.push('<tr><td>' + rc[2] + '</td><td>' + rc[6]
							+ '</td><td>' + rc[3] + '</td><td>' + rc[5]
							+ '</td><td>' + rc[7] + '</td></tr>');
				}
				html.push('</table>');
				html = html.join(''); // 最后生成的HTML表格
				// alert(html);
				tableToExcel(html);
			},
			failure : function(response) {
				Ext.Msg.alert('信息', '失败');
			}
		});
	}

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		items : [grid]
	});

	queryRecord();
});