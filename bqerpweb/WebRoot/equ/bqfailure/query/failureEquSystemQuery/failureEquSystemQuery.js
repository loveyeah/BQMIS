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
	var startdate = enddate.add(Date.MONTH, -1);
	startdate = startdate.getFirstDateOfMonth();
	var sdate = ChangeDateToString(startdate);
	var edate = ChangeDateToString(enddate);
	var timefromDate = new Ext.form.DateField({
		format : 'Y-m-d',
		name : '_timefromDate',
		id : 'timefromDate',
		itemCls : 'sex-left',
		clearCls : 'allow-float',
		checked : true,
		fieldLabel : "查询时间",
		allowBlank : false,
		readOnly : true,
		value : sdate,
		emptyText : '请选择',
		anchor : '95%'
	});
	var timetoDate = new Ext.form.DateField({
		format : 'Y-m-d',
		name : '_timetoDate',
		value : edate,
		id : 'timetoDate',
		itemCls : 'sex-left',
		clearCls : 'allow-float',
		fieldLabel : "至",
		allowBlank : false,
		readOnly : true,
		emptyText : '请选择',
		anchor : '95%'
	});

	var btnQuery = new Ext.Button({
		id : 'btnQuery',
		text : '统 计',
		iconCls : 'query',
		minWidth : 50,
		handler : function() {
			var ftime = Ext.get('timefromDate').dom.value;
			var ttime = Ext.get('timetoDate').dom.value;
			if (ftime > ttime) {
				Ext.Msg.alert('提示', '选择后一日期应比前一日期大！');
				return false;
			}
			ds.load({
				params : {
					startDate : ftime,
					endDate : ttime,
					start : 0,
					limit : 18
				}
			});
		}
	});

	var btnPrint = new Ext.Button({
		id : 'btnPrint',
		text : '导 出',
		iconCls : 'export',
		minWidth : 50,
		handler : function() {
			reportexport();
		}
	});
	var tbar = new Ext.Toolbar({
		items : ['查询时间段：', timefromDate, '  ', '至 ', timetoDate, '-', btnQuery,
				'-', btnPrint]
	})

	var item = Ext.data.Record.create([{
		name : 'belongSystem'
	}, {
		name : 'belongSystemName'
	}, {
		name : 'count'
	}, {
		name : 'eliminateCount'
	}, {
		name : 'awaitCount'
	}, {
		name : 'eliminateRate'
	}]);
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true
	});
	var cm = new Ext.grid.ColumnModel({
		columns : [new Ext.grid.RowNumberer({
			header : '序号',
			width : 50,
			align : 'center'
		}), {
			header : '系统编码',
			dataIndex : 'belongSystem',
			align : 'left'
		}, {
			header : '系统名称',
			dataIndex : 'belongSystemName',
			align : 'left'
		}, {
			header : '发现数',
			dataIndex : 'count',
			align : 'center'
		}, {
			header : '消缺数',
			dataIndex : 'eliminateCount',
			align : 'center'
		}, {
			header : '待处理数',
			dataIndex : 'awaitCount',
			align : 'center'
		}, {
			header : '消缺率',
			dataIndex : 'eliminateRate',
			align : 'center'
		}],
		rows : [[{
			header : "<b><font color='black'; font-weight:bold; size='5'>灞桥电厂缺陷统计报表(按系统)</font></b><br/>",
			height : '50',
			colspan : 6,
			align : "center"
		}]]
	});
	cm.defaultSortable = true;
	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'bqfailure/failureQueryBySystem.action'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'total',
			root : 'list'
		}, item)
	});
	ds.load({
		params : {
			startDate : sdate,
			endDate : edate,
			start : 0,
			limit : 18
		}
	});
	ds.on('beforeload', function() {
		Ext.apply(this.baseParams, {
			startDate : Ext.get('timefromDate').dom.value,
			endDate : Ext.get('timetoDate').dom.value
		});
	});
	var gridbbar = new Ext.PagingToolbar({
		pageSize : 18,
		store : ds,
		displayInfo : true,
		displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
		emptyMsg : "没有记录",
		beforePageText : '页',
		afterPageText : "共{0}"
	});

	var Grid = new Ext.grid.GridPanel({
		ds : ds,
		cm : cm,
		sm : sm,
		split : true,
		autoScroll : true,
		tbar : tbar,
		bbar : gridbbar,
		border : false,
		plugins : [new Ext.ux.plugins.GroupHeaderGrid()],
		viewConfig : {
			forceFit : true
		}
	});
	function tableToExcel(tableHTML) {
		window.clipboardData.setData("Text", tableHTML);
		try {
			var ExApp = new ActiveXObject("Excel.Application");
			var ExWBk = ExApp.workbooks.add();
			var ExWSh = ExWBk.worksheets(1);
			ExApp.Columns(1).ColumnWidth = 15;
			ExApp.Columns(2).ColumnWidth = 15;
			ExApp.Columns(3).ColumnWidth = 15;
			ExApp.DisplayAlerts = true;
			ExWSh.Paste();
			ExApp.visible = true;
		} catch (e) {
			if (e.number != -2146827859)
				alert("您的电脑没有安装Microsoft Excel软件！");
			return false;
		}
		ExWBk.worksheets(1).Paste;
	}

	function reportexport() {
		Ext.Ajax.request({
			url : 'bqfailure/failureQueryBySystem.action',
			params : {
				startDate : Ext.get('timefromDate').dom.value,
				endDate : Ext.get('timetoDate').dom.value
			},
			success : function(response) {
				var json = eval('(' + response.responseText.trim() + ')');
				var records = json.list;
				var html = ['<table border=1><tr><td align="center" colspan="6"><b><h1>灞桥电厂缺陷统计报表(按系统)</h1></b></td></tr><tr><th>系统编码</th><th>系统名称</th><th>发现数</th><th>消缺数</th><th>待处理数</th><th>消缺率</th></tr>'];
				for (var i = 0; i < records.length; i += 1) {
					var rc = records[i];
					html.push('<tr><td>' + rc.belongSystem + '</td><td>'
							+ rc.belongSystemName + '</td><td>' + rc.count
							+ '</td><td>' + rc.eliminateCount + '</td><td>'
							+ rc.awaitCount + '</td><td>' + rc.eliminateRate
							+ '</td></tr>');
				}
				html.push('</table>');
				html = html.join('');
				tableToExcel(html);
			},
			failure : function(response) {
				Ext.Msg.alert('提示', '导出失败！');
			}
		});
	}
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		collapsible : true,
		split : true,
		margins : '0 0 0 0',
		items : [Grid]
	});
})