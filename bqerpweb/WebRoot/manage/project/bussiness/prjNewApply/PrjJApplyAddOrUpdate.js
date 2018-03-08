Ext.ns("trainMaint.trainRegister");
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
trainMaint.trainRegister = function() {
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
	var sessWorname;
	var sessWorcode;
	//获取当前员工编号
	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						if (result.workerCode) {
							// 设定默认工号
							sessWorcode = result.workerCode;
							sessWorname = result.workerName;
							sessDeptCode = result.deptCode;
							deptId = result.deptId;
							sessDeptName = result.deptName;
							loadData();
						}
					}
				});
	}

	function loadData() {
		entryDate.setValue(getDate());
		entryByCode.setValue(sessWorcode);		
		entryBy.setValue(sessWorname);
		testResult.setValue(0);
		authorizeItem.setValue(0);
		personRegister.setValue(0);
		articleRegister.setValue(0);
		idCard.setValue(0);
		operateCard.setValue(0);
		cautionMoney.setValue(0);
		handInCard.setValue(0);
		startDate.setValue(getDate());
		entryBy.setDisabled(true);
		entryDate.setDisabled(true);
		
	};
	// 承包负责人
	var chargeBy = new Ext.form.TextField({
				id : 'chargeBy',
				 name : 'pja.chargeBy',
				xtype : 'textfield',
				fieldLabel : '承包负责人',
				allowBlank : false,
				anchor : '85%'
	});

//项目名称
var con_no = new Ext.form.ComboBox({
	fieldLabel:'项目名称',
	anchor : "91%",
	store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
	}),
	mode : 'local',
	hiddenName : 'pja.contractName',
	editable : true,
	allowBlank : false,
	width:'75%',
	triggerAction : 'all',
		onTriggerClick : function() {
				var url = "../prjContractQuery/prjContract.jsp";
		var emp = window
		.showModalDialog(
			url,
			'dialogWidth:750px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');

		if (typeof(emp) != "undefined") {
			con_no.setValue(emp.contract_name);
			cconId.setValue(emp.con_id);
			cliendBox.setValue(emp.client_name);
			contractorId.setValue(emp.client_id);
		}
	}
});
con_no.on('blur',function(){
	con_no.setValue(con_no.getValue());
	
})


	//承包单位安规考试成绩单及试卷
	var testResult = new Ext.form.ComboBox({
				fieldLabel : '承包单位安规考试成绩单及试卷',
				anchor : "100%",
				store : new Ext.data.SimpleStore({
							fields : ['value', 'text'],
							data : [['2', '合格'], ['1', '不合格'], ['0', '无']]
						}),
				id : 'testResult',
//				name : 'testResult',
				valueField : "value",
				displayField : "text",
				value : '0',
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'pja.testResult',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : false
			});
	//发包部门负责人批准签字的安全,技术,组织措施
	var authorizeItem = new Ext.form.ComboBox({
				fieldLabel : '发包部门负责人批准签字的安全,技术,组织措施',
				anchor : "85%",
				store : new Ext.data.SimpleStore({
							fields : ['value', 'text'],
							data : [['2', '批准'], ['1', '未批准'], ['0', '无']]
						}),
				id : 'authorizeItem',
				name : 'authorizeItem',
				valueField : "value",
				displayField : "text",
				value : '0',
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'pja.authorizeItem',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : false
			});
	//施工人员情况登记表
	var personRegister = new Ext.form.ComboBox({
				fieldLabel : '施工人员情况登记表',
				anchor : "100%",
				store : new Ext.data.SimpleStore({
							fields : ['value', 'text'],
							data : [['2', '登记'], ['1', '未登记'], ['0', '无']]
						}),
				id : 'personRegister',
				name : 'personRegister',
				valueField : "value",
				displayField : "text",
				value : '0',
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'pja.personRegister',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : false
			});
	//施工机械、工具及安全防护用品登记表
	var articleRegister = new Ext.form.ComboBox({
				fieldLabel : '施工机械、工具及安全防护用品登记表',
				anchor : "85%",
				store : new Ext.data.SimpleStore({
							fields : ['value', 'text'],
							data : [['2', '合格'], ['1', '不合格'], ['0', '无']]
						}),
				id : 'articleRegister',
				name : 'articleRegister',
				valueField : "value",
				displayField : "text",
				value : '0',
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'pja.articleRegister',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : false
			});
	//施工人员身份证复印件
	var idCard = new Ext.form.ComboBox({
				fieldLabel : '施工人员身份证复印件',
				anchor : "100%",
				store : new Ext.data.SimpleStore({
							fields : ['value', 'text'],
							data : [['2', '合格'], ['1', '不合格'], ['0', '无']]
						}),
				id : 'idCard',
				name : 'idCard',
				valueField : "value",
				displayField : "text",
				value : '0',
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'pja.idCard',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : false
			});
	//特种作业人员操作证件复印件
	var operateCard = new Ext.form.ComboBox({
				fieldLabel : '特种作业人员操作证件复印件',
				anchor : "85%",
				store : new Ext.data.SimpleStore({
							fields : ['value', 'text'],
							data : [['2', '合格'], ['1', '不合格'], ['0', '无']]
						}),
				id : 'operateCard',
				name : 'operateCard',
				valueField : "value",
				displayField : "text",
				value : '0',
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'pja.operateCard',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : false
			});
	//安全施工保证金（工程总格5%）
	var cautionMoney = new Ext.form.ComboBox({
				fieldLabel : '安全施工保证金（工程总额5%）',
				anchor : "100%",
				store : new Ext.data.SimpleStore({
							fields : ['value', 'text'],
							data : [['2', '已缴纳'], ['1', '未缴纳'], ['0', '无']]
						}),
				id : 'cautionMoney',
				name : 'cautionMoney',
				valueField : "value",
				displayField : "text",
				value : '0',
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'pja.cautionMoney',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : false
			});
	//承包方负责人向施工人员的安全技术措施交底卡
	var handInCard = new Ext.form.ComboBox({
				fieldLabel : '承包方负责人向施工人员的安全技术措施交底卡',
				anchor : "85%",
				store : new Ext.data.SimpleStore({
							fields : ['value', 'text'],
							data : [['2', '已交底'], ['1', '未交底'], ['0', '无']]
						}),
				id : 'handInCard',
				name : 'handInCard',
				valueField : "value",
				displayField : "text",
				value : '0',
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'pja.handInCard',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : false
			});
	// 承包单位名称
	var cliendBox = new Ext.form.ComboBox({
		name : 'pja.contractorName',
		id : 'cliendId',
		fieldLabel : '承包单位名称',
		mode : 'remote',
		editable : true,
		anchor : "100%",
		allowBlank : false,
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../../comm/jsp/supplierQuery/supplierQuery.jsp";
			var client = window
					.showModalDialog(
							url,
							null,
							'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');

			if (typeof(client) != "undefined") {
				cliendBox.setValue(client.supplyName);
				contractorId.setValue(client.supplierId);
			}
		}
	});
	
	cliendBox.on('blur',function(){
		cliendBox.setValue(cliendBox.getValue());
		
	});
	// 工程开始日期
	var startDate = new Ext.form.TextField({
				fieldLabel : "工程开始日期",
				name : 'pja.startDate',
				style : 'cursor:pointer',
				forceSelection : true,
				selectOnFocus : true,
				allowBlank : false,
				anchor : "100%",
				value : getDate(),
				readOnly : true,
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
	// 工程结束日期
	var endDate = new Ext.form.TextField({
				id : 'endDate',
				fieldLabel : "工程结束日期",
				name : 'pja.endDate',
				style : 'cursor:pointer',
				anchor : "85%",
				forceSelection : true,
				selectOnFocus : true,
				allowBlank : false,
				readOnly : true,
				listeners : {
					focus : function() {
						var pkr = WdatePicker({
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd',
									alwaysUseStartDate : true,
									onpicked : function() {
										if (startDate.getValue() == ""
												|| startDate.getValue() > endDate
														.getValue()) {
											Ext.Msg.alert("提示", "必须大于工程开始日期");
											endDate.setValue("")
											return;
										}
										endDate.clearInvalid();
									},
									onclearing : function() {
										endDate.markInvalid();
									}
								});
					}
				}
			});
	// 填写人
	var entryBy = new Ext.form.TextField({
				 inputType:'text',
				fieldLabel : '填写人',
				allowBlank : false,
				anchor : '100%'
	});
	// 填写时间
	var entryDate = new Ext.form.TextField({
				id : 'entryDate',
				fieldLabel : "填写时间",
				xtype : 'textfield',
				name : 'modifDate',
				style : 'cursor:pointer',
				anchor : "85%"
			});
	// 申请单Id
	var applyId = new Ext.form.Hidden({
				id : 'applyId',
				name : 'pja.applyId'
			});
	// 合同Id
	var cconId = new Ext.form.Hidden({
				id : 'cconId',
				name : 'pja.conId'
			});

	//承包单位Id
	var contractorId = new Ext.form.Hidden({
				id : 'contractorId',
				name : 'pja.contractorId'
			});
	// 工作流序号
	var workFlowNo = new Ext.form.Hidden({
				id : 'workFlowNo',
				name : 'pja.workFlowNo'
			});
	// 状态
	var statusId = new Ext.form.Hidden({
				id : 'statusId',
				name : 'pja.statusId'
			});
	// 填写人Code
	var entryByCode = new Ext.form.Hidden({
				id : 'entryByCode',
				name : 'pja.entryBy'
			});
	var prjRegister = new maint.prjRegList();
	
	//add by qxjiao 20100908
	
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
	name:'pja.prjId'
})

prjRegister.win.on('hide',function(){
	var value = prjRegister.returnValue;
	prjReg.setValue(value.prjName);
	prjId.setValue(value.prjId);
});


//保存	
function save(){
		if (!textForm.getForm().isValid()) {
			return false;
		}
		if (startDate.getValue() == ""|| endDate.getValue() == ""||startDate.getValue() > endDate.getValue()) {
			Ext.Msg.alert("提示", "工程结束日期必须大于工程开始日期");
			return;
		};
		if(prjId.getValue()==''||prjReg.getValue()==''){
			Ext.Msg.alert('警告','立项名称不能为空！');
			return ;
		}
		Ext.Msg.wait("正在保存,请等待...");
		textForm.getForm().submit({
			method : 'POST',
			params : {
							modifDate : entryDate.getValue()
						},
			url : 'manageproject/savePrjApply.action',
			success : function(form, action) {
				var o = eval("(" + action.response.responseText + ")");
				applyId.setValue(o.applyId);
				statusId.setValue(o.statusId);
				Ext.Msg.alert("提示", '操作成功！');
			},
			faliue : function() {
				Ext.Msg.alert('错误', '出现未知错误.');
			}
		});
					
};
// 取消
function channel(){
resetFromRec();
};
// 保存按钮
var saveB=new Ext.Button({
			id : 'btnSave',
			text : "保存",
			iconCls : 'save',
			handler : save
	});
// 取消按钮
var channelB=new Ext.Button({
			id : 'btnChannel',
			text : "取消",
			iconCls : 'reflesh',
			handler : channel
	});

var toolbar = new Ext.Toolbar({
items : [saveB,channelB]

});

	var textForm = new Ext.form.FormPanel({
		bodyStyle : "padding:5px 5px 0",
		labelAlign : 'right',
		width : Ext.get('div_lay').getWidth(),
		autoHeight : true,
		fileUpload : true,
		region : 'center',
		border : false,
		//bbar : toolbar,
		items : [new Ext.form.FieldSet({
			title : '开工申请单信息',
			collapsible : true,
			height : '100%',
			layout : 'form',
			items : [{
				border : false,
				layout : 'column',
				items : [
				{
					columnWidth : 1,
					layout : 'form',
					border : false,
					labelWidth : 200,
					items : [con_no]
				},{
					columnWidth : 1,
					layout : 'form',
					border : false,
					labelWidth : 200,
					items : [prjReg]
				}, {
					columnWidth : 1,
					layout : 'form',
					hidden:true,
					border : false,
					labelWidth : 200,
					items : [prjId]
				}, {
					columnWidth : .4,
					layout : 'form',
					border : false,
					labelWidth : 200,
					items : [startDate]
				}, {
					columnWidth : .6,
					layout : 'form',
					labelWidth : 270,
					border : false,
					items : [endDate]
				}, {
					border : false,
					layout : 'form',
					columnWidth : .4,
					labelWidth : 200,
					items : [cliendBox]
				}, {
					columnWidth : .6,
					layout : 'form',
					labelWidth : 270,
					border : false,
					items : [chargeBy]
				}, {
					columnWidth : .4,
					layout : 'form',
					labelWidth : 200,
					border : false,
					items : [testResult]
				}, {
					columnWidth : .6,
					layout : 'form',
					labelWidth : 270,
					border : false,
					items : [authorizeItem]
				}, {
					columnWidth : .4,
					layout : 'form',
					labelWidth : 200,
					border : false,
					items : [personRegister]
				}, {
					columnWidth : .6,
					layout : 'form',
					labelWidth : 270,
					border : false,
					items : [articleRegister]
				}, {
					columnWidth : .4,
					layout : 'form',
					labelWidth : 200,
					border : false,
					items : [idCard]
				}, {
					columnWidth : .6,
					layout : 'form',
					labelWidth : 270,
					border : false,
					items : [operateCard]
				}, {
					columnWidth : .4,
					layout : 'form',
					labelWidth : 200,
					border : false,
					items : [cautionMoney]
				}, {
					columnWidth : .6,
					layout : 'form',
					labelWidth : 270,
					border : false,
					items : [handInCard]
				}, {
					columnWidth : .4,
					layout : 'form',
					labelWidth : 200,
					border : false,
					items : [entryBy]
				}, {
					columnWidth : .6,
					layout : 'form',
					labelWidth : 270,
					border : false,
					items : [entryDate]
				},applyId,contractorId,workFlowNo,statusId,cconId,entryByCode
				]
			}]
		})],
		buttons : [saveB,channelB]
	});
	function resetFromRec() {
		textForm.getForm().reset();
		getWorkCode();
	}
	
	var detailsPanel = new Ext.Panel({
				layout : 'border',
				border : false,
				autoScroll : true,
				items : [ {
							layout:'fit',
							autoScroll : true,
							region : 'center',
							items : [textForm]
						}]
			});
			
				//getWorkCode();
	return {
		panel : detailsPanel,
		setTrainId : function(record) {
			textForm.getForm().reset();
			if(record!=null){
			applyId.setValue(record.data.applyId);
			con_no.setValue(record.data.contractName);
			cconId.setValue(record.data.conId);
			startDate.setValue(record.data.startDate.substring(0,10));
			endDate.setValue(record.data.endDate.substring(0,10));
			cliendBox.setValue(record.data.contractorName);
			contractorId.setValue(record.data.contractorId);
			chargeBy.setValue(record.data.chargeBy);
			testResult.setValue(record.data.testResult);
			authorizeItem.setValue(record.data.authorizeItem);
			personRegister.setValue(record.data.personRegister);
			articleRegister.setValue(record.data.articleRegister);
			idCard.setValue(record.data.idCard);
			operateCard.setValue(record.data.operateCard);
			cautionMoney.setValue(record.data.cautionMoney);
			handInCard.setValue(record.data.handInCadr);
			entryByCode.setValue(record.data.entryByCode)
			entryBy.setValue(record.data.enterBy);
			entryDate.setValue(record.data.EntryDate.substring(0,10));
			workFlowNo.setValue(record.data.workFlowNo);
			statusId.setValue(record.data.statusId);
//			alert(testResult.getValue());
//			alert(authorizeItem.getValue());
//			alert(personRegister.getValue());
			textForm.setTitle('开工申请单修改');
			entryBy.setDisabled(true);
			entryDate.setDisabled(true);
			prjReg.setValue(record.data.prjName);
			prjId.setValue(record.data.prjId);
			}
	}
		,resetInputField : resetFromRec
	}

}