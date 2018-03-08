Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
    Ext.QuickTips.init();
  
    
    var strMethod = "";
    // ↓↓********** 主画面*******↓↓//
	// 新增按钮
    var btnAdd = new Ext.Toolbar.Button({
        text : Constants.BTN_ADD,
        iconCls : Constants.CLS_ADD,
        handler : addRecord
    });
    // 修改按钮
    var btnUpdate = new Ext.Toolbar.Button({
        text : Constants.BTN_UPDATE,
        iconCls : Constants.CLS_UPDATE,
        handler : updateRecord
    });
    // 删除按钮
    var btnDelete = new Ext.Toolbar.Button({
        text : Constants.BTN_DELETE,
        iconCls : Constants.CLS_DELETE,
        handler : deleteRecord
    });
     // 上报按钮
    var btnReport = new Ext.Toolbar.Button({
        text : Constants.BTN_REPOET,
        iconCls : Constants.CLS_REPOET,
        handler : reportRecord
    });
    // toolbar
    var headTbar = new Ext.Toolbar({
        region : 'north',
        border:false,
        items:[btnAdd,btnUpdate,btnDelete,btnReport]
    });
    // 数据集定义
    var recordMain = new Ext.data.Record.create([{
        // 序号
        name : 'id'
    },{
        // 填表日期
        name : 'logDate'
    },{
        // 接待申请单号
        name : 'applyId'
    },{
        // 接待日期
        name : 'meetDate'
    },{
        // 接待说明
        name : 'meetNote'
    },{
    	// 就餐人数
        name : 'repastNum'
    },{
        // 就餐标准
        name : 'repastBz'
    },{
        // 就餐安排
        name : 'repastPlan'
    },{
        // 住宿人数
        name : 'roomNum'
    },{
        // 住宿标准
        name : 'roomBz'
    },{
        // 住宿安排
        name : 'roomPlan'
    },{
        // 标准金额
        name : 'payoutBz'
    },{
        // 实际金额
        name : 'payout'    
    },{
    	// 差额
        name : 'balance'
    },{
        // 其他
        name : 'other'
    },{
        // 修改时间
        name : 'updateTime'
    },{
        // 部门名称
        name : 'depName'
    },{
        // 名称
        name : 'name'
    }]);
    var storeMain = new Ext.data.JsonStore({
            url : 'administration/findRecepAppData.action',
            root : 'list',
            totalProperty : 'totalCount',
            fields : recordMain
        });
        storeMain.load({
				params : {
					start : 0,
					limit : Constants.PAGE_SIZE
				}
			});
    // Grid用columnModel
    var colus = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
                                header : "行号",
                                width : 35
                            }),
                    // 申请人
                    {
                        header : "申请人",
                        width : 100,
                        sortable : true,
                        dataIndex : 'name'
                    },
                    // 申请部门
                    {
                        header : "申请部门",
                        dataIndex : "depName",
                        sortable : true,
                        width : 130
                    },
                    // 填表日期
                    {
                        header : "填表日期",
                        width : 150,
                        sortable : true,
                        dataIndex : 'logDate'
                    },
                    // 接待日期
                    {
                        header : "接待日期",
                        width : 150,
                        sortable : true,
                        dataIndex : 'meetDate'
                    }]);
    // Grid用PagingToolbar
    var bbar = new Ext.PagingToolbar({
                            pageSize : Constants.PAGE_SIZE,
                            store : storeMain,
                            displayInfo : true,
                            displayMsg : MessageConstants.DISPLAY_MSG,
                            emptyMsg : MessageConstants.EMPTY_MSG
                        });
    // 运行执行的Grid主体
    var gridMain = new Ext.grid.GridPanel({
            region : "center",
            layout : 'fit',
            border : false,
            enableColumnMove : false,
            // 单选
            sm : new Ext.grid.RowSelectionModel({
                        singleSelect : true
                    }),
            store : storeMain,
            tbar : headTbar,
            bbar : bbar,
            cm : colus
        });
     // 注册双击事件
    gridMain.on("rowdblclick", updateRecord);
    // 显示
    var layout = new Ext.Viewport({
                layout : 'fit',
                enableTabScroll : true,
                border : false,
                items : [gridMain]
            });
    // ↑↑********** 主画面*******↑↑//
    // ↓↓*****弹出窗口*******↓↓//
    // 定义form中的工作名称
    var txtApplyId = new Ext.form.TextField({
                id : "applyId",
                fieldLabel : "审批单号",
                isFormField : true,
                width : 128,
                readOnly : true,
                maxLength : 25,
                anchor : '100%',
                name : 'entity.applyId'
            });
    // 第一行
    var firstLine = new Ext.Panel({
        border : false,
        layout : "column",
        width : 430,
        style : "padding-top:5px;padding-right:5px;padding-bottom:0px;margin-bottom:0px",
        items : [{
                    columnWidth : 0.5,
                    layout : "form",
                    border : false,
                    labelWidth : 82,
                    items : [txtApplyId]
                }, {
                    columnWidth : 0.5,
                    layout : "form",
                    border : false
                }]
    });
    // 定义form中的申请人
    var txtApplyMan = new Ext.form.TextField({
                id : "applyMan",
                fieldLabel : "申请人",
                isFormField : true,
                width : 128,
                readOnly : true,
                maxLength : 25,
                anchor : '100%'
            });
    // 定义form中的申请部门
    var txtApplyDep = new Ext.form.TextField({
                id : "applyDep",
                fieldLabel : "申请部门",
                isFormField : true,
                width : 128,
                readOnly : true,
                maxLength : 25,
                anchor : '100%'
            });
    // 第一行
    var firstLineForm1 = new Ext.Panel({
        border : false,
        layout : "column",
        width : 430,
        style : "padding-top:5px;padding-right:5px;padding-bottom:0px;margin-bottom:0px",
        items : [{
                    columnWidth : 0.5,
                    layout : "form",
                    border : false,
                    items : [txtApplyMan]
                }, {
                    columnWidth : 0.5,
                    layout : "form",
                    border : false,
                    items : [txtApplyDep]
                }]
    });
    // 定义form中的填表日期
    var txtLogDate = new Ext.form.TextField({
                id : "logDate",
                fieldLabel : "填表日期",
                isFormField : true,
                readOnly : true,
                width : 128,
                maxLength : 25,
                anchor : '100%',
                name : 'entity.logDate'
            });
    // 定义form中的接待日期
    var txtMeetDate = new Ext.form.TextField({
                id : "meetDate",
                fieldLabel : "接待日期<font color='red'>*</font>",
                isFormField : true,
                readOnly : true,
                style : 'cursor:pointer',
                width : 128,
                name : 'entity.meetDate',
                anchor : '100%',
                listeners : {
                    focus : function() {
                        WdatePicker({
                                    dateFmt : 'yyyy-MM-dd',
                                    isShowClear: false
                                });
                    }
                }
            });
    // 第二行
    var secondLineForm1 = new Ext.Panel({
        border : false,
        layout : "column",
        width : 430,
        style : "padding-top:5px;padding-right:5px;padding-bottom:0px;margin-bottom:0px",
        items : [{
                    columnWidth : 0.5,
                    layout : "form",
                    border : false,
                    items : [txtLogDate]
                },
                {
                    columnWidth : 0.5,
                    layout : "form",
                    border : false,
                    items : [txtMeetDate]
                }]
    });
    // 定义form中隐藏的就餐人数
    var hideRepastNum = new Ext.form.Hidden({
				id : 'hideRepastNum',
				name : 'entity.repastNum'
			});
     // 定义form中的就餐人数
    var txtRepastNum = new Ext.form.MoneyField({
                id : "repastNum",
                fieldLabel : "就餐人数",
                isFormField : true,
                style:'text-align:right',
                width : 128,
                allowDecimals : false,
                allowNegative : false,
                maxLength : 4,
                anchor : '100%',
                appendChar : '人'
                
            });
    // 定义form中隐藏的住宿人数
    var hideRoomNum = new Ext.form.Hidden({
				id : 'hideRoomNum',
                name : 'entity.roomNum'
			});
    // 定义form中的住宿人数
    var txtRoomNum = new Ext.form.MoneyField({
                id : "roomNum",
                fieldLabel : "住宿人数",
                width : 128,
                style:'text-align:right',
                allowDecimals : false,
                maxLength : 4,
                allowNegative : false,
                anchor : '100%',
                appendChar : '人'
            });
    // 第三行
    var thirdLineForm1 = new Ext.Panel({
        border : false,
        layout : "column",
        width : 430,
        style : "padding-top:5px;padding-right:5px;padding-bottom:0px;margin-bottom:0px",
        items : [{
                    columnWidth : 0.5,
                    layout : "form",
                    border : false,
                    items : [hideRepastNum,txtRepastNum]
                }, {
                    columnWidth : 0.5,
                    layout : "form",
                    border : false,
                    items : [hideRoomNum,txtRoomNum]
                }]
    });
     // 定义form中隐藏的就餐标准
    var hideRepastBz = new Ext.form.Hidden({
				id : 'hideRepastBz',
                name : 'entity.repastBz'
			});
    // 定义form中的就餐标准
    var txtRepastBz = new Ext.form.MoneyField({
                id : "repastBz",
                fieldLabel : "就餐标准",
                isFormField : true,
                width : 128,
                style:'text-align:right',
                maxLength : 8,
                decimalPrecision : 2,
                padding : 2,
                allowNegative : false,
                anchor : '100%',
                appendChar : '元/人'
            });
     // 定义form中隐藏的住宿标准
    var hideRoomBz = new Ext.form.Hidden({
				id : 'hideRoomBz',
                name : 'entity.roomBz'
			});
    // 定义form中的住宿标准
    var txtRoomBz = new Ext.form.MoneyField({
                id : "roomBz",
                fieldLabel : "住宿标准",
                isFormField : true,
                width : 128,
                style:'text-align:right',
                maxLength : 8,
                decimalPrecision : 2,
                padding : 2,
                allowNegative : false,
                anchor : '100%',
                appendChar : '元/人'
            });
    // 第四行
    var fourLineForm1 = new Ext.Panel({
        border : false,
        layout : "column",
        width : 430,
        style : "padding-top:5px;padding-right:5px;padding-bottom:0px;margin-bottom:0px",
        items : [{
                    columnWidth : 0.5,
                    layout : "form",
                    border : false,
                    items : [hideRepastBz,txtRepastBz]
                }, {
                    columnWidth : 0.5,
                    layout : "form",
                    border : false,
                    items : [hideRoomBz,txtRoomBz]
                }]
    });
    // 定义form中的就餐安排
    var txtRepastPlan = new Ext.form.TextArea({
                id : "repastPlan",
                fieldLabel : "就餐安排",
                maxLength : 20,
                height : 36,
                anchor : '99%',
                name : 'entity.repastPlan'
            });
    // 定义form中的住宿安排
    var txtRoomPlan = new Ext.form.TextArea({
                id : "roomPlan",
                fieldLabel : "住宿安排",
                maxLength : 20,
                height : 36,
                anchor : '99%',
                name : 'entity.roomPlan'
            });
    // 第五行
    var fiveLineForm1 = new Ext.Panel({
        border : false,
        layout : "column",
        width : 430,
        style : "padding-top:5px;padding-right:5px;padding-bottom:0px;margin-bottom:0px",
        items : [{
                    columnWidth : 0.5,
                    layout : "form",
                    border : false,
                    items : [txtRepastPlan]
                },{
                    columnWidth : 0.5,
                    layout : "form",
                    border : false,
                    items : [txtRoomPlan]
                }]
    });
     // 定义form中的具体接待说明
    var txtMeetNote = new Ext.form.TextArea({
                id : "meetNote",
                fieldLabel : "接待说明",
                maxLength : 30,
                height : 36,
                anchor : '99%',
                name : 'entity.meetNote'
            });
    // 第六行
    var sixLineForm1 = new Ext.Panel({
        border : false,
        layout : "column",
        width : 430,
        style : "padding-top:5px;padding-right:5px;padding-bottom:0px;margin-bottom:0px",
        items : [{
                    columnWidth : 1,
                    layout : "form",
                    border : false,
                    items : [txtMeetNote]
                }]
    });
     // 定义form中的其他
    var txtOther = new Ext.form.TextArea({
                id : "other",
                fieldLabel : "其他",
                maxLength : 100,
                height : 36,
                anchor : '99%',
                name : 'entity.other'
            });
    // 第七行
    var servenLineForm1 = new Ext.Panel({
        border : false,
        layout : "column",
        style : "padding-top:5px;padding-right:5px;padding-bottom:0px;margin-bottom:0px",
        width : 430,
        items : [{
                    columnWidth : 1,
                    layout : "form",
                    border : false,
                    items : [txtOther]
                }]
    });
    // 接待申请field
    var fldSet = new Ext.form.FieldSet({
        title : '接待申请',
        id : 'fieldSet',
        items : [firstLineForm1, secondLineForm1, thirdLineForm1,
						 sixLineForm1]
    });
    // 定义form中隐藏的标准支出
    var hidePayoutBz = new Ext.form.Hidden({
				id : 'hidePayoutBz',
                name : 'entity.payoutBz'
			});
    // 定义form中的标准支出
    var txtPayoutBz = new Ext.form.MoneyField({
                id : "payoutBz",
                fieldLabel : "标准支出",
                isFormField : true,
                width : 180,
                style:'text-align:right',
                decimalPrecision : 2,
                padding : 2,
                maxLength : 15,
                appendChar : '元'
            });
    // 定义form中隐藏的实际支出
    var hidePayout = new Ext.form.Hidden({
				id : 'hidePayout',
                name : 'entity.payout'
			});
    // 定义form中的实际支出
    var txtPayout = new Ext.form.MoneyField({
                id : "payout",
                fieldLabel : "实际支出",
                isFormField : true,
                decimalPrecision : 2,
                padding : 2,
                readOnly : true,
                style:'text-align:right',
                width : 180,
                maxLength : 15,
                appendChar : '元'
            });
    // 定义form中隐藏的差额
    var hideBalance = new Ext.form.Hidden({
				id : 'hideBalance',
                name : 'entity.balance'
			});
     // 定义form中的差额
    var txtBalance = new Ext.form.MoneyField({
                id : "balance",
                fieldLabel : "差额",
                decimalPrecision : 2,
                padding : 2,
                isFormField : true,
                readOnly : true,
                style:'text-align:right',
                width : 180,
                maxLength : 15,
                appendChar : '元'
            });
    // 接待申请field
    var costSet = new Ext.form.FieldSet({
        title : '费用核算',
        id : 'fieldSet',
        items : [hidePayoutBz,txtPayoutBz,hidePayout,txtPayout,hideBalance,txtBalance]
    });
    var panel = new Ext.Panel({
    items :[costSet]
    });
    // 第二个 FieldSet
	var fldSetSecond = new Ext.form.FieldSet({
				layout : 'form',
				border : true,
				autoHeight : true,
				items : [fourLineForm1, fiveLineForm1, servenLineForm1]
			})

     // 定义弹出窗体中的form
    var uppanel = new Ext.form.FormPanel({
				id : "form",
				labelAlign : 'right',
				autoHeight : true,
				autoScroll :false,
				labelWidth : 70,
                width : 468,
				frame : true,
				items : [firstLine,fldSet,fldSetSecond,panel]
			});
     // 定义弹出窗体
    var win = new Ext.Window({
                width : 500,
                height : 320,
                title : "",
                autoScroll :true,
                buttonAlign : "center",
                items : [uppanel],
                layout : 'form',
                closeAction : 'hide',
                modal : true,
                resizable : false,
                buttons : [{
                            text : Constants.BTN_SAVE,
                            iconCls : Constants.CLS_SAVE,
                            handler : confirmRecord

                        }, {
                            text : Constants.BTN_CANCEL,
                            iconCls : Constants.CLS_CANCEL,
                            handler : hideChildWin
                        }]
            });
     // ↑↑********弹出窗口*********↑↑//
    /**
     * 增加函数
     */
    function addRecord() {
		strMethod = "add";
		win.setTitle("新增接待申请");
		win.show();
		uppanel.getForm().reset();
		receive();
		getCurrentDate();
		txtPayoutBz.setValue('0');
        uppanel.focus();
	}
   /**
	 * 更新函数
	 */
     function updateRecord() {
      var rec = gridMain.getSelectionModel().getSelected();
        if (!rec) {
            Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG, MessageConstants.COM_I_001);
            return false;
        } else {
        	strMethod = "update";
            uppanel.getForm().reset();
            win.setTitle("修改接待申请");
            win.show();
            uppanel.focus();
            txtApplyId.setValue(rec.data.applyId);
            txtApplyMan.setValue(rec.data.name);
            txtApplyDep.setValue(rec.data.depName);
            txtLogDate.setValue(rec.data.logDate);
            txtMeetDate.setValue(rec.data.meetDate);
            txtRepastNum.setValue(rec.data.repastNum);
            txtRoomNum.setValue(rec.data.roomNum);
            txtRepastBz.setValue(rec.data.repastBz);
            txtRoomBz.setValue(rec.data.roomBz);
            txtRepastPlan.setValue(rec.data.repastPlan);
            txtRoomPlan.setValue(rec.data.roomPlan);
            txtMeetNote.setValue(rec.data.meetNote);
            txtOther.setValue(rec.data.other);
            txtPayoutBz.setValue(rec.data.payoutBz);
            txtPayout.setValue(rec.data.payout);
            txtBalance.setValue(rec.data.balance);
           
        }
     }
     txtPayoutBz.on('change',payoutBzChange);
     function payoutBzChange() {
		if (txtPayout.getValue() == null || txtPayout.getValue() == "") {
			txtBalance.setValue(txtPayoutBz.getValue());
		} else {
			txtBalance.setValue(txtPayoutBz.getValue() - txtPayout.getValue());
		}
	}
    /**
     * 删除button按下
     */
    function deleteRecord()
    {
    	rec = gridMain.getSelectionModel().getSelected();
    	if (rec) {
			// 弹出窗口
			Ext.Msg.confirm(Constants.NOTICE_CONFIRM,
					MessageConstants.COM_C_002, function(buttonobj) {
						if (buttonobj == "yes") {
							Ext.Ajax.request({
								method : Constants.POST,
								url : 'administration/deleteReceptionApply.action',
								params : {
									strRec : Ext.util.JSON.encode(rec.data)
								},
								success : function(result, request){
									var o = eval("(" + result.responseText + ")");
									// 排他
                                    if (o.msg == Constants.DATA_USING) {
                                        Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
                                                MessageConstants.COM_I_002);
                                    }else if(o.msg == '1'){
                                    	// 删除正常结束
                                        Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
                                                MessageConstants.COM_I_005,
														function() {
															storeMain.removeAll();
															storeMain.load({
																params : {
																	start : 0,
																	limit : Constants.PAGE_SIZE
																}
															});
															gridMain.getView().refresh();
														});
                                    }else if (o.msg == Constants.SQL_FAILURE){
                                    	// 删除异常
                                        Ext.Msg.alert(Constants.ERROR,
                                            MessageConstants.COM_E_014);
                                    }else if (o.msg == Constants.DATE_FAILURE){
                                        Ext.Msg.alert(Constants.ERROR,
                                            MessageConstants.COM_E_023);
                                    }
								}
							})
						}
					})
		} else {
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
					MessageConstants.COM_I_001);
		}
    }
     /**
     * 上报button按下
     */
    function reportRecord()
    {
    	rec = gridMain.getSelectionModel().getSelected();
    	if (rec) {
			// 弹出窗口
			Ext.Msg.confirm(Constants.NOTICE_CONFIRM,
					MessageConstants.COM_C_006, function(buttonobj) {
						if (buttonobj == "yes") {
							Ext.Ajax.request({
								method : Constants.POST,
								url : 'administration/reportReceptionApply.action',
								params : {
									strRec : Ext.util.JSON.encode(rec.data)
								},
								success : function(result, request){
									var o = eval("(" + result.responseText + ")");
									// 排他
                                    if (o.msg == Constants.DATA_USING) {
                                        Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
                                                MessageConstants.COM_I_002);
                                    }else if(o.msg == '1'){
                                    	// 上报正常结束
                                        Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
                                                MessageConstants.COM_I_007,
														function() {
															storeMain.removeAll();
															storeMain.load({
																params : {
																	start : 0,
																	limit : Constants.PAGE_SIZE
																}
															});
															gridMain.getView().refresh();
														});
                                    }else if (o.msg == Constants.SQL_FAILURE){
                                    	// 上报异常
                                        Ext.Msg.alert(Constants.ERROR,
                                            MessageConstants.COM_E_014);
                                    }else if (o.msg == Constants.DATE_FAILURE){
                                        Ext.Msg.alert(Constants.ERROR,
                                            MessageConstants.COM_E_023);
                                    }
								}
							})
						}
					})
		} else {
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
					MessageConstants.COM_I_001);
		}
    }
    /**
     * 点击保存按钮
     */
    function confirmRecord() {
    hideBalance.setValue(txtBalance.getValue());
    hidePayout.setValue(txtPayout.getValue());
    hidePayoutBz.setValue(txtPayoutBz.getValue());
    hideRepastBz.setValue(txtRepastBz.getValue());
    hideRepastNum.setValue(txtRepastNum.getValue());
    hideRoomBz.setValue(txtRoomBz.getValue());
    hideRoomNum.setValue(txtRoomNum.getValue());
    	var rec = gridMain.getSelectionModel().getSelected();
    	 Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG, MessageConstants.COM_C_001,
                function(buttonobj) {
                    if (buttonobj == 'yes') {
                        var myurl = "";
                        if (strMethod == "add") {
                                myurl = 'administration/addReceptionApply.action';
                                uppanel.getForm().submit({
								method : Constants.POST,
								url : myurl,
								params : {
									
								},
								success : function(form, action) {
									var result = eval('('
											+ action.response.responseText
											+ ')');
									// 显示成功信息
									if (result.msg == "1") {
										Ext.Msg.alert(
														MessageConstants.SYS_REMIND_MSG,
														MessageConstants.COM_I_004,
														function() {
															storeMain.load(
																	{
																		params : {
																			start : 0,
																			limit : Constants.PAGE_SIZE
																		}
																	});
															gridMain.getView()
																	.refresh();
														uppanel.getForm().reset();
										                win.hide();
														});
										

									} else if (result.msg == Constants.SQL_FAILURE) {
										Ext.Msg.alert(
												MessageConstants.SYS_ERROR_MSG,
												MessageConstants.COM_E_014);
									} else if (result.msg == Constants.DATE_FAILURE) {
										Ext.Msg.alert(
												MessageConstants.SYS_ERROR_MSG,
												MessageConstants.COM_E_023);
									}
								}
							});
                                return;
                        }
                       if (strMethod == "update") {
                                myurl = 'administration/updateReceptionApply.action';
                                uppanel.getForm().submit({
								method : Constants.POST,
								url : myurl,
								params : {
									strRec : Ext.util.JSON.encode(rec.data)
								},
								success : function(form, action) {
									var result = eval('('
											+ action.response.responseText
											+ ')');
									// 显示成功信息
									if (result.msg == "1") {
										Ext.Msg.alert(
														MessageConstants.SYS_REMIND_MSG,
														MessageConstants.COM_I_004,
														function() {
															storeMain.load(
																	{
																		params : {
																			start : 0,
																			limit : Constants.PAGE_SIZE
																		}
																	});
															gridMain.getView()
																	.refresh();
															uppanel.getForm().reset();
														    win.hide();
														});

									} // 排他
                                    else if (result.msg == Constants.DATA_USING) {
                                        Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
                                                MessageConstants.COM_I_002);
                                    }else if (result.msg == Constants.SQL_FAILURE) {
										Ext.Msg.alert(
												MessageConstants.SYS_ERROR_MSG,
												MessageConstants.COM_E_014);
									} else if (result.msg == Constants.DATA_FAILURE) {
										Ext.Msg.alert(
												MessageConstants.SYS_ERROR_MSG,
												MessageConstants.COM_E_023);
									}
								}
							});
                                return;
                        } 
                    }
                })
    }
     /**
		 * 点击取消按钮
		 */
    function hideChildWin() {
        Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG, MessageConstants.COM_C_005, function(
                        buttonobj) {
                    if (buttonobj == 'yes') {
//                        uppanel.getForm().reset();
                        win.hide();
                    }
                })

    }
    /**
     * 获取当前日期并赋值给日期
     */
    function getCurrentDate() {
        var curr_time = new Date();
        txtLogDate.setValue(curr_time.format("Y-m-d"));
        txtMeetDate.setValue(curr_time.format("Y-m-d"));
    }
      /** 获取工名和部门*/
    function receive() {
        Ext.Ajax.request({
                    url : 'administration/getSession.action',
                    method : 'POST',
                    success : function(result, request) {
                        var o = eval('(' + result.responseText + ')');
                        txtApplyMan.setValue(o.workerName.toString());
                        if (o.deptName != null) {
							txtApplyDep.setValue(o.deptName.toString());
						}
                    }
                })
    }
})