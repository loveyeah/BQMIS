Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {

	var bview;
	// 定义grid
	var MyRecord = Ext.data.Record.create([{
				name : 'id'
			}, {
				name : 'orderby'
			}, {
				name : 'repairmethodContent'
			}, {
				name : 'repairmethodFile'
			}, {
				name : 'repairmethodName'
			}, {
				name : 'repairmodeCode'
			}, {
				name : 'status'
			}]);

	var dataProxy = new Ext.data.HttpProxy({
				url : 'equstandard/getEquCStandardRepairmethodList.action'
			});

	var theReader = new Ext.data.JsonReader({
				root : "list",
				totalProperty : "totalCount"
			}, MyRecord);

	var store = new Ext.data.Store({
				proxy : dataProxy,
				reader : theReader
			});

	// 分页
	store.load({
				params : {
					start : 0,
					limit : 30
				}
			});

	var method;
	// 新增函数
	function addRecord() {
		win.show();
		myaddpanel.getForm().reset();
		btnView.setVisible(false);
		method = "add";
		myaddpanel.setTitle("新增维修方案");
		// Ext.get("repairmethodFile").dom.select();
		// document.selection.clear();
	}

	// 修改函数
	function updateRecord() {
		if (grid.selModel.hasSelection()) {
			var records = grid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑！");
			} else {
				var record = grid.getSelectionModel().getSelected();
				win.show();
				method = "update";
				myaddpanel.getForm().loadRecord(record);
				myaddpanel.setTitle("修改异动来源类型");
				if(record.get("repairmethodFile")!=null&&record.get("repairmethodFile")!="")
		        {
		        bview=record.get("repairmethodFile");
		        bview = '/power/upload_dir/equStandard/' + bview;
		        btnView.setVisible(true);
		        Ext.get("annex").dom.value = bview.replace('/power/upload_dir/equStandard/','');
		        }
		        else
		        {
		        	  btnView.setVisible(false);
		        }
			}
		} else {
			Ext.Msg.alert("提示", "请先选择要编辑的行!");
		}
	}

	// 删除按钮
	var deleteRecord = new Ext.Button({
		text : '删除',
		iconCls : 'delete',
		handler : function() {
			if (grid.getSelectionModel().getSelections().length > 0) {
				Ext.Msg.confirm('提示信息', '确认 删除 选中记录?', function(button, text) {
					if (button == 'yes') {
						var rec = grid.getSelectionModel().getSelections();
						var str = "";
						for (var i = 0; i < rec.length; i++) {
							if (i < rec.length - 1)
								str += rec[i].data.id + ",";
							else
								str += rec[i].data.id;
						}
						Ext.Ajax.request({
							waitMsg : '删除中,请稍后...',
							url : 'equstandard/deleteEquCStandardRepairmethod.action',
							params : {
								ids : str
							},
							success : function(response, options) {
								store.reload();
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
	var deleteFile = new Ext.Button({
		text : '删除文档',
		iconCls : 'delete',
		handler : function() {
			if (grid.getSelectionModel().getSelections().length > 0) {
				Ext.Msg.confirm('提示信息', '确认 删除 选中记录的 文档 ?', function(button,
						text) {
					if (button == 'yes') {
						var rec = grid.getSelectionModel().getSelections();
						var str = "";
						for (var i = 0; i < rec.length; i++) {
							if (i < rec.length - 1)
								str += rec[i].data.id + ",";
							else
								str += rec[i].data.id;
						}
						Ext.Ajax.request({
							waitMsg : '删除中,请稍后...',
							url : 'equstandard/deleteEquCStandardRepairmethodFile.action',
							params : {
								ids : str
							},
							success : function(response, options) {
								store.reload();
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

	// 锁定按钮
	var lockRecord = new Ext.Button({
		text : '锁定',
		iconCls : 'locked',
		handler : function() {
			if (grid.getSelectionModel().getSelections().length > 0) {
				Ext.Msg.confirm('提示信息', '确认 锁定 选中记录?', function(button, text) {
					if (button == 'yes') {
						var rec = grid.getSelectionModel().getSelections();
						var str = "";
						for (var i = 0; i < rec.length; i++) {
							if (i < rec.length - 1)
								str += rec[i].data.id + ",";
							else
								str += rec[i].data.id;
						}

						Ext.Ajax.request({
							waitMsg : '锁定中,请稍后...',
							url : 'equstandard/lockEquCStandardRepairmethod.action',
							params : {
								ids : str
							},
							success : function(response, options) {
								store.reload();
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

	// 解锁按钮
	var unlockRecord = new Ext.Button({
		text : '解锁',
		iconCls : 'unlocked',
		handler : function() {
			if (grid.getSelectionModel().getSelections().length > 0) {
				Ext.Msg.confirm('提示信息', '确认 解锁 选中记录?', function(button, text) {
					if (button == 'yes') {
						var rec = grid.getSelectionModel().getSelections();
						var str = "";
						for (var i = 0; i < rec.length; i++) {
							if (i < rec.length - 1)
								str += rec[i].data.id + ",";
							else
								str += rec[i].data.id;
						}
						Ext.Ajax.request({
							waitMsg : '解锁中,请稍后...',
							url : 'equstandard/unlockEquCStandardRepairmethod.action',
							params : {
								ids : str
							},
							success : function(response, options) {
								store.reload();
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
	var refRecord = new Ext.Button({
				text : '刷新',
				iconCls : 'reflesh',
				handler : function() {
					store.load({
								params : {
									start : 0,
									limit : 18
								}
							});
				}
			});

	var sm = new Ext.grid.CheckboxSelectionModel();

	// 定义grid
	var grid = new Ext.grid.GridPanel({
		region : "center",
		store : store,
		columns : [sm, new Ext.grid.RowNumberer(), {
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
					dataIndex : 'status'
				}, {
					header : "主键",
					width : 75,
					sortable : true,
					dataIndex : 'id',
					hidden : true
				}, {
					header : "名称",
					width : 150,
					sortable : true,
					dataIndex : 'repairmethodName'
				}, {
					header : "相关附件",
					width : 150,
					sortable : false,
					renderer : function changeIt(val) {
						if (val != "" && val != null) {
							// modified by liuyi 091116 文件目录不对
//							return "<a style=\"cursor:hand;color:red\" onClick=\"window.open('../../../../upload-file/equStandard/"
							return "<a style=\"cursor:hand;color:red\" onClick=\"window.open('../../../../upload_dir/equStandard/"
									+ val + "');\"/>查看附件</a>"
						} else {
							return "";
						}
					},
					dataIndex : 'repairmethodFile'
				}],
		tbar : [{
					text : "新增",
					iconCls : 'add',
					handler : addRecord
				}, {
					text : "修改",
					iconCls : 'update',
					handler : updateRecord
				}, deleteRecord, {
					xtype : "tbseparator"
				}, lockRecord, {
					xtype : "tbseparator"
				}, unlockRecord, {
					xtype : "tbseparator"
				}, deleteFile, {
					xtype : "tbseparator"
				}, refRecord],
		sm : sm, // 选择框的选择 Shorthand for selModel（selectModel）
		// 分页
		bbar : new Ext.PagingToolbar({
					pageSize : 30,
					store : store,
					displayInfo : true,
					displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
					emptyMsg : "没有记录"
				})
	});
	
	grid.on('rowdblclick',updateRecord);

	// 增加/修改
	var wd = 240;

	var repairName = {
		id : "repairmethodName",
		xtype : "textfield",
		fieldLabel : '名称',
		allowBlank : false,
		width : wd,
		name : 'baseInfo.repairmethodName'
	}

	var repairContent = new Ext.form.TextField({
				id : "repairmethodContent",
				xtype : "textarea",
				fieldLabel : '内容',
				width : wd,
				height : 50,
				name : 'baseInfo.repairmethodContent'
			})

//	var repairmethodFile = new Ext.form.TextField({
//				id : "repairmethodFile",
//				xtype : "textfield",
//				fieldLabel : '附件',
//				inputType : "file",
//				width : wd,
//				name : 'repairmethodFile'
//			});
	
	var repairCode = new Ext.form.Hidden({
				id : "repairCode",
				xtype : "textfield",
				fieldLabel : '编码',
				hidden : true,
				hideLabel : true,
				width : wd,
				readOnly : true,
				name : 'baseInfo.repairCode'
			})

	var repairId = new Ext.form.Hidden({
		id : "id",
//		xtype : "textfield",
		width : wd,
		readOnly : true,
		hidden : true,
		hideLabel : true,
		name : 'baseInfo.id'
	});

	var repairOrderby = new Ext.form.NumberField({
				id : "orderby",
				fieldLabel : '排序号',
				allowBlank : true,
				allowDecimals :false,//add by wpzhu
				width : wd,
				name : 'baseInfo.orderby'
			})

	// 附件 内容
	var annex = {
		id : "annex",
		xtype : 'fileuploadfield',
		isFormField:true,
//		name : "annex",
		name : 'repairmethodFile',
		fieldLabel : '附件',
	//	fileUpload : true,
		height : 21,
		width : wd - 60,
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

	// 点击新增，修改会弹出的FormPanel
	var myaddpanel = new Ext.FormPanel({
				frame : true,
				labelAlign : 'right',
				labelWidth : 60,
				fileUpload : true,
				title : '新增/修改维护方案',
				layout : 'column',
				items : [{
					layout : 'form',
					columnWidth : 1,
					items : [repairName]
					},{
					layout : 'form',
					columnWidth : 1,
					items : [repairContent]
					},{
					layout : 'form',
					columnWidth : 1,
					items : [repairCode,repairId]
					},{
					layout : 'form',
					columnWidth : 1,
					items : [repairOrderby]
					},{
					layout : 'form',
					columnWidth : 0.7,
					items : [annex]
					},{
					layout : 'form',
					columnWidth : 0.3,
					items : [btnView]
					}
				]
			});

	var win = new Ext.Window({
		width : 400,
		height : 250,
		buttonAlign : "center",
		items : [myaddpanel],
		layout : 'fit',
		closeAction : 'hide',
		modal : true,
		buttons : [{
			text : '保存',
			handler : function() {
				if(Ext.getCmp('repairmethodName').getValue() == null 
				|| Ext.getCmp('repairmethodName').getValue() == ''){
					Ext.Msg.alert('提示','名称不可为空，请输入！');
					return;
				}
				if (method == 'add') {
					myaddpanel.getForm().submit({
						waitMsg : '保存中,请稍后...',
						url : 'equstandard/saveEquCStandardRepairmethod.action',
						params : { 
						filepath : Ext.get("annex").dom.value
						},
						success : function(form, action) {
							myaddpanel.getForm().reset();
							win.hide();
							store.reload();
							 bview="";
						},
						failure : function(form, action) {
							var o = eval('(' + action.response.responseText
									+ ')');
							Ext.MessageBox.alert('错误', o.msg);
						}
					});
				} else if (method == "update") {
					myaddpanel.getForm().submit({
						waitMsg : '保存中,请稍后...',
						url : "equstandard/updateEquCStandardRepairmethod.action",
						params : {
							filepath : Ext.get("annex").dom.value
						},
						success : function(form, action) {
							var o = eval('(' + action.response.responseText
									+ ')');
							myaddpanel.getForm().reset();
							win.hide();
							store.reload();
							bview="";
						},
						failure : function(form, action) {
							var o = eval('(' + action.response.responseText
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
				win.hide();
			}
		}]
	});

	new Ext.Viewport({
				enableTabScroll : true,
				layout : "fit",
				items : [grid]
			});
})