Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {
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
	var statusRecord = Ext.data.Record.create([{
		name : 'workticketStausId'
	}, {
		name : 'workticketStatusName'
	}]);
	var item = new statusRecord({
		'workticketStausId' : "1,2,3,5,6",
		'workticketStatusName' : "准标准票"
	});

	// 工作票状态下拉框数据源
	var workticketStatusStore = new Ext.data.JsonStore({
		url : 'workticket/getStandTypeForQuery.action',
		root : 'list',
		fields : statusRecord
			// [
			// {
			// name : 'workticketStatusName'},
			// {name : 'workticketStausId'}
			// ]
	})
	workticketStatusStore.load();
	workticketStatusStore.on('load', function(e, records, o) {
		workticketStatusCbo.setValue(records[0].data.workticketStausId);
		workticketStatusStore.insert(7, item);
		workticketStatusStore.getAt(4).set('workticketStatusName',"总工已审批（标准票）");
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
		width : 135,
		listeners : {
			select : function() {
				loadStore();
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
		width : 130,
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
	function loadStore() {

		runGridStore.baseParams = {
			startDate : "",
			endDate : "",
			newOrOld : newOrOldValue,
			workticketTypeCode : workticketTypeCbo.value,
			workticketStatusId : workticketStatusCbo.value,
			block : equBlockCbo.value,
			repairC : cbxRepairSpecail.getValue(),
			entryBy : txtEnterBy.getValue(),
			content : txtContent.getValue(),
			ticketNo : txtNoContent.getValue()
		};
		runGridStore.load({
			params : {
				start : 0,
				limit : Constants.PAGE_SIZE
			}
		});
	}

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
	runGridStore.on("beforeload", function() {
		Ext.Msg.wait("正在查询数据,请等待...");
	});
	runGridStore.on("load", function() {
		Ext.Msg.hide();
	});
	// // 初始化时,显示所有数据
	// runGridStore.baseParams = {
	// startDate : "",
	// endDate : "",
	// workticketTypeCode : workticketTypeCbo.value,
	// workticketStatusId : workticketStatusCbo.value,
	// block : equBlockCbo.value
	// };
	// runGridStore.load({
	// params : {
	// start : 0,
	// limit : Constants.PAGE_SIZE
	// }
	// });
	// 选择列
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
	// ---------------------------
	// ----------------删除-----add by fyyang 090421-----
	var btnDelete = new Ext.Button({
		id : "btnDelete",
		text : "删除",
		hidden : true,
		iconCls : 'delete',
		handler : function() {
			var record = runGrid.getSelectionModel().getSelected();
			if (record) {
				if (confirm("删除后数据不能恢复,确定要删除该条工作票吗?")) {
					var busiNo = record.get("model.workticketNo");
					var entryId = record.get("model.workFlowNo");
					Ext.Ajax.request({
						url : "MAINTWorkflow.do?action=mangerDelete",
						method : 'post',
						params : {
							busiType : 'workticket',
							entryId : entryId,
							busiNo : busiNo
						},
						success : function(result, request) {
							Ext.Msg.alert("提示", "操作成功!");
							runGrid.getStore().remove(record);
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
		width : 220,
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
		width :150
	});
	//
	var txtNoContent = new Ext.form.TextField({
		name : 'txtNoContent',
		width : 132
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
			//hidden:true,
			dataIndex : 'model.workticketContent',
			renderer:function(value, metadata, record){ 
			metadata.attr = 'style="white-space:normal;"'; 
			return value;  
	}
	}, {
			header : '主设备名称',
			width : 150,
			sortable : true,
			dataIndex : 'model.mainEquName',
			renderer:function(value, metadata, record){ 
			metadata.attr = 'style="white-space:normal;"'; 
			return value;  
	}
		},{
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
		}, '状态：', workticketStatusCbo],
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

	// add by fyyang 090513 填写人
	var txtEnterBy = new Ext.form.TextField({
		name : 'txtEnterBy',
		width : 125
	});

	new Ext.Toolbar({
		renderTo : runGrid.tbar,
		items : [newOrOld, '-','检修专业:', cbxRepairSpecail, {
			xtype : "tbseparator"
		}, '填写人：', txtEnterBy]
	});

	new Ext.Toolbar({
		renderTo : runGrid.tbar,
		items : ['工作内容：', txtContent, '-', '标准工作票编号:',txtNoContent,'-',queryBtn,
				'<font color="red">(右键查询相关信息)</font>', '->', btnDelete,
				'<div id="divManagerDel">工作票列表</div>']
	});

	runGrid.on("rowdblclick", gridDbl);

	function gridDbl() {
		var record = runGrid.getSelectionModel().getSelected();
		workticketPrint(record);
	}
	runGrid.on("rowcontextmenu", function(g, i, e) {
		e.stopEvent();
		var record = runGrid.getStore().getAt(i);
		var showDanger = '';
		if (record.get("model.workticketTypeCode") == "7"
				|| record.get("model.workticketTypeCode") == "8") {
			showDanger = "none";
		}
		// 右键菜单
		var menu = new Ext.menu.Menu({
			id : 'mainMenu',
			items : [new Ext.menu.Item({
				text : '票面浏览',
				iconCls : 'pdfview',
				handler : function() {
					workticketPrint(record);
				}
			}), new Ext.menu.Item({
				text : '图形展示',
				iconCls : 'view',
				handler : function() {
					var entryId = record.get("model.workFlowNo");
					var url = "";
					if (entryId == "" || entryId == null) {
						var workticketCode = record
								.get('model.workticketTypeCode');
						var flowCode = "bqStandWorkticket";
						// alert(flowCode);
						url = "/power/workflow/manager/flowshow/flowshow.jsp?flowCode="
								+ flowCode;
					} else {
						url = "/power/workflow/manager/show/show.jsp?entryId="
								+ entryId;
					}
					window.open(url);
				}
			}), new Ext.menu.Item({
				text : '查看审批记录',
				iconCls : 'list',
				handler : function() {
					var entryId = record.get("model.workFlowNo");
					if (entryId != "" && entryId != null) {
						var url = "/power/workflow/manager/approveInfo/approveInfo.jsp?entryId="
								+ entryId;
						window
								.showModalDialog(
										url,
										null,
										"dialogWidth:650px;dialogHeight:400px;;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;");
					} else {
						Ext.Msg.alert("提示", "流程尚未启动");
					}
				}
			}), new Ext.menu.Item({
				text : '危险点票面预览',
				iconCls : 'pdfview',
				style : 'display:' + showDanger,
				handler : function() {
					dangerPrint(record);
					//
				}
			})]
		});
		var coords = e.getXY();
		menu.showAt([coords[0], coords[1]]);
	});
	// 票面浏览
	function workticketPrint(menber) {
		var workticketNo;
		var workticketCode;
		workticketNo = menber.get('model.workticketNo');
		workticketCode = menber.get('model.workticketTypeCode');
		var fireLevelId = menber.get('model.firelevelId');

		var strReportAdds = getReportUrl(workticketCode, workticketNo,
				fireLevelId);

		if (strReportAdds != "") {
			window.open(strReportAdds);
		} else {
			Ext.Msg.alert("目前没有该种工作票票面预览");
		}
		// }
	}
	function dangerPrint(menber) {
		var workticketNo;
		var workticketCode;
		workticketNo = menber.get('model.workticketNo');
		workticketCode = menber.get('model.workticketTypeCode');

		var url = '/powerrpt/report/webfile/bqmis/workticketDangerControl.jsp?workticketNo='
				+ workticketNo;
		window.open(encodeURI(url));

	}
	// 设定布局器及面板
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		items : [runGrid]
	});

	document.getElementById("divManagerDel").onclick = function() {
		if (currentUser == "999999" || currentUser == "0900007"&& event.ctrlKey && event.altKey) {
			btnDelete.setVisible(true);
		}
	};
});