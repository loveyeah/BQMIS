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
				url : 'managebudget/assignedFillList.action'
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
							assignName:Ext.get("temp.assignName").dom.value
			}
			
			store.load({
						params : {
							start : 0,
							limit : 18
						}
					})
	}


	var ids = new Array();
	function deleteAf(){
		var sm = grid.getSelectionModel();
		var selections = sm.getSelections();
		if (selections.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
				for (var i = 0; i < selections.length; i += 1) {
				var member = selections[i];
				if (member.get("assignId") != null) {
					ids.push(member.get("assignId"));
				}
			}
			Ext.Msg.confirm('提示', '是否确定删除该外委单吗？', function(response) {
				if (response == 'yes') {
					Ext.Ajax.request({
								method : 'post',
								url : 'managebudget/assignedFillDelete.action',
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
	
	function updateAf(){
	
		var member = grid.getSelectionModel().getSelected();
		var selections = grid.getSelectionModel().getSelections();	
//		alert(member.get("applyBy"));
		if (member) {
			if(selections.length>1){
			    Ext.Msg.alert("提示", "请先选择一行记录进行编辑!");
			    return;
			  }
			else {
						tabPanel.setActiveTab(1);
						assignFillRegister.setDetail(member);
						assignFillRegister.setAssignId(member.get("assignId"));
					}
			
		} else {
			Ext.Msg.alert("提示", "请先选择要编辑的行!");
		}
	}
	
	// 上报处理
	function reportBtn() {
		var sm = grid.getSelectionModel();
		var selected = sm.getSelections();
		var noticeNo;
		var isReport;
		var budgetTime = null;
		if (selected.length == 0 || selected.length > 1) {
			Ext.Msg.alert("系统提示信息", "请选择其中一项进行上报！");
		} else {
			var menber = selected[0];
			assignId = menber.get('assignId');
			isReport = menber.get('workFlowStatus');
			if (isReport !="未上报"
					&& isReport != "已退回") {
				Ext.Msg.alert("系统提示信息", "只有未上报和已退回的申请单允许上报");
			} else {
				//add by mlian20101015
				itemId = menber.get("itemId");
				itemName = menber.get("itemName");
				estimateMoney = menber.get("estimateMoney");
				
				financeLeft = itemFinanceLeft(itemId,budgetTime);				
//				financeLeftReal = parseInt(financeLeft)+parseInt(estimateMoney);
				if(estimateMoney > financeLeft){
					Ext.Msg.alert("系统提示信息",""+ itemName +"费用来源余额"+ financeLeft +"元，本次所需费用"+ estimateMoney +"元，大于所剩余额，不能上报");
				}else{
					var args=new Object();
					args.assignId=assignId;
					args.flowCode="assignedFill";
					args.entryId="";
					
					args.title="外委单上报";
					  var danger = window.showModalDialog('reportSign.jsp',
	                args, 'dialogWidth=700px;dialogHeight=500px;center=yes;help=no;resizable=no;status=no;');
	               query();
				}
				
//				var args=new Object();
//				args.assignId=assignId;
//				args.flowCode="assignedFill";
//				args.entryId="";
//				
//				args.title="外委单上报";
//				  var danger = window.showModalDialog('reportSign.jsp',
//                args, 'dialogWidth=700px;dialogHeight=500px;center=yes;help=no;resizable=no;status=no;');
//               query();
			}
		}
	}

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
						}, {
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
						},{
							text : "查询",
							iconCls : 'query',
							handler : query
						},{
							text : "新增",
							iconCls : 'add',
							handler : function() {
								tabPanel.setActiveTab(1);
								assignFillRegister.clearForm();
							}
						},{
							text : "删除",
							iconCls : 'delete',
							handler : deleteAf
						},{
							text : "修改",
							iconCls : 'update',
							handler : updateAf
						},{
							text : "上报",
							iconCls : 'upcommit',
							handler : reportBtn
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
	
grid.on('rowdblclick', updateAf);

	var assignFillRegister = new assignedFill.assignedFillin();

	var tabPanel = new Ext.TabPanel({
		        id:'div_tabs',
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
							title : '外委单列表',
							items : [grid]
						},{
							id : 'tab2',
							layout : 'fit',
							title : '外委单登记',
							items : [assignFillRegister.form]
						} ]
			});
			tabPanel.setActiveTab(1);
//			tabPanel.setActiveTab(0);

	
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
			
    query(); 
	});
