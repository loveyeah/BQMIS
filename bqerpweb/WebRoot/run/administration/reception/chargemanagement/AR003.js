// 接待费用管理
// author:wangyun
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	Ext.QuickTips.init();
	// ********************弹出画面******************** //
	// 修改时间
	var hdnUpdateTime = new Ext.form.Hidden({
				id : 'updateTime',
				name : 'reception.updateTime',
				isFormField : true
			})

	var hdnId = new Ext.form.Hidden({
				id : 'id',
				name : 'reception.id'
			})

	// 审批编号 Textfield
	var txtId = new Ext.form.TextField({
				id : 'applyId',
				name : 'reception.applyId',
				fieldLabel : '审批单号',
				isFormField : true,
				anchor : '40%',
				readOnly : true
			})

	// 申请人 Textfield
	var txtName = new Ext.form.TextField({
				id : 'name',
				name : 'reception.name',
				fieldLabel : '申请人',
				isFormField : true,
				anchor : '90%',
				readOnly : true
			})

	// 申请部门 Textfield
	var txtDepName = new Ext.form.TextField({
				id : 'depName',
				name : 'reception.depName',
				fieldLabel : '申请部门',
				isFormField : true,
				anchor : '90%',
				readOnly : true
			})

	// 第一行
	var fldSetFirstLine = new Ext.form.FieldSet({
		layout : 'column',
		border : false,
		style : "padding-top:0px;padding-right:0px;padding-bottom:0px;margin-bottom:0px;border:0px",
		anchor : '100%',
		items : [{
					columnWidth : 0.5,
					border : false,
					layout : 'form',
					items : [txtName]
				}, {
					columnWidth : 0.5,
					border : false,
					layout : 'form',
					items : [txtDepName]
				}]

	})

	// 填表日期 Date
	var txtLogDate = new Ext.form.TextField({
				id : 'logDate',
				name : 'reception.logDate',
				fieldLabel : '填表日期',
				isFormField : true,
				anchor : '90%',
				readOnly : true
			})

	// 接待日期 Date
	var txtMeetDate = new Ext.form.TextField({
				id : 'meetDate',
				name : 'reception.meetDate',
				fieldLabel : '接待日期<font color=red>*</font>',
				isFormField : true,
				anchor : '90%',
				allowBlank : false,
				readOnly : true,
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

			})

	// 第二行
	var fldSetSecondLine = new Ext.form.FieldSet({
		layout : 'column',
		border : false,
		style : "padding-top:2px;padding-right:0px;padding-bottom:0px;margin-bottom:0px;border:0px",
		anchor : '100%',
		items : [{
					columnWidth : 0.5,
					border : false,
					layout : 'form',
					items : [txtLogDate]
				}, {
					columnWidth : 0.5,
					border : false,
					layout : 'form',
					items : [txtMeetDate]
				}]

	})

	// 就餐人数 TextField
	var txtRepastNum = new Ext.form.MoneyField({
				id : 'repastNum',
				fieldLabel : '就餐人数',
				isFormField : true,
				anchor : '90%',
				allowDecimals : false,
				allowNegative : false,
				style : 'text-align:right',
				maxLength : 4,
				appendChar : '人'
			})

	var hdnRepastNum = new Ext.form.Hidden({
				name : 'reception.repastNum'
			})

	// 住宿人数 TextField
	var txtRoomNum = new Ext.form.MoneyField({
				id : 'roomNum',
				fieldLabel : '住宿人数',
				isFormField : true,
				allowDecimals : false,
				allowNegative : false,
				style : 'text-align:right',
				anchor : '90%',
				maxLength : 4,
				appendChar : '人'
			})

	var hdnRoomNum = new Ext.form.Hidden({
				name : 'reception.roomNum'
			})

	// 第三行
	var fldSetThirdLine = new Ext.form.FieldSet({
		layout : 'column',
		border : false,
		anchor : '100%',
		style : "padding-top:2px;padding-right:0px;padding-bottom:0px;margin-bottom:0px;border:0px",
		items : [{
					columnWidth : 0.5,
					border : false,
					layout : 'form',
					items : [hdnRepastNum, txtRepastNum]
				}, {
					columnWidth : 0.5,
					border : false,
					layout : 'form',
					items : [hdnRoomNum, txtRoomNum]
				}]

	})

	// 接待说明 TextArea
	var txtMeetNote = new Ext.form.TextArea({
				id : 'meetNote',
				name : 'reception.meetNote',
				fieldLabel : '接待说明',
				isFormField : true,
				anchor : '96.5%',
				height : 40,
				maxLength : 30
			})

	// 第四行
	var fldSetForthLine = new Ext.form.FieldSet({
		border : false,
		layout : 'column',
		style : "padding-top:2px;padding-bottom:0px;margin-bottom:0px;border:0px",
		anchor : '100%',
		items : [{
					border : false,
					layout : 'form',
					items : [txtMeetNote]
				}]
	})

	// 接待申请 FieldSet
	var fldSetReception = new Ext.form.FieldSet({
				layout : 'form',
				title : '接待申请',
				border : true,
				autoHeight : true,
				width : 450,
				items : [fldSetFirstLine, fldSetSecondLine, fldSetThirdLine,
						fldSetForthLine]
			})

	// 就餐标准 TextField
	var txtRepastBz = new Ext.form.MoneyField({
				id : 'repastBz',
				fieldLabel : '就餐标准',
				isFormField : true,
				decimalPrecision : 2,
				padding : 2,
				style : 'text-align:right',
				anchor : '90%',
				appendChar : '元/人'
			})

	var hdnRepastBz = new Ext.form.Hidden({
				name : 'reception.repastBz'
			})

	// 住宿标准 TextField
	var txtRoomBz = new Ext.form.MoneyField({
				id : 'roomBz',
				fieldLabel : '住宿标准',
				isFormField : true,
				decimalPrecision : 2,
				padding : 2,
				style : 'text-align:right',
				anchor : '90%',
				appendChar : '元/人'
			})

	var hdnRoomBz = new Ext.form.Hidden({
				name : 'reception.roomBz'
			})

	// 第五行
	var fldSetFifthLine = new Ext.form.FieldSet({
		layout : 'column',
		border : false,
		anchor : '100%',
		style : "padding-top:5px;padding-right:0px;padding-bottom:0px;margin-bottom:0px;border:0px",
		items : [{
					columnWidth : 0.5,
					border : false,
					layout : 'form',
					items : [hdnRepastBz, txtRepastBz]
				}, {
					columnWidth : 0.5,
					border : false,
					layout : 'form',
					items : [hdnRoomBz, txtRoomBz]
				}]

	})

	// 就餐安排 TextArea
	var txtRepastPlan = new Ext.form.TextArea({
				id : 'repastPlan',
				name : 'reception.repastPlan',
				fieldLabel : '就餐安排',
				isFormField : true,
				anchor : '89.5%',
				height : 40,
				maxLength : 20
			})

	// 住宿安排 TextArea
	var txtRoomPlan = new Ext.form.TextArea({
				id : 'roomPlan',
				name : 'reception.roomPlan',
				fieldLabel : '住宿安排',
				isFormField : true,
				anchor : '89.5%',
				height : 40,
				maxLength : 20
			})

	// 第六行
	var fldSetSixthLine = new Ext.form.FieldSet({
		layout : 'column',
		border : false,
		anchor : '100%',
		style : "padding-top:2px;padding-right:0px;padding-bottom:0px;margin-bottom:0px;border:0px",
		items : [{
					columnWidth : 0.5,
					border : false,
					layout : 'form',
					items : [txtRepastPlan]
				}, {
					columnWidth : 0.5,
					border : false,
					layout : 'form',
					items : [txtRoomPlan]
				}]

	})

	// 其他 TextArea
	var txtOther = new Ext.form.TextArea({
				id : 'other',
				name : 'reception.other',
				fieldLabel : '其他',
				isFormField : true,
				height : 40,
				anchor : '96.5%',
				maxLength : 100
			})

	// 第七行
	var fldSetSeventhLine = new Ext.form.FieldSet({
		border : false,
		layout : 'column',
		style : 'padding-top:2px;padding-bottom:0px;margin-bottom:0px;border:0px',
		anchor : '100%',
		items : [{
					border : false,
					layout : 'form',
					items : [txtOther]
				}]
	})

	// 第二个 FieldSet
	var fldSetSecond = new Ext.form.FieldSet({
				layout : 'form',
				border : true,
				width : 450,
				autoHeight : true,
				items : [fldSetFifthLine, fldSetSixthLine, fldSetSeventhLine]
			})

	// 标准支出 TextField
	var txtPayoutBz = new Ext.form.MoneyField({
				id : 'payoutBz',
				fieldLabel : '标准支出',
				isFormField : true,
				decimalPrecision : 2,
				padding : 2,
				style : 'text-align:right',
				anchor : '50%',
				appendChar : '元'
			})

	var hdnPayoutBz = new Ext.form.Hidden({
				name : 'reception.payoutBz'
			})

	// 第八行
	var fldSetEighthLine = new Ext.form.FieldSet({
		border : false,
		layout : 'column',
		style : 'padding-top:5px;padding-bottom:0px;margin-bottom:0px;border:0px',
		anchor : '100%',
		items : [{
					border : false,
					layout : 'form',
					items : [hdnPayoutBz, txtPayoutBz]
				}]
	})

	// 实际支出 TextField
	var txtPayout = new Ext.form.MoneyField({
				id : 'payout',
				fieldLabel : '实际支出',
				isFormField : true,
				decimalPrecision : 2,
				padding : 2,
				style : 'text-align:right',
				anchor : '50%',
				appendChar : '元'
			})

	var hdnPayout = new Ext.form.Hidden({
				name : 'reception.payout'
			})

	// 第九行
	var fldSetNinethLine = new Ext.form.FieldSet({
		border : false,
		layout : 'column',
		style : 'padding-top:2px;padding-bottom:0px;margin-bottom:0px;border:0px',
		anchor : '100%',
		items : [{
					border : false,
					layout : 'form',
					items : [hdnPayout, txtPayout]
				}]
	})

	// 差额 TextField
	var txtBalance = new Ext.form.MoneyField({
				id : 'balance',
				fieldLabel : '差额',
				isFormField : true,
				style : 'text-align:right',
				readOnly : true,
				decimalPrecision : 2,
				padding : 2,
				anchor : '50%',
				appendChar : '元'
			})

	var hdnBalance = new Ext.form.Hidden({
				name : 'reception.balance'
			})

	// 第十行
	var fldSetTenthLine = new Ext.form.FieldSet({
		border : false,
		layout : 'column',
		style : 'padding-top:2px;padding-bottom:0px;margin-bottom:0px;border:0px',
		anchor : '100%',
		items : [{
					border : false,
					layout : 'form',
					items : [hdnBalance, txtBalance]
				}]
	})

	// 第三个Panel
	var fldSetThird = new Ext.form.FieldSet({
				layout : 'form',
				border : true,
				title : '费用核算',
				width : 450,
				autoHeight : true,
				items : [fldSetEighthLine, fldSetNinethLine, fldSetTenthLine]
			})

	// 弹出画面 Panel
	var pnlPop = new Ext.FormPanel({
				border : false,
				labelWidth : 70,
				width : 480,
				frame : true,
				labelAlign : 'right',
				autoScroll : true,
				items : [hdnId, hdnUpdateTime, txtId, fldSetReception,
						fldSetSecond, fldSetThird]
			})

	// 弹出画面
	var win = new Ext.Window({
				height : 320,
				width : 500,
				layout : 'fit',
				resizable : false,
				modal : true,
				autoScroll : true,
				closeAction : 'hide',
				buttonAlign : 'center',
				title : '修改接待费用',
				items : [pnlPop],
				buttons : [{
							text : Constants.BTN_SAVE,
							iconCls : Constants.CLS_SAVE,
							handler : saveHandler
						}, {
							text : Constants.BTN_CANCEL,
							iconCls : Constants.CLS_CANCEL,
							handler : cancelHandler
						}]
			})

	// ********************主画面******************** //
	// 修改 Button
	var btnUpdate = new Ext.Button({
				text : Constants.BTN_UPDATE,
				iconCls : Constants.CLS_UPDATE,
				handler : btnUpdateHandler
			})

	// 接待费用 DS
	var dsReceptionCharge = new Ext.data.JsonStore({
				url : 'administration/getReceptionCharge.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : ['id', 'applyId', 'name', 'depName', 'logDate',
						'meetDate', 'repastNum', 'roomNum', 'meetNote',
						'repastBz', 'roomBz', 'repastPlan', 'roomPlan',
						'other', 'payoutBz', 'payout', 'balance', 'updateTime']
			})

	dsReceptionCharge.load({
				params : {
					start : 0,
					limit : Constants.PAGE_SIZE
				}
			});

	dsReceptionCharge.setDefaultSort('id');

	// 接待费用 Column
	var cmReceptionCharge = new Ext.grid.ColumnModel([
			new Ext.grid.RowNumberer({
						header : '行号',
						width : 35
					}), {
				header : '申请人',
				width : 250,
				dataIndex : 'name',
				sortable : true,
				align : 'left'
			}, {
				header : '申请部门',
				width : 250,
				dataIndex : 'depName',
				sortable : true,
				align : 'left'
			}, {
				header : '填表日期',
				width : 130,
				dataIndex : 'logDate',
				sortabel : true,
				align : 'left'
			}, {
				header : '接待日期',
				width : 130,
				dataIndex : 'meetDate',
				sortable : true,
				align : 'left'
			}])

	// 接待费用 Tbar
	var tbarReceptionCharge = new Ext.Toolbar({
				items : [btnUpdate]
			})

	// 接待费用 Grid
	var gridReceptionCharge = new Ext.grid.GridPanel({
				layout : 'fit',
				border : false,
				store : dsReceptionCharge,
				cm : cmReceptionCharge,
				sm : new Ext.grid.RowSelectionModel({
							singleSelect : true
						}),
				enableColumnMove : false,
				enableColumnHide : true,
				tbar : tbarReceptionCharge,
				// 分页
				bbar : new Ext.PagingToolbar({
							pageSize : Constants.PAGE_SIZE,
							store : dsReceptionCharge,
							displayInfo : true,
							displayMsg : MessageConstants.DISPLAY_MSG,
							emptyMsg : MessageConstants.EMPTY_MSG
						})
			})

	// 主 Panel
	var panel = new Ext.Panel({
				layout : 'fit',
				border : false,
				items : [gridReceptionCharge]
			})

	new Ext.Viewport({
				layout : "fit",
				border : false,
				items : [{
							layout : 'fit',
							border : false,
							margins : '0 0 0 0',
							region : 'center',
							autoScroll : true,
							items : [panel]
						}]
			});

	// ********************主处理******************** //
	// 接待费用Grid双击处理
	gridReceptionCharge.on('rowdblclick', btnUpdateHandler);

	// 标准支出
	txtPayoutBz.on("change", function(t, n, o) {
				txtBalance.setValue(txtPayoutBz.getValue()
						- txtPayout.getValue())
			})

	// 实际支出
	txtPayout.on("change", function(t, n, o) {
				txtBalance.setValue(txtPayoutBz.getValue()
						- txtPayout.getValue())
			})

	/**
	 * 修改按钮处理
	 */
	function btnUpdateHandler() {
		if (gridReceptionCharge.selModel.hasSelection()) {
			// 如果选中一行则显示
			win.show();
			txtId.focus();
			var record = gridReceptionCharge.getSelectionModel().getSelected();
			hdnUpdateTime.setValue(record.get('updateTime'))
			pnlPop.getForm().loadRecord(record);
			if (txtPayoutBz.getValue() == null || txtPayoutBz.getValue() == '') {
				txtPayoutBz.setValue('0');
			}
			if (txtPayout.getValue() == null || txtPayout.getValue() == '') {
				txtPayout.setValue('0');
			}

		} else {
			if (dsReceptionCharge.getCount() > 0) {
				// 否则弹出提示信息
				Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
						'&nbsp&nbsp&nbsp' + MessageConstants.COM_I_001);
			}
		}
	}

	/**
	 * 弹出画面保存处理
	 */
	function saveHandler() {
		// 接待日期为空
		if (txtMeetDate.getValue() == "") {
			var msg = "";
			msg = String.format(MessageConstants.COM_E_002, "接待日期");
			Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, msg);
			return;
		}
		Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG,
				MessageConstants.COM_C_001, function(button) {
					if (button == 'yes') {
						if (!(txtRepastNum.isValid() && txtRoomNum.isValid()
								&& txtMeetNote.isValid()
								&& txtRepastBz.isValid() && txtRoomBz.isValid()
								&& txtRepastPlan.isValid()
								&& txtRoomPlan.isValid() && txtOther.isValid()
								&& txtPayout.isValid() && txtPayoutBz.isValid())) {
							return;
						}
						var record = gridReceptionCharge.getSelectionModel()
								.getSelected();
						hdnBalance.setValue(txtBalance.getValue());
						hdnPayout.setValue(txtPayout.getValue());
						hdnPayoutBz.setValue(txtPayoutBz.getValue());
						hdnRepastBz.setValue(txtRepastBz.getValue());
						hdnRepastNum.setValue(txtRepastNum.getValue());
						hdnRoomBz.setValue(txtRoomBz.getValue());
						hdnRoomNum.setValue(txtRoomNum.getValue());
						pnlPop.getForm().submit({
							url : 'administration/updateReceptionCharge.action',
							method : Constants.POST,
							params : {
								updateTime : record.get('updateTime')
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
											MessageConstants.COM_E_017);
									return;
								}
								if (result.msg == Constants.DATE_FAILURE) {
									Ext.Msg.alert(
											MessageConstants.SYS_ERROR_MSG,
											MessageConstants.COM_E_019);
									return;
								}
								// 保存成功
								Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
										MessageConstants.COM_I_004, function() {
											win.hide();
											dsReceptionCharge.load({
														params : {
															start : 0,
															limit : Constants.PAGE_SIZE
														}
													});
										});
							},
							failure : function() {
								// 保存失败
								Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
										MessageConstants.COM_E_014);
							}
						});
					}
				})
	}

	/**
	 * 取消处理
	 */
	function cancelHandler() {
		Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG,
				MessageConstants.COM_C_005, function(button) {
					if (button == 'yes') {
						pnlPop.getForm().reset();
						win.hide();
					}
				})
	}
})