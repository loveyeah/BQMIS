Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

//var weldId=null;
//var workerName=null;

Ext.onReady(function() {
	var id = 0;
	var _url1 = "productiontec/thermalSupervise/maint/instrumentParams/sort.jsp";
	 var _url2 = "productiontec/thermalSupervise/maint/instrumentParams/grade.jsp";
	 var _url3 = "productiontec/thermalSupervise/maint/instrumentParams/precision.jsp";
	
	var tabpanel = new Ext.TabPanel({
				id : 'maintab',
				activeTab : 0,
				border : false,
				// deferredRender : true,
				tabPosition : 'bottom',
				autoScroll : true,
				items : [{
					id : 'tab1',
					title : '类别维护',
					html : '<iframe id="iframe1" name="iframe1"  src="'
							+ _url1
							+ '"  frameborder="0"  width="100%" height="100%" />'
				}, {
					id : 'tab2',
					title : '等级维护',
					html : '<iframe id="iframe2"  src="'
							+ _url2
							+ '"  frameborder="0"  width="100%" height="100%" />'
				}, {
					id : 'tab3',
					title : '精度维护',
					html : '<iframe id="iframe3"  src="'
							+ _url3
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
							border : false,
							margins : '0 0 0 0',
							// 注入表格
							items : [tabpanel]
						}]
			})
})
