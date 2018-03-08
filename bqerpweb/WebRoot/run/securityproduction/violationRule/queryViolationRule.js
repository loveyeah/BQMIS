Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	function getMonth() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString() + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t;
		return s;
	}
	var quarterDate = new Ext.form.TextField({
				id : 'quarterDate',
				name : '_quarterDate',
				fieldLabel : "年份",
				style : 'cursor:pointer',
				cls : 'Wdate',
				width : 90,
				value : getMonth(),
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M',
									dateFmt : 'yyyy-MM',
									alwaysUseStartDate : true,
									isShowClear : false
								});
					}
				}
			});
			
	var txtInfo=new Ext.form.TextField({
	    name:'info',
	   width : 200,
	   readOnly:true
		
	});		

	

	// 定义grid
	var MyRecord =new Ext.data.Record.create([{
		name : 'ruleId',
		mapping : 0
	},{
		name : 'empCode',
		mapping :1
	},{
		name : 'empName',
		mapping :2
	},{
		name : 'deptCode',
		mapping :3
	},{
		name : 'deptName',
		mapping :4
	},{
		name : 'examineDate',
		mapping :5
	},{
		name : 'examineMoney',
		mapping :6
	},{
		name : 'phenomenon',
		mapping :7
	},{
		name : 'checkBy',
		mapping :8
	},{
		name : 'checkName',
		mapping :9
	},{
		name : 'entryDate',
		mapping :10
	},{
		name : 'totalNum',
		mapping :11
	},{
		name : 'totalMoney',
		mapping :12
	}])

	var dataProxy = new Ext.data.HttpProxy(

	{
				url : 'security/queryRuleList.action'
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
	store.on("load",function(){
	
	  if(store.getTotalCount()>0)
	  {
	  	 txtInfo.setValue("违章"+store.getAt(0).get("totalNum")+"次考核"+store.getAt(0).get("totalMoney")+"元");
	  }
	  else
	  {
	  	txtInfo.setValue("");
	  }
	  
		
	});

	var sm = new Ext.grid.CheckboxSelectionModel();

	var grid = new Ext.grid.GridPanel({
				region : "center",
				layout : 'fit',
				store : store,

				columns : [sm, new Ext.grid.RowNumberer({
									header : '序号',
									width : 50
								}), 
									{
		header : '姓名',
		dataIndex : 'empName'
	},{
		header : '部门',
		dataIndex : 'deptName'
	},{
		header : '考核时间',
		dataIndex : 'examineDate'
	},{
		header : '违章现象',
		dataIndex : 'phenomenon'
	},{
		header : '考核（元）',
		dataIndex : 'examineMoney'
	},{
		header : '查处人',
		dataIndex : 'checkName'
	}],
				sm : sm,
				autoSizeColumns : true,
				viewConfig : {
					forceFit : true
				},
				tbar : ['考核时间：', quarterDate, "-", 
						{
							text : "查询",
							iconCls : 'query',
							handler : queryRecord
						},'-',txtInfo],
				// 分页
				bbar : new Ext.PagingToolbar({
							pageSize : 18,
							store : store,
							displayInfo : true,
							displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
							emptyMsg : "没有记录"
						})
			});


	// ---------------------------------------

	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				// layout : "fit",
				items : [grid]
			});

	// -------------------

	// 查询
	function queryRecord() {

		store.baseParams = {
			strMonth : quarterDate.getValue()
			

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