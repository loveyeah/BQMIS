Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {

			var reportNs = new ReportItem.report()
			var firsttab = new Ext.Panel({
						id : 'first',
						title : '报表维护',
						layout : 'fit',
						items : [reportNs.grid]
					})
			var itemNs = new ReportItem.item()
			var secondtab = new Ext.Panel({
						id : 'second',
						title : '报表指标维护',
						layout : 'fit',
						items : [itemNs.grid]
					})

			var blockNs = new ReportItem.block()
			var thirdtab = new Ext.Panel({
						id : 'third',
						title : '报表机组维护',
						layout : 'fit',
						items : [blockNs.grid]
					})
			var tabpanel = new Ext.TabPanel({
						id : 'tabpanel',
						activeTab : 0,
						animScroll : true,
						tabPosition : 'bottom',
						items : [firsttab, secondtab, thirdtab]
					})

			new Ext.Viewport({
						id : 'viewport',
						layout : 'fit',
						items : [tabpanel]
					})

			// 增加tabpaenl切换tab页事件
			tabpanel.on('tabchange', function(tabpanel, tab) {
						if (tab.getId() == 'second') {
							var report = reportNs.selectReport();
							if (report != null)
								itemNs.setReport(report);
							else
								tabpanel.setActiveTab(0);
						} else if (tab.getId() == 'third') {
							var report = reportNs.selectReport();
							if (report != null)
								blockNs.setReport(report);
							else
								tabpanel.setActiveTab(0);
						}
					})
		});
