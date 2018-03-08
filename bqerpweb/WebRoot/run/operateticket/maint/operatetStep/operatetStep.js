Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() { 
	var taskId = ""; 
	var deleteOpstep = new Array();
	var url = "opticket/findCheckStatus.action?";
	var conn = Ext.lib.Ajax.getConnectionObject().conn;
	conn.open("POST", url, false);
	conn.send(null);
	var checkStatusData = eval('(' + conn.responseText + ')').list;
	var formatData = new Ext.data.Record.create([{
		name : 'checkStatueId'
	}, {
		name : 'checkStatue'
	}]);

	var checkStore = new Ext.data.JsonStore({
		data : checkStatusData,
		fields : formatData
	});
	var rootNode = new Ext.tree.AsyncTreeNode({
		text : '操作任务维护',
		id : '-1',
		isRoot : true
	});
	function resetLine() {
		for (var j = 0; j < operateStepDs.getCount(); j++) {
			var temp = operateStepDs.getAt(j);
			temp.set("displayNo", j + 1);
		}
	}
	var treePanel = new Ext.tree.TreePanel({
//		renderTo : "treePanel",
		region : 'center',
		animate : true,
		title : '操作任务树',
		autoHeight : true,
		root : rootNode,
		border : false,
		rootVisible : false,
		loader : new Ext.tree.TreeLoader({
			dataUrl : "opticket/getOpTickTaskTreeNode.action"
		})
	});

	treePanel.on("click", treeClick);
//	treePanel.render();
	rootNode.expand();
	function treeClick(node, e) { 
		operateStepGrid.stopEditing();
		e.stopEvent();  
		var currentNode = node;
		node.toggle();
		
		var code = currentNode.attributes.code;
		if (code.length == 13 || code.length == 12) {
			// code.length == 12 modifyBy ywliu
			operateStepGrid.setVisible(true);
			taskId = currentNode.id;
			operateStepDs.load({
				params : {
					operateTaskId : taskId
				}
			});
		}else
		{
			 operateStepGrid.setVisible(false);
		}

	}
	
	var copyId ;
		treePanel.on('contextmenu', function(node, e) {
				node.select();// 很重要
				e.stopEvent();
				var code = node.attributes.code;
				var menu1 = new Ext.menu.Menu({
							id : 'mainMenu',
							items : [new Ext.menu.Item({
												text : '复制',
												iconCls : 'add',
												handler : function(){
													copyId = node.id;
												}
											})]
						});
						var menu2 = new Ext.menu.Menu({
							id : 'mainMenu',
							items : [ new Ext.menu.Item({
												text : '粘贴',
												iconCls : 'delete',
												handler : function(){
													if(code.length==10){
														Ext.Ajax.request({
														method:'post',
														url:'opticket/pasteNode.action',
														params:{
															copyTaskId:copyId,
															pasteTaskId:node.id
														},
														success:function(form,action){
															var o = eval("("+form.responseText+")");
															Ext.Msg.alert('提示','粘贴成功！');
															var cn = treePanel.getSelectionModel()
																		.getSelectedNode();
															var path = cn.getPath();
															treePanel.loader.load(node, function(){
																treePanel.expandPath(path, false);
																treePanel.getNodeById(o.id).select();
															});
														}
													});
													}else{
														Ext.Msg.alert('提示','不能粘贴在该目录下！');
													}
													
												}
											})]
						});
				var coords = e.getXY();
				var code = node.attributes.code;
				if (code.length == 13 || code.length == 12){
					menu1.showAt([coords[0], coords[1]]);
				}else if(copyId!=null&&copyId!=''){
					menu2.showAt([coords[0], coords[1]]);
				}
			}, this);
			
			
	var operateSteptbar = new Ext.Toolbar({
		items : [{
			id : 'add',
			text : "新增",
			iconCls : 'add',
			handler : function() { 
				var currentRecord = operateStepGrid.getSelectionModel()
						.getSelected();
				var count = operateStepDs.getCount();
				var currentIndex = currentRecord ? currentRecord
						.get("displayNo") : count;
				var o = new operateStep({
					operateStepId : '',
					operateTaskId : taskId,
					operateStepName : '',
					displayNo : currentIndex,
					memo : ''
				});
				operateStepGrid.stopEditing();
				operateStepDs.insert(currentIndex, o);
				operateStepSm.selectRow(currentIndex);
				operateStepGrid.startEditing(currentIndex, 2);
				resetLine();
			}
		}, '-', {
			id : 'btnDelete',
			text : "删除",
			iconCls : 'delete',
			handler : function() {
				var sm = operateStepGrid.getSelectionModel();
				var selected = sm.getSelections();
				if (selected.length == 0) {
					Ext.Msg.alert("提示", "请选择要删除的记录！");
				} else {
					for (var i = 0; i < selected.length; i += 1) {
						var member = selected[i];
						if (member.get("operateStepId") != "") {
							deleteOpstep.push(member.get("operateStepId"));
						}
						operateStepGrid.getStore().remove(member);
						operateStepGrid.getStore().getModifiedRecords()
								.remove(member);
					}
					resetLine();
				}
			}
		}, '-', {
			id : 'btnSave',
			text : "保存",
			iconCls : 'save',
			handler : function() {
				var modifyRec = operateStepGrid.getStore().getModifiedRecords();
				if (modifyRec.length > 0 || deleteOpstep.length > 0) {
					var newData = new Array();
					var updateData = new Array();
					for (var i = 0; i < modifyRec.length; i++) {
						if (modifyRec[i].get("operateStepId") == "") {
							newData.push(modifyRec[i].data);
						} else {
							updateData.push(modifyRec[i].data);
						}
					}
					Ext.Ajax.request({
						url : 'opticket/updateOperateStepList.action',
						method : 'post',
						params : {
							isAdd : Ext.util.JSON.encode(newData),
							isUpdate : Ext.util.JSON.encode(updateData),
							isDelete : deleteOpstep.join(",")
						},
						success : function(result, request) {
							var o = eval('(' + result.responseText+')');
							Ext.MessageBox.alert('提示信息', o.msg);
							operateStepDs.rejectChanges();
							deleteOpstep = [];
							operateStepDs.reload();
						},
						failure : function(result, request) {
							Ext.MessageBox.alert('提示信息', '未知错误！')
						}
					})
				} else {
					Ext.Msg.alert("提示", "您没有做任何修改!");
				}

			}
		}, '-', {
			id : 'btnCanse',
			text : "取消",
			iconCls : 'cancer',
			handler : function() {
				if (taskId == "") {
					Ext.Msg.alert('提示信息', '请选择操作任务！');
					return;
				} else {
					operateStepDs.reload();
					operateStepDs.rejectChanges();
				}

			}
		}]
	})
	var operateStep = Ext.data.Record.create([{
		name : 'operateStepId'
	}, {
		name : 'operateTaskId'
	}, {
		name : 'operateStepName'
	}, {
		name : 'displayNo'
	}, {
		name : 'memo'
	}]);
	var operateStepSm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false,
		listeners : {
			rowselect : function(sm, row, rec) {
			}
		}
	});
	var operateStepCm = new Ext.grid.ColumnModel([operateStepSm,
			new Ext.grid.RowNumberer({
				header : '行号',
				width : 35,
				hidden : true,
				sortable : false,
				align : 'left'
			}), {
				header : '内容',
				dataIndex : 'operateStepName',
				sortable : false,
				renderer : function(v) {
					return "<div style='white-space:normal;'>" + v + "</div>";
				},
				width : 400,
				editor : new Ext.form.TextField({
					listeners : {
						"render" : function() {
							this.el.on("dblclick", function() {
								var record = operateStepGrid
										.getSelectionModel().getSelected();
								var value = record.get('operateStepName');
								operateStepText.setValue(value);
								operateStepWin.show();
							})
						}
					}
				})
			}, {
				header : '顺序号',
				dataIndex : 'displayNo',
				width : 50,
				sortable : false,
				align : 'center',
				editor : new Ext.form.NumberField({
					allowBlank : false
				})
			}
//			, {
//				header : '备注',
//				dataIndex : 'memo',
//				sortable : false,
//				editor : new Ext.form.TextArea({
//					listeners : {
//						"render" : function() {
//							this.el.on("dblclick", function() {
//								var record = operateStepGrid
//										.getSelectionModel().getSelected();
//								var value = record.get('memo');
//								memoText.setValue(value);
//								win.show();
//							})
//						}
//					}
//				})
//			}
			]);
//	operateStepCm.defaultSortable = true;
	var operateStepDs = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'opticket/findOpticketStep.action'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'total',
			root : 'list'
		}, operateStep)
	});
	// workDs.load();
	operateStepbbar = new Ext.PagingToolbar({
		pageSize : 18,
		store : operateStepDs,
		displayInfo : true,
		displayMsg : '共 {2} 条',
		emptyMsg : "没有记录"
	});
	var operateStepGrid = new Ext.grid.EditorGridPanel({
		store : operateStepDs,
		cm : operateStepCm,
//		hidden:true,
		sm : operateStepSm,
		tbar : operateSteptbar,
		// bbar : workbbar,
		viewConfig : {
			forceFit : true
		},
		autoScroll:true,
		frame : false,
		border : false,
//		autoWidth : true,
//		autoHeight : true,
//		autoScroll : true,
		clicksToEdit : 1
//		,
//		loadMask : {
//			msg : '<img src="comm/images/extanim32.gif" width="32" height="32" style="margin-right: 8px;" align="absmiddle" />读取数据中 ...'
//		}
	});
	operateStepGrid.on("rowcontextmenu", function(g, i, e) {
		e.stopEvent();
		var _store = operateStepGrid.getStore();
		var menu = new Ext.menu.Menu({
			id : 'fuctionMenu',
			items : [new Ext.menu.Item({
				text : '移至顶行',
				iconCls : 'priorStep',
				handler : function() {
					if (i != 0) {
						var record = _store.getAt(i);
						_store.remove(record);
						_store.insert(0, record);
						resetLine();
					}
				}
			}), new Ext.menu.Item({
				text : '上移',
				iconCls : 'priorStep',
				handler : function() {
					if (i != 0) {
						var record = _store.getAt(i);
						_store.remove(record);
						_store.insert(i - 1, record);
						resetLine();
					}
				}
			}), new Ext.menu.Item({
				text : '下移',
				iconCls : 'nextStep',
				handler : function() {
					if ((i + 1) != _store.getCount()) {
						var record = _store.getAt(i);
						_store.remove(record);
						_store.insert(i + 1, record);
						resetLine();
					}
				}
			}), new Ext.menu.Item({
				text : '移至最后',
				iconCls : 'nextStep',
				handler : function() {
					if ((i + 1) != _store.getCount()) {
						var record = _store.getAt(i);
						_store.remove(record);
						_store.insert(_store.getCount(), record);
						resetLine();
					}
				}
			})]
		});
		var coords = e.getXY();
		menu.showAt([coords[0], coords[1]]);
	});
	var memoText = new Ext.form.TextArea({
		id : "memoText",
		maxLength : 100,
		width : 180
	});
	var operateStepText = new Ext.form.TextArea({
		id : "operateStepText",
		maxLength : 100,
		width : 180
	});
	var win = new Ext.Window({
		height : 170,
		width : 350,
		layout : 'fit',
		resizable : false,
		closeAction : 'hide',
		modal : true,
		items : [memoText],
		buttonAlign : "center",
		title : '详细信息录入窗口',
		buttons : [{
			text : "保存",
			iconCls : 'save',
			handler : function() {
				if (Ext.get("memoText").dom.value.length <= 100) {
					var record = operateStepGrid.selModel.getSelected()
					record.set("memo", Ext.get("memoText").dom.value);
					win.hide();
				}
			}
		}, {
			text : "取消",
			iconCls : 'canse',
			handler : function() {
				win.hide();
			}
		}]
	});
	var operateStepWin = new Ext.Window({
		height : 170,
		width : 350,
		layout : 'fit',
		resizable : false,
		closeAction : 'hide',
		modal : true,
		items : [operateStepText],
		buttonAlign : "center",
		title : '详细信息录入窗口',
		buttons : [{
			text : "保存",
			iconCls : 'save',
			handler : function() {
				if (Ext.get("operateStepText").dom.value.length <= 100) {
					var record = operateStepGrid.selModel.getSelected()
					record.set("operateStepName",
							Ext.get("operateStepText").dom.value);
					operateStepWin.hide();
				}
			}
		}, {
			text : "取消",
			iconCls : 'canse',
			handler : function() {
				operateStepWin.hide();
			}
		}]
	});
	var viewport = new Ext.Viewport({
		layout : 'border',
		items : [{
			region : 'west',
			layout : 'fit',
			split : true,
			width : 200,
			autoScroll : true,
			collapsible : true,
			border : false,
			items : [treePanel]
		}, {
			region : 'center',
			layout : 'fit',
//			autoWidth : true,
//			autoScroll : true,
			items : [operateStepGrid]
		}]
	});
	operateStepGrid.setVisible(false);
})