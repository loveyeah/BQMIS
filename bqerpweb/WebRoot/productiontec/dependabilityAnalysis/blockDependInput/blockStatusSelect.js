Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var currentType = '';
	var currentNode;

	var root = new Ext.tree.AsyncTreeNode({
				id : "0",
				code : 0,
				text : "机组状态"
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

				mytree.loader.dataUrl = 'productionrec/findNodeByPNode.action?pid='
						+ node.attributes.code;
			});

	root.select();
	root.expand();
	mytree.on("click", clickTree, this);

	// ---------树的click事件
	function clickTree(node) {
		currentNode = node;

//		if (node.id != "0") {
//			currentType = '';
//			pNode = node.parentNode;
//			// Ext.get("model.jzztName").dom.value = pNode.text;
//			myaddpanel.setTitle("<<" + node.text + ">>详细信息");
//		} else {
//
//			myaddpanel.setTitle("在目录<<" + node.text + ">>下增加");
//			add();
//			return false;
//		}
		Ext.Ajax.request({
			url : 'productionrec/findNodeInfo.action',
			params : {
				nodeid : currentNode.id
			}
//			,
//			method : 'post',
//			waitMsg : '正在加载数据...',
//
//			success : function(result, request) {
//				var json = eval('(' + result.responseText + ')');
//
//				Ext.get("model.jzztId").dom.value = json.jzztId;
//				Ext.get("model.jzztCode").dom.value = json.jzztCode;
//				Ext.get("model.jzztName").dom.value = json.jzztName;
//				Ext.get("model.daddyCode").dom.value = json.daddyCode;
//				if (json.jzztDescription == null)
//					Ext.get("model.jzztDescription").dom.value = "";
//				else
//					Ext.get("model.jzztDescription").dom.value = json.jzztDescription;
//				if (json.displayNo == null)
//					Ext.get("model.displayNo").dom.value = "";
//				else
//					Ext.get("model.displayNo").dom.value = json.displayNo;
//
//			},
//			failure : function(result, request) {
//				Ext.MessageBox.alert('错误', '操作失败!');
//			}
		});
	};
	
	function treeClick(node, e) {
		  e.stopEvent();
		  		if(!confirm("确定选择["+node.text+"]吗?"))
		  		return ;
		  	    window.returnValue = node.attributes;
		        window.close();
	};
	
	var viewport = new Ext.Viewport({
		layout : 'fit',
		items : [{
			region : 'center',
			layout : 'fit',
			margins : '0 0 0 0',
			collapsible : true,
			border : false,
			items : [new Ext.TabPanel({
				activeTab : 0,
				layoutOnTabChange : true,
				items : [{
					title : '机组状态树',
					border : false,
					autoScroll : true,
					items : [mytree]
				}]
			})]
		}]
	});
	mytree.on('click', treeClick);
});