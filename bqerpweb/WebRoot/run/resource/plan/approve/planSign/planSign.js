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
    function removeAppointNextRoles(){
	if(confirm("确定要清除指定下一步人吗？")){
		nrs = "";
		Ext.get("showAppointNextRoles").dom.innerHTML = "";
	}
}
Ext.onReady(function() {
	// 05/08/09 15:06 标记是否响应回车 yiliu
	var flagEnter = true;
    var arg = window.dialogArguments
    
    var strBusiNo = arg.busiNo;
    var entryId =arg.entryId;
    var flowCode=arg.flowCode;
    var mytitle="";     //arg.title;
    var actionId="";
    var eventIdentify="";
     var workerCode="";
     var nrs = "";
     var busiStatus=arg.busiStatus;//add 
    var ifSeeDanger=false;
    var actionType=arg.actionType;
    var planId=arg.id;
    // add by sychen 20100511
    var mrDept=arg.mrDept;
    var flag=arg.flag;
    
  
  
   
    
    // ↓↓*******************工作票审批**************************************
    

    
    var label = new Ext.form.Label({
        id : "label",
        text : mytitle,
         style : "color:blue;font-size:25px;line-height:50px;padding-left:150px;font-weight:bold"
    }); 
    
  
			
	function showPrintWindow() { 
			var strReportAdds = getReportUrl(workticketType,strBusiNo,fireLevelId);
			if (strReportAdds != "") {
				window.open(strReportAdds);
			} else {
				Ext.Msg.alert("目前没有该种工作票票面预览");
			}
		

	}	
   
    
   
    
    // 标题
    var titleField = new Ext.Panel({
        border : false,
        layout:'form',
        height : 60,
        items : [label],
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
			 url = application_base_path+"workflow/manager/flowshow/flowshow.jsp?flowCode="+flowCode;
        	}
        	else
        	{
        		url = application_base_path + "workflow/manager/show/show.jsp?entryId="+entryId;
        	}
        	window.open(url);
		}
    });
    // 查看审批记录
    var approveHistoryButton = new Ext.Button({
        text : "查看审批记录",
        handler : function() {
			var url = application_base_path+"workflow/manager/approveInfo/approveInfo.jsp?entryId="
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
			url = application_base_path+ "workflow/manager/appointNextRole/appointNextRoleForReport.jsp";
		}
		else
		{
		 url = application_base_path + "workflow/manager/appointNextRole/appointNextRole.jsp";
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
        style : "padding-top:10;padding-bottom:10;border-width:1px 0 0 0;",
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
		
			// radio = radio ||
		// [{actionId:1,actionName:'上报'},{actionId:2,actionName:'紧急上报'}];
		if (radio instanceof Array) {
			if (!radioSet.items) {
				radioSet.items = new Ext.util.MixedCollection();
			}
			if (radio.length > 0) {
				var _radio = new Ext.form.Radio({
					boxLabel : "<font size='2.6px'>" + radio[0].actionName
							+ "</font>",
					id : 'approve-radio' + radio[0].actionId,
					name : 'rb-approve',
					inputValue : radio[0].actionId,
					url : radio[0].url,
					eventIdentify : radio[0].changeBusiStateTo,
					listeners : {
						'check' : function() {
							var _actionId = Ext.ComponentMgr
									.get('approve-radio' + radio[0].actionId)
									.getGroupValue();
							var obj = Ext.getCmp('approve-radio' + _actionId);
							actionId = _actionId;
							eventIdentify = obj.eventIdentify;
					        //approveText.setValue("同意!");
					        
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
						id : 'approve-radio' + radio[i].actionId,
						name : 'rb-approve',
						url : radio[i].url,
						eventIdentify : radio[i].changeBusiStateTo,
						inputValue : radio[i].actionId,
						// addBy liuyingwen 2009-7-21
						listeners: {check: function(radio,checked) {
				                if (checked) {
						           // approveText.setValue("退回!");
					            } 
			                }
			            }
					});

					radioSet.items.add(_radio);
					// 添加隐藏域，为了使radio正常显示
					radioSet.items.add(new Ext.form.Hidden());
				}
			}

		}
		radioSet.doLayout();
	}

	
	 //----------费用来源-------add by fyyang 20100325------------------------
    	function choseItem() {
    // add by sychen 20100511
        var args = new Object();
        args.mrDept=mrDept;
        args.flag=flag;
    // add end --------------//
		var item = window
				.showModalDialog(
						'../../../../../comm/jsp/item/budget/budget.jsp',
//						null,//update by sychen 20100511
						args,
						'dialogWidth:560px;dialogHeight:440px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (typeof(item) != "undefined") {
			txtMrItem.setValue(item.itemName);
			txtMrItemH.setValue(item.itemCode);
		}
	}

	// 费用来源
	var txtMrItem = new Ext.form.TriggerField({
		fieldLabel : '费用来源<font color ="red">*</font>',
		displayField : 'text',
		valueField : 'id',
		blankText : '请选择',
		emptyText : '请选择',
		anchor : '50%',
		name : "itemName",
		value : arg.itemName,
		allowBlank : false,
		readOnly : true,
		hidden : false,
		height : 40,
	//	width:100,
		hideLabel : false
	});
	txtMrItem.onTriggerClick = choseItem;
	var txtMrItemH = new Ext.form.Hidden({
		name : 'mr.itemCode',
		value : arg.itemCode
	})
    //--------------------------------------------------
    // *****************************备注******************
    // 备注label
    var backLabel = new Ext.form.Label({
        text : " 审批意见：",
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
        allowBlank:false,
      //  value : "同意！",
        height : 90
    })
    // 第三行
    var remarkSet = new Ext.Panel({
        height : 120,
        border : false,
        style : "padding-top:10;border-width:1px 0 0 0;",
        items : [
        {
					columnWidth : 0.5,
					layout : "form",
					border : false,
					items : [txtMrItem]
				}
        ,backLabel, approveText]
    });
    
  


    var form = new Ext.FormPanel({
		columnWidth : 0.8,
		border : true,
		labelAlign : 'center',
		buttonAlign : 'right',
		buttons : [{
			text : "确定",
			handler : function() {
				if(approveText.getValue()=="")
				{
                   Ext.Msg.alert("提示","请填写审批意见！");
                   return;
				}
				// 显示用户验证窗口
				
				if (document.all.iframe1 != null) {
					if (iframe1.checkIsModify() == false) {
						Ext.Msg.confirm("提示", "物料详细信息已修改尚未保存，是否不进行保存继续签字审批？",
								function(buttonobj) {
									if (buttonobj == "yes") {
										workerPanel.getForm().reset();
					                    validateWin.show();
									}
								});
					}
					else
					{
						workerPanel.getForm().reset();
					    validateWin.show();
					}
				} else {

					workerPanel.getForm().reset();
					validateWin.show();
				}

			}
		}, {
			text : "取消",
			handler : function() {
				// layout.hidden = true;
				window.close();
			}
		}],
		items : [titleField, tableField, showAppointNextRoles, approveSet,
				remarkSet]
	});
  


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
			url : 'resource/planReqCommSign.action',
			waitMsg : '正在保存数据...',
			method : 'post',
			params : {
				mrNo : strBusiNo,
				workerCode : workCode.getValue(),
				//workflowNo:entryId,
				nextRoles : nrs,
				actionId:actionId,
				eventIdentify:eventIdentify,
				responseDate:datePicker.getValue(),
				approveText:approveText.getValue(),
				itemCode:txtMrItemH.getValue()
			},
			success : function(form,action) { 
				var o = eval("(" + action.response.responseText + ")"); 
				Ext.Msg.alert("提示", o.msg, function() {
					if(o.msg.indexOf('成功')!=-1)
					{
						flagEnter = true;
						window.returnValue=true;
					    window.close();
					
					}
					flagEnter = true;
					validateWin.hide();
				});

			},
			faliue : function() {
				flagEnter = true;
				Ext.Msg.alert("提示","未知错误");
			}

		}); 
                        //------------	
    
                        } else {
                        	flagEnter = true;
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
 	
 	
 	var jsonArgs = "";
 	if(actionType!="")
 	{
 		if (actionType == 'FD') {
				jsonArgs = "{'isFD':true,'isJX':false,'isSY':false}";
			} else if (actionType == 'JX') {
				jsonArgs = "{'isFD':false,'isJX':true,'isSY':false}";
			} else if (actionType == 'SY') {
				jsonArgs = "{'isFD':false,'isJX':false,'isSY':true}";
			}
 	}
 	
		Ext.Ajax.request({
			url : "MAINTWorkflow.do?action=getCurrentSteps",
			method : 'post',
			params : {
				entryId : entryId,
				workerCode : workerCode,
				jsonArgs : jsonArgs
			},
			success : function(result, request) {
				var radio = eval('(' + result.responseText + ')'); 
				radioAddHandler(radio[0].actions);
				label.setText(radio[0].stepName);
			},
			failure : function() {
				
			}
		});
	};
	
   
    
    //签字时的Enter
    document.onkeydown=function() {  
          if (flagEnter && event.keyCode==13) {  
          	     flagEnter = false;
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
        autoHeight : false,
        title : '签字审批', 
        height : 600,
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
    
     var tab2Href="run/resource/plan/approve/planDetailList/mrpDetailList.jsp?id="+planId;
     var tab2Html = "<iframe name='iframe1' src='" + tab2Href + "'style='width:100%;height:100%;border:0px;'></iframe>";
   
    // tabpanel
    var tabPanel= new Ext.TabPanel({
		        activeTab : 0,
		        layoutOnTabChange : true,
		        border : false,
		        items : [signPanel
		        // modified by liuyi 091111 物料详细信息不再tab页中显示
//		        , {
//		            title : '物料详细信息', 
//		            id:'tab2',
//		            html : tab2Html
//		        }
		        ]
		    })
		  
		
 
  
   
//       tabPanel.on("tabchange",function(){
//		
//		 if(tabPanel.getActiveTab().getId()=="tab2")
//		   	{
//		   		 
//		   	}
//		   });
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
