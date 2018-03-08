Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
			
		var projectRecord = new Ext.data.Record.create([{
						// 开工报告书id
						name : 'checkId',
						mapping : 0
					}, {
						// 项目名称
						name : 'contractName',
						mapping : 1
					}, {
						// 工程合同编号
						name : 'conId',
						mapping : 2
					}, {
						// 编号
						name : 'reportCode',
						mapping : 3
					}, {
						// 开工日期
						name : 'startDate',
						mapping : 4
					}, {
						// 竣工日期
						name : 'endDate',
						mapping : 5
					}, {
						// 承包单位名称
						name : 'contractorName',
						mapping : 6
					}, {
						// 承包单位ID
						name : 'contractorId',
						mapping : 7
					}, {
						// 承包负责人
						name : 'chargeBy',
						mapping : 8
					}, {
						// 发包部门负责人
						name : 'deptChargeBy',
						mapping : 9
					}, {
						// 发包部门工程方负责人验收评价
						name : 'checkAppraise',
						mapping : 10
					}, {
						// 填写人
						name : 'entryBy',
						mapping : 11
					}, {
						// 填写时间
						name : 'entryDate',
						mapping : 12
					}, {
						// 回录人Code
						name : 'backEntryBy',
						mapping : 13
					}, {
						// 工作流号
						name : 'workFlowNo',
						mapping : 14
					}, {
						//立项id
						name : 'prjId',
						mapping : 15
					}, {
						// 立项名称
						name : 'prjName',
						mapping : 16
					}])

		var dataProxy = new Ext.data.HttpProxy({
				url:'manageproject/queryReport.action'
			});
	    var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	    }, projectRecord);

	    var projectStore = new Ext.data.Store({
		proxy : dataProxy,
		reader : theReader
	   });
		var sm = new Ext.grid.CheckboxSelectionModel();
		var projectName = new Ext.form.TextField({
						id : 'projectName',
						fieldLabel : '项目名称',
						name : 'projectName',
						width : 100
					})
		var queryButton = new Ext.Button({
						id : 'query',
						text : '查询',
						iconCls : 'query',
						handler : queryRecord
					})	
	   var dealButton = new Ext.Button({
						id : 'deal',
						text : '签字处理',
						iconCls : 'write',
						handler : updateRecord
					})
		var projectGrid = new Ext.grid.GridPanel({
			            id:'grid_div',
						sm : sm,
						store : projectStore,
						columns : [sm, new Ext.grid.RowNumberer(),{
									header : "立项名称",
									width : 75,
									align : "center",
									sortable : true,
									dataIndex : 'prjName'
								},  {
									header : "项目名称",
									width : 75,
									align : "center",
									sortable : true,
									dataIndex : 'contractName'
								}, {
									header : "工程开始时间",
									width : 75,
									align : "center",
									sortable : true,
									dataIndex : 'startDate',
									renderer : function(v) {
										if (v != null) {
											return v.substring(0, 10);
										} else {
											return v;
										}
									}
								}, {
									header : "工程结束时间",
									width : 75,
									align : "center",
									sortable : true,
									dataIndex : 'endDate',
									renderer : function(v) {
										if (v != null) {
											return v.substring(0, 10);
										} else {
											return v;
										}
									}
								}, {
									header : "承包单位名称",
									width : 75,
									align : "center",
									sortable : true,
									dataIndex : 'contractorName'
								}, {
									header : "承包方负责人",
									width : 75,
									align : "center",
									sortable : true,
									dataIndex : 'chargeBy'
								}],
						viewConfig : {
							forceFit : true
						},
						frame : true,
						bbar : new Ext.PagingToolbar({
									pageSize : 18,
									store : projectStore,
									displayInfo : true,
									displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
									emptyMsg : "没有记录"
								}),
						tbar : ['项目名称:',projectName,queryButton,dealButton],
						border : true,
						enableColumnHide : false,
						enableColumnMove : false,
						iconCls : 'icon-grid'
					});
	
	projectGrid.on('rowdblclick',updateRecord);
	function updateRecord()
	{
		if (projectGrid.selModel.hasSelection()) {
			var records = projectGrid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑！");
			} else {
				var record=projectGrid.getSelectionModel().getSelected();
				tabs.setActiveTab(1);
				trainRegister.setTrainId(record);
			}
		} else {
			Ext.Msg.alert("提示", "请先选择要编辑的行!");
		}
	}
		function queryRecord() {
		projectStore.load({
			params : {
				start : 0,
				limit : 18,
				projectName :projectName.getValue(),
				statusId:1
			}
		});
	}
		//queryRecord();
	var trainRegister = new trainMaint.trainRegister();
	var tabs = new Ext.TabPanel({
				id:'maintab',
				activeTab : 1,
				tabPosition : 'bottom',
				plain : false,
				defaults : {
					autoScroll : true
				},
				items : [ {
							id : 'trainList',
							title : '项目验收列表',
							autoScroll : true,
							items : projectGrid,
							layout : 'fit'
						},{
							id : 'trainRegister',
							title : '项目验收审批',
							items : trainRegister.panel,
							autoScroll : true,
							layout : 'fit'
						}]
			});
	tabs.on('tabchange',function(thisTab,newTab) {
		var Id=newTab.getId();
		if (Id=='trainList') {
			queryRecord();
		}else{
			trainRegister.resetInputField();
		}
	})	
			// 设定布局器及面板
			var layout = new Ext.Viewport({
						layout : "border",
						border : false,
						items : [{
									region : 'center',
									layout : 'fit',
									border : false,
									margins : '1 0 1 1',
									collapsible : true,
									items : [tabs]

								}]
					});
					tabs.setActiveTab(0);

		});