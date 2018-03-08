Ext.onReady(function() {
	function getDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate() + 6;
		s += (t > 9 ? "" : "0") + t + " ";
		return s;
	}
	var repairStartTime = new Ext.form.TextField({
				name : "startTime",
				fieldLabel : '检修开始时间',
				style : 'cursor:pointer',
				readOnly : true,
				allowBlank : true,
				// value:getDate(),
				anchor : '80%',
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd',
									alwaysUseStartDate : true,
									isShowClear : false
								});
						this.blur();
					}
				}
			});
	var repairEndTime = new Ext.form.TextField({
				name : "endTime",
				fieldLabel : '检修结束时间',
				style : 'cursor:pointer',
				readOnly : true,
				allowBlank : true,
				anchor : '80%',
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd',
									alwaysUseStartDate : true,
									isShowClear : false
								});
						this.blur();
					}
				}
			});
	var sm = new Ext.grid.CheckboxSelectionModel();
	// grid列表数据源
	var Record = new Ext.data.Record.create([sm, {
				name : 'boilerName',
				mapping : 0
			}, {
				name : 'boilerType',
				mapping : 1
			}, {

				name : 'boilerRepairId',
				mapping : 2
			}, {
				name : 'boilerId',
				mapping : 3
			}, {
				name : 'taskSource',
				mapping : 4
			}, {
				name : 'repairRecord',
				mapping : 5
			}, {
				name : 'repairTime',
				mapping : 6
			}, {
				name : 'repairBy',
				mapping : 7
			}, {
				name : 'repairByName',
				mapping : 8
			}]);
	function query() {
		store.reload();
	}
	var gridTbar = new Ext.Toolbar({
				items : ['从', repairStartTime, '到', repairEndTime, {
							id : 'query',
							text : "查询",
							iconCls : 'query',
							handler : query
						}, '-', {
							id : 'add',
							text : "新增",
							iconCls : 'add',
							handler : addBoiler
						}, '-', {
							id : 'delete',
							text : "删除",
							iconCls : 'delete',
							handler : deleteBoiler
						}, '-', {
							id : 'saveButton',
							text : "保存",
							iconCls : 'save',
							handler : saveBoiler
						}, '-', {
							id : 'cancer',
							text : "取消",
							iconCls : 'cancer',
							handler : cancerChange
						}]
			});

	function cancerChange() {

		store.reload();
		store.rejectChanges();

	}

	function addBoiler() {

		var count = store.getCount();
		var currentIndex = count;
		var o = new Record({
					'boilerName' : '',
					'boilerType' : '',
					'taskSource' : '',
					'repairRecord' : '',
					'repairTime' : '',
					'repairBy' : ''

				});
		boilergrid.stopEditing();
		store.insert(currentIndex, o);
		sm.selectRow(currentIndex);
		boilergrid.startEditing(currentIndex, 1);

	}

	function saveBoiler() {
		var alertMsg = "";
		var modifyRec = boilergrid.getStore().getModifiedRecords();
		boilergrid.stopEditing();
		if (modifyRec.length > 0) {
			Ext.Msg.confirm('提示', '确定要保存修改数据吗?', function(button) {
				if (button == 'yes') {
					var updateData = new Array();
					var mainID = new Array();
					var detailID = new Array();
					var blockName = new Array();
					var type = new Array();
					for (var i = 0; i < modifyRec.length; i++) {
						if (modifyRec[i].get("boilerName") == null
								|| modifyRec[i].get("boilerName") == ""
								|| modifyRec[i].get("boilerName") == "undefined") {
							alertMsg += "设备名称不能为空</br>";
						}
						/*
						 * if (modifyRec[i].get("boilerType") == null ||
						 * modifyRec[i].get("boilerType") == "") { alertMsg +=
						 * "型号规格为空</br>"; }
						 */
						if (modifyRec[i].get("taskSource") == null
								|| modifyRec[i].get("taskSource") == "") {
							alertMsg += "检修来源不能为空</br>";
						}
						if (modifyRec[i].get("repairRecord") == null
								|| modifyRec[i].get("repairRecord") == "") {
							alertMsg += "检修记录不能为空</br>";
						}
						if (modifyRec[i].get("repairTime") == null
								|| modifyRec[i].get("repairTime") == "") {
							alertMsg += "检修时间不能为空</br>";
						}
						if (modifyRec[i].get("repairBy") == null
								|| modifyRec[i].get("repairBy") == "") {
							alertMsg += "检修人不能为空</br>";
						}
						if (alertMsg != "") {
							Ext.Msg.alert("提示", alertMsg);
							return;
						}
						updateData.push(modifyRec[i].data);
						blockName.push(modifyRec[i].get("boilerName"));
						type.push(modifyRec[i].get("boilerType"));

					}
					Ext.Ajax.request({
								url : 'security/saveBoilRepair.action',
								method : 'post',
								params : {
									blockName : blockName.join(","),
									type : type.join(","),
									isUpdate : Ext.util.JSON.encode(updateData)

								},
								success : function(form, options) {
									var obj = Ext.util.JSON
											.decode(form.responseText)
									Ext.MessageBox.alert('提示信息', '保存成功！')
									store.rejectChanges();
									store.reload();
								},
								failure : function(result, request) {
									Ext.MessageBox.alert('提示信息', '操作失败！')
								}
							})
				}
			})
		} else {
			Ext.Msg.alert("提示", "没有做任何修改！");
		}

	}
	function deleteBoiler() {
		var sm = boilergrid.getSelectionModel();
		var selected = sm.getSelections();
		var ids = [];
		var names = [];
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录!");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i].data;
				if (member.boilerRepairId) {
					ids.push(member.boilerRepairId);
				}
				// modofied by liuyi 20100426
				// store.remove(selected[i]);
			}

			if (ids.length > 0) {
				Ext.Msg.confirm("删除", "是否确定删除所选记录？", function(buttonobj) {
							if (buttonobj == "yes") {
								Ext.lib.Ajax.request('POST',
										'security/deleteBoilRepair.action', {
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

	var store = new Ext.data.JsonStore({
				url : 'security/getBoilRepair.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : Record
			});

	// 页面的Grid主体
	var boilergrid = new Ext.grid.EditorGridPanel({
		store : store,
		layout : 'fit',
		columns : [sm, new Ext.grid.RowNumberer({
							header : '行号',
							width : 35
						}),// 选择框
				{
					header : '设备名称',
					dataIndex : 'boilerName',
					align : 'left',
					width : 220,
					editor : new Ext.form.TextField({
						width : 100,
						displayField : 'text',
						valueField : 'id',
						listeners : {
							focus : function() {
								var args = {
									selectModel : 'single',
									rootNode : {
										id : "root",
										text : '锅炉设备树'
									},
									onlyLeaf : false
								};
								this.blur();
								var win = window
										.showModalDialog(
												'../../../run/securityproduction/BoilerRepair/blockAndType.jsp',
												args,
												'dialogWidth:'
														+ Constants.WIDTH_COM_EMPLOYEE
														+ 'px;dialogHeight:'
														+ Constants.HEIGHT_COM_EMPLOYEE
														+ 'px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
								if (typeof(win) != "undefined") {
									var record = boilergrid.getSelectionModel()
											.getSelected();

									record.set("boilerName", win.name);
									record.set("boilerType", win.type);
								}
							}
						}
					})

				}, {
					header : "型号规格",
					width : 100,
					align : 'center',
					sortable : true,
					dataIndex : 'boilerType'

				}, {
					header : "任务来源",
					width : 100,
					align : 'center',
					sortable : true,
					dataIndex : 'taskSource',
					editor : new Ext.form.ComboBox({
								id : 'source',
								readOnly : true,
								name : 'text',
								hiddenName : 'taskSource',
								mode : 'local',
								width : 70,
								value : "1",
								fieldLabel : '任务来源',
								triggerAction : 'all',
								store : new Ext.data.SimpleStore({
											fields : ['text', 'value'],
											data : [['A级检修', '1'],
													['B级检修', '2'],
													['C级检修', '3'], ['缺陷', '4']]
										}),
								valueField : 'value',
								displayField : 'text',
								anchor : "15%"
							}),
					renderer : function(val) {
						if (val == '1') {
							return "A级检修";
						} else if (val == '2') {
							return "B级检修";
						} else if (val == '3') {
							return "C级检修";
						} else if (val == '4') {
							return "缺陷";
						}
					}

				}, {
					header : '检修记录',
					dataIndex : 'repairRecord',
					align : 'left',
					width : 220,
					editor : new Ext.form.TextField({
								allowBlank : false
							})
				}, {
					header : '检修时间',
					dataIndex : 'repairTime',
					readOnly : true,
					width : 120,
					renderer : function(v) {
						if (v != null && v.length > 10) {
							return v.substr(0, 10);
						} else {
							return v;
						}
					},
					align : 'center',
					editor : new Ext.form.TextField({
								allowBlank : false,
								style : 'cursor:pointer',
								readOnly : true,
								listeners : {
									focus : function() {
										WdatePicker({
													// 时间格式
													startDate : '%y-%M-%d ',
													dateFmt : 'yyyy-MM-dd',
													alwaysUseStartDate : false,
													onpicked : function getTime() {
														var time = this.value;
														boilergrid
																.getSelectionModel()
																.getSelected()
																.set(
																		"repairTime",
																		time);
													}
												});

									}
								}
							})

				}, {
					header : '检修人',
					dataIndex : 'repairBy',
					align : 'center',
					readOnly : true,
					width : 80,
					renderer : function(value, metadata, record) {
						if (value != null) {
							return record.get("repairByName");
						}
					},
					editor : new Ext.form.TextField({
						width : 100,
						displayField : 'text',
						valueField : 'id',
						listeners : {
							focus : function() {
								var args = {
									selectModel : 'single',
									rootNode : {
										id : '0',
										text : Constants.POWER_NAME
									},
									onlyLeaf : false
								};
								this.blur();
								var win = window
										.showModalDialog(
												'../../../comm/jsp/hr/workerByDept/workerByDept.jsp',
												args,
												'dialogWidth:'
														+ Constants.WIDTH_COM_EMPLOYEE
														+ 'px;dialogHeight:'
														+ Constants.HEIGHT_COM_EMPLOYEE
														+ 'px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
								if (typeof(win) != "undefined") {
									var record = boilergrid.getSelectionModel()
											.getSelected();

									record.set("repairBy", win.workerCode);
									record.set("repairByName", win.workerName);
								}
							}
						}
					})

				}],

		sm : sm, // 选择框的选择
		tbar : gridTbar,
		// bbar:bbar,
		clicksToEdit : 1,
		viewConfig : {
			forceFit : true
		}
	});
	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				border : false,
				frame : false,
				items : [{
							bodyStyle : "padding: 1,1,1,0",
							region : "center",
							border : false,
							frame : false,
							layout : 'fit',
							items : [boilergrid]
						}]
			});
	store.on('beforeload', function() {
				Ext.apply(this.baseParams, {
							startTime : repairStartTime.getValue(),
							endTime : repairEndTime.getValue(),
							isMaint : '1'//add by ltong

						});
			});
	store.load();
})