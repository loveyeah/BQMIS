Ext.ns("bigReward.approveList");

bigReward.approveList = function() {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
	function getYearMonth() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t
		return s;

	}
	function getDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate();
		s += (t > 9 ? "" : "0") + t;
		return s;

	}
	function moneyFormat(v) {
		if (v == null || v == "") {
			return "0.0000";
		}
		v = (Math.round((v - 0) * 10000)) / 10000;
		v = (v == Math.floor(v))
				? v + ".0000"
				: ((v * 10 == Math.floor(v * 10))
						? v + "000"
						: ((v * 100 == Math.floor(v * 100)) ? v + "00" : ((v
								* 1000 == Math.floor(v * 1000)) ? v + "0" : v)));
		v = String(v);
		var ps = v.split('.');
		var whole = ps[0];
		var sub = ps[1] ? '.' + ps[1] : '.0000';
		var r = /(\d+)(\d{3})/;
		while (r.test(whole)) {
			whole = whole.replace(r, '$1' + ',' + '$2');
		}
		v = whole + sub;
		if (v.charAt(0) == '-') {
			return '-' + v.substr(1);
		}
		return v;
	}
	var id = "";
	var method = "add";
	var entryId = "";
	var specialityName = "";
	var sessWorcode;
	var sessWorname;
	var deptId = "";
	var fillName = new Ext.form.TextField({
		id : 'name',
		fieldLabel : '填写人',
		//				name : 'fillName',
		anchor : "85%",
		value : getWorkCode(),
		readOnly : true

	});
	var fillBy = new Ext.form.Hidden({
		id : "fillBy",
		name : 'reward.fillBy'
	});
	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
			success : function(action) {
				var result = eval("(" + action.responseText + ")");
				if (result.workerCode) {
					// 设定默认工号
					sessWorcode = result.workerCode;
					sessWorname = result.workerName;
					deptId = result.deptId;
					fillName.setValue(sessWorname);
					fillBy.setValue(sessWorcode);

				}
			}
		});
	}
	getWorkCode();
	var bigRewardMonth = new Ext.form.TextField({
		id : 'bigRewardMonth',
		fieldLabel : '月份',
		name : 'reward.bigRewardMonth',
		anchor : "85%",
		value : getYearMonth(),
		readOnly : true

	});
	var nameStrore = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'hr/getAllawardName.action'
		}),
		reader : new Ext.data.JsonReader({
			root : 'list'
		}, [{
			name : 'bigAwardId',
			mapping : 0
		}, {
			name : 'bigAwardName',
			mapping : 1
		}, {
			name : 'bigRewardBase',
			mapping : 2
		}])
	});

	nameStrore.load({
		params : {
		//			month:bigRewardMonth.getValue()
		}
	})

	var rewardName = new Ext.form.TextField({
		id : 'bigAwardName',
		name : 'reward.bigAwardName',
		fieldLabel : '大奖名称',
		readOnly : true,
		anchor : "85%"
	})

	var rewardBase = new Ext.form.TextField({
		id : 'bigRewardBase',
		name : 'reward.bigRewardBase',
		fieldLabel : '大奖基数',
		readOnly : true,
		anchor : "85%"
	})
	var handDate = new Ext.form.TextField({
		id : 'handedDate',
		fieldLabel : '交表时间',
		name : 'reward.handedDate',
		anchor : "85%",
		readOnly : true

	});

	var fillTime = new Ext.form.TextField({
		id : 'fillDate',
		fieldLabel : '填写时间',
		name : 'reward.fillDate',
		readOnly : true,
		value : getDate(),
		anchor : "85%"
	});

	function loadData() {
		fillBy.setValue(sessWorcode);
		fillName.setValue(sessWorname);
		/*Ext.get('fillBy').dom.value = sessWorcode;
		Ext.get('fillName').dom.value = sessWorname;*/
	};

	var bigRewardId = new Ext.form.Hidden({
		id : "bigRewardId",
		name : 'reward.bigRewardId'
	});

	var form = new Ext.form.FormPanel({
		labelAlign : 'right',
		region : 'south',
		border : true,
		items : [new Ext.form.FieldSet({
			title : '大奖发放主表信息',
			collapsible : true,
			height : '100%',
			layout : 'form',
			items : [{
				border : false,
				layout : 'column',
				items : [{
					columnWidth : .3,
					layout : 'form',
					border : false,
					labelWidth : 110,
					items : [bigRewardId, bigRewardMonth]
				}, {
					columnWidth : .3,
					layout : 'form',
					labelWidth : 110,
					border : false,
					items : [rewardName]
				}, {
					columnWidth : .3,
					layout : 'form',
					border : false,
					labelWidth : 110,
					items : [rewardBase]
				}, {
					columnWidth : .3,
					layout : 'form',
					labelWidth : 110,
					border : false,
					items : [handDate]
				}, {
					columnWidth : .3,
					layout : 'form',
					border : false,
					labelWidth : 110,
					items : [fillName, fillBy]
				}, {
					columnWidth : .3,
					layout : 'form',
					labelWidth : 110,
					border : false,
					items : [fillTime]
				}]
			}]
		})]
	});
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false
	});
	var myRecord = new Ext.data.Record.create([sm, {
		name : 'deptId',
		mapping : 0
	}, {
		name : 'deptCode',
		mapping : 1
	}, {
		name : 'deptName',
		mapping : 2
	}, {
		name : 'bigDetailId',
		mapping : 3
	}, {
		name : 'bigRewardId',
		mapping : 4
	}, {
		name : 'empCount',
		mapping : 5
	}, {
		name : 'bigRewardNum',
		mapping : 6
	}, {
		name : 'memo',
		mapping : 7
	}, {
		name : 'workFlowNo',
		mapping : 8
	}, {
		name : 'workFlowState',
		mapping : 9
	}]);

	// grid  开始 ---------------------------
	var ds = new Ext.data.JsonStore({
		url : 'hr/getBigRewardDetail.action',
		root : 'list',
		totalProperty : 'totalCount',
		fields : myRecord
	});

	ds.on("beforeload", function() {
		Ext.apply(this.baseParams, {
			mainId : id,
			workFlowState : ''
		});

	});

	var deptID = new Ext.form.Hidden({
		name : 'deptId'
	});

	var grid = new Ext.grid.EditorGridPanel({
		region : "center",
		//		layout : 'fit',

		height : 420,
		store : ds,
		columns : [sm, new Ext.grid.RowNumberer({
			header : '序号',
			width : 35
		}), {
			header : "ID",
			width : 75,
			sortable : true,
			dataIndex : 'bigDetailId',
			hidden : true
		}, {
			header : "deptId",
			width : 75,
			sortable : true,
			dataIndex : 'deptId',
			hidden : true
		}, {
			header : "部门",
			width : 200,
			align : 'center',
			value : "",
			sortable : true,
			dataIndex : 'deptName',
			editor : new Ext.form.ComboBox({
				fieldLabel : '部门',
				value : "请选择...",
				mode : 'remote',
				editable : false,
				readOnly : true,
				width : 100,
				onTriggerClick : function() {

					var args = {
						selectModel : 'single',
						rootNode : {
							id : "0",
							text : '灞桥热电厂'
						}
					}
					var url = "/power/comm/jsp/hr/dept/dept.jsp";
					var rvo = window
							.showModalDialog(
									url,
									args,
									'dialogWidth:250px;dialogHeight:400px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
					if (typeof(rvo) != "undefined") {
						var record = grid.getSelectionModel().getSelected();
						record.set("deptName", rvo.names);
						record.set("deptId", rvo.ids);
						deptID.setValue(rvo.ids);

					}

				}
			})

		}, {
			header : "人数",
			width : 175,
			sortable : true,
			dataIndex : 'empCount',
			editor : new Ext.form.TextField({
				allowBlank : false
			})
				/*,
							renderer : function(value, cellmeta, record, rowIndex,
									columnIndex, store) {
								if (rowIndex < store.getCount() - 1) {
									var empCount = record.data.empCount;
									// 强行触发renderer事件
									var totalSum = 0;
									for (var i = 0; i < store.getCount() - 1; i++) {
										totalSum += store.getAt(i).get('empCount');
									}
									if (store.getAt(store.getCount() - 1)
											.get('isNewRecord') == 'total') {
										store.getAt(store.getCount() - 1).set(
												'empCount', totalSum);
									}

									return moneyFormat(empCount);
								} else {
									totalSum = 0;
									for (var i = 0; i < store.getCount() - 1; i++) {
										totalSum += store.getAt(i).get('empCount');
									}
									return "<font color='red'>" + moneyFormat(totalSum)
											+ "</font>";
								}
							}*/
				}, {
					header : "大奖金额",
					width : 175,
					sortable : true,
					dataIndex : 'bigRewardNum',
					editor : new Ext.form.TextField({
						allowBlank : false
					})
				/*,
											renderer : function(value, cellmeta, record, rowIndex,
											columnIndex, store) {
										if (rowIndex < store.getCount() - 1) {
											var totalSum = 0;
											var bigRewardNum = record.data.bigRewardNum
											for (var i = 0; i < store.getCount() - 1; i++) {
												totalSum += store.getAt(i).get('bigRewardNum');
											}
											if (store.getAt(store.getCount() - 1)
													.get('isNewRecord') == 'total') {
												store.getAt(store.getCount() - 1).set(
														'bigRewardNum', totalSum);
											}
											return moneyFormat(bigRewardNum);
										} else {
											totalSum = 0;
											for (var i = 0; i < store.getCount() - 1; i++) {
												totalSum += store.getAt(i).get('bigRewardNum');
											}
											return "<font color='red'>" + moneyFormat(totalSum)
													+ "</font>";
										}
									}*/
				}, {
					header : "备注",
					width : 275,
					sortable : true,
					dataIndex : 'memo',
					editor : new Ext.form.TextField({
						allowBlank : true
					})
				}],
		sm : sm,
		autoSizeColumns : true,
		clicksToEdit : 1,
		viewConfig : {
			forceFit : false
		},
		tbar : [{
			id : 'add',
			text : "增加",
			iconCls : 'add',
			handler : addRecord
		}, {
			id : 'save',
			text : "保存",
			iconCls : 'save',
			handler : saveRecord
		}, {
			id : 'delete',
			text : "删除",
			iconCls : 'delete',
			handler : deleteRecord
		}, {
			id : 'cancer',
			text : "取消",
			iconCls : 'cancer',
			handler : cancel
		}, {
			id : 'report',
			text : "分发给各部门",
			hidden : true,
			iconCls : 'upcommit',
			handler : repactDept
		}, {	// add by qxjiao 20100727
			id : 'detail',
			text : "查看大奖明细",
			iconCls : 'query',
			disabled : false,
			handler : queryDetail
		}, '-', {
			id : 'btnApprove',
			iconCls : 'approve',
			text : "审批",
			handler : approveTheme
		}]
	});
	
	// add by qxjiao 20100727
		 function queryDetail() {

		var record = grid.getSelectionModel().getSelected();
		if (record == null) {
			Ext.Msg.alert("提示", "请选择一条记录！");
		} else {
			var arg = new Object();
			arg.deptName=record.get("deptName");
			arg.deptId = record.get("deptId");
			
			arg.bigRewardId =id  //  大奖主表Id
			
			
			window.showModalDialog('../bigRewardDetailQuery.jsp', arg,
					'status:no;dialogWidth=750px;dialogHeight=450px');
		}
	}
	function cancel() {
		ds.reload();
	}
	
	
	//审批 add by sychen 20100830
	function approveTheme() {
		Ext.Ajax.request({
			url : 'hr/appvoveBigAward.action',
			method : 'post',
			params : {
				bigRewardId : id
			},
			success : function(response, options) {
				Ext.Msg.alert("提示", "审批成功！");
				Ext.getCmp("div_tabs").setActiveTab(0);
				Ext.getCmp("div_grid").getStore().reload();
			},
			failure : function(response, options) {

				Ext.Msg.alert('错误', '审批时出现未知错误.');
			}
		})
	}
	
	// ↓↓****************员工验证窗口****************
	// 工号
	var workCode = new Ext.form.TextField({
				id : "workerCode",
				value : '999999',
				fieldLabel : '工号<font color ="red">*</font>',
				readOnly : true,
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
		modal : true,
		title : "请输入工号和密码",
		buttonAlign : "center",
		resizable : false,
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
								if (result) {
									var selected = sm.getSelections();
									var allDetailId = [];
									var allworkNo = [];
									for (var i = 0; i < selected.length; i += 1) {
										var member = selected[i].data;
										if (member.bigDetailId) {
											allDetailId.push(member.bigDetailId);
											allworkNo.push(member.workFlowNo);
										}
										Ext.Msg.confirm('提示', '确认要进行分发到部门吗？', function(button) {
											if (button == 'yes') {
												Ext.Ajax.request({
													url : 'hr/bigRewardDetailReport.action',
													method : 'post',
													params : {
														detailIds : allDetailId.join(","),
														rewardId : id
													},
													success : function(result, request) {
														var o = Ext.util.JSON
																.decode(result.responseText);
														validateWin.hide();
														ids = [];
														ds.load({
															params : {
																rewardId : id,
																workFlowState : '0'
															}
														});
														Ext.Msg.alert("提示信息", o.msg);
													},
													failure : function(result, request) {
														ids = [];
														Ext.Msg.alert("提示信息", "数据保存修改失败！");
													}
												})
						
											}
										})
									}
								} else {
									Ext.Msg.alert('错误', '用户名或密码输入不正确！');
								}
							}
						}, "workerCode=" + workCode.getValue() + "&loginPwd="
								+ workPwd.getValue());
			}
		}, {
			text : '取消',
			handler : function() {
				validateWin.hide();
			}
		}],
		listeners : {
			show : function(com) {
				Ext.lib.Ajax.request('POST',
						'comm/getCurrentSessionEmployee.action', {
							success : function(action) {
								var result = eval("(" + action.responseText
										+ ")");
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
	
	function repactDept() {
		if (grid.getSelectionModel().hasSelection()) {
			validateWin.show();
		} else {
			Ext.Msg.alert("提示信息", "请选择一条记录！");
		}
	}
	function init() {
		if (id != null && id != "") {

			queryRecord();
			Ext.get("add").dom.disabled = false;
			Ext.get("delete").dom.disabled = false;
			Ext.get("save").dom.disabled = false;
			Ext.get("cancer").dom.disabled = false;
		}

		else {

		}
	}

	function queryRecord() {
		ds.load();
	}

	function addRecord() {
		if (id == "" || id == null) {
			Ext.MessageBox.alert("提示", "请先选择一条主记录！");
			return;
		}
		var count = ds.getCount();
		var currentIndex = count;
		var o = new myRecord({
			'bigDetailId' : '',
			'bigRewardId' : id,
			'deptId' : '',
			'empCount' : '',
			'bigRewardNum' : '',
			'memo' : ''

		});
		ds.insert(currentIndex, o);
		grid.stopEditing();
		sm.selectRow(currentIndex);
		grid.startEditing(currentIndex, 1);

	}
	function saveRecord() {
		var alertMsg = "";
		grid.stopEditing();
		var modifyRec = grid.getStore().getModifiedRecords();
		if (modifyRec.length > 0) {
			Ext.Msg.confirm('提示', '确定要保存修改数据吗?', function(button) {
				if (button == 'yes') {
					var updateData = new Array();
					for (var i = 0; i < modifyRec.length; i++) {
						if (modifyRec[i].get("empCount") == null
								|| modifyRec[i].get("unionPerStandard") == "") {
							alertMsg += "人数不能为空</br>";
						}

						if (modifyRec[i].get("bigRewardNum") == null
								|| modifyRec[i].get("effectEndTime") == "") {
							alertMsg += "金额不能为空</br>";
						}

						if (alertMsg != "") {
							Ext.Msg.alert("提示", alertMsg);
							return;
						}
						updateData.push(modifyRec[i].data);
					}
					Ext.Ajax.request({
						url : 'hr/saveBigRewardDetail.action',
						method : 'post',
						params : {
							isUpdate : Ext.util.JSON.encode(updateData),
							rewardId : id

						},
						success : function(form, options) {
							var obj = Ext.util.JSON.decode(form.responseText)
							Ext.MessageBox.alert('提示信息', '保存成功！');
							ds.rejectChanges();
							ds.reload();

						},
						failure : function(result, request) {
							Ext.MessageBox.alert('提示信息', '操作失败！')
						}
					})
				} else {
					ds.rejectChanges();
					ds.reload();

				}
			})
		} else {
			Ext.Msg.alert("提示", "未做任何修改！");
			ds.rejectChanges();
			ds.reload();
		}
	}
	function deleteRecord() {
		var sm = grid.getSelectionModel();

		var selected = sm.getSelections();
		var ids = [];

		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i].data;
				if (member.unionPerId) {
					ids.push(member.unionPerId);
				}

			}

			if (ids.length > 0) {
				Ext.Msg.confirm("删除", "是否确定删除所选记录？", function(buttonobj) {
					if (buttonobj == "yes") {
						Ext.lib.Ajax.request('POST',
								'hr/delBigRewardDetail.action', {
									success : function(action) {
										Ext.Msg.alert("提示", "删除成功！")
										ds.reload();
									},
									failure : function() {
										Ext.Msg.alert('错误', '删除时出现未知错误.');
									}
								}, 'ids=' + ids);
					} else {
						ds.reload();
					}
				});
			}
		}
	}
	var layout = new Ext.Panel({
		layout : 'form',
		border : false,
		split : true,
		items : [grid, form]
	});

	return {
		layout : layout,
		grid : grid,
		form : form,
		loadData : loadData(),

		setFormRec : function(v) {
			var rec = v;
			form.getForm().loadRecord(v);
			fillName.setValue(v.get("fillByName"));
			fillBy.setValue(v.get("fillBy"));
			rewardName.setValue(v.get("bigAwardName"));
			id = v.get('bigRewardId');
			entryId = v.get('workFlowNo');
			getWorkCode();
			ds.load();
			method = "update";
			//			init();
		}
	}
}