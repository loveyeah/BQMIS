Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
	
Ext.onReady(function() {
	Ext.QuickTips.init();
    var strMethod = "";
    var strallow = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	// ↓↓*******************车辆维修单位维护**************************************
	
    
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
     // toolbar
    var headTbar = new Ext.Toolbar({
        region : 'north',
        border:false,
        items:[btnAdd,btnUpdate,btnDelete]
    });
    // 数据集定义
    var recordMain = new Ext.data.Record.create([
			// 流水号
			{
		    name : 'id'
	        },
			// 单位编码
			{
				name : 'cpCode'
			},// 单位名称
			{
				name : 'cpName'
			},
			// 单位地址
			{
				name : 'cpAddress'
			},
			// 联系电话
			{
				name : 'conTel'
			},
			// 联系人
			{
				name : 'connman'
			},
			// 业务范围
			{
				name : 'bsnRanger'
			},// 检索码
			{
				name : 'retrieveCode'
			},
			// 修改时间
			{
				name : 'updateTime'
			}]);
	// 车辆维修单位维护grid的store
	var carRepairUnitStore = new Ext.data.JsonStore({
				url : 'administration/getCarRepairUnitQueryList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : recordMain
			});

	carRepairUnitStore.load({
				params : {
					start : 0,
					limit : Constants.PAGE_SIZE
				}
			});
	carRepairUnitStore.on("load", function() {
				if (carRepairUnitStore.getCount() <= 0) {
					Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
							MessageConstants.COM_I_003);
				}
			});
    var colms = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
						header : "行号",
						width : 35
					}),
			// 单位编码
			{
				header : "单位编码",
				sortable : true,
				width : 85,
				dataIndex : 'cpCode'
			},
			// 单位名称
			{
				header : "单位名称",
				sortable : true,
				width : 150,
				dataIndex : 'cpName'
			},
			// 联系人
			{
				header : "联系人",
				sortable : true,
				width : 100,
				dataIndex : 'connman'
			},
			// 联系电话
			{
				header : "联系电话",
				sortable : true,
				width : 150,
				dataIndex : 'conTel'
			},
			// 检索码
			{
				header : "检索码",
				sortable : true,
				width : 75,
				dataIndex : 'retrieveCode'
			}]);
	// 分页设置		
    var bbar = new Ext.PagingToolbar({
							pageSize : Constants.PAGE_SIZE,
							store : carRepairUnitStore,
							displayInfo : true,
							displayMsg : MessageConstants.DISPLAY_MSG,
							emptyMsg : MessageConstants.EMPTY_MSG
						});
	// 选择模式
	var sm = new Ext.grid.RowSelectionModel({
							singleSelect : true
						})	;			
	// grid
	var carRepairUnitGrid = new Ext.grid.GridPanel({				
				border : true,			
				sm : sm,					
				fitToFrame : true,
				store : carRepairUnitStore,
				cm : colms,
				tbar : headTbar,
				enableColumnMove : false,
				bbar : bbar
			});
	var layout = new Ext.Viewport({
                layout : 'fit',
                enableTabScroll : true,
                border : false,
                items : [carRepairUnitGrid]
            });
   carRepairUnitGrid.on("rowdblclick", updateRecord);
    // ↑↑**********************车辆维修单位维护主画面********************↑↑//
    
    // ↓↓*****弹出窗口*******↓↓//
   // 定义form中的单位名称
    var txtUnitName = new Ext.form.TextField({
                id : "unitName",
                fieldLabel : "单位名称<font color='red'>*</font>",
                isFormField : true,
                allowBlank : false,
                width : 128,
                maxLength : 25,
                anchor : '100%',
                name : 'adcCarWhentity.cpName'
            });
     // 定义form中的联系人
    var txtCommMan = new Ext.form.TextField({
                id : "commMan",
                fieldLabel : "联系人",
                isFormField : true,
                width : 128,
                maxLength : 5,
                anchor : '100%',
                name : 'adcCarWhentity.connman'
            });
    // 第一行
    var firstLine = new Ext.Panel({
        border : false,
        layout : "column",
        style : "padding-top:5px;padding-right:5px;padding-bottom:0px;margin-bottom:0px",
        anchor : '100%',
        items : [{
                    columnWidth : 0.5,
                    layout : "form",
                    border : false,
                    items : [txtUnitName]
                }, {
                    columnWidth : 0.5,
                    layout : "form",
                    border : false,
                    items : [txtCommMan]
                }]
    });
    // 定义form中的检索码
    var txtRetrieveCode = new Ext.form.TextField({
                id : "retrieveCode",
                fieldLabel : "检索码",
                isFormField : true,
                style :'IME_MODE:inactive',
                width : 128,
                onlyLetter : true,
                maxLength : 6,
                anchor : '100%',
                name : 'adcCarWhentity.retrieveCode',
                style :{
                    'ime-mode' : 'disabled'
                },
                initEvents : function(){
		        	Ext.form.TriggerField.prototype.initEvents.call(this);
		        	var keyPress = function(e){
			            var k = e.getKey();
			            if(!Ext.isIE && (e.isSpecialKey() || k == e.BACKSPACE || k == e.DELETE)){
			                return;
			            }
			            var c = e.getCharCode();
			            var allowed = strallow;
			            if(allowed.indexOf(String.fromCharCode(c)) === -1){
			                e.stopEvent();
			            }
			        };
			        this.el.on("keypress", keyPress, this);
		        }
            });
     // 定义form中的联系电话
    var txtCommTel = new Ext.form.TextField({
		id : "commTel",
		fieldLabel : "联系电话",
		width : 128,
		maxLength : 14,
		anchor : '100%',
		name : 'adcCarWhentity.conTel',
		style : {
			'ime-mode' : 'disabled'
		},
		initEvents : function() {
			Ext.form.TriggerField.prototype.initEvents.call(this);
			var keyPress = function(e) {
				var k = e.getKey();
				if (!Ext.isIE
						&& (e.isSpecialKey() || k == e.BACKSPACE || k == e.DELETE)) {
					return;
				}
				var c = e.getCharCode();
				var allowed = "0123456789";
				if (allowed.indexOf(String.fromCharCode(c)) === -1) {
					e.stopEvent();
				}
			};
			// 禁用Ctrl + V
			var keydown = function(e) {
				var k = e.getKey();
				if (!Ext.isIE
						&& (e.isSpecialKey() || k == e.BACKSPACE || k == e.DELETE)) {
					return;
				}
				var c = String.fromCharCode(e.getCharCode());

				if (e.ctrlKey && /^v$/i.test(c)) {
					e.stopEvent();
				}
			}
			this.el.on("keydown", keydown, this);
			this.el.on("keypress", keyPress, this);
		}
	});
     // 第二行
    var secondLine = new Ext.Panel({
        border : false,
        layout : "column",
        style : "padding-top:5px;padding-right:5px;padding-bottom:0px;margin-bottom:0px",
        anchor : '100%',
        items : [{
                    columnWidth : 0.5,
                    layout : "form",
                    border : false,
                    items : [txtRetrieveCode]
                },
                {
                    columnWidth : 0.5,
                    layout : "form",
                    border : false,
                    items : [txtCommTel]
                }]
    });
    // 定义form中的单位地址
    var txtUnitAddress = new Ext.form.TextField({
                id : "unitAddress",
                fieldLabel : "单位地址",
                isFormField : true,
                maxLength : 50,
                anchor : '100%',
                name : 'adcCarWhentity.cpAddress'
            });
     // 第三行
    var thirdLine = new Ext.Panel({
        border : false,
        layout : "column",
        style : "padding-top:5px;padding-right:5px;padding-bottom:0px;margin-bottom:0px",
        anchor : '100%',
        items : [{
                    columnWidth : 1,
                    layout : "form",
                    border : false,
                    items : [txtUnitAddress]
                }]
    });
	// 定义form中的业务范围
	var txaBsnRanger = new Ext.form.TextArea({
				id : "bsnRanger",
				fieldLabel : '业务范围',
				height:25,
				maxLength:500,
                height : 137,
				readOnly : false,
				anchor : "99%",
				name : 'adcCarWhentity.bsnRanger'
			});
    // 第四行
    var fourLine = new Ext.Panel({
        border : false,
        layout : "column",
        style : "padding-top:5px;padding-right:5px;padding-bottom:0px;margin-bottom:0px",
        anchor : '100%',
        items : [{
                    columnWidth : 1,
                    layout : "form",
                    border : false,
                    items : [txaBsnRanger]
                }]
    });
	var uppanel = new Ext.Panel({
                labelAlign : 'right',
                autoHeight : true,
                border : false,
                frame : true,
                items : [firstLine,secondLine,thirdLine,fourLine]
            });	
    // 车辆维修单位维护form
	var headForm = new Ext.FormPanel({
				border : false,
				labelAlign : 'right',
				style : {
					'border' : 0
				},
                labelWidth : 90,
				autoHeight : true,
				items : [uppanel]
				});	
	 // 弹出画面
	 var win = new Ext.Window({
		height : 320,
		width : 500,
		layout : 'fit',
		modal:true,
		resizable : false,
		autoScroll:true,
		closeAction : 'hide',
		items : [headForm],
		buttonAlign : "center",
		title : '',
		buttons : [ {
			text : Constants.BTN_SAVE,
			iconCls : Constants.CLS_SAVE,
			handler : confirmRecord
		},{
			text : Constants.BTN_CANCEL,
			iconCls : Constants.CLS_CANCEL,
			handler : hideChildWin
		}]
	});	
    // ↑↑********弹出窗口*********↑↑//
	// ↓↓*******************************处理****************************************
	/**
     * 增加函数
     */
    function addRecord() {
        strMethod = "add";
        headForm.getForm().reset();
        win.setTitle("新增车辆维护单位");
        win.show();
    }
	 function updateRecord() {
        var rec = carRepairUnitGrid.getSelectionModel().getSelected();
        if (!rec) {
            Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG, MessageConstants.COM_I_001);
            return false;
        } else {
            strMethod = "update";
            headForm.getForm().reset();
            win.setTitle("修改车辆维护单位");
            win.show();
            txtUnitName.setValue(rec.data.cpName);
            txtCommTel.setValue(rec.data.conTel);
            txtRetrieveCode.setValue(rec.data.retrieveCode);
            txtCommMan.setValue(rec.data.connman);
            txtUnitAddress.setValue(rec.data.cpAddress);
            txaBsnRanger.setValue(rec.data.bsnRanger);
            
            
        }
    }
    /**
     * 删除函数
     */
     function deleteRecord() {
		var rec = carRepairUnitGrid.getSelectionModel().getSelected();
		
		if (!rec) {
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
					MessageConstants.COM_I_001);
		} else {
			 Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG, MessageConstants.COM_C_002,
                    function(buttonobj) {
                        if (buttonobj == 'yes') {
                            // 刪除
                            Ext.Ajax.request({
                                method : Constants.POST,
                                url : 'administration/deleteCarRepairUnit.action',
                                params : {
                                    strRec : Ext.util.JSON.encode(rec.data)
                                },
                                success : function(result, request) {
                                    var o = eval("(" + result.responseText + ")");
                                    // 排他
                                    if (o.msg == Constants.DATA_USING) {
                                        Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
                                                MessageConstants.COM_I_002);
                                    } else if(o.msg == '1'){
                                    	// 删除正常结束
                                        Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
                                                MessageConstants.COM_I_005,
														function() {
															carRepairUnitStore
																	.removeAll();
															carRepairUnitStore
																	.commitChanges();
															carRepairUnitStore
																	.load({
																		params : {
																			start : 0,
																			limit : Constants.PAGE_SIZE
																		}
																	});
														}
                                                );
                                       
                                        carRepairUnitGrid.getView().refresh();
                                    } else if (o.msg == Constants.SQL_FAILURE){
                                    	// 删除异常
                                        Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
                                            MessageConstants.COM_E_014);
                                    }
                                }
                            });
                        }
                    });
        } 
	}
    /**
	 * 点击取消按钮
	 */
    function hideChildWin() {
        Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG, MessageConstants.COM_C_005, function(
                        buttonobj) {
                    if (buttonobj == 'yes') {
                        headForm.getForm().reset();
                        win.hide();
                    }
                })

    }
	 /**
     * 保存button压下的操作
     */
    function confirmRecord() {
    	if(checkform()){
    	  Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG, MessageConstants.COM_C_001,
                function(buttonobj) {
                    if (buttonobj == 'yes') {
                    	if (strMethod == "add") {
                    		// 插入
                    		headForm.getForm().submit({
                    			 method : Constants.POST,
                    			 url : 'administration/addCarRepairUnit.action',
                    			 success : function (form, action){
                    			 	var result = eval('('
                                                + action.response.responseText
                                                + ')');
                                    if (result.msg == "1") {
											Ext.Msg.alert(
															MessageConstants.SYS_REMIND_MSG,
															MessageConstants.COM_I_004,
															function() {
																carRepairUnitStore
																		.removeAll();
																carRepairUnitStore
																		.commitChanges();
																carRepairUnitStore
																		.load({
																			params : {
																				start : 0,
																				limit : Constants.PAGE_SIZE
																			}
																		});
																
																headForm.getForm().reset();
																win.hide();
															});
										} else if(result.msg == Constants.SQL_FAILURE){
                                    	Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
													MessageConstants.COM_E_014);
                                    }
                    			 }
                    		})
                    	}
                   
                    if(strMethod == "update"){
                    	var rec = carRepairUnitGrid.getSelectionModel().getSelected();
                        // 更新
                    	headForm.getForm().submit({
									method : Constants.POST,
									url : 'administration/updateCarRepairUnit.action',
									params : {
										strRec : Ext.util.JSON.encode(rec.data)
									},
									success : function(form, action) {
										var result = eval('('
												+ action.response.responseText
												+ ')');
										if (result.msg == "1") {
											Ext.Msg.alert(
															MessageConstants.SYS_REMIND_MSG,
															MessageConstants.COM_I_004,
															function() {
																carRepairUnitStore
																		.removeAll();
																carRepairUnitStore
																		.commitChanges();
																carRepairUnitStore
																		.load({
																			params : {
																				start : 0,
																				limit : Constants.PAGE_SIZE
																			}
																		});
																headForm.getForm().reset();
																win.hide();
															});

										} else if (result.msg == Constants.SQL_FAILURE) {
											Ext.Msg.alert(Constants.ERROR,
													MessageConstants.COM_E_014);
										}
									}
								})
                    } }
                })
    	}
    }
    /**
	 * 保存时的check
	 */
     function checkform() {
		var strUnitName = txtUnitName.getValue();
		var strTelNum = txtCommTel.getValue();
		// 单位名称必须入力check
		if (null == strUnitName || strUnitName == "") {
			Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, String.format(
							MessageConstants.COM_E_002, '单位名称'));
			return false;
		}
		var lngTelNum = 0;
		if (strTelNum != null || strTelNum != "") {
			lngTelNum = strTelNum.toString().length;
		}
		if (lngTelNum != 0) {
			if (lngTelNum == 7 || lngTelNum == 8 || lngTelNum == 11
					|| lngTelNum == 12 || lngTelNum == 14 || lngTelNum == 13) {
				return true;
			} else {
				Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, String.format(
								MessageConstants.COM_E_021, '联系电话'));
				return false;
			}
		}
		return true;
	}
})
