Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
var contractCode = parent.currentCode;
var balanceCode = parent.balanceCode;
var balaFlagCode = parent.balaFlagCode;
var currencyName = parent.currency;
var balanceBy = parent.balanceby;
var balanceName = parent.balancename;

function edit1(balaFlag1,balanceId1){
		balanceCode = balanceId1;
		balaFlagCode = balaFlag1;
	}
Ext.onReady(function() {
	var conId = contractCode;
	var balanceId = balanceCode;
	var balaFlag = balaFlagCode;
	var currency = currencyName;
	var _url1 = "manage/projectFact/business/contractBalance/register/conBalance/balanceInfo/balanceInfo.jsp";
	if (conId != "")
		_url1 += "?conId=" + conId;
	var _url2 = "manage/projectFact/business/contractBalance/register/conBalance/balInvoice/balInvoice.jsp";
	if (balanceId != "") {
		_url1 += "&balanceId=" + balanceId;
		_url2 += "?balanceId=" + balanceId;
		if (balaFlag != "") {
			_url1 += "&balaFlag=" + balaFlag;
			_url2 += "&balaFlag=" + balaFlag;
		}
		if(currencyName != ""){
			_url1 += "&currencyName=" + currency;
			_url2 += "&currencyName=" + currency;
		}
		if(currencyName != ""){
			_url1 += "&balanceBy=" + balanceBy;
			_url2 += "&balanceBy=" + balanceBy;
		}
		if(currencyName != ""){
			_url1 += "&balanceName=" + balanceName;
			_url2 += "&balanceName=" + balanceName;
		}
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
					html : '<iframe id="iframe1" name="iframe1"  src="'
							+ _url1
							+ '"  frameborder="0"  width="100%" height="100%" />'
				}
//				, {
//					id : 'tab2',
//					title : '本次相关票据',
//					html : '<iframe id="iframe2" name="iframe2"  src="'
//							+ _url2
//							+ '"  frameborder="0"  width="100%" height="100%" />'
//
//				}
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
							border : false,
							margins : '0 0 0 0',
							// 注入表格
							items : [tabpanel]
						}]
			})
})
	