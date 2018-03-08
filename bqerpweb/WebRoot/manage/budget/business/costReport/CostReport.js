Ext.ns("CostReport.CRFill");
CostReport.CRFill = function() {
	
	var reportId = null;
	function getWorkName() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						if (result.workerCode) {
							// 设定默认工号
							//sessWorcode = result.workerCode;
							//sessWorname = result.workerName;
							//sessDeptCode = result.deptCode;
							//deptId = result.deptId;
							//sessDeptName = result.deptName;
							reportBy.setValue(result.workerName);
						}
					}
				});
		getDeptDatail();		
	}
	function getDeptDatail(){
		Ext.lib.Ajax.request('POST', 'comm/findFirstLeverDeptByDeptId.action', {
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
							reportDept.setValue(result.deptName);
							reportDeptCode.setValue(result.deptCode)
					}
				});
	}
	
	    getWorkName();
	var reportBy = new Ext.form.TextField({
		name : 'reportBy',
		xtype : 'textfield',
		fieldLabel : '报销人',
        readOnly:true,
		anchor : '90%',
     
		blankText : '不可为空！'
	});
	 reportBy.setDisabled(true);
	//reportBy.setValue(employee.getWorkerCode());
    	var reportDept= new Ext.form.TextField({
		name : 'reportDept',
		xtype : 'textfield',
		fieldLabel : '报销部门',
       	readOnly:true,
		anchor : '90%',
		blankText : '不可为空！'
	});
    reportDept.setDisabled(true);
	var reportDeptCode=new Ext.form.Hidden({
			name : 'model.reportDept'
	});
	var reportMoneyUpper = new Ext.form.TextField({
		name : 'model.reportMoneyUpper',
		xtype : 'textfield',
		fieldLabel : '报销金额(大写)',
		readOnly:true,
	
		anchor : '90%',
		blankText : '不可为空！'
	});
  // reportMoneyUpper.setDisabled(true);
	var reportMoneyLower = new Ext.form.NumberField({
		name : 'model.reportMoneyLower',
		//xtype : 'textfield',
		fieldLabel : '报销金额(小写)',

		anchor : '90%',
		blankText : '不可为空！'
	});

			
			//费用来源
			var itemName=new Power.budgetItem({
		        name:'itemId',
				fieldLabel : '费用来源',
				hiddenName : 'model.itemId',
		        anchor : '90%'
	},'2');
	

	var reportUse = new Ext.form.TextArea({
		name : 'model.reportUse',
		xtype : 'textfield',
		fieldLabel : '用途',
		readOnly : false,
		anchor : '90%',
		height : 150,
		blankText : '不可为空！'
	});
	var memo = new Ext.form.TextArea({
		name : 'model.memo',
		xtype : 'textfield',
		fieldLabel : '备注',
		readOnly : false,
		anchor : '90%',
		height : 150,
		blankText : '不可为空！'
	});

	function saveCostReport() {
		var url = "";

		if (reportId == "" || reportId == null) {
			url = "managebudget/addCostReport.action"
		} else {
			url = "managebudget/updateCostReport.action"
		}
//    if(Ext.get("model.itemId").dom.value=="null")
//    {
//    	Ext.get("model.itemId").dom.value="";
//    }
		var alertMsg="";
		if(Ext.get("model.itemId").dom.value==null||Ext.get("model.itemId").dom.value==""||Ext.get("model.itemId").dom.value=="null")
		{
			alertMsg="费用来源";
			
		}
		if(reportMoneyLower.getValue()==null||reportMoneyLower.getValue()=="")
		{
		if(alertMsg=="")
		{
			alertMsg+="报销金额";
		}
		else
		{
			alertMsg+=",报销金额";
		}
		}
		if(alertMsg!="")
		{
			Ext.Msg.alert("提示","请填写"+alertMsg+"!");
			return;
		}
		CostReportForm.getForm().submit({
			method : 'POST',
			url : url,
			params : {
				reportId : reportId
			},
			success : function(form, action) {
				var o = eval("(" + action.response.responseText + ")");
				Ext.Msg.alert("注意", o.msg);
				Ext.getCmp("div_tabs").setActiveTab(1);
				Ext.getCmp("div_grid").getStore().reload();
			},
			faliue : function() {
				Ext.Msg.alert('错误', '出现未知错误.');
			}
		});
	}
	var baseInfoField = new Ext.form.FieldSet({
		autoHeight : true,
		title : '费用报销单填写',
		height : '80%',
		layout : 'form',
//		style : {
//			"padding-top" : '30',
//			"padding-left" : '30',
//			"padding-right" : '30'
//		},
//		bodyStyle : Ext.isIE ? 'padding:0 0 5px 15px;' : 'padding:5px 15px;',
//
//		anchor : '-20',
		buttonAlign : 'center',
		items : [{
			border : false,
			layout : 'form',
			items : [{
				border : false,
				layout : 'column',
				items : [{
					columnWidth : .3,
					layout : 'form',
					labelWidth :90,
					border : false,
					items : [reportDept,reportDeptCode]
				}, {
					columnWidth : .3,
					layout : 'form',
					labelWidth :70,
					border : false,
					items : [reportBy]
				} ,{
					columnWidth : .34,
					layout : 'form',
					labelWidth :70,
					border : false,
					items : [itemName.combo]
				}, {
					columnWidth : .48,
					layout : 'form',
					labelWidth : 90,
					border : false,
					items : [reportMoneyUpper]
				}, {
					columnWidth : .47,
					layout : 'form',
					labelWidth : 90,
					border : false,
					items : [reportMoneyLower]
				}]
			},

				{
				border : false,
				layout : 'column',
				items : [{
					columnWidth : 1,
					layout : 'form',
					labelWidth : 90,
					border : false,
					items : [reportUse, memo]
				}]
			}]

		}],
		buttons : [{
			text : '保存',
			iconCls : 'save',
			handler : saveCostReport
		}, {
			text : '取消',
			iconCls : 'cancer',
			handler : function() {
				CostReportForm.getForm().reset();
					    getWorkName();
			}
		}]

	});
	reportMoneyLower.on('blur',moneyUpper);
	function moneyUpper(){
	
		reportMoneyUpper.setValue(AmountInWords(reportMoneyLower.getValue(),2));
	
	}
	var CostReportForm = new Ext.FormPanel({
		layout : 'fit',
		border : false,
		frame : true,
		fileUpload : true,
		items : [baseInfoField],
		bodyStyle : {
			'padding-top' : '5px'
		},
		defaults : {
			labelAlign : 'center'
		}
	});

    
	return {
		form : CostReportForm,
		setReportId : function(_reportId) {
			reportId = _reportId;
		},
		setDetail : function(member) {
			
			reportMoneyUpper.setValue(member.get("reportMoneyUpper"));
			reportMoneyLower.setValue(member.get("reportMoneyLower"));
		    reportUse.setValue(member.get("reportUse"));
			memo.setValue(member.get("memo"));
			reportBy.setValue(member.get("reportByName"));
			reportDept.setValue(member.get("deptName"));
//			alert(member.get("itemId"));
//			itemName.combo.setValue(member.get("itemId"));
			itemName.setValue(member.get("itemId"),member.get("itemName"));
			
		},
		clearForm : function() {
			CostReportForm.getForm().reset();
		    getWorkName();
			reportId = null;

		}
	}

};
