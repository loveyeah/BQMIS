Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {

	// 分页时每页显示记录条数
	var PAGE_SIZE = Constants.PAGE_SIZE;
	// 系统当天日期
	var startDate = new Date();
	var endDate = new Date();
	var opticketType = "";

	// 系统当天前30天的日期
	startDate.setDate(startDate.getDate() - 30);
	// 系统当天后30天的日期
	endDate.setDate(endDate.getDate() + 30);
	var optiketTypeCbo = new Ext.ux.ComboBoxTree({
		id : 'optiketTypeCbo',
		width:130,
		displayField : 'text',
		resizable : true,
		valueField : 'id',
		tree : {
			xtype : 'treepanel',
			// 虚拟节点,不能显示
			rootVisible : false,
			loader : new Ext.tree.TreeLoader({
				// dataUrl : 'opticket/getReportOpticketTypes.action'
				// dataUrl : 'opticket/getTreeNode.action'
				dataUrl : 'opticket/getDetailOpTaskTree.action'
			}),
			root : new Ext.tree.AsyncTreeNode({
				id : '-1',
				text : '灞桥电厂'
			})
		},
		selectNodeModel : 'all'
	});

	// 操作票专业下拉框数据源
	var opticketSpecialityStore = new Ext.data.JsonStore({
		url : 'opticket/getOpticketSpeciality.action',
		root : 'list',
		fields : [{
			name : 'specialityName'
		}, {
			name : 'specialityCode'
		}]
	})

	opticketSpecialityStore.load();
	// 操作票专业下拉框
	var opticketSpecialityCbo = new Ext.form.ComboBox({
		allowBlank : true,
		resizable : true,
		triggerAction : 'all',
		store : opticketSpecialityStore,
		displayField : 'specialityName',
		valueField : 'specialityCode',
		mode : 'local',
		// emptyText : '所有',
		// blankText : '所有',
		readOnly : true,
		width : 130,
		listeners : {
			'select' : function() {
				Ext.get("query").dom.click();
			}
		}
	})

	// 查询按钮
	var queryBtn = new Ext.Button({
		id : 'query',
		text : Constants.BTN_QUERY,
		iconCls : Constants.CLS_QUERY,
		handler : function() {
			var currentNode = optiketTypeCbo.getCurrentNode();
			runGridStore.baseParams = {
				// startDate : planStartDate.value,
				// endDate : planEndDate.value,
				startDate : "",
				endDate : "",
				optaskName : Ext.get('optaskName').dom.value,
				opticketType : currentNode.attributes.code,
				opticketSpeciality : opticketSpecialityCbo.value
			};
			runGridStore.load({
				params : {
					start : 0,
					limit : PAGE_SIZE
				}
			});
		}
	});
	var signBtn = new Ext.Button({
		id : 'signBtn',
		iconCls : 'write',
		text : '签字',
		handler : toUpdate
	})
	var batchSign = new Ext.Button({
		id : 'batchSign',
		iconCls : 'write',
		text : '批量签字',
		handler : batchApprove
	})
	var runGridList = new Ext.data.Record.create([{
		name : 'opticket.opticketCode'
	}, {
		name : 'opticket.opticketName'
	}, {
		name : 'opticket.opticketStatus'
	}, {
		name : 'opticket.appendixAddr'
	}, {
		name : 'opticket.opticketType'
	}, {
		name : 'opticket.isSingle'
	}, {
		name : 'opticket.workFlowNo'
	}, {
		name : 'createDate'
	}, {
		name : 'specialityName'
	}, {
		name : 'createrName'
	}])

	// grid中的store
	var runGridStore = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'opticket/getOpticketApproveList.action?isStandar=Y'
		}),
		reader : new Ext.data.JsonReader({
			root : 'list',
			totalProperty : 'totalCount'
		}, runGridList)
	})
	// 通过store的装载初始化所属下拉框的默认选项为store的第一项
	opticketSpecialityStore.on("load", function(e, records, o) {
		opticketSpecialityCbo.setValue(records[0].data.specialityCode);
	});
	// opticketTypeStore.on("load", function(e, records, o) {
	// opticketTypeCbo.setValue(records[0].data.operateTaskCode);
	// });

	// 初始化时,显示所有数据
	runGridStore.baseParams = {
		startDate : "",
		endDate : "",
		optaskName : '',
		opticketType : '',
		opticketSpeciality : opticketSpecialityCbo.value
	};

	runGridStore.load({
		params : {
			start : 0,
			limit : PAGE_SIZE
		}
	});

	// 选择列:单选
	var sm = new Ext.grid.CheckboxSelectionModel({
		header : '选择',
		id : 'check',
		width : 35,
		singleSelect : false
	});

	// 运行执行的Grid主体
	var runGrid = new Ext.grid.GridPanel({
		store : runGridStore,
		columns : [sm, {
			header : '编号',
			width : 100,
			align : 'left',
			sortable : true,
			dataIndex : 'opticket.opticketCode'
		}, {
			hidden : true,
			dataIndex : 'opticket.opticketType',
			renderer : getOpticketType
		}, {
			hidden : true,
			dataIndex : 'opticket.workFlowNo'
		}, {
			hidden : true,
			dataIndex : 'opticket.isSingle'
		}, {
			header : '操作任务',
			width : 320,
			align : 'left',
			sortable : true,
			dataIndex : 'opticket.opticketName'
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
		viewConfig : {
			forceFit : true
		},
		tbar : [{
			xtype : "tbseparator"
		}, '操作票类型：', optiketTypeCbo, {
			xtype : "tbseparator"
		}, '操作任务:', {
			id : 'optaskName',
			width:100,
			name : 'optaskName',
			xtype : 'textfield'
		}, '操作票专业：', opticketSpecialityCbo, {
			xtype : "tbseparator"
		}, queryBtn, '-', signBtn, '-', batchSign],
		// 分页
		bbar : new Ext.PagingToolbar({
			pageSize : PAGE_SIZE,
			store : runGridStore,
			displayInfo : true,
			displayMsg : Constants.DISPLAY_MSG,
			emptyMsg : Constants.EMPTY_MSG
		}),
		sm : sm,
		frame : false,
		border : false,
		enableColumnHide : true,
		enableColumnMove : false
	});

	// 注册双击事件
	runGrid.on('render', function() {
		var currentNode = null
		newNode = new Ext.tree.TreeNode({
			id : '-1',
			text : '灞桥电厂'
		})
		optiketTypeCbo.setValue(newNode);
	});
	runGrid.on("rowdblclick", toUpdate);

	// 设定布局器及面板
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		items : [{
			xtype : "panel",
			region : 'center',
			layout : 'fit',
			border : false,
			items : [runGrid]
		}]
	});
	function getOpticketType(value) {
		opticketType = value;
		return value;
	}
	// 设置显示的操作票状态名称:
	function codeToName(value) {
		return getOpStatusName(value);
	}
	// 显示查看附件
	function showAddr(value) {
		if (value != null && value != "") {
			// modify by zhengzhipeng 2008/12/15
			// return "<a href='upload-file/opticket/"+value+"'>查看附件</a>";
			return "<a href='" + value + "'>查看附件</a>";

		}
	}

	function batchApprove() {
		var sm = runGrid.getSelectionModel();
		var selected = sm.getSelections();
		if (selected.length == 0) {
			Ext.Msg.alert('提示信息', '请选择要审批的记录！');
		} else if (selected.length == 1) {
			Ext.Msg.alert('提示信息', '你选择的是一条记录，请直接点击签字按钮！');
		} else {
			var oCode = selected[0].get('opticket.opticketCode') + ",";
			var opticketStatus = selected[0].get("opticket.opticketStatus");
			var opticketType = selected[0].get("opticket.opticketType");
			for (var i = 1; i < selected.length; i++) {
				if (opticketStatus != selected[i]
						.get("opticket.opticketStatus")
						|| opticketType.substring(0, 2) != selected[i]
								.get("opticket.opticketType").substring(0, 2)) {
					Ext.Msg.alert('提示信息',"编号为'"+selected[i].get('opticket.opticketCode')+"'的操作票状态不同或者类型不同！")
					return false;
				}
				oCode += selected[i].get('opticket.opticketCode') + ",";
			}
			if (oCode.length > 0) {
				oCode.substring(0, oCode.length - 2);
			}
			var isSingle = selected[0].get("opticket.isSingle");
			var workFlowNo = selected[0].get("opticket.workFlowNo");
			var flowCode = "bqOpTicketDQ";
			if (opticketType.substring(0, 2) == "01") {
				flowCode = "bqOpTicketRL";
			}
			var args = new Object();
			args.opticketCode = oCode;
			args.opticketStatus = opticketStatus;
			args.opticketType = opticketType;
			args.isSingle = isSingle;
			args.entryId = workFlowNo;
			args.flowCode = flowCode;
			window
					.showModalDialog(
							'batchApprove.jsp',
							args,
							'dialogWidth=650px;dialogHeight=450px;center=yes;help=no;resizable=no;status=no;');
			runGridStore.reload();

		}

	}
	function toUpdate() {
		if (runGrid.selModel.hasSelection()) {// 是否有被选项
			// 获取选中行
			var url = 'approveWindow.jsp';
			var record = runGrid.getSelectionModel().getSelected();
			// 获取选中行信息:操作票编号
			var opticketCode = record.get("opticket.opticketCode");
			// 获取选中行信息:操作票状态
			var opticketStatus = record.get("opticket.opticketStatus");
			var opticketType = record.get("opticket.opticketType");
			var isSingle = record.get("opticket.isSingle");
			var workFlowNo = record.get("opticket.workFlowNo");
			var flowCode = "bqOpTicketDQ";
			if (opticketType.substring(0, 2) == "01") {
				flowCode = "bqOpTicketRL";
			}
			var args = new Object();
			args.opticketCode = opticketCode;
			args.opticketStatus = opticketStatus;
			args.opticketType = opticketType;
			args.isSingle = isSingle;
			args.entryId = workFlowNo;
			args.flowCode = flowCode;
			// Ext.Msg.alert("aa","编号:"+opticketCode+" 状态:"+opticketStatus);
			// 根据操作票状态决定转入目的jsp的url
			Ext.Ajax.request({
				url : 'MAINTWorkflow.do?action=getCurrentStepsInfo',
				method : 'POST',
				params : {
					entryId : workFlowNo
				},
				success : function(result, request) {
					var obj = eval("(" + result.responseText + ")");
					if (obj[0].url != null || obj[0].url != "") {
						var url = 'standApproveWindow.jsp';
						// url = obj[0].url;
						// alert(url);
					}
					window.showModalDialog(url, args,
							'status:no;dialogWidth=700px;dialogHeight=550px');
					runGridStore.reload();
				},
				failure : function(result, request) {
					Ext.Msg.alert("提示", "错误");
				}

			});

		} else {
			Ext.Msg.alert("提示", "请选择记录");
		}
	}

	function viewRecord() {
		var sm = runGrid.getSelectionModel();
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
				window.open("/power/report/webfile/opticketPrint.jsp?no="
						+ opticketNo);
			}
		}

	}
	setTimeout(function() {
		Ext.get('loading').remove();
		Ext.get('loading-mask').fadeOut({
			remove : true
		});
	}, 250);
});