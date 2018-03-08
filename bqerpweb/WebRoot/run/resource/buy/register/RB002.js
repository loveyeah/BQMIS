Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
var first=true;
Ext.onReady(function() {
    Ext.QuickTips.init();

    // 总tabpanel
    var tabPanel = new Ext.TabPanel({
        renderTo : document.body,
        activeTab : 0,
        tabPosition : 'bottom',
        id:"tabPanel",
        plain : true,
        defaults : {
            autoScroll : true
        },
        frame : false,
        border : false,
        items : [{
            id : 'tabContent',
            title : '登记',
            html : "<iframe name='tabContent' src='run/resource/buy/register/content/RB002_orderRegisterContent.jsp' style='width:100%;height:100%;border:0px;'></iframe>"
        },{
            id : 'tabTable',
            title : '列表',
            html : "<iframe name='tabTable' src='run/resource/buy/register/table/RB002_table.jsp' style='width:100%;height:100%;border:0px;'></iframe>"
        }]
    });

    // 设定布局器及面板
    var layout = new Ext.Viewport({
        layout : "border",
        border : false,
        items : [{
            region : 'center',
            layout : 'fit',
            border : false,
            split : true,
            collapsible : false,
            items : [tabPanel]
        }]
    });
    
    function Register() {
        // 采购单对象
        this.orderData = null;
        // 当前编辑的采购单流水号
        this.orderId = null;
        
        // 刷新采购单列表的监听器
        this.refreshListHandler = null;
        // 加载采购单记录的监听器
        this.editOrderHandler = null;
        
        if (typeof Register._intialized == 'undefined') {
            Register._intialized = true;
            
            // 刷新采购单列表
            Register.prototype.refreshList = function(argOrderId) {
		    	if (argOrderId) {
	            	// 设置当前编辑的采购单流水号
			    	this.orderId = argOrderId;
		    	} else {
		    		this.orderId = null;
		    	}
		    	
            	if (this.refreshListHandler && typeof this.refreshListHandler == 'function') {
                    // 刷新采购单列表
                    this.refreshListHandler.apply(this);
                }
            }
            
            // 编辑采购单
            Register.prototype.edit = function(argOrderData, argRefresh) {
                // 设置采购单对象
                this.orderData = argOrderData;
                // 设置采购单编辑Tab页为活动页面
                this.toTab2();
                
                // 触发加载采购单记录的监听器
                if (this.editOrderHandler && typeof this.editOrderHandler == 'function') {
                    // 编辑采购单
                    this.editOrderHandler.call(this, this.orderData, argRefresh);
                }
            }
            
            // 迁移到采购单列表Tab页
            Register.prototype.toTab1 = function() {
                tabPanel.setActiveTab('tabTable');
            }
            
            // 迁移到采购单编辑Tab页
            Register.prototype.toTab2 = function() {
                tabPanel.setActiveTab('tabContent');
            }
            
            Register.prototype.test = function(obj, flag) {
            	var str = '';
            	for (var prop in obj) {
            		if (flag && typeof obj[prop] == 'function') {
            			continue;
            		}
            		if (obj[prop] instanceof Array) {
            			str += prop + ': [' + obj[prop].join(',') + ']';
            		} else {
            			str += prop + ': ' + obj[prop] + '\n';
            		}
            	}
            	alert(str);
            }
        }
    }
    var register = new Register();
    Ext.getCmp('tabPanel').register = register;
});
