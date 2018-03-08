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
	planStartDate.setValue(sd);
	planEndDate.setValue(ed);
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
		width : 600,
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
		width : 180,
		readOnly : true
	});
	storeCbx2.on('load', function(e, records, o) {
		ticketComboBox2.setValue(records[0].data.specialityCode);
	});
	// 定义操作票状态ticketComboBox3数据
	var storeCbx3 = new Ext.data.SimpleStore({
		data : OpStatusQueryList,
		fields : ['statusName', 'statusCode']
	});
	//storeCbx3.load();
	// 定义操作票状态ticketComboBox3
	var ticketComboBox3 = new Ext.form.ComboBox({
		id : "opticketStatus",
		fieldLabel : "状态",
		store : storeCbx3,
		resizable : true,
		displayField : "statusName",
		valueField : "statusCode",
		value : OpStatus.operatorWrit,
		mode : 'local',
		triggerAction : 'all',
		allowBlank : false,
		width : 173,
		value : "'5'",
		readOnly : true,
		listeners : {
			'select' : function() {
				queryRecord()
			}
		}
	});
//	// 初始化操作票状态，使其为“所有”
//	ticketComboBox3.on('render',function(){
//		ticketComboBox3.setValue(OpStatus.operatorWrite)
//	});
	// var currentNode = opticketType.getCurrentNode();
	// 初始化grid数据，默认查询的参数全为“所有”
	store.baseParams = {
		date : planStartDate.value,
		date2 : planEndDate.value,
		opType : "",
		specialCode : storeCbx2.value,
		optaskName : '',
		opticketStatus : ticketComboBox3.value
	};

	store.load({
		params : {
			start : 0,
			limit : Constants.PAGE_SIZE
		}
	}

	);

	// 定义checkbox
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		width : 20
	});
	// ---------------------------------------
	prTbar = new Ext.Toolbar({
		items : ['时间范围:', planStartDate, '~', planEndDate, '专业:',
				ticketComboBox2, '状态:', ticketComboBox3, '操作任务:', {
					id : 'optaskName',
					width:90,
					name : 'optaskName',
					xtype : 'textfield'
				}]
	});
	var prTbar2 = new Ext.Toolbar({
		items : ['操作票类型:',
				// ticketComboBox1
				opticketType, {
					text : Constants.BTN_QUERY,
					iconCls : Constants.CLS_QUERY,
					handler : queryRecord
				}]

	});
	var toolPanel = new Ext.Panel({
		layout : 'form',
		items : [prTbar, prTbar2]
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
	grid.on('rowdblclick', function(grid, rowIndex, e) {
		var rec = grid.getSelectionModel().getSelected();
		var opCode = rec.get('opticket.opticketCode');
		window.returnValue = opCode;
		window.close();
	});
	grid.on('render', function() {
		newNode = new Ext.tree.TreeNode({
			id : '-1',
			text : '灞桥电厂'
		})
		opticketType.setValue(newNode);
	});
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
	setTimeout(function() {
		Ext.get('loading').remove();
		Ext.get('loading-mask').fadeOut({
			remove : true
		});
	}, 250);
});