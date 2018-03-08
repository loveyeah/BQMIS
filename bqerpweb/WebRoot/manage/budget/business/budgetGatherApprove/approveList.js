Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	
	
	// 定义主题
	var topicData = new Ext.data.JsonStore({
		root : 'list',
		url : "managebudget/getThemeList.action",
		fields : ['topicId', 'topicName']
	});
	topicData.on('load', function(e, records) {
		var record1=new Ext.data.Record({
    	topicName:'所有',
    	topicId:''}
    	);
    	topicData.insert(0,record1);
    	topicComboBox.setValue('');
	});
	topicData.load();
	
		var topicComboBox = new Ext.form.ComboBox({
		id : "topicComboBox",
		store : topicData,
		displayField : "topicName",
		valueField : "topicId",
		mode : 'local',
		width : 134,
		triggerAction : 'all',
		readOnly : true
	});
	
	

	var sm = new Ext.grid.CheckboxSelectionModel();
	// 创建记录类型
	var Myrecord = Ext.data.Record.create([
		 {
		name : 'budgetGatherId'
	}, {
		name : 'topicId'
	}, {
		name : 'topicName'
	}, {
		
		name : 'budgetTime'
	}, {
		
		name : 'gatherByName'
	}, {
		
		name : 'strGatherDate'
	}, {
	
		name : 'workFlowNo'
	}, {
		
		name : 'gatherStatus'
	}]);
	// 配置数据集
	var store = new Ext.data.Store({
		/* 创建代理对象 */
		proxy : new Ext.data.HttpProxy({
			url : 'managebudget/budgetGatherApproveQuery.action'
		}),
		/* 创建解析Json格式数据的解析器 */
		reader : new Ext.data.JsonReader({
			root : "list",
			totalProperty : "totalCount"
		}, Myrecord)
	});
    
	function queryRecord() {
		
		store.baseParams = {
			topicId : topicComboBox.getValue()
		};
		store.load({
			params : {
				start : 0,
				limit : 18
			}
		});
	}
	
	
	// 查询
	var query = new Ext.Button({
		id : "btnQuery",
		text : "查询",
		iconCls : "query",
		handler :queryRecord
	})
	
	// 汇总明细
	var detail = new Ext.Button({
		id : "detQuery",
		text : "汇总明细",
		iconCls : "query",
		handler :queryDetail
	})
	
	function queryDetail()
	{
		var selected = Grid.getSelectionModel().getSelections();
		if (selected.length == 0) {
			Ext.Msg.alert("提示信息", "请选择要查看明细的记录！");
			return;
		} else {
			if (selected.length != 1) {
				Ext.Msg.alert('提示信息', '请选择其中一项！')
				return;
			}
		}
		var record = Grid.getSelectionModel().getSelected();
		var gather = record.get('budgetGatherId');
		var args = new Object();
		args.gatherId = gather;
		args.topicId = record.get('topicId');
		args.budgetTime = record.get('budgetTime');
		var obj = window.showModalDialog('../../query/budgetGatherQuery/budgetGatherQuery.jsp', args,
					'status:no;dialogWidth=750px;dialogHeight=550px');
	}
	// 定义grid
	// 事件状态
	var sm_store_item = new Ext.grid.ColumnModel([sm,  {
				header : '预算主题',
				align : 'center',
				width : 110,
				sortable : true,
				dataIndex : 'topicName'
			}, {
				header : '预算时间',
				align : 'center',
				width : 110,
				sortable : true,
				dataIndex : 'budgetTime'
			}, {
				header : '汇总人',
				align : 'center',
				width : 110,
				sortable : true,
				dataIndex : 'gatherByName'
			}, {
				header : '汇总日期',
				align : 'center',
				width : 110,
				sortable : true,
				dataIndex : 'strGatherDate'
			}]);

	// 底部分页栏
	var bbar = new Ext.PagingToolbar({
		pageSize : 18,
		store : store,
		displayInfo : true,
		displayMsg : "{0} 到 {1} /共 {2} 条",
		emptyMsg : "没有记录",
		beforePageText : '',
		afterPageText : ""
	});
	// 顶部工具栏
	var tbar = new Ext.Toolbar({
		items : ['预算主题：',topicComboBox,'-',query,'-',detail]
	});
	// 可编辑的表格
	var Grid = new Ext.grid.GridPanel({
		sm : sm,
		ds : store,
		cm : sm_store_item,
		autoScroll : true,
		bbar : bbar,
		tbar : tbar,
		border : true,
		clicksToEdit : 1,
		viewConfig : {
		 forceFit : true
		}
	});
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		border : false,
		frame : false,
		items : [{
			layout : 'fit',
			border : false,
			frame : false,
			region : "center",
			items : [Grid]
		}]
	});
	
	
	Grid.on("rowdblclick", report);
	function report()
	{
		//----------------
		var record = Grid.getSelectionModel().getSelected();
		var workFlowNo = record.get('workFlowNo');
		Ext.Ajax.request({
			url : 'MAINTWorkflow.do?action=getCurrentStepsInfo',
			method : 'POST',
			params : {
				entryId : workFlowNo
			},
			success : function(result, request) {
				var url="";
				var obj = eval("(" + result.responseText + ")");
				var args = new Object();
				if (obj[0].url==null||obj[0].url=="") {
					url="approveSign.jsp";
				} 
				else
				{
					 url = "../../../../../" + obj[0].url; 
				} 
					args.busiNo = record.get('budgetGatherId');
					args.entryId = record.get("workFlowNo"); 
					var obj = window.showModalDialog(url, args,
					'status:no;dialogWidth=750px;dialogHeight=550px');
					if(obj)
					{
					  store.load();
					}
				
			},
			failure : function(result, request) {
				Ext.Msg.alert("提示","错误");
			}

		});
		//-----------------
		
		
	}
	
	queryRecord();
	
})
