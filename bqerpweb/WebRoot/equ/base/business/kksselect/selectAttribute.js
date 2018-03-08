Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	
	
	var method="one";
	var show=true;
	
	if(getParameter("op")!="")
	{
		 method=getParameter("op");
	 
	}
	
	if(method!="one")
	{
		show=false;
	}
	
		//	// -----------定义tree-----------------
	var root = new Ext.tree.AsyncTreeNode({
		id : "root",
		text : "设备树"
	});

	var mytree = new Ext.tree.TreePanel({
		renderTo : "treepanel",
		autoHeight : true,
		root : root,
		animate : true,
		enableDD:true,
        enableDrag:true,
		border : false,
		rootVisible : true,
		containerScroll : true,
		tbar:[{text:'确定',
		       hidden:show,
		       handler:treeselect}],
		// checkModel: "single" ,
	//	requestMethod : 'GET',
		loader : new Ext.tree.TreeLoader({
			url : "equ/getTreeForSelect.action",
			baseParams : {
				id : 'root',
				method:method
			}

		})
	});

	// ---------------------------------------
	 function treeselect()
	 {
	 	if(method=="many")
	 	{
	 	var b = mytree.getChecked();
		var checkid = new Array;
		var checkname=new Array;
		for (var i = 0; i < b.length; i++) {
				checkid.push(b[i].id);
				checkname.push(b[i].text.substring(b[i].text.indexOf(' ') + 1,
										b[i].text.length));
		}
			Ext.Msg.confirm("选择", "是否确定选择'" + checkname + "'？", function(buttonobj) {

			if (buttonobj == "yes") {
		var equ = new Object();
		equ.code=checkid.toString();
		equ.name=checkname.toString();
		window.returnValue = equ;
        window.close();
			}
			})
	 	}
	 	
	 }
	
	//-----------树的事件----------------------
	//树的单击事件
	mytree.on("dblclick", clickTree, this);
	mytree.on('beforeload', function(node){
		// 指定某个节点的子节点
		mytree.loader.dataUrl = 'equ/getTreeForSelect.action?method='+method+'&id=' + node.id;
	}, this);
	
	mytree.on("check",function(node,checked){alert();});
	
	function clickTree(node) {
		Ext.Msg.confirm("选择", "是否确定选择'" + node.text + "'？", function(buttonobj) {

			if (buttonobj == "yes") {
	var equ = new Object();
	equ.code=node.id;
	equ.name=node.text.substring(node.text.indexOf(' ') + 1,
										node.text.length);
							
	  window.returnValue = equ;
      window.close();
			}
		});
	}
	
	root.expand();//展开根节点

	//--------------------------------------


	//-----------设备列表--------------------
	
	var MyRecord = Ext.data.Record.create([
	{name : 'equId'},
    {name : 'equName'},
	{name : 'attributeCode'},
	{name : 'locationCode'},
	{name : 'installationCode'},
	{name : 'assetnum'}
	]);

	var dataProxy = new Ext.data.HttpProxy(

			{
				url:'equ/findEquListByFuzzy.action'
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
				limit : 18			
			}
		});
	


	var fuuzy = new Ext.form.TextField({
		id : "fuzzy",
		name : "fuzzy"
	});
	
	

 
 
	var sm = new Ext.grid.CheckboxSelectionModel({
		hidden:show
	});
  

	var grid = new Ext.grid.GridPanel({
		store : store,
		columns : [
		sm, {
			
			header : "ID",
			width : 75,
			sortable : true,
			dataIndex : 'equId',
			hidden:true
		},
		{
			header : "设备名称",
			width : 300,
			sortable : true,
			dataIndex : 'equName'
		},
		{
			header : "设备功能码",
			width : 150,
			sortable : true,
			dataIndex : 'attributeCode'
		},
		{
			header : "设备位置码",
			width : 150,
			sortable : true,
			dataIndex : 'locationCode'
		},
		{
			header : "安装点码",
			width : 150,
			sortable : true,
			dataIndex : 'installationCode'
		},
		{
			header : "物资码",
			width : 150,
			sortable : true,
			dataIndex : 'assetnum'
		}
		],
		sm : sm,
		title : '设备列表',
		tbar : [fuuzy, {
			text : "查询",
			handler : queryRecord
		},{
			text:'确定',
			id:'gridadd',
			hidden:show,
			handler:selectMany
		
		}],
		//分页
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : store,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		})
	});
	
	grid.on("dblclick", selectOneRecord);
  
//------------------	
	
		// 查询
	function queryRecord() {
		var fuzzytext = fuuzy.getValue();
		store.baseParams = {
			fuzzy : fuzzytext
		};
		store.load({
			params : {
				start : 0,
				limit : 18
			}
		});
	}
	
	function selectOneRecord(){

			if (grid.selModel.hasSelection()) {
		
			var records = grid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请先选择一个设备！");
			} else {
		
				var record = grid.getSelectionModel().getSelected();
				Ext.Msg.confirm("选择", "是否确定选择'" + record.get("equName") + "'？", function(buttonobj) {

			if (buttonobj == "yes") {
				     var equ = new Object();
				     equ.id=record.get("equId");
				     equ.code=record.get("attributeCode");
				     equ.name=record.get("equName");
				    
					 window.returnValue = equ;
                     window.close();
			}
				})
			}
		} else {
			Ext.Msg.alert("提示", "请先选择设备!");
		}
	}
	
	function selectMany() {
		if (method == "many") {
			var mysm = grid.getSelectionModel();
			var selected = mysm.getSelections();
			var ids = [];
			var names=[];
			var codes=[];
			if (selected.length == 0) {
				Ext.Msg.alert("提示", "请选择设备！");
			} else {

				for (var i = 0; i < selected.length; i += 1) {
					var member = selected[i].data;
					if (member.equId) {
						ids.push(member.equId);
						names.push(member.equName);
						codes.push(member.attributeCode);
					} else {

						store.remove(store.getAt(i));
					}
				
				}
				Ext.Msg.confirm("选择", "是否确定选择'" + names + "'？", function(buttonobj) {

			if (buttonobj == "yes") {
				 var equ = new Object();
				 equ.id=ids.toString();
				 equ.code=codes.toString();
				 equ.name=names.toString();

				window.returnValue = equ;
				window.close();
			}
				})

			}
		}

	}
	
	
	
	// -------------------------------------
	
//  var panelleft = new Ext.Panel({
//		layout : 'fit',
//		title : '树形方式查询',
//		collapsible : true,
//		items : [mytree]
//	});
//
//	var panel2 = new Ext.Panel({
//		id : 'tab2',
//		layout : 'fit',
//		title : '列表方式查询',
//		items : [grid]
//	});
//
//
//	var tabpanel = new Ext.TabPanel({
//		title : 'mytab',
//	//	layout : 'fit',
//		activeTab : 0,
//		autoScroll : true,
//		items : [panelleft, panel2]
//
//	});
//	
//	new Ext.Viewport({
//		enableTabScroll : true,
//		layout : "fit",
//		items : [tabpanel]
//	});
	

var viewport = new Ext.Viewport({
				layout : 'fit',
				items : [ {
							region : 'center',
							layout : 'fit',
							margins : '0 0 0 0',
							collapsible : true,
							border : false,
							items : [new Ext.TabPanel({
										activeTab : 0,
										layoutOnTabChange : true,
										items:[{
													title : '树形方式',
													border : false,
													autoScroll : true,
													items : [mytree]
												},{title : '列表方式',
												    border : false,
													autoScroll : true,
													layout : 'border',
													items : [{
													region : 'center',
													layout : 'fit',
													items : [grid]
												}]
												}]
							})] 
						}
				]
			});
	

});