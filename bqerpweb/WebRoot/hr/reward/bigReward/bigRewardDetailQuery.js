Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	var arg = window.dialogArguments;
	var deptId = arg.deptId;
	var bigRewardId = arg.bigRewardId;
	var deptname = arg.deptName;

	/**
	 * 金钱格式化
	 */
	function moneyFormat(v) {
		if (v == null || v == "") {
			return "0.0000";
		}
		v = (Math.round((v - 0) * 10000)) / 10000;
		v = (v == Math.floor(v))
				? v + ".0000"
				: ((v * 10 == Math.floor(v * 10))
						? v + "000"
						: ((v * 100 == Math.floor(v * 100)) ? v + "00" : ((v
								* 1000 == Math.floor(v * 1000)) ? v + "0" : v)));
		v = String(v);
		var ps = v.split('.');
		var whole = ps[0];
		var sub = ps[1] ? '.' + ps[1] : '.0000';
		var r = /(\d+)(\d{3})/;
		while (r.test(whole)) {
			whole = whole.replace(r, '$1' + ',' + '$2');
		}
		v = whole + sub;
		if (v.charAt(0) == '-') {
			return '-' + v.substr(1);
		}
		return v;
	}
	/**
	 * 数字格式化
	 */
	function numberFormat(v) {
		v = (Math.round((v - 0) * 100)) / 100;
		v = (v == Math.floor(v)) ? v + ".00" : ((v * 10 == Math.floor(v * 10))
				? v + "0"
				: v);
		v = String(v);
		var ps = v.split('.');
		var whole = ps[0];
		var sub = ps[1] ? '.' + ps[1] : '.00';
		var r = /(\d+)(\d{3})/;
		while (r.test(whole)) {
			whole = whole.replace(r, '$1' + ',' + '$2');
		}
		v = whole + sub;
		if (v.charAt(0) == '-') {
			return '-' + v.substr(1);
		}
		return v;
	}
//	var deptStrore = new Ext.data.Store({
//		proxy : new Ext.data.HttpProxy({
//			url : 'hr/queryRewardDeptList.action'
//		}),
//		autoLoad : true,
//		reader : new Ext.data.JsonReader({
//			root : 'list'
//		}, [{
//			name : 'deptId',
//			mapping : 0
//		}, {
//			name : 'deptName',
//			mapping : 1
//		}])
//	});
//
//	deptStrore.on("beforeload", function() {
//		Ext.apply(this.baseParams, {
//			bigRewardId : bigRewardId
//
//		});
//
//	});
//	deptStrore.load();
//	
//	var deptNameCom = new Ext.form.ComboBox({
//		id : 'dept',
//		fieldLabel : "部门",
//		store : deptStrore,
//		displayField : "deptName",
//		valueField : "deptId",
//		mode : 'remote',
//		triggerAction : 'all',
//	    selectOnFocus: true,
//		value : deptId,
//		readOnly : true,
//		anchor : "85%",
//		listeners : {
//			"select" : function(v,record) {
//				  deptId = this.value;
//				  
//				  deptname=record.get("deptName")
//				 
////		         init(deptId,deptname);
//				 query();
//
//			}
//		}
//	})
	
		 //update by sychen 20100915
	 var deptNameCom =new Ext.form.TextField({
	   id:'dept',
	   fieldLabel : "部门",
	   readOnly : true,
	   anchor : "85%"
	});

	function  init(v1,v2)
	{    
		if(v1!=null&&v2!=null)
		{
					Ext.getCmp('dept').value =v2;
					Ext.getCmp('dept').RawValue=v2;
					deptNameCom.autoShow=false;
					
				
		}
	}
	init(deptId,deptname);
	// 定义grid中的数据
	var gridData = new Ext.data.Record.create([{
		name : 'empId',
		mapping : 0

	},		// 部门
			{
				name : 'deptName',
				mapping : 1

			}, {
				name : 'banzu',
				mapping : 2
			}, {
				name : 'empName',
				mapping : 3
			}, {
				name : 'bigRewardBase',
				mapping : 4
			}, {
				name : 'monthAward',
				mapping : 5
			}, {
				name : 'bigRewardNum',
				mapping : 6
			},
			{
			  name:'assessmentFrom',
			  mapping : 7
			},
			{
			  name:'assessmentTo',
			  mapping : 8
			},
			{
			  name:'absenceDays',
			  mapping : 9
			}
			]);

	var gridStore = new Ext.data.JsonStore({
		url : 'hr/queryDetailBigRewardByDept.action',
		root : 'list',
		totalProperty : 'totalCount',
		fields : gridData
	});

	// 载入数据
	function query() {
		gridStore.baseParams = {
			bigRewardId : bigRewardId,
			deptId : deptId
		}
		gridStore.load();

	}
	var tbar = new Ext.Toolbar({
		items : ['部门:', deptNameCom, {
			id : 'query',
			text : "查询",
			iconCls : 'query',
			tabIndex:1,
			handler : query
		}]
	});
	var grid = new Ext.grid.GridPanel({
		autoWidth : true,
		store : gridStore,
		region : 'center',
		sm : new Ext.grid.RowSelectionModel({
			// 单选
			singleSelect : true
		}),
		columns : [new Ext.grid.RowNumberer({
			header : '行号',
			width : 35
		}), {
			header : '部门',
			align : 'center',
			width : 100,
			sortable : true,
			dataIndex : 'deptName'
		}, {
			header : '班组',
			align : 'center',
			width : 100,
			sortable : true,
			dataIndex : 'banzu'
		}, {
			header : '姓名',
			width : 100,
			align : 'center',
			sortable : true,
			dataIndex : 'empName'
		}, {
			header : "大奖基数",
			width : 100,
			sortable : true,
			align : 'center',
			//modify by kzhang 2010727
			//dataIndex : 'transQty'
			dataIndex : 'bigRewardBase'
		}, {
			header : "大奖系数",
			width : 100,
			sortable : true,
			align : 'center',
			dataIndex : 'monthAward'
		}, {
			header : "考勤开始时间",
			width : 100,
			sortable : true,
			align : 'center',
			dataIndex : 'assessmentFrom'
		},{
			header : "考勤结束时间",
			width : 100,
			sortable : true,
			align : 'center',
			dataIndex : 'assessmentTo'
		},{
			header : "缺勤天数",
			width : 100,
			sortable : true,
			align : 'center',
			dataIndex : 'absenceDays'
		},{
			header : "大奖金额",
			width : 100,
			sortable : true,
			dataIndex : 'bigRewardNum',
			renderer : function(value, cellmeta, record, rowIndex, columnIndex,
					store) {
				if (rowIndex < store.getCount() - 1) {
					var totalSum = 0;
					var bigRewardNum = record.data.bigRewardNum
					return bigRewardNum;
				} else {
					totalSum = 0;
					for (var i = 0; i < store.getCount() - 1; i++) {
						totalSum += store.getAt(i).get('bigRewardNum');
					}
					return "<font color='red'>" + numberFormat(totalSum)
							+ "</font>";
				}
			}
		}],
		tbar : tbar,
		/*
		 * bbar : new Ext.PagingToolbar({ pageSize : Constants.PAGE_SIZE, store :
		 * gridStore, displayInfo : true, displayMsg : Constants.DISPLAY_MSG,
		 * emptyMsg : Constants.EMPTY_MSG }),
		 */
		frame : false,
		border : false,
		enableColumnHide : true,
		enableColumnMove : false
	})

	function addLine() {
		// 统计行
		var newRecord = new gridData({
			'bigDetailId' : '',
			'bigRewardId' : '',
			'deptId' : '',
			'deptName' : '合计',
			'empCount' : '',
			'bigRewardNum' : '',
			'isNewRecord' : "total"
		});

		// 原数据个数
		var count = gridStore.getCount();
		// 停止原来编辑
		grid.stopEditing();
		// 插入统计行
		gridStore.insert(count, newRecord);
		grid.getView().refresh();
	};
	gridStore.on("load", addLine);
	// 设定布局器及面板
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		items : [grid]
	});
	query();
});