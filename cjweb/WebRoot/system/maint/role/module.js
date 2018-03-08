// 设置树的点击事件
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var o=null;
	var Tree = Ext.tree;
	var wroot = new Tree.AsyncTreeNode({
		text : '漕泾电厂',
		draggable : false,
		id : '1'
	});
	var wtree = new Tree.TreePanel({
		checkModel : 'cascade', // 对树的级联多选
		onlyLeafCheckable : false,// 对树所有结点都可选
		autoScroll : true,
		autoHeight : true,
		split : true,
		root : wroot,
		animate : true,
		enableDD : false,
		border : false,
		rootVisible : true,
		containerScroll : true,
		loader : new Tree.TreeLoader({
			dataUrl : 'system/findFiles.action',
			baseAttrs : {
				uiProvider : Ext.tree.TreeCheckNodeUI
			}
		})
	})
	wtree.on('beforeload', function(node) {
		wtree.loader.dataUrl = 'system/findFiles.action'
	});
	wtree.setRootNode(wroot);
	wroot.expand();
	var tbar = new Ext.Toolbar({
		items : [{
			id : 'btnUpdate',
			text : "确定",
			iconCls : 'button',
			handler : function(){
				var selectNodes = wtree.getChecked();
				var aids = "";
				var anames = "";
				if (selectNodes.length > 0) {
					for (i = 0; i < selectNodes.length; i++) { 
						if(selectNodes[i].attributes.leaf)
						{ 
							aids = aids + selectNodes[i].id + ","
							anames = anames + selectNodes[i].text + ","
						}
					}
				}
				o = new Object({
					aids : aids,
					anames : anames
				})
				window.returnValue = o;
				window.close();
			}
		}, '-', {
			id : 'btnReflesh',
			text : "刷新",
			iconCls : 'reflesh',
			handler : function() {
				wtree.getRootNode().reload();
				b=null;
			}
		},'-', {
			id : 'btnClose',
			text : "关闭",
			iconCls : 'close',
			handler : function() {
				window.close();
				o=null;
				window.returnValue = o;
			}
		}]
	});
	var viewport = new Ext.Viewport({
		layout : 'fit',
		items : [{
			tbar:tbar,
			layout : 'fit',
			autoScroll : true,
			items :[wtree]}]
	});
});