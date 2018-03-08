Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
    Ext.QuickTips.init();
	var PD002_MSG = Ext.Msg;
    // 总tabpanel
    var tabPanel = new Ext.TabPanel({
        renderTo : document.body,
        activeTab : 0,
        tabPosition : 'bottom',
        id : 'tabPanel',
        plain : true,
        defaults : {
            autoScroll : true
        },
        frame : false,
        border : false,
        items : [{
            id : 'tabQuery',
            title : '查询',
            html : "<iframe name='tabQuery' src='hr/document/jobmanagement/newemployeeregister/query/PD002_query.jsp' style='width:100%;height:100%;border:0px;'></iframe>"
        }, {
            id : 'tabRegister',
            title : '登记',
            html : "<iframe name='tabRegister' src='hr/document/jobmanagement/newemployeeregister/register/PD002_register.jsp' style='width:100%;height:100%;border:0px;'></iframe>"
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
    
    function NewEmployee() {
    	// 人员信息
    	this.empInfo = null;
    	// 部门名称
    	this.name = "";
    	// 部门ID
    	this.id = "";
    	// 新增/修改标识
    	this.flag = null;
    	
    	// 常量
    	// 员工状态: 2
		this.EMP_STATE_2 = "2";
		// 是否退转军人: 是
		this.IS_VETERAN_Y = "1"
		// 是否退转军人: 否
		this.IS_VETERAN_N = "0"
		// 是
		this.IF_YES = "是";
		// 否
		this.IF_NO = "否";
		// 新增
		this.ADD = "A";
		// 修改
		this.UPDATE = "U";
		// 控件类型: 输入
		this.TYPE_INPUT = "I";
		// 控件类型: 选择
		this.TYPE_SELECT = "S";
		// 是否存档: 是
		this.IF_SAVE_Y = "Y";
		// 是否存档: 否
		this.IF_SAVE_N = "N";
    	
    	// 刷新查询Tab页的监听器
        this.refreshTabQueryHandler = null;
        // 刷新登记Tab页的监听器
        this.refreshTabRegisterHandler = null;
        // 加载登记Tab页的监听器
        this.editTabRegisterHandler = null;
        
        // 刷新查询Tab页
        NewEmployee.prototype.refreshTabQuery = function() {
        	if (this.refreshTabQueryHandler && typeof this.refreshTabQueryHandler == 'function') {
                // 刷新查询Tab页
                this.refreshTabQueryHandler.apply(this);
            }
        }
        
        // 加载登记Tab页
        NewEmployee.prototype.editTabRegister = function(argEmpInfo) {
            // 设置人员信息
            this.empInfo = argEmpInfo;
            // 设置登记Tab页为活动页面
            this.toTabRegister();
            
            // 触发加载登记Tab页的监听器
            if (this.editTabRegisterHandler && typeof this.editTabRegisterHandler == 'function') {
                // 编辑登记Tab页
                this.editTabRegisterHandler.call(this, this.empInfo);
            }
        }
        
        // 刷新登记Tab页
        NewEmployee.prototype.refreshTabRegister = function() {
        	// 触发刷新登记Tab页的监听器
            if (this.refreshTabRegisterHandler && typeof this.refreshTabRegisterHandler == 'function') {
                // 编辑登记Tab页
                this.refreshTabRegisterHandler.call(this);
            }
        }
        
    	// 迁移到查询Tab页
        NewEmployee.prototype.toTabQuery = function() {
            tabPanel.setActiveTab('tabQuery');
        }
        
        // 迁移到登记Tab页
        NewEmployee.prototype.toTabRegister = function() {
            tabPanel.setActiveTab('tabRegister');
        }
        
        // 选择部门
		NewEmployee.prototype.deptSelect =
		    function deptSelect() {
		    	var args = {
		    		selectModel : 'single',
		    		rootNode : {
		    			id : '0',
		    			text : '灞桥电厂'
		    		}
		    	};
		    	// 调用画面
				var object = window.showModalDialog(
								'../../../../comm/jsp/hr/dept/dept.jsp',
								args,
								'dialogWidth='  + Constants.WIDTH_COM_DEPT +
								'px;dialogHeight=' + Constants.HEIGHT_COM_DEPT +
								'px;center=yes;help=no;resizable=no;status=no;');
				// 根据返回值设置画面的值
				if (object) {
					if (typeof(object.names) != "undefined") {
						this.name = object.names;
					}
					if (typeof(object.ids) != "undefined") {
						this.id = object.ids;
					}
					return true;
				} else {
					return false;
				}
		    }
    }
    
    var newEmployee = new NewEmployee();
    Ext.getCmp('tabPanel').newEmployee = newEmployee;
    Ext.getCmp('tabPanel').PD002_MSG = PD002_MSG
});
