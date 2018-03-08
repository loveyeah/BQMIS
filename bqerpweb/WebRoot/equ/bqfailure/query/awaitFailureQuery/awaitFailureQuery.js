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
			CurrentDate = CurrentDate + Month + "-";
		} else {
			CurrentDate = CurrentDate + "0" + Month + "-";
		}
		if (Day >= 10) {
			CurrentDate = CurrentDate + Day;
		} else {
			CurrentDate = CurrentDate + "0" + Day;
		}
		return CurrentDate;
	}
	var enddate = new Date();
	var startdate = enddate.add(Date.MONTH, -1);
	startdate = startdate.getFirstDateOfMonth();
	var sdate = ChangeDateToString(startdate);
	var edate = ChangeDateToString(enddate);
	var fromDate = new Ext.form.DateField({
		format : 'Y-m-d',
		name : 'startDate',
		id : 'fromDate',
		itemCls : 'sex-left',
		clearCls : 'allow-float',
		checked : true,
		fieldLabel : "查询时间从",
		allowBlank : false,
		readOnly : true,
		value : sdate,
		emptyText : '请选择',
		anchor : '100%'
	});
	var toDate = new Ext.form.DateField({
		format : 'Y-m-d',
		name : 'endDate',
		value : edate,
		id : 'toDate',
		itemCls : 'sex-left',
		clearCls : 'allow-float',
		fieldLabel : "至",
		allowBlank : false,
		readOnly : true,
		emptyText : '请选择',
		anchor : '100%'
	});

	var awaitTypeBox = new Ext.form.ComboBox({
		id : 'awaitType-combobox',
		store : new Ext.data.SimpleStore({
			fields : ["retrunValue", "displayText"],
			data : [['%', '全部'], ['1', '无备品'], ['2', '等待停机处理'], ['3', '其它']]
		}),
		fieldLabel : '待处理类别',
		valueField : "retrunValue",
		displayField : "displayText",
		mode : 'local',
		forceSelection : true,
		hiddenName : 'failurehis.awaitType',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		value : "%",
		anchor : '100%'
	});
	var dominationProfessionurl = "bqfailure/querydominationProfessionList.action";
	var dominationProfessionconn = Ext.lib.Ajax.getConnectionObject().conn;
	dominationProfessionconn.open("POST", dominationProfessionurl, false);
	dominationProfessionconn.send(null);
	var dominationProfessionGrid = eval('('
			+ dominationProfessionconn.responseText + ')').list;

	var dominationProfessionList = new Ext.data.Record.create([{
		name : 'specialityCode'
	}, {
		name : 'specialityName'
	}]);

	var dominationProfessionGrids = new Ext.data.JsonStore({
		data : dominationProfessionGrid,
		fields : dominationProfessionList
	});

	var dominationProfessionBox = new Ext.form.ComboBox({
		id : 'dominationProfessionBox',
		store : dominationProfessionGrids,
		fieldLabel : '管辖专业',
		valueField : "specialityCode",
		displayField : "specialityName",
		mode : 'local',
		forceSelection : true,
		hiddenName : 'specialityCode',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		blankText : '管辖专业',
		emptyText : '管辖专业',
		value : '',
		anchor : '100%'
	});
	var repairDepturl = "bqfailure/queryrepairDept.action";
	var repairDeptconn = Ext.lib.Ajax.getConnectionObject().conn;
	repairDeptconn.open("POST", repairDepturl, false);
	repairDeptconn.send(null);
	var repairDeptGrid = eval('(' + repairDeptconn.responseText + ')').list;

	var repairDeptList = new Ext.data.Record.create([{
		name : 'deptCode'
	}, {
		name : 'deptName'
	}]);

	var repairDeptGrids = new Ext.data.JsonStore({
		data : repairDeptGrid,
		fields : repairDeptList
	});
	var repairDepBox = new Ext.form.ComboBox({
		id : 'tbarrepairDep-combobox',
		store : repairDeptGrids,
		fieldLabel : '检修部门',
		valueField : "deptCode",
		displayField : "deptName",
		mode : 'local',
		forceSelection : true,
		hiddenName : 'deptCode',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		blankText : '请选择...',
		emptyText : '请选择...',
		value : '',
		anchor : '100%'
	});
	var statuslist = [["全部", "%"], ["未上报", "0"], ["待消缺", "1"], ["待确认", "2"],
			["已确认待消缺", "18"], ["点检待验收", "3"], ["运行待验收", "14"], ["运行已验收", "4"],
			["验收退回", "9"], ["设备部仲裁", "7"], ["已仲裁待消缺", "8"], ["点检待处理审批", "11"],
			["设备部主任待处理审批", "12"], ["发电部待处理审批", "13"], ["总工待处理审批", "20"],
			["已处理", "5"], ["点检待处理退回", "15"], ["设备部待处理退回", "16"],
			["发电部待处理退回", "17"], ["总工待处理退回", "21"], ["作废", "6"]];
	var failureStatusBox = new Ext.form.ComboBox({
		id : "statuslist",
		name : 'statuslist',
		fieldLabel : '缺陷状态',
		valueField : 'id',
		displayField : 'name',
		store : new Ext.data.SimpleStore({
			fields : ['name', 'id'],
			data : statuslist
		}),
		value : "%",
		hiddenName : 'status',
		forceSelection : true,
		editable : false,
		mode : 'local',
		emptyText : '请选择...',
		blankText : '请选择',
		triggerAction : 'all',
		selectOnFocus : false,
		anchor : '100%'
	});
	var btnQuery = new Ext.Button({
		id : 'btnQuery',
		text : '查  询',
		iconCls : 'query',
		minWidth : 70,
		handler : function() {
			var ftime = Ext.get('fromDate').dom.value;
			var ttime = Ext.get('toDate').dom.value;
			if (ftime > ttime) {
				Ext.Msg.alert('提示', '选择后一日期应比前一日期大！');
				return false;
			}
			ds.load({
				params : {
					startDate : Ext.get('fromDate').dom.value,
					endDate : Ext.get('toDate').dom.value,
					start : 0,
					limit : 18,
					repairDep : repairDepBox.getValue(),
					dominationProfession : dominationProfessionBox.getValue(),
					awaitType : awaitTypeBox.getValue(),
					failureStatus : failureStatusBox.getValue()
				}
			});
		}
	});
	var btnExport = new Ext.Button({
		id : 'btnExport',
		text : '导  出',
		iconCls : 'export',
		minWidth : 70,
		handler : function() {
			reportexport();
		}
	});
	var content = new Ext.form.FieldSet({
		title : '查询条件',
		height : '100%',
		// collapsible : true,
		layout : 'form',
		items : [{
			layout : 'column',
			border : false,
			items : [{
				border : false,
				columnWidth : 0.25,
				layout : 'form',
				items : [fromDate]
			}, {
				border : false,
				columnWidth : 0.3,
				layout : 'form',
				items : [repairDepBox]
			}, {
				border : false,
				columnWidth : 0.3,
				layout : 'form',
				items : [awaitTypeBox]
			}, {
				border : false,
				columnWidth : 0.05,
				layout : 'form'
			}, {
				border : false,
				columnWidth : 0.1,
				align : 'right',
				layout : 'form',
				items : [btnQuery]
			}]
		}, {
			layout : 'column',
			border : false,
			items : [{
				border : false,
				columnWidth : 0.25,
				layout : 'form',
				items : [toDate]
			}, {
				border : false,
				columnWidth : 0.3,
				layout : 'form',
				items : [failureStatusBox]
			}, {
				border : false,
				columnWidth : 0.3,
				layout : 'form',
				items : [dominationProfessionBox]
			}, {
				border : false,
				columnWidth : 0.05,
				layout : 'form'
			}, {
				border : false,
				columnWidth : 0.1,
				layout : 'form',
				items : [btnExport]
			}]
		}]
	});
	var form = new Ext.form.FormPanel({
		bodyStyle : "padding:5px 5px 0",
		labelAlign : 'right',
		id : 'shift-form',
		labelWidth : 70,
		autoHeight : true,
		border : false,
		items : [content]
	});
	/*-----------------------------------------------------------------*/
	var item = Ext.data.Record.create([{
		name : 'failureCode'
	}, {
		name : 'failureContent'
	}, {
		name : 'approveTime'
	}, {
		name : 'approveOpinion'
	},{
		name : 'overtime'
	}, {
		name : 'failuretypeName'
	}, {
		name : 'repairOpinion'
	}, {
		name : 'equOpinion'
	}, {
		name : 'leaderOpinion'
	}, {
		name : 'dominationProfessionName'
	}, {
		name : 'repairDepName'
	}, {
		name : 'findByName'
	}, {
		name : 'id'
	}, {
		name : 'workFlowNo'
	}, {
		name : 'type'
	}]);
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true
	});
	var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
		header : '序号',
		width : 50,
		align : 'center'
	}), {
		header : '缺陷编号',
		dataIndex : 'failureCode',
		align : 'center'
	}, {
		header : '缺陷内容',
		dataIndex : 'failureContent',
		width : 150,
		align : 'left',
		renderer : function change(val) {
			return ' <span style="white-space:normal;">' + val + ' </span>';
		}
	}, {
		header : '申请延期日期',
		dataIndex : 'approveTime',
		width : 120,
		align : 'center'
	}, {
		header : '延期原因',
		dataIndex : 'approveOpinion',
		align : 'center'
	},{
		header : '消缺时间',
		dataIndex : 'overtime',
		width : 120,
		align : 'center'
	},{
		header : '类别',
		dataIndex : 'failuretypeName',
		align : 'center'
	}, {
		header : '点检延期审批意见',
		dataIndex : 'repairOpinion',
		align : 'center'
	}, {
		header : '发电部意见',
		dataIndex : 'equOpinion',
		align : 'center'
	}, {
		header : '设备部意见',
		dataIndex : 'leaderOpinion',
		align : 'center'
	}, {
		header : '总工意见',
		dataIndex : 'type',
		align : 'center'
	}, {
		header : '管辖专业',
		dataIndex : 'dominationProfessionName',
		width : 120,
		align : 'center'
	}, {
		header : '检修部门',
		dataIndex : 'repairDepName',
		width : 120,
		align : 'center'
	}, {
		header : '发现人',
		dataIndex : 'findByName',
		width : 120,
		align : 'center'
	}]);
	cm.defaultSortable = true;
	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'bqfailure/failureAwaitQuery.action'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'total',
			root : 'list'
		}, item)
	});
	ds.load({
		params : {
			startDate : sdate,
			endDate : edate,
			start : 0,
			limit : 18,
			repairDep : "",
			dominationProfession : "",
			awaitType : "%",
			failureStatus : "%"
		}
	});
	ds.on('beforeload', function() {
		Ext.apply(this.baseParams, {
			startDate : Ext.get('fromDate').dom.value,
			endDate : Ext.get('toDate').dom.value,
			repairDep : repairDepBox.getValue(),
			dominationProfession : dominationProfessionBox.getValue(),
			awaitType : awaitTypeBox.getValue(),
			failureStatus : failureStatusBox.getValue()
		});
	});
	var gridbbar = new Ext.PagingToolbar({
		pageSize : 18,
		store : ds,
		displayInfo : true,
		displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
		emptyMsg : "没有记录",
		beforePageText : '页',
		afterPageText : "共{0}"
	});
	var Grid = new Ext.grid.GridPanel({
		ds : ds,
		cm : cm,
		sm : sm,
		split : true,
		title : "待处理缺陷列表(右键查看详细)",
		autoScroll : true,
		bbar : gridbbar,
		border : true,
		viewConfig : {
			forceFit : false
		}
	});
	Grid.on("rowcontextmenu", function(g, i, e) {
		e.stopEvent();
		var record = Grid.getStore().getAt(i);
		// 右键菜单
		var menu = new Ext.menu.Menu({
			id : 'mainMenu',
			items : [new Ext.menu.Item({
				text : '详细信息',
				handler : function() {
					Ext.Ajax.request({
						url : 'bqfailure/findFailureById.action?failure.id='
								+ record.get("id"),
						method : 'post',
						waitMsg : '正在处理数据...',
						success : function(result, request) {
							var json = eval('(' + result.responseText + ')');
							window
									.showModalDialog(
											"/power/equ/bqfailure/query/detailFailure.jsp",
											json,
											'dialogWidth=860px;dialogHeight=500px;center=yes;help=no;resizable=no;status=no;');
						},
						failure : function(result, request) {
							Ext.Msg.alert('提示', '操作失败，请联系管理员！');
						}
					});
				}
			}), new Ext.menu.Item({
				text : '图形展示',
				handler : function() {
					if (record.get("workFlowNo") != ""
							&& record.get("workFlowNo") != null) {
						window
								.open("/power/workflow/manager/show/show.jsp?entryId="
										+ record.get("workFlowNo"));
					}
				}
			})]
		});
		var coords = e.getXY();
		menu.showAt([coords[0], coords[1]]);
	});
	new Ext.Viewport({
		enableTabScroll : true,
		border : false,
		layout : "border",
		items : [{
			region : "north",
			layout : 'fit',
			height : 95,
			border : false,
			split : true,
			margins : '0 0 0 0',
			items : [form]
		}, {
			region : "center",
			layout : 'fit',
			border : false,
			bodyStyle : "padding:0px 5px 0",
			collapsible : true,
			split : true,
			margins : '0 0 0 0',
			items : [Grid]
		}]
	});

	function tableToExcel(tableHTML) {
		window.clipboardData.setData("Text", tableHTML);
		try {
			var ExApp = new ActiveXObject("Excel.Application");
			var ExWBk = ExApp.workbooks.add();
			var ExWSh = ExWBk.worksheets(1);
			ExApp.Columns(1).ColumnWidth = 15;
			ExApp.Columns(2).ColumnWidth = 50;
			ExApp.Columns(3).ColumnWidth = 15;
			ExApp.DisplayAlerts = true;
			ExWSh.Paste();
			ExApp.visible = true;
		} catch (e) {
			if (e.number != -2146827859)
				alert("您的电脑没有安装Microsoft Excel软件！");
			return false;
		}
		ExWBk.worksheets(1).Paste;
	}

	function reportexport() {
		Ext.Ajax.request({
			url : 'bqfailure/failureAwaitQuery.action',
			params : {
				startDate : Ext.get('fromDate').dom.value,
				endDate : Ext.get('toDate').dom.value,
				repairDep : repairDepBox.getValue(),
				dominationProfession : dominationProfessionBox.getValue(),
				awaitType : awaitTypeBox.getValue(),
				failureStatus : failureStatusBox.getValue()
			},
			success : function(response) {
				var json = eval('(' + response.responseText.trim() + ')');
				var records = json.list;
				var html = ['<table border=1><tr><td align="center" colspan="10"><b><h1>缺陷待处理统计</h1></b></td></tr><tr><th>缺陷编号</th><th>缺陷内容</th><th>申请待处理日期</th><th>待处理原因</th><th>消缺时间</th><th>缺陷类别</th><th>检修部门主管意见</th><th>生技部意见</th><th>值长意见</th><th>管辖专业</th><th>检修部门</th><th>发现人</th></tr>'];
				for (var i = 0; i < records.length; i += 1) {
					var rc = records[i];
					html.push('<tr><td>' + rc.failureCode + '</td><td>'
							+ rc.failureContent + '</td><td>' + rc.approveTime
							+ '</td><td>' + rc.approveOpinion + '</td><td>' + rc.overtime + '</td><td>' + rc.failuretypeName + '</td><td>'
							+ rc.repairOpinion + '</td><td>' + rc.equOpinion
							+ '</td><td>' + rc.leaderOpinion + '</td><td>'
							+ rc.dominationProfessionName + '</td><td>'
							+ rc.repairDepName + '</td><td>' + rc.findByName
							+ '</td></tr>');
				}
				html.push('</table>');
				html = html.join('');
				tableToExcel(html);
			},
			failure : function(response) {
				Ext.Msg.alert('提示', '导出失败！');
			}
		});
	}
})