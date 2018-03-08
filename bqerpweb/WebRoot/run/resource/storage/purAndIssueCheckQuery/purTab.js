Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	
	// 定义grid
var MyRecord = Ext.data.Record.create([
	{name : 'id',mapping : 0},
    {name : 'purNo',mapping : 1},
	{name : 'supplier',mapping : 2},
	{name : 'supplyName',mapping :3},
	{name : 'orderDetailModifiedDate',mapping :4},
	{name : 'contractNo',mapping :5},
	{name : 'invoiceNo',mapping :6},
	{name : 'memo',mapping :7},
	{name : 'buyerName',mapping :8},
	{name : 'totalPrice',mapping :9}
	
	]);

	var dataProxy = new Ext.data.HttpProxy(

			{
				url:'resource/queryCheckedPurList.action'
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
	
//		store.on("load",function(){
//
//		//----------add by fyyang 091226------加合计行--------
//		var totalPrice=0;//到货数
//				
//				for (var j = 0; j < store.getCount(); j++) {
//					var temp = store.getAt(j);
//					if (temp.get("totalPrice") != null) {
//						totalPrice = parseFloat(totalPrice)
//								+ parseFloat(temp.get("totalPrice"));
//					}
//					
//				}
//		var mydata=new MyRecord({
//			purNo:'合计',
//		totalPrice :totalPrice
//		});
//		if(store.getCount()>0)
//		{
//		 	store.add(mydata);
//		}
//	});

	
		
	//------------查询条件----------------
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
	var enddate = new Date();
	var startdate = enddate.add(Date.MONTH, -1);
	startdate = startdate.getFirstDateOfMonth();
	var sdate = ChangeDateToString(startdate);
	var edate = ChangeDateToString(enddate);
    var timefromDate = new Ext.form.TextField({
		id : 'timefromDate',
		name : '_timefromDate',
		fieldLabel : "开始",
		style : 'cursor:pointer',
		cls : 'Wdate',
		width : 90,
		value : sdate,
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-01',
					dateFmt : 'yyyy-MM-dd',
					alwaysUseStartDate : true
				});
			}
		}
	});
	var timetoDate = new Ext.form.TextField({
		name : '_timetoDate',
		value : edate,
		id : 'timetoDate',
		fieldLabel : "结束",
		style : 'cursor:pointer',
		cls : 'Wdate',
		width : 90,
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-01',
					dateFmt : 'yyyy-MM-dd',
					alwaysUseStartDate : true
				});
			}
		}
	});
 
	var txtPurNo = new Ext.form.TextField({
		fieldLabel : "采购单号",
		id : "txtPurNo",
		name : "txtPurNo"
	});
	var txtBuyer=new Ext.form.TextField({
		fieldLabel : "采购员",
		id : "txtBuyer",
		name : "txtBuyer"
	});
	
	var txtInvoiceNo=new Ext.form.TextField({
		fieldLabel : "发票号",
		id : "txtInvoiceNo",
		name : "txtInvoiceNo"
	});
	//------------------------------------------------------------
	var sm = new Ext.grid.CheckboxSelectionModel();

	var grid = new Ext.grid.GridPanel({
		region : "center",
		layout : 'fit',
		store : store,

		columns : [
		sm, new Ext.grid.RowNumberer({header:'序号',width : 50}),{
			
			header : "ID",
			width : 75,
			sortable : true,
			dataIndex : 'id',
			hidden:true
		},
		{
			header : "采购单号",
			width : 75,
			sortable : true,
			dataIndex : 'purNo'
		},
		{
			header : "供应商",
			width : 75,
			sortable : true,
			dataIndex : 'supplyName'
		},
		{
			header : "发票号",
			width : 75,
			sortable : true,
			dataIndex : 'invoiceNo'
		},
		{
			header : "采购员",
			width : 75,
			sortable : true,
			dataIndex : 'buyerName'
		},
		{
			header : "单据金额",
			width : 75,
			sortable : true,
			dataIndex : 'totalPrice',
			renderer:function (value)
			{
				if(value==null) return value;
			    else	return value.toFixed(2);
			}
		},
		{
			header : "备注",
			width : 75,
			sortable : true,
			dataIndex : 'memo'
		}
		],
		sm : sm,
		autoSizeColumns : true,
		viewConfig : {
			forceFit : true
		},
		tbar : ['审核时间：',timefromDate,'~', timetoDate,'-','采购单号',txtPurNo,'-',
			    '采购员',txtBuyer,'-','发票号',txtInvoiceNo,'-',
				{
			text : "查询",
			iconCls : 'query',
			handler : queryRecord
		},{
			     text:'查看采购单信息',
			     iconCls : 'query',
			     handler:queryPurInfo
			  }, '-',{
			text : '查看需求计划物资信息',
			handler : function() {
				var sm =  grid.getSelectionModel();
				var selected = sm.getSelections();
				var ids = [];
				if (selected.length == 0) {
					Ext.Msg.alert("提示", "请选择要查看的记录！");
				} else if (selected.length>1){
					Ext.Msg.alert("提示", "请选择一条记录！");
				}
				else
				{
					
					var url = "../issueCheck/planMaterialQuery.jsp";
				var args = new Object();
				args.orderNo = selected[0].data.purNo;
				args.flag="1";
				var location = window
						.showModalDialog(
								url,
								args,
								'dialogWidth=800px;dialogHeight=500px;center=yes;help=no;resizable=no;status=no;');

				}

			}
		}],
		//分页
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : store,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		})
	});


	function queryPurInfo()
	{
			var record = grid.getSelectionModel().getSelected();
		if (record == null) {
			Ext.Msg.alert("提示", "请选择一条记录！");
		} else {
			
			window.showModalDialog('../purchasewarehouseQuery/queryPurInfo.jsp', record,
					'status:no;dialogWidth=780px;dialogHeight=450px');
		}
	}
//---------------------------------------

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
	//	layout : "fit",
		items : [grid]
	});

	
	
		  
	// 查询
	function queryRecord() {
		
		store.baseParams = {
			sdate : timefromDate.getValue(),
			edate:timetoDate.getValue(),
			purNo:txtPurNo.getValue(),
			buyer:txtBuyer.getValue(),
			invoiceNo:txtInvoiceNo.getValue()
		};
		store.load({
			params : {
				start : 0,
				limit : 18
			}
		});
	}
	
	queryRecord();
	
	
	
});