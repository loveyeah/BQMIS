Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	var conName = new Ext.form.TextField({
			id : "conName",
			xtype : "textfield",
			fieldLabel : '合同名称',
			anchor : '80%',
			allowBlank : true
		});
	var contractorName = new Ext.form.TextField({
			id : "contractorName",
			xtype : "textfield",
			fieldLabel : '乙方单位',
			anchor : '80%',
			allowBlank : true
		});
	var MyRecord = Ext.data.Record.create([{
		name : 'checkSignId',
		mapping:0
		
	}, {
		name : 'conId',
		mapping:1
	}, {
		name : 'conName',//合同名称
		mapping:2
	}, {
		name : 'reportCode',//编号
		mapping:3
	}, {
		name : 'budgetCost',//预算费用
		mapping:4
	}, {
		name : 'contractorId',
		mapping:5
	}, {
		name : 'contractorName',//乙方单位名称
		mapping:6
	}, {
		name : 'owner',//甲方单位
		mapping:7
	}, {
		name : 'startDate',//开工时间
		mapping:8
	}, {
		name : 'endDate',//竣工时间
		mapping:9
	},{
		name : 'changeInfo',//工程变更简述
		mapping:10
	}, {
		name : 'devolveOnInfo',//工程资料移交情况
		mapping:11
	}, {
		name : 'memo',//备注
		mapping:12
	}, {
		name : 'applyBy',//申请人
		mapping:13
	}, {
		name : 'applyDate',//申请时间
		mapping:14
	},//add by ypan 20100913
	{
		name : 'prjName',//立项名称
		mapping:16
	}]);
	var dataProxy = new Ext.data.HttpProxy({
		url : 'manageproject/getPrjCheckMeetSign.action'
	});
  
	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "total"
	}, MyRecord);

	var store = new Ext.data.Store({
		proxy : dataProxy,
		reader : theReader
	});
  function  query()
  {
  	store.baseParams={
  		conName:conName.getValue(),
  		contractorName:contractorName.getValue(),
  		flag : 'query'
  	};
  
	store.load({
		params : {
			start : 0,
			limit : 18
		}
	});
  };
    query();
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false
	});
	
  var tbar = new Ext.Toolbar({
		id : 'tbar',
		items : ['合同名称:',conName,'-','乙方单位:',contractorName,'-',{
			id : 'query',
			text : "查询",
			iconCls : 'query',
			handler : function() {
				query();
		
			}
		}, '-', {
				id : 'btnMeetQuery',
					text : "会签查询",
					iconCls : 'view',
					handler : function() {
						var selrows = grid.getSelectionModel().getSelections();
						if (selrows.length ==1) {
							var signId = selrows[0].data.checkSignId;
							
						window.open("/powerrpt/report/webfile/hr/prjCheckMeetSign.jsp?checkSingId="
											+ signId+"");
						
							
						} else {
							Ext.Msg.alert('提示', '请从列表中选择一条记录！');
						}
					}
			
		}]
	});
	
	var grid = new Ext.grid.GridPanel({
		region : "center",
		store : store,
		columns : [// 设计页面显示的表格信息，字段与Ext.data.Record.create匹配
		sm,new Ext.grid.RowNumberer({
						header : '行号',
						width : 50,
						align : 'center'
					}), //add by ypan 20100913
			{
			header : "立项名称",
			width : 150,
			sortable : true,
			dataIndex : 'prjName',
			align : 'center'
		},{
			header : "合同名称",
			width : 150,
			sortable : true,
			align : 'center',
			dataIndex : 'conName'
		}, {
			header : "乙方单位",
			width : 150,
			sortable : true,
			dataIndex : 'contractorName',
			align : 'center'
		}, {
			header : "预算费用",
			sortable : true,
			dataIndex : 'budgetCost',
			hidden : false,
			align:'center'
		}, {
			header : "开工时间",
			width : 100,
			sortable : true,
			dataIndex : 'startDate',
			align : 'center'
		}, {
			header : "竣工时间",
			width : 100,
			sortable : true,
			dataIndex : 'endDate',
			align : 'center'
		}, {
			header : "工程变更简述",
			width : 150,
			sortable : true,
			dataIndex : 'changeInfo',
			align : 'center'
		}],
		sm : sm,
		tbar:tbar,
		// 分页
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : store,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录",
			beforePageText : '页',
			afterPageText : "共{0}"
		})
	});
	
	
 
	var view = new Ext.Viewport({
		         autoWidth : true,
				autoHeight : true,
				layout : 'fit',
				items : [grid]
			});

});
