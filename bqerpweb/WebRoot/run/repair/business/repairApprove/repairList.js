Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {

   var flag=1;
   var conn = Ext.lib.Ajax.getConnectionObject().conn;
	conn.open("POST", 'manageplan/selectApprovePage.action', false);
	conn.send(null);	
	if (conn.status == "200") {
		var result = conn.responseText;
		if(result>0){ 
		  window.location.replace("repairLeaderList.jsp");
		}
	}
	if(flag==1){
	  function getMon() {
				var d, s, t;
				d = new Date();
				s = d.getFullYear().toString(10) + "-";
				t = d.getMonth() + 1;
				s += (t > 9 ? "" : "0") + t
				return s;
			}
			var MyRecord = Ext.data.Record.create([{
						name : 'projectMainId',
						mapping : 0
					}, {
						name : 'projectMainYear',
						mapping : 1
					}, {
						name : 'repairTypeId',
						mapping : 2
					}, {
						name : 'repairTypeName',
						mapping : 3
					}, {
						name : 'fillBy',
						mapping : 4
					}, {
						name : 'fillName',
						mapping : 5
					}, {
						name : 'fillTime',
						mapping : 6
					}, {
						name : 'specialityId',
						mapping : 7
					}, {
						name : 'specialityName',
						mapping : 8
					}, {
						name : 'tasklistId',
						mapping : 9
					}, {
						name : 'tasklistName',
						mapping : 10
					}, {
						name : 'memo',
						mapping : 11
					}, {
						name : 'version',
						mapping : 12
					}, {
						name : 'workflowNo',
						mapping : 13
					}, {
						name : 'workflowStatus',
						mapping : 14
					}, {
						name : 'finalVersion',
						mapping : 15
					}, {
						name : 'situationProject',
						mapping : 16
					}]);

			var DataProxy = new Ext.data.HttpProxy({
						url : 'manageplan/findRepairRecordList.action'
					});

			var TheReader = new Ext.data.JsonReader({
						root : "list",
						totalProperty : "totalCount"
					}, MyRecord);

			var listStore = new Ext.data.Store({
						proxy : DataProxy,
						reader : TheReader
					});

			var sm = new Ext.grid.CheckboxSelectionModel();

			var editTime = new Ext.form.TextField({
						width : 100,
						style : 'cursor:pointer',
						allowBlank : true,
						readOnly : true,
						value : getMon(),
						listeners : {
							focus : function() {
								WdatePicker({
											isShowClear : true,
											startDate : '%y-%M',
											dateFmt : 'yyyy-MM'
										});
							}
						}
					});

			function query() {
				if (editTime.getValue() == null|| editTime.getValue() == "") {
					Ext.Msg.alert('提示', '请选择填写时间！')
				} else {
					listStore.baseParams = {
						flag : 'approve',
						editTime : editTime.getValue()
					}
					listStore.load({
								start : 0,
								limit : 18
							})
				}
			}

			var listGrid = new Ext.grid.GridPanel({
						id:'div_grid',
						region : "center",
						layout : 'fit',
						store : listStore,
						listeners : {
							'rowdblclick' : function() {
								var rec = listGrid.getSelectionModel().getSelected();
								tabs.setActiveTab(1);
								approveList.setMainId(rec.get("projectMainId"));
								approveList.setWorkFlowNo(rec.get("workflowNo"));
								approveList.setStatus(rec.get("workflowStatus"));
								approveList.setTasklistId(rec.get("tasklistId"));
								approveList.setSpecialityId(rec.get("specialityId"));
								approveList.setSituationProject(rec.get("situationProject"));
								approveList.setSpecialityName(rec.get("specialityName"));
								approveList.setRepairDetailBtn(true);
							}
						},
						columns : [sm, new Ext.grid.RowNumberer({
											header : '行号',
											width : 35,
											align : 'center'
										}), {
									header : "ID",
									width : 30,
									sortable : true,
									dataIndex : 'projectMainId',
									hidden : true
								}, {
									header : "专业",
									width : 150,
									align : 'center',
									sortable : true,
									dataIndex : 'specialityName'
								}, {
									header : "修理类别",
									width : 150,
									align : 'center',
									sortable : true,
									dataIndex : 'repairTypeName'
								}, {
									header : "填写时间",
									width : 150,
									align : 'center',
									sortable : true,
									dataIndex : 'fillTime'
								}, {
									header : "任务单名称",
									width : 150,
									sortable : true,
									dataIndex : 'tasklistName'
								}, {
									header : "备注",
									width : 150,
									allowBlank : false,
									sortable : true,
									dataIndex : 'memo'
								}/*, {
									header : "workflowStatus",
									width : 150,
									allowBlank : false,
									sortable : true,
									dataIndex : 'workflowStatus'
								}*/],
						sm : sm,
						autoSizeColumns : true,
						viewConfig : {
							forceFit : true
						},
						tbar : ["填写时间:", editTime,'-', {
									text : "查询",
									iconCls : 'query',
									minWidth : 70,
									handler : query
								}],
						// 分页
						bbar : new Ext.PagingToolbar({
									pageSize : 18,
									store : listStore,
									displayInfo : true,
									displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
									emptyMsg : "没有记录"
								})
					});
          
            query();	 
			var approveList = new repairMaint.approveList();
			var tabs = new Ext.TabPanel({
						id:'div_tabs',
						activeTab : 0,
						tabPosition : 'bottom',
						plain : false,
						defaults : {
							autoScroll : true
						},
						items : [{
									id : 'repairList',
									title : '检修项目列表',
									items : listGrid,
									autoScroll : true,
									layout : 'fit'
								}, {
									id : 'repairApprove',
									title : '检修项目审批',
									autoScroll : true,
									items : approveList.grid,
									layout : 'fit'
								}]
					});

			var view = new Ext.Viewport({
						layout : 'fit',
						items : [tabs]
					});	
	}
					
		});
		
		
