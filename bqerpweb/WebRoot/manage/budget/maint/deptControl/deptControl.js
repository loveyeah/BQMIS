Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
			var flagCenterId = '';

			var westsm = new Ext.grid.CheckboxSelectionModel({
						singleSelect : true
					});
			// 左边列表中的数据
			var westdatalist = new Ext.data.Record.create([{
						name : 'centerId'
					}, {
						name : 'depCode'
					}, {
						name : 'depName'
					}, {
						name : 'manager'
					}, {
						name : 'ifDuty'
					}, {
						name : 'manageName'
					}]);

			var westgrids = new Ext.data.JsonStore({
						url : 'managebudget/findDeptDutyList.action',
						root : 'list',
						totalProperty : 'totalCount',
						fields : westdatalist
					});

			westgrids.load({
						params : {
							start : 0,
							limit : 18
						}
					});

			// 左边列表
			var westgrid = new Ext.grid.GridPanel({
						width : 200,
						// height : 510,
						autoScroll : true,
						ds : westgrids,
						columns : [westsm, new Ext.grid.RowNumberer({
											header : "行号",
											width : 31
										}), {
									header : "责任中心ID",
									width : 110,
									hidden : true,
									align : "center",
									sortable : true,
									dataIndex : 'centerId'
								}, {
									header : "部门编码",
									 width:80,
									align : "center",
									sortable : true,
									dataIndex : 'depCode'
								}, {
									header : "部门名称",
									 width:100,
									align : "center",
									sortable : true,
									dataIndex : 'depName'
								}],
						sm : westsm,
						frame : true,
						bbar : new Ext.PagingToolbar({
									pageSize : 18,
									store : westgrids,
									displayInfo : true,
									displayMsg : "{0} 到 {1} /共 {2} 条",
									emptyMsg : "没有记录"
								}),
						border : true
					});

			westsm.on('rowselect', function() {
						if (westsm.hasSelection()) {
							flagCenterId = westsm.getSelected().get('centerId');
							eastgrids.load({
										params : {
//											start : 0,
//											limit : 18,
											centerId : westsm.getSelected()
													.get('centerId')
										}
									})
						}
					});

			var eastsm = new Ext.grid.CheckboxSelectionModel();
			// 右边列表中的数据
			var datalist = new Ext.data.Record.create([{
						name : 'ccm.masterId'
					}, {
						name : 'ccm.centerId'
					}, {
						name : 'ccm.itemId'
					}, {
						name : 'ccm.itemAlias'
					}, {
						name : 'ccm.masterMode'
					}, {
						name : 'ccm.dataType'
					}, {
						name : 'ccm.sysSource'
					}, {
						name : 'ccm.comeFrom'
					}, {
						name : 'ccm.displayNo'
					}, {
						name : 'ccm.isUse'
					}, {
						name : 'depCode'
					}, {
						name : 'depName'
					}, {
						name : 'itemCode'
					}, {
						name : 'itemName'
					}]);

			var eastgrids = new Ext.data.JsonStore({
						url : 'managebudget/getDeptConList.action',
						root : 'list',
						totalProperty : 'totalCount',
						fields : datalist
					});

			// 控制模式store
			var masterModeStore = new Ext.data.SimpleStore({
						fields : ['id', 'name'],
						data : [['1', '刚性控制'], ['2', '柔性控制']]
					})
			// 数据类别store
			var dataTypeStore = new Ext.data.SimpleStore({
						fields : ['id', 'name'],
						data : [['1', '技术经济指标'], ['2', '费用指标'], ['3', '财务指标']]
					})
			// 数据来源stroe
			var sysSourceStore = new Ext.data.SimpleStore({
						fields : ['id', 'name'],
						data : [['1', '计划统计'], ['2', '燃料管理'], ['3', '物资管理'],
								['4', '工程项目'], ['5', '人力资源'], ['6', '财务报销']]
					})
			// 产生方式store
			var comeFromStore = new Ext.data.SimpleStore({
						fields : ['id', 'name'],
						data : [['1', '录入'], ['2', '计算']]
					})

			var contbar = new Ext.Toolbar({
						items : [{
									id : 'btnAdd',
									iconCls : 'add',
									text : "新增",
									handler : addRec
								}, {
									id : 'btnDelete',
									iconCls : 'delete',
									text : "删除",
									handler : deleteRec

								}, {
									id : 'btnCancer',
									iconCls : 'cancer',
									text : "取消",
									handler : cancerRec

								}, '-', {
									id : 'btnSave',
									iconCls : 'save',
									text : "保存修改",
									handler : saveRec
								}]

					});

			// 右边列表
			var eastgrid = new Ext.grid.EditorGridPanel({
				tbar : contbar,
				autoScroll : true,
				ds : eastgrids,
				layout : 'fit',
				columns : [eastsm, new Ext.grid.RowNumberer({
							header : "行号",
								 width : 31
							}), {
							header : "预算控制ID",
							align : "left",
							hidden : true,
							sortable : true,
							dataIndex : 'ccm.masterId'
						}, {
							header : "预算部门id",
//							 width:100,
							align : "left",
							sortable : false,
							hidden : true,
							dataIndex : 'ccm.centerId'
						}, {
							header : "指标id",
//							 width:100,
							align : "left",
							sortable : false,
							hidden : true,
							dataIndex : 'ccm.itemId'
						}, {
							header : "指标编码",
							 width:70,
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
							 width:100,
							align : "left",
							sortable : false,
							 css : CSS_GRID_INPUT_COL,
							editor : new Ext.form.TextField({
										style : 'cursor:pointer',
										allowBlank : false
									}),
							dataIndex : 'ccm.itemAlias'
						}, {
							header : "控制模式",
							 width:90,
							align : "left",
							sortable : false,
							dataIndex : 'ccm.masterMode',
							 css : CSS_GRID_INPUT_COL,
							editor : new Ext.form.ComboBox({
										store : masterModeStore,
										displayField : 'name',
										valueField : 'id',
										mode : 'local',
										triggerAction : 'all',
										readOnly : true,
										allowBlank : false
									}),
							renderer : function(v) {
								if (v == 1) {
									return '刚性控制';
								} else if (v == 2) {
									return '柔性控制';
								}
							}
						}, {
							header : "数据类别",
							 width:90,
							align : "left",
							sortable : false,
							dataIndex : 'ccm.dataType',
							 css : CSS_GRID_INPUT_COL,
							editor : new Ext.form.ComboBox({
										store : dataTypeStore,
										displayField : 'name',
										valueField : 'id',
										mode : 'local',
										triggerAction : 'all',
										readOnly : true,
										allowBlank : false
									}),
							renderer : function(v) {
								if (v == 1) {
									return '技术经济指标';
								} else if (v == 2) {
									return '费用指标';
								} else if (v == 3) {
									return '财务指标';
								}
							}
						}, {
							header : "数据来源",
							 width:90,
							align : "left",
							sortable : false,
							dataIndex : 'ccm.sysSource',
							 css : CSS_GRID_INPUT_COL,
							editor : new Ext.form.ComboBox({
										store : sysSourceStore,
										displayField : 'name',
										valueField : 'id',
										mode : 'local',
										triggerAction : 'all',
										readOnly : true,
										allowBlank : false
									}),
							renderer : function(v) {
								if (v == 1) {
									return '计划统计';
								} else if (v == 2) {
									return '燃料管理';
								} else if (v == 3) {
									return '物资管理';
								} else if (v == 4) {
									return '工程项目';
								} else if (v == 5) {
									return '人力资源';
								} else if (v == 6) {
									return '财务报销';
								}
							}
						}, {
							header : "产生方式",
							 width:90,
							align : "left",
							sortable : false,
							dataIndex : 'ccm.comeFrom',
							 css : CSS_GRID_INPUT_COL,
							editor : new Ext.form.ComboBox({
										store : comeFromStore,
										displayField : 'name',
										valueField : 'id',
										mode : 'local',
										triggerAction : 'all',
										readOnly : true,
										allowBlank : false
									}),
							renderer : function(v) {
								if (v == 1) {
									return '录入';
								} else if (v == 2) {
									return '计算';
								}
							}
						}, {
							header : "显示顺序",
							 width :90,
							align : "left",
							sortable : false,
							dataIndex : 'ccm.displayNo',
							 css : CSS_GRID_INPUT_COL,
							editor : new Ext.form.TextField({
										style : 'cursor:pointer',
										allowBlank : false
									})
						}],
				forceFit : true,
				sm : eastsm,
				frame : true,
				width : Ext.getBody().getViewSize().width,
				// bbar : new Ext.PagingToolbar({
				// pageSize : 18,
				// store : eastgrids,
				// displayInfo : true,
				// displayMsg : "{0} 到 {1} /共 {2} 条",
				// emptyMsg : "没有记录"
				// }),
				border : true,
				enableColumnHide : false,
				iconCls : 'icon-grid',
				clicksToEdit : 1,
					 autoSizeColumns : true
				});

			new Ext.Viewport({
						enableTabScroll : true,
						layout : "border",
						items : [
							{
									region : 'west',
									layout : 'fit',
									width : 260,
									items : [westgrid]
								}, {
									region : 'center',
									layout : 'fit',
									items : [eastgrid]
								}
								]
					});

			eastgrid.on('celldblclick', choseEdit);
			function choseEdit(grid, rowIndex, columnIndex, e) {
				if (rowIndex <= grid.getStore().getCount() - 1) {
					var record = grid.getStore().getAt(rowIndex);
					var fieldName = grid.getColumnModel()
							.getDataIndex(columnIndex);
					if (fieldName == "itemCode") {
						var rec = window
								.showModalDialog(
										'../budgetItem/budgetItemTree.jsp',
										window,
										'dialogWidth=800px;dialogHeight=550px;status=no');
						if (typeof(rec) != "undefined") {
							record.set('ccm.itemId', rec.itemId);
							record.set('itemCode', rec.itemCode);
							record.set('itemName', rec.itemName);
							record.set('ccm.itemAlias', rec.itemName);
						}
					}

				}
			}

			// 增加
			function addRec() {
				var displayNo = 0;
				if (eastgrids.getCount() > 0) {
					for (var i = 0; i <= eastgrids.getCount() - 1; i++) {
						if (eastgrids.getAt(i).get('ccm.displayNo') > displayNo) {
							displayNo = eastgrids.getAt(i).get('ccm.displayNo');
						}
					}
				}
				if (!westsm.hasSelection()) {
					Ext.Msg.alert('提示信息', '请先选择部门！');
					return;
				}
				var count = eastgrids.getCount();
				var currentIndex = count;
				var o = new datalist({
							'ccm.centerId' : flagCenterId,
							'ccm.itemId' : '',
							'ccm.itemAlias' : '',
							'ccm.masterMode' : 1,
							'ccm.dataType' : 1,
							'ccm.sysSource' : 1,
							'ccm.comeFrom' : 1,
							'ccm.displayNo' : displayNo == 0 ? 1 : displayNo
									+ 3
						});
				eastgrid.stopEditing();
				eastgrids.insert(currentIndex, o);
				eastsm.selectRow(currentIndex);
				eastgrid.startEditing(currentIndex, 1);

			}

			// 删除记录
			var ids = new Array();
			function deleteRec() {
				eastgrid.stopEditing();
				var sm = eastgrid.getSelectionModel();
				var selected = sm.getSelections();
				if (selected.length == 0) {
					Ext.Msg.alert("提示", "请选择要删除的记录！");
				} else {
					for (var i = 0; i < selected.length; i += 1) {
						var member = selected[i];
						if (member.get("ccm.masterId") != null) {
							ids.push(member.get("ccm.masterId"));
						}
						eastgrid.getStore().remove(member);
						eastgrid.getStore().getModifiedRecords().remove(member);
					}
				}
			}
			// 保存
	function saveRec() {
		eastgrid.stopEditing();
		var modifyRec = eastgrid.getStore().getModifiedRecords();
		if (modifyRec.length > 0 || ids.length > 0) {
			for (var i = 0; i < eastgrids.getCount() - 1; i++) {
				for (var j = i + 1; j <= eastgrids.getCount() - 1; j++) {
					if (eastgrids.getAt(i).get('ccm.centerId') == eastgrids
							.getAt(j).get('ccm.centerId')) {
						if (eastgrids.getAt(i).get('ccm.itemId') == eastgrids
								.getAt(j).get('ccm.itemId')) {
							Ext.Msg.alert('提示信息', '同一部门内指标不可重复！')
							return;
						}
					}
					if (eastgrids.getAt(i).get('ccm.itemAlias') == eastgrids
							.getAt(j).get('ccm.itemAlias')) {
						Ext.Msg.alert('提示信息', '同一部门内指标别名不可重复！')
						return;
					}
				}
			}

			Ext.Msg.confirm('提示信息', '确定要保存修改吗？', function(button) {
				if (button == 'yes') {
					var addData = new Array();
					var updateData = new Array();
					for (var i = 0; i < modifyRec.length; i++) {
						if (modifyRec[i].get('itemCode') == null
								|| modifyRec[i].get('itemCode') == "") {
							Ext.Msg.alert('提示信息', '指标不可为空，请选择！')
							return;
						}
						if (modifyRec[i].get('ccm.isUse') == 'Y') {
							updateData.push(modifyRec[i].data);
						} else {
							addData.push(modifyRec[i].data);
						}

					}
					Ext.Ajax.request({
								url : 'managebudget/saveDeptControlInput.action',
								method : 'post',
								params : {
									isAdd : Ext.util.JSON.encode(addData),
									isUpdate : Ext.util.JSON.encode(updateData),
									isDelete : ids.join(",")
								},
								success : function(result, request) {
									Ext.Msg.alert('提示信息', '数据保存修改成功！')
									eastgrids.rejectChanges();
									ids = [];
									eastgrids.reload();
								},
								failure : function(result, request) {
									Ext.Msg.alert("提示信息", "数据保存修改失败！");
									eastgrids.rejectChanges();
									ids = [];
									eastgrids.reload();
								}
							})
				}
			})
		} else {
			Ext.MessageBox.alert('提示信息', '没有做任何修改！')
		}
	}
			// 取消
	function cancerRec() {
		var modifyRec = eastgrids.getModifiedRecords();
		if (modifyRec.length > 0 || ids.length > 0) {
			Ext.Msg.confirm('提示信息', '确定要放弃修改吗？', function(button) {
						if (button == 'yes') {
							eastgrids.reload();
							eastgrids.rejectChanges();
							ids = [];
						}
					})
		} else {
			eastgrids.reload();
		}
	}
});