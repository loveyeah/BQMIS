Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
// 部门考勤画面关联处理
DeptAttendance = function() {
	/**
	 * modified by liuyi 20100202 画面关联项排斥数组 true: 排斥 false: 不排斥
	 */
	this.checkArray = new Array(
			[true, false, false, false, false, false],
			[false, true, false, false, false, false],
			[false, false, true, false, false, false],
			[false, false, false, true, false, false],
			[false, false, false, false, true, false],
			[false, false, false, false, false, true]
		);

	/**
	 * 画面关联项名称
	 */
	this.nameArray = new Array('work','workShiftId', 'yearRest','changeRest','evectionType', 'outWork');
	/**
	 * 关联处理
	 */
	DeptAttendance.prototype.check = function(record, name) {
		var index = -1;
		// 查找名字对应的行
		for (var i = 0; i < this.nameArray.length; i++) {
			if (name == this.nameArray[i]) {
				index = i;
				break;
			}
		}
		// 找到
		if (index != -1) {
			// 找到对应的排斥关系
			var array = this.checkArray[index];
			for (var j = 0; j < array.length; j++) {
				// 排斥处理
				if (array[j] === false) {
					if (record.get(this.nameArray[j]) != ''
							&& record.get(this.nameArray[j]) != null) {
						record.set(this.nameArray[j], '');
					}
				}
			}
		}
	}
}

deptAttendanceNew = new DeptAttendance();

Ext.grid.CheckColumn = function(config) {
	Ext.apply(this, config);
	if (!this.id) {
		this.id = Ext.id();
	}
	this.renderer = this.renderer.createDelegate(this);
};

Ext.grid.CheckColumn.prototype = {
	init : function(grid) {
		this.grid = grid;
		this.grid.on('render', function() {
					var view = this.grid.getView();
					view.mainBody.on('mousedown', this.onMouseDown, this);
				}, this);
	},

	onMouseDown : function(e, t) {
		if (t.className && t.className.indexOf('x-grid3-cc-' + this.id) != -1) {
			var index = this.grid.getView().findRowIndex(t);
			var record = this.grid.store.getAt(index);
//			alert(Ext.encode(record.data))
			// grid是否可编辑
//			if (record.data['gridEditAble']) {
				record.set(this.dataIndex, !(record.data[this.dataIndex] * 1)
								? '1'
								: '0');
//			alert(Ext.encode(record.data))
			deptAttendanceNew.check(record,this.dataIndex);
//			}
		}
	},

	renderer : function(v, p, record) {
		p.css += ' x-grid3-check-col-td';
		return '<div class="x-grid3-check-col' + ((v === '1') ? '-on' : '')
				+ ' x-grid3-cc-' + this.id + '">&#160;</div>';
	}
};
Ext.onReady(function() {
	// 控件类型: 选择
	var TYPE_SELECT = "S";
	// 控件类型: 输入
	var TYPE_INPUT = "I";
	// 标识: 0
	var FLAG_0 = "0";
	// 标识: 1
	var FLAG_1 = "1";

	// 当前时间
	var time = new Date();
	// 日期
	var date = time.format('Y-m-d');
	// 星期
	var weekdayArray = new Array('星期日', '星期一', '星期二', '星期三', '星期四', '星期五',
			'星期六');
	var weekday = weekdayArray[time.getDay()];

	// 考勤日期
	var attendanceDate = new Ext.form.TextField({
				id : 'attendanceDate',
				style : 'cursor:pointer',
				value : date,
				readOnly : true,
				width : 85
			});

	// 选择日期
	attendanceDate.onClick(function() {
				WdatePicker({
							startDate : '%y-%M-%d',
							dateFmt : 'yyyy-MM-dd',
							isShowClear : false,
							onpicked : function() {
								var value = attendanceDate.getValue();
								time.setFullYear(value.substring(0, 4), value
												.substring(5, 7)
												* 1 - 1, value.substring(8, 10)
												* 1);
								weekdayTxt
										.setValue(weekdayArray[time.getDay()])
							}
						})
			});

	// 星期
	var weekdayTxt = new Ext.form.TextField({
				id : 'weekday',
				width : 85,
				value : weekday,
				disabled : true
			});

	// 考勤部门Store
	var attendanceDeptStore = new Ext.data.JsonStore({
				url : 'ca/getNewAttendanceDeptForRegister.action',
				root : 'list',
				fields : ['attendanceDeptId', 'attendanceDeptName']
			});

	// 考勤部门下拉框
	var attendanceDeptCmb = new Ext.form.ComboBox({
				id : 'attendanceDeptName',
				width : 150,
				allowBlank : false,
				triggerAction : 'all',
				store : attendanceDeptStore,
				displayField : 'attendanceDeptName',
				valueField : 'attendanceDeptId',
				mode : 'local',
				readOnly : true
			});

	// 加载数据
	attendanceDeptStore.load({
				callback : function() {
					attendanceDeptStore.insert(0, new Ext.data.Record({
										attendanceDeptId : '',
										attendanceDeptName : ''
									}));
				}
			});

	// 登记按钮
	var registerBtn = new Ext.Button({
				text : '登记',
				// text : Constants.BTN_REGISTER,
				iconCls : Constants.CLS_REGISTER,
				handler : registerHandler
			});

	// 保存按钮
	var saveBtn = new Ext.Button({
				text : Constants.BTN_SAVE,
				iconCls : Constants.CLS_SAVE,
				disabled : true,
				handler : saveHandler
			});

	// 取消按钮
	var cancelBtn = new Ext.Button({
				text : Constants.BTN_CANCEL,
				iconCls : Constants.CLS_CANCEL,
				handler : cancelHandler
			});

	// 定义grid中的数据
	var gridData = new Ext.data.Record.create([
			// 人员基本信息表
			{
		  // 人员ID
				name : 'empId',
				mapping : '0'
			}, {
				// 中文名
				name : 'chsName',
				mapping : '1'
			},
			// 部门设置表
			{
				// 部门ID
				name : 'deptId',
				mapping : '2'
			}, {
				// 部门名称
				name : 'deptName',
				mapping : '3'
			}, {
				name : 'attendanceDeptId',
				mapping : '4'
			}, {
				name : 'attendanceDeptName',
				mapping : '5'
			}, {
				name : 'work',
				mapping : '6'
			},
			// 考勤登记表
			{
				// 运行班类别ID
				name : 'workShiftId',
				mapping : '7'
			}// 加班时间id
			, {
				name : 'overtimeTypeId',
				mapping : '8'
			}, {
				name : 'overtimeTimeId',
				mapping : '9'
			} // 旷工时间Id
			, {
				name : 'absentTimeId',
				mapping : '10'
			}, {
				// 假别ID
				name : 'vacationTypeId',
				mapping : '11'
			} // 其他假时间id
			, {
				name : 'otherTimeId',
				mapping : '12'
			}, {
				name : 'changeRest',
				mapping : '13'
			}, {
				name : 'yearRest',
				mapping : '14'
			}
			// 出勤
			, {
				name : 'evectionType',
				mapping : '15'
			}, {
				name : 'outWork',
				mapping : '16'
			}, {
				name : 'memo',
				mapping : '17'
			},{
				name : 'existFlag',
				mapping : '18'
			}]);

	// 定义运行班种类的store
	var workshiftStore = new Ext.data.JsonStore({
				root : 'list',
				url : 'ca/getWorkshiftTypeCommon.action',
				fields : ['workShiftId', 'workShift']
			});

	// 定义请假种类的store
	var vacationStore = new Ext.data.JsonStore({
				root : 'list',
				url : 'ca/getVacationTypeCommon.action',
				fields : ['vacationTypeId', 'vacationType']
			});

	// add by liuyi 20100202 定义基础天数store
	var basicDaysStore = new Ext.data.JsonStore({
				root : 'list',
				url : 'ca/getBasicDaysCommon.action',
				fields : ['id', 'baseDays']
			})
	// 定义加班种类的store
	var overtimeStore = new Ext.data.JsonStore({
				root : 'list',
				url : 'ca/getOvertimeTypeCommon.action',
				fields : ['overtimeTypeId', 'overtimeType']
			});

	// 换休checkbox
	var changeResetCheckColumn = new Ext.grid.CheckColumn({
				header : '换休',
				dataIndex : 'changeRest',
				width : 50
			});

	// 年息checkbox
	var yearResetCheckColumn = new Ext.grid.CheckColumn({
				header : '年休',
				dataIndex : 'yearRest',
				width : 50
			});

	// 出差checkbox
	var evectionCheckColumn = new Ext.grid.CheckColumn({
				header : '出差',
				dataIndex : 'evectionType',
				width : 50
			});

	// 外借checkbox
	var outCheckColumn = new Ext.grid.CheckColumn({
				header : '外借',
				dataIndex : 'outWork',
				width : 50
			});

	// 出勤checkbox
	var workCheckColumn = new Ext.grid.CheckColumn({
				header : '出勤',
				dataIndex : 'work',
				width : 50
			});
	//    
	// grid store
	var gridStore = new Ext.data.JsonStore({
				url : 'ca/getDetailInfoForRegisterNew.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : gridData
			});
	// 一览grid
			//update by sychen 20100730
	//var grid = new Ext.grid.LockingGridPanel({
	var grid = new Ext.grid.EditorGridPanel({
		autoWidth : true,
		store : gridStore,
		region : 'center',
		sm : new Ext.grid.RowSelectionModel({
					// 单选
					singleSelect : true
				}),
		// 单击进行编辑
		clicksToEdit : 1,
		columns : [new Ext.grid.RowNumberer({
							header : '行号',
							width : 35,
							locked:true//add by sychen 20100730
						}), {
					header : '员工姓名',
					width : 80,
					sortable : true,
					dataIndex : 'chsName',
					locked:true//add by sychen 20100730
				}, {
					header : '所属部门',
					width : 100,
					sortable : true,
					dataIndex : 'deptName',
					locked:true//add by sychen 20100730
				}, workCheckColumn, {
					header : '运行班',
					width : 80,
					sortable : true,
					dataIndex : 'workShiftId',
					editor : new Ext.form.ComboBox({
								readOnly : true,
								triggerAction : 'all',
								store : workshiftStore,
								mode : 'local',
								valueNotFoundText : '',
								displayField : 'workShift',
								valueField : 'workShiftId',
								listClass : 'x-combo-list-small'
							}),
					renderer : getWorkShift
				},
				{
					header : '加班',
					width : 80,
					sortable : true,
					dataIndex : 'overtimeTypeId',
					editor : new Ext.form.ComboBox({
								readOnly : true,
								triggerAction : 'all',
								store : overtimeStore,
								mode : 'local',
								valueNotFoundText : '',
								displayField : 'overtimeType',
								valueField : 'overtimeTypeId',
								listClass : 'x-combo-list-small'
							}),
					renderer : getOvertimeType
				}
				, {
					header : '加班时间',
					width : 80,
					sortable : true,
					dataIndex : 'overtimeTimeId',
					editor : new Ext.form.ComboBox({
								readOnly : true,
								triggerAction : 'all',
								store : basicDaysStore,
								mode : 'local',
								valueNotFoundText : '',
								displayField : 'baseDays',
								valueField : 'id',
								listClass : 'x-combo-list-small'
							}),
					renderer : getBasicDayJB
				}, {
					header : '旷工时间',
					width : 80,
					sortable : true,
					dataIndex : 'absentTimeId',
					editor : new Ext.form.ComboBox({
								readOnly : true,
								triggerAction : 'all',
								store : basicDaysStore,
								mode : 'local',
								valueNotFoundText : '',
								displayField : 'baseDays',
								valueField : 'id',
								listClass : 'x-combo-list-small'
							}),
					renderer : getBasicDayKG
				}, {
					header : '请假事项',
					width : 80,
					sortable : true,
					dataIndex : 'vacationTypeId',
					editor : new Ext.form.ComboBox({
								readOnly : true,
								triggerAction : 'all',
								store : vacationStore,
								mode : 'local',
								valueNotFoundText : '',
								displayField : 'vacationType',
								valueField : 'vacationTypeId',
								listClass : 'x-combo-list-small'
							}),
					renderer : getVacationType
				}, {
					header : '请假时间',
					width : 80,
					sortable : true,
					dataIndex : 'otherTimeId',
					editor : new Ext.form.ComboBox({
								readOnly : true,
								triggerAction : 'all',
								store : basicDaysStore,
								mode : 'local',
								valueNotFoundText : '',
								displayField : 'baseDays',
								valueField : 'id',
								listClass : 'x-combo-list-small'
							}),
					renderer : getBasicDayBJ
				}, changeResetCheckColumn, yearResetCheckColumn,
				evectionCheckColumn, outCheckColumn, {
					header : '备注',
					width : 150,
					sortable : true,
					dataIndex : 'memo',
					editor : new Ext.form.TextArea({
								maxLength : 128,
								id : 'test',
								grow : true,
								listeners : {
									'render' : function() {
										this.el.on('dblclick', function() {
													var record = grid
															.getSelectionModel()
															.getSelected();
													grid.stopEditing();
													taShowMemo.setValue(record
															.get('memo'));
													taShowMemo
															.setDisabled(false);
													win.x = undefined;
													win.y = undefined;
													win.setTitle('详细信息录入窗口');
													win.show();
													winSaveBtn.setVisible(true);
													winCanelBtn
															.setVisible(true);
													winCloseBtn
															.setVisible(false);
												})
									}
								}
							})
				}],
		plugins : [workCheckColumn, changeResetCheckColumn,
				yearResetCheckColumn, evectionCheckColumn, outCheckColumn],
		frame : false,
		border : true,
		enableColumnHide : true,
		enableColumnMove : false
	});

//	// 设置grid可否编辑
//	grid.on('beforeedit', function() {
//				if (gridStore.getCount() > 0) {
//					if (!gridStore.getAt(0).data['gridEditAble']) {
//						return false;
//					}
//				}
//				return true;
//			});

	// 关联处理
	grid.on('afteredit', function(object) {
				if (gridStore.getCount() > 0) {
					if (object.record.get(object.field) != '') {
						deptAttendanceNew.check(object.record, object.field);
					}
				}
			});

	// 单元格双击事件
	grid.on('celldblclick', dbClickHandler);

	// panel
	var panel = new Ext.Panel({
				tbar : ['考勤日期<font color="red">*</font>:', attendanceDate,
						'&nbsp;', weekdayTxt, '-',
						'考勤部门<font color="red">*</font>:', attendanceDeptCmb,
						'-', registerBtn, saveBtn, cancelBtn],
				border : false,
				frame : false,
				layout : 'border',
				items : [grid]
			});


	var hrAttendance = new hr.Attendance();
	var tab = new Ext.TabPanel({
				activeTab : 0,
				tabPosition : 'bottom',
				plain : false,
				defaults : {
					autoScroll : true
				},
				items : [{
							id : 'trainRegister',
							title : '考勤员登记',
							items : panel,
							autoScroll : true,
							layout : 'fit'
						}, {
							id : 'trainList',
							title : '考勤员上报',
							autoScroll : true,
							items : hrAttendance.fullPanel,
							layout : 'fit'
						}]
			})
	// 设定布局器及面板
	new Ext.Viewport({
				enableTabScroll : true,
				layout : 'fit',
				border : false,
				items : [tab]
			});
	// 备注编辑
	var taShowMemo = new Ext.form.TextArea({
				maxLength : 128
			});

	// 保存按钮
	var winSaveBtn = new Ext.Button({
				text : Constants.BTN_SAVE,
				iconCls : Constants.CLS_SAVE,
				handler : function() {
					if (taShowMemo.isValid()) {
						var record = grid.getSelectionModel().getSelected();
						record.set('memo', taShowMemo.getValue());
						win.hide();
					}
				}
			});

	// 取消按钮
	var winCanelBtn = new Ext.Button({
				text : Constants.BTN_CANCEL,
				iconCls : Constants.CLS_CANCEL,
				handler : function() {
					win.hide();
				}
			});

	// 关闭按钮
	var winCloseBtn = new Ext.Button({
				text : Constants.BTN_CLOSE,
				iconCls : Constants.CLS_CANCEL,
				handler : function() {
					win.hide();
				}
			});

	// 弹出画面
	var win = new Ext.Window({
				height : 170,
				width : 350,
				layout : 'fit',
				resizable : false,
				modal : true,
				closeAction : 'hide',
				items : [taShowMemo],
				buttonAlign : "center",
				buttons : [winSaveBtn, winCanelBtn, winCloseBtn],
				listeners : {
					show : function() {
						taShowMemo.focus(true, 100);
					},
					hide : function() {
						taShowMemo.setValue("");
					}
				}
			});

	// ========================= 处理 ======================= //

	/**
	 * 登记处理
	 */
	function registerHandler(flag) {
		// check
		if (checkQuery() && checkDate()) {
			Ext.lib.Ajax.request(Constants.POST,
					'ca/getNewApprovedForRegister.action', {
						success : function(result, request) {
							if (result.responseText) {
								var o = eval("(" + result.responseText + ")");
								var succ = o.msg;
								// 已审核的场合
								if (succ == true) {
									Ext.Msg.confirm(Constants.NOTICE_CONFIRM,
											Constants.KQ005_C_001, function(
													buttonobj) {
												if (buttonobj == "yes") {
													// 加载数据
													loadData(true);
													// 登记按钮不可用
													registerBtn
															.setDisabled(true);
													// 考勤日期不可用
													attendanceDate
															.setDisabled(true);
													// 考勤部门不可用
													attendanceDeptCmb
															.setDisabled(true);
												}
											});
									// 未审核的场合
								} else if (succ == false) {
									if (typeof(flag) == 'object') {
										Ext.lib.Ajax
												.request(
														Constants.POST,
														'ca/getNewEmpExistForRegister.action',
														{
															success : function(
																	result,
																	request) {
																if (result.responseText) {
																	var o = eval("("
																			+ result.responseText
																			+ ")");
																	var succ = o.msg;
																	// 检索结果大于0的场合
																	if (succ == true) {
																		Ext.Msg
																				.confirm(
																						Constants.NOTICE_CONFIRM,
																						Constants.KQ005_C_002,
																						function(
																								buttonobj) {
																							if (buttonobj == "yes") {
																								// 加载数据
																								loadUnCheckedData();
																							}
																						});
																		// 检索结果等于0的场合
																	} else {
																		// 加载数据
																		loadUnCheckedData();
																	}
																}
															},
															failure : function() {
																Ext.Msg
																		.alert(
																				Constants.ERROR,
																				Constants.COM_E_014);
															}
														},
														'attendanceDate='
																+ attendanceDate
																		.getValue()
																+ '&attendanceDeptId='
																+ attendanceDeptCmb
																		.getValue());
									} else {
										loadUnCheckedData();
									}
								}
							}
						},
						failure : function() {
							Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
						}
					}, 'attendanceDate=' + attendanceDate.getValue()
							+ '&attendanceDeptId='
							+ attendanceDeptCmb.getValue());
		}
	}

	/**
	 * 保存处理
	 */
	function saveHandler() {
		Ext.Msg.confirm(Constants.NOTICE_CONFIRM, Constants.COM_C_001,
				function(buttonobj) {
//						alert(0)
//					return;
					if (buttonobj == 'yes') {
						if (checkGridTime()) {
							// 设置出勤标识
							setAttendanceFlag();
							// 设置迟到早退标识
							setLateEarlyFlag();
							// 定义记录数组
							var recordArray = new Array();
							// 将grid中的记录转化为数组
							for (var i = 0; i < gridStore.getCount(); i++) {
								recordArray.push(gridStore.getAt(i).data);
							}
							// 转化为字符串
							var records = Ext.util.JSON.encode(recordArray);
							var params = {
								'records' : records,
								'attendanceDate' : attendanceDate.getValue(),
								'attendanceDeptId' : attendanceDeptCmb.getValue()
							};
							Ext.Ajax.request({
								method : Constants.POST,
								url : 'ca/saveNewDeptAttendanceForRegister.action',
								params : params,
								success : function(result, request) {
									if (result.responseText) {
										var o = eval("(" + result.responseText
												+ ")");
										var succ = o.msg;
										if (succ === true) {
											Ext.Msg.alert(Constants.REMIND,
													Constants.COM_I_004);
											// 刷新grid中的数据, 并且不弹出确认对话框
											registerHandler(true);
											queryInit();//add by sychen 20100908
											if(queryFlag==true){//add by sychen 20100908
											  queryAll();
											}
										} else if (succ == Constants.DATE_FAILURE) {
											Ext.Msg.alert(Constants.ERROR,
													Constants.COM_E_023);
										} else if (succ == Constants.SQL_FAILURE) {
											Ext.Msg.alert(Constants.ERROR,
													Constants.COM_E_014);
										} else if (succ == Constants.DATA_USING) {
											Ext.Msg.alert(Constants.ERROR,
													Constants.COM_E_015);
										}
									}
								},
								failure : function() {
									Ext.Msg.alert(Constants.ERROR,
											Constants.COM_E_014);
								}
							});
						}
					}
				});
	}

	/**
	 * 加载未审核的数据
	 */
	function loadUnCheckedData() {
		// 加载数据
		loadData(false);
		// 登记按钮不可用
		registerBtn.setDisabled(true);
		// 考勤日期不可用
		attendanceDate.setDisabled(true);
		// 考勤部门不可用
		attendanceDeptCmb.setDisabled(true);
	}

	/**
	 * 取消处理
	 */
	function cancelHandler() {
		Ext.MessageBox.confirm(Constants.CONFIRM, Constants.COM_C_005,
				function(buttonobj) {
					if (buttonobj == "yes") {
						// 登记按钮可用
						registerBtn.setDisabled(false);
						// 保存按钮不可用
						saveBtn.setDisabled(true);
						// 考勤日期可用
						attendanceDate.setDisabled(false);
						// 考勤部门可用
						attendanceDeptCmb.setDisabled(false);
						// grid初期化
						gridStore.removeAll();
						grid.getView().refresh();
					}
				});
	}

	/**
	 * 加载数据
	 */
	function loadData(flag) {
		// 加载完成计数器
		var count = 0;
		// 加载请假数据
		vacationStore.load({
					callback : function() {
						vacationStore.insert(0, new Ext.data.Record({
											vacationTypeId : '',
											vacationType : ''
										}));
						loadGridData(++count, flag);
					}
				});
		// 加载加班数据
		overtimeStore.load({
					callback : function() {
						overtimeStore.insert(0, new Ext.data.Record({
											overtimeTypeId : '',
											overtimeType : ''
										}));
						loadGridData(++count, flag);
					}
				});
		// 加载运行班数据
		workshiftStore.load({
					callback : function() {
						workshiftStore.insert(0, new Ext.data.Record({
											workShiftId : '',
											workShift : ''
										}));
						loadGridData(++count, flag);
					}
				});

		// 加载基本天数数据 add by liuyi 20100202
		basicDaysStore.load({
					callback : function() {
						basicDaysStore.each(function(obj) {
									obj.set('baseDays', obj.get('baseDays')
													+ '天')
								})
						basicDaysStore.insert(0, new Ext.data.Record({
											id : '',
											baseDays : ''
										}))
						loadGridData(++count, flag);
					}
				})
	}

	/**
	 * 加载grid中的数据
	 */
	function loadGridData(count, flag) {
		// 如果运行班，加班，请假都已经加载完成
		// modified by liuyi 20100202 增加一个基本天数加载
		// if(count == 3) {
		if (count == 4) {
			// 画面排序恢复初始状态
			gridStore.sortInfo = null;
			grid.getView().removeSortIcon();
			// 加载明细部数据
			gridStore.load({
						params : {
							attendanceDate : attendanceDate.getValue(),
							attendanceDeptId : attendanceDeptCmb.getValue(),
							flag : flag,
							attendanceWeekday : weekdayArray.indexOf(weekdayTxt
									.getValue())
						},
						callback : function() {
							if (gridStore.getCount() > 0) {
								if (!flag) {
									// 保存按钮可用
									saveBtn.setDisabled(false);
								}
							}
						}
					});
		}
	}
	/**
	 * 单元格双击处理
	 */
	function dbClickHandler(grid, rowIndex, columnIndex, e) {
		// 获取当前记录
		var record = gridStore.getAt(rowIndex);
		// grid是否可编辑
//		if (!record.get('gridEditAble')) {
			// 编辑列的字段名
			var fieldName = grid.getColumnModel().getDataIndex(columnIndex);
			// 如果是备注列
			if ('memo' == fieldName) {
				taShowMemo.setValue(record.get('memo'));
				taShowMemo.setDisabled(true);
				win.x = undefined;
				win.y = undefined;
				win.setTitle('详细信息查看窗口');
				win.show();
				winSaveBtn.setVisible(false);
				winCanelBtn.setVisible(false);
				winCloseBtn.setVisible(true);
			}
//		}
	}

	/**
	 * 设置出勤标识
	 */
	function setAttendanceFlag() {
		for (var i = 0; i < gridStore.getCount(); i++) {
			var record = gridStore.getAt(i);
			// 行政班和运行班只要有一个不为空
			if (!isEmpty(record.get('amBeginTime'))
					|| !isEmpty(record.get('pmBeginTime'))
					|| !isEmpty(record.get('workShiftId'))) {
				record.set('attendanceFlag', FLAG_1);
			} else {
				record.set('attendanceFlag', FLAG_0);
			}
		}
	}

	/**
	 * 设置迟到早退标识
	 */
	function setLateEarlyFlag() {
		for (var i = 0; i < gridStore.getCount(); i++) {
			var record = gridStore.getAt(i);
			// 出勤的情况下
			if ((record.get('attendanceFlag') == FLAG_1)) {
				// 标准时间不为空
				if (!isEmpty(record.get('standardAmBeginTime'))) {
					var amEarly = false;
					var amLate = false;
					var pmEarly = false;
					var pmLate = false;

					// 上午上班
					if (!isEmpty(record.get('amBeginTime'))) {
						// 上午迟到
						if (record.get('amBeginTime') > record
								.get('standardAmBeginTime')) {
							amLate = true;
						}
						// 上午早退
						if (record.get('amEndTime') < record
								.get('standardAmEndTime')) {
							amEarly = true;
						}
					}

					// 下午上班
					if (!isEmpty(record.get('pmBeginTime'))) {
						// 下午迟到
						if (record.get('pmBeginTime') > record
								.get('standardPmBeginTime')) {
							pmLate = true;
						}
						// 下午早退
						if (record.get('pmEndTime') < record
								.get('standardPmEndTime')) {
							pmEarly = true;
						}
					}

					// 上午或下午迟到
					if (amLate || pmLate) {
						// 迟到
						record.set('lateWorkFlag', FLAG_1);
					} else {
						record.set('lateWorkFlag', FLAG_0);
					}

					// 上午或下午早退
					if (amEarly || pmEarly) {
						// 早退
						record.set('leaveEarlyFlag', FLAG_1);
					} else {
						record.set('leaveEarlyFlag', FLAG_0);
					}
				} else {
					record.set('lateWorkFlag', FLAG_0);
					record.set('leaveEarlyFlag', FLAG_0);
				}
			} else {
				record.set('lateWorkFlag', FLAG_0);
				record.set('leaveEarlyFlag', FLAG_0);
			}
		}
	}

	/**
	 * 检查日期是否合法
	 */
	function checkDate() {
		var nowDate = new Date().format('Y-m-d');
		if (attendanceDate.getValue() > nowDate) {
			Ext.Msg.alert(Constants.ERROR, String.format(Constants.COM_E_052,
							'考勤日期'));
			return false;
		}
		return true;
	}

	/**
	 * 检查查询条件是否为空
	 */
	function checkQuery() {
		var msg = "";
		msg += checkInput(attendanceDate, "考勤日期", TYPE_SELECT);
		msg += checkInput(attendanceDeptCmb, "考勤部门", TYPE_SELECT);
		if (msg != "") {
			Ext.Msg.alert(Constants.ERROR, msg);
			return false;
		}
		return true;
	}

	/**
	 * 检查必须输入项
	 */
	function checkInput(component, name, type) {
		var message = "";
		if (component.getValue() == "" || component.getValue() == null) {
			if (type == TYPE_INPUT) {
				message += String.format(Constants.COM_E_002, name);
			} else if (type == TYPE_SELECT) {
				message += String.format(Constants.COM_E_003, name);
			}
			message += "</BR>";
		}
		return message;
	}

	/**
	 * 检查上下班时间是否合法
	 */
	function checkGridTime() {
		var msg = '';
		for (var i = 0; i < gridStore.getCount(); i++) {
			var record = gridStore.getAt(i);
			// 上午上班和下班时间不都为空
			if (!(isEmpty(record.get('amBeginTime')) && isEmpty(record
					.get('amEndTime')))) {
				// 上午上班或下班时间为空
				if (isEmpty(record.get('amBeginTime'))
						|| isEmpty(record.get('amEndTime'))) {
					msg += String.format(Constants.COM_E_051, i + 1, '上午上班时间',
							'上午下班时间');
					msg += '</BR>';
					// 上午上班或下班时间都不为空
				} else if (record.get('amBeginTime') >= record.get('amEndTime')) {
					msg += String.format(Constants.COM_E_050, i + 1, '上午上班时间',
							'上午下班时间');
					msg += '</BR>';
				}
			}
			// 下午上班和下班时间不都为空
			if (!(isEmpty(record.get('pmBeginTime')) && isEmpty(record
					.get('pmEndTime')))) {
				// 下午上班或下班时间为空
				if (isEmpty(record.get('pmBeginTime'))
						|| isEmpty(record.get('pmEndTime'))) {
					msg += String.format(Constants.COM_E_051, i + 1, '下午上班时间',
							'下午下班时间');
					msg += '</BR>';
					// 下午上班或下班时间都不为空
				} else if (record.get('pmBeginTime') >= record.get('pmEndTime')) {
					msg += String.format(Constants.COM_E_050, i + 1, '下午上班时间',
							'下午下班时间');
					msg += '</BR>';
				}
			}
		}
		if (msg != '') {
			Ext.Msg.alert(Constants.ERROR, msg);
			return false;
		}
		return true;
	}

	/**
	 * 取得对应的运行班类别
	 */
	function getWorkShift(value, cellmeta, record, rowIndex, columnIndex, store) {
		if (value != '' && value != null) {
			for (var i = 0; i < workshiftStore.getCount(); i++) {
				if (workshiftStore.getAt(i).get("workShiftId") == value) {
					return workshiftStore.getAt(i).get("workShift");
				}
			}
			gridStore.getAt(rowIndex).set('workShiftId', '');
			record.modified = {};
			record.dirty = false;
		}
		return "";
	}

	/**
	 * 取得对应的请假类别
	 */
	function getVacationType(value, cellmeta, record, rowIndex, columnIndex,
			store) {
		if (value != '' && value != null) {
			for (var i = 0; i < vacationStore.getCount(); i++) {
				if (vacationStore.getAt(i).get("vacationTypeId") == value) {
					return vacationStore.getAt(i).get("vacationType");
				}
			}
			gridStore.getAt(rowIndex).set('vacationTypeId', '');
			record.modified = {};
			record.dirty = false;
		}
		return "";
	}

	/**
	 * 取得对应的加班类别
	 */
	function getOvertimeType(value, cellmeta, record, rowIndex, columnIndex,
			store) {
		if (value != '' && value != null) {
			for (var i = 0; i < overtimeStore.getCount(); i++) {
				if (overtimeStore.getAt(i).get("overtimeTypeId") == value) {
					return overtimeStore.getAt(i).get("overtimeType");
				}
			}
			record.set('overtimeTypeId', '');
			record.modified = {};
			record.dirty = false;
		}
		return "";
	}

	/**
	 * 取得对应的基本天数 加班 add by liuyi 20100202
	 */
	function getBasicDayJB(value, cellmeta, record, rowIndex, columnIndex,
			store) {
		if (value != '' && value != null) {
			for (var i = 0; i < basicDaysStore.getCount(); i++) {
				if (basicDaysStore.getAt(i).get("id") == value) {
					return basicDaysStore.getAt(i).get("baseDays");
				}
			}
			record.set('overtimeTimeId', '');
			record.modified = {};
			record.dirty = false;
		}
		return "";
	}

	/**
	 * 取得对应的基本天数 旷工 add by liuyi 20100202
	 */
	function getBasicDayKG(value, cellmeta, record, rowIndex, columnIndex,
			store) {
		if (value != '' && value != null) {
			for (var i = 0; i < basicDaysStore.getCount(); i++) {
				if (basicDaysStore.getAt(i).get("id") == value) {
					return basicDaysStore.getAt(i).get("baseDays");
				}
			}
			record.set('absentTimeId', '');
			record.modified = {};
			record.dirty = false;
		}
		return "";
	}

	/**
	 * 取得对应的基本天数 病假 add by liuyi 20100202
	 */
	function getBasicDayBJ(value, cellmeta, record, rowIndex, columnIndex,
			store) {
		if (value != '' && value != null) {
			for (var i = 0; i < basicDaysStore.getCount(); i++) {
				if (basicDaysStore.getAt(i).get("id") == value) {
					return basicDaysStore.getAt(i).get("baseDays");
				}
			}
			record.set('sickLeaveTimeId', '');
			record.modified = {};
			record.dirty = false;
		}
		return "";
	}

	/**
	 * 取得对应的基本天数 事假 add by liuyi 20100202
	 */
	function getBasicDaySJ(value, cellmeta, record, rowIndex, columnIndex,
			store) {
		if (value != '' && value != null) {
			for (var i = 0; i < basicDaysStore.getCount(); i++) {
				if (basicDaysStore.getAt(i).get("id") == value) {
					return basicDaysStore.getAt(i).get("baseDays");
				}
			}
			record.set('eventTimeId', '');
			record.modified = {};
			record.dirty = false;
		}
		return "";
	}

	/**
	 * 取得对应的基本天数 其他假 add by liuyi 20100202
	 */
	function getBasicDayQT(value, cellmeta, record, rowIndex, columnIndex,
			store) {
		if (value != '' && value != null) {
			for (var i = 0; i < basicDaysStore.getCount(); i++) {
				if (basicDaysStore.getAt(i).get("id") == value) {
					return basicDaysStore.getAt(i).get("baseDays");
				}
			}
			record.set('otherTimeId', '');
			record.modified = {};
			record.dirty = false;
		}
		return "";
	}
	/**
	 * 判断是否为空
	 */
	function isEmpty(value) {
		return value == '' || value == null;
	}
});