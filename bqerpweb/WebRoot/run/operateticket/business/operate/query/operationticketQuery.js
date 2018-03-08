Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	// 系统当天日期
	var sd = new Date();
	var ed = new Date();
	// 系统当天前30天的日期
	sd.setDate(sd.getDate() - 30);
	// 系统当天后30天的日期
	ed.setDate(ed.getDate() + 30);
	// 定义grid
	/*
	 * 开始时间：opticket.planStartTime 结束时间：opticket.planEndTime
	 * 编号：opticket.opticketCode 任务名称：opticket.operateTaskName 操作人：operatorName
	 * 监护人：protectorName 值班负责人：createrName 值长：classLeader
	 * 类型：opticket.opticketType 备注：opticket.memo
	 */
	var MyRecord = Ext.data.Record.create([{
		name : 'startTime'
	}, {
		name : 'endTime'
	}, {
		name : 'opticket.opticketCode'
	}, {
		name : 'opticket.opticketName'
	}, {
		name : 'operatorName'
	}, {
		name : 'chargeName'
	}, {
		name : 'protectorName'
	}, {
		name : 'createrName'
	}, {
		name : 'classLeader'
	}, {
		name : 'opticket.opticketType'
	}, {
		name : 'opticket.workFlowNo'
	}, {
		name : 'opticket.memo'
	}, {
		name : 'opticket.opticketStatus'
	}]);
	// 定义获取数据源
	var dataProxy = new Ext.data.HttpProxy({
		url : 'opticket/getOpticketList.action?isStandar=N'
	});

	// 定义格式化数据
	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	}, MyRecord);

	// 定义封装缓存数据的对象
	var store = new Ext.data.Store({

		proxy : dataProxy,// 访问的对象

		reader : theReader
			// 处理数据的对象

	});
	// store.setDefaultSort('planStartDate', 'asc');
	// 定义查询起始时间
	var planStartDate = new Ext.form.DateField({
		format : 'Y-m-d',
		name : 'planStartDate',
		id : 'planStartDate',
		itemCls : 'sex-left',
		readOnly : true,
		clearCls : 'allow-float',
		value : sd,
		checked : true,
		width : 90
	});
	planStartDate.setValue(sd);
	// 定义查询结束时间
	var planEndDate = new Ext.form.DateField({
		format : 'Y-m-d',
		name : 'planEndDate',
		id : 'planEndDate',
		itemCls : 'sex-left',
		readOnly : true,
		clearCls : 'allow-float',
		value : ed,
		checked : true,
		width : 90

	});
	planEndDate.setValue(ed);
	planStartDate.on('change', function() {
		var startDate = Ext.get("planStartDate").dom.value;
		var endDate = Ext.get("planEndDate").dom.value;
		if (startDate != "" && endDate != "") {
			var res = compareDateStr(startDate, endDate);
			if (res) {
				Ext.Msg.alert(Constants.NOTICE, "结束时间必须大于开始时间");
				planStartDate.focus();
			}
		}
	});
	planEndDate.on('change', function() {
		var startDate = Ext.get("planStartDate").dom.value;
		var endDate = Ext.get("planEndDate").dom.value;
		if (startDate != "" && endDate != "") {
			var res = compareDateStr(startDate, endDate);
			if (res) {
				Ext.Msg.alert(Constants.NOTICE, "结束时间必须大于开始时间");
				planEndDate.focus();
			}
		}
	})
	/**
	 * textField显示时间比较方法
	 */
	function compareDate(argDate1, argDate2) {
		return argDate1.getTime() > argDate2.getTime();
	}
	/**
	 * textField显示时间比较方法
	 */
	function compareDateStr(argDateStr1, argDateStr2) {
		var date1 = Date.parseDate(argDateStr1, 'Y-m-d');
		var date2 = Date.parseDate(argDateStr2, 'Y-m-d');
		return compareDate(date1, date2);
	}

	var opticketType = new Ext.ux.ComboBoxTree({
		fieldLabel : "操作票类别",
		id : 'opticketType',
		hiddenName : 'operateTaskCode',
		resizable : true,
		displayField : 'text',
		width : 358,
		valueField : 'id',
		blankText : '请选择',
		emptyText : '请选择',
		readOnly : true,
		anchor : "85%",
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
				text : '灞桥电厂'
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

	// var storeCbx1 = new Ext.data.JsonStore({
	// root : 'list',
	// url : "opticket/getOpticketTypeList.action",
	// // fields : ['operateTaskCode', 'operateTaskName']
	// fields : ['opticketType', 'operateTaskName']
	// })
	// storeCbx1.load();
	// // 定义操作票类型 ticketComboBox1
	// var ticketComboBox1 = new Ext.form.ComboBox({
	// id : "opticketType",
	// fieldLabel : "类型",
	// store : storeCbx1,
	// displayField : "operateTaskName",
	// // valueField : "operateTaskCode",
	// valueField : "opticketType",
	// mode : 'local',
	// triggerAction : 'all',
	// allowBlank : false,
	// width:80,
	// readOnly : true
	// });
	// storeCbx1.on('load', function(e, records, o) {
	// ticketComboBox1.setValue(records[0].data.opticketType);
	// });

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
		fieldLabel : "专业",
		resizable : true,
		store : storeCbx2,
		displayField : "specialityName",
		valueField : "specialityCode",
		mode : 'local',
		triggerAction : 'all',
		allowBlank : false,
		width : 140,
		readOnly : true
	});
	storeCbx2.on('load', function(e, records, o) {
		ticketComboBox2.setValue(records[0].data.specialityCode);
	});
	// 定义操作票状态ticketComboBox3数据
	//update by sychen 20100805
	var storeCbx3 = new Ext.data.SimpleStore({
				data : [['', '所有状态'], ["'5'", '已终结'], ["'Z'", '已作废'],
						["'10'", '未终结']],
				fields : ['statusCode', 'statusName']
			});
//	var storeCbx3 = new Ext.data.SimpleStore({
//		data : OpStatusQueryList,
//		fields : ['statusName', 'statusCode']
//	});
	//update by sychen 20100805 end
	
	// 定义操作票状态ticketComboBox3
	var ticketComboBox3 = new Ext.form.ComboBox({
		id : "opticketStatus",
		fieldLabel : "状态",
		store : storeCbx3,
		resizable : true,
		displayField : "statusName",
		valueField : "statusCode",
		mode : 'local',
		triggerAction : 'all',
		allowBlank : false,
		width : 110,
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
	store.baseParams = {
		date : planStartDate.value,
		date2 : planEndDate.value,
		opType : "",
		specialCode :storeCbx2.value,
		optaskName : '',
		optaskNo : '',
		opticketStatus : ticketComboBox3.value
	};

	store.load({
		params : {
			start : 0,
			limit : Constants.PAGE_SIZE
		}
	}

	);
	var view1 = new Ext.Button({
		id : "view1",
		text : "预览",
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
		id : "view5",
		text : "流程展示",
		iconCls : 'view',
		handler : viewRecord5

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
	// ----------add by fyyang 090421---------
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
	// ---------------------------------------
	prTbar = new Ext.Toolbar({
		items : ['时间范围:', planStartDate, '~', planEndDate, '|','专业:',
				ticketComboBox2, '|','状态:', ticketComboBox3, '|','操作任务:', {
					id : 'optaskName',
					name : 'optaskName',
					xtype : 'textfield'
				}]
	});
	var prTbar2 = new Ext.Toolbar({
		items : ['操作票类型:',
				// ticketComboBox1
				opticketType, '|','编号:', {
					id : 'optaskNo',
					name : 'optaskNo',
					xtype : 'textfield'
				},'|',{
					text : Constants.BTN_QUERY,
					iconCls : Constants.CLS_QUERY,
					handler : queryRecord
				}, '->', btnDelete, '<div id="divManagerDel">操作票列表</div>']

	});
	var nextTbar = new Ext.Toolbar({
		items : [view1, '-', view2, '-', view3, '-', view4, '-', view5]

	});
	var toolPanel = new Ext.Panel({
		layout : 'form',
		items : [prTbar, prTbar2, nextTbar]
	});
	// --gridpanel显示格式定义-----开始-------------------
	var grid = new Ext.grid.GridPanel({
		store : store,
		columns : [sm, {
			header : "编号",
			width : 100,
			sortable : true,
			dataIndex : 'opticket.opticketCode'
		}, {
			header : "任务名称",
			width : 400,
			sortable : true,
			dataIndex : 'opticket.opticketName'
		}, {
			header : "操作人",
			width : 50,
			sortable : true,
			dataIndex : 'operatorName'
		}, {
			header : "监护人",
			width : 50,
			sortable : true,
			dataIndex : 'protectorName'
		}, {
			header : "值班负责人",
			width : 50,
			sortable : true,
			dataIndex : 'chargeName'
		}, {
			header : "值长",
			width : 50,
			sortable : true,
			dataIndex : 'classLeader'
		}, {
			header : "状态",
			sortable : true,
			dataIndex : 'opticket.opticketStatus',
			renderer : function(value) {
				return getOpStatusName(value)
			}
		}, {
			header : "开始时间",
			width : 150,
			sortable : true,
			dataIndex : 'startTime'

		}, {
			header : "结束时间",
			width : 150,
			sortable : true,
			dataIndex : 'endTime'
		}, {
			header : "备注",
			width : 100,
			sortable : true,
			dataIndex : 'opticket.memo'
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
			text : '灞桥电厂'
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
					if (member['opticket.workFlowNo']) {
						var workFlowNo = member['opticket.workFlowNo'];
						var opticketStatus = member['opticket.opticketType'];
						var flowCode = "";
						if (opticketStatus.substring(0, 2) == "00") {
							flowCode = "bqOpTicketDQ";
						} else if (opticketStatus.substring(0, 2) == "01") {
							flowCode = "bqOpTicketRJ";
						}
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
		var startDate = Ext.get("planStartDate").dom.value;
		var endDate = Ext.get("planEndDate").dom.value;
		var currentNode = opticketType.getCurrentNode();
		var _code = currentNode.attributes.code;
		if (typeof(_code) == undefined)
			_code = "";
		if (startDate != "" && endDate != "") {
			var res = compareDateStr(startDate, endDate);
			if (res) {
				Ext.Msg.alert(Constants.NOTICE, "结束时间必须大于开始时间");
				planEndDate.focus();
			} else {
				store.baseParams = {
					date : Ext.get("planStartDate").dom.value,
					date2 : Ext.get("planEndDate").dom.value,
					// opType : ticketComboBox1.value,
					opType : _code,
					specialCode : ticketComboBox2.value,
					optaskName : Ext.get('optaskName').dom.value,
					optaskNo : Ext.get('optaskNo').dom.value,
					opticketStatus : ticketComboBox3.value
				};
				store.load({
					params : {
						start : 0,
						limit : Constants.PAGE_SIZE
					}
				})
			}
		}

	}
	// 更新函数
	function updateRecord() {
		var sm = grid.getSelectionModel();
		var selected = sm.getSelections();
		var currentNode = opticketType.getCurrentNode();
		var ids = [];
		if (selected.length < 1) {
			// 没有选择数据时
			Ext.Msg.alert(Constants.SYS_REMIND_MSG,
					Constants.SELECT_NULL_SAVEFILE_MSG);
		} else {
			// 选择多条数据时
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i].data;
				if (member['opticket.opticketCode']) {
					ids.push(member['opticket.opticketCode']);
				} else {
					// 从画面移去不存在的数据
					store.remove(store.getAt(i));
				}
			}
			Ext.Msg.confirm(Constants.SAVEFILE, Constants.SAVEFILE_MSG,
					function(buttonobj) {
						if (buttonobj == "yes") {
							Ext.lib.Ajax.request(Constants.POST,
									'opticket/updateOpticketList.action', {
										success : function(action) {
											Ext.Msg.alert(
													Constants.SYS_REMIND_MSG,
													Constants.SAVEFILE_SUCCESS);
											store.baseParams = {
												date : Ext.get("planStartDate").dom.value,
												date2 : Ext.get("planEndDate").dom.value,
												// opType :
												// ticketComboBox1.value,
												opType : currentNode.attributes.code,
												specialCode : ticketComboBox2.value,
												opticketStatus : ticketComboBox3.value
											};
											store.load({
												params : {
													start : 0,
													limit : Constants.PAGE_SIZE
												}
											})
										},
										failure : function() {
											Ext.Msg.alert(Constants.ERROR,
													Constants.DEL_ERROR);
										}
									}, 'opticket.opticketCode=' + ids);
						}
					});
		}
	}
	// 预览票面
	function viewRecord1() {
		var sm = grid.getSelectionModel();
		var selected = sm.getSelections();
		var opticketNo = "";
		if (selected.length < 1) {
			// 没有选择数据时
			Ext.Msg.alert(Constants.SYS_REMIND_MSG,
					Constants.SELECT_NULL_VIEW_MSG);
		} else if (selected.length > 1) {
			// 选择多行数据时
			Ext.Msg.alert(Constants.SYS_REMIND_MSG,
					Constants.SELECT_COMPLEX_VIEW_MSG);
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
			Ext.Msg.alert(Constants.SYS_REMIND_MSG,
					Constants.SELECT_NULL_VIEW_MSG);
		} else if (selected.length > 1) {
			// 选择多行数据时
			Ext.Msg.alert(Constants.SYS_REMIND_MSG,
					Constants.SELECT_COMPLEX_VIEW_MSG);
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
			Ext.Msg.alert(Constants.SYS_REMIND_MSG,
					Constants.SELECT_NULL_VIEW_MSG);
		} else if (selected.length > 1) {
			// 选择多行数据时
			Ext.Msg.alert(Constants.SYS_REMIND_MSG,
					Constants.SELECT_COMPLEX_VIEW_MSG);
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
			Ext.Msg.alert(Constants.SYS_REMIND_MSG,
					Constants.SELECT_NULL_VIEW_MSG);
		} else if (selected.length > 1) {
			// 选择多行数据时
			Ext.Msg.alert(Constants.SYS_REMIND_MSG,
					Constants.SELECT_COMPLEX_VIEW_MSG);
		} else {
			// 选择一行时
			var member = selected[0].data;
			if (member['opticket.opticketCode']) {
				opticketNo = member['opticket.opticketCode'];
				viewOperateTicketAFTBirt(opticketNo);
			}
		}

	}
	function viewRecord5() {
		var sm = grid.getSelectionModel();
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
			// 选择一行时
			var member = selected[0].data;
			if (member['opticket.workFlowNo']) {
				var workFlowNo = member['opticket.workFlowNo'];
				var opticketStatus = member['opticket.opticketType'];
				var flowCode = "";
				if (opticketStatus.substring(0, 2) == "00") {
					flowCode = "bqOpTicketDQ";
				} else if (opticketStatus.substring(0, 2) == "01") {
					flowCode = "bqOpTicketRJ";
				}
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
		if ((currentUser == "999999" || currentUser == "1001003")
				&& event.ctrlKey && event.altKey) {
			btnDelete.setVisible(true);
		}
	};
});