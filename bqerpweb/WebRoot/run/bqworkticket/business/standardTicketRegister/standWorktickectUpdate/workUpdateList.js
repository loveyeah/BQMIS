Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	// 分页时每页显示记录条数
	var PAGE_SIZE = Constants.PAGE_SIZE;
	// 页面跳转时使用
	var register = parent.Ext.getCmp('tabPanel').register;
	// 数据变更时更新Grid中的数据
	register.workticketChangeHandler = gridReload;
	// ↓↓********** 主画面*******↓↓//

	var newOrOldValue = 'new';
	// 工作票类型下拉框数据源
	var workticketTypeStore = new Ext.data.JsonStore({
		url : 'workticket/getQueryWorkticketType.action',
		root : 'list',
		fields : [{
			name : 'workticketTypeName'
		}, {
			name : 'workticketTypeCode'
		}]
	})
	workticketTypeStore.load();
	workticketTypeStore.on('load', function(e, records, o) {
		workticketTypeCbo.setValue(records[0].data.workticketTypeCode);
	});

	// 工作票种类下拉框
	var workticketTypeCbo = new Ext.form.ComboBox({
		allowBlank : true,
		triggerAction : 'all',
		store : workticketTypeStore,
		displayField : 'workticketTypeName',
		valueField : 'workticketTypeCode',
		mode : 'local',
		readOnly : true,
		width : 130
	})
	// 所属机组或系统下拉框数据源
	var equBlockStore = new Ext.data.JsonStore({
		url : 'workticket/getQueryEquBlock.action',
		root : 'list',
		fields : [{
			name : 'blockName'
		}, {
			name : 'blockCode'
		}]
	})
	equBlockStore.load();
	equBlockStore.on('load', function(e, records, o) {
		equBlockCbo.setValue(records[0].data.blockCode);
	});

	// 所属机组或系统下拉框
	var equBlockCbo = new Ext.form.ComboBox({
		allowBlank : true,
		triggerAction : 'all',
		store : equBlockStore,
		displayField : 'blockName',
		valueField : 'blockCode',
		mode : 'local',
		readOnly : true,
		width : 140
	})
	// 检修专业
	var storeRepairSpecail = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'workticket/getDetailRepairSpecialityType.action'
		}),
		reader : new Ext.data.JsonReader({
			root : 'list'
		}, [{
			name : 'specialityCode',
			mapping : 'specialityCode'
		}, {
			name : 'specialityName',
			mapping : 'specialityName'
		}])
	});
	storeRepairSpecail.load();
	var cbxRepairSpecail = new Ext.form.ComboBox({
		id : 'repairSpecailCode',
		fieldLabel : "检修专业:",
		store : storeRepairSpecail,
		displayField : "specialityName",
		valueField : "specialityCode",
		hiddenName : 'repairCode',
		mode : 'local',
		triggerAction : 'all',
		value : '',
		readOnly : true,
		width : 90,
		listeners : {
			select : function() {
				loadStore();
			}
		}
	});

	storeRepairSpecail.on('load', function(e, records) {
		var myrecord = new Ext.data.Record({
			specialityName : '所有',
			specialityCode : ''
		});
		storeRepairSpecail.insert(0, myrecord);
		cbxRepairSpecail.setValue('');
	});

	// 查询按钮
	var queryBtn = new Ext.Button({
		id : 'query',
		text : Constants.BTN_QUERY,
		iconCls : Constants.CLS_QUERY,
		handler : function() {
			loadStore();
		}
	});
	

	// grid中的数据
	var runGridList = new Ext.data.Record.create([{
		name : 'model.workticketNo'
	}, {
		name : 'model.workticketTypeCode'
	}, {
		name : 'model.workFlowNo'
	}, {
		name : 'model.mainEquName'
	}, {
		name : 'model.mainEquCode'
	}, {
		name : 'statusName'
	}, {
		name : 'sourceName'
	}, {
		name : 'chargeByName'
	}, {
		name : 'deptName'
	}, {
		name : 'planStartDate'
	}, {
		name : 'planEndDate'
	}, {
		name : 'blockName'
	}, {
		name : 'model.workticketContent'
	}, {
		name : 'isEmergencyText'
	}, {
		name : 'model.firelevelId'
	}, {
		name : 'model.entryBy'
	}, {
		name : 'repairSpecailName'
	}])

	// grid中的store
	var runGridStore = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'workticket/getQueryStandticketList.action'
		}),
		reader : new Ext.data.JsonReader({
			root : 'list',
			totalProperty : 'totalCount'
		}, runGridList)
	})
	runGridStore.setDefaultSort('model.workticketNo', 'asc');
//	runGridStore.on("beforeload", function() {
//		Ext.Msg.wait("正在查询数据,请等待...");
//	});
//	runGridStore.on("load", function() {
//		Ext.Msg.hide();
//	});
	var smCSM = new Ext.grid.CheckboxSelectionModel({
		header : "选择",
		id : "check",
		width : 35,
		singleSelect : true
	});

	// ----add by fyyang - 090318----

	var expander = new Ext.grid.RowExpander({
		tpl : new Ext.Template(
				// '<p><b>工作票号:</b> </p><br>',
				'<p><b>工作内容：</b></p>'),
		expandRow : mygridExpend
	});
	function mygridExpend(row) {
		if (typeof row == 'number') {
			row = this.grid.view.getRow(row);
		}
		var record = this.grid.store.getAt(row.rowIndex);
		var no = record.get("model.workticketNo");
		var content = "";
		// -----------------------
		Ext.Ajax.request({
			url : 'workticket/getContentWorkticketByNo.action',
			params : {
				workticketNo : no
			},
			method : 'post',
			success : function(result, request) {
				var content = result.responseText;
				content = content.substring(1, content.length - 1);
				while (content.indexOf('\\r') != -1) {

					content = content.replace("\\r", "");
				}
				while (content.indexOf('\\n') != -1) {
					content = content.replace("\\n", "<br>");
				}
				expander.tpl = new Ext.Template('<p> <br><font color="blue"><b>工作内容：</b><br>'
						+ content + '</font></p>');
				var body = Ext.DomQuery.selectNode(
						'tr:nth(2) div.x-grid3-row-body', row);
				if (expander.beforeExpand(record, body, row.rowIndex)) {
					expander.state[record.id] = true;
					Ext.fly(row).replaceClass('x-grid3-row-collapsed',
							'x-grid3-row-expanded');
					expander.fireEvent('expand', expander, record, body,
							row.rowIndex);
				}

			},
			failure : function(result, request) {
				Ext.MessageBox.alert('错误', '操作失败!');
			}
		});

	}

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
					loadStore();
				}
			}
		}), new Ext.form.Radio({
			columnWidth : 0.5,
			boxLabel : "老系统数据",
			name : "newOrOld",
			inputValue : "old"
		})]
	});
	// add by fyyang 090513 工作内容
	var txtContent = new Ext.form.TextField({
		name : 'txtContent',
		width : 306
	});
	// -------------------------------------------------
	// 运行执行的Grid主体
	var runGrid = new Ext.grid.GridPanel({
		renderTo : 'mygrid',
		store : runGridStore,
		sm : smCSM,
		region : 'center',
		layout : 'fit',
		// viewConfig : {
		// forceFit : true
		// },
		columns : [{
			header : '工作票编号',
			width : 200,
			sortable : true,
			dataIndex : 'model.workticketNo'
		}, {
			header : "工作内容",
			width : 300,
			sortable : true,
			// hidden:true,
			dataIndex : 'model.workticketContent',
			renderer : function(value, metadata, record) {
				metadata.attr = 'style="white-space:normal;"';
				return value;
			}
		}, {
			header : '主设备名称',
			width : 150,
			sortable : true,
			dataIndex : 'model.mainEquName',
			renderer : function(value, metadata, record) {
				metadata.attr = 'style="white-space:normal;"';
				return value;
			}
		}, {
			header : '状态',
			width : 100,
			sortable : true,
			dataIndex : 'statusName'
		}, {
			header : '来源',
			width : 100,
			sortable : true,
			dataIndex : 'sourceName'
		}, {
			header : '工作负责人',
			width : 70,
			sortable : true,
			dataIndex : 'chargeByName',
			hidden : true
		}, {
			header : '所属部门',
			width : 75,
			sortable : true,
			dataIndex : 'deptName',
			hidden : true
		}, {
			header : '计划开始时间',
			width : 110,
			sortable : true,
			dataIndex : 'planStartDate',
			hidden : true
		}, {
			header : '计划结束时间',
			width : 110,
			sortable : true,
			dataIndex : 'planEndDate',
			hidden : true
		}, {
			header : '所属系统',
			width : 80,
			sortable : true,
			dataIndex : 'blockName'
		}, {
			header : '工作内容',
			width : 320,
			sortable : true,
			hidden : true,
			dataIndex : 'model.workticketContent',
			renderer : function(value) {
				if (value) {
					return value.replace(/\n/g, '<br/>');
				}
				return '';
			}
		}, {
			header : '是否紧急票',
			width : 70,
			sortable : true,
			dataIndex : 'isEmergencyText',
			hidden : true
		}, {
			header : "检修专业",
			width : 70,
			sortable : true,
			dataIndex : 'repairSpecailName'
		}, {
			header : "填写人",
			width : 70,
			sortable : true,
			dataIndex : 'model.entryBy'
		}],
		tbar : ['类型：', workticketTypeCbo, {
			xtype : "tbseparator"
		}, '所属机组或系统：', equBlockCbo, {
			xtype : "tbseparator"
		}, newOrOld, '检修专业:', cbxRepairSpecail, {
			xtype : "tbseparator"
		}],
		// 分页
		bbar : new Ext.PagingToolbar({
			pageSize : Constants.PAGE_SIZE,
			store : runGridStore,
			displayInfo : true,
			displayMsg : Constants.DISPLAY_MSG,
			emptyMsg : Constants.EMPTY_MSG
		}),
		frame : false,
		border : false,
		enableColumnHide : true,
		enableColumnMove : false,
		autoWidth : true
	});

	new Ext.Toolbar({
		renderTo : runGrid.tbar,
		items : ['工作内容：', txtContent, '-', queryBtn,'-', {
					id : 'btnUpdate',
					text : "修改",
					iconCls : 'update',
					handler : modifyBtn
				}]
	});

	runGrid.on("rowdblclick", modifyBtn);
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		items : [runGrid]
	});
	var register = parent.Ext.getCmp('tabPanel').register;
	function loadStore() {
		runGridStore.baseParams = {
			startDate : "",
			endDate : "",
			newOrOld : newOrOldValue,
			workticketTypeCode : workticketTypeCbo.value,
			workticketStatusId : null,
			block : equBlockCbo.value,
			repairC : cbxRepairSpecail.getValue(),
			entryBy : "",
			content : txtContent.getValue()
		};
		runGridStore.load({
			params : {
				start : 0,
				limit : Constants.PAGE_SIZE
			}
		});
	}
	function gridReload() {
		loadStore()
	}

	// 修改处理
	function modifyBtn() {
		var sm = runGrid.getSelectionModel();
		var selected = sm.getSelections();
		var workticketNo;
		if (selected.length == 0) {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG,
					Constants.SELECT_NULL_UPDATE_MSG);
		} else {
			var menber = selected[0];
			workticketNo = menber.get('model.workticketNo');
			register.workFlowNo = menber.get('model.workFlowNo');
			register.firelevelId = menber.get('model.firelevelId');
			// 编辑工作票信息
			register.edit(workticketNo);
		}
	}
	// 票面浏览
	function workticketPrint() {
		var sm = runGrid.getSelectionModel();
		var selected = sm.getSelections();
		var workticketNo;
		var workticketCode;
		if (selected.length == 0) {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG,
					Constants.SELECT_NULL_REPORT_MSG);
		} else {

			var menber = selected[0];
			workticketNo = menber.get('model.workticketNo');
			workticketCode = menber.get('model.workticketTypeCode');
			var fireLevelId = menber.get('model.firelevelId');
			var strReportAdds = getReportUrl(workticketCode, workticketNo,
					fireLevelId);

			if (strReportAdds != "") {
				window.open(strReportAdds);
			} else {
				Ext.Msg.alert('提示', "目前没有该种工作票票面预览");
			}

		}
	}
	
});