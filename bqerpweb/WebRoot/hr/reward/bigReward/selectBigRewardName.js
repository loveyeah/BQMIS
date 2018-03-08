Ext.ns("bigReward.selectName");
bigReward.selectName = function() {
	var rewardMonth = new Date().format('Y-m');
	var bigRewardMonth = new Ext.form.TextField({
		id : 'bigRewardMonth',
		fieldLabel : '月份',
		name : 'reward.bigRewardMonth',
		anchor : "85%",
		value : rewardMonth,
		readOnly : true,
		listeners : {
			focus : function() {
				WdatePicker({
					isShowClear : true,
					startDate : '%y-%M',
					dateFmt : 'yyyy-MM'
				});
			}
		}

	});
	
	// 定义选择行
	var sm = new Ext.grid.RowSelectionModel({
		singleSelect : true
	});

	// grid中的数据Record
	var gridRecord = new Ext.data.Record.create([{
		name : 'bigRewardId',
		mapping : 0
	}, {
		name : 'bigRewardMonth',
		mapping : 1
	}, {
		name : 'bigAwardId',
		mapping : 2
	}, {
		name : 'bigAwardName',
		mapping : 3
	}, {
		name : 'bigRewardBase',
		mapping : 4
	}, {
		name : 'handedDate',
		mapping : 5
	}, {
		name : 'fillBy',
		mapping : 6
	}, {
		name : 'fillByName',
		mapping : 7
	}, {
		name : 'fillDate',
		mapping : 8
	}, {
		name : 'workFlowState',
		mapping : 9
	}, {
		name : 'workFlowNo',
		mapping : 10
	}, {
		name : 'awardName',
		mapping : 11
	}]);
	
	// grid的store
	var queryStore = new Ext.data.JsonStore({
		url : 'hr/getBigReward.action',
		root : 'list',
		totalProperty : 'totalCount',
		fields : gridRecord
	});
	
	function query() {
		queryStore.baseParams = {
			rewardMonth : bigRewardMonth.getValue()
		};
		queryStore.load({
			params : {
				start : 0,
				limit : 18
			}
		});
	}
	
	var btnConfrim = new Ext.Button({
			text:"确认",
			id:'btnConfrim',
			iconCls:'confirm',
			handler:function(){
				var obj = getValue();
			    setValue(obj.key, obj.value);
				win.hide()
			}
		});
	
	function setValue(key,value) {
		var d1 = new KeyValue({
			key : key,
			value : value
		});  
		cbStore.removeAll();
		cbStore.add(d1);
		combo.setValue(key);
	}
	function getValue() { 
		var record = grid.getSelectionModel().getSelected(); 
		if (typeof(record) != "undefined") {
			var ro = new Object(); 
			ro.key = record.data.bigRewardId; 
			ro.value = record.data.bigAwardName;
			// add by liuyi 20100421 
			return ro;
		} else {
			return null;
		}
	};	
		
	var btnQuery = new Ext.Button({
			text:"查询",
			id:'btnQuery',
			iconCls:'query',
			handler:query
		});
	// 页面的Grid主体
	var grid = new Ext.grid.GridPanel({
		region : 'north',
		layout : 'column',
		store : queryStore,
		height : 300,
		columns : [new Ext.grid.RowNumberer({
			header : '行号',
			width : 35
		}), {
			header : "ID",
			hidden : true,
			width : 100,
			dataIndex : 'bigRewardId'
		}, {
			header : "月份",
			sortable : true,
			width : 100,
			dataIndex : 'bigRewardMonth'
		}, {
			header : "大奖名称",
			sortable : true,
			width : 100,
			dataIndex : 'bigAwardName'
		}, {
			header : "大奖基数",
			sortable : true,
			width : 100,
			dataIndex : 'bigRewardBase'
		}, {
			header : "填写人",
			width : 100,
			dataIndex : 'fillByName'
		}, {
			header : "填写时间",
			width : 100,
			dataIndex : 'fillDate'
		}],
		tbar : ['月份：','-',bigRewardMonth,btnQuery],
		sm : sm,
		frame : false,
		border : false,
		enableColumnMove : false
	});
	
	var KeyValue = new Ext.data.Record.create([{
			name : 'key'
		}, {
			name : 'value'
		}]);  
	var cbStore = new Ext.data.Store({
		reader : new Ext.data.JsonReader({}, KeyValue)
	});  
	var combo = new Ext.form.ComboBox({
		fieldLabel : "大奖名称",
		store : cbStore,
		mode : 'local',
		hiddenName :'dept',
		name : 'dept',
		width: 180,
		valueField : 'key',
		displayField : 'value',
		editable : true,
		triggerAction : 'all',
		forceSelection:true,
		readOnly : true,
		onTriggerClick : function() {
			if(!this.disabled)
			{
				win.show();
			}
		}
	});
	
	var win = new Ext.Window({
		closeAction : 'hide',
		width:600,
		height:450,
		modal : true,
		layout : 'border',
		items : [{
			region : 'center',
			layout : 'fit',
			split:true,
			items : [grid]
		}],
		buttons:[btnConfrim,{
			text:"取消",
			iconCls:'cancer',
			handler:function(){
				win.hide();
			}
		}]
	}); 
	
	return {
		grid : grid,
		win : win,
		combo:combo,
		btnConfrim : btnConfrim
	}

}