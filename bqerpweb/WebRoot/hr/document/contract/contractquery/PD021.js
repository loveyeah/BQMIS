Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
    Ext.QuickTips.init();

    // 总tabpanel
    var tabPanel = new Ext.TabPanel({
        renderTo : document.body,
        activeTab : 0,
        tabPosition : 'bottom',
        id : "tabPanel",
        plain : true,
        defaults : {
            autoScroll : true
        },
        frame : false,
        border : false,
        items : [{
            id : 'tabEmployee',
            title : '员工合同查询',
            html : "<iframe name='tabEmployee' src='hr/document/contract/contractquery/employee/PD021_employee.jsp' style='width:100%;height:100%;border:0px;'></iframe>"
        }, {
            id : 'tabContinue',
            title : '续签合同查询',
            html : "<iframe name='tabContinue' src='hr/document/contract/contractquery/continue/PD021_continue.jsp' style='width:100%;height:100%;border:0px;'></iframe>"
        }, {
            id : 'tabChange',
            title : '合同变更查询',
            html : "<iframe name='tabChange' src='hr/document/contract/contractquery/change/PD021_change.jsp' style='width:100%;height:100%;border:0px;'></iframe>"
        }, {
            id : 'tabStop',
            title : '合同终止查询',
            html : "<iframe name='tabStop' src='hr/document/contract/contractquery/stop/PD021_stop.jsp' style='width:100%;height:100%;border:0px;'></iframe>"
        }, {
            id : 'tabDuedate',
            title : '合同到期查询',
            html : "<iframe name='tabDuedate' src='hr/document/contract/contractquery/duedate/PD021_duedate.jsp' style='width:100%;height:100%;border:0px;'></iframe>"
        }, {
            id : 'tabNotsign',
            title : '未签合同查询',
            html : "<iframe name='tabNotsign' src='hr/document/contract/contractquery/notsign/PD021_notsign.jsp' style='width:100%;height:100%;border:0px;'></iframe>"
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
    
    function ContractQuery() {
    	// 开始日期
    	this.startDate = "";
    	// 结束日期
    	this.endDate = "";
    	// 部门编码
    	this.deptCode = "";
    	// 部门编码
    	this.deptCode = "";
    	// 变更前部门编码
    	this.deptBeforeCode = "";
    	// 变更后部门编码
    	this.deptAfterCode = "";
    	// 合同期限
    	this.contractTerm = "";
    	// 合同形式
    	this.contractType = "";
    	// 合同到期月份
    	this.duetoTime = "";
    	// 终止类别
    	this.stopType = "";
    	// 部门名称
    	this.name = "";
    	// 部门ID
    	this.id = "";
    	
    	// 常量
    	// 员工合同
		this.EXPORT_EMPLOYEE = "EMPLOYEE";
		// 续签合同
		this.EXPORT_CONTINUE = "CONTINUE";
		// 变更合同
		this.EXPORT_CHANGE = "CHANGE";
		// 终止合同
		this.EXPORT_STOP = "STOP";
		// 到期合同
		this.EXPORT_DUEDATE = "DUEDATE";
		// 未签合同
		this.EXPORT_NOTSIGN = "NOTSIGN";
    	
    	// 查看附件
    	ContractQuery.prototype.checkFile =
    		function(contractId, fileOriger, mode) {
    			var args = new Object();
    			args.contractId = contractId;
    			args.fileOriger = fileOriger;
    			args.mode = mode;
    			window.showModalDialog(
					'../../../../hr/common/PC002.jsp',
					args,
					'dialogWidth=506px;dialogHeight=396px;center=yes;help=no;resizable=no;status=no;');
    		}
    		
    	// 判断姓名和上一行是否相同
    	ContractQuery.prototype.checkName =
    		function checkName(value, cellmeta, record, rowIndex, columnIndex, store) {
		    	if(rowIndex == 0) {
		    		return value;	
		    	} else {
		    		var recordFirst = store.getAt(rowIndex - 1);
		    		if(recordFirst.get("empId") == record.get("empId")) {
		    			return "";
		    		} else {
		    			return value;	
		    		}
		    	}
		    }
		    
		// 查看附件
		ContractQuery.prototype.invoice =
			function invoice(value, cellmeta, record) {
		    	if(value != "" && value != null) {
		    		return "<a href='#' onclick='checkFile();return false;'>查看附件</a>";
		    	} else {
		    		return "－";
		    	}
		    }
		    
		// 选择部门
		ContractQuery.prototype.deptSelect =
		    function deptSelect() {
		    	var args = {
		    		selectModel : 'single',
		    		rootNode : {
		    			id : '0',
		    			text : '合肥电厂'
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
		    
		// 弹出窗口，查看备注
		ContractQuery.prototype.showWin =
		    function showWin(value) {
		    // 备注
		    var taShowMemo = new Ext.form.TextArea({
				id : "taShowMemo",
				maxLength : 250,
		    	value : value,
		    	disabled : true
		    });
		    
		    // 弹出画面
			var win = new Ext.Window({
				height : 170,
				width : 350,
				layout : 'fit',
				resizable : false,
				modal  : true,
				closeAction : 'hide',
				items : [taShowMemo],
				buttonAlign : "center",
				title : '详细信息查看窗口',
				buttons : [{
					text : Constants.BTN_CLOSE,
					iconCls : Constants.CLS_CANCEL,
					handler : function() {
						win.hide();
					}
				}]
			});
			win.x=undefined;
			win.y=undefined;
			win.show();
			win.on('show', function() {
				taShowMemo.focus(true, 100);
			});
			win.on('hide', function() {
				taShowMemo.setValue("");
			});
		}
    }
    
    var contractQuery = new ContractQuery();
    Ext.getCmp('tabPanel').contractQuery = contractQuery;
});
