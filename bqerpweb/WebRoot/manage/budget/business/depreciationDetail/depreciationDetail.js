Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	function numberFormat(value) {
		value = String(value);
		if (value == null || value == "null") {
			value = "0";
		}
		// 整数部分
		var whole = value;
		// 小数部分
		var sub = ".00";
		// 如果有小数
		if (value.indexOf(".") > 0) {
			whole = value.substring(0, value.indexOf("."));
			sub = value.substring(value.indexOf("."), value.length);
			sub = sub + "00";
			if (sub.length > 3) {
				sub = sub.substring(0, 3);
			}
		}
		var r = /(\d+)(\d{3})/;
		while (r.test(whole)) {
			whole = whole.replace(r, '$1' + ',' + '$2');
		}
		v = whole + sub;
		if (whole == null || whole == "null" || whole == "") {
			v = "0.00";
		}
		return v;
	}

	function percentFormat(value) {
		if (value == null || value == "null") {
			value = "0";
		}
		if (value == '999999999999999') {
			return null;
		}
		value = String(value);
		// 整数部分
		var whole = value;
		// 小数部分
		var sub = ".00";
		// 如果有小数
		if (value.indexOf(".") > 0) {
			whole = value.substring(0, value.indexOf("."));
			sub = value.substring(value.indexOf("."), value.length);
			sub = sub + "00";
			if (sub.length > 3) {
				sub = sub.substring(0, 3);
			}
		}
		var v = whole + sub;
		return v + '%';
	}
	function getCurrentYear() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString();
		return s;
	}
	// 年度
	var year = new Ext.form.TextField({
		readOnly : true,
		width : 80,
		id : 'year',
		style : 'cursor:pointer',
		value : getCurrentYear(),
		listeners : {
			focus : function() {
				WdatePicker({
					dateFmt : 'yyyy',
					alwaysUseStartDate : false
				});
				this.blur();
			}
		}
	});

	var item = Ext.data.Record.create([{
		name : 'depreciationDetailId'
	}, {
		name : 'depreciationId'
	}, {
		name : 'assetName'
	}, {
		name : 'lastAsset'
	}, {
		name : 'addAsset'
	}, {
		name : 'reduceAsset'
	}, {
		name : 'newAsset'
	}, {
		name : 'depreciationRate'
	}, {
		name : 'depreciationNumber'
	}, {
		name : 'depreciationSum'
	}, {
		name : 'memo'
	}, {
		name : 'budgetTime'
	}, {
		name : 'workFlowNo'
	}, {
		name : 'workFlowStatus'
	}, {
		name : 'isUse'
	}, {
		name : 'budgetTime'
	}, {
		name : 'newAssetCount'
	}]);

	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false

	});
	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'managebudget/findDepreciationList.action'
		}),
		reader : new Ext.data.JsonReader({
			totalCount : "totalCount",
			root : "list"
		}, item)
	});

	var cm = new Ext.grid.ColumnModel([sm, new Ext.grid.RowNumberer({
		header : "行号",
		width : 31
	}), {
		header : 'depreciationDetailId',
		dataIndex : 'depreciationDetailId',
		hidden : true,
		align : 'left'
	}, {
		header : 'depreciationId',
		dataIndex : 'depreciationId',
		hidden : true,
		align : 'left'
	}, {
		header : '资产名称',
		anchor : '80%',
		dataIndex : 'assetName',
		align : 'left',
		css : CSS_GRID_INPUT_COL,
		editor : new Ext.form.TextField({})
	}, {
		header : '去年年末资产',
		dataIndex : 'lastAsset',
		anchor : '80%',
		css : CSS_GRID_INPUT_COL,
		align : 'left',
		editor : new Ext.form.NumberField({
			allowBlank : false,
			allowDecimal : true,
			allowNegative : false,
			decimalPrecision : 4
		}),
		renderer : function(value) {
			return numberFormat(value);
		}
	}, {
		header : '本年预算增加',
		anchor : '80%',
		dataIndex : 'addAsset',
		css : CSS_GRID_INPUT_COL,
		align : 'left',
		editor : new Ext.form.NumberField({
			allowBlank : false,
			allowDecimal : true,
			allowNegative : false,
			decimalPrecision : 4
		}),
		renderer : function(value) {
			return numberFormat(value);
		}
	}, {
		header : '本年预算减少',
		anchor : '80%',
		css : CSS_GRID_INPUT_COL,
		dataIndex : 'reduceAsset',
		align : 'left',
		editor : new Ext.form.NumberField({
			allowBlank : false,
			allowDecimal : true,
			allowNegative : false,
			decimalPrecision : 4
		}),
		renderer : function(value) {
			return numberFormat(value);
		}
	}, {
		header : '本年年末资产',
		anchor : '80%',
		dataIndex : 'newAssetCount',
		align : 'left',
		renderer : function(value) {
			if (value == null) {
				return 0.00;
			} else {
				return numberFormat(value);
			}
		}
	}, {
		header : '年折旧率',
		anchor : '80%',
		dataIndex : 'depreciationRate',
		align : 'left',
		css : CSS_GRID_INPUT_COL,
		editor : new Ext.form.NumberField({
			allowBlank : false,
			allowDecimal : true,
			allowNegative : false,
			decimalPrecision : 4
		}),
		renderer : function(value) {
			return percentFormat(value);
		}
	}, {
		header : '年折旧预算数',
		anchor : '80%',
		dataIndex : 'depreciationNumber',
		css : CSS_GRID_INPUT_COL,
		align : 'left',
		editor : new Ext.form.NumberField({
			allowBlank : false,
			allowDecimal : true,
			allowNegative : false,
			decimalPrecision : 4
		}),
		renderer : function(value) {
			return numberFormat(value);
		}
	}, {
		header : '累计折旧额',
		css : CSS_GRID_INPUT_COL,
		anchor : '80%',
		dataIndex : 'depreciationSum',
		align : 'left',
		editor : new Ext.form.NumberField({}),
		renderer : function(value) {
			return numberFormat(value);
		}
	}, {
		header : '备注',
		anchor : '90%',
		dataIndex : 'memo',
		css : CSS_GRID_INPUT_COL,
		align : 'left',
		editor : new Ext.form.TextArea({})
	}, {
		header : '工作流编号',
		anchor : '85%',
		hidden : true,
		dataIndex : 'workFlowNo',
		align : 'left'
	}, {
		header : '工作流状态',
		width : 40,
		hidden : true,
		dataIndex : 'workFlowStatus',
		align : 'left'
	}, {
		header : '时间',
		width : 40,
		hidden : true,
		dataIndex : 'budgetTime',
		align : 'left'
	}]);
	var gridbbar = new Ext.PagingToolbar({
		pageSize : 18,
		store : ds,
		displayInfo : true,
		displayMsg : "显示第{0}条到{1}条，共{2}条",
		emptyMsg : "没有记录"
	});
	// 增加
	function addDetail() {
		if (ds.getTotalCount() - 1 > 0) {
			if (ds.getAt(0).get('budgetTime') != year.getValue()) {
				Ext.Msg.alert('提示信息', '请先查询数据！');
				return;
			}
			if (ds.getAt(0).get('workFlowStatus') == '1'
					|| ds.getAt(0).get('workFlowStatus') == '2') {
				Ext.Msg.alert('提示信息', '审批中和审批通过的数据不允许增加！')
				return;
			}
		}
		var count = ds.getCount();
		var currentIndex = count;
		var o;
		if (count == 0) {
			o = new item({
				'depreciationDetailId' : null,
				'depreciationId' : null,
				'assetName' : null,
				'lastAsset' : 0,
				'addAsset' : 0,
				'reduceAsset' : 0,
				'newAsset' : 0,
				'depreciationRate' : 0,
				'depreciationNumber' : 0,
				'depreciationSum' : 0,
				'memo' : null,
				'workFlowNo' : null,
				'workFlowStatus' : null,
				'budgetTime' : year.getValue()
			});
		} else {
			o = new item({
				'depreciationDetailId' : null,
				'depreciationId' : ds.getAt(0).get('depreciationId'),
				'assetName' : null,
				'lastAsset' : 0,
				'addAsset' : 0,
				'reduceAsset' : 0,
				'newAsset' : 0,
				'depreciationRate' : 0,
				'depreciationNumber' : 0,
				'depreciationSum' : 0,
				'memo' : null,
				'workFlowNo' : null,
				'workFlowStatus' : null,
				'budgetTime' : year.getValue()
			});
		}
		Grid.stopEditing();
		ds.insert(currentIndex - 1, o);
		Grid.getView().refresh();
		sm.selectRow(currentIndex - 1);
		Grid.startEditing(currentIndex - 1, 1);
	}

	// 删除记录
	var ids = new Array();
	function deleteDetail() {
		if (ds.getTotalCount() - 1 > 0) {
			if (ds.getAt(0).get('budgetTime') != year.getValue()) {
				Ext.Msg.alert('提示信息', '请先查询数据！');
				return;
			}

			if (ds.getAt(0).get('workFlowStatus') == '1'
					|| ds.getAt(0).get('workFlowStatus') == '2') {
				Ext.Msg.alert('提示信息', '审批中和审批通过的数据不允许删除！')
				return;
			}
		}
		Grid.stopEditing();
		var sm = Grid.getSelectionModel();
		var selected = sm.getSelections();
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i];
				if (member.get('assetName') == '合计') {
					Ext.Msg.alert("提示", "合计列不能删除！");
					return;
				}
				if (member.get("depreciationDetailId") != null) {
					ids.push(member.get("depreciationDetailId"));

					Grid.getStore().remove(member);
					Grid.getStore().getModifiedRecords().remove(member);
				}
			}
		}
	}
	// 保存
	function saveDetail() {
		if (ds.getTotalCount() - 1 > 0) {
			if (ds.getAt(0).get('budgetTime') != year.getValue()) {
				Ext.Msg.alert('提示信息', '请先查询数据！');
				return;
			}

			if (ds.getAt(0).get('workFlowStatus') == '1'
					|| ds.getAt(0).get('workFlowStatus') == '2') {
				Ext.Msg.alert('提示信息', '审批中和审批通过的数据不允许保存修改！')
				return;
			}
		}
		Grid.stopEditing();
		var modifyRec = Grid.getStore().getModifiedRecords();
		if (modifyRec.length > 0 || ids.length > 0) {
			for (var i = 0; i < modifyRec.length; i++) {
				if (modifyRec[i].data.assetName == null
						|| modifyRec[i].data.assetName == "") {
					Ext.Msg.alert('提示', '资产名称不可为空，请输入！')
					return;
				}
				
//				if (modifyRec[i].data.lastAsset == null
//						|| modifyRec[i].data.lastAsset == "") {
//					Ext.Msg.alert('提示', '去年年末资产不可为空，请输入！')
//					return;
//				}
//				if (modifyRec[i].data.addAsset == null
//						|| modifyRec[i].data.addAsset == "") {
//					Ext.Msg.alert('提示', '本年预算增加不可为空，请输入！')
//					return;
//				}
//				if (modifyRec[i].data.reduceAsset == null
//						|| modifyRec[i].data.reduceAsset == "") {
//					Ext.Msg.alert('提示', '本年预算减少不可为空，请输入！')
//					return;
//				}
//				if (modifyRec[i].data.depreciationRate == null
//						|| modifyRec[i].data.depreciationRate == "") {
//					Ext.Msg.alert('提示', '年折旧率不可为空，请输入！')
//					return;
//				}
//				if (modifyRec[i].data.depreciationNumber == null
//						|| modifyRec[i].data.depreciationNumber == "") {
//					Ext.Msg.alert('提示', '年折旧预算数不可为空，请输入！')
//					return;
//				}
//				if (modifyRec[i].data.depreciationSum == null
//						|| modifyRec[i].data.depreciationSum == "") {
//					Ext.Msg.alert('提示', '累计折旧额不可为空，请输入！')
//					return;
//				}
			}
			
			Ext.Msg.confirm('提示信息', '确定要保存修改吗？', function(button) {
				if (button == 'yes') {
					var addData = new Array();
					var updateData = new Array();
					for (var i = 0; i < modifyRec.length; i++) {
						if (modifyRec[i].get('isUse') == 'Y') {
							updateData.push(modifyRec[i].data);
						} else if (modifyRec[i].get('isUse') != 'Y'
								&& modifyRec[i].get('assetName') != '合计') {
							addData.push(modifyRec[i].data);
						}
					}
					Ext.Ajax.request({
						url : 'managebudget/saveDepreciationRecord.action',
						method : 'post',
						params : {
							isAdd : Ext.util.JSON.encode(addData),
							isUpdate : Ext.util.JSON.encode(updateData),
							ids : ids.join(",")
						},
						success : function(result, request) {
							Ext.Msg.alert("提示信息", "数据保存修改成功！");
							ds.rejectChanges();
							ids = [];
							ds.reload();
						},
						failure : function(result, request) {
							Ext.Msg.alert("提示信息", "数据保存修改失败！");
							ds.rejectChanges();
							ids = [];
							ds.reload();
						}
					})
				}
			})

		} else {
			Ext.MessageBox.alert('提示信息', '没有做任何修改！')
		}
	}
	// 取消
	function cancerDetail() {
		var modifyRec = ds.getModifiedRecords();
		if (modifyRec.length > 0 || ids.length > 0) {
			Ext.Msg.confirm('提示信息', '确定要放弃修改吗？', function(button) {
				if (button == 'yes') {
					ds.reload();
					ds.rejectChanges();
					ids = [];
				}
			})
		} else
			ds.reload()
	}

	function fuzzyQuery() {
		ds.baseParams = {
			budgetTime : year.getValue()
		};
		ds.load({
			params : {
				start : 0,
				limit : 18
			}
		});
		ids = [];
	};

	// tbar
	var contbar = new Ext.Toolbar({
		items : ['年度:', year, '-', {
			id : 'query',
			iconCls : 'query',
			text : "查询",
			handler : fuzzyQuery
		}, '-', {
			id : 'btnAdd',
			iconCls : 'add',
			text : "新增",
			handler : addDetail
		}, '-', {
			id : 'btnDelete',
			iconCls : 'delete',
			text : "删除",
			handler : deleteDetail
		}, '-', {
			id : 'btnCancer',
			iconCls : 'cancer',
			text : "取消",
			handler : cancerDetail
		}, '-', {
			id : 'btnSave',
			iconCls : 'save',
			text : "保存",
			handler : saveDetail
		}, '-', {
			text : '上报',
			iconCls : 'upcommit',
			handler : reportRec
		}, '-', {
			text : '审批查询',
			iconCls : 'view',
			handler : approveQuery
		}]
	});
	var Grid = new Ext.grid.EditorGridPanel({
		sm : sm,
		ds : ds,
		cm : cm,
		autoScroll : true,
		bbar : gridbbar,
		tbar : contbar,
		border : true,
		clicksToEdit : 1,
		viewConfig : {
		// forceFit : true
		}
	});

	Grid.on('beforeedit', function(obj) {
		if (obj.row == ds.getCount() - 1) {
			return false;
		}
		if (obj.record.get('workFlowStatus') == 1
						|| obj.record.get('workFlowStatus') == 2) {
					return false;
				}
		return true;
	});

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		border : false,
		frame : false,
		items : [{
			layout : 'fit',
			border : false,
			frame : false,
			region : "center",
			items : [Grid]
		}]
	});

	function reportRec() {
		if (ds.getTotalCount() - 1 > 0) {
			if (ds.getAt(0).get('budgetTime') != year.getValue()) {
				Ext.Msg.alert('提示信息', '请先查询数据！');
				return;
			}
			if (ds.getAt(0).get('workFlowStatus') == '1'
					|| ds.getAt(0).get('workFlowStatus') == '2') {
				Ext.Msg.alert('提示信息', '审批中和审批通过的数据不允许上报！')
				return;
			}
			var url = "../depreciationApprove/depreciationReport.jsp";
			var arg = new Object();
			arg.entryId = ds.getAt(0).get('workFlowNo');
			arg.workflowType = "budgetDetailApprove";
			arg.depreciationId = ds.getAt(0).get('depreciationId');
			var obj = window.showModalDialog(url, arg,
					'status:no;dialogWidth=750px;dialogHeight=550px');
			if (obj) {
				fuzzyQuery();
			}
		} else {
			Ext.Msg.alert('提示信息', '无数据可上报！');
			return;
		}

	}
  
	function approveQuery() {
		if (ds.getTotalCount() - 1 > 0) {
			if (ds.getAt(0).get('budgetTime') != year.getValue()) {
				Ext.Msg.alert('提示信息', '请先查询数据！');
				return;
			}
			var entryId = ds.getAt(0).get('workFlowNo');
			if (entryId == null || entryId == "") {
				Ext.Msg.alert("提示", "无审批信息！");
				return;
			}
			var url = "../../../../workflow/manager/show/show.jsp?entryId="
					+ entryId;
			window.open(url);

		} else {
			Ext.Msg.alert('提示信息', '无数据进行审批查询！');
			return;
		}
	}

	fuzzyQuery();
})