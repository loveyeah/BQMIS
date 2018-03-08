Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	
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

	// 系统当前时间
	function getTime() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate();
		s += (t > 9 ? "" : "0") + t + " ";
		return s;
	}
	
	// 定义归口部门	
	var deptStore = new Ext.data.SimpleStore({
		fields : ['id','name'],
		data : [['1','正向（利润）预测'],['2','反向（电量）预测'],['3','反向（固本）预测'],['4','反向（变本）预测']]
	})
		var deptComboBox = new Ext.form.ComboBox({
		id : "deptComboBox",
		store : deptStore,
		displayField : "name",
		valueField : "id",
		mode : 'local',
		width : 134,
		triggerAction : 'all',
		readOnly : true,
		value : 1
	});

	
		// 年月份选择
	var formatType;
	var yearRadio = new Ext.form.Radio({
		id : 'year',
		name : 'queryWayRadio',
		// blankText : getDate(),
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
					alwaysUseStartDate : false,
					onclearing : function() {
						planStartDate.markInvalid();
					}
				});
			}
		}
	});
	// 遍历所有的REDIO获得ID
	function getChooseQueryType() {
		var list = document.getElementsByName("queryWayRadio");
		for (var i = 0; i < list.length; i++) {
			if (list[i].checked) {
				return list[i].id;
			}
		}
	}
	
//----------------------------------------------------------------	

	var sm = new Ext.grid.CheckboxSelectionModel();
	// 创建记录类型
	var Myrecord = Ext.data.Record.create([
		 {
		name : 'modelItemId'
	}, {
		name : 'daddyItemId'
	}, {
		name : 'modelItemCode'
	}, {
		
		name : 'modelItemName'
	}, {
		
		name : 'modelType'
	}, {
		
		name : 'isItem'
	}, {
	
		name : 'unitName'
	}, {
		
		name : 'comeFrom'
	}, {
		
		name : 'modelOrder'
	}, {
		
		name : 'displayNo'
	}, {
		
		name : 'modelItemExplain'
	}, {
		
		name : 'forecastId'
	}, {
		
		name : 'forecastTime'
	}, {
		
		name : 'forecastValue'
	}]);
	// 配置数据集
	var store = new Ext.data.Store({
		/* 创建代理对象 */
		proxy : new Ext.data.HttpProxy({
			url : 'managebudget/getForcastRec.action'
		}),
		/* 创建解析Json格式数据的解析器 */
		reader : new Ext.data.JsonReader({
			root : "list",
			totalProperty : "totalCount"
		}, Myrecord)
	});
    
	function queryRecord() {
		
		store.baseParams = {
			forecastTime :  time.getValue(),
			modelType : deptComboBox.getValue()
		};
		store.load({
			params : {
				start : 0,
				limit : 18
			}
		});
	}
	
	
	// 查询
	var query = new Ext.Button({
		id : "btnQuery",
		text : "查询",
		iconCls : "query",
		handler :queryRecord
	})
	
	// 保存
	var save = new Ext.Button({
		id : "btnSave",
		text : "保存",
		iconCls : "save",
		handler :  saveRecord
	})
	// 分析
	var analy = new Ext.Button({
		id : "btnAnal",
		text : "分析",
		iconCls : "save",
		handler :  analyRecord
	})
	
	// 定义grid
	// 事件状态
	var sm_store_item = new Ext.grid.ColumnModel([sm, 
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
				header : '预测值',
				align : 'center',
				width : 90,
				sortable : true,
				dataIndex : 'forecastValue',
				css : CSS_GRID_INPUT_COL,
				editor : new Ext.form.NumberField({
							allowBlank : false,
							allowDecimal : true,
							allowNegative : false,
							decimalPrecision : 2
						}),
			renderer : function(value) {
					return numberFormat(value);
				}
			},{			
			header : '预测公式',
				align : 'left',
				width : 150,
				sortable : true,
				dataIndex : 'modelItemExplain'
			}]);

	// 底部分页栏
	var bbar = new Ext.PagingToolbar({
		pageSize : 18,
		store : store,
		displayInfo : true,
		displayMsg : "{0} 到 {1} /共 {2} 条",
		emptyMsg : "没有记录"
	});
	// 顶部工具栏
	var tbar = new Ext.Toolbar({
		items : [yearRadio, monthRadio, time,'-','模型种类',deptComboBox,'-',query
		,'-',save,'-',analy]
	});
	// 可编辑的表格
	var Grid = new Ext.grid.EditorGridPanel({
		sm : sm,
		ds : store,
		cm : sm_store_item,
		autoScroll : true,
		bbar : bbar,
		tbar : tbar,
		border : true,
		clicksToEdit : 1,
		viewConfig : {
		 forceFit : true
		}
	});
	
	Grid.on('beforeedit', function(e) {
	// true可以编辑，false 不可编辑
	if (e.record.get('comeFrom') == '1')
	{
		 return true;
	}
	else
		return false;
	})
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
	
	function saveRecord()
	{
		if (store.getTotalCount() == 0) {
				Ext.Msg.alert('提示信息', '无数据进行保存！');
				return;
			}
		if(store.getAt(0).get('forecastTime') != time.getValue())
		{
			Ext.Msg.alert('提示信息', '请先进行查询！');
				return;
		}
			Ext.Msg.confirm('提示信息', '确认要保存数据吗？', function(id) {
				if (id == 'yes') {
					var add = new Array();
					var update = new Array();
					var mod = new Array();
					for (var i = 0; i < store.getTotalCount(); i++) {
						if (store.getAt(i).get('forecastId') == null
								|| store.getAt(i).get('forecastId') == "") {
							add.push(store.getAt(i).data)
						} else {
							update.push(store.getAt(i).data)
						}
						mod.push(store.getAt(i).data)
					}
					Ext.Ajax.request({
								url : 'managebudget/saveForecastItemMod.action',
								method : 'post',
								params : {
									isAdd : Ext.util.JSON.encode(add),
									isUpdate : Ext.util.JSON.encode(update),
									isMod : Ext.util.JSON.encode(mod)
								},
								success : function(result, request) {
									Ext.Msg.alert('提示信息', '数据保存成功！');
									store.reload();
								},
								failure : function(result, request) {
									Ext.Msg.alert('提示信息', '数据保存失败！');
									store.reload();
								}
							})
				}
			})
	}
	
	function analyRecord() {
		var args = new Object();
		url = "../../query/forecastModelQuery/forecastModelQuery.jsp";
		args.forecastTime = time.getValue();
		var obj = window.showModalDialog(url, args,
				'status:no;dialogWidth=750px;dialogHeight=550px');
		queryRecord();

	}
	
	queryRecord();
	
})
