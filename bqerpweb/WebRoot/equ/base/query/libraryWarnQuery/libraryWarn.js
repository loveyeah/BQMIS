Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function(){

    // 定义grid中的数据
    var gridData = new Ext.data.Record.create([{
    	// 物资ID
    	name : 'materialID',
    	mapping:0
    },{
    	// 物资编码
    	name : 'materialNo',
    	mapping:1
    }, {
    	// 物资名称
    	name : 'materialName',
    	mapping:2
    },{
    	// 最大库存
    	name : 'MAXSTOCK',
    	mapping:3
    }, {
    	// 最小库存
    	name : 'MINSTOCK',
    	mapping:4
    },  {
    	// 规格型号
    	name : 'specNo',
    	mapping:5
    }, {
    	// 单位
    	name : 'stockUmId',
    	mapping:6
    }, {
    	// 当前库存
    	name : 'nowCount',
    	 	mapping:7
    	
    }, {
    	// 需采购数量
    	name : 'buyCount',
    	 	mapping:8
    	
    }
    ]);
    	var fuzzy = new Ext.form.TextField({
		name : 'fuzzy',
		id: 'fuzzy',
		xtype : 'textfield'
	});
		var fuzzy1 = new Ext.form.TextField({
		name : 'fuzzy1',
		id: 'fuzzy1',
		xtype : 'textfield'
	});
    
    var gridStore = new Ext.data.JsonStore({
        url : 'equ/EquAction.action',
        root : 'list',
        totalProperty : 'totalCount',
        sortInfo : {field: "materialNo", direction: "ASC"},
        fields : gridData
  	});
  	
  
  	gridStore.load({
  		params : {
			start : 0,
			limit : Constants.PAGE_SIZE,
			fuzzy : '',
			fuzzy1 : ''
  		}
  	});
  		gridStore.baseParams = ({
					fuzzy : '',
					fuzzy1 : ''
				});
// gridStore.on("load",function(){  
// alert(Ext.encode(gridStore.getAt(0).data))
// })
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
  				header : '物资编码',
  				width : 100,
                sortable : true,
                 align : 'center',
                dataIndex : 'materialNo'
  			}, {
  				header : '物资名称',
  				width : 90,
  				align : 'center',
                sortable : true,
                dataIndex : 'materialName'
  			}, {
  				header : '规格型号',
  				width : 120,
  				align : 'center',
                sortable : true,
                dataIndex : 'specNo'
  			}, {
  				header : '单位',
  				width : 50,
  				align : 'center',
                sortable : true,
                dataIndex : 'stockUmId'
  			},{
  				header : '当前库存',
  				width : 70,
  				align : 'center',
                sortable : true,
                dataIndex : 'nowCount'
  			},{
  				header : '最小库存数',
  				width : 80,
                sortable : true,
                align : 'center',
                dataIndex : 'MINSTOCK'
  			},{
  				header : '最高库存数',
  				width : 80,
                sortable : true,
                align : 'center',
                dataIndex : 'MAXSTOCK'
  			},{
  				header : '需采购数量',
  				width : 90,
                sortable : true,
                align : 'center',
                dataIndex : 'buyCount'
  			}
  			
  			],
  		tbar : ['物资名称', fuzzy,
  		'型号规格', fuzzy1,{
		
			iconCls : 'query',
			text : "查询",
			handler : function() {
	  
				var fu = Ext.getCmp('fuzzy').getValue();// 得到输入框的值
				
				var fu1 = Ext.getCmp('fuzzy1').getValue();// 得到输入框的值
					
				gridStore.baseParams = ({
					fuzzy : fu,
					fuzzy1 : fu1
				});
		  	gridStore.load({
  		params : {
			start : 0,
			limit : Constants.PAGE_SIZE,
			fuzzy : '',
			fuzzy1 : ''
  		}
  	});
			}
		}],
		// 分页
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
  


    var layout = new Ext.Viewport({
                layout : 'border',
                margins : '0 0 0 0',
                border : true,
                items :  [
               {
		  region : "center",
		layout : 'fit',
		collapsible : true,
		autoScroll : true,
		items:[grid]
		}
                ]
            });
});