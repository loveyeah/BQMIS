Ext.BLANK_IMAGE_URL = "comm/ext/resources/images/default/s.gif";
Ext.QuickTips.init();
Ext.onReady(function() {
	function getCurrentMonth() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t;
		return s;
	}

	// 计划时间
	var planTime = new Ext.form.TextField({
		id : 'planTime',
		fieldLabel : '计划时间',
		style : 'cursor:pointer',
		value : getCurrentMonth(),
		readOnly : true,
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M',
					dateFmt : 'yyyy-MM',
					alwaysUseStartDate : true,
					isShowClear : false
				});
				this.blur();
			}
		}
	});
	
	// 计划主题
	var readText = new Ext.form.TextField({
		id : 'readText',
		readOnly : true,
		fieldLabel : '全厂指标',
		value : '全厂指标'
	})

	// 各个按钮
	var queryBtu = new Ext.Button({
		id : 'queryBtu',
		text : '查询',
		iconCls : 'query',
		handler : queryFun
	})
	var excelBtu = new Ext.Button({
		id : 'approveBtu',
		text : '导出',
		iconCls : 'export',
		handler : excelFun
	})
	// 工具栏
	var tbar = new Ext.Toolbar({
		id : 'tbar',
		items : ['计划时间：', planTime,  queryBtu, '-', excelBtu]
	})
	// 数据对象
	var record = new Ext.data.Record.create([{
		// 0：主题ID
		name : 'topicId',
		mapping : 0
	}, {	// 1：主题名称
		name : 'topicName',
		mapping : 1
	}, {	// 2：主题显示顺序
		name : 'topicDisplay',
		mapping : 2
	}, {	// 3：经济指标ID
		name : 'economicItemId',
		mapping : 3
	}, {	// 4：指标名称
		name : 'itemName',
		mapping : 4
	}, {	// 5：指标别名
		name : 'alias',
		mapping : 5
	}, {	// 6：单位Id
		name : 'unitId',
		mapping : 6
	}, {	// 7：单位名称
		name : 'unitName',
		mapping : 7
	}, {	// 8：分类
		name : 'itemType',
		mapping : 8
	}, {	// 9：指标显示顺序
		name : 'itemDisplay',
		mapping : 9
	}, {	// 10：指标计划主ID
		name : 'plantMainId',
		mapping : 10
	}, {	// 11：月份
		name : 'month',
		mapping : 11
	}, {	// 12：计划工作流序号
		name : 'workflowNoPlan',
		mapping : 12
	}, {	// 13：计划工作流状态
		name : 'workflowStatusPlan',
		mapping : 13
	}, {	// 14：完成情况工作流序号
		name : 'workflowNoFact',
		mapping : 14
	}, {	// 15：完成情况工作流状态
		name : 'workflowStatusFact',
		mapping : 15
	}, {	// 16：指标计划明细ID
		name : 'plantDetailId',
		mapping : 16
	}, {	// 17：#11#12计划值
		name : 'plantPlan1112',
		mapping : 17
	}, {	// 18：#1#2计划值
		name : 'plantPlan12',
		mapping : 18
	}, {	// 19：#11#12完成情况
		name : 'plantFact1112',
		mapping : 19
	}, {	// 20：#1#2完成情况
		name : 'plantFact12',
		mapping : 20
	}])

	var store = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'manageitemplan/findItemByCondition.action'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalCount',
			root : 'list'
		}, record)
	})

	// 选择模式
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true
	})
	// 列模式
	var cm = new Ext.grid.ColumnModel({
		columns : [sm, new Ext.grid.RowNumberer({
							header : '行号',
							width : 35
						}), {
					header : '分类',
					dataIndex : 'itemType',
					renderer : function(value, matedata, record, rowIndex,
							colIndex, store) {
						if (record && rowIndex > 0) {
							if (store.getAt(rowIndex).get('itemType') == store
									.getAt(rowIndex - 1).get('itemType')
									|| store.getAt(rowIndex).get('itemType') == '')
								return '';
						} 
							return value;
					}
				}, {
					header : '指标名称',
					dataIndex : 'alias'
				}, {
					header : '计量单位',
					dataIndex : 'unitName'
				}, {
					header : '计划',
					dataIndex : 'plantPlan1112',
					width : 150
				}, {
					header : '完成情况',
					dataIndex : 'plantFact1112',
					width : 150
				}, {
					header : '计划',
					dataIndex : 'plantPlan12',
					width : 150
				}, {
					header : '完成情况',
					dataIndex : 'plantFact12',
					width : 150
				}],
		defaultSortable : true,
		rows : [[{
					rowspan : 2,
					colspan : 5
				}, {
					header : '#11,#12机组指标值',
					colspan : 2,
					align : 'center'
				}, {
					header : '#1,#2机组指标值',
					rowspan : 1,
					colspan : 2,
					align : 'center'
				}]]
	})

	var grid = new Ext.grid.EditorGridPanel({
		id : 'grid',
		frame : true,
		border : false,
		tbar : tbar,
		sm : sm,
		cm : cm,
		store : store,
		autoScroll : true,
		clicksToEdit : 1,
		enableColumnMove : false,
		plugins : [new Ext.ux.plugins.GroupHeaderGrid()]
	})

	new Ext.Viewport({
		frame : true,
		border : false,
		layout : 'border',
		items : [{
			region : 'center',
			layout : 'fit',
			items : [grid]
		}]
	})
	
	function queryFun() {
		store.baseParams = {
//			topic : 1,
			month : planTime.getValue()
		}
		store.load();
	}
	


		
	// 导出
	function tableToExcel(tableHTML) {
		window.clipboardData.setData("Text", tableHTML);
		try {
			var ExApp = new ActiveXObject("Excel.Application");
			var ExWBk = ExApp.workbooks.add();
			var ExWSh = ExWBk.worksheets(1);
			ExApp.DisplayAlerts = false;
			ExApp.visible = true;
		} catch (e) {
			if (e.number != -2146827859)
				Ext.Msg.alert('提示信息', "您的电脑没有安装Microsoft Excel软件！");
			return false;
		}
		ExWBk.worksheets(1).Paste;
	}
	function excelFun() {		
		Ext.Msg.confirm('提示', '确认要导出数据？', function(id) {
			if (id == 'yes') {
				var tableHeader = "<table border=1><tr><th colspan = 7></th></tr>";
//				var html = [tableHeader];
				var html = ["<table border=1>"];
				html.push("<tr><th rowspan = 2>分类</th><th rowspan = 2>指标名称</th><th rowspan = 2>计量单位</th><th colspan = 2>#11,#12机组指标值</th><th colspan = 2>#11,#12机组指标值</th></tr>")
				html.push("<tr><th>计划</th><th>完成情况</th><th>计划</th><th>完成情况</th></tr>")
				for (var i = 0; i < store.getCount(); i++) {
					var rec = store.getAt(i).copy();
					rec.set('itemType',rec.get('itemType')== null ? "" : rec.get('itemType')) ;
					if(i > 0)
					{
						if(store.getAt(i).get('itemType') == store.getAt(i - 1).get('itemType'))
						rec.set('itemType','');
					}
					rec.set('alias',rec.get('alias')== null ? "" : rec.get('alias')) ;
					rec.set('unitName',rec.get('unitName')== null ? "" : rec.get('unitName')) ;
					rec.set('plantPlan1112',rec.get('plantPlan1112')== null ? "" : rec.get('plantPlan1112')) ;
					rec.set('plantPlan12',rec.get('plantPlan12')== null ? "" : rec.get('plantPlan12')) ;
					rec.set('plantFact1112',rec.get('plantFact1112')== null ? "" : rec.get('plantFact1112')) ;
					rec.set('plantFact12',rec.get('plantFact12')== null ? "" : rec.get('plantFact12')) ;
					html.push('<tr><td>' + rec.get('itemType') + '</td><td>' 							
							+ rec.get('alias') + '</td><td>'
							+ rec.get('unitName') + '</td><td  align = "right">'
							+ rec.get('plantPlan1112') + '</td><td  align = "right">'
							+ rec.get('plantFact1112') + '</td><td  align = "right">'
							+ rec.get('plantPlan12') + '</td><td  align = "right">'
							+ rec.get('plantFact12') + '</td>'
							+ '</tr>')
				}
				html.push('</table>');
				html = html.join(''); // 最后生成的HTML表格
				tableToExcel(html);
			}
		})
	}
	queryFun()
})