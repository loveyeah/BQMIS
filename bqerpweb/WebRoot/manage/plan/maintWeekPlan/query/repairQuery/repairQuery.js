Ext.onReady(function() {
	Ext.QuickTips.init();
	var northsm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			});
			
	var deptList = new Ext.data.Record.create([northsm, {
				name : 'planDepId',
				mapping:0
			},{
				name : 'planTime',
				mapping:1
			}, {
				name : 'editDep',
				mapping:2
			}, {
				name : 'editDepName',
				mapping:3
			},{
				name : 'editBy',
				mapping:4
			}, {
				name : 'editByName',
				mapping:5
			},  {
				name : 'editDate',
				mapping:6
			}, {
				name : 'workFlowNo',
				mapping:7
			}, {
				name : 'status',
				mapping:8
			}, {
				name : 'enterpriseCode',
				mapping:9
			}]);
	

	var store = new Ext.data.JsonStore({
				url : 'manageplan/findAlldept.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : deptList
			});
	function getMon() {
	var d, s, t;
	d = new Date();
	s = d.getFullYear().toString(10) + "-";
	t = d.getMonth() + 1;
	s += (t > 9 ? "" : "0") + t  /*+ "-";
	t = d.getDate();
	s += (t > 9 ? "" : "0") + t + " "; */
	return s;
}
var weekSelect = new Ext.form.ComboBox({
					readOnly : true,
					name : 'name',
					hiddenName : 'week',
					mode : 'local',
					width:70,
					value:"1",
					fieldLabel : '完成情况',
					triggerAction : 'all',
					store : new Ext.data.SimpleStore(
						{
							fields : ['name','value'],
							data : [
									['第一周', '1'],
									['第二周', '2'],
									['第三周', '3'],
									['第四周', '4'],
									['第五周', '5'],
									['第六周', '6']]
						}),
					valueField : 'value',
					displayField : 'name',
					anchor : "15%"
		 })
	var planStart = new Ext.form.TextField({
						style : 'cursor:pointer',
						/*id : "fuzzy",*/
						name : 'fuzzy',
						fieldLabel : '计划时间',
						readOnly : true,
						anchor : "80%",
						value :getMon() ,
						listeners : {
							focus : function() {
								WdatePicker({
											startDate : '%y-%M',
											alwaysUseStartDate : false,
											dateFmt : 'yyyy-MM',
											onpicked : function(v) {
												
												plantime1=this.value.substr(0,4)+this.value.substr(5,2);
												
											}
											/*onclearing : function() {
												planStartDate.markInvalid();
											}*/
										});
							}
						}
					});
	
	
 var btnQuery = new Ext.Button({
		text : '查询',
		iconCls : 'query',
		handler : query
	});
 var btnApproveQuery = new Ext.Button({
		text : '审批查询',
		iconCls : 'approve',
		handler : Approvequery
	});
	function accPlanTime(start) {
		if (!start) {
			start = planStart.getValue();
		}
		var plantime = start.substr(0, 4) + start.substr(5, 2);
		plantime += weekSelect.getValue();
		return plantime;
	} 
 
	
	store.on("beforeload",function(){ 
		Ext.apply(this.baseParams,{
			/*status:"0,3",*/
      		start:0,
      		limit:18,
			planTime : accPlanTime(this.value)
			
		});
		
	});
	
	
 
function query(){
	

      store.load();
      }
      
      
     
   
function Approvequery(){
var record = northgrid.getSelectionModel().getSelected();
                            if(record!=null)
                            {
                            var entryId = record.get("workFlowNo");
                           }
                          
		if (entryId == null || entryId == "") {
			url = "/power/workflow/manager/flowshow/flowshow.jsp?flowCode="
					+ "bqRepair";
			window.open(url);
		} else {
			var url = "/power/workflow/manager/show/show.jsp?entryId="
					+ entryId;
			window.open(url);

			query();
		}
	}
	var headerTbar = new Ext.Toolbar({ 
		items : ["计划时间", planStart,"计划周",weekSelect,btnQuery,'-',btnApproveQuery]
	});

  var northgrid = new Ext.grid.GridPanel({
				store : store,
				columns : [northsm, new Ext.grid.RowNumberer(), {
							header : "部门",
							width : 60,
							align : "left",
							sortable : true,
							dataIndex : 'editDepName'
						}, {
							header : "填写人",
							width : 60,
							sortable : false,
							dataIndex : 'editByName'
						}, {
							header : "填写时间",
							width : 100,
							sortable : false,
							dataIndex : 'editDate'
						}, {
							header : "状态",
							width : 60,
							align : "center",
							sortable : true,
							dataIndex : 'status'
						}],
				viewConfig : {
					forceFit : true
				},
				tbar : headerTbar,
				sm : northsm,
				frame : true,
				bbar : new Ext.PagingToolbar({
							pageSize : 18,
							store : store,
							displayInfo : true,
							displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
							emptyMsg : "没有记录"
						}),
				enableColumnHide : false,
				enableColumnMove : false,
				iconCls : 'icon-grid'
			});
			

	var detailList = new Ext.data.Record.create([northsm, {
				name : 'planDetailId',
				mapping:0
			},{
				name : 'planDepId',
				mapping:1
			}, {
				name : 'content',
				mapping:2
			}, {
				name : 'chargeDep',
				mapping:3
			},{
				name : 'chargeDepName',
				mapping:4
			},{
				name : 'assortDep',
				mapping:5
			}, {
				name : 'assortDepName',
				mapping:6
			},{
				name : 'beginTime',
				mapping:7
			},  {
				name : 'endTime',
				mapping:8
			}, {
				name : 'days',
				mapping:9
			}, {
				name : 'memo',
				mapping:10
			}, {
				name : 'isUse',
				mapping:11
			},{
				name : 'enterpriseCode',
				mapping:12
			}]);
	

	var ds = new Ext.data.JsonStore({
				url : 'manageplan/getDetailByDept.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : detailList
			});
	var southgrid = new Ext.grid.GridPanel({
				store : ds,
				columns : [/*northsm,*/ new Ext.grid.RowNumberer({
				                header : '序号',
				                width : 40,
				                align : 'center'}),
				                {
							header : "设备名称及检修内容",
							width : 100,
							sortable : true,
							dataIndex : 'content'
						}, {
							header : "负责单位",
							width : 100,
							sortable : false,
							dataIndex : 'chargeDepName'
						}, {
							header : "配合单位",
							width : 100,
							sortable : false,
							dataIndex : 'assortDep'
						}, {
							header : "计划开工日期",
							width : 80,
							sortable : true,
							dataIndex : 'beginTime',
							renderer : function format(val) {
								if(val!=null&&val!="")
								{
								str = val.substr(0, 10);
								return str;
								}
								
							}
						},{
							header : "计划结束日期",
							width : 80,
							sortable : false,
							dataIndex : 'endTime'
						}, {
							header : "天数",
							width : 60,
							sortable : false,
							dataIndex : 'days'
						},{
							header : "备注",
							width : 100,
							sortable : false,
							dataIndex : 'memo'
						}],
				viewConfig : {
					forceFit : true
				},
				/*tbar : tbar,*/
				sm : new Ext.grid.RowSelectionModel(),
				frame : true,
				bbar : new Ext.PagingToolbar({
							pageSize : 18,
							store : ds,
							displayInfo : true,
							displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
							emptyMsg : "没有记录"
						}),
				enableColumnHide : false,
				enableColumnMove : false,
				iconCls : 'icon-grid'
			});	
	
	
  northgrid.on("dblclick",function(){
	var record = northgrid.getSelectionModel().getSelected();
                             	deptId=record.get("planDepId");
                             	
                              /*	alert(deptId);*/
       ds.on("beforeload",function(){ 
		Ext.apply(this.baseParams,{
			deptId : deptId,
				start:0,
				limit:18
				
			
		});
		
	});
                              	
	
   	ds.load();
   	

})

// 设定布局器及面板
	var layout = new Ext.Viewport({
				layout : "border",
				border : false,
				items : [{
							title : "检修周计划部门信息",
							region : 'center',
							layout : 'fit',
							border : false,
							margins : '0 0 0 0',
							split : true,
							collapsible : false,
							items : [northgrid]

						}, {
							title : "计划详细信息",
							region : "south",
							layout : 'fit',
							border : false,
							margins : '0 0 0 0',
							height : 400,
							split : true,
							collapsible : true,
							// 注入表格
							items : [southgrid]
						}]
			});
})