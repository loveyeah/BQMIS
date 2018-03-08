Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function(){
	
	//add by fyyang 091223-------
			function ChangeDateToString(DateIn) {
		var Year = 0;
		var Month = 0;
		var Day = 0;
		var CurrentDate = "";
		Year = DateIn.getYear();
		Month = DateIn.getMonth() + 1;
		Day = DateIn.getDate();
		CurrentDate = Year + "-";
		if (Month >= 10) {
			CurrentDate = CurrentDate + Month + "-";
		} else {
			CurrentDate = CurrentDate + "0" + Month + "-";
		}
		if (Day >= 10) {
			CurrentDate = CurrentDate + Day;
		} else {
			CurrentDate = CurrentDate + "0" + Day;
		}
		return CurrentDate;
	}
	var enddate = new Date();
	var startdate = enddate.add(Date.MONTH, -1);
	startdate = startdate.getFirstDateOfMonth();
	var sdate = ChangeDateToString(startdate);
	var edate = ChangeDateToString(enddate);
			var timefromDate = new Ext.form.TextField({
		id : 'timefromDate',
		name : '_timefromDate',
		fieldLabel : "开始",
		style : 'cursor:pointer',
		cls : 'Wdate',
		width : 90,
		value : sdate,
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-01',
					dateFmt : 'yyyy-MM-dd',
					alwaysUseStartDate : true
				});
			}
		}
	});
	var timetoDate = new Ext.form.TextField({
		name : '_timetoDate',
		value : edate,
		id : 'timetoDate',
		fieldLabel : "结束",
		style : 'cursor:pointer',
		cls : 'Wdate',
		width : 90,
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-01',
					dateFmt : 'yyyy-MM-dd',
					alwaysUseStartDate : true
				});
			}
		}
	});
		//-add end----------------	
	/**
	 * 金钱格式化
	 */
	function moneyFormat(v) {
		if (v == null || v == "") {
			return "0.0000";
		}
		v = (Math.round((v - 0) * 10000)) / 10000;
		v = (v == Math.floor(v))
				? v + ".0000"
				: ((v * 10 == Math.floor(v * 10))
						? v + "000"
						: ((v * 100 == Math.floor(v * 100)) ? v + "00" : ((v
								* 1000 == Math.floor(v * 1000)) ? v + "0" : v)));
		v = String(v);
		var ps = v.split('.');
		var whole = ps[0];
		var sub = ps[1] ? '.' + ps[1] : '.0000';
		var r = /(\d+)(\d{3})/;
		while (r.test(whole)) {
			whole = whole.replace(r, '$1' + ',' + '$2');
		}
		v = whole + sub;
		if (v.charAt(0) == '-') {
			return '-' + v.substr(1);
		}
		return v;
	}
	
	/**
	 * 数字格式化  保留两位小数
	 */
	function numberFormat(v) {
		if (v == null || v == "") {
			return "0.00";
		}
		v = (Math.round((v - 0) * 100)) / 100;
		v = (v == Math.floor(v)) ? v + ".00" : ((v * 10 == Math.floor(v * 10))
				? v + "0"
				: v);
		v = String(v);
		var ps = v.split('.');
		var whole = ps[0];
		var sub = ps[1] ? '.' + ps[1] : '.00';
		var r = /(\d+)(\d{3})/;
		while (r.test(whole)) {
			whole = whole.replace(r, '$1' + ',' + '$2');
		}
		v = whole + sub;
		if (v.charAt(0) == '-') {
			return '-' + v.substr(1);
		}
		return v;
	}
	var llNumber = new Ext.form.TextField({
				name : 'llNumber',
				fieldLabel : '领料单号'
			});
			
	var lyDept = new Ext.form.TextField({
				name : 'lyDept',
				fieldLabel : '领用部门'
			});
	
			
	// 物料详细记录
	var material = Ext.data.Record.create([
//		{
				// 领料单明细id
//				name : 'issueDetailsId'
//			},
//			// 领料单编号
//			{
//				name : "issueNo"
//			},
			// 物料id
			{
				name : "materialId"
			},
			// 物料编码
			{
				name : 'materialNo'
			},
			// 物料名称
			{
				name : 'materialName'
			},
			// 规格型号
			{
				name : 'specNo'
			},
//			// 材质/参数
//			{
//				name : 'parameter'
//			},
//			// 存货计量单位id
//			{
//				name : 'stockUmId'
//			},
//			// 存货计量单位名称
//			{
//				name : "unitName"
//			},
//			// 申请数量
//			{
//				name : 'appliedCount'
//			},
//			// 核准数量
//			{
//				name : 'approvedCount'
//			},
//			// 物资需求明细id
//			{
//				name : "requirementDetailId"
//			},
			// 数量
			{
				name : 'qty'
			},
//			// 待发货数量
//			{
//				name : 'waitCount'
//			},
			// 单价
			{
				name : 'stdCost'
			},
			// 金额
			{
				name : "price"
			},// 本期初始
			{
				name : "openBalance"
			},// 当前结余
			{
				name : "thisQty"
			}]);
	// 物料grid的store
	var materialStore = new Ext.data.JsonStore({
			url : 'resource/getSendMaterialsAccout.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : material
			});
			
	materialStore.on('load',function(){
		if(materialStore.getTotalCount() > 0){
			var obj = new material({
				materialId : null,
				materialNo : '合计',
				materialName : null,
				specNo : null,
				qty : 0,
				stdCost : null,
				price : 0,
				openBalance : 0,
				thisQty : 0
			});
			
			for(var i = 0 ;i <= materialStore.getTotalCount() - 1; i++){
				obj.set('qty',obj.get('qty') + materialStore.getAt(i).get('qty'));
				obj.set('price',obj.get('price') + materialStore.getAt(i).get('price'));
				obj.set('openBalance',obj.get('openBalance') + materialStore.getAt(i).get('openBalance'));
				obj.set('thisQty',obj.get('thisQty') + materialStore.getAt(i).get('thisQty'));
			}
			
			materialStore.add(obj);
		}
	})
	var queryRec = function(){
		materialStore.baseParams = {
		sdate:timefromDate.getValue(),
			edate:timetoDate.getValue()
		}
		
//		materialStore.load({
//			params : {
//				start : 0,
//				limit : 18
//			}
//		})
		materialStore.load();
	}
	// 返回
	var btnReturn = new Ext.Toolbar.Button({
		text : "返回",
		iconCls : Constants.CLS_QUERY,
		handler : function() {
			window.location.replace("../purAndIssueCheckQuery/issueTab.jsp");
		}
	});
	// 物料grid
	var materialGrid = new Ext.grid.GridPanel({
				border : false,
				autoScroll : true,
				enableColumnMove : false,
				// 单选
				sm : new Ext.grid.RowSelectionModel({
							singleSelect : true
						}),
				// 单击修改
				store : materialStore,
				tbar : ['审核时间：',timefromDate,'~',timetoDate,'-', 
				{
				 text:'查询',
				 iconCls:'query',
				 handler:queryRec
				},
				
				btnReturn],
//				bbar : new Ext.PagingToolbar({
//							pageSize : Constants.PAGE_SIZE,
//							store : materialStore,
//							displayInfo : true,
//							displayMsg : "显示 {0} 条到 {1} 条,共 {2} 条",
//							emptyMsg : Constants.EMPTY_MSG
//						}),
				columns : [new Ext.grid.RowNumberer({
									header : "行号",
									width : 35
								}),
						
						// 
						{
							header : "物料编码",
							sortable : true,
							width : 75,
							dataIndex : 'materialNo'
						},
						// 物料名称
						{
							header : "物料名称",
							sortable : true,
							width : 75,
							dataIndex : 'materialName'
						},
						// 规格型号
						{
							header : "规格型号",
							sortable : true,
							width : 75,
							dataIndex : 'specNo'
						},
						
						// 数量
						{
							header : "数量",
							sortable : true,
							align : "right",
							renderer : moneyFormat,
							width : 75,
							dataIndex : 'qty'
						},{
							header : "本期初始",
							sortable : true,
							align : "right",
							renderer : moneyFormat,
							width : 75,
							dataIndex : 'openBalance'
						},{
							header : "当前结余",
							sortable : true,
							align : "right",
							renderer : moneyFormat,
							width : 75,
							dataIndex : 'thisQty'
						},
						{
							header : "单价",
							sortable : true,
							width : 75,
							align : "right",
							renderer : numberFormat,
							dataIndex : 'stdCost'
						},{
							header : "金额",
							sortable : true,
							width : 100,
							align : "right",
							renderer : numberFormat,
							dataIndex : 'price'
						}
						]

			});
			
	new Ext.Viewport({
				layout : "border",
				items : [{
					region : 'center',
					layout :'fit',
					items : [materialGrid]
				}]
			});
	queryRec()	;
})