Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.ns("maint.expRRegister");
maint.expRRegister = function() {
	var reportIds;
	var workFlowNos;
	// 报销人
	var reportBy = new Ext.form.TextField({
				inputType:'text',
				readOnly:true,
				fieldLabel : '报销人',
				anchor : '100%'
	});
	// 报销时间
	var reportDate = new Ext.form.TextField({
				inputType:'text',
				readOnly:true,
				fieldLabel : '报销时间',
				anchor : '100%'
	});
	// 报销部门
	var reportDept = new Ext.form.TextField({
				inputType:'text',
				readOnly:true,
				fieldLabel : '报销部门',
				anchor : '100%'
	});
	// 报销金额小写
	var reportMoneyLower = new Ext.form.NumberField({
				name : 'model.reportMoneyLower',
				fieldLabel : '报销金额',
//				allowBlank : false,
				anchor : '100%'
	});
	// 报销金额大写
	var reportMoneyUpper = new Ext.form.Hidden({
				name : 'model.reportMoneyUpper'
			});
			
			//费用来源
			var itemName=new Power.budgetItem({
		        name:'itemId',
				fieldLabel : '费用来源',
				hiddenName : 'model.itemId',
		        anchor : '100%'
	},'2');
	// 用途
	var reportUse = new Ext.form.TextArea({
				inputType:'text',
				name : 'model.reportUse',
				height : 140,
				fieldLabel : '用途',
				//allowBlank : false,
				anchor : '90%'
	});
	// 备注
	var memo = new Ext.form.TextArea({
				inputType:'text',
				name : 'model.memo',
				height : 140,
				fieldLabel : '备注',
				//allowBlank : false,
				anchor : '90%'
	});
	// 费用单Id
	var reportId = new Ext.form.Hidden({
				name : 'model.reportId'
			});
	reportMoneyLower.on('blur',changeReportMoney);
	function changeReportMoney(){
		if(reportIds==null||reportIds==""){
			return false;
		};
		reportMoneyUpper.setValue(AmountInWords(reportMoneyLower.getValue(),2));
	}
//保存	
function save(){
		if(reportIds==null||reportIds==""){
			Ext.Msg.alert("提示", '请选择一条验收单信息！！');
			return false;
		};
		if (!textForm.getForm().isValid()) {
			return false;
		};
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
		Ext.Msg.wait("正在保存,请等待...");
		textForm.getForm().submit({
			method : 'POST',
			params : {
						},
			url : 'managebudget/saveOrUpdateExpR.action',
			success : function(form, action) {
				var o = eval("(" + action.response.responseText + ")");
				Ext.Msg.alert("提示", '操作成功！');
				reportMoneyUpper.setValue("");
			},
			faliue : function() {
				Ext.Msg.alert('错误', '出现未知错误.');
			}
		});
					
};
//签字
function sign() {
			if(reportIds==null||reportIds==""){
				Ext.Msg.alert("提示", '请选择一条验收单信息！！');
				return false;
			};
			if (workFlowNos == null || workFlowNos == "" || workFlowNos == undefined) {
				Ext.Msg.alert('提示', '流程还未启动');
			} else if (status == "7") {
				Ext.Msg.alert('提示', '审批流程已结束');
			} else {
					var args=new Object();
					args.entryId=workFlowNos;
					args.id=reportIds;
				  var o = window.showModalDialog('sign.jsp',
                args, 'dialogWidth=700px;dialogHeight=450px;status=no;');
				if (o) {
					Ext.getCmp("grid_div").getStore().reload();
					Ext.getCmp("maintab").setActiveTab(0);
			}
		}
}
//会签查询
 function CQuery(){  
 		if(reportIds==null||reportIds==""){
			Ext.Msg.alert("提示", '请选择一条验收单信息！！');
			return false;
		};
 	var url="";
        	if(workFlowNos==null||workFlowNos=="")
        	{
			 url = "/power/workflow/manager/flowshow/flowshow.jsp?flowCode=cBillwout";
        	}
        	else
        	{
        		url = "/power/workflow/manager/show/show.jsp?entryId="+workFlowNos;
        	}
        	window.open( url);
		}
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
// 会签查询按钮
var CQueryB=new Ext.Button({
			text : "会签查询",
			iconCls : 'view',
			handler : CQuery
	});
//顶部工具栏
var toolbar = new Ext.Toolbar({
items : [saveB, '-',signB,'-',CQueryB]

});
	var receiveContent = new Ext.form.FieldSet({
		height : '80%',
		layout : 'form',
		items : [{
			border : false,
			layout : 'column',
			items : [{
				columnWidth : .3,
				layout : 'form',
				border : false,
				labelWidth :70,
				items : [reportMoneyLower, reportBy]
			}, {
				columnWidth : .3,
				layout : 'form',
				labelWidth :70,
				border : false,
				items : [itemName.combo,reportDate]
			},{
				columnWidth : .3,
				layout : 'form',
				labelWidth :70,
				border : false,
				items : [reportDept]
			},{
				columnWidth : 1,
				layout : 'form',
				labelWidth :70,
				border : false,
				items : [reportUse]
			},{
				columnWidth : 1,
				layout : 'form',
				labelWidth :70,
				border : false,
				items : [memo]
			},reportMoneyUpper,reportId
			]
		}]
	});
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
			reportId.setValue(record.data.reportId);
			reportIds=record.data.reportId;
			reportMoneyLower.setValue(record.data.reportMoneyLower);
			reportMoneyUpper.setValue(record.data.reportMoneyUpper);
			itemName.setValue(record.data.itemId,record.data.itemName);
			reportDept.setValue(record.data.reportDept);
			reportBy.setValue(record.data.reportBy);
			reportDate.setValue(record.data.reportDate.substring(0,10));
			reportUse.setValue(record.data.reportUse);
			memo.setValue(record.data.memo);
			workFlowNos=record.data.workFlowNo;
			textForm.setTitle('费用报销单审批');
			reportDept.setDisabled(true);
			reportBy.setDisabled(true);
			reportDate.setDisabled(true);
			}
	}
		,resetInputField : resetFromRec
	}

}