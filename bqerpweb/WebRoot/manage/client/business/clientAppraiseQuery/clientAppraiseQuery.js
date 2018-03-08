Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	// 定义grid
	var MyRecord = Ext.data.Record.create([{
		name : 'appraisalId'
	}, {
		name : 'intervalId'
	}, {
		name : 'cliendId'
	}, {
		name : 'totalScore'
	}, {
		name : 'intervalDate'
	}, {
		name : 'gatherFlag'
	}, {
		name : 'clientName'
	}, {
		name : 'appraiseBy'
	}, {
		name : 'appraiseName'
	}, {
		name : 'appraiseDate'
	}, {
		name : 'beginDate'
	}, {
		name : 'endDate'
	}, {
		name : 'gatherBy'
	}, {
		name : 'gatherName'
	}, {
		name : 'gatherDate'
	}, {
		name : 'appraisalResult'
	}]);

	var dataProxy = new Ext.data.HttpProxy({
		url : 'clients/findAppraiseList.action'
	});

	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	}, MyRecord);

	var store = new Ext.data.Store({
		proxy : dataProxy,
		reader : theReader
	});
	// ===========================查询条件=========================================

	var cbxClient = new Ext.form.ComboBox({
		name : 'cbxClient',
		id : 'cbxClient',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		hiddenName : 'cbxClientId',
		editable : false,
		anchor : "80%",
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../clientSelect/clientSelect.jsp?approveFlag=2";
			var client = window
					.showModalDialog(
							url,
							null,
							'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');

			if (typeof(client) != "undefined") {
				Ext.getCmp('cbxClient').setValue(client.cliendId);
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('cbxClient'), client.clientName);
			}
		}
	});

	var beginDate = new Ext.form.TextField({
		id : 'beginDate',
		fieldLabel : '评价周期开始时间',
		name : 'beginDate',
		readOnly : true
	});

	var endDate = new Ext.form.TextField({
		id : 'endDate',
		fieldLabel : '评价周期结束时间',
		name : 'endDate',
		readOnly : true
	});
	var hideIntervalId = new Ext.form.Hidden({
		id : 'hideIntervalId'
	});

	// ============================================================================
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true
	});
	var grid = new Ext.grid.EditorGridPanel({
		region : "center",
		layout : 'fit',
		store : store,
		clicksToEdit : 1,
		columns : [sm,new Ext.grid.RowNumberer({
			 header : '序号',
			width : 35
		}), {
			header : "ID",
			width : 75,
			sortable : true,
			dataIndex : 'appraisalId',
			hidden : true
		}, {
			header : "合作伙伴",
			width : 75,
			sortable : true,
			dataIndex : 'clientName'
		}, {
			header : "评价区间",
			width : 100,
			sortable : true,
			dataIndex : 'intervalDate'
		}, {
			header : "评价分数",
			width : 75,
			sortable : true,
			dataIndex : 'totalScore'
		}, {
			header : "评价人",
			width : 75,
			sortable : true,
			dataIndex : 'appraiseName'
		}, {
			header : "评价日期",
			width : 75,
			sortable : true,
			dataIndex : 'appraiseDate'
		}, {
			header : "是否汇总",
			width : 75,
			sortable : true,
			dataIndex : 'gatherFlag',
			renderer : function(value) {
				if (value == "N")
					return "否";
				if (value == "Y")
					return "是";
			}
		}],
		sm : sm,
		autoSizeColumns : true,
		viewConfig : {
			forceFit : true
		},
		tbar : ['合作伙伴：', cbxClient, '评价区间：', beginDate, '~', endDate,
				hideIntervalId, {
					text : '查询',
					iconCls : 'query',
					handler : queryRecord
				}, {
					text : '评价明细',
					iconCls : 'list',
					handler : showRecord
				}, {
					text : '汇总信息',
					iconCls : 'list',
					handler : saveSummary
				}],
			bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : store,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		})
	});

	// =================================评价汇总FORM==============================================
	var appraisalId = new Ext.form.TextField({
		id : 'appraisalId',
		hidden :true,
		hideLabel : true,
		name : 'appraise.appraisalId'
	});

	var cliendId = new Ext.form.TextField({
		id : 'clientName',
		fieldLabel : '合作伙伴',
		anchor : '95.8%',
		readOnly : true,
		name : 'appraise.clientName'
	});
	var totalScore = new Ext.form.NumberField({
		id : 'totalScore',
		fieldLabel : '评价总分',
		readOnly : true,
		anchor : '95.8%',
		name : 'appraise.totalScore'
	});

	var intervalId = new Ext.form.TextField({
		id : 'intervalDate',
		fieldLabel : '评价区间',
		anchor : '95.8%',
		readOnly : true,
		name : 'appraise.intervalDate'
	});

	var appraiseBy = new Ext.form.TextField({
		id : 'appraiseName',
		fieldLabel : '评价人',
		readOnly : true,
		anchor : '92%',
		name : 'appraise.appraiseName'
	});
	var appraiseDate = new Ext.form.TextField({
		id : 'appraiseDate',
		fieldLabel : '评价日期',
		readOnly : true,
		anchor : '92%',
		name : 'appraise.appraiseDate',
		format : 'Y-m-d'
	});
	var appraisalResult = new Ext.form.TextArea({
		id : 'appraisalResult',
		fieldLabel : '总评',
		anchor : '95%',
		name : 'appraise.appraisalResult'
	});
	var gatherBy = new Ext.form.TextField({
		fieldLabel : '汇总人',
		id : 'gatherName',
		name : 'appraise.gatherName',
		anchor : '92%',
		readOnly : true
	});
	var gatherDate = new Ext.form.TextField({
		fieldLabel : '汇总日期',
		id : 'gatherDate',
		anchor : '92%',
		name : 'appraise.gatherDate',
		readOnly : true
	});
	var myaddpanel = new Ext.FormPanel({
		title : '评价汇总信息',
		height : '100%',
		layout : 'form',
		frame : true,
		labelAlign : 'center',
		items : [{
			border : false,
			layout : 'column',
			items : [{
				border : false,
				layout : 'form',
				columnWidth : 1,
				labelWidth : 70,
				items : [appraisalId]
			}, {
				border : false,
				layout : 'form',
				columnWidth : 1,
				labelWidth : 70,
				items : [cliendId]
			}, {
				border : false,
				layout : 'form',
				columnWidth : 1,
				labelWidth : 70,
				items : [intervalId]
			}, {
				border : false,
				layout : 'form',
				columnWidth : 1,
				labelWidth : 70,
				items : [totalScore]
			}, {
				columnWidth : .5,
				layout : 'form',
				border : false,
				labelWidth : 70,
				items : [appraiseBy]
			}, {
				columnWidth : .5,
				layout : 'form',
				labelWidth : 70,
				border : false,
				items : [appraiseDate]
			}]
		}, {
			border : false,
			layout : 'form',
			columnWidth : 1,
			labelWidth : 70,
			items : [appraisalResult]
		}, {
			border : false,
			layout : 'column',
			items : [{
				columnWidth : .5,
				layout : 'form',
				border : false,
				labelWidth : 70,
				items : [gatherBy]

			}, {
				columnWidth : .5,
				layout : 'form',
				labelWidth : 70,
				border : false,
				items : [gatherDate]
			}]
		}]
	});
	var win = new Ext.Window({
		width : 400,
		height : 300,
		buttonAlign : "center",
		items : [myaddpanel],
		layout : 'fit',
		closeAction : 'hide',
		draggable : true,
		modal : true,
		buttons : [{
			text : '保存',
			id : 'btnSave',
			iconCls : 'save',
			handler : function() {
				var myurl = "";
					if (Ext.get("appraisalId").dom.value != ""
							|| Ext.get("appraisalId").dom.value != null) {
						myurl = "clients/updateAppraiseSummary.action";
					}
					myaddpanel.getForm().submit({
						method : 'POST',
						url : myurl,
						success : function(form, action) {
							var o = eval("(" + action.response.responseText
									+ ")");
							Ext.Msg.alert("提示", o.msg);
							win.hide();
							store.reload();
						},
						faliue : function() {
							Ext.Msg.alert('错误', '出现未知错误.');
						}
					});
				
			}
		}, {
			text : '取消',
			id : 'cancel',
			iconCls : 'cancer',
			handler : function() {
				win.hide();
			}
		}]
	});

	// ---------------------------------------

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		items : [grid]
	});

	beginDate.getEl().on("click", function() {
		var url = "../intervalSelect/intervalSelect.jsp";
		var obj = window
				.showModalDialog(
						url,
						null,
						'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (typeof(obj) != "undefined") {
			beginDate.setValue(obj.beginDate);
			endDate.setValue(obj.endDate);
			hideIntervalId.setValue(obj.intervalId);
		}
	});

	function queryRecord() {
//		var msg = "";
//		if (cbxClient.getValue() == "")
//			msg = "'合作伙伴'";
//		if (hideIntervalId.getValue() == "")
//			msg += "'评价区间'";
//		if (msg != "") {
//			Ext.Msg.alert("提示", "请选择" + msg + "!");
//			return;
//		}
		store.load({
			params : {
				start : 0,
				limit : 18,
				clientId : Ext.get("cbxClientId").dom.value,
				intervalId : hideIntervalId.getValue()
			}
		});
	}

	function showRecord() {
		if (grid.selModel.hasSelection()) {
			var records = grid.selModel.getSelections();
			var record = grid.getSelectionModel().getSelected();
			if (records.length > 1) {
				Ext.Msg.alert("提示", "请选择其中一条记录！");
			} else {
				var url;
				var cliendId = record.data.cliendId;
				var intervalId = record.data.intervalId;
				var clientName = record.data.clientName;
				var beginDate = record.data.beginDate;
				var endDate = record.data.endDate;
				url = "../clientAppraiseSummary/clientAppraiseRecordSelect.jsp?cliendId=" + cliendId
						+ "&intervalId=" + intervalId + "&clientName="
						+ clientName + "&endDate=" + endDate + "&beginDate="
						+ beginDate;
				var o = window.showModalDialog(url, '',
						'dialogWidth=500px;dialogHeight=400px;status=no');
			}
		} else {
			Ext.Msg.alert("提示", "请先选择一条记录!");
		}
	}

	function saveSummary() {
		if (grid.selModel.hasSelection()) {
			var records = grid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("提示", "请选择其中一项进行查看！");
			} else {
				var record = grid.getSelectionModel().getSelected();
				win.setPosition(150, 50);
				win.show();
				Ext.get('btnSave').dom.style.display = "none";// 隐藏按钮
				Ext.get('cancel').dom.style.display = "none";
				myaddpanel.getForm().loadRecord(record);
				myaddpanel.setTitle("评价汇总信息");
			}
		} else {
			Ext.Msg.alert("提示", "请先选择要查看的记录!");
		}
	}
	store.load({
			params : {
				start : 0,
				limit : 18
			}
		});
});