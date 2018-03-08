Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	var pageSize = 18;
	// 左边westgrid数据源
	var westitem = Ext.data.Record.create([{
				name : 'topicId',
				mapping : 0,
				type : 'int'
			}, {
				name : 'topicName',
				mapping : 1,
				type : 'string'
			}, {
				name : 'topicMemo',
				mapping : 2,
				type : 'string'
			}, {
				name : 'displayNo',
				mapping : 3,
				type : 'int'
			}]);

	var westsm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : false

			});
	var westds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'manageitemplan/getAllTopicRecord.action'
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
				header : '主题id',
				dataIndex : 'topicId',
				hidden : true
			}, {
				header : '主题名称',
				dataIndex : 'topicName',
				align : 'left',
				width : 90,
				editor : new Ext.form.TextField({
							style : 'cursor:pointer',
							allowBlank : false
						})
			}, {
				header : '主题说明',
				dataIndex : 'topicMemo',
				align : 'left',
				width : 180,
				editor : new Ext.form.TextField({
							style : 'cursor:pointer',
							allowBlank : false
						})
			}, {
				header : '显示顺序',
				dataIndex : 'displayNo',
				align : 'left',
				width : 60,
				editor : new Ext.form.NumberField({
							style : 'cursor:pointer',
							allowNegative : false,
							allowDecimal : false,
							allowBlank : true
						})
			}]);

	var itemCode = new Ext.form.TextField({
				name : 'itemCode',
				xtype : 'textfield',
				fieldLabel : '指标编码',
				readOnly : true,
				anchor : '85%',
				allowBlank : false,
				blankText : '不可为空！'

			});
	var itemName = new Ext.form.TextField({
				name : 'itemName',
				xtype : 'textfield',
				fieldLabel : '指标名称',
				readOnly : true,
				anchor : '85%',
				allowBlank : false,
				blankText : '不可为空！'

			});

	// 右边grid数据源
	var item = Ext.data.Record.create([{
				name : 'economicItemId',
				mapping : 0,
				type : 'int'
			},{
				name : 'topicId',
				mapping : 1,
				type : 'int'
			}, {
				name : 'itemCode',
				mapping : 2,
				type : 'string'
			}, {
				name : 'itemName',
				mapping : 3,
				type : 'string'
			}, {
				name : 'alias',
				mapping : 4,
				type : 'string'
			}, {
				name : 'unitId',
				mapping : 5,
				type : 'int'
			},{
				name : 'unitName',
				mapping : 6,
				type : 'string'
			}, {
				name : 'itemType',
				mapping : 7,
				type : 'string'
			}, {
				name : 'displayNo',
				mapping : 8,
				type : 'int'
			}
			]);

	var sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : false

			});
	var ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'manageitemplan/getItemByTopic.action'
						}),
				reader : new Ext.data.JsonReader({
							totalCount : "totalCount",
							root : "list"
						}, item)
			});
//	ds.load()

	var unit = new Power.unit({anchor : '100%'});
	unit.btnConfirm.on("click",function(){
		if(grid.getSelectionModel().hasSelection()){
			var rec = grid.getSelectionModel().getSelected();
			var aUnit = unit.getValue();
			rec.set('unitId',aUnit.unitId);
			rec.set('unitName',aUnit.unitName);
		}
	}
);	
	var cm = new Ext.grid.ColumnModel([sm, new Ext.grid.RowNumberer({
						header : "行号",
						width : 35
					}), {
				header : '经济指标id',
				dataIndex : 'economicItemId',
				hidden : true
			}, {
				header : '主题id',
				dataIndex : 'topicId',
				hidden : true
			}, {
				header : '指标编码',
				dataIndex : 'itemCode',
				align : 'left',
				width : 90,
				editor : new Ext.form.TextField({
							style : 'cursor:pointer',
							readOnly : true,
							allowBlank : false
						})
			}, {
				header : '指标名称',
				dataIndex : 'itemName',
				align : 'left',
				width : 90,
				editor : new Ext.form.TextField({
							style : 'cursor:pointer',
							readOnly : true,
							allowBlank : false
							
						})
			}, {
				header : '指标别名',
				dataIndex : 'alias',
				align : 'left',
				width : 110,
				editor : new Ext.form.TextField({
							style : 'cursor:pointer',
							allowBlank : false
						})
			},
			   {
				header : '计量单位',
				dataIndex : 'unitName',
				align : 'left',
				width : 110,
				editor : unit.combo,
				renderer : function(value,metadata,record){
					if(value != null)
						return record.get('unitName')
				}
			}
			, {
				header : '分类',
				dataIndex : 'itemType',
				align : 'left',
				width : 90,
				editor : new Ext.form.TextField({
							style : 'cursor:pointer',
							allowBlank : false
						})
			}, {
				header : '显示顺序',
				dataIndex : 'displayNo',
				align : 'left',
				width : 60,
				editor : new Ext.form.NumberField({
							style : 'cursor:pointer',
							allowNegative : false,
							allowDecimal : false,
							allowBlank : true
						})
			}]);

	// 增加
	function westaddTheme() {
		var count = westds.getCount();
		var currentIndex = count;
		var o = new westitem({
					'topicId' : null,
					'topicName' : null,
					'topicMemo' : null,
					'displayNo' : null
				});
		westgrid.stopEditing();
		westds.insert(currentIndex, o);
		westsm.selectRow(currentIndex);
		westgrid.startEditing(currentIndex, 3);
	}
	var itemNs = new Power.operateItem({});
	itemNs.confirmBtu.on('click',function(){
			var rvo = itemNs.saveRecords();
			if (typeof(rvo) != "undefined" && rvo.length > 0) {
				var i;
				var westRec = westgrid.getSelectionModel().getSelected();
				for (i = 0; i < rvo.length; i++) {
					var count = ds.getCount();
					var currentIndex = count;
					var o = new item({
					'topicId' : westRec.get('topicId'),
					'itemCode' : null,
					'itemName' : null,
					'alias' : null,
					'unitId' : null,
					'unitName' : null,
					'itemType' : null,
					'displayNo' : null
				});
					grid.stopEditing();
					ds.insert(currentIndex, o);
					sm.selectRow(currentIndex);
					o.set("itemCode", rvo[i].itemCode);
					o.set("itemName", rvo[i].itemName);
					o.set("alias", rvo[i].itemName);
					o.set("unitId", rvo[i].unitId);
					o.set("unitName", rvo[i].unitName);
					grid.startEditing(currentIndex, 3);
				}			
		}
		})	
	// 指标选择
	function selectTheme() {
		itemNs.win.show();	
//		var args = {
//					selectModel : 'single',
//					rootNode : {
//						id : "0",
//						text : '指标名称'
//					}
//				}
//				var url = "../../../../system/selItem/selectItem.jsp";
//				var rvo = window
//						.showModalDialog(
//								url,
//								args,
//								'dialogWidth:650px;dialogHeight:450px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
//				if (typeof(rvo) != "undefined") {
//					var count = ds.getCount();
//					var currentIndex = count;
//					var westRec = westgrid.getSelectionModel().getSelected();
//					var o = new item({
//					'topicId' : westRec.get('topicId'),
//					'itemCode' : null,
//					'itemName' : null,
//					'alias' : null,
//					'unitId' : null,
//					'unitName' : null,
//					'itemType' : null,
//					'displayNo' : null
//				});
//					grid.stopEditing();
//					ds.insert(currentIndex, o);
//					sm.selectRow(currentIndex);
//					o.set("itemCode", rvo.id);
//					o.set("itemName", rvo.text);
//					o.set("alias", rvo.text);
////					var unitDes = rvo.href;
//					o.set("unitId", rvo.href);
//						o.set("unitName", rvo.cls);
////					if(unitDes)
////					{
////						var str = unitDes.toString().split(',');
////						o.set("unitId", str[0]);
////						o.set("unitName", str[1]);
////					}
//					grid.startEditing(currentIndex, 3);
//				}
	}
	


	// 删除记录
	var themeIds = new Array();
	function westdeleteTheme() {
		westgrid.stopEditing();
		var westsm = westgrid.getSelectionModel();
		var selected = westsm.getSelections();
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i];
				if (member.get("topicId") != null) {
					themeIds.push(member.get("topicId"));
				}
				westgrid.getStore().remove(member);
				westgrid.getStore().getModifiedRecords().remove(member);
			}
		}
	}
	
	// 删除记录
	var ids = new Array();
	function deleteTheme() {
		grid.stopEditing();
		var sm = grid.getSelectionModel();
		var selected = sm.getSelections();
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i];
				if (member.get("economicItemId") != null) {
					ids.push(member.get("economicItemId"));
				}
				grid.getStore().remove(member);
				grid.getStore().getModifiedRecords().remove(member);
			}
		}
	}

	function checkTopicName() {
		for (var i = 0; i < westds.getCount(); i++) {
			for (var j = i + 1; j < westds.getCount(); j++) {
				if (westds.getAt(i).get('topicName') == westds
						.getAt(j).get('topicName')) {
					Ext.Msg.alert('提示','主题名称不能重复！');
					return false;
				}
			}
		}
		return true;
	}
	
	function checkDisplayNo() {
		for (var i = 0; i < westds.getCount(); i++) {
			for (var j = i + 1; j < westds.getCount(); j++) {
				if (westds.getAt(i).get('displayNo') == westds
						.getAt(j).get('displayNo')) {
					Ext.Msg.alert('提示','显示顺序不能重复！');
					return false;
				}
			}
		}
		return true;
	}
	
	// 保存
	function westsaveTheme() {
		westgrid.stopEditing();
		if(!checkTopicName())
			return;
	    if(!checkDisplayNo())
			return;
		var modifyRec = westgrid.getStore().getModifiedRecords();
		if (modifyRec.length > 0 || themeIds.length > 0) {
			for (var i = 0; i < modifyRec.length; i++) {
				if (modifyRec[i].data.topicName == null
						|| modifyRec[i].data.topicName == "") {
					Ext.Msg.alert('提示信息', '主题名称不可为空，请输入！')
					return;
				}
				if (modifyRec[i].data.displayNo == null
						|| modifyRec[i].data.displayNo == "") {
					Ext.Msg.alert('提示信息', '显示顺序不可为空，请输入！')
					return;
				}
				
			}
			Ext.Msg.confirm('提示信息', '确定要保存修改吗？', function(button) {
				if (button == 'yes') {
					var addData = new Array();
					var updateData = new Array();
					for (var i = 0; i < modifyRec.length; i++) {

						if (modifyRec[i].get('topicId') != null) {
							updateData.push(modifyRec[i].data);
						} else {
							addData.push(modifyRec[i].data);
						}

					}
					Ext.Msg.wait('操作正在进行中……');
					Ext.Ajax.request({
								url : 'manageitemplan/saveModifiedTopic.action',
								method : 'post',
								params : {
									isAdd : Ext.util.JSON.encode(addData),
									isUpdate : Ext.util.JSON.encode(updateData),
									ids : themeIds.join(",")
								},
								success : function(result, request) {
									var o = Ext.util.JSON
											.decode(result.responseText);
									Ext.Msg.alert("提示信息", o.msg);
									westds.rejectChanges();
									themeIds = [];
									westds.reload();
								},
								failure : function(result, request) {
									Ext.Msg.alert("提示信息", "数据保存修改失败！");
									westds.rejectChanges();
									themeIds = [];
									westds.reload();
								}
							})
				}
			})

		} else {
			Ext.MessageBox.alert('提示信息', '没有做任何修改！')
		}
	}

	// 保存
	function saveTheme() {
		grid.stopEditing();
		var modifyRec = grid.getStore().getModifiedRecords();
		if (modifyRec.length > 0 || ids.length > 0) {
			for (var i = 0; i < modifyRec.length; i++) {
						if(modifyRec[i].get('itemType') == null || modifyRec[i].get('itemType') == "")
						{
							Ext.Msg.alert('提示','分类不可以为空！');
							// modified by liuyi 091223 
//							ds.rejectChanges();
//							ds.reload();
							return;
						}}
			Ext.Msg.confirm('提示信息', '确定要保存修改吗？', function(button) {
				if (button == 'yes') {
					var addData = new Array();
					var updateData = new Array();
					for (var i = 0; i < modifyRec.length; i++) {
						if (modifyRec[i].get('economicItemId') != null) {
							updateData.push(modifyRec[i].data);
						} else {
							addData.push(modifyRec[i].data);
						}

					}
					Ext.Msg.wait('操作正在进行中……');
					Ext.Ajax.request({
								url : 'manageitemplan/saveModifiedItem.action',
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
									ds.rejectChanges();
									ids = [];
									ds.reload();
								},
								failure : function(result, request) {
									Ext.Msg.alert("提示信息", "数据保存修改失败！");
									ds.rejectChanges();
									ids = [];
									ds.reload();
								}
							})
				}
			})

		} else {
			Ext.MessageBox.alert('提示信息', '没有做任何修改！')
		}
	}

	// 左边的tbar
	var westcontbar = new Ext.Toolbar({
				items : [{
							id : 'btnAdd',
							iconCls : 'add',
							text : "增加",
							minWidth : 70,
							handler : westaddTheme
						}, '-', {
							id : 'btnDelete',
							iconCls : 'delete',
							minWidth : 70,
							text : "删除",
							handler : westdeleteTheme
						}, '-', {
							id : 'btnSave',
							iconCls : 'save',
							text : "保存修改",
							minWidth : 60,
							handler : westsaveTheme
						}]

			});

	// tbar
	var contbar = new Ext.Toolbar({
				items : [{
							id : 'btnAddItem',
							iconCls : 'add',
							text : "指标选择",
							minWidth : 70,
							disabled : true,
							handler : selectTheme
						}, '-', {
							id : 'btnDeleteItem',
							iconCls : 'delete',
							text : "删除",
							minWidth : 70,
							disabled : true,
							handler : deleteTheme
						}, '-', {
							id : 'btnSaveItem',
							iconCls : 'save',
							text : "保存修改",
							minWidth : 70,
							disabled : true,
							handler : saveTheme
						}]

			});

	var westgrid = new Ext.grid.EditorGridPanel({
				sm : westsm,
				ds : westds,
				cm : westcm,
				autoScroll : true,
				tbar : westcontbar,
				border : true,
				clicksToEdit : 1
			});

		westgrid.on('click',function(){
			if (westgrid.getSelectionModel().hasSelection()) {
					var selections = westgrid.getSelectionModel()
							.getSelections();
					if (selections.length == 1) {
						var seleced = westgrid.getSelectionModel()
								.getSelected();
						ds.load({
									params : {
										topicId : seleced.get('topicId')
									}
								})
						Ext.getCmp("btnAddItem").setDisabled(false) ;
						Ext.getCmp("btnDeleteItem").setDisabled(false);
						Ext.getCmp("btnSaveItem").setDisabled(false);
					}else
					{
						ds.removeAll();
						Ext.getCmp("btnAddItem").setDisabled(true) ;
						Ext.getCmp("btnDeleteItem").setDisabled(true);
						Ext.getCmp("btnSaveItem").setDisabled(true);
					}
					}else
					{
						ds.removeAll();
						Ext.getCmp("btnAddItem").setDisabled(true) ;
						Ext.getCmp("btnDeleteItem").setDisabled(true);
						Ext.getCmp("btnSaveItem").setDisabled(true);
					}
		})
		ds.on('load',function(){
		
		})
	var grid = new Ext.grid.EditorGridPanel({
				sm : sm,
				ds : ds,
				cm : cm,
				autoScroll : true,
				tbar : contbar,
				border : true,
				clicksToEdit : 1

			});
	grid.on('beforeedit',function(e){
		if(e.field == 'itemCode' || e.field == 'itemName')
			return false;
	})
	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				border : false,
				frame : false,
				items : [{
							title : '主题信息',
							region : 'west',
							split : true,
							collapsible : true,
							titleCollapse : true,
							margins : '1',
							width : 400,
							layoutConfig : {
								animate : true
							},
							layout : 'fit',
							items : [westgrid]
						}, {
							title : '指标信息',
							region : "center",
							split : true,
							collapsible : false,
							titleCollapse : true,
							margins : '1',
							layoutConfig : {
								animate : true
							},
							layout : 'fit',
							items : [grid]
						}]
			});

});