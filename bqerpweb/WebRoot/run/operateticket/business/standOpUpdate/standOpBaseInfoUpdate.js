Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
var operateCode = "";
Ext.onReady(function() {
	var wd = 180;
	operateCode = parent.currentOpCode;
	var endTicketNo = "";
	// 操作票编号
	var tfOpticketCode = new Ext.form.TextField({
		id : 'sopticketCode',
		name : 'opticket.opticketCode',
		fieldLabel : "操作票编号",
		value : Constants.AUTO_CREATE,
		readOnly : true,
		width : wd
	});
	// 操作任务
	var cbtOpTask = new Ext.ux.ComboBoxTree({
		fieldLabel : "操作任务<font color='red'>*</font>",
		id : 'cbtOpTask',
		hiddenName : 'opticket.operateTaskId',
		width : wd,
		displayField : 'text',
		valueField : 'id',
		blankText : '请选择',
		emptyText : '请选择',
		readOnly : true,
		anchor : "85%",
		allowBlank : false,
		tree : {
			xtype : 'treepanel',
			// 虚拟节点,不能显示
			rootVisible : false,
			loader : new Ext.tree.TreeLoader({
				dataUrl : 'opticket/getDetailOpTaskTree.action'
			}),
			root : new Ext.tree.AsyncTreeNode({
				id : '-1',
				text : '所有操作任务'
			}),
			listeners : {
				click : function(node) {
					var currentNode = node;
					var code = currentNode.attributes.code;
					opticketName.setValue(currentNode.text);
					opticketType.setValue(getOpType(code));
				}
			}
		},
		selectNodeModel : 'leaf',
		listeners : {
			collapse : function() {
				if (this.value) {
					this.el.dom.readOnly = false;
				}
			}
		}
	});

	// 操作任务名
	var opticketName = new Ext.form.TextField({
		fieldLabel : "操作任务名称",
		id : 'opticketName',
		width : wd,
		anchor : "85%",
		name : 'opticket.opticketName'
	});
	var hideTaskName = new Ext.form.Hidden({
		id : 'operateTaskName',
		width : wd,
		anchor : "85%",
		name : 'opticket.operateTaskName'
	});
	var opticketType = new Ext.form.TextField({
		fieldLabel : "操作票类别",
		width : wd,
		id : 'opticketType',
		readOnly : true,
		name : 'opticket.opticketType',
		renderer : function(v) {
			return getOpType(v)
		}
	});

	// 专业
	var storeSpecials = new Ext.data.JsonStore({
		url : 'opticket/getDetailOpticketSpecials.action',
		root : 'list',
		fields : ['specialityCode', 'specialityName']
	});
	storeSpecials.on('load', function(e, records) {
		// 设置所有
		cbxOpticketSpecials.setValue(records[0].data.specialityCode);
	});
	storeSpecials.load();
	var cbxOpticketSpecials = new Ext.form.ComboBox({
		id : 'specialityCode',
		fieldLabel : "专业<font color='red'>*</font>",
		triggerAction : 'all',
		store : storeSpecials,
		displayField : "specialityName",
		valueField : "specialityCode",
		hiddenName : 'opticket.specialityCode',
		mode : 'local',
		readOnly : true,
		allowBlank : false,
		width : wd
	})
	var btnSave = new Ext.Button({
		text : Constants.BTN_SAVE,
		iconCls : Constants.CLS_SAVE,
		handler : saveOpticket
	})

	// 清空
	var btnCancel = new Ext.Button({
		text : '清空',
		iconCls : 'reflesh',
		handler : function() {
			editOpticket(operateCode);
			// cbtOpTask.setDisabled(false);
			// baseInfoFormPanel.getForm().reset();
		}
	})
	var baseInfoField = new Ext.form.FieldSet({
		border : true,
		labelAlign : 'right',
		buttonAlign : 'center',
		layout : 'form',
		title : '操作票修改',
		defaults : {
			width : 800
		},
		autoHeight : true,
		style : {
			"margin-top" : "20px",
			"margin-left" : "10px",
			"margin-right" : Ext.isIE6
					? (Ext.isStrict ? "-10px" : "-13px")
					: "0"
		},
		items : [{
			layout : "column",
			border : false,
			items : [{
				columnWidth : 0.38,
				layout : "form",
				border : false,
				items : [tfOpticketCode]
			}
			]
		}, {
			columnWidth : 0.38,
			layout : "form",
			border : false,
			items : [cbtOpTask]
		}, {
			columnWidth : 0.38,
			layout : "form",
			border : false,
			items : [opticketName]
		}, {
			columnWidth : 0.38,
			layout : "form",
			border : false,
			items : [opticketType]
		}, {
			columnWidth : 0.38,
			layout : "form",
			border : false,
			items : [cbxOpticketSpecials, hideTaskName]
		}],
		buttons : [btnSave, btnCancel]
	});
	if (operateCode == "") {
		cbtOpTask.setDisabled(false);
		cbxOpticketSpecials.setDisabled(false);
		// Ext.getCmp("opticketName").setDisabled(false);
	} else {
		cbtOpTask.setDisabled(true);
		cbxOpticketSpecials.setDisabled(true);
		// Ext.getCmp("opticketName").setDisabled(true);
	}

	var baseInfoFormPanel = new Ext.FormPanel({
		border : false,
		frame : true,
		fileUpload : true,
		items : [baseInfoField]
	});

	var baseInfoViewport = new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		defaults : {
			autoScroll : true
		},
		items : [baseInfoFormPanel]
	});
	// 清除所有项目
	function clearAllFields() {
		baseInfoFormPanel.getForm().reset();
	}

	// 编辑操作票
	function editOpticket(argOpticketCode) {
		// 清除所有项目
		clearAllFields();
		if (operateCode != "") {
			// 加载数据
			Ext.Ajax.request({
				method : Constants.POST,
				url : 'opticket/getDetailOpticketByCode.action',
				success : function(result, request) {
					var record = eval('(' + result.responseText + ')');
					baseInfoFormPanel.getForm().loadRecord(record);
					// 操作任务
					Ext.getCmp('cbtOpTask').setValue({
						id : record.data.operateTaskId,
						text : record.data.operateTaskName
					});
					// 专业
					if (record.data.specialityCode != 'null') {
						cbxOpticketSpecials
								.setValue(record.data.specialityCode);
					} else {
						cbxOpticketSpecials.setValue(record.data.operateTaskId);
					}
					tfOpticketCode.setValue(argOpticketCode);
					opticketName.setValue(record.data.opticketName != null
							? record.data.opticketName
							: record.data.operateTaskName)
					opticketType.setValue(getOpType(record.data.opticketType))
				},
				failure : function() {
					Ext.Msg.alert(Constants.ERROR, '加载数据失败!');
				},
				params : {
					opticketCode : argOpticketCode
				}
			});
		}
	}
	// 增加/保存操作票
	function saveOpticket() {
		if (!baseInfoFormPanel.getForm().isValid()) {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, '操作任务,操作人，专业是必须输入的字段');
			return;
		}
		var isAdd = tfOpticketCode.value == Constants.AUTO_CREATE;
		var url = 'opticket/addDetailOpticket.action?isStand=Y';
		if (!isAdd) {
			url = 'opticket/updateDetailOpticket.action';
		}
		// 操作任务名
		hideTaskName.setValue(Ext.get('cbtOpTask').dom.value);
		baseInfoFormPanel.getForm().submit({
			method : Constants.POST,
			waitMsg : Constants.DATA_SAVING,
			url : url,
			params : {
				endTicketNo : endTicketNo
			},
			success : function(form, action) {
				if (!action.response.responseText) {
					return;
				}
				var o = eval("(" + action.response.responseText + ")");

				if (isAdd) {
					// 设置操作票编号
					tfOpticketCode.setValue(o.opticketCode);
					Ext.Msg.alert('提示', '增加成功');
					parent.currentOpCode = o.opticketCode;
					cbtOpTask.setDisabled(true);
					cbxOpticketSpecials.setDisabled(true);
				} else {
					Ext.Msg.alert(Constants.SYS_REMIND_MSG, o.msg);
				}
				parent.tabReport.document.getElementById("queryBtn").click();
			},
			faliue : function() {
				Ext.Msg.alert(Constants.ERROR, Constants.UNKNOWN_ERR);
			}
		});
	}
	editOpticket(operateCode);
});