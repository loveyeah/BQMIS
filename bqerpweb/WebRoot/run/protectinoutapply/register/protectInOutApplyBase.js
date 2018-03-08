Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
var _currentRecord = typeof(parent.currentRecord) != null
		? parent.currentRecord
		: null;
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
var currentDate = getDate();
Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
	var id;
	var no;
	var sessWorname = document.getElementById("workerName").value;
	var sessWorcode = document.getElementById("workerCode").value;
	var dept = document.getElementById("dept").value;
	var deptName = document.getElementById("deptName").value;
	var currentRecord;
	var applyId = {
		id : "applyId",
		xtype : "hidden",
		fieldLabel : 'ID',
		value : '',
		readOnly : true,
		name : 'power.applyId',
		anchor : "40%"
	}
	var protectNo = {
		id : "protectNo",
		xtype : "textfield",
		fieldLabel : '申请单号',
		value : '自动生成',
		cls : 'disable',
		readOnly : true,
		name : 'power.protectNo',
		anchor : "93%"
	}
	var applyDept = {
		fieldLabel : '申请部门',
		cls : 'disable',
		name : 'applyDept',
		xtype : 'combo',
		id : 'applyDept',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		hiddenName : 'power.applyDept',
		allowBlank : false,
		editable : false,
		anchor : "85%",
		triggerAction : 'all',
		onTriggerClick : function() {
		}
	};

	var applyBy = {
		fieldLabel : '申请人<font color="red">*</font>',
		name : 'applyBy',
		xtype : 'combo',
		id : 'applyBy',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		hiddenName : 'power.applyBy',
		allowBlank : false,
		editable : false,
		anchor : "85%",
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
			var args = {
				selectModel : 'single'
			}
			var emp = window
					.showModalDialog(
							url,
							args,
							'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
			if (typeof(emp) != "undefined") {
				Ext.getCmp('applyBy').setValue(emp.workerCode);
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('applyBy'), emp.workerName);

				Ext.getCmp('applyDept').setValue(emp.deptCode);
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('applyDept'), emp.deptName);
			}
		}
	};

	var applyDate = new Ext.form.TextField({
		id : 'applyDate',
		fieldLabel : "申请日期",
		cls : 'disable',
		name : 'power.applyDate',
		style : 'cursor:pointer',
		readOnly : true,
		anchor : "85%",
		value : currentDate
	});
	applyDate.setValue(currentDate);
	var hideEquCode = {
		id : 'hideEquCode',
		name : 'power.equCode',
		xtype : 'hidden'
	}
	var equCode = new Ext.form.TriggerField({
		fieldLabel : '设备名称',
		id : "equCode",
		name : "equCode",
		onTriggerClick : mainEquSelect,
		anchor : "93%"
	});
	function mainEquSelect() {
		var url = "../../../equ/base/business/kksselect/selectAttribute.jsp";
		var equ = window.showModalDialog(url, window,
				'dialogWidth=600px;dialogHeight=400px;status=no');
		if (typeof(equ) != "undefined") {
			var name = equ.name;
			var code = equ.code;
			equCode.setValue(name);
			Ext.getCmp("hideEquCode").setValue(code);

			// 设置所属系统
			// cbxChargeBySystem.setValue(code.substring(0,2) );
		}
	}

	// var equCode = {
	// fieldLabel : '设备名称',
	// name : 'equCode',
	// xtype : 'combo',
	// id : 'equCode',
	// store : new Ext.data.SimpleStore({
	// fields : ['id', 'name'],
	// data : [[]]
	// }),
	// mode : 'remote',
	// hiddenName : 'power.equCode',
	// anchor : "93%",
	// triggerAction : 'all',
	// onTriggerClick : function() {
	//
	// var url = "../../../equ/base/business/kksselect/selectAttribute.jsp";
	// var equ = window.showModalDialog(url, '',
	// 'dialogWidth=400px;dialogHeight=400px;status=no');
	// if (typeof(equ) != "undefined") {
	// Ext.getCmp('equCode').setValue(equ.code);
	// Ext.form.ComboBox.superclass.setValue.call(Ext
	// .getCmp('equCode'), equ.name);
	// }
	// }
	// };

	var protectName = {
		id : "protectName",
		// height:100,
		xtype : "textfield",
		fieldLabel : '保护名称<font color="red">*</font>',
		name : 'power.protectName',
		anchor : "85%"
	}
	var protectReason = {
		id : "protectReason",
		height : 80,
		xtype : "textarea",
		fieldLabel : '退出原因<font color="red">*</font>',
		name : 'power.protectReason',
		anchor : "93%"
	}
	var equEffect = {
		id : "equEffect",
		height : 80,
		xtype : "textarea",
		fieldLabel : '对系统或设备的影响',
		name : 'power.equEffect',
		anchor : "93%"
	}

	var outSafety = {
		id : "outSafety",
		height : 80,
		xtype : "textarea",
		fieldLabel : '保护退出时安措',
		name : 'power.outSafety',
		anchor : "93%"
	}
	var memo = {
		id : "memo",
		height : 60,
		xtype : "textarea",
		fieldLabel : '备注',
		name : 'power.memo',
		anchor : "93%"
	}
	var applyStartDate = new Ext.form.TextField({
		id : 'applyStartDate',
		fieldLabel : "申请开工时间",
		name : 'power.applyStartDate',
		style : 'cursor:pointer',
		anchor : "85%",
		value : currentDate,
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
	applyStartDate.setValue(currentDate);
	var applyEndDate = new Ext.form.TextField({
		id : 'applyEndDate',
		fieldLabel : "申请完工时间",
		name : 'power.applyEndDate',
		style : 'cursor:pointer',
		anchor : "85%",
		value : currentDate,
		listeners : {
			focus : function() {
				var pkr = WdatePicker({
					startDate : '%y-%M-%d 00:00:00',
					dateFmt : 'yyyy-MM-dd HH:mm:ss',
					alwaysUseStartDate : true
				});
			}
		}
	});
	applyEndDate.setValue(currentDate);
	var baseInfoField = new Ext.form.FieldSet({
		autoHeight : true,
		labelWidth : 120,
		style : {
			"padding-top" : '5',
			"padding-left" : '20'
		},
		bodyStyle : Ext.isIE ? 'padding:0 0 5px 5px;' : 'padding:5px 5px;',
		anchor : '-20',
		border : false,
		buttonAlign : 'center',
		items : [applyId, protectNo, equCode, hideEquCode, {
			layout : "column",
			border : false,
			items : [{
				columnWidth : 0.5,
				layout : "form",
				border : false,
				items : [applyBy]
			}, {
				columnWidth : 0.5,
				layout : "form",
				border : false,
				items : [applyDept]
			}]
		}, {
			layout : "column",
			border : false,
			items : [{
				columnWidth : 0.5,
				layout : "form",
				border : false,
				items : [protectName]
			}, {
				columnWidth : 0.5,
				layout : "form",
				border : false,
				items : [applyDate]
			}]
		}, {
			layout : "column",
			border : false,
			items : [{
				columnWidth : 0.5,
				layout : "form",
				border : false,
				items : [applyStartDate]
			}, {
				columnWidth : 0.5,
				layout : "form",
				border : false,
				items : [applyEndDate]
			}]
		}, protectReason, equEffect, outSafety, memo],
		buttons : [{
			text : '保存',
			iconCls : 'save',
			handler : savePower
		}, {
			text : '清空',
			iconCls : 'cancer',
			handler : function() {
				form.getForm().reset();
			}
		}]

	});

	var form = new Ext.FormPanel({
		border : false,
		frame : true,
		fileUpload : true,
		items : [baseInfoField],
		bodyStyle : {
			'padding-top' : '5px'
		},
		defaults : {
			labelAlign : 'right'
		}
	});
	function savePower() {
		var msg = "";
		if (Ext.get("applyBy").dom.value == "") {
			msg = "申请人";
		}
		if (Ext.get("protectName").dom.value == "") {
			if (msg != "") {
				msg = msg + ",保护名称";
			} else {
				msg = "保护名称";
			}
		}
		if (Ext.get("protectReason").dom.value == "") {
			if (msg != "") {
				msg = msg + ",投退原因";
			} else {
				msg = "投退原因";
			}
		}
		if (msg != "") {
			Ext.Msg.alert('提示', "请输入'" + msg + "'!");
			return;
		}
		if (applyStartDate.getValue() >= applyEndDate.getValue()) {
			Ext.Msg.alert('提示', '申请完工时间应大于开工时间！');
			return;
		}
		var url = "";
		if (Ext.get("protectNo").dom.value == "自动生成") {
			url = "protect/addProtectApply.action?isIn=N"
		} else {
			url = "protect/updateProtectApply.action"
		}

		form.getForm().submit({
			method : 'POST',
			url : url,
			success : function(form, action) {

				var o = eval("(" + action.response.responseText + ")");
				Ext.Msg.alert("注意", o.msg);
				// ----------
				if (o.msg == "增加成功！") {
					Ext.get("applyId").dom.value = o.id;
					Ext.get("protectNo").dom.value = o.no;
					clearAllFields();
				}
				if (parent.document.all.iframe2 != null) {
					parent.document.all.iframe2.src = "run/protectinoutapply/register/protectInOutApplyList.jsp";
				}
			},
			faliue : function() {
				Ext.Msg.alert('错误', '出现未知错误.');
			}
		});
	}

	// 清除所有Field
	function clearAllFields() {
		form.getForm().reset();
	}

	var baseInfoViewport = new Ext.Viewport({
		layout : "fit",
		border : false,
		items : [form],
		defaults : {
			autoScroll : true
		}
	}).show();
	if (_currentRecord != null) {
		var isIn = _currentRecord.get("power.isIn");
		if (isIn != null && isIn != "") {
			if (isIn == "Y") {
				// 找相关的退出
				var reNo = _currentRecord.get("power.relativeNo");
				if (reNo == null || reNo == "") {
					currentRecord = null;
				} else {
					Ext.Ajax.request({
						url : 'protect/findByNo.action',
						method : 'post',
						params : {
							protectNo : reNo
						},
						success : function(result, request) {
							currentRecord = Ext.decode(result.responseText);
							form.getForm().loadRecord(currentRecord);
							Ext.getCmp('equCode')
									.setValue(currentRecord.data.equName);
							Ext.getCmp('hideEquCode')
									.setValue(currentRecord.data.equCode);
							Ext.getCmp('applyBy')
									.setValue(currentRecord.data.applyBy);
							Ext.form.ComboBox.superclass.setValue.call(Ext
									.getCmp('applyBy'),
									currentRecord.data.applyName);
							Ext.getCmp('applyDept')
									.setValue(currentRecord.data.applyDept);

							Ext.form.ComboBox.superclass.setValue.call(Ext
									.getCmp('applyDept'),
									currentRecord.data.applyDeptName);
						},
						failure : function(result, request) {
							currentRecord = null;
						}
					})
				}
			} else {
				currentRecord = _currentRecord;
				id = currentRecord.get("power.applyId");
				no = currentRecord.get("power.protectNo")
			}
		}
	}
	if (currentRecord != null) {
		form.getForm().loadRecord(currentRecord);
		Ext.getCmp('equCode').setValue(currentRecord.get("equName"));
		Ext.getCmp('applyBy').setValue(currentRecord.get("power.applyBy"));
		Ext.form.ComboBox.superclass.setValue.call(Ext.getCmp('applyBy'),
				currentRecord.get("applyName"));
		Ext.getCmp('applyDept').setValue(currentRecord.get("power.applyDept"));

		Ext.form.ComboBox.superclass.setValue.call(Ext.getCmp('applyDept'),
				currentRecord.get("applyDeptName"));
	} else {
		Ext.getCmp('applyBy').setValue(sessWorcode);
		Ext.form.ComboBox.superclass.setValue.call(Ext.getCmp('applyBy'),
				sessWorname);
		Ext.getCmp('applyDept').setValue(dept);

		Ext.form.ComboBox.superclass.setValue.call(Ext.getCmp('applyDept'),
				deptName);
	}
		// if (id != null && id != "") {
		// Ext.get("applyId").dom.value = id;
		// Ext.get("protectNo").dom.value = no;
		// Ext.getCmp('applyBy').setValue(currentRecord.get("power.applyBy"));
		// Ext.form.ComboBox.superclass.setValue.call(Ext.getCmp('applyBy'),
		// currentRecord.get("applyName"));
		// Ext.getCmp('applyDept').setValue(currentRecord.get("power.applyDept"));
		//
		// Ext.form.ComboBox.superclass.setValue.call(Ext.getCmp('applyDept'),
		// currentRecord.get("applyDeptName"));
		// Ext.get("applyDate").dom.value = currentRecord.get("applyDate");
		// Ext.getCmp('equCode').setValue(currentRecord.get("equName"));
		// Ext.getCmp('hideEquCode').setValue(currentRecord.get("power.equCode"));
		// // Ext.form.ComboBox.superclass.setValue.call(Ext.getCmp('equCode'),
		// // currentRecord.get("equName"));
		// Ext.get("protectName").dom.value = currentRecord
		// .get("power.protectName");
		// Ext.getCmp('applyStartDate').setValue(currentRecord
		// .get("applyStartDate"));
		// Ext.getCmp('applyEndDate').setValue(currentRecord.get("applyEndDate"));
		// Ext.get("protectReason").dom.value = currentRecord
		// .get("power.protectReason");
		// Ext.get("equEffect").dom.value =
		// currentRecord.get("power.equEffect");
		// Ext.get("outSafety").dom.value =
		// currentRecord.get("power.outSafety");
		// Ext.get("memo").dom.value = currentRecord.get("power.memo");
		//
		// } else {
		// form.getForm().reset();
		// }
})
