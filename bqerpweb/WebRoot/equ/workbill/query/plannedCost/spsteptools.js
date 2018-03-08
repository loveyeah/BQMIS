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

	
	var woCode = getParameter("woCode");
	var stdWoCode = getParameter("stdWoCode");
	var opCode = getParameter("opCode");
	var stdopCode = getParameter("stdopCode")
	var workorderStatus = getParameter("workorderStatus");
	
	var url1;

	if (stdopCode != '') {

		
		var url1 = 'workbill/getEquCTools.action?woCode=' + stdWoCode
				+ '&operationStep=' + stdopCode;

	} else {
		url1 = 'workbill/findAll.action?woCode=' + woCode + '&operationStep='
				+ opCode;
	}

	var rec = Ext.data.Record.create([{
				name : 'baseInfo.woCode'
			}, {
				name : 'baseInfo.operationStep'
			}, {
				name : 'baseInfo.planToolNum'
			}, {
				name : 'baseInfo.planLocationId'

			}, {
				name : 'baseInfo.planToolQty'
			}, {
				name : 'baseInfo.planToolHrs'
			}, {
				name : 'baseInfo.planToolPrice'

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
							url : url1
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : 'totalCount',
							root : 'list'
						}, rec)
			});

	ds.load({
				params : {

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
		dataIndex : 'baseInfo.planToolQty',
		align : 'left'
		},
			
			{
				header : '工时',
				dataIndex : 'baseInfo.planToolHrs',
				align : 'left'
			}, {
				header : '所在仓库',
				dataIndex : 'baseInfo.planLocationId',
				hidden:true,
				align : 'left',
				
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
				align : 'left'			}, {
				header : '费用',
				dataIndex : 'baseInfo.planToolPrice',
				align : 'left'
			
		}

	]);

	/* 创建表格 */
	var grid = new Ext.grid.GridPanel({
				// el : 'siteTeam',
				ds : ds,
				cm : cm,
				sm : sm,

//				tbar : tbar,
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

	// 设定布局器及面板
	var viewport = new Ext.Viewport({
				layout : 'fit',
				autoWidth : true,
				autoHeight : true,
				fitToFrame : true,
				items : [grid]
			});
//	if (getParameter("opCode") == null || getParameter("opCode") == ''
//			|| getParameter("opCode") == 'undefined'||workorderStatus=='1') {
//		Ext.get('btnAdd').dom.disabled = true;
//		Ext.get('btnSave').dom.disabled = true;
//		Ext.get('btnDelete').dom.disabled = true;
//
//		Ext.get('btnCancer').dom.disabled = true;
//
//	}
})