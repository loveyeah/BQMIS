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

	s = s.substring(0, 10)
	return s;
}
Ext.onReady(function() {

	var id;
	var saveMark;
	if (currentRecord != null) {
		id = currentRecord.get('notice.dztzdId');
		saveMark = currentRecord.get('notice.saveMark');
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

	var dztzdId = new Ext.form.TextField({
		id : "dztzdId",
		fieldLabel : '通知单ID',
		name : 'notice.dztzdId',
		anchor : "90%"
	});

	var deviceId = new Ext.form.Hidden({
		hidden : false,
		id : "deviceId",
		name : 'notice.deviceId'
	});
	
	var deviceName = new Ext.form.TextField({
		id : 'deviceName',
		fieldLabel : '保护装置',
		readOnly : true,
		anchor : "90%",
		listeners : {
			focus : deviceSelect
		}
	});
	
	//增加成功后，保护装置不可再更改
	if (id != null && id != "") {
		var deviceName = new Ext.form.TextField({
			id : 'deviceName',
			fieldLabel : '保护装置',
			readOnly : true,
			anchor : "90%"
		});
	}
		
	var protectedDevice = new Ext.form.TextField({
		id : "protectedDevice",
		fieldLabel : '被保护的设备',
		name : 'protectedDevice',
		readOnly : true,
		anchor : "90%"
	});

	var dzjssmId = new Ext.form.Hidden({
		hidden : false,
		id : "dzjssmId",
		name : 'notice.dzjssmId'
	});
	
	var dzjssmName = new Ext.form.TextField({
		id : "dzjssmName",
		name : 'dzjssmName',
		fieldLabel : '计算说明',
		readOnly : true,
		anchor : "90%",
		listeners : {
			focus : calculateSelect
		}
	});

	var dztzdCode = new Ext.form.TextField({
		id : "dztzdCode",
		fieldLabel : '通知单编号',
		name : 'notice.dztzdCode',
		anchor : "90%"
	});

	var ctCode = new Ext.form.TextArea({
		id : "ctCode",
		fieldLabel : 'CT变比',
		name : 'notice.ctCode',
		height : 50,
		anchor : "90%"
	});

	var ptCode = new Ext.form.TextArea({
		id : "ptCode",
		fieldLabel : 'PT变比',
		name : 'notice.ptCode',
		height : 50,
		anchor : "90%"
	});

	var memo = new Ext.form.TextArea({
		id : "memo",
		height : 50,
		fieldLabel : '备注',
		name : 'notice.memo',
		anchor : "90%"
	});

	var effectiveDate = new Ext.form.TextField({
		id : 'effectiveDate',
		fieldLabel : '生效时间',
		name : 'notice.effectiveDate',
		style : 'cursor:pointer',
		anchor : '80%',
		value : getDate(),
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-%d 00:00:00',
					dateFmt : 'yyyy-MM-dd HH:mm:ss',
					alwaysUseStartDate : true
				});
			}
		}
	});

	var useStatus = new Ext.form.ComboBox({
		fieldLabel : '使用状态',
		store : new Ext.data.SimpleStore({
			fields : ['value', 'text'],
			data : [['1', '作废'], ['2', '执行'], ['3', '未执行']]
		}),
		id : 'useStatus',
		name : 'useStatus',
		valueField : "value",
		displayField : "text",
		value : '2',
		mode : 'local',
		typeAhead : true,
		forceSelection : true,
		hiddenName : 'notice.useStatus',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : false,
		anchor : "80%"
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
		name : 'notice.fillBy'
	});

	// 定义grid
	var MyRecord = Ext.data.Record.create([{
		name : 'notice.dztzdId'
	}, {
		name : 'dzdjbId'
	}, {
		name : 'fixvalueItemId'
	}, {
		name : 'fixvalueItemName'
	}, {
		name : 'protectTypeId'
	}, {
		name : 'protectTypeName'
	}, {
		name : 'wholeFixvalue'
	}, {
		name : 'memo'
	}]);
	var dataProxy = new Ext.data.HttpProxy({
		url : 'productionrec/findNoticeDetailList.action'
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
//				start : 0,
//				limit : 18,
				dztzdId : (currentRecord == null || currentRecord == "")
						? ""
						: currentRecord.get('notice.dztzdId')
			}
		});
	}

	function queryDetail()
	{
		store.load({
			params : {
				start : 0,
				limit : 18
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
			id:'saveRecord',
			iconCls : 'save',
			handler : updateHandler
		}],
		store : store,
		columns : [new Ext.grid.RowNumberer({
			header : '行号',
			width : 35,
			align : 'left'
		}), {
			header : "通知单ID",
			width : 100,
			sortable : true,
			hidden : true,
			dataIndex : 'notice.dztzdId'
		}, {
			header : "登记ID",
			width : 80,
			sortable : true,
			hidden : true,
			dataIndex : 'dzdjbId'
		}, {
			header : "项目ID",
			width : 80,
			sortable : true,
			hidden : true,
			dataIndex : 'fixvalueItemId'
		}, {
			header : "定值项目",
			width : 120,
			align : 'center',
			sortable : true,
			dataIndex : 'fixvalueItemName'
		}, {
			header : "类型ID",
			width : 80,
			sortable : true,
			hidden : true,
			align : 'center',
			dataIndex : 'protectTypeId'
		}, {
			header : "保护类型",
			width : 120,
			sortable : true,
			align : 'center',
			dataIndex : 'protectTypeName'
		}, {
			header : "整定值",
			width : 120,
			sortable : true,
			css : CSS_GRID_INPUT_COL,
			align : 'center',
			editor : new Ext.form.TextField({
				allowDecimals : false,
				allowNegative : false
			}),
			dataIndex : 'wholeFixvalue'
		}, {
			header : "备注",
			width : 250,
			css : CSS_GRID_INPUT_COL,
			sortable : true,
			align : 'center',
			editor : new Ext.form.TextField({
				allowDecimals : false,
				allowNegative : false
			}),
			dataIndex : 'memo'
		}]
	});
	var workApplyField = new Ext.form.FieldSet({
		 border : false,
        labelAlign : 'right',
        labelWidth : 85,
        layout : 'form',
        width : 750,
        defaults : {
        	autoScroll : true
        },
    	autoHeight : true,
        style : {
            "padding-top" : "5px",
            "padding-left" : "5px",
            'border' : 0
        },
		items : [{
			layout : "column",
			border : false,
			items : [{
				columnWidth : 1,
				layout : "form",
				hidden : true,
				border : false,
				items : [dztzdId]
			}]
		}, {
			layout : "column",
			border : false,
			items : [{
				columnWidth : 1,
				layout : "form",
				border : false,
				items : [dztzdCode]
			}]
		}, {
			layout : "column",
			border : false,
			items : [{
				columnWidth : 1,
				layout : "form",
				border : false,
				items : [dzjssmName,dzjssmId]
			}]
		}, {
			layout : "column",
			border : false,
			items : [{
				columnWidth : 1,
				layout : "form",
				border : false,
				items : [deviceName,deviceId]
			}]
		}, {
			layout : "column",
			border : false,
			items : [{
				columnWidth : 1,
				layout : "form",
				border : false,
				items : [protectedDevice]
			}]
		}, {
			layout : "column",
			border : false,
			items : [{
				columnWidth : 1,
				layout : "form",
				border : false,
				items : [ctCode]
			}]
		}, {
			layout : "column",
			border : false,
			items : [{
				columnWidth : 1,
				layout : "form",
				border : false,
				items : [ptCode]
			}]
		}, {
			layout : "column",
			border : false,
			items : [{
				columnWidth : 1,
				layout : "form",
				border : false,
				items : [memo]
			}]
		}, {
			layout : "column",
			border : false,
			items : [{
				columnWidth : 0.5,
				layout : "form",
				border : false,
				items : [effectiveDate]
			}, {
				columnWidth : 0.5,
				layout : "form",
				border : false,
				items : [useStatus]
			}, {
				columnWidth : 0.5,
				layout : "form",
				border : false,
				items : [fillName, fillBy]
			}]
		}]
	});

	  
    var form = new Ext.form.FormPanel({
    	layout : 'form',
    	frame : false,
    	border : false,
    	//height : 750,
    	items : [workApplyField]
    });
    var panel = new Ext.Panel({
    	region : 'north',
    	border : false,
    	autoScroll : true,
    	layout : 'fit',
        height : 330,
    	items : [form]
    });
    
	// 清除所有Field
	function clearAllFields() {
		 form.getForm().reset();
	}

	 var workApplyViewport = new Ext.Viewport({   
        layout : "border",
        autoHeight : true,
        defaults : {
        	autoScroll : true
        },
        items : [panel, detailsGrid]
    }).show();
	if (id != null && id != "") {
		Ext.get("dztzdId").dom.value = id;
		Ext.get('notice.dzjssmId').dom.value = currentRecord.get('notice.dzjssmId');
		Ext.get('dzjssmName').dom.value = currentRecord.get('jssmName');
		Ext.get('deviceId').dom.value = currentRecord.get("notice.deviceId");
		Ext.get('deviceName').dom.value = currentRecord.get("equName");
		Ext.get("protectedDevice").dom.value = currentRecord.get("protectedDevice");
		
		Ext.get("dztzdCode").dom.value = currentRecord.get("notice.dztzdCode");
		Ext.get("ctCode").dom.value = currentRecord.get("notice.ctCode");
		Ext.get("ptCode").dom.value = currentRecord.get("notice.ptCode");
		Ext.get("memo").dom.value = currentRecord.get("notice.memo");
		Ext.get("effectiveDate").dom.value = currentRecord.get("effectiveDate");
		Ext.get("notice.useStatus").dom.value = currentRecord.get("notice.useStatus");
		Ext.getCmp('fillBy').setValue(currentRecord.get("notice.fillBy"));
		Ext.getCmp('fillName').setValue(currentRecord.get("fillName"));

		queryRecord();

	} else {
		queryRecord();
	}

	function checkData() {
		if (Ext.get('dztzdCode').dom.value == null
				|| Ext.get('dztzdCode').dom.value == "")
			errorMsg = "通知单号不能为空，请填写！" + "\r\n";
		if (Ext.get('deviceId').dom.value == null
				|| Ext.get('deviceId').dom.value == "")
			errorMsg = errorMsg + "保护装置不能为空，请选择！" + "\r\n";
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
			Ext.Msg.confirm('提示', '是否确认保存数据?', function(button, text) {
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
						url : 'productionrec/addAndUpdateRecord.action',
						method : 'post',
						params : {
							addOrUpdateRecords : Ext.util.JSON
									.encode(modifyRecords),
							dztzdId : (currentRecord == null || currentRecord == "")
									? ""
									: currentRecord.get('notice.dztzdId')
						},
						success : function(form, action) {
							var result = eval('('
									+ action.response.responseText + ')');
							Ext.Msg.alert("提示", result.msg);
							
							clearAllFields();
							queryDetail();
							getWorkCode();			
							
							if (parent.document.all.iframe2 != null) {
								parent.document.all.iframe2.src = "productiontec/relayProtection/business/protectionNotice/protectionNoticeList.jsp";
								// parent.Ext.getCmp("maintab").setActiveTab(1);
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
	
	/**
	 * 保护装置选择页面
	 */
	function deviceSelect()
	{
		var url = "../deviceSelect.jsp";
		var equ = window.showModalDialog(url, window,
				'dialogWidth=600px;dialogHeight=400px;status=no');
		if (typeof(equ) != "undefined") {
			deviceId.setValue(equ.deviceId);
			deviceName.setValue(equ.deviceCode);
			deviceName.setValue(equ.deviceName);
			protectedDevice.setValue(equ.protectedDeviceName);
		}
	}
	
	/**
	 * 计算说明选择页面
	 */
	function calculateSelect()
	{
		var url = "../calculateInstruSelect.jsp";
		var equ = window.showModalDialog(url, window,
				'dialogWidth=600px;dialogHeight=400px;status=no');
		if (typeof(equ) != "undefined") {
			dzjssmId.setValue(equ.dzjssmId);
			dzjssmName.setValue(equ.jssmName);
		}
	}
	
	if(saveMark == 'Y')
	{
		Ext.get('saveRecord').dom.disabled = true;
	}
})
