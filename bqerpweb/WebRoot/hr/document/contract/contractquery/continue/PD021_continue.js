Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function(){
	
	// 自定义函数    
    var contractQuery = parent.Ext.getCmp('tabPanel').contractQuery;
    
    /**
	 * 查看附件
	 */
	checkFile = function() {
		var selected = grid.selModel.getSelections();
		var record = selected[0];
		// 参数
		var args = new Object();
		// 劳动合同附件ID
		var contractId = record.get('workContractIdInvoice');
		// 附件来源
		var fileOriger = record.get('fileOriger');
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
    
    // 所属部门
    var deptTxt = new Ext.form.TextField({
    	id : 'deptTxt',
    	width : 100,
    	allowBlank : true,
    	readOnly : true
    });
    
    // 所属部门Code
    var hiddenDeptTxt = {
		id : 'deptCode',
		xtype : 'hidden',
		value : '',
		readOnly : true,
		hidden : true
	};
    
    // 选择部门
    deptTxt.onClick(deptSelect);
    
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
		width : 100,
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
		items : ['签字日期:', startDate, '~', endDate, '-',
  				'所属部门:', deptTxt, hiddenDeptTxt, '-',
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
    	// 劳动合同签定ID
    	name : 'workContractIdContract'
    }, {
    	// 合同编号
    	name : 'workContractNo'
    }, {
    	// 甲方部门
    	name : 'deptNameFirst'
    }, {
    	// 甲方地址
    	name : 'firstAddress'
    }, {
    	// 所属部门
    	name : 'deptNameSecond'
    }, {
    	// 岗位名称
    	name : 'stationName'
    }, {
    	// 合同有效期
    	name : 'contractTerm'
    }, {
    	// 签字日期
    	name : 'workSignDate'
    }, {
    	// 开始日期
    	name : 'startDate'
    }, {
    	// 结束日期
    	name : 'endDate'
    }, {
    	// 是否执行
    	name : 'ifExecute'
    }, {
    	// 附件
    	name : 'workContractIdInvoice'
    }, {
    	// 备注
    	name : 'memoContract'
    }, {
    	// 附件来源
    	name : 'fileOriger'
    }]);
    
    var gridStore = new Ext.data.JsonStore({
        url : 'hr/getContractQueryContinue.action',
        root : 'list',
        totalProperty : 'totalCount',
        fields : gridData
  	});
  	
  	// 一览grid
  	var grid = new Ext.grid.GridPanel({
  		autoWidth : true,
  		title : '续签合同查询',
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
  				header : '合同编码',
  				width : 100,
                sortable : true,
                dataIndex : 'workContractNo'
  			}, {
  				header : '甲方部门',
  				width : 100,
                sortable : true,
                dataIndex : 'deptNameFirst'
  			}, {
  				header : '甲方地址',
  				width : 100,
                sortable : true,
                dataIndex : 'firstAddress'
  			}, {
  				header : '所属部门',
  				width : 100,
                sortable : true,
                dataIndex : 'deptNameSecond'
  			}, {
  				header : '岗位名称',
  				width : 100,
                sortable : true,
                dataIndex : 'stationName'
  			}, {
  				header : '合同有效期',
  				width : 80,
                sortable : true,
                dataIndex : 'contractTerm'
  			}, {
  				header : '签字日期',
  				width : 80,
                sortable : true,
                dataIndex : 'workSignDate'
  			}, {
  				header : '开始日期',
  				width : 80,
                sortable : true,
                dataIndex : 'startDate'
  			}, {
  				header : '结束日期',
  				width : 80,
                sortable : true,
                dataIndex : 'endDate'
  			}, {
  				header : '是否执行',
  				width : 80,
                sortable : true,
                dataIndex : 'ifExecute'
  			}, {
  				header : '附件',
  				width : 80,
                sortable : true,
                dataIndex : 'workContractIdInvoice',
                renderer : contractQuery.invoice
  			}, {
  				header : '备注',
  				width : 150,
                sortable : true,
                dataIndex : 'memoContract'
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
        if("memoContract" == fieldName) {
          contractQuery.showWin(record.get("memoContract"));
        }
	}
    
    /**
     * 选择部门
     */
    function deptSelect() {
    	if(contractQuery.deptSelect()) {
	    	// 根据返回值设置画面的值
	    	deptTxt.setValue(contractQuery.name);
	    	Ext.get("deptCode").dom.value = contractQuery.id;
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
			deptCode : Ext.get("deptCode").dom.value,
			contractTerm : contractTermCbo.getValue()
	  	};
	  	// 保存查询条件
	  	contractQuery.startDate = startDate.getValue();
	  	contractQuery.endDate = endDate.getValue();
	  	contractQuery.deptCode = Ext.get("deptCode").dom.value;
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
    	window.open("../../../../../report/webfile/hr/PD021_continueContract.jsp?startDate=" + contractQuery.startDate
    			+ "&endDate=" + contractQuery.endDate
    			+ "&deptCode=" + contractQuery.deptCode
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
    			+ "&deptCode=" + contractQuery.deptCode
    			+ "&contractTerm=" + contractQuery.contractTerm
	    			+ "&flag=" + contractQuery.EXPORT_CONTINUE;
				document.all.blankFrame.src = url;
			}
		});
    }
    queryHandler();//add by wpzhu 
});