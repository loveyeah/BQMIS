Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {

	// 取左边store数据
	function queryRecord() {
		store.load({
			params : {
				start : 0,
				limit : 18
			}
		});
	}
	
	var syxmId = new Ext.form.Hidden({
				fieldLabel : "项目编号",
				name : 'syxmId',
				anchor : '90%',
				readOnly : true
			})

	var syxmName = new Ext.form.TextField({
				fieldLabel : "项目名称",
				name : 'model.syxmName',
				anchor : '90%'

			})

	
	var displayNo = new Ext.form.NumberField({
				fieldLabel : "显示顺序",
				name : 'model.displayNo',
				maxValue : 99,
				minValue : 0,
				anchor : '90%'

			})

	// 弹窗的表单对象
	var blockForm = new Ext.form.FormPanel({
				labelAlign : 'right',
				frame : true,
				labelWidth : 90,
				items : [{
					bodyStyle : "padding:20px 0 0 0",
					layout : 'form',
					items : [syxmId,syxmName, displayNo]
				}]

			});
	// 左边的弹出窗体

	var blockAddWindow = new Ext.Window({
				// el : 'window_win',
				title : '',
				layout : 'fit',
				width : 400,
				height : 180,
				modal : true,
				closable : true,
				border : false,
				resizable : false,
				closeAction : 'hide',
				plain : true,
				// 面板中按钮的排列方式
				buttonAlign : 'center',
				items : [blockForm],
				buttons : [{
					text : '保存',
					handler : function() {
						var myurl = "";
						if(syxmName.getValue() == null || syxmName.getValue() == "")
						{
							Ext.Msg.alert('提示信息','项目名称不能为空！');
							return ;
						}
						if (blockForm.getForm().isValid())

							var myurl = "";
						if (op == "add") {
							myurl = "productionrec/saveSyxmwh.action";
						} else if (op == "edit") {
							myurl = "productionrec/updateSyxmwh.action?syxmId="+syxmId.getValue();
						} else {
							Ext.MessageBox.alert('错误', '未定义的操作');
							return;
						}
						blockForm.getForm().submit({
							method : 'POST',
							url : myurl,
							success : function(form, action) {
								var o = eval("(" + action.response.responseText
										+ ")");
								queryRecord();
								blockAddWindow.hide();
							},
							faliue : function() {
								Ext.Msg.alert('错误', '出现未知错误.');
							}
						});
					}
				}, {
					text : '取消',
					handler : function() {
						blockForm.getForm().reset();
						blockAddWindow.hide();
					}
				}]
			});
	/** 左边的grid * */

	var MyRecord = Ext.data.Record.create([{
				name : 'syxmId'
			}, {
				name : 'syxmName'
			}, {
				name : 'displayNo'
			}, {
				name : 'enterpriseCode'
			}]);

	var dataProxy = new Ext.data.HttpProxy({
				url : 'productionrec/getSyxmwhList.action '
			});

	var theReader = new Ext.data.JsonReader({
				root : "list",
				totalProperty : "totalCount"

			}, MyRecord);
	var store = new Ext.data.Store({
				proxy : dataProxy,
				reader : theReader
			});

	var weldsm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true
	});
	
	var number = new Ext.grid.RowNumberer({
				header : "行号",
				align : 'center',
				width : 40
			})
	// 新建按钮
	var westbtnAdd = new Ext.Button({
				text : '新增',
				iconCls : 'add',
				handler : function() {
					blockForm.getForm().reset();
					op = 'add';
					blockAddWindow.show();
					blockAddWindow.setTitle("增加试验项目信息");
				}
			});

	// 修改按钮
	var westbtnedit = new Ext.Button({
				text : '修改',
				iconCls : 'update',
				handler : function() {
					op = 'edit';
					if (grid.selModel.hasSelection()) {
						var records = grid.getSelectionModel().getSelected();
						var recordslen = records.length;
						if (recordslen > 1) {
							Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑！");
						} else {
							blockAddWindow.show();
							var record = grid.getSelectionModel().getSelected();
							blockForm.getForm().reset();
							blockForm.form.loadRecord(record);
					
							syxmName.setValue(record.get('syxmName'));
							displayNo.setValue(record.get('displayNo'));
							blockAddWindow.setTitle("修改试验项目信息");
						}
					} else {
						Ext.Msg.alert("提示", "请先选择要编辑的行!");
					}
				}
			});

	// 删除按钮
	var westbtndel = new Ext.Button({
				text : '删除',
				iconCls : 'delete',
				handler : function() {
					var records = grid.selModel.getSelections();
					var ids = [];
					if (records.length == 0) {
						Ext.Msg.alert("提示", "请选择要删除的记录！");
					} else {
						for (var i = 0; i < records.length; i += 1) {
							var member = records[i];
							if (member.get("syxmId")) {
								ids.push(member.get("syxmId"));
							} else {
								store.remove(store.getAt(i));
							}
						}
						Ext.Msg.confirm("删除", "是否确定删除所选的记录？", function(
										buttonobj) {
									if (buttonobj == "yes") {
										Ext.lib.Ajax
												.request(
														'POST',
														'productionrec/deleteSyxmwh.action',
														{
															success : function(
																	action) {
																queryRecord();
																con_ds.reload();
															},
															failure : function() {
																Ext.Msg
																		.alert(
																				'错误',
																				'删除时出现未知错误.');
															}
														}, 'ids=' + ids);
									}
								});
					}
				}
			});

	// 刷新按钮
	var westbtnref = new Ext.Button({
				text : '刷新',
				iconCls : 'reflesh',
				handler : function() {
					queryRecord();
				}
			});

	// 定义grid

	var grid = new Ext.grid.GridPanel({
		sm : weldsm,
		store : store,
		layout : 'fit',
		cm : new Ext.grid.ColumnModel([
				weldsm, // 选择框
				number, {
					header : "试验项目名称",
					sortable : false,
					width : 185,
					dataIndex : 'syxmName'
				},{
					header : "显示顺序",
					sortable : false,
					hidden : true,
					dataIndex : 'displayNo'
				}]),
		tbar : [westbtnAdd, {
					xtype : "tbseparator"
				}, westbtnedit, {
					xtype : "tbseparator"
				}, westbtndel, {
					xtype : "tbseparator"
				}],
		bbar : new Ext.PagingToolbar({
					pageSize : 18,
					store : store,
					displayInfo : true,
					displayMsg : "共{2}条",
					emptyMsg : "没有记录"
				}),
		clicksToEdit : 1
			// })
		});

	grid.on('rowclick', modifyBtn);
	function modifyBtn() {
		Ext.getCmp("btnAdd").setDisabled(false);
		Ext.getCmp("btnDelete").setDisabled(false);
		Ext.getCmp("btnCancer").setDisabled(false);
		Ext.getCmp("btnSave").setDisabled(false);
		var recL = grid.getSelectionModel().getSelected();
		if (recL) {
			if (recL.get("syxmId") != null) {
				con_ds.load({
							params : {
								syxmId : recL.get("syxmId"),
								start : 0,
								limit : 18

							}
						})
			}
		} else {
			Ext.Msg.alert("提示", "请选择左边的一条记录！")
		}
	}
// grid.on('rowdblclick', modifyBtn2);
// function modifyBtn2() {
//
// var recL = grid.getSelectionModel().getSelected();
//
// if (recL) {
// parent.weldId = recL.get("model.weldId");
// parent.workerName = recL.get("workerName");
//
// if (parent.document.all.iframe2 != null) {
// parent.document.all.iframe2.src =
// "productiontec/metalSupervise/business/welderInfo/assess.jsp";
//
// }
// tabpanel.setActiveTab(1);
// }
//
// }
	/** 左边的grid * */
	
	// 各种单位下拉框数据源
	var unitData = Ext.data.Record.create([{
		name : 'unitName'
	}, {
		name : 'unitId'
	}]);
	var allUnitStore = new Ext.data.JsonStore({
		url : 'resource/getAllUnitList.action',
		root : 'list',
		fields : unitData
	});
	allUnitStore.load();
	// 计价单位组合框
	var cbxStockUnit = new Ext.form.ComboBox({
// fieldLabel : "计价单位",
		// modify by ddr 20090618
		readOnly : false,
		id : 'cbxStockUnit',
		allowBlank : false,
		style : "border-bottom:solid 2px",
		triggerAction : 'all',
		store : allUnitStore,
		blankText : '',
		emptyText : '',
		valueField : 'unitId',
		displayField : 'unitName',
		mode : 'local',
		hiddenName : 'temp.stockUmId',
		listeners : {
			render : function() {
				this.clearInvalid();
			}
		}
	})

	/** 右边的grid * */

	var con_item = Ext.data.Record.create([{
				name : 'sydId'
			}, {
				name : 'syxmId'
			}, {
				name : 'sydName'
			}, {
				name : 'unitId'
			}, {
				name : 'maximum'
			}, {
				name : 'minimum'
			}, {
				name : 'displayNo'
			}, {
				name : 'memo'
			}]);

	var con_sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : false

			});
	var con_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'productionrec/findSydwhListBySyxmId.action'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : "",
							root : ""
						}, con_item)
			});

	var con_item_cm = new Ext.grid.ColumnModel([con_sm, number,
			{
				header : '试验点Id',
				dataIndex : 'sydId',
				hidden : true,
				align : 'center'
			}, {
				header : '试验项目Id',
				dataIndex : 'syxmId',
				hidden : true,
				align : 'center'
			}, {
				header : '试验点名称',
				dataIndex : 'sydName',
				align : 'center',
				editor : new Ext.form.TextField()
			}, {
				header : '计量单位',
				dataIndex : 'unitId',
				align : 'center',
				editor : cbxStockUnit,
				renderer : function(value) {
					if(value != null && value != "") {
						return allUnitStore.getAt(allUnitStore.find("unitId",value)).get("unitName");
					} else {
						return "";
					}
				}
			}, {
				header : '合格下限',
				dataIndex : 'minimum',
				align : 'center',
				editor : new Ext.form.NumberField()
			}, {
				header : '合格上限',
				dataIndex : 'maximum',
				align : 'center',
				editor : new Ext.form.NumberField()
			},  {
				header : '显示顺序',
				dataIndex : 'displayNo',
				align : 'center',
				editor : new Ext.form.NumberField({
										maxValue : 10
									})
			}, {
				header : '备注',
				dataIndex : 'memo',
				align : 'center',
				editor : new Ext.form.TextField()
			}]);
	con_item_cm.defaultSortable = true;

	// 增加
	function addTopic() {

		if (grid.selModel.hasSelection()
				&& grid.getSelectionModel().getSelections().length == 1) {
			var rec = grid.getSelectionModel().getSelected();
			var count = con_ds.getCount();

			var currentIndex = count;

			var o = new con_item({
						'syxmId' : rec.get("syxmId"),
						'sydName' : '',
						'unitId' : '',
						'minimum' : '',
						'maximum' : '',
						'displayNo' : '',
						'memo' : ''
					});

			Grid.stopEditing();
			con_ds.insert(currentIndex, o);
			con_sm.selectRow(currentIndex);
			Grid.startEditing(currentIndex, 2);
// resetLine();
		} else {
			Ext.MessageBox.alert("提示", "请选择左边的<一条>记录!");
		}
	}

	// 删除记录
	var topicIds = new Array();
	function deleteTopic() {
		Grid.stopEditing();
		var sm = Grid.getSelectionModel();
		var selected = sm.getSelections();
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i];
				if (member.get("sydId") != null) {
					topicIds.push(member.get("sydId"));
				}
				Grid.getStore().remove(member);
				Grid.getStore().getModifiedRecords().remove(member);
			}
			// resetLine();
		}
	}
	
	// 保存
	function saveTopic() {
		Grid.stopEditing();
		var modifyRec = Grid.getStore().getModifiedRecords();

		if (modifyRec.length > 0 || topicIds.length > 0) {

			if (!confirm("确定要保存修改吗?"))
				return;
			var updateData = new Array();
			for (var i = 0; i < modifyRec.length; i++) {
				if(modifyRec[i].data.sydName == "") {
					Ext.MessageBox.alert('提示', '试验点名称不能为空！')
					return 
				}
				if(modifyRec[i].data.unitId == "") {
					Ext.MessageBox.alert('提示', '计量单位不能为空！')
					return 
				}
				if(modifyRec[i].data.minimum == "") {
					Ext.MessageBox.alert('提示', '合格下限不能为空！')
					return 
				}
				if(modifyRec[i].data.maximum == "") {
					Ext.MessageBox.alert('提示', '合格上限不能为空！')
					return 
				}
				if(modifyRec[i].data.displayNo ==null||modifyRec[i].data.displayNo=="") {
					Ext.MessageBox.alert('提示', '显示顺序不能为空！')
					return 
				}
				updateData.push(modifyRec[i].data);
			
			}

			Ext.Ajax.request({
						url : 'productionrec/saveSydwh.action',
						method : 'post',
						params : {
							// isAdd : Ext.util.JSON.encode(newData),
							isUpdate : Ext.util.JSON.encode(updateData),
							isDelete : topicIds.join(",")
						},
						success : function(result, request) {
							var o = eval("(" + result.responseText+ ")")
							Ext.MessageBox.alert('提示信息', o.msg);	
							con_ds.rejectChanges();
							topicIds = [];
							con_ds.reload();
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
	function cancerTopic() {
		var modifyRec = con_ds.getModifiedRecords();
		if (modifyRec.length > 0 || topicIds.length > 0) {
			if (!confirm("确定要放弃修改吗"))
				return;
			con_ds.reload();
			con_ds.rejectChanges();
			topicIds = [];
		} else {
			con_ds.reload();
			con_ds.rejectChanges();
			topicIds = [];
		}
	}

	var contbar = new Ext.Toolbar({
				items : [{
							id : 'btnAdd',
							iconCls : 'add',
							text : "新增",
							disabled : true,
							handler : addTopic

						}, {
							id : 'btnDelete',
							iconCls : 'delete',
							disabled : true,
							text : "删除",
							handler : deleteTopic

						}, {
							id : 'btnCancer',
							iconCls : 'cancer',
							disabled : true,
							text : "取消",
							handler : cancerTopic

						}, '-', {
							id : 'btnSave',
							iconCls : 'save',
							disabled : true,
							text : "保存修改",
							handler : saveTopic
						}]

			});
	var Grid = new Ext.grid.EditorGridPanel({
		sm : con_sm,
		ds : con_ds,
		cm : con_item_cm,
		height : 425,
		autoScroll : true,
		tbar : contbar,
		border : true,
		clicksToEdit : 1,
		listeners:{ 
			'afteredit' : function(e)
			{
				var minimumValue = e.record.get("minimum");
				var maximumValue = e.record.get("maximum");
				if(maximumValue<minimumValue)
				{
					Ext.Msg.alert("提示", "合格下限不能大于合格上限！");
					return ;
				}
			}
		}
		});

	/** 左边的grid * */

	new Ext.Viewport({
				layout : "border",
				border : false,
				frame : false,
				items : [{
							layout : 'fit',
							border : false,
							frame : false,
							region : "west",
							width : '36%',
							items : [grid]
						}, {
							region : "center",
							border : false,
							frame : false,
							layout : 'fit',
							autoScroll : true,
							items : [Grid]
						}]
			});
	queryRecord();
})