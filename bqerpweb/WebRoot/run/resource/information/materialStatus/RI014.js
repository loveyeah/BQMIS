// 画面：物料基础信息维护/物料状态码维护
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function(){

	// 分页时每页显示记录条数
	var PAGE_SIZE = Constants.PAGE_SIZE;

    // 新增按钮
    var addBtn = new Ext.Button({
        id : 'add',
        text : "新增",
        iconCls : Constants.CLS_ADD,
        handler : addRecord
    });
    // 修改按钮
    var updateBtn = new Ext.Button({
        id : 'update',
        text : Constants.BTN_UPDATE,
        iconCls : Constants.CLS_UPDATE,
        handler : updateRecord
    });
    // 删除按钮
    var deleteBtn = new Ext.Button({
        id : 'delete',
        text : Constants.BTN_DELETE,
        iconCls : Constants.CLS_DELETE,
        handler : deleteRecord
    });
    // grid中的数据  编码,名称,描述,流水号
    var runGridList = new Ext.data.Record.create([{
        name : 'statusNo'
    }, {
        name : 'statusName'
    }, {
        name : 'statusDesc'
    }, {
    	name : 'materialStatusId'
    }
    ]);
    
    // grid中的store
    var runGridStore = new Ext.data.Store({
        proxy : new Ext.data.HttpProxy({
            url : 'resource/getMaterialStatusList.action'
        }),
        reader : new Ext.data.JsonReader({
            root : 'list',
            totalProperty : 'totalCount'
        }, runGridList)
    });
    runGridStore.setDefaultSort('statusNo', 'ASC');
    // 初始化时,显示所有数据  
	runGridStore.load({
		params : {
			start : 0,
			limit : PAGE_SIZE
		}
	});

   // 运行执行的Grid主体
    var runGrid = new Ext.grid.GridPanel({
        store : runGridStore,
        columns : [
                // 自动生成行号
				new Ext.grid.RowNumberer({
					header : '行号',
					width : 35
				}),{
                    header : '编码',
                    width : 30,
                    align : 'left',
                    sortable : true,
                    dataIndex : 'statusNo'
                }, {
                    header : '名称',
                    width : 50,
                    align : 'left',
                    sortable : true,
                    dataIndex : 'statusName'
                }, {
                    header : '描述',
                    width : 80,
                    align : 'left',
                    sortable : true,
                    dataIndex : 'statusDesc',
                    renderer : function (value) {
                                if (value) {
                                    return value.replace(/\n/g, '<br/>');
                                }
                                return '';
                            }
                }, {
					hidden : true,
				    header : '流水号',
					dataIndex : 'materialStatusId'
				}],
        viewConfig : {
            forceFit : true
        },
        tbar : [ addBtn,
            	{xtype : "tbseparator"}, updateBtn,
            	{xtype : "tbseparator"}, deleteBtn],
        //分页
        bbar : new Ext.PagingToolbar({
            pageSize : PAGE_SIZE,
            store : runGridStore,
            displayInfo : true,
            displayMsg : Constants.DISPLAY_MSG,
            emptyMsg : Constants.EMPTY_MSG
        }),
       sm : new Ext.grid.RowSelectionModel({singleSelect : true}),
       frame : false,
       border : false,
       enableColumnHide : true,
       enableColumnMove : false
    });
    
	// 注册双击事件
	runGrid.on("rowdblclick", updateRecord);
    
    // 设定布局器及面板
	new Ext.Viewport({
        enableTabScroll : true,   
        layout : "border",
        items : [{
			xtype : "panel",
			region : 'center',
			layout : 'fit',
			border : false,
			items : [runGrid]
		}]
    });
	
 	// 定义form中的addFlag:新增：1 修改：0
	var addFlag = {
		id : "addFlag",
		xtype : "hidden",
		value : "",
		name : "addFlag"
	}
	// 定义form中的statusNo
	var statusNo = {
	    id : "statusNo",
		xtype : "textfield",
		fieldLabel : "编码<font color='red'>*</font>",
	 	allowBlank : false,	
	 	blankText : '编码不能为空,请输入',
		maxLength : 10,
		codeField : "yes",
		width : 220,
		style :{
            'ime-mode' : 'disabled'
        },
		name : 'statusBeen.statusNo'
	}

	// 定义form中的statusName
	var statusName = {
	    id : "statusName",
		xtype : "textfield",
		fieldLabel : "名称<font color='red'>*</font>",
	 	allowBlank : false,
	    blankText : '名称不能为空,请输入',
		maxLength : 50,
		width : 220,
		name : 'statusBeen.statusName'
	}

	// 定义form中的statusDesc
	var statusDesc = {
		id : "statusDesc",
		xtype : "textarea",
		fieldLabel : "描述",
		allowBlank : true,
		maxLength : 100,
		width : 217,
		height : 80,
		name : 'statusBeen.statusDesc'
	}
	// 定义form中的materialStatusId
	var materialStatusId = {
		id : "materialStatusId",
		xtype : "hidden",
		name : "statusBeen.materialStatusId"
	}
 
	// 定义弹出窗体中的form
	var mypanel = new Ext.FormPanel({
				labelAlign : 'right',
				autoHeight : true,
				frame : true,
				items : [statusNo, statusName, statusDesc, addFlag, materialStatusId]
			});
			
	// 定义弹出窗体
	var win = new Ext.Window({
				width : 400,
				autoHeight : 100,				
				buttonAlign : "center",
				items : [mypanel],
				layout : 'fit',
				closeAction : 'hide',
				modal:true,
				resizable : false,
				buttons : [{
					text : Constants.BTN_SAVE,
					iconCls : Constants.CLS_SAVE,
					handler : function() {
					            trimStr();						
								var myurl = "";
								if(Ext.get("addFlag").dom.value == "1") {
									myurl = "resource/addMaterialStatus.action";
								} else {
									myurl = "resource/updateMaterialStatus.action";
								}
								Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_C_001, function(
					                buttonobj) {
						            if (buttonobj == "yes") {
						            	var nullMsg = "";
										if(Ext.get("statusNo").dom.value == null || Ext.get("statusNo").dom.value == "") {		    
				    						nullMsg = nullMsg + String.format(Constants.COM_E_002,"编码") + "<br/>";
										}
										if(Ext.get("statusName").dom.value == null || Ext.get("statusName").dom.value == "") {		    
				    						nullMsg = nullMsg + String.format(Constants.COM_E_002,"名称");
										}
										if(nullMsg != "") {
											Ext.Msg.alert(Constants.SYS_REMIND_MSG,	nullMsg);
										    return;
										}
						            	// 画面控件是否红线
    	                                if (!mypanel.getForm().isValid()) {
			                                return;
		                                }
										mypanel.getForm().submit({
										method : 'POST',
										url : myurl,
										success : function(form, action) {
										 	var o = eval("(" + action.response.responseText
										 		+ ")");
										 	// 保存成功
										 	if(o.flag==0) {
										 	    Ext.Msg.alert(Constants.SYS_REMIND_MSG,Constants.COM_I_004);
										 	} else if(o.flag==1) {
										 	// 名称重复
										 		Ext.Msg.alert(Constants.SYS_REMIND_MSG, String.format(Constants.COM_E_007,
										 			"物料名称"));
										 		return;
										 	} else if(o.flag==2) {
										 	// 编码重复
										 		Ext.Msg.alert(Constants.SYS_REMIND_MSG, String.format(Constants.COM_E_007,
										 			"物料编码"));
										 		return;
										 	}								 	
											runGridStore.load({
													params : {
														start : 0,
														limit : Constants.PAGE_SIZE												
													}
												});
											win.hide();
										},
										faliue : function() {									
											Ext.Msg.alert(Constants.SYS_REMIND_MSG,
													Constants.UNKNOWN_ERR);
										}
								    });
						        }						
						    });					
						}
					},{
					text : Constants.BTN_CANCEL,
					iconCls : Constants.CLS_CANCEL,
					handler : function() {
							win.hide();
						}
					}]
			});	
	/**
	 * 增加函数
	 */
	function addRecord() {
		mypanel.getForm().reset();
		win.setTitle("新增物料状态码");	
		win.show();
		Ext.get("addFlag").dom.value = "1";
		Ext.get("statusNo").dom.readOnly = false;
	}
	/**
	 * 修改函数
	 */
	function updateRecord() {
		// 是否有被选项
		if (runGrid.selModel.hasSelection()) {
			var records = runGrid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert(Constants.SYS_REMIND_MSG,
						Constants.SELECT_COMPLEX_MSG);
			} else {
				var record = runGrid.getSelectionModel().getSelected();
				win.setTitle("修改物料状态码");				
				win.show();			
				mypanel.getForm().loadRecord(record);
				Ext.get("addFlag").dom.value = "0";			
				Ext.get("materialStatusId").dom.value = record.get('materialStatusId');
				Ext.get("statusNo").dom.readOnly = true;				
			}
		} else {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG,
					Constants.COM_I_001);
		}		
	}
	/**
	 * 删除函数
	 */
	function deleteRecord() {
	    // 是否有被选项
		if (runGrid.selModel.hasSelection()) {
		    var records = runGrid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert(Constants.SYS_REMIND_MSG,
						Constants.SELECT_COMPLEX_MSG);
			} else {
				var record = runGrid.getSelectionModel().getSelected();
				Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_C_002, function(
					buttonobj) {
					if (buttonobj == "yes") {
						Ext.lib.Ajax.request(Constants.POST,
								'resource/deleteMaterialStatus.action', {
									success : function(action) {
								 	    // 删除成功
								 	    Ext.Msg.alert(Constants.SYS_REMIND_MSG,Constants.COM_I_005);								 	    
										runGridStore.load({
												params : {
													start : 0,
													limit : Constants.PAGE_SIZE													
												}
											});
									},
									failure : function() {
										Ext.Msg.alert(Constants.SYS_REMIND_MSG,
											Constants.DEL_ERROR);
										}
									}, 'statusBeen.materialStatusId=' + record.get('materialStatusId'));							
						}
				})
				
				
			}
		} else {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG,
					Constants.COM_I_001);
		}	
	}
	
	/**
	 * 去除 statusNo 的值的首尾空格
	 */
	function vaildaStatusNo() {
		var obj = Ext.get("statusNo");
		vailda(obj);
	}
	/**
	 * 去除 statusName 的值的首尾空格
	 */	
	function vaildaStatusName() {		
		var obj = Ext.get("statusName");
		vailda(obj);
	}
	/**
	 * 去除 obj 的值的首尾空格
	 */
	function vailda(obj) {
		var oldValue = obj.dom.value;		
		var newValue = oldValue.trim();
		obj.dom.value = newValue;
	}
		/**
	 * 清除输入后的首尾空格
	 */
	function trimStr() {
		vaildaStatusNo();
		vaildaStatusName();
				// 去除首尾的换行符
		var descStr = Ext.get("statusDesc").dom.value;
		Ext.get("statusDesc").dom.value = descStr.replace(/(^\s*)/g, "").replace(/(\s*$)/g, "");
	}
});