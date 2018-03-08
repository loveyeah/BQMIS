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
	var opCode;
	var workorderStatus = getParameter("workorderStatus");

	/** 窗口元素↓↓* */

	var datalist = new Ext.data.Record.create([{
				name : 'planDescription'
			}, {
				name : 'id'
			}, {
				name : 'factOpDuration'
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
				url : 'workbill/getEquJOrderstepList.action?woCode=' + woCode,
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
	var northGrid = new Ext.grid.GridPanel({
				ds : northGrids,
				columns : [northGridsm, new Ext.grid.RowNumberer(), {
					header : "工序名",
					sortable : false,
					dataIndex : 'planOperationStepTitle'
						// ,
						// editor : factOperationStepTitle
					}, {
					header : "描述",
					sortable : false,
					dataIndex : 'planDescription'
						// ,
						// editor : factDescription
					}, {
					header : "工时",
					sortable : false,
					dataIndex : 'factOpDuration'
				}, {
					header : "验证点",
					sortable : false,
					dataIndex : 'planPointName'
				}, {
					header : "排序号",
					width : 100,
					sortable : false,
					dataIndex : 'orderby'
				}, {
					header : "主键",
					width : 100,
					sortable : false,
					hidden : true,
					dataIndex : 'id'
				}],
				sm : northGridsm,
				tbar : [],
				frame : true,
				border : true,
				clicksToEdit : 1,// 单击一次编辑
				enableColumnHide : false,
				enableColumnMove : false,
				iconCls : 'icon-grid'
			});

	northGrid.on('rowclick', function(grid, rowIndex, e) {
				if (northGrid.getSelectionModel().hasSelection()) {
					opCode = northGrid.getSelectionModel().getSelected()
							.get("operationStep");
					if (northGrid.getSelectionModel().getSelected() != null
							&& northGrid.getSelectionModel().getSelections().length < 2
							&& woCode != null && opCode != null
							&& woCode != "undefined" && opCode != "undefined") {
						southTabPanel.setActiveTab(0);
						iframe0.location = "spstepman.jsp?woCode=" + woCode
								+ "&opCode=" + opCode;
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
					if (woCode != null && opCode != null
							&& woCode != "undefined" && opCode != "undefined") {
						if (getCode("woCode", iframe0.location) != woCode
								|| getCode("opCode", iframe0.location) != opCode)
							iframe0.location = "spstepman.jsp?woCode=" + woCode
									+ "&opCode=" + opCode;
					}
				}
			},
			html : '<iframe id="iframe0" src="" style="width:100%;height:100%;border:0px;"></iframe>'
		}, {
			title : '服务',
			id : 'tab3',
			listeners : {
				activate : function() {
					if (woCode != null && opCode != null
							&& woCode != "undefined" && opCode != "undefined") {
						if (getCode("woCode", iframe3.location) != woCode
								|| getCode("opCode", iframe3.location) != opCode)
							iframe3.location = "spstepserv.jsp?woCode="
									+ woCode + "&opCode=" + opCode;
					}
				}
			},
			html : '<iframe id="iframe3" src="" style="width:100%;height:100%;border:0px;"></iframe>'
		}, {
			title : '工具',
			id : 'tab1',
			listeners : {
				activate : function() {
					if (woCode != null && opCode != null
							&& woCode != "undefined" && opCode != "undefined")
						if (getCode("woCode", iframe1.location) != woCode
								|| getCode("opCode", iframe1.location) != opCode)
							iframe1.location = "spsteptools.jsp?woCode="
									+ woCode + "&opCode=" + opCode;

				}
			},
			html : '<iframe id="iframe1" style="width:100%;height:100%;border:0px;"></iframe>'

		}, {
			title : '材料',
			id : 'tab2',
			listeners : {
				activate : function() {
					if (woCode != null && opCode != null
							&& woCode != "undefined" && opCode != "undefined")
						if (getCode("woCode", iframe2.location) != woCode
								|| getCode("opCode", iframe2.location) != opCode)
							iframe2.location = "spstepmat.jsp?woCode=" + woCode
									+ "&opCode=" + opCode;
				}
			},
			html : '<iframe id="iframe2" src="" style="width:100%;height:100%;border:0px;"></iframe>'
		}]
	});
	southTabPanel.remove('tab2');
	var getcode = getParameter("woCode");

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
										woCode : getcode
									},

									success : function(form, action) {
										blockForm.getForm().reset();
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
										blockForm.getForm().reset();
										blockAddWindow.hide();
										ds.reload();
									},
									failure : function(form, action) {
										var o = eval('('
												+ action.response.responseText
												+ ')');
										Ext.MessageBox.alert('错误', o.eMsg);
									}
								});
							} else {
								Ext.MessageBox.alert('错误', '未定义的操作');
							}
					}
				}, {
					text : '取消',
					handler : function() {
						blockForm.getForm().reset();
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
					blockForm.getForm().reset();

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
									url : 'workbill/deleteEquJStepdocuments.action',
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

					ds.load({
								params : {
									start : 0,
									limit : 18,
									code : getcode
								}
							});
				}
			});
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
				url : 'workbill/getEquJStepdocumentsList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : datalist
			});
	ds.load({
				params : {
					code : getcode
				}
			});

	// 列表
	var centergrid = new Ext.grid.GridPanel({
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

						if (val != "" && val != null) {
							return "<a style=\"cursor:hand;color:red\" onClick=\"window.open('"
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
		// height : 280,
		border : true,
		enableColumnHide : false,
		enableColumnMove : false,
		iconCls : 'icon-grid'
	});

	// var i = 0;
	//
	// var southPanel = new Ext.form.FormPanel({
	// title : "相关包文件",
	// uploadFile : true,
	// height : 280,
	// frame : true,
	// border : true,
	// labelAlign : 'right',
	// labelWidth : 50,
	// autoScroll : true,
	// layout:'form',
	// id : 'southPanel',
	// // layout : 'column',
	// fileUpload : true,
	// items : [
	//
	// new Ext.form.TextField({
	//
	// id : 'form-file' + i,
	// width : 450,
	// // readOnly:true,
	//
	// emptyText : 'Select an image',
	// fieldLabel : '附件',
	//
	// inputType : 'file',
	// name : 'file[' + i + ']'
	//
	// }), new Ext.Button({
	// id : 'btn' + i,
	// text : '删除',
	// handler : function() {
	// var _panel = this.ownerCt;
	// _panel.remove(Ext.getCmp("form-file"
	// + this.id.substring(this.id.length - 1)));
	// _panel.remove(Ext.getCmp(this.id));
	// _panel.doLayout();
	//
	// // alert(this.id.substring(this.id.length-1))
	//
	// }
	// })
	//
	// // {xtype : 'fileuploadfield',
	// // id:'here',
	// // width: 400
	// // }
	//
	// ],
	// buttons : [{
	// text : '增加附件',
	// handler : function() {
	// if (southPanel.getForm().isValid()) {
	// var _panel = this.ownerCt;
	//
	// i++;
	// var _textfield = new Ext.form.TextField({
	// xtype : 'textfield',
	// id : 'form-file' + i,
	// width : 450,
	// // readOnly : true,
	//
	// emptyText : 'Select an image',
	// fieldLabel : '附件',
	//
	// inputType : 'file',
	// name : 'file[' + i + ']'
	//
	// });
	// var _delbutton = new Ext.Button({
	// id : 'btn' + i,
	// text : '删除',
	// handler : function() {
	// _panel.remove(Ext.getCmp("form-file"
	// + this.id.substring(this.id.length - 1)));
	// _panel.remove(Ext.getCmp(this.id));
	// _panel.doLayout();
	//
	// // alert(this.id.substring(this.id.length-1))
	//
	// }
	// })
	//
	// // var textfield=new Ext.form.Fileuploadfield(
	// // {
	// // // xtype : 'fileuploadfield',
	// // id:'here',
	// // width: 400
	// // });
	// //
	// _panel.add(_textfield);
	// _panel.add(_delbutton);
	// _panel.doLayout();
	//
	// }
	// }
	// }, {
	// text : '上传',
	// handler : function() {
	// if (southPanel.getForm().isValid()) {
	// var _panel = this.ownerCt;
	// _panel.remove(Ext.getCmp("form-file1"));
	// _panel.doLayout();
	//
	// }
	// }
	// }
	// // ,
	// // {
	// // text : '删除',
	// // handler : function() {
	// // southPanel.getForm().reset();
	// // }
	// // }
	// ]
	//
	// });

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
							split : false,
							collapsible : false,
							items : [northGrid]
						}, {
							// columnWidth : .5,
							region : 'center',
							layout : 'border',

							items : [{
										title : "",
										region : 'center',
										height : 500,
										// width:200,
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
										layout : 'fit',
										// border : false,
										margins : '1',
										items : [centergrid]
									}]

						}]
			});
		// if (woCode==null||(workorderStatu!='1') ){
		//
		// btnCancel.setDisabled(true);
		// btnSave.setDisabled(true)
		//
		// centerbtnAdd.setDisabled(true);
		// centerbtnUpdate.setDisabled(true);
		// centerbtnDel.setDisabled(true);
		// centerbtnref.setDisabled(true);
		// centerbtnDelFiles.setDisabled(true);
		//
		// }

});