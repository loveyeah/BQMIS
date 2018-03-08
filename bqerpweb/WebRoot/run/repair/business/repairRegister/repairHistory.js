Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	// 专业
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
				id : 'speciality',
				fieldLabel : "检修专业",
				store : storeRepairSpecail,
				displayField : "specialityName",
				valueField : "specialityId",
				// hiddenName : 'main.specialityId',
				mode : 'remote',
				triggerAction : 'all',
				value : '',
				readOnly : true,
		        disabled:true,
				anchor : "85%"
			});
			
			storeRepairSpecail.load();
			
		 storeRepairSpecail.on('load', function(e, records, o) {
		cbxRepairSpecail.setValue(parent.winSpecial);
	});
	// 版本
//	var edition = new Ext.form.TextField({
//				id : 'edition',
//				fieldLabel : "版本",
//				name : 'fillName',
//				anchor : "85%"
//			})
			
	var versionStore = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'manageplan/findVerisonListBySpecial.action?specialId='+parent.winSpecial
		}),
		reader : new Ext.data.JsonReader({
			root : 'list'
		}, [{
			name : 'version',
			mapping : 0
		}, {
			name : 'versionName',
			mapping : 1
		}])
	});
	
	
	
					
	 versionStore.on('load', function(e, records, o) {
		edition.setValue(parent.winVersion);
	});
	versionStore.load();
	
	var edition = new Ext.form.ComboBox({
		id : 'cbEdition',
		fieldLabel : "版本",
		store : versionStore,
		displayField : "versionName",
		valueField : "version",
		//hiddenName : 'version',
		mode : 'remote',
		triggerAction : 'all',
		value : '',
		readOnly : true,
//		disabled:true,
		width : 120
	})
	
			

	// grid中的数据Record
	var gridRecord = new Ext.data.Record.create([{
				name : 'specialityId',
				mapping : 0
			}, {
				name : 'specialityName',
				mapping : 1
			}, {
				name : 'repairProjectName',
				mapping : 2
			}, {
				name : 'workingCharge',
				mapping : 3
			}, {
				name : 'workingChargeName',
				mapping : 4
			}, {
				name : 'workingMenbers',
				mapping : 5
			}, {
				name : 'workingTime',
				mapping : 6
			}, {
				name : 'repairProjectId',
				mapping : 7
			},{
				name : 'acceptanceSecond',
				mapping : 8
			}, {
				name : 'acceptanceSecondName',
				mapping : 9
			},{
				name : 'acceptanceThree',
				mapping : 10
			}, {
				name : 'acceptanceThreeName',
				mapping : 11
			}]);

	// grid的store
	var queryStore = new Ext.data.JsonStore({
				url : 'manageplan/findHistoryInfo.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : gridRecord
			});
	// 定义选择行
	var sm = new Ext.grid.CheckboxSelectionModel({
			// singleSelect : true
			});

	var columns = new Ext.grid.ColumnModel([sm, new Ext.grid.RowNumberer({
						header : '行号',
						width : 35
					}), {
				header : "专业ID",
				sortable : true,
				hidden : true,
				width : 100,
				dataIndex : 'specialityId'
			}, {
				header : "专业",
				sortable : true,
				width : 100,
				dataIndex : 'specialityName'
			}, {
				header : "项目名称",
				sortable : true,
				width : 100,
				dataIndex : 'repairProjectName'
			}, {
				header : "工作负责人",
				sortable : true,
				width : 80,
				dataIndex : 'workingChargeName'
			}, {
				header : "工作成员",
				width : 150,
				dataIndex : 'workingMenbers'
			}, {
				header : "工作时间",
				width : 100,
				dataIndex : 'workingTime'
			}])
	columns.defaultSortable = true;

	// 页面的Grid主体
	var gridOrder = new Ext.grid.GridPanel({
				layout : 'fit',
				store : queryStore,
				height : 300,
				cm : columns,
				tbar : [{
							text : '专业'
						}, cbxRepairSpecail, {
							text : '版本'
						}, edition, {
							id : 'btnSubmit',
							text : "查询",
							iconCls : 'query',
							handler : findFuzzy
						}, '-', {
							id : 'btnSubmit',
							text : "确定",
							iconCls : 'query',
							handler : enter
						}],
				sm : sm,
				border : false,
				split : true,
				autoScroll : true
			});
	function findFuzzy() {
		queryStore.load({
					params : {
						spId : cbxRepairSpecail.getValue(),
						version : edition.getValue()
					}
				})
	}
	function enter() {
		gridOrder.stopEditing();
		var modifyRec = gridOrder.getSelectionModel().getSelections();
		//modify by fyyang 20100525
		parent.myMate=modifyRec;
		parent.myWin.close();
//		window.returnValue = modifyRec;
//		window.close();
	}
	var panelView = new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				border : false,
				frame : false,
				items : [{
							bodyStyle : "padding: 1,1,1,0",
							region : "center",
							border : false,
							frame : false,
							layout : 'fit',
							items : [gridOrder]
						}]
			});
	
			
			queryStore.load({
					params : {
						spId : parent.winSpecial,
						version : parent.winVersion
					}
				})

})