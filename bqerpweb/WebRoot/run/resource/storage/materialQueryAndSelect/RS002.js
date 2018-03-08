Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function(){
	var  materialClassCode="";
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
        iconCls : Constants.CLS_QUERY,
        handler : query
    });
    
    // 定义grid中的数据
    var gridData = new Ext.data.Record.create([{
    	// 物料编码
    	name : 'materialNo'
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
    	// 仓库
    	name : 'whsName'
    }, {
    	// 库位
    	name : 'locationName'
    }, {
    	// 批号
    	name : 'lotNo'
    }, {
    	// 当前库存
    	name : 'nowCount'
    },
    {
    	name:'stdCost'
    },
    {
    	name:'allCost'
    }
    ]);
    
    var gridStore = new Ext.data.JsonStore({
        url : 'resource/getMaterialQueryAndSelect.action',
        root : 'list',
        totalProperty : 'totalCount',
        sortInfo : {field: "materialNo", direction: "ASC"},
        fields : gridData
  	});
  	
  
  	gridStore.load({
  		params : {
			start : 0,
			limit : Constants.PAGE_SIZE
  		}
  	});
  	
//  	gridStore.on("load",function(){
//  		var totalNowCount=0;
//  		var totalStdCost=0;
//  		var totalAllCost=0;
//  		for (var j = 0; j < gridStore.getCount(); j++) {
//				var temp = gridStore.getAt(j);
//				if(temp.get("nowCount")!=null) 
//				{
//					totalNowCount=parseFloat(totalNowCount)+parseFloat(temp.get("nowCount"));
//				}
//				if(temp.get("stdCost")!=null) 
//				{
//					totalStdCost=parseFloat(totalStdCost)+parseFloat(temp.get("stdCost"));
//				}
//				if(temp.get("allCost")!=null) 
//				{
//					
//					totalAllCost=totalAllCost+temp.get("allCost");
//					
//				}
//  		}
//  		
//  	
//  		
//  		
//  		if(gridStore.getCount()>0)
//  		{
//  	  var myrecord=new gridData({
//  	  	materialNo:'',
//  	  	materialName:'',
//  	  	specNo:'',
//  	  	nowCount:totalNowCount.toFixed(2),
//  	  	stdCost:totalStdCost.toFixed(2),
//  	  	allCost:totalAllCost.toFixed(2)
//  	  });
//  	  gridStore.add(myrecord);
//  		}
//  	});
  	
  	  gridStore.on('beforeload', function() {
                Ext.apply(this.baseParams, {
                            fuzzyText :fuzzyText.getValue(),
                            materialClassCode:materialClassCode
                        });
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
  				hidden: true,
                sortable : true,
                dataIndex : 'locationName'
  			}, {
  				header : '批号',
  				width : 50,
                sortable : true,
                dataIndex : 'lotNo'
  			}, {
  				header : '当前库存',
  				width : 70,
                sortable : true,
                align : 'right',
                dataIndex : 'nowCount'
  			}, {
  				header : '标准成本',
  				width : 70,
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
                sortable : true,
                align : 'right',
                dataIndex : 'allCost',
                renderer:function(value)
                {
                	if(value!=null&&value!="")
                	value=value.toFixed(2);
                	return value;
                }
  			}
  			
  			],
  		tbar : [fuzzyText, '-', fuuzyBtn],
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
  
    
    /**
     * 模糊查询
     */
    function query() {
    	// 载入数据
//    	gridStore.baseParams = {
//  			fuzzyText : fuzzyText.getValue()
//  		}
	  	gridStore.load({
	  		params : {
				start : 0,
				limit : Constants.PAGE_SIZE
	  		}
	  	});
    }
    
      //-----------add by fyyang 090623--------------------------
    var root =new Ext.tree.AsyncTreeNode({
		text : '物料',
		isRoot : true,
		id : '-1'
		
	});

	var mytree = new Ext.tree.TreePanel({
		renderTo : "treepanel",
		//height : 900,
		autoHeight:true,
		border:false,
		root : root,
		requestMethod : 'GET',
		loader : new Ext.tree.TreeLoader({
			url : "resource/getMaterialClass.action"
		})
	});

	mytree.on("click", clickTree, this);
	mytree.on('beforeload', function(node) {
		mytree.loader.dataUrl = 'resource/getMaterialClass.action';
	}, this);

	function clickTree(node) {
		  materialClassCode=node.id;
	     if(materialClassCode=="-1") materialClassCode="";
		  query();
		
	};
	root.expand();//展开根节点
    //----------------------------------------------------------------
		
  	// 设定布局器及面板
//	new Ext.Viewport({   
//		enableTabScroll : true,
//        layout : "border",
//        items : [grid]
//    });
	      // 显示区域
    var layout = new Ext.Viewport({
                layout : 'border',
                margins : '0 0 0 0',
                border : true,
                items :  [
                {
			region : 'west',
			split : true,
			width : 180,
			layout : 'fit',
			minSize : 175,
			maxSize : 600,
			margins : '0 0 0 0',
			collapsible : true,
			border : false,
			autoScroll : true,
			items : [mytree]
		},{
		  region : "center",
		layout : 'fit',
		collapsible : true,
		autoScroll : true,
		items:[grid]
		}
                ]
            });
});