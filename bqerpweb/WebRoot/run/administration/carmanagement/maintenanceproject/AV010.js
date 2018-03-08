Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	Ext.QuickTips.init();
	// 费用类别显示store
    var payCodeShowStore = new Ext.data.JsonStore({
        root : 'list',
        url : "administration/getPaySortShowList.action",
        fields : ['payCode', 'payName']
    })
    payCodeShowStore.load();
    // 查询按钮
    var btnAdd = new Ext.Button({
        text : Constants.BTN_ADD,
        iconCls : Constants.CLS_ADD,
        handler : addRecord
    });	
    // 修改按钮
    var btnModify = new Ext.Button({
        text : Constants.BTN_UPDATE,
        iconCls : Constants.CLS_UPDATE,
        handler : modifyRecord
    });	
    // 删除按钮
    var btnDelete = new Ext.Button({
        text : Constants.BTN_DELETE,
        iconCls : Constants.CLS_DELETE,
        handler : delRecord
    });	
    	
	// 表示是增加还是修改
    var hiddenFlag = new Ext.form.Hidden({
        id : "flags",
        value : "0"
    });
    
    // 数据源--------------------------------
	var MyRecord = Ext.data.Record.create([{
				/**序号*/
				name : 'id'
			},{	
				/** 费用类别 */
				name : 'payCode'
			}, {
				/** 项目代码 */	
				name : 'proCode'
			}, {
				/** 项目名称 */
				name : 'proName'
			},{
				/** 有无清单 */
				name:'haveLise'
				}]);
	// 定义获取数据源
	var dataProxy = new Ext.data.HttpProxy({
		url : 'administration/getMaintenanceProjectList.action'
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
		listeners : {
			loadexception : function(ds, records, o) {
			var o = eval("(" + o.responseText + ")");
			var succ = o.msg;
			if (succ == Constants.SQL_FAILURE) {
				Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
						MessageConstants.COM_E_014);
			} else if (succ == Constants.DATE_FAILURE){
				Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
						MessageConstants.COM_E_023);
			}
		}
		}
	});
	// 定义选择列
	var sm = new Ext.grid.RowSelectionModel({
		singleSelect : true
	});
	// GRID Tbar
    var gridTbar = new Ext.Toolbar({
    	border:false,
        height:25,
        items:[btnAdd,btnModify, btnDelete,hiddenFlag]
    });
	var recordGrid = new Ext.grid.GridPanel({
		region : "center",
		store : queryStore,
		columns : [
				// 自动行号
				new Ext.grid.RowNumberer({
							header : "行号",
							width : 31
						}), {
					// 序号
					hidden:true,
					header:'序号',
					dataIndex:'id'
				},{
					
					header : "费用类别",
					sortable : true,
					dataIndex : 'payCode',
					renderer: showSortName
				}, {
					header : "项目代码",
					sortable : true,
					dataIndex : 'proCode'
				}, {
					header : "项目名称",
					sortable : true,
					dataIndex : 'proName'
				},{
					header:"有无清单",
					sortable : true,
					dataIndex:'haveLise',
					renderer:function(value)
					{
						if("Y" == value)
						return "有"
						else if("N" == value)
						return "无"
					}
				}
				],
		sm : sm,
		autoScroll : true,
		enableColumnMove : false,
		autoSizeColumns : true,
		viewConfig : {
			forceFit : false
		},
		// 头部工具栏
		tbar : gridTbar,
        // 分页
        bbar :  new Ext.PagingToolbar({
					pageSize : Constants.PAGE_SIZE,
					store : queryStore,
					displayInfo : true,
					displayMsg : Constants.DISPLAY_MSG,
					emptyMsg : MessageConstants.EMPTY_MSG
				})

	});
	
	/**
	 * 绑定数据
	 */
	function showSortName(value)
	{
		var total = payCodeShowStore.getCount();
		for(var i = 0; i < total; i++)
		{
			var record = payCodeShowStore.getAt(i);
			if(value == record.get("payCode"))
			{
				return record.get("payName")
				break;
			}
			if(i == total -1)
				return value;
		}
	}
    
    // 费用类别store
    var payCodeStore = new Ext.data.JsonStore({
        root : 'list',
        url : "administration/getPaySortCboList.action",
        fields : ['payCode', 'payName'],
		listeners : {
			loadexception : function(ds, records, o) {
			var o = eval("(" + o.responseText + ")");
			var succ = o.msg;
			if (succ == Constants.SQL_FAILURE) {
				Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
						MessageConstants.COM_E_014);
			} 
			else if (succ == Constants.DATE_FAILURE){
				Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
						MessageConstants.COM_E_023);
			}
		}
		}
        
    })
    // 费用类别
    var txtPayCode = new Ext.form.ComboBox({
        fieldLabel : '费用类别<font color ="red">*</font>',
        id : "txtPayCodeID",
        allowBlank : false,
        width:180,
    	store : payCodeStore,
        name:"txtPayCode",
        displayField : "payName",
        valueField : "payCode",
        mode : 'local',
        triggerAction : 'all',
        readOnly:true
    });
    
     payCodeStore.load();
     queryStore.on('load',function()　{
//		if(queryStore.getCount() <= 0)
//			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG, MessageConstants.COM_I_003);
		})
	// 查询数据
	queryStore.load({
            params : {
                start : 0,
                limit : Constants.PAGE_SIZE
            }
    });
	
    // 项目名称
    var txtProName = new Ext.form.TextField({
        fieldLabel : '项目名称<font color ="red">*</font>',
        id : "txtProName",
        allowBlank : false,
        width:180,
        maxLength:25
    });
    var txtHaveLise = new Ext.form.CmbHaveFormFlg({
        fieldLabel : '有无清单',
        id : "txtHaveLise",
        value:"",
        width:180
    })
    // 增加/修改弹出窗口panel
    var subPanel = new Ext.FormPanel({
        labelAlign:"right",
        layout:"form",
        frame : true,
        labelWidth:80,
        items : [txtPayCode,txtProName,txtHaveLise]
    });
    var hiddenId = new Ext.form.Hidden({
    	id:"id"
    })
    var hiddenProCode = new Ext.form.Hidden({
    	id:"hiddenProCode"
    })
    // 保存按钮
    var btnSubSave = new Ext.Button({
        text : Constants.BTN_SAVE,
        iconCls : Constants.CLS_SAVE,
        handler : saveRecord
    });
    // 取消按钮
    var btnSubCancel = new Ext.Button({
        text : Constants.BTN_CANCEL,
    	iconCls :Constants.CLS_CANCEL,
        handler : cancelRecord
    });
    
 	// 增加/修改弹出窗口
    var subWin = new Ext.Window({
    	buttons:[btnSubSave,btnSubCancel,hiddenId,hiddenProCode],
        width : 350,
        height:150,
        buttonAlign : "center",
        modal : true,
        resizable : false,
        modal:true,
        layout : 'fit',
        closeAction : 'hide',
        items : [subPanel]
    })
    
    /**
     * 点击增加按钮时
     */
    function addRecord()
    {
    	subWin.show();
    	hiddenFlag.setValue("0");
    	// 设置子窗口标题
        subWin.setTitle("新增维修项目");
        txtPayCode.setDisabled(false);
        // add by liuyi 090925 新增数据时，使费用类别可选
        txtPayCode.on('beforequery',function(obj){obj.cancel=false});
        // 显示窗口
        subPanel.getForm().reset();
    }
    /**
	 * 点击修改按钮时
	 */
    function modifyRecord() {
    	// 设置修改标识
    	hiddenFlag.setValue("1");
        if (recordGrid.selModel.hasSelection()) {
	        var record = recordGrid.getSelectionModel().getSelected();
	        // 显示主窗口
	        subWin.show();
	        // 设置子窗口标题
	        subWin.setTitle("修改维修项目");
	        txtPayCode.on('beforequery',function(obj){obj.cancel=true});
	        // 加载该行记录
	        subPanel.getForm().loadRecord(record);
	        // 设置信息
	        hiddenId.setValue(record.get("id"));
			if(isExist(record.get("payCode")))
			{
	        	txtPayCode.setValue(record.get("payCode"));
			}else txtPayCode.setValue("");
			hiddenProCode.setValue(record.get("proCode"));
	        txtProName.setValue(record.get("proName"));
	        txtHaveLise.setValue(record.get("haveLise"));
		}else 
            {
                Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
                        MessageConstants.COM_I_001);
            }
    }
    
    function isExist(value){
		var total = payCodeStore.getCount()
		for(i = 0; i < total; i++)
		{
			var record = payCodeStore.getAt(i);
			if(value == record.get("payCode"))
			{
				return true;
				break;
			}
		}
		return false;
    }
    /**
     * 点击删除按钮时
     */
    function delRecord() {
    	// 如果选择了数据的话，先弹出提示
        if (recordGrid.selModel.hasSelection()) {
        	var record = recordGrid.getSelectionModel().getSelected();
                Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG,MessageConstants.COM_C_002,
                    function(buttonobj) {
                        // 如果选择是
                        if (buttonobj == "yes") {
                            Ext.Ajax.request({
                                method : Constants.POST,
                                url : 'administration/deleteMaintenanceProject.action',
                                params : {
                                    id:record.get("id")
                                    },
                                success : function(result, request) {
									if (result.responseText) {
										var o = eval("(" + result.responseText + ")");
										var suc = o.msg;
	                                    // 如果成功，弹出删除成功
	                                    if (suc == Constants.SQL_FAILURE) {
											Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
												MessageConstants.COM_E_014);
	                                    }else if(suc == Constants.DEL_SUCCESS){
		                                        Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
		                                        MessageConstants.COM_I_005)
		                                        // 成功,则重新加载grid里的数据
		                                        // 重新加载数据
		                                    		queryStore.load({
												        params : {
												            start : 0,
												            limit : Constants.PAGE_SIZE
												        }
													});
	                                    }else {
	                                    	 Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
		                                        MessageConstants.DEL_ERROR)
	                                    }
                                    }
                                    }
                                });
                            }
                        });
        }else {
                // 如果没有选择数据，弹出错误提示框
                Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
                    MessageConstants.COM_I_001);
        }
    }
    
    /**
     * 保存按钮按下时
     */
    function saveRecord() 
    {
    	if(txtPayCode.getValue() != null &&txtPayCode.getValue() != "")
    	{
    		// 对项目名称进行为空的check
    		if(txtProName.getValue() != null && txtProName.getValue() != "")
    		{
				if(hiddenFlag.getValue() == "0")
        		{
					// 弹出提示框
    			 	Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG,MessageConstants.COM_C_001,
            			function(buttonobj) {
	                        // 如果选择是,执行增加操作
	                        if (buttonobj == "yes") {
	                        	if(check(txtProName.getValue()) <= 50) {
				            Ext.lib.Ajax.request(Constants.POST,
				            	'administration/addMaintenanceProject.action', {
				            success : function(result, request) {
							if (result.responseText) {
								var o = eval("(" + result.responseText + ")");
								var suc = o.msg;
                                // 如果成功，弹出删除成功
                                if (suc == Constants.SQL_FAILURE) {
									Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
										MessageConstants.COM_E_014);
                                }else if(suc == Constants.COM_I_004){
                                        Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
                                        MessageConstants.COM_I_004,
                                         function() {
															// 隐藏弹出窗口
															subWin.hide();
															 // 重新加载数据
						                            		queryStore.load({
													            params : {
													                start : 0,
													                limit : Constants.PAGE_SIZE
													            }
													    });
														})
//                                        // 成功,则重新加载grid里的数据
//                                        // 隐藏弹出窗口
//		                    	subWin.hide();
                                }
                                // 重新加载数据
//                            		queryStore.load({
//							            params : {
//							                start : 0,
//							                limit : Constants.PAGE_SIZE
//							            }
//							    });
                           	 }
                            }
				            }, "&payCode=" + txtPayCode.getValue()
				            	+"&proName=" + txtProName.getValue()
				            	+"&haveLise=" + txtHaveLise.getValue());
                		}
	                        }
                	})
        		// 如果是在增加状态
        		}else if(hiddenFlag.getValue() == "1")
        		{
	        		 	Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG,MessageConstants.COM_C_001,
	                		function(buttonobj) {
		                        // 如果选择是
		                        if (buttonobj == "yes") {
		                        	if(check(txtProName.getValue()) <= 50) {
				        		// 执行增加操作
					            Ext.lib.Ajax.request(Constants.POST,
					            'administration/modifyMaintenanceProject.action', {
					            	success : function(result, request) {
										if (result.responseText) {
											var o = eval("(" + result.responseText + ")");
											var suc = o.msg;
		                                    // 如果成功，弹出删除成功
		                                    if (suc == Constants.SQL_FAILURE) {
												Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
													MessageConstants.COM_E_014);
		                                    }else if(suc == Constants.COM_I_004){
			                                        Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
			                                        MessageConstants.COM_I_004,
			                                           function() {
															// 隐藏弹出窗口
															subWin.hide();
															 // 重新加载数据
						                            		queryStore.load({
													            params : {
													                start : 0,
													                limit : Constants.PAGE_SIZE
													            }
													    });
														})
			                                        // 成功,则重新加载grid里的数据
			                                        // 隐藏弹出窗口
//					                    	subWin.hide();
		                                    }
//	                                        // 重新加载数据
//	                                    		queryStore.load({
//											            params : {
//											                start : 0,
//											                limit : Constants.PAGE_SIZE
//											            }
//											    });
	                                   	 }
	                                    }
					            }, "id=" + hiddenId.getValue()
					            	 +"&payCode=" + txtPayCode.getValue()
					            	  +"&proName=" + txtProName.getValue()
					            	  +"&haveLise=" + txtHaveLise.getValue());
				                  	}
		                        }
             				})
		        		}
		}else {
			Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, String.format(MessageConstants.COM_E_002,"项目名称"));
			txtProName.markInvalid();
		}
    	}else {
    		Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, String.format(MessageConstants.COM_E_003,"费用类别"));
    		txtPayCode.markInvalid();
    	}
    }
    
    /**
     * 取消按钮按下时
     */
    function cancelRecord() 
    {
    	 Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG,MessageConstants.COM_C_005,
            function(buttonobj) {
                // 如果选择是
                if (buttonobj == "yes") 
                	subWin.hide();
            })
    }
		// 双击进入子画面
    recordGrid.on("rowdblclick",modifyRecord); 
    
    // 查询按钮
    var btnPayAdd = new Ext.Button({
        text : Constants.BTN_ADD,
        iconCls : Constants.CLS_ADD,
        handler : addPayRecord
    });	
    // 修改按钮
    var btnPayModify = new Ext.Button({
        text : Constants.BTN_UPDATE,
        iconCls : Constants.CLS_UPDATE,
        handler : modifyPayRecord
    });	
    // 删除按钮
    var btnPayDelete = new Ext.Button({
        text : Constants.BTN_DELETE,
        iconCls : Constants.CLS_DELETE,
        handler : delPayRecord
    });	
    	
	// 表示是增加还是修改
    var hiddenPayFlag = new Ext.form.Hidden({
        id : "flags",
        value : "0"
    });
    
    // 数据源--------------------------------
	var MyPayRecord = Ext.data.Record.create([{
				// 序号
				name : 'id'
			},{	
				// 费用类别
				name : 'payCode'
			}, {
				// 费用名称 	
				name : 'payName'
			}]);
	// 定义获取数据源
	var dataPayProxy = new Ext.data.HttpProxy({
		url : 'administration/getPaySortList.action'
	});

	// 定义格式化数据
	var thePayReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	}, MyPayRecord);
	// 定义封装缓存数据的对象
	var queryPayStore = new Ext.data.Store({
		// 访问的对象
		proxy : dataPayProxy,
		// 处理数据的对象
		reader : thePayReader,
		listeners : {
			loadexception : function(ds, records, o) {
			var o = eval("(" + o.responseText + ")");
			var succ = o.msg;
			if (succ == Constants.SQL_FAILURE) {
				Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
						MessageConstants.COM_E_014);
			} else if (succ == Constants.DATE_FAILURE){
				Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
						MessageConstants.COM_E_023);
			}
		}
		}
	});
	// 费用类别store改变时，费用类别数据要一致
    queryPayStore.on("load", function() {
			payCodeShowStore.load();
			payCodeStore.load();
			queryStore.load({
	            params : {
	                start : 0,
	                limit : Constants.PAGE_SIZE
            	}
			});
//			if(queryPayStore.getCount() <= 0)
//				Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG, MessageConstants.COM_I_003);
	});

	// 定义选择列
	var smPay = new Ext.grid.RowSelectionModel({
		singleSelect : true
	});
	// GRID Tbar
    var gridPayTbar = new Ext.Toolbar({
    	border:false,
        height:25,
        items:[btnPayAdd,btnPayModify, btnPayDelete,hiddenPayFlag]
    });
	var recordPayGrid = new Ext.grid.GridPanel({
		region : "center",
		store : queryPayStore,
		columns : [
				// 自动行号
				new Ext.grid.RowNumberer({
							header : "行号",
							width : 31
						}), {
					// 序号
					hidden:true,
					dataIndex:'id',
					header:'序号'
				},{
					
					header : "费用类别编码",
					sortable : true,
					dataIndex : 'payCode'
				}, {
					header : "费用类别名称",
					sortable : true,
					dataIndex : 'payName'
				}
				],
		sm : smPay,
		autoScroll : true,
		enableColumnMove : false,
		autoSizeColumns : true,
		viewConfig : {
			forceFit : false
		},
		// 头部工具栏
		tbar : gridPayTbar,
		        // 分页
        bbar :  new Ext.PagingToolbar({
					pageSize : Constants.PAGE_SIZE,
					store : queryPayStore,
					displayInfo : true,
					displayMsg : Constants.DISPLAY_MSG,
					emptyMsg : MessageConstants.EMPTY_MSG
				})
	});
	// 查询数据
	queryPayStore.load({
            params : {
                start : 0,
                limit : Constants.PAGE_SIZE
            }
    });
    // 费用类别
    var txtPayPayCode = new Ext.form.TextField({
        fieldLabel : '费用类别编码',
        id : "txtPayPayCode",
        readOnly:true,
        width:180,
        maxLength:2
    });
    // 项目代码
    var txtPayPayName = new Ext.form.TextField({
        fieldLabel : '费用类别名称<font color ="red">*</font>',
        id : "txtPayPayName",
        allowBlank : false,
        width:180,
        maxLength:25
    });
    // 增加/修改弹出窗口panel
    var subPayPanel = new Ext.FormPanel({
        labelAlign:"right",
        layout:"form",
        frame : true,
        labelWidth:90,
        items : [txtPayPayCode,txtPayPayName]
    });
    var hiddenPayId = new Ext.form.Hidden({
    	id:"id"
    })
    var hiddenPayCode = new Ext.form.Hidden({
    	id:"hiddenPayCode"
    })
    // 保存按钮
    var btnPaySubSave = new Ext.Button({
        text : Constants.BTN_SAVE,
        iconCls : Constants.CLS_SAVE,
        handler : savePayRecord
    });
    // 取消按钮
    var btnPaySubCancel = new Ext.Button({
        text : Constants.BTN_CANCEL,
    	iconCls :Constants.CLS_CANCEL,
        handler : cancelPayRecord
    });
    
 	// 增加/修改弹出窗口
    var subPayWin = new Ext.Window({
    	buttons:[btnPaySubSave,btnPaySubCancel,hiddenPayId,hiddenPayCode],
        width : 350,
        height:180,
        buttonAlign : "center",
        resizable : false,
        layout : 'fit',
        modal:true,
        closeAction : 'hide',
        items : [subPayPanel]
    })
        /**
     * 点击增加按钮时
     */
    function addPayRecord()
    {
    	subPayWin.show();
    	hiddenPayFlag.setValue("0");
    	// 设置子窗口标题
        subPayWin.setTitle("新增费用类别");
        subPayWin.setHeight(120);
        txtPayPayCode.hide();
       	// hide label 
      	txtPayPayCode.getEl().up('.x-form-item').setDisplayed(false);
       	
        // 显示窗口
        subPayPanel.getForm().reset();
    }
    /**
	 * 点击修改按钮时
	 */
    function modifyPayRecord() {
    	// 设置修改标识
    	hiddenPayFlag.setValue("1");
        if (recordPayGrid.selModel.hasSelection()) {
	        var record = recordPayGrid.getSelectionModel().getSelected();
	        // 显示主窗口
	        subPayWin.show();
	        // 设置子窗口标题
	        subPayWin.setTitle("修改费用类别");
	        subPayWin.setHeight(150);
	        txtPayPayCode.show();
       		// show label 
      		txtPayPayCode.getEl().up('.x-form-item').setDisplayed(true);
	        // 加载该行记录
	        subPayPanel.getForm().loadRecord(record);
	        // 设置信息
	        hiddenPayId.setValue(record.get("id"));
	        txtPayPayCode.setValue(record.get("payCode"));
	        hiddenPayCode.setValue(record.get("payCode"));
	        txtPayPayName.setValue(record.get("payName"));
		}else 
            {
                Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
                        MessageConstants.COM_I_001);
            }
    }
    
    /**
     * 点击删除按钮时
     */
    function delPayRecord() {
    	// 如果选择了数据的话，先弹出提示
        if (recordPayGrid.selModel.hasSelection()) {
        	var record = recordPayGrid.getSelectionModel().getSelected();
                Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG,MessageConstants.AV010_C_001,
                    function(buttonobj) {
                        // 如果选择是
                        if (buttonobj == "yes") {
                            Ext.Ajax.request({
                                method : Constants.POST,
                                url : 'administration/deletePaySort.action',
                                params : {
                                    id:record.get("id")
                                    },
                                success : function(result, request) {
									if (result.responseText) {
										var o = eval("(" + result.responseText + ")");
										var suc = o.msg;
	                                    // 如果成功，弹出删除成功
	                                    if (suc == Constants.SQL_FAILURE) {
											Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
												MessageConstants.COM_E_014);
	                                    }else if(suc == Constants.DEL_SUCCESS){
		                                        Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
		                                        MessageConstants.COM_I_005)
		                                        // 成功,则重新加载grid里的数据
		                                        // 重新加载数据
		                                    		queryPayStore.load({
												            params : {
												                start : 0,
												                limit : Constants.PAGE_SIZE
												            }
												    });
	                                    }else {
	                                    	 Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
		                                        MessageConstants.DEL_ERROR)
	                                    }
                                    }
                                    }    
                                });
                            }
                        });
        }else {
                // 如果没有选择数据，弹出错误提示框
                Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
                    MessageConstants.COM_I_001);
        }
    }
    
    /**
     * 保存按钮按下时
     */
    function savePayRecord() 
    {
		var isExit =0;
		// check画面中项目代码是否存在
		for(i=0 ; i< queryPayStore.getCount();i++)
		{
			var record=queryPayStore.getAt(i);
			if(record.get('payCode') == txtPayPayCode.getValue())
			{
				isExit++;
			}
		}
		//　如果是在新增状态
		if(hiddenPayFlag.getValue() == "0")
		{
			// 对费用名称进行为空的check
    		if(txtPayPayName.getValue() != null && txtPayPayName.getValue() != "")
    		{
				// 弹出提示框
			 	Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG,MessageConstants.COM_C_001,
        			function(buttonobj) {
                        // 如果选择是,执行增加操作
                        if (buttonobj == "yes") {
                        	if(check(txtPayPayName.getValue()) <= 50) {
			            Ext.lib.Ajax.request(Constants.POST,
			            	'administration/addPaySort.action', {
			            	success : function(result, request) {
								if (result.responseText) {
										var o = eval("(" + result.responseText + ")");
										var suc = o.msg;
	                                    // 如果成功，弹出删除成功
	                                    if (suc == Constants.SQL_FAILURE) {
											Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
												MessageConstants.COM_E_014);
	                                    }else if(suc == Constants.COM_I_004){
		                                        Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
		                                        MessageConstants.COM_I_004,
		                                        function() {
															// 隐藏弹出窗口
															subPayWin.hide();
															// 重新加载数据
				                                    		queryPayStore.load({
														            params : {
														                start : 0,
														                limit : Constants.PAGE_SIZE
														            }
														    });
														})
	                                    }
                                        
										                                   	 }
                                    }	
			            	},"&payName=" + txtPayPayName.getValue());
                		}
                        }
                	})
    		}else {
    			Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, String.format(MessageConstants.COM_E_002,"费用类别名称"));
    			txtPayPayName.markInvalid();
    		}
		// 如果是在增加状态
		}else if(hiddenPayFlag.getValue() == "1")
		{
	    		// 对费用类别名称进行为空的check
	    		if(txtPayPayName.getValue() != null && txtPayPayName.getValue() != "")
	    		{
	        		 Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG,MessageConstants.COM_C_001,
                		function(buttonobj) {
                        // 如果选择是
                        if (buttonobj == "yes") {
                        	if(check(txtPayPayName.getValue()) <= 50) {
		        		// 执行增加操作
			            Ext.lib.Ajax.request(Constants.POST,
			            'administration/modifyPaySort.action', {
			            	success : function(result, request) {
									if (result.responseText) {
										var o = eval("(" + result.responseText + ")");
										var suc = o.msg;
	                                    // 失败
	                                    if (suc == Constants.SQL_FAILURE) {
											Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
												MessageConstants.COM_E_014);
										// 如果成功，弹出删除成功
	                                    }else if(suc == Constants.COM_I_004){
		                                        Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
		                                        MessageConstants.COM_I_004,
		                                        function() {
															// 隐藏弹出窗口
															subPayWin.hide();
															// 重新加载数据
				                                    		queryPayStore.load({
														            params : {
														                start : 0,
														                limit : Constants.PAGE_SIZE
														            }
														    });
														})
	                                    }
                                   	 }
                                    }
			            }, "id=" + hiddenPayId.getValue()
			            	  +"&payName=" + txtPayPayName.getValue());
		                  	}
                        }
         				})
    			}else {
    				Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, String.format(MessageConstants.COM_E_002,"费用类别名称"));
    				txtPayPayName.markInvalid();
    			}
    			
	    	}
    }
    
    /**
     * 取消按钮按下时
     */
    function cancelPayRecord() 
    {
    	 Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG,MessageConstants.COM_C_005,
            function(buttonobj) {
                // 如果选择是
                if (buttonobj == "yes") 
                	subPayWin.hide();
            })
    }
		// 双击进入登记tab
    recordPayGrid.on("rowdblclick",modifyPayRecord); 
	// 费用类别Panel
	var tabPaySort= new Ext.Panel({
				title : '费用类别维护',
				layout : 'fit',
				border : false,
				items : [recordPayGrid]
			})		
        // 维修项目维护Panel
	var tabMaintenance = new Ext.Panel({
				title : '维修项目维护',
				layout : 'border',
				border : false,
				items : [recordGrid]
			})
			
    // **********主画面********** //
	var tabPanel = new Ext.TabPanel({
				activeTab : 0,
				autoScroll : true,
				layoutOnTabChange : true,
				tabPosition : 'bottom',
				border : false,
				items : [tabPaySort, tabMaintenance]
			})

	// **********主画面********** //
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		border : false,
		items : [{
					layout : 'fit',
					border : false,
					margins : '0 0 0 0',
					region : 'center',
					autoScroll : true,
					items : [tabPanel]
				}]
	});
	
	/**
	 * 获取字符串的单字节长度 
	 */
	function check(str) {   
  		var reg=/[^\x00-\xff]/
  		var s = 0;  
  		var ts;   
  		for (i=0;i<str.length;i++) { 
    		ts=str.substring(i,i+1); 
    		if (reg.test(ts))
    		{
    			s = s + 2;
    		}else {
				s = s + 1;    			
  			}
  		}
  		return s;
	} 		
})