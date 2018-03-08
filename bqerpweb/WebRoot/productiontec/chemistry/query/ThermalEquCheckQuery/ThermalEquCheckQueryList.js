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

	var beginDate = new Ext.form.TextField({
		id : 'beginDate',
		name : 'findTime',
		style : 'cursor:pointer',
		width : 85,
		value : sdate,
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-%d',
					dateFmt : 'yyyy-MM-dd',
					alwaysUseStartDate : true,
					onpicked : function() {
						if (endDate.getValue() != "") {
							if (beginDate.getValue() == ""
									|| beginDate.getValue() > endDate
											.getValue()) {
								Ext.Msg.alert("提示", "必须小于后一日期");
								return;
							}
						}
					},
					onclearing : function() {
						beginDate.markInvalid();
					}
				});
			}
		}
	});

	var endDate = new Ext.form.TextField({
		id : 'endDate',
		name : 'findTime',
		style : 'cursor:pointer',
		width : 85,
		value : edate,
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-%d',
					dateFmt : 'yyyy-MM-dd',
					alwaysUseStartDate : true,
					onpicked : function() {
						if (beginDate.getValue() == ""
								|| beginDate.getValue() > endDate.getValue()) {
							Ext.Msg.alert("提示", "必须大于前一日期");
							return;
						}
					},
					onclearing : function() {
						endDate.markInvalid();
					}
				});
			}
		}
	});

	// 所属机组
	var storeCharge = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'workticket/getDetailEquList.action'
		}),
		reader : new Ext.data.JsonReader({
			root : 'list'
		}, [{
			name : 'blockCode',
			mapping : 'blockCode'
		}, {
			name : 'blockName',
			mapping : 'blockName'
		}])
	});

	storeCharge.load();
	var apartCodeBox = new Ext.form.ComboBox({
		id : 'deviceCode',
		fieldLabel : "机组",
		store : storeCharge,
		displayField : "blockName",
		valueField : "blockCode",
		hiddenName : 'equ.deviceCode',
		mode : 'local',
		triggerAction : 'all',
		value : '',
		readOnly : true,
		width : 100
	});

	// 定义grid
	var MyRecord = Ext.data.Record.create([{
		name : 'equ.rlsbjcId'
	}, {
		name : 'equ.deviceCode'
	}, {
		name : 'deviceName'
	}, {
		name : 'equ.testType'
	}, {
		name : 'startDate'
	}, {
		name : 'endDate'
	}, {
		name : 'equ.examineBy'
	}, {
		name : 'examineName'
	}, {
		name : 'equ.chargeBy'
	}, {
		name : 'chargeName'
	}, {
		name : 'equ.content'
	}, {
		name : 'equ.fillBy'
	}, {
		name : 'fillName'
	}, {
		name : 'fillDate'
	}, {
		name : 'equ.depCode'
	}, {
		name : 'depName'
	}]);

	var dataProxy = new Ext.data.HttpProxy({
		url : 'productionrec/findThermalEquCheckList.action'
	});

	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	}, MyRecord);

	var store = new Ext.data.Store({
		proxy : dataProxy,
		reader : theReader
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
			dataIndex : 'equ.rlsbjcId',
			hidden : true
		}, {
			header : "机组编码",
			width : 100,
			sortable : true,
			hidden : true,
			align : 'center',
			dataIndex : 'equ.deviceCode'
		}, {
			header : "机组",
			width : 100,
			sortable : true,
			dataIndex : 'deviceName',
			align : 'center'
		}, {
			header : "检修类别",
			width : 100,
			sortable : true,
			renderer : function returnValue(val) {
				if (val == "1") {
					return '大修';
				}
				if (val == '2') {
					return '小修';
				}
				if (val == '3') {
					return '临时检修';
				}
			},
			dataIndex : 'equ.testType',
			align : 'center'
		}, {
			header : "检查情况",
			width : 150,
			sortable : true,
			dataIndex : 'equ.content',
			align : 'center'
		}, {
			header : "检查开始时间",
			width : 140,
			sortable : true,
			dataIndex : 'startDate',
			align : 'center'
		}, {
			header : "检查结束时间",
			width : 140,
			sortable : true,
			dataIndex : 'endDate',
			align : 'center'
		}, {
			header : "检查人员",
			width : 120,
			sortable : true,
			dataIndex : 'equ.examineBy',
			align : 'center'
		}, {
			header : "负责人",
			width : 100,
			sortable : true,
			dataIndex : 'chargeName',
			align : 'center'
		}],
		sm : sm,
		tbar : ["机组编码:", apartCodeBox, '-'," 检查开始时间:", beginDate, "~", endDate, {
			id : 'btnQuery',
			text : "查询",
			iconCls : 'query',
			handler : queryRecord
		}, '-', {
			text : "查看",
			iconCls : 'list',
			handler : updateRecord
		}, '-', {
			text : "检查情况明细",
			iconCls : 'list',
			handler : updateDetailRecord
		}],
		// 分页
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : store,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		})
	});
	grid.on('rowdblclick', updateRecord);

	function updateRecord() {
		if (grid.selModel.hasSelection()) {
			var records = grid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑！");
			} else {
				var member = records[0];
				parent.currentRecord = member;
				parent.Ext.getCmp("maintab").setActiveTab(1);
				var url = "productiontec/chemistry/query/ThermalEquCheckQuery/ThermalEquCheckQueryBase.jsp";

				parent.document.all.iframe2.src = url;
			}
		} else {
			Ext.Msg.alert("提示", "请先选择要编辑的行!");
		}
	}

	function updateDetailRecord() {
		if (grid.selModel.hasSelection()) {
			var sm = grid.getSelectionModel();
			var selected = sm.getSelections();
			var rlsbjcId = [];
			if (selected.length > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑！");
			} else {

				for (var i = 0; i < selected.length; i += 1) {
					var member = selected[i];
					if (member.get("equ.rlsbjcId")) {
						rlsbjcId.push(member.get("equ.rlsbjcId"));
					}
				}
				url = "ThermalEquCheckDetailSelectQuery.jsp?rlsbjcId=" + rlsbjcId;
				var o = window.showModalDialog(url, '',
						'dialogWidth=800px;dialogHeight=500px;status=no');
			}
		} else {
			Ext.Msg.alert('提示', '请从列表中选择一条记录!');
		}
	}

	function queryRecord() {
		store.load({
			params : {
				start : 0,
				limit : 18,
				deviceCode : apartCodeBox.getValue(),
				sDate : Ext.get('beginDate').dom.value,
				eDate : Ext.get('endDate').dom.value
			}
		});
	}

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		items : [grid]
	});

	queryRecord();

});