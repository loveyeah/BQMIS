/** 
 * 功能:部门选择
 *    仅限window.showModalDialog(url,args,params)模式打开 
 *    其中args 格式为:{selectModel:'cascade',rootNode:{id:'2',text:'检修部门'}}
 *    selectModel:可取'multiple':多选; 'single':单选; 'cascade':级联多选 
 *    rootNode中id为根结点ID
 *    rootNode中text为根结点显示内容 
 *    
 *    返回参数:格式为{ids:aids,names:anames}
 *    其中aids为部门主键数组(Array)
 *    其中anames为部门名称主键数组(Array)
 *    //join(",")
 */

Ext.onReady(function(){  
	var arg = window.dialogArguments;  
	var selectModel =arg?arg.selectModel:'single'; 
	var onlyLeaf = arg?arg.onlyLeaf:false; 
	var rootNode = arg?arg.rootNode:{id:'0',text:'漕泾电厂'};  
	var Tree = Ext.tree;
	var wroot = new Tree.AsyncTreeNode(rootNode);
	var tbar = new Ext.Toolbar({
		items:[{
			text:'部门选择:',
			xtype:'label'
//			//测试
//			xtype:'button',
//			handler : function(){
//				var args = {selectModel:'multiple',rootNode:{id:'1',text:'xxx'}};
//				var rvo = window.showModalDialog('dept.jsp',args,'');
//				alert(rvo.ids.join(","));
//				alert(rvo.names.join(","));
//			}
		},{
			text:'确定',
			xtype:'button',
			handler : function()
			{  
				var selectNodes = wtree.getChecked();
				var aids = new Array();
				var acodes = new Array();
				var anames =new Array() ;
			    if(selectNodes.length >0)
			    {
			    	for(var i=0;i<selectNodes.length;i++)
			    	{
			    		aids.push(selectNodes[i].id);
			    		acodes.push(selectNodes[i].attributes.code);
			    		anames.push(selectNodes[i].text);
			    	} 
			    	window.returnValue = {ids:aids.join(","),codes:acodes.join(","),names:anames.join(",")};
			    	window.close();
			    }
			}
		} ,{
			text:'清除',
			xtype:'button',
			handler : function()
			{   
				var selectNodes = wtree.getChecked(); 
				var aids = new Array();
				var acodes = new Array();
				var anames =new Array() ;
		    	for(var i=0;i<selectNodes.length;i++)
		    	{ 
		    		selectNodes[i].attributes.checked = false; 
		    	    selectNodes[i].getUI().checkbox.checked = false; 
		    	}   
		    	aids.push("");
			    acodes.push("");
			    anames.push("");
				window.returnValue = {ids:aids.join(","),codes:acodes.join(","),names:anames.join(",")};
			    window.close(); 
			}
		}] 
	});
	 
	var wtree = new Tree.TreePanel({
		el : 'dept-tree-div',
		//title : '部门选择',
		tbar: tbar,
		checkModel : selectModel, // single对树的级联多选cascade
		onlyLeafCheckable : onlyLeaf,// 对树所有结点都可选 
		autoWidth : true,
		autoHeight:true,
		layout:'fit',
		autoScroll : true,
		root : wroot,
		animate : true,
		enableDD : false,
		border : false,
		rootVisible : true,
		containerScroll : true,
		loader : new Tree.TreeLoader({
			dataUrl : 'comm/getDeptsByPid.action',
			baseAttrs : {
				uiProvider : Ext.tree.TreeCheckNodeUI
			}
		})
	}) 
	wtree.render();
	wroot.expand();
});