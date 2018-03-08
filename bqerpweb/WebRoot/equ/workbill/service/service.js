Ext.onReady(function() {

	var method = "insert";
	// Ext.state.Manager.setProvider(new Ext.state.CookieProvider());

	var rn = new Ext.grid.RowNumberer({
				header : "序列号",
				// selectMode : 'true',
				width : 45
			});
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false
	})
	var cm = new Ext.grid.ColumnModel([sm,rn, {
				header : "",
				dataIndex : "service.code",
				hidden : true
			}, {
				header : "名称",
				dataIndex : "service.name",
				// sortable : true,
				width : 100
			}, {
				header : "费用",
				dataIndex : "service.fee",
				// sortable : true,
				width : 100

			}, {
				header : "类型",
				dataIndex : "typename",
				// sortable : true,
				width : 100
			}, {
				header : "来源",
				dataIndex : "service.fromCom",
				// sortable : true,
				hidden : true,
				width : 100
			}]);
	//
	var ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'workbill/getServiceList.action'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : "totalProperty",
							root : "list"
						}, [{
									name : "service.id"
								}, {
									name : "service.code"
								}, {
									name : "service.name"
								}, {
									name : "service.type"
								}, {
									name : "service.fromCom"
								}, {
									name : 'service.serUnit'
								}, {
									name : 'service.fee'
								}, {
									name : 'service.isUse'
								}, {
									name : 'typename'
								}])
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
	var grid = new Ext.grid.GridPanel({
		ds : ds,
		cm : cm,
		sm : sm,
		viewConfig : {
			forceFit : true
		},
		tbar : [{
					id : "addcus",
					text : "新增",
					iconCls : 'add',
					handler : function() {
						method = "insert";
						win.setTitle("新增服务");
						blockForm.getForm().reset();
						win.show(); // 显示表单所在窗体
					}
				}, {
					id : "updatecus",
					text : "修改",
					iconCls : 'update',
					handler : function() {
						method = "update";
						win.setTitle("修改服务");
						if (grid.getSelectionModel().hasSelection()) {
							var selrows = grid.getSelectionModel()
									.getSelections();
							if (selrows.length == 1) {
								win.show();
								var rec = selrows[0].data;
								// alert(Ext.encode(rec))
								// blockForm.getForm().loadRecord(rec);
								blockForm.getForm().loadRecord(selrows[0]);
								// serviceid.setValue(selrows[0].get("service.id"))
								// servicefee.setValue(selrows[0].get("service.fee"));
								// servicename.setValue(selrows[0].get("service.name"));
								var obj = new Object();
								obj.id = selrows[0].get("service.type");
								obj.text = selrows[0].get("typename");

								// alert(Ext.encode(obj))
								servicetype.setValue(obj);
							} else {
								Ext.Msg.alert('提示信息', '请选择一个服务!');
							}
						}else{
							Ext.Msg.alert('提示信息','请选择要修改的服务！');
						}
					}
				}, {
					id : "deletecus",
					text : "删除",
					iconCls : 'delete',
					handler : function() {
						var sm = grid.getSelectionModel();
						var selected = sm.getSelections();
						var ids = [];
						if (selected.length == 0 || selected.length < 0) {
							Ext.Msg.alert("提示", "请选择要删除的记录！");
						} else {
							for (var i = 0; i < selected.length; i += 1) {
								var member = selected[i];
								if (member.get("service.id")) {
									ids.push(member.get("service.id"));
								} 
//								else {
//									store.remove(store.getAt(i));
//								}
								Ext.Msg.confirm("删除", "是否确定删除的记录？", function(
										buttonobj) {
									if (buttonobj == "yes") {
										Ext.lib.Ajax
												.request(
														'POST',
														'workbill/deleteService.action',
														{
															success : function(
																	action) {
																Ext.Msg
																		.alert(
																				"提示",
																				"删除成功！")
																ds.reload();
															},
															failure : function() {
																Ext.Msg
																		.alert(
																				'错误',
																				'删除时出现未知错误.');
															}
														}, 'id='
																+ ids.join(","));
									}
								});
							}
						}
					}
				}],
		bbar : gridbbar
	});
	//
	ds.load({
				params : {
					start : 0,
					limit : 18

				}
			});
	grid.on("rowdblclick", function() {
				 Ext.get("updatecus").dom.click();
				// // alert()
			});
	//
	/**
	 * 以下是表单 __________________________________________________
	 */
	var serviceid = new Ext.form.Hidden({
				id : "serviceid",
				name : 'service.id'
	})
	var servicecode = new Ext.form.TextField({
				id : "servicecode",
				fieldLabel : "服务编码",
				anchor : '90%',
				name : 'service.code',
				emptyText : '自动生成',
				readOnly : true
			})
	var servicename = new Ext.form.TextField({
				id : "serviceName",
				allowBlank : false,
				fieldLabel : "服务名称<font color='red'>*</font>",
				name : 'service.name',
				anchor : '90%',
				readOnly : false
			});
	var servicefee = new Ext.form.NumberField({
				id : "servicefee",
				allowBlank : false,
				fieldLabel : "服务费用<font color='red'>*</font>",
				name : 'service.fee',
				anchor : '90%',
				readOnly : false
			});

	var servicetype = new Ext.ux.ComboBoxTree({
				fieldLabel : "服务类型<font color='red'>*</font>",
				// anchor : '100%',
				displayField : 'text',
				valueField : 'id',
				id : 'servicetype',
				allowBlank : false,
				blankText : '请选择',
				emptyText : '请选择',
				readOnly : true,
				anchor : "90%",
				tree : {
					xtype : 'treepanel',
					// 虚拟节点,不能显示
					rootVisible : false,
					loader : new Ext.tree.TreeLoader({
								dataUrl : 'workbill/findBySPId.action'
							}),
					root : new Ext.tree.AsyncTreeNode({
								id : '0',
								name : '服务类别',
								text : '服务类别'
							})
				},
				selectNodeModel : 'all',
				listeners : {
					'select' : function(combo, record, index) {
						servicetype.setValue(record.get('servicetype'));
					}
				}
			});
	var servicetypeId = new Ext.form.Hidden({
				id : 'servicetypeId',
				name : 'service.type'
			});
	// 定义一个记录
	var RoleRecord = Ext.data.Record.create([{
				name : "id"
			}, {
				name : "code"
			}, {
				name : "name"
			}, {
				name : "type"
			}, {
				name : "fromCom"
			}, {
				name : 'serUnit'
			}, {
				name : 'fee'
			}, {
				name : 'isUse'
			}]);
	// 表单对象
	var blockForm = new Ext.FormPanel({
		labelAlign : 'center',
		labelWidth : 90,
		// title : '新增/修改',
		frame : true,
		items : [serviceid,servicecode, servicename, servicetype, servicetypeId,
				servicefee]
		});
	// blockForm.render();
	// 窗体对象
	var win = new Ext.Window({
				el : 'window-win',
				width : 400,
				height : 200,
				closeAction : 'hide',
				modal : true,
				items : [blockForm],
				buttons : [{
					text : '保存',
					iconCls : 'save',
					handler : function() {
						var url = "";
						if (method == "insert") {
							url = "workbill/addService.action";
						} else if (method == "update") {
							url = "workbill/updateService.action"
						} else {
							url = "undifine";
						}
						Ext.get("service.type").dom.value = servicetype.value;
						if (blockForm.getForm().isValid()) {
							blockForm.getForm().submit({
								waitMsg : '保存中,请稍后...',
								url : url,
								success : function() {
									Ext.Msg.alert("注意", "操作成功！");
									blockForm.getForm().reset();
									win.hide();
									ds.reload();
								},
								failure : function(form, action) {
									var o = eval("("
											+ action.response.responseText
											+ ")");
									Ext.Msg.alert('错误', o.msg);
								}
							});
//							win.hide();
						}
					}
				}, {
					text : '取消',
					iconCls : 'cancer',
					handler : function() {
						blockForm.getForm().reset();
						win.hide();
					}
				}]
			});

	// // 设定布局器及面板
	new Ext.Viewport({
				enableTabScroll : true,
				layout : "fit",
				items : [grid]
			});

});
