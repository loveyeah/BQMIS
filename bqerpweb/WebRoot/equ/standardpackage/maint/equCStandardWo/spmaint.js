Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	Ext.QuickTips.init();
	// 专业
	var url = "timework/useprolist.action";
	var conn = Ext.lib.Ajax.getConnectionObject().conn;
	conn.open("POST", url, false);
	conn.send(null);
	var search_data1 = eval('(' + conn.responseText + ')');

	// 维修方案
	var url2 = "equstandard/getEquCStandardRepairmethodListToUse.action";
	var conn2 = Ext.lib.Ajax.getConnectionObject().conn;
	conn2.open("POST", url2, false);
	conn2.send(null);
	var search_data2 = eval('(' + conn2.responseText + ')');

	// 维修模式
	var url3 = "equstandard/getEquCStandardRepairmodeListToUse.action";
	var conn3 = Ext.lib.Ajax.getConnectionObject().conn;
	conn3.open("POST", url3, false);
	conn3.send(null);
	var search_data3 = eval('(' + conn3.responseText + ')');

	// 基础数据
	// 是、否（公用）
	var yes_no_data = [["是", "Y"], ["否", "N"]];

	// 标准工作名称
	var workorderTitle = {
		id : "workorderTitle",
		xtype : "textfield",
		fieldLabel : '标准工作名称',
		name : 'baseInfo.workorderTitle',
		allowBlank : false,
		blankText : '标准工作名称...',
		maxLength : 50,
		anchor : '99%'
	};

	// 标准工作指令
	var jobCode = {
		id : "jobCode",
		xtype : "textfield",
		fieldLabel : '标准工作指令',
		name : 'baseInfo.jobCode',
		allowBlank : false,
		blankText : '标准工作指令...',
		anchor : '99%'
	};

	// 标准工作描述
	var workorderMemo = {
		id : "workorderMemo",
		xtype : "textarea",
		fieldLabel : '描述',
		name : 'baseInfo.workorderMemo',
		blankText : '描述...',
		anchor : '99%'
	};

	// 专业
	var professionCode = {
		id : "professionCode",
		xtype : "combo",
		name : 'professionCode',
		triggerAction : 'all',
		store : new Ext.data.SimpleStore({
					fields : ['name', 'id'],
					data : search_data1
				}),
		hiddenName : 'baseInfo.professionCode',
		displayField : 'name',
		valueField : 'id',
		fieldLabel : "专业",
		mode : 'local',
		emptyText : '请选择...',
		blankText : '请选择',
		readOnly : true,
		anchor : '99%'
	};

	// 维修方案编号
	var repairmethodCode = {
		id : "repairmethodCode",
		xtype : "combo",
		name : 'repairmethodCode',
		triggerAction : 'all',
		store : new Ext.data.SimpleStore({
					fields : ['name', 'id'],
					data : search_data2
				}),
		hiddenName : 'baseInfo.repairmethodCode',
		displayField : 'name',
		valueField : 'id',
		fieldLabel : "维修方案",
		mode : 'local',
		emptyText : '请选择...',
		blankText : '请选择',
		readOnly : true,
		anchor : '99%'
	};

	// 维修模式
	var repairModel = {
		id : "repairModel",
		xtype : "combo",
		name : 'repairModel',
		triggerAction : 'all',
		store : new Ext.data.SimpleStore({
					fields : ['name', 'id'],
					data : search_data3
				}),
		hiddenName : 'baseInfo.repairModel',
		displayField : 'name',
		valueField : 'id',
		fieldLabel : "维修模式",
		mode : 'local',
		emptyText : '请选择...',
		blankText : '请选择',
		readOnly : true,
		anchor : '99%'
	};

	// 工单是否外包
	var ifOutside = {
		id : "ifOutside",
		xtype : "combo",
		name : 'ifOutside',
		allowBlank : true,
		triggerAction : 'all',
		store : new Ext.data.SimpleStore({
					fields : ['name', 'id'],
					data : yes_no_data
				}),
		hiddenName : 'baseInfo.ifOutside',
		displayField : 'name',
		valueField : 'id',
		value : 'N',
		fieldLabel : "工单是否外包",
		mode : 'local',
		emptyText : '工单是否外包...',
		blankText : '工单是否外包',
		readOnly : true,
		anchor : '99%'
	};

	// 是否填写报告
	var ifReport = {
		id : "ifReport",
		xtype : "combo",
		name : 'ifReport',
		allowBlank : true,
		triggerAction : 'all',
		store : new Ext.data.SimpleStore({
					fields : ['name', 'id'],
					data : yes_no_data
				}),
		hiddenName : 'baseInfo.ifReport',
		displayField : 'name',
		valueField : 'id',
		value : 'N',
		fieldLabel : "是否填写报告",
		mode : 'local',
		emptyText : '是否填写报告...',
		blankText : '是否填写报告',
		readOnly : true,
		anchor : '99%'
	};

	// 是否拆除保温
	var ifRemove = {
		id : "ifRemove",
		xtype : "combo",
		name : 'ifRemove',
		allowBlank : true,
		triggerAction : 'all',
		store : new Ext.data.SimpleStore({
					fields : ['name', 'id'],
					data : yes_no_data
				}),
		hiddenName : 'baseInfo.ifRemove',
		displayField : 'name',
		valueField : 'id',
		value : 'N',
		fieldLabel : "是否拆除保温",
		mode : 'local',
		emptyText : '是否拆除保温...',
		blankText : '是否拆除保温',
		readOnly : true,
		anchor : '99%'
	};

	// 是否使用吊车
	var ifCrane = {
		id : "ifCrane",
		xtype : "combo",
		name : 'ifCrane',
		allowBlank : true,
		triggerAction : 'all',
		store : new Ext.data.SimpleStore({
					fields : ['name', 'id'],
					data : yes_no_data
				}),
		hiddenName : 'baseInfo.ifCrane',
		displayField : 'name',
		valueField : 'id',
		value : 'N',
		fieldLabel : "是否使用吊车",
		mode : 'local',
		emptyText : '是否使用吊车...',
		blankText : '是否使用吊车',
		readOnly : true,
		anchor : '99%'
	};

	// 是否使用脚手架
	var ifFalsework = {
		id : "ifFalsework",
		xtype : "combo",
		name : 'ifFalsework',
		allowBlank : true,
		triggerAction : 'all',
		store : new Ext.data.SimpleStore({
					fields : ['name', 'id'],
					data : yes_no_data
				}),
		hiddenName : 'baseInfo.ifFalsework',
		displayField : 'name',
		valueField : 'id',
		value : 'N',
		fieldLabel : "是否使用脚手架",
		mode : 'local',
		emptyText : '是否使用脚手架...',
		blankText : '是否使用脚手架',
		readOnly : true,
		anchor : '99%'
	};

	// 排序号
	var orderby = {
		id : "orderby",
		xtype : "numberfield",
		fieldLabel : '排序号',
		name : 'baseInfo.orderby',
		allowBlank : true,
		allowDecimals:false,//modify by wpzhu
		blankText : '排序号...',
		anchor : '99%'
	};

	// id
	var hidden_id = {
		id : "woId",
		xtype : "textfield",
		name : 'baseInfo.woId',
		hidden : true,
		hideLabel : true,
		anchor : '50%'
	};

	// 左边弹窗的表单对象
	var blockForm = new Ext.form.FormPanel({
				labelAlign : 'right',
				frame : true,
				items : [{
					border : false,
					layout : 'form',
					items : [{
								border : false,
								layout : 'form',
								columnWidth : 1,
								items : [workorderTitle]
							}, {
								border : false,
								layout : 'column',
								items : [{
									columnWidth : .5,
									layout : 'form',
									border : false,
									items : [jobCode, repairModel, ifOutside,
											ifReport, ifFalsework]
								}, {
									columnWidth : .5,
									layout : 'form',
									border : false,
									items : [professionCode, repairmethodCode,
											ifRemove, ifCrane, orderby]
								}]
							}, {
								border : false,
								layout : 'form',
								columnWidth : 1,
								items : [workorderMemo, hidden_id]
							}]
				}]
			});

	// 左边的弹出窗体
	var blockAddWindow;
	function showAddWindow() {
		blockForm.getForm().reset();
		if (!blockAddWindow) {
			blockAddWindow = new Ext.Window({
				// el : 'window_win',
				title : '',
				layout : 'fit',
				width : 550,
				height : 330,
				modal : false,
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
					handler : function() {
						
						if (blockForm.getForm().isValid())
							if (op == "add") {
								blockForm.getForm().submit({
									waitMsg : '保存中,请稍后...',
									url : "equstandard/saveEquCStandardWo.action",
									success : function(form, action) {
										blockForm.getForm().reset();
										blockAddWindow.hide();
										westgrids.reload();
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
									url : "equstandard/updateEquCStandardWo.action",
									params : {
										// method : "edit",
										id : westgrid.getSelectionModel()
												.getSelected().get("woId")

									},
									success : function(form, action) {
										var o = eval('('
												+ action.response.responseText
												+ ')');
												
										Ext.MessageBox.alert('提示', o.msg);		
										blockForm.getForm().reset();
										blockAddWindow.hide();
										westgrids.reload();
									},
									failure : function(form, action) {
										var o = eval('('
												+ action.response.responseText
												+ ')');
										Ext.MessageBox.alert('提示', o.msg);
									}
								});
							} else {
								Ext.MessageBox.alert('错误', '未定义的操作');
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
		if (op == "add") {
			// 新增时，赋初始值
			blockAddWindow.setTitle("新增标准工作包");
		} else if (op == "edit") {
			blockAddWindow.setTitle("修改标准工作包");
			blockAddWindow.show();
			var rec = westgrid.getSelectionModel().getSelected();
			blockForm.getForm().loadRecord(rec);
		} else {
		}
		blockAddWindow.show(Ext.get('getrole'));
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
				Ext.Msg.confirm('提示信息', '确认 删除 选中记录?', function(button, text) {
					if (button == 'yes') {
						var rec = westgrid.getSelectionModel().getSelections();
						var str = "";
						for (var i = 0; i < rec.length; i++) {
							if (i < rec.length - 1)
								str += rec[i].data.woId + ",";
							else
								str += rec[i].data.woId;
						}
						Ext.Ajax.request({
									waitMsg : '删除中,请稍后...',
									url : 'equstandard/deleteEquCStandardWo.action',
									params : {
										ids : str
									},
									success : function(response, options) {
										westgrids.reload();
										eastab.setActiveTab(0);
										Ext.getCmp('titlepanel')
												.setTitle("单选左侧列表中记录显示");
										iframe0.location = "about:blank";
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

	// 锁定按钮
	var westbtnlock = new Ext.Button({
		text : '锁定',
		iconCls : 'locked',
		handler : function() {
			if (westgrid.getSelectionModel().getSelections().length > 0) {
				Ext.Msg.confirm('提示信息', '确认 锁定 选中记录?', function(button, text) {
					if (button == 'yes') {
						var rec = westgrid.getSelectionModel().getSelections();
						var str = "";
						for (var i = 0; i < rec.length; i++) {
							if (i < rec.length - 1)
								str += rec[i].data.woId + ",";
							else
								str += rec[i].data.woId;
						}

						Ext.Ajax.request({
									waitMsg : '锁定中,请稍后...',
									url : 'equstandard/lockEquCStandardWo.action',
									params : {
										ids : str
									},
									success : function(response, options) {
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

	// 解锁按钮
	var westbtnunlock = new Ext.Button({
		text : '解锁',
		iconCls : 'unlocked',
		handler : function() {
			if (westgrid.getSelectionModel().getSelections().length > 0) {
				Ext.Msg.confirm('提示信息', '确认 解锁 选中记录?', function(button, text) {
					if (button == 'yes') {
						var rec = westgrid.getSelectionModel().getSelections();
						var str = "";
						for (var i = 0; i < rec.length; i++) {
							if (i < rec.length - 1)
								str += rec[i].data.woId + ",";
							else
								str += rec[i].data.woId;
						}
						Ext.Ajax.request({
									waitMsg : '解锁中,请稍后...',
									url : 'equstandard/unlockEquCStandardWo.action',
									params : {
										ids : str
									},
									success : function(response, options) {
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

	// 刷新按钮
	var westbtnref = new Ext.Button({
				text : '刷新',
				iconCls : 'reflesh',
				handler : function() {
					westgrids.load({
								params : {
									start : 0,
									limit : 18
								}
							});
				}
			});

	var westsm = new Ext.grid.CheckboxSelectionModel();
	// 左边列表中的数据
	var datalist = new Ext.data.Record.create([

	{
				name : 'workorderTitle'
			}, {
				name : 'workorderTitle'
			}, {
				name : 'jobCode'
			}, {
				name : 'woCode'
			}, {
				name : 'maintDep'
			}, {
				name : 'professionCode'
			}, {
				name : 'repairModel'
			}, {
				name : 'repairmethodCode'
			}, {
				name : 'kksCode'
			}, {
				name : 'planWotime'
			}, {
				name : 'ifOutside'
			}, {
				name : 'ifReport'
			}, {
				name : 'ifRemove'
			}, {
				name : 'ifCrane'
			}, {
				name : 'ifFalsework'
			}, {
				name : 'remark'
			}, {
				name : 'woId'
			}, {
				name : 'status'
			}, {
				name : 'orderby'
			}, {
				name : 'workorderMemo'
			}]);

	var westgrids = new Ext.data.JsonStore({
				url : 'equstandard/getEquCStandardWoList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : datalist
			});

	westgrids.load({
				params : {
					start : 0,
					limit : 18
				}
			});

	// 左边列表
	var westgrid = new Ext.grid.GridPanel({
				ds : westgrids,
				columns : [westsm, new Ext.grid.RowNumberer(), {
							header : "状态",
							width : 40,
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
							header : "标准工作名称",
							width : 120,
							sortable : false,
							dataIndex : 'workorderTitle'
						}, {
							header : "标准工作指令",
							width : 120,
							sortable : true,
							dataIndex : 'jobCode'
						}],
				tbar : [westbtnAdd, {
							xtype : "tbseparator"
						}, westbtnedit, {
							xtype : "tbseparator"
						}, westbtndel, {
							xtype : "tbseparator"
						}, westbtnlock, {
							xtype : "tbseparator"
						}, westbtnunlock, {
							xtype : "tbseparator"
						}, westbtnref],
				sm : westsm,
				frame : true,
				bbar : new Ext.PagingToolbar({
							pageSize : 18,
							store : westgrids,
							displayInfo : true,
							displayMsg : "{0} 到 {1} /共 {2} 条",
							emptyMsg : "没有记录"
						}),
				border : true,
				enableColumnHide : false,
				enableColumnMove : false,
				iconCls : 'icon-grid'
			});

	var sendcode;
	// westgrid 的事件
	westgrid.on('rowclick', function(grid, rowIndex, e) {
		if (westgrid.getSelectionModel().hasSelection()) {
			if (westgrid.getSelectionModel().getSelected() != null
					&& westgrid.getSelectionModel().getSelections().length < 2) {
				sendcode = westgrid.getSelectionModel().getSelected()
						.get("woCode");
				title = westgrid.getSelectionModel().getSelected()
						.get("workorderTitle");
				eastab.setActiveTab(0);
				Ext.getCmp('titlepanel').setTitle(title + " 的 详 细 内 容");
				iframe0.location = "../equCStandardOrderstep/equCStandardOrderstep.jsp?woCode="
						+ sendcode;
			} else {
				eastab.setActiveTab(0);
				Ext.getCmp('titlepanel').setTitle("单选左侧列表中记录显示");
				iframe0.location = "about:blank";
			}
		}else{
			eastab.setActiveTab(0);
				Ext.getCmp('titlepanel').setTitle("单选左侧列表中记录显示");
				iframe0.location = "about:blank";
		}
	})

	function getCode(psName, postUrl) {
		var result = "";
		var str = postUrl.search.substring(0);
		if (str.indexOf(psName) != -1
				&& (str.substr(str.indexOf(psName) - 1, 1) == "?" || str
						.substr(str.indexOf(psName) - 1, 1) == "&")) {
			if (str.substring(str.indexOf(psName), str.length).indexOf("&") != -1) {
				var Test = str.substring(str.indexOf(psName), str.length);
				result = Test.substr(Test.indexOf(psName), Test.indexOf("&")
								- Test.indexOf(psName));
				result = result.substring(result.indexOf("=") + 1,
						result.length);
			} else {
				result = str.substring(str.indexOf(psName), str.length);
				result = result.substring(result.indexOf("=") + 1,
						result.length);
			}
		}
		return result;
	}

	// 右边TAB页
	var eastab = new Ext.TabPanel({
		renderTo : document.body,
		activeTab : 0,
		tabPosition : 'bottom',
		plain : true,
		defaults : {
			autoScroll : true
		},
		frame : false,
		border : false,
		items : [{
			title : '标准工作',
			id : 'tab0',
			listeners : {
				activate : function() {
					if (sendcode != null) {
						if (getCode("woCode", iframe0.location) != sendcode)
							iframe0.location = "../equCStandardOrderstep/equCStandardOrderstep.jsp?woCode="
									+ sendcode;
					}
				}
			},
			html : '<iframe id="iframe0" src="" style="width:100%;height:100%;border:0px;"></iframe>'
		}, {
			title : '相关设备',
			id : 'tab1',
			listeners : {
				activate : function() {
					if (sendcode != null) {
						if (getCode("woCode", iframe1.location) != sendcode)
							iframe1.location = "../equCStandardRelateEqu/relateequ.jsp?woCode="
									+ sendcode;
					}
				}
			},
			html : '<iframe id="iframe1" src="" style="width:100%;height:100%;border:0px;"></iframe>'

		}, {
			title : '相关文件包',
			id : 'tab2',
			listeners : {
				activate : function() {
					if (sendcode != null) {
						if (getCode("woCode", iframe2.location) != sendcode)
							iframe2.location = "../equCStandardRelateFile/equdocument.jsp?woCode="
									+ sendcode;
					}
				}
			},
			html : '<iframe id="iframe2" src="" style="width:100%;height:100%;border:0px;"></iframe>'
		}]
	});

	// 设定布局器及面板
	var layout = new Ext.Viewport({
				layout : "border",
				border : false,
				items : [{
							title : "标准包列表",
							region : 'west',
							layout : 'fit',
							border : false,
							margins : '1 0 1 1',
							split : true,
							collapsible : true,
							width : 350,
							items : [westgrid]

						}, {
							title : "单选左侧列表中记录显示",
							id : "titlepanel",
							region : 'center',
							layout : 'fit',
							border : false,
							margins : '1 0 -3 0',
							split : true,
							collapsible : true,
							items : [eastab]
						}]
			});
});