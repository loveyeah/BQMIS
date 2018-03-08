Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	//Ext.QuickTips.init();
	var nowdate = new Date();
	var exdate = nowdate.format("Y-m")
	var date;
	var fristTotal=0;
	var secondTotal=0;
	var threeTotal=0;
	var fourTotal=0;
	var fiveTotal=0;
	var rec = Ext.data.Record.create([{
				name : 'dayOfMonth',
				mapping:0
			}, {
				name : 'valueOfFirst',
				mapping:1
			}, {
				name : 'valueOfSecond',
				mapping:2
			}, {
				name : 'valueOfThree',
				mapping:3
			}, {
				name : 'valueOfFour',
				mapping:4
			}, {
				name : 'valueOfFive',
				mapping:5
			}]);

	var store = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'manager/finReportListByMon.action'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : 'totalCount',
							root : 'list'
						}, rec)
			});
	//月度选择
	var monthDate = new Ext.form.TextField({
				name : 'monthDate',
				value : exdate,
				id : 'monthDate',
				fieldLabel : "月份",
				style : 'cursor:pointer',
				cls : 'Wdate',
				width : 90,
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%m',
									dateFmt : 'yyyy-MM',
									alwaysUseStartDate : true,
									isShowClear : false
								});
					}
				}
			});
	// -----------------------------------
	//查询按钮
	var btnPrint = new Ext.Button({
				text : "查询",
				iconCls : 'query',
				disabled : false,
				handler : printSupply
			});
	function addLine() {
		// 统计行
		var newRecord = new rec({
			'dayOfMonth' : '合计',
			'valueOfFirst' : '',
			'valueOfSecond' : '',
			'valueOfThree' : '',
			'valueOfFour' : '',
			'valueOfFive' : ''
		});
		// 原数据个数
		var count = store.getCount();
		// 停止原来编辑
		gridPanel.stopEditing();
		// 插入统计行
		store.insert(count, newRecord);
//		gridPanel.getView().refresh();
	};
	// head工具栏
	var headTbar = new Ext.Toolbar({
		region : 'north',
		items : [
//		'报表名:', reportNameBox, 
		'月份：', monthDate, btnPrint,
				new Ext.Button({
					text : '导出',
					iconCls : 'print',
					handler : 
					function(){
					var url = "../../../../manager/reportExportByMon.action?date="+date;
					window.open(url);
				}
				}
				)]
	});
	var gridPanel = new Ext.grid.GridPanel({
				ds : store,
				title:'aa',
				columns : [{
							header : "序号",
							width : 50,
							//align : "center",
							sortable : false,
							dataIndex : 'dayOfMonth'
//							,renderer:function(v){
//								
//								return v;
//							} 
						},{
							header : "运行一班",
							width : 75,
							sortable : false,
							dataIndex : 'valueOfFirst'
							,renderer:function(value, cellmeta, record, rowIndex, columnIndex,
							store){
								var hj = record.data.dayOfMonth;
								if(hj!="合计")
								{ 
									if(value!=null&&value!=""){
									fristTotal+=parseInt(value);
									}
									return value;
								}else{
									return fristTotal;
								}
							} 
		                  },{
							header : "运行二班",
							width : 100,
							//align : "center",
							sortable : false,
							dataIndex : 'valueOfSecond'
							,renderer:function(value, cellmeta, record, rowIndex, columnIndex,
							store){
								var hj = record.data.dayOfMonth;
								if(hj!="合计")
								{ 
									if(value!=null&&value!=""){
									secondTotal+=parseInt(value);
									}
									return value;
								}else{
									return secondTotal;
								}
							} 
						},{
							header : "运行三班",
							width : 100,
							//align : "center",
							sortable : false,
							dataIndex : 'valueOfThree'
							,renderer:function(value, cellmeta, record, rowIndex, columnIndex,
							store){
								var hj = record.data.dayOfMonth;
								if(hj!="合计")
								{ 
									if(value!=null&&value!=""){
									threeTotal+=parseInt(value);
									}
									return value;
								}else{
									return threeTotal;
								}
							} 
						},{
							header : "运行四班",
							width : 100,
							//align : "center",
							sortable : false,
							dataIndex : 'valueOfFour'
							,renderer:function(value, cellmeta, record, rowIndex, columnIndex,
							store){
								var hj = record.data.dayOfMonth;
								if(hj!="合计")
								{ 
									if(value!=null&&value!=""){
									fourTotal+=parseInt(value);
									}
									return value;
								}else{
									return fourTotal;
								}
							} 
						},{
							header : "运行五班",
							width : 100,
							//align : "center",
							sortable : false,
							dataIndex : 'valueOfFive'
							,renderer:function(value, cellmeta, record, rowIndex, columnIndex,
							store){
								var hj = record.data.dayOfMonth;
								if(hj!="合计")
								{ 
									if(value!=null&&value!=""){
									fiveTotal+=parseInt(value);
									}
									return value;
								}else{
									return fiveTotal;
								}
							} 
						}],
				viewConfig : {
			                 forceFit : true
		           },
				//tbar : headTbar,
//				sm : westsm,
//				frame : true,
//				bbar : new Ext.PagingToolbar({
//							pageSize : 18,
//							store : westgrids,
//							displayInfo : true,
//							displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
//							emptyMsg : "没有记录"
//						}),
				border : true,
				enableColumnHide : false,
				enableColumnMove : false,
				iconCls : 'icon-grid'
			});
	function printSupply() {
		if (monthDate.getValue() == null || monthDate.getValue() == "") {
			Ext.Msg.alert('提示信息', '请先选择月份！');
			return;
		}
		date=monthDate.getValue();
		fristTotal=0;
		secondTotal=0;
	  	threeTotal=0;
		fourTotal=0;
		fiveTotal=0;
		isdata=true;
		store.load({
			params : {
						date : date
					}
		});
		gridPanel.setTitle('<center>燃料运行班'+date.substring(5)+'月上煤量汇总</center>');
	}
	printSupply();
	store.on("load", addLine);
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		items : [
			{
					region : "north",
					layout : 'fit',
					height : 30,
					border : false,
					split : true,
					margins : '0 0 0 0',
					items : [headTbar]
					
				}, {
					region : "center",
					layout : 'fit',
					border : true,
					collapsible : true,
					split : true,
					margins : '0 0 0 0',
					items : [gridPanel]
					
				}]
	});

})