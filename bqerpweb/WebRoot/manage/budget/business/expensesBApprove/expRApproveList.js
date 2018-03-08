Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
function getDate() {
	var d, s, t;
	d = new Date();
	s = d.getFullYear().toString(10) + "-";
	t = d.getMonth() + 1;
	s += (t > 9 ? "" : "0") + t + "-";
	t = d.getDate();
	s += (t > 9 ? "" : "0") + t + " ";
	return s;
}
Ext.onReady(function() {
		var start=0;
		var limit=18;
		var record = new Ext.data.Record.create([{
						// 报销申请单id
						name : 'reportId',
						mapping : 0
					}, {
						// 报销金额大写
						name : 'reportMoneyUpper',
						mapping : 1
					}, {
						// 报销金额小写
						name : 'reportMoneyLower',
						mapping : 2
					}, {
						// 用途
						name : 'reportUse',
						mapping : 3
					}, {
						// 备注
						name : 'memo',
						mapping : 4
					}, {
						// 报销人
						name : 'reportBy',
						mapping : 5
					}, {
						// 报销时间
						name : 'reportDate',
						mapping : 6
					}, {
						// 审批状态
						name : 'workFlowStatus',
						mapping : 7
					} ,{
						// 工作流实例号
						name : 'workFlowNo',
						mapping : 8
					}, {
						// 报销部门
						name : 'reportDept',
						mapping : 9
					}, {
						// 费用来源
						name : 'itemName',
						mapping : 10
					}, {
						// 费用来源Id
						name : 'itemId',
						mapping : 11
					}])

		var dataProxy = new Ext.data.HttpProxy({
				url:'managebudget/finExpRList.action'
			});
	    var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	    }, record);

	    var store = new Ext.data.Store({
		proxy : dataProxy,
		reader : theReader
	   });
		var sm = new Ext.grid.CheckboxSelectionModel();
		// 开始日期
		var startDate = new Ext.form.TextField({
					fieldLabel : "",
					style : 'cursor:pointer',
					forceSelection : true,
					selectOnFocus : true,
					//allowBlank : false,
					anchor : "80%",
					//value : getDate(),
					readOnly : true,
					listeners : {
						focus : function() {
							WdatePicker({
										startDate : '%y-%M-%d',
										dateFmt : 'yyyy-MM-dd',
										alwaysUseStartDate : true
									});
						}
					}
				});
		// 结束日期
		var endDate = new Ext.form.TextField({
					id : 'endDate',
					fieldLabel : "",
					style : 'cursor:pointer',
					anchor : "80%",
					forceSelection : true,
					selectOnFocus : true,
					//allowBlank : false,
					readOnly : true,
					listeners : {
						focus : function() {
							var pkr = WdatePicker({
										startDate : '%y-%M-%d',
										dateFmt : 'yyyy-MM-dd',
										alwaysUseStartDate : true,
										onpicked : function() {
											if (startDate.getValue() == ""
													|| startDate.getValue() > endDate
															.getValue()) {
												Ext.Msg.alert("提示", "必须大于工程开始日期");
												endDate.setValue("")
												return;
											}
											endDate.clearInvalid();
										},
										onclearing : function() {
											endDate.markInvalid();
										}
									});
						}
					}
				});
		var queryButton = new Ext.Button({
						id : 'query',
						text : '查询',
						iconCls : 'query',
						handler : queryRecord
					})	
	   var dealButton = new Ext.Button({
						id : 'deal',
						text : '审批处理',
						iconCls : 'write',
						handler : updateRecord
					})
		var gridPanel = new Ext.grid.GridPanel({
			            id:'grid_div',
						sm : sm,
						store : store,
						columns : [sm, new Ext.grid.RowNumberer({
									header : "序号",
									width : 40,
									align : "center"
								}), {
									header : "报销人",
									width : 75,
									align : "center",
									sortable : true,
									dataIndex : 'reportBy'
								}, {
									header : "报销金额",
									width : 75,
									align : "center",
									sortable : true,
									dataIndex : 'reportMoneyLower'
								}, {
									header : "用途",
									width : 75,
									align : "center",
									sortable : true,
									dataIndex : 'reportUse'
								}, {
									header : "备注",
									width : 75,
									align : "center",
									sortable : true,
									dataIndex : 'memo'
								}],
						viewConfig : {
							forceFit : true
						},
						frame : true,
						bbar : new Ext.PagingToolbar({
									pageSize : limit,
									store : store,
									displayInfo : true,
									displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
									emptyMsg : "没有记录"
								}),
						tbar : ['会签时间:',startDate,'--',endDate,'-',queryButton,'-',dealButton],
						border : true,
						enableColumnHide : false,
						enableColumnMove : false,
						iconCls : 'icon-grid'
					});
	gridPanel.on('rowdblclick',updateRecord);
	function updateRecord()
	{
		if (gridPanel.selModel.hasSelection()) {
			var records = gridPanel.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择一条报销单进行审批！");
			} else {
				var record=gridPanel.getSelectionModel().getSelected();
				tabs.setActiveTab(1);
				trainRegister.setTrainId(record);
			}
		} else {
			Ext.Msg.alert("提示", "请先选择要审批的信报销单!");
		}
	}
		function queryRecord() {
			if ((endDate.getValue()==null||endDate.getValue()=="")&&(startDate.getValue()!=null&&startDate.getValue()!="")||
			(endDate.getValue()!=null&&endDate.getValue()!="")&&(startDate.getValue()==null||startDate.getValue()=="")) {
				Ext.Msg.alert("提示", "请选择时间段!");
				return;
			}
			if ((endDate.getValue()!=null||endDate.getValue()!="")&&(startDate.getValue()!=null||startDate.getValue()!="")&&endDate.getValue()<startDate.getValue()) {
				Ext.Msg.alert("提示", "结束时间必须大于开始时间!");
				return;
			}
		store.load({
			params : {
				start : start,
				limit : limit,
				startDate :startDate.getValue(),
				endDate :endDate.getValue(),
				flag:"1"
			}
		});
	}
		//queryRecord();
	var trainRegister = new maint.expRRegister();
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
							title : '报销单列表',
							autoScroll : true,
							items : gridPanel,
							layout : 'fit'
						},{
							id : 'trainRegister',
							title : '报销单审批',
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