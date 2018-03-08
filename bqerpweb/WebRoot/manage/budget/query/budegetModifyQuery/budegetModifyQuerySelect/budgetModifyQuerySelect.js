Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var budgetItemId=getParameter("budgetItemId");
	var changeId =getParameter("changeId");
	var changeWorkFlowNo = getParameter("changeWorkFlowNo");
	
	var _url1 = "manage/budget/query/budegetModifyQuery/budegetModifyQuerySelect/budgetModifyQueryInfo.jsp?budgetItemId="+budgetItemId+"&changeId="+changeId+"&changeWorkFlowNo="+changeWorkFlowNo;

	Ext.Ajax.request({
					url : 'managebudget/findBudgetModifyIds.action',
					params : {
						budgetItemId :budgetItemId
					},
					method : 'post',
					waitMsg : '正在加载数据...',
					success : function(result, request) {
					 	var i = 0;
					 	var o = eval("("+result.responseText+")");				 	
					 	for(i=0;i< o.length-1;i++){				 		
					 	_url1 ="manage/budget/query/budegetModifyQuery/budegetModifyQuerySelect/budgetModifyQueryInfo.jsp?changeId="+ o[i]+"&budgetItemId="+budgetItemId+"&changeWorkFlowNo="+changeWorkFlowNo;
					 	addtapanel();
					 	}
					},
					failure : function(result, request) {
						Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
					}
				});
	var tabpanel = new Ext.TabPanel({
		id : 'maintab',
		activeTab : 0,
		border : false,
		tabPosition : 'bottom',
		autoScroll : true,
		resizeTabs : true, // turn on tab resizing
		tabWidth : 105,
		items : [{
			id : 'tab1',
			title : '预算变更信息1',
			html : '<iframe id="iframe1" name="iframe1"  src="' + _url1
					+ '"  frameborder="0"  width="100%" height="100%" />'
		}]
	});
	
	function addtapanel(){
		var index = tabpanel.items.length + 1;
		var title =  tabpanel.items.length+1;
		var tabid = "tab"+ index;
		var tabpage  = tabpanel.add({
		id : tabid,
		title : "预算变更信息"+title,
		html : '<iframe id="iframe2"  src="' + _url1
					+ '"  frameborder="0"  width="100%" height="100%" />'
		})
	}
	
	var layout = new Ext.Viewport({
		layout : "border",
		border : false,
		items : [{
			region : "center",
			layout : 'fit',
			collapsible : true,
			split : true,
			border :false,
			margins : '0 0 0 0',
			// 注入表格
			items : [tabpanel]
		}]
	})
})
