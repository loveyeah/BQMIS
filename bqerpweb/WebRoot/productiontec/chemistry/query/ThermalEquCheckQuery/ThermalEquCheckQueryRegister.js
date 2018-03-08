var currentRecord = null;

Ext.onReady(function() {
	var _url1 = "productiontec/chemistry/query/ThermalEquCheckQuery/ThermalEquCheckQueryList.jsp";
	var _url2 = "productiontec/chemistry/query/ThermalEquCheckQuery/ThermalEquCheckQueryBase.jsp";
    var tabpanel = new Ext.TabPanel({
		id : 'maintab',
		activeTab : 0,
		border : false,
		tabPosition : 'bottom',
		autoScroll : true,
		items : [{
			id : 'tab1',
			title : '热力设备检查情况列表',
			html : '<iframe id="iframe1" name="iframe1"  src="' + _url1
					+ '"  frameborder="0"  width="100%" height="100%" />'
		}, {
			id : 'tab2',
			title : '热力设备检查情况信息',
			html : '<iframe id="iframe2"  src="' + _url2
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
			border :false,
			margins : '0 0 0 0',
			// 注入表格
			items : [tabpanel]
		}]
	})
});

//
//
//
//var currentRecord = null;
//
//Ext.onReady(function() {
//	  var tabPanel = new Ext.TabPanel({
//        renderTo : document.body,
//        activeTab : 0,
//        tabPosition : 'bottom',
//        id:"maintab",
//        defaults : {
//            autoScroll : true
//        },
//        frame : false,
//        border : false,
//        items : [ {
//            id : 'tab2',
//            title : '热力设备检查情况登记',
//            html :"<iframe name='iframe2' id='iframe2' src='' style='width:100%;height:100%;border:0px;'></iframe>"
//        },{
//            id : 'tab1',
//            title : '热力设备检查情况列表',
//            html : "<iframe name='iframe1' id='iframe1' src='' style='width:100%;height:100%;border:0px;'></iframe>"
//        }]
//    });
//
//    // 设定布局器及面板
//    var layout = new Ext.Viewport({
//		layout : "border",
//		border : false,
//		items : [{
//			title : "",
//			region : 'center',
//			layout : 'fit',
//		//	border : false,
//			margins : '0 0 0 0',
//			split : true,
//			collapsible : false,
//			items : [tabPanel]
//		}]
//	});
//});