Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
function numberFormat(value) {
		value = String(value);
		if (value == null || value == "null") {
			value = "0";
		}
		// 整数部分
		var whole = value;
		// 小数部分
		var sub = ".00";
		// 如果有小数
		if (value.indexOf(".") > 0) {
			whole = value.substring(0, value.indexOf("."));
			sub = value.substring(value.indexOf("."), value.length);
			sub = sub + "00";
			if (sub.length > 3) {
				sub = sub.substring(0, 3);
			}
		}
		var r = /(\d+)(\d{3})/;
		while (r.test(whole)) {
			whole = whole.replace(r, '$1' + ',' + '$2');
		}
		v = whole + sub;
		if (whole == null || whole == "null" || whole == "") {
			v = "0.00";
		}
		return v;
	}
	
	function getDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate();
		s += (t > 9 ? "" : "0") + t + " ";
		t = d.getHours();
		s += (t > 9 ? "" : "0") + t + ":";
		t = d.getMinutes();
		s += (t > 9 ? "" : "0") + t + ":";
		t = d.getSeconds();
		s += (t > 9 ? "" : "0") + t;
		return s;
	}
	function getCurrentYear()
	{
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString();		
		return s;
	}
	// 系统当前月
	function getMonth() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString() + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t ;
		return s;
	}
	
	var formatType;
	var yearRadio = new Ext.form.Radio({
			id : 'year',
			name : 'queryWayRadio',
			hideLabel : true,
			boxLabel : '年份'	
		});
	var monthRadio = new Ext.form.Radio({
			id : 'month',
			name : 'queryWayRadio',
			hideLabel : true,
			boxLabel : '月份',
			checked : true,
			listeners : {
				check : function() {
					var queryType = getChooseQueryType();
					switch (queryType) {
						case 'year' : {
							formatType = 1;
							time.setValue(getCurrentYear());
							break;
						}
						case 'month' : {
							time.setValue(getMonth());
							formatType = 2;
							break;
						}
					}
				}
			}
		});	
	var time = new Ext.form.TextField({
		id : 'time',
		allowBlank : true,
		// value : getDate(),
		width : 100,
		readOnly : true,
		listeners : {
			focus : function() {
				var format = '';
				if(formatType == 1)
					format = 'yyyy';
				if(formatType == 2)
					format = 'yyyy-MM';
				WdatePicker({
					startDate : '%y-%M-%d',
					dateFmt : format,
					alwaysUseStartDate : true,
					onclearing : function() {
										planStartDate.markInvalid();
									}
									
				});
			}
		}
	});
	//遍历所有的REDIO获得ID
	function getChooseQueryType() {
		var list = document.getElementsByName("queryWayRadio");
		for (var i = 0; i < list.length; i++) {
			if (list[i].checked) {
				return list[i].id;
			}
		}
	}
	
	var item = Ext.data.Record.create([{
		name : 'capitalId'
	},{
		name : 'budgetTime'
	}, {
		name : 'workFlowNo'
	}, {
		name : 'workFlowStatus'
	}, {
		name : 'capitalDetailId'
	},{
		name : 'project'
	},{
		name : 'materialCost'
	},{
		name : 'workingCost'
	},{
		name : 'otherCost'
	},{
		name : 'deviceCost'
	},{
		name : 'totalCost'
	},{
		name : 'memo'
	},{
		name : 'isUse'
	}
	]);

	
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false

	});
	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'managebudget/capitalApproveQuery.action'
		}),
		reader : new Ext.data.JsonReader({
			totalCount : "totalCount",
			root : "list"
		}, item)
	});

//	ds.on('load',function(){
//		var obj;
//		var currentIndex = ds.getCount();
//			var materialCostCount = 0;
//			var workingCostCount = 0;
//			var otherCostCount = 0;
//			var deviceCostCount = 0;
//			var totalCostCount = 0;
//			for(var i = 0; i <= ds.getTotalCount() - 1; i++)
//			{
//				materialCostCount += ds.getAt(i).get('materialCost');
//				workingCostCount += ds.getAt(i).get('workingCost');
//				otherCostCount += ds.getAt(i).get('otherCost');
//				deviceCostCount += ds.getAt(i).get('deviceCost');
//				totalCostCount += ds.getAt(i).get('totalCost')
//			}
//			obj = new item({
////				  用以标记合计行
//						'capitalId' : null,
//						'project' : '合计',
//						'materialCost' : materialCostCount,
//						'workingCost' : workingCostCount,
//						'otherCost' : otherCostCount,
//						'deviceCost' : deviceCostCount,
//						'totalCost' : totalCostCount,
//						'memo' : null
//					});			
////		}
//		Grid.stopEditing();
//		ds.add(obj);
//	});
	// 事件状态
	var cm = new Ext.grid.ColumnModel([sm,
		new Ext.grid.RowNumberer({
			header : "行号",
			width : 31
			}),
	{
		header : '项目名称',
		dataIndex : 'project',
		width : 110,
		editor : new Ext.form.TextField({})
	}, {
		header : '材料费用',
		dataIndex : 'materialCost',
		align : 'right',
		width : 90,
		renderer : function(value) {
			return numberFormat(value);
		}
	}, {
		header : '人工费用',
		dataIndex : 'workingCost',
		align : 'right',
		width : 90,
		renderer : function(value) {
			return numberFormat(value);
		}
	}, {
		header : '其他费用',
		dataIndex : 'otherCost',
		align : 'right',
		width : 90,
		renderer : function(value) {
			return numberFormat(value);
		}
	}, {
		header : "设备费用",
		width : 90,
		dataIndex : 'deviceCost',
		align : 'right',
		renderer : function(value) {
			return numberFormat(value);
		}
	}, {
		header : '合计费用',
		dataIndex : 'totalCost',
		align : 'right',
		width : 90,
		renderer : function(value) {
			return numberFormat(value);
		}
	},{
			header : '备注',
			width : 130,
			align : 'left',
			sortable : true,
			dataIndex : 'memo'
		}
	]);
		
	var bbar = new Ext.PagingToolbar({
		pageSize : 18,
		store : ds,
		displayInfo : true,
		displayMsg : "显示第{0}条到{1}条，共{2}条",
		emptyMsg : "没有记录"
	});
    
	function queryRecord() {		
	if(time.getValue() == null || time.getValue() == "")
		{
			Ext.Msg.alert('提示信息','时间不可为空，请先选择！');
			return;
		}
		ds.baseParams = {
			budgetTime : time.getValue()
		};
		ds.load({
			params : {
				start : 0,
				limit : 18
			}
		});
	}
	
	
	// 查询
	var query = new Ext.Button({
		id : "btnQuery",
		text : "查询",
		iconCls : "query",
		handler :queryRecord
	})
	
	// 定义grid

	// 顶部工具栏
	var tbar = new Ext.Toolbar({
		items : [yearRadio,monthRadio,time,'-',query,
		{
			text:'审批',
			iconCls : 'view',
			handler:report
		}
		]
	});
	// 可编辑的表格
	var Grid = new Ext.grid.GridPanel({
		sm : sm,
		ds : ds,
		cm : cm,
		autoScroll : true,
		bbar : bbar,
		tbar : tbar,
		border : true,
		viewConfig : {
		 forceFit : true
		}
	});
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		border : false,
		frame : false,
		items : [{
			layout : 'fit',
			border : false,
			frame : false,
			region : "center",
			items : [Grid]
		}]
	});
	

//	Grid.on("rowdblclick", report);
	function report()
	{
		//----------------
		if(ds.getTotalCount()==0)
		{
			Ext.Msg.alert("提示","没有记录审批！");
			return;
		}
		var record = ds.getAt(0);
		var workFlowNo = record.get('workFlowNo');
		
		Ext.Ajax.request({
			url : 'MAINTWorkflow.do?action=getCurrentStepsInfo',
			method : 'POST',
			params : {
				entryId : workFlowNo
			},
			success : function(result, request) {
				var url="";
				var obj = eval("(" + result.responseText + ")");
				var args = new Object();
				if (obj[0].url==null||obj[0].url=="") {
					url="approveSign.jsp";
				} 
				else
				{
					 url = "../../../../../" + obj[0].url; 
				}
				
					args.busiNo = record.get('capitalId');
					args.entryId = record.get("workFlowNo"); 
					var obj = window.showModalDialog(url, args,
					'status:no;dialogWidth=750px;dialogHeight=550px');
					if(obj)
					{
					 queryRecord();
					}
				
			},
			failure : function(result, request) {
				Ext.Msg.alert("提示","错误");
			}

		});
		//-----------------
		
		
	}
	
	queryRecord();
	
})
