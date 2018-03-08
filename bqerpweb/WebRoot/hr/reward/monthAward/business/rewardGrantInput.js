Ext.ns("trainMaint.trainRegister");
trainMaint.trainRegister = function() {
	var grantId = null;
	var deptId;
	var sessDeptName;
	var sessDeptCode;
	var sessWorname;
	var sessWorcode;
	var workFlowState = 0;
	function getDeptCode(deptId) {
		Ext.lib.Ajax.request('POST', 'hr/initRewardGrantDept.action?deptId='
						+ deptId, {
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						if (result != "") {
							strDeptId.setValue(result[0])
							deptName.setValue(result[1])
							// 设定默认工号
						} else {
							strDeptId.setValue(deptId)
							deptName.setValue(sessDeptName)
						}
					}
				});
	}

	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						if (result.workerCode) {
							// 设定默认工号
							sessWorcode = result.workerCode;
							sessWorname = result.workerName;
							deptId = result.deptId;
							getDeptCode(deptId);
							sessDeptName = result.deptCode;
							sessDeptName = result.deptName;

							strGroupId.setValue(deptId);
							groupName.setValue(sessDeptName)
							fillByName.setValue(sessWorname);
							strFillByCode.setValue(sessWorcode);
						}
					}
				});
	}

	var grantMonthStart = new Date();
	grantMonthStart = grantMonthStart.format('Y-m');

	// 时间
	var grantMonth = new Ext.form.TextField({
				width : 250,
				style : 'cursor:pointer',
				name : "rwGrant.grantMonth",
				fieldLabel : '时间',
				allowBlank : true,
				readOnly : true,
				value : grantMonthStart,
				listeners : {
					focus : function() {
						WdatePicker({
									isShowClear : true,
									startDate : '%y-%M',
									dateFmt : 'yyyy-MM'
								});
						this.blur();
					}
				}
			});
	// 主表ID
	var strGrantId = new Ext.form.Hidden({
				id : 'grantId',
				name : 'rwGrant.grantId'
			})

	// 部门
	var deptName = new Ext.form.TextField({
				id : 'deptName',
				fieldLabel : '部门',
				width : 250
			})
	var strDeptId = new Ext.form.Hidden({
				id : 'deptId',
				name : 'rwGrant.deptId'
			})
	// 班组
	var groupName = new Ext.form.ComboBox({
				id : 'groupName',
				fieldLabel : '班组',
				width : 250,
				onTriggerClick : function() {
					var rewardGroup = new Reward.rewardGroup({
								deptId : strDeptId.getValue(),
								deptName:deptName.getValue()
							})
					rewardGroup.win.show();
					rewardGroup.groupGrid.on('rowdblclick', function() {
								var rec = rewardGroup.groupGrid
										.getSelectionModel().getSelected();
								groupName.setValue(rec.get('groupName'));
								strGroupId.setValue(rec.get('groupId'))
								rewardGroup.win.hide();
							})
				}
			})
	var strGroupId = new Ext.form.Hidden({
				id : 'groupId',
				name : 'rwGrant.groupId'
			})
	// 制表人
	var fillByName = new Ext.form.TextField({
				id : 'fillByName',
				fieldLabel : '制表人',
				width : 250
			})
	var strFillByCode = new Ext.form.Hidden({
				id : 'strFillByCode',
				name : 'rwGrant.fillBy'
			})
	// 制表时间
	var fillDate = new Date();
	fillDate = fillDate.format('Y-m-d');

	var fillByDate = new Ext.form.TextField({
				width : 250,
				style : 'cursor:pointer',
				name : "rwGrant.fillDate",
				fieldLabel : '制表时间',
				allowBlank : true,
				value : fillDate,
				readOnly : true
			});

	var btnAdd = new Ext.Button({
				text : '新增',
				iconCls : 'add',
				handler : addFunction
			})
	function addFunction() {
		grantMonth.setVisible(grantMonthStart);
		groupName.setValue(sessDeptName);
		strGroupId.setValue(deptId);
		strGrantId.setValue("");
		grantId = null;
		detailStore.removeAll();
		Ext.getCmp("btnSave").disable();
		Ext.getCmp("btnDelete").disable();
		Ext.getCmp("btnAdd").disable();

	}
	//add by wpzhu 增加合计
	function addLine() {
        var count = detailStore.getCount();
		var currentIndex = count;
		var o = new detailRecord({
					'detailId' : '',
					'grantId' : '',
					'empId' : '',
					'coefficientNum' : '',
					'baseNum' : '',
					'amountNum' : '',
					'awardNum' : '',
					'monthRewardNum' : '0',
					'quantifyCash' : '0',
					'monthAssessNum' : '0',
					'quantifyAssessNum' : '0',
					'totalNum' :'' ,
					'signBy' : '',
					'memo' : '',
					'empName' : '合计'
				});
        // 停止原来编辑
				detailGrid.stopEditing();
		detailStore.insert(currentIndex, o);
		sm.selectRow(currentIndex);
		detailGrid.startEditing(currentIndex, 1);
        detailGrid.getView().refresh();
    };		
	var save = new Ext.Button({
				text : '保存',
				iconCls : 'save',
				handler : function() {
					var myurl = "";
					myurl = "hr/saveRewardGrand.action";
					outTrainForm.getForm().submit({
						method : 'POST',
						url : myurl,
						success : function(form, action) {
							// alert(action.response.responseText)
							var o = eval("(" + action.response.responseText
									+ ")");
							Ext.Msg.alert("注意", o.msg);
							if (o.grantId != null) {
								grantId = o.grantId;
								strGrantId.setValue(o.grantId);
								Ext.getCmp("btnAdd").setDisabled(false);
								Ext.getCmp("btnDelete").setDisabled(false);
								Ext.getCmp("btnSave").setDisabled(false);
								detailStore.load({
											params : {
												grantId : grantId
											},
											callback : addLine
										});
							} else {
								detailStore.removeAll();
								Ext.getCmp("btnSave").disable();
								Ext.getCmp("btnDelete").disable();
								Ext.getCmp("btnAdd").disable();
							}
						},
						faliue : function() {
							Ext.Msg.alert('提示', '请联系管理员!.');
						}
					});
				}

			});
	// 删除
	var btnDel = new Ext.Button({
				text : '删除',
				iconCls : 'delete',
				handler : function() {
					if (strGrantId.getValue() != null
							&& strGrantId.getValue() != "") {
						Ext.Ajax.request({
									url : 'hr/deleteRewardGrand.action',
									params : {
										grantId : strGrantId.getValue()
									},
									success : function(action) {
										var result = eval("("
												+ action.responseText + ")");
										Ext.Msg.alert("提示", result.msg);
										addFunction();
									}
								});
					} else {
						Ext.MessageBox.alert('提示', '没有相关主表信息！')
					}
				}
			})
	// 取消
	var btnCancel = new Ext.Button({
				text : '取消',
				iconCls : 'reflesh',
				handler : function() {
					location.reload();
				}
			})
	// 上报
	var btnReport = new Ext.Button({
				text : '上报',
				iconCls : 'upcommit',
				handler : function() {
					validateWin.show();
				}
			})
	// ↓↓****************员工验证窗口****************

	// 工号
	var workCode = new Ext.form.TextField({
				id : "workCode",
				fieldLabel : '工号<font color ="red">*</font>',
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
		title : "请输入工号和密码",
		buttonAlign : "center",
		resizable : false,
		modal : true,// add
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
								// 如果验证成功，进行上报操作
								if (result) {
									var myurl = "";
									myurl = "hr/updateRewardGrand.action";
									outTrainForm.getForm().submit({
										method : 'POST',
										url : myurl,
										success : function(form, action) {
											var o = eval("("
													+ action.response.responseText
													+ ")");
											Ext.Msg.alert("提示", o.msg,
													function() {
														if (o.msg.indexOf('成功') != -1) {

															window.returnValue = o.obj;
															window.close();
														}
														validateWin.hide();
													});
											addFunction();
										},
										faliue : function() {
											Ext.Msg.alert('提示', '请联系管理员!.');
										}
									});
								} else {
									Ext.Msg.alert("提示", "密码错误");
								}
							}
						}, "workerCode=" + workCode.getValue() + "&loginPwd="
								+ workPwd.getValue());
			}
		}, {
			// 取消按钮
			text : '取消',
			handler : function() {
				validateWin.hide();
			}
		}],
		listeners : {
			show : function(com) {
				// 取得默认工号
				Ext.lib.Ajax.request('POST',
						'comm/getCurrentSessionEmployee.action', {
							success : function(action) {
								var result = eval("(" + action.responseText
										+ ")")
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

	var detailRecord = Ext.data.Record.create([{
				name : 'detailId',
				mapping : 0
			}, {
				name : 'grantId',
				mapping : 1
			}, {
				name : 'empId',
				mapping : 2
			}, {
				name : 'coefficientNum',
				mapping : 3
			}, {
				name : 'baseNum',
				mapping : 4
			}, {
				name : 'amountNum',
				mapping : 5
			}, {
				name : 'awardNum',
				mapping : 6
			} , {
				name : 'monthRewardNum',
				mapping : 7
			}, {
				name : 'quantifyCash',
				mapping : 8
			}, {
				name : 'monthAssessNum',
				mapping : 9
			}, {
				name : 'quantifyAssessNum',
				mapping : 10
			}, {
				name : 'totalNum',
				mapping : 11
			}, {
				name : 'signBy',
				mapping : 12
			}, {
				name : 'memo',
				mapping : 13
			}, {
				name : 'empName',
				mapping : 14
			}, {//add by sychen 20100903
				name : 'addValue',
				mapping : 15
			}]);
	var sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : false

			});
	var detailStore = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'hr/getRewardDetailList.action'
						}),
				reader : new Ext.data.ArrayReader({}, detailRecord)
			})

	var person = new Power.person({
				anchor : '85%'
			}, {
				selectModel : 'single'
			});

	person.btnConfirm.on("click", function() {
				if (detailGrid.getSelectionModel().hasSelection()) {
					var rec = detailGrid.getSelectionModel().getSelected();
					var per = person.chooseWorker();
					Ext.Ajax.request({
								url : 'hr/getRewardMonthAward.action',
								params : {
									empId : per.get('empId'),
									grantId : grantId
								},
								success : function(action) {
									var result = eval("(" + action.responseText
											+ ")");
									rec.set('empId', per.get('empId'));
									rec.set('empName', per.get('workerName'))
									rec.set('coefficientNum', result[1]);
									rec.set('baseNum', result[0]);
									//add by sychen 20100903
									rec.set('monthRewardNum', result[2]);
									rec.set('quantifyCash', result[3]);
									rec.set('addValue', result[4]);
									
									//add by sychen 20100903 end
								}
							});
				}
			});
	// 增加
	function addTheme() {
		var count = detailStore.getCount();
		var currentIndex = count;
		var o = new detailRecord({
					'detailId' : null,
					'grantId' : grantId,
					'empId' : null,
					'coefficientNum' : null,
					'baseNum' : null,
					'amountNum' : null,
					'awardNum' : '0',
//					'monthRewardNum' : '0',//update by sychen 20100903
//					'quantifyCash' : '0',
					'monthRewardNum' :null,
					'quantifyCash' : null,
					'monthAssessNum' : '0',
					'quantifyAssessNum' : '0',
					'addValue' :null,//add by sychen 20100903
					'totalNum' : null,
					'signBy' : null,
					'memo' : null,
					'empName' : null
				});
		detailGrid.stopEditing();
		detailStore.insert(currentIndex-1, o);//modify by wpzhu 20100726
		sm.selectRow(currentIndex-1);
		detailGrid.startEditing(currentIndex-1, 1);
	}

	// 删除记录
	var ids = new Array();
	function deleteTheme() {
		detailGrid.stopEditing();
		var sm = detailGrid.getSelectionModel();
		var selected = sm.getSelections();
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i];
				// add by sychen 20100903
				if (member.get("empName") == "合计") {
					Ext.Msg.alert("提示", "合计行不可删除！");
					return;
				}
				// add by sychen 20100903 end
				if (member.get("detailId") != null) {
					ids.push(member.get("detailId"));
				}
				detailGrid.getStore().remove(member);
				detailGrid.getStore().getModifiedRecords().remove(member);
			}
		}
	}

	// 保存
	function saveTheme() {
		detailGrid.stopEditing();
		var modifyRec = detailGrid.getStore().getModifiedRecords();
		if (modifyRec.length > 0 || ids.length > 0) {

			Ext.Msg.confirm('提示信息', '确定要保存修改吗？', function(button) {
				if (button == 'yes') {
					var addData = new Array();
					var updateData = new Array();
					for (var i = 0; i < modifyRec.length; i++) {
						if(modifyRec[i].get('empName') != "合计") {
							
							if (modifyRec[i].get('detailId') != null&&modifyRec[i].get('detailId')!="") {//modify by wpzhu
								updateData.push(modifyRec[i].data);
							} else {
								addData.push(modifyRec[i].data);
							}
						}

						/*if (modifyRec[i].get('detailId') != null) {
							updateData.push(modifyRec[i].data);
						} else {
							addData.push(modifyRec[i].data);
						}*/

					}
					Ext.Ajax.request({
								url : 'com/saveOrUpdateRewardDetail.action',
								method : 'post',
								params : {
									grantId : grantId,
									isAdd : Ext.util.JSON.encode(addData),
									isUpdate : Ext.util.JSON.encode(updateData),
									ids : ids.join(","),
									workFlowState : workFlowState

								},
								success : function(result, request) {
									var o = Ext.util.JSON
											.decode(result.responseText);
									Ext.Msg.alert("提示信息", o.msg);
									detailStore.rejectChanges();
									ids = [];
									detailStore.load({
												params : {
													grantId : grantId
												},
												callback:addLine
											});
								},
								failure : function(result, request) {
									Ext.Msg.alert("提示信息", "数据保存修改失败！");
									detailStore.rejectChanges();
									ids = [];
									detailStore.reload();
								}
							})
				}
			})

		} else {
			Ext.MessageBox.alert('提示信息', '没有做任何修改！')
		}
	}
	var contbar = new Ext.Toolbar({
				items : [{
							id : 'btnAdd',
							iconCls : 'add',
							text : "增加",
							minWidth : 70,
							disabled : true,
							handler : addTheme
						}, '-', {
							id : 'btnDelete',
							iconCls : 'delete',
							minWidth : 70,
							disabled : true,
							text : "删除",
							handler : deleteTheme
						}, '-', {
							id : 'btnSave',
							iconCls : 'save',
							text : "保存修改",
							minWidth : 70,
							disabled : true,
							handler : saveTheme
						}]

			});
	var detailGrid = new Ext.grid.EditorGridPanel({
//				layout : 'fit',
				//autoHeight : true,
				autoScrolla : true,
				sm : sm,
				enableColumnMove : false,
				store : detailStore,
				columns : [sm, new Ext.grid.RowNumberer({
									header : "行号",
									width : 35
								}), {
							header : "ID",
							sortable : true,
							dataIndex : 'detailId',
							hidden : true
						}, {
							header : "grantId",
							sortable : true,
							dataIndex : 'grantId',
							hidden : true
						}, {
							header : "empId",
							sortable : true,

							dataIndex : 'empId',
							hidden : true
						}, {
							header : "姓名",
							width : 100,
							align : 'center',
							sortable : true,
							dataIndex : 'empName',
							editor : person.combo,
							renderer : function(value, metadata, record) {
								if (value != null)
									return record.get("empName");
							}
						}, {
							header : "系数",
							width : 100,
							sortable : true,
							dataIndex : 'coefficientNum'
						}, {
							header : "基数",
							width : 100,
							sortable : true,
							dataIndex : 'baseNum'
						}, {
							header : "金额",
							width : 100,
							sortable : true,
							hidden : true,
							dataIndex : 'amountNum',
							renderer : function(value, cellmeta, record, rowIndex,
		                            columnIndex, store) {
		                        if (rowIndex < store.getCount() - 1) {
		                            // 强行触发renderer事件
		                            var totalSum = 0;
		                            return value;
		                        } else {
		                            totalSum = 0;
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                                totalSum += store.getAt(i).get('amountNum');
		                            }
		                            return totalSum.toFixed(2);
		                        }
		                    }
						}, {

							header : "奖励",
							width : 100,
							sortable : true,
							hidden : true,
							dataIndex : 'awardNum',
							renderer : function(value, cellmeta, record, rowIndex,
		                            columnIndex, store) {
		                        if (rowIndex < store.getCount() - 1) {
		                            // 强行触发renderer事件
		                            var totalSum = 0;
		                            return value;
		                        } else {
		                            totalSum = 0;
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                                totalSum += store.getAt(i).get('awardNum');
		                            }
		                            return totalSum.toFixed(2);
		                        }
		                    },
							editor : new Ext.form.NumberField({
										style : 'cursor:pointer',
										// allowNegative : false,
										allowDecimal : false,
										allowBlank : true
									})

						}, {
							header : "月度奖金",
							width : 100,
							sortable : true,
							dataIndex : 'monthRewardNum',
							renderer : function(value, cellmeta, record, rowIndex,
		                            columnIndex, store) {
		                        if (rowIndex < store.getCount() - 1) {
		                            // 强行触发renderer事件
		                            var totalSum = 0;
		                            return value;
		                        } else {
		                            totalSum = 0;
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                                totalSum += store.getAt(i).get('monthRewardNum');
		                            }
		                            return totalSum.toFixed(2);
		                        }
		                    },
		                    editor : new Ext.form.NumberField({
										style : 'cursor:pointer',
										// allowNegative : false,
										allowDecimal : false,
										allowBlank : true
									})
						}, {

							header : "月度考核",
							width : 100,
							sortable : true,
							dataIndex : 'monthAssessNum',
							renderer : function(value, cellmeta, record, rowIndex,
		                            columnIndex, store) {
		                        if (rowIndex < store.getCount() - 1) {
		                            // 强行触发renderer事件
		                            var totalSum = 0;
		                            return value;
		                        } else {
		                            totalSum = 0;
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                                totalSum += store.getAt(i).get('monthAssessNum');
		                            }
		                            return totalSum.toFixed(2);
		                        }
		                    },
							editor : new Ext.form.NumberField({
										style : 'cursor:pointer',
										// allowNegative : false,
										allowDecimal : false,
										allowBlank : true
									})

						}, {
							header : "量化奖金",
							width : 100,
							sortable : true,
							dataIndex : 'quantifyCash',
							renderer : function(value, cellmeta, record, rowIndex,
		                            columnIndex, store) {
		                        if (rowIndex < store.getCount() - 1) {
		                            // 强行触发renderer事件
		                            var totalSum = 0;
		                            return value;
		                        } else {
		                            totalSum = 0;
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                                totalSum += store.getAt(i).get('quantifyCash');
		                            }
		                            return totalSum.toFixed(2);
		                        }
		                    },
		                    editor : new Ext.form.NumberField({
										style : 'cursor:pointer',
										// allowNegative : false,
										allowDecimal : false,
										allowBlank : true
									})
						}, {

							header : "量化考核",
							width : 100,
							sortable : true,
							dataIndex : 'quantifyAssessNum',
							renderer : function(value, cellmeta, record, rowIndex,
		                            columnIndex, store) {
		                        if (rowIndex < store.getCount() - 1) {
		                            // 强行触发renderer事件
		                            var totalSum = 0;
		                            return value;
		                        } else {
		                            totalSum = 0;
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                                totalSum += store.getAt(i).get('quantifyAssessNum');
		                            }
		                            return totalSum.toFixed(2);
		                        }
		                    },
							editor : new Ext.form.NumberField({
										style : 'cursor:pointer',
										// allowNegative : false,
										allowDecimal : false,
										allowBlank : true
									})

						},{//add by sychen 20100903
			              header : "技师公会增加值",
			              width : 100,
			              sortable : true,
			              dataIndex : 'addValue',
			              renderer : function(value, cellmeta, record, rowIndex, columnIndex,
					      store) {
				          if (rowIndex < store.getCount() - 1) {
					             var totalSum = 0;
		                          return value;
				         } else {
				             	totalSum = 0;
					            for (var i = 0; i < store.getCount() - 1; i++) {
						        totalSum += store.getAt(i).get('addValue');
					     }
		                       return totalSum.toFixed(2);
				      }
			          },editor : new Ext.form.NumberField({
										style : 'cursor:pointer',
										allowDecimal : false,
										allowBlank : true
									})
		     },{
							header : "合计",
							width : 100,
							sortable : true,
							dataIndex : 'totalNum',
							renderer : function(value, cellmeta, record, rowIndex,
		                            columnIndex, store) {
		                        if (rowIndex < store.getCount() - 1) {
		                            // 强行触发renderer事件
		                            var totalSum = 0;
		                            return value;
		                        } else {
		                            totalSum = 0;
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                                totalSum += store.getAt(i).get('totalNum');
		                            }
		                            return totalSum.toFixed(2);
		                        }
		                    }
						}, {

							header : "签名",
							width : 100,
							sortable : true,
							dataIndex : 'signBy',
							editor : new Ext.form.TextField({
										style : 'cursor:pointer',
										allowNegative : false,
										allowDecimal : false,
										allowBlank : true
									})

						}, {

							header : "备注",
							width : 180,
							sortable : true,
							dataIndex : 'memo',
							editor : new Ext.form.TextArea({
										style : 'cursor:pointer',
										allowNegative : false,
										allowDecimal : false,
										allowBlank : true
									})

						}],
				tbar : contbar,
				clicksToEdit : 1,
				autoSizeColumns : true

			});
			
//add by fyyang 2010-09-06			
detailGrid.on("headercontextmenu",function(g,c,e){
e.stopEvent();
var menu = new Ext.menu.Menu({
			id : 'mainMenu',
			items : [new Ext.menu.Item({
				text : '清空列',
				iconCls : 'delete',
				handler : function() {
					var cname=detailGrid.getColumnModel().getDataIndex(c);
					for(var i=0;i<detailStore.getCount();i++)
					{
						detailStore.getAt(i).set(cname,null);
					}
				}
			})]
		});
		var coords = e.getXY();
		menu.showAt([coords[0], coords[1]]);
	
});

	var outTrainForm = new Ext.form.FormPanel({
		title : '主信息',
		bodyStyle : "padding:5px 5px 0",
		labelAlign : 'right',
		// labelWidth : 100,
		autoHeight : true,
		layout : 'form',
		border : false,
		tbar : [btnAdd, '-', save, '-', btnDel, '-', btnCancel, '-', btnReport],
		items : [grantMonth, strGrantId, {
					border : false,
					layout : 'form',
					items : [{
								border : false,
								layout : 'column',
								items : [{
											columnWidth : .5,
											layout : 'form',
											labelWidth : 100,
											border : false,
											items : [deptName, strDeptId]
										}, {
											columnWidth : .5,
											layout : 'form',
											labelWidth : 100,
											border : false,
											items : [groupName, strGroupId]
										}]
							}, {
								border : false,
								layout : 'column',
								items : [{
											columnWidth : .5,
											layout : 'form',
											labelWidth : 100,
											border : false,
											items : [fillByName, strFillByCode]
										}, {
											columnWidth : .5,
											layout : 'form',
											labelWidth : 100,
											border : false,
											items : [fillByDate]
										}]
							}]

				}]

	});
	//add by wpzhu 20100726
  detailGrid.on('beforeedit', function(e) {
			if (e.record.get("empName")=="合计" ) {
					return false;
				}
			})
	// panel
	var detailsPanel = new Ext.Panel({
				layout : 'border',
				border : false,
				autoScroll : true,
				items : [{
							region : 'north',
							height : 150,
							split : true,
							items : [outTrainForm]
						}, {
							region : 'center',
							layout:'fit',
							items : [detailGrid]
						}]
			});
	getWorkCode();
	return {
		panel : detailsPanel,
		setTrainId : function(rec) {
			grantRec = rec;
			grantId = rec.get("grantId");
			detailStore.load({
						params : {
							grantId : grantId
						},
						callback : addLine
					});
			grantMonth.setValue(rec.get("grantMonth"));
			strGrantId.setValue(rec.get("grantId"));
			strDeptId.setValue(rec.get("deptId"));
			deptName.setValue(rec.get("deptName"));
			strGroupId.setValue(rec.get("groupId"));
			groupName.setValue(rec.get("groupName"));
			strFillByCode.setValue(rec.get("fillBy"));
			fillByName.setValue(rec.get("workName"));
			fillByDate.setValue(rec.get("fillDate"));

		},
		setFeeDetailBtn : function(b) {
			if (b) {
				Ext.getCmp("btnSave").enable();
				Ext.getCmp("btnDelete").enable();
				Ext.getCmp("btnAdd").enable();
			} else {
				Ext.getCmp("btnSave").disable();
				Ext.getCmp("btnDelete").disable();
				Ext.getCmp("btnAdd").disable();
			}
		},
		resetInputField : function() {
			outTrainForm.getForm().reset();
			person.combo.enable();
			sex.enable();
			dept.combo.enable();
			grantId = null;
			ids = [];
			detailStore.removeAll();
			detailGrid.getView().refresh();

		}
	}

};