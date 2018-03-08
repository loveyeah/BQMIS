Ext.ns("repairMaint.approveList");

repairMaint.approveList = function() {

	function getYear() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10);
		return s;
	}
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

		Ext.QuickTips.init();
		Ext.form.Field.prototype.msgTarget = 'side';
	}
	var id = "";
	var specialityName = "";
	var entryId = "";
	var acceptanceLevel = "";
	
	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
			success : function(action) {
				var result = eval("(" + action.responseText + ")");
				if (result.workerCode) {
					// 设定默认工号
					sessWorcode = result.workerCode;
					sessWorname = result.workerName;
					loadData();
				}
			}
		});
	}
	function loadData() {
		Ext.get('fillBy').dom.value = sessWorcode;
		Ext.get('fillName').dom.value = sessWorname;
	};

	var fillName = new Ext.form.TextField({
		id : 'fillName',
		fieldLabel : '填写人',
		name : 'fillName',
		anchor : "85%",
		readOnly : true
	});
	var fillBy = new Ext.form.Hidden({
		id : "fillBy",
		name : 'main.fillBy'
	});
	var fillTime = new Ext.form.TextField({
		id : 'fillTime',
		fieldLabel : '填写时间',
		name : 'main.fillDate',
		readOnly : true,
		value : getDate(),
		anchor : "85%"
	});

	var repairProjectName = new Ext.form.TextField({
		id : 'repairProjectName',
		name : 'main.repairProjectName',
		fieldLabel : '项目名称',
		readOnly : true,
		anchor : "92%"
	})
	var workingMenbers = new Ext.form.TextField({
		id : 'workingMenbers',
		fieldLabel : '工作成员',
		readOnly : true,
		anchor : "85%"
	})
	var workingCharge = new Ext.form.Hidden({
		id : 'workingCharge',
		fieldLabel : '工作负责人id',
		readOnly : true,
		anchor : "85%"
	})
	var workingChargeName = new Ext.form.TextField({
		id : 'workingChargeName',
		fieldLabel : '工作负责人',
		readOnly : true,
		anchor : "85%"
	})
	var startEndTime = new Ext.form.TextField({
		id : 'startEndTime',
		fieldLabel : '实际工期',
		readOnly : true,
		anchor : "85%"
	});
	var planStartEndDate = new Ext.form.TextField({
		id : 'planStartEndDate',
		fieldLabel : '计划工期',
		readOnly : true,
		anchor : "85%"
	});
	var cbxRepairSpecail = new Ext.form.TextField({
		id : 'specialityId',
		fieldLabel : '检修专业',
		readOnly : true,
		anchor : "85%"
	})
	var finish = new Ext.form.TextArea({
		id : 'finish',
		fieldLabel : "完成情况填写",
		height : 70,
		anchor : "92%",
		readOnly : true
	});
	var memo = new Ext.form.TextArea({
		id : 'memo',
		fieldLabel : "备注",
		height : 70,
		anchor : "92%",
		readOnly : true
	});
	var acceptanceLevelName = new Ext.form.TextField({
		id : 'acceptanceLevel',
		fieldLabel : '验收级别',
		readOnly : true
	})
	var acceptId = new Ext.form.Hidden({
		id : 'acceptId',
		name : 'main.acceptId',
		fieldLabel : '验收ID'
	})

	var formtbar = new Ext.Toolbar({
		items : [{
			id : 'btnSign',
			text : "审批",
			iconCls : 'write',
			handler : signApprove
		}, '-', {
			id : 'btnQuery',
			text : "会签查询",
			iconCls : 'view',
			handler : meetQuery
		}, '-', {
			id : 'btnMeet',
			text : "会签表",
			iconCls : 'pdfview',
			handler : checkRptPreview
		}]
	});

	var form = new Ext.form.FormPanel({
		bodyStyle : "padding:5px 5px 0",
		labelAlign : 'right',
		autoHeight : true,
		region : 'center',
		border : false,
		tbar : formtbar,
		items : [new Ext.form.FieldSet({
			title : '检修项目验收基本信息',
			collapsible : true,
			height : '100%',
			layout : 'form',
			items : [{
				border : false,
				layout : 'column',
				items : [{
					columnWidth : 1,
					layout : 'form',
					border : false,
					labelWidth : 110,
					items : [repairProjectName]
				}, {
					columnWidth : .5,
					layout : 'form',
					labelWidth : 110,
					border : false,
					items : [workingMenbers]
				}, {
					columnWidth : .5,
					layout : 'form',
					border : false,
					labelWidth : 110,
					items : [workingCharge, workingChargeName]
				}, {
					columnWidth : .5,
					layout : 'form',
					border : false,
					labelWidth : 110,
					items : [planStartEndDate]
				}, {
					columnWidth : .5,
					layout : 'form',
					labelWidth : 110,
					border : false,
					items : [startEndTime]
				}, {
					columnWidth : .5,
					layout : 'form',
					labelWidth : 110,
					border : false,
					items : [cbxRepairSpecail]
				}, {
					columnWidth : .5,
					layout : 'form',
					labelWidth : 110,
					border : false,
					items : [fillName, fillBy]
				}, {
					border : false,
					layout : 'form',
					columnWidth : .5,
					labelWidth : 110,
					items : [acceptanceLevelName]
				}, {
					border : false,
					layout : 'form',
					columnWidth : .5,
					labelWidth : 110,
					items : [fillTime]
				}, {
					border : false,
					layout : 'form',
					columnWidth : 1,
					labelWidth : 110,
					items : [finish]
				}, {
					border : false,
					layout : 'form',
					columnWidth : 1,
					labelWidth : 110,
					items : [memo]
				}]
			}]
		})]
	});

	var layout = new Ext.Panel({
		autoWidth : true,
		autoHeight : true,
		border : false,
		autoScroll : true,
		split : true,
		items : [form]
	});

	function signApprove() {
		if (id == "" || id == null) {
			Ext.Msg.alert('提示', '请先从列表中选择一行记录！');
			return false;
		}
		var url;
		url = "approveSign.jsp";
		var args = new Object();
		args.acceptId = id;
		args.entryId = entryId;
		args.acceptanceLevel = acceptanceLevel;
		args.specialityName = cbxRepairSpecail.getValue();
		var o = window.showModalDialog(url, args,
				'dialogWidth=800px;dialogHeight=600px;status=no');
		if (o) {
			form.getForm().reset();
			id = "";
			entryId = "";
			Ext.getCmp("div_tabs").setActiveTab(0);
			Ext.getCmp("div_grid").getStore().reload();
			// tabs.setActiveTab(0);
			// var _url1 =
			// "run/repair/business/repairAcceptApprove/repairAcceptApproveList.jsp"
			// parent.Ext.getCmp("maintab").setActiveTab(1);
			// parent.document.all.iframe1.src = _url1;

		}
	}

	function meetQuery() {
		if (id == null || id == "") {
			Ext.Msg.alert('提示', '请先从列表中选择一行记录！');
			return false;
		}
		if (entryId == null || entryId == "") {
			url = "/power/workflow/manager/flowshow/flowshow.jsp?flowCode="
					+ "bqRepairAcceptApprove";
			window.open(url);
		} else {
			var url = "/power/workflow/manager/show/show.jsp?entryId="
					+ entryId;
			window.open(url);
		}
	}

	function checkRptPreview() {
		if (id == "" || id == null) {
			Ext.Msg.alert('提示', '请先从列表中选择一行记录!');
			return false;
		}
		var url = "/powerrpt/report/webfile/bqmis/repairAccept.jsp?acceptId="
				+ id;
		window.open(url);
	};

	return {
		grid : layout,
		setFormRec : function(v) {
			var rec = v;
			form.getForm().loadRecord(v);
			planStartEndDate.setValue(v.get('planStartEndDate'));
			startEndTime.setValue(v.get('startEndTime'));
			finish.setValue(v.get('completion'));
			acceptanceLevelName.setValue(v.get('acceptanceLevelName'));
			cbxRepairSpecail.setValue(v.get('specialityName'));
			if (v.get('planStartEndDate') == "----") {
				planStartEndDate.setValue("");
			} else {
				planStartEndDate.setValue(v.get('planStartEndDate'));
			}
			id = v.get('acceptId');
			entryId = v.get('workflowNo');
			acceptanceLevel = v.get('acceptanceLevel');
			getWorkCode();
		}
	}
}