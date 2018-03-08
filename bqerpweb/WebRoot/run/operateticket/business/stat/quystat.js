Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	Ext.QuickTips.init();

	var DataRecord = Ext.data.Record.create([{
		name : 'optickectType'
	}, {
		name : 'beginNo'
	}, {
		name : 'endNo'
	}, {
		name : 'statCount',
		type : 'int'
	}, {
		name : 'quyCount',
		type : 'int'
	}, {
		name : 'invaliCount',
		type : 'int'
	}, {
		name : 'useStandOpCount',
		type : 'int'
	}, {
		name : 'useStandOpQuyCount',
		type : 'int'
	}]);
	var nowdate = new Date();
	var date1 = nowdate.toLocaleDateString();
	var exdate;
	if (date1.substring(6, 7) == '月') {
		exdate = date1.substring(0, 4) + '-0' + date1.substring(5, 6);
	} else {
		exdate = date1.substring(0, 4) + "-" + date1.substring(5, 7);
	}
	var monthDate = new Ext.form.TextField({
		name : '_monthDate',
		value : exdate,
		id : 'monthDate',
		fieldLabel : "月份",
		style : 'cursor:pointer',
		cls : 'Wdate',
		// width : 150,
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M',
					dateFmt : 'yyyy-MM',
					alwaysUseStartDate : true
				});
			}
		}
	});
	var storeSpecials = new Ext.data.JsonStore({
		url : 'opticket/getDetailOpticketSpecials.action',
		root : 'list',
		fields : ['specialityCode', 'specialityName']
	});
	storeSpecials.on('load',function(){
		storeSpecials.remove(storeSpecials.getAt(0));
		//cbxOpticketSpecials.setValue(storeSpecials.getAt(0).get('specialityCode'));
		var record1=new Ext.data.Record({
    	specialityName:'所有',
    	specialityCode:'%'}
    	);
    	storeSpecials.insert(0,record1);
    	cbxOpticketSpecials.setValue('%');
		gridDs.load();
	})
	var cbxOpticketSpecials = new Ext.form.ComboBox({
		id : 'specialityCode',
		fieldLabel : "专业",
		triggerAction : 'all',
		store : storeSpecials,
		displayField : "specialityName",
		valueField : "specialityCode",
		mode : 'local',
		readOnly : true,
		allowBlank : false
	})
	var btnQuery = new Ext.Button({
		text : '查询',
		iconCls : 'query',
		handler : function() {
			Ext.Msg.wait("正在查询,请等待...");
			gridDs.load();
		}
	});

	var gridTbar = new Ext.Toolbar({
		items : ['统计月份', monthDate, '-检修专业', cbxOpticketSpecials, '-', btnQuery]
	});
	var gridDs = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'opticket/getOptickectQuyStat.action'
		}),
		reader : new Ext.data.JsonReader({}, DataRecord)
	});
	gridDs.on('beforeload', function() {
		Ext.apply(this.baseParams, {
			date : monthDate.getValue(),
			specialCode : cbxOpticketSpecials.getValue()
		});
	});
	gridDs.on('load', function() {
		var statCount = 0;
		var quyCount = 0;
		var invaliCount = 0;
		var useStandOpCount = 0;
		var useStandOpQuyCount = 0;
		var totlaNum = 0;
		for (var i = 0; i < gridDs.getCount(); i++) {
			var rec = gridDs.getAt(i);
			statCount += rec.get("statCount");
			quyCount += rec.get("quyCount");
			invaliCount += rec.get("invaliCount");
			useStandOpCount += rec.get("useStandOpCount");
			useStandOpQuyCount += rec.get("useStandOpQuyCount");
		}
		var o = new DataRecord({
			optickectType : '<font color="red">合计</font>',
			statCount : statCount,
			quyCount : quyCount,
			invaliCount : invaliCount,
			useStandOpCount : useStandOpCount,
			useStandOpQuyCount : useStandOpQuyCount
		});
		gridDs.add(o);
		bb.setValue("本单位评价:本月操作票共有   " + statCount + "  份,合格   "
				+ quyCount + "  份,合格率  "
				+ to2bits(quyCount / statCount)
				+ "  %,  标准票使用率:   "
				+ to2bits(useStandOpCount / statCount)
				+ "%");
		Ext.Msg.hide();
	});
	function to2bits(flt) {
		if (parseFloat(flt) == flt)
			// return Math.round(flt * 100) / 100;
			return Math.round(flt * 10000) / 100;
		else
			return 0;
	}

	var gridCm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(), {
		header : '操作票类别',
		dataIndex : 'optickectType',
		width : 150,
		align : 'left'
	}, {
		header : '起始号',
		dataIndex : 'beginNo',
		align : 'left'
	}, {
		header : '结束号',
		dataIndex : 'endNo',
		align : 'left'
	}, {
		header : '统计数量',
		dataIndex : 'statCount',
		align : 'left'
	}, {
		header : '合格数量',
		dataIndex : 'quyCount',
		align : 'left'
	}, {
		header : '未执行/作废',
		dataIndex : 'invaliCount',
		align : 'left'
	}, {
		header : '使用标准票数量',
		dataIndex : 'useStandOpCount',
		align : 'left'
	}, {
		header : '使用标准票合格数量',
		dataIndex : 'useStandOpQuyCount',
		align : 'left'
	}]);
	var bb = new Ext.form.TextArea({
		id : 'totalMsg',
		readOnly : true
	});

	var grid = new Ext.grid.GridPanel({
		ds : gridDs,
		tbar : gridTbar,
		cm : gridCm,
		autoWidth : true,

		enableColumnMove : false,
		enableColumnHide : true,
		border : false
	});
	// 设定布局器及面板
	var layout = new Ext.Viewport({
		layout : 'border',
		border : false,
		items : [{
			layout : 'fit',
			height : 80,
			margins : '0 0 0 0',
			region : 'south',
			items : bb
		}, {
			layout : 'fit',
			autoScroll : true,
			margins : '0 0 0 0',
			region : 'center',
			items : grid
		}]
	});
	storeSpecials.load();
});