Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	var id = "";
	// 开始工龄
	var startSeniority = new Ext.form.NumberField({
				fieldLabel : "开始工龄",
				id : 'startSeniority',
				name : ' osalary.startSeniority',
				decimalPrecision : 0,
				maxLength : 2,
				anchor : '85%'
			})
	// 结束工龄
	var endSeniority = new Ext.form.NumberField({
				fieldLabel : "结束工龄",
				id : 'endSeniority',
				name : ' osalary.endSeniority',
				decimalPrecision : 0,
				maxLength : 2,
				anchor : '85%'
			})
	// 运行津贴薪点
	var salarypoint = new Ext.form.NumberField({
				id : 'salarypoint',
				name : 'osalary.operationSalaryPoint',
				decimalPrecision : 2,
				fieldLabel : '运行津贴薪点',
				anchor : '85%'
			})
	// 备注
	var memo = new Ext.form.TextArea({
				id : "memo",
				fieldLabel : '备注',
				name : 'osalary.memo',
				anchor : '85%'
			})

	// 弹窗的表单对象
	var blockForm = new Ext.form.FormPanel({
				labelAlign : 'left',
				frame : true,
				labelWidth : 50,
				items : [startSeniority,endSeniority,salarypoint, memo]
			});
	// 弹出窗体
	var win = new Ext.Window({
				width : 300,
				height : 250,
				buttonAlign : "center",
				items : [blockForm],
				layout : 'fit',
				buttons : [{
							text : '保存',
							iconCls : 'save',
							handler : function() {
								var myurl = "";
								if (id == "") {
									myurl = "com/saveOperationSalary.action";
								} else {
									myurl = "com/updateOperationSalary.action";
								}
								blockForm.getForm().submit({
											method : 'POST',
											url : myurl,
											params : {
												"osalary.operationSalaryId" : id
											},
											success : function(form, action) {
												Ext.Msg.alert('错误', '操作成功!');
												queryRecord();
												id = "";
												win.hide();
											},
											faliue : function() {
												Ext.Msg.alert('错误', '操作失败!');
											}
										});
							}
						}, {
							text : '取消',
							iconCls : 'cancer',
							handler : function() {
								win.hide();
							}
						}]

			});
	// 新建按钮
	var westbtnAdd = new Ext.Button({
				text : '新增',
				iconCls : 'add',
				handler : addRecord
			});

	// 修改按钮
	var westbtnedit = new Ext.Button({
				text : '修改',
				iconCls : 'update',
				handler : updateRecord
			});

	// 删除按钮
	var westbtndel = new Ext.Button({
				text : '删除',
				iconCls : 'delete',
				handler : deleteRecord
			});
	// 刷新按钮
	var westbtnref = new Ext.Button({
				text : '刷新',
				iconCls : 'reflesh',
				handler : function() {
					queryRecord();
				}
			});

	var westsm = new Ext.grid.CheckboxSelectionModel();

	var datalist = new Ext.data.Record.create([

	{
				name : 'operationSalaryId'
			}, {
				name : 'startSeniority'
			}, {
				name : 'endSeniority'
			}, {
				name : 'operationSalaryPoint'
			}, {
				name : 'memo'
			}, {
				name : 'isUse'
			}]);
			
	var westgrids = new Ext.data.JsonStore({
				url : 'com/getOperationSalaryList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : datalist
			});
			
	// 左边列表
	var westgrid = new Ext.grid.GridPanel({
				ds : westgrids,
				columns : [westsm, new Ext.grid.RowNumberer(), {
							header : "开始工龄",
							width : 40,
							sortable : true,
							dataIndex : 'startSeniority'
						}, {
							header : "结束工龄",
							width : 40,
							sortable : true,
							dataIndex : 'endSeniority'
						}, {
							header : "运行津贴薪点",
							width : 40,
							sortable : false,
							dataIndex : 'operationSalaryPoint'
						}, {
							header : "备注",
							width : 40,
							sortable : true,
							dataIndex : 'memo'
						}],
				viewConfig : {
					forceFit : true
				},
				tbar : [westbtnAdd, {
							xtype : "tbseparator"
						}, westbtnedit, {
							xtype : "tbseparator"
						}, westbtndel, {
							xtype : "tbseparator"
						}, westbtnref],
				sm : westsm,
				frame : true,
				bbar : new Ext.PagingToolbar({
							pageSize : 18,
							store : westgrids,
							displayInfo : true,
							displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
							emptyMsg : "没有记录"
						}),
				border : true,
				enableColumnHide : false,
				enableColumnMove : false,
				iconCls : 'icon-grid'
			});

	// westgrid.on("rowdblclick", updateRecord);
	function queryRecord() {
		westgrids.load({
					params : {
						start : 0,
						limit : 18
					}
				});
	}
	function addRecord() {
		blockForm.getForm().reset();
		win.show();
		win.setTitle("增加运行津贴维护");

	}
	function updateRecord() {
		if (westgrid.selModel.hasSelection()) {
			var records = westgrid.getSelectionModel().getSelections();
			id = records[0].data.operationSalaryId;
			win.show();
			var record = westgrid.getSelectionModel().getSelected();
			blockForm.getForm().reset();
			blockForm.form.loadRecord(record);
			salarypoint.setValue(record.get("operationSalaryPoint"))
			win.setTitle("修改运行津贴维护");
		} else {
			Ext.Msg.alert("提示", "请先选择要编辑的行!");
		}
	}

	function deleteRecord() {
		var records = westgrid.selModel.getSelections();
		var ids = [];
		if (records.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < records.length; i += 1) {
				var member = records[i];
				if (member.get("operationSalaryId")) {
					ids.push(member.get("operationSalaryId"));
				} else {
					store.remove(store.getAt(i));
				}
			}
			Ext.Msg.confirm("删除", "是否确定删除所选的记录？", function(buttonobj) {
						if (buttonobj == "yes") {
							Ext.lib.Ajax.request('POST',
									'com/delOperationSalary.action', {
										success : function(action) {
											Ext.Msg.alert("提示", "删除成功！")
											queryRecord();
										},
										failure : function() {
											Ext.Msg.alert('错误', '删除错误.');
										}
									}, 'ids=' + ids);
						}
					});
		}
	}
	// 设定布局器及面板
	var layout = new Ext.Viewport({
				layout : "border",
				border : false,
				items : [{
							region : 'center',
							layout : 'fit',
							border : false,
							margins : '1 0 2 1',
							collapsible : true,
							items : [westgrid]
						}]
			});
	queryRecord();
});