Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	Ext.QuickTips.init();   
	var tabPanel = new Ext.TabPanel({
		renderTo : document.body,
		activeTab : 0, 
		tabPosition : 'bottom',
		id : "tabPanel",
		plain : true, 
		defaults : {
			autoScroll : true
		},
		frame : false,
		border : false,
		items : [{
			id : 'receiveMessage',
			title : '消息接收',  
			html : "<iframe name='receive' src='message/bussiness/receivemsg/msgreceive.jsp' style='width:100%;height:100%;border:0px;'></iframe>"

		}, {
			id : 'sendMessage',
			title : '消息发送',
			html : "<iframe name='send'  src='message/bussiness/sendmsg/msgsd.jsp' style='width:100%;height:100%;border:0px;'></iframe>"
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
 
