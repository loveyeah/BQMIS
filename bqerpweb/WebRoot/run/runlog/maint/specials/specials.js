Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	// // -----------定义tree-----------------
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
	var currentNode;
	var currentType = "add";
	var root = new Ext.tree.AsyncTreeNode({
		id : "0",
		text : "专业树"
	});

	var mytree = new Ext.tree.TreePanel({
		renderTo : "treepanel",
		height : 900,
		root : root,
		requestMethod : 'GET',
		loader : new Ext.tree.TreeLoader({
			url : "runlog/findRunSpecials.action",
			baseParams : {
				id : '0'
			}
		})
	});

	// 树的单击事件
	mytree.on("click", clickTree, this);

	function clickTree(node, e) {
		Ext.getCmp("del").show();
		e.stopEvent();
		if (node.id == "1") {
			currentType = 'add';
			webpageform.setTitle('欢迎来到专业树界面！');
			webpageform.getForm().reset();
			return false;
		}

		Ext.Ajax.request({
			url : 'runlog/findRunSpecialsByCode.action',
			method : 'post',
			params : {
				code : node.id
			},
			success : function(result, request) {
				webpageform.getForm().reset();
				var o = eval('(' + result.responseText + ')');
				//add by fyyang 20100617 增加显示顺序
				if(o.displayNo!=null)
				{
				Ext.get('spe.displayNo').dom.value =o.displayNo;
				}
				else
				{
					Ext.get('spe.displayNo').dom.value ="";
				}
				Ext.get('spe.specialityCode').dom.value = o.specialityCode;
				Ext.get('spe.specialityName').dom.value = o.specialityName;
				Ext.get('spe.PSpecialityCode').dom.value = o.PSpecialityCode;
				Ext.get('specialityId').dom.value = o.specialityId;
				if (o.specialityChar != null) {
					Ext.get("spe.specialityChar").dom.value = o.specialityChar;
				}
				specialityTypeComboBox.setValue(o.specialityType);
				Ext.get('spe.specialityCode').dom.readOnly=true;
				currentType = 'update';
				currentNode = node;
				webpageform.setTitle("修改<<" + o.specialityName + ">>基本信息");
			},
			failure : function(result, request) {
				Ext.MessageBox.alert('错误', '操作失败!');
			}
		});
	};
	mytree.on('beforeload', function(node) {
		mytree.loader.dataUrl = 'runlog/findRunSpecials.action?id=' + node.id;
	}, this);
	root.expand();// 展开根节点

	// -----------专业维护表单----------------------
	var specialityTypeComboBox = new Ext.form.ComboBox({
		fieldLabel : '类型',
		store : new Ext.data.SimpleStore({
			fields : ["retrunValue", "displayText"],
			data : [['0', '公用'], ['1', '运行'], ['2', '检修'],['3','检修管理专用']]
		}),
		valueField : "retrunValue",
		displayField : "displayText",
		mode : 'local',
		forceSelection : true,
		hiddenName : 'spe.specialityType',
		value : '0',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		name : 'spe.specialityType'
	});


	var webpageform = new Ext.FormPanel({
		frame : true,
		labelAlign : 'left',
		width : 600,
		height : 500,
		bodyStyle : 'padding:30px',
		items : [{
			xtype : 'fieldset',
			id : 'formSet',
			labelAlign : 'right',
			labelWidth : 90,
			title : '详细信息',
			defaults : {
				width : 200
			},
			defaultType : 'textfield',
			autoHeight : true,
			bodyStyle : Ext.isIE
					? 'padding:0 0 5px 15px;'
					: 'padding:10px 15px;',
			border : true,
			style : {
				"margin-left" : "10px",
				"margin-right" : Ext.isIE6
						? (Ext.isStrict ? "-10px" : "-13px")
						: "0"
			},
			items : [{
				fieldLabel : 'ID',
				xtype : 'hidden',
				name : 'specialityId',
				readOnly : true
			}, {
				fieldLabel : '编码',
				name : 'spe.specialityCode',
				allowBlank : false
			}, {
				fieldLabel : '父编码',
				name : 'spe.PSpecialityCode',
				readOnly : true
			}, {
				fieldLabel : '名称',
				allowBlank : false,
				name : 'spe.specialityName'
			},

			specialityTypeComboBox,

			{
				id : 'spe.specialityChar',
				width : 200,
				fieldLabel : '专业标识符',
				name : 'spe.specialityChar'
			},{
				fieldLabel : '显示顺序',
				xtype : 'numberfield',
				name : 'spe.displayNo'
			}],
			buttons : [{
				id : 'del',
				text : '删除',
				iconCls : 'delete',
				handler : function() {
					Ext.Msg.confirm('提示', '删除后您将不能恢复!确定要删除吗?', function(
							response) {
						if (response == 'yes') {
							webpageform.getForm().submit({
								method : 'post',
								url : 'runlog/deleteRunSpecials.action',
								waitMsg : '正在删除数据,请等待...',
								success : function(form, action) {
									var removeNode = mytree
											.getNodeById(currentNode.id);
									removeNode.parentNode
											.removeChild(removeNode);
									alert('删除成功');
									webpageform.getForm().reset();
								}
							})
						}
					});
				}
			}, {
				text : '保存',
				iconCls : 'save',
				handler : function() {
					if (webpageform.getForm().isValid()) {
						// 增加
						if (currentType == "add") {
							webpageform.getForm().submit({
								method : 'post',
								url : 'runlog/addRunSpecials.action',
								waitMsg : '正在保存数据,请等待...',
								success : function(form, action) {
									//webpageform.getForm().reset();
									var nd = mytree.getNodeById(currentNode.id);
									var path = nd.getPath();
									mytree.getRootNode().reload();
									mytree.expandPath(path, false);
									Ext.get('spe.specialityCode').dom.readOnly=true;
									alert('增加成功');
								},
								failure : function() {
									alert('增加失败');
								}
							});
						}
						// 修改
						else {
							webpageform.getForm().submit({
								method : 'post',
								url : 'runlog/updateRunSpecials.action',
								waitMsg : '正在保存数据,请等待...',
								success : function(form, action) {
									var nd = mytree.getNodeById(currentNode.id);
									mytree.getNodeById(nd.parentNode.id)
										 .reload();
//									var path = nd.getPath();
//									mytree.getRootNode().reload();
//									mytree.expandPath(path, false);
									alert('修改成功');
								},
								failure : function(form, action) {
									alert('修改失败');
								}
							})
						}
					}
				}
			}]
		}]
	});

	mytree.on('contextmenu', function(node, e) {
		e.stopEvent();
		if (node.id == "1") {
			return false;
		}
		var menu = new Ext.menu.Menu({
			id : 'mainMenu',
			items : [new Ext.menu.Item({
				text : '新增',
				iconCls : 'add',
				// style : 'display:' + (node.isLeaf() ? 'none' : ''),
				handler : function() {
					webpageform.getForm().reset();
					Ext.get('spe.specialityCode').dom.readOnly=false;
					currentNode = node;
					currentType = "add";
					Ext.get("spe.PSpecialityCode").dom.value = node.id;
					Ext.get("specialityId").dom.value = "自动生成";
					Ext.getCmp("del").hide();
				}
			}), new Ext.menu.Item({
				text : '修改',
				iconCls : 'update',
				handler : function() {
					clickTree(node, e);
				}
			})]
		});
		var coords = e.getXY();
		menu.showAt([coords[0], coords[1]]);
	}, this);

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		items : [{
			region : 'west',
			width : 300,
			layout : 'fit',
			collapsible : true,
			split : true,
			items : [mytree]
		}, {
			collapsible : true,
			region : 'center',
			layout : 'fit',
			items : [webpageform]
		}]
	});

});