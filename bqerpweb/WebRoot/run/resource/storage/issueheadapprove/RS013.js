Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Constants.RS012_E_001 = "当前领料单已经上报了，不能操作。请确认。";
Ext.QuickTips.init();
Ext.onReady(function() {
	var issueApprove = new IssueApprove();
	var entryId;
	var orginalId;
	Ext.override(Ext.grid.GridView, {
		doRender : function(cs, rs, ds, startRow, colCount, stripe) {
			var ts = this.templates, ct = ts.cell, rt = ts.row, last = colCount
					- 1;
			var tstyle = 'width:' + this.getTotalWidth() + ';';
			// buffers
			var buf = [], cb, c, p = {}, rp = {
				tstyle : tstyle
			}, r;
			for (var j = 0, len = rs.length; j < len; j++) {
				r = rs[j];
				cb = [];
				var rowIndex = (j + startRow);
				for (var i = 0; i < colCount; i++) {
					c = cs[i];
					p.id = c.id;
					p.css = i == 0 ? 'x-grid3-cell-first ' : (i == last
							? 'x-grid3-cell-last '
							: '');
					p.attr = p.cellAttr = "";
					p.value = c.renderer(r.data[c.name], p, r, rowIndex, i, ds);
					p.style = c.style;
					if (p.value == undefined || p.value === "")
						p.value = "&#160;";
					if (r.dirty && typeof r.modified[c.name] !== 'undefined') {
						p.css += ' x-grid3-dirty-cell';
					}
					cb[cb.length] = ct.apply(p);
				}
				var alt = [];
				if (stripe && ((rowIndex + 1) % 2 == 0)) {
					alt[0] = "x-grid3-row-alt";
				}
				if (r.dirty) {
					alt[1] = " x-grid3-dirty-row";
				}
				rp.cols = colCount;
				if (this.getRowClass) {
					alt[2] = this.getRowClass(r, rowIndex, rp, ds);
				}
				rp.alt = alt.join(" ");
				rp.cells = cb.join("");
				buf[buf.length] = rt.apply(rp);
			}
			return buf.join("");
		}
	});
	// record
	var queryRecord = new Ext.data.Record.create([
			// 领料单ID
			{
		name : 'issueHeadId',
		mapping : 0
	},		// 单据状态
	{
		name : 'issueStatus',
		mapping : 1
	},
			// 领料单编号，
			{
				name : 'issueNo',
				mapping : 2
			},
			// 项目编号
			{
				name : 'projectCode',
				mapping : 3
			},
			// 工单编号
			{
				name : 'woNo',
				mapping : 4
			},
			// 成本分摊项目编码
			{
				name : 'costItemId',
				mapping : 5
			},
			// 费用来源ID
			{
				name : 'itemCode',
				mapping : 6
			},
			// 需求计划申请单编号
			{
				name : 'mrNo',
				mapping : 7
			},// 申请领料人
			{
				name : 'receiptBy',
				mapping : 8
			},// 领用部门
			{
				name : 'receiptDep',
				mapping : 9
			},// 申请领用日期
			{
				name : 'dueDate',
				mapping : 10
			},// 费用归口部门
			{
				name : 'feeByDep',
				mapping : 11
			},// 费用归口专业
			{
				name : 'feeBySpecial',
				mapping : 12
			},// 备注
			{
				name : 'memo',
				mapping : 13
			},
			// 是否紧急领用
			{
				name : 'isEmergency',
				mapping : 14
			},// 上次修改日期
			{
				name : 'lastModifiedDate',
				mapping : 15
			},// 工作流实例号
			{
				name : 'workFlowNo',
				mapping : 16
			},// 计划来源id
			{
				name : 'planOriginalId',
				mapping : 17
			}, {
				name : 'receiptDeptName',
				mapping : 18
			}]);
	// 领料单grid的store
	var queryStore = new Ext.data.JsonStore({
				url : 'resource/getIssueHeadApproveList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : queryRecord
			});
	queryStore.load({
				params : {
					start : 0,
					limit : Constants.PAGE_SIZE
				}
			});
	// 确认按钮是否可用
	queryStore.on("load", function() {
				if (queryStore.getCount() == 0) {
					confirmBtn.setDisabled(true);
				} else {
					confirmBtn.setDisabled(false);
				}
				var date;
				for (var i = 0; i < queryStore.getCount(); i++) {
					if (!queryStore.getAt(i).get('feeBySpecial')) {
						queryStore.getAt(i).set('feeBySpecial', "");
					}
				}

			});
	var confirmBtn = new Ext.Button({
				text : '审批',
				id : 'confirmBtn',
				name : 'confirmBtn',
				disabled : false,
				handler : function() {
					issueSign();
					// 5/7/09 调用双击grid相同的方法 yiliu
					// ShowIssueHeadRegister();
				}
			});
	// grid工具栏
	var txtIssueNo = new Ext.form.TextField({
				name : 'txtIssueNo',
				fieldLabel : '领料单号'
			});
	// 定义审批状态
	var stateComboBox = new Ext.form.ComboBox({
				id : "stateCob",
				store : issueApprove.approveQueryStatus,
				displayField : "name",
				valueField : "value",
				mode : 'local',
				triggerAction : 'all',
				hiddenName : 'stateComboBox',
				readOnly : true,
				value : '',
				width : 135
			});

	var btnView = new Ext.Button({
				text : '查看流程',
				iconCls : 'view',
				id : 'btnView',
				name : 'btnView',
				disabled : false,
				handler : function() {
					if (queryGrid.selModel.hasSelection()) {
						var record = queryGrid.getSelectionModel()
								.getSelected();
						var entryId = record.get("workFlowNo");
						var url = application_base_path + "workflow/manager/show/show.jsp?entryId="
								+ entryId;

						window.open(url);
					} else {
						Ext.Msg.alert("提示", "请选择要查看的记录！");
					}
				}
			});

	var headTbar = new Ext.Toolbar({
				region : "north",
				height : 25,
				items : ['领料单号：', txtIssueNo, '-', '审批状态：', stateComboBox, {
							text : "查询",
							iconCls : Constants.CLS_QUERY,
							handler : function() {
								queryStore.baseParams = {
									issueNo : txtIssueNo.getValue(),
									status : stateComboBox.getValue()
								};
								queryStore.load({
											params : {
												start : 0,
												limit : Constants.PAGE_SIZE
											}
										});
							}
						}, "-", confirmBtn, "-", btnView]
			})
	// 查询grid
	var queryGrid = new Ext.grid.GridPanel({
				region : "center",
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
							width : 80,
							dataIndex : 'issueNo'
						},
						// 单据状态
						{
							header : "单据状态",
							sortable : true,
							width : 100,
							renderer : IssueStatusValue,
							dataIndex : 'issueStatus'
						},
						// 需求计划单编号
						{
							header : "需求计划单编号",
							sortable : true,
							width : 100,
							dataIndex : 'mrNo'
						},
						// 申请领料人
						{
							header : "申请领料人",
							sortable : true,
							width : 100,
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
						},
						// 日期
						{
							header : "申请日期",
							sortable : true,
							width : 100,
							renderer : renderDate,
							dataIndex : 'dueDate'
						},
						// 日期
						{
							header : "备注",
							sortable : true,
							width : 200,
							dataIndex : 'memo'
						}, {
							header : "领用部门",
							sortable : true,
							width : 200,
							dataIndex : 'receiptDeptName'
						}, {
							header : "费用来源",
							sortable : true,
							width : 200,
							renderer : function(val) {
								return getBudgetName(val);
							},
							dataIndex : 'itemCode'
						}],
				viewConfig : {
					forceFit : true
				},
				enableColumnMove : false,
				// 分页
				bbar : new Ext.PagingToolbar({
							pageSize : Constants.PAGE_SIZE,
							store : queryStore,
							displayInfo : true,
							displayMsg : Constants.DISPLAY_MSG,
							emptyMsg : Constants.EMPTY_MSG
						})
			});

	// ↓↓*******************查看备注************************
	// 备注
	var memoText = new Ext.form.TextArea({
				id : "memoText",
				readOnly : true,
				width : 180
			});

	// 弹出画面
	var win = new Ext.Window({
				height : 170,
				width : 350,
				layout : 'fit',
				modal : true,
				resizable : false,
				closeAction : 'hide',
				items : [memoText],
				buttonAlign : "center",
				title : '详细信息查看窗口',
				buttons : [{
							text : '返回',
							handler : function() {
								win.hide();
							}
						}]
			});
	// ↑↑*******************查看备注************************
	queryGrid.on({
				// 'rowdblclick' : ShowIssueHeadRegister,
				'rowdblclick' : issueSign,
				'cellclick' : showMemoWin,
				'rowclick' : showDetail
			});

	var detailRecord = Ext.data.Record.create([
			// 领料单明细id
			{
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
				name : "itemId"
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
			},
			// 库存数量
			{
				name : "stock" // add by ywliu 20091019
			}, {
				name : 'modifyMemo'
			} // add by fyyang 100112
	]);
	// 明细grid的store
	var detailStore = new Ext.data.JsonStore({
				url : 'resource/getIssueHeadRegisterDetailList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : detailRecord
			});
	// 个数变更
	detailStore.addEvents('countChanged');

	// ---add by fyyang 090723---加合计行------
	detailStore.on("load", function() {

				var totalappliedCount = 0; // 申请数量
				var totalapprovedCount = 0; // 核准数量
				var totalactIssuedCount = 0; // 已发货数量
				var totalwaitCount = 0;// 待发货数量

				for (var j = 0; j < detailStore.getCount(); j++) {
					var temp = detailStore.getAt(j);
					if (temp.get("appliedCount") != null) {
						totalappliedCount = parseFloat(totalappliedCount)
								+ parseFloat(temp.get("appliedCount"));
					}
					if (temp.get("approvedCount") != null) {
						totalapprovedCount = parseFloat(totalapprovedCount)
								+ parseFloat(temp.get("approvedCount"));
					}
					if (temp.get("actIssuedCount") != null) {
						totalactIssuedCount = parseFloat(totalactIssuedCount)
								+ parseFloat(temp.get("actIssuedCount"));
					}
					if (temp.get("waitCount") != null) {
						totalwaitCount = parseFloat(totalwaitCount)
								+ parseFloat(temp.get("waitCount"));
					}
				}
				var mydata = new detailRecord({
							appliedCount : totalappliedCount,
							approvedCount : totalapprovedCount,
							actIssuedCount : totalactIssuedCount,
							waitCount : totalwaitCount
						});
				if (detailStore.getCount() > 0) {
					detailStore.add(mydata);
				}

			});

	// ----add by fyyang 20100315 -------------
	var txtAdviceItem = new Ext.form.TextField({
				name : 'txtAdviceItem',
				fieldLabel : "本年预算费用",
				readOnly : true,
				anchor : '100%'
			});

	var txtFactItem = new Ext.form.TextField({
				name : 'txtFactItem',
				fieldLabel : "本年已领费用",
				readOnly : true,
				   width:80
			});
	// -----------------------------------------

	var detailGrid = new Ext.grid.EditorGridPanel({
		height : 220,
		//region : "center",
		renderTo : "mygrid",
		style : "padding-top:0px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
		border : false,
		clicksToEdit : 1,
		autoScroll : true,
		sm : new Ext.grid.RowSelectionModel({
					singleSelect : true
				}),
		store : detailStore,
		// add------------------
		listeners : {
			'beforeedit' : function(e) {
				if (e.field == "approvedCount") {
					var column = e.record.get('issueDetailsId');
					if (column == null || column == "undefined") {
						return false;
					}
				}
			},
			'afteredit' : function(e) {
				if (e.field == "approvedCount") {
					var column = e.record.get('issueDetailsId');
					if (column != null && column != "undefined") {
						var totalapprovedCount = 0;
						for (var j = 0; j < detailStore.getCount() - 1; j++) {
							var temp = detailStore.getAt(j);
							if (temp.get("approvedCount") != null) {
								totalapprovedCount = parseFloat(totalapprovedCount)
										+ parseFloat(temp.get("approvedCount"));
							}
						}
						detailStore.getAt(detailStore.getCount() - 1).set(
								'approvedCount', totalapprovedCount);
					}
				}
			}

		},
		// addend--------------------
		columns : [new Ext.grid.RowNumberer({
							header : "行号",
							width : 35
						}),
				// 到货单号
				{
					header : "项次号",
					sortable : true,
					width : 75,
					dataIndex : 'issueDetailsId',
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						// -------add by ltong 20100412 申请数量与审核数量不一致时，标识出来
						if (rowIndex <= store.getCount() - 1) {
							var appliedQty = record.get('appliedCount');
							var approvedQty = record.get('approvedCount');
							if (appliedQty != approvedQty && value != null) {
								return "<font color='green'>" + value
										+ "</font>";
							} else {
								return value;
							}
						}
					}
				},
				// 供应商
				{
					header : "物料编码",
					sortable : true,
					width : 75,
					dataIndex : 'materialNo',
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						// -------add by ltong 20100412 申请数量与审核数量不一致时，标识出来
						if (rowIndex <= store.getCount() - 1) {
							var appliedQty = record.get('appliedCount');
							var approvedQty = record.get('approvedCount');
							if (appliedQty != approvedQty && value != null) {
								return "<font color='green'>" + value
										+ "</font>";
							} else {
								return value;
							}
						}
					}
				},
				// 物料名称
				{
					header : "物料名称",
					sortable : true,
					width : 75,
					dataIndex : 'materialName',
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						// -------add by ltong 20100412 申请数量与审核数量不一致时，标识出来
						if (rowIndex <= store.getCount() - 1) {
							var appliedQty = record.get('appliedCount');
							var approvedQty = record.get('approvedCount');
							if (appliedQty != approvedQty && value != null) {
								return "<font color='green'>" + value
										+ "</font>";
							} else {
								return value;
							}
						}
					}
				},
				// 规格型号
				{
					header : "规格型号",
					sortable : true,
					width : 75,
					dataIndex : 'specNo',
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						// -------add by ltong 20100412 申请数量与审核数量不一致时，标识出来
						if (rowIndex <= store.getCount() - 1) {
							var appliedQty = record.get('appliedCount');
							var approvedQty = record.get('approvedCount');
							if (appliedQty != approvedQty && value != null) {
								return "<font color='green'>" + value
										+ "</font>";
							} else {
								return value;
							}
						}
					}
				},
				// 材质/参数
				{
					header : "材质/参数",
					sortable : true,
					width : 75,
					dataIndex : 'parameter',
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						// -------add by ltong 20100412 申请数量与审核数量不一致时，标识出来
						if (rowIndex <= store.getCount() - 1) {
							var appliedQty = record.get('appliedCount');
							var approvedQty = record.get('approvedCount');
							if (appliedQty != approvedQty && value != null) {
								return "<font color='green'>" + value
										+ "</font>";
							} else {
								return value;
							}
						}
					}
				},
				// 单位
				{
					header : "单位",
					sortable : true,
					width : 75,
					dataIndex : 'unitName',
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						// -------add by ltong 20100412 申请数量与审核数量不一致时，标识出来
						if (rowIndex <= store.getCount() - 1) {
							var appliedQty = record.get('appliedCount');
							var approvedQty = record.get('approvedCount');
							if (appliedQty != approvedQty && value != null) {
								return "<font color='green'>" + value
										+ "</font>";
							} else {
								return value;
							}
						}
					}
				},
				// 申请数量
				{
					header : "申请数量",
					sortable : true,
					width : 75,
					align : "right",
					renderer : numRenderer,
					dataIndex : 'appliedCount',
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						// -------add by ltong 20100412 申请数量与审核数量不一致时，标识出来
						if (rowIndex <= store.getCount() - 1) {
							var appliedQty = record.get('appliedCount');
							var approvedQty = record.get('approvedCount');
							if (appliedQty != approvedQty && value != null) {
								return "<font color='green'>" + value
										+ "</font>";
							} else {
								return value;
							}
						}
					}
				},
				// 核准数量
				{
					header : "核准数量<font color='red'>*</font>",
					sortable : true,
					align : "right",
					renderer : numRenderer,
					width : 75,
					css : CSS_GRID_INPUT_COL,
					editor : new Ext.form.NumberField({
								maxValue : 999999999999999.99,
								minValue : 0,
								decimalPrecision : 4
							}),
					dataIndex : 'approvedCount',
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						// -------add by ltong 20100412 申请数量与审核数量不一致时，标识出来
						if (rowIndex <= store.getCount() - 1) {
							var appliedQty = record.get('appliedCount');
							var approvedQty = record.get('approvedCount');
							if (appliedQty != approvedQty && value != null) {
								return "<font color='green'>" + value
										+ "</font>";
							} else {
								return value;
							}
						}
					}
				}, {
					header : "核准信息",
					sortable : true,
					align : "right",
					width : 75,
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
					renderer : numRenderer,
					width : 75,
					dataIndex : 'actIssuedCount',
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						// -------add by ltong 20100412 申请数量与审核数量不一致时，标识出来
						if (rowIndex <= store.getCount() - 1) {
							var appliedQty = record.get('appliedCount');
							var approvedQty = record.get('approvedCount');
							if (appliedQty != approvedQty && value != null) {
								return "<font color='green'>" + value
										+ "</font>";
							} else {
								return value;
							}
						}
					}
				},
				// 待发货数量
				{
					header : "待发货数量",
					sortable : true,
					align : "right",
					renderer : numRenderer,
					width : 75,
					dataIndex : 'waitCount',
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						// -------add by ltong 20100412 申请数量与审核数量不一致时，标识出来
						if (rowIndex <= store.getCount() - 1) {
							var appliedQty = record.get('appliedCount');
							var approvedQty = record.get('approvedCount');
							if (appliedQty != approvedQty && value != null) {
								return "<font color='green'>" + value
										+ "</font>";
							} else {
								return value;
							}
						}
					}
				},
				// 成本分摊项目
				{
					header : "成本分摊项目",
					sortable : true,
					width : 100,
					hidden : true,
					renderer : function(val) {
						if (val == "1") {
							return "成本分摊项目1";
						} else if (val == "2") {
							return "成本分摊项目2";
						} else if (val == "3") {
							return "成本分摊项目3";
						} else if (val == "4") {
							return "成本分摊项目4";
						} else {
							return "";
						}
					},
					dataIndex : 'costItemId'
				},
				// TODO 费用来源
				{
					header : "费用来源",
					sortable : true,
					width : 75,
					// -------add by ltong 20100412 申请数量与审核数量不一致时，标识出来
					renderer : function(val, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (rowIndex <= store.getCount() - 1) {
							var appliedQty = record.get('appliedCount');
							var approvedQty = record.get('approvedCount');
							if (appliedQty != approvedQty && val != null) {
								return "<font color='green'>"
										+ getBudgetName(val) + "</font>";
							} else {
								return getBudgetName(val);
							}
						}
					},
					dataIndex : 'itemId'
				},
				// TODO 当前库存
				{
					header : "当前库存",
					sortable : true,
					width : 75,
					renderer : function(v) {
						if (v != null)
							return numRenderer(v);
					},
					dataIndex : 'stock',// add by ywliu 20091019
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						// -------add by ltong 20100412 申请数量与审核数量不一致时，标识出来
						if (rowIndex <= store.getCount() - 1) {
							var appliedQty = record.get('appliedCount');
							var approvedQty = record.get('approvedCount');
							if (appliedQty != approvedQty && value != null) {
								return "<font color='green'>" + value
										+ "</font>";
							} else {
								return value;
							}
						}
					}
				}],

		enableColumnMove : false,
		// 分页
		bbar : new Ext.PagingToolbar({
					pageSize : Constants.PAGE_SIZE,
					store : detailStore,
					displayInfo : true,
					displayMsg : Constants.DISPLAY_MSG,
					emptyMsg : Constants.EMPTY_MSG
				}),
		tbar : new Ext.Toolbar({
			region : "north",
			height : 25,
			items : [
					'请核实数量',
					"<div>&nbsp;&nbsp;&nbsp;&nbsp;</div>",
					'颜色说明：',
					"<div>&nbsp;&nbsp;</div>",
					"<div style='width:40; color:white; background:green'>申请数量与审核数量不等</div>",
					'-',
					{
						text : "保存",
						iconCls : 'save',
						handler : function() {
							Ext.Ajax.request({
										url : 'resource/saveDetailIssue.action',
										method : 'post',
										params : {
											updateDetails : Ext.util.JSON
													.encode(getDetailRecords())
										},
										success : function(result, request) {
											detailStore.reload();
											Ext.Msg.alert('提示信息', '操作成功！');
										},
										failure : function(restlt, request) {
										}
									})

						}
					}, "-", {
						text : "取消",
						iconCls : 'cancer',
						handler : function() {
							detailStore.reload();
						}
					}, '-', '本年预算费用:', txtAdviceItem]
		})
	});
	
	
	
	//--------------add by fyyang renderTo
	//----add by fyyang 20100419------------   
	   var txtCheckPrice=new Ext.form.TextField({
	  name : 'txtCheckPrice',
		fieldLabel : "本年财务审核领料费用:",
		readOnly : true,
	    width:80
	  });
	     var txtCheckPriceRate=new Ext.form.TextField({
	  name : 'txtCheckPriceRate',
		fieldLabel : "本年财务审核领用费用完效率",
		readOnly : true,
	   width:60
	  });
	       var txtFactItemRate=new Ext.form.TextField({
	  name : 'txtFactItemRate',
		fieldLabel : "本年仓库发出费用完效率",
		readOnly : true,
		   width:60
	  });
	  	var detailTbar= new Ext.Toolbar({
		renderTo : detailGrid.tbar,
		items :['本年财务审核领料费用:',txtCheckPrice,'-','本年财务审核领用费用完效率(%):',txtCheckPriceRate,'-','本年已领费用:',txtFactItem,'-','本年仓库发出费用完效率(%):',txtFactItemRate]
	});
	//---------------------------------------------
	
	

	// 供应商查询 Panel
	var tabQuery = new Ext.Panel({
				region : "north",
				layout : 'border',
				height : 400,
				border : false,
				items : [headTbar, queryGrid]
			})
			
			var tabDetail = new Ext.Panel({
				region : "center",
				layout : 'fit',
				height : 400,
				border : false,
				items : [detailGrid]
			})
	
//	var tabRegister = new Ext.Panel({
//				region : "center",
//				layout : 'border',
//				border : false,
//				items : [tabQuery, detailGrid]
//			})

	var layout = new Ext.Viewport({
				layout : 'border',
				margins : '0 0 0 0',
				border : false,
				items : [tabQuery,tabDetail]
			});

	/**
	 * 根据人员code查找对应名字
	 */
	function findEmpName(empCode) {
		// 画面初始值设置
		Ext.lib.Ajax.request('POST', 'resource/getReceiptByName.action', {
					success : function(action) {
						var name = action.responseText;
						receiptBy.setValue(name);
						formPanel.fireEvent('doInit');
						hdnReceiptBy.fireEvent('valueChanged', true);
					}
				}, 'receiptBy=' + empCode);
	}
	function IssueStatusValue(value) {
		// if (value == "0") {
		// return "待审批状态";
		// }
		// if (value == "1") {
		// return "审批中状态";
		// }
		// if (value == "2") {
		// return "审批结束状态";
		// }
		// if (value == "9") {
		// return "退回状态";
		// }
		return issueApprove.getStatusName(value);
	}

	/**
	 * 显示备注窗口
	 */
	function showMemoWin(grid, row, col, e) {
		// 记录
		var record = grid.getStore().getAt(row);
		// 字段
		var fieldName = grid.getColumnModel().getDataIndex(col);
		if (fieldName == 'memo') {
			var data = record.get(fieldName);
			memoText.setValue(data);
			win.show();
		}
	}
	function renderDate(value) {
		if (!value)
			return "";
		var reDate = /\d{4}\-\d{2}\-\d{2}/gi;
		var strDate = value.match(reDate);
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

	/**
	 * 当单击主grid中一行时，下面的grid显示详细信息
	 */
	function showDetail() {
		if (queryGrid.getSelectionModel().getSelected() != null) {
			var record = queryGrid.getSelectionModel().getSelected();

			txtAdviceItem.setValue(getBudgetName(record.get("itemCode"), 1,
					record.get("receiptDep")));
//			txtFactItem.setValue(getBudgetName(record.get("itemCode"), 2,
//					record.get("receiptDep")));
					
						//--------add by fyyang 20100419---------------
			var data=getBudgetName(record.get('itemCode'),3,record.get("receiptDep"));
			var mydata=data.split(",");
			txtFactItem.setValue(mydata[0]);
			txtCheckPrice.setValue(mydata[1]);
			var data1=( parseFloat(mydata[1])/parseFloat(txtAdviceItem.getValue()))*100;
			var data2=(parseFloat(mydata[0])/parseFloat(txtAdviceItem.getValue()))*100;
			txtCheckPriceRate.setValue(parseFloat(txtAdviceItem.getValue())==0? 0:  data1.toFixed(2)) ;
            txtFactItemRate.setValue(parseFloat(txtAdviceItem.getValue())==0? 0: data2.toFixed(2));
			//----------------------------------------------------
			detailStore.on({
						'beforeload' : function() {
							Ext.apply(this.baseParams, {
										issueHeadId : record.get('issueHeadId'),
										flag : "1",
										requimentHeadId : ""
									});
						}
					});
			detailStore.load({
						params : {
							start : 0,
							limit : Constants.PAGE_SIZE
						}
					});
		}

	}

	/**
	 * 获取明细grid中的数据,传回后台
	 * 
	 * @param isNew
	 *            新记录
	 */
	function getDetailRecords() {
		var results = [];
		for (var i = 0; i < detailStore.getCount() - 1; i++) {
			var record = detailStore.getAt(i);
			results.push(record.data);
		}
		return results;

	}
	/**
	 * 画面初期化
	 */
	function initAllDatas() {
		// 列表tab重新载入
		queryStore.load({
					params : {
						start : 0,
						limit : Constants.PAGE_SIZE
					}
				});
		detailStore.removeAll();
	}

	function issueSign() {
		if (queryGrid.selModel.hasSelection()) {
			var _record = queryGrid.getSelectionModel().getSelected();
			var _issueHeadId = _record.get("issueHeadId");
			var _entryId = _record.get("workFlowNo");
			var _orginalId = _record.get("planOriginalId");
			var itemCode = _record.get("itemCode");
			var itemName = getBudgetName(_record.get("itemCode"));
		
			var mrNo=_record.get("mrNo");//add by sychen 20100511
			var receiptDep=_record.get("receiptDep");//add by sychen 20100511
			var flag="2";//add by sychen 20100511

			var _arr = [];
			var url = "RS015.jsp?issueHeadId=" + _issueHeadId + "&entryId="
					+ _entryId + "&orginalId=" + _orginalId + "&itemCode="
					+ itemCode + "&itemName=" + itemName
					+ "&mrNo=" + mrNo+ "&receiptDep=" + receiptDep+ "&flag=" + flag;//add by sychen 20100511
			var o = window.showModalDialog(url, '',
					'dialogWidth=800px;dialogHeight=500px;status=no');
			if (typeof(o) != "undefined") {
				var params = "issueHeadId=" + _issueHeadId + "&actionId="
						+ o.actionId + "&approveText=" + o.opinion
						+ "&workerCode=" + o.workerCode + "&nrs=" + o.nrs
						+ "&eventIdentify=" + o.eventIdentify + "&itemCode="
						+ o.itemCode
						+ "&mrNo=" + mrNo+ "&receiptDep=" + receiptDep+ "&flag=" + flag;//add by sychen 20100511
				Ext.lib.Ajax.request('POST',
						'resource/approveIssueHeadRecord.action', {
							success : function(action) {
								var result = eval("(" + action.responseText
										+ ")");
								if (!result.success) {
									Ext.Msg.alert("提示", "审批失败！");
								} else {
									Ext.Msg.alert("提示", "审批成功！");
									initAllDatas();
								}
							},
							faliure : function(action) {
								Ext.Msg.alert("提示", "审批失败!");
							}
						}, params);
			}

		} else {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_001);
		}
	}
});
