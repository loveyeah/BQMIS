Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {

	// 增加按钮
	var addBtn = new Ext.Button({
				text : Constants.BTN_ADD,
				iconCls : Constants.CLS_ADD,
				handler : addHandler
			});

	// 修改按钮
	var updateBtn = new Ext.Button({
				text : Constants.BTN_UPDATE,
				iconCls : Constants.CLS_UPDATE,
				handler : updateHandler
			});

	// 删除按钮
	var deleteBtn = new Ext.Button({
				text : Constants.BTN_DELETE,
				iconCls : Constants.CLS_DELETE,
				handler : deleteHandler
			});

	// 查询 add by bjxu
	var queryBtn = new Ext.Button({
				text : '查询',
				iconCls : 'query',
				handler : query
			})

	// 定义grid中的数据
	var gridData = new Ext.data.Record.create([{
				// 序号
				name : 'id'
			}, {
				// 人员编码
				name : 'workerCode'
			}, {
				// 姓名
				name : 'workerName'
			}]);

	var gridStore = new Ext.data.JsonStore({
				url : 'administration/getPersonSignInfo.action',
				root : 'list',
				totalProperty : 'totalCount',
				sortInfo : {
					field : "workerCode",
					direction : "ASC"
				},
				fields : gridData
			});

	var queryName = new Ext.form.TextField({
				id : 'workerName',
				fieldLabel : '姓名',
				blankText : '员工姓名',
				width : 90
			})
gridStore.on('beforeload', function() {
		Ext.apply(this.baseParams, {
			queryName : queryName.getValue()
		})
	});

	// 加载数据
	gridStore.load({
		params : {
			start : 0,
			limit : Constants.PAGE_SIZE
		}
	});

	function query() {
		gridStore.reload()
	}
	// 一览grid
	var grid = new Ext.grid.GridPanel({
				autoWidth : true,
				store : gridStore,
				region : 'center',
				sm : new Ext.grid.RowSelectionModel({
							// 单选
							singleSelect : true
						}),
				columns : [new Ext.grid.RowNumberer({
									header : '行号',
									width : 50
								}), {
							header : '工号',
							width : 100,
							sortable : true,
							dataIndex : 'workerCode'
						}, {
							header : '姓名',
							width : 100,
							sortable : true,
							dataIndex : 'workerName'
						}],
				tbar : [addBtn, updateBtn, deleteBtn, '->', '员工姓名:', queryName,
						queryBtn],
				bbar : new Ext.PagingToolbar({
							pageSize : Constants.PAGE_SIZE,
							store : gridStore,
							displayInfo : true,
							displayMsg : MessageConstants.DISPLAY_MSG,
							emptyMsg : MessageConstants.EMPTY_MSG
						}),
				frame : false,
				border : false,
				enableColumnHide : true,
				enableColumnMove : false
			});

	// 行双击事件
	grid.on("rowdblclick", updateHandler);

	// 设定布局器及面板
	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				items : [grid]
			});

	// *********** 弹出窗口 开始 **********//

	// 新增/修改标识
	var flag = {
		id : 'flag',
		xtype : 'hidden'
	}

	// id
	var id = new Ext.form.TextField({
				id : 'id',
				name : 'id',
				hideLabel : true,
				hidden : true
			});

	// 工号
	var workerCodeTxt = new Ext.form.TextField({
				id : 'workerCode',
				name : 'workerCode',
				readOnly : true,
				fieldLabel : '工号<font color="red">*</font>',
				allowBlank : false,
				anchor : "90%"
			});

	// 工号单击事件
	workerCodeTxt.onClick(workerSelect);

	// 姓名
	var workerNameTxt = new Ext.form.TextField({
				id : 'workerName',
				readOnly : true,
				fieldLabel : '姓名<font color="red">*</font>',
				allowBlank : false,
				anchor : "90%"
			});

	// 图片选择
	var picSelectTxt = new Ext.form.TextField({
				id : "picSelect",
				name : "picSelect",
				height : 21,
				inputType : 'file',
				fieldLabel : '选择图片<font color="red">*</font>',
				anchor : "90%",
				initEvents : function() {
					Ext.form.TextField.prototype.initEvents.apply(this,
							arguments);
					var keydown = function(e) {
						e.stopEvent();
					};
					this.el.on("keydown", keydown, this);
				}
			});

	// 个性签名图片
	var signPicTxt = new Ext.form.TextField({
		id : 'signPic',
		fieldLabel : '个性签名图片<br/>(bmp格式)',
		autoCreate : {
			tag : 'input',
			src : '',
			type : 'image',
			style : 'filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale);'
		},
		height : 150,
		inputType : 'image',
		anchor : "90%"
	});

	// 弹出窗口Form
	var formPanel = new Ext.FormPanel({
				frame : true,
				fileUpload : true,
				height : 320,
				labelAlign : 'right',
				items : [flag, id, workerCodeTxt, workerNameTxt, picSelectTxt,
						signPicTxt],
				listeners : {
					render : formInitial
				}
			});

	// 弹出窗口
	var win = new Ext.Window({
				width : 350,
				height : 330,
				modal : true,
				title : '新增/修改',
				buttonAlign : 'center',
				layout : 'fit',
				items : [formPanel],
				closeAction : 'hide',
				resizable : false,
				buttons : [{
							text : Constants.BTN_UPLOAD,
							buttonAlign : 'center',
							iconCls : Constants.CLS_UPLOAD,
							handler : uploadHandler
						}, {
							text : Constants.BTN_CANCEL,
							iconCls : Constants.CLS_CANCEL,
							buttonAlign : 'center',
							handler : function() {
								Ext.Msg.confirm(
										MessageConstants.SYS_CONFIRM_MSG,
										MessageConstants.COM_C_005, function(
												buttonobj) {
											// 确认关闭弹出窗口
											if (buttonobj == "yes") {
												win.hide();
											}
										});
							}
						}],
				listeners : {
					hide : hideHandler
				}
			});

	// *********** 弹出窗口 结束 **********//

	/**
	 * 新增处理
	 */
	function addHandler() {
		win.setTitle("新增个性签名");
		win.show();
		workerCodeTxt.setDisabled(false);
		Ext.get("flag").dom.value = "A";
	}

	/**
	 * 修改处理
	 */
	function updateHandler() {
		var selected = grid.selModel.getSelections();
		if (selected.length < 1) {
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
					MessageConstants.COM_I_001);
		} else {
			var record = selected[0].data;
			// alert(Ext.encode(selected[0].data))
			// return
			win.setTitle("修改个性签名");
			win.show();
			workerCodeTxt.setValue(record.workerCode);
			workerNameTxt.setValue(record.workerName);
			picSelectTxt.setValue()
			Ext.get("id").dom.value = record.id;
			Ext.get("flag").dom.value = "U";
			Ext.get("signPic").dom.src = "administration/getPersonSignPicture.action?id="
					+ record.id + "&time=" + new Date().getTime();
		}
	}

	/**
	 * 删除处理
	 */
	function deleteHandler() {
		var selected = grid.selModel.getSelections();
		if (selected.length < 1) {
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
					MessageConstants.COM_I_001);
		} else {
			Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG,
					MessageConstants.COM_C_002, function(buttonobj) {
						// 确认保存
						if (buttonobj == "yes") {
							Ext.lib.Ajax.request(Constants.POST,
									'administration/deleteSignPicture.action',
									{
										success : function(result, request) {
											if (result.responseText) {
												var o = eval("("
														+ result.responseText
														+ ")");
												var succ = o.msg;
												if (succ == Constants.SQL_FAILURE) {
													Ext.Msg
															.alert(
																	MessageConstants.SYS_ERROR_MSG,
																	MessageConstants.COM_E_014);
												} else {
													Ext.Msg
															.alert(
																	MessageConstants.SYS_REMIND_MSG,
																	MessageConstants.COM_I_005);
												}
											}

											gridStore.load({
														params : {
															start : 0,
															limit : Constants.PAGE_SIZE
														}
													});
										},
										failure : function() {
											Ext.Msg
													.alert(
															MessageConstants.SYS_ERROR_MSG,
															MessageConstants.DEL_ERROR);
										}
									}, 'id=' + selected[0].data.id);
						}
					});
		}
	}

	/**
	 * 调用共通选择人员
	 */
	function workerSelect() {
		if (Ext.get("flag").dom.value == "A") {
			var args = new Object();
			args.selectModel = "single";
			args.rootNode = {
				id : '0',
				text : '合肥电厂'
			};
			var object = window
					.showModalDialog(
							'../../../../comm/jsp/hr/workerByDept/workerByDept.jsp',
							args,
							'dialogWidth=800px;dialogHeight=520px;center=yes;help=no;resizable=no;status=no;');
			if (object) {
				if (typeof(object.workerCode) != "undefined") {
					workerCodeTxt.setValue(object.workerCode);
				}
				if (typeof(object.workerName) != "undefined") {
					workerNameTxt.setValue(object.workerName);
				}
			}
		}
	}

	/**
	 * 上传处理
	 */
	function uploadHandler() {
		// form是否验证通过
		if (checkForm() && checkBmp()) {
			var flag = Ext.get("flag").dom.value;
			// 新增
			if (flag == "A") {
				addSignPic();
				// 修改
			} else if (flag == "U") {
				updateSignPic();
			}
		}
	}

	/**
	 * 新增个性签名
	 */
	function addSignPic() {
		Ext.lib.Ajax.request(Constants.POST,
				'administration/getSignPictureRepeat.action', {
					success : function(result, request) {
						if (result.responseText) {
							var o = eval("(" + result.responseText + ")");
							var succ = o.msg;
							if (succ == true) {
								Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
										MessageConstants.AC001_E_002);
							} else if (succ == false) {
								Ext.Msg.confirm(
										MessageConstants.SYS_CONFIRM_MSG,
										MessageConstants.AC001_C_002, function(
												buttonobj) {
											// 确认保存
											if (buttonobj == "yes") {
												formPanel.getForm().submit({
													method : Constants.POST,
													url : 'administration/addSignPicture.action',
													success : function(form,
															action) {
														if (action.response.responseText) {
															var o = eval("("
																	+ action.response.responseText
																	+ ")");
															var succ = o.msg;
															if (succ == Constants.SQL_FAILURE) {
																Ext.Msg
																		.alert(
																				MessageConstants.SYS_ERROR_MSG,
																				MessageConstants.COM_E_014);
															} else if (succ == Constants.IO_FAILURE) {
																Ext.Msg
																		.alert(
																				MessageConstants.SYS_ERROR_MSG,
																				MessageConstants.COM_E_022);
															} else if (succ == Constants.FILE_NOT_EXIST) {
																Ext.Msg
																		.alert(
																				MessageConstants.SYS_ERROR_MSG,
																				MessageConstants.COM_E_024);
															} else {
																Ext.Msg
																		.alert(
																				MessageConstants.SYS_REMIND_MSG,
																				MessageConstants.COM_I_004,
																				function() {
																					// 隐藏弹出窗口
																					win
																							.hide();
																					// 重新查询数据
																					gridStore
																							.load(
																									{
																										params : {
																											start : 0,
																											limit : Constants.PAGE_SIZE
																										}
																									});
																				});
															}
														}
													},
													failure : function() {
														Ext.Msg
																.alert(
																		MessageConstants.SYS_ERROR_MSG,
																		MessageConstants.UNKNOWN_ERR);
													}
												});
											}
										});
							} else if (succ == Constants.SQL_FAILURE) {
								Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
										MessageConstants.COM_E_014);
							}
						}
					},
					failure : function() {
						Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
								MessageConstants.UNKNOWN_ERR);
					}
				}, 'workerCode=' + workerCodeTxt.getValue());

	}

	/**
	 * 修改个性签名
	 */
	function updateSignPic() {
		Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG,
				MessageConstants.AC001_C_001, function(buttonobj) {
					// 确认保存
					if (buttonobj == "yes") {
						formPanel.getForm().submit({
							method : Constants.POST,
							url : 'administration/updateSignPicture.action',
							success : function(form, action) {
								if (action.response.responseText) {
									var o = eval("("
											+ action.response.responseText
											+ ")");
									var succ = o.msg;
									if (succ == Constants.SQL_FAILURE) {
										Ext.Msg.alert(
												MessageConstants.SYS_ERROR_MSG,
												MessageConstants.COM_E_014);
									} else if (succ == Constants.IO_FAILURE) {
										Ext.Msg.alert(
												MessageConstants.SYS_ERROR_MSG,
												MessageConstants.COM_E_022);
									} else if (succ == Constants.FILE_NOT_EXIST) {
										Ext.Msg.alert(
												MessageConstants.SYS_ERROR_MSG,
												MessageConstants.COM_E_024);
									} else {
										Ext.Msg
												.alert(
														MessageConstants.SYS_REMIND_MSG,
														MessageConstants.COM_I_004,
														function() {
															// 隐藏弹出窗口
															win.hide();
															// 重新查询数据
															gridStore.load({
																params : {
																	start : 0,
																	limit : Constants.PAGE_SIZE
																}
															});
														});
									}
								}
							},
							failure : function() {
								Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
										MessageConstants.UNKNOWN_ERR);
							}
						});
					}
				});
	}

	/**
	 * Form初始化处理
	 */
	function formInitial() {
		// 图片选择后
		formPanel.getForm().findField('picSelect').on('render', function() {
			Ext.get('picSelect').on('change',
					function(field, newValue, oldValue) {
						var url = Ext.get('picSelect').dom.value;
						var image = Ext.get('signPic').dom;
						if (checkBmp()) {
							var flag = Ext.get("flag").dom.value;
							if (Ext.isIE7) {
								image.src = Ext.BLANK_IMAGE_URL;
								// 覆盖原来的图片
								image.filters
										.item("DXImageTransform.Microsoft.AlphaImageLoader").src = url;
							} else {
								image.src = url;
							}
						}
					});
		});
	}

	/**
	 * 检查图片格式是否为bmp、jpeg、gif、png、jpg
	 */
	function checkBmp() {
		var url = (Ext.get('picSelect').dom.value).toString();
		if (url == "") {
			return true;
		} else {
			var format = url.substring(url.lastIndexOf(".") + 1, url.length);
			if (format == "bmp" || format == "jpeg" || format == "gif"
					|| format == "png" || format == "jpg") {
				return true;
			} else {
				Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG, String.format(
								MessageConstants.AC001_E_001, '个性签名'));
				return false;
			}
		}
	}

	/**
	 * 清空图片信息
	 */
	function removeImage() {
		if (picSelectTxt.el && picSelectTxt.el.dom) {
			var imagephotoDom = signPicTxt.el.dom;
			var imagephotoClone = imagephotoDom.cloneNode();
			var prt = imagephotoDom.parentNode;
			prt.removeChild(imagephotoDom);
			prt.appendChild(imagephotoClone);

			signPicTxt.applyToMarkup(imagephotoClone);

			// 清除附件内容
			var domAppend = picSelectTxt.el.dom;
			var parent = domAppend.parentNode;

			// 保存
			var domForSave = domAppend.cloneNode();
			// 移除附件控件
			parent.removeChild(domAppend);
			// 再追加控件
			parent.appendChild(domForSave);
			// 应用该控件
			picSelectTxt.applyToMarkup(domForSave);
		}
		Ext.get("signPic").dom.src = "";
	}

	/**
	 * 检查Form是否验证通过
	 */
	function checkForm() {
		var msg = "";
		// 工号不可以为空
		if ((!workerCodeTxt.getValue())) {
			msg += String.format(MessageConstants.COM_E_003, '工号');
			Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, msg);
			return false;
		}

		// 姓名不可以为空
		if ((!workerNameTxt.getValue())) {
			msg += String.format(MessageConstants.COM_E_003, '姓名');
			Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, msg);
			return false;
		}

		// 选择图片不可以为空
		if ((picSelectTxt.getValue() == "" && Ext.get("flag").dom.value != "U")) {
			msg += String.format(MessageConstants.COM_E_002, '选择图片');
			Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, msg);
			return false;
		}

		return true;
	}

	/**
	 * 弹出窗口关闭时，清空Form中的信息
	 */
	function hideHandler() {
		formPanel.getForm().reset();
		removeImage();
	}
});