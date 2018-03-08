Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {
	var shiftId = getParameter('shiftId');
	function treeClick(node, e) {
		e.stopEvent();
		ds.load({
			params : {
				deptId : node.id,
				method: 'dept'
			}
		});
	};
	var Tree = Ext.tree;
	var tree = new Tree.TreePanel({
		el : 'tree',
		title : '选择部门',
		autoScroll : true,
		root : root,
		animate : true,// 是否有动画效果
		enableDD : false,// 是否可以拖放节点
		useArrows : false, // 文件夹前显示的图标改变了不在是+号了
		border : false,
		rootVisible : true,
		containerScroll : true,// 是否含滚动条
		lines : true,
		loader : new Tree.TreeLoader({
			dataUrl : 'runlog/getDeptTree.action'
		})
	});

	tree.on('beforeload', function(node) {
		tree.loader.dataUrl = 'runlog/getDeptTree.action?pid=' + node.id;
	});

	var root = new Tree.AsyncTreeNode({
		text : '部门体系',
		iconCls : 'home',
		draggable : false,
		id : '0'
	});
	tree.setRootNode(root);
	root.expand();
	tree.on('click', treeClick);
	tree.render();
	var fuuzy = new Ext.form.TextField({
		id : "fuzzy",
		name : "fuzzy"
	});

	var item = Ext.data.Record.create([{
		name : 'empCode'
	}, {
		name : 'chsName'
	}, {
		name : 'deptId'
	}]);
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false,
		listeners : {
			rowselect : function(sm, row, rec) {
			}
		}
	});
	var item_cm = new Ext.grid.ColumnModel([sm, {
		header : '员工编号',
		dataIndex : 'empCode',
		align : 'center'
	}, {
		header : '员工名称',
		dataIndex : 'chsName',
		align : 'center'
	}]);
	item_cm.defaultSortable = true;
	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'runlog/getEmpbyList.action'
		}),
		reader : new Ext.data.JsonReader({}, item)
	});

	var Grid = new Ext.grid.EditorGridPanel({
		title : '值班人员设置',
		ds : ds,
		cm : item_cm,
		sm : sm,
		width : 300,
		height : 380,
		autoScroll : true,
		tbar : [fuuzy, {
			text : "查询",
			iconCls : 'query',
			handler : function() {
				if(Ext.get('fuzzy').dom.value == "")
				{
					Ext.MessageBox.alert('提示', '因为数据量较大，请先输入查询的员工编号或名称!');
				}
				else
				{
					ds.load({
						params : {
							fuzzy : Ext.get('fuzzy').dom.value,
							method: 'fuzzy'
						}
					});
				}
			}
		}],
		border : false,
		viewConfig : {
			forceFit : false
		},
		buttons : [{
			text : '保存',
			iconCls : 'save',
			handler : function() {
				var selectedRows = Grid.getSelectionModel().getSelections();
				if (selectedRows.length > 0) {
					var ids = [];
					var deps = [];

					for (var i = 0; i < selectedRows.length; i += 1) {
						var member = selectedRows[i].data;
						if (member.empCode) {
							ids.push(member.empCode);
							deps.push(member.deptId);
						}
					}
					Ext.Ajax.request({
						url : 'runlog/addShiftWorker.action',
						params : {
							ids : ids.join(","),
							deps : deps.join(","),
							shiftId : shiftId
						},
						method : 'post',
						waitMsg : '正在保存数据...',
						success : function(result, request) {
							var o = new Object();
							o.value = "true";
							window.returnValue = o;
							window.close();
						},
						failure : function(result, request) {
							Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
							window.close();
						}
					});
				} else {
					Ext.Msg.alert('提示', '请选择您要删除的记录!');
				}
			}
		}, {
			text : '取消',
			iconCls : 'cancer',
			handler : function() {
				window.close();
			}
		}]
	});

	var left = new Ext.Panel({
		region : 'west',
		layout : 'fit',
		border : true,
		width : 200,
		height : 380,
		collapsible : true,
		items : [tree]
	});
	var right = new Ext.Panel({
		region : "center",
		layout : 'fit',
		width : 300,
		height : 380,
		collapsible : true,
		items : [Grid]
	});
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		items : [left, right]
	});
})