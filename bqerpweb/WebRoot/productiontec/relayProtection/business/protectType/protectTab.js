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
            title : '保护类型名称维护',
            html :"<iframe name='iframe1' id='iframe1' src='productiontec/relayProtection/business/protectType/protectType.jsp'style='width:100%;height:100%;border:0px;'></iframe>"
        },
        {
            id : 'tab2',
            title : '定值项目维护',
            html : "<iframe name='iframe2' id='iframe2'  src='productiontec/relayProtection/business/protectType/protectPro.jsp' style='width:100%;height:100%;border:0px;'></iframe>"
        },
        {
            id : 'tab3',
            title : '保护装置对应保护类型',
            html : "<iframe name='iframe3' id='iframe3'  src='productiontec/relayProtection/business/protectType/deviceAndType.jsp' style='width:100%;height:100%;border:0px;'></iframe>"
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