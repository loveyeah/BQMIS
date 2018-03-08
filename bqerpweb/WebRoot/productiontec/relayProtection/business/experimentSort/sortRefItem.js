Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
var sortId="";
	
	// 定义grid
var MyRecord = Ext.data.Record.create([
	{name : 'sylbId'},
    {name : 'sylbName'},
	{name : 'displayNo'}
	]);

	var dataProxy = new Ext.data.HttpProxy(

			{
				url:'productionrec/findExperimentSortList.action'
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
		
	var sm = new Ext.grid.CheckboxSelectionModel({
	singleSelect : true
	});

	var grid = new Ext.grid.GridPanel({
		region : "center",
		layout : 'fit',
		store : store,

		columns : [
		sm, new Ext.grid.RowNumberer({header:'序号',width : 50}),{
			
			header : "ID",
			width : 75,
			sortable : true,
			dataIndex : 'sylbId',
			hidden:true
		},
		{
			header : "类别名称",
			width : 75,
			sortable : true,
			dataIndex : 'sylbName'
		},
		{
			header : "显示顺序",
			width : 75,
			sortable : true,
			dataIndex : 'displayNo'
		}
		],
		sm : sm,
		autoSizeColumns : true,
		viewConfig : {
			forceFit : true
		},
		//分页
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : store,
			displayInfo : true,
			displayMsg : "一共 {2} 条",
			emptyMsg : "没有记录"
		})
	});

grid.on("rowclick", queryRecord);
//---------------------------------------
Ext.grid.CheckColumn = function(config) {
	    Ext.apply(this, config);
	    if (!this.id) {
	        this.id = Ext.id();
	    }
	    this.renderer = this.renderer.createDelegate(this);
	};
Ext.grid.CheckColumn.prototype = {
	    init : function(grid) {
	        this.grid = grid;
	        this.grid.on('render', function() {
		            var view = this.grid.getView();
		            view.mainBody.on('mousedown', this.onMouseDown, this);
		        }, this);
	    },

	
	    onMouseDown : function(e, t) {
	        if (t.className && t.className.indexOf('x-grid3-cc-' + this.id) != -1) {
	            e.stopEvent();
	            var index = this.grid.getView().findRowIndex(t);
	            var record = this.grid.store.getAt(index);
	            if (record.data[this.dataIndex] == "Y") {	            	
	                record.set(this.dataIndex, "N");
	            } else {
	                record.set(this.dataIndex, "Y");
	            }
	        }
	    },
	    renderer : function(v, p, record) {
	        p.css += ' x-grid3-check-col-td';
	    	p.css += ' x-item-disabled';
	        return '<div class="x-grid3-check-col' + (v == 'Y' ? '-on' : '')
	                + ' x-grid3-cc-' + this.id + '">&#160;</div>';
	    }
	}
 var checkColumn = new Ext.grid.CheckColumn({
        header : "类型选择",
        dataIndex : 'flag',
        width : 60
    });

		// 定义对应项目grid
var rightMyRecord = Ext.data.Record.create([
	{name : 'syxmId'},
    {name : 'syxmName'},
	{name : 'flag'}
	]);

	var rightDataProxy = new Ext.data.HttpProxy(

			{
				url:'productionrec/findExperimentSortRefItemList.action'
			}

	);
	var rightTheReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"

	}, rightMyRecord);

	var rightStore = new Ext.data.Store({

		proxy : rightDataProxy,

		reader : rightTheReader

	});

	var rightSm = new Ext.grid.CheckboxSelectionModel();

	var rightGrid = new Ext.grid.EditorGridPanel({
		region : "center",
		layout : 'fit',
		store : rightStore,
         plugins:[checkColumn],
		columns : [
		 new Ext.grid.RowNumberer({header:'序号',width : 50}),{
			
			header : "ID",
			width : 75,
			sortable : true,
			dataIndex : 'syxmId',
			hidden:true
		},
		{
			header : "试验项目名称",
			width : 75,
			sortable : true,
			dataIndex : 'syxmName'
		},checkColumn
		],
		sm : rightSm,
		autoSizeColumns : true,
		viewConfig : {
			forceFit : true
		},
		//分页
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : rightStore,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		})
	});
	rightStore.getCount()
	function queryRecord()
	{
		if (grid.selModel.hasSelection()) {
			
		
		var record = grid.getSelectionModel().getSelected();
		sortId=record.get("sylbId");
		rightStore.load({
			params : {
				sortId:sortId,
				start : 0,
				limit : 18				
			}
		});
		
		}else{
		 Ext.Msg.alert("提示", "请选择一条记录!");
		 rightStore.removeAll();
		}
	}
	var headBar=new Ext.Toolbar({
	  id:'headBar',
	  items:[{
	   text:'保存',
	   iconCls:'save',
	   handler:function(){
	   	if(sortId=="")
	   	{
	   		Ext.Msg.alert("提示","请选择实验类别！");
	   		return ;
	   	}
	   	var strSelectIds = "";
	   	var strNoselectIds="";
	  for(var i=0;i<rightStore.getCount();i++)
	  {
	  	if(rightStore.getAt(i).get("flag")=="Y")
	  	{
	  		if(strSelectIds=="") strSelectIds=rightStore.getAt(i).get("syxmId");
	  		else strSelectIds+=","+rightStore.getAt(i).get("syxmId");
	  	}
	  	else
	  	{
	  		if(strNoselectIds=="") strNoselectIds=rightStore.getAt(i).get("syxmId");
	  		else strNoselectIds+=","+rightStore.getAt(i).get("syxmId");
	  	}
	  }
	 
	   Ext.Ajax.request({
         url:'productionrec/saveExperimentSortRefItemInfo.action',
         method:'post',
         params:{
          selectIds:strSelectIds,
          noselectIds:strNoselectIds,
          sortId:sortId
           },
         success:function(result){
          var obj = eval('(' + result.responseText + ')');
          Ext.Msg.alert("提示","保存成功！");
          queryRecord();
          },
		 failure : function(action) {
		 	
				}
          });
	   	
	   }
	  }]
	});

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		items : [
		{
			region : 'north',
			layout : 'fit',
			items:[headBar]
		},
		{
			region:'west',
			layout : 'fit',
			width : 400,
			split : true,
			items:[grid]
		},
		{
			region:'center',
			layout : 'fit',
			items:[rightGrid]
		}
		]
	});

	
	
	
	
	
});