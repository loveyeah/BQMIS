Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	// 初始化时间
	function ChangeDateToString(DateIn) {
		var Year = 0;
		var Month = 0;
		var Day = 0;
		var CurrentDate = "";

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
	var startdate = date.add(Date.DAY, -30);
	var enddate = date;
	var sdate = ChangeDateToString(startdate) + " 00:00:00";
	var edate = ChangeDateToString(enddate) + " 23:59:59";

	// 定义grid
	var MyRecord = Ext.data.Record.create([{
				name : 'conModifyId'
			}, {
				name : 'conId'
			}, {
				name : 'workflowStatus'
			}, {
				name : 'conModifyNo'
			}, {
				name : 'contractName'
			}, {
				name : 'clientName'
			}, {
				name : 'conomodifyType'
			}, {
				name : 'operateName'
			}, {
				name : 'operateDeptName'
			}, {
				name : 'signStartDate'
			}, {
				name : 'signEndDate'
			}, {
				name : 'currencyType'
			}, {
				name : 'modiyActAmount'
			}, {
				name : 'workFlowNo'
			}]);

	var dataProxy = new Ext.data.HttpProxy(

	{
				url : 'managecontract/findConModifyList.action'
			}

	);

	var theReader = new Ext.data.JsonReader({
				root : "list",
				totalProperty : "totalCount"

			}, MyRecord);

	var store = new Ext.data.Store({

				proxy : dataProxy,

				reader : theReader

			});

	// 分页
	var fuzzy = new Ext.form.TextField({
				id : "fuzzy",
				name : "fuzzy",
				width : 180,
				emptyText : "合同变更编号/合同名称/合作伙伴"
			});
	var startDate = new Ext.form.TextField({
				id : 'startDate',
				fieldLabel : "",
				name : 'startDate',
				style : 'cursor:pointer',
				width : 110,
				value : sdate,
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d 00:00:00',
									dateFmt : 'yyyy-MM-dd HH:mm:ss',
									alwaysUseStartDate : true
								});
					}
				}
			})

	var endDate = new Ext.form.TextField({
				id : 'endDate',
				fieldLabel : "",
				name : 'endDate',
				style : 'cursor:pointer',
				width : 110,
				value : edate,
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d 00:00:00',
									dateFmt : 'yyyy-MM-dd HH:mm:ss',
									alwaysUseStartDate : true
								});
					}
				}
			})
	var sm = new Ext.grid.CheckboxSelectionModel();
				
		
	var grid = new Ext.grid.GridPanel({
				region : "center",
				store : store,
				autoScroll : true,
				columns : [sm, new Ext.grid.RowNumberer({
									header : '项次号',
									width : 50,
									align : 'center'
								}), {

							header : "ID",
							width : 100,
							sortable : true,
							dataIndex : 'conModifyId',
							hidden : true
						}, {

							header : "合同ID",
							width : 100,
							sortable : true,
							dataIndex : 'conId',
							hidden : true
						}, {

							header : "会签状态",
							width : 100,
							sortable : true,
							align : 'center',
							dataIndex : 'workflowStatus',
							renderer : function changeIt(val) {
								if (val == "0")
									return "未上报";
								if (val == "1")
									return "审批中";
								if (val == "3")
									return "已退回";
								if (val == "2")
									return "已审批";
							}
						},

						{
							header : "合同变更编号",
							width : 120,
							sortable : true,
							dataIndex : 'conModifyNo',
							align : 'center'
						},

						{
							header : "合同名称",
							width : 100,
							sortable : true,
							dataIndex : 'contractName',
							align : 'center'
						},

						{
							header : "合作伙伴",
							width : 100,
							sortable : true,
							dataIndex : 'clientName',
							align : 'center'
						}, {
							header : "变更类型",
							width : 100,
							sortable : true,
							dataIndex : 'conomodifyType',
							align : 'center',
							renderer : function changeIt(val) {
								if (val == "1")
									return "合同变更";
								if (val == "2")
									return "合同解除";

							}
						}, {
							header : "经办人",
							width : 100,
							sortable : true,
							dataIndex : 'operateName',
							align : 'center'
						}, {
							header : "申请部门",
							width : 100,
							sortable : true,
							dataIndex : 'operateDeptName',
							align : 'center'
						}, {
							header : "会签开始时间",
							width : 100,
							sortable : true,
							dataIndex : 'signStartDate',
							align : 'center'
						}, {
							header : "会签结束时间",
							width : 100,
							sortable : true,
							hidden : true,
							dataIndex : 'signEndDate',
							align : 'center'
						}, {
							header : "币别",
							width : 100,
							sortable : true,
							dataIndex : 'currencyType',
							hidden : true,
							align : 'center'
						}, {
							header : "总金额",
							width : 100,
							sortable : true,
							dataIndex : 'modiyActAmount',
							hidden : true,
							align : 'center'
						}],
				sm : sm,
				tbar : ['会签时间：  从', startDate, '至', endDate, '  ', fuzzy, {
							text : "查询",
							id : "btnQuery",
							iconCls : 'query',
							handler : queryRecord
						}, '-', {
							text : "修改",
							iconCls : 'update',
							handler : updateRecord
						}, '-', {
							text : "会签审批查询",
							iconCls : 'view',
							handler : showWorkFlow
						}, '-', {
							text : "会签表",
							iconCls : 'pdfview',
								handler : CheckRptPreview
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
   // 票面浏览
	function CheckRptPreview() {
	var selrows = grid.getSelectionModel().getSelections();
		if (selrows.length > 0) {
//			var entryId = selrows[0].data.workFlowNo;
//			if (entryId == null || entryId == "") {
//				Ext.Msg.alert('提示', '流程尚未启动！');
//			} else {
				var modifyId =  selrows[0].data.conModifyId;
				var url = "/powerrpt/report/webfile/bqmis/GCContractModifyMeetSign.jsp?modifyId="
						+ modifyId;
				window.open(url);
//			}
		} else {
			Ext.Msg.alert('提示', '请从列表中选择一条记录！');
		}
	};	
	function showWorkFlow() {
		var selrows = grid.getSelectionModel().getSelections();
		if (selrows.length > 0) {
			var entryId = selrows[0].data.workFlowNo;
			if (entryId == null || entryId == "") {
				url = "/power/workflow/manager/flowshow/flowshow.jsp?flowCode="
								+ "prjConModify";
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
	grid.on('rowdblclick', updateRecord);
	function updateRecord() {
		if (grid.selModel.hasSelection()) {
			var records = grid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑！");
			} else {
				var record = grid.getSelectionModel().getSelected();
//				if(record.get("workflowStatus") != 0){
//				Ext.Msg.alert("系统提示信息", "请选择其中未上报合同进行编辑！");
//				return;
//				}
				parent.Ext.getCmp("maintab").setActiveTab(2);
				parent.Ext.getCmp("maintab").setActiveTab(1);
				
				url2 = "manage/projectFact/business/modifyMeetSign/register/tabPayPlan/payplan.jsp";
				var id = record.get("conModifyId");
				var conId = record.get("conId");
				var totalMoney = record.get("modiyActAmount");
				var currencyType = record.get("currencyType");
				parent.document.all.iframe3.src = url2 + "?id=" + id + "&conId="
						+ conId + "&totalMoney=" + totalMoney
						+ "&currencyType=" + currencyType;
						
				var url1 = "manage/projectFact/business/modifyMeetSign/register/tabBase/base.jsp";
				parent.document.all.iframe2.src = url1 + "?id="
						+ record.get("conModifyId") + "&conId="
						+ record.get("conId")+"&workflowStatus="
						+record.get("workflowStatus");
					//传工作流状态，修改by 何学良 09/04/08	
						
			}
		} else {
			Ext.Msg.alert("提示", "请先选择要编辑的行!");
		}
	}
	function queryRecord() {
		var ftime = Ext.get('startDate').dom.value;
		var ttime = Ext.get('endDate').dom.value;
		if (ftime > ttime) {
			Ext.Msg.alert('提示', '选择后一日期应比前一日期大！');
			return false;
		}
		store.load({
					params : {
						conTypeId : 2,
						start : 0,
						limit : 18,
						fuzzy : fuzzy.getValue(),
						startDate : Ext.get('startDate').dom.value,
						endDate : Ext.get('endDate').dom.value
					}
				});
	}
	// ---------------------------------------
	new Ext.Viewport({
				enableTabScroll : true,
				layout : "fit",
				items : [grid]
			});

	queryRecord();

});