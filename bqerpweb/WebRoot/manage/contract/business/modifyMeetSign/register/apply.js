Ext.onReady(function() {
	var id = 0;
	var _url1 = "manage/contract/business/modifyMeetSign/register/tabList/list.jsp";
	var _url2 = "manage/contract/business/modifyMeetSign/register/tabBase/base.jsp";
	var _url3 = "manage/contract/business/modifyMeetSign/register/tabPayPlan/payplan.jsp";
	var tabPanel = new Ext.TabPanel({
				renderTo : document.body,
				activeTab : 0,
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
					title : '合同变更列表',
					html : '<iframe id="iframe1" name="iframe1"  src="'
							+ _url1
							+ '"  frameborder="0"  width="100%" height="100%" />'
				}, {
					id : 'tab2',
					title : '合同变更',
					html : '<iframe id="iframe2" name="iframe2"  src="'
							+ _url2
							+ '"  frameborder="0"  width="100%" height="100%" />'
				}
//				, {
//					id : "tab3",
//					title : '付款计划变更',
//					html : '<iframe  id="iframe3" name="iframe3" src="'
//							+ _url3
//							+ '"  frameborder="0"  width="100%" height="100%" />'
//							,
//					listeners : {
//						activate : function() {
//                               alert(iframe3.location)
//						}
//					}
//				}
				]
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