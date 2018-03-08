Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() { 
	Ext.QuickTips.init();// 支持tips提示
	Ext.form.Field.prototype.msgTarget = 'side';// 提示的方式
	var tbar = new Ext.Toolbar({
		items : [{
			id : 'btnAdd',
			text : "新增类别",
			iconCls : 'add',
			handler : function() {
				addwin.setTitle("新增");
				typeform.getForm().reset();
				addwin.show('btnAdd');
				   
				name.on("change", function(filed, newValue, oldValue) {

					Ext.Ajax.request({
						url : 'manager/getRetrieveCode.action',
						params : {
							name : newValue
						},
						method : 'post',
						success : function(result, request) {
							var json = result.responseText;
							var o = eval("(" + json + ")");
							Ext.get("retrieveCode").dom.value = o.substring(0,
									8);
						}
					});
				});
			}
		}, '-', {
			id : 'btnUpdate',
			text : "修改类别",
			iconCls : 'update',
			handler : updateEmpType
		}, '-', {
			id : 'btnDel',
			text : "删除类别",
			iconCls : 'delete',
			handler : function() {
				var selectedRows = Grid.getSelectionModel().getSelections();
				if (selectedRows.length > 0) {
					var ids = "";
					Ext.Msg.confirm('提示', '删除的数据您将不能恢复,确定要删除吗?', function(b) {
						if (b == 'yes') {
							for (i = 0; i < selectedRows.length; i++) {
								ids += selectedRows[i].data.empTypeId + ",";
							}
							if (ids.length > 0) {
								ids = ids.substring(0, ids.length - 1);
							}
							Ext.Ajax.request({
								url : 'empTypeMaint.action',
								params : {
									method : 'delete',
									ids : ids
								},
								method : 'post',
								waitMsg : '正在删除数据...',
								success : function(result, request) {
									ds.load({
										params : {
											start : 0,
											limit : 18
										}
									})
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
		}, '-', {
			id : 'btnReflesh',
			text : "查询类别",
			iconCls : 'query',
			handler : function() {
				Grid.store.reload();
			}
		}]
	});
	
	function updateEmpType(){
		var selrows = Grid.getSelectionModel().getSelections();
		
				if (selrows.length>1)
				{
					Ext.Msg.alert('提示', '请选择一条记录进行修改！');
				}
				else if(selrows.length>0){
					var id = selrows[0].data.empTypeId;
					Ext.Ajax.request({
						url : 'empTypeModel.action',
						params : {
							id : id
						},
						method : 'post',
						waitMsg : '正在加载数据...',
						success : function(result, request) {
							var o = eval('(' + result.responseText + ')');
							Ext.get('empTypeId').dom.value = o.model.empTypeId;
							Ext.get('empTypeName').dom.value = o.model.empTypeName;
							usecom.setValue(o.model.isUse);
							Ext.get("retrieveCode").dom.value = o.model.retrieveCode;
						},
						failure : function(result, request) {
							Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
						}
					});
					addwin.setTitle("修改");
					addwin.show(Ext.get('btnUpdate'));
					name.on("change", function(filed, newValue, oldValue) {

					Ext.Ajax.request({
						url : 'manager/getRetrieveCode.action',
						params : {
							name : newValue
						},
						method : 'post',
						success : function(result, request) {
							var json = result.responseText;
							var o = eval("(" + json + ")");
							Ext.get("retrieveCode").dom.value = o.substring(0,
									8);
						}
					});
				});
				} else {
					Ext.Msg.alert('提示', '请选择要修改的记录！');
				}
	}

	var item = Ext.data.Record.create([{
		name : 'empTypeId'
	}, {
		name : 'empTypeName'
	}, {
		name : 'isUse'
	}, {
		name : 'retrieveCode'
	}]);

	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false
	});

	var item_cm = new Ext.grid.ColumnModel([sm, {
		header : '员工类别ID',
		dataIndex : 'empTypeId',
		hidden:true,
		width : 80,
		align : 'left'
	}, {
		header : '员工类别名称',
		dataIndex : 'empTypeName',
		width : 120,
		align : 'left'
	}, {
		header : '使用标志',
		dataIndex : 'isUse', 
		align : 'left',
		renderer : function(v) {
			if (v == 'Y') {//update by sychen 20100901
//			if (v == 'U') {
				return "使用";
			}
			if (v == 'N') {
				return "停用";

			}
			if (v == 'L') {
				return "注销";
			}
		}
	}, {
		header : '检索码',
		dataIndex : 'retrieveCode',
		align : 'left'
	}]);

	item_cm.defaultSortable = true;

	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'empTypeMaint.action'
		}),
		baseParams : {
			method : 'list'
		},
		reader : new Ext.data.JsonReader({
			totalProperty : "totalProperty",
			root : 'root'
		}, item)
	});
	// ds.load();
	ds.load({
		params : {
			start : 0,
			limit : 18
		}
	})
	var Grid = new Ext.grid.EditorGridPanel({
		//title : '员工类别信息查询',
		ds : ds,
		cm : item_cm,
		sm : sm,
		resizeable : true,
		autoScroll : true,
		tbar : tbar,
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : ds,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录",
			beforePageText:"页",
			afterPageText:"共{0}页"
		}),
		viewConfig : {
			forceFit : true
		}
	});
	Grid.on("dblclick" , function(){
		updateEmpType();
	}) ;

	layout = new Ext.Viewport({
		layout : "border",
		border : false,
		items : [{
			region : "center",
			layout : 'fit',
			collapsible : true,
			split : true,
			margins : '0 0 0 0',
			items : [Grid]

		}]
	});
	var usecom = new Ext.form.ComboBox({
		fieldLabel : '使用标志',
		store : new Ext.data.SimpleStore({
			fields : ['value', 'text'],
			data : [['Y', '使用'], ['L', '注销'], ['N', '停用']]//update by sychen 20100901
//			data : [['U', '使用'], ['L', '注销'], ['N', '停用']]
		}),
		id : 'isUse',
		name : 'isUse',
		valueField : "value",
		displayField : "text",
		value:'U',
		mode : 'local',
		typeAhead : true,
		forceSelection : true,
		hiddenName : 'value',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : false,
		emptyText : '请选择',
		width : 200
	});
	var name = new Ext.form.TextField({
		id : 'empTypeName',
		name : 'empTypeName',
		fieldLabel : '员工类别名称',
		allowBlank : false,
		width : 200
	});
	var typeform = new Ext.FormPanel({
		id : 'type-form',
		frame : true,
		lableAlign : 'right',
		labelWidth : 80,
		autoWidth : true,
		autoHeight : true,
		items : [new Ext.form.TextField({
			id : 'empTypeId',
			name : 'empTypeId',
			value : '自动生成',
			fieldLabel : '员工类别编号',
			readOnly : true,
			allowBlank : false,
			width : 200
		}), name, usecom, new Ext.form.TextField({
			id : 'retrieveCode',
			name : 'retrieveCode',
			fieldLabel : '检索码',
			allowBlank : false,
			width : 200
		})],
		buttons : [{
			text : '提交',
			iconCls : 'save',
			handler : function() {
				var method = (Ext.get('empTypeId').dom.value == '自动生成')
						? 'add'
						: 'update';
						
				if(!typeform.getForm().isValid())
				{
					return false;
				}
				typeform.getForm().submit({
					url : 'empTypeMaint.action',
					method : 'post',
					params : {
						method : method
					},
					success : function(form, action) {
						var message = eval('(' + action.response.responseText
								+ ')');
						var record = eval('(' + message.data + ')');
						if (method == 'add') {
							var p = new item({
								empTypeId : record.empTypeId,
								empTypeName : record.empTypeName,
								isUse : record.isUse,
								retrieveCode : record.retrieveCode
							});
							Ext.Msg.alert('提示', '增加成功！');
							Grid.store.reload();
						} else if (method == 'update') {
							var selectedRows = Grid.getSelectionModel()
									.getSelections();
							var r = selectedRows[selectedRows.length - 1];
							r.set("empTypeId", record.empTypeId);
							r.set("empTypeName", record.empTypeName);
							r.set("isUse", record.isUse);
							r.set("retrieveCode", record.retrieveCode);
						}
						Ext.Msg.alert('提示', '修改成功！');
						Grid.getView().refresh();
						addwin.hide();
					},
					failure : function(form, action) { 
						var error = eval('(' + action.response.responseText + ')');
						Ext.MessageBox.show({
							title : '错误',
							msg : '数据保存失败！' + error.errMsg,
							buttons : Ext.Msg.OK,
							icon : Ext.MessageBox.ERROR
						});
					}
				})
			}
		}, {
			text : '取消',
			iconCls : 'cancer',
			handler : function() {
				//typeform.getForm().reset();
				addwin.hide();
			}
		}]
	});

	var addwin = new Ext.Window({
		title : '新增员工类别信息',
		el : 'win',
		autoHeight : true,
		width : 400,
		closeAction : 'hide',
		modal:true,
		items : [typeform]
	})
});