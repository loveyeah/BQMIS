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
	function renderDate(value) {
		if (!value)
			return "";
		var reDate = /\d{4}\-\d{2}\-\d{2}/gi;
		var strDate = value.match(reDate);
		if (!strDate)
			return "";
		return strDate;
	}
	// 显示所有领料单checkbox
	var issueAllCheck = new Ext.form.Checkbox({
				boxLabel : "显示所有领料单",
				anchor : '100%'
			});
	// grid工具栏
	var gridTbar = new Ext.Toolbar({
				items : [issueAllCheck]
			});
	// 查询值
	var fuzzy = new Ext.form.TextField({
				id : "fuzzy",
				value : "",
				width : 200,
				maxLength : 30,
				name : "fuzzy"
			});
	// record
	var queryRecord = new Ext.data.Record.create([
			// 领料单ID
			{
		name : 'issueHeadId'
	},		// 单据状态
	{
		name : 'issueStatus'
	},
			// 领料单编号，
			{
				name : 'issueNo'
			},
			// 项目编号
			{
				name : 'projectCode'
			},
			// 工单编号
			{
				name : 'woNo'
			},
			// 成本分摊项目编码
			{
				name : 'costItemId'
			},
			// 费用来源ID
			{
				name : 'itemId'
			},
			// 需求计划申请单编号
			{
				name : 'mrNo'
			},// 申请领料人
			{
				name : 'receiptBy'
			},// 领用部门
			{
				name : 'receiptDep'
			},// 申请领用日期
			{
				name : 'dueDate'
			},// 费用归口部门
			{
				name : 'feeByDep'
			},// 费用归口专业
			{
				name : 'feeBySpecial'
			},// 备注
			{
				name : 'memo'
			},// 单据状态
			{
				name : 'issueStatus'
			},// 是否紧急领用
			{
				name : 'isEmergency'
			},// 上次修改日期
			{
				name : 'lastModifiedDate'
			}, {
				name : 'workFlowNo'
			}, {
				name : 'planOriginalId'
			}]);
	// 领料单grid的store
	var queryStore = new Ext.data.JsonStore({
				url : 'resource/getIssueHeadRigisterList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : queryRecord
			});

	queryStore.on('beforeload', function() {
				Ext.apply(this.baseParams, {
							fuzzy : fuzzy.getValue(),
							flag : issueAllCheck.getValue() ? "1" : "0"
						});
			});
	queryStore.load({
				params : {
					start : 0,
					limit : Constants.PAGE_SIZE
				}
			});
	// 确认按钮是否可用
	queryStore.on("load", function() {
				if (queryStore.getCount() == 0) {
					confirmBtn.setDisabled(true);
				} else {
					confirmBtn.setDisabled(false);
				}
			});
	var confirmBtn = new Ext.Button({
				text : '确认',
				id : 'confirmBtn',
				name : 'confirmBtn',
				iconCls : 'confirm',
				disabled : false,
				handler : function() {
					returnValues();
				}
			});
	// grid工具栏
	var headTbar = new Ext.Toolbar({
				region : "north",
				height : 25,
				items : ["领料单编号:", fuzzy, "-", {
							text : "查询",
							iconCls : Constants.CLS_QUERY,
							handler : function() {
								queryStore.load({
											params : {
												start : 0,
												limit : Constants.PAGE_SIZE
											}
										});
							}
						}, "-", confirmBtn]
			})
	// 查询grid
	var queryGrid = new Ext.grid.GridPanel({
				region : "center",
				border : false,
				sm : new Ext.grid.RowSelectionModel({
							singleSelect : true
						}),
				fitToFrame : true,
				store : queryStore,
				columns : [new Ext.grid.RowNumberer({
									header : "行号",
									width : 35
								}),
						// 领料单编号
						{
							header : "领料单编号",
							sortable : true,
							width : 80,
							dataIndex : 'issueNo'
						},
						// 单据状态
						{
							header : "单据状态",
							sortable : true,
							width : 100,
							renderer : IssueStatusValue,
							dataIndex : 'issueStatus'
						},
						// 需求计划单编号
						{
							header : "需求计划单编号",
							sortable : true,
							width : 100,
							dataIndex : 'mrNo'
						},
						// 申请领料人
						{
							header : "申请领料人",
							sortable : true,
							width : 100,
							renderer : function(val) {
								// 查找对应的申请人名字
								var url = "resource/getReceiptByName.action?receiptBy="
										+ val;
								var conn = Ext.lib.Ajax.getConnectionObject().conn;
								conn.open("POST", url, false);
								conn.send(null);
								if (conn.status == 200) {
									return conn.responseText;
								}

								return "";
							},
							dataIndex : 'receiptBy'
						},
						// 日期
						{
							header : "申请日期",
							sortable : true,
							width : 100,
							renderer : renderDate,
							dataIndex : 'dueDate'
						},
						// 日期
						{
							header : "备注",
							sortable : true,
							width : 200,
							dataIndex : 'memo'
						}],
				viewConfig : {
					forceFit : true
				},
				tbar : gridTbar,
				enableColumnMove : false,
				// 分页
				bbar : new Ext.PagingToolbar({
							pageSize : Constants.PAGE_SIZE,
							store : queryStore,
							displayInfo : true,
							displayMsg : Constants.DISPLAY_MSG,
							emptyMsg : Constants.EMPTY_MSG
						})
			});

			// check申请领用日期是否小于当前日期 
function checkdueDate() {
	var record = queryGrid.getSelectionModel().getSelected();
	var now = new Date();
	var nowdate = ChangeDateToString(now);
	 var duedate = record.get('dueDate');
	if (duedate < nowdate) 
		{
			Ext.Msg.alert('提示','申请领用日期已过，不能对其进行操作！');
			return false;
		}
		else {
			return true;
		}

	}
 queryGrid.on("rowdblclick", returnValues);
 // 返回数据
    function returnValues() {
        if (queryGrid.selModel.hasSelection() && checkdueDate()) {
			var smodel = queryGrid.getSelectionModel();
			var selected = smodel.getSelections();
			if (selected.length == 0) {
				Ext.Msg.alert(Constants.NOTICE, Constants.SELECT_NULL_DEL_MSG);
			} else {
					window.returnValue = selected[0].data.issueNo;
					window.close();
				
			}
        }
    }
	// 供应商查询 Panel
	var tabQuery = new Ext.Panel({
				layout : 'border',
				border : false,
				items : [headTbar, queryGrid]
			})

	new Ext.Viewport({
				enableTabScroll : true,
				layout : "fit",
				border : false,
				items : [{
							layout : 'fit',
							border : false,
							margins : '0 0 0 0',
							region : 'center',
							autoScroll : true,
							items : [tabQuery]
						}]
			});
	
	function findEmpName(empCode) {
		// 画面初始值设置
		Ext.lib.Ajax.request('POST', 'resource/getReceiptByName.action', {
					success : function(action) {
						var name = action.responseText;
						receiptBy.setValue(name);
						formPanel.fireEvent('doInit');
						hdnReceiptBy.fireEvent('valueChanged', true);
					}
				}, 'receiptBy=' + empCode);
	}
	function IssueStatusValue(value) {
		if (value == "0") {
			return "待审批状态";
		}
		if (value == "1") {
			return "审批中状态";
		}
		if (value == "2") {
			return "审批结束状态";
		}
		if (value == "9") {
			return "退回状态";
		}
	}

});