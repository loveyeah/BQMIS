Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

// 常量 仓库删除出错信息
Constants.RS003_C_001 = "确认进行移库处理吗？";
Constants.RS003_E_002 = "调入库位不能同调出库位相同。";
Constants.RS003_E_001 = "调入仓库不能同调出仓库相同。";
Constants.RS003_E_003 = "&nbsp调拨数量不能大于当前库存。&nbsp";

function getDate() {
	var d, s, t;
	d = new Date();
	s = d.getFullYear().toString(10) + "-";
	t = d.getMonth() + 1;
	s += (t > 9 ? "" : "0") + t + "-";
	t = d.getDate();
	s += (t > 9 ? "" : "0") + t + " ";
	return s;
}
Ext.onReady(function() {
	Ext.QuickTips.init();
	// 获取到用户的信息
	getUserName();

	// 数字的format
	function format(n) {
		var str = n + "";
		if (/\./.test(str)) {
			// 小数部分
			var tempDecimal = str.split(".")[1];
			// 小数部分存在的时候
			if (tempDecimal) {
				// 小数位数过长时，截取前５位
				if (tempDecimal.length > 5) {
					tempDecimal = tempDecimal.substring(0, 5);
				}
				// 小数部分进行三位一格处理
				// var tempDecimalFromat =
				// tempDecimal.split("").join("").replace(
				// /(.{3})/g, "$1,").replace(/,$/, "").split("").join("");
			};
			// 整数部分
			var strInteger = str.split(".")[0];
		} else {
			var strInteger = str;
			var tempDecimalFromat = ""
		}
		// 整数部分进行三位一格处理
		var re = strInteger.split("").reverse().join("").replace(/(.{3})/g,
				"$1,").replace(/,$/, "").split("").reverse().join("");
		// 整数部分和小数部分的合并
		if (tempDecimal) {
			return re + "." + tempDecimal;
		} else {
			return re;
		}
	}
	// 事务作用码
	var txtTransactionNo = new Ext.form.TextField({
				fieldLabel : '事务作用码',
				isFormField : true,
				allowBlank : true,
				value : 'TT',
				readOnly : true,
				maxLengthText : 10,
				id : "transactionNo",
				name : "transactionNo",
				anchor : '95%'
			});
	// 事务作用名称
	var txtTransactionName = new Ext.form.TextField({
				fieldLabel : '事务作用名称',
				allowBlank : true,
				maxLengthText : 100,
				isFormField : true,
				readOnly : true,
				id : "transactionName",
				name : "transactionName",
				anchor : '100%'
			});

	// 事物信息行(事物作用码，事物作用名称)
	var transactionLine = new Ext.Panel({
				border : false,
				height : 40,
				layout : "column",
				style : "padding-top:0px;padding-bottom:0px;margin-bottom:0px",
				anchor : '95%',
				items : [{
							columnWidth : 0.3,
							layout : "form",
							border : false,
							items : [txtTransactionNo]
						}, {
							columnWidth : 0.4,
							layout : "form",
							border : false,
							items : [txtTransactionName]
						}, {
							columnWidth : 0.3,
							layout : "form",
							border : false
						}]
			});

	// 物料编码
	var txtMaterialNo = new Ext.form.TextField({
				fieldLabel : '物料编码<font color="red">*</font>',
				allowBlank : false,
				maxLength : 30,
                readOnly : true,
				id : "materialNo",
				name : "materialNo",
				anchor : '95%'
			});
			
	// 物料编码的单击事件
	txtMaterialNo.onClick(popupSelect);

	// 显示检索画面
	function popupSelect() {
		this.blur();
		var args = 0;
		// 显示窗体
		var object = window
				.showModalDialog(
						'RS003_MaterialQueryAndSelect.jsp',
						args,
						'dialogWidth=800px;dialogHeight=520px;center=yes;help=no;resizable=no;status=no;');
		if (object) {
			if(object.flag == "Y") {
				// 物料ＩＤ
				if (typeof(object.materialId) != "undefined") {
					hdMaterialId.setValue(object.materialId);
				} else {
					hdMaterialId.reset();
				}
				// 物料编码
				if (typeof(object.materialNo) != "undefined") {
					txtMaterialNo.setValue(object.materialNo);
				} else {
					txtMaterialNo.reset();
				}
				// 物料批号
				if (typeof(object.lotNo != "undefined")) {
					txtLotNo.setValue(object.lotNo);
				} else {
					txtLotNo.reset();
				}
				// 物料名称
				if (typeof(object.materialName != "undefined")) {
					txtMaterialName.setValue(object.materialName);
				} else {
					txtMaterialName.reset();
				}
				// 原仓库名称
				if (typeof(object.whsName != "undefined")) {
					txtWhsNameFrom.setValue(object.whsName);
				} else {
					txtWhsNameFrom.reset();
				}
				// 原仓库编码
				if (typeof(object.whsNo != "undefined")) {
					hdWhsNoFrom.setValue(object.whsNo);
				} else {
					hdWhsNoFrom.reset();
				}
				// 原库位
				if (typeof(object.locationNo != "undefined")) {
					hdLocationNoFrom.setValue(object.locationNo);
				} else {
					hdLocationNoFrom.reset();
				}
				// 原库位名称
				if (typeof(object.locationName != "undefined")) {
					txtLocationNameFrom.setValue(object.locationName);
				} else {
					txtLocationNameFrom.reset();
				}
				// 当前库存
				if (typeof(object.nowCount != "undefined")) {
					var nubFormat = object.nowCount;
					nubFormat.replace(',', '');
					hdnowCount.setValue(parseFloat(nubFormat));
				} else {
					hdnowCount.setValue(0);
				}
				// 库存物料记录.上次修改时间
				if (typeof(object.lastModifiedDateWhs != "undefined")) {
					hdLastModifiedDateWhs.setValue(object.lastModifiedDateWhs);
				} else {
					hdLastModifiedDateWhs.reset();
				}
				// 库位物料记录.上次修改时间
				if (typeof(object.lastModifiedDateLocation != "undefined")) {
					hdLastModifiedDateLocation
							.setValue(object.lastModifiedDateLocation);
				} else {
					hdLastModifiedDateLocation.reset();
				}
				// 批号记录.上次修改时间
				if (typeof(object.lastModifiedDateLot != "undefined")) {
					hdLastModifiedDateLot.setValue(object.lastModifiedDateLot);
				} else {
					hdLastModifiedDateLot.reset();
				}
				// 移行库位的设置
				if (typeof(object.locationNo) != "undefined") {
					if (object.locationNo == null || object.locationNo == "") {
						cmbLocationNoTo.clearValue();
						cmbLocationNoTo.disable();
					} else {
						cmbLocationNoTo.enable();
					}
				}
			} else if(object.flag == "N"){
				// 物料ＩＤ
				hdMaterialId.reset();
				// 物料编码
				txtMaterialNo.reset();
				// 物料批号
				txtLotNo.reset();
				// 物料名称
				txtMaterialName.reset();
				// 原仓库名称
				txtWhsNameFrom.reset();
				// 原仓库编码
				hdWhsNoFrom.reset();
				// 原库位
				hdLocationNoFrom.reset();
				// 原库位名称
				txtLocationNameFrom.reset();
				// 当前库存
				hdnowCount.setValue(0);
				// 库存物料记录.上次修改时间
				hdLastModifiedDateWhs.reset();
				// 库位物料记录.上次修改时间
				hdLastModifiedDateLocation.reset();
				// 批号记录.上次修改时间
				hdLastModifiedDateLot.reset();
				// 移行库位的设置
				cmbLocationNoTo.clearValue();
				cmbLocationNoTo.disable();
			}
		}
	}

	// 物料ID
	var hdMaterialId = new Ext.form.Hidden({
				id : "materialId",
				name : "materialId"
			})
	// 物料批号
	var txtLotNo = new Ext.form.TextField({
				fieldLabel : '物料批号',
				allowBlank : true,
				isFormField : true,
				readOnly : true,
				id : "lotNo",
				name : "lotNo",
				anchor : '100%'
			});

	// 物料信息行(物料编码，物料批号)
	var materialLine = new Ext.Panel({
				border : false,
				height : 40,
				layout : "column",
				style : "padding-top:0px;padding-bottom:0px;margin-bottom:0px",
				anchor : '95%',
				items : [{
							columnWidth : 0.3,
							layout : "form",
							border : false,
							items : [txtMaterialNo, hdMaterialId]
						}, {
							columnWidth : 0.4,
							layout : "form",
							border : false,
							items : [txtLotNo]
						}, {
							columnWidth : 0.3,
							layout : "form",
							border : false
						}]
			});

	// 物料名称
	var txtMaterialName = new Ext.form.TextField({
				fieldLabel : '物料名称',
				isFormField : true,
				allowBlank : true,
				readOnly : true,
				id : "materialName",
				name : "materialName",
				anchor : '95%'
			});
	// 原仓库名称
	var txtWhsNameFrom = new Ext.form.TextField({
				fieldLabel : '仓库',
				allowBlank : true,
				isFormField : true,
				readOnly : true,
				id : "fromWhsName",
				name : "fromWhsName",
				anchor : '100%'
			});
	// 原仓库
	var hdWhsNoFrom = new Ext.form.Hidden({
				id : "fromWhsNo",
				name : "fromWhsNo"
			});
	// 库存物料记录.上次修改时间
	var hdLastModifiedDateWhs = new Ext.form.Hidden({
				id : "lastModifiedDateWhs",
				name : "lastModifiedDateWhs"
			})
	// 原库位名称
	var txtLocationNameFrom = new Ext.form.TextField({
				fieldLabel : '库位',
				allowBlank : true,
				readOnly : true,
				id : "fromLocationName",
				name : "fromLocationName",
				anchor : '95%'
			});

	// 原库位
	var hdLocationNoFrom = new Ext.form.Hidden({
				id : "fromLocationId",
				name : "fromLocationId"
			});
	// 库位物料记录.上次修改时间
	var hdLastModifiedDateLocation = new Ext.form.Hidden({
				id : "lastModifiedDateLocation",
				name : "lastModifiedDateLocation"
			});
	// 批号记录.上次修改时间
	var hdLastModifiedDateLot = new Ext.form.Hidden({
				id : "lastModifiedDateLot",
				name : "lastModifiedDateLot"
			});

	// 原库存信息行(物料名称，仓库，库位)
	var storeFromLine = new Ext.Panel({
		border : false,
		height : 40,
		layout : "column",
		style : "padding-top:0px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
		anchor : '95%',
		items : [{
					columnWidth : 0.3,
					layout : "form",
					border : false,
					items : [txtMaterialName]
				}, {
					columnWidth : 0.4,
					layout : "form",
					border : false,
					items : [txtWhsNameFrom, hdWhsNoFrom, hdLastModifiedDateWhs]
				}, {
					columnWidth : 0.3,
					layout : "form",
					border : false,
					items : [txtLocationNameFrom, hdLocationNoFrom,
							hdLastModifiedDateLocation, hdLastModifiedDateLot]
				}]
	});

	// 日期
	var dfModifiedDate = new Ext.form.TextField({
				id : 'modifiedDate',
				fieldLabel : "日期",
				name : 'modifiedDate',
				style : 'cursor:pointer',
				anchor : "95%",
				readOnly : true,
				value : getDate()
			})

	// 操作员
	var txtModifiedBy = new Ext.form.TextField({
				fieldLabel : '操作员',
				allowBlank : true,
				readOnly : true,
				id : "modifiedBy",
				name : "modifiedBy",
				anchor : '100%'
			});
	// 操作信息行(日期，操作员)
	var informationLine = new Ext.Panel({
		border : false,
		height : 40,
		layout : "column",
		style : "padding-top:0px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
		anchor : '95%',
		items : [{
					columnWidth : 0.3,
					layout : "form",
					border : false,
					items : [dfModifiedDate]
				}, {
					columnWidth : 0.4,
					layout : "form",
					border : false,
					items : [txtModifiedBy]
				}, {
					columnWidth : 0.3,
					layout : "form",
					border : false
				}]
	});

	// 获取登陆用户的信息
	function getUserName() {
		Ext.lib.Ajax.request('POST', 'resource/getStoreUserName.action', {
					success : function(action) {
						// 返回的内容
						var result = action.responseText;
						// 通过固定的字符分割
						var divResult = result.split("TT:");
						// 移库的名称
						var name = "";
						// 设置用户名
						txtModifiedBy.setValue(divResult[0]);
						// 设置移库的名称
						for (i = 1; i < divResult.length; i++) {
							name += divResult[i];
						}
						txtTransactionName.setValue(name);
					}
				});

	}

	var warehouseStore = new Ext.data.JsonStore({
				root : 'list',
				url : "resource/getWarehouseList.action",
				fields : ['whsNo', 'whsName']
			});
	warehouseStore.load({
				params : {
					flag : 1
				}
			}
	);

	// 调入库
	var cmbWhsNoTo = new Ext.form.ComboBox({
				fieldLabel : '调入库<font color="red">*</font>',
				allowBlank : false,
				hiddenName : "toWhsId",
				anchor : '95%',
				store : warehouseStore,
				displayField : "whsName",
				valueField : "whsNo",
				mode : 'local',
				triggerAction : 'all',
				readOnly : true
			});

	var locationStore = new Ext.data.JsonStore({
				root : 'list',
				url : "resource/getLocationList.action",
				fields : ['locationNo', 'locationName']
			})

	cmbWhsNoTo.on("change", selectLocation);
	// 库位发生变化时，库位的连动处理
	function selectLocation() {
		// 当仓库不为空的时候，库位的加载
		if (cmbWhsNoTo.getValue() != null && cmbWhsNoTo.getValue() != "") {
			locationStore.load({
						params : {
							whsNo : cmbWhsNoTo.getValue()
						}
					});
            cmbLocationNoTo.clearValue();        
		} else {
			locationStore.removeAll();
			cmbLocationNoTo.clearValue();
		}
	}
	// 库位
	var cmbLocationNoTo = new Ext.form.ComboBox({
				fieldLabel : '调入库位',
				store : locationStore,
				displayField : "locationName",
				valueField : "locationNo",
				mode : 'local',
				triggerAction : 'all',
				readOnly : true,
				hiddenName : "toLocationId",
				anchor : '100%'
			});

	// 调拨数量表示
    var txtChange = new Powererp.form.NumField({
      allowNegative : false,
      anchor : '95%',
      fieldLabel : '调拨数量<font color="red">*</font>',
      maxValue : 999999999999999.99999,
      id : 'numChange',
      name : 'numChange',
      hiddenName : 'numChange',
      padding : 2,
      decimalPrecision : 5
    })  

	// 当前库存
	var hdnowCount = new Ext.form.Hidden({
				id : "nowCount",
				name : "nowCount"
			})

	// 库存信息行(仓库，库位，调拨数量)
	var storeToLine = new Ext.Panel({
		border : false,
		height : 40,
		layout : "column",
		style : "padding-top:0px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
		anchor : '95%',
		items : [{
					columnWidth : 0.3,
					layout : "form",
					border : false,
					items : [cmbWhsNoTo]
				}, {
					columnWidth : 0.4,
					layout : "form",
					border : false,
					items : [cmbLocationNoTo]
				}, {
					columnWidth : 0.3,
					layout : "form",
					border : false,
					items : [txtChange]
				}]
	});

	// 确认移库
	var btnStoreChange = new Ext.Button({
				text : '确认移库',
				handler : storeChangeHandler
			})

	/**
	 * 确认移库的单击事件
	 */
	function storeChangeHandler() {
		// 调拨数量有效的时候，进行下记处理
		if (txtChange.validate()) {
			Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.RS003_C_001,
					function(buttonobj) {
						if (buttonobj == "yes") {
							// 判断是否可以移库
							var checkResult = isPanelCanSave();
							if (checkResult) {
								// 移库
								storePanel.getForm().submit({
									method : 'POST',
									url : 'resource/updateStore.action',
									success : function(form, action) {
										Ext.Msg.alert(Constants.SYS_REMIND_MSG,
												'&nbsp&nbsp&nbsp'
														+ Constants.COM_I_004);
										// 初期化    
                                        storePanel.getForm().reset();
                                        getUserName();
									},
									failure : function(form, action) {
										var result = Ext.util.JSON
												.decode(action.response.responseText);
										if (result == 'A') {
											Ext.Msg.alert(
													Constants.SYS_REMIND_MSG,
													Constants.COM_E_015);
										} else {
											Ext.Msg.alert(
													Constants.SYS_REMIND_MSG,
													Constants.COM_E_014);
										}
									}
								});
							}

						}
					});
		}
	}

	/**
	 * panel的内容check
	 */
	function isPanelCanSave() {
		// 获取消息
		var msg = checkPanelInfo();
		if (msg) {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, msg);
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Panel的内容check消息处理
	 */
	function checkPanelInfo() {
		var msg = "";
		// 物料编码不可以为空
		if ((!txtMaterialNo.getValue())) {
			msg += String.format(Constants.COM_E_003, '物料编码');
		}

		// 调入库不可以为空
		if ((!cmbWhsNoTo.getValue())) {
			if (msg != "") {
				msg += '<br/>' + String.format(Constants.COM_E_003, '调入库');
			} else {
				msg += String.format(Constants.COM_E_003, '调入库');
			}

		}
		
		// 调拨数量不可以为空
		if ((txtChange.getValue() == "") && (txtChange.getValue() != "0")) {
			if (msg != "") {
				msg += '<br/>' + String.format(Constants.COM_E_002, '调拨数量');
			} else {
				msg += String.format(Constants.COM_E_002, '调拨数量');
			}

		}

		// 必须项的check存在的时候，放回
		if (msg != "") {
			return msg;
		}

        // 调拨数量为０的时候
        if(txtChange.getValue() == 0){
           return String.format(Constants.COM_E_013,"调拨数量");
        }
        
		// 调入库位有值的时候,调出的库位没有值
		if (hdLocationNoFrom.getValue() && !cmbLocationNoTo.getValue()) {
			return String.format(Constants.COM_E_003, '调入库位');
		}

		// 调出库位有值的时候
		if (hdLocationNoFrom.getValue() != null
				&& hdLocationNoFrom.getValue() != "") {
			// 调出库位为空的情况下,和调出库位相同
			if ((cmbLocationNoTo.getValue() == hdLocationNoFrom.getValue())
					&& (cmbWhsNoTo.getValue() == hdWhsNoFrom.getValue())) {
				return Constants.RS003_E_002;
			}
		}

		// 调出库位没有值
		if (hdLocationNoFrom.getValue() == null
				|| hdLocationNoFrom.getValue() == "") {
			// 和调出库位相同
			if (cmbWhsNoTo.getValue() == hdWhsNoFrom.getValue()) {
				return Constants.RS003_E_001;
			}
		}

		// 调出数量 > 当前库存时
		if (txtChange.getValue() > hdnowCount.getValue()) {
			return Constants.RS003_E_003;
		}
	}

	
    // 画面这个panel
	var storePanel = new Ext.FormPanel({
				border : false,
				bodyBorder : false,
				frame : false,
				bodyStyle : {
					'padding-top' : '0px',
					'padding-bottom' : '0px'
				},
				defaults : {
					labelAlign : 'right'
				},
				buttonAlign : 'right',
				width : 400,
				labelPad : 15,
				height : 700,
				labelWidth : 85,
				labelAlign:'right',
                tbar : [btnStoreChange],
				items : [new Ext.form.FieldSet({
							bodyStyle : {
								'padding-top' : '10px',
								'padding-bottom' : '0px'
							},
							layout : 'form',
							border : false,
							bodyBorder : false,
							body : false,
							height : 700,
							items : [transactionLine, materialLine,
									storeFromLine, informationLine, storeToLine]
						})]
			});

	// 显示区域
	var layout = new Ext.Viewport({
				enableTabScroll : true,
                autoScroll:true,
                layout : 'fit',
				region : 'center',
				border : false,
				items : [storePanel]
			});
})