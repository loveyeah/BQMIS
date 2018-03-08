/**
 * 功能:根据部门选择人员  
 * 仅限window.showModalDialog(url,args,params)模式打开 其中args
 * 		 格式为:{selectModel:'single',notIn:"'999999','888888'",rootNode:{id:'2',text:'检修部门'}}
 *		 selectModel:可取'multiple':多选; 其他为均为单选; (默认为单选)
 *       notIn为过滤掉的工号集 , 格式为'999999','888888'
 *       rootNode根部门结点 
 * 返回参数: 
 * 		单选时: 格式为  {workerCode:'999999',workerName:'管理员',deptId:'12',deptCode:'10',deptName:'管理部门'}
 *      多选时: 格式为  {list:[{...},{....},...],codes:'99992,22222,3333,...',name:'王小宝,杨国庆,...'}
 */
Ext.onReady(function() {
	
	 var deptIdChoose='';
	
	var arg = window.dialogArguments;
	var selectModel = arg ? arg.selectModel : 'multiple';
	// 默认为单选
	var isSingleSelect = !(selectModel == "multiple");
	var notIn = arg?arg.notIn:"";
	var rootNode =  (arg&&arg.rootNode)  ? arg.rootNode : {
		id : '0',
		text : '服务类别'
	};
   var rootNodeObj = new Ext.tree.AsyncTreeNode(rootNode);
	var deptTree = new Ext.tree.TreePanel({
		
		loader : new Ext.tree.TreeLoader({
			dataUrl : 'workbill/findBySPId.action'
		}),
		root : rootNodeObj,
		autoWidth : true, 
		layout : 'fit',
		autoScroll : true, 
		animate : true,
		enableDD : false,
		border : false,
		rootVisible : true,
		containerScroll : true
	});
	
	
	deptTree.on('click',function(node,e){
		e.stopEvent();
		deptIdChoose = node.id;
		ds.load({
					params : {
						start : 0,
						limit : 20
					}
				});
	});
	
//	var deptIdChoose = new Ext.ux.ComboBoxTree({
//		fieldLabel : '所属部门',
//		id : 'deptId',
//		displayField : 'text',
//		width : 180,
//		height:400,
//		valueField : 'id',
//		hiddenName : 'ChooseDeptId',
//		blankText : '所有部门',
//		emptyText : '所有部门',
//		readOnly : true,
//		tree : {
//			xtype : 'treepanel',
//			rootVisible : !(rootNode.id == "-1"),
//			loader : new Ext.tree.TreeLoader({
//				dataUrl : 'comm/getDeptsByPid.action'//'empInfoManage.action?method=getDep'
//			}),
//			root : new Ext.tree.AsyncTreeNode(rootNode)
//		},
//		selectNodeModel : 'all'
//	});
	
	 

	var toolbar = new Ext.Toolbar({
		items : [
//		deptIdChoose, 
			{
			text : '服务编码/服务名模糊查询',
			xtype:'label' 
		}, {
			name : 'queryKey',
			width : '50pt',
			xtype : 'textfield',
			listeners:{   
	            specialkey:function(field,e){   
	                if (e.getKey()==Ext.EventObject.ENTER && this.getValue().length > 0 ){   
	                     ds.load({
							params : {
								start : 0,
								limit : 20
							}
						});
	                }   
	            }   
        	}    
		}, {
			text : '查询',
			iconCls : 'query',
			xtype : 'button',
			handler : function() {
				ds.load({
					params : {
						start : 0,
						limit : 20
					}
				});
			}
		}, {
			text : '确定',
			iconCls : 'confirm',
			xtype : 'button',
			handler : function() {
				chooseWorker();
			}
		}]
	});
	
	function chooseWorker() {
		// 单选
		if (isSingleSelect) {
			var record = grid.getSelectionModel().getSelected();
			if (typeof(record) != "object") {
				Ext.Msg.alert("提示", "请选择服务!");
				return false;
			}
//			var ro = record.data;
			var object = new Object();
				object = {
			code :record.get('code'),
			name : record.get('name'),
			fee : record.get('fee'),
			serUnit:record.get('serUnit')
			};
			window.returnValue = object;
			window.close();
		}
		// 多选
		else {
			var selectNodes = grid.getSelectionModel().getSelections();
			if (selectNodes.length == 0) {
				Ext.Msg.alert("提示", "请选择服务!");
				return false;
			}
			var ros = new Array();
			var workerCodes = new Array();
			var workerNames = new Array();
			var deptIds = new Array();
			for (var i = 0; i < selectNodes.length; i++) {
				var record = selectNodes[i].data;
				workerCodes.push(record.workerCode);
				workerNames.push(record.workerName);
				deptIds.push(record.deptId);
				ros.push(record);
			}
			window.returnValue = {
				list : ros,
				codes : workerCodes.join(","),
				names : workerNames.join(","),
				deptIds: deptIds.join(",")
			};
			window.close();
		}
	}

	var User = Ext.data.Record.create([{
		name : 'id'
	},{
		name : 'serUnit'
	} ,{
		name : 'code'
	},{
		name : 'name'
	}, {
		name : 'fee'
	}, {
		name : 'type'
	}, {
		name : 'fromCom'
	}]);
	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'workbill/getServiceByType.action'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalCount',
			root : 'list'
		}, User)
	});

	/* 设置每一行的选择框 */
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : isSingleSelect
	});
		ds.on('beforeload', function() {
		Ext.apply(this.baseParams, {
			typeId :deptIdChoose,
			queryKey : Ext.get("queryKey")? Ext.get("queryKey").dom.value : ''
//			notInWorkerCodes : notIn
		});
	});
	ds.load({
		params : {
			start : 0,
			limit : 20
		}
	});
//	ds.on('beforeload', function() {
//		Ext.apply(this.baseParams, {
//			deptId : deptIdChoose,
//			queryKey : Ext.get("queryKey").dom.value,
//			notInWorkerCodes : notIn
//		});
//	});

	var cm = new Ext.grid.ColumnModel([sm, {
		header : '服务编号',
		dataIndex : 'code',
		align : 'left',
		width : 80
	}, {
		header : '服务名称',
		dataIndex : 'name',
		style : 'display="none"',
		align : 'left',
		width : 100
	}, {
		header : '服务类型',
		dataIndex : 'type',
		align : 'left' ,
		width : 100
	}, {
		header : '费用',
		dataIndex : 'fee',
		align : 'left' ,
		width : 80
	}]);
	cm.defaultSortable = true;
	/* 设置分页的工具条 */
	var bbar = new Ext.PagingToolbar({
		pageSize : 20,
		store : ds,
		displayInfo : true,
		displayMsg : '共 {2} 条',
		emptyMsg : "没有记录"
	});
	var grid = new Ext.grid.EditorGridPanel({
		ds : ds, 
		cm : cm,
		sm : sm,
		bbar : bbar,
		tbar : toolbar,
		autoWidth : true,
		autoScroll:true, 
		fitToFrame : true,
		border : false
	});
	grid.enableColumnHide = false;
	if (isSingleSelect) {
		grid.on("rowdblclick", function() {
			chooseWorker();
		});
	}
	var panel = new Ext.Viewport({ 
		layout:'border',
		items : [{
			region:'center',  
			layout:'fit',
			items:[grid]
		},
		{
			region:'west',  
			width:200,
			layout:'fit',
			items:[deptTree]
		}]
	});
	rootNodeObj.expand();
});