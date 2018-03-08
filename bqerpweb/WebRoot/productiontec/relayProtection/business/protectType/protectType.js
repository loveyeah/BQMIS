Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {

	// 系统当前时间
	function getDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate();
		s += (t > 9 ? "" : "0") + t + " ";

		return s;
	}

	// ***********!!!!!*******************
	
	// 从session取登录人编码姓名部门相关信息
	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						if (result.workerCode) {
							// 设定默认工号，赋给全局变量
							workerCode = result.workerCode;
							workerName = result.workerName;

						}
					}
				});
	}
	//继电保护类型编号
	var protectTypeId = new Ext.form.Hidden({
		fieldLabel : '继电保护类型编号',
		id : 'protectTypeId',
		name : 'pjc.protectTypeId',
		readOnly : true,
		width : 180
	})
	//名称 
	var protectTypeName = new Ext.form.TextField({
		fieldLabel : "继电保护类型名称",
		id : 'protectTypeName',
		name : 'pjc.protectTypeName',
		width : 180
	})
	
	


	// 弹窗的表单对象
	var blockForm = new Ext.form.FormPanel({
				labelAlign : 'right',
				frame : true,
				labelWidth : 120,
				style : 'padding:10px,5px,0px,5px',
				fileUpload : true,
				items : [protectTypeId,protectTypeName]

			});

	// 左边的弹出窗体
	var blockAddWindow;
	function showAddWindow() {
		if (!blockAddWindow) {
			blockAddWindow = new Ext.Window({
				title : '',
				layout : 'fit',
				width : 370,
				height : 130,
				modal : true,
				closable : true,
				border : false,
				resizable : false,
				closeAction : 'hide',
				plain : true,
				// 面板中按钮的排列方式
				buttonAlign : 'center',
				items : [blockForm],
				buttons : [{
					text : '保存',
					iconCls : 'save',
					handler : function() {
						if(Ext.get('protectTypeName').dom.value ==null || Ext.get('protectTypeName').dom.value == "")
						{
							Ext.Msg.alert("提示信息","继电保护类型名称不能为空!");
							return;
						}
						if (blockForm.getForm().isValid())
							if (op == "add") {
								blockForm.getForm().submit({
									waitMsg : '保存中,请稍后...',
									url : "productionrec/addRelayProtection.action",
									success : function(form, action) {
										var result = eval('('
														+ action.response.responseText
														+ ')');
												Ext.Msg.alert("提示信息",result.msg);
										blockForm.getForm().reset();
										blockAddWindow.hide();
										westgrids.reload();
									},
									failure : function(form, action) {
										var result = eval('('
														+ action.response.responseText
														+ ')');
												Ext.Msg.alert("提示信息",result.msg);
									}
								});
							} else if (op == "edit") {
								blockForm.getForm().submit({

									waitMsg : '保存中,请稍后...',
									url : "productionrec/updateRelayProtection.action",
									params : {
										// method : "edit",
										id : westgrid.getSelectionModel()
												.getSelected().get("protectTypeId")

									},
									success : function(form, action) {
										var result = eval('('
														+ action.response.responseText
														+ ')');
												
												// 显示成功信息
												Ext.Msg.alert("提示信息",result.msg);
										blockForm.getForm().reset();
										blockAddWindow.hide();
										westgrids.reload();
									},
									failure : function(form, action) {
										var result = eval('('
														+ action.response.responseText
														+ ')');
												Ext.Msg.alert("提示信息",result.msg);
										
									}
								});
							} else {
								Ext.MessageBox.alert('错误', '未定义的操作');
							}
					}
				}, {
					text : '取消',
					iconCls : 'cancer',
					handler : function() {
						blockForm.getForm().reset();
						blockAddWindow.hide();
					}
				}]
			});
		}
		if (op == "add") {
			blockForm.getForm().reset();
			// 新增时，赋初始值
			blockAddWindow.setTitle("新增继电保护类型维护");
		} else if (op == "edit") {
			blockAddWindow.setTitle("修改继电保护类型维护");
			blockAddWindow.show();
			var rec = westgrid.getSelectionModel().getSelected();
			Ext.get('protectTypeId').dom.value = rec.get('protectTypeId') ==null
				? "" : rec.get('protectTypeId')
			blockForm.getForm().loadRecord(rec);
		} else {
		}
		blockAddWindow.show()
	};

	// 左边按钮
	// 新建按钮
	var westbtnAdd = new Ext.Button({
				text : '新增',
				iconCls : 'add',
				handler : function() {
					op = "add";
					showAddWindow();
				}
			});

	// 选择判断
	function CKSelectdone() {
		var rec = westgrid.getSelectionModel().getSelections();
		if (rec.length != 1) {
			Ext.Msg.alert('提示信息', "请选择一行！");
			return false;
		} else {
			op = "edit";
			showAddWindow();
		}
	}

	// 修改按钮
	var westbtnedit = new Ext.Button({
				text : '修改',
				iconCls : 'update',
				handler : CKSelectdone
			});

	// 删除按钮
	var westbtndel = new Ext.Button({
		text : '删除',
		iconCls : 'delete',
		handler : function() {
			if (westgrid.getSelectionModel().getSelections().length > 0) {
				Ext.Msg.confirm('提示信息', '确认删除选中记录? ', function(button, text) {
					if (button == 'yes') {
						var rec = westgrid.getSelectionModel().getSelections();
						var str = "";
						for (var i = 0; i < rec.length; i++) {
							if (i < rec.length - 1)
								str += rec[i].data.protectTypeId + ",";
							else
								str += rec[i].data.protectTypeId;
						}
						Ext.Ajax.request({
									waitMsg : '删除中,请稍后...',
									url : 'productionrec/deleteRelayProtection.action',
									params : {
										ids : str
									},
									success : function(response, options) {
										westgrids.reload();
									},
									failure : function() {
										Ext.Msg.alert('提示信息', '服务器错误,请稍候重试!')
									}
								});
					}
				})
			} else {
				Ext.Msg.alert('提示信息', "请至少选择一行！");
				return false;
			}
		}
	});

	// 刷新按钮
	var westbtnref = new Ext.Button({
				text : '刷新',
				iconCls : 'reflesh',
				handler : function() {
					westgrids.load({
								params : {
									start : 0,
									limit : 18
								}
							});
				}
			});

	var westsm = new Ext.grid.CheckboxSelectionModel();
	// 左边列表中的数据
	var datalist = new Ext.data.Record.create([

	{
				name : 'protectTypeId'
			}, {
				name : 'protectTypeName'
			}]);

	var westgrids = new Ext.data.JsonStore({
				url : 'productionrec/findRelayProtectionList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : datalist
			});

	westgrids.load({
				params : {
					start : 0,
					limit : 18
				}
			});

	// 左边列表
	var westgrid = new Ext.grid.GridPanel({
				ds : westgrids,
				columns : [westsm, new Ext.grid.RowNumberer({
									header : "行号",
									width : 31
								}), {
							header : "继电保护类型编号",
							width : 120,
							align : "center",
							sortable : true,
							hidden : true,
							dataIndex : 'protectTypeId'
						}, {
							header : "名称",
							width : 200,
							align : "center",
							sortable : false,
							dataIndex : 'protectTypeName'
						}],
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
							displayMsg : "{0} 到 {1} /共 {2} 条",
							emptyMsg : "没有记录"
						}),
				border : true,
				enableColumnHide : false,
				enableColumnMove : false,
				iconCls : 'icon-grid'
			});

	// westgrid 的事件
	westgrid.on('rowdblclick', function(grid, rowIndex, e) {
				CKSelectdone()
			})

	// 设定布局器及面板
	var layout = new Ext.Viewport({
				layout : "border",
				border : false,
				items : [{
							region : 'center',
							layout : 'fit',
							border : false,
							margins : '1 0 1 1',
							collapsible : true,
							items : [westgrid]

						}]
			});
});