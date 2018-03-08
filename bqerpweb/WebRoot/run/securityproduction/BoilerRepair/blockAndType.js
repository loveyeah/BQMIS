Ext.onReady(function() {
	var arg = window.dialogArguments;
	var selectModel = arg ? arg.selectModel : 'single';
	var onlyLeaf = arg ? arg.onlyLeaf : false;
	var rootNode = (arg && arg.rootNode) ? arg.rootNode : {
		id : 'root',
		text : '锅炉设备树'
	};
	var Tree = Ext.tree;
	var wroot = new Tree.AsyncTreeNode(rootNode);
	var tbar = new Ext.Toolbar({
		items : [{
			text : '锅炉设备树:',
			xtype : 'label'
		}, {
			text : '确定',
			xtype : 'button',
			handler : function() {

				node = wtree.getSelectionModel().getSelectedNode();
					clickTree(node);
		/*		var selectNodes = wtree.getChecked();
				//alert("selectNodes is:"+selectNodes);
			
				var aids = new Array();
				var acodes = new Array();
				var anames = new Array();
				if (selectNodes.length > 0) {

					for (var i = 0; i < selectNodes.length; i++) {
						aids.push(selectNodes[i].id);
						acodes.push(selectNodes[i].attributes.code);
						anames.push(selectNodes[i].text);
					}
					window.returnValue = {
						ids : aids.join(","),
						codes : acodes.join(","),
						names : anames.join(",")
					};
					window.close();
				}*/
			}
		}, {
			text : '清除',
			xtype : 'button',
			handler : function() {
				var selectNodes = wtree.getChecked();
				var aids = new Array();
				var acodes = new Array();
				var anames = new Array();
				for (var i = 0; i < selectNodes.length; i++) {
					selectNodes[i].attributes.checked = false;
					selectNodes[i].getUI().checkbox.checked = false;
				}
				aids.push("");
				acodes.push("");
				anames.push("");
				window.returnValue = {
					ids : aids.join(","),
					codes : acodes.join(","),
					names : anames.join(",")
				};
				window.close();
			}
		}]
	});
	
	
	

	/* wtree.on("check",function(node,checked){alert();}); */

	function clickTree(node) {
		if(node)
		{
		if(node.id=='root')
		{
				Ext.MessageBox.alert('提示信息', '请选择根节点下面的节点！');
				return;
		}
		Ext.Msg.confirm("选择", "是否确定选择'" + node.text + "'？",
				function(buttonobj) {
					currentNode=node;
					if (buttonobj == "yes") {
						var equ = new Object();
						equ.code = node.id;
						equ.type = node.attributes.code;
						if(node.text!=null&&node.text!="")
						{
						equ.name = node.text.substring(node.text.indexOf(' ')
								+ 1, node.text.length);
						}

						window.returnValue = equ;
						window.close();
					}
				});
	}
	 else {
			Ext.MessageBox.alert('提示信息', '请选择一个树节点！');
			return ;
	}
	}

	
	var wtree =  new Ext.tree.TreePanel({
		el : 'block-tree-div',
		// title : '部门选择',
		tbar : tbar,
		checkModel : selectModel, // single对树的级联多选cascade
		onlyLeafCheckable : onlyLeaf,// 对树所有结点都可选
		autoWidth : true,
		autoHeight : true,
		layout : 'fit',
		autoScroll : true,
		root : wroot,
		animate : true,
		enableDD : false,
		border : false,
		rootVisible : true,
		containerScroll : true,
		loader : new Tree.TreeLoader({
			dataUrl : "security/getTreeListForBoiler.action",
			baseParams : {
				id : 'root'
			},
			baseAttrs : {
				uiProvider : Ext.tree.TreeCheckNodeUI
			}
		})
	})
    wtree.on("click", clickTree, this);
    wtree.on('beforeload', function(node){
    	// 指定某个节点的子节点
    	wtree.loader.dataUrl = 'security/getTreeListForBoiler.action?id=' + node.id; }, this);
	 
	wtree.render();
	wroot.expand();
});