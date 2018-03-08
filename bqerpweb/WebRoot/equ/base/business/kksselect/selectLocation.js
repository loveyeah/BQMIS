Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	// // -----------定义tree-----------------
	var root = new Ext.tree.AsyncTreeNode({
		id : "root",
		text : "位置树"
	});

	var mytree = new Ext.tree.TreePanel({
		renderTo : "treepanel",
		region : 'center',
		title : '设备位置树',
//		 animate:true,   
//         enableDD:true, 
		height : 900,
		root : root,
		requestMethod : 'GET',
		loader : new Ext.tree.TreeLoader({
			url : "equ/getLocationTreeList.action",
			baseParams : {
				id : 'root'
			}

		})
	});

	mytree.on("click", clickTree, this);
	mytree.on('beforeload', function(node) {
		mytree.loader.dataUrl = 'equ/getLocationTreeList.action?id=' + node.id;
		//mytree.loader.dataUrl = 'test.txt';
	}, this);

	function clickTree(node) {
		var location = new Object();
		if(node.id!="root")
		{
		location.code = node.id;
		location.name = node.text.substring(node.text.indexOf(' ') + 1,
				node.text.length);
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