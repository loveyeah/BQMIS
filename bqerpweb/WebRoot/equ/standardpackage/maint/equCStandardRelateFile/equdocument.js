Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	Ext.QuickTips.init();

	var bview = '';
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
//	var filePath = new Ext.form.TextField({
//				id : "filePath",
//				fieldLabel : '文档路径',
//				inputType : 'file',
//				anchor : "90%",
//				width : 300,
//				height : 22
////				,
////				name : 'uploadFilePath'
//			});


	// 附件 内容
	var annex = {
		id : "annex",
		xtype : 'fileuploadfield',
		isFormField:true,
//		name : "annex",
		name : 'uploadFilePath',
		fieldLabel : '附件',
	//	fileUpload : true,
		height : 21,
		width : 240,
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
	
	
	var blockForm = new Ext.form.FormPanel({
				labelAlign : 'right',
				frame : true,
				fileUpload : true,
				layout : 'column',
				items : [{
					layout : 'form',
					columnWidth : 1,
					items : [fileName]
				},{
					layout : 'form',
					columnWidth : 1,
					items : [relateFile]
				},
//					{
//					layout : 'form',
//					columnWidth : 1,
//					items : [filePath]
//				},
					{
					layout : 'form',
					columnWidth : .7,
					items : [annex]
				},{
					layout : 'form',
					columnWidth : .3,
					items : [btnView]
				}]
//				fileName, relateFile, filePath]
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

									url : "equstandard/saveEquCStandardStepdocuments.action",

									params : {
										filePath : Ext.get("uploadFilePath").dom.value,
										woCode : getcode
									},

									success : function(form, action) {
										blockForm.getForm().reset();
										blockAddWindow.hide();
										ds.reload();
										 bview="";
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
									url : "equstandard/updateEquCStandardStepdocuments.action",
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
										 bview="";
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
					bview = '';
					btnView.setVisible(false);
					showAddWindow();
				}
			});

	// 编辑按钮
	var centerbtnUpdate = new Ext.Button({
				text : '修改',
				iconCls : 'update',
				handler : function() {
					op = "update";
					var rec = centergrid.getSelectionModel().getSelected();
					if(rec)//modify by wpzhu
					{
					blockForm.getForm().reset();
					showAddWindow();
					blockForm.getForm().loadRecord(rec);
					}else 
					{
							Ext.Msg.alert("提示", "请选择要修改的记录！");
							return;
					}
					if(rec.get("filePath")!=null&&rec.get("filePath")!="")
		        {
		        bview=rec.get("filePath");
		        bview = '/power/upload_dir/standard/' + bview;
		        btnView.setVisible(true);
		        Ext.get("annex").dom.value = bview.replace('/power/upload_dir/standard/','');
		        }
		        else
		        {
		        	  btnView.setVisible(false);
		        }
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
							url : 'equstandard/deleteEquCStandardStepdocuments.action',
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
	//删除文档
	var centerbtnDelFiles = new Ext.Button({
		text : '删除文档',
		iconCls : 'delete',
		handler : function() {
			if (centergrid.getSelectionModel().getSelections().length > 0) {
				Ext.Msg.confirm('提示信息', '确认 删除 选中记录的文档?', function(button, text) {
					if (button == 'yes') {
						var rec = centergrid.getSelectionModel()
								.getSelections();
						var str = rec[0].get("id");
						for (var i = 1; i < rec.length; i++) {
							str += "," + rec[i].get("id");
						}
						Ext.Ajax.request({
							waitMsg : '删除中,请稍后...',
							url : 'equstandard/deleteEquCStandardStepdocumentsFiles.action',
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
				name : 'filePath'
			}, {
				name : 'id'
			}]);

	var ds = new Ext.data.JsonStore({
				url : 'equstandard/getEquCStandardStepdocumentsList.action',
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
					renderer : function changeIt(val) {
						if (val != "" && val != null) {
//							return "<a style=\"cursor:hand;color:red\" onClick=\"window.open('/power/upload-file/standard/"
							return "<a style=\"cursor:hand;color:red\" onClick=\"window.open('/power/upload_dir/standard/"
									+ val + "');\"/>查看附件</a>"
						} else {
							return "";
						}
					}
				}],

		tbar : [centerbtnAdd, {
					xtype : "tbseparator"
				}, centerbtnUpdate, {
					xtype : "tbseparator"
				}, centerbtnDel, {
					xtype : "tbseparator"
				},  centerbtnDelFiles, {
					xtype : "tbseparator"
				}, centerbtnref],
		sm : sm,
		frame : true,
		border : true,
		enableColumnHide : false,
		enableColumnMove : false,
		iconCls : 'icon-grid'
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
							margins : '0',
							split : false,
							collapsible : false,
							items : [centergrid]
						}]
			});
});