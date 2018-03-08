// config combo 配置及listeners
// isQuery 是否为公共选择 true:是
// type 类别
// isChooseOne 是否选择一条 true:是
Tool = function(config, isQuery, type, isChooseOne,queryAll) {

	
	
	var label1 = new Ext.form.Label({
				text : '名称：'
			})
	var queryName = new Ext.form.TextField({
				id : 'queryName'
			})
	var label2 = new Ext.form.Label({
				text : '规格型号：'
			})
	var queryModel = new Ext.form.TextField({
				id : 'queryModel'
			})

	var queryBtu = new Ext.Button({
				id : 'queryBtu',
				text : '查询',
				iconCls : 'query',
				handler : queryFun
			})
	function queryFun() {
		
		toolStore.load({
					params : {
						start : 0,
						limit : 18
					}
				})
	}
	var addBtu = new Ext.Button({
				id : 'addBtu',
				text : '新增',
				iconCls : 'add',
				handler : addFun
			})
	function addFun() {
		editWin.setTitle('新增电动工具和电气安全用具清册')
		editWin.show();
		formPanel.getForm().reset()
	}
	var updateBtu = new Ext.Button({
				id : 'updateBtu',
				text : '修改',
				iconCls : 'update',
				handler : updateFun
			})
	function updateFun() {
		if (sm.hasSelection()) {
			if (sm.getSelections().length > 1)
				Ext.Msg.alert('提示', '请选择其中一条数据!');
			else {
				editWin.setTitle('修改电动工具和电气安全用具清册')
				editWin.show();
				// formPanel.getForm().loadRecord(sm.getSelected())
				formPanel.getForm().setValues(sm.getSelected().data)
			}
		} else
			Ext.Msg.alert('提示', '请先选择要修改的数据！')
	}
	var saveBtu = new Ext.Button({
				id : 'saveBtu',
				text : '保存',
				iconCls : 'save',
				handler : saveFun
			})
	function saveFun() {
		if (formPanel.getForm().isValid()) {
			Ext.Msg.confirm("提示", '确认要保存数据吗？', function(buttonId) {
						if (buttonId == 'yes') {
							formPanel.getForm().submit({
								url : 'security/saveToolsEntity.action',
								method : 'post',
								success : function(form, action) {
									if (action && action.response
											&& action.response.responseText) {
										var res = Ext
												.decode(action.response.responseText)
										Ext.Msg.alert('提示', res.msg)
									}
									editWin.hide();
									queryFun();
								},
								failure : function(form, action) {
									Ext.Msg.alert('提示', '保存出现异常！')
								}
							})
						}
					})
		}
	}
	var cancelBtu = new Ext.Button({
				id : "cancelBtu",
				text : '取消',
				iconCls : 'cancer',
				handler : cancelFun
			})
	function cancelFun() {
		formPanel.getForm().reset()
		editWin.hide()
	}
	var deleteBtu = new Ext.Button({
				id : 'deleteBtu',
				text : '删除',
				iconCls : 'delete',
				handler : deleteFun
			})
	function deleteFun() {
		if (sm.hasSelection()) {
			Ext.Msg.confirm('提示', '确认要删除数据吗？', function(buttonId) {
				if (buttonId == 'yes') {
					var ids = new Array();
					var selects = sm.getSelections();
					for (var i = 0; i < selects.length; i++) {
						ids.push(selects[i].get('toolId'))
					}
					if (ids.length > 0) {
						Ext.Ajax.request({
									url : 'security/deleteToolsEntity.action',
									method : 'post',
									params : {
										ids : ids.join(",")
									},
									success : function(response, options) {
										if (response && response.responseText) {
											var res = Ext
													.decode(response.responseText)
											Ext.Msg.alert('提示', res.msg);
											queryFun()
										}
									},
									failure : function(response, options) {
										Ext.Msg.alert('提示', '删除数据出现异常！')
									}
								})
					}
				}
			})

		} else
			Ext.Msg.alert('提示', '请先选择要删除的数据！')
	}
	var confirmBtu = new Ext.Button({
				id : 'confirmBtu',
				text : '确定',
				iconCls : 'confirm',
				handler : confirmFun
			})
	function confirmFun() {
		if (sm.hasSelection()) {
			if (isChooseOne != null && isChooseOne) {
				if (sm.getSelections().length > 1)
					Ext.Msg.alert('提示', '请选择其中一条数据!')
				else {
					setValue(sm.getSelected().get('toolId'), sm.getSelected()
									.get('toolName'))
					win.hide()
					return sm.getSelected();
				}
			} else {
				win.hide()
				return sm.getSelections()
			}
		} else {
			win.hide()
			return null;
		}
	}
	var cancelWinBtu = new Ext.Button({
				id : 'cancelWinBtu',
				text : '取消',
				iconCls : 'cancer',
				handler : cancelWinFun
			})
	function cancelWinFun() {
		win.hide()
	}
	var tbar = new Ext.Toolbar({
				items : [label1, queryName, label2, queryModel, queryBtu,
						addBtu, updateBtu, deleteBtu, confirmBtu, cancelWinBtu]
			})
	var ToolRecord = new Ext.data.Record.create([{
				name : 'toolId',
				mapping : 0
			}, {
				name : 'toolCode',
				mapping : 1
			}, {
				name : 'toolName',
				mapping : 2
			}, {
				name : 'toolType',
				mapping : 3
			}, {
				name : 'toolModel',
				mapping : 4
			}, {
				name : 'factoryDate',
				mapping : 5
			}, {
				name : 'memo',
				mapping : 6
			}, {
				name : 'chargeBy',
				mapping : 7
			}, {
				name : 'chargeName',
				mapping : 8
			}])

	var toolStore = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'security/findToolsByCondi.action'
						}),
				reader : new Ext.data.JsonReader({
							root : 'list',
							totalProperty : 'totalCount'
						}, ToolRecord)

			})
	toolStore.on('beforeload', function() {
				Ext.apply(this.baseParams, {
							toolName : queryName.getValue(),
							toolModel : queryModel.getValue(),
							toolType : type,
							isFiltrate : queryAll// 1
							//填写人过滤
						})
			})
	var sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : false
			})

	var cm = new Ext.grid.ColumnModel([sm, new Ext.grid.RowNumberer({
						header : '行号',
						width : 35
					}), {
				header : '编号',
				dataIndex : 'toolCode'
			}, {
				header : '名称',
				dataIndex : 'toolName'
			}, {
				header : '规格型号',
				dataIndex : 'toolModel'
			}, {
				header : '类别',
				dataIndex : 'toolType',
				renderer : function(v) {
					if (v == 1)
						return '电气安全用具';
					else if (v == 2)
						return '电动工具';
					else if (v == 3)
						return '安全带';
					else
						return '';
				}
			}, {
				header : '出厂日期',
				dataIndex : 'factoryDate'
			}, {
				header : '备注',
				dataIndex : 'memo'
			}])

	var bbar = new Ext.PagingToolbar({
				pageSize : 18,
				store : toolStore,
				displayInfo : true,
				displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
				emptyMsg : "没有记录"
			})
	var grid = new Ext.grid.GridPanel({
				id : 'grid',
				frame : 'true',
				border : false,
				layout : 'fit',
				autoScroll : true,
				sm : sm,
				store : toolStore,
				cm : cm,
				tbar : tbar,
				bbar : bbar
			})

	// 工具ID
	var toolId = new Ext.form.Hidden({
				id : 'toolId',
				name : 'tool.toolId'
			})
	// 编号
	var toolCode = new Ext.form.TextField({
				id : 'toolCode',
				name : 'tool.toolCode',
				fieldLabel : '编号',
				allowBlank : false,
				anchor : '90%'
			})
	// 名称
	var toolName = new Ext.form.TextField({
				id : 'toolName',
				name : 'tool.toolName',
				fieldLabel : '名称',
				allowBlank : false,
				anchor : '90%'
			})
	// 类别
	var typeStore = new Ext.data.SimpleStore({
				fields : ['value', 'text'],
				data : [['1', '电气安全用具'], ['2', '电动工具'], ['3', '安全带']]
			})
	var toolType = new Ext.form.ComboBox({
				id : 'toolType',
				hiddenName : 'tool.toolType',
				store : typeStore,
				displayField : 'text',
				valueField : 'value',
				readOnly : true,
				mode : 'local',
				triggerAction : 'all',
				value : 1,
				fieldLabel : '类别',
				anchor : '90%'
			})
	// 规格型号
	var toolModel = new Ext.form.TextField({
				id : 'toolModel',
				name : 'tool.toolModel',
				fieldLabel : '规格型号',
				anchor : '90%'
			})
	// 出厂日期
	var factoryDate = new Ext.form.TextField({
				id : 'factoryDate',
				name : 'tool.factoryDate',
				fieldLabel : '出厂日期',
				style : 'cursor:pointer',
				anchor : '90%',
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%m-%d',
									dateFmt : 'yyyy-MM-dd'
								})
					}
				}
			})
	// 备注
	var memo = new Ext.form.TextArea({
				id : 'memo',
				name : 'tool.memo',
				fieldLabel : '备注',
				anchor : '94%',
				height : 75
			})

	var formPanel = new Ext.form.FormPanel({
				id : 'formPanel',
				frame : true,
				border : false,
				layout : 'column',
				buttons : [saveBtu, cancelBtu],
				buttonAlign : 'center',
				labelAlign : 'right',
				labelWidth : 70,
				defaults : {
					layout : 'form',
					frame : false,
					border : false
				},
				items : [{
							columnWidth : 0.5,
							items : [toolId, toolName, toolModel, factoryDate]
						}, {
							columnWidth : 0.5,
							items : [toolCode, toolType]
						}, {
							columnWidth : 1,
							items : [memo]
						}]
			})

	var editWin = new Ext.Window({
				id : 'editWin',
				width : 500,
				height : 260,
				items : [formPanel],
				modal : true,
				closeAction : 'hide'
			})

	var win = new Ext.Window({
				id : 'win',
				frame : true,
				border : false,
				modal : true,
				closeAction : 'hide',
				layout : 'fit',
				items : [grid],
				width : 700,
				height : 400
			})
	var cbStore = new Ext.data.Store({
				reader : new Ext.data.JsonReader({}, ToolRecord)
			})
	var combo = new Ext.form.ComboBox({
				fieldLabel : '工具/用具/清册',
				store : cbStore,
				mode : 'local',
				hiddenName : 'toolId',
				width : 180,
				valueField : 'toolId',
				displayField : 'toolName',
				triggerAction : 'all',
				forceSelection : true,
				readOnly : true,
				listeners : (config && config.listeners)
						? config.listeners
						: {},
				onTriggerClick : function() {
					win.show();
				}
			});
	Ext.apply(combo, config);

	function setValue(toolId, toolName) {
		var d = new ToolRecord({
					toolId : toolId,
					toolName : toolName
				})
		cbStore.removeAll();
		cbStore.add(d);
		combo.setValue(toolId)
	}

	this.grid = grid;
	this.init = function() {
		if (isQuery != null && isQuery) {
			addBtu.setVisible(false)
			updateBtu.setVisible(false)
			deleteBtu.setVisible(false)
			grid.on('rowdblclick', function() {
						Ext.get("confirmBtu").dom.click();
						confirmFun()
					})
		} else {
			confirmBtu.setVisible(false)
			cancelWinBtu.setVisible(false)
			grid.on('rowdblclick', updateFun)
		}
		//queryFun()
	};
	this.setValue = setValue;
	this.confirmBtu = confirmBtu;
	this.confirmFun = confirmFun;
	this.combo = combo;
	this.queryFun=queryFun;
}