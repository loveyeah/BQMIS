Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	Ext.QuickTips.init();
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
	};

	var nowdate = new Date().format('yyyy-MM-dd');

	// 操作人
	var operatorname = new Ext.form.ComboBox({
		id : "operatorname",
		xtype : "combo",
		fieldLabel : '操作人',
		name : 'operatorname',
		hiddenName : 'operator',
		blankText : '操作人...',
		anchor : '93%',
		store : [],
		allowBlank : false,
		readOnly : true,
		onTriggerClick : function() {
			var emp = window
					.showModalDialog("../../../../comm/jsp/validate.jsp", "",
							"dialogHeight:180px;dialogWidth:350px;status:no;scroll:yes;help:no");

			if (emp != null) {
				document.getElementById("operatorname").value = emp.workerName;
				document.getElementById("operator").value = emp.workerCode;
			}
		}
	});

	// 监护人
	var protectorname = {
		id : "protectorname",
		xtype : "combo",
		fieldLabel : '监护人',
		name : 'protectorname',
		hiddenName : 'protector',
		blankText : '监护人...',
		anchor : '93%',
		triggerAction : 'all',
		mode : 'local',
		store : [],
		allowBlank : true,
		readOnly : true,
		onTriggerClick : function() {
			var emp = window
					.showModalDialog("../../../../comm/jsp/validate.jsp", "",
							"dialogHeight:180px;dialogWidth:350px;status:no;scroll:yes;help:no");

			if (emp != null) {
				document.getElementById("protectorname").value = emp.workerName;
				document.getElementById("protector").value = emp.workerCode;
			}
		}
	};

	// 密码
	var password1 = {
		id : "password1",
		name : "password1",
		xtype : "textfield",
		fieldLabel : '操作人密码',
		inputType : 'password',
		allowBlank : false,
		anchor : '93%'
	};

	// 密码
	var password2 = {
		id : "password2",
		name : "password2",
		xtype : "textfield",
		fieldLabel : '监护人密码',
		inputType : 'password',
		allowBlank : false,
		anchor : '93%'
	};

	// 备注
	var memo = {
		id : "memo",
		xtype : "textarea",
		fieldLabel : '备注',
		name : 'memo',
		allowBlank : true,
		blankText : '备注...',
		anchor : '96%'
	};

	// 试验说明
	var workExplain = {
		id : "workExplain",
		xtype : "textarea",
		fieldLabel : '试验说明',
		name : 'workExplain',
		allowBlank : true,
		blankText : '试验说明...',
		anchor : '96%'
	};

	// 事故预想号
	var imagecode = {
		id : "imagecode",
		xtype : "textfield",
		fieldLabel : '事故预想编号',
		name : 'imagecode',
		allowBlank : true,
		blankText : '事故预想编号...',
		anchor : '93%'
	};

	// 工作结果
	var search_data2 = [["正常", "1"], ["不正常", "2"], ["因故未作", "3"], ["取消", "4"]];

	// 工作结果下拉框
	var rungrid_workresult = {
		fieldLabel : '工作结果',
		id : "rungrid_workresult",
		xtype : "combo",
		name : 'rungrid_workresult',
		allowBlank : true,
		triggerAction : 'all',
		store : new Ext.data.SimpleStore({
					fields : ['name', 'id'],
					data : search_data2
				}),
		hiddenName : 'RungridWorkresult',
		displayField : 'name',
		valueField : 'id',
		mode : 'local',
		value : '1',
		emptyText : '工作结果...',
		blankText : '工作结果',
		readOnly : true,
		width : 100
	};

	// 签名的form
	var blockdForm = new Ext.form.FormPanel({
				labelAlign : 'right',
				frame : true,
				items : [{
							border : false,
							layout : 'form',
							items : [{
										border : false,
										layout : 'column',
										items : [{
													columnWidth : .5,
													layout : 'form',
													border : false,
													items : [operatorname]
												}, {
													columnWidth : .5,
													layout : 'form',
													border : false,
													items : [protectorname]
												}]
									}, {
										border : false,
										layout : 'form',
										columnWidth : 1,
										items : [memo]
									}, {
										border : false,
										layout : 'form',
										columnWidth : 1,
										items : [workExplain]
									}, {
										border : false,
										layout : 'column',
										items : [{
													columnWidth : .5,
													layout : 'form',
													border : false,
													items : [rungrid_workresult]
												}, {
													columnWidth : .5,
													layout : 'form',
													border : false,
													items : [imagecode]
												}]
									}]
						}]
			});

	// 执行窗体对象
	var blockAdddWindow;
	function showAdddWindow() {
		if (!blockAdddWindow) {
			blockAdddWindow = new Ext.Window({
				title : '',
				layout : 'fit',
				width : 480,
				height : 270,
				modal : true,
				closable : true,
				border : false,
				resizable : false,
				closeAction : 'hide',
				plain : true,
				// 面板中按钮的排列方式
				buttonAlign : 'center',
				items : [blockdForm],
				buttons : [{
					text : '保存',
					handler : function() {
						if (blockdForm.getForm().isValid()) {
							if (re == "ok" || event.keyCode == 13) {
								var rec = rungrid.getSelectionModel()
										.getSelected();
								if (document.getElementById("operator").value == null
										|| document.getElementById("operator").value == "") {
									document.getElementById("operator").value = document
											.getElementById("operatorname").value;
								}
								if (document.getElementById("protector").value == null
										|| document.getElementById("protector").value == "") {
									document.getElementById("protector").value = document
											.getElementById("protectorname").value;
								}
								if (rec.get("timeworkjInfo.ifExplain") == "Y"
										&& document
												.getElementById("workExplain").value == "") {
									Ext.Msg.alert('提示信息', '实验说明不可为空!');
									return false;
								} else if (rec.get("timeworkjInfo.ifimage") == "Y"
										&& document.getElementById("imagecode").value == "") {
									Ext.Msg.alert('提示信息', '必须填写事故预想号!');
									return false;
								} else {
									blockdForm.getForm().submit({
										waitMsg : '保存中,请稍后...',
										url : "timework/operateTimeworkj.action",
										params : {
											id : rec.get("timeworkjInfo.id")
										},
										success : function(form, action) {
											var o = eval('('
													+ action.response.responseText
													+ ')');
											Ext.MessageBox.alert('成功',
													'操 作 成 功 !');
											blockdForm.getForm().reset();
											blockAdddWindow.hide();
											rungrids.reload();
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
						}
					}
				}, {
					text : '取消',
					handler : function() {
						blockdForm.getForm().reset();
						blockAdddWindow.hide();
						rungrids.reload();
					}
				}]
			});
		}
		blockAdddWindow.setTitle("签名");
		blockAdddWindow.show(Ext.get('getrole'));
		var rec = rungrid.getSelectionModel().getSelected();
		// blockdForm.getForm().loadRecord(rec);
		if (rec.get("timeworkjInfo.memo") == null) {
			Ext.get("memo").dom.value = "";
		} else {
			Ext.get("memo").dom.value = rec.get("timeworkjInfo.memo");
		}
		if (rec.get("timeworkjInfo.workExplain") == null) {
			Ext.get("workExplain").dom.value = "";
		} else {
			Ext.get("workExplain").dom.value = rec
					.get("timeworkjInfo.workExplain");
		}
		document.getElementById("operatorname").onchange = function() {
			document.getElementById("operator").value = document
					.getElementById("operatorname").value;
		};
		document.getElementById("protectorname").onchange = function() {
			document.getElementById("protector").value = document
					.getElementById("protectorname").value;
		};
	};

	var search_data3 = [["直接执行", "N"], ["审批执行", "Y"], ["全部", ""]];

	// 工作类型
	var url = "timework/uselistTimeworktype.action";
	var conn = Ext.lib.Ajax.getConnectionObject().conn;
	conn.open("POST", url, false);
	conn.send(null);
	var search_data1
	if (conn.responseText != "") {
		search_data1 = eval('([' + conn.responseText
				// +
				// ',[\"全部定期工作\",\"\"],[\"延期定期工作\",\"delay\"],[\"临时下发工作\",\"temp\"],[\"已结束定期工作\",\"ed\"]])');
				+ ',[\"全部定期工作\",\"\"],[\"临时下发工作\",\"temp\"]])');
	} else {
		// search_data1 =
		// eval('([[\"全部定期工作\",\"\"],[\"延期定期工作\",\"delay\"],[\"临时下发工作\",\"temp\"],[\"已结束定期工作\",\"ed\"]])');
		search_data1 = eval('([[\"全部定期工作\",\"\"],[\"临时下发工作\",\"temp\"]])');
	}

	// 查询时间
	var start_time = {
		id : "startdt",
		xtype : "datefield",
		fieldLabel : '从',
		name : 'startdt',
		allowBlank : true,
		blankText : '时间...',
		format : 'Y-m-d',
		value : nowdate,
		width : 85
	};

	var end_time = {
		id : "enddt",
		xtype : "datefield",
		fieldLabel : '到',
		name : 'enddt',
		allowBlank : true,
		blankText : '时间...',
		format : 'Y-m-d',
		value : nowdate,
		width : 85
	};

	// rungrid中的Grid的工作类型下拉框
	var rungrid_work_type = new Ext.form.ComboBox({
				id : "rungrid_work_type",
				xtype : "combo",
				name : 'rungrid_work_type',
				allowBlank : true,
				triggerAction : 'all',
				store : new Ext.data.SimpleStore({
							fields : ['name', 'id'],
							data : search_data1
						}),
				hiddenName : 'RungridWorkType',
				displayField : 'name',
				valueField : 'id',
				mode : 'local',
				value : "",
				emptyText : '工作类型...',
				blankText : '工作类型',
				readOnly : true
			});

	// rungrid中的Grid的执行类别下拉框
	var rungrid_ifcheck = {
		id : "rungrid_ifcheck",
		xtype : "combo",
		name : 'rungrid_ifcheck',
		allowBlank : true,
		triggerAction : 'all',
		store : new Ext.data.SimpleStore({
					fields : ['name', 'id'],
					data : search_data3
				}),
		hiddenName : 'RungridIfChk',
		displayField : 'name',
		valueField : 'id',
		mode : 'local',
		value : "",
		emptyText : '...',
		blankText : '',
		readOnly : true
	};

	// rungrid中的查询按钮
	var rungridqbtn = new Ext.Button({
		id : 'query',
		text : '查询',
		iconCls : 'query',
		handler : function() {
			rungrids.load({
						params : {
							querystimeDate : Ext.get("startdt").dom.value,
							queryetimeDate : Ext.get("enddt").dom.value,
							queryWorkType : Ext.get("RungridWorkType").dom.value,
							// queryRunType : Ext.get("RungridIfChk").dom.value,
							start : 0,
							limit : 18
						}
					});
		}
	});

	// rungrid中的按钮
	var rungridbtn = new Ext.Button({
		id : 'reflesh',
		text : '刷新',
		iconCls : 'reflesh',
		handler : function() {
			rungrids.load({
						params : {
							querystimeDate : Ext.get("startdt").dom.value,
							queryetimeDate : Ext.get("enddt").dom.value,
							queryWorkType : Ext.get("RungridWorkType").dom.value,
							start : 0,
							limit : 18
						}
					});
		}
	});

	function CKSelectone() {
		var rec = rungrid.getSelectionModel().getSelections();
		if (rec.length != 1) {
			Ext.Msg.alert('提示信息', "请选择一条记录！");
			return false;
		} else {
			return true;
		}
	}

	// 上报按钮
	var rungridhandup = new Ext.Button({
				id : 'handup',
				text : '上报',
				iconCls : 'upcommit',
				handler : function() {
					if (CKSelectone()) {
						var rec = rungrid.getSelectionModel().getSelected();
						if (rec.get("timeworkjInfo.ifcheck") == "N") {
							Ext.Msg.alert('提示信息', "本工作无需上报");
							return false;
						} else if (rec.get("timeworkjInfo.status") != "1") {
							Ext.Msg.alert('提示信息', "已经上报");
							return false;
						} else {
							Ext.Ajax.request({
										waitMsg : '上报中,请稍后...',
										url : 'timework/handupTimeworkj.action',
										params : {
											method : "lock",
											id : rungrid.getSelectionModel()
													.getSelected()
													.get("timeworkjInfo.id")
										},
										success : function(response, options) {
											rungrids.reload();
											Ext.Msg.alert('提示信息', '上报成功！');
										},
										failure : function() {
											Ext.Msg.alert('提示信息',
													'服务器错误,请稍候重试!')
										}
									});
						}
					}
				}
			});

	// 执行登记
	var rungridop = new Ext.Button({
				id : 'handup',
				text : '执行登记',
				iconCls : 'write',
				handler : function() {
					if (CKSelectone()) {
						var rec = rungrid.getSelectionModel().getSelected();
						if (rec.get("timeworkjInfo.status") == "0"
								|| rec.get("timeworkjInfo.status") == "5") {
							blockdForm.getForm().reset();
							re = "ok";
							showAdddWindow();
						} else {
							Ext.Msg.alert('提示信息', "不符合执行规程!");
							rungrids.reload();
							return false;
						}
					}
				}
			});

	// 临时下发定期工作

	// 工作名称
	var work_item_nameTemp = {
		id : "work_item_nameTemp",
		xtype : "textfield",
		allowBlank : false,
		fieldLabel : '工作项目名称',
		name : 'timeworkInfoTemp.workItemName',
		blankText : '工作项目名称',
		anchor : '97%',
		readOnly : false
	};

	// 专业
	var url = "timework/useprolist.action";
	var conn = Ext.lib.Ajax.getConnectionObject().conn;
	conn.open("POST", url, false);
	conn.send(null);
	var search_data4 = eval('(' + conn.responseText + ')');

	var class_sequence_dataTemp = [];

	// 专业
	var machprof_codeTemp = {
		id : "machprofCodeTemp",
		xtype : "combo",
		name : 'machprofCodeTemp',
		allowBlank : false,
		triggerAction : 'all',
		store : new Ext.data.SimpleStore({
					fields : ['name', 'id'],
					data : search_data4
				}),
		hiddenName : 'timeworkInfoTemp.machprofCode',
		displayField : 'name',
		valueField : 'id',
		fieldLabel : "专业",
		mode : 'local',
		emptyText : '请选择...',
		blankText : '请选择',
		readOnly : true,
		listeners : {
			select : function(index, scrollIntoView) {
				// 班次
				var url = "timework/findShfitTimeBySpecial.action?spcode="
						+ index.value;
				var conn = Ext.lib.Ajax.getConnectionObject().conn;
				conn.open("POST", url, false);
				conn.send(null);
				class_sequence_dataTemp = eval('(' + conn.responseText + ')');
				class_sequenceTemp.store.loadData(class_sequence_dataTemp);
			}
		}
	};

	// 班次
	var class_sequenceTemp = {
		id : "classSequenceTemp",
		xtype : "combo",
		name : 'classSequenceTemp',
		allowBlank : true,
		triggerAction : 'all',
		store : new Ext.data.SimpleStore({
					fields : ['name', 'id'],
					data : []
				}),
		hiddenName : 'timeworkInfoTemp.classSequence',
		displayField : 'name',
		valueField : 'id',
		fieldLabel : "班次",
		mode : 'local',
		emptyText : '请选择...',
		blankText : '请选择',
		readOnly : true
	};

	// 是、否（公用）
	var yes_no_data = [["是", "Y"], ["否", "N"]];

	// 说明是否必填
	var ifexplainTemp = {
		id : "ifexplainTemp",
		xtype : "combo",
		name : 'ifexplainTemp',
		allowBlank : true,
		triggerAction : 'all',
		store : new Ext.data.SimpleStore({
					fields : ['name', 'id'],
					data : yes_no_data
				}),
		hiddenName : 'timeworkInfoTemp.ifExplain',
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
	var ifcheckTemp = {
		id : "ifcheckTemp",
		xtype : "combo",
		name : 'ifcheckTemp',
		allowBlank : true,
		triggerAction : 'all',
		store : new Ext.data.SimpleStore({
					fields : ['name', 'id'],
					data : yes_no_data
				}),
		hiddenName : 'timeworkInfoTemp.ifcheck',
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
	var ifimageTemp = {
		id : "ifimageTemp",
		xtype : "combo",
		name : 'ifimageTemp',
		allowBlank : true,
		triggerAction : 'all',
		store : new Ext.data.SimpleStore({
					fields : ['name', 'id'],
					data : yes_no_data
				}),
		hiddenName : 'timeworkInfoTemp.ifimage',
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
	var iftestTemp = {
		id : "iftestTemp",
		xtype : "combo",
		name : 'iftestTemp',
		allowBlank : true,
		triggerAction : 'all',
		store : new Ext.data.SimpleStore({
					fields : ['name', 'id'],
					data : yes_no_data
				}),
		hiddenName : 'timeworkInfoTemp.iftest',
		displayField : 'name',
		valueField : 'id',
		value : 'N',
		fieldLabel : "是否要试验",
		mode : 'local',
		emptyText : '请选择...',
		blankText : '请选择',
		readOnly : true
	};

	// 工作类型
	var url = "timework/uselistTimeworktype.action";
	var conn = Ext.lib.Ajax.getConnectionObject().conn;
	conn.open("POST", url, false);
	conn.send(null);
	var search_data5 = eval('([' + conn.responseText + '])');

	// 工作类型
	var work_typeTemp = {
		id : "workTypeTemp",
		xtype : "combo",
		name : 'workTypeTemp',
		allowBlank : false,
		triggerAction : 'all',
		store : new Ext.data.SimpleStore({
					fields : ['name', 'id'],
					data : search_data5
				}),
		hiddenName : 'timeworkInfoTemp.workType',
		displayField : 'name',
		valueField : 'id',
		fieldLabel : "工作类型",
		mode : 'local',
		emptyText : '请选择...',
		blankText : '请选择',
		readOnly : true
	};

	// 重要程度的数据
	var importantlvl_dataTemp = [["一般", "1"], ["重要", "2"]];

	// 重要程度
	var importantlvlTemp = {
		id : "importantlvlTemp",
		xtype : "combo",
		name : 'importantlvlTemp',
		allowBlank : true,
		triggerAction : 'all',
		store : new Ext.data.SimpleStore({
					fields : ['name', 'id'],
					data : importantlvl_dataTemp
				}),
		hiddenName : 'timeworkInfoTemp.importantlvl',
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
	var ifopticketTemp = {
		fieldLabel : "是否要操作票",
		store : new Ext.data.SimpleStore({
					fields : ['name', 'id'],
					data : yes_no_data
				}),
		valueField : 'id',
		displayField : 'name',
		id : 'ifopticketTemp',
		name : 'ifopticketTemp',
		mode : 'local',
		forceSelection : true,
		hiddenName : 'timeworkInfoTemp.ifopticket',
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
					opticket_codeTemp.store.loadData(opticket_code_dataTemp);
				} else {
					opticket_codeTemp.store.loadData([]);
					Ext.get("timeworkInfoTemp.opticketCode").dom.value = "";
					Ext.get("opticketCodeTemp").dom.value = "";
				}
			}
		},
		emptyText : '请选择...',
		blankText : '请选择',
		readOnly : true
	};

	// 任务类别
	var opticket_code_dataTemp = [["测试", "1"], ["测试测试", "2"]];
	var opticket_codeTemp = {
		id : "opticketCodeTemp",
		xtype : "combo",
		name : 'opticketCodeTemp',
		allowBlank : true,
		triggerAction : 'all',
		store : new Ext.data.SimpleStore({
					fields : ['name', 'id'],
					data : []
				}),
		hiddenName : 'timeworkInfoTemp.opticketCode',
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
	var work_explainTemp = {
		id : "workExplainTemp",
		xtype : "textarea",
		fieldLabel : '试验说明',
		name : 'timeworkInfoTemp.workExplain',
		allowBlank : true,
		blankText : '说明...',
		anchor : '96%'
	};

	// 备注
	var memoTemp = {
		id : "memoTemp",
		xtype : "textarea",
		fieldLabel : '备注',
		name : 'timeworkInfoTemp.memo',
		allowBlank : true,
		blankText : '备注...',
		anchor : '96%'
	};

	// Center中的Grid的button 添加按钮 窗体对象 表单对象
	var blockTempForm = new Ext.form.FormPanel({
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
						items : [work_item_nameTemp]
					}, {
						border : false,
						layout : 'column',
						items : [{
							columnWidth : .5,
							layout : 'form',
							border : false,
							items : [work_typeTemp, machprof_codeTemp,
									class_sequenceTemp]
						}, {
							columnWidth : .5,
							layout : 'form',
							border : false,
							items : [ifexplainTemp, ifimageTemp, ifopticketTemp]
						}]
					}, {
						border : false,
						layout : 'form',
						columnWidth : 1,
						items : [opticket_codeTemp]
					}, {
						border : false,
						layout : 'form',
						columnWidth : 1,
						items : [work_explainTemp]
					}, {
						border : false,
						layout : 'form',
						columnWidth : 1,
						items : [memoTemp]
					}]
		}]
	});

	// Center中的Grid的button 添加按钮 窗体对象
	var blockTempWindow;
	function showTempWindow() {
		if (!blockTempWindow) {
			blockTempWindow = new Ext.Window({
				// el : 'window_win',
				title : '',
				layout : 'fit',
				width : 600,
				height : 350,
				modal : true,
				closable : true,
				border : false,
				resizable : false,
				closeAction : 'hide',
				plain : true,
				// 面板中按钮的排列方式
				buttonAlign : 'center',
				items : [blockTempForm],
				buttons : [{
					text : '保存',
					handler : function() {
						var str = "";
						var i = 0;
						if (Ext.get("timeworkInfoTemp.classSequence").dom.value == "") {
							i++;
							str += i + "、班 次 未 选<br>";
						}
						if (str == "") {
							if (blockTempForm.getForm().isValid())
								if (op == "insert") {

									blockTempForm.getForm().submit({
										waitMsg : '保存中,请稍后...',
										url : "timework/addTempTimework.action",
										params : {
											method : "insert"
										},
										success : function(form, action) {
											blockTempForm.getForm().reset();
											blockTempWindow.hide();
											rungrids.reload();
										},
										failure : function(form, action) {
											var o = eval('('
													+ action.response.responseText
													+ ')');
											Ext.MessageBox.alert('错误', o.eMsg);
										}
									});
								} else if (op == "edit") {
									blockTempForm.getForm().submit({
										waitMsg : '保存中,请稍后...',
										url : "timework/updateTempTimework.action",
										params : {
											method : "edit",
											id : ''
										},
										success : function(form, action) {
											var o = eval('('
													+ action.response.responseText
													+ ')');

											blockTempForm.getForm().reset();
											blockTempWindow.hide();
											rungrids.reload();
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
						blockTempForm.getForm().reset();
						blockTempWindow.hide();
					}
				}]
			});
		}
		// 新增时，赋初始值
		blockTempWindow.setTitle("新增定期工作");
		blockTempForm.getForm().reset();
		blockTempWindow.show(Ext.get('getrole'));
	};

	// 添加临时下发工作
	var rungridTemp = new Ext.Button({
				text : '临时下发工作',
				iconCls : 'add',
				handler : function() {
					op = "insert";
					showTempWindow();
				}
			});

	// rungrid中的数据
	var rungridlist = new Ext.data.Record.create([rungridsm, {
				name : 'timeworkjInfo.id'
			}, {
				name : 'timeworkjInfo.workItemCode'
			}, {
				name : 'timeworkjInfo.machprofCode'
			}, {
				name : 'timeworkjInfo.workType'
			}, {
				name : 'timeworkjInfo.workItemName'
			}, {
				name : 'timeworkjInfo.workDate'
			}, {
				name : 'timeworkjInfo.cycle'
			}, {
				name : 'timeworkjInfo.classSequence'
			}, {
				name : 'timeworkjInfo.classTeam'
			}, {
				name : 'timeworkjInfo.dutytype'
			}, {
				name : 'timeworkjInfo.ifdelay'
			}, {
				name : 'timeworkjInfo.delayresult'
			}, {
				name : 'timeworkjInfo.delayman'
			}, {
				name : 'timeworkjInfo.delaydate'
			}, {
				name : 'timeworkjInfo.delaytype'
			}, {
				name : 'timeworkjInfo.opTicket'
			}, {
				name : 'timeworkjInfo.workresult'
			}, {
				name : 'timeworkjInfo.workExplain'
			}, {
				name : 'timeworkjInfo.ifcheck'
			}, {
				name : 'timeworkjInfo.checkdate'
			}, {
				name : 'timeworkjInfo.checkman'
			}, {
				name : 'timeworkjInfo.checkresult'
			}, {
				name : 'timeworkjInfo.ifimage'
			}, {
				name : 'timeworkjInfo.imagecode'
			}, {
				name : 'timeworkjInfo.ifExplain'
			}, {
				name : 'timeworkjInfo.memo'
			}, {
				name : 'timeworkjInfo.operator'
			}, {
				name : 'timeworkjInfo.delingDate'
			}, {
				name : 'timeworkjInfo.delayDate'
			}, {
				name : 'timeworkjInfo.protector'
			}, {
				name : 'timeworkjInfo.ifOpticket'
			}, {
				name : 'timeworkjInfo.conman'
			}, {
				name : 'timeworkjInfo.condate'
			}, {
				name : 'timeworkjInfo.status'
			}, {
				name : 'timeworkjInfo.enterprisecode'
			}, {
				name : 'timeworkjInfo.isUse'
			}, {
				name : 'checkmanName'
			}, {
				name : 'checkresultNm'
			}, {
				name : 'classsequenceName'
			}, {
				name : 'classteamName'
			}, {
				name : 'conmanName'
			}, {
				name : 'delaymanName'
			}, {
				name : 'delayresultNm'
			}, {
				name : 'dutytypeName'
			}, {
				name : 'ifcheckNm'
			}, {
				name : 'ifdelayNm'
			}, {
				name : 'ifexplainNm'
			}, {
				name : 'ifimageNm'
			}, {
				name : 'ifopticketNm'
			}, {
				name : 'machprofName'
			}, {
				name : 'operatorName'
			}, {
				name : 'protectorName'
			}, {
				name : 'statusNm'
			}, {
				name : 'workresultNm'
			}, {
				name : 'worktypeName'
			}, {
				name : 'checkdateNm'
			}, {
				name : 'condateNm'
			}, {
				name : 'delayDateNm'
			}, {
				name : 'delaydateNm'
			}, {
				name : 'delingDateNm'
			}, {
				name : 'workDateNm'
			}]);

	var rungridsm = new Ext.grid.CheckboxSelectionModel();

	var rungrids = new Ext.data.JsonStore({
				url : 'timework/getlistTimeworkj.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : rungridlist
			});

	rungrids.load({
				params : {
					querystimeDate : nowdate,
					queryetimeDate : nowdate,
					start : 0,
					limit : 18
				}
			});
	rungrids.on('beforeload', function() {
				Ext.apply(this.baseParams, {
					queryWorkType : Ext.get("RungridWorkType").dom.value,
					querystimeDate : Ext.get("startdt").dom.value,
					queryetimeDate : Ext.get("enddt").dom.value
						// ,
						// queryRunType : Ext.get("RungridIfChk").dom.value
					});
			});
	// 运行执行的Grid主体
	var rungrid = new Ext.grid.GridPanel({
				store : rungrids,
				columns : [rungridsm, new Ext.grid.RowNumberer(), {
							header : "状态",
							width : 25,
							sortable : false,
							dataIndex : 'statusNm'
						}, {
							header : "工作日期",
							width : 30,
							align : "center",
							sortable : true,
							dataIndex : 'workDateNm'
						}, {
							header : "工作内容",
							width : 150,
							align : "center",
							sortable : false,
							dataIndex : 'timeworkjInfo.workItemName'
						}, {
							header : "工作结果",
							width : 25,
							align : "center",
							sortable : false,
							dataIndex : 'workresultNm'
						},
						// {
						// header : "审批结果",
						// width : 20,
						// sortable : false,
						// renderer : function changeIt(val) {
						// if (val == "0") {
						// return "未审批";
						// } else if (val == "1") {
						// return "同意";
						// } else if (val == "2") {
						// return "不同意";
						// } else {
						// return "";
						// }
						// },
						// dataIndex : 'timeworkjInfo.checkresult'
						// },
						{
							header : "班次",
							width : 20,
							sortable : false,
							dataIndex : 'classsequenceName'
						}, {
							header : "值别",
							width : 20,
							sortable : false,
							dataIndex : 'dutytypeName',
							renderer : function changeIt(val) {
								if (val == null) {
									return "临时下发";
								} else {
									return val;
								}
							}
						}, {
							header : "操作人",
							width : 20,
							sortable : false,
							dataIndex : 'operatorName'
						}, {
							header : "监护人",
							width : 20,
							sortable : false,
							dataIndex : 'protectorName'
						}],
				viewConfig : {
					forceFit : true
				},
				tbar : ['工作类型 ', rungrid_work_type, {
							xtype : "tbseparator"
						}, start_time, '到', end_time,
						// '执行类别 ', rungrid_ifcheck,
						{
							xtype : "tbseparator"
						}, rungridqbtn, {
							xtype : "tbseparator"
						}, rungridbtn, {
							xtype : "tbseparator"
						}, rungridTemp, {
							xtype : "tbseparator"
						},
						// rungridhandup, {
						// xtype : "tbseparator"
						// },
						rungridop],
				sm : rungridsm,
				frame : false,
				border : false,
				bbar : new Ext.PagingToolbar({
							pageSize : 18,
							store : rungrids,
							displayInfo : true,
							displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
							emptyMsg : "没有记录"
						}),
				enableColumnHide : false,
				enableColumnMove : false,
				iconCls : 'icon-grid'
			});

	// 编码
	var workItemCodeShow = {
		id : "workItemCodeShow",
		xtype : "textfield",
		fieldLabel : '任务编码',
		name : 'workItemCodeShow',
		readOnly : true,
		anchor : '96%'
	};
	// 专业
	var machprofCodeShow = {
		id : "machprofCodeShow",
		xtype : "textfield",
		fieldLabel : '专业',
		name : 'machprofCodeShow',
		readOnly : true,
		anchor : '96%'
	};
	// 工作内容
	var workItemNameShow = {
		id : "workItemNameShow",
		xtype : "textfield",
		fieldLabel : '工作内容',
		name : 'workItemNameShow',
		readOnly : true,
		anchor : '98%'
	};
	// 工作类型
	var worktypeNameShow = {
		id : "worktypeNameShow",
		xtype : "textfield",
		fieldLabel : '工作类型',
		name : 'worktypeNameShow',
		readOnly : true,
		anchor : '96%'
	};
	// 状态
	var statusNmShow = {
		id : "statusNmShow",
		xtype : "textfield",
		fieldLabel : '状态',
		name : 'statusNmShow',
		readOnly : true,
		anchor : '96%'
	};
	// 班次
	var classsequenceNameShow = {
		id : "classsequenceNameShow",
		xtype : "textfield",
		fieldLabel : '班次',
		name : 'classsequenceNameShow',
		readOnly : true,
		anchor : '96%'
	};
	// 值别
	var dutytypeNameShow = {
		id : "dutytypeNameShow",
		xtype : "textfield",
		fieldLabel : '值别',
		name : 'dutytypeNameShow',
		readOnly : true,
		anchor : '96%'
	};
	// 是否要审批
	var ifcheckNmShow = {
		id : "ifcheckNmShow",
		xtype : "combo",
		fieldLabel : '是否要审批',
		name : 'ifcheckNmShow',
		readOnly : true,
		anchor : '96%',
		onTriggerClick : function() {
			tabpanel.setActiveTab(2);
		}
	};
	// 审批时间
	var checkdateShow = {
		id : "checkdateShow",
		xtype : "textfield",
		fieldLabel : '审批时间',
		name : 'checkdateShow',
		readOnly : true,
		anchor : '96%'
	};
	// 审批人
	var checkmanNameShow = {
		id : "checkmanNameShow",
		xtype : "textfield",
		fieldLabel : '审批人',
		name : 'checkmanNameShow',
		readOnly : true,
		anchor : '96%'
	};
	// 审批结果
	var checkresultNmShow = {
		id : "checkresultNmShow",
		xtype : "textfield",
		fieldLabel : '审批结果',
		name : 'checkresultNmShow',
		readOnly : true,
		anchor : '96%'
	};
	// 执行时间
	var workDateShow = {
		id : "workDateShow",
		name : 'workDateShow',
		xtype : "textfield",
		fieldLabel : '执行时间',
		readOnly : true,
		anchor : '96%'
	};
	// 操作人
	var operatornameShow = {
		id : "operatornameShow",
		name : 'operatornameShow',
		xtype : "textfield",
		fieldLabel : '操作人',
		readOnly : true,
		anchor : '96%'
	};
	// 监护人
	var protectornameShow = {
		id : "protectornameShow",
		name : 'protectornameShow',
		xtype : "textfield",
		fieldLabel : '监护人',
		readOnly : true,
		anchor : '96%'
	};
	// 说明是否必填
	var ifExplainShow = {
		id : "ifExplainShow",
		xtype : "textfield",
		fieldLabel : '说明是否必填',
		name : 'ifExplainShow',
		readOnly : true,
		anchor : '96%'
	};
	// 工作结果
	var workresultNmShow = {
		id : "workresultNmShow",
		xtype : "combo",
		fieldLabel : '工作结果',
		name : 'workresultNmShow',
		readOnly : true,
		anchor : '96%',
		onTriggerClick : function() {
			tabpanel.setActiveTab(1);
		}
	};
	// 是否延期
	var ifdelayNmShow = {
		id : "ifdelayNmShow",
		xtype : "combo",
		fieldLabel : '是否延期',
		name : 'ifdelayNmShow',
		readOnly : true,
		anchor : '96%',
		onTriggerClick : function() {
			tabpanel.setActiveTab(3);
		}
	};
	// 延期审批人
	var delaymanNameShow = {
		id : "delaymanNameShow",
		xtype : "textfield",
		fieldLabel : '延期审批人',
		name : 'delaymanNameShow',
		readOnly : true,
		anchor : '96%'
	};
	// 延期审批状态
	var delayresultNmShow = {
		id : "delayresultNmShow",
		xtype : "textfield",
		fieldLabel : '延期审批状态',
		name : 'delayresultNmShow',
		readOnly : true,
		anchor : '96%'
	};
	// 延期审时间
	var delaydateShow = {
		id : "delaydateShow",
		xtype : "textfield",
		fieldLabel : '延期审时间',
		name : 'delaydateShow',
		readOnly : true,
		anchor : '96%'
	};
	// 工作说明
	var workExplainShow = {
		id : "workExplainShow",
		xtype : "textarea",
		fieldLabel : '工作说明',
		name : 'workExplainShow',
		readOnly : true,
		anchor : '98%'
	};
	// 备注
	var memoShow = {
		id : "memoShow",
		xtype : "textarea",
		fieldLabel : '备注',
		name : 'memoShow',
		readOnly : true,
		anchor : '98%'
	};
	// 是否做事故预想
	var ifimageNmShow = {
		id : "ifimageNmShow",
		xtype : "textfield",
		fieldLabel : '是否做事故预想',
		name : 'ifimageNmShow',
		readOnly : true,
		anchor : '96%'
	};
	// 预想号
	var imagecodeShow = {
		id : "imagecodeShow",
		xtype : "textfield",
		fieldLabel : '预想号',
		name : 'imagecodeShow',
		readOnly : true,
		anchor : '96%'
	};
	// 数据展示的form 基本信息
	var blockFormbase = new Ext.form.FormPanel({
				labelAlign : 'right',
				frame : true,
				border : false,
				width : 498,
				height : 239,
				items : [{
					border : false,
					layout : 'form',
					items : [{
								border : false,
								layout : 'column',
								items : [{
											columnWidth : .5,
											layout : 'form',
											border : false,
											items : [workItemCodeShow]
										}, {
											columnWidth : .5,
											layout : 'form',
											border : false,
											items : [machprofCodeShow]
										}]
							}, {
								border : false,
								layout : 'form',
								columnWidth : 1,
								items : [workItemNameShow]
							}, {
								border : false,
								layout : 'column',
								items : [{
									columnWidth : .5,
									layout : 'form',
									border : false,
									items : [worktypeNameShow,
											classsequenceNameShow,
											workresultNmShow
									// ,ifcheckNmShow,ifdelayNmShow
									]
								}, {
									columnWidth : .5,
									layout : 'form',
									border : false,
									items : [statusNmShow, dutytypeNameShow]
								}]
							}, {
								border : false,
								layout : 'form',
								columnWidth : 1,
								items : [memoShow]
							}]
				}]
			});

	// 数据展示的form 执行信息
	var blockFormop = new Ext.form.FormPanel({
				labelAlign : 'right',
				frame : true,
				border : false,
				width : 498,
				height : 239,
				items : [{
					border : false,
					layout : 'form',
					items : [{
						border : false,
						layout : 'column',
						items : [{
							columnWidth : .5,
							layout : 'form',
							border : false,
							items : [operatornameShow, ifimageNmShow,
									ifExplainShow]
						}, {
							columnWidth : .5,
							layout : 'form',
							border : false,
							items : [protectornameShow, imagecodeShow,
									workDateShow]
						}]
					}, {
						border : false,
						layout : 'form',
						columnWidth : 1,
						items : [workExplainShow]
					}]
				}]
			});

	// 数据展示的form 审批信息
	var blockFormck = new Ext.form.FormPanel({
				labelAlign : 'right',
				frame : true,
				border : false,
				width : 498,
				height : 239,
				items : [{
					border : false,
					layout : 'form',
					items : [{
						border : false,
						layout : 'column',
						items : [{
									columnWidth : .5,
									layout : 'form',
									border : false,
									items : [checkmanNameShow,
											checkresultNmShow]
								}, {
									columnWidth : .5,
									layout : 'form',
									border : false,
									items : [checkdateShow]
								}]
					}]
				}]
			});

	// 数据展示的form 延期审批信息
	var blockFormde = new Ext.form.FormPanel({
				labelAlign : 'right',
				frame : true,
				border : false,
				width : 498,
				height : 239,
				items : [{
					border : false,
					layout : 'form',
					items : [{
								border : false,
								layout : 'column',
								items : [{
											columnWidth : .5,
											layout : 'form',
											border : false,
											items : [delaymanNameShow,
													delaydateShow]
										}, {
											columnWidth : .5,
											layout : 'form',
											border : false,
											items : [delayresultNmShow]
										}]
							}]
				}]
			});

	// 确认人
	var conmanNameShow = {
		id : "conmanNameShow",
		xtype : "textfield",
		fieldLabel : '确认人',
		name : 'conmanNameShow',
		readOnly : true,
		anchor : '96%'
	};
	// 确认时间
	var condateNmShow = {
		id : "condateNmShow",
		xtype : "textfield",
		fieldLabel : '确认时间',
		name : 'condateNmShow',
		readOnly : true,
		anchor : '96%'
	};

	// 数据展示的form 确认信息
	var blockFormcon = new Ext.form.FormPanel({
				labelAlign : 'right',
				frame : true,
				border : false,
				width : 498,
				height : 239,
				items : [{
							border : false,
							layout : 'form',
							items : [{
										border : false,
										layout : 'column',
										items : [{
													columnWidth : .5,
													layout : 'form',
													border : false,
													items : [conmanNameShow]
												}, {
													columnWidth : .5,
													layout : 'form',
													border : false,
													items : [condateNmShow]
												}]
									}]
						}]
			});

	// 总tabpanel
	var tabpanel = new Ext.TabPanel({
				renderTo : document.body,
				activeTab : 0,
				tabPosition : 'bottom',
				plain : true,
				layoutOnTabChange : true,
				defaults : {
					autoScroll : true
				},
				frame : false,
				border : false,
				items : [{
							title : '基本信息',
							items : blockFormbase
						}, {
							title : '执行信息',
							items : blockFormop,
							listeners : {
								activate : function() {
									Ext.get("operatornameShow").dom.value = (rec
											.get("operatorName") == null
											? ""
											: rec.get("operatorName"));
									Ext.get("protectornameShow").dom.value = (rec
											.get("protectorName") == null
											? ""
											: rec.get("protectorName"));
									Ext.get("ifExplainShow").dom.value = (rec
											.get("timeworkjInfo.ifExplain") == "Y"
											? "必填"
											: "不");
									Ext.get("workDateShow").dom.value = (rec
											.get("timeworkjInfo.workDate") == null
											? ""
											: rec.get("workDateNm"));
									Ext.get("workExplainShow").dom.value = (rec
											.get("timeworkjInfo.workExplain") == null
											? ""
											: rec
													.get("timeworkjInfo.workExplain"));
									Ext.get("ifimageNmShow").dom.value = (rec
											.get("timeworkjInfo.ifimage") == "Y"
											? "要"
											: "不");
									Ext.get("imagecodeShow").dom.value = (rec
											.get("timeworkjInfo.imagecode") == null
											? ""
											: rec
													.get("timeworkjInfo.imagecode"));
								}
							}
						}
				// , {
				// title : '审批信息',
				// items : blockFormck,
				// listeners : {
				// activate : function() {
				// Ext.get("checkdateShow").dom.value = (rec
				// .get("timeworkjInfo.checkdate") == null
				// ? ""
				// : rec.get("checkdateNm"));
				// Ext.get("checkmanNameShow").dom.value = (rec
				// .get("checkmanName") == null
				// ? ""
				// : rec.get("checkmanName"));
				// Ext.get("checkresultNmShow").dom.value = (rec
				// .get("checkresultNm") == null
				// ? ""
				// : rec.get("checkresultNm"));
				// }
				// }
				// }, {
				// title : '延期审批信息',
				// items : blockFormde,
				// listeners : {
				// activate : function() {
				// Ext.get("delaydateShow").dom.value = (rec
				// .get("timeworkjInfo.delaydate") == null
				// ? ""
				// : rec.get("delaydateNm"));
				// Ext.get("delaymanNameShow").dom.value = (rec
				// .get("delaymanName") == null
				// ? ""
				// : rec.get("delaymanName"));
				// Ext.get("delayresultNmShow").dom.value = (rec
				// .get("delayresultNm") == null
				// ? ""
				// : rec.get("delayresultNm"));
				// }
				// }
				// }, {
				// title : '运行确认信息',
				// items : blockFormcon,
				// listeners : {
				// activate : function() {
				// Ext.get("conmanNameShow").dom.value = (rec
				// .get("conmanName") == null
				// ? ""
				// : rec.get("conmanName"));
				// Ext.get("condateNmShow").dom.value = (rec
				// .get("timeworkjInfo.condate") == null
				// ? ""
				// : rec.get("condateNm"));
				// }
				// }
				// }
				]
			});
	var allrec = null;
	// South中的Grid的button 添加按钮 窗体对象
	var blockAddWindow;
	function showAddWindow(rec) {
		if (!blockAddWindow) {
			allrec = rec;
			blockAddWindow = new Ext.Window({
						title : '',
						layout : 'fit',
						width : 510,
						height : 296,
						modal : true,
						closable : true,
						border : false,
						resizable : false,
						closeAction : 'hide',
						plain : true,
						// 面板中按钮的排列方式
						buttonAlign : 'center',
						items : [tabpanel]
					});
		}
		blockAddWindow.setTitle("定期工作执行明细");
		blockAddWindow.show();
		Ext.get("workItemCodeShow").dom.value = rec
				.get("timeworkjInfo.workItemCode");
		Ext.get("machprofCodeShow").dom.value = rec.get("machprofName");
		Ext.get("workItemNameShow").dom.value = rec
				.get("timeworkjInfo.workItemName");
		Ext.get("worktypeNameShow").dom.value = rec.get("worktypeName");
		Ext.get("statusNmShow").dom.value = rec.get("statusNm");
		Ext.get("classsequenceNameShow").dom.value = rec
				.get("classsequenceName");
		Ext.get("dutytypeNameShow").dom.value = (rec
				.get("timeworkjInfo.dutytype") == null) ? "临时下发" : rec
				.get("dutytypeName");
		// Ext.get("ifcheckNmShow").dom.value =
		// (rec.get("timeworkjInfo.ifcheck") == "Y"
		// ? "要审批"
		// : "");
		// Ext.get("ifdelayNmShow").dom.value =
		// (rec.get("timeworkjInfo.ifdelay") == "Y"
		// ? "已申请"
		// : "");
		Ext.get("memoShow").dom.value = (rec.get("timeworkjInfo.memo") == null
				? ""
				: rec.get("timeworkjInfo.memo"));
		Ext.get("workresultNmShow").dom.value = (rec.get("workresultNm") == null
				? ""
				: rec.get("workresultNm"));
	};

	rungrid.on('rowdblclick', function(grid, rowIndex, e) {
				rec = rungrid.getStore().getAt(rowIndex);
				showAddWindow(rec);
				tabpanel.setActiveTab(0);
			});

	// 设定布局器及面板
	var layout = new Ext.Viewport({
				layout : "border",
				border : false,

				items : [{
							title : "",
							region : 'center',
							layout : 'fit',
							border : false,
							margins : '0 0 0 0',
							split : true,
							collapsible : false,
							items : [rungrid]
						}]
			});
});