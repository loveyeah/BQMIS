Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.form.Label.prototype.setText = function(argText) {
	this.el.dom.innerHTML = argText;
}
// 获取时间
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
}
 var nrs = "";
    function removeAppointNextRoles(){
	if(confirm("确定要清除指定下一步人吗？")){
		nrs = "";
		Ext.get("showAppointNextRoles").dom.innerHTML = "";
	}
}
Ext.onReady(function() {
    var arg = window.dialogArguments
    
    var strBusiNo = arg.busiNo;
    var workticketType=arg.workticketType;
    var entryId =arg.entryId;
    var flowCode=arg.flowCode;
    var mytitle=arg.title;
    var actionId="";
    var eventIdentify="";
     var workerCode="";
    
     var busiStatus=arg.busiStatus;//add 
    var ifSeeDanger=false;
    var fireLevelId=arg.fireLevelId;
    
    // ↓↓*******************************************************
    


var btnPrintWorkticket = new Ext.Button({
				text : '打印工作票',
				align : 'right',
				handler : showPrintWindow
			})
			
	function showPrintWindow() {
	
			    Ext.Ajax.request({
				url : 'bqworkticket/dhCreateTicket.action',
				params : {
					workticketNo :  strBusiNo 
				},
				method : 'post',
				success : function(result, request) {
				},
				failure : function(result, request) {
					Ext.MessageBox.alert('错误', '操作失败!');
				}
			});
			var strReportAdds = getReportUrl(workticketType,strBusiNo,fireLevelId);
			if (strReportAdds != "") {
				window.open(strReportAdds);
			} else {
				Ext.Msg.alert("目前没有该种工作票票面预览");
			}
		
			
		

	}			
  
    var label = new Ext.form.Label({
        id : "label",
        text : mytitle,
         style : "color:blue;font-size:30px;line-height:100px;padding-left:150px;font-weight:bold"
    });
    // 标题
    var titleField = new Ext.Panel({
        border : false,
        height : 100,
        items : [label],
        	buttons : [btnPrintWorkticket],
				defaults : {
					buttonAlign : 'right'
				}
    })
    
    // 设置响应时间
    var timeCheck = new Ext.form.Checkbox({
        boxLabel : "设置响应时间",
        style : "padding-top:120;border-width:1px 0 0 0;",
        listeners : {
            check : function(box, checked) {
                if (checked) {
                    // 如果checkbox选中,显示时间选择textfield
                    Ext.getCmp('datePicker').setVisible(true);
                } else {
                    Ext.getCmp('datePicker').setVisible(false);
                }
            }
        }
    });
    // 时间选择
    var datePicker = new Ext.form.TextField({
        style : 'cursor:pointer',
        id : "datePicker",
        hidden : true,
        value : getDate(),
        listeners : {
            focus : function() {
                WdatePicker({
                            startDate : '%y-%M-01 00:00:00',
                            dateFmt : 'yyyy-MM-dd HH:mm:ss',
                            alwaysUseStartDate : true
                        });
            }
        }
    });
    // 图形显示
    var graphButton = new Ext.Button({
        text : "图形展示",
        handler : function(){  
        	var url="";
        	if(entryId==null||entryId=="")
        	{
			 url = "/power/workflow/manager/flowshow/flowshow.jsp?flowCode="+flowCode;
        	}
        	else
        	{
        		url = "/power/workflow/manager/show/show.jsp?entryId="+entryId;
        	}
        	window.open(url);
		}
    });
    // 查看审批记录
    var approveHistoryButton = new Ext.Button({
        text : "查看审批记录",
        handler : function() {
			var url = "/power/workflow/manager/approveInfo/approveInfo.jsp?entryId="
					+ entryId;
			window.showModalDialog(
							url,
							null,
							"dialogWidth:650px;dialogHeight:400px;;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;");
		}
    });
    // 显示下步角色
    
    var btnNextDisplay = new Ext.Button({
        text : "显示下步角色",
        handler:getNextSteps
    });
    // 显示指定下一步角色
	var showAppointNextRoles = new Ext.form.Label({
		border : false,
		id:'showAppointNextRoles',
		style : "color:red;font-size:12px"
	});
    	function getNextSteps() {
		//var actionId = Ext.ComponentMgr.get('approve-radio').getGroupValue();
		var args = new Object();
		args.flowCode =flowCode;
		args.actionId =actionId;
		args.entryId = entryId;
		var url="";
		if(entryId==null||entryId=="")
		{
			url = "/power/workflow/manager/appointNextRole/appointNextRoleForReport.jsp";
		}
		else
		{
		 url = "/power/workflow/manager/appointNextRole/appointNextRole.jsp";
		}
		
		var ro = window
				.showModalDialog(
						url,
						args,
						"dialogWidth:400px;dialogHeight:350px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;");
		if (typeof(ro) != "undefined") {
			if (ro) {
				nrs = ro.nrs;
				showAppointNextRoles
						.setText("<span style=\"cursor:hand;color:blue\"  onclick=\"removeAppointNextRoles();\">[取消指定]</span>  您指定下一步角色为：[ "
								+ ro.nrsname + " ]");
			}
		}
	};
    // 第二行
    var tableField = new Ext.Panel({
        border : false,
        height : 70,
        autoWidth : false,
        anchor : '100%',
        style : "padding-top:20;padding-bottom:20;border-width:1px 0 0 0;",
        layout : "column",
        items : [
				// 设置响应时间checkbox
				{
					columnWidth : 0.185,
					id : "tiemcheck",
					layout : "form",
					hideLabels : true,
					border : false,
					items : [timeCheck]
				},
				// 时间选择
				{
					columnWidth : 0.265,
					hideLabels : true,
					id : "datepickerPanel",
					layout : "form",
					border : false,
					items : [datePicker]
				}, {
					columnWidth : 0.02,
					border : false
				},
				// 图形显示按钮
				{
					columnWidth : 0.145,
					layout : "form",
					hideLabels : true,
					border : false,
					items : [graphButton]
				},
				// 查看审批记录按钮
				{
					columnWidth : 0.19,
					layout : "form",
					hideLabels : true,
					border : false,
					id:'approve_his',
					items : [approveHistoryButton]
				},
				// 下一步按钮
				{
					columnWidth : 0.19,
					layout : "form",
					hideLabels : true,
					border : false,
					id:'approve_next',
					items : [btnNextDisplay]
				}]

    });
    // 审批方式label
    var approveLabel = new Ext.form.Label({
        text : " 审批方式：",
        style : 'font-size:12px'
    });
    // 单选按钮组
    var radioSet = new Ext.Panel({
        height : 50,
        border : false,
        layout : "column"
    });
    // 审批方式panel
    var approveSet = new Ext.Panel({
        height : 80,
        border : false,
        layout : 'form',
        style : "padding-top:20;border-width:1px 0 0 0;",
        items : [approveLabel, radioSet]
    });

    /**
	 * 审批方式radio button生成
	 */

	function radioAddHandler(radio) {
		
		if (radio instanceof Array) {
			if (!radioSet.items) {
				radioSet.items = new Ext.util.MixedCollection();
			}
			if (radio.length > 0) {
				var _radio = new Ext.form.Radio({
					boxLabel : "<font size='2.6px'>" + radio[0].actionName
							+ "</font>",
					id : 'approve-radio'+radio[0].actionId,
					name : 'rb-approve',
					inputValue : radio[0].actionId,
					url:  radio[0].url,
					eventIdentify: radio[0].changeBusiStateTo,
					listeners:{
						'check' : function(){
							 var _actionId = Ext.ComponentMgr.get('approve-radio'+radio[0].actionId).getGroupValue();   
							 var obj = Ext.getCmp('approve-radio'+_actionId );
							 actionId = _actionId;
							 eventIdentify=obj.eventIdentify
							
						}	 
					},
					checked : true
				}); 
				radioSet.items.add(_radio); 
				// 添加隐藏域，为了使radio正常显示
				radioSet.items.add(new Ext.form.Hidden());

				for (var i = 1; i < radio.length; i++) {
					// 添加隐藏域，为了使radio正常显示
					radioSet.items.add(new Ext.form.Hidden());
					var _radio = new Ext.form.Radio({
						boxLabel : "<font size='2.6px'>" + radio[i].actionName
								+ "</font>",
						id : 'approve-radio'+radio[i].actionId,
						name : 'rb-approve', 
						url:  radio[i].url,
						eventIdentify: radio[i].changeBusiStateTo,
						inputValue : radio[i].actionId
					});
					  
					radioSet.items.add(_radio);
					// 添加隐藏域，为了使radio正常显示
					radioSet.items.add(new Ext.form.Hidden());
				}
				
					//加作废审批状态-----
			
					radioSet.items.add(new Ext.form.Hidden());
					 _radio = new Ext.form.Radio({
						boxLabel : "<font size='2.6px'>未执行/作废</font>",
						id : 'approve-radio0',
						name : 'rb-approve', 
						url:'',
						eventIdentify: 'ZF',
						inputValue : '0'
					});
					  
					radioSet.items.add(_radio);
					// 添加隐藏域，为了使radio正常显示
					radioSet.items.add(new Ext.form.Hidden());
				//-----------------------
			}

		}
		radioSet.doLayout();
	}

    // *****************************备注******************
    // 备注label
    var backLabel = new Ext.form.Label({
        text : " 备注：",
        style : 'font-size:12px',
        height : 20
    });
    // 备注输入框
    var approveText  = new Ext.form.TextArea({
        width : '100%',
        autoScroll : true,
        name : 'power.approveText',
        isFormField : true,
        border : false,
        value : "请填写审批意见！",
        height : 90
    })
    // 第三行
    var remarkSet = new Ext.Panel({
        height : 120,
        border : false,
        style : "padding-top:10;border-width:1px 0 0 0;",
        items : [backLabel, approveText]
    });
    
//    //----------------------
   var endDate = new Ext.form.TextField({
        id : 'endDate',
        fieldLabel : "工作结束时间",
        name : 'hisModel.oldApprovedFinishDate',
        style : 'cursor:pointer',
        anchor : "80%",
        value : getDate(),
        listeners : {
            focus : function() {
                WdatePicker({
                    startDate : '%y-%M-%d 00:00:00',
                    dateFmt : 'yyyy-MM-dd HH:mm:ss',
                    alwaysUseStartDate : true
                });
            }
        }
    });
     var endChargeBy = {
		fieldLabel : '动火执行人',
		name : 'endChargeBy',
		xtype : 'combo',
		id : 'endChargeBy',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		 hiddenName : 'hisModel.oldChargeBy',
		editable : false,
		anchor : "80%",
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
			var args = {
				selectModel : 'single',
				notIn : "'999999'",
				rootNode : {
					id : '-1',
					text : '合肥电厂'
				}
			}
			var emp = window
					.showModalDialog(
							url,
							args,
							'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');

			if (typeof(emp) != "undefined") {
				Ext.getCmp('endChargeBy').setValue(emp.workerCode);
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('endChargeBy'), emp.workerName);
			}
		}
	};
	
	 var endAcceptBy = {
		fieldLabel : '消防监护人',
		name : 'endAcceptBy',
		xtype : 'combo',
		id : 'endAcceptBy',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
	   hiddenName : 'hisModel.newChargeBy',
		editable : false,
		anchor : "80%",
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
			var args = {
				selectModel : 'single',
				notIn : "'999999'",
				rootNode : {
					id : '-1',
					text : '合肥电厂'
				}
			}
			var emp = window
					.showModalDialog(
							url,
							args,
							'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');

			if (typeof(emp) != "undefined") {
				Ext.getCmp('endAcceptBy').setValue(emp.workerCode);
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('endAcceptBy'), emp.workerName);
			}
		}
	};
	var endPemitBy = {
		fieldLabel : '动火执行人（许可回填）',
		name : 'endPemitBy',
		xtype : 'combo',
		id : 'endPemitBy',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
	    hiddenName : 'hisModel.fireBy',
		editable : false,
		anchor : "80%",
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
			var args = {
				selectModel : 'single',
				notIn : "'999999'",
				rootNode : {
					id : '-1',
					text : '合肥电厂'
				}
			}
			var emp = window
					.showModalDialog(
							url,
							args,
							'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');

			if (typeof(emp) != "undefined") {
				Ext.getCmp('endPemitBy').setValue(emp.workerCode);
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('endPemitBy'), emp.workerName);
			}
		}
	};
	
	var endDutyBy = {
		fieldLabel : '值长（一级动火）/运行班长（二级动火）',
		name : 'endDutyBy',
		xtype : 'combo',
		id : 'endDutyBy',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		 hiddenName : 'hisModel.dutyChargeBy',
		editable : false,
		anchor : "80%",
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
			var args = {
				selectModel : 'single',
				notIn : "'999999'",
				rootNode : {
					id : '-1',
					text : '合肥电厂'
				}
			}
			var emp = window
					.showModalDialog(
							url,
							args,
							'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');

			if (typeof(emp) != "undefined") {
				Ext.getCmp('endDutyBy').setValue(emp.workerCode);
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('endDutyBy'), emp.workerName);
			}
		}
	};
	


    
 
    	var permitPanel = new Ext.Panel({
       // height : 40,
        border : false,
        style : "padding-top:10;border-width:1px 0 0 0;",
        layout : "column",
        labelWidth:140,
        labelAlign:"right",
        items : [{
					columnWidth : 0.7,
					layout : "form",
					border : false,
					items : [endPemitBy]
				}]
    });
    
		var endPanel = new Ext.Panel({
       // height : 40,
        border : false,
        style : "padding-top:10;border-width:1px 0 0 0;",
        layout : "column",
        labelWidth:100,
        labelAlign:"right",
        items : [{
					columnWidth : 0.5,
					layout : "form",
					border : false,
					items : [endDate,endDutyBy]
				},{
					columnWidth : 0.5,
					layout : "form",
					border : false,
					items : [endChargeBy,endAcceptBy]
				}]
    });
// //----------------------   


    var form = new Ext.FormPanel({
		columnWidth : 0.8,
		border : true,
		autoScroll : true,
		labelAlign : 'center',
		buttonAlign : 'right',
		buttons : [{
			text : "确定",
			handler : function() {
				// 显示用户验证窗口
         if(workticketType!="4")
         {
         	//动火票无危险点，不需判断
				if (!ifSeeDanger) {
					Ext.Msg.alert("提示", "请查看危险点！");
					return;
				}
         }
				workerPanel.getForm().reset();
				validateWin.show();

			}
		}, {
			text : "取消",
			handler : function() {
				// layout.hidden = true;
				window.close();
			}
		}],
		items : [titleField,permitPanel,endPanel,tableField, showAppointNextRoles, approveSet,
				remarkSet]
	});
  

    // 显示区域
// var layout = new Ext.Viewport({
// layout : 'fit',
// border : false,
// height : 950,
// width : 600,
// items : [{
// layout : 'fit',
// margins : '0 0 0 0',
// region : 'center',
// autoScroll : true,
// items : [signPanel]
//                }],
//        defaults : {
//            autoScroll : true
//        }
//    });
    // ↓↓****************员工验证窗口****************

    // 工号
    var workCode = new Ext.form.TextField({
        id : "workCode",
        fieldLabel : '工号<font color ="red">*</font>',
        allowBlank : false,
        width : 120
    });

    // 密码
    var workPwd = new Ext.form.TextField({
        id : "workPwd",
        fieldLabel : '密码<font color ="red">*</font>',
        allowBlank : false,
        inputType : "password",
        width : 120
    });
    // 弹出窗口panel
    var workerPanel = new Ext.FormPanel({
        frame : true,
        labelAlign : 'right',
        height : 120,
        items : [workCode, workPwd]
    });

    // 弹出窗口
    var validateWin = new Ext.Window({
        width : 300,
        height : 140,
        title : "请输入工号和密码",
        buttonAlign : "center",
        resizable : false,
        modal:true,
        items : [workerPanel],
        buttons : [{
            text : '确定',
            id:'btnSign',
            handler : function() {
           
            	
                // 工号确认
                Ext.lib.Ajax.request('POST', 'comm/workticketApproveCheckUser.action',
                    {success : function(action) {
                        var result = eval(action.responseText);
                        // 如果验证成功，进行签字操作
                        if (result) {
             //----------审批------------------------
            form.getForm().submit({
			url : 'bqworkticket/fireEndApprove.action',
			waitMsg : '正在保存数据...',
			method : 'post',
			params : {
				workticketNo : strBusiNo,
				workerCode : workCode.getValue(),
				//workflowNo:entryId,
				nextRoles : nrs,
				actionId:actionId,
				eventIdentify:eventIdentify,
				responseDate:datePicker.getValue(),
				approveText:approveText.getValue()
			},
			success : function(form,action) { 
				var o = eval("(" + action.response.responseText + ")"); 
				Ext.Msg.alert("提示", o.msg, function() {
					if(o.msg.indexOf('成功')!=-1)
					{
						window.returnValue=true;
					window.close();
					
					}
					validateWin.hide();
				});

			},
			faliue : function() {
				Ext.Msg.alert("提示","未知错误");
			}

		}); 
                        //------------	
    
                        } else {
                            Ext.Msg.alert("提示","密码错误");
                        }
                    }
                }, "workerCode=" + workCode.getValue() + "&loginPwd=" + workPwd.getValue());
            }
        }, {
            // 取消按钮
            text :"取消",
            iconCls :"cancer",
            handler : function() {
                validateWin.hide();
            }
        }],
        listeners : {
            show : function(com) {
                // 取得默认工号
               workCode.setValue(workerCode);
            }
        },
        closeAction : 'hide'
    });
    
    function getWorkCode()
    {
     Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
                    success : function(action) {
                    	 var result = eval("(" + action.responseText + ")");
                        if (result.workerCode) {
                            // 设定默认工号
                        	workerCode=result.workerCode;
                          
                        }
                    }
                });
    }
    getWorkCode();
    
　　 // ↑↑****************员工验证窗口****************

    	function getFirstSteps() {
    	
		Ext.Ajax.request({
			url : "MAINTWorkflow.do?action=getFirstStep",
			method : 'post',
			params : {
				flowCode : flowCode
				//jsonArgs : "{'isDBP':false,'is12Failure':true,'isYSZC':true}"
			},
			success : function(result, request) {
				
				var radio = eval('(' + result.responseText + ')'); 
			
				radioAddHandler(radio.actions);
			},
			failure : function() {
				 Ext.Msg.alert("提示","错误");
			}
		});
	};
	
 function getCurrentSteps() {
		Ext.Ajax.request({
			url : "MAINTWorkflow.do?action=getCurrentSteps",
			method : 'post',
			params : {
				entryId : entryId,
				workerCode : workerCode
//				jsonArgs : "{'isDBP':false,'is12Failure':true,'isYSZC':true}"
			},
			success : function(result, request) {
				var radio = eval('(' + result.responseText + ')'); 
				radioAddHandler(radio[0].actions);
			},
			failure : function() {
				
			}
		});
	};
	
   
    
    //签字时的Enter
    document.onkeydown=function() {  
          if (event.keyCode==13) {  
         		 document.getElementById('btnSign').click();
   			}
    }
    if(entryId==null||entryId=="")
    {
    	Ext.get("approve_his").dom.style.display = 'none';
    	getFirstSteps();
    
    }
    else
    {
        getCurrentSteps();
    }
    
    //-------------------------布局-------------------
       var signPanel = new Ext.Panel({
       	id:"tab1",
        layout : "column",
        autoScroll : true,
        title : '签字审批', 
//        height : 600,
        border : true,
        items : [{
                    columnWidth : 0.1,
                    border : false
                }, form, {
                    columnWidth : 0.1,
                    border : false
                }],
        defaults : {
            labelAlign : 'center'    
        }
    });

    // 票面浏览panel
      var tab4Href=getReportUrl(workticketType,strBusiNo,fireLevelId);   //"/powerrpt/report/webfile/ElectricOne.jsp?no="+strBusiNo;
   
     var tab4Html = "<iframe name='tabSafety' src='" + tab4Href + "'style='width:100%;height:100%;border:0px;'></iframe>";
    var ticketPanel = new Ext.Panel({
                title : "票面浏览",
                id:'tab4',
                html : tab4Html
            });
  
  
   //----------安措信息控制-------------------------------------   
     var showSafetyTyep="";
     var ifdosafety="";
     if(workticketType!="4")
     {
			     if(busiStatus=="2"||busiStatus=="3")
			     {
			     	//已上报和已签发
			     	showSafetyTyep="N";
			     }
			     else if(busiStatus=="17"||busiStatus=="18"||busiStatus=="4")
			     {
			     	//4已接票17点检已审核签发18值长已审核
			     	showSafetyTyep="Y";
			     }
			     else if(busiStatus=="5")
			     {
			     	//5已批准 即安措办理
			     	showSafetyTyep="Y";
			     	ifdosafety=true;
			     }
			     else
			     {
			     	showSafetyTyep="";
			     }
     
     }
     else
     {
       //动火票	
     	 if(busiStatus=="2") showSafetyTyep="repair"; 	//签发时可修改检修安措
     	else if(busiStatus=="3") showSafetyTyep="run"; //许可人填写运行时安措
     	 else if(busiStatus=="23") {showSafetyTyep="repair"; ifdosafety=true;} //工作负责人办理检修安措
     	else if(busiStatus=="24") {showSafetyTyep="run"; ifdosafety=true;} //工作许可人办理运行安措
     	else { showSafetyTyep="";}
     }
 //---------------------------------------------------------------------------------------------------------------- 
     var tab2Href="run/bqworkticket/business/approve/safety/workticketSafety.jsp?workticketNo="+strBusiNo+"&workticketType="+workticketType+"&showSafetyTyep="+showSafetyTyep+"&ifdosafety="+ifdosafety;
     var tab2Html = "<iframe name='tabSafety' src='" + tab2Href + "'style='width:100%;height:100%;border:0px;'></iframe>";
    
    //危险点
     var tab3Href="run/bqworkticket/business/approve/danger/danger.jsp?workticketNo="+strBusiNo;
       var tab3Html = "<iframe name='tabDanger' src='" + tab3Href + "'style='width:100%;height:100%;border:0px;'></iframe>";
       
    // tabpanel
    var tabPanel;
    //-------------tabPanel------------------------------------
//    if(workticketType!="4")
//    {
    	//非动火票有危险点
		    tabPanel= new Ext.TabPanel({
		        activeTab : 0,
		        layoutOnTabChange : true,
		        border : false,
		        items : [signPanel, {
		            title : '安措信息', 
		            id:'tab2',
		            html : tab2Html
		        },{
		            title : '危险点信息', 
		            id:'tab3',
		            html : tab3Html
		        }, ticketPanel]
		    })
		  
		
   
//    }
//    else
//    {
//    	//动火票无危险点
//    	   tabPanel= new Ext.TabPanel({
//		        activeTab : 0,
//		        layoutOnTabChange : true,
//		        border : false,
//		        items : [signPanel, {
//		            title : '安措信息', 
//		            id:'tab2',
//		            html : tab2Html
//		        },ticketPanel]
//		    })
//    }
   
       tabPanel.on("tabchange",function(){
		   	if(tabPanel.getActiveTab().getId()=="tab3")
		   	{
		   		ifSeeDanger=true;
		   	}
		   });
   //-----------------------------------------------


    new Ext.Viewport({
                enableTabScroll : true,
                layout : "fit",
                border : false,
                items : [{
                            layout : 'fit',
                            border : false,
                            margins : '0 0 0 0',
                            region : 'center',
                            autoScroll : true,
                            items : [tabPanel]
                        }]
            });
            
 
    
});
