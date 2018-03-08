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

	var getcode = getParameter("code");

	var FileName = new Ext.form.TextField({
				id : "FileName",
				xtype : "textfield",
				inputType : "file",
				fieldLabel : '文 档 路 径',
				anchor : "90%",
				width : 300,
				height : 22,
				name : 'FileName'
			});

	var blockForm = new Ext.form.FormPanel({
				frame : true,
				labelAlign : 'right',
				items : [FileName]
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
						height : 120,
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
											url : "equstandard/addStandardjob.action",
											params : {
												method : "add"
											},
											success : function(form, action) {
												// blockForm.getForm().reset();
												// blockAddWindow.hide();
												// westgrids.reload();
											},
											failure : function(form, action) {
												var o = eval('('
														+ action.response.responseText
														+ ')');
												Ext.MessageBox.alert('错误',
														o.eMsg);
											}
										});
									} else if (op == "edit") {
										blockForm.getForm().submit({
											waitMsg : '保存中,请稍后...',
											url : "equstandard/editStandardjob.action",
											params : {
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
												westgrids.reload();
											},
											failure : function(form, action) {
												var o = eval('('
														+ action.response.responseText
														+ ')');
												Ext.MessageBox.alert('错误',
														o.eMsg);
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
					document.getElementById("Filename").select();
					document.selection.clear();
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
								var str = "";
								for (var i = 0; i < rec.length; i++) {
									str += rec[i].get("realatequInfo.equId")
											+ ",";
								}
								Ext.Ajax.request({
											waitMsg : '删除中,请稍后...',
											url : 'equstandard/delRealequ.action',
											params : {
												ids : str
											},
											success : function(response,
													options) {
												centergrids.reload();
												Ext.Msg
														.alert('提示信息',
																'删除记录成功！');
											},
											failure : function() {
												Ext.Msg.alert('提示信息',
														'服务器错误,请稍候重试!')
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
				iconCls : 'refresh',
				handler : function() {
					centergrids.load({
								params : {
									start : 0,
									limit : 18
								}
							});
				}
			});
	var centersm = new Ext.grid.CheckboxSelectionModel();
	// 左边列表中的数据
	var datalist = new Ext.data.Record.create([{
				name : 'realatequInfo.equId'
			}, {
				name : 'realatequInfo.code'
			}, {
				name : 'realatequInfo.attributeCode'
			}, {
				name : 'realatequInfo.status'
			}, {
				name : 'equname'
			}]);

	var centergrids = new Ext.data.JsonStore({
				url : 'equstandard/getRealequList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : datalist
			});
	centergrids.load({
				params : {
					code : getcode
				}
			});

	// 列表
	var centergrid = new Ext.grid.GridPanel({
				ds : centergrids,
				columns : [centersm, new Ext.grid.RowNumberer(), {
							header : "设备名称",
							width : 180,
							sortable : false,
							dataIndex : 'equname'
						}],
				tbar : [centerbtnAdd, {
							xtype : "tbseparator"
						}, centerbtnDel, {
							xtype : "tbseparator"
						}, centerbtnref],
				sm : centersm,
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