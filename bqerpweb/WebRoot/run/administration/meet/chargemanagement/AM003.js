// 会务费用管理
// author:wangyun
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	Ext.QuickTips.init();

	/**
	 * 下载文件
	 */
	download = function(id) {
		document.all.blankFrame.src = "administration/downloadMeetFile.action?id="
				+ id;
	}

	// ********************主画面******************** //
	// ********************修改会务费用明细画面******************** //
	// 修改时间
	var hdnUpdateTime = new Ext.form.Hidden({
				id : 'updateTime',
				name : 'meet.meetUpdateTime',
				isFormField : true
			})

	// 审批单号
	var txtId = new Ext.form.TextField({
				id : 'meetId',
				name : 'meet.meetId',
				fieldLabel : '审批单号',
				isFormField : true,
				anchor : '45%',
				readOnly : true
			})

	// 申请人
	var txtName = new Ext.form.TextField({
				id : 'name',
				name : 'meet.name',
				fieldLabel : '申请人',
				isFormField : true,
				anchor : '95%',
				readOnly : true
			})

	// 申请部门
	var txtDepName = new Ext.form.TextField({
				id : 'depName',
				name : 'meet.depName',
				fieldLabel : '申请部门',
				isFormField : true,
				anchor : '95%',
				readOnly : true
			})

	// 第一行
	var fldsetFirstLine = new Ext.form.FieldSet({
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

	// 会议开始时间
	var txtStartMeetDate = new Ext.form.TextField({
				id : 'startMeetDate',
				name : 'meet.startMeetDate',
				fieldLabel : '会议开始时间<font color=red>*</font>',
				isFormField : true,
				anchor : '95%',
				readOnly : true,
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd HH:mm',
									alwaysUseStartDate : false,
									isShowClear : false
								});
					}
				}
			})

	// 会议结束时间
	var txtEndMeetDate = new Ext.form.TextField({
				id : 'endMeetDate',
				name : 'meet.endMeetDate',
				fieldLabel : '会议结束时间',
				isFormField : true,
				anchor : '95%',
				readOnly : true,
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd HH:mm',
									alwaysUseStartDate : false
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
					items : [txtStartMeetDate]
				}, {
					columnWidth : 0.5,
					border : false,
					layout : 'form',
					items : [txtEndMeetDate]
				}]
	})

	// 会议名称
	var txtMeetName = new Ext.form.TextField({
				id : 'meetName',
				name : 'meet.meetName',
				fieldLabel : '会议名称<font color=red>*</font>',
				isFormField : true,
				allowBlank : false,
				anchor : '97.5%',
				maxLength : 50
			})

	// 第三行
	var fldSetThirdLine = new Ext.form.FieldSet({
		layout : 'column',
		border : false,
		style : "padding-top:2px;padding-right:0px;padding-bottom:0px;margin-bottom:0px;border:0px",
		anchor : '100%',
		items : [{
					border : false,
					layout : 'form',
					items : [txtMeetName]
				}]
	})

	// 会议地点
	var txtMeetPlace = new Ext.form.TextField({
				id : 'meetPlace',
				name : 'meet.meetPlace',
				fieldLabel : '会议地点',
				isFormField : true,
				anchor : '97.5%',
				maxLength : 25
			})

	// 第四行
	var fldSetForthLine = new Ext.form.FieldSet({
		layout : 'column',
		border : false,
		style : "padding-top:2px;padding-right:0px;padding-bottom:0px;margin-bottom:0px;border:0px",
		anchor : '100%',
		items : [{
					border : false,
					layout : 'form',
					items : [txtMeetPlace]
				}]
	})

	// 会场要求
	var txtRoomNeed = new Ext.form.TextField({
				id : 'roomNeed',
				name : 'meet.roomNeed',
				fieldLabel : '会场要求',
				isFormField : true,
				anchor : '97.5%',
				maxLength : 75
			})

	// 第五行
	var fldSetFifthLine = new Ext.form.FieldSet({
		layout : 'column',
		border : false,
		style : "padding-top:2px;padding-right:0px;padding-bottom:0px;margin-bottom:0px;border:0px",
		anchor : '100%',
		items : [{
					border : false,
					layout : 'form',
					items : [txtRoomNeed]
				}]
	})

	// 会议其他要求
	var txtMeetOther = new Ext.form.TextArea({
				id : 'meetOther',
				name : 'meet.meetOther',
				fieldLabel : '会议其他要求',
				isFormField : true,
				anchor : '97%',
				height : 40,
				maxLength : 100
			})

	// 第六行
	var fldSetSixthLine = new Ext.form.FieldSet({
		layout : 'column',
		border : false,
		style : "padding-top:2px;padding-right:0px;padding-bottom:0px;margin-bottom:0px;border:0px",
		anchor : '100%',
		items : [{
					border : false,
					layout : 'form',
					items : [txtMeetOther]
				}]
	})

	// 会议就餐时间
	var txtDinnerTime = new Ext.form.TextField({
				id : 'dinnerTime',
				name : 'meet.dinnerTime',
				fieldLabel : '会议就餐时间',
				isFormField : true,
				anchor : '95%',
				readOnly : true,
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd HH:mm',
									alwaysUseStartDate : false
								});
					}
				}
			})

	// 会议就餐人数
	var txtDinnerNum = new Ext.form.MoneyField({
				id : 'dinnerNum',
				fieldLabel : '会议就餐人数',
				isFormField : true,
				allowDecimals : false,
				allowNegative : false,
				anchor : '95%',
				style : 'text-align:right',
				maxLength : 8,
				appendChar : '人'
			})

	var hdnDinnerNum = new Ext.form.Hidden({
				name : 'meet.dinnerNum'
			})

	// 第七行
	var fldSetSeventhLine = new Ext.form.FieldSet({
		layout : 'column',
		border : false,
		style : "padding-top:2px;padding-right:0px;padding-bottom:0px;margin-bottom:0px;border:0px",
		anchor : '100%',
		items : [{
					columnWidth : 0.5,
					border : false,
					layout : 'form',
					items : [txtDinnerTime]
				}, {
					columnWidth : 0.5,
					border : false,
					layout : 'form',
					items : [txtDinnerNum, hdnDinnerNum]
				}]
	})

	// 参会人员及会场情况
	var fldsetMeetRoom = new Ext.form.FieldSet({
				layout : 'form',
				title : '参会人员及会场情况',
				border : true,
				labelWidth : 87,
				autoHeight : true,
				width : 470,
				items : [hdnUpdateTime, fldsetFirstLine, fldSetSecondLine,
						fldSetThirdLine, fldSetForthLine, fldSetFifthLine,
						fldSetSixthLine, fldSetSeventhLine]
			})

	// 名称
	var txtCigName = new Ext.form.TextField({
				id : 'cigName',
				name : 'meet.cigName',
				fieldLabel : '名称',
				anchor : '95%',
				maxLength : 25
			})

	// 价格
	var txtCigPrice = new Ext.form.MoneyField({
				id : 'cigPrice',
				fieldLabel : '价格',
				anchor : '95%',
				maxLength : 14,
				decimalPrecision : 2,
				padding : 2,
				style : 'text-align:right',
				appendChar : '元'
			})

	var hdnCigPrice = new Ext.form.Hidden({
				name : 'meet.cigPrice'

			})

	// 数量
	var txtCigNum = new Ext.form.MoneyField({
				id : 'cigNum',
				fieldLabel : '数量',
				anchor : '95%',
				maxLength : 8,
				allowDecimals : false,
				allowNegative : false,
				style : 'text-align:right',
				appendChar : '包'
			})

	var hdnCigNum = new Ext.form.Hidden({
				name : 'meet.cigNum'
			})

	// 会议用烟
	var fldsetCig = new Ext.form.FieldSet({
				layout : 'form',
				title : '会议用烟',
				border : true,
				autoHeight : true,
				items : [txtCigName, txtCigPrice, txtCigNum, hdnCigPrice,
						hdnCigNum]
			})

	// 名称
	var txtWineName = new Ext.form.TextField({
				id : 'wineName',
				name : 'meet.wineName',
				fieldLabel : '名称',
				anchor : '95%',
				maxLength : 25
			})

	// 价格
	var txtWinePrice = new Ext.form.MoneyField({
				id : 'winePrice',
				fieldLabel : '价格',
				anchor : '95%',
				maxLength : 14,
				decimalPrecision : 2,
				padding : 2,
				style : 'text-align:right',
				appendChar : '元'
			})

	var hdnWinePrice = new Ext.form.Hidden({
				name : 'meet.winePrice'
			})

	// 数量
	var txtWineNum = new Ext.form.MoneyField({
				id : 'wineNum',
				fieldLabel : '数量',
				anchor : '95%',
				maxLength : 8,
				allowDecimals : false,
				allowNegative : false,
				style : 'text-align:right',
				appendChar : '瓶'
			})

	var hdnWineNum = new Ext.form.Hidden({
				name : 'meet.wineNum'
			})

	// 会议用酒
	var fldsetWine = new Ext.form.FieldSet({
				layout : 'form',
				title : '会议用酒',
				border : true,
				autoHeight : true,
				items : [txtWineName, txtWinePrice, txtWineNum, hdnWinePrice,
						hdnWineNum]
			})

	// 会议用烟和用酒
	var fldsetCigWine = new Ext.Panel({
				layout : 'column',
				border : false,
				autoHeight : true,
				labelWidth : 50,
				width : 470,
				style : 'padding-top:2px',
				items : [{
							columnWidth : 0.47,
							border : false,
							items : [fldsetCig]
						}, {
							columnWidth : 0.06,
							border : false
						}, {
							columnWidth : 0.47,
							border : false,
							items : [fldsetWine]
						}]
			})

	// 套间数量
	var txtTfNum = new Ext.form.MoneyField({
				id : 'tfNum',
				fieldLabel : '套间数量',
				anchor : '95%',
				maxLength : 8,
				allowDecimals : false,
				allowNegative : false,
				style : 'text-align:right',
				appendChar : '间'
			})

	var hdnTfNum = new Ext.form.Hidden({
				name : 'meet.tfNum'
			})

	// 套间用品
	var txtTfThing = new Ext.form.TextField({
				id : 'tfThing',
				name : 'meet.tfThing',
				fieldLabel : '套间用品',
				anchor : '95%',
				maxLength : 50
			})

	// 套间用品选择
	var btnTfChoose = new Ext.Button({
				text : '···',
				align : 'center',
				handler : btnTfChooseHandler
			})

	// 单间数量
	var txtDjNum = new Ext.form.MoneyField({
				id : 'djNum',
				fieldLabel : '单间数量',
				anchor : '95%',
				maxLength : 8,
				allowDecimals : false,
				allowNegative : false,
				style : 'text-align:right',
				appendChar : '间'
			})

	var hdnDjNum = new Ext.form.Hidden({
				name : 'meet.djNum'
			})

	// 单间用品
	var txtDjThing = new Ext.form.TextField({
				id : 'djThing',
				name : 'meet.djThing',
				fieldLabel : '单间用品',
				anchor : '95%',
				maxLength : 50
			})

	// 单间用品选择
	var btnDjChoose = new Ext.Button({
				text : '···',
				align : 'center',
				handler : btnDjChooseHandler
			})

	// 标间数量
	var txtBjNum = new Ext.form.MoneyField({
				id : 'bjNum',
				fieldLabel : '标间数量',
				anchor : '95%',
				allowDecimals : false,
				allowNegative : false,
				maxLength : 8,
				style : 'text-align:right',
				appendChar : '间'
			})

	var hdnBjNum = new Ext.form.Hidden({
				name : 'meet.bjNum'
			})

	// 标间用品
	var txtBjThing = new Ext.form.TextField({
				id : 'bjThing',
				name : 'meet.bjThing',
				fieldLabel : '标间用品',
				anchor : '95%',
				maxLength : 50
			})

	// 标间用品选择
	var btnBjChoose = new Ext.Button({
				text : '···',
				align : 'center',
				handler : btnBjChooseHandler
			})

	// 会议就餐标准
	var txtDinnerBz = new Ext.form.MoneyField({
				id : 'dinnerBz',
				fieldLabel : '会议就餐标准',
				anchor : '95%',
				style : 'text-align:right',
				padding : 2,
				decimalPrecision : 2,
				maxLength : 19,
				appendChar : '元'
			})

	var hdnDinnerBz = new Ext.form.Hidden({
				name : 'meet.dinnerBz'
			})

	// 费用预算汇总
	var txtBudpayInall = new Ext.form.MoneyField({
				id : 'budpayInall',
				fieldLabel : '费用预算汇总',
				anchor : '95%',
				align : 'right',
				readOnly : true,
				padding : 2,
				decimalPrecision : 2,
				style : 'text-align:right',
				maxLength : 19,
				appendChar : '元'
			})

	var hdnBudpayInall = new Ext.form.Hidden({
				name : 'meet.budpayInall'
			})

	// 实际费用汇总
	var txtRealpayInall = new Ext.form.MoneyField({
				id : 'realpayInall',
				fieldLabel : '实际费用汇总',
				anchor : '95%',
				align : 'right',
				readOnly : true,
				padding : 2,
				decimalPrecision : 2,
				style : 'text-align:right',
				maxLength : 14,
				appendChar : '元'
			})

	var hdnRealpayInall = new Ext.form.Hidden({
				name : 'meet.realpayInall'
			})

	var fldsetRealpay = new Ext.form.FieldSet({
		border : false,
		style : "padding-top:2px;padding-right:0px;padding-bottom:0px;margin-bottom:0px;border:0px",
		items : [txtRealpayInall, hdnRealpayInall]
	})

	// 会议住宿及就餐费用
	var fldsetRoomDinner = new Ext.form.FieldSet({
		layout : 'form',
		title : '会议住宿及就餐费用',
		border : true,
		autoHeight : true,
		style : 'padding-top:2px',
		labelWidth : 60,
		width : 470,
		items : [{
			layout : 'column',
			style : "padding-top:2px;padding-right:0px;padding-bottom:0px;margin-bottom:0px;border:0px",
			border : false,
			items : [{
						columnWidth : 0.35,
						border : false,
						layout : 'form',
						items : [txtTfNum, hdnTfNum]
					}, {
						columnWidth : 0.55,
						border : false,
						layout : 'form',
						items : [txtTfThing]
					}, {
						columnWidth : 0.1,
						border : false,
						layout : 'form',
						items : [btnTfChoose]
					}]
		}, {
			layout : 'column',
			style : "padding-top:2px;padding-right:0px;padding-bottom:0px;margin-bottom:0px;border:0px",
			border : false,
			items : [{
						columnWidth : 0.35,
						border : false,
						layout : 'form',
						items : [txtDjNum, hdnDjNum]
					}, {
						columnWidth : 0.55,
						border : false,
						layout : 'form',
						items : [txtDjThing]
					}, {
						columnWidth : 0.1,
						border : false,
						layout : 'form',
						items : [btnDjChoose]
					}]
		}, {
			layout : 'column',
			style : "padding-top:2px;padding-right:0px;padding-bottom:0px;margin-bottom:0px;border:0px",
			border : false,
			items : [{
						columnWidth : 0.35,
						border : false,
						layout : 'form',
						items : [txtBjNum, hdnBjNum]
					}, {
						columnWidth : 0.55,
						border : false,
						layout : 'form',
						items : [txtBjThing]
					}, {
						columnWidth : 0.1,
						border : false,
						layout : 'form',
						items : [btnBjChoose]
					}]
		}, {
			layout : 'column',
			labelWidth : 100,
			style : "padding-top:2px;padding-right:0px;padding-bottom:0px;margin-bottom:0px;border:0px",
			border : false,
			items : [{
						columnWidth : 0.5,
						border : false,
						layout : 'form',
						items : [txtDinnerBz, hdnDinnerBz]
					}, {
						columnWidth : 0.5,
						border : false,
						layout : 'form',
						items : [txtBudpayInall, hdnBudpayInall]
					}]
		}, {
			layout : 'column',
			labelWidth : 90,
			style : "padding-top:2px;padding-right:0px;padding-bottom:0px;margin-bottom:0px;border:0px",
			border : false,
			items : [{
						columnWidth : 0.5,
						border : false,
						layout : 'form',
						items : [fldsetRealpay]
					}, {
						columnWidth : 0.5,
						border : false,
						layout : 'form'
					}]
		}]
	})

	var ckb1 = new Ext.form.Checkbox({
				boxLabel : '欢迎书签',
				id : 'ckb1',
				checked : false
			})

	var ckb2 = new Ext.form.Checkbox({
				boxLabel : '矿泉水',
				id : 'ckb2',
				checked : false
			})

	var ckb3 = new Ext.form.Checkbox({
				boxLabel : '水果',
				id : 'ckb3',
				checked : false
			})

	var ckb4 = new Ext.form.Checkbox({
				boxLabel : '鲜花',
				id : 'ckb4',
				checked : false
			})

	var ckb5 = new Ext.form.Checkbox({
				boxLabel : '烟',
				id : 'ckb5',
				checked : false
			})

	// 会议住宿房间物品选择 FieldSet
	var fldsetRoom = new Ext.form.FieldSet({
				layout : 'column',
				border : false,
				anchor : '100%',
				style : 'padding-top:7px',
				items : [{
							columnWidth : 0.2,
							border : false,
							items : [ckb1]
						}, {
							columnWidth : 0.2,
							border : false,
							items : [ckb2]
						}, {
							columnWidth : 0.2,
							border : false,
							items : [ckb3]
						}, {
							columnWidth : 0.2,
							border : false,
							items : [ckb4]
						}, {
							columnWidth : 0.2,
							border : false,
							items : [ckb5]
						}]
			})

	// 会议住宿房间物品选择 Panel
	var pnlRoom = new Ext.Panel({
				border : false,
				autoHeight : true,
				frame : true,
				autoWidth : true,
				items : [fldsetRoom]
			})

	// 会议住宿房间物品选择
	var winRoom = new Ext.Window({
				height : 100,
				width : 400,
				layout : 'fit',
				resizable : false,
				modal : true,
				closeAction : 'hide',
				buttonAlign : 'center',
				title : '会议住宿房间物品选择',
				items : [pnlRoom],
				buttons : [{
							text : '提交',
							handler : commitHandler
						}]
			})

	// 参会人员 会议材料 会议桌签 DS
	var dsMatieria = new Ext.data.JsonStore({
				url : 'administration/getMeetFile.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : ['id', 'fileName', 'meetId', 'meetFileUpdateTime']
			})

	// 参会人员 会议材料 会议桌签 cm
	var cmMatieria = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
						header : '行号',
						width : 35
					}), {
				header : '文件名',
				width : 300,
				dataIndex : 'fileName',
				renderer : fileName,
				sortable : true,
				align : 'left'
			}, {
				header : '删除附件',
				width : 70,
				renderer : dele
			}])

	// 附件
	var tfAppend = new Ext.form.TextField({
				id : "fileUpload",
				inputType : 'file',
				name : 'fileUpload',
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
				layout : 'fit',
				ds : dsMatieria,
				cm : cmMatieria,
				tbar : tbarQuestFile,
				style : 'padding-top:0px',
				autoWidth : true,
				autoScroll : true,
				enableColumnMove : false,
				enableColumnHide : true,
				border : false
			})

	// 会议材料及会议桌签
	var fldsetMatieria = new Ext.form.FieldSet({
				layout : 'fit',
				title : '会议材料及会议桌签',
				border : true,
				width : 470,
				height : 200,
				items : [gridMatieria]
			})

	var fpnlMatieria = new Ext.FormPanel({
		border : false,
		autoScroll : true,
		autoHeight : true,
		autoWidth : true,
		fileUpload : true,
		style : 'padding-top:5px;padding-left:0px;padding-right:0px;margin-right:0px',
		items : [fldsetMatieria]
	})

	// 数据集
	var recordDetail = new Ext.data.Record.create([{
				name : 'id'
			}, {
				name : 'payName'
			}, {
				name : 'payBudget'
			}, {
				name : 'payReal'
			}, {
				name : 'note'
			}, {
				name : 'updateTime'
			}])

	// 费用明细DS
	var dsDetail = new Ext.data.JsonStore({
				url : 'administration/getChargeDetail.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : recordDetail,
				listeners : {
					load : function(ds, records, o) {
						addLine();
					}
				}
			})

	// 费用名称
	var txtPayName = new Ext.form.TextField({
				id : 'payName',
				maxLength : 50
			})

	// 实际费用
	var txtPayReal = new Ext.form.NumberField({
				id : 'payReal',
				maxLength : 19
			})

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
						var rec = gridDetail.getSelectionModel()
								.getSelections();
						if (memoText.isValid()) {
							var tempValue = memoText.getValue().replace(/\n/g,
									'');
							rec[0].set("note", tempValue);
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

	// 费用明细cm
	var cmDetail = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
						header : "行号",
						width : 35
					}), {
				sortable : true,
				header : '费用名称',
				dataIndex : 'payName',
				width : 160,
				editor : txtPayName,
				align : 'left'
			}, {
				sortable : true,
				header : '预算费用',
				dataIndex : 'payBudget',
				width : 90,
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
							totalSum += store.getAt(i).get('payBudget') * 1;
						}
						return renderNumber(totalSum);
					}
				},
				align : 'right'
			}, {
				sortable : true,
				header : '实际费用',
				dataIndex : 'payReal',
				width : 90,
				editor : txtPayReal,
				renderer : function(value, cellmeta, record, rowIndex,
						columnIndex, store) {
					// 如果不是最后一行
					if (rowIndex < store.getCount() - 1) {
						var totalSum = 0;
						// record.set('payReal', value)
						for (var i = 0; i < store.getCount() - 1; i++) {
							totalSum += store.getAt(i).get('payReal') * 1;
						}
						if (store.getAt(store.getCount() - 1).get('countType') == 'total') {
							store.getAt(store.getCount() - 1).set('payReal',
									totalSum);
							txtRealpayInall.setValue(totalSum);

						}
						return renderNumber(value);
						// 如果是最后一行
					} else {
						var totalSum = 0;
						// 对该列除最后一个单元格以为求和
						for (var i = 0; i < store.getCount() - 1; i++) {
							totalSum += store.getAt(i).get('payReal') * 1;
						}
						return renderNumber(totalSum);
					}
				},
				align : 'right'
			}, {
				sortable : true,
				header : '备注',
				dataIndex : 'note',
				width : 80,
				editor : txtNote,
				align : 'left'
			}])

	// 新增明细
	var btnAddDetail = new Ext.Button({
				text : '新增明细',
				align : 'center',
				handler : btnAddDetailHandler
			})

	// 删除明细
	var btnDeleteDetail = new Ext.Button({
				text : '删除明细',
				align : 'center',
				handler : btnDeleteDetailHandler
			})

	// 取消明细
	var btnCancelDetail = new Ext.Button({
				text : '取消明细',
				align : 'center',
				handler : btnCancelDetailHandler
			})

	// 费用明细tbar
	var tbarDetail = new Ext.Toolbar({
				items : [btnAddDetail, btnDeleteDetail, btnCancelDetail]
			})

	var rsm = new Ext.grid.RowSelectionModel({
				singleSelect : false
			});

	// 费用明细grid
	var gridDetail = new Ext.grid.EditorGridPanel({
				layout : 'fit',
				store : dsDetail,
				sm : rsm,
				cm : cmDetail,
				enableColumnMove : false,
				enableColumnHide : true,
				width : 470,
				height : 200,
				// 单击修改
				clicksToEdit : 1,
				border : false,
				tbar : tbarDetail
			})

	// 弹出画面
	var pnlPop = new Ext.FormPanel({
		border : false,
		autoScroll : true,
		autoHeight : true,
		autoWidth : true,
		labelAlign : 'right',
		style : 'padding-top:2px;padding-left:0px;padding-right:0px;margin-right:0px',
		items : [txtId, fldsetMeetRoom, fldsetCigWine, fldsetRoomDinner]
	})

	var pnlWin = new Ext.Panel({
				border : false,
				autoScroll : true,
				autoHeight : true,
				autoWidth : true,
				frame : true,
				items : [pnlPop, fpnlMatieria, gridDetail]
			})

	// 弹出画面
	var win = new Ext.Window({
				height : 500,
				width : 520,
				layout : 'fit',
				resizable : false,
				modal : true,
				autoScroll : true,
				closeAction : 'hide',
				buttonAlign : 'center',
				title : '修改会务费用明细',
				items : [pnlWin],
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
	// ********************会务费用管理画面******************** //
	// 修改
	var btnUpdate = new Ext.Button({
				text : Constants.BTN_UPDATE,
				iconCls : Constants.CLS_UPDATE,
				align : 'center',
				handler : btnUpdateHandler
			})

	// 会务费用管理
	var dsMeetCharge = new Ext.data.JsonStore({
				url : 'administration/getMeetChargeInfo.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : ['meetId', 'name', 'depName', 'startMeetDate',
						'endMeetDate', 'meetName', 'roomNeed', 'meetPlace',
						'meetOther', 'dinnerTime', 'dinnerNum', 'cigName',
						'cigPrice', 'cigNum', 'wineName', 'winePrice',
						'wineNum', 'tfNum', 'tfThing', 'djNum', 'djThing',
						'bjNum', 'bjThing', 'dinnerBz', 'budpayInall',
						'realpayInall', 'meetUpdateTime']
			})

	dsMeetCharge.load({
				params : {
					start : 0,
					limit : Constants.PAGE_SIZE
				}
			});

	dsMeetCharge.setDefaultSort('meetId');

	// 会务费用管理
	var cmMeetCharge = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
						header : '行号',
						width : 35,
						align : 'right'
					}), {
				header : '申请人',
				width : 180,
				dataIndex : 'name',
				sortable : true,
				align : 'left'
			}, {
				header : '会议名称',
				width : 250,
				dataIndex : 'meetName',
				sortable : true,
				align : 'left'
			}, {
				header : '会议开始时间',
				width : 160,
				dataIndex : 'startMeetDate',
				sortabel : true,
				align : 'left'
			}, {
				header : '会议结束时间',
				width : 160,
				dataIndex : 'endMeetDate',
				sortable : true,
				align : 'left'
			}])

	// 会务费用管理
	var tbarMeetCharge = new Ext.Toolbar({
				items : [btnUpdate]
			})

	// 会务费用管理
	var gridMeetCharge = new Ext.grid.GridPanel({
				layout : 'fit',
				border : false,
				store : dsMeetCharge,
				cm : cmMeetCharge,
				sm : new Ext.grid.RowSelectionModel({
							singleSelect : true
						}),
				enableColumnMove : false,
				enableColumnHide : true,
				tbar : tbarMeetCharge,
				// 分页
				bbar : new Ext.PagingToolbar({
							pageSize : Constants.PAGE_SIZE,
							store : dsMeetCharge,
							displayInfo : true,
							displayMsg : MessageConstants.DISPLAY_MSG,
							emptyMsg : MessageConstants.EMPTY_MSG
						})
			})

	// 主Panel
	var panel = new Ext.Panel({
				layout : 'fit',
				border : false,
				items : [gridMeetCharge]
			})

	new Ext.Viewport({
				layout : 'fit',
				border : false,
				items : [{
							layout : 'fit',
							border : false,
							margins : '0 0 0 0',
							region : 'center',
							items : [panel]
						}]
			})

	// *******************主处理******************** //
	// 保存明细部信息
	var originalDetailData = []
	// 保存已删除的明细部ID
	var deleteDetailId = []
	// 住宿Flag
	var flgRoom = 0;
	// grid双击处理
	gridMeetCharge.on("rowdblclick", function() {
				btnUpdateHandler();
			})

	rsm.on("rowselect", rowSelectedHandler);
	function rowSelectedHandler(rsm, rowIndex) {
		row = rowIndex;
	}

	// 明细表合计行禁用
	gridDetail.on("beforeedit", function(obj) {
				if (obj.row == dsDetail.getCount() - 1) {
					obj.cancel = true;
				}
			})

	/**
	 * 拷贝明细部信息
	 */
	function cloneLocationRecord(record) {
		var objClone = new Object();
		// 拷贝属性
		objClone['id'] = record['id'];
		objClone['payName'] = record['payName'];
		objClone['payBudget'] = record['payBudget'];
		objClone['payReal'] = record['payReal'];
		objClone['note'] = record['note'];
		objClone['updateTime'] = record['updateTime'];
		return objClone;
	}

	/**
	 * 获取明细信息，db已存在的和新增加的数据分开保存
	 */
	function getDetailList(isNew) {
		// 记录
		var records = new Array();
		// 循环
		for (var index = 0; index < dsDetail.getCount() - 1; index++) {
			var record = dsDetail.getAt(index).data;
			if (isNew) {
				// 新记录
				if (record.isNewRecord) {
					records.push(cloneLocationRecord(record));
				}
			} else {
				// db中原有记录
				if (!record.isNewRecord) {
					records.push(cloneLocationRecord(record));
				}
			}
		}
		return records;
	}

	/**
	 * 修改按钮处理
	 */
	function btnUpdateHandler() {
		// 没有选中一行
		if (!gridMeetCharge.selModel.hasSelection()) {
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
					MessageConstants.COM_I_001);
		} else {
			// 弹出画面
			win.show();
			txtId.focus();
			var record = gridMeetCharge.getSelectionModel().getSelected();
			hdnUpdateTime.setValue(record.get('meetUpdateTime'));
			// 导入数据到画面
			pnlPop.getForm().loadRecord(record);
			// 会议附件信息
			dsMatieria.load({
						params : {
							meetId : record.get('meetId')
						}
					})
			// 费用明细数据
			dsDetail.load({
						params : {
							meetId : record.get('meetId')
						}
					})

			// 保存明細信息
			dsDetail.on("load", function() {
						originalDetailData = getDetailList();
					})
		}
	}

	/**
	 * 保存会议信息
	 */
	function saveMeetInfo() {
		hdnBjNum.setValue(txtBjNum.getValue());
		hdnBudpayInall.setValue(txtBudpayInall.getValue());
		hdnCigNum.setValue(txtCigNum.getValue());
		hdnCigPrice.setValue(txtCigPrice.getValue());
		hdnDinnerBz.setValue(txtDinnerBz.getValue());
		hdnDinnerNum.setValue(txtDinnerNum.getValue());
		hdnDjNum.setValue(txtDjNum.getValue());
		hdnRealpayInall.setValue(txtRealpayInall.getValue());
		hdnTfNum.setValue(txtTfNum.getValue());
		hdnWineNum.setValue(txtWineNum.getValue());
		hdnWinePrice.setValue(txtWinePrice.getValue());
		pnlPop.getForm().submit({
			url : 'administration/saveMeetInfo.action',
			method : Constants.POST,
			params : {
				// 新增加的明细记录
				newDetail : Ext.util.JSON.encode(getDetailList(true)),
				// 修改过的明细db记录
				dbDetail : Ext.util.JSON.encode(getDetailList()),
				// 删除的明细id集
				deleteDetailId : Ext.util.JSON.encode(deleteDetailId),
				// 最后修改时间
				meetUpdateTime : hdnUpdateTime.getValue()
			},
			success : function(form, action) {
				var result = eval("(" + action.response.responseText + ")");
				if (result.msg == Constants.DATE_FAILURE) {
					Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
							MessageConstants.COM_E_019);
					return;
				}
				if (result.msg == Constants.DATA_USING) {
					Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
							MessageConstants.COM_I_002);
					return;
				}
				// 保存成功
				Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
						MessageConstants.COM_I_004, function() {
							win.hide();
							originalDetailData = [];
							deleteDetailId = [];
							dsMeetCharge.load({
										params : {
											start : 0,
											limit : Constants.PAGE_SIZE
										}
									});
						});
			},
			failure : function(form, action) {
				// 保存失败
				Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
						MessageConstants.COM_E_014);
			}
		})
	}

	/**
	 * 保存按钮处理
	 */
	function saveHandler() {
		var msg = "";
		var flgCode = false;
		// 会议名称为空
		if (!txtMeetName.getValue()) {
			msg = String.format(MessageConstants.COM_E_002, "会议名称");
			flgCode = true;
		}
		// 会议开始时间
		if (!txtStartMeetDate.getValue()) {
			msg = String.format(MessageConstants.COM_E_002, "会议开始时间");
			flgCode = true;
		}
		if (flgCode) {
			Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, msg);
			return;
		}

		// 时间比较
		var startTime = txtStartMeetDate.getValue();
		var endTime = txtEndMeetDate.getValue();
		if (endTime != "" && startTime > endTime) {
			var msg = String.format(MessageConstants.COM_E_009, "开始时间", "结束时间");
			Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, msg);
			return;
		}

		Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG,
				MessageConstants.COM_C_001, function(button) {
					if (button == 'yes') {

						// 控件输入是否有效
						if (!(txtStartMeetDate.isValid()
								&& txtEndMeetDate.isValid()
								&& txtMeetName.isValid()
								&& txtMeetPlace.isValid()
								&& txtRoomNeed.isValid()
								&& txtMeetOther.isValid()
								&& txtDinnerTime.isValid()
								&& txtDinnerNum.isValid()
								&& txtCigName.isValid() && txtCigNum.isValid()
								&& txtCigPrice.isValid()
								&& txtWineName.isValid()
								&& txtWineNum.isValid()
								&& txtWinePrice.isValid() && txtTfNum.isValid()
								&& txtTfThing.isValid() && txtDjNum.isValid()
								&& txtDjThing.isValid() && txtBjNum.isValid()
								&& txtBjThing.isValid()
								&& txtDinnerBz.isValid()
								&& txtBudpayInall.isValid() && txtRealpayInall
								.isValid())) {
							return;
						}

						saveMeetInfo();
						return;
					}
				})
	}

	/**
	 * 取消按钮处理
	 */
	function cancelHandler() {
		Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG,
				MessageConstants.COM_C_005, function(button) {
					if (button == 'yes') {
						originalDetailData = [];
						deleteDetailId = [];
						win.hide();
					}
				})
	}

	/**
	 * 套间用品选择按钮处理
	 */
	function btnTfChooseHandler() {
		flgRoom = 1;
		if (txtTfThing.getValue() != null && txtTfThing.getValue() != "") {
			// 是否重新选择
			Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG,
					MessageConstants.AR001_C_001, function(button) {
						if (button == 'yes') {
							winRoom.show();
						}
					});
		} else {
			winRoom.show();
		}

	}

	/**
	 * 单间用品选择按钮处理
	 */
	function btnDjChooseHandler() {
		flgRoom = 2;
		if (txtDjThing.getValue() != null && txtDjThing.getValue() != "") {
			// 是否重新选择
			Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG,
					MessageConstants.AR001_C_001, function(button) {
						if (button == 'yes') {
							winRoom.show();
						}
					});
		} else {
			winRoom.show();
		}

	}

	/**
	 * 标间用品选择按钮处理
	 */
	function btnBjChooseHandler() {
		flgRoom = 3;
		if (txtBjThing.getValue() != null && txtBjThing.getValue() != "") {
			// 是否重新选择
			Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG,
					MessageConstants.AR001_C_001, function(button) {
						if (button == 'yes') {
							winRoom.show();
						}
					});
		} else {
			winRoom.show();
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
			url : 'administration/uploadMeetFile.action',
			method : Constants.POST,
			params : {
				meetId : txtId.getValue()
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
					Ext.Msg.alert(MessageConstants.ERROR,
							MessageConstants.COM_E_022);
					return;
				}
				// 数据格式化错误
				if (o.msg == Constants.DATE_FAILURE) {
					Ext.Msg.alert(MessageConstants.ERROR,
							MessageConstants.COM_E_023);
					return;
				}
				// 文件不存在
				if (o.msg == Constants.FILE_NOT_EXIST) {
					Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
							MessageConstants.COM_E_024);
					return;
				}

				// 上传成功
				dsMatieria.load({
							params : {
								meetId : txtId.getValue()
							}
						})
				gridMatieria.getView().refresh();
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
										url : 'administration/deleteMeetFile.action',
										method : Constants.POST,
										params : {
											id : record.get('id'),
											meetFileUpdateTime : record
													.get('meetFileUpdateTime')
										},
										success : function(result, response) {
											var o = eval("("
													+ result.responseText + ")");
											if (o.msg == Constants.DATA_USING) {
												// 排他处理
												Ext.Msg
														.alert(
																MessageConstants.SYS_ERROR_MSG,
																MessageConstants.COM_I_002);
												return;
											}
											// 删除成功
											Ext.Msg
													.alert(
															MessageConstants.SYS_REMIND_MSG,
															MessageConstants.COM_I_005,
															function() {
																// 画面上删除一行
																dsMatieria
																		.remove(record);
																gridMatieria
																		.getView()
																		.refresh();
															});
										}
									})
						}
					})
		}
	}

	/**
	 * 新增明细处理
	 */
	function btnAddDetailHandler() {
		var record = new recordDetail({
					id : '',
					payName : '',
					payBudget : '',
					payReal : '',
					note : '',
					isNewRecord : true
				})

		// 原数据个数
		var count = dsDetail.getCount();
		if (count == null) {
			count = 0;
		}
		// 停止原来编辑
		gridDetail.stopEditing();
		// 插入新数据
		if (count == 0) {
			dsDetail.insert(0, record);
		} else {
			dsDetail.insert(count - 1, record);
		}
		gridDetail.getView().refresh();
		// 开始编辑新记录第一列
		if (count == 0) {
			gridDetail.startEditing(0, 1);
		} else {
			gridDetail.startEditing(count - 1, 1);
		}
		if (dsDetail.getCount() <= 1) {
			addLine();
		}
	}

	/**
	 * 删除明细
	 */
	function btnDeleteDetailHandler() {
		var record = gridDetail.selModel.getSelected();
		if (gridDetail.selModel.hasSelection()) {
			// 选中的合计行
			if (record.get('countType') == 'total') {
				return;
			}
			Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG,
					MessageConstants.COM_C_002, function(button) {
						if (button == 'yes') {
							// 如果选中一行则删除
							dsDetail.remove(record);
							gridDetail.getView().refresh();
							// 如果不是新增加的记录, 保存删除的流水号
							if (!record.get('isNewRecord')) {
								var deleteRecord = new Object();
								deleteRecord['id'] = record.get('id');
								deleteRecord['payName'] = record.get('payName');
								deleteRecord['payBudget'] = record
										.get('payBudget')
								deleteRecord['payReal'] = record.get('payReal');
								deleteRecord['note'] = record.get('note');
								deleteRecord['updateTime'] = record
										.get('updateTime');
								deleteDetailId.push((deleteRecord));
							}
							if (dsDetail.getCount() == 1) {
								dsDetail.removeAll();
							}
						}
					})
		} else {
			// 否则弹出提示信息
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
					MessageConstants.COM_I_001);
		}
	}

	/**
	 * 取消明细
	 */
	function btnCancelDetailHandler() {
		Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG,
				MessageConstants.COM_C_005, function(button) {
					if (button == 'yes') {
						originalDetailData = [];
						deleteDetailId = [];
						// 费用明细数据
						dsDetail.load({
									params : {
										meetId : txtId.getValue()
									}
								})
					}
				})
	}

	/**
	 * 提交按钮处理
	 */
	function commitHandler() {
		if (ckb1.getValue() == false && ckb2.getValue() == false
				&& ckb3.getValue() == false && ckb4.getValue() == false
				&& ckb5.getValue() == false) {
			// 都没有选中
			Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
					MessageConstants.AR001_E_001);
			return;
		} else {
			var str = "";
			if (ckb1.getValue() == true) {
				str += "欢迎书签 ";
				ckb1.setValue(false);
			}
			if (ckb2.getValue() == true) {
				str += "矿泉水 ";
				ckb2.setValue(false);
			}
			if (ckb3.getValue() == true) {
				str += "水果 ";
				ckb3.setValue(false);
			}
			if (ckb4.getValue() == true) {
				str += "鲜花 ";
				ckb4.setValue(false);
			}
			if (ckb5.getValue() == true) {
				str += "烟 ";
				ckb5.setValue(false);
			}
			if (str.lastIndexOf(" ") == str.length - 1) {
				str = str.substring(0, str.length - 1);
			}
			if (flgRoom == 1) {
				txtTfThing.setValue(str);
			}
			if (flgRoom == 2) {
				txtDjThing.setValue(str);
			}
			if (flgRoom == 3) {
				txtBjThing.setValue(str);
			}
		}
		winRoom.hide();
	}

	/**
	 * 备注双击处理
	 */
	function cellClickHandler() {
		var rec = gridDetail.getSelectionModel().getSelections();
		var record = gridDetail.getStore().getAt(row);
		winEnter.show();
		memoText.setValue(record.get("note"));
	}

	function dele(value) {
		if (value != "") {
			return "<span style='padding-left:20px'><a href='#'  onclick= 'btnDeleteAppendHandler();return false;'><img src='comm/ext/tool/dialog_close_btn.gif'></a></span>";
		} else {
			return "";
		}
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

	/**
	 * 费用渲染
	 */
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

	/**
	 * 查询结束时插入统计行
	 */
	function addLine() {
		// 统计行
		var record = new recordDetail({
					countType : "total"
				});
		// 原数据个数
		var count = dsDetail.getCount();
		if (count > 0) {
			dsDetail.insert(count, record);
			gridDetail.getView().refresh();
		}
	};

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
})