Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var specialcode = parent.document.getElementById("specialityCode").value;
	var fromdate = parent.document.getElementById("fromDate").value;
	var todate = parent.document.getElementById("toDate").value;
	var west_item = Ext.data.Record.create([{
		name : 'runLogid'
	}, {
		name : 'runLogno'
	}, {
		name : 'shiftId'
	}, {
		name : 'shiftName'
	}, {
		name : 'shiftTimeId'
	}, {
		name : 'shiftTimeName'
	}, {
		name : 'specialityCode'
	}, {
		name : 'specialityName'
	}, {
		name : 'weatherKeyId'
	}, {
		name : 'awayClassLeader'
	}, {
		name : 'takeClassLeader'
	}, {
		name : 'awayClassTime'
	}, {
		name : 'takeClassTime'
	}, {
		name : 'approveMan'
	}, {
		name : 'approveContent'
	}, {
		name : 'awayLeaderName'
	}, {
		name : 'takeLeaderName'
	}, {
		name : 'weathername'
	}]);

	var west_ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'runlog/RunLogMainQuery.action'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'total',
			root : 'root'
		}, west_item)
	});
	var west_item_cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(),{
		header : '运行日志号',
		dataIndex : 'runLogno',
		align : 'left'
	}, {
		header : '单元或专业',
		dataIndex : 'specialityName',
		align : 'center'
	}, {
		header : '值别名称',
		dataIndex : 'shiftName',
		align : 'center'
	}, {
		header : '班次名称',
		dataIndex : 'shiftTimeName',
		align : 'center'
	}, {
		header : '值班负责人',
		dataIndex : 'awayLeaderName',
		align : 'center'
	}, {
		header : '值班开始时间',
		dataIndex : 'takeClassTime',
		align : 'center'
	}, {
		header : '值班结束时间',
		dataIndex : 'awayClassTime',
		align : 'center'
	}, {
		header : '接班人',
		dataIndex : 'takeLeaderName',
		align : 'center'
	}, {
		header : '当班天气',
		dataIndex : 'weathername',
		align : 'center'
	}]);
	west_ds.on('beforeload', function() {
		Ext.apply(this.baseParams, {
			specialcode : specialcode,
			formdate : fromdate,
			todate : todate
		});
	});
	west_ds.load({
		params : {
			start : 0,
			limit : 18
		}
	});
	var gridbbar = new Ext.PagingToolbar({
		pageSize : 18,
		store : west_ds,
		displayInfo : true,
		displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
		emptyMsg : "没有记录",
		beforePageText : '页',
		afterPageText : "共{0}"
	});
	var westGrid = new Ext.grid.GridPanel({
		el:'orgGrid',
		ds : west_ds,
		cm : west_item_cm,
		collapsible : true,
		border : false,
		bbar : gridbbar,
		autoScroll:true,
		//width : Ext.get("orgGrid").getWidth(),
		split : true,
		viewConfig : {
			forceFit : true
		}
	});
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		items : [westGrid]
	});
	setTimeout(function() {
		Ext.get('loading').remove();
		Ext.get('loading-mask').fadeOut({
			remove : true
		});
	}, 250);
})
