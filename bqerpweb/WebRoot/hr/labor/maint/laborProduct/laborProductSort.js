Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
// 劳保用品分类维护
	Ext.ns("LaborProduct.laborClass");
	LaborProduct.laborClass = function() {

		var MyClassRecord = Ext.data.Record.create([{
					name : 'laborClassId',
					mapping : 0
				}, {
					name : 'laborClassCode',
					mapping : 1
				}, {
					name : 'laborClassName',
					mapping : 2
				}]);

		var classDataProxy = new Ext.data.HttpProxy({
					url : 'com/findLaborClassList.action'
				});

		var classTheReader = new Ext.data.JsonReader({
					root : "list",
					totalProperty : "totalCount"
				}, MyClassRecord);

		var classStore = new Ext.data.Store({
					proxy : classDataProxy,
					reader : classTheReader
				});

		classStore.load()
		var classSm = new Ext.grid.CheckboxSelectionModel();

		var laborClassGrid = new Ext.grid.GridPanel({
					region : "center",
					layout : 'fit',
					store : classStore,
					columns : [classSm, new Ext.grid.RowNumberer({
										header : '行号',
										width : 35,
										align : 'left'
									}), {
								header : "ID",
								width : 75,
								sortable : true,
								dataIndex : 'laborClassId',
								hidden : true
							}, {
								header : "分类编码",
								width : 200,
								allowBlank : false,
								sortable : true,
								dataIndex : 'laborClassCode'
							}, {
								header : "分类名称",
								width : 200,
								allowBlank : false,
								sortable : true,
								dataIndex : 'laborClassName'
							}],
					sm : classSm,
					autoSizeColumns : true,
					viewConfig : {
						forceFit : true
					},
					tbar : [{
								text : "新增",
								iconCls : 'add',
								minWidth : 70,
								handler : classAddRecord
							}, '-', {
								text : "修改",
								iconCls : 'update',
								minWidth : 70,
								handler : classUpdateRecord
							}, '-', {
								text : "删除",
								iconCls : 'delete',
								minWidth : 70,
								handler : classDeleteRecord
							}],
					// 分页
					bbar : new Ext.PagingToolbar({
								pageSize : 18,
								store : classStore,
								displayInfo : true,
								displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
								emptyMsg : "没有记录"
							})
				});
		laborClassGrid.on("rowdblclick", classUpdateRecord);

		var laborClassId = new Ext.form.Hidden({
					id : "laborClassId",
					fieldLabel : 'ID',
					readOnly : true,
					value : '',
					name : 'laborClass.laborClassId'
				});

		var laborClassCode = new Ext.form.TextField({
					id : "laborClassCode",
					fieldLabel : '分类编码',
					allowBlank : false,
					width : 200,
					name : 'laborClass.laborClassCode'
				});

		var laborClassName = new Ext.form.TextField({
					id : "laborClassName",
					fieldLabel : '分类名称',
					allowBlank : false,
					width : 200,
					name : 'laborClass.laborClassName'
				});

		var classaddpanel = new Ext.FormPanel({
					frame : true,
					labelAlign : 'center',
					labelWidth : 80,
					closeAction : 'hide',
					title : '增加/修改劳保用品分类信息',
					items : [laborClassId, laborClassCode, laborClassName]
				});

		var classWin = new Ext.Window({
					width : 320,
					height : 150,
					buttonAlign : "center",
					items : [classaddpanel],
					layout : 'fit',
					closeAction : 'hide',
					resizable : false,
					modal : true,
					buttons : [{
						text : '保存',
						iconCls : 'save',
						handler : function() {
							var myurl = "";
							if (laborClassId.getValue() == "") {
								myurl = "com/saveLaborClassInfo.action";
							} else {
								myurl = "com/updateLaborClassInfo.action";
							}
							classaddpanel.getForm().submit({
								method : 'POST',
								url : myurl,
								success : function(form, action) {
									if (action.result.existFlag == true) {
										Ext.Msg.alert("提示",
												"该劳保用品分类已存在 ,请重新输入!");
									} else {
										var o = eval("("
												+ action.response.responseText
												+ ")");
										Ext.Msg.alert("注意", o.msg);
										if (o.msg.indexOf("成功") != -1) {
											classStore.load({
														params : {
															start : 0,
															limit : 18
														}
													});
											classWin.hide();
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
							classWin.hide();
						}
					}]

				});

		function classAddRecord() {
			classaddpanel.getForm().reset();
			classWin.show();
			classaddpanel.setTitle("增加劳保用品分类信息");
		}

		function classUpdateRecord() {
			if (laborClassGrid.selModel.hasSelection()) {
				var records = laborClassGrid.selModel.getSelections();
				var recordslen = records.length;
				if (recordslen > 1) {
					Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑！");
				} else {
					classWin.show();
					var record = laborClassGrid.getSelectionModel()
							.getSelected();
					classaddpanel.getForm().reset();
					classaddpanel.getForm().loadRecord(record)

					classaddpanel.setTitle("修改劳保用品分类信息");
				}
			} else {
				Ext.Msg.alert("提示", "请先选择要编辑的行!");
			}
		}

		function classDeleteRecord() {
			var records = laborClassGrid.selModel.getSelections();
			var classIds = [];
			if (records.length == 0) {
				Ext.Msg.alert("提示", "请选择要删除的记录！");
			} else {

				for (var i = 0; i < records.length; i += 1) {
					var member = records[i];
					if (member.get("laborClassId")) {
						classIds.push(member.get("laborClassId"));
					} else {
						classStore.remove(classStore.getAt(i));
					}
				}
				Ext.Msg.confirm("删除", "是否确定删除所选的记录？", function(buttonobj) {
					if (buttonobj == "yes") {
						Ext.Ajax.request({
									url : 'com/deleteLaborClassInfo.action',
									method : 'post',
									params : {
										classIds : classIds.join(',')
									},
									success : function(response, options) {
										var result = Ext.util.JSON
												.decode(response.responseText)
										if (result && result.existFlag == true) {
											Ext.Msg.alert("提示",
													"劳保用品维护正在使用该劳保用品分类，无法删除!");
										} else {
											Ext.Msg.alert("提示", "删除成功！");
											classStore.load({
														params : {
															start : 0,
															limit : 18
														}
													});
										}
									},
									failure : function(response, options) {

										Ext.Msg.alert('错误', '删除时出现未知错误.');
									}
								})
					}
				});
			}
		}
		return {
			grid : laborClassGrid
		}
	};
	});