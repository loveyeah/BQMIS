Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	Ext.QuickTips.init();
	// 专业
	var url = "timework/useprolist.action";
	var conn = Ext.lib.Ajax.getConnectionObject().conn;
	conn.open("POST", url, false);
	conn.send(null);
	var search_data1 = eval('(' + conn.responseText + ')');

	// 工作类型
	var url = "timework/uselistTimeworktype.action";
	var conn = Ext.lib.Ajax.getConnectionObject().conn;
	conn.open("POST", url, false);
	conn.send(null);
	var search_data2 = eval('([' + conn.responseText + '])');

	// 格式化时间函数
	Date.prototype.format = function(format) {
		var o = {
			"M+" : this.getMonth() + 1, // month
			"d+" : this.getDate(), // day
			"h+" : this.getHours(), // hour
			"m+" : this.getMinutes(), // minute
			"s+" : this.getSeconds(), // second
			"q+" : Math.floor((this.getMonth() + 3) / 3), // quarter
			"S" : this.getMilliseconds()
			// millisecond
		}
		if (/(y+)/.test(format))
			format = format.replace(RegExp.$1, (this.getFullYear() + "")
							.substr(4 - RegExp.$1.length));
		for (var k in o)
			if (new RegExp("(" + k + ")").test(format))
				format = format.replace(RegExp.$1, RegExp.$1.length == 1
								? o[k]
								: ("00" + o[k]).substr(("" + o[k]).length));
		return format;
	}

	// 第几周
	var week_no_data = [["- - -", "0"], ["第一周", "1"], ["第二周", "2"],
			["第三周", "3"], ["第四周", "4"], ["第五周", "5"]];
	// 日期
	var week_day_data1 = [["1", "1"], ["2", "2"], ["3", "3"], ["4", "4"],
			["5", "5"], ["6", "6"], ["7", "7"], ["8", "8"], ["9", "9"],
			["10", "10"], ["11", "11"], ["12", "12"], ["13", "13"],
			["14", "14"], ["15", "15"], ["16", "16"], ["17", "17"],
			["18", "18"], ["19", "19"], ["20", "20"], ["21", "21"],
			["22", "22"], ["23", "23"], ["24", "24"], ["25", "25"],
			["26", "26"], ["27", "27"], ["28", "28"], ["29", "29"],
			["30", "30"], ["31", "31"]];
	var week_day_data2 = [["日", "1"], ["一", "2"], ["二", "3"], ["三", "4"],
			["四", "5"], ["五", "6"], ["六", "7"]];

	// 子表月份
	// 月份数据
	var month_data = [["每月", "0"], ["一月", "1"], ["二月", "2"], ["三月", "3"],
			["四月", "4"], ["五月", "5"], ["六月", "6"], ["七月", "7"], ["八月", "8"],
			["九月", "9"], ["十月", "10"], ["十一月", "11"], ["十二月", "12"]]

	// 月份
	var month = {
		fieldLabel : "月份",
		store : new Ext.data.SimpleStore({
					fields : ['name', 'id'],
					data : month_data
				}),
		valueField : 'id',
		displayField : 'name',
		mode : 'local',
		forceSelection : true,
		hiddenName : 'timeworkdInfo.month',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		name : 'month',
		id : "month",
		xtype : "combo",
		value : "0",
		allowBlank : false,
		emptyText : '请选择...',
		blankText : '请选择',
		readOnly : true
	};

	// 子表第几周
	var month_week_no = new Ext.form.ComboBox({
				id : "monthweekNo",
				xtype : "combo",
				name : 'monthweekNo',
				allowBlank : true,
				triggerAction : 'all',
				store : new Ext.data.SimpleStore({
							fields : ['name', 'id'],
							data : week_no_data
						}),
				hiddenName : 'timeworkdInfo.weekNo',
				displayField : 'name',
				valueField : 'id',
				fieldLabel : "第几周",
				listeners : {
					select : function(index, scrollIntoView) {
						var getvalue = index.value;
						if (getvalue == 0) {
							month_week_day.store.loadData(week_day_data1);
						} else {
							Ext.get("testDay").dom.value = "";
							month_week_day.store.loadData(week_day_data2);
						}
					}
				},
				mode : 'local',
				emptyText : '请选择...',
				blankText : '请选择',
				readOnly : true
			});

	// 子表日期
	var month_week_day = new Ext.form.ComboBox({
				id : "testDay",
				xtype : "combo",
				name : 'testDay',
				allowBlank : false,
				triggerAction : 'all',
				store : new Ext.data.SimpleStore({
							fields : ['name', 'id'],
							data : week_day_data1
						}),
				hiddenName : 'timeworkdInfo.testDay',
				displayField : 'name',
				valueField : 'id',
				fieldLabel : "日期",
				mode : 'local',
				emptyText : '请选择...',
				blankText : '请选择',
				readOnly : true
			});

	var class_sequence_data1 = [];
	// 子表班次
	var month_class_sequence = new Ext.form.ComboBox({
				id : "monthclassSequence",
				xtype : "combo",
				name : 'monthclassSequence',
				allowBlank : false,
				triggerAction : 'all',
				store : new Ext.data.SimpleStore({
							fields : ['name', 'id'],
							data : class_sequence_data1
						}),
				hiddenName : 'timeworkdInfo.classSequence',
				displayField : 'name',
				valueField : 'id',
				fieldLabel : "班次",
				mode : 'local',
				emptyText : '请选择...',
				blankText : '请选择',
				readOnly : true
			});

	// 子表
	// South中的Grid的button 添加按钮 窗体对象 表单对象
	var blockdForm = new Ext.form.FormPanel({
				labelAlign : 'right',
				frame : true,
				items : [{
							border : false,
							layout : 'form',
							items : [{
										border : false,
										layout : 'form',
										name : 'timeworkdInfo',
										columnWidth : 1,
										items : [month]
									}, {
										border : false,
										layout : 'form',
										columnWidth : 1,
										items : [month_week_no]
									}, {
										border : false,
										layout : 'form',
										columnWidth : 1,
										items : [month_week_day]
									}, {
										border : false,
										layout : 'form',
										columnWidth : 1,
										items : [month_class_sequence]
									}]
						}]
			});

	// South中的Grid的button 添加按钮 窗体对象
	var blockAdddWindow;
	function showAdddWindow() {
		if (!blockAdddWindow) {
			blockAdddWindow = new Ext.Window({
				title : '',
				layout : 'fit',
				width : 300,
				height : 180,
				closable : true,
				border : false,
				resizable : false,
				closeAction : 'hide',
				modal : true,
				plain : true,
				// 面板中按钮的排列方式
				buttonAlign : 'center',
				items : [blockdForm],
				buttons : [{
					text : '保存',
					handler : function() {
						if (blockdForm.getForm().isValid())
							if (op == "add") {
								var rec = centergrid.getSelectionModel()
										.getSelections();
								var codes = "";
								for (var i = 0; i < rec.length; i++) {
									codes += rec[i].data.workItemCode + ",";
								}
								blockdForm.getForm().submit({
									waitMsg : '保存中,请稍后...',
									url : "timework/addTimeworkd.action",
									params : {
										method : "add",
										classSequence : Ext
												.get("monthclassSequence").dom.value,
										codes : codes
									},
									success : function(form, action) {
										var o = eval('('
												+ action.response.responseText
												+ ')');
										Ext.MessageBox.alert('成功', o.eMsg);
										blockdForm.getForm().reset();
										blockAdddWindow.hide();
										southds.reload();
									},
									failure : function(form, action) {
										var o = eval('('
												+ action.response.responseText
												+ ')');
										Ext.MessageBox.alert('错误', o.eMsg);
									}
								});
							} else if (op == "edit") {
								blockdForm.getForm().submit({
									waitMsg : '保存中,请稍后...',
									url : "timework/updateTimeworkd.action",
									params : {
										method : "edit",
										id : southgrid.getSelectionModel()
												.getSelected().data.id,
										classSequence : Ext
												.get("monthclassSequence").dom.value
									},
									success : function(form, action) {
										blockdForm.getForm().reset();
										blockAdddWindow.hide();
										southds.reload();
									},
									failure : function(form, action) {
										var o = eval('('
												+ action.response.responseText
												+ ')');
										Ext.MessageBox.alert('错误', o.eMsg);
									}
								});
							}
					}
				}, {
					text : '取消',
					handler : function() {
						blockdForm.getForm().reset();
						blockAdddWindow.hide();
					}
				}]
			});
		}
		// 子表班次
		var url = "timework/findShfitTimeBySpecial.action?spcode="
				+ centergrid.getSelectionModel().getSelected().data.machprofCode;
		var conn = Ext.lib.Ajax.getConnectionObject().conn;
		conn.open("POST", url, false);
		conn.send(null);
		class_sequence_data1 = eval('(' + conn.responseText + ')');
		month_class_sequence.store.loadData(class_sequence_data1);
		if (op == "add") {
			// 新增时，赋初始值
			blockAdddWindow.setTitle("新增附属周期");
			blockdForm.getForm().reset();
		} else if (op == "edit") {
			blockAdddWindow.setTitle("修改附属周期");
			blockdForm.getForm().reset();
			blockAdddWindow.show();
			var rec = southgrid.getSelectionModel().getSelected();
			blockdForm.getForm().loadRecord(rec);
			month_week_no.setValue(rec.data.weekNo);
			if (rec.data.weekNo == 0 || rec.data.weekNo == null) {
				month_week_day.store.loadData(week_day_data1);
			} else {
				month_week_day.store.loadData(week_day_data2);
			}
			month_week_day.setValue(rec.data.testDay);
			month_class_sequence.setValue(rec.data.classSequence);
			if (Ext.get("timeworkdInfo.weekNo").dom.value == 'null') {
				Ext.get("timeworkdInfo.weekNo").dom.value = "";
			}
			if (Ext.get("timeworkdInfo.testDay").dom.value == 'null') {
				Ext.get("timeworkdInfo.testDay").dom.value = "";
			}
			if (Ext.get("timeworkdInfo.classSequence").dom.value == 'null') {
				Ext.get("timeworkdInfo.classSequence").dom.value = "";
			}

		} else {
		}
		blockAdddWindow.show(Ext.get('getrole'));
	};

	// South中的Grid的button
	// South中的Grid的button 添加按钮
	var sgbtnAdd = new Ext.Button({
				text : '新增',
				iconCls : 'add',
				handler : function() {
					var rec = centergrid.getSelectionModel().getSelections();
					if (rec.length == 1) {
						Ext.Msg.confirm('提示信息', '为选中的记录添加附属周期?', function(
										button, text) {
									if (button == 'yes') {
										op = "add";
										showAdddWindow();
									}
								})
					} else {
						Ext.Msg.alert('提示信息', "请选择一行！");
						return false;
					}
				}
			});

	// South选择判断
	function CKSelectdone() {
		var rec = southgrid.getSelectionModel().getSelections();
		var recm = centergrid.getSelectionModel().getSelections();
		if (rec.length != 1) {
			Ext.Msg.alert('提示信息', "请选择一行！");
			return false;
		}
		if (recm.length != 1) {
			Ext.Msg.alert('提示信息', "请选中此子项的主记录！");
			return false;
		} else {
			op = "edit";
			showAdddWindow();
		}
	}

	// South中的Grid的button 修改按钮
	var sgbtnUdt = new Ext.Button({
				id : 'update',
				text : '修改',
				iconCls : 'update',
				handler : CKSelectdone
			});

	// South中的Grid的button 锁定按钮
	var sgbtnLok = new Ext.Button({
		id : 'locked',
		text : '锁定',
		iconCls : 'locked',
		handler : function() {
			if (southgrid.getSelectionModel().getSelections().length > 0) {
				Ext.Msg.confirm('提示信息', '确认 锁定 选中记录?', function(button, text) {
					if (button == 'yes') {
						var rec = southgrid.getSelectionModel().getSelections();
						var str = "";
						for (var i = 0; i < rec.length; i++) {
							str += rec[i].data.id + ",";
						}
						Ext.Ajax.request({
									waitMsg : '锁定中,请稍后...',
									url : 'timework/lockTimeworkd.action',
									params : {
										method : "lock",
										ids : str
									},
									success : function(response, options) {
										southds.reload();
										Ext.Msg.alert('提示信息', '锁定记录成功！');
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
	// South中的Grid的button 解锁按钮
	var sgbtnUlk = new Ext.Button({
		id : 'unlocked',
		text : '解锁',
		iconCls : 'unlocked',
		handler : function() {
			if (southgrid.getSelectionModel().getSelections().length > 0) {
				Ext.Msg.confirm('提示信息', '确认 解锁 选中记录?', function(button, text) {
					if (button == 'yes') {
						var rec = southgrid.getSelectionModel().getSelections();
						var str = "";
						for (var i = 0; i < rec.length; i++) {
							str += rec[i].data.id + ",";
						}
						Ext.Ajax.request({
									waitMsg : '锁定中,请稍后...',
									url : 'timework/unlockTimeworkd.action',
									params : {
										method : "lock",
										ids : str
									},
									success : function(response, options) {
										southds.reload();
										Ext.Msg.alert('提示信息', '解锁记录成功！');
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
	// South中的Grid的button 刷新按钮
	var sgbtnRsh = new Ext.Button({
				id : 'reflesh',
				text : '刷新',
				iconCls : 'reflesh',
				handler : function() {
					southds.reload();
				}
			});
	// South中的Grid的button 删除按钮
	var sgbtnDlt = new Ext.Button({
		id : 'delete',
		text : '删除',
		iconCls : 'delete',
		handler : function() {
			if (southgrid.getSelectionModel().getSelections().length > 0) {
				Ext.Msg.confirm('提示信息', '确认 删除 选中记录?', function(button, text) {
					if (button == 'yes') {
						var rec = southgrid.getSelectionModel().getSelections();
						var str = "";
						for (var i = 0; i < rec.length; i++) {
							str += rec[i].data.id + ",";
						}
						Ext.Ajax.request({
									waitMsg : '删除中,请稍后...',
									url : 'timework/deleteTimeworkd.action',
									params : {
										method : "lock",
										ids : str
									},
									success : function(response, options) {
										southds.reload();
										Ext.Msg.alert('提示信息', '删除记录成功！');
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

	// South中的数据
	var timeworkdlist = new Ext.data.Record.create([centersm, {
				name : 'classSequence'
			}, {
				name : 'cycleSequence'
			}, {
				name : 'id'
			}, {
				name : 'month'
			}, {
				name : 'status'
			}, {
				name : 'testDay'
			}, {
				name : 'weekNo'
			}, {
				name : 'workItemCode'
			}, {
				name : 'enterprisecode'
			}]);
	var southsm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			});

	var southds = new Ext.data.JsonStore({
				url : 'timework/getlistTimeworkd.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : timeworkdlist
			});

	// South中的Grid主体
	var southgrid = new Ext.grid.GridPanel({
				store : southds,
				columns : [southsm, new Ext.grid.RowNumberer(), {
							header : "状态",
							width : 30,
							sortable : true,
							renderer : function changeIt(val) {
								if (val == "C") {
									return "正常";
								} else if (val == "L") {
									return "锁定";
								} else if (val == "O") {
									return "注销";
								} else {
									return "状态异常";
								}
							},
							dataIndex : 'status'
						}, {
							header : "工作名称",
							width : 200,
							sortable : false,
							dataIndex : 'workItemCode'
						}, {
							header : "周期描述",
							width : 100,
							sortable : false,
							dataIndex : 'cycleSequence'
						}, {
							header : "开始时间",
							width : 60,
							sortable : true,
							dataIndex : 'enterprisecode',
							renderer : function format(val) {
								str = val.substr(0, 10);
								return str;
							}
						}],
				viewConfig : {
					forceFit : true
				},
				tbar : [sgbtnAdd, {
							xtype : "tbseparator"
						}, sgbtnUdt, {
							xtype : "tbseparator"
						}, sgbtnLok, {
							xtype : "tbseparator"
						}, sgbtnUlk, {
							xtype : "tbseparator"
						}, sgbtnRsh, {
							xtype : "tbseparator"
						}, sgbtnDlt],
				sm : southsm,
				frame : true,
				enableColumnHide : false,
				enableColumnMove : false,
				iconCls : 'icon-grid'
			});

	// Center中的Grid的button

	// Center中的Grid的button 添加按钮 窗体对象 表单对象

	// 工作编号
	var work_item_code = {
		id : "workItemCode",
		xtype : "textfield",
		fieldLabel : '工作项目编码',
		name : 'timeworkInfo.workItemCode',
		anchor : '90%',
		readOnly : true
	};

	// 工作名称
	var work_item_name = {
		id : "workItemName",
		xtype : "textfield",
		allowBlank : false,
		fieldLabel : '工作项目名称',
		name : 'timeworkInfo.workItemName',
		blankText : '工作项目名称',
		anchor : '97%',
		readOnly : false
	};

	// 周期
	var cycle_data = [["每日", "1"], ["每月", "2"], ["每周", "3"], ["每班", "4"],
			["偶数日", "5"], ["奇数日", "6"], ["隔日", "7"], ["双休日", "8"], ["隔月", "9"]];

	// 初始化函数
	function resetdata() {
		week_no.store.loadData([]);
		Ext.get("timeworkInfo.weekNo").dom.value = "";
		Ext.get("weekNo").dom.value = "";
		week_day.store.loadData([]);
		Ext.get("timeworkInfo.weekDay").dom.value = "";
		Ext.get("weekDay").dom.value = "";
		class_sequence.store.loadData([]);
		Ext.get("timeworkInfo.classSequence").dom.value = "";
		Ext.get("classSequence").dom.value = "";
		Ext.get("cycleNumber").dom.value = "";
		Ext.get("cycleNumber").dom.readOnly = true;
	};
	// 周期判定函数
	function cyclerule(index, scrollIntoView) {
		var getvalue = index.value;
		if (getvalue == 1) {
			resetdata();
			class_sequence.store.loadData(class_sequence_data);
		} else if (getvalue == 2) {
			resetdata();
			week_no.store.loadData(week_no_data);
			week_day.store.loadData(week_day_data1);
			class_sequence.store.loadData(class_sequence_data);
		} else if (getvalue == 3) {
			resetdata();
			week_day.store.loadData(week_day_data2);
			class_sequence.store.loadData(class_sequence_data);
		} else if (getvalue == 4) {
			resetdata();
		} else if (getvalue == 5) {
			resetdata();
			class_sequence.store.loadData(class_sequence_data);
		} else if (getvalue == 6) {
			resetdata();
			class_sequence.store.loadData(class_sequence_data);
		} else if (getvalue == 7) {
			resetdata();
			class_sequence.store.loadData(class_sequence_data);
			Ext.get("cycleNumber").dom.readOnly = false;
		} else if (getvalue == 8) {
			resetdata();
			class_sequence.store.loadData(class_sequence_data);
		} else if (getvalue == 9) {
			resetdata();
			week_no.store.loadData(week_no_data);
			week_day.store.loadData(week_day_data1);
			class_sequence.store.loadData(class_sequence_data);
			Ext.get("cycleNumber").dom.readOnly = false;
		} else {
			resetdata();
		}
	}
	// 周期加载判定函数
	function cycleruleload(index, scrollIntoView) {
		var getvalue = index.value;
		if (getvalue == 1) {
			class_sequence.store.loadData(class_sequence_data);
		} else if (getvalue == 2) {
			week_no.store.loadData(week_no_data);
			week_day.store.loadData(week_day_data1);
			class_sequence.store.loadData(class_sequence_data);
		} else if (getvalue == 3) {
			week_day.store.loadData(week_day_data2);
			class_sequence.store.loadData(class_sequence_data);
		} else if (getvalue == 4) {
		} else if (getvalue == 5) {
			class_sequence.store.loadData(class_sequence_data);
		} else if (getvalue == 6) {
			class_sequence.store.loadData(class_sequence_data);
		} else if (getvalue == 7) {
			class_sequence.store.loadData(class_sequence_data);
			Ext.get("cycleNumber").dom.readOnly = false;
		} else if (getvalue == 8) {
			class_sequence.store.loadData(class_sequence_data);
		} else if (getvalue == 9) {
			week_no.store.loadData(week_no_data);
			week_day.store.loadData(week_day_data1);
			class_sequence.store.loadData(class_sequence_data);
			Ext.get("cycleNumber").dom.readOnly = false;
		} else {
			resetdata();
		}
	}

	var work_range_type = {
		fieldLabel : "周期",
		store : new Ext.data.SimpleStore({
					fields : ['name', 'id'],
					data : cycle_data
				}),
		valueField : 'id',
		displayField : 'name',
		mode : 'local',
		forceSelection : true,
		hiddenName : 'timeworkInfo.workRangeType',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		name : 'workRangeType',
		id : "workRangeType",
		xtype : "combo",
		allowBlank : false,
		listeners : {
			select : cyclerule
		},
		emptyText : '请选择...',
		blankText : '请选择',
		readOnly : true
	};

	// 第几周
	var week_no = {
		id : "weekNo",
		xtype : "combo",
		name : 'weekNo',
		allowBlank : true,
		triggerAction : 'all',
		store : new Ext.data.SimpleStore({
					fields : ['name', 'id'],
					data : []
				}),
		hiddenName : 'timeworkInfo.weekNo',
		displayField : 'name',
		valueField : 'id',
		fieldLabel : "第几周",
		listeners : {
			select : function(index, scrollIntoView) {
				var getvalue = index.value;
				if (getvalue == 0) {
					week_day.store.loadData(week_day_data1);
				} else {
					Ext.get("weekDay").dom.value = "";
					week_day.store.loadData(week_day_data2);
				}
			}
		},
		mode : 'local',
		emptyText : '请选择...',
		blankText : '请选择',
		readOnly : true
	};

	// 日期
	var week_day = {
		id : "weekDay",
		xtype : "combo",
		name : 'weekDay',
		allowBlank : true,
		triggerAction : 'all',
		store : new Ext.data.SimpleStore({
					fields : ['name', 'id'],
					data : []
				}),
		hiddenName : 'timeworkInfo.weekDay',
		displayField : 'name',
		valueField : 'id',
		fieldLabel : "日期",
		mode : 'local',
		emptyText : '请选择...',
		blankText : '请选择',
		readOnly : true
	};

	// 班次
	var class_sequence = {
		id : "classSequence",
		xtype : "combo",
		name : 'classSequence',
		allowBlank : true,
		triggerAction : 'all',
		store : new Ext.data.SimpleStore({
					fields : ['name', 'id'],
					data : []
				}),
		hiddenName : 'timeworkInfo.classSequence',
		displayField : 'name',
		valueField : 'id',
		fieldLabel : "班次",
		mode : 'local',
		emptyText : '请选择...',
		blankText : '请选择',
		readOnly : true
	};

	// 周期数值
	var cycle_number = {
		id : "cycleNumber",
		name : "timeworkInfo.cycleNumber",
		xtype : "numberfield",
		fieldLabel : '周期数',
		// regex : /^\d$/,
		// regexText : "只能够输入数字",
		anchor : '93%',
		readOnly : true
	};

	// 是、否（公用）
	var yes_no_data = [["是", "Y"], ["否", "N"]];

	// 说明是否必填
	var ifexplain = {
		id : "ifexplain",
		xtype : "combo",
		name : 'ifexplain',
		allowBlank : true,
		triggerAction : 'all',
		store : new Ext.data.SimpleStore({
					fields : ['name', 'id'],
					data : yes_no_data
				}),
		hiddenName : 'timeworkInfo.ifexplain',
		displayField : 'name',
		valueField : 'id',
		value : 'N',
		fieldLabel : "说明是否必填",
		mode : 'local',
		emptyText : '请选择...',
		blankText : '请选择',
		readOnly : true
	};

	// 是否审批
	var ifcheck = {
		id : "ifcheck",
		xtype : "combo",
		name : 'ifcheck',
		allowBlank : true,
		triggerAction : 'all',
		store : new Ext.data.SimpleStore({
					fields : ['name', 'id'],
					data : yes_no_data
				}),
		hiddenName : 'timeworkInfo.ifcheck',
		displayField : 'name',
		valueField : 'id',
		value : 'N',
		fieldLabel : "是否审批",
		mode : 'local',
		emptyText : '请选择...',
		blankText : '请选择',
		readOnly : true
	};

	// 是否做事故预想
	var ifimage = {
		id : "ifimage",
		xtype : "combo",
		name : 'ifimage',
		allowBlank : true,
		triggerAction : 'all',
		store : new Ext.data.SimpleStore({
					fields : ['name', 'id'],
					data : yes_no_data
				}),
		hiddenName : 'timeworkInfo.ifimage',
		displayField : 'name',
		valueField : 'id',
		value : 'N',
		fieldLabel : "是否做事故预想",
		mode : 'local',
		emptyText : '请选择...',
		blankText : '请选择',
		readOnly : true
	};

	// 是否要试验
	var iftest = {
		id : "iftest",
		xtype : "combo",
		name : 'iftest',
		allowBlank : true,
		triggerAction : 'all',
		store : new Ext.data.SimpleStore({
					fields : ['name', 'id'],
					data : yes_no_data
				}),
		hiddenName : 'timeworkInfo.iftest',
		displayField : 'name',
		valueField : 'id',
		value : 'N',
		fieldLabel : "是否要试验",
		mode : 'local',
		emptyText : '请选择...',
		blankText : '请选择',
		readOnly : true
	};

	// 开始时间
	var start_time = {
		id : "startTime",
		xtype : "datefield",
		fieldLabel : '开始时间',
		name : 'timeworkInfo.startTime',
		allowBlank : false,
		blankText : '时间...',
		format : 'Y-m-d',
		value : new Date().format('yyyy-MM-dd'),
		anchor : '93%'
	};

	// 工作类型
	var work_type = {
		id : "workType",
		xtype : "combo",
		name : 'workType',
		allowBlank : false,
		triggerAction : 'all',
		store : new Ext.data.SimpleStore({
					fields : ['name', 'id'],
					data : search_data2
				}),
		hiddenName : 'timeworkInfo.workType',
		displayField : 'name',
		valueField : 'id',
		fieldLabel : "工作类型",
		mode : 'local',
		emptyText : '请选择...',
		blankText : '请选择',
		readOnly : true
	};

	var class_sequence_data = [];

	// 专业
	var machprof_code = {
		id : "machprofCode",
		xtype : "combo",
		name : 'machprofCode',
		allowBlank : false,
		triggerAction : 'all',
		store : new Ext.data.SimpleStore({
					fields : ['name', 'id'],
					data : search_data1
				}),
		hiddenName : 'timeworkInfo.machprofCode',
		displayField : 'name',
		valueField : 'id',
		fieldLabel : "专业",
		mode : 'local',
		emptyText : '请选择...',
		blankText : '请选择',
		readOnly : true,
		listeners : {
			select : function(index, scrollIntoView) {
				work_range_type.store.loadData(cycle_data);
				Ext.get("timeworkInfo.workRangeType").dom.value = "";
				Ext.get("workRangeType").dom.value = "";
				resetdata();

				// 班次
				var url = "timework/findShfitTimeBySpecial.action?spcode="
						+ index.value;
				var conn = Ext.lib.Ajax.getConnectionObject().conn;
				conn.open("POST", url, false);
				conn.send(null);
				class_sequence_data = eval('(' + conn.responseText + ')');
			}
		}
	};

	// 重要程度的数据
	var importantlvl_data = [["一般", "1"], ["重要", "2"]];

	// 重要程度
	var importantlvl = {
		id : "importantlvl",
		xtype : "combo",
		name : 'importantlvl',
		allowBlank : true,
		triggerAction : 'all',
		store : new Ext.data.SimpleStore({
					fields : ['name', 'id'],
					data : importantlvl_data
				}),
		hiddenName : 'timeworkInfo.importantlvl',
		displayField : 'name',
		valueField : 'id',
		value : '1',
		fieldLabel : "重要程度",
		mode : 'local',
		emptyText : '请选择...',
		blankText : '请选择',
		readOnly : true
	};

	// 是否要操作票
	var ifopticket = {
		fieldLabel : "是否要操作票",
		store : new Ext.data.SimpleStore({
					fields : ['name', 'id'],
					data : yes_no_data
				}),
		valueField : 'id',
		displayField : 'name',
		id : 'ifopticket',
		name : 'ifopticket',
		mode : 'local',
		forceSelection : true,
		hiddenName : 'timeworkInfo.ifopticket',
		value : 'N',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		xtype : "combo",
		allowBlank : true,
		listeners : {
			select : function(index, scrollIntoView) {
				var getvalue = index.value;
				if (getvalue == 'Y') {
					opticket_code.store.loadData(opticket_code_data);
				} else {
					opticket_code.store.loadData([]);
					Ext.get("timeworkInfo.opticketCode").dom.value = "";
					Ext.get("opticketCode").dom.value = "";
				}
			}
		},
		emptyText : '请选择...',
		blankText : '请选择',
		readOnly : true
	};

	// 任务类别
	var opticket_code_data = [["测试", "1"], ["测试测试", "2"]];
	var opticket_code = {
		id : "opticketCode",
		xtype : "combo",
		name : 'opticketCode',
		allowBlank : true,
		triggerAction : 'all',
		store : new Ext.data.SimpleStore({
					fields : ['name', 'id'],
					data : []
				}),
		hiddenName : 'timeworkInfo.opticketCode',
		displayField : 'name',
		valueField : 'id',
		fieldLabel : "任务类别",
		mode : 'local',
		emptyText : '请选择...',
		blankText : '请选择',
		readOnly : true,
		anchor : '97%'
	};

	// 试验说明
	var work_explain = {
		id : "workExplain",
		xtype : "textarea",
		fieldLabel : '试验说明',
		name : 'timeworkInfo.workExplain',
		allowBlank : true,
		blankText : '说明...',
		anchor : '96%'
	};

	// 周期描述
	var cycle_sequence = {
		id : "cycleSequence",
		xtype : "textfield",
		fieldLabel : '周期描述',
		name : 'timeworkInfo.cycleSequence',
		allowBlank : true,
		blankText : '描述...',
		anchor : '97%'
	};

	// 备注
	var memo = {
		id : "memo",
		xtype : "textarea",
		fieldLabel : '备注',
		name : 'timeworkInfo.memo',
		allowBlank : true,
		blankText : '备注...',
		anchor : '96%'
	};

	// Center中的Grid的button 添加按钮 窗体对象 表单对象
	var blockForm = new Ext.form.FormPanel({
				labelAlign : 'right',
				frame : true,
				items : [{
					border : false,
					layout : 'form',
					items : [{
								border : false,
								layout : 'form',
								name : 'timeworkInfo',
								columnWidth : 1,
								items : [work_item_name]
							}, {
								border : false,
								layout : 'column',
								items : [{
									columnWidth : .5,
									layout : 'form',
									border : false,
									items : [machprof_code, work_range_type,
											week_no, week_day, class_sequence,
											cycle_number]
								}, {
									columnWidth : .5,
									layout : 'form',
									border : false,
									items : [work_type, importantlvl,
											ifexplain,
											// ifcheck,iftest,
											ifimage, ifopticket, start_time]
								}]
							}, {
								border : false,
								layout : 'form',
								columnWidth : 1,
								items : [opticket_code]
							}, {
								border : false,
								layout : 'form',
								columnWidth : 1,
								items : [work_explain]
							}, {
								border : false,
								layout : 'form',
								columnWidth : 1,
								items : [memo]
							}]
				}]
			});

	// Center中的Grid的button 添加按钮 窗体对象
	var blockAddWindow;
	function showAddWindow() {
		if (!blockAddWindow) {
			blockAddWindow = new Ext.Window({
				// el : 'window_win',
				title : '',
				layout : 'fit',
				width : 600,
				height : 470,
				closable : true,
				border : false,
				resizable : false,
				closeAction : 'hide',
				modal : true,
				plain : true,
				// 面板中按钮的排列方式
				buttonAlign : 'center',
				items : [blockForm],
				buttons : [{
					text : '保存',
					handler : function() {
						var str = "";
						var i = 0;
						if (Ext.get("timeworkInfo.ifopticket").dom.value == 'Y'
								&& Ext.get("timeworkInfo.opticketCode").dom.value == '') {
							i++;
							str += i + "、请选择任务类别<br>";
						}

						var rangetype = Ext.get("timeworkInfo.workRangeType").dom.value;
						if (rangetype == 1
								&& Ext.get("classSequence").dom.value == "") {
							i++;
							str += i + "、班 次 未 选<br>";
						} else if (rangetype == 2) {
							if (Ext.get("classSequence").dom.value == "") {
								i++;
								str += i + "、班 次 未 选<br>";
							}
							if (Ext.get("weekDay").dom.value == "") {
								i++;
								str += i + "、日期输入有误<br>";
							}
						} else if (rangetype == 3) {
							if (Ext.get("classSequence").dom.value == "") {
								i++;
								str += i + "、班 次 未 选<br>";
							}
							if (Ext.get("weekDay").dom.value == "") {
								i++;
								str += i + "、请输入周几<br>";
							}
						} else if (rangetype == 5 || rangetype == 6
								|| rangetype == 8) {
							if (Ext.get("classSequence").dom.value == "") {
								i++;
								str += i + "、班 次 未 选<br>";
							}
						} else if (rangetype == 7) {
							if (Ext.get("classSequence").dom.value == "") {
								i++;
								str += i + "、班 次 未 选<br>";
							}
							if (Ext.get("cycleNumber").dom.value == "") {
								i++;
								str += i + "、周期数未填<br>";
							}
						} else if (rangetype == 9) {
							if (Ext.get("classSequence").dom.value == "") {
								i++;
								str += i + "、班 次 未 选<br>";
							}
							if (Ext.get("weekDay").dom.value == "") {
								i++;
								str += i + "、日期输入有误<br>";
							}
							if (Ext.get("cycleNumber").dom.value == "") {
								i++;
								str += i + "、周期数未填<br>";
							}
						} else {

						}
						if (str == "") {
							if (blockForm.getForm().isValid())
								if (op == "insert") {

									blockForm.getForm().submit({
										waitMsg : '保存中,请稍后...',
										url : "timework/addTimework.action",
										params : {
											method : "insert",
											classSequence : Ext
													.get("classSequence").dom.value
										},
										success : function(form, action) {
											blockForm.getForm().reset();
											blockAddWindow.hide();
											centerds.reload();
											southds.load({
														params : {
															workitemcode : ''
														}
													});
										},
										failure : function(form, action) {
											var o = eval('('
													+ action.response.responseText
													+ ')');
											Ext.MessageBox.alert('错误', o.eMsg);
										}
									});
								} else if (op == "edit") {
									blockForm.getForm().submit({
										waitMsg : '保存中,请稍后...',
										url : "timework/updateTimework.action",
										params : {
											method : "edit",
											id : centergrid.getSelectionModel()
													.getSelected().data.id,
											classSequence : Ext
													.get("classSequence").dom.value
										},
										success : function(form, action) {
											var o = eval('('
													+ action.response.responseText
													+ ')');

											blockForm.getForm().reset();
											blockAddWindow.hide();
											centerds.reload();
											southds.load({
												params : {
													workitemcode : centergrid
															.getSelectionModel()
															.getSelected().data.workItemCode,
													workitemname : o.workitemnameMsg,
													startime : o.startimeMsg
												}
											});
										},
										failure : function(form, action) {
											var o = eval('('
													+ action.response.responseText
													+ ')');
											Ext.MessageBox.alert('错误', o.eMsg);
										}
									});
								}
						} else {
							Ext.MessageBox.alert('错误', str);
						}
					}
				}, {
					text : '取消',
					handler : function() {
						blockForm.getForm().reset();
						blockAddWindow.hide();
					}
				}]
			});
		}
		if (op == "insert") {
			// 新增时，赋初始值
			blockAddWindow.setTitle("新增定期工作");
			blockForm.getForm().reset();
		} else if (op == "edit") {
			// 班次
			var url = "timework/findShfitTimeBySpecial.action?spcode="
					+ centergrid.getSelectionModel().getSelected().data.machprofCode;
			var conn = Ext.lib.Ajax.getConnectionObject().conn;
			conn.open("POST", url, false);
			conn.send(null);
			class_sequence_data = eval('(' + conn.responseText + ')');
			class_sequence.store.loadData(class_sequence_data);
			blockAddWindow.setTitle("修改定期工作");
			blockAddWindow.show();
			opticket_code.store.loadData(opticket_code_data);
			var rec = centergrid.getSelectionModel().getSelected();
			cyclerule({
						value : rec.data.workRangeType
					});
			blockForm.getForm().loadRecord(rec);
			if (Ext.get("timeworkInfo.weekNo").dom.value != 'null') {
				week_day.store.loadData(week_day_data2);
			}
			blockForm.getForm().loadRecord(rec);
			if (Ext.get("timeworkInfo.weekNo").dom.value == 'null') {
				Ext.get("timeworkInfo.weekNo").dom.value = "";
			} else {
				week_day.store.loadData(week_day_data2);
			}
			if (Ext.get("timeworkInfo.weekDay").dom.value == 'null') {
				Ext.get("timeworkInfo.weekDay").dom.value = "";
			}
			if (Ext.get("timeworkInfo.classSequence").dom.value == 'null') {
				Ext.get("timeworkInfo.classSequence").dom.value = "";
			}
			if (Ext.get("timeworkInfo.ifopticket").dom.value == "Y") {
				Ext.get("timeworkInfo.opticketCode").dom.value = rec.data.opticketCode;
			} else {
				opticket_code.store.loadData([]);
				Ext.get("timeworkInfo.opticketCode").dom.value = "";
				Ext.get("opticketCode").dom.value = "";
			}
			Ext.get("startTime").dom.value = rec.data.startTime.substr(0, 10);
		} else {
		}
		blockAddWindow.show(Ext.get('getrole'));
	};

	// Center中的Grid的专业下拉框
	var center_machprof_code = {
		id : "center_machprof_code",
		xtype : "combo",
		name : 'center_machprof_code',
		allowBlank : true,
		triggerAction : 'all',
		store : new Ext.data.SimpleStore({
					fields : ['name', 'id'],
					data : search_data1
				}),
		hiddenName : 'CenterMachprofCode',
		displayField : 'name',
		valueField : 'id',
		mode : 'local',
		emptyText : '专业...',
		blankText : '专业',
		readOnly : true
	};

	// Center中的Grid的工作类型下拉框
	var center_work_type = {
		id : "center_work_type",
		xtype : "combo",
		name : 'center_work_type',
		allowBlank : true,
		triggerAction : 'all',
		store : new Ext.data.SimpleStore({
					fields : ['name', 'id'],
					data : search_data2
				}),
		hiddenName : 'CenterWorkType',
		displayField : 'name',
		valueField : 'id',
		mode : 'local',
		emptyText : '工作类型...',
		blankText : '工作类型',
		readOnly : true
	};

	// Center中的Grid的button 查询按钮
	var ctbtnQuery = new Ext.Button({
				text : '查询',
				iconCls : 'query',
				handler : function() {
					centerds.load({
								params : {
									start : 0,
									limit : 10
								}
							});
				}
			});

	// Center中的Grid的button 添加按钮
	var ctbtnAdd = new Ext.Button({
				text : '新增',
				iconCls : 'add',
				handler : function() {
					op = "insert";
					showAddWindow();
				}
			});

	function CKSelectone() {
		var rec = centergrid.getSelectionModel().getSelections();
		if (rec.length != 1) {
			Ext.Msg.alert('提示信息', "请选择一行！");
			return false;
		} else {
			op = "edit";
			showAddWindow();
		}
	}

	// Center中的Grid的button 修改按钮
	var ctbtnUdt = new Ext.Button({
				id : 'update',
				text : '修改',
				iconCls : 'update',
				handler : CKSelectone
			});

	// Center中的Grid的button 锁定按钮
	var ctbtnLok = new Ext.Button({
		id : 'locked',
		text : '锁定',
		iconCls : 'locked',
		handler : function() {
			if (centergrid.getSelectionModel().getSelections().length > 0) {
				Ext.Msg.confirm('提示信息', '确认 锁定 选中记录?', function(button, text) {
							if (button == 'yes') {
								var rec = centergrid.getSelectionModel()
										.getSelections();
								var str = "";
								for (var i = 0; i < rec.length; i++) {
									str += rec[i].data.id + ",";
								}
								Ext.Ajax.request({
											waitMsg : '锁定中,请稍后...',
											url : 'timework/lockTimework.action',
											params : {
												method : "lock",
												ids : str
											},
											success : function(response,
													options) {
												centerds.reload();
												southds.reload();
												Ext.Msg
														.alert('提示信息',
																'锁定记录成功！');
											},
											failure : function() {
												Ext.Msg.alert('提示信息',
														'服务器错误,请稍候重试!')
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
	// Center中的Grid的button 解锁按钮
	var ctbtnUlk = new Ext.Button({
		id : 'unlocked',
		text : '解锁',
		iconCls : 'unlocked',
		handler : function() {
			if (centergrid.getSelectionModel().getSelections().length > 0) {
				Ext.Msg.confirm('提示信息', '确认 解锁 选中记录?', function(button, text) {
							if (button == 'yes') {
								var rec = centergrid.getSelectionModel()
										.getSelections();
								var str = "";
								for (var i = 0; i < rec.length; i++) {
									str += rec[i].data.id + ",";
								}
								Ext.Ajax.request({
											waitMsg : '解锁中,请稍后...',
											url : 'timework/unlockTimework.action',
											params : {
												method : "unlock",
												ids : str
											},
											success : function(response,
													options) {
												centerds.reload();
												southds.reload();
												Ext.Msg
														.alert('提示信息',
																'解锁记录成功！');
											},
											failure : function() {
												Ext.Msg.alert('提示信息',
														'服务器错误,请稍候重试!')
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
	// Center中的Grid的button 刷新按钮
	var ctbtnRsh = new Ext.Button({
				id : 'reflesh',
				text : '刷新',
				iconCls : 'reflesh',
				handler : function() {
					centerds.load({
								params : {
									start : 0,
									limit : 10
								}
							});
					southds.load({
								params : {
									workitemcode : ''
								}
							});
				}
			});
	// Center中的Grid的button 删除按钮
	var ctbtnDlt = new Ext.Button({
		id : 'delete',
		text : '删除',
		iconCls : 'delete',
		handler : function() {
			if (centergrid.getSelectionModel().getSelections().length > 0) {
				Ext.Msg.confirm('提示信息', '确认 删除 选中记录?', function(button, text) {
							if (button == 'yes') {
								var rec = centergrid.getSelectionModel()
										.getSelections();
								var str = "";
								for (var i = 0; i < rec.length; i++) {
									str += rec[i].data.id + ",";
								}
								Ext.Ajax.request({
											waitMsg : '删除中,请稍后...',
											url : 'timework/deleteTimework.action',
											params : {
												method : "delete",
												ids : str
											},
											success : function(response,
													options) {
												centerds.reload();
												southds.reload();
												Ext.Msg
														.alert('提示信息',
																'删除记录成功！');
											},
											failure : function() {
												Ext.Msg.alert('提示信息',
														'服务器错误,请稍候重试!')
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

	// Center中的数据
	var timeworklist = new Ext.data.Record.create([centersm, {
				name : 'classSequence'
			}, {
				name : 'cycle'
			}, {
				name : 'cycleNumber'
			}, {
				name : 'cycleSequence'
			}, {
				name : 'delaytype'
			}, {
				name : 'id'
			}, {
				name : 'ifcheck'
			}, {
				name : 'ifexplain'
			}, {
				name : 'ifimage'
			}, {
				name : 'ifopticket'
			}, {
				name : 'iftest'
			}, {
				name : 'importantlvl'
			}, {
				name : 'machprofCode'
			}, {
				name : 'memo'
			}, {
				name : 'operator'
			}, {
				name : 'opticketCode'
			}, {
				name : 'protector'
			}, {
				name : 'startTime',
				format : 'Y-m-d'
			}, {
				name : 'status'
			}, {
				name : 'weekDay'
			}, {
				name : 'weekNo'
			}, {
				name : 'workExplain'
			}, {
				name : 'workItemCode'
			}, {
				name : 'workItemName'
			}, {
				name : 'workRangeType'
			}, {
				name : 'workType'
			}]);
	var centersm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			});

	var centerds = new Ext.data.JsonStore({
				url : 'timework/getlistTimework.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : timeworklist
			});
	centerds.load({
				params : {
					start : 0,
					limit : 10
				}
			});
	centerds.on('beforeload', function() {
		Ext.apply(this.baseParams, {
					CenterMachprofCode : Ext.get("CenterMachprofCode").dom.value,
					CenterWorkType : Ext.get("CenterWorkType").dom.value
				});
	});

	// Center中的Grid主体
	var centergrid = new Ext.grid.GridPanel({
				store : centerds,
				columns : [centersm, new Ext.grid.RowNumberer(), {
							header : "状态",
							width : 30,
							align : "center",
							sortable : true,
							renderer : function changeIt(val) {
								if (val == "C") {
									return "正常";
								} else if (val == "L") {
									return "锁定";
								} else if (val == "O") {
									return "注销";
								} else {
									return "状态异常";
								}
							},
							dataIndex : 'status'
						}, {
							header : "工作名称",
							width : 200,
							sortable : false,
							dataIndex : 'workItemName'
						}, {
							header : "周期描述",
							width : 100,
							sortable : false,
							dataIndex : 'cycleSequence'
						}, {
							header : "开始时间",
							width : 60,
							align : "center",
							sortable : true,
							renderer : function format(val) {
								str = val.substr(0, 10);
								return str;
							},
							dataIndex : 'startTime'
						}],
				viewConfig : {
					forceFit : true
				},
				tbar : [center_machprof_code, {
							xtype : "tbseparator"
						}, center_work_type, {
							xtype : "tbseparator"
						}, ctbtnQuery, {
							xtype : "tbseparator"
						}, ctbtnAdd, {
							xtype : "tbseparator"
						}, ctbtnUdt, {
							xtype : "tbseparator"
						}, ctbtnLok, {
							xtype : "tbseparator"
						}, ctbtnUlk, {
							xtype : "tbseparator"
						}, ctbtnRsh, {
							xtype : "tbseparator"
						}, ctbtnDlt],
				sm : centersm,
				frame : true,
				bbar : new Ext.PagingToolbar({
							pageSize : 10,
							store : centerds,
							displayInfo : true,
							displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
							emptyMsg : "没有记录"
						}),
				enableColumnHide : false,
				enableColumnMove : false,
				iconCls : 'icon-grid'
			});

	// Center 的事件
	centergrid.on('rowclick', function(grid, rowIndex, e) {
		southds.load({
			params : {
				workitemcode : centergrid.getStore().getAt(rowIndex).data.workItemCode,
				workitemname : centergrid.getStore().getAt(rowIndex).data.workItemName,
				startime : centergrid.getStore().getAt(rowIndex).data.startTime
			}
		});
	})

	centergrid.on('rowdblclick', function(grid, rowIndex, e) {
		southds.load({
			params : {
				workitemcode : centergrid.getStore().getAt(rowIndex).data.workItemCode,
				workitemname : centergrid.getStore().getAt(rowIndex).data.workItemName,
				startime : centergrid.getStore().getAt(rowIndex).data.startTime
			}
		});
		CKSelectone();
	})

	// 设定布局器及面板
	var layout = new Ext.Viewport({
				layout : "border",
				border : false,
				items : [{
							title : "定期工作维护",
							region : 'center',
							layout : 'fit',
							border : false,
							margins : '0 0 0 0',
							split : true,
							collapsible : false,
							items : [centergrid]

						}, {
							title : "附属子集",
							region : "south",
							layout : 'fit',
							border : false,
							margins : '0 0 0 0',
							height : 170,
							split : true,
							collapsible : true,
							// 注入表格
							items : [southgrid]
						}]
			});
});