Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var operateMethod = "add";
	function choseEdit(userManlistGrid, rowIndex, columnIndex, e) {
		str = "";
		for (var i = 0; i < atnStore.getCount(); i++) {
			if (atnStore.getAt(i).get("model.workerCode") != ""
					&& atnStore.getAt(i).get("model.workerCode") != null)
				str += "'" + atnStore.getAt(i).get("model.workerCode") + "',";
		}
		str = str.substring(0, str.length - 1);
		var args = {
			selectModel : 'signal',
			notIn : str
		}
		var person = window
				.showModalDialog(
						'../../comm/jsp/hr/workerByDept/workerByDept2.jsp',
						args,
						'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (typeof(person) != "undefined") {
			var record = userManlistGrid.getSelectionModel().getSelected();
			record.set("model.workerCode", person.workerCode);
			record.set("workerName", person.workerName);
			// record.set("atnInfo.depCode", person.deptCode);
		}

	};

	var txtCode = new Ext.form.TextField({
				id : "code",
				allowBlank : false,
				width : 300,
				name : 'code',
				xtype : "textfield",
				fieldLabel : '模板编号'
			});
	var txtXlsFile = new Ext.form.TextField({
				id : "xlsFile",
				name : 'xlsFile',
				inputType : "file",
				fieldLabel : '模板文件',
				// allowBlank : false,
				width : 300
			});
	var txtMemo = new Ext.form.TextField({
				id : "memo",
				name : 'memo',
				xtype : "textarea",
				width : 300,
				fieldLabel : '备注'
			});
	var ryRadio = new Ext.form.Radio({
				id : 'ry',
				boxLabel : '是',
				name : 'rs',
				inputValue : 'Y',
				checked : true
			});

	var rnRadio = new Ext.form.Radio({
				id : 'rn',
				boxLabel : '否',
				name : 'rs',
				inputValue : 'N'
			});
	var isUse = {
		layout : 'column',
		isFormField : true,
		fieldLabel : '是否使用',
		style : 'cursor:hand',
		anchor : '80%',
		border : false,
		items : [{
					columnWidth : .4,
					border : false,
					items : [ryRadio]
				}, {
					columnWidth : .4,
					border : false,
					items : [rnRadio]
				}]
	};
	var timeUnitStore = new Ext.data.SimpleStore({
				root : 'list',
				data : [['R', '日报表'], ['Y', '月报表'], ['J', '季报表'], ['N', '年报表']],
				fields : ['name', 'value']
			})
	var timeUnitBox = new Ext.form.ComboBox({
				fieldLabel : '时间类型',
				store : timeUnitStore,
				id : 'timeUnitBox',
				name : 'timeUnitBox',
				valueField : "name",
				displayField : "value",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				// hiddenName : 'statItem.dataTimeType',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				width : 300,
				allowBlank : false,
				emptyText : '请选择',
				anchor : '85%'
			});
	var lblAlert = new Ext.form.Label({
		hidden : true,
		html : '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color="red">↑↑不选择文件时，不会修改已上传的模板文件!</font>'
	});
	var form = new Ext.FormPanel({
				frame : true,
				fileUpload : true,
				labelAlign : 'center',
				items : [txtCode, txtXlsFile, lblAlert, txtMemo, timeUnitBox,
						isUse],
				buttons : [{
					text : '保存',
					iconCls : 'save',
					handler : function() {

						if (form.getForm().isValid() ) {
							if (txtXlsFile.getValue() == "" && operateMethod != "update") {
								Ext.Msg.alert("提示", "请上传模板!");
								return;
							}
							Ext.Msg.wait("正在保存数据...");
							var isU = Ext.get("ry").dom.checked ? "Y" : "N";
							form.getForm().submit({
								url : 'comm/addExcelTemplate.action',
								waitMsg : '正在保存数据...',
								method : 'post',
								params : {
									isUse : isU,
									dateType : timeUnitBox.getValue(),
									operateMethod : operateMethod
								},
								success : function(form, action) {
									win.hide();
									ds.reload();
								},
								failure : function(form, action) {
									var o = eval("("
											+ action.response.responseText
											+ ")");
									Ext.Msg.alert("提示", o.msg);
									// alert(action.response.responseText);
								}
							});
						}
					}
				}]
			});
	var win = new Ext.Window({
				width : 500,
				title : 'Excel报表模板维护',
				height : 300,
				plain : true,
				layout : 'fit',
				plain : true,
				closeAction : 'hide',
				modal : true,
				items : [form]
			});

	var Template = new Ext.data.Record.create([{
				name : 'code'
			}, {
				name : 'memo'
			}, {
				name : 'isUse'
			}, {
				name : 'dateType'
			}]);
	var ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'comm/getExcelTemplateList.action',
							method : 'post'
						}),
				reader : new Ext.data.JsonReader({}, Template)
			});
	ds.on("load", function() {
				Ext.Msg.hide();
			});
	ds.load();
	var cb = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true,
				listeners : {
					rowselect : function(sm, row, rec) {
					}
				}
			});
	var colModel = new Ext.grid.ColumnModel([cb, {
		header : '模板编号',
		dataIndex : 'code',
		sortable : true,
		renderer : function(v) {
			return "<a href='comm/viewTemplate.action?code=" + v + "'>♣" + v
					+ "</a>";
		}
	}, {
		header : '说明',
		dataIndex : 'memo',
		width : 300
	}, {
		header : '时间类型',
		dataIndex : 'dateType',
		width : 100,
		renderer : function(v) {
			if (v == 'R')
				return "日报表";
			if (v == 'Y')
				return "月报表";
			if (v == 'J')
				return "季报表";
			if (v == 'N')
				return "年报表";
		}
	}, {
		header : '是否使用',
		dataIndex : 'isUse',
		width : 100,
		renderer : function(v) {
			if (v == 'Y')
				return "是";
			if (v == 'N')
				return "否";
		}
	}]);
	var tbar = new Ext.Toolbar({
		items : [{
					id : 'btnAdd',
					text : "增加",
					iconCls : 'add',
					handler : function() {
						operateMethod = "add";
						win.show();
						Ext.getCmp("code").getEl().dom.readOnly = false;
						form.getForm().reset();
						lblAlert.hide();
					}
				}, '-', {
					id : 'btnUpdate',
					text : '修改',
					iconCls : 'update',
					handler : function() {
						var rec = grid.getSelectionModel().getSelected();
						if (rec != null) {
							operateMethod = "update";
							win.show();
							lblAlert.show();
							Ext.getCmp("code").getEl().dom.readOnly = true;
							txtCode.setValue(rec.get("code"));
							txtMemo.setValue(rec.get("memo"));
							timeUnitBox.setValue(rec.get("dateType"));
							if (rec.get("isUse") == 'Y')
								ryRadio.setValue(true);
							else
								rnRadio.setValue(true);
						} else {
							Ext.Msg.alert('提示', '请选择一条记录，或者双击弹出修改页面！');
						}
					}
				}, '-', {
					id : 'btnDelete',
					text : "删除",
					iconCls : 'delete',
					handler : function() {
						var selected = grid.getSelectionModel().getSelections();
						if (selected.length == 0) {
							Ext.Msg.alert("提示", "请选择要删除的模板！");
						} else if (confirm("确定要删除模板吗？")) {
							Ext.Msg.wait("正在删除数据...");
							var record = grid.getSelectionModel().getSelected();
							Ext.Ajax.request({
								url : 'comm/removeExcelTemplate.action',
								method : 'post',
								params : {
									code : record.get("code")
								},
								callback : function(options, success, response) {
									ds.reload();
								}
							});
						}
					}
				},
				// '-',{
				// text:'报表查询',
				// iconCls:'excelview',
				// handler : function() {
				// var record = grid.getSelectionModel().getSelected();
				// if(record != null)
				// {
				// var args = new Object();
				// args.code = record.get("code");
				// window.showModalDialog("../test.jsp",args,"dialogWidth=500px;dialogHeight=400px");
				// }
				// }
				// },
				'-', {
					text : '权限设置',
					iconCls : 'view',
					handler : function() {
						var record = grid.getSelectionModel().getSelected();
						if (record != null) {
							atnStore.removeAll();
							atnManlistWindow.show();
							atnStore.load({
										params : {
											code : record.get("code")
										}
									});
						} else {
							Ext.MessageBox.alert('提示', '请先选择一条记录！');
						}
					}
				}]
	});
	// 参加人员
	var atnManName = {
		header : "人员",
		sortable : false,
		dataIndex : 'workerName',
		width : 50,
		editor : new Ext.form.TextField({
					readOnly : true
				})
	};

	var code = new Ext.form.TextField({
				dataIndex : 'model.code',
				hidden : true
			});

	var id = new Ext.form.TextField({
				dataIndex : 'model.id',
				hidden : true
			});

	var workerCode = {
		header : "工号",
		width : 100,
		sortable : false,
		dataIndex : 'model.workerCode',
		width : 135
	};

	// var atnManDep = new Ext.form.TextField({
	// dataIndex : 'atnInfo.depCode',
	// hidden : true
	// });

	function addAtnRecords() {
		var count = atnStore.getCount();
		var currentIndex = count;
		var o = new atnStorelist({
					'model.id' : '',
					'model.code' : grid.getSelectionModel().getSelected()
							.get("code"),
					'model.workerCode' : '',
					'workerName' : ''
				});

		userManlistGrid.stopEditing();
		atnStore.insert(currentIndex, o);
		atnSm.selectRow(currentIndex);
		userManlistGrid.startEditing(currentIndex, 2);

	};
	var atnBtnAdd = new Ext.Button({
				text : '增加',
				iconCls : 'add',
				handler : function() {
					addAtnRecords();
				}
			});

	var ids = new Array();
	var atnBtnDel = new Ext.Button({
				text : '删除',
				iconCls : 'delete',
				handler : function() {
					userManlistGrid.stopEditing();
					var atnSm = userManlistGrid.getSelectionModel();
					var selected = atnSm.getSelections();
					if (selected.length == 0) {
						Ext.Msg.alert("提示", "请选择要删除的记录！");
					} else {
						for (var i = 0; i < selected.length; i += 1) {
							var member = selected[i];
							if (member.get("model.id") != null) {
								ids.push(member.get("model.id"));
							}
							userManlistGrid.getStore().remove(member);
							userManlistGrid.getStore().getModifiedRecords()
									.remove(member);
						}
					}
				}
			});
	var atnBtnSav = new Ext.Button({
		text : '保存',
		iconCls : 'save',
		handler : function() {
			userManlistGrid.stopEditing();
			var modifyRec = userManlistGrid.getStore().getModifiedRecords();
			if (modifyRec.length > 0 || ids.length > 0) {
				Ext.Msg.confirm('提示信息', '是否确定修改？', function(button, text) {
					if (button == 'yes') {
						var updateData = new Array();
						for (var i = 0; i < modifyRec.length; i++) {
							updateData.push(modifyRec[i].data);
						}
						Ext.Ajax.request({
									url : 'comm/saveRoleUsers.action',
									method : 'post',
									params : {
										isUpdate : Ext.util.JSON
												.encode(updateData),
										isDelete : ids.join(","),
										id : grid.getSelectionModel()
												.getSelected().get("code")
									},
									success : function(result, request) {
										var o = eval('(' + result.responseText
												+ ')');
										Ext.MessageBox.alert('提示信息', o.msg);
										atnStore.rejectChanges();
										ids = [];
										atnStore.load({
													params : {
														code : grid
																.getSelectionModel()
																.getSelected()
																.get("code")
													}
												});
									},
									failure : function(result, request) {
										Ext.MessageBox.alert('提示信息', '未知错误！')
									}
								})
					}
				});
			} else {
				Ext.MessageBox.alert('提示信息', '没有做任何修改！')
			}
		}
	});
	var atnSm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			});

	var atnStorelist = new Ext.data.Record.create([atnSm, {
				name : 'model.id'
			}, {
				name : 'model.code'
			}, {
				name : 'model.workerCode'
			}, {
				name : 'workerName'
			}]);

	var atnStore = new Ext.data.JsonStore({
				url : 'comm/getUsersList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : atnStorelist
			})
	// add by bjxu 091103
	atnStore.on('beforeload', function() {
				Ext.apply(this.baseParams, {
							start : 0,
							limit : 18
						});
			});
	// 参加人员Grid
	var userManlistGrid = new Ext.grid.EditorGridPanel({
		store : atnStore,
		columns : [new Ext.grid.RowNumberer(), atnManName, code, id, workerCode],
		viewConfig : {
			forceFit : true
		},
		tbar : [atnBtnAdd, atnBtnDel, atnBtnSav],
		sm : atnSm,
		frame : true,
		enableColumnHide : false,
		enableColumnMove : false,
		iconCls : 'icon-grid',
		bbar : new Ext.PagingToolbar({
					pageSize : 18,
					store : atnStore,
					displayInfo : true,
					displayMsg : '共 {2} 条',
					emptyMsg : "没有记录"
				})
	});

	userManlistGrid.on('celldblclick', choseEdit);

	var atnManlistWindow = new Ext.Window({
				title : '人员名单',
				layout : 'fit',
				width : 400,
				height : 450,
				closable : true,
				border : false,
				resizable : false,
				closeAction : 'hide',
				modal : true,
				plain : true,
				buttonAlign : 'center',
				items : [userManlistGrid]
			});
	var grid = new Ext.grid.GridPanel({
				tbar : tbar,
				id : 'template-grid',
				ds : ds,
				layout : 'fit',
				autoHeight : true,
				cm : colModel,
				border : false,
				autoWidth : true,
				fitToFrame : true
			});
	grid.on("rowdblclick", function() {
				Ext.get("btnUpdate").dom.click();
			});
	var view = new Ext.Viewport({
				layout : 'fit',
				frame : true,
				items : [grid]
			});
		// grid.render(Ext.getBody());
});