/**
 * 退库管理
 * @author 刘公磊 081226
 */

Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
// 把数量用逗号分隔
function divide(value) {
	if(value==null){
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
	var n1 = Math.round(exp1); //四舍五入
	var n2 = Math.round(exp2); //四舍五入

	var rslt = n1 / n2; //除

	if (rslt >= 0) {
		rslt = Math.floor(rslt); //返回值为小于等于其数值参数的最大整数值。
	} else {
		rslt = Math.ceil(rslt); //返回值为大于等于其数字参数的最小整数。
	}

	return rslt;
}
Ext.onReady(function() {
	var date = null
	/**
	 * 事务作用码
	 */
	var txtTransactionNo = new Ext.form.TextField({
				fieldLabel : '事务作用码',
				labelAlign : 'right',
				isFormField : true,
				allowBlank : true,
				readOnly : true,
				width : 180,
				id : "transactionNo",
				name : "trans.transNo",
				anchor : '90%'//'100%'
			});
	/**
	 * 事务作用名称
	 */
	var txtTransactionName = new Ext.form.TextField({
				fieldLabel : '事务作用名称',
				allowBlank : true,
				isFormField : true,
				width : 180,
				labelAlign : 'right',
				readOnly : true,
				id : "transactionName",
				name : "trans.transName",
				anchor : '90%'//'100%'
			});
	/**
	 * 事物信息行(事物作用码，事物作用名称)
	 */
	var transactionLine = new Ext.form.FieldSet({
		border : false,
		height : 30,
		labelAlign : 'right',
		layout : "column",
		style : "padding-top:5px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
		anchor : '100%',
		items : [{
					columnWidth : 0.5,
					layout : "form",
					border : false,
					labelAlign : 'right',
					items : [txtTransactionNo]
				}, {
					columnWidth : 0.5,
					layout : "form",
					border : false,
					labelAlign : 'right',
					items : [txtTransactionName]
				}]
	});

	/**
	 * 单据号
	 */
	var txtBillNo = new Ext.form.TextField({
				fieldLabel : '单据号',
				isFormField : true,
				allowBlank : true,
				readOnly : true,
				id : "receiveNo",
				labelAlign : 'right',
				name : "IJID.receiveNo",
				anchor : '90%'//'100%'
			});
	/**
	 * 项次号
	 */
	var txtItemNo = new Ext.form.TextField({
				fieldLabel : '项次号',
				isFormField : true,
				allowBlank : true,
				readOnly : true,
				id : "issueId",
				name : "IJID.issueId",
				anchor : '90%'//'100%'
			});

	/**
	 * 单据信息行(单据号，项次号)
	 */
	var billInforLine = new Ext.form.FieldSet({
		border : false,
		height : 30,
		layout : "column",
		labelAlign : 'right',
		style : "padding-top:5px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
		anchor : '100%',
		items : [{
					columnWidth : 0.5,
					layout : "form",
					border : false,
					labelAlign : 'right',
					items : [txtBillNo]
				}, {
					columnWidth : 0.5,
					layout : "form",
					border : false,
					labelAlign : 'right',
					items : [txtItemNo]
				}]
	});

	/**
	 * 物料编码
	 */
	var txtMaterialNo = new Ext.form.TextField({
				fieldLabel : '物料编码',
				isFormField : true,
				allowBlank : true,
				readOnly : true,
				id : "materialNo",
				labelAlign : 'right',
				name : "ICM.materialNo",
				anchor : '90%'//'100%'
			});
	/**
	 * 物料名称
	 */
	var txtMaterialName = new Ext.form.TextField({
				fieldLabel : '物料名称',
				isFormField : true,
				allowBlank : true,
				readOnly : true,
				id : "materialName",
				labelAlign : 'right',
				name : "ICM.materialName",
				anchor : '90%'//'100%'
			});
	/**
	 * 物料信息行(物料编码，物料名称)
	 */
	var materialLine = new Ext.form.FieldSet({
		border : false,
		height : 30,
		layout : "column",
		labelAlign : 'right',
		style : "padding-top:5px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
		anchor : '100%',
		items : [{
					columnWidth : 0.5,
					layout : "form",
					border : false,
					labelAlign : 'right',
					items : [txtMaterialNo]
				}, {
					columnWidth : 0.5,
					layout : "form",
					border : false,
					labelAlign : 'right',
					items : [txtMaterialName]
				}]
	});
	/**
	 * 日期
	 */
	var cldModifiedDate = new Ext.form.DateField({
				fieldLabel : '日期',
				readOnly : true,
				labelAlign : 'right',
				name : "ModifiedDate",
				id : 'ModifiedDate',
				format : "Y-m-d",
				hideTrigger : true,
				anchor : '90%',//'100%',
				clearCls : 'allow-float',
				checked : true
			});
	/**
	 * 操作员
	 */
	var txtModifiedBy = new Ext.form.TextField({
				fieldLabel : '操作员',
				allowBlank : true,
				isFormField : true,
				readOnly : true,
				id : "modifiedBy",
				labelAlign : 'right',
				name : "loc.modifiedBy",
				anchor : '90%'//'100%'
			});
	/**
	 * 操作信息行(日期，操作员)
	 */ 
	var informationLine = new Ext.form.FieldSet({
		border : false,
		height : 30,
		layout : "column",
		labelAlign : 'right',
		style : "padding-top:5px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",

		items : [{
					columnWidth : 0.5,
					layout : "form",
					border : false,
					labelAlign : 'right',
					items : [cldModifiedDate]
				}, {
					columnWidth : 0.5,
					layout : "form",
					border : false,
					labelAlign : 'right',
					items : [txtModifiedBy]
				}]
	});
	/**
	 * 原仓库用store
	 */
	var storeWhsNoFrom = new Ext.data.JsonStore({
			 url : 'resource/getWareHouse.action',
			 root : 'list',
			 fields : ['fromWhsId','whsName']
		});
	/**
	 *  原仓库
	 */
	var cbboxWhsNoFrom = new Ext.form.ComboBox({
				fieldLabel : "仓库<font color='red'>*</font>",
				isFormField : true,
				readOnly : true,
				allowBlank : false,
				mode : 'local',
				triggerAction : 'all',
				store : storeWhsNoFrom,
				displayField : "whsName",
				valueField : "fromWhsId",
				id : "whsNoFrom",
				labelAlign : 'right',
				name : "whsNoFrom",
				anchor : '90%'//'100%'
			});
	/**
	 * 原库位用store
	 */
	var storeLocationNoFrom = new Ext.data.JsonStore({
				url : 'resource/getWareHouse.action',
				root : 'list',
				fields : ['fromLocationId','locationName']
			});
	/**
	 * 原库位
	 */
	var cbboxLocationNoFrom = new Ext.form.ComboBox({
				fieldLabel : '库位',
				isFormField : true,
				readOnly : true,
				triggerAction : 'all',
				mode : 'local',
				store : storeLocationNoFrom,
				displayField : "locationName",
				valueField : "fromLocationId",
				id : "locationName",
				labelAlign : 'right',
				name : "locationName",
				anchor : '90%'//'100%'
				
			});
			cbboxLocationNoFrom.disable();
	
	/**
	 * 原库存信息行(仓库，库位)
	 */
	var storeFromLine = new Ext.form.FieldSet({
		border : false,
		height : 30,
		layout : "column",
		labelAlign : 'right',
		style : "padding-top:5px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
		anchor : '100%',
		items : [{
					columnWidth : 0.5,
					layout : "form",
					border : false,
					labelAlign : 'right',
					items : [cbboxWhsNoFrom]
				}, {
					columnWidth : 0.5,
					layout : "form",
					border : false,
					labelAlign : 'right',
					items : [cbboxLocationNoFrom]
				}]
	});
	/**
	 * 退库原因store
	 */
	var storeMoveBackReason = new Ext.data.JsonStore({
				url : 'resource/getMoveBackReason.action',
				root : 'list',
				fields : ['transId','reasonName']
			});
	/**
	 * 退库原因
	 */
	var cbMoveBackReason = new Ext.form.ComboBox({
				fieldLabel : "退库原因<font color='red'>*</font>",
				isFormField : true,
				readOnly : true,
				allowBlank : false,
				triggerAction : 'all',
				mode : 'local',
				store : storeMoveBackReason,
				labelAlign : 'right',
				displayField : "reasonName",
				valueField : "transId",
				id : "moveBackReason",
				name : "loc.modifiedBy",
				anchor : '95%'//'100%'
			});
	/**
	 * 退库原因行
	 */
	var moveBackReasonLine = new Ext.form.FieldSet({
		border : false,
		height : 30,
		layout : "column",
		labelAlign : 'right',
		style : "padding-top:5px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",

		items : [{
					columnWidth : 1,
					layout : "form",
					border : false,
					labelAlign : 'right',
					items : [cbMoveBackReason]
				}, {
					layout : "form",
					border : false
				}]
	});
	/**
	 * 退库数量
	 */
	var txtMoveBackNumber = new Powererp.form.NumField({
				fieldLabel : "退库数量<font color='red'>*</font>",
				allowBlank : false,
				isFormField : true,
				minValue : 0.01,
				style:'text-align:right',
				maxValue : 99999999999.99,
				allowNegative : false,
				labelAlign : 'right',
				padding : 2,
				id : "moveBackNumber",
				name : "whs.whsNoTo",
				anchor : '90%'//'100%'
			});
	/**
	 * 退库数量行
	 */
	var moveBackNumberLine = new Ext.form.FieldSet({
		border : false,
		height : 30,
		layout : "column",
		labelAlign : 'right',
		style : "padding-top:5px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",

		items : [{
					columnWidth : 0.5,
					layout : "form",
					border : false,
					labelAlign : 'right', 
					items : [txtMoveBackNumber]
				}, {
					columnWidth : 0.5,
					layout : "form",
					border : false
				}]
	});
	/**
	 * 备注
	 */
	var txtRemark = new Ext.form.TextArea({
				fieldLabel : '备注',
				allowBlank : true,
				height:Constants.TEXTAREA_HEIGHT,
				isFormField : true,
				allowNegative : false,
				maxLength : 128,
				labelAlign : 'right',
				id : "remark",
				name : "remark",
				anchor : '95%'//'100%'
			});

	/**
	 * 备注行
	 */
	var remarkLine = new Ext.form.FieldSet({
		border : false,
		height : 60,
		layout : "column",
		style : "padding-top:5px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
		items : [{
					columnWidth : 1,
					layout : "form",
					border : false,
					labelAlign : 'right',
					items : [txtRemark]
				}, {
					layout : "form",
					border : false
				}]
	});

	/**
	 * 领料单号
	 */ 
	var txtreceiveMaterial = new Ext.form.TextField({
				fieldLabel : "领料单号<font color='red'>*</font>",
				allowBlank : false,
				labelAlign : 'right',
				maxLength : 15,
				id : "receive_No",
				name : "receive_No",
				anchor : '90%'//'100%'
			});

	/**
	 *  grid中用数据
	 */
	var runGridData = new Ext.data.Record.create([
			// 领料单明细表.领料单明细ID
			{
		        name : 'issueDetailsId'
	        },
			// 领料单表头.领料单编号
			{
				name : 'issueNo'
			},
			// 物料主文件.名称
			{
				name : 'materialName'
			},
			// 物料主文件.存货计量单位
			{
				name : 'stockUmId'
			},
			// 领料单明细表.实际数量
			{
				name : 'actIssuedCount'
			},
			// 领料单明细表.需求计划明细ID
			{
		        name : 'requirementDetailId'
	        },
			// 领料单明细表.操作日期
			{
		        name : 'lastModifiedDate'
	        },
			// 领料单明细表.物料ID 
			{
				name : 'materialId'
			},
			// 领料单表头.申请领料人
			{
				name : 'receiptBy'
			},
			// 领料单表头.领用部门 
			{
				name : 'receiptDept'
			},
			// 领料单表头.领料单ID
			{
				name : 'issueHeadId'
			},
			// 领料单表头.费用归口部门
			{
				name : 'feeByDep'
			},
			// 物料主文件.编码
			{
				name : 'materialNo'
			},
			// 存货计量单位名称
			{
				name : 'stockUmName'
			}]);

	var id = new Ext.form.Hidden({
				id : "id",
				name : 'id',
				value:''
			});		
	// 退库grid用columns
	var colms = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
						header : "行号",
						width : 35
					}), {
				// 项次号
				header : "项次号",
				width : 45,
				sortable : true,
				dataIndex : 'issueDetailsId'
			}, {
				// 物料编码
				header : "物料编码",
				width : 55,
				sortable : true,
				dataIndex : 'materialNo'
			}, {
				// 物料名称
				header : "物料名称",
				width : 55,
				sortable : true,
				dataIndex : 'materialName'
			}, {
				// 存货计量单位
				header : "单位",
				width : 40,
				sortable : true,
				dataIndex : 'stockUmName'
			}, {
				// 领用数量
				align : 'right',
				header : "领用数量",
				renderer : divide,
				width : 70,
				sortable : true,
				dataIndex : 'actIssuedCount'
			}]);

	/**
	 * 退库grid用store
	 */ 
	var gridStore = new Ext.data.JsonStore({
				url : 'resource/findByReceiveNo.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : runGridData
			})
	/**
	 * 查询按钮
	 */
	var searchBtn = new Ext.Button({
				id : 'search',
				text : Constants.BTN_QUERY,
				iconCls : Constants.CLS_QUERY,
				handler :checkReceiveNo
			});
	/**
	 * 确认按钮
	 */ 
	var validateBtn = new Ext.Button({
				text : '确认',
				id : "validate",
				disabled:true,
				handler : validateBtnClick
			});
	var tbar = new Ext.Toolbar(["领料单号<font color='red'>*</font>: ", txtreceiveMaterial, searchBtn])//, '-',validateBtn 隐藏页面的“确认”按钮 modify by ywliu 090717
	/**
	 * 退库grid
	 */

	var moveBackGrid = new Ext.grid.EditorGridPanel({
				region : "center",
				anchor : "100%",
				width : 300,
				fitToFrame : true,
				enableColumnMove : false,
				sm : new Ext.grid.CheckboxSelectionModel({
							singleSelect : true,
							listeners : {
								rowselect : function(rsm, row, rec) {
								}
							}
						}),
				store : gridStore,
				cm : colms,
				tbar : tbar,
				bbar : new Ext.PagingToolbar({
							pageSize : Constants.PAGE_SIZE,
							store : gridStore,
							displayInfo : true,
							displayMsg : '共 {2} 条',
							emptyMsg : Constants.EMPTY_MSG
						}),
				border : true,
				autoSizeColumns : true
			});
			
	/**
	 * moveBackGrid的双击事件
	 */	
			moveBackGrid.on('rowdblclick',validateClick);

	/**
     * 左面grid用panel
     */ 
    var panelLeft = new Ext.Panel({
                region : 'west',
                layout : 'fit',
                width : 305,
                autoScroll : true,
                collapsible : true,
                containerScroll : true,
                border : false,
                autoScroll : true,
                split : false,
                items : [moveBackGrid]
            });
    /**
     * 确认退库按钮
     */
    var validateMovebackBtn = new Ext.Button({
                text : '确认退库',
                id : "validateMoveback",
                handler : validateMovebackClick
            });
    /**
     * 右面form用panel
     */ 
    var panelRight = new Ext.Panel({
                labelAlign : 'right',
                region : "center",
                height : 800,
                labelPad : 15,
                labelWidth : 65,
                autoScroll : true,
                tbar : ([validateMovebackBtn]),
                items : [transactionLine, billInforLine, materialLine,
                        informationLine, storeFromLine, moveBackReasonLine,
                        moveBackNumberLine, remarkLine]
            });

    new Ext.Viewport({
                enableTabScroll : true,
                layout : "border",
                items : [panelLeft, panelRight]
            });
            
    /**
     * 点击确认退库
     */
    function validateMovebackClick() {
    	if(id.getValue()===""){
    	 return ;	
    	}
		Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_C_001,
				function(buttonobj) {
					if (buttonobj == "yes") {
						if (pageItemCheck()) {
							movebackClick();
						}
					}
				});
	}
    /**
	 * 确认退库画面项目check
	 */
    function pageItemCheck(){
        var rec = moveBackGrid.getSelectionModel().getSelected();
        var str = "";
        //仓库
        if("" == cbboxWhsNoFrom.getValue()){
            str += String.format(Constants.COM_E_003,"仓库")+"\<br>";
        }
        //退库原因必须输入的确认
        if("" == cbMoveBackReason.getValue()){
            str += String.format(Constants.COM_E_003,"退库原因")+"\<br>";
        }
        //退库数量必须输入的确认
        if("" == txtMoveBackNumber.getValue().toString()){
            str += String.format(Constants.COM_E_002,"退库数量");
        }
        if(""!= str){
            Ext.Msg.alert(Constants.SYS_REMIND_MSG, str);
            return false;
        }
        //退库数量 退库数量 〉领用数量
        if (txtMoveBackNumber.getValue() > rec.get('actIssuedCount')) {
            Ext.Msg.alert(Constants.SYS_REMIND_MSG, String.format(Constants.COM_E_006,
                            "退库数量", "领用数量"));
            return false;
        }
        return true;
    }
    
    /**
     * 确认退库判断是否执行
     */
    function movebackClick(){
        var rec = moveBackGrid.getSelectionModel().getSelected();
        Ext.Ajax.request({
                    method : 'POST',
                    url : 'resource/updateMovebackWarehouse.action',
                    params : {
                        rec : Ext.util.JSON.encode(rec.data),
                        warehouseInvId :cbboxWhsNoFrom.getValue('fromWhsId'),
                        locationInvId : cbboxLocationNoFrom.getValue('locationName'),
                        moveBackReason : cbMoveBackReason.getValue('moveBackReason'),
                        moveBackNumber : Ext.get('moveBackNumber').dom.value,
                        remark : Ext.get('remark').dom.value
                    },
                    success : function(result, request) {
                        var over = eval('(' + result.responseText + ')');
                        if(over.flag == "1"){
                            Ext.Msg.alert(Constants.SYS_REMIND_MSG,
                                                    String.format(Constants.COM_E_001));
                        }else if(over.flag == "2"){
                            Ext.Msg.alert(Constants.SYS_REMIND_MSG,
                                                    String.format(Constants.COM_I_004));
                                                    id.setValue("");
                                                txtreceiveMaterial.reset();
                                                gridStore.removeAll();
                                                storeWhsNoFrom.removeAll();
                                                storeMoveBackReason.removeAll();
                                                storeLocationNoFrom.removeAll();
                                                txtTransactionNo.reset();
                                                txtBillNo.reset();
                                                txtTransactionName.reset();
                                                txtItemNo.reset();
                                                txtMaterialNo.reset();
                                                txtMaterialName.reset();
												cldModifiedDate.reset();
												txtModifiedBy.reset();
                                                cbboxWhsNoFrom.reset();
                                                cbboxLocationNoFrom.reset();
                                                cbMoveBackReason.reset();
                                                txtMoveBackNumber.reset();
                                                txtRemark.reset();
                        }else if(over.flag == "3"){
                        	Ext.Msg.alert(Constants.SYS_REMIND_MSG,
                                                    String.format(Constants.COM_E_014));
                        }else{
                        	Ext.Msg.alert(Constants.SYS_REMIND_MSG,
                                                    String.format(Constants.COM_E_015));
                        }
                    }
                })
                }
    /**
     * moveBackGrid数据查询
     */
    function findReceiveMsg() {
    	gridStore.baseParams = {
			receiveNo : Ext.get('receive_No').dom.value
		};
 		gridStore.load({
					params : {
						start : 0,
						limit : Constants.PAGE_SIZE
					}
				});
		gridStore.on('load',function(){
			// BG IT-BUG-RS005-002
				/*if(!gridStore.getCount()>0){
                                Ext.Msg.alert(Constants.NOTICE,String.format(Constants.COM_I_003));
                            }
			alert(gridStore.getCount());*/
			if(gridStore.getCount()>0){
				validateBtn.setDisabled(false);
			}else{
				validateBtn.setDisabled(true);
			}
		}
		)
    }
    /**
     * 点击确认按钮判断是否执行
     */
    function validateBtnClick(){
        if(!moveBackGrid.selModel.hasSelection()){
            Ext.Msg.alert(Constants.SYS_REMIND_MSG, String.format(Constants.COM_I_001));
        } else {
            validateClick();
        }
    }
    /**
     * rowdbClickFn实行确认
     */
    function validateClick() {
		if ("" != cbMoveBackReason.getValue()
				|| "" != txtMoveBackNumber.getValue().toString()
				|| "" != txtRemark.getValue()
				|| "" != cbboxWhsNoFrom.getValue()
				|| "" != cbboxLocationNoFrom.getValue()) {
			Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_C_004,
					function(buttonobj) {
						if (buttonobj == "yes") {
							rowdbClickFn();
						}
					});
		} else {
			rowdbClickFn();
		}
	}

    /**
	 * moveBackGrid双击或点击确认按钮处理
	 */
    function rowdbClickFn() {
            var rec = moveBackGrid.getSelectionModel().getSelected();
            cbboxWhsNoFrom.reset();
            storeWhsNoFrom.removeAll();
            Ext.Ajax.request({
                        url : 'resource/getWareHouse.action',
                    params : {
                            order_No : rec.get('issueNo'),
                            sequence_No : rec.get('issueDetailsId'),
                            material_Id : rec.get('materialId')
                        },
                        success : function(result, request) {
							
                            var cbboxData = eval('(' + result.responseText
                                    + ')');
                            storeWhsNoFrom.loadData(cbboxData);
                            id.setValue(rec.get('issueDetailsId'));
                        },
                        failure : function(result, request) {
                        }
                    });
            cbboxLocationNoFrom.reset();
            storeLocationNoFrom.removeAll();
            storeWhsNoFrom.on('load',locationData);
            storeMoveBackReason.load();
            txtTransactionNo.setValue('R');
            txtTransactionName.setValue('退库');
            getCurrentDate();
            receive();
            Ext.get('receiveNo').dom.value = rec.get('issueNo');
            Ext.get('issueId').dom.value = rec.get('issueDetailsId');
            Ext.get('materialNo').dom.value = rec.get('materialNo');
            Ext.get('materialName').dom.value = rec.get('materialName');
            cbMoveBackReason.reset();
            txtMoveBackNumber.reset();
            txtRemark.reset();
        }
    /**
     * 取得库位信息
     */
        function locationData() {
        var rec = moveBackGrid.getSelectionModel().getSelected();
        storeLocationNoFrom.removeAll();
        Ext.Ajax.request({
                    url : 'resource/getMovebackLocationName.action',
                    params : {
                        order_No : rec.get('issueNo'),
                        sequence_No : rec.get('issueDetailsId'),
                        material_Id : rec.get('materialId'),
                        fromWhsId : cbboxWhsNoFrom.getValue('fromWhsId')
                    },
                    success : function(result, request) {
                        var cbboxData = eval('(' + result.responseText + ')');
                        storeLocationNoFrom.loadData(cbboxData);
                        if (storeLocationNoFrom.getCount()>0) {
                            cbboxLocationNoFrom.enable();
                        } else {
                            cbboxLocationNoFrom.disable();
                        }
                    },
                    failure : function(result, request) {
                    }
                });
    }
    /**
     * 仓库库位联动
     */
    cbboxWhsNoFrom.on('select', locationData);
    /**
     * 检查‘领料单号’是否入力
     */
    function checkReceiveNo() {
		var receiveNo = Ext.get('receive_No').dom.value;
		if ("" != cbMoveBackReason.getValue()
				|| "" != txtMoveBackNumber.getValue().toString()
				|| "" != txtRemark.getValue()
				|| "" != cbboxWhsNoFrom.getValue()
				|| "" != cbboxLocationNoFrom.getValue()) {
			Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_C_004,
					function(buttonobj) {
						if (buttonobj == "yes") {
							gridStore.removeAll();
							storeWhsNoFrom.removeAll();
							storeMoveBackReason.removeAll();
							storeLocationNoFrom.removeAll();
							txtTransactionNo.reset();
							txtBillNo.reset();
							txtTransactionName.reset();
							txtItemNo.reset();
							txtMaterialNo.reset();
							cldModifiedDate.reset();
							txtModifiedBy.reset();
							txtMaterialName.reset();
							cbboxWhsNoFrom.reset();
							cbboxLocationNoFrom.reset();
							cbMoveBackReason.reset();
							txtMoveBackNumber.reset();
							txtRemark.reset();
							findReceiveMsg();
						}
					})
		} else if ("" == receiveNo || null == receiveNo) {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, String.format(Constants.COM_E_002,
							"领料单号"));
		} else {
			findReceiveMsg();
		}
	}
    /**
	 * 获取当前日期并赋值给日期
	 */
    function getCurrentDate() {
        var curr_time = new Date();
        cldModifiedDate.setValue(curr_time.format("Y-m-d"));
    }
     /** 获取工号 */
    function receive() {
        Ext.Ajax.request({
                    url : 'workticket/getSession.action',
                    method : 'POST',
                    success : function(result, request) {
                        var o = eval('(' + result.responseText + ')');
                        txtModifiedBy.setValue(o.workerName.toString());
                    },
                    failure : function() {
                    }
                })
    }
})