Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function(){

    var arg = window.dialogArguments;
	var materialNo=arg.materialNo;
	var orderNo=arg.orderNo;
	var purNo=arg.purNo;
    
	
	/**
	 * 金钱格式化
	 */
	function moneyFormat(v) {
		if (v == null || v == "") {
			return "0.0000";
		}
		v = (Math.round((v - 0) * 10000)) / 10000;
		v = (v == Math.floor(v))
				? v + ".0000"
				: ((v * 10 == Math.floor(v * 10))
						? v + "000"
						: ((v * 100 == Math.floor(v * 100)) ? v + "00" : ((v
								* 1000 == Math.floor(v * 1000)) ? v + "0" : v)));
		v = String(v);
		var ps = v.split('.');
		var whole = ps[0];
		var sub = ps[1] ? '.' + ps[1] : '.0000';
		var r = /(\d+)(\d{3})/;
		while (r.test(whole)) {
			whole = whole.replace(r, '$1' + ',' + '$2');
		}
		v = whole + sub;
		if (v.charAt(0) == '-') {
			return '-' + v.substr(1);
		}
		return v;
	}
	/**
	 * 数字格式化
	 */
	function numberFormat(v) {
		v = (Math.round((v - 0) * 100)) / 100;
		v = (v == Math.floor(v)) ? v + ".00" : ((v * 10 == Math.floor(v * 10))
				? v + "0"
				: v);
		v = String(v);
		var ps = v.split('.');
		var whole = ps[0];
		var sub = ps[1] ? '.' + ps[1] : '.00';
		var r = /(\d+)(\d{3})/;
		while (r.test(whole)) {
			whole = whole.replace(r, '$1' + ',' + '$2');
		}
		v = whole + sub;
		if (v.charAt(0) == '-') {
			return '-' + v.substr(1);
		}
		return v;
	}
    // 定义grid中的数据
    var gridData = new Ext.data.Record.create([{
		// 物料编码
		name : 'materialNo'
	}, {
		//到货单号
		name : 'orderNo'
	}, {
		//采购单号
		name : 'purNo'
	},
			// 异动数量
			{
				name : 'transQty'
			},
			// 单位
			{
				name : 'transUmId'
			},
			// 物料单价
			{
				name : 'price'
			},
			// 标准成本
			{
				name : 'stdCost'
			},
			// 操作人
			{
				name : 'operatedBy'
			},
			// 操作时间
			{
				name : 'operatedDate'
			},
			// 操作仓库
			{
				name : 'whsName'
			},
			// 操作库位
			{
				name : 'locationName'
			},
			// 调入仓库
			{
				name : 'whsNameTwo'
			},
			// 调入库位
			{
				name : 'locationNameTwo'
			},
			// 批号
			{
				name : 'lotNo'
			},
			// 备注
			{
				name : 'memo'
			}]);
    
    var gridStore = new Ext.data.JsonStore({
        url : 'resource/queryForArrivalList.action',
        root : 'list',
        totalProperty : 'totalCount',
      //  sortInfo : {field: "materialNo", direction: "ASC"},
       fields : gridData
  	});
  	
  	// 载入数据
  	gridStore.baseParams = {
  		materialNo : materialNo,
  		orderNo : orderNo,
  		purNo : purNo
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
  				header : '采购单号',
  				width : 70,
                sortable : true,
                dataIndex : 'purNo'
  			}, {
  				header : '物料编码',
  				width : 120,
                sortable : true,
                dataIndex : 'materialNo'
  			}, {
  				header : '到货单号',
  				width : 70,
                sortable : true,
                hidden : true,
                dataIndex : 'orderNo'
  			},  //异动数量
            {   header : "异动数量",
                width : 80,
                sortable : true,
                align : 'right',
                renderer : moneyFormat,
                dataIndex : 'transQty'
            },
            //  单位
            {   header : "单位",
                width : 100,
                sortable : true,
                dataIndex : 'transUmId'            
            },
            //  单位
            {   header : "物料单价",
                width : 100,
                sortable : true,
                align : 'right',
                renderer : numberFormat,
                dataIndex : 'price'            
            },
            //  单位
            {   header : "标准成本",
                width : 100,
                sortable : true,
                align : 'right',
                renderer : numberFormat,
                dataIndex : 'stdCost'            
            },
              //操作人
            {   header : "操作人",
                width : 100,
                sortable : true,
                dataIndex : 'operatedBy'            
            },
              //操作时间
            {   header : "操作时间",
                width : 100,
                sortable : true,
                dataIndex : 'operatedDate'            
            },
              //操作仓库
            {   header : "操作仓库",
                width : 100,
                sortable : true,
                dataIndex : 'whsName'            
            },
              //操作库位
            {   header : "操作库位",
                width : 100,
                sortable : true,
                dataIndex : 'locationName'            
            },
              //调入仓库
            {   header : "调入仓库",
                width : 100,
                sortable : true,
                dataIndex : 'whsNameTwo'            
            },
              //调入库位
            {   header : "调入库位",
                width : 100,
                sortable : true,
                dataIndex : 'locationNameTwo'            
            },
              //批号
            {   header : "批号",
                width : 40,
                sortable : true,
                dataIndex : 'lotNo'            
            },
              //备注
            {   header : "备注",
                width : 200,
                sortable : true,
                dataIndex : 'memo'            
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
  	
  	// 设定布局器及面板
	new Ext.Viewport({   
		enableTabScroll : true,
        layout : "border",
        items : [grid]
    });
   
});