var currentRecord = null;
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
            title : '活动纪要信息查询',
            html :"<iframe name='iframe1' id='iframe1' src='productiontec/heatControlSupervise/query/superviseInformationQuery/activityInfoQuery.jsp'style='width:100%;height:100%;border:0px;'></iframe>"
        },
        {
            id : 'tab2',
            title : '总结信息查询',
            html : "<iframe name='iframe2' id='iframe2'  src='productiontec/heatControlSupervise/query/superviseInformationQuery/summaryInfoQuery.jsp' style='width:100%;height:100%;border:0px;'></iframe>"
        },
        {
            id : 'tab3',
            title : '事件分析信息查询',
            html : "<iframe name='iframe2' id='iframe2'  src='productiontec/heatControlSupervise/query/superviseInformationQuery/eventAnalInfoQuery.jsp' style='width:100%;height:100%;border:0px;'></iframe>"
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