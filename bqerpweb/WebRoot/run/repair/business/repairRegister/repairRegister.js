Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
function getYear() {
	var d, s, t;
	d = new Date();
	s = d.getFullYear().toString(10);
	return s;
}
function getDate() {
	var d, s, t;
	d = new Date();
	s = d.getFullYear().toString(10) + "-";
	t = d.getMonth() + 1;
	s += (t > 9 ? "" : "0") + t + "-";
	t = d.getDate();
	s += (t > 9 ? "" : "0") + t + " ";
	t = d.getHours();
	s += (t > 9 ? "" : "0") + t + ":";
	t = d.getMinutes();
	s += (t > 9 ? "" : "0") + t + ":";
	t = d.getSeconds();
	s += (t > 9 ? "" : "0") + t;
	return s;
}

var myMate = null;
var myWin;
var res;
var winSpecial;
var winVersion;
Ext.onReady(function() {

	var id = getParameter("id");
	var revoke = getParameter("revoke");
	var situationProject = getParameter("situationProject");

	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';

	var method = "add";
	var sessWorname;
	var sessWorcode;
	var entryId;
	var fillBy;
	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
			success : function(action) {
				var result = eval("(" + action.responseText + ")");
				if (result.workerCode) {
					// 设定默认工号
					sessWorcode = result.workerCode;
					sessWorname = result.workerName;
					loadData();
				}
			}
		});
	}
	function loadData() {
		fillBy = sessWorcode;
		Ext.get('fillName').dom.value = sessWorname;
		annextbar.setDisabled(true);
	};

	var typeStore = new Ext.data.JsonStore({
		url : 'managecontract/getConCurrencyList.action',
		root : 'list',
		fields : ['currencyId', 'currencyName']
	})
	typeStore.load();

	var projectMainId = new Ext.form.Hidden({
		id : 'projectMainId',
		name : 'main.projectMainId'
	});

	var projectMainYear = new Ext.form.TextField({
		id : 'projectMainYear',
		name : 'main.projectMainYear',
		fieldLabel : '年度',
		readOnly : true,
		anchor : "85%",
		value : getYear(),
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y',
					alwaysUseStartDate : false,
					dateFmt : 'yyyy',
					isShowClear : false,
					onpicked : function(v) {
						this.blur();
					}
				});
			}
		}
	});

	var storeRepairType = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'manageplan/getRepairType.action'
		}),
		reader : new Ext.data.JsonReader({
			root : 'list'
		}, [{
			name : 'repairTypeId',
			mapping : 0
		}, {
			name : 'repairTypeName',
			mapping : 1
		}])
	});

	var cbxRepairType = new Ext.form.ComboBox({
		id : 'cbxRepairType',
		fieldLabel : "检修类别",
		store : storeRepairType,
		displayField : "repairTypeName",
		valueField : "repairTypeId",
		hiddenName : 'main.repairTypeId',
		mode : 'remote',
		triggerAction : 'all',
		value : '',
		readOnly : true,
		anchor : "85%"
	})
	var storeRepairSpecail = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'manageplan/getRepairSpecialityType.action'
		}),
		reader : new Ext.data.JsonReader({
			root : 'list'
		}, [{
			name : 'specialityId',
			mapping : 'specialityId'
		}, {
			name : 'specialityName',
			mapping : 'specialityName'
		}])
	});
	var cbxRepairSpecail = new Ext.form.ComboBox({
		id : 'specialityId',
		fieldLabel : "检修专业",
		store : storeRepairSpecail,
		displayField : "specialityName",
		valueField : "specialityId",
		hiddenName : 'main.specialityId',
		mode : 'remote',
		triggerAction : 'all',
		value : '',
		readOnly : true,
		anchor : "85%"
	})

	var tasklistId = {
		fieldLabel : '任务单',
		name : 'tasklistId',
		xtype : 'combo',
		id : 'tasklistId',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		hiddenName : 'main.tasklistId',
		allowBlank : false,
		editable : false,
		anchor : "85%",
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "selectRepair.jsp?sepeciality="
					+ Ext.get('specialityId').dom.value;
			var args = {
				selectModel : 'single',
				notIn : "'999999'",
				rootNode : {
					id : '-1',
					text : '合肥电厂'
				}
			}
			var emp = window
					.showModalDialog(
							url,
							args,
							'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');

			if (typeof(emp) != "undefined") {
				Ext.getCmp('tasklistId').setValue(emp.id);
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('tasklistId'), emp.name);
			}
		}
	};

	var fillName = new Ext.form.TextField({
		id : 'fillName',
		fieldLabel : '填写人',
		name : 'fillName',
		anchor : "85%",
		readOnly : true

	});
	var fillBy = new Ext.form.Hidden({
		id : "fillBy",
		name : 'main.fillBy'
	});
	var fillTime = new Ext.form.TextField({
		id : 'fillTime',
		fieldLabel : '填写时间',
		name : 'main.fillTime',
		readOnly : true,
		value : getDate(),
		anchor : "85%"
	});

	var memo = new Ext.form.TextArea({
		id : 'memo',
		fieldLabel : "备注",
		name : 'main.memo',
		height : 50,
		anchor : "92%"
	});

	var situationProjectBox = new Ext.form.ComboBox({
		fieldLabel : '是否需要检修，物业公司落实项目',
		store : new Ext.data.SimpleStore({
			fields : ['value', 'text'],
			data : [['Y', '是'], ['N', '否']]
		}),
		id : 'situationProject',
		name : 'situationProject',
		valueField : "value",
		displayField : "text",
		value : 'Y',
		mode : 'local',
		typeAhead : true,
		forceSelection : true,
		hiddenName : 'main.situationProject',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : false,
		anchor : "85%"
	});

	var formtbar = new Ext.Toolbar({
		items : [{
			id : 'btnAdd',
			text : "新增",
			iconCls : 'add',
			handler : function() {
				form.getForm().reset();
				Ext.get('btnSave').dom.disabled = false
				Ext.get('btnDelete').dom.disabled = false;
				Ext.get('btnReport').dom.disabled = true;// modify by wpzhu
				loadData();
				method = "add";
				id = "";
				entryId = "";
				situationProject = "";
				annex_ds.load({
					params : {
						repairMainId : 0
					// 清空明细
					}
				})
				getWorkCode();
			}
		}, '-', {
			id : 'btnSave',
			text : "保存",
			iconCls : 'save',
			handler : function() {
				if (!form.getForm().isValid()) {
					return false;
				}
				if (!checkInput())
					return;
				form.getForm().submit({
					url : 'manageplan/saveRepairRecord.action',
					method : 'post',
					params : {
						method : method
					},
					success : function(form, action) {
						parent.iframe1.document.getElementById("btnSubmit")
								.click();
						if (method == "add") {
							var message = eval('('
									+ action.response.responseText + ')');
							parent.iframe1.document.getElementById("btnSubmit")
									.click();
							id = message.data.projectMainId;
							Ext.get('projectMainId').dom.value = message.data.projectMainId;
							method = "update";
							annextbar.setDisabled(false);

							Ext.Msg.alert("成功", message.msg);
						} else {
							annextbar.setDisabled(false);
							situationProject = situationProjectBox.getValue();
							situation = situationProjectBox.getValue();
							Ext.Msg.alert("成功", "保存成功");
						}
					},
					failure : function() {
						Ext.Msg.alert('提示', '操作失败，请联系管理员！');
					}
				})
			}
		}, '-', {
			id : 'btnDelete',
			text : "删除",
			iconCls : 'delete',
			handler : function() {
				if (id == "") {
					Ext.MessageBox.alert('提示', '请选择需删除的记录!');
					return false;
				}
				Ext.Msg.confirm('提示', '删除的数据您将不能恢复,确定要删除吗?', function(b) {
					if (b == "yes") {
						Ext.Ajax.request({
							url : 'manageplan/deleteRepairRecord.action',
							params : {
								ids : id
							},
							method : 'post',
							waitMsg : '正在删除数据...',
							success : function(result, request) {
								parent.iframe1.document
										.getElementById("btnSubmit").click();
								loadData();
								form.getForm().reset();
								method = "add";
								id = "";
								annex_ds.load({
									params : {
										repairMainId : 0
									// 清空明细
									}
								})
								getWorkCode()
								Ext.MessageBox.alert('提示', '删除成功!');
							},
							failure : function(result, request) {
								Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
							}
						});
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
				var url = 'reportSign.jsp'
				var args = new Object();
				args.entryId = entryId;
				args.approveId = id;
				args.sepeciality = Ext.get('specialityId').dom.value;
				// function fillsituation()//modify by wpzhu 10/05/24
				// {
				// Ext.Ajax.request({
				// url : 'manageplan/findSituation.action',
				// params : {
				// id : id
				// },
				// method : 'post',
				// success : function(result, request) {
				// res = result.responseText;//后台直接返回字符串
				// alert(res)
				// return res;
				// },
				// failure : function(result, request) {
				// Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
				// }
				// });
				// }
				// fillsituation();
				/*
				 * if (situationProject == "" || situationProject == null) {
				 * args.situationProject = situationProjectBox.getValue(); }
				 * else { if(situationProjectBox.getValue()!=situationProject) {
				 * args.situationProject=situationProjectBox.getValue(); }else {
				 * args.situationProject = situationProject; } }
				 */
				// alert(res)
				args.situationProject = situationProjectBox.getValue();
				args.flowCode = "bqRepairPlanApprove";
				var o = window.showModalDialog(url, args,
						'status:no;dialogWidth=800px;dialogHeight=550px');
				if (o) {
					// parent.iframe1.document.getElementById("btnQuery").click();
					form.getForm().reset();
					method = "add";
					id = "";
					entryId = "";
					annex_ds.load();
					getWorkCode();
					var _url1 = "run/repair/business/repairRegister/repairRegList.jsp"
					parent.Ext.getCmp("maintab").setActiveTab(0);
					parent.document.all.iframe1.src = _url1;
				}
			}
		}]
	});
	var form = new Ext.form.FormPanel({
		bodyStyle : "padding:5px 5px 0",
		labelAlign : 'right',
		width : Ext.get('div_lay').getWidth(),
		autoHeight : true,
		region : 'center',
		border : false,
		tbar : formtbar,
		items : [new Ext.form.FieldSet({
			title : '检修项目基本信息',
			collapsible : true,
			height : '100%',
			layout : 'form',
			items : [{
				border : false,
				layout : 'column',
				items : [{
					columnWidth : .5,
					layout : 'form',
					border : false,
					labelWidth : 110,
					items : [projectMainId, projectMainYear]
				}, {
					columnWidth : .5,
					layout : 'form',
					labelWidth : 110,
					border : false,
					items : [cbxRepairType]
				}, {
					columnWidth : .5,
					layout : 'form',
					border : false,
					labelWidth : 110,
					items : [cbxRepairSpecail]
				}, {
					columnWidth : .5,
					layout : 'form',
					labelWidth : 110,
					border : false,
					items : [tasklistId]
				}, {
					columnWidth : .5,
					layout : 'form',
					border : false,
					labelWidth : 110,
					items : [fillBy, fillName]
				}, {
					columnWidth : .5,
					layout : 'form',
					labelWidth : 110,
					border : false,
					items : [fillTime]
				}, {
					columnWidth : .5,
					layout : 'form',
					labelWidth : 110,
					border : false,
					items : [situationProjectBox]
				}, {
					border : false,
					layout : 'form',
					columnWidth : 1,
					labelWidth : 110,
					items : [memo]
				}]
			}]
		})]
	});

	function addDetailRecord() {
		// modify by fyyang 20100517
		var count = annex_ds.getCount();
		var currentIndex = count;
		var o = new annex_item({
			'projectDetailId' : '',
			'projectMainId' : id,
			'repairProjectId' : '',
			'repairProjectName' : '',
			'workingCharge' : '',
			'workingChargeName' : '',
			'workingMenbers' : '',
			'workingTime' : '',
			'standard' : '2',
			'material' : 'Y',
			'startDate' : '',
			'endDate' : '',
			'acceptanceSecond' : '',
			'acceptanceSecondName' : '',
			'acceptanceThree' : '',
			'acceptanceThreeName' : ''
		});
		annex_ds.insert(currentIndex, o);
		annexGrid.stopEditing();
		annex_sm.selectRow(currentIndex);
		annexGrid.startEditing(currentIndex, 1);
	}

	var annextbar = new Ext.Toolbar({
		id : 'annextbar',
		items : ['明细：', {
			id : 'btnDetailAdd',
			text : "新增",
			iconCls : 'add',
			handler : addDetailRecord
		}, '-', {
			id : 'btnDetailSave',
			text : "保存",
			iconCls : 'save',
			handler : function() {
				annexGrid.stopEditing();
				var alertMsg = "";
				var modifyRec = annexGrid.getStore().getModifiedRecords();
				for (var i = 0; i < modifyRec.length; i++) {// add by wpzhu
					// 20100603
					if (modifyRec[i].get("repairProjectName") == null
							|| modifyRec[i].get("repairProjectName ") == "") {
						alertMsg += " 项目名称不能为空</br>";
					}
					if (modifyRec[i].get("workingChargeName") == null
							|| modifyRec[i].get("workingChargeName") == "") {
						alertMsg += "工作负责人不能为空</br>";
					}
					if (modifyRec[i].get("workingMenbers") == null
							|| modifyRec[i].get("workingMenbers") == "") {
						alertMsg += "工作成员不能为空</br>";
					}
					if (modifyRec[i].get("startDate") == null
							|| modifyRec[i].get("startDate") == "") {
						alertMsg += "计划开始日期不能为空</br>";
					}
					if (modifyRec[i].get("endDate") == null
							|| modifyRec[i].get("endDate") == "") {
						alertMsg += "计划结束日期不能为空</br>";
					}
					if (modifyRec[i].get("standard") == null
							|| modifyRec[i].get("standard") == "") {
						alertMsg += "验收标准不能为空</br>";
					}
					if (modifyRec[i].get("material") == null
							|| modifyRec[i].get("material") == "") {
						alertMsg += "是否落实材料不能为空</br>";
					}
					//======================add by drdu 20100612===================
					if (modifyRec[i].get("standard") == '2') {
						if (modifyRec[i].get("acceptanceSecondName") == null
								|| modifyRec[i].get("acceptanceSecondName") == "") {
							alertMsg += "二级验收人不能为空</br>";
						}
					} else if (modifyRec[i].get("standard") == '3') {
						if (modifyRec[i].get("acceptanceSecondName") == null
								|| modifyRec[i].get("acceptanceSecondName") == "") {
							alertMsg += "二级验收人不能为空</br>";
						}
						if (modifyRec[i].get("acceptanceThreeName") == null
								|| modifyRec[i].get(" acceptanceThreeName ") == "") {
							alertMsg += "三级验收人不能为空</br>";
						}
					}
					//======================add by drdu 20100612  end===================
					if (alertMsg != "") {
						Ext.Msg.alert("提示", alertMsg);
						return;
					}
				}
				if (modifyRec.length > 0) {
					Ext.Msg.confirm('提示', '确定要保存修改数据吗?', function(button) {
						if (button == 'yes') {
							var updateData = new Array();
							for (var i = 0; i < modifyRec.length; i++) {
								updateData.push(modifyRec[i].data);
							}
							Ext.Ajax.request({
								url : 'manageplan/saveOrUpdateRecord.action',
								method : 'post',
								params : {
									isUpdate : Ext.util.JSON.encode(updateData)
								},
								success : function(form, options) {
									var obj = Ext.util.JSON
											.decode(form.responseText)
									Ext.MessageBox.alert('提示', '保存成功！')
									Ext.get('btnReport').dom.disabled = false;// add
									// by
									// wpzhu
									annex_ds.rejectChanges();
									annex_ds.load({
										params : {
											repairMainId : id
										}
									});
								},
								failure : function(result, request) {
									Ext.MessageBox.alert('提示信息', '操作失败！')
								}
							})
						}
					})
				} else {
					Ext.MessageBox.alert('提示信息', '没有做任何修改！')
				}
			}
		}, '-', {
			id : 'btnAnnexDelete',
			text : "删除",
			iconCls : 'delete',
			handler : function() {
				var sm = annexGrid.getSelectionModel();
				var selected = sm.getSelections();
				var ids = [];
				if (selected.length == 0) {
					Ext.Msg.alert("提示", "请选择要删除的记录！");
				} else {
					for (var i = 0; i < selected.length; i += 1) {
						var member = selected[i].data;
						if (member.projectDetailId) {
							ids.push(member.projectDetailId);
						} else {
							annex_ds.remove(store.getAt(i));
						}
					}
					Ext.Msg.confirm("删除", "是否确定删除所选的记录？", function(buttonobj) {
						if (buttonobj == "yes") {
							Ext.lib.Ajax.request('POST',
									'manageplan/deleteRepairDetail.action', {
										success : function(action) {
											Ext.Msg.alert("提示", "删除成功！")
											annex_ds.reload();
										},
										failure : function() {
											Ext.Msg.alert('错误', '删除时出现未知错误.');
										}
									}, 'ids=' + ids);
						}
					});
				}

			}
		}, '-', {
			id : 'btnSelect',
			text : "调用上次版本检修项目",
			iconCls : 'add',
			handler : addHisRecord
		}, '-', {
			id : 'btnReflesh',
			text : "刷新",
			iconCls : 'reflesh',
			handler : function() {
				annex_ds.reload();
				annex_ds.rejectChanges();
			}
		}]
	});
	var annex_item = Ext.data.Record.create([{
		name : 'projectDetailId',
		mapping : 0
	}, {
		name : 'projectMainId',
		mapping : 1
	}, {
		name : 'repairProjectId',
		mapping : 2
	}, {
		name : 'repairProjectName',
		mapping : 3
	}, {
		name : 'workingCharge',
		mapping : 4
	}, {
		name : 'workingChargeName',
		mapping : 5
	}, {
		name : 'workingMenbers',
		mapping : 6
	}, {
		name : 'workingTime',
		mapping : 7
	}, {
		name : 'standard',
		mapping : 8
	}, {
		name : 'material',
		mapping : 9
	}, {
		name : 'startDate',
		mapping : 12
	}, {
		name : 'endDate',
		mapping : 13
	},		// add by wpzhu 20100603
			{
				name : 'acceptanceSecond',
				mapping : 14
			}, {
				name : 'acceptanceSecondName',
				mapping : 15
			}, {
				name : 'acceptanceThree',
				mapping : 16
			}, {
				name : 'acceptanceThreeName',
				mapping : 17
			}]);

	var annex_ds = new Ext.data.JsonStore({
		url : 'manageplan/repairDetailList.action',
		root : 'list',
		totalProperty : 'totalCount',
		fields : annex_item
	});

	var annex_sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false
	});

	var annex_item_cm = new Ext.grid.ColumnModel([annex_sm,
			new Ext.grid.RowNumberer({
				header : '序号',
				width : 35,
				align : 'center'
			}), {
				header : '明细ID',
				dataIndex : 'projectDetailId',
				align : 'center',
				hidden : true
			}, {
				header : '主表ID',
				dataIndex : 'projectMainId',
				align : 'center',
				hidden : true
			}, {
				header : '项目ID',
				dataIndex : 'repairProjectId',
				align : 'center',
				hidden : true
			}, {
				header : '项目名称',
				dataIndex : 'repairProjectName',
				align : 'left',
				width : 210,
				editor : new Ext.form.TriggerField({
					width : 320,
					allowBlank : false,
					// readOnly : true,
					onTriggerClick : repairProSelect
				// listeners : {
				// render : function(f) {
				// f.el.on('keyup', function(e) {
				// annexGrid.getSelectionModel().getSelected()
				// .set("repairProjectId", 'temp');
				// });
				// }
				// }
				})
			}, {
				header : '工作负责人ID',
				dataIndex : 'workingCharge',
				align : 'center',
				hidden : true
			}, {
				header : '工作负责人',
				dataIndex : 'workingChargeName',
				width : 80,
				align : 'center',
				editor : new Ext.form.TriggerField({
					width : 320,
					readOnly : true,
					allowBlank : false,
					onTriggerClick : workingChargeSelect,
					listeners : {
						render : function(f) {
							f.el.on('keyup', function(e) {
								annexGrid.getSelectionModel().getSelected()
										.set("workingCharge", 'temp');
							});
						}
					}
				})
			}, {
				header : '工作成员',
				dataIndex : 'workingMenbers',
				align : 'center',
				editor : new Ext.form.TextField()
			}, {
				header : '工作时间',
				dataIndex : 'workingTime',
				align : 'center',
				hidden : true,
				editor : new Ext.form.TextField()
			}, {
				header : '计划开始日期',
				dataIndex : 'startDate',
				width : 80,
				align : 'center',
				editor : new Ext.form.TextField({
					allowBlank : false,
					style : 'cursor:pointer',
					readOnly : true,
					listeners : {
						focus : function() {
							WdatePicker({
								// 时间格式
								startDate : '%y-%M-%d',
								dateFmt : 'yyyy-MM-dd',
								alwaysUseStartDate : false,
								onpicked : checkTime1
							});

						}
					}
				})

			}, {
				header : '计划结束日期',
				dataIndex : 'endDate',
				readOnly : true,
				width : 80,
				align : 'center',
				editor : new Ext.form.TextField({
					allowBlank : false,
					style : 'cursor:pointer',
					readOnly : true,
					listeners : {
						focus : function() {
							WdatePicker({
								// 时间格式
								startDate : '%y-%M-%d ',
								dateFmt : 'yyyy-MM-dd',
								alwaysUseStartDate : false,
								onpicked : checkTime2
							});

						}
					}
				})

			}, {
				header : '验收标准',
				dataIndex : 'standard',
				align : 'center',
				width : 60,
				renderer : function(v) {
					if (v == '2') {
						return '二级';
					} else {
						return '三级';
					}
				},
				editor : new Ext.form.ComboBox({
					readOnly : true,
					name : 'standard',
					hiddenName : 'standard',
					mode : 'local',
					width : 70,
					value : '2',
					fieldLabel : '验收标准',
					triggerAction : 'all',
					listeners : {
						"select" : function() {
						}
					},
					store : new Ext.data.SimpleStore({
						fields : ['name', 'value'],
						data : [['二级', '2'], ['三级', '3']]
					}),
					valueField : 'value',
					displayField : 'name',
					anchor : "15%",
					listeners : {
						"select" : function(v) {
						}
					}
				})
			}, {
				header : '二级验收人',
				dataIndex : 'acceptanceSecondName',
				width : 80,
				align : 'center',
				editor : new Ext.form.TriggerField({
					width : 320,
					readOnly : true,
					allowBlank : false,
					onTriggerClick : acceptSecondSelect,
					listeners : {
						render : function(f) {
							f.el.on('keyup', function(e) {
								annexGrid.getSelectionModel().getSelected()
										.set("acceptanceSecond", 'temp');
							});
						}
					}
				})
			}, {
				header : '三级验收人',
				dataIndex : 'acceptanceThreeName',
				width : 80,
				align : 'center',
				editor : new Ext.form.TriggerField({
					width : 320,
					readOnly : true,
					allowBlank : false,
					onTriggerClick : acceptThreeSelect,
					listeners : {
						render : function(f) {
							f.el.on('keyup', function(e) {
								annexGrid.getSelectionModel().getSelected()
										.set("acceptanceThree", 'temp');
							});
						}
					}
				})
			}, {
				header : '是否落实材料',
				dataIndex : 'material',
				align : 'center',
				renderer : function(v) {
					if (v == 'Y') {
						return '是';
					} else {
						return '否';
					}
				},
				editor : new Ext.form.ComboBox({
					readOnly : true,
					name : 'material',
					hiddenName : 'material',
					mode : 'local',
					width : 70,
					value : 'Y',
					fieldLabel : '是否落实材料',
					triggerAction : 'all',
					listeners : {
						"select" : function() {
						}
					},
					store : new Ext.data.SimpleStore({
						fields : ['name', 'value'],
						data : [['是', 'Y'], ['否', 'N']]
					}),
					valueField : 'value',
					displayField : 'name',
					anchor : "15%",
					listeners : {
						"select" : function() {
						}
					}
				})
			}]);
	annex_item_cm.defaultSortable = true;
	var annexGrid = new Ext.grid.EditorGridPanel({
		ds : annex_ds,
		cm : annex_item_cm,
		sm : annex_sm,
		clicksToEdit : 1,
		split : true,
		autoHeight : true,
		autoScroll : true,
		tbar : annextbar,
		border : false
	});

	getWorkCode();

	var layout = new Ext.Panel({
		autoWidth : true,
		autoHeight : true,
		border : false,
		autoScroll : true,
		split : true,
		items : [form, annexGrid]
	});
	layout.render(Ext.getBody());

	if (id != "") {
		Ext.Ajax.request({
			url : 'manageplan/findRepairProInfoById.action',
			params : {
				id : id
			},
			method : 'post',
			waitMsg : '正在加载数据...',
			success : function(result, request) {
				var obj = eval('(' + result.responseText + ')');
				form.getForm().loadRecord(obj);
				method = "update";
				Ext.get("projectMainId").dom.value = obj[0];
				if (obj[1] != null) {
					Ext.get("projectMainYear").dom.value = obj[1];
				}
				if (obj[3] != null) {
					Ext.getCmp('cbxRepairType').setValue(obj[2]);
					Ext.form.ComboBox.superclass.setValue.call(Ext
							.getCmp('cbxRepairType'), obj[3]);
				}
				if (obj[8] != null) {
					Ext.getCmp('specialityId').setValue(obj[7]);
					Ext.form.ComboBox.superclass.setValue.call(Ext
							.getCmp('specialityId'), obj[8]);
				}
				if (obj[10] != null) {
					Ext.getCmp('tasklistId').setValue(obj[9]);
					Ext.form.ComboBox.superclass.setValue.call(Ext
							.getCmp('tasklistId'), obj[10]);
				}
				if (obj[5] != null) {
					Ext.get("fillBy").dom.value = obj[4];
					Ext.get("fillName").dom.value = obj[5];
				}
				if (obj[6] != null) {
					Ext.get("fillTime").dom.value = obj[6];
				}
				if (obj[11] != null) {
					Ext.get("memo").dom.value = obj[11];
				}
				if (obj[16] != null) {
					var va = obj[16] == '是' ? 'Y' : 'N';
					situationProjectBox.setValue(va);
					// Ext.get("situationProject").dom.value = obj[16];
				}
				entryId = obj[13];
				annex_ds.load({
					params : {
						repairMainId : id
					}
				});
				if (revoke != "") {
					Ext.get('btnSave').dom.disabled = true
					Ext.get('btnDelete').dom.disabled = true;
					Ext.get('btnReport').dom.disabled = true;
				} else {
					annextbar.setDisabled(false);

				}
			},
			failure : function(result, request) {
				Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
			}
		});
		method = "update";
	} else {
		parent.Ext.getCmp("maintab").setActiveTab(2);
		parent.Ext.getCmp("maintab").setActiveTab(1);
	}

	function addHisRecord() {

		
		if(Ext.get("main.specialityId").dom.value==null||Ext.get("main.specialityId").dom.value=="")
		{
			Ext.Msg.alert("提示","请选择检修专业");
			return;
		}
		// ------------add by fyyang 20100524-------
		myMate = null;
		winSpecial=Ext.get("main.specialityId").dom.value;
		
	var conn = Ext.lib.Ajax.getConnectionObject().conn;
	conn.open("POST", 'manageplan/findNewVersionBySpecial.action?specialId='
			+ winSpecial, false);
	conn.send(null);
	// 成功状态码为200
	if (conn.status == "200") {
		var result = conn.responseText;
		 winVersion=result;
	}
		
		myWin = new Ext.Window({
			title : '检修项目历史选择',
			modal : true,
			closeAction : 'close',
			html : "<iframe src='run/repair/business/repairRegister/repairHistory.jsp' width=\"100%\" height=\"100%\" id=\"newWin\" ></iframe>",
			layout : 'fit',
			collapsible : true, // 折叠
			maximizable : true, // 最大化
			plain : true,
			width : 650,
			height : 400,
			listeners : {
				'close' : function() {

					if (myMate != null) {
						for (var i = 0; i < myMate.length; i++) {
							var o = new annex_item({
								'projectDetailId' : '',
								'projectMainId' : id,
								'repairProjectId' : '',
								'repairProjectName' : '',
								'workingCharge' : '',
								'workingChargeName' : '',
								'workingMenbers' : '',
								'workingTime' : '',
								'standard' : '2',
								'material' : 'Y',
								'startDate' : '',
								'endDate' : '',
								'acceptanceSecond' : '',// add by wpzhu 20100603
								'acceptanceSecondName' : '',
								'acceptanceThree' : '',
								'acceptanceThreeName' : ''

							});
							annex_ds.insert(currentIndex, o);
							o.set('repairProjectName',
									myMate[i].data.repairProjectName);
							o.set('repairProjectId',
									myMate[i].data.repairProjectId);
							o
									.set('workingCharge',
											myMate[i].data.workingCharge);
							o.set('workingChargeName',
									myMate[i].data.workingChargeName);
							o.set('workingMenbers',
									myMate[i].data.workingMenbers);
							o.set('workingTime', myMate[i].data.workingTime);
							o.set('acceptanceSecond', // add by wpzhu 20100603
									myMate[i].data.acceptanceSecond);
							o.set('acceptanceThree',
									myMate[i].data.acceptanceThree);

							o.set('acceptanceSecondName',
									myMate[i].data.acceptanceSecondName);
							o.set('acceptanceThreeName',
									myMate[i].data.acceptanceThreeName);

							annexGrid.stopEditing();
							var count = annex_ds.getCount();
							var currentIndex = count;
							annex_sm.selectRow(currentIndex);
							annexGrid.startEditing(currentIndex, 1);
						}
						annexGrid.getView().refresh();
					}
				}
			}
		});
		myWin.show();
		// -----------add end------------------------------
		// var url = "repairHistory.jsp";
		// var mate = window.showModalDialog(url, window,
		// 'dialogWidth=600px;dialogHeight=400px;status=no');
		// if (typeof(mate) != "undefined") {
		// for (var i = 0; i < mate.length; i++) {
		// var o = new annex_item({
		// 'projectDetailId' : '',
		// 'projectMainId' : id,
		// 'repairProjectId': '',
		// 'repairProjectName' : '',
		// 'workingCharge' : '',
		// 'workingChargeName' : '',
		// 'workingMenbers' : '',
		// 'workingTime' : '',
		// 'standard' : '2',
		// 'material' : 'Y',
		// 'startDate' : '',
		// 'endDate' : ''
		// });
		// annex_ds.insert(currentIndex, o);
		// o.set('repairProjectName', mate[i].data.repairProjectName);
		// o.set('repairProjectId', mate[i].data.repairProjectId);
		// o.set('workingCharge', mate[i].data.workingCharge);
		// o.set('workingChargeName', mate[i].data.workingChargeName);
		// o.set('workingMenbers', mate[i].data.workingMenbers);
		// o.set('workingTime', mate[i].data.workingTime);
		//
		// annexGrid.stopEditing();
		// var count = annex_ds.getCount();
		// var currentIndex = count;
		// annex_sm.selectRow(currentIndex);
		// annexGrid.startEditing(currentIndex, 1);
		// }
		// annexGrid.getView().refresh();
		// }
	}

	/**
	 * 工作负责人选择页面
	 */
	function workingChargeSelect() {
		var url = "../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
		var args = {
			selectModel : 'single',
			notIn : "'999999'",
			rootNode : {
				id : '-1',
				text : '灞桥电厂'
			}
		}
		var emp = window
				.showModalDialog(
						url,
						args,
						'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');

		if (typeof(emp) != "undefined") {
			var record = annexGrid.selModel.getSelected()
			record.set("workingChargeName", emp.workerName);
			record.set("workingCharge", emp.workerCode);
			annexGrid.getView().refresh();
		}
	}
	function acceptSecondSelect() {
		var url = "../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
		var args = {
			selectModel : 'single',
			notIn : "'999999'",
			rootNode : {
				id : '-1',
				text : '灞桥电厂'
			}
		}
		var emp = window
				.showModalDialog(
						url,
						args,
						'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');

		if (typeof(emp) != "undefined") {
			var record = annexGrid.selModel.getSelected()
			record.set("acceptanceSecondName", emp.workerName);
			record.set("acceptanceSecond", emp.workerCode);
			annexGrid.getView().refresh();
		}
	}
	function acceptThreeSelect() {
		var url = "../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
		var args = {
			selectModel : 'single',
			notIn : "'999999'",
			rootNode : {
				id : '-1',
				text : '灞桥电厂'
			}
		}
		var emp = window
				.showModalDialog(
						url,
						args,
						'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');

		if (typeof(emp) != "undefined") {
			var record = annexGrid.selModel.getSelected()
			record.set("acceptanceThreeName", emp.workerName);
			record.set("acceptanceThree", emp.workerCode);
			annexGrid.getView().refresh();
		}
	}
	/**
	 * 检修项目选择树
	 */
	function repairProSelect() {
		var url = "repairProjectTree.jsp?";
		url += "op=many";
		var mate = window
				.showModalDialog(url, window,
						'dialogWidth=600px;dialogHeight=400px;status=no,scrollbars:yes');
		if (typeof(mate) != "undefined") {
			var repairProject = mate.code.split(",");
			var repairProjectName = mate.name.split(",");
			var workingCharge = mate.workingCharge.split(",");
			var workingMenbers = mate.workingMenbers.split(",");
			var workingTime = mate.workingTime.split(",");
			var workingChargeCode = mate.workingChargeCode.split(",");

			// modify by fyyang 20100517
			var record = annexGrid.selModel.getSelected();
			record.set('repairProjectName', repairProjectName[0]);
			record.set('repairProjectId', repairProject[0]);
			record.set('workingCharge', workingChargeCode[0]);
			record.set('workingChargeName', workingCharge[0]);
			record.set('workingMenbers', workingMenbers[0]);
			record.set('workingTime', workingTime[0]);

			if (repairProject.length > 1) {
				for (var i = 1; i < repairProject.length; i++) {
					var count = annex_ds.getCount();
					var currentIndex = count;
					var o = new annex_item({
						'projectDetailId' : '',
						'projectMainId' : id,
						'repairProjectId' : '',
						'repairProjectName' : '',
						'workingCharge' : '',
						'workingChargeName' : '',
						'workingMenbers' : '',
						'workingTime' : '',
						'standard' : '2',
						'material' : 'Y',
						'startDate' : '',
						'endDate' : ''
					});
					annex_ds.insert(currentIndex, o);
					o.set('repairProjectName', repairProjectName[i]);
					o.set('repairProjectId', repairProject[i]);
					o.set('workingCharge', workingChargeCode[i]);
					o.set('workingChargeName', workingCharge[i]);
					o.set('workingMenbers', workingMenbers[i]);
					o.set('workingTime', workingTime[i]);

					annexGrid.stopEditing();
					annex_sm.selectRow(currentIndex);
					annexGrid.startEditing(currentIndex, 1);
				}
			}

			annexGrid.getView().refresh();
		}
	};

	function checkInput() {
		var msg = "";
		if (cbxRepairType.getValue() == "") {
			msg = "'检修类别'";
		}
		if (cbxRepairSpecail.getValue() == "") {
			if (msg == "")
				msg = "'检修专业'";
			else
				msg = msg + ",'检修专业'";
		}
		if (msg != "") {
			Ext.Msg.alert("提示", "请输入" + msg);
			return false
		} else {
			return true;
		}
	}

	function checkTime1() {
		var startdate1 = this.value;
		startdate2 = startdate1.substring(0, 10);
		var enddate1 = annexGrid.getSelectionModel().getSelected()
				.get("endDate");
		if (enddate1 != null && enddate1 != "") {
			enddate2 = enddate1.substring(0, 10);
			if (startdate2 != "") {
				if (startdate2 > enddate2 && enddate2 != "") {
					Ext.Msg.alert("提示", "开始日期必须早于结束日期");
					return;
				}
			}
		}
		annexGrid.getSelectionModel().getSelected()
				.set("startDate", startdate2);
	}

	function checkTime2() {
		var endtime1 = this.value;
		var endtime2 = endtime1.substring(0, 10);
		var beginTime1 = annexGrid.getSelectionModel().getSelected()
				.get("startDate");
		if (beginTime1 != null && beginTime1 != "") {
			beginTime2 = beginTime1.substring(0, 10);
			if (endtime2 != "" && beginTime2 != "") {
				if (endtime2 < beginTime2 && endtime2 != "") {
					Ext.Msg.alert("提示", "结束日期必须晚于开始日期");
					return;
				}
			}
		}
		annexGrid.getSelectionModel().getSelected().set("endDate", endtime2);
	}

})