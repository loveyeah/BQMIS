Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	Ext.QuickTips.init();

	var bview;

	function getParameter(psName) {
		var url = self.location;
		var result = "";
		var str = self.location.search.substring(0);

		if (str.indexOf(psName) != -1
				&& (str.substr(str.indexOf(psName) - 1, 1) == "?" || str
						.substr(str.indexOf(psName) - 1, 1) == "&")) {

			if (str.substring(str.indexOf(psName), str.length).indexOf("&") != -1) {
				var Test = str.substring(str.indexOf(psName), str.length);
				result = Test.substr(Test.indexOf(psName), Test.indexOf("&")
								- Test.indexOf(psName));

				result = result.substring(result.indexOf("=") + 1,
						result.length);
			} else {
				result = str.substring(str.indexOf(psName), str.length);
				result = result.substring(result.indexOf("=") + 1,
						result.length);
			}
		}

		return result;

	}

	function getCode(psName, postUrl) {
		var result = "";
		var str = postUrl.search.substring(0);
		if (str.indexOf(psName) != -1
				&& (str.substr(str.indexOf(psName) - 1, 1) == "?" || str
						.substr(str.indexOf(psName) - 1, 1) == "&")) {
			if (str.substring(str.indexOf(psName), str.length).indexOf("&") != -1) {
				var Test = str.substring(str.indexOf(psName), str.length);
				result = Test.substr(Test.indexOf(psName), Test.indexOf("&")
								- Test.indexOf(psName));
				result = result.substring(result.indexOf("=") + 1,
						result.length);
			} else {
				result = str.substring(str.indexOf(psName), str.length);
				result = result.substring(result.indexOf("=") + 1,
						result.length);
			}
		}
		return result;

	}

	var woCode = getParameter("woCode");

	var stdWoCode = getParameter("faWoCode");

	var workorderStatus = getParameter("workorderStatus");

	var opCode = '';
	var stdopCode = '';

	var url = 'workbill/getEquJOrderstepList.action?woCode=' + woCode;
	var url1 = 'workbill/getEquJStepdocumentsList.action?code=' + woCode;

	if (stdWoCode != null && stdWoCode != 'null' && stdWoCode != ''
			&& stdWoCode != 'undefined') {

		// modified by liuyi 20100520
		// url = 'workbill/getEquCOrderstepList.action?woCode=' + stdWoCode;
		// url1 = 'workbill/getEquCStepdocumentsList.action?code=' + stdWoCode;
	}

	/** 弹窗元素↓↓* */

	// 工序名
	var planOperationStepTitle = new Ext.form.TextField({
				id : "planOperationStepTitle",
				// xtype : "textfield",
				fieldLabel : '工序名',
				name : 'baseInfo.planOperationStepTitle',
				allowBlank : false,
				blankText : '工序名...',
				anchor : '99%'
			});

	// 描述
	// 弹出画面

	var memoText = new Ext.form.TextArea({
				id : "memoText",
				maxLength : 128,
				width : 180
			});

	var win = new Ext.Window({
				height : 170,
				width : 350,
				layout : 'fit',
				modal : true,
				resizable : false,
				closeAction : 'hide',
				items : [memoText],
				buttonAlign : "center",
				title : '详细信息录入窗口',
				buttons : [{
					text : '保存',
					iconCls : 'save',
					handler : function() {
						if (!memoText.isValid()) {
							return;
						}
						var record = northGrid.selModel.getSelected();
						record.set("planDescription",
								Ext.get("memoText").dom.value);
						win.hide();
					}
				}, {
					text : '关闭',
					iconCls : 'cancer',
					handler : function() {
						win.hide();
					}
				}]
			});
	var planDescription = new Ext.form.TextArea({
				id : "planDescription",
				// xtype : "textarea",
				fieldLabel : '描述',
				name : 'baseInfo.planDescription',
				allowBlank : true,
				blankText : '描述...',
				anchor : '99%',
				listeners : {
					"render" : function() {
						this.el.on("dblclick", function() {
									var record = northGrid.getSelectionModel()
											.getSelected();
									var value = record.get('planDescription');
									memoText.setValue(value);
									win.x = undefined;
									win.y = undefined;
									win.show();
								})
					}
				}
			});
	// 工时
	var planOpDuration = new Ext.form.NumberField({
				id : "planOpDuration",
				// xtype : "numberfield",
				fieldLabel : '工时',
				name : 'baseInfo.planOpDuration',
				blankText : '工时...',
				anchor : '50%',
				allowBlank : true
			});
	// 验证点
	var planPointName = new Ext.form.TextField({
				id : "planPointName",
				// xtype : "textfield",
				fieldLabel : '验证点',
				name : 'baseInfo.planPointName',
				allowBlank : true,
				blankText : '验证点...',
				anchor : '99%'
			});

	// 排序号
	var orderby = new Ext.form.NumberField({
				id : "orderby",
				// xtype : "numberfield",
				fieldLabel : '排序号',
				name : 'baseInfo.orderby',
				blankText : '排序号...',
				anchor : '50%',
				allowBlank : true
			});

	/** 弹窗元素↑↑* */
	/** 窗口元素↓↓* */
	var datalist = new Ext.data.Record.create([{
				name : 'planDescription'
			}, {
				name : 'id'
			}, {
				name : 'planOpDuration'
			}, {
				name : 'operationStep'
			}, {
				name : 'planOperationStepTitle'
			}, {
				name : 'orderby'
			}, {
				name : 'planPointName'
			}, {
				name : 'woCode'
			}]);

	var northGridsm = new Ext.grid.CheckboxSelectionModel();

	var northGrids = new Ext.data.JsonStore({
				url : url,
				root : 'list',
				totalProperty : 'totalCount',
				fields : datalist
			});

	northGrids.load({
			// params : {
			// start : 0,
			// limit : 18
			// }
			});

	/** northGrid的按钮组↓↓* */

	// 增加
	function addRecord() {
		var count = northGrids.getCount();
		var currentIndex = count;
		var o = new datalist({
					planOperationStepTitle : '',
					planDescription : '',
					planOpDuration : '',
					planPointName : '',
					orderby : '',
					woCode : ''
				});
		northGrid.stopEditing();
		northGrids.insert(currentIndex, o);
		northGridsm.selectRow(currentIndex);
		northGrid.startEditing(currentIndex, 2);
	}

	var btnAdd = new Ext.Button({
				id : 'btnAdd',
				// disabled:true,
				text : "新增",
				iconCls : 'add',
				handler : addRecord
			})

	// add by drdu 091116
	function checkInput() {
		var msg = "";
		if (planOperationStepTitle.getValue() == "") {
			msg = "'工序名'";
		}
		if (msg != "") {
			Ext.Msg.alert("提示", "请输入" + msg);
			return false
		} else {
			return true;
		}
	}

	// 保存
	function saveModifies() {
		northGrid.stopEditing();

		var modifyRec = northGrid.getStore().getModifiedRecords();
		// if (getParameter("faWoCode") != null)
		// modifyRec = northGrids.getRange(0, northGrids.getCount());
		// if (saved == '1')
		// modifyRec = northGrid.getStore().getModifiedRecords();

		if (!checkInput())// add by drdu 091116
			return;
		if (modifyRec.length > 0 || ids.length > 0) {
			Ext.Msg.confirm('提示信息', '确定要保存修改数据吗?', function(button) {
				if (button == 'yes') {
					var updateData = new Array();
					for (var i = 0; i < modifyRec.length; i++) {
						updateData.push(modifyRec[i].data);
					}
					Ext.Ajax.request({
								url : 'workbill/saveEquJOrderstep.action',
								method : 'post',
								params : {
									isUpdate : Ext.util.JSON.encode(updateData),
									isDelete : ids.join(","),
									woCode : getParameter("woCode")
								},
								success : function(result, request) {

									// parent.iframe2.document.all.stdWocode =
									// null;

									var o = eval('(' + result.responseText
											+ ')');

									Ext.MessageBox.alert('提示信息', o.msg);

									// parent.Ext.getCmp("tabPanel").setActiveTab(2);
									// var _url3 =
									// "equ/workbill/plannedCost/plannedCost.jsp";
									// parent.document.all.iframe3.src = _url3
									// + "?woCode=" + getParameter("woCode")
									// + "&workorderStatus=" + workorderStatus
									// + "&faWoCode=" + getParameter("faWoCode")
									// + "&saved=" + 1;

									northGrids.rejectChanges();
									ids = [];
									northGrids.reload();
								},
								failure : function(result, request) {
									Ext.MessageBox.alert('提示信息', '未知错误！')
								}
							})
				}
			})

		} else {
			Ext.MessageBox.alert('提示信息', '没有做任何修改！')
		}
	}

	var btnSave = new Ext.Button({
				id : 'btnSave',
				text : "保存修改",
				iconCls : 'save',
				handler : saveModifies
			})

	// 删除记录
	var ids = new Array();
	function deleteRecords() {
		northGrid.stopEditing();
		var selected = northGrid.getSelectionModel().getSelections();
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i];
				if (member.get("id") != null) {
					ids.push(member.get("id"));
				}
				northGrid.getStore().remove(member);
				northGrid.getStore().getModifiedRecords().remove(member);
			}
		}
	}

	var btnDelete = new Ext.Button({
				text : "删除",
				id : 'btnDelete',
				iconCls : 'delete',
				handler : deleteRecords
			})

	// 取消
	function cancel() {
		var modifyRec = northGrids.getModifiedRecords();
		if (modifyRec.length > 0 || ids.length > 0) {
			Ext.Msg.confirm('提示信息', '确定要放弃修改数据吗?', function(button) {
						if (button == 'yes') {
							northGrids.reload();
							northGrids.rejectChanges();
							ids = [];
						}
					})
		} else {
			northGrids.reload();

		}
	}

	var btnCancel = new Ext.Button({
				id : 'btnCancel',
				text : "取消",
				iconCls : 'cancer',
				handler : cancel
			})
	/** northGrid的按钮组↑↑* */

	// 列表
	var cmWidth = 60;
	var northGrid = new Ext.grid.EditorGridPanel({
				title : '工序列表',
				ds : northGrids,
				columns : [northGridsm, new Ext.grid.RowNumberer(), {
							header : "工序名",
							// width : cmWidth,
							sortable : false,
							dataIndex : 'planOperationStepTitle',
							editor : planOperationStepTitle
						}, {
							header : "描述",
							// width : cmWidth,
							sortable : false,
							dataIndex : 'planDescription',
							editor : planDescription
						}, {
							header : "工时",
							// width : cmWidth,
							sortable : false,
							dataIndex : 'planOpDuration',
							editor : planOpDuration
						}, {
							header : "验证点",
							// width : cmWidth,
							sortable : false,
							dataIndex : 'planPointName',
							editor : planPointName
						}, {
							header : "排序号",
							// width : cmWidth,
							sortable : false,
							dataIndex : 'orderby',
							editor : orderby
						}, {
							header : "主键",
							// width : cmWidth,
							sortable : false,
							hidden : true,
							dataIndex : 'id'
						}],
				sm : northGridsm,
				tbar : [btnAdd, btnDelete, btnCancel, '-', btnSave],
				frame : true,
				border : true,
				clicksToEdit : 1,// 单击一次编辑
				// autoWidth:'true',
				enableColumnHide : false,
				enableColumnMove : false,
				iconCls : 'icon-grid'
			});

	northGrid.on('rowclick', function(grid, rowIndex, e) {
		if (northGrid.getSelectionModel().hasSelection()) {
//			 alert(stdWoCode)
			if (stdWoCode == null || stdWoCode == 'null' || stdWoCode == ''||stdWoCode=='undefined') {

				opCode = northGrid.getSelectionModel().getSelected()
						.get("operationStep");
//				 alert(1)
			} else {
//				 alert(2)
				stdopCode = northGrid.getSelectionModel().getSelected()
						.get("operationStep");
//				 alert(stdopCode)

			}

			if (northGrid.getSelectionModel().getSelected() != null
					&& northGrid.getSelectionModel().getSelections().length < 2
					&& ((

					opCode != null && opCode != "undefined") || (stdopCode != null && stdopCode != "undefined"))) {

				southTabPanel.setActiveTab(0);

				if (getCode("opCode", iframe0.location) != opCode
						|| getCode("stdopCode", iframe0.location) != stdopCode) {

					// alert(opCode)
					iframe0.location = "spstepman.jsp?woCode=" + woCode
							+ "&opCode=" + opCode + "&stdopCode=" + stdopCode
							+ "&stdWoCode=" + stdWoCode + "&workorderStatus="
							+ workorderStatus;

				}
				//					

			} else {
				southTabPanel.setActiveTab(0);
				iframe0.location = "about:blank";
			}
		} else {
			opCode = null;
			southTabPanel.setActiveTab(0);
			iframe0.location = "about:blank";
			iframe3.location = "about:blank";
			iframe1.location = "about:blank";
		}
	});

	// tab
	var southTabPanel = new Ext.TabPanel({
		id : 'southTabPanel',
		activeTab : 0,
		layoutOnTabChange : true,
		tabPosition : 'top',
		plain : true,
		defaults : {
			autoScroll : true
		},
		frame : false,
		border : false,
		items : [{
			title : '人工',
			id : 'tab0',
			listeners : {
				activate : function() {

					if (((opCode != "" && opCode != null) || (stdopCode != "" && stdopCode != null))) {

						if (getCode("opCode", iframe0.location) != opCode
								|| getCode("stdopCode", iframe0.location) != stdopCode) {

							// alert(opCode)
							iframe0.location = "spstepman.jsp?woCode=" + woCode
									+ "&opCode=" + opCode + "&stdopCode="
									+ stdopCode + "&stdWoCode=" + stdWoCode
									+ "&workorderStatus=" + workorderStatus;

						}

					}
				}
			},

			html : '<iframe id="iframe0" src="" style="width:100%;height:100%;border:0px;"></iframe>'
		}, {
			title : '服务',
			id : 'tab3',
			listeners : {
				activate : function() {
					if (((opCode != "" && opCode != null) || (stdopCode != "" && stdopCode != null))) {

						if (getCode("opCode", iframe3.location) != opCode
								|| getCode("stdopCode", iframe3.location) != stdopCode) {

							// alert(opCode)
							iframe3.location = "spstepserv.jsp?woCode="
									+ woCode + "&opCode=" + opCode
									+ "&stdopCode=" + stdopCode + "&stdWoCode="
									+ stdWoCode + "&workorderStatus="
									+ workorderStatus;

						}
					}
				}
			},
			html : '<iframe id="iframe3" src="" style="width:100%;height:100%;border:0px;"></iframe>'
		}, {
			title : '工具',
			id : 'tab1',
			listeners : {
				activate : function() {
					if (((opCode != "" && opCode != null) || (stdopCode != "" && stdopCode != null))) {

						if (getCode("opCode", iframe1.location) != opCode
								|| getCode("stdopCode", iframe1.location) != stdopCode) {

							// alert(opCode)
							iframe1.location = "spsteptools.jsp?woCode="
									+ woCode + "&opCode=" + opCode
									+ "&stdopCode=" + stdopCode + "&stdWoCode="
									+ stdWoCode + "&workorderStatus="
									+ workorderStatus;

						}

					}
				}
			},
			html : '<iframe id="iframe1" style="width:100%;height:100%;border:0px;"></iframe>'

		}, {
			title : '材料',
			id : 'tab2',
			listeners : {
				activate : function() {
					if (((opCode != "" && opCode != null) || (stdopCode != "" && stdopCode != null))) {

						if (getCode("opCode", iframe2.location) != opCode
								|| getCode("stdopCode", iframe2.location) != stdopCode) {

							// alert(opCode)
							iframe2.location = "spstepmat.jsp?woCode=" + woCode
									+ "&opCode=" + opCode + "&stdopCode="
									+ stdopCode + "&stdWoCode=" + stdWoCode
									+ "&workorderStatus=" + workorderStatus;

						}

					}

				}
			},
			html : '<iframe id="iframe2" src="" style="width:100%;height:100%;border:0px;"></iframe>'
		}]
	});
	// 隐藏材料子页面
	southTabPanel.remove('tab2')
	// var getcode = getParameter("woCode");

	var fileName = new Ext.form.TextField({
				id : "fileName",
				fieldLabel : '文档名称',
				anchor : "90%",
				height : 22,
				allowBlank : false,
				name : 'baseInfo.fileName'
			});
	var relateFile = new Ext.form.TextArea({
				id : "relateFile",
				fieldLabel : '摘要',
				labelAlign : 'left',
				anchor : "90%",
				name : 'baseInfo.relateFile'
			});
	// 附件 内容
	var annex = {
		id : "annex",
		xtype : 'fileuploadfield',
		isFormField : true,
		// name : "annex",
		name : 'uploadFilePath',
		fieldLabel : '附件',
		// fileUpload : true,
		height : 21,
		anchor : "95%",
		buttonCfg : {
			text : '浏览...',
			iconCls : 'upload-icon'
		}
	}
	// 查看
	var btnView = new Ext.Button({
				id : 'btnView',
				text : '查看',
				handler : function() {
					window.open(bview);
				}
			});
	btnView.setVisible(false);

	// var filePath = new Ext.form.TextField({
	// id : "filePath",
	// fieldLabel : '文档路径',
	// inputType : 'file',
	// anchor : "90%",
	// width : 300,
	// height : 22,
	// name : 'uploadFilePath'
	// });

	// var blockForm = new Ext.form.FormPanel({
	// labelAlign : 'right',
	// frame : true,
	// fileUpload : true,
	// layout : 'form',
	// items : [fileName, relateFile, filePath]
	// });

	var blockForm = new Ext.FormPanel({
				frame : true,
				labelAlign : 'right',
				labelWidth : 60,
				fileUpload : true,
				title : '',
				layout : 'column',
				items : [{
							layout : 'form',
							columnWidth : 1,
							items : [fileName]
						}, {
							layout : 'form',
							columnWidth : 1,
							items : [relateFile]
						}, {
							layout : 'form',
							columnWidth : 0.7,
							items : [annex]
						}, {
							layout : 'form',
							columnWidth : 0.3,
							items : [btnView]
						}]
			});

	// 弹出窗体
	var blockAddWindow;
	function showAddWindow() {
		if (!blockAddWindow) {
			blockAddWindow = new Ext.Window({
				// el : 'window_win',
				title : '',
				layout : 'fit',
				width : 600,
				// height : 120,
				modal : false,
				closable : true,
				border : false,
				resizable : false,
				closeAction : 'hide',
				plain : true,
				// 面板中按钮的排列方式
				buttonAlign : 'center',
				items : [blockForm],
				buttons : [{
					text : '保存',
					handler : function() {
						if (blockForm.getForm().isValid())
							if (op == "add") {
								if (Ext.get("annex").dom.value == null
										|| Ext.get("annex").dom.value == '') {
									Ext.Msg.alert('提示信息', '附件文档不可为空，请选择！');
									return;
								}
								blockForm.getForm().submit({
									waitMsg : '保存中,请稍后...',

									url : "workbill/saveEquJStepdocuments.action",

									params : {
										filePath : Ext.get("uploadFilePath").dom.value,
										woCode : getParameter("woCode")

									},

									success : function(form, action) {

										var o = eval('('
												+ action.response.responseText
												+ ')');

										Ext.MessageBox.alert('成功', o.msg);

										// blockForm.getForm().reset();
										blockAddWindow.hide();
										ds.reload();
									},
									failure : function(form, action) {
										var o = eval('('
												+ action.response.responseText
												+ ')');
										Ext.MessageBox.alert('错误', o.msg);
									}
								});
							} else if (op == "update") {
								var rec = centergrid.getSelectionModel()
										.getSelected();
								if (Ext.get("annex").dom.value == null
										|| Ext.get("annex").dom.value == '') {
									Ext.Msg.alert('提示信息', '附件文档不可为空，请选择！');
									return;
								}
								blockForm.getForm().submit({
									waitMsg : '保存中,请稍后...',
									url : "workbill/updateEquJStepdocuments.action",
									params : {
										id : rec.data.id,
										filePath : Ext.get("annex").dom.value,
										fileName : Ext.get("fileName").dom.value,
										relateFile : Ext.get("relateFile").dom.value
										// method : "edit",
										// id : westgrid
										// .getSelectionModel()
										// .getSelected()
										// .get("standardInfo.jobId")
									},
									success : function(form, action) {

										var o = eval('('
												+ action.response.responseText
												+ ')');
										Ext.MessageBox.alert('成功', o.msg);
										// blockForm.getForm().reset();
										blockAddWindow.hide();
										ds.reload();
									},
									failure : function(form, action) {
										var o = eval('('
												+ action.response.responseText
												+ ')');
										Ext.MessageBox.alert('错误', o.msg);
									}
								});
							} else {
								Ext.MessageBox.alert('错误', '未定义的操作');
							}
					}
				}, {
					text : '取消',
					handler : function() {
						// blockForm.getForm().reset();
						blockAddWindow.hide();
					}
				}]
			});
		}
		if (op == "add") {
			// 新增时，赋初始值
			blockAddWindow.setTitle("新增文档");
		} else if (op == "edit") {
			blockAddWindow.setTitle("修改文档");
			blockAddWindow.show();
			// var rec = westgrid.getSelectionModel().getSelected();
			// blockForm.getForm().loadRecord(rec);
			// if (Ext.get("standardInfo.jopDuration").dom.value == 'null') {
			// Ext.get("standardInfo.jopDuration").dom.value = "";
			// }
			// if (Ext.get("standardInfo.priority").dom.value == 'null') {
			// Ext.get("standardInfo.priority").dom.value = "";
			// }
		} else {
		}
		blockAddWindow.show(Ext.get('getrole'));
	};

	// 新建按钮
	var centerbtnAdd = new Ext.Button({
				text : '新增',
				iconCls : 'add',
				handler : function() {
					op = "add";
					blockForm.getForm().reset();
					showAddWindow();
					btnView.setVisible(false);
					Ext.get("annex").dom.value = '';
					// document.getElementById("Filename").select();
					// document.selection.clear();
				}
			});

	// 编辑按钮
	var centerbtnUpdate = new Ext.Button({
				text : '修改',
				iconCls : 'update',
				handler : function() {
					op = "update";
					// blockForm.getForm().reset();

					var rec = centergrid.getSelectionModel().getSelected();
					if (rec) {
						// filePath.setValue(rec.data.filePath);
						showAddWindow();
						blockForm.getForm().loadRecord(rec);
						if (rec.get("filePath") != null
								&& rec.get("filePath") != "") {
							bview = rec.get("filePath");
							btnView.setVisible(true);
							Ext.get("annex").dom.value = bview.replace(
									'/power/upload_dir/standard/', '');
						} else {
							Ext.get("annex").dom.value = '';
							btnView.setVisible(false);
						}
					} else {
						Ext.Msg.alert("提示", "请选择一条要修改的记录")
					}
					// document.getElementById("Filename").select();

					// document.selection.clear();
				}
			});
	// 删除按钮
	var centerbtnDel = new Ext.Button({
		text : '删除',
		iconCls : 'delete',
		handler : function() {
			if (centergrid.getSelectionModel().getSelections().length > 0) {
				Ext.Msg.confirm('提示信息', '确认 删除 选中记录?', function(button, text) {
					if (button == 'yes') {
						var rec = centergrid.getSelectionModel()
								.getSelections();
						var str = rec[0].get("id");
						for (var i = 1; i < rec.length; i++) {
							str += "," + rec[i].get("id");
						}
						Ext.Ajax.request({
									waitMsg : '删除中,请稍后...',
									url : 'workbill/deleteEquJStepdocuments.action',
									params : {
										ids : str
									},
									success : function(response, options) {
										ds.reload();
										Ext.Msg.alert('提示信息', '删除记录成功！');
									},
									failure : function() {
										Ext.Msg.alert('提示信息', '服务器错误,请稍候重试!')
									}
								});
					}
				})
			} else {
				Ext.Msg.alert('提示信息', "请至少选择一行！");
				return false;
			}
		}
	});
	// 删除文档
	var centerbtnDelFiles = new Ext.Button({
		text : '删除文档',
		iconCls : 'delete',
		handler : function() {
			if (centergrid.getSelectionModel().getSelections().length > 0) {
				Ext.Msg.confirm('提示信息', '确认 删除 选中记录的文档?',
						function(button, text) {
							if (button == 'yes') {
								var rec = centergrid.getSelectionModel()
										.getSelections();
								var str = rec[0].get("id");
								for (var i = 1; i < rec.length; i++) {
									str += "," + rec[i].get("id");
								}
								Ext.Ajax.request({
									waitMsg : '删除中,请稍后...',
									url : 'workbill/deleteEquJStepdocumentsFiles.action',
									params : {
										ids2 : str
									},
									success : function(response, options) {
										ds.reload();
										Ext.Msg.alert('提示信息', '删除文档成功！');
									},
									failure : function() {
										Ext.Msg.alert('提示信息', '服务器错误,请稍候重试!')
									}
								});
							} else {
								ds.reload();
							}
						})
			} else {
				Ext.Msg.alert('提示信息', "请至少选择一行！");
				return false;
			}
		}
	});
	// 刷新按钮
	var centerbtnref = new Ext.Button({
				text : '刷新',

				iconCls : 'reflesh',
				handler : function() {

					ds.reload(
							// {
							// params : {
							// start : 0,
							// limit : 18,
							// code : woCode
							// }
							// }
							);
				}
			});

	// 保存标准包导入文件
	// function saveAll() {
	//
	// // centerGrid.stopEditing();
	// // var modifyRec = centerGrid.getStore().getModifiedRecords();
	// // if (getParameter("stdWoCode") != null)
	// var modifyRec = ds.getRange(0, ds.getCount());
	// // if (fileSaved == '1')
	// // modifyRec = centerGrid.getStore().getModifiedRecords();
	//
	// if (modifyRec.length > 0 || ids.length > 0) {
	// if (!confirm("确定要保存修改吗?"))
	// return;
	// // var newData = new Array();
	// var updateData = new Array();
	// for (var i = 0; i < modifyRec.length; i++) {
	// // if (modifyRec[i].get("id") == "") {
	// // newData.push(modifyRec[i].data);
	// // } else {
	// updateData.push(modifyRec[i].data);
	// // }
	// }
	//
	// Ext.Ajax.request({
	// url : 'workbill/saveAllfiles.action',
	// method : 'post',
	// params : {
	// // isAdd : Ext.util.JSON.encode(newData),
	// isUpdate : Ext.util.JSON.encode(updateData),
	// woCode : woCode
	// },
	// success : function(result, request) {
	//
	// var o = eval('(' + result.responseText + ')');
	// Ext.MessageBox.alert('提示信息', o.msg);
	//
	// parent.Ext.getCmp("tabPanel").setActiveTab(2);
	// var _url3 = "equ/workbill/plannedCost/plannedCost.jsp";
	// parent.document.all.iframe3.src = _url3
	// + "?woCode=" + getParameter("woCode")
	// + "&workorderStatus=" + workorderStatus
	// + "&faWoCode=" + getParameter("faWoCode")
	// + "&saved=" + getParameter("saved")
	// + "&fileSaved=" + 1;
	//
	// ds.rejectChanges();
	// // ids = [];
	// ds.reload();
	// },
	// failure : function(result, request) {
	// Ext.MessageBox.alert('提示信息', '未知错误！')
	// }
	// })
	// } else {
	// Ext.MessageBox.alert('提示信息', '没有做任何修改！')
	// }
	// }

	var sm = new Ext.grid.CheckboxSelectionModel();
	// 左边列表中的数据
	var datalist = new Ext.data.Record.create([{
				name : 'fileName'
			}, {
				name : 'relateFile'
			}, {
				name : 'fileCode'
			}, {
				name : 'filePath'
			}, {
				name : 'id'
			}]);

	var ds = new Ext.data.JsonStore({
				url : url1,
				root : 'list',
				totalProperty : 'totalCount',
				fields : datalist
			});

	ds.load({

	});

	// 列表
	var centergrid = new Ext.grid.GridPanel({
		title : "相关文件",
		ds : ds,
		columns : [sm, new Ext.grid.RowNumberer({

				}), {
					header : "相关文档列表",
					width : 100,
					align : 'left',
					sortable : false,
					dataIndex : 'fileName'
				}, {
					header : "附件",
					width : 100,
					align : 'left',
					sortable : false,
					dataIndex : 'filePath',
					renderer : function(val) {
						// var val = record.get("fileCode")
						// + record.get("fileType");
						if (val != "" && val != null) {
							// alert(val)
							// modified by liuyi 20100520
							return "<a style=\"cursor:hand;color:red\"onClick=\"window.open('/power/upload_dir/standard/"
									+ val + "');\"/>查看附件</a>"
							// modify by drdu 091116
							// return "<a style=\"cursor:hand;color:red\"
							// onClick=\"window.open('"
							// + val + "');\"/>查看附件</a>"
						} else {
							return "";
						}
					}

				}, {
					header : "摘要",
					width : 100,
					align : 'left',
					sortable : false,
					dataIndex : 'relateFile'
				}],

		tbar : [centerbtnAdd, {
					xtype : "tbseparator"
				}, centerbtnUpdate, {
					xtype : "tbseparator"
				}, centerbtnDel, {
					xtype : "tbseparator"
				}, centerbtnDelFiles, {
					xtype : "tbseparator"
				}, centerbtnref],
		sm : sm,
		frame : true,
		height : 280,
		border : true,
		enableColumnHide : false,
		enableColumnMove : false,
		iconCls : 'icon-grid'
	});

	// 设定布局器及面板
	var layout = new Ext.Viewport({
				// layout : "column",
				layout : "border",
				border : false,
				items : [{
							title : "",
							region : 'west',
							layout : 'fit',
							border : false,
							// margins : '0',
							width : '50%',
							// height : 845,
							// columnWidth : .5,
							split : true,
							collapsible : false,
							items : [northGrid]
						}, {
							// columnWidth : .5,
							region : 'center',
							layout : 'border',
							split : true,

							items : [{
										title : "",
										region : 'center',
										height : 500,
										// width:200,
										split : true,
										layout : 'fit',
										border : false,
										margins : '1',
										items : [southTabPanel]
									}, {
										title : "",
										region : 'south',
										height : 300,
										// width:200,
										frame : true,
										border : true,
										split : true,
										layout : 'fit',
										// border : false,
										margins : '1',
										items : [centergrid]
									}]

						}]
			});

	if (woCode == null || woCode == 'undefined' || woCode == ''
			|| workorderStatus == '1') {

		Ext.get('btnAdd').dom.disabled = true;
		Ext.get('btnDelete').dom.disabled = true;
		Ext.get('btnCancel').dom.disabled = true;
		Ext.get('btnSave').dom.disabled = true;

		centerbtnAdd.setDisabled(true);
		centerbtnUpdate.setDisabled(true);
		centerbtnDel.setDisabled(true);
		centerbtnref.setDisabled(true);
		centerbtnDelFiles.setDisabled(true);

	}

})

;