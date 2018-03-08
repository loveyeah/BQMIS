// 画面：物资出门登记
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {
	// 主画面

	var strMethod;
	var strMsg;
	// 新增按钮
	var btnAdd = new Ext.Button({
				id : 'add',
				text : Constants.BTN_ADD,
				iconCls : Constants.CLS_ADD,
				handler : addRecord
			});
	// 修改按钮
	var btnUpdate = new Ext.Button({
				id : 'update',
				text : Constants.BTN_UPDATE,
				iconCls : Constants.CLS_UPDATE,
				handler : updateRecord
			});
	// 删除按钮
	var btnDelete = new Ext.Button({
				id : 'delete',
				text : Constants.BTN_DELETE,
				iconCls : Constants.CLS_DELETE,
				handler : deleteRecord
			});
	// 隐藏姓名
	var hiddenName = new Ext.form.Hidden({
				id : 'hiddName',
				value : '0'
			});
	// 隐藏部门
	var hiddenDep = new Ext.form.Hidden({
				id : 'hiddDep',
				value : '0'
			});
	// 隐藏申请人编码
	var hiddenMan = new Ext.form.Hidden({
				id : 'hiddMan',
				value : '0'
			});
	// 取得登陆用户信息
	Ext.Ajax.request({
				url : "administration/getUserInfo.action",
				method : "post",
				success : function(result, request) {
					var data = eval("(" + result.responseText + ")");
					hiddenMan.setValue(data.userCode);
					hiddenName.setValue(data.userName);
					hiddenDep.setValue(data.depName);
				}
			});
	// grid中的数据
	var recordInfoGridList = new Ext.data.Record.create([{
				name : 'insertBy'
			}, {
				name : 'firm'
			}, {
				name : 'insertDate'
			}, {
				name : 'chsName'
			}, {
				name : 'updateTime'
			}, {
				name : 'id'
			}, {
				name : 'paperId'
			}, {
				name : 'visitedDep'
			}, {
				name : 'inDate'
			}, {
				name : 'outDate'
			}, {
				name : 'note'
			}, {
				name : 'onDuty'
			}, {
				name : 'papertypeCd'
			}, {
				name : 'visitedMan'
			},{
				name : 'deptName'
			}]);

	// grid中的store
	var storeInfoGrid = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'administration/getVisitInfoList.action'
						}),
				reader : new Ext.data.JsonReader({
							root : 'list',
							totalProperty : 'totalCount'
						}, recordInfoGridList)
			});
	// 初始化时,显示所有数据
	storeInfoGrid.load({
				params : {
					start : 0,
					limit : Constants.PAGE_SIZE
				}
			});

	// grid中的列
	var cm = new Ext.grid.ColumnModel([
			// 自动生成行号
			new Ext.grid.RowNumberer({
						header : '行号',
						width : 35
					}), {
				header : '来访人',
				width : 120,
				align : 'left',
				sortable : true,
				dataIndex : 'insertBy'
			}, {
				header : '来访人单位',
				width : 180,
				align : 'left',
				sortable : true,
				dataIndex : 'firm'
			}, {
				header : '来访时间',
				width : 120,
				align : 'left',
				sortable : true,
				renderer : renderDate,
				dataIndex : 'insertDate'
			}, {
				header : '被访人',
				width : 120,
				align : 'left',
				sortable : true,
				dataIndex : 'chsName'
			}])
	// 定义选择列
	var sm = new Ext.grid.RowSelectionModel({
				singleSelect : true
			});

	// 运行执行的Grid主体
	var gridInfo = new Ext.grid.GridPanel({
				store : storeInfoGrid,
				cm : cm,
				viewConfig : {
					forceFit : false
				},
				tbar : [btnAdd, btnUpdate, btnDelete],
				// 分页
				bbar : new Ext.PagingToolbar({
							pageSize : Constants.PAGE_SIZE,
							store : storeInfoGrid,
							displayInfo : true,
							displayMsg : MessageConstants.DISPLAY_MSG,
							emptyMsg : MessageConstants.EMPTY_MSG
						}),
				sm : sm,
				frame : false,
				border : false,
				enableColumnHide : true,
				enableColumnMove : false
			});

	// 注册双击事件
	gridInfo.on("rowdblclick", updateRecord);

	// 设定布局器及面板
	new Ext.Viewport({
				enableTabScroll : true,
				layout : "fit",
				items : [gridInfo]
			});

	// 弹出窗口

	// 来访人
	var txtInsertBy = new Ext.form.TextField({
				id : 'insertBy',
				fieldLabel : "来访人<font color='red'>*</font>",
				isFormField : true,
				allowBlank : false,
				maxLength : 5,
				anchor : '100%',
				name : 'adJManpass.insertby'
			});
	// 被访人部门
	var txtDepName = new Ext.form.TextField({
				id : 'visitedDep',
				fieldLabel : "被访人部门<font color='red'>*</font>",
				isFormField : true,
				maxLength : 50,
				readOnly : true,
				allowBlank : false,
				anchor : '100%',
				name : 'visiteddep'
			});
	// 隐藏部门
	var hiddenDepName = new Ext.form.Hidden({
				id : 'hiddenVisitedDep',
				name : 'adJManpass.visiteddep'
			});
	txtDepName.onClick(selectDep);
	// 第一行
	var firstLine = new Ext.Panel({
		border : false,
		layout : 'column',
		style : "padding-top:5px;padding-right:5px;padding-bottom:0px;margin-bottom:0px",
		anchor : '100%',
		items : [{
					columnWidth : 0.5,
					layout : "form",
					border : false,
					items : [hiddenDepName, txtInsertBy]
				}, {
					columnWidth : 0.5,
					layout : "form",
					border : false,
					items : [txtDepName]
				}]
	});
	// 来访时间
	var dteInit = getCurrentDate();
	var txtInsertDate = new Ext.form.TextField({
				id : 'insertDate',
				fieldLabel : "来访时间",
				name : 'insertdate',
				style : 'cursor:pointer',
				anchor : '100%',
				readOnly : true,
				renderer : renderDate,
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d 00:00',
									dateFmt : 'yyyy-MM-dd HH:mm'
								});
					}
				}
			})
	txtInsertDate.setValue(dteInit);
	// 被访人
	var drpVisitedMan = new Ext.form.CmbWorkerByDept({
				id : "visitedMan",
				name : "visitedman",
				fieldLabel : "被访人<font color='red'>*</font>",
				allowBlank : false,
				maxLength : 12,
				anchor : '100%'
			});
	// 隐藏被访人
	var hiddenInsert = new Ext.form.Hidden({
				id : 'hiddenVisitedman',
				name : 'adJManpass.visitedman'
			});

	// 第二行
	var secondLine = new Ext.Panel({
		border : false,
		layout : 'column',
		style : "padding-top:5px;padding-right:5px;padding-bottom:0px;margin-bottom:0px",
		anchor : '100%',
		items : [{
					columnWidth : 0.5,
					layout : 'form',
					border : false,
					items : [hiddenInsert, txtInsertDate]
				}, {
					columnWidth : 0.5,
					layout : 'form',
					border : false,
					items : [drpVisitedMan]
				}]
	});
	// 证件类别
	var txtPapertypeCd = new Ext.form.CmbPaperType({
				id : "papertypeCd",
				name : "papertypeCd",
				fieldLabel : "证件类别",
				allowBlank : true,
				maxLength : 25,
				anchor : '100%'
			})
	// 隐藏证件类别
	var hiddenPapertypeCd = new Ext.form.Hidden({
				id : 'hiddenPapertypeCd',
				name : 'adJManpass.papertypeCd'
			});
	txtPapertypeCd.store.load();

	// 进厂时间
	var dteInit = getCurrentDate();
	var txtinDate = new Ext.form.TextField({
				id : 'inDate',
				fieldLabel : "进厂时间",
				name : 'inDate',
				style : 'cursor:pointer',
				anchor : '100%',
				readOnly : true,
				renderer : renderDate,
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d 00:00',
									dateFmt : 'yyyy-MM-dd HH:mm'
								});
					}
				}
			})
	txtinDate.setValue(dteInit);
	// 第三行
	var thirdLine = new Ext.Panel({
		border : false,
		layout : 'column',
		style : "padding-top:5px;padding-right:5px;padding-bottom:0px;margin-bottom:0px",
		anchor : '100%',
		items : [{
					columnWidth : 0.5,
					layout : 'form',
					border : false,
					items : [txtPapertypeCd,hiddenPapertypeCd]
				}, {
					columnWidth : 0.5,
					layout : 'form',
					border : false,
					items : [txtinDate]
				}]
	});
	// 证件号
	var txtPaperId = new Ext.form.TextField({
				id : 'paperId',
				fieldLabel : "证件号",
				isFormField : true,
				maxLength : 50,
				anchor : '100%',
				name : 'adJManpass.paperId'
			});
	// 出厂时间
	var dteInit = getCurrentDate();
	var txtOutDate = new Ext.form.TextField({
				id : 'outDate',
				fieldLabel : "出厂时间",
				name : 'outDate',
				style : 'cursor:pointer',
				anchor : '100%',
				readOnly : true,
				renderer : renderDate,
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d 00:00',
									dateFmt : 'yyyy-MM-dd HH:mm'
								});
					}
				}
			})
//	txtOutDate.setValue(dteInit);
	// 第四行
	var forthLine = new Ext.Panel({
		border : false,
		layout : 'column',
		style : "padding-top:5px;padding-right:5px;padding-bottom:0px;margin-bottom:0px",
		anchor : '100%',
		items : [{
					columnWidth : .5,
					layout : 'form',
					border : false,
					items : [txtPaperId]
				}, {
					columnWidth : .5,
					layout : 'form',
					border : false,
					items : [txtOutDate]
				}]
	});
	// 来访人单位
	var txtFirm = new Ext.form.TextField({
				id : 'firm',
				fieldLabel : '来访人单位',
				maxLength : 25,
				anchor : '100%',
				name : 'adJManpass.firm'
			});
	// 值班人
	var txtOnDuty = new Ext.form.TextField({
				id : 'onDuty',
				fieldLabel : '值班人',
				maxLength : 10,
				anchor : '100%',
				name : 'adJManpass.onduty'
			});
	// 第五行
	var fiveLine = new Ext.Panel({
		border : false,
		layout : 'column',
		style : "padding-top:5px;padding-right:5px;padding-bottom:0px;margin-bottom:0px",
		anchor : '100%',
		items : [{
					columnWidth : .5,
					layout : 'form',
					border : false,
					items : [txtFirm]
				}, {
					columnWidth : .5,
					layout : 'form',
					border : false,
					items : [txtOnDuty]
				}]
	});
	// 备注
	var txtNote = new Ext.form.TextArea({
				id : 'note',
				fieldLabel : '备注',
				maxLength : 627,
				anchor : '99%',
				name : 'adJManpass.note'
			});
	// 第六行
	var sixLine = new Ext.Panel({
		border : false,
		layout : 'column',
		style : "padding-top:5px;padding-right:4px;padding-bottom:0px;margin-bottom:0px",
		anchor : '100%',
		items : [{
					columnWidth : 1,
					layout : 'form',
					border : false,
					items : [txtNote]
				}]
	});
	// 隐藏的上次修改时间
	var hiddenlastModifiedDate = {
		id : 'updateTime',
		xtype : 'hidden',
		readOnly : true,
		hidden : true,
		name : 'adJManpass.updateTime'
	};
	// 隐藏序号
	var hiddenId = {
		id : 'id',
		xtype : 'hidden',
		readOnly : true,
		hidden : true,
		name : 'adJManpass.id'
	};
	// 第六行
	var sevenLine = new Ext.Panel({
		border : false,
		layout : "column",
		style : "padding-top:5px;padding-right:4px;padding-bottom:0px;margin-bottom:0px",
		anchor : '100%',
		items : [{
					columnWidth : 1,
					layout : "form",
					border : false,
					items : [hiddenlastModifiedDate, hiddenId]
				}]
	});
	// 定义弹出窗体中的form
	var myPanel = new Ext.FormPanel({
				labelAlign : 'right',
				autoHeight : true,
				frame : true,
				items : [firstLine, secondLine, thirdLine, forthLine, fiveLine,
						sixLine, sevenLine]
			});

	// 定义弹出窗体
	var win = new Ext.Window({
				width : 500,
				autoHeight : true,
				buttonAlign : 'center',
				items : [myPanel],
				layout : 'fit',
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
							handler : function() {
								Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG,
										MessageConstants.COM_C_005, function(
												buttonobj) {
											if (buttonobj == 'yes') {
												win.hide();
											} else {
												return
											}
										})

							}
						}]
			});

	// 处理
	/**
	 * 增加函数
	 */
	function addRecord() {
		strMethod = "add";
		myPanel.getForm().reset();
		txtOnDuty.setValue(hiddenName.getValue());
		win.setTitle("新增来访人员登记");
		win.show();
	}

	/**
	 * 保存button压下的操作
	 */
	function confirmRecord() {
		// check是否为空
		if (!checkform()) {
			Ext.Msg.alert(Constants.ERROR, strMsg);
			return;
		}
		// check 时间
		if (!checkDate()) {			
			return;
		}
		
		hiddenPapertypeCd.setValue(txtPapertypeCd.getValue());
		Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG, MessageConstants.COM_C_001,
				function(buttonobj) {
					if (buttonobj == 'yes') {
						// check 字段过长
						if (isNotLong()) {						
							var myurl = "";
							// 增加的情况下
							if (strMethod == "add") {
							   hiddenInsert.setValue(drpVisitedMan.getValue());		                  
								myurl = 'administration/addVisitData.action';							
								myPanel.getForm().submit({
									method : Constants.POST,
									url : myurl,
									params : {
										insertdate :Ext.get('insertdate').dom.value,
										inDate :Ext.get('inDate').dom.value,
										outDate :Ext.get('outDate').dom.value
									},
									success : function(form, action) {
										var result = eval('('
												+ action.response.responseText
												+ ')');
										// SQL异常
										if (result.msg == Constants.SQL_FAILURE) {
											Ext.Msg.alert(Constants.ERROR,
													MessageConstants.COM_E_014);
											return;
										}
										// 显示成功信息
										Ext.Msg
												.alert(
														MessageConstants.SYS_REMIND_MSG,
														MessageConstants.COM_I_004,
														function() {
															// 重新载入
															storeInfoGrid.load(
																	{
																		params : {
																			start : 0,
																			limit : Constants.PAGE_SIZE
																		}
																	});
															// 页面初始化
															gridInfo.getView()
																	.refresh();
															win.hide();
														});
									},
									failure : function() {
								
									}
								});
								return;
							}
							// 修改时的情况
							if (strMethod == "update") {
								hiddenInsert.setValue(drpVisitedMan.getValue());
								myurl = 'administration/updateVisitData.action';
								myPanel.getForm().submit({
									method : Constants.POST,
									url : myurl,
									params : {
										insertdate :Ext.get('insertdate').dom.value,
										inDate :Ext.get('inDate').dom.value,
										outDate :Ext.get('outDate').dom.value
									},
									success : function(form, action) {
										var result = eval('('
												+ action.response.responseText
												+ ')');
										// 排他
										if (result.msg == Constants.DATA_USING) {
											Ext.Msg.alert(Constants.ERROR,
													MessageConstants.COM_I_002);
											return;
										}
										// SQL异常
										if (result.msg == Constants.SQL_FAILURE) {
											Ext.Msg.alert(Constants.ERROR,
													MessageConstants.COM_E_014);
											return;
										}
										// 显示成功信息
										Ext.Msg
												.alert(
														MessageConstants.SYS_REMIND_MSG,
														MessageConstants.COM_I_004,
														function() {
															// 重新载入
															storeInfoGrid.load(
																	{
																		params : {
																			start : 0,
																			limit : Constants.PAGE_SIZE
																		}
																	});
															// 页面初始化
															gridInfo.getView()
																	.refresh();
															win.hide();
														});
									},
									failure : function() {								
									}
								});

								return;
							}
						} else {
							return;
						}
					}
				});
	}

	/**
	 * 在增加数据前的非空检查
	 */
	function checkform() {
		strMsg = '';
		var insertBy = Ext.get('insertby').dom.value;
		if (insertBy == "" || insertBy == null) {
			strMsg += String.format(MessageConstants.COM_E_002, '来访人');					
			return false;
		}

		var visitedDep = Ext.get('visitedDep').dom.value;
		if (visitedDep == "" || visitedDep == null) {
			strMsg += String.format(MessageConstants.COM_E_002, '被访人部门');				
			return false;
		}

		var visitedMan = Ext.get('visitedMan').dom.value;
		if (visitedMan == "" || visitedMan == null) {
			strMsg += String.format(MessageConstants.COM_E_002, '被访人');
				
			return false;
		}

		if (strMsg == "") {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 修改函数
	 */
	function updateRecord() {
		var rec = gridInfo.getSelectionModel().getSelected();
		if (!rec) {
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
					MessageConstants.COM_I_001);
			return false;
		} else {
			strMethod = "update";
			myPanel.getForm().reset();
			win.setTitle("修改来访人员登记");
			win.show();
			Ext.get('insertBy').dom.value = (rec.get('insertBy') == null
					? ""
					: rec.data.insertBy);	
			Ext.get('insertDate').dom.value = (rec.get('insertDate') == null
					? ""
					: renderDate(rec.data.insertDate));
			Ext.get('inDate').dom.value = (rec.get('inDate') == null
					? ""
					: renderDate(rec.data.inDate));
			Ext.get('paperId').dom.value = (rec.get('paperId') == null
					? ""
					: rec.data.paperId);
			Ext.get('outDate').dom.value = (rec.get('outDate') == null
					? ""
					: renderDate(rec.data.outDate));
			Ext.get('firm').dom.value = (rec.get('firm') == null
					? ""
					: rec.data.firm);
			Ext.get('onDuty').dom.value = (rec.get('onDuty') == null
					? ""
					: rec.data.onDuty);
			Ext.get('note').dom.value = (rec.get('note') == null
					? ""
					: rec.data.note);
			Ext.get('updateTime').dom.value = (rec.get('updateTime') == null
					? ""
					: rec.data.updateTime);
			Ext.get('id').dom.value = rec.get('id');
			txtPapertypeCd.setValue(rec.get("papertypeCd"));			
			if (txtPapertypeCd.getValue() == Ext.get('papertypeCd').dom.value) {				
				txtPapertypeCd.setValue("");
				Ext.get('papertypeCd').dom.value = "";
			}
			drpVisitedMan.setValue(rec.get("chsName"));
			txtDepName.setValue(rec.get("deptName"));
			hiddenDepName.setValue(rec.get("visitedDep"));
			hiddenInsert.setValue(rec.get("visitedMan"));
		}
	}
	/**
	 * 删除函数
	 */
	function deleteRecord() {
		var rec = gridInfo.getSelectionModel().getSelected();
		if (rec) {
			var id = rec.get('id');
			var updateTime = rec.get('updateTime');
			Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG, MessageConstants.COM_C_002,
					function(buttonobj) {
						if (buttonobj == 'yes') {
							// 刪除
							Ext.Ajax.request({
								method : Constants.POST,
								url : 'administration/deleteVisitData.action',
								params : {
									id : id,
									updateTime : updateTime
								},
								success : function(result, request) {
									var o = eval("(" + result.responseText
											+ ")");
									// 排他
									if (o.msg == 'U') {
										Ext.Msg.alert(Constants.ERROR,
												MessageConstants.COM_I_002);
									} else {
										// 显示成功信息
										Ext.Msg
												.alert(
														MessageConstants.SYS_REMIND_MSG,
														MessageConstants.COM_I_005,
														function() {
															// 重新载入数据
															storeInfoGrid.load(
																	{
																		params : {
																			start : 0,
																			limit : Constants.PAGE_SIZE
																		}
																	});
															// 画面初期化
															gridInfo.getView()
																	.refresh();
														});
									}
								},
								failure : function() {
									Ext.Msg.alert(Constants.ERROR,
											MessageConstants.COM_E_014);
								}
							});
						}
					});
		} else {
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
					MessageConstants.COM_I_001);
		}
	}

	/**
	 * 获取当前时间
	 */
	function getCurrentDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate();
		s += (t > 9 ? "" : "0") + t + " ";
		t = d.getHours();
		s += (t > 9 ? "" : "0") + t + ":";
		t = d.getMinutes();
		s += (t > 9 ? "" : "0") + t;
		return s;
	}
	/**
	 * 去掉时间中T和秒
	 */
	function renderDate(value) {
		if (!value)
			return "";
		var reDate = /\d{4}\-\d{2}\-\d{2}/gi;
		var reTime = /\d{2}:\d{2}/gi;

		var strDate = value.match(reDate);
		var strTime = value.match(reTime);
		if (!strDate)
			return "";
		return strTime ? strDate + " " + strTime : strDate;
	}

	// 判断入力框是否过长
	function isNotLong() {
		 if (check(Ext.get('insertBy').dom.value.RTrim()) > 10
		 || check(Ext.get('paperId').dom.value.RTrim()) > 50
		 || check(Ext.get('firm').dom.value.RTrim()) > 50
		 || check(Ext.get('onDuty').dom.value.RTrim()) > 20
		 || check(Ext.get('note').dom.value.RTrim()) > 1225) {
		 return false;
		 }
		return true;
	}
	// 去掉右空格
	String.prototype.RTrim = function() {
		return this.replace(/(\s*$)/g, "");
	};
	// check字段的长度return 字符串单字节的长度
	function check(str) {
		var reg = /[^\x00-\xff]/
		var s = 0;
		var ts;
		for (i = 0; i < str.length; i++) {
			ts = str.substring(i, i + 1);
			if (reg.test(ts)) {
				s = s + 2;
			} else {
				s = s + 1;
			}
		}
		return s;
	}
	// 选择部门处理函数
	function selectDep() {
		var args = new Object();
		args.selectModel = "single";
		args.rootNode = {
			// modify by liuyi 091027 
//			id : '-1',
			id : '0',
			text : '灞桥热电厂'
		};
        args.onlyLeaf = true;
		var object = window
				.showModalDialog(
						'../../../../comm/jsp/hr/dept/dept.jsp',
						args,
						'dialogWidth=800px;dialogHeight=520px;center=yes;help=no;resizable=no;status=no;');
		// 根据返回值设置画面的值
		if (object) {
			Ext.get('visitedMan').dom.value = "";
			if (typeof(object.names) != "undefined") {
				txtDepName.setValue(object.names);
				hiddenDepName.setValue(object.codes);
			}
			if (typeof(object.codes) != "undefined") {			
				drpVisitedMan.store.load({
							params : {
								strDeptCode : object.codes
							}
						});
			}
		}
	}
	/**
	 * 
	 * 时间的有效性检查
	 */
	function checkDate() {
		var effectiveDate = Ext.get('inDate').dom.value;
		var dateStart = Date.parseDate(effectiveDate, 'Y-m-d');
        
		var discontinueDate = Ext.get('outDate').dom.value;
		var dateEnd = Date.parseDate(discontinueDate, 'Y-m-d');
        
		if (!(effectiveDate == "" || effectiveDate == null)
				&& !(discontinueDate == "" || discontinueDate == null)
				&& (effectiveDate > discontinueDate)) {
			Ext.Msg.alert(Constants.ERROR, String.format(
							MessageConstants.COM_E_009, "进厂时间", "出厂时间"));
			return false;
		} else {
			return true;
		}
	}
});