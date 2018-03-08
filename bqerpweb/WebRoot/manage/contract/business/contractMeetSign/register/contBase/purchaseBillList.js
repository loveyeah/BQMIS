Ext.onReady(function(){
    // 页面跳转时使用    
    //var register = parent.Ext.getCmp('tabPanel').register;
    // 设置刷新采购单列表的监听器
    //register.refreshListHandler = refreshAllData;
    
	// 分页时每页显示记录条数
	var PAGE_SIZE = Constants.PAGE_SIZE;
    
    // ********** 主画面******* //
    var wd = 180;
    
    // 模糊查询输入框
    var tfFuuzy = new Ext.form.TextField({
        id : "tfFuuzy",
        width : wd + 200,
        hideLabel : true,
        emptyText : '采购订单号/合同编号/供应商编号/供应商名称/采购员编号/采购员姓名'
    });
    
 	// head工具栏
    var headTbar = new Ext.Toolbar({
    	region : 'north',
        items:[tfFuuzy,'-',
             {
	            id : 'btnQuery',
	            text : "模糊查询",
	            iconCls : Constants.CLS_QUERY,
	            handler : gridFresh
            }, '-', {
	            id : 'btnSubmit',
	            text : "确认",
	            iconCls : Constants.CLS_OK,
	            handler : choosePurchaseBill
            }]
    });

    // 是否显示所有采购单
    var ckbShowAll = new Ext.form.Checkbox({
        id : 'ckbShowAll',
        boxLabel : "显示所有采购单",
        width : wd,
        hideLabel : true
    })

    // grid工具栏
    var gridTbar = new Ext.Toolbar({
        items:[ckbShowAll]
    });

	//定义选择列
	var sm = new Ext.grid.RowSelectionModel({
		singleSelect : true
	});

    // grid中的数据Record
    var gridRecord = new Ext.data.Record.create([{
            // 采购单流水号
            name : 'orderId'
        }, {
            // 采购单编号
            name : 'purNo'
        }, {
            // 采购单状态
            name : 'purStatus'
        }, {
            // 人员名称
            name : 'buyerName'
        }, {
            // 合同号
            name : 'contractNo'
        }, {
            // 供应商ID
            name : 'supplier'
        }, {
            // 供应商编号
            name : 'supplierNo'
        },{
            // 供应商名称
            name : 'supplyName'
        },{
            // 采购员
            name : 'buyer'
        }, {
            // 备注
            name : 'orderMemo'
         }, {
            // 税率
            name : 'orderTaxRate'
        }, {
            // 汇率
            name : 'rate'
        },{
            // 交期
            name : 'orderDueDate'
        },{
            // 币别
            name : 'currencyId'
        },{
            // 采购订单表.上次修改日期
            name : 'orderModifyDate'
    }]);

    // grid的store
    var queryStore = new Ext.data.JsonStore({
        url : 'resource/getRegisterOrderList.action',
        root : 'list',
        totalProperty : 'totalCount',
        fields : gridRecord
    });
    queryStore.on('load', function(store, records) {
    	var diabledFlag = !records || records.length < 1;
    	Ext.getCmp('btnSubmit').setDisabled(diabledFlag);
    });
    
    // 分页工具栏
    var pagebar = new Ext.PagingToolbar({
        pageSize : PAGE_SIZE,
        store : queryStore,
        displayInfo : true,
        displayMsg : Constants.DISPLAY_MSG,
        emptyMsg : Constants.EMPTY_MSG
    });
    
    // 页面的Grid主体
    var gridOrder = new Ext.grid.GridPanel({
    	region : 'center',
        store : queryStore,
        columns : [new Ext.grid.RowNumberer({
					header : '行号',
					width : 35
				}), {
                    header : "采购单编号",
                    sortable: true,
                    width : 100,
                    dataIndex : 'purNo'
                }, {
                    header : "合同号",
                    sortable: true,
                    width : 100,
                    dataIndex : 'contractNo'
                }, {
                    header : "供应商编号",
                    sortable: true,
                    width : 90,
                    dataIndex : 'supplierNo'
                },{
                    header : "供应商名称",
                    sortable: true,
                    width : 100,
                    dataIndex : 'supplyName'
                },{
                    header : "采购员",
                    sortable: true,
                    width : 80,
                    dataIndex : 'buyerName'
                }, {
                    header : "备注",
                    sortable: true,
                    width : 270,
                    dataIndex : 'orderMemo'
                }, {
                	// 税率
                    hidden : true,
                    dataIndex : 'orderTaxRate'
                }, {
                	// 汇率
                    hidden : true,
                    dataIndex : 'rate'
                }, {
                	// 交期
                    hidden : true,
                    dataIndex : 'orderDueDate'
                }, {
                	// 币别
                    hidden : true,
                    dataIndex : 'currencyId'
                }, {
                	// 采购单流水号
                	header : "采购单流水号",
                    hidden : true,
                    dataIndex : 'orderId'
                }, {
                	// 采购单状态
                    hidden : true,
                    dataIndex : 'purStatus'
                }, {
                	// 人员Id
                    hidden : true,
                    dataIndex : 'buyer'
                }, {
                	// 供应商Id
                    hidden : true,
                    dataIndex : 'supplier'
                }, {
                	// 采购订单表.上次修改日期
                    hidden : true,
                    dataIndex : 'orderModifyDate'
                }],
        tbar : gridTbar,
        bbar : pagebar,
        sm : sm,
        frame : false,
        border : false,
        enableColumnMove : false
    });
    gridOrder.on('rowdblclick', choosePurchaseBill);
    gridOrder.on('render', function() {
	    // 初始化Grid
	    gridFresh();
    });
    
    // 主框架
    new Ext.Viewport({   
        layout : "border",
        autoHeight : true,
        items : [headTbar, gridOrder]
    });
    // ↑↑********** 主画面*******↑↑//

    // ↓↓*********处理***********↓↓//
    // 编辑Grid
  
    
    
    // 刷新画面数据
   // function refreshAllData() {
    //	if (register.orderId) {
    //		queryStore.on('load', tempRefreshFun);
    //	}
    	// 重新加载Grid中的数据
    //	queryStore.reload();
  //  }
    
    // 根据条件加载Grid
    function gridFresh() {
        queryStore.baseParams = {
            fuzzy : tfFuuzy.getValue(),
            isShowAll : ckbShowAll.checked
        };
        queryStore.load({
        	params:{
        		start : 0,
        		limit : PAGE_SIZE
    		}
		});
    }
    function choosePurchaseBill(){
    	var selectNodes = gridOrder.getSelectionModel().getSelections();
			if (selectNodes.length == 0) {
				Ext.Msg.alert("提示", "请选择采购单!");
				return false;
			}
			var ros = new Array();
			var purNos = new Array();
			//var workerNames = new Array();
			//var deptIds = new Array();
			for (var i = 0; i < selectNodes.length; i++) {
				var record = selectNodes[i];
				purNos.push(record.data.purNo);
				//workerNames.push(record.workerName);
				//deptIds.push(record.deptId);
				ros.push(record.data);
			}
			
			window.returnValue = {
				list : ros,
				codes : purNos.join(",")
				//names : workerNames.join(","),
				//deptIds : deptIds.join(",")
			};
			window.close();
    }
    // 重新加载Grid中的数据
   // function gridReload() {
   //    queryStore.reload();
   //}
    // ↑↑*********处理***********↑↑//
});