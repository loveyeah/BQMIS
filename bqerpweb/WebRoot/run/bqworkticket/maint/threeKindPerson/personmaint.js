Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	Ext.QuickTips.init();

	// 总tabpanel
	var tabpanel = new Ext.TabPanel({
		renderTo : document.body,
		activeTab : 0,
		tabPosition : 'bottom',
		plain : true,
		defaults : {
			autoScroll : true
		},
		frame : false,
		border : false,
		items : [{
			title : '工作票签发人维护',
			html : "<iframe src='run/bqworkticket/maint/threeKindPerson/signperson.jsp' style='width:100%;height:100%;border:0px;'></iframe>"
		}, {
			title : '工作票许可人维护',
			html : "<iframe src='run/bqworkticket/maint/threeKindPerson/permissonperson.jsp' style='width:100%;height:100%;border:0px;'></iframe>"
		}, {
			title : '工作票负责人维护',
			html : "<iframe src='run/bqworkticket/maint/threeKindPerson/resperson.jsp' style='width:100%;height:100%;border:0px;'></iframe>"
		}]
	});

	// 设定布局器及面板
	var layout = new Ext.Viewport({
				layout : "border",
				border : false,
				items : [{
							title : "",
							region : 'center',
							layout : 'fit',
							border : false,
							margins : '0 0 0 0',
							split : true,
							collapsible : false,
							items : [tabpanel]
						}]
	});
});
