Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {

	// 获得年份
	function getDate() {
		var Y;
		Y = new Date();
		Y = Y.getFullYear().toString(10);
		return Y;
	}
	// 获得月份
	function getMonth() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString() + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t;
		return s;
	}

	// 系统当前时间
	function getTime() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate();
		s += (t > 9 ? "" : "0") + t + " ";
		return s;
	}

	var firstsm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			});
	// 左上列表中的数据
	var firstRecord = new Ext.data.Record.create([{
				name : 'top.centerTopicId'
			}, {
				name : 'top.centerId'
			}, {
				name : 'top.topicId'
			}, {
				name : 'deptName'
			}, {
				name : 'topicName'
			}, {
				name : 'top.directManager'
			}, {
				name : 'directManageName'
			}, {
				name : 'topicCode'
			}, {
				name : 'deptCode'
			}]);

	var firstStore = new Ext.data.JsonStore({
				url : 'managebudget/findDeptTopicInMaint.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : firstRecord
			});

	firstStore.load({
				params : {
					isquery : '1'
				}
			});
	// 左上列表
	var firstGrid = new Ext.grid.GridPanel({
				width : 400,
				autoScroll : true,
				ds : firstStore,
				columns : [firstsm, new Ext.grid.RowNumberer({
									header : "行号",
									width : 31
								}), {
							header : "预算部门主题ID",
							width : 110,
							hidden : true,
							align : "center",
							sortable : true,
							dataIndex : 'top.centerTopicId'
						}, {
							header : "预算部门ID",
							width : 110,
							hidden : true,
							align : "center",
							sortable : true,
							dataIndex : 'top.centerId'
						}, {
							header : "部门名称",
							width : 120,
							align : "center",
							sortable : true,
							dataIndex : 'deptName'
						}, {
							header : "部门编码",
							width : 80,
							align : "center",
							sortable : true,
							dataIndex : 'deptCode'
						}, {
							header : "成本中心编码",
							width : 100,
							align : "center",
							sortable : true
						}],
				sm : firstsm,
				frame : true,
				border : true
			});

	firstsm.on('rowselect', function() {
				if (firstsm.hasSelection()) {
					thirdStore.load({
								params : {
									centerTopicId : firstsm.getSelected()
											.get('top.centerTopicId'),
									findmonth : getMonth.getValue(),
									centerId : firstsm.getSelected()
											.get('top.centerId')

								}
							})
				}
			});

	var thirdsm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			});
	// 右边列表中的数据
	var thirdRecord = new Ext.data.Record.create([{
				name : 'centerItemId'
			}, {
				name : 'centerTopicId'
			}, {
				name : 'itemId'
			}, {
				name : 'itemAlias'
			}, {
				name : 'dataSource'
			}, {
				name : 'displayNo'
			}, {
				name : 'isUse'
			}, {
				name : 'itemCode'
			}, {
				name : 'itemName'
			}, {
				name : 'financeName'
			}, {
				name : 'financeHappen'
			}, {
				name : 'budgetItemId'
			}, {
				name : 'budgetMakeId'
			}, {
				name : 'centerId'
			}, {
				name : 'topicId'
			}, {
				name : 'budgetTime'
			}]);

	var thirdStore = new Ext.data.JsonStore({
				url : 'managebudget/findDeptTopicBusinessList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : thirdRecord
			});

	// 月份
	var getMonth = new Ext.form.TextField({
		id : 'getMonth',
		allowBlank : true,
		readOnly : true,
		value : getMonth(),
		width : 100,
		listeners : {
			focus : function() {
				var format = 'yyyy-MM';
				WdatePicker({
							startDate : '%y-%M-%d',
							dateFmt : format,
							alwaysUseStartDate : false,
							onclearing : function() {
								planStartDate.markInvalid();
							},
							onpicked : function() {
								thirdStore.load({
											params : {
												centerTopicId : firstsm
														.getSelected()
														.get('top.centerTopicId'),
												findmonth : getMonth.getValue(),
												centerId : firstsm
														.getSelected()
														.get('top.centerId')

											}
										})
							}
						});
			}
		}
	});
	var contbar = new Ext.Toolbar({
				items : ['月份：', getMonth, '-', {
							id : 'btnAdd',
							iconCls : 'add',
							text : "获取财务数据",
							handler : ''
						}, '-', {
							id : 'btnSave',
							iconCls : 'save',
							text : "保存修改",
							handler : saveRec
						}, '-', {
							id : 'btnCancer',
							iconCls : 'cancer',
							text : "取消",
							handler : cancerRec
						}]

			});

	// 右边列表
	var thirdGrid = new Ext.grid.EditorGridPanel({
				tbar : contbar,
				autoScroll : true,
				ds : thirdStore,
				layout : 'fit',
				columns : [thirdsm, new Ext.grid.RowNumberer({
									header : "行号",
									width : 35
								}), {
							header : "预算部门ID",
							align : "left",
							hidden : true,
							sortable : true,
							dataIndex : 'centerId'
						}, {
							header : "预算主题id",
							align : "left",
							sortable : false,
							hidden : true,
							dataIndex : 'topicId'
						}, {
							header : "指标id",
							align : "left",
							sortable : false,
							hidden : true,
							dataIndex : 'itemId'
						}, {
							header : "指标编码",
							width : 100,
							align : "left",
							sortable : false,
							dataIndex : 'itemCode'
						}, {
							header : "指标名称",
							align : "left",
							sortable : false,
							hidden : true,
							dataIndex : 'itemName'
						}, {
							header : "指标别名",
							width : 100,
							align : "left",
							sortable : false,
							css : CSS_GRID_INPUT_COL,
							dataIndex : 'itemAlias'
						}, {
							header : "财务系统编码",
							width : 100,
							align : "left",
							sortable : false,
							dataIndex : 'financeName',
							css : CSS_GRID_INPUT_COL
						}, {
							header : "财务数据",
							width : 100,
							align : "left",
							sortable : false,
							dataIndex : 'financeHappen',
							css : CSS_GRID_INPUT_COL,
							editor : new Ext.form.TextField({
										style : 'cursor:pointer',
										allowBlank : false
									})
						}],
				forceFit : true,
				sm : thirdsm,
				frame : true,
				border : true,
				enableColumnHide : false,
				iconCls : 'icon-grid',
				clicksToEdit : 1,
				autoSizeColumns : true
			});

	var left = new Ext.Panel({
				region : "center",
				width : 260,
				autoScroll : true,
				containerScroll : true,
				layout : 'border',
				items : [{
							region : 'center',
							layout : 'fit',
							height : 290,
							items : [firstGrid]
						}]

			});

	var viewport = new Ext.Viewport({
				layout : 'border',
				items : [{
							region : 'center',
							split : true,
							layout : 'fit',
							collapsible : true,
							border : false,
							autoScroll : true,
							items : [thirdGrid]
						}, {
							region : "west",
							width : 370,
							autoScroll : true,
							containerScroll : true,
							layout : 'border',
							items : [left]
						}]
			});

	// 保存
	var modifyRecords = new Array();
	function saveRec() {
		thirdGrid.stopEditing();
		if (thirdStore.getCount() == 0) {
			Ext.Msg.alert('提示', '无数据进行保存！')
			return;
		}

		if (thirdStore.getAt(0).get('budgetMakeId') == null
				|| thirdStore.getAt(0).get('budgetMakeId') == '') {
			for (var i = 0; i < thirdStore.getCount(); i++) {
				thirdStore.getAt(i).set('budgetTime', getMonth.getValue());
				modifyRecords.push(thirdStore.getAt(i).data)
			}
			savaAction()
		} else {
			var modifyRec = thirdGrid.getStore().getModifiedRecords();
			if (modifyRec.length > 0) {
				Ext.Msg.confirm('提示', "确定要保存修改吗?", function(buttonId) {
							if (buttonId == 'yes') {
								Ext.Msg.wait("正在保存数据,请等待...");
								for (var i = 0; i < modifyRec.length; i++) {
									modifyRec[i].set('budgetTime', getMonth
													.getValue())
									modifyRecords.push(modifyRec[i].data);
								}
								savaAction()
							}
						})

			} else {
				Ext.MessageBox.alert('提示信息', '没有做任何修改！')
			}
		}
	}
	function savaAction() {
		if (modifyRecords.length > 0) {
			Ext.Ajax.request({
						url : 'managebudget/saveBudgetMake.action',
						method : 'post',
						params : {
							addOrUpdateRecords : Ext.util.JSON
									.encode(modifyRecords),
							isHappen : '1'
						},
						success : function(result, request) {
							Ext.Msg.alert('提示信息', '数据保存修改成功！')
							thirdStore.rejectChanges();
							ids = [];
							thirdStore.reload();
						},
						failure : function(result, request) {
							Ext.Msg.alert("提示信息", "数据保存修改失败！");
							thirdStore.rejectChanges();
							ids = [];
							thirdStore.reload();
						}
					})
		}
	}
	// 取消
	function cancerRec() {
		var modifyRec = thirdStore.getModifiedRecords();
		if (modifyRec.length > 0 || ids.length > 0) {
			Ext.Msg.confirm('提示信息', '确定要放弃修改吗？', function(button) {
						if (button == 'yes') {
							thirdStore.reload();
							thirdStore.rejectChanges();
							ids = [];
						}
					})
		} else {
			thirdStore.reload();
		}
	}
});