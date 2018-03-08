Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
var materialGrid;
  function checkIsModify()
	{
		//add by fyyang 091028
		var record = materialGrid.getStore().getModifiedRecords();
		if(record.length>1)
		{
		   return false;
		}
		else
		{
			return true;
		}
	}
Ext.onReady(function() {
	
	
	
var planId=getParameter("id");
	
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
	// 09/07/21 ywliu
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
	 * 获取当前月的日期
	 */
	function getCurrentDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t;
		return s;
	}

	/**
	 * 获取当前前一月的日期
	 */
	function getCurrentDateFrom() {
		var d, s, t;
		d = new Date();
		d.setDate(d.getDate() - 30)
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t;
		return s;
	}
	/**
	 * 获取当前后一月的日期
	 */
	function getCurrentDateTo() {
		var d, s, t;
		d = new Date();
		d.setDate(d.getDate() + 30)
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t;
		return s;
	}
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
	 * 需求时间与当前时间比较
	 */
	function checkTime() {
		var startDate = txtMrDateFrom.getValue();
		var endDate = txtMrDateTo.getValue();
		if (startDate != "" && endDate != "") {
			var res = compareDateStr(startDate, endDate);
			if (res) {
				Ext.Msg.alert(Constants.SYS_REMIND_MSG, String.format(
								Constants.COM_E_006, "计划月份（开始月份）", "至（终止月份）"));

			}
		}
		return true;
	}
	/**
	 * textField显示时间比较方法
	 */
	function compareDate(argDate1, argDate2) {
		return argDate1.getTime() >= argDate2.getTime();
	}
	/**
	 * textField显示时间比较方法
	 */
	function compareDateStr(argDateStr1, argDateStr2) {
		var date1 = Date.parseDate(argDateStr1, 'Y-m');
		var date2 = Date.parseDate(argDateStr2, 'Y-m');
		return compareDate(date1, date2);
	}

	/**
	 * 赋值主grid中物料类别
	 */
	function getClassName() {
		return txtMaterialClass.getRawValue();
	}
	/**
	 * 当单击主grid中一行时，下面的grid显示详细信息
	 */
	function showDetail() {
		if (headGrid.getSelectionModel().getSelected() != null) {
			var record = headGrid.getSelectionModel().getSelected();
			materialStore.load({
						params : {
							headId : record.get('requirementHeadId')
						}
					});
		}

	}
	// --------第三行grid--结束
	// 物料详细记录
	var material = Ext.data.Record.create([{
				name : 'requirementDetailId'
			}, {
				name : 'materialNo'
			}, {
				name : 'materialName'
			}, {
				name : 'materSize'
			}, {
				name : 'appliedQty'
			}, {
				name : 'approvedQty'
			}, {
				name : 'issQty'
			}, {
				name : 'estimatedPrice'
			}, {
				name : 'estimatedSum'
			}, {
				name : 'purQty'
			}, {
				name : 'stockUmName'
			}, {
				name : 'usage'
			}, {
				name : 'memo'
			}, {
				name : 'needDate'
			}, {
				name : 'parameter'
			}, {
				name : 'docNo'
			}, {
				name : 'whsName'
			}, {
				name : 'qualityClass'
			}, {
				name : 'left'
			}, {
				name : 'tempNum'
			}, {
				name : 'itemId'
			}]);
			
	// 物料grid的store
	var materialStore = new Ext.data.JsonStore({
				url : 'resource/getMaterialDetail.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : material
			});
			
	var editorApprovedQty = new Ext.form.TextField({
		fieldLabel : '设备名称内容<font color="red">*</font>',
		id : "approvedQty",
		name : "approvedQty",
		allowBlank : false,
		readOnly : false,
		decimalPrecision : 4
	});
	
	// 物料grid
	 materialGrid = new Ext.grid.EditorGridPanel({
				// layout : 'fit',
				// height : 200,
				// anchor : "100%",
				region : "center",
				border : false,
				autoScroll : true,
				enableColumnMove : false,
				// 单选
				sm : new Ext.grid.RowSelectionModel({
							singleSelect : true
						}),
				// 单击修改
				store : materialStore,
				columns : [new Ext.grid.RowNumberer({
									header : "行号",
									width : 35
								}),
						// 项次号
						{
							header : "项次号",
							width : 100,
							hidden:true,
							sortable : true,
							dataIndex : 'requirementDetailId'
						},
						// 物料编码
						{
							header : "物料编码",
							width : 100,
							sortable : true,
							dataIndex : 'materialNo'
						},// 物料名称
						{
							header : "物料名称",
							width : 100,
							sortable : true,
							dataIndex : 'materialName'
						},// 物料规格
						{
							header : "物料规格",
							width : 100,
							sortable : true,
							dataIndex : 'materSize'
						},// 申请数量
						{
							header : "申请数量",
							width : 100,
							sortable : true,
							renderer : function(value, cellmeta, record, rowIndex, columnIndex,
									store) {
								if (rowIndex < store.getCount() - 1) {
									// 强行触发renderer事件
									var totalSum = 0;
									for (var i = 0; i < store.getCount() - 1; i++) {
										totalSum += store.getAt(i).get('appliedQty');
									}
									if (store.getAt(store.getCount() - 1).get('isNewRecord') == 'total') {
										store.getAt(store.getCount() - 1).set('appliedQty',
												totalSum);
									}
									return moneyFormat(value);
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
						},// 核准数量
						{
							header : "核准数量",
							width : 100,
							sortable : true,
							css : CSS_GRID_INPUT_COL,
							renderer : function(value, cellmeta, record, rowIndex, columnIndex,
									store) {
								if (rowIndex < store.getCount() - 1) {
									// 强行触发renderer事件
									var totalSum = 0;
									for (var i = 0; i < store.getCount() - 1; i++) {
										totalSum += store.getAt(i).get('approvedQty');
									}
									if (store.getAt(store.getCount() - 1).get('isNewRecord') == 'total') {
										store.getAt(store.getCount() - 1).set('approvedQty',
												totalSum);
									}
									return moneyFormat(value);
								} else {
									totalSum = 0;
									for (var i = 0; i < store.getCount() - 1; i++) {
										totalSum += store.getAt(i).get('approvedQty');
									}
									return "<font color='red'>" + moneyFormat(totalSum)
											+ "</font>";
								}
							},
							dataIndex : 'approvedQty',
							editor:editorApprovedQty
//							renderer:function(value){
//								alert(value);
//                             return "<span style='color:blue;'>"+value+"</span>";
//
//		              }
						},// 已领数量
						{
							header : "已领数量",
							width : 100,
							sortable : true,
							hidden:true,
							renderer : moneyFormat,
							dataIndex : 'issQty'
						},
						// 单位
						{
							header : "单位",
							width : 100,
							sortable : true,
							dataIndex : 'stockUmName'
						},
						// 需求日期
						{
							header : "需求日期",
							width : 100,
							sortable : true,
							dataIndex : 'needDate'
						},
						// 估计单价
						{
							header : "估计单价",
							width : 100,
							sortable : true,
							renderer : function(value, cellmeta, record, rowIndex, columnIndex,
									store) {
								if (rowIndex < store.getCount() - 1) {
									if (store.getAt(store.getCount() - 1).get('isNewRecord') == 'total') {
										store.getAt(store.getCount() - 1).set('estimatedPrice',
												"");
									}
									return numberFormat(value);
								} 
							},
							dataIndex : 'estimatedPrice'
						},// 估计金额
						{
							header : "估计金额",
							width : 100,
							sortable : true,
							renderer : function(value, cellmeta, record, rowIndex, columnIndex,
									store) {
								if (rowIndex < store.getCount() - 1) {
									// 强行触发renderer事件
									var totalSum = 0;
									for (var i = 0; i < store.getCount() - 1; i++) {
										totalSum += store.getAt(i).get('estimatedSum');
									}
									if (store.getAt(store.getCount() - 1).get('isNewRecord') == 'total') {
										store.getAt(store.getCount() - 1).set('estimatedSum',
												totalSum);
									}
									return moneyFormat(value);
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
						},
						// 采购数量
						{
							header : "采购数量",
							width : 100,
							sortable : true,
							hidden:true,
							renderer : numberFormat,
							dataIndex : 'purQty'
						},
						

						// 物料材质
						{
							header : "物料材质",
							width : 100,
							sortable : true,
							dataIndex : 'parameter'
						},
						// 物料图号
						{
							header : "物料图号",
							width : 100,
							sortable : true,
							dataIndex : 'docNo'
						},
						// 仓库
						{
							header : "仓库",
							width : 100,
							sortable : true,
							dataIndex : 'whsName'
						},// 质量等级
						{
							header : "质量等级",
							width : 100,
							sortable : true,
							dataIndex : 'qualityClass'
						},
						// 当前库存
						{
							header : "当前库存",
							width : 100,
							sortable : true,
							renderer :  function(value, cellmeta, record, rowIndex, columnIndex,
									store) {
								if (rowIndex < store.getCount() - 1) {
									if (store.getAt(store.getCount() - 1).get('isNewRecord') == 'total') {
										store.getAt(store.getCount() - 1).set('left',
												"");
									}
									return numberFormat(value);
								} 
							},
							dataIndex : 'left'
						},
						// 暂收数量
						{
							header : "暂收数量",
							width : 100,
							sortable : true,
							hidden:true,
							renderer : numberFormat,
							dataIndex : 'tempNum'
						},
						// 费用来源
						{
							header : "费用来源",
							width : 100,
							sortable : true,
							renderer :function(val){
//	                        if(val == "1"){
//		                            return "费用来源1";
//		                        }else if(val =="2"){
//		                            return "费用来源2";
//		                        }else if(val == "3"){
//		                            return "费用来源3";
//		                        }else if(val == "4"){
//		                            return "费用来源4";
//		                        }else {
//		                            return "";
//		                        }
								// modify by ywliu 20100225
								return getBudgetName(val);
		                    },
							dataIndex : 'itemId'
						},
						// 用途
						{
							header : "用途",
							width : 100,
							sortable : true,
							dataIndex : 'usage'
						},
						// 备注
						{
							header : "备注",
							width : 100,
							sortable : true,
							dataIndex : 'memo'
						}],
						tbar:['请确认核准数量','-',
						{text:'保存',
						handler:saveRecord
						  },'-',
						  {
						  	text:'取消',
						  	handler:function()
						  	{
						  		queryRecord();
						  	}
						  }
						  ],
				clicksToEdit:1,
				enableColumnMove : false,
				enableColumnHide : true,
				border : false
			});
	//materialGrid.on("celldblclick", cellClickHandler);
	
	materialGrid.on("beforeedit",function contorlEdit(e) {
		var record = e.record.get('isNewRecord');
		if(record == "total") {
			return false;
		}
		
	});
	
	
			
	function saveRecord()
	{
		var record = materialGrid.getStore().getModifiedRecords();
		
		var modifyRecords = new Array(); 
		for (var i = 0; i < record.length; i++) { 
				modifyRecords.push(record[i].data); 
			}
			//alert(Ext.util.JSON.encode(modifyRecords));
			if (record.length > 0) {
					Ext.Ajax.request({
						url : 'resource/updatePlanDetailForApprove.action',
						params : {
							//data : str
							data : Ext.util.JSON.encode(modifyRecords)
						},
						method : 'post',
						waitMsg : '正在加载数据...',
						success : function(result, request) {
							var json = eval('(' + result.responseText + ')');
							Ext.Msg.alert("注意", json.msg);
							if(json.msg=="保存成功！")
							{
							
							materialStore.load({
								params : {
									start : 0,
									limit : 10
								},
								callback : addLine
							});// 加入统计行 ywliu 090723
							materialStore.rejectChanges();
							}
						},
						failure : function(result, request) {
							Ext.MessageBox.alert('错误', '操作失败!');
						}
					});
			}
			 else {
			alert("没有对数据进行任何修改！");
		}
	}
	
	// 备注
	var memoText = new Ext.form.TextArea({
				id : "memoText",
				width : 180,
				readOnly : true
			});


	var layout = new Ext.Viewport({
				layout : 'fit',
				margins : '0 0 0 0',
				border : false,
				items : [materialGrid]
			});
	// 查询
	function queryRecord() {
		materialStore.baseParams = {
			headId:planId
		};
		materialStore.load({
			params : {
				start : 0,
				limit : 10
			},
			callback : addLine
		});
	}
	
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
		// 停止原来编辑
		materialGrid.stopEditing();
		// 插入统计行
		materialStore.insert(count, record);
		materialGrid.getView().refresh();
		totalCount = materialStore.getCount() - 1;

	};
	
    queryRecord();
    
  

});