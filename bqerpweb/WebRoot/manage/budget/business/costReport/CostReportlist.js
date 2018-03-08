 Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	var MyRecord = new Ext.data.Record.create([{
		name : 'reportId',
		mapping : 0

	}, {
		name : 'reportBy',
		mapping : 1

	}, {
		name : 'reportMoneyLower',
		mapping : 2

	}, {
		name : 'reportUse',
		mapping : 3

	}, {
		name : 'memo',
		mapping : 4
	}, {
		name : 'reportByName',
		mapping : 5
	}, {
		name : 'deptName',
		mapping : 6
	}, {
		name : 'itemName',
		mapping : 7
	}, {
		name : 'reportMoneyUpper',
		mapping : 8
	}, {
		name : 'workflowstatus',
		mapping : 9
	}, {
		name : 'itemId',
		mapping : 10
	}]);           



	var DataProxy = new Ext.data.HttpProxy({
		url : 'managebudget/creportList.action'
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


	var ids = new Array();
	function deleteCost() {
		var sm = grid.getSelectionModel();
		var selections = sm.getSelections();
		if (selections.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selections.length; i += 1) {
				var member = selections[i];
				if (member.get("reportId") != null) {
					ids.push(member.get("reportId"));
				}
			}
			Ext.Msg.confirm('提示', '是否确定删除该费用报销记录？', function(response) {
				if (response == 'yes') {
					Ext.Ajax.request({
						method : 'post',
						url : 'managebudget/deleteCostReport.action',
						params : {
							ids : ids.join(",")
						},
						success : function(action) {
							Ext.Msg.alert("提示", "删除成功！");
							store.reload();
						},
						failure : function() {
							Ext.Msg.alert('错误', '删除时出现未知错误.');
						}
					});
				}
			});
		}

	}
		function  upcommitCost(){
			var budgetTime = null;
				var member = grid.getSelectionModel().getSelected();
		var selections = grid.getSelectionModel().getSelections();
		if (member) {
			if (selections.length > 1) {
				Ext.Msg.alert("提示", "请先选择一行记录上报!");
				return;
			} else {
			
				//add by mlian20101015
				itemId = member.get("itemId");
				itemName = member.get("itemName");
				reportMoneyLower = member.get("reportMoneyLower");
				
				financeLeft = itemFinanceLeft(itemId,budgetTime);				
//				financeLeftReal = parseInt(financeLeft)+parseInt(reportMoneyLower);
//				alert(financeLeft);
//				alert(itemId);
				if(reportMoneyLower > financeLeft){
					Ext.Msg.alert("系统提示信息",""+ itemName +"费用来源余额"+ financeLeft +"元，本次所需费用"+ reportMoneyLower +"元，大于所剩余额，不能上报");
				}else{
						var args=new Object();
						args.busiNo=member.get("reportId");
						args.flowCode="cBillwout";
						args.entryId="";
						args.title="费用报销单上报";
						var danger = window.showModalDialog('../expensesBApprove/reportSign.jsp',
		                args, 'dialogWidth=700px;dialogHeight=500px;center=yes;help=no;resizable=no;status=no;');
		               	store.load({
						params : {
							start : 0,
							limit : 18
				
						}
					}); 
				}
				
//			member.get("reportId")
//				var args=new Object();
//					args.busiNo=member.get("reportId");
//					args.flowCode="cBillwout";
//					args.entryId="";
//					args.title="费用报销单上报";
//					var danger = window.showModalDialog('../expensesBApprove/reportSign.jsp',
//	                args, 'dialogWidth=700px;dialogHeight=500px;center=yes;help=no;resizable=no;status=no;');
//	               	store.load({
//					params : {
//						start : 0,
//						limit : 18
//			
//					}
//		}); 

			}

		} else {
			Ext.Msg.alert("提示", "请先选择要上报的行!");
		}

			
		}
			
		

	function updateCost() {

		var member = grid.getSelectionModel().getSelected();
		var selections = grid.getSelectionModel().getSelections();
		if (member) {
			if (selections.length > 1) {
				Ext.Msg.alert("提示", "请先选择一行记录进行编辑!");
				return;
			} else {
				tabPanel.setActiveTab(0);
				CostReportRegister.setDetail(member);
				CostReportRegister.setReportId(member.get("reportId"));
			}

		} else {
			Ext.Msg.alert("提示", "请先选择要编辑的行!");
		}

	}
	var fuzzy = new Ext.form.TextField({
		name : 'fuzzy',
		id: 'fuzzy',
		xtype : 'textfield'
	});
	var grid = new Ext.grid.GridPanel({
		id : 'div_grid',
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
			hidden : true,
			sortable : true,
			dataIndex : 'reportId'
		
		}, {
			header : "报销人",
			width : 150,
			allowBlank : false,
			sortable : true,
			dataIndex : 'reportByName'
		}, {
			header : "报销金额",
			width : 150,
			allowBlank : false,
			sortable : true,
			dataIndex : 'reportMoneyLower'
		}, {
			header : "用途",
			width : 150,
			allowBlank : false,
			sortable : true,
			dataIndex : 'reportUse'
		}, {
			header : "备注",
			width : 150,
			allowBlank : false,
			sortable : true,
			dataIndex : 'memo'
		}, {
			header : "状态",
			width : 150,
			allowBlank : false,
			sortable : true,
			dataIndex : 'workflowstatus',
			renderer:function(v){
				if(v==0){
					return "未上报";
				}else if(v==10){
					return "已退回";
				}else{
					return "";
				}
			}
		},{
			header : "费用来源",
			width : 150,
			allowBlank : false,
			sortable : true,
			hidden : true,
			dataIndex : 'itemId'
		}],
		sm : sm,
		autoSizeColumns : true,
		viewConfig : {
			forceFit : true
		},
		tbar : ['报销人', fuzzy, {
		
			iconCls : 'query',
			text : "查询",
			handler : function() {
	  
				var fu = Ext.getCmp('fuzzy').getValue();// 得到输入框的值
					
				store.baseParams = ({
					fuzzy : fu
				});
				store.load({
					params : {
						start : 0,
						limit : 18,
					  fuzzy : fu
					}
		});
			}
		}, {
			text : "新增",
			iconCls : 'add',
			handler : function() {
				tabPanel.setActiveTab(0);
				CostReportRegister.clearForm();
			}
		}, {
			text : "删除",
			iconCls : 'delete',
			handler : deleteCost
		}, {
			text : "修改",
			iconCls : 'update',
			handler : updateCost
		}, {
			text : "上报",
			iconCls : 'upcommit',
			handler : upcommitCost
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

	grid.on('rowdblclick', updateCost);


	var CostReportRegister = new CostReport.CRFill();
	var tabPanel = new Ext.TabPanel({
		id : 'div_tabs',
		renderTo : document.body,
		activeTab : 0,
		tabPosition : 'bottom',
		plain : true,
		defaults : {
			autoScroll : true
		},
		frame : false,
		border : false,
		items : [{
			id : 'tab1',
			layout : 'fit',
			title : '费用报销填写',
			items : [CostReportRegister.form]
		},{
			id : 'tab2',
			layout : 'fit',
			title : '费用报销列表',
			items : [grid]	
		} ]
	});
 
tabPanel.on('tabchange',function(thisTab,newTab) {
	var Id=newTab.getId();
	if (Id=='tab1') {
		CostReportRegister.clearForm();

	}else{	query();
		
	}
})	
	function query() {
		store.load({
			params : {
				start : 0,
				limit : 18,
				  fuzzy :''

			}
		})
	}
			
	store.baseParams = ({
					fuzzy : ''
				});
	var layout = new Ext.Viewport({
		layout : "border",
		border : false,
		items : [{
			title : "",
			region : 'center',
			layout : 'fit',
			border : false,
			margins : '0 0 0 0',
			split : true,
			collapsible : false,
			items : [tabPanel]
		}]
	});

});
