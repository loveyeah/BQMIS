Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var rec = Ext.data.Record.create([{
				name : 'reportCode'
			}, {
				name : 'reportName'
			}, {
				name : 'reportType'
			}, {
				name : 'timeDelay'
			}, {
				name : 'timeUnit'
			}, {
				name : 'displayNo'
			}
//			, {
//				name : 'groupNature'
//			}
			]);

	var ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'manager/getBpCInputReportList.action'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : 'totalCount',
							root : 'list'
						}, rec)
			});

	ds.load({
				params : {

					start : 0,
					limit : 18
				}
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

	var timeUnitStore = new Ext.data.SimpleStore({
				root : 'list',
				data : [['1', '小时'], ['3', '天'], ['4', '月']],
				fields : ['name', 'value']
			})
	var timeUnitBox = new Ext.form.ComboBox({
				fieldLabel : '时间类型',
				store : timeUnitStore,
				id : 'timeUnitBox',
				name : 'timeUnitBox',
				valueField : "name",
				displayField : "value",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				// hiddenName : 'statItem.dataTimeType',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : true,
				emptyText : '请选择',
				anchor : '85%'
			});

	// 班组性质
//	var groupNature = new Ext.form.ComboBox({
//		fieldLabel : '班组性质',
//		store : [['1', '前夜班'],['2', '上午班'],['4', '下午班'], ['5', '后夜班']],
//		id : 'groupNature',
//		name : 'groupNature',
//		valueField : "id",
//		displayField : "name",
//		mode : 'local',
//		typeAhead : true,
//		forceSelection : true,
//		// hiddenName : 'statItem.itemStatType',
//		editable : false,
//		triggerAction : 'all',
//		selectOnFocus : true,
//		allowBlank : true,
//		emptyText : '请选择',
//		anchor : '85%'
//			// value : '1'
//		})

	var cm = new Ext.grid.ColumnModel([sm, rn, {
				header : '报表编码',
				width : 40,
				dataIndex : 'reportCode',
				align : 'left',
				editor : new Ext.form.TextField({
							readOnly : true
						})
			}, {
				header : '报表名称',
				dataIndex : 'reportName',
				align : 'left',
				editor : new Ext.form.TextField({
							allowBlank : false
						})
			}, {
				header : '時段类型',
				width : 40,
				dataIndex : 'reportType',
				align : 'left',
				editor : dataTimeTypeBox,
				renderer : function changeIt(val) {
					for (i = 0; i < dataTimeTypeStore.getCount(); i++) {
						if (dataTimeTypeStore.getAt(i).get("key") == val)
							return dataTimeTypeStore.getAt(i).get("value");
					}
				}
			}, {
				header : '录入延时',
				dataIndex : 'timeDelay',
				width : 40,
				align : 'left',
				editor : new Ext.form.NumberField({
							allowBlank : false,
							maxLength : 10,
							maxLengthText : '最多输入10个数字！'
						})
				// ,
		}	, {
				header : '延时单位',
				width : 40,
				dataIndex : 'timeUnit',
				align : 'left',
				editor : timeUnitBox,
				renderer : function changeIt(val) {
					for (i = 0; i < timeUnitStore.getCount(); i++) {
						if (timeUnitStore.getAt(i).get("name") == val)
							return timeUnitStore.getAt(i).get("value");
					}

				}
			}, {
				header : '显示顺序',
				width : 40,
				dataIndex : 'displayNo',
				align : 'left',
				editor : new Ext.form.NumberField({
							maxLength : 10
						})
			}
//			, {
//				header : '班组性质',
//				width : 40,
//				dataIndex : 'groupNature',
//				align : 'left',
//				editor : groupNature,
//				renderer : function changeIt(val) {
//				if(val == '2')
//				  return "上午班"
//				if(val == '4')
//				  return "下午班"
//				if(val == '1')
//				  return "前夜班"
//				if(val == '5')
//				  return "后夜班"
//				}
//			}
			]);

	// 重置排序号
	function resetLine() {
		for (var j = 0; j < ds.getCount(); j++) {
			var temp = ds.getAt(j);
			temp.set("displayNo", j + 1);

		}
	}
	// 增加
	function addRecords() {
		var currentRecord = grid.getSelectionModel().getSelected();
		var rowNo = ds.indexOf(currentRecord);

		var count = ds.getCount();

		// var currentIndex = currentRecord ? rowNo : count;

		var o = new rec({
					'reportCode' : '',
					'reportName' : '',
					'reportType' : '1',
					'timeDelay' : '',
					'timeUnit' : '',
					'displayNo' : ''
//					,
//					'groupNature' : ''
				});

		grid.stopEditing();
		ds.insert(count, o);
		sm.selectRow(count);
		grid.startEditing(count, 2);
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
				if (member.get("reportCode") != null) {
					ids.push(member.get("reportCode"));
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
				// if (modifyRec[i].get("id") == "") {
				// newData.push(modifyRec[i].data);
				// } else {
				if (modifyRec[i].data.reportName == "") {
					Ext.MessageBox.alert('提示信息', '录入报表名称不能为空！')
					return
				}
				if ((modifyRec[i].data.timeDelay == "") && modifyRec[i].data.timeDelay != "0") {
					Ext.MessageBox.alert('提示信息', '录入延时不能为空！')
					return
				}
				if (modifyRec[i].data.timeUnit == "") {
					Ext.MessageBox.alert('提示信息', '延时单位不能为空！')
					return
				}
//				if(modifyRec[i].data.reportType == '2' && modifyRec[i].data.groupNature == ""){
//				    Ext.MessageBox.alert('提示信息', '班组报表班组性质不能为空！')
//					return
//				}
				updateData.push(modifyRec[i].data);

			}

			Ext.Ajax.request({
						url : 'manager/saveBpCInputReport.action',
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
		var records = grid.selModel.getSelections();
		if (records.length == 1) {
			var rec = grid.getSelectionModel().getSelected();
			if(rec.get("reportType") == 2){
				Ext.Msg.alert('提示','班组指标无时间点设置!');
				return;
			}
			if (rec.get("reportType") != null && rec.get("reportType") != '') {
				window
						.showModalDialog(
								"/power/manage/stat/maint/reportName/dateTypeMaint.jsp?reportCode="
										+ rec.get("reportCode") + "&dateType="
										+ rec.get("reportType"),
								null,
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
							text : "时点设置",
							handler : timeSet
						}, '-', {
							text : '权限设置',
							iconCls : 'view',
							handler : function() {
								var record = grid.getSelectionModel()
										.getSelected();
								if (record != null) {
									var reportCode = record.get("reportCode")+"_lr";
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
				autoWidth : true,
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