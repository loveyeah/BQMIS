Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

//add by ming_lian 2010-8-12
Ext.onReady(function() {
	
	//定义树结构
	var deptId;
	var treeLoader = new Ext.tree.TreeLoader({
		dataUrl : 'comm/getDeptsByPid.action'
	})
	// 部门树
	var deptTree = new Ext.tree.TreePanel({
		region : 'center',
		animate : true,
		//height:700,
		allowDomMove:false,
		autoScroll : true,
		containerScroll : true,
		collapsible : true,
		split : true,
		border : false,
		rootVisible : false,
		root : root,
		animate : true,
		enableDD : false,
		border : false,
		containerScroll : true,
		loader :treeLoader
	});  
	// 定义根节点
	var root = new Ext.tree.AsyncTreeNode({
		text : "灞桥电厂",
		isRoot : true,
		id : '0'
		
	});
	
	deptTree.on("click", treeClick);
	deptTree.setRootNode(root);
	root.select();
	
	/**
	 * 点击树时处理
	 */
	function treeClick(node, e) {
		e.stopEvent();
			
		deptId = node.id;
				
		// 导入已分配的岗位数据
		AssignMentPostStore.baseParams = {
			deptId :  node.id
		}
		AssignMentPostStore.load({
			params : {
					deptId : node.id
				}
			});
		currentID = node.id;
		currentName = node.text;
		node.toggle();
	}
	
	var hrSm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false
	});
	
	var fuzzyName = new Ext.form.TextField({
		id : 'fuzzy',
		name:'fuzzy'
	})
	
	var tbar = new Ext.Toolbar({
		items : ['岗位名称：',fuzzyName,
			{text : '查询',
			iconCls : 'query',
			id : 'btuQuery',
			handler : function(){
				
				deptId = AssignMentPostStore.getAt(0)==null?"":AssignMentPostStore.getAt(0).get("deptId");
				AssignMentPostStore.baseParams = {
					stationName : fuzzyName.getValue(),
					deptId : deptId
				}
				AssignMentPostStore.load({
					params : {
						start : 0,
						limit : 18
					}
				});
			}},
			{text :'确定',
			iconCls : 'ok',
			id : 'btuOk',
			handler : function(){
						var selections = AssignMentPostGrid.selModel.getSelections();
					
						if (selections.length == 0) {
							Ext.Msg.alert("提示", "请选择所要的岗位！");
						}
						window.returnValue = selections;
						window.close();
						}
			}]
			});		
	
	var AssignMentPostData = Ext.data.Record.create([
			// 部门ID
			{
				name : 'deptId'
			},
			// 岗位ID
			{
				name : 'stationId'
			},
			// 岗位名称
			{
				name : 'stationName'
			}]);
	// 已分配的岗位store
	var AssignMentPostStore = new Ext.data.JsonStore({
				url : 'hr/getAssignMentPostList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : AssignMentPostData
			});	
	
	// 已分配的岗位grid
	var AssignMentPostGrid = new Ext.grid.GridPanel({
				autoScroll : true,
				autoHeight: true,
				autoWidth :true,
				title :"已分配岗位",
				enableColumnMove : false,
				clicksToEdit :1,
				sm : hrSm,
				store : AssignMentPostStore,
				columns : [hrSm,{
							header : "部门ID",
							sortable : true,
							hidden : true,
							dataIndex : 'deptID'
						},{
							header : "岗位ID",
							sortable : true,
							hidden : true,
							dataIndex : 'stationID'
						},{
							header : "岗位名称",
							width : 200,
							sortable : true,
							dataIndex : 'stationName'
						}],
				
				autoSizeColumns : true
			});
	var leftPanel = new Ext.Panel({
				region : 'west',
				layout : 'fit',
				width : '30%',
				autoScroll : true,
				border:false,
				containerScroll : true,
				collapsible : true,
				split : false,
				items : [deptTree]
			});
	// 右边的panel
	var rightPanel = new Ext.Panel({
				layout : 'form',
				border:false,
				items : [tbar,AssignMentPostGrid]
			});
	// 显示区域
	var view = new Ext.Viewport({
				enableTabScroll : true,
				autoScroll:true,
				layout : "border",
				items : [leftPanel,{
					region :'center',
					layout : 'form',
					autoHeight:true,
					autoWidth:true,
					autoScroll:true,
					items : [rightPanel]
				}]
			});
	
})

