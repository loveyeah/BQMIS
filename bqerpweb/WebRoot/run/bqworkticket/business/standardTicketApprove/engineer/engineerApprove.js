Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

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
Ext.form.Label.prototype.setText = function(argText) {
	this.el.dom.innerHTML = argText;
}

Ext.onReady(function() {
var actionId="";
var workeCode="";
    // ↓↓*****************签字画面*****************↓↓//

    var arg = window.dialogArguments
    var strWorkticketNo = arg.workticketNo;
    var entryId = arg.workFlowNo;
    var strWorkticketType = arg.workticketTypeCode;
    // 取得票面打印JSP地址
      var firelevelId=arg.firelevelId;
    var strReportAdds=getReportUrl(strWorkticketType,strWorkticketNo,firelevelId);

    var showAppointNextRoles = new Ext.form.Label({
		border : false,
		id:'showAppointNextRoles',
		style : "color:red;font-size:12px"
	});

    
	Ext.Ajax.request({
			url : "MAINTWorkflow.do?action=getCurrentSteps",
			method : 'post',
			params : {
				entryId : entryId,
				workerCode : workeCode
			},
			success : function(result, request) {
				var radio = eval('(' + result.responseText + ')'); 
				radioAddHandler(radio[0].actions);
			},
			failure : function() {
				
			}
		});

    // 创建"是否退票"RADIO
		function radioAddHandler(radio) {
		
		if (radio instanceof Array) {
			if (!topField.items) {
				topField.items = new Ext.util.MixedCollection();
			}
			if (radio.length > 0) {
				var _radio = new Ext.form.Radio({
					boxLabel : "<font size='2.6px'>" + radio[0].actionName
							+ "</font>",
					id : 'approve-radio'+radio[0].actionId,
					name : 'actionId',
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
				topField.items.add(_radio); 
				// 添加隐藏域，为了使radio正常显示
				topField.items.add(new Ext.form.Hidden());

				for (var i = 1; i < radio.length; i++) {
					// 添加隐藏域，为了使radio正常显示
					topField.items.add(new Ext.form.Hidden());
					var _radio = new Ext.form.Radio({
						boxLabel : "<font size='2.6px'>" + radio[i].actionName
								+ "</font>",
						id : 'approve-radio'+radio[i].actionId,
						name : 'actionId', 
						url:  radio[i].url,
						eventIdentify: radio[i].changeBusiStateTo,
						inputValue : radio[i].actionId
					});
					  
					topField.items.add(_radio);
					// 添加隐藏域，为了使radio正常显示
					topField.items.add(new Ext.form.Hidden());
				}
			}

		}
		topField.doLayout();
	}
//    function radioAddHandler(radio) {
//        if (radio instanceof Array) {
//            if (!topField.items) {
//                topField.items = new Ext.util.MixedCollection();
//            }
//            topField.items.add(new Ext.form.Radio({
//                        boxLabel : '<font size = "2px">' + radio[0].actionName
//                                + '</font>',
//                        id : 'approve-radio',
//                        name : 'actionId',
//                        inputValue : radio[0].actionId
//                    }));
//            topField.items.add(new Ext.form.Hidden());
//            
//            for (var i = 1; i < radio.length; i++) {
//                topField.items.add(new Ext.form.Hidden());
//                topField.items.add(new Ext.form.Radio({
//                            boxLabel : '<font size = "2px">'
//                                    + radio[i].actionName + '</font>',
//                            name : 'actionId',
//                            inputValue : radio[i].actionId
//                        }));
//                topField.items.add(new Ext.form.Hidden());
//            }
//        }
//        if (topField.items.getCount() > 0) {
//            topField.items.itemAt(0).setValue(true);
//        }
//        topField.doLayout();
//    }

    // 工作票接收人签字label
    var titleLabel = new Ext.form.Label({
        height : 20,
        id : "label",
        text : "总工程师审批",
        style : "font-size:30px;color:blue;line-height:100px;padding-left:155px"
    });

    // 审批方式label
    var approveLabel = new Ext.form.Label({
                text : " 审批方式： ",
                style : 'font-size:12px'
            })

    // 第一行
    var topField = new Ext.Panel({
                height : 20,
                border : false,
                isFormField : true,
                layout : "column"
            })

    var topPanel = new Ext.Panel({
                height : 60,
                border : false,
                style : "padding-top:10;border-width:1px 0 0 0;",
                items : [approveLabel, topField]
            })

    // 时间选择
    var dateFld = new Ext.form.TextField({
                id : 'responseDate',
                style : 'cursor:pointer',
                autoHeight : true,
                hidden : true,
                value : getDate(),
                startDate : '%y-%M-%d 00:00:00',
                listeners : {
                    focus : function() {
                        WdatePicker({
                                    startDate : '%y-%M-01 00:00:00',
                                    dateFmt : 'yyyy-MM-dd HH:mm:ss',
                                    alwaysUseStartDate : true
                                });
                    }
                }
            })

    // 设置相应时间 checkbox
    var timeChkBox = new Ext.form.Checkbox({
                boxLabel : '设置响应时间',
                listeners : {
                    check : function(box, checked) {
                        if (checked) {
                            Ext.getCmp('responseDate').setVisible(true);
                            Ext.getCmp('midField').doLayout();
                        } else {
                            Ext.getCmp('responseDate').setVisible(false);
                            Ext.getCmp('midField').doLayout();
                        }
                    }
                }
            })

    // 图形展示 按钮
    var picShowBtn = new Ext.Button({
                text : '图形展示',
                handler : function(){  
					var url = "/power/workflow/manager/show/show.jsp?entryId="+entryId;
					window.open(url);
				}
            })

    // 查看审批记录 按钮
    var showRecordBtn = new Ext.Button({
                text : '查看审批记录',
                handler : function() {
			var url = "/power/workflow/manager/approveInfo/approveInfo.jsp?entryId="
					+ entryId;
			window.showModalDialog(
							url,
							null,
							"dialogWidth:650px;dialogHeight:400px;;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;");
		}
            })

    // 显示下步角色 按钮
    var showNextBtn = new Ext.Button({
                text : '显示下步角色',
				handler : getNextSteps
            })
function getNextSteps() {
		
		var args = new Object();
		args.actionId = actionId;
		args.entryId = entryId;
		var url = "/power/workflow/manager/appointNextRole/appointNextRole.jsp"; 
		var ro = window
				.showModalDialog(
						url,
						args,
						"dialogWidth:400px;dialogHeight:350px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;");
		if (typeof(ro) != "undefined") {
			if (ro) {
				nrs = ro.nrs;
				showAppointNextRoles.setText("<span style=\"cursor:hand;color:blue\"  onclick=\"removeAppointNextRoles();\">[取消指定]</span>  您指定下一步角色为：[ " + ro.nrsname + " ]");
			}
		} 
	};
    // 第二行
    var midField = new Ext.Panel({
                Height : 70,
                autoWidth : true,
                id : 'midField',
                layout : 'column',
                border : false,
                anchor : '100%',
                style : "padding-top:20;padding-bottom:20;border-width:1px 0 0 0;",
                items : [{
                            columnWidth : 0.185,
                            layout : "form",
                            hideLabels : true,
                            border : false,
                            items : [timeChkBox]
                        }, {
                            columnWidth : 0.265,
                            hideLabels : true,
                            autoHeight : true,
                            layout : "form",
                            border : false,
                            id : 'datepickerPanel',
                            items : [dateFld]
                        }, {
                            columnWidth : 0.02,
                            border : false
                        }, {
                            columnWidth : 0.145,
                            layout : "form",
                            hideLabels : true,
                            border : false,
                            items : [picShowBtn]
                        }, {
                            columnWidth : 0.19,
                            layout : "form",
                            hideLabels : true,
                            border : false,
                            items : [showRecordBtn]
                        }, {
                            columnWidth : 0.19,
                            layout : "form",
                            hideLabels : true,
                            border : false,
                            items : [showNextBtn]
                        }]
            })

    // 备注
    var note = new Ext.form.TextArea({
                width : '100%',
                name:'approveText',
                autoScroll : true,
                isFormField : true,
                border : false,
                value : Constants.INPUT_COMMENT,
                height : 110
            })

    // 备注label
    var backLabel = new Ext.form.Label({
                text : " 备注：",
                style : 'font-size:12px;padding-top:10'
            });

    // 第三行
    var remarkSet = new Ext.Panel({
                autoHeight : true,
                autoScroll : false,
                border : false,
                style : "padding-top:10;border-width:1px 0 0 0;",
                items : [backLabel, note]
            });

    // 接收人签字panel
    var form = new Ext.FormPanel({
                columnWidth : 0.7,
                border : true,
                buttonAlign : 'right',
                items : [titleLabel, topPanel,showAppointNextRoles, midField, remarkSet],
                buttons : [{
                            text : Constants.CONFIRM,
                            handler : receive
                        }, {
                            text : Constants.BTN_CANCEL,
                            handler : function() {
                            	window.close();
                            }
                        }]
            })

    var receivePanel = new Ext.Panel({
                title : "总工程师审批",
                layout : "column",
                height : 600,
                border : true,
                items : [{
                            columnWidth : 0.15,
                            border : false
                        }, form, {
                            columnWidth : 0.15,
                            border : false
                        }]
            })

    // 票面浏览panel
    var ticketPanel = new Ext.Panel({
                title : "票面浏览",
                html : "<iframe name='tabContent' src='"+strReportAdds+"' style='width:100%;height:100%;border:0px;'></iframe>"
            });

    var params = "?workticketNo=" + strWorkticketNo + "&workticketType" + "=" + strWorkticketType + "&canUpdate=true";
   
    
    //危险点
     var tab3Href="run/bqworkticket/business/standardTicketApprove/danger/danger.jsp?workticketNo="+strWorkticketNo;
       var tab3Html = "<iframe name='tabDanger' src='" + tab3Href + "'style='width:100%;height:100%;border:0px;'></iframe>";
       
    // tabpanel
    var tabPanel = new Ext.TabPanel({
        activeTab : 0,
        layoutOnTabChange : true,
        border : false,
        items : [receivePanel, {
            title : '安措信息', //modify by fyyang 090105 安措页面由approve\safetyMeaure.jsp改为receiveSafetyInfo.jsp
            html : "<iframe name='safetyInfo' src='run/bqworkticket/business/standardTicketApprove/safety/safetyInfo.jsp"+ params + "' style='width:100%;height:100%;border:0px;'></iframe>"
        },{
            title : '危险点信息', //modify by fyyang 090105 安措页面由approve\safetyMeaure.jsp改为receiveSafetyInfo.jsp
            html : tab3Html
        }, ticketPanel]
    })

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
    // ↑↑*****************接收人签字画面*****************↑↑//

    // ↓↓*****************员工确认画面*****************↓↓//
    // 工号
    var workerCode = new Ext.form.TextField({
                id : 'workerCode',
                frame : true,
                fieldLabel : '工号<font color ="red">*</font>',
                allowBlank : false,
                width : 110
            })

    // 密码
    var workerPwd = new Ext.form.TextField({
                id : 'workerPwd',
                frame : true,
                fieldLabel : '密码<font color ="red">*</font>',
                allowBlank : false,
                inputType : "password",
                width : 110
            })

    // 弹出窗口panel
    var popFormPanel = new Ext.FormPanel({
                frame : true,
                autoHeight : true,
                autoWidth : true,
                labelAlign : 'right',
                layout : 'form',
                buttonAlign : 'center',
                items : [workerCode, workerPwd],
                buttons : [{
                            text : Constants.BTN_SAVE,
                            id:'btnSign',
                            handler : save
                        }, {
                            text : Constants.BTN_CANCEL,
                            handler : function() {
                            	workerPwd.setValue("");
                                popWin.hide();
                            }
                        }]
            })

    // 输入工号弹出画面
    var popWin = new Ext.Window({
                resizable : false,
                title : '请输入工号与密码',
                autoHeight : true,
                width : 300,
                modal:true,
                closeAction : 'hide',
                items : [popFormPanel]
            })

    // ↑↑*****************员工确认画面*****************↑↑//

    // 主处理

    /** 获取工号 */
    function receive() {
      workerCode.setValue(workeCode);
                        popWin.show();
    }

    /** 工号确认 */
    function save() {
        Ext.lib.Ajax.request('POST',
                'comm/workticketApproveCheckUser.action', {
                    success : function(action) {
                        var result = eval(action.responseText);
                        if (result) {
                        
                        	form.getForm().submit({
								url : 'workticket/StandardEngineerApprove.action',
								waitMsg : '正在保存数据...',
								method : 'post',
								params : {
									workticketNo : strWorkticketNo,
									workerCode : workerCode.getValue(),
									nextRoles : nrs
								},
								success : function(result, request) {
									Ext.Msg.alert(Constants.APPROVE,
											Constants.APPROVE_SUCCESS,
											function() {
												window.close();
												popWin.hide();
											});

								},
								failure : function() {
									Ext.Msg.alert(Constants.ERROR,
											Constants.OPERATE_ERROR_MSG);
								}

							});

                        } else {
                            Ext.Msg.alert(Constants.SYS_REMIND_MSG,
                                    Constants.USER_CHECK_ERROR);
                        }
                    }
                }, "workerCode=" + workerCode.getValue() + "&loginPwd="
                        + workerPwd.getValue());

    }
    
    
    //add by fyyang 090106
    //签字时的Enter
    document.onkeydown=function() {  
          if (event.keyCode==13) {  
         		 document.getElementById('btnSign').click();
   			}
    }
    
    function getWorkCode()
    {
     Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
                    success : function(action) {
                    	 var result = eval("(" + action.responseText + ")");
                        if (result.workerCode) {
                            // 设定默认工号
                        	workeCode=result.workerCode;
                          
                        }
                    }
                });
    }
    getWorkCode();
})
