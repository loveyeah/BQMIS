Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	Ext.QuickTips.init();

	var txtIssueNo = new Ext.form.TextField({
		ieldLabel : '领料单号',
		width : 180,
		readOnly : false,
		id : "orderNo",
		name : "orderNo"
	});
	// grid工具栏
	var gridTbar = new Ext.Toolbar({
		border : false,
		items : ["领料单号:", txtIssueNo, '-', {
			text : "查询",
			iconCls : 'query',
			handler : gridFresh
		}, '-', {
			text : "打印",
			iconCls : 'print',
			handler : printRecord
		}, '-', {
			text : "返回",
			iconCls : 'untread',
			handler : function() {
			window.location.replace("../issue/RS004.jsp");
		}
		}]
	});
	// 物料异动grid
	var queryStore = new Ext.data.JsonStore({
		url : 'resource/findAfterPrintIssueList.action',
		root : 'list',
		totalProperty : 'totalCount',
		fields : [
				// 单据号
				{
					name : 'orderNo'
				},
				// 操作时间
				{
					name : 'operatedDate'
				},
				{
					name : 'whsNo'
				},
				{
					name : 'whsName'
				}
				// add by liuyi 20100201 增加领料单的单据种类 用memo存储
				,{
					name : 'memo'
				}
				// 物资
				,{
					name : 'materialNo'
				}
				]
	});
	// 载入数据
	// queryStore.load({
	// params : {
	// start : 0,
	// limit : Constants.PAGE_SIZE
	// }
	// });

	// grid
	var transPanel = new Ext.grid.GridPanel({
		region : "center",
		enableColumnMove : false,
		enableColumnHide : true,
		frame : false,
		border : false,
		store : queryStore,
		// 单选
		sm : new Ext.grid.RowSelectionModel({
			singleSelect : true
		}),
		columns : [new Ext.grid.RowNumberer({
			header : "行号",
			width : 35
		}),
				// 单据号
				{
					header : "单据号",
					width : 55,
					sortable : true,
					hidden :true,
					dataIndex : 'orderNo'
				},{
					header : "whsNo",
					width : 150,
					sortable : true,
					hidden : true,
					dataIndex : 'whsNo'
				},{
					header : "仓库",
					width : 150,
					sortable : true,
					hidden :true,
					dataIndex : 'whsName'
				},
				// 操作时间
				{
					header : "操作时间",
					width : 120,
					sortable : true,
					dataIndex : 'operatedDate'
				}],
		tbar : gridTbar,
		// 分页
		bbar : new Ext.PagingToolbar({
			pageSize : Constants.PAGE_SIZE,
			store : queryStore,
			displayInfo : true,
			displayMsg : Constants.DISPLAY_MSG,
			emptyMsg : Constants.EMPTY_MSG
		})
	});

	transPanel.on("rowdblclick", printRecord);

	// 显示区域
	var layout = new Ext.Viewport({
		layout : "border",
		border : false,
		defaults : {
			autoScroll : true
		},
		items : [transPanel]

	});
	// ↓↓*******************************处理****************************************
	// 查询处理函数
	function gridFresh() {
		if (txtIssueNo.getValue() == "") {
			Ext.Msg.alert("提示", "请先输入领料单号!");
			return false;
		}
		queryStore.baseParams = {
			issueNo : txtIssueNo.getValue()
		}
		queryStore.load({
			params : {
				start : 0,
				limit : Constants.PAGE_SIZE
//				issueNo : txtIssueNo.getValue()
			}
		});
	}

	function printRecord() {
		var selected = transPanel.getSelectionModel().getSelections();
		var birtEntryDate;
		var birtIssueNo;
		var birtWhsNo;
		var flag;

		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请先选择一行记录！");
		} else {
			var menber = selected[0];
			birtEntryDate = menber.get('operatedDate');
			birtIssueNo = menber.get('orderNo');
			birtWhsNo = menber.get('whsNo');
			materialNo = menber.get('materialNo')
			flag = 1;
			var whsno = [];
			 whsno = birtWhsNo.split(",");
			 
			 var materiTemp = []
			 if(materialNo != null && materialNo != '')
			 	materiTemp = materialNo.split(',')
			for (var i = 0; i < whsno.length; i++) {
				var whsNoName = whsno[i];
							
			var url = '';
			// modofied by liuyi 20100201 固定资产类单据有几个物资出几张单子
			if(menber.get('memo') != null && menber.get('memo') != '1')
			{
				url = "/powerrpt/report/webfile/issueDetails.jsp?issueNo="
					+ birtIssueNo + "&operatedDate=" + birtEntryDate + "&flag="
					+ flag+ "&whsName="+ whsNoName;
			}
			else 
			{
				
				url = "/powerrpt/report/webfile/issueDetails.jsp?issueNo="
					+ birtIssueNo + "&operatedDate=" + birtEntryDate + "&flag="
					+ flag+ "&whsName="+ whsNoName
					// 固定资产类标记 gdFlag=1
					+ "&gdFlag=1"
					+ "&materialId="
					+ materiTemp[i];
			}
			window.open(url);
			}
		}
	}

});