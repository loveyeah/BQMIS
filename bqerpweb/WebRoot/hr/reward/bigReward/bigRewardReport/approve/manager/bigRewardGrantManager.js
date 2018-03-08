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
		Ext.lib.Ajax.request('POST', 'hr/initBigRewardGrantDept.action?deptId='
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
					url : 'hr/getMaxBigGarntMonth.action',
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
								});
						storeAwareName.load({
									params : {
										monthDate : grantMonth.getValue(),
										deptId : deptName.getValue(),
										workFlowState : workFlowState
									}
								});

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
							url : 'hr/getBigApproveGroup.action'
						}),
				reader : new Ext.data.JsonReader({}, groupRec)
			});

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
				anchor : "85%",
				listeners : {
					select : function() {
						storeAwareName.load({
									params : {
										monthDate : grantMonth.getValue(),
										deptId : deptName.getValue(),
										groupId : groupName.getValue(),
										workFlowState : workFlowState
									}
								});
					}
				}

			})
	// 大奖名称
	var awareRec = new Ext.data.Record.create([{
				name : 'bigAwardId',
				mapping : '0'
			}, {
				name : 'bigAwardName',
				mapping : '1'
			}])
	var storeAwareName = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'hr/getBigRewardApproveAwareList.action'
						}),
				reader : new Ext.data.JsonReader({}, awareRec)
			});

	storeAwareName.load();
	storeAwareName.on('load', function() {
				var temp = new groupRec({
							bigAwardId : null,
							bigAwardName : '全部'
						})
				storeAwareName.insert(0, temp)
			});

	var awareName = new Ext.form.ComboBox({
				id : 'awareName',
				fieldLabel : "大奖名称",
				store : storeAwareName,
				displayField : "bigAwardName",
				valueField : "bigAwardId",
				mode : 'local',
				triggerAction : 'all',
				readOnly : true,
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
				name : 'bigGrantId',
				mapping : 0
			}, {
				name : 'bigDetailId',
				mapping : 1
			}, {
				name : 'awardName',
				mapping : 2
			}, {
				name : 'deptName',
				mapping : 3
			}, {
				name : 'groupName',
				mapping : 4
			}, {
				name : 'chsName',
				mapping : 5
			}, {
				name : 'coefficientNum',
				mapping : 6
			}, {
				name : 'baseNum',
				mapping : 7
			}, {
				name : 'amountNum',
				mapping : 8
			}, {
				name : 'signBy',
				mapping : 9
			}, {
				name : 'memo',
				mapping : 10
			}, {
				name : 'fillBy',
				mapping : 11
			}, {
				name : 'fillDate',
				mapping : 12
			}, {
				name : 'empId',
				mapping : 13
			}]);

	var DataProxy = new Ext.data.HttpProxy({
				url : 'hr/getApproveBigRewardGrandList.action'
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
							dataIndex : 'bigGrantId',
							hidden : true
						}, {
							header : "明细表ID",
							sortable : true,
							dataIndex : 'bigDetailId',
							hidden : true
						}, {
							header : "大奖名称",
							width : 100,
							align : 'center',
							sortable : true,
							dataIndex : 'awardName'
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
							dataIndex : 'chsName'
						}, {
							header : "系数",
							width : 100,
							align : 'center',
							sortable : true,
							dataIndex : 'coefficientNum'
						}, {
							header : "基数",
							width : 100,
							align : 'center',
							sortable : true,
							dataIndex : 'baseNum'
						}, {
							header : "金额",
							width : 100,
							align : 'center',
							sortable : true,
							dataIndex : 'amountNum',
							editor : new Ext.form.NumberField({
										style : 'cursor:pointer',
										allowNegative : false,
										allowDecimal : false,
										allowBlank : true
									}),
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

							header : "签名",
							width : 100,
							align : 'center',
							sortable : true,
							dataIndex : 'signBy'

						}, {

							header : "备注",
							width : 180,
							align : 'center',
							sortable : true,
							dataIndex : 'memo'

						}],

				autoSizeColumns : true,
				viewConfig : {
					forceFit : true
				},
				tbar : ['时间:', grantMonth, '-', '部门:', deptName, '-', '班组:',
						groupName, '-', '大奖名称：', awareName, '-', {
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
						//deptId : deptName.getValue(),
						groupId : groupName.getValue(),
						workFlowState : workFlowState,
						bigAwardId : awareName.getValue()
					},
					callback : addLine
				})
		grid.stopEditing();
		grid.getView().refresh();

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
									myurl = "hr/approveBigReward.action";
									Ext.Ajax.request({
										method : 'POST',
										url : myurl,
										params : {
											monthDate : grantMonth.getValue(),
											deptId : deptName.getValue(),
											workFlowState : workFlowState,
											bigAwardId : awareName.getValue()
										},
										success : function(action) {
											var o = eval("("
													+ action.responseText + ")");
											Ext.Msg.alert("提示", o.msg)
											window.close();
											validateWin.hide();
											query();
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
					bigGrantId : "",
					bugDetailId : "",
					awardName : "",
					deptName : "",
					groupName : "",
					chsName : "",
					coefficientNum : "",
					baseNum : "",
					amountNum : "",
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
			var html = ['<table border=1><tr><th>大奖名称</th><th>部门</th><th>班组</th><th>姓名</th><th>系数</th><th>基数</th><th>金额</th>'
					+ '<th>签名</th><th>备注</th></tr>'];
			for (var i = 0; i < listStore.getTotalCount(); i++) {
				var rc = listStore.getAt(i);
				html
						.push('<tr><td>'
								+ (rc.get('awardName') == null ? "" : rc
										.get('awardName'))
								+ '</td>'
								+ '<td>'
								+ (rc.get('deptName') == null ? "" : rc
										.get('deptName'))
								+ '</td>'
								+ '<td>'
								+ (rc.get('groupName') == null ? "" : rc
										.get('groupName'))
								+ '</td>'
								+ '<td>'
								+ (rc.get('chsName') == null ? "" : rc
										.get('chsName'))
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
								+ (rc.get('amountNum') == null ? "" : rc
										.get('amountNum'))
								+ '</td>'
								+ '<td>'
								+ (rc.get('signBy') == null ? "" : rc
										.get('signBy'))
								+ '</td>'
								+ '<td>'
								+ (rc.get('memo') == null ? "" : rc.get('memo'))
								+ '</td>' + '</tr>')

			}
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
