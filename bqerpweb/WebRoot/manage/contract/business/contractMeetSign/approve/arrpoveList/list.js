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
	var approve = new Ext.form.Radio({
		boxLabel : '待签',
		id : 'approve',
		name : 'isRunning',
		inputValue : 'Y',
		checked : true,
		listeners : {
			check : radiochange
		}
	});
	var signed = new Ext.form.Radio({
		boxLabel : '已签',
		name : 'isRunning',
		id : 'signed',
		inputValue : 'N',
		listeners : {
			check : radiochange
		}
	});
	function radiochange() {
		var status;
		if (approve.checked) {
			// Ext.get('btnSign').dom.disabled = false;
			btnSign.setDisabled(false);
			status = 'approve';
		} else {
			btnSign.setDisabled(true);
			status = 'endsign';
		}
		var sd = Ext.get('fromDate').dom.value;
		var ed = Ext.get('toDate').dom.value;
		if (sd > ed) {
					Ext.Msg.alert("提示", "选择后一日期应比前一日期大！");
					return false;
		}else{
		con_ds.load({
			params : {
				startdate : Ext.get('fromDate').dom.value,
				enddate : Ext.get('toDate').dom.value,
				start : 0,
				limit : 18,
				status : status,
				conTypeId : 1
			}
		});
	}
	};
	var btnSign = new Ext.Toolbar.Button({
		id : 'btnSign',
		text : "签字处理",
		iconCls : 'update',
		handler : function() {
			var sels = Grid.getSelectionModel().getSelections();
			if (sels.length > 0) {
				id = sels[0].data.conId;
				contractNo = sels[0].data.conttreesNo;
				parent.Ext.getCmp("maintab").setActiveTab(1);
				var url = "manage/contract/business/contractMeetSign/approve/conInfo/conInfo.jsp";
				parent.document.all.iframe2.src = url + "?id=" + id +"&contractNo="+contractNo;
			} else {
				Ext.Msg.alert('提示', '请从列表中选择一条记录！');
			}
		}
	});
	var contbar = new Ext.Toolbar({
		width : Ext.getBody().getWidth(),
		items : ['合同会签申请时间从：', fromDate, '至:', toDate, '-', approve, signed,
				'-', {
					id : 'btnQuery',
					text : "查询",
					iconCls : 'query',
					handler : radiochange
				}, '-', btnSign]
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
		name : 'currencyName'
	}]);
	var con_sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true
			// listeners : {
			// rowselect : function(sm, row, rec) {
			// }
			// }
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
				align : 'left',
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
				align : 'center'
			}, {
				header : '会签开始时间',
				dataIndex : 'signStartDate',
				width : 120,
				align : 'center'
			}, {
				header : '会签结束时间',
				dataIndex : 'signEndDate',
				width : 120,
				align : 'center'
			}]);
	con_item_cm.defaultSortable = true;
	var con_ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'managecontract/queryApproveConlist.action'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'total',
			root : 'root'
		}, con_item)
	});
	//con_ds.load({
		//params : {
		//	conTypeId : 1
		//}
	//})
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
		id = record.data.conId;
		contractNo = record.data.conttreesNo;
		parent.Ext.getCmp("maintab").setActiveTab(1);
		var url = "manage/contract/business/contractMeetSign/approve/conInfo/conInfo.jsp";
		parent.document.all.iframe2.src = url + "?id=" + id +"&contractNo="+contractNo;
	});
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		items : [Grid]
	});
})