Ext.onReady(function() {
	// 分页时每页显示记录条数
	var PAGE_SIZE = Constants.PAGE_SIZE;
	// 系统当天日期
	var startDate = new Date();
	var endDate = new Date();
	// 系统当天前30天的日期
	startDate.setDate(startDate.getDate() - 30);
	// 系统当天后30天的日期
	endDate.setDate(endDate.getDate() + 30);

	// ********** 主画面******* //
	var wd = 100;
	var newOrOldValue = "";
	// 操作票类型检索
	var optiketTypeCbo = new Ext.ux.ComboBoxTree({
		id : 'optiketTypeCbo',
		displayField : 'text',
		width : 100,
		resizable : true,
		valueField : 'id',
		tree : {
			xtype : 'treepanel',
			// 虚拟节点,不能显示
			rootVisible : true,
			loader : new Ext.tree.TreeLoader({
				// dataUrl : 'opticket/getTreeNode.action'
				dataUrl : 'opticket/getDetailOpTaskTree.action'
			}),
			root : new Ext.tree.AsyncTreeNode({
				id : '-1',
				text : '所有操作任务'
			})
		},
		selectNodeModel : 'all'
	});
	var optaskName = new Ext.form.TextField({
		id : 'optaskName',
		name : 'optaskName',
		width : 60
	})
	// 操作票专业
	var storeOpSpecial = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'opticket/getReportOpSpecials.action'
		}),
		reader : new Ext.data.JsonReader({
			root : "list"
		}, [{
			name : 'specialityCode'
		}, {
			name : 'specialityName'
		}])
	});
	var newOrOld = new Ext.Panel({
		hideLabel : true,
		layout : 'column',
		width : 200,
		style : {
			cursor : 'hand'
		},
		height : 20,
		items : [new Ext.form.Radio({
			columnWidth : 0.5,
			checked : true, // 设置当前为选中状态,仅且一个为选中.
			boxLabel : "新系统数据", // Radio标签
			name : "newOrOld", // 用于form提交时传送的参数名
			inputValue : "new", // 提交时传送的参数值
			listeners : {
				check : function(checkbox, checked) { // 选中时,调用的事件
					newOrOldValue = getSelectValue();
					searchOptickets()
				}
			}
		}), new Ext.form.Radio({
			columnWidth : 0.5,
			boxLabel : "老系统数据",
			name : "newOrOld",
			inputValue : "old"
		})]
	});
	storeOpSpecial.on('load', function(e, records) {
		records[0].data.specialityCode = "";
		records[0].data.specialityName = "所有";
		opticketSpecialCbo.setValue(records[0].data.specialityCode);
	});
	storeOpSpecial.load();
	// 操作票专业组合框
	var opticketSpecialCbo = new Ext.form.ComboBox({
		id : "opticketSpecialCbo",
		width:100,
		resizable : true,
		store : storeOpSpecial,
		triggerAction : 'all',
		valueField : "specialityCode",
		displayField : "specialityName",
		mode : 'local',
		selectOnFocus : true,
		readOnly : true
			// ,
			// listeners : {
			// 'select' : function() {
			// searchOptickets();
			// }
			// }
	});

	// grid工具栏
	var gridTbar = new Ext.Toolbar({
		items : ['操作票类型:', optiketTypeCbo, '-', '专业:', opticketSpecialCbo, '-',
				'操作任务：', optaskName, '-', {
					text : Constants.BTN_QUERY,
					id : 'queryBtn',
					iconCls : 'query',
					handler : searchOptickets
				},{
			text : '修改',
			iconCls : 'update',
			handler : function() {
				var sm = rungrid.getSelectionModel();
				var selected = sm.getSelections();
				if (selected.length == 0) {
					Ext.Msg.alert('提示信息', '请选择你要修改的操作票！')
				} else {
					opticketCode = selected[0].get('opticket.opticketCode');
					parent.edit(opticketCode);
				}
			}
		}, '-', {
			text : "操作前检查项目",
			iconCls : 'next',
			handler : checkBef,
			id : 'checkBef'
		}, '-', {
			text : "操作后完成工作",
			handler : workAft,
			iconCls : 'last',
			id : 'workAft'
		} ]
	});
	var view1 = new Ext.Button({
		id : "view1",
		text : "操作票预览",
		iconCls : 'pdfview',
		handler : viewRecord1

	});
	var view2 = new Ext.Button({
		id : "view2",
		iconCls : 'pdfview',
		text : "预览危险点",
		handler : viewRecord2

	});
	var view3 = new Ext.Button({
		id : "view3",
		iconCls : 'pdfview',
		text : "操作前检查项目预览",
		handler : viewRecord3

	});
	var view4 = new Ext.Button({
		id : "view4",
		iconCls : 'pdfview',
		text : "操作后完成任务预览",
		handler : viewRecord4

	});
	var view5 = new Ext.Button({
		id : "view",
		text : "流程展示",
		iconCls : 'view',
		handler : viewRecord

	});
	var tooTbar = new Ext.Toolbar({
		items : [newOrOld, '-', '-', view1, '-', view2, '-', view3, '-', view4, '-', view5]
	});
	var toolPanel = new Ext.Panel({
		layout : 'form',
		items : [gridTbar, tooTbar]
	});
	// 选择列
	var smCSM = new Ext.grid.CheckboxSelectionModel({
		header : "选择",
		id : "check",
		width : 35,
		singleSelect : true,
		listeners : {
			rowselect : function(sm, row, rec) {
				var code = rec.get('opticket.opticketCode');
				parent.currentOpCode = code;
				if (rec.get("opticket.opticketType").substring(0, 2) == "00") {
					Ext.get("checkBef").dom.disabled = false;
					Ext.get("workAft").dom.disabled = false;
					Ext.get("view2").dom.disabled = true;
					Ext.get("view3").dom.disabled = false;
					Ext.get("view4").dom.disabled = false;
				} else {
					Ext.get("workAft").dom.disabled = true;
					Ext.get("checkBef").dom.disabled = true;
					Ext.get("view2").dom.disabled = false;
					Ext.get("view3").dom.disabled = true;
					Ext.get("view4").dom.disabled = true;
				}
			}
		}
	});

	// grid中的数据Record
	var rungridlist = new Ext.data.Record.create([{
		// 编号
		name : 'opticket.opticketCode'
	}, {
		// 操作任务
		name : 'opticket.opticketName'
	}, {
		// 操作票状态
		name : 'opticket.opticketStatus'
	}, {
		// 操作票类别
		name : 'opticket.opticketType'
	}, {
		name : 'createDate'
	}, {
		name : 'specialityName'
	}, {
		name : 'opticket.workFlowNo'
	}, {
		name : 'createrName'
	}]);

	// grid的store
	var queryStore = new Ext.data.JsonStore({
		url : 'opticket/getOpticketList.action?isStandar=Y',
		root : 'list',
		totalProperty : 'totalCount',
		fields : rungridlist
	});

	// 分页工具栏
	var pagebar = new Ext.PagingToolbar({
		pageSize : PAGE_SIZE,
		store : queryStore,
		displayInfo : true,
		displayMsg : Constants.DISPLAY_MSG,
		emptyMsg : Constants.EMPTY_MSG
	});
	function getSelectValue() {
		var ns = document.getElementsByName("newOrOld");
		for (var i = 0; i < ns.length; i++) {
			if (ns[i].checked) {
				return ns[i].value;
			}
		}
	}

	// 页面的Grid主体
	var rungrid = new Ext.grid.GridPanel({
		store : queryStore,
		viewConfig : {
			forceFit : true
		},
		loadMask : {
			msg : '<img src="comm/images/extanim32.gif" width="32" height="32" style="margin-right: 8px;" align="absmiddle" />读取数据中 ...'
		},
		columns : [smCSM, {
			header : "编号",
			sortable : true,
			width : 100,
			dataIndex : 'opticket.opticketCode'
		}, {
			header : "操作任务",
			sortable : true,
			width : 320,
			dataIndex : 'opticket.opticketName'
		}, {
			header : "操作票类别",
			hidden : true,
			dataIndex : 'opticket.opticketType'
		}, {
			header : "接收专业",
			sortable : true,
			width : 100,
			dataIndex : 'specialityName'
		}, {
			header : "操作票状态",
			sortable : true,
			width : 100,
			dataIndex : 'opticket.opticketStatus',
			renderer : function(value) {
				return getOpStatusName(value)
			}
		}, {
			header : "创建人",
			sortable : true,
			dataIndex : 'createrName'
		}, {
			header : "创建时间",
			sortable : true,
			dataIndex : 'createDate'
		}],
		// tbar : gridTbar,toolPanel
		tbar : toolPanel,
		bbar : pagebar,
		sm : smCSM,
		frame : false,
		border : false,
		enableColumnMove : false
	});
	rungrid.on('rowdblclick', editOpticket);
	rungrid.on('render', function() {
		var currentNode = null
		newNode = new Ext.tree.TreeNode({
			id : '-1',
			text : '灞桥电厂'
		})
		optiketTypeCbo.setValue(newNode);
		gridFresh(currentNode);
	});

	// 主框架
	new Ext.Viewport({
		layout : "border",
		items : [{
			xtype : "panel",
			region : 'center',
			layout : 'fit',
			border : false,
			items : [rungrid]
		}]
	});
	// ↑↑********** 主画面*******↑↑//

	// ↓↓*********处理***********↓↓//
	// 查询处理
	function searchOptickets() {
		var currentNode = optiketTypeCbo.getCurrentNode();
		gridFresh(currentNode);
	}
	// 编辑Grid
	function editOpticket(grid, rowIndex) {
		var opticketCode = grid.getStore().getAt(rowIndex)
				.get('opticket.opticketCode');
		// 编辑操作票记录
		parent.edit(opticketCode);
	}

	// 根据条件加载Grid
	function gridFresh(currentNode) {
		if (currentNode != null) {
			var code = currentNode.attributes.code;
		} else {
			var code = "";
		}
		queryStore.baseParams = {
			date : "",
			date2 : "",
			newOrOld : newOrOldValue,
			opType : code,
			specialCode : opticketSpecialCbo.value,
			optaskName : optaskName.getValue()
		};
		queryStore.load({
			params : {
				start : 0,
				limit : 18
			}
		});
	}
	// 重新加载Grid中的数据
	function gridReload() {
		queryStore.reload();
	}
	function checkBef() {
		var sm = rungrid.getSelectionModel();
		var selected = sm.getSelections();
		if (selected.length < 1) {
			// 没有选择数据时
			Ext.Msg.alert(Constants.SYS_REMIND_MSG,
					Constants.SELECT_NULL_VIEW_MSG);
		} else if (selected.length > 1) {
			// 选择多行数据时
			Ext.Msg.alert(Constants.SYS_REMIND_MSG,
					Constants.SELECT_COMPLEX_VIEW_MSG);
		} else {
			// 选择一行数据时
			opticketNo = selected[0].get('opticket.opticketCode');
			var url = "../content/befCheckStep.jsp?opCode=" + opticketNo;
			window
					.showModalDialog(
							url,
							null,
							'dialogWidth=700px;dialogHeight=500px;center=yes;help=no;resizable=no;status=no;');
		}

	}
	function workAft() {
		var sm = rungrid.getSelectionModel();
		var selected = sm.getSelections();
		if (selected.length < 1) {
			// 没有选择数据时
			Ext.Msg.alert(Constants.SYS_REMIND_MSG,
					Constants.SELECT_NULL_VIEW_MSG);
		} else if (selected.length > 1) {
			// 选择多行数据时
			Ext.Msg.alert(Constants.SYS_REMIND_MSG,
					Constants.SELECT_COMPLEX_VIEW_MSG);
		} else {
			// 选择一行数据时
			opticketNo = selected[0].get('opticket.opticketCode');
			var url = "../content/finishWork.jsp?opCode=" + opticketNo;
			window
					.showModalDialog(
							url,
							null,
							'dialogWidth=720px;dialogHeight=500px;center=yes;help=no;resizable=no;status=no;');

		}
	}
	// gridReload();
	setTimeout(function() {
		Ext.get('loading').remove();
		Ext.get('loading-mask').fadeOut({
			remove : true
		});
	}, 250);
	function viewRecord() {
		var sm = rungrid.getSelectionModel();
		var selected = sm.getSelections();
		if (selected.length < 1) {
			// 没有选择数据时
			Ext.Msg.alert('提示', '请选择一条记录');
		} else {
			// 选择一行时
			var member = selected[0].data;
			if (member['opticket.opticketCode']) {
				var workFlowNo = member['opticket.workFlowNo'];
				var flowCode = "bqStandOpTicket";
				viewLC(workFlowNo, flowCode);
			}
		}

	}
	// 预览票面
	function viewRecord1() {
		var sm = rungrid.getSelectionModel();
		var selected = sm.getSelections();
		var opticketNo = "";
		if (selected.length < 1) {
			// 没有选择数据时
			Ext.Msg.alert('提示', '请选择一条记录');
		} else {
			// 选择一行时
			var member = selected[0].data;
			if (member['opticket.opticketCode']) {
				opticketNo = member['opticket.opticketCode'];
				if (member['opticket.opticketType'].substring(0, 2) == '01') {
					viewOperateTicketRJBirt(opticketNo);
				} else {
					viewOperateTicketDQBirt(opticketNo);
				}
			}
		}

	}
	function viewRecord2() {
		var sm = rungrid.getSelectionModel();
		var selected = sm.getSelections();
		var opticketNo = "";
		if (selected.length < 1) {
			// 没有选择数据时
			Ext.Msg.alert('提示', '请选择一条记录');
		} else {
			// 选择一行时
			var member = selected[0].data;
			if (member['opticket.opticketCode']) {
				opticketNo = member['opticket.opticketCode'];
				viewOperateTicketDangerBirt(opticketNo);
			}
		}

	}
	function viewRecord3() {
		var sm = rungrid.getSelectionModel();
		var selected = sm.getSelections();
		var opticketNo = "";
		if (selected.length < 1) {
			// 没有选择数据时
			Ext.Msg.alert('提示', '请选择一条记录');
		} else {
			// 选择一行时

			var member = selected[0].data;
			if (member['opticket.opticketCode']) {
				opticketNo = member['opticket.opticketCode'];
				viewOperateTicketBEFBirt(opticketNo);
			}
		}

	}
	function viewRecord4() {
		var sm = rungrid.getSelectionModel();
		var selected = sm.getSelections();
		var opticketNo = "";
		if (selected.length < 1) {
			// 没有选择数据时
			Ext.Msg.alert('提示', '请选择一条记录');
		} else {
			// 选择一行时
			var member = selected[0].data;
			if (member['opticket.opticketCode']) {
				opticketNo = member['opticket.opticketCode'];
				viewOperateTicketAFTBirt(opticketNo);
			}
		}

	}
});