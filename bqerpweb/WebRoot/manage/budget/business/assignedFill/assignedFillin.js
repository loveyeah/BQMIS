Ext.ns("assignedFill.assignedFillin");
assignedFill.assignedFillin = function() {
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
	var strAssignId = null;	
	
	var assignId = new Ext.form.Hidden({
				id : 'assignId',
				name : 'temp.assignId',
				xtype : 'textfield',
				fieldLabel : '外委ID',
				anchor : '85%'
			});
	
	var assignName = new Ext.form.TextField({
				name : 'temp.assignName',
				xtype : 'textfield',
				fieldLabel : '外委名称',
				allowBlank : false,
				readOnly : false,
				anchor : '90%'
			});
			
	//费用来源
	var itemId=new Power.budgetItem({
		        name:'itemId',
				fieldLabel : '费用来源',
				hiddenName : 'temp.itemId',
		        anchor : '90%',
		        editable : true,
				allowBlank : false,
				triggerAction : 'all'
	},'');
	
   var applyBy = new Ext.form.TextField({				
				xtype : 'textfield',
				fieldLabel : '申请人',
				readOnly : true,
				anchor : '90%'
				});
	var applyByValue = new Ext.form.Hidden({
					name : 'temp.applyBy'			
				})
				
	 var applyDept = new Ext.form.TextField({				
				xtype : 'textfield',
				fieldLabel : '申请部门',
				readOnly : true,
				anchor : '90%'
				});
	var applyDeptCode=new Ext.form.Hidden({
				name:'temp.applyDept'
	})
	var applyDeptValue = new Ext.form.Hidden({
			name : 'temp.applyDept'
		})
				
				
	 var applyDate = new Ext.form.TextField({
				name : 'temp.applyDate',
				xtype : 'textfield',
				fieldLabel : '申请时间',
//				value:getDate(),
				readOnly : true,
				anchor : '90%'
				});
			
	 var estimateMoney = new Ext.form.NumberField({
				name : 'temp.estimateMoney',
				xtype : 'textfield',
				fieldLabel : '估算金额',
				allowBlank : false,
				readOnly : false,
				anchor : '90%'
				});
			
	var assignFunction = new Ext.form.TextArea({
				name : 'temp.assignFunction',
				xtype : 'textfield',
				fieldLabel : '用途',
				readOnly : false,
				anchor : '95.5%',
				atuoWidth: true,
				height:150
			});
			
	var memo = new Ext.form.TextArea({
				name : 'temp.memo',
				xtype : 'textfield',
				fieldLabel : '备注',
				readOnly : false,
				anchor : '95.5%',
				atuoWidth: true,
				height:150
			});		
			
   function saveAssignedFill() {
   	
   		
		var url = "";
		if (assignId.getValue() == "" || assignId.getValue() == null) {			
			url = "managebudget/saveAssignedFill.action"
		} else {
			url = "managebudget/updateAssignedFill.action"
		}
 	
		assignedFillForm.getForm().submit({
					method : 'POST',
					url : url,
					params : {
							assignId : strAssignId,
							applyDept:applyDeptCode.getValue()
						},
					success : function(form, action) {
						var o = eval("(" + action.response.responseText + ")");
						Ext.Msg.alert("注意", o.msg);
						Ext.getCmp("div_tabs").setActiveTab(0);
			            Ext.getCmp("div_grid").getStore().reload();
					},
					faliue : function() {
						Ext.Msg.alert('错误', '出现未知错误.');
					}
				});
	 }

	 	
	function getWorkDetail() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
							applyDate.setValue(getDate());
							applyBy.setValue(result.workerName);
					}
				});
	getDeptDatail();
	}
	function getDeptDatail(){
		Ext.lib.Ajax.request('POST', 'comm/findFirstLeverDeptByDeptId.action', {
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
							applyDept.setValue(result.deptName);
							applyDeptCode.setValue(result.deptCode)
					}
				});
	}
	var assignedFillField = new Ext.form.FieldSet({
		autoHeight : true,
	    title : '外委单填写',
		height : '100%',
		layout : 'form',
		style : {
			"padding-top" : '30',
			"padding-left" : '30',
			"padding-right" : '30'
		},
		bodyStyle : Ext.isIE ? 'padding:0 0 5px 15px;' : 'padding:5px 15px;',

		anchor : '-20',
		buttonAlign : 'center',
		items : [{
			border : false,
			layout : 'form',
			items : [{
						border : false,
						layout : 'column',
						items : [{
									columnWidth : 1,
									layout : 'form',
//									labelWidth : 70,
									border : false,
									items : [assignId]
								}]
					},{
						border : false,
						layout : 'column',
						items : [{
									columnWidth : 0.3,
									layout : 'form',
									border : false,
//									labelWidth : 70,
									items : [assignName]
								}, {
									columnWidth : 0.3,
									layout : 'form',
//									labelWidth : 70,
									border : false,
									items : [estimateMoney]
								}, {
									columnWidth : 0.4,
									layout : 'form',
//									labelWidth :70,
									border : false,
									items : [itemId.combo]
								}]
					}, {
						border : false,
						layout : 'column',
						items : [{
									columnWidth : 0.299,
									layout : 'form',
//									labelWidth : 70,
									border : false,
									items : [applyDept]
								}, {
									columnWidth : 0.001,
									layout : 'form',
//									labelWidth : 70,
									border : false,
									items : [applyDeptValue]
								},{
									columnWidth : 0.299,
									layout : 'form',
									border : false,
									items : [applyBy]
								},  {
									columnWidth : 0.001,
									layout : 'form',
									border : false,
									items : [applyByValue]
								},{
									columnWidth : 0.4,
									layout : 'form',
									border : false,
									items : [applyDate]
								}]
					}, {
						border : false,
						layout : 'column',
						items : [{
									columnWidth : 1,
									layout : 'form',
//									labelWidth : 70,
									border : false,
									items : [assignFunction]
								}]
					}, {
						border : false,
						layout : 'column',
						items : [{
									columnWidth : 1,
									layout : 'form',
//									labelWidth : 70,
									border : false,
									items : [memo]
								}]
					}]

		}],
		buttons : [{
					text : '保存',
					iconCls : 'save',
					handler : saveAssignedFill
				}, {
					text : '取消',
					iconCls : 'cancer',
					handler : function() {
						assignedFillForm.getForm().reset();
						getWorkDetail();
					}
				}]

	});

	var assignedFillForm = new Ext.FormPanel({
				layout:'fit',
				border : false,
				frame : true,	
				fileUpload : true,
				items : [assignedFillField],
				bodyStyle : {
					'padding-top' : '5px'
				},
				defaults : {
					labelAlign : 'center'
				}
			});
	
	
    getWorkDetail();

	return {
		form:assignedFillForm,
		setAssignId : function(_assignId) {
			strAssignId = _assignId;
			},
		setDetail:function(member){
//			alert(member.get("applyBy"));
			  assignId.setValue(member.get("assignId"));
//			  var commValue = member.get("assignName").split(',');
			  assignName.setValue(member.get("assignName"));
			  itemId.setValue(member.get("itemId"),member.get("itemName"));
			  estimateMoney.setValue(member.get("estimateMoney"));
			  assignFunction.setValue(member.get("assignFunction"));
			  memo.setValue(member.get("memo"));
			  applyBy.setValue(member.get("applyBy"));
			  applyByValue.setValue(member.get("applyById"));
			  applyDate.setValue(member.get("applyDate").substring(0,10));
			  applyDept.setValue(member.get("applyDept"));
			  applyDeptValue.setValue(member.get("applyDeptId"));
			  
		},
		clearForm:function()
		{
			assignedFillForm.getForm().reset();
			assignedFillId = null;
			getWorkDetail();
			
		}
	}
};