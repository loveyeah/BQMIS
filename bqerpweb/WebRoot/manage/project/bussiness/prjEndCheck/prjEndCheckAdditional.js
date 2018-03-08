Ext.onReady(function(){
	var args = window.dialogArguments;
	var checkId = args.data.checkId;
	
	var projectContentCp = args.data.projectContent;
	var endInfoCp = args.data.endInfo;
	var checkTextCp = args.data.checkText;
	var qualityAssessCp =args.data.qualityAssess;
	var constructChargeByCp =args.data.constructChargeBy;
	var contractorChargeByCp =args.data.contractorChargeBy;

	var projectContent = new Ext.form.TextArea({
		id : "projectContent",
		fieldLabel : '工程简要内容',
		height: 150,
		value:projectContentCp,
		name : 'temp.projectContent',
		anchor : "86%"
	});
	var endInfo = new Ext.form.TextArea({
		id : "endInfo",
		fieldLabel : '竣工情况说明',
		height: 150,
		value:endInfoCp,
		name : 'temp.endInfo',
		anchor : "86%",
		scroll:true
	});
	var checkText = new Ext.form.TextField({
		id : "checkText",
		fieldLabel : '验收结论意见',
		value : checkTextCp,
		name : 'temp.checkText',
		anchor : "86%"
	});
	var qualityAssess = new Ext.form.TextField({
		id : "qualityAssess",
		fieldLabel : '质量评级',
		value : qualityAssessCp,
		name : 'temp.qualityAssess',
		anchor : "72%"
	});
	var constructChargeBy = new Ext.form.ComboBox({

		name : 'constructChargeBy123',
		id : 'constructChargeBy',
		fieldLabel : '建设单位负责人',
		value:constructChargeByCp,
		store : new Ext.data.SimpleStore({
				fields : ['id', 'name'],
				data : [[]]
		}),
		mode : 'remote',
		hiddenName:'oldConstructChargeBy',
		editable : false,
		anchor : "86%",
		triggerAction : 'all',
			onTriggerClick : function() {
					var url = "../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
					var args = {
					selectModel : 'single',
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
				Ext.form.ComboBox.superclass.setValue.call(Ext
				.getCmp('constructChargeBy'), emp.workerName);
				constructChargeByCode.setValue(emp.workerCode);
			}
		}
	});
	var constructChargeByCode=new Ext.form.Hidden({
				name:'constructChargeBy'
	});
	
	var contractorChargeBy = new Ext.form.TextField({
		id : "contractorChargeBy",
		fieldLabel : '施工单位负责人',
		value : contractorChargeByCp,
		name : 'temp.contractorChargeBy',
		anchor : "86%"
	});
	
	
	var workApplyField = new Ext.form.FieldSet({
		border : true,
		labelWidth : 100,
		labelAlign : 'right',
		buttonAlign : 'center',
		layout : 'form',
		title : '验收证书补录',
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
						items : [projectContent]
					},
					{
						columnWidth : 1,
						layout : "form",
						border : false,
						items : [endInfo]
					}, {
						columnWidth : 0.5,
						layout : "form",
						border : false,
						items : [checkText]
					}, {
						columnWidth : 0.5,
						layout : "form",
						border : false,
						items : [qualityAssess]
					}, {
						columnWidth : 1,
						layout : "form",
						border : false,
						items : [constructChargeBy,constructChargeByCode]
					}, {
						columnWidth : 1,
						layout : "form",
						border : false,
						items : [contractorChargeBy]
					}],
			buttons : [{
				text : '保存',
				iconCls : 'save',
				handler : saveCheckAdditional
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
	var workApplyViewport = new Ext.Viewport({
		layout : "fit",
		border : false,
		height : 150,
		items : [form],
		defaults : {
			autoScroll : true
		}
	}).show();
	
	function saveCheckAdditional(){
		
		var url = "manageproject/addPrjEndCheckAdditional.action";
		form.getForm().submit({
			method : 'POST',
			url : url,
			params : {
						checkId : checkId,
						projectContent : projectContent.getValue(),
						endInfo : endInfo.getValue(),
						checkText :checkText.getValue(),
						qualityAssess:qualityAssess.getValue(),
						constructChargeBy:constructChargeByCode.getValue(),
						contractorChargeBy:contractorChargeBy.getValue()
				
					},
			success : function(resp){
						alert('已成功添加补录！');
						window.returnValue = "success";
						window.close();
					},
			failure : function(resp){
						Ext.Msg.alert('警告','出现未知错误！');
					}
		});
			
		};

}
)