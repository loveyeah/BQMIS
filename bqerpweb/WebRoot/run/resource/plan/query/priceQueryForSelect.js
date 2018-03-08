Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	
	var args = window.dialogArguments;
	var materialName=args.materialName;
	var requirementDetailId=args.requirementDetailId;
	
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
    	{name : 'inquireDetailId'},
    {name : 'gatherId'},
	{name : 'inquireSupplier'},
	{name:'materialId'},
	{name:'materialName'},
	{name:'supplyName'},
	{name:'inquireQty'},
	{name:'unitPrice'},
	{name:'totalPrice'},
	{name:'qualityTime'},
	{name:'offerCycle'},
	{name:'specNo'},
	{name:'effectStartDate'},
	{name:'effectEndDate'},
	{name: 'isSelectSupplier'},
	{name: 'filePath'}
	]);
        
     // 是否免检 显示为Checkbox
    var ckcQaControlFlag = new Ext.grid.CheckColumn({
        header : "是否免检",
        dataIndex : 'qaControlFlag',
        width : 60
    });
        
  

	var dataProxy = new Ext.data.HttpProxy({
		url : 'resource/getInquirePriceInfo.action'
	});

	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	}, gridRecord);

	var store = new Ext.data.Store({
		proxy : dataProxy,
		reader : theReader
	});
     
	//------------------------		
    var sm=new Ext.grid.CheckboxSelectionModel({
		singleSelect : false
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
		
		columns : [sm,new Ext.grid.RowNumberer({
                            header : "行号",
                            width : 35
                        }),{
			
			header : "ID",
			width : 75,
			sortable : true,
			dataIndex : 'inquireDetailId',
			hidden:true
		},

		{
			header : "汇总ID",
			width : 75,
			sortable : true,
			dataIndex : 'gatherId',
			hidden:true
		},
		{
			header : "物料名称",
			width : 75,
			sortable : true,
			dataIndex : 'materialName',
			renderer:showColor //add by ywliu 20091029
		},
		{
			header : "供应商",
			width : 75,
			sortable : true,
			dataIndex : 'supplyName',
			renderer:showColor //add by ywliu 20091029
		},
		{
			header : "规格型号",
			width : 75,
			sortable : true,
			dataIndex : 'specNo',
			renderer:showColor//add by ywliu 20091029
		},
		{
			header : "采购数量",
			width : 75,
			sortable : true,
			dataIndex : 'inquireQty',
			renderer:showColor//add by ywliu 20091029
		},
		{
			header : "单价",
			width : 75,
			sortable : true,
			dataIndex : 'unitPrice',
			renderer:showColor//add by ywliu 20091029
		},
		{
			header : "总价",
			width : 75,
			sortable : true,
			dataIndex : 'totalPrice',
			renderer:showColor//add by ywliu 20091029
		},
		{
			header : "报价有效开始日期",
			width : 110,
			sortable : true,
			dataIndex : 'effectStartDate',
			renderer:showColor//add by ywliu 20091029
		},
		{
			header : "报价有效结束日期",
			width : 110,
			sortable : true,
			dataIndex : 'effectEndDate',
			renderer:showColor//add by ywliu 20091029
		},
        {
          
            header : '状态',
            hidden : true,
            dataIndex : 'isEnquire',
            renderer:function(value)
            {   
            	if(value=="N") return "未询价";
            	else if(value=="Q") return "已询价";
            }
        },{//add by wpzhu 100412
			header : "附件",
			sortable : true,
			dataIndex : 'filePath',
			renderer:function(v){
				if(v !=null && v !='')
				{ 
					var s =  '<a href="#" onclick="window.open(\''+v+'\');return  false;">[查看附件]</a>';
					return s;
				}
			} 
		} ],
        sm : sm,
		
		autoSizeColumns : true,
		viewConfig : {
			forceFit : true
		},
		//title : '物料需求计划汇总查询',
		// 分页
		bbar : new Ext.PagingToolbar({
			pageSize : 10,
			store : store,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		})
		
	});
		//add by ywliu 20091029
	 function showColor(value, cellmeta, record, rowIndex, columnIndex, store)
	 {
	 	if(value==null)
	 	{
	 	  return "";	
	 	}
	 	
	 	if(record.get("isSelectSupplier")=="Y")
	 	{
	 		return "<font color='red'>" + value + "</font>";
	 	}
	 	else
	 	{
	 	 return 	value;
	 	}
	 	
	 }
	
	// ---------------------------------------
  
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		// layout : "fit",
		items : [grid]
	});

	// 查询
	function queryRecord() {
		  store.baseParams = {
          materialName:materialName,
          requirementDetailId : requirementDetailId,
          buyer: ""
        };
		store.load({
			params : {
				start : 0,
				limit : 10
			}
		});
	}
	
    queryRecord();
});