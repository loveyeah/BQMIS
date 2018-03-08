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
									|| beginDate.getValue() > endDate.getValue()) {
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
		hiddenName : 'condenser.deviceCode',
		mode : 'local',
		triggerAction : 'all',
		value : '',
		readOnly : true,
		width : 100
	});
	
	// 定义grid
	var MyRecord = Ext.data.Record.create([{
		name : 'condenser.nqjxlId'
	}, {
		name : 'condenser.deviceCode'
	}, {
		name : 'deviceName'
	}, {
		name : 'condenser.waterQuanlity'
	}, {
		name : 'condenser.place'
	}, {
		name : 'startDate'
	}, {
		name : 'endDate'
	}, {
		name : 'condenser.handleStep'
	}, {
		name : 'condenser.handleResult'
	}, {
		name : 'condenser.memo'
	}, {
		name : 'condenser.fillBy'
	}, {
		name : 'fillName'
	}, {
		name : 'fillDate'
	}, {
		name : 'condenser.content'
	}]);

	var dataProxy = new Ext.data.HttpProxy({
				url : 'productionrec/findCondenserLeakList.action'
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
			dataIndex : 'condenser.nqjxlId',
			hidden : true
		}, {
			header : "机组编码",
			width : 100,
			sortable : true,
			hidden : true,
			align : 'center',
			dataIndex : 'condenser.deviceCode'
		}, {
			header : "机组",
			width : 100,
			sortable : true,
			dataIndex : 'deviceName',
			align : 'center'
		}, {
			header : "凝结水水质",
			width : 100,
			sortable : true,
			dataIndex : 'condenser.waterQuanlity',
			align : 'center'
		}, {
			header : "部位",
			width : 100,
			sortable : true,
			dataIndex : 'condenser.place',
			align : 'center'
		},{
			header : "泄漏开始时间",
			width : 140,
			sortable : true,
			dataIndex : 'startDate',
			align : 'center'
		}, {
			header : "泄漏结束时间",
			width : 140,
			sortable : true,
			dataIndex : 'endDate',
			align : 'center'
		}, {
			header : "处理措施",
			width : 120,
			sortable : true,
			dataIndex : 'condenser.handleStep',
			align : 'center'
		}, {
			header : "处理结果",
			width : 100,
			sortable : true,
			dataIndex : 'condenser.handleResult',
			align : 'center'
		}, {
			header : "备注",
			width : 100,
			sortable : true,
			dataIndex : 'condenser.memo',
			align : 'center'
		},{
			header : "附件",
			width : 75,
			sortable : true,
			dataIndex : 'condenser.content',
			renderer:function(v){
				if(v !=null && v !='')
				{ 
					var s =  '<a href="#" onclick="window.open(\''+v+'\');return  false;">[查看]</a>';
					return s;
				}
			} 
			
		}],
		sm : sm,
		tbar : ["机组编码:", apartCodeBox,'-',"泄露开始时间:", beginDate, "~", endDate ,{
			id : 'btnQuery',
			text : "查询",
			iconCls : 'query',
			handler : queryRecord
		}, '-', {
			text : "查看",
			iconCls : 'list',
			handler : updateRecord
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
				Ext.Msg.alert("系统提示信息", "请选择其中一项进行查看！");
			} else {
				var member = records[0];
				parent.currentRecord = member;
				parent.Ext.getCmp("maintab").setActiveTab(1);
				var url = "productiontec/chemistry/query/condenserLeakQuery/condenserLeakQueryBase.jsp";			
				parent.document.all.iframe2.src = url;		
				
			}
		} else {
			Ext.Msg.alert("提示", "请先选择要查看的行!");
		}
	}
	
	function queryRecord() {
		store.load({
			params : {
				start : 0,
				limit : 18,
				deviceCode:apartCodeBox.getValue(),
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