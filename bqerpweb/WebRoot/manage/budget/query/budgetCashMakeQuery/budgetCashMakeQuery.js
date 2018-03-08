Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	// 获得年份
	function getDate() {
		var Y;
		Y = new Date();
		Y = Y.getFullYear().toString(10);
		return Y;
	}
	// 获得月份
	function getMonth() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString() + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t;
		return s;
	}

	// 系统当前时间
	function getTime() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate();
		s += (t > 9 ? "" : "0") + t + " ";
		return s;
	}
	// 定义从页面取得数据

	var entryId; // 工作流编号
	var id; // 项目Id
	var topicId = 4;
	var deptCode; // 部门Id

	// 上报人
	var reportName = new Ext.form.Hidden({
		id : 'reportName',
		name : 'reportName',
		dataIndex : 'reportName'
	});

	function queryData() {
		store.load({
			params : {
				budgetTime : time.getValue(),
				deptCode : deptComboBox.getValue()==""?deptCode:deptComboBox.getValue(),
				topicId : topicId
			}
		});
		store.rejectChanges();
	}

	// 年月份选择
	var formatType;
	var yearRadio = new Ext.form.Radio({
		id : 'year',
		name : 'queryWayRadio',
		// blankText : getDate(),
		hideLabel : true,
		boxLabel : '年份',
		checked : true,
		listeners : {
			check : function() {
				var queryType = getChooseQueryType();
				switch (queryType) {
					case 'year' : {
						formatType = 1;
						time.setValue(getDate());
						break;
					}
					case 'month' : {
						time.setValue(getMonth());
						formatType = 2;
						break;
					}
				}
			}
		}
	});
	var monthRadio = new Ext.form.Radio({
		id : 'month',
		name : 'queryWayRadio',
		hideLabel : true,
		boxLabel : '月份'
	});

	var time = new Ext.form.TextField({
		id : 'time',
		allowBlank : true,
		readOnly : true,
		value : getMonth(),
		width : 100,
		listeners : {
			focus : function() {
				var format = '';
				if (formatType == 1)
					format = 'yyyy';
				if (formatType == 2)
					format = 'yyyy-MM';
				WdatePicker({
					startDate : '%y-%M-%d',
					dateFmt : format,
					alwaysUseStartDate : false,
					onclearing : function() {
						planStartDate.markInvalid();
					}
				});
			}
		}
	});
	// 遍历所有的REDIO获得ID
	function getChooseQueryType() {
		var list = document.getElementsByName("queryWayRadio");
		for (var i = 0; i < list.length; i++) {
			if (list[i].checked) {
				return list[i].id;
			}
		}
	}

	// 定义部门
	var deptData = new Ext.data.JsonStore({
		root : 'list',
		url : "managebudget/findBudgetDeptList.action",
		fields : ['depCode','depName']
	});
//	deptData.on('load', function(e, records) {
//		var record1=new Ext.data.Record({
//    	depName:'所有',
//    	centerId:''}
//    	);
//    	deptData.insert(0,record1);
//    	deptComboBox.setValue('');
//	});
	deptData.load();
	
	var deptComboBox = new Ext.form.ComboBox({
		id : "deptComboBox",
		store : deptData,
		displayField : "depName",
		valueField : "depCode",
		mode : 'local',
		width : 150,
		triggerAction : 'all',
		readOnly : true
	});
	
	var sm = new Ext.grid.CheckboxSelectionModel();
	// 创建记录类型
	var Myrecord = Ext.data.Record.create([{
		// 预算编制单ID
		name : 'budgetMakeId',
		mapping : 0
	}, {
		// 预算明细ID
		name : 'budgetItemId',
		mapping : 1
	}, {
		// 预算指标id
		name : 'itemId',
		mapping : 2
	}, {
		// 预算指标名称
		name : 'itemAlias',
		mapping : 3
	}, {
		// 编制依据
		name : 'budgetBasis',
		mapping : 4
	}, {
		// 预算值
		name : 'budgetAmount',
		mapping : 5
	}, {
		// 计量单位名称
		name : 'unitName',
		mapping : 7
	}, {
		// 预算部门id
		name : 'centerId',
		mapping : 8
	}, {
		// 预算主题id
		name : 'topicId'
	}, {
		// 预算时间
		name : 'budgetTime'
	}, {
		// 编制状态
		name : 'makeStatus',
		mapping : 9
	}, {
		// 工作流序号
		name : 'workFlowNo',
		mapping : 10
	}, {
		name : 'financeItem',
		mapping : 11
	}, {
		name : 'dataSource',
		mapping : 12
	}, {
		name : 'itemCode',
		mapping : 13
	}, {
		name : 'budgetGatherId',
		mapping : 14
	}, {
		name : 'adviceBudget',
		mapping : 15
	}]);
	// 配置数据集
	var store = new Ext.data.Store({
		/* 创建代理对象 */
		proxy : new Ext.data.HttpProxy({
			url : 'managebudget/findBudgetBasisList.action'
		}),
		/* 创建解析Json格式数据的解析器 */
		reader : new Ext.data.JsonReader({
			root : "list",
			totalProperty : "totalCount"
		}, Myrecord)
	});

	// 查询
	var query = new Ext.Button({
		id : "btnQuery",
		text : "查询",
		iconCls : "query",
		handler : function query() {
			queryData();
		}
	})
	// 保存
	var btnExport = new Ext.Button({
		id : "btnExport",
		text : "导出",
		iconCls : Constants.CLS_EXPORT,
		handler : exportRecord
	})

	// 定义grid
	// 事件状态
	var sm_store_item = new Ext.grid.ColumnModel([sm, {
		header : '预算项目',
		align : 'left',
		width : 280,
		sortable : true,
		dataIndex : 'itemAlias',
		renderer : function(value, cellmeta, record, rowIndex, columnIndex,
				store) {
			// --------------处理相同名称只显示第一行-----------------
			if (rowIndex != 0) {
				if (store.getAt(rowIndex - 1).get("itemCode") == store
						.getAt(rowIndex).get("itemCode"))
					return "";
			} else {
				return value;
			}
			// ----------------------------------------------------
			var level = 0;
			if (record.get("itemCode") != null || record.get("itemCode") != "") {
				level = (record.get("itemCode").length / 3) - 2;
			}
			if (level > 0) {
				var levelNo = "";
				for (var i = 0; i < level; i++) {
					levelNo = "  " + levelNo;
				}
				value = levelNo + value;
			}
			return "<pre>" + value + "</pre>";
		}
	}, {
		header : '财务科目编码',
		align : 'center',
		width : 110,
		sortable : true,
		dataIndex : 'financeItem',
		renderer : function(value, cellmeta, record, rowIndex, columnIndex,
				store) {
			if (rowIndex != 0) {
				if (store.getAt(rowIndex - 1).get("itemCode") == store
						.getAt(rowIndex).get("itemCode"))
					return "";
			} else {
				return value;
			}
			return value;
		}
	}, {
		header : '计量单位',
		align : 'center',
		width : 110,
		sortable : true,
		dataIndex : 'unitName',
		renderer : function(value, cellmeta, record, rowIndex, columnIndex,
				store) {
			if (rowIndex != 0) {
				if (store.getAt(rowIndex - 1).get("itemCode") == store
						.getAt(rowIndex).get("itemCode"))
					return "";
			} else {
				return value;
			}
			return value;
		}
	}, {
		header : '预算值',
		align : 'right',
		width : 110,
		sortable : true,
		dataIndex : 'adviceBudget',
		renderer : function(value, cellmeta, record, rowIndex, columnIndex,
				store) {
			if (rowIndex != 0) {
				if (store.getAt(rowIndex - 1).get("itemCode") == store
						.getAt(rowIndex).get("itemCode"))
					return "";
			} else {
				return value;
			}
			return value;
		}
	}, {
		header : '编制依据',
		align : 'center',
		width : 110,
		sortable : true,
		css : CSS_GRID_INPUT_COL,
		dataIndex : 'budgetBasis'
	}, {
		header : '金额',
		align : 'right',
		width : 90,
		sortable : true,
		dataIndex : 'budgetAmount'
	}, {
		header : "预算编制单Id",
		align : 'center',
		width : 110,
		sortable : true,
		hidden : true,
		dataIndex : 'budgetMakeId'
	}, {
		header : '预算部门',
		align : 'center',
		width : 110,
		sortable : true,
		hidden : true,
		dataIndex : 'centerId'
	}, {
		header : '预算主题',
		align : 'center',
		width : 110,
		sortable : true,
		hidden : true,
		dataIndex : 'topicId'
	}, {
		header : '预算时间',
		align : 'center',
		width : 110,
		sortable : true,
		hidden : true,
		dataIndex : 'budgetTime'
	}, {
		header : '工作流序号',
		align : 'center',
		width : 110,
		sortable : true,
		hidden : true,
		dataIndex : 'workFlowNo'
	}, {
		header : '编制状态',
		align : 'center',
		width : 110,
		sortable : true,
		hidden : true,
		dataIndex : 'makeStatus',
		editor : new Ext.form.TextField(),
		renderer : function(value) {
			if (value == "0")
				return "未上报";
			else if (value == "1")
				return "编制审批中";
			else if (value == "2")
				return "编制审批通过";
			else if (value == "3")
				return "编制审批退回";
			else
				return value;
		}
	}, {
		header : '来源',
		align : 'center',
		width : 110,
		sortable : true,
		dataIndex : 'dataSource',
		renderer : function(value) {
			if (value == "1")
				return "编制录入";
			if (value == "2")
				return "编制计算";
		}
	}]);

	// 顶部工具栏
	var tbar = new Ext.Toolbar({
		items : ['预算部门：',deptComboBox,'-','预算时间：',yearRadio, monthRadio, time, '-', query, '-', btnExport]
	});

	var Grid = new Ext.grid.GridPanel({
		sm : sm,
		ds : store,
		cm : sm_store_item,
		title : '编制科目',
		autoScroll : true,
		tbar : tbar,
		border : true,
		viewConfig : {
		// forceFit : true
		}
	});
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

	function tableToExcel(tableHTML) {
		window.clipboardData.setData("Text", tableHTML);
		try {
			var ExApp = new ActiveXObject("Excel.Application");
			var ExWBk = ExApp.workbooks.add();
			var ExWSh = ExWBk.worksheets(1);
			ExWSh.Columns("A").ColumnWidth = 20;
			ExApp.DisplayAlerts = false;
			ExApp.visible = true;
		} catch (e) {
			if (e.number != -2146827859)
				alert("您的电脑没有安装Microsoft Excel软件！");
			return false;
		}
		ExWBk.worksheets(1).Paste;
	}
	function exportRecord() {
		Ext.Ajax.request({
			url : 'managebudget/findBudgetBasisList.action',
			params : {
				budgetTime : time.getValue(),
				deptCode : deptComboBox.getValue(),
				topicId : topicId
			},
			success : function(response) {
				var json = eval('(' + response.responseText.trim() + ')');
				var records = json.list;
				// alert(records.length);
				var html = ['<table border=1><tr><th>预算项目</th><th>财务科目编码</th><th>计量单位</th><th>预算值</th><th>编制依据</th><th>金额</th><th>来源</th></tr>'];
				for (var i = 0; i < records.length; i += 1) {

					var rc = records[i];
					var source;
					if (rc[12] == 1) {
						source = '编制录入';
					} else {
						source = '编制计算';
					}
					var itemName = rc[3];
					if (i > 0) {
						if (records[i - 1][3] == itemName)
							itemName = "";
					}

					if (itemName != "") {
						var level = 0;
						if (rc[13] != null || rc[13] != "") {
							level = (rc[13].length / 3) - 2;
						}
						if (level > 0) {
							var levelNo = "";
							for (var j = 0; j < level; j++) {
								levelNo = "  " + levelNo;
							}
							itemName = levelNo + itemName;
						}
					}

					var budgetCount = rc[15];
					if (i > 0) {
						if (records[i - 1][15] == budgetCount)
							budgetCount = "";
					}
					html.push('<tr><td><pre>' + itemName + '</pre></td><td>'
							+ (rc[11] == null ? "" : rc[11]) + '</td><td>'
							+ (rc[7] == null ? "" : rc[7]) + '</td><td>'
							+ budgetCount + '</td><td>'
							+ (rc[4] == null ? "" : rc[4]) + '</td><td>'
							+ (rc[5] == null ? 0 : rc[5]) + '</td><td>'
							+ source + '</td></tr>');
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
	store.on('load', function() {
		for (var j = 0; j < store.getCount(); j++) {
			var temp = store.getAt(j);
			if (temp.get("budgetAmount") == null
					|| temp.get("budgetAmount") == "") {
				temp.set("budgetAmount", "0");
			}
		}
	});
	Grid.on('cellclick', function(grid, rowIndex, columnIndex, e) {
		var record = Grid.getStore().getAt(rowIndex);
		var fieldName = Grid.getColumnModel().getDataIndex(columnIndex);
		if (fieldName == "budgetBasis") {
			budgetBasisSelect();
		}
	});

	function budgetBasisSelect() {
		var record = Grid.selModel.getSelected();
		var budgetItemId = record.get('budgetItemId');
		var budgetGatherId = record.get('budgetGatherId');
		var makeStatus = record.get('makeStatus');
		var unit = record.get('unitName');
		var url = "";
		if (budgetGatherId != null && makeStatus == '2') {
			url = "../../business/budgetMake/budgetBasisSelect.jsp?budgetItemId="
					+ budgetItemId + "&makeStatus='5'"+ "&unit="+unit+"";
		} else {
			url = "../../business/budgetMake/budgetBasisSelect.jsp?budgetItemId="
					+ budgetItemId+ "&unit="+unit+"";
		}
		var mate = window
				.showModalDialog(url, window,
						'dialogWidth=600px;dialogHeight=400px;status=no,scrollbars:yes');
		queryData();
	}

	// 从session取登录人编码姓名部门相关信息
	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
			success : function(action) {
				var result = eval("(" + action.responseText + ")");
				if (result.workerCode) {
					// 设定默认工号
					var workerCode = result.workerCode;
					reportName.setValue(result.workerName);
					deptCode = result.deptCode;
					
					queryData();
				}
			}
		});
	}
	getWorkCode();
})
