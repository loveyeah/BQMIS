Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	var prjMeetSign = new PrjCheck.meetSign();
	//--------------------------------
	 function renderMoney(v) {
    	return renderNumber(v, 2);//修改计算金额现在2位小数
    }
    
    function renderNumber(v, argDecimal) {
		if (v) {
			if (typeof argDecimal != 'number') {
				argDecimal = 2;
			}
			v = Number(v).toFixed(argDecimal);
			var t = '';
			v = String(v);
			while ((t = v.replace(/^(-?[0-9]+)([0-9]{3}.*)$/, '$1,$2')) !== v)
				v = t;
			
			return v;
		} else
			return '';
	}
	//----------------------------------------
	
	
	
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
	}, {
		name : 'prjId',//申请时间
		mapping:15
	}, {
		name : 'prjName',//申请时间
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
  		flag : 'fillQuery'
  	};
  
	store.load({
		params : {
			start : 0,
			limit : 18
		}
	});
  };
  //query();
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
			id : 'update',
			text : "修改",
			iconCls : 'update',
			handler :function()
			{
				var record = grid.getSelectionModel().getSelected();
		if (record == null) {
			Ext.MessageBox.alert('提示信息', '请选择一行记录！')
			return;
		}
		tabs.setActiveTab(0);
		prjMeetSign.setFormRec(record);

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
					}),{
			header : "立项名称",
			width : 150,
			sortable : true,
			align : 'center',
			dataIndex : 'prjName'
		}, {
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
			align : 'center',
			renderer : function(value, cellmeta, record) {

				var budgetCost = record.data.budgetCost;

				return renderMoney(budgetCost);
			}
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
	
	grid.on('rowdblclick', function(grid, rowIndex, e) {
		var record = grid.getSelectionModel().getSelected();
		tabs.setActiveTab(0);
		prjMeetSign.setFormRec(record);
		
	});
  
	var tabs = new Ext.TabPanel({
				activeTab : 1,
				tabPosition : 'bottom',  
				items : [{
				        	layout : 'fit',
							id : 'detail',
							title : '工程项目会签单填写',
							items : [prjMeetSign.layout]
							
						},{
							id : 'list',
							title : '工程项目会签单列表',
							autoScroll : true,
							items : grid,
							layout : 'fit'
						}]
			});
 tabs.on('tabchange', function(tab, newtab) {
		if (newtab.getId() == 'list') {
			 query();
			prjMeetSign.resetFormRec();
		}
	})
	var view = new Ext.Viewport({
		         autoWidth : true,
				autoHeight : true,
				layout : 'fit',
				items : [tabs]
			});

});
