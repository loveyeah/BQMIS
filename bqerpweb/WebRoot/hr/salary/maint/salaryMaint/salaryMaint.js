Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {　
	var tabpanel = new Ext.TabPanel({
		title : 'mytab',
		activeTab : 0,
		items : [{
		id : 'tab1',
		title : '基础工资',
		html: '<iframe src="hr/salary/maint/basisSalary/basisSalary.jsp"  width="100%" height="800" />'
	}, {
		id : 'tab2',
		title : '工龄工资',
		html: '<iframe src="hr/salary/maint/senioritySalary/senioritySalary.jsp"  width="100%" height="800" />'
	}, {
		id : 'tab3',
		title : '薪点点值',
		html: '<iframe src="hr/salary/maint/salarypoint/salarypoint.jsp"  width="100%" height="800" />'
	},{
		id : 'tab4',
		title : '运行津贴',
		html: '<iframe src="hr/salary/maint/operationsalary/operationsalary.jsp"  width="100%" height="800" />'
	},{
		id : 'tab5',
		title : '岗位薪级',
		html: '<iframe src="hr/salary/maint/salaryLevel/gangLevel.jsp"  width="100%" height="800" />'
	},{
		id : 'tab6',
		title : '加班工资',
		html: '<iframe src="hr/salary/maint/overtimeSalary/overtimeSalary.jsp"  width="100%" height="800" />'
	},{
		id : 'tab7',
		title : '月出勤天数',
		html: '<iframe src="hr/salary/maint/attendanceDays/attendanceDays.jsp"  width="100%" height="800" />'
	},{
		id : 'tab8',
		title : '病假工资标准',
		html: '<iframe src="hr/salary/maint/sickSalary/sickSalary.jsp"  width="100%" height="800" />'
	},{
		id : 'tab9',
		title : '月奖基数',
		html: '<iframe src="hr/salary/maint/monthAward/monthAward.jsp"  width="100%" height="800" />'
	},{
		id : 'tab10',
		title : '月奖扣除标准天数',
		html: '<iframe src="hr/salary/maint/monthAwardStandarDays/monthAwardStanderDays.jsp"  width="100%" height="800" />'
	},{
		id : 'tab11',
		title : '大奖维护',
		html: '<iframe src="hr/salary/maint/bigaward/bigAward.jsp"  width="100%" height="800" />'
	}]　
	});
	
	var viewnew = new Ext.Viewport({
		autoScroll : true,
		layout : "fit",
		items : [tabpanel]
	});

});