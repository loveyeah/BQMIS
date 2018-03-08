Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	
	function getCurrentYear()
	{
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString();		
		return s;
	}
		// 年度
	var year = new Ext.form.TextField({
				// fieldLabel : '时间',
				readOnly : true,
				width : 80,
				id : 'year',
				style : 'cursor:pointer',
				value : getCurrentYear(),
				listeners : {
					focus : function() {
						WdatePicker({
									// 时间格式
									dateFmt : 'yyyy',
									alwaysUseStartDate : false
								});
						this.blur();
					}
				}
			});
	// 费用store
	var feeStore = new Ext.data.SimpleStore({
		fields : ['id','name'],
		data : [['1','长期借款'],['2','短期借款']]
	})
	var fee = new Ext.form.ComboBox({
			store : feeStore,
			displayField : 'name',
			valueField : 'id',
			mode : 'local',
			triggerAction : 'all',
			readOnly : true,
			allowBlank : false,
			width : 100,
			value : 1
	})
	
	

	var sm = new Ext.grid.CheckboxSelectionModel();
	// 创建记录类型
	var Myrecord =Ext.data.Record.create([{
		name : 'financeId'
	},{
		name : 'budgetTime'
	}, {
		name : 'financeType'
	}, {
		name : 'workFlowNo'
	}, {
		name : 'workFlowStatus'
	}, {
		name : 'financeDetailId'
	},{
		name : 'loanName'
	},{
		name : 'lastLoan'
	},{
		name : 'newLoan'
	},{
		name : 'repayment'
	},{
		name : 'balance'
	},{
		name : 'interest'
	},{
		name : 'memo'
	},{
		name : 'isUse'
	}
	]);
	// 配置数据集
	var store = new Ext.data.Store({
		/* 创建代理对象 */
		proxy : new Ext.data.HttpProxy({
			url : 'managebudget/financeApproveQuery.action'
		}),
		/* 创建解析Json格式数据的解析器 */
		reader : new Ext.data.JsonReader({
			root : "list",
			totalProperty : "totalCount"
		}, Myrecord)
	});
    
	function queryRecord() {
		
	if(year.getValue() == null || year.getValue() == "")
		{
			Ext.Msg.alert('提示信息','年度不可为空，请先选择！');
			return;
		}
		store.baseParams = {
			budgetTime : year.getValue(),
			financeType : fee.getValue()
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
	var sm_store_item =new Ext.grid.ColumnModel([sm,
		new Ext.grid.RowNumberer({
			header : "行号",
			width : 31
			}),
	{
		header : '借款名称',
		dataIndex : 'loanName',
		width : 110
	}, {
		header : '去年年末余额',
		dataIndex : 'lastLoan',
		align : 'right',
		width : 90
	}, {
		header : '本年新增借款',
		dataIndex : 'newLoan',
		align : 'right',
		width : 90
	}, {
		header : '本年到期还款',
		dataIndex : 'repayment',
		align : 'right',
		width : 90
	}, {
		header : "本年年末余款",
		width : 90,
		dataIndex : 'balance',
		align : 'right'
	}, {
		header : '本年支付利息',
		dataIndex : 'interest',
		align : 'right',
		width : 90
	},{
			header : '备注',
			width : 130,
			align : 'left',
			sortable : true,
			dataIndex : 'memo'
		}
	]);
		

	// 底部分页栏
	var bbar = new Ext.PagingToolbar({
		pageSize : 18,
		store : store,
		displayInfo : true,
		displayMsg : "显示第{0}条到{1}条，共{2}条",
		emptyMsg : "没有记录"
	});
	// 顶部工具栏
	var tbar = new Ext.Toolbar({
		items : ['年度:',year,'-','费用:',fee,'-',query,
		{
			text:'审批',
			iconCls : 'view',
			handler:report
		}
		]
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
	

//	Grid.on("rowdblclick", report);
	function report()
	{
		//----------------
		if(store.getCount()==0)
		{
			Ext.Msg.alert("提示","没有记录审批！");
			return;
		}
		var record = store.getAt(0);
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
				
					args.busiNo = record.get('financeId');
					args.entryId = record.get("workFlowNo"); 
					var obj = window.showModalDialog(url, args,
					'status:no;dialogWidth=750px;dialogHeight=550px');
					if(obj)
					{
					 queryRecord();
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
