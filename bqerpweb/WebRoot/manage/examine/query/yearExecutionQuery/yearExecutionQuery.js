Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {
	var itemId = '';
	
	var con_item = Ext.data.Record.create([{
				name : 'executionid1'
			}, {
				name : 'executionid2'
			}, {
				name : 'executionid3'
			}, {
				name : 'itemcode'
			}, {
				name : 'itemid'
			}, {
				name : 'itemname'
			}, {
				name : 'unitname'
			}, {
				name : 'planValue1'
			}, {
				name : 'realValue1'
			}, {
				name : 'flawValue1'
			}, {
				name : 'planValue2'
			}, {
				name : 'realValue2'
			}, {
				name : 'flawValue2'
			}, {
				name : 'planValue3'
			}, {
				name : 'realValue3'
			}, {
				name : 'flawValue3'
			}]);
			
	var con_sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			});
	var con_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'managexam/getYearExecutionTable.action' // 考核主题
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : "totalCount",
							root : "list"
						}, con_item)
			});
	var con_item_cm = new Ext.grid.ColumnModel({
	columns: [con_sm, 
			{
		header : '考核指标',
		dataIndex : 'itemname',
		width:200
	}, {
		header : '单位',
		dataIndex : 'unitname'
	}, {
		header : '年度计划',
		dataIndex : 'planValue1'
	}, {
		header : '实际值',
		dataIndex : 'realValue1'
	}, {
		header : '差值',
		dataIndex : 'flawValue1'
	}, {
		header : '年度计划',
		dataIndex : 'planValue2'
	}, {
		header : '实际值',
		dataIndex : 'realValue2'
	}, {
		header : '差值',
		dataIndex : 'flawValue2'
	}, {
		header : '年度计划',
		dataIndex : 'planValue3'
	}, {
		header : '实际值',
		dataIndex : 'realValue3'
	}, {
		header : '差值',
		dataIndex : 'flawValue4'
	}],
	defaultSortable : true,
	rows : [[{
							rowspan : 1,
							colspan : 3
						}, {
							header : '#11、12机',
							colspan : 3,
							align : 'center'
						},{
							header : '#1、2机',
							colspan : 3,
							align : 'center'
						},{
							header : '全厂合并',
							colspan : 3,
							align : 'center'
						}]]});
	
	function getMonth() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10);
//		t = d.getMonth() + 1;
//		s += (t > 9 ? "" : "0") ;
		return s;
	}
	
	var meetingMonth = new Ext.form.TextField({
				style : 'cursor:pointer',
				// id : 'month',
				columnWidth : 0.5,
				readOnly : true,
				anchor : "70%",
				fieldLabel : '年份',
				name : 'month',
				value : getMonth(),
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y',
									dateFmt : 'yyyy',
									alwaysUseStartDate : true
								});
						this.blur();
					}
				}
			});
	
	
	// 载入数据
	function getTopic() {
		con_ds.load({
					params : {
						datetime : meetingMonth.getValue()
					}
				});
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

	// 导出
	function exportRecord() {		
		Ext.Msg.confirm('提示', '确认要导出数据？', function(id) {
			if (id == 'yes') {
				var tableHeader = "<table border=1><tr><th colspan = 9>年度计划执行情况查询</th></tr>";
				var html = [tableHeader];
				html.push("<tr><th rowspan = 2 colspan = 2>考核指标</th><th rowspan = 2>单位</th><th colspan = 3>#11、12机</th><th colspan = 3>#1、2机</th><th colspan = 3>全厂合并</th></tr>")
				html.push("<tr><th>年度计划</th><th>实际值</th><th>差值</th><th>年度计划</th><th>实际值</th><th>差值</th><th>年度计划</th><th>实际值</th><th>差值</th></tr>")
				for (var i = 0; i < con_ds.getTotalCount(); i++) {
					var rec = con_ds.getAt(i);
					rec.set('itemname',rec.get('itemname')== null ? "" : rec.get('itemname')) ;
					rec.set('unitname',rec.get('unitname')== null ? "" : rec.get('unitname')) ;
					rec.set('planValue1',rec.get('planValue1')== null ? "" : rec.get('planValue1')) ;
					rec.set('realValue1',rec.get('realValue1')== null ? "" : rec.get('realValue1')) ;
					rec.set('flawValue1',rec.get('flawValue1')== null ? "" : rec.get('flawValue1')) ;
					rec.set('planValue2',rec.get('planValue2')== null ? "" : rec.get('planValue2')) ;
					rec.set('realValue2',rec.get('realValue2')== null ? "" : rec.get('realValue2')) ;
					rec.set('flawValue2',rec.get('flawValue2')== null ? "" : rec.get('flawValue2')) ;
					rec.set('planValue3',rec.get('planValue3')== null ? "" : rec.get('planValue3')) ;
					rec.set('realValue3',rec.get('realValue3')== null ? "" : rec.get('realValue3')) ;
					rec.set('flawValue3',rec.get('flawValue3')== null ? "" : rec.get('flawValue3')) ;
					html.push('<tr><td colspan = 2>' + rec.get('itemname') + '</td><td>' 							
							+ rec.get('unitname') + '</td><td>'
							+ rec.get('planValue1') + '</td><td>'
							+ rec.get('realValue1') + '</td><td>'
							+ rec.get('flawValue1') + '</td><td>'
							+ rec.get('planValue2') + '</td><td>'
							+ rec.get('realValue2') + '</td><td>'
							+ rec.get('flawValue2') + '</td><td>'
							+ rec.get('planValue3') + '</td><td>'
							+ rec.get('realValue3') + '</td><td>'
							+ rec.get('flawValue3') + '</td>'
							+ '</tr>')
				}
				html.push('</table>');
				html = html.join(''); // 最后生成的HTML表格
				tableToExcel(html);
			}
		})
	}
	
	
	var contbar = new Ext.Toolbar({
				items : ['年份',meetingMonth,{
							id : 'btnQuery',
							iconCls : 'query',
							text : "查询",
							handler : getTopic
						}, '-', {
							id : 'btnExport',
							iconCls : 'export',
							text : "导出",
							handler : exportRecord
						}]

			});
	
	var Grid = new Ext.grid.EditorGridPanel({
				viewConfig : {
					forceFit : true
				},
				sm : con_sm,
				ds : con_ds,
				cm : con_item_cm,
				height : 425,
				split : true,
				autoScroll : true,
				layout : 'fit',
				frame : false,
				// bbar : gridbbar,
				tbar : contbar,
				border : true,
				plugins : [new Ext.ux.plugins.GroupHeaderGrid()]
			});		

	var layout = new Ext.Viewport({
				layout : "border",
				border : false,
				items : [{
							region : "center",
							layout : 'fit',
							collapsible : true,
							split : true,
							margins : '0 0 0 0',
							// 注入表格
							items : [Grid]//Grid  panel 改为panel

						}]
			})
});
