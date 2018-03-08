Ext.BLANK_IMAGE_URL='comm/ext/resources/images/default/s.gif';
Ext.onReady(function () {
	//add by ypan 20100728
		var queryDeptId = new Ext.ux.ComboBoxTree({
			fieldLabel : '所属部门',
			id : 'deptId',
			displayField : 'text',
			valueField : 'id',
			hiddenName : 'empinfo.deptId',
			blankText : '请选择',
			emptyText : '请选择', 
			resizable:true,
			width : 250,
			// value:{id:'0',text:'合肥电厂',attributes:{description:'deptName'}},
			tree : {
				xtype : 'treepanel',
				autoScroll : false,
				loader : new Ext.tree.TreeLoader({
					dataUrl : 'empInfoManage.action?method=getDep&flag=roleQuery'
				}),
				root : new Ext.tree.AsyncTreeNode({
					id : '0',
					//name : '灞桥热电厂',
					text : '灞桥热电厂'
				})
			},
			selectNodeModel : 'all'
		})
	
	//查询
	function queryRecord(){
		store.baseParams = {
			deptId : queryDeptId.getValue()
		};
		store.load({
			params : {
				start : 0,
				limit : 18
			}
		});
	}
	function queryDeptId() {};
	function empChangeRecord (){};
	function save () {};
	//定义tbar
	var headerTbar = new Ext.Toolbar({
		items : ['部门：',queryDeptId,{
			text : "查询",
			iconCls : 'query',
			handler : queryRecord},{
		    text : '员工更新',
		    iconCls : 'add',
		    handler : empChangeRecord},{
		    text : '保存',
		    iconCls : 'add',
		    handler : save
		    },'->']
		
	});
	//定义head form
	var headForm =  new Ext.form.FormPanel({
		height : 28,
		frame : false,
		//fileUpload : true,
		layout : 'border',
		items : [{
			bodyStyle : "padding: 1,1,1,0",
			//region : "center",
			border : true,
			frame : false,
			layout : 'form',
			items : [headerTbar]
		}]
	});
	
	//定义record
	var MyRecord = Ext.data.Record.create([
		{
			name : 'deptName',
			mapping :　0
		},{
			name : 'bzName',
			mapping : 1
		},{
			name : 'empCode',
			mapping : 2
		},{
			name : 'empName',
			mapping : 3
		},{
			name : 'hisDay',
			mapping : 4
		},{
			name : 'leftDay',
			mapping : 5
		}
	]);
	//定义reader
	var theReader = new Ext.data.JsonReader({
				root :　"list",
				totalProperty : "totalCount"
	},MyRecord);
	//定义proxy
	var dataProxy = new Ext.data.HttpProxy({
				url :　'ca/queryLeftDay.action'
	});
	//定义store
	var store = new Ext.data.Store({
				proxy : dataProxy,
				reader : theReader
	});
	var sm = new Ext.grid.CheckboxSelectionModel({
	//	singleSelect : true
	})
	//定义grid
	var grid = new Ext.grid.GridPanel({
		region : "center",
		store : store,
		columns : [sm,new Ext.grid.RowNumberer({
			header : '序号',
			width : 35,
			align : 'left'
		}),{
			header : '所属部门',
			width : 120,
			sortable : true,
			align : 'left',
			dataIndex : 'deptName'
		},{
			header : '班组',
			width : 120,
			sortable : true,
			align : 'left',
			dataIndex : 'bzName'
		},{
			header : '工号',
			width : 120,
			sortable : true,
			align : 'left',
			dataIndex : 'empCode'
		},{
			header : '员工姓名',
			width : 120,
			sortable : true,
			align : 'left',
			dataIndex : 'empName'
		},{
			header : '历史换休天数统计',
			width : 140,
			sortabe : true,
			align : 'left',
			dataIndex : 'hisDay'
		},{
			header : '结存换休天数',
			width : 120,
			sortable : false,
			dataIndex : 'leftDay'
		}],
		sm : sm,
		tbar : headForm,
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : store,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : '没有记录'
		})
	});
	
	//定义显示viewport
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		items : [grid]
	});
});