Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.useShims = true;
var createType = "";
/* 根据危险点ID设置Radio的选中 */
function setRadioByDangerId(dangerId) {
	var radios = document.getElementsByName("selectOne");
	for (var i = 0; i < radios.length; i++) {
		if (radios[i].value == dangerId) {
			radios[i].checked = true;
		}
	}
}
Ext.onReady(function() {

	// 工作票编号
	var strWorkticketNo = getParameter("workticketNo");
	// 工作票类型编号
	var strWorkticketType = getParameter("workticketType");
	var deleteDangerControl = [];
	var MyRecord = Ext.data.Record.create([{
		name : 'dangerId'
	}, {
		name : 'dangerName'
	}, {
		name : 'dangerMeasure'
	}, {
		name : 'orderBy'
	}, , {
		name : 'isRunadd'
	}]);
	var store = new Ext.data.GroupingStore({
		proxy : new Ext.data.HttpProxy({
			url : 'workticket/findDangerListForRegister.action'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalCount',
			root : 'list'
		}, MyRecord),
		groupField : 'dangerName',
		sortInfo : '',
		remoteGroup : true
			// groupField : 'dangerId',
			// groupOnSort : true,
			// sortInfo : {
			// field : 'dangerName',
			// direction : "ASC"
			// }
	});
	store.load({
		params : {
			workticketNo : strWorkticketNo
		}
	});

	// 底部工具条
	var bbar = new Ext.Toolbar({
			// items : [new Ext.Toolbar.Fill(), {
			// id : 'returnbtn',
			// iconCls:'priorStep',
			// text : '上一步',
			// handler : function() {
			// var tabPanel = parent.Ext.getCmp('tabPanel');
			// tabPanel.setActiveTab('tabSafety');
			// }
			// }]
	});

	var buttonBar = new Ext.Toolbar({
		items : ['危险点及控制措施  ', {
			text : '新增',
			id : "btnGridAdd",
			iconCls : 'add',
			handler : adddanger
		}, '-', {
			text : '修改',
			id : "btnGridUpdate",
			iconCls : 'update',
			handler : modifyDanger
		}, '-', {
			text : '删除危险点',
			id : "btnGridDelete",
			iconCls : 'delete',
			handler : function() {
				var dangerId = getDangerId();
				if (dangerId != null) {
					dangerdelete(dangerId);
				} else {
					Ext.Msg.alert("提示", "您没有选择任何危险点!");
				}
			}
		}]
	});
	function modifyDanger() {
		var dangerId = getDangerId();
		if (dangerId != null) {
			var dangerRec = getDangerData(dangerId);

			// if(dangerRec.get("isRunadd")=="N")
			// {
			// //非运行时补充的危险点不能修改
			// Ext.Msg.alert("提示","非运行时补充的危险点不能修改!");
			// return;
			// }
			// 由标准票生成的不能修改
			if (createType == "Y") {
				return false;
			}
			controlStore.load({
				params : {
					dangerId : dangerId
				}
			});
			Ext.getCmp("win_controlWin").show();
			controlWin.ifReflesh = false;
			Ext.get("dangerContentId").dom.value = dangerId;
			Ext.get("dangerName").dom.value = dangerRec.get("dangerName");
			Ext.get("orderBy").dom.value = dangerRec.get("orderBy");
		} else {
			Ext.Msg.alert("提示", "您没有选择任何危险点!");
		}
	}
	/* 删除危险点 */
	function dangerdelete(dangerId) {
		// 由标准票生成的不能删除
		if (createType == "Y") {
			return false;
		}
		var dangerRec = getDangerData(dangerId);
		if (dangerRec.get("isRunadd") == "N") {
			// 非运行时补充的危险点不能修改
			Ext.Msg.alert("提示", "非运行时补充的危险点不能删除!");
			return;
		}
		Ext.Msg.confirm("删除", "是否确定删除此危险点及其控制措施吗？", function(buttonobj) {
			if (buttonobj == "yes") {
				Ext.lib.Ajax.request('POST',
						'workticket/deleteDangerForRegister.action', {
							success : function(action) {
								Ext.Msg.alert("提示", "删除成功！")
								store.reload();

							},
							failure : function() {
								Ext.Msg.alert('错误', '删除时出现未知错误.');
							}
						}, 'ids=' + dangerId);
			}
		});

		return false;
	};
	/* 取得选中的危险点ID */
	function getDangerId() {
		var radios = document.getElementsByName("selectOne");
		for (var i = 0; i < radios.length; i++) {
			if (radios[i].checked) {
				return radios[i].value;
			}
		}
		return null;
	};

	/* 取得选中的危险点信息 */
	function getDangerData(dangerId) {
		for (var i = 0; i < store.getCount(); i++) {
			var rec = store.getAt(i);
			if (rec.get("dangerId") == dangerId) {
				return rec;
			}
		}
		return null;
	}
	var sm = new Ext.grid.RowSelectionModel({
		singleSelect : true,
		listeners : {
			rowselect : function(obj, rowIndex, rec) {
				setRadioByDangerId(rec.get("dangerId"));
			}
		}
	});
	/* 显示的grid */
	var grid = new Ext.grid.GridPanel({
		sm : sm,
		store : store,
		columns : [new Ext.grid.RowNumberer(), {
			dataIndex : 'dangerId',
			hidden : true
		}, {
			header : '危险点',
			width : 280,
			dataIndex : 'dangerName',
			hidden : true
		}, {
			header : "控制措施",
			width : 450,
			dataIndex : 'dangerMeasure',
			renderer : function(v) {
				if (v == null)
					v = "";
				return '<span style="white-space:normal;">' + v + '</span>';
			}
		}, {
			header : "排序",
			width : 450,
			dataIndex : 'orderBy',
			hidden : true
		}],
		tbar : buttonBar,
		bbar : bbar,
		view : new Ext.grid.GroupingView({
			forceFit : false,
			enableGroupingMenu : false,
			hideGroupedColumn : true,
			showGroupName : false,
			autoFill : true,
			startCollapsed : false,
			toggleGroup : function(group, expanded) {
				this.grid.stopEditing();
				group = Ext.getDom(group);
				var clickDangerId = group.getElementsByTagName("input")[0].value;
				if (getDangerId() == clickDangerId) {
					expanded = true;
				}
				var gel = Ext.fly(group);
				expanded = expanded !== undefined ? expanded : gel
						.hasClass('x-grid-group-collapsed');
				this.state[gel.dom.id] = expanded;
				gel[expanded ? 'removeClass' : 'addClass']('x-grid-group-collapsed');
			},
			groupTextTpl : '<input type="radio"  value="{[values.rs[0].data.dangerId]}" name="selectOne"></input><span onclick="setRadioByDangerId({[values.rs[0].data.dangerId]});">{[values.rs[0].data.dangerName]}</span>'
		})
	});
	store.on('load', function() {
		var radios = document.getElementsByName("selectOne");
		if (radios) {
			// modify by fyyang 090318
			if (radios[0] != null) {
				radios[0].checked = true;
			}
		}
	});
	grid.on("rowdblclick", modifyDanger);
	/* 增加危险点 */
	function adddanger() {
		controlStore.rejectChanges();
		deleteDangerControl = [];
		controlStore.removeAll();
		dangerpanel.getForm().reset();
		controlWin.show();
		controlWin.ifReflesh = false;
	}

	// ----------控制措施grid------------

	var controlMyRecord = Ext.data.Record.create([{
		name : 'dangerContentId'
	}, {
		name : 'dangerName'
	}, {
		name : 'orderBy'
	}]);
	var controlStore = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
					url : 'workticket/findDangerControlList.action'
				}),
		reader : new Ext.data.JsonReader({
			root : "list",
			totalProperty : "totalCount"

		}, controlMyRecord)

	});
	var controlSm = new Ext.grid.CheckboxSelectionModel();
	/* grid按钮操作 */
	var GridManager = {
		addRecord : function() {
			var currentRecord = controlGrid.getSelectionModel().getSelected();
			var count = controlStore.getCount();
			var currentIndex = currentRecord
					? currentRecord.get("orderBy") - 1
					: count;
			var m = new controlMyRecord({
				dangerContentId : "",
				dangerName : "",
				orderBy : ""
			});
			controlGrid.stopEditing();
			controlStore.insert(currentIndex, m);
			controlSm.selectRow(currentIndex);
			controlGrid.startEditing(currentIndex, 2);
			resetLine();
		},
		refleshGrid : function() {
			if (deleteDangerControl.length > 0
					|| controlStore.getModifiedRecords().length > 0) {
				if (confirm("确定要放弃所做的修改吗?")) {
					var myContentId = "";
					if (Ext.get("dangerContentId").dom.value == "自动生成") {
						myContentId = -1;
					} else {
						myContentId = Ext.get("dangerContentId").dom.value;
					}
					controlStore.load({
						params : {
							dangerId : myContentId
						}
					});
					// controlStore.reload();
					deleteDangerControl = [];
					controlStore.rejectChanges();
				}
			}
		},
		deleteRecord : function() {
			var sm = controlGrid.getSelectionModel();
			var selected = sm.getSelections();
			if (selected.length == 0) {
				Ext.Msg.alert("提示", "请选择要删除的记录！");
			} else {
				for (var i = 0; i < selected.length; i += 1) {
					var member = selected[i];
					if (member.get("dangerContentId") != "") {
						deleteDangerControl.push(member.get("dangerContentId"));
					}
					controlGrid.getStore().remove(member);
					controlGrid.getStore().getModifiedRecords().remove(member);
				}
				resetLine();
			}
		}
	};
	/* grid按钮 */
	var controlTbar = new Ext.Toolbar({
		items : ['危险点控制措施  ', {
			text : '新增',
			iconCls : 'add',
			handler : function() {
				GridManager.addRecord();
			}
		}, {
			text : '删除',
			iconCls : 'delete',
			handler : function() {
				GridManager.deleteRecord();
			}
		}, {
			text : '刷新',
			iconCls : 'reflesh',
			handler : function() {
				GridManager.refleshGrid();
			}
		}]
	});
	var controlGrid = new Ext.grid.EditorGridPanel({
		store : controlStore,
		clicksToEdit : 1,
		sm : controlSm,
		tbar : controlTbar,
		viewConfig : {
			forceFit : true
		},
		columns : [controlSm, new Ext.grid.RowNumberer({
			header : '序号',
			width : 35,
			height : 100,
			align : 'center'
		}), {
			header : "控制措施",
			width : 300,
			sortable : true,
			dataIndex : 'dangerName',
			editor : new Ext.form.TextField({
				allowBlank : false,
				align : 'left'
			}),
			renderer : function(v) {
				if (v == null)
					v = "";
				return '<span style="white-space:normal;">' + v + '</span>';
			}
		}, {
			header : "排序",
			width : 35,
			sortable : true,
			dataIndex : 'orderBy',
			align : 'center'
		}]
	});
	// 右键菜单
	controlGrid.on("rowcontextmenu", function(g, i, e) {
		e.stopEvent();
		var _store = controlGrid.getStore();
		var menu = new Ext.menu.Menu({
			id : 'fuctionMenu',
			items : [new Ext.menu.Item({
				text : '移至顶行',
				iconCls : 'priorStep',
				handler : function() {
					if (i != 0) {
						var record = _store.getAt(i);
						_store.remove(record);
						_store.insert(0, record);
						resetLine();
					}
				}
			}), new Ext.menu.Item({
				text : '上移',
				iconCls : 'priorStep',
				handler : function() {
					if (i != 0) {
						var record = _store.getAt(i);
						_store.remove(record);
						_store.insert(i - 1, record);
						resetLine();
					}
				}
			}), new Ext.menu.Item({
				text : '下移',
				iconCls : 'nextStep',
				handler : function() {
					if ((i + 1) != _store.getCount()) {
						var record = _store.getAt(i);
						_store.remove(record);
						_store.insert(i + 1, record);
						resetLine();
					}
				}
			}), new Ext.menu.Item({
				text : '移至最后',
				iconCls : 'nextStep',
				handler : function() {
					if ((i + 1) != _store.getCount()) {
						var record = _store.getAt(i);
						_store.remove(record);
						_store.insert(_store.getCount(), record);
						resetLine();
					}
				}
			})]
		});
		var coords = e.getXY();
		menu.showAt([coords[0], coords[1]]);
	});
	// 重置排序字段
	function resetLine() {
		for (var j = 0; j < controlStore.getCount(); j++) {
			var temp = controlStore.getAt(j);
			temp.set("orderBy", j + 1);
		}
	}
	// 危险点保存
	var dangerContentId = {
		id : "dangerContentId",
		xtype : "textfield",
		fieldLabel : '编号',
		value : '自动生成',
		readOnly : true,
		name : 'dangerContentId',
		anchor : "90%"
	};
	var dangerName = {
		id : "dangerName",
		xtype : "textarea",
		fieldLabel : '危险点名称',
		height : 45,
		allowBlank : false,
		name : 'danger.dangerName',
		anchor : "95%"
	};
	var orderBy = {
		id : "orderBy",
		xtype : "numberfield",
		fieldLabel : '排序',
		name : 'danger.orderBy',
		anchor : "90%"
	}
	var dangerpanel = new Ext.FormPanel({
		id : 'dangerpanel',
		frame : true,
		labelAlign : 'left',
		labelWidth : 70,
		bodyStyle : 'padding:0px',
		height : 120,
		items : [{
			layout : 'column',
			items : [{
				columnWidth : 0.5,
				layout : 'form',
				items : [dangerContentId]
			}, {
				columnWidth : 0.5,
				layout : 'form',
				items : [orderBy]
			}]
		}, dangerName],
		buttons : [{
			text : '保存',
			iconCls : 'save',
			handler : savedanger
		}, {
			text : '关闭',
			iconCls : 'cancer',
			handler : function() {
				// 尚未保存
				if (deleteDangerControl.length > 0
						|| controlStore.getModifiedRecords().length > 0) {
					if (confirm("您所做的修改尚未保存,确定放弃修改吗?")) {
						deleteDangerControl = [];
						controlStore.rejectChanges();
						controlWin.hide();
					}
				} else {
					controlWin.hide();
				}
				if (controlWin.ifReflesh) {
					store.reload();
				}

			}
		}]
	});

	var right = new Ext.Panel({
		region : "center",
		autoScroll : true,
		containerScroll : true,
		layout : 'border',
		items : [{
			region : 'north',
			height : 120,
			items : [dangerpanel]
		}, {
			region : 'center',
			layout : 'fit',
			items : [controlGrid]
		}]

	});
	Ext.Window.prototype.ifReflesh = false;
	var controlWin = new Ext.Window({
		id : 'win_controlWin',
		modal : true,
		width : 600,
		height : 400,
		closable : false,
		buttonAlign : "center",
		items : [right],
		layout : 'fit',
		closeAction : "hide",
		title : '危险点信息'
	});

	// 增加/修改危险点基本信息
	function savedanger() {
		if (!dangerpanel.getForm().isValid()) {
			return false;
		}
		var myurl = "workticket/saveDangerForRegister.action?isRun=Y"
		dangerpanel.getForm().submit({
			method : 'POST',
			waitMsg : '正在保存危险点信息...',
			params : {
				workticketNo : strWorkticketNo
			},
			url : myurl,
			success : function(form, action) {
				controlWin.ifReflesh = true;
				var o = eval("(" + action.response.responseText + ")");
				Ext.get("dangerContentId").dom.value = o.id;
				saveRecord();
			},
			faliue : function() {
				Ext.Msg.alert('错误', '操作失败!');
			}
		});
	};
	/* 保存grid的修改信息 */
	function saveRecord() {
		Ext.Msg.wait("正在保存控制措施...");
		var changes = [];
		var record = controlGrid.getStore().getModifiedRecords();
		if (record.length > 0 || deleteDangerControl.length > 0) {
			for (var i = 0; i < record.length; i++) {
				var data = record[i];
				var rec = new Object();
				rec.dangerId = data.get("dangerContentId");
				if (data.get("dangerName") == null
						|| data.get("dangerName") == "") {
					Ext.Msg.alert("提示", "控制措施不能为空!");
					return false;
				}
				rec.dangerName = data.get("dangerName");
				rec.orderBy = data.get("orderBy");
				rec.workticketNo = strWorkticketNo;
				rec.isRun = "N";
				rec.pId = Ext.get("dangerContentId").dom.value;
				changes.push(rec);
			}
			Ext.Ajax.request({
				url : 'workticket/saveControlForRegister.action',
				params : {
					data : Ext.util.JSON.encode(changes),
					deleteIds : deleteDangerControl.join(",")
				},
				method : 'post',
				success : function(result, request) {
					Ext.Msg.alert("提示", "保存成功!");
					deleteDangerControl = [];

					controlStore.load({
						params : {
							dangerId : Ext.get("dangerContentId").dom.value
						}
					});
					controlStore.rejectChanges();
				},
				failure : function(result, request) {
					Ext.MessageBox.alert('错误', '操作失败!');
				}
			});
		} else {
			Ext.Msg.alert("提示", "保存成功!");
		}
	};

	// ---------------------------------------

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		layout : "fit",
		items : [grid]
	});

});