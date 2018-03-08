var currentRecord = null;
var tabUrls = {
	applyList : 'run/protectinoutapply/register/protectInOutApplyList.jsp',
	applyIn : '/power/run/protectinoutapply/register/protectInApplyBase.jsp',
	applyOut : '/power/run/protectinoutapply/register/protectInOutApplyBase.jsp'
};
Ext.onReady(function() {

	var tabPanel = new Ext.TabPanel({
		renderTo : document.body,
		activeTab : 2,
		tabPosition : 'bottom',
		id : "maintab",
		plain : true,
		defaults : {
			autoScroll : true
		},
		frame : false,
		border : false,
		items : [{
			id : 'tab1',
			title : '保护退出申请',
			listeners : {
				activate : function() {
					if (typeof(currentRecord) != null)
						document.iframe1.location = tabUrls.applyOut;
				}
			},
			html : "<iframe name='iframe1' id='iframe1'  src='"+tabUrls.applyOut+"' style='width:100%;height:100%;border:0px;'></iframe>"
		}, {
			id : 'tab3',
			title : '保护投入申请',
			listeners : {
				activate : function() {
					if (typeof(currentRecord) != null)
						document.iframe3.location = tabUrls.applyIn;
				}
			},
			html : "<iframe name='iframe3' id='iframe3'  src='"+tabUrls.applyIn+"' style='width:100%;height:100%;border:0px;'></iframe>"
		}, {
			id : 'tab2',
			title : '申请单上报列表',
			html : "<iframe name='iframe2' id='iframe2' src='"+tabUrls.applyList+"'style='width:100%;height:100%;border:0px;'></iframe>"
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
			items : [tabPanel]
		}]
	});

});