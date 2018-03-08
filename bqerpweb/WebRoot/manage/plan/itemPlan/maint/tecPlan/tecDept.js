Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {　
	var args = window.dialogArguments;
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

	var con_sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : false

			});
	var con_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'manageitemplan/getTecDeptList.action'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : "totalCount",
							root : "list"
						}, con_item)
			});
	con_ds.load()

	var con_item_cm = new Ext.grid.ColumnModel([con_sm,
			new Ext.grid.RowNumberer(), {
				header : '部门名称',
				dataIndex : 'depName',
				align : 'center',
				editor : new Ext.form.TextField({
							readOnly : true
						})
			}, {
				header : '显示顺序',
				dataIndex : 'displayNo',
				align : 'center',
				editor : new Ext.form.TextField()
			}, {
				hidden : true,
				dataIndex : 'depCode',
				align : 'center',
				editor : new Ext.form.TextField()
			}]);
	con_item_cm.defaultSortable = true;
	var dept = new Power.dept({});
	dept.btnConfrim.on('click',function(){
		var deptInstance = dept.getValue();
		if (deptInstance != null) {
					var count = con_ds.getCount();
					var currentIndex = count;
					var record = new con_item({
								depId : null,
								depCode : deptInstance.key,
								depName : deptInstance.value,
								displayNo : null
							})
					Grid.stopEditing();
					con_ds.insert(currentIndex, record);
					con_sm.selectRow(currentIndex);
					Grid.startEditing(currentIndex, 3);}
		
	})
//	dept.win.on('show',function(){
//		alert(9)
//	})
	// 增加
	function addTopic() {
		dept.win.show();
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
				if (member.get("depId") != null) {
					deptIds.push(member.get("depId"));
				}
				Grid.getStore().remove(member);
				Grid.getStore().getModifiedRecords().remove(member);
			}
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
						if (modifyRec[i].get('depId') != null) {
							updateData.push(modifyRec[i].data);
						} else {
							addData.push(modifyRec[i].data);
						}

					}
					Ext.Msg.wait('操作正在进行中……');
					Ext.Ajax.request({
								url : 'manageitemplan/saveOrUpdateTecDept.action',
								method : 'post',
								params : {
									isAdd : Ext.util.JSON.encode(addData),
									isUpdate : Ext.util.JSON.encode(updateData),
									ids : deptIds.join(",")
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
				id : "contbar",
				items : [{
							id : 'btnAdd',
							iconCls : 'add',
							text : "部门选择",
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
							text : "保存修改",
							handler : saveTopic
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

	if (typeof(args) != "undefined" && typeof(args) == 'object') {
		contbar.setDisabled(true)
		Grid.on("click", function() {
					var rec = Grid.selModel.getSelected();
					window.returnValue = rec;
					window.close();
				})
	}

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