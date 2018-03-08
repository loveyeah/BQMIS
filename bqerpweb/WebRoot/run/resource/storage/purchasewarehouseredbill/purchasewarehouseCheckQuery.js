Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var flag = ""; // add by ywliu 20100128
	var defaultWhsNo = "";
	var defaultLocationNo = "";
	Ext.QuickTips.init();
	// 对应变更
function numberFormat(value){
			if(value==="" || value == null){ // 增加判断是否为null modify by ywliu 090721	
				return value
			}
		    value = String(value);
            // 整数部分
            var whole = value;
            // 小数部分
            var sub = ".00";
            // 如果有小数
		    if (value.indexOf(".") > 0) {
		    	whole = value.substring(0, value.indexOf("."));
			    sub = value.substring(value.indexOf("."), value.length);
			    sub = sub + "00";
			    if(sub.length > 3){
			    	sub = sub.substring(0,3);
			    }
		    }
            var r = /(\d+)(\d{3})/;
            while (r.test(whole)){
                whole = whole.replace(r, '$1' + ',' + '$2');
            }
            v = whole + sub;
            return v;	
	}
	function ChangeDateToString(DateIn) {
		var Year = 0;
		var Month = 0;
		var Day = 0;
		var CurrentDate = "";
		// 初始化时间
		Year = DateIn.getYear();
		Month = DateIn.getMonth() + 1;
		Day = DateIn.getDate();
		CurrentDate = Year + "-";
		if (Month >= 10) {
			CurrentDate = CurrentDate + Month + "-";
		} else {
			CurrentDate = CurrentDate + "0" + Month + "-";
		}
		if (Day >= 10) {
			CurrentDate = CurrentDate + Day;
		} else {
			CurrentDate = CurrentDate + "0" + Day;
		}
		return CurrentDate;
	}
	var enddate = new Date().add(Date.MONTH,+1);
	var startdate = new Date().add(Date.MONTH, -1);
	startdate = startdate.getFirstDateOfMonth();
	var sdate = ChangeDateToString(startdate);
	var edate = ChangeDateToString(enddate);
	var planStartDate = new Ext.form.DateField({
		fieldLabel : '入库时间',
		format : 'Y-m-d',
		name : 'startDate',
		id : 'planStartDate',
		itemCls : 'sex-left',
		clearCls : 'allow-float',
		checked : true,
		allowBlank : false,
		readOnly : true,
		value : sdate,
		emptyText : '请选择',
		//width : 110
		anchor : '100%'
	});
	var planEndDate = new Ext.form.DateField({
		fieldLabel : '至',
		format : 'Y-m-d',
		name : 'endDate',
		value : edate,
		id : 'planEndDate',
		itemCls : 'sex-left',
		clearCls : 'allow-float',
		allowBlank : false,
		readOnly : true,
		emptyText : '请选择',
		anchor : '100%'
	});

	// 物料名称
	var txtMaterialName = new Ext.form.TextField({
		fieldLabel : '物料名称',
		anchor : '100%',
		readOnly : false,
		id : "txtMaterialName",
		name : "txtMaterialName"
	});
	//--add by fyyang 20100203 
	var myMaterialNo = new Ext.form.TextField({
		fieldLabel : '物料编码',
		anchor : '100%',
		readOnly : false,
		id : "myMaterialNo",
		name : "myMaterialNo"
	});
	//-------------------------
	
	// 供应商
	var txtSupplyName = new Ext.form.TextField({
		fieldLabel : '供应商',
		anchor : '100%',
		id : "supplyName",
		listeners : {
			focus : selectSupplier
		}
	});
	
	// 采购人员
	var txtPurBy = new Ext.form.TextField({
		fieldLabel : '采购人员',
		anchor : '100%',
		id : "purBy",
		name : "PurBy"
	});
	
	// txtPurBy.onClick(selectPersonWin);
	// var txtMrByH = new Ext.form.Hidden();
	// txtMrByH.on('render', getUserName)
	// 替代物料流水号
	var hdnAltMatId = new Ext.form.Hidden({
		id : "materialId",
		name : "materialId"
	});
	
	// 是否红单
	var redbillStore = new Ext.data.SimpleStore({
		root : 'list',
		data : [['Y','是'],['N','否']],
		fields : ['name' ,'key']
	})
	
	//类型
	var redbill = new Ext.form.ComboBox({
				id : 'redbill',
				name : 'redbillStore',
				fieldLabel : "是否红单",
				mode : 'local',
				typeAhead : true,
				valueField : "name",
				displayField : "key",
				store : redbillStore,
				forceSelection : true,
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : false,
				anchor : '95%'
	});

	var btnQuery = new Ext.Button({
		text : "查询",
		minWidth : 70,
		handler : searchBtn
			
	});
	
	var btnClear = new Ext.Button({
		id : 'btnClear',
		text : '清除条件',
		minWidth : 70,
		handler : function() {
			form.getForm().reset();
		}
	});
	
	var content = new Ext.form.FieldSet({
		title : '查询条件',
		height : '100%',
		collapsible : true,
		layout : 'form',
		items : [{
			layout : 'column',
			border : false,
			items : [{
				border : false,
				columnWidth : 0.25,
				layout : 'form',
				items : [planStartDate]
			}, {
				border : false,
				columnWidth : 0.25,
				layout : 'form',
				items : [planEndDate]
			}
			, {
				border : false,
				columnWidth : 0.23,
				layout : 'form',
				items : [txtSupplyName]
			}
			, 	
			{
	            border : false,
				columnWidth : 0.01		
			},{
				border : false,
				columnWidth : 0.11,
				align : 'center',
				layout : 'form',
				items : [btnQuery]
			}, {
				border : false,
				columnWidth : 0.15,
				align : 'center',
				layout : 'form',
				items : [btnClear]
			}]
		}
		, {
			layout : 'column',
			border : false,
			items : [ 
//				{  modify by drdu 2009/06/01
//				border : false,
//				columnWidth : 0.25,
//				layout : 'form',
//				items : [txtMaterialNo]
//			},
			{
				border : false,
				columnWidth : 0.25,
				layout : 'form',
				items : [txtMaterialName]
			}, 
				{
				border : false,
				columnWidth : 0.25,
				layout : 'form',
				items : [myMaterialNo]
			},
			{
				border : false,
				columnWidth : 0.23,
				layout : 'form',
				items : [txtPurBy]
			},
				{
	            border : false,
				columnWidth : 0.24,	
				layout : 'form',
				items : [redbill]		
			}]
		}
//		, {
//			layout : 'column',
//			border : false,
//			items : [{
//				border : false,
//				columnWidth : 1,
//				layout : 'form',
//				items : [txtPurBy]
//			}]
//		}
		]
	});
	var form = new Ext.form.FormPanel({
		bodyStyle : "padding:5px 5px 0",
		labelAlign : 'right',
		id : 'shift-form',
		labelWidth : 80,
		autoHeight : true,
		region : 'center',
		border : false,
		items : [content]
	});

	// 物料异动grid
	var queryStore = new Ext.data.JsonStore({
		url : 'resource/getPurchasehouseCheckList.action',
		totalProperty : 'total',
		root : 'root',
		fields : [{
			name : 'id'
		}, {
			name : 'arrivalDetailID'
		}, {
			name : 'materialId'
		}, {
			name : 'arrivalDetailModifiedDate'
		}, {
			name : 'materialNo'
		}, {
			name : 'materialName'
		}, {
			name : 'specNo'
		}, {
			name : 'stockUmId'
		}, {
			name : 'purOrderDetailsRcvQty'
		}, {
			name : 'unitPrice'
		}, {
			name : 'currencyType'
		}, {
			name : 'estimatedSum'
		}, {
			name : 'operateBy'
		}, {
			name : 'buyerName'
		}, {
			name : 'supplyName'
		}, {
			name : 'contractNo'
		}, {
			name : 'invoiceNo'
		}, {
			name : 'lotCode'
		}, {
			name : 'defaultWhsNo'
		}, {
			name : 'defaultLocationNo'
		}, {
			name : 'arrivalNo'
		}, {
			name : 'purNo'
		},
		{name:'sumRedQty'}
		,{name:'stdCost'}
		]
	});
	// 载入数据
//	queryStore.load({
//		params : {
//			start : 0,
//			limit : 18
//		}
//	});

	queryStore.on("load",function(){
		if(queryStore.getCount() > 0) {
			if(queryStore.getAt(0).data.purOrderDetailsRcvQty < 0) {
				transPanel.getColumnModel().setHidden(11,true);
			}
		}
		Ext.Msg.hide();
	});
	// grid
	var transPanel = new Ext.grid.GridPanel({
		region : "center",
		enableColumnMove : false,
		enableColumnHide : true,
		frame : false,
		border : false,
		store : queryStore,
		// modify by ywliu 20100127
		viewConfig : {
			forceFit : false,
			getRowClass : function(record, rowIndex, rowParams, store) {
				// 禁用数据显示红色
				if (record.data.purOrderDetailsRcvQty < 0) {
					return 'x-grid-record-red';
				}
				else {
					return '';
				} 
			}
		},
		// 单选
		sm : new Ext.grid.RowSelectionModel({
			singleSelect : true
		}),
		columns : [new Ext.grid.RowNumberer({
			header : "行号",
			width : 35
		}),// ID
				{
					header : "ID",
					anchor : '85%',
					sortable : true,
					hidden :true,
					dataIndex : 'id'
				},
				// 明细ID
				{
					header : "ID",
					anchor : '85%',
					sortable : true,
					hidden :true,
					dataIndex : 'arrivalDetailID'
				},
				// 物料ID
				{
					header : "materialId",
					anchor : '85%',
					sortable : true,
					hidden :true,
					dataIndex : 'materialId'
				},
				// 入库日期
				{
					header : "入库日期",
					anchor : '85%',
					sortable : true,
					dataIndex : 'arrivalDetailModifiedDate'
				},
				// 物料编码
				{
					header : "到货单号",
					anchor : '85%',
					sortable : true,
					dataIndex : 'arrivalNo'
				},
				// 物料编码
				{
					header : "物料编码",
					anchor : '85%',
					sortable : true,
					dataIndex : 'materialNo'
				},
				// 物料名称
				{
					header : "物料名称",
					anchor : '85%',
					sortable : true,
					dataIndex : 'materialName'
				},
				// 规格型号
				{
					header : "规格型号",
					anchor : '85%',
					sortable : true,
					dataIndex : 'specNo'
				},// 单位
				{
					header : "单位",
					width : 50,
					sortable : true,
					renderer:unitName,
					dataIndex : 'stockUmId'
				},
				// 入库数
				{
					header : "入库数",
					width : 75,
					sortable : true,
					renderer : numberFormat,
					//align : 'right',
					dataIndex : 'purOrderDetailsRcvQty'
				},
					{
					header : "红单可操作数",
					width : 75,
					sortable : true,
					renderer : numberFormat,
					//align : 'right',
					dataIndex : 'sumRedQty'
				},
				// 单价
				{
					header : "单价",
					width : 75,
					sortable : true,
					renderer : numberFormat,
					//align : 'right',
					dataIndex : 'unitPrice' 
				},
				{
					header : "标准成本",
					width : 75,
					sortable : true,
					renderer : numberFormat,
					//align : 'right',
					dataIndex : 'stdCost'
				},
				// 金额
				{
					header : "金额",
					width : 75,
					sortable : true,
					renderer : numberFormat,
				//	align : 'right',
					dataIndex : 'estimatedSum'
				},// 币别
				{
					header : "币别",
					width : 75,
					sortable : true,
					//align : 'right',
					dataIndex : 'currencyType'
				},
				// 入库人员
				{
					header : "入库人员",
					width : 70,
					hidden :true,
					sortable : true,
					dataIndex : 'operateBy'
				},
				{
					header : "采购人员",
					width : 70,
					sortable : true,
					dataIndex : 'buyerName'
				},
				{
					header : "供应商",
					width : 110,
					sortable : true,
					dataIndex : 'supplyName'
				},
				{
					header : "合同编号",
					anchor : '85%',
					sortable : true,
					dataIndex : 'contractNo'
				},
				{
					header : "发票号",
					anchor : '85%',
					sortable : true,
					dataIndex : 'invoiceNo'
				},
				// 仓库
				{
					header : "仓库",
					width : 100,
					sortable : true,
					hidden :true,
					renderer : comboBoxWarehouseRenderer,
					dataIndex : 'defaultWhsNo'
				},
				// 库位
				{
					header : "库位",
					width : 100,
					hidden :true,
					sortable : true,
					dataIndex : 'defaultLocationNo'
				}],
		tbar:[{text: '红单处理',
			  		iconCls : Constants.CLS_UPDATE,
			    	handler:function() {
			    		
			    		
			    		
			    		
			    		
			    		var record = transPanel.getSelectionModel().getSelected();
						if (record == null) {
							Ext.Msg.alert("提示", "请选择一条记录！");
						} else {
							if(record.data.purOrderDetailsRcvQty > 0) {
								flag = 'red';
								win.show();
								Ext.get('purOrderDetailsRcvQty').dom.parentNode.parentNode.childNodes[0].innerHTML = "<font color='red'>红单数量:</font>";
								unitPrice.setVisible(false);
								Ext.get('purOrderDetailsRcvQty').dom.readOnly = false;
								myaddpanel.getForm().reset();
			        			myaddpanel.form.loadRecord(record);
			        			
			        			purOrderDetailsRcvQty.setValue(0-parseInt(record.get("sumRedQty")));
			        			defaultWhsNo = record.get('defaultWhsNo');
			        			defaultLocationNo = record.get('defaultLocationNo');
							} else {
								Ext.Msg.alert("提示", "只能对入库物资操作！");
							}
						}
			    	}
			  },{text: '蓝单处理', // modify by ywliu 20100129 
			  		iconCls : Constants.CLS_UPDATE,
			    	handler:function() {
			    		var record = transPanel.getSelectionModel().getSelected();
						if (record == null) {
							Ext.Msg.alert("提示", "请选择一条记录！");
						} else {
							if(record.data.purOrderDetailsRcvQty < 0) {
								flag = 'blue';
								win.title = '蓝单处理';
								win.show();
								Ext.get('purOrderDetailsRcvQty').dom.parentNode.parentNode.childNodes[0].innerHTML = "<font color='blue'>蓝单数量:</font>";
								unitPrice.setVisible(true);
								purOrderDetailsRcvQty.fieldLabel = '蓝单数量';
								Ext.get('purOrderDetailsRcvQty').dom.readOnly = true;
								myaddpanel.getForm().reset();
			        			myaddpanel.form.loadRecord(record);
			        			purOrderDetailsRcvQty.setValue(0-parseInt(record.get("sumRedQty")));
			        			defaultWhsNo = record.get('defaultWhsNo');
			        			defaultLocationNo = record.get('defaultLocationNo');
			        			txtUnitPrice.setValue("");
							} else {
								Ext.Msg.alert("提示", "只能对红单操作！");
							}
						}
			    	}
			  }],
		// 分页
		bbar : new Ext.PagingToolbar({
			pageSize : Constants.PAGE_SIZE,
			store : queryStore,
			displayInfo : true,
			displayMsg : Constants.DISPLAY_MSG,
			emptyMsg : Constants.EMPTY_MSG
		})
	});

	// 显示区域
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		items : [{
			region : "north",
			layout : 'fit',
			height : 140,
			border : true,
			split : true,
			margins : '0 0 0 0',
			// 注入表格
			items : [form]
		}, {
			region : "center",
			layout : 'fit',
			border : false,
			collapsible : true,
			split : true,
			margins : '0 0 0 0',
			// 注入表格
			items : [transPanel]
		}]
	});
	var txtUnitPrice=new Ext.form.TextField({
		fieldLabel : '单价',
		id : "unitPrice",
		name : "unitPrice",
		anchor : '100%'
	});
	
	var unitPrice = new Ext.Panel({
		border : false,
		layout : "form",
		anchor : '100%',
		items : [txtUnitPrice]
	})
	
	// 到货单ID  
	var txtArrivalId = new Ext.form.TextField({
		fieldLabel : '到货单号ID',
		isFormField : true,
		readOnly : true,
		hidden : true,
		hideLabel :true,
		id : "id",
		name : "id",
		anchor : '100%'
	});
	
	// 到货单明细ID  
	var arrivalDetailID = new Ext.form.TextField({
		fieldLabel : '到货单明细ID',
		isFormField : true,
		readOnly : true,
		hidden : true,
		hideLabel :true,
		id : "arrivalDetailID",
		name : "arrivalDetailID",
		anchor : '100%'
	});
	
	// 到货单号  
	var txtArrivalNo = new Ext.form.TextField({
		fieldLabel : '到货单号',
		isFormField : true,
		readOnly : true,
		id : "arrivalNo",
		name : "arrivalNo",
		anchor : '100%'
	});
	
	// 物料ID  
	var txtMaterialId = new Ext.form.TextField({
		fieldLabel : '物料ID',
		isFormField : true,
		readOnly : true,
		hidden : true,
		hideLabel :true,
		id : "materialId",
		name : "materialId",
		anchor : '100%'
	});
	
	// 物料编码  
	var txtMaterialNo = new Ext.form.TextField({
		fieldLabel : '物料编码',
		isFormField : true,
		readOnly : true,
		id : "materialNo",
		name : "materialNo",
		anchor : '100%'
	});
	
	var materialName = new Ext.form.TextField({
		fieldLabel : '物料名称',
		readOnly : true,
		anchor : '100%',
		id : "materialName",
		name : 'materialName'
	});
	
	// 规格型号
	var txtSpecNo = new Ext.form.TextField({
		fieldLabel : '规格型号',
		readOnly : true,
		anchor : '100%',
		id : "specNo",
		name : 'specNo'
	});
	
	var purOrderDetailsRcvQty = new Ext.form.NumberField({
		fieldLabel : '<font color="red">红单数量</font>',
		anchor : '100%',
		id : "purOrderDetailsRcvQty",
		name : 'purOrderDetailsRcvQty',
		blankText : false,
		minValue : 0
	});
	
	var myaddpanel = new Ext.FormPanel({
		frame : true,
		labelAlign : 'center',
		labelWidth:80,
		closeAction : 'hide',
		items : [txtArrivalNo,txtMaterialNo,materialName,txtSpecNo,purOrderDetailsRcvQty,unitPrice,txtArrivalId,arrivalDetailID,txtMaterialId] 
	});

	//add by drdu 090806
	function isNumber()
	{
		var str=Ext.get('purOrderDetailsRcvQty').dom.value;
		var re=/^-[1-9]\d*$/;
		if(re.test(str))
		{
			return true;
		}else{
			Ext.Msg.alert("提示","红单数量只能输入负整数!");
			return false;
		}

	}
	
	var win = new Ext.Window({
		width : 400,
		height : 250,
		title : '入库红单处理',
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
				if(flag == "red") {
				//---add by fyyang ------------
				var record = transPanel.getSelectionModel().getSelected();
				var allowCount=parseInt(record.get("sumRedQty"));
				//add by drdu 090806

				
				if(allowCount+parseInt(Ext.get('purOrderDetailsRcvQty').dom.value)<0)
				{
					
				 Ext.Msg.alert("提示","数量不能超过入库的数量");	
				 return ;
				}
				
				//------------------------------
				// 检索此物料的库存总量
				var url = "resource/getStorageMaterialCounts.action?materialId="
						+ Ext.get('materialId').dom.value+"&whsNo="+defaultWhsNo+"&locationNo="+defaultLocationNo;
				var conn = Ext.lib.Ajax.getConnectionObject().conn;
				conn.open("POST", url, false);
				conn.send(null);
				// 当前物料总库存数
				var allCounts = Ext.util.JSON.decode("(" + conn.responseText + ")");
				var count = 0;
			
				count =0-parseInt(Ext.get('purOrderDetailsRcvQty').dom.value);
				if((parseInt(allCounts)-count) < 0) {
					Ext.Msg.alert('提示', '库存不足，不能填写红单！');
				}
				else {
					if((parseInt(allCounts)-count) == 0&&record.get("unitPrice")!=record.get("stdCost"))
					{
						//add by fyyang 20100311
					   	 Ext.Msg.alert('提示', '红单操作后库存数量为0金额不为0，不能填写红单！');
					   	  return;
					}
					
					
					Ext.Ajax.request({
					params : {
						arrivalNo : Ext.get('arrivalNo').dom.value,
						rcvQty : Ext.get('purOrderDetailsRcvQty').dom.value,
						materialNo : Ext.get('materialNo').dom.value,
						id : Ext.get('id').dom.value,
						arrivalDetailID : Ext.get('arrivalDetailID').dom.value,
						materialId : Ext.get('materialId').dom.value,
						unitPrice: Ext.get('unitPrice').dom.value,  //add by fyyang 091201
						flag :  flag
					},
					url : 'resource/addPurchaseWarehouseRedBill.action',
					method : 'post',
					success : function(result) {
					    searchBtn();
							win.hide();
							flag = "";
						},
						failure : function(action) {
						}
		
					});
			}
			} else if(flag == 'blue') {
				var record = transPanel.getSelectionModel().getSelected();
				var allowCount=parseInt(record.get("sumRedQty"));
				//add by drdu 090806
//				if(!isNumber())
//				{
//					return;
//				}
				
				if(allowCount+parseInt(Ext.get('purOrderDetailsRcvQty').dom.value)<0)
				{
					
				 Ext.Msg.alert("提示","数量不能超过入库的数量");	
				 return ;
				}
				Ext.Ajax.request({
					params : {
						arrivalNo : Ext.get('arrivalNo').dom.value,
						rcvQty : Ext.get('purOrderDetailsRcvQty').dom.value,
						materialNo : Ext.get('materialNo').dom.value,
						id : Ext.get('id').dom.value,
						arrivalDetailID : Ext.get('arrivalDetailID').dom.value,
						materialId : Ext.get('materialId').dom.value,
						unitPrice: Ext.get('unitPrice').dom.value,  //add by fyyang 091201
						flag :  flag
					},
					url : 'resource/addPurchaseWarehouseRedBill.action',
					method : 'post',
					success : function(result) {
					        searchBtn();
							win.hide();
							flag = "";
						},
						failure : function(action) {
						}
		
					});
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
	
	// ↓↓*******************************处理****************************************
	// 查询处理函数
	function searchBtn()
	{
		var ftime = Ext.get('planStartDate').dom.value;
		var ttime = Ext.get('planEndDate').dom.value;
		if (ftime > ttime) {
			Ext.Msg.alert('提示', '起始时间不能大于结束时间！');
			return false;
		}
			Ext.Msg.wait("正在查询数据，请等待...");
			// modify by ywliu 20100126
			queryStore.baseParams = {
				startdate : Ext.get('planStartDate').dom.value,
				enddate : Ext.get('planEndDate').dom.value,
				materialName : Ext.get('txtMaterialName').dom.value,
				supplyName : Ext.get('supplyName').dom.value,
				redBill : redbill.getValue(), // modify by ywliu 20100128
					purBy : Ext.get('purBy').dom.value,
						materialNo: myMaterialNo.getValue()
			}
			queryStore.load({
				params : {
					start : 0,
					limit : 18
				}
			});
		}
		
	/**
	 * 查看入库操作明细
	 */
	 function queryStorageDetail() {

		var record = transPanel.getSelectionModel().getSelected();
		if (record == null) {
			Ext.Msg.alert("提示", "请选择一条记录！");
		} else {
			var arg = new Object();
			// alert(record.get("materialNo"));
			arg.materialNo = record.get("materialNo");
			arg.orderNo = record.get("arrivalNo");
			arg.purNo = record.get("purNo");
			window.showModalDialog('../purchasewarehouseQuery/purchaseWhsDetailSelect.jsp', arg,
					'status:no;dialogWidth=750px;dialogHeight=450px');
		}
	}
	
	
	/**
	 * 选择物料信息表单信息:form
	 */
	function selectMaterial() {
		var mate = window.showModalDialog('../../plan/RP001.jsp', window,
				'dialogWidth=800px;dialogHeight=550px;status=no');
		if (typeof(mate) != "undefined") {
			// 设置物料编码
			txtMaterialNo.setValue(mate.materialNo);
			// 设置物料名称
			txtMaterialName.setValue(mate.materialName);
			// 设置规格型号
			txtSpecNo.setValue(mate.specNo);
			// 设置物料id
			hdnAltMatId.setValue(mate.materialId);
		}
		Ext.get("materialName").focus();
	}
	/**
	 * 供应商选择页面
	 */
	function selectSupplier() {
		var supply = window.showModalDialog(
				'../../../../comm/jsp/supplierQuery/supplierQuery.jsp', window,
				'dialogWidth=800px;dialogHeight=600px;status=no');
		if (typeof(supply) != "undefined") {
			txtSupplyName.setValue(supply.supplyName);
		}
	}
	/**
	 * 人员选择画面处理
	 */
	function selectPersonWin() {
		var args = {
			selectModel : 'signal',
			notIn : "",
			rootNode : {
				id : '-1',
				text : '合肥电厂'
			}
		}
		var person = window
				.showModalDialog(
						'../../../../comm/jsp/hr/workerByDept/workerByDept.jsp',
						args,
						'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (typeof(person) != "undefined") {
			txtWhsBy.setValue(person.workerName);
			// txtMrByH.setValue(person.workerCode);
		}
	}
	/**
	 * 格式化数据----计量单位
	 */
	function unitName(value) {
		if(value!==null&&value!==""){
		var url = "resource/getRS001UnitName.action?unitCode=" + value;
		var conn = Ext.lib.Ajax.getConnectionObject().conn;
		conn.open("POST", url, false);
		conn.send(null);
		return conn.responseText;
		}else{
			return "";
		}
		
	};
	/**
	 * 格式化数据----仓库
	 */
	function comboBoxWarehouseRenderer(value) {
		if(value!=null&&value!=""){
		var url = "resource/getWarehouseName.action?whsNo=" + value;
		var conn = Ext.lib.Ajax.getConnectionObject().conn;
		conn.open("POST", url, false);
		conn.send(null);
		return conn.responseText;
		}else{
			return "";
		}
	};
	
	searchBtn();
});