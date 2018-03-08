Ext.BLANK_IMAGE_URL = "comm/ext/resources/images/default/s.gif";
Ext.QuickTips.init();
Ext.onReady(function(){
	var msg = null;
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
			CurrentDate = CurrentDate + Month;
		} else {
			CurrentDate = CurrentDate + "0" + Month;
		}
		return CurrentDate;
	}
	var enddate = new Date();
	var startdate = enddate.add(Date.MONTH, 1);
	startdate = startdate.getFirstDateOfMonth();

	
	// 计划时间
	var planTime = new Ext.form.TextField({
				id : 'planTime',
				fieldLabel : '计划时间',
				style : 'cursor:pointer',
				value : ChangeDateToString(startdate),
				readOnly : true,
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M',
									dateFmt : 'yyyy-MM',
									alwaysUseStartDate : true,
									isShowClear : false
								});
						this.blur();
					}
				}
			});
	
	
	// 数据对象
	var record = new Ext.data.Record.create([{
		//0：主题ID
		name : 'topicId',
		mapping : 0
	},{//1：主题名称
		name : 'topicName',
		mapping : 1
	},{//2：主题显示顺序
		name : 'topicDisplay',
		mapping : 2
	},{//3：经济指标ID
		name : 'economicItemId',
		mapping : 3
	},{//4：指标名称
		name : 'itemName',
		mapping : 4
	},{//5：指标别名
		name : 'alias',
		mapping : 5
	},{//6：单位Id
		name : 'unitId',
		mapping : 6
	},{//7：单位名称
		name : 'unitName',
		mapping : 7
	},{//8：分类
		name : 'itemType',
		mapping : 8
	},{//9：指标显示顺序
		name : 'itemDisplay',
		mapping : 9
	},{//10：指标计划主ID
		name : 'plantMainId',
		mapping : 10
	},{//11：月份
		name : 'month',
		mapping : 11
	},{//12：计划工作流序号
		name : 'workflowNoPlan',
		mapping : 12
	},{//13：计划工作流状态
		name : 'workflowStatusPlan',
		mapping : 13
	},{//14：完成情况工作流序号
		name : 'workflowNoFact',
		mapping : 14
	},{//15：完成情况工作流状态
		name : 'workflowStatusFact',
		mapping : 15
	},{//16：指标计划明细ID
		name : 'plantDetailId',
		mapping : 16
	},{//17：#11#12计划值
		name : 'plantPlan1112',
		mapping : 17
	},{//18：#1#2计划值
		name : 'plantPlan12',
		mapping : 18
	},{//19：#11#12完成情况
		name : 'plantFact1112',
		mapping : 19
	},{//20：#1#2完成情况
		name : 'plantFact12',
		mapping : 20
	}])
	
	var store = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'manageitemplan/findItemByCondition.action'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalCount',
			root : 'list'
		},record)
	})
	
	store.on('load', function() {
		if (store.getTotalCount() > 0) {
			var workflowStatus = store.getAt(0).get("workflowStatusPlan");
			if (workflowStatus == 2 || workflowStatus == null) {
				Ext.get('btnSign').dom.disabled = true
			} else {
				Ext.get('btnSign').dom.disabled = false
			}
		}
	})
	
	// 各个按钮
	var queryBtu = new Ext.Button({
		id : 'queryBtu',
		text : '查询',
		iconCls : 'query',
		handler : queryFun
	})
	var reportBtu = new Ext.Button({
		id : 'btnSign',
		text : '签字',
		iconCls : 'write',
		handler : signFun
	})
	// 工具栏
	var tbar = new Ext.Toolbar({
		id : 'tbar',
		items : ['计划时间：',planTime,queryBtu,'-',reportBtu]
	})
	
	
	// 选择模式
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true
	})
	// 列模式
	var cm = new Ext.grid.ColumnModel([sm, new Ext.grid.RowNumberer({
		header : '行号',
		width : 35
	}),{
		header : '分类',
		dataIndex : 'itemType',
		renderer : function(value, matedata, record, rowIndex, colIndex, store) {
			if (record && rowIndex > 0) {
				if (store.getAt(rowIndex).get('itemType') == store
						.getAt(rowIndex - 1).get('itemType')
						|| store.getAt(rowIndex).get('itemType') == '')
					return '';
			} 
				return value;
		}
	},{
		header : '指标名称',
		dataIndex : 'alias'
	},{
		header : '计量单位',
		dataIndex : 'unitName'
	},{
		header : '#11,#12机组计划',
		dataIndex : 'plantPlan1112',
		align : 'right' ,
		width : 150
	},{
		header : '#1,#2机组计划',
		dataIndex : 'plantPlan12',
		align : 'right' ,
		width : 150
	}])
	
	var grid = new Ext.grid.EditorGridPanel({
		id : 'grid',
		frame : true,
		border : false,
		tbar : tbar,
		sm : sm,
		cm : cm,
		store : store,
		autoScroll : true,
		clicksToEdit : 1
	})
	
	new Ext.Viewport({
		frame : true,
		border : false,
		layout : 'border',
		items : [{
			region : 'center',
			layout : 'fit',
			items : [grid]
		}]
	})

	function queryFun()
	{
		store.baseParams = {
			topic : 1,
			month : planTime.getValue(),
			planStatus : "reported"
		}
		store.load();
	}
	store.on('load',function(){
		if(store.getTotalCount() > 0){
			Ext.Ajax.request({
				url : 'manageitemplan/judgeApprovePlant.action',
				method : 'post',
				params : {
					factMainId : store.getAt(0).get('plantMainId')
				},
				success : function(response,options){
					var result = Ext.util.JSON.decode(response.responseText);
					if(result){
						var str = result.toString().split(",");
						if(str.length > 1){
							if(str[0] == 0){
								msg = '您没有权限进行签字！';
							}else
							msg = null;
						}
					}
				}
			})
		}
	})
	function signFun(){
		if(msg != null && msg != ''){
			Ext.Msg.alert('提示',msg);
//			msg = null;
			return;
		}
		var url = "sign.jsp";
		if (store.getCount() > 0) {
			var workflowStatus = store.getAt(0).get("workflowStatusPlan");
			if(workflowStatus ==1)
			{
			var planmainid = store.getAt(0).get("plantMainId");
			var workFlowNo = store.getAt(0).get("workflowNoPlan");
			var args = new Object();
			args.entryId = workFlowNo;
			args.planmainid = planmainid;
			var obj = window.showModalDialog(url, args,
					'status:no;dialogWidth=770px;dialogHeight=550px');
			}
		}
		if (obj) {
			queryFun();
		}
	}
	
	queryFun()
})