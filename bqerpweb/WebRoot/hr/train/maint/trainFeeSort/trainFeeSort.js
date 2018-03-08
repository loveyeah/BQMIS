Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var pageSize = 18;
	var MyRecord = Ext.data.Record.create([{
				name : 'feeSortId',
				mapping : 0
			}, {
				name : 'feeSortName',
				mapping : 1
			}, {
				name : 'orderBy',
				mapping : 2
			}]);
	var sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : false

			});
	var ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'com/findTrainSortList.action'
						}),

				reader : new Ext.data.JsonReader({
							totalCount : "totalCount",
							root : "list"
						}, MyRecord)
			});
	ds.load()

	var cm = new Ext.grid.ColumnModel([sm, new Ext.grid.RowNumberer({
									header : "行号",
									width : 50
								}), {
							header : '费用类别id',
							dataIndex : 'feeSortId',
							hidden : true
						}, {
							header : '费用类别名称',
							dataIndex : 'feeSortName',
							align : 'left',
							width :400,
							editor : new Ext.form.TextField({
										style : 'cursor:pointer',
										allowBlank : false
									})
						}, {
							header : '排序',
							dataIndex : 'orderBy',
							align : 'left',
							width : 300,
							editor : new Ext.form.NumberField({
										style : 'cursor:pointer',
										allowNegative : false,
										allowDecimal : false,
										allowBlank : true
									})
						}]);
	
	// 增加
	function addTheme() {
		var count = ds.getCount();
		var currentIndex = count;
		var o = new MyRecord({
					'feeSortId' : null,
					'feeSortName' : null,
					'orderBy' : null
				});
		grid.stopEditing();
		ds.insert(currentIndex, o);
		sm.selectRow(currentIndex);
		grid.startEditing(currentIndex, 3);
	}

	// 删除记录
	var ids = new Array();
	function deleteTheme() {
		grid.stopEditing();
		var sm = grid.getSelectionModel();
		var selected = sm.getSelections();
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i];
				if (member.get("feeSortId") != null) {
					ids.push(member.get("feeSortId"));
				}
				grid.getStore().remove(member);
				grid.getStore().getModifiedRecords().remove(member);
			}
		}
	}

	function checkFeeSortName() {
		for (var i = 0; i < ds.getCount(); i++) {
			for (var j = i + 1; j < ds.getCount(); j++) {
				if (ds.getAt(i).get('feeSortName') == ds.getAt(j)
						.get('feeSortName')) {
					Ext.Msg.alert('提示', '费用类别名称不能重复！');
					return false;
				}
			}
		}
		return true;
	}

	function checkOrderBy() {
		for (var i = 0; i < ds.getCount(); i++) {
			for (var j = i + 1; j < ds.getCount(); j++) {
				if (ds.getAt(i).get('orderBy') == ds.getAt(j).get('orderBy')) {
					Ext.Msg.alert('提示', '排序不能重复！');
					return false;
				}
			}
		}
		return true;
	}

	// 保存
	function saveTheme() {
		grid.stopEditing();
		if (!checkFeeSortName())
			return;
		if (!checkOrderBy())
			return;
		var modifyRec = grid.getStore().getModifiedRecords();
		if (modifyRec.length > 0 || ids.length > 0) {
			for (var i = 0; i < modifyRec.length; i++) {
				if (modifyRec[i].data.feeSortName == null
						|| modifyRec[i].data.feeSortName == "") {
					Ext.Msg.alert('提示信息', '费用类别名称不可为空，请输入！')
					return;
				}
				if (modifyRec[i].data.orderBy == null
						|| modifyRec[i].data.orderBy == "") {
					Ext.Msg.alert('提示信息', '排序不可为空，请输入！')
					return;
				}

			}
			Ext.Msg.confirm('提示信息', '确定要保存修改吗？', function(button) {
				if (button == 'yes') {
					var addData = new Array();
					var updateData = new Array();
					for (var i = 0; i < modifyRec.length; i++) {

						if (modifyRec[i].get('feeSortId') != null) {
							updateData.push(modifyRec[i].data);
						} else {
							addData.push(modifyRec[i].data);
						}

					}
					Ext.Ajax.request({
								url : 'com/saveTrainSortInfo.action',
								method : 'post',
								params : {
									isAdd : Ext.util.JSON.encode(addData),
									isUpdate : Ext.util.JSON.encode(updateData),
									ids : ids.join(",")
								},
								success : function(result, request) {
									var o = Ext.util.JSON
											.decode(result.responseText);
									Ext.Msg.alert("提示信息", o.msg);
									ds.rejectChanges();
									ids = [];
									ds.reload();
								},
								failure : function(result, request) {
									Ext.Msg.alert("提示信息", "数据保存修改失败！");
									ds.rejectChanges();
									ids = [];
									ds.reload();
								}
							})
				}
			})

		} else {
			Ext.MessageBox.alert('提示信息', '没有做任何修改！')
		}
	}
	
	var contbar = new Ext.Toolbar({
				items : [{
							id : 'btnAdd',
							iconCls : 'add',
							text : "增加",
							minWidth : 70,
							handler : addTheme
						}, '-', {
							id : 'btnDelete',
							iconCls : 'delete',
							minWidth : 70,
							text : "删除",
							handler :deleteTheme
						}, '-', {
							id : 'btnSave',
							iconCls : 'save',
							text : "保存修改",
							minWidth : 70,
							handler : saveTheme
						}]

			});
	var grid = new Ext.grid.EditorGridPanel({
				sm : sm,
				ds : ds,
				cm : cm,
				autoScroll : true,
				tbar : contbar,
				bbar : new Ext.PagingToolbar({
								pageSize : 18,
								store : ds,
								displayInfo : true,
								displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
								emptyMsg : "没有记录"
							}),
				border : true,
				clicksToEdit : 1
			});
	

	// 显示区域
	var view = new Ext.Viewport({
				enableTabScroll : true,
				autoScroll : true,
				layout : "border",
				items : [{
							region : 'center',
							layout : 'fit',
							autoScroll : true,
							items : [grid]
						}]
			})
});