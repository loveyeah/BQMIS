// 采购单登记Constants
Constants.RB002_E_002 = "拆分数量必须小于待采购数。请确认。"

Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
    Ext.QuickTips.init();
    
    var orderRegister = window.dialogArguments.orderRegister;
    var firstLoad = 0;
  
    function renderNumber(v, argDecimal) {
		if (v) {
			if (typeof argDecimal != 'number') {
				argDecimal = 2;
			}
			v = Number(v).toFixed(argDecimal);
			var t = '';
			v = String(v);
			while ((t = v.replace(/^(-?[0-9]+)([0-9]{3}.*)$/, '$1,$2')) !== v)
				v = t;
			
			return v;
		} else
			return '';
	}
    // ↓↓**********************计划单拆分/合并********************↓↓//
    
    // 是否显示所有
    var ckbShowAll = new Ext.form.Checkbox({
        id : 'ckbShowAll',
        boxLabel : "显示所有",
        width : 140,
        hideLabel : true,
        listeners : {
            check : showAllHandler
        }
    })

    // ↑↑**********************需求计划物资********************↑↑//
    var planRecord = Ext.data.Record.create([{
	        // 物料编码
	        name : 'materialNo'
	    }, {
	        // 物料ID
	        name : 'materialId'
	    }, {
	        // 计划单号
	        name : 'requirementHeadId'
	    }, {
	        // 计划单项次号
	        name : 'requirementDetailId'
	    }, {
	        // 核准数量
	        name : 'approvedQty'
	    }, {
	        // 已采购数
	        name : 'planPurQty'
	    }, {
	        // 待采购数
	        name : 'needQty'
        }, {
            // 是否是其它采购员的需求计划物资
            name : 'isOtherPlan'
	    }, {
	        // 采购订单与需求计划关联表.上次修改日期
	        name : 'planRelateModifyDate'
        }, {
            // 物资需求计划明细表.上次修改日期
            name : 'planModifyDate'
    }
    ,{name:'materialName'} //物资名称
    ,{name:'specNo'} //规格型号
    ,{name:'parameter'} //材质/参数
    ,{name:'quotedPrice'} //单价
    ,{name:'supplier'} //供应商
    ,{name:'supplyName' } //供应商名称
    ,{name:'supplierNo'}
    ,{name : 'stockUmName'} // add by liuyi 091127 单位名
    ,{name:'orderDetailsMemo'}// 明细备注
    ,{name: 'sbDeptName'} // 申报部门
    ,{name: 'sbMemo'} // 申报时的明细
    ]);
	
    // grid列模式
    var planCM = new Ext.grid.ColumnModel([
    	new Ext.grid.RowNumberer({
			header : '行号',
			width : 35
		}), {
	        header : '物料编码',
	        width : 90,
	        align : 'center',
	        dataIndex : 'materialNo',
	        sortable : false
	    },  {
	        header : '物料名称',
	        width : 60,
	        dataIndex : 'materialName',
	        sortable : false
	    },{
	        header : '规格型号',
	        width : 60,
	        dataIndex : 'specNo',
	        sortable : false
	    },{
	        header : '材质/参数',
	        width : 60,
	        dataIndex : 'parameter',
	        sortable : false
	    },{
	        header : '单位',
	        width : 60,
	        hidden : true,
	        dataIndex : 'stockUmName',
	        sortable : false
	    },{
	        header : '物料Id',
	        hidden: true,
	        dataIndex : 'materialId',
	        sortable : false
	    }, {
	        header : '计划单号',
	        width : 65,
	         hidden: true,
	        dataIndex : 'requirementHeadId',
	        sortable : false
	    }, {
	        header : '计划单项次号',
	        width : 80,
	         hidden: true,
	        dataIndex : 'requirementDetailId',
	        sortable : false
	    }, {
	        header : '核准数量',
	        width : 60,
	        renderer : renderNumber,
	        dataIndex : 'approvedQty',
	        sortable : false
	    }, {
	        header : '已采购数',
	        width : 60,
	        renderer : renderNumber,
	        dataIndex : 'planPurQty',
	        sortable : false
	    }, {
	        header : '待采购数',
	        width : 60,
	        renderer : renderNumber,
	        dataIndex : 'needQty',
	        sortable : false
        }, {
            // 是否是其它采购员的需求计划物资
            hidden : true,
            dataIndex : 'isOtherPlan',
	        sortable : false
        }, {
            // 采购订单与需求计划关联表.上次修改日期
            hidden : true,
            dataIndex : 'planRelateModifyDate',
	        sortable : false
        }, {
            // 物资需求计划明细表.上次修改日期
            hidden : true,
            dataIndex : 'planModifyDate',
	        sortable : false
    },
    {
    	//单价
    	header : '报价',
    	 width : 60,
    	 align : 'center',
    	  dataIndex : 'quotedPrice',
	        sortable : false
    },
    {
    	//供应商
    	header : '供应商',
    	 align : 'center',
    	  dataIndex : 'supplyName',
	        sortable : false
    },
    {
    	//需求备注
    	header : '需求备注',
    	 align : 'center',
    	  dataIndex : 'sbMemo'
    },
    {
    	//申报部门
    	header : '申报部门',
    	 align : 'center',
    	  dataIndex : 'sbDeptName'
    }
    ]);
    planCM.defaultSortable = true;
    // ↓↓**********************需求计划物资Grid************↓↓//
    // 数据源
    var planStore = new Ext.data.Store({
        proxy : new Ext.data.MemoryProxy({
        	list: orderRegister.planOrderDatas,
        	totalCount: orderRegister.planOrderDatas.length
        	}),
        reader : new Ext.data.JsonReader({
        	root : 'list',
        	totalProperty : 'totalCount'
        }, planRecord)
    });
    planStore.sortData = function(f, direction){
        direction = direction || 'ASC';
        var fn = function(r1, r2){
        	if (r1.data.isOtherPlan != r2.data.isOtherPlan) {
        		return r1.data.isOtherPlan ? 1 : -1;
        	}
        	// 物料编码
            if (r1.data.materialNo > r2.data.materialNo) {
            	return 1;
            }
            if (r1.data.materialNo < r2.data.materialNo) {
            	return -1;
            }
            // 计划单号
            if (r1.data.requirementHeadId > r2.data.requirementHeadId) {
            	return 1;
            }
            if (r1.data.requirementHeadId < r2.data.requirementHeadId) {
            	return -1;
            }
            // 计划单项次号
            if (r1.data.requirementDetailId > r2.data.requirementDetailId) {
            	return 1;
            }
            if (r1.data.requirementDetailId < r2.data.requirementDetailId) {
            	return -1;
            }
        	return 0;
        };
        this.data.sort(direction, fn);
        if(this.snapshot && this.snapshot != this.data){
            this.snapshot.sort(direction, fn);
        }
    }
    planStore.setDefaultSort('materialNo', 'asc');
    
    // grid
    var planGrid = new Ext.grid.GridPanel({
		enableColumnMove : false,
		enableColumnHide : true,
        ds : planStore,
        cm : planCM,
        sm : new Ext.grid.RowSelectionModel(),
        border : false,
//        frame : true,
        autoScroll : true
    });
    planGrid.on('mouseover', function() {
    	return false;
    });
    planGrid.on('rowclick', function() {
    	var records = planGrid.getSelectionModel().getSelections();
		var diabledFlag = records && records.length > 1;
		
		Ext.getCmp('btnMeasure').setDisabled(diabledFlag);
    });
    planGrid.on('rowmousedown', function(grid, rowIndex, e) {
    	if (planStore.getAt(rowIndex).data.isOtherPlan) {
    		return false;
    	}
    	return true;
    });
    // ↑↑**********************需求计划物资Grid************↑↑//
	
    // ↓↓**********************采购订单明细************↓↓//
    var detailRecprd = Ext.data.Record.create([{
	        // PO单明细项次号
	        name : 'orderDetailsId'
	    }, {
	        // 物料编码
	        name : 'materialNo'
	    }, {
	        // 物料Id
	        name : 'materialId'
	    }, {
	        // 数量
	        name : 'purQty'
    }
     ,{name:'quotedPrice'} //单价
    ,{name:'supplier'} //供应商
    ,{name:'supplyName' } //供应商名称
      ,{name:'supplierNo'} //供应商编号
     ,{name:'orderDetailsMemo'}// 明细备注
    ,{name: 'sbDeptName'} // 申报部门
    ,{name: 'sbMemo'} // 申报时的备注
    ]);

    var detailCM = new Ext.grid.ColumnModel([
    	new Ext.grid.RowNumberer({
			header : '行号',
			width : 35
		}), {
	        header : 'PO单明细项次号',
	        dataIndex : 'orderDetailsId',
	        width : 100,
	        sortable: true
	    }, {
	        header : '物料编码',
	        dataIndex : 'materialNo',
	        width : 80,
	        sortable: true
	    }, {
	        header : '数量',
	        dataIndex : 'purQty',
	        renderer : renderNumber,
	        width : 48,
	        sortable: true
	    }, {
	        header : '物料Id',
	        dataIndex : 'materialId',
	        hidden : true
    }, {
	        header : '报价',
	        dataIndex : 'quotedPrice'
    }, {
	        header : '供应商',
	        dataIndex : 'supplyName'
    },
    {
    	//需求备注
    	header : '需求备注',
    	 align : 'center',
    	  dataIndex : 'sbMemo'
    },
    {
    	//申报部门
    	header : '申报部门',
    	 align : 'center',
    	  dataIndex : 'sbDeptName'
    }
    
    ]);
    detailCM.defaultSortable = true;
    // 数据源
    var detailStore = new Ext.data.Store({
        proxy : new Ext.data.MemoryProxy(orderRegister.orderDetailsData['planed']),
        reader : new Ext.data.JsonReader(null, detailRecprd)
    });
    
    // 待选作业方式grid
    var detailGrid = new Ext.grid.GridPanel({
		enableColumnMove : false,
		enableColumnHide : true,
        ds : detailStore,
        cm : detailCM,
        sm : new Ext.grid.RowSelectionModel({
			singleSelect : true
		}),
        border : false,
//        frame : true,
        autoScroll : true
    });
    // ↑↑**********************采购订单明细************↑↑//

    // ↓↓**********************中间按钮************↓↓//
    // 添加成员button
    var btnToRight = new Ext.Button({
        id : 'btnGrant',
        height : 80,
        text : " >>  ",
        style:"margin-left:12;margin-top:180",
        handler : function() {
        	var records = planGrid.getSelectionModel().getSelections();
    		if (!records || records.length < 1) {
        		// 如果没有选择显示提示信息
	    		Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_001);
	    		return;
        	}
        	
        	for (var i = records.length - 1; i>=0; i--) {
	        	var record = records[i].data;
	        	if (record.isOtherPlan) {
	        		// 对应的物料或者分类不是当前采购员的
		    		Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.RB002_E_001);
		    		return;
	        	}
	            // 右移 ->>
	        	orderRegister.rightMove(record);
	        	planStore.remove(records[i]); //modify by fyyang 090730
        	}
        	
        	// 刷新
        	//planStore.reload(); 
        	
        	detailStore.reload();
        }
    });
    // 去除成员button
    var btnToLeft = new Ext.Button({
        id : 'btnRevoke',
        text : " <<  ",
        style:"margin-left:12;margin-top:30;",
        handler : function() {
        	var records = detailGrid.getSelectionModel().getSelections();
    		if (!records || records.length < 1) {
        		// 如果没有选择显示提示信息
	    		Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_001);
	    		return;
        	}
        	// 物料编码
        	var materialId = records[0].data.materialId;
            // 左移 <<-
        	orderRegister.leftMove(materialId);
        	// 刷新Grid
        	//planStore.reload();  
        	queryRecord(); //modify by fyyang 090730
        	detailStore.reload();
        }
    });
    // ↑↑**********************中间按钮************↑↑//
    //-------add by fyyang 090730--增加按物资名称查询------
      
    var  txtMaterialName=new Ext.form.TextField({
    	name:'txtMaterialName',
    	fieldLabel:'物资名称'
    });
    
    function queryRecord() {
		// 按物料名称查询
		planStore.reload();
		var mName = txtMaterialName.getValue();
		for (var i = 0; i < planStore.getCount();) {
			var myMaterialName = planStore.getAt(i).get("materialName");
			if (myMaterialName.indexOf(mName) == -1) {
				planStore.remove(planStore.getAt(i));
			} else {
				i++;
			}

		}
	}
    // --------------------------------------------------
    var top = new Ext.Panel({
        region : 'north',
        border : true,
        layout : 'column',
        height: 27,
        title : '',
        items : [{
    			columnWidth : 0.55,
				layout : 'form',
				border : false,
				tbar : [ckbShowAll,
				 //--------add by fyyang 090730----------
				   '物资名称：',txtMaterialName,
				   {
				   	text:'查询',
				   	iconCls:'query',
				   	handler: queryRecord
				   
				   },
				//---------------------------------------
				'->', {
		            id : 'btnMeasure',
		            text : "拆分",
		            handler : function() {
		            	var records = planGrid.getSelectionModel().getSelections();
    					if (!records || records.length < 1) {
			        		// 如果没有选择显示提示信息
				    		Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_001);
				    		return;
			        	}
			        	myaddpanel.getForm().reset();
			        	win.show();
		            }
	            }]
    		}, {
    			columnWidth : 0.45,
				layout : 'form',
				border : false,
				tbar : ['->', {
		            id : 'btnSubmit',
		            text : "确认",
		            handler : function() {
		            	//add by fyyang  --------------判断选择的物资是不是同一个供应商-----
	if(detailStore.getCount()>0)
	{
    var firstSupplier=  "";
    var mynum=0;
    for(var i=0;i<detailStore.getCount();i++)
    {
    		if(detailStore.getAt(i).get("supplier")!=null &&detailStore.getAt(i).get("supplier")!="")
    		{
    			 if(firstSupplier=="")
    			 {
    			 	firstSupplier=detailStore.getAt(i).get("supplier");
    			 }
    			 else
    			 {
    			 	  if(firstSupplier!=detailStore.getAt(i).get("supplier") )
    			 	  {
    			 	  		Ext.Msg.alert("提示","请选择从同一供应商采购的物资！");
    	                                     return ;
    			 	  }
    			 }
    	
    		}
    
	}
	}
    //-----------------------------------------------------------------
		            	window.returnValue = {};
		            	// 确定
		            	window.close();
		            }
	            }]
    		}]
    });
 
    var left = new Ext.Panel({
        region : 'west',
        layout : 'fit',
        border : false,
        width : 650,
        collapsible : true,
         style:"border-left:1px solid;border-bottom:1px solid;",
        items : [planGrid]
    });
    
    var mid = new Ext.Panel({
        layout : "form",
        region : "center",
//        frame : true,
        border : false,
         style:"border-left:1px solid;border-right:1px solid;border-bottom:1px solid;",
        items : [btnToRight, btnToLeft]
    });
    var right = new Ext.Panel({
        region : "east",
        layout : 'fit',
        width : 285,
        collapsible : true,
        border : false,
        style:"border-right:1px solid;border-bottom:1px solid;",
        items : [detailGrid]
    });

    new Ext.Viewport({
        enableTabScroll : true,
        layout : "border",
        items : [top, left, mid, right]
    });
    
    planStore.on('load', function(store, records) {
    	var diabledFlag = !records || records.length < 1;
    	btnToRight.setDisabled(diabledFlag);
    	Ext.getCmp('btnMeasure').setDisabled(diabledFlag);
    	if (orderRegister.otherPlanOrderDatas.length == records.length) {
    		Ext.getCmp('btnMeasure').setDisabled(true);
    	}
    	
    	firstLoad++;
    	if (diabledFlag && firstLoad < 2) {
    		Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_003);
    	}
    	
    	if (!diabledFlag && ckbShowAll.checked) {
    		for (var i = records.length - 1, j = orderRegister.otherPlanOrderDatas.length - 1; j>=0; i--,j--) {
				planGrid.getView().getRow(i).style.backgroundColor = 'darkgray';
    		}
    	}
    });
    planStore.load();
    
    detailStore.on('load', function(store, records) {
    	var diabledFlag = !records || records.length < 1;
    	btnToLeft.setDisabled(diabledFlag);
    	
    	firstLoad++;
    	if (diabledFlag && firstLoad < 2) {
    		Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_003);
    	}
    });
    detailStore.load();
    // ========        弹出画面        ============
    // 拆分数量
    var nfMeasureNum = new Powererp.form.NumField({
        id : "nfMeasureNum",
        fieldLabel : '拆分数量',
        allowNegative : false,
        width : 180,
        style : {
        	'text-align' : 'right'
        },
        decimalPrecision : 2,
        padding : 2,
        maxLength : 9
    });
    
    // 弹出窗口panel
    var myaddpanel = new Ext.FormPanel({
        frame : true,
        labelAlign : 'right',
        labelWidth : 70,
        items : [nfMeasureNum]
    });

    // 弹出窗口
    var win = new Ext.Window({
        width : 300,
        height : 130,
        title : '需求数量拆分',
        buttonAlign : "center",
        resizable : false,
        modal : true,
        layout : 'fit',
        closeAction : 'hide',
        items : [myaddpanel],
        buttons : [{
            text : Constants.CONFIRM,
            handler : function() {
                if (!myaddpanel.getForm().isValid()) {
                    return false;
                }
                var records = planGrid.getSelectionModel().getSelections();
                record = records[0].data;
    			
                var num = nfMeasureNum.getValue();
                if (num < 0 || num > record.needQty) {
                	Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.RB002_E_002);
		    		return;
                }
                // 拆分
                orderRegister.disassemble(record, nfMeasureNum.getValue());
                
                // 刷新Grid
        		planStore.reload();
                win.hide();
            }
        }]
    });
    
    function showAllHandler(ckb, value) {
    	orderRegister.showAllChecked = value;
    	if (value) {
    		// 是否已加载其它采购员的需求计划物资
	    	if (orderRegister.isOtherPlanLoaded){
	    		Array.prototype.push.apply(orderRegister.planOrderDatas, orderRegister.otherPlanOrderDatas);
	    	} else {
	    		// 更新请求参数
	    		orderRegister.checkParams();
	    		
	    		orderRegister.isOtherPlanLoaded = true;
	    		// 取得需求计划明细数据
		    	Ext.Ajax.request({
		            method : Constants.POST,
		            url : 'resource/getRegisterOtherPlans.action',
		            success : function(result, request) {
		            	if (result.responseText) {
		                	var records = eval('(' + result.responseText + ')');
		                	records = [].concat(records);
		                	var record = null;
		                	var plan = null;
		                	for (var intCnt = 0; intCnt < records.length; intCnt++) {
		                		record = records[intCnt];
		                		// 采购订单与需求计划关联表.申请单项次号
		                		if (!record.requirementDetailId) {
		                			continue;
		                		}
		                		
		                		orderRegister.addOtherPlan(record);
		                	}
		                	
		                	Array.prototype.push.apply(orderRegister.planOrderDatas, orderRegister.otherPlanOrderDatas);
							
		                	if (!ckbShowAll.checked) {
		                		// 清空其它采购员的需求计划物资
    							orderRegister.clearOtherPlans();
		                	}
					    	// 刷新Grid
							planStore.reload();
							return;
		            	}
		            },
		            failure : function() {
		                Ext.Msg.alert(Constants.ERROR, '加载数据失败!');
		            },
		            params : {
		            	buyer : orderRegister.orderData.buyer,
		            	supplier : orderRegister.orderData.supplier
		        	}
		        });
	    	}
    	} else {
    		// 清空其它采购员的需求计划物资
    		orderRegister.clearOtherPlans();
    	}
    	
    	// 刷新Grid
		planStore.reload();
    }
    
    // 初始化时设置'显示所有'CheckBox的状态
    function isInitShowAllChecked() {
    	if (orderRegister.showAllChecked) {
			ckbShowAll.setValue(true);
			return;
    	}
    	ckbShowAll.setValue(false);
    }
    
    // 初始化时设置'显示所有'CheckBox的状态
    isInitShowAllChecked();
});
