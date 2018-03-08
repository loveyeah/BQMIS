Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
		function renderMoney(v) {
			return renderNumber(v, 2);// 修改计算金额现在2位小数
		}
		function renderNumber(v, argDecimal) {
			if (v) {
				if (typeof argDecimal != 'number') {
					argDecimal = 2;
				}
				v = Number(v).toFixed(argDecimal);
				var t = '';
				v = String(v);
				while ((t = v.replace(/^(-?[0-9]+)([0-9]{3}.*)$/, '$1,$2')) !== v)
					v = t;

				return v;
			} else
				return '0.00';
		}
		// ------------------------------
		var MyRecord = Ext.data.Record.create([{
			name : 'workerId',
			mapping : 0
		}, {
			name : 'workerName',
			mapping : 1
		},  {
			name : 'totalSalary',
			mapping : 2
		}, {
			name : 'basicSalary',
			mapping : 3
		}, {
			name : 'workAgeSalary',
			mapping : 4
		}, {
			name : 'monthAward',
			mapping : 5
		}, {
			name : 'bigAward',
			mapping : 6
		}, {
			name : 'totalIncome',
			mapping : 7
		}]);
		var dataProxy = new Ext.data.HttpProxy({
			url : 'com/workerSalaryQuery.action'
		});

		var theReader = new Ext.data.JsonReader({
			root : "list",
			totalProperty : "totalCount"
		}, MyRecord);
		var store = new Ext.data.Store({
			proxy : dataProxy,
			reader : theReader
		});
	 
		function getDate1() {
			var d, s, t;
			d = new Date();
			s = d.getFullYear().toString(10) + "-";
			t = d.getMonth() + 1;
			s += (t > 9 ? "" : "0") + t /*
										 * + "-"; t = d.getDate(); s += (t > 9 ? "" :
										 * "0") + t + " ";
										 */
			return s;
		}

		function getDate2() {
			var d, s, t;
			d = new Date();
			s = d.getFullYear().toString(10) + "-";
			t = d.getMonth() + 2;
			s += (t > 9 ? "" : "0") + t /*
										 * + "-"; t = d.getDate(); s += (t > 9 ? "" :
										 * "0") + t + " ";
										 */
			return s;
		}
		// 开始时间选择
		var startDate = new Ext.form.TextField({
			style : 'cursor:pointer',
			name : 'time',
			fieldLabel : '从',
			readOnly : true,
			anchor : "80%",
			value : getDate1(),
			listeners : {
				focus : function() {
					WdatePicker({
						startDate : '%y-%M',
						alwaysUseStartDate : false,
						dateFmt : 'yyyy-MM',
						isShowClear : false,
						onpicked : function(v) { 
							this.blur();
						}
					});
				}
			}
		});

		var endDate = new Ext.form.TextField({
			style : 'cursor:pointer',
			name : 'time',
			fieldLabel : '至',
			readOnly : true,
			anchor : "80%",
			value : getDate2(),
			listeners : {
				focus : function() {
					WdatePicker({
						startDate : '%y-%M',
						alwaysUseStartDate : false,
						dateFmt : 'yyyy-MM',
						isShowClear : false,
						onpicked : function(v) {
							this.blur();
						}
					});
				}
			}
		});
		var sm = new Ext.grid.CheckboxSelectionModel({
				// singleSelect : true
		});
		var headerTbar = new Ext.Toolbar({
					items : ['从', startDate, '至', endDate, {
						text : "查询",
						iconCls : 'query',
						handler : queryRecord
					}, {
						text : "导出",
						iconCls : Constants.CLS_EXPORT,
						handler : exportRecord
					}]
				});
		function queryRecord() {
			store.baseParams = {
				startDate : startDate.getValue(),
				endDate : endDate.getValue()
			}

			store.load({
				params : {
					start : 0,
					limit : 18
				},
				callback : addLine
			});

		}
		function addLine() {

			// 统计行
			var record = new MyRecord({
				workerName : "",
				month : "",
				totalSalary : "",
				basicSalary : "",
				workAgeSalary : "",
				monthAward : "",
				bigAward : "",
				totalIncome : "",
				isNewRecord : "total"
			});
			var count = store.getCount();

			// 停止原来编辑
			grid.stopEditing();
			// 插入统计行
			store.insert(count, record);
			grid.getView().refresh();

		}
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
			Ext.Ajax.request({
				url : 'com/workerSalaryQuery.action',
				method : 'post',
				params : {
					startDate : startDate.getValue(),
					endDate : endDate.getValue()

				},

				success : function(response) {
					var json = eval('(' + response.responseText.trim() + ')');
					var records = json.list;

					var title = "";
					title = "员工收入统计";
					var tableHeader = "<table border=1 ><tr><th align=center colspan =8  style=font-size:5.55mm; FONT-FAMILY: 仿宋_GB2312>"
							+ title + "</th></tr>";
					var html = [tableHeader];

					html.push("<tr><th align=right colspan = 2>月份：</th>"
							+ "<td  align=left colspan = 3>"
							+ startDate.getValue()
							+ "</td><td  align=left colspan = 3>"
							+ endDate.getValue() + "</td></tr>")

					html
							.push("<tr><th>姓名</th><th>总工资</th><th>基础工资</th><th>工龄工资</th>"
									+ "<th>月奖</th><th>大奖</th><th >总收入</th></tr>")

					for (var i = 0; i < records.length; i += 1) {

						var rc = records[i];

						html.push('<tr><td align ="left">'
								+ (rc[0] == null ? "" : rc[0])
								+ '</td><td align ="left">'
								+ (rc[1] == null ? "" : rc[1])
								+ '</td><td align ="left">'
								+ (rc[2] == null ? "" : rc[2])
								+ '</td><td align ="left">'
								+ (rc[3] == null ? "" : rc[3])
								+ '</td><td align ="left">'
								+ (rc[4] == null ? "" : rc[4])
								+ '</td><td align ="left">'
								+ (rc[5] == null ? "" : rc[5])
								+ '</td><td align ="left">'
								+ (rc[6] == null ? "" : rc[6]) + '</td></tr>');
					}
					html.push('</table>');
					html = html.join('');
					tableToExcel(html);
				},
				failure : function(response) {
					Ext.Msg.alert('提示', '导出失败！');
				}
			});
		}

		var grid = new Ext.grid.GridPanel({
			listeners:{
				'render':function(){ 
					queryRecord();
				}
			},
			store : store,
			columns : [new Ext.grid.RowNumberer({
				header : '序号',
				width : 35,
				align : 'left'
			}), {
				header : "姓名",
				sortable : true,
				hidden : false,
				align : 'left',
				dataIndex : 'workerName',
				renderer : function(value, cellmeta, record, rowIndex,
						columnIndex, store) {
					if (rowIndex < store.getCount() - 1) {
						var workerName = record.data.workerName;
						// 强行触发renderer事件
						return workerName;
					} else {

						return "<font color='red'>" + "合计" + "</font>";
					}

				}
			},/* {
				header : "月份",
				sortable : true,
				hidden : false,
				align : 'left',
				dataIndex : 'month'
				
			},*/ {
				header : "总工资",
				width : 120,
				sortable : true,
				align : 'left',
				dataIndex : 'totalSalary',
				renderer : function(value, cellmeta, record, rowIndex,
						columnIndex, store) {
					if (rowIndex < store.getCount() - 1) {
						var totalSalary = record.data.totalSalary;
						// 强行触发renderer事件
						return renderMoney(totalSalary);
					} else {
						totalSum = 0;
						for (var i = 0; i < store.getCount() - 1; i++) {
							totalSum += store.getAt(i).get('totalSalary');
						}

						return "<font color='red'>" + renderMoney(totalSum)
								+ "</font>";
					}

				}
			}, {
				header : "基础工资",
				width : 100,
				sortable : true,
				align : 'left',
				dataIndex : 'basicSalary',
				renderer : function(value, cellmeta, record, rowIndex,
						columnIndex, store) {
					if (rowIndex < store.getCount() - 1) {
						var basicSalary = record.data.basicSalary
						// 强行触发renderer事件
						return renderMoney(basicSalary);
					} else {
						totalSum = 0;
						for (var i = 0; i < store.getCount() - 1; i++) {
							totalSum += store.getAt(i).get('basicSalary');
						}

						return "<font color='red'>" + renderMoney(totalSum)
								+ "</font>";
					}

				}
			}, {
				header : "工龄工资",
				width : 130,
				sortable : true,
				align : 'left',
				dataIndex : 'workAgeSalary',
				renderer : function(value, cellmeta, record, rowIndex,
						columnIndex, store) {
					if (rowIndex < store.getCount() - 1) {
						var workAgeSalary = record.data.workAgeSalary
						// 强行触发renderer事件
						return renderMoney(workAgeSalary);
					} else {
						totalSum = 0;
						for (var i = 0; i < store.getCount() - 1; i++) {
							totalSum += store.getAt(i).get('workAgeSalary');
						}

						return "<font color='red'>" + renderMoney(totalSum)
								+ "</font>";
					}

				}
			}, {
				header : "月奖",
				width : 120,
				sortable : true,
				align : 'left',
				dataIndex : 'monthAward',
				renderer : function(value, cellmeta, record, rowIndex,
						columnIndex, store) {
					if (rowIndex < store.getCount() - 1) {
						var monthAward = record.data.monthAward
						// 强行触发renderer事件
						return renderMoney(monthAward);
					} else {
						totalSum = 0;
						for (var i = 0; i < store.getCount() - 1; i++) {
							totalSum += store.getAt(i).get('monthAward');
						}

						return "<font color='red'>" + renderMoney(totalSum)
								+ "</font>";
					}

				}
			}, {
				header : "大奖",
				width : 120,
				sortable : true,
				align : 'left',
				dataIndex : 'bigAward',
				renderer : function(value, cellmeta, record, rowIndex,
						columnIndex, store) {
					if (rowIndex < store.getCount() - 1) {
						var bigAward = record.data.bigAward
						// 强行触发renderer事件
						return renderMoney(bigAward);
					} else {
						totalSum = 0;
						for (var i = 0; i < store.getCount() - 1; i++) {
							totalSum += store.getAt(i).get('bigAward');
						}

						return "<font color='red'>" + renderMoney(totalSum)
								+ "</font>";
					}

				}
			}, {
				header : "总收入",
				width : 120,
				sortable : true,
				align : 'left',
				dataIndex : 'totalIncome',
				renderer : function(value, cellmeta, record, rowIndex,
						columnIndex, store) {
					if (rowIndex < store.getCount() - 1) {
						var totalIncome = record.data.totalIncome
						// 强行触发renderer事件
						return renderMoney(totalIncome);
					} else {
						totalSum = 0;
						for (var i = 0; i < store.getCount() - 1; i++) {
							totalSum += store.getAt(i).get('totalIncome');
						}

						return "<font color='red'>" + renderMoney(totalSum)
								+ "</font>";
					}

				}
			}],
			sm : sm,
			tbar : headerTbar, 
			bbar : new Ext.PagingToolbar({
				pageSize : 18,
				store : store,
				displayInfo : true
		// displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
		// emptyMsg : "没有记录"
		})
	});
	var queryObj = new workerSalary.query();
	grid.on('rowdblclick', function(grid, rowIndex, e) {
		var record = grid.getSelectionModel().getSelected();
		tabs.setActiveTab(1);
		queryObj.getMaxMonth(record.get("workerId"));
		
		
	});
	var tabs = new Ext.TabPanel({
		activeTab : 0,
		items : [{
			id : 'salarysummary',
			title : '员工收入统计',
			items : grid,
			autoScroll : true,
			layout : 'fit'
		}, {
			id : 'salaryquery',
			title : '收入月度查询',
			autoScroll : true,
			items : [queryObj.grid],
			layout : 'fit'
		}]
	});
	tabs.on('tabchange', function(tab, newtab) {
		if (newtab.getId() == 'salaryquery') {
			var record = grid.getSelectionModel().getSelected();
			if(record==null)
			{
				Ext.MessageBox.alert("提示","请选择一条记录查看！");
				return;
			}
			
		}
		if (newtab.getId() == 'salarysummary') {
			queryRecord();
		}
		
		
	})
	var view = new Ext.Viewport({
				layout : 'fit',
				items : [tabs]
			});

		

})