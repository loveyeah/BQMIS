Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	Ext.QuickTips.init();
	var runlogId = getParameter('runlogId');
	var specialcode = getParameter('specialcode');
	var id;
	var tbar = new Ext.Toolbar({
		items : [{
			id : 'btnAdd',
			text : "安装",
			iconCls : 'add',
			handler : function() {
				addwin.setTitle("安装接地线闸刀");
				form.getForm().reset();
				addwin.show('btnAdd');
			}
		}, '-', {
			id : 'btnUpdate',
			text : "拆除",
			iconCls : 'update',
			handler : function() {
				var selrows = Grid.getSelectionModel().getSelections();
				if (selrows.length > 0) {
					Ext.Msg.confirm('提示', '确定要拆除吗?', function(b) {
						if (b == 'yes') {
							var record = Grid.getSelectionModel().getSelected();
							Ext.Ajax.request({
								url : 'runlog/backEarthRecord.action',
								params : {
									earthRecordId : record.data.earthRecordId,
									runlogid : runlogId
								},
								method : 'post',
								waitMsg : '正在保存数据...',
								success : function(result, request) {
									ds.reload();
								},
								failure : function(result, request) {
									Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
								}
							});
						}
					})
				} else {
					Ext.Msg.alert('提示', '请选择您要操作的记录！');
				}
			}
		}, '-', {
			id : 'btnDel',
			text : "删除",
			iconCls : 'delete',
			handler : function() {
				var selrows = Grid.getSelectionModel().getSelections();
				if (selrows.length > 0) {
					Ext.Msg.confirm('提示', '删除的数据您将不能恢复,确定要删除吗?', function(b) {
						if (b == 'yes') {
							var record = Grid.getSelectionModel().getSelected();
							Ext.Ajax.request({
								url : 'runlog/deleteEarthRecord.action',
								params : {
									earthRecordId : record.data.earthRecordId
								},
								method : 'post',
								waitMsg : '正在删除数据...',
								success : function(result, request) {
									ds.reload();
								},
								failure : function(result, request) {
									Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
								}
							});
						}
					});
				} else {
					Ext.Msg.alert('提示', '请选择您要删除的记录!');
				}
			}
		}]
	});

	var item = Ext.data.Record.create([{
		name : 'earthRecordId'
	}, {
		name : 'earthId'
	}, {
		name : 'earthName'
	}, {
		name : 'specialityName'
	}, {
		name : 'installManName'
	}, {
		name : 'installTime'
	}, {
		name : 'installPlace'
	}]);
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true
	});
	var item_cm = new Ext.grid.ColumnModel([sm,{
		header : '专业',
		dataIndex : 'specialityName',
		width : 40,
		align : 'center'
	}, {
		header : '地线名称',
		dataIndex : 'earthName',
		// width : 50,
		align : 'center'
	}, {
		header : '装设人',
		dataIndex : 'installManName',
		width : 40,
		align : 'center'
	}, {
		header : '装设时间',
		dataIndex : 'installTime',
		align : 'center'
	}, {
		header : '安装位置',
		dataIndex : 'installPlace',
		align : 'center'
	}]);
	item_cm.defaultSortable = true;
	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'runlog/findEarthRecordList.action'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'total',
			root : 'root'
		}, item)
	});
	ds.on('beforeload', function() {
		Ext.apply(this.baseParams, {
			specialcode : specialcode
		});
	});
	ds.load({
		params : {
			start : 0,
			limit : 18
		}
	});
	var gridbbar = new Ext.PagingToolbar({
		pageSize : 18,
		store : ds,
		displayInfo : true,
		displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
		emptyMsg : "没有记录",
		beforePageText : '页',
		afterPageText : "共{0}"
	});
	var Grid = new Ext.grid.GridPanel({
		ds : ds,
		cm : item_cm,
		sm : sm,
		autoWidth : true,
		autoScroll : true,
		bbar : gridbbar,
		tbar : tbar,
		border : false,
		viewConfig : {
			forceFit : true
		}
	});
	var layout = new Ext.Viewport({
		layout : 'fit',
		border : true,
		items : [{
			layout : 'fit',
			margins : '0 0 0 0',
			region : 'center',
			autoScroll : true,
			items : Grid
		}]
	});
	// --------------------------------------------------------------------------------------------------------------------------------------------------------

	var earthStore = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'runlog/getEarthList.action'
		}),
		reader : new Ext.data.JsonReader({
			id : "unit"
		}, [{
			name : 'earthId'
		}, {
			name : 'earthName'
		}])
	});
	earthStore.load();
	earthStore.on("load", function(xx, records, o) {
		earthBox.setValue(records[0].data.earthId);
	});

	var earthBox = new Ext.form.ComboBox({
		fieldLabel : '地线名称',
		id : 'earthBox',
		store : earthStore,
		valueField : "earthId",
		displayField : "earthName",
		mode : 'local',
		triggerAction : 'all',
		forceSelection : true,
		hiddenName : 'earthid',
		editable : false,
		allowBlank : false,
		selectOnFocus : true,
		name : 'earthId',
		anchor : '95%'
	});
	var content = new Ext.form.FieldSet({
		title : '地线维护',
		height : '100%',
		layout : 'form',
		items : [earthBox, {
			id : 'installPlace',
			name : 'installPlace',
			xtype : 'textarea',
			fieldLabel : '安装位置',
			readOnly : false,
			allowBlank : true,
			anchor : '95%'
		}],
		buttons : [{
			text : '保存',
			iconCls : 'save',
			handler : function() {
				var url = 'runlog/installEarthRecord.action';
				form.getForm().submit({
					url : url,
					method : 'post',
					params : {
						runlogid : runlogId,
						specialcode : specialcode
					},
					success : function(form, action) {
						var message = eval('(' + action.response.responseText
								+ ')');
						 Ext.Msg.alert("成功", message.data);
						ds.reload();
						addwin.hide();
					},
					failure : function(form, action) {
						Ext.Msg.alert('错误', '出现未知错误.');
					}
				})
			}

		}, {
			text : '取消',
			iconCls : 'cancer',
			handler : function() {
				form.getForm().reset();
				addwin.hide();
			}
		}]
	});

	var form = new Ext.form.FormPanel({
		bodyStyle : "padding:5px 5px 0",
		labelAlign : 'right',
		id : 'shift-form',
		labelWidth : 80,
		autoHeight : true,
		region : 'center',
		border : false,
		items : [content]
	});

	var addwin = new Ext.Window({
		title : '新增',
		el : 'win',
		autoHeight : true,
		width : 350,
		modal : true,
		closeAction : 'hide',
		items : [form]
	})
});