Ext.ns("trainMaint.trainRegister");
trainMaint.trainRegister = function() {
	var rewardId = null;
	var deptId;
	var sessDeptName;
	var sessDeptCode;
	var sessWorname;
	var sessWorcode;
	function getDeptCode() {
		Ext.lib.Ajax.request('POST', 'hr/initRewardGrand.action?deptId='
						+ deptId, {
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						if (result != "") {
							strDeptId.setValue(result[0])
							deptName.setValue(result[1])
							// 设定默认工号
						} else {
							deptName.setValue(sessDeptName)
							strDeptId.setValue(deptId)
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
							strGroupId.setValue(deptId);
//							getDeptCode();
							sessDeptName = result.deptCode;
							sessDeptName = result.deptName;
							fillByName.setValue(sessWorname);
							strFillByCode.setValue(sessWorcode);
							workCode.setValue(sessWorcode);
						}
					}
				});
	}

	var grantMonthStart = new Date();
	grantMonthStart = grantMonthStart.format('Y-m');
	// 时间
	var rewardMonth = new Ext.form.TextField({
				width : 250,
				style : 'cursor:pointer',
				name : "reward.rewardMonth",
				fieldLabel : '月度',
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
			
	var handedDateStart = new Date();		
	handedDateStart = handedDateStart.format('Y-m-d');		
	// 时间
	var handedDate = new Ext.form.TextField({
		width : 250,
		style : 'cursor:pointer',
		name : "reward.handedDate",
		fieldLabel : '交表日期',
		allowBlank : true,
		readOnly : true,
		value : handedDateStart,
		listeners : {
			focus : function() {
				WdatePicker({
							isShowClear : true,
							startDate : '%y-%M-%d',
							dateFmt : 'yyyy-MM-dd'
						});
				this.blur();
			}

		}
	});		
	
		// 全厂系数
	var coefficient = new Ext.form.TextField({
				id : 'coefficient',
				fieldLabel : '全厂系数',
				name : 'reward.coefficient',
				//allowBlank:false,
				width : 250
			})

	// 月奖基数
	var monthBase = new Ext.form.TextField({
				id : 'deptName',
				fieldLabel : '月奖基数',
				name : 'reward.monthBase',
				width : 250,
				value:800
			})
	var strDeptId = new Ext.form.Hidden({
				id : 'deptId',
				name : 'reward.deptId'
			})
	var strGroupId = new Ext.form.Hidden({
				id : 'groupId',
				name : 'reward.groupId'
			})
	// 制表人
	var fillByName = new Ext.form.TextField({
				id : 'fillByName',
				fieldLabel : '填写人',
				readOnly : true,
				width : 250
			})
	var strFillByCode = new Ext.form.Hidden({
				id : 'strFillByCode',
				name : 'reward.fillBy'
			})
	// 制表时间
	var fillDate = new Date();
	fillDate = fillDate.format('Y-m-d');

	var fillByDate = new Ext.form.TextField({
				width : 250,
				style : 'cursor:pointer',
				fieldLabel : '填写时间',
				allowBlank : true,
				value : fillDate,
				readOnly : true
			});
	
	function resetFromRec() {
		outTrainForm.getForm().reset();
//		person.combo.enable();
//		sex.enable();
//		dept.combo.enable();
		getWorkCode();
		rewardId = null;
		ids = [];
		detailStore.removeAll();
	//	detailGrid.getView().refresh();

	}		
	
	var btnAdd = new Ext.Button({
				text : '新增',
				iconCls : 'add',
				handler : resetFromRec
			})
	var save = new Ext.Button({
				text : '保存',
				iconCls : 'save',
				handler : function() {
					var detailIsNull = false;
					if(detailGrid.getStore().getCount()==0) {
						detailIsNull = true;
					}
					Ext.Msg.wait("正在保存,请等待...");
					outTrainForm.getForm().submit({
						method : 'POST',
						params : {
							detailIsNull : detailIsNull,
							rewardId : rewardId
						},
						url : 'hr/saveOrUpdateMonthReward.action',
						success : function(form, action) {
							var o = eval("(" + action.response.responseText+ ")");
							Ext.Msg.alert("注意", o.msg);
							rewardId = o.rewardId;
							Ext.getCmp("btnAdd").setDisabled(false);
							Ext.getCmp("btnDelete").setDisabled(false);
							Ext.getCmp("btnSave").setDisabled(false);
							Ext.getCmp("btnDistribute").setDisabled(false);
							if(rewardId != "" && typeof(rewardId) != 'undefined') {
								detailStore.load({
									params : {
										rewardId : rewardId
									},
									callback : addLine
								});
							}
							// if (o.msg.indexOf("成功") != -1) {
							// outTrainForm.getForm().reset();
							// }
						},
						faliue : function() {
							Ext.Msg.alert('错误', '出现未知错误.');
						}
					});
				}

			});
	// 删除
	var btnDel = new Ext.Button({
				text : '删除',
				iconCls : 'delete',
				handler : function() {
					//alert();
					Ext.Ajax.request({
						url : 'hr/delateMonthReward.action',
						params : {
							rewardId : rewardId
						},
						method : 'post',
						waitMsg : '正在加载数据...',
						success : function(result, request) {
								var o = Ext.util.JSON.decode(result.responseText);
								outTrainForm.getForm().reset();
								rewardId = "";
								detailStore.removeAll();
								detailGrid.getView().refresh();
								Ext.Msg.alert("提示信息", o.msg);
						},
						failure : function(result, request) {
							Ext.MessageBox.alert('错误', '操作失败,请联系管理员!');
						}
					});
				}
			})
	// 取消
	var btnCancel = new Ext.Button({
				text : '取消',
				iconCls : 'reflesh',
				handler : function() {
				}
			})
	// 计算
	var btnCalculate = new Ext.Button({
				text : '计算',
				iconCls : 'save',
				handler : function() {
					var totalCount =0;
					for(var i = 0;i<detailStore.getCount()-1;i++) {
						var record = detailStore.getAt(i);
						var totalMoney = record.get('lastMonthNum')+record.get('monthRewardNum')
									+record.get('quantifyCash')+record.get('extraAddNum')
									+record.get('monthAssessNum')+record.get('otherNum');
						//alert(totalMoney);
						record.set('totalNum',totalMoney);
						totalCount+=totalMoney;
						if(i==detailStore.getCount()-2){
							record = detailStore.getAt(i+1);
							record.set('totalNum',totalCount);
						}
					}
					Ext.Msg.alert("提示", "计算成功！");
				}
			})		
	// 上报
	var btnReport = new Ext.Button({
				text : '上报',
				iconCls : 'upcommit',
				handler : function() {
					if(rewardId != "") {
						Ext.Ajax.request({
							url : 'hr/monthRewardReport.action',
							method : 'post',
							params : {
								rewardId : rewardId
							},
							success : function(result, request) {
								var o = Ext.util.JSON.decode(result.responseText);
								outTrainForm.getForm().reset();
								rewardId = "";
								detailStore.removeAll();
								detailGrid.getView().refresh();
								Ext.Msg.alert("提示信息", o.msg);
							},
							failure : function(result, request) {
								Ext.Msg.alert("提示信息", "数据保存修改失败！");
							}
						})
					} else {
					}	
				}
			})

		
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
	
	detailStore.on("load",function()
	{
		Ext.Msg.hide();
	});		
			
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
					'detailId' : null,
					'rewardId' : null,
					'deptId' : null,
					'deptName' : null,
					'empCount' : null,
					'lastMonthNum' : null,
					'monthRewardNum' : null,
					'quantifyCash' : null,
					'extraAddNum' : null,
					'monthAssessNum' : null,
					'otherNum' : null,
					'totalNum' : null,
					'memo' : null,
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
					'detailId' : null,
					'rewardId' : rewardId,
					'deptId' : null,
					'deptName' : null,
					'empCount' : null,
					'lastMonthNum' : null,
					'monthRewardNum' : null,
					'quantifyCash' : null,
					'extraAddNum' : null,
					'monthAssessNum' : null,
					'otherNum' : null,
					'totalNum' : null,
					'memo' : null,
					'workFlowNo' : null
				});
		detailGrid.stopEditing();
		detailStore.insert(currentIndex-1, o);
		sm.selectRow(currentIndex-1);
		detailGrid.startEditing(currentIndex-1, 1);
		detailGrid.getView().refresh();
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
							if (modifyRec[i].get('detailId') != null) {
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
									flag:"report"

								},
								success : function(result, request) {
									var o = Ext.util.JSON
											.decode(result.responseText);
									Ext.Msg.alert("提示信息", o.msg);
									detailStore.rejectChanges();
									ids = [];
									detailStore.load({
												params : {
													rewardId : rewardId/*,
													workFlowState : "'0','1'"*/
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
	function deptDetailQuery()
	{
	var record = detailGrid.getSelectionModel().getSelected();
		if (record == null) {
			Ext.Msg.alert("提示", "请选择一条记录！");
		} else {
			var arg = new Object();
			arg.deptName=record.get("workFlowNo");
			arg.deptId = record.get("deptId");
			arg.rewardId =rewardId  //  月奖主表Id
			
			
			
			window.showModalDialog('deptDetailQuery.jsp', arg,
					'status:no;dialogWidth=750px;dialogHeight=450px');
		}
	}
	
		// ↓↓****************员工验证窗口****************
	// 工号
	var workCode = new Ext.form.TextField({
				id : "workerCode",
//				value : workcode,
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
														rewardId : rewardId/*,
														workFlowState : "'0','1'"*/
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
						}, '-',{//add by wpzhu 20100724
							id : 'query',
							iconCls : 'query',
							minWidth : 70,
							disabled : false,
							text : "查看部门明细",
							handler : deptDetailQuery
						},'-', {
							id : 'btnSave',
							iconCls : 'save',
							text : "保存修改",
							minWidth : 70,
							disabled : true,
							handler : saveTheme
						},'-', {
							id : 'btnDistribute',
							iconCls : 'upcommit',
							text : "分发给各部门",
							minWidth : 70,
							disabled : true,
							handler : function() {
								if (detailGrid.getSelectionModel().hasSelection()) {//modify by wpzhu 20100819
									
									var res = detailGrid.getSelectionModel().getSelections();
									var flag=false;
									for(i=0;i<res.length;i++)
									{
										record=res[i];
								if(record.get("workFlowState")=="1"||record.get("workFlowState")=="2"
								||record.get("workFlowState")=="3"	||record.get("workFlowState")=="4")
										flag=true;
									}
									
									
									if(flag)
									{
										
											Ext.Msg.alert("提示信息", "请选择所有未分发的记录！");
											return;
								     
									}else
									{
										
									validateWin.show();
									}
								} else {
									Ext.Msg.alert("提示信息", "请选择一条记录！");
								}
							}  //add by ywliu 20100810
						}]

			});
			
		
	var detailGrid = new Ext.grid.EditorGridPanel({
				layout : 'fit',
//				autoHeight : true,
				autoScroll : true,
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
								allowNegative : false,
								allowDecimal : false,
								allowBlank : true
						 	}),
						 	renderer : function(value, cellmeta, record, rowIndex,
		                            columnIndex, store) {
		                        if (rowIndex < store.getCount() - 1) {//modify by wpzhu 20100716
		                            // 强行触发renderer事件
		                        	var empCount=record.data.empCount;
		                        	return empCount;
		                          
		                        } else {
		                            totalSum = 0;
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                                totalSum += store.getAt(i).get('empCount');
		                            }
		                            return totalSum;
		                        }
		                    }
					   	}, {
							header : "上月结余",
							width : 100,
							sortable : true,
							dataIndex : 'lastMonthNum',
							editor : new Ext.form.NumberField({
								allowNegative : false,
								allowDecimal : false,
								allowBlank : true
							}),
							renderer : function(value, cellmeta, record, rowIndex,
		                            columnIndex, store) {
		                           var myValue=""; 	
		                        if (rowIndex < store.getCount() - 1) {
		                            // 强行触发renderer事件
		                        	var  lastMontNum=record.data.lastMonthNum;
		                        	
		                        	myValue=  lastMontNum;
		                        	
		                           
		                        } else {
		                            totalSum = 0;
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                                totalSum += store.getAt(i).get('lastMonthNum');
		                            }
		                            myValue= totalSum;
		                        }
		                        if(myValue!=null&&myValue!="")
		                        {
		                        	return myValue.toFixed(2);
		                        }
		                        else return myValue;
		                    }
						}, {
							header : "月奖金额",
							width : 100,
							sortable : true,
							dataIndex : 'monthRewardNum',
							editor : new Ext.form.NumberField({
								allowNegative : false,
								allowDecimal : false,
								allowBlank : true
							}),
							renderer : function(value, cellmeta, record, rowIndex,
		                            columnIndex, store) {
		                            	  var myValue=""; 	
		                        if (rowIndex < store.getCount() - 1) {
		                            // 强行触发renderer事件
		                        	var monthRewardNum=record.data.monthRewardNum;
		                            myValue=  monthRewardNum;
		                        } else {
		                            totalSum = 0;
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                                totalSum += store.getAt(i).get('monthRewardNum');
		                            }
		                            myValue= totalSum;
		                        }
		                         if(myValue!=null&&myValue!="")
		                        {
		                        	return myValue.toFixed(0);
		                        }
		                        else return myValue;
		                        
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
		                            	  var myValue=""; 	
		                        if (rowIndex < store.getCount() - 1) {
		                            // 强行触发renderer事件
		                        	var quantifyCash=record.data.quantifyCash;
		                        	myValue=  quantifyCash;
		                           
		                        } else {
		                            totalSum = 0;
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                                totalSum += store.getAt(i).get('quantifyCash');
		                            }
		                            myValue= totalSum;
		                        }
		                         if(myValue!=null&&myValue!="")
		                        {
		                        	return myValue.toFixed(0);
		                        }
		                        else return myValue;
		                    }

						}, {

							header : "工会主席技师增加值",
							width : 120,
							sortable : true,
							dataIndex : 'extraAddNum',
							editor : new Ext.form.NumberField({
								style : 'cursor:pointer',
								allowNegative : false,
								allowDecimal : false,
								allowBlank : true
							}),
							renderer : function(value, cellmeta, record, rowIndex,
		                            columnIndex, store) {
		                           var myValue=""; 
		                        if (rowIndex < store.getCount() - 1) {
		                            // 强行触发renderer事件
		                        	var extraAddNum=record.data.extraAddNum;
		                        	myValue= extraAddNum;
		                           
		                        } else {
		                            totalSum = 0;
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                                totalSum += store.getAt(i).get('extraAddNum');
		                            }
		                            myValue= totalSum;
		                        }
		                          if(myValue!=null&&myValue!="")
		                        {
		                        	return myValue.toFixed(0);
		                        }
		                        else return myValue;
		                        
		                    }
						}, {
							header : "月度考核",
							width : 80,
							sortable : true,
							dataIndex : 'monthAssessNum',
							editor : new Ext.form.NumberField({
								style : 'cursor:pointer',
								allowDecimals : false,
								allowBlank : true
							}),
							renderer : function(value, cellmeta, record, rowIndex,
		                            columnIndex, store) {
		                            	 var myValue=""; 
		                        if (rowIndex < store.getCount() - 1) {
		                            // 强行触发renderer事件
		                        	var monthAssessNum=record.data.monthAssessNum;
		                        	myValue= monthAssessNum;
		                        } else {
		                            totalSum = 0;
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                                totalSum += store.getAt(i).get('monthAssessNum');
		                            }
		                            myValue= totalSum;
		                        }
		                           if(myValue!=null&&myValue!="")
		                        {
		                        	//return  parseFloat(myValue).toFixed(2);
		                        	return  myValue.toFixed(0);
		                        	
		                        }
		                        else
		                        return myValue;
		                    }		

						}, {
							header : "其他",
							width : 80,
							sortable : true,
							dataIndex : 'otherNum',
							editor : new Ext.form.NumberField({
								style : 'cursor:pointer',
								allowDecimals : false,
								allowBlank : true
							}),
							renderer : function(value, cellmeta, record, rowIndex,
		                            columnIndex, store) {
		                            	 var myValue=""; 
		                        if (rowIndex < store.getCount() - 1) {
		                            // 强行触发renderer事件
		                        	var otherNum=record.data.otherNum;
		                        	myValue= otherNum;
		                            
		                        } else {
		                            totalSum = 0;
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                                totalSum += store.getAt(i).get('otherNum');
		                            }
		                            myValue= totalSum;
		                        }
		                           if(myValue!=null&&myValue!="")
		                        {
		                        	return myValue.toFixed(0);
		                        }
		                        else return myValue;
		                    }				
						}, {

							header : "金额",
							width : 80,
							sortable : true,
							dataIndex : 'totalNum',
//							editor : new Ext.form.NumberField({//update by sychen 20100903
//										style : 'cursor:pointer',
//										allowNegative : false,
//										allowDecimals : false,
//										allowBlank : true
//									}),
							renderer : function(value, cellmeta, record, rowIndex,
		                            columnIndex, store) {
		                            var myValue="";  	
		                        if (rowIndex < store.getCount() - 1) {
		                            // 强行触发renderer事件
		                        	var totalNum=record.data.totalNum;
		                        	myValue= totalNum;
		                           
		                        } else {
		                            totalSum = 0;
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                                totalSum += store.getAt(i).get('totalNum');
		                            }
		                            myValue= totalSum;
		                        }
		                        
		                           if(myValue!=null&&myValue!="")
		                        {
		                        	return myValue.toFixed(0);
		                        }
		                        else return myValue;
		                    }					

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

						},
						{//add by wpzhu 20100819

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
   //add by sychen 20100903
    detailGrid.on("afteredit", function(obj) {
		var totalSum=obj.record.get("lastMonthNum")+obj.record.get("monthRewardNum")+obj.record.get("quantifyCash")
		              +obj.record.get("extraAddNum")+obj.record.get("monthAssessNum")+obj.record.get("otherNum");
	    obj.record.set("totalNum",totalSum);
		totalSum = 0;
		for (var i = 0; i < detailStore.getCount() - 1; i++) {
			totalSum += detailStore.getAt(i)
					.get('totalNum');
		}
		if (detailStore.getAt(detailStore.getCount() - 1)
				.get('workFlowNo') == "合计") {
			detailStore.getAt(detailStore.getCount() - 1).set(
					'totalNum', totalSum);
		}
	})
   //add by sychen 20100903 end 
	
	var outTrainForm = new Ext.form.FormPanel({
		title : '主信息',
		bodyStyle : "padding:5px 5px 0",
		labelAlign : 'right',
		// labelWidth : 100,
		autoHeight : true,
		layout : 'form',
		border : false,
		tbar : [btnAdd, '-', save, '-', btnDel, '-', btnCancel, '-', btnCalculate,'-', btnReport],
		items : [{
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
											items : [rewardMonth]
										}, {
											columnWidth : .5,
											layout : 'form',
											labelWidth : 100,
											border : false,
											items : [monthBase]
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
							}, {
								border : false,
								layout : 'column',
								items : [{
											columnWidth : .5,
											layout : 'form',
											labelWidth : 100,
											border : false,
											items : [handedDate]
										},{
											columnWidth : .5,
											layout : 'form',
											labelWidth : 100,
											border : false,
											items : [coefficient]
										}]
							}]

				}]

	});
	
	getWorkCode();

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
							layout:'fit',//modify by wpzhu 20100716
							autoScroll : true,
							region : 'center',
							items : [detailGrid]
						}]
			});
	return {
		panel : detailsPanel,
		setTrainId : function(record) {
			outTrainForm.getForm().reset();
			rewardId = record.data.rewardId;
			rewardMonth.setValue(record.data.rewardMonth);
			monthBase.setValue(record.data.monthBase);
			handedDate.setValue(record.data.handedDate.substring(0,10));
			fillByName.setValue(record.data.fillBy);
			fillByDate.setValue(record.data.fillDate.substring(0,10));
			coefficient.setValue(record.data.coefficient); //add by fyyang 20100722
			detailStore.load({
				params : {
					rewardId : rewardId/*,
					workFlowState :"'0','1'"*/  //modify by wpzhu 20100819
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
			} else {
				Ext.getCmp("btnSave").disable();
				Ext.getCmp("btnDelete").disable();
				Ext.getCmp("btnAdd").disable();
				Ext.getCmp("btnDistribute").disable();
			}
		},
		resetInputField : resetFromRec
	}

};