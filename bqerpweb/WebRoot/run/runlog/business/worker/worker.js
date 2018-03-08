Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	var runlogId = getParameter('runlogId');
	var tbar = new Ext.Toolbar({
		items : ['代班人员管理：', {
			id : 'btnAdd',
			text : "增加",
			iconCls : 'add',
			handler : function() {
				var args = {
					selectModel : 'multiple',
					rootNode : {
						id : '-1',
						text : '灞桥电厂'
					}
				}
				var url = "/power/comm/jsp/hr/workerByDept/workerByDept2.jsp";
				var rvo = window
						.showModalDialog(
								url,
								args,
								'dialogWidth:550px;dialogHeight:500px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
				if (typeof(rvo) != "undefined") {
					Ext.Ajax.request({
						url : 'runlog/addAgent.action',
						params : {
							ids : rvo.codes,
							runlogId : runlogId
						},
						method : 'post',
						waitMsg : '正在保存数据...',
						success : function(result, request) {
							Ext.Msg.alert('提示', '保存成功！');
							left_ds.reload();
						},
						failure : function(result, request) {
							Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
							window.close();
						}
					});
				}
			}
		}, '-', {
			id : 'btnDelete',
			text : "删除",
			iconCls : 'delete',
			handler : function() {
				var selectedRows = leftGrid.getSelectionModel().getSelections();
				if (selectedRows.length > 0) {
					Ext.Msg.confirm('提示', '删除的数据您将不能恢复,确定要删除吗?', function(b) {
						if (b == 'yes') {
							if ((selectedRows[0].data.woWorktype == "LOGAGN")
									|| (selectedRows[0].data.woWorktype == "LOGONS")) {
								Ext.Ajax.request({
									url : 'runlog/deleteAgent.action',
									params : {
										runlogWorkerId : selectedRows[0].data.runlogWorkerId
									},
									method : 'post',
									waitMsg : '正在处理...',
									success : function(result, request) {
										left_ds.reload();
									},
									failure : function(result, request) {
										Ext.MessageBox.alert('错误',
												'操作失败,请联系管理员!');
									}
								});
							} else {
								Ext.Msg.alert('提示', '您只可以删除正常的值班人员或代班人员！');
							}
						}
					})
				} else {
					Ext.Msg.alert('提示', '请在左边列表中选择一条记录!');
				}

			}
		}, '-', {
			id : 'btnRefresh',
			text : "刷新",
			iconCls : 'reflesh',
			handler : function() {
				left_ds.reload();
			}
		}]
	})
	var left_item = Ext.data.Record.create([{
		name : 'runlogWorkerId'
	}, {
		name : 'woWorktype'
	}, {
		name : 'bookedEmployee'
	}, {
		name : 'specialCode'
	}, {
		name : 'workerName'
	}, {
		name : 'specialName'
	}]);
	var left_sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false
	});
	var left_ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'runlog/findRunLogWorkerList.action'
		}),
		reader : new Ext.data.JsonReader({}, left_item)
	});
	left_ds.load({
		params : {
			method : "all",
			runlogId : runlogId
		}
	});
	var left_item_cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(),
			left_sm, {
				header : '专业',
				dataIndex : 'specialName',
				align : 'center'
			}, {
				header : '值班人员',
				dataIndex : 'workerName',
				align : 'center'
			}, {
				header : '状态',
				dataIndex : 'woWorktype',
				renderer : changeColor,
				align : 'center'
			}]);
	function changeColor(v) {
		if (v == 'LOGONS') {
			return "<div  style='width:40; background:green'>&nbsp;&nbsp;&nbsp;&nbsp;</div>";
		} else if (v == 'LOGAGN') {
			return "<div  style='width:40; background:gray'>&nbsp;&nbsp;&nbsp;&nbsp;</div>";
		} else if ((v == 'LOGABS') || (v == 'LOGABG')) {
			return "<div  style='width:40; background:red'>&nbsp;&nbsp;&nbsp;&nbsp;</div>";
		} else if (v == 'LOGSUC') {
			return "<div  style='width:40; background:navy'>&nbsp;&nbsp;&nbsp;&nbsp;</div>";
		} else if (v == 'LOGCHG') {
			return "<div  style='width:40; background:blue'>&nbsp;&nbsp;&nbsp;&nbsp;</div>";
		}
	}
	var leftGrid = new Ext.grid.GridPanel({
		id : 'left-grid',
		el : 'left-div',
		title : '值班人员',
		height : Ext.get("left-div").getHeight(),
		width : Ext.get("left-div").getWidth(),
		ds : left_ds,
		bbar : tbar,
		cm : left_item_cm,
		sm : left_sm,
		fitToFrame : true,
		border : true
			// ,
			// listeners : {
			// render : function(g) {
			// g.getSelectionModel().selectRow(0);
			// },
			// delay : 10
			// }
	});
	leftGrid.render();

	var righttbar = new Ext.Toolbar({
		items : [{
			id : 'btnSave',
			text : "保存缺勤原因",
			iconCls : 'update',
			handler : saveReason
		}]
	})
	var right_item = Ext.data.Record.create([{
		name : 'runlogWorkerId'
	}, {
		name : 'woWorktype'
	}, {
		name : 'bookedEmployee'
	}, {
		name : 'operateMemo'
	}, {
		name : 'workerName'
	}, {
		name : 'specialName'
	}]);
	var right_sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true
	});
	var right_ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'runlog/findRunLogWorkerList.action'
		}),
		reader : new Ext.data.JsonReader({}, right_item)
	});
	right_ds.load({
		params : {
			method : "absent",
			runlogId : runlogId
		}
	});
	var right_item_cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(),
			right_sm, {
				header : '缺勤人员',
				dataIndex : 'workerName',
				align : 'left'
			}, {
				header : '缺勤原因',
				dataIndex : 'operateMemo',
				editor : new Ext.form.TextField({
					allowBlank : true,
					align : 'left'
				}),
				align : 'center',
				width : 300
			}]);
	var rightGrid = new Ext.grid.EditorGridPanel({
		id : 'right-grid',
		el : 'right-div',
		title : '缺勤人员',
		height : Ext.get("right-div").getHeight(),
		width : Ext.get("right-div").getWidth(),
		ds : right_ds,
		cm : right_item_cm,
		sm : right_sm,
		fitToFrame : true,
		bbar : righttbar,
		border : true

			// viewConfig : {
			// forceFit : true
			// }
			// ,
			// listeners : {
			// render : function(g) {
			// g.getSelectionModel().selectRow(0);
			// },
			// delay : 10
			// }
	});
	rightGrid.render();
	function saveReason() {
		var str = "[";
		var record = rightGrid.getStore().getModifiedRecords();
		if (record.length > 0) {
			for (var i = 0; i < record.length; i++) {
				var data = record[i];
				str = str + "{'runlogWorkerId':" + data.get("runlogWorkerId")
						+ ",'operateMemo':'" + data.get("operateMemo") + "' },"
			}
			if (str.length > 1) {
				str = str.substring(0, str.length - 1);
			}
			str = str + "]";

			Ext.Ajax.request({
				url : 'runlog/addAbsentReason.action',
				params : {
					data : str
				},
				method : 'post',
				waitMsg : '正在加载数据...',
				success : function(result, request) {
					var json = eval('(' + result.responseText + ')');
					Ext.Msg.alert("注意", json.msg);
					right_ds.reload();
				},
				failure : function(result, request) {
					Ext.MessageBox.alert('错误', '操作失败!');
				}
			});
		} else {
			alert("没有对数据进行任何修改！");
		}

	}
	var addtBar = new Ext.Toolbar({
		items : [{
			id : 'btnAdd',
			text : "[>>>]",
			handler : function() {
				var selectedRows = leftGrid.getSelectionModel().getSelections();
				if (selectedRows.length > 0) {
					var ids = [];
					for (var i = 0; i < selectedRows.length; i += 1) {
						var member = selectedRows[i].data;
						if (member.woWorktype != "LOGSUC") {
							ids.push(member.runlogWorkerId);
						}
					}
					Ext.Ajax.request({
						url : 'runlog/addAbsent.action',
						params : {
							ids : ids.join(",")
						},
						method : 'post',
						waitMsg : '正在处理...',
						success : function(result, request) {
							right_ds.reload();
							left_ds.reload();
						},
						failure : function(result, request) {
							Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
						}
					});
				} else {

					Ext.Msg.alert('提示', '请在左边列表中选择一条记录!');
				}
			}
		}]
	});
	addtBar.render("add-div");

	var deleteBar = new Ext.Toolbar({
		items : [{
			id : 'btnDelete',
			text : "[<<<]",
			handler : function() {
				var selectedRows = rightGrid.getSelectionModel()
						.getSelections();
				if (selectedRows.length > 0) {
					Ext.Ajax.request({
						url : 'runlog/deleteAbsent.action',
						params : {
							runlogWorkerId : selectedRows[0].data.runlogWorkerId
						},
						method : 'post',
						waitMsg : '正在处理...',
						success : function(result, request) {
							right_ds.reload();
							left_ds.reload();
						},
						failure : function(result, request) {
							Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
						}
					});
				} else {
					Ext.Msg.alert('提示', '请在右边列表中选择一条记录!');
				}
			}
		}]
	});
	deleteBar.render("delete-div");
})