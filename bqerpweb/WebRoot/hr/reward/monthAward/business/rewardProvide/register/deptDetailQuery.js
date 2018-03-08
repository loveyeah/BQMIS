Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	var arg = window.dialogArguments;
	var deptId = arg.deptId;
	var  rewardId= arg.rewardId;
	var deptname = arg.deptName;
   
	
		var 	standardDays="";
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
//			url : 'hr/getRewardDetailDeptList.action'
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
//			rewardId : rewardId
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
//	    selectOnFocus: true,
//		mode : 'remote',
//		triggerAction : 'all',
//		value : deptId,
//		readOnly : true,
//		anchor : "85%",
//		listeners : {
//			"select" : function(v,record) {
//                 
//				  deptId = this.value;
//				  deptname=record.get("deptName")
//				  query();
//
//			}
//		}
//	})
//	 deptNameCom.expand(false);
	
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
		}
	}
	init(deptId,deptname);
	// 定义grid中的数据
	var gridData = new Ext.data.Record.create([
		  {
		   name : 'deptName',
		   mapping : 0
  
	       },		
			 {
				name : 'banzu',
				mapping : 1
			}, {
				name : 'newEmpCode',
				mapping : 2
			}, {
				name : 'empName',
				mapping : 3
			}, {
				name : 'monthBase',
				mapping : 4
			}, {
				name : 'coefficient',
				mapping : 5
			},{
				name : 'monthAward',
				mapping : 6
			}, {
				name : 'monthMoney',
				mapping : 7
			},{
				name : 'lingHua',
				mapping : 8
			},{
				name : 'addValue',
				mapping : 9
			},{
				name : 'totalMoney',
				mapping : 13
			},{
				name : 'absenceDays',//add 
				mapping : 11
			},{
				name : 'standardsDays',
				mapping : 10
			},{
				name : 'percent',
				mapping : 15
			}]);

	var gridStore = new Ext.data.JsonStore({
		url : 'hr/findDetailMonthRewardByDept.action',
		root : 'list',
		totalProperty : 'totalCount',
		fields : gridData
	});

	// 载入数据
	function query() {
		gridStore.baseParams = {
			rewardId : rewardId,
			deptId : deptId
		}
		gridStore.load();

	}
	var tbar = new Ext.Toolbar({
		items : ['部门:', deptNameCom, {
			id : 'query',
			text : "查询",
			tabIndex:1,
			iconCls : 'query',
			handler : query
		}, "-", {
		id : 'btnPrint',
		text : '导 出',
		iconCls : 'export',
		handler : function() {
			reportexport();
		}
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
			width : 35,
			sortable : true,
			dataIndex : 'deptName'
		}, {
			header : '班组',
			align : 'center',
			width : 35,
			sortable : true,
			dataIndex : 'banzu'
		}, {
			header : '人员编号',
			align : 'center',
			width : 100,
			sortable : true,
			dataIndex : 'newEmpCode'
		},{
			header : '姓名',
			width : 100,
			align : 'center',
			sortable : true,
			dataIndex : 'empName'
		}, {
			header : "月奖基数",
			width : 100,
			sortable : true,
			align : 'center',
			dataIndex : 'monthBase'
		}, {
			header : "全厂月奖系数",
			width : 100,
			sortable : true,
			align : 'center',
			dataIndex : 'coefficient'
		},{
			header : "月奖系数",
			width : 100,
			sortable : true,
			align : 'center',
			dataIndex : 'monthAward'
		}, {
			header : "缺勤天数",
			width : 100,
			sortable : true,
			align : 'center',
			dataIndex : 'absenceDays'
		},{
			header : "月奖扣除标准天数",
			width : 100,
			sortable : true,
			align : 'center',
			dataIndex : 'standardsDays'
		},{
			header : "月奖扣除比例",
			width : 100,
			sortable : true,
			align : 'center',
			dataIndex : 'percent',
			renderer:function (value, cellmeta, record, rowIndex, columnIndex,
					store)
			{
			 var 	days=record.get("standardsDays");
			var absenceDays=record.get("absenceDays");
			if(days==0)
			{
			return 0;
			}
			else if(absenceDays>=days)
			{
		    return 1;
			}
		    else if(days!=0&&absenceDays<days)
		    {
		    	
		    return  numberFormat ((absenceDays/days)*100)+'%';
		    }
				
			}
		},{
			header : "月奖金额",
			width : 100,
			sortable : true,
			dataIndex : 'monthMoney',
			renderer : function(value, cellmeta, record, rowIndex, columnIndex,
					store) {
				if (rowIndex < store.getCount() - 1) {
					var totalSum = 0;
					var monthMoney = record.data.monthMoney
					return monthMoney;
				} else {
					totalSum = 0;
					for (var i = 0; i < store.getCount() - 1; i++) {
						totalSum += store.getAt(i).get('monthMoney');
					}
					return "<font color='red'>" + numberFormat(totalSum)
							+ "</font>";
				}
			}
		},{
			header : "量化兑现",
			width : 100,
			sortable : true,
			dataIndex : 'lingHua',
			renderer : function(value, cellmeta, record, rowIndex, columnIndex,
					store) {
				if (rowIndex < store.getCount() - 1) {
					var totalSum = 0;
					var lingHua = record.data.lingHua
					return lingHua;
				} else {
					totalSum = 0;
					for (var i = 0; i < store.getCount() - 1; i++) {
						totalSum += store.getAt(i).get('lingHua');
					}
					return "<font color='red'>" + numberFormat(totalSum)
							+ "</font>";
				}
			}
		},{
			header : "工会主席技师增加值",
			width : 100,
			sortable : true,
			dataIndex : 'addValue',
			renderer : function(value, cellmeta, record, rowIndex, columnIndex,
					store) {
				if (rowIndex < store.getCount() - 1) {
					var totalSum = 0;
					var addValue = record.data.addValue
					return addValue;
				} else {
					totalSum = 0;
					for (var i = 0; i < store.getCount() - 1; i++) {
						totalSum += store.getAt(i).get('addValue');
					}
					return "<font color='red'>" + numberFormat(totalSum)
							+ "</font>";
				}
			}
		},{
			header : "金额",
			width : 100,
			sortable : true,
			dataIndex : 'totalMoney',
			renderer : function(value, cellmeta, record, rowIndex, columnIndex,
					store) {
				if (rowIndex < store.getCount() - 1) {
					var totalSum = 0;
					var totalMoney = record.data.totalMoney
					return totalMoney;
				} else {
					totalSum = 0;
					for (var i = 0; i < store.getCount() - 1; i++) {
						totalSum += store.getAt(i).get('totalMoney');
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
	
	//add by sychen 20100826 
	function tableToExcel(tableHTML) {
		window.clipboardData.setData("Text", tableHTML);
		try {
			var ExApp = new ActiveXObject("Excel.Application");
			var ExWBk = ExApp.workbooks.add();
			var ExWSh = ExWBk.worksheets(1);
			ExApp.Columns(6).ColumnWidth = 15;
			ExApp.Columns(9).ColumnWidth = 20;
			ExApp.Columns(10).ColumnWidth = 15;
			ExApp.Columns(13).ColumnWidth = 25;
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
		var year=new Date();
		Ext.Ajax.request({
			url : 'hr/findDetailMonthRewardByDept.action?rewardId=' + rewardId+ '&deptId='+ deptId,
			success : function(response) {
				var json = eval('(' + response.responseText.trim() + ')');
				var records = json.list;
				var html = ['<table border=1><tr><td align="center" colspan="14"><b><h1>月奖发放录入查看 '+deptNameCom.getValue()+' 明细信息</h1></b></td></tr>' +
						'<tr><th>部门</th><th>班组</th><th>人员编号</th><th>姓名</th><th>月奖基数</th><th>全厂月奖系数</th>' +
						'<th>月奖系数</th><th>缺勤天数</th><th>月奖扣除标准天数</th><th>月奖扣除比例</th><th>月奖金额</th>' +
						'<th>量化兑现</th><th>工会主席技师增加值</th><th>金额</th></tr>'];
				for (var i = 0; i < records.length; i += 1) {
					var rc = records[i];
					html.push('<tr><td align ="left">' + (rc[0] ==null?"":rc[0])+ 
					       '</td><td align ="left">' + (rc[1]==null?"":rc[1] ) +
					       '</td><td align ="left">' + (rc[2]==null?"":rc[2] ) +
					       '</td><td align ="left">' + (rc[3] ==null?"":rc[3]) +
					       '</td><td align ="left">' + (rc[4]==null?"":rc[4] )+
					       '</td><td align ="left">' + (rc[5] ==null?"":rc[5] ) +
					       '</td><td align ="left">' + (rc[6]==null?"":rc[6])+
					       '</td><td align ="left">' + (rc[11]==null?"":rc[11]) +
                           '</td><td align ="left">'+ (rc[10] ==null?"":rc[10])+
					       '</td><td align ="left">' + (rc[15]==null?"":rc[15])+
					       '</td><td align ="left">' + (rc[7] ==null?"":rc[7])+
					       '</td><td align ="left">' + (rc[8] ==null?"":rc[8])+
					       '</td><td align ="left">' + (rc[9] ==null?"":rc[9])+
						   '</td><td align ="left">' + (rc[13] ==null?"":rc[13])+
						   '</td></tr>');
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

	function addLine() {
		// 统计行
		var newRecord = new gridData({
		  	'deptName' : '合计',
			'banzu' : '',
			'newEmpCode' : '',
			'empName' : '',
			'monthBase' : '',
			'coefficient' : '',
			'monthAward':'',
			'monthMoney':'',
			'lingHua':'',
			'addValue':'',
			'totalMoney':'',
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