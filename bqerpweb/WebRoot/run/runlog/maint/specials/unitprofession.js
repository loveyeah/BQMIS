Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var specialStore = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'runlog/getSpecialExp.action'
		}),
		reader : new Ext.data.JsonReader({
			id : "name"
		}, [{
			name : 'specialityCode'
		}, {
			name : 'specialityName'
		}])
	});
	
	var specialBox = new Ext.form.ComboBox({
		fieldLabel : '上级专业',
		store : specialStore,
		valueField : "specialityCode",
		displayField : "specialityName",
		mode : 'local',
		triggerAction : 'all',
		forceSelection : true,
		hiddenName : 'unitProfession.HSpecialityCode',
		editable : true,
		selectOnFocus : true,
		name : 'specialityCode',
		id:'specialBox',
		anchor : '80%'
	});
	specialStore.load();
	// ///////////////////tree///////////////////////////
	var Tree = Ext.tree;
	var root = new Tree.AsyncTreeNode({
		text : '根节点',
		draggable : false,
		id : '0'
	});
	var mytree = new Tree.TreePanel({
		el : 'mytree-div',
		title : '专业树',
		// checkModel : 'cascade', // 对树的级联多选
		onlyLeafCheckable : false,// 对树所有结点都可选
		autoWidth : true,
		autoScroll : true,
		root : root,
		animate : true,
		enableDD : false,
		border : true,
		rootVisible : true,
		containerScroll : true,
		loader : new Tree.TreeLoader({
			dataUrl : 'runlog/findRunSpecials.action',
			baseAttrs : {
				uiProvider : Ext.tree.TreeCheckNodeUI
			}
		})
	})

	mytree.on('beforeload', function(node) {
		mytree.loader.dataUrl = 'runlog/findRunSpecials.action?id=' + node.id;
	}, this);

	mytree.setRootNode(root);
	mytree.render();
	root.expand();

	// ///////////////////////右grid/////////////////////////////////

	var rightrecord = Ext.data.Record.create([{
		name : 'id'
	}, {
		name : 'specialityCode'
	}, {
		name : 'specialityName'
	}, {
		name : 'HSpecialityCode'
	}, {
		name : 'displayNo'
	},{
		name : 'HSpecialityName'
	}]);
	var dataProxy = new Ext.data.HttpProxy({
		url : 'runlog/findUintProfessionList.action'
	});
	var rightsm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false
	});
	var rightcm = new Ext.grid.ColumnModel([rightsm, {
		header : "ID",
		// width : 25,
		sortable : true,
		dataIndex : 'id',
		hidden : true
	}, {
		header : "编码",
		// width : 100,
		sortable : true,
		dataIndex : 'specialityCode'
	}, {
		header : "专业名称",
		// width : 150,
		sortable : true,
		dataIndex : 'specialityName'
	}, {
		header : "上级专业",
		// width : 75,
		sortable : true,
		dataIndex : 'HSpecialityName'
	}, {
		header : "显示顺序",
		// width : 75,
		sortable : true,
		dataIndex : 'displayNo'
	}]);

	var rightstore = new Ext.data.Store({
		proxy : dataProxy,
		reader : new Ext.data.JsonReader({}, rightrecord)
	});

	rightstore.load();

	var rightgrid = new Ext.grid.GridPanel({
		id : 'rightgrid',
		el : 'rightgrid-div',
		store : rightstore,
		sm : rightsm,
		cm : rightcm,
		autoSizeColumns : true,
		width : 500,
		viewConfig : {
			forceFit : true
		},
		title : '运行对应的单元与专业'
	});

	rightgrid.render();

	// ///////////////////////////3个按钮///////////////////////////////////
	var addbar = new Ext.Toolbar({
		items : [{
			id : 'addbar',
			text : ">>>>",
			handler : function() {
				var b = mytree.getChecked();
				var checkcodes = new Array;
				for (var i = 0; i < b.length; i++) {
					checkcodes.push(b[i].id);
				}
				if (checkcodes == "") {
					Ext.MessageBox.alert('错误', '请在专业树中选择需要增加的专业！');
					return false;
				} else {

					Ext.Ajax.request({
						url : 'runlog/addUnitProfession.action',
						params : {
							codes : checkcodes.join(",")
						},
						method : 'post',
						success : function(result, request) {
							rightstore.reload();
						},
						failure : function(result, request) {
							Ext.MessageBox.alert('错误', '操作失败!');
						}
					});

				}
			}
		}]
	});
	addbar.render("addbar-div");

	var updatebar = new Ext.Toolbar({
		items : [{
			id : 'updatebar',
			text : "修改",
			handler : function() {
				var rightrecord = rightgrid.getSelectionModel().getSelections();
				if (rightrecord.length != 1) {
					//Ext.Msg.alert("请选择一行进行修改！");
					Ext.MessageBox.alert('提示', '请在右边列表中选择一条需修改的记录!');
					return false;
				} else {
					rightrecord = rightgrid.getSelectionModel().getSelected();
					specialStore.load({
						params : {
							code : rightrecord.data.specialityCode
						}
					});
					rightwin.show();
					Ext.get("unitProfession.id").dom.value = rightrecord.data.id;
					Ext.get("unitProfession.specialityCode").dom.value = rightrecord.data.specialityCode;
					Ext.get("unitProfession.specialityName").dom.value = rightrecord.data.specialityName;
					
					specialBox.setValue(rightrecord.data.HSpecialityCode);
					Ext.get("unitProfession.displayNo").dom.value=rightrecord.data.displayNo;
				}
			}
		}]
	});
	updatebar.render("updatebar-div");

	var deletebar = new Ext.Toolbar({
		items : [{
			id : 'deletebar',
			text : " <<<<",
			handler : function() {
				if (rightgrid.selModel.hasSelection()) {
					var rec = rightgrid.getSelectionModel().getSelections();
					var names = "";
					for (i = 0; i < rec.length; i++) {
						names += rec[i].data.specialityName + ",";
					}
					names = names.substring(0, names.length - 1);

					if (confirm("确定要删除\"" + names + "\"专业吗？")) {
						for (i = 0; i < rec.length; i++) {
							Ext.Ajax.request({
								url : 'runlog/deleteUnitProfession.action?unitProfession.id='
										+ rec[i].get("id"),
								method : 'post',
								waitMsg : '正在删除数据...',
								success : function(result, request) {
									rightstore.load();
								},
								failure : function(result, request) {
									Ext.Msg.alert('提示', '删除失败!');
								}
							});
						}
					}
				} else {
					Ext.Msg.alert('提示', '请在右边表中选择需要删除的行（可多选）!')
				}
			}
		}]
	});
	deletebar.render("deletebar-div");

	// ///////////////////////////修改panel///////////////////////////////
	
	var rightpanel = new Ext.FormPanel({
		frame : true,
		autoWidth : true,
		autoHeight : true,
		align : 'center',
		labelAlign : 'left',
		items : [{
			xtype : 'fieldset',
			labelAlign : 'right',
			title : '修改的页面',
			defaultType : 'textfield',
			autoHeight : true,
			border : true,
			items : [{
				fieldLabel : 'ID',
				xtype : 'hidden',
				allowBlank : false,
				anchor : '80%',
				name : 'unitProfession.id'
			}, {
				fieldLabel : '编码',
				allowBlank : false,
				readOnly : true,
				anchor : '80%',
				name : 'unitProfession.specialityCode'
			}, {
				fieldLabel : '名称',
				allowBlank : false,
				anchor : '80%',
				readOnly : true,
				name : 'unitProfession.specialityName'
			}, specialBox, {
						fieldLabel : '显示顺序',
						xtype : 'numberfield',
						allowBlank : false,
						anchor : '80%',
						name : 'unitProfession.displayNo'
					}],
			buttons : [{
				text : '保存',
				iconCls : 'save',
				handler : function() {
					if (rightpanel.getForm().isValid()) {
						rightpanel.getForm().submit({
							url : 'runlog/updateUnitProfession.action',
							waitMsg : '正在更新数据...',
							method : 'post',
							success : function(form, action) {
								rightstore.load();
								Ext.Msg.alert('提示', '修改成功!')
							},
							failure : function(form, action) {
								Ext.Msg.alert('提示', '修改失败!')
							}
						});
						rightwin.hide();
					}
				}
			}, {
				text : '取消',
				iconCls : 'cancer',
				handler : function() {
					rightwin.hide();
				}
			}]
		}]
	});

	var rightwin = new Ext.Window({
		width : 400,
		height : 225,
		layout : 'fit',
		modal:true,
		closeAction : 'hide',
		items : [rightpanel]
	});

});