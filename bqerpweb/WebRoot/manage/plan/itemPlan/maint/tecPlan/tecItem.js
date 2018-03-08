Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	var args = window.dialogArguments;
	// 计量单位
//	var unitData = Ext.data.Record.create([{
//				name : 'unitName'
//			}, {
//				name : 'unitId'
//			}]);
//	var allUnitStore = new Ext.data.JsonStore({
//				url : 'manager/getUnitList.action',
//				root : 'list',
//				fields : unitData
//			});
//
//	allUnitStore.load();

	var con_item = Ext.data.Record.create([{
				name : 'technologyItemId',
				mapping : 0
			}, {
				name : 'itemCode',
				mapping : 1
			}, {
				name : 'itemName',
				mapping : 2
			}, {
				name : 'alias',
				mapping : 3
			}, {
				name : 'unitId',
				mapping : 4
			}, {
				name : 'displayNo',
				mapping : 5
			}
			// add by liuyi 091222 
			,{
				name : 'unitName',
				mapping : 8
			}
			]);

	var con_sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : false
			});
	var con_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'manageitemplan/getTecItemList.action'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : "totalCount",
							root : "list"
						}, con_item)
			});
	con_ds.load()

	var unitNs = new Power.unit({});
	unitNs.btnConfirm.on('click',function(){
		if(Grid.getSelectionModel().hasSelection()){
			var rec = Grid.getSelectionModel().getSelected();
			var aUnit = unitNs.getValue();
			rec.set('unitId',aUnit.unitId);
			rec.set('unitName',aUnit.unitName);
		}
	})
	var con_item_cm = new Ext.grid.ColumnModel([con_sm,

			new Ext.grid.RowNumberer(), {
				header : '指标别名',
				dataIndex : 'alias',
				align : 'center',
				editor : new Ext.form.TextField()
			}, {
				header : '指标编码',
				dataIndex : 'itemCode',
				align : 'center'
			}, {
			    header : '指标名称',
				dataIndex : 'itemName',
				align : 'center'
			}, {
				header : '计量单位',
				dataIndex : 'unitName',
				align : 'center',
				editor : unitNs.combo
//				new Ext.form.ComboBox({
//							fieldLabel : '计量单位',
//							store : allUnitStore,
//							id : 'unitName',
//							name : 'unitName',
//							valueField : "unitId",
//							displayField : "unitName",
//							mode : 'local',
//							typeAhead : true,
//							forceSelection : true,
//							editable : false,
//							triggerAction : 'all',
//							selectOnFocus : true,
//							allowBlank : true,
//							emptyText : '请选择',
//							anchor : '85%',
//							listeners : {
//								render : function() {
//									this.clearInvalid();
//								}
//							}
//						}),
//				renderer : function(v) {
//					if (v != null) {
//						var recIndex = allUnitStore.find("unitId", v);
//						return allUnitStore.getAt(recIndex).get("unitName");
//					}
//				}
			}, {
				header : '显示顺序',
				dataIndex : 'displayNo',
				align : 'center',
				editor : new Ext.form.NumberField({
							style : 'cursor:pointer',
							allowNegative : false,
							allowDecimal : false,
							allowBlank : true
						})
			}]);

	var itemNs = new Power.operateItem({});
	itemNs.confirmBtu.on('click',function(){
			var rvo = itemNs.saveRecords();
			if (typeof(rvo) != "undefined" && rvo.length > 0) {
				for (var i = 0; i < rvo.length; i++) {
						var count = con_ds.getCount();
						var currentIndex = count;
						var record = new con_item({
									technologyItemId : null,
									itemCode : null,
									itemName : null,
									alias : null,
									unitId : null,
									displayNo : null
								})
						Grid.stopEditing();
						con_ds.insert(currentIndex, record);
						con_sm.selectRow(currentIndex);
						record.set('itemCode',rvo[i].itemCode);
						record.set('itemName',rvo[i].itemName);
						record.set('alias',rvo[i].itemName);
						record.set('unitId',rvo[i].unitId);
						record.set('unitName',rvo[i].unitName);
						Grid.startEditing(currentIndex, 4);
					}
			}
		})	
	
	// 增加
	function addTopic() {
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
//			var count = con_ds.getCount();
//			var currentIndex = count;
//			var record = new con_item({
//						technologyItemId : null,
//						itemCode : null,
//						itemName : null,
//						alias : null,
//						unitId : null,
//						displayNo : null
//					})
//			Grid.stopEditing();
//			con_ds.insert(currentIndex, record);
//			con_sm.selectRow(currentIndex);
//			record.set('itemCode', rvo.id);
//			record.set('itemName', rvo.text);
//			record.set('alias', rvo.text);
////			var unitDes = rvo.href;
//			record.set("unitId", rvo.href);
//						record.set("unitName", rvo.cls);
////			if (unitDes) {
////				var str = unitDes.toString().split(',');
////				record.set("unitId", str[0]);
////				record.set("unitName", str[1]);
////			}
//			Grid.startEditing(currentIndex, 4);
//		}
	}

	// 删除记录
	var itemIds = new Array();
	function deleteTopic() {
		Grid.stopEditing();
		var sm = Grid.getSelectionModel();
		var selected = sm.getSelections();
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i];
				if (member.get("technologyItemId") != null) {
					itemIds.push(member.get("technologyItemId"));
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
		if (modifyRec.length > 0 || itemIds.length > 0) {
			Ext.Msg.confirm('提示信息', '确定要保存修改吗？', function(button) {
				if (button == 'yes') {
					var addData = new Array();
					var updateData = new Array();
					for (var i = 0; i < modifyRec.length; i++) {
						if (modifyRec[i].get('technologyItemId') != null) {
							updateData.push(modifyRec[i].data);
						} else {
							addData.push(modifyRec[i].data);
						}

					}
					Ext.Msg.wait('操作正在进行中……');
					Ext.Ajax.request({
								url : 'manageitemplan/saveOrUpdateTecItem.action',
								method : 'post',
								params : {
									isAdd : Ext.util.JSON.encode(addData),
									isUpdate : Ext.util.JSON.encode(updateData),
									ids : itemIds.join(",")
								},
								success : function(result, request) {
									var o = Ext.util.JSON
											.decode(result.responseText);
									Ext.Msg.alert("提示信息", o.msg);
									con_ds.rejectChanges();
									itemIds = [];
									con_ds.reload();
								},
								failure : function(result, request) {
									Ext.Msg.alert("提示信息", "数据保存修改失败！");
									con_ds.rejectChanges();
									itemIds = [];
									con_ds.reload();
								}
							})
				}
			})

		} else {
			Ext.MessageBox.alert('提示信息', '没有做任何修改！')
		}
	}
	// 取消
	function cancerTopic() {
		var modifyRec = con_ds.getModifiedRecords();
		if (modifyRec.length > 0 || itemIds.length > 0) {
			if (!confirm("确定要放弃修改吗"))
				return;
			con_ds.reload();
			con_ds.rejectChanges();
			itemIds = [];
		} else {
			con_ds.reload();
			con_ds.rejectChanges();
			itemIds = [];
		}
	}
	var contbar = new Ext.Toolbar({
				items : [{
							id : 'btnAdd',
							iconCls : 'add',
							text : "指标选择",
							handler : addTopic

						}, {
							id : 'btnDelete',
							iconCls : 'delete',
							text : "删除",
							handler : deleteTopic

						}, {
							id : 'btnCancer',
							iconCls : 'cancer',
							text : "取消",
							handler : cancerTopic

						}, '-', {
							id : 'btnSave',
							iconCls : 'save',
							text : "保存修改",
							handler : saveTopic
						}]

			});
	var Grid = new Ext.grid.EditorGridPanel({
				sm : con_sm,
				ds : con_ds,
				cm : con_item_cm,
				autoScroll : true,
				tbar : contbar,
				border : true,
				clicksToEdit : 1,
				viewConfig : {
					forceFit : true
				}
			});

	if (typeof(args) != "undefined" && typeof(args) == 'object') {
		contbar.setDisabled(true)
		Grid.on("click", function() {
					var rec = Grid.selModel.getSelected();
					window.returnValue = rec;
					window.close();
				})
	}

	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				border : false,
				frame : false,
				items : [{
							layout : 'fit',
							border : false,
							frame : false,
							region : "center",
							items : [Grid]
						}]
			});

})