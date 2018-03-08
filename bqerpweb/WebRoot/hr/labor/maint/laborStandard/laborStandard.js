Ext.onReady(function() {

	var pageSize = 18;
	var lbWorkId;
	// 左边westgrid数据源
	var westitem = Ext.data.Record.create([{
				name : 'lbWorkId'
			}, {
				name : 'lbWorkName'
			}, {
				name : 'ifLbSpecialKind'
			}]);

	var westsm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : false

			});
	var westds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'com/findLaborWorkTypeList.action'
						}),

				reader : new Ext.data.JsonReader({
							totalCount : "totalCount",
							root : "list"
						}, westitem)
			});
	westds.load()

	var westcm = new Ext.grid.ColumnModel([westsm, new Ext.grid.RowNumberer({
						header : "行号",
						width : 35
					}), {
				header : "劳保工种ID",
				width : 75,
				sortable : true,
				hidden : true,
				dataIndex : 'lbWorkId'
			}, {
				header : "劳保工种",
				width : 130,
				sortable : true,
				dataIndex : 'lbWorkName'
			}, {
				header : "是否特殊",
				width : 75,
				sortable : true,
				dataIndex : 'ifLbSpecialKind',
				renderer : function(v) {
					if (v == 'Y') {
						return "是";
					}
					if (v == 'N') {
						return "否";
					}
				}
			}]);
	var westgrid = new Ext.grid.EditorGridPanel({
				sm : westsm,
				ds : westds,
				cm : westcm,
				autoScroll : true,
				border : true
			});
	westgrid.on("rowclick", function() {
				if (westgrid.getSelectionModel().hasSelection()) {
					var selections = westgrid.getSelectionModel()
							.getSelections();
					if (selections.length == 1) {
						var seleced = westgrid.getSelectionModel()
								.getSelected();
						AssignMentStore.reload({
									params : {
										lbWorkId : seleced.get('lbWorkId')
									}
								})
						UnAssignMentStore.reload({
									params : {
										lbWorkId : seleced.get('lbWorkId')
									}
								})
					}
				}
			})

	// 增加
	function addTheme() {
		var rec = westgrid.getSelectionModel().getSelections();
		if (rec.length != 1) {
			Ext.Msg.alert('提示', "请选择一项劳保工种！");
			return;
		}
		lbWorkId = rec[0].get("lbWorkId");
		if (UnAssignMentGrid.selModel.hasSelection()) {
			var records = UnAssignMentGrid.selModel.getSelections();
			var record;
			var recordArray = [];
			var isExist;
			for (var i = 0; i < records.length; i++) {
				AssignStoreAdd(records[i])
			}
			AssignMentGrid.getView().refresh();
			for (var i = 0; i < records.length; i++) {
				record = records[i];
				UnAssignMentStore.remove(record);
				UnAssignMentGrid.getView().refresh();
			}
		} else {
			Ext.Msg.alert("提示", "请选择待分配的劳保用品");
		}
	}
	function AssignStoreAdd(record) {
		var newRecord = new AssignMentData({
					laborStandardId : record.data.laborStandardId,
					lbWorkId : lbWorkId,
					laborMaterialId : record.data.laborMaterialId,
					laborMaterialName : record.data.laborMaterialName,
					spacingMonth : "",
					materialNum : "",
					sendKind : ""
				});
		AssignMentStore.insert(AssignMentStore.getCount(), newRecord);
	}
   
	
	// 刪除
	var ids = new Array();
	function deleteTheme() {
		var rec = westgrid.getSelectionModel().getSelections();
		if (rec.length != 1) {
			Ext.Msg.alert('提示', "请选择一项劳保工种！");
			return;
		}
		lbWorkId = rec[0].get("lbWorkId");
		if (AssignMentGrid.selModel.hasSelection()) {
			var records = AssignMentGrid.selModel.getSelections();
			var record;
			for (var i = 0; i < records.length; i++) {
				record = records[i];
				UnAssignStoreAdd(record);
			}
			UnAssignMentGrid.getView().refresh();
		    var selected = AssignMentGrid.getSelectionModel().getSelections();
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i];
				if (member.get("laborStandardId") != null) {
					ids.push(member.get("laborStandardId"));
				}
				westgrid.getStore().remove(member);
				westgrid.getStore().getModifiedRecords().remove(member);
			}
			AssignMentGrid.getView().refresh();
		} else {
			Ext.Msg.alert("提示", "请选择已分配的劳保用品");
		}
	}
	function UnAssignStoreAdd(record) {
		var newRecord = new UnAssignMentData({
					laborMaterialId : record.data.laborMaterialId,
					laborMaterialName : record.data.laborMaterialName
				});
		UnAssignMentStore.add(newRecord);
	}
	

	// 保存
	function saveTheme() {
		var record;
		var dataList = [];
		var modifyRec = AssignMentGrid.getStore().getModifiedRecords();
		
		for (var i = 0; i < AssignMentStore.getCount(); i++) {
			record = AssignMentStore.getAt(i);
			if (record.data.spacingMonth === "") {
				Ext.Msg.alert("错误", "间隔（月）不能为空，请输入！");
				return;
			}
			if (record.data.materialNum === "") {
				Ext.Msg.alert("错误", "数量不能为空，请输入！");
				return;
			}
			if (record.data.sendKind === "") {
				Ext.Msg.alert("错误", "发放类别不能为空，请输入！");
				return;
			}
		}
		if (modifyRec.length > 0 || ids.length > 0){
		Ext.Msg.confirm("确定", "确定要保存吗？", function(buttonobj) {
			if (buttonobj == "yes") {
				for (var i = 0; i < AssignMentStore.getCount(); i++) {
					record = AssignMentStore.getAt(i);
					if (parseFloat(record.get("spacingMonth")) <= 0) {
						Ext.Msg.alert("错误", "间隔（月）不能小于等于零");
						return;
					}
					if (parseFloat(record.get("materialNum")) <= 0) {
						Ext.Msg.alert("错误", "数量不能小于等于零");
						return;
					}
				}
			
				var addData = new Array();
				var updateData = new Array();
				for (var i = 0; i < modifyRec.length; i++) {

						if (modifyRec[i].get('laborStandardId') != null) {
							updateData.push(modifyRec[i].data);
						} else {
							addData.push(modifyRec[i].data);
						}

					}
					Ext.Ajax.request({
								url : 'com/saveLaborStandard.action',
								method : 'post',
								params : {
									isAdd : Ext.util.JSON.encode(addData),
									isUpdate : Ext.util.JSON.encode(updateData),
									ids : ids.join(",")
								},
								success : function(result, request) {
									var o = Ext.util.JSON
											.decode(result.responseText);
									Ext.Msg.alert("提示信息", o.msg);
									AssignMentStore.rejectChanges();
									ids = [];
									AssignMentStore.reload();
									UnAssignMentStore.reload();
								},
								failure : function(result, request) {
									Ext.Msg.alert("提示信息", "数据保存修改失败！");
									AssignMentStore.rejectChanges();
									ids = [];
									AssignMentStore.reload();
									
								}
							})
			}
		});
		} else {
			Ext.MessageBox.alert('提示信息', '没有做任何修改！')
		}
	}

	var toolbar = new Ext.Toolbar({
				items : [{
							id : 'btnAdd',
							iconCls : 'add',
							text : "增加",
							minWidth : 70,
							handler : addTheme
						}, '-', {
							id : 'btnDelete',
							iconCls : 'delete',
							minWidth : 70,
							text : "刪除",
							handler : deleteTheme
						}, '-', {
							id : 'btnSave',
							iconCls : 'save',
							text : "保存",
							minWidth : 60,
							handler : saveTheme
						}]
			})
	var centerFieldSet = new Ext.form.FieldSet({
				layout : "form",
				style : "padding-left:2px;padding-top:12px;margin-bottom:3px",
				height : 43,
				border : false,
				labelAlign : 'right',
				anchor : '100%',
				items : [toolbar]

			});
				
 // 待分配用品
	var UnAssignMentData = Ext.data.Record.create([{
				name : 'laborMaterialId',
				mapping : 0
			}, {
				name : 'laborMaterialName',
				mapping : 1
			}, {
				name : 'unitId',
				mapping : 2
			}, {
				name : 'unitName',
				mapping : 3
			}, {
				name : 'receiveKind',
				mapping : 4
			}]);
	
	var UnAssignMentStore = new Ext.data.JsonStore({
				url : 'com/findNoSelectLaborStandardList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : UnAssignMentData
			});

	
	UnAssignMentStore.load();
	
	var UnAssignMentGrid = new Ext.grid.EditorGridPanel({
				height : 270,
				width : 620,
				title : "待分配",
				sm : new Ext.grid.RowSelectionModel({
							singleSelect : false
						}),
				enableColumnMove : false,
				store : UnAssignMentStore,
				columns : [new Ext.grid.RowNumberer({
									header : "行号",
									width : 50
								}), {
							header : "ID",
							width : 75,
							sortable : true,
							dataIndex : 'laborMaterialId',
							hidden : true
						}, {
							header : "勞保用品",
							width : 200,
							sortable : true,
							dataIndex : 'laborMaterialName'
						}, {
							header : "单位",
							width : 185,
							sortable : true,
							dataIndex : 'unitName'
						}, {
							header : "领用类别",
							width : 180,
							sortable : true,
							dataIndex : 'receiveKind',
							renderer : function(v) {
								if (v == 1) {
									return "个人领用";
								}
								if (v == 2) {
									return "统一领用";
								}if (v == 3) {
									return "公用";
								}
							}
						}],
				autoSizeColumns : true

			});
	// 已分配用品
	var AssignMentData = Ext.data.Record.create([{
				name : 'laborStandardId',
				mapping : 0
			}, {
				name : 'lbWorkId',
				mapping : 1
			}, {
				name : 'laborMaterialId',
				mapping : 2
			}, {
				name : 'laborMaterialName',
				mapping : 3
			}, {
				name : 'spacingMonth',
				mapping : 4
			}, {
				name : 'materialNum',
				mapping : 5
			}, {
				name : 'sendKind',
				mapping : 6
			}]);

	var AssignMentStore = new Ext.data.JsonStore({
				url : 'com/findLaborStandardList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : AssignMentData
			});

	AssignMentStore.load();
	
	var AssignMentGrid = new Ext.grid.EditorGridPanel({
				height : 270,
				width : 620,
				title : "已分配",
				sm : new Ext.grid.RowSelectionModel({
							singleSelect : false
						}),
				enableColumnMove : false,
				store : AssignMentStore,
				columns : [new Ext.grid.RowNumberer({
									header : "行号",
									width : 50
								}), {
							header : "ID",
							width : 75,
							sortable : true,
							dataIndex : 'laborMaterialId',
							hidden : true
						}, {
							header : "勞保用品",
							width : 200,
							sortable : true,
							dataIndex : 'laborMaterialName'
						}, {
							header : "间隔（月）",
							width : 106,
							sortable : true,
							dataIndex : 'spacingMonth',
							editor : new Ext.form.NumberField({
										style : 'cursor:pointer',
										allowNegative : false,
										allowDecimal : false,
										allowBlank : true
									})
						}, {
							header : "数量",
							width : 106,
							sortable : true,
							dataIndex : 'materialNum',
							editor : new Ext.form.NumberField({
										style : 'cursor:pointer',
										allowNegative : false,
										allowDecimal : false,
										allowBlank : true
									})
						}, {
							header : "发放类别",
							width : 150,
							sortable : true,
							dataIndex : 'sendKind',
							editor : new Ext.form.ComboBox({
										store : new Ext.data.SimpleStore({
													fields : ["retrunValue",
															"displayText"],
													data : [['1', '所有'],
															['2', '男用'],
															['3', '女用']]
												}),
										displayField : "displayText",
										valueField : "retrunValue",
										id : 'sendKind',
										allowBlank : false,
										mode : 'local',
										triggerAction : 'all',
										readOnly : true
									}),
							renderer : function(v) {
								if (v == "1") {
									return "所有";
								}
								if (v == "2") {
									return "男用";
								}
								if (v == "3") {
									return "女用";
								}
							}
						}],
				autoSizeColumns : true

			});
	var leftPanel = new Ext.Panel({
				region : 'west',
				layout : 'fit',
				width : '26%',
				autoScroll : true,
				border : false,
				containerScroll : true,
				collapsible : true,
				split : false,
				items : [westgrid]
			});

	// 右边的panel
	var rightPanel = new Ext.Panel({
				layout : 'form',
				border : false,
				items : [AssignMentGrid, centerFieldSet, UnAssignMentGrid]
			});

	// 显示区域
	var view = new Ext.Viewport({
				enableTabScroll : true,
				autoScroll : true,
				layout : "border",
				items : [leftPanel, {
							region : 'center',
							layout : 'fit',
							width : 580,
							height : 650,
							autoScroll : true,
							items : [rightPanel]
						}]
			})

});