Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	// 登陆人部门ID
//	var workerDeptId = null;
	// 登陆人一级部门ID
	//var deptId = null;
	// 工作流状态
	var workFlowState = "";

	// 取登陆人一级部门
//	function getDeptCode(id) {
//		Ext.lib.Ajax.request('POST', 'hr/initRewardGrantDept.action?deptId='
//						+ workerDeptId, {
//					success : function(action) {
//						var result = eval("(" + action.responseText + ")");
//						if (result != "") {
//							deptId = result[0]
//						} else {
//							deptId = workerDeptId
//						}
//						
//					}
//				});
//	}
//	function getWorkCode() {
//		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
//					success : function(action) {
//						var result = eval("(" + action.responseText + ")");
//						if (result.workerCode) {
//							workerDeptId = result.deptId;
//							getDeptCode(workerDeptId);
//						}
//					}
//				});
//	}

	// 取最大时间
	function monthInit() {
		Ext.Ajax.request({
					url : 'hr/getMaxGarntMonth.action',
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						if (result) {
							grantMonth.setValue(result);
							query();
						}
					}
				});
	}

	// 时间
	var grantMonth = new Ext.form.TextField({
				anchor : "85%",
				style : 'cursor:pointer',
				name : "rwGrant.grantMonth",
				fieldLabel : '时间',
				allowBlank : true,
				readOnly : true,
				listeners : {
					focus : function() {
						WdatePicker({
									isShowClear : true,
									startDate : '%y-%M',
									dateFmt : 'yyyy-MM',
									onpicked : function() {
										storeGroupName.load({
													params : {
														monthDate : grantMonth
																.getValue(),
														deptId : deptName
																.getValue()
													}
												})
									}
								});
						this.blur();
					}

				}
			});

	// 部门
	var deptRec = new Ext.data.Record.create([{
				name : 'deptId',
				mapping : '0'
			}, {
				name : 'deptName',
				mapping : '1'
			}])

	var storeDeptName = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'hr/getAllFirstDept.action'
						}),
				reader : new Ext.data.JsonReader({}, deptRec)
			});

	storeDeptName.load();

	storeDeptName.on('load', function() {
				var temp = new deptRec({
							deptId : null,
							deptName : '全部'
						})
				storeDeptName.insert(0, temp)
			});

	var deptName = new Ext.form.ComboBox({
				id : 'deptName',
				fieldLabel : "部门",
				store : storeDeptName,
				displayField : "deptName",
				valueField : "deptId",
				mode : 'local',
				triggerAction : 'all',
				readOnly : true,
				// value : '',
				anchor : "85%",
				listeners : {
					select : function() {
						storeGroupName.load({
									params : {
										monthDate : grantMonth.getValue(),
										deptId : deptName.getValue()
									}
								})
					}
				}
			})

	// 班组
	var groupRec = new Ext.data.Record.create([{
				name : 'groupId',
				mapping : '0'
			}, {
				name : 'groupName',
				mapping : '1'
			}])
	var storeGroupName = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'hr/getApproveGroup.action'
						}),
				// params : {
				// deptId : deptName.getValue()
				// },
				reader : new Ext.data.JsonReader({}, groupRec)
			});

	// storeGroupName.load();

	storeGroupName.on('load', function() {
				var temp = new groupRec({
							groupId : null,
							groupName : '全部'
						})
				storeGroupName.insert(0, temp)
			});
	var groupName = new Ext.form.ComboBox({
				id : 'groupName',
				fieldLabel : "班组",
				store : storeGroupName,
				displayField : "groupName",
				valueField : "groupId",
				mode : 'local',
				triggerAction : 'all',
				readOnly : true,
				// value : '',
				anchor : "85%"
			})

			
	// 
	var typeYear = new Ext.data.SimpleStore({
				data : [['', '全部'],['0', '未上报'], ['1', '已上报'], ['2', '已汇总'], ['3', '已审批'], ['4', '已打印'], ['5', '高管已审核']],
				fields : ['value', 'text']
			})
	var cbStatus = new Ext.form.ComboBox({
				id : 'cbStatus',
				fieldLabel : '状态',
				store : typeYear,
				displayField : 'text',
				valueField : 'value',
				readOnly : true,
				hiddenName : 'status',
				mode : 'local',
				triggerAction : 'all',
				width : 85,
				value : ''
			});			
			
//	// 按钮
//	var btnApprove = new Ext.Button({
//				id : 'btnApprove',
//				text : '审核',
//				iconCls : 'approve',
//				handler : approve
//			})
	var btnExport = new Ext.Button({
				id : 'btnExport',
				text : '导出',
				iconCls : 'export',
				handler : exportDataFun
			})
// update by sychen 20100902
		var MyRecord = Ext.data.Record.create([{
				name : 'grantId',
				mapping : 0
			}, {
				name : 'detailId',
				mapping : 1
			}, {
				name : 'deptName',
				mapping : 2
			}, {
				name : 'groupName',
				mapping : 3
			}, {
				name : 'fillByName',
				mapping : 4
			}, {
				name : 'coefficientNum',
				mapping : 5
			}, {
				name : 'baseNum',
				mapping : 6
			}, {
				name : 'amountNum',
				mapping : 7
			}, {
				name : 'awardNum',
				mapping : 8
			}, {
				name : 'monthRewardNum',
				mapping : 9
			}, {
				name : 'quantifyCash',
				mapping : 10
			}, {
				name : 'monthAssessNum',
				mapping : 11
			}, {
				name : 'quantifyAssessNum',
				mapping : 12
			}, {
				name : 'totalNum',
				mapping : 13
			}, {
				name : 'signBy',
				mapping : 14
			}, {
				name : 'memo',
				mapping : 15
			}, {
				name : 'fillBy',
				mapping : 16
			}, {
				name : 'fillDate',
				mapping : 17
			}, {
				name : 'empId',
				mapping : 18
			}, {
				name : 'status',
				mapping : 19
			}, {//add by sychen 20100903
				name : 'addValue',
				mapping : 20
			}]);	
			
// update by sychen 20100902 end
			
//	var MyRecord = Ext.data.Record.create([{
//				name : 'grantId',
//				mapping : 0
//			}, {
//				name : 'detailId',
//				mapping : 1
//			}, {
//				name : 'deptName',
//				mapping : 2
//			}, {
//				name : 'groupName',
//				mapping : 3
//			}, {
//				name : 'fillByName',
//				mapping : 4
//			}, {
//				name : 'coefficientNum',
//				mapping : 5
//			}, {
//				name : 'baseNum',
//				mapping : 6
//			}, {
//				name : 'amountNum',
//				mapping : 7
//			}, {
//				name : 'awardNum',
//				mapping : 8
//			}, {
//				name : 'totalNum',
//				mapping : 9
//			}, {
//				name : 'signBy',
//				mapping : 10
//			}, {
//				name : 'memo',
//				mapping : 11
//			}, {
//				name : 'fillBy',
//				mapping : 12
//			}, {
//				name : 'fillDate',
//				mapping : 13
//			}, {
//				name : 'empId',
//				mapping : 14
//			}, {//add by qxjiao 20100727
//				name : 'status',
//				mapping : 15
//			}]);

	var DataProxy = new Ext.data.HttpProxy({
				url : 'hr/getApproveRewardGrandList.action'
			});

	var TheReader = new Ext.data.JsonReader({
			// root : "list",
			// totalProperty : "totalCount"
			}, MyRecord);

	var listStore = new Ext.data.Store({
				proxy : DataProxy,
				reader : TheReader
			});
	//add by sychen 20100902		
	listStore.on("beforeload", function() {
		Ext.Msg.wait("正在查询数据,请等待...");
	});
	listStore.on("load", function() {
		Ext.Msg.hide();
	});
	//add by sychen 20100902 end 
	var sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			});
	var grid = new Ext.grid.EditorGridPanel({
				layout : 'fit',
				region : 'center',
				autoWidth : true,
				enableColumnHide : true,
				enableColumnMove : false,
				store : listStore,
				sm : sm,
				columns : [sm, new Ext.grid.RowNumberer({
									header : "行号",
									width : 35
								}), {
							header : "主表ID",
							sortable : true,
							dataIndex : 'grantId',
							hidden : true
						}, {
							header : "明细表ID",
							sortable : true,
							dataIndex : 'detailId',
							hidden : true
						}, {
							header : "部门",
							width : 100,
							align : 'center',
							sortable : true,
							dataIndex : 'deptName'
						}, {
							header : "班组",
							width : 100,
							align : 'center',
							sortable : true,
							dataIndex : 'groupName'
						}, {
							header : "姓名",
							width : 100,
							align : 'center',
							sortable : true,
							dataIndex : 'fillByName'
						}, {
							header : "系数",
							width : 100,
							sortable : true,
							dataIndex : 'coefficientNum'
						}, {
							header : "基数",
							width : 100,
							sortable : true,
							dataIndex : 'baseNum'
						}, {
							header : "金额",
							width : 100,
							sortable : true,
							hidden:true,// add  by sychen 20100902
							dataIndex : 'amountNum',
							renderer : function(value, cellmeta, record,
									rowIndex, columnIndex, store) {
								if (rowIndex < store.getCount() - 1) {
									var totalSum = 0;
									for (var i = 0; i < store.getCount() - 1; i++) {
										totalSum += store.getAt(i)
												.get('amountNum');
									}
									if (store.getAt(store.getCount() - 1)
											.get('isNewRecord') == 'total') {
										store.getAt(store.getCount() - 1).set(
												'amountNum', totalSum);
									}
									return value;
								} else {
									totalSum = 0;
									for (var i = 0; i < store.getCount() - 1; i++) {
										totalSum += store.getAt(i)
												.get('amountNum');
									}
									return "<font color='red'>" + totalSum
											+ "</font>";
								}
							}

						}, {

							header : "奖励",
							width : 100,
							sortable : true,
							hidden:true,//add by sychen 20100902
							dataIndex : 'awardNum',
							renderer : function(value, cellmeta, record,
									rowIndex, columnIndex, store) {
								if (rowIndex < store.getCount() - 1) {
									var totalSum = 0;
									for (var i = 0; i < store.getCount() - 1; i++) {
										totalSum += store.getAt(i)
												.get('awardNum');
									}
									if (store.getAt(store.getCount() - 1)
											.get('isNewRecord') == 'total') {
										store.getAt(store.getCount() - 1).set(
												'awardNum', totalSum);
									}
									return value;
								} else {
									totalSum = 0;
									for (var i = 0; i < store.getCount() - 1; i++) {
										totalSum += store.getAt(i)
												.get('awardNum');
									}
									return "<font color='red'>" + totalSum
											+ "</font>";
								}
							}

						},{//add by sychen 20100902
							header : "月度奖金",
							width : 100,
							sortable : true,
							dataIndex : 'monthRewardNum'
						}, {

							header : "月度考核",
							width : 100,
							sortable : true,
							dataIndex : 'monthAssessNum'

						}, {
							header : "量化奖金",
							width : 100,
							sortable : true,
							dataIndex : 'quantifyCash'
						}, {

							header : "量化考核",
							width : 100,
							sortable : true,
							dataIndex : 'quantifyAssessNum'
                          
						}, //add by sychen 20100902 end 
						  {//add by sychen 20100903
			              header : "技师工会增加值",
			              width : 150,
			              sortable : true,
			              dataIndex : 'addValue',
			              renderer : function(value, cellmeta, record, rowIndex, columnIndex,
					      store) {
				          if (rowIndex < store.getCount() - 1) {
					             var totalSum = 0;
		                          return value;
				         } else {
				             	totalSum = 0;
					            for (var i = 0; i < store.getCount() - 1; i++) {
						        totalSum += store.getAt(i).get('addValue');
					     }
		                       return totalSum.toFixed(2);
				      }
			          }
		     }, {
							header : "合计",
							width : 100,
							sortable : true,
							dataIndex : 'totalNum',
							renderer : function(value, cellmeta, record,
									rowIndex, columnIndex, store) {
								if (rowIndex < store.getCount() - 1) {
									var totalSum = 0;
									for (var i = 0; i < store.getCount() - 1; i++) {
										totalSum += store.getAt(i)
												.get('totalNum');
									}
									if (store.getAt(store.getCount() - 1)
											.get('isNewRecord') == 'total') {
										store.getAt(store.getCount() - 1).set(
												'totalNum', totalSum);
									}
									return value;
								} else {
									totalSum = 0;
									for (var i = 0; i < store.getCount() - 1; i++) {
										totalSum += store.getAt(i)
												.get('totalNum');
									}
									return "<font color='red'>" + totalSum
											+ "</font>";
								}
							}
						}, {

							header : "签名",
							width : 100,
							sortable : true,
							dataIndex : 'signBy',
							renderer: function(value, cellmeta, record,
									rowIndex, columnIndex, store)
							{
							 	return record.get("fillByName");
							}

						}, {//add by qxjiao 20100727
							header:"状态",
							width:100,
							sortable:true,
							dataIndex:'status',
							renderer:function(value, cellmeta, record,
									rowIndex, columnIndex, store)
							{
								if(value=="0")
							 	return "未上报";
							 	if(value=="1")
							 	return "已上报";
							 	if(value=="2")
							 	return "已汇总";
							 	if(value=="3")
							 	return "已审批";
							 	if(value=="4")
							 	return "已打印";
							 	if(value=="5")
							 	return "高管已审核";
							}
							
						},{

							header : "备注",
							width : 180,
							sortable : true,
							dataIndex : 'memo'

						}],

				autoSizeColumns : true,
				viewConfig : {
					forceFit : true
				},
				tbar : ['时间:', grantMonth, '-', '部门:', deptName, '-', '班组:',
						groupName,'-','状态：',cbStatus, '-', {
							text : "查询",
							iconCls : 'query',
							minWidth : 70,
							handler : query
						}, '-', btnExport]

			});

	grid.on('rowclick', function() {
				var rec = grid.getSelectionModel().getSelected();
				fillByName.setValue(rec.get("fillBy"));
				fillByDate.setValue(rec.get("fillDate"));
			})
	// 编辑前的操作
	grid.on("beforeedit", function(obj) {
				var record = obj.record;
				// 当是统计行时退出编辑不进行编辑
				if (obj.record.get('isNewRecord') == 'total') {
					return false;
				};
			})

	// 制表人
	var fillByName = new Ext.form.TextField({
				id : 'fillByName',
				fieldLabel : '制表人',
				width : 250,
				allowBlank : true,
				readOnly : true
			})
	// 制表时间
	var fillByDate = new Ext.form.TextField({
				width : 250,
				style : 'cursor:pointer',
				fieldLabel : '制表时间',
				allowBlank : true,
				readOnly : true
			});
	var Form = new Ext.Toolbar({
				items : ['制表人:', fillByName, '-', '制表时间:', fillByDate]
			});

	function query() {
		listStore.removeAll();
		listStore.load({
					params : {
						monthDate : grantMonth.getValue(),
						deptId : deptName.getValue(),
						groupId : groupName.getValue(),
						workFlowState : cbStatus.getValue()
					},
					callback : addLine
				})
		grid.stopEditing();
		grid.getView().refresh();
	}

	function MyClass() {
		this.detailId = "";
		this.grantId = "";
		this.empId = "";
		this.coefficientNum = "";
		this.baseNum = "";
		this.amountNum = "";
		this.awardNum = "";
		this.signBy = "";
		this.memo = "";
	}

	// 增加统计行
	function addLine() {
		// 统计行
		var record = new MyRecord({
					grantId : "",
					detailId : "",
					deptName : "",
					groupName : "",
					fillByName : "",
					coefficientNum : "",
					baseNum : "",
					amountNum : "",
					awardNum : "",
					totalNum : "",
					isNewRecord : "total"
				});
		// 原数据个数
		var count = listStore.getCount();

		// 停止原来编辑
		grid.stopEditing();
		// 插入统计行
		listStore.insert(count, record);
		grid.getView().refresh();

	};
	// -------导出---------
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
	function exportDataFun() {
		if (listStore.getTotalCount() == 0) {
			Ext.Msg.alert('提示', '无数据可导出！')
		} else {
			//update by sychen 20100902
			var html = ['<table border=1><tr><th>部门</th><th>班组</th><th>姓名</th><th>系数</th><th>基数</th><th>月度奖金</th>' +
					'<th>月度考核</th><th>量化奖金</th><th>量化考核</th><th>合计</th><th>签名</th><th>备注</th></tr>'];
								for (var i = 0; i < listStore.getTotalCount(); i++) {
				var rc = listStore.getAt(i);
				html
						.push('<tr><td>'
								+ (rc.get('deptName') == null ? "" : rc
										.get('deptName'))
								+ '</td>'
								+ '<td>'
								+ (rc.get('groupName') == null ? "" : rc
										.get('groupName'))
								+ '</td>'
								+ '<td>'
								+ (rc.get('fillByName') == null ? "" : rc
										.get('fillByName'))
								+ '</td>'
								+ '<td>'
								+ (rc.get('coefficientNum') == null ? "" : rc
										.get('coefficientNum'))
								+ '</td>'
								+ '<td>'
								+ (rc.get('baseNum') == null ? "" : rc
										.get('baseNum'))
								+ '</td>'
								+ '<td>'
								+ (rc.get('monthRewardNum') == null ? "" : rc
										.get('monthRewardNum'))
								+ '</td>'
								+ '<td>'
								+ (rc.get('monthAssessNum') == null ? "" : rc
										.get('monthAssessNum'))
								+ '</td>'
								+ '<td>'
								+ (rc.get('quantifyCash') == null ? "" : rc
										.get('quantifyCash'))
								+ '</td>'
								+ '<td>'
								+ (rc.get('quantifyAssessNum') == null ? "" : rc
										.get('quantifyAssessNum'))
								+ '</td>'
								+ '<td>'
								+ (rc.get('addValue') == null ? "" : rc
										.get('addValue'))
								+ '</td>'
								+ '<td>'
								+ (rc.get('totalNum') == null ? "" : rc
										.get('totalNum'))
								+ '</td>'
								+ '<td>'
								+ (rc.get('fillByName') == null ? "" : rc
										.get('fillByName'))
								+ '</td>'
								+ '<td>'
								+ (rc.get('memo') == null ? "" : rc.get('memo'))
								+ '</td>' + '</tr>')
			}
			//update by sychen 20100902 end 
			
//			var html = ['<table border=1><tr><th>部门</th><th>班组</th><th>姓名</th><th>系数</th><th>基数</th><th>金额</th><th>奖励</th><th>合计</th>'
//					+ '<th>签名</th><th>备注</th></tr>'];		
//			for (var i = 0; i < listStore.getTotalCount(); i++) {
//				var rc = listStore.getAt(i);
//				html
//						.push('<tr><td>'
//								+ (rc.get('deptName') == null ? "" : rc
//										.get('deptName'))
//								+ '</td>'
//								+ '<td>'
//								+ (rc.get('groupName') == null ? "" : rc
//										.get('groupName'))
//								+ '</td>'
//								+ '<td>'
//								+ (rc.get('fillByName') == null ? "" : rc
//										.get('fillByName'))
//								+ '</td>'
//								+ '<td>'
//								+ (rc.get('coefficientNum') == null ? "" : rc
//										.get('coefficientNum'))
//								+ '</td>'
//								+ '<td>'
//								+ (rc.get('baseNum') == null ? "" : rc
//										.get('baseNum'))
//								+ '</td>'
//								+ '<td>'
//								+ (rc.get('amountNum') == null ? "" : rc
//										.get('amountNum'))
//								+ '</td>'
//								+ '<td>'
//								+ (rc.get('awardNum') == null ? "" : rc
//										.get('awardNum'))
//								+ '</td>'
//								+ '<td>'
//								+ (rc.get('totalNum') == null ? "" : rc
//										.get('totalNum'))
//								+ '</td>'
//								+ '<td>'
//								+ (rc.get('fillByName') == null ? "" : rc
//										.get('fillByName'))
//								+ '</td>'
//								+ '<td>'
//								+ (rc.get('memo') == null ? "" : rc.get('memo'))
//								+ '</td>' + '</tr>')
//
//			}
			html.push('</table>');
			html = html.join(''); // 最后生成的HTML表格
			tableToExcel(html);
		}

	}
	// --------------------
	
	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				items : [{
							region : 'center',
							layout : 'fit',
							items : [grid]
						}, {
							region : 'south',
							border : false,
							height : 25,
							items : [{
										xtype : 'panel',
										border : false,
										items : [{
													border : false,
													height : 25,
													items : [Form]
												}]
									}]
						}]
			});
			
	monthInit();
	

});
