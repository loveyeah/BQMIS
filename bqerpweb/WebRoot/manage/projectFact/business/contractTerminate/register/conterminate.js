Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
 
Ext.onReady(function() {
	var id = getParameter("id");
	var _url1 = "manage/projectFact/business/contractTerminate/register/terminateList/terlist.jsp";
	var _url2 = "manage/projectFact/business/contractTerminate/register/terminateBase/terbase.jsp";
    var tabpanel = new Ext.TabPanel({
		id : 'maintab',
		activeTab : 0,
		border : false,
		tabPosition : 'bottom',
		autoScroll : true,
		items : [{
			id : 'tab1',
			title : '合同列表',
			html : '<iframe id="iframe1" name="iframe1"  src="' + _url1
					+ '"  frameborder="0"  width="100%" height="100%" />'
					
			
		}, {
			id : 'tab2',
			title : '合同验收终止',
			html : '<iframe id="iframe2" name="iframe2" src="' + _url2
					+ '"  frameborder="0"  width="100%" height="100%" />'
//			listeners : {
//						activate : onClick
//					}
		}]
	});	
//	function onClick()
//	{
//		var record = grid.getSelectionModel().getSelected();
//		var recordslen = record.length;
//		alert(1);
//		if (recordslen > 0){
//			alert(1);
//			id = record.data.conId;
//			var currencyType = record.data.currencyType;
//			var actAmount = record.data.actAmount;
//			parent.Ext.getCmp("maintab").setActiveTab(1);
//			var url = "manage/contract/business/contractTerminate/register/terminateBase/terbase.jsp";
//			parent.document.all.iframe2.src = url + "?id=" + id;			
//		}
//		else{
//			Ext.Msg.alert("提示", "请选择一行要编辑的记录！");
//		}
//	}
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
