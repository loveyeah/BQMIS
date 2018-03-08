Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {

	var rec = Ext.data.Record.create([{
				name : 'accountCode'
			}, {
				name : 'accountName'
			}, {
				name : 'accountType'
			}]);

	var ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'manager/getBpCAnalyseAccountList.action'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : 'totalCount',
							root : 'list'
						}, rec)
			});

	/* 设置每一行的选择框 */
	var sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : false,
				listeners : {
					rowselect : function(sm, row, rec) {
						// Ext.getCmp("form").getForm().loadRecord(rec);
					}
				}
			});

	var rn = new Ext.grid.RowNumberer();
	
	// 时间类型
	var dataTimeTypeData = Ext.data.Record.create([{
				name : 'value'
			}, {
				name : 'key'
			}]);
			
		var dataTimeTypeStore = new Ext.data.JsonStore({
				url : 'comm/getBpBasicDataByType.action?type=DATA_TIME_TYPE',
				fields : dataTimeTypeData
			});

	dataTimeTypeStore.load();
	

	var dataTimeTypeBox = new Ext.form.ComboBox({
				fieldLabel : '时间类型',
				store : dataTimeTypeStore,
				id : 'dataTimeType',
				name : 'dataTimeType',
				valueField : "key",
				displayField : "value",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'statItem.dataTimeType',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : true,
				emptyText : '请选择',
				anchor : '85%'
			});
	var cm = new Ext.grid.ColumnModel([sm, rn

	,		{
				header : '台帐编码',
				dataIndex : 'accountCode',
				align : 'left'
			},

			{
				header : '台帐名称',
				dataIndex : 'accountName',
				align : 'left',
				editor : new Ext.form.TextField({}),
				width : 180,
				renderer:function(value, metadata, record){ 
						metadata.attr = 'style="white-space:normal;"'; 
						return value;//modify by ywliu 20090911  
				}
				// ,
			// width : auto
		}	, {
				header : '台帐类型',
				dataIndex : 'accountType',
				align : 'left',

				editor : dataTimeTypeBox,
				renderer : function changeIt(val) {
					for (i = 0; i < dataTimeTypeStore.getCount(); i++) {
						if (dataTimeTypeStore.getAt(i).get("key") == val)
							return dataTimeTypeStore.getAt(i).get("value");
					}

				}

			}]);
	ds.load({
				params : {
					start : 0,
					limit : 18
				}
			});

	// // 重置排序号
	// function resetLine() {
	// for (var j = 0; j < ds.getCount(); j++) {
	// var temp = ds.getAt(j);
	// temp.set("displayNo", j + 1);
	//
	// }
	// }
	// 增加
	function addRecords() {
		var currentRecord = grid.getSelectionModel().getSelected();
		var rowNo = ds.indexOf(currentRecord);

		var count = ds.getCount();

		var currentIndex = count;

		var o = new rec({
					'accountCode' : '',
					'accountName' : '',
					'accountType' : '1'

				});

		grid.stopEditing();
		ds.insert(currentIndex, o);
		sm.selectRow(currentIndex);
		grid.startEditing(currentIndex, 2);
		// resetLine();
	}

	// 删除记录
	var ids = new Array();
	function deleteRecords() {
		grid.stopEditing();
		var sm = grid.getSelectionModel();
		var selected = sm.getSelections();
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i];
				if (member.get("accountCode") != null) {
					ids.push(member.get("accountCode"));
				}
				grid.getStore().remove(member);
				grid.getStore().getModifiedRecords().remove(member);
			}
			// resetLine();
		}
	}
	// 保存
	function save() {
		grid.stopEditing();
		var modifyRec = grid.getStore().getModifiedRecords();

		if (modifyRec.length > 0 || ids.length > 0) {

			if (!confirm("确定要保存修改吗?"))
				return;
			// var newData = new Array();
			var updateData = new Array();
			for (var i = 0; i < modifyRec.length; i++) {

				if (modifyRec[i].get("accountName") == "") {
					Ext.MessageBox.alert('提示信息', '录入台帐名称不能为空！')
					return
				}
				updateData.push(modifyRec[i].data);
				// }
			}

			Ext.Ajax.request({
						url : 'manager/saveBpCAnalyseAccount.action',
						method : 'post',
						params : {
							// isAdd : Ext.util.JSON.encode(newData),
							isUpdate : Ext.util.JSON.encode(updateData),
							isDelete : ids.join(",")
						},
						success : function(result, request) {
							var o = eval('(' + result.responseText + ')');
							Ext.MessageBox.alert('提示信息', o.msg);

							ds.rejectChanges();
							ids = [];
							ds.reload();
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
	function cancer() {
		var modifyRec = ds.getModifiedRecords();
		if (modifyRec.length > 0 || ids.length > 0) {
			if (!confirm("确定要放弃修改吗"))
				return;
			ds.reload();
			ds.rejectChanges();
			ids = [];
		} else {
			ds.reload();
			ds.rejectChanges();
			ids = [];
		}
	}
	// 时点设置
	function timeSet() {
		var recs = grid.getSelectionModel().getSelections();

		if (recs.length == 1) {
			var rec = grid.getSelectionModel().getSelected();
			if (

			rec.get("accountType") != null && rec.get("accountType") != '') {

				if (rec.get("accountCode") == '') {
					Ext.Msg.alert('提示', '请先保存再设置时点！');
					return;
				}
				window
						.showModalDialog("/power/manage/stat/maint/accountName/dateTypeMaint.jsp?accountCode="
								+ rec.get("accountCode")
								+ "&dateType="
								+ rec.get("accountType"));
			} else
				Ext.Msg.alert('提示', '请先选择台帐类型！')
		} else
			Ext.Msg.alert('提示', '请选择一条记录！')
	}

	var tbar = new Ext.Toolbar({
				items : [{
							id : 'btnAdd',
							iconCls : 'add',
							text : "新增",
							handler : addRecords

						}, {
							id : 'btnDelete',
							iconCls : 'delete',
							text : "删除",
							handler : deleteRecords

						}, {
							id : 'btnCancer',
							iconCls : 'cancer',
							text : "取消",
							handler : cancer

						}, '-', {
							id : 'btnSave',
							iconCls : 'save',
							text : "保存修改",
							handler : save
						}, '-', {
							id : 'btnTimeSet',
							iconCls : 'write',
							text : "时点设置",
							handler : timeSet
						}, '-', {
							text : '权限设置',
							iconCls : 'view',
							handler : function() {
								var record = grid.getSelectionModel()
										.getSelected();
								if (record != null) {
									var reportCode = record.get("accountCode")+"_tz";
									
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

	/* 创建表格 */
	var grid = new Ext.grid.EditorGridPanel({
				// el : 'siteTeam',
				ds : ds,
				cm : cm,
				sm : sm,
				tbar : tbar,
				// title : '用户列表',
				autoWidth : true,
				fitToFrame : true,
				border : false,
				clicksToEdit : 1,// 单击一次编辑
				bbar : new Ext.PagingToolbar({
							pageSize : 18,
							store : ds,
							displayInfo : true,
							displayMsg : "显示第{0}条到{1}条，共{2}条",
							emptyMsg : "没有记录",
							beforePageText : '',
							afterPageText : ""
						}),
				viewConfig : {
					forceFit : false
				}

			});

	// 设定布局器及面板
	var viewport = new Ext.Viewport({
				layout : 'fit',
				fitToFrame : true,
				items : [grid]
			});

})