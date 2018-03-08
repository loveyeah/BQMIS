Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	var laborClass = new LaborProduct.laborClass();
	var laborMaterial = new LaborProduct.laborMaterial();
	var tabs = new Ext.TabPanel({
				activeTab : 0,
				tabPosition : 'bottom',
				plain : true,
				defaults : {
					autoScroll : true
				},
				items : [{
							id : 'laborClassMaint',
							title : '劳保用品分类维护',
							autoScroll : true,
							items : laborClass.grid,
							layout : 'fit'
						}, {
							id : 'laborMaterialMaint',
							title : '劳保用品维护',
							items : laborMaterial.grid,
							autoScroll : true,
							layout : 'fit'
						}]
			});

	var view = new Ext.Viewport({
				layout : 'fit',
				items : [tabs]
			});

});
