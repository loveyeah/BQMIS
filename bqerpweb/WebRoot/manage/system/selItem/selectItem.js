Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() { 
     var Tree = Ext.tree;
	var tree = new Tree.TreePanel({ 
		autoScroll : true,
		root : root,
		autoHeight : true,
		animate : true,// 是否有动画效果
		enableDD : false,// 是否可以拖放节点
		useArrows : false, // 文件夹前显示的图标改变了不在是+号了
		border : false,
		rootVisible : true,
		containerScroll : true,// 是否含滚动条
		lines : true,
		loader : new Tree.TreeLoader({
			dataUrl : 'manager/getTreeList.action'
		})
	});

	tree.on('beforeload', function(node) {
		tree.loader.dataUrl = 'manager/getTreeList.action?pid=' + node.id;
	});
 
	function treeClick(node, e) {
		  e.stopEvent();
		  if(node.attributes.description == 'Y')
		  {
		  		if(!confirm("确定选择["+node.text+"]吗?"))
		  		return ;
		  	    window.returnValue = node.attributes;
		        window.close();
		  } 
		  else
		  {
		  	alert("["+node.text+"]没有数据,不能选择!");
		  }
	};
	var root = new Tree.AsyncTreeNode({
		text : '经营指标编码体系',
		//iconCls : 'home',
		iconCls : 'x-tree-node-icon',
		draggable : false,
		id : '0'
	});
	tree.setRootNode(root);
	root.expand();
//	tree.on('dblclick', treeClick);
	tree.on('click', treeClick);
	tree.render(Ext.getBody());
});