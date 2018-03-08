Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

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
	var fromMonth = new Ext.form.TextField({
		name : '_fromMonth',
		value : exdate,
		id : 'fromMonth',
		style : 'cursor:pointer',
		hideLabel : true,
		cls : 'Wdate',
		width : 150,
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
	var toMonth = new Ext.form.TextField({
		name : '_toMonth',
		value : exdate,
		id : 'toMonth',
		style : 'cursor:pointer',
		hideLabel : true,
		cls : 'Wdate',
		width : 150,
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
	/**
	 * 年份Field
	 */
	var d = new Date();
	year = d.getFullYear();
	var fromYear = new Ext.form.TextField({
		id : 'fromYear',
		name : '_fromYear',
		style : 'cursor:pointer',
		hideLabel : true,
		cls : 'Wdate',
		width : 150,
		value : year,
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
	var toYear = new Ext.form.TextField({
		id : 'toYear',
		name : '_toYear',
		style : 'cursor:pointer',
		hideLabel : true,
		cls : 'Wdate',
		width : 150,
		value : year,
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

	var monthBox = new Ext.form.Checkbox({
		id : 'monthBox',
		name : 'queryWayBox',
		hideLabel : true,
		checked : true,
		boxLabel : '月份区间:'
	});

	var yearBox = new Ext.form.Checkbox({
		id : 'yearBox',
		name : 'queryWayBox',
		hideLabel : true,
		boxLabel : '年份区间:'
	});
	
	// 数据来源stroe
	var sysSourceStore = new Ext.data.SimpleStore({
		fields : ['id', 'name'],
		data : [['', '所有'],['1', '计划统计'], ['2', '燃料管理'], ['3', '物资管理'],
				['4', '工程项目'], ['5', '人力资源']]
	})
	
	var sysSourceCombox = new Ext.form.ComboBox({
				fieldLabel : '数据来源',
				store : sysSourceStore,
				id : 'sysSource',
				name : 'sysSource',
				valueField : "id",
				displayField : "name",
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : true,
				emptyText : '请选择',
				anchor : '92.5%',
				value : ''
			});

	function validate() {
		var msg = "";
		if (monthBox.checked) {
			if (fromMonth.getValue() > toMonth.getValue())
				msg += "选择结束月份应大于等于开始月份;<br>";
		} else if (yearBox.checked) {
			if (fromYear.getValue() > toYear.getValue())
				msg += "选择结束年份应大于等于开始年份;";
		} else {
			if(!monthBox.checked) {
				msg += "选择需要采集的月份区间;";
			} else if(!yearBox.checked) {
				msg += "选择需要采集的年份区间;";
			}
		}
		if (msg == "") {
			return true;
		} else {
			Ext.Msg.alert('提示', msg);
			return false;
		}
	}
	function compute() {
//		if (!validate()) {
//			return false;
//		}
		Ext.Msg.wait("正在处理数据!,请等待...");
		Ext.Ajax.request({
			url : 'managebudget/collectionItem.action',
			method : 'post',
			params : {
				"isCollect" : collectBox.checked,
				"isMonth" : monthBox.checked,
				"isYear" : yearBox.checked,
				"startMonth" : fromMonth.getValue(),
				"endMonth" : fromMonth.getValue(),//toMonth.getValue()
				"startYear" : fromYear.getValue(),
				"endYear" : fromYear.getValue(),//toYear.getValue()
				"sysSource" : sysSourceCombox.getValue()
			},
			success : function(result, request) {
				var o = eval('(' + result.responseText + ')');
				Ext.Msg.hide();
				Ext.MessageBox.alert('提示信息', o.msg);
			},
			failure : function(result, request) {
				Ext.Msg.hide();
				Ext.MessageBox.alert('提示信息', '未知错误，请联系管理员！')
			}

		})
	}
	var btnCompute = new Ext.Button({
		id : 'btnCompute',
		text : '确  定',
		minWidth : 70,
		handler : compute
	});
	function buildData() {
//		if (!validate()) {
//			return false;
//		}
		Ext.Msg.wait("正在处理数据!,请等待...");
		Ext.Ajax.request({
			url : 'managebudget/collectionItem.action',
			method : 'post',
			params : {
				"isCollect" : collectBox.checked,
				"isMonth" : monthBox.checked,
				"isYear" : yearBox.checked,
				"startMonth" : fromMonth.getValue(),
				"endMonth" : fromMonth.getValue(),//toMonth.getValue()
				"startYear" : fromYear.getValue(),
				"endYear" : fromYear.getValue(),//toYear.getValue()
				"sysSource" : sysSourceCombox.getValue(),
				"isBuild" : "1"
			},
			success : function(result, request) {
				var o = eval('(' + result.responseText + ')');
				Ext.Msg.hide();
				Ext.MessageBox.alert('提示信息', o.msg);
			},
			failure : function(result, request) {
				Ext.Msg.hide();
				Ext.MessageBox.alert('提示信息', '未知错误，请联系管理员！')
			}

		})
	}
	var btnBuildData = new Ext.Button({
		id : 'btnBuildData',
		text : '构造当月数据',
		minWidth : 70,
		handler : buildData
	});

	var Query = new Ext.form.FieldSet({
		title : '时间设置',
		border : true,
		height : 160,
		// collapsible : true,
		anchor : '100%',
		layout : 'column',
		items : [{
			border : false,
			columnWidth : 0.2,
			align : 'center',
			layout : 'form',
			items : [monthBox, yearBox]
		}, {
			border : false,
			columnWidth : 0.4,
			anchor : '100%',
			layout : 'form',
			items : [fromMonth, fromYear]
//		}, {
//			border : false,
//			columnWidth : 0.4,
//			anchor : '100%',
//			layout : 'form',
//			items : [toMonth, toYear]
		},{
			border : false,
			columnWidth : 0.5,
			anchor : '100%',
			layout : 'form',
			items : [sysSourceCombox]
		}]
	});
	var collectBox = new Ext.form.Checkbox({
		id : 'collectBox',
		boxLabel : '采集数据',
		hideLabel : true,
		checked : true,
		name : 'queryTypeBox'
	});
	
	var QueryType = new Ext.form.FieldSet({
		title : '计算设置',
		border : true,
		height : 160,
		buttonAlign : 'center',
		// collapsible : true,
		layout : 'form',
		items : [{
			layout : 'column',
			border : false,
			items : [{
				border : false,
				columnWidth : 0.3,
				layout : 'form'
			}, {
				border : false,
				columnWidth : 0.7,
				layout : 'form',
				items : [collectBox]
			}]
		}],
		buttons : [btnCompute,btnBuildData]
	});
	var lblCompluteDate = new Ext.form.Label({
					 
    });
	var form = new Ext.form.FormPanel({
		bodyStyle : "padding:5px 5px 5px 5px",
		labelAlign : 'right',
		id : 'shift-form',
		// labelWidth : 5,
		autoHeight : true,
		region : 'center',
		border : false,
		layout : 'form',
		items : [{
			layout : 'column',
			border : false,
			items : [{
				border : false,
				columnWidth : 0.6,
				align : 'center',
				layout : 'form',
				anchor : '100%',
				items : [
				lblCompluteDate,
				Query]
			}, {
				border : false,
				columnWidth : 0.05,
				align : 'center',
				layout : 'form',
				anchor : '100%'
			}, {
				border : false,
				columnWidth : 0.25,
				align : 'center',
				layout : 'form',
				anchor : '100%',
				items : [QueryType]
			}]
		}]
	});

	/*-----------------------------------------------------------------------------------------------------------------*/
	var item = Ext.data.Record.create([{
		name : 'accountOrder'
	}, {
		name : 'itemCode'
	}, {
		name : 'itemName'
	}, {
		name : 'dataCollectWay'
	}]);
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true
	});
	var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
		header : '行',
		width : 30,
		align : 'center'
	}), {
		header : '等级',
		dataIndex : 'accountOrder',
		width : 50,
		align : 'center'
	}, {
		header : '指标编码',
		dataIndex : 'itemCode',
		width : 80,
		align : 'center'
	}, {
		header : '指标名称',
		dataIndex : 'itemName',
		width : 80,
		align : 'center'
	}, {
		header : '产生方式',
		dataIndex : 'dataCollectWay',
		width : 80,
		align : 'center',
		renderer : function(e) {
			if (e == 1)
				return "手工录入";
			if (e == 2)
				return "实时采集";
			if (e == 3)
				return "派生计算";
		}
	}]);
	cm.defaultSortable = true;
	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'manager/findComputeStatItemList.action'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalCount',
			root : 'list'
		}, item)
	});
	var Grid = new Ext.grid.GridPanel({
		ds : ds,
		cm : cm,
		sm : sm,
		split : true,
		autoScroll : true,
		loadMask : {
			msg : '读取数据中 ...'
		},
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : ds,
			displayInfo : true,
			displayMsg : '显示第 {0} 条到 {1} 条记录，一共 {2} 条',
			emptyMsg : "没有记录"
		}),
		border : false,
		viewConfig : {
			forceFit : false
		}
	});
	var panelleft = new Ext.Panel({
		region : 'west',
		title : '指标计算',
		layout : 'fit',
		width : 220,
		autoScroll : true,
		containerScroll : true,
		border : false,
		collapsible : true,
		split : true,
		items : [Grid]
	});

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		items : [{
			region : "north",
			layout : 'fit',
			height : 180,
			border : false,
			split : true,
			items : [form]
		}, {
			region : "center",
			// enableTabScroll : true,
			layout : "fit",
			collapsible : true,
			border : false,
			// split : true,
			// margins : '0 0 0 0',
			// 注入表格
			items : [panelleft]
		}] 
	}); 
	
//	Ext.override(Ext.form.Label, {
//		setText : function(t) {
//			this.text = t;
//			if (this.rendered) {
//				this.el.update(t);
//			}
//		}
//	});
//	Ext.Ajax.request({
//		url : "manager/findComputeDateBetween.action",
//		method : 'POST',
//		success : function(response, options) {
//			lblCompluteDate.setText('已计算日期：'+response.responseText); 
//		}
//	}); 
	
	
})
