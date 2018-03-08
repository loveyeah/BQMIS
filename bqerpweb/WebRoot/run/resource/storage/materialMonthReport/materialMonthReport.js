Ext.onReady(function() {

	var panel1 = new Ext.FormPanel({
		id : 'tab1',
		title : '仓库月份收发记录',
		items : [{html : '<iframe src="run/resource/storage/materialMonthReport/warehouseReport.jsp"  width="100%" height="800" />'}]
		//items : [{html : '<iframe src="/powerrpt/report/webfile/receiveIssues.jsp"  width="100%" height="800" />'}]
	});
	var panel2 = new Ext.FormPanel({
		id : 'tab2',
		title : '物料月份收发记录',
		items : [{html : '<iframe src="run/resource/storage/materialMonthReport/materialReport.jsp"  width="100%" height="800" />'}]
	});
	var tabpanel = new Ext.TabPanel({
		title : 'mytab',
		activeTab : 0,
	//	autoScroll : true,
		items : [panel1, panel2]

	});
	
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		items : [tabpanel]
	});

});