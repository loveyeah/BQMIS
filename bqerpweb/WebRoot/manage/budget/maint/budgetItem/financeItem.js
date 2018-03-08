Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var root = new Ext.tree.AsyncTreeNode({
		id : "0",
		code : 0,
		text : "财务科目"
	});
	var Tree = Ext.tree;
	var mytree = new Tree.TreePanel({
		rootVisible : true,
		autoHeight : true,
		root : root,
		animate : true,
		enableDD : true,
		border : false,
		containerScroll : true,
		loader : new Tree.TreeLoader({
			dataUrl : 'managebudget/findFinanceTree.action'
		})
	});
	mytree.on('beforeload', function(node) {
		mytree.loader.dataUrl = 'managebudget/findFinanceTree.action?pid='
				+ node.attributes.code;
	});
	root.select();
	root.expand();
	mytree.on("click", clickTree, this);
	function clickTree(node, e) {
		e.stopEvent();
			if (!confirm("确定选择[" + node.text + "]吗?"))
				return;
			var obj = new Object();
			obj.finaceName = node.text;
			obj.finaceId = node.id;
			window.returnValue = obj;
			window.close();
	};
	
	mytree.setRootNode(root);
	root.expand();
//	//	tree.on('dblclick', treeClick);
//	mytree.on('click', clickTree);
	mytree.render(Ext.getBody());
});