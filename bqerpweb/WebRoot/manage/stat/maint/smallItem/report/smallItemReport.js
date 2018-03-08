Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() { 
	var rec = Ext.data.Record.create([{
				name : 'reportId'
			}, {
				name : 'reportName'
			}, {
				name : 'dataType'
			}, {
				name : 'rowHeadName'
			}, {
				name : 'columnNum'
			}, {
				name : 'typeCode'
			}]);

	var ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'manager/findSmallItemReportList.action'
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

	var rn = new Ext.grid.RowNumberer({

	});
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
	var reportTypeStore = new Ext.data.SimpleStore({

				data : [['类型一', 'LXY'], ['节能监督', 'JNJD']],
				fields : ['name', 'value']
			});
	var reportTypeBox = new Ext.form.ComboBox({
				fieldLabel : '时间类型',
				store : reportTypeStore,

				name : 'reportTypeBox',
				valueField : "value",
				displayField : "name",
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
	var cm = new Ext.grid.ColumnModel([sm, rn, {
				header : '报表编码',
				width : 40,
				dataIndex : 'reportId',
				align : 'left'
			}, {
				header : '报表名称',
				dataIndex : 'reportName',
				align : 'left',
				editor : new Ext.form.TextField({
							allowBlank : false
						})
			}, {
				header : '时段类型',
				width : 40,
				dataIndex : 'dataType',
				align : 'left',
				editor : dataTimeTypeBox,
				renderer : function changeIt(val) {
					for (i = 0; i < dataTimeTypeStore.getCount(); i++) {
						if (dataTimeTypeStore.getAt(i).get("key") == val)
							return dataTimeTypeStore.getAt(i).get("value");
					}
				}
			}, {
				header : '报表类型',
				width : 40,
				dataIndex : 'typeCode',
				align : 'left',
				editor : reportTypeBox,
				renderer : function changeIt(val) {
					if (val != null && val != "") {
						return reportTypeStore.getAt(reportTypeStore.find(
								"value", val)).get("name");

					} else
						return "";

				}
			}, {
				header : '行头名称',
				dataIndex : 'rowHeadName',
				align : 'left',
				width : 40,
				editor : new Ext.form.TextField({
							allowBlank : false
						})
			}, {
				header : '每行显示列数',
				dataIndex : 'columnNum',
				width : 40,
				align : 'left',
				editor : new Ext.form.NumberField({
					allowBlank : false
						// maxLength : 100,
						// maxLengthText : '最多输入100个数字！'
					})
			}]);
	ds.load({
				params : {
					start : 0,
					limit : 18
				}
			});
	// 增加
	function addRecords() {
		var currentRecord = grid.getSelectionModel().getSelected();
		var rowNo = ds.indexOf(currentRecord);
		var count = ds.getCount();
		// var currentIndex = currentRecord ? rowNo : count;
		var o = new rec({
					'reportId' : '',
					'reportName' : '',
					'dataType' : '1',
					'rowHeadName' : '',
					'columnNum' : '',
					'typeCode' : ''
				});

		grid.stopEditing();
		ds.insert(count, o);
		sm.selectRow(count);
		grid.startEditing(count, 2);
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
				if (member.get("reportId") != null
						&& member.get("reportId") != "") {
					ids.push(member.get("reportId"));
				}
				grid.getStore().remove(member);
				grid.getStore().getModifiedRecords().remove(member);
			}
		}
	}
	// 保存
	function save() {
		grid.stopEditing();
		var modifyRec = grid.getStore().getModifiedRecords();
		if (modifyRec.length > 0 || ids.length > 0) {
			Ext.Msg.confirm('提示', '确定要保存修改吗?', function(b) {
				if (b == "yes") {
					var updateData = new Array();
					for (var i = 0; i < modifyRec.length; i++) {
						if (modifyRec[i].data.reportName == "") {
							Ext.MessageBox.alert('提示信息', '报表名称不能为空！')
							return
						}
						if (modifyRec[i].data.rowHeadName == "") {
							Ext.MessageBox.alert('提示信息', '行头名称不能为空！')
							return
						}
						if (modifyRec[i].data.columnNum == "") {
							Ext.MessageBox.alert('提示信息', '每行显示列数不能为空！')
							return
						}
						updateData.push(modifyRec[i].data);
					}
					Ext.Ajax.request({
								url : 'manager/smallItemReportMaint.action',
								method : 'post',
								params : {
									isUpdate : Ext.util.JSON.encode(updateData),
									isDelete : ids.join(",")
								},
								success : function(result, request) {
									var o = eval('(' + result.responseText
											+ ')');
									Ext.MessageBox.alert('提示信息', o.msg);
									ds.rejectChanges();
									ids = [];
									ds.reload();
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
	function cancer() {
		var modifyRec = ds.getModifiedRecords();
		if (modifyRec.length > 0 || ids.length > 0) {
			Ext.Msg.confirm('提示', '确定要放弃修改吗?', function(b) {
						if (b == "yes") {
							ds.reload();
							ds.rejectChanges();
							ids = [];
						}
					})
		} else {
			ds.reload();
			ds.rejectChanges();
			ids = [];
		}
	}
	// 行类型设置
	function timeSet() {
		var records = grid.selModel.getSelections();
		if (records.length == 1) {
			var rec = grid.getSelectionModel().getSelected();
			if (rec.get("reportId") != null && rec.get("reportId") != '') {
				var args = new Object();
				args.reportId = rec.get("reportId");
				args.reportName = rec.get("reportName");
				window.showModalDialog(
								"/power/manage/stat/maint/smallItem/report/rowNameSet.jsp",
								args,
								"dialogHeight:400px;dialogWidth:500px;center:yes;help:no;resizable:no;status:no;");
			} else
				Ext.Msg.alert('提示', '请先选择报表类型！')
		} else
			Ext.Msg.alert('提示', '请选择一条记录！')
	}

	var tbar = new Ext.Toolbar({
				items : [{
							id : 'btnAdd',
							iconCls : 'add',
							text : "新增",
							handler : addRecords

						}, '-', {
							id : 'btnDelete',
							iconCls : 'delete',
							text : "删除",
							handler : deleteRecords

						}, '-', {
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
							text : "行类型设置",
							handler : timeSet
						}, '-', {
							text : '权限设置',
							iconCls : 'view',
							handler : function() {
								var record = grid.getSelectionModel()
										.getSelected();
								if (record != null) {
									var reportCode = record.get("reportId")+"_xzb";
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
				ds : ds,
				cm : cm,
				sm : sm,
				tbar : tbar,
				viewConfig : {
					forceFit : true
				},

				width : Ext.getBody().getViewSize().width,

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
						})
			});

	// 设定布局器及面板
	var viewport = new Ext.Viewport({
				layout : 'fit',
				fitToFrame : true,
				items : [grid]
			});

})