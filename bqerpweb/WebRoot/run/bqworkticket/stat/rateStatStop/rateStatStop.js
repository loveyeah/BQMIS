Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif'; 
Ext.onReady(function() { 
	Ext.QuickTips.init(); 
	var DataRecord = Ext.data.Record.create([{
		name : 'workticketTypeCode'
	}, {
		name : 'workticketTypeName'
	},{
		name:'startWorkticketNo' 
	},{
		name:'endWorkticketNo' 
	}, {
		name : 'workticketQualifiedNum',
		type : 'int'
	}, {
		name : 'invalidNum',
		type : 'int'
	}, {
		name : 'usedStanderworkticketNum',
		type : 'int'
	}, {
		name : 'usedStaworkQualifiedNum',
		type : 'int'
	}, {
		name : 'totlaNum',
		type : 'int'
	}
	
	]);  
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
	var repairDeptStore = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'bqfailure/queryRepairDeptStopList.action',
			method : 'post'
		}),
		reader : new Ext.data.JsonReader({
			root : 'list'
		}, [{
			name : 'deptCode',
			mapping : 'deptCode'
		}, {
			name : 'deptName',
			mapping : 'deptName'
		}])
	});
//	repairDeptStore.on('load',function(){
//		
//		var record1=new Ext.data.Record({
//    	deptName:'所有部门',
//    	deptCode:'%'}
//    	);
//    	repairDeptStore.insert(0,record1);
//    	repairDepComboBox.setValue('%');
//		gridDs.load();
//	})
	repairDeptStore.load();
    var repairDepComboBox = new Ext.form.ComboBox({
		id : 'repairDep-combobox',
		store : repairDeptStore,
		fieldLabel : "检修部门<font color='red'>*</font>",
		valueField : "deptCode",
		displayField : "deptName",
		mode : 'local',
		forceSelection : true,
		hiddenName : 'failure.repairDep',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : false,
		blankText : '所有部门',
		emptyText : '所有部门',
		anchor : '95%'
	});
	var btnQuery = new Ext.Button({
		text : '查询',
		iconCls : 'query',
		handler : function() { 
			Ext.Msg.wait("正在查询,请等待...");
			gridDs.load();
		}
	});
	
	var gridTbar = new Ext.Toolbar({
		items:['统计月份:',monthDate,'检修部门:',repairDepComboBox,'-',btnQuery]
	});
	var gridDs = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'workticket/getRateStatData.action'
		}),
		reader : new Ext.data.JsonReader({ 
		}, DataRecord) 
	});   
	gridDs.on('beforeload', function() {
		Ext.apply(this.baseParams, {
			date : monthDate.getValue(),
			dept : repairDepComboBox.getValue()
			// add by liuyi 091219 
			,stop : 'stop'
		});
	}); 
	gridDs.on('load', function() {
		var  workticketQualifiedNum = 0;
		var	usedStanderworkticketNum = 0;
		var	usedStaworkQualifiedNum = 0; 
		var	invalidNum=0;
		var	totlaNum=0;
		for(var i=0;i<gridDs.getCount();i++)
		{
			var rec = gridDs.getAt(i);
	        workticketQualifiedNum+= rec.get("workticketQualifiedNum");
	        usedStanderworkticketNum+= rec.get("usedStanderworkticketNum");
	        usedStaworkQualifiedNum+= rec.get("usedStaworkQualifiedNum");
			invalidNum+=rec.get("invalidNum");
			totlaNum+=rec.get("totlaNum");
		}
		var o = new DataRecord({
			workticketTypeName : '<font color="red">合计</font>',
            workticketQualifiedNum:workticketQualifiedNum,
            usedStanderworkticketNum:usedStanderworkticketNum,
            usedStaworkQualifiedNum:usedStaworkQualifiedNum,
			invalidNum:invalidNum,
			totlaNum:totlaNum
		});
		gridDs.add(o);
		function to2bits(flt) {
			if (parseFloat(flt) == flt)
				// return Math.round(flt * 100) / 100;
				return Math.round(flt * 10000) / 100;
			else
				return 0;
		} 
		 bb.setValue("本单位评价:本月工作票共有   "+totlaNum+"  份,合格   "+workticketQualifiedNum+"  份,合格率  "+to2bits(workticketQualifiedNum/totlaNum)+"  %,  标准票使用率:   "+to2bits(usedStaworkQualifiedNum/workticketQualifiedNum)+"%");	
		Ext.Msg.hide();
	}); 
	var gridCm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(), {
		header : '工作票类别',
		dataIndex : 'workticketTypeName',
		width:150,
		align : 'left' 
	}, {
		header : '起始号',
		dataIndex : 'startWorkticketNo',
		align : 'left' 
	}, {
		header : '结束号',
		dataIndex : 'endWorkticketNo',
		align : 'left' 
	}, {
		header : '统计数量',
		dataIndex : 'totlaNum',
		align : 'left' 
	}, {
		header : '合格数量',
		dataIndex : 'workticketQualifiedNum',
		align : 'left' 
	}, {
		header : '未执行/作废数量',
		width:160,
		dataIndex : 'invalidNum',
		align : 'left' 
	}, {
		header : '使用标准票数量',
		width:160,
		dataIndex : 'usedStanderworkticketNum',
		align : 'left' 
	}, {
		header : '使用标准票合格数量',
		width:160,
		dataIndex : 'usedStaworkQualifiedNum',
		align : 'left' 
	}]);
	var grid = new Ext.grid.GridPanel({
		ds : gridDs,
		tbar:gridTbar,
		cm : gridCm,   
		autoWidth : true, 
		autoScroll : true,
		enableColumnMove : false,
		enableColumnHide : true,
		border : false 
	}); 
	var bb = new Ext.form.TextArea({
		id:'totalMsg' ,
		readOnly:true
	});

	// 设定布局器及面板
	var layout = new Ext.Viewport({
		layout : 'border',
		border : false,
		items : [	{
			layout:'fit',
			height:80,
			margins : '0 0 0 0',
			region : 'south', 
			items : bb
		},
			{
		    layout : 'fit',
		    autoScroll:true,
			margins : '0 0 0 0', 
			region : 'center', 
			items : grid
		}]
	}); 
gridDs.load();
});