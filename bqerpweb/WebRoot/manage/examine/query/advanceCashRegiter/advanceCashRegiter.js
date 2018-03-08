Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	Ext.QuickTips.init();
	function getMonth() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t;
		return s;
	}
	var monthDate = new Ext.form.TextField({
				name : 'monthDate',
				value : getMonth(),
				id : 'monthDate',
				fieldLabel : "日期",
				style : 'cursor:pointer',
				cls : 'Wdate',
				width : 90,
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y%M',
									dateFmt : 'yyyy-MM',
									alwaysUseStartDate : false
								});
						this.blur();
					}
				}
			});

	var record = new Ext.data.Record.create([{
				name : 'itemId',
				mapping : 0
			}, {
				name : 'itemName',
				mapping : 1
			}, {
				name : 'value2',
				mapping : 2
			}, {
				name : 'value3',
				mapping : 3
			}, {
				name : 'value4',
				mapping : 4
			}])

	var ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'managexam/getAdvanceCashRegiter.action'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : 'totalCount',
							root : 'list'
						}, record)
			})

	// var con_item_cm = new Ext.grid.ColumnModel({
	// columns : [con_sm,
	// // {
	// // header : '编码',
	// // width : 40,
	// // dataIndex : 'topicId',
	// // align : 'center'
	// // },
	// {
	// header : '指标名称',
	// dataIndex : 'itemname'
	// // ,
	// // editor : new Ext.form.TextField()
	// }

	var cm = new Ext.grid.ColumnModel({
				columns : [new Ext.grid.RowNumberer({
									header : '行号',
									width : 31
								}), {
							header : '指标',
							dataIndex : 'itemName'
						}, {
							header : '生产面责任部门',
							dataIndex : 'value2'
						}, {
							header : '经营面责任部门',
							dataIndex : 'value3'
						}, {
							header : '多经总公司',
							dataIndex : 'value4'
						}]
			})
	var con_sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			});

	var grid = new Ext.grid.EditorGridPanel({
				viewConfig : {
					forceFit : true
				},
				sm : con_sm,
				ds : ds,
				cm : cm,
				// height : 425,
				split : true,
				autoScroll : true,
				layout : 'fit',
				frame : false,
				// bbar : gridbbar,
				tbar : ['月份：', monthDate, {
							text : '查询',
							iconCls : 'query',
							handler : query
						}, '-', {
							text : "导出",
							id : 'myexport',
							iconCls : 'export',
							handler : exportRec
						}],
				border : true
			});
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

	function exportRec() {
		var count = ds.getTotalCount();
		if (count > 0) {
			var html = ['<table border=1><tr><th>行号</th><th colspan="2">指标</th><th>生产面责任部门</th><th>经营面责任部门</th><th>多经总公司</th></tr>'];
			var i = 1;
			for (i; i < count+3; i++) {
				var rc = ds.getAt(i-1);
				html.push('<tr><td>' +i+ '</td><td colspan = "2">' + rc.get("itemName")
						+ '</td><td>' + (rc.get("value2") == null ?"":rc.get("value2")) + '</td><td>' + (rc.get("value3") == null?"":rc.get("value3") )
						+ '</td><td>' + (rc.get("value4") == null ? "" :rc.get("value4")) + '</td></tr>');
			}
			html.push('</table>');
			html = html.join(''); // 最后生成的HTML表格
			tableToExcel(html);
		}
	}

	var layout = new Ext.Viewport({
				layout : "border",
				border : false,
				items : [{
							region : "center",
							layout : 'fit',
							collapsible : true,
							split : true,
							margins : '0 0 0 0',
							items : [grid]

						}]
			})

	function query() {
		ds.baseParams = {
			month : monthDate.getValue()
		}
		ds.load(
				// {
				// params : {
				// start : 0,
				// limit : 18
				// }
				// }
				)
	}
	ds.on("load", function() {
				var temp = monthDate.getValue().substring(5, 7);
				var tempM = temp > 9 ? temp : temp.substring(1, 2);
				var itemNameTemp = tempM + "月合计应兑现金额";
				var value2 = 0;
				var value3 = 0;
				var value4 = 0;
				var totalValue = 0;
				if (ds.getTotalCount() > 0) {
					for (var i = 0; i < ds.getTotalCount(); i++) {
						value2 += ds.getAt(i).get("value2");
						value3 += ds.getAt(i).get("value3");
						value4 += ds.getAt(i).get("value4");
					}
				}
				totalValue = value2 + value3 + value4;

				var ob1 = new record({
							'itemId' : 0,
							'itemName' : itemNameTemp,
							'value2' : value2,
							'value3' : value3,
							'value4' : value4
						});
				var ob2 = new record({
							'itemId' : 0,
							'itemName' : "",
							'value2' : "",
							'value3' : "",
							'value4' : "合计:" + totalValue
						});
				grid.stopEditing();
				ds.insert(ds.getCount(), ob1);
				ds.insert(ds.getCount(), ob2);
			})
	query()
})