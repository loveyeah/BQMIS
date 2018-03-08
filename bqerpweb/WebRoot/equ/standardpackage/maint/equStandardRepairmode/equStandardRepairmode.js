Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {

	var MyRecord = Ext.data.Record.create([{
				name : 'orderby'
			}, {
				name : 'repairmodeName'
			}, {
				name : 'id'
			}, {
				name : 'repairmodeCode'
			}, {
				name : 'repairmodeMemo'
			}, {
				name : 'status'
			}]);

	var dataProxy = new Ext.data.HttpProxy({
				url : 'equstandard/getEquCStandardRepairmodeList.action '
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
					limit : 18
				}
			});

	var sm = new Ext.grid.CheckboxSelectionModel();

	number = new Ext.grid.RowNumberer({
				header : "",
				align : 'left'
			})
	// 增加
	function addRecord() {
		// var currentRecord = gird.getSelectionModel().getSelected();
		var count = store.getCount();
		var currentIndex = count;
		// var currentIndex = currentRecord
		// ? currentRecord.get("displayNo") - 1
		// : count;
		var o = new MyRecord({
					status : 'C',
					repairmodeName : '',
					repairmodeMemo : '',
					orderby : ''
				});

		grid.stopEditing();
		store.insert(currentIndex, o);
		sm.selectRow(currentIndex);
		grid.startEditing(currentIndex, 3);
		// resetLine();
	}

	// 删除记录
	var ids = new Array();
	function deleteRecords() {
		var sm = grid.getSelectionModel();
		var selected = sm.getSelections();
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i];
				if (member.get("id") != null) {
					ids.push(member.get("id"));
				}
				grid.getStore().remove(member);
				grid.getStore().getModifiedRecords().remove(member);
			}
			// resetLine();
		}
	}
	// 保存
	function saveModifies() {
		grid.stopEditing();
		var modifyRec = grid.getStore().getModifiedRecords();
		if (modifyRec.length > 0 || ids.length > 0) {
			Ext.Msg.confirm('提示', '确定要保存修改数据吗?', function(button) {
				if (button == 'yes') {
					var updateData = new Array();
					for (var i = 0; i < modifyRec.length; i++) {
						updateData.push(modifyRec[i].data);
					}

					Ext.Ajax.request({
								url : 'equstandard/saveEquCStandardRepairmode.action',
								method : 'post',
								params : {
									// isAdd : Ext.util.JSON.encode(newData),
									isUpdate : Ext.util.JSON.encode(updateData),

									isDelete : ids.join(",")
								},
								success : function(result, request) {
									var o = eval('(' + result.responseText
											+ ')');
									Ext.MessageBox.alert('提示信息', o.msg);
									store.rejectChanges();
									ids = [];
									store.reload();
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
	// 取消
	function cancel() {
		var modifyRec = store.getModifiedRecords();
		if (modifyRec.length > 0 || ids.length > 0) {
			Ext.Msg.confirm('提示信息', '确定要放弃修改数据吗?', function(button) {
						if (button == 'yes') {
							store.reload();
							store.rejectChanges();
							ids = [];
						}
					})

		} else {
			store.reload();
			store.rejectChanges();
			ids = [];
		}
	}

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
						var record = grid.selModel.getSelected();
						record.set("repairmodeMemo",
								Ext.get("memoText").dom.value);
						win.hide();
					}
				}, {
					text : '关闭',
					iconCls : 'close',
					handler : function() {
						win.hide();
					}
				}]
			});

	// 定义grid
	var grid = new Ext.grid.EditorGridPanel({
		region : "center",
		store : store,
		columns : [sm, // 选择框
				number, {
					header : "状态",
					width : 80,
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
					editor : new Ext.form.ComboBox({

								store : new Ext.data.SimpleStore({
											fields : ["retrunValue",
													"displayText"],
											data : [['C', '正常'], ['L', '锁定']]
										}),
								valueField : "retrunValue",
								displayField : "displayText",
								mode : 'local',
								forceSelection : true,
								blankText : '工具',
								emptyText : '工具',
								// hiddenName : 'baseInfo.planToolCode',
								// value : '',

								editable : false,
								triggerAction : 'all',
								selectOnFocus : true,
								allowBlank : false,
								// name : 'baseInfo.planToolCode2',
								anchor : '99%'

							}),

					dataIndex : 'status'
				}, {
					header : "名称",
					width : 100,
					sortable : false,
					dataIndex : 'repairmodeName',
					editor : new Ext.form.TextField({
							// id : "repairmodeName",
							// xtype : "textfield",
							// fieldLabel : '名称',
							// allowBlank : false,
							// width : wd,
							// name : 'baseInfo.repairmodeName'
							})
				}, {
					header : "备注",
					width : 300,
					sortable : false,
					dataIndex : 'repairmodeMemo',
					editor : new Ext.form.TextArea({
								// id : "repairmodeMemo",
								// xtype : "textarea",
								// fieldLabel : '备注',
								// width : wd,
								// height : 100,
								// name : 'baseInfo.repairmodeMemo',
								maxLength : 128,
								listeners : {
									"render" : function() {
										this.el.on("dblclick", function() {
													var record = grid
															.getSelectionModel()
															.getSelected();
													var value = record
															.get('repairmodeMemo');
													memoText.setValue(value);
													win.x = undefined;
													win.y = undefined;
													win.show();
												})
									}
								}
							})
					// new Ext.form.TextArea({
					// maxLength : 128,
					// listeners:{
					// "render" : function() {
					// this.el.on("dblclick", function(){
					// var record = rightGrid.getSelectionModel().getSelected();
					// var value = record.get('memo');
					// memoText.setValue(value);
					// win.x = undefined;
					// win.y = undefined;
					// win.show();
					// })
					// }
					// }
					// }),
				}, {
					header : "排序号",
					width : 100,
					sortable : false,
					dataIndex : 'orderby',
					editor : new Ext.form.NumberField({
							// id : "orderby",
							// fieldLabel : '排序号',
							// allowBlank : true,
							// width : wd,
							// name : 'baseInfo.orderby'
							})
				}, {
					header : "主键",
					width : 100,
					sortable : false,
					hidden : true,
					dataIndex : 'id'
				}],
		tbar : [{
					text : "新增",
					iconCls : 'add',

					handler : addRecord
				},
				// {
				// text : "修改",
				// iconCls : 'update',
				//
				// handler : updateRecord
				// },
				{
					text : "删除",
					iconCls : 'delete',
					handler : deleteRecords

				}, {
					text : "保存",
					iconCls : 'save',
					handler : saveModifies
				}, {
					text : "取消",
					iconCls : 'cancer',
					handler : cancel
				}, {
					text : "锁定",
					iconCls : 'locked',
					handler : lockedRecord
				}, {
					text : "解锁",
					iconCls : 'unlocked',
					handler : unlockedRecord
				}],
		sm : sm, // 选择框的选择 Shorthand for selModel（selectModel）
		// 分页
		bbar : new Ext.PagingToolbar({
					pageSize : 18,
					store : store,
					displayInfo : true,
					displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
					emptyMsg : "没有记录"
				})
	});

	// var wd = 180;
	//
	// var repairOrderby = new Ext.form.NumberField({
	// id : "orderby",
	// fieldLabel : '排序号',
	// allowBlank : true,
	// width : wd,
	// name : 'baseInfo.orderby'
	// })
	//
	// var repairName = {
	// id : "repairmodeName",
	// xtype : "textfield",
	// fieldLabel : '名称',
	// allowBlank : false,
	// width : wd,
	// name : 'baseInfo.repairmodeName'
	// }
	//
	// var repairMemo = {
	// id : "repairmodeMemo",
	// xtype : "textarea",
	// fieldLabel : '备注',
	// width : wd,
	// height : 100,
	// name : 'baseInfo.repairmodeMemo'
	// }
	//
	// var repairid = {
	// id : "id",
	// xtype : "hidden",
	// width : wd,
	// readOnly : true,
	// hideLabel : true,
	// name : 'baseInfo.id'
	// }
	//
	// var myaddpanel = new Ext.FormPanel({
	// frame : true,
	// labelAlign : 'right',
	// title : '新增/修改维护方案',
	// items : [repairName, repairMemo, repairOrderby, repairid]
	// });

	// var win = new Ext.Window({
	// width : 400,
	// height : 250,
	// buttonAlign : "center",
	// items : [myaddpanel],
	// layout : 'fit',
	// closeAction : 'hide',
	// modal : true,
	// buttons : [{
	// text : '保存',
	// handler : function() {
	// if (method == 'add') {
	// myaddpanel.getForm().submit({
	// waitMsg : '保存中,请稍后...',
	// url : 'equstandard/saveEquCStandardRepairmode.action',
	// success : function(form, action) {
	// myaddpanel.getForm().reset();
	// win.hide();
	// store.reload();
	// },
	// failure : function(form, action) {
	// var o = eval('(' + action.response.responseText
	// + ')');
	// Ext.MessageBox.alert('错误', o.eMsg);
	// }
	// });
	// } else if (method = "update") {
	// myaddpanel.getForm().submit({
	//
	// waitMsg : '保存中,请稍后...',
	// url : "equstandard/updateEquCStandardRepairmode.action",
	// params : {
	// id : grid.getSelectionModel().getSelected()
	// .get("id")
	// },
	// success : function(form, action) {
	// var o = eval('(' + action.response.responseText
	// + ')');
	// myaddpanel.getForm().reset();
	// win.hide();
	// store.reload();
	//
	// },
	//
	// failure : function(form, action) {
	// var o = eval('(' + action.response.responseText
	// + ')');
	// Ext.MessageBox.alert('错误', o.eMsg);
	// }
	// });
	// } else {
	// Ext.MessageBox.alert('错误', '未定义的操作');
	// }
	// }
	// }, {
	// text : '取消',
	// handler : function() {
	// win.hide();
	// }
	// }]
	// });

	// // 新增窗口
	// function addRecord() {
	// method = 'add';
	// myaddpanel.getForm().reset();
	// win.show();
	// myaddpanel.setTitle("新增维修方案");
	// }
	//
	// // 修改窗口
	// function updateRecord() {
	// method = "update";
	// if (grid.selModel.hasSelection()) {
	//
	// var records = grid.selModel.getSelections();
	// var recordslen = records.length;
	// if (recordslen > 1) {
	// Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑！");
	// } else {
	// var record = grid.getSelectionModel().getSelected();
	// win.show();
	// myaddpanel.getForm().loadRecord(record);
	// myaddpanel.setTitle("修改异动来源类型");
	// }
	// } else {
	// Ext.Msg.alert("提示", "请先选择要编辑的行!");
	// }
	//
	// }
	//
	// 删除
	// function deleteRecord() {
	// if (grid.getSelectionModel().getSelections().length > 0) {
	// Ext.Msg.confirm('提示信息', '确认 删除 选中记录?', function(button, text) {
	// if (button == 'yes') {
	// var rec = grid.getSelectionModel().getSelections();
	// var str = "";
	// for (var i = 0; i < rec.length; i++) {
	// str += rec[i].get("id") + ",";
	// }
	// Ext.Ajax.request({
	// waitMsg : '删除中,请稍后...',
	// url : "equstandard/deleteEquCStandardRepairmodes.action",
	// params : {
	// ids : str
	// },
	// success : function(response, options) {
	// store.reload();
	// },
	// failure : function() {
	// Ext.Msg.alert('提示信息', '服务器错误,请稍候重试!')
	// }
	// });
	// }
	// })
	// } else {
	// Ext.Msg.alert('提示信息', "请至少选择一行！");
	// return false;
	// }
	// }

	// 锁定
	function lockedRecord() {
		if (grid.getSelectionModel().getSelections().length > 0) {
			Ext.Msg.confirm('提示信息', '确认 锁定 选中记录?', function(button, text) {
				if (button == 'yes') {
					var rec = grid.getSelectionModel().getSelections();
					var str = "";
					for (var i = 0; i < rec.length; i++) {
						str += rec[i].get("id") + ",";
					}
					Ext.Ajax.request({
								waitMsg : '锁定中,请稍后...',
								url : 'equstandard/lockEquCStandardRepairmodes.action',
								params : {
									//method : "lock",
									ids : str
								},
								success : function(response, options) {
									store.reload();
									Ext.Msg.alert('提示信息', '锁定记录成功！');
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

	// 解锁
	function unlockedRecord() {
		if (grid.getSelectionModel().getSelections().length > 0) {
			Ext.Msg.confirm('提示信息', '确认 解锁 选中记录?', function(button, text) {
				if (button == 'yes') {
					var rec = grid.getSelectionModel().getSelections();
					var str = "";
					for (var i = 0; i < rec.length; i++) {
						str += rec[i].get("id") + ",";
					}
					Ext.Ajax.request({
						waitMsg : '解锁中,请稍后...',
						url : 'equstandard/unlockEquCStandardRepairmodes.action',
						params : {
							//method : "unlock",
							ids : str
						},
						success : function(response, options) {
							store.reload();
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

	new Ext.Viewport({
				enableTabScroll : true,
				layout : "fit",
				items : [grid]
			});
})