Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	Ext.QuickTips.init();

	var txtArrivalNo = new Ext.form.TextField({
		ieldLabel : '到货单号',
		width : 180,
		readOnly : false,
		id : "arrivalNo",
		name : "arrivalNo"
	});
	// grid工具栏
	var gridTbar = new Ext.Toolbar({
		border : false,
		items : ["到货单号:", txtArrivalNo, '-', {
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
				window.location.replace("../purchasewarehouse/RS001.jsp");
			}
		}]
	});
	// 物料异动grid
	var queryStore = new Ext.data.JsonStore({
		url : 'resource/findAfterPrintList.action',
		root : 'list',
		totalProperty : 'totalCount',
		fields : [
				// 单据号
				{
					name : 'purNo'
				},
				// 操作时间
				{
					name : 'operateDate'
				},
				// 到货单号
				{
					name : 'arrivalNo'
				}, {
					name : 'supplier'
				}, {
					name : 'supplyName'
				}, {
					name : 'whsNo'
				}, {
					name : 'whsName'
				}
				/** 仓库对应的所有物资 add by liuyi 20100430 仓库以 , 分割，此为 ;分割 */
				,{
					name : 'allMetailIds'
				}
				/** 仓库对应的 需求计划为固定资产类的物资 add by liuyi 20100430  */
				,{
					name : 'gdMetailIds'
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
					width : 80,
					sortable : true,
					hidden : true,
					dataIndex : 'purNo'
				},
				// 到货单号
				{
					header : "到货单号",
					width : 80,
					sortable : true,
					hidden : true,
					dataIndex : 'arrivalNo'
				}, {
					header : "whsNo",
					width : 80,
					hidden : true,
					sortable : true,
					dataIndex : 'whsNo'
				}, {
					header : "仓库",
					width : 150,
					sortable : true,
					hidden : true,
					dataIndex : 'whsName'
				},
				// 操作时间
				{
					header : "操作时间",
					width : 120,
					sortable : true,
					dataIndex : 'operateDate'
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
		if (txtArrivalNo.getValue() == "") {
			Ext.Msg.alert("提示", "请先输入到货单号!");
			return false;
		}
		// modified by liuyi 20100430
//		queryStore.load({
//			params : {
//				start : 0,
//				limit : Constants.PAGE_SIZE,
//				arrivalNo : txtArrivalNo.getValue()
//			}
//		});
		queryStore.baseParams = {
			arrivalNo : txtArrivalNo.getValue()
		}
		queryStore.load({
			params : {
				start : 0,
				limit : Constants.PAGE_SIZE
			}
		});
	}

	function printRecord() {
		var selected = transPanel.getSelectionModel().getSelections();
		var birtOperateDate;
		var birtArrivalNo;
		var birtSupplyName;
		var birtPurNo;
		var birtWhs;
		var flag;
		// add by liuyi 20100430 需求计划为固定资产类
		var allMetailIdsTemp;
		var gdMetailIdsTemp;
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请先选择一条记录！");
		} else {
			var menber = selected[0];
			birtOperateDate = menber.get('operateDate');
			birtArrivalNo = menber.get('arrivalNo');
			birtSupplyName = menber.get('supplyName');
			birtPurNo = menber.get('purNo');
			birtWhs = menber.get('whsNo');
			flag = 1;
			// add by liuyi 20100430 需求计划为固定资产类
			allMetailIdsTemp = menber.get('allMetailIds');
			gdMetailIdsTemp = menber.get('gdMetailIds');
			var whsno = [];
			whsno = birtWhs.split(",");
			// add by liuyi 20100430 处理需求计划为固定资产类
			// add by liuyi 20100430 对应仓库数组下所有的物资
			// 数组中元素为数据
			var allMetailIds = new Array();
			// add by liuyi 20100430
			// 对应仓库数组下所有来源为固定资产类的物资 数组元素为数组
			var gdMetailIds = new Array();
			allMetailIds = allMetailIdsTemp.split(";");
			gdMetailIds = gdMetailIdsTemp.split(";");
			for (var i = 0; i < whsno.length; i++) {
				var whsNoName = whsno[i];
				// modified by liuyi 20100430 判断明细物资来源为固定资产类
				var all = allMetailIds[i];
				var gd = gdMetailIds[i];
				if (all == gd) {
					;
				} else {
					var url = "/powerrpt/report/webfile/receiptBill.jsp?operateDate="
							+ birtOperateDate
							+ "&arrivalNo="
							+ birtArrivalNo
							+ "&purNo="
							+ birtPurNo
							+ "&flag="
							+ flag
							+ "&whsName=" + whsNoName
							+ "&metailIdNotIn=" + gd;
					window.open(url);
				}
				
				// add by liuyi 20100430 固定资产类物资补打印
				var gdAddIds = gd.split(",");
				for (var j = 0; j < gdAddIds.length; j++) {
					if (gdAddIds[j] != null && gdAddIds[j] != '') {
						var urlAdd = "/powerrpt/report/webfile/receiptBillAddOfGd.jsp?operateDate="
								+ birtOperateDate
								+ "&arrivalNo="
								+ birtArrivalNo
								+ "&purNo="
								+ birtPurNo
								+ "&flag="
								+ flag
								+ "&whsName="
								+ whsNoName
								+ "&materialId=" + gdAddIds[j];
						window.open(urlAdd);
					}
				}
			}
		}
	}

});