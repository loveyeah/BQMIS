Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	var MyRecord = Ext.data.Record.create([{
				name : 'bigGrantId',
				mapping : 0
			}, {
				name : 'bigGrantMonth',
				mapping : 1
			}, {
				name : 'bigAwardId',
				mapping : 2
			}, {
				name : 'bigAwardName',
				mapping : 3
			}, {
				name : 'deptId',
				mapping : 4
			}, {
				name : 'deptName',
				mapping : 5
			}, {
				name : 'groupId',
				mapping : 6
			}, {
				name : 'groupName',
				mapping : 7
			}, {
				name : 'fillBy',
				mapping : 8
			}, {
				name : 'fillDate',
				mapping : 9
			}, {
				name : 'workName',
				mapping : 10
			}]);

	var DataProxy = new Ext.data.HttpProxy({
				url : 'hr/getBigRewardGrandList.action'
			});

	var TheReader = new Ext.data.JsonReader({
			// root : "list",
			// totalProperty : "totalCount"
			}, MyRecord);

	var listStore = new Ext.data.Store({
				proxy : DataProxy,
				reader : TheReader
			});

	listStore.baseParams = {

	}
	listStore.load()
	var sm = new Ext.grid.CheckboxSelectionModel();

	var month = new Date();
	month = month.format('Y-m');
	// 时间
	var strGrandMonth = new Ext.form.TextField({
				width : 100,
				style : 'cursor:pointer',
				allowBlank : true,
				readOnly : true,
				value : month,
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

	function query() {
		if (strGrandMonth.getValue() == null || strGrandMonth.getValue() == "") {
			Ext.Msg.alert('提示', '请选择时间！')
		} else {
			listStore.load({
						params : {
							strGrandMonth : strGrandMonth.getValue(),
							start : 0,
							limit : 18
						}
					})
		}

	}

	var grid = new Ext.grid.GridPanel({
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
							dataIndex : 'bigGrantId',
							hidden : true
						}, {
							header : "时间",
							width : 150,
							allowBlank : false,
							sortable : true,
							hidden : true,
							dataIndex : 'grantMonth'
						}, {
							header : "大奖ID",
							width : 150,
							allowBlank : false,
							sortable : true,
							hidden : true,
							dataIndex : 'bigAwardId'
						}, {
							header : "大奖名称",
							width : 150,
							allowBlank : false,
							sortable : true,
							dataIndex : 'bigAwardName'
						}, {
							header : "部门",
							width : 150,
							allowBlank : false,
							sortable : true,
							dataIndex : 'deptName'
						}, {
							header : "班组",
							width : 150,
							allowBlank : false,
							sortable : true,
							dataIndex : 'groupName'
						}, {
							header : "制表人",
							width : 150,
							allowBlank : false,
							sortable : true,
							dataIndex : 'workName'
						}, {
							header : "制表时间",
							width : 150,
							allowBlank : false,
							sortable : true,
							dataIndex : 'fillDate'
						}],
				sm : sm,
				autoSizeColumns : true,
				viewConfig : {
					forceFit : true
				},
				tbar : ["培训时间:", strGrandMonth, '-', {
							text : "查询",
							iconCls : 'query',
							minWidth : 70,
							handler : query
						}, {
							text : "修改",
							iconCls : 'update',
							minWidth : 70,
							handler : function() {
								// tabs.setActiveTab(0);
								// trainRegister.setFeeDetailBtn(false);
								// trainRegister.resetInputField();
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
				activeTab : 0,
				tabPosition : 'bottom',
				plain : false,
				defaults : {
					autoScroll : true
				},
				items : [{
							id : 'trainRegister',
							title : '大奖发放上报录入',
							items : trainRegister.panel,
							autoScroll : true,
							layout : 'fit'
						}, {
							id : 'trainList',
							title : '大奖发放上报列表',
							autoScroll : true,
							items : grid,
							layout : 'fit'
						}]
			});

	var view = new Ext.Viewport({
				layout : 'fit',
				items : [tabs]
			});

		// trainRegister.rbgTrainCharacter.setValue(2);

});
