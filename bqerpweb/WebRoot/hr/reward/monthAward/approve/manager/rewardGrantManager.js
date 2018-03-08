Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	// 登陆人部门ID
	var workerDeptId = null;
	// 登陆人一级部门ID
	var deptId = null;
	// 工作流状态
	var workFlowState = 4;

	// 取登陆人一级部门
	function getDeptCode(id) {
		Ext.lib.Ajax.request('POST', 'hr/initRewardGrantDept.action?deptId='
						+ workerDeptId, {
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						if (result != "") {
							deptId = result[0]
						} else {
							deptId = workerDeptId
						}
					}
				});
	}
	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						if (result.workerCode) {
							workerDeptId = result.deptId;
							getDeptCode(workerDeptId);
						}
					}
				});
	}

	// 取最大时间
	function monthInit() {
		Ext.Ajax.request({
					url : 'hr/getMaxGarntMonth.action',
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						if (result) {
							grantMonth.setValue(result);
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

	// 按钮
	var btnApprove = new Ext.Button({
				id : 'btnApprove',
				text : '审核',
				iconCls : 'approve',
				handler : approve
			})
	var btnExport = new Ext.Button({
				id : 'btnExport',
				text : '导出',
				iconCls : 'export',
				handler : exportDataFun
			})
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
			}, {//add by sychen 20100903
				name : 'addValue',
				mapping : 20
			}]);

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
							hidden : true,
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
							hidden : true,
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

						}, {//modify by ywliu 20100813
							header : "月度奖金",
							width : 100,
							sortable : true,
							dataIndex : 'monthRewardNum',
							renderer : function(value, cellmeta, record, rowIndex,
		                            columnIndex, store) {
		                        if (rowIndex < store.getCount() - 1) {
		                            // 强行触发renderer事件
		                            var totalSum = 0;
		                            return value;
		                        } else {
		                            totalSum = 0;
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                                totalSum += store.getAt(i).get('monthRewardNum');
		                            }
		                            return totalSum.toFixed(2);
		                        }
		                    }
		                 
						}, {//modify by ywliu 20100813

							header : "月度考核",
							width : 100,
							sortable : true,
							dataIndex : 'monthAssessNum',
							renderer : function(value, cellmeta, record, rowIndex,
		                            columnIndex, store) {
		                        if (rowIndex < store.getCount() - 1) {
		                            // 强行触发renderer事件
		                            var totalSum = 0;
		                            return value;
		                        } else {
		                            totalSum = 0;
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                                totalSum += store.getAt(i).get('monthAssessNum');
		                            }
		                            return totalSum.toFixed(2);
		                        }
		                    }

						}, {//modify by ywliu 20100813
							header : "量化奖金",
							width : 100,
							sortable : true,
							dataIndex : 'quantifyCash',
							renderer : function(value, cellmeta, record, rowIndex,
		                            columnIndex, store) {
		                        if (rowIndex < store.getCount() - 1) {
		                            // 强行触发renderer事件
		                            var totalSum = 0;
		                            return value;
		                        } else {
		                            totalSum = 0;
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                                totalSum += store.getAt(i).get('quantifyCash');
		                            }
		                            return totalSum.toFixed(2);
		                        }
		                    }
						}, {//modify by ywliu 20100813

							header : "量化考核",
							width : 100,
							sortable : true,
							dataIndex : 'quantifyAssessNum',
							renderer : function(value, cellmeta, record, rowIndex,
		                            columnIndex, store) {
		                        if (rowIndex < store.getCount() - 1) {
		                            // 强行触发renderer事件
		                            var totalSum = 0;
		                            return value;
		                        } else {
		                            totalSum = 0;
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                                totalSum += store.getAt(i).get('quantifyAssessNum');
		                            }
		                            return totalSum.toFixed(2);
		                        }
		                    }

						},  {//add by sychen 20100903
			              header : "技师公会增加值",
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
							dataIndex : 'signBy'

						}, {

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
						groupName, '-', {
							text : "查询",
							iconCls : 'query',
							minWidth : 70,
							handler : query
						}],
				listeners : {
					'render' : function() {
						tbar2.render(grid.tbar);
					}
				}

			});

	var tbar2 = new Ext.Toolbar({
				items : [btnApprove, '-', btnExport]
			})
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
		listStore.removeAll()
		listStore.load({
					params : {
						monthDate : grantMonth.getValue(),
//						deptId : deptId,   //modify by wpzhu 20100728
						groupId : groupName.getValue(),
						workFlowState : workFlowState
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

	// 审批
	function approve() {
		Ext.Msg.confirm('提示', '本部门全部班组是否都已上报？', function(button) {
					if (button == 'yes') {
						validateWin.show();
					}
				})
	}
	// ↓↓****************员工验证窗口****************

	// 工号
	var workCode = new Ext.form.TextField({
				id : "workCode",
				fieldLabel : '工号<font color ="red">*</font>',
				allowBlank : false,
				width : 120
			});

	// 密码
	var workPwd = new Ext.form.TextField({
				id : "workPwd",
				fieldLabel : '密码<font color ="red">*</font>',
				allowBlank : false,
				inputType : "password",
				width : 120
			});
	// 弹出窗口panel
	var workerPanel = new Ext.FormPanel({
				frame : true,
				labelAlign : 'right',
				height : 120,
				items : [workCode, workPwd]
			});

	// 弹出窗口
	var validateWin = new Ext.Window({
		width : 300,
		height : 140,
		title : "请输入工号和密码",
		buttonAlign : "center",
		resizable : false,
		modal : true,// add
		items : [workerPanel],
		buttons : [{
			text : '确定',
			id : 'btnSign',
			handler : function() {
				// 工号确认
				Ext.lib.Ajax.request('POST',
						'comm/workticketApproveCheckUser.action', {
							success : function(action) {
								var result = eval(action.responseText);
								// 如果验证成功，进行上报操作
								if (result) {
									var myurl = "";
									myurl = "hr/approveReward.action";
									Ext.Ajax.request({
										method : 'POST',
										url : myurl,
										params : {
											monthDate : grantMonth.getValue(),
											deptId : deptId,
											workFlowState : workFlowState
										},
										success : function(action) {
											var o = eval("("
													+ action.responseText + ")");
											Ext.Msg.alert("提示", o.msg)
											window.close();
											validateWin.hide();
											location.reload();
										},
										faliue : function() {
											Ext.Msg.alert('提示', '请联系管理员!.');
										}
									});
								} else {
									Ext.Msg.alert("提示", "密码错误");
								}
							}
						}, "workerCode=" + workCode.getValue() + "&loginPwd="
								+ workPwd.getValue());
			}
		}, {
			// 取消按钮
			text : '取消',
			handler : function() {
				validateWin.hide();
			}
		}],
		listeners : {
			show : function(com) {
				// 取得默认工号
				Ext.lib.Ajax.request('POST',
						'comm/getCurrentSessionEmployee.action', {
							success : function(action) {
								var result = eval("(" + action.responseText
										+ ")")
								if (result.workerCode) {
									// 设定默认工号
									workCode.setValue(result.workerCode);
								}
							}
						});
			}
		},
		closeAction : 'hide'
	});
	// ↑↑****************员工验证窗口****************
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
			//update by sychen 20100903
			var html = ['<table border=1><tr><th>部门</th><th>班组</th><th>姓名</th><th>系数</th><th>基数</th><th>月度奖金</th>' +
					'<th>月度考核</th><th>量化奖金</th><th>量化考核</th><th>技师公会增加值</th><th>合计</th><th>签名</th><th>备注</th></tr>'];
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
								+ (rc.get('signBy') == null ? "" : rc
										.get('signBy'))
								+ '</td>'
								+ '<td>'
								+ (rc.get('memo') == null ? "" : rc.get('memo'))
								+ '</td>' + '</tr>')

			}
			
			//update by sychen 20100903 end
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
//								+ (rc.get('signBy') == null ? "" : rc
//										.get('signBy'))
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
	getWorkCode();
	query();
});
