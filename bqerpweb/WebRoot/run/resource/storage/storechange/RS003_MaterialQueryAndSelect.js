Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function(){
	
	// 模糊查询框
	var fuzzyText = new Ext.form.TextField({
		id : 'fuzzyText',
		name : 'fuzzyText',
        width : 405,
		emptyText : '批号/仓库编码/库位编码/仓库名称/库位名称/物料编码/物料名称/规格型号'
	});
	
	// 模糊查询按钮
    var fuuzyBtn = new Ext.Button({
        id : 'fuuzyBtn',
        text : '模糊查询',
        handler : query
    });
	
	// 确认按钮
    var confirmBtn = new Ext.Button({
        id : 'confirm',
        text : '确认',
        handler : returnValues
    });
    
    // 取消按钮
    var cancelBtn = new Ext.Button({
    	id : 'cancelBtn',
    	text : Constants.BTN_CANCEL,
    	iconCls : Constants.CLS_CANCEL,
    	handler : cancelHandler
    })
    
    // 定义grid中的数据
    var gridData = new Ext.data.Record.create([{
    	// 物料编码
    	name : 'materialNo'
    }, {
    	// 物料ID
    	name : 'materialId'
    }, {
    	// 物料名称
    	name : 'materialName'
    }, {
    	// 规格型号
    	name : 'specNo'
    }, {
    	// 单位
    	name : 'stockUmId'
    }, {
    	// 仓库编码
    	name : 'whsNo'
    }, {
    	// 仓库名称
    	name : 'whsName'
    }, {
    	// 库位号
    	name : 'locationNo'
    }, {
    	// 库位名称
    	name : 'locationName'
    }, {
    	// 批号
    	name : 'lotNo'
    }, {
    	// 当前库存
    	name : 'nowCount'
    }, {
    	// 库存物料记录.上次修改时间
    	name : 'lastModifiedDateWhs'
    }, {
    	// 库位物料记录.上次修改时间
    	name : 'lastModifiedDateLocation'
    }, {
    	// 批号记录表.上次修改时间
    	name : 'lastModifiedDateLot'
    }]);
    
    var gridStore = new Ext.data.JsonStore({
        url : 'resource/getMaterialQueryAndSelect.action',
        root : 'list',
        totalProperty : 'totalCount',
        sortInfo : {field: "materialNo", direction: "ASC"},
        fields : gridData
  	});
  	
  	// 载入数据
  	gridStore.baseParams = {
  		fuzzyText : fuzzyText.getValue()
  	}
  	gridStore.load({
  		params : {
			start : 0,
			limit : Constants.PAGE_SIZE
  		}
  	});

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
  				header : '物料编码',
  				width : 70,
                sortable : true,
                dataIndex : 'materialNo'
  			}, {
  				header : '物料名称',
  				width : 175,
                sortable : true,
                dataIndex : 'materialName'
  			}, {
  				header : '规格型号',
  				width : 60,
                sortable : true,
                dataIndex : 'specNo'
  			}, {
  				header : '单位',
  				width : 50,
                sortable : true,
                dataIndex : 'stockUmId'
  			}, {
  				header : '仓库',
  				width : 175,
                sortable : true,
                dataIndex : 'whsName'
  			}, {
  				header : '库位',
  				width : 90,
                sortable : true,
                dataIndex : 'locationName'
  			}, {
  				header : '批号',
  				width : 70,
                sortable : true,
                dataIndex : 'lotNo'
  			}, {
  				header : '当前库存',
  				width : 70,
                sortable : true,
                align : 'right',
                dataIndex : 'nowCount'
  			}],
  		tbar : [fuzzyText, '-', fuuzyBtn, '-', confirmBtn, '-', cancelBtn],
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
  	})
  	
  	// 双击行
  	grid.on("rowdblclick", returnValues);
  	
  	// 设定布局器及面板
	new Ext.Viewport({   
		enableTabScroll : true,
        layout : "border",
        items : [grid]
    });
    
    /**
     * 选中一行数据,返回
     */
    function returnValues() {
		var selected = grid.getSelectionModel().getSelections();
		if(selected.length < 1) {
			Ext.Msg.alert(Constants.ERROR, Constants.COM_I_001)
		} else {
			var record = selected[0];
			var object = new Object();
			// 物料ID
			object.materialId = record.get("materialId");
			// 物料编码
			object.materialNo = record.get("materialNo");
			// 物料名称
			object.materialName = record.get("materialName");
			// 规格型号
			object.specNo = record.get("specNo");
			// 仓库编码
			object.whsNo = record.get("whsNo");
			// 仓库名称
			object.whsName = record.get("whsName");
			// 库位编码
			object.locationNo = record.get("locationNo");
			// 库位名称
			object.locationName = record.get("locationName");
			// 批号
			var lotNo = 0;
			if(record.get("lotNo") != "") {
				lotNo = record.get("lotNo");
			}
			object.lotNo = lotNo;
			// 当前库存
			object.nowCount = record.get("nowCount");
			// 库存物料记录.上次修改时间
			object.lastModifiedDateWhs = record.get("lastModifiedDateWhs");
			// 库位物料记录.上次修改时间
			object.lastModifiedDateLocation = record.get("lastModifiedDateLocation");
			// 批号记录表.上次修改时间
			object.lastModifiedDateLot = record.get("lastModifiedDateLot");
			// 标识
			object.flag = "Y";
			window.returnValue = object;
			window.close();
		}
    }
    
    /**
     * 模糊查询
     */
    function query() {
    	// 载入数据
	  	gridStore.baseParams = {
  			fuzzyText : fuzzyText.getValue()
  		}
	  	gridStore.load({
	  		params : {
				start : 0,
				limit : Constants.PAGE_SIZE
	  		}
	  	});
    }
    
    /**
     * 取消
     */
    function cancelHandler() {
    	var object = new Object();
    	// 标识
		object.flag = "N";
    	window.returnValue = object;
    	window.close();
    }
});