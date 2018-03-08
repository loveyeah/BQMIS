Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
var currentOpCode="";
var tabUrls = {
	tabReport:'run/operateticket/business/register/report/standOpUpdateList.jsp',
	tabBaseInfo:'/power/run/operateticket/business/register/baseInfo/standOpBaseInfo.jsp',
	tabContent:'/power/run/operateticket/business/register/content/opContent.jsp',
	tabDanger:'/power/run/operateticket/business/register/content/dangerMeasure.jsp' 
}; 
function edit(opCode)
{
	currentOpCode = opCode; 
	Ext.getCmp("tabPanel").setActiveTab(0); 
}
Ext.onReady(function() {
	Ext.QuickTips.init();
	// 总tabpanel
	var tabPanel = new Ext.TabPanel({
        renderTo : document.body,
        activeTab : 3,
        tabPosition : 'bottom',
        id:"tabPanel",
        plain : true,
//        deferredRender : false,
        defaults : {
            autoScroll : true
        },
        frame : false,
        border : false,
        items : [{
            id : 'tabBaseInfo',
            title : '详细信息',
            listeners : {
            	activate : function(){
            		if(currentOpCode!="" && document.tabBaseInfo.operateCode != currentOpCode){
            			document.tabBaseInfo.location = tabUrls.tabBaseInfo; 				
            		}else if(currentOpCode==""){
            			document.tabBaseInfo.location = "about:blank";
            		}
            	}
            },
             html : "<iframe name='tabBaseInfo' src="+tabUrls.tabBaseInfo+" style='width:100%;height:100%;border:0px;'></iframe>"
        }, {
            id : 'tabContent',
            title : '操作项目信息列表',
            listeners : {
            	activate : function(){
            		if(currentOpCode!="" && document.tabContent.operateCode != currentOpCode){
            			document.tabContent.location = tabUrls.tabContent; 				
            		}else if(currentOpCode==""){
            			document.tabContent.location = "about:blank";
            		}
            	}
            },
            html : "<iframe name='tabContent' src="+tabUrls.tabContent+" style='width:100%;height:100%;border:0px;'></iframe>"
        },  {
            id : 'tabDanger',
            title : '危险点信息列表',
            listeners : {
            	activate : function(){ 
            		if(currentOpCode!="" && document.tabDanger.operateCode != currentOpCode){
            			document.tabDanger.location = tabUrls.tabDanger; 
            		}else if(currentOpCode==""){
            			document.tabDanger.location = "about:blank";
            		}
            	}
            },
            html : "<iframe name='tabDanger' src="+tabUrls.tabDanger+"  style='width:100%;height:100%;border:0px;'></iframe>"
        },{
            id : 'tabReport',
            title : '上报列表',
            html : "<iframe name='tabReport' src="+tabUrls.tabReport+" style='width:100%;height:100%;border:0px;'></iframe>"
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
