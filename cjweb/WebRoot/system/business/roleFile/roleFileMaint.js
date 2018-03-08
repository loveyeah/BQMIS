// 设置树的点击事件
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var w = screen.availWidth - 500;
	var h = screen.availHeight - 290;
	var roleId = "";
	var fileId;
	var scrwidth = document.body.clientWidth;
	Ext.QuickTips.init();
	// 设置已选功能模块树

	var rtbar = new Ext.Toolbar({
		items : ['<font color="blue">角色列表</font>', {
			id : 'rlikeName',
			xtype : 'textfield',
			width : 80
		}, '-', {
			id : 'btnQuery',
			text : "模糊查询",
			handler : function() {
				Ext.Ajax.request({
					url : 'system/findRole.action',
					params : {
						start : 0,
						limit : 18,
						likename : Ext.get("rlikeName").dom.value
					},
					waitMsg : '正在查询数据...',
					success : function(result, request) {
						var gridData = eval('(' + result.responseText + ')');
						ds.loadData(gridData);
					},
					failure : function(result, request) {
						Ext.Msg.alert('提示信息','失败，请联系管理员！');
					}
				})
			}
		}]
	});
	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'system/findRole.action',
			method : 'post'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'data.total',
			root : 'data.rolesList'
		}, [{
			name : 'roleId'
		}, {
			name : 'roleName'
		}, {
			name : 'line'
		}, {
			name : 'memo'
		}, {
			name : 'isUse'
		}, {
			name : 'enterpriseCode'
		}, {
			name : 'modifyBy'
		}, {
			name : 'modifyDate'
		}])
	});
	ds.load({
		params : {
			start : 0,
			limit : 18,
			likename : ''
		}
	});
	ds.on('beforeload', function() {
		Ext.apply(this.baseParams, {
			likename : Ext.get("rlikeName").dom.value
		});
	});
	// 单击Grid行事件
	var sm = new Ext.grid.RowSelectionModel({
		singleSelect : true,
		listeners : {
			rowselect : function(sm, row, rec) {
				Ext.Msg.wait("请等候", "加载中", "操作进行中...");
				roleId = rec.data.roleId;
				atree.getRootNode().reload();
				wtree.getRootNode().reload();
				setTimeout(function() {
					Ext.Msg.hide();
				}, 250);
			}
		}
	});
	
	var colModel = new Ext.grid.ColumnModel([{
		header : '角色名称',
		dataIndex : 'roleName',
		width : 170
	}]);
	// 排序
	colModel.defaultSortable = true;
	var rbbar = new Ext.PagingToolbar({
		pageSize : 18,
		store : ds,
		displayInfo : true,
		displayMsg : '共 {2} 条',
		emptyMsg : "没有记录"
	})
	var roleGrid = new Ext.grid.GridPanel({
		id : 'role-grid',
		el : 'role-div',
		ds : ds,
		cm : colModel,
		sm : sm,
		bbar : rbbar,
		tbar : rtbar,
		width : 220,
		fitToFrame : true,
		border : true,
		listeners : {
			render : function(g) {
				g.selModel.selectFirstRow();
			},
			delay : 10
		}
	});
	var Tree = Ext.tree;
	var aroot = new Tree.AsyncTreeNode({
		text : '漕泾热电厂',
		draggable : false,
		id : '1'
	});
	var atree = new Tree.TreePanel({
		el : 'file-tree-div',
		title : '已选功能模块树',
		checkModel : 'cascade', // 对树的级联多选
		onlyLeafCheckable : false,// 对树所有结点都可选
		autoWidth : true,
		autoScroll : true,
		root : aroot,
		animate : true,
		enableDD : false,
		border : false,
		rootVisible : true,
		containerScroll : true,
		loader : new Tree.TreeLoader({
			dataUrl : 'system/findFilesByRoleId.action?roleId=' + roleId,
			baseAttrs : {
				uiProvider : Ext.tree.TreeCheckNodeUI
			} // 添加 uiProvider 属性
		})
	})
	atree.on('beforeload', function(node) {
		atree.loader.dataUrl = 'system/findFilesByRoleId.action?roleId='
				+ roleId
	});
	atree.on('click', treeClick);
	atree.setRootNode(aroot);
	atree.render();
	aroot.expand();
	function treeClick() {

	}
	roleGrid.render();
	roleGrid.addListener('rowcontextmenu', function(_grid, rowIndex, e) {
		var menu = new Ext.menu.Menu({
			id : 'mainMenu',
			items : [new Ext.menu.Item({
				text : '查看用户',
				iconCls : 'personp',
				handler : function() {
					var User = Ext.data.Record.create([{
						name : 'workerId'
					}, {
						name : 'workerCode'
					}, {
						name : 'workerName'
					}])
					var user_store = new Ext.data.Store({
						proxy : new Ext.data.HttpProxy({
							url : 'system/findUserByRP.action'
						}),
						reader : new Ext.data.JsonReader({
							totalCount : 'data.totalCount',
							root : 'data.list'
						}, User)
					});
					var user_cm = new Ext.grid.ColumnModel([
							new Ext.grid.RowNumberer(), {
								header : '工号',
								dataIndex : 'workerCode',
								align : 'left',
								width : 200
							}, {
								header : '姓名',
								dataIndex : 'workerName',
								align : 'left',
								width : 200
							}]);
					user_cm.defaultSortable = true;		
					var usergrid = new Ext.grid.EditorGridPanel({
						ds : user_store,
						cm : user_cm,
						region : 'center',
						title : '用户列表',
						fitToFrame : true,
						border : false
					})

					var userwin = new Ext.Window({
						closable : true,
						width : 300,
						height : 400,
						plain : true,
						layout : 'border',
						items : [usergrid]
					});
					userwin.show();
					Ext.Ajax.request({
						url : 'system/findUserByRP.action',
						method : 'post',
						waitMsg : '正在查询数据...',
						params : {
							roleId : roleGrid.getStore().getAt(rowIndex).data.roleId,
							userlike : ''
						},
						success : function(result, request) {
							var gridData = eval('(' + result.responseText + ')');
							user_store.loadData(gridData);
						},
						failure : function(result, request) {
							Ext.MessageBox.alert('提示信息','失败！');
						}
					})
				}
			})]
		});
		e.preventDefault();
		menu.showAt(e.getXY());
	});

	// 设置树形面板
	var Tree = Ext.tree;
	var wroot = new Tree.AsyncTreeNode({
		text : '漕泾电厂',
		draggable : false,
		id : '1'
	});
	var wtree = new Tree.TreePanel({
		el : 'waitfile-tree-div',
		title : '未选功能模块树',
		checkModel : 'cascade', // 对树的级联多选
		onlyLeafCheckable : false,// 对树所有结点都可选
		autoWidth : true,
		autoScroll : true,
		root : wroot,
		animate : true,
		enableDD : false,
		border : false,
		rootVisible : true,
		containerScroll : true,
		loader : new Tree.TreeLoader({
			dataUrl : 'system/findWaitFilesByBoleId.action',
			baseAttrs : {
				uiProvider : Ext.tree.TreeCheckNodeUI
			}
		})
	})
	wtree.on('beforeload', function(node) {
		wtree.loader.dataUrl = 'system/findWaitFilesByBoleId.action?&roleId='
				+ roleId
	});
//	var aids = "";
//	wtree.on("check", function(node, checked) {
//		aids += node.id;
//		aids += ",";
//	});
//	if (aids.length > 0) {
//		aids = aids.substring(0, ids.length - 1);
//	}
//	var wids = "";
//	atree.on("check", function(node, checked) {
//		wids += node.id;
//		wids += ",";
//	});
//	if (wids.length > 0) {
//		wids = wids.substring(0, ids.length - 1);
//	}
	//取得以选择节点的id与text
	function getCheckNode(inTree){
		var selectNodes=inTree.getChecked();
//		var ids =new Array();
		var ids ="";
		if(selectNodes.length>0){
			for(i=0;i<selectNodes.length;i++){
				if(selectNodes[i].attributes.leaf){
//				ids.push(selectNodes.id);
				ids=ids+selectNodes[i].id+","
				}
			}
		}
		if(ids.length<1 && ids==""){
			Ext.Msg.alert('提示信息','请选择叶子模块！');
			return
		}
		return ids;
	}
	wtree.setRootNode(wroot);
	wtree.render();

	wroot.expand();
	var grantBar = new Ext.Toolbar({
		items : [{
			id : 'btnGrant',
			text : "<<<",
			height : 20,
			handler : function() {
				if(roleId==""){
					Ext.Msg.alert('提示信息',"请选择角色！");
					return false;
				}
				var aids=getCheckNode(wtree);
				Ext.Ajax.request({
					url : 'system/addRoleFile.action',
					params : {
						ids : aids,
						'rolefile.roleId' : roleId
					},
					method : 'post',
					waitMsg : '正在处理...',
					success : function(result, request) {
						atree.getRootNode().reload();
						wtree.getRootNode().reload();
					},
					failure : function(result, request) {
						Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
					}
				});
				aids = "";
			}
		}]
	});
	grantBar.render("grant-div");

	var revokeBar = new Ext.Toolbar({
		items : [{
			id : 'btnRevoke',
			text : ">>>",
			height : 20,
			handler : function() {
				if(roleId==""){
					Ext.Msg.alert('提示信息',"请选择角色！");
					return false;
				}
				var wids = getCheckNode(atree);
				Ext.Ajax.request({
					url : 'system/delRoleFile.action',
					params : {
						ids : wids,
						'rolefile.roleId' : roleId
					},
					method : 'post',
					waitMsg : '正在处理...',
					success : function(result, request) {
						atree.getRootNode().reload();
						wtree.getRootNode().reload();
					},
					failure : function(result, request) {
						Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
					}
				});
				wids = "";
			}
		}]
	});
	revokeBar.render("revoke-div");
	setTimeout(function() {
		Ext.get('loading').remove();
		Ext.get('loading-mask').fadeOut({
			remove : true
		});
	}, 250);
	var layout=new Ext.Viewport({
		layout : "border",
		border : false,
		items : [{
			region : 'center',
			layout : 'border',
			border : false,
			split : true,
			items : [{
				region : 'center',
				layout : 'fit',
				border : true,
				margins : '0 0 0 0',
				split : true,
				collapsible : false,
				items : [atree]
			},{
				region : 'east',
				layout : 'border',
				border : false,
				width : 40,
				items :[{
					region : 'north',
					layout : 'fit',
					border : true,
					margins : '200 0 0 0',
//					margins : '300 0 0 0',
					split : false,
					height : 30,
					items : [grantBar]
				},{
					region : 'center',
					layout : 'fit',
					border : false,
					margins : '0 0 0 0',
					split : false
//					height : 20,
//					items : [grantBar]
				},{
					region : 'south',
					layout : 'fit',
					border : true,
					margins : '0 0 300 0',
//					margins : '0 0 400 0',
					split : false,
					height : 30,
					items : [revokeBar]
				}]
			}]
		}, {
			region : "west",
			collapsible : true,
//			title : '角色列表',
			layout : 'fit',
			border : true,
			margins : '0 0 0 50',
//			width : 230,
			width : scrwidth/11*3,
			split : true,
			items : [roleGrid]
		},{
			region : "east",
//			title : '未选权限列表',
			layout : 'fit',
			collapsible : true,
			border : true,
			margins : '0 50 0 0',
//			width : 230,
//			width : 300,
			width : scrwidth/11*3,
			split : true,
			items : [wtree]
		}]
	
	})
});
