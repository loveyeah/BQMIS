Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
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
	var date = new Date();
	var startdate = date.add(Date.DAY, -60);
	var enddate = date;
	var sdate = ChangeDateToString(startdate);
	var edate = ChangeDateToString(enddate);
	var fromDate = new Ext.form.DateField({
		format : 'Y-m-d',
		name : 'startDate',
		id : 'fromDate',
		itemCls : 'sex-left',
		clearCls : 'allow-float',
		checked : true,
		value : sdate,
		emptyText : '请选择',
		width : 90
	});
	var toDate = new Ext.form.DateField({
		format : 'Y-m-d',
		name : 'endDate',
		value : edate,
		id : 'toDate',
		itemCls : 'sex-left',
		clearCls : 'allow-float',
		checked : true,
		emptyText : '请选择',
		width : 90
	});
	var fuzzytext = new Ext.form.TextField({
		id : 'fuzzy',
		name : 'fuzzy',
		width : 100
		//emptyText : "合同编号/合同名称/合作伙伴"
	})
	var contbar = new Ext.Toolbar({
		width : Ext.get('div_lay').getWidth(),
		items : ['会签结束时间从：', fromDate, '至:', toDate, '-', fuzzytext, '-', {
			id : 'btnQuery',
			text : "查询(按合同编号、合同名称、合作伙伴模糊查询)",
			iconCls : 'query',
			handler : function() {
				con_ds.load({
					params : {
						conTypeId : 2,
						fuzzy : Ext.get('fuzzy').dom.value,
						startdate : Ext.get('fromDate').dom.value,
						enddate : Ext.get('toDate').dom.value,
						start : 0,
						limit : 18
					}
				});
			}
		}, '-', {
			id : 'btnConDetail',
			text : "浏览合同",
			iconCls : 'list',
			handler : function() {
				var selrows = Grid.getSelectionModel().getSelections();
				if (selrows.length > 0) {
					var record = Grid.getSelectionModel().getSelected();
					var url;
					var conId = record.data.conId;

					if ((conId != null) && (conId != "")) {
						url = "/power/manage/projectFact/business/conBaseInfo/conBaseInfo.jsp?id="
								+ conId;
					}
					var o = window.showModalDialog(url, '',
							'dialogWidth=700px;dialogHeight=600px;status=no');
				} else {
					Ext.Msg.alert('提示', '请从列表中选择一条记录!');
				}
			}
		}, '-', {
			id : 'btnApproveQuery',
			text : "会签审批查询",
			iconCls : 'view',
			handler : function() {
			var selrows = Grid.getSelectionModel().getSelections();
				if (selrows.length > 0) {
					var entryId = selrows[0].data.workflowNo;
					if (entryId == null || entryId == "") {
						Ext.Msg.alert('提示', '流程尚未启动！');
					} else {
						var url = "/power/workflow/manager/show/show.jsp?entryId="
								+ entryId;
						window.open(url);
					}
				} else {
					Ext.Msg.alert('提示', '请从列表中选择一条记录！');
				}
			}
		}]
	});
	var con_item = Ext.data.Record.create([{
		name : 'conId'
	}, {
		name : 'fileStatue'
	}, {
		name : 'conttreesNo'
	}, {
		name : 'contractName'
	}, {
		name : 'clientName'
	}, {
		name : 'actAmount'
	}, {
		name : 'currencyType'
	}, {
		name : 'operateName'
	}, {
		name : 'operateDeptName'
	}, {
		name : 'signStartDate'
	}, {
		name : 'signEndDate'
	}, {
		name : 'execFlag'
	}, {
		name : 'workflowNo'
	},{
		name : 'currencyName'
	}]);
	var con_sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			rowselect : function(sm, row, rec) {

			}
		}
	});
	function change(v) {
		if (v == 'DRF') {
			return '未归档';
		}
		if (v == 'PRE') {
			return '预归档';
		}
		if (v == 'BAK') {
			return '退回';
		}
	}
	var con_item_cm = new Ext.grid.ColumnModel([con_sm,
			new Ext.grid.RowNumberer({
				header : '项次号',
				width : 50,
				align : 'center'
			}), {
				header : '归档状态',
				dataIndex : 'fileStatue',
				align : 'center',
				renderer : change
			}, {
				header : '合同编号',
				dataIndex : 'conttreesNo',
				align : 'center'
			}, {
				header : '合同名称',
				dataIndex : 'contractName',
				align : 'center'
			}, {
				header : '合作伙伴',
				dataIndex : 'clientName',
				align : 'center'
			}, {
				header : '金额',
				dataIndex : 'actAmount',
				align : 'center'
			}, {
				header : '币别',
				dataIndex : 'currencyName',
				align : 'center'
//				,
//				renderer : function(v) {
//
//					if (v == 1) {
//						return "RMB";
//					}
//					if (v == 2) {
//						return "USD";
//					} else {
//						return "异常";
//					}
//
//				}
			}, {
				header : '经办人',
				dataIndex : 'operateName',
				align : 'center'
			}, {
				header : '经办部门',
				dataIndex : 'operateDeptName',
				width : 120,
				align : 'center'
			}, {
				header : '会签开始时间',
				dataIndex : 'signStartDate',
				width : 150,
				align : 'center'
			}, {
				header : '会签结束时间',
				dataIndex : 'signEndDate',
				width : 150,
				align : 'center'
			}]);
	con_item_cm.defaultSortable = true;
	var con_ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'managecontract/getArchiveList.action'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'total',
			root : 'root'
		}, con_item)
	});
	con_ds.load({
		params : {
			conTypeId : 2,
			startdate : sdate,
			enddate : edate,
			start : 0,
			limit : 18
		}
	});
	var gridbbar = new Ext.PagingToolbar({
		pageSize : 18,
		store : con_ds,
		width : Ext.get('div_lay').getWidth(),
		displayInfo : true,
		displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
		emptyMsg : "没有记录",
		beforePageText : '页',
		afterPageText : "共{0}"
	});
	var Grid = new Ext.grid.GridPanel({
		ds : con_ds,
		cm : con_item_cm,
		sm : con_sm,
		title : '待归档合同列表',
		width : Ext.get('div_lay').getWidth(),
		split : true,
		// autoScroll : true,
		bbar : gridbbar,
		tbar : contbar,
		border : false,
		viewConfig : {
			forceFit : false
		}
	});
	Grid.on('rowdblclick', function(grid, rowIndex, e) {
		var record = Grid.getSelectionModel().getSelected();
		id = record.data.conId;
		var status = record.data.fileStatue;
		var flag = record.data.execFlag;
		var apply = "yes";
		if (status == 'DRF') {
			if ((flag != '1') && (flag != '4')) {
				apply = "no";
			}
		} else if (status == 'BAK') {
			apply = "yes";
		} else {
			apply = "no";
		}
		parent.Ext.getCmp("maintab").setActiveTab(2);
		parent.Ext.getCmp("maintab").setActiveTab(1);
		var url2 = "manage/projectFact/business/contractArchive/conArchive/conArchive.jsp";
		parent.document.all.iframe2.src = url2 + "?id=" + id + "&appay="
				+ apply;
		var url3 = "manage/projectFact/business/contractArchive/payPlan/payPlan.jsp";
		parent.document.all.iframe3.src = url3 + "?id=" + id;
	});
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		items : [Grid]
	});
})
