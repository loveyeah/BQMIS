Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
	var id;
	var method;
	var tbar = new Ext.Toolbar({
		items : [{
			id : 'btnAdd',
			text : "新增",
			iconCls : 'add',
			handler : function() {
				addwin.setTitle("增加地线信息");
				form.getForm().reset();
				addwin.show('btnAdd');
				method = 'add';
			}
		}, '-', {
			id : 'btnUpdate',
			text : "修改",
			iconCls : 'update',
			handler : function() {
				method = 'update';
				var selrows = Grid.getSelectionModel().getSelections();
				if (selrows.length > 0) {
					if(selrows.length > 1)
					{
						Ext.Msg.alert('提示', '请选择一条修改的记录！');
						return false;
					}
					var record = Grid.getSelectionModel().getSelected();
					form.getForm().reset();
					addwin.show(Ext.get('btnUpdate'));
					form.getForm().loadRecord(record);
					addwin.setTitle("修改地线信息");
				} else {
					Ext.Msg.alert('提示', '请选择您要修改的记录！');
				}
			}
		}, '-', {
			id : 'btnDel',
			text : "删除",
			iconCls : 'delete',
			handler : function() {
				var selrows = Grid.getSelectionModel().getSelections();
				if (selrows.length > 0) {
					var ids = [];
					Ext.Msg.confirm('提示', '删除的数据您将不能恢复,确定要删除吗?', function(b) {
						for (var i = 0; i < selrows.length; i += 1) {
							var member = selrows[i].data;
							if (member.earthId) {
								ids.push(member.earthId);
							}
						}
						Ext.Ajax.request({
							url : 'runlog/delteEarth.action',
							params : {
								ids : ids.join(",")
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

					});
				} else {
					Ext.Msg.alert('提示', '请选择您要删除的记录!');
				}
			}
		}]
	});

	var item = Ext.data.Record.create([{
		name : 'earthId'
	}, {
		name : 'earthName'
	}, {
		name : 'memo'
	}, {
		name : 'displayNo'
	}]);
	var sm = new Ext.grid.CheckboxSelectionModel({});
	var item_cm = new Ext.grid.ColumnModel([sm, {
		header : '名称',
		dataIndex : 'earthName',
		width : 40,
		align : 'left'
	}, {
		header : '备注',
		dataIndex : 'memo',
		align : 'left'
	}, {
		header : '顺序',
		dataIndex : 'displayNo',
		width : 20,
		align : 'left'
	}]);
	item_cm.defaultSortable = true;
	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'runlog/getEarthList.action'
		}),
		reader : new Ext.data.JsonReader({}, item)
	});
	ds.load();
	var Grid = new Ext.grid.GridPanel({
		ds : ds,
		cm : item_cm,
		sm : sm,
		autoWidth : true,
		autoScroll : true,
		tbar : tbar,
		border : false,
		viewConfig : {
			forceFit : true
		}
	});
	Grid.on('rowdblclick', function(grid, rowIndex, e) {
		method = 'update';
		var record = Grid.getStore().getAt(rowIndex);
		form.getForm().reset();
		addwin.show(Ext.get('btnUpdate'));
		form.getForm().loadRecord(record);
		addwin.setTitle("修改地线信息");
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

	var content = new Ext.form.FieldSet({
		title : '地线维护',
		height : '100%',
		layout : 'form',
		items : [{
			id : 'earthId',
			name : 'earth.earthId',
			xtype : 'numberfield',
			fieldLabel : '序号',
			readOnly : false,
			hidden:true,
            hideLabel:true,
			anchor : '95%'
		},{
			id : 'earthName',
			name : 'earth.earthName',
			xtype : 'textfield',
			fieldLabel : '地线名称',
			readOnly : false,
			allowBlank : false,
			anchor : '95%'
		}, {
			id : 'memo',
			name : 'earth.memo',
			xtype : 'textarea',
			fieldLabel : '备注',
			readOnly : false,
			allowBlank : true,
			anchor : '94.5%'
		}, {
			id : 'displayNo',
			name : 'earth.displayNo',
			xtype : 'numberfield',
			fieldLabel : '初始次序',
			readOnly : false,
			anchor : '95%'
		}],
		buttons : [{
			text : '保存',
			iconCls : 'save',
			handler : function() {
				var url = '';
				if (method == "add") {
					url = 'runlog/addEarth.action';

				} else {
					url = 'runlog/updateEarth.action';
				}
				if(!form.getForm().isValid())
				{
					return false;
				}
				form.getForm().submit({
					url : url,
					method : 'post',
					params : {},
					success : function(form, action) {
						var message = eval('(' + action.response.responseText
								+ ')');
						// Ext.Msg.alert("成功", message.data);
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
		modal:true,
		autoHeight : true,
		width : 450,
		closeAction : 'hide',
		items : [form]
	})
});