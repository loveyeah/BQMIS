Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

var currentRecord = parent.currentRecord;
var errorMsg = "";
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
	
	var id;
	var annex;
	if (currentRecord != null) {
		id = currentRecord.get("condenser.nqjxlId");
		annex = currentRecord.get("condenser.content");
	}
	
	// 从session取登录人编码姓名部门相关信息
	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
			success : function(action) {
				var result = eval("(" + action.responseText + ")");
				if (result.workerCode) {
					// 设定默认工号，赋给全局变量
					Ext.get('fillBy').dom.value = result.workerCode;
					Ext.get('fillName').dom.value = result.workerName;
				}
			}
		});
	}
	getWorkCode();
	// ------------定义form---------------

	var nqjxlId = new Ext.form.TextField({
		id : 'nqjxlId',
		name : 'condenser.nqjxlId',
		anchor : "80%",
		hidden : true
	});
	// 所属机组
	var storeCharge = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'workticket/getDetailEquList.action'
		}),
		reader : new Ext.data.JsonReader({
			root : 'list'
		}, [{
			name : 'blockCode',
			mapping : 'blockCode'
		}, {
			name : 'blockName',
			mapping : 'blockName'
		}])
	});
	storeCharge.load();
	var apartCodeBox = new Ext.form.ComboBox({
		id : 'deviceCode',
		fieldLabel : "机组",
		store : storeCharge,
		displayField : "blockName",
		valueField : "blockCode",
		hiddenName : 'condenser.deviceCode',
		mode : 'local',
		triggerAction : 'all',
		value : '',
		readOnly : true,
		anchor : "90%"
	});

	var waterQuanlity = new Ext.form.TextField({
		id : 'waterQuanlity',
		fieldLabel : '凝结水水质',
		name : 'condenser.waterQuanlity',
		anchor : "80%"
	});

	var place = new Ext.form.TextField({
		id : 'place',
		fieldLabel : '部位',
		name : 'condenser.place',
		anchor : "90%"
	});

	var beginDate = new Ext.form.TextField({
		id : 'startDate',
		fieldLabel : '泄露开始时间',
		name : 'condenser.startDate',
		style : 'cursor:pointer',
		anchor : "90%",
		value : getDate(),
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-%d 00:00:00',
					dateFmt : 'yyyy-MM-dd HH:mm:ss',
					alwaysUseStartDate : true,
					onpicked : function() {
						if (endDate.getValue() != "") {
							if (beginDate.getValue() == ""
									|| beginDate.getValue() > endDate
											.getValue()) {
								Ext.Msg.alert("提示", "必须小于后一日期");
								beginDate.setValue("");
								return;
							}
						}
					},
					onclearing : function() {
						beginDate.markInvalid();
					}
				});
			}
		}
	});

	var endDate = new Ext.form.TextField({
		id : 'endDate',
		fieldLabel : '泄露结束时间',
		name : 'condenser.endDate',
		style : 'cursor:pointer',
		anchor : "80%",
		value : getDate(),
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-%d 00:00:00',
					dateFmt : 'yyyy-MM-dd HH:mm:ss',
					alwaysUseStartDate : true,
					onpicked : function() {
						if (beginDate.getValue() == ""
								|| beginDate.getValue() > endDate.getValue()) {
							Ext.Msg.alert("提示", "必须大于前一日期");
							endDate.setValue("");
							return;
						}
					},
					onclearing : function() {
						endDate.markInvalid();
					}
				});
			}
		}
	});

	// 附件
	var conFile = {
		id : "conFile",
		xtype : 'fileuploadfield',
		isFormField : true,
		name : "conFile",
		fieldLabel : '内容',
		height : 21,
		anchor : "100%",
		buttonCfg : {
			text : '浏览...',
			iconCls : 'upload-icon'
		}
	}

	// 查看
	var btnView = new Ext.Button({
		id : 'btnView',
		text : '查看',
		handler : function() {
			window.open(bview);
		}
	});
	btnView.setVisible(false);

	var handleStep = new Ext.form.TextArea({
		id : 'handleStep',
		height : 50,
		fieldLabel : '处理措施',
		name : 'condenser.handleStep',
		anchor : "90%"
	});

	var handleResult = new Ext.form.TextArea({
		id : 'handleResult',
		height : 50,
		fieldLabel : '处理结果',
		name : 'condenser.handleResult',
		anchor : "90%"
	});

	var memo = new Ext.form.TextArea({
		id : "memo",
		height : 50,
		fieldLabel : '备注',
		name : 'condenser.memo',
		anchor : "90%"
	});
	
	var fillName = new Ext.form.TextField({
		id : 'fillName',
		fieldLabel : '填写人',
		name : 'fillName',
		anchor : '80%',
		readOnly : true

	});
	// 填报人编码
	var fillBy = new Ext.form.Hidden({
		hidden : false,
		id : "fillBy",
		name : 'condenser.fillBy'
	});
	var fillDate = new Ext.form.TextField({
		id : 'fillDate',
		fieldLabel : '填写时间',
		name : 'condenser.fillDate',
		readOnly : true,
		value : getDate(),
		anchor : "80%"
	});
	// --------------定义凝汽器泄漏明细grid------------------------------------------
	var MyRecord = Ext.data.Record.create([{
		name : 'nqjxlDetailId'
	}, {
		name : 'nqjxlId'
	}, {
		name : 'projectNames'
	}, {
		name : 'itemName1'
	}, {
		name : 'itemName2'
	}, {
		name : 'itemName3'
	}, {
		name : 'itemName4'
	}]);
	var dataProxy = new Ext.data.HttpProxy({
		url : 'productionrec/findcondenserDetailsList.action'
	});
	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	}, MyRecord);
	var store = new Ext.data.Store({
		proxy : dataProxy,
		reader : theReader
	});

	function queryRecord() {
		store.load({
			params : {
				nqjxlId : (currentRecord == null || currentRecord == "")
						? ""
						: currentRecord.get('condenser.nqjxlId')
			}
		});
	}

	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true
	});

	// 明细grid
	var detailsGrid = new Ext.grid.EditorGridPanel({
		region : "center",
		frame : false,
		border : false,
		height : 330,
		autoScroll : true,
		enableColumnMove : false,
		sm : sm,
		 tbar : [{
		 text : '保存',
		 iconCls : 'save',
		 handler : updateHandler
		 }],
		store : store,
		columns : [new Ext.grid.RowNumberer({
			header : '行号',
			width : 35,
			align : 'left'
		}), {
			header : "ID",
			sortable : true,
			width : 50,
			dataIndex : 'nqjxlDetailId',
			hidden : true
		}, {
			header : "项目名称",
			anchor : '100%',
			sortable : true,
			dataIndex : 'projectNames',
			align : 'center'
		}, {
			header : "YD(EPB)",
			anchor : '100%',
			css : CSS_GRID_INPUT_COL,
			sortable : true,
			dataIndex : 'itemName1',
			renderer : function returnValue(val) {
				if (val == "0") {
					return '';
				} else {
					return val;
				}
			},
			editor : new Ext.form.NumberField({
				maxLength : 15,
				decimalPrecision: 4, // 默认的小数点位数  
				maxLengthText : '最多输入15个数字！'
			}),
			align : 'center'
		}, {
			header : "DD(US/CM)",
			anchor : '100%',
			css : CSS_GRID_INPUT_COL,
			sortable : true,
			
			dataIndex : 'itemName2',
			renderer : function returnValue(val) {
				if (val == "0") {
					return '';
				} else {
					return val;
				}
			},
			editor : new Ext.form.NumberField({
				maxLength : 15,
				decimalPrecision: 4, // 默认的小数点位数  
				maxLengthText : '最多输入15个数字！'
				
			}),
			align : 'center'
		}, {
			header : "NA+(PPB)",
			anchor : '100%',
			css : CSS_GRID_INPUT_COL,
			sortable : true,
			dataIndex : 'itemName3',
			renderer : function returnValue(val) {
				if (val == "0") {
					return '';
				} else {
					return val;
				}
			},
			editor : new Ext.form.NumberField({
				maxLength : 15,
				decimalPrecision: 4, // 默认的小数点位数  
				maxLengthText : '最多输入15个数字！'
			}),
			align : 'center'
		}, {
			header : "SIO2(PPB)",
			anchor : '100%',
			css : CSS_GRID_INPUT_COL,
			sortable : true,
			dataIndex : 'itemName4',
			renderer : function returnValue(val) {
				if (val == "0") {
					return '';
				} else {
					return val;
				}
			},
			editor : new Ext.form.NumberField({
				maxLength : 15,
				decimalPrecision: 4, // 默认的小数点位数  
				maxLengthText : '最多输入15个数字！'
			}),
			align : 'center'
		}]
	});
	var workApplyField = new Ext.Panel({
		border : true,
		labelWidth : 80,
		region : 'north',
		labelAlign : 'right',
		autoHeight : true,
		style : {
			"margin-top" : "20px",
			"margin-left" : "10px",
			"margin-right" : Ext.isIE6
					? (Ext.isStrict ? "-10px" : "-13px")
					: "0"
		},
		items : [{
			layout : 'column',
			hidden : true,
			items : [{
				columnWidth : 1,
				layout : 'form',
				items : [nqjxlId]
			}]
		}, {
			layout : 'column',
			items : [{
				columnWidth : 0.5,
				layout : 'form',
				items : [apartCodeBox]
			}, {
				columnWidth : 0.5,
				layout : 'form',
				items : [waterQuanlity]
			}]
		}, {
			layout : 'column',
			items : [{
				columnWidth : 1,
				layout : 'form',
				items : [place]
			}]
		}, {
			layout : 'column',
			items : [{
				columnWidth : 0.5,
				layout : 'form',
				items : [beginDate]
			}, {
				columnWidth : 0.5,
				layout : 'form',
				items : [endDate]
			}]
		}, {
			layout : 'column',
			items : [{
				columnWidth : 0.78,
				border : false,
				layout : "form",
				items : [conFile]
			}, {
				columnWidth : 0.22,
				border : false,
				layout : "form",
				items : [btnView]
			}]
		}, {
			layout : 'column',
			items : [{
				columnWidth : 1,
				layout : 'form',
				items : [handleStep]
			}]
		}, {
			layout : 'column',
			items : [{
				columnWidth : 1,
				layout : 'form',
				items : [handleResult]
			}]
		}, {
			layout : 'column',
			items : [{
				columnWidth : 1,
				layout : 'form',
				items : [memo]
			}]
		}, {
			layout : 'column',
			items : [{
				columnWidth : 0.5,
				layout : 'form',
				items : [fillName,fillBy]
			}, {
				columnWidth : 0.5,
				layout : 'form',
				items : [fillDate]
			}]
		}]
	});

	var form = new Ext.form.FormPanel({
		border : false,
		frame : true,
		layout : 'form',
		fileUpload : true,
		items : [workApplyField, detailsGrid],
		bodyStyle : {
			'padding-top' : '5px'
		},
		defaults : {
			labelAlign : 'right'
		}
	});

	var workApply = new Ext.form.FieldSet({
		border : true,
		labelWidth : 80,
		labelAlign : 'right',
		layout : 'form',
		title : '台账信息',
		autoHeight : true,
		items : [form]
	});

	// 清除所有Field
	function clearAllFields() {
		 form.getForm().reset();
		 getWorkCode();
		 store.load();
	}

	var workApplyViewport = new Ext.Viewport({
		layout : "fit",
		border : false,
		autoScroll : true,
		height : 150,
		items : [workApply]

	}).show();
	if (id != null && id != "") {
		Ext.get("nqjxlId").dom.value = id;
		Ext.get('deviceCode').dom.value = currentRecord.get('deviceName');
		Ext.get('condenser.deviceCode').dom.value = currentRecord.get('condenser.deviceCode');
		Ext.get('waterQuanlity').dom.value = currentRecord.get("condenser.waterQuanlity");
		Ext.get("place").dom.value = currentRecord.get("condenser.place");
		Ext.get("startDate").dom.value = currentRecord.get("startDate");
		Ext.get("endDate").dom.value = currentRecord.get("endDate");
		Ext.get("handleStep").dom.value = currentRecord.get("condenser.handleStep");
		Ext.get("handleResult").dom.value = currentRecord.get("condenser.handleResult");
		Ext.get("memo").dom.value = currentRecord.get("condenser.memo");
		Ext.getCmp('fillBy').setValue(currentRecord.get("condenser.fillBy"));
		Ext.getCmp('fillName').setValue(currentRecord.get("fillName"));
		Ext.getCmp('fillDate').setValue(currentRecord.get("fillDate"));
		if(currentRecord.get("condenser.content")!=null&&currentRecord.get("condenser.content")!="")
		        {
		       bview=currentRecord.get("condenser.content");
		       btnView.setVisible(true); 
		         Ext.get("conFile").dom.value = bview.replace('/power/upload_dir/productionrec/','');
		        }
		        else
		        {
		        	  btnView.setVisible(false);
		        }
		queryRecord();
		
	} else {
		form.getForm().reset();
		queryRecord();
	}

	function checkData() {
		if (Ext.get('deviceCode').dom.value == null
				|| Ext.get('deviceCode').dom.value == "")
			errorMsg = "机组不能为空，请选择！" + "\r\n";
		if (Ext.get('startDate').dom.value == null
				|| Ext.get('startDate').dom.value == "")
			errorMsg = "泄露开始日期不能为空，请选择！" + "\r\n";
		if (Ext.get('endDate').dom.value == null
				|| Ext.get('endDate').dom.value == "")
			errorMsg = "泄露结束日期不能为空，请选择！" + "\r\n";
		if (errorMsg == null || errorMsg == "")
			return true
		else
			return false;
	}
	/**
	 * 修改处理
	 */
	
	function updateHandler() {
		if (!checkData()) {
			Ext.Msg.alert("提示信息", errorMsg);
			errorMsg = "";
			return;
		}
		var modifyRec = new Array();
		detailsGrid.stopEditing(); // 停止编辑
		if (store != null) {
			for (i = 0; i <= store.getCount() - 1; i++) {
				modifyRec[i] = store.getAt(i);
			}
		}
		if (modifyRec.length > 0) {
			Ext.Msg.confirm('提示', '是否确认保存数据? ', function(button, text) {
				if (button == 'yes') {
					Ext.Msg.wait("正在保存数据,请等待...");
					var modifyRecords = new Array();
					for (var i = 0; i < modifyRec.length; i++) {
						modifyRecords.push(modifyRec[i].data);
					}
					if (!form.getForm().isValid()) {
						return;
					}
					form.getForm().submit({
						url : 'productionrec/modifyCondenserLeakDetails.action',
						method : 'post',
						params : {
							addOrUpdateRecords : Ext.util.JSON.encode(modifyRecords),
							filePath : Ext.get("conFile").dom.value,
						 nqjxlId : (currentRecord == null || currentRecord ==
						 "")
						 ? "" : currentRecord.get('condenser.nqjxlId')
						},
						success : function(result, request) {
							Ext.MessageBox.alert('提示', '操作成功！ ');
							clearAllFields();
							if (parent.document.all.iframe1 != null) {
						parent.document.all.iframe1.src = parent.document.all.iframe1.src;
						parent.Ext.getCmp("maintab").setActiveTab(0);
				}
						},
						failure : function(result, request) {
							Ext.MessageBox.alert('提示', '操作失败！ ');
						}
					})
				}
			})
		} else {
			Ext.Msg.alert('提示', '您没有做任何修改！');
		}
	}
	
	function loadAnnex() {
		if (annex != null && annex != "") {
			bview = annex;
			btnView.setVisible(true);
		} else {
			btnView.setVisible(false);
		}
	}
	loadAnnex();
})
