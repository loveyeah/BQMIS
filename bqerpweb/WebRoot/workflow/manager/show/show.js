Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function(){
	var entryId = getParameter("entryId");
//entryId=41
xmlloadWF(entryId);
function xmlloadWF(entryId) {
	Ext.Ajax.request({
		url : 'MAINTWorkflow.do',
		method : 'post',
		params : {
			action : 'loadByEntryId',
			entryId :  entryId
		},
		success : function(result, request) { 
			var xml = result.responseText;
			if (xml != null && xml != "") {
				Ext.Ajax.request({
					url : 'MAINTWorkflow.do',
					method : 'post',
					params : {
						action : 'getEntryInfo',
						entryId :  entryId
					},
					success : function(result, request) { 
						var runInfo =eval('(' + result.responseText + ')');   
						if (runInfo != null && runInfo != "") { 
							if(runInfo.currentStepId == "")
							{
								if(!runInfo.passStepIds.search("3") >= 0)
								{
									runInfo.passStepIds +=",3";
								}
							} 
							schema.xmlload(xml,runInfo.passStepIds,runInfo.currentStepId);
						}
					}
				});  
			}
		}
	});
	
	var buttons = new Ext.Toolbar({
		items:[
		{
			text : "查看审批列表",
           //iconCls : 'add',
			handler : function()
			{
				var url = "../approveInfo/approveInfo.jsp?entryId="+entryId; 
				window.showModalDialog(
						url,
						null,
						"dialogWidth:650px;dialogHeight:400px;;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;");
			} 
		}
		]
	});
	buttons.render("buttonsDiv");
	
//	var HistoryRecord = Ext.data.Record.create([{
//		name : 'stepName'
//	}, {
//		name : 'callerName'
//	}, {
//		name : 'opinionTime'
//	}, {
//		name : 'actionName'
//	}, {
//		name : 'opinion'
//	}]);
//	var  HistoryDs = new Ext.data.Store({
//		proxy : new Ext.data.HttpProxy({
//			url : 'MAINTWorkflow.do?action=getApproveList',
//			method : 'post'
//		}),
//		reader : new Ext.data.JsonReader({}, HistoryRecord)
//	});
//	 HistoryDs.load(); 
//	var  HistoryCm = new Ext.grid.ColumnModel([ new Ext.grid.RowNumberer(), {
//		header : '操作时状态',
//		dataIndex : 'stepName',
//		align : 'left',
//		width : 80
//	}, {
//		header : '操作人',
//		dataIndex : 'callerName',
//		align : 'left',
//		width : 130
//	}, {
//		header : '操作时间',
//		dataIndex : 'opinionTime',
//		align : 'left',
//		width : 80
//	}, {
//		header : '执行动作',
//		dataIndex : 'actionName',
//		align : 'left',
//		width : 130
//	}, {
//		header : 'opinion',
//		dataIndex : '审批意见',
//		align : 'left',
//		width : 130
//	}]);
//	 HistoryCm.defaultSortable = true; 
//	var  HistoryGrid = new Ext.grid.GridPanel({
//		layout:'fit',
//		ds : HistoryDs,
//		cm :  HistoryCm,
//		sm :  HistorySm,
//		title : '工作流列表',
//		autoScroll  : true,
//		autoWidth : true,
//		autoHeight : true,
//		border : false
//	});
//	 HistoryGrid.enableColumnHide = false;
//	 var win =new Ext.Window({
//	 	
//	 });
	
}
}); 
function showMenu(id) { 
	event.returnValue = false;
	event.cancelBubble = true;
	return false;
}
