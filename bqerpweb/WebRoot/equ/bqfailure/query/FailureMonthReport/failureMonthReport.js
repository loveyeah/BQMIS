Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
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
	var nowdate = new Date();
	var date1 = nowdate.toLocaleDateString();
	var startDate = ChangeDateToString(nowdate.getFirstDateOfMonth());
	var endDate = ChangeDateToString(nowdate.getLastDateOfMonth());
	var exdate;
	if (date1.substring(6, 7) == '月') {
		exdate = date1.substring(0, 4) + '-0' + date1.substring(5, 6);
	} else {
		exdate = date1.substring(0, 4) + "-" + date1.substring(5, 7);
	}
	var enddate = exdate;
	var monthDate = new Ext.form.TextField({
				name : '_monthDate',
				value : exdate,
				id : 'monthDate',
				fieldLabel : "月份",
				style : 'cursor:pointer',
				cls : 'Wdate',
				// width : 150,
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
	var btnNoEliminate = new Ext.Button({
				id : 'btnNoEliminate',
				text : '未消除缺陷查询',
				iconCls : 'query',
				handler : function() {
					var rec = Grid.getSelectionModel().getSelected();
					if (rec) {
						var dominationProfession = rec
								.get('dominationProfession');
						var date = Ext.get('monthDate').dom.value + "-01";
						var monthdate = new Date(date.replace(/-/g, "/"));
						var sdate = monthdate.getFirstDateOfMonth();
						var edate = monthdate.getLastDateOfMonth();
						startDate = ChangeDateToString(sdate);
						endDate = ChangeDateToString(edate);
						not_ds.load({
									params : {
										start : 0,
										limit : 18,
										startDate : startDate,
										endDate : endDate,
										dominationProfession : dominationProfession
									}
								});
						not_win.show();
					} else {
						Ext.Msg.alert('提示', '请从列表中选择查看的记录！');
					}
				}

			});
	var btnBack = new Ext.Button({
				id : 'btnBack',
				text : '退回缺陷查询',
				iconCls : 'query',
				handler : function() {
					var rec = Grid.getSelectionModel().getSelected();
					if (rec) {
						var dominationProfession = rec
								.get('dominationProfession');
						var date = Ext.get('monthDate').dom.value + "-01";
						var monthdate = new Date(date.replace(/-/g, "/"));
						var sdate = monthdate.getFirstDateOfMonth();
						var edate = monthdate.getLastDateOfMonth();
						startDate = ChangeDateToString(sdate);
						endDate = ChangeDateToString(edate);
						back_ds.load({
									params : {
										start : 0,
										limit : 18,
										startDate : startDate,
										endDate : endDate,
										dominationProfession : dominationProfession
									}
								});
						back_win.show();
					} else {
						Ext.Msg.alert('提示', '请从列表中选择查看的记录！');
					}
				}
			});
	var btnQuery = new Ext.Button({
				id : 'btnQuery',
				text : '统 计',
				iconCls : 'query',
				minWidth : 50,
				handler : function() {
					ds.load({
								params : {
									startDate : Ext.get('monthDate').dom.value
								}
							});
				}
			});
	var btnExport = new Ext.Button({
				id : 'btnPrint',
				text : '导 出',
				iconCls : 'export',
				minWidth : 50,
				handler : function() {
					reportexport();
				}
			});
	var tbar = new Ext.Toolbar({
				items : ['查询月份：', monthDate, '-', btnNoEliminate, '-', btnBack,
						'-', btnQuery, '-', btnExport]
			})

	var item = Ext.data.Record.create([{
				name : 'dominationProfession'
			}, {
				name : 'dominationProfessionName'
			}, {
				name : 'eliminateCount'
			}, {
				name : 'noeliminateCount'
			}, {
				name : 'repeatCount'
			}, {
				name : 'backCount'
			}
			// add by liuyi 20100428 增加消缺率
			, {
				name : 'rate'
			}
			// add by ltong 20100527 超时缺陷数 缺陷及时率 缺陷总数
			, {
				name : 'overTime'
			}, {
				name : 'failureInTime'
			}, {
				name : 'totalNum'
			}]);
	var sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			});
	var cm = new Ext.grid.ColumnModel({
				columns : [sm, new Ext.grid.RowNumberer({
									header : '序号',
									width : 35,
									align : 'center'
								}), {
							header : '检修专业',
							dataIndex : 'dominationProfessionName',
							align : 'left'
						}, {
							header : '已消除缺陷数',
							dataIndex : 'eliminateCount',
							align : 'center'
						}, {
							header : '未消除缺陷数',
							dataIndex : 'noeliminateCount',
							align : 'center'
						}, {
							header : '退回缺陷数',
							dataIndex : 'backCount',
							align : 'center'
						}
						// add by liuyi 20100428
						, {
							header : '消缺率',
							dataIndex : 'rate',
							align : 'center'
						}// add by ltong 20100527
						, {
							header : '超时缺陷数',
							dataIndex : 'overTime',
							align : 'center'
						}, {
							header : '缺陷总数',
							dataIndex : 'totalNum',
							align : 'center',
							hidden : true
						}, {
							header : '缺陷及时率',
							dataIndex : 'failureInTime',
							align : 'center'
						}]
			});
	cm.defaultSortable = true;
	var ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'bqfailure/failureMonthReport.action'
						}),
				reader : new Ext.data.JsonReader({

				}		, item)
			});
	ds.load({
				params : {
					startDate : exdate
				}
			});
	// add by liuyi 20100428
	ds.on('load', function() {
		var eliminateCountTotal = 0;
		var noeliminateCountTotal = 0;
		var repeatCountTotal = 0;
		var backCountTotal = 0;
		var rateTotal = '0.00%';
		var overTimeTotal = 0;
		var totalNumTotal = 0;
		var failureInTimeTotal = '0.00%';
		ds.each(function(re) {
			eliminateCountTotal += (((re.get('eliminateCount') == null || re
					.get('eliminateCount') == '') ? 0 : re
					.get('eliminateCount')) - 0);
			noeliminateCountTotal += (((re.get('noeliminateCount') == null || re
					.get('noeliminateCount') == '') ? 0 : re
					.get('noeliminateCount')) - 0);
			repeatCountTotal += (((re.get('repeatCount') == null || re
					.get('repeatCount') == '') ? 0 : re.get('repeatCount')) - 0);
			backCountTotal += (((re.get('backCount') == null || re
					.get('backCount') == '') ? 0 : re.get('backCount')) - 0);
			overTimeTotal += (((re.get('overTime') == null || re
					.get('overTime') == '') ? 0 : re.get('overTime')) - 0);
			totalNumTotal += (((re.get('totalNum') == null || re
					.get('totalNum') == '') ? 0 : re.get('totalNum')) - 0);

			var rateIterator = '0.00%'
			var den = (re.get('eliminateCount') - 0)
					+ (re.get('noeliminateCount') - 0)
					+ (re.get('backCount') - 0);
			if (den != 0) {
				rateIterator = ((re.get('eliminateCount') - 0) / den * 100)
						.toFixed(2)
						+ '%';
			}
			re.set('rate', rateIterator);
		})
		var denominator = (eliminateCountTotal - 0)
				+ (noeliminateCountTotal - 0) + (backCountTotal - 0);
		if (denominator != 0) {
			rateTotal = ((eliminateCountTotal - 0) / denominator * 100)
					.toFixed(2)
					+ '%';
		}
		var inTime = (totalNumTotal - 0) - (overTimeTotal - 0);
		if ((totalNumTotal - 0) != 0) {
			failureInTimeTotal = (inTime / (totalNumTotal - 0) * 100).toFixed(2)
					+ '%';
		}
		var data = new item({
					'dominationProfession' : null,
					'dominationProfessionName' : '总计',
					'eliminateCount' : eliminateCountTotal,
					'noeliminateCount' : noeliminateCountTotal,
					'repeatCount' : repeatCountTotal,
					'backCount' : backCountTotal,
					'rate' : rateTotal,
					'overTime' : overTimeTotal,
					'failureInTime' : failureInTimeTotal
				})
		ds.add(data)
	})
	var Grid = new Ext.grid.GridPanel({
				ds : ds,
				cm : cm,
				sm : sm,
				split : true,
				autoScroll : true,
				tbar : tbar,
				loadMask : {
					msg : '读取数据中 ...'
				},
				border : false,
				viewConfig : {
					forceFit : true
				}
			});
			
	//add by sychen 20100915 
	Grid.on("cellclick",showDeptDetail);
	function showDeptDetail(grid, rowIndex, columnIndex, e)
	{
		if (Grid.getSelectionModel().hasSelection()) {
			var rec = Grid.getSelectionModel().getSelected();
			var colName = grid.getColumnModel().getDataIndex(columnIndex);
			    if(rec.get("dominationProfessionName")!="总计"&&
				    (colName=="eliminateCount"||colName=="noeliminateCount"||
				     colName=="backCount"||colName=="overTime")){
					 	var defectType="";
					 	 if(colName=="eliminateCount"){
					 	     defectType="A";
					 	    }
					 	 else  if(colName=="noeliminateCount"){
					 	     defectType="B";
					 	    }
					 	 else  if(colName=="backCount"){
					 	     defectType="C";
					 	    }
					 	 else  if(colName=="overTime"){
					 	     defectType="D";
					 	    }
					 	 else 
					 	   return;
					  var url = "equDeptDetail.jsp";
					  var args = new Object();
					  args.specialty = rec.get("dominationProfession");
					  args.defectType = defectType;
					  args.writeDate = monthDate.getValue();

					var obj = window.showModalDialog(url, args,
							'status:no;dialogWidth=750px;dialogHeight=350px');
					}
				}
	}		
	
	//add by sychen 20100915 end
	
	function tableToExcel(tableHTML) {
		window.clipboardData.setData("Text", tableHTML);
		try {
			var ExApp = new ActiveXObject("Excel.Application");
			var ExWBk = ExApp.workbooks.add();
			var ExWSh = ExWBk.worksheets(1);
			ExApp.Columns(1).ColumnWidth = 15;
			ExApp.Columns(2).ColumnWidth = 15;
			ExApp.Columns(3).ColumnWidth = 15;
			ExApp.Columns(4).ColumnWidth = 15;
			ExApp.Columns(5).ColumnWidth = 15;
			ExApp.DisplayAlerts = true;
			ExWSh.Paste();
			ExApp.visible = true;
		} catch (e) {
			if (e.number != -2146827859)
				alert("您的电脑没有安装Microsoft Excel软件！");
			return false;
		}
		ExWBk.worksheets(1).Paste;
	}

	function reportexport() {
		// modified by liuyi 20100428
		// Ext.Ajax.request({
		// url : 'bqfailure/failureMonthReport.action',
		// params : {
		// startDate : Ext.get('monthDate').dom.value
		// },
		// success : function(response) {
		// var json = eval('(' + response.responseText.trim() + ')');
		// var records = json;
		// var html = ['<table border=1><tr><td align="center"
		// colspan="5"><b><h1>缺陷统计月报表('+Ext.get('monthDate').dom.value+')</h1></b></td></tr><tr><th>检修专业</th><th>已消除缺陷数</th><th>未消除缺陷数</th><th>缺陷重复发生数</th><th>退回缺陷数</th></tr>'];
		// for (var i = 0; i < records.length; i += 1) {
		// var rc = records[i];
		// html.push('<tr><td>' + rc.dominationProfessionName + '</td><td>'
		// + rc.eliminateCount + '</td><td>' + rc.noeliminateCount
		// + '</td><td>' + rc.repeatCount + '</td><td>'
		// + rc.backCount + '</td></tr>');
		// }
		// html.push('</table>');
		// html = html.join('');
		// tableToExcel(html);
		// },
		// failure : function(response) {
		// Ext.Msg.alert('提示', '导出失败！');
		// }
		// });

		var html = ['<table border=1><tr><td align="center" colspan="7"><b><h1>缺陷统计月报表('
				+ Ext.get('monthDate').dom.value
				+ ')</h1></b></td></tr><tr><th>检修专业</th><th>已消除缺陷数</th><th>未消除缺陷数</th><th>退回缺陷数</th><th>消缺率</th><th>超时缺陷数</th><th>缺陷及时率</th></tr>'];
		for (var i = 0; i < ds.getCount(); i += 1) {
			var rc = ds.getAt(i).data;
			html.push('<tr><td>' + rc.dominationProfessionName + '</td><td>'
					+ rc.eliminateCount + '</td><td>' + rc.noeliminateCount
					+ '</td><td>' + rc.backCount + '</td><td>' + rc.rate
					+ '</td><td>' + rc.overTime + '</td><td>'
					+ rc.failureInTime + '</td></tr>');
		}
		html.push('</table>');
		html = html.join('');
		tableToExcel(html);

	}
	new Ext.Viewport({
				enableTabScroll : true,
				layout : "fit",
				collapsible : true,
				split : true,
				margins : '0 0 0 0',
				items : [Grid]
			});

	var Failure = new Ext.data.Record.create([{
				name : 'id'
			}, {
				name : 'failureCode'
			}, {
				name : 'woStatusName'
			}, {
				name : 'failuretypeName'
			}, {
				name : 'failurePri'
			}, {
				name : 'attributeCode'
			}, {
				name : 'equName'
			}, {
				name : 'failureContent'
			}, {
				name : 'locationDesc'
			}, {
				name : 'findDate'
			}, {
				name : 'findBy'
			}, {
				name : 'findByName'
			}, {
				name : 'findDept'
			}, {
				name : 'findDeptName'
			}, {
				name : 'woCode'
			}, {
				name : 'bugCode'
			}, {
				name : 'bugName'
			}, {
				name : 'failuretypeCode'
			}, {
				name : 'failureLevel'
			}, {
				name : 'woStatus'
			}, {
				name : 'preContent'
			}, {
				name : 'ifStopRun'
			}, {
				name : 'ifStopRunName'
			}, {
				name : 'runProfession'
			}, {
				name : 'dominationProfession'
			}, {
				name : 'dominationProfessionName'
			}, {
				name : 'repairDep'
			}, {
				name : 'repairDepName'
			}, {
				name : 'installationCode'
			}, {
				name : 'installationDesc'
			}, {
				name : 'belongSystem'
			}, {
				name : 'belongSystemName'
			}, {
				name : 'likelyReason'
			}, {
				name : 'woPriority'
			}, {
				name : 'writeBy'
			}, {
				name : 'writeByName'
			}, {
				name : 'writeDept'
			}, {
				name : 'writeDeptName'
			}, {
				name : 'writeDate'
			}, {
				name : 'repairDept'
			}, {
				name : 'realrepairDept'
			}, {
				name : 'ifOpenWorkorder'
			}, {
				name : 'ifRepeat'
			}, {
				name : 'supervisor'
			}, {
				name : 'workFlowNo'
			}, {
				name : 'wfState'
			}, {
				name : 'entrepriseCode'
			}, {
				name : 'isuse'
			}]);

	var not_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'bqfailure/findNotEliminateFailureList.action',
							method : 'post'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : 'total',
							root : 'list'
						}, Failure)
			});

	// 单击Grid行事件
	var not_sm = new Ext.grid.RowSelectionModel({
				singleSelect : true
			});

	var colModel = new Ext.grid.ColumnModel([
			new Ext.grid.CheckboxSelectionModel(), new Ext.grid.RowNumberer({
						header : '序号',
						width : 35
					}), {
				header : "id",
				dataIndex : "id",
				hidden : true
			}, {
				header : '缺陷编码',
				dataIndex : 'failureCode',
				width : 100
			}, {
				header : '所属系统',
				dataIndex : 'belongSystemName',
				width : 100
			}, {
				header : '缺陷内容',
				dataIndex : 'failureContent',
				width : 300,
				renderer : function change(val) {
					return ' <span style="white-space:normal;">' + val
							+ ' </span>';
				}
			}, {
				header : '管辖专业',
				dataIndex : 'dominationProfessionName',
				width : 60
			}, {
				header : '检修部门',
				dataIndex : 'repairDepName',
				width : 120
			}, {
				header : '状态',
				dataIndex : 'woStatusName',
				width : 120
			}, {
				header : '发现时间',
				dataIndex : 'findDate',
				width : 120
			}, {
				header : '发现人',
				dataIndex : 'findByName',
				width : 60
			}, {
				dataIndex : "failuretypeCode",
				hidden : true
			}, {
				header : '发现部门',
				dataIndex : 'findDeptName',
				width : 100
			}, {
				header : '类别',
				dataIndex : 'failuretypeName',
				width : 60
			}, {
				dataIndex : "failureLevel",
				hidden : true
			}, {
				header : '优先级',
				dataIndex : 'failurePri',
				width : 140
			}, {
				header : '设备功能码',
				dataIndex : 'attributeCode',
				width : 120
			}, {
				header : '故障名称',
				dataIndex : "bugName"
			}, {
				header : '设备名称',
				dataIndex : 'equName',
				width : 120
			}, {
				header : '填写人',
				dataIndex : 'writeByName',
				width : 60
			}, {
				header : '填写人部门',
				dataIndex : 'writeDeptName',
				width : 100
			}, {
				header : '填写时间',
				dataIndex : 'writeDate',
				width : 120
			}, {
				dataIndex : "ifStopRun",
				hidden : true
			}, {
				dataIndex : "ifStopRunName",
				hidden : true
			}, {
				dataIndex : "belongSystem",
				hidden : true
			}]);
	// 排序
	colModel.defaultSortable = true;

	var failureGrid = new Ext.grid.GridPanel({
				id : 'failure-grid',
				width : 630,
				height : 415,
				border : false,
				autoScroll : true,
				// title : '未消缺缺陷列表',
				ds : not_ds,
				cm : colModel,
				sm : not_sm,
				loadMask : {
					msg : '读取数据中 ...'
				},
				border : true,
				bbar : new Ext.PagingToolbar({
							pageSize : 18,
							store : not_ds,
							displayInfo : true,
							displayMsg : '显示第 {0} 条到 {1} 条记录，一共 {2} 条',
							emptyMsg : "没有记录"
						})
			});
	failureGrid.on("rowcontextmenu", function(g, i, e) {
		e.stopEvent();
		var record = failureGrid.getStore().getAt(i);
		// 右键菜单
		var menu = new Ext.menu.Menu({
			id : 'mainMenu',
			items : [new Ext.menu.Item({
				text : '详细信息',
				handler : function() {
					Ext.Ajax.request({
						url : 'bqfailure/findFailureById.action?failure.id='
								+ record.get("id"),
						method : 'post',
						waitMsg : '正在处理数据...',
						success : function(result, request) {
							var json = eval('(' + result.responseText + ')');
							window
									.showModalDialog(
											"/power/equ/bqfailure/query/detailFailure.jsp",
											json,
											'dialogWidth=860px;dialogHeight=500px;center=yes;help=no;resizable=no;status=no;');
						},
						failure : function(result, request) {
							Ext.Msg.alert('提示', '操作失败，请联系管理员！');
						}
					});
				}
			}), new Ext.menu.Item({
				text : '图形展示',
				handler : function() {
					if (record.get("workFlowNo") != "") {
						window
								.open("/power/workflow/manager/show/show.jsp?entryId="
										+ record.get("workFlowNo"));
					}
				}
			})]
		});
		var coords = e.getXY();
		menu.showAt([coords[0], coords[1]]);
	});
	var back_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'bqfailure/findBackFailureList.action',
							method : 'post'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : 'total',
							root : 'list'
						}, Failure)
			})
	not_ds.on('beforeload', function() {
				Ext.apply(this.baseParams, {
							startDate : startDate,
							endDate : endDate
						});
			})
	back_ds.on('beforeload', function() {
				Ext.apply(this.baseParams, {
							startDate : startDate,
							endDate : endDate
						});
			})
	var backGrid = new Ext.grid.GridPanel({
				id : 'backgrid',
				width : 630,
				height : 415,
				border : false,
				autoScroll : true,
				// title : '退回缺陷列表',
				ds : back_ds,
				cm : colModel,
				sm : not_sm,
				loadMask : {
					msg : '读取数据中 ...'
				},
				border : true,
				bbar : new Ext.PagingToolbar({
							pageSize : 18,
							store : back_ds,
							displayInfo : true,
							displayMsg : '显示第 {0} 条到 {1} 条记录，一共 {2} 条',
							emptyMsg : "没有记录"
						})
			});
	backGrid.on("rowcontextmenu", function(g, i, e) {
		e.stopEvent();
		var record = backGrid.getStore().getAt(i);
		// 右键菜单
		var menu = new Ext.menu.Menu({
			id : 'mainMenu',
			items : [new Ext.menu.Item({
				text : '详细信息',
				handler : function() {
					Ext.Ajax.request({
						url : 'bqfailure/findFailureById.action?failure.id='
								+ record.get("id"),
						method : 'post',
						waitMsg : '正在处理数据...',
						success : function(result, request) {
							var json = eval('(' + result.responseText + ')');
							window
									.showModalDialog(
											"/power/equ/bqfailure/query/detailFailure.jsp",
											json,
											'dialogWidth=860px;dialogHeight=500px;center=yes;help=no;resizable=no;status=no;');
						},
						failure : function(result, request) {
							Ext.Msg.alert('提示', '操作失败，请联系管理员！');
						}
					});
				}
			}), new Ext.menu.Item({
				text : '图形展示',
				handler : function() {
					if (record.get("workFlowNo") != ""
							&& record.get("workFlowNo") != null) {
						window
								.open("/power/workflow/manager/show/show.jsp?entryId="
										+ record.get("workFlowNo"));
					}
				}
			})]
		});
		var coords = e.getXY();
		menu.showAt([coords[0], coords[1]]);
	});
	var not_win = new Ext.Window({
				autoScroll : true,
				title : '未消除缺陷列表(右键查看详细)',
				width : 642,
				border : false,
				height : 450,
				closeAction : 'hide',
				items : [failureGrid]
			});
	var back_win = new Ext.Window({
				autoScroll : true,
				width : 642,
				title : '退回缺陷列表(右键查看详细)',
				border : false,
				height : 450,
				closeAction : 'hide',
				items : [backGrid]
			});

})