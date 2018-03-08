//var pBlockId=1;

Ext.onReady(function() {
//	alert(type)
	  var tabPanel = new Ext.TabPanel({
        renderTo : document.body,
        activeTab : 0,
     //   tabPosition : 'bottom',
        id:"maintab",
        plain : true,
        defaults : {
            autoScroll : true
        },
        frame : false,
        border : false,
        items : [{
            id : 'tab1',
            title : '引风机信息列表',
            html : "<iframe name='iframe1' id='iframe1' src='productiontec/dependabilityAnalysis/auxiliaryRegister/fuelList/fuelList.jsp' style='width:100%;height:100%;border:0px;'></iframe>"
        }, {
            id : 'tab2',
            title : '引风机基本信息',
            html :"<iframe name='iframe2' id='iframe2' src='productiontec/dependabilityAnalysis/auxiliaryRegister/fuelInfo/fuelInfo.jsp'style='width:100%;height:100%;border:0px;'></iframe>"
        }, {
            id : 'tab3',
            title : '引风机技术参数',
            html :"<iframe name='iframe3' id='iframe3' src='productiontec/dependabilityAnalysis/IDFRegister/IDFParam/IDFParam.jsp'style='width:100%;height:100%;border:0px;'></iframe>"
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