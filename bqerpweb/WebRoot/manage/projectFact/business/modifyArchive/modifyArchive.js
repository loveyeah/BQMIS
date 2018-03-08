
	var _url1 = "manage/projectFact/business/modifyArchive/tabList/list.jsp";
	var _url2 = "manage/projectFact/business/modifyArchive/tabArchive/archiveBase.jsp";
	var _url3 = "manage/projectFact/business/modifyArchive/tabPayPlan/payPlan.jsp";

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
            title : '合同变更列表',
            html : "<iframe name='iframe1' id='iframe1'  src='manage/projectFact/business/modifyArchive/tabList/list.jsp' style='width:100%;height:100%;border:0px;'></iframe>"
        }, {
            id : 'tab2',
            title : '合同变更归档申请',
            html :"<iframe name='iframe2' id='iframe2' src='manage/projectFact/business/modifyArchive/tabArchive/archiveBase.jsp'style='width:100%;height:100%;border:0px;'></iframe>"
        }, {
            id:"tab3",
            title : '付款计划',
            html : "<iframe name='iframe3' id='iframe3' src='manage/projectFact/business/modifyArchive/tabPayPlan/payPlan.jsp' style='width:100%;height:100%;border:0px;'></iframe>"
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