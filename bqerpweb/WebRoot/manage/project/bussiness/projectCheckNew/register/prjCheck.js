var addStartReport = function() {

	var checkId = "";

	// 系统当前时间
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

	
	var prjRegister = new maint.prjRegList();
	
//立项名称
var prjReg = new Ext.form.ComboBox({
	id:'prjReg',
	fieldLabel:'立项名称',
	store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
	}),
	mode : 'remote',
	anchor : "91%",
	editable : false,
	triggerAction : 'all',
		onTriggerClick : function() {
				prjRegister.win.show();
	}
});

var prjId = new Ext.form.Hidden({
	name:'check.prjId'
})

prjRegister.win.on('hide',function(){
	var value = prjRegister.returnValue;
	prjReg.setValue(value.prjName);
	prjId.setValue(value.prjId);
});
	
	// 从session取登录人编码姓名部门相关信息
	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
			success : function(action) {
				var result = eval("(" + action.responseText + ")");
				if (result.workerCode) {
					// 设定默认工号，赋给全局变量
					entry_by.setValue(result.workerName);
				}
			}
		});
	}
	getWorkCode();
	function setProNo() {
		Ext.Ajax.request({
			url : 'manageproject/getPrjNo.action',
			method : 'post',
			success : function(result, request) {
				var json = result.responseText;
				var o = eval("(" + json + ")");
				report_code.setValue(o.prjNo)
			}
		});
	}

	function resetForm() {
		checkId = "";
		blockForm.getForm().reset();
		getWorkCode();
		// setProNo();
	}
	// 合同编号
	var con_no = new Ext.form.ComboBox({
		fieldLabel : '项目名称',
		id : 'con_no',
		allowBlank : false,
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		hiddenName : 'check.contractName',
		editable : true,
		anchor : "85%",

		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../prjContractQuery/prjContract.jsp";
			var emp = window
					.showModalDialog(
							url,
							'dialogWidth:750px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');

			if (typeof(emp) != "undefined") {
				con_no.setValue(emp.contract_name);
				con_name.setValue(emp.con_id);
				// Ext.form.ComboBox.superclass.setValue.call(Ext
				// .getCmp('chargeByName'), emp.workerName);
			}
		}
	});

	con_no.on('blur', function() {
		con_no.setValue(con_no.getValue());
	})

	// 工程合同名称
	var con_name = new Ext.form.Hidden({
		inputType : 'text',
		name : 'check.conId'
	})

	// 编号
	var report_code = new Ext.form.TextField({
		fieldLabel : "编号",
		inputType : 'text',
		anchor : "85%",
		readOnly : true,
		name : 'check.reportCode'
	})

	// 开工日期
	var start_date = new Ext.form.TextField({
		id : 'startDate',
		fieldLabel : '工程开始时间',
		style : 'cursor:pointer',
		anchor : "85%",
		allowBlank : false,
		name : 'check.startDate',
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%m-%d',
					dateFmt : 'yyyy-MM-dd',
					alwaysUseStartDate : true

				});

			}
		}
	})

	// 竣工日期
	var end_date = new Ext.form.TextField({
		id : 'endDate',
		fieldLabel : '工程结束日期',
		style : 'cursor:pointer',
		anchor : "85%",
		allowBlank : false,
		name : 'check.endDate',
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%m-%d',
					dateFmt : 'yyyy-MM-dd',
					alwaysUseStartDate : true

				});

			}
		}
	})
	// 承包负责人
	var chargeBy = new Ext.form.TextField({
		id : 'chargeBy',
		name : 'check.chargeBy',
		xtype : 'textfield',
		fieldLabel : '承包负责人',
		// readOnly : true,
		allowBlank : false,
		anchor : "85%"
	});
	// 承包单位名称
	var cliendBox = new Ext.form.ComboBox({
		name : 'check.contractorName',
		id : 'cliendId',
		fieldLabel : '承包单位名称',
		mode : 'remote',
		editable : false,
		anchor : "85%",
		allowBlank : false,
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../../../comm/jsp/supplierQuery/supplierQuery.jsp";
			var client = window
					.showModalDialog(
							url,
							null,
							'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');

			if (typeof(client) != "undefined") {
				cliendBox.setValue(client.supplyName);
				contractorId.setValue(client.supplierId);
				// hidcliendBox.setValue(client.cliendId);
			}
		}
	});
	// 承包单位Id
	var contractorId = new Ext.form.Hidden({
		id : 'contractorId',
		name : 'check.contractorId'
	});

	// 发包部门负责人
	var entry_by = new Ext.form.TextField({
		fieldLabel : "填写人",
		inputType : 'text',
		name : 'entryby_name',
		anchor : "85%",
		readOnly : true
	})

	// 发包部门负责人
	var deptChargeBy_code = new Ext.form.TextField({
		fieldLabel : "发包部门负责人",
		inputType : 'text',
		name : 'check.deptChargeBy',
		anchor : "85%"
	})
	
	var deptChargeBy = new Ext.form.ComboBox({
			fieldLabel : '负责人',
			name:'approveby',
			anchor : "85%",
			editable:false,
			onTriggerClick : function() {
									var args = {
										selectModel : 'single',
										rootNode : {
											id : "0",
											text : '滨淮热电厂'
										}
									}
									var url = "/power/comm/jsp/hr/workerByDept/workerByDept2.jsp";
									var rvo = window
											.showModalDialog(
													url,
													args,
													'dialogWidth:550px;dialogHeight:400px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
									if (typeof(rvo) != "undefined") {
										deptChargeBy.setValue(rvo.workerName);
										deptChargeBy_code.setValue(rvo.workerCode);
									}
								}
		})

	// 发包部门工程方负责人验收评价
	var checkAppraise = new Ext.form.TextField({
		fieldLabel : "发包部门工程方负责人验收评价",
		inputType : 'text',
		name : 'check.checkAppraise',
		anchor : "85%"
	})
	// 填写时间
	var entry_date = new Ext.form.TextField({
		fieldLabel : "填写时间",
		inputType : 'text',
		name : 'model.entryDate',
		anchor : "85%",
		value : getDate(),
		readOnly : true
	})
	var report_id = new Ext.form.TextField({
		inputType : 'text',
		name : 'model.reportId',
		anchor : "85%",
		readOnly : true
	})

	function checkInput() {
		var warning = '';
		if (con_no.getValue() == '') {
			warning += '合同编号、';
		}
		if (con_name.getValue() == '') {
			warning += '合同名称、';
		}
		if (report_code.getValue() == '') {
			warning += '编号、';
		}
		if (prj_funds.getValue() == '') {
			warning += '投资、';
		}
		if (start_date.getValue() == '') {
			warning += '开工时间、';
		}
		if (end_date.getValue() == '') {
			warning += '竣工时间、';
		}
		if (prj_location.getValue() == '') {
			warning += '工程地点、';
		}
		if (prj_type_id.getValue() == '') {
			warning += '工程类别、';
		}
		if (entry_by.getValue() == '') {
			warning += '填写人、';
		}
		if (entry_date.getValue() == '') {
			warning += '填写时间、';
		}
		if (warning != '') {
			Ext.Msg.alert('警告', warning.substring(0, warning.length - 1)
					+ '不能为空！');
			return;
		}

		if (start_date.getValue() < getDate()) {
			Ext.Msg.alert('警告', '开工时间应大于当前时间！');
			return;
		}
		if (start_date.getValue() > end_date.getValue()) {
			Ext.Msg.alert('警告', '开工时间不能大于竣工时间！');
			return;
		}

	}

	var btnAdd = new Ext.Button({
		text : '新增',
		iconCls : 'add',
		handler : resetForm
	})

	// 保存按钮
	var save = new Ext.Button({
		text : '保存',
		iconCls : 'save',
		handler : function() {
			// checkInput();
			if (!blockForm.getForm().isValid()) {
				return false;
			};
			if (start_date.getValue() == ""|| end_date.getValue() == ""||start_date.getValue() > end_date.getValue()) {
				Ext.Msg.alert("提示", "工程结束日期必须大于工程开始日期");
				return;
			};
			if(prjId.getValue()==''||prjReg.getValue()==''){
				Ext.Msg.alert('提示','立项名称不能为空！');
				return ;
			}
			var url = "";
			if (checkId != "" && checkId != null) {
				url = 'manageproject/updateCheck.action';
			} else {
				url = 'manageproject/saveCheck.action';
			}
			blockForm.getForm().submit({
				method : 'POST',
				url : url,
				params : {
					checkId : checkId
				},
				success : function(form, action) {
					var o = eval("(" + action.response.responseText + ")");
					Ext.Msg.alert("注意", o.msg);
					checkId = o.checkId;
					report_code.setValue(o.reportCode);
				},
				faliue : function() {
					Ext.Msg.alert('错误', '保存失败');
				}
			});

		}
	});
	// 上报按钮
	var reportBtn = new Ext.Button({
		text : '上报',
		iconCls : 'upcommit',
		handler : function() {
			//alert();
		//update by kzhang 20100821
		if (checkId==null||checkId=="") {
			Ext.Msg.alert("提示", "请选择一项工程竣工验收单进行上报！");
			return false;
		}
				var args=new Object();
				args.busiNo=checkId;
				args.flowCode="prjEndChek";
				args.entryId="";
				args.title="工程竣工验收单上报";
				var danger = window.showModalDialog('reportSign.jsp',
                args, 'dialogWidth=700px;dialogHeight=500px;center=yes;help=no;resizable=no;status=no;');
                blockForm.getForm().reset();
                checkId=null;
		}
	});

	var deleteButton = new Ext.Button({
		id : 'delete',
		text : '删除',
		iconCls : 'delete',
		handler : function() {
			deleteRecord();
		}

	})
	// 删除记录
	function deleteRecord() {
		// var records = repGrid.selModel.getSelections();
		// var ids = [];
		// if (records.length == 0) {
		// Ext.Msg.alert("提示", "请选择要删除的记录！");
		// } else {
		//
		// for (var i = 0; i < records.length; i += 1) {
		// var member = records[i];
		// if (member.get("checkId")) {
		// ids.push(member.get("checkId"));
		// } else {
		//
		// repStore.remove(repStore.getAt(i));
		// }
		// }

		// Ext.Msg.confirm("删除", "是否确定删除所选的记录？", function(buttonobj) {
		//
		// if (buttonobj == "yes") {
		Ext.lib.Ajax.request('POST', 'manageproject/deleteCheck.action', {
			success : function(action) {
				Ext.Msg.alert("提示", "删除成功！")
				resetForm();

			},
			failure : function() {
				Ext.Msg.alert('错误', '删除时出现未知错误.');
			}
		}, 'ids=' + checkId);
		// }
		// });
		//
		// }

	}

	// 表单对象
	var blockForm = new Ext.form.FormPanel({
		labelAlign : 'right',
//		 frame : true,
		labelWidth : 200,
//		 style : 'padding:100px,100px,200px,100px',
		
		layout : 'column',
		closeAction : 'hide',
		fileUpload : true,
		items : [{
			layout : "column",
			border : false,
			items : [{
						columnWidth : 1,
						layout : "form",
						border : false,
						items : [prjReg]
					},{
//				columnWidth : 1.2,
//				layout : "form",
//				border : false,
//				items : [{
//					layout : "column",
//					border : false,
//					items : [{
						//modify by ypan 20100915
						columnWidth : 0.5,
						layout : "form",
						border : false,
						items : [con_no]
					}, {
						columnWidth : 1,
						layout : "form",
						hidden : true,
						border : false,
						items : [con_name]
					}, {
						columnWidth : 0.5,
						layout : "form",
						border : false,
						items : [report_code]
					},{
						columnWidth : 1,
						layout : "form",
						hidden:true,
						border : false,
						items : [prjId]
					},
//				}, {
//					layout : "column",
//					border : false,
//					items : [{
					{
						columnWidth : 0.5,
						layout : "form",
						border : false,
						items : [start_date]
					}, {
						columnWidth : 1,
						layout : "form",
						hidden : true,
						border : false,
						items : [contractorId]
					}, {
						columnWidth : 0.5,
						layout : "form",
						border : false,
						items : [end_date]
					},{
//				}, {
//					layout : "column",
//					border : false,
//					items : [{
						columnWidth : 0.5,
						layout : "form",
						border : false,
						items : [cliendBox]
					}, {
						columnWidth : 0.5,
						layout : "form",
						border : false,
						items : [chargeBy]
					},{
//				}, {
//					layout : "column",
//					border : false,
//					items : [{
						columnWidth : 0.5,
						layout : "form",
						border : false,
						items : [deptChargeBy]
					}, {
						columnWidth : 0.5,
						layout : "form",
						border : false,
						items : [checkAppraise]
					},{
//				}, {
//					layout : "column",
//					border : false,
//					items : [{
						columnWidth : 0.5,
						layout : "form",
						border : false,
						items : [entry_by]
					}, {
						columnWidth : 0.5,
						layout : "form",
						border : false,
						items : [entry_date]
					}, {
						columnWidth : 1,
						layout : "form",
						hidden:true,
						border : false,
						items : [deptChargeBy_code]
					}]
//				}]
//			}]
		}],
		// buttons : [save,cancel]
		tbar : [btnAdd, '-', save, '-', deleteButton, '-', reportBtn]
	});

	return {
		panel : blockForm,
		updateForm : function(record) {
			blockForm.getForm().reset();
			checkId = record.data.checkId;
			con_no.setValue(record.data.contractName);
			con_name.setValue(record.data.conId);
			report_code.setValue(record.data.reportCode);
			chargeBy.setValue(record.data.chargeBy);
			start_date.setValue(record.data.startDate.substring(0, 10));
			end_date.setValue(record.data.endDate.substring(0, 10));
			cliendBox.setValue(record.data.contractorName);
			contractorId.setValue(record.data.contractorId);
			deptChargeBy_code.setValue(record.data.deptChargeBy);
			deptChargeBy.setValue(record.data.deptChargeByName);
			entry_by.setValue(record.data.entryBy);
			entry_date.setValue(record.data.entryDate.substring(0, 10));
			checkAppraise.setValue(record.data.checkAppraise);
			prjId.setValue(record.data.prjId);
			prjReg.setValue(record.data.prjName);
			// blockForm.setTitle('开工报告书修改');
		},
		resetform : function() {
			resetForm();
		}
	}

};