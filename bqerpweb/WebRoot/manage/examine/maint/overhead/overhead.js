Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	Ext.ns("OverHand.dept");
	OverHand.dept = function() {
		var idws = []; 
		var znDeptSm = new Ext.grid.CheckboxSelectionModel({
			singleSelect : true
		});

		var znDept = new Ext.data.Record.create([{
			name : 'cdInfo.memo'
		}, {
			name : 'cdInfo.depId'
		}, {
			name : 'cdInfo.depCode'
		}, {
			name : 'deptname'
		}]);
		var znDeptStore = new Ext.data.JsonStore({
			url : 'managexam/getDeptList.action', 
			fields : znDept
		});
		znDeptStore.load();
		var commDept = new Power.dept();
		commDept.btnConfrim.on('click',function(){ 
			var node = commDept.getValue(); 
			var record = znDeptGrid.getSelectionModel().getSelected();
				record.set("cdInfo.depCode", node.attributes.code);
				record.set("deptname", node.text); 
		}); 
		var znDeptGrid = new Ext.grid.EditorGridPanel({
			store : znDeptStore, 
			clicksToEdit:1,
			columns : [new Ext.grid.RowNumberer(), {
				header : "职能部门",
				width : 100,
				sortable : false,
				dataIndex : 'deptname',
				editor : new Ext.form.TriggerField({
					editable : false,
					triggerAction : 'all',
					forceSelection : true,
					readOnly : true,
					onTriggerClick : function() {
						commDept.win.show();
					}
				})
			}, new Ext.form.TextField({
				dataIndex : 'cdInfo.depCode',
				hidden : true
			}), {
				header : "备注",
				dataIndex : 'cdInfo.memo',
				editor : new Ext.form.TextField()
			}],
			viewConfig : {
				forceFit : true
			},
			tbar : [new Ext.Button({
				text : '新增',
				iconCls : 'add',
				handler : function() {
					var currentIndex = znDeptStore.getCount();
					var o = new znDept({
						'cdInfo.depCode' : '',
						'deptname' : '',
						'cdInfo.memo' : ''
					});
					znDeptGrid.stopEditing();
					znDeptStore.insert(currentIndex, o);
					znDeptSm.selectRow(currentIndex);
					znDeptGrid.startEditing(currentIndex, 2);
				}
			}), '-', new Ext.Button({
				text : '保存',
				iconCls : 'save',
				handler : function() {
					znDeptGrid.stopEditing();
					var modifyRec = znDeptGrid.getStore().getModifiedRecords();
					if (modifyRec.length > 0 || idws.length > 0) {
						if (!confirm("确定要保存修改吗?"))
							return;
						var updateData = new Array();
						for (var i = 0; i < modifyRec.length; i++) {
							if (modifyRec[i].get("topicName") == "") {
								Ext.MessageBox.alert('提示信息', '职能部门不能为空！')
								return
							}
							updateData.push(modifyRec[i].data);
						}
						Ext.Ajax.request({
							url : 'managexam/saveDeptList.action',
							method : 'post',
							params : {
								isUpdate : Ext.util.JSON.encode(updateData),
								isDelete : idws.join(",")
							},
							success : function(result, request) {
								Ext.MessageBox.alert('提示信息', "操作成功!");
								znDeptGrid.getStore().rejectChanges();
								idws = [];
								znDeptGrid.getStore().reload();
							},
							failure : function(result, request) {
								Ext.MessageBox.alert('提示信息', '操作失败！')
							}
						})
					} else {
						Ext.MessageBox.alert('提示信息', '没有做任何修改！')
					}

				}
			}), '-', new Ext.Button({
				text : '删除',
				iconCls : 'delete',
				handler : function() {
					znDeptGrid.stopEditing();
					var westSm = znDeptGrid.getSelectionModel();
					var selected = znDeptGrid.getSelectionModel()
							.getSelections();
					if (selected.length == 0) {
						Ext.Msg.alert("提示", "请选择要删除的记录！");
					} else {
						for (var i = 0; i < selected.length; i++) {
							var member = selected[i];
							if (member.get("cdInfo.depId") != null) {
								idws.push(member.get("cdInfo.depId"));
							}
							znDeptGrid.getStore().remove(member);
							znDeptGrid.getStore().getModifiedRecords()
									.remove(member);
						}
					}

				}
			})],
			sm : znDeptSm,
			bbar : new Ext.PagingToolbar({
				pageSize : 20,
				store : znDeptStore,
				displayInfo : true,
				displayMsg : "{2}条记录",
				emptyMsg : "没有记录"
			}),
			enableColumnHide : false,
			enableColumnMove : false
		});
		return {
			grid : znDeptGrid
		}
	};
	
	//管理费用指标
	Ext.ns("OverHand.item");
	OverHand.item = function(){ 
		var ids = [];
		var znItemSm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			});

	var znItem = new Ext.data.Record.create([ {
				name : 'overheadItemId'
			}, {
				name : 'itemCode'
			}, {
				name : 'itemName'
			}
			, {
				name : 'unit.unitId'
			}, {
				name : 'unit.unitName'
			}
			]);

	var znItemStore = new Ext.data.JsonStore({
				url : 'managexam/getOverheadList.action',
				fields : znItem
			});

	znItemStore.load();
	var commUnit = new Power.unit({});  
	
	commUnit.btnConfirm.on("click", function() {
			var rec = znItemGrid.getSelectionModel().getSelected();
			var unit = commUnit.getValue();
			rec.set("unit.unitName", unit.unitName);
			rec.set("unit.unitId", unit.unitId); 
		});
			var znItemGrid = new Ext.grid.EditorGridPanel({
				tbar : [new Ext.Button({
					text:'新增',
					iconCls:'add',
					handler : function() {
					var currentIndex = znItemStore.getCount(); 
					var o = new znItem({
						'overheadItemId' : null,
						'itemCode' : null,
						'itemName' : null,
						'ohinfo.unitId' : null,
						'unit.unitId' : null,
						'unit.unitName' : null
					});
					znItemGrid.stopEditing();
					znItemGrid.getStore().insert(currentIndex, o);
					znItemGrid.getSelectionModel().selectRow(currentIndex);
					znItemGrid.startEditing(currentIndex, 1);
				}
				}), new Ext.Button({
					text:'删除',
					iconCls:'delete',
					handler : function() { 
					znItemGrid.stopEditing(); 
					var selected =  znItemGrid.getSelectionModel().getSelections();
					if (selected.length == 0) {
						Ext.Msg.alert("提示", "请选择要删除的记录！");
					} else {
						for (var i = 0; i < selected.length; i += 1) {
							var member = selected[i];
							if (member.get("overheadItemId") != null) {
								ids.push(member.get("overheadItemId"));
							}
							znItemGrid.getStore().remove(member);
							znItemGrid.getStore().getModifiedRecords()
									.remove(member);
						}
					}

				}
				}), new Ext.Button({
					text:'保存',
					iconCls:'save',
					handler : function() { 
					znItemGrid.stopEditing();
					var modifyRec = znItemGrid.getStore().getModifiedRecords();
					if (modifyRec.length > 0 || ids.length > 0) {
						if (!confirm("确定要保存修改吗?"))
							return;
						var updateData = new Array();
						for (var i = 0; i < modifyRec.length; i++) {
							
							if (modifyRec[i].get("itemCode") == null || modifyRec[i].get("itemCode") == "") {
								Ext.MessageBox.alert('提示信息', '指标不能为空！')
								return
							}
							if (modifyRec[i].get("unit.unitId") == null || modifyRec[i].get("unit.unitId") == "") {
								Ext.MessageBox.alert('提示信息', '计量单位不能为空！')
								return
							}
							updateData.push(modifyRec[i].data);
						}
						Ext.Ajax.request({
							url : 'managexam/saveOverheadList.action',
							method : 'post',
							params : {
								modifyData : Ext.util.JSON.encode(updateData),
								deleteIds : ids.join(",")
							},
							success : function(result, request) {
								Ext.MessageBox.alert('提示信息', "操作成功!");
								znItemGrid.getStore().rejectChanges();
								ids = [];
								znItemGrid.getStore().reload();
							},
							failure : function(result, request) {
								Ext.MessageBox.alert('提示信息', '操作失败！')
							}
						})
					} else {
						Ext.MessageBox.alert('提示信息', '没有做任何修改！')
					} 
				}
				})],
				store : znItemStore,
				clicksToEdit:1,
				columns : [new Ext.grid.RowNumberer(), {
							header : "管理费用指标",
							width : 100,
							sortable : false,
							dataIndex : 'itemName',
							editor : new Ext.form.TriggerField({
								onTriggerClick:function(){
									var ro = window.showModalDialog("/power/manage/system/selItem/selectItem.jsp",null,"dialogWidth=500px;dialogHeight=400px");
								    var rec = znItemGrid.getSelectionModel().getSelected();
								    if(ro){
								    this.setValue(ro.text);
									rec.set("itemCode", ro.id);
									rec.set("itemName",ro.text); 
								    }
								}
							})
						}, {
							header : "计量单位",
							width : 100,
							sortable : false,
							dataIndex : 'unit.unitName',
							editor : new Ext.form.TriggerField({
								editable : false,
								onTriggerClick : function() {
									commUnit.win.show();
								}
							})
						}],
				viewConfig : {
					forceFit : true
				},
				sm : znItemSm,
				bbar : new Ext.PagingToolbar({
							pageSize : 20,
							store : znItemStore,
							displayInfo : true,
							displayMsg : "{2}条记录",
							emptyMsg : "没有记录"
						}),
				frame : false,
				enableColumnHide : false,
				enableColumnMove : false 
			});
			return {
				grid:znItemGrid
			}
	};
	

	var dept = new OverHand.dept();
	var item =  new  OverHand.item();
	var tabs = new Ext.TabPanel({
		activeTab : 0,
		tabPosition : 'bottom',
		plain : true,
		defaults : {
			autoScroll : true
		},
		items : [{
			id : 'deptMaint',
			title : '职能部门维护',
			autoScroll : true,
			items : dept.grid,
			layout : 'fit'
		}, {
			id : 'itemMaint',
			title : '管理费用指标',
			items:item.grid,
			autoScroll : true,
			layout : 'fit'
		}]
	});
	var view = new Ext.Viewport({
		layout : 'fit',
		items : [tabs]
	});
});
