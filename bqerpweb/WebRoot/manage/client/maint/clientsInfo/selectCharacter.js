Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	// 定义grid
	var MyRecord = Ext.data.Record.create([{
		name : 'characterId'
	}, {
		name : 'characterName'
	}, {
		name : 'memo'
	}, {
		name : 'enterpriseCode'
	}]);

	var dataProxy = new Ext.data.HttpProxy({
		url : 'clients/getClientsCharacterList.action'
	});

	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	}, MyRecord);

	var store = new Ext.data.Store({
		proxy : dataProxy,
		reader : theReader
	});
     
	var fuzzy = new Ext.form.TextField({
		id : "fuzzy",
		name : "fuzzy"
	});
    
	var grid = new Ext.grid.GridPanel({
		region : "center",
		layout : 'fit',
		store : store,
		columns : [new Ext.grid.RowNumberer(), {
				header : "性质Id",
				width : 75,
				sortable : true,
				hidden : true,
				dataIndex : 'characterId'
			}, {
				header : "合作伙伴性质名称",
				width : 75,
				sortable : true,
				dataIndex : 'characterName'
	//			hidden : true
			}, {
				header : "备注",
				width : 75,
				sortable : true,
				dataIndex : 'memo'
	//			hidden : true
			}],
		stripeRows : true,
		autoSizeColumns : true,
		autoScroll : true,
		enableColumnMove : false,
		viewConfig : {
			forceFit : true
		},

		tbar : ["合作伙伴类型名称查询：",fuzzy,{
			text : "查询",
			handler : queryRecord
		}],
	
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : store,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		})
	});

	grid.on("rowdblclick", updateRecord);
	// ---------------------------------------

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		// layout : "fit",
		items : [grid]
	});
    
	function queryRecord() {
		store.load({
			params : {
				start : 0,
				limit : 18,
				fuzzytext : fuzzy.getValue()
			}
		});
	}
	
	function updateRecord() {
        if (grid.selModel.hasSelection()) {
            var records = grid.selModel.getSelections();
            var recordslen = records.length;
            if (recordslen > 1) {
                Ext.Msg.alert("系统提示信息", "请选择其中一项！");
            } else {
                var record = grid.getSelectionModel().getSelected();
                var ro = record.data;
				window.returnValue = ro;
				window.close();
            }
        } else {
           Ext.Msg.alert("提示", "请先选择一项!");
        }
    }

	store.load({
		params : {
			start : 0,
			limit : 18
		}
	});
	
});