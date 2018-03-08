Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
    // 系统当天日期
    var dteFrom = new Date();
    var dteTo = new Date();
    // 系统当天前15天的日期
    dteFrom.setDate(dteFrom.getDate() - 15);
    dteFrom = dteFrom.format('Y-m-d');
    // 系统当天后15天的日期
    dteTo.setDate(dteTo.getDate()); 
    dteTo = dteTo.format('Y-m-d');
    // 定义查询起始时间
	var  startDate = new Ext.form.TextField({
        id : 'startDate',
        name : 'startDate',
        style : 'cursor:pointer',
        value : dteFrom,
        readOnly:true,
        width:80,
        listeners : {
        	focus : function() {
                WdatePicker({
                	isShowClear: false,
                    startDate : '%y-%M-%d',
                    dateFmt : 'yyyy-MM-dd',
                    alwaysUseStartDate : false
                });
            }
        }
    });
 	//定义查询结束时间
    var endDate = new Ext.form.TextField({
        id : 'endDate',
        name : 'endDate',
        style : 'cursor:pointer',
        value : dteTo,
        width:80,
        readOnly:true,
        listeners : {
        	focus : function() {
                WdatePicker({
                	isShowClear: false,
                    startDate : '%y-%M-%d',
                    dateFmt : 'yyyy-MM-dd',
                    alwaysUseStartDate : true
                });
            }
        }
    });     
	//通行证号查询参数
    var passcode = new Ext.form.ComboBox({
                id : "passcode",
                fieldLabel : "所有",
                store : new Ext.data.JsonStore({fields : ['value','text'],data : [{value:'',text : ''},{value:'A',text : 'A'},{value:'B',text : 'B'},{value:'C',text : 'C'}]}),
                displayField : "text",
                valueField : "value",
                mode : 'local',
                triggerAction : 'all',
                allowBlank : true,
                width:40,
                readOnly : true
            });
	// 定义所在单位
	var firm = new Ext.form.TextField({
				id : "firm",
				name : "firm",
				width : 80,
				emptyText : ""
			});
    //  类定义
	var recordMy = Ext.data.Record.create([{
                name : 'passCode'
            }, {
                name : 'passTime'
            }, {
                name : 'car_no'
            }, {
                name : 'paperType_name'
            }, {
                name : 'paper_id'
            }, {
                name : 'firm'
            }, {
                name : 'preMan'
            }, {
                name : 'give_date'
            }, {
                name : 'postMan'
            }]);
	// 定义获取数据源
	var dataProxy = new Ext.data.HttpProxy({
				   url:'administration/getCarsList.action'
		});
	// 定义格式化数据
	var theReader = new Ext.data.JsonReader({
			root : "list",
            totalProperty : "totalCount"
		},recordMy);
	// 定义封装缓存数据的对象
	var storeMy = new Ext.data.Store({
		proxy : dataProxy,
		reader : theReader
		});
    storeMy.on("load",function(){
	      if(storeMy.getTotalCount() <= 0){
	  	    Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,MessageConstants.COM_I_003);
	       }
	     });
    //定义grid的ColumnModel
    var cmMy = new Ext.grid.ColumnModel([ 
                   new Ext.grid.RowNumberer(
					   {
							header: "行号",
							width:35
						}),{
                        	header:"通行证号",
                        	width:60,
                        	sortable:true,
                        	dataIndex:'passCode'
                        },{
                        	header:"进出时间",
                        	width:80,
                        	sortable:true,
                        	dataIndex:'passTime'
						},{
							header:"车牌号",
							width:50,
							sortable:true,
							dataIndex:'car_no'
						},{
							header:"证件类别",
							width:60,
							sortable:true,
							dataIndex:'paperType_name'
						},{
							header:"证件号",
							width:60,
							sortable:true,
							dataIndex:'paper_id'
						},{
							header:"车辆所属单位",
							width:100,
							sortable:true,
							dataIndex:'firm'
						},{
							header:"前经办人",
							width:60,
							sortable:true,
							dataIndex:'preMan'
						},{
							header:"退证时间",
							width:80,
							sortable:true,
							dataIndex:'give_date'
						},{
							header:"后经办人",
							width:60,
							sortable:true,
							dataIndex:'postMan'
						}]);
    // --gridpanel显示格式定义-----开始-------------------
	var grid = new Ext.grid.GridPanel({
				store:storeMy,
				cm : cmMy,
				sm : new Ext.grid.RowSelectionModel({
							singleSelect : true
						}),
				//顶部工具栏
				tbar:['进出日期: 从',startDate,'到',
						   endDate,'-','通行证号: ',passcode,'-',
						   '车辆所属单位: ',firm,'-',{
                            			text : "查询",
                            			id   : 'firm',
                            			name : 'firm',
                            			iconCls : Constants.CLS_QUERY,
                            			handler : queryRecord
                          }],
                 // 底部工具栏
                 bbar : new Ext.PagingToolbar({
                            pageSize : Constants.PAGE_SIZE,
                            store : storeMy,
                            displayInfo : true,
                            displayMsg : MessageConstants.DISPLAY_MSG,
                            emptyMsg : MessageConstants.EMPTY_MSG
                        }),
                  // 不允许移动列
                  enableColumnMove : false,
                  frame : false,
                  autoExpandColumn : 2,
                  viewConfig : {
					forceFit : true
				}
    });
    // --gridpanel显示格式定义-----结束-------------------
	
    // 页面加载显示数据
     new Ext.Viewport({
                enableTabScroll : true,
                layout : "border",
                items : [{
                            xtype : "panel",
                            region : 'center',
                            layout : 'fit',
                            border : false,
                            items:[grid]
                        }]
            });

	//查询函数
   function queryRecord(){
   		//获取开始时间值
        var startDateValue = Ext.get("startDate").dom.value;
        //获取结束时间值
        var endDateValue = Ext.get("endDate").dom.value;
   		//比较开始时间与结束时间
       
        if((startDateValue !="") && (endDateValue !="")){
        	 
       		var date1 = Date.parseDate(startDateValue,'Y-m-d');
   		    var date2 = Date.parseDate(endDateValue,'Y-m-d');
        	if(date1.getTime() > date2.getTime()){
        		  Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, String.format(MessageConstants.COM_E_009,"开始日期","结束日期"));
                   return;
        	}
        }

   		
   		storeMy.baseParams = {
   			strStartDate : startDate.getValue(),
			strEndDate : endDate.getValue(),
			strPasscode : passcode.value,
			strFirm : firm.getValue()
   			};
   		storeMy.load({
   				params:{
   					start : 0,
   					limit :  Constants.PAGE_SIZE
   				}
   			});
   }            
});
