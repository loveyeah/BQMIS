var addStartReport = function() {

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

	// 从session取登录人编码姓名部门相关信息
	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
			success : function(action) {
				var result = eval("(" + action.responseText + ")");
				if (result.workerCode) {
					// 设定默认工号，赋给全局变量
					entry_by.setValue(result.workerName);
					entry_by_hidden.setValue(result.workerCode);
				}
			}
		});
	}
	getWorkCode();

	function setProNo() {
		Ext.Ajax.request({
			url : 'manageproject/getPrjNo.action',
			method : 'post',
			params : {
				type : 2
			},
			success : function(result, request) {
				var json = result.responseText;
				var o = eval("(" + json + ")");
				report_code.setValue(o.prjNo)
			}
		});
	}

	setProNo();

	var prjRegister = new maint.prjRegList();

	// 立项名称
	var prjReg = new Ext.form.ComboBox({
		id : 'prjReg',
		fieldLabel : '立项名称',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		anchor : "93%",
		editable : false,
		triggerAction : 'all',
		onTriggerClick : function() {
			prjRegister.win.show();
		}
	});

	var prjId = new Ext.form.Hidden({
		name : 'model.prjId'
	})
	prjRegister.win.on('hide', function() {
		var value = prjRegister.returnValue;
		prjReg.setValue(value.prjName);
		prjId.setValue(value.prjId);
	});

	function resetForm() {
		blockForm.getForm().reset();
		getWorkCode();
		setProNo();
	}
	// 合同编号
	var con_no = new Ext.form.ComboBox({
		fieldLabel : '工程合同编号',
		id : 'con_no',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		hiddenName : 'model.conttreesNo',
		editable : true,
		anchor : "85%",
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../prjContractQuery/prjContract.jsp";
			var emp = window
					.showModalDialog(
							url,
							'dialogWidth:750px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');

			if (typeof(emp) != "undefined") {
				con_no.setValue(emp.conttrees_no);
				con_name.setValue(emp.contract_name);
				Ext.Ajax.request({
					method : 'POST',
					url : 'manageproject/getEndPrjByCon.action',
					params : {
						con_no : emp.conttrees_no
					},
					success : function(resp) {
						var result = eval("(" + resp.responseText + ")");
						if (result != null) {
							report_code.setValue(result.reportCode);
							prj_funds.setValue(result.prjFunds);
							prj_location.setValue(result.prjLocation);
							start_date.setValue(result.startDate.substring(0,
									10));
							end_date.setValue(result.endDate.substring(0, 10));
							var node = new Ext.tree.AsyncTreeNode();
							node.id = result.prjTypeId;
							Ext.Ajax.request({
								method : 'post',
								url : 'manageproject/findTypeById.action',
								params : {
									node : result.prjTypeId
								},
								success : function(response) {
									var rs = eval("(" + response.responseText
											+ ")");
									node.text = rs.prjType;
									prj_type_id.setValue(node);
								},
								failure : function(response) {
									Ext.Msg.alert('提示', '出现了未知错误！');
								}
							});

						} else {
							resetForm();
							con_no.setValue(emp.conttrees_no);
							con_name.setValue(emp.contract_name);
						}

					},
					failure : function(resp) {
						Ext.Msg.alert('警告', '出现未知错误！');
					}
				});
			}
		}
	});
	con_no.on('blur', function() {
		con_no.setValue(con_no.getValue());

	})
	// 工程合同名称
	var con_name = new Ext.form.TextField({
		fieldLabel : "合同名称",
		anchor : "85%",
		inputType : 'text',
		name : 'model.contractName'
	})

	// 编号
	var report_code = new Ext.form.TextField({
		fieldLabel : "编号",
		anchor : "85%",
		inputType : 'text',
		readOnly : true,
		name : 'model.reportCode'
	})
	// 投资
	var prj_funds = new Ext.form.NumberField({
		fieldLabel : "投资",
		anchor : "85%",
		inputType : 'text',
		name : 'model.prjFunds'
	})

	// 开工日期
	var start_date = new Ext.form.TextField({
		id : 'startDate',
		fieldLabel : '开工时间',
		anchor : "85%",
		style : 'cursor:pointer',
		name : 'model.startDate',
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
		fieldLabel : '竣工日期',
		anchor : "85%",
		style : 'cursor:pointer',
		name : 'model.endDate',
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
	// 工程地点
	var prj_location = new Ext.form.TextField({
		fieldLabel : "工程地点",
		anchor : "85%",
		inputType : 'text',
		name : 'model.prjLocation'
	})
	// 工程类别
	//modify by ypan 20100915
	var prj_type_id = new Ext.ux.ComboBoxTree({
		fieldLabel : "工程类别",
		anchor : "85%",
		allowBlank : false,
		displayField : 'text',
		valueField : 'id',
		hiddenName : 'model.prjTypeId',
		readOnly : true,
		tree : {
			xtype : 'treepanel',
			// 虚拟节点,不能显示
			rootVisible : false,
			loader : new Ext.tree.TreeLoader({
				dataUrl : 'manageproject/findByPId.action'
			}),
			root : new Ext.tree.AsyncTreeNode({
				id : '0',
				name : '灞桥电厂',
				text : '灞桥电厂'
			}),
			listeners : {
				click : function(node, rec) {
					typeId = node.id;
					typeName = node.text
				}
			}
		},
		selectNodeModel : 'all',
		listeners : {
			select : function(newNode, oldNode) {
				this.setValue(node.id);

			}
		}

	})
	// 填写人
	var entry_by = new Ext.form.TextField({
		fieldLabel : "填写人",
		anchor : "85%",
		inputType : 'text',
		name : 'entryby_name',
		readOnly : true
	})
	var entry_by_hidden = new Ext.form.TextField({
		inputType : 'text',
		name : 'model.entryBy'

	})
	// 填写时间
	var entry_date = new Ext.form.TextField({
		fieldLabel : "填写时间",
		inputType : 'text',
		anchor : "85%",
		name : 'model.entryDate',
		value : getDate(),
		readOnly : true
	})
	var report_id = new Ext.form.TextField({
		inputType : 'text',
		name : 'model.reportId',
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
		if (report_id.getValue() == '') {
			if (prj_type_id.getValue() == '') {
				warning += '工程类别、';
			}
		}
		if (entry_by.getValue() == '') {
			warning += '填写人、';
		}
		if (entry_date.getValue() == '') {
			warning += '填写时间、';
		}
		if (prjReg.getValue() == '') {
			warning += '立项名称、';
		}
		if (warning != '') {
			Ext.Msg.alert('警告', warning.substring(0, warning.length - 1)
					+ '不能为空！');
			return false;
		}

		if (start_date.getValue() > end_date.getValue()) {
			Ext.Msg.alert('警告', '开工时间不能大于竣工时间！');
			return false;
		}
		return true;
	}

	// 保存按钮
	var save = new Ext.Button({
		text : '保存',
		iconCls : 'save',
		handler : function() {
			if (checkInput()) {
				blockForm.getForm().submit({
					method : 'POST',
					params : {
						type : '2'
					},
					url : 'manageproject/addPrjReport.action',
					success : function(form, action) {
						var o = eval("(" + action.response.responseText + ")");
						Ext.Msg.alert("注意", o.status);
						report_id.setValue(o.reportId);
					},
					faliue : function() {
						Ext.Msg.alert('错误', o.status);
					}
				});
			}
		}
	});
	// 取消按钮
	var cancel = new Ext.Button({
		text : '取消',
		iconCls : 'reflesh',
		handler : function() {
			resetForm();
		}
	});

	var panel1 = new Ext.Panel({
		border : false,
		tbar : [new Ext.Button({
			text : '新增',
			iconCls : 'add',
			handler : function() {
				resetForm();
				report_id.setValue('');
			}
		}), '-', save, '-', cancel],
		items : [{
			layout : "column",
			border : false,
			items : [{
				columnWidth : 1,
				layout : "form",
				border : false,
				items : [{
					layout : "column",
					border : false,
					items : [{
						columnWidth : 1,
						layout : "form",
						border : false,
						items : [prjReg]
					}]
				}, {
					layout : "column",
					border : false,
					items : [{
						columnWidth : 0.5,
						layout : "form",
						border : false,
						items : [con_no]
					}, {
						columnWidth : 0.5,
						layout : "form",
						border : false,
						items : [con_name]
					}]
				}, {
					layout : "column",
					border : false,
					items : [{
						columnWidth : 0.5,
						layout : "form",
						border : false,
						items : [report_code]
					}, {
						columnWidth : 0.5,
						layout : "form",
						border : false,
						items : [prj_funds]
					}]
				}, {
					layout : "column",
					border : false,
					items : [{
						columnWidth : 0.5,
						layout : "form",
						border : false,
						items : [start_date]
					}, {
						columnWidth : 0.5,
						layout : "form",
						border : false,
						items : [end_date]
					}]
				}, {
					layout : "column",
					border : false,
					items : [{
						columnWidth : 0.5,
						layout : "form",
						border : false,
						items : [prj_location]
					}, {
						columnWidth : 0.5,
						layout : "form",
						border : false,
						items : [prj_type_id]
					}]
				}, {
					layout : "column",
					border : false,
					items : [{
						columnWidth : 0,
						layout : "form",
						hidden : true,
						border : false,
						items : [entry_by_hidden]
					}, {
						columnWidth : 0.5,
						layout : "form",
						border : false,
						items : [entry_by]
					}, {
						columnWidth : 0,
						layout : "form",
						hidden : true,
						border : false,
						items : [prjId]
					}, {
						columnWidth : 0.5,
						layout : "form",
						border : false,
						items : [entry_date]
					}]
				}, {
					layout : "column",
					border : false,
					items : [{
						columnWidth : 0.5,
						layout : "form",
						border : false,
						hidden : true,
						items : [report_id]
					}, {
						columnWidth : 0.5,
						layout : "form",
						border : false,
						items : [{}]
					}]
				}]
			}]
		}]
	});

	// 表单对象
	var blockForm = new Ext.form.FormPanel({
		labelAlign : 'right',
		frame : true,
		labelWidth : 80,
		layout : 'column',
		closeAction : 'hide',
		fileUpload : true,
		items : [{
			xtype : 'fieldset',
			buttonAlign : 'center',
			id : 'formSet',
			title : '竣工报告书填写',
			labelAlign : 'right',
			labelWidth : 100,
			defaultType : 'textfield',
			autoHeight : true,
			border : true,
			items : [panel1]
		}]

	});
	return {
		panel : blockForm,
		updateForm : function(record) {
			blockForm.getForm().reset();
			report_id.setValue(record.data.reportId);
			con_no.setValue(record.data.conttreesNo);
			con_name.setValue(record.data.contractName);
			report_code.setValue(record.data.reportCode);
			prj_funds.setValue(record.data.prjFunds);
			start_date.setValue(record.data.startDate.substring(0, 10));
			end_date.setValue(record.data.endDate.substring(0, 10));
			prj_location.setValue(record.data.prjLocation);
			var curentNode = new Ext.tree.AsyncTreeNode();
			curentNode.id = "";
			curentNode.text = record.data.prjTypeId;
			prj_type_id.setValue(curentNode);
			entry_by.setValue(record.data.entryName);
			entry_date.setValue(record.data.entryDate.substring(0, 10));
			entry_by_hidden.setValue(record.data.entryBy);
			prjReg.setValue(record.data.prjName);
			prjId.setValue(record.data.prjId);
			blockForm.setTitle('竣工报告书修改');
		},
		resetform : function() {
			resetForm();
		}
	}

};