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
		width : 110
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
		width : 110
	});
	var fuzzy = new Ext.form.TextField({
				id : "fuzzy",
				name : "fuzzy",
				width : 150,
				emptyText : "合同编号/合同名称/供应商"
			});
			
	   // 票面浏览
	function CheckRptPreview() {
		var selrows = Grid.getSelectionModel().getSelections();
		if (selrows.length <= 0) {
			Ext.Msg.alert("提示", "请选择要浏览的记录！");
			return false;
		} else{
			conId  = selrows[0].data.conId;;
			var url = "/powerrpt/report/webfile/CGConMeetSign.jsp?conId="
					+ conId;
			window.open(url);
			}

	};	
	//修改 by xlhe 09/04/08
	var contbar = new Ext.Toolbar({
		items : ['会签时间从：', fromDate, '至:', toDate, '-',fuzzy, '-', {
			id : 'btnQuery',
			text : "查询",
			iconCls : 'query',
			handler : function() {
				var sd = Ext.get('fromDate').dom.value;
				var ed = Ext.get('toDate').dom.value;
				if (sd > ed) {
					Ext.Msg.alert("提示", "选择后一日期应比前一日期大！");
				}
				con_ds.load({
					params : {
						start : 0,
						limit : 18,
						fuzzy : fuzzy.getValue(),
						startdate : Ext.get('fromDate').dom.value,
						enddate : Ext.get('toDate').dom.value,
						conTypeId : 1
						
					}
				});
			}
		}, '-', {
			id : 'btnSave',
			text : "修改",
			iconCls : 'update',
			handler : function() {
				var selrows = Grid.getSelectionModel().getSelections();
				if (selrows.length > 0) {
					id = selrows[0].data.conId;
					parent.Ext.getCmp("maintab").setActiveTab(1);
					var url = "manage/contract/business/contractMeetSign/register/contBase/base.jsp";
					parent.document.all.iframe2.src = url + "?id=" + id;
				} else {
					Ext.Msg.alert('提示', '请选择要修改的记录！');
				}
			}
		}, '-', {
					id : 'btnMeetQuery',
					text : "会签查询",
					iconCls : 'view',
					handler : function() {
						var selrows = Grid.getSelectionModel().getSelections();
						if (selrows.length > 0) {
							var entryId = selrows[0].data.workflowNo;
							if (entryId == null || entryId == "") {
								url = "/power/workflow/manager/flowshow/flowshow.jsp?flowCode="
								+ "bqCGContract";
								window.open(url);
							} else {
								var url = "/power/workflow/manager/show/show.jsp?entryId="
										+ entryId;
								window.open(url);
							}
						} else {
							Ext.Msg.alert('提示', '请从列表中选择一条记录！');
						}
					}
				}
//				, '-', {
//					id : 'btnMeetList',
//					text : "会签表",
//					iconCls : 'pdfview',
//					handler : function() {
//					CheckRptPreview();
//					}
//				}
				]
	});
	var con_item = Ext.data.Record.create([{
		name : 'conId'
	}, {
		name : 'workflowStatus'
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
		name : 'workflowNo'
	}, {
		name : 'currencyName'
	}]);
	var con_sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			rowselect : function(sm, row, rec) {
				if (rec.data.workflowStatus != '1') {
					Ext.get('btnSave').dom.disabled = false;
				} else {
					Ext.get('btnSave').dom.disabled = true;
				}
			}
		}
	});
	var con_item_cm = new Ext.grid.ColumnModel([con_sm,
			new Ext.grid.RowNumberer({
				header : '项次号',
				width : 50,
				align : 'center'
			}), {
				header : '会签状态',
				dataIndex : 'workflowStatus',
				align : 'center',
				renderer : function(v) {
					if (v == 0) {
						return "未上报";
					}
					if (v == 1) {
						return "会签中";
					}
					if (v == 2) {
						return "已会签";
					}
					if (v == 3) {
						return "退回";
					}
					return v;
				}
			}, {
				header : '合同编号',
				dataIndex : 'conttreesNo',
				align : 'center',
				width : 140
			}, {
				header : '合同名称',
				dataIndex : 'contractName',
				align : 'center',
				width :200,
				renderer : function change(val) {
				return ' <span style="white-space:normal;">' + val + ' </span>';
		}
			}, {
				header : '供应商',
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
				
			}, {
				header : '经办人',
				dataIndex : 'operateName',
				align : 'center'
			}, {
				header : '经办部门',
				dataIndex : 'operateDeptName',
				align : 'center',
				width : 140
			}, {
				header : '会签开始时间',
				dataIndex : 'signStartDate',
				align : 'center',
				width : 120
			}

			]);
	con_item_cm.defaultSortable = true;
	var con_ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'managecontract/queryMeetConlist.action'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'total',
			root : 'root'
		}, con_item)
	});
	con_ds.load({
		params : {
			startdate : sdate,
			enddate : edate,
			start : 0,
			limit : 18,
			conTypeId : 1
		}
	});
	con_ds.on('beforeload', function() {
		Ext.apply(this.baseParams, {
			//fuzzy : Ext.get('fuzzy').dom.value,
			startdate : sdate,
			enddate : edate,
			conTypeId : 1					
		});
	});
	var gridbbar = new Ext.PagingToolbar({
		pageSize : 18,
		store : con_ds,
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
		title : '待会签合同列表',
		width : Ext.get('div_lay').getWidth(),
		split : true,
		autoScroll : true,
		bbar : gridbbar,
		tbar : contbar,
		border : false,
		viewConfig : {
			forceFit : false
		}
	});
	Grid.on('rowdblclick', function(grid, rowIndex, e) {
		var record = Grid.getSelectionModel().getSelected();
		var revoke;
		if (record.data.workflowStatus == '1') {
			revoke = 'revoke';
		} else {
			revoke = "";
		}
		id = record.data.conId;
		var currencyType = record.data.currencyType;
		var actAmount = record.data.actAmount;
		var contractNo = record.data.conttreesNo;
		parent.Ext.getCmp("maintab").setActiveTab(2);
		parent.Ext.getCmp("maintab").setActiveTab(1);
		var url = "manage/contract/business/contractMeetSign/register/contBase/base.jsp";
		parent.document.all.iframe2.src = url + "?id=" + id + "&revoke="
				+ revoke + "&contractNo=" + contractNo;
//		var url2 = "manage/contract/business/contractMeetSign/register/payPlan/payPlan.jsp";
//		parent.document.all.iframe3.src = url2 + "?id=" + id + "&currencyType="
//				+ currencyType + "&actAmount=" + actAmount;

	});
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		items : [Grid]
	});
})
