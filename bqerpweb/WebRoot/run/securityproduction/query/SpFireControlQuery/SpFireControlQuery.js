Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';

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
	var edate = ChangeDateToString(enddate);
	
	// 定义grid
	var MyRecord = Ext.data.Record.create([{
		name : 'id',
		mapping : 0
	}, {
		name : 'deployPart',
		mapping : 1
	}, {
		name : 'type',
		mapping : 2
	}, {
		name : 'param',
		mapping : 3
	}, {
		name : 'controlNumber',
		mapping : 4
	}, {
		name : 'serialCode',
		mapping : 5
	}, {
		name : 'validDate',
		mapping : 6
	}, {
		name : 'checkDate',
		mapping : 7
	}, {
		name : 'changeDate',
		mapping : 8
	}, {
		name : 'checkBy',
		mapping : 9
	}, {
		name : 'checkName',
		mapping : 10
	}, {
		name : 'memo',
		mapping : 11
	}]);
	var dataProxy = new Ext.data.HttpProxy({
		url : 'security/findFireControlList.action'
	});
	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	}, MyRecord);
	var store = new Ext.data.Store({
		proxy : dataProxy,
		reader : theReader
	});

	var deployPartQuery = new Ext.form.TextField({
		id : 'deployPartQuery',
		name : 'deployPart',
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
			dataIndex : 'id'
		}, {
			header : "配置部位",
			width : 100,
			sortable : true,
			align : 'left',
			dataIndex : 'deployPart'
		}, {
			header : "型号",
			width : 100,
			sortable : true,
			align : 'left',
			dataIndex : 'type'
		}, {
			header : "规格",
			width : 100,
			sortable : true,
			align : 'left',
			dataIndex : 'param'
		}, {
			header : "数量",
			width : 70,
			sortable : true,
			align : 'left',
			dataIndex : 'controlNumber'
		}, {
			header : "编号",
			width : 100,
			sortable : true,
			align : 'left',
			dataIndex : 'serialCode'
		}, {
			header : "有效日期",
			width : 90,
			sortable : true,
			align : 'left',
			dataIndex : 'validDate'
		}, {
			header : "检测日期",
			width : 90,
			sortable : true,
			align : 'left',
			dataIndex : 'checkDate'
		}, {
			header : "更换日期",
			width : 90,
			sortable : true,
			align : 'left',
			dataIndex : 'changeDate'
		}, {
			header : "检测人",
			width : 80,
			sortable : true,
			align : 'left',
			dataIndex : 'checkName'
		}, {
			header : "备注",
			width : 150,
			sortable : true,
			align : 'left',
			dataIndex : 'memo'
		}],
		sm : sm,
		tbar : ["配置部位:", deployPartQuery, {
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
		store.baseParams = {
			start : 0,
			limit : 18,
			deployPart : deployPartQuery.getValue()
		}
		store.load();
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
	
	var parseString = function(v){
		if(v == null)
			return '';
		else
			return v;
	}
	function myExport() {
		Ext.Ajax.request({
			url : 'security/findFireControlList.action',
			params : {
//				start : 0,
//				limit : 18,
				deployPart : deployPartQuery.getValue()
			},
			success : function(response) {
				var json = eval('(' + response.responseText.trim() + ')');
				var records = json.list;
				// alert(records.length);
				var html = ['<table border=1><tr><th>配置部位</th><th>型号</th><th>规格</th><th>数量</th><th>编号</th><th>有效日期</th><th>检测日期</th><th>更换日期</th><th>检测人</th><th>备注</th></tr>'];
				for (var i = 0; i < records.length; i += 1) {
					var rc = records[i];
					html.push('<tr><td>' + parseString(rc[1]) + '</td><td>' + parseString(rc[2])
							+ '</td><td>' + parseString(rc[3]) + '</td><td>' + parseString(rc[4])
							+ '</td><td>' + parseString(rc[5]) + '</td><td>' + parseString(rc[6]) 
							+ '</td><td>' + parseString(rc[7]) + '</td><td>' + parseString(rc[8])
							+ '</td><td>' + parseString(rc[10]) + '</td><td>' + parseString(rc[11])
							+ '</td></tr>');
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