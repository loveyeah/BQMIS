Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var Tree = Ext.tree;
	var root = new Tree.AsyncTreeNode({
				text : '预算指标编码体系',
				iconCls : 'x-tree-node-icon',
				draggable : false,
				code : '000'
			});
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
							dataUrl : 'managebudget/findBudgetTree.action?pid=000'
						})
			});
	tree.on('beforeload', function(node) {
				tree.loader.dataUrl = 'managebudget/findBudgetTree.action?pid='
						+ node.attributes.code;
			});
	// tree.setRootNode(root);
	 root.expand();
	tree.on('click', treeClick);
	function treeClick(node, e) {
			e.stopEvent();
			Ext.Ajax.request({
				url : 'managebudget/getBudgetItemtxInfo.action',
				params : {
					id : node.id
				},
				method : 'post',
				waitMsg : '正在加载数据...',
				success : function(result, request) {
					var str = result.responseText;
					var o = eval("(" + str.substring(1, str.length - 1) + ")")
					// 是否预算指标
					var isItem = (o.isItem == "Y" ? true : false);
					if (isItem) {
						var obj = new Object();
						obj.itemCode = o.zbbmtxCode;
						obj.itemName = o.zbbmtxName;
						obj.itemId = o.itemId;
						obj.unitId = o.enterpriseCode;
						window.returnValue = obj;
						window.close();
					} else {
					Ext.Msg.alert("提示","非预算指标");
					}
				},
				failure : function(result, request) {
					Ext.MessageBox.alert('提示', '操作失败!');
				}
			});
	}
	var layout = new Ext.Viewport({
				layout : 'fit',
				items : [tree]
			})
});