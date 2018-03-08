Ext.BLANK_IMAGE_URL = "comm/ext/resources/images/default/s.gif";
Ext.QuickTips.init();
Ext.onReady(function() {
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

	// 各个按钮
	var queryBtu = new Ext.Button({
		id : 'queryBtu',
		text : '查询',
		iconCls : 'query',
		handler : queryFun
	})

	var signBtu = new Ext.Button({
		id : 'btnSign',
		text : '签字',
		iconCls : 'write',
		handler : signFun
	})
	// 工具栏
	var tbar = new Ext.Toolbar({
		id : 'tbar',
		items : ['计划时间：', planTime, queryBtu, '-', signBtu]
	})
	// 数据对象
	var record = new Ext.data.Record.create([{
		// 0：主题ID
		name : 'topicId',
		mapping : 0
	}, {	// 1：主题名称
		name : 'topicName',
		mapping : 1
	}, {	// 2：主题显示顺序
		name : 'topicDisplay',
		mapping : 2
	}, {	// 3：经济指标ID
		name : 'economicItemId',
		mapping : 3
	}, {	// 4：指标名称
		name : 'itemName',
		mapping : 4
	}, {	// 5：指标别名
		name : 'alias',
		mapping : 5
	}, {	// 6：单位Id
		name : 'unitId',
		mapping : 6
	}, {	// 7：单位名称
		name : 'unitName',
		mapping : 7
	}, {	// 8：分类
		name : 'itemType',
		mapping : 8
	}, {	// 9：指标显示顺序
		name : 'itemDisplay',
		mapping : 9
	}, {	// 10：指标计划主ID
		name : 'depMainId',
		mapping : 10
	}, {	// 11：月份
		name : 'month',
		mapping : 11
	}, {	// 12：计划工作流序号
		name : 'workflowNoPlan',
		mapping : 12
	}, {	// 13：计划工作流状态
		name : 'workflowStatusPlan',
		mapping : 13
	}, {	// 14：完成情况工作流序号
		name : 'workflowNoFact',
		mapping : 14
	}, {	// 15：完成情况工作流状态
		name : 'workflowStatusFact',
		mapping : 15
	}, {	// 16：指标计划明细ID
		name : 'depDetailId',
		mapping : 16
	}, {	// 17：#11#12计划值
		name : 'depPlan1112',
		mapping : 17
	}, {	// 18：#1#2计划值
		name : 'depPlan12',
		mapping : 18
	}, {	// 19：#11#12完成情况
		name : 'depFact1112',
		mapping : 19
	}, {	// 20：#1#2完成情况
		name : 'depFact12',
		mapping : 20
	}])

	var store = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'manageitemplan/findItemByCondition.action'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalCount',
			root : 'list'
		}, record)
	})

	store.on('load', function() {
		if (store.getTotalCount() > 0) {
			var oneRec = store.getAt(0);
			if (oneRec.get("workflowStatusFact") == 2
					|| oneRec.get("workflowStatusFact") == null) {
				Ext.get('btnSign').dom.disabled = true
			} else {
				Ext.get('btnSign').dom.disabled = false
			}
		}
	})

	// 选择模式
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true
	})
	// 列模式
	var cm = new Ext.grid.ColumnModel({
		columns : [sm, new Ext.grid.RowNumberer({
			header : '行号',
			width : 35
		}), {
			header : '分类',
			dataIndex : 'itemType',
			renderer : function(value, matedata, record, rowIndex, colIndex,
					store) {
				if (record && rowIndex > 0) {
					if (store.getAt(rowIndex).get('itemType') == store
							.getAt(rowIndex - 1).get('itemType')
							|| store.getAt(rowIndex).get('itemType') == '')
						return '';
				}
				return value;
			}
		}, {
			header : '指标名称',
			dataIndex : 'alias'
		}, {
			header : '计量单位',
			dataIndex : 'unitName',
			align : 'center'
		}, {
			header : '计划',
			dataIndex : 'depPlan1112',
			width : 150,
			align : 'center'
		}, {
			header : '完成情况',
			dataIndex : 'depFact1112',
			width : 150,
			align : 'center'
		}, {
			header : '计划',
			dataIndex : 'depPlan12',
			width : 150,
			align : 'center'
		}, {
			header : '完成情况',
			dataIndex : 'depFact12',
			width : 150,
			align : 'center'
		}],
		defaultSortable : true,
		rows : [[{
			rowspan : 2,
			colspan : 5
		}, {
			header : '#11,#12机组指标值',
			colspan : 2,
			align : 'center'
		}, {
			header : '#1,#2机组指标值',
			rowspan : 1,
			colspan : 2,
			align : 'center'
		}]]
	})

	var grid = new Ext.grid.EditorGridPanel({
		id : 'grid',
		frame : true,
		border : false,
		tbar : tbar,
		sm : sm,
		cm : cm,
		store : store,
		autoScroll : true,
		clicksToEdit : 1,
		enableColumnMove : false,
		plugins : [new Ext.ux.plugins.GroupHeaderGrid()]
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
	function queryFun() {
		store.baseParams = {
			topic : 2,
			month : planTime.getValue(),
			realStatus : "reported"
		}
		store.load();
	}

	store.on('load',function(){
		if(store.getTotalCount() > 0){
			Ext.Ajax.request({
				url : 'manageitemplan/judgeApproveDept.action',
				method : 'post',
				params : {
					planMainId : store.getAt(0).get('depMainId')
				},
				success : function(response,options){
					var result = Ext.util.JSON.decode(response.responseText);
					if(result){
						var str = result.toString().split(",");
						if(str.length > 1){
							if(str[1] == 0){
								msg = '您没有权限进行签字！';
							}else
							msg = null;
						}
					}
				}
			})
		}
	})
	function signFun() {
		if(msg != null && msg != ''){
			Ext.Msg.alert('提示',msg);
//			msg = null;
			return;
		}
		var url = "sign.jsp";
		if (store.getCount() > 0) {
			var workflowStatus = store.getAt(0).get("workflowStatusFact");
			if (workflowStatus == 1) {
				var depmainid = store.getAt(0).get("depMainId");
				var workFlowNo = store.getAt(0).get("workflowNoFact");
				var args = new Object();
				args.entryId = workFlowNo;
				args.depmainid = depmainid;
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