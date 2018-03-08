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
	
	// 合同形式下拉框
	var contractTypeCbo = new Ext.form.CmbHRCode({
		id : 'contractType',
		width : 85,
		type : '劳动合同形式'
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
  				'合同有效期:', contractTermCbo, '-',
  				'合同形式:', contractTypeCbo
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
    }, {
    	// 是否执行
    	name : 'ifExecute'
    }]);
    
    var gridStore = new Ext.data.JsonStore({
        url : 'hr/getContractQueryEmployee.action',
        root : 'list',
        totalProperty : 'totalCount',
        fields : gridData
  	});
  	
  	// 一览grid
  	var grid = new Ext.grid.GridPanel({
  		autoWidth : true,
  		title : '劳动合同台帐',
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
  				align:'center',
  				width : 80,
                sortable : true,
                dataIndex : 'chsName',
                renderer : checkName
  			}, {
  				header : '合同编码',
  				width : 100,
  				align:'center',
                sortable : true,
                dataIndex : 'workContractNo',
                renderer : showColor
  			}, {
  				header : '甲方部门',
  				width : 100,
                sortable : true,
                align:'center',
                dataIndex : 'deptNameFirst',
                renderer : showColor
  			}, {
  				header : '甲方地址',
  				align:'center',
  				width : 100,
                sortable : true,
                dataIndex : 'firstAddress',
                renderer : showColor
  			}, {
  				header : '所属部门',
  				align:'center',
  				width : 100,
                sortable : true,
                dataIndex : 'deptNameSecond',
                renderer : showColor
  			}, {
  				header : '岗位名称',
  				align:'center',
  				width : 100,
                sortable : true,
                dataIndex : 'stationName',
                renderer : showColor
  			}, {
  				header : '合同有效期',
  				align:'center',
  				width : 80,
                sortable : true,
                dataIndex : 'contractTerm',
                renderer : showColor
  			}, {
  				header : '合同形式',
  				align:'center',
  				width : 80,
                sortable : true,
                dataIndex : 'contractContinueMark',
                renderer : showColor
  			}, {
  				header : '签字日期',
  				align:'center',
  				width : 80,
                sortable : true,
                dataIndex : 'workSignDate',
                renderer : showColor
  			}, {
  				header : '开始日期',
  				width : 80,
  				align:'center',
                sortable : true,
                dataIndex : 'startDate',
                renderer : showColor
  			}, {
  				header : '结束日期',
  				align:'center',
  				width : 80,
                sortable : true,
                dataIndex : 'endDate',
                renderer : showColor
  			}, {
  				header : '试用期开始',
  				align:'center',
  				width : 80,
                sortable : true,
                dataIndex : 'tryoutStartDate',
                renderer : showColor
  			}, {
  				header : '试用期终止',
  				align:'center',
  				width : 80,
                sortable : true,
                dataIndex : 'tryoutEndDate',
                renderer : showColor
  			}, {
  				header : '附件',
  				width : 80,
  				align:'center',
                sortable : true,
                dataIndex : 'workContractIdInvoice',
                renderer : invoice
  			}, {
  				header : '备注',
  				align:'center',
  				width : 150,
                sortable : true,
                dataIndex : 'memoContract',
                renderer : showColor
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
			contractTerm : contractTermCbo.getValue(),
			contractType : contractTypeCbo.getValue()
	  	};
	  	// 保存查询条件
	  	contractQuery.startDate = startDate.getValue();
	  	contractQuery.endDate = endDate.getValue();
	  	contractQuery.deptCode = Ext.get("deptCode").dom.value;
	  	contractQuery.contractTerm = contractTermCbo.getValue();
	  	contractQuery.contractType = contractTypeCbo.getValue();
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
    	window.open("/powerrpt/report/webfile/hr/PD021_emploeeyContract.jsp?startDate=" + contractQuery.startDate
    			+ "&endDate=" + contractQuery.endDate
    			+ "&deptCode=" + contractQuery.deptCode
    			+ "&contractTerm=" + contractQuery.contractTerm
    			+ "&contractType=" + contractQuery.contractType
    			+ "&duetoTime="
    			+"&enterpriseCode=hfdc"
    			+ "&isDueOrNot=false");
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
	    			+ "&contractType=" + contractQuery.contractType
	    			+ "&flag=" + contractQuery.EXPORT_EMPLOYEE;
				document.all.blankFrame.src = url;
			}
		});
    }
    
    /**
     * 判断姓名和上一行是否相同
     */
    function checkName(value, cellmeta, record, rowIndex, columnIndex, store) {
    	if(rowIndex == 0) {
    		return showColor(value, cellmeta, record);	
    	} else {
    		var recordFirst = store.getAt(rowIndex - 1);
    		if(recordFirst.get("empId") == record.get("empId")) {
    			return "";
    		} else {
    			return showColor(value, cellmeta, record);
    		}
    	}
    }
    
    /**
     * 根据是否到期设置颜色
     */
    function showColor(value, cellmeta, record) {
    	if(value == null) {
			value = "";
		}
    	var nowDate = new Date().format('Y-m-d');
    	if((nowDate > record.get('endDate')) && (record.get('ifExecute') == '1')) {
    		return "<font color='red'>" + escapeHTML(value) + "</font>";
    	} else {
    		return escapeHTML(value);
    	}
    }
    
    /**
     * 查看附件
     */
    function invoice(value, cellmeta, record) {
    	var nowDate = new Date().format('Y-m-d');
    	if(value != "" && value != null) {
    		if((nowDate > record.get('endDate')) && (record.get('ifExecute') == '1')) {
    			return "<a href='#' style='color:red;' onclick='checkFile();return false;'>查看附件</a>";
    		} else {
    			return "<a href='#' onclick='checkFile();return false;'>查看附件</a>";
    		}
    	} else {
    		if((nowDate > record.get('endDate')) && (record.get('ifExecute') == '1')) {
    			return "<font color='red'>－</font>";
    		} else {
    			return "－";
    		}
    	}
    }
    queryHandler();//进入页面进行一次查询
});