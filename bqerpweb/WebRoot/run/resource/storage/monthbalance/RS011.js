/**
 * 库存月结
 * @author 黄维杰 081226
 */
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
//    function test(obj) {
//        var temp = '';
//        for(var prop in obj) {
//            temp += prop + ': ' + obj[prop] + '\n'
//        }
//        alert(temp);
//    }
    Ext.QuickTips.init();
    
    /** 画面结账年月份 */
    var userYearMonth = "";
    
    var now = new Date();
    var month = now.getMonth();
    var day = now.getDate();
    var snow = now.getYear() + "-" + (month+1 > 9 ? month+1 : ("0"+(month+1))) + "-" 
            + (day > 9 ? day : ("0" + day));
    // 结账日期
    var today = new Ext.form.TextField({
        style : {
                    'margin-top' : 1
                },
        fieldLabel : "结账日期",
        value : snow,
        readOnly : true
    })
    
    // 结账年月份
    var balancetime = new Ext.form.NumberField({
        fieldLabel : "结账年月份<font color='red'>*</font>",
        maxLength : 6,
        allowBlank : false,
        minLength : 6,
        allowDecimals: false
    })
    
    // 主框架
    var listPanel = new Ext.FormPanel({
                autoScroll : true,
                labelAlign : 'right',
                buttonAlign : 'center',
                width : 300,
                height : 105,
                frame : false,
                border : false,
                items : [today, balancetime],
                buttons : [{
                    text : "确定",
                    handler : onYes
                }]
            });
    listPanel.render("position");
    onInitial();
    function onInitial() {
        Ext.Ajax.request({
            method : Constants.POST,
            url : 'resource/getLatestBalance.action',
            success : function(result, request) {
                // check返回信息
                var o = eval("("
                        + result.responseText + ")");
                balancetime.setValue(o.msg);
            },
            failure : function() {
                Ext.Msg.alert(Constants.SYS_REMIND_MSG,
                        Constants.COM_E_014);
            },
            params : {
                userYearMonth : userYearMonth
            }
        });
    }
    
    function onYes() {
        var balanceType = "";
        userYearMonth = balancetime.getValue();
        if (!balancetime.validate()) {
        	Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.RS011_E_001);
			return;
        } else {
        Ext.Ajax.request({
                    method : Constants.POST,
                    timeout:900000,
                    url : 'resource/checkYearMonth.action',
                    success : function(result, request) {
                        // check返回信息
                        var o = eval("("
                                + result.responseText + ")");
                        // 返回"ISON",处于结账状态（上次结账失败），拒绝操作。
                        if (o.msg == "ISON") {
                        	Ext.Msg.alert(Constants.SYS_REMIND_MSG,
                                    Constants.RS011_E_003);
                        // 返回“A”，重新结算上次记录
                        }else if (o.msg == "A") {
                            balanceType = "A";
                            Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.RS011_C_002, function(
                            buttonobj) {
                                if (buttonobj == "yes") {
									Ext.Msg.wait("操作进行中...", "请稍候");
                                    Ext.Ajax.request({
                                        method : Constants.POST,
                                        timeout:900000,
                                        url : 'resource/doBalance.action',
                                        success : function(result, request) {
                                            // 结算完了，返回结算结果
                                            var o = eval("("
                                                    + result.responseText + ")");
                                            // 结算成功
                                            if (o.msg == "ok") {
                                                Ext.Msg.alert(Constants.SYS_REMIND_MSG,
                                                        Constants.RS011_C_003);
					                        // 所有记录都已结账了，无需结账
					                        } else if (o.msg == "OVER") {
					                        	Ext.Msg.alert(Constants.SYS_REMIND_MSG,
					                                    Constants.RS011_E_004);
                                            // 数据库出错，弹出提示信息
                                            } else if (o.msg == "sql") {
                                                Ext.Msg.alert(Constants.SYS_REMIND_MSG,
                                                        Constants.COM_E_014);
                                            // 结算失败，弹出失败源
                                            } else {
                                                Ext.Msg.alert(Constants.SYS_REMIND_MSG, 
                                                Constants.RS011_E_002);
                                            }
                                        },
                                        failure : function() {
                                            Ext.Msg.alert(Constants.SYS_REMIND_MSG,
                                                    Constants.COM_E_014);
                                        },
                                        params : {
                                            userYearMonth : userYearMonth,
                                            balanceType : balanceType
                                        }
                                    });
                                }
                            })
                            
                        // 返回“B”，结算本月
                        } else if (o.msg == "B") {
                            balanceType = "B";
                            Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.RS011_C_001, function(
                            buttonobj) {
                                if (buttonobj == "yes") {
									Ext.Msg.wait("操作进行中...", "请稍候");
                                    Ext.Ajax.request({
                                        method : Constants.POST,
                                        timeout:900000,
                                        url : 'resource/doBalance.action',
                                        success : function(result, request) {
                                            // 结算完了，返回结算结果
                                            var o = eval("("
                                                    + result.responseText + ")");
                                            // 结算成功
                                            if (o.msg == "ok") {
                                                Ext.Msg.alert(Constants.SYS_REMIND_MSG,
                                                        Constants.RS011_C_003);// 所有记录都已结账了，无需结账
					                        } else if (o.msg == "OVER") {
					                        	Ext.Msg.alert(Constants.SYS_REMIND_MSG,
					                                    Constants.RS011_E_004);
                                            // 数据库出错，弹出提示信息
                                            } else if (o.msg == "sql") {
                                                Ext.Msg.alert(Constants.SYS_REMIND_MSG,
                                                        Constants.COM_E_014);
                                            // 结算失败，弹出失败源
                                            } else {
//                                                Ext.Msg.alert(Constants.SYS_REMIND_MSG,
//                                                    Constants.RS011_E_002);
                                            	  Ext.Msg.alert(Constants.SYS_REMIND_MSG,
                                                    o.msg);
                                            }
                                        },
                                        failure : function() {
                                            Ext.Msg.alert(Constants.SYS_REMIND_MSG,
                                                    Constants.COM_E_014);
                                        },
                                        params : {
                                            userYearMonth : userYearMonth,
                                            balanceType : balanceType
                                        }
                                    });
                                }
                            })
                        // 返回“time”，输入的时间不满足结算条件
                        } else if (o.msg == "TIME") {
                            Ext.Msg.alert(Constants.SYS_REMIND_MSG,
                                    Constants.RS011_E_001);
                        }
                    },
                    failure : function() {
                        Ext.Msg.alert(Constants.SYS_REMIND_MSG,
                                Constants.COM_E_014);
                    },
                    params : {
                        userYearMonth : userYearMonth
                    }
                });
        }
    }
})