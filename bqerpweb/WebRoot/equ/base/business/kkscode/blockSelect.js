Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	
	// 定义grid
var MyRecord = Ext.data.Record.create([
	{name : 'id'},
    {name : 'blockCode'},
	{name : 'blockName'}
	]);

	var dataProxy = new Ext.data.HttpProxy(

			{
				url:'equ/getblockList.action'
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
				limit : 10				
			}
		});
	



	var fuuzy = new Ext.form.TextField({
		id : "fuzzy",
		name : "fuzzy"
	});
	//var sm = new Ext.grid.CheckboxSelectionModel();

	var grid = new Ext.grid.GridPanel({
		region : "center",
		layout : 'fit',
		// region:"east",
		store : store,

		columns : [// 设计页面显示的表格信息，字段与Ext.data.Record.create匹配
		//sm, 
			{
			
			header : "ID",
			width : 75,
			sortable : true,
			dataIndex : 'id',
			hidden:true
		},

		{
			header : "机组编码",
			width : 75,
			sortable : true,
			dataIndex : 'blockCode'
		},

		{
			header : "机组名称",
			width : 75,
			sortable : true,
			dataIndex : 'blockName'
		}
		],
		//sm : sm,
		autoSizeColumns : true,
		viewConfig : {
			forceFit : true
		},

		
//
//	height : 350,
//
//		width : 300,

		title : '机组信息管理',
		
		tbar : [fuuzy, {
			text : "查询",
			handler : queryRecord
		}],
		//分页
		bbar : new Ext.PagingToolbar({
			pageSize : 10,
			store : store,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		})
	});

grid.on("dblclick", getRecord);
//---------------------------------------

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
	//	layout : "fit",
		items : [grid]
	});

	// -------------------

	// 查询
	function queryRecord() {
		var fuzzytext = fuuzy.getValue();
		store.baseParams = {
			fuzzy : fuzzytext
		};
		store.load({
			params : {
				start : 0,
				limit : 10
			}
		});
	}
	
	function getRecord()
	{
		var record = grid.getSelectionModel().getSelected();
	   //alert(record.get("id"));
		  var obj=new Object();
		      obj.code=record.get("blockCode");
		      obj.name=record.get("blockName");
	    window.returnValue = obj;
        window.close();
	}
	


});