Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

var currentRecord = parent.currentRecord;

Ext.onReady(function() {

	
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
	anchor : '73%',
	editable : false,
	triggerAction : 'all',
		onTriggerClick : function() {
				prjRegister.win.show();
	}
});

var prjId = new Ext.form.Hidden({
	id:'prjId',
	name:'temp.prjId'
})

prjRegister.win.on('hide',function(){
	var value = prjRegister.returnValue;
	prjReg.setValue(value.prjName);
	prjId.setValue(value.prjId);
});

	
	var ffid="";
	if (currentRecord != null) {
		ffid = currentRecord.get("checkId"); 
	}
	
	var checkId = new Ext.form.Hidden({
		id : "checkId",
		name : 'temp.checkId',
		anchor : "40%"
	});
	var contractorId = new Ext.form.Hidden({
		id : "contractorId",
//		xtype : "hidden",
		fieldLabel : '施工单位ID',
//		value : '',
		name : 'temp.contractorId',
		anchor : "86%"
	});
	var contractorName = new Ext.form.ComboBox({
		id : "contractorName",
		xtype : "textfield",
		fieldLabel : '施工单位',
		value : '',
		name : 'temp.contractorName',
		anchor : "86%",
		editable : true,
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../../comm/jsp/supplierQuery/supplierQuery.jsp";
			var emp = window.showModalDialog(url,
			'dialogWidth:750px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');

			if (typeof(emp) != "undefined") {
				
				contractorId.setValue(emp.supplierId);
				contractorName.setValue(emp.supplyName);
			
			}
		}
	});
	var prjLocation = new Ext.form.TextField({
		fieldLabel : '工程地点',
		name : 'temp.prjLocation',
		id : 'prjLocation',
		anchor : "54%"
	});
	var reportCode = new Ext.form.TextField({
		fieldLabel : '工程编号',
		name : 'temp.reportCode',
		emptyText:'自动生成',
		id : 'reportCode',
		anchor : '86%',
		readOnly:true
	});
	
	var projectPrice = new Ext.form.NumberField({
		fieldLabel : '工程造价',
		name : 'temp.projectPrice',
		anchor : '54%',
		id : 'projectPrice'
	});
	var conId = new Ext.form.Hidden({
		id : "conId",
//		xtype : "hidden",
		fieldLabel : '工程ID',
//		value : '',
		name : 'temp.conId',
		anchor : "74%"
	});
	var conName = new Ext.form.ComboBox({
		fieldLabel : '工程名称',
		name : 'temp.conName',
		xtype : 'textfield',
		anchor : '73%',
		id : 'conName',
		editable : true,
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../prjContractQuery/prjContract.jsp";
			var emp = window.showModalDialog(url,
			'dialogWidth:750px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');

			if (typeof(emp) != "undefined") {
//				conId.setValue(emp.con_id);
//				conName.setValue(emp.contract_name);
//				reportCode.setValue();
				//conNo.setValue(emp.conttrees_no);
				contractorId.setValue(emp.client_id);
				contractorName.setValue(emp.client_name);
				conId.setValue(emp.con_id);
				conName.setValue(emp.contract_name);
				Ext.Ajax.request({
					method:'POST',
					url:'manageproject/getEndPrjByCon.action',
					params:{
						con_no:emp.conttrees_no
					},
					success:function(resp){
						var result = eval("(" + resp.responseText + ")");
						if(result!=null){
							reportCode.setValue(result.reportCode);
							projectPrice.setValue(result.prjFunds);
							prjLocation.setValue(result.prjLocation);
							startDate.setValue(result.startDate.substring(0,10));
							endDate.setValue(result.endDate.substring(0,10));
//							contractorId.setValue(result.supplierId);
//							contractorName.setValue(result.supplyName);			
						}
						
					},
					failure:function(resp){
						Ext.Msg.alert('警告','出现未知错误！');
					}
					});
			}
		}

	});
	
	var startDate = new Ext.form.DateField({
		fieldLabel : '开始日期',
		name : 'temp.startDate',
		allowBlank : false,
		anchor : '86%',
		id : 'startDate',
		format : 'Y-m-d'
	});
	var endDate = new Ext.form.DateField({
		fieldLabel : '结束日期',
		name : 'temp.endDate',
		allowBlank : false,
		anchor : '54%',
		id : 'endDate',
		format : 'Y-m-d'
	});
	var checkDate = new Ext.form.DateField({
		fieldLabel : '验收日期',
		name : 'temp.checkDate',
		allowBlank : false,
		anchor : '86%',
		id : 'checkDate',
		format : 'Y-m-d'
	});
	var quantities = new Ext.form.TextField({
		fieldLabel : '工程量',
		name : 'temp.quantities',
		anchor : '54%',
		id : 'quantities'
	});
	var entryDate = new Ext.form.DateField({
		fieldLabel : '填写时间',
		name : 'temp.entryDate',
		anchor : '86%',
		id : 'entryDate',
		readOnly: true,
		format : 'Y-m-d',
//		disable:true
		disabled : true
	});
	var entryBy = new Ext.form.TextField({
		fieldLabel : '填写人',
		name : 'temp.entryBy',
		xtype : 'textfield',
		anchor : '54%',
		id : 'entryBy',
		readOnly: true
	});
	
	
	var workApplyField = new Ext.form.FieldSet({
		border : true,
		labelWidth : 80,
		labelAlign : 'right',
		buttonAlign : 'center',
		layout : 'form',
		title : '验收证书填写',
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
						items : [checkId]
					},
					{
						columnWidth : 1,
						layout : "form",
						border : false,
						items : [contractorId]
					},{
						columnWidth : 0.4,
						layout : "form",
						border : false,
						items : [contractorName]
					},{
						columnWidth : 0.6,
						layout : "form",
						border : false,
						items : [prjLocation]
					}, {
						columnWidth : 0.4,
						layout : "form",
						border : false,
						items : [reportCode]
					}, {
						columnWidth : 0.6,
						layout : "form",
						border : false,
						items : [projectPrice]
					}, {
						columnWidth : 1,
						layout : "form",
						border : false,
						items : [conId]
					},{
						columnWidth : 1,
						layout : "form",
						border : false,
						items : [conName]
					}, {
						columnWidth : 1,
						layout : "form",
						border : false,
						items : [prjReg]
					}, {
						columnWidth : 1,
						layout : "form",
						hidden:true,
						border : false,
						items : [prjId]
					},{
						columnWidth : 0.4,
						layout : "form",
						border : false,
						items : [startDate]
					}, {
						columnWidth : 0.6,
						layout : "form",
						border : false,
						items : [endDate]
					},{
						columnWidth : 0.4,
						layout : "form",
						border : false,
						items : [checkDate]
					}, {
						columnWidth : 0.6,
						layout : "form",
						border : false,
						items : [quantities]
					}, {
						columnWidth : 0.4,
						layout : "form",
						border : false,
						items : [entryDate]
					},{
						columnWidth : 0.6,
						layout : "form",
						border : false,
						items : [entryBy]
					}],
		buttons : [{
			text : '保存',
			iconCls : 'save',
			handler : saveCheck
		}, {
			text : '清空',
			iconCls : 'cancer',
			handler : function() {
				form.getForm().reset();
				contractorName.setValue("");
				prjLocation.setValue("");
				reportCode.setValue("");
				projectPrice.setValue("");
				contractorId.setValue("");
				conId.setValue("");
				conName.setValue("");
				startDate.setValue("");
				endDate.setValue("");
				checkDate.setValue("");
				quantities.setValue("");
				entryDate.setValue("");
				entryBy.setValue("");
				ffid = "";
			}
		}]
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
	function saveCheck() {

			if(startDate.getValue()>endDate.getValue()){
					Ext.Msg.alert('警告','开工时间不能大于竣工时间！');
					return;
				}
			if(prjId.getValue()==''||prjReg.getValue()==''){
				Ext.Msg.alert('提示','立项名称不能为空！');
				return;
			}
			var url = "";
			if (ffid == null || ffid == "") {
				url = "manageproject/addPrjEndCheck.action"
			} else {
				url = "manageproject/updatePrjEndCheck.action"
			}
			
			form.getForm().submit({
				method : 'POST',
				url : url,
				success : function(form, action) {
					var o = eval("(" + action.response.responseText + ")");
					
					//----------
					//清除Field
					
						clearAllFields();
						parent.Ext.getCmp("maintab").setActiveTab(0);
						if (parent.document.all.iframe1 != null) {
							parent.document.all.iframe1.src = "manage/project/bussiness/prjEndCheck/prjEndCheckList.jsp";
						}

//					if (o.msg == "增加成功！") {
//						clearAllFields();
//						parent.Ext.getCmp("maintab").setActiveTab(0);
//						if (parent.document.all.iframe1 != null) {
//							parent.document.all.iframe1.src = "manage\project\bussiness\prjEndCheck\prjEndCheckList.jsp";
//						}		
//					} else {
//						Ext.Msg.alert("注意", o.msg);
//					}
				},
				failure : function(form, action) {
//					var o = eval("(" + action.response.responseText + ")");
					Ext.Msg.alert("注意", "填写有误，请重新填写");
				}
			});
		
	}

	// 清除所有Field
	function clearAllFields() {
		form.getForm().reset();
		contractorName.setValue("");
		prjLocation.setValue("");
		reportCode.setValue("");
		projectPrice.setValue("");
		contractorId.setValue("");
		conId.setValue("");
		conName.setValue("");
		startDate.setValue("");
		endDate.setValue("");
		checkDate.setValue("");
		quantities.setValue("");
		entryDate.setValue("");
		entryBy.setValue("");
	}
	
	
	
	if (ffid != null && ffid != "") {
		Ext.getCmp('checkId').setValue(currentRecord.get("checkId"));

		Ext.getCmp('contractorId').setValue(currentRecord.get("contractorId"));
	
		Ext.getCmp('contractorName').setValue(currentRecord.get("contractorName"));
		Ext.getCmp('conId').setValue(currentRecord.get("conId"));
		Ext.getCmp('conName').setValue(currentRecord.get("conName"));
		Ext.getCmp('reportCode').setValue(currentRecord.get("reportCode"));
		Ext.getCmp('startDate').setValue(currentRecord.get("startDate")==null?"":currentRecord.get("startDate").substring(0,10));
		Ext.getCmp('endDate').setValue(currentRecord.get("endDate")==null?"":currentRecord.get("endDate").substring(0,10));
		Ext.getCmp('prjLocation').setValue(currentRecord.get("prjLocation"));
		Ext.getCmp('projectPrice').setValue(currentRecord.get("projectPrice"));
		Ext.getCmp('checkDate').setValue(currentRecord.get("checkDate")==null?"":currentRecord.get("checkDate").substring(0,10));
		Ext.getCmp('quantities').setValue(currentRecord.get("quantities"));
		Ext.getCmp('entryDate').setValue(currentRecord.get("entryDate")==null?"":currentRecord.get("entryDate").substring(0,10));
		Ext.getCmp('entryBy').setValue(currentRecord.get("entryBy"));
		var prj = currentRecord.get("prjId");
		if(prj!=null&&prj!=''){
			var prjValue = prj.split(",");
			var pid= prjValue[0];
			var pname = prjValue[1];
			prjReg.setValue(pname);
			prjId.setValue(pid);	
		}
		
	} else {
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
//	alert(Ext.getCmp("startDate").getEl());

})
