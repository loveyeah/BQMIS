Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
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
			}, {
				name : 'tecFact',
				mapping : 7
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
	con_ds.load()
	
	var con_item_cm = new Ext.grid.ColumnModel([con_sm,
			new Ext.grid.RowNumberer(), {
				header : '部门名称',
				dataIndex : 'deptName',
				align : 'center'
			}, {
				header : '指标名称',
				dataIndex : 'itemName',
				align : 'center'
			}, {
				header : '计划值',
				dataIndex : 'tecPlan',
				align : 'center'
			}, {
				header : '完成值',
				dataIndex : 'tecFact',
				align : 'center',
				editor : new Ext.form.TextField()
			}]);
	con_item_cm.defaultSortable = true;

	// 增加
	function addTopic() {
		var count = con_ds.getCount();
		var currentIndex = count;
		var record = new con_item({
					'tecDetailId' : null,
					'tecMainId' : null,
					'technologyItemId' : null,
					'depId' : null,
					'deptName' : null,
					'itemName' : null,
					'tecPlan' : null
				});
		Grid.stopEditing();
		con_ds.insert(currentIndex, record);
		con_sm.selectRow(currentIndex);
		Grid.startEditing(currentIndex, 2);

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
						if (modifyRec[i].get('tecDetailId') != null) {
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
		items : [
				// {
				// id : 'btnAdd',
				// iconCls : 'add',
				// text : "新增",
				// handler : addTopic
				//
				// }, {
				// id : 'btnDelete',
				// iconCls : 'delete',
				// text : "删除",
				// handler : deleteTopic
				//
				// },
				{
			id : 'btnCancer',
			iconCls : 'cancer',
			text : "取消",
			handler : cancerTopic

		}, '-', {
			id : 'btnSave',
			iconCls : 'save',
			text : "保存修改",
			handler : saveTopic
		}, '-', {
			text : '返回',
			iconCls : 'query',
			handler : function() {
				window.location.replace("fillTecItemFinish.jsp?month=" + month);
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
							// bodyStyle : "padding: 20,10,20,20",
							layout : 'fit',
							border : false,
							frame : false,
							region : "center",

							items : [Grid]
						}]
			});

})