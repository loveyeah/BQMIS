Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
 
Ext.onReady(function() {
	var id=0;
	var _url1 = "manage/contract/business/contractMeetSign/register/meetList/meetList.jsp";
	var _url2 = "manage/contract/business/contractMeetSign/register/contBase/base.jsp";
	var _url3 = "manage/contract/business/contractMeetSign/register/payPlan/payPlan.jsp";
	
    var tabpanel = new Ext.TabPanel({
		id : 'maintab',
		activeTab : 0,
		border : false,
		//deferredRender : true,
		tabPosition : 'bottom',
		autoScroll : true,
		items : [ {
			id : 'tab1',
			title : '合同会签列表',
			html : '<iframe  id="iframe1" name="iframe1" src="' + _url1
					+ '"  frameborder="0"  width="100%" height="100%" />'
		},{
			id : 'tab2',
			title : '合同起草',
			html : '<iframe id="iframe2" name="iframe2"  src="' + _url2
					+ '"  frameborder="0"  width="100%" height="100%" />'
		}
//		, {
//			id : 'tab3',
//			title : '付款计划设置',
//			html : '<iframe id="iframe3"  name="iframe3" src="' + _url3
//					+ '"  frameborder="0"  width="100%" height="100%" />'
//		//		modified by xlhe, 2009/5/21
//			
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
