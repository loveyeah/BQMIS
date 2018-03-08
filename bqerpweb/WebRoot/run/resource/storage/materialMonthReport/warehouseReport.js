// 物料盘点表打印
// author:chenshoujiang
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
    Ext.QuickTips.init();   
 //--------add by fyyang 090810----月份
    /**
	 * 月份Field
	 */
	var nowdate = new Date();
	var date1 = nowdate.toLocaleDateString();
	var exdate;
	if (date1.substring(6, 7) == '月') {
		exdate = date1.substring(0, 4) + '-0' + date1.substring(5, 6);
	} else {
		exdate = date1.substring(0, 4) + "-" + date1.substring(5, 7);
	}
	var enddate = exdate;
	var monthDate = new Ext.form.TextField({
		name : 'monthDate',
		value : exdate,
		id : 'monthDate',
		fieldLabel : "月份",
		style : 'cursor:pointer',
		cls : 'Wdate',
		width : 90,
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M',
					dateFmt : 'yyyy-MM',
					alwaysUseStartDate : true
				});
			}
		}
	});
 //-----------------------------------   
    
   
    
    // 打印盘点表按钮
    var btnPrint = new Ext.Button({
        text : "打印",
        iconCls : 'print',
        disabled:false,
        handler : printMaterialType
    }); 
       
    // head工具栏
    var headTbar = new Ext.Toolbar({
        region : 'north',
//      border:false,
        items : ['月份：',monthDate,btnPrint]
    });
    
    // 显示区域
    var layout = new Ext.Viewport({
        layout : 'fit',
//        margins : '0 0 0 0',
//        border : false,
        autoHeight : true,
        items : [headTbar]        
    });

    
    
    
    /**
     * 物料盘点打印按钮按下时
     */
    function printMaterialType() 
    {
    	var strMonth=monthDate.getValue().substring(0,4)+monthDate.getValue().substring(5,7);
        strReportAdds="/powerrpt/report/webfile/receiveIssues.jsp?month="+strMonth;
		window.open(encodeURI(strReportAdds));
    }
   
    
   
})