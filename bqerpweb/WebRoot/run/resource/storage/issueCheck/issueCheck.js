Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Constants.RS004_E_001 = "当前{0}可供出库数量不足。";
Constants.RS004_E_002 = "本次发货数量不能大于待发货数量";
Constants.RS004_I_002 = "没有物料出库。";

Ext.onReady(function() {
	Ext.override(Ext.grid.GridView, {
		ensureVisible : function(row, col, hscroll) {
			if (typeof row != "number") {
				row = row.rowIndex;
			}
			if (!this.ds) {
				return;
			}
			if (row < 0 || row >= this.ds.getCount()) {
				return;
			}
			col = (col !== undefined ? col : 0);
			var rowEl = this.getRow(row), cellEl;
			if (!(hscroll === false && col === 0)) {
				while (this.cm.isHidden(col)) {
					col++;
				}
				cellEl = this.getCell(row, col);
			}
			if (!rowEl) {
				return;
			}
			var c = this.scroller.dom;
			var ctop = 0;
			var p = rowEl, stop = this.el.dom;
			while (p && p != stop) {
				ctop += p.offsetTop;
				p = p.offsetParent;
			}
			ctop -= this.mainHd.dom.offsetHeight;
			var cbot = ctop + rowEl.offsetHeight;
			var ch = c.clientHeight;
			var stop = parseInt(c.scrollTop, 10);
			var sbot = stop + ch;
			if (ctop < stop) {
				c.scrollTop = ctop;
			} else if (cbot > sbot) {
				c.scrollTop = cbot - ch;
			}
			if (hscroll !== false) {
				var cleft = parseInt(cellEl.offsetLeft, 10);
				var cright = cleft + cellEl.offsetWidth;
				var sleft = parseInt(c.scrollLeft, 10);
				var sright = sleft + c.clientWidth;
				if (cleft < sleft) {
					c.scrollLeft = cleft;
				} else if (cright > sright) {
					c.scrollLeft = cright - c.clientWidth;
				}
			}
			return cellEl ? Ext.fly(cellEl).getXY() : [this.el.getX(),
					Ext.fly(rowEl).getY()];
		},
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
					// 判断是否是最后行
					if (r.data["isNewRecord"] == "total") {
						// 替换掉其中的背景颜色
						p.style = c.style
								.replace(/background\s*:\s*[^;]*;/, '');
					} else {
						// 引用原样式
						p.style = c.style;
					};
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
				// 出库时间
				{
					name : "lastModifiedDate"
				}, {
					name : "workFlowNo"
				}, {
					name : "projectCode"
				}, {
					name : "refIssueNo"
				}, {
					name : "actIssuedCount"
				}]
	});
	// 载入数据
	// issueHeaderStore.load({
	// params : {
	// start : 0,
	// limit : Constants.PAGE_SIZE
	// }
	// });

	var mysm = new Ext.grid.CheckboxSelectionModel();
	// 领料单表头panel
	var issueheadPanel = new Ext.grid.GridPanel({
		border : true,
		sm : mysm,
		enableColumnMove : false,
		store : issueHeaderStore,
		columns : [mysm,
				// 行号
				new Ext.grid.RowNumberer({
					header : "行号",
					width : 35
				}),
				// 编号
				{
					header : "编号",
					sortable : true,
					width : 80,
					dataIndex : 'issueNo'
				},
				// 日期
				{
					header : "申请领用日期",
					sortable : true,
					width : 80,
					renderer : dateRenderer,
					dataIndex : 'dueDate'
				}, {

					header : "实际领用日期",
					sortable : true,
					width : 80,
					//renderer : dateRenderer,
					dataIndex : 'lastModifiedDate'
				}, {

					header : "workFlowNo",
					sortable : true,
					hidden : true,
					dataIndex : 'workFlowNo'
				}],
		viewConfig : {
			forceFit : false,
			getRowClass : function(record, rowIndex, rowParams, store) {
				// 禁用数据显示红色
				if (record.data.actIssuedCount < 0 && record.data.refIssueNo != "" && record.data.refIssueNo != null) {
					return 'x-grid-record-red';
				}else if (record.data.actIssuedCount > 0 && record.data.refIssueNo != "" && record.data.refIssueNo != null) {
					return 'x-grid-record-blue';
				}
				else {
					return '';
				} 
			}
		},
		// 分页
		bbar : new Ext.PagingToolbar({
			pageSize : Constants.PAGE_SIZE,
			store : issueHeaderStore,
			displayInfo : true,
			displayMsg : '共 {2} 条',
			emptyMsg : Constants.EMPTY_MSG
		})
	});
	
	

	// 双击处理
	issueheadPanel.on('rowdblclick', issueHeadDblClick);
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
	var hdnMrNo = new Ext.form.TextField({
		id : "mrNo",
		fieldLabel : '需求计划单编号',
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
		anchor : "90%",// "100%",
		name : ''
	});
	// 计划来源
	var sourceType = new Ext.form.TextField({
		id : "planOriginalDesc",
		fieldLabel : '单据类型',
		readOnly : true,
		anchor : "90%",// "100%",
		name : ''
	})
	// TODO 名称取得方式? 费用来源
	var costSource = new Ext.form.TextField({
		// id : "itemCode",
		fieldLabel : '费用来源',
		readOnly : true,
		anchor : "90%",// "100%",
		name : ''
	});
	// 日期
	var operateDate = new Ext.form.TextField({
		id : "dueDate",
		fieldLabel : '申请领用日期',
		readOnly : true,
		anchor : "90%",// "100%",
		name : ''
	});

	// 操作员
	var operator = new Ext.form.TextField({
		id : "receiptBy",
		fieldLabel : '申请领料人',
		readOnly : true,
		anchor : "90%",// "100%",
		name : ''
	});

	// 备注
	var detailMemo = new Ext.form.TextArea({
		id : "memo",
		maxLength : 128,
		height : Constants.TEXTAREA_HEIGHT,
		fieldLabel : '备注',
		anchor : "94.5%",// "100%",
		name : '',
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
			},{//事务ID add by drdu 20100408
				name : 'transHisId'
			},{//税额 add by drdu 20100408
				name : 'taxCount'
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
			issueNo : issueCode.getValue(),
			issueDate:hdnLastModifiedDate.getValue()
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
				{
					header : "transHisId",
					width : 100,
					sortable : true,
					hidden:true,
					dataIndex : 'transHisId'
				},
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
					width : 75,
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
					header : "本次实发数量",
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
				},detailSm,{//----------add by drdu 20100408-----------------
					header : '税额',
					width : 75,
					sorttable : true,
					dataIndex : 'taxCount',
					css : CSS_GRID_INPUT_COL,
					editor : new Ext.form.NumberField({
						maxValue : 99999999999.9999,
						minValue : 0,
						decimalPrecision : 4
					}),
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (rowIndex < store.getCount() - 1) {
							var taxCount = record.data.taxCount;
							// 强行触发renderer事件
							var totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('taxCount');
							}
							if (store.getAt(store.getCount() - 1).get('isNewRecord') == 'total') {
								store.getAt(store.getCount() - 1).set('taxCount', totalSum);
							}
							return moneyFormat(taxCount);
						} else {
							totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('taxCount');
							}
							return "<font color='red'>" + moneyFormat(totalSum)
									+ "</font>";
						}
					}
				}]

	});
	rightGrid.on('beforeedit', checkIsEdit);

	// 备注
	var memoText = new Ext.form.TextArea({
		id : "memoText",
		maxLength : 128,
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
		title : '详细信息录入窗口',
		buttons : [{
			text : Constants.BTN_SAVE,
			iconCls : Constants.CLS_SAVE,
			handler : function() {
				if (!memoText.isValid()) {
					return;
				}
				var record = rightGrid.selModel.getSelected();
				record.set("memo", Ext.get("memoText").dom.value);
				win.hide();
			}
		}, {
			text : Constants.BTN_CANCEL,
			iconCls : Constants.CLS_CANCEL,
			handler : function() {
				win.hide();
			}
		}]
	});
	// ↑↑*******************物资详细grid***************************
	//-----------add by fyyang 20100107--------------------------
	
	var txtWoNo = new Ext.form.TextField({
		id : "woNo",
		fieldLabel : '工单编号',
		readOnly : true,
		anchor : "90%",// "100%",
		name : ''
	});
		var txtProjectCode = new Ext.form.TextField({
		id : "projectCode",
		fieldLabel : '项目编号',
		readOnly : true,
		anchor : "90%",// "100%",
		name : ''
	});
		var txtReceiptDep = new Ext.form.TextField({
		id : "receiptDep",
		fieldLabel : '领用部门',
		readOnly : true,
		anchor : "90%",// "100%",
		name : ''
	});
	
	//-----------------------------------------------------------
	var wd = 25;
	var headForm = new Ext.form.FieldSet({
		border : false,
		labelAlign : 'right',
		style : {
			'border' : 0
		},
		layout : 'form',
		labelWidth : 100,
		width : 550,

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
						items : [hdnIssueHeadId, hdnLastModifiedDate,
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
				columnWidth : 0.48,
				layout : "form",
				border : false,
				height : wd,
				labelAlign : "right",
				items : [issueCode]
			},{
				columnWidth : 0.48,
				layout : "form",
				border : false,
				height : wd,
				labelAlign : "right",
				items : [hdnMrNo]
			}]
		}, {
			layout : "column",
			border : false,
			items : [
					// 计划来源
					{
						columnWidth : 0.48,
						layout : "form",
						border : false,
						height : wd,
						labelAlign : "right",
						items : [sourceType]
					},
					// 费用来源
					{
						columnWidth : 0.48,
						layout : "form",
						border : false,
						height : wd,
						labelAlign : "right",
						items : [costSource]
					}]
		},  {
			layout : "column",
			border : false,
			items : [
					// 
					{
						columnWidth : 0.48,
						layout : "form",
						border : false,
						height : wd,
						labelAlign : "right",
						items : [txtWoNo]
					},
					// 
					{
						columnWidth : 0.48,
						layout : "form",
						border : false,
						height : wd,
						labelAlign : "right",
						items : [txtProjectCode]
					}]
		},{
			layout : "column",
			border : false,
			items : [
					// 日期
					{
						columnWidth : 0.48,
						layout : "form",
						border : false,
						height : wd,
						labelAlign : "right",
						items : [operator]
					},
					// 操作员
					{
						columnWidth : 0.48,
						layout : "form",
						border : false,
						height : wd,
						labelAlign : "right",
						items : [txtReceiptDep]
					}]
		}, {
			layout : "column",
			border : false,
			items : [
			// 备注
			{
				columnWidth : 0.48,
				layout : "form",
				border : false,
				labelAlign : "right",
				items : [operateDate]
			}]
		}, {
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

	var checkBtn = new Ext.Button({
		text : '审核',
		id : 'checkBtn',
		iconCls : Constants.CLS_OK,
		name : 'checkBtn',
		// disabled : true,
		handler : function() {
			btnCheck();
		}
	});

	var cancelBtn = new Ext.Button({
		text : '取消审核',
		id : 'cancelBtn',
		iconCls : Constants.CLS_CANCEL,
		name : 'cancelBtn',
		// disabled : true,
		handler : function() {
			btnCheckCancel();
		}
	});

	var btnView = new Ext.Button({
		text : '查看流程',
		id : 'btnView',
		iconCls : 'view',
		name : 'btnView',
		// disabled : true,
		handler : function() {
			showFlow();
		}
	});

	var checkStatus = new Ext.form.ComboBox({
		store : new Ext.data.SimpleStore({
			fields : ['value', 'text'],
			data : [['2', '未审核'], ['C', '已审核']]
		}),
		id : 'checkStatus',
		name : 'checkStatus',
		valueField : "value",
		displayField : "text",
		value : '2',
		mode : 'local',
		typeAhead : true,
		forceSelection : true,
		// hiddenName : 'staInfo.checkStatus',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : false,
		width : 100
	});
	
	var issueNoFuzzy = new Ext.form.TextField({
		id : "issueNoFuzzy",
		fieldLabel : '领料单编号',
		readOnly : false,
		width : 90
	});

	var headPanel = new Ext.Panel({
		region : 'north',
		bodyStyle : 'display:none',
		tbar : ['领料单编号:', issueNoFuzzy, '-', '审核状态：', checkStatus, '-', {
			text : '查询',
			iconCls : 'query',
			handler : queryRecord
		}, "-", checkBtn, "-", cancelBtn, "-", btnView, '-', {
			text : '查看需求计划物资信息',
			hidden:true,
			handler : function() {
				var sm = issueheadPanel.getSelectionModel();
				var selected = sm.getSelections();
				var ids = [];
				if (selected.length == 0) {
					Ext.Msg.alert("提示", "请选择要查看的记录！");
				} else if (selected.length>1){
					Ext.Msg.alert("提示", "请选择一条记录！");
				}
				else
				{
					
					var url = "planMaterialQuery.jsp";
				var args = new Object();
				args.orderNo = selected[0].data.issueNo;
				args.flag="2";
				var location = window
						.showModalDialog(
								url,
								args,
								'dialogWidth=800px;dialogHeight=500px;center=yes;help=no;resizable=no;status=no;');

				}

			}
		}

		],
		height : 20
	});

	var detailPanel = new Ext.FormPanel({
		style : "padding-top:1.5px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
		labelAlign : 'right',
		items : [headForm]
	});
	var right = new Ext.Panel({
		region : "center",
		autoScroll : true,
		containerScroll : true,
		layout : 'border',
		items : [{
			region : 'north',
			height : 250,
			autoScroll : true,
			items : [detailPanel]
		}, {
			region : 'center',
			layout : 'fit',
			autoScroll : true,
			items : [rightGrid]
		}]

	});

	new Ext.Viewport({
		layout : "border",
		items : [headPanel, {
			region : 'west',
			layout : 'fit',
			border : false,
			margins : '0 0 0 0',
			collapsible : true,
			split : true,
			width : 400,
			autoScroll : true,
			items : [{
				border : false,
				autoScroll : true,
				layout : 'border',
				items : [{
					region : 'center',
					layout : 'fit',
					items : [issueheadPanel]
				}]
			}]
		}, right]
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
	/**
	 * 表头确认处理
	 */
	function btnCheck() {
		rightGrid.stopEditing();
		var sm = issueheadPanel.getSelectionModel();
		var selected = sm.getSelections();
		var ids = [];
		var updateData = new Array();
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要审核的记录！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i].data;
				if (member.issueNo) {
					ids.push(member.issueNo); // modify by ywliu 20100202
					ids.push(member.lastModifiedDate); // modify by ywliu 20100202
				} else {
					issueHeaderStore.remove(issueHeaderStore.getAt(i));
				}
			}
			// add by drdu 20100408 修改税额-------------
			for (var i = 0; i < rightGrid.getStore().getCount() - 1; i++) {
				var rec = rightGrid.getStore().getAt(i);
				if (rightStore.getAt(rightStore.getCount() - 1) != 'isNewRecord') {
					updateData.push({
						id : rec.get("transHisId"),
						tc : rec.get("taxCount")
					});
				}
			}
			//--------------------------------------------
			Ext.Msg.confirm("审核", "是否确定审核？", function(buttonobj) {
				if (buttonobj == "yes") {
					Ext.lib.Ajax.request('POST',
							'resource/issueCheckOp.action', {
								success : function(action) {
									Ext.Msg.alert("提示", "审核成功！");
									initPanelDatas();
								},
								failure : function() {
									Ext.Msg.alert('错误', '未知错误.');
								}
							}, 'ids=' + ids + "&updateData="
										+ Ext.util.JSON.encode(updateData));//add by drdu 20100409
				}

			});
		}
	}

	// add by drdu 091103
	function btnCheckCancel() {
		var sm = issueheadPanel.getSelectionModel();
		var selected = sm.getSelections();
		var ids = [];
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要取消审核的记录！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i].data;
				if (member.issueNo) {
					ids.push(member.issueNo); // modify by ywliu 20100202
					ids.push(member.lastModifiedDate); // modify by ywliu 20100202
				} else {
					issueHeaderStore.remove(issueHeaderStore.getAt(i));
				}
			}
			Ext.Msg.confirm("取消审核", "是否确定取消审核？", function(buttonobj) {
				if (buttonobj == "yes") {
					Ext.lib.Ajax.request('POST',
							'resource/issueCheckCancel.action', {
								success : function(action) {
									Ext.Msg.alert("提示", "取消审核成功！");
									initPanelDatas();
								},
								failure : function() {
									Ext.Msg.alert('错误', '删除时出现未知错误.');
								}
							}, 'ids=' + ids);
				}

			});
		}
	}

	// add by drdu 091103
	function showFlow() {
		if (issueheadPanel.selModel.hasSelection()) {
			var record = issueheadPanel.getSelectionModel().getSelected();
			var entryId = record.get("workFlowNo");
			if (entryId == null) {
				Ext.Msg.alert("提示", "流程未启动！");
			} else {
				var url = application_base_path + "workflow/manager/show/show.jsp?entryId="
						+ entryId;

				window.open(url);
			}
		} else {
			Ext.Msg.alert("提示", "请先选择要查看的记录！");
		}
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
		// 清空分页栏显示信息
		// 05/09/09 15:49 分页栏已隐藏 yiliu
		// rightGrid.getBottomToolbar().updateInfo();
		// 左边grid重新load
		// issueHeaderStore.load({
		// params : {
		// start : 0,
		// limit : Constants.PAGE_SIZE
		// }
		// });
		queryRecord();
	}

	/**
	 * 载入明细数据
	 */
	function loadDetailDatas() {
		Ext.Msg.wait("正在查询数据，请等待...");
		// 载入表单数据
		editHeadRecord = issueheadPanel.selModel.getSelected();
		detailPanel.getForm().trackResetOnLoad = true, detailPanel.getForm()
				.loadRecord(editHeadRecord);
		birtEntryDate = editHeadRecord.data.dueDate;
		sourceType.setValue(getBillNameById(editHeadRecord
				.get("planOriginalDesc")));
				
//		alert(editHeadRecord
//				.get("mrNo"));		
				
	
				
		rightStore.load({
			params : {
				start : 0,
				limit : Constants.PAGE_SIZE
			},
			callback : addLine
		});
		// 事务作用码
		bussinessCode.setValue('I');
		// 事务作用名称
		bussinessName.setValue('出库');
		// 操作日期
		//operateDate.setValue(getSysDate());
		operateDate.setValue(editHeadRecord.get("dueDate").substring(0,10));
		// TODO 临时费用来源
		var itemCode = editHeadRecord.get('itemCode');
//		if (itemCode == "zzfy") {
//			costSource.setValue("生产成本");
//		} else if (itemCode == "lwcb") {
//			costSource.setValue("劳务成本");
//		}
		costSource.setValue(getBudgetName(itemCode));
//		costSource.setValue(itemCode);
		// if(itemid == "1"){
		// costSource.setValue('费用来源1');
		// }else if(itemid == "2"){
		// costSource.setValue('费用来源2');
		// }else if(itemid == "3"){
		// costSource.setValue('费用来源3');
		// }else if(itemid == "4"){
		// costSource.setValue('费用来源4');
		// }else {
		// costSource.setValue('');
		// }
		// 获取当前用户
//		Ext.lib.Ajax.request('POST', 'resource/getLogonWorkerCode.action', {
//			success : function(action) {
//				operator.setValue(action.responseText);
//				operator.originalValue = operator.getValue();
//			},
//			failure : function() {
//			}
//		});
	}

	rightStore.on("load", function() {
		Ext.Msg.hide();
	});

//	/**
//	 * 获取当前日时
//	 */
//	function getSysDate() {
//		var d, s, t;
//		d = new Date();
//		s = d.getFullYear().toString(10) + "-";
//		t = d.getMonth() + 1;
//		s += (t > 9 ? "" : "0") + t + "-";
//		t = d.getDate();
//		s += (t > 9 ? "" : "0") + t;
//		return s;
//	}
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
	
	/**
	 * 判断是否是最后行，如果是则不能编辑
	 */
	function checkIsEdit(obj) {

		if (obj.row == rightStore.getCount() - 1) {

			obj.cancel = true;
		}
	}

	queryRecord();

});
