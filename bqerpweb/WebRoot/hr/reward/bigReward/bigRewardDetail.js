Ext.ns("bigReward.queryList");

bigReward.queryList = function() {
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
				// name : 'fillName',
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
							fillName.setValue(sessWorname);
							fillBy.setValue(sessWorcode);

						}
					}
				});
	}
	// getWorkCode();
	var bigRewardMonth = new Ext.form.TextField({
				id : 'bigRewardMonth',
				fieldLabel : '月份',
				name : 'reward.bigRewardMonth',
				anchor : "85%",
				value : getYearMonth(),
				readOnly : true,
				listeners : {
					focus : function() {
						WdatePicker({
									isShowClear : true,
									startDate : '%y-%M',
									dateFmt : 'yyyy-MM'
								});
					}
				}

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
	// month:bigRewardMonth.getValue()
				}
			})
	function getTypeAndName() {
		Ext.Ajax.request({
					url : 'hr/getAllawardName.action',
					method : 'post',
					params : {
						awardID : rewardName.getValue()
					},
					success : function(action) {
						var result = eval("(" + action.responseText + ")");

						if (result.list[0] != null) {
							rewardBase.setValue(result.list[0][2]);

						}
					},
					failure : function(result, request) {
						Ext.MessageBox.alert('提示信息', '操作失败！')
					}
				})
	}
	var rewardName = new Ext.form.ComboBox({
				id : 'bigAwardName',
				fieldLabel : "大奖名称",
				store : nameStrore,
				displayField : "bigAwardName",
				valueField : "bigAwardId",
				hiddenName : 'reward.bigAwardId',
				mode : 'local',
				triggerAction : 'all',
				value : '',
				readOnly : false,
				anchor : "85%",
				listeners : {
					"select" : function() {
						getTypeAndName();

					}
				}
			})

	var rewardBase = new Ext.form.TextField({
				id : 'bigRewardBase',
				name : 'reward.bigRewardBase',
				fieldLabel : '大奖基数',
				readOnly : false,
				anchor : "85%"
			})
	var handDate = new Ext.form.TextField({
				id : 'handedDate',
				fieldLabel : '交表时间',
				name : 'reward.handedDate',
				anchor : "85%",
				readOnly : true,
				listeners : {
					focus : function() {
						WdatePicker({
									isShowClear : true,
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd'
								});
					}
				}

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
		/*
		 * Ext.get('fillBy').dom.value = sessWorcode;
		 * Ext.get('fillName').dom.value = sessWorname;
		 */
	};

	var formtbar = new Ext.Toolbar({
		items : [{
			id : 'btnSave',
			text : "保存",
			iconCls : 'save',
			handler : function() {
				if (!form.getForm().isValid()) {
					return false;
				}
				if (bigRewardMonth.getValue() == null
						|| bigRewardMonth.getValue() == "") {
					Ext.Msg.alert('提示', '请选择月份！');
					return;
				}
				if (rewardName.getValue() == null
						|| rewardName.getValue() == "") {
					Ext.Msg.alert('提示', '请选择大奖名称！');
					return;
				}
				if (rewardBase.getValue() == null
						|| rewardBase.getValue() == "") {
					Ext.Msg.alert('提示', '请填写大奖基数！');
					return;
				}
				if (handDate.getValue() == null || handDate.getValue() == "") {
					Ext.Msg.alert('提示', '请选择交表时间！');
					return;
				}
				form.getForm().submit({
					url : 'hr/saveBigReward.action',
					method : 'post',
					params : {
						method : method,
						rewardname : rewardName.getRawValue()
					},
					success : function(form, action) {
						if (method == "add") {
							var message = eval('('
									+ action.response.responseText + ')');
							Ext.Msg.alert("提示", message.msg);
							if (message.msg.indexOf('成功') != -1) {
								id = message.bigRewardId;
								queryRecord();
								Ext.get('bigRewardId').dom.value = message.bigRewardId;
								method = "update";
								Ext.getCmp("buildDetail").enable();
								Ext.getCmp("btnAdd").enable();
								Ext.getCmp("delete").enable();
								Ext.getCmp("save").enable();
								Ext.getCmp("cancer").enable();
								Ext.getCmp("report").enable();
							}
						} else {
							Ext.Msg.alert("成功", "保存成功");
						}
					},
					failure : function() {
						Ext.Msg.alert('提示', '操作失败，请联系管理员！');
					}
				})
			}
		}, '-', {
			text : "删除",
			iconCls : 'delete',
			handler : function() {
				if (id == "") {
					Ext.MessageBox.alert('提示', '请选择需删除的记录!');
					return false;
				}
				Ext.Msg.confirm("删除", "是否确定删除所选记录？", function(buttonobj) {
							if (buttonobj == "yes") {
								Ext.lib.Ajax.request('POST',
										'hr/delBigReward.action', {
											success : function(action) {
												Ext.Msg.alert("提示", "删除成功！")
												getWorkCode();
												form.getForm().reset();
												ds.reload();
												method = "add";
												id = "";
												entryId = "";
												Ext.MessageBox.alert('提示',
														'删除成功!');
											},
											failure : function() {
												Ext.Msg.alert('错误',
														'删除时出现未知错误.');
											}
										}, 'id=' + id);
							} else {
								ds.reload();
							}
						});
			}
		}, '-', {
			id : 'btnReport',
			text : "上报",
			iconCls : 'upcommit',
			handler : function() {
				if (id == "") {
					Ext.MessageBox.alert('提示', '请从列表中选择一条需上报的记录!');
					return false;
				}
				Ext.Ajax.request({
							url : 'hr/bigRewardReport.action',
							method : 'post',
							params : {
								bigRewardId : id
							},
							success : function(result, request) {
								var o = Ext.util.JSON
										.decode(result.responseText);
								form.getForm().reset();
								id = "";
								ds.removeAll();
								grid.getView().refresh();
								Ext.Msg.alert("提示信息", o.msg);
							},
							failure : function(result, request) {
								Ext.Msg.alert("提示信息", "数据保存修改失败！");
							}
						})
			}
		}]
	});

	var bigRewardId = new Ext.form.Hidden({
				id : "bigRewardId",
				name : 'reward.bigRewardId'
			});

	var form = new Ext.form.FormPanel({
				// bodyStyle : "padding:5px 5px 0",
				labelAlign : 'right',
				// autoHeight : true,
				region : 'center',
				// layout:'border',
				border : true,
				tbar : formtbar,
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
											items : [bigRewardId,
													bigRewardMonth]
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
	var record = new Ext.data.Record.create([{
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
	// grid 开始 ---------------------------
	var ds = new Ext.data.JsonStore({
				url : 'hr/getBigRewardDetail.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : record
			});
	ds.on("beforeload", function() {
				Ext.apply(this.baseParams, {
							mainId : id/*,
							workFlowState : '0'*/ //modify by wpzhu 20100819
						});

			}); 
	function addLine() { 
		// 统计行
		
		var newRecord = new Ext.data.Record({
					'bigDetailId' : '',
					'bigRewardId' : '',
					'deptId' : '',
					'deptName' : '合计',
					'empCount' : '',
					'bigRewardNum' : '',
					'memo' : '',
					'isNewRecord' :  'total'
				}); 
		// 原数据个数
		var count = ds.getCount();
		// 停止原来编辑
		grid.stopEditing();
		// 插入统计行
		ds.insert(count, newRecord);
		grid.getView().refresh();
		Ext.Msg.hide();
	};
	
	ds.on("load",addLine);

	var detailDept = new Power.dept({
				anchor : '100%'
			});
	detailDept.btnConfrim.on("click", function() {
				if (grid.getSelectionModel().hasSelection()) {
					var rec = grid.getSelectionModel().getSelected();
					var aDept = detailDept.getValue();
					var flag = 0;
					for (var i = 0; i < ds.getCount(); i++) {
						if (ds.getAt(i).data.deptId == aDept.key) {
							flag = "1";
							Ext.Msg.alert("提示", "不能重复选择部门!");
						}
					}
					if (flag == 0) {
						rec.set('deptId', aDept.key);
						rec.set('deptName', aDept.value);
					}
				}
			});
			
			
	// ↓↓****************员工验证窗口****************
	// 工号
	var workCode = new Ext.form.TextField({
				id : "workerCode",
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
														ds.reload();
														/*ds.load({
															params : {
																mainId : id,
																workFlowState : '0'  //modify by wpzhu 20100819
															}
														});*/
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
			// modify by wpzhu 20100819
			var res = grid.getSelectionModel().getSelections();
			var flag = false;
			for (i = 0; i < res.length; i++) {
				record = res[i];
				if (record.get("workFlowState") == "1"
						|| record.get("workFlowState") == "2"
						|| record.get("workFlowState") == "3"
						|| record.get("workFlowState") == "4")
					flag = true;
			}

			if (flag) {

				Ext.Msg.alert("提示信息", "请选择所有未分发的记录！");
				return;

			} else if(record.get("workFlowState") == "0"){
                
				validateWin.show();
			}

		} else {
			Ext.Msg.alert("提示信息", "请选择一条记录！");
		}
	}

	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false
	});
	var grid = new Ext.grid.EditorGridPanel({
				region : "center",
				layout : 'fit',
				height : 1000,
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
							width : 250,
							align : 'center',
							value : "",
							sortable : true,
							dataIndex : 'deptName',
							editor : detailDept.combo
						}, {
							header : "人数",
							width : 150,
							align :'center',
							sortable : true,
							dataIndex : 'empCount',
							editor : new Ext.form.NumberField({
										allowBlank : false,
										allowDecimals : false,
										allowNegative : false,
										decimalPrecision : 0
									}),
							renderer : function(value, cellmeta, record,
									rowIndex, columnIndex, store) {
								if (rowIndex < store.getCount() - 1) {
									var empCount = record.data.empCount; // 强行触发renderer事件
									// var
									totalSum = 0;
									for (var i = 0; i < store.getCount() - 1; i++) {
										totalSum += store.getAt(i)
												.get('empCount');
									}
									if (store.getAt(store.getCount() - 1)
											.get('isNewRecord') == 'total') {
										store.getAt(store.getCount() - 1).set(
												'empCount', totalSum);
									}
									return empCount;
								} else {
									totalSum = 0;
									for (var i = 0; i < store.getCount() - 1; i++) {
										totalSum += store.getAt(i)
												.get('empCount');
									}
									return "<font color='red'>" + totalSum
											+ "</font>";
								}
							}

						}, {
							header : "大奖金额",
							width : 150,
							align :'center',
							sortable : true,
							dataIndex : 'bigRewardNum',
							editor : new Ext.form.NumberField({
										allowBlank : false,
										decimalPrecision : 2
									}),
							renderer : function(value, cellmeta, record,
									rowIndex, columnIndex, store) {
								if (rowIndex < store.getCount() - 1) {
									var totalSum = 0;
									var bigRewardNum = record.data.bigRewardNum
									for (var i = 0; i < store.getCount() - 1; i++) {
										totalSum += store.getAt(i)
												.get('bigRewardNum');
									}
									if (store.getAt(store.getCount() - 1)
											.get('isNewRecord') == 'total') {
										store.getAt(store.getCount() - 1).set(
												'bigRewardNum', totalSum);
									}
									return bigRewardNum;
								} else {
									totalSum = 0;
									for (var i = 0; i < store.getCount() - 1; i++) {
										totalSum += store.getAt(i)
												.get('bigRewardNum');
									}
									return "<font color='red'>" + totalSum
											+ "</font>";
								}
							}

						}, {
							header : "备注",
							width : 350,
							sortable : true,
							dataIndex : 'memo',
							editor : new Ext.form.TextField({
										allowBlank : true
									})
						},{//add by wpzhu 20100819

							header : "状态",
							width : 180,
							sortable : true,
							dataIndex : 'workFlowState',
							renderer:function (v)
							{
								
								if(v=="0")
								{
								return "未分发"
								}
								else  if(v=="1")
								{
								return "已分发"
								}else  if(v=="2")
								{
									return "部门已查阅";
								}else  if(v=="3")
								{
									return "已分发到班组";
								}
								else  if(v=="4")
								{
									return "审批结束";
								}
							}
					
						}],
				sm : sm,
				autoSizeColumns : true,
				clicksToEdit : 1,
				viewConfig : {
					forceFit : false
				},
				tbar : [{
							text : "生成明细",
							id : 'buildDetail',
							iconCls : 'add',
							disabled : true,
							handler : init
						}, {
							id : 'btnAdd',
							text : "新增",
							iconCls : 'add',
							disabled : true,
							handler : addRecord
						}, {
							id : 'save',
							text : "保存",
							iconCls : 'save',
							disabled : true,
							handler : saveRecord
						}, {
							id : 'delete',
							text : "删除",
							iconCls : 'delete',
							disabled : true,
							handler : deleteRecord
						}, {
							id : 'cancer',
							text : "取消",
							iconCls : 'cancer',
							disabled : true,
							handler : cancel
						},{//add by wpzhu 20100724
							id : 'detail',
							text : "查看大奖明细",
							iconCls : 'query',
							disabled : false,
							handler : queryDetail
						}, {
							id : 'report',
							text : "分发给各部门",
							iconCls : 'upcommit',
							disabled : true,
							handler : repactDept
						}]
			});

	grid.on('beforeedit', checkIsEdit);
	
	 function queryDetail() {

		var record = grid.getSelectionModel().getSelected();
		if (record == null) {
			Ext.Msg.alert("提示", "请选择一条记录！");
		} else {
			var arg = new Object();
			arg.deptName=record.get("deptName");
			arg.deptId = record.get("deptId");
			
			arg.bigRewardId =id  //  大奖主表Id
			
			
			window.showModalDialog('bigRewardDetailQuery.jsp', arg,
					'status:no;dialogWidth=750px;dialogHeight=450px');
		}
	}
	
	/**
	 * 判断是否是最后行，如果是则不能编辑
	 */
	function checkIsEdit(obj) {
		if (obj.row == ds.getCount() - 1) {
			obj.cancel = true;
		}
	}

	// grid.on('rowdblclick', function(grid, rowIndex, e) {
	// var record = grid.getSelectionModel().getSelected();
	// var sm = grid.getSelectionModel();
	// if (grid.selModel.hasSelection()) {
	// var bigDetailId = record.get("bigDetailId");
	// var url = "attendanceInfo.jsp";
	// var args = new Object();
	// args.bigDetailId = bigDetailId;
	// var location = window
	// .showModalDialog(
	// url,
	// args,
	// 'dialogWidth=750px;dialogHeight=500px;center=yes;help=no;resizable=no;status=no;');
	//
	// } else {
	// Ext.Msg.alert("提示", "请选择要打印的物资!");
	// }
	//
	// });

	function cancel() {
		ds.load();
	}
	function init() {
		if (id != null && id != "") {
			//add by kzhang 2010727
			Ext.Msg.wait("正在生成，请稍等....");
			Ext.Ajax.request({
						url : 'hr/buildBigRewardDetailData.action',
						method : 'post',
						params : {
							bigRewardId : id,
							rewardMonth : bigRewardMonth.getValue(),
							bigRewardBase : rewardBase.getValue()
						},
						success : function(result, request) {
							var o = Ext.util.JSON.decode(result.responseText);
							ds.load();
							Ext.get("add").dom.disabled = false;
							Ext.get("delete").dom.disabled = false;
							Ext.get("save").dom.disabled = false;
							Ext.get("cancer").dom.disabled = false;
							
							Ext.Msg.alert("提示信息", o.msg);
							
						},
						failure : function(result, request) {
							Ext.Msg.alert("提示信息", "数据保存修改失败！");
						}
					})
			// grid.getTopToolbar().setDisabled(false);
		} else {

			/*
			 * grid.getTopToolbar().setDisabled(true);
			 * Ext.get("add").dom.disabled = true;
			 * Ext.get("delete").dom.disabled = true;
			 * Ext.get("save").dom.disabled = true;
			 * Ext.get("cancer").dom.disabled = true;
			 */
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
		var o = new record({
					'bigDetailId' : '',
					'bigRewardId' : id,
					'deptId' : '',
					'deptName' : '',
					'empCount' : '',
					'bigRewardNum' : '',
					'memo' : '',
					'isNewRecord' : true

				});
		ds.insert(currentIndex - 1, o);
		grid.stopEditing();
		sm.selectRow(currentIndex - 1);
		grid.startEditing(currentIndex - 1, 1);

	}
	function saveRecord() {
		var alertMsg = "";
		grid.stopEditing();
		var modifyRec = grid.getStore().getModifiedRecords();
		if (modifyRec.length > 1) {
			Ext.Msg.confirm('提示', '确定要保存修改数据吗?', function(button) {
				if (button == 'yes') {
					var updateData = new Array();
					for (var i = 0; i < modifyRec.length; i++) {
						if (modifyRec[i].get('isNewRecord') != "total") {
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
					}
					Ext.Ajax.request({
								url : 'hr/saveBigRewardDetail.action',
								method : 'post',
								params : {
									isUpdate : Ext.util.JSON.encode(updateData),
									rewardId : id,
									ids : ids.join(",")
								},
								success : function(form, options) {
									var obj = Ext.util.JSON
											.decode(form.responseText)
									Ext.MessageBox.alert('提示信息', '保存成功！');
									ds.rejectChanges();
									ds.load();
									ids = [];
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
			ds.load();
		}
	}
	// 删除记录
	var ids = new Array();
	function deleteRecord() {
		grid.stopEditing();
		var sm = grid.getSelectionModel();

		var selected = sm.getSelections();
		var ids = [];

		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selected.length; i++) {
				var member = selected[i].data;
				if (member.bigDetailId) {
					ids.push(member.bigDetailId);
				}
			}
			if (ids.length > 0) {
				Ext.Msg.confirm("删除", "是否确定删除所选记录？", function(buttonobj) {
							if (buttonobj == "yes") {
								Ext.lib.Ajax.request('POST',
										'hr/deleteBigRewardDetail.action', {
											success : function(action) {
												Ext.Msg.alert("提示", "删除成功！")
												store.reload();
											},
											failure : function() {
												Ext.Msg.alert('错误',
														'删除时出现未知错误.');
											}
										}, 'ids=' + ids);
							} else {
								store.reload();
							}
						});
			}
		}
	}
	var layout = new Ext.Panel({
				layout : 'form',
				border : false,
				split : true,
				items : [form, grid]
			});

	return {
		layout : layout,
		grid : grid,
		form : form,
		loadData : loadData(),
		setFeeDetailBtn : function(b) {
			if (b) {
				Ext.getCmp("buildDetail").enable();
				Ext.getCmp("btnAdd").enable();
				Ext.getCmp("delete").enable();
				Ext.getCmp("save").enable();
				Ext.getCmp("cancer").enable();
				Ext.getCmp("report").enable();
			} else {
				Ext.getCmp("buildDetail").disable();
				Ext.getCmp("btnAdd").disable();
				Ext.getCmp("delete").disable();
				Ext.getCmp("save").disable();
				Ext.getCmp("cancer").disable();
				Ext.getCmp("report").disable();
			}
		},
		setFormRec : function(v) {
			var rec = v;
			form.getForm().loadRecord(v);
			fillName.setValue(v.get("fillByName"));
			fillBy.setValue(v.get("fillBy"));
			rewardName.setValue(v.get("bigAwardId"));
			id = v.get('bigRewardId');
			entryId = v.get('workFlowNo');
			// getWorkCode();
			ds.load()
			method = "update";
		},
		resetFormRec : function() {
			getWorkCode();
			form.getForm().reset();
			ds.removeAll();
			id = "";
			method = "add";
		}
	}
}