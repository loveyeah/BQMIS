Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var _url1 = "productiontec/business/dependabilityReport.jsp";
	var _url2 = "productiontec/business/dependabilityReportS.jsp";
	var _url3 = "productiontec/business/dependabilityReportM.jsp";

	var tabpanel = new Ext.TabPanel({
				id : 'maintab',
				activeTab : 0,
				border : false,
				items : [{
					id : 'tab1',
					title : '可靠性月度报表',
					html : '<iframe  id="iframe3" src="'
							+ _url3
							+ '"  frameborder="0"  width="100%" height="100%" />',
					listeners : {
						activate : function() {
							document.getElementById("iframe3").src = document
									.getElementById("iframe3").src;
						}
					}
				}, {
					id : 'tab2',
					title : '可靠性季度报表',
					html : '<iframe id="iframe2"  src="'
							+ _url2
							+ '"  frameborder="0"  width="100%" height="100%" />',
					listeners : {
						activate : function() {
							Ext.get("iframe2").dom.src = Ext.get("iframe2").dom.src;
						}
					}
				}, {
					id : 'tab3',
					title : '可靠性年度报表',
					html : '<iframe id="iframe1" name="iframe1"  src="'
							+ _url1
							+ '"  frameborder="0"  width="100%" height="100%" />',
					listeners : {
						activate : function() {
							Ext.get("iframe1").dom.src = Ext.get("iframe1").dom.src;
						}
					}
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
							margins : '0 0 0 0',
							// 注入表格
							items : [tabpanel]
						}]
			})
})
