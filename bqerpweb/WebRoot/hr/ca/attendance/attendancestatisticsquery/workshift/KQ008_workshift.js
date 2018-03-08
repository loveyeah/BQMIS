Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {

    // 自定义函数
    var contractQuery = parent.Ext.getCmp('tabPanel').contractQuery;
    // 查询条件
    var queryObject = new Object();

    // 年度
    var startDate = new Ext.form.TextField({
        id : 'startDate',
        style : 'cursor:pointer',
        readOnly : true,
        width : 100,
        listeners : {
            focus : function() {
                WdatePicker({
                    startDate : '%y-%M',
                    dateFmt : 'yyyy-MM',
                    alwaysUseStartDate : false,
                    isShowClear : false
                });
                this.blur();
            }
        }
    });
    startDate.setValue(new Date().format("Y-m"));

    // 查询按钮
    var queryBtn = new Ext.Button({
        text : Constants.BTN_QUERY,
        iconCls : Constants.CLS_QUERY,
        handler : queryHandler
    });

    // 导出按钮
    var exportBtn = new Ext.Button({
        text : Constants.BTN_EXPORT,
        iconCls : Constants.CLS_EXPORT,
        disabled : true,
        handler : exportHandler
    });
    var titleLabel= new Ext.form.FieldSet({
        anchor : '100%',
        border : false,
        region:'north',
        height:35,
        style : "padding-top:8px;padding-bottom:0px;border:0px;margin:0px;text-align:center;",
        items : [new Ext.form.Label({
            text:'离职员工花名册',
            style:'font-size:22px;'
        })]
    })
    
   // 帐票预览
  	var report = new Ext.BoxComponent({
        el: 'popFrame'
    });
    
    var fullPanel = new Ext.Panel({
        tbar : ['选择年月<font color="red">*</font>:', startDate, '-', queryBtn, exportBtn],
        layout:'fit',
        border:false,
        items:[report]
    })
    // 设定布局器及面板
    new Ext.Viewport({
        enableTabScroll : true,
        layout : "fit",
        border:false,
        items : [fullPanel]
    });

    /**
     * 查询处理
     */
    function queryHandler() {
    
    	 // 保存查询条件
        var startDate1 = startDate.getValue();
        var year = startDate1.substr(0,4);
        var month = startDate1.substr(5,2);
        queryObject.year = year;
        queryObject.month = month;
        exportBtn.setDisabled(false);
        document.all.popFrame.src = "/power/report/webfile/hr/ca/KQ013_attendentStatistics.jsp?type=3"+'&year='+year+"&month="+month;
    }

    /**
     * 导出处理
     */
    function exportHandler() {
        Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_007, function(buttonobj) {
            if (buttonobj == "yes") {
				document.all.popFrame1.src = "ca/exportLeaveStatisticsQueryInfo.action?year="
						+ queryObject.year + "&month=" + queryObject.month+"&type=3";
			}
        });
    
    	
        }
});