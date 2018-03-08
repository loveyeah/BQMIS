var currentRecord = null;
var first=true;
Ext.onReady(function() {
	  var tabPanel = new Ext.TabPanel({
        renderTo : document.body,
        activeTab : 0,
        tabPosition : 'bottom',
        id:"maintab",
        plain : true,
        defaults : {
            autoScroll : true
        },
        frame : false,
        border : false,
        items : [{
            id : 'tab1',
            title : '物资临时编码审核列表',
            html : "<iframe name='iframe1' id='iframe1' src='run/resource/information/tempMaterialCheck/tempMaterialCheckList.jsp' style='width:100%;height:100%;border:0px;'></iframe>"
        }, {
            id : 'tab2',
            title : '物资临时编码审核登记',
            html :"<iframe name='iframe2' id='iframe2' src=''style='width:100%;height:100%;border:0px;'></iframe>"
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
			border : false,
			margins : '0 0 0 0',
			split : true,
			collapsible : false,
			items : [tabPanel]
		}]
	});
});