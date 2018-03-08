var start=0;
var limit=18;
var StartAndEndDate;
var applyId;
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
	//项目名称输入
	var projectName = new Ext.form.TextField({
				id : "projectName",
				name : "projectName",
				width : 200,
				emptyText : ""
			});
	//修改
	function update() {
				var selrows = Grid.getSelectionModel().getSelections();
					if (selrows.length == 1) {
					var record = Grid.getSelectionModel().getSelected();
					applyId=record.get('applyId');
					CheckRptPreview();
				} else {
					Ext.Msg.alert('提示', '请选择一条记录进行修改！');
				}
			};
	// 会签表浏览
	function CheckRptPreview() {
		if (applyId == "" || applyId == null) {
			Ext.Msg.alert('提示', '请在审批列表中选择待签字的申请单！');
			return false;
		}
		var url = "/powerrpt/report/webfile/projectApplyReport.jsp?reportId="
				+ applyId;
		window.open(url);

	};
	//查询
	function query() {
	con_ds.baseParams = {
			ContractName : projectName.getValue(),
			status:statusValue.getValue(),
			flag:'queryAll'
	};
	con_ds.load({
					params : {
						ContractName : projectName.getValue(),
						status:statusValue.getValue(),
						start : start,
						limit : limit
					}
				})
	};
	//查询按钮
	var queryB=new Ext.Button({
			id : 'btnQuery',
			text : "查询",
			iconCls : 'query',
			handler : query
	});
	//状态类型
var statusValue = new Ext.form.ComboBox({
		store : [['', '全部'], ['0', '未上报'], ['1', '已上报'], ['2', '发包部门负责人已签字'], ['3', '公共安全监察主管已签字'], ['4', '安全监察部负责人已签字'], ['5', '综合公司办理发包工程出入证已签字']],
		value : '',
		//id : 'smartDate',
		name : 'status',
		width:220,
		valueField : "value",
		displayField : "text",
		fieldLabel : "状态选择：",
		mode : 'local',
		readOnly : true,
		anchor : '140%',
		typeAhead : true,
		forceSelection : true,
		editable : false,
		triggerAction : 'all',
		disabled : false,
		selectOnFocus : true
	});
	//修改按钮
	var updateB=new Ext.Button({
			id : 'btnUpdate',
			text : "详细查看会签表",
			iconCls : 'pdfview',
			handler : update
	});
	//项目开工申请单列表工具栏
	var contbar = new Ext.Toolbar({
		items : ['项目名称：',projectName, '-',statusValue, '-',queryB,  '-',updateB]
	});
	var record = Ext.data.Record.create([{
		name : 'applyId',
		mapping : 0
	}, {
		name : 'contractName'
		,mapping : 1
	}, {
		name : 'conId'
		,mapping : 2
	}, {
		name : 'startDate'
		,mapping : 3
	}, {
		name : 'endDate'
		,mapping : 4
	}, {
		name : 'contractorName'
		,mapping : 5
	}, {
		name : 'contractorId'
		,mapping : 6
	}, {
		name : 'chargeBy'
		,mapping : 7
	}, {
		name : 'testResult'
		,mapping : 8
	}, {
		name : 'authorizeItem'
		,mapping : 9
	}, {
		name : 'personRegister'
		,mapping : 10
	}, {
		name : 'articleRegister'
		,mapping : 11
	}, {
		name : 'idCard'
		,mapping : 12
	}, {
		name : 'operateCard'
		,mapping : 13
	}, {
		name : 'cautionMoney'
		,mapping : 14
	}, {
		name : 'handInCadr'
		,mapping : 15
	}, {
		name : 'enterBy'
		,mapping : 16
	}, {
		name : 'EntryDate'
		,mapping : 17
	}, {
		name : 'workFlowNo'
		,mapping : 18
	}, {
		name : 'statusId'
		,mapping : 19
	}, {
		name : 'entryByCode'
		,mapping : 20
	}, {
		name : 'prjName'
		,mapping : 22
	}]);
	//单选框
	var con_sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		listeners : {
			rowselect : function(sm, row, rec) {
			}
		}
	});
	var con_item_cm = new Ext.grid.ColumnModel([con_sm,
			{
				header : '立项名称',
				dataIndex : 'prjName',
				align : 'center',
				width :200
			}, {
				header : '项目名称',
				dataIndex : 'contractName',
				align : 'center',
				width :200
			}, {
				header : '工程开始时间',
				id:'startDate',
				dataIndex : 'startDate',
				align : 'center',
				hidden: true,
				renderer : function(v) {
					if(v!=null&&v!=""){
					v = v.substring(0,10);
					var vtemp  = v.split('-');
					if (vtemp.length!=3) {
						return false;
					}
					v = vtemp[0]+'年'+vtemp[1]+'月'+vtemp[2]+'日';
					StartAndEndDate=v;
					}
				}
			},{
				header : '工程结束时间',
				id:'startDate',
				dataIndex : 'endDate',
				align : 'center',
				hidden: true,
				renderer : function(v) {
					if(v!=null&&v!=""){
					v = v.substring(0,10);
					var vtemp  = v.split('-');
					if (vtemp.length!=3) {
						return false;
					}
					v = vtemp[0]+'年'+vtemp[1]+'月'+vtemp[2]+'日';
					StartAndEndDate+="-"+v;
					}
				}
			}, {
				header : '工程起始时间',
				width: 250,
				align : 'center',
				renderer : function(v) {
				  return StartAndEndDate;
				}
			}, {
				header : '承包单位名称',
				width: 200,
				dataIndex : 'contractorName',
				align : 'center'
				
			}, {
				header : '承包方负责人',
				dataIndex : 'chargeBy',
				align : 'center'
			},{
				header : '状态',
				dataIndex : 'statusId',
				align : 'center',
				width: 250,
				renderer : function(v) {
					if(v==0){
						return "未上报";
					}else if (v==1) {
						return "已上报";
					}else if (v==2) {
						return "发包部门负责人已签字";
					}else if (v==3) {
						return "公共安全监察主管已签字";
					}else if (v==4) {
						return "安全监察部负责人已签字";
					}else if (v==5) {
						return "综合公司办理发包工程出入证已签字";
					}else {
						return "";
					}
				}
			}]);
	con_item_cm.defaultSortable = true;
	var con_ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'manageproject/finPrjApplyByContractNameAndStatus.action'
		}),
		reader : new Ext.data.JsonReader({
			root : "list",
		totalProperty : "totalCount"
		}, record)
	});
	//底部分页工具栏
	var gridbbar = new Ext.PagingToolbar({
		pageSize : limit,
		store : con_ds,
		displayInfo : true,
		displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
		emptyMsg : "没有记录",
		beforePageText : '页',
		afterPageText : "共{0}"
	});
	//项目开工申请单列表
	var Grid = new Ext.grid.GridPanel({
		ds : con_ds,
		cm : con_item_cm,
		sm : con_sm,
		title : '项目开工申请单列表',
		width : Ext.get('div_lay').getWidth(),
		split : true,
		autoScroll : true,
		bbar : gridbbar,
		tbar : contbar,
		border : false,
		viewConfig : {
			forceFit : true
		}
	});
	Grid.on('rowdblclick', function(grid, rowIndex, e) {
		var record = Grid.getSelectionModel().getSelected();
					applyId=record.get('applyId');
					CheckRptPreview();
	});
	query();
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		autoHeight:true,
		items : [Grid]
	});
})
