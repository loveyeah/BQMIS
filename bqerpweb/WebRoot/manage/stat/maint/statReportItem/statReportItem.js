Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	var con_item = Ext.data.Record.create([{
				name : 'reportCode'
			}, {
				name : 'reportName'
			}, {
				name : 'reportType'
			}]);

	var con_sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			});
	var con_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'manager/findStatReportList.action' // 统计报表名称
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : "totalCount",
							root : "list"
						}, con_item)
			});
	con_ds.load({
				params : {
					start : 0,
					limit : 18
				}
			})

	var con_item_cm = new Ext.grid.ColumnModel([con_sm, {
				header : '编码',
				width:40,
				dataIndex : 'reportCode',
				align : 'center'
			}, {
				header : '统计报表名称',
				dataIndex : 'reportName',
				align : 'left',
				editor : new Ext.form.TextField()
			}, {
				header : '报表类型',
				dataIndex : 'reportType',
				align : 'center',
				width: 60,
				editor : new Ext.form.ComboBox({
							readOnly : true,
							name : 'name',
							hiddenName : 'hiddenName',
							mode : 'local',
							triggerAction : 'all',
							store : new Ext.data.SimpleStore({
										fields : ['name', 'value'],
										data : [['月', 'M'],

												['日', 'D']]
									}),
							displayField : 'name',
							valueField : 'value'

						}),
				renderer : function changeIt(val) {
					// alert(val)
					if (val == "M") {
						return "月";
					} else if (val == "D") {
						return "日";

					} else {
						return "";
					}
				}
			}]);
	con_item_cm.defaultSortable = true;

	var gridbbar = new Ext.PagingToolbar({
				pageSize : 18,
				store : con_ds,
				displayInfo : true,
				displayMsg : "共{2}条",
				emptyMsg : "没有记录",
				beforePageText : '',
				afterPageText : ""
			});
	// 增加
	function addTopic() {

		var count = con_ds.getCount();

		var currentIndex = count;

		var o = new con_item({
					'reportType' : 'M',
					'reportCode' : '',

					'reportName' : ''

				});

		Grid.stopEditing();
		con_ds.insert(currentIndex, o);
		con_sm.selectRow(currentIndex);
		Grid.startEditing(currentIndex, 2);
		// resetLine();
	}

	// 删除记录
	var topicIds = new Array();
	function deleteTopic() {
		Grid.stopEditing();
		var sm = Grid.getSelectionModel();
		var selected = sm.getSelections();
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i];
				if (member.get("reportCode") != null) {
					topicIds.push(member.get("reportCode"));
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

		if (modifyRec.length > 0 || topicIds.length > 0) {

			if (!confirm("确定要保存修改吗?"))
				return;
			// var newData = new Array();
			var updateData = new Array();
			for (var i = 0; i < modifyRec.length; i++) {
				if (modifyRec[i].get("reportName") == "") {
					Ext.MessageBox.alert('提示信息', '报表名称不能为空！')
					return
				}

				updateData.push(modifyRec[i].data);
				// }
			}

			Ext.Ajax.request({
						url : 'manager/modifyRecords.action',
						method : 'post',
						params : {
							// isAdd : Ext.util.JSON.encode(newData),
							addOrUpdateRecords : Ext.util.JSON
									.encode(updateData),
							deleteRecords : topicIds.join(",")
						},
						success : function(result, request) {
							var o = eval('(' + result.responseText + ')');
							Ext.MessageBox.alert('提示信息', o.msg);

							con_ds.rejectChanges();
							topicIds = [];
							con_ds.reload();
						},
						failure : function(result, request) {
							Ext.MessageBox.alert('提示信息', '未知错误！')
						}
					})
		} else {
			Ext.MessageBox.alert('提示信息', '没有做任何修改！')
		}
	}
	// 取消
	function cancerTopic() {
		var modifyRec = con_ds.getModifiedRecords();
		if (modifyRec.length > 0 || topicIds.length > 0) {
			if (!confirm("确定要放弃修改吗"))
				return;
			con_ds.reload();
			con_ds.rejectChanges();
			topicIds = [];
		} else {
			con_ds.reload();
			con_ds.rejectChanges();
			topicIds = [];
		}
	}
	var contbar = new Ext.Toolbar({
				items : [{
							id : 'btnAdd',
							iconCls : 'add',
							text : "新增",
							handler : addTopic

						}, {
							id : 'btnDelete',
							iconCls : 'delete',
							text : "删除",
							handler : deleteTopic

						}, {
							id : 'btnCancer',
							iconCls : 'cancer',
							text : "取消",
							handler : cancerTopic

						}, '-', {
							id : 'btnSave',
							iconCls : 'save',
							text : "保存",
							handler : saveTopic
						}, '-', {
							text : '权限设置',
							iconCls : 'view',
							handler : function() {
								var record = Grid.getSelectionModel()
										.getSelected();
								if (record != null) {
									var reportCode = record.get("reportCode")+"_tj";
									var permiUser = new Power.reportPermissions(
											{
												reportCode : reportCode
											})
									permiUser.win.show();
								} else {
									Ext.MessageBox.alert('提示', '请先选择一条记录！');
								}
							}

						}]

			});
	var Grid = new Ext.grid.EditorGridPanel({
		viewConfig : {
			forceFit : true
		},
		sm : con_sm,
		ds : con_ds,
		cm : con_item_cm,
		height : 425,
		split : true,
		autoScroll : true,
		bbar : gridbbar,
		tbar : contbar,
		border : true
	});
	Grid.on('rowclick', modifyBtn);

	function modifyBtn() {
		var recL = Grid.getSelectionModel().getSelected();
		if (recL && recL.data.reportCode != "") {
			store.load({
						params : {
							reportCode : recL.get("reportCode")
						}
					});
		}
		// else {
		// Ext.Msg.alert("提示", "请选择左边的一条记录！")
		// }
	}

	var MyRecord = Ext.data.Record.create([{
				name : 'model.id.reportCode'
			}, {
				name : 'model.id.itemCode'
			}, {
				name : 'itemName'
			}, {
				name : 'model.displayNo'
			}]);

	var dataProxy = new Ext.data.HttpProxy({
				url : 'manager/findStatReportItemList.action '
			});

	var theReader = new Ext.data.JsonReader({
				root : "list",
				totalProperty : "totalCount"
			}, MyRecord);

	var store = new Ext.data.Store({
				proxy : dataProxy,
				reader : theReader
			});
	var sm = new Ext.grid.CheckboxSelectionModel({
			// singleSelect : true
			});

	number = new Ext.grid.RowNumberer({
			
				header : "",
				align : 'left'
			})
	// 定义grid
	var grid = new Ext.grid.EditorGridPanel({
		viewConfig : {
			forceFit : true
		},
		store : store,
		layout : 'fit',
		autoScroll : true,
		columns : [number, { 
					header : "指标编码", 
					align : "left",
					width:45,
					sortable : false,
					dataIndex : 'model.id.itemCode'
				}, {
					header : "指标名称", 
					align : "left",
					sortable : true,
					dataIndex : 'itemName'
				}, {
					header : "显示顺序",
					width:30,
					align : "left",
					sortable : true,
					dataIndex : 'model.displayNo',
					editor : new Ext.form.NumberField()
				}],
		tbar : [{
					text : "指标选择",
					iconCls : 'add',
					handler : addRecord
				} 
		],
		sm : sm 
		});

	// 重置排序号
	function resetLine() {
		for (var j = 0; j < store.getCount(); j++) {
			var temp = store.getAt(j);
			temp.set("displayNo", j + 1);
		}
	}
	// 指标选择
	function addRecord() {
		var recL = Grid.getSelectionModel().getSelected();
		if (recL) {
			if (recL.data.reportCode != "") {
				var args = {
					accountType : recL.get("reportType"),
					accountCode : recL.get("reportCode")
				}
				var url = "selectItem.jsp";
				rvo = window
						.showModalDialog(
								url,
								args,
								'dialogWidth:600px;dialogHeight:400px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
				store.load({
							params : {
								reportCode : recL.get("reportCode")
							}
						}); 
			} else {
				Ext.Msg.alert("提示", "请选择一条已保存的记录！")
			}
		} else {
			Ext.Msg.alert("提示", "请选择左边一条记录！")
		}
	}

	// 删除记录
	var ids = new Array();
	var reportcode = new Array();
	function deleteRecords() {
		var sm = grid.getSelectionModel();
		var selections = sm.getSelections();
		if (selections.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selections.length; i += 1) {
				var member = selections[i];
				var id = new Array();
				var code = new Array();

				if (member.get("model.id.itemCode") != null
						&& member.get("model.id.reportCode") != null) {
					id.push("'" + member.get("model.id.itemCode") + "'");
					reportcode.push(member.get("model.id.reportCode"));
					// id.push(member.get("model.id.itemCode") + ","
					// + member.get("model.id.reportCode"));
					var idstr = '';
					idstr = id.join(",");
					ids.push(idstr);
				}
				grid.getStore().remove(member);
				grid.getStore().getModifiedRecords().remove(member);
			}
		}
	}
	// 保存
	function saveModifies() {
		grid.stopEditing();
		var modifyRec = store.getRange(0, rvo.length);
		alert(modifyRec.length)
		if (modifyRec.length > 0 || ids.length > 0) {
			if (!confirm("确定要保存修改吗?"))
				return;
			var updateData = new Array();
			for (var i = 0; i < modifyRec.length; i++) {
				updateData.push(modifyRec[i].data);
			}
			// alert(ids.join(";"));
			Ext.Ajax.request({
						url : 'manager/saveStatReportItem.action',
						method : 'post',
						params : {
							isUpdate : Ext.util.JSON.encode(updateData),
							isDelete : ids.join(","),
							isCode : reportcode
						},
						success : function(result, request) {
							var o = eval('(' + result.responseText + ')');
							Ext.MessageBox.alert('提示信息', o.msg);
							store.rejectChanges();
							ids = [];
							store.reload();
						},
						failure : function(result, request) {
							Ext.MessageBox.alert('提示信息', '未知错误！')
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
			if (!confirm("确定要放弃修改吗"))
				return;
			store.reload();
			store.rejectChanges();
			ids = [];
		} else {
			store.reload();
			store.rejectChanges();
			ids = [];
		}
	}

	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				border : false,
				frame : false,
				items : [{
							bodyStyle : "padding: 1,1,1,1",
							layout : 'fit',
							border : false,
							frame : false,
							region : "west",
							width : '37%',
							items : [Grid]
						}, {
							bodyStyle : "padding: 1,1,1,0",
							region : "center",
							border : false,
							frame : false,
							layout : 'fit',
							items : [grid]
						}]
			});
})