Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif'; 
Ext.onReady(function() {
	var args = window.dialogArguments;
	var isCollectItem = args.isCollectItem;
	Ext.QuickTips.init(); 
	var tab = new Ext.TabPanel({ 
		id : "mainTabPanel", 
		activeTab : 0
	}); 
	if(isCollectItem)
	{ 
		tab.add({
			id : 'collectDotSet',
			layout:'fit',
			title : '对应采集点',
			html : "<iframe name='collectDotSet' src='manage/stat/maint/itemDetailSet/collectionSet/collectionSet.jsp' style='width:100%;height:100%;border:0px;'></iframe>"
		});
	} 
	tab.add({
			id : 'timeDotSet',
			layout:'fit',
			title : '时间点设置',
			html : "<iframe name='timeDotSet' src='manage/stat/maint/itemDetailSet/timeDotSet/timeDotSet.jsp' style='width:100%;height:100%;border:0px;'></iframe>"
		});
	var view = new Ext.Viewport({
		layout : 'fit',
		items : [tab]
	});
	
});