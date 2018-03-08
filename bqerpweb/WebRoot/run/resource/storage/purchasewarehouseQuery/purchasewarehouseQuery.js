Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	Ext.QuickTips.init();
	
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
	// 对应变更
function numberFormat(value){
	if(value == null)
		return '';
			if(value===""){
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

	// 物料编码  modify by drdu 2009/06/01
	var txtMaterialNo = new Ext.form.TextField({
		fieldLabel : '物料编码',
		isFormField : true,
		id : "materialNo",
		name : "materialNo",
		anchor : '100%'
//		,
//		listeners : {
//			focus : selectMaterial
//		}
	});
	// 物料名称
	var txtMaterialName = new Ext.form.TextField({
		fieldLabel : '物料名称',
		anchor : '100%',
		readOnly : false,
		id : "materialName",
		name : "materialName"
	});
	// 规格型号
//	var txtSpecNo = new Ext.form.TextField({
//		fieldLabel : '规格型号',
//		readOnly : true,
//		anchor : '100%',
//		id : "specNo",
//		name : 'specNo'
//	});
	
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
	
	// txtPurBy.onClick(selectPersonWin);
	// var txtMrByH = new Ext.form.Hidden();
	// txtMrByH.on('render', getUserName)
	// 替代物料流水号
	var hdnAltMatId = new Ext.form.Hidden({
		id : "materialId",
		name : "materialId"
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
	
	var btnExport = new Ext.Button({
		id : 'btnExport',
		text : '导  出',
		minWidth : 70,
		handler : function() {
			myExport();
		}
	});

	var btnPrint = new Ext.Button({
		id : 'btnPrint',
		text : '打  印',
		minWidth : 70,
		handler : function() {
		}
	});
	//-----------add by fyyang 100112--------
	var txtPurNo = new Ext.form.TextField({
		fieldLabel : '采购单号',
		anchor : '100%',
		id : "purNo",
		name : "PurNo"
	});
	
	var txtArrivalNo = new Ext.form.TextField({
		fieldLabel : '到货单号',
		anchor : '100%',
		id : "arrivalNo",
		name : "arrivalNo"
	});
	//----------------------------------------
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
//				{
//				border : false,
//				columnWidth : 0.23,
//				layout : 'form',
//				items : [txtSpecNo]
//			},
			{
				border : false,
				columnWidth : 0.25,
				layout : 'form',
				items : [txtPurBy]
			},
				{
	            border : false,
				columnWidth : 0.23,
				layout : 'form',
				items : [redbill]		
			},{
	            border : false,
				columnWidth : 0.01	
			},{
				border : false,
				columnWidth : 0.11,
				layout : 'form',
				items : [btnExport]
			}, {
				border : false,
				columnWidth : 0.13,
				layout : 'form',
				items : [btnPrint]
			}]
		},
		{
			layout : 'column',
			border : false,
			items : [{
				border : false,
				columnWidth : 0.25,
				layout : 'form',
				items : [txtArrivalNo]
			}, {
				border : false,
				columnWidth : 0.25,
				layout : 'form',
				items : [txtPurNo]
			},{  // modify by ywliu 2010/02/04
				border : false,
				columnWidth : 0.23,
				layout : 'form',
				items : [txtMaterialNo]
			}
			
			]
		}
//		, {
//			layout : 'column',
//			border : false,
//			items : [{
//				border : false,
//				columnWidth : 1,
//				layout : 'form',
//				items : [txtMaterialNo]
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
		url : 'resource/findPurchasehouseList.action',
		totalProperty : 'total',
		root : 'root',
		fields : [{
			name : 'id'
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
		},{name:'memo'},
		{name:'orderDetailModifiedDate'},
		{name : 'relationArrivalNo'} // add by ywliu 20100129
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
				} else if (record.data.purOrderDetailsRcvQty > 0 && record.data.relationArrivalNo != null) {
					return 'x-grid-record-blue';
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
				{
					header : "采购单号",
					anchor : '85%',
					sortable : true,
					dataIndex : 'purNo'
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
					renderer : moneyFormat,
					align : 'right',
					dataIndex : 'purOrderDetailsRcvQty'
				},
				// 单价
				{
					header : "单价",
					width : 75,
					sortable : true,
					renderer : numberFormat,
					align : 'right',
					dataIndex : 'unitPrice'
				},
				// 金额
				{
					header : "金额",
					width : 75,
					sortable : true,
					renderer : numberFormat,
					align : 'right',
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
//				{
//					header : "合同编号",
//					anchor : '85%',
//					sortable : true,
//					dataIndex : 'contractNo'
//				},
//				{
//					header : "发票号",
//					anchor : '85%',
//					sortable : true,
//					dataIndex : 'invoiceNo'
//				},
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
		tbar:[{text:'查看入库明细',
			    	iconCls : Constants.CLS_QUERY,
			    	handler:queryStorageDetail
			  },{
			     text:'查看采购单信息',
			     iconCls : Constants.CLS_QUERY,
			     handler:queryPurInfo
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
	
	function queryPurInfo()
	{
			var record = transPanel.getSelectionModel().getSelected();
		if (record == null) {
			Ext.Msg.alert("提示", "请选择一条记录！");
		} else {
			
			window.showModalDialog('queryPurInfo.jsp', record,
					'status:no;dialogWidth=780px;dialogHeight=450px');
		}
	}

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
			supplyName : Ext.get('supplyName').dom.value,
			purBy : Ext.get('purBy').dom.value,
			redBill : redbill.getValue(),
			purNo:txtPurNo.getValue(),
			arrivalNo:txtArrivalNo.getValue(),
			materialNo:txtMaterialNo.getValue() // add by ywliu 20100204
			
		};
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
			window.showModalDialog('purchaseWhsDetailSelect.jsp', arg,
					'status:no;dialogWidth=750px;dialogHeight=450px');
		}
	}
			// 导出
	function tableToExcel(tableHTML) {
		window.clipboardData.setData("Text", tableHTML);
		try {
			var ExApp = new ActiveXObject("Excel.Application");
			var ExWBk = ExApp.workbooks.add();
			var ExWSh = ExWBk.worksheets(1);
			ExApp.DisplayAlerts = false;
			ExApp.visible = true;
		} catch (e) {
			if (e.number != -2146827859)
				alert("您的电脑没有安装Microsoft Excel软件！");
			return false;
		}
		ExWBk.worksheets(1).Paste;
	}
	function myExport() {
		Ext.Ajax.request({
			// add by ywliu 20100204
			params : {
				startdate : Ext.get('planStartDate').dom.value,
				enddate : Ext.get('planEndDate').dom.value,
				materialName : Ext.get('materialName').dom.value,
				supplyName : Ext.get('supplyName').dom.value,
				purBy : Ext.get('purBy').dom.value,
				redBill : redbill.getValue(),
				purNo:txtPurNo.getValue(),
				arrivalNo:txtArrivalNo.getValue(),
				materialNo:txtMaterialNo.getValue() // add by ywliu 20100204
			},
			url : 'resource/findPurchasehouseList.action',
			success : function(response) {
				var json = eval('(' + response.responseText.trim() + ')');
				var records = json.root;
				// alert(records.length);
				var html = ['<table border=1><tr><th>入库日期</th><th>物料编码</th><th>物料名称</th><th>规格型号</th><th>单位</th><th>入库数</th><th>单价</th><th>金额</th><th>币别</th><th>采购人员</th><th>供应商</th><th>合同编号</th><th>发票号</th></tr>'];
				for (var i = 0; i < records.length; i += 1) {
					var rc = records[i];
					// add by ywliu 201020205
					var arrivalDetailModifiedDate = "";
					var materialNo = "";
					var materialName = "";
					var specNo = "";
					var stockUmId = "";
					var purOrderDetailsRcvQty = "";
					var unitPrice = "";
					var estimatedSum = "";
					var currencyType = "";
					var buyerName = "";
					var supplyName = "";
					var contractNo = "";
					var invoiceNo = "";
					if(rc.arrivalDetailModifiedDate != null) {
						arrivalDetailModifiedDate = rc.arrivalDetailModifiedDate;
					}
					if(rc.materialNo != null) {
						materialNo = rc.materialNo;
					} 
					if(rc.materialName != null) {
						materialName = rc.materialName;
					}
					if(rc.specNo != null) {
						specNo = rc.specNo;
					}
					if(rc.stockUmId != null) {
						stockUmId = rc.stockUmId;
					}
					if(rc.purOrderDetailsRcvQty != null) {
						purOrderDetailsRcvQty = rc.purOrderDetailsRcvQty;
					}
					if(rc.unitPrice != null) {
						unitPrice = rc.unitPrice;
					}
					if(rc.estimatedSum != null) {
						estimatedSum = rc.estimatedSum;
					}
					if(rc.currencyType != null) {
						currencyType = rc.currencyType;
					}
					if(rc.buyerName != null) {
						buyerName = rc.buyerName;
					}
					if(rc.supplyName != null) {
						supplyName = rc.supplyName;
					}
					if(rc.supplyName != null) {
						supplyName = rc.supplyName;
					}
					if(rc.contractNo != null) {
						contractNo = rc.contractNo;
					}
					if(rc.invoiceNo != null) {
						invoiceNo = rc.invoiceNo;
					}
					html.push('<tr><td>' + arrivalDetailModifiedDate + '</td><td>'
							+ materialNo + '</td><td>' + materialName
							+ '</td><td>' + specNo + '</td><td>'
							+ stockUmId + '</td><td>' + purOrderDetailsRcvQty
							+ '</td><td>' + unitPrice + '</td><td>'
							+ estimatedSum + '</td><td>' + currencyType
							+ '</td><td>'
							+ buyerName + '</td><td>' + supplyName
							+ '</td><td>' + contractNo
							+ '</td><td>' + invoiceNo
							+ '</td></tr>');
				}
				html.push('</table>');
				html = html.join(''); // 最后生成的HTML表格
				// alert(html);
				tableToExcel(html);
			},
			failure : function(response) {
				Ext.Msg.alert('信息', '失败');
			}
		});
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