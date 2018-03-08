Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	var con_item = Ext.data.Record.create([{
				name : 'depId',
				mapping : 0
			}, {
				name : 'depCode',
				mapping : 1
			}, {
				name : 'depName',
				mapping : 2
			}, {
				name : 'displayNo',
				mapping : 3
			}]);

	var dept_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'manageitemplan/getTecDeptList.action'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : "totalCount",
							root : "list"
						}, con_item)
			});
	dept_ds.load()
	var con_item_cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(),{
				header : '部门名称',
				dataIndex : 'depName'
			}, {
				header : '显示顺序',
				hidden : true,
				dataIndex : 'displayNo',
				align : 'center'
			}, {
				hidden : true,
				dataIndex : 'depCode',
				align : 'center'
			}]);
	con_item_cm.defaultSortable = true;
	// 取消
	var contbar = new Ext.Toolbar({
				id : "contbar",
				items : [{
							id : 'btnAdd',
							iconCls : 'add',
							text : "部门选择"
						}]
			});
	var leftGrid = new Ext.grid.GridPanel({
				title : '部门列表',
				sm : con_sm,
				ds : dept_ds,
				cm : con_item_cm,
				autoScroll : true,
				autoHeight : true,
				// tbar : contbar,
				border : true,
				viewConfig : {
					forceFit : true
				}
			});
			
	leftGrid.on("click", function() {
	if (leftGrid.getSelectionModel().hasSelection()) {
					var selections = leftGrid.getSelectionModel()
							.getSelections();
					if (selections.length == 1) {
						var seleced = leftGrid.getSelectionModel()
								.getSelected();
						con_ds.reload({
									params : {
										deptId : seleced.get('depId')
									}
								})
						contbar.setDisabled(false)
					}else
					{
						con_ds.removeAll();
						contbar.setDisabled(true)
//						Ext.getCmp("btnAddItem").setDisabled(true) ;
//						Ext.getCmp("btnDeleteItem").setDisabled(true);
//						Ext.getCmp("btnSaveItem").setDisabled(true);
					}
					}else
					{
						con_ds.removeAll();
						contbar.setDisabled(true)
//						Ext.getCmp("btnAddItem").setDisabled(true) ;
//						Ext.getCmp("btnDeleteItem").setDisabled(true);
//						Ext.getCmp("btnSaveItem").setDisabled(true);
					}
			})
	// 指标选择
	// var arg = window.dialogArguments;
	// var month = arg.month;
	var month = getParameter("month");
	var con_item = Ext.data.Record.create([{
				name : 'tecDetailId',
				mapping : 0
			}, {
				name : 'tecMainId',
				mapping : 1
			}, {
				name : 'depId',
				mapping : 2
			}, {
				name : 'technologyItemId',
				mapping : 3
			}, {
				name : 'deptName',
				mapping : 4
			}, {
				name : 'itemName',
				mapping : 5
			}, {
				name : 'tecPlan',
				mapping : 6
			}]);

	var con_sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : false

			});
	var con_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'manageitemplan/getTecDeptItemList.action'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : "totalCount",
							root : "list"
						}, con_item)
			});
	con_ds.baseParams = {
		month : month
	}
//	con_ds.load()

	/** 部门选择 * */
	function deptSelect() {
		var args = new Object();
		args.clickFlg = true;
		var url = "../../../../../manage/plan/itemPlan/maint/tecPlan/tecDept.jsp";
		var obj = window.showModalDialog(url, null,
				'dialogWidth=500px;dialogHeight=400px;status=no');
		if (typeof(obj) != "undefined") {
			var record = Grid.selModel.getSelected()
			var deptName = obj.data.depName;
			var deptId = obj.data.depId;
			record.set("deptName", deptName);
			record.set("depId", deptId);
			Grid.getView().refresh();
		}
	};
	/** 指标选择* */
	function itemSelect() {
		var args = new Object();
		args.clickFlg = true;
		var url = "../../../../../manage/plan/itemPlan/maint/tecPlan/tecItem.jsp";
		var obj = window.showModalDialog(url, null,
				'dialogWidth=500px;dialogHeight=400px;status=no');
		if (typeof(obj) != "undefined") {
			var record = Grid.selModel.getSelected()
			var alias = obj.data.alias;
			var technologyItemId = obj.data.technologyItemId;
			record.set("itemName", alias);
			record.set("technologyItemId", technologyItemId);
			Grid.getView().refresh();
		}
	}
	var con_item_cm = new Ext.grid.ColumnModel([con_sm,
			new Ext.grid.RowNumberer()
			// , {
			// header : '部门名称',
			// dataIndex : 'deptName',
			// align : 'center',
			// editor : new Ext.form.TriggerField({
			// onTriggerClick : deptSelect,
			// listeners : {
			// render : function(f) {
			// f.el.on('keyup', function(e) {
			// Grid.getSelectionModel()
			// .getSelected()
			// .set("depId", 'temp');
			// });
			// }
			// }
			// })
			// }
			, {
				header : '指标名称',
				dataIndex : 'itemName',
				align : 'center',
				editor : new Ext.form.TriggerField({
					onTriggerClick : itemSelect,
					listeners : {
						render : function(f) {
							f.el.on('keyup', function(e) {
										Grid
												.getSelectionModel()
												.getSelected()
												.set("technologyItemId", 'temp');
									});
						}
					}
				})
			}, {
				header : '计划值',
				dataIndex : 'tecPlan',
				align : 'center',
				editor : new Ext.form.TextField()
			}]);
	con_item_cm.defaultSortable = true;

	// 增加
	function addTopic() {
		var rec = leftGrid.getSelectionModel().getSelected();
		if (typeof(rec) != "undefined") {
			var url = "selectTecItem.jsp";
			var itemObj = window.showModalDialog(url, '',
					'dialogWidth=800px;dialogHeight=400px;status=no');
			if (typeof(itemObj) != "undefined" && itemObj != null) {
				var itemLen = itemObj.length;
				if (itemLen > 0) {
					for (i = 0; i < itemLen; i++) {
						var count = con_ds.getCount();
						var currentIndex = count;
						var record = new con_item({
									'tecDetailId' : null,
									'tecMainId' : null,
									'depId' : rec.data.depId,
									'technologyItemId ' : null,
									'deptName' : null,
									'itemName' : null,
									'tecPlan ' : null
								})

						Grid.stopEditing();
						con_ds.insert(currentIndex, record);
						con_sm.selectRow(currentIndex);
						record.set("technologyItemId",
								itemObj[i].technologyItemId);
						record.set("itemName", itemObj[i].alias);
						record.set("tecPlan", '');
						Grid.startEditing(currentIndex, 3);
					}
				}
			}
		} else {
			Ext.Msg.alert("提示", "请选择部门!");
			return false;
		}
	}

	// 删除记录
	var deptIds = new Array();
	function deleteTopic() {
		Grid.stopEditing();
		var sm = Grid.getSelectionModel();
		var selected = sm.getSelections();
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i];
				if (member.get("tecDetailId") != null) {
					deptIds.push(member.get("tecDetailId"));
				}
				Grid.getStore().remove(member);
				Grid.getStore().getModifiedRecords().remove(member);
			}
			// resetLine();
		}
	}
	// 保存
	function saveTopic() {
		Grid.stopEditing();
		var modifyRec = Grid.getStore().getModifiedRecords();
		if (modifyRec.length > 0 || deptIds.length > 0) {
			Ext.Msg.confirm('提示信息', '确定要保存修改吗？', function(button) {
				if (button == 'yes') {
					var addData = new Array();
					var updateData = new Array();
					for (var i = 0; i < modifyRec.length; i++) {
						if (modifyRec[i].get('tecDetailId') != null
								&& modifyRec[i].get('tecDetailId') != "") {
							updateData.push(modifyRec[i].data);
						} else {
							addData.push(modifyRec[i].data);
						}

					}
					Ext.Msg.wait('操作正在进行中……');
					Ext.Ajax.request({
								url : 'manageitemplan/saveOrUpdateTecDetail.action',
								method : 'post',
								params : {
									isAdd : Ext.util.JSON.encode(addData),
									isUpdate : Ext.util.JSON.encode(updateData),
									ids : deptIds.join(","),
									month : month
								},
								success : function(result, request) {
									var o = Ext.util.JSON
											.decode(result.responseText);
									Ext.Msg.alert("提示信息", o.msg);
									con_ds.rejectChanges();
									deptIds = [];
									con_ds.reload();
								},
								failure : function(result, request) {
									Ext.Msg.alert("提示信息", "数据保存修改失败！");
									con_ds.rejectChanges();
									deptIds = [];
									con_ds.reload();
								}
							})
				}
			})
		} else {
			Ext.MessageBox.alert('提示信息', '没有做任何修改！')
		}
	}
	// 取消
	function cancerTopic() {
		var modifyRec = con_ds.getModifiedRecords();
		if (modifyRec.length > 0 || deptIds.length > 0) {
			if (!confirm("确定要放弃修改吗"))
				return;
			con_ds.reload();
			con_ds.rejectChanges();
			deptIds = [];
		} else {
			con_ds.reload();
			con_ds.rejectChanges();
			deptIds = [];
		}
	}
	var contbar = new Ext.Toolbar({
				items : [{
							id : 'btnAdd',
							iconCls : 'add',
							text : "指标选择",
							disabled : true,
							handler : addTopic

						}, {
							id : 'btnDelete',
							iconCls : 'delete',
							disabled : true,
							text : "删除",
							handler : deleteTopic

						}, {
							id : 'btnCancer',
							disabled : true,
							iconCls : 'cancer',
							text : "取消",
							handler : cancerTopic

						}, '-', {
							id : 'btnSave',
							disabled : true,
							iconCls : 'save',
							text : "保存修改",
							handler : saveTopic
						}, '-', {
							text : '返回',
							iconCls : 'query',
							handler : function() {
								window.location
										.replace("fillTecItem.jsp?month="
												+ month);
							}

						}]

			});
	var Grid = new Ext.grid.EditorGridPanel({
				sm : con_sm,
				ds : con_ds,
				cm : con_item_cm,
				autoScroll : true,
				tbar : contbar,
				border : true,
				clicksToEdit : 1,
				viewConfig : {
					forceFit : true
				}

			});

	/** 左边的grid * */

	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				border : false,
				frame : false,
				items : [{
							width : "15%",
							border : false,
							frame : false,
							region : "west",
							items : [leftGrid]
						}, {
							layout : 'fit',
							border : false,
							frame : false,
							region : "center",
							items : [Grid]
						}]
			});

})