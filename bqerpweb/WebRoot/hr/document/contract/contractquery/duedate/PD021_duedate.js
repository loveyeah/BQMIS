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
	
	// 当前时间
	var date = new Date();
	date = date.format('Ym');
	
	// 合同到期月份
    var duedateDate = new Ext.form.TextField({
        id : 'duedate',
        style : 'cursor:pointer',
        value : date,
        readOnly : true,
        width : 85
    });
    
    duedateDate.onClick( function() {
        WdatePicker({
            startDate : '%y-%M',
            dateFmt : 'yyyyMM',
            isShowClear : false
        })
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
		items : ['合同到期月份<font color="red">*</font>:', duedateDate, '-',
  				queryBtn, printBtn, exportBtn
  		]
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
    	// 合同形式
    	name : 'contractContinueMark'
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
    	// 试用期开始
    	name : 'tryoutStartDate'
    }, {
    	// 试用期终止
    	name : 'tryoutEndDate'
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
        url : 'hr/getContractQueryDuedate.action',
        root : 'list',
        totalProperty : 'totalCount',
        fields : gridData
  	});
  	
  	// 一览grid
  	var grid = new Ext.grid.GridPanel({
  		autoWidth : true,
  		title : '到期劳动合同台帐',
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
  				header : '合同形式',
  				width : 80,
                sortable : true,
                dataIndex : 'contractContinueMark'
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
  				header : '试用期开始',
  				width : 80,
                sortable : true,
                dataIndex : 'tryoutStartDate'
  			}, {
  				header : '试用期终止',
  				width : 80,
                sortable : true,
                dataIndex : 'tryoutEndDate'
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
        items : [queryTbar, grid]
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
     * 查询处理
     */
    function queryHandler() {
    	// 查询基本条件
		gridStore.baseParams = {
			duetoTime : duedateDate.getValue()
	  	};
	  	// 保存查询条件
	  	contractQuery.duetoTime = duedateDate.getValue();
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
    	window.open("../../../../../report/webfile/hr/PD021_dueContract.jsp?startDate="
    			+ "&endDate="
    			+ "&deptCode="
    			+ "&contractTerm="
    			+ "&contractType="
    			+ "&duetoTime=" + contractQuery.duetoTime
    			+ "&isDueOrNot=true");
    }
    
    /**
     * 导出处理
     */
    function exportHandler() {
    	Ext.Msg.confirm(Constants.NOTICE_CONFIRM, Constants.COM_C_007, function(
			buttonobj) {
			if(buttonobj == "yes") {
				var url = "hr/exportContractExcel.action?duetoTime="
    			+ contractQuery.duetoTime
	    		+ "&flag=" + contractQuery.EXPORT_DUEDATE;
				document.all.blankFrame.src = url;
			}
		});
    }
    queryHandler();
});