Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
var currentOpCode="";
var tabUrls = {
	tabReport:'run/operateticket/business/register/report/standOpReport.jsp',
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
            		if(document.tabBaseInfo.operateCode != currentOpCode || currentOpCode==''){
						document.tabBaseInfo.location = tabUrls.tabBaseInfo; 
            		}
            	}
            },
             html : "<iframe name='tabBaseInfo' src="+tabUrls.tabBaseInfo+" style='width:100%;height:100%;border:0px;'></iframe>"
//            html : "<iframe name='tabBaseInfo' src="+tabUrls.tabBaseInfo+" style='width:100%;height:100%;border:0px;'></iframe>"
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

//	function Register() {
//		// 操作票编号
//		this.opticketCode = null;
//
//		// 新增操作票的监听器
//		this.addRecord = null;
//		// 通过操作票编号加载工作票记录的监听器
//		this._loadRecordHandlers = [];
//		// 操作票记录变更时的监听器
//		this._changeOpticketHandlers = [];
//
//		if (typeof Register._intialized == 'undefined') {
//			Register._intialized = true;
//
//			// 追加操作票
//			Register.prototype.add = function() {
//				// 操作票编号
//				this.opticketCode = null;
//				// 设置第一个页面为活动页面
//				this.toTab1();
//
//				if (this.addRecord && typeof this.addRecord == 'function') {
//					// 新增操作票
//					this.addRecord.apply(this);
//				}
//			}
//
//			// 编辑操作票
//			Register.prototype.edit = function(argCode) {
//				// 设置操作票编号
//				this.opticketCode = argCode;
//				// 设置第一个页面为活动页面
//				this.toTab1();
//
//				// 触发通过操作票编号加载工作票记录的监听器
//				for (var i = 0; i < this._loadRecordHandlers.length; i++) {
//					var fun = this._loadRecordHandlers[i];
//					if (fun && typeof fun == 'function') {
//						fun.call(this, this.opticketCode);
//					}
//				}
//			}
//
//			// 操作票记录变更
//			Register.prototype.changeOpticket = function(argChange) {
//				// 触发操作票记录变更时的监听器
//				for (var i = 0; i < this._changeOpticketHandlers.length; i++) {
//					var fun = this._changeOpticketHandlers[i];
//					if (fun && typeof fun == 'function') {
//						fun.call(this, this.opticketCode);
//					}
//				}
//
//				if (argChange) {
//					// 迁移到操作项目信息列表Tab页
//					this.toTab2();
//				}
//			}
//
//			// 添加通过操作票编号加载工作票记录时的监听器
//			Register.prototype.addLoadOpticketHandler = function(argHandler) {
//				if (!argHandler || !(typeof argHandler == 'function')) {
//					return;
//				}
//				this._loadRecordHandlers.push(argHandler);
//			}
//
//			// 添加操作票记录变更时的监听器
//			Register.prototype.addOpticketChange = function(argHandler) {
//				if (!argHandler || !(typeof argHandler == 'function')) {
//					return;
//				}
//				this._changeOpticketHandlers.push(argHandler);
//			}
//
//			// 迁移到操作票详细信息Tab页
//			Register.prototype.toTab1 = function() {
//				tabPanel.setActiveTab('tabBaseInfo');
//			}
//
//			// 迁移到操作项目信息列表Tab页
//			Register.prototype.toTab2 = function() {
//				tabPanel.setActiveTab('tabContent');
//			}
//
//			// 迁移到危险点信息列表Tab页
//			Register.prototype.toTab3 = function() {
//				tabPanel.setActiveTab('tabDanger');
//			}
//		}
//	}
//	var register = new Register();
//	Ext.getCmp('tabPanel').register = register;
});
