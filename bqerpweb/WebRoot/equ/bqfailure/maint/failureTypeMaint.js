// 设置树的点击事件

Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var isUseComboBox = new Ext.form.ComboBox({
				fieldLabel : '是否使用',
				store : new Ext.data.SimpleStore({
							fields : ["retrunValue", "displayText"],
							data : [['Y', '是'], ['N', '否']]
						}),
				valueField : "retrunValue",
				displayField : "displayText",
				mode : 'local',
				forceSelection : true,
				hiddenName : 'failureType.isUse',
				value : 'Y',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : false,
				anchor : '50%'
			});
	var needOverTimeComboBox = new Ext.form.ComboBox({
				fieldLabel : '是否需要统计超时',
				store : new Ext.data.SimpleStore({
							fields : ["retrunValue", "displayText"],
							data : [['Y', '是'], ['N', '否']]
						}),
				valueField : "retrunValue",
				displayField : "displayText",
				mode : 'local',
				forceSelection : true,
				hiddenName : 'failureType.needCaclOvertime',
				value : 'Y',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : false,
				anchor : '50%'
			});
	var gridForm = new Ext.FormPanel({
		id : 'failureType-form',
		frame : true,
		autoWidth : true,
		autoHeight : true,
		align : 'center',
		labelAlign : 'left',
		items : [{
			xtype : 'fieldset',
			labelAlign : 'right',
			title : '缺陷类别',
			defaultType : 'textfield',
			autoHeight : true,
			border : true,
			defaults : {
				width : 270
			},
			items : [{
						id : 'id',
						name : 'failureType.id',
						xtype : 'hidden'
					}, {
						fieldLabel : '类别编码',
						allowBlank : false,
						name : 'failureType.failuretypeCode'
					}, {
						fieldLabel : '类别名称',
						allowBlank : false,
						name : 'failureType.failuretypeName'
					}, {
						fieldLabel : '缺陷优先级',
						name : 'failureType.failurePri'
					}, {
						xtype : 'textarea',
						fieldLabel : '类别描述',
						name : 'failureType.failuretypeDesc'
					}, needOverTimeComboBox],
			buttons : [{
				text : '保存',
				iconCls : 'save',
				handler : function() {
					if (gridForm.getForm().isValid()) {
						gridForm.getForm().submit({
							url : 'bqfailure/'
									+ (Ext.get("failureType.id").dom.value == ""
											? "addFailureType"
											: "updateFailureType") + '.action',
							waitMsg : '正在保存数据...',
							method : 'post',
							success : function(form, action) {
								if (Ext.get("failureType.id").dom.value == "") {
									win.hide();
									Ext.Msg.alert('提示', action.result.msg);
								} else {
									win.hide();
									Ext.Msg.alert('提示', action.result.msg);
								}
								ds.load();
							},
							failure : function(form, action) {
								Ext.Msg.alert('提示', action.result.msg);
							}
						});
					}
				}
			}, {
				text : '取消',
				iconCls : 'cancer',
				handler : function() {
					win.hide();
				}
			}]
		}]
	});
	var FailureType = new Ext.data.Record.create([{
				name : 'id'
			}, {
				name : 'failuretypeCode'
			}, {
				name : 'failuretypeName'
			}, {
				name : 'failurePri'
			}, {
				name : 'failuretypeDesc'
			}, {
				name : 'needCaclOvertime'
			}, {
				name : 'isUse'
			}, {
				name : 'enterpriseCode'
			}]);
	var ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'bqfailure/findEquFailureTypelist.action',
							method : 'post'
						}),
				reader : new Ext.data.JsonReader({
							root : 'failureTypelist'
						}, FailureType)
			});
	ds.load();
	var colModel = new Ext.grid.ColumnModel([
			new Ext.grid.CheckboxSelectionModel(), new Ext.grid.RowNumberer({
						header : '序号',
						width : 35
					}), {
				id : 'id',
				header : 'id',
				dataIndex : 'id',
				hidden : true,
				width : 50
			}, {
				header : '类别编码',
				dataIndex : 'failuretypeCode',
				width : 80
			}, {
				header : '类别名称',
				dataIndex : 'failuretypeName',
				width : 130
			}, {
				header : '缺陷优先级',
				dataIndex : 'failurePri',
				width : 130
			}, {
				header : '类别描述',
				dataIndex : 'failuretypeDesc',
				width : 230
			}, {
				header : '是否需要计算超时',
				dataIndex : 'needCaclOvertime',
				width : 130,
				renderer : function changeIt(val) {
					return (val == "Y") ? "是" : "否";
				}
			}]);
	// 排序
	colModel.defaultSortable = true;
	var tbar = new Ext.Toolbar({
		items : [{
					id : 'btnAdd',
					text : "新增",
					iconCls : 'add',
					handler : function() {
						win.show();
						gridForm.getForm().reset();
						Ext.get("failureType.failuretypeCode").dom.focus();
					}
				}, '-', {
					id : 'btnDelete',
					text : "删除",
					iconCls : 'delete',
					handler : function() {
						var rec = failureTypeGrid.getSelectionModel()
								.getSelected();
						if (rec) {
							if (confirm("确定要删除\"" + rec.data.failuretypeName
									+ "\"类别吗？")) {
								Ext.Ajax.request({
									url : 'bqfailure/deleteFailureType.action?failureType.id='
											+ rec.get("id"),
									method : 'post',
									waitMsg : '正在删除数据...',
									success : function(result, request) {
										Ext.MessageBox.alert('提示','删除成功!');
										ds.load();
									},
									failure : function(result, request) {
										Ext.MessageBox.alert('提示','删除失败！');
									}
								});
							}
						} else {
							alert('请选择要删除的信息!');
						}
					}
				}, '-', {
					id : 'btnUpdate',
					text : "修改",
					iconCls : 'update',
					handler : function() {
						var rec = failureTypeGrid.getSelectionModel()
								.getSelected();
						if (rec) {
							win.show();
							Ext.get("failureType.id").dom.value = rec.get("id");
							Ext.get("failureType.failuretypeCode").dom.value = rec
									.get("failuretypeCode");
							Ext.get("failureType.failuretypeName").dom.value = rec
									.get("failuretypeName");
							Ext.get("failureType.failurePri").dom.value = rec
									.get("failurePri");
							Ext.get("failureType.failuretypeDesc").dom.value = rec
									.get("failuretypeDesc");
							Ext.get("failureType.needCaclOvertime").dom.value = rec
									.get("needCaclOvertime");
							Ext.get("failureType.isUse").dom.value = rec
									.get("isUse");
							Ext.get("failureType.enterpriseCode").dom.value = rec
									.get("enterpriseCode");
						} else {
							alert('请选择要修改的信息!');
						}
					}
				}, '-', {
					id : 'btnReflesh',
					text : "刷新",
					iconCls : 'reflesh',
					handler : function() {
						ds.load();
					}
				}]
	});
	// 单击Grid行事件
	var sm = new Ext.grid.RowSelectionModel({
				singleSelect : true
			});
	var failureTypeGrid = new Ext.grid.GridPanel({
				id : 'failureType-grid',
				autoScroll : true,
				//title : '缺陷类别列表',
				ds : ds,
				cm : colModel,
				sm : sm,
				tbar : tbar,
				border : true
			});
	failureTypeGrid.on('rowdblclick', function(grid, rowIndex, e) {
				win.show();
				var rec = failureTypeGrid.getStore().getAt(rowIndex);
				Ext.get("failureType.id").dom.value = rec.get("id");
				Ext.get("failureType.failuretypeCode").dom.value = rec
						.get("failuretypeCode");
				Ext.get("failureType.failuretypeName").dom.value = rec
						.get("failuretypeName");
				Ext.get("failureType.failurePri").dom.value = rec
						.get("failurePri");
				Ext.get("failureType.failuretypeDesc").dom.value = rec
						.get("failuretypeDesc");
				needOverTimeComboBox.setValue(rec.get("needCaclOvertime"));
			});

	var win = new Ext.Window({
				el : 'form-div',
				width : 500,
				height : 280,
				closeAction : 'hide',
				items : [gridForm]
			});
	var viewport = new Ext.Viewport({
				layout : 'fit',
				autoWidth : true,
				autoHeight : true,
				fitToFrame : true,
				items : [failureTypeGrid]
			});

	setTimeout(function() {
				Ext.get('loading').remove();
				Ext.get('loading-mask').fadeOut({
							remove : true
						});
			}, 250);
});