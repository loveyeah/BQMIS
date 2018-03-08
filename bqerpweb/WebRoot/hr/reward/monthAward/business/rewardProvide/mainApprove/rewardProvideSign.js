Ext.ns("trainMaint.trainRegister");
trainMaint.trainRegister = function() {
	var rewardId = null;
	var deptId;
	var workcode="";
	var detailRecord = Ext.data.Record.create([{
				name : 'detailId',
				mapping:0
			}, {
				name : 'rewardId',
				mapping:1
			}, {
				name : 'deptId',
				mapping:2
			}, {
				name : 'empCount',
				mapping:3
			}, {
				name : 'lastMonthNum',
				mapping:4
			}, {
				name : 'monthRewardNum',
				mapping:5
			}, {
				name : 'quantifyCash',
				mapping:6
			}, {
				name : 'extraAddNum',
				mapping:7
			}, {
				name : 'monthAssessNum',
				mapping:8
			}, {
				name : 'otherNum',
				mapping:9
			}, {
				name : 'totalNum',
				mapping:10
			}, {
				name : 'memo',
				mapping:11
			}, {
				name : 'workFlowState',
				mapping:12
			}, {
				name : 'workFlowNo',
				mapping:13
			}, {
				name : 'deptName'
			}, {
				name : 'isUse'
			},{
				name : 'sumTotalnum',
				mapping:16
			}]);
	var sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : false

			});
			
	var detailStore = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'hr/queryMonthRewardDetail.action'
						}),
				reader : new Ext.data.JsonReader({
					root : "list",
					totalProperty : "totalCount"
				}, detailRecord)
			})
	var detailDept = new Power.dept({
				anchor : '100%'
			});
	detailDept.btnConfrim.on("click", function() {
				if (detailGrid.getSelectionModel().hasSelection()) {
					var rec = detailGrid.getSelectionModel().getSelected();
					var aDept = detailDept.getValue();
					var flag = 0;
					for(var i=0;i<detailStore.getCount();i++) {
						if(detailStore.getAt(i).data.deptId== aDept.key) {
							flag = "1";
							Ext.Msg.alert("提示","不能重复选择部门!");
						}
					}
					if(flag == 0) {
						rec.set('deptId', aDept.key);
						rec.set('workFlowNo', aDept.value);	
					}
				}
			});
			
	/**
     * 添加统计行
     */
    function addLine() {
        var count = detailStore.getCount();
		var currentIndex = count;
		var record = new detailRecord({
					'detailId' : '',
					'rewardId' : '',
					'deptId' : '',
					'deptName' : '',
					'empCount' : '',
					'lastMonthNum' : '',
					'monthRewardNum' : '',
					'quantifyCash' : '',
					'extraAddNum' : '',
					'monthAssessNum' : '',
					'otherNum' : '',
					'totalNum' : '',
					'memo' : '',
					'workFlowNo' : '合计'
				});
        // 停止原来编辑
        detailGrid.stopEditing();
        // 插入统计行
//        detailGrid.stopEditing();
//		detailStore.insert(currentIndex, o);
//		sm.selectRow(currentIndex);
//		detailGrid.startEditing(currentIndex, 1);
        detailStore.insert(count, record);
        detailGrid.getView().refresh();
//        totalCount = materialStore.getCount() - 1;
    };		

	// 增加
	function addTheme() {
		var count = detailStore.getCount();
		var currentIndex = count;
		var o = new detailRecord({
					'detailId' : '',
					'rewardId' : rewardId,
					'deptId' : '',
					'deptName' : '',
					'empCount' : '',
					'lastMonthNum' : '',
					'monthRewardNum' : '',
					'quantifyCash' : '',
					'extraAddNum' : '',
					'monthAssessNum' : '',
					'otherNum' : '',
					'totalNum' : '',
					'memo' : '',
					'workFlowNo' : ''
				});
		detailGrid.stopEditing();
		detailStore.insert(currentIndex-1, o);
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
//			for (var i = 0; i < modifyRec.length; i++) {
//				if (modifyRec[i].data.feeSortId == null
//						|| modifyRec[i].data.feeSortId == "") {
//					Ext.Msg.alert('提示信息', '费用类别不可为空，请输入！')
//					return;
//				}
//				if (modifyRec[i].data.actualFee == null
//						|| modifyRec[i].data.actualFee == "") {
//					Ext.Msg.alert('提示信息', '实际费用不可为空，请输入！')
//					return;
//				}
//				if (modifyRec[i].data.feeDept == null
//						|| modifyRec[i].data.feeDept == "") {
//					Ext.Msg.alert('提示信息', '培训归口部门不可为空，请输入！')
//					return;
//				}
//			}
			Ext.Msg.confirm('提示信息', '确定要保存修改吗？', function(button) {
				if (button == 'yes') {
					var addData = new Array();
					var updateData = new Array();
					for (var i = 0; i < modifyRec.length; i++) {
						if(modifyRec[i].get('workFlowNo') != "合计") {
							
							if (modifyRec[i].get('detailId') != null&&modifyRec[i].get('detailId')!="") {//modify by wpzhu
								updateData.push(modifyRec[i].data);
							} else {
								addData.push(modifyRec[i].data);
							}
						}

					}
					Ext.Ajax.request({
								url : 'hr/saveOrUpdateMonthRewardDetail.action',
								method : 'post',
								params : {
									rewardId : rewardId,
									isAdd : Ext.util.JSON.encode(addData),
									isUpdate : Ext.util.JSON.encode(updateData),
									ids : ids.join(","),
									flag:"approve"

								},
								success : function(result, request) {
									var o = Ext.util.JSON
											.decode(result.responseText);
									Ext.Msg.alert("提示信息", o.msg);
									detailStore.rejectChanges();
									ids = [];
									detailStore.load({
												params : {
													rewardId : rewardId,
													workFlowState : '0'
												},
												callback : addLine
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
	
	//审批 add by sychen 20100830
	function approveTheme() {
		Ext.Ajax.request({
			url : 'hr/appvoveMonthAward.action',
			method : 'post',
			params : {
				rewardId : rewardId
			},
			success : function(response, options) {
				Ext.Msg.alert("提示", "审批成功！");
				Ext.getCmp("div_tabs").setActiveTab(1);
				Ext.getCmp("div_grid").getStore().reload();
			},
			failure : function(response, options) {

				Ext.Msg.alert('错误', '审批时出现未知错误.');
			}
		})
	}
	
	// ↓↓****************员工验证窗口****************
	// 工号
// modify by wpzhu 20100726--------------
	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
			success : function(action) {
				var result = eval("(" + action.responseText + ")");
				if (result.workerCode) {
					// 设定默认工号
					workcode = result.workerCode;
					workCode.setValue(workcode);

				}
			}
		});
	}
	getWorkCode();
	var workCode = new Ext.form.TextField({
				id : "workerCode",
				value : workcode,
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
						'managecontract/contractApproveCheckUser.action', {
							success : function(action) {
								var result = eval(action.responseText);
								if (result) {
									for(var i=0;i<detailGrid.getSelections().length;i++) {
										var sm = detailGrid.getSelectionModel();
										var selected = sm.getSelections();
										var member = selected[i];
										if (member.get("detailId") != null) {
											ids.push(member.get("detailId"));
										}
									}	
									Ext.Ajax.request({
											url : 'hr/monthRewardApprove.action',
											method : 'post',
											params : {
												detailIds : ids.join(","),
												rewardId : rewardId
											},
											success : function(result, request) {
												var o = Ext.util.JSON.decode(result.responseText);
												ids = [];
												detailStore.load({
													params : {
														rewardId : rewardId,
														workFlowState : '0'
													},
													callback : addLine
												});
												validateWin.hide();
												Ext.Msg.alert("提示信息", o.msg);
											},
											failure : function(result, request) {
												ids = [];
												Ext.Msg.alert("提示信息", "数据保存修改失败！");
											}
										})
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
				// 取得默认工号
			}
		},
		closeAction : 'hide'
	});
	// ↑↑****************员工验证窗口****************
	
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
						}, {
							id : 'btnDistribute',
							iconCls : 'upcommit',
							text : "分发给各部门",
							minWidth : 70,
							disabled : true,
							hidden :true,
							handler : function() {
								if (detailGrid.getSelectionModel().hasSelection()) {
									validateWin.show();
								} else {
									Ext.Msg.alert("提示信息", "请选择一条记录！");
								}
							}  //add by ypan 20100727
						},'-', {
							id : 'query',
							iconCls : 'query',
							minWidth : 70,
							disabled : false,
							text : "查看部门明细",
							handler : function()
	  {
	var record = detailGrid.getSelectionModel().getSelected();
		if (record == null) {
			Ext.Msg.alert("提示", "请选择一条记录！");
		} else {
			var arg = new Object();
			arg.deptName=record.get("workFlowNo");
			arg.deptId = record.get("deptId");
			arg.rewardId =rewardId  //  月奖主表Id
			
			
			//modify by ypan 20100727
			window.showModalDialog('../register/deptDetailQuery.jsp', arg,
					'status:no;dialogWidth=750px;dialogHeight=450px');
		}
	}
						}, '-', {
							id : 'btnApprove',
							iconCls : 'approve',
							minWidth : 70,
							disabled : true,
							text : "审批",
							handler : approveTheme
						}]

			});
	var detailGrid = new Ext.grid.EditorGridPanel({
				layout : 'fit',
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
							header : "rewardId",
							sortable : true,
							dataIndex : 'rewardId',
							hidden : true
						}, {
							header : "deptId",
							sortable : true,
							dataIndex : 'deptId',
							hidden : true
						}, {
							header : "部门",
							width : 100,
							align : 'center',
							sortable : true,
							dataIndex : 'workFlowNo',
							editor : detailDept.combo
						}, {
							header : "人数",
							width : 100,
							sortable : true,
							dataIndex : 'empCount',
							editor : new Ext.form.NumberField({
						 	}),
						 	renderer : function(value, cellmeta, record, rowIndex,
		                            columnIndex, store) {
		                            //add by ypan 20100727
		                            var totalSum="";
		                        if (rowIndex < store.getCount() - 1) {
		                            // 强行触发renderer事件
		                            var totalSum = 0;
		                           /* for (var i = 0; i < store.getCount() - 1; i++) {
		                                totalSum += store.getAt(i).get('empCount');
		                            }
		                            if (store.getAt(store.getCount() - 1).get('workFlowNo') == '合计') {
		                                store.getAt(store.getCount() - 1).set('empCount', totalSum);
		                            }*/
		                            return value;
		                        } else {
		                            totalSum = 0;
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                                totalSum += store.getAt(i).get('empCount');
		                            }
		                            //modify by ypan 20100727
		                            return totalSum.toFixed(2);
		                        }
		                    }
					   	}, {
							header : "上月结余",
							width : 100,
							sortable : true,
							dataIndex : 'lastMonthNum',
							editor : new Ext.form.NumberField({
							}),
							renderer : function(value, cellmeta, record, rowIndex,
		                            columnIndex, store) {
		                            //add by ypan 20100727
		                            var myValue="";    
		                        if (rowIndex < store.getCount() - 1) {
		                            // 强行触发renderer事件
		                            var totalSum = 0;
		                            //modify by ypan 20100727
		                            myValue= value;
		                        } else {
		                            totalSum = 0;
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                                totalSum += store.getAt(i).get('lastMonthNum');
		                            }
		                             //modify by ypan 20100727
		                            myValue= totalSum;
		                         }
		                         if(myValue!=null&&myValue!="")
		                          {
		                            return myValue.toFixed(2);
		                          }
		                          else return   myValue;
		                    }
						}, {
							header : "月奖金额",
							width : 100,
							sortable : true,
							dataIndex : 'monthRewardNum',
							editor : new Ext.form.NumberField({
							}),
							renderer : function(value, cellmeta, record, rowIndex,
		                            columnIndex, store) {
		                            //add by ypan 20100727
		                            var myValue="";      
		                        if (rowIndex < store.getCount() - 1) {
		                            // 强行触发renderer事件
		                            var totalSum = 0;
		                            //modify by ypan 20100727
		                            myValue= value;
		                        } else {
		                            totalSum = 0;
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                                totalSum += store.getAt(i).get('monthRewardNum');
		                            }
		                            //modify by ypan 20100727
		                            myValue= totalSum;
		                        }
		                          if(myValue!=null&&myValue!="")
		                          {
		                            return myValue.toFixed(0);
		                          }
		                          else return   myValue;
		                    }
						}, {
							header : "量化兑现",
							width : 100,
							sortable : true,
							dataIndex : 'quantifyCash',
							editor : new Ext.form.NumberField({
										style : 'cursor:pointer',
										allowNegative : false,
										allowDecimal : false,
										allowBlank : true
									}),
							renderer : function(value, cellmeta, record, rowIndex,
		                            columnIndex, store) {
		                            //add by ypan 20100727
		                            var myValue="";  
		                        if (rowIndex < store.getCount() - 1) {
		                            // 强行触发renderer事件
		                            var totalSum = 0;
		                           //modify by ypan 20100727
		                            myValue= value;
		                        } else {
		                            totalSum = 0;
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                                totalSum += store.getAt(i).get('quantifyCash');
		                            }
		                            //modify by ypan 20100727
		                            myValue= totalSum;
		                        }
		                         if(myValue!=null&&myValue!="")
		                          {
		                            return myValue.toFixed(0);
		                          }
		                          else return   myValue;
		                    }

						}, {

							header : "工会主席技师增加值",
							width : 120,
							sortable : true,
							dataIndex : 'extraAddNum',
							editor : new Ext.form.NumberField({
							}),
							renderer : function(value, cellmeta, record, rowIndex,
		                            columnIndex, store) {
		                            //add by ypan 20100727
		                            var myValue=""; 
		                        if (rowIndex < store.getCount() - 1) {
		                            // 强行触发renderer事件
		                            var totalSum = 0;
		                         //modify by ypan 20100727
		                            myValue= value;
		                        } else {
		                            totalSum = 0;
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                                totalSum += store.getAt(i).get('extraAddNum');
		                            }
		                            //modify by ypan 20100727
		                            myValue= totalSum;
		                        }
		                        if(myValue!=null&&myValue!="")
		                          {
		                            return myValue.toFixed(0);
		                          }
		                          else return   myValue;
		                    }
						}, {
							header : "月度考核",
							width : 80,
							sortable : true,
							dataIndex : 'monthAssessNum',
							editor : new Ext.form.NumberField({
								allowDecimal : false,
								allowBlank : true
							}),
							renderer : function(value, cellmeta, record, rowIndex,
		                            columnIndex, store) {
		                            //add by ypan 20100727
		                            var myValue=""; 
		                        if (rowIndex < store.getCount() - 1) {
		                            // 强行触发renderer事件
		                            var totalSum = 0;
		                          //modify by ypan 20100727
		                            myValue= value;
		                        } else {
		                            totalSum = 0;
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                                totalSum += store.getAt(i).get('monthAssessNum');
		                            }
		                            //modify by ypan 20100727
		                            myValue= totalSum;
		                        }
		                        if(myValue!=null&&myValue!="")
		                          {
		                            return myValue.toFixed(0);
		                          }
		                          else return   myValue;
		                    }		

						}, {
							header : "其他",
							width : 80,
							sortable : true,
							dataIndex : 'otherNum',
							editor : new Ext.form.NumberField({
							}),
							renderer : function(value, cellmeta, record, rowIndex,
		                            columnIndex, store) {
		                            //add by ypan 20100727
		                            var myValue=""; 
		                        if (rowIndex < store.getCount() - 1) {
		                            // 强行触发renderer事件
		                            var totalSum = 0;
		                        //modify by ypan 20100727
		                            myValue= value;
		                        } else {
		                            totalSum = 0;
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                                totalSum += store.getAt(i).get('otherNum');
		                            }
		                            //modify by ypan 20100727
		                            myValue= totalSum;
		                        }
		                        if(myValue!=null&&myValue!="")
		                          {
		                            return myValue.toFixed(0);
		                          }
		                          else return   myValue;
		                    }				
						}, {

							header : "金额",
							width : 80,
							sortable : true,
							dataIndex : 'sumTotalnum',
							editor : new Ext.form.NumberField({
									}),
							renderer : function(value, cellmeta, record, rowIndex,
		                            columnIndex, store) {
		                            //add by ypan 20100727
		                            var myValue="";
		                        if (rowIndex < store.getCount() - 1) {
		                            // 强行触发renderer事件
		                            var totalSum = 0;
		                            //modify by ypan 20100727
		                            myValue=value;
		                        } else {
		                            totalSum = 0;
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                                totalSum += store.getAt(i).get('sumTotalnum');
		                            }
		                         //modify by ypan 20100727
		                            myValue=totalSum;
		                        }
		                         //add by ypan 20100727
		                       if(myValue!=null&&myValue!="")
		                          {
		                            return myValue.toFixed(0);
		                          }
		                          else return   myValue; 
		                    }					

						}, {

							header : "备注",
							width : 180,
							sortable : true,
							dataIndex : 'memo',
							editor : new Ext.form.TextArea({
									})

						}],
				tbar : contbar,
				clicksToEdit : 1,
				autoSizeColumns : true

			});
			
	detailGrid.on('beforeedit', checkIsEdit);	
	/**
     * 判断是否是最后行，如果是则不能编辑
     */
    function checkIsEdit(obj) {
        if (obj.row == detailStore.getCount() - 1) {
            obj.cancel = true;
        }
    }

	// panel
	var detailsPanel = new Ext.Panel({
				layout : 'fit',
				border : false,
				autoScroll : true,
				items : [detailGrid]
			});
			
	return {
		panel : detailsPanel,
		setTrainId : function(record) {
			rewardId = record.data.rewardId;
			detailStore.load({
				params : {
					rewardId : rewardId,
					workFlowState : ''
				},
				callback : addLine
			});

		},
		setFeeDetailBtn : function(b) {
			if (b) {
				Ext.getCmp("btnSave").enable();
				Ext.getCmp("btnDelete").enable();
				Ext.getCmp("btnAdd").enable();
				Ext.getCmp("btnDistribute").enable();
				Ext.getCmp("btnApprove").enable();
			} else {
				Ext.getCmp("btnSave").disable();
				Ext.getCmp("btnDelete").disable();
				Ext.getCmp("btnAdd").disable();
				Ext.getCmp("btnDistribute").disable();
				Ext.getCmp("btnApprove").disable();
			}
		},
		resetInputField : function() {
			outTrainForm.getForm().reset();
//			person.combo.enable();
//			sex.enable();
//			dept.combo.enable();
			rewardId = null;
			ids = [];
			detailStore.removeAll();
			detailGrid.getView().refresh();

		}
	}

};