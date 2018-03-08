Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	Ext.QuickTips.init();

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

		url = 'workbill/getEquCOrderstepList.action?woCode=' + stdWoCode;
		url1 = 'workbill/getEquCStepdocumentsList.action?code=' + stdWoCode;
	}


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



	// 列表
	var cmWidth = 60;
	var northGrid = new Ext.grid.GridPanel({
				title : '工序列表',
				ds : northGrids,
				columns : [northGridsm, new Ext.grid.RowNumberer(), {
							header : "工序名",
							// width : cmWidth,
							sortable : false,
							dataIndex : 'planOperationStepTitle'
						}, {
							header : "描述",
							// width : cmWidth,
							sortable : false,
							dataIndex : 'planDescription'
						}, {
							header : "工时",
							// width : cmWidth,
							sortable : false,
							dataIndex : 'planOpDuration'
						}, {
							header : "验证点",
							// width : cmWidth,
							sortable : false,
							dataIndex : 'planPointName'
						}, {
							header : "排序号",
							// width : cmWidth,
							sortable : false,
							dataIndex : 'orderby'
						}, {
							header : "主键",
							// width : cmWidth,
							sortable : false,
							hidden : true,
							dataIndex : 'id'
						}],
				sm : northGridsm,
				tbar : [],
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
			// alert(stdWoCode)
			if (stdWoCode == null || stdWoCode == 'null' || stdWoCode == '') {

				opCode = northGrid.getSelectionModel().getSelected()
						.get("operationStep");
				// alert(1)
			} else {
				// alert(2)
				stdopCode = northGrid.getSelectionModel().getSelected()
						.get("operationStep");
				// alert(stdopCode)

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
				width : 300,
				height : 22,
				name : 'baseInfo.fileName'
			});
	var relateFile = new Ext.form.TextArea({
				id : "relateFile",
				fieldLabel : '摘要',
				labelAlign : 'left',
				anchor : "90%",

				name : 'baseInfo.relateFile'
			});
	var filePath = new Ext.form.TextField({
				id : "filePath",
				fieldLabel : '文档路径',
				inputType : 'file',
				anchor : "90%",
				width : 300,
				height : 22,
				name : 'uploadFilePath'
			});

	var blockForm = new Ext.form.FormPanel({
				labelAlign : 'right',
				frame : true,
				fileUpload : true,
				layout : 'form',
				items : [fileName, relateFile, filePath]
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
								blockForm.getForm().submit({
									waitMsg : '保存中,请稍后...',
									url : "workbill/updateEquJStepdocuments.action",
									params : {
										id : rec.data.id,
										filePath : Ext.get("uploadFilePath").dom.value
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
										Ext.MessageBox.alert('错误', o.Msg);
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
					// blockForm.getForm().reset();
					showAddWindow();
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

					showAddWindow();
					var rec = centergrid.getSelectionModel().getSelected();

					// filePath.setValue(rec.data.filePath);

					blockForm.getForm().loadRecord(rec);
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

							return "<a style=\"cursor:hand;color:red\" onClick=\"window.open('/power/upload-file/standard/"
									+ val + "');\"/>查看附件</a>"
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

		tbar : [],
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

//	if (woCode == null || woCode == 'undefined' || woCode == ''
//			|| workorderStatus == '1') {
//
//		Ext.get('btnAdd').dom.disabled = true;
//		Ext.get('btnDelete').dom.disabled = true;
//		Ext.get('btnCancel').dom.disabled = true;
//		Ext.get('btnSave').dom.disabled = true;
//
//		centerbtnAdd.setDisabled(true);
//		centerbtnUpdate.setDisabled(true);
//		centerbtnDel.setDisabled(true);
//		centerbtnref.setDisabled(true);
//		centerbtnDelFiles.setDisabled(true);
//
//	}

})

;