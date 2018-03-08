
// author:chenshoujiang
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
    Ext.QuickTips.init();   
 //--------add by fyyang 090810----月份
    /**
	 * 月份Field
	 */
    
    // 获得年份
	function getDate() {
		var Y;
		Y = new Date();
		Y = Y.getFullYear().toString(10);
		return Y;
	}
	
	// 获得月份
	function getMonth() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString() + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t;
		return s;
	}
	
	// 年月份选择
	var formatType;
	var yearRadio = new Ext.form.Radio({
		id : 'year',
		name : 'queryWayRadio',
		hideLabel : true,
		boxLabel : '年份'
	});
	var monthRadio = new Ext.form.Radio({
		id : 'month',
		name : 'queryWayRadio',
		hideLabel : true,
		boxLabel : '月份',
		checked : true,
		listeners : {
			check : function() {
				var queryType = getChooseQueryType();
//				
				switch (queryType) {
					case 'year' : {
						formatType = 1;
						time.setValue(getDate());
						break;
					}
					case 'month' : {
						time.setValue(getMonth());
						formatType = 2;
						break;
					}
				}
			}
		}
	});
	
	var time = new Ext.form.TextField({
		id : 'time',
		allowBlank : true,
		readOnly : true,
		value : getMonth(),
		width : 100,
		listeners : {
			focus : function() {
				var format = '';
				if (formatType == 1)
					format = 'yyyy';
				if (formatType == 2)
					format = 'yyyy-MM';
				WdatePicker({
					startDate : '%y-%M-%d',
					dateFmt : format,
					alwaysUseStartDate : false
				});
			}
		}
	});
 //-----------------------------------   
    
    
    // 打印盘点表按钮
    var btnPrint = new Ext.Button({
        text : "查询",
        iconCls : 'print',
        disabled:false,
        handler : printSupply
    }); 
    
    // head工具栏
    var headTbar = new Ext.Toolbar({
        region : 'north',
//      border:false,
        items : [yearRadio,'-',monthRadio,'-',time,'-',btnPrint]
    });
    
    // 显示区域
//    var layout = new Ext.Viewport({
//        layout : 'fit',
////        margins : '0 0 0 0',
////        border : false,
//        autoHeight : true,
//        items : [headTbar]        
//    });

   
    // 遍历所有的REDIO获得ID
	function getChooseQueryType() {
		var list = document.getElementsByName("queryWayRadio");
		for (var i = 0; i < list.length; i++) {
			if (list[i].checked) {
				return list[i].id;
			}
		}
	}
    /**
     * 物料供应打印按钮按下时
     */
    function printSupply() 
    {
    	var strMonth=time.getValue();//.substring(0,4)+time.getValue().substring(5,7);
        url="/powerrpt/report/webfile/bqmis/businessItem.jsp?dateTime="+strMonth;
        document.all.iframe1.src = url;
		//window.open(encodeURI(strReportAdds));
    }
    
//    var month=time.getValue();//.substring(0,4)+time.getValue().substring(5,7);
//    var url="/powerrpt/report/webfile/bqmis/businessItem.jsp?dateTime="+month;
    var url="";
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
			html : '<iframe id="iframe1" name="iframe1"  src="' + url
					+ '"  frameborder="0"  width="100%" height="100%"  />'
		}]
	});
   
})