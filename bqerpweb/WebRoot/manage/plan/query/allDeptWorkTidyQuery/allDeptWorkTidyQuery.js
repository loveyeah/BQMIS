Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {

	// 工作流序号
	var workFlowNo;
	// 工作流状态
	var signStatus;
	// 计划时间(部门工作计划汇总表主键)
	// var planTime;

	function getDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t
		return s;
	}
	function init() {
				con_ds.baseParams = {
								planTime : planTime.getValue(),
								// ,
								//部门计划审批结束
								 approve : 'signStatus'
							}
				con_ds.load({
										params : {
										// start : 0,
										// limit : 18
										}
									})
						}
//		Ext.lib.Ajax.request('POST', 'manageplan/getBpJPlanJobMain.action', {
//					success : function(action) {
//						var result = eval("(" + action.responseText + ")");
//						if (result != null) {
//							workFlowNo = result.workFlowNo;
//							signStatus = result.signStatus;
//							if (signStatus == '2') {
//								Ext.getCmp('btnapprove').setDisabled(true)
//							} else {
//								Ext.getCmp('btnapprove').setDisabled(false)
//							}
//							con_ds.baseParams = {
//								planTime : planTime.getValue()
//								// ,
//								// approve : 'Y'
//							}
//							con_ds.load({
//										params : {
//										// start : 0,
//										// limit : 18
//										}
//									})
//						}
//					}
//				}, 'planTime=' + planTime.getValue());
//	}

	/** 左边的grid * */
	var con_item = Ext.data.Record.create([{
				name : 'planTime',
				mapping : 0
			}, {
				name : 'editByName',
				mapping : 1
			}, {
				name : 'editDate',
				mapping : 2
			}, {
				name : 'depMainId',
				mapping : 3
			}, {
				name : 'depName',
				mapping : 4
			}, {
				name : 'jobId',
				mapping : 5
			}, {
				name : 'jobContent',
				mapping : 6
			}, {
				name : 'completeDate',
				mapping : 7
			}, {
				name : 'workFlowNo',
				mapping : 8
			}, {
				name : 'completeChar',
				mapping : 9
			}]);

	var con_sm = new Ext.grid.CheckboxSelectionModel({

	});
	var con_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'manageplan/getBpPlanDeptModList.action'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : "totalCount",
							root : "list"
						}, con_item)
			});

	var con_item_cm = new Ext.grid.ColumnModel([con_sm,

	{
		header : '部门',
		dataIndex : 'depName',
		align : 'center',
		renderer : function(value, matedata, record, rowIndex, colIndex, store) {
			if (record && rowIndex > 0) {
				if (store.getAt(rowIndex).get('depName') == store
						.getAt(rowIndex - 1).get('depName')
						|| store.getAt(rowIndex).get('depName') == '')
					return '';
			}
			return value;
		}

	}, {
		header : '工作内容',
		dataIndex : 'jobContent',
		width : 500,
		sortable : false,
		align : 'left'

	}, {
		header : '完成时间',
		dataIndex : 'completeDate',
		align : 'center',
		sortable : false,
		renderer : function(v) {
			if (v == '0') {
				return '当月';
			} else if (v == '1') {
				return '跨越';
			} else if (v == '2') {
				return '长期';
			} else if (v == 3) {
				return '全年';
			} else
				return '';
		}

	}]);
	con_item_cm.defaultSortable = true;

	var gridbbar = new Ext.PagingToolbar({
				pageSize : 18,
				store : con_ds,
				displayInfo : true,
				displayMsg : "显示第{0}条到{1}条，共{2}条",
				emptyMsg : "没有记录",
				beforePageText : '',
				afterPageText : ""
			});
	// 计划时间
	var planTime = new Ext.form.TextField({
				name : 'planTime',
				fieldLabel : '计划时间',
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '',
									alwaysUseStartDate : true,
									dateFmt : "yyyy-MM",
									isShowClear : false
								});
						this.blur();
					}
				}

			});
	planTime.setValue(getDate());

	function queryFun() {
		init();
	}
	var query = new Ext.Button({
				text : '查询',
				id : 'butQuery',
				iconCls : 'query',
				handler : queryFun

			})

	var contbar = new Ext.Toolbar({
				items : [planTime, query,
//				{
//							text : "审批查询",
//							id : 'btnapprove',
//							iconCls : 'approve',
//							handler : approve
//						},
						'', {
							text : '导出',
							id : 'btnExport',
							iconCls : 'export',
							handler : function() {
								myExport();
							}
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
				alert("您的电脑没有安装Microsoft Excel软件！");
			return false;
		}
		ExWBk.worksheets(1).Paste;
	}

	function myExport() {
		// Ext.Ajax.request({
		// url : 'manageplan/getBpPlanDeptModList.action?planTime='
		// + planTime.getValue(),
		// success : function(response) {
		// var json = eval('(' + response.responseText.trim() + ')');
		// var records = json.list;
		var html = ['<table border=0 style="font-size:5.55mm; FONT-FAMILY: 仿宋_GB2312">'];
		for (var i = 0; i < con_ds.getCount(); i++) {
			var rec = con_ds.getAt(i).copy();
			rec.set('jobContent', rec.get('jobContent') == null ? "" : rec
							.get('jobContent'));
			if (rec.get('completeDate') != null) {
				if (rec.get('completeDate') == "0")
					rec.set('completeDate', "当月");
				if (rec.get('completeDate') == "1")
					rec.set('completeDate', "跨越");
				if (rec.get('completeDate') == "2")
					rec.set('completeDate', "长期");
				if (rec.get('completeDate') == "3")
					rec.set('completeDate', "全年");
			} else {
				rec.set('completeDate', "");
			}
			rec.set('depName', rec.get('depName') == null ? "" : rec
							.get('depName'));
			if (i > 0) {
				if (con_ds.getAt(i).get('depName') == con_ds.getAt(i - 1)
						.get('depName')) {
					html.push('<tr><td align ="left" colspan="6" >'
							+ rec.get('jobContent') + '</td>'
							+ '<td  colspan="2" align = "center" >' + rec.get('completeDate')
							+ '</td>' + '</tr>')
				} else {
					html.push('<tr style="font-size:5.55mm; FONT-FAMILY: 方正仿宋简体"><th  colspan ="8">' + rec.get('depName')
							+ '</th></tr>')
					html
							.push('<tr ><th align = "left" colspan="6">序号</th><th colspan="2">完成时间</th></tr>')
				}
			} else {
				html.push('<tr style="font-size:5.55mm; FONT-FAMILY: 方正仿宋简体"><th colspan ="8">' + rec.get('depName')
						+ '</th></tr>')
			html
							.push('<tr><th align = "left" colspan="6">序号</th><th colspan="2">完成时间</th></tr>')
				html.push('<tr><td align ="left" colspan="6">'
						+ rec.get('jobContent') + '</td>'
						+ '<td  colspan="2" align = "center">' + rec.get('completeDate')
						+ '</td>' + '</tr>')
			}
		}

		// for (var i = 0; i < records.length; i += 1) {
		// var rc = records[i];
		// if (i > 0) {
		// if (records[i - 1][4] == records[i][4]) {
		// records[i][4] = "";
		// }
		// }
		// html.push('<tr><td align ="left">' + rc[4]
		// + '</td><td align ="left" colspan="3">' + rc[6]
		// + '</td><td align ="center">' + rc[9]
		// + '</td></tr>');
		// }
		html.push('</table>');
		html = html.join(''); // 最后生成的HTML表格
		tableToExcel(html);
		// },
		// failure : function(response) {
		// Ext.Msg.alert('信息', '失败');
		// }
		// });
	}

	// var export
	var Grid = new Ext.grid.GridPanel({
				sm : con_sm,
				ds : con_ds,
				cm : con_item_cm,
				height : 425,
				autoScroll : true,
				// bbar : gridbbar,
				tbar : contbar,
				border : true
			});

	// 签字
	function approve() {
		var rec = Grid.getSelectionModel().getSelected();
		if (rec != null) {
			var entryId = rec.get("workFlowNo");
			if (entryId == null || entryId == "") {
				url = "/power/workflow/manager/flowshow/flowshow.jsp?flowCode="
						+ "bqDeptJobplan";
				window.open(url);
			} else {
				var url = "/power/workflow/manager/show/show.jsp?entryId="
						+ entryId;
				window.open(url);
			}
		} else {
			Ext.Msg.alert('提示', '请选择要查看的部门');
			return;
		}

	}

	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				border : false,
				frame : false,
				items : [{
							layout : 'fit',
							border : false,
							frame : false,
							region : "center",
							items : [Grid]
						}]
			});
	queryFun();
})
