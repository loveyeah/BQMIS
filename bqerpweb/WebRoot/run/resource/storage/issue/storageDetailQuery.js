Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function(){
	
	var arg = window.dialogArguments;
	var materialNo=arg.materialNo;

    
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
    },{name:'materialId'}]);
    
    var gridStore = new Ext.data.JsonStore({
        url : 'resource/getMaterialQueryByMaterialNo.action',
        root : 'list',
        totalProperty : 'totalCount',
        sortInfo : {field: "materialNo", direction: "ASC"},
        fields : gridData
  	});
  	
  	
  	
  	// 载入数据
  	gridStore.baseParams = {
  		materialNo : materialNo
  	}
  
  	

  	gridStore.load({
  		params : {
			start : 0,
			limit : Constants.PAGE_SIZE
  		}
  	});
  		//------add by fyyang 091208-------------------
  		gridStore.on("load",function(){
	
		var totalNum = 0; 
				
				for (var j = 0; j < gridStore.getCount(); j++) {
					var temp = gridStore.getAt(j);
					if (temp.get("nowCount") != null) {
						totalNum = parseFloat(totalNum)
								+ parseFloat(temp.get("nowCount"));
					}
				}
		var mydata=new gridData({
			materialNo : "合计",
			materialName : "",
			specNo : "",
			stockUmId : "",
			whsName : "",
			locationName : "",
			lotNo : "",
			nowCount : totalNum,
			materialId : ""
		});
		if(gridStore.getCount()>0)
		{
		 	gridStore.add(mydata);
		}
		
	})
  	//---------------------------------------------------
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
                dataIndex : 'materialNo',
                  renderer : showColor
  			}, {
  				header : '物料名称',
  				width : 140,
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
  				width : 140,
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
                dataIndex : 'nowCount',
                   renderer : showColor
  			}],
  		
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
  	
  	    // 根据不同的状态显示不同的颜色
    function showColor(value, cellmeta, record, rowIndex, columnIndex, store) {
    
    	// 获得当前行的工作票状态
    	var materialId = record.data["materialId"];
    	
    	if(materialId ==0) {
    	return "<font color='red'>" + value + "</font>";
    	} 
    	else
    	{
    	
    	return value;
    	}
    }
  	
  	// 设定布局器及面板
	new Ext.Viewport({   
		enableTabScroll : true,
        layout : "border",
        items : [grid]
    });
    
   
});