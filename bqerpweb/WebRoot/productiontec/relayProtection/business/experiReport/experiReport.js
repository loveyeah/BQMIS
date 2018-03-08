Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	var bview;
	var findById;
	var title = "";
	// 系统当前时间
	function getDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate();
		s += (t > 9 ? "" : "0") + t + " ";
		return s;
	}
	// 从session取登录人编码姓名部门相关信息
	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						if (result.workerCode) {
							// 设定默认工号，赋给全局变量
							chargeByName.setValue(result.workerName);
							chargeBy.setValue(result.workerCode);
						}
					}
				});
	}

	// 装置名称
	var fuzzyText = new Ext.form.TextField({
				id : 'fuzzyText',
				name : 'fuzzyText',
				width : 100,
				emptyText : '（装置名称）'
			})
	var sDate = new Ext.form.TextField({
				id : 'sate',
				name : 'sDate',
				fieldLabel : "开始",
				style : 'cursor:pointer',
				cls : 'Wdate',
				width : 85,
				// value : sdate,
				listeners : {
					focus : function() {
						WdatePicker({
									// startDate : '%y-%M-01',
									dateFmt : 'yyyy-MM-dd',
									alwaysUseStartDate : true
								});
					}
				}
			});
	var eDate = new Ext.form.TextField({
				name : 'eDate',
				// value : edate,
				id : 'eDate',
				fieldLabel : "结束",
				style : 'cursor:pointer',
				cls : 'Wdate',
				width : 85,
				listeners : {
					focus : function() {
						WdatePicker({
									// startDate : '%y-%M-01',
									dateFmt : 'yyyy-MM-dd',
									alwaysUseStartDate : true
								});
					}
				}
			});

	var queryButton = new Ext.Button({
				text : '查询',
				iconCls : 'query',
				handler : function() {
					if (sDate.getValue() != null && sDate.getValue() != ""
							&& eDate.getValue() != null
							&& eDate.getValue() != "") {
						if (sDate.getValue() > eDate.getValue()) {
							Ext.Msg.alert('提示信息', '查询试验时间中开始时间大于结束时间！');
							return;
						}
					}
					recordQuery();
				}
			});
	// 试验报告编号
	var jdsybgId = new Ext.form.Hidden({
				id : 'jdsybgId',
				name : 'report.jdsybgId'
			})
	// 试验报告名称
	var jdsybgName = new Ext.form.TextField({
				fieldLabel : '试验报告名称',
				id : 'jdsybgName',
				name : 'report.jdsybgName',
				width : 407
			})

	// 试验类别编号
	var sylbId = new Ext.form.Hidden({
				id : 'sylbId',
				name : 'report.sylbId'
			})
	// 试验类别名称
	var sortName = new Ext.form.TriggerField({
				fieldLabel : '试验类别',
				width : 145,
				id : "sylbName",
				hiddenName : 'sylbName',
				blankText : '请选择',
				emptyText : '请选择',
				maxLength : 100,
				readOnly : true
			});
	sortName.onTriggerClick = sortSelect;

	/**
	 * 试验类别选择画面处理
	 */
	function sortSelect() {
		var args = {
			selectModel : 'signal',
			notIn : "",
			rootNode : {
				id : '-1',
				text : '合肥电厂'
			}
		}
		var url = "../sortSelect.jsp";
		var sort = window.showModalDialog(url, window,
				'dialogWidth=600px;dialogHeight=400px;status=no');
		if (typeof(sort) != "undefined") {
			sortName.setValue(sort.sylbName);
			sylbId.setValue(sort.sylbId);
		}
	}
	// 负责人
	var chargeByName = new Ext.form.TriggerField({
				fieldLabel : '试验负责人',
				width : 145,
				id : "chargeByName",
				hiddenName : 'chargeByName',
				blankText : '请选择',
				emptyText : '请选择',
				maxLength : 100,
				readOnly : true
			});
	chargeByName.onTriggerClick = selectPersonWin;
	var chargeBy = new Ext.form.Hidden({
				id : 'chargeBy',
				name : 'report.chargeBy'
			})
	/**
	 * 人员选择画面处理
	 */
	function selectPersonWin() {
		var args = {
			selectModel : 'signal',
			notIn : "",
			rootNode : {
				id : '-1',
				text : '合肥电厂'
			}
		}
		var person = window
				.showModalDialog(
						'../../../../comm/jsp/hr/workerByDept/workerByDept.jsp',
						args,
						'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (typeof(person) != "undefined") {
			chargeByName.setValue(person.workerName);
			chargeBy.setValue(person.workerCode);
		}
	}
	// 装置编号
	var deviceId = new Ext.form.Hidden({
				id : 'deviceId',
				name : 'report.deviceId'
			})
	// 装置名称
	var deviceName = new Ext.form.TriggerField({
				fieldLabel : '装置名称',
				width : 407,
				id : "deviceName",
				hiddenName : 'deviceName',
				blankText : '请选择',
				emptyText : '请选择',
				maxLength : 100,
				readOnly : true
			});
	deviceName.onTriggerClick = deviceSelect;

	/**
	 * 设备选择画面处理
	 */
	function deviceSelect() {
		var args = {
			selectModel : 'signal',
			notIn : "",
			rootNode : {
				id : '-1',
				text : '合肥电厂'
			}
		}
		var url = "../deviceSelect.jsp?";
		var equ = window.showModalDialog(url, window,
				'dialogWidth=600px;dialogHeight=400px;status=no');
		if (typeof(equ) != "undefined") {
			deviceName.setValue(equ.deviceName);
			deviceId.setValue(equ.deviceId);
		}
	}

	// 试验地点
	var testPlace = new Ext.form.TextField({
				fieldLabel : "试验地点",
				name : 'report.testPlace',
				width : 407
			})
	// 试验日期
	var testDate = new Ext.form.DateField({
				fieldLabel : '试验日期',
				id : 'testDate',
				name : 'report.testDate',
				width : 145,
				format : 'Y-m-d',
				value : new Date(),
				allowBlank : false
			})
	// 上次试验时间
	var strLastTestDate = new Ext.form.TextField({
				fieldLabel : "上次试验时间",
				name : 'strLastTestDate',
				readOnly : true,
				width : 145
			})
	// 试验性质stoe testType
	var testTypeStore = new Ext.data.SimpleStore({
				fields : ['id', 'name'],
				data : [['', ''], ['1', '预防性试验'], ['2', '大修前试验'],
						['3', '大修后试验'], ['4', '交接试验'], ['5', '检定试验'],
						['6', '监督性试验']]
			});
	// 试验性质
	var testType = new Ext.form.ComboBox({
				store : testTypeStore,
				id : "testType",
				displayField : "name",
				valueField : "id",
				mode : 'local',
				width : 145,
				fieldLabel : "试验性质",
				triggerAction : 'all',
				readOnly : true,
				hiddenName : 'report.testType'
			})
	// 本次计划实验日期
	var strPlanTestDate = new Ext.form.TextField({
				fieldLabel : "本次计划实验日期",
				name : 'strPlanTestDate',
				readOnly : true,
				width : 145
			})

	// 天气
	var weather = new Ext.form.TextField({
				fieldLabel : "天气",
				name : 'report.weather',
				width : 145
			})
	// 温度
	var temperature = new Ext.form.NumberField({
				id : 'temperature',
				name : 'report.temperature',
				fieldLabel : '温度（摄氏度）',
				width : 145,
				allowDecimals : true,
				allowNegative : false,
				allowBlank : true
			})
	// 湿度
	var humidity = new Ext.form.NumberField({
				id : 'humidity',
				name : 'report.humidity',
				fieldLabel : '湿度',
				width : 145,
				allowDecimals : true,
				allowNegative : false,
				allowBlank : true
			})

	// 试验人员
	var testByName = new Ext.form.TriggerField({
				fieldLabel : '试验人员',
				width : 145,
				id : "testByName",
				hiddenName : 'testByName',
				blankText : '请选择',
				emptyText : '请选择',
				maxLength : 100,
				readOnly : true
			});
	testByName.onTriggerClick = testChoose;
	var testBy = new Ext.form.Hidden({
				id : 'testBy',
				name : 'report.testBy'
			})
	/**
	 * 人员选择画面处理
	 */
	function testChoose() {
		var args = {
			selectModel : 'signal',
			notIn : "",
			rootNode : {
				id : '-1',
				text : '合肥电厂'
			}
		}
		var person = window
				.showModalDialog(
						'../../../../comm/jsp/hr/workerByDept/workerByDept.jsp',
						args,
						'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (typeof(person) != "undefined") {
			testByName.setValue(person.workerName);
			testBy.setValue(person.workerCode);
		}
	}
	// 试验情况
	var testSituation = new Ext.form.TextArea({
				fieldLabel : '试验情况',
				id : 'testSituation',
				name : 'report.testSituation',
				width : 407
			})
	// 附件 内容
	var annex = {
		id : "annex",
		xtype : 'fileuploadfield',
		isFormField : true,
		name : "annex",
		fieldLabel : '内容',
		// fileUpload : true,
		height : 21,
		anchor : "95%",
		buttonCfg : {
			text : '浏览...',
			iconCls : 'upload-icon'
		}
	}

	// 查看
	var btnView = new Ext.Button({
				id : 'btnView',
				text : '查看',
				handler : function() {
					window.open(bview);
				}
			});
	btnView.setVisible(false);
	// 备注
	var memo = new Ext.form.TextArea({
				fieldLabel : '备注',
				id : 'memo',
				name : 'report.memo',
				width : 407
			})
	// 弹窗的表单对象
	var blockForm = new Ext.form.FormPanel({
				labelAlign : 'right',
				border : true,
				frame : true,
				autoHeight : true,
				fileUpload : true,
				labelWidth : 110,
				style : 'padding:10px,5px,0px,5px',
				items : [{
							layout : 'column',
							border : false,
							items : [{
										columnWidth : 1,
										layout : 'form',
										items : [jdsybgId, jdsybgName]
									}, {
										columnWidth : 0.5,
										layout : 'form',
										items : [sylbId, sortName]
									}, {
										columnWidth : 0.5,
										layout : 'form',
										items : [chargeByName, chargeBy]
									}, {
										columnWidth : 1,
										layout : 'form',
										items : [deviceId, deviceName]
									}, {
										columnWidth : 1,
										layout : 'form',
										items : [testPlace]
									}, {
										columnWidth : 0.5,
										layout : 'form',
										items : [testDate]
									}, {
										columnWidth : 0.5,
										layout : 'form',
										items : [strLastTestDate]
									}, {
										columnWidth : 0.5,
										layout : 'form',
										items : [testType]
									}, {
										columnWidth : 0.5,
										layout : 'form',
										items : [strPlanTestDate]
									}, {
										columnWidth : 0.5,
										layout : 'form',
										items : [weather]
									}, {
										columnWidth : 0.5,
										layout : 'form',
										items : [temperature]
									}, {
										columnWidth : 0.5,
										layout : 'form',
										items : [humidity]
									}, {
										columnWidth : 0.5,
										layout : 'form',
										items : [testByName, testBy]
									}, {
										columnWidth : 1,
										layout : 'form',
										items : [testSituation]
									}, {
										columnWidth : 0.8,
										border : false,
										layout : 'form',
										items : [annex]
									}, {
										columnWidth : 0.2,
										border : false,
										layout : 'form',
										items : [btnView]
									}, {
										columnWidth : 1,
										layout : 'form',
										items : [memo]
									}]
						}]
			});

	// 左边的弹出窗体
	var blockAddWindow;
	function showAddWindow() {
		if (!blockAddWindow) {
			blockAddWindow = new Ext.Window({
				title : '',
				layout : 'fit',
				width : 560,
				height : 470,
				modal : true,
				closable : true,
				border : false,
				resizable : false,
				closeAction : 'hide',
				plain : true,
				// 面板中按钮的排列方式
				buttonAlign : 'center',
				items : [blockForm],
				buttons : [{
					text : '保存',
					iconCls : 'save',
					handler : function() {
						if (Ext.get('jdsybgName').dom.value == null
								|| Ext.get('jdsybgName').dom.value == "") {
							Ext.Msg.alert("提示信息", "试验报告名称不能为空，请输入！");
							return;
						}
						if (Ext.get('sylbId').getValue() == null
								|| Ext.get('sylbId').getValue() == "") {
							Ext.Msg.alert("提示信息", "试验类别不能为空，请选择！");
							return;
						}
						if (Ext.get('deviceId').getValue() == null
								|| Ext.get('deviceId').getValue() == "") {
							Ext.Msg.alert("提示信息", "装置名称不能为空，请选择！");
							return;
						}
						if (blockForm.getForm().isValid())
							if (op == "add") {
								blockForm.getForm().submit({
									waitMsg : '保存中,请稍后...',
									params : {
										filePath : Ext.get("annex").dom.value
									},
									url : "productionrec/addExperimentReportInfo.action",
									success : function(form, action) {
										var result = eval('('
												+ action.response.responseText
												+ ')');
										Ext.Msg.alert("提示信息", result.msg);
										blockForm.getForm().reset();
										blockAddWindow.hide();
										fuzzyText.setValue("")
										sDate.setValue("")
										eDate.setValue("")
//										westgrids.reload();
										recordQuery();
										bview = "";
									},
									failure : function(form, action) {
										var result = eval('('
												+ action.response.responseText
												+ ')');
										Ext.Msg.alert("提示信息", result.msg);
									}
								});
							} else if (op == "edit") {
								blockForm.getForm().submit({
									waitMsg : '保存中,请稍后...',
									params : {
										filePath : Ext.get("annex").dom.value
									},
									url : "productionrec/updateExperimentReportInfo.action",
									success : function(form, action) {
										var result = eval('('
												+ action.response.responseText
												+ ')');
										// 显示成功信息
										Ext.Msg.alert("提示信息", result.msg);
										blockForm.getForm().reset();
										blockAddWindow.hide();
										fuzzyText.setValue("")
										sDate.setValue("")
										eDate.setValue("")
//										westgrids.reload();
										recordQuery();
										bview = "";
									},
									failure : function(form, action) {
										var result = eval('('
												+ action.response.responseText
												+ ')');
										Ext.Msg.alert("提示信息", result.msg);

									}
								});
							} else {
								Ext.MessageBox.alert('错误', '未定义的操作');
							}
					}
				}, {
					text : '取消',
					iconCls : 'cancer',
					handler : function() {
						blockForm.getForm().reset();
						blockAddWindow.hide();
					}
				}]
			});
		}
		if (op == "add") {
			blockForm.getForm().reset();
			// 新增时，赋初始值
			blockAddWindow.setTitle("新增继电保护试验报告信息");
		} else if (op == "edit") {
			blockAddWindow.setTitle("修改继电保护试验报告信息");
			blockAddWindow.show();
			var rec = westgrid.getSelectionModel().getSelected();

			Ext.get('jdsybgId').dom.value = rec.get('report.jdsybgId') == null
					? ""
					: rec.get('report.jdsybgId')
			Ext.get('jdsybgName').dom.value = rec.get('report.jdsybgName') == null
					? ""
					: rec.get('report.jdsybgName')
			sylbId.setValue(rec.get('report.sylbId') == null ? "" : rec
					.get('report.sylbId'))
			sortName.setValue(rec.get('sortName') == null ? "" : rec
					.get('sortName'))
			chargeByName.setValue(rec.get('chargeByName') == null ? "" : rec
					.get('chargeByName'))
			chargeBy.setValue(rec.get('report.chargeBy') == null ? "" : rec
					.get('report.chargeBy'))
			deviceId.setValue(rec.get('report.deviceId') == null ? "" : rec
					.get('report.deviceId'))
			deviceName.setValue(rec.get('deviceName') == null ? "" : rec
					.get('deviceName'))
			testPlace.setValue(rec.get('report.testPlace') == null ? "" : rec
					.get('report.testPlace'))
			testDate.setValue(rec.get('strTestDate') == null ? "" : rec
					.get('strTestDate'))
			strLastTestDate.setValue(rec.get('strLastTestDate') == null
					? ""
					: rec.get('strLastTestDate'))
			testType.setValue(rec.get('report.testType') == null ? "" : rec
					.get('report.testType'))
			strPlanTestDate.setValue(rec.get('strPlanTestDate') == null
					? ""
					: rec.get('strPlanTestDate'))
			weather.setValue(rec.get('report.weather') == null ? "" : rec
					.get('report.weather'))
			temperature.setValue(rec.get('report.temperature') == null
					? ""
					: rec.get('report.temperature'))
			humidity.setValue(rec.get('report.humidity') == null ? "" : rec
					.get('report.humidity'))
			testByName.setValue(rec.get('testByName') == null ? "" : rec
					.get('testByName'))
			testBy.setValue(rec.get('report.testBy') == null ? "" : rec
					.get('report.testBy'))
			testSituation.setValue(rec.get('report.testSituation') == null
					? ""
					: rec.get('report.testSituation'))
			if (rec.get("report.content") != null
					&& rec.get("report.content") != "") {
				bview = rec.get("report.content");
				btnView.setVisible(true);
				Ext.get("annex").dom.value = bview.replace(
						'/power/upload_dir/productionrec/', '');
			} else {
				btnView.setVisible(false);
			}
			memo.setValue(rec.get('report.memo') == null ? "" : rec
					.get('report.memo'))
		} else {
		}
		blockAddWindow.show()
	};

	// 左边按钮
	// 新建按钮
	var westbtnAdd = new Ext.Button({
				text : '新增',
				iconCls : 'add',
				handler : function() {
					op = "add";
					showAddWindow();
				}
			});

	// 选择判断
	function CKSelectdone() {
		var rec = westgrid.getSelectionModel().getSelections();
		if (rec.length != 1) {
			Ext.Msg.alert('提示信息', "请选择一行！");
			return false;
		} else {
			op = "edit";
			showAddWindow();
		}
	}

	// 修改按钮
	var westbtnedit = new Ext.Button({
				text : '修改',
				iconCls : 'update',
				handler : CKSelectdone
			});

	// 删除按钮
	var westbtndel = new Ext.Button({
		text : '删除',
		iconCls : 'delete',
		handler : function() {
			if (westgrid.getSelectionModel().getSelections().length > 0) {
				Ext.Msg.confirm('提示信息', '确认删除所选中记录?', function(button, text) {
					if (button == 'yes') {
						var rec = westgrid.getSelectionModel().getSelections();
						var str = "";
						for (var i = 0; i < rec.length; i++) {
							if (i < rec.length - 1)
								str += rec[i].get('report.jdsybgId') + ",";
							else
								str += rec[i].get('report.jdsybgId');
						}
						Ext.Ajax.request({
							waitMsg : '删除中,请稍后...',
							url : 'productionrec/deleteExperimentReport.action',
							params : {
								ids : str
							},
							success : function(response, options) {
								Ext.Msg.alert('提示信息', '数据删除成功！')
								westgrids.reload();
							},
							failure : function() {
								Ext.Msg.alert('提示信息', '服务器错误,请稍候重试!')
							}
						});
					}
				})
			} else {
				Ext.Msg.alert('提示信息', "请至少选择一行！");
				return false;
			}
		}
	});

	var westsm = new Ext.grid.CheckboxSelectionModel();
	// 左边列表中的数据
	var datalist = new Ext.data.Record.create([

	{
				name : 'report.jdsybgId'
			}, {
				name : 'report.deviceId'
			}, {
				name : 'report.sylbId'
			}, {
				name : 'report.jdsybgName'
			}, {
				name : 'report.testPlace'
			}, {
				name : 'report.testDate'
			}, {
				name : 'report.lastTestDate'
			}, {
				name : 'report.planTestDate'
			}, {
				name : 'report.testType'
			}, {
				name : 'report.weather'
			}, {
				name : 'report.temperature'
			}, {
				name : 'report.humidity'
			}, {
				name : 'report.testBy'
			}, {
				name : 'report.chargeBy'
			}, {
				name : 'report.testSituation'
			}, {
				name : 'report.content'
			}, {
				name : 'report.memo'
			}, {
				name : 'report.enterpriseCode'
			}, {
				name : 'deviceName'
			}, {
				name : 'sortName'
			}, {
				name : 'strTestDate'
			}, {
				name : 'strLastTestDate'
			}, {
				name : 'strPlanTestDate'
			}, {
				name : 'chargeByName'
			}, {
				name : 'testByName'
			}]);

	var westgrids = new Ext.data.JsonStore({
				url : 'productionrec/findExperimentReportList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : datalist
			});
	function recordQuery() {
		westgrids.baseParams = {
			deviceName : fuzzyText.getValue(),
			sDate : sDate.getValue(),
			eDate : eDate.getValue()
		}
		westgrids.load({
					params : {
						start : 0,
						limit : 18
					}
				});
	}

	// 试验报告记录
	var btndetails = new Ext.Button({
				text : '试验报告记录',
				iconCls : 'update',
				handler : function() {
					var rec = westgrid.getSelectionModel().getSelections();
					if (rec.length != 1) {
						Ext.Msg.alert('提示信息', "请选择一行！");
						return false;
					} else {
						var record = westgrid.getSelectionModel().getSelected();
						findById = record.get('report.jdsybgId');
						title = record.get('report.jdsybgName');
						detailsWin.setTitle("试验名称：'" + title + "'的试验报告记录");
						queryRecord();
						detailsWin.show();
					}
				}
			});

	// 左边列表
	var westgrid = new Ext.grid.GridPanel({
				ds : westgrids,
				columns : [westsm, new Ext.grid.RowNumberer({
									header : "行号",
									width : 31
								}), {
							header : "试验报告编号",
							width : 120,
							hidden : true,
							align : "center",
							sortable : true,
							dataIndex : 'report.jdsybgId'
						}, {
							header : "试验报告名称",
							width : 120,
							align : "center",
							sortable : true,
							dataIndex : 'report.jdsybgName'
						}, {
							header : "装置名称",
							width : 120,
							align : "center",
							sortable : true,
							dataIndex : 'deviceName'
						}, {
							header : "类别名称",
							width : 120,
							align : "center",
							sortable : true,
							dataIndex : 'sortName'
						}, {
							header : "试验日期",
							width : 120,
							align : "center",
							sortable : true,
							dataIndex : 'strTestDate'
						}, {
							header : "内容",
							width : 75,
							sortable : true,
							dataIndex : 'report.content',
							renderer : function(v) {
								if (v != null && v != '') {
									var s = '<a href="#" onclick="window.open(\''
											+ v
											+ '\');return  false;">[查看]</a>';
									return s;
								}
							}
						}, {
							header : "试验人员",
							width : 120,
							align : "center",
							sortable : true,
							dataIndex : 'testByName'
						}, {
							header : "试验负责人",
							width : 120,
							align : "center",
							sortable : true,
							dataIndex : 'chargeByName'
						}],
				tbar : [{
							text : '装置名称：'
						}, fuzzyText, '-', {
							text : '试验时间:'
						}, sDate, '-', {
							text : '至：'
						}, eDate, queryButton, westbtnAdd, {
							xtype : "tbseparator"
						}, westbtnedit, {
							xtype : "tbseparator"
						}, westbtndel, {
							xtype : "tbseparator"
						}, btndetails],
				sm : westsm,
				frame : true,
				bbar : new Ext.PagingToolbar({
							pageSize : 18,
							store : westgrids,
							displayInfo : true,
							displayMsg : "{0} 到 {1} /共 {2} 条",
							emptyMsg : "没有记录"
						}),
				border : true
			});

	// westgrid 的事件
	westgrid.on('rowdblclick', function(grid, rowIndex, e) {
				CKSelectdone()
			})

	// 定义grid
	var myRecord = Ext.data.Record.create([{
				// 试验报告记录编号
				name : 'pjjs.jdsybgjgId'
			}, {
				// 试验报告编号
				name : 'pjjs.jdsybgId'
			}, {
				// 试验报告名称
				name : 'jdsybgName'
			}, {
				// 试验项目编号
				name : 'syxmId'
			}, {
				// 试验项目名称
				name : 'syxmName'
			}, {
				// 试验点编号
				name : 'pjjs.sydId'
			}, {
				// 试验点名称
				name : 'sydName'
			}, {
				// 仪器仪表编号
				name : 'pjjs.regulatorId'
			}, {
				// 仪器仪表名称
				name : 'regulatorName'
			}, {
				// 试验结果
				name : 'pjjs.result'
			}, {
				// 单位编码
				name : 'pjjs.enterpriseCode'
			}, {
				// 合格下限
				name : 'minimum'
			}, {
				// 合格上限
				name : 'maximum'
			}]);
	var dataProxy = new Ext.data.HttpProxy({
				url : 'productionrec/findExperiReportDetailsList.action'
			});
	var theReader = new Ext.data.JsonReader({
				root : "list",
				totalProperty : "totalCount"
			}, myRecord);
	var store = new Ext.data.Store({
				proxy : dataProxy,
				reader : theReader
			});

	function queryRecord() {
		store.baseParams = {
			jdsybgId : findById
		}
		store.load({
					params : {
						start : 0,
						limit : 18						
					}
				});
	}
	var sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			});

	// 明细grid
	var detailsGrid = new Ext.grid.EditorGridPanel({
				region : "center",
				frame : false,
				border : false,
				height : 330,
				autoScroll : true,
				enableColumnMove : false,
				sm : sm,
				tbar : [{
							text : '保存',
							iconCls : 'save',
							handler : updateHandler
						}],
				store : store,
				columns : [new Ext.grid.RowNumberer({
									header : '行号',
									width : 35,
									align : 'left'
								}), {
							header : "试验报告记录编号",
							width : 100,
							sortable : true,
							dataIndex : 'pjjs.jdsybgjgId',
							hidden : true
						}, {
							header : "试验报告编号",
							width : 80,
							sortable : true,
							hidden : true,
							dataIndex : 'pjjs.jdsybgId'
						}, {
							header : "试验报告名称",
							width : 80,
							sortable : true,
							hidden : true,
							dataIndex : 'sydName'
						}, {
							header : "试验项目编号",
							width : 80,
							sortable : true,
							hidden : true,
							dataIndex : 'syxmId'
						}, {
							header : "试验项目名称",
							width : 80,
							sortable : true,
							dataIndex : 'syxmName'
						}, {
							header : "试验点编号",
							width : 80,
							sortable : true,
							hidden : true,
							dataIndex : 'pjjs.sydId'
						}, {
							header : "试验点名称",
							width : 80,
							sortable : true,
							dataIndex : 'sydName'
						}, {
							header : "仪器仪表编号",
							width : 80,
							sortable : true,
							hidden : true,
							dataIndex : 'pjjs.regulatorId'
						}, {
							header : "仪器仪表名称",
							width : 80,
							css : CSS_GRID_INPUT_COL,
							sortable : true,
							align : 'right',
							dataIndex : 'regulatorName'
						}, {
							header : "试验结果",
							width : 100,
							css : CSS_GRID_INPUT_COL,
							sortable : true,
							align : 'right',
							editor : new Ext.form.NumberField({
										maxValue : 99999999.99,
										// minValue : 0,
										allowDecimals : true,
										allowNegative : true
									}),
							dataIndex : 'pjjs.result'
						}, {
							header : "合格下限",
							width : 100,
							sortable : true,
							align : 'right',
							dataIndex : 'minimum'
						}, {
							header : "合格上限",
							width : 100,
							sortable : true,
							align : 'right',
							dataIndex : 'maximum'
						}],
				bbar : new Ext.PagingToolbar({
							pageSize : 18,
							store : store,
							displayInfo : true,
							displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
							emptyMsg : "没有记录"
						})
			});

	detailsGrid.on('celldblclick', choseEdit);

	function updateHandler() {
		var modifyRec = new Array();
		detailsGrid.stopEditing(); // 停止编辑
		if (store != null) {
			for (i = 0; i <= store.getCount() - 1; i++) {
				modifyRec[i] = store.getAt(i);
			}
		}
		if (modifyRec.length > 0) {
			Ext.Msg.confirm('提示', '确认保存表格中的数据?', function(button, text) {
				if (button == 'yes') {

					Ext.Msg.wait("正在保存数据,请等待...");
					var modifyRecords = new Array();

					for (var i = 0; i < modifyRec.length; i++) {
						modifyRecords.push(modifyRec[i].data);
					}
					Ext.Ajax.request({
								url : 'productionrec/modifyExperiReportDetails.action',
								method : 'post',
								params : {
									addOrUpdateRecords : Ext.util.JSON
											.encode(modifyRecords),
									jdsybgId : findById
								},
								success : function(result, request) {
									Ext.MessageBox.alert('提示', '操作成功！ ');
									detailsWin.hide();
								},
								failure : function(result, request) {
									Ext.MessageBox.alert('提示', '操作失败！ ');
								}
							})
				}
			})
		} else {
			Ext.Msg.alert('提示', '没有数据进行保存！');
		}
	}

	function choseEdit(grid, rowIndex, columnIndex, e) {
		if (rowIndex <= grid.getStore().getCount() - 1) {
			var record = grid.getStore().getAt(rowIndex);
			var fieldName = grid.getColumnModel().getDataIndex(columnIndex);
			if (fieldName == "regulatorName") {
				var object = {
					jdzyId : 9
				}
				var expr = window.showModalDialog(
						'../../../thermalSupervise/account_comm.jsp', object,
						'dialogWidth=800px;dialogHeight=550px;status=no');
				if (typeof(expr) != "undefined") {
					 record.set('pjjs.regulatorId', expr.regulatorId);
					 record.set('regulatorName', expr.names);
				}
			}

		}
	}

	// 试验报告记录窗口
	var detailsWin = new Ext.Window({
				title : '',
				layout : 'fit',
				width : 660,
				height : 460,
				modal : true,
				closable : true,
				border : false,
				resizable : true,
				closeAction : 'hide',
				plain : true,
				// 面板中按钮的排列方式
				buttonAlign : 'center',
				items : [detailsGrid]
			})
	
			
	recordQuery();
	// 设定布局器及面板
	var layout = new Ext.Viewport({
				layout : "border",
				border : false,
				autoScroll : true,
				items : [{
							region : 'center',
							layout : 'fit',
							border : false,
							margins : '1 0 1 1',
							autoScroll : true,
							items : [westgrid]
						}]
			});

});