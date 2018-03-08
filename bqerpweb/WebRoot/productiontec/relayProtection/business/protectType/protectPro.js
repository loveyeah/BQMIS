Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	//继电保护定值项目编号
	var fixvalueItemId = new Ext.form.Hidden({
		fieldLabel : '继电保护定值项目编号',
		id : 'fixvalueItemId',
		name : 'pjcd.fixvalueItemId',
		readOnly : true,
		width : 180
	})
	//继电保护类型名称
	var protectTypeName = new Ext.form.TextField({
		fieldLabel : '继电保护类型名称',
		id : 'protectTypeName',
		name : 'protectTypeName',
		readOnly : true,
		width : 180
	})
	
	 protectTypeName.onClick(selectType);
	 function selectType() {
    	this.sValue = this.getValue();
    	if (Ext.get('protectTypeName').dom.disabled) {
    		return;
    	}
		var supply = window.showModalDialog(
				'../typeSelect.jsp',
				window, 'dialogWidth=400px;dialogHeight=300px;status=no');
		if (typeof(supply) != "undefined" && this.sValue != supply.supplier) {
			protectTypeName.setValue(supply.protectTypeName);
			protectTypeId.setValue(supply.protectTypeId);
		}
	 }
	//继电保护类型编号
	var protectTypeId = new Ext.form.Hidden({
		id : 'protectTypeId',
		name : 'pjcd.protectTypeId'
	})
	//名称 
	var fixvalueItemName = new Ext.form.TextField({
		fieldLabel : "继电保护定值项目名称",
		id : 'fixvalueItemName',
		name : 'pjcd.fixvalueItemName',
		width : 180
	})
	
	


	// 弹窗的表单对象
	var blockForm = new Ext.form.FormPanel({
				labelAlign : 'right',
				frame : true,
				labelWidth : 130,
				style : 'padding:10px,5px,0px,5px',
				fileUpload : true,
				items : [fixvalueItemId,protectTypeName,protectTypeId,fixvalueItemName]

			});

	// 左边的弹出窗体
	var blockAddWindow;
	function showAddWindow() {
		if (!blockAddWindow) {
			blockAddWindow = new Ext.Window({
				title : '',
				layout : 'fit',
				width : 400,
				height : 150,
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
						if(Ext.get('fixvalueItemName').dom.value ==null || Ext.get('fixvalueItemName').dom.value == "")
						{
							Ext.Msg.alert("提示信息","继电保护定值项目名称不能为空!");
							return;
						}
						if(Ext.get('protectTypeName').dom.value == null || Ext.get('protectTypeName').dom.value == "")
						{
							Ext.Msg.alert("提示信息","继电保护类型名称不能为空，请选择！")
							return;
						}
						if (blockForm.getForm().isValid())
							if (op == "add") {
								blockForm.getForm().submit({
									waitMsg : '保存中,请稍后...',
									url : "productionrec/addPtJdbhCDzxmwh.action",
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
									url : "productionrec/updatePtJdbhCDzxmwh.action",
									params : {
										id : westgrid.getSelectionModel()
												.getSelected().get("fixvalueItemId")

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
			blockAddWindow.setTitle("新增继电定值项目维护");
		} else if (op == "edit") {
			blockAddWindow.setTitle("修改继电定值项目维护");
			blockAddWindow.show();
			var rec = westgrid.getSelectionModel().getSelected();
			Ext.get('fixvalueItemId').dom.value = rec.get('pjcd.fixvalueItemId') ==null
				? "" : rec.get('pjcd.fixvalueItemId');
			Ext.get('protectTypeId').dom.value = rec.get('pjcd.protectTypeId') ==null
				? "" : rec.get('pjcd.protectTypeId');
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
								str += rec[i].get("pjcd.fixvalueItemId") + ",";
							else
								str += rec[i].get("pjcd.fixvalueItemId");
						}
						Ext.Ajax.request({
									waitMsg : '删除中,请稍后...',
									url : 'productionrec/deletePtJdbhCDzxmwh.action',
									params : {
										ids : str
									},
									success : function(response, options) {
										Ext.Msg.alert("提示信息","数据删除成功！ ");
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
				name : 'pjcd.fixvalueItemId'
			}, {
				name : 'pjcd.protectTypeId'
			},{
				name : 'pjcd.fixvalueItemName'
			},{
				name : 'protectTypeName'
			}]);

	var westgrids = new Ext.data.JsonStore({
				url : 'productionrec/findPtJdbhCDzxmwhList.action',
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
							header : "继电保护定值项目编号",
							width : 150,
							align : "center",
							sortable : true,
							hidden : true,
							dataIndex : 'pjcd.fixvalueItemId'
						}, {
							header : "继电保护定值项目名称",
							width : 200,
							align : "center",
							sortable : false,
							dataIndex : 'pjcd.fixvalueItemName'
						}, {
							header : "继电保护类型名称",
							width : 200,
							align : "center",
							sortable : true,
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