Ext.BLANK_IMAGE_URL = 'ext/resources/images/default/s.gif';
Ext.form.Label.prototype.setText = function(argText) {
	this.el.dom.innerHTML = argText;
}

function getDate() {
	var d, s, t;
	d = new Date();
	s = d.getFullYear().toString(10) + "-";
	t = d.getMonth() + 1;
	s += (t > 9 ? "" : "0") + t + "-";
	t = d.getDate();
	s += (t > 9 ? "" : "0") + t + " ";
	t = d.getHours();
	s += (t > 9 ? "" : "0") + t + ":";
	t = d.getMinutes();
	s += (t > 9 ? "" : "0") + t + ":";
	t = d.getSeconds();
	s += (t > 9 ? "" : "0") + t;
	return s;
}

var sessWorname;
var sessWorcode;
var fillBy;
var totalAmountSum;
var budgetItemId = getParameter("budgetItemId");
var makeStatus = getParameter("makeStatus");
var unit = getParameter("unit");

Ext.onReady(function() {
	
Ext.form.Label.prototype.setText = function(argText) {
		this.el.dom.innerHTML = argText;
	}
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false
	});

	var Record = new Ext.data.Record.create([sm, {
		name : 'basisId',
		mapping : 0
	}, {
		name : 'budgetItemId',
		mapping : 1
	}, {
		name : 'budgetBasis',
		mapping : 2
	}, {
		name : 'budgetAmount',
		mapping : 3
	}, {
		name : 'lastModifyBy',
		mapping : 4
	}, {
		name : 'lastModifyDate',
		mapping : 5
	}]);

	var store = new Ext.data.JsonStore({
		url : 'managebudget/findBasisListById.action',
		root : 'list',
		totalProperty : 'totalCount',
		fields : Record
	});

	var unitLabel = new Ext.form.Label({
		id : 'unitLabel',
		text : "单位："
	});
	var unitValue = new Ext.form.Label({
		id : 'unitValue',
		name : 'unitValue',
		readOnly :true,
		width : 80
	});
	
	var gridTbar = new Ext.Toolbar({
		items : [{
			id : 'reflesh',
			text : "刷新",
			iconCls : 'reflesh',
			handler : query
		}, '-', {
			id : 'add',
			text : "新增",
			iconCls : 'add',
			handler : addRecord
		}, '-', {
			id : 'delete',
			text : "删除",
			iconCls : 'delete',
			handler : deleteRecord
		}, '-', {
			id : 'save',
			text : "保存",
			iconCls : 'save',
			handler : saveRecord
		},'-',unitLabel,unitValue
		// , '-', {
		// id : 'confirm',
		// text : "确定",
		// iconCls : 'query',
		// handler : confirmRecord
		// }
		]
	});

	var fillName = new Ext.form.TextField({
		id : 'fillName',
		fieldLabel : '填写人',
		name : 'fillName',
		anchor : "85%",
		readOnly : true
	});

	var fillTime = new Ext.form.TextField({
		id : 'fillTime',
		fieldLabel : '填写时间',
		name : 'fillTime',
		readOnly : true,
		value : getDate(),
		anchor : "85%"
	});

	var stationGrid = new Ext.grid.EditorGridPanel({
		store : store,
		layout : 'fit',
		columns : [sm, new Ext.grid.RowNumberer({
			header : '行号',
			width : 35
		}), {
			header : '编制依据',
			dataIndex : 'budgetBasis',
			align : 'left',
			width : 220,
			editor : new Ext.form.TextField({
				allowBlank : false
			})
		}, {
			header : '金额',
			dataIndex : 'budgetAmount',
			align : 'center',
			width : 180,
			editor : new Ext.form.NumberField({
				allowBlank : false,
				selectOnFocus : true,
				allowDecimals : true,
				decimalPrecision : 5
			})
		}, {
			header : '上次修改人',
			dataIndex : 'lastModifyBy',
			align : 'left',
			width : 220
		}, {
			header : '上次修改日期',
			dataIndex : 'lastModifyDate',
			align : 'left',
			width : 220
		}],
		sm : sm,
		tbar : gridTbar,
		clicksToEdit : 1,
		// 分页
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : store,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		})
	});

	store.on('load', function() {
		if (makeStatus == "" || makeStatus == '0' || makeStatus == null
				|| makeStatus == 3) {
			stationGrid.getTopToolbar().items.get('add').setDisabled(false);
			stationGrid.getTopToolbar().items.get('delete').setDisabled(false);
			stationGrid.getTopToolbar().items.get('save').setDisabled(false);
			// stationGrid.getTopToolbar().items.get('confirm').setDisabled(false);
			stationGrid.getTopToolbar().items.get('reflesh').setDisabled(false);
		} else {
			stationGrid.getTopToolbar().items.get('add').setDisabled(true);
			stationGrid.getTopToolbar().items.get('delete').setDisabled(true);
			stationGrid.getTopToolbar().items.get('save').setDisabled(true);
			// stationGrid.getTopToolbar().items.get('confirm').setDisabled(true);
			stationGrid.getTopToolbar().items.get('reflesh').setDisabled(true);
		}

			// getNum();
	});

	function query() {
		store.baseParams = {
			budgetItemId : budgetItemId
		};
		store.load({
			params : {
				start : 0,
				limit : 18
			}
		});
	}
	query();

	function addRecord() {
		var count = store.getCount();
		var currentIndex = count;
		var o = new Record({
			'basisId' : '',
			'budgetItemId' : budgetItemId,
			'budgetBasis' : '',
			'budgetAmount' : ''
		});
		stationGrid.stopEditing();
		store.insert(currentIndex, o);
		sm.selectRow(currentIndex);
		stationGrid.startEditing(currentIndex, 1);
	}

	var ids = new Array();
	function deleteRecord() {
		stationGrid.stopEditing();
		var sm = stationGrid.getSelectionModel();
		var selected = sm.getSelections();
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i];
				if (member.get("basisId") != null) {
					ids.push(member.get("basisId"));
				}
				stationGrid.getStore().remove(member);
				stationGrid.getStore().getModifiedRecords().remove(member);
			}
		}
	}
	
	function saveRecord() {
		var alertMsg = "";
		stationGrid.stopEditing();
		var modifyRec = stationGrid.getStore().getModifiedRecords();

		var amountSum = 0;
		for (var i = 0; i < store.getCount(); i++) {
				amountSum += store.getAt(i).get('budgetAmount');
		}
		if (modifyRec.length > 0 || ids.length > 0) {
			Ext.Msg.confirm('提示', '确定要保存修改数据吗?', function(button) {
				if (button == 'yes') {
					var updateData = new Array();
					for (var i = 0; i < modifyRec.length; i++) {
						if (modifyRec[i].get("budgetBasis") == null
								|| modifyRec[i].get("budgetBasis") == "") {
							alertMsg += "编制依据不能为空</br>";
						}
						if (modifyRec[i].get("budgetAmount") == null
								|| modifyRec[i].get("budgetAmount") == "") {
							alertMsg += "金额不能为空</br>";
						}
						if (alertMsg != "") {
							Ext.Msg.alert("提示", alertMsg);
							return;
						}
						updateData.push(modifyRec[i].data);
					}
					Ext.Ajax.request({
						url : 'managebudget/editBasisRecord.action',
						method : 'post',
						params : {
							isUpdate : Ext.util.JSON.encode(updateData),
							isDelete : ids.join(","),
							amountSum : amountSum,
							budgetItemId : budgetItemId
						},
						success : function(form, options) {
							var obj = Ext.util.JSON.decode(form.responseText)
							Ext.MessageBox.alert('提示信息', '保存成功!');
							// store.rejectChanges();
							// 保存成功后，直接把amountSum值付过去
							var basis = new Object();
							basis.amountSum = amountSum.toString();
							window.returnValue = basis;
							window.close();
						},
						failure : function(result, request) {
							Ext.MessageBox.alert('提示信息', '操作失败！')
						}
					})
				} else {
					store.rejectChanges();
					store.reload();
				}
			})
		} else {
			Ext.Msg.alert("提示", "未做任何修改！");
			store.rejectChanges();
			store.reload();
		}

	}
	
	
	// function confirmRecord() {
	// stationGrid.stopEditing();
	// var amountSum = null;
	// var basisId = null;
	// for (var i = 0; i < store.getCount(); i++) {
	// amountSum += store.getAt(i).get('budgetAmount');
	// basisId = store.getAt(i).get('basisId');
	// }
	// if ((store.getCount() != 0) && (basisId == "" || basisId == null)) {
	// Ext.Msg.alert("提示", "请先保存数据！");
	// return;
	// } else if (store.getCount() == 0) {
	// amountSum = 0;
	// Ext.Msg.confirm("选择", "编制依据明细已全部删除，是否同步修改预算值？",
	// function(buttonobj) {
	// if (buttonobj == "yes") {
	// var basis = new Object();
	// basis.amountSum = amountSum.toString();
	// window.returnValue = basis;
	// window.close();
	// }
	// })
	// } else {
	// Ext.Msg.confirm("选择", "是否确定选择？", function(buttonobj) {
	// if (buttonobj == "yes") {
	// var basis = new Object();
	// basis.amountSum = amountSum.toString();
	// window.returnValue = basis;
	// window.close();
	// }
	// })
	// }
	// }

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		border : false,
		frame : false,
		items : [{
			bodyStyle : "padding: 1,1,1,0",
			region : "center",
			border : false,
			frame : false,
			layout : 'fit',
			items : [stationGrid]
		}]
	});

	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
			success : function(action) {
				var result = eval("(" + action.responseText + ")");
				if (result.workerCode) {
					// 设定默认工号
					sessWorcode = result.workerCode;
					sessWorname = result.workerName;
					loadData();
				}
			}
		});
	}
	function loadData() {
		fillBy = sessWorcode;
		Ext.get('fillName').dom.value = sessWorname;
	};
	
	unitValue.setText(unit);
})
