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

	var workorderStatus = getParameter("workorderStatus");

	
	/** 窗口元素↓↓* */
	var datalist = new Ext.data.Record.create([{
				name : 'logContent'
			}, {
				name : 'id'
			}, {
				name : 'woCode'
			}]);

	var northGridsm = new Ext.grid.CheckboxSelectionModel();

	var northGrids = new Ext.data.JsonStore({
				url : 'workbill/getEquJWoLog.action?woCode=' + woCode,
				root : 'list',
				totalProperty : 'totalCount',
				fields : datalist
			});

	northGrids.load({
				params : {
					start : 0,
					limit : 18
				}
			});

	
	// 列表

	var northGrid = new Ext.grid.GridPanel({
		ds : northGrids,
		columns : [northGridsm, new Ext.grid.RowNumberer(), {
					width : 700,
					header : "日志内容",
					sortable : false,
					dataIndex : 'logContent'
				}],
		sm : northGridsm,
		tbar : [],
		frame : true,
		border : true,
		clicksToEdit:1,//单击一次编辑
		enableColumnHide : false,
		enableColumnMove : false
			// ,
			// iconCls : 'icon-grid'
		});

	// 设定布局器及面板
	var layout = new Ext.Viewport({
				layout : 'border',
				border : false,
				items : [{
							title : "",
							region : 'center',
							layout : 'fit',
							// border : false,
							// margins : '0',
							// height : 200,
							// split : false,
							// collapsible : false,
							items : [northGrid]
						}]
			});

	if (woCode == '' || workorderStatus > '0') {

		/*btnAdd.setDisabled(true);
		btnDelete.setDisabled(true);
		btnCancel.setDisabled(true);
		btnSave.setDisabled(true);;*/

	}

})