/*Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();*/
Ext.onReady(function(){
   //物资名称
	var materialName = new Ext.form.TextField({
		id : 'fuzzy',
		name : 'fuzzyText',
         anchor : '85%',
        fieldLabel:'物资名称'
	});
	
	var modelName = new Ext.form.TextField({
		id : 'fuzzyText',
		name : 'fuzzyText',
        anchor : '85%',
        fieldLabel:'型号规格'
	});
	 function query() {
    	// 载入数据
   	gridStore.baseParams = {
 			modelName : modelName.getValue(),
 			materialName:materialName.getValue()
 		};
	  	gridStore.load({
	  		params : {
				start : 0,
				limit : 18
	  		}
	  	});
    }
    
	// 查询按钮
    var fuuzyBtn = new Ext.Button({
        id : 'fuuzyBtn',
        text : '查询',
        iconCls : 'query',
        handler : query
    });
    
    // 定义grid中的数据
    var gridData = new Ext.data.Record.create([{
    	// 物料编码
    	name : 'materialNo',
    	mapping:0
    }, {
    	// 物料名称
    	name : 'materialName',
    	mapping:1
    }, {
    	// 规格型号
    	name : 'specNo',
    	mapping:2
    }, {
    	// 单位
    	name : 'stockUmId',
    	mapping:3
    	
    }, {
    	// 当前库存数
    	name : 'whsName',
    	mapping:4
    }, {
    	// 最小库存数
    	name : 'locationName',
    	mapping:5
    }, {
    	// 最大库存数
    	name : 'lotNo',
    	mapping:6
    } /*{
    	// 当前库存
    	name : 'nowCount'
    },
    {
    	name:'stdCost'
    },
    {
    	name:'allCost'
    }*/
    ]);
    
 /*   var gridStore = new Ext.data.JsonStore({
        url : 'resource/getMaterialQueryAndSelect.action',
        root : 'list',
        totalProperty : 'totalCount',
        sortInfo : {field: "materialNo", direction: "ASC"},
        fields : gridData
  	});*/
  	
  
	var dataProxy = new Ext.data.HttpProxy(

			{
				url:'equ/findEquLibrary.action'
			}

	);

	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"

	}, gridData);

	var gridStore = new Ext.data.Store({

		proxy : dataProxy,

		reader : theReader

	});
  	gridStore.load({
  		params : {
			start : 0,
			limit : 18
  		}
  	});
  	
  	 /* gridStore.on('beforeload', function() {
                Ext.apply(this.baseParams, {
                            fuzzyText :fuzzyText.getValue(),
                            materialClassCode:materialClassCode
                        });
            });*/
  //modify by ypan 20100925
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
  				width:40
  			}), {
  				header : '物资编码',
  				anchor : '100%',
                sortable : true,
                dataIndex : 'materialNo'
  			}, {
  				header : '物资名称',
  				anchor : '100%',
                sortable : true,
                dataIndex : 'materialName'
  			}, {
  				header : '规格型号',
  				anchor : '100%',
                sortable : true,
                dataIndex : 'specNo'
  			}, {
  				header : '单位',
  				anchor : '85%',
                sortable : true,
                dataIndex : 'stockUmId'
  			}, {
  				header : '当前库存数',
  				anchor : '85%',
                sortable : true,
                dataIndex : 'whsName'
  			}, {
  				header : '最小库存数',
  				anchor : '85%',
                sortable : true,
                dataIndex : 'locationName'
  			}, {
  				header : '最高库存数',
  				anchor : '85%',
                sortable : true,
                dataIndex : 'lotNo'
  			}/*, {
  				header : '当前库存',
  				width : 70,
                sortable : true,
                align : 'right',
                dataIndex : 'nowCount'
  			}, {
  				header : '标准成本',
  				width : 70,
  				hidden : true,
                sortable : true,
                align : 'right',
                dataIndex : 'stdCost',
                renderer:function(value)
                {
                	if(value!=null&&value!="")
                	value=value.toFixed(2);
                	return value;
                }
  			}, {
  				header : '总金额',
  				width : 70,
  				hidden : true,
                sortable : true,
                align : 'right',
                dataIndex : 'allCost',
                renderer:function(value)
                {
                	if(value!=null&&value!="")
                	value=value.toFixed(2);
                	return value;
                }
  			}*/
  			],
  		tbar : ['物资名称',materialName, '-','型号规格', modelName,fuuzyBtn],
  		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : gridStore,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		}),
        frame : false,
        border : false,
        enableColumnHide : true,
        enableColumnMove : false
  	})
  
    
    /**
     * 模糊查询
     */
   
   

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