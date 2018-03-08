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
	var stdopCode = getParameter("stdopCode")
	var workorderStatus = getParameter("workorderStatus");

	var url4;
	if (stdopCode != '') {

		url4 = 'workbill/getEquCMainmat.action?woCode=' + stdWoCode
				+ '&opCode=' + stdopCode;

	} else {
		url4 = 'workbill/getEquJMainmat.action?woCode=' + woCode + '&opCode='
				+ opCode;

	}

	/** 窗口元素↓↓* */

	var datalist = new Ext.data.Record.create([{
				name : 'baseInfo.id'
			}, {
				name : 'baseInfo.orderby'
			}, {
				name : 'baseInfo.planItemQty'
			}, {
				name : 'baseInfo.planLocationId'
			}, {
				name : 'baseInfo.planMatPicid'
			}, {
				name : 'baseInfo.planMaterialPrice'
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
				url : url4,
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

	/** centerGrid的按钮组↓↓* */
	// 增加
	function addRecords() {
		// var currentRecord = gird.getSelectionModel().getSelected();
		var count = centerGrids.getCount();
		var currentIndex = count;
		// var currentIndex = currentRecord
		// ? currentRecord.get("displayNo") - 1
		// : count;
		var o = new datalist({
					'baseInfo.woCode' : woCode,
					'baseInfo.operationStep' : opCode,
					'baseInfo.planItemQty' : '',
					'baseInfo.planLocationId' : '',
					'baseInfo.planMaterialPrice' : '',
					'matName' : '',
					'baseInfo.planUnit' : ''
				});

		centerGrid.stopEditing();
		centerGrids.insert(currentIndex, o);
		centerGridsm.selectRow(currentIndex);
		centerGrid.startEditing(currentIndex, 2);
		// resetLine();
	}

	// 删除记录
	var ids = new Array();
	function deleteRecords() {
		centerGrid.stopEditing();

		var centerGridsm = centerGrid.getSelectionModel();
		var selected = centerGridsm.getSelections();
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i];
				if (member.get("baseInfo.id") != null) {
					ids.push(member.get("baseInfo.id"));
				}
				centerGrid.getStore().remove(member);
				centerGrid.getStore().getModifiedRecords().remove(member);
			}
			// resetLine();
		}
	}

	// 保存
	function save() {
		centerGrid.stopEditing();
		var modifyRec = centerGrid.getStore().getModifiedRecords();

		if (modifyRec.length > 0 || ids.length > 0) {
			if (!confirm("确定要保存修改吗?"))
				return;
			// var newData = new Array();
			var updateData = new Array();
			for (var i = 0; i < modifyRec.length; i++) {
				// if (modifyRec[i].get("id") == "") {
				// newData.push(modifyRec[i].data);
				// } else {
				updateData.push(modifyRec[i].data);
				// }
			}

			Ext.Ajax.request({
						url : 'workbill/saveEquJMainmat.action',
						method : 'post',
						params : {
							// isAdd : Ext.util.JSON.encode(newData),
							isUpdate : Ext.util.JSON.encode(updateData),
							woCode : woCode,
							isDelete : ids.join(",")
						},
						success : function(result, request) {

							var o = eval('(' + result.responseText + ')');
							Ext.MessageBox.alert('提示信息', o.msg);

							centerGrids.rejectChanges();
							ids = [];
							centerGrids.reload();
						},
						failure : function(result, request) {
							Ext.MessageBox.alert('提示信息', '未知错误！')
						}
					})
		} else {
			Ext.MessageBox.alert('提示信息', '没有做任何修改！')
		}
	}
	// 取消
	function cancer() {
		var modifyRec = centerGrids.getModifiedRecords();
		if (modifyRec.length > 0 || ids.length > 0) {
			if (!confirm("确定要放弃修改吗"))
				return;
			centerGrids.reload();
			centerGrids.rejectChanges();
			ids = [];
		} else {
			centerGrids.reload();
			centerGrids.rejectChanges();
			ids = [];
		}
	}

	// 新增
	var centerGridAdd = new Ext.Button({
				text : '新增',
				iconCls : 'add',
				handler : addRecords

			});

	var centerGridDel = new Ext.Button({
				text : '删除',
				iconCls : 'delete',
				handler : deleteRecords
			});

	var centerGridSave = new Ext.Button({
				text : '保存修改',
				iconCls : 'save',
				handler : save
			});

	// 取消
	var centerGridCancer = new Ext.Button({
		text : '取消',
		iconCls : 'cancer',
		handler : cancer
			// function() {
			// centerGrids.load({
			// params : {
			// start : 0,
			// limit : 18
			// }
			// });
			// }
		});
	/** centerGrid的按钮组↑↑* */

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
							if (mystore2.getAt(i).get("planLocationId") == val)
								return mystore2.getAt(i)
										.get("planLocationName");
						}

					}
				}, {
					header : "数量",
					sortable : false,
					dataIndex : 'baseInfo.planItemQty'
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
					dataIndex : 'baseInfo.planMaterialPrice'
				}, {
					header : "费用",
					sortable : true,
					// dataIndex:'fee',
					renderer : function(value, params, record) {
						var v = (10000
								* Number(record
										.get("baseInfo.planMaterialPrice")) * Number(record
								.get("baseInfo.planItemQty")))
								/ 10000;
						return renderMoney(v);
					}

					// dataIndex :
					// 'baseInfo.planMaterialPrice*baseInfo.planItemQty'
					// editor : new Ext.form.NumberField()

				}],
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

		// if (getParameter("opCode") == null || getParameter("opCode") == ''
		// || getParameter("opCode") == 'undefined'||workorderStatus=='1') {
		//
		// centerGridAdd.setDisabled(true);
		// centerGridSave.setDisabled(true);
		// centerGridDel.setDisabled(true);
		// centerGridCancer.setDisabled(true);
		//
		// }
})