var arg = window.dialogArguments;
var checkId = arg.checkId;
Ext.onReady(function(){
	
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
	


//承包方提出竣工验收申请
var check_apply = new Ext.form.TextArea({
	fieldLabel : '承包方提出竣工验收申请',
//	labelWidth:'30%',
	name:'check.checkApply',
	width:'100%'
	
})
//申请负责人 
var apply_by = new Ext.form.TextField({
	fieldLabel : '负责人',
	width:'100%',
	name:'check.applyBy'
})
//签字时间
	var apply_date = new Ext.form.TextField({
		fieldLabel : '签字时间',
		style : 'cursor:pointer',
		width:'100%',
		name:'check.applyDate',
		listeners : {
			focus : function(){
				WdatePicker({
					startDate : '%y-%m-%d',
					dateFmt : 'yyyy-MM-dd',
					alwaysUseStartDate : true
						
				});
				
			}
		}
	})


//退还出入证审核意见
var approve_text = new Ext.form.TextArea({
	fieldLabel : '退还出入证审核意见',
	name:'check.approveText',
	width:'100%'
	
})
//退还负责人 
var approve_by = new Ext.form.ComboBox({
	fieldLabel : '负责人',
	name:'approveby',
	editable:false,
	anchor : '100%',
	onTriggerClick : function() {
							var args = {
								selectModel : 'single',
								rootNode : {
									id : "0",
									text : '灞桥热电厂'
								}
							}
							var url = "/power/comm/jsp/hr/workerByDept/workerByDept2.jsp";
							var rvo = window
									.showModalDialog(
											url,
											args,
											'dialogWidth:550px;dialogHeight:400px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
							if (typeof(rvo) != "undefined") {
								approve_by.setValue(rvo.workerName);
								approve_by_code.setValue(rvo.workerCode);
							}
						}
})

var approve_by_code =  new Ext.form.TextField({
	name:'check.approveBy'
})
//退还签字时间
	var approve_date = new Ext.form.TextField({
		fieldLabel : '签字时间',
		style : 'cursor:pointer',
		width:'100%',
		name:'check.approveDate',
		listeners : {
			focus : function(){
				WdatePicker({
					startDate : '%y-%m-%d',
					dateFmt : 'yyyy-MM-dd',
					alwaysUseStartDate : true
						
				});
				
			}
		}
	})
	

//安监部门验收评价
var check_text = new Ext.form.TextArea({
	fieldLabel : '安监部门验收评价',
	width:'100%',
	name:'check.checkText'
	
})
//安监负责人 
var check_by = new Ext.form.ComboBox({
	fieldLabel : '负责人',
	name:'checkBy',
	editable:false,
	anchor : '100%',
	onTriggerClick : function() {
							var args = {
								selectModel : 'single',
								rootNode : {
									id : "0",
									text : '灞桥热电厂'
								}
							}
							var url = "/power/comm/jsp/hr/workerByDept/workerByDept2.jsp";
							var rvo = window
									.showModalDialog(
											url,
											args,
											'dialogWidth:550px;dialogHeight:400px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
							if (typeof(rvo) != "undefined") {
								check_by.setValue(rvo.workerName);
								check_by_code.setValue(rvo.workerCode);
							}
						}
})
var check_by_code = new Ext.form.Hidden({
	name:'check.checkBy'
})
//安监签字时间
	var check_date = new Ext.form.TextField({
		fieldLabel : '签字时间',
		style : 'cursor:pointer',
		name:'check.checkDate',
		width:'100%',
		listeners : {
			focus : function(){
				WdatePicker({
					startDate : '%y-%m-%d',
					dateFmt : 'yyyy-MM-dd',
					alwaysUseStartDate : true
						
				});
				
			}
		}
	})
	


	// 保存按钮
	var save = new Ext.Button({
				text : '保存',
				iconCls : 'save',
				handler : function() {
						blockForm.getForm().submit({
							method : 'POST',
							url : 'manageproject/backEtryCheck.action',
							params:{
								check_id:checkId
							},
							success : function(form, action) {
							var o = eval("(" + action.response.responseText + ")");
								Ext.Msg.alert("注意", o.status);
							},
							faliue : function() {
								Ext.Msg.alert('错误', o.status);
							}
						});
				}
			});

	var panel1 = new Ext.Panel({
				border : false,
//				labelWidth:'100',
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
																items : [check_apply]
															}]
												}, {
													layout : "column",
													border : false,
													items : [{
																columnWidth : 0.5,
																layout : "form",
																border : false,
																items : [apply_by]
															}, {
																columnWidth : 0.5,
																layout : "form",
																border : false,
																items : [apply_date]
															}]
												},{
													layout : "column",
													border : false,
													items : [{
																columnWidth : 1,
																layout : "form",
																border : false,
																items : [approve_text]
															}]
												},{
													layout : "column",
													border : false,
													items : [{
																columnWidth :0.5,
																layout : "form",
																border : false,
																items : [approve_by]
															}, {
																columnWidth : 0.5,
																layout : "form",
																border : false,
																items : [approve_date]
															}]
												} ,{
													layout : "column",
													border : false,
													items : [{
																columnWidth :1,
																layout : "form",
																border : false,
																items : [check_text]
															}]
												}
												 ,{
													layout : "column",
													border : false,
													items : [{
																columnWidth : 0.5,
																layout : "form",
																border : false,
																items : [check_by]
															}, {
																columnWidth : 0.5,
																layout : "form",
																border : false,
																items : [check_date]
															}]
												},{
													layout : "column",
													border : false,
													items : [{columnWidth :0.3,
																layout : "form",
																border : false,
																items : [{}]},{
																columnWidth :0.3,
																layout : "form",
																border : false,
																items : [save]
															}]
												},{
													layout : "column",
													border : false,
													items : [{
																columnWidth :0.3,
																layout : "form",
																hidden:true,
																border : false,
																items : [approve_by_code,check_by_code]
															}]
													}
												 ]
									}]
						}]
			});
	
	
		
			
	// 表单对象
	var blockForm = new Ext.form.FormPanel({
				labelAlign : 'right',
				frame : true,
				layout : 'column',
				closeAction : 'hide',
				fileUpload : true,
				items : [{
					xtype : 'fieldset',
					buttonAlign:'center',
					id : 'formSet',
					title : '工程竣工验收单回录',
					labelAlign : 'right',
					labelWidth : 140,
					defaultType : 'textfield',
					autoHeight : true,
					border : true,
					items : [panel1]
		}]

			});
		var view = new Ext.Viewport({
			layout:'fit',
			items:[blockForm]
		})
			
});