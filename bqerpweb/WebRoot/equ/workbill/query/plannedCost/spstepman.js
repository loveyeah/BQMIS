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
	var stdWoCode = getParameter("stdWoCode");
	var opCode = getParameter("opCode");
	var stdopCode = getParameter("stdopCode");

	var workorderStatus = getParameter("workorderStatus");
	var url1;

	if (stdopCode != '') {
		// alert(1)

		url1 = 'workbill/getEquCManplan.action?woCode=' + stdWoCode
				+ '&opCode=' + stdopCode;
	} else {
		// alert(2)
		url1 = 'workbill/getEquJManplan.action?woCode=' + woCode + '&opCode='
				+ opCode;
	}

	/** 弹窗元素↓↓* */

	// 工种
	var url = "com/getTypeOfWorkList.action";
	var conn = Ext.lib.Ajax.getConnectionObject().conn;
	conn.open("POST", url, false);
	conn.send(null);
	var planLaborCode_data = eval('(' + conn.responseText + ')');
	var mystore = new Ext.data.SimpleStore({
				fields : ['planLaborName', 'planLaborCode'],
				data : planLaborCode_data
			});

	/** 弹窗元素↑↑* */
	/** 窗口元素↓↓* */

	var datalist = new Ext.data.Record.create([{
				name : 'id'
			}, {
				name : 'orderby'
			}, {
				name : 'planFee'
			}, {
				name : 'planLaborCode'
			}, {
				name : 'planLaborHrs'
			}, {
				name : 'planLaborQty'
			}, {
				name : 'woCode'
			}, {
				name : 'operationStep'
			}]);

	var centerGridsm = new Ext.grid.CheckboxSelectionModel();

	var centerGrids = new Ext.data.JsonStore({
				url : url1,
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
	var centerGrid = new Ext.grid.GridPanel({
				ds : centerGrids,
				columns : [centerGridsm, new Ext.grid.RowNumberer(), {
							header : "工种",
							sortable : false,
							dataIndex : 'planLaborCode',
							renderer : function changeIt(val) {
								for (i = 0; i < mystore.getCount(); i++) {
									if (mystore.getAt(i).get("planLaborCode") == val)
										return mystore.getAt(i)
												.get("planLaborName");
								}

							}

						}, {
							header : "人数",
							sortable : false,
							dataIndex : 'planLaborQty'
						}, {
							header : "工时",
							sortable : false,
							dataIndex : 'planLaborHrs'
						}, {
							header : "费用",
							sortable : false,
							dataIndex : 'planFee'
						}],
				sm : centerGridsm,
				tbar : [],
				frame : true,
				border : true,
				clicksToEdit : 1,// 单击一次编辑
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

//	if (getParameter("opCode") == null || getParameter("opCode") == ''
//			|| getParameter("opCode") == 'undefined'||workorderStatus=='1') {
//
//		centerGridAdd.setDisabled(true);
//		centerGridSave.setDisabled(true);
//		centerGridDel.setDisabled(true);
//		centerGridCancel.setDisabled(true);
//
//	}

})