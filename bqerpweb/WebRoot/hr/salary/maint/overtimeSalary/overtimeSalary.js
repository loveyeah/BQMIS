Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var overtimeSalaryBase = new OvertimeSalary.overtimeSalaryBase();
	var overtimeSalaryScale = new OvertimeSalary.overtimeSalaryScale();
	var tabs = new Ext.TabPanel({
				activeTab : 0,  
				items : [{
							id : 'pointSalaryMaint',
							title : '加班工资基数',
							items:[overtimeSalaryBase.grid],
							autoScroll : true,
							layout : 'fit'
						}
						,{
							id : 'salaryLevelMaint',
							title : '加班工资系数',
							autoScroll : true,
							items:[overtimeSalaryScale.grid],
							layout : 'fit'
						}
						]
			});

	var view = new Ext.Viewport({
				layout : 'fit',
				items : [tabs]
			});

});