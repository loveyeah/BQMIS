Ext.onReady(function(){
	Ext.form.Field.prototype.msgTarget = 'title';
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
	 * 获取当前月的日期
	 */
	function getCurrentDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t;
		return s;
	}

	/**
	 * 获取当前前一月的日期
	 */
	function getCurrentDateFrom() {
		var d, s, t;
		d = new Date();
		var day = d.getDate();
		d.setDate(d.getDate() - 30)
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t;
		s +='-';
		s +=(day > 9 ? "" : "0") + day;
		return s;
	}
	/**
	 * 获取当前后一月的日期
	 */
	function getCurrentDateTo() {
		var d, s, t;
		d = new Date();
		var day = d.getDate();
		d.setDate(d.getDate() + 30)
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t;
		s +='-';
		s +=(day > 9 ? "" : "0") + day;
		return s;
	}
	/**
	 * textField显示时间比较方法
	 */
	function compareDate(argDate1, argDate2) {
		return argDate1.getTime() >= argDate2.getTime();
	}
	
	// 分页时每页显示记录条数
	var PAGE_SIZE = Constants.PAGE_SIZE;
    
    // ********** 主画面******* //
    var wd = 180;
    
    //起始时间选择器
    var fromDate = new Ext.form.DateField({
        id : 'beginDate',
        name : 'beginDate',
//        format : 'Y-m-d',
        width : 100,
        allowBlank : false,
        readOnly : true,
        value : getCurrentDateFrom(),
        fieldLabel : '起始时间'
    });

    
    //结束时间选择器
    var toDate = new Ext.form.DateField({
        id : 'endDate',
        name : 'endDate',
        format : 'Y-m-d',
        width : 100,
        allowBlank : false,
        readOnly : true,
        value : getCurrentDateTo(),
        fieldLabel : '结束时间'
    });
    //采购单编号
    var purchaseNumber = new Ext.form.TextField({
        id : 'purchaseNumber',
        fieldLabel : '采购单编号',
        width : 100
    });

    
function chosePurchaser() {
		var args = {
			selectModel : 'single',
			rootNode : {
				id : '0',
				text : '合肥电厂'
			}
		}
		var pur = window
				.showModalDialog(
						'../../comm/purchaserForSelect.jsp',
						args,
						'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (typeof(pur) != "undefined") {
			purchaser.setValue(pur.names);
			purchaserH.setValue(pur.codes);
		}
	}
    // 采购员
	var purchaser = new Ext.form.TriggerField({
				fieldLabel : '采购员',
				// width : 108,
				id : "purchaser",
				displayField : 'text',
				valueField : 'id',
				hiddenName : 'mrDept',
				blankText : '请选择',
				emptyText : '请选择',
				maxLength : 100,
				anchor : '90%',
				readOnly : true,
				allowBlank : false
			});
	purchaser.onTriggerClick = chosePurchaser;
	var purchaserH = new Ext.form.Hidden({
				hiddenName : 'mrDept'
			})

    // 采购人
	var purBy = new Ext.form.TextField({
				fieldLabel : '申请人员',
				readOnly : true,
				maxLength : 100,
				width : 108,
				id : "mrBy",
				name : "mrBy"
			});
	purBy.onClick(selectPersonWin);
	var purByH = new Ext.form.Hidden({
	   value : ''
	});
	purByH.on('render', getUserName);
    



	//定义选择行
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
        },{
        	//最后修改人
        	name : 'lastModifiedBy'        
        },{
        	//工程项目
            name : 'project'
        },{
        	//最后修改人姓名
            name : 'lastModifiedByName'
        }]);

    // grid的store
    var queryStore = new Ext.data.JsonStore({
        url : 'resource/getPurchaseOrderList.action',
        root : 'list',
        totalProperty : 'totalCount',
        fields : gridRecord
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
    	region : 'north',
    	layout : 'column',
        store : queryStore,
        height:300,
        //autoHeight : true,
        columns : [new Ext.grid.RowNumberer({
					header : '行号',
					width : 35
				}),{
				    header : "制定日期",
				    sortable : true,
				    width : 100,
				    dataIndex : 'orderModifyDate'
				}, {
                    header : "采购单编号",
                    sortable: true,
                    width : 100,
                    dataIndex : 'purNo'
                }, {
                    header : "合同号",
                    sortable: true,
                    width : 100,
                    dataIndex : 'contractNo'
                },{
                    header : "采购人",
                    sortable: true,
                    width : 80,
                    dataIndex : 'buyerName'
                },{
                	// 供应商Id
                	header : "供应商编号",
                	width : 150,
                	hidden : true,
                    dataIndex : 'supplier'
                }, {
                	// 供应商名称
                	header : "供应商名称",
                	width : 100,
                    dataIndex : 'supplyName'
                },{
                   header : "申购单位",
                   sortable : true,
                   width : 100,
                   hidden : true
                },{
                	header : "最后修改人编号",
                	sortable : true,
                	width : 80,
                	hidden : true,
                	dataIndex : 'lastModifiedBy'
                },{
                	header : "最后修改人",
                	sortable : true,
                	width : 80,
                	hidden : true,
                	dataIndex : 'lastModifiedByName'
                },{
                    header : "单位工程编号",
                    sortable: true,
                    width : 270,
                    hidden : true,
                    dataIndex : 'project'
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
                	header : "采购单状态",
                	width : 100,
                    hidden : true,
                    dataIndex : 'purStatus'
                }, {
                	// 人员Id
                    hidden : true,
                    dataIndex : 'buyer'
                },  {
                	// 采购订单表.上次修改日期
                    hidden : true,
                    dataIndex : 'orderModifyDate'
                },{
                	// 备注
                	header : "备注",
                	width : 200,
                    hidden : false,
                    dataIndex : 'orderMemo'
                }],
        tbar:  [{text : '起始时间'},
               fromDate,
               { text : '结束时间'},
               toDate,
               { text : '采购人'},purchaser,
//               purBy,
               { text : '采购单编号'},purchaseNumber,
               {
	            id : 'btnSubmit',
	            text : "查询",
	            iconCls : Constants.CLS_OK,
	            handler : findFuzzy
            }],       
        bbar : pagebar,
        sm : sm,
        frame : false,
        border : false,
        enableColumnMove : false
    });
    // todo  双击改点击，查明细
    gridOrder.on('rowclick', showDetail);
    gridOrder.on('render', function() {
	    // 初始化Grid
	    gridFresh();
    });
    
     var detailRecord = new Ext.data.Record.create([{
           // 采购单流水号
            name : 'orderId'
        }, {
            // 采购单编号
            name : 'purNo'
        }, {
            // 采购单状态
            name : 'purStatus'
        }, {
            // 物料Id
            name : 'materialId'
        }, {
            //物料编码
            name : 'materialNo'
        }, {
            // 物料名称
            name : 'materialName'
        }, {
            // 规格型号
            name : 'specNo'
        },{
            // 材质/参数
            name : 'parameter'
        },
        	{
            // 单价
            name : 'unitPrice'
           },
           {
             //币别
           	name : 'currencyName'
           },
        	{
            // 采购数量
            name : 'purQty'
        }, {
            // 已收数量
            name : 'rcvQty'
        },
        	{
            // 暂收数量
            name : 'insQty'
           },
        {
            // 采购订单明细表.交期
            name : 'orderDetailsDueDate'
        },
        	{
            // 明细备注
            name : 'orderDetailsMemo'
         }
        ,{
            // 申请单项次号
            name : 'requirementDetailId'
         }
         ,{
            //采购订单明细项次号
         	name : 'orderDetailsId'
//         	name : 'purOrderDetailsId'ORDER_DETAILS_ID
         }
         ]);
      // 物料grid的store
	var detailStore = new Ext.data.JsonStore({
				url : 'resource/getPurchaseDetailList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : detailRecord
			});
	// 物料grid
	var materialGrid = new Ext.grid.GridPanel({
				region : 'center',
				border : false,
				autoScroll : true,
				enableColumnMove : false,
				store : detailStore,
				columns : [new Ext.grid.RowNumberer({
									header : "行号",
									width : 35
								}),
						// 采购单流水号
						{
							header : "采购单流水号",
							width : 100,
							sortable : true,
							hidden : true,
							dataIndex : 'orderDetailsId'
						},
						// 采购单编号
						{
							header : "采购单编号",
							width : 100,
							sortable : true,
							dataIndex : 'purNo'
						},
						// 申请单项次号
						{
							header : "申请单项次号",
							width : 100,
							sortable : true,
							hidden:true,
							dataIndex : 'requirementDetailId'
						},
						// 采购订单项次号
						{
							header : "采购订单明细项次号",
							width : 100,
							sortable : true,
							hidden : true,
							dataIndex : 'orderDetailsId'
						},// 采购单状态
						{
							header : "采购单状态",
							width : 100,
							sortable : true,
							hidden : true,
							dataIndex : 'purStatus'
						},// 物料编码
						{
							header : "物料编码",
							width : 100,
							sortable : true,
							dataIndex : 'materialNo'
						},// 物料名称
						{
							header : "物料名称",
							width : 100,
							sortable : true,
							dataIndex : 'materialName'
						},// 规格型号
						{
							header : " 规格型号",
							width : 100,
							sortable : true,
							dataIndex : 'specNo'
						},// 材质/参数
						{
							header : " 材质/参数",
							width : 100,
							sortable : true,
							dataIndex : 'parameter'
						},// 采购数量
						{
							header : "采购数量",
							width : 100,
							sortable : true,
							align : 'right',
							renderer : moneyFormat,
							dataIndex : 'purQty'
						},// 已收数量
						{
							header : "已收数量",
							width : 100,
							sortable : true,
							align : 'right',
							renderer : moneyFormat,
							dataIndex : 'rcvQty'
						},// 暂收数量
						{
							header : "暂收数量",
							width : 100,
							sortable : true,
							align : 'right',
							renderer : moneyFormat,
							dataIndex : 'insQty'
						},//单价
						{
						   header : "单价",
						   width : 100,
						   sortable : true,
						   align : 'right',
						   renderer : function(value, params, record,row){
						   	if(record.data.purNo != null && record.data.purNo != '')
						   		return numberFormat(value)
						   	else
						   		return '';
						   },
						   dataIndex : 'unitPrice'
						},//币别
						{
						   header : "币别",
						   width : 100,
						   dataIndex : 'currencyName'
						},
						// 需求日期
						{
							header : "需求日期",
							width : 100,
							sortable : true,
							dataIndex : 'orderDetailsDueDate',
							renderer : function(value){
								if(value!=null&&value!="")
							     value = value.substring(0,10);
							     return value;
							}
						},// 明细备注
						{
							header : "明细备注",
							width : 150,
							sortable : true,
							dataIndex : 'orderDetailsMemo'
						}]
			});
			
    // 主框架
    new Ext.Viewport({   
        layout : 'border',
		margins : '0 0 0 0',
		border : false,
        items : [gridOrder,materialGrid]
    });
    // ↑↑********** 主画面*******↑↑//

    // ↓↓*********处理***********↓↓//
    function findFuzzy(start) {
		var startDate = fromDate.getValue();
		var endDate = toDate.getValue();
        if(start > 0){
           start = start; 
        }else{
            start=0;
        }
		if (startDate != "" && endDate != "") {
			var res = compareDate(startDate, endDate);
			if (res) {
				Ext.Msg.alert(Constants.SYS_REMIND_MSG, String.format(
								Constants.COM_E_006, "起始时间", "结束时间"));

			}
			else {
				gridFresh();
       	// 清空明细部分gird
				detailStore.removeAll();
			}
		}
	}
//---------add by fyyang 090721----------------------------
			detailStore.on("load", function() {

				var totalPurQty = 0; // 采购数量
				var totalRcvQty = 0; // 已收数量
				var totalInsQty = 0; // 暂收数量
				var totalUnitPrice = 0;
				for (var j = 0; j < detailStore.getCount(); j++) {
					var temp = detailStore.getAt(j);
					if (temp.get("purQty") != null) {
						totalPurQty = parseFloat(totalPurQty)
								+ parseFloat(temp.get("purQty"));
					}
					if (temp.get("rcvQty") != null) {
						totalRcvQty = parseFloat(totalRcvQty)
								+ parseFloat(temp.get("rcvQty"));
					}
					if (temp.get("insQty") != null) {
						totalInsQty = parseFloat(totalInsQty)
								+ parseFloat(temp.get("insQty"));
					}
					if (temp.get("unitPrice") != null) {
						totalUnitPrice = parseFloat(totalUnitPrice)
								+ parseFloat(temp.get("unitPrice"));
					}
				}
               if(detailStore.getCount()>0)
               {
				var mytotal = new detailRecord({
					orderDetailsId : null,
					purNo : '',
					materialId : null,
					purQty : totalPurQty.toFixed(2),
					rcvQty : totalRcvQty.toFixed(2),
					insQty : totalInsQty.toFixed(2),
					unitPrice : null//totalUnitPrice.toFixed(2)

				});
				detailStore.add(mytotal);
               }

			});
	//-----------------------------------------------------------------		
     /**
	 * 当单击主grid中一行时，下面的grid显示详细信息
	 */
	function showDetail() {
		if (gridOrder.getSelectionModel().getSelected() != null) {
		   
			var record = gridOrder.getSelectionModel().getSelected();
			detailStore.load({
						params : {
							purNo : record.get('purNo')
						}
					});
					
			 
		}

	}
   
    
    // 根据条件加载Grid
    function gridFresh() {
//    	alert(fromDate.getValue());
        queryStore.baseParams = {
            queryTimeFrom : Ext.get("beginDate").dom.value,
            queryTimeTo :   Ext.get("endDate").dom.value,
//            buyer : purByH.getValue(),
            buyer : purchaserH.getValue(),
            purNo : purchaseNumber.getValue()
        };
        queryStore.load({
        	params:{
        		start : 0,
        		limit : PAGE_SIZE
    		}
		});
    	
    }
    
    // 重新加载Grid中的数据
    function gridReload() {
        queryStore.reload();
    }
    // ↑↑*********处理***********↑↑//
    /**
	 * 获取申请人的姓名和工号
	 */
	function getUserName() {
		if (purBy.getValue() == "" && purH.getValue() == "") {
			Ext.Ajax.request({
						url : 'resource/getInfo.action',
						method : 'post',
						success : function(action) {
							var result = eval("("+action.responseText+")");
                            if(result!=""&&result!=null){
							 purBy.setValue(result.workerName);
							 purByH.setValue(result.workerCode);
                            }
                            firstLoad();
						}
					});
		}
	}
   /**
     * 人员选择画面处理
     */
    function selectPersonWin(){
        var args = {
                selectModel : 'signal',
                notIn : "",
                rootNode : {
                    id : '-1',
                    text : '合肥电厂'
                }
            } 
         var person = window.showModalDialog('../../../../comm/jsp/hr/workerByDept/workerByDept.jsp',
                args, 'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
        if (typeof(person) != "undefined") {
            purBy.setValue(person.workerName);
            purByH.setValue(person.workerCode);
        }
    }
    
    
    
});