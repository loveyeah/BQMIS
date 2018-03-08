Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
function renderDate(value) {
	if (!value)
		return "";

	var reDate = /\d{4}\-\d{2}\-\d{2}/gi;
	var strDate = value.match(reDate);
	if (!strDate)
		return "";
	return strDate;
}
function numberFormat(value) {
	if (value === null) {
		return "";
	}
	if (value === "") {
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
	v = v + "元";
	return v;
}
Ext.onReady(function() {
	Ext.QuickTips.init();
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
				}
			});
	
	/**
	 * 下载文件
	 */
	download = function(id) {
		document.all.blankFrame.src = "administration/downloadAdJCarwhInvoiceFile.action?id="
				+ id;
	}
	Ext.override(Ext.grid.GridView, {
				// 重写doRender方法
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
							// 如果该行是统计行并且改列是第一列
							if (r.data["countType"] == "total" && i == 0) {
								p.value = "合计";
							} else {
								p.value = c.renderer(r.data[c.name], p, r,
										rowIndex, i, ds);
							}
							p.style = c.style;
							if (p.value == undefined || p.value === "")
								p.value = "&#160;";
							if (r.dirty
									&& typeof r.modified[c.name] !== 'undefined') {
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
	var isSave;
	var projectDetails = [];
	var deleteDetails = [];
	// ↓↓*******************车辆维修申请**************************************

	// 车辆维修申请grid的store
	var carRepairApproveStore = new Ext.data.JsonStore({
				url : 'administration/getCarRepairApproveQueryManagementList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : [
						// 流水号
						{
					name : 'id'
				},
						// 到车辆维护ID
						{
							name : 'whId'
						},// 车牌号
						{
							name : 'carNo'
						},
						// 车名
						{
							name : 'carName'
						},
						// 维修日期
						{
							name : 'repairDate'
						},
						// 单位编码
						{
							name : 'cpCode'
						},
						// 维修里程
						{
							name : 'driveMile'
						},// 经办人
						{
							name : 'manager'
						},
						// 支出事由
						{
							name : 'reason'
						},// 预计费用
						{
							name : 'sum'
						},// 备注
						{
							name : 'memo'
						},// 修改时间
						{
							name : 'updateTime'
						}, {
							name : 'repairStatus'
						}, {
							name : 'managerName'
						}, {
							name : "driveFileId"
						}, {
							name : "driveFileUpdateTime"
						}, {
							name : "useStatus"
						}]
			});

	carRepairApproveStore.load({
				params : {
					start : 0,
					limit : Constants.PAGE_SIZE
				}
			});
	carRepairApproveStore.on("load", function() {
	
	});

	// grid
	var carRepairApproveGrid = new Ext.grid.GridPanel({
		border : true,
		sm : new Ext.grid.RowSelectionModel({
					singleSelect : true
				}),
		fitToFrame : true,
		store : carRepairApproveStore,
		columns : [new Ext.grid.RowNumberer({
							header : "行号",
							width : 35
						}),
				// 维修申请单号
				{
					header : "维修申请单号",
					sortable : true,
					width : 150,
					dataIndex : 'whId'
				},
				// 车牌号
				{
					header : "车牌号",
					sortable : true,
					width : 100,
					dataIndex : 'carNo'
				},
				// 车名
				{
					header : "车名",
					sortable : true,
					width : 75,
					dataIndex : 'carName'
				}],
		viewConfig : {
			forceFit : false
		},
		tbar : [{
					text : Constants.BTN_DETAIL,
					iconCls : Constants.CLS_DETAIL,
					handler : function() {
						showCarRepairApproveDetail();
					}
				}, {
					text : "打印维修单",
					iconCls : Constants.CLS_PRINT,
					handler : function() {
						if (carRepairApproveGrid.selModel.hasSelection()) {
							// xsTan 追加开始 2009-2-4 追加打印接口
							var record = carRepairApproveGrid
									.getSelectionModel().getSelected();
							var applyNo = record.get('whId');
							window
									.open("/power/report/webfile/administration/ARF07.jsp?applyNo="
											+ applyNo);
							// xsTan 追加结束 2009-2-4 追加打印接口
						} else {

							Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
									Constants.COM_I_001);
						}
					}
				}],
		enableColumnMove : false,
		// 分页
		bbar : new Ext.PagingToolbar({
					pageSize : Constants.PAGE_SIZE,
					store : carRepairApproveStore,
					displayInfo : true,
					displayMsg : MessageConstants.DISPLAY_MSG,
					emptyMsg : MessageConstants.EMPTY_MSG
				})
	});

	carRepairApproveGrid.on("rowdblclick", showCarRepairApproveDetail);

	// 维修申请单号
	var whIdTextfield = new Ext.form.TextField({
				id : "whId",
				xtype : "textfield",
				fieldLabel : '维修申请单号',
				readOnly : true,
				anchor : "100%",
				name : 'bean.whId'
			});
	// 不可继续使用
	var isContiueUse = new Ext.form.Checkbox({
				id : "isContiueUse",
				boxLabel : "不可继续使用",
				name : "isContiueUse",
				hideLabel : true
			})
	// 车牌号
	var carNoTextfield = new Ext.form.TextField({
				id : "carNo",
				xtype : "textfield",
				fieldLabel : '车牌号',
				readOnly : true,
				anchor : "100%",
				name : 'bean.carNo'
			});

	// 车名
	var carNameTextfield = new Ext.form.TextField({
				id : "carName",
				xtype : "textfield",
				fieldLabel : '车名',
				readOnly : true,
				anchor : "100%",
				name : 'bean.carName'
			});

	// 维修里程
	var driveMileTextfield = new Ext.form.MoneyField({
				id : "driveMile",
				// xtype : "textfield",
				fieldLabel : '维修里程',
				maxValue : 9999999999999.99,
				appendChar : '公里',
				allowNegative : false,
				style:'text-align:right',
				padding : 2,
				readOnly : false,
				anchor : "100%",
				name : ''
			});
	var driveMileHidden = new Ext.form.Hidden({
				id : "driveMileHidden",
				name : "bean.driveMile"
			});

	// 经办人
	var managerTextfield = new Ext.form.TextField({
				id : "managerName",
				xtype : "textfield",
				fieldLabel : '经办人',
				readOnly : true,
				anchor : "100%",
				name : ''
			});

	// 维修单位
	var cpCodeStore = new Ext.data.JsonStore({
				root : 'list',
				url : "administration/getAdCCarmendWhCpCodeManagementList.action",
				fields : ['cpCode', 'cpName']
			})
	cpCodeStore.load();
	var cpCodeCombo = new Ext.form.ComboBox({
		store : cpCodeStore,
		displayField : "cpName",
		valueField : "cpCode",
		mode : 'local',
		fieldLabel : "维修单位",
		triggerAction : 'all',
		anchor : '99.8%',
		readOnly : true
			// width : 80
		})

	// 维修日期
	var repairDateField = new Ext.form.TextField({
				id : "repairDate",
				anchor : '98%',
				// format : 'Y-m-d',
				fieldLabel : "维修日期<font color='red'>*</font>",
				readOnly : true,
				isFormField : true,
				name : 'bean.repairDate',
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd',
									alwaysUseStartDate : false,
									isShowClear : false
								});
					}
				}
			});

	// 预算费用
	var sumNumfield = new Ext.form.MoneyField({
				anchor : '100%',
				fieldLabel : '预算费用',
				appendChar : '元',
				maxValue : 99999999999.99,
				padding : 2,
				id : 'sum',
				hiddenName : 'sum',
				readOnly : true

			})
	// 实际费用
	var realSumNumfield = new Ext.form.MoneyField({
				anchor : '100%',
				fieldLabel : '实际费用',
				appendChar : '元',
				maxValue : 99999999999.99,
				padding : 2,
				id : 'realSum',
				hiddenName : 'realSum',
				readOnly : true

			})
	var sumHidden = new Ext.form.Hidden({
				id : "hiddenSum",
				name : 'bean.sum'
			})

	var realSumHidden = new Ext.form.Hidden({
				id : "realSumHidden",
				name : 'bean.realSum'
			})

	// 支出事由
	var reasonTextArea = new Ext.form.TextArea({
				id : "reason",
				fieldLabel : '支出事由',
				height : 40,
				maxLength : 30,
				readOnly : true,
				anchor : "99%",
				name : 'bean.reason'
			});

	// 备注
	var memoTextArea = new Ext.form.TextArea({
				id : "memo",
				fieldLabel : '备注',
				height : 40,
				maxLength : 50,
				readOnly : false,
				anchor : "99%",
				name : 'bean.memo'
			});

	// 参会人员 会议材料 会议桌签 DS
	var dsMatieria = new Ext.data.JsonStore({
				url : 'administration/getAdJCarwhInvoiceFile.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : ['id', 'fileName', 'updateTime']
			})

	// 参会人员 会议材料 会议桌签 cm
	var cmMatieria = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
						header : '行号',
						width : 35
					}), {
				header : '文件名',
				width : 200,
				dataIndex : 'fileName',
				renderer : fileName,
				sortable : true,
				align : 'left'
			}, {
				header : '删除附件',
				width : 70,
				align :'center',
				renderer : dele
			}])

	// 附件
	var tfAppend = new Ext.form.TextField({
				id : "fileUpload",
				inputType : 'file',
				name : 'fileUpload',
				height:20,
				fieldLabel : "附件",
				width : 200
			});

	// 上传附件
	var btnUploadAppend = new Ext.Button({
				text : '上传附件',
				iconCls : Constants.CLS_UPLOAD,
				align : 'center',
				handler : btnUploadAppendHandler
			})

	var testpanel = new Ext.FormPanel({
				frame : true,
				style : "border:0px",
				fileUpload : true,
				items : [tfAppend, btnUploadAppend]
			})

	// 头部工具栏
	var tbarQuestFile = new Ext.Toolbar(testpanel)

	// 参会人员 会议材料 会议桌签
	var gridMatieria = new Ext.grid.GridPanel({
		// layout : 'fit',
		ds : dsMatieria,
		cm : cmMatieria,
		tbar : tbarQuestFile,
		style : "padding-top:0px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
		width : 468,
		height : 150,
		autoScroll : true,
		enableColumnMove : false,
		enableColumnHide : true,
		border : false
	})

	var fpnlMatieria = new Ext.FormPanel({
		border : false,
		fileUpload : true,
		style : 'padding-top:0px;padding-left:0px;padding-right:1px;margin-right:0px',
		items : [gridMatieria]
	})
	var headForm = new Ext.FormPanel({
				border : false,
				labelAlign : 'right',
				style : {
					'border' : 0
				},
				// region : "north",
				frame : true,
				layout : 'form',
				labelWidth : 90,
				width : 469,
				autoHeight : true,
				items : [{
							layout : "column",
							style : "padding-top:4px",
							border : false,
							items : [{
										columnWidth : 0.48,
										layout : "form",
										border : false,
										height : 30,
										labelAlign : "right",
										items : [whIdTextfield]
									}, {
										columnWidth : 0.215,

										border : false

									}, {
										columnWidth : 0.25,
										layout : "form",
										border : false,
										height : 30,
										labelAlign : "right",
										items : [isContiueUse]
									}]
						}, {
							layout : "column",
							style : "padding-top:0px",
							border : false,
							items : [{
										columnWidth : 0.48,
										layout : "form",
										border : false,
										height : 30,
										labelAlign : "right",
										items : [carNoTextfield]
									}, {
										columnWidth : 0.48,
										layout : "form",
										border : false,
										height : 30,
										labelAlign : "right",
										items : [carNameTextfield]
									}]
						}, {
							layout : "column",
							style : "padding-top:0px",
							border : false,
							items : [{
										columnWidth : 0.48,
										layout : "form",
										border : false,
										height : 30,
										labelAlign : "right",
										items : [driveMileTextfield]
									}, {
										columnWidth : 0.48,
										layout : "form",
										border : false,
										height : 30,
										labelAlign : "right",
										items : [managerTextfield]
									}]
						}, {
							layout : "column",
							style : "padding-top:0px",
							border : false,
							items : [{
										columnWidth : 0.48,
										layout : "form",
										border : false,
										height : 30,
										labelAlign : "right",
										items : [cpCodeCombo]
									}, {
										columnWidth : 0.49,
										layout : "form",
										border : false,
										height : 30,
										labelAlign : "right",
										items : [repairDateField]
									}]
						}, {
							layout : "column",
							style : "padding-top:0px",
							border : false,
							items : [{
										columnWidth : 0.48,
										layout : "form",
										border : false,
										height : 30,
										labelAlign : "right",
										items : [sumNumfield]
									}, {
										columnWidth : 0.48,
										layout : "form",
										border : false,
										height : 30,
										labelAlign : "right",
										items : [realSumNumfield]
									}]
						}, {
							layout : "column",
							style : "padding-top:0px",
							border : false,
							items : [{
										columnWidth : 0.965,
										layout : "form",
										border : false,
										height : 45,
										labelAlign : "right",
										items : [reasonTextArea]
									}]
						}, {
							layout : "column",
							style : "padding-top:2px",
							border : false,
							items : [{
										columnWidth : 0.965,
										layout : "form",
										border : false,
										height : 45,
										labelAlign : "right",
										items : [memoTextArea]
									}]
						}, sumHidden, realSumHidden, driveMileHidden]

			});
	// 增加明细button
	var addDetailBtn = new Ext.Button({
				text : "新增明细",
				handler : function() {
					addDetailList();
				}
			});

	// 删除明细button
	var deleteDetailBtn = new Ext.Button({
				text : "删除明细",
				handler : function() {
					deleteDetailList();
				}
			});
	// 取消明细button
	var cancelDetailBtn = new Ext.Button({
				text : "取消明细",
				handler : function() {
					cancelDetailList();
				}
			});

	var repairProjectStore = new Ext.data.JsonStore({
				lastQuery : '',
				root : 'list',
				url : "administration/getRepairProjectManagementList.action",
				fields : ['proCode', 'proName', 'haveLise']
			})
	repairProjectStore.load();
	var unitStore = new Ext.data.JsonStore({
				lastQuery : '',
				root : 'list',
				url : "administration/getUnit.action",
				fields : ['strUnitName', 'strUnitID']
			})
	unitStore.load();
	// 明细记录
	var carwhMxDetailRecord = Ext.data.Record.create([
			// 车辆维护明细id
			{
		name : 'carwhMxId'
	},
			// 项目代码
			{
				name : "proCode"
			},// 项目详细
			{
				name : "proDetail"
			},
			// 费用预算
			{
				name : "price"
			},
			// 车辆维护明细.修改时间
			{
				name : "carwhMxUpdateTime"
			},
			// 车辆维护之维修项目.有无清单，
			{
				name : 'haveLise'
			},// 车辆维护之维修项目.序号
			{
				name : 'carwhProId'
			},
			// 车辆维护之维修项目.修改时间
			{
				name : "carwhProUpdateTime"
			}, {
				name : "realPrice"
			},
			// 是否是新规的记录
			{
				name : "isNew"
			},{
				name : "proName"
			}]);

	// 明细Store
	var carwhMxDetailStore = new Ext.data.JsonStore({
				url : 'administration/getCarwhMxDetailManagementList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : carwhMxDetailRecord
			});
	var carwhMxDetailCopyStore = new Ext.data.JsonStore({
				url : 'administration/getCarwhMxDetailManagementList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : carwhMxDetailRecord
			});

	carwhMxDetailStore.on('beforeload', function() {
				Ext.apply(this.baseParams, {
							whId : whIdTextfield.getValue()
						});
			});
	carwhMxDetailCopyStore.on('beforeload', function() {
				Ext.apply(this.baseParams, {
							whId : whIdTextfield.getValue()
						});
			});

	// 明细grid
	var carwhMxDetailGrid = new Ext.grid.EditorGridPanel({
		height : 220,
		width : 468,
		// region : "center",
		style : "padding-top:0px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
		border : false,
		clicksToEdit : 1,
		// autoScroll : true,
		sm : new Ext.grid.RowSelectionModel({
					singleSelect : true
				}),
		tbar : [addDetailBtn,  deleteDetailBtn,  cancelDetailBtn],
		store : carwhMxDetailStore,
		columns : [new Ext.grid.RowNumberer({
							header : "行号",
							width : 35
						}),
				// 项目
				{
					header : "项目<font color ='red'>*</font>",
					sortable : true,
					width : 75,
					editor : new Ext.form.ComboBox({
								store : repairProjectStore,
								displayField : "proName",
								valueField : "proCode",
								mode : 'local',
								lastQuery : '',
								triggerAction : 'all',
								readOnly : true

							}),
					dataIndex : 'proName'
				},
				// 项目详细
				{
					header : "项目详细",
					sortable : true,
					width : 150,
					align :'center',
					renderer : projectDetailFormat,
					dataIndex : 'proDetail'
				},
				// 费用预算（元）
				{
					header : "实际费用",
					sortable : true,
					width : 150,
					align : 'right',
					editor : new Ext.form.NumberField({
								maxValue : 99999999999.99
							}),
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {

						// 如果不是最后一行
						if (rowIndex < store.getCount() - 1) {
							return renderNumber(value);
							// 如果是最后一行
						} else {
							var totalSum = 0;
							// 对该列除最后一个单元格以为求和
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('realPrice') * 1;
							}
							realSumNumfield.setValue(totalSum);
							return renderNumber(totalSum);
						}
					},
					dataIndex : 'realPrice'
				}],

		enableColumnMove : false
	});

	carwhMxDetailGrid.on("beforeedit", function(obj) {
		var record = obj.record;
		var field = obj.field;
		if (record.get("countType") === "total") {
			obj.cancel = true;
			return;
		}
		var detailRecord = carwhMxDetailStore.getAt(carwhMxDetailStore
				.getCount()
				- 1);
		if (detailRecord.data.countType === "total") {
			carwhMxDetailStore.remove(detailRecord);
		}
		if (field === "realPrice") {

			var proCode = record.get("proCode");
			repairProjectStore.clearFilter(true);
			for (var i = 0; i < repairProjectStore.getCount(); i++) {
				if (repairProjectStore.getAt(i).get("proCode") === proCode) {
					if (repairProjectStore.getAt(i).get("haveLise") === "Y") {

						obj.cancel = true;
					}
				}
			}

			commDetailFilter();
			addLine();
		}
		if (field === "proName") {
			carwhMxDetailStore.filterBy(function(record) {
						if (record.data.isNew === "1") {
							return false;
						} else {
							return true;
						}
					})
			commDetailFilter();
			addLine();
			repairProjectStore.filterBy(function(record) {
				var count = carwhMxDetailStore.getCount();
				for (var i = 0; i < carwhMxDetailStore.getCount() - 1; i++) {
					if ((carwhMxDetailStore.getAt(i).data.proCode === record.data.proCode)) {
						return false
					}
				}
				return true;

			});
		}

	});

	carwhMxDetailGrid.on("afteredit", function(obj) {
		var detailRecord = carwhMxDetailStore.getAt(carwhMxDetailStore
				.getCount()
				- 1);
		if (detailRecord.data.countType === "total") {
			carwhMxDetailStore.remove(detailRecord);
		}
		var record = obj.record;
		var field = obj.field;
		var proCodeBefore = record.get("proCode");
		var proCodeAfter = record.get("proName");
		if (field === "proName") {
			repairProjectStore.clearFilter(true);
			carwhListStore.clearFilter(true);
			carwhMxDetailStore.clearFilter(true);
			if (record.data.carwhMxId !== "") {
				var originalValue = obj.originalValue;
				for (var i = 0; i < carwhMxDetailCopyStore.getCount(); i++) {
					if (record.data.carwhMxId === carwhMxDetailCopyStore
							.getAt(i).get("carwhMxId")) {
						record.set('carwhMxId', "");
						record.set('realPrice', 0);
						record.set('proName',projectNameFormat(proCodeAfter));
						record.set('proCode',proCodeAfter);
						record.set('isNew', "0");
						carwhMxDetailStore.insert(
								carwhMxDetailStore.getCount(), DetailCopy(
										carwhMxDetailCopyStore.getAt(i),
										record.data.price, "1"));
						carwhMxDetailStore.filterBy(function(record) {
									if (record.data.isNew === "1") {
										return false;
									} else {
										return true;
									}
								})

					}

				}
				var array = [];
				for (var j = 0; j < carwhListStore.getCount(); j++) {
					if (carwhListStore.getAt(j).get("proCode") === proCodeBefore) {
						if (carwhListStore.getAt(j).get("id") !== "") {
							carwhListStore.getAt(j).set("isNew", "1");
						} else {
							if (carwhListStore.getAt(j).get("whId") !== "") {
								array.push(carwhListStore.getAt(j));
							}
						}

					}
				}
				// 新规的要删除
				for (var i = 0; i < array.length; i++) {
					carwhListStore.remove(array[i]);
				}

			} else {
				record.set("realPrice", 0);
				record.set('proName',projectNameFormat(proCodeAfter));
				record.set('proCode',proCodeAfter);
				var originalValue = obj.originalValue;
				var array = [];
				for (var i = 0; i < carwhListStore.getCount(); i++) {
					if ((carwhListStore.getAt(i).get("proCode") === proCodeBefore)
							&& (carwhListStore.getAt(i).get("id") === "")) {
						array.push(carwhListStore.getAt(i));
					}
				}
				for (var i = 0; i < array.length; i++) {
					carwhListStore.remove(array[i]);
				}

				carwhMxDetailStore.filterBy(function(record) {
							if (record.data.isNew === "1") {
								return false;
							} else {
								return true;
							}
						});
			}

		}
		if (field === "realPrice") {
			if (record.data.price === "") {
				record.set("realPrice", 0);
			}
			commDetailFilter();

			var sum = 0;
			for (var i = 0; i < carwhMxDetailStore.getCount(); i++) {
				sum = parseFloat(carwhMxDetailStore.getAt(i).get("realPrice"))
						+ sum;
			}
			realSumNumfield.setValue(sum);

		}
		commDetailFilter();
		addLine();

	});

	// 弹出画面
	var win = new Ext.Window({
				height : 320,
				width : 500,
				layout : 'form',
				modal : true,
				resizable : false,
				autoScroll : true,
				closeAction : 'hide',
				items : [headForm, fpnlMatieria, carwhMxDetailGrid],
				buttonAlign : "center",
				title : '维修管理明细',
				buttons : [{
							text : Constants.BTN_SAVE,
							iconCls : Constants.CLS_SAVE,
							handler : function() {
								isSave = true;
								saveDetailList();
							}
						}, {
							text : Constants.BTN_STOP,
							iconCls : Constants.CLS_STOP,
							handler : function() {
								isSave = false;
								saveDetailList();
							}
						}, {
							text : Constants.BTN_CANCEL,
							iconCls : Constants.CLS_CANCEL,
							handler : function() {
								Ext.Msg.confirm(
										MessageConstants.SYS_CONFIRM_MSG,
										MessageConstants.COM_C_005, function(
												buttonobj) {
											if (buttonobj == 'yes') {
												win.hide();
											}
										});
							}
						}]
			});

	// fangjihu 备注修改
	// 备注
	var txtNote = new Ext.form.TextField({
				id : 'note',
				maxLength : 100,
				listeners : {
					"render" : function() {
						this.el.on("dblclick", cellClickHandler);
					}
				}
			})

	// 备注-弹出窗口
	var memoText = new Ext.form.TextArea({
				id : "memoText",
				maxLength : 100,
				width : 180
			});

	// 弹出画面
	var winEnter = new Ext.Window({
				height : 170,
				width : 350,
				layout : 'fit',
				resizable : false,
				modal : true,
				closeAction : 'hide',
				items : [memoText],
				buttonAlign : "center",
				title : '详细信息录入窗口',
				buttons : [{
					text : Constants.BTN_CONFIRM,
					iconCls : Constants.CLS_OK,
					handler : function() {
										var rec = carwhListGrid
												.getSelectionModel()
												.getSelections();
										if (Ext.get("memoText").dom.value.length <= 100
												&& memoText.isValid()) {
											rec[0]
													.set(
															"note",
															Ext.get("memoText").dom.value);
											winEnter.hide();
										}
									
					
					}
				}, {
					text : Constants.BTN_CANCEL,
					iconCls : Constants.CLS_CANCEL,
					handler : function() {
						winEnter.hide();
					}
				}]
			});
		
			
			
	// 项目详细一览grid（项目详细画面）
	var carwhListRecord = Ext.data.Record.create([
			// 材料结算清单id
			{
		name : 'id'
	},
			// 车辆维护ID
			{
				name : "whId"
			},// 项目代码
			{
				name : "proCode"
			},
			// 单位
			{
				name : "unit"
			},
			// 预算数量
			{
				name : "num"
			},
			// 预算单价，
			{
				name : 'unitPrice'
			},// 零件名称
			{
				name : 'partName'
			},// 备注
			{
				name : 'note'
			},
			// 修改时间
			{
				name : "updateTime"
			}, {
				name : "sum"
			},
			// 是否是新规的记录
			{
				name : "isNew"
			}, {
				name : "realNum"
			}, {
				name : "realUnitPrice"
			}, {
				name : "realSum"
			}]);

	// 项目详细Store
	var carwhListStore = new Ext.data.JsonStore({
				url : 'administration/getAdJCarwhManagementList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : carwhListRecord
			});

	var lineNumber = new Ext.grid.RowNumberer({
				header : "行号",
				width : 35
			});
	// 明细grid
	var carwhListGrid = new Ext.grid.EditorGridPanel({
		id : "carwhListGrid",
		height : 220,
		region : "center",
		style : "padding-top:0px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
		border : false,
		clicksToEdit : 1,
		// autoScroll : true,
		sm : new Ext.grid.RowSelectionModel({
					singleSelect : true
				}),
		tbar : [{
					text : Constants.BTN_ADD,
					iconCls : Constants.CLS_ADD,
					handler : function() {
						addCarWhList();

					}
				}, {
					text : Constants.BTN_DELETE,
					iconCls : Constants.CLS_DELETE,
					handler : function() {
						deleteCarWhList();
					}
				},  {
					text : Constants.BTN_CONFIRM,
					iconCls : Constants.CLS_OK,
					handler : function() {
						confirmCarWhList();
					}
				},  {
					text : Constants.BTN_CANCEL,
					iconCls : Constants.CLS_CANCEL,
					handler : function() {
						cancleCarWhList();
					}
				}],
		store : carwhListStore,
		columns : [lineNumber,
				// 项目
				{
			header : "维护单据号",
			sortable : true,
			width : 100,
			dataIndex : 'whId'
		},
				// 项目名称
				{
					header : "项目名称",
					sortable : true,
					width : 75,
					renderer : projectNameFormat,
					dataIndex : 'proCode'
				},
				// 零件名称
				{
					header : "零件名称<font color='red'>*</font>",
					sortable : true,
					width : 75,
					editor : new Ext.form.TextField({
								maxLength : 25
							}),
					dataIndex : 'partName'
				},
				// 单位
				{
					header : "单位",
					sortable : true,
					width : 75,
					editor : new Ext.form.ComboBox({
								store : unitStore,
								displayField : "strUnitName",
								valueField : "strUnitID",
								mode : 'local',
								lastQuery : '',
								triggerAction : 'all',
								readOnly : true
							}),
					renderer : getUnitName,
					dataIndex : 'unit'
				},
				// 数量
				{
					header : "预算数量",
					sortable : true,
					width : 75,
					align : 'right',
					renderer:renderNumber1,
					dataIndex : 'num'
				},
				// 单位价格（元）
				{
					header : "预算单价",
					sortable : true,
					width : 100,
					align : 'right',
					renderer : numberFormat,
					dataIndex : 'unitPrice'
				},
				// 金额（元）
				{
					header : "预算金额",
					sortable : true,
					width : 100,
					align : 'right',
					renderer : numberFormat,
					dataIndex : 'sum'
				},
				// 数量
				{
					header : "实际数量<font color='red'>*</font>",
					sortable : true,
					width : 75,
					align : 'right',
					renderer:renderNumber1,
					editor : new Ext.form.NumberField({
								maxValue : 9999999999.99
							}),
					dataIndex : 'realNum'
				},
				// 单位价格（元）
				{
					header : "实际单价<font color='red'>*</font>",
					sortable : true,
					width : 100,
					align : 'right',
					editor : new Ext.form.NumberField({
								maxValue : 99999999999.99
							}),
					renderer : numberFormat,
					dataIndex : 'realUnitPrice'
				},
				// 金额（元）
				{
					header : "实际金额",
					sortable : true,
					width : 100,
					align : 'right',
					renderer : numberFormat,
					dataIndex : 'realSum'
				},
				// 备注
				{
					header : "备注",
					sortable : true,
					width : 150,
					editor : txtNote,
					dataIndex : 'note'
				}],
		viewConfig : {
			forceFit : false
		},

		enableColumnMove : false
	});

	carwhListGrid.on("afteredit", function(obj) {
		var record = obj.record;
		var field = obj.field;
		if (field === "realNum" || field === "realUnitPrice") {
			if ((record.get("realNum") !== "") && (record.get("realUnitPrice"))) {
				record.set("realSum", parseFloat(record.get("realNum"))
								* parseFloat(record.get("realUnitPrice")));
			}
			if((record.get("realNum") === "") || (record.get("realUnitPrice"))===""){
						record.set("realSum","");
					}
		}
	});
	// 弹出画面
	var listWin = new Ext.Window({
				id : "win",
				height : 250,
				width : 400,
				layout : 'border',
				modal : true,
				closable : false,
				resizable : false,
				autoScroll : true,
				closeAction : 'hide',
				items : [carwhListGrid],
				buttonAlign : "center",
				title : '项目详细画面'
			});

	var view = new Ext.Viewport({
				enableTabScroll : true,
				autoScroll : true,
				layout : "fit",
				items : [carRepairApproveGrid]
			})
	/**
	 * 显示详细信息
	 */
	function showCarRepairApproveDetail() {
		if (carRepairApproveGrid.selModel.hasSelection()) {
			win.show();
			headForm.focus();
			win.setTitle("维修管理明细");
			winSetValue();
		} else {

			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG, Constants.COM_I_001);
		}

	}

	function projectDetailFormat(value, cellmeta, record, rowIndex,
			columnIndex, store) {

		var flag = true;
		var proCode = record.get("proCode");
		if (proCode === "") {
			return "<U>" + value + "</U>";
		}
		repairProjectStore.clearFilter(true);
		for (var i = 0; i < repairProjectStore.getCount(); i++) {
			if (repairProjectStore.getAt(i).get("proCode") === proCode) {
				if (repairProjectStore.getAt(i).get("haveLise") === "Y") {
					var whId = whIdTextfield.getValue();

					var showWindow = 'showWindow("' + proCode + '","' + whId
							+ '");return false;';
					return "<a href='javascript:fun(\"" + proCode + "\",\""
							+ whId + "\")'>" + value + "</a>";

				} else {
					return "<U>" + value + "</U>";
				}
			}
		}

	}
	
	/**
	 * 备注双击处理
	 */
	function cellClickHandler() {
		var record = carwhListGrid.selModel.getSelected();
		winEnter.show();
		memoText.setValue(record.get("note"));
	}
	/**
	 * 将项目编码转化为名称
	 */
	function projectNameFormat(value) {

		repairProjectStore.clearFilter(true);
		for (var i = 0; i < repairProjectStore.getCount(); i++) {
			if (repairProjectStore.getAt(i).get("proCode") === value) {
				return repairProjectStore.getAt(i).get("proName");
			}
		}
		return "";
	}

	fun = function(proCode, whId) {

		// 显示窗口
		listWin.show();
		carwhListGrid.getView().scroller.dom.scrollLeft = 0;
		projectDetails = [];
		carwhListStore.clearFilter(true);
		for (var i = 0; i < carwhListStore.getCount(); i++) {
			projectDetails.push(projectRecordCopy(carwhListStore.getAt(i)));
		}
		commonFilter();

	}

	/**
	 * 增加一条记录
	 */
	function addCarWhList() {
		if (carwhMxDetailGrid.selModel.hasSelection()) {

			var record = carwhMxDetailGrid.getSelectionModel().getSelected();
			// 新记录
			var record = new carwhListRecord({
						id : "",
						whId : whIdTextfield.getValue(),
						proCode : record.data.proCode,
						unit : "",
						num : 0,
						unitPrice : 0,
						partName : "",
						note : "",
						updateTime : "",
						sum : 0,
						realSum : "",
						realNum : "",
						realUnitPrice : "",
						isNew : "0"
					});
			carwhListStore.clearFilter(true);
			// 原数据个数
			var count = carwhListStore.getCount();
			// 插入新数据
			carwhListStore.insert(count, record);
			carwhListGrid.getView().refresh();
			commonFilter();
		}
	}
	function deleteCarWhList() {
		if (carwhListGrid.selModel.hasSelection()) {
			var record = carwhListGrid.selModel.getSelected();
			if (record.data.id !== "") {
				record.set('isNew', '1');
			} else {
				carwhListStore.remove(record);
			}
			carwhListGrid.getView().refresh();
			commonFilter();
		} else {
			// 否则弹出提示信息
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
					MessageConstants.COM_I_001);
		}

	}
	/**
	 * 增加明细
	 */
	function addDetailList() {
		repairProjectStore.clearFilter(true);
		var len = repairProjectStore.getCount();
		if (carwhMxDetailStore.getCount() > 0) {
			var detailRecord = carwhMxDetailStore.getAt(carwhMxDetailStore
					.getCount()
					- 1);
			if (detailRecord.data.countType === "total") {
				carwhMxDetailStore.remove(detailRecord);
			}
		}
		if ((carwhMxDetailStore.getCount()) < len) {
			// 新记录
			var record = new carwhMxDetailRecord({
						carwhMxId : "",
						proCode : "",
						proName : "",
						proDetail : "项目详细",
						price : 0,
						realPrice : 0,
						carwhMxUpdateTime : "",
						haveLise : "",
						carwhProId : "",
						carwhProUpdateTime : "",
						isNew : "0"
					});
			carwhMxDetailStore.clearFilter(true);
			// 原数据个数
			var count = carwhMxDetailStore.getCount();
			// 插入新数据
			carwhMxDetailStore.insert(count, record);

			carwhMxDetailStore.filterBy(function(record) {
						if (record.data.isNew === "1") {
							return false;
						} else {
							return true;
						}
					})
			addLine();
		} else {
			if (carwhMxDetailStore.getCount() > 0) {
				commDetailFilter();
				addLine();
			}
			Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
					MessageConstants.AV007_E_002);

		}
	}

	/**
	 * 删除明细
	 */
	function deleteDetailList() {

		if (carwhMxDetailGrid.selModel.hasSelection()) {
			Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG,
					MessageConstants.COM_C_002, function(button) {
						if (button == 'yes') {
							var record = carwhMxDetailGrid.selModel
									.getSelected();
							if (record.data.countType === "total") {
								return;
							}
							var detailRecord = carwhMxDetailStore
									.getAt(carwhMxDetailStore.getCount() - 1);
							if (detailRecord.data.countType === "total") {
								carwhMxDetailStore.remove(detailRecord);
								carwhMxDetailGrid.getView().refresh();
							}
							repairProjectStore.clearFilter(true);
							carwhListStore.clearFilter(true);
							carwhMxDetailStore.clearFilter(true);
							if (record.data.carwhMxId !== "") {
								var array = [];
								for (var j = 0; j < carwhListStore.getCount(); j++) {
									if ((carwhListStore.getAt(j).get("proCode") === record
											.get("proCode"))) {
										if (carwhListStore.getAt(j).get("id") !== "") {
											carwhListStore.getAt(j).set(
													"isNew", '1');
										} else {
											if (carwhListStore.getAt(j)
													.get("whId") !== "") {
												array.push(carwhListStore
														.getAt(j));
											}
										}

									}
								}
								// 新规的要删除
								for (var i = 0; i < array.length; i++) {
									carwhListStore.remove(array[i]);
								}
								record.set('isNew', "1");

								carwhMxDetailStore.filterBy(function(record) {
											if (record.data.isNew === "1") {
												return false;
											} else {
												return true;
											}
										})
								if (carwhMxDetailStore.getCount() > 0) {
									addLine();
								}
							} else {
								var proCode = record.data.proCode;
								var array = []
								for (var i = 0; i < carwhListStore.getCount(); i++) {
									if ((carwhListStore.getAt(i).get("proCode") === proCode)
											&& (carwhListStore.getAt(i)
													.get("id") === "")) {
										array.push(carwhListStore.getAt(i));
									}
								}
								for (var i = 0; i < array.length; i++) {
									carwhListStore.remove(array[i]);
								}
								carwhMxDetailStore.remove(record);
								carwhMxDetailStore.filterBy(function(record) {
											if (record.data.isNew === "1") {
												return false;
											} else {
												return true;
											}
										});
								if (carwhMxDetailStore.getCount() > 0) {
									addLine();
								}
							}
						}
					});
			

		} else {
			/*
			 * carwhMxDetailStore.filterBy(function(record) { if
			 * (record.data.isNew === "1") { return false; } else { return true; }
			 * }); addLine();
			 */
			// 否则弹出提示信息
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
					MessageConstants.COM_I_001);

		}
	}

	function cancelDetailList() {
		Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG,
				MessageConstants.COM_C_005, function(buttonobj) {
					if(buttonobj=='no'){
						return ;
					}
					var whId = whIdTextfield.getValue();
					if (whId === "") {
						headForm.getForm().reset();
						repairDateField.setValue(getCurrentDate());
						carwhMxDetailStore.removeAll();
						carwhListStore.removeAll();
						// addLine();
					} else {

						carwhMxDetailStore.load({
									params : {
										start : 0,
										limit : Constants.PAGE_SIZE
									}
								});

						carwhMxDetailCopyStore.load({
									params : {
										start : 0,
										limit : Constants.PAGE_SIZE
									}
								});
						carwhMxDetailStore.on("load", function() {
							if (carwhMxDetailStore.getCount() > 0) {
								for(var i=0;i<carwhMxDetailStore.getCount();i++){
						 	 	var record  = carwhMxDetailStore.getAt(i);
						 	 	carwhMxDetailStore.getAt(i).set("proName",projectNameFormat(carwhMxDetailStore.getAt(i).get("proCode")));
						 	 	carwhMxDetailStore.getAt(i).modified=[];
								}
								addLine();
						}
					})
						carwhMxDetailStore.clearFilter(true);
						// 项目详细数据源
						carwhListStore.baseParams = {
							whId : whIdTextfield.getValue()
						};
						carwhListStore.load({
									params : {
										start : 0,
										limit : Constants.PAGE_SIZE
									}
								});

					}
				});

	}
	function DetailCopy(record, price, isNew) {
		var record1 = new carwhMxDetailRecord({
					carwhMxId : "",
					proCode : "",
					proDetail : "项目详细",
					price : "",
					realPrice : "",
					carwhMxUpdateTime : "",
					haveLise : "",
					carwhProId : "",
					carwhProUpdateTime : "",
					isNew : "1"
				});
		record1.set("carwhMxId", record.get("carwhMxId"));
		record1.set("proCode", record.get("proCode"));
		record1.set("proDetail", "项目详细");
		record1.set("price", price);
		record1.set("carwhMxUpdateTime", record.get("carwhMxUpdateTime"));
		record1.set("haveLise", record.get("haveLise"));
		record1.set("carwhProId", record.get("carwhProId"));
		record1.set("carwhProUpdateTime", record.get("carwhProUpdateTime"));
		record1.set("isNew", isNew);

		return record1;

	}

	function commonFilter() {
		carwhListStore.clearFilter(true);
		carwhListStore.filterBy(function(record) {
					if (carwhMxDetailGrid.selModel.hasSelection()) {
						var selectRecord = carwhMxDetailGrid
								.getSelectionModel().getSelected();
						if (selectRecord.data.carwhMxId === "") {
							if ((record.data.proCode === selectRecord.data.proCode)
									&& (record.data.isNew === "0")) {
								return true;
							} else {
								return false;
							}
						} else {
							if ((record.data.proCode === selectRecord.data.proCode)
									&& (record.data.isNew === "0")) {
								return true;
							} else {
								return false;
							}
						}
					}

				})
	}
	/**
	 * 点击项目确认按钮的处理
	 */
	function confirmCarWhList() {
		var record;
		for (var i = 0; i < carwhListStore.getCount(); i++) {
			record = carwhListStore.getAt(i);
			
			
			if (record.get("partName") === "") {
				Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, String.format(
								MessageConstants.COM_E_002, '零件名称'));
				return;
			}
			if (record.get("realNum") === "") {
				// 
				Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, String.format(
								MessageConstants.COM_E_002, '实际数量'));
				return;
			}
			if (parseFloat(record.get("realNum")) <= 0) {
				Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, String.format(
								MessageConstants.COM_E_013, '实际数量'));
				return;
			}
			if (record.get("realUnitPrice") === "") {
				Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, String.format(
								MessageConstants.COM_E_002, '实际单价'));
				return;
			}

			
			if (parseFloat(record.get("realUnitPrice")) <= 0) {
				Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, String.format(
								MessageConstants.COM_E_013, '实际单价'));
				return;
			}
		}
		var sum = 0;
		for (var i = 0; i < carwhListStore.getCount(); i++) {
			sum = carwhListStore.getAt(i).get("realSum") + sum;
		}

		if (carwhMxDetailGrid.selModel.hasSelection()) {
			var selectRecord = carwhMxDetailGrid.getSelectionModel()
					.getSelected();
			selectRecord.set("realPrice", sum);
		}
		var detailRecord = carwhMxDetailStore.getAt(carwhMxDetailStore
				.getCount()
				- 1);
		if (detailRecord.data.countType === "total") {
			carwhMxDetailStore.remove(detailRecord);
		}
		commDetailFilter();
		addLine();
		listWin.hide();

	}

	/**
	 * 项目点击取消按钮的处理
	 */
	function cancleCarWhList() {
		Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG,
				MessageConstants.COM_C_005, function(buttonobj) {
					if (buttonobj == "yes") {
						carwhListStore.clearFilter(true);
						carwhListStore.removeAll();
						for (var i = 0; i < projectDetails.length; i++) {
							carwhListStore.add(projectDetails[i]);
						}
						commonFilter();
						var sum = 0;

						for (var i = 0; i < carwhListStore.getCount(); i++) {
							sum = carwhListStore.getAt(i).get("sum") + sum;
						}
						if (carwhMxDetailGrid.selModel.hasSelection()) {

							var selectRecord = carwhMxDetailGrid
									.getSelectionModel().getSelected();
							selectRecord.set("price", sum);

						}
						carwhListStore.clearFilter(true);

						var detailRecord = carwhMxDetailStore
								.getAt(carwhMxDetailStore.getCount() - 1);
						if (detailRecord.data.countType === "total") {
							carwhMxDetailStore.remove(detailRecord);
						}
						commDetailFilter();
						addLine();
						listWin.hide();

					}
				})
	}

	function commDetailFilter() {
		carwhMxDetailStore.clearFilter();
		carwhMxDetailStore.filterBy(function(record) {
					if (record.data.isNew === "1") {
						return false;
					} else {
						return true;
					}
				})
	}

	function renderNumber(v, argDecimal) {
		if (v) {
			if (typeof argDecimal != 'number') {
				argDecimal = 2;
			}
			v = Number(v).toFixed(argDecimal);
			var t = '';
			v = String(v);
			while ((t = v.replace(/^(-?[0-9]+)([0-9]{3}.*)$/, '$1,$2')) !== v)
				v = t;

			return v + "元";
		} else
			return "0.00元";
	}
	
	function renderNumber1(v, argDecimal) {
		if(v===0)return "0.00";
		if (v) {
			if (typeof argDecimal != 'number') {
				argDecimal = 2;
			}
			v = Number(v).toFixed(argDecimal);
			var t = '';
			v = String(v);
			while ((t = v.replace(/^(-?[0-9]+)([0-9]{3}.*)$/, '$1,$2')) !== v)
				v = t;

			return v ;
		} else
			return "";
	}

	/**
	 * 查询结束时插入统计行
	 */
	function addLine() {
		commDetailFilter();
		// 统计行
		var record = new carwhMxDetailRecord({
					// isNew:"1",
					countType : "total"
				});
		// 原数据个数
		var count = carwhMxDetailStore.getCount();
		carwhMxDetailStore.insert(count, record);
		carwhMxDetailGrid.getView().refresh();
	};

	/**
	 * 获取当前时间
	 */
	function getCurrentDate() {
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
	 * 修改的时候弹出画面的设置
	 */
	function winSetValue() {
		var record = carRepairApproveGrid.getSelectionModel().getSelected();
		headForm.getForm().loadRecord(record);
		if (record.data.useStatus === "Y") {
			isContiueUse.setValue(true);
		} else {
			isContiueUse.setValue(false);
		}
		repairDateField.setValue(renderDate(record.data.repairDate));
		// 单位是否存在情况的设置
		var flag =false;
		for(var i = 0;i<cpCodeStore.getCount();i++){
			if(record.data.cpCode===cpCodeStore.getAt(i).data.cpCode){
				flag =true;
				break;	
			}
		}
		if(flag){
			cpCodeCombo.setValue(record.data.cpCode);
		}else{
			cpCodeCombo.setValue("");
		}
		dsMatieria.load({
					params : {
						whId : whIdTextfield.getValue()
					}
				});
		// 清除附件内容
		var domAppend = tfAppend.el.dom;
		var parent = domAppend.parentNode;

		// 保存
		var domForSave = domAppend.cloneNode();
		// 移除附件控件
		parent.removeChild(domAppend);
		// 再追加控件
		parent.appendChild(domForSave);
		// 应用该控件
		tfAppend.applyToMarkup(domForSave);
		
		carwhMxDetailStore.load({
					params : {
						start : 0,
						limit : Constants.PAGE_SIZE
					}
				});

		carwhMxDetailCopyStore.load({
					params : {
						start : 0,
						limit : Constants.PAGE_SIZE
					}
				});
		carwhMxDetailStore.on("load", function() {
					if (carwhMxDetailStore.getCount() > 0) {
						for(var i=0;i<carwhMxDetailStore.getCount();i++){
						  var record  = carwhMxDetailStore.getAt(i);
						  carwhMxDetailStore.getAt(i).set("proName",projectNameFormat(carwhMxDetailStore.getAt(i).get("proCode")));
						  carwhMxDetailStore.getAt(i).modified=[];
						}
						addLine();
					}
				})
		carwhMxDetailStore.clearFilter(true);
		// 项目详细数据源
		carwhListStore.baseParams = {
			whId : whIdTextfield.getValue()
		};
		carwhListStore.load({
					params : {
						start : 0,
						limit : Constants.PAGE_SIZE
					}
				});
	}

	/**
	 * 获取单位对应的名字
	 */
	function getUnitName(value) {
		for (var i = 0; i < unitStore.getCount(); i++) {
			if (value === unitStore.getAt(i).data.strUnitID) {
				return unitStore.getAt(i).data.strUnitName;
			}
		}
		return "";
	}
	function projectRecordCopy(record) {
		var recordCopy = new carwhListRecord({
					id : "",
					whId : "",
					proCode : "",
					unit : "",
					num : "",
					unitPrice : "",
					partName : "",
					note : "",
					updateTime : "",
					sum : "",
					realNum : "",
					realUnitPrice : "",
					realSum : "",
					isNew : ""
				});

		recordCopy.set("id", record.get("id"));
		recordCopy.set("whId", record.get("whId"));
		recordCopy.set("proCode", record.get("proCode"));
		recordCopy.set("unit", record.get("unit"));
		recordCopy.set("num", record.get("num"));
		recordCopy.set("unitPrice", record.get("unitPrice"));
		recordCopy.set("partName", record.get("partName"));
		recordCopy.set("note", record.get("note"));
		recordCopy.set("updateTime", record.get("updateTime"));
		recordCopy.set("sum", record.get("sum"));
		recordCopy.set("realSum", record.get("realSum"));
		recordCopy.set("realNum", record.get("realNum"));
		recordCopy.set("realUnitPrice", record.get("realUnitPrice"));
		recordCopy.set("isNew", record.get("isNew"));
		recordCopy.modified = [];
		return recordCopy;
	}

	/**
	 * 附件名渲染
	 */
	function fileName(value, cellmeta, record) {
		if (value != "") {
			var id = record.get("id");
			var download = 'download("' + id + '");return false;';
			return "<a href='#' onclick='" + download + "'>" + value + "</a>";
		} else {
			return "";
		}
	}

	function dele(value) {
		if (value != "") {
			return "<a href='#'  onclick= 'btnDeleteAppendHandler();return false;'><img src='comm/ext/tool/dialog_close_btn.gif'></a>";
		} else {
			return "";
		}
	}

	/**
	 * 上传附件处理
	 */
	function btnUploadAppendHandler() {
		if (!checkFilePath(tfAppend.getValue())) {
				Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
					MessageConstants.COM_E_024);
			return;
		}
		fpnlMatieria.getForm().submit({
			url : 'administration/uploadAdJCarwhInvoiceFile.action',
			method : Constants.POST,
			params : {
				whId : whIdTextfield.getValue()
			},
			success : function(form, action) {
				var o = eval("(" + action.response.responseText + ")");
				// 排他
				if (o.msg == Constants.DATA_USING) {
					Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
							MessageConstants.COM_I_002);
					return;
				}
				// IO错误
				if (o.msg == Constants.IO_FAILURE) {
					Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
							MessageConstants.COM_E_022);
					return;
				}
				// 数据格式化错误
				if (o.msg == Constants.DATE_FAILURE) {
					Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
							MessageConstants.COM_E_023);
					return;
				} 
				// 文件不存在
				if (o.msg == Constants.FILE_NOT_EXIST) {
					Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
							MessageConstants.COM_E_024);
					return;
				}	
					dsMatieria.load({
								params : {
									whId : whIdTextfield.getValue()
								}
							})
					gridMatieria.getView().refresh();
				
			},
			failure : function() {
				Ext.Msg.alert(Constants.ERROR, MessageConstants.DEL_ERROR);
			}
		})
	}

	/**
	 * 删除附件处理
	 */
	btnDeleteAppendHandler = function() {
		var record = gridMatieria.selModel.getSelected();
		// 是否选中一行
		if (!gridMatieria.selModel.hasSelection()) {
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
					MessageConstants.COM_I_001);
			return;
		} else {
			Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG,
					MessageConstants.COM_C_002, function(button) {
						// 是否删除
						if (button == 'yes') {

							// 数据库中删除
							Ext.Ajax.request({
								url : 'administration/deleteAdJCarwhInvoiceFile.action',
								method : Constants.POST,
								params : {
									id : record.get('id'),
									updateTime : record.get('updateTime')
								},
								success : function(result, response) {
									var o = eval("(" + result.responseText
											+ ")");
									if (o.msg == Constants.DATA_USING) {
										// 排他处理
										Ext.Msg
												.alert(
														MessageConstants.SYS_ERROR_MSG,
														MessageConstants.COM_I_002);
										return;
									}
									// 删除成功
									Ext.Msg.alert(
											MessageConstants.SYS_REMIND_MSG,
											MessageConstants.COM_I_005,
											function() {
												// 画面上删除一行
												dsMatieria.remove(record);
												dsMatieria.totalLength = dsMatieria
														.getTotalCount()
														- 1;
												gridMatieria.getView()
														.refresh();
											});
								}
							})
						}
					})
		}
	}

	function saveDetailList() {
		var COM_E_003_RepairDate = "维修日期不能为空，请选择"
		if(repairDateField.getValue()===""){
			Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
						COM_E_003_RepairDate);
				return;
		}	
		for (var i = 0; i < carwhMxDetailStore.getCount()-1; i++) {
			if (carwhMxDetailStore.getAt(i).data.proCode === "") {
				Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, String.format(
								MessageConstants.COM_E_003, '项目名称'));
				return;
			}

		}
		Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG,
				MessageConstants.COM_C_001, function(buttonobj) {
					if (buttonobj == 'yes') {
						var detailList = [];
						var detailProjectList = [];
						sumHidden.setValue(sumNumfield.getValue());
						realSumHidden.setValue(realSumNumfield.getValue());
						driveMileHidden.setValue(driveMileTextfield.getValue());
						carwhMxDetailStore.clearFilter(true);
						carwhListStore.clearFilter(true);

						for (var i = 0; i < carwhMxDetailStore.getCount(); i++) {
							detailList.push(carwhMxDetailStore.getAt(i).data);
						}
						for (var i = 0; i < carwhListStore.getCount(); i++) {
							detailProjectList
									.push(carwhListStore.getAt(i).data);
						}
						headForm.getForm().submit({
							method : Constants.POST,
							url : "administration/updateCarwhManagementList.action",
							params : {
								isContiueUse : isContiueUse.checked ? "Y" : "N",
								flag : isSave ? "1" : "0",
								cpCode : cpCodeCombo.getValue(),
								carwh : carRepairApproveGrid.selModel
										.hasSelection() ? Ext.util.JSON
										.encode(carRepairApproveGrid.selModel
												.getSelected().data) : "",
								detailList : Ext.util.JSON.encode(detailList),
								detailProjectList : Ext.util.JSON
										.encode(detailProjectList)

							},

							success : function(form, action) {
								var result = eval("("
										+ action.response.responseText + ")");

								if (result.msg == Constants.DATA_USING) {
									// 排他处理
									Ext.Msg.alert(
											MessageConstants.SYS_ERROR_MSG,
											MessageConstants.COM_I_002);
									return;
								}
								if (result.msg == Constants.SQL_FAILURE) {
									Ext.Msg.alert(
											MessageConstants.SYS_ERROR_MSG,
											MessageConstants.COM_E_014);
									return;
								}
								if (result.msg == Constants.DATE_FAILURE) {
									Ext.Msg.alert(
											MessageConstants.SYS_ERROR_MSG,
											MessageConstants.COM_E_023);
									return;
								}
								// 保存成功
								Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
										MessageConstants.COM_I_004, function() {
											win.hide();
											carRepairApproveStore.load({
														params : {
															start : 0,
															limit : Constants.PAGE_SIZE
														}
													})
										});

							},
							failure : function() {
							}
						});

					}
				});

	}

})
