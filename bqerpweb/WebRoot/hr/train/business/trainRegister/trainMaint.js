Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	var MyRecord = Ext.data.Record.create([{
				name : 'trainId',
				mapping : 0
			}, {
				name : 'workCode',
				mapping : 1
			}, {
				name : 'workName',
				mapping : 2
			}, {
				name : 'trainStartdate',
				mapping : 3
			}, {
				name : 'trainEnddate',
				mapping : 4
			}, {
				name : 'trainSite',
				mapping : 5
			}, {
				name : 'trainContent',
				mapping : 6
			}, {
				name : 'trainCharacter',
				mapping : 7
			}, {
				name : 'certificateSort',
				mapping : 8
			}, {
				name : 'certificateNo',
				mapping : 9
			}, {
				name : 'trainTotalFee',
				mapping : 10
			}, {
				name : 'certificateName',
				mapping : 11
			},{
				name : 'trainInsititution',
				mapping : 12
			}, {
				name : 'isReceived',
				mapping : 13
			}]);

	var DataProxy = new Ext.data.HttpProxy({
				url : 'com/findOutTrainList.action'
			});

	var TheReader = new Ext.data.JsonReader({
				root : "list",
				totalProperty : "totalCount"
			}, MyRecord);

	var listStore = new Ext.data.Store({
				proxy : DataProxy,
				reader : TheReader
			});

	listStore.load()
	var sm = new Ext.grid.CheckboxSelectionModel();

	var trainStart = new Date();
	trainStart = trainStart.format('Y-01-01');
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
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd'
								});
					}
				}
			});

	var trainEnd = new Date();
	trainEnd = trainEnd.format('Y-m-d');

	// 培训结束时间
	var trainEnddate = new Ext.form.TextField({
				width : 100,
				style : 'cursor:pointer',
				allowBlank : true,
				value : trainEnd,
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

	function query() {
		if (trainStartdate.getValue() == null
				|| trainStartdate.getValue() == "") {
			Ext.Msg.alert('提示', '请选择培训开始时间！')
		}
		if (trainEnddate.getValue() == null || trainEnddate.getValue() == "") {
			Ext.Msg.alert('提示', '请选择培训结束时间！')
		} else {
			listStore.load({
						params : {
							trainStartdate : trainStartdate.getValue(),
							trainEnddate : trainEnddate.getValue(),
							start : 0,
							limit : 18
						}
					})
		}

		init();
	}
var flag="";
	var grid = new Ext.grid.GridPanel({
				region : "center",
				layout : 'fit',
				store : listStore,
				listeners : {
					'rowdblclick' : function() {
						var rec = grid.getSelectionModel().getSelected();
						tabs.setActiveTab(0);
						 flag=2;
						trainRegister.setTrainId(rec.get("trainId"),flag);
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
							dataIndex : 'trainId',
							hidden : true
						}, {
							header : "姓名",
							width : 150,
							allowBlank : false,
							sortable : true,
							dataIndex : 'workName'
						}, {
							header : "培训开始时间",
							width : 150,
							allowBlank : false,
							sortable : true,
							dataIndex : 'trainStartdate'
						}, {
							header : "培训结束时间",
							width : 150,
							allowBlank : false,
							sortable : true,
							dataIndex : 'trainEnddate'
						}, {
							header : "培训地点",
							width : 150,
							allowBlank : false,
							sortable : true,
							dataIndex : 'trainSite'
						}, {
							header : "培训内容",
							width : 150,
							allowBlank : false,
							sortable : true,
							dataIndex : 'trainContent'
						}, {
							header : "培训性质",
							width : 150,
							allowBlank : false,
							sortable : true,
							dataIndex : 'trainCharacter',
							renderer : function(v) {
								if (v == "1") {
									return "学历培训";
								}
								if (v == "2") {
									return "岗位提升";
								}
								if (v == "3") {
									return "取证培训";
								}
								if (v == "4") {
									return "复证培训";
								}
								if (v == "5") {
									return "技术竞赛";
								}
								if (v == "6") {
									return "其他";
								}
							}
						}, {
							header : "证书类别",
							width : 150,
							allowBlank : false,
							sortable : true,
							dataIndex : 'certificateName'
						}, {
							header : "证书编号",
							width : 150,
							allowBlank : false,
							sortable : true,
							dataIndex : 'certificateNo'
						}, {
							header : "培训费用",
							width : 150,
							allowBlank : false,
							sortable : true,
							dataIndex : 'trainTotalFee'
						}],
				sm : sm,
				autoSizeColumns : true,
				viewConfig : {
					forceFit : true
				},
				tbar : ["培训时间:", trainStartdate, "至", trainEnddate, '-', {
							text : "查询",
							iconCls : 'query',
							minWidth : 70,
							handler : query
						}, {
							text : "新建培训登记",
							iconCls : 'add',
							minWidth : 100,
							handler : function() {
								tabs.setActiveTab(0);
								trainRegister.setFeeDetailBtn(false);
								trainRegister.resetInputField();
							}
						},{
							text : "复制",
							iconCls : 'add',
							minWidth : 100,
							handler : function() {
								var rec = grid.getSelectionModel().getSelected();
								var selrows = grid.getSelectionModel().getSelections();
								if(selrows.length!=1)
								{
									Ext.Msg.alert('提示', '请选择一条记录！')
									return;
								
								}else
								{
									
						   
						       flag=1;
						      tabs.setActiveTab(0);
						     trainRegister.setTrainId(rec.get("trainId"),flag);
						     trainRegister.setFeeDetailBtn(true);
						     
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
	function init() {
		Ext.Ajax.request({
					method : 'post',
					url : 'com/findOutTrainList.action',
					params : {
						trainStartdate : trainStartdate.getValue(),
						trainEnddate : trainEnddate.getValue(),
						start : 0,
						limit : 18
					}
				});
	}
	init();

	var trainRegister = new trainMaint.trainRegister();
	var tabs = new Ext.TabPanel({
				activeTab : 0,
				tabPosition : 'bottom',
				plain : false,
				defaults : {
					autoScroll : true
				},
				listeners : {//add by wpzhu 20100702
			beforetabchange : function(tabs, newTab, currentTab) {
				if (newTab.id == 'trainRegister') {
					
				} else if (newTab.id == 'trainList') {
					query();
					
				}
			}
		},
				items : [{
							id : 'trainRegister',
							title : '培训登记',
							items : trainRegister.panel,
							autoScroll : true,
							layout : 'fit'
						}, {
							id : 'trainList',
							title : '培训列表',
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
