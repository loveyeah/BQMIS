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
        },{
            // 需求计划明细ID
            name : 'requirementDetailId'
        },
        //申请人
        {name:'applyByName'},
        //申请部门
        {name:'applyDeptName'},
        //申请原因
        {name:'applyReason'},
        //申请原因
        {name:'mrDate'},
        //申请原因
        {name:'planOriginalId'},
        // add by ywliu 20091030
        //需求日期
        {name:'dueDate'},
        //建议供应商
        {name:'supplier'},
        //备注信息
        {name:'memo'},
        //填写人
        {name:'entryBy'}
        // add by ywliu 20091030 End
        // add by liuyi 091109 是否退回
        ,{
        	name : 'isReturn'
        }
        // add by liuyi 091109 退回原因
        ,{
        	name : 'returnReason'
        },{name:'factory'}
        ]);
        
     // 是否免检 显示为Checkbox
    var ckcQaControlFlag = new Ext.grid.CheckColumn({
        header : "是否免检",
        dataIndex : 'qaControlFlag',
        width : 100
    });
    
    var smAnnex = new Ext.grid.CheckboxSelectionModel({
    });
    


	var dataProxy = new Ext.data.HttpProxy({
		url : 'resource/getMRPMaterialDetail.action'
	});

	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	}, gridRecord);

	var store = new Ext.data.Store({
		proxy : dataProxy,
		reader : theReader
	});
     


	var sm = new Ext.grid.RowSelectionModel({
				singleSelect : true
			});
			
  
			
	var btnSign = new Ext.Toolbar.Button({
		id : 'btnSign',
		style : "margin-left:100px",
		text : "汇总(分发)",
		iconCls : 'add',
		menuAlign : 'center',
		handler : addGatherInfo
	});		

//    
//	// 仓库 DataStore
//    var dsDelayStore = new Ext.data.JsonStore({
//        root : 'list',
//        url : "resource/getWarehouseList.action",
//        fields : ['whsNo', 'whsName']
//    });
//    
//	// 仓库
//    dsDelayStore.load();
//    dsDelayStore.on('load', function() {
//        if(dsDelayStore.getTotalCount() > 0) {
//            var recordLocation = dsDelayStore.getAt(0);
//            dsDelayStore.remove(recordLocation);
//            }
//    })
//    
//    // 仓库组合框
//    var cboDelayStore = new Ext.form.ComboBox({
//        fieldLabel : "仓库",
//        name : "delayStore",
//        width : 150,
//        store : dsDelayStore,
//        displayField : "whsName",
//        valueField : "whsNo",
//        mode : 'local',
//        triggerAction : 'all',
//        readOnly : true
//    });
	var txtBuyer=new Ext.form.TextField({
	    name:'txtBuyer',
	    fieldLabel:'采购员'
	});
	function chosebuyer() {
		var args = {
			selectModel : 'single',
			rootNode : {
				id : '0',
				text : '合肥电厂'
			}
		}
		var dept = window
				.showModalDialog(
						'../../comm/purchaserForSelect.jsp',
						args,
						'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (typeof(dept) != "undefined") {
			txtBuyer.setValue(dept.names);
			txtBuyerHide.setValue(dept.codes);
			
             //queryRecord();
		}
	}
    // 采购员
	var txtBuyer = new Ext.form.TriggerField({
				fieldLabel : '采购员',
				id : "mytxtBuyer",
				displayField : 'text',
				valueField : 'id',
				hiddenName : 'txtBuyer',
				blankText : '请选择',
				emptyText : '请选择',
				maxLength : 100,
				anchor : '90%',
				readOnly : true,
				allowBlank : true
			});
	txtBuyer.onTriggerClick = chosebuyer;
	var txtBuyerHide = new Ext.form.Hidden({
				hiddenName : 'txtBuyerHide'
			})
	
		// 部门
	 function choseDept(){
        var args = {selectModel:'single',rootNode:{id:'0',text:'合肥电厂'}}
         var dept = window.showModalDialog('../../../../comm/jsp/hr/dept/dept.jsp',
                args, 'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
        if (typeof(dept) != "undefined") {
            txtMrDept.setValue(dept.names);
            txtMrDeptH.setValue(dept.codes);   
        }
    } 
	var txtMrDept = new Ext.form.TriggerField({
				fieldLabel : '部门',
				width : 108,
				id : "mrDeptId",
				displayField : 'text',
				valueField : 'id',
				hiddenName : 'mrDept',
				blankText : '请选择',
				emptyText : '请选择',
				maxLength : 100,
				anchor : '100%',
				readOnly : true
			});
    txtMrDept.onTriggerClick = choseDept;
    var txtMrDeptH =new Ext.form.Hidden({
        hiddenName : 'mrDept'
    })
    
    //add by fyyang 090807---增加计划作废和取消作废功能---------
    var backReason=new Ext.form.TextArea({
	id : "backReason",
		fieldLabel : '作废原因<font color ="red">*</font>',// modify by ywliu 20091010
		width : 200,
		name : 'backReason',
		height:80,
		allowBlank : false
	}); 
	var myaddpanel = new Ext.FormPanel({
		frame : true,
		labelAlign : 'center',
		labelWidth:80,
		closeAction : 'hide',
		title : '填写作废原因',
		items : [backReason] 
	});

	var win = new Ext.Window({
		width : 400,
		height : 180,
		buttonAlign : "center",
		items : [myaddpanel],
		layout : 'fit',
		closeAction : 'hide',
		resizable : false,
		modal : true,
		buttons : [{
			text : '保存',
			iconCls:'save',
			handler : function() {
				if(backReason.getValue() != null && backReason.getValue() != "") {// modify by ywliu 20091010
					var sm = grid.getSelectionModel();
					var selected = sm.getSelections();
					var menber = selected[0];
				    var	requirementDetailId=menber.get('requirementDetailId');
						Ext.Ajax.request({
							method : 'post',
							url : 'resource/blankOutPlanMaterial.action',
							params : {
								cancelReason : backReason.getValue(),
								requirementDetailId : requirementDetailId
							},
							success : function(action) {
								Ext.Msg.alert("提示", "作废成功！");
								win.hide();
								queryRecord();
							},
							failure : function() {
								Ext.Msg.alert(Constants.SYS_REMIND_MSG,
										Constants.COM_E_014);
							}
						});
				} else {
					Ext.Msg.alert("提示", "请先填写作废原因!");
				}
			}
		}, {
			text : '取消',
			iconCls:'cancer',
			handler : function() { 
				win.hide();
			}
		}]

	});
    function planOp()
    {
    	var sm = grid.getSelectionModel();
		var selected = sm.getSelections();
		var menber = selected[0];
		if(selected.length>1)
		{
			Ext.Msg.alert("提示", "请先选择一条记录!");
			return;
		}
		if (grid.selModel.hasSelection()) {
			Ext.Msg.confirm("提示", "是否确定作废?", function(buttonobj) {
				if (buttonobj == "yes") {
					myaddpanel.getForm().reset();
				  win.show();
				}
			})
		} else {
			Ext.Msg.alert("提示", "请先选择要作废的记录!");
		}
    }
    
    function cancelPlanOp()
    {
    	var flag=window.showModalDialog('MRPBlankOutList.jsp', '',
							'dialogWidth=800px;dialogHeight=400px;status=no');
         if(flag)
         {
		  queryRecord();
         }
    }
	//-----------------------------------------------------
    var headTbar = new Ext.Toolbar({
        region : 'north',
        height : 30,
     	border:false,
        items : [{xtype : 'tbspacer',width : 20},'-','申请部门：',txtMrDept,txtMrDeptH,{
			id : 'btnQuery',
			text : "查询",
			iconCls : 'query',
			handler :queryRecord
		},'-','分配给(采购员)：',txtBuyer,'-',btnSign,'-',{
		   text:'计划作废',
		   handler:planOp
		},'-',{
		 text:'取消作废',
		 handler:cancelPlanOp
		}]
    });
    
	var grid = new Ext.grid.GridPanel({
		region : "center",
        layout : 'fit',
        autoScroll : true,
        height : 410,
        frame : true,
        isFormField : false,
        border : true,
        anchor : "0",
        autoHeight : false,
        // 标题不可以移动
        enableColumnMove : false,
		store : store,
		columns : [smAnnex,new Ext.grid.RowNumberer({
                            header : "行号",
                            width : 35
                        }), {
            header : "物料ID<font color='red'>*</font>",
            sortable: true,
            width : 80,
            hidden : true,
            dataIndex : 'materialId'
        },{
            header : "物料编码<font color='red'>*</font>",
            sortable: true,
            width : 80,
            dataIndex : 'materialNo',
			renderer : function(v, cellmeta, record, rowIndex, columnIndex, store)
			{
				if(record.get('isReturn') == 'Y' && v != null)
						return "<font color=red>" + v + "</font>";
				else return v;
			}
        }, {
            header : "物料名称",
            sortable: true,
            width : 100,
            dataIndex : 'materialName',
			renderer : function(v, cellmeta, record, rowIndex, columnIndex, store)
			{
				if(record.get('isReturn') == 'Y' && v != null)
						return "<font color=red>" + v + "</font>";
				else return v;
			}
        },{
            header : "规格型号",
            sortable: true,
            width : 60,
            dataIndex : 'specNo',
			renderer : function(v, cellmeta, record, rowIndex, columnIndex, store)
			{
				if(record.get('isReturn') == 'Y' && v != null)
						return "<font color=red>" + v + "</font>";
				else return v;
			}
        },{
            header : "采购数量<font color='red'>*</font>",
            sortable: true,
            width : 60,
            align : 'right',
            dataIndex : 'approvedQty',
			renderer : function(v, cellmeta, record, rowIndex, columnIndex, store)
			{
				if(record.get('isReturn') == 'Y' && v != null)
						return "<font color=red>" + v + "</font>";
				else return v;
			}
        },  {
        	header : '计量单位',
            hidden : false,
            width : 100,
            dataIndex : 'stockUmName',
			 renderer : function(v, cellmeta, record, rowIndex, columnIndex, store)
			{
				if(record.get('isReturn') == 'Y' && v != null)
						return "<font color=red>" + v + "</font>";
				else return v;
			}
        },  // add by ywliu 20091030
        {
        	header : "需求日期",
			width : 100,
			sortable : true,
			dataIndex : 'dueDate',
			renderer : function(v, cellmeta, record, rowIndex, columnIndex, store)
			{
				if(record.get('isReturn') == 'Y' && v != null)
						return "<font color=red>" + v + "</font>";
				else return v;
			}
        },{
        	header : '申请人',
        	width : 100,
            hidden : false,
            dataIndex : 'applyByName',
			 renderer : function(v, cellmeta, record, rowIndex, columnIndex, store)
			{
				if(record.get('isReturn') == 'Y' && v != null)
						return "<font color=red>" + v + "</font>";
				else return v;
			}
        },{
            header : "材质/参数",
            sortable: true,
            width : 60,
            dataIndex : 'parameter',
			renderer : function(v, cellmeta, record, rowIndex, columnIndex, store)
			{
				if(record.get('isReturn') == 'Y' && v != null)
						return "<font color=red>" + v + "</font>";
				else return v;
			}
        },{
        	header : '申请部门',
        	width : 100,
            hidden : false,
            dataIndex : 'applyDeptName',
			 renderer : function(v, cellmeta, record, rowIndex, columnIndex, store)
			{
				if(record.get('isReturn') == 'Y' && v != null)
						return "<font color=red>" + v + "</font>";
				else return v;
			}
        },
        {
        	header : '申请理由',
        	width : 100,
            hidden : false,
            dataIndex : 'applyReason',
			 renderer : function(v, cellmeta, record, rowIndex, columnIndex, store)
			{
				if(record.get('isReturn') == 'Y' && v != null)
						return "<font color=red>" + v + "</font>";
				else return v;
			}
        },    {
        	header : "计划种类",
			width : 100,
			sortable : true,
			renderer : function(value, cellmeta, record, rowIndex, columnIndex, store)
			{
				if(record.get('isReturn') == 'Y' && value != null)
						return "<font color=red>" + planOriginalName(record.get("planOriginalId"), value) + "</font>";
				else 
					return planOriginalName(record.get("planOriginalId"), value);
			},
//			renderer : function(value, cellmeta, record, rowIndex) {
//				return planOriginalName(record.get("planOriginalId"), value);
//			},
			dataIndex : 'planOriginalId'
        },{
            header : "已收数量",
            sortable: true,
            width : 60,
            hidden:true,
            align : 'right',
            dataIndex : 'issQty'
        }, {
        	header : '最大库存量',
            hidden : true,
            dataIndex : 'maxStock'
        },ckcQaControlFlag,{
        	header : '需求计划明细ID',
        	width : 100,
        	hidden : false,
        	dataIndex : 'requirementDetailId',
			 renderer : function(v, cellmeta, record, rowIndex, columnIndex, store)
			{
				if(record.get('isReturn') == 'Y' && v != null)
						return "<font color=red>" + v + "</font>";
				else return v;
			}
        },
        
        
        {
        	 header : "申请日期",
			 width : 100,
			 sortable : true,
			 renderer : renderDate,
			 dataIndex : 'mrDate',
			 renderer : function(v, cellmeta, record, rowIndex, columnIndex, store)
			{
				if(record.get('isReturn') == 'Y' && v != null)
						return "<font color=red>" + v + "</font>";
				else return v;
			}
        },
        {
        	header : "计划时间",
			width : 100,
			sortable : true,
//			renderer : function(value, cellmeta, record, rowIndex) {
//				return getPlanDateInfoByDate(record.get("planOriginalId"), value);
//			},
			renderer : function(value, cellmeta, record, rowIndex, columnIndex, store)
			{
				if(record.get('isReturn') == 'Y' && value != null)
						return "<font color=red>" + getPlanDateInfoByDate(record.get("planOriginalId"), value) + "</font>";
				else 
					return getPlanDateInfoByDate(record.get("planOriginalId"), value);
			},
			dataIndex : 'mrDate'
        },
     
       
        {
        	header : "建议供应商",
			width : 100,
			sortable : true,
			dataIndex : 'supplier',
			renderer : function(v, cellmeta, record, rowIndex, columnIndex, store)
			{
				if(record.get('isReturn') == 'Y' && v != null)
						return "<font color=red>" + v + "</font>";
				else return v;
			}
        },
        {
        	header : "生产厂家",
			width : 100,
			sortable : true,
			dataIndex : 'factory',
			renderer : function(v, cellmeta, record, rowIndex, columnIndex, store)
			{
				if(record.get('isReturn') == 'Y' && v != null)
						return "<font color=red>" + v + "</font>";
				else return v;
			}
        },
        
        {
        	header : "备注信息",
			width : 100,
			sortable : true,
			dataIndex : 'memo',
			renderer : function(v, cellmeta, record, rowIndex, columnIndex, store)
			{
				if(record.get('isReturn') == 'Y' && v != null)
						return "<font color=red>" + v + "</font>";
				else return v;
			}
        },
        {
        	header : "填写人",
			width : 100,
			sortable : true,
			dataIndex : 'entryBy',
			hidden:true,
			renderer : function(v, cellmeta, record, rowIndex, columnIndex, store)
			{
				if(record.get('isReturn') == 'Y')
						return "<font color=red>" + v + "</font>";
				else return v;
			}
        }
        // add by ywliu 20091030 End
        ,
        {
        	header : "是否退回",
			width : 100,
			sortable : true,
			dataIndex : 'isReturn',
			renderer : function(v, cellmeta, record, rowIndex, columnIndex, store)
			{
				if(record.get('isReturn') == 'Y')
						return "<font color=red>" + '已退回' + "</font>";
			}
        }
        ,
        {
        	header : "退回原因",
			width : 100,
			sortable : true,
			dataIndex : 'returnReason',
			renderer : function(v, cellmeta, record, rowIndex, columnIndex, store)
			{
				if(record.get('isReturn') == 'Y')
						return "<font color=red>" + v + "</font>";
			}
        }
        ],
        sm : smAnnex,
		
		autoSizeColumns : true
//		viewConfig : {
//			forceFit : true
//		},

		// modified by liuyi 20100406 不要分页
		// 分页
//		bbar : new Ext.PagingToolbar({
//			pageSize : 18,
//			store : store,
//			displayInfo : true,
//			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
//			emptyMsg : "没有记录"
//		})
		
	});

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		autoScroll : true,
		// layout : "fit",
		items : [headTbar,grid]
	});
	
	/**
	 * 去掉时间中T
	 */
	function renderDate(value) {
		if (!value)
			return "";

		var reDate = /\d{4}\-\d{2}\-\d{2}/gi;
		var reTime = /\d{2}:\d{2}:\d{2}/gi;

		var strDate = value.match(reDate);
		var strTime = value.match(reTime);
		if (!strDate)
			return "";
		return strTime ? strDate : strDate;
	}
	
	function addGatherInfo() {
		var sm = grid.getSelectionModel();
		var selected = sm.getSelections();
		var modifyRec = grid.getStore().getModifiedRecords();
		var updateData = new Array();
		var ids="";
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要汇总的记录！");
		} else if(txtBuyer.getValue()==null||txtBuyer.getValue()=="")
		{
			Ext.Msg.alert("提示", "请选择采购员！");
		}
		else {

			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i].data;
				if (member) {
					if(ids=="")
					{
					ids=member.requirementDetailId;
					}
					else
					{
						ids=ids+","+member.requirementDetailId;
					}
//					var data = [];
//					data.push(member.materialId);
//					data.push(member.approvedQty);
//					
//					data.push(member.supplier);
//					data.push(member.factory);
//					updateData.push(data);
//					updateData.push(member.approvedQty);
//					updateData.push(member.requirementDetailId);
				} 
			}
			
//			for (var a = 0;a < updateData.length; a++) {
//				//内层循环
//				for (var j = 0;j < updateData.length - 1; j++) {
//					if (updateData[j][0] > updateData[j + 1][0]) {
//						var temp  = updateData[j];
//						updateData[j] = updateData[j + 1];
//						updateData[j + 1] = temp;
//					}
//				}
//			}
			Ext.util.JSON.encode(updateData);
			Ext.Ajax.request({
				url : 'resource/addGatherMaterialDetail.action',
				method : 'post',
				params : {
					saveDatail : ids,//Ext.util.JSON.encode(updateData),
					buyer:txtBuyerHide.getValue()
				},
				success : function(result, request) {
					var o = eval('(' + result.responseText + ')');
					queryRecord();
					Ext.MessageBox.alert('提示信息', "汇总成功！");// modify by ywliu 提示信息改为汇总成功
				},
				failure : function(result, request) {
					Ext.MessageBox.alert('提示信息', '未知错误！')
				}
			})
		}
		
	}
	
	// 查询
	function queryRecord() {
		
		store.baseParams = {
			buyer:txtBuyer.getValue(),
			applyDept:txtMrDeptH.getValue()
 		};
 		store.load(
 		// modified by liuyi 20100406 不要求分页
// 			{
//		 	params : {
//		 		start : 0,
//		 		limit : 18
//		 	}
//		}
		);
	}
    queryRecord();

});