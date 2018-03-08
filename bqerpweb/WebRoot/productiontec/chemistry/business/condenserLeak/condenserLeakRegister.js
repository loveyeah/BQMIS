var currentRecord = null;

Ext.onReady(function() {
	  var tabPanel = new Ext.TabPanel({
        renderTo : document.body,
        activeTab : 0,
        tabPosition : 'bottom',
        id:"maintab",
      //  plain : true,
        defaults : {
            autoScroll : true
        },
        frame : false,
        border : false,
        items : [ {
            id : 'tab2',
            title : '凝汽器泄漏台帐登记',
            html :"<iframe name='iframe2' id='iframe2' src='productiontec/chemistry/business/condenserLeak/condenserLeakBase.jsp' style='width:100%;height:100%;border:0px;'></iframe>"
        },{
            id : 'tab1',
            title : '凝汽器泄漏台帐列表',
            html : "<iframe name='iframe1' id='iframe1' src='productiontec/chemistry/business/condenserLeak/condenserLeakList.jsp' style='width:100%;height:100%;border:0px;'></iframe>"
        }]
    });

    // 设定布局器及面板
    var layout = new Ext.Viewport({
		layout : "border",
		border : false,
		items : [{
			title : "",
			region : 'center',
			layout : 'fit',
		//	border : false,
			margins : '0 0 0 0',
			split : true,
			collapsible : false,
			items : [tabPanel]
		}]
	});
});