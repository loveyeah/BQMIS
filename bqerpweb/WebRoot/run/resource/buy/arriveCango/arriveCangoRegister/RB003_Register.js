Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Constants.PAGE_SIZE = 18;

// ----------------add-----------------------
Ext.override(Ext.grid.GridView, {
	doRender : function(cs, rs, ds, startRow, colCount, stripe) {
		var ts = this.templates, ct = ts.cell, rt = ts.row, last = colCount - 1;
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
				// 判断是否是统计行
				if (r.data["materialNo"] == null
						|| r.data["materialNo"] == "undefined") {
					// 替换掉其中的背景颜色
					p.style = c.style.replace(/background\s*:\s*[^;]*;/, '');
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
// ------------------------------------------
function numberFormat(v) {
	// modify by fyyang 091109 数量改为小数点后4位
	// value = String(value);
	// if (value == null || value == "null") {
	// value = "0";
	// }
	// // 整数部分
	// var whole = value;
	// // 小数部分
	// var sub = ".00";
	// // 如果有小数
	// if (value.indexOf(".") > 0) {
	// whole = value.substring(0, value.indexOf("."));
	// sub = value.substring(value.indexOf("."), value.length);
	// sub = sub + "00";
	// if (sub.length > 3) {
	// sub = sub.substring(0, 3);
	// }
	// }
	// var r = /(\d+)(\d{3})/;
	// while (r.test(whole)) {
	// whole = whole.replace(r, '$1' + ',' + '$2');
	// }
	// v = whole + sub;
	// if (whole == null || whole == "null" || whole == "") {
	// v = "0.00";
	// }
	// return v;
	if (v == null || v == "") {
		return "0.0000";
	}
	v = (Math.round((v - 0) * 10000)) / 10000;
	v = (v == Math.floor(v)) ? v + ".0000" : ((v * 10 == Math.floor(v * 10))
			? v + "000"
			: ((v * 100 == Math.floor(v * 100)) ? v + "00" : ((v * 1000 == Math
					.floor(v * 1000)) ? v + "0" : v)));
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
// 把数量用逗号分隔
function divide(value) {
	if (value == null) {
		return "";
	}
	var svalue = value + "";
	var decimal = "";
	var negative = false;
	var tempV = "";
	// 如果有小数
	if (svalue.indexOf(".") > 0) {
		decimal = svalue.substring(svalue.indexOf("."), svalue.length);
	}
	// 如果是负数
	if (svalue.indexOf("-") >= 0) {
		negative = true;
		svalue = svalue.substring(1, svalue.length);
	}
	tempV = svalue.substring(0, svalue.length - decimal.length);
	svalue = "";
	while (Div(tempV, 1000) > 0) {
		var temp = Div(tempV, 1000);
		var oddment = tempV - temp * 1000;
		var soddment = "";
		tempV = Div(tempV, 1000);
		soddment += (0 == Div(oddment, 100)) ? "0" : Div(oddment, 100);
		oddment -= Div(oddment, 100) * 100;
		soddment += (0 == Div(oddment, 10)) ? "0" : Div(oddment, 10);
		oddment -= Div(oddment, 10) * 10;
		soddment += (0 == Div(oddment, 1)) ? "0" : Div(oddment, 1);
		oddment -= Div(oddment, 1) * 1;
		svalue = soddment + "," + svalue;
	}
	svalue = tempV + "," + svalue;
	svalue = svalue.substring(0, svalue.length - 1);
	svalue += decimal;
	if (true == negative) {
		svalue = "-" + svalue;
	}
	return svalue;
}
// 整除
function Div(exp1, exp2) {
	var n1 = Math.round(exp1); // 四舍五入
	var n2 = Math.round(exp2); // 四舍五入

	var rslt = n1 / n2; // 除

	if (rslt >= 0) {
		rslt = Math.floor(rslt); // 返回值为小于等于其数值参数的最大整数值。
	} else {
		rslt = Math.ceil(rslt); // 返回值为大于等于其数字参数的最小整数。
	}

	return rslt;
}
// / 序列化为JSON字符串,不包括函数
Object.prototype.toJSONString = function() {
	var str = '';
	var obj = this;

	if (obj instanceof Array) {
		// 数组
		str = '[';
		for (var i = 0; i < obj.length; i++) {
			str += obj[i].toJSONString() + ',';
		}
		if (obj.length > 0) {
			// 去除最后的逗号
			str = str.slice(0, -1);
		}
		str += ']';
		return str;
	}

	str = '{';
	var sub = null;
	for (var prop in obj) {
		sub = obj[prop];
		if (sub == null || sub == undefined) {
			// 为NULL
			str += '"' + prop + '":null,';
		} else if (typeof sub == 'object') {
			if (sub instanceof Date) {
				// 转换时间格式
				str += '"' + prop + '":"' + renderDate(sub) + '",';
			} else {
				str += '"' + prop + '":' + sub.toJSONString() + ',';
			}
		} else if (typeof sub == 'number' || typeof sub == 'boolean') {
			// 布尔型或者数字
			str += '"' + prop + '":' + sub + ',';
		} else if (typeof sub != 'function') {
			str += '"' + prop + '":"' + sub + '",';
		}
	}
	if (str != '{') {
		// 去除最后的逗号
		str = str.slice(0, -1);
	}
	str += '}';
	return str;
}

Ext.onReady(function() {
	// 5/7/09 16:46 用来判断是否隐藏提示 yiliu
	var flagHide = false;
	// 页面跳转时使用
	var register = parent.Ext.getCmp('tabPanel').register;
	// 设置监听器
	register.addRecord = setAllButtons;
	Ext.QuickTips.init();
	var purNo;
	var supplierCode;
	var arrivId;
	var mifNo;
	var invNo;
	// 是否是上报
	var flagReport = true;
	// 保存到货单详细的原始数据，用来判断数据有没有变更
	var objFormDatas = null;
	var strInvoiceOrderCode = null;// add by fyyang 091106
									// 保存发票号原始数据，用来判断数据有没有变更
	// 保存物资详细的原始数据，用来判断数据有没有变更
	var objGridDatas = null;
	// 是否初始化
	var flagInit = false;
	// 是否保存到db
	var flagDB = false;
	var flagNew = false;
	// 排他
	var flagOther = false;
	// 行号
	var row;

	// ↓↓*******************到货登记登记Tab**************************************
	// 查询字符串(采货单号/供应商/物资名称)
	var fuzzy = new Ext.form.TextField({
				id : "fuzzy",
				name : "fuzzy",
				width : 190,
				emptyText : "采购单号/供应商/物料"
			});

	// 确认按钮
	var query = new Ext.Button({
				text : "确认",
				id : "query",
				iconCls : Constants.CLS_OK,
				disabled : false,
				handler : function() {
					var rec = rightGrid.getSelectionModel().getSelections();
					if (rec == null) {
						Ext.Msg.alert(Constants.SYS_REMIND_MSG,
								Constants.COM_I_001);
					} else {
						submitBtn();
					}

				}
			});
	var stocktbar = new Ext.Toolbar({
		height : 25,
		items : [fuzzy, {
					text : "模糊查询",
					iconCls : Constants.CLS_QUERY,
					handler : function() {
						stockStore.baseParams = {
							queryString : fuzzy.getValue()
						};

						stockStore.load({
									params : {

										start : 0,
										limit : Constants.PAGE_SIZE
									}
								});
					}
				}]
			// , "-", query 隐藏页面的“确认”按钮 modify by ywliu 090717
		});

	// 采购gridStore
	var stockStore = new Ext.data.JsonStore({
				url : 'resource/getStockDesc.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : [
						// 采购单号
						{
					name : 'purNo'
				},
						// 合作伙伴名称
						{
							name : 'supplier'
						},
						// 日期
						{
							name : 'date'
						},
						// 合同号
						{
							name : 'contract'
						},
						// 备注
						{
							name : 'memo'
						},
						// 供应商ID
						{
							name : 'detailMemo'
						}]
			});

	stockStore.setDefaultSort("purNo", 'DESC');// 将升序显示ASC改为降序DESC显示 modify by
												// ywliu 2009/7/2
	stockStore.load({
				params : {
					start : 0,
					limit : Constants.PAGE_SIZE
				}
			});
	stockStore.on('load', function() {
				if (stockStore.getCount() == 0) {
					query.setDisabled(true);
				} else {
					query.setDisabled(false);
				}
			});
	// 采购panel
	var stockPanel = new Ext.grid.GridPanel({
				layout : 'fit',
				buttonAlign : 'center',
				bodyStyle : 'width:100%',
				border : true,
				fitToFrame : true,
				autoWidth : true,
				// 标题不可以移动
				enableColumnMove : false,
				store : stockStore,
				// 单选
				sm : new Ext.grid.RowSelectionModel({
					singleSelect : true
						// 不使用单击事件 yiliu 05/18/09
						// ,
						// listeners : {
						// rowselect : function(sm, row, rec) {
						// submitBtn();
						// }
						// }
					}),
				columns : [new Ext.grid.RowNumberer({
									header : "行号",
									align : 'right',
									width : 35
								}),
						// 采购单号
						{
							header : "采购单号",
							width : 75,
							sortable : true,
							align : 'left',
							defaultSortable : true,
							dataIndex : 'purNo'
						},
						// 供应商
						{
							header : "供应商",
							width : 120,
							sortable : true,
							align : 'left',
							dataIndex : 'supplier'
						},
						// 日期
						{
							header : "发布日期",
							width : 100,
							align : 'left',
							renderer : fromDateRend,
							sortable : true,
							hidden : true,
							dataIndex : 'date'
						}],
				viewConfig : {
					forceFit : true,
					autoFill : true
				},
				tbar : stocktbar,
				// 分页
				bbar : new Ext.PagingToolbar({
							pageSize : Constants.PAGE_SIZE,
							store : stockStore,
							displayInfo : true,
							displayMsg : '共 {2} 条',
							emptyMsg : Constants.EMPTY_MSG
						})
			})
	// 双击事件
	stockPanel.on('rowdblclick', submitBtn);

	// 保存按钮
	var btnSave = new Ext.Button({
				text : Constants.BTN_SAVE,
				iconCls : Constants.CLS_SAVE,
				id : "btnSave",
				disabled : true,
				handler : saveHandler
			});

	// 删除按钮
	var btnDelete = new Ext.Button({
				text : Constants.BTN_DELETE,
				iconCls : Constants.CLS_DELETE,
				disabled : true,
				handler : deleteArriveInfo
			});

	// 打印到货单按钮
	var printBtn = new Ext.Button({
				text : "打印",
				iconCls : 'print',
				disabled : true,
				handler : printArriveInfo
			});
	// 上报按钮
	var reportBtn = new Ext.Button({
				text : "发送",
				iconCls : Constants.CLS_REPOET,
				disabled : true,
				handler : reportHandler
			});

	var arriveCangotbar = new Ext.Toolbar({
				height : 25,
				items : [btnSave, '-', btnDelete, '-', printBtn, '-', reportBtn]
			});

	// 事务作用码
	var bussinessCode = new Ext.form.TextField({
				id : "bussinessCode",
				height : 22,
				fieldLabel : '事务作用码',
				readOnly : true,
				anchor : "90%"
			});

	// 事务作用名称
	var bussinessName = new Ext.form.TextField({
				id : "bussinessName",
				height : 22,
				fieldLabel : '事务作用名称',
				readOnly : true,
				anchor : "90%"
			});
	// 到货单号
	var arrivalOrderCode = new Ext.form.TextField({
				id : "arrivalOrderCode",
				height : 22,
				fieldLabel : '到货单号',
				readOnly : true,
				anchor : "90%",
				name : 'arriveCango.arrivalNo'
			});
	// 发票号 add by drdu 090618
	var invoiceOrderCode = new Ext.form.TextField({
				id : "invoiceOrderCode",
				height : 22,
				fieldLabel : '发票号',
				readOnly : false,
				anchor : "90%",
				name : 'arriveCango.invoiceNo',
				maxLength : 200
			});
	// 采购单号
	var purchaseOrderCode = new Ext.form.TextField({
				id : "purchaseOrderCode",
				height : 22,
				fieldLabel : '采购单号',
				readOnly : true,
				anchor : "90%",
				name : 'arriveCango.purNo'
			});
	// 采购单号
	var purchaseOrderCodeHidden = new Ext.form.Hidden({
				id : "purchaseOrderCodeHidden"
			});
	// 合同号
	var contactNo = new Ext.form.TextField({
				height : 22,
				id : "contactNo",
				fieldLabel : '合同号',
				readOnly : true,
				anchor : "90%",
				name : 'arriveCango.contractNo'
			});

	// 供应商
	var supplyName = new Ext.form.TextField({
				height : 22,
				id : "supplyName",
				fieldLabel : '供应商',
				readOnly : true,
				anchor : "90%"
			});
	var supplyNameHidden = new Ext.form.Hidden({
				id : "supplyNameHidden",
				name : "arriveCango.supplier"
			})
	// 日期
	var operateDate = new Ext.form.TextField({
				height : 22,
				id : "operateDate",
				fieldLabel : '日期',
				renderer : fromDateRender,
				readOnly : true,
				anchor : "90%"
			});
	var operateDateHidden = new Ext.form.Hidden({
				id : 'operateDateHidden',
				name : 'arriveCango.lastModifiedDate'
			})
	// 操作员
	var operator = new Ext.form.TextField({
				height : 22,
				id : "operator",
				fieldLabel : '操作员',
				readOnly : true,
				anchor : "90%",
				name : 'arriveCango.lastModifiedBy'
			});

	// 备注
	var detailMemo = new Ext.form.TextArea({
				height : Constants.TEXTAREA_HEIGHT,
				id : "detaMemo",
				fieldLabel : '备注',
				readOnly : false,
				maxLength : 128,
				anchor : "94.6%",
				name : 'arriveCango.memo'
			});

	var rightData = Ext.data.Record.create([
			// 物料编码
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
			// 流水号
			{
				name : 'id'
			},
			// 到货单流水号
			{
				name : 'arrivalId'
			},
			// 物料ID
			{
				name : 'materialID'
			},
			// 单位
			{
				name : 'purUm'
			},
			// 采购数
			{
				name : 'purQty'
			},
			// 已收数
			{
				name : 'rcvQty'
			},
			// 待收数
			{
				name : 'insQty'
			},
			// 到货数
			{
				name : 'theQty'
			},
			// 到货单明细流水号
			{
				name : 'arrivalDID'
			},
			// 批号
			{
				name : 'lotCode'
			},
			// 备注
			{
				name : 'detailMemo'
			},
			// 到货单编号
			{
				name : 'mifNo'
			},
			// 到货登记/验收表最后修改时间
			{
				name : 'dtArrivalInfo'
			},
			// 到货登记/验收明细表最后修改时间
			{
				name : 'dtArrivalDetailInfo'
			},
			// 采购单明细最后修改时间
			{
				name : 'dtOrderDetailInfo'
			}, {
				name : 'qaControlFlag'
			} // 是否免检 add by fyyang 090511
			, {
				name : 'sbMemo'
			}// 需求备注
			, {
				name : 'sbDeptName'
			}// 申报部门
	]);

	// 物资详细列表的store
	var rightStore = new Ext.data.JsonStore({
				url : 'resource/getMaterialDetails.action',
				root : 'list',
				// totalProperty : 'totalCount',
				fields : rightData
			});
	rightStore.on("load", function() {
				// 5/7/09 16:55 加入标记判断隐藏提示 yiliu
				if (flagHide) {
					Ext.Msg.hide();
					flagHide = false;
				}
				// ----------add by fyyang 090723------加合计行--------
				var totalpurQty = 0; // 采购数
				var totalrcvQty = 0; // 暂收数
				var totalinsQty = 0; // 待收数
				var totaltheQty = 0;// 到货数

				for (var j = 0; j < rightStore.getCount(); j++) {
					var temp = rightStore.getAt(j);
					if (temp.get("purQty") != null) {
						totalpurQty = parseFloat(totalpurQty)
								+ parseFloat(temp.get("purQty"));
					}
					if (temp.get("rcvQty") != null) {
						totalrcvQty = parseFloat(totalrcvQty)
								+ parseFloat(temp.get("rcvQty"));
					}
					if (temp.get("insQty") != null) {
						totalinsQty = parseFloat(totalinsQty)
								+ parseFloat(temp.get("insQty"));
					}
					if (temp.get("theQty") != null) {
						totaltheQty = parseFloat(totaltheQty)
								+ parseFloat(temp.get("theQty"));
					}
				}
				var mydata = new rightData({
							purQty : totalpurQty,
							rcvQty : totalrcvQty,
							insQty : totalinsQty,
							theQty : totaltheQty
						});
				if (rightStore.getCount() > 0) {
					rightStore.add(mydata);
				}

			});
	var rightStoreCopy = new Ext.data.JsonStore({
				url : 'resource/getMaterialDetails.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : rightData
			});

	// 分页
	var arriveCangobbar = new Ext.PagingToolbar({
				pageSize : Constants.PAGE_SIZE,
				store : rightStore,
				displayInfo : true,
				displayMsg : Constants.DISPLAY_MSG,
				emptyMsg : Constants.EMPTY_MSG
			});

	// 备注
	var memoWin = {
		header : "备注",
		width : 150,
		align : 'left',
		editor : new Ext.form.TextArea({
					maxLength : 250,
					listeners : {
						"render" : function() {
							this.el.on("dblclick", cellClickHandler);
						}
					}
				}),
		css : CSS_GRID_INPUT_COL,
		sortable : true,
		dataIndex : 'detailMemo'
	}

	var rsm = new Ext.grid.RowSelectionModel({
				singleSelect : false
			});
	rsm.on("rowselect", rowSelectedHandler);
	// 物资详细grid
	var rightGrid = new Ext.grid.EditorGridPanel({
		layout : 'fit',
		region : "center",
		anchor : "100%",
		autoScroll : true,
		style : "border-top:solid 1px",
		border : false,
		enableColumnMove : false,
		sm : rsm,
		clicksToEdit : 1,
		store : rightStore,
		// add by fyyang---增加合计行---------------
		listeners : {
			'beforeedit' : function(e) {
				if (e.field == "theQty" || e.field == "lotCode"
						|| e.field == "detailMemo") {
					var column = e.record.get('materialNo');
					if (column == null || column == "undefined") {
						return false;
					}
				}
			},
			'afteredit' : function(e) {
				if (e.field == "theQty") {
					var column = e.record.get('materialNo');
					if (column != null && column != "undefined") {
						var sumdata = 0;
						for (var j = 0; j < rightStore.getCount() - 1; j++) {
							var temp = rightStore.getAt(j);
							//add by ltong 默认到货数为空 20100412
							if (temp.get("theQty") != null
									&& temp.get("theQty") != '') {
								sumdata = parseFloat(sumdata)
										+ parseFloat(temp.get("theQty"));
							}
						}
						rightStore.getAt(rightStore.getCount() - 1).set(
								'theQty', sumdata);
					}
				}
			}

		},
		// addend--------------------
		columns : [new Ext.grid.RowNumberer({
							header : "行号",
							align : 'right',
							dataIndex : 'line',
							width : 35
						}),
				// 物料编码
				{
					header : "物料编码",
					width : 75,
					align : 'left',
					sortable : true,
					dataIndex : 'materialNo'
				},
				// 物料名称
				{
					header : "物料名称",
					width : 75,
					align : 'left',
					sortable : true,
					dataIndex : 'materialName'
				},
				// 规格型号
				{
					header : "规格型号",
					width : 75,
					align : 'left',
					sortable : true,
					dataIndex : 'specNo'
				},
				// 单位
				{
					header : "单位",
					width : 50,
					align : 'left',
					sortable : true,
					renderer : unitName,
					dataIndex : 'purUm'
				},
				// 采购数
				{
					header : "采购数",
					width : 75,
					align : 'right',
					renderer : numberFormat,
					sortable : true,
					dataIndex : 'purQty'
				},
				// 已收数
				{
					// header : "已收数",
					header : "暂收数",
					width : 75,
					align : 'right',
					renderer : numberFormat,
					sortable : true,
					dataIndex : 'rcvQty'
				},
				// 待收数
				{
					header : "待收数",
					width : 75,
					align : 'right',
					renderer : numberFormat,
					sortable : true,
					dataIndex : 'insQty'
				},
				// 到货数
				{
					header : "到货数",
					width : 75,
					align : 'right',
					renderer : numberFormat,
					editor : new Ext.form.NumberField({
								maxLength : 10,
								maxValue : 9999999.9999,
								decimalPrecision : 4
							}),
					css : CSS_GRID_INPUT_COL,
					sortable : true,
					dataIndex : 'theQty'
				},
				// 批号
				{
					header : "批号",
					width : 50,
					align : 'left',
					editor : new Ext.form.TextField({
								maxLength : 30
							}),
					sortable : true,
					css : CSS_GRID_INPUT_COL,
					dataIndex : 'lotCode'
				}, memoWin, {

					header : "是否免检",
					width : 70,
					align : 'left',
					sortable : true,
					renderer : function(value) {
						if (value == "Y")
							return "是";
						if (value == "N")
							return "否";
					},
					dataIndex : 'qaControlFlag'

				}, {
					header : '需求备注',
					dataIndex : 'sbMemo'
				}, {
					header : '申报部门',
					dataIndex : 'sbDeptName'
				}]
			// 5/5/09 16:02 取消分页 yiliu
			// bbar : arriveCangobbar

		});

	rightStore.setDefaultSort("materialNo");
	rightGrid.on("afteredit", function(obj) {
				var record = obj.record;
				var field = obj.field;
				if (field == 'theQty') {
					var number = record.get('theQty');
					var InsQty = record.get("InsQty");
					if (parseFloat(number) < 0) {
						Ext.Msg.alert(Constants.SYS_REMIND_MSG, String.format(
										Constants.COM_E_012, "到货数"));
						record.set('theQty', "0");
					}
				}
			});
	// 备注
	var memoText = new Ext.form.TextArea({
				id : "memoText",
				maxLength : 250,
				width : 180
			});

	// 弹出画面
	var win = new Ext.Window({
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
					text : Constants.BTN_SAVE,
					iconCls : Constants.CLS_SAVE,
					handler : function() {
						var rec = rightGrid.getSelectionModel().getSelections();
						if (Ext.get("memoText").dom.value.length <= 250) {
							rec[0].set("detailMemo",
									Ext.get("memoText").dom.value);
							win.hide();
						}
					}
				}, {
					text : Constants.BTN_CANCEL,
					iconCls : Constants.CLS_CANCEL,
					handler : function() {
						win.hide();
					}
				}]
	});
	// 第一行
	var firstLine = new Ext.form.FieldSet({
		border : false,
		height : 25,
		bodyStyle : 'width:100%',
		autoWidth : true,
		style : "padding-top:2px;padding-right:0px;padding-bottom:0px;margin-bottom:0px;border:0px",
		layout : "column",
		anchor : '100%',
		items : [{
					columnWidth : 0.5,
					layout : "form",
					border : false,
					items : [bussinessCode]
				}, {
					columnWidth : 0.5,
					layout : "form",
					border : false,
					items : [bussinessName]
				}]
	});
	// 第二行
	var secondLine = new Ext.form.FieldSet({
		border : false,
		height : 25,
		bodyStyle : 'width:100%',
		autoWidth : true,
		style : "padding-top:2px;padding-right:0px;padding-bottom:0px;margin-bottom:0px;border:0px",
		layout : "column",
		anchor : '100%',
		items : [{
					columnWidth : 0.5,
					layout : "form",
					border : false,
					items : [arrivalOrderCode]
				}, {
					columnWidth : 0.5,
					layout : "form",
					border : false,
					items : [purchaseOrderCode]
				}]
	});
	// 第三行
	var threeLine = new Ext.form.FieldSet({
		border : false,
		height : 25,
		bodyStyle : 'width:100%',
		autoWidth : true,
		style : "padding-top:2px;padding-right:0px;padding-bottom:0px;margin-bottom:0px;border:0px",
		layout : "column",
		anchor : '100%',
		items : [{
					columnWidth : 0.5,
					layout : "form",
					border : false,
					items : [contactNo]
				}, {
					columnWidth : 0.5,
					layout : "form",
					border : false,
					items : [supplyName]
				}]
	});
	// 第四行
	var fourLine = new Ext.form.FieldSet({
		border : false,
		height : 25,
		bodyStyle : 'width:100%',
		autoWidth : true,
		style : "padding-top:2px;padding-right:0px;padding-bottom:0px;margin-bottom:0px;border:0px",
		layout : "column",
		anchor : '100%',
		items : [{
					columnWidth : 0.5,
					layout : "form",
					border : false,
					items : [operateDate]
				}]
	});
	// 第五行
	var fiveLine = new Ext.form.FieldSet({
		border : false,
		bodyStyle : 'width:100%',
		autoWidth : true,
		autoHeight : true,
		style : "padding-top:2px;padding-right:0px;padding-bottom:0px;margin-bottom:0px;border:0px",
		layout : "column",
		anchor : '100%',
		items : [{
					columnWidth : 0.996,
					layout : "form",
					border : false,
					items : [detailMemo]
				}]
	});
	// 第六行 add by drdu 090618
	var sixLine = new Ext.form.FieldSet({
		border : false,
		height : 25,
		bodyStyle : 'width:100%',
		autoWidth : true,
		style : "padding-top:2px;padding-right:0px;padding-bottom:0px;margin-bottom:0px;border:0px",
		layout : "column",
		anchor : '100%',
		items : [{
					columnWidth : 0.5,
					layout : "form",
					// 隐藏发票号 add by bjxu 091209
					// hidden : true,
					border : false,
					items : [
					// invoiceOrderCode
					operateDate]
				}, {
					columnWidth : 0.5,
					layout : "form",
					border : false,
					items : [operator]
				}]
	});
	// 到货panel
	var arriveCangoPanel = new Ext.FormPanel({
		split : false,
		region : 'north',
		style : "padding-top:1.5px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
		autoHeight : true,
		border : false,
		frame : false,
		labelAlign : 'right',
		labelWidth : 80,
		anchor : '100%',
		items : [firstLine, secondLine, threeLine, sixLine,
				// fourLine,
				fiveLine, supplyNameHidden, operateDateHidden]

	});

	var leftPanel = new Ext.Panel({
				region : 'west',
				layout : 'fit',
				width : 330,
				autoScroll : true,
				border : false,
				containerScroll : true,
				collapsible : true,
				split : false,
				items : [stockPanel]
			});
	// 右边的panel
	var rightPanel = new Ext.Panel({
				region : "center",
				layout : 'border',
				border : true,
				tbar : arriveCangotbar,
				items : [arriveCangoPanel, rightGrid]
			});
	// 显示区域
	var view = new Ext.Viewport({
				enableTabScroll : true,
				autoScroll : true,
				layout : "border",
				items : [leftPanel, rightPanel]
			})

	// ***************************************
	// 查询结果grid双击处理
	function submitBtn() {
		Ext.Msg.wait("正在查询数据，请等待...");
		flagHide = true;
		var check = false;
		if (flagInit && isformCanSave()) {
			Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_C_004,
					function(buttonobj) {
						if (buttonobj == "yes") {
							intializtion();
						}
					})
		} else {
			intializtion();
		}

	}

	// 从列表tab过来的登记tab设置
	function setAllButtons(argMifNo, argSupplierCode, argArrivalId, argInvNo) {
		if (argArrivalId) {
			editMifNo(argArrivalId, argMifNo, argInvNo);
			supplierCode = argSupplierCode;
			arrivId = argArrivalId;
			mifNo = argMifNo;
			invNo = argInvNo;
			flagInit = true;
		}
	}

	// 设置表单
	function editMifNo(argArrivalId, argMifNo, argInvNo) {
		url = 'resource/setDesc.action?arrivalId =' + argArrivalId + "&limit="
				+ Constants.PAGE_SIZE + "&start=" + 0;
		var conn = Ext.lib.Ajax.getConnectionObject().conn;
		conn.open("POST", url, false);
		conn.send(null);

		var mydata = conn.responseText;
		mydata = Ext.util.JSON.decode(mydata);
		bussinessCode.setValue("N");
		bussinessName.setValue("到货登记");
		arrivalOrderCode.setValue(argMifNo);
		mifNo = argMifNo;// add by ywliu 20091118
		purchaseOrderCode.setValue(mydata.purNo);
		// add by drdu 090618
		// invoiceOrderCode.setValue(argInvNo);
		// modify by fyyang 091109 发票号
		// invoiceOrderCode.setValue(mydata.invoiceNo); //modify by fyyang
		// 091211
		contactNo.setValue(mydata.contract);
		supplyName.setValue(mydata.supplier);
		var fromDate = new Date();
		operateDateHidden.setValue(fromDateRender(fromDate));
		operateDate.setValue(fromDateRender(fromDate).substring(0, 10));
		detailMemo.setValue(mydata.memo);
		supplierCode = mydata.supplierId;
		// 设定操作员
		Ext.lib.Ajax.request('POST', 'resource/getArrivalWorkerCode.action', {
					success : function(action) {
						operator.setValue(action.responseText);
						operator.originalValue = operator.getValue();
					},
					failure : function() {
					}
				});
		btnSave.setDisabled(false);
		printBtn.setDisabled(false);
		if (Ext.get("purchaseOrderCode").dom.value != "") {
			reportBtn.setDisabled(false);
		} else {
			reportBtn.setDisabled(true);
		}
		btnDelete.setDisabled(false);
		saveData();
		rightStore.load({
					params : {
						arrivalId : argArrivalId,
						flag : true
						// ,
						// start : 0,
						// limit : Constants.PAGE_SIZE
					}
				});
		rightStoreCopy.load({
					params : {
						arrivalId : argArrivalId,
						flag : true,
						start : 0,
						limit : Constants.PAGE_SIZE
					}
				});
		flagDB = true;
	}

	// 初始化
	function intializtion() {
		var sm = stockPanel.getSelectionModel();
		var selected = sm.getSelections();
		if (selected.length == 0) {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_001);
		} else {
			var member = selected[0].data;
			Ext.Ajax.request({
						url : 'resource/getReportArrival.action',
						method : 'POST',
						params : {
							purNo : member["purNo"]
						},
						success : function(result, request) {
							var mydata = eval("(" + result.responseText + ")");
							if (mydata.success == "") {
								// 初始化到货tab
								bussinessCode.setValue("N");
								bussinessName.setValue("到货登记");
								arrivalOrderCode.setValue("");
								purNo = member["purNo"];
								purchaseOrderCode.setValue(member["purNo"]);
								contactNo.setValue(member["contract"]);
								supplyName.setValue(member["supplier"]);
								var fromDate = new Date();
								operateDateHidden
										.setValue(fromDateRender(fromDate));
								operateDate.setValue(fromDateRender(fromDate)
										.substring(0, 10));
								detailMemo.setValue("");
								// add by drdu 090618
								// invoiceOrderCode.setValue(""); //modify by
								// fyyang 091211
								// 设定操作员
								Ext.lib.Ajax.request('POST',
										'resource/getArrivalWorkerCode.action',
										{
											success : function(action) {
												operator
														.setValue(action.responseText);
												operator.originalValue = operator
														.getValue();
											},
											failure : function() {
											}
										});
								btnSave.setDisabled(false);
								printBtn.setDisabled(false);
								if (Ext.get("purchaseOrderCode").dom.value != "") {
									reportBtn.setDisabled(false);
								} else {
									reportBtn.setDisabled(true);
								}

								if (Ext.get("arrivalOrderCode").dom.value != "") {
									btnDelete.setDisabled(false);
								} else {
									btnDelete.setDisabled(true);
								}
								saveData();
								rightStore.load({
											params : {
												purNo : purNo,
												flag : false
												// ,
												// start : 0,
												// limit : Constants.PAGE_SIZE
											}
										});

								rightStoreCopy.load({
											params : {
												purNo : purNo,
												flag : false,
												start : 0,
												limit : Constants.PAGE_SIZE
											}
										});
								flagNew = true;
								flagDB = false;
							} else {
								editMifNo(mydata.success, mydata.msg,
										mydata.msgNo);
								arrivId = mydata.success;
								invNo = mydata.msgNo;
							}
							flagInit = true;
						},
						failure : function() {
						}
					});

		}
		return true;
	}

	// 保存原有数据
	function saveData() {
		// 保存原始数据，用来判断数据有没有变更
		objFormDatas = detailMemo.getValue();
		// strInvoiceOrderCode=invoiceOrderCode.getValue(); //modify by fyyang
		// 091211
	}

	// 表单check
	function isformCanSave() {
		// 信息没有改变直接返回
		if (!isArriveChanged() && !isCangoChanged()) {
			return false;
		}
		return true;
	}

	// 到货单可变
	function isArriveChanged() {
		var obj = detailMemo.getValue();
		if (objFormDatas != null) {
			if (obj != objFormDatas) {
				return true;
			}
		}
		// ----add by fyyang 091106--------delete by fyyang 091211-----
		// if(strInvoiceOrderCode!=null)
		// {
		// if(invoiceOrderCode.getValue()!=strInvoiceOrderCode)
		// {
		// return true;
		// }
		// }
		// --------add end-------------------------------
		return false;
	}

	// 物资详细可变
	function isCangoChanged() {
		// modify by fyyang 090723
		// rightStore.getCount()--->rightStore.getCount()-1
		for (var i = 0; i < rightStore.getCount() - 1; i++) {
			// 判断有没有改变
			if (!(rightStore.getAt(i).get("theQty") == "" && rightStoreCopy
					.getAt(i).get("theQty") == "")) {

				if (rightStore.getAt(i).get("theQty") != rightStoreCopy
						.getAt(i).get("theQty")) {
					return true;
				}
			}
			if (!(rightStore.getAt(i).get("lotCode") == "" && rightStoreCopy
					.getAt(i).get("lotCode") == "")) {

				if (rightStore.getAt(i).get("lotCode") != rightStoreCopy
						.getAt(i).get("lotCode")) {
					return true;
				}
			}
			if (!(rightStore.getAt(i).get("detailMemo") == "" && rightStoreCopy
					.getAt(i).get("detailMemo") == "")) {

				if (rightStore.getAt(i).get("detailMemo") != rightStoreCopy
						.getAt(i).get("detailMemo")) {
					return true;
				}
			}
		}
		return false;
	}

	// 物资详细grid双击处理
	function cellClickHandler() {
		var rec = rightGrid.getSelectionModel().getSelections();
		var record = rightGrid.getStore().getAt(row);
		win.show();
		memoText.setValue(record.get("detailMemo"));
	}

	function rowSelectedHandler(rsm, rowIndex) {
		row = rowIndex;
	}
	// -----------------------add by fyyang
	// 091123------------------------------------------------
	function checkTheQty() {
		for (var i = 0; i < rightStore.getCount() - 1; i++) {
			// 判断有没有改变
			if (rightStore.getAt(i).get("theQty") != ""
					&& rightStore.getAt(i).get("theQty") != 0
					&& rightStore.getAt(i).get("theQty") != null) {
				return true;
			}
		}
		return false;
	}
	// add by fyyang 091218 判断到货数大于待收数的行
	function checkMoreTheQty() {
		var lineNum = "";
		for (var i = 0; i < rightStore.getCount() - 1; i++) {
			if (rightStore.getAt(i).get("theQty") > rightStore.getAt(i)
					.get("insQty")) {
				var j = i + 1;
				if (lineNum == "")
					lineNum = j;
				else
					lineNum = lineNum + "," + j;
			}
		}
		return lineNum;
	}
	// -----------------------------------------------------------------------

	// 点击保存按钮
	function saveHandler() {
		// modify by drdu 091124
		// if(invoiceOrderCode.getValue() != null && invoiceOrderCode.getValue()
		// != null)
		// {
		// if(invoiceOrderCode.getValue().length > 30)
		// {
		// Ext.Msg.alert('提示','发票号最多为30位！');
		// return;
		// }
		// }
		// add by fyyang 091123
		if (!checkTheQty()) {
			
			Ext.Msg.alert('提示', '请填写到货数！');
			return;
		}
		if (isformCanSave() || !flagDB) {
			var lineNum = checkMoreTheQty();
			var strMsg = "";
			if (lineNum == "") {
				strMsg = "确定要保存吗？";
			} else {
				strMsg = "第" + lineNum + "行到货数大于待收数，是否继续保存？"
			}
			Ext.Msg.confirm(Constants.SYS_REMIND_MSG, strMsg, function(
							buttonobj) {
						if (buttonobj == "yes") {
							if (checkReimburse()) {
								saveArriveInfo();
							} else {
								// alert(Constants.COM_E_001);
								Ext.Msg.alert(Constants.SYS_REMIND_MSG,
										Constants.COM_E_001);
							}
						}
					})
		} else {
			// alert(Constants.COM_I_006);
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_006);
		}
	}

	// 保存操作
	function saveArriveInfo() {

		// var urll = 'resource/getSupplier.action?supplierName='
		// + Ext.get("supplyName").dom.value;
		//
		// var conn = Ext.lib.Ajax.getConnectionObject().conn;
		// conn.open("POST", urll, false);
		// conn.send(null);
		// // alert(conn.responseText);
		// var mydata = conn.responseText;
		// if (mydata != null && mydata != "") {
		// mydata = Ext.util.JSON.decode(mydata);
		// supplyNameHidden.setValue(mydata);
		// }
		var _sel = stockPanel.getSelectionModel().getSelected();
		supplyNameHidden.setValue(_sel.get("detailMemo"));

		var saveInfo = [];

		for (var i = 0; i < rightStore.getTotalCount(); i++) {
			member = rightStore.getAt(i).data;
			saveInfo.push(member);
		}

		var flagUpdate;
		if (Ext.get("arrivalOrderCode").dom.value != "") {
			flagUpdate = true;
		} else {
			flagUpdate = false;
		}
		url = 'resource/updateArrival.action';
		// 提交表单
		arriveCangoPanel.getForm().submit({
			url : url,
			method : 'POST',
			params : {
				flagUpdate : flagUpdate,
				arrivalId : arrivId,
				purNo : Ext.get("purchaseOrderCode").dom.value,
				mifNo : mifNo,
				saveInfo : Ext.util.JSON.encode(saveInfo),// saveInfo.toJSONString(),modify
															// by drdu 091124
				invNo : ''// Ext.get("invoiceOrderCode").dom.value //modify by
							// fyyang 091211
			},
			success : function(form, action) {
				var resultl = eval("(" + action.response.responseText + ")");
				if (resultl.msg == "otherUse") {
					// 排他处理
					Ext.Msg
							.alert(Constants.SYS_REMIND_MSG,
									Constants.COM_E_015);
					// alert(Constants.COM_E_015);
					flagOther = true;
					return;
				} else {
					if (resultl.success == "0") {
						Ext.Msg.alert(Constants.SYS_REMIND_MSG, resultl.msg);
						// alert(resultl.msg);
					} else {
						if (resultl.msg != "") {
							arrivId = resultl.msgid;
							mifNo = resultl.msg;
							invNo = resultl.msgNo;
						}
						if (flagReport) {
							if (Ext.get("arrivalOrderCode").dom.value != "") {
								Ext.Msg.alert(Constants.SYS_REMIND_MSG,
										Constants.COM_I_004);
								// Ext.Msg.alert('保存成功','提示');
								// alert("保存成功！");
							} else {
								Ext.Msg.alert(Constants.SYS_REMIND_MSG, String
												.format(Constants.RB003_I_001,
														mifNo));
								// alert("保存成功，到货单号："+mifNo);
								register.add();
							};
						}

						// invNo=invoiceOrderCode.getValue();//delete by fyyang
						// 091211
						editMifNo(arrivId, mifNo, invNo);
						flagNew = false;
						flagReport = true;
					}
				}
			},
			failure : function() {
				// if (Ext.get("detaMemo").dom.value.length <= 128) {
				Ext.Msg.alert(Constants.ERROR, String.format(
								Constants.COM_E_007, arrivId));
				// alert(arrivId+"已存在。请重新输入。");
				// }
			}
		});
	}

	// 删除处理
	function deleteArriveInfo() {
		if (checkReimburse()) {
			var saveInfo = [];
			for (var i = 0; i < rightStore.getTotalCount(); i++) {
				member = rightStore.getAt(i).data;
				saveInfo.push(member);
			}
			Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_C_002,
					function(buttonobj) {
						if (buttonobj == "yes") {
							Ext.Ajax.request({
								method : Constants.POST,
								waitMsg : Constants.DATA_DEL,
								url : 'resource/deleteArrival.action',
								success : function(result, request) {
									var result = eval("(" + result.responseText
											+ ")");
									if (result.msg == "otherUse") {
										// 排他处理
										Ext.Msg.alert(Constants.SYS_REMIND_MSG,
												Constants.COM_E_015);
										flagOther = true;
										return;
									}
									Ext.Msg.alert(Constants.SYS_REMIND_MSG,
											Constants.COM_I_005);
									btnSave.setDisabled(true);
									printBtn.setDisabled(true);
									reportBtn.setDisabled(true);
									btnDelete.setDisabled(true);
									var arrFlag;
									if (Ext.get("arrivalOrderCode").dom.value != "") {
										arrFlag = true;
									} else {
										arrFlag = false;
									}
									rightStore.reload({
												params : {
													arrivalId : arrivId,
													purNo : purNo,
													flag : arrFlag,
													start : 0,
													limit : Constants.PAGE_SIZE
												}
											});
									rightStoreCopy.reload({
												params : {
													arrivalId : arrivId,
													purNo : purNo,
													flag : arrFlag,
													start : 0,
													limit : Constants.PAGE_SIZE
												}
											});
									arriveCangoPanel.getForm().reset();
									operator.setValue("");
									register.add();
									flagNew = false;
								},
								failure : function() {
									Ext.Msg.alert(Constants.ERROR,
											String.format(Constants.COM_E_007,
													arrivId));
								},
								params : {
									arrivalId : arrivId,
									mifNo : Ext.get("arrivalOrderCode").dom.value,
									saveInfo : Ext.util.JSON.encode(saveInfo)
								}
							});
						}
					})
		} else {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_E_001);
		}

	}

	// 结帐
	function checkReimburse() {
		url = 'resource/isReimBurse.action';
		var conn = Ext.lib.Ajax.getConnectionObject().conn;
		conn.open("POST", url, false);
		conn.send(null);
		var mydata = conn.responseText;
		mydata = Ext.util.JSON.decode(mydata);
		if (mydata == "0") {
			return true;
		}
		return false;
	}
	// 上报处理
	var isReport = false;// add by liuyi 20100401 是否为上报
	rightStore.on('load', function() {
				if (isReport) {
					if (!flagOther) {
						Ext.Msg.hide();
						reportArriveInfo();
					}
					flagOther = false;

				}
				isReport = false;
			})
	function reportHandler() {
		// modified by liuyi 20100401
		if (isformCanSave() || flagNew) {
			Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_W_002,
					function(buttonobj) {
						if (buttonobj == "yes") {
							flagReport = false;
							saveArriveInfo();
							isReport = true;
							Ext.Msg.wait('正在保存数据……')
							// if (!flagOther) {
							// reportArriveInfo();
							// }
							// flagOther = false;
						}
					});
			flagNew = false;
		} else {
			reportArriveInfo();
		}
	}
	// 上报子操作
	function reportArriveInfoChild() {
		if (checkReimburse()) {
			var theQtyFlag = false;
			var member;
			var saveInfo = [];
			for (var i = 0; i < rightStore.getTotalCount(); i++) {
				member = rightStore.getAt(i).data;
				if (member.theQty != 0) {
					theQtyFlag = true;
				}
				saveInfo.push(member);
			}
			if (theQtyFlag) {
				Ext.Ajax.request({
							url : 'resource/reportArrival.action',
							method : 'POST',
							params : {
								arrivalId : arrivId,
								mifNo : mifNo,
								supplierCode : supplierCode,
								supplierT : Ext.get("supplyName").dom.value,
								memo : Ext.get("detaMemo").dom.value,
								saveInfo : Ext.util.JSON.encode(saveInfo)
							},
							success : function(result, request) {
								var result = eval("(" + result.responseText
										+ ")");
								if (result.msg == "otherUse") {
									// 排他处理
									Ext.Msg.alert(Constants.SYS_REMIND_MSG,
											Constants.COM_E_015);
									return;
								}
								Ext.Msg
										.alert(Constants.SYS_REMIND_MSG,
												"发送成功！");
								stockStore.reload({
											params : {
												start : 0,
												limit : Constants.PAGE_SIZE
											}
										});
								arriveCangoPanel.getForm().reset();
								operator.setValue("");
								rightStore.removeAll();
								rightStoreCopy.removeAll();
								btnSave.setDisabled(true);
								printBtn.setDisabled(true);
								reportBtn.setDisabled(true);
								btnDelete.setDisabled(true);
								register.add();
								flagInit = false;
							},
							failure : function() {
								Ext.Msg.alert(Constants.ERROR,
										Constants.COM_E_014);
							}
						});
			} else {
				Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.RB003_I_002);
			}
		} else {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_E_001);
		}
	}
	// 上报操作
	function reportArriveInfo() {
		Ext.Msg.confirm(Constants.SYS_REMIND_MSG, "确定要发送吗？",
				function(buttonobj) {
					if (buttonobj == "yes") {
						reportArriveInfoChild();
					}
				});

	}

	// 打印处理
	function printArriveInfo() {
		if (isformCanSave()) {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_010);
		} else {
			if (Ext.get("arrivalOrderCode").dom.value == "") {
				Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.RB003_E_001);
			} else {
				window.open("/power/"+"report/webfile/achieveGoods.jsp?mifNo="
						+ Ext.get("arrivalOrderCode").dom.value);
			}
		}
	}
	// 取得单位名称
	function unitName(value) {
		if (value != null && value != "undefined") {
			var url = "resource/getRB003UnitName.action?unitCode=" + value;
			var conn = Ext.lib.Ajax.getConnectionObject().conn;
			conn.open("POST", url, false);
			conn.send(null);
			return conn.responseText;
		} else
			return "";
	};
	// 渲染日期
	function fromDateRender(value) {
		if (!value)
			return '';
		if (value instanceof Date)
			return renderDate(value);
		var reDate = /\d{4}\-\d{2}\-\d{2}/gi;
		var reTime = /\d{2}:\d{2}:\d{2}/gi;
		var strDate = value.match(reDate);
		var strTime = value.match(reTime);
		if (!strDate)
			return "";
		return strTime ? strDate + " " + strTime : strDate;
	}
	// 渲染日期
	function fromDateRend(value) {
		if (!value)
			return '';
		if (value instanceof Date)
			return renderDate(value);
		var reDate = /\d{4}\-\d{2}\-\d{2}/gi;
		var reTime = /\d{2}:\d{2}:\d{2}/gi;
		var strDate = value.match(reDate);
		var strTime = value.match(reTime);
		if (!strDate)
			return "";
		return strDate;
	}

	// 渲染时间格式
	function renderDate(value) {
		return value ? value.dateFormat('Y-m-d h-m-s') : '';
	}

	setAllButtons(register.mifNo, register.supplierCode, register.arrivalId);
});