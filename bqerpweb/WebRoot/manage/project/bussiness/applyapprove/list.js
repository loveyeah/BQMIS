Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	// var isWo = "";
	// var isMa = "";
	var tabpanel = parent.Ext.getCmp('maintab');
	// 将时间转成字符串格式
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

	// 系统当前时间
	var enddate = new Date();
	// 系统到现在前三十天
	var startdate = new Date();
	startdate.setDate(enddate.getDate() - 30);
	// startdate = startdate.getFirstDateOfMonth();
	var sdate = ChangeDateToString(startdate);
	var edate = ChangeDateToString(enddate);

	// 年份
	// 立项审批申请时间
	var timefromDate = new Ext.form.DateField({
				format : 'Y-m-d',
				name : '_timefromDate',
				id : 'timefromDate',
				itemCls : 'sex-left',
				clearCls : 'allow-float',
				checked : true,
				allowBlank : false,
				readOnly : true,
				value : sdate,
				fieldLabel : "立项审批申请时间",
				emptyText : '请选择',
				anchor : '90%'
			});
	var timetoDate = new Ext.form.DateField({
				format : 'Y-m-d',
				name : '_timetoDate',
				value : edate,
				id : 'timetoDate',
				itemCls : 'sex-left',
				clearCls : 'allow-float',
				allowBlank : false,
				readOnly : true,
				fieldLabel : "至",
				emptyText : '请选择',
				anchor : '80%'
			});
	// 待签
	var peopleWaitRadio = new Ext.form.Radio({
				boxLabel : '待签',
				hideLabel : true,
				id : 'peopleWaitRadio',
				name : 'actionId',
				// inputValue : '1',
				checked : true,
				listeners : {
					check : function(radio, checked) {
					}
				}
			});

	// 已签
	var peopleSignRadio = new Ext.form.Radio({
				boxLabel : '已签',
				hideLabel : true,
				id : 'peopleSignRadio',
				name : 'actionId',
				// inputValue : '1',
				listeners : {
					check : function(radio, checked) {
					}
				}
			});

	// 按钮
	var btnQuery = new Ext.Button({
				id : 'btnQuery',
				iconCls:'query',
				text : '查  询',
				minWidth : 80,
				handler : function() {
					if (peopleWaitRadio.checked) {
						var isWait = "Y";
					}
					if (peopleSignRadio.checked) {
						var isSign = "Y";
					}
					con_ds.reload(
//						{
//								params : {
//									prjStatusType : 1,
//									timefromDate : ChangeDateToString(timefromDate
//											.getValue()),
//									timetoDate : ChangeDateToString(timetoDate
//											.getValue()),
//									isWait : isWait,
//									isSign : isSign,
//									start : 0,
//									limit : 18
//
//								}
//							}
							);
				}
			});

	var btnInfo = new Ext.Button({
				id : 'btnInfo',
				text : '签字处理',
				iconCls:'approve',
				minWidth : 80,
				handler : function() {
					var record = Grid.getSelectionModel().getSelected();
					if (record) {
						var url = "../signProject/approveProject.jsp";
						var args = new Object();
						args.prjNo = record.get("prjjInfo.prjNo");
						args.entryId = record.get("prjjInfo.accWorkFlowNo");
						args.workflowType = "hfProject1";
						args.prjTypeId = record.get("prjjInfo.prjTypeId");
						args.prjStatus = record.get("prjjInfo.prjStatus");
						window.showModalDialog(url, args,
										'status:no;dialogWidth=750px;dialogHeight=550px');
						con_ds.load();
					} else {
						Ext.Msg.alert("提示", "请选择一条项目记录！")
					}
				}
			});
// 票面浏览
	function ApproveRptPreview() {

		var selected = Grid.getSelectionModel().getSelections();
		var projectId;
		var prjTypeId;
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要浏览的项目记录！");
		} else {
			var menber = selected[0];
			projectId = menber.get('prjjInfo.id');
			prjTypeId = menber.get('prjjInfo.prjTypeId');
			var url;
			if(prjTypeId == 42){
			url = "/powerrpt/report/webfile/bqmis/LXprojectapply.jsp?projectId="
					+ projectId;
			}else{
			url = "/powerrpt/report/webfile/bqmis/projectapply.jsp?projectId="
					+ projectId;
					}
			window.open(url);

		}
	};

	var btnRptPreview = new Ext.Button({
				iconCls : 'pdfview',
				id : 'btnReport',
				text : '立项票面预览',
				minWidth : 80,
				handler : ApproveRptPreview
			});
	var queryField = new Ext.form.FieldSet({
				title : '查询条件',
				height : '100%',
				
				layout : 'form',
				bodyStyle : "padding:0px 0px 0px 0px",
				items : [{
							layout : 'column',
							border : false,
							items : [{
										border : false,
										columnWidth : 0.31,
										labelWidth : 105,
										layout : 'form',
										items : [timefromDate]
									}, {
										border : false,
										labelWidth : 50,
										columnWidth : 0.25,
										layout : 'form',
										items : [timetoDate]
									},
//									{
//										border : false,
//										columnWidth : 0.1,
//										layout : 'form',
//										items : [peopleWaitRadio]
//									}, {
//										border : false,
//										columnWidth : 0.1,
//										layout : 'form',
//										items : [peopleSignRadio]
//									},
									{
										columnWidth : 0.12,
										layout : 'form',
										border : false,
										items : [btnQuery]

									}
//									, {
//										columnWidth : 0.12,
//										layout : 'form',
//										border : false,
//										items : [btnInfo]
//									}
									]
						}]
			});
	var form = new Ext.form.FormPanel({
				bodyStyle : "padding:20px 5px 5 5",
				labelAlign : 'left',
				id : 'shift-form',
				labelWidth : 80,
				autoHeight : true,
				region : 'center',
				layout : 'column',
				border : false,
				items : [{
							columnWidth : 1,
							layout : 'form',
							border : false,
							items : [queryField]

						}]
			});
	// /////////////////////////////////////////////////////////////////////////////

	var con_item = Ext.data.Record.create([{
				name : 'prjjInfo.prjYear'
			}, {
				name : 'prjjInfo.accWorkFlowNo'
			}, {
				name : 'prjjInfo.prjTypeId'
			}, {
				name : 'prjjInfo.prjName'
			}, {
				name : 'prjjInfo.prjNoShow'
			}, {
				name : 'prjjInfo.prjNo'
			}, {
				name : 'prjjInfo.prjStatus'
			}, {
				name : 'prjjInfo.planAmount'
			}, {
				name : 'prjjInfo.chargeBy'
			}, {
				name : 'prjjInfo.prjContent'
			}, {
				name : 'prjjInfo.planStartDate'
			}, {
				name : 'prjjInfo.planEndDate'
			}, {
				name : 'prjjInfo.itemId'
			},{
				name : 'prjjInfo.planTimeLimit'
			}, {
				name : 'prjjInfo.memo'
			}, {
				name : 'prjjInfo.annex'
			}, {
				name : 'prjjInfo.id'
			}, {
				name : 'chargeByName'
			}, {
				name : 'prjTypeName'
			}, {
				name : 'changeDepName'
			}, {
				name : 'enterByName'
			}, {
				name : 'planStartDate'
			}, {
				name : 'planEndDate'
			}, {
				name : 'entryDate'
			}, {
				name : 'prjStatus'
			}, {
				name : 'prjStatusId'
			}]);
	var con_sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true

			});
	var con_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'manageproject/getProjectApplyList.action'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : "totalCount",
							root : "list"
						}, con_item)
			});
	con_ds.load({
				params : {
					prjStatusType : 1,
					entryType : 'approve',
					workFlowType : "hfProject1",
					timefromDate : ChangeDateToString(timefromDate.getValue()),
					timetoDate : ChangeDateToString(timetoDate.getValue()),
					start : 0,
					limit : 18

				}
			});
	con_ds.on('beforeload', function() {
				if (peopleWaitRadio.checked) {
					var isWait = "Y";
				}
				if (peopleSignRadio.checked) {
					var isSign = "Y";
				}
				Ext.apply(this.baseParams, {
							prjStatusType : 1,
							timefromDate : ChangeDateToString(timefromDate
									.getValue()),
							timetoDate : ChangeDateToString(timetoDate
									.getValue()),
							isWait : isWait,
							entryType : 'approve',
							workFlowType : "hfProject1",
							isSign : isSign

						});
			});
	var con_item_cm = new Ext.grid.ColumnModel([con_sm,
			new Ext.grid.RowNumberer({
						header : '序号',
						width : 50,
						align : 'center'
					}),

			{
				header : '项目编号',
				dataIndex : 'prjjInfo.prjNoShow',
				align : 'center'

			}, {
				header : '项目名称',
				dataIndex : 'prjjInfo.prjName',
				align : 'center'
			}, {
				header : '项目类别',
				dataIndex : 'prjTypeName',
				align : 'center'
			}, {
				header : '项目状态',
				dataIndex : 'prjStatus',
				align : 'center'
				// renderer : function chargeIt(v) {}
		}	, {
				header : '项目年份',
				dataIndex : 'prjjInfo.prjYear',
				align : 'center'
			}, {
				header : '预算费用',
				dataIndex : 'prjjInfo.planAmount',
				align : 'center'
			}, {
				header : '工程负责人',
				dataIndex : 'chargeByName',
				align : 'center'
			}]);
	con_item_cm.defaultSortable = true;

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
				// title : '合同列表',
				width : Ext.get('div_lay').getWidth(),
				split : true,
				autoScroll : true,
				bbar : gridbbar,
				 tbar : [btnInfo,btnRptPreview],
				border : false,
				viewConfig : {
					forceFit : true
				}
			});

	Grid.on('rowdblclick', modifyBtn);
	function modifyBtn() {
		var rec = Grid.getSelectionModel().getSelected();
		if (rec) {
			parent.myRecord = rec;
			if (parent.document.all.iframe2 != null) {
				parent.document.all.iframe2.src = "manage/project/bussiness/applyapprove/baseInfo.jsp";
			}
			tabpanel.setActiveTab(1);
		}
	}

	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				items : [{
							region : "north",
							layout : 'fit',
							height : 100,
							border : true,
							split : true,
							margins : '0 0 0 0',
							items : [form]
						}, {

							region : "center",
							layout : 'fit',
							title : '立项列表',
							border : false,
							
							split : true,
							margins : '0 0 0 0',
							items : [Grid]
						}]
			});

});