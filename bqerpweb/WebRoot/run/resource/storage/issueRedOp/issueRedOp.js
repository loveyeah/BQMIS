Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var flag = ""; // add by ywliu 20100128
	
	Ext.QuickTips.init();
	// 对应变更
function numberFormat(value){
			if(value==null||value===""){
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
	var enddate = new Date().add(Date.MONTH, +1);
	var startdate = new Date().add(Date.MONTH, -1);
	startdate = startdate.getFirstDateOfMonth();
	var sdate = ChangeDateToString(startdate);
	var edate = ChangeDateToString(enddate);
	var planStartDate = new Ext.form.DateField({
		fieldLabel : '出库时间',
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
	// add by ywliu 20100203 
	var txtMaterialNo=new Ext.form.TextField({
		id:'materialNo',
		name:'materialNo',
		readOnly:false,
		anchor : '100%',
		fieldLabel : '物料编码'
	});
	// add by ywliu 20100203 
	var txtSpecNo=new Ext.form.TextField({
		 id:'specNo',
		 name:'specNo',
		 anchor : '100%',
		 fieldLabel : '规格型号'
	});

	// 物料名称
	var txtMaterialName = new Ext.form.TextField({
		fieldLabel : '物料名称',
		anchor : '100%',
		readOnly : false,
		id : "materialName",
		name : "materialName"
	});

	// 领料单号
	var txtIssueNo = new Ext.form.TextField({
		fieldLabel : '领料单号',
		anchor : '100%',
		id : "issueNo"
	});
	// 替代物料流水号
	var hdnAltMatId = new Ext.form.Hidden({
		id : "materialId",
		name : "materialId"
	});
	
	// 是否红单 add by ywliu 20100203
	var redbillStore = new Ext.data.SimpleStore({
		root : 'list',
		data : [['Y','是'],['N','否']],
		fields : ['name' ,'key']
	})
	
	//类型 add by ywliu 20100203
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
				anchor : '100%'
	});
	
	var btnQuery = new Ext.Button({
		text : "查询",
		//iconCls : 'query',
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
			}, 
				{
				border : false,
				columnWidth : 0.23,
				layout : 'form',
				items : [txtIssueNo]
			}
			// by liuyi 05/25/09
			,{
	            border : false,
				columnWidth : 0.01		
			},
				{
				border : false,
				columnWidth : 0.11,
				layout : 'form',
				items : [btnQuery]
			}, {
				border : false,
				columnWidth : 0.15,
				layout : 'form',
				items : [btnClear]
			}]
		},{
			layout : 'column',
			border : false,
			items : [
				{
				border : false,
				columnWidth : 0.25,
				layout : 'form',
				items : [txtMaterialNo]
			}, 
				{
				border : false,
				columnWidth : 0.25,
				layout : 'form',
				items : [txtMaterialName]
			},
				{
				border : false,
				columnWidth : 0.23,
				layout : 'form',
				items : [redbill]
//			}, 
//			{
//	            border : false,
//				columnWidth : 0.49		
			}]
		},{
			layout : 'column',
			border : false,
			items : [
				{
				border : false,
				columnWidth : 0.25,
				layout : 'form',
				items : [txtSpecNo]
			}]
		}
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
		url : 'resource/findIssueDetailCheckListForBack.action',
		totalProperty : 'total',
		root : 'root',
		fields : [{
			name : 'issueDetailsId'
		}, {
			name : 'lastModifiedDate'
		}, {
			name : 'issueNo'    
		}, {
			name : 'freeDeptName'
		}, {
			name : 'freeSpecialName'
		}, {
			name : 'itemCode'
		},{
			name:'materialId'
		},
		{
			name : 'materialNo'
		}, {
			name : 'materialName'
		}, {
			name : 'specNo'
		}, {
			name : 'stockUmId'
		}, {
			name : 'actIssuedCount'
		}, {
			name : 'unitPrice'
		}, {
			name : 'actPrice'
		}, {
			name : 'appliedQty'
		}, {
			name : 'estimatedPrice'
		}, {
			name : 'actEstimatePrice'
		}, {
			name : 'whsNo'
		}, {
			name : 'locationNo'
		}, {
			name : 'lastModifyBy'
		}, {
			name : 'memo'
		},{name:'waitNum'}  //红单可操作数
		]
	});
	// 载入数据


	queryStore.on("load",function()
	{
		Ext.Msg.hide();
	}
	);
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
				if (record.data.actIssuedCount < 0) {
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
		}),
				// 入库日期
				{
					header : "最后出库日期",
					anchor : '85%',
					sortable : true,
					dataIndex : 'lastModifiedDate'
				},
				// 物料编码
				{
					header : "领料单号",
					anchor : '85%',
					sortable : true,
					dataIndex : 'issueNo'
				},
				// 费用归口部门
				{
					header : "费用归口部门",
					anchor : '85%',
					sortable : true,
					dataIndex : 'freeDeptName'
				},
				// 费用归口专业
				{
					header : "费用归口专业",
					anchor : '85%',
					sortable : true,
					hidden : true,
					dataIndex : 'freeSpecialName'
				},
				// 费用来源
				{
					header : "费用来源",
					anchor : '85%',
					sortable : true,
					renderer : function(val) {
						return getBudgetName(val);
//					 	if (val == "zzfy") {
//							return "制造费用";
//						} else if (val == "lwcb") {
//							return "劳务成本";
//						}else {
//							return "";// 费用来源显示中文 , modify by ywliu 2009/7/10
//						}
			        },
					dataIndex : 'itemCode'
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
					header : "出库数量",
					width : 75,
					sortable : true,
					renderer : numberFormat,
					//align : 'right',
					dataIndex : 'actIssuedCount'
				},
					{
					header : "可操作数量",
					anchor : '85%',
					sortable : true,
					renderer : numberFormat,
					dataIndex : 'waitNum'
				},
				// 单价
				{
					header : "单价",
					width : 75,
					sortable : true,
					hidden : true,
					renderer : numberFormat,
					//align : 'right',
					dataIndex : 'unitPrice'
				},
				// 金额
				{
					header : "金额",
					width : 75,
					sortable : true,
					hidden : true,
					renderer : numberFormat,
				//	align : 'right',
					dataIndex : 'actPrice'
				},
				// 入库人员
				{
					header : "估计数量",
					width : 70,
					sortable : true,
					hidden : true,
					renderer : numberFormat,
					dataIndex : 'appliedQty'
				},
				{
					header : "估计单价",
					width : 70,
					sortable : true,
					hidden : true,
					renderer : numberFormat,
					dataIndex : 'estimatedPrice'
				},
				{
					header : "估计金额",
					width : 110,
					sortable : true,
					hidden : true,
					renderer : numberFormat,
					dataIndex : 'actEstimatePrice'
				},
				{
					header : "仓库",
					anchor : '85%',
					sortable : true,
					hidden : true,
					dataIndex : 'whsNo'
				},
				{
					header : "出库人员",
					width : 100,
					sortable : true,
					hidden : true,
					dataIndex : 'lastModifyBy'
				},
				{
					header : "备注",
					width : 100,
					sortable : true,
					hidden : true,
					dataIndex : 'memo'
				}],
		 tbar:[
//		 	{text:'查看出库明细',
//			    	iconCls : Constants.CLS_QUERY,
//			    	handler:queryStorageDetail
//			  },
			  {
			  	text:'红单处理',
			  	iconCls:'save',
			  	handler:redOp
			  },{
			  	text:'蓝单处理',
			  	iconCls:'save',
			  	handler:blueOp
			  }
			  ],
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
			height : 120,
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
			queryStore.baseParams = {
				startdate : Ext.get('planStartDate').dom.value,
				enddate : Ext.get('planEndDate').dom.value,
				materialName : Ext.get('materialName').dom.value,
			    issueNo : Ext.get('issueNo').dom.value,
			    specNo : Ext.get('specNo').dom.value, // modify by ywliu 20100128
			    materialNo : Ext.get('materialNo').dom.value, // modify by ywliu 20100128
			    redBill : redbill.getValue() // modify by ywliu 20100128
			}
			queryStore.load({
				params : {
					start : 0,
					limit : 18
				}
			});
		}



	/**
	 * 查看出库操作明细
	 */
	 function queryStorageDetail()
	 {
	 	
	 	var record = transPanel.getSelectionModel().getSelected();
	 	if(record==null)
	 	{
	 		Ext.Msg.alert("提示","请选择一条记录！");
	 	}
	 	else
	 	{
	 		var arg = new Object();
	 		//alert(record.get("materialNo"));
	 		arg.materialNo=record.get("materialNo");
	 		arg.issueNo=record.get("issueNo");
	 		window.showModalDialog('issueDetailQuery.jsp', arg,
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
	
	function redOp()
	{
		//出库红单处理
		var record = transPanel.getSelectionModel().getSelected();
	 	if(record==null)
	 	{
	 		Ext.Msg.alert("提示","请选择一条记录！");
	 	}
	 	else
	 	{
	 		//if(record.data.waitNum > 0) {
		 		// 确认是否正在结帐
				Ext.lib.Ajax.request('POST',
					'resource/isIssueBalanceNow.action', {
						success : function(action) {
							if ('true' == action.responseText) {
								// 正在结帐,弹出信息无法进行业务
								Ext.Msg.alert(
										Constants.SYS_REMIND_MSG,
										Constants.COM_E_001);
								return;
							}
							else
							{
							   	myaddpanel.getForm().reset(); 
							   	flag = 'red';
		                        win.show(); 
		                        Ext.get("txtNowPrice").dom.parentNode.parentNode.style.display = 'none';
		                        //txtNowPrice.setVisible(false);
							   	txtMaterialName.setValue(record.get("materialName"));
							   	txtIssueNo.setValue(record.get("issueNo"));
							   	txtMaterialNo.setValue(record.get("materialNo"));
							   	txtSpecNo.setValue(record.get("specNo"));
							   	txtNumCount.setValue(record.get("waitNum"));
							   	//txtIssueNo,txtMaterialNo,txtMaterialName,txtSpecNo,txtNumCount
							}
						}
				},"transCode=I" );
//	 		} else {
//	 			Ext.Msg.alert("提示", "只能对出库物资操作！");
//	 		}	
	 	}
	}
	
	// add by ywliu 20100203 蓝单处理
	function blueOp() {
		//出库蓝单处理
		var record = transPanel.getSelectionModel().getSelected();
	 	if(record==null) {
	 		Ext.Msg.alert("提示","请选择一条记录！");
	 	}
	 	else {
	 		if(record.data.actIssuedCount < 0) {
		 		// 确认是否正在结帐
				Ext.lib.Ajax.request('POST',
					'resource/isIssueBalanceNow.action', {
						success : function(action) {
							if ('true' == action.responseText) {
								// 正在结帐,弹出信息无法进行业务
								Ext.Msg.alert(
										Constants.SYS_REMIND_MSG,
										Constants.COM_E_001);
								return;
							} else {
							   	myaddpanel.getForm().reset(); 
		                        win.show(); 
		                         Ext.get("txtNowPrice").dom.parentNode.parentNode.style.display = '';
		                        Ext.get('txtNumCount').dom.readOnly = true;
		                        Ext.get('txtMemo').dom.readOnly = true;
		                        flag = 'blue';
							   	txtMaterialName.setValue(record.get("materialName"));
							   	txtIssueNo.setValue(record.get("issueNo"));
							   	txtMaterialNo.setValue(record.get("materialNo"));
							   	txtSpecNo.setValue(record.get("specNo"));
							   	txtNumCount.setValue(0-record.get("actIssuedCount"));
							   	//txtIssueNo,txtMaterialNo,txtMaterialName,txtSpecNo,txtNumCount
							}
					}
				},"transCode=I" );
	 		} else {
	 			Ext.Msg.alert("提示", "只能对红单操作！");
	 		}	
	 	}
	}
	
	//---------红单窗口--------------------
	var txtIssueNo=new Ext.form.TextField({
	 id:'txtIssueNo',
	 name:'txtIssueNo',
	 readOnly:true,
	 fieldLabel : '领料单号'
	});
	var txtMaterialNo=new Ext.form.TextField({
	 id:'txtMaterialNo',
	 name:'txtMaterialNo',
	 readOnly:true,
	 fieldLabel : '物料编码'
	});
	var txtMaterialName=new Ext.form.TextField({
	id:'txtMaterialName',
	   name:'txtMaterialName',
	   readOnly:true,
	   fieldLabel : '物资名称'
	});
	var txtSpecNo=new Ext.form.TextField({
	 id:'txtSpecNo',
	 name:'txtSpecNo',
	 readOnly:true,
	 fieldLabel : '规格型号'
	});
	
	var txtNumCount=new Ext.form.NumberField({
	   id:'txtNumCount',
	   name:'txtNumCount',
	   fieldLabel : '数量'
	});
	var txtMemo=new Ext.form.TextArea({
	 id : "txtMemo",
		fieldLabel : '备注',
		anchor : "95%",
		name : 'txtMemo',
		height:80
	}); 
	
		var txtNowPrice=new Ext.form.NumberField({
	   id:'txtNowPrice',
	   name:'txtNowPrice',
	   decimalPrecision:6, 
	//   allowBlank:false,
	   fieldLabel : '价格'
	});
	
	
	
	var myaddpanel = new Ext.FormPanel({
		frame : true,
		labelAlign : 'center',
		labelWidth:80,
		fileUpload : true,
		closeAction : 'hide',
		title : '出库红蓝单处理',
		layout:'form',
		items : [txtIssueNo,txtMaterialNo,txtMaterialName,txtSpecNo,txtNumCount,txtNowPrice,txtMemo] 
	});

	var win = new Ext.Window({
		width : 500,
		height : 350,
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
					var record = transPanel.getSelectionModel().getSelected();
					var actCount=parseInt( record.get("waitNum"));
					var num=parseInt(txtNumCount.getValue());
					
					if((actCount>0&&num>actCount)||(actCount<0&&num<actCount))
					{
					  	Ext.Msg.alert("提示","数量不能超过出库数量！")
					  	return ;
					}
					//------------红单处理
					
					
					Ext.Ajax.request({
						url : 'resource/IssueMaterialRedBackOp.action',
						success : function(action) {
	                          
									win.hide();
									flag = "";
									Ext.Msg.alert("提示",
									"操作成功");
									searchBtn();
						},
						failure : function() {
							Ext.Msg.alert("提示",
									"操作失败");
						},
						params : {
							issueDetailId:record.get("issueDetailsId"),
							issueHeadNo:record.get("issueNo"),
							materialId:record.get("materialId"),
							memo:txtMemo.getValue(),
							backCount:txtNumCount.getValue(),
							stdCost:record.get("unitPrice"),
							flag :  flag //add by ywliu 20100203
						}
					});
				} else if(flag == "blue") {
					if(txtNowPrice.getValue()=="")
					{
					  Ext.Msg.alert("提示","请输入价格！");
					  return;
					}
					var record = transPanel.getSelectionModel().getSelected();
					var actCount=parseInt(0- record.get("actIssuedCount"));
					var num=parseInt(txtNumCount.getValue());
					
					if(num>actCount)
					{
					  	Ext.Msg.alert("提示","数量不能超过出库数量！")
					  	return ;
					}
					//------------------------------
					// 检索此物料的库存总量
					var url = "resource/getStorageMaterialCounts.action?materialId="
							+ record.get("materialId")+"&whsNo="+record.get("whsNo")+"&locationNo="+record.get("locationNo");
					var conn = Ext.lib.Ajax.getConnectionObject().conn;
					conn.open("POST", url, false);
					conn.send(null);
					// 当前物料总库存数
					var allCounts = Ext.util.JSON.decode("(" + conn.responseText + ")");
					if((parseInt(allCounts)-num) < 0) {
						Ext.Msg.alert('提示', '库存不足，不能填写红单！');
					}else {
						//------------红单处理
						Ext.Ajax.request({
							url : 'resource/IssueMaterialRedBackOp.action',
							success : function(action) {
		                          
										win.hide();
										flag = "";
										Ext.Msg.alert("提示",
										"操作成功");
										searchBtn();
							},
							failure : function() {
								Ext.Msg.alert("提示",
										"操作失败");
							},
							params : {
								issueDetailId:record.get("issueDetailsId"),
								issueHeadNo:record.get("issueNo"),
								materialId:record.get("materialId"),
								memo:txtMemo.getValue(),
								backCount:record.get("actIssuedCount"),
								stdCost:txtNowPrice.getValue(),
								flag :  flag //add by ywliu 20100203
							}
						});
					}	
				}
			}
			// ---------------------------
		}, {
			text : '取消',
			iconCls:'cancer',
			handler : function() { 
				win.hide();
			}
		}]

	});
	
	searchBtn();
});