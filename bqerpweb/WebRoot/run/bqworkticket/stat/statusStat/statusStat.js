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
		name : 'notIssuedNum',
		type : 'int'
	}, {
		name : 'issuedNum',
		type : 'int'
	}, {
		name : 'receivedNum',
		type : 'int'
	}, {
		name : 'approvaledNum',
		type : 'int'
	}, {
		name : 'licensedNum',
		type : 'int'
	}, {
		name : 'achievedNum',
		type : 'int'
	}, {
		name : 'invalidNum',
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
	var repairDeptRec=new Ext.data.Record.create([{
		name : 'deptCode'},{
		name : 'deptName'
	}])
	var repairDeptStore = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'bqfailure/repairDept.action',
			method : 'post'
		}),
		reader : new Ext.data.JsonReader({
			root : 'list'
		}, repairDeptRec
//		[{
//			name : 'deptCode'
//			,
//			mapping : 'deptCode'
//		}, {
//			name : 'deptName'
//			,
//			mapping : 'deptName'
//		}]
		)
	});
	repairDeptStore.load();
	repairDeptStore.on('load',function(){
		var item= new repairDeptRec({
			deptCode : '',
			deptName : '所有部门'
		})
		var num=repairDeptStore.getCount()+1
		repairDeptStore.insert(num,item);
	})
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
//		allowBlank : false,
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
		items:['统计月份',monthDate,'-检修部门',repairDepComboBox,'-',btnQuery]
	});
	var gridDs = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'workticket/getStatusStatData.action'
		}),
		reader : new Ext.data.JsonReader({ 
		}, DataRecord) 
	});   
	gridDs.on('beforeload', function() {
		Ext.apply(this.baseParams, {
			date : monthDate.getValue(),
			dept : repairDepComboBox.getValue()
		});
	}); 
	gridDs.on('load', function() {
	
		var  notIssuedNum = 0;
		var	issuedNum = 0;
		var	receivedNum = 0;
		var	approvaledNum = 0;
		var	licensedNum = 0;
		var	achievedNum=0;
		var	invalidNum=0;
		var	totlaNum=0;
		for(var i=0;i<gridDs.getCount();i++)
		{
			var rec = gridDs.getAt(i);
			notIssuedNum += rec.get("notIssuedNum");
			issuedNum += rec.get("issuedNum");
			receivedNum += rec.get("receivedNum");
			approvaledNum += rec.get("approvaledNum");
			licensedNum += rec.get("licensedNum");
			achievedNum+=rec.get("achievedNum");
			invalidNum+=rec.get("invalidNum");
			totlaNum+=rec.get("totlaNum");
		}
		var o = new DataRecord({
			workticketTypeName : '<font color="red">合计</font>',
			notIssuedNum : notIssuedNum,
			issuedNum : issuedNum,
			receivedNum : receivedNum,
			approvaledNum : approvaledNum,
			licensedNum : licensedNum,
			achievedNum:achievedNum,
			invalidNum:invalidNum,
			totlaNum:totlaNum
		});
		gridDs.add(o);   
		 bb.setValue("共有工作票   "+totlaNum+"   张,其中:\n作废票  "+invalidNum+"  张,占    "
		 				+to2bits(invalidNum/totlaNum)+"%;\n已开工  "+licensedNum+"  张,占    "+to2bits(licensedNum/totlaNum)
		 				+"%;\n已完成  "+achievedNum+"  张,占    "+to2bits(achievedNum/totlaNum)+"%");	
		 Ext.Msg.hide();
	}); 
	function to2bits(flt) {
		if (parseFloat(flt) == flt)
			//return Math.round(flt * 100) / 100;
			return Math.round(flt * 10000) / 100;
		else
			return 0;
	} 

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
		header : '未签发',
		dataIndex : 'notIssuedNum',
		align : 'left' 
	}, {
		header : '已签发',
		dataIndex : 'issuedNum',
		align : 'left' 
	}, {
		header : '已接收',
		dataIndex : 'receivedNum',
		align : 'left' 
	}, {
		header : '已批准',
		dataIndex : 'approvaledNum',
		align : 'left' 
	}, {
		header : '已许可',
		dataIndex : 'licensedNum',
		align : 'left' 
	}, {
		header : '已完成',
		dataIndex : 'achievedNum',
		align : 'left' 
	}, {
		header : '未执行/作废',
		dataIndex : 'invalidNum',
		align : 'left' 
	}, {
		header : '总计',
		dataIndex : 'totlaNum',
		align : 'left' 
	}]); 
	var bb = new Ext.form.TextArea({
		id:'totalMsg' ,
		readOnly:true
	});
	
	
	var grid = new Ext.grid.GridPanel({
		ds : gridDs,
		tbar:gridTbar, 
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
		items : [
			{
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