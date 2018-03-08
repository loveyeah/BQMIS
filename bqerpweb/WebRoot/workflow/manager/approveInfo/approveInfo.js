Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var entryId = getParameter("entryId"); 
	var Historyoperation = Ext.data.Record.create([{
				name : 'id'
			}, {
				name : 'entryId'
			}, {
				name : 'stepId'
			}, {
				name : 'stepName'
			}, {
				name : 'actionId'
			}, {
				name : 'actionName'
			}, {
				name : 'caller'
			}, {
				name : 'callerName'
			}, {
				name : 'opinion'
			}, {
				name : 'opinionTime'
			}]);

	var ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'MAINTWorkflow.do?action=getApproveList&entryId='
									+ entryId,
							method : 'post'
						}),
				reader : new Ext.data.JsonReader({}, Historyoperation)
			});
	ds.load();

	var cm = new Ext.grid.ColumnModel([{
				header : '状态',
				dataIndex : 'stepName',
				align : 'left',
				width : 100
			}, {
				header : '执行动作',
				dataIndex : 'actionName',
				align : 'left',
				width : 100
			}, {
				header : '执行人',
				dataIndex : 'callerName',
				align : 'left',
				width : 100
			}, {
				header : '执行时间',
				dataIndex : 'opinionTime',
				align : 'left',
				width : 100
			}, {
				header : '审批意见',
				dataIndex : 'opinion',
				align : 'left',
				width : 200,
				renderer : function change(val) {
					return ' <span style="white-space:normal;">' + val
							+ ' </span>';
				}
			}]);
	cm.defaultSortable = false;
	var grid = new Ext.grid.GridPanel({
				ds : ds,
				cm : cm,
				title : '审批信息列表',
				autoWidth : true,
				layout : 'fit',
				height : 380,
				fitToFrame : true,
				border : false
			});
	grid.enableColumnHide = false;

	grid.render(Ext.getBody());
});