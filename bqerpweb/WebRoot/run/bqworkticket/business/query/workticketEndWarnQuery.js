Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {

	// 获得当前日期
	function getDate() {
		var date, value, strTemp;
		date = new Date();
		value = date.getFullYear().toString(10) + "-";
		strTemp = date.getMonth() + 1;
		value += (strTemp > 9 ? "" : "0") + strTemp + "-";
		strTemp = date.getDate();
		value += (strTemp > 9 ? "" : "0") + strTemp;
		return value;
	}

	// 工作票类型下拉框数据源
	var workticketTypeStore = new Ext.data.JsonStore({
		url : 'workticket/getEndWarnWorkticketType.action',
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

	// 工作票种类下拉框 modify by drdu 090327
	var workticketTypeCbo = new Ext.form.ComboBox({
		allowBlank : true,
		triggerAction : 'all',
		store : workticketTypeStore,
		displayField : 'workticketTypeName',
		valueField : 'workticketTypeCode',
		mode : 'local',
		readOnly : true,
		width : 120
	})

	// 运行专业下拉框数据源
	var runSpecialityStore = new Ext.data.JsonStore({
		url : 'workticket/getEndWarnRunSpeciality.action',
		root : 'list',
		fields : [{
			name : 'specialityName'
		}, {
			name : 'specialityCode'
		}]
	})
	runSpecialityStore.load({
		params : {
			// "1"表示运行专业
			specialityType : "1"
		}
	});
	runSpecialityStore.on('load', function(e, records, o) {
		runSpecialityCbo.setValue(records[0].data.specialityCode);
	});

	// 运行专业下拉框
	var runSpecialityCbo = new Ext.form.ComboBox({
		allowBlank : true,
		triggerAction : 'all',
		store : runSpecialityStore,
		displayField : 'specialityName',
		valueField : 'specialityCode',
		mode : 'local',
		readOnly : true,
		width : 160
	})

	// 检修专业下拉框数据源
	var repairSpecialityStore = new Ext.data.JsonStore({
		url : 'workticket/getEndWarnRunSpeciality.action',
		root : 'list',
		fields : [{
			name : 'specialityName'
		}, {
			name : 'specialityCode'
		}]
	})
	repairSpecialityStore.load({
		params : {
			// "2"表示检修专业
			specialityType : "2"
		}
	});
	repairSpecialityStore.on('load', function(e, records, o) {
		repairSpecialityCbo.setValue(records[0].data.specialityCode);
	});

	// 检修专业下拉框 modify by drdu 090327
	var repairSpecialityCbo = new Ext.form.ComboBox({
		allowBlank : true,
		triggerAction : 'all',
		store : repairSpecialityStore,
		displayField : 'specialityName',
		valueField : 'specialityCode',
		mode : 'local',
		readOnly : true,
		width : 130
	})

	// 查询按钮
	var queryBtn = new Ext.Button({
		id : 'query',
		text : Constants.BTN_QUERY,
		iconCls : Constants.CLS_QUERY,
		handler : myquery
	});
	function myquery() {
		runGridStore.baseParams = {
			workticketTypeCode : workticketTypeCbo.value,
			runSpeciality : runSpecialityCbo.value,
			repairSpeciality : repairSpecialityCbo.value
		};
		runGridStore.load();
	}

	// grid中的数据
	var runGridList = new Ext.data.Record.create([{
		name : 'model.workticketNo'
	}, {
		name : 'model.workticketContent'
	}, {
		name : 'chargeByName'
	}, {
		name : 'deptName'
	}, {
		name : 'planStartDate'
	}, {
		name : 'planEndDate'
	}, {
		name : 'approveEndDate'
	}, {
		name : 'delayToDate'
	}, {
		name : 'blockName'
	}, {
		name : 'statusName'
	}])

	// grid中的store
	var runGridStore = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'workticket/getEndWarnWorkticketList.action'
		}),
		reader : new Ext.data.JsonReader({
			root : 'list',
			totalProperty : 'totalCount'
		}, runGridList)
	})
	runGridStore.setDefaultSort('model.workticketNo', 'asc');

	// 初始化时,显示所有数据
	runGridStore.baseParams = {
		workticketTypeCode : "%",
		runSpeciality : "%",
		repairSpeciality : "%"
	};
	runGridStore.load();

	// ----add by fyyang - 090311----

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
		// add by sltang
		var opCode = "";
		Ext.Ajax.request({
			url : 'workticket/getOptickectCode.action',
			params : {
				workticketNo : no
			},
			method : 'post',
			success : function(result, request) {
				opCode = result.responseText;
			},
			failure : function(result, request) {
				Ext.MessageBox.alert('错误', '操作失败!');
			}
		});
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
				expander.tpl = new Ext.Template(
						'<p> <br><font color="blue"><b>工作内容：</b><br>' + content
								+ '</font></p>', '<p><br><font color="blue"><b>操作票号:</b><br>'+opCode+'</font></p>');
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
	// ---------------------------
	// 运行执行的Grid主体
	var runGrid = new Ext.grid.GridPanel({
		region : 'center',
		layout : 'fit',
		viewConfig : {
			forceFit : true
		},
		store : runGridStore,
		// title : '工作票终结预警查询 (' + getDate() + ")",
		columns : [ {
			header : '工作票编号',
			width : 200,
			sortable : true,
			dataIndex : 'model.workticketNo',
			renderer : showColor
		}, {
			header : "工作内容",
			width : 300,
			sortable : true,
			//hidden:true,
			dataIndex : 'model.workticketContent',
			renderer:function(value, metadata, record){ 
			metadata.attr = 'style="white-space:normal;"'; 
			return value;  
	}
	}, {
			header : '工作负责人',
			width : 100,
			sortable : true,
			dataIndex : 'chargeByName',
			renderer : showColor
		}, {
			header : '所属部门',
			width : 100,
			sortable : true,
			dataIndex : 'deptName',
			renderer : showColor
		}, {
			header : '计划开始时间',
			width : 100,
			sortable : true,
			dataIndex : 'planStartDate',
			renderer : showColor
		}, {
			header : '计划结束时间',
			width : 140,
			sortable : true,
			dataIndex : 'planEndDate',
			renderer : showColor
		}, {
			header : '批准结束时间',
			width : 140,
			sortable : true,
			dataIndex : 'approveEndDate',
			renderer : showColor
		}, {
			header : '有效延期时间',
			width : 140,
			sortable : true,
			dataIndex : 'delayToDate',
			renderer : showColor
		}, {
			header : '所属系统',
			width : 100,
			sortable : true,
			dataIndex : 'blockName',
			renderer : showColor
		}],
		tbar : ['工作票类型：', workticketTypeCbo, {
			xtype : "tbseparator"
		}, '运行专业：', runSpecialityCbo, {
			xtype : "tbseparator"
		}, '检修专业：', repairSpecialityCbo, {
			xtype : "tbseparator"
		}, queryBtn],
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

	var headTbar = new Ext.Toolbar({
		region : 'north',
		items : [
				'工作票终结预警查询  (' + getDate() + ')',
				"<div>&nbsp;&nbsp;&nbsp;&nbsp;</div>",
				'颜色说明：',
				"<div>&nbsp;&nbsp;</div>",
				"<div style='width:50;color:white;  background:black'>一天内终结</div>",
				"<div>&nbsp;&nbsp;</div>",
				"<div style='width:50; color:white; background:blue'>4小时内终结</div>",
				"<div>&nbsp;&nbsp;</div>",
				"<div style='width:50; color:white; background:red'>已超时</div>"]
	});
	// 设定布局器及面板
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		items : [headTbar, runGrid]
	});

	// 根据不同的状态显示不同的颜色
	function showColor(value, cellmeta, record, rowIndex, columnIndex, store) {
		var strValue = value;
		if (columnIndex == 1) {
			if (value != null) {
				strValue = value.replace(/\r/g, "<br/>");
			} else {
				return strValue = "";
			}
		}
		// 获得当前行的工作票状态
		var strStatusName = record.data["statusName"];
		var strColor = "black";
		// 超时
		if (strStatusName == "1") {
			strColor = "red";
			// 4小时内终结
		} else if (strStatusName == "2") {
			strColor = "blue";
			// 一天内终结
		} else if (strStatusName == "3") {
			strColor = "black";
		}
		return "<font color='" + strColor + "'>" + strValue + "</font>";
	}

});