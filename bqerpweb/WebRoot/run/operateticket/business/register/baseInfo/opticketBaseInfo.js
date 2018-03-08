Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
var operateCode = "";
var standTicketNo = "";
var endTicketNo="";
Ext.onReady(function() {
	operateCode = parent.currentOpCode;
	var wd = 180;
	var isauto = "N";
	Ext.Ajax.request({
		url : 'comm/getPamValue.action',
		method : 'post',
		params : {
			pamNo : 'ISAUTO'
		},
		success : function(result, request) {
			isauto = result.responseText;
			if (isauto == "N") {
				applyNo.setDisabled(true);
			}
		},
		failure : function(result, request) {
			Ext.Msg.alert('提示信息', '操作失败！')
		}
	})
	// 操作票编号
	var tfOpticketCode = new Ext.form.TextField({
		id : 'sopticketCode',
		name : 'opticket.opticketCode',
		fieldLabel : "操作票编号",
		value : Constants.AUTO_CREATE,
		readOnly : true,
		width : wd
	});
	var applyNo = new Ext.form.TriggerField({ // TriggerField
		fieldLabel : '任务单编号',
		name : 'opticket.applyNo',
		id : 'applyNo',
		allowBlank : false,
		readOnly : false,
		width : wd,
		onTriggerClick : function(e) {
			if (!applyNo.disabled) {
				var args = new Object();
				args.typeCode = "2";
				var url = "/power/comm/taskselect/taskListSelect.jsp";
				var no = window.showModalDialog(url, args,
						'dialogWidth=700px;dialogHeight=500px;status=no');
				if (typeof(no) != "undefined") {
					applyNo.setValue(no.taskNo);
				}
			}
		}
	})
	// 操作任务
	var cbtOpTask = new Ext.ux.ComboBoxTree({
		fieldLabel : "操作任务",
		id : 'cbtOpTask',
		hiddenName : 'opticket.operateTaskId',
		width : wd,
		displayField : 'text',
		valueField : 'id',
		// blankText : '请选择',
		// emptyText : '请选择',
		// readOnly : true,
		anchor : "85%",
		// disabled : true,
		// allowBlank : false,
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
					if (code.length == 13) {
						// code.length == 13 modifyBy ywliu
						opticketType.setDisabled(true);
						opticketType.setValue(code.substring(0, 2));
					} else {
						opticketType.setDisabled(false);
					}
				}
			}
		},
		selectNodeModel : 'leaf',
		listeners : {
			collapse : function() {
				if (this.value) {
					this.el.dom.readOnly = true;
				}
			}
		}
	});

	var isStandar = {
		id : 'isStandar',
		layout : 'column',
		isFormField : true,
		fieldLabel : '标准操作票',
		// xtype : "textfield",
		readOnly : true,
		border : false,
		items : [{
			columnWidth : .3,
			border : false,
			items : new Ext.form.Radio({
				id : 'isStandar1',
				boxLabel : '是',
				name : 'opticket.isStandar',
				inputValue : 'Y'
			})
		}, {
			columnWidth : .3,
			border : false,
			items : new Ext.form.Radio({
				id : 'isStandar2',
				boxLabel : '否',
				name : 'opticket.isStandar',
				inputValue : 'N',
				checked : true
			})
		}]
	};
	var opticketName = new Ext.form.TextField({
		fieldLabel : "操作任务名称",
		id : 'opticketName',
		width : wd,
		anchor : "85%",
		name : 'opticket.opticketName'
	});
	var opticketType = new Ext.form.ComboBox({
		store : new Ext.data.SimpleStore({
			fields : ["retrunValue", "displayText"],
			data : [['00', '电气操作票'], ['01', '热机操作票']]
		}),
		id : 'opticketType',
		readOnly : true,
		name : 'opticket.opticketType',
		valueField : "retrunValue",
		displayField : "displayText",
		mode : 'local',
		forceSelection : true,
		hiddenName : 'opticket.opticketType',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : false,
		fieldLabel : "操作票类别<font color='red'>*</font>",
		width : wd,
		renderer : function(v) {
			getOpType(v)
		}
	});
	var isSingle = new Ext.form.ComboBox({
		id : 'isSingle',
		fieldLabel : '操作方式',
		hidden : true,
		store : new Ext.data.SimpleStore({
			fields : ["retrunValue", "displayText"],
			data : [['Y', '单人操作'], ['N', '监护操作']]
		}),
		valueField : "retrunValue",
		displayField : "displayText",
		mode : 'local',
		forceSelection : true,
		blankText : '操作方式',
		emptyText : '操作方式',
		hiddenName : 'opticket.isSingle',
		editable : false,
		// value : 'N',
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : false,
		name : 'isSingle',
		width : wd
	});
	// 操作任务名
	var hideTaskName = new Ext.form.Hidden({
		id : 'operateTaskName',
		name : 'opticket.operateTaskName'
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

	// // 开始日期
	// var dfPlanStartTime = new Ext.form.TextField({
	// id : 'planStartTime',
	// fieldLabel : "操作计划开始时间",
	// name : 'opticket.planStartTime',
	// style : 'cursor:pointer',
	// anchor : "98%",
	// readOnly : true,
	// allowBlank : false,
	// value : new Date().dateFormat('Y-m-d H:i:s'),
	// listeners : {
	// focus : function() {
	// WdatePicker({
	// startDate : '%y-%M-%d 00:00:00',
	// dateFmt : 'yyyy-MM-dd HH:mm:ss',
	// alwaysUseStartDate : true
	// });
	// }
	// }
	// })
	//
	// // 结束日期
	// var dfPlanEndTime = new Ext.form.TextField({
	// id : 'planEndTime',
	// fieldLabel : "操作计划结束时间",
	// name : 'opticket.planEndTime',
	// style : 'cursor:pointer',
	// anchor : "98%",
	// readOnly : true,
	// allowBlank : false,
	// value : new Date().dateFormat('Y-m-d H:i:s'),
	// listeners : {
	// focus : function() {
	// WdatePicker({
	// startDate : '%y-%M-%d 00:00:00',
	// dateFmt : 'yyyy-MM-dd HH:mm:ss',
	// alwaysUseStartDate : true
	// });
	// }
	// }
	// })

	// 附件
	// var tfAppend = new Ext.form.TextField({
	// id : "appendixAddr",
	// inputType : 'file',
	// name : 'uploadFile',
	// fieldLabel : "附件",
	// height : 22,
	// width : 300
	// });

	// 备注
	var taRemark = new Ext.form.TextArea({
		id : 'memo',
		fieldLabel : "备注",
		name : 'opticket.memo',
		height : 150,
		anchor : "85%"
	});

	// 保存
	var btnSave = new Ext.Button({
		text : Constants.BTN_SAVE,
		iconCls : Constants.CLS_SAVE,
		handler : saveOpticket
	})

	// 清空
	var btnCancel = new Ext.Button({
		text : '刷新',
		iconCls : 'reflesh',
		handler : function() {
			if (operateCode != "") {
				editOpticket(operateCode);
			} else {
				window.location = window.location;
			}

			// cbtOpTask.setDisabled(false);
			// baseInfoFormPanel.getForm().reset();
		}
	})
	var mainop = new Ext.form.TextField({
		id : "addMainOp",
		readOnly : true,
		// hidden : true,
		labelSeparator : '',
		name : "addMainOp",
		value : "调用标准票",
		style : {
			'text-decoration' : 'underline',
			'text-align' : 'center'
		},
		listeners : {
			focus : function() {
				this.blur();
				var returnValue = window
						.showModalDialog(
								'../../operate/query/standOperateticket.jsp',
								null,
								'dialogWidth=800px;dialogHeight=500px;center=yes;help=no;resizable=no;status=no;');
				if (typeof(returnValue) != "undefined") {
					standTicketNo = returnValue;
					tfOpticketCode.setValue(Constants.AUTO_CREATE);
					Ext.getCmp("operatorName").setDisabled(false);
					cbtOpTask.setDisabled(true);
					// cbxOpticketSpecials.setDisabled(true);
					opticketType.setDisabled(true);
					Ext.Ajax.request({
						url : 'opticket/getDetailOpticketByCode.action',
						method : 'post',
						params : {
							opticketCode : standTicketNo
						},
						success : function(result, request) {
							var record = eval('(' + result.responseText + ')');
							baseInfoFormPanel.getForm().loadRecord(record);

							if (record.data.opticketType.substring(0, 2) == "01") {
								opticketType.setValue("01");
							} else {
								opticketType.setValue("00");
							}
							Ext.getCmp('cbtOpTask').setValue({
								id : record.data.operateTaskId,
								text : record.data.operateTaskName
							});
							// 专业
							if (record.data.specialityCode != 'null') {
								cbxOpticketSpecials
										.setValue(record.data.specialityCode);
							} else {
								cbxOpticketSpecials.setValue('');
							}
							endTicketNo="";
						},
						failure : function(result, request) {
							Ext.Msg.alert('提示信息', '操作失败！')
						}
					})
				}
			}
		}
	})
	var endop = new Ext.form.TextField({
		id : "endop",
		readOnly : true,
		// hidden : true,
		labelSeparator : '',
		name : "endop",
		value : "由终结票生成",
		style : {
			'text-decoration' : 'underline',
			'text-align' : 'center'
		},
		listeners : {
			focus : function() {
				this.blur();
				var returnValue = window
						.showModalDialog(
								'queryEndOptickect.jsp',
								null,
								'dialogWidth=800px;dialogHeight=500px;center=yes;help=no;resizable=no;status=no;');
				if (typeof(returnValue) != "undefined") {
					endTicketNo = returnValue;
					tfOpticketCode.setValue(Constants.AUTO_CREATE);
					Ext.getCmp("operatorName").setDisabled(false);
//					cbtOpTask.setDisabled(true);
					// cbxOpticketSpecials.setDisabled(true);
					opticketType.setDisabled(true);
					Ext.Ajax.request({
						url : 'opticket/getDetailOpticketByCode.action',
						method : 'post',
						params : {
							opticketCode : endTicketNo
						},
						success : function(result, request) {
							var record = eval('(' + result.responseText + ')');
							baseInfoFormPanel.getForm().loadRecord(record);

							if (record.data.opticketType.substring(0, 2) == "01") {
								opticketType.setValue("01");
							} else {
								opticketType.setValue("00");
							}
							Ext.getCmp('cbtOpTask').setValue({
								id : record.data.operateTaskId,
								text : record.data.operateTaskName
							});
							Ext.getCmp('applyNo').setValue('');
							// 专业
							if (record.data.specialityCode != 'null') {
								cbxOpticketSpecials
										.setValue(record.data.specialityCode);
							} else {
								cbxOpticketSpecials.setValue('');
							}
							cbtOpTask.setDisabled(true);
							standTicketNo="";
						},
						failure : function(result, request) {
							Ext.Msg.alert('提示信息', '操作失败！')
						}
					})
				}
			}
		}
	})
	var baseInfoField = new Ext.form.FieldSet({
		border : true,
		labelAlign : 'right',
		buttonAlign : 'center',
		labelWidth : 110,
		layout : 'form',
		title : '操作票填写',
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
				items : [applyNo]
			}, {
				columnWidth : 0.38,
				layout : "form",
				border : false,
				items : [endop]
			}]
		}, {
			layout : "column",
			border : false,
			items : [{
				columnWidth : 0.38,
				layout : "form",
				border : false,
				items : [tfOpticketCode]
			}, {
				columnWidth : 0.38,
				layout : "form",
				border : false,
				items : [mainop]
			}]
		}, {
			columnWidth : 0.38,
			layout : "form",
			border : false,
			items : [opticketType]
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
			items : [{
				id : 'operatorMan',
				name : 'opticket.operatorMan',
				xtype : 'hidden'
			}]
		}, {
			columnWidth : 0.38,
			layout : "form",
			border : false,
			items : [{
				// id : 'operatorBy',
				id : 'operatorName',
				xtype : 'trigger',
				allowBlank : false,
				readOnly : true,
				fieldLabel : "操作人<font color='red'>*</font>",
				blankText : '请选择操作人',
				emptyText : '请选择操作人',
				width : wd,
				onTriggerClick : function(e) {
					var args = {
						selectModel : 'single'
					}
					var emp = window
							.showModalDialog(
									"../../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp",
									args,
									'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
					if (emp != null) {

						Ext.get('operatorMan').dom.value = emp.workerCode
						// opratorCode.setValue() = emp.workerCode;
						Ext.get('operatorName').dom.value = emp.workerName
					}
				}
			}]
		}, {
			layout : 'form',
			items : [
					// isSingle,
					hideTaskName, cbxOpticketSpecials]
		}, {
			layout : "column",
			border : false,
			items : [{
				columnWidth : 0.38,
				layout : "form",
				border : false,
				items : [new Ext.form.TextField({
					id : 'workTicketNo',
					readOnly : true,
					width : wd,
					name : "opticket.workTicketNo",
					fieldLabel : '工作票编号'
				})]
			}, {
				columnWidth : 0.38,
				layout : "form",
				border : false,
				items : [new Ext.form.TextField({
					id : "workticket",
					readOnly : true,
					labelSeparator : '',
					value : "关联工作票",
					style : {
						'text-decoration' : 'underline',
						'text-align' : 'center'
					},
					listeners : {
						focus : function() {
							this.blur();
							var object = window
									.showModalDialog(
											'workticketQuery.jsp',
											null,
											'dialogWidth=800px;dialogHeight=500px;center=yes;help=no;resizable=no;status=no;');
							if (object != null) {
								Ext.get('opticket.workTicketNo').dom.value = object;
							}
						}
					}
				})]
			}]
		},
				// {
				// layout : "column",
				// border : false,
				// items : [{
				// columnWidth : 0.4,
				// layout : "form",
				// border : false,
				// items : [dfPlanStartTime]
				// }, {
				// columnWidth : 0.4,
				// layout : "form",
				// border : false,
				// items : [dfPlanEndTime]
				// }]
				// },
				{
					layout : 'form',
					items : [
					// tfAppend,
					taRemark]
				}],
		buttons : [btnSave, btnCancel]
	});
	if (operateCode == "") {
		cbtOpTask.setDisabled(false);
		Ext.getCmp("operatorName").setDisabled(false);
		cbxOpticketSpecials.setDisabled(false);
		opticketType.setDisabled(false);
		mainop.setVisible(true);
		endop.setVisible(true);
	} else {
		cbtOpTask.setDisabled(true);
		cbxOpticketSpecials.setDisabled(true);
		opticketType.setDisabled(true);
		Ext.getCmp("operatorName").setDisabled(true);
		mainop.setVisible(false);
		endop.setVisible(false);
	}
	var baseInfoFormPanel = new Ext.FormPanel({
		border : false,
		frame : true,
		fileUpload : true,
		items : [baseInfoField]
	});
	// baseInfoFormPanel.on('render',function(){
	// if(operateCode == ""){
	// Ext.get("addMainOp").dom.hidden=false;
	// }else{
	// Ext.get("addMainOp").dom.hidden=true;
	// }
	// })
	var baseInfoViewport = new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		defaults : {
			autoScroll : true
		},
		items : [baseInfoFormPanel]
	});
	// 时间去t处理
	function renderDate(value) {
		if (!value)
			return "";
		var reDate = /\d{4}\-\d{2}\-\d{2}/gi;
		var reTime = /\d{2}:\d{2}:\d{2}/gi;
		var strDate = value.match(reDate);
		var strTime = value.match(reTime);
		if (!strDate)
			return "";
		strTime = strTime ? strTime : '00:00:00';
		return strDate + " " + strTime;
	}

	// 检查计划结束时间是否大于计划开始时间
	function endDateCheck() {
		var startDate = Ext.get("planStartTime").dom.value;
		var endDate = Ext.get("planEndTime").dom.value;
		if (startDate && endDate) {
			return compareDateStr(startDate, endDate);
		}
		return true;
	}

	// 显示时间比较方法
	function compareDate(argDate1, argDate2) {
		return argDate1.getTime() <= argDate2.getTime();
	}

	// textField显示时间比较方法
	function compareDateStr(argDateStr1, argDateStr2) {
		var date1 = Date.parseDate(argDateStr1, 'Y-m-d H:i:s');
		var date2 = Date.parseDate(argDateStr2, 'Y-m-d H:i:s');
		return compareDate(date1, date2);
	}

	// 清除所有项目
	function clearAllFields() {

		// baseInfoFormPanel.getForm().reset();
		// cbtOpTask.on('beforequery', function() {
		// return true
		// });
	}

	// 编辑操作票
	function editOpticket() {
		clearAllFields();
		if (operateCode != "") {
			// 加载数据
			Ext.Ajax.request({
				method : Constants.POST,
				url : 'opticket/getDetailOpticketByCode.action',
				params : {
					opticketCode : operateCode
				},
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
						cbxOpticketSpecials.setValue('');
					}
					tfOpticketCode.setValue(operateCode);
					opticketName.setValue(record.data.opticketName != null
							? record.data.opticketName
							: record.data.operateTaskName)
					opticketType.setValue(record.data.opticketType.substring(0,
							2))
					// // 开始日期
					// dfPlanStartTime.setValue(renderDate(record.data.planStartTime));
					// // 结束日期
					// dfPlanEndTime.setValue(renderDate(record.data.planEndTime));
				},
				failure : function() {
					Ext.Msg.alert(Constants.ERROR, '加载数据失败!');
				}
			});
		}

	}

	// 增加/保存操作票
	function saveOpticket() {
		if (!baseInfoFormPanel.getForm().isValid()) {
			Ext.Msg
					.alert(Constants.SYS_REMIND_MSG,
							'操作类别,操作人，专业,任务单编号是必须输入的字段');
			return;
		}
		var isAdd = tfOpticketCode.value == Constants.AUTO_CREATE;
		var url = 'opticket/addDetailOpticket.action';
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
				standTicketNo : standTicketNo,
				endTicketNo : endTicketNo,
				isStand : 'N'
			},
			success : function(form, action) {
				if (!action.response.responseText) {
					return;
				}
				var o = eval("(" + action.response.responseText + ")");
				Ext.Msg.alert("提示", "操作成功!");
				if (isAdd) {
					// 设置操作票编号
					tfOpticketCode.setValue(o.opticketCode);
					parent.edit(o.opticketCode);
				}
				cbtOpTask.setDisabled(true);
				Ext.getCmp("operatorName").setDisabled(true);
				cbxOpticketSpecials.setDisabled(true);
				opticketType.setDisabled(true);
				parent.tabReport.document.getElementById("queryBtn").click();
			},
			failure : function(form, action) {
				Ext.Msg.alert(Constants.ERROR, Constants.UNKNOWN_ERR);
			}
		});
	}
	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
			success : function(action) {
				var result = eval("(" + action.responseText + ")");
				if (result.workerCode) {
					Ext.get('operatorMan').dom.value = result.workerCode
					Ext.get('operatorName').dom.value = result.workerName
				}
			}
		});
	}
	getWorkCode();
	editOpticket();
});