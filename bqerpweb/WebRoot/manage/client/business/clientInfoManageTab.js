var cliendId = "";
var cliendName = "";

function edit(cliend,clientname) {
	cliendId = cliend;
	cliendName = clientname;
}

Ext.onReady(function() {

	var panel1 = new Ext.FormPanel({
		id : 'tab1',
		title : '合作伙伴列表',
		items : [{html : '<iframe src="manage/client/business/clientList/clientList.jsp"  width="100%" height="800" />'}]
	});
	var panel2 = new Ext.FormPanel({
		id : 'tab2',
		title : '合作伙伴信息',
		autoScroll:true,
		items : [{ html : '<iframe id="iframe1" name="iframe1" scrolling="yes" src="manage/client/business/clientInfo/clientInfo.jsp" width="100%" height="800"/>'}]
	});
	var panel3 = new Ext.FormPanel({
		id : 'tab3',
		title : '联系人信息',
		items : [{html : '<iframe id="iframe2" scrolling="yes" src="manage/client/business/clientsContactInfo/clientsContactInfo.jsp"  width="100%" height="800" />'}]
	});
	var panel4 = new Ext.FormPanel({
		id : 'tab4',
		title : '资质材料信息',
		items : [{html : '<iframe id="iframe3" scrolling="yes" src="manage/client/business/clientQualificationInfo/clientQualificationList.jsp"  width="100%" height="800" />'}]
	});
	var panel5 = new Ext.FormPanel({
		id : 'tab5',
		title : '评价记录信息',
		items : [{html : '<iframe id="iframe4" scrolling="yes" src="manage/client/business/clientAppraiseInfo/clientAppraiseInfo.jsp"  width="100%" height="800" />'}]
	});

	var tabpanel = new Ext.TabPanel({
		title : 'mytab',
		id : 'mytab',
		activeTab : 0,
	//	autoScroll : true,
		items : [panel1, panel2, panel3,panel4,panel5]

	});
	
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		items : [tabpanel]
	});
	
});