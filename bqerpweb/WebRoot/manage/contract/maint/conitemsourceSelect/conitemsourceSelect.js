Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
//	var currentType = '';
//	var currentNode;
//	var date = new Date();
	var root = new Ext.tree.AsyncTreeNode({
		id : "0",
		code : 0,
		text : "费用来源"
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
			dataUrl : "managecontract/findByPItemId.action"
		})
	});
	root.select();
	root.expand();
	mytree.on("click", clickTree, this);

	function clickTree(node) {
		var location = new Object();
		if(node.id!="0")
		{
		location.itemCode = node.attributes.code;
		location.itemName = node.text;
		}
		else
		{
			location.itemCode="";
			location.itemName="";
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