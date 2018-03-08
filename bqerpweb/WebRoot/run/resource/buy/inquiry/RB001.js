// 物料询价管理
// author:sufeiyu

Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.QuickTips.init();
Ext.onReady(function() {
	var strMethod;
	var strMsg;
	// ============== 定义grid ===============
	var MyRecord = Ext.data.Record.create([{
				name : 'quotationId'
			}, {
				name : 'materialId'
			}, {
				name : 'unitId'
			}, {
				name : 'unitName'
			}, {
				name : 'materialNo'
			}, {
				name : 'materialName'
			}, {
				name : 'specNo'
			}, {
				name : 'parameter'
			}, {
				name : 'supplier'
			}, {
				name : 'clientCode'
			}, {
				name : 'supplyName'
			}, {
				name : 'quotedQty'
			}, {
				name : 'quotedPrice'
			}, {
				name : 'quotationCurrency'
			}, {
				name : 'currencyName'
			}, {
				name : 'effectiveDate'
			}, {
				name : 'discontinueDate'
			}, {
				name : 'lastModifiedDate'
			}, {
				name : 'memo'
			}]);

	// 定义获取数据源
	var dataProxy = new Ext.data.HttpProxy({
				url : 'resource/getQuotationList.action'
			});

	// 定义格式化数据
	var theReader = new Ext.data.JsonReader({
				root : "list",
				totalProperty : "totalCount"
			}, MyRecord);

	// 定义封装缓存数据的对象
	var queryStore = new Ext.data.Store({
				// 访问的对象
				proxy : dataProxy,
				// 处理数据的对象
				reader : theReader,
				sortInfo : {
					field : "materialNo",
					direction : "ASC"
				}
			});
	queryStore.load({
				params : {
					start : 0,
					limit : Constants.PAGE_SIZE
				}
			});

	// -----------------控件定义----------------------------------
	// 查询参数
	var txtFuzzy1 = new Ext.form.TextField({
				id : "fuzzy",
				name : "fuzzy",
				width : 235,
				emptyText : "物料编码/物料名称/供应商编码/供应商名称"
			});

	//定义选择列
	var sm = new Ext.grid.RowSelectionModel({
				singleSelect : true
			});

	// --gridpanel显示格式定义-----开始-------------------
	var grid = new Ext.grid.GridPanel({
				region : "center",
				layout : 'fit',
				store : queryStore,
				columns : [
						//自动行号
						new Ext.grid.RowNumberer({
									header : "行号",
									width : 31
								}), {
							header : "流水号",
							hidden : true,
							dataIndex : 'quotationId'
						}, {
							header : "物料ID",
							hidden : true,
							dataIndex : 'materialId'
						}, {
							header : "物料编码",
							sortable : true,
							width : 65,
							dataIndex : 'materialNo'
						}, {
							header : "物料名称",
							sortable : true,
							width : 70,
							dataIndex : 'materialName'
						}, {
							header : "规格型号",
							sortable : true,
							width : 65,
							dataIndex : 'specNo'
						}, {
							header : "材质/参数",
							sortable : true,
							width : 65,
							dataIndex : 'parameter'
						}, {
							header : "供应商编码",
							sortable : true,
							width : 75,
							dataIndex : 'clientCode'
						}, {
							header : "供应商名称",
							sortable : true,
							width : 75,
							dataIndex : 'supplyName'
						}, {
							header : "计量单位",
							sortable : true,
							width : 60,
							dataIndex : 'unitName'
						}, {
							header : "报价数量",
							sortable : true,
							align : 'right',
							renderer : divide2,
							width : 63,
							dataIndex : 'quotedQty'
						}, {
							header : "报价",
							sortable : true,
							headerAlign : 'left',
							align : 'right',
							renderer : divide4,
							width : 60,
							dataIndex : 'quotedPrice'
						}, {
							header : "供应商币别ID",
							hidden : true,
							dataIndex : 'quotationCurrency'
						}, {
							header : "供应商币别",
							sortable : true,
							width : 72,
							dataIndex : 'currencyName'
						}, {
							header : "报价有效开始日期",
							sortable : true,
							width : 109,
							dataIndex : 'effectiveDate'
						}, {
							header : "报价有效截止日期",
							sortable : true,
							width : 109,
							dataIndex : 'discontinueDate'
						}, {
							header : "上次修改时间",
							hidden : true,
							dataIndex : 'lastModifiedDate'
						}, {
							header : "备注",
							sortable : true,
							width : 39,
							dataIndex : 'memo'
						}],
				sm : sm,
				autoScroll : true,
				enableColumnMove : false,
				autoSizeColumns : true,
				viewConfig : {
					forceFit : false
				},

				// 头部工具栏
				tbar : [txtFuzzy1, "-", {
							text : "模糊查询",
							iconCls : Constants.CLS_QUERY,
							handler : queryRecord
						}, "-", {
							text : "新增",
							iconCls : Constants.CLS_ADD,
							handler : addRecord
						}, "-", {
							text : Constants.BTN_UPDATE,
							iconCls : Constants.CLS_UPDATE,
							handler : updateRecord
						}, "-", {
							text : Constants.BTN_DELETE,
							iconCls : Constants.CLS_DELETE,
							handler : deleteRecord
						}],

				// 分页
				bbar : new Ext.PagingToolbar({
							pageSize : Constants.PAGE_SIZE,
							store : queryStore,
							displayInfo : true,
							displayMsg : Constants.DISPLAY_MSG,
							emptyMsg : Constants.EMPTY_MSG
						})
			});
	// --gridpanel显示格式定义-----结束--------------------

	// 注册双击事件
	grid.on("rowdblclick", updateRecord);

	// ***************************弹出窗口**************************//

	// 隐藏的流水号
	var hiddenQuotationID = {
		id : 'quotationId',
		xtype : 'hidden',
		readOnly : true,
		hidden : true,
		value : '0',
		name : 'quotation.quotationId'
	};
	// modify BY ywliu 09/04/29
	var hiddenGatherID = {
		id : 'gatherId',
		xtype : 'hidden',
		readOnly : true,
		hidden : true,
		value : '0',
		name : 'gatherId'
	};

	// 隐藏的物料ID
	var hiddenMaterialID = {
		id : 'materialId',
		xtype : 'hidden',
		readOnly : true,
		hidden : true,
		value : '0',
		name : 'quotation.materialId'
	};

	// 隐藏的计量单位ID
	var hiddenUnitId = {
		id : 'unitId',
		xtype : 'hidden',
		readOnly : true,
		hidden : true,
		value : '0',
		name : 'quotation.unitId'
	};

	// 隐藏的供应商币别ID
	var hiddenQuotationCurrency = {
		id : 'quotationCurrency',
		xtype : 'hidden',
		readOnly : true,
		hidden : true,
		value : '0',
		name : 'quotation.quotationCurrency'
	};

	// 物料编码
	var txtMaterialNo = new Ext.form.TextField({
				fieldLabel : "物料编码<font color='red'>*</font>",
				id : 'materialNo',
				isFormField : true,
				labelAlign : 'right',
				readOnly : true,
				width : 180,
				name : 'quotation.materialNo',
				allowBlank : false
			});

	// start jincong 单击时间修改 2008-01-10
	// 物料编码的单击事件
	txtMaterialNo.onClick(selectMaterial);
	// end jincong 单击时间修改 2008-01-10
	
	// 物料名称
	var txtMaterialName = new Ext.form.TextField({
				fieldLabel : '物料名称',
				id : 'materialName',
				isFormField : true,
				labelAlign : 'right',
				readOnly : true,
				width : 180,
				name : 'quotation.materialName'
			});

	// 规格型号
	var txtSpecNo = new Ext.form.TextField({
				fieldLabel : '规格型号',
				id : 'specNo',
				isFormField : true,
				labelAlign : 'right',
				readOnly : true,
				width : 180,
				name : 'quotation.specNo'
			});

	// 材质/参数
	var txtParameter = new Ext.form.TextField({
				fieldLabel : '材质/参数',
				id : 'parameter',
				isFormField : true,
				labelAlign : 'right',
				readOnly : true,
				width : 180,
				name : 'quotation.parameter'
			});

	// 供应商编码
	var txtSupplier = new Ext.form.TextField({
				fieldLabel : "供应商编码<font color='red'>*</font>",
				id : 'clientCode',
				isFormField : true,
				labelAlign : 'right',
				readOnly : true,
				width : 180,
				allowBlank : false
			});
			
	// start jincong 单击时间修改 2008-01-10
	// 供应商编码的单击事件
	txtSupplier.onClick(selectSupplier);
	// end jincong 单击时间修改 2008-01-10
			
	// 隐藏的供应商编码
	var hiddenTxtSupplier = {
		id : 'supplier',
		xtype : 'hidden',
		readOnly : true,
		hidden : true,
		name : 'quotation.supplier'
	};

	// 供应商名称
	var txtSupplyName = new Ext.form.TextField({
				fieldLabel : '供应商名称',
				id : 'supplyName',
				isFormField : true,
				labelAlign : 'right',
				readOnly : true,
				width : 180,
				name : 'quotation.supplyName'
			});

	// 计量单位	
	var unitStore = new Ext.data.JsonStore({
				url : 'resource/getAllUnitList.action',
				root : 'list',
				fields : [{
							name : 'unitId'
						}, {
							name : 'unitName'
						}]
			});

	// 计量单位下拉框
	var cbUnitName = new Ext.form.ComboBox({
				id : 'unitName',
				fieldLabel : "计量单位<font color='red'>*</font>",
				allowBlank : false,
				labelAlign : 'right',
				triggerAction : 'all',
				store : unitStore,
				displayField : 'unitName',
				valueField : 'unitId',
				mode : 'remote',
				width : 180,
				readOnly : true,
				name : 'unitName',
				listeners : {
					"select" : function() {
						Ext.get("unitId").dom.value = cbUnitName
								.getValue();
					}
				}
			});
			

	// 报价数量
	var nbQuotedQty = new Powererp.form.NumField({
				fieldLabel : "报价数量<font color='red'>*</font>",
				id : 'quotedQty',
				isFormField : true,
				allowBlank : false,
				labelAlign : 'right',
				//精度
				decimalPrecision : 2,
				minValue : 0.01,
				maxValue : 9999999.99,
				allowNegative : false,
				style : 'text-align:right',
				padding : 2,
				width : 180,
				name : 'quotedQty'
			});

	// 报价数量隐藏
	var hdQuotedQty = {
		id : "numquotedQty",
		name : "quotation.quotedQty",
		xtype : 'hidden',
		readOnly : true,
		hidden : true,
		value : '0'
	}

	// 报价
	var nbQuotedPrice = new Powererp.form.NumField({
				fieldLabel : "报价<font color='red'>*</font>",
				id : 'quotedPrice',
				isFormField : true,
				allowBlank : false,
				labelAlign : 'right',
				//精度
				decimalPrecision : 4,
				padding : 4,
				minValue : 0.0001,
				maxValue : 9999999999999.9999,
				allowNegative : false,
				style : 'text-align:right',
				width : 180,
				name : 'quotedPrice'
			});

	// 报价隐藏
	var hdQuotedPrice = {
		id : "numquotedPrice",
		name : "quotation.quotedPrice",
		xtype : 'hidden',
		readOnly : true,
		hidden : true,
		value : '0'
	}

	// 币种检索
	var currencyStore = new Ext.data.JsonStore({
				url : 'resource/getCurrency.action',
				root : 'list',
				fields : [{
							name : 'currencyDesc'
						}, {
							name : 'currencyId'
						}, {
							name : 'currencyName'
						}, {
							name : 'currencyNo'
						}, {
							name : 'enterpriseCode'
						}, {
							name : 'isUse'
						}, {
							name : 'lastModifiedBy'
						}, {
							name : 'lastModifiedDate'
						}]
			});
		currencyStore.on('load', function() {
    		// 默认为人民币
    		//add by fyyang 090514
    		cbCurrencyName.setValue(2);
    	});
             currencyStore.load();
	// 供应商币别下拉框
	var cbCurrencyName = new Ext.form.ComboBox({
				id : 'currencyName',
				fieldLabel : "供应商币别<font color='red'>*</font>",
				allowBlank : false,
				labelAlign : 'right',
				triggerAction : 'all',
				store : currencyStore,
				displayField : 'currencyName',
				valueField : 'currencyId',
				mode : 'remote',
				width : 180,
				emptyText : '',
				readOnly : true,
				name : 'quotation.currencyName',
				listeners : {
					"select" : function() {
						Ext.get("quotationCurrency").dom.value = cbCurrencyName
								.getValue();
					}
				}
			});

	// 报价有效开始日期
	var dteEffectiveDate = new Ext.form.TextField({
				id : 'effectiveDate',
				fieldLabel : "有效开始日期<font color='red'>*</font>",
				name : 'quotation.effectiveDate',
				style : 'cursor:pointer',
				width : 180,
				readOnly : true,
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd',
									alwaysUseStartDate : false,
				                    onpicked : function(){
				                        dteEffectiveDate.clearInvalid();
				                    },
				                    onclearing:function(){
				                    	dteEffectiveDate.markInvalid();
				                    }
								});
					}
				}
			})

	// 报价有效截止日期
	var dteDiscontinueDate = new Ext.form.TextField({
				id : 'discontinueDate',
				fieldLabel : "有效截止日期<font color='red'>*</font>",
				name : 'quotation.discontinueDate',
				style : 'cursor:pointer',
				width : 180,
				readOnly : true,
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd',
									alwaysUseStartDate : false,
				                    onpicked : function(){
				                        dteDiscontinueDate.clearInvalid();
				                    },
				                    onclearing:function(){
				                    	dteDiscontinueDate.markInvalid();
				                    }
								});
					}
				}
			})

	// 隐藏的上次修改时间
	var hiddenlastModifiedDate = {
		id : 'lastModifiedDate',
		xtype : 'hidden',
		readOnly : true,
		hidden : true,
		value : '0',
		name : 'lastModifiedDate'
	};

	// 备注
	var txaMemo = new Ext.form.TextArea({
				fieldLabel : '备注',
				id : 'memo',
				isFormField : true,
				maxLength : 250,
				labelAlign : 'right',
				width : 180,
				height : Constants.TEXTAREA_HEIGHT,
				name : 'quotation.memo'
			});

	// Form
	var addPanel = new Ext.FormPanel({
				frame : true,
				labelAlign : 'right',
				// modify BY ywliu 09/04/29 add hiddenGatherID
				items : [hiddenQuotationID,hiddenGatherID, hiddenMaterialID, txtMaterialNo,
						hiddenQuotationCurrency, txtMaterialName, txtSpecNo,
						txtParameter, txtSupplier,hiddenTxtSupplier, hiddenUnitId, txtSupplyName,
						cbUnitName,nbQuotedQty, nbQuotedPrice, cbCurrencyName,
						dteEffectiveDate, dteDiscontinueDate,
						hiddenlastModifiedDate, txaMemo, hdQuotedQty,
						hdQuotedPrice]
			});

	// 弹出窗口
	var win = new Ext.Window({
				width : 360,
				height : 470,
				modal : true,
				title : '增加/修改',
				buttonAlign : 'center',
				items : [addPanel],
				layout : 'fit',
				closeAction : 'hide',
				resizable : false,
				buttons : [{
							text : Constants.BTN_SAVE,
							iconCls : Constants.CLS_SAVE,
							buttonAlign : 'center',
							handler : confirmRecord
						}, {
							text : Constants.BTN_CANCEL,
							iconCls : Constants.CLS_CANCEL,
							buttonAlign : 'center',
							handler : function() {
								win.hide();
							}
						}]
			});

	/**
	 * 
	 *  查询记录 
	 */
	function queryRecord() {
		// 查询参数
		var strFuzzytext = txtFuzzy1.getValue();
		queryStore.baseParams = {
			fuzzy : strFuzzytext
		};
		queryStore.load({
					params : {
						start : 0,
						limit : Constants.PAGE_SIZE
					}
				});
		grid.getView().refresh();
	}

	/**
	 * 
	 *  增加记录 
	 */
	function addRecord() {
		strMethod = "add";
		addPanel.getForm().reset();
		cbCurrencyName.setDisabled(false);
		win.setTitle("新增物料询价");
		win.show();
	}

	/**
	 * 
	 *  在物资名称查询及选择画面选择（共通控件） 
	 */
	function selectMaterial() {
		if (strMethod == "add") {
			var mate = window.showModalDialog('../../plan/query/MRPGatherInformation.jsp', null,
					'dialogWidth=800px;dialogHeight=550px;center=yes;help=no;resizable=no;status=no');	
		    if (typeof(mate) != "undefined") {
				Ext.get('materialNo').dom.value = mate.materialNo;				
				Ext.get('materialId').dom.value = mate.materialId;
				Ext.get('unitName').dom.value = mate.stockUmId == null
						? ""
						: mate.stockUmId;
				// start jincong 增加单位ID 2008-01-10
				Ext.get('unitId').dom.value = mate.stock == null
						? '0'
						: mate.stock;
				// start jincong 增加单位ID 2008-01-10
				Ext.get('materialName').dom.value = mate.materialName == null
						? ""
						: mate.materialName;
				Ext.get('specNo').dom.value = mate.specNo == null
						? ""
						: mate.specNo;
				Ext.get('parameter').dom.value = mate.parameter == null
						? ""
						: mate.parameter;
				Ext.get('quotedQty').dom.value = mate.nbQuotedQty == null
						? ""
						: mate.nbQuotedQty;
				Ext.get('gatherId').dom.value = mate.gatherId == null
						? ""
						: mate.gatherId;
			} else {
				Ext.get('materialNo').dom.value = "";				
				Ext.get('materialId').dom.value = "";
				Ext.get('unitName').dom.value = "";
				Ext.get('materialName').dom.value = ""
				Ext.get('specNo').dom.value = ""
				Ext.get('parameter').dom.value = ""
			}
		}
	}

	/**
	 * 
	 *  在供应商查询Tab画面选择（共通控件） 
	 */
	function selectSupplier() {
		if (strMethod == "add") {
			var supply = window.showModalDialog(
					'../../../../comm/jsp/supplierQuery/supplierQuery.jsp',
					null, 'dialogWidth=800px;dialogHeight=600px;center=yes;help=no;resizable=no;status=no');
			if (typeof(supply) != "undefined") {
				Ext.get('supplier').dom.value = supply.supplierId;
				Ext.get('clientCode').dom.value = supply.supplier;
				Ext.get('supplyName').dom.value = supply.supplyName == null
						? ""
						: supply.supplyName;
			}
		}
	}

	/**
	 * 
	 *  修改记录
	 */
	function updateRecord() {
		var rec = grid.getSelectionModel().getSelected();
		if (!rec) {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_001);
			return false;
		} else {
			strMethod = "update";
			addPanel.getForm().reset();
			win.setTitle("修改物料询价");
			win.show();
			Ext.get('quotationId').dom.value = rec.get('quotationId');
			Ext.get('materialId').dom.value = rec.get('materialId');
			Ext.get('unitId').dom.value = rec.get('unitId');
			Ext.get('quotationCurrency').dom.value = rec
					.get('quotationCurrency');
			Ext.get('materialNo').dom.value = rec.get('materialNo');
			Ext.get('materialName').dom.value = rec.get('materialName') == null
					? ""
					: rec.data.materialName;
			Ext.get('specNo').dom.value = rec.get('specNo') == null
					? ""
					: rec.data.specNo;
			Ext.get('parameter').dom.value = rec.get('parameter') == null
					? ""
					: rec.data.parameter;
			Ext.get('supplier').dom.value = rec.get('supplier');
			Ext.get('clientCode').dom.value = rec.get('clientCode') == null
					? ""
					: rec.data.clientCode;
			Ext.get('supplyName').dom.value = rec.get('supplyName') == null
					? ""
					: rec.data.supplyName;
			Ext.get('unitName').dom.value = rec.get('unitName') == null
					? ""
					: rec.data.unitName;		
			Ext.get('numquotedQty').dom.value = rec.get('quotedQty');
			Ext.get('quotedQty').dom.value = divide2(rec.get('quotedQty'));
			Ext.get('numquotedPrice').dom.value = rec.get('quotedPrice');
			Ext.get('quotedPrice').dom.value = divide4(rec.get('quotedPrice'));
			Ext.get('currencyName').dom.value = rec.get('currencyName') == null
					? ""
					: rec.data.currencyName;		
			Ext.get('effectiveDate').dom.value = rec.get('effectiveDate');
			Ext.get('discontinueDate').dom.value = rec.get('discontinueDate');
			Ext.get('lastModifiedDate').dom.value = rec.get('lastModifiedDate');
			Ext.get('memo').dom.value = rec.get('memo') == null
					? ""
					: rec.data.memo;
			cbCurrencyName.setDisabled(false);
		}
	}

	/**
	 * 删除记录
	 */
	function deleteRecord() {
		var rec = grid.getSelectionModel().getSelected();
		if (rec) {
			var quotationId = rec.get('quotationId');
			var lastModifiedDate = rec.get('lastModifiedDate');
			Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_C_002,
					function(buttonobj) {
						if (buttonobj == 'yes') {
							// 刪除
							Ext.Ajax.request({
								method : Constants.POST,
								url : 'resource/deleteData.action',
								params : {
									quotationId : quotationId,
									lastModifiedDate : lastModifiedDate
								},
								success : function(result, request) {
									var o = eval("(" + result.responseText
											+ ")");
									//排他		
									if (o.msg == 'U') {
										Ext.Msg.alert(Constants.SYS_REMIND_MSG,
												Constants.COM_E_015);
									} else {
										Ext.Msg.alert(Constants.SYS_REMIND_MSG,
												Constants.COM_I_005);
										queryStore.load({
													params : {
														start : 0,
														limit : Constants.PAGE_SIZE
													}
												});
										grid.getView().refresh();
									}
								},
								failure : function() {
									Ext.Msg.alert(Constants.ERROR,
											Constants.DEL_ERROR);
								}
							});
						}
					});
		} else {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_001);
		}
	}

	/**
	 * 
	 * 时间的有效性检查
	 */
	function checkDate() {
		var effectiveDate = Ext.get('effectiveDate').dom.value;
		var dateStart = Date.parseDate(effectiveDate, 'Y-m-d');

		var discontinueDate = Ext.get('discontinueDate').dom.value;
		var dateEnd = Date.parseDate(discontinueDate, 'Y-m-d');

		if (dateStart.getTime() > dateEnd.getTime()) {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, String.format(Constants.COM_E_006,
							"报价有效开始日期", "报价有效截止日期"));
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 
	 *  在增加数据前的非空检查 
	 */
	function checkAddform() {
		strMsg = "";
		var materialNo = Ext.get('materialNo').dom.value;
		if (materialNo == "" || materialNo == null) {
			strMsg += String.format(Constants.COM_E_003, '物料编码 ') + "<br/>";
		}

		var supplier = Ext.get('supplier').dom.value;
		if (supplier == "" || supplier == null) {
			strMsg += String.format(Constants.COM_E_003, '供应商编码 ') + "<br/>";
		}

		var unitName = Ext.get('unitName').dom.value;
		if (unitName == "" || unitName == null) {
			strMsg += String.format(Constants.COM_E_003, '计量单位 ') + "<br/>";
		}

		var quotedQty = Ext.get('quotedQty').dom.value;
		if (quotedQty == "" || quotedQty == null) {
			strMsg += String.format(Constants.COM_E_003, '报价数量 ') + "<br/>";
		}

		var quotedPrice = Ext.get('quotedPrice').dom.value;
		if (quotedPrice == "" || quotedPrice == null) {
			strMsg += String.format(Constants.COM_E_003, '报价 ') + "<br/>";
		}

		var currencyName = Ext.get('currencyName').dom.value;
		if (currencyName == "" || currencyName == null) {
			strMsg += String.format(Constants.COM_E_003, '供应商币别 ') + "<br/>";
		}

		var effectiveDate = Ext.get('effectiveDate').dom.value;
		if (effectiveDate == "" || effectiveDate == null) {
			strMsg += String.format(Constants.COM_E_003, '报价有效开始日期') + "<br/>";
		}

		var discontinueDate = Ext.get('discontinueDate').dom.value;
		if (discontinueDate == "" || discontinueDate == null) {
			strMsg += String.format(Constants.COM_E_003, '报价有效截止日期 ') + "<br/>";
		}

		if (strMsg == "") {
			return true;
		} else {
			return false;
		}
	}

	function isValid() {
		var quotedQty = Ext.get('quotedQty').dom.value;
		if (quotedQty == "" || quotedQty == null) {
		} else if (quotedQty >= 9999999.99) {
			return false;
		}

		var quotedPrice = Ext.get('quotedPrice').dom.value;
		if (quotedPrice == "" || quotedPrice == null) {
		} else if (quotedPrice >= 9999999999999.9999) {
			return false;
		}

		return true;
	}

	/**
	 * 
	 *  在修改数据前的非空检查 
	 */
	function checkUpdateform() {
		strMsg = "";
//		var unitId = Ext.get('unitId').dom.value;
//		if (unitId == "" || unitId == null) {
//			strMsg += String.format(Constants.COM_E_003, '计量单位 ') + "<br/>";
//		}
		var quotedQty = Ext.get('quotedQty').dom.value;
		if (quotedQty == "" || quotedQty == null) {
			strMsg += String.format(Constants.COM_E_003, '报价数量 ') + "<br/>";
		}

		var quotedPrice = Ext.get('quotedPrice').dom.value;
		if (quotedPrice == "" || quotedPrice == null) {
			strMsg += String.format(Constants.COM_E_003, '报价 ') + "<br/>";
		}

		var currencyName = Ext.get('currencyName').dom.value;
		if (currencyName == "" || currencyName == null) {
			strMsg += String.format(Constants.COM_E_003, '供应商币别 ') + "<br/>";
		}

		var effectiveDate = Ext.get('effectiveDate').dom.value;
		if (effectiveDate == "" || effectiveDate == null) {
			strMsg += String.format(Constants.COM_E_003, '报价有效开始日期') + "<br/>";
		}

		var discontinueDate = Ext.get('discontinueDate').dom.value;
		if (discontinueDate == "" || discontinueDate == null) {
			strMsg += String.format(Constants.COM_E_003, '报价有效截止日期 ') + "<br/>";
		}

		if (strMsg == "") {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 检查画面数据是否修改
	 */
	function isModified() {
		var rec = grid.getSelectionModel().getSelected();
		var flag = true;
		//计量单位
		if (Ext.get('unitId').dom.value != rec.get('unitId')){
		    flag = false;
		}
		//数量
		if (Ext.get('quotedQty').dom.value != rec.get('quotedQty')) {
			flag = false;
		}
		//报价
		if (Ext.get('quotedPrice').dom.value != rec.get('quotedPrice')) {
			flag = false;
		}
		//币别	
		if (Ext.get('quotationCurrency').dom.value != rec
				.get('quotationCurrency')) {
			flag = false;
		}
		//有效开始日期
		if (Ext.get('effectiveDate').dom.value != rec.get('effectiveDate')) {
			flag = false;
		}
		//有效截止日期
		if (Ext.get('discontinueDate').dom.value != rec.get('discontinueDate')) {
			flag = false;
		}
		// 备注
		if ((Ext.get('memo').dom.value == '' ? null : Ext.get('memo').dom.value) != rec
				.get('memo')) {
			flag = false;
		}
		if (flag == false) {
			return true;
		} else {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_006);
			return false;
		}
	}

	/**
	 * 确定button压下的操作
	 */
	function confirmRecord() {
		if (strMethod == "update") {
			if (isModified()) {
			} else {
				return;
			}
		}
		Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_C_001,
				function(buttonobj) {
					if (buttonobj == 'yes') {
						if (isValid()) {
							var myurl = "";
							if (strMethod == "add") {
								if (checkAddform()) {
									myurl = 'resource/addData.action';
									if (checkDate()) {
										Ext.get('numquotedQty').dom.value = parseFloat(Ext.get('quotedQty').dom.value.replace(/,/g, ''));
										Ext.get('numquotedPrice').dom.value = parseFloat(Ext.get('quotedPrice').dom.value.replace(/,/g, ''));
										addPanel.getForm().submit({
											method : Constants.POST,
											url : myurl,
											// modify BY ywliu 09/04/29
											params : {
												gatherId : Ext.get('gatherId').dom.value
											},
											success : function(form, action) {
												var result = eval('('
														+ action.response.responseText
														+ ')');
												// 日期重叠
												if (result.msg == 'R') {
													Ext.Msg
															.alert(
																	Constants.ERROR,
																	Constants.RB001_E_001);
												} else {
													// 显示成功信息
													Ext.Msg
															.alert(
																	Constants.SYS_REMIND_MSG,
																	Constants.COM_I_004);
													queryStore.load({
														params : {
															start : 0,
															limit : Constants.PAGE_SIZE
														}
													});
													grid.getView().refresh();
													win.hide();
												}
											},
											failure : function() {
											}
										});
									}
									return;
								} else {
									Ext.Msg.alert(Constants.SYS_REMIND_MSG, strMsg);
									return;
								}
							}

							if (strMethod == "update") {
								if (checkUpdateform()) {
									myurl = 'resource/updateData.action';
									if (checkDate()) {
										Ext.get('numquotedQty').dom.value = parseFloat(Ext.get('quotedQty').dom.value.replace(/,/g, ''));
										Ext.get('numquotedPrice').dom.value = parseFloat(Ext.get('quotedPrice').dom.value.replace(/,/g, ''));
										addPanel.getForm().submit({
											method : Constants.POST,
											url : myurl,
											params : {
												lastModifiedDate : Ext
														.get('lastModifiedDate').dom.value
											},

											success : function(form, action) {
												var result = eval('('
														+ action.response.responseText
														+ ')');
												// 日期重叠
												if (result.msg == 'R') {
													Ext.Msg
															.alert(
																	Constants.ERROR,
																	Constants.RB001_E_001);
													return;
												}
												// 排他
												if (result.msg == 'U') {
													Ext.Msg
															.alert(
																	Constants.ERROR,
																	Constants.COM_E_015);
													return;
												}
												// 显示成功信息
												Ext.Msg.alert(Constants.SYS_REMIND_MSG,
														Constants.COM_I_004);
												queryStore.load({
													params : {
														start : 0,
														limit : Constants.PAGE_SIZE
													}
												});
												grid.getView().refresh();
												win.hide();

											},
											failure : function() {
											}
										});

									}
									return;
								} else {
									Ext.Msg.alert(Constants.SYS_REMIND_MSG, strMsg);
									return;
								}
							}
						} else {
							return;
						}
					}
				});
	}

	function getCurrentDate() {

		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate();
		s += (t > 9 ? "" : "0") + t + " ";
		t = d.getHours();
		s += (t > 9 ? "" : "0") + t + ":";
		t = d.getMinutes();
		s += (t > 9 ? "" : "0") + t + ":";
		t = d.getSeconds();
		s += (t > 9 ? "" : "0") + t;

		return s;

	}

	function divide2(value) {
		if (value == null) {
			return "";
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
	
	function divide4(value) {
		if (value == null) {
			return "";
		}
		   value = String(value);
            // 整数部分
            var whole = value;
            // 小数部分
            var sub = ".0000";
            // 如果有小数
		    if (value.indexOf(".") > 0) {
		    	whole = value.substring(0, value.indexOf("."));
			    sub = value.substring(value.indexOf("."), value.length);
			    sub = sub + "0000";
			    if(sub.length > 5){
			    	sub = sub.substring(0,5);
			    }
		    }
            var r = /(\d+)(\d{3})/;
            while (r.test(whole)){
                whole = whole.replace(r, '$1' + ',' + '$2');
            }
            v = whole + sub;
            return v;
	}
	
	/**
	 * 页面加载显示数据
	 */
	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				items : [grid]
			});
})
