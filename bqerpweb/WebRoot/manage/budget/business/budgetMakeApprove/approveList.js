Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	
	// 定义部门
	var deptData = new Ext.data.JsonStore({
		root : 'list',
		url : "managebudget/findBudgetDeptList.action",
		fields : ['centerId', 'depName']
	});
	deptData.on('load', function(e, records) {
		var record1=new Ext.data.Record({
    	depName:'所有',
    	centerId:''}
    	);
    	deptData.insert(0,record1);
    	deptComboBox.setValue('');
	});
	deptData.load();
	
		var deptComboBox = new Ext.form.ComboBox({
		id : "deptComboBox",
		store : deptData,
		displayField : "depName",
		valueField : "centerId",
		mode : 'local',
		width : 134,
		triggerAction : 'all',
		readOnly : true
	});
	
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
		// 预算编制单ID
		name : 'budgetMakeId'
	}, {
		// 预算部门
		name : 'centerId'
	}, {
		// 预算主题
		name : 'topicId'
	}, {
		// 预算时间
		name : 'budgetTime'
	}, {
		// 编制人
		name : 'makeBy'
	}, {
		// 编制日期
		name : 'makeDate'
	}, {
		// 工作流序号
		name : 'workFlowNo'
	}, {
		// 编制状态
		name : 'makeStatus'
	}, {
		// 主题名称
		name : 'topicName'
	}, {
		// 部门名称
		name : 'centerName'
	}, {
		// 编制人名称
		name : 'makeByName'
	}]);
	// 配置数据集
	var store = new Ext.data.Store({
		/* 创建代理对象 */
		proxy : new Ext.data.HttpProxy({
			url : 'managebudget/findMakeApproveList.action'
		}),
		/* 创建解析Json格式数据的解析器 */
		reader : new Ext.data.JsonReader({
			root : "list",
			totalProperty : "totalCount"
		}, Myrecord)
	});
    
	function queryRecord() {
		
		store.baseParams = {
			topicId : topicComboBox.getValue(),
			centerId : deptComboBox.getValue()
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
	
	// 定义grid
	// 事件状态
	var sm_store_item = new Ext.grid.ColumnModel([sm,  {
				header : '预算主题',
				align : 'center',
				width : 110,
				sortable : true,
				dataIndex : 'topicName'
			}, {
				header : '预算部门',
				align : 'center',
				width : 110,
				sortable : true,
				dataIndex : 'centerName'
			},  {
				header : '预算时间',
				align : 'center',
				width : 110,
				sortable : true,
				dataIndex : 'budgetTime'
			}, {
				header : '编制人',
				align : 'center',
				width : 110,
				sortable : true,
				dataIndex : 'makeByName'
			}, {
				header : '编制日期',
				align : 'center',
				width : 110,
				sortable : true,
				dataIndex : 'makeDate'
			}, {
				header : '编制状态',
				align : 'center',
				width : 110,
				sortable : true,
				hidden:true,
				dataIndex : 'makeStatus',
				renderer:function(value)
				{
					if(value=="0") return "未上报";
					else if(value=="1") return "编制审批中";
					else if(value=="2") return "编制审批通过";
					else if(value=="3") return "编制审批退回";
					else return value;
				}
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
		items : ['预算主题：',topicComboBox,'-','预算部门：',deptComboBox,'-',query]
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
					url="makeApproveSign.jsp";
				} 
				else
				{
					 url = "../../../../../" + obj[0].url; 
				} 
					args.busiNo = record.get('budgetMakeId');
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
