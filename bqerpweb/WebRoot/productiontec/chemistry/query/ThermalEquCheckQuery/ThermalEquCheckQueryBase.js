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
	if (currentRecord != null) {
		id = currentRecord.get("equ.rlsbjcId");
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
				if (result.deptCode) {
					Ext.get('depCode').dom.value = result.deptCode;
					Ext.get('depName').dom.value = result.deptName;
				}
			}
		});
	}
	getWorkCode();
	// ------------定义form---------------

	var rlsbjcId = new Ext.form.TextField({
		id : 'rlsbjcId',
		name : 'equ.rlsbjcId',
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
		hiddenName : 'equ.deviceCode',
		mode : 'local',
		triggerAction : 'all',
		value : '',
		readOnly : true,
		anchor : "90%"
	});
	apartCodeBox.on('beforequery', function() {
		return false
	});
	var testType = new Ext.form.ComboBox({
		fieldLabel : '检修类别',
		store : new Ext.data.SimpleStore({
			fields : ['value', 'text'],
			data : [['1', '大修'], ['2', '小修'], ['3', '临时检修']]
		}),
		id : 'testType',
		name : 'testType',
		valueField : "value",
		displayField : "text",
		value : '1',
		mode : 'local',
		typeAhead : true,
		forceSelection : true,
		hiddenName : 'equ.testType',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : false,
		anchor : "80%"
	});
	testType.on('beforequery', function() {
		return false
	});

	var beginDate = new Ext.form.TextField({
		id : 'startDate',
		fieldLabel : '检修开始时间',
		name : 'equ.startDate',
		style : 'cursor:pointer',
		readOnly : true,
		anchor : "90%"
	});

	var endDate = new Ext.form.TextField({
		id : 'endDate',
		fieldLabel : '检修结束时间',
		name : 'equ.endDate',
		style : 'cursor:pointer',
		readOnly : true,
		anchor : "80%"
	});

	var examineBy = new Ext.form.TextField({
		id : 'examineBy',
		fieldLabel : '检查人员',
		readOnly : true,
		name : 'equ.examineBy',
		anchor : "90%"
	});

	var chargeBy = {
		fieldLabel : '负责人',
		name : 'chargeBy',
		xtype : 'combo',
		id : 'chargeBy',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		hiddenName : 'equ.chargeBy',
		editable : false,
		anchor : "90%"
	};

	var content = new Ext.form.TextArea({
		id : 'content',
		height : 50,
		fieldLabel : '检查情况',
		readOnly : true,
		name : 'equ.content',
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
		name : 'equ.fillBy'
	});

	var depName = new Ext.form.TextField({
		id : 'depName',
		fieldLabel : '填写部门',
		name : 'depName',
		anchor : '80%',
		readOnly : true
	});

	var depCode = new Ext.form.Hidden({
		hidden : false,
		id : 'depCode',
		name : 'equ.depCode'
	});

	var fillDate = new Ext.form.TextField({
		id : 'fillDate',
		fieldLabel : '填写时间',
		name : 'equ.fillDate',
		readOnly : true,
		value : getDate(),
		anchor : "80%"
	});

	var workApplyField = new Ext.form.FieldSet({
		border : true,
		labelWidth : 80,
		labelAlign : 'right',
		buttonAlign : 'center',
		layout : 'form',
		title : '热力设备检查情况登记',
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
				items : [rlsbjcId]
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
				items : [testType]
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
				columnWidth : 1,
				layout : "form",
				items : [examineBy]
			}]
		}, {
			layout : 'column',
			items : [{
				columnWidth : 1,
				layout : 'form',
				items : [chargeBy]
			}]
		}, {
			layout : 'column',
			items : [{
				columnWidth : 1,
				layout : 'form',
				items : [content]
			}]
		}, {
			layout : 'column',
			items : [{
				columnWidth : 0.5,
				layout : 'form',
				items : [fillName, fillBy]
			}, {
				columnWidth : 0.5,
				layout : 'form',
				hidden : true,
				items : [fillDate]
			}]
		}, {
			layout : 'column',
			items : [{
				columnWidth : 0.5,
				layout : 'form',
				items : [depName, depCode]
			}, {
				columnWidth : 0.5,
				layout : 'form',
				items : [fillDate]
			}]
		}],
		buttons : [{
			text : '清空',
			iconCls : 'cancer',
			handler : function() {
				form.getForm().reset();
				getWorkCode();
			}
		}]
	});

	var form = new Ext.FormPanel({
		border : false,
		frame : true,
		items : [workApplyField],
		bodyStyle : {
			'padding-top' : '5px'
		},
		defaults : {
			labelAlign : 'right'
		}
	});

	// 清除所有Field
	function clearAllFields() {
		form.getForm().reset();
	}

	var workApplyViewport = new Ext.Viewport({
		layout : "fit",
		border : false,
		height : 150,
		items : [form],
		defaults : {
			autoScroll : true
		}
	}).show();
	if (id != null && id != "") {
		Ext.get("rlsbjcId").dom.value = id;
		Ext.get('deviceCode').dom.value = currentRecord.get('deviceName');
		Ext.get('equ.deviceCode').dom.value = currentRecord
				.get('equ.deviceCode');
		Ext.get("startDate").dom.value = currentRecord.get("startDate");
		Ext.get("endDate").dom.value = currentRecord.get("endDate");
		Ext.getCmp('examineBy').setValue(currentRecord.get("equ.examineBy"));
		Ext.getCmp('chargeBy').setValue(currentRecord.get("chargeName"));
		Ext.get("content").dom.value = currentRecord.get("equ.content");
		Ext.getCmp('fillBy').setValue(currentRecord.get("condenser.fillBy"));
		Ext.getCmp('fillName').setValue(currentRecord.get("fillName"));
		Ext.getCmp('fillDate').setValue(currentRecord.get("fillDate"));

	} else {
		form.getForm().reset();
	}

})
