Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Constants.RS004_E_001 = "当前{0}可供出库数量不足。";
Constants.RS004_E_002 = "本次发货数量不能大于待发货数量";
Constants.RS004_I_002 = "没有物料出库。";

Ext.onReady(function() {
	// add by ywliu 090722 对统计行进行处理
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

	// ↓↓*******************领料单报表所需数据**************************************
	var birtEntryDate;
	var birtIssueNo;
	var birtWhsNo = new Array();

	Ext.QuickTips.init();
	// ↓↓*******************领料单表头***************************
	// 领料单号text
	var fuzzy = new Ext.form.TextField({
		id : "fuzzy",
		name : "fuzzy",
		width:60
	});
	var txtApplyBy=new Ext.form.TextField({
	id : "applyName",
		name : "applyName",
		width:60
	});
	var txtmaterail_name=new Ext.form.TextField({
	id : "materail_name",
		name : "materail_name",
		width:60
	});
	// 领料单表头grid的store
	var issueHeaderStore = new Ext.data.JsonStore({
		url : 'resource/getIssueHeadDatas.action',
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
				},// 申请领料人
				{
					name : 'receiptByName'
				},
				// 领用部门
				{
					name : 'receiptDepName'
				}]
	});
	// 载入数据
	issueHeaderStore.load({
		params : {
			start : 0,
			limit : 18
		}
	});
	//add by drdu 091210
	issueHeaderStore.on('beforeload', function() {
		Ext.apply(this.baseParams, {
			issueNo : fuzzy.getValue(),
			applyName : txtApplyBy.getValue(),
			materail_name : txtmaterail_name.getValue()
		});
	});
	// issueHeaderStore.on('load',function(store,records){
	// if(store.getCount() == 0){
	// Ext.getCmp('btnConfirm').setDisabled(true);
	// }else{
	// Ext.getCmp('btnConfirm').setDisabled(false);
	// };
	// }); 隐藏确认按钮，modify by ywliu
	// 领料单表头panel
	var issueheadPanel = new Ext.grid.GridPanel({
		border : true,
		// 单选
		sm : new Ext.grid.RowSelectionModel({
			singleSelect : true
		}),
		enableColumnMove : false,
		store : issueHeaderStore,
		columns : [
				// 行号
				new Ext.grid.RowNumberer({
					header : "行号",
					width : 35
				}),
				// 编号
				{
					header : "编号",
					sortable : true,
					width : 40,
					dataIndex : 'issueNo'
				},
				// 编号
				{
					header : "申请人",
					sortable : true,
					width : 40,
					dataIndex : 'receiptByName'
				},
				// 编号
				{
					header : "申请部门",
					sortable : true,
					width : 70,
					dataIndex : 'receiptDepName'
				},
				// 日期
				{
					header : "日期",
					sortable : true,
					width : 50,
					renderer : dateRenderer,
					dataIndex : 'dueDate'
				}],
		viewConfig : {
			forceFit : true
		},
		tbar : ['领料单号:', fuzzy, '-', '申请人：',txtApplyBy,'物资名称：',txtmaterail_name,{
			text : Constants.BTN_QUERY,
			iconCls : Constants.CLS_QUERY,
			handler : function() {
				// 载入数据
				issueHeaderStore.load({
					params : {
						// 参数 领料单编号 modify by drdu 091210
//						issueNo : fuzzy.getValue().trim(),
//						applyName: txtApplyBy.getValue().trim(),
//						materail_name:txtmaterail_name.getValue().trim(),
						start : 0,
						limit : 18
					}
				});
			}
		}],// , '-',{
		// text : '确认',
		// id : 'btnConfirm',
		// iconCls : Constants.CLS_OK,
		// handler : btnOkHandler
		// } 隐藏页面的“确认”按钮 modify by ywliu 090717
		// 分页
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
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
	var hdnMrNo = new Ext.form.Hidden({
		id : "mrNo",
		value : ""
	});
	// 事务作用码
	var bussinessCode = new Ext.form.TextField({
		id : "bussinessCode",
		fieldLabel : '事务作用码',
		readOnly : true,
		anchor : "90%",
		name : ''
	});

	// 事务作用名称
	var bussinessName = new Ext.form.TextField({
		id : "bussinessName",
		fieldLabel : '事务作用名称',
		readOnly : true,
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

	var getRealPerson = new Ext.form.TriggerField({
		fieldLabel : '领料人',
		// width : 108,
		id : "getRealPerson",
		displayField : 'text',
		valueField : 'id',
		hiddenName : 'getRealPerson',
		blankText : '请选择',
		emptyText : '请选择',
		allowBlank : false,
		maxLength : 100,
		anchor : '90%',
		readOnly : true
	});
	getRealPerson.onTriggerClick = selectPersonWin;
	var getRealPersonH = new Ext.form.Hidden({
		hiddenName : 'getRealPersonH'
	})

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
		id : "operateDate",
		fieldLabel : '日期',
		readOnly : true,
		anchor : "90%",// "100%",
		name : ''
	});

	// 操作员
	var operator = new Ext.form.TextField({
		id : "operator",
		fieldLabel : '操作员',
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
		name : ''
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
			{  name:'specNo'}
			]);
	// 采购详细列表的store
	var rightStore = new Ext.data.JsonStore({
		url : 'resource/getMaterialDetailDatas.action',
		root : 'list',
		totalProperty : 'totalCount',
		fields : rightData
	});
	rightStore.on('beforeload', function() {
		Ext.apply(this.baseParams, {
			issueHeadId : hdnIssueHeadId.getValue()
		})
	});
	// 批号的store
	var lotNoStore = new Ext.data.JsonStore({
		url : "resource/getLotsByMaterialid.action",
		fields : [{
			name : 'lotNo'
		}]
	});
	// 仓库的store
	var whsStore = new Ext.data.JsonStore({
		root : 'list',
		url : "resource/getWhsByLots.action",
		fields : [{
			name : 'whsNo'
		}, {
			name : 'whsName'
		}]
	});
	// 库位的store
	var locationStore = new Ext.data.JsonStore({
		root : 'list',
		url : "resource/getLocationByWhsAndLots.action",
		fields : [{
			name : 'locationNo'
		}, {
			name : 'locationName'
		}]
	});
	// 物资详细列表grid
	var rightGrid = new Ext.grid.EditorGridPanel({
		region : "center",
		autoScroll : true,
		style : "border-top:solid 1px",
		border : false,
		enableColumnMove : false,
		clicksToEdit : 1,
		store : rightStore,
		sm : new Ext.grid.RowSelectionModel({
			singleSelect : true
		}),
		columns : [
				// 行号
				new Ext.grid.RowNumberer({
					header : "行号",
					width : 35
				}),
				// 物料编码
				{
					header : "物料编码",
					width : 75,
					sortable : true,
					dataIndex : 'materialNo'
				},
				// 物料名称
				{
					header : "物料名称",
					width : 75,
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
				// 规格型号
				{
					header : "规格型号",
					width : 75,
					sortable : true,
					dataIndex : 'specNo'
				},
				// 需求数量
				{
					header : "需求数量",
					width : 75,
					align : 'right',
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						// 新增合计申请数量 add by ywliu 0090722
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
							return numRenderer(value);
						} else {
							totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('demandNum');
							}
							return "<font color='red'>" + numRenderer(totalSum)
									+ "</font>";
						}
					},
					sortable : true,
					dataIndex : 'demandNum'
				},
				// 实际数量
				{
					header : "实际数量",
					width : 75,
					align : 'right',
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						// 新增合计申请数量 add by ywliu 0090722
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
							return numRenderer(value);
						} else {
							totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i)
										.get('actIssuedCount');
							}
							return "<font color='red'>" + numRenderer(totalSum)
									+ "</font>";
						}
					},
					sortable : true,
					dataIndex : 'actIssuedCount'
				},
				// 本次发货数量
				{
					header : "本次发货数量<font color='red'>*</font>",
					width : 100,
					sortable : true,
					align : 'right',
					dataIndex : 'thisNum',
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						// 新增合计申请数量 add by ywliu 0090722
						if (rowIndex < store.getCount() - 1) {
							// 强行触发renderer事件
							var totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('thisNum');
							}
							if (store.getAt(store.getCount() - 1)
									.get('isNewRecord') == 'total') {
								store.getAt(store.getCount() - 1).set(
										'thisNum', totalSum);
							}
							return numRenderer(value);
						} else {
							totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('thisNum');
							}
							return "<font color='red'>" + numRenderer(totalSum)
									+ "</font>";
						}
					},
					css : CSS_GRID_INPUT_COL,
					editor : new Ext.form.NumberField({
						maxValue : 99999999999.9999,
						minValue : 0,
						decimalPrecision : 4
					})
				},
				// 待发货数量
				{
					header : "待发货数量",
					width : 75,
					align : 'right',
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						// 新增合计申请数量 add by ywliu 0090722
						if (rowIndex < store.getCount() - 1) {
							// 强行触发renderer事件
							var totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('waitNum');
							}
							if (store.getAt(store.getCount() - 1)
									.get('isNewRecord') == 'total') {
								store.getAt(store.getCount() - 1).set(
										'waitNum', totalSum);
							}
							return numRenderer(value);
						} else {
							totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('waitNum');
							}
							return "<font color='red'>" + numRenderer(totalSum)
									+ "</font>";
						}
					},
					sortable : true,
					dataIndex : 'waitNum'
				},
				// 批号
				{
					header : "批号",
					width : 75,
					dataIndex : 'lotNo',
					renderer : function(value) {
						if (!value || value == 0) {
							return "";
						}
						return value;
					},
					css : CSS_GRID_INPUT_COL,
					editor : new Ext.form.ComboBox({
						store : lotNoStore,
						displayField : "lotNo",
						valueField : "lotNo",
						emptyText : "",
						mode : 'local',
						triggerAction : 'all',
						readOnly : true
					}),
					sortable : true
				},
				// 仓库
				{
					header : "仓库",
					width : 100,
					dataIndex : 'whsNo',
					sortable : true,
					css : CSS_GRID_INPUT_COL,
					editor : new Ext.form.ComboBox({
						store : whsStore,
						displayField : "whsName",
						emptyText : "",
						valueField : "whsNo",
						mode : 'local',
						triggerAction : 'all',
						readOnly : true
					}),
					renderer : comboBoxWarehouseRenderer
				},
				// 库位
				{
					header : "库位",
					width : 100,
					dataIndex : 'locationNo',
					sortable : true,
					css : CSS_GRID_INPUT_COL,
					editor : new Ext.form.ComboBox({
						store : locationStore,
						displayField : "locationName",
						valueField : "locationNo",
						mode : 'local',
						triggerAction : 'all',
						readOnly : true
					}),
					renderer : comboBoxLocationRenderer
				},
				// 保管员
				{
					header : "保管员",
					width : 75,
					sortable : true,
					dataIndex : 'save'
				},
				// TODO 费用来源
				{
					header : "费用来源",
					width : 75,
					sortable : true,
					hidden : true,
					// renderer : function(val) {
					// if (val == "1") {
					// return "费用来源1";
					// } else if (val == "2") {
					// return "费用来源2";
					// } else if (val == "3") {
					// return "费用来源3";
					// } else if (val == "4") {
					// return "费用来源4";
					// } else {
					// return "";
					// }
					// },
					dataIndex : 'itemCode'
				},
				// 备注
				{
					header : "备注",
					width : 75,
					css : CSS_GRID_INPUT_COL,
					editor : new Ext.form.TextArea({
						maxLength : 128,
						listeners : {
							"render" : function() {
								this.el.on("dblclick", function() {
									var record = rightGrid.getSelectionModel()
											.getSelected();
									var value = record.get('memo');
									memoText.setValue(value);
									win.x = undefined;
									win.y = undefined;
									win.show();
								})
							}
						}
					}),
					sortable : true,
					dataIndex : 'memo'
				}],
		tbar : ['物料信息', {
			text : '查看库存明细',
			iconCls : Constants.CLS_QUERY,
			handler : queryStorageDetail
		}]
			// ,
			// bbar : new Ext.PagingToolbar({
			// pageSize : Constants.PAGE_SIZE,
			// store : rightStore,
			// displayInfo : true,
			// displayMsg : Constants.DISPLAY_MSG,
			// emptyMsg : Constants.EMPTY_MSG
			// })
	});

	// -----------库存明细查询--------------

	// rightGrid.on("dblclick", queryStorageDetail);
	function queryStorageDetail() {

		var record = rightGrid.getSelectionModel().getSelected();
		if (record == null) {
			Ext.Msg.alert("提示", "请选择一条记录！");
		} else {
			var arg = new Object();
			// alert(record.get("materialNo"));
			arg.materialNo = record.get("materialNo");
			window.showModalDialog('storageDetailQuery.jsp', arg,
					'status:no;dialogWidth=750px;dialogHeight=450px');
		}
	}
	// -----------------------------------

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
	var wd = 25;
	var headForm = new Ext.form.FieldSet({
		border : false,
		labelAlign : 'right',
		style : {
			'border' : 0
		},
		layout : 'form',
		labelWidth : 80,
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
						columnWidth : 0.48,
						layout : "form",
						border : false,
						height : wd,
						labelAlign : "right",
						items : [issueCode]
					}, {
						columnWidth : 0.48,
						layout : "form",
						border : false,
						height : wd,
						labelAlign : "right",
						items : [getRealPerson]
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
		}, {
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
						items : [operateDate]
					},
					// 操作员
					{
						columnWidth : 0.48,
						layout : "form",
						border : false,
						height : wd,
						labelAlign : "right",
						items : [operator]
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
	// 明细panel
	var detailPanel = new Ext.FormPanel({
		border : false,
		region : 'north',
		height : 160,
		style : "padding-top:1.5px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
		labelAlign : 'right',
		items : [headForm]
	});

	// 左边的panel
	var leftPanel = new Ext.Panel({
		region : 'west',
		layout : 'fit',
		width : 400,
		border : false,
		containerScroll : true,
		collapsible : true,
		items : [issueheadPanel]
	});
	// 右边的panel
	var rightPanel = new Ext.Panel({
		region : "center",
		layout : 'border',
		border : true,
		tbar : [{
			text : '确认出库',
			iconCls : Constants.CLS_SEND,
			handler : issueHandler
		}, {
			text : '出库回滚',
			iconCls : Constants.CLS_SEND,
			handler : cancelIssue
		}, {	// add by drdu 091121
			text : '补打印',
			iconCls : 'print',
			handler : function() {
			window.location.replace("../issue/afterPrintIssue.jsp");
		}
		}],
		items : [detailPanel, rightGrid]
	});

	// 显示区域
	var view = new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		items : [leftPanel, rightPanel]
	})
	// ↓↓********************处理***********************
	// 初始化批号数据
	rightStore.on('load', function(store, records) {
		var record;
		// 循环判断
		for (var i = 0; i < records.length; i++) {
			record = records[i];
			record.set('thisNum', 0);
			// 如果批号为零
			if (isLotNoBlank(record.get('materialId'))) {
				// 没有批号
				record.set('noLotNo', 'Y');
				// 批号设为0
				record.set('lotNo', '0');
			};
		}
	});

	// 增加统计行 add by ywliu 090722
	function addLine() {
		// 统计行
		var record = new rightData({
			materialId : "",
			materialName : "",
			equCode : "",
			materSize : "",
			parameter : "",
			stockUmName : "",
			demandNum : "",
			actIssuedCount : "",
			thisNum : "",
			waitNum : "",
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

	// 编辑前的操作
	rightGrid.on("beforeedit", function(obj) {
		var record = obj.record;
		var field = obj.field;
		// 物料id
		var lngMaterialId = record.get('materialId');
		// add by ywliu 090722 当是统计行时退出编辑不进行编辑
		if (obj.record.get('isNewRecord') == 'total') {
			return false;
		}
		// 编辑的是本次发货数量或是批号
		if (field == "thisNum" || field == "lotNo") {
			if (record.get('noLotNo') !== "Y") {
				// 载入当前行批号数据
				lotNoStore.load({
					params : {
						materialId : lngMaterialId
					}
				});
			} else {
				// 批号disable
				if (field == "lotNo")
					obj.cancel = true;
			}
		}
		if (field == "whsNo") {
			if (record.get('lotNo') == '' || record.get('lotNo') == null) {
				obj.cancel = true;
			}
			// 载入当前行仓库信息
			whsStore.load({
				params : {
					materialId : lngMaterialId,
					lotNo : record.get('lotNo')
				}
			});

		}
		if (field == "locationNo") {
			if (record.get('whsNo') == '' || record.get('whsNo') == null) {
				obj.cancel = true;
			}
			// 载入当前行库位信息
			locationStore.load({
				params : {
					materialId : lngMaterialId,
					lotNo : record.get('lotNo'),
					whsNo : record.get('whsNo')
				}
			});
		}
	});
	// 编辑后的操作
	rightGrid.on("afteredit", function(obj) {
		var record = obj.record;
		var field = obj.field;
		// 物料id
		var lngMaterialId = record.get('materialId');
		if (field == 'lotNo') {
			// 值改变
			if (obj.value != obj.originalValue) {
				record.set('whsNo', '');
				record.set('locationNo', '');
				record.set('save', '');
			}
		}
		if (field == 'whsNo') {
			// 值改变
			if (obj.value != obj.originalValue) {
				record.set('locationNo', '');
				// 保管员设置
				Ext.lib.Ajax.request('POST', 'resource/getIssueSaveMan.action',
						{
							success : function(action) {
								record.set('save', action.responseText);
							}
						}, "whsNo=" + record.get('whsNo'));
			}
		}
	});
	/**
	 * 选择领料人
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
						'../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp',
						args,
						'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (typeof(person) != "undefined") {
			getRealPerson.setValue(person.workerName);
			getRealPersonH.setValue(person.workerCode);
		}
	}

	/**
	 * 判断物料对应的批号是否为0
	 * 
	 * @param lngMaterialId
	 *            物料id
	 */
	function isLotNoBlank(lngMaterialId) {
		var url = "resource/getLotsByMaterialid.action?materialId="
				+ lngMaterialId;
		var conn = Ext.lib.Ajax.getConnectionObject().conn;
		conn.open("POST", url, false);
		conn.send(null);
		var lotNos = Ext.util.JSON.decode("(" + conn.responseText + ")");
		if (lotNos != null && lotNos.length > 0) {
			if (lotNos[0].lotNo === "0") {
				return true;
			}
		}
		return false;
	}
	/**
	 * 表头确认处理
	 */
	function btnOkHandler() {
		// 判断有无选择记录
		if (issueheadPanel.selModel.hasSelection()) {
			issueHeadDblClick();
		} else {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_001);
			return;
		}

	}
	/**
	 * 表头双击处理
	 */
	function issueHeadDblClick() {
		// 判断明细部分记录有无变化
		if (isDetailChanged()) {
			Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_C_004,
					function(buttonobj) {
						if (buttonobj == "yes") {
							loadDetailDatas();
						}
					});
		} else {
			loadDetailDatas();
		}
	}

	// // ↓↓****************员工验证窗口****************
	// // 工号
	// var workCode = new Ext.form.TextField({
	// id : "workCode",
	// fieldLabel : '工号<font color ="red">*</font>',
	// allowBlank : false,
	// width : 120
	// });
	//
	// // 密码
	// var workPwd = new Ext.form.TextField({
	// id : "workPwd",
	// fieldLabel : '密码<font color ="red">*</font>',
	// allowBlank : false,
	// inputType : "password",
	// width : 120
	// });
	// // 弹出窗口panel
	// var workerPanel = new Ext.FormPanel({
	// frame : true,
	// labelAlign : 'right',
	// height : 120,
	// items : [workCode, workPwd]
	// });
	//    
	// // 弹出窗口
	// var validateWin = new Ext.Window({
	// width : 300,
	// height : 140,
	// title : "请输入领料人工号和密码",
	// buttonAlign : "center",
	// resizable : false,
	// modal:true,
	// items : [workerPanel],
	// buttons : [{
	// text : '确定',
	// id:'btnSign',
	// handler : function() {
	// // 工号确认
	// Ext.lib.Ajax.request('POST', 'comm/workticketApproveCheckUser.action',
	// {success : function(action) {
	// var result = eval(action.responseText);
	// // 如果验证成功，进行操作
	// if (result) {
	// updateDbTable();
	// } else {
	// Ext.Msg.alert("提示","密码错误");
	// }
	// }
	// }, "workerCode=" + workCode.getValue() + "&loginPwd=" +
	// workPwd.getValue());
	// }
	// }, {
	// // 取消按钮
	// text :"取消",
	// iconCls :"cancer",
	// handler : function() {
	// validateWin.hide();
	// }
	// }],
	// closeAction : 'hide'
	// });

	/**
	 * 出库处理
	 */
	function issueHandler() {
		// bug对应 UT-BUG-RS004-029
		if (!hdnIssueHeadId.getValue()) {
			return
		}
		if (getRealPersonH.getValue() == ""
				|| getRealPersonH.getValue() == null) {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, "请选择领料人！");
			return;
		}
		// 明细部分没有变更不做处理
		if (!isDetailChanged()) {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_006);
			return;
		}
		// 数据无效时返回
		if (!detailMemo.isValid()) {
			return;
		}
		// 提示要不要出库
		Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_C_001,
				function(button) {
					// 出库的情况下
					if (button == 'yes') {
						// 本次发货数量为空的情况下不做处理
						// modify by fyyang 091105 本次发货数量可以为空
						// if (isThisNumBlank()){
						// Ext.Msg.alert(Constants.SYS_REMIND_MSG,
						// String.format(Constants.COM_E_002, "本次出库数量"));
						// return;
						// }
						if (!isThisNumValid()) {
							return;
						}
						// 保存表头
						if (isThisNumAllZero()) {
							if (detailMemo.isDirty()) {
								Ext.Ajax.request({
									url : "resource/updateIssueHead.action",
									success : function(action) {
										// 返回值
										var result = eval("("
												+ action.responseText + ")");
										if (!result.success) {
											Ext.Msg.alert(Constants.ERROR,
													result.msg);
											return;
										} else {
											// 弹出保存成功提示
											Ext.Msg
													.alert(
															Constants.SYS_REMIND_MSG,
															Constants.COM_I_004
																	+ "<br/>"
																	+ Constants.RS004_I_002);
											// 领料单数据重新载入
											issueHeaderStore.load({
												params : {
													start : 0,
													limit : 18
												}
											});
											// 画面初期化
											initPanelDatas();
											return;
										}
									},
									params : {
										'issueHeadId' : hdnIssueHeadId
												.getValue(),
										'strMemo' : detailMemo.getValue(),
										'lastModifiedDate' : hdnLastModifiedDate
												.getValue()
									}
								})
							}
							return;
						}
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
											// 领料单.是否紧急领用
											var isEmergency = false;
											if (Constants.CHECKED_VALUE == editHeadRecord
													.get("isEmergency")) {
												isEmergency = true;
											}
											if (!checkIssueNum(isEmergency)) {
												// check出错，返回
												return;
											} else {

												updateDbTable();
											}
										}
									}
								}, "transCode=I");
					}
				});
	}
	/**
	 * 判断本次发货数量是否有效
	 */
	function isThisNumValid() {
		// 本次发货数量check
		for (var i = 0; i < rightStore.getCount() - 1; i++) {// modify by
			// ywliu 090724
			// 在Store中除去统计行
			var record = rightStore.getAt(i);
			// 本次发货数
			var thisNum = record.get('thisNum');
			// 待发货数
			var waitNum = record.get('waitNum');
			if (thisNum < 0) {
				Ext.Msg.alert(Constants.SYS_REMIND_MSG, String.format(
						Constants.COM_E_012, '本次发货数量'));
				return false;
			}
		}
		for (var i = 0; i < rightStore.getCount() - 1; i++) { // modify by
			// ywliu 090724
			// 在Store中除去统计行
			var record = rightStore.getAt(i);
			// 本次发货数
			var thisNum = record.get('thisNum');
			// 待发货数
			var waitNum = record.get('waitNum');
			if (thisNum > waitNum) {
				Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.RS004_E_002);
				return false;
			}
		}
		return true;
	}
	/**
	 * db更新处理
	 */
	function updateDbTable() {
		for (var i = 0; i < rightStore.getCount() - 1; i++) {// 将统计行的数据去掉 add
			// by ywliu
			// 090723
			var temp = rightStore.getAt(i).data.whsNo;
			var k = 0;
			for (var j = 0; j < birtWhsNo.length; j++) {
				if (temp == birtWhsNo[j]) {
					k = 1;
				}
			}
			if (k == 0) {
				birtWhsNo.push(rightStore.getAt(i).data.whsNo);
			}
		}
		var params = [];
		birtIssueNo = editHeadRecord.data.issueNo;
		// 领料单数据
		params.push('form=' + Ext.util.JSON.encode(editHeadRecord.data));
		// 备注
		params.push('&memo=' + detailMemo.getValue());
		// grid数据
		params.push('&records='
				+ Ext.util.JSON.encode(getMaterialDetailGridDataList()));

		Ext.Ajax.request({
			url : 'resource/updateIssueDbTables.action',
			success : function(action) {
				if (birtIssueNo != null && birtIssueNo != ""
						&& birtWhsNo.length > 0) {
						
						// modified by liuyi 20100128 
						if(sourceType.getValue() != null && sourceType.getValue() == '固定资产类')
						{
							for(var i = 0; i <= rightStore.getCount() - 1; i++)
							{
								if(rightStore.getAt(i).get('materialId') != null && rightStore.getAt(i).get('materialId') != '')
								{
								if (rightStore.getAt(i).get('thisNum') != null
										&& rightStore.getAt(i).get('thisNum') != ''
										&& rightStore.getAt(i).get('thisNum') != 0) {
									// 仓库
									var whoNameTemp = rightStore.getAt(i)
											.get('whsNo')
									if (whoNameTemp != null
											&& whoNameTemp != "") {
										strReportAdds = "/powerrpt/report/webfile/issueDetails.jsp?issueNo="
												+ birtIssueNo
												+ "&whsName="
												+ whoNameTemp
												+ "&filledDate="
												+ birtEntryDate
												// 固定资产类标记 gdFlag=1
												+ "&gdFlag=1"
												+ "&materialId="
												+ rightStore.getAt(i)
														.get('materialId');
										window.open(strReportAdds);
									} else {
										var materTemp = rightStore.getAt(i)
												.get('materialId')
										// var issueTemp = birtIssueNo

										findDefaultWhoById(materTemp,
												birtIssueNo, birtEntryDate);
									}
								}

							}
							
							}
						}
						//-------------------------------------------------
						else {
						for (var i = 0; i < birtWhsNo.length; i++) {
							var birtwhsName = birtWhsNo[i];
							if (birtwhsName != null && birtwhsName != "") {
								strReportAdds = "/powerrpt/report/webfile/issueDetails.jsp?issueNo="
										+ birtIssueNo
										+ "&whsName="
										+ birtwhsName
										+ "&filledDate="
										+ birtEntryDate;
								window.open(strReportAdds);
							} else {
								var result = eval("(" + action.responseText
										+ ")");
								for (var i = 0; i < result.length; i++) {
									strReportAdds = "/powerrpt/report/webfile/issueDetails.jsp?issueNo="
											+ birtIssueNo
											+ "&whsName="
											+ result[i]
											+ "&filledDate="
											+ birtEntryDate;
									window.open(strReportAdds);
								}
							}
						}// add by ywliu 2009/06/25

					}
				}
				// 返回值
				var result = eval("(" + action.responseText + ")");
				if (result == null) { // modify by ywliu if(!result.success)
					Ext.Msg.alert(Constants.SYS_REMIND_MSG, result.msg);
					return;
				}
				Ext.Msg.alert(Constants.SYS_REMIND_MSG, "出库成功！", function() { // modify
							// by
							// ywliu
							// 2009/7/2
							// 将提示信息改为出库成功
							// 画面初期化
							initPanelDatas();
							window.close();
						});
				birtIssueNo = "";
				birtWhsNo = [];
			},
			failure : function() {
				Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_E_014);
			},
			params : {
				'form' : Ext.util.JSON.encode(editHeadRecord.data),
				'strMemo' : detailMemo.getValue(),
				'records' : Ext.util.JSON
						.encode(getMaterialDetailGridDataList()),
				'getRealPerson' : getRealPersonH.getValue()
			}
		});
	}
	/**
	 * 获取物料明细grid中的数据
	 */
	function getMaterialDetailGridDataList() {
		var list = [];
		for (var i = 0; i < rightStore.getCount() - 1; i++) { // 将统计行的数据去掉 add
			// by ywliu
			// 090723
			list.push(rightStore.getAt(i).data);
		}
		return list;
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
		issueHeaderStore.load({
			params : {
				start : 0,
				limit : 18
			}
		});

	}
	/**
	 * 判断明细部分是否变化
	 */
	function isDetailChanged() {
		// 判断领料单备注有无变化
		if (detailMemo.isDirty()) {
			// form备注有变化
			return true;
		}
		// 判断明细记录有无变化
		for (var i = 0; i < rightStore.getCount() - 1; i++) {// modify by
			// ywliu 090724
			// 在Store中除去统计行
			var record = rightStore.getAt(i);
			// 本次发货数量
			if (record.get('thisNum') !== 0) {
				return true;
			}
			// 批号
			if (record.get('lotNo') && record.get('lotNo') !== '0') {
				return true;
			}
			// 仓库
			if (record.get('whsNo')) {
				return true;
			}
			// 库位
			if (record.get('locationNo')) {
				return true;
			}
			// 备注
			if (record.get('memo')) {
				return true;
			}
		}
		return false;
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
		// add by fyyang 090619 计划来源改为了单据类型
		sourceType.setValue(getBillNameById(editHeadRecord
				.get("planOriginalDesc")));
		rightStore.load({
			params : {
				start : 0,
				limit : 18
			},
			callback : addLine
				// 加入统计行 add by ywliu 090722
				});
		// 事务作用码
		bussinessCode.setValue('I');
		// 事务作用名称
		bussinessName.setValue('出库');
		// 操作日期
		operateDate.setValue(getSysDate());
		// TODO 临时费用来源
		var itemCode = editHeadRecord.get('itemCode');
		costSource.setValue(getBudgetName(itemCode));
//		if (itemCode == "zzfy") {
//			costSource.setValue("生产成本");
//		} else if (itemCode == "lwcb") {
//			costSource.setValue("劳务成本");
//		}
		// 费用来源显示中文 , modify by ywliu 2009/7/10
		// costSource.setValue(itemCode);
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
		Ext.lib.Ajax.request('POST', 'resource/getLogonWorkerCode.action', {
			success : function(action) {
				operator.setValue(action.responseText);
				operator.originalValue = operator.getValue();
			},
			failure : function() {
			}
		});
	}

	rightStore.on("load", function() {
		Ext.Msg.hide();
	});
	/**
	 * 判断本次发货数量是否为空
	 */
	function isThisNumBlank() {
		// 记录
		var record;
		// 循环判断
		for (var i = 0; i < rightStore.getCount() - 1; i++) {// modify by
			// ywliu 090724
			// 在Store中除去统计行
			record = rightStore.getAt(i);
			// 出库数有输入,返回false
			// 5/9/09 16:32 缺少判定输入为0时的情况 yiliu
			if (record.get('thisNum') == null || record.get('thisNum') === ""
					|| record.get('thisNum') == 0) {
				return true;
			}
		}
		// 没有输入出库数
		return false;
	}
	/**
	 * 判断出库数量是不是全部为零
	 */
	function isThisNumAllZero() {
		// 记录
		var record;
		// 循环判断
		for (var i = 0; i < rightStore.getCount() - 1; i++) {// modify by
			// ywliu 090724
			// 在Store中除去统计行
			record = rightStore.getAt(i);
			// 出库数有输入,返回false
			if (record.get('thisNum') && record.get('thisNum') != 0) {
				return false;
			}
		}
		// 没有输入出库数
		return true;
	}
	/**
	 * 出库数量check
	 */
	function checkIssueNum(isEmergency) {
		var msg = "";
		for (var i = 0; i < rightStore.getCount() - 1; i++) { // modify by
			// ywliu 090724
			// 在Store中除去统计行
			var record = rightStore.getAt(i);
			// 本次出库数量
			var issueNum = record.get('thisNum');
			// 如果没有输入出货量，不做check
			if (issueNum == null || issueNum == 0) {
				continue;
			}
			// 判断物资库存数量是否够用
			msg = checkIssueNumsAll(record);

			// check出错
			if (msg) {
				Ext.Msg.alert(Constants.SYS_REMIND_MSG, msg);
				return false;
			}
			//modify by fyyang 091202
//			// 如果该领料单为紧急领用，不做下面的check
//			if (isEmergency) {
//				continue;
//			}
//			// 检索此物料的库存总量
//			var url = "resource/getStorageMaterialCounts.action?materialId="
//					+ record.get('materialId');
//			var conn = Ext.lib.Ajax.getConnectionObject().conn;
//			conn.open("POST", url, false);
//			conn.send(null);
//			// 当前物料总库存数
//			var allCounts = Ext.util.JSON.decode("(" + conn.responseText + ")");
//
//			// 检查去除紧急领用后的物资数量是不是够用
//			msg = checkIssueNumsReduceEmergency(record, allCounts);
//			if (msg) {
//				Ext.Msg.alert(Constants.SYS_REMIND_MSG, msg);
//				return false;
//			}
//			var url = "resource/isIssueTypePlan.action";
//			var conn = Ext.lib.Ajax.getConnectionObject().conn;
//			conn.open("POST", url, false);
//			conn.send(null);
//			if ("true" != conn.responseText) {
//				// 不是按计划不做计划check
//				continue;
//			}
//			// 检查计划等级高的领料单所需物料数
//			msg = checkPlanRelating(record, allCounts);
//			if (msg) {
//				Ext.Msg.alert(Constants.SYS_REMIND_MSG, msg);
//				return false;
//			}

		}

		return true;
	}
	/**
	 * 不去除紧急领用时，出库数量check
	 */
	function checkIssueNumsAll(record) {
		// 批号
		var lotNo = record.get('lotNo');
		// 仓库
		var whsNo = record.get('whsNo');
		// 库位
		var locationNo = record.get('locationNo');
		// 物料id
		var lngMaterialId = record.get('materialId');
		// 本次出库数量
		var issueNum = record.get('thisNum');
		var params = [];
		// 物料id
		params.push("materialId=" + lngMaterialId);
		// 批号、仓库、库位、全部为空的情况下
		if (locationNo) {
			params.push("&locationNo=" + locationNo);
		}
		if (whsNo) {
			params.push("&whsNo=" + whsNo);
		}
		if (lotNo) {
			params.push("&lotNo=" + lotNo);
		}
		var url = "resource/getStorageMaterialCounts.action?" + params.join('');
		var conn = Ext.lib.Ajax.getConnectionObject().conn;
		conn.open("POST", url, false);
		conn.send(null);
		// 库存数
		var counts = Ext.util.JSON.decode("(" + conn.responseText + ")");
		// 如果库存数小于要出库数
		if ((counts - issueNum) < 0) {

			if (!lotNo) {
				// 批号为空
				return String.format(Constants.RS004_E_001, "库存");
			}
			if (!whsNo) {
				// 非批控制
				if (lotNo == "0") {
					return String.format(Constants.RS004_E_001, "库存");
				}
				// 仓库为空
				return String.format(Constants.RS004_E_001, "批次");
			}
			if (!locationNo) {
				// 库位为空
				return String.format(Constants.RS004_E_001, "仓库");
			}
			// 都不为空
			return String.format(Constants.RS004_E_001, "库位");
		}
		return "";
	}
	/**
	 * 去除紧急领用时，出库数量check
	 * 
	 * @param record
	 *            grid的记录
	 */
	function checkIssueNumsReduceEmergency(record, allCounts) {
		// 物料id
		var lngMaterialId = record.get('materialId');
		// 本次出库数量
		var issueNum = record.get('thisNum');
		var params = [];
		// 物料id
		params.push("materialId=" + lngMaterialId);
		// 检索紧急领用所需的该物料数量
		var url = "resource/getEmergencyDemandCounts.action?" + params.join('');
		var conn = Ext.lib.Ajax.getConnectionObject().conn;
		conn.open("POST", url, false);
		conn.send(null);
		// 紧急领用所需物料总数
		var EmCounts = Ext.util.JSON.decode("(" + conn.responseText + ")");
		// 本次出库数量要小于等于当前此物资的库存总量与领料单.是否紧急领用为“是”的所需此物资总量的差
		if (allCounts - EmCounts < issueNum) {
			return String.format(Constants.RS004_E_001, "库存");
		}
		return "";
	}

	/**
	 * 计划关联check
	 */
	function checkPlanRelating(record, allCounts) {
		// 需求单编号
		var mrNo = editHeadRecord.get('mrNo');
		// 物料id
		var lngMaterialId = record.get('materialId');
		// 领料单id
		var issueHeadId = editHeadRecord.get('issueHeadId');
		// 本次出库数量
		var issueNum = record.get('thisNum');
		// 参数
		var params = [];
		// 物料id
		params.push("materialId=" + lngMaterialId);
		// 领料单id
		params.push('&issueHeadId=' + issueHeadId);
		// 需求单存在
		if (mrNo) {
			params.push('&mrNo=' + mrNo);
		}
		// 计划等级高的领料单所需的该物料数量
		var url = "resource/getPlanRelateCounts.action?" + params.join('');
		var conn = Ext.lib.Ajax.getConnectionObject().conn;
		conn.open("POST", url, false);
		conn.send(null);
		// 所需物料总数
		var counts = Ext.util.JSON.decode("(" + conn.responseText + ")");
		// 本次出库数量要小于等于当前此物资的库存总量与领料单.是否紧急领用为“是”的所需此物资总量的差
		if (allCounts - counts < issueNum) {
			return String.format(Constants.RS004_E_001, "库存");
		}
		return "";
	}

	function cancelIssue() {
		// 确认是否正在结帐
		Ext.lib.Ajax.request('POST', 'resource/isIssueBalanceNow.action', {
			success : function(action) {
				if ('true' == action.responseText) {
					// 正在结帐,弹出信息无法进行业务
					Ext.Msg
							.alert(Constants.SYS_REMIND_MSG,
									Constants.COM_E_001);
					return;
				} else {
					window.showModalDialog('cancelIssueQuery.jsp', null,
							'status:no;dialogWidth=750px;dialogHeight=450px');
					issueHeaderStore.reload();

				}
			}
		}, "transCode=I");

		// }
	}

	// add by drdu 091120

//	function printReport() {
//		window.showModalDialog('afterPrintIssue.jsp', null,
//				'status:no;dialogWidth=750px;dialogHeight=450px');
//		issueHeaderStore.reload();
//	}

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
	function numRenderer(v) {
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
	 * 格式化仓库数据
	 */
	function comboBoxWarehouseRenderer(value, cellmeta, record) {
		var whsNo = record.data["whsNo"];
		if (!whsNo) {
			return;
		}
		var url = "resource/getWarehouseName.action?whsNo=" + whsNo;
		var conn = Ext.lib.Ajax.getConnectionObject().conn;
		conn.open("POST", url, false);
		conn.send(null);
		return conn.responseText;

	};
	/**
	 * 格式化库位数据
	 */
	function comboBoxLocationRenderer(value, cellmeta, record) {
		var whsNo = record.data["whsNo"];
		var locationNo = record.data["locationNo"];
		if (!whsNo || !locationNo) {
			return;
		}
		var url = "resource/getLocationName.action?whsNo=" + whsNo
				+ "&locationNo=" + locationNo;
		var conn = Ext.lib.Ajax.getConnectionObject().conn;
		conn.open("POST", url, false);
		conn.send(null);
		return conn.responseText;

	}
	
	
	// add by liuyi 20100201
	function findDefaultWhoById(materialId,birtIssueNo,birtEntryDate)
	{
		
		var conn = Ext.lib.Ajax.getConnectionObject().conn;
					conn.open("POST", "resource/getMaterialById.action?materialId="
				+ materialId, false);
					conn.send(null); 
					// 成功状态码为200
					if (conn.status == "200") {    
						var o = eval('(' + conn.responseText + ')'); 
						strReportAdds = "/powerrpt/report/webfile/issueDetails.jsp?issueNo="
											+ birtIssueNo
											+ "&whsName="
											+ o.defaultWhsNo
											+ "&filledDate="
											+ birtEntryDate
											// 固定资产类标记 gdFlag=1
											+ "&gdFlag=1"
											+ "&materialId="
											+ materialId;
									window.open(strReportAdds);

					}
					
//		Ext.Ajax.request({
//			url : 'resource/getMaterialById.action',
//			method : 'post',
//			params : {
//				materialId : materialId
//			},
//			success : function(response, options) {
//				var result = Ext.util.JSON.decode(response.responseText)
//				return result.defaultWhsNo
//			},
//			failure : function() {
//				return null;
//			}
//		})
	}
		// ↑↑********************处理***********************
});
