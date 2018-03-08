Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var id=0;
	var _url1 = "hr/employee/unionPersident/unionPersidentStandard.jsp";
	var _url2 = "hr/reward/technician/technicianStanderds.jsp";
	
    var tabpanel = new Ext.TabPanel({
		id : 'maintab',
		activeTab : 0,
		border : false,
		tabPosition : 'bottom',
		autoScroll : true,
		items : [{
			id : 'tab1',
			title : '工会主席标准维护',
			html : '<iframe id="iframe1" name="iframe1"  src="' + _url1
					+ '"  frameborder="0"  width="100%" height="100%" />'
		}, {
			id : 'tab2',
			title : '技师标准维护',
			html : '<iframe id="iframe2" name="iframe2" src="' + _url2
					+ '"  frameborder="0"  width="100%" height="100%" />'
			
		}]

	});
	var layout = new Ext.Viewport({
		layout : "border",
		border : false,
		items : [{
			region : "center",
			layout : 'fit',
			collapsible : true,
			split : true,
			border :false,
			margins : '0 0 0 0',
			// 注入表格
			items : [tabpanel]
		}]
	})
	
	
})
