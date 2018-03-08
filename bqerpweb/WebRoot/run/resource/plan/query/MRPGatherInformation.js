Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
    Ext.grid.CheckColumn = function(config) {
	    Ext.apply(this, config);
	    if (!this.id) {
	        this.id = Ext.id();
	    }
	    this.renderer = this.renderer.createDelegate(this);
	};
	Ext.grid.CheckColumn.prototype = {
	    init : function(grid) {
	        this.grid = grid;
	        this.grid.on('render', function() {
		            var view = this.grid.getView();
		            view.mainBody.on('mousedown', this.onMouseDown, this);
		        }, this);
	    },
	
	    onMouseDown : function(e, t) {
	    		e.stopEvent();
	    		return false;
	        if (t.className && t.className.indexOf('x-grid3-cc-' + this.id) != -1) {
	            e.stopEvent();
	            var index = this.grid.getView().findRowIndex(t);
	            var record = this.grid.store.getAt(index);
	            if (record.data[this.dataIndex] == Constants.CHECKED_VALUE) {
	                record.set(this.dataIndex, Constants.UNCHECKED_VALUE);
	            } else {
	                record.set(this.dataIndex, Constants.CHECKED_VALUE);
	            }
	        }
	    },
	
	    renderer : function(v, p, record) {
	        p.css += ' x-grid3-check-col-td';
	    	p.css += ' x-item-disabled';
	        return '<div class="x-grid3-check-col' + (v == 'Y' ? '-on' : '')
	                + ' x-grid3-cc-' + this.id + '">&#160;</div>';
	    }
	}
	
	// grid中的数据Record
    var gridRecord = new Ext.data.Record.create([
    	{
            // 物料ID
            name : 'gatherId'
        }, 
    	{
            // 物料ID
            name : 'materialId'
        }, {
            // 物料编码
            name : 'materialNo'
        }, {
            // 物料名称
            name : 'materialName'
        },{
            // 规格型号
            name : 'className'
        },{
            // 规格型号
            name : 'specNo'
        },{
            // 材质/参数
            name : 'parameter'
        }, {
            // 采购数量
            name : 'approvedQty'
         }, {
            // 已收数量
            name : 'issQty'
        }, {
            // 计量单位
            name : 'stockUmName'
        }, {
            // 最大库存量
            name : 'maxStock'
        },{
            // 是否免检
            name : 'qaControlFlag'
        }]);
        
     // 是否免检 显示为Checkbox
    var ckcQaControlFlag = new Ext.grid.CheckColumn({
        header : "是否免检",
        dataIndex : 'qaControlFlag',
        width : 60
    });
        
    // Grid列定义
    var columnModel = new Ext.grid.ColumnModel([
    	new Ext.grid.RowNumberer({
			header : '行号',
			width : 35
		}), {
            header : "物料编码<font color='red'>*</font>",
            sortable: true,
            width : 80,
            dataIndex : 'materialNo',
            css:CSS_GRID_INPUT_COL
            
        }, {
            header : "物料名称",
            sortable: true,
            width : 100,
            dataIndex : 'materialName'
        },{
            header : "规格型号",
            sortable: true,
            width : 60,
            dataIndex : 'specNo'
        },{
            header : "材质/参数",
            sortable: true,
            width : 60,
            dataIndex : 'parameter'
        }, {
            header : "采购数量<font color='red'>*</font>",
            sortable: true,
            width : 60,
            align : 'right',
            dataIndex : 'approvedQty'
        }, {
            header : "已收数量",
            sortable: true,
            width : 60,
            align : 'right',
            dataIndex : 'issQty'
        },  {
        	header : '物料ID',
            hidden : true,
            dataIndex : 'materialId'
        }, {
        	header : '计量单位',
            hidden : true,
            dataIndex : 'stockUmName'
        }, {
        	header : '最大库存量',
            hidden : true,
            dataIndex : 'maxStock'
        }]);

	var dataProxy = new Ext.data.HttpProxy({
		url : 'resource/getMaterialGatherDetail.action'
	});

	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	}, gridRecord);

	var store = new Ext.data.Store({
		proxy : dataProxy,
		reader : theReader
	});
     

	// 分页
//	store.load({
//		params : {
//			start : 0,
//			limit : 10
//		}
//	});

	var sm = new Ext.grid.RowSelectionModel({
				singleSelect : true
			});
    
    
    
	var grid = new Ext.grid.GridPanel({
		region : "center",
        layout : 'fit',
        autoScroll : true,
        height : 410,
        isFormField : false,
        border : true,
        anchor : "0",
        // 标题不可以移动
        enableColumnMove : false,
		store : store,
		
		columns : [new Ext.grid.RowNumberer({
                            header : "行号",
                            width : 35
                        }),{
            header : "物料汇总ID<font color='red'>*</font>",
            sortable: true,
            width : 80,
            hidden : true,
            dataIndex : 'gatherId',
            css:CSS_GRID_INPUT_COL
            
        }, {
            header : "物料ID<font color='red'>*</font>",
            sortable: true,
            width : 80,
            hidden : true,
            dataIndex : 'materialId',
            css:CSS_GRID_INPUT_COL
            
        },{
            header : "物料编码<font color='red'>*</font>",
            sortable: true,
            width : 80,
            dataIndex : 'materialNo',
            css:CSS_GRID_INPUT_COL
            
        }, {
            header : "物料名称",
            sortable: true,
            width : 100,
            dataIndex : 'materialName'
        },{
            header : "规格型号",
            sortable: true,
            width : 60,
            dataIndex : 'specNo'
        },{
            header : "材质/参数",
            sortable: true,
            width : 60,
            dataIndex : 'parameter'
        }, {
            header : "采购数量<font color='red'>*</font>",
            sortable: true,
            width : 60,
            align : 'right',
            dataIndex : 'approvedQty'
        }, {
            header : "已收数量",
            sortable: true,
            width : 60,
            align : 'right',
            dataIndex : 'issQty'
        }, {
        	header : '计量单位',
            hidden : false,
            dataIndex : 'stockUmName'
        }, {
        	header : '最大库存量',
            hidden : false,
            dataIndex : 'maxStock'
        },ckcQaControlFlag],
        sm : new Ext.grid.RowSelectionModel({
                    singleSelect : true
                }),
		
		autoSizeColumns : true,
		viewConfig : {
			forceFit : true
		},
		title : '物料需求计划汇总查询',

		// 分页
		bbar : new Ext.PagingToolbar({
			pageSize : 10,
			store : store,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		})
		
	});

	// ---------------------------------------
    grid.on('dblclick', returnList);
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		// layout : "fit",
		items : [grid]
	});

	// 查询
	function queryRecord() {
		store.load({
			params : {
				start : 0,
				limit : 10
			}
		});
	}
    queryRecord();
	function returnList() {
        // 选择的记录
        var obj = new Object();
        var record = grid.getSelectionModel().getSelected();
        // 返回值
        obj.gatherId = record.get("gatherId"), 
        obj.materialId = record.get("materialId"), 
        obj.materialNo = record.get("materialNo"), 
        obj.materialName = record.get("materialName"), 
        obj.specNo = record.get("specNo"), 
        obj.parameter = record.get("parameter"), 
        obj.stockUmId = record.get("stockUmName"),
        obj.nbQuotedQty = record.get("approvedQty")-record.get("issQty"),
        // start jincong 增加单位ID 2008-01-10
//        obj.stock = record.get("stockUmId"),
        // end jincong 增加单位ID 2008-01-10
//        obj.factory = record.get("factory"), 
//        obj.docNo = record.get("docNo"), 
        obj.className = record.get("className"), 
//        obj.locationNo = record.get("locationNo"), 
        obj.maxStock = record.get("maxStock"), 
        obj.qaControlFlag = record.get("qaControlFlag");
        // 采购数量
        obj.purUmId = record.get("purUmId");
        window.returnValue = obj;
        // 关闭画面
        window.close();
    }

	

});