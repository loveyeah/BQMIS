Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	var arg = window.dialogArguments;
	var month = arg.month;
	function getDate() {
		var s;
		s = month;
		return s;
	}

	/** 左边的grid * */
	var con_item = Ext.data.Record.create([{
				name : 'jobId',
				mapping : 0
			}, {
				name : 'deptMainId',
				mapping : 1
			}, {
				name : 'jobContent',
				mapping : 2
			}, {
				name : 'ifComplete',
				mapping : 3
			}, {
				name : 'completeDesc',
				mapping : 4
			}, {
				name : 'completeData',
				mapping : 5
			}, {
				name : 'chargeBy',
				mapping : 6
			}, {
				name : 'orderBy',
				mapping : 7
			}, {
				name : 'editDepcode',
				mapping : 8
			}, {
				name : 'deptName',
				mapping : 9
			}, {
				name : 'finishEditBy',
				mapping : 10
			},{
				name : 'editName',
				mapping : 11
			},{
				name:'finishEditDate',
				mapping : 12
			},{
				name : 'finishSignStatus',
				mapping : 13
			},{
				name : 'finishWorkFlowNo',
				mapping : 14
			},{
				name : 'linkJobId',
				mapping : 15
			},{
			    name:'level1DeptName',
			    mapping: 16
			},{
			    name:'completeDataName',
			    mapping: 17
			},{
			    name:'ifCompleteName',
			    mapping: 18
			}]);

	var con_sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : false
			});
	var con_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'manageplan/getPlanJobCompleteApproveList.action'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : "totalCount",
							root : "list"
						}, con_item)
			});

	var con_item_cm = new Ext.grid.ColumnModel([con_sm, {
		header : '部门',
		dataIndex : 'level1DeptName',
		align : 'center',
		renderer : function(value, matedata, record, rowIndex, colIndex, store) {
			if (record && rowIndex > 0) {
				if (store.getAt(rowIndex).get('level1DeptName') == store
						.getAt(rowIndex - 1).get('level1DeptName')
						|| store.getAt(rowIndex).get('level1DeptName') == '')
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
		dataIndex : 'completeData',
		align : 'center',
			 renderer : function(v) {
			if (v == '0') {
				return '当月';
			} else if (v == '1') {
				return '跨月';
			} else if (v == '2') {
				return '长期';
			} else
				return '全年';
		}

		}, {
		header : '完成情况',
		dataIndex : 'ifComplete',
		align : 'center' ,
			 renderer : function(v) {
			if (v == '0')
				return '未完成';
			else if (v == '1')
				return '进行中';
			else if (v == '2')
				return '已完成';
			else
				return '';
		}
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

	var txtIfComplete= new Ext.form.ComboBox({
				name : 'txtIfComplete',
				hiddenName : 'txtIfComplete',
				store : new Ext.data.SimpleStore({
					fields : ['name', 'value'],
					data : [['未完成', '0'], ['进行中', '1'], ['已完成', '2']]
					}),
				displayField : 'name',
				valueField : 'value',
				triggerAction : 'all',
				readOnly : true,
				mode : 'local',
				width : 80,
				listeners : {
					"select" : function() {
						queryFun();
					}
				}
			});
	
	function queryFun() {
		con_ds.baseParams = {
			planTime : getDate(),
			txtIfComplete:txtIfComplete.getValue(),
			flag:'show'
		}
		con_ds.load();
	}
	

	var contbar = new Ext.Toolbar({
				items : ['计划时间：',planTime,'-','完成情况:',txtIfComplete,'-',{
							text : "查询",
							id : 'btnQuery',
							iconCls : 'query',
							handler : queryFun
						},{
							text : "导出",
							id : 'btnreport',
							iconCls : 'export',
							handler : approveFun
						}]
			});

	var Grid = new Ext.grid.GridPanel({
				sm : con_sm,
				ds : con_ds,
				cm : con_item_cm,
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
			url : 'manageplan/getPlanJobCompleteApproveList.action?planTime=' + month,
			success : function(response) {
				var json = eval('(' + response.responseText.trim() + ')');
				var records = json.list;
				var html = ['<table border=0 style="font-size:5.55mm; FONT-FAMILY: 仿宋_GB2312">'];
				for (var i = 0; i < records.length; i += 1) {
					var rc = records[i];
					var deptName;
					if (i > 0) {
						if (records[i][16] == records[i - 1][16]) {
							html.push('<tr><td colspan="2" align ="center">' + rc[7]
							+ '</td><td align ="left" colspan="6">' + rc[2]
							+ '</td><td colspan="2" align ="center">' + rc[17]
							+ '</td><td colspan="2" align ="center">' + rc[18]
							+ '</td><td colspan="2" align ="center">'
							+ (rc[4] == null ? "" : rc[4]) + '</td></tr>');
							deptName = "";
						} else {
							deptName = records[i][16];
							html.push('<tr style="font-size:5.55mm; FONT-FAMILY: 方正仿宋简体"><th  colspan ="14">' +deptName
							+ '</th></tr>')
							html.push('<tr ><th colspan="2">序号</th><th align = "left" colspan="6">工作内容</th><th colspan="2">完成时间</th><th  colspan="2">完成情况</th><th colspan="2">考核说明</th></tr>')
				
						    html.push('<tr><td colspan="2" align ="center">' + rc[7]
							+ '</td><td align ="left" colspan="6">' + rc[2]
							+ '</td><td colspan="2" align ="center">' + rc[17]
							+ '</td><td colspan="2" align ="center">' + rc[18]
							+ '</td><td colspan="2" align ="center">'
							+ (rc[4] == null ? "" : rc[4]) + '</td></tr>');
						}
					} else {
						deptName = records[i][16];
						html.push('<tr style="font-size:5.55mm; FONT-FAMILY: 方正仿宋简体"><th colspan ="14">' +deptName+ '</th></tr>')
						html.push('<tr><th colspan="2">序号</th><th align = "left" colspan="6">工作内容</th><th colspan="2">完成时间</th><th  colspan="2">完成情况</th><th colspan="2">考核说明</th></tr>')
						html.push('<tr><td colspan="2" align ="center">' + rc[7]
							+ '</td><td align ="left" colspan="6">' + rc[2]
							+ '</td><td colspan="2" align ="center">' + rc[17]
							+ '</td><td colspan="2" align ="center">' + rc[18]
							+ '</td><td colspan="2" align ="center">'
							+ (rc[4] == null ? "" : rc[4]) + '</td></tr>');
					}
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
