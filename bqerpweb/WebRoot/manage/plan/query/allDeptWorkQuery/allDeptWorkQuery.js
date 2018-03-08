Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	var deptMainId = null;
	var finishWorkNo = null;
	var finishStatus = null;
	function getDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t
		return s;
	}

	/** 左边的grid * */
	var con_item = Ext.data.Record.create([{
				name : 'planTime',
				mapping : 0

			}, {
				name : 'finishWorkFlowNo',
				mapping : 1
			}, {
				name : 'finishSignStatus',
				mapping : 2
			}, {
				name : 'deptCode',
				mapping : 3
			}, {
				name : 'deptName',
				mapping : 4
			}, {
				name : 'jobId',
				mapping : 5
			}, {
				name : 'jobContent',
				mapping : 6
			}, {
				name : 'ifComplete',
				mapping : 7
			}, {
				name : 'completeDesc',
				mapping : 8
			}, {
				name : 'completeDate',
				mapping : 9
			}]);

	var con_sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : false
			});
	var con_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'manageplan/getPlanCompleteAllQuery.action'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : "totalCount",
							root : "list"
						}, con_item)
			});
	// con_ds.on('load',function(){
	// if(con_ds.getTotalCount() > 0){
	// if(con_ds.getAt(0).get('finishSignStatus')== '2')
	// {
	// Ext.getCmp('btnreport').setDisabled(true)
	// }else
	// Ext.getCmp('btnreport').setDisabled(false)
	// }
	// })
	var con_item_cm = new Ext.grid.ColumnModel([con_sm, {
		header : '部门',
		dataIndex : 'deptName',
		align : 'center',
		renderer : function(value, matedata, record, rowIndex, colIndex, store) {
			if (record && rowIndex > 0) {
				if (con_ds.getAt(rowIndex).get('deptName') == con_ds
						.getAt(rowIndex - 1).get('deptName')
						|| con_ds.getAt(rowIndex).get('deptName') == '')
					return '';
			}
			return value;
		}
	}, {
		header : '工作内容',
		dataIndex : 'jobContent',
		align : 'center',
		width: 350

	}, {
		header : '完成时间',
		dataIndex : 'completeDate',
		align : 'center'
			// renderer : function(v) {
			// if (v == '0') {
			// return '当月完成';
			// } else if (v == '1') {
			// return '跨越完成';
			// } else if (v == '2') {
			// return '长期';
			// } else
			// return '';
			// }

		}, {
		header : '完成情况',
		dataIndex : 'ifComplete',
		align : 'center'
			// renderer : function(v) {
			// if (v == '0')
			// return '未完成';
			// else if (v == '1')
			// return '进行中';
			// else if (v == '2')
			// return '已完成';
			// else
			// return '';
			// }
		}, {
		header : '考核说明',
		dataIndex : 'completeDesc',
		align : 'center'
	}]);
	con_item_cm.defaultSortable = true;

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
		con_ds.baseParams = {
			// isApprove : 'Y',
			planTime : planTime.getValue()
		}
		con_ds.load();

	}
	var query = new Ext.Button({
				text : '查询',
				iconCls : 'query',
				handler : queryFun

			})

	var contbar = new Ext.Toolbar({
				items : [planTime, query, {
							text : "审批查询",
							id : 'btnreport',
							iconCls : 'approve',
							handler : approveFun
						}, "-", {
							text : "导出",
							id : 'myexport',
							iconCls : 'export',
							handler : exportRec
						}]
			});

	var Grid = new Ext.grid.GridPanel({
				sm : con_sm,
				ds : con_ds,
				cm : con_item_cm,
				// height : 425,
				autoScroll : true,
				tbar : contbar,
				border : true
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

	// add by drdu 20100104
	function exportRec() {
		Ext.Ajax.request({
			url : 'manageplan/getPlanCompleteAllQuery.action',
			params : {
				planTime : planTime.getValue()
			},
			success : function(response) {
				var json = eval('(' + response.responseText.trim() + ')');
				var records = json.list;
//				var html = ['<table border=1><tr><th>部门</th><th>工作内容</th><th>完成时间</th><th>完成情况</th><th>考核说明</th></tr>'];
				var html = ['<table border=0 style="font-size:5.55mm; FONT-FAMILY: 仿宋_GB2312">'];
				for (var i = 0; i < records.length; i += 1) {
					var rc = records[i];
						var rc6 = rc[6] != null ? rc[6] : "";
					var rc9 = rc[9] != null ? rc[9] : "";
					var rc7 = rc[7] != null ? rc[7] : "";
					var rc8 = rc[8] != null ? rc[8] : "";
					var deptName;
					if (i > 0) {
						if (records[i][4] == records[i - 1][4]) {
								html.push('<tr><td align ="left" colspan="6">' + rc[6]
							+ '</td><td colspan="2" align ="center">' + rc[9]
							+ '</td><td colspan="2" align ="center">' + rc[7]
							+ '</td><td colspan="2" align ="center">'
							+ (rc[8] == null ? "" : rc[8]) + '</td></tr>');
							deptName = "";
						} else {
							deptName = records[i][4];
								html.push('<tr style="font-size:5.55mm; FONT-FAMILY: 方正仿宋简体"><th  colspan ="12">' +deptName
							+ '</th></tr>')
							html.push('<tr ><th align = "left" colspan="6">序号</th><th colspan="2">完成时间</th><th  colspan="2">完成情况</th><th colspan="2">考核说明</th></tr>')
						}
					} else {
						deptName = rc[4];
						html.push('<tr style="font-size:5.55mm; FONT-FAMILY: 方正仿宋简体"><th colspan ="12">' +deptName+ '</th></tr>')
						html.push('<tr><th align = "left" colspan="6">序号</th><th colspan="2">完成时间</th><th  colspan="2">完成情况</th><th colspan="2">考核说明</th></tr>')
						html.push('<tr><td align ="left" colspan="6">' + rc[6]
							+ '</td><td colspan="2" align ="center">' + rc[9]
							+ '</td><td colspan="2" align ="center">' + rc[7]
							+ '</td><td colspan="2" align ="center">'
							+ (rc[8] == null ? "" : rc[8]) + '</td></tr>');
					}
//					html.push('<tr><td>' + deptName + '</td><td>' + rc6
//							+ '</td><td>' + rc9 + '</td><td>' + rc7
//							+ '</td><td>' + rc8 + '</td></tr>');
				}
				html.push('</table>');
				html = html.join(''); // 最后生成的HTML表格
				// alert(html);
				tableToExcel(html);
			},
			failure : function(response) {
				Ext.Msg.alert('信息', '失败');
			}
		});
	}

	function approveFun() {
		if (con_ds.getCount() > 0) {
			var entryId = con_ds.getAt(0).get("finishWorkFlowNo");
			if (entryId == null || entryId == "") {
				url = "/power/workflow/manager/flowshow/flowshow.jsp?flowCode="
						+ "bqDeptJobFinishGather";
				window.open(url);
			} else {
				var url = "/power/workflow/manager/show/show.jsp?entryId="
						+ entryId;
				window.open(url);
			}
		} else {
			Ext.Msg.alert('提示', '无数据进行查看！');
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
