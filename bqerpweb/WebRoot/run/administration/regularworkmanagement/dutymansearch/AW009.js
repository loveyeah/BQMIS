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
	var  txtStartDate = new Ext.form.TextField({
        id : 'startDate',
        name : 'startDate',
        style : 'cursor:pointer',
        value : dteFrom,
        readOnly:true,
        listeners : {
        	focus : function() {
                WdatePicker({
                	isShowClear:false,
                    startDate : '%y-%M-%d',
                    dateFmt : 'yyyy-MM-dd',
                    alwaysUseStartDate : false
                });
            }
        }
    });
 	//定义查询结束时间
    var txtEndDate = new Ext.form.TextField({
        id : 'endDate',
        name : 'endDate',
        style : 'cursor:pointer',
        value : dteTo,
        readOnly:true,
        listeners : {
        	focus : function() {
                WdatePicker({
                    startDate : '%y-%M-%d',
                    dateFmt : 'yyyy-MM-dd',
                    alwaysUseStartDate : true
                });
            }
        }
    });     
    //子工作类别查询列表,调用共通函数
    var drpWorkType = new Ext.form.CmbSubWorkType({
         width : 100
    });
    	drpWorkType.store.load();
	//值别查询类别,调用共通函数
    var drpDutyType = new Ext.form.CmbDuty({
         width : 100
    });
    	drpDutyType.store.load();    	
    //类定义
	var recordMy = Ext.data.Record.create([{
                name : 'dutyManName'
            }, {
                name : 'dutyTime'
            }, {
                name : 'position'
            }, {
                name : 'dutyTypeName'
            }, {
                name : 'subWorkTypeName'
            },{
            	name : 'replaceManName'
            },{
            	name : 'leaveManName'
            },{
            	name : 'reason'
            }]);
	// 定义获取数据源
	var dataProxy = new Ext.data.HttpProxy({
				   url:'administration/getOnDutyManInfo.action'
		});
	// 定义格式化数据
	var theReader = new Ext.data.JsonReader({
			root : "list",
            totalProperty : "totalCount"
		},recordMy);
	// 定义封装缓存数据的对象
	var store = new Ext.data.Store({
		proxy : dataProxy,
		reader : theReader
		});
	store.on("load",function(){
	  if(store.getTotalCount() <= 0){
	  	bbar.disable(true);
	  	Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,MessageConstants.COM_I_003);
	  }else{
	  	bbar.enable(true);
	  }
	});
   //创建ColumnModel
	var gridCM = new Ext.grid.ColumnModel([ new Ext.grid.RowNumberer(
							{
							header: "行号",
							width:35
							})
							,{
                        	header:"值班人员",
                        	width:70,
                        	sortable:true,
                        	dataIndex:'dutyManName'
                        },{
							header:"值班时间",
							width:100,
							sortable:true,
							dataIndex:'dutyTime'
						},{
							header:"岗位名称",
							width:80,
							sortable:true,
							dataIndex:'position'
						},{
							header:"值别",
							width:70,
							sortable:true,
							dataIndex:'dutyTypeName'
						},{
							header:"工作类别",
							width:70,
							sortable:true,
							dataIndex:'subWorkTypeName'
						},{
							header:"替班人员",
							width:70,
							sortable:true,
							dataIndex:'replaceManName'
						},{
							header:"缺勤人员",
							width:70,
							sortable:true,
							dataIndex:'leaveManName'
						},{
							header:"缺勤原因",
							width:120,
							sortable:true,
							dataIndex:'reason'
						}]);
	var bbar = new Ext.PagingToolbar({
                            pageSize : Constants.PAGE_SIZE,
                            store : store,
                            disabled : true,
                            displayInfo : true,
                            displayMsg : MessageConstants.DISPLAY_MSG,
                            emptyMsg : MessageConstants.EMPTY_MSG
                        });
    // --gridpanel显示格式定义-----开始-------------------
	var grid = new Ext.grid.GridPanel({
				store : store,
				cm : gridCM,
				//顶部工具栏
				tbar:['值班日期: 从',txtStartDate,'到',
						   txtEndDate,'-','工作类别: ',drpWorkType,'-',
						   '值别: ',drpDutyType,'-',{
                            			text : "查询",
                            			id   : 'firm',
                            			name : 'firm',
                            			iconCls : "query",
                            			handler : queryRecord
                          }],
                 // 底部工具栏
                 bbar : bbar,
                 model:'local',
                  // 不允许移动列
                  enableColumnMove : false
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
       
        if((startDateValue!="") && (endDateValue !="")){
        	 
       		var date1 = Date.parseDate(startDateValue,'Y-m-d');
   		    var date2 = Date.parseDate(endDateValue,'Y-m-d');
        	if(date1.getTime() > date2.getTime()){
        		 Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, String.format(MessageConstants.COM_E_009,"开始日期","结束日期"));
                 return ;
        	}
        }
      store.baseParams = {
   			strStartDate   :  txtStartDate.getValue(),
			strEndDate    :  txtEndDate.getValue(),
			strSubWorkTypeCode :  drpWorkType.value,
			strDutyTypeCode   :  drpDutyType.value
   			};
   		store.load({
   				params:{
   					start : 0,
   					limit : Constants.PAGE_SIZE
   				}
   		});
   		
   		
   }
});
