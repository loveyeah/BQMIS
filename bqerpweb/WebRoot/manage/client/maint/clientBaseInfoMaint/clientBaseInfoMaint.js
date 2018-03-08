Ext.onReady(function() {

	var panel1 = new Ext.FormPanel({
		id : 'tab1',
		title : '合作伙伴类型维护',
		items : [{html : '<iframe src="manage/client/maint/clientsType/clientsType.jsp"  width="100%" height="800" />'}]
	});
	var panel2 = new Ext.FormPanel({
		id : 'tab2',
		title : '公司性质维护',
		items : [{html : '<iframe src="manage/client/maint/clientsCharacter/clientsCharacter.jsp"  width="100%" height="800" />'}]
	});
	var panel3 = new Ext.FormPanel({
		id : 'tab3',
		title : '重要程度维护',
		items : [{html : '<iframe src="manage/client/maint/clientImportance/clientImportance.jsp"  width="100%" height="800" />'}]
	});
	var panel4 = new Ext.FormPanel({
		id : 'tab4',
		title : '行业字典维护',
		items : [{html : '<iframe src="manage/client/maint/clientTrade/clientTrade.jsp"  width="100%" height="800" />'}]
	});

	var tabpanel = new Ext.TabPanel({
		title : 'mytab',
		activeTab : 0,
	//	autoScroll : true,
		items : [panel1, panel2, panel3,panel4]

	});
	
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		items : [tabpanel]
	});

});