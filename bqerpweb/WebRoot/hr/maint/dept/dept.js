Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var flagBanzu = '0';
	
	var method = "update";
	var Tree = Ext.tree;
	var tree = new Tree.TreePanel({
				el : 'dept-tree-div',
				rootVisible : false,
				autoHeight : true,
				root : root,
				animate : true,
				enableDD : true,
				border : false,
				containerScroll : true,
				loader : new Tree.TreeLoader({
							dataUrl : 'com/findDeptList.action'
						})
			});
	var root = new Tree.AsyncTreeNode({
				text : '灞桥电厂',
				draggable : false,
				id : '-1'
			});
	tree.setRootNode(root);
	tree.render();
	root.expand();
	root.select();
	currentNode = root;
	tree.on('click', treeClick);
	function treeClick(node, e) {
		e.stopEvent();
		if (node.id == "0") {
			Ext.get("pDeptName").dom.value = "";
		} else {
			Ext.get("pDeptName").dom.value = node.parentNode.text;
		}

		Ext.Ajax.request({
					url : 'com/findDeptById.action',
					method : 'post',
					params : {
						node : node.id
					},
					success : function(result, request) {

						Ext.get("modifyBy").dom.value = "";
						Ext.get("modifyDate").dom.value = "";
						var o = eval('(' + result.responseText + ')');

						deptForm.getForm().loadRecord(o);
						deptForm.load(o);
						// add by liuyi 090929
						if(o.data.isBanzu == '1')
						{
							nBanzu.setValue(false)
							yBanzu.setValue(true);
						}
							
						else	
						{
							yBanzu.setValue(false)
							nBanzu.setValue(true)
						}
						// add
						if (o.data.createBy != null) {
							Ext.get("createBy").dom.value = o.createMan;
						}
						if (o.data.createDate != null) {
							Ext.get("createDate").dom.value = o.data.createDate
									.substr(0, 10);
						}
						if (o.data.modifiyBy != null) {
							Ext.get("modifyBy").dom.value = o.modifyMan;
						}
						if (o.data.modifiyDate != null) {
							Ext.get("modifyDate").dom.value = o.data.modifiyDate
									.substr(0, 10);
						}
						
						if (Ext.get("isUse").dom.value == 'Y') {
							Ext.getCmp('y').setValue(true);
						} else {
							Ext.getCmp('n').setValue(true)
						}
						// currentType = 'update';
						currentNode = node;
						// deptForm.setTitle("修改" + o.data.deptName + "基本信息");

						deptForm.setTitle(o.data.deptName + "基本信息"); // modify
						method = ""; // add
					},
					failure : function(result, request) {
						Ext.MessageBox.alert('错误', '操作失败!');
					}
				});
		Ext.get("deptCode").dom.readOnly = true;
		Ext.get("deptName").dom.readOnly = true;
		Ext.get("manager").dom.readOnly = true;
		Ext.get("Use").dom.readOnly = true;
		Ext.get("memo").dom.readOnly = true;
	};
	// 添加按钮
	var btnAdd = new Ext.Button({
				text : '新增部门',
				iconCls : 'add',
				handler : function() {
					var node = tree.getSelectionModel().getSelectedNode();
					method = "add";
					if (node == null) {
						Ext.Msg.alert('提示信息', '请选择一个上级部门！');
						return;
					};
					Ext.get("deptCode").dom.readOnly = false;
					Ext.get("deptName").dom.readOnly = false;
					Ext.get("manager").dom.readOnly = false;
					Ext.get("memo").dom.readOnly = false;
					var t = node.text;
					deptForm.getForm().reset();
					Ext.get("pDeptName").dom.value = node.text;
					deptForm.setTitle("在" + node.text + "下增加部门");
				}
			});
	var tdata = [[1, '管理部门'], [2, '生产部门'], [3, '行政部门'], [4, '外来部门'],
			[5, '检修部门']];
	// 部门性质
	var feadata = [['',''],[1,'主业'],[2,'多经'],[3,'全民支援集体'],[4,'代管单位']]
	// 部门状态
	var statusdata = [['0','新'],['1','老'],['3','新老共用']]
	// 部门级别
	var leveldata = [['',''],[1,'一级部门'],[2,'二级部门'],[3,'三级部门'],[4,'四级部门'],[5,'五级部门']]
	// 删除按钮
	var btnDlt = new Ext.Button({
		id : 'delete',
		text : '删除部门',
		iconCls : 'delete',
		handler : function() {
			var node = tree.getSelectionModel().getSelectedNode();
			if (node == null) {
				Ext.Msg.alert('提示信息', '请选择一个部门！');
				return;
			};

			var msg = '确认删除该部门及其所有子部门?';
			if (node.isLeaf())
				msg = '确认删除该部门?';
			Ext.Msg.confirm('提示信息', msg, function(button, text) {
						if (button == 'yes') {
							Ext.Ajax.request({
										url : 'deleteDept.action?id=' + node.id,
										success : function(response, options) {
											Ext.Msg.alert('提示信息', '删除成功!'); // add
											var rnode = node.parentNode;
											rnode.removeChild(node);
											deptForm.getForm().reset();
											root.select(); // add
											initadd(); // add

										},
										failure : function() {
											Ext.Msg.alert('提示信息',
													'服务器错误,请稍候重试!')
										}
									});
						}
					})
		}
	});

	// 更新按钮
	var btnUdt = new Ext.Button({
		id : 'update',
		text : '修改部门',
		iconCls : 'update',
		handler : function() {
			var node = tree.getSelectionModel().getSelectedNode();
			if (node.text == "灞桥电厂") {
				Ext.Msg.alert('提示信息', '请选择一个部门！');
				return;
			}
			method = "update";

			if (node == null) {
				Ext.Msg.alert('提示信息', '请选择一个部门！');
				return;
			};
			// ---------modify--------
			// deptForm.getForm().load({
			// url : 'getSelectedNode.action?id=' + node.id
			// });

			Ext.Ajax.request({
						url : 'com/findDeptById.action',
						method : 'post',
						params : {
							node : node.id
						},
						success : function(result, request) {

							Ext.get("modifyBy").dom.value = "";
							Ext.get("modifyDate").dom.value = "";
							var o = eval('(' + result.responseText + ')');
							deptForm.getForm().loadRecord(o);
							deptForm.load(o);

							if (o.data.createBy != null) {
								Ext.get("createBy").dom.value = o.createMan;
							}
							if (o.data.createDate != null) {
								Ext.get("createDate").dom.value = o.data.createDate
										.substr(0, 10);
							}
							if (o.data.modifiyBy != null) {
								Ext.get("modifyBy").dom.value = o.modifyMan;
							}
							if (o.data.modifiyDate != null) {
								Ext.get("modifyDate").dom.value = o.data.modifiyDate
										.substr(0, 10);
							}
							if (Ext.get("isUse").dom.value == 'U') {
								Ext.getCmp('y').setValue(true);
							} else {
								Ext.getCmp('n').setValue(true)
							}

							deptForm.setTitle("修改" + o.data.deptName + "基本信息");
						},
						failure : function(result, request) {
							Ext.MessageBox.alert('错误', '操作失败!');
						}
					});

			// -----------------------------

			Ext.get("deptName").dom.readOnly = false;
			Ext.get("manager").dom.readOnly = false;
			Ext.get("Use").dom.readOnly = false;
			Ext.get("memo").dom.readOnly = false;
		}
	});
	var utype = new Ext.form.ComboBox({
				hiddenName : 'deptTypeId',
				fieldLabel : '部门类别',
				anchor : '80%',
				readOnly : true,
				editable : false,
				allowBlank : false,
				emptyText : '请选择',
				triggerAction : 'all',
				store : new Ext.data.SimpleStore({
							fields : ['value', 'text'],
							data : tdata
						}),
				displayField : 'text',
				valueField : 'value',
				mode : 'local'
			});

	// 部门性质
	var depFeature = new Ext.form.ComboBox({
		hiddenName : 'depFeature',
		fieldLabel : '部门性质',
		anchor : '80%',
		readOnly : true,
		allowBlank : true,
		triggerAction : 'all',
		store : new Ext.data.SimpleStore({
			fields : ['value','text'],
			data : feadata
		}),
		displayField : 'text',
		valueField : 'value',
		mode : 'local'
	})
	// add by liuyi 091219 
	// 部门状态
	var deptStatus = new Ext.form.ComboBox({
		hiddenName : 'deptStatus',
		fieldLabel : "部门状态<font color='red'>*</font>",
		anchor : '80%',
		readOnly : true,
		allowBlank : false,
		triggerAction : 'all',
		store : new Ext.data.SimpleStore({
			fields : ['value','text'],
			data : statusdata
		}),
		displayField : 'text',
		valueField : 'value',
		mode : 'local',
		value : 3
	})
	// 部门级别
	var deptLevel = new Ext.form.ComboBox({
		hiddenName : 'deptLevel',
		fieldLabel : '部门级别',
		anchor : '80%',
		readOnly : true,
		allowBlank : true,
		triggerAction : 'all',
		store : new Ext.data.SimpleStore({
			fields : ['value','text'],
			data : leveldata
		}),
		displayField : 'text',
		valueField : 'value',
		mode : 'local'
	})
	//add by wpzhu 100506
	var peopleNum = new Ext.form.NumberField({
		id : "peopleNumber",
		fieldLabel : '定员人数',
		readOnly:false,
		allowBlank :true,
		allowDecimals :false,
		allowNegative :false,
//		name : 'peopleNum',
		anchor : "85%"
	})
	// 是否班组
	var yBanzu = new Ext.form.Radio({
		id : 'yBanzu',
		boxLabel : '是',
		name : 'banzu'
	})
	var nBanzu = new Ext.form.Radio({
		id : 'nBanzu',
		boxLabel : '否',
		name : 'banzu',
		checked : true
	})
	yBanzu.on('check',function(){
		if(yBanzu.getValue() == true)
			flagBanzu = '1';
		else 
			flagBanzu = '0';
	})
	var fs = new Ext.form.FieldSet({
		title : '部门信息',
		height : '100%',
		layout : 'form',
		items : [{
			border : false,
			layout : 'column',
			items : [{
						columnWidth : .5,
						layout : 'form',
						border : false,
						labelWidth : 100,
						items : [{
									id : 'pDeptName',
									xtype : 'textfield',
									fieldLabel : '上级部门',
									readOnly : true,
									anchor : '85%'
								}, {
									id : 'deptCode',
									xtype : 'textfield',
									fieldLabel : "部门编码<font color='red'>*</font>",
									allowBlank : false,
									readOnly : true,
									anchor : '85%'
								}, {
									id : 'deptName',
									xtype : 'textfield',
									fieldLabel : "部门名称<font color='red'>*</font>",
									allowBlank : false,
									readOnly : true,
									anchor : '85%'
								}, utype, {
									id : 'manager',
									xtype : 'numberfield',
									fieldLabel : '管理者',
									readOnly : true,
									anchor : '85%'
								},depFeature,deptStatus,peopleNum]
					}, {
						columnWidth : .5,
						layout : 'form',
						labelWidth : 100,
						border : false,
						items : [{
									id : 'createBy',
									xtype : 'textfield',
									fieldLabel : '创建者',
									readOnly : true,
									anchor : '85%'
								}, {
									id : 'createDate',
									xtype : 'textfield',
									fieldLabel : '创建时间',
									readOnly : true,
									anchor : '85%'
								}, {
									id : 'modifyBy',
									xtype : 'textfield',
									fieldLabel : '修改者',
									readOnly : true,
									anchor : '85%'
								}, {
									id : 'modifyDate',
									xtype : 'textfield',
									fieldLabel : '修改时间',
									readOnly : true,
									anchor : '85%'
								}, {
									id : 'isUse',
									xtype : 'hidden'
								}, {
									id : 'Use',
									layout : 'column',
									readOnly : true,
									isFormField : true,
									fieldLabel : '状态',
									border : false,
									anchor : '85%',
									items : [{
												columnWidth : .4,
												border : false,
												items : new Ext.form.Radio({
															id : 'y',
															boxLabel : '启用',
															name : 'u',
															inputValue : '启用',
															checked : true
														})
											}, {
												columnWidth : .4,
												border : false,
												items : new Ext.form.Radio({
															id : 'n',
															boxLabel : '停用',
															name : 'u',
															inputValue : '停用'
														})
											}]
								}, {
									id : 'isBanzu',
									layout : 'column',
									readOnly : true,
									isFormField : true,
									fieldLabel : '是否班组',
									border : false,
									anchor : '85%',
									items : [{
												columnWidth : .4,
												border : false,
												items : yBanzu
											}, {
												columnWidth : .4,
												border : false,
												items : nBanzu
											}]
								},deptLevel]
					}]
		}, {
			border : false,
			layout : 'form',
			columnWidth : 1,
			labelWidth : 100,
			items : [{
						id : 'memo',
						xtype : 'textarea',
						readOnly : true,
						fieldLabel : '备注',
						height : 120,
						anchor : '92.5%'
					}]
		}],
		buttons : [{
			text : '保存',
			iconCls : 'save',
			handler : function() {
				var node = tree.getSelectionModel().getSelectedNode();

				if (method == "add") {
					if (!deptForm.getForm().isValid()) {
						return false;
					}

					deptForm.getForm().doAction('submit', {
						url : 'addDept.action?pid=' + node.id,
						params : {
							flagBanzu : flagBanzu
						},
						// 提交成功的回调函数
						success : function(f, action) {
							var id = action.result.id;
							var text = action.result.text;
							// var url = action.result.url;

							var newNode = new Ext.tree.AsyncTreeNode({
								id : id,
								leaf : true,
								text : text
									// url : url
								});
							if (!node.isLeaf()) {

								var fc = node.firstChild;
								if (fc == null)
									node.appendChild(newNode);
								else
									node.insertBefore(newNode, fc);

							} else {

								var rid = node.id;
								var rtext = node.text;
								// var rurl = node.attributes['url'];
								var rnode = new Ext.tree.AsyncTreeNode({
									id : rid,
									text : rtext,
									leaf : false
										// url : rurl
									});

								var rsibling = node.nextSibling;
								var pnode = node.parentNode;
								node.remove();

								if (rsibling != null)
									pnode.insertBefore(rnode, rsibling);
								else
									pnode.appendChild(rnode);
								rnode.appendChild(newNode);
								rnode.expand();

							}

							// newNode.select();

							// deptForm.getForm().load({
							// url : url
							// });

							// add
							deptForm.setTitle(text + "基本信息");
							method = "";
							Ext.Msg.alert('提示信息', "增加成功");
						},
						failure : function(form, action) {
							var o = eval("(" + action.response.responseText
									+ ")");
							Ext.Msg.alert('错误', o.errMsg);
						}
					});
				}
				// else
				else if (method == "update") {
					deptForm.getForm().doAction('submit', {
						url : 'updateDept.action?id=' + node.id,
						params : {
							flagBanzu : flagBanzu
						},
						// 提交成功的回调函数
						success : function() {
							node.setText(Ext.get("deptName").dom.value);
							deptForm.getForm().load({
										url : node.attributes['url'],
										success : function() {
											var str = Ext.getCmp('isUse')
													.getValue();
											if (str == 'U') {
												Ext.getCmp('y').setValue(true);
											} else {
												Ext.getCmp('n').setValue(true);
											}
										}
									});
							Ext.Msg.alert('提示信息', "修改成功");
						},
						// 提交失败的回调函数
						failure : function() {
							Ext.Msg.alert('提示信息', "操作失败");
						}
					});
				} else {

				}
				flagBanzu = '0';
			}
		}, {
			text : '取消',
			iconCls : 'canse',
			handler : function() {
			}
		}]
	});
	var deptForm = new Ext.FormPanel({
				id : 'webpage-form',
				title : '在目录<<灞桥电厂>>下增加',
				frame : true,
				labelAlign : 'left',
				width : 600,
				height : 500,
				bodyStyle : 'padding:30px',
				items : [fs]

			})
	var tbar = new Ext.Toolbar({
				items : [btnAdd, btnUdt, btnDlt]
			});
	// 设定布局器及面板
	var viewport = new Ext.Viewport({
				layout : 'border',
				items : [{
							region : 'west',
							layout : 'fit',
							// el : 'west-div',
							split : true,
							width : 200,
							autoScroll : true,
							collapsible : true,
							border : false,
							items : [tree]
						}, {
							region : 'center',
							id : 'center-panel',
							layout : 'border',
							items : [{
										region : 'north',
										id : 'north-panel',
										items : [tbar]
									}, {
										region : 'center',
										layout : 'fit',
										id : 'dept-center-panel',
										items : [deptForm]
									}]
						}]
			});

	// 开始时设置为在大唐灞桥电厂下增加
	function initadd() {
		var node = tree.getSelectionModel().getSelectedNode();
		method = "add";
		if (node == null) {
			Ext.Msg.alert('提示信息', '请选择一个上级部门！');
			return;
		};
		Ext.get("deptCode").dom.readOnly = false;
		Ext.get("deptName").dom.readOnly = false;
		Ext.get("manager").dom.readOnly = false;
		Ext.get("memo").dom.readOnly = false;
		var t = node.text;
		deptForm.getForm().reset();
		Ext.get("pDeptName").dom.value = node.text;
		deptForm.setTitle("在" + node.text + "下增加部门");

	}
	initadd();
		// --------------

});
