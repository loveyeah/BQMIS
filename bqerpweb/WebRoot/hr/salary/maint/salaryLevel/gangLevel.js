Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var pointSalary = new GangLevel.pointSalary();
	var salaryLevel = new GangLevel.salaryLevel();
	var tabs = new Ext.TabPanel({
				activeTab : 0,  
				items : [{
							id : 'pointSalaryMaint',
							title : '岗位薪点工资',
							items:[salaryLevel.grid],
							autoScroll : true,
							layout : 'fit'
						},{
							id : 'salaryLevelMaint',
							title : '岗位薪级名称',
							autoScroll : true,
							items:[pointSalary.grid],
							layout : 'fit'
						}]
			});

	var view = new Ext.Viewport({
				layout : 'fit',
				items : [tabs]
			});

});