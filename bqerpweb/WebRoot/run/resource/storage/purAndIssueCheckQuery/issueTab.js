Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	
	// 定义grid
var MyRecord = Ext.data.Record.create([
	{name : 'issueHeadId',mapping : 0},
    {name : 'issueNo',mapping : 1},
	{name : 'getRealPerson',mapping : 2},
	{name : 'receiptByName',mapping :3},
	{name : 'receiptDept',mapping :4},
	{name : 'totalPrice',mapping :5},
	{name : 'memo',mapping :9},
	{name : 'lastModifyDate',mapping :15}
	
	]);

	var dataProxy = new Ext.data.HttpProxy(

			{
				url:'resource/queryCheckedIssueList.action'
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
//			issueNo:'合计',
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
 
	var txtIssueNo = new Ext.form.TextField({
		fieldLabel : "领料单号",
		id : "txtIssueNo",
		name : "txtIssueNo",
		width:140
	});
	var txtReceiptBy=new Ext.form.TextField({
		fieldLabel : "申请领料人",
		id : "txtReceiptBy",
		name : "txtReceiptBy",
		width:140
	});
	
	var txtReceiptDept=new Ext.form.TextField({
		fieldLabel : "申请领用部门",
		id : "txtReceiptDept",
		name : "txtReceiptDept",
		width:170
	});
	
		// 是否红单
	var typeStore = new Ext.data.SimpleStore({
		root : 'list',
		data : [['','所有'],['4','生产类'],['5','行政办公类'],['1','固定资产类'],['2','专项物资类'],['12','计算机相关材料']],
		fields : ['name' ,'key']
	})
	
	//类型
	var cbBillType = new Ext.form.ComboBox({
				id : 'redbill',
				name : 'redbillStore',
				fieldLabel : "单据种类",
				mode : 'local',
				typeAhead : true,
				valueField : "name",
				displayField : "key",
				store : typeStore,
				forceSelection : true,
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : false,
				value:'',
		        width:145
	});
	
		// 查询详细信息按钮
	var btnReturn = new Ext.Toolbar.Button({
		text : "查询详细",
		iconCls :'query',
		handler : function() {
			window.location.replace("../sendMaterialsAccount/sendMaterialsAccount.jsp");
		}
	});
	
	// add by ywliu 20100202 start
	var btnQueryDetail = new Ext.Toolbar.Button({
		text : "审核物资明细查询",
		iconCls :'query',
		handler : function() {
			var record = grid.getSelectionModel().getSelected();
			if (record == null) {
				Ext.Msg.alert("提示", "请选择一条记录！");
			} else {
				var arg = new Object();
				arg.no = record.data.issueNo;
				arg.sdate = timefromDate.getValue();
				arg.edate = timetoDate.getValue();
				arg.issueNo = txtIssueNo.getValue();
				arg.receiptBy = txtReceiptBy.getValue();
				arg.receiptDept = txtReceiptDept.getValue();
				arg.billType = cbBillType.getValue();
				window.showModalDialog('queryIssueDetailInfo.jsp', arg,
						'status:no;dialogWidth=780px;dialogHeight=450px');
			}
		}
	});
	
	// end
	//------------------------------------------------------------
	var sm = new Ext.grid.CheckboxSelectionModel();

	var grid = new Ext.grid.GridPanel({
		region : "center",
		layout : 'fit',
		store : store,
        renderTo:"mygrid",
		columns : [
		sm, new Ext.grid.RowNumberer({header:'序号',width : 50}),{
			
			header : "ID",
			width : 75,
			sortable : true,
			dataIndex : 'issueHeadId',
			hidden:true
		},
		{
			header : "日期",
			width : 75,
			sortable : true,
			hidden:true,
			dataIndex : 'lastModifyDate'
		},{
			header : "领料单号",
			width : 75,
			sortable : true,
			dataIndex : 'issueNo'
		},
		{
			header : "实际领料人",
			width : 75,
			sortable : true,
			dataIndex : 'getRealPerson'
		},
		{
			header : "申请领料人",
			width : 75,
			sortable : true,
			dataIndex : 'receiptByName'
		},
		{
			header : "申请领用部门",
			width : 75,
			sortable : true,
			dataIndex : 'receiptDept'
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
		tbar : ['审核时间：',timefromDate,'~', timetoDate,'-','领料单号:',txtIssueNo,'-',
			    '申请领用人:',txtReceiptBy],
		//分页
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : store,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		})
	});


	
	var toolBarTop = new Ext.Toolbar({
		renderTo : grid.tbar,
		items : ['申请领用部门:',txtReceiptDept,'-','单据种类:',cbBillType,'-',
				{
			text : "查询",
			iconCls : 'query',
			handler : queryRecord
		},'-',{
			     text:'查看领料单信息',
			     iconCls : 'query',
			     handler:queryPurInfo
			  },btnReturn,'-',btnQueryDetail]
	});	
	
		function queryPurInfo()
	{
			var record = grid.getSelectionModel().getSelected();
		if (record == null) {
			Ext.Msg.alert("提示", "请选择一条记录！");
		} else {
			var obj=new Object();
			obj.record=record;
			obj.startDate=timefromDate.getValue();
			obj.endDate=timetoDate.getValue();
			window.showModalDialog('queryIssueInfo.jsp', obj,
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
			issueNo:txtIssueNo.getValue(),
			receiptBy:txtReceiptBy.getValue(),
			receiptDept:txtReceiptDept.getValue(),
			billType:cbBillType.getValue()
		};
		store.load({
			params : {
				start : 0,
				limit : 18
			}
		});
	}
	
	queryRecord();
	
	btnReturn.setVisible(false);
	
	
});