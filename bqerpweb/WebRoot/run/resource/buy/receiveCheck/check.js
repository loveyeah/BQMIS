Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
//----------------add-----------------------
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
					// 判断是否是统计行
					if (r.data["materialNo"] ==null||r.data["materialNo"]=="undefined") {
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
//------------------------------------------
// 对应变更
function numberFormat(value){
			if(value===""){
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
			    if(sub.length > 3){
			    	sub = sub.substring(0,3);
			    }
		    }
            var r = /(\d+)(\d{3})/;
            while (r.test(whole)){
                whole = whole.replace(r, '$1' + ',' + '$2');
            }
            v = whole + sub;
            return v;	
	}
Ext.onReady(function() {
	Ext.QuickTips.init();

	var objFormDatas;
	// ↓↓*******************采购入库管理**************************************
	// 查询值
	var fuzzy = new Ext.form.TextField({
				id : "fuzzy",
				value : "",
				maxLength:30,
				name : "fuzzy"
			});

	// 到货单grid的store
	var arrivalOrderStore = new Ext.data.JsonStore({
				url : 'resource/getArrivalListForArrivalCheck.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : [
						// 到货登记/验收表的流水号
						{
					name : 'id'
				},
						// 到货登记/验收表.单据编号，
						{
							name : 'arrivalNo'
						},
						// 到货登记/验收表.订单编号
						{
							name : 'purNo'
						},
						// 到货登记/验收表.合同编号
						{
							name : 'contractNo'
						},
						// 供应商表.供应商编号
						{
							name : 'supplier'
						},
						// 供应商表.供应商全称
						{
							name : 'supplyName'
						},
						// 到货登记/验收表.操作日期
						{
							name : 'operateDate'
						},// 到货登记/验收表备注
						{
							name : 'memo'
						},// 登陆者名字
						{
							name : 'loginName'
						},{
							name:'operateDate2'
						}]
			});
	var confirmBtn = new Ext.Button({
				text : '确认',
				id : 'confirmBtn',
				iconCls : Constants.CLS_OK,
				name : 'confirmBtn',
				disabled : true,
				handler : function() {
					showRightPurchaseWarehouse();
				}
			});
    arrivalOrderStore.setDefaultSort('arrivalNo', 'DESC'); // 降序排列 modify by ywliu 090717
	arrivalOrderStore.on('beforeload', function() {
				Ext.apply(this.baseParams, {
							fuzzy : fuzzy.getValue()
						});
			});
	arrivalOrderStore.load({
				params : {
					start : 0,
					limit : Constants.PAGE_SIZE
				}
			});
	arrivalOrderStore.on("load", function() {
				if (arrivalOrderStore.getCount() > 0) {
					confirmBtn.setDisabled(false);
				} else {
					confirmBtn.setDisabled(true);
					
				}

			});

	// 到货单grid
	var arrivalOrderGrid = new Ext.grid.GridPanel({
				// renderTo : 'left-div',
				border : true,
				// height:700,
				// width:300,
				sm : new Ext.grid.RowSelectionModel({
							singleSelect : true
						}),
					
				fitToFrame : true,
				store : arrivalOrderStore,
				columns : [new Ext.grid.RowNumberer({
							header : "行号",
							width : 35
			}),
						// 到货单号
						{
					header : "到货单号",
					sortable : true,
					width : 120,
					dataIndex : 'arrivalNo'
				},
						// 供应商
						{
							header : "供应商",
							sortable : true,
							width : 100,
							dataIndex : 'supplyName'
						},
						// 日期
						{
							header : "日期",
							sortable : true,
							width : 100,
							dataIndex : 'operateDate'
						}],
				viewConfig : {
					forceFit : true
				},
				tbar : ['到货单号:', fuzzy, "-", {
							text : "查询",
							iconCls : Constants.CLS_QUERY,
							handler : function() {/*
								if (rightGridDetailIsChanged()) {
									// 
									Ext.Msg.confirm("提示信息", "放弃已修改的内容吗？", function(buttonobj) {
						if (buttonobj == "yes") {
							queryRecord();
						}
					});
									
								} else {
									queryRecord();
								}

							*/
								queryRecord();
							}
						}],//, "-",confirmBtn 隐藏页面的“确认”按钮 modify by ywliu 090717
				enableColumnMove : false,		
				// 分页
				bbar : new Ext.PagingToolbar({
							pageSize : Constants.PAGE_SIZE,
							store : arrivalOrderStore,
							displayInfo : true,
							displayMsg : '共 {2} 条',
							emptyMsg : Constants.EMPTY_MSG
						})
			});
	// arrivalOrderGrid.render();
	arrivalOrderGrid.on("rowdblclick", showRightPurchaseWarehouse);

	// 事务作用码
	var bussinessCode = new Ext.form.TextField({
				id : "bussinessCode",
				xtype : "textfield",
				fieldLabel : '事务作用码',
				readOnly : true,
				anchor : "100%",
				name : ''
			});

	// 事务作用名称
	var bussinessName = new Ext.form.TextField({
				id : "bussinessName",
				xtype : "textfield",
				fieldLabel : '事务作用名称',
				readOnly : true,
				anchor : "100%",
				name : ''
			});
	// 到货单号
	var arrivalOrderCode = new Ext.form.TextField({
				id : "arrivalOrderCode",
				xtype : "textfield",
				fieldLabel : '到货单号',
				readOnly : true,
				anchor : "100%",
				name : ''
			});
	// 采购单号
	var purchaseOrderCode = new Ext.form.TextField({
				id : "purchaseOrderCode",
				xtype : "textfield",
				fieldLabel : '采购单号',
				readOnly : true,
				anchor : "100%",
				name : ''
			});
	// 合同号
	var contactNo = new Ext.form.TextField({
				id : "contactNo",
				xtype : "textfield",
				fieldLabel : '合同号',
				readOnly : true,
				anchor : "100%",
				name : ''
			});

	// 供应商
	var supplyName = new Ext.form.TextField({
				id : "supplyName",
				xtype : "textfield",
				fieldLabel : '供应商',
				readOnly : true,
				anchor : "100%",
				name : ''
			});
	// 日期
	var operateDate = new Ext.form.TextField({
				id : "operateDate",
				xtype : "textfield",
				fieldLabel : '日期',
				readOnly : true,
				anchor : "100%",
				name : ''
			});

	// 操作员
	var operator = new Ext.form.TextField({
				id : "operator",
				xtype : "textfield",
				fieldLabel : '操作员',
				readOnly : true,
				anchor : "100%",
				name : ''
			});

	// 备注
	var detailMemo = new Ext.form.TextArea({
				id : "detailMemo",
				fieldLabel : '备注',
				height:Constants.TEXTAREA_HEIGHT,
				maxLength:128,
				readOnly : false,
				anchor : "94.5%",
				name : ''
			});
	// 备注
	var detailMemoHidden = new Ext.form.Hidden({
				id : "detailMemoHidden",
				name : 'detailMemoHidden'
			});
	var id = new Ext.form.Hidden({
				id : "id",
				name : 'id',
				value:''
			});

	var rightData = Ext.data.Record.create([
			// 到货登记/验收表流水号
			{
		name : 'id'
	},
			// 到货登记/验收表.单据编号
			{
				name : 'arrivalNo'
			},
			// 到货登记/验收明细表.备注
			{
				name : 'memo'
			},
			// 到货登记/验收明细表.批次
			{
				name : 'lotCode'
			},
			// 到货登记/验收明细表.已收数量
			{
				name : 'rcvQty'
			},
			// 到货登记/验收明细表.操作人
			{
				name : 'operateBy'
			},
			// 物料主文件.流水号
			{
				name : 'materialId'
			},
			// 物料主文件.编码
			{
				name : 'materialNo'
			},
			// 物料主文件.名称
			{
				name : 'materialName'
			},
			// 物料主文件.是否批控制
			{
				name : 'isLot'
			},
			// 物料主文件.物料分类
			{
				name : 'materialClassId'
			},
			// 物料主文件.计价方式
			{
				name : 'costMethod'
			},
			// 物料主文件.标准成本
			{
				name : 'stdCost'
			},
			// 物料主文件.缺省仓库编码
			{
				name : 'defaultWhsNo'
			},
			// 物料主文件.缺省库位编码
			{
				name : 'defaultLocationNo'
			},// 物料主文件.规格型号
			{
				name : 'specNo'
			},// 物料主文件.存货计量单位
			{
				name : 'stockUmId'
			},// 物料主文件.计划价格
			{
				name : 'frozenCost'
			},// 采购订单明细表.流水号
			{
				name : 'purOrderDetailsId'
			},// 采购订单明细表.采购数量
			{
				name : 'purQty'
			},// 采购订单明细表.已收数
			{
				name : 'purOrderDetailsRcvQty'
			},// 待入库数
			{
				name : 'waitQty'
			},// 本次入库数
			{
				name : 'thisQty'
			},// 采购订单明细表.单价
			{
				name : 'unitPrice'
			},// 采购订单明细表.交期
			{
				name : 'dueDate'
			},// 采购订单明细表.税率
			{
				name : 'taxRate'
			},// 采购订单明细表.币别
			{
				name : 'currencyType'
			},// 采购订单明细表.生产厂家
			{
				name : 'factory'
			},// 保管员
			{
				name : 'saveName'
			},// 仓库编码
			{
				name : 'whsNo'
			},// 仓位编码
			{
				name : 'locationNo'
			},// 备注
			{
				name : 'gridMemo'
			},// 暂收数量
			{
				name : 'insqty'
			},
			{
				name : 'arrivalDetailID'
			},// 
			{
				name : 'recQty'
			},// 
			{
				name : 'arrivalDetailModifiedDate'
			},// 
			{
				name : 'orderDetailModifiedDate'
			},// 
			{
				name : 'materialModifiedDate'
			},{
				name :'canQty'
			}
			,{name:'itemStatus'}
			]);
	// 采购详细列表的store
	var rightStore = new Ext.data.JsonStore({
				url : 'resource/getArrivalDetailListForArrivalCheck.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : rightData
			});
			rightStore.setDefaultSort('materialNo', 'ASC');
			rightStore.on('beforeload', function() {
				Ext.apply(this.baseParams, {
							id : id.getValue()
						});
			});
	//----------add by fyyang 090722----增加合计列------
			rightStore.on("load",function()
			{
				var totalPurQty = 0; // 采购数量
				var totalpurOrderDetailsRcvQty = 0; // 已入库数量
				var totalcanQty = 0; // 到货数量
				for (var j = 0; j < rightStore.getCount(); j++) {
					var temp = rightStore.getAt(j);
					if (temp.get("purQty") != null) {
						totalPurQty = parseFloat(totalPurQty)
								+ parseFloat(temp.get("purQty"));
					}
					if (temp.get("purOrderDetailsRcvQty") != null) {
						totalpurOrderDetailsRcvQty = parseFloat(totalpurOrderDetailsRcvQty)
								+ parseFloat(temp.get("purOrderDetailsRcvQty"));
					}
					if (temp.get("canQty") != null) {
						totalcanQty = parseFloat(totalcanQty)
								+ parseFloat(temp.get("canQty"));
					}
					
				}
			    var mydata=new rightData({
			    	stockUmId:null,
			    	itemStatus :'',
			    purQty:totalPurQty,
			    purOrderDetailsRcvQty:totalpurOrderDetailsRcvQty,
			    canQty:totalcanQty
			    });
			    rightStore.add(mydata);
			    rightGrid.getView().refresh();
			});
	//--------------------------------------------------		
			
	// 采购详细列表的store
	var rightStoreCopy = new Ext.data.JsonStore({
				url : 'resource/getArrivalDetailListForArrivalCheck.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : rightData
			});	
		rightStoreCopy.setDefaultSort('materialNo', 'ASC');
	var warehouseStore = new Ext.data.JsonStore({
				root : 'list',
				url : "resource/getWarehouseList.action",
				fields : ['whsNo', 'whsName']
			});
	warehouseStore.load({
						params : {
							flag : 1
						}
					});
	var locationStore = new Ext.data.JsonStore({
				root : 'list',
				url : "resource/getLocationList.action",
				fields : ['locationNo', 'locationName']
			})


	// 采购详细列表grid
	var rightGrid = new Ext.grid.EditorGridPanel({
				region : "center",
				layout : 'fit',				
				style:"border-top:solid 1px",
				//height : 275,
				//anchor : "100%",
				autoScroll:true,
				enableColumnMove : false,
				clicksToEdit : 1,
				border:false,
				store : rightStore,
				//add------------------
				listeners : {
				'beforeedit' : function(e) { 
					if (e.field == "itemStatus")
						{ var column = e.record.get('materialNo'); 
						  if(column==null||column=="undefined")
						  {
						  	
						  	return false;
						  }
						}
				}
					
				},
				//addend--------------------
				columns : [new Ext.grid.RowNumberer({
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
						// 规格型号
						{
							header : "规格型号",
							width : 75,
							sortable : true,
							dataIndex : 'specNo'
						},// 单位
						{
							header : "单位",
							width : 50,
							sortable : true,
							renderer:unitName,
							dataIndex : 'stockUmId'
						}
						,
						// 采购数
						{
							header : "采购数",
							width : 50,
							sortable : true,
							renderer : numberFormat,
							align : 'right',
							dataIndex : 'purQty'
						},
						// 已入库数
						{
							header : "已入库数",
							width : 75,
							sortable : true,
							renderer : numberFormat,
							align : 'right',
							dataIndex : 'purOrderDetailsRcvQty'
						},
						// 待入库数 canQty
						{
							header : "待入库数",
							width : 75,
							sortable : true,
							hidden :true,
							renderer : numberFormat,
							align : 'right',
							dataIndex : 'waitQty'
						},{
							header : "到货数",
							width : 75,
							sortable : true,
							renderer : numberFormat,
							align : 'right',
							dataIndex : 'canQty'
						}
  					    ,
//						// 本次入库数
//						{
//							header : "本次入库数<font color='red'>*</font>",
//							width : 85,
//							sortable : true,
//							css:CSS_GRID_INPUT_COL,
//							align : 'right',
//							editor : new Ext.form.NumberField({
//								
//							}),
//							renderer : numberFormat,
//							dataIndex : 'thisQty'
//						},
//						// 批号
//						{
//							header : "批号",
//							width : 50,
//							sortable : true,
//							dataIndex : 'lotCode'
//						},
//						// 仓库
//						{
//							header : "仓库",
//							width : 100,
//							sortable : true,
//							css:CSS_GRID_INPUT_COL,
//							renderer : comboBoxWarehouseRenderer,
//							editor : new Ext.form.ComboBox({
//								store : warehouseStore,
//								displayField : "whsName",
//								valueField : "whsNo",
//								mode : 'local',
//
//								triggerAction : 'all',
//								readOnly : true
//									
//								}),
//							dataIndex : 'whsNo'
//						},
//						// 库位
//						{
//							header : "库位",
//							width : 100,
//							sortable : true,
//							css:CSS_GRID_INPUT_COL,
//							renderer : comboBoxLocationRenderer,
//							editor : new Ext.form.ComboBox({
//								store : locationStore,
//								displayField : "locationName",
//								valueField : "locationNo",
//								mode : 'local',
//
//								triggerAction : 'all',
//
//								readOnly : true
//									// width : 80
//								}),
//							dataIndex : 'locationNo'
//						},
//						// 交期
//						{
//							header : "交期",
//							width : 80,
//							sortable : true,
//							dataIndex : 'dueDate'
//						},
//						// 保管员
//						{
//							header : "保管员",
//							width : 50,
//							sortable : true,
//							dataIndex : 'saveName'
//						},
						// 验收员
						{
							header : "验收员",
							width : 50,
							sortable : true,
							renderer:getOperateByName,
							dataIndex : 'operateBy'
						},

						//是否合格
						{
						header : "是否合格",
							width : 100,
							dataIndex:'itemStatus',
							css:CSS_GRID_INPUT_COL,
							renderer:function(value){
                             if(value=="1")
                             {
                             	return "合格";
                             }
                             else if(value==null||value=="3")
                             {
                              return "不合格";	
                             }
                             else if(value=="")
                             {
                             	return "";
                             }

		              },
							editor:new Ext.form.ComboBox({
										//fieldLabel : '是否叶子',
										store : new Ext.data.SimpleStore({
											fields : ["retrunValue", "displayText"],
											data : [['0', ''],['1', '合格'], ['3', '不合格']]
										}),
										valueField : "retrunValue",
										displayField : "displayText",
										mode : 'local',
										forceSelection : true,
										value : '1',
										editable : false,
										triggerAction : 'all',
										selectOnFocus : true
											
									         })
						}
						],

				autoSizeColumns : true

			});
	
		
			
     // 备注
    var memoText = new Ext.form.TextArea({
         id : "memoText",
         maxLength:128,
        width : 180
    });
	
    // 弹出画面
	 var win = new Ext.Window({
		height : 170,
		width : 350,
		layout : 'fit',
		modal:true,
		resizable : false,
		closeAction : 'hide',
		items : [memoText],
		buttonAlign : "center",
		title : '详细信息录入窗口',
		buttons : [{
			text : Constants.BTN_SAVE,
			iconCls : Constants.CLS_SAVE,
			handler : function() {
				var rec = rightGrid.selModel.getSelectedCell();
				var record = rightGrid.getStore().getAt(rec[0]);
				// BG eBT_PowrERP_UTBUG_RS001_026
				if(Ext.get("memoText").dom.value.length<=128){
					
				record.set("gridMemo", Ext.get("memoText").dom.value);
				win.hide();
				}
			}
		},{
			text : Constants.BTN_CANCEL,
			iconCls : Constants.CLS_CANCEL,
			handler : function() {
				win.hide();
			}
		}]
	});		
	// 		
	var fieldFlag = new Ext.form.Hidden({
				id : "fieldFlag",
				name : "fieldFlag",
				value : ""
			});
	var whsNoFlag = new Ext.form.Hidden({
				id : "whsNoFlag",
				name : "whsNoFlag"
			});
	var locationNoFlag = new Ext.form.Hidden({
				id : "locationNoFlag",
				name : "locationNoFlag"
			});
	var arrivalDetailIDFlag = new Ext.form.Hidden({
				id : "arrivalDetailIDFlag",
				name : "arrivalDetailIDFlag"
			});



	// 物资详细grid双击处理
    function cellClickHandler(/*grid, rowIndex, columnIndex, e*/) {
   
    	var rec = rightGrid.selModel.getSelectedCell();
		var record = rightGrid.getStore().getAt(rec[0]);
		win.show();
       	memoText.setValue(record.get("gridMemo"));
    }

	


	/**
	 * 格式化数据
	 */
	function unitName(value) {
		if(value!==null&&value!==""){
		var url = "resource/getRS001UnitName.action?unitCode=" + value;
		var conn = Ext.lib.Ajax.getConnectionObject().conn;
		conn.open("POST", url, false);
		conn.send(null);
		return conn.responseText;
		}else{
			return "";
		}
		
	};
	
	function getOperateByName(value){
		if(value!==null&&value!==""){
		var url = "resource/getRS001OperaterByName.action?operateBy=" + value;
		var conn = Ext.lib.Ajax.getConnectionObject().conn;
		conn.open("POST", url, false);
		conn.send(null);
		return conn.responseText;
		}else{
			return "";
		}
	}
	/**
	 * 格式化数据
	 */
	function comboBoxWarehouseRenderer(value, cellmeta, record) {
		var whsNo = record.data["whsNo"];

		var url = "resource/getWarehouseName.action?whsNo=" + whsNo;
		var conn = Ext.lib.Ajax.getConnectionObject().conn;
		conn.open("POST", url, false);
		conn.send(null);
		return conn.responseText;

	};
	/**
	 * 格式化数据
	 */
	function comboBoxLocationRenderer(value, cellmeta, record) {
		var whsNo = record.data["whsNo"];
		var locationNo = record.data["locationNo"];
		var url = "resource/getLocationName.action?whsNo=" + whsNo
				+ "&locationNo=" + locationNo;
		var conn = Ext.lib.Ajax.getConnectionObject().conn;
		conn.open("POST", url, false);
		conn.send(null);
		return conn.responseText;

	}

	var headForm = new Ext.form.FieldSet({
				border : false,
				labelAlign : 'right',
				style : {
					'border' : 0
				},
				layout : 'form',
                labelWidth : 90,
				width : 550,
				autoHeight : true,
				// BG RS001-002
				items : [{
							layout : "column",
							style : "padding-top:5px",
							border : false,
							items : [{
										columnWidth : 0.45,
										layout : "form",
										border : false,
										height : 30,
										labelAlign : "right",
										items : [bussinessCode]
									}, {
										columnWidth : 0.45,
										layout : "form",
										border : false,
										height : 30,
										labelAlign : "right",
										items : [bussinessName]
									}]
						}, {
							layout : "column",
							border : false,
							items : [{
										columnWidth : 0.45,
										layout : "form",
										border : false,
										height : 30,
										labelAlign : "right",
										items : [arrivalOrderCode]
									}, {
										columnWidth : 0.45,
										layout : "form",
										border : false,
										height : 30,
										labelAlign : "right",
										items : [purchaseOrderCode]
									}]
						}, {
							layout : "column",
							border : false,
							items : [{
										columnWidth : 0.45,
										layout : "form",
										border : false,
										height : 30,
										labelAlign : "right",
										items : [contactNo]
									}, {
										columnWidth : 0.45,
										layout : "form",
										border : false,
										height : 30,
										labelAlign : "right",
										items : [supplyName]
									}]
						}, {
							layout : "column",
							border : false,
							items : [{
										columnWidth : 0.45,
										layout : "form",
										border : false,
										height : 30,
										labelAlign : "right",
										items : [operateDate]
									}, {
										columnWidth : 0.45,
										layout : "form",
										border : false,
										height : 30,
										labelAlign : "right",
										items : [operator]
									}]
						}, {
							layout : "column",
							border : false,
							items : [{
										columnWidth : 0.947,
										layout : "form",
										border : false,
										height : 80,
										labelAlign : "right",
										items : [detailMemo]
									}]
						}]

			});

	 // 明细panel
	var detailPanel = new Ext.FormPanel({
				border : false,
				region : 'north',
				height : 180,
				style : "padding-top:1.5px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
				labelAlign : 'right',
				items : [headForm]
			});
			
	var leftPanel = new Ext.Panel({
				region : 'west',
				layout : 'fit',
				width : 300,
				autoScroll : true,
				border:false,
				containerScroll : true,
				collapsible : true,
				split : false,
				items : [arrivalOrderGrid]
			});
	// 右边的panel
	var rightPanel = new Ext.Panel({
				region : "center",
				// autoScroll:true,
				layout : 'border',
				border:true,
				items : [detailPanel,rightGrid],
				tbar : [{
							text : "确认验收",
							iconCls : Constants.CLS_SAVE,
							handler : function() {
								confirmPurchaseWarehouse();
							}
						}]

			});
	// 显示区域
	var view = new Ext.Viewport({
				enableTabScroll : true,
				autoScroll:true,
				layout : "border",
				items : [leftPanel, rightPanel]
			})

	// ↓↓*******************************处理****************************************
	/**
	 * 检查数据是否改变
	 */
	function checkIsChanged() {
		return false;
	}
	/**
	 * 查询操作
	 */
	function queryRecord() {
		arrivalOrderStore.load({
					params : {
						start : 0,
						limit : Constants.PAGE_SIZE
					}
				});
		
	}
	/**
	 * 获取当前日期
	 */
	function getCurrentDate() {
		var d, s, t;
	d = new Date();
	s = d.getFullYear().toString(10) + "-";
	t = d.getMonth() + 1;
	s += (t > 9 ? "" : "0") + t + "-";
	t = d.getDate();
	s += (t > 9 ? "" : "0") + t + " ";
	return s;

	}
	/**
	 * 获取事务名称
	 */
	function getTranctionName() {
		Ext.lib.Ajax.request('POST', 'resource/getTransName.action', {
					success : function(action) {
						
						bussinessName.setValue(action.responseText);
					}
				}, "transCode=" + 'C');
	}

	/**
	 * 双击或点击确认按钮处理
	 */
	function showRightPurchaseWarehouse() {
		if (rightGridDetailIsChanged()) {
			
			Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_C_004, function(buttonobj) {
						if (buttonobj == "yes") {
							showRightGrid();
						}
					});
		} else {
			showRightGrid();
		}
		// 显示详细信息
		function showRightGrid(){
			Ext.Msg.wait("正在查询数据，请等待...");
			if (arrivalOrderGrid.selModel.hasSelection()) {

			var record = arrivalOrderGrid.getSelectionModel().getSelected();
			// 事务作用码
			bussinessCode.setValue('C');
			// 事务作用名称
			getTranctionName();
			
			// 到货单号
			arrivalOrderCode.setValue(record.data.arrivalNo);
			// 采购单号
			purchaseOrderCode.setValue(record.data.purNo);
			// 合同号
			contactNo.setValue(record.data.contractNo);
			// 供应商
			supplyName.setValue(record.data.supplyName);
			// 日期
			operateDate.setValue(getCurrentDate());
			// 操作员
			operator.setValue(record.data.loginName);
			if(record.data.memo==null){
				detailMemo.setValue("");
				detailMemoHidden.setValue("");
			}else{
			// 备注
			detailMemo.setValue(record.data.memo);
			// 备注保存
			detailMemoHidden.setValue(record.data.memo);
			}
			// 流水号
			id.setValue(record.data.id);
			purchaseOrderDetail(record.data.id);
			
			

		} else {
			
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_001);
		}
//		Ext.Msg.hide();
		}
		

	}
	/**
	 * 显示详细入库信息
	 */
	function purchaseOrderDetail(id) {
		rightStore.load({
					params : {
						start : 0,
						limit : Constants.PAGE_SIZE,
						id : id
					}
				});
		rightStoreCopy.load({
					params : {
						start : 0,
						limit : Constants.PAGE_SIZE,
						id : id
					}
				});		
				
	}

	rightStore.on("load",function(){
		Ext.Msg.hide(); 
	});
	
	function confirmPurchaseWarehouse() {
		
				
				
		if(id.getValue()===""){
				return ;
			}
		if(!rightGridDetailIsChanged()){
			
			Ext.Msg.alert(Constants.SYS_REMIND_MSG,Constants.COM_I_006);
		}else{
			
		Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_C_001, function(buttonobj) {
			if (buttonobj == "yes") {
				// 对应式样变更
			
			var array = getPurchaseWarehousDetailList();
				if(array.length!=0||detailMemoIsChanged()===1){
					// BG RS001-027
				if(detailMemo.getValue().length>128){
					return ;
				}
				
				
				Ext.Ajax.request({
								method:'POST',
								url:'resource/updateForArrivalCheck.action',
								params : {
                        data : Ext.util.JSON.encode(getPurchaseWarehousDetailList()),
                        detailMemo :detailMemo.getValue(),
                        memoFlag : detailMemoIsChanged(),
                        id : id.getValue()
                       
                    },
									success : function(action) {
									
										var o = eval('(' + action.responseText + ')');
										if(o.msg=="OK")
										{
											Ext.Msg.alert(Constants.SYS_REMIND_MSG,
													'保存成功！');
											arrivalOrderStore.load({
														params : {
															start : 0,
															limit : Constants.PAGE_SIZE
														}
													});
											detailPanel.getForm().reset();	
											rightStore.removeAll();
//											rightGrid.getBottomToolbar().updateInfo();
											
											id.setValue("");
										}
									}
								});
				}
			}

		});
		}
	}
	function getPurchaseWarehousDetailList() {

		var modifyRec = rightGrid.getStore().getModifiedRecords(); 
		var modifyRecords = new Array(); 
			for (var i = 0; i < modifyRec.length; i++) { 
				modifyRecords.push(modifyRec[i].data); 
			}
	   return modifyRecords;		
		
	}

	/**
	 * 备注是否被修改
	 */
	function detailMemoIsChanged() {
		if (detailMemoHidden.getValue() == detailMemo.getValue()) {
			return 0;
		} else {
			return 1;
		}
	}
	/**
	 * 详细列表的grid内容是否有变更
	 */
	function rightGridDetailIsChanged(){
		
		if(id.getValue()===""){
			return false;
		}
		if(rightStore.getCount()==0 && detailMemo.getValue()==""){
			return false;
		}else{
			
				if(detailMemo.getValue()!=detailMemoHidden.getValue()){
					
					return true;
				}else{
					var flag = false;
					for(var i = 0;i<rightStore.getCount();i++){
					
						
						if (rightStore.getAt(i).get("itemStatus") !== rightStoreCopy
								.getAt(i).get("itemStatus")) {
									
							flag = true;
							break;
						}
			
						
					}
					return flag;
				}
			
		}
		
	}

})
