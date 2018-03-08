Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {
	 
	// 获得年份
	function getDate() {
		var Y;
		Y = new Date();
		Y = Y.getFullYear().toString(10);
		return Y;
	}
	function treeClick(node, e) {
		var node  = tree.getSelectionModel().getSelectedNode();
		var itemId = node.itemId;
		};
		
	var time = new Ext.form.TextField({
				id : 'time',
				allowBlank : true,
				readOnly : true,
				value : getDate(),
//				width : 100,
				listeners : {
					focus : function() {
						WdatePicker({
									dateFmt : 'yyyy',
									alwaysUseStartDate : false,
									onclearing : function() {
										planStartDate.markInvalid();
									}
								});
					}
				}
			});
	
	// 查询
	var btnQuery = new Ext.Button({
				id : "btnQuery",
				text : "查询",
				iconCls : "query",
				handler : function query() {
					tree.loader.dataUrl = 'managebudget/getItemIdTree.action?&budgetTime='+time.getValue();
					root.reload();
				}
			})
			
	var btnOk = new Ext.Button({
				id : "btnOk",
				text : "确定",
				iconCls : "",
				handler : function confirm() {
						var ro = tree.getSelectionModel().getSelectedNode();
						window.returnValue = ro;
						window.close();
				}
			})
	var Tree = Ext.tree;
	var tree = new Tree.TreePanel({
				autoScroll : true,
				root : root,
				layout : 'fit',
				autoHeight : false,
				animate : true,// 是否有动画效果
				enableDD : false,// 是否可以拖放节点
				useArrows : false, // 文件夹前显示的图标改变了不在是+号了
				border : false,
				rootVisible : true,
				containerScroll : true,// 是否含滚动条
				lines : true,
				tbar:['年份',time, '-', btnQuery,'-',btnOk],
				loader : new Tree.TreeLoader({
							dataUrl : 'managebudget/getItemIdTree.action?pid=000&budgetTime='+time.getValue()
						})
			});
		
			
	tree.on('beforeload', function(node) {
				tree.loader.dataUrl = 'managebudget/getItemIdTree.action?pid='
						+ node.attributes.code+'&budgetTime='+time.getValue();
			});
			
			
	var root = new Tree.AsyncTreeNode({
				text : '预算指标',
				iconCls : 'x-tree-node-icon',
				draggable : false,
				code : '000'
			});
	tree.setRootNode(root);
	root.expand();
	tree.on('click', treeClick);

	// 树↑	
		new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		items : [tree]
	});
	root.select();
	
});
