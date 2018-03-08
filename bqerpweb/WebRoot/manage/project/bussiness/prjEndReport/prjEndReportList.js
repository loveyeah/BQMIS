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
				type:'2',
				flag:'1'
			}
		repStore.load({
			params : {
				start : 0,
				limit : 18				
			}
		});	
	}
	
})

var updateButton = new Ext.Button({
	id:'update',
	text:'修改',
	iconCls:'update',
	handler:function(){
		
		if(repGrid.selModel.hasSelection()){
			var rec = repGrid.getSelectionModel().getSelections();
			if (rec.length > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项进行修改！");
			}else{
				var record = repGrid.getSelectionModel().getSelected();
				tabs.setActiveTab(1);
				addSp.updateForm(record);
			}
		} else {
			Ext.Msg.alert("提示", "请先选择要编辑的行!");
		}
			
		
	}
	
})


var deleteButton = new Ext.Button({
	id:'delete',
	text:'删除',
	iconCls:'delete',
	handler:function(){
		deleteRecord();
	}
	
})
//删除记录
	function deleteRecord()
	{
		var records = repGrid.selModel.getSelections();
		var ids = [];
		if (records.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {

			
			for (var i = 0; i < records.length; i += 1) {
				var member = records[i];
				if (member.get("reportId")) {
					ids.push(member.get("reportId")); 
				} else {
					
					repStore.remove(repStore.getAt(i));
				}
			}
		
			Ext.Msg.confirm("删除", "是否确定删除所选的记录？", function(
					buttonobj) {

				if (buttonobj == "yes") {

					Ext.lib.Ajax.request('POST',
							'manageproject/deletePrjReport.action', {
								success : function(action) {
									Ext.Msg.alert("提示", "删除成功！")
                                   	queryRecord();
						         	
								},
								failure : function() {
									Ext.Msg.alert('错误', '删除时出现未知错误.');
								}
							}, 'ids=' + ids);
				}
			});
			
		
		}

	}
var printButton = new Ext.Button({
	id:'print',
	text:'打印竣工报告书',
	iconCls:'print',
	handler:function(){
				if (repGrid.getSelectionModel().hasSelection()) {
									var rec = repGrid.getSelectionModel().getSelections();
									var record = repGrid.getSelectionModel().getSelected();
									if(rec.length>1){
										Ext.Msg.alert('警告','请选择一条记录查看！')
									}else{
										var url = "/powerrpt/report/webfile/projectEndReport.jsp?reportId="+record.data.reportId+"&backEntryBy="+record.data.backEntryBy;
										window.open(url);
									}
									
								} else {
									Ext.Msg.alert("提示信息", "请选择一条记录！");
								}	
	}
	
})
var saveButton = new Ext.Button({
	id:'save',
	text:'增加',
	iconCls:'save',
	handler:function(){
			tabs.setActiveTab(1);
			addSp.resetform();
	}
	
})
var checkButton = new Ext.Button({
		id : 'check',
		text : '竣工报告书回录',
		iconCls : 'check',
		handler : function() {
			var rec = repGrid.getSelectionModel().getSelections();
			if (rec.length > 1 || rec.length == 0) {
				Ext.Msg.alert('警告', '请选择一条记录');
			} else {
				var record = repGrid.getSelectionModel().getSelected();
				reportId = record.data.reportId;
				var args = new Object();
				args.reportId = reportId;
				args.rec = record;
				var url= "proOpenReregister.jsp";
				var emp = window
						.showModalDialog(
								url,
								args,
								'dialogWidth:900px;dialogHeight:700px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
			}
			repStore.reload();
		}

	})


var prj_type =  new Ext.ux.ComboBoxTree({
						fieldLabel : "工程类别",
						//allowBlank : false,
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
		// 竣工报告书id
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
		// 审批单位负责人
		name : 'approveChargeBy',
		mapping : 12
	}, {
		// 审批单位批复意见
		name : 'approveText',
		mapping : 13
	}, {
		// 审批单位批复时间
		name : 'approveDate',
		mapping : 14
	}, {
		// 施工单位负责人
		name : 'workChargeBy',
		mapping : 15
	}, {
		// 施工单位经办人
		name : 'workOperate_by',
		mapping : 16
	}, {
		// 施工单位经办时间
		name : 'workApproveDate',
		mapping : 17
	}, {
		// 回录人
		name : 'backEntryBy',
		mapping : 18
	}, {
		// 立项id
		name : 'prjId',
		mapping : 19
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
				listeners : {
					'rowdblclick' : function() {
						var rec = repGrid.getSelectionModel().getSelected();
						tabs.setActiveTab(1);
						addSp.updateForm(rec);
					}
				},
				columns : [sm,new Ext.grid.RowNumberer(),{
							header : "立项名称",
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
				prj_type,queryButton,'-',saveButton,'-',updateButton,'-',deleteButton,'-',printButton,'-',checkButton] ,
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
				type:'2',
				flag:'1'
			}
		repStore.load({
			params : {
				start : 0,
				limit : 18				
			}
		});	
	}
	var addSp = new addStartReport();
	
	var tabs = new Ext.TabPanel({
				activeTab : 1,
				tabPosition : 'bottom',
				plain : false,
				defaults : {
					autoScroll : true
				},
				items : [{
							id : 'repGridPanel',
							title : '竣工报告列表',
							autoScroll : true,
							items : repGrid,
							layout : 'fit'
						},{
							id : 'addStartReport',
							title : '竣工报告录入',
							items : addSp.panel,
							autoScroll : true,
							layout : 'fit'
						} ]
			});
		//queryRecord();
		tabs.on('tabchange',function(thisTab,newTab) {
		var Id=newTab.getId();
		if (Id=='repGridPanel') {
			queryRecord();
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