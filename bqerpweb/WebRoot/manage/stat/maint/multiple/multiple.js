Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	// 将时间转成字符串格式
	function ChangeDateToString(DateIn) {
		var Year = 0;
		var Month = 0;
		var Day = 0;
		var CurrentDate = "";
		// 初始化时间
		Year = DateIn.getYear();
		alert(Year)
		Month = DateIn.getMonth() + 1;
		Day = DateIn.getDate();
		CurrentDate = Year + "-";
		if (Month >= 10) {
			CurrentDate = CurrentDate + Month + "-";
		} else {
			CurrentDate = CurrentDate + Month + "-";
		}
		if (Day >= 10) {
			CurrentDate = CurrentDate + Day;
		} else {
			CurrentDate = CurrentDate + Day;
		}

		return CurrentDate;
	}
	// 指标store
	var url = "manager/getItemListToUse.action";
	var conn = Ext.lib.Ajax.getConnectionObject().conn;
	conn.open("POST", url, false);
	conn.send(null);
	var planLaborCode_data = eval('(' + conn.responseText + ')');

	var mystore = new Ext.data.SimpleStore({
				fields : ['planLaborName', 'planLaborCode'],
				data : planLaborCode_data
			});

	var MyRecord = Ext.data.Record.create([{
				name : 'itemCodeAdd'
			}, {
				name : 'tableCodeAdd'
			}, {
				name : 'baseInfo.id.itemCode'
			}, {
				name : 'baseInfo.id.tableCode'
			}, {
				name : 'baseInfo.tableName'
			}, {
				name : 'baseInfo.maxTableValue'
			}, {
				name : 'baseInfo.multiple'
			}, {
				name : 'baseInfo.startValue'
			}, {
				name : 'baseInfo.endValue'
			}, {
				name : 'baseInfo.fixDate'
			}, {
				name : 'baseInfo.endDate'
			}, {
				name : 'baseInfo.ifUsed'
			}]);

	var dataProxy = new Ext.data.HttpProxy({
				url : 'manager/getBpCStatItemList.action '
			});

	var theReader = new Ext.data.JsonReader({
				root : "list",
				totalProperty : "totalCount"

			}, MyRecord);

	var store = new Ext.data.Store({

				proxy : dataProxy,

				reader : theReader

			});

	// 分页
	store.load({
				params : {
					start : 0,
					limit : 18
				}
			});

	var sm = new Ext.grid.CheckboxSelectionModel();

	number = new Ext.grid.RowNumberer({
				header : "",
				align : 'left'
			})

	// 重置排序号
	function resetLine() {
		for (var j = 0; j < store.getCount(); j++) {
			var temp = store.getAt(j);
			temp.set("displayNo", j + 1);
		
		}
	}
	// 增加
	function addRecord() {
		 var currentRecord = grid.getSelectionModel().getSelected();
		var rowNo = store.indexOf(currentRecord);
	

		
		var count = store.getCount();
		

		var currentIndex = currentRecord ? rowNo  : count;
		var o = new MyRecord({
					'baseInfo.id.itemCode' : '0',
					'baseInfo.id.tableCode' : '0',
					'baseInfo.maxTableValue' : '0',
					'baseInfo.multiple' : '0.00',
					'baseInfo.startValue' : '0',
					'baseInfo.endValue' : '0',
					'baseInfo.fixDate' : '0000-00-00',
					'baseInfo.endDate' : '0000--00-00',
					'baseInfo.ifUsed' : '1'
				});

		grid.stopEditing();
		store.insert(currentIndex, o);
		sm.selectRow(currentIndex);
		grid.startEditing(currentIndex, 3);
		 resetLine();
	}



	// 删除记录

	var ids = new Array();
	function deleteRecords() {
		var sm = grid.getSelectionModel();
		var selections = sm.getSelections();
		if (selections.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selections.length; i += 1) {
				var member = selections[i];
				var id = new Array();
				if (member.get("itemCodeAdd") != null
						&& member.get("tableCodeAdd")) {
					id.push(member.get("itemCodeAdd"));
					id.push(member.get("tableCodeAdd"));
					var idstr = '';
					idstr = id.join(",");

					ids.push(idstr);
				}
				grid.getStore().remove(member);
				grid.getStore().getModifiedRecords().remove(member);
			}
			// resetLine();
		}
	}
	// 保存
	function saveModifies() {
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
				updateData.push(modifyRec[i].data);
				// }
			}

			Ext.Ajax.request({
						url : 'manager/saveBpCMetricTable.action',
						method : 'post',
						params : {
							// isAdd : Ext.util.JSON.encode(newData),
							isUpdate : Ext.util.JSON.encode(updateData),

							isDelete : ids.join(";")
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

	// 定义grid
	var grid = new Ext.grid.EditorGridPanel({
				region : "center",
				store : store,
				columns : [sm, // 选择框
						number,

						{
							header : "指标编码",
							width : 100,
							sortable : false,
							dataIndex : 'baseInfo.id.itemCode',
							renderer : function changeIt(val) {
								for (i = 0; i < mystore.getCount(); i++) {
									if (mystore.getAt(i).get("planLaborCode") == val)
										return mystore.getAt(i)
												.get("planLaborName");
								}

							},
							editor : new Ext.form.ComboBox({
										id : "planLaborCode",
										fieldLabel : '工种',
										name : 'planLaborCode',
										allowBlank : false,
										blankText : '工种...',
										anchor : '100%',
										store : mystore,

										displayField : 'planLaborName',
										valueField : 'planLaborCode',
										mode : 'local',
										value : '',
										readOnly : true,
										triggerAction : 'all'
									})

						}, {
							header : "表值编码",
							width : 100,
							sortable : false,
							dataIndex : 'baseInfo.id.tableCode',
							editor : new Ext.form.TextField({
									// id : "repairmodeName",
									// xtype : "textfield",
									// fieldLabel : '名称',
									// allowBlank : false,
									// width : wd,
									// name : 'baseInfo.repairmodeName'
									})
						}, {
							header : "表码最大值",
							width : 100,
							sortable : false,
							dataIndex : 'baseInfo.maxTableValue',
							editor : new Ext.form.NumberField({
									// id : "repairmodeName",
									// xtype : "textfield",
									// fieldLabel : '名称',
									// allowBlank : false,
									// width : wd,
									// name : 'baseInfo.repairmodeName'
									})
						}, {
							header : "倍率",
							width : 100,
							sortable : false,
							dataIndex : 'baseInfo.multiple',
							editor : new Ext.form.NumberField({
								value : "0.00"
									// id : "repairmodeName",
									// xtype : "textfield",
									// fieldLabel : '名称',
									// allowBlank : false,
									// width : wd,
									// name : 'baseInfo.repairmodeName'
								})
						}, {
							header : "初始值",
							width : 100,
							sortable : false,
							dataIndex : 'baseInfo.startValue',
							editor : new Ext.form.NumberField({
									// id : "repairmodeName",
									// xtype : "textfield",
									// fieldLabel : '名称',
									// allowBlank : false,
									// width : wd,
									// name : 'baseInfo.repairmodeName'
									})
						}, {
							header : "终止值",
							width : 100,
							sortable : false,
							dataIndex : 'baseInfo.endValue',
							editor : new Ext.form.NumberField({
									// id : "orderby",
									// fieldLabel : '排序号',
									// allowBlank : true,
									// width : wd,
									// name : 'baseInfo.orderby'
									})
						}, {
							header : "安装日期",
							width : 100,
							sortable : false,
							dataIndex : 'baseInfo.fixDate',
							renderer :

							function(value) {
								if (value != null) {
									if (typeof(value) == 'object') {
										value = Ext.encode(value)

										var myYear = parseInt(value.substring(
												1, 5));
										var myMonth = parseInt(value.substring(
												6, 8));
										var myDay = parseInt(value.substring(9,
												11));
										return myYear + '-' + myMonth + '-'
												+ myDay;

									} else {

										var myYear = parseInt(value.substring(
												0, 4));
										var myMonth = parseInt(value.substring(
												5, 7));
										var myDay = parseInt(value.substring(8,
												10));
										return myYear + '-' + myMonth + '-'
												+ myDay;

									}
								} else
									return "";
							},

							editor : new Ext.form.DateField({

							})
						}, {
							header : "终止日期",
							width : 100,
							sortable : false,
							dataIndex : 'baseInfo.endDate',
							renderer :

							function(value) {
								if (value != null) {
									if (typeof(value) == 'object') {
										value = Ext.encode(value)

										var myYear = parseInt(value.substring(
												1, 5));
										var myMonth = parseInt(value.substring(
												6, 8));
										var myDay = parseInt(value.substring(9,
												11));
										return myYear + '-' + myMonth + '-'
												+ myDay;

									} else {

										var myYear = parseInt(value.substring(
												0, 4));
										var myMonth = parseInt(value.substring(
												5, 7));
										var myDay = parseInt(value.substring(8,
												10));
										return myYear + '-' + myMonth + '-'
												+ myDay;

									}
								} else
									return "";

							},

							editor : new Ext.form.DateField({

							})
						}, {
							header : "是否可用",
							width : 80,
							align : "center",
							sortable : true,
							dataIndex : 'baseInfo.ifUsed',
							renderer : function changeIt(val) {
								if (val == "1") {
									return "是";
								} else if (val == "0") {
									return "否";
								} else {
									return "状态异常";
								}
							},
							editor : new Ext.form.ComboBox({

										store : new Ext.data.SimpleStore({
													fields : ["retrunValue",
															"displayText"],
													data : [['1', '是'],
															['0', '否']]
												}),
										valueField : "retrunValue",
										displayField : "displayText",
										mode : 'local',
										forceSelection : true,
										blankText : '工具',
										emptyText : '工具',
										// hiddenName : 'baseInfo.planToolCode',
										// value : '',

										editable : false,
										triggerAction : 'all',
										selectOnFocus : true,
										allowBlank : false,
										// name : 'baseInfo.planToolCode2',
										anchor : '99%'

									})

						}],
				tbar : [{
							text : "新增",
							iconCls : 'add',

							handler : addRecord
						}, {
							text : "删除",
							iconCls : 'delete',
							handler : deleteRecords

						}, {
							text : "保存",
							iconCls : 'save',
							handler : saveModifies
						}, {
							text : "取消",
							iconCls : 'cancer',
							handler : cancel
						}],
				sm : sm, // 选择框的选择 Shorthand for selModel（selectModel）
				// 分页
				bbar : new Ext.PagingToolbar({
							pageSize : 18,
							store : store,
							displayInfo : true,
							displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
							emptyMsg : "没有记录"
						})
			});



	new Ext.Viewport({
				enableTabScroll : true,
				layout : "fit",
				items : [grid]
			});
})