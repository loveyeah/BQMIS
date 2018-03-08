Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
var currentCode = "";
var balanceCode = "";
var balaFlagCode = "";
var tabUrl = {
	billQuery : "/power/"+"run/resource/buy/purchasewarehouseCheck/billQuery/billQuery.jsp",
	materialQuery : "/power/"+"run/resource/buy/purchasewarehouseCheck/materialQuery/materialQuery.jsp"
}
function toFuz(contractCode, balCode){
	currentCode = contractCode;
	balanceCode = balCode;
}
function edit(contractCode, balCode,balaFlag) {
	currentCode = contractCode;
	balanceCode = balCode;
	balaFlagCode = balaFlag;
	Ext.getCmp("billQuery").setActiveTab(1);
}
Ext.onReady(function() {
	var tabpanel = new Ext.TabPanel({
		id : 'billQuery',
		activeTab : 0,
		border : false,
		tabPosition : 'bottom',
		autoScroll : true,
		items : [{
			id : 'tab1',
			title : '验收单据查询',
			html : '<iframe id="iframe1" name="iframe1"  src="'
					+ tabUrl.billQuery
					+ '"  frameborder="0"  width="100%" height="100%" />'
		}, {
			id : 'materialQuery',
			title : '验收物资查询',
			listeners : {
				activate : function() {
//					if (currentCode != ""
//							&& document.iframe2.contractCode != currentCode) {
//							document.iframe2.location = tabUrl.contractInfo;
//					}else if(currentCode==""){
//						document.iframe2.location = "about:blank"
//					}else if(document.iframe2.balanceCode!=balanceCode){
						document.iframe2.location = tabUrl.materialQuery;
//					}
				}
			},
			html : '<iframe id="iframe2"  name="iframe2"   frameborder="0"  width="100%" height="100%" />'

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
