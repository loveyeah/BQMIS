Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	var args = window.dialogArguments;
	var gatherId = args.gatherId;
	var topicId = args.topicId;
	var budgetTime = args.budgetTime;
	var grid;
	var ds ;
	var canSave = true;
	var isSaved = true;

	
	// 创建一个对象
	var obj = new Ext.data.Record.create([{
		// 预算汇总ID
		name : 'budgetGatherId'
	},{
		// 预算主题
		name : 'topicId'
	},{
		// 预算时间
		name : 'budgetTime'
	},{
		// 汇总人
		name : 'gatherBy'
	},{
		// 工作流序号
		name : 'gatherWorkFlowNo'
	},{
		// 汇总状态
		name : 'gatherStatus'
	},{
		// 部门编码
		name : 'centerId'
	},{
		// 指标编码
		name : 'itemId'
	},{
		// 建议预算
		name : 'adviceBudget'
	},{
		// 数据来源
		name : 'dataSource'
	}])
	
	// 系统当前时间
	function getMonth() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString() + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t ;
		return s;
	}
	function getDate(){
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString()
		return s;
	}
	function query(){
//		Ext.Msg.wait("正在查询数据!请等待...");
		Ext.Ajax.request({
			url : 'managebudget/findBudgetGatherList.action',
			params : {
				topicId : topicId,
				budgetTime : budgetTime,
				gatherId : gatherId
			},
			method : 'post',
			waitMsg : '正在加载数据...',
			success : function(result, request) {
				var json = eval('(' + result.responseText + ')');
				document.getElementById("gridDiv").innerHTML = "";
				ds = new Ext.data.JsonStore({
					data : json.data,
					fields : Ext.decode(json.fieldsNames)
				});
				var wd = Ext.get('gridDiv').getWidth()
				var ht = Ext.get('gridDiv').getHeight()	
				var sm = new Ext.grid.CellSelectionModel({});
				grid = new Ext.grid.EditorGridPanel({
					renderTo : 'gridDiv',
					stripeRows: true, 
					id : 'grid',
					split : true,
					width : wd,
					height : ht,
					autoScroll : true,
					border : false,
//					columns : json.columModel, 
					sm : sm,
					cm : new Ext.grid.ColumnModel(
						{
						columns : Ext.decode(json.columModel)
					}),
					enableColumnMove : false,  
					//plugins : [new Ext.ux.plugins.GroupHeaderGrid(null,json.rows)],
					ds : ds,
					clicksToEdit : 1
				});  
//				grid.rows = json.rows;
				grid.render();
				for (var i = 0; i <= ds.getTotalCount() - 1; i++) 
				for(var j = 2; j <= grid.getColumnModel().getColumnCount() - 2; j++)
				{
					grid.getSelectionModel().select(i,j);
					var cell = grid.getSelectionModel().getSelectedCell()
					var hearder = grid.getColumnModel().getDataIndex(cell[1]);
					var advice = grid.getStore().getAt(cell[0]).get(hearder)
					if(advice == null || advice == "")
					{
						 canSave = false;
					}
					else 
					{
						canSave = true;
					}
				}
				ds.on('update',function(ds,record,edit){
					isSaved = false;
				});

					if (ds != null && ds.getTotalCount() > 0) {
					if (ds.getAt(0).get('gatherStatus') == null
							|| ds.getAt(0).get('gatherStatus') == "")
						isSaved = false;
				}
				grid.on('beforeedit', function(e) {
							// true可以编辑，false 不可编辑
					return false;
						})
//				Ext.Msg.hide();
			},
			failure : function(result, request) {
				Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
//				Ext.Msg.hide();
			}
		});
	}
	// 设定布局器及面板
	var view = new Ext.Viewport({
				layout : 'fit',
				margins : '0 0 0 0',
				collapsible : true,
				split : true,
				border : false,
				items : [new Ext.Panel({
							id : 'panel',
							border : false,
							items : [{
										html : '<div id="gridDiv"></div>'
									}]
						})]
			});
	Ext.get('gridDiv').setWidth(Ext.get('panel').getWidth());
	Ext.get('gridDiv').setHeight(Ext.get('panel').getHeight() - 25);
	//遍历所有的REDIO获得ID
	function getChooseQueryType() {
		var list = document.getElementsByName("queryWayRadio");
		for (var i = 0; i < list.length; i++) {
			if (list[i].checked) {
				return list[i].id;
			}
		}
	}
	query();
});