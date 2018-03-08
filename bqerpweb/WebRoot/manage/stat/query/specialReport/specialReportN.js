Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	Ext.QuickTips.init();
	var nowdate = new Date();
	var date1 = nowdate.toLocaleDateString();
	var exdate = date1.substring(0, 4)
	var reportCode;

	var rec = Ext.data.Record.create([{
				name : 'reportCode'
			}, {
				name : 'reportName'
			}, {
				name : 'reportType'
			}]);

	var ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'manager/getBpCReportNewList.action'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : 'totalCount',
							root : 'list'
						}, rec)
			});

	var reportNameBox = new Ext.form.ComboBox({
				fieldLabel : '报表名称',
				store : ds,
				id : 'template',
				name : 'template',
				valueField : "reportCode",
				displayField : "reportName",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				editable : false,
				width : 250,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : true,
				emptyText : '请选择',
				resizable : true,
				listeners : {
					select : function(index, record) {
						reportCode = record.data.reportCode;
						reportNameBox.setValue(reportCode);
					}
				}
			});
	ds.on('load', function(e, records) {
				reportCode = records[0].data.reportCode;
				reportNameBox.setValue(reportCode)
			});

	ds.load({
				params : {
					reportType : '3'
				}
			});

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
									startDate : '%y',
									dateFmt : 'yyyy',
									alwaysUseStartDate : true,
									isShowClear : false
								});
					}
				}
			});
	// -----------------------------------

	var btnPrint = new Ext.Button({
				text : "查询",
				iconCls : 'query',
				disabled : false,
				handler : printSupply
			});

	// head工具栏
	var headTbar = new Ext.Toolbar({
		region : 'north',
		items : ['报表名:', reportNameBox, '月份：', monthDate, btnPrint,
				new Ext.Button({
					text : '导出',
					iconCls : 'print',
					handler : function() {
						var strMonth = monthDate.getValue().substring(0, 4);
						var url;
						if(reportCode == 1 ){
							url = "/powerrpt/frameset?__report=bqmis/specialReportNj1.rptdesign";
							url += "&__action=print&month=" + strMonth
									+ "&reportCode=" + reportCode
									+ "&__format=xls";
						}
						if(reportCode == 2){
							url = "/powerrpt/frameset?__report=bqmis/specialReportNj2.rptdesign";
							url += "&__action=print&month=" + strMonth
									+ "&reportCode=" + reportCode
									+ "&__format=xls";
						}
						if ( reportCode == 2
								|| reportCode == 4) {
							url = "/powerrpt/frameset?__report=bqmis/specialReportNj.rptdesign";
							url += "&__action=print&month=" + strMonth
									+ "&reportCode=" + reportCode
									+ "&__format=xls";
						}
						if(reportCode == 3 ){
							url = "/powerrpt/frameset?__report=bqmis/specialReportNj3.rptdesign";
							url += "&__action=print&month=" + strMonth
									+ "&reportCode=" + reportCode
									+ "&__format=xls";
						}
						if (reportCode == 5) {
							url = "/powerrpt/frameset?__report=bqmis/specialReportNj5.rptdesign";
							url += "&__action=print&month=" + strMonth
									+ "&reportCode=" + reportCode
									+ "&__format=xls";
									
						}
						if(reportCode == 28){
							url = "/powerrpt/frameset?__report=bqmis/specialReportN28.rptdesign";
							url += "&__action=print&month=" + strMonth
									+ "&reportCode=" + reportCode
									+ "&__format=xls";
						}
						if(reportCode == 29){
							url = "/powerrpt/frameset?__report=bqmis/specialReportN29.rptdesign";
							url += "&__action=print&month=" + strMonth
									+ "&reportCode=" + reportCode
									+ "&__format=xls";
						}
						if (reportCode == 32) {
							url = "/powerrpt/frameset?__report=bqmis/specialReportN.rptdesign";
							url += "&__action=print&month=" + strMonth
									+ "&reportCode=" + reportCode
									+ "&__format=xls";
						}
						if(reportCode == 30 ){
							url = "/powerrpt/frameset?__report=bqmis/specialReportN30.rptdesign";
							url += "&__action=print&month=" + strMonth
									+ "&reportCode=" + reportCode
									+ "&__format=xls";
						}
						if(reportCode == 31){
							url = "/powerrpt/frameset?__report=bqmis/specialReportN31.rptdesign";
							url += "&__action=print&month=" + strMonth
									+ "&reportCode=" + reportCode
									+ "&__format=xls";
						}
						window.open(url);
					}
				})]
	});

	function printSupply() {
		if (monthDate.getValue() == null || monthDate.getValue() == "") {
			Ext.Msg.alert('提示信息', '请先选择月份！');
			return;
		}
		var strMonth = monthDate.getValue().substring(0, 4);
		var url;
		if(reportCode == 1){
			url = "/powerrpt/report/webfile/bqmis/specialReportNj1.jsp?month="
					+ strMonth + "&reportCode=" + reportCode;
		}
		if( reportCode == 2){
			url = "/powerrpt/report/webfile/bqmis/specialReportNj2.jsp?month="
					+ strMonth + "&reportCode=" + reportCode;
		}
		if (reportCode == 4) {
			url = "/powerrpt/report/webfile/bqmis/specialReportNj.jsp?month="
					+ strMonth + "&reportCode=" + reportCode;
		}
		if(reportCode == 3){
			url = "/powerrpt/report/webfile/bqmis/specialReportNj3.jsp?month="
					+ strMonth + "&reportCode=" + reportCode;
		}
		if(reportCode == 5){
			url = "/powerrpt/report/webfile/bqmis/specialReportNj5.jsp?month="
					+ strMonth + "&reportCode=" + reportCode;
		}
		if(reportCode == 28)
		{
			url = "/powerrpt/report/webfile/bqmis/specialReportN28.jsp?month="
					+ strMonth + "&reportCode=" + reportCode;
		}
		if(reportCode == 29){
			url = "/powerrpt/report/webfile/bqmis/specialReportN29.jsp?month="
					+ strMonth + "&reportCode=" + reportCode;
		}
		if (reportCode == 32) {
			url = "/powerrpt/report/webfile/bqmis/specialReportN.jsp?month="
					+ strMonth + "&reportCode=" + reportCode;
		}
		if(reportCode == 30){
			url = "/powerrpt/report/webfile/bqmis/specialReportN30.jsp?month="
					+ strMonth + "&reportCode=" + reportCode;
		}
		if(reportCode == 31){
			url = "/powerrpt/report/webfile/bqmis/specialReportN31.jsp?month="
					+ strMonth + "&reportCode=" + reportCode;
		}
//		alert(url)
		document.all.iframe1.src = url;
	}

	var strMonth = monthDate.getValue().substring(0, 4);
	var url = "/powerrpt/report/webfile/bqmis/specialReportNj1.jsp?month="
			+ strMonth + "&reportCode=" + 1;
	// var url = "about:blank"
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		items : [{
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
					html : '<iframe id="iframe1" name="iframe1"  src="'
							+ url
							+ '"  frameborder="0"  width="100%" height="100%"  />'
				}]
	});

})