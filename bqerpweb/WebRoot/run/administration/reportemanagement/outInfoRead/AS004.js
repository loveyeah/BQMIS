Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function(){
	
	/**
	 * 下载文件
	 */
	 download = function(id) {
		document.all.blankFrame.src = "administration/downloadOutQueryFile.action?id=" + id;
	}
	
	// 阅读状态下拉框
    var readStatusCbo = new Ext.form.CmbReadStatus({
    	id : 'readStatusCbo',
    	width : 120
    });
    
    // 一个值被选中时，重新检索数据
    readStatusCbo.on('select', queryHandler);
    
    // 初始化时设置未阅读
    readStatusCbo.setValue("N");
    
    // 查阅按钮
    var readBtn = new Ext.Button({
    	text : '查阅',
    	handler : readHandler
    });
    
    // 定义grid中的数据
    var gridData = new Ext.data.Record.create([{
    	// 申请单号
    	name : 'applyId'
    }, {
    	// ID
    	name : 'id'
    }, {
    	// 修改时间
    	name : 'updateTime'
    }, {
    	// 呈报日期
    	name : 'applyDate'
    }, {
    	// 部门名称
    	name : 'depName'
    }, {
    	// 姓名
    	name : 'name'
    }]);
    
    var gridStore = new Ext.data.JsonStore({
        url : 'administration/getOutInfoRead.action',
        root : 'list',
        totalProperty : 'totalCount',
        sortInfo : {field: "applyId", direction: "ASC"},
        fields : gridData
  	});
  	    
    // 加载数据
  	queryHandler();
  	
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
  				header : '申报人',
  				width : 175,
                sortable : true,
                dataIndex : 'name'
  			}, {
  				header : '呈报部门',
  				width : 150,
                sortable : true,
                dataIndex : 'depName'
  			}, {
  				header : '呈报日期',
  				width : 150,
                sortable : true,
                dataIndex : 'applyDate'
  			}],
  		tbar : ['阅读状态:', readStatusCbo, '-', readBtn],
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
  	grid.on("rowdblclick", readHandler);
  	
  	// 设定布局器及面板
	new Ext.Viewport({   
		enableTabScroll : true,
        layout : "border",
        items : [grid]
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
        url : 'administration/getOutQueryFileRead.action',
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
  		region : 'center',
  		height : 150,
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
        frame : true,
        border : false,
        enableColumnHide : true,
        enableColumnMove : false
  	});
  	
	// 帐票预览
  	var report = new Ext.BoxComponent({
  		region : 'north',
        el: 'popFrame',
        height : 370,
        width : 500
    });
    
    var popPanel = new Ext.Panel({
    	layout : 'border',
    	height : 800,
    	width : 650,
    	autoScroll : true,
    	items : [report, popupGrid]
    });
    
    // 弹出窗口
  	var win = new Ext.Window({
		width : 500,
		height : 330,
		modal : true,
		title : '签报详细查阅',
		buttonAlign : 'center',
		items : [popPanel],
		layout : 'fit',
		closeAction : 'hide',
        resizable : false,
		buttons : [{
			text : '此签报内容已经阅读',
			id : 'readed',
            buttonAlign : 'center',
			handler : hadReadHandler
		}, {
			text : Constants.BTN_CANCEL,
			iconCls : Constants.CLS_CANCEL,
            buttonAlign : 'center',
			handler : function() {
				Ext.Msg.confirm(Constants.NOTICE_CONFIRM, MessageConstants.COM_C_005, function(
					buttonobj) {
					// 确认关闭弹出窗口
					if (buttonobj == "yes") {
						win.hide();
					}});
			}
		}]
	});
    //*********** 弹出窗口 结束 **********//
	
	/**
	 * 检索数据
	 */
	function queryHandler() {
		// 加载数据
	  	gridStore.baseParams = {
	  		status : readStatusCbo.getValue()
	  	};
	  	gridStore.load({
			params : {
				start : 0,
				limit : Constants.PAGE_SIZE
			}
	  	});
	}
	
    /**
     * 查阅处理
     */
    function readHandler() {
    	var selected = grid.selModel.getSelections();
    	// 如果没有选中行
		if (selected.length < 1) {
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG, MessageConstants.COM_I_001);
		} else {
			document.all.popFrame.src = "report/webfile/signReport.jsp?status="
				+ readStatusCbo.getValue()
				+"&applyId=" + selected[0].data.applyId;
			// 显示窗口
			win.show();
			if(readStatusCbo.getValue() == "Y") {
				Ext.get("readed").dom.disabled = true;
			} else {
				Ext.get("readed").dom.disabled = false;
			}
			// 查询
			popupGridStore.baseParams = {
				applyId : selected[0].data.applyId
		  	};
			popupGridStore.load();
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
     * 已经阅读处理
     */
    function hadReadHandler() {
    	var selected = grid.selModel.getSelections();
    	Ext.lib.Ajax.request(Constants.POST,
			'administration/updateOutInfoRead.action', {
			success : function(result, request) {
            	if (result.responseText) {
                    var o = eval("(" + result.responseText + ")");
                    var succ = o.msg;
                    if(succ == Constants.SQL_FAILURE){
                    	Ext.Msg.alert(Constants.ERROR,
                        	MessageConstants.COM_E_014); 
                    } else if(succ == Constants.DATA_USING) {
                    	Ext.Msg.alert(MessageConstants.ERROR,
                        	MessageConstants.COM_I_002);
                    } else {
                    	Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
                        	MessageConstants.COM_I_004,
                        	function() {
	                        	// 清空数据
				                popupGridStore.removeAll();
				                win.hide();
								// 重新检索数据
								queryHandler();
                        	}); 
                    }
                } 
			},
			failure : function() {
				Ext.Msg.alert(Constants.ERROR,
                	MessageConstants.UNKNOWN_ERR); 
			}
		}, 'id=' + selected[0].data.id
			+ '&updateTime=' + selected[0].data.updateTime);
    }
});