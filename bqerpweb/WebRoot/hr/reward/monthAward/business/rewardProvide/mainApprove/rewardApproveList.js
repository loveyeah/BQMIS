Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	var MyRecord = Ext.data.Record.create([{
				name : 'rewardId'
			}, {
				name : 'rewardMonth'
			}, {
				name : 'monthBase'
			}, {
				name : 'handedDate'
			}, {
				name : 'fillBy'
			}, {
				name : 'fillDate'
			}, {
				name : 'workFlowState'
			}, {
				name : 'workFlowNo'
			}, {
				name : 'isUse'
			}, {
				name : 'enterpriseCode'
			}]);

	var DataProxy = new Ext.data.HttpProxy({
				url : 'hr/monthRewardApproveQuery.action'
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

	var trainStart = new Date().format('Y-m');
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
//	listStore.on('load',function() {
//		if(listStore.getCount() == 0) {
//			var trainStart = new Date().format('Y-m');
//			trainStartdate.setValue(trainStart);
//		} else {
//			var record = listStore.getAt(0);
//			trainStartdate.setValue(record.get('rewardMonth'));
//			listStore.removeAll();
//			listStore.add(record);
//		}
//	})
	
	function query() {
		listStore.load({
					params : {
						rewardMonth : trainStartdate.getValue(),
						start : 0,
						limit : 18
					}
				})
	}

	var grid = new Ext.grid.GridPanel({
				id:'div_grid',
				region : "center",
				layout : 'fit',
				store : listStore,
				listeners : {
					'rowdblclick' : function() {
						var rec = grid.getSelectionModel().getSelected();
						tabs.setActiveTab(0);
						trainRegister.setTrainId(rec);
						trainRegister.setFeeDetailBtn(true);
					}
				},
				columns : [sm, new Ext.grid.RowNumberer({
									header : '行号',
									width : 35,
									align : 'left'
								}), {
							header : "ID",
							width : 35,
							sortable : true,
							dataIndex : 'rewardId',
							hidden : true
						}, {
							header : "月度",
							width : 150,
							allowBlank : false,
							sortable : true,
							dataIndex : 'rewardMonth'
						}, {
							header : "月奖基数",
							width : 150,
							allowBlank : false,
							sortable : true,
							dataIndex : 'monthBase'
						}, {
							header : "填写人",
							width : 150,
							allowBlank : false,
							sortable : true,
							dataIndex : 'fillBy'
						}, {
							header : "填写时间",
							width : 150,
							allowBlank : false,
							sortable : true,
							dataIndex : 'fillDate',
							renderer : function(val) {
								if(val != "") {
									return val.substring(0,10);
								}
							}
						}],
				sm : sm,
				autoSizeColumns : true,
				viewConfig : {
					forceFit : true
				},
				tbar : ["月度:", trainStartdate, {
							text : "查询",
							iconCls : 'query',
							minWidth : 70,
							handler : query
						},{
							text : "签字处理",
							iconCls : 'write',
							minWidth : 70,
							handler : function() {
								if (grid.getSelectionModel().hasSelection()) {
									if(grid.getSelections().length == 1) {
										var rec = grid.getSelectionModel().getSelected();
										tabs.setActiveTab(0);
										trainRegister.setTrainId(rec);
										trainRegister.setFeeDetailBtn(true);
									} else {
										Ext.Msg.alert("提示信息", "请选择一条记录！");
									}
								}
							}
						}],
				// 分页
				bbar : new Ext.PagingToolbar({
							pageSize : 18,
							store : listStore,
							displayInfo : true,
							displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
							emptyMsg : "没有记录"
						})
			});

	var trainRegister = new trainMaint.trainRegister();
	var tabs = new Ext.TabPanel({
				id:'div_tabs',
				activeTab : 1,
				tabPosition : 'bottom',
				plain : false,
				defaults : {
					autoScroll : true
				},
				items : [{//modify by wpzhu 20100717
							id : 'trainRegister',
							title : '奖金发放审批',
							items : trainRegister.panel,
							autoScroll : true,
							layout : 'fit'
						}, {
							id : 'trainList',
							title : '奖金发放列表',
							autoScroll : true,
							items : grid,
							layout : 'fit'
						}]
			});
	query();
	var view = new Ext.Viewport({
				layout : 'fit',
				items : [tabs]
			});

	});
