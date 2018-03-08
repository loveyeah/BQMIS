Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	// 劳保用品维护
	Ext.ns("LaborProduct.laborMaterial");
	LaborProduct.laborMaterial = function() {
		// 劳保用品增加时取劳保用品分类
		var MyRecord = Ext.data.Record.create([{
					name : 'laborClassId',
					mapping : 0
				}, {
					name : 'laborClassCode',
					mapping : 1
				}, {
					name : 'laborClassName',
					mapping : 2
				}]);

		var laborClassStore = new Ext.data.JsonStore({
					url : 'com/findLaborClassList.action',
					root : 'list',
					fields : MyRecord
				});

		laborClassStore.load();

		var MyMaterialRecord = Ext.data.Record.create([{
					name : 'laborMaterialId',
					mapping : 0
				}, {
					name : 'laborMaterialName',
					mapping : 1
				}, {
					name : 'materialCode',
					mapping : 2
				}, {
					name : 'materialName',
					mapping : 3
				}, {
					name : 'laborClassId',
					mapping : 4
				}, {
					name : 'laborClassName',
					mapping : 5
				}, {
					name : 'unitId',
					mapping : 6
				}, {
					name : 'unitName',
					mapping : 7
				}, {
					name : 'isSend',
					mapping : 8
				}, {
					name : 'receiveKind',
					mapping : 9
				}, {
					name : 'orderBy',
					mapping : 10
				}]);

		var materialDataProxy = new Ext.data.HttpProxy({
					url : 'com/findLaborMaterialList.action'
				});

		var materialTheReader = new Ext.data.JsonReader({
					root : "list",
					totalProperty : "totalCount"
				}, MyMaterialRecord);

		var materialStore = new Ext.data.Store({
					proxy : materialDataProxy,
					reader : materialTheReader
				});
		materialStore.load()
		var materialSm = new Ext.grid.CheckboxSelectionModel();

		var laborMaterialGrid = new Ext.grid.GridPanel({
					region : "center",
					layout : 'fit',
					store : materialStore,
					columns : [materialSm, new Ext.grid.RowNumberer({
										header : '行号',
										width : 35,
										align : 'left'
									}), {
								header : "ID",
								width : 75,
								sortable : true,
								dataIndex : 'laborMaterialId',
								hidden : true
							}, {
								header : "名称",
								width : 200,
								sortable : true,
								dataIndex : 'laborMaterialName'
							}, {
								header : "对应物资编码",
								width : 200,
								sortable : true,
								dataIndex : 'materialCode'
							}, {
								header : "对应仓库物资",
								width : 200,
								sortable : true,
								dataIndex : 'materialName'
							}, {
								header : "劳保用品分类",
								width : 200,
								sortable : true,
								dataIndex : 'laborClassName'
							}, {
								header : "单位",
								width : 200,
								sortable : true,
								dataIndex : 'unitName'
							}, {
								header : "发放",
								width : 200,
								sortable : true,
								dataIndex : 'isSend',
								renderer : function(v) {
									if (v == "Y") {
										return "是";
									}
									if (v == "N") {
										return "否";
									}
								}
							}, {
								header : "领取类别",
								width : 200,
								sortable : true,
								dataIndex : 'receiveKind',
								renderer : function(v) {
									if (v == 1) {
										return "个人领用";
									}
									if (v == 2) {
										return "部门领用";
									}
									if (v == 3) {
										return "公用";
									}
								}
							}, {
								header : "顺序",
								width : 200,
								sortable : true,
								dataIndex : 'orderBy'
							}],
					sm : materialSm,
					autoSizeColumns : true,
					viewConfig : {
						forceFit : true
					},
					tbar : [{
								text : "新增",
								iconCls : 'add',
								minWidth : 70,
								handler : materialAddRecord
							}, '-', {
								text : "修改",
								iconCls : 'update',
								minWidth : 70,
								handler : materialUpdateRecord
							}, '-', {
								text : "删除",
								iconCls : 'delete',
								minWidth : 70,
								handler : materialDeleteRecord
							}],
					// 分页
					bbar : new Ext.PagingToolbar({
								pageSize : 18,
								store : materialStore,
								displayInfo : true,
								displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
								emptyMsg : "没有记录"
							})
				});
		laborMaterialGrid.on("rowdblclick", materialUpdateRecord);

		var laborMaterialId = new Ext.form.Hidden({
					id : "laborMaterialId",
					fieldLabel : 'ID',
					readOnly : true,
					name : 'laborMaterial.laborMaterialId'
				});

		var laborMaterialName = new Ext.form.TextField({
					id : "laborMaterialName",
					fieldLabel : '名称',
					allowBlank : false,
					width : 200,
					name : 'laborMaterial.laborMaterialName'
				});

		var materialCode = new Ext.form.NumberField({
					id : "materialCode",
					fieldLabel : '对应物资编码',
					allowBlank : false,
					width : 200,
					name : 'laborMaterial.materialCode'
				});

		var materialName = new Ext.form.TextField({
					id : "materialName",
					fieldLabel : '对应仓库物资',
					allowBlank : false,
					width : 200,
					name : 'laborMaterial.materialName'
				});

		var laborClassIdOrName = new Ext.form.ComboBox({
					fieldLabel : '劳保用品分类',
					id : 'laborClassId',
					name : 'laborClassName',
					allowBlank : false,
					style : "border-bottom:solid 1px",
					triggerAction : 'all',
					editable : false,
					store : laborClassStore,
					blankText : '',
					emptyText : '',
					valueField : 'laborClassId',
					displayField : 'laborClassName',
					hiddenName : 'laborMaterial.laborClassId',
					mode : 'local',
					width : 200
				});

		var isSend = new Ext.form.ComboBox({
					fieldLabel : '发放',
					store : new Ext.data.SimpleStore({
								fields : ['value', 'text'],
								data : [['Y', '是'], ['N', '否']]
							}),
					id : 'isSend',
					name : 'isSend',
					valueField : "value",
					displayField : "text",
					 value : 'Y',
					mode : 'local',
					typeAhead : true,
					forceSelection : true,
					hiddenName : 'laborMaterial.isSend',
					editable : false,
					triggerAction : 'all',
					selectOnFocus : true,
					allowBlank : false,
					width : 200
				});

		var receiveKind = new Ext.form.ComboBox({
					fieldLabel : '领取类别',
					store : new Ext.data.SimpleStore({
								fields : ['value', 'text'],
								data : [['3', '公用'],['1', '个人领用'], ['2', '部门领用']]
							}),
					id : 'receiveKind',
					name : 'receiveKind',
					valueField : "value",
					displayField : "text",
					 value : '3',
					mode : 'local',
					typeAhead : true,
					forceSelection : true,
					hiddenName : 'laborMaterial.receiveKind',
					editable : false,
					triggerAction : 'all',
					selectOnFocus : true,
					allowBlank : false,
					width : 200
				});

		var orderBy = new Ext.form.NumberField({
					id : "orderBy",
					fieldLabel : '顺序',
					allowBlank : false,
					width : 200,
					name : 'laborMaterial.orderBy'
				});

		var unit = new Power.unit({
					hiddenName : 'laborMaterial.unitId',
					width : 200,
					fieldLabel : '单位',
					allowBlank : false
				});
		unit.btnConfirm.on("click", function() {
					var aUnit = unit.getValue();
					unit.setValue(aUnit.unitId, aUnit.unitName);
				});
		var materialaddpanel = new Ext.FormPanel({
					frame : true,
					labelAlign : 'center',
					labelWidth : 80,
					closeAction : 'hide',
					title : '增加/修改劳保用品分类信息',
					items : [laborMaterialId, laborMaterialName, materialCode,
							materialName, laborClassIdOrName, unit.combo,
							isSend, receiveKind, orderBy]
				});

		var materialWin = new Ext.Window({
					width : 400,
					height : 320,
					buttonAlign : "center",
					items : [materialaddpanel],
					layout : 'fit',
					closeAction : 'hide',
					resizable : false,
					modal : true,
					buttons : [{
						text : '保存',
						iconCls : 'save',
						handler : function() {
							
							var myurl = "";
							if (laborMaterialId.getValue() == "") {
								myurl = "com/saveLaborMaterialInfo.action";
							} else {
								myurl = "com/updateLaborMaterialInfo.action";
							}
                               
							materialaddpanel.getForm().submit({
								method : 'POST',
								url : myurl,
								success : function(form, action) {
									if (action.result.existFlag == true) {
										Ext.Msg.alert("提示", "该劳保用品已存在 ,请重新输入!");
									} else {
										var o = eval("("
												+ action.response.responseText
												+ ")");
										Ext.Msg.alert("注意", o.msg);
										if (o.msg.indexOf("成功") != -1) {
											materialStore.load({
														params : {
															start : 0,
															limit : 18
														}
													});
											materialWin.hide();
										}
									}
								},
								faliue : function() {
									Ext.Msg.alert('错误', '出现未知错误.');
								}
							});
						}
					}, {
						text : '取消',
						iconCls : 'cancer',
						handler : function() {
							materialWin.hide();
						}
					}]

				});

		function materialAddRecord() {
			materialaddpanel.getForm().reset();
			materialWin.show();
			materialaddpanel.setTitle("增加劳保用品分类信息");
		}

		function materialUpdateRecord() {
			if (laborMaterialGrid.selModel.hasSelection()) {
				var records = laborMaterialGrid.selModel.getSelections();
				var recordslen = records.length;
				if (recordslen > 1) {
					Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑！");
				} else {
					materialWin.show();
					var record = laborMaterialGrid.getSelectionModel()
							.getSelected();
					materialaddpanel.getForm().reset();
					materialaddpanel.getForm().loadRecord(record)
					unit.setValue(record.data.unitId, record.data.unitName);
					// unit.setValue(record.get('unitId'),
					// record.get('unitName'));

					materialaddpanel.setTitle("修改劳保用品分类信息");
				}
			} else {
				Ext.Msg.alert("提示", "请先选择要编辑的行!");
			}
		}

		function materialDeleteRecord() {
			var records = laborMaterialGrid.selModel.getSelections();
			var materialIds = [];
			if (records.length == 0) {
				Ext.Msg.alert("提示", "请选择要删除的记录！");
			} else {

				for (var i = 0; i < records.length; i += 1) {
					var member = records[i];
					if (member.get("laborMaterialId")) {
						materialIds.push(member.get("laborMaterialId"));
					} else {
						materialStore.remove(materialStore.getAt(i));
					}
				}
				Ext.Msg.confirm("删除", "是否确定删除所选的记录？", function(buttonobj) {
							if (buttonobj == "yes") {
								Ext.lib.Ajax.request('POST',
										'com/deleteLaborMaterialInfo.action', {
											success : function(action) {
												Ext.Msg.alert("提示", "删除成功！");
												materialStore.load({
															params : {
																start : 0,
																limit : 18
															}
														});
											},
											failure : function() {
												Ext.Msg.alert('错误',
														'删除时出现未知错误.');
											}
										}, 'materialIds=' + materialIds);
							}
						});
			}
		}
		return {
			grid : laborMaterialGrid
		}
	};
	
});