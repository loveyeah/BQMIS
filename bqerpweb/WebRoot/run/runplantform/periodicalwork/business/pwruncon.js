Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.apply(Ext.form.VTypes, {
			daterange : function(val, field) {
				var date = field.parseDate(val);

				if (!date) {
					return;
				}
				if (field.startDateField
						&& (!this.dateRangeMax || (date.getTime() != this.dateRangeMax
								.getTime()))) {
					var start = Ext.getCmp(field.startDateField);
					start.setMaxValue(date);
					start.validate();
					this.dateRangeMax = date;
				} else if (field.endDateField
						&& (!this.dateRangeMin || (date.getTime() != this.dateRangeMin
								.getTime()))) {
					var end = Ext.getCmp(field.endDateField);
					end.setMinValue(date);
					end.validate();
					this.dateRangeMin = date;
				}
				/*
				 * Always return true since we're only using this vtype to set
				 * the min/max allowed values (these are tested for after the
				 * vtype test)
				 */
				return true;
			},

			password : function(val, field) {
				if (field.initialPassField) {
					var pwd = Ext.getCmp(field.initialPassField);
					return (val == pwd.getValue());
				}
				return true;
			},

			passwordText : 'Passwords do not match'
		});

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
	}
	// 执行类别
	var search_data2 = [["全部", ""], ["异常", "N"], ["正常", "Y"]];

	// 工作类型
	var url = "timework/uselistTimeworktype.action";
	var conn = Ext.lib.Ajax.getConnectionObject().conn;
	conn.open("POST", url, false);
	conn.send(null);
	var search_data1
	if (conn.responseText != "") {
		search_data1 = eval('([' + conn.responseText
				+ ',[\"全部定期工作\",\"all\"]])');
	} else {
		search_data1 = eval('([[\"全部定期工作\",\"all\"]])');
	}

	// 专业
	var url = "timework/useprolist.action";
	var conn = Ext.lib.Ajax.getConnectionObject().conn;
	conn.open("POST", url, false);
	conn.send(null);
	var search_data3;
	if (conn.responseText != null || conn.responseText != "") {
		search_data3 = eval('(' + conn.responseText + ')');
	} else {
		search_data3 = [];
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
		value : new Date().format('yyyy-MM-dd'),
		width : 120
	};

	var end_time = {
		id : "enddt",
		xtype : "datefield",
		fieldLabel : '到',
		name : 'enddt',
		allowBlank : true,
		blankText : '时间...',
		format : 'Y-m-d',
		value : new Date().format('yyyy-MM-dd'),
		width : 120
	};

	// rungrid中的Grid的工作类型下拉框
	var rungrid_work_type = {
		fieldLabel : '工作类型',
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
		emptyText : '工作类型...',
		blankText : '工作类型',
		readOnly : true,
		width : 120
	};

	// rungrid中的Grid的延期查询下拉框
	var rungrid_ifcheck = {
		fieldLabel : '专业',
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
		emptyText : '执行类别...',
		blankText : '执行类别',
		readOnly : true,
		width : 120
	};

	// rungrid中的Grid的延期查询下拉框
	var rungrid_workitemname = {
		fieldLabel : '工作内容',
		id : "rungrid_workitemname",
		xtype : "combo",
		name : 'rungrid_workitemname',
		allowBlank : true,
		triggerAction : 'all',
		store : new Ext.data.SimpleStore({
					fields : ['name', 'id'],
					data : search_data2
				}),
		hiddenName : 'RungridWorkitemname',
		displayField : 'name',
		valueField : 'id',
		mode : 'local',
		emptyText : '执行类别...',
		blankText : '执行类别',
		readOnly : true,
		width : 120
	};
	// rungrid中的Grid的延期查询下拉框
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
		emptyText : '执行类别...',
		blankText : '执行类别',
		readOnly : true,
		width : 120
	};
	// 签名
	var signame = {
		id : "username",
		name : "username",
		xtype : "textfield",
		fieldLabel : '帐号',
		anchor : '93%'
	};

	// 密码
	var password = {
		id : "password",
		name : "password",
		xtype : "textfield",
		fieldLabel : '密码',
		inputType : 'password',
		anchor : '93%'
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
										layout : 'form',
										columnWidth : 1,
										items : [signame]
									}, {
										border : false,
										layout : 'form',
										columnWidth : 1,
										items : [password]
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
				width : 280,
				height : 130,
				modal : false,
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
							if (re == "ok") {
								var rec = rungrid.getSelectionModel()
										.getSelected();
								blockdForm.getForm().submit({
									waitMsg : '保存中,请稍后...',
									url : "timework/confirmTimeworkj.action",
									params : {
										id : rec.get("timeworkjInfo.id")
									},
									success : function(form, action) {
										var o = eval('('
												+ action.response.responseText
												+ ')');
										Ext.MessageBox.alert('成功', '操 作 成 功 !');
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
							} else {
								var rec = rungrid.getSelectionModel()
										.getSelected();
								blockdForm.getForm().submit({
									waitMsg : '保存中,请稍后...',
									url : "timework/disagreeTimeworkj.action",
									params : {
										id : rec.get("timeworkjInfo.id")
									},
									success : function(form, action) {
										var o = eval('('
												+ action.response.responseText
												+ ')');
										Ext.MessageBox.alert('成功', '操 作 成 功 !');
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
				}, {
					text : '取消',
					handler : function() {
						blockdForm.getForm().reset();
						blockAdddWindow.hide();
					}
				}]
			});
		}
		blockAdddWindow.setTitle("签名");
		blockAdddWindow.show(Ext.get('getrole'));
	};

	// rungrid中的按钮 查询
	var btnQuery = new Ext.Button({
		text : '查询',
		xtype : "button",
		iconCls : 'query',
		handler : function() {
			rungrids.load({
						params : {
							querystimeDate : Ext.get("startdt").dom.value,
							queryetimeDate : Ext.get("enddt").dom.value,
							queryWorkType : Ext.get("RungridWorkType").dom.value,
							queryMachprofCode : Ext.get("RungridIfChk").dom.value,
							queryWorkResult : Ext.get("RungridWorkresult").dom.value,
							start : 0,
							limit : 18
						}
					});
		}
	});

	// rungrid中的按钮 打印
	var btnPrint = new Ext.Button({
				text : '打印',
				xtype : "button",
				iconCls : 'print',
				handler : function() {
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

	// 确认按钮
	var rungridcon = new Ext.Button({
				id : 'handup',
				text : '确认',
				iconCls : 'save',
				handler : function() {
					if (CKSelectone()) {
						var rec = rungrid.getSelectionModel().getSelected();
						if (rec.get("conmanName") == null) {
							if (rec.get("timeworkjInfo.operator") != null
									&& rec.get("timeworkjInfo.protector") != null
									&& rec.get("timeworkjInfo.workresult") != null) {
								re = "ok";
								showAdddWindow();
							} else {
								Ext.Msg.alert('提示信息', "未完成的工作不可确认");
							}
						} else {
							Ext.Msg.alert('提示信息', "已确认的工作");
						}
					}
				}
			});

	// rungrid中的按钮 刷新
	var btnRefresh = new Ext.Button({
				id : 'reflesh',
				text : '刷新',
				iconCls : 'reflesh',
				handler : function() {
				}
			});

	var tbarform = new Ext.FormPanel({
		labelAlign : 'right',
		frame : true,
		items : [{
			border : false,
			layout : 'form',
			width : 800,
			items : [{
						border : false,
						layout : 'column',
						items : [{
									columnWidth : .33,
									layout : 'form',
									border : false,
									items : [start_time, end_time]
								}, {
									columnWidth : .34,
									layout : 'form',
									border : false,
									items : [rungrid_work_type, rungrid_ifcheck]
								}, {
									columnWidth : .33,
									layout : 'form',
									border : false,
									items : [rungrid_workresult]
								}]
					}]
		}]
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
	// rungrids.load({
	// params : {
	// start : 0,
	// limit : 15
	// }
	// });
	rungrids.on('beforeload', function() {
				Ext.apply(this.baseParams, {
							querystimeDate : Ext.get("startdt").dom.value,
							queryetimeDate : Ext.get("enddt").dom.value,
							queryWorkType : Ext.get("RungridWorkType").dom.value,
							queryMachprofCode : Ext.get("RungridIfChk").dom.value,
							queryWorkResult : Ext.get("RungridWorkresult").dom.value
						});
			});
	// 运行执行的Grid主体
	var rungrid = new Ext.grid.GridPanel({
				store : rungrids,
				columns : [rungridsm, new Ext.grid.RowNumberer(), {
							header : "专业",
							width : 20,
							sortable : false,
							dataIndex : 'machprofName'
						}, {
							header : "工作类型",
							width : 20,
							sortable : false,
							dataIndex : 'worktypeName'
						}, {
							header : "工作日期",
							width : 35,
							align : "center",
							sortable : true,
							dataIndex : 'workDateNm'
						}, {
							header : "工作内容",
							width : 100,
							align : "center",
							sortable : false,
							dataIndex : 'timeworkjInfo.workItemName'
						}, {
							header : "工作结果",
							width : 20,
							align : "center",
							sortable : false,
							renderer : function changeIt(val) {
								if (val == "1") {
									return "正常";
								} else if (val == "2") {
									return "不正常";
								} else if (val == "3") {
									return "因故未作";
								} else if (val == "4") {
									return "取消";
								} else {
									return "";
								}
							},
							dataIndex : 'timeworkjInfo.workresult'
						}, {
							header : "班次",
							width : 20,
							sortable : false,
							dataIndex : 'classsequenceName'
						}, {
							header : "班组",
							width : 20,
							sortable : false,
							dataIndex : 'dutytypeName'
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
						}, {
							header : "运行确认人",
							width : 20,
							sortable : false,
							dataIndex : 'conmanName'
						}, {
							header : "运行确认时间",
							width : 30,
							sortable : false,
							renderer : function format(val) {
								if (val != null) {
									str = val.substr(0, 10);
									str += " ";
									str += val.substr(11, 20);
									return str;
								}
							},
							dataIndex : 'timeworkjInfo.condate'
						}],
				viewConfig : {
					forceFit : true
				},
				tbar : [btnQuery, {
							xtype : "tbseparator"
						}, rungridcon, {
							xtype : "tbseparator"
						}, btnRefresh],
				sm : rungridsm,
				frame : false,
				border : false,
				bbar : new Ext.PagingToolbar({
							pageSize : 15,
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
											ifcheckNmShow, ifdelayNmShow]
								}, {
									columnWidth : .5,
									layout : 'form',
									border : false,
									items : [statusNmShow, dutytypeNameShow,
											workresultNmShow]
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
											.get("timeworkjInfo.ifimage") == "Y"
											? rec
													.get("timeworkjInfo.imagecode")
											: "");
								}
							}
						}, {
							title : '审批信息',
							items : blockFormck,
							listeners : {
								activate : function() {
									Ext.get("checkdateShow").dom.value = (rec
											.get("timeworkjInfo.checkdate") == null
											? ""
											: rec.get("checkdateNm"));
									Ext.get("checkmanNameShow").dom.value = (rec
											.get("checkmanName") == null
											? ""
											: rec.get("checkmanName"));
									Ext.get("checkresultNmShow").dom.value = (rec
											.get("checkresultNm") == null
											? ""
											: rec.get("checkresultNm"));
								}
							}
						}, {
							title : '延期审批信息',
							items : blockFormde,
							listeners : {
								activate : function() {
									Ext.get("delaydateShow").dom.value = (rec
											.get("timeworkjInfo.delaydate") == null
											? ""
											: rec.get("delaydateNm"));
									Ext.get("delaymanNameShow").dom.value = (rec
											.get("delaymanName") == null
											? ""
											: rec.get("delaymanName"));
									Ext.get("delayresultNmShow").dom.value = (rec
											.get("delayresultNm") == null
											? ""
											: rec.get("delayresultNm"));
								}
							}
						}, {
							title : '运行确认信息',
							items : blockFormcon,
							listeners : {
								activate : function() {
									Ext.get("conmanNameShow").dom.value = (rec
											.get("conmanName") == null
											? ""
											: rec.get("conmanName"));
									Ext.get("condateNmShow").dom.value = (rec
											.get("timeworkjInfo.condate") == null
											? ""
											: rec.get("condateNm"));
								}
							}
						}]
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
						modal : false,
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
		Ext.get("dutytypeNameShow").dom.value = rec.get("dutytypeName");
		Ext.get("ifcheckNmShow").dom.value = (rec.get("timeworkjInfo.ifcheck") == "Y"
				? "要审批"
				: "");
		Ext.get("ifdelayNmShow").dom.value = (rec.get("timeworkjInfo.ifdelay") == "Y"
				? "已申请"
				: "");
		Ext.get("memoShow").dom.value = (rec.get("timeworkjInfo.memo") == null
				? ""
				: rec.get("timeworkjInfo.memo"));
		Ext.get("workresultNmShow").dom.value = (rec.get("workresultNm") == null
				? "asd"
				: rec.get("workresultNm"));
	};

	rungrid.on('rowdblclick', function(grid, rowIndex, e) {
				rec = rungrid.getStore().getAt(rowIndex);
				showAddWindow(rec);
			})

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
							split : false,
							collapsible : false,
							items : [rungrid]

						}, {
							title : "",
							region : "north",
							layout : 'fit',
							border : false,
							margins : '0 0 0 0',
							height : 62,
							split : false,
							collapsible : false,
							// 注入表格
							items : [tbarform]
						}]
			});
});