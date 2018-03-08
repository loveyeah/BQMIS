Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
var date = new Date();
	var dateTime = new Ext.form.DateField({
		fieldLabel : '修改日期',
		format : 'Y-m-d H:i:s',
		name : 'character.lastModifiedDate',
		// value : '2008-11',
		id : 'lastModifiedDate',
		itemCls : 'sex-left',
		clearCls : 'allow-float',
		checked : true,
		readOnly:true, 
		value : date,
		readOnly : true,
		anchor : '90%'
	});
	var gridForm = new Ext.FormPanel({
		id : 'conClientCharacter-form',
		frame : true,
		autoWidth : true,
		autoHeight : true,
		align : 'center',
		labelAlign : 'left',
		items : [{
			xtype : 'fieldset',
			labelAlign : 'right',
			title : '公司性质维护',
			defaultType : 'textfield',
			autoHeight : true,
			border : true,
			items : [{
				name : 'character.characterId',
				xtype : 'hidden'
			}, {
				fieldLabel : '公司性质编码',
				allowBlank : false,
				anchor : '90%',
				name : 'character.characterCode',
				xtype : 'hidden'
			}, {
				fieldLabel : '公司性质名称',
				allowBlank : false,
				anchor : '90%',
				name : 'character.characterName'
			}, {
				xtype : 'hidden',
				anchor : '90%',
				name : 'character.lastModifiedBy'
			}, {
				fieldLabel : '修改人',
				readOnly : true,
				anchor : '90%',
				name : 'lastModifiedName'
			}, dateTime, {
				fieldLabel : '备注',
				anchor : '90%',
				xtype : "textarea",
				name : 'character.memo'
			}],
			buttons : [{
				text : '确定',
				iconCls : 'save',
				handler : function() {
					if (gridForm.getForm().isValid()) {
						gridForm.getForm().submit({
							url : 'managecontract/'
									+ (Ext.get("character.characterId").dom.value == ""
											? "managecontract/addConClientCharacter"
											: "managecontract/updateConClientCharacter")
									+ '.action',
							waitMsg : '正在保存数据...',
							method : 'post',
							success : function(form, action) {
								if (Ext.get("character.characterId").dom.value == "") {
									ds.load();
									win.hide();
								} else {
									ds.load();
									win.hide();
									Ext.Msg.alert('提示', '修改成功!');
								}
								
							},
							failure : function(form, action) {
								var o = eval("("
											+ action.response.responseText
											+ ")");
									Ext.Msg.alert("注意", o.errMsg);
							}
						});
					}
				}
			}, {
				text : '关闭',
				iconCls : 'cancer',
				handler : function() {
					gridForm.getForm().reset();
					win.hide();
				}
			}]
		}]
	});

	var conCharacter = new Ext.data.Record.create([{
		name : 'characterId'
	}, {
		name : 'characterCode'
	}, {
		name : 'characterName'
	}, {
		name : 'isUse'
	}, {
		name : 'memo'
	}, {
		name : 'lastModifiedName'
	}, {
		name : 'lastModifiedDate'
	}]);
	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'managecontract/findConClientCharacter.action',
			method : 'post'
		}),
		reader : new Ext.data.JsonReader({
				 root : 'list'
				}, conCharacter)
	});
	ds.load();
	var box2 = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false,
		listeners : {
			rowselect : function(sm, row, rec) {
			}
		}
	});

	var colModel = new Ext.grid.ColumnModel([box2, {
		id : 'characterId',
		header : 'ID',
		dataIndex : 'characterId',
		sortable : true,
		hidden : true,
		width : 200
	}, {
		id : 'characterCode',
		header : '公司性质编码',
		dataIndex : 'characterCode',
		width : 200,
		hidden : true,
		sortable : true
	}, {
		id : 'characterName',
		header : '公司性质名称',
		dataIndex : 'characterName',
		align:'center',
		width : 200
	},{
		header : '备注',
		dataIndex : 'memo',
		align:'center',
		width : 200
	},
	{
		header : "修改人",
			width : 200,
			sortable : true,
			dataIndex : 'lastModifiedName',
			align:'center'
	}, {
		header : "修改时间",
			width : 200,
			sortable : true,
			dataIndex : 'lastModifiedDate',
			align:'center'
	}]);
	// 排序
	colModel.defaultSortable = true;
	var tbar = new Ext.Toolbar({
		items : [{
			id : 'btnAdd',
			text : "增加",
			iconCls : 'add',
			handler : function() {
				gridForm.getForm().reset();
				win.show();
				Ext.get("character.characterName").dom.focus();
			}
		}, '-', {
			id : 'btnUpdate',
			text : "修改",
			iconCls : 'update',
			handler : function() {
				var rec = conCharacterGrid.getSelectionModel().getSelections();
				if (rec.length != 1) {
					Ext.Msg.alert("提示", "请选择一行！");
					return false;
				} else {
					win.show();
					var rec = conCharacterGrid.getSelectionModel().getSelected();
					
					Ext.get("character.characterId").dom.value = rec.get("characterId");
					Ext.get("character.characterCode").dom.value = rec.get("characterCode");
					Ext.get("character.characterName").dom.value = rec.get("characterName");
					Ext.get("character.lastModifiedBy").dom.value = rec.get("lastModifiedBy");
					Ext.get("lastModifiedName").dom.value = rec.get("lastModifiedName");
					Ext.get("character.lastModifiedDate").dom.value = rec.get("lastModifiedDate");
				if(rec.get("memo")!=null)
				{
					Ext.get("character.memo").dom.value = rec.get("memo");
				}
			}
		}
	}, '-', {
			id : 'btnDelete',
			text : "删除",
			iconCls : 'delete',
			handler : function() {
				var rec = conCharacterGrid.getSelectionModel().getSelections();
				var names = "";
				if (rec.length == 0) {
					Ext.Msg.alert("提示", "请选择要删除的记录！");
				} else {
					for (i = 0; i < rec.length; i++) {
						names += rec[i].data.characterName + ",";
					}
					names = names.substring(0, names.length - 1);

					if (confirm("确定要删除\"" + names + "\"记录吗？")) {
						for (i = 0; i < rec.length; i++) {
							Ext.Ajax.request({
								url : 'managecontract/deleteConClientCharacter.action?character.characterId='
										+ rec[i].get("characterId"),
								method : 'post',
								waitMsg : '正在删除数据...',
								success : function(result, request) {
									Ext.Msg.alert('提示', '删除成功!');
									ds.load();
								},
								failure : function(result, request) {
									Ext.Msg.alert('提示', '删除失败!');
								}
							});
						}
					}
				}
			}
		}]

	});

	var conCharacterGrid = new Ext.grid.GridPanel({
		id : 'conclientcharacter-grid',
		autoScroll : true,
		title : '公司性质列表',
		ds : ds,
		cm : colModel,
		sm : box2,
		tbar : tbar,
		border : true
	});
	conCharacterGrid.on('rowdblclick', function(grid, rowIndex, e) {
		win.show();
		var rec = conCharacterGrid.getStore().getAt(rowIndex);
	//	gridForm.getForm().loadRecord(rec);
		Ext.get("character.characterId").dom.value = rec.get("characterId");
		Ext.get("character.characterCode").dom.value = rec.get("characterCode");
		Ext.get("character.characterName").dom.value = rec.get("characterName");
		Ext.get("character.lastModifiedBy").dom.value = rec.get("lastModifiedBy");
		Ext.get("lastModifiedName").dom.value = rec.get("lastModifiedName");
		Ext.get("character.lastModifiedDate").dom.value = rec.get("lastModifiedDate");
		if(rec.get("memo")!=null)
		{
		Ext.get("character.memo").dom.value = rec.get("memo");
		}
	});
	var win = new Ext.Window({
		el : 'form-div',
		width : 400,
		height : 260,
		modal:true,
		closeAction : 'hide',
		items : [gridForm]
	});
	var viewport = new Ext.Viewport({
		region : "center",
		layout : 'fit',
		// layout : "border",
		autoWidth : true,
		autoHeight : true,
		fitToFrame : true,
		items : [conCharacterGrid]
	});

	setTimeout(function() {
		Ext.get('loading').remove();
		Ext.get('loading-mask').fadeOut({
			remove : true
		});
	}, 250);
});