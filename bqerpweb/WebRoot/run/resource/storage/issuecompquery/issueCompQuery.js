Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {
	var queryType = "";
	var issueApprove = new IssueApprove();

	function renderDate(value) {
		if (!value)
			return "";
		var reDate = /\d{4}\-\d{2}\-\d{2}/gi;
		var strDate = value.match(reDate);
		if (!strDate)
			return "";
		return strDate;
	}
	var wd = 90;
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
		s += '-';
		s += (day > 9 ? "" : "0") + day;
		return s;
	}
	/**
	 * 获取当前日期
	 */
	function getCurrentDateTo() {
		var d, s, t;
		d = new Date();
		var day = d.getDate();
		d.setDate(d.getDate());
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t;
		s += '-';
		s += (day > 9 ? "" : "0") + day;
		return s;
	}
	// ------------查询条件------------------------

	// 起始时间选择器
	var fromDate = new Ext.form.DateField({
		id : 'beginDate',
		name : 'beginDate',
		// format : 'Y-m-d',
		width : 85,
		allowBlank : false,
		readOnly : true,
		value : getCurrentDateFrom(),
		fieldLabel : '起始时间'
	});

	// 结束时间选择器
	var toDate = new Ext.form.DateField({
		id : 'endDate',
		name : 'endDate',
		format : 'Y-m-d',
		width : 85,
		allowBlank : false,
		readOnly : true,
		value : getCurrentDateTo(),
		fieldLabel : '结束时间'
	});

	// 领料人
	var issueBy = new Ext.form.TextField({
		name : 'issueBy',
		fieldLabel : '领料人：'

	});
	// 申请人
	var applyBy = new Ext.form.TriggerField({
		isFormField : true,
		width : wd,
		fieldLabel : "申请人",
		readOnly : true,
		onTriggerClick : function() {
			selectApplyPersonWin();
		}
	});
	// 申请人隐藏域
	var hdnApplyBy = new Ext.form.Hidden({
		id : "applyBy",
		isFormField : true,
		name : "headInfo.applyBy"
	});
	var issueDept = new Ext.form.TextField({
		name : 'issueDept',
		fieldLabel : '领用部门：',
		width : 130
	});
	var materialName = new Ext.form.TextField({
		name : 'materialName',
		fieldLabel : '物料名称：',
		width : 100
	});
	// ------add by fyyang 20100113----------
	var txtIssueNo = new Ext.form.TextField({
		name : 'issueNo',
		fieldLabel : '领料单号：',
		width : 140
	});
	// ---------------------------------------------

	// 定义审批状态
	var stateComboBox = new Ext.form.ComboBox({
		id : "stateCob",
		store : issueApprove.allQueryStatus,
		displayField : "name",
		valueField : "value",
		mode : 'local',
		triggerAction : 'all',
		hiddenName : 'stateComboBox',
		readOnly : true,
		value : '',
		width : 135
	});
	// 定义出库状态
	var cbIssueStatus = new Ext.form.ComboBox({
		id : "cbIssueStatusid",
		store : [['', '所有'], ['1', '未出库'], ['2', '已出库'], ['3', '部分出库']],
		displayField : "name",
		valueField : "value",
		mode : 'local',
		triggerAction : 'all',
		hiddenName : 'cbIssueStatus',
		readOnly : true,
		value : '',
		width : 70
	});

	// ------------------------------------
	/**
	 * 获取申请人的姓名和工号
	 */
	function getUserName(init) {
		// queryStoreFresh();
	}
	function getChooseQueryType() {
		var list = document.getElementsByName("rightRadio");
		for (var i = 0; i < list.length; i++) {
			if (list[i].checked) {
				return list[i].id;
			}
		}
	}

	var reportRadio = new Ext.form.Radio({
		id : 'report',
		name : 'rightRadio',
		// modified by liuyi 20100317
		// boxLabel : '我上报的',
		boxLabel : '我的',
		width : 90,
		hideLabel : true,
		// checked : true,
		listeners : {
			check : function() {
				var type = getChooseQueryType();
				switch (type) {
					case 'report' : {
						queryType = '1';
						applyBy.setValue(currentUserName);
						hdnApplyBy.setValue(currentUser);
						issueDept.setValue(currentDeptName);
						issueDept.setDisabled(true);
						queryStoreFresh();
						break;
					}
					case 'approve' : {
						queryType = '2';
						applyBy.setValue("");
						hdnApplyBy.setValue("");
						issueDept.setValue("");
						issueDept.setDisabled(false);
						queryStoreFresh();
						break;
					}
					case 'all' : {
						queryType = '3';
						applyBy.setValue("");
						hdnApplyBy.setValue("");
						issueDept.setValue("");
						issueDept.setDisabled(false);
						queryStoreFresh();
						break;
					}
					case 'dept' : {
						queryType = '4';
						applyBy.setValue("");
						hdnApplyBy.setValue("");
						issueDept.setValue(currentDeptName);
						issueDept.setDisabled(true);
						queryStoreFresh();
						break;
					}
				}
				// findFuzzy();
			}
		}
	});

	var deptRadio = new Ext.form.Radio({
		id : 'dept',
		name : 'rightRadio',
		hideLabel : true,
		// disabled : true,
		width : 90,
		boxLabel : ' 本部门的'
	});

	var approveRadio = new Ext.form.Radio({
		id : 'approve',
		name : 'rightRadio',
		hideLabel : true,
		// disabled : true,
		width : 90,
		boxLabel : ' 我审批的'
	});

	var allRadio = new Ext.form.Radio({
		id : 'all',
		name : 'rightRadio',
		hideLabel : true,
		labelSeparator : '',
		width : 90,
		boxLabel : '所有的'
	});
	// -------------------------------------
	// ------------------------------------------
	var queryRecord = new Ext.data.Record.create([
			// 领料单ID
			{
				name : 'issueHeadId'
			}, // 单据状态
			{
				name : 'issueStatus'
			},
			// 领料单编号，
			{
				name : 'issueNo'
			},
			// 项目编号
			{
				name : 'projectCode'
			},
			// 工单编号
			{
				name : 'woNo'
			},
			// 成本分摊项目编码
			{
				name : 'costItemId'
			},
			// 费用来源ID
			{
				name : 'itemCode'
			},
			// 需求计划申请单编号
			{
				name : 'mrNo'
			},// 申请领料人
			{
				name : 'receiptBy'
			},// 领用部门
			{
				name : 'receiptDep'
			},// 申请领用日期
			{
				name : 'dueDate'
			},// 费用归口部门
			{
				name : 'feeByDep'
			},// 费用归口专业
			{
				name : 'feeBySpecial'
			},// 备注
			{
				name : 'memo'
			},// 单据状态
			{
				name : 'issueStatus'
			},// 是否紧急领用
			{
				name : 'isEmergency'
			},// 上次修改日期
			{
				name : 'lastModifiedDate'
			}, {
				name : 'workFlowNo'
			}, {
				name : 'planOriginalId'
			}, {
				// 需求计划表头
				name : 'requimentHeadId'
			}, {
				name : 'enterpriseCode' // 标识出库情况
			}]);
	// 领料单grid的store
	var queryStore = new Ext.data.JsonStore({
		url : 'resource/getIssueInfoList.action',
		root : 'list',
		totalProperty : 'totalCount',
		fields : queryRecord
	});

	// 根据条件加载queryStore
	function queryStoreFresh() {
		queryStore.baseParams = {
			fromDate : Ext.get("beginDate").dom.value,
			toDate : Ext.get("endDate").dom.value,
			applicationBy : hdnApplyBy.getValue(),
			acceptDept : issueDept.getValue(),
			materialName : materialName.getValue(),
			receiveStatus : cbIssueStatus.getValue(),
			issueStatus : stateComboBox.getValue(),
			queryType : queryType,
			issueNo : txtIssueNo.getValue()

		};
		queryStore.load({
			params : {
				start : 0,
				limit : Constants.PAGE_SIZE
			}
		});
		materialStore.removeAll();

	}

	// ---------------add by fyyang 20100315-------------

	var txtAdviceItem = new Ext.form.TextField({
		name : 'txtAdviceItem',
		fieldLabel : "本年预算费用",
		readOnly : true,
		width:80
	});

	var txtFactItem = new Ext.form.TextField({
		name : 'txtFactItem',
		fieldLabel : "本年已领费用",
		readOnly : true,
		width:80
	});
	// --------------------------------------------------

	// 查询grid
	var queryGrid = new Ext.grid.GridPanel({
		// region : "center",
		renderTo : "mygrid",
		border : false,
		sm : new Ext.grid.RowSelectionModel({
			singleSelect : true
		}),
		fitToFrame : true,
		store : queryStore,
		columns : [new Ext.grid.RowNumberer({
			header : "行号",
			width : 35
		}),
				// 领料单编号
				{
					header : "领料单编号",
					sortable : true,
					width : 60,
					dataIndex : 'issueNo'
				},
				// 单据状态
				{
					header : "单据状态",
					sortable : true,
					width : 80,
					renderer : function(value) {
						return issueApprove.getStatusName(value);

					},
					dataIndex : 'issueStatus'
				},
				// 需求计划单编号
				{
					header : "需求计划单编号",
					sortable : true,
					width : 80,
					dataIndex : 'mrNo'
				},
				// 申请领料人
				{
					header : "申请领料人",
					sortable : true,
					width : 70,
					renderer : function(val) {
						// 查找对应的申请人名字
						var url = "resource/getReceiptByName.action?receiptBy="
								+ val;
						var conn = Ext.lib.Ajax.getConnectionObject().conn;
						conn.open("POST", url, false);
						conn.send(null);
						if (conn.status == 200) {
							return conn.responseText;
						}

						return "";
					},
					dataIndex : 'receiptBy'
				}, {
					header : "申请部门",
					sortable : true,
					width : 100,
					dataIndex : 'receiptDep',
					renderer : function(val) {
						if (val != null && val != "") {
							var start = val.indexOf('/');
							return val.substring(0, start);
						}
					}

				},
				// 日期
				{
					header : "申请日期",
					sortable : true,
					width : 70,
					renderer : renderDate,
					dataIndex : 'dueDate'  
				},
				// 日期
				{
					header : "单据种类",
					sortable : true,
					width : 110,
					dataIndex : 'planOriginalId',
					renderer : function(val) {
						return getIssuekindName(val);
					}
				},
				// 日期
				{
					header : "备注",
					sortable : true,
					width : 110,
					dataIndex : 'memo'
				}, {
					header : "出库情况",
					sortable : true,
					width : 60,
					dataIndex : 'enterpriseCode',
					renderer : function(value) {
						if (value == "1")
							return "未出库";
						else if (value == "2")
							return "已出库";
						else if (value == "3")
							return "部分出库";
						else
							return "";
					}

				}, {
					header : "费用来源",
					sortable : true,
					width : 70,
						 renderer : function(val) {
					 	if(val==""||val==null)// modify by wpzhu 100408
					 	{
					 	return"";
					 	}

					 	if (val == "zzfy") {
							return "生产类费用";
						} else if (val == "sclfy") {
							return "生产类费用";
						}else {
							return getBudgetName(val);
						}},
					dataIndex : 'itemCode'
				}],
		// viewConfig : {
		// forceFit : true
		// },

		enableColumnMove : false,
		tbar : [reportRadio, deptRadio, approveRadio, allRadio],
		// 分页
		bbar : new Ext.PagingToolbar({
			pageSize : Constants.PAGE_SIZE,
			store : queryStore,
			displayInfo : true,
			displayMsg : "显示 {0} 条到 {1} 条,共 {2} 条",
			emptyMsg : Constants.EMPTY_MSG
		})
	});

	// todo 双击改点击，查明细
	queryGrid.on('rowclick', function() {
		ShowIssueHeadRegister();
	})
	// showDetail);
	queryGrid.on('render', function() {
		// 初始化Grid
		queryStoreFresh();
	});

	// ------物资明细grid-----------------------
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
	// 物料详细记录
	var material = Ext.data.Record.create([{
		// 领料单明细id
		name : 'issueDetailsId'
	},
			// 上次修改日时
			{
				name : "lastModifiedDate"
			},
			// 物料id
			{
				name : "materialId"
			},
			// 物料编码，
			{
				name : 'materialNo'
			},
			// 物料名称
			{
				name : 'materialName'
			},
			// 规格型号
			{
				name : 'specNo'
			},
			// 材质/参数
			{
				name : 'parameter'
			},
			// 存货计量单位id
			{
				name : 'stockUmId'
			},
			// 存货计量单位名称
			{
				name : "unitName"
			},
			// 申请数量
			{
				name : 'appliedCount'
			},
			// 核准数量
			{
				name : 'approvedCount'
			},
			// 物资需求明细id
			{
				name : "requirementDetailId"
			},
			// 实际数量
			{
				name : 'actIssuedCount'
			},
			// 待发货数量
			{
				name : 'waitCount'
			},
			// 成本分摊项目编码
			{
				name : 'costItemId'
			},
			// 费用来源id
			{
				name : "itemCode"
			},
			// 费用来源名称
			{
				name : 'itemName'
			},
			// 是否是新规的记录
			{
				name : "isNew"
			},
			// 是否是删除的记录
			{
				name : "isDeleted"
			}, {
				name : 'modifyMemo'
			} // add by fyyang 100112
			, {
				name : 'unitPrice'
			}, {
				name : 'price'
			}]);
	// 物料grid的store
	var materialStore = new Ext.data.JsonStore({
		url : 'resource/getIssueHeadRegisterDetailList.action',
		root : 'list',
		totalProperty : 'totalCount',
		fields : material
	});
	// 物料grid
	var materialGrid = new Ext.grid.GridPanel({
	renderTo : "detailGrid",
		border : false,
		autoScroll : true,
		enableColumnMove : false,
		// 单选
		sm : new Ext.grid.RowSelectionModel({
			singleSelect : true
		}),
		// 单击修改
		store : materialStore,
		tbar : [ '颜色说明：',
		"<div style='width:40; color:white; background:green'>申请数量与核准数量不等</div>",
		"<div>&nbsp;&nbsp;</div>",
		'本年预算费用:', txtAdviceItem],
		columns : [new Ext.grid.RowNumberer({
			header : "行号",
			width : 35
		}),
				// 到货单号
				{
					header : "项次号",
					sortable : true,
					width : 45,
					dataIndex : 'issueDetailsId'
				},
				// 供应商
				{
					header : "物料编码<font color='red'>*</font>",
					sortable : true,
					width : 60,
					dataIndex : 'materialNo',
					// -------add by drdu 20100406 申请数量与审核数量不一致时，标识出来
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (rowIndex < store.getCount() - 1) {
							var appliedQty = record.get('appliedCount');
							var approvedQty = record.get('approvedCount');
							if (appliedQty != approvedQty && v != null) {
								return "<font color='green'>" + v + "</font>";
							}else return v;
						}
					}
				},
				// 物料名称
				{
					header : "物料名称",
					sortable : true,
					width : 55,
					dataIndex : 'materialName',
					// -------add by drdu 20100406 申请数量与审核数量不一致时，标识出来
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (rowIndex < store.getCount() - 1) {
							var appliedQty = record.get('appliedCount');
							var approvedQty = record.get('approvedCount');
							if (appliedQty != approvedQty && v != null) {
								return "<font color='green'>" + v + "</font>";
							}else return v;
						}
					}
				},
				// 规格型号
				{
					header : "规格型号",
					sortable : true,
					width : 55,
					dataIndex : 'specNo',
					// -------add by drdu 20100406 申请数量与审核数量不一致时，标识出来
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (rowIndex < store.getCount() - 1) {
							var appliedQty = record.get('appliedCount');
							var approvedQty = record.get('approvedCount');
							if (appliedQty != approvedQty && v != null) {
								return "<font color='green'>" + v + "</font>";
							}else return v;
						}
					}
				},
				// 材质/参数
				{
					header : "材质/参数",
					sortable : true,
					width : 55,
					dataIndex : 'parameter',
					// -------add by drdu 20100406 申请数量与审核数量不一致时，标识出来
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (rowIndex < store.getCount() - 1) {
							var appliedQty = record.get('appliedCount');
							var approvedQty = record.get('approvedCount');
							if (appliedQty != approvedQty && v != null) {
								return "<font color='green'>" + v + "</font>";
							}else return v;
						}
					}
				},
				// 单位
				{
					header : "单位",
					sortable : true,
					width : 40,
					dataIndex : 'unitName',
					// -------add by drdu 20100406 申请数量与审核数量不一致时，标识出来
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (rowIndex < store.getCount() - 1) {
							var appliedQty = record.get('appliedCount');
							var approvedQty = record.get('approvedCount');
							if (appliedQty != approvedQty && v != null) {
								return "<font color='green'>" + v + "</font>";
							}else return v;
						}
					}
				},
				// 申请数量
				{
					header : "申请数量<font color='red'>*</font>",
					sortable : true,
					width : 60,
					align : "right",
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (rowIndex < store.getCount() - 1) {
							// 强行触发renderer事件
							var totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('appliedCount');
							}
							if (store.getAt(store.getCount() - 1)
									.get('isNewRecord') == 'total') {
								store.getAt(store.getCount() - 1).set(
										'appliedCount', totalSum);
							}
							var appliedQty = record.get('appliedCount');
							var approvedQty = record.get('approvedCount');
							if (appliedQty != approvedQty) {
								return "<font color='green'>" + moneyFormat(value) + "</font>";
							}
							return moneyFormat(value);
						} else {
							totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('appliedCount');
							}
							return "<font color='red'>" + moneyFormat(totalSum)
									+ "</font>";
						}
					},
					editor : new Ext.form.NumberField({
						maxValue : 999999999999999.99
					}),
					dataIndex : 'appliedCount'
				},
				// 核准数量
				{
					header : "核准数量",
					sortable : true,
					align : "right",
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (rowIndex < store.getCount() - 1) {
							// 强行触发renderer事件
							var totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('approvedCount');
							}
							if (store.getAt(store.getCount() - 1)
									.get('isNewRecord') == 'total') {
								store.getAt(store.getCount() - 1).set(
										'approvedCount', totalSum);
							}
							var appliedQty = record.get('appliedCount');
							var approvedQty = record.get('approvedCount');
							if (appliedQty != approvedQty) {
								return "<font color='green'>" + moneyFormat(value) + "</font>";
							}
							return moneyFormat(value);
						} else {
							totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('approvedCount');
							}
							return "<font color='red'>" + moneyFormat(totalSum)
									+ "</font>";
						}
					},
					width : 55,
					dataIndex : 'approvedCount'
				}, {
					header : "核准信息",
					sortable : true,
					align : "right",
					width : 55,
					dataIndex : 'modifyMemo',
					renderer : function(value, metadata, record) {
						if (value != null)
							return "<span title='" + value + "' >" + value
									+ "</span>";
					}
				},
				// 已发货数量
				{
					header : "已发货数量",
					sortable : true,
					align : "right",
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
							var appliedQty = record.get('appliedCount');
							var approvedQty = record.get('approvedCount');
							if (appliedQty != approvedQty) {
								return "<font color='green'>" + moneyFormat(value) + "</font>";
							}
							return moneyFormat(value);
						} else {
							totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i)
										.get('actIssuedCount');
							}
							return "<font color='red'>" + moneyFormat(totalSum)
									+ "</font>";
						}
					},
					width : 60,
					dataIndex : 'actIssuedCount'
				},
				// 待发货数量
				{
					header : "待发货数量",
					sortable : true,
					align : "right",
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (rowIndex < store.getCount() - 1) {
							// 强行触发renderer事件
							var totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('waitCount');
							}
							if (store.getAt(store.getCount() - 1)
									.get('isNewRecord') == 'total') {
								store.getAt(store.getCount() - 1).set(
										'waitCount', totalSum);
							}
							var appliedQty = record.get('appliedCount');
							var approvedQty = record.get('approvedCount');
							if (appliedQty != approvedQty) {
								return "<font color='green'>" + moneyFormat(value) + "</font>";
							}
							return moneyFormat(value);
						} else {
							totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('waitCount');
							}
							return "<font color='red'>" + moneyFormat(totalSum)
									+ "</font>";
						}
					},
					width : 60,
					dataIndex : 'waitCount'
				},

				{
					header : "单价",
					sortable : true,
					width : 50,
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (rowIndex < store.getCount() - 1) {		
							var appliedQty = record.get('appliedCount');
							var approvedQty = record.get('approvedCount');
							if (appliedQty != approvedQty) {
								return "<font color='green'>" + moneyFormat(value) + "</font>";
							}
						}
						return moneyFormat(value);
						
					},
					dataIndex : 'unitPrice'
				}, {
					header : "金额",
					sortable : true,
					width : 50,
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (rowIndex < store.getCount() - 1) {
							// 强行触发renderer事件
							var totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('price');
							}
							if (store.getAt(store.getCount() - 1)
									.get('isNewRecord') == 'total') {
								store.getAt(store.getCount() - 1).set('price',
										totalSum);
							}
							var appliedQty = record.get('appliedCount');
							var approvedQty = record.get('approvedCount');
							if (appliedQty != approvedQty) {
								return "<font color='green'>" + moneyFormat(value) + "</font>";
							}
							return moneyFormat(value);
						} else {
							totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('price');
							}
							return "<font color='red'>" + moneyFormat(totalSum)
									+ "</font>";
						}
					},
					dataIndex : 'price'
				}]

	});
	// -----------------------------------------
	var btnDelete = new Ext.Button({
		id : "btnDelete",
		text : "删除",
		hidden : true,
		iconCls : 'delete',
		handler : function() {
			var record = queryGrid.getSelectionModel().getSelected();
			if (record) {
				if (confirm("删除后数据不能恢复,确定要删除该条领料单吗?")) {
					var issueId = record.get("issueHeadId");
					var entryId = record.get("workFlowNo");
					Ext.Ajax.request({
						url : "resource/deleteIssueHead.action",
						method : 'post',
						params : {
							entryId : entryId,
							issueId : issueId
						},
						success : function(result, request) {
							Ext.Msg.alert("提示", "操作成功!");
							queryGrid.getStore().remove(record);
							btnDelete.setVisible(false);
						},
						failure : function() {
							Ext.Msg.alert("提示", "操作失败!");
						}
					});
				}
			} else {
				Ext.Msg.alert("提示", "请选择一条删除的记录！");
			}

		}
	});

	var right = new Ext.Panel({
		region : "center",
		autoScroll : true,
		containerScroll : true,
		layout : 'fit',
		items : [materialGrid]

	});

	// add by fyyang 090731-------------------
	var btnView = new Ext.Button({
		text : '查看流程',
		iconCls : 'view',
		id : 'btnView',
		name : 'btnView',
		disabled : false,
		handler : function() {
			if (queryGrid.selModel.hasSelection()) {
				var record = queryGrid.getSelectionModel().getSelected();
				var entryId = record.get("workFlowNo");
				var url = application_base_path + "workflow/manager/show/show.jsp?entryId="
						+ entryId;

				window.open(url);
			} else {
				Ext.Msg.alert("提示", "请选择要查看的记录！");
			}
		}
	});
	// ---------------------------------------

	// 查询详细信息按钮
	// var btnReturn = new Ext.Toolbar.Button({
	// text : "查询详细",
	// iconCls : Constants.CLS_QUERY,
	// handler : function() {
	// window.location.replace("../sendMaterialsAccount/sendMaterialsAccount.jsp");
	// }
	// });

	var toolBarTop = new Ext.Toolbar({
		renderTo : queryGrid.tbar,
		items : ['从', fromDate, '到', toDate, '-', '申请人:', applyBy, '-',
				'申请部门：', issueDept, '-', '申请单号:', txtIssueNo]
	});
	var tbar2 = new Ext.Toolbar({
		// height:20,
		renderTo : queryGrid.tbar,
		items : ['物料名称：', materialName, '-', '审批状态：', stateComboBox, '-',
				'出库情况:', cbIssueStatus, '-', {
					text : '查询',
					iconCls : 'query',
					handler : function() {
						queryStoreFresh();
					}
				}, '-', btnView, '->', btnDelete,
				'<div id="divManagerDel">领料单列表</div>']
	});
	
	 //----add by fyyang 20100419------------   
	   var txtCheckPrice=new Ext.form.TextField({
	  name : 'txtCheckPrice',
		fieldLabel : "本年财务审核领料费用:",
		readOnly : true,
	    width:80
	  });
	     var txtCheckPriceRate=new Ext.form.TextField({
	  name : 'txtCheckPriceRate',
		fieldLabel : "本年财务审核领用费用完成率",
		readOnly : true,
	   width:60
	  });
	       var txtFactItemRate=new Ext.form.TextField({
	  name : 'txtFactItemRate',
		fieldLabel : "本年仓库发出费用完成率",
		readOnly : true,
		   width:60
	  });
	//---------------------------------------------
		var detailTbar= new Ext.Toolbar({
		renderTo : materialGrid.tbar,
		items :['本年财务审核领料费用:',txtCheckPrice,'-','本年财务审核领用费用完成率(%):',txtCheckPriceRate,'-','本年已领费用:',txtFactItem,'-','本年仓库发出费用完成率(%):',txtFactItemRate]
	});

	new Ext.Viewport({
		// enableTabScroll : true,
		layout : "border",
		items : [{
			region : 'north',
			layout : 'fit',
			border : false,
			margins : '0 0 0 0',
			collapsible : true,
			split : true,
			height : 400,
			autoScroll : true,
			items : [queryGrid]
		}, right]
	});
	// 新增统计行 add by ywliu 090721
	function addLine() {
		// 统计行
		var record = new material({
			stockUmId : "",
			unitName : "",
			appliedCount : "",
			approvedCount : "",
			actIssuedCount : "",
			waitCount : "",

			isNewRecord : "total"
		});
		// 原数据个数
		var count = materialStore.getCount();
		// 停止原来编辑
		materialGrid.stopEditing();
		// 插入统计行
		materialStore.insert(count, record);
		materialGrid.getView().refresh();
		totalCount = materialStore.getCount() - 1;

	};

	/**
	 * 申请人选择画面处理
	 */
	function selectApplyPersonWin() {
		// if (applyBy.disabled) {
		// return;
		// }
		if (queryType != '1') {
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
				applyBy.setValue(person.workerName);
				hdnApplyBy.setValue(person.workerCode);
			}
		}
	}

	function ShowIssueHeadRegister() {
		if (queryGrid.selModel.hasSelection()) {
			var record = queryGrid.getSelectionModel().getSelected();
			var start = record.get('receiptDep').indexOf('/');
			var deptCode = record.get('receiptDep').substring(start + 1,
					record.get('receiptDep').length);

			txtAdviceItem.setValue(getBudgetName(record.get('itemCode'), 1,
					deptCode));
//			txtFactItem.setValue(getBudgetName(record.get('itemCode'), 2,
//					deptCode));
			//--------add by fyyang 20100419---------------
			var data=getBudgetName(record.get('itemCode'),3,deptCode);
			var mydata=data.split(",");
			txtFactItem.setValue(mydata[0]);
			txtCheckPrice.setValue(mydata[1]);
			var data1=( parseFloat(mydata[1])/parseFloat(txtAdviceItem.getValue()))*100;
			var data2=(parseFloat(mydata[0])/parseFloat(txtAdviceItem.getValue()))*100;
			txtCheckPriceRate.setValue(parseFloat(txtAdviceItem.getValue())==0? 0:  numberFormat(data1)) ;
            txtFactItemRate.setValue(parseFloat(txtAdviceItem.getValue())==0? 0: numberFormat(data2));
			//----------------------------------------------------
			materialStore.load({
				params : {
					// headId : record.get('issueHeadId')
					issueHeadId : record.get('issueHeadId'),
					flag : "1",
					requimentHeadId : ""
				},
				callback : addLine
			});
			entryId = record.get('workFlowNo');
			orginalId = record.get('planOriginalId');

		}
	}

	document.getElementById("divManagerDel").onclick = function() {
		if (currentUser == "999999" && event.ctrlKey && event.altKey) {
			btnDelete.setVisible(true);
		}
	};

	function initData() {
		applyBy.setValue(currentUserName);
		hdnApplyBy.setValue(currentUser);
		issueDept.setValue(currentDeptName);
		issueDept.setDisabled(true);
		Ext.get("report").dom.checked = true;
		queryType = "1";
		Ext.get("all").dom.parentNode.style.display = "none";
		Ext.Ajax.request({
			url : 'resource/getQueryRightByWorkId.action',
			method : 'post',
			params : {
				fileAddr : 'run/resource/storage/issuecompquery/issueCompQuery.jsp'
			},
			success : function(action) {
				var state = action.responseText;
				if (state.indexOf('1') != -1) {
					Ext.get("all").dom.parentNode.style.display = "";
				}

				queryStoreFresh();

			}
		});
	}
	initData();
})