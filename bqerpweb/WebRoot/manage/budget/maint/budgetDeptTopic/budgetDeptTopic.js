Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	var flagCenterId = '';

	var westsm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true
	});
	// 左边列表中的数据
	var westdatalist = new Ext.data.Record.create([{
		name : 'centerId'
	}, {
		name : 'depCode'
	}, {
		name : 'depName'
	}, {
		name : 'manager'
	}, {
		name : 'ifDuty'
	}, {
		name : 'manageName'
	}]);

	var westgrids = new Ext.data.JsonStore({
		url : 'managebudget/findBudgetDeptList.action',
		root : 'list',
		totalProperty : 'totalCount',
		fields : westdatalist
	});

	westgrids.load({
		params : {
			start : 0,
			limit : 18
		}
	});

	// 左边列表
	var westgrid = new Ext.grid.GridPanel({
		title : '预算部门列表',
		width : 200,
		split : true,
		autoScroll : true,
		ds : westgrids,
		columns : [westsm, new Ext.grid.RowNumberer({
			header : "行号",
			width : 31
		}), {
			header : "责任中心ID",
			width : 110,
			hidden : true,
			align : "center",
			sortable : true,
			dataIndex : 'centerId'
		}, {
			header : "部门编码",
			width : 100,
			align : "center",
			sortable : true,
			dataIndex : 'depCode'
		}, {
			header : "部门名称",
			width : 180,
			align : "center",
			sortable : true,
			dataIndex : 'depName'
		}],
		sm : westsm,
		// frame : true,
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : westgrids,
			displayInfo : true,
			displayMsg : "共 {2} 条",
			emptyMsg : "没有记录"
		}),
		border : true
	});

	westsm.on('rowselect', function() {
		if (westsm.hasSelection()) {
			flagCenterId = westsm.getSelected().get('centerId');
			eastgrids.load({
				params : {
					start : 0,
					limit : 18,
					centerId : westsm.getSelected().get('centerId')
				}
			})
		}
	});

	var directManageName = new Ext.form.TextField({
		id : 'directManageName',
		fieldLabel : '直接负责人',
		readOnly : true,
		allowBlank : false,
		anchor : "100%",
		name : 'directManageName',
		listeners : {
			focus : selectPersonWin
		}
	});

	var eastsm = new Ext.grid.CheckboxSelectionModel();
	// 右边列表中的数据
	var datalist = new Ext.data.Record.create([{
		name : 'top.centerTopicId'
	}, {
		name : 'top.centerId'
	}, {
		name : 'top.topicId'
	}, {
		name : 'deptName'
	}, {
		name : 'topicName'
	}, {
		name : 'top.directManager'
	}, {
		name : 'directManageName'
	}, {
		name : 'topicCode'
	}, {
		name : 'deptCode'
	}]);
	var eastgrids = new Ext.data.JsonStore({
		url : 'managebudget/findDeptTopicByCode.action ',
		root : 'list',
		totalProperty : 'totalCount',
		fields : datalist
	});
	var contbar = new Ext.Toolbar({
		items : [{
			id : 'btnAdd',
			iconCls : 'add',
			text : "新增",
			handler : addRec
		}, {
			id : 'btnDelete',
			iconCls : 'delete',
			text : "删除",
			handler : deleteRec

		}, {
			id : 'btnCancer',
			iconCls : 'cancer',
			text : "取消",
			handler : cancerRec

		}, '-', {
			id : 'btnSave',
			iconCls : 'save',
			text : "保存修改",
			handler : saveRec
		}]

	});

	// 右边列表
	var eastgrid = new Ext.grid.EditorGridPanel({
		tbar : contbar,
		autoScroll : true,
		ds : eastgrids,
		layout : 'fit',
		columns : [eastsm, new Ext.grid.RowNumberer({
			header : "行号",
			width : 31
		}), {
			header : "ID",
			sortable : false,
			hidden : true,
			dataIndex : 'top.centerTopicId'
		}, {
			header : "部门ID",
			align : "center",
			sortable : true,
			hidden : true,
			dataIndex : 'top.centerId'
		}, {
			header : "主题ID",
			align : "center",
			sortable : true,
			hidden : true,
			dataIndex : 'top.topicId'
		}, {
			header : "主题编码",
			align : "center",
			sortable : true,
			dataIndex : 'topicCode'
		}, {
			header : "主题名称",
			align : "center",
			sortable : true,
			dataIndex : 'topicName'
		}, {
			header : "直接负责人编码",
			align : "center",
			sortable : true,
			hidden : true,
			dataIndex : 'top.directManager'
		}, {
			header : "直接负责人",
			align : "center",
			sortable : true,
			css : CSS_GRID_INPUT_COL,
			dataIndex : 'directManageName',
			editor : new Ext.form.TriggerField({
				width : 320,
				allowBlank : false,
				onTriggerClick : selectPersonWin,
				listeners : {
					render : function(f) {
						f.el.on('keyup', function(e) {
							eastgrid.getSelectionModel().getSelected().set(
									"top.directManager", 'temp');
						});
					}
				}
			})
		}],
		forceFit : true,
		sm : eastsm,
		// frame : true,
		width : Ext.getBody().getViewSize().width,
		border : true,
		enableColumnHide : false,
		iconCls : 'icon-grid',
		clicksToEdit : 1,
		autoSizeColumns : true
	});

	eastgrid.on('celldblclick', choseEdit);
	function choseEdit(grid, rowIndex, columnIndex, e) {

		if (rowIndex <= grid.getStore().getCount() - 1) {

			var record = grid.getStore().getAt(rowIndex);
			var fieldName = grid.getColumnModel().getDataIndex(columnIndex);
			if (fieldName == "topicCode") {
				var rec = window.showModalDialog('../themeSelect.jsp', window,
						'dialogWidth=800px;dialogHeight=550px;status=no');
				if (typeof(rec) != "undefined") {
					record.set('top.topicId', rec.topicId);
					record.set('topicCode', rec.topicCode);
					record.set('topicName', rec.topicName);
				}
			}
		}
	}
	// 增加
	function addRec() {
		if (!westsm.hasSelection()) {
			Ext.Msg.alert('提示信息', '请先选择左边的一条记录！');
			return;
		}
		var count = eastgrids.getCount();
		var currentIndex = count;
		var o = new datalist({
			'top.centerId' : flagCenterId,
			'top.topicId' : '',
			'topicCode' : '',
			'topicName' : '',
			'directManageName' : ''
		});
		eastgrid.stopEditing();
		eastgrids.insert(currentIndex, o);
		eastsm.selectRow(currentIndex);
		eastgrid.startEditing(currentIndex, 1);
	}

	// 删除记录
	var ids = new Array();
	function deleteRec() {
		eastgrid.stopEditing();
		var sm = eastgrid.getSelectionModel();
		var selected = sm.getSelections();
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i];
				if (member.get("top.centerTopicId") != null) {
					ids.push(member.get("top.centerTopicId"));
				}
				eastgrid.getStore().remove(member);
				eastgrid.getStore().getModifiedRecords().remove(member);
			}
		}
	}
	// 保存
	var centerId;
	function saveRec() {
		eastgrid.stopEditing();
		var modifyRec = eastgrid.getStore().getModifiedRecords();
		if (modifyRec.length > 0 || ids.length > 0) {
			for (var i = 0; i < eastgrids.getCount() - 1; i++) {
				for (var j = i + 1; j <= eastgrids.getCount() - 1; j++) {
					if (eastgrids.getAt(i).get('top.centerId') == eastgrids
							.getAt(j).get('top.centerId')) {
						if (eastgrids.getAt(i).get('top.topicId') == eastgrids
								.getAt(j).get('top.topicId')) {
							Ext.Msg.alert('提示信息', '同一部门内主题不可重复！')
							return;
						}
					}
				}
			}
			if (!confirm("确定要保存修改吗?"))
				return;
			var updateData = new Array();
			for (var i = 0; i < modifyRec.length; i++) {
				if (modifyRec[i].data.topicCode == null
						|| modifyRec[i].data.topicCode == "") {
					Ext.Msg.alert('提示信息', '主题编码不可为空，请选择！')
					return;
				}
				updateData.push(modifyRec[i].data);
			}
			Ext.Ajax.request({
				url : 'managebudget/addAndUpdateDeptTopic.action',
				method : 'post',
				params : {
					isUpdate : Ext.util.JSON.encode(updateData),
					isDelete : ids.join(","),
					isCode : centerId
				},
				success : function(result, request) {
					var o = eval('(' + result.responseText + ')');
					Ext.MessageBox.alert('提示信息', o.msg);
					eastgrids.rejectChanges();
					ids = [];
					eastgrids.reload();
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
	function cancerRec() {
		var modifyRec = eastgrids.getModifiedRecords();
		if (modifyRec.length > 0 || ids.length > 0) {
			if (!confirm("确定要放弃修改吗"))
				return;
			eastgrids.reload();
			eastgrids.rejectChanges();
			ids = [];
		} else {
			eastgrids.reload();
			eastgrids.rejectChanges();
			ids = [];
		}
	}

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		border : false,
		frame : false,
		items : [{
			bodyStyle : "padding: 2,2,2,2",
			layout : 'fit',
			border : false,
			frame : false,
			region : "west",
			width : '35%',
			items : [westgrid]
		}, {
			bodyStyle : "padding: 2,2,2,2",
			region : "center",
			border : false,
			autoScroll : true,
			frame : false,
			layout : 'fit',
			items : [eastgrid]
		}]
	});

	// 选择人员窗口
	function selectPersonWin() {
		var args = {
			selectModel : 'signal',
			notIn : "",
			rootNode : {
				id : '-1',
				text : '合肥电厂'
			}
		}
		var person = window
				.showModalDialog(
						'../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp',
						args,
						'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (typeof(person) != "undefined") {
			var record = eastgrid.selModel.getSelected()
			var names = person.workerName.split(",");
			var codes = person.workerCode.split(",");

			record.set("directManageName", names[0]);
			record.set("top.directManager", codes[0]);
			eastgrid.getView().refresh();
		}
	}
});