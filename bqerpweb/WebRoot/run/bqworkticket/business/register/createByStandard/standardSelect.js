Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {
	var fireLevel=window.dialogArguments.fireLevel; 
	var typeCode=window.dialogArguments.workticketTypeCode; 
	
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
		//workticketTypeCbo.setValue(records[0].data.workticketTypeCode);
		workticketTypeCbo.setValue(typeCode);
		workticketTypeCbo.setDisabled(true);
		
	});


	// 工作票种类下拉框
	var workticketTypeCbo = new Ext.form.ComboBox({
		allowBlank : true,
		triggerAction : 'all',
		store : workticketTypeStore,
		resizable : true,
		displayField : 'workticketTypeName',
		valueField : 'workticketTypeCode',
		mode : 'local',
		readOnly : true,
		width : 150
	});
	var equBlockCbo = new Ext.form.TextField({
		name : 'equBlockCbo',
		width : 200
	});
	//add by fyyang 090512 工作内容		
	var txtContent = new Ext.form.TextField({
		name : 'txtContent',
		width : 220
	});
	var txtNoContent = new Ext.form.TextField({
		name : 'txtNoContent',
		width : 150
	});

	// 查询按钮
	var queryBtn = new Ext.Button({
		id : 'query',
		text : Constants.BTN_QUERY,
		iconCls : Constants.CLS_QUERY,
		handler : function() {

			Ext.Msg.wait("正在加载数据，请等待...");
			runGridStore.baseParams = {
				startDate : "",
				endDate : "",
				workticketTypeCode : typeCode,
				block : equBlockCbo.value,
				newOrOld : newOrOldValue,
				content : txtContent.getValue(),
				ticketNo : txtNoContent.getValue(),
				fireLevel:fireLevel
			};
			runGridStore.load({
				params : {
					start : 0,
					limit : Constants.PAGE_SIZE
				}
			});

		}
	});

	// grid中的数据
	var runGridList = new Ext.data.Record.create([{
		name : 'model.workticketNo'
	}, {
		name : 'model.workticketTypeCode'
	}, {
		name : 'model.mainEquName'
	}, {
		name : 'model.mainEquCode'
	}, {
		name : 'model.sourceId'
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
			url : 'workticket/getStandListForSelect.action'
		}),
		reader : new Ext.data.JsonReader({
			root : 'list',
			totalProperty : 'totalCount'
		}, runGridList)
	})
	runGridStore.setDefaultSort('model.workticketNo', 'asc');

	runGridStore.on("load", function() {
		Ext.Msg.hide();
	});

	// 选择列
	var smCSM = new Ext.grid.CheckboxSelectionModel({
		header : "选择",
		id : "check",
		width : 35,
		singleSelect : true
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
			inputValue : "new"
				//			, // 提交时传送的参数值

				}), new Ext.form.Radio({
					columnWidth : 0.5,
					boxLabel : "老系统数据",
					name : "newOrOld",
					inputValue : "old",
					listeners : {
						check : function(checkbox, checked) { // 选中时,调用的事件 
							newOrOldValue = getSelectValue();
							Ext.get('query').dom.click();
							//					  loadStore();
						}
					}
				})]
	});
	// 运行执行的Grid主体
	var runGrid = new Ext.grid.GridPanel({
		renderTo : 'mygrid',
		store : runGridStore,
		sm : smCSM,
		region : 'center',
		layout : 'fit',
		columns : [{
			header : '工作票编号',
			width : 120,
			sortable : true,
			dataIndex : 'model.workticketNo'
		}, {
			header : '主设备名称',
			width : 80,
			sortable : true,
			dataIndex : 'model.mainEquName'
		}, {
			header : '来源',
			width : 100,
			sortable : true,
			dataIndex : 'sourceName'
		}, {
			header : '所属系统',
			width : 80,
			sortable : true,
			dataIndex : 'blockName'
		}, {
			header : '工作内容',
			width : 320,
			sortable : true,
			dataIndex : 'model.workticketContent',
			renderer : function(value) {
				if (value) {
					return value.replace(/\n/g, '<br/>');
				}
				return '';
			}
		}, {
			header : '状态',
			width : 100,
			sortable : true,
			dataIndex : 'statusName'
		}],
		/*tbar : [newOrOld, {
			xtype : "tbseparator"
		}, '类型：', workticketTypeCbo, {
			xtype : "tbseparator"
		}, '主设备名称：', equBlockCbo],*/
		tbar : [newOrOld, {
			xtype : "tbseparator"
		}, '类型：', workticketTypeCbo, {
			xtype : "tbseparator"
		}],
		//分页
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

	//================add by drdu 091103   start====================
	var printBtn = new Ext.Button({
		text : '票面浏览',
		iconCls : 'pdfview',
		handler : function() {
			var record = runGrid.getSelectionModel().getSelected();
			if (record) {
				workticketPrint(record);
			} else {
				Ext.Msg.alert("提示", "请选择一条记录!");
			}
		}
	});

	function workticketPrint(menber) {
		var workticketNo;
		var workticketCode;
		var fireLevelId;
		workticketNo = menber.get('model.workticketNo');
		workticketCode = menber.get('model.workticketTypeCode');
		fireLevelId = menber.get('model.firelevelId');

		var strReportAdds = getReportUrl(workticketCode, workticketNo,
				fireLevelId);

		if (strReportAdds != "") {
			window.open(strReportAdds);
		} else {
			Ext.Msg.alert("目前没有该种工作票票面预览");
		}
	}

	//========================end================================
	new Ext.Toolbar({
		renderTo : runGrid.tbar

		,
		items : ['工作内容：', txtContent,'-','标准票号',txtNoContent, {
			xtype : "tbseparator"
		}, queryBtn, '<font color="red">(双击选择标准票)</font>', "-", printBtn]
	});

	runGrid.on("rowdblclick", returnValues);

	// 返回数据
	function returnValues() {
		if (runGrid.selModel.hasSelection()) {
			var smodel = runGrid.getSelectionModel();
			var selected = smodel.getSelections();
			if (selected.length == 0) {
				Ext.Msg.alert(Constants.NOTICE, Constants.SELECT_NULL_DEL_MSG);
			} else {
				var member = selected[0];
				var object = new Object();
				// 返回工作票编号
				object.standticketNo = member.get('model.workticketNo');
				object.workticketTypeCode = member
						.get('model.workticketTypeCode');
				object.sourceId = member.get('model.sourceId');

				var args = window.dialogArguments;
				Ext.Msg.confirm(Constants.SYS_REMIND_MSG, '是否确定选择工作票编号为'
						+ object.standticketNo + '的票', function(buttonobj) {
					if (buttonobj == "yes") {
						// 返回工作票编号
						window.returnValue = object;
						window.close();

					}
				});
			}
		}
	}

	// 设定布局器及面板
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		items : [runGrid]
	});
	//    var args = window.dialogArguments; 
	//    equBlockCbo.setValue(args.mainEquName);
	// 初始化时,显示所有数据

	
	runGridStore.baseParams = {
		startDate : "",
		endDate : "",
		workticketTypeCode : typeCode,
		//workticketStatusId : workticketStatusCbo.value,
		block : equBlockCbo.value,
		newOrOld : newOrOldValue,
		content : txtContent.getValue(),
		ticketNo : txtNoContent.getValue(),
		fireLevel:fireLevel
	};
	runGridStore.load({
		params : {
			start : 0,
			limit : Constants.PAGE_SIZE
		}
	});
});