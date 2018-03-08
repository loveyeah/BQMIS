Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	var MyRecord = Ext.data.Record.create([{
				name : 'assignId',
				mapping:0
			}, {
				name : 'assignName',
				mapping:1
			}, {
				name : 'itemId',
				mapping:2
			}, {
				name : 'itemName',
				mapping:12
			}, {
				name : 'estimateMoney',
				mapping:3
			}, {
				name : 'assignFunction',
				mapping:4
			},{
				name : 'memo',
				mapping:5
			}, {
				name : 'applyBy',
				mapping:6
			},{
				name : 'applyById',
				mapping:7
			}, {
				name : 'applyDept',
				mapping:9
			}, {
				name : 'applyDate',
				mapping:8
			}, {
				name : 'workFlowNo',
				mapping:10
			}, {
				name : 'workFlowStatus',
				mapping:11
			}, {
				name : 'applyDeptId',
				mapping:13
			}]);


	var DataProxy = new Ext.data.HttpProxy({
				url : 'managebudget/assignedFillListall.action'
			});

	var TheReader = new Ext.data.JsonReader({
				root : "list",
				totalProperty : "totalCount"
			}, MyRecord);

	var store = new Ext.data.Store({
				proxy : DataProxy,
				reader : TheReader
			});


	var sm = new Ext.grid.CheckboxSelectionModel();

	function query() {
			store.baseParams = {
							assignName:Ext.get("temp.assignName").dom.value,
							applyBy:Ext.get("temp.applyBy").dom.value
			}
			
			store.load({
						params : {
							start : 0,
							limit : 18
						}
					})
	}


	var ids = new Array();	
	
	var grid = new Ext.grid.GridPanel({
		        id:'div_grid',
				region : "center",
				layout : 'fit',
				store : store,
				columns : [sm, new Ext.grid.RowNumberer({
									header : '序号',
									width : 35,
									align : 'left'
								}), {
							header : "ID",
							width : 35,
							sortable : true,
							dataIndex : 'assignId',
							hidden : true
						},{
							header : "外委名称",
							width : 80,
							allowBlank : false,
							sortable : true,
							dataIndex : 'assignName'
//							renderer:function(v){
//								var rv = v.split(',');
//								return rv[0];
//							}
						}, {
							header : "费用来源",
							width : 80,
							allowBlank : false,
							sortable : true,
							dataIndex : 'itemName'
//							renderer:function(value, cellmeta, record, rowIndex,
//		                            columnIndex, store){
//								var value = record.get("assignName");
//								return value.split(',')[1];
//							}
						},{
							header : "估计金额",
							width : 80,
							allowBlank : false,
							sortable : true,
							dataIndex : 'estimateMoney'
						}, {
							header : "状态",
							width : 60,
							sortable : true, 
							dataIndex : 'workFlowStatus' 
						},{
							header : "用途",
							width : 80,
							allowBlank : false,
							sortable : true,
							dataIndex : 'assignFunction'
						},{
							header : "备注",
							width : 80,
							allowBlank : false,
							sortable : true,
							dataIndex : 'memo'
						}, {
							header : "工作流号",
							width : 35,
							sortable : true,
							dataIndex : 'workFlowNo',
							hidden : true
						}],
				sm : sm,
				autoSizeColumns : true,
				viewConfig : {
					forceFit : true
				},
				tbar : [ '外委名称',{
							id : 'temp.assignName',
							name : 'temp.assignName',
							xtype : 'textfield'
						},'申请人',{
							id : 'temp.applyBy',
							name : 'temp.applyBy',
							xtype : 'textfield'
						},{
							text : "查询",
							iconCls : 'query',
							handler : query
						},{
							text : "审批查询",
							iconCls : 'view',
							handler : approveQuery
						}],
				// 分页
				bbar : new Ext.PagingToolbar({
							pageSize : 18,
							store : store,
							displayInfo : true,
							displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
							emptyMsg : "没有记录"
						})
			});
	
grid.on('rowdblclick', approveQuery);

//	var tabPanel = new Ext.TabPanel({
//		        id:'div_tabs',
//				renderTo : document.body,
//				activeTab : 0,
//				tabPosition : 'bottom',
//				plain : true,
//				defaults : {
//					autoScroll : true
//				},
//				frame : false,
//				border : false,
//				items : [{
//							id : 'tab1',
//							layout : 'fit',
//							title : '外委单列表',
//							items : [grid]
//						}]
//			});
//			tabPanel.setActiveTab(1);
////			tabPanel.setActiveTab(0);
//
//	
//	var layout = new Ext.Viewport({
//				layout : "border",
//				border : false,
//				items : [{
//							title : "",
//							region : 'center',
//							layout : 'fit',
//							border : false,
//							margins : '0 0 0 0',
//							split : true,
//							collapsible : false,
//							items : [tabPanel]
//						}]
//			});

//审批查询
 function approveQuery(){ 
 	
 	    var selections = grid.getSelectionModel().getSelections();	
		if(selections.length>1 || selections.length==0){
			    Ext.Msg.alert("提示", "请先选择一行记录进行查询!");
			    return;
		};
	var entryId = grid.getSelectionModel().getSelected().data.workFlowNo;
 	var url="";
        	if(entryId==null||entryId=="")
        	{
			 url = "/power/workflow/manager/flowshow/flowshow.jsp?flowCode=assignedFill";
        	}
        	else
        	{
        		url = "/power/workflow/manager/show/show.jsp?entryId="+entryId;
        	}
        	window.open( url);
		}
			
	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				items : [grid]
			});

    query(); 
	});
