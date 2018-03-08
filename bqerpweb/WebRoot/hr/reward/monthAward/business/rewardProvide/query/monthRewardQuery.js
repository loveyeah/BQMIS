Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	var MyRecord = Ext.data.Record.create([{
				name : 'detailId'
			}, {
				name : 'rewardId'
			}, {
				name : 'deptId'
			}, {
				name : 'empCount'
			}, {
				name : 'lastMonthNum'
			}, {
				name : 'monthRewardNum'
			}, {
				name : 'quantifyCash'
			}, {
				name : 'extraAddNum'
			}, {
				name : 'monthAssessNum'
			}, {
				name : 'otherNum'
			}, {
				name : 'totalNum'
			}, {
				name : 'memo'
			}, {
				name : 'workFlowState'
			}, {
				name : 'workFlowNo'
			}, {
				name : 'deptName'
			}, {
				name : 'isUse'
			}]);

	var DataProxy = new Ext.data.HttpProxy({
				url : 'hr/queryMonthRewardDetailByRewardMonth.action'
			});

	var TheReader = new Ext.data.JsonReader({
				root : "list",
				totalProperty : "totalCount"
			}, MyRecord);

	var listStore = new Ext.data.Store({
				proxy : DataProxy,
				reader : TheReader
			});

	var sm = new Ext.grid.CheckboxSelectionModel();

	var trainStart = new Date();
	trainStart = trainStart.format('Y-m');
	// 培训开始时间
	var trainStartdate = new Ext.form.TextField({
				width : 100,
				style : 'cursor:pointer',
				allowBlank : true,
				readOnly : true,
				value : trainStart,
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
	function addLine() {
        var count = listStore.getCount();
		var currentIndex = count;
		var record = new MyRecord({
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
//        grid.stopEditing();
        // 插入统计行
        listStore.insert(count, record);
        grid.getView().refresh();
    };		

	function query() {
		listStore.load({
			params : {
				rewardMonth : trainStartdate.getValue(),
				start : 0,
				limit : 18
			},
			callback : addLine
		})
		Ext.Ajax.request({
			url : 'hr/queryMonthRewardByRewardMonth.action',
			waitMsg : '正在保存数据...',
			method : 'post',
			params : {
				rewardMonth : trainStartdate.getValue()
			},
			success : function(result, action) {
//				var o = eval("(" + action.response.responseText+ ")");
				var responseArray = Ext.util.JSON.decode(result.responseText);
				if(responseArray != null) {
					rewardMonth.setValue(responseArray.rewardMonth);
					monthBase.setValue(responseArray.monthBase);
					fillByDate.setValue(responseArray.fillDate.substring(0,10));
					fillByName.setValue(responseArray.fillBy);
				} else {
					rewardMonth.setValue("");
					monthBase.setValue("");
					fillByDate.setValue("");
					fillByName.setValue("");
				}
				
			},
			failure : function() {
			}
		});
	}

	var grid = new Ext.grid.GridPanel({
				layout : 'fit',
				autoHeight : true,
				autoScrolla : true,
				enableColumnMove : false,
				store : listStore,
				listeners : {
					'rowdblclick' : function() {
					}
				},
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
							dataIndex : 'workFlowNo'
						}, {
							header : "人数",
							width : 60,
							sortable : true,
							dataIndex : 'empCount',
							renderer : function(value, cellmeta, record, rowIndex,
		                            columnIndex, store) {
		                        if (rowIndex < store.getCount() - 1) {
		                            // 强行触发renderer事件
		                            var totalSum = 0;
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                                totalSum += store.getAt(i).get('empCount');
		                            }
		                            if (store.getAt(store.getCount() - 1).get('workFlowNo') == '合计') {
		                                store.getAt(store.getCount() - 1).set('empCount', totalSum);
		                            }
		                            return value;
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
							width : 60,
							sortable : true,
							dataIndex : 'lastMonthNum',
							renderer : function(value, cellmeta, record, rowIndex,
		                            columnIndex, store) {
		                        if (rowIndex < store.getCount() - 1) {
		                            // 强行触发renderer事件
		                            var totalSum = 0;
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                                totalSum += store.getAt(i).get('lastMonthNum');
		                            }
		                            if (store.getAt(store.getCount() - 1).get('workFlowNo') == '合计') {
		                                store.getAt(store.getCount() - 1).set('lastMonthNum', totalSum);
		                            }
		                            return value;
		                        } else {
		                            totalSum = 0;
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                                totalSum += store.getAt(i).get('lastMonthNum');
		                            }
		                            return totalSum;
		                        }
		                    }
						}, {
							header : "月奖金额",
							width : 60,
							sortable : true,
							dataIndex : 'monthRewardNum',
							renderer : function(value, cellmeta, record, rowIndex,
		                            columnIndex, store) {
		                        if (rowIndex < store.getCount() - 1) {
		                            // 强行触发renderer事件
		                            var totalSum = 0;
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                                totalSum += store.getAt(i).get('monthRewardNum');
		                            }
		                            if (store.getAt(store.getCount() - 1).get('workFlowNo') == '合计') {
		                                store.getAt(store.getCount() - 1).set('monthRewardNum', totalSum);
		                            }
		                            return value;
		                        } else {
		                            totalSum = 0;
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                                totalSum += store.getAt(i).get('monthRewardNum');
		                            }
		                            return totalSum;
		                        }
		                    }
						}, {
							header : "量化兑现",
							width : 60,
							sortable : true,
							dataIndex : 'quantifyCash',
							renderer : function(value, cellmeta, record, rowIndex,
		                            columnIndex, store) {
		                        if (rowIndex < store.getCount() - 1) {
		                            // 强行触发renderer事件
		                            var totalSum = 0;
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                                totalSum += store.getAt(i).get('quantifyCash');
		                            }
		                            if (store.getAt(store.getCount() - 1).get('workFlowNo') == '合计') {
		                                store.getAt(store.getCount() - 1).set('quantifyCash', totalSum);
		                            }
		                            return value;
		                        } else {
		                            totalSum = 0;
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                                totalSum += store.getAt(i).get('quantifyCash');
		                            }
		                            return totalSum;
		                        }
		                    }
						}, {

							header : "工会主席技师增加值",
							width : 120,
							sortable : true,
							dataIndex : 'extraAddNum',
							renderer : function(value, cellmeta, record, rowIndex,
		                            columnIndex, store) {
		                        if (rowIndex < store.getCount() - 1) {
		                            // 强行触发renderer事件
		                            var totalSum = 0;
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                                totalSum += store.getAt(i).get('extraAddNum');
		                            }
		                            if (store.getAt(store.getCount() - 1).get('workFlowNo') == '合计') {
		                                store.getAt(store.getCount() - 1).set('extraAddNum', totalSum);
		                            }
		                            return value;
		                        } else {
		                            totalSum = 0;
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                                totalSum += store.getAt(i).get('extraAddNum');
		                            }
		                            return totalSum;
		                        }
		                    }
						}, {
							header : "月度考核",
							width : 80,
							sortable : true,
							dataIndex : 'monthAssessNum',
							renderer : function(value, cellmeta, record, rowIndex,
		                            columnIndex, store) {
		                        if (rowIndex < store.getCount() - 1) {
		                            // 强行触发renderer事件
		                            var totalSum = 0;
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                                totalSum += store.getAt(i).get('monthAssessNum');
		                            }
		                            if (store.getAt(store.getCount() - 1).get('workFlowNo') == '合计') {
		                                store.getAt(store.getCount() - 1).set('monthAssessNum', totalSum);
		                            }
		                            return value;
		                        } else {
		                            totalSum = 0;
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                                totalSum += store.getAt(i).get('monthAssessNum');
		                            }
		                            return totalSum;
		                        }
		                    }
						}, {
							header : "其他",
							width : 80,
							sortable : true,
							dataIndex : 'otherNum',
							renderer : function(value, cellmeta, record, rowIndex,
		                            columnIndex, store) {
		                        if (rowIndex < store.getCount() - 1) {
		                            // 强行触发renderer事件
		                            var totalSum = 0;
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                                totalSum += store.getAt(i).get('otherNum');
		                            }
		                            if (store.getAt(store.getCount() - 1).get('workFlowNo') == '合计') {
		                                store.getAt(store.getCount() - 1).set('otherNum', totalSum);
		                            }
		                            return value;
		                        } else {
		                            totalSum = 0;
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                                totalSum += store.getAt(i).get('otherNum');
		                            }
		                            return totalSum;
		                        }
		                    }		
						}, {
							header : "金额",
							width : 80,
							sortable : true,
							dataIndex : 'totalNum',
							renderer : function(value, cellmeta, record, rowIndex,
		                            columnIndex, store) {
		                        if (rowIndex < store.getCount() - 1) {
		                            // 强行触发renderer事件
		                            var totalSum = 0;
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                                totalSum += store.getAt(i).get('totalNum');
		                            }
		                            if (store.getAt(store.getCount() - 1).get('workFlowNo') == '合计') {
		                                store.getAt(store.getCount() - 1).set('totalNum', totalSum);
		                            }
		                            return value;
		                        } else {
		                            totalSum = 0;
		                            for (var i = 0; i < store.getCount() - 1; i++) {
		                                totalSum += store.getAt(i).get('totalNum');
		                            }
		                            return totalSum;
		                        }
		                    }		
						}, {
							header : "备注",
							width : 160,
							sortable : true,
							dataIndex : 'memo'
						}, {
							header : "状态",
							width : 180,
							sortable : true,
							dataIndex : 'workFlowState'
						}],
				sm : sm,
				clicksToEdit : 1,
				autoSizeColumns : true,
				tbar : ["月度:", trainStartdate, {
							text : "查询",
							iconCls : 'query',
							minWidth : 70,
							handler : query
						},{
							text : "导出",
							iconCls : 'export',
							minWidth : 70,
							handler : function() {
								if (grid.getSelectionModel().hasSelection()) {
									Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_007, function(buttonobj) {
										var recs = grid.getSelectionModel().getSelections();
										var html = [];
										html.push('<table border=1><th colspan=2>部门</th><th>人数</th><th>上月结余</th><th>月奖金额</th><th>量化兑现</th><th colspan=2>工会主席技师增加值</th><th>月度考核</th><th>其他</th><th>金额</th><th colspan=2>备注</th><th>状态</th>');
										for(var i = 0; i<recs.length; i++){
											var re = recs[i];
											var workFlowNo = (re.get('workFlowNo')==null)?"":re.get('workFlowNo');
											var empCount = (re.get('empCount')==null)?"":re.get('empCount');
											var lastMonthNum = (re.get('lastMonthNum')==null)?"":re.get('lastMonthNum');
											var monthRewardNum = (re.get('monthRewardNum')==null)?"":re.get('monthRewardNum');
											var quantifyCash = (re.get('quantifyCash')==null)?"":re.get('quantifyCash');
											var extraAddNum = (re.get('extraAddNum')==null)?"":re.get('extraAddNum');
											var monthAssessNum = (re.get('monthAssessNum')==null)?"":re.get('monthAssessNum');
											var otherNum = (re.get('otherNum')==null)?"":re.get('otherNum');
											var totalNum = (re.get('totalNum')==null)?"":re.get('totalNum');
											var memo = (re.get('memo')==null)?"":re.get('memo');
											var workFlowState = (re.get('workFlowState')==null)?"":re.get('workFlowState');
											html.push('<tr><td colspan=2>'+workFlowNo+'</td><td>'+empCount+'</td>' +
													'<td>'+lastMonthNum+'</td><td>'+monthRewardNum+'</td>' +
													'<td>'+quantifyCash+'</td><td colspan=2>'+extraAddNum+'</td>' +
															'<td>'+monthAssessNum+'</td><td>'+otherNum+'</td>' +
																	'<td>'+totalNum+'</td><td colspan=2>'+memo+'</td><td>'+workFlowState+'</td></tr>');
										}
										html.push('</table>');
										html = html.join(''); // 最后生成的HTML表格
										tableToExcel(html);
									});
								} else {
									Ext.Msg.alert("提示信息", "请选择一条记录！");
								}
							}
						}, {//add by qxjiao 20100907
							id : 'query',
							iconCls : 'query',
							minWidth : 70,
							disabled : false,
							text : "查看部门明细",
							handler : deptDetailQuery
						}]
			});
			
				function deptDetailQuery()
	{
	var record = grid.getSelectionModel().getSelected();
		if (record == null) {
			Ext.Msg.alert("提示", "请选择一条记录！");
		} else {
			var arg = new Object();
			arg.deptName=record.get("workFlowNo");
			arg.deptId = record.get("deptId");
			arg.rewardId =record.get('rewardId');  //  月奖主表Id
			
			
			
			window.showModalDialog('deptDetailQuery.jsp', arg,
					'status:no;dialogWidth=750px;dialogHeight=450px');
		}
	}
	
	
	 /**
     * 将HTML转化为Excel文档
     */
    function tableToExcel(tableHTML){
		window.clipboardData.setData("Text",tableHTML);
		try{
			var ExApp = new ActiveXObject("Excel.Application");
			var ExWBk = ExApp.workbooks.add();
			var ExWSh = ExWBk.worksheets(1);
			ExApp.visible = true;
		}catch(e){
			if(e.number != -2146827859){
				Ext.Msg.alert('提示','您的电脑没有安装Microsoft Excel软件!')
			}
			return false;
		}
		ExWBk.worksheets(1).Paste;
	}		
			
	var rewardMonth = new Ext.form.TextField({
				width : 250,
				style : 'cursor:pointer',
				name : "reward.rewardMonth",
				fieldLabel : '月度',
				allowBlank : true,
				readOnly : true
			});
			
	// 月奖基数
	var monthBase = new Ext.form.TextField({
				id : 'deptName',
				fieldLabel : '月奖基数',
				name : 'reward.monthBase',
				width : 250,
				readOnly : true
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
	var fillByDate = new Ext.form.TextField({
				width : 250,
				style : 'cursor:pointer',
				fieldLabel : '填写时间',
				allowBlank : true,
				readOnly : true
			});		
			
	var outTrainForm = new Ext.form.FormPanel({
		title : '主信息',
		bodyStyle : "padding:5px 5px 0",
		labelAlign : 'right',
		// labelWidth : 100,
		autoHeight : true,
		layout : 'form',
		border : false,
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
							}]

				}]

	});		
	
	var detailsPanel = new Ext.Panel({
		layout : 'border',
		border : false,
		autoScroll : true,
		items : [{
					region : 'center',
					items : [grid]
				},{
					region : 'south',
					height : 150,
					split : true,
					items : [outTrainForm]
				}]
	});
	query();
	var view = new Ext.Viewport({
				layout : 'fit',
				items : [detailsPanel]
			});


	});
