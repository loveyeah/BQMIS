//  画面：物料基础信息维护/汇率维护
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function(){

	// 分页时每页显示记录条数
	var PAGE_SIZE = Constants.PAGE_SIZE;
	
    // 新增按钮
    var addBtn = new Ext.Button({
        id : 'add',
        text : "新增",
        iconCls : Constants.CLS_ADD,
        handler : addRecord
    });
    // 修改按钮
    var updateBtn = new Ext.Button({
        id : 'update',
        text : "修改",
        iconCls : Constants.CLS_UPDATE,
        handler : updateRecord
    });
    // 删除按钮
    var deleteBtn = new Ext.Button({
        id : 'delete',
        text : "删除",
        iconCls : Constants.CLS_DELETE,
        handler : deleteRecord
    });
    // grid中的数据 基准币别,兑换币别,汇率,有效开始日期,有效截止日期,流水号
    var runGridList = new Ext.data.Record.create([{
    	// 基准币别 name
        name : 'oriCurrencyName'
    }, {
    	// 兑换币别 name
        name : 'dstCurrencyName'
    }, {
  	    // 汇率
        name : 'rate'
    }, {
        // 有效开始日期
    	name : 'effectiveDate'
    }, {
    	// 有效截止日期 
    	name : 'discontinueDate'
    }, {
    	// 流水号
    	name : 'exchangeRateId'
    }, {
    	// 基准币别 id
    	name : 'oriCurrencyId'
    }, {
    	// 兑换币别 id
    	name : 'dstCurrencyId'
    }
    ]);
    
    // grid中的store
    var runGridStore = new Ext.data.Store({
        proxy : new Ext.data.HttpProxy({
            url : 'resource/getExchangeRateList.action' 
        }),
        reader : new Ext.data.JsonReader({
            root : 'list',
            totalProperty : 'totalCount'
        }, runGridList)
    });
        
    // 初始化时,显示所有数据
	runGridStore.load({
		params : {
			start : 0,
			limit : PAGE_SIZE
		}
	});   
	
    // 运行执行的Grid主体
    var runGrid = new Ext.grid.GridPanel({     
        store : runGridStore,
        columns : [
                // 自动生成行号
				new Ext.grid.RowNumberer({
					header : '行号',
					width : 35
				}),{
                    header : '基准货币',
                    width : 40,
                    align : 'left',
                    sortable : true,
                    dataIndex : 'oriCurrencyName' 
                }, {
                    header : '比较货币',
                    width : 40,
                    align : 'left',
                    sortable : true,
                    dataIndex : 'dstCurrencyName' 
                }, {
                    header : '汇率',
                    width : 40,
                    align : 'left',
                    sortable : true,
                    dataIndex : 'rate',
                    renderer : numberFormat
                },{
                    header : '有效开始日期',
                    width : 40,
                    align : 'left',
                    sortable : true,
                    dataIndex : 'effectiveDate' 
                },{
                    header : '有效截止日期',
                    width : 40,
                    align : 'left',
                    sortable : true,
                    dataIndex : 'discontinueDate' 
                }, {
                    header : '汇率流水号',
					hidden : true,
					dataIndex : 'exchangeRateId'
				}, {
				    header : '基准货币流水号',
					hidden : true,
					dataIndex : 'oriCurrencyId'
				}, {
				    header : '比较货币流水号',
					hidden : true,
					dataIndex : 'dstCurrencyId'
				}],
        viewConfig : {
            forceFit : true
        },
        tbar : [ addBtn,
            	{xtype : "tbseparator"}, updateBtn,
            	{xtype : "tbseparator"}, deleteBtn],
        //分页
        bbar : new Ext.PagingToolbar({
            pageSize : PAGE_SIZE,
            store : runGridStore,
            displayInfo : true,
            displayMsg : Constants.DISPLAY_MSG,
            emptyMsg : Constants.EMPTY_MSG
        }),  
       sm : new Ext.grid.RowSelectionModel({singleSelect : true}),
       frame : false,
       border : false,
       enableColumnHide : true,
       enableColumnMove : false
    });
    
	// 注册双击事件
 	runGrid.on("rowdblclick", updateRecord);
    
    // 设定布局器及面板
	new Ext.Viewport({
        enableTabScroll : true,   
        layout : "border",
        items : [{
			xtype : "panel",
			region : 'center',
			layout : 'fit',
			border : false,
			items : [runGrid]
		}]
    });    
    	
 	// 定义form中的addFlag:新增：1 修改：0
	var addFlag = {
		id : "addFlag",
		xtype : "hidden",
		value : "",
		name : "addFlag"
	}
	// 定义form中的比较货币 下拉框数据源
	var currencyNameStore = new Ext.data.JsonStore({
		// 获得货币名称列表action
        url : 'resource/getCurrencyNameList.action',
        root : 'list',  
        fields : [{name : 'currencyName'},
            {name : 'currencyId'}]      
    })	
    // 定义form中的基准货币 下拉框数据源
	var oricurrencyNameStore = new Ext.data.JsonStore({
	    // 获得货币名称列表action
        url : 'resource/getCurrencyNameList.action',
        root : 'list',  
        fields : [{name : 'currencyName'},
            {name : 'currencyId'}]      
    })
    currencyNameStore.load();
    oricurrencyNameStore.load();
    // 基准货币 下拉框
    var oriCurrencyNameCbo = new Ext.form.ComboBox({ 
    	fieldLabel:"基准货币<font color='red'>*</font>",  
    	id : "oriCurrencyNameCbo",
        allowBlank : false,
        triggerAction : 'all',
        store : oricurrencyNameStore,
        displayField : 'currencyName',
        valueField : 'currencyId',
        mode : 'local',
  		activeitem : 0,
        readOnly : true,
        width : 180,
        hiddenName : "rateBeen.oriCurrencyId"
    })
    // 比较货币 下拉框
    var dstCurrencyNameCbo = new Ext.form.ComboBox({    
    	fieldLabel:"比较货币<font color='red'>*</font>",
    	id :"dstCurrencyNameCbo",
        allowBlank : false,
        triggerAction : 'all',
        store : currencyNameStore,
        displayField : 'currencyName',
        valueField : 'currencyId',
        mode : 'local',
  		activeitem : 0,
        readOnly : true,
        width : 180,
        hiddenName : "rateBeen.dstCurrencyId"
    })
    // 汇率
    var rateTxt = new Powererp.form.NumField({
      allowNegative : false,
      width : 180,
      fieldLabel:"汇率<font color='red'>*</font>",
      maxValue : 99999999999.9999,
      minValue : 0.0001,
      id : 'rate',
      name : 'rate',
      allowBlank : false,
      hiddenName : 'rateBeen.rate',
      decimalPrecision : 4,
      padding : 4
    })

    // 有效开始日期
    var effectiveDate = new Ext.form.TextField({
        id : 'effectiveDate',
        fieldLabel : "有效开始日期<font color='red'>*</font>",
        name : 'rateBeen.effectiveDate',
        style : 'cursor:pointer',
        width : 180,
        readOnly:true,
        allowBlank : false,
        listeners : {
            focus : function() {
                WdatePicker({
                    startDate : '%y-%M-%d',
                    dateFmt : 'yyyy-MM-dd',
                    alwaysUseStartDate : false,
                    onpicked : function(){
                        effectiveDate.clearInvalid();
                    },
                    onclearing:function(){
                    	effectiveDate.markInvalid();
                    }
                });
            }
        }
    })
    
    // 有效截止日期
    var discontinueDate = new Ext.form.TextField({
        id : 'discontinueDate',
        fieldLabel : "有效截止日期<font color='red'>*</font>",
        name : 'rateBeen.discontinueDate',
        style : 'cursor:pointer',
        width : 180,
        readOnly:true,
        allowBlank : false,
        listeners : {
            focus : function() {
                WdatePicker({
                    startDate : '%y-%M-%d',
                    dateFmt : 'yyyy-MM-dd',
                    alwaysUseStartDate : false,
                    onpicked : function(){
                        discontinueDate.clearInvalid();
                    },
                    onclearing:function(){
                    	discontinueDate.markInvalid();
                    }
                });
            }
        }
    })
    // 定义form中的隐藏的流水号
	var exchangeRateId = {
		id : "exchangeRateId",
		xtype : "hidden",
		name : 'rateBeen.exchangeRateId'
	}	 
	// 定义弹出窗体中的form
	var mypanel = new Ext.FormPanel({
				labelAlign : 'right',
				autoHeight : true,
				frame : true,
				items : [oriCurrencyNameCbo, dstCurrencyNameCbo, rateTxt, effectiveDate, discontinueDate, exchangeRateId,  addFlag]
			});
	// 定义弹出窗体
	var win = new Ext.Window({
				width : 350,
				autoHeight : 100,				
				buttonAlign : "center",
				items : [mypanel],
				layout : 'fit',
				closeAction : 'hide',
				modal : true,
				resizable : false,
				buttons : [{
					text : Constants.BTN_SAVE,
					iconCls : Constants.CLS_SAVE,
					handler : function() {
							// 设定action的url
							var myurl = "";
							if(Ext.get("addFlag").dom.value == "1") {
								myurl = "resource/addExchangeRate.action";
							} else {
								myurl = "resource/updateExchangeRate.action";								
							}		
						    Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_C_001, function(
					            buttonobj) {
					            if (buttonobj == "yes") {
					            	if(!canSave()){
					            	    return;					            	
					            	}
									mypanel.getForm().submit({
										method : 'POST',
										url : myurl,							    
										success : function(form,action) {										    
								            var result = eval("(" + action.response.responseText + ")");                                     	
                                          	if(result.success){
                                          		if(result.flag == 1){
                                          			  // 相同的基准货币和兑换货币在相同时段内不能有不同汇率。
                                          			  Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.RI008_E_002);
						                              return;
                                          		}
                                          		if(result.flag == 0){
                                          			// 保存成功
                                          			Ext.Msg.alert(Constants.SYS_REMIND_MSG,Constants.COM_I_004);
                                          		}
                                          		if(result.flag == 2){
                                          		    // 保存失败
            		                                Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_E_014);
                                          	    }
                                          	}
										    runGridStore.load({
													params : {
														start : 0,
														limit : Constants.PAGE_SIZE												
													}
												});
										    win.hide();
										},
										faliue : function() {
											Ext.Msg.alert(Constants.SYS_REMIND_MSG,
													Constants.UNKNOWN_ERR);
										}								
								    });	
								}
							});							
						}
					},{
					text : Constants.BTN_CANCEL,
					iconCls : Constants.CLS_CANCEL,
					handler : function() {
							win.hide();
						}
					}]
			});	
			
    /**
     * 增加函数
     */
	function addRecord() {
		mypanel.getForm().reset();
		win.setTitle("新增汇率");
		win.show();
	    currencyNameStore.load();
		Ext.get("addFlag").dom.value = "1";
	}
	/**
	 * 修改函数
	 */
	function updateRecord() {
		// 是否有被选项
		if (runGrid.selModel.hasSelection()) {
			var records = runGrid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert(Constants.SYS_REMIND_MSG,
						Constants.SELECT_COMPLEX_MSG);
			} else {
				var record = runGrid.getSelectionModel().getSelected();	
				win.setTitle("修改汇率");			
				win.show();			
				mypanel.getForm().loadRecord(record);
				Ext.get("addFlag").dom.value = "0";
				// 修改画面的基准货币，比较货币
				dstCurrencyNameCbo.setValue(record.get('dstCurrencyId'));
				oriCurrencyNameCbo.setValue(record.get('oriCurrencyId'));
				// 设定汇率
				rateTxt.setValue(record.get('rate'));
				// 设定流水号
				Ext.get("exchangeRateId").dom.value = record.get('exchangeRateId');				
			}
		} else {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG,
					Constants.COM_I_001);
		}		
	}
	/**
	 * 删除函数
	 */
	function deleteRecord() {
		// 是否有被选项
		if (runGrid.selModel.hasSelection()) {
		    var records = runGrid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert(Constants.SYS_REMIND_MSG,
						Constants.SELECT_COMPLEX_MSG);
			} else {
				var record = runGrid.getSelectionModel().getSelected();
				Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_C_002, function(
					buttonobj) {
					if (buttonobj == "yes") {
						Ext.lib.Ajax.request(Constants.POST,
								'resource/deleteExchangeRate.action', {
									success : function(action) {
										// 删除成功
								 	    Ext.Msg.alert(Constants.SYS_REMIND_MSG,Constants.COM_I_005);
										runGridStore.load({
												params : {
													start : 0,
													limit : Constants.PAGE_SIZE													
												}
											});
									},
									failure : function() {
										Ext.Msg.alert(Constants.SYS_REMIND_MSG,
											Constants.DEL_ERROR);
										}
									}, 'rateBeen.exchangeRateId=' + record.get('exchangeRateId'));							
						}
				})				
			}
		} else {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG,
					Constants.COM_I_001);
		}	
	}
	/**
	 * 在保存时check输入
	 */
	function canSave() {		
		var nullMsg = "";
	    var oriCurrency = oriCurrencyNameCbo.value;
		var dstCurrency = dstCurrencyNameCbo.value;
		var rate = Ext.get("rate").dom.value;
		var strStartDate = Ext.get("effectiveDate").dom.value;
		var strEndDate = Ext.get("discontinueDate").dom.value;
		// 全部不能为空
		if(oriCurrency == null || oriCurrency == "") {
			nullMsg = nullMsg + String.format(Constants.COM_E_003,"基准货币") + "<br/>";
		    oriCurrencyNameCbo.markInvalid();
		}
		if(dstCurrency == null || dstCurrency == "") {
			nullMsg = nullMsg + String.format(Constants.COM_E_003,"比较货币") + "<br/>";
		    dstCurrencyNameCbo.markInvalid();
		}		
		if(rate == null || rate == "") {
			nullMsg = nullMsg + String.format(Constants.COM_E_002,"汇率") + "<br/>";
		    rateTxt.markInvalid();
		}
		if(strStartDate == null || strStartDate == "") {
			nullMsg = nullMsg + String.format(Constants.COM_E_003,"有效开始日期") + "<br/>";
		    effectiveDate.markInvalid();
		}else{
			effectiveDate.clearInvalid();
		}
		if(strEndDate == null || strEndDate == "") {
			nullMsg = nullMsg + String.format(Constants.COM_E_003,"有效截止日期");
		    discontinueDate.markInvalid();
		}else{
			discontinueDate.clearInvalid();
		}
		if(nullMsg != "") {
            Ext.Msg.alert(Constants.SYS_REMIND_MSG,	nullMsg);
            return false;
        }
        // 基准货币与比较货币不能相同
        if(oriCurrency == dstCurrency) {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.RI008_E_001);
			return false;
		}
		// 汇率大小判断
		if(rate <= 0) {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, String.format(Constants.COM_E_013,"汇率"));
			return false;
		}
		if(rate > 99999999999.9999) {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, String.format(Constants.COM_E_006,"汇率","99999999999.9999"));
			return false;
		}
	    // 有效日期前后判断
		if(strStartDate != "" && strEndDate != "") {
			var dateStart = Date.parseDate(strStartDate, 'Y-m-d');
			var dateEnd = Date.parseDate(strEndDate, 'Y-m-d');
			if (dateStart.getTime() > dateEnd.getTime()) {
				Ext.Msg.alert(Constants.SYS_REMIND_MSG, String.format(Constants.COM_E_005,"有效截止日期","有效开始日期"));
				return false;
			} else {
				return true;
			}
		}
		return true;
	}

	/**
	 * 大数字中间用','分隔 小数点后3位
	 */
	function numberFormat(value){
		    value = String(value);
            // 整数部分
            var whole = value;
            // 小数部分
            var sub = ".0000";
            // 如果有小数
		    if (value.indexOf(".") > 0) {
		    	whole = value.substring(0, value.indexOf("."));
			    sub = value.substring(value.indexOf("."), value.length);
			    sub = sub + "0000";
			    if(sub.length > 5){
			    	sub = sub.substring(0,5);
			    }
		    }
            var r = /(\d+)(\d{3})/;
            while (r.test(whole)){
                whole = whole.replace(r, '$1' + ',' + '$2');
            }
            v = whole + sub;
            return v;	
	}
});