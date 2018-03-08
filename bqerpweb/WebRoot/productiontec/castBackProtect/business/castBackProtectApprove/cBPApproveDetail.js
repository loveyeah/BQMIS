Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
    Ext.QuickTips.init();
    
    var status = getParameter("status");
    var applyId = getParameter("applyId");
    var workflowNo = getParameter("workflowNo");
    var type=getParameter("protectionType");
    var blockId=getParameter("blockId");
    var executorMan = getParameter("executor");
    var flag = 0;
	function getDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate();
		s += (t > 9 ? "" : "0") + t + " ";
		t = d.getHours();
		s += (t > 9 ? "" : "0") + t + ":";
		t = d.getMinutes();
		s += (t > 9 ? "" : "0") + t + ":";
		t = d.getSeconds();
		s += (t > 9 ? "" : "0") + t;

		return s;
	};
	
	var applyCode = new Ext.form.TextField({
				id : 'applyCode',
				name : 'protectApply.applyCode',
				fieldLabel : '编号',
				readOnly : false,
				anchor : '95%'
			});

	var inOut = new Ext.form.TextField({
				id : 'inOut',
				name : 'protectApply.inOut',
				fieldLabel : '投/退',
				readOnly : true,
				anchor : '90%'
			});
			
	var protectionType = new Ext.form.TextField({
				id : 'protectionType',
				name : 'protectApply.protectionType',
				fieldLabel : '投退保护类型',
				readOnly : true,
				anchor : '95%'
			});
			
	var applyDep = new Ext.form.TextField({
				id : 'applyDep',
				name : 'deptName',
				editable : false,
				readOnly : true,
				fieldLabel : '申请部门',
				anchor : "90%"
			});
				
			
	var applyBy = new Ext.form.Hidden({
				id : 'applyBy',
				name : 'protectApply.applyBy'

			});
	var applyName = new Ext.form.TextField({
				id : 'applyName',
				fieldLabel : '申请人',
				allowBlank : false,
				readOnly : true,
				name : 'applyName',
				anchor : "95%"
			});		
			

	var applyTime = new Ext.form.TextField({
				id : 'applyTime',
				fieldLabel : "申请时间",
				name : 'protectApply.applyTime',
				style : 'cursor:pointer',
				anchor : "90%",
				readOnly : true
			});		
	
		var blockData = new Ext.data.SimpleStore({
				data : [[1, '300MW机组'], [4,'125MW机组']],
				fields : ['value', 'name']
			});
	
	var blockComboBox = new Ext.form.ComboBox({
				id : "blockId",
				fieldLabel:'机组',
				name:'blockComboBox',
				store : blockData,
				displayField : "name",
				valueField : "value",
				mode : 'local',
				triggerAction : 'all',
				hiddenName:'protectApply.blockId',
				readOnly : true,
				value:'',
		        anchor : '95%'
			}); 		
 
	var protectionName = new Ext.form.TextArea({
				id : 'protectionName',
				name : 'protectApply.protectionName',
				fieldLabel : '保护名称',
				readOnly : false,
				anchor : '95%'
			});		
		
	var protectionReason = new Ext.form.TextArea({
				id : 'protectionReason',
				name : 'protectApply.protectionReason',
				fieldLabel : '退出原因',
				readOnly : false,
				anchor : '95%'
			});	
			
	var measures = new Ext.form.TextArea({
				id : 'measures',
				name : 'protectApply.measures',
				fieldLabel : '安全、技术措施',
				readOnly : false,
				anchor : '95%'
			});	
			
	var memo = new Ext.form.TextArea({
				id : 'memo',
				name : 'protectApply.memo',
				fieldLabel : '备注',
				readOnly : true,
				anchor : '95%'
			});			
	
	var executor2 = new Ext.form.Hidden({
				id : 'executor2',
				name : 'protectApply.executor'

			});
	var executorName2 = new Ext.form.TextField({
				id : 'executorName2',
				fieldLabel : '执行人',
				allowBlank : false,
				readOnly : true,
				name : 'executorName2',
				anchor : "90%"
			});		
			
		
			
	var classLeader= new Ext.form.Hidden({
		id : 'classLeader',
		name : 'protectApply.classLeader'
	
	})
	var classLeaderName = new Ext.form.TriggerField({
		id : 'classLeaderName',
		fieldLabel : '班长',
		allowBlank : false,
		name : 'classLeaderName',
		anchor : "95%",
		onTriggerClick : function() {
			var url = "/power/comm/jsp/hr/workerByDept/workerByDept2.jsp";
			var args = {
				selectModel : 'single',
				rootNode : {
					id : '1',
					text : '灞桥热电厂'
				}
			}
			var emp = window
					.showModalDialog(
							url,
							args,
							'dialogWidth:600px;dialogHeight:420px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
			if (typeof(emp) != "undefined") {
				Ext.getCmp('classLeader').setValue(emp.workerCode);
				Ext.getCmp('classLeaderName').setValue(emp.workerName);
			}
		}
	})	

	var guardian= new Ext.form.Hidden({
		id : 'guardian',
		name : 'protectApply.guardian'
	
	})
	var guardianName = new Ext.form.TriggerField({
		id : 'guardianName',
		fieldLabel : '监护人',
		allowBlank : false,
		name : 'guardianName',
		anchor : "95%",
		onTriggerClick : function() {
			var url = "/power/comm/jsp/hr/workerByDept/workerByDept2.jsp";
			var args = {
				selectModel : 'single',
				rootNode : {
					id : '1',
					text : '灞桥热电厂'
				}
			}
			var emp = window
					.showModalDialog(
							url,
							args,
							'dialogWidth:600px;dialogHeight:420px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
			if (typeof(emp) != "undefined") {
				Ext.getCmp('guardian').setValue(emp.workerCode);
				Ext.getCmp('guardianName').setValue(emp.workerName);
			}
		}
	})	
	var executeTime = new Ext.form.TextField({
				id : 'executeTime',
				fieldLabel : "投退时间",
				name : 'protectApply.executeTime',
				style : 'cursor:pointer',
				anchor : "90%",
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d HH:mm:ss',
									dateFmt : 'yyyy-MM-dd HH:mm:ss',
									alwaysUseStartDate : true
								});
					}
				}
			});
			
	var executor = new Ext.form.Hidden({
		id : 'executor',
		name : 'protectApply.executor'
	
	})
	var executorName = new Ext.form.TriggerField({
		id : 'executorName',
		fieldLabel : '请选择执行人',
		allowBlank : false,
		name : 'executorName',
		anchor : "95%",
		onTriggerClick : function() {
			var url = "/power/comm/jsp/hr/workerByDept/workerByDept.jsp";
			var args = {
				selectModel : 'single',
				flag:'deptFilter',
				rootNode : {
					id : '0',
					text : '灞桥热电厂'
				}
			}
			var emp = window
					.showModalDialog(
							url,
							args,
							'dialogWidth:600px;dialogHeight:420px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
			if (typeof(emp) != "undefined") {
				Ext.getCmp('executor').setValue(emp.workerCode);
				Ext.getCmp('executorName').setValue(emp.workerName);
			}
		}
	})	
		
			
	var baseInfoField = new Ext.form.FieldSet({
				autoHeight : true,
				labelWidth : 100,
				style : {
					"padding-top" : '25',
					"padding-left" : '30'
				},
				bodyStyle : Ext.isIE
						? 'padding:0 0 5px 15px;'
						: 'padding:5px 15px;',

				anchor : '-20',
				border : false,
				buttonAlign : 'center',
				items : [{
								border : false,
								layout : 'column',
								items : [{
											columnWidth : .5,
											layout : 'form',
											border : false,
											items : [applyCode]
										}, {
											columnWidth : .5,
											layout : 'form',
											border : false,
											items : [inOut]
										}]
						 	}, {
								border : false,
								layout : 'column',
								items : [{
											columnWidth : 1,
											layout : 'form',
											border : false,
											items : [protectionType]
										}]
						 	},{
								border : false,
								layout : 'column',
								items : [{
											columnWidth : .5,
											layout : 'form',
											border : false,
											items : [blockComboBox]
										}, {
											columnWidth : .5,
											layout : 'form',
											border : false,
											items : [applyDep]
										}]
						 	},{
								border : false,
								layout : 'column',
								items : [{
											columnWidth : .5,
											layout : 'form',
											border : false,
											items : [applyName]
										}, {
											columnWidth : .5,
											layout : 'form',
											border : false,
											items : [applyTime]
										}]
						 	},protectionName,protectionReason,measures,memo]
			});

	var form = new Ext.form.FormPanel({
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

 function saveRecords() {
		var url = "";
		url = "productionrec/saveCastBackProtect.action"
		form.getForm().submit({
					method : 'POST',
					url : url,
					params : {
						applyId:applyId,
						status:status,
						entryId:workflowNo
					},
					success : function(formt, action) {
						var o = eval("(" + action.response.responseText + ")");
						Ext.Msg.alert("注意", o.msg);
						if (o.msg.indexOf("成功") != -1) {
							flag = 1;
							if(status==2){
							  blockId=blockComboBox.getValue()
							}
							
						}
					},
					faliue : function() {
						Ext.Msg.alert('错误', '出现未知错误.');
					}
				});

	}
 
  
  	function approve(){
  		if (status=='2'&&(blockComboBox.getValue() == null|| blockComboBox.getValue())  == "") {
				Ext.Msg.alert("提示", "审批前，请先指定投退保护所属的机组");
				return;
			}
		else if (status=='3'&&(applyCode.getValue() == null|| applyCode.getValue())  == "") {
				Ext.Msg.alert("提示", "审批前，请先填写申请单编号");
				return;
			}
		else if(status=='6'&&(executorMan==null||executorMan=="null"||executorMan=="")){
		       Ext.Msg.alert("提示", "审批前，请先指定执行人");
				return;
		}	
		
		else {

			var args = new Object();
			args.applyId = applyId;
			args.workflowType = "bqCastBackProtectApprove";
			args.entryId = workflowNo;
			args.protectionType=type;
			args.blockId=blockId;
			if(status=='6'){
				args.nextRolePs=executorMan;
		    }
		    else{
				args.nextRolePs="";
		    }
			args.title = "投退保护审批";
			var result = window
					.showModalDialog(
							'approveSign.jsp',
							args,
							'dialogWidth=700px;dialogHeight=500px;center=yes;help=no;resizable=no;status=no;');

		}
		window.location.replace("cBPApproveList.jsp");
	}
	
	
	var orderPanel = new Ext.FormPanel({
					frame : true,
					labelAlign : 'center',
					labelWidth : 100,
					closeAction : 'hide',
					title : '下达执行人命令',
					items : [
					executor,executorName
					]
				});

		var win = new Ext.Window({
					width : 300,
					height : 150,
					buttonAlign : "center",
					items : [orderPanel],
					layout : 'fit',
					closeAction : 'hide',
					resizable : false,
					modal : true,
					buttons : [{
						text : '确定',
						iconCls : 'confirm',
						handler : function() {
							var myurl = "";
								myurl = "productionrec/saveCastBackProtect.action";
						
							orderPanel.getForm().submit({
								method : 'POST',
								url : myurl,
								params : {
								applyId : applyId,
								status: status,
								entryId:workflowNo
							},
							success : function(form, action) {
						var o = eval("(" + action.response.responseText + ")");
						Ext.Msg.confirm("注意", o.msg, function(button) {

									if (button == 'yes') {
										if (o.msg.indexOf("成功") != -1) {
											win.hide();
											executorMan=executor.getValue();
//											window.location
//													.replace("cBPApproveList.jsp");
										}
									}
								});

					},
								faliue : function() {
									Ext.Msg.alert('错误', '出现未知错误.');

								}
							});
						}
					}, {
						text : '取消',
						iconCls : 'cancer',
						handler : function() {
							win.hide();
							window.location.replace("cBPApproveList.jsp");
						}
					}]

				});
	
	function order(){
	  win.show();
	  
	}

    // head工具栏
	var headTbar = new Ext.Toolbar({
				region : 'north',
				items : [{
							id : 'saveBtu',
							text : "保存",
							iconCls : 'save',
							handler : saveRecords
						},{
							id : 'approveBtu',
							text : '审批',
							iconCls : 'approve',
							handler : approve
						},{
							id : 'orderBtu',
							text : '下达执行命令',
							iconCls : 'save',
							handler : order
						}]
			});
    function init(){
		Ext.Ajax.request({
					method : 'post',
					url : 'productionrec/getCastBackProtectApproveList.action',
					params : {
						applyId : applyId
					},
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						
						if (result.list.length > 0) {
							var str = Ext.encode(result.list);
							var ob = eval("("
									+ str.substring(1, str.length - 1) + ")")

							applyCode.setValue(ob[1]);
							if (ob[2] == 'I')
								inOut.setValue("投入申请单");
							else if (ob[2] == 'O')
								inOut.setValue("退出申请单");
							if (ob[3] == '1')
								protectionType.setValue("其它继电保护及安全自动装置、调度自动化设备投退");
							else if (ob[3] == '2')
								protectionType.setValue("380V电压等级及以下设备继电保护及安全自动装置投退");
							else if (ob[3] == '3')
								protectionType.setValue("热控保护投退");
							applyDep.setValue(ob[5]);
							applyName.setValue(ob[7]);
							applyTime.setValue(ob[8]);
							protectionName.setValue(ob[9]);
							protectionReason.setValue(ob[10]);
							measures.setValue(ob[11]);
							memo.setValue(ob[22]);
							classLeaderName.setValue(ob[16]);
							executorName.setValue(ob[18]);
							guardianName.setValue(ob[20]);
							executeTime.setValue(ob[21]);
							if (ob[23] == '1')
								blockComboBox.setValue("300MW机组");
							else if (ob[23] == '4')
								blockComboBox.setValue("125MW机组");
						} 
					}
				});
				
			if(status == '2'){
			  blockComboBox.setDisabled(false);
			}
			else{
			  blockComboBox.setDisabled(true);
			}
				
		if (status == '1' || status == '2' || status == '3' || status == '4'|| status == '5'|| status == '6'){
			Ext.get("approveBtu").dom.parentNode.style.display = '';
			Ext.get("saveBtu").dom.parentNode.style.display = '';
		}
		else{
			Ext.get("approveBtu").dom.parentNode.style.display = 'none';
			Ext.get("saveBtu").dom.parentNode.style.display = 'none';
		}
		if (status == '6')
			Ext.get("orderBtu").dom.parentNode.style.display = '';
		else
			Ext.get("orderBtu").dom.parentNode.style.display = 'none';
	}
 
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		items : [{
			region : "north",
			layout : 'fit',
			height : 30,
			border : false,
			split : true,
			margins : '0 0 0 0',
			items : [headTbar]
		}, {
			region : "center",
			layout : 'fit',
			border : true,
			collapsible : true,
			split : true,
			margins : '0 0 0 0',
			items : [form]
		}]
	});
      init();
  
})