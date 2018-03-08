Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var currentType = 'add';
	var currentNode = new Object();
	currentNode.id = "root";
	currentNode.text = "安装点树";
	//	// -----------定义tree-----------------
	var root = new Ext.tree.AsyncTreeNode({
		id : "root",
		text : "安装点树"
	});

	var mytree = new Ext.tree.TreePanel({
		renderTo : "treepanel",
		height : 900,
		root : root,
		requestMethod : 'GET',
		loader : new Ext.tree.TreeLoader({
			url : "equ/getInstallTreeList.action",
			baseParams : {
				id : 'root'
			}

		})
	});

	mytree.on("click", clickTree, this);
	mytree.on('beforeload', function(node) {
		mytree.loader.dataUrl = 'equ/getInstallTreeList.action?id=' + node.id;
	}, this);

	function clickTree(node) {
		var install = new Object();
		if (node.id != "root") {
			install.code = node.id;
			install.name = node.text.substring(node.text.indexOf(' ') + 1,
					node.text.length);
		} else {
			install.code = "";
			install.name = "";
		}
		window.returnValue = install;
		window.close();
	};
	root.expand();//展开根节点
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		items : [mytree]
	});

});