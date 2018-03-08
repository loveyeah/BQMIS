Ext.onReady(function() {

	var panel1 = new Ext.FormPanel({
		id : 'tab1',
		title : '机组编码',
		items : [{html : '<iframe src="equ/base/maint/equcode/equBlockMaint.jsp"  width="100%" height="800" />'}]
	});
	var panel2 = new Ext.FormPanel({
		id : 'tab2',
		title : '系统编码',
		items : [{html : '<iframe src="equ/base/maint/equcode/sysCodeMaint.jsp"  width="100%" height="800" />'}]
	});
	var panel3 = new Ext.FormPanel({
		id : 'tab3',
		title : '设备编码',
		items : [{html : '<iframe src="equ/base/maint/equcode/equCodeMaint.jsp"  width="100%" height="800" />'}]
	});
	var panel4 = new Ext.FormPanel({
		id : 'tab4',
		title : '部件编码',
		items : [{html : '<iframe src="equ/base/maint/equcode/partCodeMaint.jsp"  width="100%" height="800" />'}]
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