Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function(){
	
	/**
	 * 显示签报附件窗口
	 */
    showWindow = function(applyId) {
		// 显示窗口
		win.show();
		// 查询
		popupGridStore.baseParams = {
			applyId : applyId
	  	};
		popupGridStore.load({
			params : {
				start : 0,
				limit : Constants.PAGE_SIZE
			},
			callback : function() {
				if(popupGridStore.getCount() == 0) {
					Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG, MessageConstants.COM_I_003);
				}
			}
		});
	}
	
	/**
	 * 下载文件
	 */
	 download = function(id) {
		document.all.blankFrame.src = "administration/downloadOutQueryFile.action?id=" + id;
	}
	
    // 系统当天前15天的日期
    var sd = new Date();
    sd.setDate(sd.getDate() - 15);
    sd = sd.format('Y-m-d');
    
    // 系统当天的日期
    var ed = new Date();
    ed = ed.format('Y-m-d');
    
    // 查询起始时间
	var startDate = new Ext.form.TextField({
        id : 'startDate',
        style : 'cursor:pointer',
        value : sd,
        readOnly:true,
        width : 100,
        listeners : {
        	focus : function() {
                WdatePicker({
                    startDate : '%y-%M-%d',
                    dateFmt : 'yyyy-MM-dd',
                    isShowClear : false,
                    alwaysUseStartDate : true
                });
            }
        }
    });
    
 	// 查询结束时间
    var endDate = new Ext.form.TextField({
        id : 'endDate',
        style : 'cursor:pointer',
        value : ed,
        readOnly:true,
        width : 100,
        listeners : {
        	focus : function() {
                WdatePicker({
                    startDate : '%y-%M-%d',
                    dateFmt : 'yyyy-MM-dd',
                    isShowClear : false,
                    alwaysUseStartDate : true
                });
            }
        }
    });

    // 呈报部门
    var reortDeptTxt = new Ext.form.TextField({
    	readOnly : true,
    	width : 120
    });
    
    // 呈报部门code
    var hiddenReortDeptTxt = {
		id : 'deptCode',
		xtype : 'hidden',
		readOnly : true,
		hidden : true
	};
	
	// 呈报部门单击事件
	reortDeptTxt.onClick(reportDeptTxtHandler);
    
    // 申请人
	var applierCmb = new Ext.form.CmbWorkerByDept({
		width : 80
	});
	
    // 单据状态
    var reportStatusCmb = new Ext.form.CmbReportStatus({
    	width : 80
    });
    
    // 查询按钮
    var queryBtn = new Ext.Button({
    	text : Constants.BTN_QUERY,
    	iconCls : Constants.CLS_QUERY,
    	handler : queryHandler
    });
    
    // 打印签报单按钮
    var printBtn = new Ext.Button({
    	text : '打印签报单',
    	iconCls : Constants.CLS_PRINT,
    	handler : printHandler
    });
    
    // 查询条件
	var headTbar = new Ext.Toolbar({
		region : 'north',
		border:false,
		height:25,
		items:['呈报日期:从', startDate, '到', endDate, '-',
  				'呈报部门:', reortDeptTxt, hiddenReortDeptTxt, '-',
  				'申报人:', applierCmb, '-',
  				'上报状态:', reportStatusCmb
  		]
	});
    
    // 定义grid中的数据
    var gridData = new Ext.data.Record.create([{
    	// 申请单号
    	name : 'applyId'
    }, {
    	// 申请人
    	name : 'applyMan'
    }, {
    	// 呈报日期
    	name : 'applyDate'
    }, {
    	// 呈报主题
    	name : 'applyTopic'
    }, {
    	// 呈报内容
    	name : 'applyText'
    }, {
    	// 核稿人
    	name : 'checkedMan'
    }, {
    	// 签字状态
    	name : 'dcmStatus'
    }, {
    	// 签报种类
    	name : 'appType'
    }, {
    	// 编号
    	name : 'reportId'
    }, {
    	// 部门经理姓名
    	name : 'depBossCode'
    }, {
    	// 部门经理意见
    	name : 'depIdea'
    }, {
    	// 部门经理签字时间
    	name : 'depSignDate'
    }, {
    	// 行政部经理姓名
    	name : 'xzBossCode'
    }, {
    	// 行政部经理意见
    	name : 'xzIdea'
    }, {
    	// 行政部经理签字时间
    	name : 'xzSignDate'
    }, {
    	// 总经理姓名
    	name : 'bigBossCode'
    }, {
    	// 总经理意见
    	name : 'bigIdea'
    }, {
    	// 总经理签字时间
    	name : 'bigSignDate'
    }, {
    	// 部门名称
    	name : 'depName'
    }, {
    	// 姓名
    	name : 'name'
    }, {
    	// 附件条数
    	name : 'fileCount'
    }]);
    
    var gridStore = new Ext.data.JsonStore({
        url : 'administration/getOutQueryApply.action',
        root : 'list',
        totalProperty : 'totalCount',
        sortInfo : {field: "applyId", direction: "ASC"},
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
  				header : '申请单号',
  				width : 100,
                sortable : true,
                dataIndex : 'applyId'
  			}, {
  				header : '申请人',
  				width : 100,
                sortable : true,
                dataIndex : 'name'
  			}, {
  				header : '呈报部门',
  				width : 100,
                sortable : true,
                dataIndex : 'depName'
  			}, {
  				header : '呈报日期',
  				width : 100,
                sortable : true,
                dataIndex : 'applyDate'
  			}, {
  				header : '呈报主题',
  				width : 150,
                sortable : true,
                dataIndex : 'applyTopic'
  			}, {
  				header : '呈报内容',
  				width : 200,
                sortable : true,
                dataIndex : 'applyText'
  			}, {
  				header : '上报状态',
  				width : 70,
                sortable : true,
                dataIndex : 'dcmStatus'
  			}, {
  				header : '签报种类',
  				width : 70,
                sortable : true,
                dataIndex : 'appType'
  			}, {
  				header : '查看附件',
  				width : 70,
                sortable : true,
                dataIndex : 'fileCount',
                renderer : fileCount
  			}],
  		tbar : [queryBtn, printBtn],
  		bbar : new Ext.PagingToolbar({
            pageSize : Constants.PAGE_SIZE,
            store : gridStore,
            displayInfo : true,
            displayMsg : MessageConstants.DISPLAY_MSG,
            emptyMsg : MessageConstants.EMPTY_MSG
        }),
        frame : false,
        border : false,
        enableColumnHide : true,
        enableColumnMove : false
  	});
  	
  	// 行双击事件
  	grid.on("rowdblclick", printHandler);
  	
  	// 设定布局器及面板
	new Ext.Viewport({   
		enableTabScroll : true,
        layout : "border",
        items : [headTbar, grid]
    });
    
    //*********** 弹出窗口 开始 **********//
    // 定义grid中的数据
    var popupGridData = new Ext.data.Record.create([{
    	// 附件ID
    	name : 'id'
    }, {
    	// 附件名称
    	name : 'fileName'
    }]);
    
    var popupGridStore = new Ext.data.JsonStore({
        url : 'administration/getOutQueryFile.action',
        root : 'list',
        totalProperty : 'totalCount',
        sortInfo : {field: "id", direction: "ASC"},
        fields : popupGridData
  	});
  	
  	// 一览grid
  	var popupGrid = new Ext.grid.GridPanel({
    	id : 'popupGrid',
  		autoWidth : true,
  		store : popupGridStore,
  		region: 'center',
  		sm : new Ext.grid.RowSelectionModel({
  			// 单选
  			singleSelect : true
  		}),
  		columns : [
  			new Ext.grid.RowNumberer({
  				header : '行号',
  				width : 50
  			}), {
  				header : '附件名',
  				width : 150,
                sortable : true,
                dataIndex : 'fileName',
                renderer : fileName
  			}],
  		bbar : new Ext.PagingToolbar({
            pageSize : Constants.PAGE_SIZE,
            store : popupGridStore,
            displayInfo : true,
            displayMsg : MessageConstants.DISPLAY_MSG,
            emptyMsg : MessageConstants.EMPTY_MSG
        }),
        frame : false,
        border : false,
        enableColumnHide : true,
        enableColumnMove : false
  	});
  	
  	// 弹出窗口
  	var win = new Ext.Window({
  		id : 'win',
		width : 500,
		height : 300,
		modal : true,
		title : '签报附件',
		buttonAlign : 'center',
		items : [popupGrid],
		layout : 'border',
		closeAction : 'hide',
        resizable : false,
		buttons : [{
			text : Constants.BTN_CLOSE,
			iconCls : Constants.CLS_CANCEL,
            buttonAlign : 'center',
			handler : function() {
				Ext.Msg.confirm(Constants.NOTICE_CONFIRM, "确认关闭此窗口吗？", function(
					buttonobj) {
					// 确认关闭弹出窗口
					if (buttonobj == "yes") {
						popupGridStore.removeAll();
						win.hide();
					}});
			}
		}]
	});
    
    //*********** 弹出窗口 结束 **********//

    /**
     * 查看附件
     */
    function fileCount(value, cellmeta, record) {
		if(value == '0') {
			return "<U>查看附件</U>";
		} else {
			var applyId = record.get("applyId");
			var showWindow = 'showWindow("' + applyId + '");return false;';
			return "<a href='#' onclick='"+ showWindow+ "'>查看附件</a>";
		}
    }
    
    /**
     * 附件名
     */
    function fileName(value, cellmeta, record) {
    	if(value != "") {
    		var id = record.get("id");
    		var download = 'download("' + id + '");return false;';
    		return "<a href='#' onclick='"+ download+ "'>" + value + "</a>";
    	} else {
    		return "";
    	}
    }
    
    /**
     * 选择呈报部门
     */
    function reportDeptTxtHandler() {
    	var args = {selectModel:'single',rootNode:{id:'0',text:'合肥电厂'},onlyLeaf:true};
    	// 调用画面
		var object = window.showModalDialog(
						'../../../../comm/jsp/hr/dept/dept.jsp',
						args,
						'dialogWidth=800px;dialogHeight=520px;center=yes;help=no;resizable=no;status=no;');
		// 根据返回值设置画面的值
		if (object) {
			var oldValue = Ext.get("deptCode").dom.value;
			reortDeptTxt.setValue(object.names);
			Ext.get("deptCode").dom.value = object.codes;
			// 当值发生改变时
			if(oldValue != object.codes) {
				applierCmb.setValue("");
				applierCmb.store.removeAll();
				applierCmb.store.load({
					params : {
						strDeptCode : object.codes
					}
				});
			}
		}
    }
    
    /**
     * 检查开始时间是否小于结束时间
     */
    function checkDate() {
    	var startOld = startDate.getValue();
    	var endOld = endDate.getValue();
    	if(startOld == "" || endOld == "") {
    		return true;
    	} else {
	    	var start = Date.parseDate(startOld, 'Y-m-d');
	    	var end = Date.parseDate(endOld, 'Y-m-d');
	    	if(start.getTime() > end.getTime()) {
	    		Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, String.format(MessageConstants.COM_E_009, '开始日期', '结束日期'));
	    		return false;
	    	}
	    	return true;
    	}
    }
    
    /**
     * 查询处理
     */
    function queryHandler() {
    	if(checkDate()) {
    		var start = startDate.getValue();
    		var end = endDate.getValue();
    		if(start != "") {
    			start = start.replace('-', '/').replace('-', '/');
    		}
    		if(end != "") {
    			end = end.replace('-', '/').replace('-', '/');
    		}
	    	gridStore.baseParams = {
		  		startDate : start,
		  		endDate : end,
		  		deptCode : Ext.get("deptCode").dom.value,
		  		applierCode : applierCmb.getValue(),
		  		reportStatus : reportStatusCmb.getValue()
		  	};
	    	gridStore.load({
	    		params : {
					start : 0,
					limit : Constants.PAGE_SIZE
				},
				callback : function() {
					if(gridStore.getCount() == 0) {
						Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG, MessageConstants.COM_I_003);
					}
				}
	    	});
    	}
    }
    
    /**
     * 检查grid中是否有选中行
     */
    function checkGrid() {
    	var selected = grid.getSelectionModel().getSelections();
    	// 如果没有选中行
		if (selected.length < 1) {
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG, MessageConstants.COM_I_001);
			return false;
		}
		return true;
    }
    
    /**
     * 打印处理
     */
    function printHandler() {
    	if(checkGrid()) {    
            // xsTan 追加开始 2009-2-4 追加打印功能
            var record = grid.getSelectionModel().getSelected();
            var applyId = record.get('applyId');
            window.open("/power/report/webfile/administration/ARF08.jsp?applyId=" +applyId);
            // xsTan 追加结束 2009-2-4 追加打印功能
    	}
    }
});