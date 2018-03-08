Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
    Ext.QuickTips.init();
    
    var materialId = window.dialogArguments.materialId;
    var supplier = window.dialogArguments.supplier;
    // ↑↑**********************报价查询********************↑↑//
    var priceRecord = Ext.data.Record.create([{
	        // 单位
	        name : 'unitId'
	    }, {
	        // 报价数量
	        name : 'quotedQty'
	    }, {
	        // 报价
	        name : 'quotedPrice'
    },{name:'unitName'}]);
	
    // grid列模式
    var priceCM = new Ext.grid.ColumnModel([
    	new Ext.grid.RowNumberer({
			header : '行号',
			width : 35
		}), {
	        header : '单位',
	        width : 100,
	        dataIndex : 'unitName'
	    }, {
	        header : '报价数量',
	        width : 100,
	        dataIndex : 'quotedQty'
	    }, {
	        header : '报价',
	        width : 120,
	        dataIndex : 'quotedPrice'
    }]);
    priceCM.defaultSortable = true;
    // ↓↓**********************需求计划物资Grid************↓↓//
    // 数据源
    var priceStore = new Ext.data.JsonStore({
        url : 'resource/getRegisterUnitPrice.action',
        root : 'list',
        totalProperty : 'totalCount',
        fields : priceRecord
    });
    
    var confirmBtn = new Ext.Button({
    	id : 'btnSubmit',
        text : "确认",
        handler : returnObject
    })
    
    // 分页工具栏
	var pagebar = new Ext.PagingToolbar({
		pageSize : Constants.PAGE_SIZE,
		store : priceStore,
		displayInfo : true,
		displayMsg : '一共 {2} 条',
		emptyMsg : Constants.EMPTY_MSG
	})
    // grid
    var priceGrid = new Ext.grid.GridPanel({
    	region : 'center',
		enableColumnMove : false,
		enableColumnHide : true,
        ds : priceStore,
        cm : priceCM,
        tbar : [confirmBtn],
        bbar : pagebar,
        border : false,
        frame : false,
        autoScroll : true
    });
    priceGrid.on('rowdblclick', returnObject);
    
    // 弹出窗口
    new Ext.Viewport({
        enableTabScroll : true,   
        layout : "border",
        items : [priceGrid]
    });
    
    // 根据条件加载Grid
    function gridFresh() {
        priceStore.baseParams = {
            materialId : materialId,
            supplier : supplier
        };
        priceStore.load({
        	params:{
        		start : 0,
        		limit : Constants.PAGE_SIZE
    		}
		});
    }
    
    // 返回结果
    function returnObject(grid, rowIndex) {
    	var records = priceGrid.getSelectionModel().getSelections();
    	if (!records || records.length < 1) {
    		// 如果没有选择显示提示信息
    		Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_001);
    		return;
    	}
    	var record = records[0];
    	if (typeof rowIndex == 'number') {
    		record = grid.getStore().getAt(rowIndex);
    	}
    	
    	var obj = {};
    	// 报价
        obj.quotedPrice = record.data.quotedPrice;
    	
    	window.returnValue = obj;
		// 关闭画面
		window.close();
    }
    
    // 初始化
    gridFresh();
});