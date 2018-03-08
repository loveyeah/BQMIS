Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	//	// -----------定义tree-----------------
var mytree=new Ext.tree.TreePanel({   
　　　el:"mytree",   
　　　animate:true,   
　　　collapsible:true,   
　　　enableDD:true,   
　　　enableDrag:true,   
　　　rootVisible:false,   
　　　autoScroll:true,   
//　　　autoHeight:true,   
　　//　width:150,   
　　　lines:true
　});   
　   
　//根节点   
　var root=new Ext.tree.TreeNode({   
　　　id:"root",   
　　　text:"单据类型",   
　　　expanded:true  
　});   
　   
　//第一个子节点
var sub1=new Ext.tree.TreeNode({
 id:'1',
 text:'固定资产类',
 leaf:true
});

//第二个子节点
var sub2=new Ext.tree.TreeNode({
 id:'2',
 text:'专项物资类',
 leaf:true
});

var sub5=new Ext.tree.TreeNode({   // add by ywliu 20091023
　　id:"12",   
　　text:"计算机相关材料",   
　　leaf:true
　});   
var sub3=　new Ext.tree.TreeNode({   
　　　id:"4",   
　　　text:"生产类",
   leaf:true
　});   
var sub4=new Ext.tree.TreeNode({   
　　　id:"5",   
　　　text:"行政办公类"  ,
    leaf:true
　});

var sub6=new Ext.tree.TreeNode({   // add by ywliu 20091023
　　id:"15",   
　　text:"劳保用品类",   
　　leaf:true
　});   
　root.appendChild(sub3); 
root.appendChild(sub4); 
 root.appendChild(sub1); 
　root.appendChild(sub2); 
　root.appendChild(sub5);  // add by ywliu 20091023
root.appendChild(sub6);  //add by fyyang 20100203 
  
　   

　   
　mytree.setRootNode(root);//设置根节点   
　   
　//mytree.render();//不要忘记render()下,不然不显示哦   

	mytree.on("click", clickTree, this);


	function clickTree(node) {
		
		if(node.isLeaf())
		{
		var typeId = new Object();
		typeId.id=node.id;
		typeId.text=node.text;
		window.returnValue = typeId;
		window.close();
		}
	};


	
	
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		items : [mytree]
	});

});