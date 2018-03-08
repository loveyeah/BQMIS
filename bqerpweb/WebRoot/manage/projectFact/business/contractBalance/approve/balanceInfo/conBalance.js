Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
 
Ext.onReady(function() {
	var conId=getParameter("conId");
	var balanceId = getParameter("balanceId");
	var _url1 = "manage/projectFact/business/contractBalance/approve/balanceInfo/balanceInfo.jsp";
	if(conId!="")
		_url1 +="?conId="+conId;	
	var _url2 = "manage/projectFact/business/contractBalance/approve/balanceInfo/balInvoice.jsp";
//	var _url3 = "manage/projectFact/business/contractBalance/approve/balanceInfo/materialList.jsp";
	if(balanceId!=""){
		_url1 +="&balanceId="+balanceId;
		_url2 +="?balanceId="+balanceId;
//		_url3 +="?balanceId="+balanceId;
	}
    var tabpanel = new Ext.TabPanel({
		id : 'maintab',
		activeTab : 0,
		border : false,
		tabPosition : 'top',
		autoScroll : true,
		items : [{
			id : 'tab1',
			title : '本次付款信息',
			html : '<iframe id="iframe1" name="iframe1"  src="' + _url1
					+ '"  frameborder="0"  width="100%" height="100%" />'
		}
//		, {
//			id : 'tab2',
//			title : '本次相关票据',
//			html : '<iframe id="iframe2"  src="' + _url2
//					+ '"  frameborder="0"  width="100%" height="100%" />'
//			
//		}
//		, {
//			id : 'tab3',
//			title : '本次采购物资',
//			html : '<iframe id="iframe3"  src="' + _url3
//					+ '"  frameborder="0"  width="100%" height="100%" />'
//			
//		}
		]

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
