Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	// var currentType = 'add';
	var currentNode = new Object();
	var currentReasonId = "0";
	currentNode.id = "0";
	currentNode.text = "故障树";
	// -------------------------

	var wd = 180;

	var bugId = {
		id : "bugId",
		xtype : "textfield",
		fieldLabel : 'ID',
		value : '自动生成',
		width : wd,
		readOnly : true,
		name : 'bugId'

	}
	var PBugId = {
		id : "PBugId",
		xtype : "textfield",
		fieldLabel : '父ID',
		allowBlank : false,
		readOnly : true,
		width : wd,
		name : 'equCBug.PBugId'

	}

	var bugCode = new Ext.form.TextField({
		id : "bugCode",
		xtype : "textfield",
		fieldLabel : '故障码',
		name : 'equCBug.bugCode',
		width : wd,
		allowBlank : false,
		readOnly : true
	});

	var bugName = {
		id : "bugName",
		xtype : "textfield",
		fieldLabel : '故障名称',
		name : 'equCBug.bugName',
		width : wd,
		allowBlank : false,
		readOnly : true
	}

	var bugDesc = {
		id : "bugDesc",
		xtype : "textarea",
		fieldLabel : '故障现象描述',
		name : 'equCBug.bugDesc',
		anchor : '95%',
		height:80,
		allowBlank : false,
		readOnly : true
	}

	var ifLeaf = new Ext.form.ComboBox({
		fieldLabel : '是否叶子',
		store : new Ext.data.SimpleStore({
			fields : ["retrunValue", "displayText"],
			data : [['0', '否'], ['1', '是']]
		}),
		valueField : "retrunValue",
		displayField : "displayText",
		mode : 'local',
		forceSelection : true,
		blankText : '请选择是否叶子',
		emptyText : '选择是否叶子',
		hiddenName : 'equCBug.ifLeaf',
		value : '0',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : false,
		name : 'equCBug.ifLeaf',
		width : wd,
		readOnly : true
			// anchor : '50%'
	});

	var entryBy = {
		id : "entryBy",
		xtype : "textfield",
		fieldLabel : '填写人',
		width : wd,
		name : 'equCBug.entryBy',
		readOnly : true
	}

	var entryDate = {
		id : 'entryDate',
		xtype : "datefield",
		fieldLabel : '填写时间',
		name : 'equCBug.entryDate',
		format : 'Y-m-d',
		width : wd,
		readOnly : true

	};

	var myaddpanel = new Ext.FormPanel({
		frame : true,
		labelAlign : 'center',
		labelWidth:80,
		title : '增加/修改故障信息',
		items : [{
			layout : 'column',
			items : [{
				columnWidth : 0.5,
				layout : 'form',
				items : [bugId, bugCode, entryBy,ifLeaf]
			}, {
				columnWidth : 0.5,
				layout : 'form',
				items : [PBugId, bugName, entryDate]
			}]
		},{
			layout : 'column',
			items : [{
				columnWidth : 1,
				layout : 'form',
				items : [bugDesc]
			}]
		}]
			// items : [bugId,pBugId,bugName,bugDesc,ifLeaf,entryBy,entryDate]

	});

	var win = new Ext.Window({
		width : 600,
		height : 300,
		buttonAlign : "center",
		items : [myaddpanel],
		layout : 'fit',
		closeAction : 'hide',
		buttons : [{
			text : '关闭',
			handler : function() {
				win.hide();
			}
		}]

	});
	// -------------------------

	// // -----------定义tree-----------------
	var root = new Ext.tree.AsyncTreeNode({
		id : "root",
		text : "故障树"
	});

	var mytree = new Ext.tree.TreePanel({
	renderTo : "treepanel",
		autoHeight : true,
		root : root,
		animate : true,
		enableDD : false,
		border : false,
		rootVisible : true,
		containerScroll : true,
		loader : new Ext.tree.TreeLoader({
			url : "equbug/getBugTreeByCode.action",
			baseParams : {
				id : 'root'
			}

		})
	});
	// --------tree的事件-----------------
	mytree.on("click", clickTree, this);
	mytree.on("dblclick", dbclickTree, this);
	mytree.on('beforeload', function(node) {
		mytree.loader.dataUrl = 'equbug/getBugTreeByCode.action?id=' + node.id;
	}, this);
	
	function  dbclickTree(node)
	{
		Ext.Msg.confirm("选择", "是否确定选择" + node.text + "？", function(buttonobj) {

			if (buttonobj == "yes") {
					var bug = new Object();
				if (node.id != "0") {
					bug.code = node.id;
					bug.name = node.text;
						Ext.Ajax.request({
						url : 'equbug/getReasonStringByCode.action',
						params : {
							bugCode : node.id
						},
						method : 'post',
						waitMsg : '正在加载数据...',
						success : function(result, request) {
							var json =result.responseText;
						
							if(json.indexOf('(')>0)
							{
							bug.reason=json.substring(json.indexOf('('),json.length-2);
							
							}
							else
							{
								bug.reason="";
							}
							
							window.returnValue = bug;
				            window.close();
						},
						failure : function(result, request) {
							Ext.MessageBox.alert('错误', '操作失败!');
						}
					});
				
				} else {
					bug.code = "";
					bug.name = "";
					bug.reason="";
					window.returnValue = bug;
				    window.close();
				}
				
				
			}
		});
	}

	function clickTree(node) {
		gridreason.setTitle(node.text + "---对应的故障原因");
		currentNode = node;
		currentReasonId = "0";

		reasonStore.baseParams = {
			bugCode : node.id,
			fuzzy : '%'
		};
		reasonStore.load({
			params : {
				start : 0,
				limit : 10
			}
		});
		gridsolution.setTitle("解决方案");
		querySolution();
		// alert(node.id);

	};
	root.expand();// 展开根节点
	// 为树加右键菜单
	mytree.on('contextmenu', function(node, e) {
		e.stopEvent();

		var menu = new Ext.menu.Menu({
			id : 'mainMenu',
			items : [ new Ext.menu.Item({
				text : '查看',
				style : 'display:' + (node.id == "root" ? 'none' : ''),
				iconCls : 'query',
				handler : function() {
					myaddpanel.getForm().reset();
					win.show();
					myaddpanel.setTitle("查看'" + node.text + "'基本信息");
					
					Ext.get("bugId").dom.value = node.id;
					Ext.Ajax.request({
						url : 'equbug/findBugByCode.action',
						params : {
							code : node.id
						},
						method : 'post',
						waitMsg : '正在加载数据...',
						success : function(result, request) {
							var json = eval('(' + result.responseText + ')');

							var obj = json.data;
							Ext.get("bugCode").dom.value = obj.bugCode;
							Ext.get("bugId").dom.value = obj.bugId;
							Ext.get("bugDesc").dom.value = obj.bugDesc;
							Ext.get("PBugId").dom.value = obj.PBugId;
							Ext.get("bugName").dom.value = obj.bugName;
							ifLeaf.setValue(obj.ifLeaf);
							if (obj.entryBy != null) {
								Ext.get("entryBy").dom.value = json.workName;
							}
							if (obj.entryDate != null) {
								Ext.get("entryDate").dom.value = obj.entryDate
										.substring(0, 10);
							}

						},
						failure : function(result, request) {
							Ext.MessageBox.alert('错误', '操作失败!');
						}
					});

				}
			})]
		});
		var coords = e.getXY();
		menu.showAt([coords[0], coords[1]]);
	}, this);

	// --------------------------------
	// 定义grid 故障原因
	var MyRecord = Ext.data.Record.create([{
		name : 'bugReasonId'
	}, {
		name : 'bugCode'
	}, {
		name : 'bugReasonDesc'
	}]);

	var dataProxy = new Ext.data.HttpProxy(

			{
				url : 'equbug/findBugReaListByCode.action'
			}

	);

	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"

	}, MyRecord);

	var reasonStore = new Ext.data.Store({

		proxy : dataProxy,

		reader : theReader

	});

	var fuuzy = new Ext.form.TextField({
		id : "fuzzy",
		name : "fuzzy"
	});
	var sm = new Ext.grid.CheckboxSelectionModel();

	var gridreason = new Ext.grid.GridPanel({
		store : reasonStore,
		height : 200,
		columns : [// 设计页面显示的表格信息，字段与Ext.data.Record.create匹配
		sm, new Ext.grid.RowNumberer({width:20,align : 'center'}),
			{
			header : "ID",
			sortable : true,
			dataIndex : 'bugReasonId',
			hidden:true

		},

		{
			header : "故障编码",
			sortable : true,
			dataIndex : 'bugCode',
			hidden : true,
			align : 'center'
		},

		{
			header : "原因",
			width : 450,
			sortable : true,
			dataIndex : 'bugReasonDesc',
			align : 'center'
		}],
		sm : sm,
		title : '故障原因',
		tbar : [fuuzy, {
			text : "查询",
			handler : queryreason
		}],
		// 分页
		bbar : new Ext.PagingToolbar({
			pageSize : 10,
			store : reasonStore,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		})
	});

	gridreason.addListener('rowClick', rowClick);
	function rowClick(grid, rowIndex, e) {
		var record = grid.getStore().getAt(rowIndex);
		var reasonId = record.get("bugReasonId");
		var reasonDesc = record.get("bugReasonDesc");
		currentReasonId = reasonId;
		gridsolution.setTitle(reasonDesc + "---对应的解决方案");
		solutionstore.baseParams = {
			reasonId : reasonId,
			fuzzy : '%'
		};
		solutionstore.load({
			params : {
				start : 0,
				limit : 10
			}
		});

	}

	function queryreason() {
		var fuzzytext = fuuzy.getValue();
		reasonStore.baseParams = {
			fuzzy : fuzzytext,
			bugCode : currentNode.id
		};
		reasonStore.load({
			params : {
				start : 0,
				limit : 10
			}
		});
	}


	// -------------故障解决方案--------------
	// 定义grid 故障解决方案
	var MyRecord1 = Ext.data.Record.create([{
		name : 'equSolutionId'
	}, {
		name : 'equSolutionDesc'
	}, {
		name : 'memo'
	}, {
		name : 'fileName'
	}, {
		name : 'fileView'
	}]);

	var dataProxy1 = new Ext.data.HttpProxy(

			{
				url : 'equbug/findBugSolutionList.action'
			}

	);

	var theReader1 = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"

	}, MyRecord1);

	var solutionstore = new Ext.data.Store({

		proxy : dataProxy1,

		reader : theReader1

	});

	var solutionFuzzy = new Ext.form.TextField({
		id : "solutionFuzzy",
		name : "solutionFuzzy"
	});
	var mysm = new Ext.grid.CheckboxSelectionModel();

	var gridsolution = new Ext.grid.GridPanel({
		store : solutionstore,
		columns : [mysm, new Ext.grid.RowNumberer({width:20,align : 'center'}),{

			header : "ID",
			sortable : true,
			dataIndex : 'equSolutionId',
			hidden : true
		}, {
			header : "解决方案描述",
			width : 280,
			sortable : true,
			dataIndex : 'equSolutionDesc'
		},

		{
			header : "备注",
			width : 100,
			sortable : true,
			dataIndex : 'memo'
		}, {
			header : "附件",
			sortable : true,
			dataIndex : 'fileName',
			hidden : true
		}, {
			header : "附件",
			width : 75,
			sortable : true,
			dataIndex : 'fileView'
		}],
		sm : mysm,
		title : '故障解决方案',

		tbar : [solutionFuzzy, {
			text : "查询",
			handler : querySolution
		}],
		// 分页
		bbar : new Ext.PagingToolbar({
			pageSize : 10,
			store : solutionstore,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		})
	});

	function querySolution() {

		solutionstore.baseParams = {
			reasonId : currentReasonId,
			fuzzy : solutionFuzzy.getValue()
		};
		solutionstore.load({
			params : {
				start : 0,
				limit : 10
			}
		});

	}
	// ------------------------------------
		//-------------add 故障列表----------------
	var MyRecord = Ext.data.Record.create([
	{name : 'bugId'},
    {name : 'bugCode'},
	{name : 'bugName'}
	]);

	var dataProxy = new Ext.data.HttpProxy(

			{
				url:'equbug/findBugListByName.action'
			}

	);

	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"

	}, MyRecord);

	var store = new Ext.data.Store({

		proxy : dataProxy,

		reader : theReader

	});
//分页
	store.load({
			params : {
				start : 0,
				limit : 12		
			}
		});
	

	var sm = new Ext.grid.CheckboxSelectionModel();
   var bugFuzzy = new Ext.form.TextField({
		id : "bugFuzzy",
		name : "bugFuzzy"
	});
	var grid = new Ext.grid.GridPanel({
		store : store,
		columns : [
		sm, {
			
			header : "ID",
			sortable : true,
			dataIndex : 'bugId',
			hidden:true
		},

		{
			header : "故障编码",
			sortable : true,
			dataIndex : 'bugCode',
			align:'center',
			hidden:true
		},

		{
			header : "故障名称",
			width : 200,
			sortable : true,
			dataIndex : 'bugName',
			align:'center'
		}
		],
		sm : sm,
		tbar : [
		bugFuzzy, {
			text : "查询",
			 iconCls : 'query',
			handler : queryRecord
		}, {
			text : "查看",
			 iconCls : 'query',
			handler : updateRecord
		}],	
		bbar : new Ext.PagingToolbar({
			pageSize : 12,
			store : store,
			displayInfo : true,
			displayMsg : "",
			emptyMsg : ""
		})
	});
	grid.on("dblclick", selectRecord);
	grid.addListener('rowClick', gridrowClick);
	function gridrowClick(grid, rowIndex, e) {
			var record = grid.getStore().getAt(rowIndex);
		var bugCode = record.get("bugCode");
		var bugName = record.get("bugName");
	gridreason.setTitle(bugName + "---对应的故障原因");
		currentNode.id = bugCode;
		currentNode.text=bugName;
		currentReasonId = "0";

		reasonStore.baseParams = {
			bugCode : bugCode,
			fuzzy : '%'
		};
		reasonStore.load({
			params : {
				start : 0,
				limit : 10
			}
		});
		gridsolution.setTitle("解决方案");
		querySolution();
	}

	function queryRecord() {
		var fuzzytext = bugFuzzy.getValue();
		store.baseParams = {
			fuzzy : fuzzytext
		};
		store.load({
			params : {
				start : 0,
				limit : 12
			}
		});
	}
   function 	selectRecord()
	{
		if (grid.selModel.hasSelection()) {
		
			var records = grid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑！");
			} else {
				var record = grid.getSelectionModel().getSelected();
			var code= record.get("bugCode");
			   var name= record.get("bugName");
			Ext.Msg.confirm("选择", "是否确定选择" + name + "？", function(buttonobj) {

			if (buttonobj == "yes") {
				var bug = new Object();
				if (code != "0") {
					bug.code = code;
					bug.name = name;
					Ext.Ajax.request({
						url : 'equbug/getReasonStringByCode.action',
						params : {
							bugCode : code
						},
						method : 'post',
						waitMsg : '正在加载数据...',
						success : function(result, request) {
							var json =result.responseText;
							if(json.indexOf('(')>0)
							{
							bug.reason=json.substring(json.indexOf('('),json.length-2);
							
							}
							else
							{
								bug.reason="";
							}
						
							window.returnValue = bug;
				            window.close();
						},
						failure : function(result, request) {
							Ext.MessageBox.alert('错误', '操作失败!');
						}
					});
				} else {
					bug.code = "";
					bug.name = "";
					window.returnValue = bug;
				window.close();
				}
				
			}
		});
				
			}
		} else {
			Ext.Msg.alert("提示", "请先选择故障!");
		}
	}
	
		function updateRecord()
	{
			if (grid.selModel.hasSelection()) {
		
			var records = grid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑！");
			} else {
				var record = grid.getSelectionModel().getSelected();
			      currentNode.id=record.get("bugCode");
			      currentNode.text=record.get("bugName");
					myaddpanel.getForm().reset();
					win.show();
					myaddpanel.setTitle("查看'" + currentNode.text + "'基本信息");
					
					Ext.get("bugId").dom.value = currentNode.id;
					Ext.Ajax.request({
						url : 'equbug/findBugByCode.action',
						params : {
							code : currentNode.id
						},
						method : 'post',
						waitMsg : '正在加载数据...',
						success : function(result, request) {
							var json = eval('(' + result.responseText + ')');

							var obj = json.data;
							Ext.get("bugCode").dom.value = obj.bugCode;
							Ext.get("bugId").dom.value = obj.bugId;
							Ext.get("bugDesc").dom.value = obj.bugDesc;
							Ext.get("PBugId").dom.value = obj.PBugId;
							Ext.get("bugName").dom.value = obj.bugName;
							ifLeaf.setValue(obj.ifLeaf);
							if (obj.entryBy != null) {
								Ext.get("entryBy").dom.value = json.workName;
							}
							if (obj.entryDate != null) {
								Ext.get("entryDate").dom.value = obj.entryDate
										.substring(0, 10);
							}

						},
						failure : function(result, request) {
							Ext.MessageBox.alert('错误', '操作失败!');
						}
					});
			}
		} else {
			Ext.Msg.alert("提示", "请先选择要编辑的行!");
		}
		
		
			        
	}
	// ----------布局---------------------

	
	var right = new Ext.Panel({
		region : "center",
		layout : 'border',
		items : [{
			region : 'north',
			height : 200,
			items : [gridreason]
		}, {
			region : 'center',
			layout : 'fit',
			items : [gridsolution]
		}]

	});
	
	
	
	var viewport = new Ext.Viewport({
				layout : 'border',
				items : [ {
							region : 'west',
							split : true,
							width : 250,
							layout : 'fit',
							minSize : 175,
							maxSize : 600,
							margins : '0 0 0 0',
							collapsible : true,
							border : false,
							//autoScroll : true,
							items : [new Ext.TabPanel({
							tabPosition : 'bottom',
										activeTab : 0,
										layoutOnTabChange : true,
										items:[{title : '列表方式',
												    border : false,
													autoScroll : true,
													layout : 'border',
													items : [{
													region : 'center',
													layout : 'fit',
													items : [grid]
												}]
												},{
													title : '树形方式',
													border : false,
													autoScroll : true,
													items : [mytree]
												}]
							})] 
						}, right// 初始标签页
				]
			});
			

});
