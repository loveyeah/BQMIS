/** 
 * 功能:专业选择
 *    仅限window.showModalDialog(url,args,params)模式打开 
 *    其中args 格式为:{selectModel:'cascade',rootNode:{id:'10',text:'单元长'},specialType:'0'}
 *    selectModel:可取'multiple':多选; 'single':单选; 'cascade':级联多选 
 *    rootNode中id为根结点ID
 *    rootNode中text为根结点显示内容 
 *    specialType 专业类别（['0', '公用'], ['1', '运行'], ['2', '检修']）
 *    不要求传入专业类别：''
 *    返回参数:格式为{ids:aids,names:anames}
 *    其中aids为专业主键数组(Array)
 *    其中anames为专业名称主键数组(Array)
 *    //join(",")
 */

Ext.onReady(function(){  
	var arg = window.dialogArguments;
	var selectModel =arg?arg.selectModel:'single'; 
	var rootNode = arg?arg.rootNode:{id:'0',text:'专业树'}; 
	var specialType = arg?arg.specialType:'';
	var Tree = Ext.tree;
	var sroot = new Tree.AsyncTreeNode(rootNode);
	var tbar = new Ext.Toolbar({
		items:[{
			text:'专业选择:',
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
				var selectNodes = stree.getChecked();
				var aids = new Array();
				var anames =new Array() ;
			    if(selectNodes.length >0)
			    {
			    	for(var i=0;i<selectNodes.length;i++)
			    	{
			    		aids.push(selectNodes[i].id);
			    		anames.push(selectNodes[i].text);
			    	} 
			    	window.returnValue = {ids:aids.join(","),names:anames.join(",")};
			    	window.close();
			    }
			}
		}] 
	});
	 
	var stree = new Tree.TreePanel({
		el : 'specials-tree-div',
		tbar: tbar,
		checkModel : selectModel, // single对树的级联多选cascade
		onlyLeafCheckable : false,// 对树所有结点都可选 
		autoWidth : true,
		autoHeight:true,
		layout:'fit',
		autoScroll : true,
		root : sroot,
		animate : true,
		enableDD : false,
		border : false,
		rootVisible : true,
		containerScroll : true,
		loader : new Tree.TreeLoader({
			dataUrl : "comm/getSpecialsByPCode.action?enterpriseCode=hfdc&specialType="+specialType,
			baseAttrs : {
				uiProvider : Ext.tree.TreeCheckNodeUI
			}
		})
	}) 
	stree.render();
	sroot.expand();
});