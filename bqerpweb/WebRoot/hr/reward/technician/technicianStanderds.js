Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var id = "";
	var count = null;
	var isHave = 0;
	var rowCount = 0;

	function ChangeDateToString(DateIn) {
		var Year = 0;
		var Month = 0;
		var CurrentDate = "";
		// 初始化时间
		Year = DateIn.getYear();
		Month = DateIn.getMonth() + 1;
		CurrentDate = Year + "-";
		if (Month >= 10) {
			CurrentDate = CurrentDate + Month;
		} else {
			CurrentDate = CurrentDate + "0" + Month;
		}
		return CurrentDate;
	}

	function StringToDate(DateStr) {
		var converted = Date.parse(DateStr);
		var myDate = new Date(converted);
		if (isNaN(myDate)) {
			var arys = DateStr.split('-');
			myDate = new Date(arys[0], arys[1]);
		}
		return myDate;
	}

	function checkTime1() {
		var startdate1 = this.value;
		startdate2 = startdate1.substring(0, 10);

		var enddate1 = grid.getSelectionModel().getSelected()
				.get("effectEndTime");
		if (enddate1 != null && enddate1 != "") {
			enddate2 = enddate1.substring(0, 10);

			if (startdate2 != "") {
				if (startdate2 > enddate2 && enddate2 != "") {
					Ext.Msg.alert("提示", "开始日期必须早于结束日期");
					return;
				}
			}
		}

		grid.getSelectionModel().getSelected().set("effectStartTime",
				startdate2);
	}
	function checkTime2() {
		var endtime1 = this.value;
		var endtime2 = endtime1.substring(0, 10);

		var beginTime1 = grid.getSelectionModel().getSelected()
				.get("effectStartTime");
		if (beginTime1 != null && beginTime1 != "") {
			beginTime2 = beginTime1.substring(0, 10);

			if (endtime2 != "" && beginTime2 != "") {
				if (endtime2 < beginTime2 && endtime2 != "") {
					Ext.Msg.alert("提示", "结束日期必须晚于开始日期");
					return;
				}
			}
		}
		grid.getSelectionModel().getSelected().set("effectEndTime", endtime2);
	}
	// 定义grid
	var MyRecord = Ext.data.Record.create([{
				name : 'techId',
				mapping : 0
			}, {
				name : 'techStandard',
				mapping : 1
			}, {
				name : 'isEmploy',
				mapping : 2
			}, {
				name : 'effectStartTime',
				mapping : 3
			}, {
				name : 'effectEndTime',
				mapping : 4
			}, {
				name : 'memo',
				mapping : 5
			}]);

	var dataProxy = new Ext.data.HttpProxy({
				url : 'hr/getTechnicianList.action'
			});

	var theReader = new Ext.data.JsonReader({
				root : "list",
				totalProperty : "totalCount"
			}, MyRecord);

	var store = new Ext.data.Store({
				proxy : dataProxy,
				reader : theReader
			});
	store.on('load', function() {
				count = store.getCount();
			})

	function queryRecord() {
		store.load({
					params : {
						start : 0,
						limit : 18
					}
				});
		rowCount = 0;
	}

	var sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			});

	var grid = new Ext.grid.EditorGridPanel({
		region : "center",
		layout : 'fit',
		store : store,
		columns : [sm, new Ext.grid.RowNumberer({
							header : '序号',
							width : 35
						}), {
					header : "ID",
					width : 75,
					sortable : true,
					dataIndex : 'techId',
					hidden : true
				}, {
					header : "技师标准",
					width : 75,
					sortable : true,
					dataIndex : 'techStandard',
					editor : new Ext.form.NumberField({})
				}, {
					header : "是否聘用",
					sortable : true,
					dataIndex : 'isEmploy',
					editor : new Ext.form.ComboBox({
								id : "isEmploy",
								fieldLabel : '是否聘用',
								store : new Ext.data.SimpleStore({
											fields : ['value', 'text'],
											data : [['Y', '是'], ['N', '否']]
										}),
								name : 'isEmploy',
								valueField : "value",
								displayField : "text",
								mode : 'local',
								typeAhead : true,
								hiddenName : 'repair.repairResult',
								triggerAction : 'all',
								selectOnFocus : true,
								value : 'Y',
								listeners : {
									'select' : function(combox, record, index) {
										var isEmploya = null;
										var isEndTime = null;
										for (var i = 0; i < store.getCount()
												- 1; i++) {
											isEmploya = store.getAt(i)
													.get('isEmploy')
											if (isEmploya == record
													.get('value')) {
												if (isEndTime == null) {
													isEndTime = store
															.getAt(i)
															.get('effectEndTime')
												}
												if (isEndTime < store.getAt(i)
														.get('effectEndTime')) {
													isEndTime = store
															.getAt(i)
															.get('effectEndTime')
												}
											}
										}
										if (isEndTime != null
												&& isEndTime != "") {
											var record = store.getAt(grid
													.getStore().getCount()
													- 1);
											record.set('effectStartTime',
													isEndTime);
											isHave = 1
										} else {
											var record = store.getAt(grid
													.getStore().getCount()
													- 1);
											record.set('effectStartTime',
													isEndTime);
											isHave = 0
										}
									}
								}

							}),
					renderer : function(val) {
						if (val == 'Y') {
							return "是";
						} else if (val == 'N') {
							return "否";
						}
					}

				}, {
					header : "生效开始时间",
					width : 75,
					dataIndex : 'effectStartTime',
					renderer : function(v) {
						if (v != null && v.length > 10) {
							return v.substr(0, 10);
						} else {
							return v;
						}
					},
					editor : new Ext.form.TextField({
								allowBlank : false,
								style : 'cursor:pointer',
								readOnly : true,
								listeners : {
									focus : function() {
										if (isHave == 0) {
											WdatePicker({
														// 时间格式
														startDate : '%y-%M-%d ',
														dateFmt : 'yyyy-MM-dd',
														alwaysUseStartDate : false,
														onpicked : checkTime1
													});
										}

									}
								}
							})
				}, {
					header : "生效结束时间",
					width : 75,
					dataIndex : 'effectEndTime',
					renderer : function(v) {
						if (v != null && v.length > 10) {
							return v.substr(0, 10);
						} else {
							return v;
						}
					},
					editor : new Ext.form.TextField({
								style : 'cursor:pointer',
								readOnly : true,
								listeners : {
									focus : function() {
										WdatePicker({
													// 时间格式
													startDate : '%y-%M-%d',
													dateFmt : 'yyyy-MM-dd',
													alwaysUseStartDate : false,
													onpicked : checkTime2
												});
									}
								}
							})
				}, {
					header : "备注",
					width : 75,
					sortable : true,
					dataIndex : 'memo',
					editor : new Ext.form.TextField({
								allowBlank : false
							})
				}],
		sm : sm,
		autoSizeColumns : true,
		viewConfig : {
			forceFit : true
		},
		tbar : [{
					text : "新增",
					iconCls : 'add',
					handler : addRecord
				}, {
					text : "保存",
					iconCls : 'update',
					handler : updateRecord
				}, {
					text : "删除",
					iconCls : 'delete',
					handler : deleteRecord
				}, {
					text : "刷新",
					iconCls : 'reflesh',
					handler : queryRecord
				}],
		// 分页
		bbar : new Ext.PagingToolbar({
					pageSize : 18,
					store : store,
					displayInfo : true,
					displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
					emptyMsg : "没有记录"
				})
	});
	grid.on('beforeedit', function(e) {
				if (e.record.get('techId') != grid.getStore().getAt(count - 1)
						.get("techId"))
					return false;

			});

	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				items : [grid]
			});

	// -------------------

	function addRecord() {
		count = grid.getStore().getTotalCount() + 1;
		rowCount++;
		if (rowCount <= 1) {
			var count1 = store.getCount();
			var currentIndex = count1;
			var o = new MyRecord({
						'techId' : '',
						'techStandard' : '',
						'isEmploy' : '',
						'effectStartTime' : '',
						'effectEndTime' : '',
						'memo' : ''

					});
			grid.stopEditing();
			store.insert(currentIndex, o);
			sm.selectRow(currentIndex);
			grid.startEditing(currentIndex, 1);
		} else {
			Ext.Msg.alert("提示", "每次只能修改一条技师标准!</br>");
			store.reload();
			rowCount = 0;
			return;
		}

	}

	function updateRecord() {
		var alertMsg = "";
		var modifyRec = grid.getStore().getModifiedRecords();
		grid.stopEditing();
		if (modifyRec.length > 0) {
			Ext.Msg.confirm('提示', '确定要保存修改数据吗?', function(button) {
						if (button == 'yes') {
							var isUpdate = new Array();
							for (var i = 0; i < modifyRec.length; i++) {
								if (modifyRec[i].get("techStandard") == null
										|| modifyRec[i].get("techStandard") == "") {
									alertMsg += "技师标准不能为空</br>";
									Ext.Msg.alert("提示", alertMsg);
									return;
								}
								if (modifyRec[i].get("isEmploy") == null
										|| modifyRec[i].get("isEmploy") == "") {
									alertMsg += "请选择是否聘用</br>";
									Ext.Msg.alert("提示", alertMsg);
									return;
								}
								if (modifyRec[i].get("effectStartTime") == null
										|| modifyRec[i].get("effectStartTime") == "") {
									alertMsg += "请选择生效开始时间</br>";
									Ext.Msg.alert("提示", alertMsg);
									return;
								}
								if (modifyRec[i].get("effectEndTime") == null
										|| modifyRec[i].get("effectEndTime") == "") {
									alertMsg += "请选择生效结束时间</br>";
									Ext.Msg.alert("提示", alertMsg);
									return;
								}
								if (modifyRec[i].get("effectEndTime") < modifyRec[i]
										.get("effectStartTime")) {
									alertMsg += "生效结束时间必须晚于生效开始时间</br>";
									Ext.Msg.alert("提示", alertMsg);
									return;
								}
								isUpdate.push(modifyRec[i].data);

							}
							Ext.Ajax.request({
										url : 'hr/saveTechicianList.action',
										method : 'post',
										params : {
											isUpdate : Ext.util.JSON
													.encode(isUpdate)

										},
										success : function(form, options) {
											var obj = Ext.util.JSON
													.decode(form.responseText)
											Ext.MessageBox.alert('提示信息',
													'保存成功！')
											store.rejectChanges();
											store.reload();
											rowCount = 0;
										},
										failure : function(result, request) {
											Ext.MessageBox.alert('提示信息',
													'操作失败！')
										}
									})
						} else {
							store.rejectChanges();
							store.reload();

						}
					})
		} else {
			Ext.Msg.alert("提示", "没有做任何修改！");
			store.rejectChanges();
			rowCount = 0;
			store.reload();
		}

	}

	function deleteRecord() {
		var selected = grid.getSelectionModel().getSelections();
		var ids = [];
		var names = [];
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录!");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i].data;
				if (member.techId
						&& (member.techId != grid.getStore().getAt(count - 1)
								.get("techId"))) {
					Ext.Msg.alert("提示", "只能删除最后一条记录！");
					store.rejectChanges();
					store.reload();
					return;
				} else {
					ids.push(member.techId);
				}

			}
			if (ids == "" || ids == null) {
				store.rejectChanges();
				store.reload();
				return;
			}

			if (ids.length > 0) {
				Ext.Msg.confirm("提示", "是否确定删除所选记录？", function(buttonobj) {
							if (buttonobj == "yes") {
								Ext.lib.Ajax.request('POST',
										'hr/deleteTechicianList.action', {
											success : function(action) {
												Ext.Msg.alert("提示", "删除成功！")
												store.reload();
											},
											failure : function() {
												Ext.Msg.alert('错误',
														'删除时出现未知错误.');
											}
										}, 'ids=' + ids);
							} else {
								store.reload();
							}
						});
			}
		}

	}

	queryRecord();
});