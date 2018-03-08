Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
function getDate() {
	var d, s, t;
	d = new Date();
	s = d.getFullYear().toString(10) + "-";
	t = d.getMonth() + 1;
	s += (t > 9 ? "" : "0") + t + "-";
	t = d.getDate();
	s += (t > 9 ? "" : "0") + t + " ";
	t = d.getHours();
	s += (t > 9 ? "" : "0") + t + ":";
	t = d.getMinutes();
	s += (t > 9 ? "" : "0") + t + ":";
	t = d.getSeconds();
	s += (t > 9 ? "" : "0") + t;
	return s;
}
Ext.onReady(function() {

	var formJson = window.dialogArguments;

	// 组建扩展规则
	// base 基本信息
	// eliminate 消缺信息
	// arbitrate 仲裁信息
	// await 待处理信息
	// acceptance 验收信息
		var failureFormPanel = new Ext.FormPanel({
		title : '基本信息',
		border : false,
		labelAlign : 'right',
		form :'panel',
		items : [{
			layout : "column",
			border : false,
			items : [{
				name : 'id',
				xtype : 'hidden'
			}, {
				columnWidth : 0.3,
				layout : "form",
				border : false,
				items : [{
					xtype : "textfield",
					fieldLabel : "发现人",
					name : "findByName",
					readOnly : true,
					anchor : "95%"
				}]
			}, {
				columnWidth : 0.3,
				layout : "form",
				border : false,
				items : [{
					xtype : "textfield",
					fieldLabel : "发现部门",
					name : "findDeptName",
					readOnly : true,
					anchor : "95%"
				}]
			}, {
				columnWidth : 0.3,
				layout : "form",
				border : false,
				items : [{
					xtype : "textfield",
					fieldLabel : "发现时间",
					name : "findDate",
					readOnly : true,
					anchor : "95%"
				}]
			}]
		}, {
			layout : "column",
			border : false,
			items : [{
				columnWidth : 0.9,
				layout : "form",
				border : false,
				items : [{
					xtype : "textfield",
					fieldLabel : "设备名称",
					name : "equName",
					allowBlank : false,
					readOnly : true,
					anchor : "98.2%"
				}]
			}]
		}, {
			layout : "column",
			border : false,
			items : [{
				columnWidth : 0.3,
				layout : "form",
				border : false,
				items : [{
					xtype : "textfield",
					fieldLabel : "所属系统",
					name : "belongSystemName",
					allowBlank : false,
					readOnly : true,
					anchor : "95%"
				}]
			}, {
				columnWidth : 0.3,
				layout : "form",
				border : false,
				items : [{
					xtype : "textfield",
					fieldLabel : "故障类型",
					name : "bugName",
					readOnly : true,
					allowBlank : false,
					anchor : "95%"
				}]
			}, {
				columnWidth : 0.3,
				layout : "form",
				border : false,
				items : [{
					xtype : "textfield",
					fieldLabel : "当前状态",
					value : '未上报',
					name : "woStatusName",
					readOnly : true,
					anchor : "95%"
				}]
			}]
		}, {
			layout : "column",
			border : false,
			items : [{
				columnWidth : 0.3,
				layout : "form",
				border : false,
				items : [{
					xtype : "textfield",
					fieldLabel : "运行专业",
					name : "runProfessionName",
					readOnly : true,
					anchor : "95%"
				}]
			}, {
				columnWidth : 0.3,
				layout : "form",
				border : false,
				items : [{
					xtype : "textfield",
					fieldLabel : "是否限制运行",
					name : "ifStopRunName",
					readOnly : true,
					value : '否',
					anchor : "95%"
				}]
			}, {
				columnWidth : 0.3,
				layout : "form",
				border : false,
				items : [{
					xtype : "textfield",
					fieldLabel : "管辖专业",
					name : "dominationProfessionName",
					readOnly : true,
					value : '否',
					anchor : "95%"
				}]
			}]
		}, {
			layout : "column",
			border : false,
			items : [{
				columnWidth : 0.3,
				layout : "form",
				border : false,
				items : [{
					xtype : "textfield",
					fieldLabel : "缺陷类别",
					name : "failuretypeName",
					readOnly : true,
					anchor : "95%"
				}]
			}, {
				columnWidth : 0.3,
				layout : "form",
				border : false,
				items : [{
					xtype : "textfield",
					fieldLabel : "优先级",
					name : "failurePri",
					readOnly : true,
					anchor : "95%"
				}]
			}, {
				columnWidth : 0.3,
				layout : "form",
				border : false,
				items : [{
					xtype : "textfield",
					fieldLabel : "检修部门",
					name : "repairDepName",
					readOnly : true,
					anchor : "95%"
				}]
			}]
		}, {
			layout : "column",
			border : false,
			items : [{
				columnWidth : 0.9,
				layout : "form",
				border : false,
				items : [{
					xtype : "textarea",
					fieldLabel : "未消除前措施",
					name : "preContent",
					readOnly : true,
					anchor : "98%",
					height : 55
				}]
			}]
		}, {
			layout : "column",
			border : false,
			items : [{
				columnWidth : 0.9,
				layout : "form",
				border : false,
				items : [{
					xtype : "textarea",
					fieldLabel : "缺陷内容",
					name : "failureContent",
					anchor : "98%",
					allowBlank : false,
					readOnly : true,
					height : 55
				}]
			}]
		}, {
			layout : "column",
			border : false,
			items : [{
				columnWidth : 0.9,
				layout : "form",
				border : false,
				items : [{
					xtype : "textarea",
					fieldLabel : "可能原因",
					name : "likelyReason",
					anchor : "98%",
					readOnly : true,
					height : 55
				}]
			}]
		}, {
			layout : "column",
			border : false,
			items : [{
				columnWidth : 0.3,
				layout : "form",
				border : false,
				items : [{
					xtype : "textfield",
					fieldLabel : "是否电话通知",
					id : 'isTel',
					name : "isTel",
					readOnly : true,
					anchor : "95%"
				}]
			}, {
				columnWidth : 0.3,
				layout : "form",
				border : false,
				items : [{
					xtype : "textfield",
					fieldLabel : "电话通知人",
					id : 'telManName',
					name : "telManName",
					readOnly : true,
					anchor : "95%"
				}]
			}, {
				columnWidth : 0.3,
				layout : "form",
				border : false,
				items : [{
					xtype : "textfield",
					fieldLabel : "电话通知时间",
					id : 'telTime',
					name : "telTime",
					readOnly : true,
					anchor : "95%"
				}]
			}]
		}, {
			layout : "column",
			border : false,
			items : [{
				columnWidth : 0.3,
				layout : "form",
				border : false,
				items : [{
					xtype : "textfield",
					fieldLabel : "是否短信通知",
					id : 'isMessage',
					name : "isMessage",
					readOnly : true,
					anchor : "95%"
				}]
			}, {
				columnWidth : 0.3,
				layout : "form",
				border : false,
				items : [{
					xtype : "textfield",
					fieldLabel : "是否产生工单",
					id : 'ifOpenWorkorderName',
					name : "ifOpenWorkorderName",
					readOnly : true,
					anchor : "95%"
				}]
			}, {
				columnWidth : 0.3,
				layout : "form",
				border : false,
				items : [{
					xtype : "textfield",
					fieldLabel : "是否点检系统产生",
					id : 'isCheck',
					name : "isCheck",
					readOnly : true,
					anchor : "95%"
				}]
			}]
		}, {
			layout : "column",
			border : false,
			items : [{
				columnWidth : 0.3,
				layout : "form",
				border : false,
				items : [{
					xtype : "textfield",
					fieldLabel : "填写人",
					name : "writeByName",
					readOnly : true,
					anchor : "95%"
				}]
			}, {
				columnWidth : 0.3,
				layout : "form",
				border : false,
				items : [{
					xtype : "textfield",
					fieldLabel : "填写部门",
					name : "writeDeptName",
					readOnly : true,
					anchor : "95%"
				}]
			}, {
				columnWidth : 0.3,
				layout : "form",
				border : false,
				items : [{
					xtype : "textfield",
					fieldLabel : "填写时间",
					name : "writeDate",
					readOnly : true,
					anchor : "95%"
				}]
			}]
		}, {
			layout : "column",
			border : false,
			items : [{
				columnWidth : 0.3,
				layout : "form",
				border : false,
				items : [{
					xtype : "textfield",
					fieldLabel : "认领人",
					name : "cliamByName",
					readOnly : true,
					anchor : "95%"
				}]
			}, {
				columnWidth : 0.3,
				layout : "form",
				border : false,
				items : [{
					xtype : "textfield",
					fieldLabel : "认领时间",
					name : "cliamDate",
					readOnly : true,
					anchor : "95%"
				}]
			}]
		}]
	});
 
var expander = new Ext.grid.RowExpander({
		tpl : new Ext.Template('<div class="detailData">', '', '</div>'),
		expandRow : mygridExpend 
	});
	function mygridExpend(row) { 
		if (typeof row == 'number') {
			row = this.grid.view.getRow(row);
		}
		var record = this.grid.store.getAt(row.rowIndex);
		var type = record.get('approveType');
		if (type == '7') {
			expander.tpl = new Ext.Template('<font color="green" size="2" style="line-height:15pt">申请仲裁人：<U>'
					+ record.get('approvePeopleName')
					+ '</U>；申请仲裁时间：<U>'
					+ record.get('approveTime')
					+ '</U>；申请仲裁类别：<U>'
					+ record.get('arbitrateType')
					+ '</U>；<br>申请仲裁原因：<U>'
					+ record.get('approveOpinion') + '</U>；</font>');
		} else if (type == '2' || type == '3' || type == '8') {
			expander.tpl = new Ext.Template('<font color="green" size="2" style="line-height:15pt">仲裁人：<U>'
					+ record.get('approvePeopleName')
					+ '</U>；仲裁时间：<U>'
					+ record.get('approveTime')
					+ '</U>；<br>仲裁后管辖专业：<U>'
					+ record.get('arbitrateProfessionName')
					+ '</U>；仲裁后检修部门：<U>'
					+ record.get('arbitrateDeptName')
					+ '</U>；验收仲裁类别：<U>'
					+ record.get('checkArbitrateType')
					+ '</U>；仲裁后缺陷类别：<U>'
					+ record.get('arbitrateKind')
					+ '</U>；<br>仲裁意见：<U>'
					+ record.get('approveOpinion')
					+ '</U>；</font>');
		} else if (type == '11') {
			expander.tpl = new Ext.Template('<font color="green" size="2" style="line-height:15pt">申请延期待处理人：<U>'
					+ record.get('approvePeopleName')
					+ '</U>；申请延期待处理时间：<U>'
					+ record.get('approveTime')
					+ '</U>；延期待处理类别：<U>'
					+ record.get('awaitType')
					+ '</U>；申请延长日期到：<U>'
					+ record.get('delayDate')
					+ '</U>；<br>申请延期待处理原因：<U>'
					+ record.get('approveOpinion') + '</U>；</font>');
		} else if (type == '12' || type == '13' || type == '5' || type == '19' || type == '20' || type == '21' || type == '22') {
			expander.tpl = new Ext.Template('<font color="green" size="2" style="line-height:15pt">待处理人：<U>'
					+ record.get('approvePeopleName')
					+ '</U>；延期待处理时间：<U>'
					+ record.get('approveTime')
					+ '</U>；批准延长日期到：<U>'
					+ record.get('delayDate')
					+ '</U>；<br>延期待处理意见：<U>'
					+ record.get('approveOpinion') + '</U>；</font>');
		} else if (type == '15' || type == '16' || type == '17' || type == '23') {
			expander.tpl = new Ext.Template('<font color="green" size="2" style="line-height:15pt">延期待处理人：<U>'
					+ record.get('approvePeopleName')
					+ '</U>；延期待处理时间：<U>'
					+ record.get('approveTime')
					+ '</U>；<br>退回原因：<U>'
					+ record.get('approveOpinion') + '</U>；</font>');
		} else if (type == '1') {
			expander.tpl = new Ext.Template('<font color="green" size="2" style="line-height:15pt">消缺人：<U>'
					+ record.get('approvePeopleName')
					+ '</U>；消缺时间：<U>'
					+ record.get('approveTime')
					+ '</U>；<br>消缺方式：<U>'
					+ record.get('eliminateType')
					+ '</U>；消缺班组：<U>'
					+ record.get('eliminateClassName')
					+ '</U>；工作负责人：<U>'
					+ record.get('chargeManName')
					+ '</U>；处理结果：<U>'
					+ record.get('tackleResult')
					+ '</U>；<br>交待内容：<U>'
					+ record.get('approveOpinion') + '</U>；</font>');
		} else if (type == '4' || type == '14' || type == '9') {
			expander.tpl = new Ext.Template('<font color="green" size="2" style="line-height:15pt">验收人：<U>'
					+ record.get('approvePeopleName')
					+ '</U>；验收时间：<U>'
					+ record.get('approveTime')
					+ '</U>；验收质量：<U>'
					+ record.get('checkQuality')
					+ '</U>；现场验收人：<U>'
					+ record.get('checkManName')
					+ '</U>；<br>说明：<U>'
					+ record.get('approveOpinion') + '</U>；</font>');
		} else if (type == '6') {
			expander.tpl = new Ext.Template('<font color="green" size="2" style="line-height:15pt">审批人：<U>'
					+ record.get('approvePeopleName')
					+ '</U>；作废时间：<U>'
					+ record.get('approveTime')
					+ '</U>；<br>作废原因：<U>'
					+ record.get('approveOpinion') + '</U>；</font>');
		} else if (type == '10') {
			expander.tpl = new Ext.Template('<font color="green" size="2" style="line-height:15pt">审批人：<U>'
					+ record.get('approvePeopleName')
					+ '</U>；退回时间：<U>'
					+ record.get('approveTime')
					+ '</U>；<br>退回原因：<U>'
					+ record.get('approveOpinion') + '</U>；</font>');
		}else if (type == '18') {
			expander.tpl = new Ext.Template('<font color="green" size="2" style="line-height:15pt">确认人：<U>'
					+ record.get('approvePeopleName')
					+ '</U>；确认时间：<U>'
					+ record.get('approveTime')
					+ '</U>；<br>确认意见：<U>'
					+ record.get('approveOpinion') + '</U>；</font>');
		}  else {
			expander.tpl = new Ext.Template('<p> <br><font color="blue"><b> </b><br>'
					+ type + '</font></p>');
		}

		var body = Ext.DomQuery.selectNode('tr:nth(2) div.x-grid3-row-body',
				row);
		if (this.beforeExpand(record, body, row.rowIndex)) {
			this.state[record.id] = true;
			Ext.fly(row).replaceClass('x-grid3-row-collapsed',
					'x-grid3-row-expanded');
			this.fireEvent('expand', this, record, body, row.rowIndex);
		}

	}
	var Historyoperation = Ext.data.Record.create([expander, {
		name : 'id'
	}, {
		name : 'failureCode'
	}, {
		name : 'approveType'
	}, {
		name : 'approveTypeName'
	}, {
		name : 'groupName'
	}, {
		name : 'approveTime'
	}, {
		name : 'approveOpinion'
	}, {
		name : 'approvePeople'
	}, {
		name : 'approvePeopleName'
	}, {
		name : 'arbitrateType'
	}, {
		name : 'arbitrateDept'
	}, {
		name : 'arbitrateDeptName'
	}, {
		name : 'arbitrateProfession'
	}, {
		name : 'arbitrateProfessionName'
	}, {
		name : 'checkArbitrateType'
	}, {
		name : 'arbitrateKind'
	}, {
		name : 'awaitType'
	}, {
		name : 'delayDate'
	}, {
		name : 'awaitappoDate'
	}, {
		name : 'eliminateType'
	}, {
		name : 'eliminateClass'
	}, {
		name : 'eliminateClassName'
	}, {
		name : 'tackleResult'
	}, {
		name : 'chargeMan'
	}, {
		name : 'chargeManName'
	}, {
		name : 'chargerLeader'
	}, {
		name : 'chargerLeaderName'
	}, {
		name : 'checkMan'
	}, {
		name : 'checkManName'
	}, {
		name : 'checkQuality'
	}]);
	var ds = new Ext.data.GroupingStore({
		proxy : new Ext.data.HttpProxy({
			url : 'bqfailure/queryApproveList.action?failureCode='
					+ formJson.data.failureCode,
			method : 'post'
		}),
		reader : new Ext.data.JsonReader({}, Historyoperation),
		groupField : 'name',
		groupOnSort : true,
		sortInfo : {
			field : 'groupName',
			direction : "ASC"
		}
	});

	ds.load();
	var cm = new Ext.grid.ColumnModel([{
		header : '操作类型',
		dataIndex : 'approveTypeName',
		align : 'center',
		width : 120
	}, {
		header : '操作时间',
		dataIndex : 'approveTime',
		align : 'center',
		width : 120
	}, {
		header : '操作人',
		dataIndex : 'approvePeopleName',
		align : 'center',
		width : 120
	}, {
		header : '类别',
		dataIndex : 'groupName',
		align : 'left',
		hidden : true,
		width : 120
	}]);
	cm.defaultSortable = false; 
	var grid = new Ext.grid.GridPanel({
		ds : ds,
		cm : cm,
		title : '分类详细信息',
		sm : new Ext.grid.CheckboxSelectionModel({
			selectRow : function(index, keepExisting, preventViewNotify) {
				expander.toggleRow(index);
			}
		}),
		autoWidth : true,
		layout : 'fit',
		height : 380,
		fitToFrame : true,
		enableColumnHide : true,
		enableColumnMove : false,
		plugins : expander,
		view : new Ext.grid.GroupingView({
			forceFit : true,
			sortAscText : '正序',
			sortDescText : '倒序',
			columnsText : '列显示/隐藏',
			groupByText : '依本列分组',
			showGroupsText : '分组显示',
			groupTextTpl : '{text}'
		}),
		collapsible : true,
		animCollapse : false,
		border : false,
		listeners : {
			activate : function() { 
					grid.getSelectionModel().selectAll(); 
			}
		}
	});
	grid.enableColumnHide = false; 
	var approve = Ext.data.Record.create([{
		name : 'id'
	}, {
		name : 'entryId'
	}, {
		name : 'stepId'
	}, {
		name : 'stepName'
	}, {
		name : 'actionId'
	}, {
		name : 'actionName'
	}, {
		name : 'caller'
	}, {
		name : 'callerName'
	}, {
		name : 'opinion'
	}, {
		name : 'opinionTime'
	}]);

	var approveds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'MAINTWorkflow.do?action=getApproveList&entryId='
					+ formJson.data.workFlowNo,
			method : 'post'
		}),
		reader : new Ext.data.JsonReader({}, approve)
	});
	approveds.load();

	var approvecm = new Ext.grid.ColumnModel([{
		header : '状态',
		dataIndex : 'stepName',
		align : 'left',
		width : 120
	}, {
		header : '执行动作',
		dataIndex : 'actionName',
		align : 'left',
		width : 100
	}, {
		header : '执行人',
		dataIndex : 'callerName',
		align : 'left',
		width : 80
	}, {
		header : '执行时间',
		dataIndex : 'opinionTime',
		align : 'left',
		width : 120
	}, {
		header : '审批意见',
		dataIndex : 'opinion',
		align : 'left',
		width : 400,
		renderer : function renderName(value, metadata, record) {
			metadata.attr = 'style="white-space:normal;"';
			return value;
		}
	}]);
	approvecm.defaultSortable = false;
	var approvegrid = new Ext.grid.GridPanel({
		ds : approveds,
		cm : approvecm,
		title : '审批列表',
		autoWidth : true,
		layout : 'fit',
		height : 380,
		fitToFrame : true,
		border : false
	});

	var failureTab = new Ext.TabPanel({
		activeTab : 0,
		layoutOnTabChange : true,
		items : [failureFormPanel, approvegrid, grid]
	});

	var viewport = new Ext.Viewport({
		layout : "fit",
		items : [failureTab]
	});


	failureFormPanel.getForm().loadRecord(formJson);
	Ext.get('isMessage').dom.value=formJson.data.isMessage == "Y"? "是":"否";
	Ext.get('isTel').dom.value=formJson.data.isTel == "Y"? "是":"否";
	Ext.get('isCheck').dom.value=formJson.data.isCheck == "Y"? "是":"否";

});