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
				dataIndex : "tool.code",
				hidden : true
			}, {
				header : "名称",
				dataIndex : "tool.name",
				// sortable : true,
				width : 100
			}, {
				header : "费用",
				dataIndex : "tool.fee",
				// sortable : true,
				width : 100

			}, {
				header : "类型",
				dataIndex : "typename",
				// sortable : true,
				width : 100
			}, {
				header : "来源",
				dataIndex : "tool.fromCom",
				// sortable : true,
				hidden : true,
				width : 100
			}]);
	//
	var ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'workbill/getToolList.action'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : "totalProperty",
							root : "list"
						}, [{
									name : "tool.id"
								}, {
									name : "tool.code"
								}, {
									name : "tool.name"
								}, {
									name : "tool.type"
								}, {
									name : "tool.fromCom"
								}, {
									name : 'tool.serUnit'
								}, {
									name : 'tool.fee'
								}, {
									name : 'tool.isUse'
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
						win.setTitle("新增工具");
						blockForm.getForm().reset();
						win.show(); // 显示表单所在窗体
					}
				}, {
					id : "updatecus",
					text : "修改",
					iconCls : 'update',
					handler : function() {
						method = "update";
						win.setTitle("修改工具");
						if (grid.getSelectionModel().hasSelection()) {
							var selrows = grid.getSelectionModel()
									.getSelections();
							if (selrows.length == 1) {
								win.show();
								var rec = selrows[0].data;
								blockForm.getForm().loadRecord(selrows[0]);
								var obj = new Object();
								obj.text = selrows[0].get("typename");
								obj.id = selrows[0].get("tool.type");
								servicetype.setValue(obj);

							} else {
								Ext.Msg.alert('提示信息', '请选择一个工具!');
							}
						}else{
							Ext.Msg.alert('提示','请选择要修改的工具！');
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
								if (member.get("tool.id")) {
									ids.push(member.get("tool.id"));
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
														'workbill/deleteTool.action',
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
				name : 'tool.id'
	})
	var servicecode = new Ext.form.TextField({
				id : "servicecode",
				fieldLabel : "工具编码",
				anchor : '90%',
				name : 'tool.code',
				emptyText : '自动生成',
				readOnly : true
			})
	var servicename = new Ext.form.TextField({
				id : "serviceName",
				allowBlank : false,
				fieldLabel : "工具名称<font color='red'>*</font>",
				name : 'tool.name',
				anchor : '90%',
				readOnly : false
			});
	var servicefee = new Ext.form.NumberField({
				id : "servicefee",
				allowBlank : false,
				fieldLabel : "工具费用<font color='red'>*</font>",
				name : 'tool.fee',
				anchor : '90%',
				readOnly : false
			});

	var servicetype = new Ext.ux.ComboBoxTree({
				fieldLabel : "工具类型<font color='red'>*</font>",
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
								dataUrl : 'workbill/findByPId.action'
							}),
					root : new Ext.tree.AsyncTreeNode({
								id : '0',
								name : '工具类别',
								text : '工具类别'
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
				name : 'tool.type'
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
							url = "workbill/addTool.action";
						} else if (method == "update") {
							url = "workbill/updateTool.action"
						} else {
							url = "undifine";
						}
						Ext.get("tool.type").dom.value = servicetype.value;
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
