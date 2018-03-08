Ext.ns("trainMaint.trainRegister");
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
trainMaint.trainRegister = function() {
	var applyIds;
	var entryId;
	var backEntryBy;
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

//项目名称
var con_no = new Ext.form.ComboBox({
	fieldLabel:'项目名称',
	anchor : "80%",
	store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
	}),
	mode : 'local',
	hiddenName : 'check.contractName',
	editable : true,
	allowBlank : false,
	width:'80%',
	triggerAction : 'all',
		onTriggerClick : function() {
				var url = "../../prjContractQuery/prjContract.jsp";
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
	
});
// 承包单位名称
	var cliendBox = new Ext.form.ComboBox({
		name : 'check.contractorName',
		id : 'cliendId',
		fieldLabel : '承包单位名称',
		mode : 'remote',
		editable : false,
		anchor : "80%",
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
			}
		}
	});
	// 工程开始日期
	var startDate = new Ext.form.TextField({
				fieldLabel : "工程开始日期",
				name : 'check.startDate',
				style : 'cursor:pointer',
				forceSelection : true,
				selectOnFocus : true,
				allowBlank : false,
				anchor : "80%",
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
				name : 'check.endDate',
				style : 'cursor:pointer',
				anchor : "80%",
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
//发包部门负责人选择
var chargeByName = new Ext.form.ComboBox({
	name : 'chargeByName',
	id : 'chargeByName',
	fieldLabel : "发包部门负责人",
	store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
	}),
	mode : 'remote',
	hiddenName:'deptChargeBy',
	editable : false,
	anchor : "80%",
	triggerAction : 'all',
	onTriggerClick : function() {
			var url = "../../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
			var args = {
					selectcheck : 'single',
					//notIn : "'999999'",
					rootNode : {
					id : '0',
					text : '灞桥电厂'
					}
	}
	var emp = window
		.showModalDialog(
		url,
		args,
		'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');

	if (typeof(emp) != "undefined") {
		chargeByName.setValue(emp.workerName);
		workerCode.setValue(emp.workerCode);
	}
	}
});
// 填写人
	var entryBy = new Ext.form.TextField({
				inputType:'text',
				name : 'check.entryBy',
				fieldLabel : '填写人',
				allowBlank : false,
				anchor : '80%'
	});
	// 编号
	var reportCode = new Ext.form.TextField({
				inputType:'text',
				name : 'check.reportCode',
				fieldLabel : '编号',
				allowBlank : false,
				anchor : '80%'
	});
	// 承包方负责人
	var chargeBy = new Ext.form.TextField({
				inputType:'text',
				name : 'check.chargeBy',
				fieldLabel : '承包方负责人',
				allowBlank : false,
				anchor : '80%'
	});
	// 发包部门工程方负责人验收评价
	var checkAppraise = new Ext.form.TextField({
				inputType:'text',
				name : 'check.checkAppraise',
				fieldLabel : '发包部门工程方负责人验收评价',
//				allowBlank : false,
				anchor : '80%'
	});
	// 填写时间
	var entryDate = new Ext.form.TextField({
				inputType:'text',
				name : 'entryDate',
				fieldLabel : '填写时间',
				allowBlank : false,
				anchor : '80%'
	});
	// 验收单Id
	var checkId = new Ext.form.Hidden({
				id : 'checkId',
				name : 'check.checkId'
			});
	// 合同Id
	var cconId = new Ext.form.Hidden({
				id : 'cconId',
				name : 'check.conId'
			});

	//承包单位Id
	var contractorId = new Ext.form.Hidden({
				id : 'contractorId',
				name : 'check.contractorId'
			});
	// 发包方负责人Code
	var workerCode = new Ext.form.Hidden({
				id : 'workerCode',
				name : 'check.deptChargeBy'
			});
	// 填写人Code
	var entryByCode = new Ext.form.Hidden({
				id : 'entryByCode',
				name : 'check.chargeBy'
			});
//保存	
function save(){
		if(applyIds==null||applyIds==""){
			Ext.Msg.alert("提示", '请选择一条验收单信息！！');
			return false;
		};
		if (!textForm.getForm().isValid()) {
			return false;
		};
		Ext.Msg.wait("正在保存,请等待...");
		textForm.getForm().submit({
			method : 'POST',
			params : {
							chargeBy : chargeBy.getValue(),
							contractName : con_no.getValue()
//							startDate : startDate.getValue(),
//							endDate : endDate.getValue(),
//							contractorName:contractorNameValue,
//							chargeBy : chargeBy.getValue()
						},
			url : 'manageproject/projectEndCheckSave.action',
			success : function(form, action) {
				var o = eval("(" + action.response.responseText + ")");
				Ext.Msg.alert("提示", '操作成功！');
				//isChange=0;
			},
			faliue : function() {
				Ext.Msg.alert('错误', '出现未知错误.');
			}
		});
					
};
//签字
function sign() {
			if(applyIds==null||applyIds==""){
				Ext.Msg.alert("提示", '请选择一条验收单信息！！');
				return false;
			};
//			alert(isChange);
//			if (isChange>0) {
//				Ext.Msg.alert("提示", '信息已修改，请先保存！！');
//				return false;
//			};
			if (entryId == null || entryId == "" || entryId == undefined) {
				Ext.Msg.alert('提示', '流程还未启动');
			} else if (status == "4") {
				Ext.Msg.alert('提示', '审批流程已结束');
			} else {
					var args=new Object();
					args.entryId=entryId;
					args.id=applyIds;
				  var o = window.showModalDialog('sign.jsp',
                args, 'dialogWidth=700px;dialogHeight=450px;status=no;');
				if (o) {
//					textForm.getForm().reset();
					applyIds = "";
//					entryId = "";
//					backEntryBy="";
					Ext.getCmp("maintab").setActiveTab(0);
					//Ext.getCmp("grid_div").getStore().reload();
				}
			}
		}
//会签查询
 function CQuery(){  
 		if(applyIds==null||applyIds==""){
			Ext.Msg.alert("提示", '请选择一条验收单信息！！');
			return false;
		};
 	var url="";
        	if(entryId==null||entryId=="")
        	{
			 url = "/power/workflow/manager/flowshow/flowshow.jsp?flowCode='prjEndChek'";
        	}
        	else
        	{
        		url = "/power/workflow/manager/show/show.jsp?entryId="+entryId;
        	}
        	window.open( url);
		}
	// 会签表浏览
	function CheckRptPreview() {
		if (applyIds == "" || applyIds == null) {
			Ext.Msg.alert('提示', '请在审批列表中选择待签字的验收单！');
			return false;
		}
		var url = "/powerrpt/report/webfile/prjCheck.jsp?checkId="+ applyIds;
		if (backEntryBy!=null&&backEntryBy!="") {
			url+='&backEntryBy='+backEntryBy;
		}
		window.open(url);

	};
// 回录相关部门签字信息
	function backEntryM() {
		if (applyIds == "" || applyIds == null) {
			Ext.Msg.alert('提示', '请在审批列表中选择待签字的验收单！');
			return false;
		}
		var args=new Object();
					args.checkId=applyIds;
				  var o = window.showModalDialog('prjBackEntry.jsp',
                args, 'dialogWidth=700px;dialogHeight=340px;status=no;');
	};
// 保存按钮
var saveB=new Ext.Button({
			id : 'btnSave',
			text : "保存",
			iconCls : 'save',
			handler : save
	});
// 签字按钮
var signB=new Ext.Button({
			text : "签字",
			iconCls : 'write',
			handler : sign
	});
// 会签表按钮
var CtableB=new Ext.Button({
			text : "会签表",
			iconCls : 'pdfview',
			handler : CheckRptPreview
	});
// 会签查询按钮
var CQueryB=new Ext.Button({
			text : "会签查询",
			iconCls : 'view',
			handler : CQuery
	});
// 回录相关部门签字信息按钮
var BackEntryB=new Ext.Button({
			text : "回录相关部门签字信息",
			iconCls : 'view',
			handler : backEntryM
	});
//顶部工具栏
var toolbar = new Ext.Toolbar({
items : [saveB, '-',signB, '-',CtableB, '-',CQueryB, '-',BackEntryB]

});
	var receiveContent = new Ext.form.FieldSet({
		height : '80%',
		layout : 'form',
		items : [{
			border : false,
			layout : 'column',
			items : [{
				columnWidth : .5,
				layout : 'form',
				border : false,
				labelWidth :150,
				items : [con_no, startDate,cliendBox
				,chargeByName,entryBy]
			}, {
				columnWidth : .5,
				layout : 'form',
				labelWidth :190,
				border : false,
				items : [reportCode,endDate,chargeBy
				,checkAppraise,entryDate]
			},checkId,cconId,contractorId,workerCode,entryByCode
			]
		}]
	});
	function edit(){
			var args = new Object();
			args.checkId = checkId;
			args.workflowType = "prjEndChek";
			args.entryId = workflowNo;
			args.title = "";
			var result = window
					.showModalDialog(
							'approveSign.jsp',
							args,'dialogWidth=700px;dialogHeight=500px;center=yes;help=no;resizable=no;status=no;');

	}
	var textForm = new Ext.form.FormPanel({
		bodyStyle : "padding:5px 5px 0",
		labelAlign : 'right',
		labelWidth : 65,
		autoHeight : true,
		region : 'center',
		border : false,
		items : [receiveContent]
	});
	function resetFromRec() {
		textForm.getForm().reset();
	}
	var detailsPanel = new Ext.Panel({
				layout : 'border',
				border : false,
				autoScroll : true,
				tbar: toolbar,
				items : [ {
							layout:'fit',
							autoScroll : true,
							region : 'center',
							items : [textForm]
						}]
			});
	return {
		panel : detailsPanel,
		setTrainId : function(record) {
			textForm.getForm().reset();
			if(record!=null){
			checkId.setValue(record.data.checkId);
			con_no.setValue(record.data.contractName);
			cconId.setValue(record.data.conId);
			startDate.setValue(record.data.startDate.substring(0,10));
			endDate.setValue(record.data.endDate.substring(0,10));
			cliendBox.setValue(record.data.contractorName);
			contractorId.setValue(record.data.contractorId);
			chargeByName.setValue(record.data.deptChargeBy);
			chargeBy.setValue(record.data.chargeBy);
			entryBy.setValue(record.data.entryBy);
			entryDate.setValue(record.data.entryDate.substring(0,10));
			checkAppraise.setValue(record.data.checkAppraise);
			reportCode.setValue(record.data.reportCode);
			applyIds=record.data.checkId;
			entryId=record.data.workFlowNo;
			backEntryBy=record.data.backEntryBy;
			textForm.setTitle('工程竣工验收单审批');
			entryBy.setDisabled(true);
			entryDate.setDisabled(true);
			reportCode.setDisabled(true);
			}
	}
		,resetInputField : resetFromRec
	}

};