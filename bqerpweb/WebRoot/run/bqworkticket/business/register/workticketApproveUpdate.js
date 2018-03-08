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
Ext.onReady(function() {
		var register = parent.Ext.getCmp('tabPanel').register;
   // var arg = window.dialogArguments
    
    var strBusiNo =register.workticketNo;
    var workticketType=register.workticketTypeCode;
    var entryId;
    var flowCode;
    var mytitle="审批信息";
    var actionId="";
    var eventIdentify="";
     var workerCode="";
     var nrs = "";
     var ifSeeDanger=false;
       var fireLevelId;
    // ↓↓*******************停送电申请单上报页面**************************************
    

    //停送电申请单上报label
    var label = new Ext.form.Label({
        id : "label",
        text : mytitle,
         style : "color:blue;font-size:30px;line-height:80px;padding-left:250px;font-weight:bold"
    });
    // 标题
    var titleField = new Ext.Panel({
        border : false,
        height : 60,
        items : [label]
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
 


    // *****************************备注******************

    
 //安措办理
       var safetyExeLabel = new Ext.form.Label({
        text : " 安措办理：",
        style : 'color:red'
    });
    var safetyExeId=new Ext.form.Hidden({
     fieldLabel : '安措办理ID',
		name : 'safetyExeHisModel.id',
		id : 'safetyExeId'
    });
    
     var safetyExePermitBy = {
		fieldLabel : '工作许可人',
		name : 'safetyExePermitBy',
		xtype : 'combo',
		id : 'safetyExePermitBy',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
	    hiddenName : 'safetyExeHisModel.oldChargeBy',
		editable : false,
		anchor : "80%",
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
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
				Ext.getCmp('safetyExePermitBy').setValue(emp.workerCode);
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('safetyExePermitBy'), emp.workerName);
			}
		}
	};
	
	  var safetyExeDutyBy = {
		fieldLabel : '值班负责人',
		name : 'safetyExeDutyBy',
		xtype : 'combo',
		id : 'safetyExeDutyBy',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
	    hiddenName : 'safetyExeHisModel.dutyChargeBy',
		editable : false,
		anchor : "80%",
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
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
				Ext.getCmp('safetyExeDutyBy').setValue(emp.workerCode);
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('safetyExeDutyBy'), emp.workerName);
			}
		}
	};
	
	var safetyExePanel = new Ext.Panel({
       // height : 40,
		id:'safetyExePanel',
        border : false,
        style : "padding-top:10;border-width:1px 0 0 0;",
        layout : "column",
        labelWidth:80,
        labelAlign:"right",
        items : [{
					columnWidth : 1,
					layout : "form",
					border : false,
					items : [safetyExeLabel]
				},{
					columnWidth : 0.5,
					layout : "form",
					border : false,
					items : [safetyExePermitBy,safetyExeId]
				},{
					columnWidth : 0.5,
					layout : "form",
					border : false,
					items : [safetyExeDutyBy]
				}]
    });
 //-------------许可开工---------
      var permitLabel = new Ext.form.Label({
        text : " 许可开工：",
        style : 'color:red'
    });
   var permitId=new Ext.form.Hidden({
     fieldLabel : '许可开工ID',
		name : 'pemitHisModel.id',
		id : 'permitId'
    });
    //许可开工
    
       var permitBy = {
		fieldLabel : '许可人',
		name : 'permitBy',
		xtype : 'combo',
		id : 'permitBy',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
	    hiddenName : 'pemitHisModel.approveBy',
		editable : false,
		anchor : "80%",
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
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
				Ext.getCmp('permitBy').setValue(emp.workerCode);
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('permitBy'), emp.workerName);
			}
		}
	};
	    
    var permitChargeBy = {
		fieldLabel : '工作负责人',
		name : 'permitChargeBy',
		xtype : 'combo',
		id : 'permitChargeBy',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		 hiddenName : 'pemitHisModel.oldChargeBy',
		editable : false,
		anchor : "80%",
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
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
				Ext.getCmp('permitChargeBy').setValue(emp.workerCode);
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('permitChargeBy'), emp.workerName);
			}
		}
	};
    
    var permitDutyBy = {
		fieldLabel : '值班负责人',
		name : 'permitDutyBy',
		xtype : 'combo',
		id : 'permitDutyBy',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		 hiddenName : 'pemitHisModel.dutyChargeBy',
		editable : false,
		anchor : "80%",
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
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
				Ext.getCmp('permitDutyBy').setValue(emp.workerCode);
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('permitDutyBy'), emp.workerName);
			}
		}
	};
	
	
    
    
       var permitDate = new Ext.form.TextField({
        id : 'permitDate',
        fieldLabel : "许可开工时间",
        name : 'pemitHisModel.oldApprovedFinishDate',
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
    })

    	var permitPanel = new Ext.Panel({
       // height : 40,
        border : false,
        style : "padding-top:10;border-width:1px 0 0 0;",
        layout : "column",
        labelWidth:80,
        labelAlign:"right",
        items : [{
					columnWidth : 1,
					layout : "form",
					border : false,
					items : [permitLabel]
				},{
					columnWidth : 0.5,
					layout : "form",
					border : false,
					items : [permitBy,permitChargeBy,permitId]
				},{
					columnWidth : 0.5,
					layout : "form",
					border : false,
					items : [permitDate,permitDutyBy]
				}]
    });
 //----------------------   
//-----------工作负责人变更--------
//        var changeLabel = new Ext.form.Label({
//        text : " 工作负责人变更：",
//        style : 'color:red'
//    });
    
       var changeId=new Ext.form.Hidden({
     fieldLabel : '变更工作负责人ID',
		name : 'changeChargeModel.id',
		id : 'changeId'
    });
     var changeLabel = new Ext.form.Checkbox({
                id : 'changeLabel',
                fieldLabel : '<font color="red" size=3>工作负责人变更</font>',
                listeners : {
                    check : function() {
                        // 选中状态
                        if (changeLabel.checked) {
                          
                           Ext.get("oldChargeBy").dom.parentNode.parentNode.parentNode.style.display = '';
                            Ext.get("changeSignBy").dom.parentNode.parentNode.parentNode.style.display = '';
                             Ext.get("changeDate").dom.parentNode.parentNode.parentNode.style.display = '';
                           Ext.get("newChargeBy").dom.parentNode.parentNode.parentNode.style.display = '';
                             Ext.get("changeDutyBy").dom.parentNode.parentNode.parentNode.style.display = '';
                            // 未选中状态
                        } else {
                        	  Ext.get("oldChargeBy").dom.parentNode.parentNode.parentNode.style.display = 'none';
                            Ext.get("changeSignBy").dom.parentNode.parentNode.parentNode.style.display = 'none';
                             Ext.get("changeDate").dom.parentNode.parentNode.parentNode.style.display = 'none';
                           Ext.get("newChargeBy").dom.parentNode.parentNode.parentNode.style.display = 'none';
                             Ext.get("changeDutyBy").dom.parentNode.parentNode.parentNode.style.display = 'none';
                        }
                    }
                }
            })
    
     var oldChargeBy = {
		fieldLabel : '原工作负责人',
		name : 'oldChargeBy',
		xtype : 'combo',
		id : 'oldChargeBy',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		 hiddenName : 'changeChargeModel.oldChargeBy',
		editable : false,
		anchor : "80%",
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
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
				Ext.getCmp('oldChargeBy').setValue(emp.workerCode);
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('oldChargeBy'), emp.workerName);
			}
		}
	};
	
	 var newChargeBy = {
		fieldLabel : '现工作负责人',
		name : 'newChargeBy',
		xtype : 'combo',
		id : 'newChargeBy',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		 hiddenName : 'changeChargeModel.newChargeBy',
		editable : false,
		anchor : "80%",
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
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
				Ext.getCmp('newChargeBy').setValue(emp.workerCode);
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('newChargeBy'), emp.workerName);
			}
		}
	};
	
  var changeSignBy = {
		fieldLabel : '签发人',
		name : 'changeSignBy',
		xtype : 'combo',
		id : 'changeSignBy',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
	    hiddenName : 'changeChargeModel.approveBy',
		editable : false,
		anchor : "80%",
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
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
				Ext.getCmp('changeSignBy').setValue(emp.workerCode);
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('changeSignBy'), emp.workerName);
			}
		}
	};
   
	var changeDutyBy = {
		fieldLabel : '值班负责人',
		name : 'changeDutyBy',
		xtype : 'combo',
		id : 'changeDutyBy',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		 hiddenName : 'changeChargeModel.dutyChargeBy',
		editable : false,
		anchor : "80%",
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
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
				Ext.getCmp('changeDutyBy').setValue(emp.workerCode);
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('changeDutyBy'), emp.workerName);
			}
		}
	};
    var changeDate = new Ext.form.TextField({
        id : 'changeDate',
        fieldLabel : "变更时间",
        name : 'changeChargeModel.oldApprovedFinishDate',
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
    })
    
   
    
    	var changePanel = new Ext.Panel({
       // height : 40,
    		id:'changePanel',
        border : false,
        style : "padding-top:10;border-width:1px 0 0 0;",
        layout : "column",
        labelWidth:80,
        labelAlign:"right",
        items : [{
					columnWidth : 1,
					layout : "form",
					border : false,
					 labelWidth:130,
					items : [changeLabel]
				},{
					columnWidth : 0.5,
					layout : "form",
					border : false,
					items : [oldChargeBy,changeSignBy,changeDate]
				},{
					columnWidth : 0.5,
					layout : "form",
					border : false,
					items : [newChargeBy,changeDutyBy,changeId]
				}]
    });
//------------------------------    
//-----工作票延期----------
//       var delayLabel = new Ext.form.Label({
//        text : " 工作票延期：",
//        style : 'color:red'
//    });
       var delayId=new Ext.form.Hidden({
     fieldLabel : '工作票延期ID',
		name : 'delayHisModel.id',
		id : 'delayId'
    });
    
        var delayLabel = new Ext.form.Checkbox({
                id : 'delayLabel',
                fieldLabel : '<font color="red" size=3>工作票延期</font>',
                listeners : {
                    check : function() {
                        // 选中状态
                        if (delayLabel.checked) {
                          //delayOldDate,delayChargeBy,delayDutyBy,delayToDate,delayRunBy
                           Ext.get("delayOldDate").dom.parentNode.parentNode.parentNode.style.display = '';
                            Ext.get("delayChargeBy").dom.parentNode.parentNode.parentNode.style.display = '';
                             Ext.get("delayDutyBy").dom.parentNode.parentNode.parentNode.style.display = '';
                           Ext.get("delayToDate").dom.parentNode.parentNode.parentNode.style.display = '';
                             Ext.get("delayRunBy").dom.parentNode.parentNode.parentNode.style.display = '';
                            // 未选中状态
                        } else {
                        	   Ext.get("delayOldDate").dom.parentNode.parentNode.parentNode.style.display = 'none';
                            Ext.get("delayChargeBy").dom.parentNode.parentNode.parentNode.style.display = 'none';
                             Ext.get("delayDutyBy").dom.parentNode.parentNode.parentNode.style.display = 'none';
                           Ext.get("delayToDate").dom.parentNode.parentNode.parentNode.style.display = 'none';
                             Ext.get("delayRunBy").dom.parentNode.parentNode.parentNode.style.display = 'none';
                        }
                    }
                }
            })
    
      var delayOldDate = new Ext.form.TextField({
        id : 'delayOldDate',
        fieldLabel : "原时间",
        name : 'delayHisModel.oldApprovedFinishDate',
        style : 'cursor:pointer',
        anchor : "80%"
//        listeners : {
//            focus : function() {
//                WdatePicker({
//                    startDate : '%y-%M-%d 00:00:00',
//                    dateFmt : 'yyyy-MM-dd HH:mm:ss',
//                    alwaysUseStartDate : true
//                });
//            }
//        }
    })
       var delayToDate = new Ext.form.TextField({
        id : 'delayToDate',
        fieldLabel : "延期到",
        name : 'delayHisModel.newApprovedFinishDate',
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
    })
    var delayChargeBy = {
		fieldLabel : '工作负责人',
		name : 'delayChargeBy',
		xtype : 'combo',
		id : 'delayChargeBy',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
	    hiddenName : 'delayHisModel.oldChargeBy',
		editable : false,
		anchor : "80%",
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
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
				Ext.getCmp('delayChargeBy').setValue(emp.workerCode);
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('delayChargeBy'), emp.workerName);
			}
		}
	};
	
	 var delayRunBy = {
		fieldLabel : '运行值班负责人',
		name : 'delayRunBy',
		xtype : 'combo',
		id : 'delayRunBy',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		 hiddenName : 'delayHisModel.dutyChargeBy',
		editable : false,
		anchor : "80%",
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
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
				Ext.getCmp('delayRunBy').setValue(emp.workerCode);
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('delayRunBy'), emp.workerName);
			}
		}
	};
	
	 var delayDutyBy = {
		fieldLabel : '值长',
		name : 'delayDutyBy',
		xtype : 'combo',
		id : 'delayDutyBy',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		 hiddenName : 'delayHisModel.newChargeBy',
		editable : false,
		anchor : "80%",
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
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
				Ext.getCmp('delayDutyBy').setValue(emp.workerCode);
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('delayDutyBy'), emp.workerName);
			}
		}
	};
    
		var delayPanel = new Ext.Panel({
       // height : 40,
			id:'delayPanel',
        border : false,
        style : "padding-top:10;border-width:1px 0 0 0;",
        layout : "column",
        labelWidth:100,
        labelAlign:"right",
        items : [{
					columnWidth : 1,
					layout : "form",
					border : false,
					items : [delayLabel]
				},{
					columnWidth : 0.5,
					layout : "form",
					border : false,
					items : [delayOldDate,delayChargeBy,delayDutyBy]
				},{
					columnWidth : 0.5,
					layout : "form",
					border : false,
					items : [delayToDate,delayRunBy,delayId]
				}]
    });
//--------------------------    
//---------工作终结 ------------
    
         var endId=new Ext.form.Hidden({
     fieldLabel : '工作票终结ID',
		name : 'endHisModel.id',
		id : 'endId'
    });
        var endLabel = new Ext.form.Label({
        text : " 工作票终结：",
        style : 'color:red'
        });
    
        var endDate = new Ext.form.TextField({
        id : 'endDate',
        fieldLabel : "工作结束时间",
        name : 'endHisModel.oldApprovedFinishDate',
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
		fieldLabel : '工作负责人',
		name : 'endChargeBy',
		xtype : 'combo',
		id : 'endChargeBy',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		 hiddenName : 'endHisModel.oldChargeBy',
		editable : false,
		anchor : "80%",
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
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
		fieldLabel : '点检验收人',
		name : 'endAcceptBy',
		xtype : 'combo',
		id : 'endAcceptBy',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
	   hiddenName : 'endHisModel.newChargeBy',
		editable : false,
		anchor : "80%",
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
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
		fieldLabel : '工作许可人',
		name : 'endPemitBy',
		xtype : 'combo',
		id : 'endPemitBy',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
	    hiddenName : 'endHisModel.fireBy',
		editable : false,
		anchor : "80%",
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
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
		fieldLabel : '值班负责人',
		name : 'endDutyBy',
		xtype : 'combo',
		id : 'endDutyBy',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		 hiddenName : 'endHisModel.dutyChargeBy',
		editable : false,
		anchor : "80%",
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
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
	
	  var endTotalLine  = new Ext.form.NumberField({
	  	fieldLabel : '接地线总数',
	  	id:'endTotalLine',
        anchor : "80%",
        name : 'endHisModel.totalLine',
        border : false
    });
      var endNoBackoutLine  = new Ext.form.NumberField({
	  	fieldLabel : '未拆除数',
	  	id:'endNoBackoutLine',
        anchor : "80%",
        name : 'endHisModel.nobackoutLine',
        border : false
    });
      var endNoBackoutNum  = new Ext.form.NumberField({
	  	fieldLabel : '未拆除编号',
	  	id:'endNoBackoutNum',
        anchor : "80%",
        name : 'endHisModel.nobackoutNum',
        border : false
    });
    
//      var endMemo  = new Ext.form.TextArea({
//	  	fieldLabel : '备注',
//	  	 autoScroll : true,
//        isFormField : true,
//        anchor : "90%",
//        name : 'endMemo',
//        border : false
//    });
    
    
		var endPanel = new Ext.Panel({
       // height : 40,
        border : false,
        style : "padding-top:10;border-width:1px 0 0 0;",
        layout : "column",
        labelWidth:100,
        labelAlign:"right",
        items : [{
					columnWidth : 1,
					layout : "form",
					border : false,
					items : [endLabel]
				},{
					columnWidth : 0.5,
					layout : "form",
					border : false,
					items : [endDate,endPemitBy,endTotalLine,endNoBackoutNum,endId]
				},{
					columnWidth : 0.5,
					layout : "form",
					border : false,
					items : [endChargeBy,endAcceptBy,endDutyBy,endNoBackoutLine]
				}]
    });
//--------------------------   
 //----------动火票----------------
          var dhEndId=new Ext.form.Hidden({
     fieldLabel : '动火票终结ID',
		name : 'dhModel.id',
		id : 'dhEndId'
    });
    var dhEndDate = new Ext.form.TextField({
        id : 'dhEndDate',
        fieldLabel : "工作结束时间",
        name : 'dhModel.oldApprovedFinishDate',
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
     var dhEndChargeBy = {
		fieldLabel : '动火执行人',
		name : 'dhEndChargeBy',
		xtype : 'combo',
		id : 'dhEndChargeBy',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		 hiddenName : 'dhModel.oldChargeBy',
		editable : false,
		anchor : "80%",
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
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
				Ext.getCmp('dhEndChargeBy').setValue(emp.workerCode);
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('dhEndChargeBy'), emp.workerName);
			}
		}
	};
	
	 var dhEndAcceptBy = {
		fieldLabel : '消防监护人',
		name : 'dhEndAcceptBy',
		xtype : 'combo',
		id : 'dhEndAcceptBy',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
	   hiddenName : 'dhModel.newChargeBy',
		editable : false,
		anchor : "80%",
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
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
				Ext.getCmp('dhEndAcceptBy').setValue(emp.workerCode);
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('dhEndAcceptBy'), emp.workerName);
			}
		}
	};
	var dhEndPemitBy = {
		fieldLabel : '动火执行人（许可回填）',
		name : 'dhEndPemitBy',
		xtype : 'combo',
		id : 'dhEndPemitBy',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
	    hiddenName : 'dhModel.fireBy',
		editable : false,
		anchor : "57%",
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
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
				Ext.getCmp('dhEndPemitBy').setValue(emp.workerCode);
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('dhEndPemitBy'), emp.workerName);
			}
		}
	};
	
	var dhEndDutyBy = {
		fieldLabel : '值长',
		name : 'dhEndDutyBy',
		xtype : 'combo',
		id : 'dhEndDutyBy',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		 hiddenName : 'dhModel.dutyChargeBy',
		editable : false,
		anchor : "80%",
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
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
				Ext.getCmp('dhEndDutyBy').setValue(emp.workerCode);
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('dhEndDutyBy'), emp.workerName);
			}
		}
	};
	


    
 
    	var dhPermitPanel = new Ext.Panel({
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
					items : [dhEndPemitBy]
				}]
    });
    
		var dhEndPanel = new Ext.Panel({
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
					items : [dhEndDate,dhEndDutyBy,dhEndId]
				},{
					columnWidth : 0.5,
					layout : "form",
					border : false,
					items : [dhEndChargeBy,dhEndAcceptBy]
				}]
    });
// //----------------------   
 //----------动火票-----------------   
  
   	var bbar = new Ext.Toolbar({
		height:20,
		//align:'center',
		items : [new Ext.Toolbar.Fill(), {
			id : 'btnUpdate',
			text : '修改',
			iconCls:'update',
			handler : function() {
		         
				  form.getForm().submit({
					url : 'workticket/updateApproveInfo.action',
					waitMsg : '正在保存数据...',
					method : 'post',
					params : {
						workticketNo : strBusiNo,
						workticketTypeCode:workticketType,
						changeLabelStatus:changeLabel.checked,
						delayLabelStatus:delayLabel.checked
					},
					success : function(form,action) { 
						var o = eval("(" + action.response.responseText + ")"); 
						Ext.Msg.alert("提示", o.msg);
					
		
					},
					faliue : function() {
						Ext.Msg.alert("提示","未知错误");
					}
		
				}); 
			}
		}
		]
	}) ;
    
    var form = new Ext.FormPanel({
    
	//	columnWidth : 0.8,
		border : true,
		labelAlign : 'center',
		buttonAlign : 'right',
		bbar:bbar,
		autoScroll:true, 
		items : [titleField, safetyExePanel,permitPanel, endPanel,changePanel,delayPanel,dhPermitPanel,dhEndPanel
				]
	});
  



 
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
                            items : [form]
                        }]
            });
            
  
//  //字段显隐控制------------------------   
            function typeCheck()
            {
            //动火票和非动火票
              if(workticketType=="4")
              {
              	//titleField, safetyExePanel,permitPanel, endPanel,changePanel,delayPanel,dhPermitPanel,dhEndPanel
             safetyExePanel.setVisible(false);
             permitPanel.setVisible(false);
             endPanel.setVisible(false);
             changePanel.setVisible(false);
             delayPanel.setVisible(false);
              }
              else
              {
              	dhPermitPanel.setVisible(false);
              	dhEndPanel.setVisible(false);
              }
            	
            if(workticketType=="1")
            {
            Ext.get("permitDutyBy").dom.parentNode.parentNode.parentNode.style.display = 'none';
            }
            else if(workticketType=="2")
            {
            	//电二种票
            	 Ext.get("endDutyBy").dom.parentNode.parentNode.parentNode.style.display = 'none';
            	 Ext.get("endNoBackoutLine").dom.parentNode.parentNode.style.display = 'none';
            	 Ext.get("endNoBackoutNum").dom.parentNode.parentNode.style.display = 'none';
            	 Ext.get("delayPanel").dom.style.display = 'none';
            	 Ext.get("changePanel").dom.style.display = 'none';
            	  Ext.get("safetyExePanel").dom.style.display = 'none';
            	 
            }
            else if(workticketType=="3")
            {
            	 Ext.get("endDutyBy").dom.parentNode.parentNode.parentNode.style.display = 'none';
            	 Ext.get("endNoBackoutLine").dom.parentNode.parentNode.style.display = 'none';
            	 Ext.get("endNoBackoutNum").dom.parentNode.parentNode.style.display = 'none';
            	  Ext.get("endTotalLine").dom.parentNode.parentNode.style.display = 'none';
            	   Ext.get("safetyExePanel").dom.style.display = 'none';
            }
             else if(workticketType=="5")
            {
            	 Ext.get("endDutyBy").dom.parentNode.parentNode.parentNode.style.display = 'none';
            	 Ext.get("endNoBackoutLine").dom.parentNode.parentNode.style.display = 'none';
            	 Ext.get("endNoBackoutNum").dom.parentNode.parentNode.style.display = 'none';
            	 Ext.get("endTotalLine").dom.parentNode.parentNode.style.display = 'none';
            	 Ext.get("endAcceptBy").dom.parentNode.parentNode.parentNode.style.display = 'none';
            	   Ext.get("safetyExePanel").dom.style.display = 'none';
            }
              else if(workticketType=="7")
            {
            	//热力机械二
            	Ext.get("endDutyBy").dom.parentNode.parentNode.parentNode.style.display = 'none';
            	 Ext.get("endNoBackoutLine").dom.parentNode.parentNode.style.display = 'none';
            	 Ext.get("endNoBackoutNum").dom.parentNode.parentNode.style.display = 'none';
            	 Ext.get("delayPanel").dom.style.display = 'none';
            	 Ext.get("changePanel").dom.style.display = 'none';
            	 Ext.get("endTotalLine").dom.parentNode.parentNode.style.display = 'none';
            	  Ext.get("safetyExePanel").dom.style.display = 'none';
            }
              else if(workticketType=="8")
            {
            	//热控二
            	Ext.get("endDutyBy").dom.parentNode.parentNode.parentNode.style.display = 'none';
            	 Ext.get("endNoBackoutLine").dom.parentNode.parentNode.style.display = 'none';
            	 Ext.get("endNoBackoutNum").dom.parentNode.parentNode.style.display = 'none';
            	 Ext.get("delayPanel").dom.style.display = 'none';
            	 Ext.get("changePanel").dom.style.display = 'none';
            	 Ext.get("endTotalLine").dom.parentNode.parentNode.style.display = 'none';
            	 Ext.get("endAcceptBy").dom.parentNode.parentNode.parentNode.style.display = 'none';
            	  Ext.get("safetyExePanel").dom.style.display = 'none';
            }
            else
            {
            }
            
            }
  //----------------------------------------------------------------       
            function initData() {
		       Ext.lib.Ajax.request('POST',
				'workticket/findApproveInfoForUpdate.action', {
					success : function(result, request) {
						//----------------默认值
						    Ext.lib.Ajax.request('POST',
				'bqworkticket/getWorkticketBaseInfoByNo.action', {
					success : function(result, request) {
						var record = eval('(' + result.responseText + ')');
						var chargeByCode=record.data.model.chargeBy;
						var chargeByName=record.data.chargeByName;
					//许可：工作负责人
					   Ext.getCmp('permitChargeBy').setValue(chargeByCode);
				       Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('permitChargeBy'),chargeByName);
					//工作负责人变更
						Ext.getCmp('oldChargeBy').setValue(chargeByCode);
				       Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('oldChargeBy'), chargeByName);
						//工作票延期
					   Ext.getCmp('delayChargeBy').setValue(chargeByCode);
				       Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('delayChargeBy'), chargeByName);
						if(record.data.approveEndDate!=null)
						{
						delayOldDate.setValue(record.data.approveEndDate.substring(0,19));
						}
						//终结
						Ext.getCmp('endChargeBy').setValue(chargeByCode);
				        Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('endChargeBy'), chargeByName);
						
			
					},
					failure : function() {
						Ext.Msg.alert("提示",
								"错误");
					}
				}, 'workticketNo=' + strBusiNo);
						//------------------------------
						var record = eval('(' + result.responseText + ')');
					     var isShowChangeCharge='none';
					     var isShowDelay='none';
						for(var i=0;i<record.list.length;i++)
						{
							var data=record.list[i];
							if(workticketType=="4")
							{
							 //动火票	
								if(data.model.approveStatus==28)
								{
									if(data.model.id!=null)
									{
										dhEndId.setValue(data.model.id);
									}
									if(data.strOldApprovedFinishDate!=null)
									{
								     dhEndDate.setValue(data.strOldApprovedFinishDate);	
									}
									if(data.model.oldChargeBy!=null&&data.oldChargeByName!=null)
									{
										//动火执行人
										Ext.getCmp('dhEndChargeBy').setValue(data.model.oldChargeBy);
										Ext.form.ComboBox.superclass.setValue.call(Ext
												.getCmp('dhEndChargeBy'), data.oldChargeByName);
									 	
									}
									//消防监护人
									if(data.model.newChargeBy!=null&&data.newChargeByName!=null)
									{
									Ext.getCmp('dhEndAcceptBy').setValue(data.model.newChargeBy);
									Ext.form.ComboBox.superclass.setValue.call(Ext
											.getCmp('dhEndAcceptBy'), data.newChargeByName);
									}
									//动火执行人（许可回填）
									if(data.model.fireBy!=null&&data.fireByName!=null)
									{
									Ext.getCmp('dhEndPemitBy').setValue(data.model.fireBy);
									Ext.form.ComboBox.superclass.setValue.call(Ext
											.getCmp('dhEndPemitBy'), data.fireByName);
									}
									//值长
									if(data.model.dutyChargeBy!=null&&data.dutyChargeByName!=null)
									{
									Ext.getCmp('dhEndDutyBy').setValue(data.model.dutyChargeBy);
									Ext.form.ComboBox.superclass.setValue.call(Ext
											.getCmp('dhEndDutyBy'), data.dutyChargeByName);
									}
								}
							}
							{
							//非动火票	
							if(data.model.changeStatus==2)
							{
								isShowChangeCharge='';
								if(data.model.id!=null)
								{
								 	changeId.setValue(data.model.id);
								}
								//原工作负责人
								if(data.model.oldChargeBy!=null&&data.oldChargeByName!=null)
								{
								Ext.getCmp('oldChargeBy').setValue(data.model.oldChargeBy);
								Ext.form.ComboBox.superclass.setValue.call(Ext
									.getCmp('oldChargeBy'), data.oldChargeByName);
								}
								//现工作负责人
								if(data.model.newChargeBy!=null&&data.newChargeByName)
								{
								    Ext.getCmp('newChargeBy').setValue(data.model.newChargeBy);
									Ext.form.ComboBox.superclass.setValue.call(Ext
											.getCmp('newChargeBy'), data.newChargeByName);
								}
								//签发人
								if(data.model.approveBy!=null&&data.approveByName!=null)
								{
								Ext.getCmp('changeSignBy').setValue(data.model.approveBy);
								Ext.form.ComboBox.superclass.setValue.call(Ext
										.getCmp('changeSignBy'), data.approveByName);
								}
								//值班负责人
								if(data.model.dutyChargeBy!=null&&data.dutyChargeByName!=null)
								{
								Ext.getCmp('changeDutyBy').setValue(data.model.dutyChargeBy);
								Ext.form.ComboBox.superclass.setValue.call(Ext
											.getCmp('changeDutyBy'),data.dutyChargeByName);
								}
								//变更时间
								if(data.model.strOldApprovedFinishDate!=null)
								{
								changeDate.setValue(data.model.strOldApprovedFinishDate);
								}
							}
							if(data.model.changeStatus==1)
							{
								isShowDelay='';
								if(data.model.id!=null)
								{
								  delayId.setValue(data.model.id);	
								}
								//延期
								//原时间
								if(data.strOldApprovedFinishDate!=null)
								{
								 delayOldDate.setValue(data.strOldApprovedFinishDate);
								}
								//延期到
								if(data.strNewApprovedFinishDate!=null)
								{
								 delayToDate.setValue(data.strNewApprovedFinishDate)
								}
								//工作负责人
								if(data.model.oldChargeBy!=null&&data.oldChargeByName!=null)
								{
								 Ext.getCmp('delayChargeBy').setValue(data.model.oldChargeBy);
								 Ext.form.ComboBox.superclass.setValue.call(Ext
											.getCmp('delayChargeBy'), data.oldChargeByName);
								}
								//运行值班负责人
								if(data.model.dutyChargeBy!=null&&data.dutyChargeByName!=null)
								{
								Ext.getCmp('delayRunBy').setValue(data.model.dutyChargeBy);
								Ext.form.ComboBox.superclass.setValue.call(Ext
										.getCmp('delayRunBy'), data.dutyChargeByName);
								}
								//值长
								if(data.model.newChargeBy!=null&&data.newChargeByName!=null)
								{
								Ext.getCmp('delayDutyBy').setValue(data.model.newChargeBy);
								Ext.form.ComboBox.superclass.setValue.call(Ext
										.getCmp('delayDutyBy'), data.newChargeByName);
								}
								
							}
							//4---许可开工 2--工作负责人变更 1--延期  7--安措办理 8---终结
							if(data.model.changeStatus==7)
							{
								if(data.model.id!=null)
								{
								  	safetyExeId.setValue(data.model.id);
								}
								//安措办理
								//工作许可人
								if(data.model.oldChargeBy!=null&&data.oldChargeByName!=null)
								{
								Ext.getCmp('safetyExePermitBy').setValue(data.model.oldChargeBy);
				                Ext.form.ComboBox.superclass.setValue.call(Ext
						         .getCmp('safetyExePermitBy'), data.oldChargeByName);
								}
						       //值班负责人  
						         if(data.model.dutyChargeBy!=null&&data.dutyChargeByName!=null)
						         {
						         Ext.getCmp('safetyExeDutyBy').setValue(data.model.dutyChargeBy);
				                 Ext.form.ComboBox.superclass.setValue.call(Ext
						          .getCmp('safetyExeDutyBy'), data.dutyChargeByName);
						         }
							}
							if(data.model.changeStatus==4)
							{
								//许可开工
								if(data.model.id!=null)
								{
								  	permitId.setValue(data.model.id);
								}
								//许可人
								if(data.model.approveBy!=null&&data.approveByName!=null)
								{
								Ext.getCmp('permitBy').setValue(data.model.approveBy);
				                Ext.form.ComboBox.superclass.setValue.call(Ext
						         .getCmp('permitBy'), data.approveByName);
								}
						       //工作负责人
								if(data.model.oldChargeBy!=null&&data.oldChargeByName!=null)
								{
						       Ext.getCmp('permitChargeBy').setValue(data.model.oldChargeBy);
				              Ext.form.ComboBox.superclass.setValue.call(Ext
						        .getCmp('permitChargeBy'),  data.oldChargeByName);
								}
						        //值班负责人
								if(data.model.dutyChargeBy!=null&&data.dutyChargeByName!=null)
								{
						        Ext.getCmp('permitDutyBy').setValue(data.model.dutyChargeBy);
				                 Ext.form.ComboBox.superclass.setValue.call(Ext
						           .getCmp('permitDutyBy'), data.dutyChargeByName);
								}
								if(data.strOldApprovedFinishDate!=null)
								{
								permitDate.setValue(data.strOldApprovedFinishDate);
								}
							}
							if(data.model.approveStatus==8)
							{
								//终结
								if(data.model.id!=null)
								{
								  endId.setValue(data.model.id);	
								}
								//结束时间
								if(data.strOldApprovedFinishDate!=null)
								{
								endDate.setValue(data.strOldApprovedFinishDate);
								}
								//工作负责人
								if(data.model.oldChargeBy!=null&&data.oldChargeByName!=null)
								{
								Ext.getCmp('endChargeBy').setValue(data.model.oldChargeBy);
				                Ext.form.ComboBox.superclass.setValue.call(Ext
						       .getCmp('endChargeBy'), data.oldChargeByName);
								}
								//点检验收人
								if(data.model.newChargeBy!=null&&data.newChargeByName!=null)
								{
								Ext.getCmp('endAcceptBy').setValue(data.model.newChargeBy);
								Ext.form.ComboBox.superclass.setValue.call(Ext
								.getCmp('endAcceptBy'), data.newChargeByName);
								}
								//工作许可人
								if(data.model.fireBy!=null&&data.fireByName!=null)
								{
								Ext.getCmp('endPemitBy').setValue(data.model.fireBy);
								Ext.form.ComboBox.superclass.setValue.call(Ext
									.getCmp('endPemitBy'), data.fireByName);
								}
								//值班负责人
								if(data.model.dutyChargeBy!=null&&data.dutyChargeByName!=null)
								{
								Ext.getCmp('endDutyBy').setValue(data.model.dutyChargeBy);
								Ext.form.ComboBox.superclass.setValue.call(Ext
										.getCmp('endDutyBy'), data.dutyChargeByName);
								}
								if(data.model.totalLine!=null)
								{
								endTotalLine.setValue(data.model.totalLine);
								}
								if(data.model.nobackoutLine!=null)
								{
								endNoBackoutLine.setValue(data.model.nobackoutLine);
								}
								if(data.model.nobackoutNum!=null)
								{
								 endNoBackoutNum.setValue(data.model.nobackoutNum);
								}
							}
						}
						}
						
						 showChange(isShowChangeCharge);
					     showDelay(isShowDelay);
				 
						
                 
					},
					failure : function() {
						btnModify.setVisible(false);
						Ext.Msg.alert("提示",
								"错误");
					}
				}, 'workticketNo=' + strBusiNo);
	        }
	        
	        function showChange(status)
	        {
	         //工作负责人变更默认不展开
	        	if(status=="none")
	        	{
	        	 Ext.get("changeLabel").dom.checked = false;	
	        	}
	        	else
	        	{
	        		Ext.get("changeLabel").dom.checked = true;
	        	}
	        	
	        	Ext.get("oldChargeBy").dom.parentNode.parentNode.parentNode.style.display = status;
                Ext.get("changeSignBy").dom.parentNode.parentNode.parentNode.style.display =status;
                Ext.get("changeDate").dom.parentNode.parentNode.parentNode.style.display = status;
                Ext.get("newChargeBy").dom.parentNode.parentNode.parentNode.style.display =status;
                Ext.get("changeDutyBy").dom.parentNode.parentNode.parentNode.style.display = status;
	        }
	        function showDelay(status)
	        {
	        	if(status=="none")
	        	{
	        	 Ext.get("delayLabel").dom.checked = false;	
	        	}
	        	else
	        	{
	        		Ext.get("delayLabel").dom.checked = true;
	        	}
	         //延期默认不展开           
				Ext.get("delayOldDate").dom.parentNode.parentNode.parentNode.style.display = status;
                Ext.get("delayChargeBy").dom.parentNode.parentNode.parentNode.style.display = status;
                Ext.get("delayDutyBy").dom.parentNode.parentNode.parentNode.style.display = status;
                Ext.get("delayToDate").dom.parentNode.parentNode.parentNode.style.display =status;
                Ext.get("delayRunBy").dom.parentNode.parentNode.parentNode.style.display = status;
	        }
	      
	        if((workticketType!="4"&&register.workticketStatus==8)||(workticketType=="4"&&register.workticketStatus==28))
	        {
	        	  initData();
	        	  typeCheck();
	        	  
	        	 form.setVisible(true);
	        }
	        else
	        {
	        	
	        	 form.setVisible(false);
	        }
	       
	     
	       
});
