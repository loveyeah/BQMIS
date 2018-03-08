Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	Ext.QuickTips.init();

	function getParameter(psName) {
		var url = self.location;
		var result = "";
		var str = self.location.search.substring(0);
		if (str.indexOf(psName) != -1
				&& (str.substr(str.indexOf(psName) - 1, 1) == "?" || str
						.substr(str.indexOf(psName) - 1, 1) == "&")) {
			if (str.substring(str.indexOf(psName), str.length).indexOf("&") != -1) {
				var Test = str.substring(str.indexOf(psName), str.length);
				result = Test.substr(Test.indexOf(psName), Test.indexOf("&")
						- Test.indexOf(psName));
				result = result.substring(result.indexOf("=") + 1,
						result.length);
			} else {
				result = str.substring(str.indexOf(psName), str.length);
				result = result.substring(result.indexOf("=") + 1,
						result.length);
			}
		}
		return result;
	}

	var woCode = getParameter("woCode");
	var opCode = getParameter("opCode");

	/** 窗口元素↓↓* */

	var datalist = new Ext.data.Record.create([{
		name : 'baseInfo.id'
	}, {
		name : 'baseInfo.factFee'
	}, {
		name : 'baseInfo.planServiceCode'
	}, {
		name : 'baseInfo.planServiceUnit'
	}, {
		name : 'baseInfo.woCode'
	}, {
		name : 'baseInfo.operationStep'
	}, {
		name : 'servName'
	}]);

	var centerGridsm = new Ext.grid.CheckboxSelectionModel();

	var centerGrids = new Ext.data.JsonStore({
		url : 'workbill/getEquJStandardService.action?woCode=' + woCode
				+ '&opCode=' + opCode,
		root : 'list',
		totalProperty : 'totalCount',
		fields : datalist
	});

	centerGrids.load({
		params : {
			start : 0,
			limit : 18
		}
	});

	// 列表
	var mystore=new Ext.data.SimpleStore({
					fields : ['Name', 'Value'],
					data : [['我的', '1'], ['你的', '2'], ['他的', '3'], ['她的', '4']]
				});
				
	
	var centerGrid = new Ext.grid.GridPanel({
		ds : centerGrids,
		columns : [centerGridsm, new Ext.grid.RowNumberer(), {
			header : "服务",
			sortable : false,
			dataIndex : 'servName'
		}, {
			header : "服务编码",
			sortable : false,
			hidden : true,
			dataIndex : 'baseInfo.planServiceCode'

		}, {
			header : "服务计量单位",
			sortable : false,
			hidden:true,
			dataIndex : 'baseInfo.planServiceUnit',
				renderer : function changeIt(val) {
					for (i = 0; i < mystore.getCount(); i++) {
						if (mystore.getAt(i).get("Value") == val)
							return mystore.getAt(i).get("Name");
					}

				}

		}, {
			header : "费用",
			sortable : false,
			dataIndex : 'baseInfo.factFee'
		}],
		sm : centerGridsm,
		tbar : [],
		frame : true,
		border : true,
		clicksToEdit:1,//单击一次编辑
		enableColumnHide : false,
		enableColumnMove : false,
		iconCls : 'icon-grid'
	});

	// 设定布局器及面板
	var layout = new Ext.Viewport({
		layout : "border",
		border : false,
		items : [{
			title : "",
			region : 'center',
			layout : 'fit',
			border : false,
			margins : '0',
			height : 200,
			split : false,
			collapsible : false,
			items : [centerGrid]
		}]
	});
})