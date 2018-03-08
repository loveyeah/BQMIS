Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {

	// 系统当天日期
	var sd = new Date();
	var ed = new Date();
	// 系统当天前30天的日期
	sd.setDate(sd.getDate() - 30);
	// 系统当天后30天的日期
	ed.setDate(ed.getDate() + 30);

	// 开始时间选择
	var startDate = new Ext.form.DateField({
		id : 'startDate',
		width : 100,
		allowBlank : true,
		readOnly : false,
		value : sd,
		format : 'Y-m-d'
	})
	startDate.setValue(sd);
	startDate.on('change', checkDate);

	// 结束时间选择
	var endDate = new Ext.form.DateField({
		id : 'endDate',
		width : 100,
		allowBlank : true,
		readOnly : false,
		value : ed,
		format : 'Y-m-d'
	})
	endDate.setValue(ed);
	endDate.on('change', checkDate);

	// 检查时间输入
	function checkDate() {
		var strStartDate = Ext.get("startDate").dom.value;
		var strEndDate = Ext.get("endDate").dom.value;
		if ((strStartDate.length == 10 || strStartDate.length == 0)
				&& (strEndDate.length == 10 || strEndDate.length == 0)) {
			if ((strStartDate.length == 0) && (strEndDate.length == 0)) {
				startDate.setValue("");
				endDate.setValue("");
				return true;
			} else if (strStartDate.length == 0) {
				startDate.setValue("");
				if ((Date.parseDate(strStartDate, 'Y-m-d')) != "undefined") {
					return true;
				} else {
					Ext.Msg.alert(Constants.NOTICE, "日期格式不正确！");
					return false;
				}
			} else if (strEndDate.length == 0) {
				endDate.setValue("");
				if ((Date.parseDate(strEndDate, 'Y-m-d')) != "undefined") {
					return true;
				} else {
					Ext.Msg.alert(Constants.NOTICE, "日期格式不正确！");
					return false;
				}
			} else {
				var dateStart = Date.parseDate(strStartDate, 'Y-m-d');
				var dateEnd = Date.parseDate(strEndDate, 'Y-m-d');
				if (dateStart != "undefined" && dateEnd != "undefined") {
					if (dateStart.getTime() > dateEnd.getTime()) {
						Ext.Msg.alert(Constants.NOTICE, "开始时间必须小于结束时间！");
						return false;
					} else {
						return true;
					}
				} else {
					Ext.Msg.alert(Constants.NOTICE, "日期格式不正确！");
					return false;
				}
			}
		} else {
			Ext.Msg.alert(Constants.NOTICE, "日期格式不正确！");
			return false;
		}
	}

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
		width : 150
	})

	// 工作票状态下拉框数据源
	var workticketStatusStore = new Ext.data.JsonStore({
		url : 'workticket/getQueryWorkticketStatus.action',
		root : 'list',
		fields : [{
			name : 'workticketStatusName'
		}, {
			name : 'workticketStausId'
		}]
	}) 
	workticketStatusStore.load(); 
	workticketStatusStore.on('load', function(e, records, o) {
		for(var i =0;i<workticketStatusStore.getCount();i++ )
		{
			var temp = workticketStatusStore.getAt(i);
			var id = temp.get("workticketStausId") ; 
			if(id == "1" || id == "8" || id == "14")
			{ 
				workticketStatusStore.remove(temp);
			} 
		}   
		workticketStatusCbo.setValue(18);  
	});

	// 工作票状态下拉框
	var workticketStatusCbo = new Ext.form.ComboBox({
		allowBlank : true,
		triggerAction : 'all',
		store : workticketStatusStore,
		displayField : 'workticketStatusName',
		valueField : 'workticketStausId',
		mode : 'local',
		readOnly : true,
		width : 150,
		listeners:{
			'select' : function(){
				Ext.Msg.wait("正在查询,请等待...");
			 	runGridStore.baseParams = {
					startDate : startDate.value,
					endDate : endDate.value,
					workticketTypeCode : workticketTypeCbo.value,
					workticketStatusId : workticketStatusCbo.value,
					block : equBlockCbo.value
				};
				runGridStore.load({
					params : {
						start : 0,
						limit : Constants.PAGE_SIZE
					}
				});	
				Ext.Msg.hide();
			}
		}
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
		width : 120
	})

	// 查询按钮
	var queryBtn = new Ext.Button({
		id : 'query',
		text : Constants.BTN_QUERY,
		iconCls : Constants.CLS_QUERY,
		handler : function() {
			if (checkDate()) {
				runGridStore.baseParams = {
					startDate : startDate.value,
					endDate : endDate.value,
					workticketTypeCode : workticketTypeCbo.value,
					workticketStatusId : workticketStatusCbo.value,
					block : equBlockCbo.value
				};
				runGridStore.load({
					params : {
						start : 0,
						limit : Constants.PAGE_SIZE
					}
				});
			}
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
	}])

	// grid中的store
	var runGridStore = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'workticket/getQueryWorkticketList.action'
		}),
		reader : new Ext.data.JsonReader({
			root : 'list',
			totalProperty : 'totalCount'
		}, runGridList)
	})
	runGridStore.setDefaultSort('model.workticketNo', 'asc');

	// 初始化时,显示所有数据
	runGridStore.baseParams = {
		startDate : startDate.value,
		endDate : endDate.value,
		workticketTypeCode : workticketTypeCbo.value,
		workticketStatusId : workticketStatusCbo.value==null?18:workticketStatusCbo.value,
		block : equBlockCbo.value
	};
	 
	runGridStore.load({
		params : { 
			start : 0,
			limit : Constants.PAGE_SIZE
		}
	});
	// 选择列
	var smCSM = new Ext.grid.CheckboxSelectionModel({
		header : "选择",
		id : "check",
		width : 35,
		singleSelect : true
	});

	// ----add by fyyang - 090311----

	var expander = new Ext.grid.RowExpander({
		tpl : new Ext.Template('<p><b>工作内容：</b></p>'),
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
		var conn = Ext.lib.Ajax.getConnectionObject().conn;
		conn
				.open("POST",
						'workticket/getContentWorkticketByNo.action?workticketNo='
								+ no, false);
		conn.send(null);

		// 成功状态码为200
		if (conn.status == "200") {
			var result = conn.responseText;
			content = result.substring(1, result.length - 1);
			while (content.indexOf('\\r') != -1) {

				content = content.replace("\\r", "");
			}
			while (content.indexOf('\\n') != -1) {
				content = content.replace("\\n", "<br>");
			}

		}

		expander.tpl = new Ext.Template('<p> <br><font color="blue"><b>工作内容：</b><br>'
				+ content + '</font></p>');
		var body = Ext.DomQuery.selectNode('tr:nth(2) div.x-grid3-row-body',
				row);
		if (this.beforeExpand(record, body, row.rowIndex)) {
			this.state[record.id] = true;
			Ext.fly(row).replaceClass('x-grid3-row-collapsed',
					'x-grid3-row-expanded');
			this.fireEvent('expand', this, record, body, row.rowIndex);
		}

	}
	var gridToolBar = new Ext.Toolbar({
		items : ['工作票状态：', workticketStatusCbo, '-', queryBtn, '', {
			text : '确定',
			iconCls : 'confirm',
			xtype : 'button',
			handler : function() {
				chooseWorkticket();
			}
		}]
	});
	function chooseWorkticket() {
		var rec = runGrid.getSelectionModel().getSelections();
		if (rec.length > 0) {
			var code = rec[0].get('model.workticketNo');
			window.returnValue = code;
			window.close();
		} else {
			Ext.Msg.alert('提示', '请选择工作票！');
		}
	}
	var columnModel = new Ext.grid.ColumnModel([expander, {
		header : '工作票编号',
		width : 150,
		sortable : true,
		dataIndex : 'model.workticketNo'
	}, {
		header : '状态',
		width : 150,
		sortable : true,
		dataIndex : 'statusName'
	}, {
		header : '所属系统',
		width : 120,
		sortable : true,
		dataIndex : 'blockName'
	}, {
		header : '来源',
		width : 100,
		sortable : true,
		dataIndex : 'sourceName'
	}, {
		header : '工作负责人',
		width : 70,
		sortable : true,
		dataIndex : 'chargeByName'
	}, {
		header : '所属部门',
		width : 100,
		sortable : true,
		dataIndex : 'deptName'
	}, {
		header : '计划开始时间',
		width : 110,
		sortable : true,
		dataIndex : 'planStartDate'
	}, {
		header : '计划结束时间',
		width : 110,
		sortable : true,
		dataIndex : 'planEndDate'
	}, {
		header : '是否紧急票',
		width : 70,
		align : 'center',
		sortable : true,
		dataIndex : 'isEmergencyText'
	}]);
	// ---------------------------
	// 运行执行的Grid主体

	var runGrid = new Ext.grid.GridPanel({
		store : runGridStore,
		sm : smCSM,
		region : 'center',
		layout : 'fit',
		frame : false,
		border : false,
		enableColumnHide : true,
		enableColumnMove : false,
		autoWidth : true,
		plugins : expander,
		collapsible : true,
		animCollapse : false,
		tbar : gridToolBar,
//		viewConfig : {
//			forceFit : true
//		},
		cm : columnModel,
		bbar : new Ext.PagingToolbar({
			pageSize : Constants.PAGE_SIZE,
			store : runGridStore,
			displayInfo : true,
			displayMsg : Constants.DISPLAY_MSG,
			emptyMsg : Constants.EMPTY_MSG
		})
	});
	runGrid.on('rowdblclick', function() {
		chooseWorkticket();
	})
	var headTbar = new Ext.Toolbar({
		region : 'north',
		items : ['时间范围：', startDate, '~', endDate, '-', '工作票类型：',
				workticketTypeCbo, '-', '所属机组或系统：', equBlockCbo]

	});
	// 设定布局器及面板
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		items : [headTbar, runGrid]
	});

});