Ext.onReady(function() {
	var inlist = new ProductApply.InOrOutMaint("I");
	var outlist = new ProductApply.InOrOutMaint("O");
	//var ttType;
	var tabs = new Ext.TabPanel({
		activeTab : 0,
		tabPosition : 'bottom',
		plain : true,
		defaults : {
			autoScroll : true
		},
		items : [{
			id : 'inApply',
			title : '投入申请单',
			autoScroll : true,
			items : inlist.grid,
			layout : 'fit',
			listeners:{
                            //deactivate:function(a){alert("删除,a表示当前标签页");},
                            activate:function(){ttType = 1;}
                      }

		}, {
			id : 'outApply',
			title : '退出申请单',
			 items : [outlist.grid],
			autoScroll : true,
			layout : 'fit',
			listeners:{
                            //deactivate:function(a){alert("删除,a表示当前标签页");},
                            activate:function(){ttType = 2;}
                      }
		}]
	});
	var view = new Ext.Viewport({
		layout : 'fit',
		items : [tabs]
	});
	
})