// 画面：全部定期工作
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {
	// 主画面

    
	// 工作类别
	var workCmb = new Ext.form.CmbSubWorkType({
				id : 'workType',
				fieldLabel : "工作类别",
				labelAlign : 'right',
				width : 180,
				anchor : '100%',
				emptyText : '',
				name : 'workType'				
			})
	workCmb.store.load();
	// 隐藏登陆用户权限信息	
	var hiddenMan = new Ext.form.Hidden({
				id : 'hiddMan',
				value : '0'
			});
	// 取得登陆用户权限信息
	Ext.Ajax.request({
				url : "administration/getUserWorkTypeInfo.action",
				method : "post",
				success : function(result, request) {
					var data = eval("(" + result.responseText + ")");
					hiddenMan.setValue(data.workType);					
				}
			});	
	// 查询按钮
	var btnQuery = new Ext.Button({
						id : 'query',
						text : Constants.BTN_QUERY,
						iconCls : Constants.CLS_QUERY,
						handler : function() {
							storeInfoGrid.load({
										params : {
											start : 0,
											limit : Constants.PAGE_SIZE
										}
									});
						}
					});
  
	// 打印按钮
	var btnPrint = new Ext.Button({
				id : 'print',
				text : Constants.BTN_PRINT,
				iconCls : Constants.CLS_PRINT,
				disabled : true,				
				handler : printRecord
			});
	// 关闭按钮
	var btnClose = new Ext.Button({
						id : 'close',
						text : Constants.BTN_CLOSE,	
						iconCls:Constants.CLS_CANCEL,
						handler : function() {
							window.close();
						}
					});
	// grid中的数据
	var recordInfoGridList = new Ext.data.Record.create([{
				name : 'workitemName'
			}, {
				name : 'workrangeType'
			}, {
				name : 'startTime'
			}, {
				name : 'startWeek'
			}, {
				name : 'workExplain'
			}, {
				name : 'ifWeekend'
			}]);

	// grid中的store
	var storeInfoGrid = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'administration/getAllRegularQuery.action'
						}),
				reader : new Ext.data.JsonReader({
							root : 'list',
							totalProperty : 'totalCount'
						}, recordInfoGridList)
			});

	// before load事件,传递查询字符串作为参数
	storeInfoGrid.on('beforeload', function() {
				Ext.apply(this.baseParams, {
							fuzzy : workCmb.getValue()
						});
			});
    // 按钮操作
	storeInfoGrid.on("load", function() {
		if (storeInfoGrid.getCount() > 0) {
			btnPrint.setDisabled(false);
		} else{
			btnPrint.setDisabled(true);
		}
	});
	// 检索无数据
	storeInfoGrid.on("load",function(){
	  if(storeInfoGrid.getTotalCount() <= 0){
	  	Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,MessageConstants.COM_I_003);
	  }
	})

	// 运行执行的Grid主体
	var gridInfo = new Ext.grid.GridPanel({
				store : storeInfoGrid,
				columns : [
						// 自动生成行号
						new Ext.grid.RowNumberer({
									header : '行号',
									width : 35
								}), {
							header : '工作名称',
							width : 30,
							align : 'left',
							sortable : true,
							dataIndex : 'workitemName'
						}
						, {
							header : '周期类别',
							width : 30,
							align : 'left',
							sortable : true,
							dataIndex : 'workrangeType'
						}, {
							header : '计划开始时间',
							width : 30,
							align : 'left',
							sortable : true,
							renderer: renderDate,
							dataIndex : 'startTime'
						}, {
							header : '星期',
							width : 20,
							align : 'left',
							sortable : true,
							dataIndex : 'startWeek'
						}, {
							header : '具体工作内容',
							width : 30,
							align : 'left',
							sortable : true,
							dataIndex : 'workExplain'
						}, {
							header : '节假日是否工作',
							width : 30,
							align : 'left',
							sortable : true,
							dataIndex : 'ifWeekend'
						}],
				viewConfig : {
					forceFit : true
				},
				tbar : ['工作类别:',workCmb,'-', btnQuery, btnPrint, btnClose],
				// 分页
				bbar : new Ext.PagingToolbar({
							pageSize : Constants.PAGE_SIZE,
							store : storeInfoGrid,
							displayInfo : true,
							displayMsg : MessageConstants.DISPLAY_MSG,
							emptyMsg : MessageConstants.EMPTY_MSG
						}),
				sm : new Ext.grid.RowSelectionModel({
							singleSelect : true
						}),
				frame : false,
				border : false,
				enableColumnHide : true,
				enableColumnMove : false
			});


	// 设定布局器及面板
	new Ext.Viewport({
				enableTabScroll : true,
				layout : "fit",
				items : [gridInfo]
			});
	gridInfo.focus();

	// ↑↑********** 主画面*******↑↑//

	
	// ↓↓*******************************处理****************************************
			
	/**
	 * 打印函数
	 */
	function printRecord() {
		// 打印操作（共通）
		// TODO	
		window.open("/power/report/webfile/administration/ARF01.jsp?workTypeCode="
						+ hiddenMan.getValue()
						+ "&subWorkTypeCode="
						+ workCmb.getValue()+"&subWorkTypeName="
						+Ext.get('workType').dom.value);
	}
	/**
	 * 去掉时间中T和秒
	 */
	function renderDate(value) {
		if (!value)
			return "";
		var reDate = /\d{4}\-\d{2}\-\d{2}/gi;
		var reTime = /\d{2}:\d{2}/gi;

		var strDate = value.match(reDate);
		var strTime = value.match(reTime);
		if (!strDate)
			return "";
		return strTime ? strDate + " " + strTime : strDate;
	}
});