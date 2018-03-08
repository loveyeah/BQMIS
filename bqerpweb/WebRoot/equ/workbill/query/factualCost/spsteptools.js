Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {

	function getParameter(psName) {
		var url = self.location;
		var result = "";
		var str = self.location.search.substring(0);
		if (str.indexOf(psName) != -1
				&& (str.substr(str.indexOf(psName) - 1, 1) == "?" || str
						.substr(str.indexOf(psName) - 1, 1) ==

				"&")) {
			if (str.substring(str.indexOf(psName), str.length).indexOf("&")

			!= -1) {
				var Test = str.substring(str.indexOf(psName),

				str.length);
				result = Test.substr(Test.indexOf(psName), Test.indexOf

				("&") - Test.indexOf(psName));
				result = result.substring(result.indexOf("=") + 1,
						result.length);
			} else {
				result = str.substring(str.indexOf(psName), str.length);
				result = result.substring(result.indexOf("=") + 1,
						result.length);
			}
		}
		return result;
	};

	var woCode1 = getParameter("woCode");

	var operationStep1 = getParameter("opCode");
	

	var rec = Ext.data.Record.create([{
		name : 'baseInfo.woCode'
	}, {
		name : 'baseInfo.operationStep'
	}, {
		name : 'baseInfo.planToolNum'
	}, {
		name : 'baseInfo.planLocationId'

	}, {
		name : 'baseInfo.factToolQty'
	}, {
		name : 'baseInfo.factToolHrs'
	}, {
		name : 'baseInfo.factToolPrice'

	}, {
		name : 'baseInfo.planToolDescription'

	}, {
		name : 'baseInfo.id'

	}, {
		name : 'baseInfo.orderby'
	}, {
		name : 'toolsName'
	}]);

	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'workbill/findAll.action'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalCount',
			root : 'list'
		}, rec)
	});

	ds.load({
		params : {
			woCode : woCode1,
			operationStep : operationStep1,
			start : 0,
			limit : 18
		}
	});

	/* 设置每一行的选择框 */
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false,
		listeners : {
			rowselect : function(sm, row, rec) {
				// Ext.getCmp("form").getForm().loadRecord(rec);
			}
		}
	});

	var rn = new Ext.grid.RowNumberer({

	});

	
	 var mystore = new Ext.data.SimpleStore({
										fields : ["retrunValue", "displayText"],
										data : [['1', '典雅黑'], ['2', '深灰']]
									});

	
	var cm = new Ext.grid.ColumnModel([sm, rn

	, {
		header : '工具',
		dataIndex : 'toolsName',
		align : 'left'
	}, {
		header : '工具编码',
		dataIndex : 'baseInfo.planToolNum',
		align : 'left',
		hidden : true
		
			}, {
				header : '数量',
				dataIndex : 'baseInfo.factToolQty',
				align : 'left'
			},
			{
				header : '工时',
				dataIndex : 'baseInfo.factToolHrs',
				align : 'left'
			// ,
			// width : auto
			}, {
				header : '所在仓库',
				dataIndex : 'baseInfo.planLocationId',
				align : 'left',
				hidden:true,
				renderer : function changeIt(val) {
					for (i = 0; i < mystore.getCount(); i++) {
						if (mystore.getAt(i).get("retrunValue") == val)
							return mystore.getAt(i).get("displayText");
					}
					// return val
				}

			}, {
				header : '描述',
				dataIndex : 'baseInfo.planToolDescription',
				align : 'left'
			}, {
				header : '费用',
				dataIndex : 'baseInfo.factToolPrice',
				align : 'left'
			}

	]);



	/* 创建表格 */
	var grid = new Ext.grid.EditorGridPanel({
		// el : 'siteTeam',
		ds : ds,
		cm : cm,
		sm : sm,

//		tbar : tbar,
		// title : '用户列表',
		autoWidth : true,
		fitToFrame : true,
		border : true,
		frame : true,
		clicksToEdit:1,//单击一次编辑
		viewConfig : {
			forceFit : false
		}
	});
	// ds.on('beforeload', function() {
	// Ext.apply(this.baseParams, {
	// userlike : Ext.get("userlike").dom.value
	// });
	// });
	// grid.enableColumnHide = false;
	// grid.render();
	// grid.on("rowdblclick", function() {
	// Ext.get("btnUpdate").dom.click();
	// });

	// 设定布局器及面板
	var viewport = new Ext.Viewport({
		layout : 'fit',
		autoWidth : true,
		autoHeight : true,
		fitToFrame : true,
		items : [grid]
	});
})