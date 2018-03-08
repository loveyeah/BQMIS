Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	Ext.ns("workerSalary.query");
	workerSalary.query = function() {
		function getDate() {
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
		var MyRecord = Ext.data.Record.create([{
			name : 'workerName',
			mapping : 0
		}, {
			name : 'baseWage',
			mapping : 1
		}, {
			name : 'remainWage',
			mapping : 2
		}, {
			name : 'pointWage',
			mapping : 3
		}, {
			name : 'ageWage',
			mapping : 4
		}, {
			name : 'operationWage',
			mapping : 5
		}, {
			name : 'overtimeWage',
			mapping : 6
		}, {
			name : 'deductionWage',
			mapping : 7
		}, {
			name : 'others',
			mapping : 8
		}, {
			name : 'totalWage',
			mapping : 9
		}, {
			name : 'wageMemo',
			mapping : 10
		}, {
			name : 'individualWageOne',
			mapping : 11
		}, {
			name : 'individualWageTwo',
			mapping : 12
		}, {
			name : 'monthAwards',
			mapping : 13
		}, {
			name : 'bigAwards',
			mapping : 14
		}, {
			name : 'totalMoney',
			mapping : 15
		}]);
		var dataProxy = new Ext.data.HttpProxy({
			url : 'com/getSalaryByMonth.action'
		});

		var theReader = new Ext.data.JsonReader({
			root : "list",
			totalProperty : "totalCount"
		}, MyRecord);
		var store = new Ext.data.Store({
			proxy : dataProxy,
			reader : theReader
		});
  var flag="";
		// 月份时间选择
		var month = new Ext.form.TextField({
			style : 'cursor:pointer',
			name : 'time',
			fieldLabel : '月份',
			readOnly : true,
			anchor : "80%",
			value : getDate(),
			listeners : {
				focus : function() {
					WdatePicker({
						startDate : '%y-%M',
						alwaysUseStartDate : false,
						dateFmt : 'yyyy-MM',
						isShowClear : false,
						onpicked : function(v) {
							 flag="1";
							queryRecord(workerId);
                             
							this.blur();
						}
					});
				}
			}
		});

		var sm = new Ext.grid.CheckboxSelectionModel({
				// singleSelect : true
		});
		var tbar = new Ext.Toolbar({
					items : ['月份', month, {
						text : "查询",
						iconCls : 'query',
						handler : queryRecord
					}, {
						text : "导出",
						iconCls : Constants.CLS_EXPORT,
						handler : exportRecord
					}]
				});
	var  workerId="";
		function queryRecord(workerId) {
		
			store.baseParams = {
				month : month.getValue(),
				empId:workerId,
				flag:flag

			}

			store.load({
				params : {
					start : 0,
					limit : 18
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
			var yearMonth = month.getValue().substr(0, 7);
			Ext.Ajax.request({
				url : 'com/getSalaryByMonth.action',
				method : 'post',
				params : {
					empId:workerId
				},
				success : function(response) {
					var json = eval('(' + response.responseText.trim() + ')');
					var records = json.list;

					var title = "";
					title = "收入月度查询";
					var tableHeader = "<table border=1 ><tr><th align=center colspan = 16 style=font-size:5.55mm; FONT-FAMILY: 仿宋_GB2312>"
							+ title + "</th></tr>";
					var html = [tableHeader];

					html
							.push("<tr><th align=right colspan = 3>月份：</th><td  align=left colspan = 13>"
									+ yearMonth + "</td></tr>")

					html
							.push("<tr><th>员工姓名</th><th>基础工资</th><th>保留工资</th><th>薪点工资</th><th>工龄工资</th><th>运行津贴</th>"
									+ "<th>加班工资</th><th>扣除工资</th><th>其他</th><th >总金额</th><th >备注</th><th> 单项奖1</th>"
									+ "<th>单项奖2</th><th>月奖</th><th>大奖</th><th >总收入</th></tr>")

					for (var i = 0; i < records.length; i += 1) {

						var rc = records[i];

						html
								.push('<tr><td align ="left">'
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
										+ (rc[6] == null ? "" : rc[6])
										+ '</td><td align ="left">'
										+ (rc[7] == null ? "" : rc[7])
										+ '</td><td align ="left">'
										+ (rc[8] == null ? "" : rc[8])
										+ '</td><td align ="left">'
										+ (rc[9] == null ? "" : rc[9])
										+ '</td><td align ="left">'
										+ (rc[10] == null ? "" : rc[10])
										+ '</td><td align ="left">'
										+ (rc[11] == null ? "" : rc[11])
										+ '</td><td align ="left">'
										+ (rc[12] == null ? "" : rc[12])
										+ '</td><td align ="left">'
										+ (rc[13] == null ? "" : rc[13])
										+ '</td><td align ="left">'
										+ (rc[14] == null ? "" : rc[14])
										+ '</td><td align ="left">'
										+ (rc[15] == null ? "" : rc[15])
										+ '</td></tr>');
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
			layout : 'fit',
			region : "center",
			autoScroll : true,
			frame : false,
			border : false,
			store : store,
			columns : [new Ext.grid.RowNumberer({
				header : '序号',
				width : 35
			}), {
				header : "员工名称",
				sortable : true,
				hidden : false,
				align : 'left',
				dataIndex : 'workerName'
			}, {
				header : "基础工资",
				sortable : true,
				hidden : false,
				align : 'left',
				dataIndex : 'baseWage'
			}, {
				header : "保留工资",
				width : 120,
				sortable : true,
				align : 'left',
				dataIndex : 'remainWage'
			}, {
				header : "薪点工资",
				width : 100,
				sortable : true,
				align : 'left',
				dataIndex : 'pointWage'
			}, {
				header : "工龄工资",
				width : 130,
				sortable : true,
				align : 'left',
				dataIndex : 'ageWage'
			}, {
				header : "运行津贴",
				width : 120,
				sortable : true,
				align : 'left',
				dataIndex : 'operationWage'
			}, {
				header : "加班工资",
				width : 120,
				sortable : true,
				align : 'left',
				dataIndex : 'overtimeWage'
			}, {
				header : "扣除工资",
				width : 120,
				sortable : true,
				align : 'left',
				dataIndex : 'deductionWage'
			}, {
				header : "其他",
				width : 120,
				sortable : true,
				align : 'left',
				dataIndex : 'others'
			}, {
				header : "总工资",
				width : 120,
				sortable : true,
				align : 'left',
				dataIndex : 'totalWage'
			}, {
				header : "备注",
				width : 120,
				sortable : true,
				align : 'left',
				dataIndex : 'wageMemo'
			}, {
				header : "单项奖1",
				width : 120,
				sortable : true,
				align : 'left',
				dataIndex : 'individualWageOne'
			}, {
				header : "单项奖2",
				width : 120,
				sortable : true,
				align : 'left',
				dataIndex : 'individualWageTwo'
			}, {
				header : "月奖",
				width : 120,
				sortable : true,
				align : 'left',
				dataIndex : 'monthAwards'
			}, {
				header : "大奖",
				width : 120,
				sortable : true,
				align : 'left',
				dataIndex : 'bigAwards'
			}, {
				header : "总收入",
				width : 120,
				sortable : true,
				align : 'left',
				dataIndex : 'totalMoney'
			}],
			sm : sm,
			tbar : tbar,
			// 分页
			viewConfig : {
				forceFit : true
			},
			bbar : new Ext.PagingToolbar({
				pageSize : 18,
				store : store,
				displayInfo : true
			})
		});
 
  function getMaxMonth(empId) {
  	        workerId=empId;
			Ext.Ajax.request({
				url : 'com/getSalaryByMonth.action',
				method : 'post',
				params : {
					empId : workerId
				},
				success : function(response) {
					var json = eval('(' + response.responseText.trim() + ')');
					var records = json.list;
					if(records!=null)
					{
					var rec = records[0]
					var time = rec[16];
					if (time != null && time != "") {
						month.setValue(time);
                        queryRecord(workerId);
					}

				}
				}
			})

		}
 

		return {
			grid : grid,
			query:queryRecord,
			getMaxMonth:getMaxMonth
			
		
			
		
		}
	};

})