Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
//			var flagCenterId = '';
	// 预算部门主题ID
	var  flagCenterTopicId = null;
	// 预算部门ID
	var flagCenterId = null;

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
//							start : 0,
//							limit : 18
						}
					});
			// 左上列表
			var firstGrid = new Ext.grid.GridPanel({
						width : 260,
//						height : 300,
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
								},{
									header : "预算部门ID",
									width : 110,
									hidden : true,
									align : "center",
									sortable : true,
									dataIndex : 'top.centerId'
								}, {
									header : "部门名称",
									 width:120,
									align : "center",
									sortable : true,
									dataIndex : 'deptName'
								}, {
									header : "部门编码",
									 width:60,
									align : "center",
									sortable : true,
									dataIndex : 'deptCode'
								}],
						sm : firstsm,
						frame : true,
//						bbar : new Ext.PagingToolbar({
//									pageSize : 18,
//									store : firstStore,
//									displayInfo : true,
//									displayMsg : "{0} 到 {1} /共 {2} 条",
//									emptyMsg : "没有记录"
//								}),
						border : true
					});

			firstsm.on('rowselect', function() {
						if (firstsm.hasSelection()) {
//							flagCenterTopicId = firstsm.getSelected().get('top.centerTopicId');
							flagCenterId = firstsm.getSelected().get('top.centerId');
							secondStore.load({
										params : {
//											start : 0,
//											limit : 18,
											centerId : firstsm.getSelected()
														.get('top.centerId')
										}
									})
						}
					});

//		********************以下开始为第二grid*****************************			
			var secondsm = new Ext.grid.CheckboxSelectionModel({
						singleSelect : true
					});
			// 左上列表中的数据
			var secondRecord = new Ext.data.Record.create([{
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

			var secondStore = new Ext.data.JsonStore({
						url : 'managebudget/findDeptTopicByCode.action',
						root : 'list',
						totalProperty : 'totalCount',
						fields : secondRecord
					});

			// 左下列表
			var secondGrid = new Ext.grid.GridPanel({
						width : 260,
//						 height : 510,
						autoScroll : true,
						ds : secondStore,
						columns : [secondsm, new Ext.grid.RowNumberer({
											header : "行号",
											width : 31
										}), {
									header : "预算部门主题ID",
									width : 110,
									hidden : true,
									align : "center",
									sortable : true,
									dataIndex : 'top.centerTopicId'
								},{
									header : "预算部门ID",
									width : 110,
									hidden : true,
									align : "center",
									sortable : true,
									dataIndex : 'top.centerId'
								},{
									header : "预算主题ID",
									width : 110,
									hidden : true,
									align : "center",
									sortable : true,
									dataIndex : 'top.topicId'
								}, {
									header : "主题编码",
									 width:80,
									align : "center",
									sortable : true,
									dataIndex : 'topicCode'
								}, {
									header : "主题名称",
									 width:100,
									align : "center",
									sortable : true,
									dataIndex : 'topicName'
								}],
						sm : secondsm,
						frame : true,
//						bbar : new Ext.PagingToolbar({
//									pageSize : 18,
//									store : secondStore,
//									displayInfo : true,
//									displayMsg : "{0} 到 {1} /共 {2} 条",
//									emptyMsg : "没有记录"
//								}),
						border : true
					});

			secondsm.on('rowselect', function() {
						if (secondsm.hasSelection()) {
							flagCenterTopicId = secondsm.getSelected().get('top.centerTopicId');
							flagCenterId = secondsm.getSelected().get('top.centerId');
							thirdStore.load({
										params : {
//											start : 0,
//											limit : 18,
											centerTopicId : secondsm.getSelected()
														.get('top.centerTopicId')
										}
									})
						}
					});
					
// ******************以上为第二gird***********************************					
			var thirdsm = new Ext.grid.CheckboxSelectionModel();
			// 右边列表中的数据
			//modify by ypan 20100903
			var thirdRecord = new Ext.data.Record.create([{
						name : 'ccc.centerItemId'
					}, {
						name : 'ccc.centerTopicId'
					}, {
						name : 'ccc.itemId'
					}, {
						name : 'ccc.itemAlias'
					}, {
						name : 'ccc.dataSource'
					}, {
						name : 'ccc.displayNo'
					}, {
						name : 'ccc.isUse'
					}, {
						name : 'itemCode'
					}, {
						name : 'itemName'
					}, {
						name : 'ccc.dataType'
					},{
						name : 'ccc.masterMode'
					}]);

			var thirdStore = new Ext.data.JsonStore({
						url : 'managebudget/findDeptTopicItemList.action',
						root : 'list',
						totalProperty : 'totalCount',
						fields : thirdRecord
					});

			
			// 数据来源stroe
			var dataSourceStore = new Ext.data.SimpleStore({
						fields : ['id', 'name'],
						data : [['1', '编制录入'], ['2', '编制计算']
//						, ['3', '编制引用'],
//						['4', '部门分解']
								]
					})
           var dataTypeStore = new Ext.data.SimpleStore({
						fields : ['id', 'name'],
						data : [['1', '费用指标'], ['2', '财务指标']
								]
					})
					
			 var masterModeStore = new Ext.data.SimpleStore({
						fields : ['id', 'name'],
						data : [['1', '刚性控制'], ['2', '柔性控制']
								]
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
			var thirdGrid = new Ext.grid.EditorGridPanel({
				tbar : contbar,
				autoScroll : true,
				ds : thirdStore,
				layout : 'fit',
				columns : [thirdsm, new Ext.grid.RowNumberer({
							header : "行号",
								 width : 31
							}), {
							header : "预算部门指标ID",
							align : "left",
							hidden : true,
							sortable : true,
							dataIndex : 'ccc.centerItemId'
						}, {
							header : "预算部门主题id",
//							 width:100,
							align : "left",
							sortable : false,
							hidden : true,
							dataIndex : 'ccc.centerTopicId'
						}, {
							header : "指标id",
//							 width:100,
							align : "left",
							sortable : false,
							hidden : true,
							dataIndex : 'ccc.itemId'
						}, {
							header : "指标编码",
							 width:100,
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
							dataIndex : 'ccc.itemAlias'
						}, {
							header : "来源",
							 width:100,
							align : "left",
							sortable : false,
							dataIndex : 'ccc.dataSource',
							 css : CSS_GRID_INPUT_COL,
							editor : new Ext.form.ComboBox({
										store : dataSourceStore,
										displayField : 'name',
										valueField : 'id',
										mode : 'local',
										triggerAction : 'all',
										readOnly : true,
										allowBlank : false
									}),
							renderer : function(v) {
								if (v == 1) {
									return '编制录入';
								} else if (v == 2) {
									return '编制计算';
								} 
//								else if (v == 3) {
//									return '编制引用';
//								} else if (v == 4) {
//									return '部门分解';
//								} 
							}
						}, {
							header : "数据类别",
							 width:100,
							align : "left",
							sortable : false,
							dataIndex : 'ccc.dataType',
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
									return '费用指标';
								} else if (v == 2) {
									return '财务指标';
								} 
							}
						},{
							header : "控制模式",
							 width:100,
							align : "left",
							sortable : false,
							dataIndex : 'ccc.masterMode',
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
						},{
							header : "显示顺序",
							 width :100,
							align : "left",
							sortable : false,
							dataIndex : 'ccc.displayNo',
							 css : CSS_GRID_INPUT_COL,
							editor : new Ext.form.TextField({
										style : 'cursor:pointer',
										allowBlank : false
									})
						}],
				forceFit : true,
				sm : thirdsm,
				frame : true,
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

				
	var left = new Ext.Panel({
		region : "center",
		width : 260,
		autoScroll : true,
		containerScroll : true,
		layout : 'border',
		items : [{
			region : 'north',
			layout : 'fit',
			height : 290,
			items : [firstGrid]
		}, {
			region : 'center',
			layout : 'fit',
			items : [secondGrid]
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
							width : 270,
							autoScroll : true,
							containerScroll : true,
							layout : 'border',
							items : [left]
						}]
			});			

			thirdGrid.on('celldblclick', choseEdit);
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
							record.set('ccc.itemId', rec.itemId);
							record.set('itemCode', rec.itemCode);
							record.set('itemName', rec.itemName);
							record.set('ccc.itemAlias', rec.itemName);
						}
					}

				}
			}

			// 增加
			function addRec() {
				var displayNo = 0;
				if (thirdStore.getCount() > 0) {
					for (var i = 0; i <= thirdStore.getCount() - 1; i++) {
						if (thirdStore.getAt(i).get('ccc.displayNo') > displayNo) {
							displayNo = thirdStore.getAt(i).get('ccc.displayNo');
						}
					}
				}
				if (!firstsm.hasSelection()) {
					Ext.Msg.alert('提示信息', '请先选择部门！');
					return;
				}
				if (!secondsm.hasSelection()) {
					Ext.Msg.alert('提示信息', '请先选择主题！');
					return;
				}
				var count = thirdStore.getCount();
				var currentIndex = count;
			    //modify by ypan 20100903
				var o = new thirdRecord({
							'ccc.centerTopicId' : flagCenterTopicId,
							'ccc.itemId' : '',
							'ccc.itemAlias' : '',
							'ccc.dataSource' : 1,
							'ccc.dataType' :1,
							'ccc.masterMode' :1,
							'ccc.displayNo' : displayNo == 0 ? 1 : displayNo
									+ 3
						});
				thirdGrid.stopEditing();
				thirdStore.insert(currentIndex, o);
				thirdsm.selectRow(currentIndex);
				thirdGrid.startEditing(currentIndex, 1);

			}

			// 删除记录
			var ids = new Array();
			function deleteRec() {
				thirdGrid.stopEditing();
				var sm = thirdGrid.getSelectionModel();
				var selected = sm.getSelections();
				if (selected.length == 0) {
					Ext.Msg.alert("提示", "请选择要删除的记录！");
				} else {
					for (var i = 0; i < selected.length; i += 1) {
						var member = selected[i];
						if (member.get("ccc.centerItemId") != null) {
							ids.push(member.get("ccc.centerItemId"));
						}
						thirdGrid.getStore().remove(member);
						thirdGrid.getStore().getModifiedRecords().remove(member);
					}
				}
			}
			// 保存
	function saveRec() {
		thirdGrid.stopEditing();
		var modifyRec = thirdGrid.getStore().getModifiedRecords();
		if (modifyRec.length > 0 || ids.length > 0) {
			for (var i = 0; i < thirdStore.getCount() - 1; i++) {
				for (var j = i + 1; j <= thirdStore.getCount() - 1; j++) {
					if (thirdStore.getAt(i).get('ccc.centerTopicId') == thirdStore
							.getAt(j).get('ccc.centerTopicId')) {
						if (thirdStore.getAt(i).get('ccc.itemId') == thirdStore
								.getAt(j).get('ccc.itemId')) {
							Ext.Msg.alert('提示信息', '同一部门的同一主题的指标不可重复！')
							return;
						}
					}
					if (thirdStore.getAt(i).get('ccc.itemAlias') == thirdStore
							.getAt(j).get('ccc.itemAlias')) {
						Ext.Msg.alert('提示信息', '同一部门的同一主题的指标别名不可重复！')
						return;
					}
				}
			}

			Ext.Msg.confirm('提示信息', '确定要保存修改吗？', function(button) {
				if (button == 'yes') {
					var addData = new Array();
					var updateData = new Array();
					var itemIds = '';
					var dciIds = '';
					for (var i = 0; i < modifyRec.length; i++) {
						if (modifyRec[i].get('itemCode') == null
								|| modifyRec[i].get('itemCode') == "") {
							Ext.Msg.alert('提示信息', '指标不可为空，请选择！')
							return;
						}
						else
						{
							itemIds +=  modifyRec[i].get('ccc.itemId') + ',';
							if(modifyRec[i].get('ccc.centerItemId') == null
								|| modifyRec[i].get('ccc.centerItemId') == "")
							dciIds += 'null' + ',';
							else 
							dciIds += modifyRec[i].get('ccc.centerItemId') + ',';
						}
						
						if (modifyRec[i].get('ccc.isUse') == 'Y') {
							updateData.push(modifyRec[i].data);
						} else {
							addData.push(modifyRec[i].data);
						}
					}
					itemIds = itemIds.substring(0,itemIds.length - 1);
					dciIds = dciIds.substring(0,dciIds.length - 1);
					if(itemIds != '' && dciIds != '')
					{
						if(!judge(flagCenterId,itemIds,dciIds))
						{
							Ext.Msg.alert('提示信息','该指标已存在于其他部门或主题下！!')
							return;
						}
					}
					
					Ext.Ajax.request({
								url : 'managebudget/saveDeptTopicItem.action',
								method : 'post',
								params : {
									isAdd : Ext.util.JSON.encode(addData),
									isUpdate : Ext.util.JSON.encode(updateData),
									isDelete : ids.join(",")
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
			})

		} else {
			Ext.MessageBox.alert('提示信息', '没有做任何修改！')
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

	// 判断函数
	function judge(center,items,dciIds)
	{
		var conn = Ext.lib.Ajax.getConnectionObject().conn;
		conn.open("POST", 'managebudget/judgeDeptItem.action?centerId=' + center
						+ '&itemIds=' + items +'&dciIds=' + dciIds , false);
		conn.send(null);
		if (conn.status == "200") {
			var ob = eval('(' + conn.responseText + ')');

			if (ob.msg.indexOf('不允许') != -1) {
				return false;
			} else {
				return true;
			}	
		}
	}
});