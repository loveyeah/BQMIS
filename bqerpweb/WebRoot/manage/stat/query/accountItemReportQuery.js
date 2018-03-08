Ext.onReady(function() {
// 系统当天日期
	var sd = new Date();
	var ed = new Date();
	// 系统当天前30天的日期
	sd.setDate(sd.getDate());
	// 系统当天后30天的日期
	ed.setDate(ed.getDate() + 10);

//----------左边grid----------------------
			// 定义grid
var leftMyRecord = Ext.data.Record.create([
	{name : 'itemCode'},
    {name : 'itemName'},
	{name : 'unitName'},
	{name : 'unitCode'}
	]);

	var leftDataProxy = new Ext.data.HttpProxy(
			{
				url:'manager/findItemNameListForReportQuery.action'
			}
	);

	var leftTheReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"

	}, leftMyRecord);

	var leftStore = new Ext.data.Store({

		proxy : leftDataProxy,

		reader : leftTheReader

	});

	var sm = new Ext.grid.CheckboxSelectionModel();

	var leftGrid = new Ext.grid.GridPanel({
//		region : "center",
//		layout : 'fit',
		autoScroll : true,
		store : leftStore,

		columns : [// 设计页面显示的表格信息，字段与Ext.data.Record.create匹配
		sm, 

		{
			header : "指标名称",
			width : 100,
			sortable : true,
			dataIndex : 'itemName'
		},

		{
			header : "单位",
			width : 100,
			sortable : true,
			dataIndex : 'unitName'
		}
		],
		sm : sm

	});
//--------------------------------		
//-------------tbar--------------
    // 检修专业
    var itemCodeStore = new Ext.data.Store({
        proxy : new Ext.data.HttpProxy({
            url : 'manager/getBpCAnalyseAccountList.action'
        }),
        reader : new Ext.data.JsonReader({
        	totalProperty : "totalCount",
            root : 'list'
        }
        , [{
                name : 'accountCode',
                mapping : 'baseInfo.accountCode'
            }, {
                name : 'accountName',
                mapping : 'baseInfo.accountName'
            }]
            )
    });
    itemCodeStore.load();
  
    var cbxItemCode = new Ext.form.ComboBox({
        id : 'cbxItemCode',
        fieldLabel : "台帐:",
        store : itemCodeStore,
        displayField : "accountName",
        valueField : "accountCode",
        hiddenName : 'itemCode',
        mode : 'local',
        triggerAction : 'all',
        value : '',
        readOnly : true,
        width : 150,
        listeners : {
			select : cbxItemCodeSelected
		}
    });	
    function cbxItemCodeSelected()
    {
    		Ext.Ajax.request({
		url : 'manager/getAccountItemModel.action',
		params : {
			itemId : Ext.get("itemCode").dom.value
		},
		method : 'post',
		waitMsg : '正在加载数据...',
		success : function(result, request) {
			var data = eval('(' + result.responseText + ')');
              var myType=data.accountType;
             // 1  时 3  日4  月5  季6  年
              if(myType=="1") 
              {
              Ext.get("rdHour").dom.checked=true;
              hideData();
              startHour.show();
              endHour.show();
              }
              if(myType=="3") 
              {
              Ext.get("rdDay").dom.checked=true;
               hideData();
               startDate.show();
               endDate.show();
              }
              if(myType=="4") 
              {
              Ext.get("rdMonth").dom.checked=true;
                 hideData();
                 startMonth.show();
                 endMonth.show();
              }
              
               if(myType=="5")
               {
               Ext.get("rdSeason").dom.checked=true;
                 hideData();
                 startYear.show();
                 startBox.show();
                 endYear.show();
                 endBox.show();
               }
               if(myType=="6") 
               {
               Ext.get("rdYear").dom.checked=true;
                  hideData();
               startYear.show();
                  endYear.show();
               }
         
		
		},
		failure : function(result, request) {
			Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
		}
	   });
    }

	//--------按日-----
    var startDate=new Ext.form.DateField({
    name:'startDate',
    readOnly:true,
    format:'Y-m-d',
    value:sd,
    width : 100
    
    });
 
      var endDate=new Ext.form.DateField({
    name:'endDate',
    readOnly:true,
    format:'Y-m-d',
     value:ed,
        width : 100
    
    });
   //------------------按小时-----------------------------

    //------------小时初始化数据---------------------
    function getSDate() {
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

 function getEDate() {
	var d, s, t;
	d = new Date();
	s = d.getFullYear().toString(10) + "-";
	t = d.getMonth() + 1;
	s += (t > 9 ? "" : "0") + t + "-";
	t = d.getDate()+1;
	s += (t > 9 ? "" : "0") + t + " ";
	t = d.getHours();
	s += (t > 9 ? "" : "0") + t + ":";
	t = d.getMinutes();
	s += (t > 9 ? "" : "0") + t + ":";
	t = d.getSeconds();
	s += (t > 9 ? "" : "0") + t;

	return s;
}
    //---------------------------------------------
  // var sHourNow=getSDate();
     var sHourDate=getSDate().substring(0,13);
     var eHourDay=getEDate().substring(0,13);
    // alert(eHourDay);
         
        	var startHour = new Ext.form.TextField({
		name : 'startHour',
		value : sHourDate,
		id : 'startHour',
		fieldLabel : "小时",
		style : 'cursor:pointer',
		cls : 'Wdate',
		 width : 100,
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-%d 00',
					dateFmt : 'yyyy-MM-dd HH',
					alwaysUseStartDate : true
				});
			}
		}
	});

    
    	var endHour = new Ext.form.TextField({
		name : 'endHour',
		value : eHourDay,
		id : 'endHour',
		fieldLabel : "小时",
		style : 'cursor:pointer',
		cls : 'Wdate',
		 width : 100,
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-%d 00',
					dateFmt : 'yyyy-MM-dd HH',
					alwaysUseStartDate : true
				});
			}
		}
	});
  
    //------------按月--------------
//--------初始化月份---------------------    
	var nowdate = new Date();
	
	var date1 = nowdate.toLocaleDateString();
	var sMonthdate;
	var eMonthdate;
	if (date1.substring(6, 7) == '月') {
		sMonthdate = date1.substring(0, 4) + '-0' + date1.substring(5, 6);
	} else {
		sMonthdate = date1.substring(0, 4) + "-" + date1.substring(5, 7);
	}
	 var eMonthYear=parseInt(sMonthdate.substring(0,4));
	 var eMonth=parseInt(sMonthdate.substring(5,7));
	 if(eMonth==12)
	 {
	  	eMonthYear=eMonthYear+1;
	  	eMonth=1;
	 }
	 else
	 {
	 	eMonth=eMonth+1;
	 }
	 var strEMonth=eMonth+"";
	 if(strEMonth.length==1)
	 {
	   	strEMonth="0"+eMonth;
	 }
	 
	 eMonthdate=eMonthYear+"-"+strEMonth;
//-----------------------------------------------	 
    	var startMonth = new Ext.form.TextField({
		name : 'startMonth',
		value : sMonthdate,
		id : 'startMonth',
		fieldLabel : "月份",
		style : 'cursor:pointer',
		cls : 'Wdate',
		 width : 100,
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
	
	var endMonth = new Ext.form.TextField({
		name : 'endMonth',
		value : eMonthdate,
		id : 'endMonth',
		fieldLabel : "月份",
		style : 'cursor:pointer',
		cls : 'Wdate',
		 width : 100,
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
	
	//-------------年份-----------
	
	
var syear=date1.substring(0, 4);
var eyear=parseInt(syear)+1;
	var startYear = new Ext.form.TextField({
		id : 'startYear',
		name : 'startYear',
		fieldLabel : "年份",
		style : 'cursor:pointer',
		cls : 'Wdate',
		// width : 150,
		value : syear,
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y',
					dateFmt : 'yyyy',
					alwaysUseStartDate : true
				});
			}
		}
	});
	
	var endYear = new Ext.form.TextField({
		id : 'endYear',
		name : 'endYear',
		fieldLabel : "年份",
		style : 'cursor:pointer',
		cls : 'Wdate',
		// width : 150,
		value : eyear,
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y',
					dateFmt : 'yyyy',
					alwaysUseStartDate : true
				});
			}
		}
	});
	//--------季------------------
	
//	var startSeason = new Ext.form.TextField({
//		id : 'startSeason',
//		name : 'startSeason',
//		fieldLabel : "年份",
//		style : 'cursor:pointer',
//		cls : 'Wdate',
//		autowidth : true,
//	//	value : year,
//		listeners : {
//			focus : function() {
//				WdatePicker({
//					startDate : '%y',
//					dateFmt : 'yyyy',
//					alwaysUseStartDate : true
//				});
//			}
//		}
//	});
	var startBox = new Ext.form.ComboBox({
		fieldLabel : '季度',
		store : [['1', '第一季度'], ['2', '第二季度'], ['3', '第三季度'], ['4', '第四季度']],
		id : 'startBox',
		name : 'startBox',
		valueField : "value",
		displayField : "text",
		mode : 'local',
		typeAhead : true,
		forceSelection : true,
		hiddenName : 'startBoxName',
		editable : false,
		triggerAction : 'all',
		width : 110,
		selectOnFocus : true,
		value : 1
	});
	
	
//		var endSeason = new Ext.form.TextField({
//		id : 'endSeason',
//		name : 'endSeason',
//		fieldLabel : "年份",
//		style : 'cursor:pointer',
//		cls : 'Wdate',
//		autowidth : true,
//	//	value : year,
//		listeners : {
//			focus : function() {
//				WdatePicker({
//					startDate : '%y',
//					dateFmt : 'yyyy',
//					alwaysUseStartDate : true
//				});
//			}
//		}
//	});
	var endBox = new Ext.form.ComboBox({
		fieldLabel : '季度',
		store : [['1', '第一季度'], ['2', '第二季度'], ['3', '第三季度'], ['4', '第四季度']],
		id : 'endBox',
		name : 'endBox',
		valueField : "value",
		displayField : "text",
		mode : 'local',
		typeAhead : true,
		forceSelection : true,
		hiddenName : 'endBoxName',
		editable : false,
		triggerAction : 'all',
		width : 110,
		selectOnFocus : true,
		value : 1
	});
	
	//----------------------------
   
var itemType = new Ext.Panel({
		hideLabel : true,
		layout : 'column',
		width : 200,
		style : {
			cursor : 'hand'
		},
		height : 20,
		items : [new Ext.form.Radio({
			columnWidth : 0.2,
			checked : true, 
			boxLabel : "时", 
			name : "itemType", 
			id:'rdHour',
			inputValue : "hour", 
			disabled:true,
			listeners : {
				check : function(checkbox, checked) { // 选中时,调用的事件
					   Ext.get("startDate").dom.format='Y-m';
				}
			}
		}), new Ext.form.Radio({
			columnWidth : 0.2,
			boxLabel : "日",
			name : "itemType",
			id:'rdDay',
			disabled:true,
			inputValue : "day"
		}), new Ext.form.Radio({
			columnWidth : 0.2,
			boxLabel : "月",
			name : "itemType",
			id:'rdMonth',
			disabled:true,
			inputValue : "month"
		}), new Ext.form.Radio({
			columnWidth : 0.2,
			boxLabel : "季",
			name : "itemType",
			disabled:true,
			id:'rdSeason',
			inputValue : "season"
		}), new Ext.form.Radio({
			columnWidth : 0.2,
			boxLabel : "年",
			name : "itemType",
			disabled:true,
			id:'rdYear',
			inputValue : "year"
		})]
		
	});
    
    

    
    
var headPanel=new Ext.Panel({
region : 'north',
renderTo:"mytbar",
bodyStyle:'display:none',
tbar:['台帐:',cbxItemCode,'-','区间：',startDate,startHour,startMonth,startYear,startBox,'~',
endDate,endHour,endMonth,endYear,endBox,
{
  text:'查询',
  iconcls:'query',
  handler: queryData
}
],
height:20
});

function hideData()
{
	startHour.hide();
	startDate.hide();
	startMonth.hide();
	startYear.hide();
	startBox.hide();
	endDate.hide();
	endMonth.hide();
    endYear.hide();
	endBox.hide();
	endHour.hide();
	
}
hideData();
startHour.show();
endHour.show();


function queryData()
{
	var selectType=getSelectValue();
	var accountCodeData=Ext.get("itemCode").dom.value; 	
	var strStartDate="";
	var strEndDate="";
	if(Ext.get("itemCode").dom.value==""||Ext.get("itemCode").dom.value==null)
	{
	  Ext.Msg.alert("提示","请选择要查询的台帐！");	
	  return ;
	}
	
	if(selectType=="day")
	{
	var dateStart = Date.parseDate(Ext.get("startDate").dom.value, 'Y-m-d');
	var dateEnd = Date.parseDate(Ext.get("endDate").dom.value, 'Y-m-d');
	if (dateStart.getTime() >= dateEnd.getTime()) {
		Ext.Msg.alert("提示","结束日期必须大于开始日期！");	
					return;
				}
			strStartDate=	Ext.get("startDate").dom.value;
			strEndDate=Ext.get("endDate").dom.value;
	}
	if(selectType=="hour")
	{
		var dateStart = Date.parseDate(Ext.get("startHour").dom.value, 'Y-m-d H');
	var dateEnd = Date.parseDate(Ext.get("endHour").dom.value, 'Y-m-d H');
	if (dateStart.getTime() >= dateEnd.getTime()) {
		Ext.Msg.alert("提示","结束日期必须大于开始日期！");	
					return;
				}
			strStartDate=	Ext.get("startHour").dom.value;
			strEndDate=Ext.get("endHour").dom.value;
		
	}
	if(selectType=="year")
	{
		var dateStart = Date.parseDate(Ext.get("startYear").dom.value, 'Y');
	    var dateEnd = Date.parseDate(Ext.get("endYear").dom.value, 'Y');
	  if (dateStart.getTime() >= dateEnd.getTime()) {
		Ext.Msg.alert("提示","结束日期必须大于开始日期！");	
					return;
				}
			strStartDate=	Ext.get("startYear").dom.value;
			strEndDate=Ext.get("endYear").dom.value;
		
	}
	
		if(selectType=="month")
	{
		var dateStart = Date.parseDate(Ext.get("startMonth").dom.value, 'Y-m');
	    var dateEnd = Date.parseDate(Ext.get("endMonth").dom.value, 'Y-m');
	  if (dateStart.getTime() >= dateEnd.getTime()) {
		Ext.Msg.alert("提示","结束日期必须大于开始日期！");	
					return;
				}
			strStartDate=	Ext.get("startMonth").dom.value;
			strEndDate=Ext.get("endMonth").dom.value;
		
	}
	
   if(selectType=="season")
	{
		var dateStart = Date.parseDate(Ext.get("startYear").dom.value, 'Y');
	    var dateEnd = Date.parseDate(Ext.get("endYear").dom.value, 'Y');
	
	  if (dateStart.getTime() >dateEnd.getTime()) {
		Ext.Msg.alert("提示","结束日期必须大于开始日期！");	
					return;
				}
			 if (dateStart.getTime() ==dateEnd.getTime())	
			 {
			 	
			var	seasonStart=parseInt(Ext.get("startBoxName").dom.value);	
			var	seasonEnd=parseInt(Ext.get("endBoxName").dom.value);	
	        if (seasonStart>= seasonEnd) {
		          Ext.Msg.alert("提示","结束日期必须大于开始日期！");	
					return;
				}
				
			 }
			strStartDate=	Ext.get("startYear").dom.value+Ext.get("startBoxName").dom.value;
			strEndDate=Ext.get("endYear").dom.value+Ext.get("endBoxName").dom.value;
		
	}

  	leftStore.baseParams={accountCode:accountCodeData};
  	leftStore.load();
  	//---------------
 
  		Ext.Ajax.request({
		url : 'manager/findItemDataListForReportQuery.action',
		params : {
			accountCode : accountCodeData,
			itemType:selectType,
			startDate:strStartDate,
			endDate:strEndDate
		},
		method : 'post',
		waitMsg : '正在加载数据...',
		success : function(result, request) {
			var json = eval('(' + result.responseText + ')');
                 document.getElementById("centerDiv").innerHTML = "";
			
		  var grid = new Ext.grid.GridPanel({
					renderTo : 'centerDiv',
					id : 'flex-grid',
					//split : true,
					autoScroll : true,
					width:530,	
					height:510,
			       // autoHeight:true,
					border : false,
					cm : new Ext.grid.ColumnModel(json.columModle),
					ds : new Ext.data.JsonStore({
						data : json.data,
						fields : json.fieldsNames
					})
				});  
			grid.render();
			
		},
		failure : function(result, request) {
			Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
		}
	});
	
	
  	
  	//----------------
}

var tbar2=new Ext.Toolbar({
	//height:20,
	renderTo:headPanel.tbar,
 items:[itemType]
});

function getSelectValue() {
		var ns = document.getElementsByName("itemType");
		for (var i = 0; i < ns.length; i++) {
			if (ns[i].checked) {
				return ns[i].value;
			}
		}
	}
//-------------------------------	


	
	
//var view = 	new Ext.Viewport({
//		layout : "border",
//		items : [headPanel,{
//			region : 'west',
//			layout : 'fit',
//			border : false,
//			margins : '0 0 0 0',
//			collapsible : true,
//			split : true,
//			width : 250,
//			autoScroll : true,
//			items : [{
//				border : false,
//				autoScroll : true,
//				layout : 'border',
//				items : [{
//					region : 'center',
//					layout : 'fit',
//					items : [leftGrid]
//				}]
//			}]
//		} , {region : 'center',
//			layout : 'fit',
//			autoScroll : true,
//			items:[{html:'<div id="centerDiv"></div>'}]}]
//	});

var view = 	new Ext.Viewport({
		layout : "border",
		items : [headPanel,{
			region : 'east',
			layout : 'fit',
			border : false,
			margins : '0 0 0 0',
			collapsible : true,
			split : true,
			width : 530,
			autoScroll : true,
			items:[{html:'<div id="centerDiv" ></div>'}]
		} , {region : 'center',
			layout : 'fit',
			autoScroll : true,
		    items : [leftGrid]
			}]
	});



});


