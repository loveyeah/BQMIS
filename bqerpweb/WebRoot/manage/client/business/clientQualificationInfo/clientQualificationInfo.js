//资质材料信息
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

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
	var id = getParameter("qualificationId");
	
	if(id != "")
	{
		Ext.Ajax.request({
			url : 'clients/findQualificationById.action',
			params : {
				qualificationId : id
			},
			method : 'post',
			waitMsg : '正在加载数据...',
			success : function(result, request) {
				var o = eval('(' + result.responseText + ')');
				form.getForm().loadRecord(o);
			},
			failure : function(result, request) {
				Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
			}
		});
	}
	var qualificationId = {
		id : "qualificationId",
		xtype : "hidden",
		fieldLabel : 'ID',
		value : '',
		readOnly : true,
		name : 'qualification.qualificationId',
		anchor : "40%"
	}
	var aptitudeName = {
		id : "aptitudeName",
		xtype : "textfield",
		fieldLabel : '资质材料名称',
		readOnly : true,
		name : 'qualification.aptitudeName',
		anchor : "75%"
	}

	var clientName = new Ext.form.ComboBox({
		id : 'clientName',
		fieldLabel : "合作伙伴名称",
		name : 'clientName',
		anchor : '75%',
		readOnly : true,
		allowBlank : true
	});

	var cliendId = new Ext.form.Hidden({
		id : 'cliendId',
		name : 'qualification.cliendId'
	});

	var qualificationOrg = {
		id : "qualificationOrg",
		xtype : "textfield",
		fieldLabel : '发证机构',
		readOnly : true,
		name : 'qualification.qualificationOrg',
		anchor : "75%"
	}

	var sendPaperDate = new Ext.form.TextField({
		id : 'sendPaperDate',
		fieldLabel : "发证日期",
		name : 'qualification.sendPaperDate',
		style : 'cursor:pointer',
		readOnly : true,
		anchor : "75%"
	});

	var beginDate = new Ext.form.TextField({
		id : 'beginDate',
		fieldLabel : "证书生效日期",
		name : 'qualification.beginDate',
		style : 'cursor:pointer',
		anchor : "75%",
		readOnly : true
	});

	var endDate = new Ext.form.TextField({
		id : 'endDate',
		fieldLabel : "证书失效日期",
		name : 'qualification.endDate',
		style : 'cursor:pointer',
		anchor : "75%",
		readOnly : true
	});

	var memo = {
		id : "memo",
		height : 90,
		xtype : "textarea",
		fieldLabel : '备注',
		readOnly : true,
		name : 'qualification.memo',
		anchor : "75%"
	}

	var workApplyField = new Ext.form.FieldSet({
		border : true,
		labelWidth : 80,
		labelAlign : 'right',
		buttonAlign : 'center',
		layout : 'form',
		title : '合作伙伴资质信息',
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

	var workApplyViewport = new Ext.Viewport({
		layout : "fit",
		border : false,
		height : 150,
		items : [form],
		defaults : {
			autoScroll : true
		}
	}).show();
})
