Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var specialcode = parent.document.getElementById("specialityCode").value;
	var fromdate = parent.document.getElementById("fromDate").value;
	var todate = parent.document.getElementById("toDate").value;
	var left_item = Ext.data.Record.create([{
		name : 'runlogWorkerId'
	}, {
		name : 'woWorktype'
	}, {
		name : 'bookedEmployee'
	}, {
		name : 'workerName'
	}, {
		name : 'specialCode'
	}, {
		name : 'specialName'
	}, {
		name : 'runLogno'
	}, {
		name : 'shiftName'
	}]);
	var left_ds = new Ext.data.GroupingStore({
		proxy : new Ext.data.HttpProxy({
			url : 'runlog/queryWorkerList.action'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'total',
			root : 'root'
		}, left_item),
		groupField : 'name',
		groupOnSort : true,
		sortInfo : {
			field : 'runLogno',
			direction : "DESC"
		}
	});
	left_ds.on('beforeload', function() {
		Ext.apply(this.baseParams, {
			method : "all",
			specialcode : specialcode,
			formdate : fromdate,
			todate : todate
		});
	});
	left_ds.load({
		params : {
			start : 0,
			limit : 18
		}
	});
	var left_item_cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(), {
		header : '日志号',
		dataIndex : 'runLogno',
		align : 'center'
	}, {
		header : '专业',
		dataIndex : 'specialName',
		align : 'center'
	}, {
		header : '值别',
		dataIndex : 'shiftName',
		align : 'center'
	}, {
		header : '值班人员',
		dataIndex : 'workerName',
		align : 'center'
	}, {
		header : '状态',
		dataIndex : 'woWorktype',
		renderer : changeColor,
		align : 'center'
	}]);
	function changeColor(v) {
		if (v == 'LOGONS') {
			return "<div  style='width:40; background:green'>&nbsp;&nbsp;&nbsp;&nbsp;</div>";
		} else if (v == 'LOGAGN') {
			return "<div  style='width:40; background:gray'>&nbsp;&nbsp;&nbsp;&nbsp;</div>";
		} else if ((v == 'LOGABS') || (v == 'LOGABG')) {
			return "<div  style='width:40; background:red'>&nbsp;&nbsp;&nbsp;&nbsp;</div>";
		} else if (v == 'LOGSUC') {
			return "<div  style='width:40; background:navy'>&nbsp;&nbsp;&nbsp;&nbsp;</div>";
		}
	}
	var leftbbar = new Ext.PagingToolbar({
		pageSize : 18,
		store : left_ds,
		displayInfo : true,
		displayMsg : "共 {2} 条",
		emptyMsg : "没有记录",
		beforePageText : '页',
		afterPageText : "共{0}"
	});
	var leftGrid = new Ext.grid.GridPanel({
		id : 'left-grid',
		title : '值班人员',
		ds : left_ds,
		bbar : leftbbar,
		cm : left_item_cm,
		fitToFrame : true,
		view : new Ext.grid.GroupingView({
			forceFit : true,
			sortAscText : '正序',
			sortDescText : '倒序',
			columnsText : '列显示/隐藏',
			groupByText : '依本列分组',
			showGroupsText : '分组显示',
			groupTextTpl : '{text}'
		}),
		border : true,
		viewConfig : {
			forceFit : true
		}
	});
	var right_item = Ext.data.Record.create([{
		name : 'runlogWorkerId'
	}, {
		name : 'woWorktype'
	}, {
		name : 'bookedEmployee'
	}, {
		name : 'workerName'
	}, {
		name : 'specialCode'
	}, {
		name : 'specialName'
	}, {
		name : 'runLogno'
	}, {
		name : 'shiftName'
	}, {
		name : 'operateMemo'
	}]);
	var right_ds = new Ext.data.GroupingStore({
		proxy : new Ext.data.HttpProxy({
			url : 'runlog/queryWorkerList.action'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'total',
			root : 'root'
		}, right_item),
		groupField : 'name',
		groupOnSort : true,
		sortInfo : {
			field : 'runLogno',
			direction : "DESC"
		}
	});
	right_ds.on('beforeload', function() {
		Ext.apply(this.baseParams, {
			method : "absent",
			specialcode : specialcode,
			formdate : fromdate,
			todate : todate
		});
	});
	right_ds.load({
		params : {
			start : 0,
			limit : 18
		}
	});
	
	var right_item_cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(), {
		header : '日志号',
		dataIndex : 'runLogno',
		align : 'center'
	}, {
		header : '专业',
		dataIndex : 'specialName',
		align : 'center'
	}, {
		header : '缺勤人员',
		dataIndex : 'workerName',
		align : 'center'
	}, {
		header : '缺勤原因',
		dataIndex : 'operateMemo',
		align : 'center',
		width : 300
	}]);
	var rightbbar = new Ext.PagingToolbar({
		pageSize : 18,
		store : right_ds,
		displayInfo : true,
		displayMsg : "共 {2} 条",
		emptyMsg : "没有记录",
		beforePageText : '页',
		afterPageText : "共{0}"
	});
	var rightGrid = new Ext.grid.GridPanel({
		id : 'right-grid',
		title : '缺勤人员',
		ds : right_ds,
		bbar : rightbbar,
		cm : right_item_cm,
		fitToFrame : true,
		view : new Ext.grid.GroupingView({
			forceFit : true,
			sortAscText : '正序',
			sortDescText : '倒序',
			columnsText : '列显示/隐藏',
			groupByText : '依本列分组',
			showGroupsText : '分组显示',
			groupTextTpl : '{text}'
		}),
		border : true,
		viewConfig : {
			forceFit : true
		}
	});
	
	var layout = new Ext.Viewport({
		layout : "border",
		border : false,
		items : [{
			region : "center",
			layout : 'fit',
			width : '400',
			border : false,
			collapsible : true,
			split : true,
			margins : '0 0 0 0',
			// 注入表格
			items : [leftGrid]
		}, {
			region : "east",
			layout : 'fit',
			width : '450',
			border : false,
			collapsible : true,
			split : true,
			margins : '0 0 0 0',
			// 注入表格
			items : [rightGrid]
		}, {
			region : "south",
			height : 20,
			layout : 'fit',
			border : false,
			collapsible : false,
			split : false,
			html : "<div style='font-size:10pt' style='overflow:auto;width:100%;height:100%'>值班人员说明:<font color='green'>■</font> 值班人员 <font color='navy'>■</font> 日志使用人员<font color='gray'>■</font> 代班人员 <font color='red'>■</font> 缺勤人员 </div>"
		}]
	});
	setTimeout(function() {
		Ext.get('loading').remove();
		Ext.get('loading-mask').fadeOut({
			remove : true
		});
	}, 250);
})