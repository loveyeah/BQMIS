Ext.onReady(function(){
    Ext.QuickTips.init();  
    	function numberFormat(value) {
		if (value === "") {
			return value
		}
		value = String(value);
		// 整数部分
		var whole = value;
		// 小数部分
		var sub = ".0000";
		// 如果有小数
		if (value.indexOf(".") > 0) {
			whole = value.substring(0, value.indexOf("."));
			sub = value.substring(value.indexOf("."), value.length);
			sub = sub + "0000";
			if (sub.length > 3) {
				sub = sub.substring(0, 5);
			}
		}
		var r = /(\d+)(\d{3})/;
		while (r.test(whole)) {
			whole = whole.replace(r, '$1' + ',' + '$2');
		}
		v = whole + sub;
		return v;
	}
    var MyRecord = Ext.data.Record.create([{
		name : 'apartCode'
	},{
		name : 'blockName'
	}, {
		name : 'nodeCode'
	}, {
		name : 'nodeName'
	}, {
		name : 'collectNow'
	}, {
		name : 'collectHis'
	}, {
		name : 'descriptor'
	}, {
		name : 'nodeType'
	}, {
		name : 'maxValue'
	}, {
		name : 'minValue'
	}, {
		name : 'standardValue'
	}]); 

    var detailCM = new Ext.grid.ColumnModel([
    	new Ext.grid.RowNumberer({
			header : '序号',
			width : 35,
			align : 'left'
		}), 
		{
			header : "机组",
			sortable : true,
			hidden : true,
			dataIndex : 'apartCode',
			align : 'center'
		}, 
		{
			header : "机组",
			sortable : true,
			dataIndex : 'blockName',
			width : 80,
			align : 'center'
		}, {
			header : "节点编码",
			width : 100,
			sortable : true,
			align : 'left',
			dataIndex : 'nodeCode'
		}, {
			header : "节点名称",
			width : 250,
			sortable : true,
			dataIndex : 'nodeName',
			align : 'left'
		}, {
			header : "实时采集",
			width : 100,
			sortable : true,
			dataIndex : 'collectNow',
			align : 'center',
			renderer : function(v) {
				if (v == '1') {
					return "是";
				}
				if (v == '0') {
					return "否";
				}
			}
		}, {
			header : "历史采集",
			width : 120,
			sortable : true,
			dataIndex : 'collectHis',
			align : 'center',
			renderer : function(v) {
				if (v == '1') {
					return "是";
				}
				if (v == '0') {
					return "否";
				}
			}
		}, {
			header : "节点描述",
			width : 120,
			sortable : true,
			dataIndex : 'descriptor',
			align : 'left'
		}, {
			header : "节点类别",
			width : 120,
			sortable : true,
			dataIndex : 'nodeType',
			align : 'center',
			renderer : function(v) {
				if (v == '1') {
					return "模拟量";
				}
				if (v == '2') {
					return "信号量";
				}
			}
		}, {
			header : "最小值",
			width : 120,
			sortable : true,
			renderer : numberFormat,
			dataIndex : 'minValue',
			align : 'center'
		}, {
			header : "最大值",
			width : 120,
			sortable : true,
			renderer : numberFormat,
			dataIndex : 'maxValue',
			align : 'center'
		}, {
			header : "标准值",
			width : 120,
			sortable : true,
			renderer : numberFormat,
			dataIndex : 'standardValue',
			align : 'center'
		}]);
    detailCM.defaultSortable = true;
    // 数据源
    var detailStore = new Ext.data.Store({
        proxy :   dataProxy = new Ext.data.HttpProxy({
				url : 'manager/findDcsNode.action'
			}),
        reader : new Ext.data.JsonReader({
        	root : 'list',
        	totalProperty : 'totalCount'
        }, MyRecord)
    }); 
    detailStore.on("beforeload",function(){ 
		Ext.Msg.wait("正在加载数据!,请等待...");    
		Ext.apply(   
        this.baseParams,   
        {   
        	sys:apartCodeBox.getValue(),
            queryKey : queryKey.getValue()
        });  
	});
    detailStore.on("load",function(){ 
		Ext.Msg.hide();
    });
    var bbar = new Ext.PagingToolbar({
		pageSize : 20,
		store : detailStore,
		displayInfo : true,
		displayMsg : "一共 {2} 条",
		emptyMsg : "没有记录",
		beforePageText : '',
		afterPageText : '共{0}页'
	});
	var queryKey = new Ext.form.TextField({
		id:'queryKey',
		emptyText:'结点编码或名称模糊查询',
		width:250
	});
	// 机组
	var storeChargeBySystem = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'workticket/getDetailEquList.action'
		}),
		reader : new Ext.data.JsonReader({
			root : 'list'
		}, [{
			name : 'blockCode',
			mapping : 'blockCode'
		}, {
			name : 'blockName',
			mapping : 'blockName'
		}])
	});
	storeChargeBySystem.on("load",function(){
		var all = new Ext.data.Record({
			'blockCode':'%',
			'blockName':'所有机组'
		});
		storeChargeBySystem.insert(0,all);
		apartCodeBox.setValue(all.data.blockCode);
	});
	storeChargeBySystem.load();
	var apartCodeBox = new Ext.form.ComboBox({
		id : 'apartCode',
		fieldLabel : "机组", 
		store : storeChargeBySystem,
		displayField : "blockName",
		valueField : "blockCode",
		hiddenName : 'node.apartCode',
		selectOnFocus:true,
		mode : 'local',
		triggerAction : 'all',
		value : '',
		readOnly : true,
		width : 150,
		listeners:{
			'select':function(){ 
				 detailStore.reload();
			}
		}
	}); 
	var tbar = new Ext.Toolbar({
		id : 'tbar',
		items : [apartCodeBox,queryKey, new Ext.Button({
			id : 'btnQuery',
			text : '查询',
			handler : function() {
				  detailStore.reload();
			}
		})]
	});
    
    // 待选作业方式grid
    var detailGrid = new Ext.grid.GridPanel({
    	tbar:tbar,
		enableColumnMove : false,
		enableColumnHide : true,
        ds : detailStore,
        cm : detailCM,
        height : 500,
        sm : new Ext.grid.RowSelectionModel({
			singleSelect : true
		}),
        border : false, 
        autoScroll : true,
        bbar : bbar,
        viewConfig : {
			forceFit : false,
			getRowClass : function(record, rowIndex, rowParams, store) {
				// 禁用数据显示红色
				if (record.data.isCorrespond == "N") {
					return 'x-grid-record-blue';
				} else {
					return '';
				}  
			}
		}
    });
    
    detailGrid.on('rowdblclick', function() {
    	if(confirm("确定选择吗?"))
    	{
	    	var record = detailGrid.getSelectionModel().getSelected();  
	    	window.returnValue = record.data;
	    	window.close();
    	}
    }); 
    new Ext.Viewport({
        enableTabScroll : true,
        layout : 'fit',
		margins : '10, 10, 10 ,10',
        items : [detailGrid]
    }); 
     detailStore.load({
    	params : {
			start : 0,
			limit : 20
		}
    });  
});