Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	
	var obj = window.dialogArguments;
	 var queryType=obj.queryType;
	   var strDate=obj.strDate;
	   var endDate=obj.endDate;
	    var type=obj.type;
							
	var w = screen.availWidth - 210;
	var currentPanel = "main";
	
	var searchFail = new Ext.data.Record.create([{
		name : 'id'
	}, {
		name : 'failureCode'
	}, {
		name : 'woStatusName'
	}, {
		name : 'failuretypeName'
	}, {
		name : 'failurePri'
	}, {
		name : 'attributeCode'
	}, {
		name : 'equName'
	}, {
		name : 'failureContent'
	}, {
		name : 'locationDesc'
	}, {
		name : 'findDate'
	}, {
		name : 'findBy'
	}, {
		name : 'findByName'
	}, {
		name : 'findDept'
	}, {
		name : 'findDeptName'
	}, {
		name : 'woCode'
	}, {
		name : 'bugCode'
	}, {
		name : 'bugName'
	}, {
		name : 'failuretypeCode'
	}, {
		name : 'failureLevel'
	}, {
		name : 'woStatus'
	}, {
		name : 'preContent'
	}, {
		name : 'ifStopRun'
	}, {
		name : 'ifStopRunName'
	}, {
		name : 'runProfession'
	}, {
		name : 'dominationProfession'
	}, {
		name : 'dominationProfessionName'
	}, {
		name : 'repairDep'
	}, {
		name : 'repairDepName'
	}, {
		name : 'installationCode'
	}, {
		name : 'installationDesc'
	}, {
		name : 'belongSystem'
	}, {
		name : 'belongSystemName'
	}, {
		name : 'likelyReason'
	}, {
		name : 'woPriority'
	}, {
		name : 'writeBy'
	}, {
		name : 'writeByName'
	}, {
		name : 'writeDept'
	}, {
		name : 'writeDeptName'
	}, {
		name : 'writeDate'
	}, {
		name : 'repairDept'
	}, {
		name : 'realrepairDept'
	}, {
		name : 'ifOpenWorkorder'
	}, {
		name : 'ifRepeat'
	}, {
		name : 'supervisor'
	}, {
		name : 'workFlowNo'
	}, {
		name : 'wfState'
	}, {
		name : 'entrepriseCode'
	}, {
		name : 'isuse'
	}, {
		name : 'isOverTime'
	}, {
		name : 'groupName'
	}, {
		name : 'delayDate'
	}]);
	var ds = new Ext.data.GroupingStore({
		proxy : new Ext.data.HttpProxy({
			url : 'bqfailure/queryListByType.action',
			method : 'post'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalCount',
			root : 'list'
		}, searchFail),
		groupField : 'groupName',
		sortInfo : '',
		remoteGroup : true
	});
	
	ds.on('beforeload', function() {
		
		Ext.apply(this.baseParams, {
			 queryType:queryType,
	         strDate :strDate,
	         endDate: endDate,
	         type:type
	});
	});
	ds.load({
		params : {
			start : 0,
			limit : 18
			
		}
	});
	/* 设置每一行的选择框 */
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			rowselect : function(sm, row, rec) {

			}
		}
	});

	var cm = new Ext.grid.ColumnModel([sm, new Ext.grid.RowNumberer({
		header : '序号',
		width : 35
	}), {
		header : "id",
		dataIndex : "id",
		hidden : true
	}, {
		header : '缺陷内容',
		dataIndex : 'failureContent',
		width : 150,
		renderer : function change(val) {
			return ' <span style="white-space:normal;">' + val + ' </span>';
		}
	}, {
		header : '检修部门',
		dataIndex : 'repairDepName',
		width : 120
	}, {
		header : '管辖专业',
		dataIndex : 'dominationProfessionName',
		width : 100
	}, {
		header : '状态',
		dataIndex : 'woStatusName',
		width : 120
	}, {
		header : '填写时间',
		dataIndex : 'writeDate',
		width : 120
	},  {
		header : '批准延期时间',
		dataIndex : 'delayDate',
		width : 120
	},{
		header : '发现人',
		dataIndex : 'findByName',
		width : 60
	}, {
		header : '发现部门',
		dataIndex : 'findDeptName',
		width : 120
	}, {
		header : '缺陷编码',
		dataIndex : 'failureCode',
		width : 100
	}, {
		header : '所属系统',
		dataIndex : 'belongSystemName',
		width : 100
	}, {
		header : '发现时间',
		dataIndex : 'findDate',
		width : 120
	}, {
		header : '类别',
		dataIndex : 'failuretypeName',
		width : 60
	}, {
		header : '优先级',
		dataIndex : 'failurePri',
		width : 140
	},
			{
				dataIndex : 'workFlowNo',
				hidden : true
			}, {
				header : '类别',
				dataIndex : 'groupName',
				align : 'left',
				hidden : true,
				width : 120
			}
			
	]);
	
	/* 设置分页的工具条 */
	var bbar = new Ext.PagingToolbar({
		pageSize : 18,
		store : ds,
		displayInfo : true,
		displayMsg : '显示第 {0} 条到 {1} 条记录，一共 {2} 条',
		emptyMsg : "没有记录"
	})


	
	
	function queryWF() {
		var rec = grid.getSelectionModel().getSelected();
		if (rec) {
			if (rec.get("workFlowNo") != 0) {
				if (rec.get("workFlowNo") != "") {
					window
							.open("../../../../workflow/manager/show/show.jsp?entryId="
									+ rec.get("workFlowNo"));
				}
			} else {
				var flowCode = "bqFailure";
				url = "/power/workflow/manager/flowshow/flowshow.jsp?flowCode="
						+ flowCode;
				window.open(url);
			}
		} else {
			Ext.Msg.alert('提示', '请从列表中选择一条缺陷！');
		}
	};
	var tbar = new Ext.Toolbar({
		id : 'thirdTbar',
//		renderTo : grid.tbar,
		items : [
				{
					id : 'query',
					text : "查看流程",
					iconCls : 'view',
					handler : queryWF
				},
				'-',
				{
					id : 'query',
					iconCls : 'list',
					text : "查看缺陷信息<font color='red'><双击显示详细信息></font>",
					handler : function() {
						var rec = grid.getSelectionModel().getSelected();
						if (rec) {
							Ext.Ajax.request({

								url : 'bqfailure/findFailureById.action?failure.id='
										+ rec.get("id"),
								method : 'post',
								waitMsg : '正在处理数据...',
								success : function(result, request) {
									var json = eval('(' + result.responseText
											+ ')');
								 var url = "../../../../equ/bqfailure/query/"+json.msg+"";	
									window
											.showModalDialog(
													url,
													json,
													'dialogWidth=860px;dialogHeight=500px;center=yes;help=no;resizable=no;status=no;');
								},
								failure : function(result, request) {
									alert('打开失败！');
								}
							});
						} else {
							alert('请选择一条记录!');
						}
					}
				}]
	});
	
//   queryIt() ;
	/* 创建表格 */
	var grid = new Ext.grid.GridPanel({
		id : 'gridid',
		ds : ds,
		cm : cm,
		autoScroll : true,
		// autoWidth : true,
		width : 200,
		sm : sm,
		bbar : bbar,
		tbar : tbar,
		view : new Ext.grid.GroupingView({
			forceFit : false,
			enableGroupingMenu : false,
			hideGroupedColumn : true,
			showGroupName : false,
			startCollapsed : false,
			groupTextTpl : '{text}'
		}),
		border : false
	});

	grid.getView().getRowClass = function(record, index) {
		return (record.data.isOverTime == 'Y' ? 'red-row' : 'black-row');
	};
	grid.on('rowdblclick', function(grid, rowIndex, e) {
		var rec = grid.getSelectionModel().getSelected();
		if (rec) {
			Ext.Ajax.request({

				url : 'bqfailure/findFailureById.action?failure.id='
						+ rec.get("id"),
				method : 'post',
				waitMsg : '正在处理数据...',
				success : function(result, request) {
					var json = eval('(' + result.responseText + ')');
					
					 var url = "../../../../equ/bqfailure/query/"+json.msg+"";
					window
							.showModalDialog(
									url,
									json,
									'dialogWidth=860px;dialogHeight=500px;center=yes;help=no;resizable=no;status=no;scrollbars:yes;');
				},
				failure : function(result, request) {
					Ext.MessageBox.alert('打开失败！');
				}
			});
		}
	});
	


	
	
	
  	new Ext.Viewport({
		 layout : "fit",
		items : [grid]
	});
	
	
});