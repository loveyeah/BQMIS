Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	// 时间段Field
	function ChangeDateToString(DateIn) {
		var Year = 0;
		var Month = 0;
		var Day = 0;
		var CurrentDate = "";
		Year = DateIn.getYear();
		Month = DateIn.getMonth() + 1;
		Day = DateIn.getDate();
		CurrentDate = Year + "-";
		if (Month >= 10) {
			CurrentDate = CurrentDate + Month + "-";
		} else {
			CurrentDate = CurrentDate + "0" + Month + "-";
		}
		if (Day >= 10) {
			CurrentDate = CurrentDate + Day;
		} else {
			CurrentDate = CurrentDate + "0" + Day;
		}
		return CurrentDate;
	}
	var enddate = new Date();
	var startdate = enddate.add(Date.MONTH, -1);
	startdate = startdate.getFirstDateOfMonth();
	var sdate = ChangeDateToString(startdate);
	var edate = ChangeDateToString(enddate);
	var timefromDate = new Ext.form.TextField({
		id : 'timefromDate',
		name : '_timefromDate',
		fieldLabel : "开始",
		style : 'cursor:pointer',
		cls : 'Wdate',
		// width : 150,
		value : sdate,
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-01',
					dateFmt : 'yyyy-MM-dd',
					alwaysUseStartDate : true
				});
			}
		}
	});
	var timetoDate = new Ext.form.TextField({
		name : '_timetoDate',
		value : edate,
		id : 'timetoDate',
		fieldLabel : "结束",
		style : 'cursor:pointer',
		cls : 'Wdate',
		// width : 150,
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-01',
					dateFmt : 'yyyy-MM-dd',
					alwaysUseStartDate : true
				});
			}
		}
	});

	/**
	 * 月份Field
	 */
	var nowdate = new Date();
	var date1 = nowdate.toLocaleDateString();
	var exdate;
	if (date1.substring(6, 7) == '月') {
		exdate = date1.substring(0, 4) + '-0' + date1.substring(5, 6);
	} else {
		exdate = date1.substring(0, 4) + "-" + date1.substring(5, 7);
	}
	var enddate = exdate;
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
	/**
	 * 年份Field
	 */
	var d = new Date();
	year = d.getFullYear();
	var yearDate = new Ext.form.TextField({
		id : 'yearDate',
		name : '_yearDate',
		fieldLabel : "年份",
		style : 'cursor:pointer',
		cls : 'Wdate',
		// width : 150,
		value : year,
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y',
					dateFmt : 'yyyy',
					alwaysUseStartDate : true
				});
			}
		}
	});
	/**
	 * 季度Field
	 */

	var quarterDate = new Ext.form.TextField({
		id : 'quarterDate',
		name : '_quarterDate',
		fieldLabel : "年份",
		style : 'cursor:pointer',
		cls : 'Wdate',
		autowidth : true,
		value : year,
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y',
					dateFmt : 'yyyy',
					alwaysUseStartDate : true
				});
			}
		}
	});
	var quarterBox = new Ext.form.ComboBox({
		fieldLabel : '季度',
		store : [['1', '第一季度'], ['2', '第二季度'], ['3', '第三季度'], ['4', '第四季度']],
		id : 'quarterBox',
		name : 'quarterBoxName',
		valueField : "value",
		displayField : "text",
		mode : 'local',
		typeAhead : true,
		forceSelection : true,
		hiddenName : 'quarterBoxName',
		editable : false,
		triggerAction : 'all',
		width : 110,
		selectOnFocus : true,
		value : 1
	});
	var bugRadio = new Ext.form.Radio({
		id : 'bug',
		boxLabel : '故障',
		hideLabel : true,
		name : 'queryTypeRadio',
		checked : true
	});
	var runProfessionRadio = new Ext.form.Radio({
		id : 'runprofession',
		boxLabel : '运行专业',
		hideLabel : true,
		name : 'queryTypeRadio'
	})
	var repairDeptRadio = new Ext.form.Radio({
		id : 'repairdep',
		boxLabel : '检修部门',
		hideLabel : true,
		name : 'queryTypeRadio'
	})
	var statusRadio = new Ext.form.Radio({
		id : 'status',
		boxLabel : '状态',
		hideLabel : true,
		name : 'queryTypeRadio'
	})
	var dominationProfessionRadio = new Ext.form.Radio({
		id : 'domprofession',
		hideLabel : true,
		boxLabel : '管辖专业',
		name : 'queryTypeRadio'
	})
	var findDeptRadio = new Ext.form.Radio({
		id : 'finddep',
		hideLabel : true,
		boxLabel : '发现部门',
		name : 'queryTypeRadio'
	})
	// var pendingRadio = new Ext.form.Radio({
	// id : 'pending',
	// hideLabel : true,
	// boxLabel : '待处理',
	// name : 'queryTypeRadio'
	// })
	var typeRadio = new Ext.form.Radio({
		id : 'failuretype',
		hideLabel : true,
		boxLabel : '类型',
		name : 'queryTypeRadio'
	})

	var timeRadio = new Ext.form.Radio({
		id : 'time',
		name : 'queryWayRadio',
		boxLabel : '按时间段',
		hideLabel : true,
		checked : true
	});

	var quarterRadio = new Ext.form.Radio({
		id : 'quarter',
		name : 'queryWayRadio',
		hideLabel : true,
		boxLabel : ' 按季度'
	});

	var monthRadio = new Ext.form.Radio({
		id : 'month',
		name : 'queryWayRadio',
		hideLabel : true,
		boxLabel : '按月份'
	});

	var yearRadio = new Ext.form.Radio({
		id : 'year',
		name : 'queryWayRadio',
		hideLabel : true,
		boxLabel : '按年份',
		listeners : {
			check : function() {
				var queryType = getChooseQueryType();
				switch (queryType) {
					case 'time' : {
						timePanel.show();
						yearPanel.hide();
						quarterPanel.hide();
						monthPanel.hide();
						break;
					}
					case 'year' : {
						yearPanel.show();
						timePanel.hide();
						quarterPanel.hide();
						monthPanel.hide();
						break;
					}
					case 'quarter' : {
						quarterPanel.show();
						yearPanel.hide();
						timePanel.hide();
						monthPanel.hide();
						break;
					}
					case 'month' : {
						monthPanel.show();
						quarterPanel.hide();
						yearPanel.hide();
						timePanel.hide();
						break;
					}
				}
				// Ext.get("timeAreaPanel").dom.style.display = "none";
				// Ext.get("yearPanel").dom.style.display = "";
			}
		}
	});

	/**
	 * 时间段Panel
	 */
	var timePanel = new Ext.Panel({
		id : 'timeAreaPanel',
		border : false,
		layout : 'form',
		labelWidth : 32,
		items : [timefromDate, timetoDate]
	});
	/**
	 * 年份Panel
	 */
	var yearPanel = new Ext.Panel({
		id : 'yearPanel',
		hidden : true,
		border : false,
		layout : 'form',
		labelWidth : 32,
		items : [yearDate]
	});
	/**
	 * 季度Panel
	 */
	var quarterPanel = new Ext.Panel({
		id : 'quarterPanel',
		labelWidth : 32,
		layout : 'form',
		border : false,
		hidden : true,
		items : [quarterDate, quarterBox]
	});
	/**
	 * 月份Panel
	 */
	var monthPanel = new Ext.Panel({
		id : 'monthPanel',
		labelWidth : 32,
		border : false,
		hidden : true,
		layout : 'form',
		items : [monthDate]
	});
	function query() {
		
		var startDate;
		var endDate;
		var type = gettype();
		var queryType = getChooseQueryType();
		switch (queryType) {
			case 'time' : {
				startDate = Ext.get('timefromDate').dom.value;
				endDate = Ext.get('timetoDate').dom.value;
				break;
			}
			case 'year' : {
				var year = Ext.get('yearDate').dom.value;
				startDate = year + "-01-01";
				endDate = year + "-12-31";
				break;
			}
			case 'quarter' : {
				var year = Ext.get('quarterDate').dom.value;
				var quarter = quarterBox.getValue();
				if (quarter == '1') {
					startDate = year + "-01-01";
					endDate = year + "-03-31";
				} else if (quarter == '2') {
					startDate = year + "-04-01";
					endDate = year + "-06-30";
				} else if (quarter == '3') {
					startDate = year + "-07-01";
					endDate = year + "-09-30";
				} else {
					startDate = year + "-10-01";
					endDate = year + "-12-31";
				}
				break;
			}
			case 'month' : {
				var date = Ext.get('monthDate').dom.value + "-01";
				var monthdate = new Date(date.replace(/-/g, "/"));
				var sdate = monthdate.getFirstDateOfMonth();
				var edate = monthdate.getLastDateOfMonth();
				startDate = ChangeDateToString(sdate);
				endDate = ChangeDateToString(edate);
				break;
			}
		}
		if (startDate > endDate) {
			Ext.Msg.alert('提示', '选择结束日期应比开始日期大！');
			return false;
		}
		ds.load({
			params : {
				startDate : startDate,
				endDate : endDate,
				type : type
			}
		});
		if (pieRadio.checked) {
			displaygraphpie(startDate, endDate, type);
		} else if (columnRadio.checked) {
			displaygraphcolumn(startDate, endDate, type);
		} else {
			displaygraphline(startDate, endDate, type);
		}
	}
	var btnQuery = new Ext.Button({
		id : 'btnQuery',
		text : '查  询',
		iconCls : 'query',
		minWidth : 70,
		handler : query
	});

	var btnPrint = new Ext.Button({
		id : 'btnPrint',
		text : '打 印',
		iconCls : 'print',
		minWidth : 70,
		handler : function() {

		}
	});
	var queryTypeRadioPanel = new Ext.Panel({
		achor : '100%',
		border : false,
		layout : 'form',
		items : [{
			layout : 'column',
			border : false,
			items : [{
				border : false,
				columnWidth : 0.5,
				align : 'center',
				layout : 'form',
				items : [timeRadio]
			}, {
				border : false,
				columnWidth : 0.5,
				align : 'center',
				layout : 'form',
				items : [yearRadio]
			}]
		}, {
			layout : 'column',
			border : false,
			items : [{
				border : false,
				columnWidth : 0.5,
				align : 'center',
				layout : 'form',
				items : [quarterRadio]
			}, {
				border : false,
				columnWidth : 0.5,
				align : 'center',
				layout : 'form',
				items : [monthRadio]
			}]
		}]
	});
	var Query = new Ext.form.FieldSet({
		title : '查询方式',
		border : true,
		height : 75,
		// collapsible : true,
		anchor : '100%',
		layout : 'column',
		items : [{
			id : 'query',
			border : false,
			columnWidth : 0.55,
			anchor : '100%',
			items : [timePanel, yearPanel, quarterPanel, monthPanel]
		}, {
			border : false,
			columnWidth : 0.45,
			anchor : '100%',
			items : [queryTypeRadioPanel]
		}]
	});
	var QueryType = new Ext.form.FieldSet({
		title : '查询类型',
		border : true,
		height : 75,
		// collapsible : true,
		layout : 'form',
		items : [{
			layout : 'column',
			border : false,
			items : [{
				border : false,
				columnWidth : 0.25,
				layout : 'form',
				items : [bugRadio]
			}, {
				border : false,
				columnWidth : 0.25,
				layout : 'form',
				items : [runProfessionRadio]
			}, {
				border : false,
				columnWidth : 0.25,
				align : 'center',
				layout : 'form',
				items : [repairDeptRadio]
			}, {
				border : false,
				columnWidth : 0.25,
				align : 'center',
				layout : 'form',
				items : [statusRadio]

			}]
		}, {
			layout : 'column',
			border : false,
			items : [{
				border : false,
				columnWidth : 0.25,
				layout : 'form',
				items : [typeRadio]
			}, {
				border : false,
				columnWidth : 0.25,
				layout : 'form',
				items : [findDeptRadio]
			}, {
				border : false,
				columnWidth : 0.25,
				layout : 'form',
				items : [dominationProfessionRadio]
			}]
		}]
	});
	var Querybutton = new Ext.form.FieldSet({
		border : false,
		frame : false,
		height : 80,
		collapsible : true,
		layout : 'form',
		items : [{
			border : false,
			height : 20,
			layout : 'form'
		}, btnPrint, {
			border : false,
			height : 8,
			layout : 'form'
		}, btnQuery]
	});
	var form = new Ext.form.FormPanel({
		bodyStyle : "padding:5px 5px 0",
		labelAlign : 'right',
		id : 'shift-form',
		// labelWidth : 5,
		autoHeight : true,
		region : 'center',
		border : false,
		layout : 'form',
		items : [{
			layout : 'column',
			border : false,
			items : [{
				border : false,
				columnWidth : 0.45,
				align : 'center',
				layout : 'form',
				anchor : '100%',
				items : [Query]
			}, {
				border : false,
				columnWidth : 0.45,
				align : 'center',
				layout : 'form',
				anchor : '100%',
				items : [QueryType]
			}, {
				border : false,
				columnWidth : 0.1,
				align : 'center',
				anchor : '100%',
				layout : 'form',
				items : [Querybutton]
			}]
		}]
	});

	/*-----------------------------------------------------------------------------------------------------------------*/
	var item = Ext.data.Record.create([{
		name : 'queryType'
	}, {
		name : 'queryTypeName'
	}, {
		name : 'count'
	}
	// add by liuyi 20100428 已消除缺陷数 未消除缺陷数 缺陷重复发生数  退回缺陷数
	, {
		name : 'eliminateCount'
	}, {
		name : 'noeliminateCount'
	}, {
		name : 'repeatCount'
	}, {
		name : 'backCount'
	}
	// add by liuyi 20100428 增加消缺率
	,{
		name : 'rate'
	}
	]);
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true
	});
	var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
		header : '行',
		width : 30,
		align : 'center'
	}), {
		header : '类别',
		dataIndex : 'queryTypeName',
		width : 120,
		align : 'center'
	}, {
		header : '数量',
		dataIndex : 'count',
		width : 50,
		align : 'center'
	}
	// add by liuyi 20100428 
	, {
			header : '已消除缺陷数',
			dataIndex : 'eliminateCount',
			align : 'center'
		}, {
			header : '未消除缺陷数',
			dataIndex : 'noeliminateCount',
			align : 'center'
		}, {
			header : '退回缺陷数',
			dataIndex : 'backCount',
			align : 'center'
		}
		, {
			header : '消缺率',
			dataIndex : 'rate',
			align : 'center'
		}
	]);
	cm.defaultSortable = true;
	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'bqfailure/failureIntegerQuery.action'
		}),
		reader : new Ext.data.JsonReader({
				// totalProperty : 'total',
				// root : 'root'
				}, item)
	});
	ds.load({
		params : {
			startDate : sdate,
			endDate : edate,
			type : 'bug'
		}
	});
	// add by liuyi 20100428 
	ds.on('load',function(){	
			var countTotal = 0;
			var eliminateCountTotal = 0;
			var noeliminateCountTotal = 0;
			var repeatCountTotal = 0;
			var backCountTotal = 0;
			var rateTotal = '0.00%';
			ds.each(function(re) {
				countTotal += (((re.get('count') == null || re
						.get('count') == '') ? 0 : re
						.get('count')) - 0);
				eliminateCountTotal += (((re.get('eliminateCount') == null || re
						.get('eliminateCount') == '') ? 0 : re
						.get('eliminateCount')) - 0);
				noeliminateCountTotal += (((re.get('noeliminateCount') == null || re
						.get('noeliminateCount') == '') ? 0 : re
						.get('noeliminateCount')) - 0);
				repeatCountTotal += (((re.get('repeatCount') == null || re
						.get('repeatCount') == '') ? 0 : re
						.get('repeatCount')) - 0);
				backCountTotal += (((re.get('backCount') == null || re
						.get('backCount') == '') ? 0 : re
						.get('backCount')) - 0);
				
				var rateIterator = '0.00%'
				var den = (re.get('eliminateCount') - 0) + (re.get('noeliminateCount') - 0 ) + (re
					.get('backCount') - 0);
				if(den != 0){
					rateIterator = (( re.get('eliminateCount') - 0) / den *100).toFixed(2) + '%';
				}
				re.set('rate',rateIterator)
			})
			var denominator = (eliminateCountTotal - 0) + (noeliminateCountTotal - 0) + (backCountTotal - 0);
			if ( denominator != 0)
			{
				rateTotal = ((eliminateCountTotal - 0) / denominator *100).toFixed(2) + '%';
			}
			var data = new item({
				'queryType' : null,
				'queryTypeName' : '总计',
				'count' : countTotal,
				'eliminateCount' : eliminateCountTotal,
				'noeliminateCount' : noeliminateCountTotal,
				'repeatCount' : repeatCountTotal,
				'backCount' : backCountTotal,
				'rate' : rateTotal
			})
			ds.add(data)	
	})
	var Grid = new Ext.grid.GridPanel({
		ds : ds,
		cm : cm,
		// sm : sm,
		split : true,
		autoScroll : true,
		// bbar : gridbbar,
		border : false,
		viewConfig : {
			forceFit : false
		}
	});
	//add by wpzhu 20100809------------------
	 Grid.on("rowcontextmenu", function(g, i, e) { 
				e.stopEvent();
				var record = Grid.getStore().getAt(i);
				// 右键菜单
				var menu = new Ext.menu.Menu({
					id : 'mainMenu',
					items : [new Ext.menu.Item({
						text : '查看详细信息',
						iconCls : 'view',
						handler : function() { 
							var type = gettype();
				
							if(record.get("queryType")==""||record.get("queryType")==null)
							{
								Ext.Msg.alert("提示","请选择!");
								return;
							}
							else
							{ 
							  var url = "../../../../equ/bqfailure/query/failureIntegratedQuery/searchFailDetail.jsp";
										
							  var obj=new Object();
							  obj.queryType=record.get("queryType");
							  obj.strDate=sdate;
							  obj.endDate=edate;
							  obj.type=type;
					
								window.showModalDialog(
												url,
												obj,
												"dialogWidth:650px;dialogHeight:550px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;");
							}
						}
					})]
				});
				var coords = e.getXY();
				menu.showAt([coords[0], coords[1]]);
			});
	Grid.on('rowdblclick', function(grid, rowIndex, e) {
		var rec = Grid.getSelectionModel().getSelected();
		if (rec) {
			
							  var url = "../../../../equ/bqfailure/query/failureIntegratedQuery/searchFailDetail.jsp";
								var type = gettype();		
							  var obj=new Object();
							   var obj=new Object();
							  obj.queryType=rec.get("queryType");
							  obj.strDate=sdate;
							  obj.endDate=edate;
							  obj.type=type;
								window.showModalDialog(
												url,
												obj,
												"dialogWidth:650px;dialogHeight:550px;;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;");
							
		}
	});
	//-------------------------------------------------------------------------------------------------------------
	var panelleft = new Ext.Panel({
		region : 'west',
		title : '缺陷综合统计',
		layout : 'fit',
		width : 220,
		autoScroll : true,
		containerScroll : true,
		border : true,
		collapsible : true,
		split : true,
		items : [Grid]
	});

	var pieRadio = new Ext.form.Radio({
		id : 'pieRadio',
		hideLabel : true,
		name : 'graTypeRadio',
		checked : true
	});
	var columnRadio = new Ext.form.Radio({
		id : 'columnRadio',
		hideLabel : true,
		name : 'graTypeRadio',
		checked : false
	});
	var lineRadio = new Ext.form.Radio({
		id : 'lineRadio',
		hideLabel : true,
		name : 'graTypeRadio',
		checked : false,
		listeners : {
			check : function() {
				query();
			}
		}
	});
	var tbar = new Ext.Toolbar({
		items : ['图形分析：', pieRadio, '饼状图', '-', columnRadio, '柱状图', '-',
				lineRadio, '曲线图']
	})
	var right = new Ext.Panel({
		region : "center",
		layout : 'fit',
		border : true,
		tbar : tbar,
		html : '<div id="flashPie" style="overflow:auto;width:100%;height=100%"></div>'
	});
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		items : [{
			region : "north",
			layout : 'fit',
			height : 90,
			border : false,
			split : true,
			items : [form]
		}, {
			region : "center",
			// enableTabScroll : true,
			layout : "border",
			collapsible : true,
			border : false,
			// split : true,
			// margins : '0 0 0 0',
			// 注入表格
			items : [panelleft, right]
		}],
		listeners : {
			"afterlayout" : function() {
				displaygraphpie(sdate, edate, "bug");
			}
		}
	});
})
 
// 显示饼状图
function displaygraphpie(startDate, endDate, type) {
	var so = new SWFObject("/power/equ/bqfailure/query/failureIntegratedQuery/amchart/ampie/ampie.swf", "amline", "100%",
			"100%", "8", "#FFFFFF");
	so.addVariable("path", "comm/amchart/ampie/");
	so.addVariable("settings_file",
			escape("/power/equ/bqfailure/query/failureIntegratedQuery/amchart/ampie/ampie_settings.xml"));
	so.addVariable("data_file",
			escape("bqfailure/getIntegerPie.action?startDate=" + startDate
					+ " &endDate=" + endDate + " &type=" + type + ""));
	so.addVariable("preloader_color", "#999999");
	so.write("flashPie");
}
// 显示曲线图
function displaygraphline(startDate, endDate, type) {
	var so = new SWFObject("/power/equ/bqfailure/query/failureIntegratedQuery/amchart/amline/amline.swf", "amline", "100%",
			"100%", "8", "#FFFFFF");
	so.addVariable("path", "comm/amchart/amline/");
	so.addVariable("settings_file",
			escape("/power/equ/bqfailure/query/failureIntegratedQuery/amchart/amline/amline_settings.xml"));
	so.addVariable("data_file",
			escape("bqfailure/getIntegerLine.action?startDate=" + startDate
					+ " &endDate=" + endDate + " &type=" + type + ""));
	so.addVariable("preloader_color", "#999999");
	so.write("flashPie");
}
// 显示柱状图
function displaygraphcolumn(startDate, endDate, type) {
	var so = new SWFObject("/power/equ/bqfailure/query/failureIntegratedQuery/amchart/amcolumn/amcolumn.swf", "amline",
			"100%", "100%", "8", "#FFFFFF");
	so.addVariable("path", "comm/amchart/amcolumn/");
	so.addVariable("settings_file",
			escape("/power/equ/bqfailure/query/failureIntegratedQuery/amchart/amcolumn/amcolumn_settings.xml"));
	so.addVariable("data_file",
			escape("bqfailure/getIntegerColumn.action?startDate=" + startDate
					+ " &endDate=" + endDate + " &type=" + type + ""));
	so.addVariable("preloader_color", "#999999");
	so.write("flashPie");
}
/**
 * 遍历取得Radio值
 * 
 */
function getChooseQueryType() {
	var list = document.getElementsByName("queryWayRadio");
	for (var i = 0; i < list.length; i++) {
		if (list[i].checked) {
			return list[i].id;
		}
	}
}
function gettype() {
	var list = document.getElementsByName("queryTypeRadio");
	for (var i = 0; i < list.length; i++) {
		if (list[i].checked) {
			return list[i].id;
		}
	}
}