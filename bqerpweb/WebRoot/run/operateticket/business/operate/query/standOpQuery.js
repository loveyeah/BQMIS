Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	// 系统当天日期
	var sd = new Date();
	var ed = new Date();
	// 系统当天前30天的日期
	sd.setDate(sd.getDate() - 30);
	// 系统当天后30天的日期
	ed.setDate(ed.getDate() + 30);

	var newOrOldValue = 'new';
	// 定义grid
	/*
	 * 开始时间：opticket.planStartTime 结束时间：opticket.planEndTime
	 * 编号：opticket.opticketCode 任务名称：opticket.operateTaskName 操作人：operatorName
	 * 监护人：protectorName 值班负责人：createrName 值长：classLeader
	 * 类型：opticket.opticketType 备注：opticket.memo
	 */
	var MyRecord = Ext.data.Record.create([
			// {
			// name : 'planStartDate'
			// },
			// {
			// name : 'planEndDate'
			// },
			{
				name : 'opticket.opticketCode'
			}, {
				name : 'opticket.opticketName'
			}, {
				name : 'protectorName'
			}, {
				name : 'chargeName'
			}, {
				name : 'opticket.workFlowNo'
			}, {
				name : 'opticket.opticketType'
			}, {
				name : 'opticket.memo'
			}, {
				name : 'opticket.opticketStatus'
			}]);
	// 定义获取数据源
	var dataProxy = new Ext.data.HttpProxy({
		url : 'opticket/getOpticketList.action?isStandar=Y'
	});

	// 定义格式化数据
	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	}, MyRecord);

	// 定义封装缓存数据的对象
	var store = new Ext.data.Store({
		proxy : dataProxy,
		reader : theReader
	});
	store.on("beforeload", function() {
		Ext.Msg.wait("正在查询数据,请等待...");
	});
	store.on("load", function() {
		Ext.Msg.hide();
	});

	var opticketType = new Ext.ux.ComboBoxTree({
		fieldLabel : "操作票类别",
		id : 'opticketType',
		hiddenName : 'operateTaskCode',
		displayField : 'text',
		valueField : 'id',
		blankText : '请选择',
		emptyText : '请选择',
		resizable : true,
		readOnly : true,
		width : 130,
		allowBlank : false,
		tree : {
			xtype : 'treepanel',
			rootVisible : true,
			loader : new Ext.tree.TreeLoader({
				// dataUrl : 'opticket/getTreeNode.action'
				dataUrl : 'opticket/getDetailOpTaskTree.action'
			}),
			root : new Ext.tree.AsyncTreeNode({
				id : '-1',
				text : '所有'
			})
		},
		selectNodeModel : 'all',
		listeners : {
			collapse : function() {
				if (this.value) {
					this.el.dom.readOnly = false;
				}
			}
		}
	});
	// 定义操作票专业 ticketComboBox2数据源
	var storeCbx2 = new Ext.data.JsonStore({
		root : 'list',
		url : "opticket/getSpeTypeList.action",
		fields : ['specialityCode', 'specialityName']
	})
	storeCbx2.load();
	// 定义操作票专业 ticketComboBox2
	var ticketComboBox2 = new Ext.form.ComboBox({
		id : "speType",
		resizable : true,
		fieldLabel : "专业",
		store : storeCbx2,
		displayField : "specialityName",
		valueField : "specialityCode",
		mode : 'local',
		triggerAction : 'all',
		allowBlank : false,
		readOnly : true,
		width : 100
	});
	storeCbx2.on('load', function(e, records, o) {
		ticketComboBox2.setValue(records[0].data.specialityCode);
	});
	// 定义操作票状态ticketComboBox3数据
	var storeCbx3 = new Ext.data.SimpleStore({
		data : standardOpStatusQueryList,
		fields : ['statusName', 'statusCode']
	});
	// 定义操作票状态ticketComboBox3
	var ticketComboBox3 = new Ext.form.ComboBox({
		id : "opticketStatus",
		fieldLabel : "状态",
		resizable : true,
		store : storeCbx3,
		displayField : "statusName",
		valueField : "statusCode",
		mode : 'local',
		triggerAction : 'all',
		allowBlank : false,
		width : 100,
		readOnly : true,
		listeners : {
			'select' : function() {
				queryRecord()
			}
		}
	});
	// 初始化操作票状态，使其为“所有”
	ticketComboBox3.setValue("所有");
	// var currentNode = opticketType.getCurrentNode();
	// 初始化grid数据，默认查询的参数全为“所有”
	// store.baseParams = {
	// date : "",
	// date2 : "",
	// newOrOld:newOrOldValue,
	// // opType : ticketComboBox1.value,
	// opType : "",
	// specialCode : ticketComboBox2.value,
	// optaskName : '',
	// opticketStatus : ticketComboBox3.value
	// };
	// store.load({
	// params : {
	// start : 0,
	// limit : Constants.PAGE_SIZE
	// }
	// }
	//
	// );
	var view5 = new Ext.Button({
		id : "view",
		text : "流程展示",
		iconCls : 'view',
		handler : viewRecord

	});
	// 定义checkbox
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		width : 20,
		listeners : {
			rowselect : function(sm, row, rec) {
				if (rec.get("opticket.opticketType").substring(0, 2) == "00") {
					Ext.get("view2").dom.disabled = true;
					Ext.get("view3").dom.disabled = false;
					Ext.get("view4").dom.disabled = false;
				} else {
					Ext.get("view2").dom.disabled = false;
					Ext.get("view3").dom.disabled = true;
					Ext.get("view4").dom.disabled = true;
				}
			}
		}
	});
	// 预览票面
	function viewRecord1() {
		var sm = grid.getSelectionModel();
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
		var sm = grid.getSelectionModel();
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
		var sm = grid.getSelectionModel();
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
		var sm = grid.getSelectionModel();
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
	function getSelectValue() {
		var ns = document.getElementsByName("newOrOld");
		for (var i = 0; i < ns.length; i++) {
			if (ns[i].checked) {
				return ns[i].value;
			}
		}
	}

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
					queryRecord();
				}
			}
		}), new Ext.form.Radio({
			columnWidth : 0.5,
			boxLabel : "老系统数据",
			name : "newOrOld",
			inputValue : "old"
		})]
	});
	var nextTbar = new Ext.Toolbar({
		items : [newOrOld, view1, '-', view2, '-', view3, '-', view4, '-',
				view5]

	});

	//by ghzhou
	var btnDelete = new Ext.Button({
		id : "btnDelete",
		text : "删除",
		hidden : true,
		iconCls : 'delete',
		handler : function() {
			var record = grid.getSelectionModel().getSelected();
			if (record) {
				if (confirm("删除后数据不能恢复,确定要删除该条操作票吗?")) {
					var busiNo = record.get("opticket.opticketCode");
					var entryId = record.get("opticket.workFlowNo");
					Ext.Ajax.request({
						url : "MAINTWorkflow.do?action=mangerDelete",
						method : 'post',
						params : {
							busiType : 'opticket',
							entryId : entryId,
							busiNo : busiNo
						},
						success : function(result, request) {
							Ext.Msg.alert("提示", "操作成功!");
							grid.getStore().remove(record);
							btnDelete.setVisible(false);
						},
						failure : function() {
							Ext.Msg.alert("提示", "操作失败!");
						}
					});
				}
			} else {
				Ext.Msg.alert("提示", "请选择一条删除的记录！");
			}
		}
	});
	
	var queryBar = new Ext.Toolbar({
		items : [
				// '时间范围:', planStartDate, '~', planEndDate,
				'操作票类型:',
				// ticketComboBox1
				opticketType, '专业:', ticketComboBox2, '状态:', ticketComboBox3,
				'-', '操作任务:', {
					id : 'optaskName',
					width:90,
					name : 'optaskName',
					xtype : 'textfield'
				}, '-', {
					text : '查询',
					iconCls : 'query',
					handler : queryRecord
				},'->', btnDelete, '<div id="divManagerDel">操作票列表</div>']
	});

	var toolPanel = new Ext.Panel({
		items : [queryBar, nextTbar]
	});
	var grid = new Ext.grid.GridPanel({
		store : store,
		columns : [new Ext.grid.RowNumberer({
			header : "",
			sortable : false,
			width : 30
		}), {
			header : "编号",
			width : 150,
			sortable : true,
			dataIndex : 'opticket.opticketCode'
		}, {
			header : "任务名称",
			width : 400,
			sortable : true,
			dataIndex : 'opticket.opticketName',
			renderer : function(v) {
				if (v != null) {
					return "<div style='white-space:normal;'>" + v + "</div>";
				} else {
					return ""
				}

			}
		}, {
			header : "状态",
			sortable : true,
			dataIndex : 'opticket.opticketStatus',
			renderer : function(value) {
				return getOpStatusName(value)
			}
		}, {
			header : "安检部门负责人",
			width : 150,
			sortable : true,
			dataIndex : 'protectorName'
		}, {
			header : "工程师",
			width : 150,
			sortable : true,
			dataIndex : 'chargeName'
		}, {
			header : "备注",
			width : 180,
			sortable : true,
			dataIndex : 'opticket.memo'
		}, {
			dataIndex : 'opticket.opticketStatus',
			hidden : true
		}],
		sm : sm,
		// 头部工具栏
		tbar : toolPanel,
		// 底部工具栏
		bbar : new Ext.PagingToolbar({
			pageSize : Constants.PAGE_SIZE,
			store : store,
			displayInfo : true,
			displayMsg : Constants.DISPLAY_MSG,
			emptyMsg : Constants.EMPTY_MSG
		}),
		// 不允许移动列
		enableColumnMove : false
	});
	grid.on('render', function() {
		newNode = new Ext.tree.TreeNode({
			id : '-1',
			text : '所有'
		})
		opticketType.setValue(newNode);
	});
	grid.on("rowcontextmenu", function(g, i, e) {
		e.stopEvent();
		var record = grid.getStore().getAt(i);
		// 右键菜单
		var menu = new Ext.menu.Menu({
			id : 'mainMenu',
			items : [new Ext.menu.Item({
				text : "预览",
				iconCls : 'pdfview',
				handler : function() {
					var opticketNo = "";
					var member = record.data;
					if (member['opticket.opticketCode']) {
						opticketNo = member['opticket.opticketCode'];
						if (member['opticket.opticketType'].substring(0, 2) == '01') {
							viewOperateTicketRJBirt(opticketNo);
						} else {
							viewOperateTicketDQBirt(opticketNo);
						}
					}
				}
			}), new Ext.menu.Item({
				text : "流程展示",
				iconCls : 'view',
				handler : function() {
					var member = record.data;
					if (member['opticket.opticketCode']) {
						var workFlowNo = member['opticket.workFlowNo'];
						var flowCode = "bqStandOpTicket";
						viewLC(workFlowNo, flowCode);
					}
				}
			})]
		});
		var coords = e.getXY();
		menu.showAt([coords[0], coords[1]]);
	});
	grid.on("rowdblclick", viewRecord1);
	// grid.on("rowclick", changeButton);
	// grid.on("rowclick",viewRecord);
	// function changeButton(grid, t) {
	//
	// var st = store.getAt(t).get('opticket.opticketStatus');
	// var sc = sm.getCount();
	//
	// if (st == "5" && sc == 1) {
	// save.setDisabled(false);
	// } else {
	// save.setDisabled(true);
	//
	// }
	//
	// }
	// --gridpanel显示格式定义-----结束--------------------
	// 页面加载显示数据
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		items : [{
			xtype : "panel",
			region : 'center',
			layout : 'fit',
			border : false,
			items : [grid]
		}]
	});
	// 查询函数
	function queryRecord() {
		var currentNode = opticketType.getCurrentNode();
		store.baseParams = {
			date : "",
			date2 : "",
			newOrOld : newOrOldValue,
			opType : currentNode == null ? "" : currentNode.attributes.code,
			specialCode : ticketComboBox2.value,
			optaskName : Ext.get('optaskName').dom.value,
			opticketStatus : ticketComboBox3.value
		};
		store.load({
			params : {
				start : 0,
				limit : Constants.PAGE_SIZE
			}
		})
	}
	function viewRecord() {
		var sm = grid.getSelectionModel();
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
	setTimeout(function() {
		Ext.get('loading').remove();
		Ext.get('loading-mask').fadeOut({
			remove : true
		});
	}, 250);
	document.getElementById("divManagerDel").onclick = function() {
		if ((currentUser == "999999" || currentUser == "1001003" || currentUser == "0900007"||currentUser == "3000007")
				&& event.ctrlKey && event.altKey) {
			btnDelete.setVisible(true);
		}
	};
});