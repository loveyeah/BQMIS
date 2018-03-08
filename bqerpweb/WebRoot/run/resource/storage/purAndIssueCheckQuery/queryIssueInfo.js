Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Constants.RS004_E_001 = "当前{0}可供出库数量不足。";
Constants.RS004_E_002 = "本次发货数量不能大于待发货数量";
Constants.RS004_I_002 = "没有物料出库。";

Ext.onReady(function() {
	
		var startDate=window.dialogArguments.startDate;
		var endDate=window.dialogArguments.endDate;
	
	/**
	 * 金钱格式化
	 */
	function moneyFormat(v) {
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

	Ext.QuickTips.init();
	// ↓↓*******************领料单表头***************************

	// 领料单表头grid的store
	var issueHeaderStore = new Ext.data.JsonStore({
		url : 'resource/getIssueHeadListForCheck.action',
		root : 'list',
		totalProperty : 'totalCount',
		fields : [
				// 领料单流水号
				{
					name : 'issueHeadId'
				},
				// 编号
				{
					name : 'issueNo'
				},
				// 申请领用日期
				{
					name : 'dueDate'
				},
				// 计划来源
				{
					name : 'planOriginalDesc'
				},
				// 费用来源ID
				{
					name : 'itemCode'
				},
				// TODO 费用来源名称
				{
					name : 'itemName'
				},
				// 备注
				{
					name : 'memo'
				},
				// 工单编号
				{
					name : 'woNo'
				},
				// 需求单编号
				{
					name : 'mrNo'
				},
				// 申请领料人
				{
					name : 'receiptBy'
				},
				// 领用部门
				{
					name : 'receiptDep'
				},
				// 归口部门
				{
					name : 'feeByDep'
				},
				// 是否紧急领用
				{
					name : 'isEmergency'
				},
				// 上次修改时间
				{
					name : "lastModifiedDate"
				}, {
					name : "workFlowNo"
				}]
	});
	

	// ↑↑*******************领料单表头***************************
	// ↓↓*******************领料单明细***************************
	// 领料单表头流水号
	var hdnIssueHeadId = new Ext.form.Hidden({
		id : 'issueHeadId',
		value : ""
	});
	var hdnLastModifiedDate = new Ext.form.Hidden({
		id : "lastModifiedDate",
		value : ""
	});
	// 需求单编号
	var hdnMrNo = new Ext.form.Hidden({
		id : "mrNo",
		value : ""
	});
	// 事务作用码
	var bussinessCode = new Ext.form.TextField({
		id : "bussinessCode",
		fieldLabel : '事务作用码',
		readOnly : true,
		hidden : true,
		hideLabel : true,
		anchor : "90%",
		name : ''
	});

	// 事务作用名称
	var bussinessName = new Ext.form.TextField({
		id : "bussinessName",
		fieldLabel : '事务作用名称',
		readOnly : true,
		hidden : true,
		hideLabel : true,
		anchor : "90%",// "100%",
		name : ''
	});
	// 领料单编号
	var issueCode = new Ext.form.TextField({
		id : "issueNo",
		fieldLabel : '领料单编号',
		readOnly : true,
		anchor : "100%",// "100%",
		name : ''
	});
	// 计划来源
	var txtLastModifyDate = new Ext.form.TextField({
		id : "lastModifyDate",
		fieldLabel : '日期',
		readOnly : true,
		anchor : "100%",// "100%",
		name : ''
	})
	
	var txtRealGetPerson = new Ext.form.TextField({
	    id : "getRealPerson",
		fieldLabel : '领料人',
		readOnly : true,
		anchor : "100%",// "100%",
		name : ''
	});


	var txtReceiptByName = new Ext.form.TextField({
		id : "receiptByName",
		fieldLabel : '申请人',
		readOnly : true,
		anchor : "100%",// "100%",
		name : ''
	});
	
	// 操作员
	var txtReceiptDept = new Ext.form.TextField({
		id : "receiptDept",
		fieldLabel : '申请部门',
		readOnly : true,
		anchor : "100%",// "100%",
		name : ''
	});

	// 备注
	var detailMemo = new Ext.form.TextArea({
		id : "memo",
		maxLength : 128,
		height : Constants.TEXTAREA_HEIGHT,
		fieldLabel : '备注',
		anchor : "94.5%",// "100%",
		name : 'memo',
		readOnly : true

	});
	// ↑↑*******************领料单明细***************************
	// 物资详细grid有无变更flag
	var blnChanged = false;
	// 正在编辑的领料单数据
	var editHeadRecord = {};
	// ↓↓*******************物资详细grid***************************
	// 物资详细grid记录
	var rightData = Ext.data.Record.create([
			// 物料ID
			{
				name : 'materialId'
			},
			// 领料单明细id
			{
				name : 'issueDetailsId'
			},
			// 物料编码
			{
				name : 'materialNo'
			},
			// 物料名称
			{
				name : 'materialName'
			},
			// TODO 单位名称取得？单位
			{
				name : 'stockUmId'
			},
			// 单位名称
			{
				name : 'unitName'
			},
			// 需求数量
			{
				name : 'demandNum'
			},
			// 实际数量
			{
				name : 'actIssuedCount'
			},
			// 代发货数量
			{
				name : 'waitNum'
			},
			// 本次发货数量
			{
				name : 'thisNum'
			},
			// 批号
			{
				name : 'lotNo'
			},
			// 仓库
			{
				name : 'whsNo'
			},
			// 库位
			{
				name : 'locationNo'
			},
			// TODO 费用来源
			{
				name : 'itemCode'
			},
			// 保管员
			{
				name : 'save'
			},
			// 备注
			{
				name : 'memo'
			},
			// 需求计划明细id
			{
				name : 'requirementDetailId'
			},
			// 是否无批号
			{
				name : 'noLotNo'
			},
			// 上次修改时间
			{
				name : 'lastModifiedDate'
			},
			// 单价 add by drdu 091126
			{
				name : 'unitPrice'
			},
			// 总金额 add by drdu 091126
			{
				name : 'actPrice'
			}]);
	// 采购详细列表的store
	var rightStore = new Ext.data.JsonStore({
		url : 'resource/getMaterialDetailListForCheck.action',
		root : 'list',
		totalProperty : 'totalCount',
		fields : rightData
	});
	rightStore.on('beforeload', function() {
		Ext.apply(this.baseParams, {
			//issueHeadId : hdnIssueHeadId.getValue()
			issueNo:issueCode.getValue(),
			issueDate:'',
			startCheckDate:startDate,
			endCheckDate:endDate
		})
	});

		var detailSm = new Ext.grid.CheckboxSelectionModel();
	// 物资详细列表grid
	var rightGrid = new Ext.grid.EditorGridPanel({
		region : "center",
		autoScroll : true,
		style : "border-top:solid 1px",
		border : false,
		enableColumnMove : false,
		clicksToEdit : 1,
		store : rightStore,
		sm :detailSm,
		columns : [
				// 行号
				new Ext.grid.RowNumberer({
					header : "行号",
					width : 35
				}),
				// 物料编码
				{
					header : "物料编码",
					width : 100,
					sortable : true,
					dataIndex : 'materialNo'
				},
				// 物料名称
				{
					header : "物料名称",
					width : 150,
					sortable : true,
					dataIndex : 'materialName'
				},
				// 单位
				{
					header : "单位",
					width : 30,
					sortable : true,
					dataIndex : 'unitName'
				},
				// 单价
				{
					header : "单价",
					width : 75,
					align : 'right',
					sortable : true,
					dataIndex : 'unitPrice'
				},
				// 需求数量
				{
					header : "申请数量",
					width : 75,
					align : 'right',
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (rowIndex < store.getCount() - 1) {
							// 强行触发renderer事件
							var totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('demandNum');
							}
							if (store.getAt(store.getCount() - 1)
									.get('isNewRecord') == 'total') {
								store.getAt(store.getCount() - 1).set(
										'demandNum', totalSum);
							}
//							return numRenderer(value);
							return  moneyFormat(value) ;
						} else {
							totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('demandNum');
							}
//							return "<font color='red'>" + numRenderer(totalSum)
//									+ "</font>";
							return "<font color='red'>" + moneyFormat(totalSum)
									+ "</font>";
						}
					},
					sortable : true,
					dataIndex : 'demandNum'
				},
				// 实际数量
				{
					header : "实发数量",
					width : 75,
					align : 'right',
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (rowIndex < store.getCount() - 1) {
							// 强行触发renderer事件
							var totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i)
										.get('actIssuedCount');
							}
							if (store.getAt(store.getCount() - 1)
									.get('isNewRecord') == 'total') {
								store.getAt(store.getCount() - 1).set(
										'actIssuedCount', totalSum);
							}
							//return numRenderer(value);
							return moneyFormat(value);
						} else {
							totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i)
										.get('actIssuedCount');
							}
//							return "<font color='red'>" + numRenderer(totalSum)
//									+ "</font>";
							return "<font color='red'>" + moneyFormat(totalSum)
									+ "</font>";
						}
					},
					sortable : true,
					dataIndex : 'actIssuedCount'
				},
				// 总金额
				{
					header : "总金额",
					width : 85,
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (rowIndex < store.getCount() - 1) {
							// 强行触发renderer事件
							var totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i)
										.get('actPrice');
							}
							if (store.getAt(store.getCount() - 1)
									.get('isNewRecord') == 'total') {
								store.getAt(store.getCount() - 1).set(
										'actPrice', totalSum);
							}
							//return moneyFormat(value);
							return value.toFixed(2);
						} else {
							totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i)
										.get('actPrice');
							}
							
							return "<font color='red'>" + totalSum.toFixed(2)
									+ "</font>";
//							return "<font color='red'>" + moneyFormat(totalSum)
//									+ "</font>";
						}
					},
					sortable : true,
					align : 'right',
					dataIndex : 'actPrice'
				},
				{//审核时间
					header : "审核时间",
					width : 115,
					align : 'center',
					sortable : true,
					dataIndex : 'lastModifiedDate'
				}
				]

	});
	



	
	// ↑↑*******************物资详细grid***************************
	var wd = 25;
	var headForm = new Ext.form.FieldSet({
		border : false,
		labelAlign : 'right',
		style : {
			'border' : 0
		},
		layout : 'form',
		labelWidth : 80,
		width : 750,

		autoHeight : true,
		items : [{
			layout : "column",
			border : false,
			style : "padding-top:5px;",
			items : [
					// 事务作用码
					{
						columnWidth : 0.48,
						layout : "form",
						border : false,
						height : wd,
						labelAlign : "right",
						items : [hdnIssueHeadId, hdnLastModifiedDate, hdnMrNo,
								bussinessCode]
					},
					// 事务作用名称
					{
						columnWidth : 0.48,
						layout : "form",
						border : false,
						height : wd,
						labelAlign : "right",
						items : [bussinessName]
					}]
		}, {
			layout : "column",
			border : false,
			items : [
			// 领料单编号
			{
				columnWidth : 0.3,
				layout : "form",
				border : false,
				height : wd,
				labelAlign : "right",
				items : [issueCode]
			},{
				columnWidth : 0.3,
				layout : "form",
				border : false,
				height : wd,
				labelAlign : "right",
				items : [txtLastModifyDate]
			},{
				columnWidth : 0.3,
				layout : "form",
				border : false,
				height : wd,
				labelAlign : "right",
				items : [txtRealGetPerson]
			}]
		}, {
			layout : "column",
			border : false,
			items : [
					{
						columnWidth : 0.3,
						layout : "form",
						border : false,
						height : wd,
						labelAlign : "right",
						items : [txtReceiptByName]
					},
					{
						columnWidth : 0.6,
						layout : "form",
						border : false,
						height : wd,
						labelAlign : "right",
						items : [txtReceiptDept]
					}]
		},  {
			layout : "column",
			border : false,
			items : [
			// 备注
			{
				columnWidth : 0.96,
				layout : "form",
				border : false,
				height : 49,
				labelAlign : "right",
				items : [detailMemo]
			}]
		}]
	});

	

	var detailPanel = new Ext.FormPanel({
		style : "padding-top:1.5px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
		labelAlign : 'right',
		items : [headForm]
	});


		new Ext.Viewport({
				layout : "border",
				items : [ {
							region : 'north',
							height : 160,
							autoScroll : true,
							items : [detailPanel]
						}, {
							region : 'center',
							layout : 'fit',
							autoScroll : true,
							items : [rightGrid]
						}]
			});

	// ↓↓********************处理***********************
	function addLine() {
		// 统计行
		var record = new rightData({
			id : "",
			arrivalNo : "",
			memo : "",
			lotCode : "",
			rcvQty : "",
			operateBy : "",
			materialId : "",
			materialName : "",
			isLot : "",
			materialClassId : "",
			costMethod : "",
			stdCost : "",
			defaultWhsNo : "",
			demandNum : "",
			actIssuedCount : "",
			waitNum : "",
			thisNum : "",
			unitPrice : "",
			dueDate : "",
			taxRate : "",
			stockUmId : "",
			canQty : "",
			isNewRecord : "total"
		});
		// 原数据个数
		var count = rightStore.getCount();
		// 停止原来编辑
		rightGrid.stopEditing();
		// 插入统计行
		rightStore.insert(count, record);
		rightGrid.getView().refresh();
		totalCount = rightStore.getCount() - 1;

	};

	function queryRecord() {
			issueHeaderStore.baseParams = {
			checkStatus : checkStatus.getValue(),
			issueNoFuzzy : issueNoFuzzy.getValue()
		};
		issueHeaderStore.load({
			params : {
				start : 0,
				limit : Constants.PAGE_SIZE
			}
		});
		issueHeaderStore.on("load", check);
	}
	




	// add by drdu 091103
	function check() {
		var status;
		status = checkStatus.getValue();
		if (status == '2') {
			Ext.get('checkBtn').dom.disabled = false
			Ext.get('cancelBtn').dom.disabled = true
		} else if (status == 'C') {
			Ext.get('checkBtn').dom.disabled = true
			Ext.get('cancelBtn').dom.disabled = false
		}
	}

	/**
	 * 表头双击处理
	 */
	function issueHeadDblClick() {

		loadDetailDatas();
	}

	/**
	 * 画面初期化
	 */
	function initPanelDatas() {
		// form初期化
		detailPanel.getForm().reset();
		issueCode.setValue("");
		sourceType.setValue("");
		costSource.setValue("");
		operateDate.setValue("");
		operator.setValue("");
		detailMemo.setValue("");
		detailMemo.originalValue = "";
		// bug对应 UT-BUG-RS004-029
		hdnIssueHeadId.setValue("");
		hdnIssueHeadId.originalValue = "";
		hdnLastModifiedDate.setValue("");
		hdnLastModifiedDate.originalValue = "";
		// 领料单数据初始化
		editHeadRecord = {};
		// 物资详细grid初期化
		rightStore.removeAll();
		
		queryRecord();
	}

	/**
	 * 载入明细数据
	 */
	function loadDetailDatas() {
		
		Ext.Msg.wait("正在查询数据，请等待...");
		// 载入表单数据
		
		editHeadRecord = window.dialogArguments.record;
	  
		
		detailPanel.getForm().trackResetOnLoad = true, detailPanel.getForm()
				.loadRecord(editHeadRecord);
				if(editHeadRecord.data.lastModifyDate!=null)
				{
				 txtLastModifyDate.setValue(editHeadRecord.data.lastModifyDate.substring(0,10));
				}
		birtEntryDate = editHeadRecord.data.dueDate;

		rightStore.load({
			params : {
				start : 0,
				limit : Constants.PAGE_SIZE
			},
			callback : addLine
		});
		

	}

	rightStore.on("load", function() {
		Ext.Msg.hide();
	});

	/**
	 * 获取当前日时
	 */
	function getSysDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate();
		s += (t > 9 ? "" : "0") + t;
		return s;
	}
	/**
	 * 格式化日期字符串
	 */
	function dateRenderer(value) {
		if (!value)
			return "";

		var reDate = /\d{4}\-\d{2}\-\d{2}/gi;
		var reTime = /\d{2}:\d{2}:\d{2}/gi;

		var strDate = value.match(reDate);
		var strTime = value.match(reTime);
		if (!strDate)
			return "";
		return strDate;
	}
	/**
	 * 数字格式化
	 */
	function numRenderer(value) {
		if (value === "" || value == null) {
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
			if (sub.length > 3) {
				sub = sub.substring(0, 3);
			}
		}
		var r = /(\d+)(\d{3})/;
		while (r.test(whole)) {
			whole = whole.replace(r, '$1' + ',' + '$2');
		}
		v = whole + sub;
		return v;
	}

	
issueHeadDblClick();
});
