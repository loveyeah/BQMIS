function getYear() {
	var d, s, t;
	d = new Date();
	s = d.getFullYear().toString(10);
	return s;
}
Ext.onReady(function() {
        
	var version=null;
	var tasklistId=null;
	
		Ext.form.Field.prototype.msgTarget = 'title';

		var projectMainYear = new Ext.form.TextField({
			id : 'projectMainYear',
			fieldLabel : '年度',
			readOnly : true,
			width : 60,
			value : getYear(),
			listeners : {
				focus : function() {
					WdatePicker({
						startDate : '%y',
						alwaysUseStartDate : false,
						dateFmt : 'yyyy',
						isShowClear : false,
						onpicked : function(v) {
							this.blur();
						}
					});
				}
			}
		});

		
	var versionStore = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'manageplan/getRepairVerisonList.action'
		}),
		reader : new Ext.data.JsonReader({
			root : 'list'
		}, [{
			name : 'version',
			mapping : 0
		}, {
			name : 'versionName',
			mapping : 1
		}])
	});
	
	versionStore.on('beforeload', function() {
						this.baseParams = {
							tasklistId : tasklistId
						};
					},versionStore);  
	
	var cbxVersion = new Ext.form.ComboBox({
		id : 'cbxVersion',
		fieldLabel : "版本",
		store : versionStore,
		displayField : "versionName",
		valueField : "version",
		hiddenName : 'version',
		mode : 'remote',
		triggerAction : 'all',
		value : '',
		readOnly : true,
		disabled:true,
		width : 120
	})

		// 定义选择行
		var sm = new Ext.grid.RowSelectionModel({
			singleSelect : true
		});

		// grid中的数据Record
		var gridRecord = new Ext.data.Record.create([{
				name : 'tasklistId',
				mapping:0
			}, {
				name : 'tasklistYear',
				mapping:1
			}, {
				name : 'tasklistName',
				mapping:2
			}, {
				name : 'entryBy',
				mapping:3
			}, {
				name : 'entryByName',
				mapping:4
			}, {
				name : 'entryDate',
				mapping:5
			}]);

		// grid的store
		var queryStore = new Ext.data.JsonStore({
			url : 'manageplan/getRepairTask.action',
			root : 'list',
			totalProperty : 'totalCount',
			fields : gridRecord
		});

		// 分页工具栏
		var pagebar = new Ext.PagingToolbar({
			pageSize : 18,
			store : queryStore,
			displayInfo : true,
			displayMsg : "一共 {2} 条",
			emptyMsg : "没有记录"
		});

		// 页面的Grid主体
		var gridOrder = new Ext.grid.GridPanel({
			region : 'north',
			layout : 'column',
			store : queryStore,
			height : 200,
			columns : [new Ext.grid.RowNumberer({
				header : '行号',
				width : 35
			}), {
				header : "ID",
				hidden : true,
				width : 100,
				dataIndex : 'projectMainId'
			},{
				header : "检修任务单名称",
				width : 300,
				dataIndex : 'tasklistName'
			}, {
				header : "填写人",
				width : 150,
				dataIndex : 'entryByName'
			}, {
				header : "填写日期",
				sortable : true,
				width : 150,
				dataIndex : 'entryDate'
			}],
			tbar : [{
				text : '年度'
			}, projectMainYear, {
				id : 'btnSubmit',
				text : "查询",
				iconCls : 'query',
				handler : findFuzzy
			}],
			bbar : pagebar,
			sm : sm,
			frame : false,
			border : false,
			enableColumnMove : false
		});

		gridOrder.on('rowclick', showDetail);
		gridOrder.on('render', function() {
			gridFresh();
		});

		var detailRecord = new Ext.data.Record.create([{
			name : 'projectDetailId',
			mapping : 0
		}, {
			name : 'projectMainId',
			mapping : 1
		}, {
			name : 'repairProjectId',
			mapping : 2
		}, {
			name : 'repairProjectName',
			mapping : 3
		}, {
			name : 'workingCharge',
			mapping : 4
		}, {
			name : 'workingChargeName',
			mapping : 5
		}, {
			name : 'workingMenbers',
			mapping : 6
		}, {
			name : 'startDate',
			mapping : 7
		}, {
			name : 'endDate',
			mapping : 8
		}, {
			name : 'standard',
			mapping : 9
		}, {
			name : 'material',
			mapping : 10
		}, {
			name : 'situation',
			mapping : 11
		}, {
			name : 'reason',
			mapping : 12
		}, {
			name : 'workflowNo',
			mapping : 13
		}, {
			name : 'workflowStatus',
			mapping : 14
		}, {
			name : 'specialityName',
			mapping : 15
		}]);
		var detailStore = new Ext.data.JsonStore({
			url : 'manageplan/getRepairQueryList.action',
			root : 'list',
			totalProperty : 'totalCount',
			fields : detailRecord
		});
		
       var detailSm = new Ext.grid.RowSelectionModel({
			singleSelect : true
		}); 
		
		var materialGrid = new Ext.grid.GridPanel({
			region : 'center',
			border : false,
			autoScroll : true,
			enableColumnMove : false,
			store : detailStore,
			sm : detailSm,
			columns : [new Ext.grid.RowNumberer({
				header : "行号",
				width : 35
			}), {
				header : '明细ID',
				dataIndex : 'projectDetailId',
				align : 'center',
				hidden : true
			}, {
				header : '主表ID',
				dataIndex : 'projectMainId',
				align : 'center',
				hidden : true
			},  {
				header : '专业',
				dataIndex : 'specialityName',
				align : 'left',
				width : 90,
		renderer : function(value, matedata, record, rowIndex, colIndex, store) {
			if (record && rowIndex > 0) {
				if (detailStore.getAt(rowIndex).get('specialityName') == detailStore
						.getAt(rowIndex - 1).get('specialityName')
						|| detailStore.getAt(rowIndex).get('specialityName') == '')
					return '';
			}
			return value;
		}
			}, {
				header : '项目ID',
				dataIndex : 'repairProjectId',
				align : 'center',
				hidden : true
			}, {
				header : '项目名称',
				dataIndex : 'repairProjectName',
				align : 'left',
				width : 120
			}, {
				header : '工作负责人',
				dataIndex : 'workingChargeName',
				width : 70,
				align : 'center'
			}, {
				header : '工作成员',
				dataIndex : 'workingMenbers',
				align : 'center'
			}, {
				header : '计划开始时间',
				dataIndex : 'startDate',
				align : 'center'
			}, {
				header : '计划结束时间',
				dataIndex : 'endDate',
				align : 'center'
			}, {
				header : '验收标准',
				dataIndex : 'standard',
				align : 'center',
				renderer : function(v)
				{
					if(v=='2')
					{
						return '二级';
					}else{
						return '三级';
					}
				}
			}, {
				header : '是否落实材料',
				dataIndex : 'material',
				align : 'center',
				renderer : function(v) {
					if (v == 'Y') {
						return '是';
					} else {
						return '否';
					}
				}
			}, {
				header : '落实材料情况',
				dataIndex : 'situation',
				align : 'center',
				renderer : function(v) {
					if (v == 'Y') {
						return '是';
					} else {
						return '否';
					}
				}
			}, {
				header : '未落实原因',
				dataIndex : 'reason',
				align : 'center'
			}],
			tbar : [{
				text : '版本'
			},cbxVersion,  '-',{
				id : 'btnSubmit',
				text : "查询",
				disabled:true,
				iconCls : 'query',
				handler : queryDetail
			}, '-', {
							id : 'btnapprove',
							text : "审批信息",
				            disabled:true,
							iconCls : 'view',
							handler : approveQuery
			}, '-', {
							id : 'btnExport',
							iconCls : 'export',
							text : '导出',
							handler : exportRecord
						}]
		});

	// 导出
	function tableToExcel(tableHTML) {
		window.clipboardData.setData("Text", tableHTML);
		try {
			var ExApp = new ActiveXObject("Excel.Application");
			var ExWBk = ExApp.workbooks.add();
			var ExWSh = ExWBk.worksheets(1);
			ExApp.DisplayAlerts = false;
			ExApp.visible = true;
		} catch (e) {
			if (e.number != -2146827859)
				Ext.Msg.alert('提示信息', "您的电脑没有安装Microsoft Excel软件！");
			return false;
		}
		ExWBk.worksheets(1).Paste;
	}
	function exportRecord() {
		if(detailStore.getCount() == 0){
			Ext.Msg.alert('提示','无数据进行导出！');
			return;
		}
		Ext.Msg.confirm('提示', '确认要导出数据？', function(id) {
			if (id == 'yes') {
				
				var title = "";
					title = "检修计划查询报表";
				var tableHeader = "<table border=1><tr><th colspan = 10>"+title+"</th></tr>";
				var html = [tableHeader];
				html
						.push("<tr><th colspan = 4>版本</th><td colspan = 6>"+cbxVersion.getValue()+"</td></tr>")
			
				html
						.push("<tr><th>专业</th><th>项目名称</th><th>工作负责人</th><th>工作成员" +
								"</th><th>计划开始时间</th><th>计划结束时间</th><th>验收标准</th><th>" +
								"是否落实材料</th><th>落实材料情况</th><th>未落实原因</th></tr>")

				var rec = detailStore.getAt(0);
					html.push('<tr><td>' + rec.get('specialityName')
							+ '</td><td>' + (rec.get('repairProjectName')==null?"":rec.get('repairProjectName'))
							+ '</td><td>' + (rec.get('workingChargeName')==null?"":rec.get('workingChargeName'))
							+ '</td><td>' + (rec.get('workingMenbers')==null?"":rec.get('workingMenbers'))
							+ '</td><td>' + (rec.get('startDate')==null?"":rec.get('startDate'))
							+ '</td><td>' + (rec.get('endDate')==null?"":rec.get('endDate'))
							+ '</td><td>' + (rec.get('standard')=='2'?"二级":"三级")
							+ '</td><td>' + (rec.get('material')=='Y'?"是":"否")
							+ '</td><td>' + (rec.get('situation')=='Y'?"是":"否")
							+ '</td><td>' + (rec.get('reason')==null?"":rec.get('reason')) + '</td></tr>')				
								
				for (var i = 1; i < detailStore.getCount(); i++) {
					var rec = detailStore.getAt(i);
					var recs = detailStore.getAt(i-1);
					
					html.push('<tr><td>' + (recs.get('specialityName')==rec.get('specialityName')?"":rec.get('specialityName'))
							+ '</td><td>' + (rec.get('repairProjectName')==null?"":rec.get('repairProjectName'))
							+ '</td><td>' + (rec.get('workingChargeName')==null?"":rec.get('workingChargeName'))
							+ '</td><td>' + (rec.get('workingMenbers')==null?"":rec.get('workingMenbers'))
							+ '</td><td>' + (rec.get('startDate')==null?"":rec.get('startDate'))
							+ '</td><td>' + (rec.get('endDate')==null?"":rec.get('endDate'))
							+ '</td><td>' + (rec.get('standard')=='2'?"二级":"三级")
							+ '</td><td>' + (rec.get('material')=='Y'?"是":"否")
							+ '</td><td>' + (rec.get('situation')=='Y'?"是":"否")
							+ '</td><td>' + (rec.get('reason')==null?"":rec.get('reason')) + '</td></tr>')
				}
				
				html.push('</table>');
				html = html.join(''); // 最后生成的HTML表格
				tableToExcel(html);
			
			}
		})
	}	
		
		new Ext.Viewport({
			layout : 'border',
			margins : '0 0 0 0',
			border : false,
			items : [gridOrder, materialGrid]
		});

		function findFuzzy(start) {
			gridFresh();
			cbxVersion.setDisabled(true);
			Ext.getCmp("btnSubmit").setDisabled(true);
			Ext.getCmp("btnapprove").setDisabled(true);
			detailStore.removeAll();
		}
		
		function queryDetail(){
		  if (gridOrder.getSelectionModel().getSelected() != null) {
				var record = gridOrder.getSelectionModel().getSelected();
				tasklistId=record.get('tasklistId');
				
				 detailStore.load({
					params : {
						tasklistId :tasklistId,
						version : cbxVersion.getValue()
					}
				});
				}
		 else{
		   Ext.Msg.alert('提示', '请选择一条检修任务单记录！');
		 }
		}
		
		function approveQuery() {
		if (!detailSm.hasSelection() || detailSm.getSelections().length > 1)
			Ext.Msg.alert('提示', '请选择一条数据进行查看！');
		else {
			var rec = detailSm.getSelected();
			var workflowType = "bqRepairPlanApprove";
			var entryId = rec.get('workflowNo');
			var url;
			if (entryId == null || entryId == "")
				url = "/power/workflow/manager/flowshow/flowshow.jsp?flowCode="
						+ workflowType;
			else
				url = url = "/power/workflow/manager/show/show.jsp?entryId="
						+ entryId;
			window.open(url);

		}
	}


		/**
		 * 当单击主grid中一行时，下面的grid显示详细信息
		 */
		function showDetail() {
			
			if (gridOrder.getSelectionModel().getSelected() != null) {
				
				var record = gridOrder.getSelectionModel().getSelected();
				tasklistId=record.get('tasklistId');
				versionStore.load();
				
				Ext.Ajax.request({
			url : 'manageplan/getRepairMaxVersionMain.action',
					method : 'post',
					params : {
					         tasklistId : record.get('tasklistId')
					},
					success :function(response,options) {
						var res = response.responseText;
						
						if(res.toString() == '')
						{
						 cbxVersion.setValue("");
						 cbxVersion.setDisabled(true);
						 Ext.getCmp("btnSubmit").setDisabled(true);
						 Ext.getCmp("btnapprove").setDisabled(true);
                        	detailStore.removeAll();
						} else {
								version = res.toString();
								if(version=='0')
								cbxVersion.setValue("初稿");
								else 
								cbxVersion.setValue("第" + version+ "版本");
								cbxVersion.enable();
								Ext.getCmp("btnSubmit").enable();
								Ext.getCmp("btnapprove").enable();
								detailStore.load({
													params : {
														tasklistId : record
																.get('tasklistId'),
														version : cbxVersion
																.getValue()
													}
												});
									}
					}
				});
				
			}
		}

		function gridFresh() {
			cbxVersion.setValue("");
			queryStore.baseParams = {
				year : projectMainYear.getValue()
			};
			queryStore.load({
				params : {
					start : 0,
					limit : 18
				}
			});
		}

});