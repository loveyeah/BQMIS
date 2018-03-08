Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

			var monthDate = new Ext.form.TextField({
						id : "monthDate",
						fieldLabel : '时间',
						width : 80,
						value : new Date().format('Y-m'),
						name : 'monthDate',
						listeners : {
							focus : function() {
								WdatePicker({
											startDate : '%y-%M',
											dateFmt : 'yyyy-MM',
											alwaysUseStartDate : true
										});
							}
						}
					});

			var detailDept = new Power.dept({
						anchor : '100%'
					});
			detailDept.btnConfrim.on("click", function() {
						if (myGrid.getSelectionModel().hasSelection()) {
							var rec = myGrid.getSelectionModel().getSelected();
							var aDept = detailDept.getValue();
							rec.set('deptId', aDept.key);
							rec.set('isUse', aDept.value);
						}
					});
			// 定义grid
			var MyRecord = Ext.data.Record.create([{
						name : 'coefficientId'
					}, {
						name : 'deptId'
					}, {
						name : 'quantifyCoefficient'
					}, {
						// 部门名称
						name : 'isUse'
					}]);

			var dataProxy = new Ext.data.HttpProxy({
						url : 'hr/getDeptQuantifyList.action'
					});

			var theReader = new Ext.data.JsonReader({
						root : "list",
						totalProperty : "totalCount"
					}, MyRecord);

			var store = new Ext.data.Store({
						proxy : dataProxy,
						reader : theReader
					});

			function queryRecord() {
				store.baseParams = {
					monthDate : monthDate.getValue()
				};
				store.load({
							params : {
								start : 0,
								limit : 18
							}
						});
			}

			var sm = new Ext.grid.CheckboxSelectionModel({
						singleSelect : false
					});
			var myGrid = new Ext.grid.EditorGridPanel({
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
									dataIndex : 'coefficientId',
									hidden : true
								}, {
									header : "部门ID",
									width : 75,
									sortable : true,
									dataIndex : 'deptId',
									hidden : true
								}, {
									header : "部门",
									width : 75,
									sortable : true,
									dataIndex : 'deptId',
									editor : detailDept.combo,
									renderer : function(value, metadata, record) {
										if (value != null)
											return record.get('isUse')
									}
								}, {
									header : "量化系数",
									width : 75,
									sortable : true,
									dataIndex : 'quantifyCoefficient',
									editor : new Ext.form.NumberField({
									         decimalPrecision: 4 //add by sychen 20100825
									})
								}],
						sm : sm,
						autoSizeColumns : true,
						viewConfig : {
							forceFit : true
						},
						tbar : ['查询时间:', monthDate, '-', {
									text : "查询",
									iconCls : 'reflesh',
									handler : queryRecord
								}, {
									text : "新增",
									iconCls : 'add',
									handler : addRecord
								}, {
									text : "修改",
									iconCls : 'update',
									handler : updateRecord
								}, {
									text : "删除",
									iconCls : 'delete',
									handler : deleteRecord
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

			new Ext.Viewport({
						enableTabScroll : true,
						layout : "border",
						items : [myGrid]
					});

			// -------------------

			function addRecord() {
				var count1 = store.getCount();
				var currentIndex = count1;
				var o = new MyRecord({
							'coefficientId' : '',
							'deptId' : '',
							'deptName' : '',
							'quantifyCoefficient' : ''
						});
				myGrid.stopEditing();
				store.insert(currentIndex, o);
				sm.selectRow(currentIndex);
				myGrid.startEditing(currentIndex, 1);
			}

			function updateRecord() {
				var alertMsg = "";
				var modifyRec = myGrid.getStore().getModifiedRecords();
				myGrid.stopEditing();
				if (modifyRec.length > 0) {
					Ext.Msg.confirm('提示', '确定要保存修改数据吗?', function(button) {
								if (button == 'yes') {
									var isUpdate = new Array();
									for (var i = 0; i < modifyRec.length; i++) {
										isUpdate.push(modifyRec[i].data);

									}
									Ext.Ajax.request({
												url : 'hr/saveDeptQuantifyList.action',
												method : 'post',
												params : {
													isUpdate : Ext.util.JSON
															.encode(isUpdate),
													monthDate : monthDate
															.getValue()

												},
												success : function(form,
														options) {
													var obj = Ext.util.JSON
															.decode(form.responseText)
													Ext.MessageBox.alert(
															'提示信息', '保存成功！')
													store.rejectChanges();
													store.reload();
												},
												failure : function(result,
														request) {
													Ext.MessageBox.alert(
															'提示信息', '操作失败！')
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
					store.reload();
				}

			}

			function deleteRecord() {
				var selected = myGrid.getSelectionModel().getSelections();
				var ids = [];
				if (selected.length == 0) {
					Ext.Msg.alert("提示", "请选择要删除的记录!");
				} else {
					for (var i = 0; i < selected.length; i += 1) {
						var member = selected[i].data;
						ids.push(member.coefficientId);
					}
					if (ids.length > 0) {
						Ext.Msg.confirm("提示", "是否确定删除所选记录？",
								function(buttonobj) {
									if (buttonobj == "yes") {
										Ext.lib.Ajax
												.request(
														'POST',
														'hr/deleteDeptQuantifyList.action',
														{
															success : function(
																	action) {
																Ext.Msg
																		.alert(
																				"提示",
																				"删除成功！")
																store.reload();
															},
															failure : function() {
																Ext.Msg
																		.alert(
																				'错误',
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