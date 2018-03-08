var currentRecord = null;

Ext.onReady(function() {
	var _url1 = "productiontec/chemistry/query/condenserLeakQuery/condenserLeakQueryList.jsp";
	var _url2 = "productiontec/chemistry/query/condenserLeakQuery/condenserLeakQueryBase.jsp";
    var tabpanel = new Ext.TabPanel({
		id : 'maintab',
		activeTab : 0,
		border : false,
		tabPosition : 'bottom',
		autoScroll : true,
		items : [{
			id : 'tab1',
			title : '凝汽器泄漏台帐列表',
			html : '<iframe id="iframe1" name="iframe1"  src="' + _url1
					+ '"  frameborder="0"  width="100%" height="100%" />'
		}, {
			id : 'tab2',
			title : '凝汽器泄漏台帐信息',
			html : '<iframe id="iframe2"  src="' + _url2
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
});
