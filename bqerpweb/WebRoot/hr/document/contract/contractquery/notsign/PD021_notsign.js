Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function(){
	
	// 自定义函数    
    var contractQuery = parent.Ext.getCmp('tabPanel').contractQuery;
	
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
    	width : 120,
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
    
	// 定义grid中的数据
    var gridData = new Ext.data.Record.create([{
    	// 员工姓名
    	name : 'chsName'
    }, {
    	// 员工ID
    	name : 'empId'
    }, {
    	// 员工编号
    	name : 'empCode'
    }, {
    	// 档案编号
    	name : 'archivesId'
    }, {
    	// 所属部门
    	name : 'deptName'
    }, {
    	// 性别
    	name : 'sex'
    }, {
    	// 出生日期
    	name : 'birthday'
    }, {
    	// 参加工作时间
    	name : 'workDate'
    }, {
    	// 进厂日期
    	name : 'missionDate'
    }]);
    
    var gridStore = new Ext.data.JsonStore({
        url : 'hr/getContractQueryNotsign.action',
        root : 'list',
        totalProperty : 'totalCount',
        fields : gridData
  	});
  	
  	// 一览grid
  	var grid = new Ext.grid.GridPanel({
  		autoWidth : true,
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
                dataIndex : 'chsName'
  			}, {
  				header : '员工工号',
  				width : 80,
                sortable : true,
                dataIndex : 'empCode'
  			}, {
  				header : '档案编码',
  				width : 100,
                sortable : true,
                dataIndex : 'archivesId'
  			}, {
  				header : '所属部门',
  				width : 100,
                sortable : true,
                dataIndex : 'deptName'
  			}, {
  				header : '性别',
  				width : 50,
                sortable : true,
                dataIndex : 'sex'
  			}, {
  				header : '出生日期',
  				width : 100,
                sortable : true,
                dataIndex : 'birthday'
  			}, {
  				header : '参加工作日期',
  				width : 100,
                sortable : true,
                dataIndex : 'workDate'
  			}, {
  				header : '进厂日期',
  				width : 100,
                sortable : true,
                dataIndex : 'missionDate'
  			}],
  		tbar : ['进厂日期:', startDate, '~', endDate,
  				'-', '所属部门:', deptTxt, hiddenDeptTxt, '-',
  				queryBtn, printBtn, exportBtn],
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
	
  	// 设定布局器及面板
	new Ext.Viewport({   
		enableTabScroll : true,
        layout : "border",
        items : [grid]
    });
    
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
			deptCode : Ext.get("deptCode").dom.value
	  	};
	  	// 保存查询条件
	  	contractQuery.startDate = startDate.getValue();
	  	contractQuery.endDate = endDate.getValue();
	  	contractQuery.deptCode = Ext.get("deptCode").dom.value;
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
    	window.open("../../../../../report/webfile/hr/PD021_notSignContract.jsp?startDate=" + contractQuery.startDate
    			+ "&endDate=" + contractQuery.endDate
    			+ "&deptCode=" + contractQuery.deptCode);
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
	    			+ "&flag=" + contractQuery.EXPORT_NOTSIGN;
				document.all.blankFrame.src = url;
			}
		});
    }
    queryHandler();
});