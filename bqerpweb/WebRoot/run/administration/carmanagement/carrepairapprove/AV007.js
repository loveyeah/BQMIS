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

	var projectDetails = [];
	var deleteDetails = [];
	// ↓↓*******************车辆维修申请**************************************

	// 车辆维修申请grid的store
	var carRepairApproveStore = new Ext.data.JsonStore({
				url : 'administration/getCarRepairApproveQueryList.action',
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
						},//  修改时间
						{
							name : 'updateTime'
						}, {
							name : 'repairStatus'
						},{
							name :'driveFileId'
						},{
							name :'driveFileUpdateTime'
						},{
							name :'useStatus'
						}]
			});

	carRepairApproveStore.load({
				params : {
					start : 0,
					limit : Constants.PAGE_SIZE
				}
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
							text : Constants.BTN_ADD,
							iconCls : Constants.CLS_ADD,
							handler : function() {
								AddCarRepairApproveDetail();
							}
						}, {
							text : Constants.BTN_UPDATE,
							iconCls : Constants.CLS_UPDATE,
							handler : function() {
								showCarRepairApproveDetail();
							}
						}, {
							text : Constants.BTN_DELETE,
							iconCls : Constants.CLS_DELETE,
							handler : function() {
								deleteCarRepairApproverDetail();
							}
						}, {
							text : Constants.BTN_REPOET,
							iconCls : Constants.CLS_REPOET,
							handler : function() {
								reportCarRepairApproverDetail();
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
				fieldLabel : '车牌号<font color="red">*</font>',
				readOnly : true,
				anchor : "100%",
				name : 'bean.carNo'
			});
	carNoTextfield.onClick(popupSelect);

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
				//xtype : "textfield",
				fieldLabel : '维修里程',
				maxValue:9999999999999.99,
				appendChar : '公里',
				style:'text-align:right',
				padding : 2,
				allowNegative : false,
				readOnly : false,
				anchor : "100%",
				name : ''
			});
	var driveMileHidden = new Ext.form.Hidden({
			id :"driveMileHidden",
			name:"bean.driveMile"
		});			

	// 经办人
	var managerStore = new Ext.data.JsonStore({
				root : 'list',
				url : "administration/getDriverInfoList.action",
				fields : ['driverCode', 'driverName']
			})
	managerStore.load();
	var managerCombo = new Ext.form.ComboBox({
				store : managerStore,
				displayField : "driverName",
				valueField : "driverCode",
				mode : 'local',
				fieldLabel : "经办人",
				triggerAction : 'all',
				anchor : '98%',
				readOnly : true
			})

	// 维修单位
	var cpCodeStore = new Ext.data.JsonStore({
				root : 'list',
				url : "administration/getAdCCarmendWhCpCodeList.action",
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
				maxValue :99999999999.99,
				padding : 2,
				id : 'sum',
				hiddenName : 'sum',
				readOnly : true

			})
	var sumHidden = new Ext.form.Hidden({
				id : "hiddenSum",
				name : 'bean.sum'
			})

	// 支出事由
	var reasonTextArea = new Ext.form.TextArea({
				id : "reason",
				fieldLabel : '支出事由',
				height : 40,
				maxLength : 30,
				readOnly : false,
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

	var headForm = new Ext.FormPanel({
				border : false,
				labelAlign : 'right',
				style : {
					'border' : 0
				},
				frame : true,
				// region : "north",
				layout : 'form',
				labelWidth : 90,
				width:468,
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
										columnWidth : 0.24,
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
										columnWidth : 0.49,
										layout : "form",
										border : false,
										height : 30,
										labelAlign : "right",
										items : [managerCombo]
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
						}, sumHidden,driveMileHidden]

			});
	// 增加明细button
	var addDetailBtn = new Ext.Button({
				text : "增加明细",
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
				url : "administration/getRepairProjectList.action",
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
			},
			{
				name :"proName"
			},
			// 项目详细
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
			},
			// 是否是新规的记录
			{
				name : "isNew"
			}]);

	// 明细Store
	var carwhMxDetailStore = new Ext.data.JsonStore({
				url : 'administration/getCarwhMxDetailList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : carwhMxDetailRecord
			});
	var carwhMxDetailCopyStore = new Ext.data.JsonStore({
				url : 'administration/getCarwhMxDetailList.action',
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
		// region : "center",
		style : "padding-top:0px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
		border : false,
		width :468,
		clicksToEdit : 1,
		// autoScroll : true,
		sm : new Ext.grid.RowSelectionModel({
					singleSelect : true
				}),
		tbar : [addDetailBtn, deleteDetailBtn, cancelDetailBtn],
		store : carwhMxDetailStore,
		columns : [new Ext.grid.RowNumberer({
							header : "行号",
							width : 35
						}),
				// 项目
				{
					header : "项目<font color='red'>*</font>",
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
							
					// renderer : projectNameFormat,
					dataIndex : 'proName'
				},
				// 项目详细
				{
					header : "项目详细",
					sortable : true,
					width : 150,
					renderer : projectDetailFormat,
					align :"center",
					dataIndex : 'proDetail'
				},
				// 费用预算（元）
				{
					header : "费用预算",
					sortable : true,
					width : 150,
					align : 'right',
					editor : new Ext.form.NumberField({maxValue:99999999999.99}),
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
								totalSum += store.getAt(i).get('price') * 1;
							}
							sumNumfield.setValue(totalSum);
							return renderNumber(totalSum);
						}
					},
					dataIndex : 'price'
				}],

		viewConfig : {
			forceFit : false
		},

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
		if (field === "price") {

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
						record.set('price', 0);
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
				record.set("price", 0);
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
		if (field === "price") {
			if(record.data.price===""){
				record.set("price",0);
			}
			commDetailFilter();

			var sum = 0;
			for (var i = 0; i < carwhMxDetailStore.getCount(); i++) {
				sum = parseFloat(carwhMxDetailStore.getAt(i).get("price"))
						+ sum;
			}
			sumNumfield.setValue(sum);

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
				items : [headForm, carwhMxDetailGrid],
				buttonAlign : "center",
				title : '修改维修申请单',
				buttons : [{
							text : Constants.BTN_SAVE,
							iconCls : Constants.CLS_SAVE,
							handler : function() {
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
											if(buttonobj == 'yes'){		
												win.hide();
											}
										});
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
			}]);

	// 项目详细Store
	var carwhListStore = new Ext.data.JsonStore({
				url : 'administration/getAdJCarwhList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : carwhListRecord
			});
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
				}, {
					text : Constants.BTN_CONFIRM,
					iconCls : Constants.CLS_OK,
					handler : function() {
						confirmCarWhList();
					}
				}, {
					text : Constants.BTN_CANCEL,
					iconCls : Constants.CLS_CANCEL,
					handler : function() {
						cancleCarWhList();
					}
				}],
		store : carwhListStore,
		columns : [new Ext.grid.RowNumberer({
							header : "行号",
							width : 35
						}),
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
					editor : new Ext.form.TextField({maxLength:25}),
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
					renderer:getUnitName,	
					dataIndex : 'unit'
				},
				// 数量
				{
					header : "预算数量<font color='red'>*</font>",
					sortable : true,
					width : 75,
					align : 'right',
					renderer:renderNumber1,
					editor : new Ext.form.NumberField({maxValue:9999999999.99}),

					dataIndex : 'num'
				},
				// 单位价格（元）
				{
					header : "预算单价<font color='red'>*</font>",
					sortable : true,
					width : 100,
					align : 'right',
					editor : new Ext.form.NumberField({maxValue:99999999999.99}),
					renderer : numberFormat,
					dataIndex : 'unitPrice'
				},
				// 金额（元）
				{
					header : "金额",
					sortable : true,
					width : 75,
					align : 'right',
					renderer : numberFormat,
					dataIndex : 'sum'
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
				if (field === "num" || field === "unitPrice") {
					if ((record.get("num") !== "") && (record.get("unitPrice")!=="")) {
						record.set("sum", parseFloat(record.get("num"))
										* parseFloat(record.get("unitPrice")));
					}
					if((record.get("num") === "") || (record.get("unitPrice"))===""){
						record.set("sum","");
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
				closable:false,
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
			win.setTitle("修改维修申请");
			winSetValue();
		} else {

			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG, Constants.COM_I_001);
		}

	}

	function deleteCarRepairApproverDetail() {
		if (carRepairApproveGrid.selModel.hasSelection()) {
			Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG,
					MessageConstants.COM_C_002, function(buttonobj) {
						if (buttonobj == 'yes') {
							Ext.Ajax.request({
								method : 'POST',
								url : 'administration/deleteCarRepairApproverDetail.action',
								params : {
									data : Ext.util.JSON
											.encode(carRepairApproveGrid.selModel
													.getSelected().data)

								},
								success : function(action) {

									var result = eval('(' + action.responseText
											+ ')');
									if (result.msg == Constants.DATA_USING) {
										// 排他处理
										Ext.Msg
												.alert(
														MessageConstants.SYS_ERROR_MSG,
														MessageConstants.COM_I_002);
										return;
									}
									if (result.msg == Constants.SQL_FAILURE) {
										Ext.Msg
												.alert(
														MessageConstants.SYS_ERROR_MSG,
														MessageConstants.COM_E_014);
										return;
									}
									if (result.msg == Constants.DATE_FAILURE) {
										Ext.Msg
												.alert(
														MessageConstants.SYS_ERROR_MSG,
														MessageConstants.COM_E_023);
										return;
									}
									// 保存成功
									Ext.Msg.alert(
											MessageConstants.SYS_REMIND_MSG,
											MessageConstants.COM_I_005,function(){
									carRepairApproveStore.load({
												params : {
													start : 0,
													limit : Constants.PAGE_SIZE
												}
											});
											});


								}
							});

						}
					});

		} else {
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG, Constants.COM_I_001);
		}
	}

	/**
	 * 上报处理
	 */
	function reportCarRepairApproverDetail() {
		if (carRepairApproveGrid.selModel.hasSelection()) {
			Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG,
					MessageConstants.COM_C_006, function(buttonobj) {
						if (buttonobj == 'yes') {
							Ext.Ajax.request({
								method : 'POST',
								url : 'administration/reportCarRepairApproverDetail.action',
								params : {
									data : Ext.util.JSON
											.encode(carRepairApproveGrid.selModel
													.getSelected().data)
								},
								success : function(action) {
									var result = eval('(' + action.responseText
											+ ')');
									if (result.msg == Constants.DATA_USING) {
										// 排他处理
										Ext.Msg
												.alert(
														MessageConstants.SYS_ERROR_MSG,
														MessageConstants.COM_I_002);
										return;
									}
									if (result.msg == Constants.SQL_FAILURE) {
										Ext.Msg
												.alert(
														MessageConstants.SYS_ERROR_MSG,
														MessageConstants.COM_E_014);
										return;
									}
									if (result.msg == Constants.DATE_FAILURE) {
										Ext.Msg
												.alert(
														MessageConstants.SYS_ERROR_MSG,
														MessageConstants.COM_E_023);
										return;
									}
									// 上报成功
									Ext.Msg.alert(
											MessageConstants.SYS_REMIND_MSG,
											MessageConstants.COM_I_007,function(){
												carRepairApproveStore.load({
												params : {
													start : 0,
													limit : Constants.PAGE_SIZE
												}
											});
											});

									
								}
							});
						}
					});

		} else {
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG, Constants.COM_I_001);
		}

	}

	/**
	 * 新增按钮的处理时间
	 */
	function AddCarRepairApproveDetail() {
		win.show();
		headForm.focus();
		win.setTitle("新增维修申请");
		headForm.getForm().reset();
		isContiueUse.setValue(false);
		repairDateField.setValue(getCurrentDate());
		carwhMxDetailStore.removeAll();
		carwhListStore.removeAll();
		// addLine();

	}

	function popupSelect() {
		this.blur();
		var args = 0;
		// 弹出车辆选择共通画面
		var object = window
				.showModalDialog(
						'../../../../comm/jsp/administration/selectCar.jsp',
						null,
						'dialogWidth=400px;dialogHeight=320px;center=yes;help=no;resizable=no;status=no;');
		if (object) {

			// 返回值
			// 车牌号
			if (typeof(object.carNo) != "undefined") {
				carNoTextfield.setValue(object.carNo);
			} else {
				carNoTextfield.reset();
			}
			// 车名
			if (typeof(object.carName) != "undefined") {
				carNameTextfield.setValue(object.carName);
			} else {
				carNameTextfield.reset();
			}
			// 维修里程
			if (typeof(object.runMile) != "undefined") {
				driveMileTextfield.setValue(object.runMile);
			} else {
				driveMileTextfield.reset();
			}
			// 经办人
			if (typeof(object.driver) != "undefined") {
				managerCombo.setValue(object.driver);
			}else{
				managerCombo.setValue("");
			}

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
						num : "",
						unitPrice : "",
						partName : "",
						note : "",
						updateTime : "",
						sum : "",
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
		if(carwhMxDetailStore.getCount()>0){
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
						proName :"",
						proDetail : "项目详细",
						price : 0,
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
		
		} else {/*
			var detailRecord = carwhMxDetailStore.getAt(carwhMxDetailStore
				.getCount()
				- 1);
		if (detailRecord.data.countType === "total") {
			carwhMxDetailStore.remove(detailRecord);
		}
			carwhMxDetailStore.filterBy(function(record) {
							if (record.data.isNew === "1") {
								return false;
							} else {
								return true;
							}
						});
				if (carwhMxDetailStore.getCount() > 0) {
					addLine();
				}*/
			// 否则弹出提示信息
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
					MessageConstants.COM_I_001);
				
					

		}
	}

	function cancelDetailList() {
		Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG, MessageConstants.COM_C_005,
				function(buttonobj) {
		if(buttonobj=='no'){
			return ;
		}
		var whId = whIdTextfield.getValue();
		if (whId === "") {

//			headForm.getForm().reset();
//			repairDateField.setValue(getCurrentDate());
			carwhMxDetailStore.removeAll();
			carwhListStore.removeAll();
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
			proCode : '',
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
					proName : "",
					proDetail : "项目详细",
					price : "",
					carwhMxUpdateTime : "",
					haveLise : "",
					carwhProId : "",
					carwhProUpdateTime : "",
					isNew : "1"
				});
		record1.set("carwhMxId", record.get("carwhMxId"));
		record1.set("proCode", record.get("proCode"));
		record1.set("proName", record.get("proName"));
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
		var COM_E_002_NUM = "数量不能为空，请输入。";
		var COM_E_002_UNITPRICE = "单位价格不能为空，请输入。";
		var COM_E_002_PARTNAME = "零件名称不能为空，请输入。";
		for (var i = 0; i < carwhListStore.getCount(); i++) {
			record = carwhListStore.getAt(i);
			
			if (record.get("partName") === "") {
				Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
						String.format(
							MessageConstants.COM_E_002, '零件名称'));
				return;
			}
			if (record.get("num") === "") {
				// 
				Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, String.format(
							MessageConstants.COM_E_002, '预算数量'));
				return;
			}
				
			if (parseFloat(record.get("num")) <= 0) {
				Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, String.format(
							MessageConstants.COM_E_013, '预算数量'));
				return;
			}
			if (record.get("unitPrice") === "") {
				Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
						String.format(
							MessageConstants.COM_E_002, '预算单价'));
				return;
			}

		
			if (parseFloat(record.get("unitPrice")) <= 0) {
				Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, String.format(
							MessageConstants.COM_E_013, '预算单价'));
				return;
			}
		}
		var sum = 0;
		for (var i = 0; i < carwhListStore.getCount(); i++) {
			sum = carwhListStore.getAt(i).get("sum") + sum;

		}

		if (carwhMxDetailGrid.selModel.hasSelection()) {

			var selectRecord = carwhMxDetailGrid.getSelectionModel()
					.getSelected();
			selectRecord.set("price", sum);

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
		Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG, MessageConstants.COM_C_005,
				function(buttonobj) {
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
					//        	isNew:"1",
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
		// 经办人是否删除情况的设值
		var flag =false;
		for(var i = 0;i<managerStore.getCount();i++){
			if(record.data.manager===managerStore.getAt(i).data.driverCode){
				flag =true;
				break;	
			}
			
		}
		if(flag){
		managerCombo.setValue(record.data.manager);
		}else{
			managerCombo.setValue("");
		}
		sumNumfield.setValue("");
		driveMileTextfield.setValue(record.data.driveMile);
		repairDateField.setValue(renderDate(record.data.repairDate));
		// 单位是否存在情况的设置
		flag =false;
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
			proCode : '',
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
	function getUnitName(value){
		for(var i =0;i<unitStore.getCount();i++){
			if(value===unitStore.getAt(i).data.strUnitID){
				return unitStore.getAt(i).data.strUnitName;
			}
		}
		return "";
	}
	
	
	/**
	 * 备注双击处理
	 */
	function cellClickHandler() {
		var record = carwhListGrid.selModel.getSelected();
		winEnter.show();
		memoText.setValue(record.get("note"));
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
		recordCopy.set("isNew", record.get("isNew"));
		recordCopy.modified = [];
		return recordCopy;
	}
	function saveDetailList() {
		var COM_E_003_CARNO ="车牌号不能为空，请选择。"
		if(carNoTextfield.getValue()===""){
			Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
						COM_E_003_CARNO);
				return;
		}
		var COM_E_003_RepairDate = "维修日期不能为空，请选择"
		if(repairDateField.getValue()===""){
			Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
						COM_E_003_RepairDate);
				return;
		}
		var COM_E_003_PROCODE ="项目名称不能为空，请选择。"
		for(var i=0;i<carwhMxDetailStore.getCount()-1;i++){
			if(carwhMxDetailStore.getAt(i).data.proCode===""){
				Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
						COM_E_003_PROCODE);
				return;
			}
			
			
		}
		Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG,
				MessageConstants.COM_C_001, function(buttonobj) {
					if (buttonobj == 'yes') {

						var myurl = 'administration/updateCarwhList.action';
						var detailList = [];
						var detailProjectList = [];
						sumHidden.setValue(sumNumfield.getValue());
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
							url : "administration/updateCarwhList.action",
							params : {

								isContiueUse : isContiueUse.checked ? "Y" : "N",
								manager : managerCombo.getValue(),
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
										MessageConstants.COM_I_004,function(){
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
