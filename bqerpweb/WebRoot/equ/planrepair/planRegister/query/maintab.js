Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

var repair = "";
var myRecord = "";
//function edit(repairId) {
//	repair = repairId;
//}
Ext.onReady(function() {
	var _url1 = "equ/planrepair/planRegister/query/list.jsp";
	var _url2 = "equ/planrepair/planRegister/query/baseInfo.jsp";
	var _url3 = "equ/planrepair/planRegister/query/detailList.jsp";
	var tabpanel = new Ext.TabPanel({
				id : 'maintab',
				activeTab : 0,
				border : false,
				// deferredRender : true,
				tabPosition : 'bottom',
				autoScroll : true,
				items : [{
					id : 'tab1',
					title : '计划检修列表',
					html : '<iframe id="iframe1" name="iframe1"  src="'
							+ _url1
							+ '"  frameborder="0"  width="100%" height="100%" />'
				}, {
					id : 'tab2',
					title : '计划检修内容',
					listeners : {
						activate : function() {
							if (repair != "" || repair != repair) {
									iframe2.location = "../../../../" + _url2;
							}
						}
					},
					html : '<iframe id="iframe2" name="iframe2" src="'
							+ _url2
							+ '"  frameborder="0"  width="100%" height="100%" />'

				}, {
					id : 'tab3',
					title : '计划检修明细',
					listeners : {
						activate : function() {
							if (repair != "" || repair != repair) {
								iframe3.location = "../../../../" + _url3
							} else{
								iframe3.location = 'about:blank'
							}
						}
					},
					html : '<iframe id="iframe3" name="iframe3" src="'
							+ _url3
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
							items : [tabpanel]
						}]
			})

})
