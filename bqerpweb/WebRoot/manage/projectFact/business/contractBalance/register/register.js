Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
var currentCode = "";
var balanceCode = "";
var balaFlagCode = "";
var currency = "";
var balancename = "";
var balanceby = "";
var tabUrl = {
	contractInfo : "/power/manage/projectFact/business/contractBalance/register/conBalance/conBalance.jsp",
	balanceList : "/power/manage/projectFact/business/contractBalance/register/balanceList/balanceList.jsp"
}
function toFuz(contractCode, balCode,balaFlag){
	currentCode = contractCode;
	balanceCode = balCode;
	balaFlagCode = balaFlag;
}
function edit(contractCode, balCode,balaFlag,currencyName,balanceBy,balanceName) {
	currentCode = contractCode;
	balanceCode = balCode;
	balaFlagCode = balaFlag;
	currency = currencyName;
	balanceby = balanceBy;
	balancename = balanceName;
	Ext.getCmp("maintab").setActiveTab(1);
}
Ext.onReady(function() {
	var tabpanel = new Ext.TabPanel({
		id : 'maintab',
		activeTab : 0,
		border : false,
		tabPosition : 'bottom',
		autoScroll : true,
		items : [{
			id : 'tab1',
			title : '合同列表',
			html : '<iframe id="iframe1" name="iframe1"  src="'
					+ tabUrl.balanceList
					+ '"  frameborder="0"  width="100%" height="100%" />'
		}, {
			id : 'tab2',
			title : '结算申请',
			listeners : {
				activate : function() {
					if (currentCode != ""
							&& document.iframe2.contractCode != currentCode) {
							document.iframe2.location = tabUrl.contractInfo;
					}else if(currentCode==""){
						document.iframe2.location = "about:blank"
					}else if(document.iframe2.balanceCode!=balanceCode || balanceCode != ""){
						document.iframe2.location = tabUrl.contractInfo;
					}
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
