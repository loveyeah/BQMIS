Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	Ext.QuickTips.init();

	// 名称
	var work_type_name = {
		id : "name",
		xtype : "textfield",
		allowBlank : false,
		fieldLabel : '状态名称',
		name : 'timeworkstatusInfo.name',
		blankText : '状态名称',
		anchor : '97%',
		readOnly : false
	};

	// 排序号
	var orderby = {
		id : "orderby",
		xtype : "textfield",
		allowBlank : true,
		fieldLabel : '排序号',
		name : 'timeworkstatusInfo.orderby',
		blankText : '排序号',
		anchor : '97%',
		readOnly : false
	};

	// 备注
	var memo = {
		id : "memo",
		xtype : "textarea",
		fieldLabel : '备注',
		name : 'timeworkstatusInfo.memo',
		allowBlank : true,
		blankText : '说明...',
		anchor : '96%'
	};

	// 提交框
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
										items : [work_type_name]
									}, {
										border : false,
										layout : 'form',
										columnWidth : 1,
										items : [orderby]
									}, {
										border : false,
										layout : 'form',
										columnWidth : 1,
										items : [memo]
									}]
						}]
			});

	// 弹出窗体
	var blockAddWindow;
	function showAddWindow() {
		if (!blockAddWindow) {
			blockAddWindow = new Ext.Window({
				title : '',
				layout : 'fit',
				width : 300,
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
							if (op == "insert") {
								blockForm.getForm().submit({
									waitMsg : '保存中,请稍后...',
									url : "timework/addTimeworkstatus.action",
									params : {
										method : "insert"
									},
									success : function(form, action) {
										blockForm.getForm().reset();
										blockAddWindow.hide();
										centerds.reload();
									},
									failure : function(form, action) {
										var o = eval('('
												+ action.response.responseText
												+ ')');
										Ext.MessageBox.alert('错误', o.eMsg);
									}
								});
							} else if (op == "edit") {
								blockForm.getForm().submit({
									waitMsg : '保存中,请稍后...',
									url : "timework/updateTimeworkstatus.action",
									params : {
										method : "edit",
										id : centergrid.getSelectionModel()
												.getSelected().data.id
									},
									success : function(form, action) {
										var o = eval('('
												+ action.response.responseText
												+ ')');
										blockForm.getForm().reset();
										blockAddWindow.hide();
										centerds.reload();
									},
									failure : function(form, action) {
										var o = eval('('
												+ action.response.responseText
												+ ')');
										Ext.MessageBox.alert('错误', o.eMsg);
									}
								});
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
		if (op == "insert") {
			// 新增时，赋初始值
			blockAddWindow.setTitle("新增定期工作状态");
		} else if (op == "edit") {
			blockAddWindow.setTitle("修改定期工作状态");
			blockAddWindow.show();
			var rec = centergrid.getSelectionModel().getSelected();
			blockForm.getForm().loadRecord(rec);
		} else {
		}
		blockAddWindow.show(Ext.get('getrole'));
	};

	// 添加按钮
	var btnAdd = new Ext.Button({
				id : 'add',
				text : '新建状态',
				iconCls : 'add',
				handler : function() {
					op = "insert";
					showAddWindow();
				}
			});

	function CKSelectone() {
		var rec = centergrid.getSelectionModel().getSelections();
		if (rec.length != 1) {
			Ext.Msg.alert('提示信息', "请选择一行！");
			return false;
		} else {
			op = "edit";
			showAddWindow();
		}
	}

	// 修改按钮
	var btnEdit = new Ext.Button({
				id : 'update',
				text : '修改',
				iconCls : 'update',
				handler : CKSelectone
			});

	// 锁定按钮
	var btnLok = new Ext.Button({
		id : 'locked',
		text : '锁定',
		iconCls : 'locked',
		handler : function() {
			if (centergrid.getSelectionModel().getSelections().length > 0) {
				Ext.Msg.confirm('提示信息', '确认 锁定 选中记录?', function(button, text) {
							if (button == 'yes') {
								var rec = centergrid.getSelectionModel()
										.getSelections();
								var str = "";
								for (var i = 0; i < rec.length; i++) {
									str += rec[i].data.id + ",";
								}
								Ext.Ajax.request({
											waitMsg : '锁定中,请稍后...',
											url : 'timework/lockTimeworkstatus.action',
											params : {
												method : "lock",
												ids : str
											},
											success : function(response,
													options) {
												centerds.reload();
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
	var btnUlk = new Ext.Button({
		id : 'unlocked',
		text : '解锁',
		iconCls : 'unlocked',
		handler : function() {
			if (centergrid.getSelectionModel().getSelections().length > 0) {
				Ext.Msg.confirm('提示信息', '确认 解锁 选中记录?', function(button, text) {
					if (button == 'yes') {
						var rec = centergrid.getSelectionModel()
								.getSelections();
						var str = "";
						for (var i = 0; i < rec.length; i++) {
							str += rec[i].data.id + ",";
						}
						Ext.Ajax.request({
									waitMsg : '解锁中,请稍后...',
									url : 'timework/unlockTimeworkstatus.action',
									params : {
										method : "unlock",
										ids : str
									},
									success : function(response, options) {
										centerds.reload();
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
	var btnRsh = new Ext.Button({
				id : 'reflesh',
				text : '刷新',
				iconCls : 'reflesh',
				handler : function() {
					centerds.load();
				}
			});

	// 删除按钮
	var btnDlt = new Ext.Button({
		id : 'delete',
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
							str += rec[i].data.id + ",";
						}
						Ext.Ajax.request({
									waitMsg : '删除中,请稍后...',
									url : 'timework/deleteTimeworkstatus.action',
									params : {
										method : "delete",
										ids : str
									},
									success : function(response, options) {
										centerds.reload();
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

	var datalist = new Ext.data.Record.create([centersm, {
				name : 'id'
			}, {
				name : 'code'
			}, {
				name : 'name'
			}, {
				name : 'orderby'
			}, {
				name : 'memo'
			}, {
				name : 'status'
			}]);

	var centersm = new Ext.grid.CheckboxSelectionModel();

	var centerds = new Ext.data.JsonStore({
				url : 'timework/getlistTimeworkstatus.action',
				fields : datalist
			});
	centerds.load();

	var centergrid = new Ext.grid.GridPanel({
				store : centerds,
				columns : [centersm, new Ext.grid.RowNumberer(), {
							header : "状态",
							width : 30,
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
							dataIndex : 'status'
						}, {
							header : "名称",
							width : 200,
							sortable : false,
							dataIndex : 'name'
						}, {
							header : "备注",
							width : 100,
							sortable : false,
							dataIndex : 'memo'
						}],
				viewConfig : {
					forceFit : true
				},
				tbar : [btnAdd, {
							xtype : "tbseparator"
						}, btnEdit, {
							xtype : "tbseparator"
						}, btnLok, {
							xtype : "tbseparator"
						}, btnUlk, {
							xtype : "tbseparator"
						}, btnRsh, {
							xtype : "tbseparator"
						}, btnDlt],
				sm : centersm,
				frame : true,
				enableColumnHide : false,
				enableColumnMove : false,
				iconCls : 'icon-grid'
			});

	// 设定布局器及面板
	var layout = new Ext.Viewport({
				layout : "border",
				border : false,

				items : [{
							title : "定期工作-定期工作状态维护",
							region : 'center',
							layout : 'fit',
							border : false,
							margins : '0 0 0 0',
							split : true,
							collapsible : false,
							items : [centergrid]

						}]
			});
});