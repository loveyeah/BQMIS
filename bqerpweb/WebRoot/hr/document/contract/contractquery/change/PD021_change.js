Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function(){
	
	// 自定义函数    
    var contractQuery = parent.Ext.getCmp('tabPanel').contractQuery;
	// 标识
	var flag = "";
	
	/**
	 * 查看附件
	 */
	checkFile = function(fileFlag) {
		var selected = grid.selModel.getSelections();
		var record = selected[0];
		// 参数
		var args = new Object();
		// 劳动合同附件ID
		var contractId;
		// 附件来源
		var fileOriger;
		if(fileFlag == "B") {
			contractId = record.get("workContractIdInvoiceBefore");
			fileOriger = record.get("fileOrigerBefore");
		} else if(fileFlag == "A"){
			contractId = record.get("workContractIdInvoiceAfter");
			fileOriger = record.get("fileOrigerAfter");
		}
		// 模式(查看)
		var mode = "read";
		// 查看附件
		contractQuery.checkFile(contractId, fileOriger, mode);
	}
	
	// 开始时间选择
    var startDate = new Ext.form.TextField({
        id : 'startDate',
        style : 'cursor:pointer',
        readOnly : true,
        width : 85
    });
    
    startDate.onClick( function() {
        WdatePicker({
            startDate : '%y-%M-%d',
            dateFmt : 'yyyy-MM-dd'
        })
    });
    
    // 结束时间选择
    var endDate = new Ext.form.TextField({
        id : 'endDate',
        style : 'cursor:pointer',
        readOnly : true,
        width : 85
    });
    
    endDate.onClick( function() {
        WdatePicker({
            startDate : '%y-%M-%d',
            dateFmt : 'yyyy-MM-dd'
        })
    });
    
    // 变更前部门
    var deptBeforeTxt = new Ext.form.TextField({
    	id : 'deptBeforeTxt',
    	width : 100,
    	allowBlank : true,
    	readOnly : true
    });
    
    // 变更前部门Code
    var hiddenDeptBeforeTxt = {
		id : 'deptBeforeCode',
		xtype : 'hidden',
		value : '',
		readOnly : true,
		hidden : true
	};
	
	// 变更后部门
    var deptAfterTxt = new Ext.form.TextField({
    	id : 'deptAfterTxt',
    	width : 100,
    	allowBlank : true,
    	readOnly : true
    });
    
    // 变更后部门Code
    var hiddenDeptAfterTxt = {
		id : 'deptAfterCode',
		xtype : 'hidden',
		value : '',
		readOnly : true,
		hidden : true
	};
    
    // 选择部门
    deptBeforeTxt.onClick(function() {
    	flag = "B";
    	deptSelect();
    });
    
    deptAfterTxt.onClick(function() {
    	flag = "A";
    	deptSelect();
    });
    
    // 合同有效期数据
	var contractTermData = Ext.data.Record.create([{
        // 劳动合同有效期
        name: 'contractTerm'
    }, {
        // 劳动合同有效期ID
        name: 'contractTermId'}
    ]);
    
    // 合同有效期
	var contractTermStore = new Ext.data.JsonStore({
		url : 'hr/getContractTermQuery.action',
		root : 'list',				
		fields : contractTermData	
	});
	
	// 合同有效期下拉框
	var contractTermCbo = new Ext.form.ComboBox({  
		id : 'contractTermCbo',
		width : 90,
		allowBlank : true,
        triggerAction : 'all',
        store : contractTermStore,
        displayField : 'contractTerm',
        valueField : 'contractTermId',
        mode : 'local',
        readOnly : true
	});
	
	// 加载数据
	contractTermStore.load({
		callback : function() {
			var record = new contractTermData({
				contractTerm : '',
				contractTermId : ''
			});
			contractTermStore.insert(0, record);
		}
	});
	
	// 查询按钮
    var queryBtn = new Ext.Button({
    	text : Constants.BTN_QUERY,
    	iconCls : Constants.CLS_QUERY,
    	handler : queryHandler
    });
    
    // 打印按钮
    var printBtn = new Ext.Button({
    	text : Constants.BTN_PRINT,
    	iconCls : Constants.CLS_PRINT,
    	disabled : true,
    	handler : printHandler
    });
    
    // 导出按钮
    var exportBtn = new Ext.Button({
    	text : Constants.BTN_EXPORT,
    	iconCls : Constants.CLS_EXPORT,
    	disabled : true,
    	handler : exportHandler
    });
    
    // 查询条件Toolbar
	var queryTbar = new Ext.Toolbar({
		region : 'north',
		border : true,
		height : 25,
		items : ['变更日期:', startDate, '~', endDate, '-',
  				'变更前部门:', deptBeforeTxt, hiddenDeptBeforeTxt,
  				'变更后部门:', deptAfterTxt, hiddenDeptAfterTxt, '-',
  				'合同有效期:', contractTermCbo
  		]
	});
	
	// 按钮Toolbar
	var btnTbar = new Ext.Toolbar({
		region : 'center',
		border : true,
		height : 25,
		items : [queryBtn, printBtn, exportBtn]
	});
	
	// panel
	var panelTbar = new Ext.Panel({
		region : 'north',
		border : false,
		height : 51,
		layout : 'border',
		items : [queryTbar, btnTbar]
	});
    
	// 定义grid中的数据
    var gridData = new Ext.data.Record.create([{
    	// 员工姓名
    	name : 'chsName'
    }, {
    	// 员工ID
    	name : 'empId'
    }, {
    	// 劳动合同变更ID
    	name : 'contractChangedId'
    }, {
    	// 变更日期
    	name : 'changeDate'
    }, {
    	// 变更前合同期限
    	name : 'contractTermBefore'
    }, {
    	// 变更后合同期限
    	name : 'contractTermAfter'
    }, {
    	// 变更前起始日期
    	name : 'oldStartTime'
    }, {
    	// 变更后起始日期
    	name : 'newStartTime'
    }, {
    	// 变更前终止日期
    	name : 'oldEndTime'
    }, {
    	// 变更后终止日期
    	name : 'newEndTime'
    }, {
    	// 变更前部门
    	name : 'deptNameFirst'
    }, {
    	// 变更后部门
    	name : 'deptNameSecond'
    }, {
    	// 变更前岗位
    	name : 'stationNameBefore'
    }, {
    	// 变更后岗位
    	name : 'stationNameAfter'
    }, {
    	// 变更前附件
    	name : 'workContractIdInvoiceBefore'
    }, {
    	// 变更后附件
    	name : 'workContractIdInvoiceAfter'
    }, {
    	// 变更原因
    	name : 'changeReason'
    }, {
    	// 备注
    	name : 'memoChange'
    }, {
    	// 变更前附件来源
    	name : 'fileOrigerBefore'
    }, {
    	// 变更后附件来源
    	name : 'fileOrigerAfter'
    }]);
    
    var gridStore = new Ext.data.JsonStore({
        url : 'hr/getContractQueryChange.action',
        root : 'list',
        totalProperty : 'totalCount',
        fields : gridData
  	});
  	
  	// 一览grid
  	var grid = new Ext.grid.GridPanel({
  		autoWidth : true,
  		title : '合同变更查询',
  		store : gridStore,
  		region: 'center',
  		sm : new Ext.grid.RowSelectionModel({
  			// 单选
  			singleSelect : true
  		}),
  		columns : [
  			new Ext.grid.RowNumberer({
  				header : '行号',
  				width : 35
  			}), {
  				header : '员工姓名',
  				width : 80,
                sortable : true,
                dataIndex : 'chsName',
                renderer : contractQuery.checkName
  			}, {
  				header : '变更日期',
  				width : 100,
                sortable : true,
                dataIndex : 'changeDate'
  			}, {
  				header : '变更前合同期限',
  				width : 100,
                sortable : true,
                dataIndex : 'contractTermBefore'
  			}, {
  				header : '变更后合同期限',
  				width : 100,
                sortable : true,
                dataIndex : 'contractTermAfter'
  			}, {
  				header : '变更前起始日期',
  				width : 100,
                sortable : true,
                dataIndex : 'oldStartTime'
  			}, {
  				header : '变更后起始日期',
  				width : 100,
                sortable : true,
                dataIndex : 'newStartTime'
  			}, {
  				header : '变更前终止日期',
  				width : 100,
                sortable : true,
                dataIndex : 'oldEndTime'
  			}, {
  				header : '变更后终止日期',
  				width : 100,
                sortable : true,
                dataIndex : 'newEndTime'
  			}, {
  				header : '变更前部门',
  				width : 80,
                sortable : true,
                dataIndex : 'deptNameFirst'
  			}, {
  				header : '变更后部门',
  				width : 80,
                sortable : true,
                dataIndex : 'deptNameSecond'
  			}, {
  				header : '变更前岗位',
  				width : 80,
                sortable : true,
                dataIndex : 'stationNameBefore'
  			}, {
  				header : '变更后岗位',
  				width : 80,
                sortable : true,
                dataIndex : 'stationNameAfter'
  			}, {
  				header : '变更前附件',
  				width : 80,
                sortable : true,
                dataIndex : 'workContractIdInvoiceBefore',
                renderer : invoiceBefore
  			}, {
  				header : '变更后附件',
  				width : 80,
                sortable : true,
                dataIndex : 'workContractIdInvoiceAfter',
                renderer : invoiceAfter
  			}, {
  				header : '变更原因',
  				width : 100,
                sortable : true,
                dataIndex : 'changeReason'
  			}, {
  				header : '备注',
  				width : 150,
                sortable : true,
                dataIndex : 'memoChange'
  			}],
  		bbar : new Ext.PagingToolbar({
            pageSize : Constants.PAGE_SIZE,
            store : gridStore,
            displayInfo : true,
            displayMsg : Constants.DISPLAY_MSG,
            emptyMsg : Constants.EMPTY_MSG
        }),
        frame : false,
        border : false,
        enableColumnHide : true,
        enableColumnMove : false
  	});
  	
  	// 单元格双击事件
  	grid.on('celldblclick', dbClickHandler);
	
  	// 设定布局器及面板
	new Ext.Viewport({   
		enableTabScroll : true,
        layout : "border",
        items : [panelTbar, grid]
    });
    
    /**
     * celldblclick事件处理函数
     */
	function dbClickHandler(grid, rowIndex, columnIndex, e) {
        // 获取当前记录
        var record = gridStore.getAt(rowIndex); 
        // 编辑列的字段名
        var fieldName = grid.getColumnModel().getDataIndex(columnIndex);
        if("memoChange" == fieldName) {
          contractQuery.showWin(record.get("memoChange"));
        }
	}
    
    /**
     * 选择部门
     */
    function deptSelect() {
    	if(contractQuery.deptSelect()) {
			// 根据返回值设置画面的值
			if(flag == "B") {
				deptBeforeTxt.setValue(contractQuery.name);
				Ext.get("deptBeforeCode").dom.value = contractQuery.id;
			} else if(flag == "A") {
				deptAfterTxt.setValue(contractQuery.name);
				Ext.get("deptAfterCode").dom.value = contractQuery.id;
			}
    	}
    }
    
    /**
     * 查询处理
     */
    function queryHandler() {
    	// 查询基本条件
		gridStore.baseParams = {
			startDate : startDate.getValue(),
			endDate : endDate.getValue(),
			deptBeforeCode : Ext.get("deptBeforeCode").dom.value,
			deptAfterCode : Ext.get("deptAfterCode").dom.value,
			contractTerm : contractTermCbo.getValue()
	  	};
	  	// 保存查询条件
	  	contractQuery.startDate = startDate.getValue();
	  	contractQuery.endDate = endDate.getValue();
	  	contractQuery.deptBeforeCode = Ext.get("deptBeforeCode").dom.value;
	  	contractQuery.deptAfterCode = Ext.get("deptAfterCode").dom.value;
	  	contractQuery.contractTerm = contractTermCbo.getValue();
	  	// 查询
		gridStore.load({
			params : {
				start : 0,
				limit : Constants.PAGE_SIZE
			},
			callback : function() {
				if(gridStore.getCount() == 0) {
					printBtn.setDisabled(true);
					exportBtn.setDisabled(true);
					Ext.Msg.alert(Constants.REMIND, Constants.COM_I_003);
				} else {
					printBtn.setDisabled(false);
					exportBtn.setDisabled(false);
				}
			}
		});
    }
    
    /**
     * 打印处理
     */
    function printHandler() {
    	window.open("../../../../../report/webfile/hr/PD021_changeContract.jsp?startDate=" + contractQuery.startDate
    			+ "&endDate=" + contractQuery.endDate
    			+ "&deptBeforeCode=" + contractQuery.deptBeforeCode
    			+ "&deptAfterCode=" + contractQuery.deptAfterCode
    			+ "&contractTerm=" + contractQuery.contractTerm);
    }
    
    /**
     * 导出处理
     */
    function exportHandler() {
    	Ext.Msg.confirm(Constants.NOTICE_CONFIRM, Constants.COM_C_007, function(
			buttonobj) {
			if(buttonobj == "yes") {
				var url = "hr/exportContractExcel.action?startDate=" + contractQuery.startDate
	    			+ "&endDate=" + contractQuery.endDate
	    			+ "&deptBeforeCode=" + contractQuery.deptBeforeCode
	    			+ "&deptAfterCode=" + contractQuery.deptAfterCode
	    			+ "&contractTerm=" + contractQuery.contractTerm
	    			+ "&flag=" + contractQuery.EXPORT_CHANGE;
				document.all.blankFrame.src = url;
			}
		});
    }
    
    /**
     * 查看变更前附件
     */
    function invoiceBefore(value, cellmeta, record) {
    	if(value != "" && value != null) {
    		var checkFile = 'checkFile("' + "B" + '");return false;';
    		return "<a href='#' onclick='"+ checkFile+ "'>查看附件</a>";
    	} else {
    		return "－";
    	}
    }
    
    /**
     * 查看变更后附件
     */
    function invoiceAfter(value, cellmeta, record) {
    	if(value != "" && value != null) {
    		var checkFile = 'checkFile("' + "A" + '");return false;';
    		return "<a href='#' onclick='"+ checkFile+ "'>查看附件</a>";
    	} else {
    		return "－";
    	}
    }
    queryHandler();
});