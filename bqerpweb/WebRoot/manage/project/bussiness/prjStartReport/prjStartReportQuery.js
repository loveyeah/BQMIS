Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	
//开工日期
var start_date = new Ext.form.TextField({
		id : 'start_date',
		fieldLabel : '开工时间',
		style : 'cursor:pointer',
		name:'model.startDate',
		listeners : {
			focus : function(){
				WdatePicker({
					startDate : '%y-%m-%d',
					dateFmt : 'yyyy-MM-dd',
					alwaysUseStartDate : true
						
				});
				
			}
		}
	})
	
//竣工日期
	var end_date = new Ext.form.TextField({
		id : 'end_date',
		fieldLabel : '竣工日期',
		style : 'cursor:pointer',
		name:'model.endDate',
		listeners : {
			focus : function(){
				WdatePicker({
					startDate : '%y-%m-%d',
					dateFmt : 'yyyy-MM-dd',
					alwaysUseStartDate : true
						
				});
				
			}
		}
	})
//查询按钮
var queryButton = new Ext.Button({
	id:'query',
	text:'查询',
	iconCls:'query',
	handler:function(){
			repStore.baseParams = {
				start_date:start_date.getValue(),
				end_date:end_date.getValue(),
				prj_type:prj_type.getValue(),
				type:'1'
			}
		repStore.load({
			params : {
				start : 0,
				limit : 18				
			}
		});	
	}
	
})



var printButton = new Ext.Button({
	id:'print',
	text:'详细查看开工报告书',
	iconCls:'print',
	handler:function(){
				if (repGrid.getSelectionModel().hasSelection()) {
									var rec = repGrid.getSelectionModel().getSelections();
									var record = repGrid.getSelectionModel().getSelected();
									if(rec.length>1){
										Ext.Msg.alert('警告','请选择一条记录查看！')
									}else{
										var url = "/powerrpt/report/webfile/projectStartReport.jsp?reportId="+record.data.reportId;
										window.open(url);
									}
									
								} else {
									Ext.Msg.alert("提示信息", "请选择一条记录！");
								}	
	}
	
})




var prj_type =  new Ext.ux.ComboBoxTree({
						fieldLabel : "工程类别",
//						allowBlank : false,
						displayField : 'text',
						width:'60%',
				       valueField : 'id',
						hiddenName : 'model.prjTypeId',
						readOnly : true,
						tree : {
							xtype : 'treepanel',
							// 虚拟节点,不能显示
							rootVisible : false,
							loader : new Ext.tree.TreeLoader({
								dataUrl : 'manageproject/findByPId.action'
							}),
							root : new Ext.tree.AsyncTreeNode({
								id : '0',
								name : '灞桥电厂',
								text : '灞桥电厂'
							}),
							listeners : {
								click : function(node,rec) {
									typeId = node.id;
									typeName = node.text
								}
							}
						},
						selectNodeModel : 'all',
						listeners : {
							select: function(newNode,oldNode){
								this.setValue(node.id);
						
					}
				}

					})

var datalist = new Ext.data.Record.create([{
		// 开工报告书id
		name : 'reportId',
		mapping:0
	},{
		//工程合同编号
		name : 'conttreesNo',
		mapping:1
	},{
		//工程合同名称
		name : 'contractName',
		mapping:2
	},{
		// 工程投资
		name : 'prjFunds',
		mapping:3
	},{
		// 工程类别
		name : 'prjTypeId',
		mapping:4
	},{
		//开工日期
		name : 'startDate',
		mapping:5
	},{
		//竣工日期
		name : 'endDate',
		mapping:6
	},{
		//编号
		name : 'reportCode',
		mapping:7
	},{
		//工程地点
		name : 'prjLocation',
		mapping:8
	},{
		//填写人
		name : 'entryBy',
		mapping:9
	},{
		//填写时间
		name : 'entryDate',
		mapping:10
	},{
		//填写人姓名
		name : 'entryName',
		mapping:11
	}, {
		// 项目名称
		name : 'prjName',
		mapping : 20
	}])
	
	var repStore = new Ext.data.JsonStore({
				url : 'manageproject/getPrjList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : datalist
			});
	var sm = new Ext.grid.CheckboxSelectionModel();
	
	var repGrid = new Ext.grid.GridPanel({
				sm:sm,
				ds : repStore,
				columns : [sm,new Ext.grid.RowNumberer(),{
							header : "项目名称",
							width : 75,
							align : "center",
							sortable : true,
							dataIndex : 'prjName'
						},{
							header : "工程合同编号",
							width : 75,
							align : "center",
							sortable : true,
							dataIndex : 'conttreesNo'
						},{
							header : "工程合同名称",
							width : 75,
							align : "center",
							sortable : true,
							dataIndex : 'contractName'
						},{
							header : "工程投资",
							width : 75,
							align : "center",
							sortable : true,
							dataIndex : 'prjFunds'
						},{
							header : "工程类别",
							width : 75,
							align : "center",
							sortable : true,
							dataIndex : 'prjTypeId'
						},{
							header : "开工日期",
							width : 75,
							align : "center",
							sortable : true,
							dataIndex : 'startDate',
							renderer:function(v){
								if(v != null) {
									return v.substring(0,10);
								} else {
									return v;
								}
								
							}
						},{
							header : "竣工日期",
							width : 75,
							align : "center",
							sortable : true,
							dataIndex : 'endDate',
							renderer:function(v){
								if(v != null) {
									return v.substring(0,10);
								} else {
									return v;
								}
							}
						}],
				viewConfig : {
			                 forceFit : true
		           },
				frame : true,
				bbar : new Ext.PagingToolbar({
							pageSize : 18,
							store : repStore,
							displayInfo : true,
							displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
							emptyMsg : "没有记录"
						}),
				tbar:['开工时间：',start_date,'竣工时间：',end_date,'工程类别：',
				prj_type,queryButton,'-',printButton] ,
				border : true,
				enableColumnHide : false,
				enableColumnMove : false,
				iconCls : 'icon-grid'
			});
	function queryRecord(){
		repStore.baseParams = {
				start_date:start_date.getValue(),
				end_date:end_date.getValue(),
				prj_type:prj_type.getValue(),
				type:'1'
			}
		repStore.load({
			params : {
				start : 0,
				limit : 18				
			}
		});	
	}

			queryRecord()
	
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
							items : [repGrid]

						}]
			});
			
});