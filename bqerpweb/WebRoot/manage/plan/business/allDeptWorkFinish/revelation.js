Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	var deptMainId = null;
	var finishWorkNo = null;
	var finishStatus = null;
	var arg = window.dialogArguments;
	var month = arg.month;
	function getDate() {
		var s;
		s = month;
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
			}
			// add by liuyi 20100525 
			,{
				name : 'orderBy',
				mapping : 10
			}
			]);

	var con_sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : false
			});
	var con_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
//							url : 'manageplan/getPlanCompleteAllQuery.action' 
					//update by sychen 20100415
							url : 'manageplan/getBpPlanDeptCompleteShowAllList.action'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : "totalCount",
							root : "list"
						}, con_item)
			});

	var con_item_cm = new Ext.grid.ColumnModel([con_sm, {
		header : '部门',
		dataIndex : 'deptName',
		align : 'center',
		renderer : function(value, matedata, record, rowIndex, colIndex, store) {
			if (record && rowIndex > 0) {
				if (store.getAt(rowIndex).get('deptName') == store
						.getAt(rowIndex - 1).get('deptName')
						|| store.getAt(rowIndex).get('deptName') == '')
					return '';
			}
			return value;
		}
	}, {
		header : '序号',
		dataIndex : 'orderBy',
		align : 'center'

	}, {
		header : '工作内容',
		dataIndex : 'jobContent',
		align : 'left'

	}, {
		header : '完成时间',
		dataIndex : 'completeDate',
		align : 'center'
			// ,
			// renderer : function(v) {
			// if (v == '0') {
			// return '当月';
			// } else if (v == '1') {
			// return '跨越';
			// } else if (v == '2') {
			// return '长期';
			// } else
			// return '';
			// }

		}, {
		header : '完成情况',
		dataIndex : 'ifComplete',
		align : 'center'
			// ,
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
				readOnly : true
			});
	planTime.setValue(getDate());

	function queryFun() {
		con_ds.baseParams = {
			planTime : getDate()
		}
		con_ds.load();
	}
	var query = new Ext.Button({
				text : '查询',
				iconCls : 'query',
				handler : queryFun

			})

	var contbar = new Ext.Toolbar({
				items : [planTime, {
							text : "导出",
							id : 'btnreport',
							iconCls : 'approve',
							handler : approveFun
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
	function myExport() {
		Ext.Ajax.request({
//			url : 'manageplan/getPlanCompleteAllQuery.action?planTime=' + month,
			//update by sychen 20100415
			url : 'manageplan/getBpPlanDeptCompleteShowAllList.action?planTime=' + month,
			success : function(response) {
				var json = eval('(' + response.responseText.trim() + ')');
				var records = json.list;
				//'<tr><th>部门</th><th colspan="3">工作内容</th><th>完成时间</th><th>完成情况</th><th>考核说明</th></tr>
				var html = ['<table border=0 style="font-size:5.55mm; FONT-FAMILY: 仿宋_GB2312">'];
				for (var i = 0; i < records.length; i += 1) {
					var rc = records[i];
					var deptName;
					if (i > 0) {
						if (records[i][4] == records[i - 1][4]) {
							html.push('<tr><td colspan="2" align ="center">' + rc[10]
							+ '</td><td align ="left" colspan="6">' + rc[6]
							+ '</td><td colspan="2" align ="center">' + rc[9]
							+ '</td><td colspan="2" align ="center">' + rc[7]
							+ '</td><td colspan="2" align ="center">'
							+ (rc[8] == null ? "" : rc[8]) + '</td></tr>');
							deptName = "";
						} else {
							deptName = records[i][4];
							html.push('<tr style="font-size:5.55mm; FONT-FAMILY: 方正仿宋简体"><th  colspan ="14">' +deptName
							+ '</th></tr>')
							html.push('<tr ><th colspan="2">序号</th><th align = "left" colspan="6">工作内容</th><th colspan="2">完成时间</th><th  colspan="2">完成情况</th><th colspan="2">考核说明</th></tr>')
						    //add by sychen 20100415
						    html.push('<tr><td colspan="2" align ="center">' + rc[10]
							+ '</td><td align ="left" colspan="6">' + rc[6]
							+ '</td><td colspan="2" align ="center">' + rc[9]
							+ '</td><td colspan="2" align ="center">' + rc[7]
							+ '</td><td colspan="2" align ="center">'
							+ (rc[8] == null ? "" : rc[8]) + '</td></tr>');
						}
					} else {
						deptName = records[i][4];
						html.push('<tr style="font-size:5.55mm; FONT-FAMILY: 方正仿宋简体"><th colspan ="14">' +deptName+ '</th></tr>')
						html.push('<tr><th colspan="2">序号</th><th align = "left" colspan="6">工作内容</th><th colspan="2">完成时间</th><th  colspan="2">完成情况</th><th colspan="2">考核说明</th></tr>')
						html.push('<tr><td colspan="2" align ="center">' + rc[10]
							+ '</td><td align ="left" colspan="6">' + rc[6]
							+ '</td><td colspan="2" align ="center">' + rc[9]
							+ '</td><td colspan="2" align ="center">' + rc[7]
							+ '</td><td colspan="2" align ="center">'
							+ (rc[8] == null ? "" : rc[8]) + '</td></tr>');
					}
//					html.push('<tr><td align ="left">' + deptName
//							+ '</td><td align ="left" colspan="3">' + rc[6]
//							+ '</td><td align ="center">' + rc[9]
//							+ '</td><td align ="center">' + rc[7]
//							+ '</td><td align ="center">'
//							+ (rc[8] == null ? "" : rc[8]) + '</td></tr>');
				}
				html.push('</table>');
				html = html.join(''); // 最后生成的HTML表格
				tableToExcel(html);
			},
			failure : function(response) {
				Ext.Msg.alert('信息', '失败');
			}
		});
	}

	function approveFun() {
		myExport();
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
