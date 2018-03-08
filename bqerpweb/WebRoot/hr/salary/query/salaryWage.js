Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	// add by sychen 20100804
	function ForDight(Dight, How) {
		var Dight = Math.round(Dight * Math.pow(10, How)) / Math.pow(10, How);
		return Dight;
	}

	// 系统当前时间
	function getMonth() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString() + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t;
		return s;
	}
	var month = new Ext.form.TextField({
				style : 'cursor:pointer',
				id : "month",
				name : 'month',
				readOnly : true,
				anchor : "80%",
				value : getMonth(),
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M',
									alwaysUseStartDate : false,
									dateFmt : 'yyyy-MM',
									onpicked : function() {
									},
									onclearing : function() {
										month.markInvalid();
									}
								});
					}
				}
			});
	// 部门
	var deptNs = new Power.dept({
				width : 80
			});
	// 定义grid
	var MyRecord = Ext.data.Record.create([{
				name : 'wageId'
			}, {
				name : 'empId'
			}, {
				name : 'chsName'
			}, {
				name : 'salaryMonth'
			}, {
				name : 'basisWage'
			}, {
				name : 'ageWage'
			}, {
				name : 'operationWage'
			}, {
				name : 'remainWage'
			}, {
				name : 'pointWage'
			}, {
				name : 'overtimeWage'
			}, {
				name : 'deductionWage'
			}, {
				name : 'totalWage'
			}, {
				name : 'wageMemo'
			},
			// {
			// name : 'monthBasic'
			// }, {
			// name : 'coefficient'
			// }, {
			// name : 'monthAwardCut'
			// }, {
			// name : 'monthAward'
			// }, {
			// name : 'monthAwardMemo'
			// }, {
			// name : 'award1Basic'
			// }, {
			// name : 'award1coeff'
			// }, {
			// name : 'bigAwardOneCut'
			// }, {
			// name : 'bigAwardOne'
			// }, {
			// name : 'award2Basic'
			// }, {
			// name : 'award2coeff'
			// }, {
			// name : 'bigAwardTwoCut'
			// }, {
			// name : 'bigAwardTwo'
			// }, {
			// name : 'bigAwardMemo'
			// },
			
			{
				name : 'deptName'// add by sychen 20100803
			}, {
				name : 'banzu'// add by sychen 20100803
			},{
				name : 'others'// add by sychen 20100804
			}, {
				name : 'individualAwardsOne'// add by sychen 20100804
			}, {
				name : 'individualAwardsTwo'// add by sychen 20100804
			}, {
				name : 'monthAwards'// add by sychen 20100804
			}, {
				name : 'bigAwards'// add by sychen 20100804
			}, {
				name : 'totalIncome'// add by sychen 20100804
			}, {
				name : 'deptId'// add by sychen 20100804
			}, {
				name : 'newEmpCode'// add by sychen 20100806
			}, {
				name : 'banZhu'// add by mgxia 20100909
			}]);

	var dataProxy = new Ext.data.HttpProxy({
				url : 'com/getBasicPrimiumAndAward.action'
			});

	var theReader = new Ext.data.JsonReader({
				root : "list",
				totalProperty : "totalCount"
			}, MyRecord);

	var store = new Ext.data.Store({
				proxy : dataProxy,
				reader : theReader
			});
	var sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : false
			});
	// 工资统计grid
	var grid = new Ext.grid.EditorGridPanel({
		autoWidth : true,
		region : "center",
		layout : 'fit',
		store : store,
		clicksToEdit : 1,
		columns : [sm, new Ext.grid.RowNumberer({
							header : '序号',
							width : 35
						}), {
					header : "ID",
					width : 75,
					sortable : true,
					dataIndex : 'wageId',
					hidden : true
				}, {
					header : "empId",
					width : 40,
					sortable : true,
					dataIndex : 'empId',
					hidden : true
				}, {// add by sychen 20100806
					header : "员工工号",
					width : 80,
					sortable : false,
					dataIndex : 'newEmpCode'
				}, {// add by sychen 20100803
					header : "部门",
					width : 80,
					sortable : false,
					dataIndex : 'deptName'
				}, {// add by mgxia 20100909
					header : "班组",
					width : 80,
					sortable : false,
					dataIndex : 'banZhu'
				},{
					header : "班组",
					width : 150,
					sortable : false,
					dataIndex : 'banzu'
				},{
					header : "员工名称",
					width : 65,
					sortable : false,
					dataIndex : 'chsName'
				}, {
					header : "月份",
					width : 65,
					sortable : true,
					dataIndex : 'salaryMonth'
				}, {
					header : "基础工资",
					width : 65,
					sortable : false,
					dataIndex : 'basisWage',
					editor : new Ext.form.NumberField({
								allowNegative : false
							})
				}, {
					header : "保留工资",
					width : 65,
					sortable : true,
					dataIndex : 'remainWage',
					editor : new Ext.form.NumberField({
								allowNegative : false
							})
				}, {
					header : "薪点工资",
					width : 65,
					sortable : false,
					dataIndex : 'pointWage',
					editor : new Ext.form.NumberField({
								allowNegative : false
							})
				}, {
					header : "工龄工资",
					width : 65,
					sortable : true,
					dataIndex : 'ageWage',
					editor : new Ext.form.NumberField({
								allowNegative : false
							})
				}, {
					header : "运行津贴",
					width : 65,
					sortable : false,
					dataIndex : 'operationWage',
					editor : new Ext.form.NumberField({
								allowNegative : false
							})
				}, {
					header : "加班工资",
					width : 65,
					sortable : true,
					dataIndex : 'overtimeWage',
					editor : new Ext.form.NumberField({
								allowNegative : false
							})
				}, {
					header : "扣除工资",
					width : 65,
					sortable : false,
					dataIndex : 'deductionWage',
					editor : new Ext.form.NumberField({
								allowNegative : false
							})
				}, {// add by sychen 20100804 modified by ghzhou 20100815 允许负数
					header : "其他",
					width : 65,
					sortable : false,
					dataIndex : 'others',
					editor : new Ext.form.NumberField({
								allowNegative : true
							})
				}, {
					header : "总工资",
					// width : 65,
					sortable : true,
					dataIndex : 'totalWage',
					renderer : function(value) {// add by sychen 20100804
						return ForDight(value, 0);
					}
				}, {// add by sychen 20100804 modified by ghzhou 20100815
					// 文本框太小问题
					header : "单项奖1",
					width : 65,
					sortable : false,
					dataIndex : 'individualAwardsOne',
					editor : new Ext.form.NumberField({
								allowNegative : false
							})
				}, {// add by sychen 20100804
					header : "单项奖2",
					width : 65,
					sortable : false,
					dataIndex : 'individualAwardsTwo',
					editor : new Ext.form.NumberField({
								allowNegative : false
							})
				}, {// add by sychen 20100804
					header : "月奖",
					width : 65,
					sortable : false,
					dataIndex : 'monthAwards',
					editor : new Ext.form.NumberField({
								allowNegative : false
							})
				}, {// add by sychen 20100804
					header : "大奖",
					width : 65,
					sortable : false,
					dataIndex : 'bigAwards',
					editor : new Ext.form.NumberField({
								allowNegative : false
							})
				}, {// add by sychen 20100804
					header : "总收入",
					width : 65,
					sortable : false,
					dataIndex : 'totalIncome',
					editor : new Ext.form.NumberField({
								allowNegative : false
							}),
					renderer : function(value) {
						return ForDight(value, 0);
					}
				}, {
					header : "工资备注",
					width : 80,
					sortable : false,
					dataIndex : 'wageMemo',
					editor : new Ext.form.TextArea({})
				}
		// , {
		// header : "月奖基数",
		// width : 65,
		// sortable : true,
		// dataIndex : 'monthBasic'
		// }, {
		// header : "月奖系数",
		// width : 65,
		// sortable : false,
		// dataIndex : 'coefficient'
		// }, {
		// header : "月奖扣除",
		// width : 65,
		// sortable : true,
		// dataIndex : 'monthAwardCut',
		// editor : new Ext.form.NumberField({
		// allowNegative: false
		// })
		// }, {
		// header : "月奖",
		// width : 65,
		// sortable : false,
		// dataIndex : 'monthAward',
		// editor : new Ext.form.NumberField({
		// allowNegative: false
		// })
		// }, {
		// header : "月奖备注",
		// width : 65,
		// sortable : true,
		// dataIndex : 'monthAwardMemo',
		// editor : new Ext.form.TextArea({
		// })
		// }, {
		// header : "大奖1基数",
		// width : 65,
		// sortable : false,
		// dataIndex : 'award1Basic'
		// }, {
		// header : "大奖1系数",
		// width : 65,
		// sortable : false,
		// dataIndex : 'award1coeff'
		// }, {
		// header : "大奖1扣除",
		// width : 65,
		// sortable : true,
		// dataIndex : 'bigAwardOneCut',
		// editor : new Ext.form.NumberField({
		// allowNegative: false
		// })
		// }, {
		// header : "大奖1",
		// width : 65,
		// sortable : false,
		// dataIndex : 'bigAwardOne',
		// editor : new Ext.form.NumberField({
		// allowNegative: false
		// })
		// }, {
		// header : "大奖2基数",
		// width : 65,
		// sortable : true,
		// dataIndex : 'award2Basic'
		// }, {
		// header : "大奖2系数",
		// width : 65,
		// sortable : false,
		// dataIndex : 'award2coeff'
		// }, {
		// header : "大奖2扣除",
		// width : 65,
		// sortable : true,
		// dataIndex : 'bigAwardTwoCut',
		// editor : new Ext.form.NumberField({
		// allowNegative: false
		// })
		// }, {
		// header : "大奖2",
		// width : 65,
		// sortable : false,
		// dataIndex : 'bigAwardTwo',
		// editor : new Ext.form.NumberField({
		// allowNegative: false
		// })
		// }, {
		// header : "大奖备注",
		// width : 80,
		// sortable : false,
		// dataIndex : 'bigAwardMemo',
		// editor : new Ext.form.TextArea({
		// })
		// }
		],
		// viewConfig : {
		// forceFit : true
		// },
		tbar : ['部门：', deptNs.combo, "月份：", month, {
					text : "查询",
					iconCls : 'query',
					handler : queryRecord
				}, {
					text : "计算",
					// iconCls : 'computer',
					handler : computer
				}, {
					text : "保存",
					iconCls : 'save',
					handler : save
				}, {
					text : "导出",
					iconCls : 'export',
					handler : exportFun
				}, {
					text : "查看员工明细",
					iconCls : 'query',
					handler : function() {
						var records = grid.selModel.getSelections();
						var ids = [];
						if (records.length == 0) {
							Ext.Msg.alert("提示", "请选择要查看的记录！");
						} else {
							for (var i = 0; i < records.length; i += 1) {
								var member = records[i];
								if (member.get("empId")) {
									ids.push(member.get("empId"));
								}
							}

							var arg = new Object();
							arg.ids = ids;
							arg.month = month.getValue();
							window
									.showModalDialog('salaryDetail.jsp', arg,
											'status:no;dialogWidth=1000px;dialogHeight=500px');
						}
					}
				}],
		sm : sm,
		frame : false,
		bbar : new Ext.PagingToolbar({
					pageSize : 18,
					store : store,
					displayInfo : true,
					displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
					emptyMsg : "没有记录"
				})
	});

	// grid.on("rowdblclick", updateRecord);
	grid.on('afteredit', function(e) {
				e.record.set('totalWage', e.record.get('basisWage')
								+ e.record.get('remainWage')
								+ e.record.get('pointWage')
								+ e.record.get('ageWage')
								+ e.record.get('operationWage')
								+ e.record.get('overtimeWage')
								- e.record.get('deductionWage')
								+ e.record.get('others'));

				e.record.set('totalIncome', e.record.get('totalWage')
								+ e.record.get('individualAwardsOne')
								+ e.record.get('individualAwardsTwo')
								+ e.record.get('monthAwards')
								+ e.record.get('bigAwards'))

			})
	// ---------------------------------------

	// if (flag == 1) {
	// grid.getColumnModel().setHidden(6,false);
	// grid.getColumnModel().setHidden(7,false);
	// grid.getColumnModel().setHidden(8,false);
	// grid.getColumnModel().setHidden(9,false);
	// grid.getColumnModel().setHidden(10,false);
	// grid.getColumnModel().setHidden(11,false);
	// grid.getColumnModel().setHidden(12,false);
	// grid.getColumnModel().setHidden(13,false);
	// grid.getColumnModel().setHidden(14,false);
	//		
	// grid.getColumnModel().setHidden(15,true);
	// grid.getColumnModel().setHidden(16,true);
	// grid.getColumnModel().setHidden(17,true);
	// grid.getColumnModel().setHidden(18,true);
	// grid.getColumnModel().setHidden(19,true);
	//		
	// grid.getColumnModel().setHidden(20,true);
	// grid.getColumnModel().setHidden(21,true);
	// grid.getColumnModel().setHidden(22,true);
	// grid.getColumnModel().setHidden(23,true);
	// grid.getColumnModel().setHidden(24,true);
	// grid.getColumnModel().setHidden(25,true);
	// grid.getColumnModel().setHidden(26,true);
	// grid.getColumnModel().setHidden(27,true);
	// grid.getColumnModel().setHidden(28,true);
	// } else if (flag == 2) {
	// grid.getColumnModel().setHidden(6,true);
	// grid.getColumnModel().setHidden(7,true);
	// grid.getColumnModel().setHidden(8,true);
	// grid.getColumnModel().setHidden(9,true);
	// grid.getColumnModel().setHidden(10,true);
	// grid.getColumnModel().setHidden(11,true);
	// grid.getColumnModel().setHidden(12,true);
	// grid.getColumnModel().setHidden(13,true);
	// grid.getColumnModel().setHidden(14,true);
	//		
	// grid.getColumnModel().setHidden(15,false);
	// grid.getColumnModel().setHidden(16,false);
	// grid.getColumnModel().setHidden(17,false);
	// grid.getColumnModel().setHidden(18,false);
	// grid.getColumnModel().setHidden(19,false);
	//		
	// grid.getColumnModel().setHidden(20,true);
	// grid.getColumnModel().setHidden(21,true);
	// grid.getColumnModel().setHidden(22,true);
	// grid.getColumnModel().setHidden(23,true);
	// grid.getColumnModel().setHidden(24,true);
	// grid.getColumnModel().setHidden(25,true);
	// grid.getColumnModel().setHidden(26,true);
	// grid.getColumnModel().setHidden(27,true);
	// grid.getColumnModel().setHidden(28,true);
	// } else {
	// grid.getColumnModel().setHidden(6,true);
	// grid.getColumnModel().setHidden(7,true);
	// grid.getColumnModel().setHidden(8,true);
	// grid.getColumnModel().setHidden(9,true);
	// grid.getColumnModel().setHidden(10,true);
	// grid.getColumnModel().setHidden(11,true);
	// grid.getColumnModel().setHidden(12,true);
	// grid.getColumnModel().setHidden(13,true);
	// grid.getColumnModel().setHidden(14,true);
	//		
	// grid.getColumnModel().setHidden(15,true);
	// grid.getColumnModel().setHidden(16,true);
	// grid.getColumnModel().setHidden(17,true);
	// grid.getColumnModel().setHidden(18,true);
	// grid.getColumnModel().setHidden(19,true);
	//		
	// grid.getColumnModel().setHidden(20,false);
	// grid.getColumnModel().setHidden(21,false);
	// grid.getColumnModel().setHidden(22,false);
	// grid.getColumnModel().setHidden(23,false);
	// grid.getColumnModel().setHidden(24,false);
	// grid.getColumnModel().setHidden(25,false);
	// grid.getColumnModel().setHidden(26,false);
	// grid.getColumnModel().setHidden(27,false);
	// grid.getColumnModel().setHidden(28,false);
	// }

	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				items : [grid]
			});

	function queryRecord() {
		if (month.getValue() == '') {
			Ext.MessageBox.alert('提示', '请先选择月份！');
			return;
		}
		store.baseParams = {
			deptId : deptNs.combo.getValue(),
			yearMonth : month.getValue()
		}
		store.load({
					params : {
						start : 0,
						limit : 18
					}
				});
	}

	function computer() {
		if (deptNs.combo.getValue() == null || deptNs.combo.getValue() == '') {
			Ext.MessageBox.alert('提示信息', '请先选择一部门后进行计算！');
		} else {
			Ext.Msg.wait('提示信息','正在计算请稍后');
			Ext.Ajax.request({
						url : 'com/calculateSalary.action',
						method : 'post',
						params : {
							deptId : deptNs.combo.getValue(),
							yearMonth : month.getValue(),
							flag : flag
						},
						success : function(result, request) {

							// Ext.MessageBox.alert('提示信息', '保存修改成功！');
							queryRecord();
							Ext.Msg.hide();
						},
						failure : function(result, request) {
							Ext.MessageBox.alert('提示信息', '计算失败！')
						}
					})
		}

	}

	// save 方法 update by sychen 20100806
	var ids = new Array();
	var deptNames = new Array();
	var deptId = null;
	var deptName = null;
	function save() {
		var modifyRec = grid.getStore().getModifiedRecords();
		if (modifyRec.length > 0) {
			for (var i = 0; i < modifyRec.length; i++) {
				ids.push(modifyRec[i].get("deptId"));
				deptNames.push(modifyRec[i].get("deptName"));
			}
			for (var i = ids.length - 1; i >= 1; i--) {
				if (ids[i - 1] == ids[i]) {
					ids.splice(i, 1);// splice（）方法的应用
					deptNames.splice(i, 1);// splice（）方法的应用
				}
			}
			ids = ids.join(",");
			deptNames = deptNames.join(",");
			arr = ids.split(",");
			deptArr = deptNames.split(",");
			for (i = 0; i < arr.length; i++) {
				deptId = arr[i];
				deptName = deptArr[i];
				Ext.Ajax.request({
					url : 'com/checkOldSalaryModify.action',
					method : 'post',
					params : {
						deptId : deptId
					},
					success : function(response, options) {
						var res = response.responseText;
						if ((res.toString() != '')
								&& (res.toString() != month.getValue())) {
							Ext.Msg.alert('注意', '<' + deptName + '>'
											+ '历史工资数据无法修改！');
							return;
						} else {
							grid.stopEditing();
							if (modifyRec.length > 0) {
								Ext.Msg.confirm('提示', '确定要保存修改吗?', function(b) {
									if (b == 'yes') {
										var array = new Array();

										for (var i = 0; i <= modifyRec.length
												- 1; i++) {
											array.push(modifyRec[i].data)
										}
										Ext.Msg.wait('提示信息','正在保存请稍后');
										Ext.Ajax.request({
											url : 'com/saveBasicPrimiumAndAward.action',
											method : 'post',
											params : {
												isUpdate : Ext.util.JSON
														.encode(array)
											},
											success : function(result, request) {

												Ext.Msg.hide();
												Ext.MessageBox.alert('提示信息',
														'保存修改成功！');
												queryRecord();
												
											},
											failure : function(result, request) {
												Ext.MessageBox.alert('提示信息',
														'保存修改失败！')
											}
										})
									}
								})
							} else {
								Ext.MessageBox.alert('提示信息', '没有做任何修改！')
							}
						}

					}
				});
			}
		}
	}

	// function save() {
	// grid.stopEditing();
	// var modifyRec = grid.getStore().getModifiedRecords();
	// if (modifyRec.length > 0) {
	// Ext.Msg.confirm('提示', '确定要保存修改吗?', function(b) {
	// if (b == 'yes') {
	// var array = new Array();
	//					
	// for(var i =0; i <= modifyRec.length - 1; i++){
	// array.push(modifyRec[i].data)
	// }
	// Ext.Ajax.request({
	// url : 'com/saveBasicPrimiumAndAward.action',
	// method : 'post',
	// params : {
	// isUpdate : Ext.util.JSON.encode(array)
	// },
	// success : function(result, request) {
	//							
	// Ext.MessageBox.alert('提示信息', '保存修改成功！');
	// queryRecord();
	// },
	// failure : function(result, request) {
	// Ext.MessageBox.alert('提示信息', '保存修改失败！')
	// }
	// })
	// }
	// })
	// } else {
	// Ext.MessageBox.alert('提示信息', '没有做任何修改！')
	// }
	// }

	// 导出
//	function tableToExcel(tableHTML) {
//		window.clipboardData.setData("Text", tableHTML);
//		try {
//			var ExApp = new ActiveXObject("Excel.Application");
//			var ExWBk = ExApp.workbooks.add();
//			var ExWSh = ExWBk.worksheets(1);
//
//			ExWSh.Columns("A").ColumnWidth = 12;
//			ExWSh.Columns("B").ColumnWidth = 30;
//			ExWSh.Columns("D").ColumnWidth = 12;
//			ExWSh.Columns("S").ColumnWidth = 20;
//			ExApp.DisplayAlerts = false;
//			ExApp.visible = true;
//		} catch (e) {
//			if (e.number != -2146827859)
//				alert("您的电脑没有安装Microsoft Excel软件！");
//			return false;
//		}
//		ExWBk.worksheets(1).Paste;
//	}

//	var expStore = new Ext.data.Store({
//				proxy : dataProxy,
//				reader : theReader
//			});
//			function query() {
//		expStore.baseParams = {
//			deptId : deptNs.combo.getValue(),
//			yearMonth : month.getValue()
//		}
//		expStore.load();
//	}
//	
//	function exportFun() {
//query();
//alert(expStore.getTotalCount())
//		if (expStore.getTotalCount() > 0) {
//			excelRecord();
//		} else {
////			expStore.load({
////						callback : function() {
////							excelRecord()
////						}
////					})
//		}
//
//	}
//	function excelRecord() {
//		var html;
//		// if (flag == 1) {
//		// update by sychen 20100804
//		html = ['<table border=1><tr><th>员工工号</th><th>部门</th><th>员工名称</th><th>月份</th><th>基础工资</th><th>保留工资</th><th>薪点工资</th><th>工龄工资</th><th>运行津贴</th><th>加班工资</th><th>扣除工资</th>'
//				+ '<th>其他</th><th>总工资</th><th>单项奖1</th><th>单项奖2</th><th>月奖</th><th>大奖</th><th>总收入</th><th>工资备注</th></tr>'];
//		for (var i = 0; i < expStore.getTotalCount(); i += 1) {
//			var rc = expStore.getAt(i).data;
//			html.push('<tr><td>' + rc.newEmpCode + '</td>' + '<td>'
//					+ rc.deptName + '</td><td>' + rc.chsName + '</td><td>'
//					+ rc.salaryMonth + '</td><td>' + rc.basisWage + '</td><td>'
//					+ rc.remainWage + '</td><td>' + rc.pointWage + '</td><td>'
//					+ rc.ageWage + '</td><td>' + rc.operationWage + '</td><td>'
//					+ rc.overtimeWage + '</td><td>' + rc.deductionWage
//					+ '</td><td>' + rc.others + '</td><td>' + rc.totalWage
//					+ '</td><td>' + rc.individualAwardsOne + '</td><td>'
//					+ rc.individualAwardsTwo + '</td><td>' + rc.monthAwards
//					+ '</td><td>' + rc.bigAwards + '</td><td>' + rc.totalIncome
//					+ '</td><td>' + (rc.wageMemo == null ? "" : rc.wageMemo)
//					+ '</td></tr>');
//		}
		// html = ['<table
		// border=1><tr><th>员工名称</th><th>月份</th><th>基础工资</th><th>保留工资</th><th>薪点工资</th><th>工龄工资</th><th>运行津贴</th><th>加班工资</th><th>扣除工资</th><th>总工资</th><th>工资备注</th></tr>'];
		// for (var i = 0; i < expStore.getTotalCount(); i += 1) {
		// var rc = expStore.getAt(i).data;
		// html.push('<tr><td>' + rc.chsName + '</td><td>'
		// + rc.salaryMonth + '</td><td>' + rc.basisWage
		// + '</td><td>' + rc.remainWage + '</td><td>'
		// + rc.pointWage + '</td><td>' + rc.ageWage
		// + '</td><td>' + rc.operationWage + '</td><td>'
		// + rc.overtimeWage + '</td><td>' + rc.deductionWage
		// + '</td><td>'
		// + rc.totalWage + '</td><td>' + rc.wageMemo
		// + '</td></tr>');
		// }

		// update by sychen 20100804 end
		// } else if (flag == 2) {
		// html = ['<table
		// border=1><tr><th>员工名称</th><th>月份</th><th>月奖基数</th><th>月奖系数</th><th>月奖扣除</th><th>月奖</th><th>月奖备注</th></tr>'];
		// for (var i = 0; i < expStore.getTotalCount(); i += 1) {
		// var rc = expStore.getAt(i).data;
		// html.push('<tr><td>' + rc.chsName + '</td><td>'
		// + rc.salaryMonth + '</td><td>' + rc.monthBasic
		// + '</td><td>' + rc.coefficient + '</td><td>'
		// + rc.monthAwardCut + '</td><td>' + rc.monthAward
		// + '</td><td>' + rc.monthAwardMemo
		// + '</td></tr>');
		// }
		// } else {
		// html = ['<table
		// border=1><tr><th>员工名称</th><th>月份</th><th>大奖1基数</th><th>大奖1系数</th><th>大奖1扣除</th><th>大奖1</th><th>大奖2基数</th><th>大奖2系数</th><th>大奖2扣除</th><th>大奖2</th><th>大奖备注</th></tr>'];
		//					   for (var i = 0; i < expStore.getTotalCount(); i += 1) {
		//						var rc = expStore.getAt(i).data;
		//						html.push('<tr><td>' + rc.chsName + '</td><td>'
		//								+ rc.salaryMonth + '</td><td>' + rc.award1Basic
		//								+ '</td><td>' + rc.award1coeff + '</td><td>'
		//								+ rc.bigAwardOneCut + '</td><td>' + rc.bigAwardOne
		//								+ '</td><td>' + rc.award2Basic + '</td><td>'
		//								+ rc.award2coeff + '</td><td>' + rc.bigAwardTwoCut
		//								+ '</td><td>'
		//								+ rc.bigAwardTwo + '</td><td>' + rc.bigAwardMemo
		//								+ '</td></tr>');
		//					}
		//				}
//		html.push('</table>');
//		html = html.join(''); // 最后生成的HTML表格
//		// alert(html);
//		tableToExcel(html);
//	}
	
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
		function exportFun() {
			
			Ext.Msg.wait('提示信息','正在导出请稍后');
			Ext.Ajax.request({
				url : 'com/getBasicPrimiumAndAward.action',
				method : 'post',
				params : {
					deptId : deptNs.combo.getValue(),
					yearMonth : month.getValue()

				},

				success : function(response) {
					var json = eval('(' + response.responseText.trim() + ')');
					var records = json.list;

					var html;
					html = ['<table border=1><tr><th>员工工号</th><th>部门</th><th>员工名称</th><th>月份</th><th>基础工资</th><th>保留工资</th><th>薪点工资</th><th>工龄工资</th><th>运行津贴</th><th>加班工资</th><th>扣除工资</th>'
						+ '<th>其他</th><th>总工资</th><th>单项奖1</th><th>单项奖2</th><th>月奖</th><th>大奖</th><th>总收入</th><th>工资备注</th></tr>'];

					for (var i = 0; i < records.length; i += 1) {

						var rc = records[i];
					html.push('<tr><td>' + rc.newEmpCode + '&nbsp&nbsp</td>' + '<td>'
					+ rc.deptName + '</td><td>' + rc.chsName + '</td><td>'
					+ rc.salaryMonth + '&nbsp</td><td>' + rc.basisWage + '</td><td>'
					+ rc.remainWage + '</td><td>' + rc.pointWage + '</td><td>'
					+ rc.ageWage + '</td><td>' + rc.operationWage + '</td><td>'
					+ rc.overtimeWage + '</td><td>' + rc.deductionWage
					+ '</td><td>' + rc.others + '</td><td>' + rc.totalWage
					+ '</td><td>' + rc.individualAwardsOne + '</td><td>'
					+ rc.individualAwardsTwo + '</td><td>' + rc.monthAwards
					+ '</td><td>' + rc.bigAwards + '</td><td>' + rc.totalIncome
					+ '</td><td>' + (rc.wageMemo == null ? "" : rc.wageMemo)
					+ '</td></tr>');
					}
					html.push('</table>');
					html = html.join('');
					tableToExcel(html);
					Ext.Msg.hide();
				},
				failure : function(response) {
					Ext.Msg.alert('提示', '导出失败！');
				}
			})
		}
	
	queryRecord();
	
});