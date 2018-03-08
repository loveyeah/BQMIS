Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {
	//增加和修改控制 '0':修改；'1':增加
	var flag = '0';
	// 增加按钮
	var btnAdd = new Ext.Button({
				id : 'add',
				text : Constants.BTN_ADD,
				iconCls : Constants.CLS_ADD,
				handler : addHander
			});

	// 修改按钮
	var btnEdit = new Ext.Button({
				id : 'edit',
				text : Constants.BTN_UPDATE,
				iconCls : Constants.CLS_UPDATE,
				handler : editMsg
			});
	// 删除按钮
	var btnDelete = new Ext.Button({
				id : 'delete',
				text : Constants.BTN_DELETE,
				iconCls : Constants.CLS_DELETE,
				handler : deleteHander
			});
  
	// 类定义
	var paperGridList = new Ext.data.Record.create([{
				name : 'id'
			}, {
				name : 'papertypeCode'
			}, {
				name : 'papertypeName'
			}, {
				name : 'retrieveCode'
			}, {
				name : 'updateTime'
			}]);
	// grid的store
    var paperStore = new Ext.data.JsonStore({
        url : 'administration/searchPaper.action',
        root : 'list',
        totalProperty : 'totalCount',
        fields : paperGridList
        , listeners : {
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
    
    // 排序
    paperStore.setDefaultSort('id','ASC');
    paperStore.load({
        params : {
            start : 0,
            limit : Constants.PAGE_SIZE
        }
    });
    
	// 分页工具栏
	var pagebar = new Ext.PagingToolbar({
				pageSize : Constants.PAGE_SIZE,
				store : paperStore,
				displayInfo : true,
				displayMsg : MessageConstants.DISPLAY_MSG,
				emptyMsg : MessageConstants.EMPTY_MSG
			});
   //gridpanel显示格式定义
	var paperGrid = new Ext.grid.GridPanel({
				store : paperStore,
				columns : [new Ext.grid.RowNumberer({
									header : '行号',
									width : 35
								}), {
							header : "类别编码",
							width : 80,
							sortable : true,
							dataIndex : 'papertypeCode'
						}, {
							header : "类别名称",
							width : 150,
							sortable : true,
							dataIndex : 'papertypeName'
						}, {
							header : "检索码",
							width : 150,
							sortable : true,
							dataIndex : 'retrieveCode'
						}],
				viewConfig : {
					forceFit : false
				},
				sm : new Ext.grid.RowSelectionModel({
							singleSelect : true
						}),
			    region : "center",
				layout : 'fit',
				autoScroll : true,
				enableColumnMove : false,
				autoSizeColumns : true,
				tbar : [btnAdd, btnEdit, btnDelete],
				bbar : pagebar
			});
	// 设定布局器及面板
	var layout = new Ext.Viewport({
				layout : "border",
				border : false,
				items : [{
							title : "",
							region : 'center',
							layout : 'fit',
							border : false,
							margins : '0 0 0 0',
							split : true,
							collapsible : false,
							items : [paperGrid]
						}]
			});
	// 双击编辑
	paperGrid.on("rowdblclick", editMsg);
    // 组件默认宽度
    var width = 180;   
	// 修改面板 隐藏序号
	var hiddenId = {
		id : 'id',
		xtype : 'hidden',
		readOnly : true,
		hidden : true,
		name : 'adCPaper.id'
	}; 
    //修改面板 类别编码
    var txtUpdateTypeCode = new Ext.form.TextField({
        fieldLabel : "类别编码",
        id : "updatePapertypeCode",
        name : "adCPaper.papertypeCode",
        isFormField:true,
        allowBlank : false,
        maxLength : 2,
        width : width
    });

    // 修改面板 类别名称
    var txtUpdateTypeName = new Ext.form.TextField({
        fieldLabel : "类别名称<font color='red'>*</font>",
        id : "updatePapertypeName",
        name : "adCPaper.papertypeName",
        allowBlank : false,
        maxLength : 25,
        width : width
    });

    // 修改面板 检索码
    var txtUpdateRetrieveCode = new Ext.form.TextField({
        fieldLabel : "检索码  ",
        id : "updateRetrieveCode",
        name : "adCPaper.retrieveCode",
        style : "ime-mode:disabled",
        onlyLetter : true,
        maxLength : 6,
        width : width
    });   
    // 修改面板
    var updatePanel = new Ext.FormPanel({
        frame : true,
        labelWidth : 80,        
        labelAlign : 'right',
        defaultType : "textfield",
        items : [hiddenId,txtUpdateTypeCode, txtUpdateTypeName,txtUpdateRetrieveCode]
    });    
    // 修改面板 弹出窗口
    var updateWin = new Ext.Window({
        width : 350,
        height : 180,
        modal : true,
        buttonAlign : "center",
        resizable : false,
        items : [updatePanel],
        title : '',
        buttons : [{
			// 保存按钮
			text : Constants.BTN_SAVE,
			iconCls : Constants.CLS_SAVE,
			handler : confirmRecord
		},
        {  // 取消按钮
		   text : Constants.BTN_CANCEL,
		   iconCls : Constants.CLS_CANCEL,
		   handler : function() {
			 Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG, MessageConstants.COM_C_005,
				function(buttonobj) {
					if (buttonobj == 'yes') {
						updateWin.hide();
					}
				})
					}
				}],
        layout : 'fit',
        closeAction : 'hide'
    });
    
    // 增加面板 类别名称
    var txtAddTypeName = new Ext.form.TextField({
        fieldLabel : "类别名称<font color='red'>*</font>",
        id : "addPapertypeName",
        name : "adCPaper.papertypeName",
        allowBlank : false,
        maxLength : 25,
        width : width
    });

    // 增加面板 检索码
    var txtAddRetrieveCode = new Ext.form.TextField({
        fieldLabel : "检索码  ",
        id : "addRetrieveCode",
        name : "adCPaper.retrieveCode",
        style : "ime-mode:disabled",
        onlyLetter : true,
        maxLength : 6,
        width : width
    });   
    // 增加面板
    var addPanel = new Ext.FormPanel({
        frame : true,
        labelWidth : 80,        
        labelAlign : 'right',
        defaultType : "textfield",
        items : [txtAddTypeName, txtAddRetrieveCode]
    });    
    // 增加面板 弹出窗口
    var addWin = new Ext.Window({
        width : 350,
        height : 130,
        modal : true,
        buttonAlign : "center",
        resizable : false,
        items : [addPanel],
        title : '',
        buttons : [{
			// 保存按钮
			text : Constants.BTN_SAVE,
			iconCls : Constants.CLS_SAVE,
			handler : confirmRecord
		},
        {  // 取消按钮
		   text : Constants.BTN_CANCEL,
		   iconCls : Constants.CLS_CANCEL,
		   handler : function() {
			 Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG, MessageConstants.COM_C_005,
				function(buttonobj) {
					if (buttonobj == 'yes') {
						addWin.hide();
					}
				})
					}
				}],
        layout : 'fit',
        closeAction : 'hide'
    });
    /**
	 * 按下保存按钮动作
	 */
	function confirmRecord(){
	       if(flag == '1'){
	       	    	if(addIsNotNull()){
	       	    		Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG, MessageConstants.COM_C_001,pressYesButton);	 	   
	       	    	}else{
	       	    		return;
	       	    	}
	       	    }
	       if(flag == '0'){
	       	    	if(updateIsNotNull()){
	       	    		Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG, MessageConstants.COM_C_001,pressYesButton);	 	  
	       	    	}else{
	       	    		return;
	       	    	}
	       	    }
	       }
	/**
	 * 按下YES按钮动作
	 */
	function  pressYesButton(buttonobj){
		//判断用户所按按钮值
	    if(buttonobj == 'yes'){
	        var myurl = "";				       
				if (flag == "1") {
					myurl = 'administration/addPaper.action';
					addPanel.getForm().submit({
							method : Constants.POST,
							url : myurl,
							success : function(form, action) {
									var result = eval('('
											+ action.response.responseText
											+ ')');
							// sql 异常
										if(result&&result.msg){
											if (result.msg == Constants.SQL_FAILURE) {
												Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
																MessageConstants.COM_E_014);
											} else {
							// 显示成功信息
							Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
								MessageConstants.COM_I_004,function(){
							      paperStore.load({
								  params : {
										start : 0,
										limit : Constants.PAGE_SIZE
										}
									});
								  paperGrid.getView().refresh();
								  addWin.hide();
								});	
							   }
						     }
							},
							failure : function() {
									}
						});
				}			     	
			    if (flag == "0") {
					myurl = 'administration/updatePaper.action';
					updatePanel.getForm().submit({
					        method : Constants.POST,
							url : myurl,
							success : function(form, action) {
								    var result = eval('('
										+ action.response.responseText
										+ ')');
									// sql 异常
										if(result&&result.msg){
											if (result.msg == Constants.SQL_FAILURE) {
												Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
																MessageConstants.COM_E_014);
											} else {
									// 显示成功信息
									Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
										MessageConstants.COM_I_004,function(){
									    paperStore.load({
										params : {
											start : 0,
											limit : Constants.PAGE_SIZE
										 }
										});
										paperGrid.getView().refresh();
											   updateWin.hide();
										});	
									
							   }
							}
							},
							failure : function() {
										}
					});
				}
	    }
	}

    // 增加处理
    function addHander() {
        flag = '1'
        addPanel.getForm().reset();
        addWin.setTitle("新增证件类别")
        addWin.show();
    }
	// 修改处理
	function editMsg() {
	    flag ='0';
        // 判断是否已选择了数据
        if (paperGrid.selModel.hasSelection()) {
            var record = paperGrid.getSelectionModel().getSelected();
                updateWin.setTitle("修改证件类别");
                txtUpdateTypeCode.readOnly = true;
                updateWin.show();
                // 设置隐藏域的值
                Ext.get('id').dom.value = record.get('id') == null
					? ""
					: record.data.id;
                Ext.get('updatePapertypeCode').dom.value = record.get('papertypeCode') == null
					? ""
					: record.data.papertypeCode;
				Ext.get('updatePapertypeName').dom.value = record.get('papertypeName') == null
					? ""
					: record.data.papertypeName;
				Ext.get('updateRetrieveCode').dom.value = record.get('retrieveCode') == null
					? ""
					: record.data.retrieveCode;
              
        }else {
            Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
                    MessageConstants.COM_I_001);
        }
	}
    
    //删除处理
    function deleteHander() {
        // 判断是否已选择了数据
        if (paperGrid.selModel.hasSelection()) {
            var record = paperGrid.getSelectionModel().getSelected();
            var paperIdValue = record.get("id");
            Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG, MessageConstants.COM_C_002,
            function(buttonObj) {
                if (buttonObj == "yes") {
                    Ext.Ajax.request({
                           method : Constants.POST,
                           url : 'administration/deletePaper.action',
                           params : {
									paperIdValue : paperIdValue
								}, 
                           success : function(result, request) {
                                var o = eval("(" + result.responseText
											+ ")");
									if(o&&o.msg){
									// sql异常
									   if(o.msg == Constants.SQL_FAILURE){
									      Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
																MessageConstants.COM_E_014);
									   }else{
									   // 显示成功
									   Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
												MessageConstants.COM_I_005,function(){
													 paperStore.load({
													 params : {
														start : 0,
														limit : Constants.PAGE_SIZE
													   }
												      });
										             paperGrid.getView().refresh();													
												});		
									   	
									   }
									 }
								},
                                failure : function() {
                                }
                            });
                       }
                   });
        } else {
            Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,MessageConstants.COM_I_001);
        }
    }
    // 修改时判断证件类别名称是否为空
    function updateIsNotNull() {
    	var msg = "";
    	if ( txtUpdateTypeName.getValue() == "" ) {
    		msg = String.format(MessageConstants.COM_E_002,"类别名称");
    		Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, msg);
    		return false
    	}
    	return true;
    }
    // 增加时判断证件类别名称是否为空
    function addIsNotNull() {
    	var msg = "";
    	if ( txtAddTypeName.getValue() == "" ) {
    		msg = String.format(MessageConstants.COM_E_002,"类别名称");
    		Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, msg);
    		return false
    	}
    	return true;
    }
})