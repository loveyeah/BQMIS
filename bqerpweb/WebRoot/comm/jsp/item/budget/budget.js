
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

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
				loader : new Tree.TreeLoader({
							dataUrl : 'managebudget/findBudgetTreeForWz.action?pid=002'
						})
			});
	tree.on('beforeload', function(node) {
				tree.loader.dataUrl = 'managebudget/findBudgetTreeForWz.action?pid='
						+ node.attributes.code;
			});
	var root = new Tree.AsyncTreeNode({
				text : '财务预算科目',
				iconCls : 'x-tree-node-icon',
				draggable : false,
				code : '002'
			});
	tree.setRootNode(root);
	root.expand();

	tree.on("click", clickTree, this);


	function clickTree(node) {
		
//		if(node.isLeaf())
//		{
		var obj = new Object();
		obj.itemCode=node.attributes.code;
		obj.itemName=node.text;
		window.returnValue = obj;
		window.close();
		//}
	};


	
	
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		items : [tree]
	});

});