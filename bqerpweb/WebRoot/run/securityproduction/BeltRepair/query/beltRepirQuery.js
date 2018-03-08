Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {

  var repairStartTime = new Ext.form.TextField({
		readOnly : true,
		width : 100,
		listeners : {
			focus : function(){
				WdatePicker({
					startDate : '%y-%m-%d',
					dateFmt : 'yyyy-MM-dd'
				})
			}
		}
	})
	var repairEndTime = new Ext.form.TextField({
		readOnly : true,
		width : 100,
		listeners : {
			focus : function(){
				WdatePicker({
					startDate : '%y-%m-%d',
					dateFmt : 'yyyy-MM-dd'
				})
			}
		}
	})
	var beltRecord = new Ext.data.Record.create([
	{
		name : 'repairId',
		mapping : 0
	}, {
		name : 'toolId',
		mapping : 1
	}, {
		name : 'useTime',
		mapping : 2
	}, {
		name : 'belongDep',
		mapping:3
	}, {
		name : 'belongDepName',
		mapping : 4
	}, {
		name : 'beltNumber',
		mapping : 5
	}, {
		name : 'repairResult',
		mapping : 6
	}, {
		name : 'repairBy',
		mapping : 7
	}, {
		name : 'repairByName',
		mapping : 8
	}, {
		name : 'repairDep',
		mapping : 9
	}, {
		name : 'repairDepName',
		mapping : 10
	}, {
		name : 'repairBegin',
		mapping : 11
	}, {
		name : 'repairEnd',
		mapping : 12
	}, {
		name : 'nextTime',
		mapping : 13
	}, {
		name : 'memo',
		mapping : 14
	} ,{
		name : 'toolName',
		mapping : 15
	}]);
	
	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'security/findBeltRepairList.action',
			method : 'post'
		}),
		reader : new Ext.data.JsonReader({
			root : 'list'
		}, beltRecord)
	});
	
	var sm= new Ext.grid.CheckboxSelectionModel({
		singleSelect : false,
		listeners : {
			rowselect : function(sm, row, rec) { }
		}
	});

	var colModel = new Ext.grid.ColumnModel([	
	sm,new Ext.grid.RowNumberer({
                header : '行号',
                width : 35
            }),{
//			id : 'repairId',
			header : 'ID',
			hidden:true,
			dataIndex : 'repairId',
			sortable : true,
			width : 100
		},{
//		id : 'useTime',
		header : '使用时间',
		dataIndex : 'useTime',
		width : 100,
		sortable : true
	}, {
		header : '所属部门',
		dataIndex : 'belongDepName',
		width : 110
	}, 
	{
		header : '数量',
		dataIndex : 'beltNumber',
		width : 100
		
	},{
		header : '工具名称',
		dataIndex : 'toolName',
		width : 100
		
	}, {
		header : '检验结果',
		dataIndex : 'repairResult',
		width : 130
	},
	{
//		id : 'repairByName',
		header : '检修人',
		dataIndex : 'repairByName',
		width : 100,
		sortable : true
	}, {
		header : '检修部门',
		dataIndex : 'repairDepName',
		width : 110
	}, 
	{
		header : '检验开始时间',
		dataIndex : 'repairBegin',
		width : 100
		
	}, {
		header : '检验结束时间',
		dataIndex : 'repairEnd',
		width : 100
	},
	{
		header : '下次检验时间',
		dataIndex : 'nextTime',
		width : 100
		
	}, {
		header : '备注',
		dataIndex : 'memo',
		width : 130
	}]);
	
		var query = new Ext.Button({
//		id : 'query',
		text : '查询',
		iconCls : 'query',
		handler : queryBeltRepair
	})
	var exporData = new Ext.Button({
		id : 'exporData',
		text : '导出',
		iconCls : 'export',
		handler : exportStore
	})
	function queryBeltRepair()
	{
		ds.reload();
	}
	colModel.defaultSortable = true;
	var tbar = new Ext.Toolbar({
		items : ['检验开始时间',repairStartTime,'至:',repairEndTime,query,exporData]
	})
	
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
				alert("您的电脑没有安装Microsoft Excel软件！");
			return false;
		}
		ExWBk.worksheets(1).Paste;
	}
	function exportStore(){
		copyDs.load();
	}
	
	function exportDataFun() {
		if (copyDs.getTotalCount() == 0) {
			Ext.Msg.alert('提示', '无数据可导出！')
		} else {
			var html = ['<table border=1><tr><th>使用时间</th><th>所属部门</th><th>数量</th><th>工具名称</th><th>检验结果</th><th>检修人</th><th>检验部门</th><th>检验开始时间</th>'
					+ '<th>检验结束时间</th><th>下次检验时间</th><th>备注</th></tr>'];
			for (var i = 0; i < copyDs.getTotalCount(); i++) {
				var rc = copyDs.getAt(i);
				html.push('<tr><td>' + (rc.get('useTime') == null ? "" : rc.get('useTime')) + '</td>' + '<td>'
						+(rc.get('belongDepName') == null ? "" : rc.get('belongDepName')) + '</td>' + '<td>'
						+(rc.get('beltNumber') == null ? "" : rc.get('beltNumber')) + '</td>'
						+ '<td>' + (rc.get('toolName') == null ? "" : rc.get('toolName')) + '</td>'
						+ '<td>' +(rc.get('repairResult') == null ? "" : rc.get('repairResult'))  + '</td>' + '<td>'
						+(rc.get('repairByName') == null ? "" : rc.get('repairByName')) + '</td>' + '<td>'
						+ (rc.get('repairDepName') == null ? "" : rc.get('repairDepName')) + '</td>' + '<td>'
						+(rc.get('repairBegin') == null ? "" : rc.get('repairBegin')) + '</td>' + '<td>'
						+ (rc.get('repairEnd') == null ? "" : rc.get('repairEnd')) + '</td>' + '<td>'
						+(rc.get('nextTime') == null ? "" : rc.get('nextTime'))  + '</td>' + '<td>'
						+ (rc.get('memo') == null ? "" : rc.get('memo')) + '</td>'	+ '</tr>')

			}
			html.push('</table>');
			html = html.join(''); // 最后生成的HTML表格
			tableToExcel(html);
		}

	}
	var bbar = new Ext.PagingToolbar({
						pageSize : 18,
						store : ds,
						displayInfo : true,
						displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
						emptyMsg : "没有记录"
					})
	
	var beltGrid = new Ext.grid.GridPanel({
				id : 'query-grid',
				autoScroll : true,
				ds : ds,
				cm : colModel,
				sm : sm,
				tbar : tbar,
				bbar:bbar,
				border : true
			});
	var viewport = new Ext.Viewport({
		region : "center",
		layout : 'fit',
		autoWidth:true,
		autoHeight:true,
		fitToFrame : true,
		items : [beltGrid]
	});
	ds.on('beforeload', function() {
				Ext.apply(this.baseParams, {
							beginTime : repairStartTime.getValue(),
							endTime : repairEndTime.getValue()
							
						})
			})
	var copyDs = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'security/findBeltRepairList.action'
		}),
		reader : new Ext.data.JsonReader({
			root : 'list'
		}, beltRecord)
	});
	copyDs.on('beforeload', function() {
				Ext.apply(this.baseParams, {
							beginTime : repairStartTime.getValue(),
							endTime : repairEndTime.getValue()
							
						})
			});
	copyDs.on('load',function(){
		exportDataFun()
	})
	ds.load({
		params : {
			start : 0,
			limit : 18
		}
	})
	
});