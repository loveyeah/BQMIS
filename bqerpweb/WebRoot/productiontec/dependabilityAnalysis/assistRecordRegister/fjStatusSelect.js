Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	// // -----------定义tree-----------------
	var root = new Ext.tree.AsyncTreeNode({
				id : "0",
				code : 0,
				text : "辅机设备状态"
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
				loader : new Tree.TreeLoader({})
			});
	mytree.on('beforeload', function(node) {

				mytree.loader.dataUrl = 'productionrec/findFjStatusNodeByPNode.action?pid='
						+ node.attributes.code;
			});

	root.select();
	root.expand();

	mytree.on("click", clickTree, this);


	function clickTree(node) {
		var location = new Object();
		if(node.id!="0")
		{
		location.code = node.id;
		location.name = node.text;
		}
		else
		{
			location.code="";
			location.name="";
		}
		window.returnValue = location;
		window.close();

	};
    root.expand();//展开根节点
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		items : [mytree]
	});

});