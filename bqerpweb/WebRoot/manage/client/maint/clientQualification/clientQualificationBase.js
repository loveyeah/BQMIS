Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

var currentRecord = parent.currentRecord;

function getDate() {
	var d, s, t;
	d = new Date();
	s = d.getFullYear().toString(10) + "-";
	t = d.getMonth() + 1;
	s += (t > 9 ? "" : "0") + t + "-";
	t = d.getDate();
	s += (t > 9 ? "" : "0") + t + " ";
	return s;
}
var yeardata = '';
var myDate = new Date();
var myMonth = myDate.getMonth() + 1;
myMonth = (myMonth < 10 ? "0" + myMonth : myMonth);

for (var i = 2004; i < myDate.getFullYear() + 7; i++) {
	if (i < myDate.getFullYear() + 6)
		yeardata += '[' + i + ',' + i + '],';
	else
		yeardata += '[' + i + ',' + i + ']';
}
var yeardata = eval('[' + yeardata + ']');

Ext.onReady(function() {
	var id;
	if (currentRecord != null) {
		id = currentRecord.get("qualificationId");
	}
	function selectClientsInfo() {
		var clientsInfo = window
				.showModalDialog("/power/manage/client/maint/clientsContact/selectClientsInfo.jsp");
		if (typeof(clientsInfo) != "undefined") {
			clientName.setValue(clientsInfo.clientName);
			Ext.get('cliendId').dom.value = clientsInfo.cliendId;
		}
	}

	var qualificationId = {
		id : "qualificationId",
		xtype : "hidden",
		fieldLabel : 'ID',
		value : '',
		readOnly : true,
		name : 'qualification.qualificationId'
	}
	var aptitudeName = {
		id : "aptitudeName",
		xtype : "textfield",
		fieldLabel : '资质材料名称',
		name : 'qualification.aptitudeName',
		width : 450
	}

	var clientName = new Ext.form.ComboBox({
		id : 'clientName',
		fieldLabel : "合作伙伴名称",
		name : 'clientName',
		width : 450,
		readOnly : true,
		allowBlank : true,
		onTriggerClick : function() {
			selectClientsInfo();
		}
	});

	var cliendId = new Ext.form.Hidden({
		id : 'cliendId',
		name : 'qualification.cliendId'
	});

	var qualificationOrg = {
		id : "qualificationOrg",
		xtype : "textfield",
		fieldLabel : '发证机构',
		name : 'qualification.qualificationOrg',
		width : 450
	}

	var sendPaperDate = new Ext.form.TextField({
		id : 'sendPaperDate',
		fieldLabel : "发证日期",
		name : 'qualification.sendPaperDate',
		style : 'cursor:pointer',
		width : 450,
		value : getDate(),
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-%d',
					dateFmt : 'yyyy-MM-dd',
					alwaysUseStartDate : true
				});
			}
		}
	});

	var beginDate = new Ext.form.TextField({
		id : 'beginDate',
		fieldLabel : "证书生效日期",
		name : 'qualification.beginDate',
		style : 'cursor:pointer',
		width : 150,
		value : getDate(),
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-%d',
					dateFmt : 'yyyy-MM-dd',
					alwaysUseStartDate : true,
					onpicked : function() {
						if (endDate.getValue() != "") {
							if (beginDate.getValue() == ""
									|| beginDate.getValue() > endDate
											.getValue()) {
								Ext.Msg.alert("提示", "必须小于证书失效日期");
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
		fieldLabel : "证书失效日期",
		name : 'qualification.endDate',
		style : 'cursor:pointer',
		width : 145,
		value : getDate(),
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-%d',
					dateFmt : 'yyyy-MM-dd',
					alwaysUseStartDate : true,
					onpicked : function() {
						if (beginDate.getValue() == ""
								|| beginDate.getValue() > endDate.getValue()) {
							Ext.Msg.alert("提示", "必须大于证书生效日期");
							endDate.setValue("")
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

	var memo = {
		id : "memo",
		height : 90,
		xtype : "textarea",
		fieldLabel : '备注',
		name : 'qualification.memo',
		//anchor : "75%"
		width : 450
	}

	var workApplyField = new Ext.form.FieldSet({
		border : true,
		labelWidth : 80,
		labelAlign : 'right',
		buttonAlign : 'center',
		layout : 'form',
		title : '合作伙伴资质登记',
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
				columnWidth : 1,
				layout : "form",
				border : false,
				items : [qualificationId]
			}, {
				columnWidth : 1,
				layout : "form",
				border : false,
				items : [aptitudeName]
			}, {
				columnWidth : 1,
				layout : "form",
				border : false,
				items : [clientName, cliendId]
			}, {
				columnWidth : 1,
				layout : "form",
				border : false,
				items : [qualificationOrg]
			}, {
				columnWidth : 1,
				layout : "form",
				border : false,
				items : [sendPaperDate]
			}]
		}, {
			layout : "column",
			border : false,
			items : [{
				columnWidth : 0.43,
				layout : "form",
				border : false,
				items : [beginDate]
			}, {
				columnWidth : 0.43,
				layout : "form",
				border : false,
				items : [endDate]
			}]
		}, {
			columnWidth : 1,
			layout : "form",
			border : false,
			items : [memo]
		}],
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
		items : [workApplyField],
		bodyStyle : {
			'padding-top' : '5px'
		},
		defaults : {
			labelAlign : 'right'
		}
	});
	function savePower() {
		var msg = "";
		if (Ext.get("aptitudeName").dom.value == "") {
			msg = "资质材料名称";
		}
		if (Ext.get("clientName").dom.value == "") {
			if (msg != "") {
				msg = msg + ",合作伙伴";
			} else {
				msg = "合作伙伴";
			}
		}
		if (Ext.get("qualificationOrg").dom.value == "") {
			if (msg != "") {
				msg = msg + ",发证机构";
			} else {
				msg = "发证机构";
			}
		}
		if (Ext.get("sendPaperDate").dom.value == "") {
			if (msg != "") {
				msg = msg + ",发证日期";
			} else {
				msg = "发证日期";
			}
		}
		if (Ext.get("beginDate").dom.value == "") {
			if (msg != "") {
				msg = msg + ",证书生效日期";
			} else {
				msg = "证书生效日期";
			}
		}
		if (Ext.get("endDate").dom.value == "") {
			if (msg != "") {
				msg = msg + ",证书失效日期";
			} else {
				msg = "证书失效日期";
			}
		}
		if (msg != "") {
			Ext.Msg.alert('提示', "请输入'" + msg + "'!");
			return;
		}
		var url = "";
		if (Ext.get("qualificationId").dom.value == ""
				|| Ext.get("qualificationId").dom.value == null) {
			url = "clients/addQualification.action"
		} else {
			url = "clients/updateQualification.action"
		}
		form.getForm().submit({
			method : 'POST',
			url : url,
			success : function(form, action) {
				var o = eval("(" + action.response.responseText + ")");
				Ext.Msg.alert("注意", o.msg);
				// ----------
				// 清除Field

				if (o.msg == "增加成功！") {
					Ext.get("qualificationId").dom.value = o.id;
					clearAllFields();
				}
				if (parent.document.all.iframe1 != null) {
						parent.document.all.iframe1.src = parent.document.all.iframe1.src;
						parent.Ext.getCmp("maintab").setActiveTab(0);
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
		Ext.get("qualificationId").dom.value = id;
		Ext.get("cliendId").dom.value = currentRecord.get("cliendId");
		Ext.getCmp('clientName').setValue(currentRecord.get("cliendId"));
		Ext.form.ComboBox.superclass.setValue.call(Ext.getCmp('clientName'),
				currentRecord.get("clientName"));
		Ext.getCmp('aptitudeName').setValue(currentRecord.get("aptitudeName"));
		Ext.getCmp('qualificationOrg').setValue(currentRecord
				.get("qualificationOrg"));
		Ext.get("sendPaperDate").dom.value = currentRecord.get("sendPaperDate");
		Ext.get("beginDate").dom.value = currentRecord.get("beginDate");
		Ext.get("endDate").dom.value = currentRecord.get("endDate");
		Ext.get("memo").dom.value = currentRecord.get("memo");
	} else {
		form.getForm().reset();
	}
})
