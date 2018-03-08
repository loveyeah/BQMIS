Ext.onReady(function() {

	var panel1 = new Ext.FormPanel({
		id : 'tab1',
		title : '类型维护',
		items : [{html : '<iframe src="manage/contract/maint/cooperator/conclienttype.jsp"  width="100%" height="800" />'}]
	});
	var panel2 = new Ext.FormPanel({
		id : 'tab2',
		title : '公司性质维护',
		items : [{html : '<iframe src="manage/contract/maint/cooperator/conclientcharacter.jsp"  width="100%" height="800" />'}]
	});
	var panel3 = new Ext.FormPanel({
		id : 'tab3',
		title : '重要程度维护',
		items : [{html : '<iframe src="manage/contract/maint/cooperator/clientImport.jsp"  width="100%" height="800" />'}]
	});
	var panel4 = new Ext.FormPanel({
		id : 'tab4',
		title : '行业字典维护',
		items : [{html : '<iframe src="manage/contract/maint/cooperator/conTrade.jsp"  width="100%" height="800" />'}]
	});

	var tabpanel = new Ext.TabPanel({
		activeTab : 0,
		tabPosition : 'bottom',
	//	autoScroll : true,
		items : [panel1, panel2, panel3,panel4]
		

	});
	
	new Ext.Viewport({
		//title : '合作伙伴基础数据维护',
		enableTabScroll : true,
		layoutOnTabChange:true,
		layout : "fit",
		items : [tabpanel]
	});

});