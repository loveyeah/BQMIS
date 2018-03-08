Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	function ChangeDateToString(DateIn) {
		var Year = 0;
		var Month = 0;
		var Day = 0;
		var CurrentDate = "";
		// 初始化时间
		Year = DateIn.getYear();
		Month = DateIn.getMonth() + 1;
		Day = DateIn.getDate();
		CurrentDate = Year + "-";
		if (Month >= 10) {
			CurrentDate = CurrentDate + Month;
		} else {
			CurrentDate = CurrentDate + "0" + Month;
		}
		return CurrentDate;
	}
	var enddate = new Date();
	var startdate = enddate.add(Date.MONTH, 1);
	startdate = startdate.getFirstDateOfMonth();
	// 审批状态
	var status;
	
	// 计划时间
	var planDate = new Ext.form.TextField({
				name : 'planTime',
				fieldLabel : '计划时间',
				//value : ChangeDateToString(startdate),
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '',
									alwaysUseStartDate : true,
									dateFmt : "yyyy-MM"
									// add by liuyi 091214
									,
									isShowClear : false

								});
						this.blur();
					}
				}
			});
	var con_item = Ext.data.Record.create([{
				name : 'trainDetail.trainingDetailId'
			}, {
				name : 'trainDetail.trainingMainId'
			}, {
				name : 'trainDetail.trainingTypeId'
			}, {
				name : 'trainDetail.trainingName'
			}, {
				name : 'trainDetail.trainingLevel'
			}, {
				name : 'trainDetail.trainingNumber'
			}, {
				name : 'trainDetail.trainingHours'
			}, {
				name : 'trainDetail.chargeBy'
			}, {
				name : 'trainDetail.trainingDep'
			}, {
				name : 'trainDetail.fillBy'
			}, {
				name : 'planTypeName'
			}, {
				name : 'fillDate'
			}, {
				name : 'trainingTypeName'
			}, {
				name : 'chargeName'
			}, {
				name : 'deptName'
			}, {
				name : 'deptCode'
			}, {
				name : 'gatherId'
			}, {
				name : 'workflowNo'
			}, {
				name : 'workflowStatus'
			},{name:'trainingMonth'}]);

	var con_sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : false
			});
	var con_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'manageplan/trainPlanGatherAfter.action'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : "totalCount",
							root : "list"
						}, con_item)
			});

	var queryRec = function() {
	if(planDate.getValue()!=null&&planDate.getValue()!="")
	{
		var this_title = "<center><font color='black' size='2'>"
				+ planDate.getValue().substring(0, 4) + '年'
				+ planDate.getValue().substring(5, 7) + "月厂各部门月度培训计划</center>";
				
		          Grid.setTitle(this_title);
	}    
		con_ds.baseParams = {
			planTime : planDate.getValue()
		}
		con_ds.load({
					params : {
						approve : "approve",
//						approve : status,
						start : 0,
						limit : 18
					}
				})
	}
	var con_item_cm = new Ext.grid.ColumnModel([con_sm,
			// add by liuyi 091214 行号
			new Ext.grid.RowNumberer({
						header : '',
						width : 35
					}), {
				header : "部门",
				width : 100,
				sortable : false,
				align : 'center',
				renderer : function(value, matedata, record, rowIndex,
						colIndex, store) {
					if (record && rowIndex > 0) {
						if (con_ds.getAt(rowIndex).get('deptName') == con_ds
								.getAt(rowIndex - 1).get('deptName')
								|| con_ds.getAt(rowIndex).get('deptName') == '')
							return '';
					}
					return value;
				},
				dataIndex : 'deptName'
			}
			// add by liuyi 091214
			, {
				header : '类别',
				width : 100,
				align : 'center',
				dataIndex : 'planTypeName'
			}, {
				header : "项目级别",
				width : 100,
				sortable : false,
				hidden : true,
				align : 'center',
				dataIndex : 'trainDetail.trainingLevel',
				renderer : function(v) {
					if (v == 1) {
						return "合计";
					}
					if (v == 2) {
						return "明细";
					}
				}
			}, {
				header : "计划培训项目",
				width : 400,
				sortable : false,
				align : 'left',
				dataIndex : 'trainDetail.trainingName'
			}, {
				header : "计划人数",
				width : 100,
				sortable : false,
				align : 'center',
				dataIndex : 'trainDetail.trainingNumber'
				// renderer : getlevelName
		}, {
				header : "培训课时",
				width : 100,
				sortable : false,
				align : 'center',
				dataIndex : 'trainDetail.trainingHours'
				// renderer : getlevelName
		}	, {
				header : "负责人",
				width : 100,
				sortable : false,
				align : 'center',
				dataIndex : 'trainDetail.chargeBy'
			}]);
	con_item_cm.defaultSortable = true;

	var gridbbar = new Ext.PagingToolbar({
				pageSize : 18,
				store : con_ds,
				displayInfo : true,
				displayMsg : "显示第{0}条到{1}条，共{2}条",
				emptyMsg : "没有记录",
				beforePageText : '第'
//				afterPageText : ""
			});

	// tbar
	var contbar = new Ext.Toolbar({
		items : ['计划时间：', planDate, {
					id : 'query',
					iconCls : 'query',
					text : "查询",
					handler : init
				}, {
					id : 'gather',
					iconCls : 'query',
					text : "审批",
					handler : function() {
						if (con_ds.getTotalCount() == 0) {
							Ext.Msg.alert('提示', '无数据进行审批！');
							return;
						}
						// Ext.Msg.confirm('提示', '确认要进行审批吗？', function(id) {
						// if (id == 'yes') {
						// alert('进行审批函数，未做！')
						var url = "sign.jsp";
						var gatherId = con_ds.getAt(0).get("gatherId");
						var workFlowNo = con_ds.getAt(0).get("workflowNo");
						var args = new Object();
						args.entryId = workFlowNo;
						args.gatherId = gatherId;
						// args.workflowType = "bqDeptJobplan";
						var obj = window
								.showModalDialog(url, args,
										'status:no;dialogWidth=770px;dialogHeight=550px');
						if (obj) {
							init();
						}
//						con_ds.load({
//									params : {
//										approve : "",
//										start : 0,
//										limit : 18
//									}
//								})
					}
				}]
	});

	var title = "<center>培训计划</center>";

	var Grid = new Ext.grid.GridPanel({
				sm : con_sm,
				region : 'center',
				ds : con_ds,
				layout : 'fit',
				cm : con_item_cm,
				autoScroll : true,
				bbar : gridbbar,
				// tbar : contbar,
				title : title,
				// titleCollapse : true,
				// collapsible : true,
				border : true,
				viewConfig : {
					forceFit : false
				}
			});
	// Grid.on('rowclick', "");

			// modified by liuyi 20100428 移出初始化方法，否则点击一次就对con_ds加一个load事件
			con_ds.on("load", function() {
					if (con_ds.getTotalCount() > 0) {
						if (con_ds.getAt(0).get("workflowStatus") == 1) {
							Ext.get("gather").dom.disabled = false;
//							
						} else {
							Ext.get("gather").dom.disabled = true;
//							
						}
						//----------add by fyyang 20100524-----------
						if(planDate.getValue()==null||planDate.getValue()=="")
						{
						planDate.setValue(con_ds.getAt(0).get("trainingMonth"));
						}
						//----------add end--------------------------
					} else {
						Ext.get("gather").dom.disabled = true;
						if(planDate.getValue()==null||planDate.getValue()=="")
						{
						planDate.setValue(ChangeDateToString(startdate));
						}
					}
					
				var this_title = "<center><font color='black' size='2'>"
				+ planDate.getValue().substring(0, 4) + '年'
				+ planDate.getValue().substring(5, 7) + "月厂各部门月度培训计划</center>";
				
		          Grid.setTitle(this_title);
					
				})
	function init() {
//		con_ds.on("load", function() {
//					if (con_ds.getTotalCount() > 0) {
//						if (con_ds.getAt(0).get("workflowStatus") == 1) {
//							Ext.get("gather").dom.disabled = false;
////							status = "approve";
//						} else {
//							Ext.get("gather").dom.disabled = true;
////							status = "";
//						}
//					} else {
//						Ext.get("gather").dom.disabled = true;
////						status = "";
//					}
//				})
		// Ext.Ajax.request({
		// method : 'post',
		// url : 'manageplan/trainPlanGatherAfter.action',
		// params : {
		// planTime : planDate.getValue()
		// },
		// success : function(action) {
		// con_ds.on("load", function() {
		// alert(Ext.encode(con_ds.getAt(0).data))
		//
		// })
		// var result = eval("(" + action.responseText + ")");
		// if (result.list.length > 0) {
		// var str = Ext.encode(result.list);
		// var ob = eval("("
		// + str.substring(1, str.length - 1) + ")")
		// if (ob.workflowStatus == 1) {
		// Ext.get("gather").dom.disabled = false;
		// status = "approve";
		// } else {
		// Ext.get("gather").dom.disabled = true;
		// }
		//
		// } else {
		// Ext.get("gather").dom.disabled = true;
		// }
		queryRec();
		// }
		// });
	}
	init();
	new Ext.Viewport({
				autoHeight : true,
				layout : 'border',
				fitToFrame : true,
				items : [{
							region : 'north',
							items : [contbar],
							style : 'padding-bottom:0.8px'
						}, Grid]
			})
})