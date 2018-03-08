Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
var tabUrl = {
	contractInfo : "/power/manage/contract/business/contractBalance/contractPayment/paymentTab/paymentInfo/paymentInfo.jsp",
	balanceList : "/power/manage/contract/business/contractBalance/contractPayment/paymentTab/paymentList/paymentList.jsp"
}
// 列表刷新
var refreshFlag = true;
// 传入信息页面的申请id
var flagId = null;
Ext.onReady(function() {
	var tabpanel = new Ext.TabPanel({
		id : 'maintab',
		activeTab : 0,
		border : false,
		tabPosition : 'bottom',
		autoScroll : true,
		items : [{
			id : 'tab1',
			title : '付款列表',
			listeners : {
				activate : function() {
					if (refreshFlag) {
							document.iframe1.location = tabUrl.balanceList;
					}
				}
			},
			html : '<iframe id="iframe1"  name="iframe1"   frameborder="0"  width="100%" height="100%" />'
			},{
			id : 'tab2',
			title : '付款申请',
			html : '<iframe id="iframe2" name="iframe2"  src="'
					+ tabUrl.contractInfo
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
