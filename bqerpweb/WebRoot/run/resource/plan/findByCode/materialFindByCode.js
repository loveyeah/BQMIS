Ext.onReady(function() {
	function getParameter(psName) {
		var url = self.location;
		var result = "";
		var str = self.location.search.substring(0);
		if (str.indexOf(psName) != -1
				&& (str.substr(str.indexOf(psName) - 1, 1) == "?" || str
						.substr(str.indexOf(psName) - 1, 1) == "&")) {
			if (str.substring(str.indexOf(psName), str.length).indexOf("&") != -1) {
				var Test = str.substring(str.indexOf(psName), str.length);
				result = Test.substr(Test.indexOf(psName), Test.indexOf("&")
								- Test.indexOf(psName));
				result = result.substring(result.indexOf("=") + 1,
						result.length);
			} else {
				result = str.substring(str.indexOf(psName), str.length);
				result = result.substring(result.indexOf("=") + 1,
						result.length);
			}
		}
		return result;
	}
	var headId = getParameter("headId");
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
/**
	 * 数字格式化
	 */
	function numberFormat(v) {
		
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
  // 物料记录
	var material = Ext.data.Record.create([{
		name : 'requirementDetailId'
	}, {
		name : 'materialId'
	}, {
		name : 'materialName'
	}, {
		name : 'equCode'
	}, {
		name : 'materSize'
	}, {
		name : 'parameter'
	}, {
		name : 'stockUmName'
	}, {
		name : 'appliedQty'
	}, {
		name : 'estimatedPrice'
	}, {
		name : 'left'
	}, {
		name : 'maxStock'
	}, {
		name : 'usage'
	}, {
		name : 'factory'
	}, {
		name : 'needDate'
	}, {
		name : 'supplier'
	}, {
		name : 'memo'
	}, {
		name : 'itemId'
	}, {
		name : 'planOriginalId'
	}, {
		name : 'planOriginalIdHId'
	}, {
		name : 'planOriginalIdHName'
	}, {
		name : 'lastModifiedDate'
	}, {
	    name : 'materialCode'
	},{
	    name : 'issQty'
	},{
		name : 'insQty'
	}
	]);
	// 物料grid的store
	var materialStore = new Ext.data.JsonStore({
		url : 'resource/getMaterial.action',
		root : 'list',
		totalProperty : 'totalCount',
		fields : material
	});

// 申请单编号
	var txtMrNo = new Ext.form.TextField({
		fieldLabel : '申请单编号',
		isFormField : true,
		readOnly : true,
		maxLength : 20,
		id : 'mrNo',
		name : "mr.mrNo",
		anchor : '100%'
	});
	// 申请日期
	var txtMrDate = new Ext.form.TextField({
		fieldLabel : '申请日期',
		readOnly : true,
		width : 100,
		id : 'mrDate',
		name : "mr.mrDate",
		anchor : '100%'
	});
	// 申请人
	var txtMrBy = new Ext.form.TextField({
		fieldLabel : '申请人',
		readOnly : true,
		maxLength : 100,
		name : "mrBy",
		anchor : '100%'
	});
	var txtMrByH = new Ext.form.Hidden({
		name : "mr.mrBy"
	});
	// 申请部门
	var txtMrDept = new Ext.form.TextField({
		fieldLabel : '申请部门',
		readOnly : true,
		maxLength : 100,
		name : "mrDept",
		anchor : '100%'
	});
	
var firstLine = new Ext.Panel({
		border : false,
		height : 30,
		layout : "column",
		style : "padding-top:5px;padding-right:0px;padding-bottom:0px;"
				+ "margin-bottom:0px;margin-left:-5px",
		anchor : '100%',
		items : [{
			columnWidth : 0.25,
			layout : "form",
			border : false,
			items : [txtMrNo]
		}, {
			columnWidth : 0.25,
			layout : "form",
			border : false,
			items : [txtMrDate]
		}, {
			columnWidth : 0.25,
			layout : "form",
			border : false,
			items : [txtMrBy, txtMrByH]
		}, {
			columnWidth : 0.25,
			layout : "form",
			border : false,
			items : [txtMrDept]
		}]
	});
	// 需求日期
	var txtMrDueDate = new Ext.form.TextField({
		fieldLabel : "需求日期<font color ='red'>*</font>",
		readOnly : true,
		name : "mr.dueDate",
		anchor : '100%',
		style : 'cursor:pointer'

		
	});

	// 计划来源
	var txtMrOriginal = new Ext.form.TextField({
		fieldLabel : "计划种类<font color ='red'>*</font>",
		readOnly : true,
		name : "mr.planOriginalId",
		anchor : '100%',
		style : 'cursor:pointer'

		
	});
   

	// 费用来源
	var txtMrItem =   new Ext.form.TextField({
		fieldLabel : '费用来源<font color ="red">*</font>',
		displayField : 'text',
		valueField : 'id',
		blankText : '请选择',
		anchor : '100%',
		name : "itemName",
		readOnly : true,
		hidden : false,
		hideLabel : false
	});
	var txtMrItemH = new Ext.form.Hidden({
		name : 'mr.itemCode',
		value : ''
	})
	
	// 归口部门
	var txtMrCostDept = new Ext.form.TriggerField({
		fieldLabel : '归口部门<font color ="red">*</font>',
		displayField : 'text',
		valueField : 'id',
		blankText : '请选择',
		emptyText : '请选择',
		anchor : '100%',
		name : "costDept",
		allowBlank : false,
		readOnly : true,
		hidden : true,
		hideLabel : true
	});
	
	var txtPlanDateMemo = new Ext.form.TextField({
		name : 'mr.planDateMemo',
		fieldLabel : "月份/季度",
		readOnly : true,
		anchor : '100%'
	});

	// ---------------------------------------------------
	var secondLine = new Ext.Panel({
		border : false,
		height : 30,
		layout : "column",
		style : "padding-top:5px;padding-right:0px;padding-bottom:0px;"
				+ "margin-bottom:0px;margin-left:-5px",
		anchor : '100%',
		items : [{
			columnWidth : 0.25,
			layout : "form",
			border : false,
			items : [txtMrDueDate]
		}, {
			columnWidth : 0.25,
			layout : "form",
			border : false,
			items : [txtMrOriginal]
		}, {
			columnWidth : 0.25,
			layout : "form",
			border : false,
			items : [txtMrItem]
		}, {
			columnWidth : 0.25,
			layout : "form",
			border : false,
			items : [txtPlanDateMemo]
		}]
	});
	 var txtAdviceItem=new Ext.form.TextField({
	  name : 'txtAdviceItem',
		fieldLabel : "本年预算费用",
		readOnly : true,
		anchor : '100%'
	  });
	var txtMrDeptH = new Ext.form.Hidden({
		name : "mr.mrDept"
	});

	 var txtFactItem=new Ext.form.TextField({
	  name : 'txtFactItem',
		fieldLabel : "本年已领费用",
		readOnly : true,
		anchor : '100%'
	  });

	var thirdLine = new Ext.Panel({
		border : false,
		height : 30,
		labelWidth:85,
		layout : "column",
		style : "padding-top:5px;padding-right:0px;padding-bottom:0px;"
				+ "margin-bottom:0px;margin-left:-5px",
		anchor : '100%',
		items : [ {
			columnWidth : 0.25,
			layout : "form",
			border : false,
			items : [txtAdviceItem]
		}, {
			columnWidth : 0.25,
			layout : "form",
			border : false,
			items : [txtFactItem]
		}]
	});
// 备注
	var txtMrMemo = new Ext.form.TextArea({
		fieldLabel : "申请理由<font color ='red'>*</font>",
		readOnly:true,
		maxLength : 150,
		anchor : '99.6%',
		height : Constants.TEXTAREA_HEIGHT,
		id : "mrReason",
		name : "mr.mrReason"

	});
	var forthLine = new Ext.Panel({
		border : false,
		autoHeight : true,
		layout : "column",
		style : "padding-top:5px;padding-right:0px;padding-bottom:0px;"
				+ "margin-bottom:0px;margin-left:-5px",
		anchor : '100%',
		items : [{
			columnWidth : 1,
			layout : "form",
			border : false,
			items : [txtMrMemo]
		}]
	});
	var sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			}) // 选择框的选择 
	var materialGrid = new Ext.grid.EditorGridPanel({
		frame : false,
		border : false,
		enableColumnMove : false,
		clicksToEdit : 0,
		store : materialStore,
		columns : [sm, new Ext.grid.RowNumberer({
			header : "行号",
			width : 35
		}), {	// 明细表ID
			hidden : true,
			dataIndex : 'requirementDetailId'
		}, {
			header : "物料编码",
			width : 100,
			sortable : true,
			dataIndex : 'materialCode'
		}, {	// 物料名称
			header : "物料名称<font color ='red'>*</font>",
			width : 100,
			css : CSS_GRID_INPUT_COL,
			dataIndex : 'materialName'
		}, {
			hidden : true,
			dataIndex : 'materialId'
		}, {	// 设备名称
			header : "设备名称",
			width : 100,
			dataIndex : 'equCode'
		}, {
			hear : "",
			dataIndex : "clickToChose",
			width : 40
			
		}, {	// 规格型号
			header : "规格型号",
			width : 100,
			dataIndex : 'materSize'
		}, {	// 材质/参数
			header : "材质/参数",
			width : 100,
			dataIndex : 'parameter'
		}, {	// 单位
			header : "单位",
			width : 100,
			dataIndex : 'stockUmName'
		}, {	// 申请数量
			header : "申请数量<font color ='red'>*</font>",
			width : 100,
			align : 'right',
			css : CSS_GRID_INPUT_COL,
			renderer : function(value, cellmeta, record, rowIndex, columnIndex,
					store) {
				if (rowIndex < store.getCount() - 1) {
					var appliedQty = record.data.appliedQty;
					// 强行触发renderer事件
					var totalSum = 0;
					for (var i = 0; i < store.getCount() - 1; i++) {
						totalSum += store.getAt(i).get('appliedQty');
					}
					if (store.getAt(store.getCount() - 1).get('isNewRecord') == 'total') {
						store.getAt(store.getCount() - 1).set('appliedQty',
								totalSum);
					}
					
					return moneyFormat(appliedQty);
				} else {
					totalSum = 0;
					for (var i = 0; i < store.getCount() - 1; i++) {
						totalSum += store.getAt(i).get('appliedQty');
					}
					return "<font color='red'>" + moneyFormat(totalSum)
							+ "</font>";
				}
			},
			dataIndex : 'appliedQty'
		}, {	// 估计单价 modify By ywliu 09/05/14
			header : "估计单价<font color ='red'>*</font>",
			width : 100,
			align : 'right',
			renderer : function(value, cellmeta, record, rowIndex, columnIndex,
					store) {
				if (rowIndex < store.getCount() - 1) {
					return moneyFormat(value)
				}
			},
			css : CSS_GRID_INPUT_COL,
			dataIndex : 'estimatedPrice'
		}, {	// 估计金额
			header : "估计金额",
			width : 100,
			align : 'right',
			renderer : function(value, cellmeta, record, rowIndex, columnIndex,
					store) {
				if (rowIndex < store.getCount() - 1) {
					var subSum = record.data.appliedQty
							* record.data.estimatedPrice;
					// 强行触发renderer事件
					var totalSum = 0;
					record.set('estimatedSum', subSum);
					for (var i = 0; i < store.getCount() - 1; i++) {
						totalSum += store.getAt(i).get('estimatedSum');
					}
					if (store.getAt(store.getCount() - 1).get('isNewRecord') == 'total') {
						store.getAt(store.getCount() - 1).set('estimatedSum',
								totalSum);
					}
					return moneyFormat(subSum);
				} else {
					totalSum = 0;
					for (var i = 0; i < store.getCount() - 1; i++) {
						totalSum += store.getAt(i).get('estimatedSum');
					}
					return "<font color='red'>" + moneyFormat(totalSum)
							+ "</font>";
				}
			},
			dataIndex : 'estimatedSum'
		}, {	// 当前库存
			header : "当前库存",
			width : 100,
			align : 'right',
			renderer : function(value, cellmeta, record, rowIndex, columnIndex,
					store) {
				if (rowIndex < store.getCount() - 1) {
					return numberFormat(value)
				}
			},
			dataIndex : 'left'
		}, {	// 最大库存
			hidden : true,
			dataIndex : 'maxStock'
		}, {	// 计划来源
			header : "计划种类<font color ='red'>*</font>",
			witdh : 100,
			renderer : function(v, index, record) {

				return record.get("planOriginalIdHName");
			},
			css : CSS_GRID_INPUT_COL,
			hidden : true,
			dataIndex : 'planOriginalIdHId'

		}, {
			hidden : true,
			dataIndex : 'planOriginalIdHName'
		}, {
			hidden : true,
			renderer : function(v, index, record) {
				if (v != "" && v != null && v != "undefined") {
					Ext.Ajax.request({
						params : {
							planOrigianlId : v
						},
						url : 'resource/getOrigianlName.action',
						method : 'post',
						success : function(result) {
							var recordSub = eval('(' + result.responseText
									+ ')');
							if (recordSub.data != null && recordSub.data != "") {
								var text = recordSub.data['planOriginalDesc'];
								record.set("planOriginalIdHName", text);
								record.set("planOriginalIdHId", text);
							}
						}

					});
					return v;
				} else {
					return "";
				}
			},
			dataIndex : 'planOriginalId'
		}, {	// 费用来源
			header : "费用来源<font color ='red'>*</font>",
			witdh : 100,
			hidden : true,
			renderer : function(val, cellmeta, record, rowIndex, columnIndex,
					store) {
				if (rowIndex < store.getCount() - 1) {
					return txtMrItem.getValue();
				} else {
					return "";
				}

			},
			dataIndex : 'itemId'
		}, {	// 用途
			header : "用途",
			width : 100,
			css : CSS_GRID_INPUT_COL,
			renderer : function(v, cellmeta, record, rowIndex, columnIndex,
					store) {
				if (rowIndex = store.getCount() - 1) {
				}
				return v;
			},
			dataIndex : 'usage'
		}, {	// 生产厂家
			header : "生产厂家",
			width : 100,
			dataIndex : 'factory'
		}, {	// 需求日期
			header : "需求日期<font color ='red'>*</font>",
			width : 100,
			css : CSS_GRID_INPUT_COL,
			dataIndex : 'needDate'
		}, {	// 建议供应商
			header : "建议供应商",
			width : 100,
			css : CSS_GRID_INPUT_COL,
			dataIndex : 'supplier'
		}, {	// 备注
			header : "备注",
			width : 145,
			dataIndex : 'memo',
			css : CSS_GRID_INPUT_COL
			
		}, {
			hidden : true,
			dataIndex : 'lastModifiedDate'
		}, {
					header : "已到货数",
					width : 100,
					sortable : true,
					align : 'right',
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						// 新增合计申请数量 add by ywliu 0090722
						if (rowIndex < store.getCount() - 1) {
							// 强行触发renderer事件
							var totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('insQty');
								
							}
					
							if (store.getAt(store.getCount() - 1)
									.get('isNewRecord') == 'total') {
								store.getAt(store.getCount() - 1).set(
										'insQty', totalSum);
							}
								
							
					   		//--------------------------------	
							
							if (record.get('useFlag') == 'C' && value != null)
							{
									return "<font color='red'>"
									+ numberFormat(value) + "</font>";	
							}
							else
							{
								return numberFormat(value);
							}
						
						} else {
							totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('insQty');
							}
							return "<font color='red'>"
									+ numberFormat(totalSum) + "</font>";
						}
					},
					dataIndex : 'insQty'
					
				},  {
					header : "已领数量",
					width : 100,
					sortable : true,
					align : 'right',
					renderer : function(value, cellmeta, record, rowIndex, columnIndex,
							store) {
								// 新增合计申请数量 add by ywliu 0090722
						if (rowIndex < store.getCount() - 1) {
							// 强行触发renderer事件
							var totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('issQty');
							}
							if (store.getAt(store.getCount() - 1).get('isNewRecord') == 'total') {
								store.getAt(store.getCount() - 1).set('issQty',
										totalSum);
							}
							return numberFormat(value);
						} else {
							totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('issQty');
							}
							return "<font color='red'>" + numberFormat(totalSum)
									+ "</font>";
						}
					},
					dataIndex : 'issQty'
				} ],
		sm :sm
	});
	
		var fifthLine = new Ext.Panel({
		border : false,
		layout : "column",
		height:600,
		anchor : '100%',
		
		items : [{
			columnWidth : 1,
			layout : "fit",
			border : false,
			height:550,
			autoScroll : true,
			items : [materialGrid]
		}]
	});
	function addLine() {
		// 统计行
		var record = new material({
			requirementDetailId : "",
			materialId : "",
			materialName : "",
			equCode : "",
			materSize : "",
			parameter : "",
			stockUmName : "",
			appliedQty : "",
			estimatedPrice : "",
			left : "",
			maxStock : "",
			usage : "",
			factory : "",
			needDate : "",
			supplier : "",
			memo : "",
			itemId : "",
			planOriginalId : "",
			planOriginalIdHId : "",
			planOriginalIdHName : "",
			lastModifiedDate : "",
			isNewRecord : "total"
		});
		// 原数据个数
		var count = materialStore.getCount();
		for (i = 0; i < count; i++) {
			var record2 = materialStore.getAt(i);
		}
		// 停止原来编辑
		materialGrid.stopEditing();
		// 插入统计行
		materialStore.insert(count, record);
		materialGrid.getView().refresh();
//		totalCount = materialStore.getCount() - 1;

	};
	function  query()
	{
	materialStore.on('beforeload', function() {
		Ext.apply(this.baseParams, {
			headId:headId
			
			
		});
	});
	materialStore.load({
		params : {
			start : 0,
			limit : Constants.PAGE_SIZE
		}
	});
	}
	// --------第五行grid--结束
	// 表单panel
	var formPanel = new Ext.FormPanel({
		labelAlign : 'right',
		labelPad : 15,
		labelWidth : 75,
		height : 200,
		border : false,
		region : 'north',
		items : [firstLine, secondLine, thirdLine, forthLine,
				fifthLine]
	});
	
	var registerPanel = new Ext.Panel({
		frame : false,
		border : false,
		layout : 'fit',
		items : [formPanel]
	});
	
	var layout = new Ext.Viewport({
		layout : 'fit',
		margins : '0 0 0 0',
		region : 'center',
		border : true,
		items : [registerPanel]
	}); 
	//初始化页面数据
	function initInfo(headId) {
		if (headId != "" && headId != null) {
			Ext.Ajax.request({
				params : {
					headId : headId
				},
				url : 'resource/getFormInfo.action',
				method : 'post',
				success : function(result, request) {
					var record = eval('(' + result.responseText + ')');
//					 alert(result.responseText)
					formPanel.getForm().loadRecord(record);
					txtPlanDateMemo.setValue(record.data['planDateMemo']);
					txtMrDate.setValue(renderDate(record.data['mrDate']));
					txtMrDueDate.setValue(renderDate(record.data['dueDate']));
					txtMrCostDept.setValue(record.data['costDept']);
					if (record.data['planOriginalId'] != "") {
						Ext.Ajax.request({
							params : {
								planOrigianlId : record.data['planOriginalId']
							},
							url : 'resource/getOrigianlName.action',
							method : 'post',
							success : function(result) {
								var recordSub = eval('(' + result.responseText
										+ ')');
								var obj = new Object();
								if (recordSub.data != null
										&& recordSub.data != "") {
									obj.text = recordSub.data['planOriginalDesc'];
									obj.id = parseInt(record.data['planOriginalId']);
									txtMrOriginal.setValue(obj.text);
						
								}
							},
							failure : function(action) {
							}

						});
					}
					txtMrItem.setValue(getBudgetName(record.data['itemCode']));
				
					txtAdviceItem.setValue(getBudgetName(record.data['itemCode'],1,txtMrDeptH.getValue()));
					txtFactItem.setValue(getBudgetName(record.data['itemCode'],2,txtMrDeptH.getValue()));
					txtMrItemH.setValue(record.data['itemCode']);
					

				},
				failure : function(action) {
				}
			});
			materialStore.load({
				params : {
					headId : headId
				},
				callback : addLine
			});
		}
	}
	/**
	 * 初始化页面数据
	 */
	function getInitData(headId) {
		if (headId != "" && headId != null) {
			initInfo(headId);
		} else {
			
		}

	}
//	getInitData(headId);
		getInitData(headId);
	
});