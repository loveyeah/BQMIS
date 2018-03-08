Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	var args = window.dialogArguments;
	var forecastTime = args.forecastTime;
	function numberFormat(value) {
		value = String(value);
		if (value == null || value == "null") {
			value = "0";
		}
		// 整数部分
		var whole = value;
		// 小数部分
		var sub = ".00";
		// 如果有小数
		if (value.indexOf(".") > 0) {
			whole = value.substring(0, value.indexOf("."));
			sub = value.substring(value.indexOf("."), value.length);
			sub = sub + "00";
			if (sub.length > 3) {
				sub = sub.substring(0, 3);
			}
		}
		var r = /(\d+)(\d{3})/;
		while (r.test(whole)) {
			whole = whole.replace(r, '$1' + ',' + '$2');
		}
		v = whole + sub;
		if (whole == null || whole == "null" || whole == "") {
			v = "0.00";
		}
		return v;
	}
	// 创建一个对象
	var record = new Ext.data.Record.create([{
		
		name : 'forecastId'
	},{
		
		name : 'modelItemId'
	},{

		name : 'forecastTime'
	},{

		name : 'forecastValue'
	},{

		name : 'modelType'
	},{

		name : 'modelItemCode'
	},{

		name : 'modelItemName'
	},{

		name : 'unitName'
	},{

		name : 'firstValue'
	},{
		name : 'secondValue'
	},{
		name : 'thirdValue'
	},{
		name : 'forthValue'
	},{
		name : 'averageValue'
	}])
	
	// 配置数据集
	var store = new Ext.data.Store({
		/* 创建代理对象 */
		proxy : new Ext.data.HttpProxy({
			url : 'managebudget/getForcastModelRecords.action'
		}),
		/* 创建解析Json格式数据的解析器 */
		reader : new Ext.data.JsonReader({
			root : "list",
			totalProperty : "totalCount"
		}, record)
	});
	
	var cm = new Ext.grid.ColumnModel([
		new Ext.grid.RowNumberer({
			header : "行号",
			width : 31
			}),{
				header : '预测指标',
				align : 'center',
				width : 100,
				sortable : true,
				dataIndex : 'modelItemName'
			}, {
				header : '计量单位',
				align : 'center',
				width : 100,
				sortable : true,
				dataIndex : 'unitName'
			}, {
				header : '正向利润预测',
				align : 'center',
				width : 90,
				sortable : true,
				dataIndex : 'firstValue',
				renderer : function(value) {
					return numberFormat(value);
				}
			},{			
				header : '反向电量预测',
				align : 'center',
				width : 90,
				sortable : true,
				dataIndex : 'secondValue',
				renderer : function(value) {
					return numberFormat(value);
				}
			},{			
				header : '反向固本预测',
				align : 'center',
				width : 90,
				sortable : true,
				dataIndex : 'thirdValue',
				renderer : function(value) {
					return numberFormat(value);
				}
			},{			
				header : '反向变本预测',
				align : 'center',
				width : 90,
				sortable : true,
				dataIndex : 'forthValue',
				renderer : function(value) {
					return numberFormat(value);
				}
			},{			
				header : '平均值',
				align : 'center',
				width : 90,
				sortable : true,
				dataIndex : 'averageValue',
				renderer : function(value) {
					return numberFormat(value);
				}
			}]);

	// 底部分页栏
	var bbar = new Ext.PagingToolbar({
		pageSize : 18,
		store : store,
		displayInfo : true,
		displayMsg : "{0} 到 {1} /共 {2} 条",
		emptyMsg : "没有记录"
	});
	var sm = new Ext.grid.CheckboxSelectionModel();
	// 可编辑的表格
	var Grid = new Ext.grid.GridPanel({
		sm : sm,
		ds : store,
		cm : cm,
		title : '预测时间：' + forecastTime + "指标数据",
		autoScroll : true,
		bbar : bbar,
		border : true,
		clicksToEdit : 1,
		viewConfig : {
		 forceFit : true
		}
	});
	function queryRecord() {
		
		store.baseParams = {
			forecastTime :  forecastTime
		};
		store.load({
			params : {
				start : 0,
				limit : 18
			}
		});
	}
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		border : false,
		frame : false,
		items : [{
			layout : 'fit',
			border : false,
			frame : false,
			region : "center",
			items : [Grid]
		}]
	});
	queryRecord();
});