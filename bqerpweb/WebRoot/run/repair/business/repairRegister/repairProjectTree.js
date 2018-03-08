
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	// op 值为'many'的时候，选择多项时树带checkbox
	// var method = 'many'
	var method = getParameter("op");
	var root = new Ext.tree.AsyncTreeNode({
				id : "root",
				text : "检修项目"
			});

	currentNode = root;
	var mytree = new Ext.tree.TreePanel({
				el : "treepanel",
				autoHeight : true,
				root : root,
				checkModel : method == 'many'?'childCascade':'single', // 对树的级联选中所有子结点
				onlyLeafCheckable : false,// 对树所有结点都可选
				autoWidth : true,
				autoScroll : true,
				animate : true,
				enableDD : false,
				border : false,
				rootVisible : true,
//				containerScroll : true,
				tbar : [{
							text : '确定',
							iconCls : 'confirm',
							handler : treeselect
						}],
				requestMethod : 'GET',

				loader : new Ext.tree.TreeLoader({
							url : "manageplan/findRepairTreeList.action",
							baseParams : {
								id : 'root',
								method : method
							},
							baseAttrs : {
								uiProvider : Ext.tree.TreeCheckNodeUI
							} // 添加 uiProvider 属性
						})
			});

	// ---------------------------------------
	function treeselect() {
		if (method == "many") {
			var b = mytree.getChecked();
			var repairId = new Array;
			var repairProjectName = new Array;
			var workingCharge = new Array;
			var workingMenbers = new Array;
			var workingTime = new Array;
			var workingChargeCode = new Array;
			for (var i = 0; i < b.length; i++) {
				repairId.push(b[i].id);
				repairProjectName.push(b[i].text.substring(b[i].text
								.indexOf(' ')
								+ 1, b[i].text.length));
				workingCharge.push(b[i].attributes.workingCharge);
				workingMenbers.push(b[i].attributes.workingMenbers);
				workingTime.push(b[i].attributes.workingTime);
				workingChargeCode.push(b[i].attributes.workingChargeCode);

			}
			Ext.Msg.confirm("选择", "是否确定选择'" + repairProjectName + "'？",

			function(buttonobj) {

						if (buttonobj == "yes") {
							var equ = new Object();
							equ.code = repairId.toString();
							equ.name = repairProjectName.toString();
							equ.workingCharge = workingCharge.toString();
							equ.workingMenbers = workingMenbers.toString();
							equ.workingTime = workingTime.toString();
							equ.workingChargeCode = workingChargeCode
									.toString();
							window.returnValue = equ;
							window.close();
						}
					})

		}
  if(method=="single"){
			var b = mytree.getChecked();
			Ext.Msg.confirm("选择", "是否确定选择'" + b[0].text + "'？",
         
			function(buttonobj) {

						if (buttonobj == "yes") {
							var equ = new Object();
							equ.code = b[0].id.toString();
							equ.name = b[0].text.toString();
							equ.workingCharge = b[0].attributes.workingCharge;
							equ.workingMenbers = b[0].attributes.workingMenbers;
							equ.workingTime = b[0].attributes.workingTime;
							equ.workingChargeCode = b[0].attributes.workingChargeCode;
							window.returnValue = equ;
							window.close();
						}
					})

		
  	
  }
	}
	// -----------树的事件----------------------
	// 树的单击事件
	mytree.on("click", clickTree, this);
	mytree.on('beforeload', function(node) {
				// 指定某个节点的子节点
				mytree.loader.dataUrl = 'manageplan/findRepairTreeList.action?id='
						+ node.id;
			}, this);

	mytree.on("check", function(node, checked) {
				// alert();
			});
	// ---------树的click事
	function clickTree(node) {
		Ext.Msg.confirm("选择", "是否确定选择'" + node.text + "'？",
				function(buttonobj) {
					if (buttonobj == "yes") {
						var equ = new Object();
						if (node.id == "root") {
							Ext.Msg.alert('提示', '不能选择根目录！')
							return;
						}
						equ.code = node.id;
						equ.name = node.text.substring(node.text.indexOf(' ')
										+ 1, node.text.length);
						equ.workingCharge = node.attributes.workingCharge;
						equ.workingMenbers = node.attributes.workingMenbers;
						equ.workingTime = node.attributes.workingTime;
						equ.workingChargeCode = node.attributes.workingChargeCode;
						window.returnValue = equ;
						window.close();
					}
				});
	}
   mytree.render();
   root.expand();// 展开根节点

	 
});