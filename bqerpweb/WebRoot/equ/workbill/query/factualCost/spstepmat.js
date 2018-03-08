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
		name : 'baseInfo.orderby'
	}, {
		name : 'baseInfo.factItemQty'
	}, {
		name : 'baseInfo.planLocationId'
	}, {
		name : 'baseInfo.factMatPicid'
	}, {
		name : 'baseInfo.factMaterialPrice'
	}, {
		name : 'baseInfo.planMaterialCode'
	}, {
		name : 'baseInfo.planUnit'
	}, {
		name : 'baseInfo.woCode'
	}, {
		name : 'baseInfo.operationStep'
	}, {
		name : 'matName'
	}, {
		name : 'fee'
	}]);

	var centerGridsm = new Ext.grid.CheckboxSelectionModel();

	var centerGrids = new Ext.data.JsonStore({
		url : 'workbill/getEquJMainmat.action?woCode=' + woCode + '&opCode='
				+ opCode,
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
	var planLaborCode_data = [["专业AB", "AB"], ["专业CD", "CD"], ["专业EF", "EF"],
			["专业GH", "GH"], ["专业IJ", "IJ"]];

	function renderMoney(v) {
		return renderNumber(v, 4);
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
	var mystore = new Ext.data.SimpleStore({
				fields : ['planUnitName', 'planUnit'],
				data : planLaborCode_data
			});

	var mystore2 = new Ext.data.SimpleStore({
				fields : ['planLocationName', 'planLocationId'],
				data : planLaborCode_data
			});

	
	var centerGrid = new Ext.grid.GridPanel({

		ds : centerGrids,
		sm : centerGridsm,
		columns : [centerGridsm, new Ext.grid.RowNumberer(), {
			header : "物资",
			sortable : false,
			dataIndex : 'matName'

	

		}, {
			header : "物资编码",
			sortable : false,
			dataIndex : 'baseInfo.planMaterialCode',
			hidden : true
		}, {
			header : "仓库",
			sortable : false,
			dataIndex : 'baseInfo.planLocationId',
			hidden:true,
			renderer : function changeIt(val) {
				for (i = 0; i < mystore2.getCount(); i++) {
					if (mystore2.getAt(i).get("factLocationId") == val)
						return mystore2.getAt(i).get("factLocationName");
				}

			}
		}, {
			header : "数量",
			sortable : false,
			dataIndex : 'baseInfo.factItemQty'
		}, {
			header : "计量单位",
			sortable : false,
			dataIndex : 'baseInfo.planUnit',
		
			renderer : function changeIt(val) {
				for (i = 0; i < mystore.getCount(); i++) {
					if (mystore.getAt(i).get("planUnit") == val)
						return mystore.getAt(i).get("planUnitName");
				}

			}
		}, {
			header : "单价",
			sortable : false,
			dataIndex : 'baseInfo.factMaterialPrice'
		}, {
			header : "费用",
			sortable : true,
			// dataIndex:'fee',
			   renderer : function(value, params, record) {
            	var v = (10000 * Number(record.get("baseInfo.factMaterialPrice")) * Number(record.get("baseInfo.factItemQty")))/10000;
            	return renderMoney(v);
            }

				// dataIndex : 'baseInfo.planMaterialPrice*baseInfo.planItemQty'
				// editor : new Ext.form.NumberField()

		}],
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