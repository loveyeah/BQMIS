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
	var sdate = ChangeDateToString(startdate)+" 00:00:00";
	var edate = ChangeDateToString(enddate)+" 00:00:00";
	
	// 定义grid
var MyRecord = Ext.data.Record.create([
	{name : 'conModifyId'},
	{name:'conId'},
    {name : 'fileStatus'},
	{name : 'conModifyNo'},
	{name : 'contractName'},
	{name : 'clientName'},
	{name : 'conomodifyType'},
	{name : 'operateName'},
	{name : 'operateDeptName'},
	{name : 'signStartDate'},
	{name : 'signEndDate'},
	{name:'currencyType'},
	{name:'modiyActAmount'},
	{name:'workFlowNo'}
	]);

	var dataProxy = new Ext.data.HttpProxy(

			{
				url:'managecontract/findConModifyList.action'
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
//分页

	
		var fuzzy = new Ext.form.TextField({
		id : "fuzzy",
		name : "fuzzy"
	});
//		var startDate = new Ext.form.DateField({
//		id : "startDate",
//		format : 'Y-m-d h:i:s',
//		fieldLabel : '',
//		width : 150,
//		value:sdate,
//		name : 'startDate'
//
//	});
	    var startDate = new Ext.form.TextField({
        id : 'startDate',
        fieldLabel : "",
        name : 'startDate',
        style : 'cursor:pointer',
        width : 150,
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
        width : 150,
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
//	var endDate = new Ext.form.DateField({
//		id : "endDate",
//		format : 'Y-m-d h:i:s',
//		fieldLabel : '',
//		width : 150,
//		value:edate,
//		name : 'endDate'
//
//	});
		

	var sm = new Ext.grid.CheckboxSelectionModel({singleSelect : true});

	var grid = new Ext.grid.GridPanel({
			region : "center",
		store : store,
      //  width : Ext.get('div_lay').getWidth(),
		columns : [
		sm, new Ext.grid.RowNumberer({
		header : '项次号',
		width : 50,
		align : 'center'
	}),{
			
			header : "ID",
			width : 100,
			sortable : true,
			dataIndex : 'conModifyId',
			hidden:true
		},{
			
			header : "合同ID",
			width : 100,
			sortable : true,
			dataIndex : 'conId',
			hidden:true
		},{
			
			header : "归档状态",
			width : 100,
			sortable : true,
			align : 'center',
			dataIndex : 'fileStatus',
				renderer : function changeIt(val) {
					if(val=="PRE") return "预归档";
					else if(val=="OK") return "已归档";
					else if(val=="BAK") return "被退回";
					else if(val=="DRF") return "未归档";
					else return "未归档"; 
				
			}
		},

		{
			header : "合同变更编号",
			width : 120,
			sortable : true,
			dataIndex : 'conModifyNo',
			align:'center'
		},

		{
			header : "合同名称",
			width : 100,
			sortable : true,
			dataIndex : 'contractName',
			align:'center'
		},

		{
			header : "供应商",
			width : 100,
			sortable : true,
			dataIndex : 'clientName',
			align:'center'
		},
		{
			header : "变更类型",
			width : 100,
			sortable : true,
			dataIndex : 'conomodifyType',
			align:'center',
				renderer : function changeIt(val) {
					if(val=="1") return "合同变更";
				    if(val=="2") return "合同解除";
				
			}
		},
		{
			header : "经办人",
			width : 100,
			sortable : true,
			dataIndex : 'operateName',
			align:'center'
		},
		{
			header : "申请部门",
			width : 100,
			sortable : true,
			dataIndex : 'operateDeptName',
			align:'center'
		},
		{
			header : "会签开始时间",
			width : 100,
			sortable : true,
			dataIndex : 'signStartDate',
			align:'center'
		},
		{
			header : "会签结束时间",
			width : 100,
			sortable : true,
			dataIndex : 'signEndDate',
			align:'center'
		},
		{
			header : "币别",
			width : 100,
			sortable : true,
			dataIndex : 'currencyType',
			hidden:true,
			align:'center'
		},
		{
			header : "总金额",
			width : 100,
			sortable : true,
			dataIndex : 'modiyActAmount',
			hidden:true,
			align:'center'
		}
		],
		sm : sm,
		tbar : ['会签时间：  从',startDate,'至',endDate,'  ',fuzzy,
		{
			text : "查询(合同变更编号、合同名称、供应商)",
            iconCls : 'query',
            handler:queryRecord
		},'-', {
			text : "浏览合同变更信息",
			iconCls : 'list',
			handler:updateRecord
	
		},'-', {
			text : "会签审批查询",
			iconCls : 'view',
			handler : showWorkFlow
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


	function showWorkFlow()
	{
		var selrows = grid.getSelectionModel().getSelections();
		if (selrows.length > 0) {
			var entryId = selrows[0].data.workFlowNo;
			if (entryId == null || entryId == "") {
				Ext.Msg.alert('提示', '流程尚未启动！');
			} else {
				var url = "/power/workflow/manager/show/show.jsp?entryId="
						+ entryId;
				window.open(url);
			}
		} else {
			Ext.Msg.alert('提示', '请从列表中选择一条记录！');
		}
	}
	grid.on('rowdblclick',updateRecord);
	function updateRecord()
	{
		
		if (grid.selModel.hasSelection()) {
		
			var records = grid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项记录！");
			} else {
				var record = grid.getSelectionModel().getSelected();
	            parent.Ext.getCmp("maintab").setActiveTab(2);
		        parent.Ext.getCmp("maintab").setActiveTab(1);
		        var url="manage/contract/business/modifyArchive/tabArchive/archiveBase.jsp";
	        	parent.document.all.iframe2.src = url+"?id="+record.get("conModifyId")+"&conId="+record.get("conId");
		         url="manage/contract/business/modifyArchive/tabPayPlan/payPlan.jsp";
		        
		       
		         var conId=record.get("conId");
		         parent.document.all.iframe3.src = url+"?id="+conId;
			}
		} else {
			Ext.Msg.alert("提示", "请先选择记录!");
		}
	}
     function queryRecord() {
	
		store.load({
			params : {
				start : 0,
				limit : 18,
				fuzzy : fuzzy.getValue(),
			startDate:Ext.get('startDate').dom.value,
			endDate:Ext.get('endDate').dom.value,
			conTypeId : 1
			}
		});
	}
//---------------------------------------

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		items : [grid]
	});
	
	queryRecord();

});