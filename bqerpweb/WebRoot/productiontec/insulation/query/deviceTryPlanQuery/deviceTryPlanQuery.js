// 绝缘设备预试计划

Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.QuickTips.init();
Ext.onReady(function() {
	var strMethod;
	var strMsg;
	// ============== 定义grid ===============
	var record = Ext.data.Record.create([
	
		{
			// 设备序号
			name : 'pjj.deviceId'
		},
		{
			// 设备名称
			name : 'pjj.deviceName'
		},
		{
			//  检验周期（月）
			name : 'pjj.testCycle'
		},
		{
			// 制造厂家
			name : 'pjj.factory'
		},
		{
			// 型号
			name : 'pjj.sizes'
		},
		{
			// 量程
			name : 'pjj.userRange'
		},
		{
			// 电压
			name : 'pjj.voltage'
		},
		{
			// 备注
			name : 'pjj.memo'
		},
		{
			// 单位编码
			name : 'pjj.enterpriseCode'
		},
		{
			// 下次试验时间
			name : 'nextDate'
		},
		{
			// 最近试验人员
			name : 'operateBy'
		},
		{
			// 最近试验人员姓名
			name : 'operateName'
		},
		{
			// 最近试验日期
			name : 'operateDate'
		}
		])

	// 定义获取数据源
	var dataProxy = new Ext.data.HttpProxy({
				url : 'productionrec/findDeviceTryList.action'
			});

	// 定义格式化数据
	var theReader = new Ext.data.JsonReader({
				root : "list",
				totalProperty : "totalCount"
			}, record);

	// 定义封装缓存数据的对象
	var queryStore = new Ext.data.Store({
				// 访问的对象
				proxy : dataProxy,
				// 处理数据的对象
				reader : theReader
			});
	queryStore.load({
				params : {
					start : 0,
					limit : Constants.PAGE_SIZE
				}
			});

	// -----------------控件定义----------------------------------
	// 查询参数
	var txtFuzzy = new Ext.form.TextField({
				id : "fuzzy",
				name : "pjj.fuzzy",
				width : 235,
				emptyText : "（设备名称）"
			});

	//定义选择列
    var check = new Ext.grid.CheckboxSelectionModel({
    		singleSelect : false
    });
	// --gridpanel显示格式定义-----开始-------------------
	var grid = new Ext.grid.GridPanel({
				region : "center",
				layout : 'fit',
				store : queryStore,
				stripeRows : true,
				columns : [
				 		check,
						//自动行号
						new Ext.grid.RowNumberer({
									header : "行号",
									width : 31
								}),{
									header : "序号",
									dataIndex : 'pjj.deviceId',
									hidden : true,
									sortable : true
								},
								{
							header : "设备名称",
							dataIndex : 'pjj.deviceName',
							sortable : true
						}, {
							header : "试验周期（月）",
							dataIndex : 'pjj.testCycle',
							sortable : true
						}, {
							header : "制造厂家",
							dataIndex : 'pjj.factory',
							hidden : true,
							sortable : true
						},{
							header : '型号',
							dataIndex : 'pjj.sizes',
							sortable : true
						},{
							header : '量程',
							hidden : true,
							dataIndex : 'pjj.userRange',
							sortable : true
						},{
							header : '电压',
							dataIndex : 'pjj.voltage',
							hidden : true,
							sortable : true
						},{
							header : '最近试验日期',
							dataIndex : 'operateDate',
							sortable : true
						},{
							header : '最近试验人员',
							dataIndex : 'operateName',
							sortable : true
						},{
							header : '下次试验日期',
							dataIndex : 'nextDate',
							sortable : true
						},{
							header : '备注',
							dataIndex : 'pjj.memo',
							sortable : true
						}],
				sm : check,
				autoScroll : true,
				enableColumnMove : false,
				autoSizeColumns : true,
				viewConfig : {
					forceFit : false
				},

				// 头部工具栏
				tbar : [ {
							text : "模糊查询:"
						},txtFuzzy,{
							text : "查询",
							iconCls : Constants.CLS_QUERY,
							handler : queryRecord
						}
						
						],

				// 分页
				bbar : new Ext.PagingToolbar({
							pageSize : Constants.PAGE_SIZE,
							store : queryStore,
							displayInfo : true,
							displayMsg : Constants.DISPLAY_MSG,
							emptyMsg : Constants.EMPTY_MSG
						})
			});
	// --gridpanel显示格式定义-----结束--------------------





	/**
	 * 
	 *  查询记录 
	 */
	function queryRecord() {
		// 查询参数
		var name = txtFuzzy.getValue();
		queryStore.baseParams = {
			name : name
		};
		queryStore.load({
					params : {
						start : 0,
						limit : Constants.PAGE_SIZE
					}
				});
		grid.getView().refresh();
	}
			

	/**
	 * 页面加载显示数据
	 */
	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				items : [grid]

			});

		
})









							