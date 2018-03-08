Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	// // -----------定义tree-----------------
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
	var currentNode;
	var currentType = "add";
	var root = new Ext.tree.AsyncTreeNode({
				id : "0",
				text : "证书类别"
			});

	var mytree = new Ext.tree.TreePanel({
				renderTo : "treepanel",
				height : 900,
				root : root,
				requestMethod : 'GET',
				loader : new Ext.tree.TreeLoader({
							url : "com/listCertificate.action",
							baseParams : {
								pid : '0'
							}
						})
			});

	// 树的单击事件
	mytree.on("click", clickTree, this);

	function clickTree(node, e) {
		Ext.getCmp("del").show();
		e.stopEvent();
		if (node.id == "0") {
			currentType = 'add';
			webpageform.getForm().reset();
			return false;
		}
		currentType = "";
		certificateId.setValue(node.id);
		certificateName.setValue(node.text);
		certificateMemo.setValue(node.attributes.description)
	};
	mytree.on('beforeload', function(node) {
				mytree.loader.dataUrl = 'com/listCertificate.action?pid='
						+ node.id;
			}, this);
	root.expand();// 展开根节点

	// -----------专业维护表单----------------------
	var certificateId = new Ext.form.Hidden({
				name : 'id',
				id : 'certificateId'
			})
	var certificatePid = new Ext.form.Hidden({
				name : 'pid',
				id : 'certificatePid'
			})
	var certificateName = new Ext.form.TextField({
				fieldLabel : '证书名称',
				name : 'name'
			});

	var certificateMemo = new Ext.form.TextArea({
				fieldLabel : "证书描述",
				name : "memo",
				height : 100
			})
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
			items : [certificateId, certificatePid, certificateName,
					certificateMemo],
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
										url : 'com/delCertificate.action',
										waitMsg : '正在删除数据,请等待...',
										success : function(form, action) {
											mytree.getRootNode().reload();
											alert('删除成功');
											webpageform.getForm().reset();
										},
										failure : function() {
											alert('该节点有子节点，不能删除');
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
								url : 'com/saveCertificate.action',
								waitMsg : '正在保存数据,请等待...',
								success : function(form, action) {
									// webpageform.getForm().reset();
//									var nd = mytree.getNodeById(currentNode.id);
//									var path = nd.getPath();
									mytree.getRootNode().reload();
//									mytree.expandPath(path, false);
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
										url : 'com/updateCertificate.action',
										waitMsg : '正在保存数据,请等待...',
										success : function(form, action) {
											alert('修改成功');
											mytree.getRootNode().reload();
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
				var menu = new Ext.menu.Menu({
							id : 'mainMenu',
							items : [new Ext.menu.Item({
												text : '添加',
												iconCls : 'add',
												handler : function() {
													webpageform.getForm()
															.reset();
													currentNode = node;
													currentType = "add";
													certificatePid
															.setValue(node.id);
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