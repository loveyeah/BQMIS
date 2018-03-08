Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {
	// 定义数据格式
	var MyRecord = Ext.data.Record.create([{
				name : 'clientTypeId'
			}, {
				name : 'typeName'
			}]);
	// 定义获取数据源
	var dataProxy = new Ext.data.HttpProxy({
				url : 'resource/getVendorType.action'
			});

	// 定义格式化数据方式
	var theReader = new Ext.data.JsonReader({
				root : "list",
				totalProperty : "totalCount"
			}, MyRecord);

	// 定义封装缓存数据的对象
	var store = new Ext.data.Store({
				proxy : dataProxy,
				reader : theReader,
				sortInfo : {
					field : "clientTypeId",
					direction : "ASC"
				}

			});
	// 加载store
	store.load({
				params : {
					start : 0,
					limit : Constants.PAGE_SIZE
				}
			});
	// 定义gird
	var grid = new Ext.grid.GridPanel({
				region : "center",
				layout : 'fit',
				store : store,
				columns : [new Ext.grid.RowNumberer({
									header : '行号',
									width : 35
								}), {
							header : "合作伙伴类型",
							dataIndex : 'typeName'

						}, {
							hidden : true,
							dataIndex : "clientTypeId"
						}],
				autoSizeColumns : true,
				sm : new Ext.grid.RowSelectionModel({
							singleSelect : true
						}),
				viewConfig : {
					// 还会对超出的部分进行缩减，让每一列的尺寸适应GRID的宽度大小，阻止水平滚动条的出现
					forceFit : true
				},

				// 头部工具栏
				tbar : [{
							text : "新增",
							iconCls : Constants.CLS_ADD,
							handler : addRecord
						}, '-', {
							text : Constants.BTN_UPDATE,
							iconCls : Constants.CLS_UPDATE,
							handler : updateRecord
						}, '-', {
							text : Constants.BTN_DELETE,
							iconCls : Constants.CLS_DELETE,
							handler : deleteRecord
						}],
				// 底部工具栏
				bbar : new Ext.PagingToolbar({
							pageSize : Constants.PAGE_SIZE,
							store : store,
							displayInfo : true,
							displayMsg : Constants.DISPLAY_MSG,
							emptyMsg : Constants.EMPTY_MSG
						}),

				enableColumnMove : false
			});

	// 定义合作伙伴类型textbox
	var txtVendorType = new Ext.form.TextField({
				fieldLabel : "合作伙伴类型<font color='red'>*</font>",
				width : "100%",
				name : "typeName",
				maxLength : 40,
				allowBlank : false
			})
	var hidVendorTypeId = {
		id : "hidClientTypeId",
		xtype : "hidden",
		name : "clientTypeId"
	}
	// 定义弹出窗口内部控件panel
	var myPanel = new Ext.FormPanel({
				labelAlign : 'top',
				height : 150,
				frame : true,
				items : [txtVendorType, hidVendorTypeId]

			});
	// 定义增加、更新和删除的Action url
	var urlSend = "";
	// 定义“增加”和“修改”弹出窗体
	var win = new Ext.Window({
		width : 400,
		height : 150,
		title : "增加/修改合作伙伴类型",
		buttonAlign : "center",
		items : [myPanel],
		layout : 'fit',
		closeAction : 'hide',
		modal : true,
		resizable : false,
		buttons : [{
			text : Constants.BTN_SAVE,
			iconCls : Constants.CLS_SAVE,
			handler : function() {
				Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_C_001,
						function(buttonobj) {
							if (buttonobj == "yes") {
								// 判断是否为空
								if (Ext.get("typeName").dom.value.trim() == "") {
									Ext.Msg.alert(Constants.SYS_REMIND_MSG,
											String.format(Constants.COM_E_002,
													"合作伙伴类型"));
                                                    
									// 如果判断长度是否小于40
								} else if (Ext.get("typeName").dom.value.trim().length <= 40) {
									send();
								} 
							}
						});
			}

		}, {
			text : Constants.BTN_CANCEL,
			iconCls : Constants.CLS_CANCEL,
			handler : function() {
				win.hide();
			}
		}]
	});

	// 注册双击事件
	grid.on("rowdblclick", updateRecord);

	// 修改合作伙伴类型函数
	function updateRecord() {
		// 是否有被选项
		if (grid.selModel.hasSelection()) {
			var records = grid.selModel.getSelections();
			var recordslen = records.length;
			// 不允许多选，检查选择条数
			if (recordslen > 1) {
				Ext.Msg.alert(Constants.SYS_REMIND_MSG,
						Constants.SELECT_COMPLEX_MSG);
			} else {
				var record = grid.getSelectionModel().getSelected();
				win.show();
				// 重命名弹出窗口名称
				win.setTitle("修改合作伙伴类型");
				urlSend = 'resource/updateVendorType.action';
				// 将被选择的第一条数据加载给form
				myPanel.getForm().loadRecord(record);
				flag = Ext.get("typeName").dom.value;
			}
			// 没有记录被选择，弹出提示框
		} else {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_001);
		}
	}
	// 增加合作伙伴类型函数
	function addRecord() {
		win.show();
		win.setTitle("新增合作伙伴类型");
		urlSend = 'resource/addVendorType.action';
		myPanel.getForm().reset();
	}
	// 删除合作伙伴函数
	function deleteRecord() {
		// 是否有被选项
		if (grid.selModel.hasSelection()) {
			var records = grid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_001);
			} else {
				// 获取被选记录的数据
				var record = grid.getSelectionModel().getSelected();
				// 获取数据中的合作伙伴类型id
				var id = record.data.clientTypeId;
				Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_C_002,
						function(buttonobj) {
							if (buttonobj == "yes") {
								Ext.lib.Ajax.request(Constants.POST,
										'resource/deleteVendorType.action', {
											success : function(action) {
												var result = eval(action.responseText);
												// 如果成功，弹出删除成功
												if (result) {
													Ext.Msg
															.alert(
																	Constants.SYS_REMIND_MSG,
																	Constants.COM_I_005)
													// 成功,则重新加载grid里的数据
												} else {
													// 如果失败，弹出删除失败信息
													Ext.Msg
															.alert(
																	Constants.ERROR,
																	Constants.COM_E_014);
												}

												store.load({
													params : {
														start : 0,
														limit : Constants.PAGE_SIZE
													}
												});
											},
											failure : function() {
												
											}
										}, 'id=' + id);
							}
						});

			}
			// 没有记录被选择，弹出提示框
		} else {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_001);
		}
	}
//	// 去掉右空格
//	String.prototype.RTrim = function() {
//		return this.replace(/(\s*$)/g, "");
//	};

	/**
	 * 取得str的长度
	 */
	function strlen(str) {
		var i;
		var len;
		len = 0;
		for (i = 0; i < str.length; i++) {
			if (str.charCodeAt(i) > 255)
				len += 2;
			else
				len++;
		}
		return len;
	}

	// 发送数据
	function send() {
		Ext.Ajax.request({
					url : urlSend,
					method : Constants.POST,
					params : {
						// 修改记录的id
						id : Ext.get("hidClientTypeId").dom.value,
						vendorType : txtVendorType.getValue().trim()
					},
					success : function(action) {
						var result = eval(action.responseText);
						if (result) {
							// 如果增加成功，弹出保存成功信息
							Ext.Msg.alert(Constants.SYS_REMIND_MSG, String
											.format(Constants.COM_I_004));
							win.hide();
						} else {
							// 失败时，弹出提示
							Ext.Msg.alert(Constants.ERROR, String.format(
											Constants.COM_E_007, "合作伙伴类型"));
						}
						// 更新信息
						store.load({
									params : {
										start : 0,
										limit : Constants.PAGE_SIZE
									}
								});

					},
					// 失败
					failure : function() {
						
					}
				});
	}
	// 页面加载显示数据
	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				items : [grid]
			});

});