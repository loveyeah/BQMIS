Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {
	// 组件默认宽度
	var width = 180;
	// 新增或修改,flag='0'更新;flag='1'增加
	var flag = '0';
    // 菜谱类别下拉框
	var drpMenuType = new Ext.form.CmbCMenuType({
	            fieldLabel : "类别",
				id : "drpMenuTypeCode",
				allowBlank : true,
				triggerAction : 'all',
				displayField : 'strMenuTypeName',
				valueField : 'strMenuTypeCode',
				mode : 'local',
				readOnly : true,
				width : 150,
				anchor : '100%'
	});
	//load
	drpMenuType.store.load();
	// 设置初始值
	drpMenuType.store.on("load",function(){
	    if(drpMenuType.store.getCount()==0){
	    	var msg = String.format(MessageConstants.COM_I_013, '菜谱类别');
	        Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,msg,function(){
							      var parentTab = parent.Ext.getCmp("mainTabPanel");
							        parentTab.remove(parentTab.activeTab);
						        }
						    );
	    }else{
	        var initData = drpMenuType.store.getAt(0).get('strMenuTypeCode');
	        drpMenuType.setValue(initData,true);
		    searchByMenu();
	    }	
	})
		
	// 设置菜谱类别下拉框监听事件
	drpMenuType.addListener('select', searchByMenu);	
	// 增加按钮
	var btnAdd = new Ext.Button({
				id : 'add',
				text : Constants.BTN_ADD,
				iconCls : Constants.CLS_ADD,
				handler : addHandler
			});

	// 修改按钮
	var btnEdit = new Ext.Button({
				id : 'edit',
				text : Constants.BTN_UPDATE,
				iconCls : Constants.CLS_UPDATE,
				handler : editHandler
			});

	// 删除按钮
	var btnDelete = new Ext.Button({
				id : 'delete',
				text : Constants.BTN_DELETE,
				iconCls : Constants.CLS_DELETE,
				handler : deleteHandler 
			});

	// 类定义
	var recordMenuGridList = new Ext.data.Record.create([{
				name : 'id'
			}, {
				name : 'menuCode'
			}, {
				name : 'menuName'
			}, {
				name : 'menuMemo'
			}, {
				name : 'menuPrice'
			}, {
				name : 'retrieveCode'
			}, {
				name : 'menutypeCode'
			}, {
				name : 'updateTime'
			}]);
	// grid的store
	var storeMenu = new Ext.data.JsonStore({
				url : 'administration/searchMenu.action',
                root : 'list',
                totalProperty : 'totalCount',
				fields : recordMenuGridList
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
	//排序
	storeMenu.setDefaultSort('id', 'ASC');
	// 分页工具栏
	var pagebar = new Ext.PagingToolbar({
				pageSize : Constants.PAGE_SIZE,
				store : storeMenu,
				displayInfo : true,
				displayMsg : MessageConstants.DISPLAY_MSG,
				emptyMsg : MessageConstants.EMPTY_MSG
			});
	var gridMenu = new Ext.grid.GridPanel({
				store : storeMenu,
				columns : [new Ext.grid.RowNumberer({
									header : '行号',
									width : 35
								}),{	
							header : "菜谱编码",
							width : 60,
							sortable : true,	
							dataIndex : 'menuCode'
						}, {
							header : "菜谱名称",
							width : 100,
							sortable : true,
							dataIndex : 'menuName'
						}, {
							header : "配料说明",
							width : 180,
							sortable : true,
							dataIndex : 'menuMemo'
						}, {
							header : "价格",
							width : 100,
							align : 'right',
							sortable : true,
							renderer : divide2,
							dataIndex : 'menuPrice'
						}, {
							header : "检索码",
							width : 100,
							sortable : true,
							dataIndex : 'retrieveCode'
						}],
				viewConfig : {
					forceFit : false
				},
				sm : new Ext.grid.RowSelectionModel({
							singleSelect : true
						}),
				tbar : ['菜谱类别:', drpMenuType,'-', btnAdd, btnEdit, btnDelete],
				bbar : pagebar,
				frame : false,
				border : false

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
							items : [gridMenu]
						}]
			});
	// 双击编辑
	gridMenu.on("rowdblclick", editHandler);
	// 菜谱类别
	var txtMenuType = new Ext.form.TextField({
				fieldLabel : "菜谱类别",
				id : "menutypeCode",
				name : "menuWH.menutypeCode",
				anchor : '90%'
			});
	// 菜谱名称
	var txtMenuName = new Ext.form.TextField({
				fieldLabel : "菜谱名称<font color='red'>*</font>",
				id : "menuName",
				name : "menuWH.menuName",
				maxLength : 25,
				allowBlank : false,
				isFormField : true,
				anchor : '90%'
			});
	// 价格
	var txtPrice = new Ext.form.MoneyField({
		        fieldLabel : "价格",
				id : 'menuPrice',
				isFormField : true,
				decimalPrecision : 2,
				padding : 2,
				maxValue : 99999999999.99,
				maxLength : 14,
				appendChar : '元',
				allowNegative : false,
				style : 'text-align:right',
				anchor : '90%'
	})
	var hidePrice = new Ext.form.Hidden({
		name : "menuWH.menuPrice"
	})
	// 检索码
	var txtRetrieveCode = new Ext.form.TextField({
                id : "retrieveCode",
                fieldLabel : "检索码",
                style : "ime-mode:disabled",
                onlyLetter : true,
                width : 128,
                maxLength : 6,
                anchor : '90%',
                name : 'menuWH.retrieveCode'
            });
	// 配料说明
	var txaMenuMemo = new Ext.form.TextArea({
				fieldLabel : "配料说明",
				id : "menuMemo",
				name : "menuWH.menuMemo",
				maxLength : 127,
				height : 120,
				anchor : '90%'
			});
	// 隐藏的上次修改时间
	var hiddenlastModifiedDate = {
		id : 'updateTime',
		xtype : 'hidden',
		readOnly : true,
		hidden : true,
		value : '0',
		name : 'menuWH.updateTime'
	};
	// 隐藏序号
	var hiddenId = {
		id : 'id',
		xtype : 'hidden',
		readOnly : true,
		hidden : true,
		value : '0',
		name : 'menuWH.id'
	};
	// 第一行
	var firstLine = new Ext.Panel({
		border : false,
		layout : "column",
		style : "padding-top:5px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
		anchor : '100%',
		items : [{ // 类别
			columnWidth : 0.46,
			layout : "form",
			border : false,
			items : [txtMenuType]
		}, {	// 菜谱名称
					columnWidth : 0.46,
					layout : "form",
					border : false,
					items : [txtMenuName]
				}]
	});
	// 第二行
	var secondLine = new Ext.Panel({
		border : false,
		layout : "column",
		style : "padding-top:5px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
		anchor : '100%',
		items : [{ // 价格
			columnWidth : 0.46,
			layout : "form",
			border : false,
			items : [txtPrice,hidePrice]
		}, {	// 检索码
					columnWidth : 0.46,
					layout : "form",
					border : false,
					items : [txtRetrieveCode]
				}]
	});
	// 第四行
	var fourLine = new Ext.Panel({
		border : false,
		layout : "column",
		style : "padding-top:5px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
		anchor : '100%',
		items : [{ // 配料说明
			columnWidth : 0.97,
			layout : "form",
			border : false,
			items : [txaMenuMemo]
		}]
	});
	// 第五行
	var fiveLine = new Ext.Panel({
		border : false,
		layout : "column",
		style : "padding-top:5px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
		anchor : '100%',
		items : [{
					columnWidth : 1,
					layout : "form",
					border : false,
					items : [hiddenlastModifiedDate, hiddenId]
				}]
	});
	// panel
	var menuPanel = new Ext.FormPanel({
		        frame : true,
				labelAlign : 'right',
				labelWidth : 80,
				items : [firstLine, secondLine, fourLine,fiveLine]
			});
			
			
	// 弹出窗口
	var win = new Ext.Window({
				width : 500,
				height : 300,
				modal : true,
				buttonAlign : "center",
				resizable : false,
				items : [menuPanel],
				title : '',
				buttons : [{
							// 保存按钮
							text : Constants.BTN_SAVE,
							iconCls : Constants.CLS_SAVE,
							handler : confirmRecord
						}, {
							// 取消按钮
							text : Constants.BTN_CANCEL,
							iconCls : Constants.CLS_CANCEL,
							handler : function() {
							 Ext.Msg.confirm(Constants.NOTICE_CONFIRM,
							   MessageConstants.COM_C_005,
				               function(buttonobj) {
					           if (buttonobj == 'yes') {
						       win.hide();
					}
				})
							}
						}],
				layout : 'fit',
				closeAction : 'hide'
			});
	// 按菜谱类别查询
    function searchByMenu() {  	
		var menuTypeText = drpMenuType.getValue();
		storeMenu.baseParams = {
			menuTypeValue : menuTypeText
		};
		storeMenu.load({
				params : {
					start : 0,
					limit : Constants.PAGE_SIZE
				}
			});
	};	
	// 编辑处理
	function editHandler() {
		var rec = gridMenu.getSelectionModel().getSelected();
		if (!rec) {
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG, MessageConstants.COM_I_001);
			return false;
		} else {
			flag = "0";
			menuPanel.getForm().reset();
			win.setTitle("修改菜谱");
			win.show();	
			Ext.get("menutypeCode").dom.readOnly = true;
			menuPanel.getForm().loadRecord(rec);
			Ext.get('menutypeCode').dom.value = Ext.get('drpMenuTypeCode').dom.value;
			Ext.get('menuName').dom.value = rec.get('menuName') == null
					? ""
					: rec.data.menuName;
			Ext.get('menuPrice').dom.value = divide2(rec.get('menuPrice')); 
			
			Ext.get('retrieveCode').dom.value = rec.get('retrieveCode') == null
					? ""
					: rec.data.retrieveCode;
			Ext.get('menuMemo').dom.value = rec.get('menuMemo') == null
					? ""
					: rec.data.menuMemo;					
			Ext.get('updateTime').dom.value = rec.get('updateTime') == null
					? ""
					: rec.data.updateTime;
			Ext.get('id').dom.value = rec.get('id');			
		}		
	}
	// 增加处理
	function addHandler() {
		flag = '1'
		menuPanel.getForm().reset();
		win.setTitle("新增菜谱");
		win.show();
		Ext.get('menutypeCode').dom.value = Ext.get('drpMenuTypeCode').dom.value;
		Ext.get("menutypeCode").dom.readOnly = true;
		Ext.get("updateTime").dom.value = "2009-10-10T10:10:10";
	}
	// 删除处理
	function deleteHandler() {
		var menuTypeText = drpMenuType.getValue();
		// 判断是否已选择了数据
		if (gridMenu.selModel.hasSelection()) {
		var record = gridMenu.getSelectionModel().getSelected();
		//向后台传数据
		var menuIdText = record.get("id");
		var updateTime = (record.get('updateTime') == null
					? ""
					: record.get('updateTime')); 
		Ext.Msg.confirm(Constants.NOTICE_CONFIRM, MessageConstants.COM_C_002,
		    function(buttonObj) {
		      if (buttonObj == "yes") {
		         Ext.Ajax.request({
                    method : Constants.POST,
                    url : 'administration/deleteMenu.action', 
                    params : {
						menuIdValue : menuIdText,
						updateTime : updateTime
						},                           
		            success : function(result, request) {
                            var o = eval("(" + result.responseText
											+ ")");
							// 排他
							if (o.msg == Constants.DATA_USING) {
									Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
										MessageConstants.COM_I_002);
							}
							// SQL异常
							if (o.msg == Constants.SQL_FAILURE) {
									Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
										MessageConstants.COM_E_014);
							}
					  //显示成功
		              Ext.Msg.alert(
		                MessageConstants.SYS_REMIND_MSG,
		                MessageConstants.DEL_SUCCESS,
		                function(){
		                	storeMenu.baseParams = {
			                    menuTypeValue : menuTypeText
		                     };
		                	storeMenu.load({
		                    params : {
		                           start : 0,
		                           limit : Constants.PAGE_SIZE
		                      }
		                 });
		                })
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
    // 判断数据是否为空
    function checkform() {
		// 判断数据是否为空
		if ( txtMenuName.getValue() == "" ) {
		msg = String.format(MessageConstants.COM_E_002,"菜谱名称");
		Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG, msg);
		return false;
		}
		return true;
	}
    // 保存button压下的操作
	function confirmRecord() {
	 if(isNotNull()){
		var menuTypeText = drpMenuType.getValue();
		Ext.Msg.confirm(Constants.NOTICE_CONFIRM, MessageConstants.COM_C_001,
				function(buttonobj) {
					if (buttonobj == 'yes') {
						if (flag == "1") {
						   recordAdd(menuTypeText);
						}
						if (flag == "0") {
						   recordEdit(menuTypeText);
						}
					}
				});
			}
	}
	//货币渲染2位小数位数
    function divide2(v, argDecimal) {
    	if(v == 0){return '0.00 元'}
		if (v) {
			if (typeof argDecimal != 'number') {
				argDecimal = 2;
			}
			v = Number(v).toFixed(argDecimal);
			var t = '';
			v = String(v);
			while ((t = v.replace(/^(-?[0-9]+)([0-9]{3}.*)$/, '$1,$2')) !== v)
				v = t;
			
			return v + ' 元';
		} else
			return '';
	}
	// 非空check
    function isNotNull() {
    	var msg = "";
    	// 判断数据是否为空
    	if ( txtMenuName.getValue() == "" ) {
    		msg += String.format(MessageConstants.COM_E_002,"菜谱名称");
    		Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, msg);
    		return false;
    	}
    	return true;
    }
    // 保存按钮后的增加处理
    function recordAdd(menuTypeText){   
	   if (checkform()) {
		  var myurl = 'administration/addMenu.action';
		  //设置隐藏域价格值
		  hidePrice.setValue(txtPrice.getValue());
		  menuPanel.getForm().submit({
			method : Constants.POST,
			url : myurl,
			params : {
				menuTypeValue:menuTypeText
				},
			success : function(form, action) {
			var result = eval('('
					+ action.response.responseText
				+ ')');
			// 显示成功
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
						MessageConstants.COM_I_004,function(){
							storeMenu.baseParams = {
			                   menuTypeValue : menuTypeText
		                    };
						 	storeMenu.load({
					          params : {
							     start : 0,
							     limit : Constants.PAGE_SIZE
							    }
							  });
					        gridMenu.getView().refresh();
							win.hide();
						 });
			},
			failure : function() {}
			});				
    }
    }
    // 保存按钮后的修改处理
    function recordEdit(menuTypeText){ 
	   if (checkform()) {
			var myurl = 'administration/updateMenu.action';
			hidePrice.setValue(txtPrice.getValue());
			menuPanel.getForm().submit({
						method : Constants.POST,
						url : myurl,
						params : {
							 menuTypeValue:menuTypeText
							},
						success : function(form, action) {
						    var result = eval('('
									+ action.response.responseText
									+ ')');
							// 排他
							if (result.msg == Constants.DATA_USING) {
									Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
										MessageConstants.COM_I_002);
							}
							// SQL异常
							if (result.msg == Constants.SQL_FAILURE) {
									Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
										MessageConstants.COM_E_014);
							}
							// 显示成功信息
							Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
									MessageConstants.COM_I_004,function(){
									storeMenu.baseParams = {
			                           menuTypeValue : menuTypeText
		                             };
									  storeMenu.load({
										 params : {
											start : 0,
											limit : Constants.PAGE_SIZE
											}
									  });
									  gridMenu.getView().refresh();
									  win.hide();
									});									
						},
						failure : function() {}
							});
		} else {
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG, msg);
				return;
			}						
    } 
    
});