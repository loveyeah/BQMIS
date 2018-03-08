Ext.onReady(function() {
	
	var entryId;
	
	var id = getParameter("id");
	var check = "add";
	var conId = "";
	conId = getParameter("conId");
	if (id != null && id != "") {
		check = "update";
	}
	// ------------定义form---------------
	// 合同编号
	var conModifyNo = {
		id : "conModifyNo",
		xtype : "textfield",
		fieldLabel : '合同变更号',
		readOnly : true,
		name : 'apply.conModifyNo',
		anchor : "90%"
	}

	// 变更类型
	var conomodifyType = new Ext.form.ComboBox({
		fieldLabel : '变更类型',
		store : [['1', '合同变更'], ['2', '合同解除']],
		id : 'conomodifyType',
		name : 'apply.conomodifyType',
		valueField : "value",
		displayField : "text",
		mode : 'local',
		typeAhead : true,
		forceSelection : true,
		hiddenName : 'apply.currencyType',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		emptyText : '请选择',
		anchor : '90%'
	});
	conomodifyType.on('beforequery', function() {
		return false
	});

	// 合同编号
	var conttreesNo = {
		fieldLabel : '合同编号',
		name : 'conttreesNo',
		xtype : 'combo',
		id : 'conttreesNo',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		hiddenName : 'apply.conttreesNo',
		editable : false,
		readOnly : true,
		anchor : "95%",
		triggerAction : 'all',
		onTriggerClick : function() {
		}
	};

	// 合同名称
	var contractName = {
		id : "contractName",
		xtype : "textfield",
		fieldLabel : '合同名称',
		name : 'apply.contractName',
		readOnly : true,
		anchor : "95%"
	}
	// 变更经办人

	var operateBy = {
		fieldLabel : '变更经办人',
		name : 'operateBy',
		xtype : 'combo',
		id : 'operateBy',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		hiddenName : 'apply.operateBy',
		editable : false,
		anchor : "95%",
		triggerAction : 'all',
		onTriggerClick : function() {
		}
	};

	// 申请部门
	var operateDepCode = {
		fieldLabel : '申请部门',
		name : 'operateDepCode',
		xtype : 'combo',
		id : 'operateDepCode',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		hiddenName : 'apply.operateDepCode',
		editable : false,
		anchor : "95%",
		triggerAction : 'all',
		onTriggerClick : function() {
		}
	};

	var operateLeadBy = {
		fieldLabel : '部门负责人',
		name : 'operateLeadBy',
		xtype : 'combo',
		id : 'operateLeadBy',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		hiddenName : 'apply.operateLeadBy',
		editable : false,
		anchor : "100%",
		triggerAction : 'all',
		onTriggerClick : function() {

		}
	};

	// 币别
	var currencyType = new Ext.form.ComboBox({
		fieldLabel : '币别',
		store : [['1', 'RMB'], ['2', 'USD']],
		id : 'currencyType',
		name : 'currencyType',
		valueField : "value",
		displayField : "text",
		mode : 'local',
		typeAhead : true,
		forceSelection : true,
		hiddenName : 'apply.currencyType',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		emptyText : '请选择',
		anchor : "95%"
	});
	currencyType.on('beforequery', function() {
		return false
	});

	var actAmount = {
		id : "actAmount",
		xtype : "numberfield",
		fieldLabel : '原合同金额',
		readOnly : true,
		name : 'apply.actAmount',
		anchor : "95%"
	}
	var modiyActAmount = {
		id : "modiyActAmount",
		xtype : "numberfield",
		fieldLabel : '现合同金额',
		readOnly : true,
		name : 'apply.modiyActAmount',
		anchor : "100%"
	}
	var startDate = {
		id : "startDate",
		xtype : "textfield",
		fieldLabel : '合同履行开始时间',
		name : 'apply.startDate',
		readOnly : true,
		anchor : "90%"
	}
	var endDate = {
		id : "endDate",
		xtype : "textfield",
		fieldLabel : '合同履行结束时间',
		readOnly : true,
		name : 'apply.endDate',
		anchor : "90%"
	}
	var conomodifyName = {
		id : "conomodifyName",
		xtype : "textarea",
		fieldLabel : '变更原因',
		readOnly : true,
		name : 'apply.conomodifyName',
		anchor : "94%"
	}
	var entryBy = {
		id : "entryBy",
		xtype : "textfield",
		fieldLabel : '起草人',
		name : 'apply.entryBy',
		readOnly : true,
		anchor : "90%"
	}
	var entryDate = {
		id : "entryDate",
		xtype : "textfield",
		fieldLabel : '起草日期',
		name : 'apply.entryDate',
		readOnly : true,
		anchor : "90%"
	}
	var oriFileName = {
		id : "oriFileName",
		xtype : "textfield",
		fieldLabel : '合同变更文本',
		name : 'apply.oriFileName',
		readOnly : true,
		anchor : "95%"
	}

	var btnView = new Ext.Button({
		id : 'btnView',
		text : '查看',
		handler : function() {
			if (id == "") {
				Ext.Msg.alert('提示', '请选择合同');
				return false;
			}
			window.open("/power/managecontract/showConFile.action?conid=" + id
					+ "&type=CON");
		}
	});

	var myform = new Ext.FormPanel({
		frame : true,
		width : Ext.get('div_lay').getWidth(),
		// labelAlign : 'left',
		// bodyStyle : 'padding:5px',
		items : [{
			xtype : 'fieldset',
			title : '基本信息',
			// width : 800,
			labelAlign : 'right',
			labelWidth : 100,
			collapsible : true,// add
			autoHeight : true,
			bodyStyle : Ext.isIE
					? 'padding:0 0 5px 15px;'
					: 'padding:10px 15px;',
			border : true,
			style : {
				"margin-left" : "10px",
				"margin-right" : Ext.isIE6
						? (Ext.isStrict ? "-10px" : "-13px")
						: "0"
			},

			items : [{
				layout : 'column',
				items : [{
					columnWidth : 0.5,
					layout : 'form',
					items : [conModifyNo]
				}, {
					columnWidth : 0.5,
					layout : 'form',
					items : [conomodifyType]
				}]
			}, {
				layout : 'column',
				items : [{
					columnWidth : 1,
					layout : 'form',
					items : [conttreesNo]
				}]
			}, {
				layout : 'column',
				items : [{
					columnWidth : 1,
					layout : 'form',
					items : [contractName]
				}]
			}, {
				layout : 'column',
				items : [{
					columnWidth : 0.3,
					layout : 'form',
					items : [operateBy, currencyType]
				}, {
					columnWidth : 0.35,
					layout : 'form',
					items : [operateDepCode, actAmount]
				}, {
					columnWidth : 0.3,
					layout : 'form',
					items : [operateLeadBy, modiyActAmount]
				}]
			}, {
				layout : 'column',
				items : [{
					columnWidth : 0.5,
					layout : 'form',
					items : [startDate]
				}, {
					columnWidth : 0.5,
					layout : 'form',
					items : [endDate]
				}]
			}, {
				layout : 'column',
				items : [{
					columnWidth : 1,
					layout : 'form',
					items : [conomodifyName]
				}]
			}, {
				layout : 'column',
				items : [{
					columnWidth : 0.5,
					layout : 'form',
					items : [entryBy]
				}, {
					columnWidth : 0.5,
					layout : 'form',
					items : [entryDate]
				}]
			}, {
				layout : 'column',
				items : [{
					columnWidth : 0.4,
					layout : 'form',
					items : [oriFileName]
				}, {
					columnWidth : 0.3,
					layout : 'form',
					items : [btnView]
				}]
			}]

		}]
	});
	// ----------------------------------------------------------------

	// --------------定义合同附件grid------------------------------------------
	var MyRecordAnnex = Ext.data.Record.create([{
		name : 'conDocId'
	}, {
		name : 'keyId'
	}, {
		name : 'docName'
	}, {
		name : 'docMemo'
	}, {
		name : 'oriFileName'
	}, {
		name : 'oriFile'
	}, {
		name : 'lastModifiedName'
	}, {
		name : 'lastModifiedDate'
	}]);

	var dataProxyAnnex = new Ext.data.HttpProxy(
			{
				url : 'managecontract/findDocModifyList.action?modifyId=' + id
						+ '&docType=MCONATT'
			}
	);

	var theReaderAnnex = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"

	}, MyRecordAnnex);

	var storeAnnex = new Ext.data.Store({

		proxy : dataProxyAnnex,

		reader : theReaderAnnex

	});
	// 分页
	if (check == "update") {
		storeAnnex.load();
	}
	var gridAnnex = new Ext.grid.GridPanel({
		region : "center",
		store : storeAnnex,
		width : Ext.get('div_lay').getWidth(),
		height : 200,
		columns : [{

			header : "ID",
			sortable : true,
			width : 50,
			dataIndex : 'conDocId',
			hidden : true
		}, {

			header : "合同ID",
			sortable : true,
			width : 50,
			dataIndex : 'keyId',
			hidden : true
		}, {

			header : "查看",
			sortable : true,
			hidden : true,
			width : 100
		}, new Ext.grid.RowNumberer({
			header : '项次号',
			width : 50,
			align : 'center'
		}), {
			header : "名称",
			width : 200,
			sortable : true,
			dataIndex : 'docName',
			align : 'center'
		}, {
			header : "备注",
			width : 150,
			sortable : true,
			dataIndex : 'docMemo',
			align : 'center'
		}, {
			header : "原始文件名",
			width : 100,
			sortable : true,
			dataIndex : 'oriFileName',
			align : 'center',
			hidden : true
		}, {
			header : "原始文件",
			width : 100,
			sortable : true,
			dataIndex : 'oriFile',
			align : 'center'
		}, {
			header : "上传日期",
			width : 120,
			sortable : true,
			dataIndex : 'lastModifiedDate',
			align : 'center'
		}, {
			header : "上传人",
			width : 90,
			sortable : true,
			dataIndex : 'lastModifiedName',
			align : 'center'
		}],
		tbar : ['合同附件']
	});

	// ---------------------------------------------
	// -------------------定义合同凭据grid------------
	var MyRecordProof = Ext.data.Record.create([{
		name : 'conDocId'
	}, {
		name : 'keyId'
	}, {
		name : 'docName'
	}, {
		name : 'docMemo'
	}, {
		name : 'oriFileName'
	}, {
		name : 'oriFile'
	}, {
		name : 'lastModifiedName'
	}, {
		name : 'lastModifiedDate'
	}]);

	var dataProxyProof = new Ext.data.HttpProxy(
			{
				url : 'managecontract/findDocModifyList.action?modifyId=' + id
						+ '&docType=MCONEVI'
			}
	);

	var theReaderProof = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"

	}, MyRecordProof);

	var storeProof = new Ext.data.Store({

		proxy : dataProxyProof,

		reader : theReaderProof

	});
	// 分页
	if (check == "update") {
		storeProof.load();
	}

	var gridProof = new Ext.grid.GridPanel({
		// region : "center",
		store : storeProof,
		width : Ext.get('div_lay').getWidth(),
		height : 200,
		columns : [{

			header : "ID",
			sortable : true,
			dataIndex : 'conDocId',
			hidden : true
		}, {

			header : "合同ID",
			sortable : true,
			dataIndex : 'keyId',
			hidden : true
		}, {

			header : "查看",
			sortable : true,
			hidden : true,
			width : 80
		}, new Ext.grid.RowNumberer({
			header : '项次号',
			width : 50,
			align : 'center'
		}), {
			header : "名称",
			sortable : true,
			dataIndex : 'docName',
			width : 200,
			align : 'center'
		}, {
			header : "备注",
			sortable : true,
			width : 150,
			dataIndex : 'docMemo',
			align : 'center'
		}, {
			header : "原始文件名",
			sortable : true,
			width : 100,
			dataIndex : 'oriFileName',
			align : 'center',
			hidden : true
		}, {
			header : "原始文件",
			sortable : true,
			width : 100,
			dataIndex : 'oriFile',
			align : 'center'
		}, {
			header : "上传日期",
			sortable : true,
			width : 120,
			dataIndex : 'lastModifiedDate',
			align : 'center'
		}, {
			header : "上传人",
			sortable : true,
			width : 90,
			dataIndex : 'lastModifiedName',
			align : 'center'
		}],
		tbar : ['合同附件']
	});
	// --------------------------------

	var backRecord = Ext.data.Record.create([{
		name : 'opinion'
	}, {
		name : 'gdWorker'
	}, {
		name : 'withdrawalTime'
	}]);
	var backReader = new Ext.data.JsonReader({
		root : '',
		totalProperty : ''

	}, backRecord);

	var backStore = new Ext.data.Store({
		url : 'managecontract/findBackOpinion.action?modifyId='+id,
		reader : backReader
	});
	
if(id!=""&&id!=null){
		backStore.load();
}
	var backGrid = new Ext.grid.GridPanel({

		iconCls : 'icon-grid',
		ds : backStore,
		cm : new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(), {
			header : '退回时间',
			dataIndex : 'withdrawalTime'
		}, {
			header : '退回意见',
			dataIndex : 'opinion'
		}, {
			header : '归档人',
			dataIndex : 'gdWorker'
		}

		])

	});

	var backWin = new Ext.Window({
		title : '归档退回意见',
		modal : true,
		height : 300,
		width : 450,
		layout : 'fit',

		buttonAlign : 'center',
		items : [backGrid],
		buttons : [{
			text : '关闭',

			iconCls : 'close',
			handler : function() {
				backWin.hide();
			}
		}]
	});

	var layout = new Ext.Panel({
		autoWidth : true,
		autoHeight : true,
		border : false,
		autoScroll : true,
		split : true,
		items : [myform, gridAnnex, gridProof],
		tbar : [{
			text : '申请归档',
			iconCls : 'add',
			id : 'btnApply',
			handler : function() {
				Ext.Msg.confirm('提示', '确定要申请归档吗?', function(b) {
					if (b == 'yes') {
						Ext.Ajax.request({
							url : 'managecontract/applyArchive.action',
							params : {
								conid : id
							},
							success : function(result, request) {
								Ext.MessageBox.alert('提示', '提交成功!');
							},
							failure : function(result, request) {
								Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');

							}

						});
					}
				})
			}
		}, '-', {
			text : '退回意见',
			iconCls : 'list',
			handler : function() {
				backWin.show();

			}
		}, '-', {
			text : '会签查询',
			iconCls : 'view',
			handler : function()
			{
				if (entryId == null || entryId == "") {
					Ext.Msg.alert('提示', '流程尚未启动！');
				} else {
					var url = "/power/workflow/manager/show/show.jsp?entryId="
							+ entryId;
					window.open(url);
				}
			}
		}, '-', {
			text : '变更会签表',
			iconCls : 'pdfview'
		}, '-', {
			text : '合同信息',
			iconCls : 'list',
			handler : function() {
				url = "../../../../../manage/projectFact/business/conBaseInfo/conBaseInfo.jsp?id="
						+ conId;

				var o = window.showModalDialog(url, '',
						'dialogWidth=800px;dialogHeight=600px;status=no');
			}
		}]
	});

	layout.render(Ext.getBody());

	if (check == "update") {
		myform.getForm().load({
			url : 'managecontract/findModifyBaseInfo.action?modifyId=' + id,

			success : function(form, action) {
				o = eval("(" + action.response.responseText + ")");
				entryId = o.data.workFlowNo;
				
				if (o.data.operateBy != null) {
					Ext.getCmp('operateBy').setValue(o.data.operateBy);
					Ext.form.ComboBox.superclass.setValue.call(Ext
							.getCmp('operateBy'), o.data.operateName);

				}

				if (o.data.operateDepCode != null) {
					Ext.getCmp('operateDepCode')
							.setValue(o.data.operateDepCode);
					Ext.form.ComboBox.superclass.setValue.call(Ext
							.getCmp('operateDepCode'), o.data.operateDeptName);
				}
				if (o.data.operateLeadBy != null) {
					Ext.getCmp('operateLeadBy').setValue(o.data.operateLeadBy);
					Ext.form.ComboBox.superclass.setValue.call(Ext
							.getCmp('operateLeadBy'), o.data.operateLeadName);
				}
				if (o.data.entryName != null) {
					Ext.get("entryBy").dom.value = o.data.entryName;
				}
				if (o.data.startDate != null) {
					Ext.get("startDate").dom.value = o.data.startDate
							.substring(0, 10);

				}
				if (o.data.endDate != null) {
					Ext.get("endDate").dom.value = o.data.endDate.substring(0,
							10);
				}
				
				// alert(o.data.conomodifyName);

			}
		});
	}
    if (id != null && id != "") {
		myform.getForm().load({
			url : 'managecontract/findModifyBaseInfo.action?modifyId=' + id,

			success : function(form, action) {
				o = eval("(" + action.response.responseText + ")");

				var wkflowStat = o.data.workflowStatus;
				var fileStat = o.data.fileStatus;

				if (wkflowStat == 2 && (fileStat == BAR || fileStat == DRE)) {

					Ext.get('btnApply').dom.disabled = false
				} else {
					Ext.get('btnApply').dom.disabled = true;
				}
			}
		})
	}
	else {
		Ext.get('btnApply').dom.disabled = true
	} 

});