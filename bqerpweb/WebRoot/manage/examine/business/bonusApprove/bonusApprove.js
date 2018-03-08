Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	Ext.ns("CashApprove.list");
	
	var queryYearMonth = null;
	
	CashApprove.list = function() {
		function getYear() {
			var d, s, t;
			d = new Date();
			s = d.getFullYear().toString();
			return s;
		}

		// 定义grid
		var MyRecord = Ext.data.Record.create([{
			name : 'declareId'
		}, {
			name : 'yearMonth'
		}, {
			name : 'workflowNo'
		}, {
			name : 'status'
		}, {
			name : 'lastModifyName'
		}, {
			name : 'lastModifyDate'
		}]);

		var dataProxy = new Ext.data.HttpProxy({
			url : 'managexam/findAwardMainApproveList.action'
		});

		var theReader = new Ext.data.JsonReader({
			root : "list",
			totalProperty : "totalCount"
		}, MyRecord);

		var store = new Ext.data.Store({
			proxy : dataProxy,
			reader : theReader
		});
		// 分页

		var findingMonth = new Ext.form.TextField({
			style : 'cursor:pointer',
			id : 'findingMonth',
			columnWidth : 0.5,
			readOnly : true,
			anchor : "60%",
			fieldLabel : '年度',
			name : 'month',
			value : getYear(),
			listeners : {
				focus : function() {
					WdatePicker({
						startDate : '%y',
						dateFmt : 'yyyy',
						alwaysUseStartDate : true
					});
					this.blur();
				}
			}
		});

		var btnSign = new Ext.Toolbar.Button({
			id : 'btnSign',
			text : "签字处理",
			iconCls : 'update',
			handler : edit
		});

		var sm = new Ext.grid.CheckboxSelectionModel();

		var grid = new Ext.grid.GridPanel({
			region : "center",
			store : store,

			columns : [sm, new Ext.grid.RowNumberer({
				header : '序号',
				width : 35,
				align : 'center'
			}), {

				header : "ID",
				width : 100,
				sortable : true,
				dataIndex : 'declareId',
				hidden : true
			}, {

				header : "月份",
				width : 100,
				sortable : true,
				dataIndex : 'yearMonth'
			}, {

				header : "审批状态",
				width : 80,
				sortable : true,
				dataIndex : 'status',
				renderer : function(v) {
					if (v == 0) {
						return "未上报";
					}
					if (v == 1) {
						return "审批中";
					}
					if (v == 2) {
						return "审批结束";
					}
					if (v == 3) {
						return "退回";
					}
					return v;
				}
			}, {
				header : "修改人",
				width : 100,
				sortable : true,
				dataIndex : 'lastModifyName',
				align : 'center'
			}, {
				header : "修改日期",
				width : 100,
				sortable : true,
				dataIndex : 'lastModifyDate',
				align : 'center'
			}],
			sm : sm,
			tbar : ['年度: ', findingMonth, {
				text : "查询",
				iconCls : 'query',
				handler : queryRecord
			}, '-', btnSign],
			// 分页
			bbar : new Ext.PagingToolbar({
				pageSize : 18,
				store : store,
				displayInfo : true,
				displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
				emptyMsg : "没有记录"
			})
		});

		grid.on('rowdblclick', edit);

		/**
		 * 双击grid事件
		 */
		function edit() {
			var rec = grid.getSelectionModel().getSelected();
			if (rec) {
				tabs.setActiveTab(1);
				if(rec.get("yearMonth") != queryYearMonth)
				{
					queryYearMonth = rec.get("yearMonth");
					detail.reload(queryYearMonth);
				}
			} else {
				Ext.Msg.alert('提示', '请从列表中选择一条记录！');
			}
		}

		function queryRecord() {
			store.load({
				params : {
					start : 0,
					limit : 18,
					month : findingMonth.getValue()
				}
			});
		}
		return {
			grid : grid,
			edit: edit
		}

		queryRecord();
		
	};

	// 奖金申报信息
	Ext.ns("CashApprove.detail");
	CashApprove.detail = function() {
		
		var currentId = null;
		
		var selct_mod = new Ext.grid.CheckboxSelectionModel({
			singleSelect : true
		});
		var MyRecord = Ext.data.Record.create([{
			name : 'itemName'
		}, {
			name : 'unitName'
		}, {
			name : 'planValue'
		}, {
			name : 'realValue'
		}, {
			name : 'complete'
		}, {
			name : 'affiliatedDept'
		}, {
			name : 'affiliatedValue'
		}, {
			name : 'affiliatedLevel'
		}, {
			name : 'cash'
		}, {
			name : 'memo'
		}, {
			name : 'affiliatedId'
		}, {
			name : 'declarDetailId'
		}]);
		var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
				header : '序号',
				width : 35,
				align : 'center'
			}),{
			header : '指标名称',
			dataIndex : 'itemName',
			width : 150,
			align : 'left'
		}, {
			header : '挂靠部门',
			dataIndex : 'affiliatedDept',
			width : 150,
			align : 'left'
		}, {
			header : '挂靠标准',
			dataIndex : 'affiliatedValue',
			width : 150,
			align : 'left'
		}, {
			header : '兑现奖金',
			dataIndex : 'cash',
			width : 150,
			align : 'left',
			editor : new Ext.form.NumberField({
				allowDecimals : true,
				decimalPrecision : 4
			})
		}, {
			header : '备注',
			dataIndex : 'memo',
			width : 200,
			align : 'left',
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : '计量单位',
			dataIndex : 'unitName',
			width : 100,
			align : 'left'
		}, {
			header : '计划值',
			dataIndex : 'planValue',
			width : 100,
			align : 'left'
		}, {
			header : '实际值',
			dataIndex : 'realValue',
			width : 100,
			align : 'left'
		}, {
			header : '完成情况',
			dataIndex : 'complete',
			width : 100,
			align : 'left'
		}, {
			header : '挂靠级别',
			dataIndex : 'affiliatedLevel',
			width : 100,
			align : 'left'
		}]);

		var sel_ds = new Ext.data.Store({
			proxy : new Ext.data.HttpProxy({
				url : 'managexam/getAwardList.action'
			}),
			reader : new Ext.data.JsonReader({}, MyRecord)
		});
		
		var postType = 'real';

		function savedata() {
			Grid.stopEditing();
			var changeRec = Grid.getStore().getModifiedRecords();
			if (changeRec.length == 0)
				Ext.Msg.alert('提示信息', "没有作任何操作!");
			else if (changeRec.length >= 0)
				if (!confirm("确定要保存修改吗?"))
					return;
			// var newData = new Array();
			var updateData = new Array();
			for (var i = 0; i < changeRec.length; i++) {

				updateData.push(changeRec[i].data);
				// }
			}
			Ext.Ajax.request({
				url : 'managexam/saveAward.action',
				method : 'post',
				params : {
					UpdateRecords : Ext.util.JSON.encode(updateData),
					type : postType,
					datetime : findingMonth.getValue()
				},
				success : function(result, request) {
					Ext.Msg.alert('提示信息', "操作成功!");
					sel_ds.rejectChanges();
					sel_ds.reload();
				},
				failure : function(result, request) {
					Ext.Msg.alert('提示信息', '操作失败')
				}
			})
		}
		function getMonth() {
			var d, s, t;
			d = new Date();
			s = d.getFullYear().toString(10) + "-";
			t = d.getMonth() + 1;
			s += (t > 9 ? "" : "0") + t;
			return s;
		}
		var findingMonth = new Ext.form.TextField({
			style : 'cursor:pointer',
			id : 'findingMonth',
			columnWidth : 0.5,
			readOnly : true,
			anchor : "70%",
			fieldLabel : '月份',
			name : 'month',
			value : getMonth(),
			listeners : {
				focus : function() {
					WdatePicker({
						startDate : '%y-%M',
						dateFmt : 'yyyy-MM',
						alwaysUseStartDate : true
					});
					this.blur();
				}
			}
		});

		var selbar = new Ext.Toolbar({
			items : [{
				id : 'btnSave',
				iconCls : 'save',
				text : "保存",
				handler : savedata
			}, '-', {
				id : 'btnupdata',
				iconCls : 'write',
				text : "签字",
				handler : reportRecord
			}]
		});
		
		function reportRecord() {
			var record = list.grid.getSelectionModel().getSelected();
			var workFlowNo = record.get('workflowNo');
			Ext.Ajax.request({
				url : 'MAINTWorkflow.do?action=getCurrentStepsInfo',
				method : 'POST',
				params : {
					entryId : workFlowNo
				},
				success : function(result, request) {
					var url = "";
					var obj = eval("(" + result.responseText + ")");
					var args = new Object();
					if (obj[0].url == null || obj[0].url == "") {
						url = "sign.jsp";
					} else {
						url = "../../../../../" + obj[0].url;
					}
					args.month = record.get('yearMonth');
					args.entryId = record.get("workflowNo");
					args.declareId = record.get("declareId");

					var obj = window.showModalDialog(url, args,
							'status:no;dialogWidth=750px;dialogHeight=500px');

					list.grid.getStore().reload();
					tabs.setActiveTab(0);
					// sel_ds.load();
				},
				failure : function(result, request) {
					Ext.Msg.alert("提示", "错误");
				}
			});
		}
		
		cm.defaultSortable = false;
		var Grid = new Ext.grid.EditorGridPanel({
			viewConfig : {
				forceFit : false
			},
			sm : selct_mod,
			clicksToEdit : 1,
			ds : sel_ds,
			cm : cm,
			height : 425,
			split : true,
			autoScroll : true,
			layout : 'fit',
			frame : false,
			tbar : selbar,
			border : true
		});
		return {
			grid : Grid,
			reload:function(dt){
				sel_ds.load({
					params : {
						datetime : dt
					}
				});
			}
		}
	}

	var list = new CashApprove.list();
	var detail = new CashApprove.detail();
	var tabs = new Ext.TabPanel({
		activeTab : 0,
		tabPosition : 'bottom',
		plain : true,
		defaults : {
			autoScroll : true
		},
		items : [{
			id : 'deptMaint',
			title : '奖金申报审批列表',
			autoScroll : true,
			items : list.grid,
			layout : 'fit'
		}, {
			id : 'itemMaint',
			title : '奖金申报信息',
			items : detail.grid,
			autoScroll : true,
			layout : 'fit',
			listeners:{
				activate:function(tab){
					var rec = list.grid.getSelectionModel().getSelected();
					if(!rec)
					{
						Ext.Msg.alert("提示","请选择一行记录!");
						tabs.setActiveTab(0);
					}
					else{
						list.edit();
					}
				}
			}
		}]
	});
	var view = new Ext.Viewport({
		layout : 'fit',
		items : [tabs]
	}); 
});
