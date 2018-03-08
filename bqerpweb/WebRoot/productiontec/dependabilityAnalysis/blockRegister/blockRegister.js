var pBlockId=1;


Ext.onReady(function() {
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
            title : '机组列表',
            html : "<iframe name='iframe1' id='iframe1' src='productiontec/dependabilityAnalysis/blockRegister/listTab/blockList.jsp' style='width:100%;height:100%;border:0px;'></iframe>"
        }, {
            id : 'tab2',
            title : '机组',
            html :"<iframe name='iframe2' id='iframe2' src='productiontec/dependabilityAnalysis/blockRegister/blockTab/blockInfo.jsp'style='width:100%;height:100%;border:0px;'></iframe>"
        }, {
            id : 'tab3',
            title : '锅炉',
            html :"<iframe name='iframe3' id='iframe3' src='productiontec/dependabilityAnalysis/blockRegister/boilerTab/boilerInfo.jsp'style='width:100%;height:100%;border:0px;'></iframe>"
        }, {
            id : 'tab4',
            title : '汽轮机',
            html :"<iframe name='iframe4' id='iframe4' src='productiontec/dependabilityAnalysis/blockRegister/turbineTab/turbineInfo.jsp'style='width:100%;height:100%;border:0px;'></iframe>"
        }, {
            id : 'tab5',
            title : '发电机',
            html :"<iframe name='iframe5' id='iframe5' src='productiontec/dependabilityAnalysis/blockRegister/generatorInfo/generatorInfo.jsp'style='width:100%;height:100%;border:0px;'></iframe>"
        }, {
            id : 'tab6',
            title : '主变压器',
            html :"<iframe name='iframe6' id='iframe6' src='productiontec/dependabilityAnalysis/blockRegister/transformerTab/transformerTab.jsp'style='width:100%;height:100%;border:0px;'></iframe>"
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