Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	Ext.QuickTips.init();

	function getYear() {
		var d, s;
		d = new Date();
		s = d.getFullYear().toString(10);
		return s;
	}
	function getSeason() {
		var d, m;
		d = new Date();
		m = d.getMonth() + 1;
		if (m % 3 == 0)
			return m / 3;
		else
			return Math.floor(m / 3) + 1
	}
	// 年份
	var year = new Ext.form.TextField({
		id : 'year',
		fieldLabel : '年份',
		name : 'year',
		style : 'cursor:pointer',
		readOnly : true,
		width : 80,
		value : getYear(),
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y',
					alwaysUseStartDate : false,
					dateFmt : 'yyyy',
					onpicked : function() {
						sea.setDisabled(false)
					},
					onclearing : function() {
						sea.setValue(null);
						sea.setDisabled(true)
					}
				});
			}
		}
	})
	// 季度
	var seStore = new Ext.data.SimpleStore({
		fields : ['id', 'name'],
		data : [['', ''], ['1', '一季度'], ['2', '二季度'], ['3', '三季度'],
				['4', '四季度']]
	})
	var sea = new Ext.form.ComboBox({
		id : 'sea',
		fieldLabel : '季度',
		name : 'sea',
		store : seStore,
		valueField : 'id',
		displayField : 'name',
		width : 80,
		mode : 'local',
		triggerAction : 'all',
		value : getSeason()
	})
	var measureCode = new Ext.form.TextField({
		id : 'measureCode',
		fieldLabel : "措施编号",
		name : 'measureCode',
		anchor : "75%"
	});
	/**
	 * 当单击主grid中一行时，下面的grid显示详细信息
	 */
	function showDetail() {
		if (headGrid.getSelectionModel().getSelected() != null) {
			var record = headGrid.getSelectionModel().getSelected();
			if (record.get('check.isProblem') == 'Y') {
				materialStore.load({
					params : {
						checkupId : record.get('check.checkupId')
					}
				});
			} else {
				materialStore.removeAll();
			}
		}
	}
	// 查询按钮
	var btnFind = new Ext.Toolbar.Button({
		text : "查询",
		iconCls : Constants.CLS_QUERY,
		handler : queryRecord
	});

	function queryRecord() {
		var season = null;
		if (year.getValue() != null && year.getValue() != '') {
			if (sea.getValue() == null || sea.getValue() == '') {
				Ext.Msg.alert('提示', '请选择季度');
				return;
			}
		}
		if (year.getValue() != null && year.getValue() != '') {
			season = year.getValue();
			season += sea.getValue();
		}
		headStore.load({
			params : {
				start : 0,
				limit : 18,
				status : 1,
				season : season,
				measureCode : Ext.get('measureCode').dom.value
			}
		});
	}

	var signBtn = new Ext.Toolbar.Button({
		text : "审核",
		iconCls : 'add',
		handler : sign
	});

	var secondLine = new Ext.Panel({
		border : false,
		height : 30,
		layout : "column",
		style : "padding-top:5px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
		anchor : '100%',
		items : [{
			columnWidth : 0.13,
			layout : "form",
			border : false,
			labelWidth : 35,
			items : [year]
		}, {
			columnWidth : 0.13,
			layout : "form",
			border : false,
			labelWidth : 35,
			items : [sea]
		}, {
			columnWidth : 0.2,
			layout : "form",
			border : false,
			labelWidth : 70,
			items : [measureCode]
		}, {
			columnWidth : 0.1,
			layout : "form",
			border : false,
			labelWidth : 1,
			items : [btnFind]
		}, {
			columnWidth : 0.1,
			layout : "form",
			border : false,
			labelWidth : 1,
			items : [signBtn]
		}]
	});

	var headInfo = Ext.data.Record.create([{
		name : 'check.checkupId'
	}, {
		name : 'check.measureCode'
	}, {
		name : 'check.isProblem'
	}, {
		name : 'check.season'
	}, {
		name : 'check.approveText'
	}, {
		name : 'check.mrReason'
	}, {
		name : 'checkName'
	}, {
		name : 'checkDate'
	}, {
		name : 'approveName'
	}, {
		name : 'approveDate'
	}, {
		name : 'modifyName'
	}, {
		name : 'modifyDate'
	}, {
		name : 'check.approveStatus'
	}, {
		name : 'measureName'
	}])

	var dataProxy = new Ext.data.HttpProxy({
		url : 'security/getSafeCheckupList.action'
	});

	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	}, headInfo);
	var headStore = new Ext.data.Store({
		proxy : dataProxy,
		reader : theReader
	});

	var pageBar = new Ext.PagingToolbar({
		pageSize : Constants.PAGE_SIZE,
		store : headStore,
		displayInfo : true,
		displayMsg : Constants.DISPLAY_MSG,
		emptyMsg : Constants.EMPTY_MSG
	});

	var headsm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true
	});

	var headGrid = new Ext.grid.GridPanel({
		region : "center",
		layout : 'fit',
		height : 200,
		autoSizeColumns : true,
		border : false,
		enableColumnMove : false,
		// 单选
		sm : headsm,
		store : headStore,
		columns : [new Ext.grid.RowNumberer({
			header : "行号",
			width : 35
		}), {
			header : "ID",
			width : 60,
			sortable : true,
			// hidden : true,
			dataIndex : 'check.checkupId'
		}, {
			header : "审核状态",
			width : 100,
			sortable : true,
			dataIndex : 'check.approveStatus',
			renderer : function(v) {
				if (v == '1') {
					return "已上报";
				}
				if (v == '2') {
					return "已审核";
				}
				if (v == '3') {
					return "已退回";
				}
			}
		}, {
			header : "措施编号",
			width : 80,
			sortable : true,
			dataIndex : 'check.measureCode'
		}, {
			header : "措施名称",
			width : 80,
			sortable : true,
			dataIndex : 'measureName'
		}, {
			header : "季度",
			width : 120,
			sortable : true,
			dataIndex : 'check.season',
			renderer : function(v) {
				var numstr = "";
				if (v.substring(4, 5) == "1")
					numstr = "一季度";
				else if (v.substring(4, 5) == "2")
					numstr = "二季度";
				else if (v.substring(4, 5) == "3")
					numstr = "三季度";
				else if (v.substring(4, 5) == "4")
					numstr = "四季度";
				var string = v.substring(0, 4) + "年" + numstr;
				return string;
			}
		}, {
			header : "是否存在问题",
			dataIndex : "check.isProblem",
			sortable : true,
			width : 100,
			renderer : function(v) {
				if (v == 'Y') {
					return "是";
				}
				if (v == 'N') {
					return "否";
				}
			}
		}, {
			header : "检查人",
			width : 80,
			sortable : true,
			dataIndex : 'checkName'
		}, {
			header : "检查时间",
			width : 130,
			sortable : true,
			dataIndex : 'checkDate'
		}, {
			header : "审核人",
			width : 100,
			sortable : true,
			hidden : true,
			dataIndex : 'approveName'
		}, {
			header : "审核时间",
			width : 100,
			hidden : true,
			sortable : true,
			dataIndex : 'approveDate'
		}, {
			header : "审核意见",
			width : 100,
			sortable : true,
			hidden : true,
			dataIndex : 'approveText'
		}],
		// 分页
		bbar : pageBar
	});

	headGrid.on('rowclick', showDetail);

	var thirdLine = new Ext.Panel({
		border : false,
		layout : "column",
		anchor : '100%',
		items : [{
			columnWidth : 1,
			layout : "fit",
			border : false,
			autoScroll : false,
			items : [headGrid]
		}]
	});

	// 详细记录
	var material = Ext.data.Record.create([{
		name : 'amend.amendId'
	}, {
		name : 'amend.checkupId'
	}, {
		name : 'amend.existProblem'
	}, {
		name : 'amend.amendMeasure'
	}, {
		name : 'amend.beforeAmendMeasure'
	}, {
		name : 'planFinishDate'
	}, {
		name : 'amendFinishDate'
	}, {
		name : 'chargeDeptName'
	}, {
		name : 'chargeName'
	}, {
		name : 'superviseDeptName'
	}, {
		name : 'superviseName'
	}, {
		name : 'amend.noAmendReason'
	}, {
		name : 'amend.problemKind'
	}]);

	var materialStore = new Ext.data.JsonStore({
		url : 'security/getSafeAmendList.action',
		root : 'list',
		totalProperty : 'totalCount',
		fields : material
	});

	var materialGrid = new Ext.grid.GridPanel({
		region : "center",
		border : false,
		autoScroll : true,
		enableColumnMove : false,
		// 单选
		sm : new Ext.grid.RowSelectionModel({
			singleSelect : true
		}),
		// 单击修改
		store : materialStore,
		columns : [new Ext.grid.RowNumberer({
			header : "行号",
			width : 35
		}), {
			header : "明细ID",
			width : 100,
			sortable : true,
			hidden : true,
			dataIndex : 'amend.amendId'
		}, {
			header : "检查ID",
			width : 100,
			sortable : true,
			hidden : true,
			dataIndex : 'amend.checkupId'
		}, {
			header : "存在的问题",
			width : 100,
			sortable : true,
			dataIndex : 'amend.existProblem'
		}, {
			header : "问题性质",
			width : 100,
			sortable : true,
			dataIndex : 'amend.problemKind',
			renderer : function(v) {
				if (v == '1') {
					return "一般问题";
				}
				if (v == '2') {
					return "重大问题";
				}
			}
		}, {
			header : "整改措施",
			width : 100,
			sortable : true,
			dataIndex : 'amend.amendMeasure'
		}, {
			header : "整改前的防范措施",
			width : 120,
			sortable : true,
			dataIndex : 'amend.beforeAmendMeasure'
		}, {
			header : "计划完成时间",
			width : 100,
			sortable : true,
			dataIndex : 'planFinishDate'
		}, {
			header : "整改完成时间",
			width : 100,
			sortable : true,
			dataIndex : 'amendFinishDate'
		}, {
			header : "整改责任部门",
			width : 100,
			sortable : true,
			dataIndex : 'chargeDeptName'
		}, {
			header : "整改责任人",
			width : 80,
			sortable : true,
			dataIndex : 'chargeName'
		}, {
			header : "整改监督部门",
			width : 120,
			sortable : true,
			dataIndex : 'superviseDeptName'
		}, {
			header : "整改监督人",
			width : 80,
			sortable : true,
			dataIndex : 'superviseName'
		}, {
			header : "未整改原因",
			width : 100,
			sortable : true,
			dataIndex : 'amend.noAmendReason'
		}]
	});

	var fourthLine = new Ext.Panel({
		layout : "fit",
		anchor : '100%',
		autoHeight : true,
		items : [materialGrid]
	});
	// 表单panel
	var formPanel = new Ext.FormPanel({
		labelAlign : 'right',
		labelPad : 15,
		labelWidth : 75,
		border : false,
		autoHeight : true,
		region : 'north',
		items : [secondLine, thirdLine]
	});
	var queryPanel = new Ext.Panel({
		layout : 'border',
		frame : false,
		border : false,
		items : [formPanel, materialGrid]
	});
	var layout = new Ext.Viewport({
		layout : 'fit',
		margins : '0 0 0 0',
		border : false,
		items : [queryPanel]
	});

	function sign() {
		Ext.MessageBox.buttonText = {
			yes : '通过',
			no : '未通过',
			cancel : '取消'
		}
		var myMessageBox = Ext.MessageBox.show({
			title : '审核',
			msg : '是否通过？',
			modal : true,
			buttons : Ext.Msg.YESNOCANCEL,
			prompt : true,
			value : '请输入审核意见',
			fn : call,
			icon : Ext.Msg.QUESTION
		});

		function call(id, msg) {
			var record = headGrid.getSelectionModel().getSelected();
			var checkId = "";
			Ext.Ajax.request({
				url : 'security/checkApprove.action',
				params : {
					checkupId : record.get('check.checkupId'),
					buttonId : id,
					approveText : msg
				},
				success : function(result, request) {
					if (id == 'yes' || id == 'no') {
						Ext.Msg.buttonText.ok = '确定'
						Ext.Msg.show({
							title : '提示',
							msg : '数据审核成功！',
							modal : true,
							buttons : Ext.Msg.OK
						});
						materialStore.removeAll();
					} else if (id == 'cancel') {
						Ext.Msg.buttonText.ok = '确定'
						Ext.Msg.show({
							title : '提示',
							msg : '已取消审核！',
							modal : true,
							buttons : Ext.Msg.OK
						});
					}
					queryRecord();
				},
				failure : function(result, request) {
					alert('操作失败,请联系管理员!');
				}
			});
		}
	}

	queryRecord();

});