Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	Ext.QuickTips.init();

	// 标准工作指令
	var standard_code = {
		id : "jobCode",
		xtype : "textfield",
		fieldLabel : '标准工作指令',
		name : 'standardInfo.jobCode',
		allowBlank : false,
		blankText : '标准工作指令...',
		anchor : '97%'
	};

	// 标准工作指令描述
	var standard_explain = {
		id : "description",
		xtype : "textfield",
		fieldLabel : '描述',
		name : 'standardInfo.description',
		allowBlank : false,
		blankText : '描述...',
		anchor : '97%'
	};

	// -----------定义tree-----------------
	var root = new Ext.tree.AsyncTreeNode({
				id : "0",
				text : "专业树"
			});

	var mytree = new Ext.tree.TreePanel({
				el : "treepanel",
				region : 'center',
				// autoHeight : true,
				height : 900,
				root : root,
				containerScroll : true,
				requestMethod : 'GET',
				loader : new Ext.tree.TreeLoader({
							url : "equstandard/findRunSpecials.action",
							baseParams : {
								id : '0'
							}
						})
			});

	// --------tree的事件-----------------
	mytree.on("dblclick", clickTree, this);
	mytree.on('beforeload', function(node) {
				mytree.loader.dataUrl = 'equstandard/findRunSpecials.action?id='
						+ node.id;
			}, this);

	function clickTree(node) {
		Ext.get("standardInfo.speciality").dom.value = node.id;
		Ext.get("specialityName").dom.value = node.text;
		blockTreeWindow.hide();
	};

	// 左边的弹出窗体
	var blockTreeWindow;
	function showTreeWindow() {
		if (!blockTreeWindow) {
			blockTreeWindow = new Ext.Window({
						// el : 'window_win',
						title : '双击选择专业',
						layout : 'fit',
						width : 220,
						height : 350,
						modal : false,
						closable : true,
						border : false,
						scroll : true,
						resizable : true,
						closeAction : 'hide',
						plain : true,
						// 面板中按钮的排列方式
						buttonAlign : 'center',
						items : [mytree]
					});
		}
		blockTreeWindow.show(Ext.get('getrole'));
	};

	// 专业编码
	var speciality = {
		id : "speciality",
		xtype : "combo",
		fieldLabel : '专业编码',
		name : 'standardInfo.speciality',
		allowBlank : true,
		blankText : '专业编码...',
		anchor : '100%',
		onTriggerClick : function() {
			showTreeWindow();
		}
	};

	// 专业名称
	var speciality_name = {
		id : "specialityName",
		xtype : "textfield",
		fieldLabel : '专业名称',
		name : 'specialityName',
		allowBlank : true,
		readOnly : true,
		blankText : '专业名称...',
		anchor : '95%'
	};

	// 工时
	var job_duration = {
		id : "jopDuration",
		xtype : "numberfield",
		fieldLabel : '工时',
		name : 'standardInfo.jopDuration',
		allowBlank : true,
		blankText : '工时...',
		anchor : '100%'
	};

	// 排序号
	var priority = {
		id : "priority",
		xtype : "numberfield",
		fieldLabel : '排序号',
		name : 'standardInfo.priority',
		allowBlank : true,
		blankText : '排序号...',
		anchor : '66%'
	};

	// 左边弹窗的表单对象
	var blockForm = new Ext.form.FormPanel({
				labelAlign : 'right',
				frame : true,
				items : [{
							border : false,
							layout : 'form',
							items : [{
										border : false,
										layout : 'form',
										columnWidth : 1,
										items : [standard_code]
									}, {
										border : false,
										layout : 'form',
										columnWidth : 1,
										items : [standard_explain]
									}, {
										border : false,
										layout : 'column',
										items : [{
													columnWidth : .4,
													layout : 'form',
													border : false,
													items : [speciality,
															job_duration]
												}, {
													columnWidth : .6,
													layout : 'form',
													border : false,
													items : [speciality_name,
															priority]
												}]
									}]
						}]
			});

	// 左边的弹出窗体
	var blockAddWindow;
	function showAddWindow() {
		if (!blockAddWindow) {
			blockAddWindow = new Ext.Window({
						// el : 'window_win',
						title : '',
						layout : 'fit',
						width : 600,
						height : 200,
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
									} else if (op == "edit") {
										blockForm.getForm().submit({
											waitMsg : '保存中,请稍后...',
											url : "equstandard/editStandardjob.action",
											params : {
												method : "edit",
												id : westgrid
														.getSelectionModel()
														.getSelected()
														.get("standardInfo.jobId")
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
			blockAddWindow.setTitle("新增标准工序");
		} else if (op == "edit") {
			blockAddWindow.setTitle("修改标准工序");
			blockAddWindow.show();
			var rec = westgrid.getSelectionModel().getSelected();
			blockForm.getForm().loadRecord(rec);
			if (Ext.get("standardInfo.jopDuration").dom.value == 'null') {
				Ext.get("standardInfo.jopDuration").dom.value = "";
			}
			if (Ext.get("standardInfo.priority").dom.value == 'null') {
				Ext.get("standardInfo.priority").dom.value = "";
			}
		} else {
		}
		blockAddWindow.show(Ext.get('getrole'));
	};

	// 左边按钮
	// 新建按钮
	var westbtnAdd = new Ext.Button({
				text : '新增',
				iconCls : 'add',
				handler : function() {
					op = "add";
					showAddWindow();
				}
			});

	// 选择判断
	function CKSelectdone() {
		var rec = westgrid.getSelectionModel().getSelections();
		if (rec.length != 1) {
			Ext.Msg.alert('提示信息', "请选择一行！");
			return false;
		} else {
			op = "edit";
			showAddWindow();
		}
	}

	// 修改按钮
	var westbtnedit = new Ext.Button({
				text : '修改',
				iconCls : 'update',
				handler : CKSelectdone
			});

	// 删除按钮
	var westbtndel = new Ext.Button({
		text : '删除',
		iconCls : 'delete',
		handler : function() {
			if (westgrid.getSelectionModel().getSelections().length > 0) {
				Ext.Msg.confirm('提示信息', '确认 删除 选中记录?', function(button, text) {
							if (button == 'yes') {
								var rec = westgrid.getSelectionModel()
										.getSelections();
								var str = "";
								for (var i = 0; i < rec.length; i++) {
									str += rec[i].get("standardInfo.jobId")
											+ ",";
								}
								Ext.Ajax.request({
											waitMsg : '删除中,请稍后...',
											url : 'equstandard/delStandardjob.action',
											params : {
												method : "lock",
												ids : str
											},
											success : function(response,
													options) {
												westgrids.reload();
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

	// 锁定按钮
	var westbtnlock = new Ext.Button({
		text : '锁定',
		iconCls : 'locked',
		handler : function() {
			if (westgrid.getSelectionModel().getSelections().length > 0) {
				Ext.Msg.confirm('提示信息', '确认 锁定 选中记录?', function(button, text) {
							if (button == 'yes') {
								var rec = westgrid.getSelectionModel()
										.getSelections();
								var str = "";
								for (var i = 0; i < rec.length; i++) {
									str += rec[i].get("standardInfo.jobId")
											+ ",";
								}
								Ext.Ajax.request({
											waitMsg : '锁定中,请稍后...',
											url : 'equstandard/lockStandardjob.action',
											params : {
												method : "lock",
												ids : str
											},
											success : function(response,
													options) {
												westgrids.reload();
												Ext.Msg
														.alert('提示信息',
																'锁定记录成功！');
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

	// 解锁按钮
	var westbtnunlock = new Ext.Button({
		text : '解锁',
		iconCls : 'unlocked',
		handler : function() {
			if (westgrid.getSelectionModel().getSelections().length > 0) {
				Ext.Msg.confirm('提示信息', '确认 解锁 选中记录?', function(button, text) {
					if (button == 'yes') {
						var rec = westgrid.getSelectionModel().getSelections();
						var str = "";
						for (var i = 0; i < rec.length; i++) {
							str += rec[i].get("standardInfo.jobId") + ",";
						}
						Ext.Ajax.request({
									waitMsg : '解锁中,请稍后...',
									url : 'equstandard/unlockStandardjob.action',
									params : {
										method : "unlock",
										ids : str
									},
									success : function(response, options) {
										westgrids.reload();
										Ext.Msg.alert('提示信息', '解锁记录成功！');
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
	var westbtnref = new Ext.Button({
				text : '刷新',
				iconCls : 'refresh',
				handler : function() {
					westgrids.load({
								params : {
									start : 0,
									limit : 18
								}
							});
				}
			});
	var westsm = new Ext.grid.CheckboxSelectionModel();
	// 左边列表中的数据
	var datalist = new Ext.data.Record.create([{
				name : 'standardInfo.calnum'
			}, {
				name : 'standardInfo.crewId'
			}, {
				name : 'standardInfo.description'
			}, {
				name : 'standardInfo.downTime'
			}, {
				name : 'standardInfo.interruptable'
			}, {
				name : 'standardInfo.jobCode'
			}, {
				name : 'standardInfo.jobId'
			}, {
				name : 'standardInfo.jopDuration'
			}, {
				name : 'standardInfo.laborCode'
			}, {
				name : 'standardInfo.maintDep'
			}, {
				name : 'standardInfo.priority'
			}, {
				name : 'standardInfo.speciality'
			}, {
				name : 'standardInfo.supervisor'
			}, {
				name : 'standardInfo.status'
			}, {
				name : 'standardInfo.code'
			}, {
				name : 'specialityName'
			}]);

	var westgrids = new Ext.data.JsonStore({
				url : 'equstandard/getStandardjobList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : datalist
			});

	westgrids.load({
				params : {
					start : 0,
					limit : 18
				}
			});

	// 左边列表
	var westgrid = new Ext.grid.GridPanel({
				ds : westgrids,
				columns : [westsm, new Ext.grid.RowNumberer(), {
							header : "状态",
							width : 40,
							align : "center",
							sortable : true,
							renderer : function changeIt(val) {
								if (val == "C") {
									return "正常";
								} else if (val == "L") {
									return "锁定";
								} else if (val == "O") {
									return "注销";
								} else {
									return "状态异常";
								}
							},
							dataIndex : 'standardInfo.status'
						}, {
							header : "标准工序描述",
							width : 180,
							sortable : false,
							dataIndex : 'standardInfo.description'
						}, {
							header : "编码",
							width : 71,
							sortable : true,
							dataIndex : 'standardInfo.jobCode'
						}],
				tbar : [westbtnAdd, {
							xtype : "tbseparator"
						}, westbtnedit, {
							xtype : "tbseparator"
						}, westbtndel, {
							xtype : "tbseparator"
						}, westbtnlock, {
							xtype : "tbseparator"
						}, westbtnunlock, {
							xtype : "tbseparator"
						}, westbtnref],
				sm : westsm,
				frame : true,
				bbar : new Ext.PagingToolbar({
							pageSize : 18,
							store : westgrids,
							displayInfo : true,
							displayMsg : "{0} 到 {1} /共 {2} 条",
							emptyMsg : "没有记录"
						}),
				border : true,
				enableColumnHide : false,
				enableColumnMove : false,
				iconCls : 'icon-grid'
			});

	var sendcode;
	// westgrid 的事件
	westgrid.on('rowdblclick', function(grid, rowIndex, e) {
		sendcode = westgrid.getSelectionModel().getSelected()
				.get("standardInfo.code");
		title = westgrid.getSelectionModel().getSelected()
				.get("standardInfo.description");
		eastab.setActiveTab(0);
		Ext.getCmp('titlepanel').setTitle(title + " 的 详 细 内 容");
		iframe2.location = "equdocument.jsp?code=" + sendcode;
			// iframe0.location = "../business/equjob.jsp?code=" + sendcode;
		})

	// westgrid.on('rowclick', function(grid, rowIndex, e) {
	// getcode = westgrid.getSelectionModel().getSelected()
	// .get("standardInfo.code");
	// if (getcode != sendcode) {
	// sendcode = null;
	// eastab.setActiveTab(0);
	// iframe0.location = "about:blank";
	// iframe1.location = "about:blank";
	// iframe2.location = "about:blank";
	// }
	// })

	// 右边TAB页
	var eastab = new Ext.TabPanel({
		renderTo : document.body,
		activeTab : 0,
		tabPosition : 'bottom',
		plain : true,
		defaults : {
			autoScroll : true
		},
		frame : false,
		border : false,
		items : [{
			title : '标准工作',
			id : 'tab0',
			listeners : {
				activate : function() {
					if (sendcode != null)
						iframe1.location = "standardjob.jsp?code=" + sendcode;
				}
			},
			html : '<iframe id="iframe0" src="" style="width:100%;height:100%;border:0px;"></iframe>'
		}, {
			title : '相关设备',
			id : 'tab1',
			listeners : {
				activate : function() {
					if (sendcode != null)
						iframe1.location = "equbase.jsp?code=" + sendcode;
				}
			},
			html : '<iframe id="iframe1" src="" style="width:100%;height:100%;border:0px;"></iframe>'

		}, {
			title : '相关文件包',
			id : 'tab2',
			// listeners : {
			// activate : function() {
			// if (sendcode != null)
			// iframe2.location = "../business/equdocument.jsp?code="
			// + sendcode;
			// }
			// },
			html : '<iframe id="iframe2" src="" style="width:100%;height:100%;border:0px;"></iframe>'
		}]
	});

	// 设定布局器及面板
	var layout = new Ext.Viewport({
				layout : "border",
				border : false,
				items : [{
							title : "标准包列表",
							region : 'west',
							layout : 'fit',
							border : false,
							margins : '1 0 1 1',
							split : true,
							collapsible : true,
							width : 350,
							items : [westgrid]

						}, {
							title : "双击左侧列表中记录显示",
							id : "titlepanel",
							region : 'center',
							layout : 'fit',
							border : false,
							margins : '1 0 0 0',
							split : true,
							collapsible : false,
							items : [eastab]
						}]
			});
});