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
				mapping : '11'
			}, {
				name : 'effectTime',
				mapping : '14'
			}, {
				name : 'effectFlg',//modify by wphzu 
				mapping : '15'
			},{
				name : 'certificateStartTime',//add by wphzu 
				mapping : '16'
			},{
				name : 'certificateEndTime',//add by wphzu 
				mapping : '17'
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

	var sm = new Ext.grid.CheckboxSelectionModel();
	var trainStart = new Date();
	trainStart = trainStart.format('Y-01-01');
	// 证书有效开始时间
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

	// 证书有效结束时间
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

	// 姓名
	var personName = new Ext.form.TextField({
				width : 130,
				name : 'personName',
				fieldLabel : '姓名'
			});

	// 工作部门
	var deptName = new Ext.form.TextField({
				width : 130,
				name : 'deptName',
				fieldLabel : '工作部门'
			});
	// 证书类别

	var rbgCertificateSort = new trainMaint.certificate({}, false, {
				hiddenName : 'certificateSort',
				width : 130,
				fieldLabel : '证书类别'
			});

	listStore.load({
				params : {
					certificateStartTime : trainStartdate.getValue(),
					certificateEndTime : trainEnddate.getValue(),
					start : 0,
					limit : 18

				}
			})
	listStore.on('beforeload', function() {
				Ext.apply(this.baseParams, {
							certificateStartTime : trainStartdate.getValue(),
							certificateEndTime : trainEnddate.getValue(),
							personName : personName.getValue(),
							deptName : deptName.getValue(),
							certificateType : Ext.get("certificateSort").dom.value
						});
			});

	function query() {
		if (trainStartdate.getValue() == null
				|| trainStartdate.getValue() == "") {
			Ext.Msg.alert('提示', '请选择证书有效开始时间！')
		}
		if (trainEnddate.getValue() == null || trainEnddate.getValue() == "") {
			Ext.Msg.alert('提示', '请选择证书有效结束时间！')
		} else {
			listStore.load({
						params : {
							// trainStartdate : trainStartdate.getValue(),
							// trainEnddate : trainEnddate.getValue(),
							// personName : personName.getValue(),
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
						}, {
							header : "证书有效时间",
							width : 250,
							allowBlank : false,
							sortable : true,
							dataIndex : 'effectTime'
						}, {
							header : "当前状态",
							width : 100,
							allowBlank : false,
							sortable : true,
							dataIndex : 'effectFlg',
							renderer : function(v, cellmeta, record) {
								if (v == "1")
									return "有效";
								else if(v=="0")
								{
									 if(record.get("certificateStartTime")==""||record.get("certificateStartTime")==null)
									 {
									 	if(record.get("certificateEndTime")==""||record.get("certificateEndTime")==null)
									 	{
									 		
									 		return "有效";
									 	}
									 }
									return "无效";
								}
							   
							}
							
						}],
				sm : sm,
				autoSizeColumns : true,
				viewConfig : {
					forceFit : true
				},
				tbar : ["证书有效期:", trainStartdate, "至", trainEnddate, '-', "姓名",
						personName, '-', "部门", deptName, '-', "类别",
						rbgCertificateSort.combo, '-', {
							text : "查询",
							iconCls : 'query',
							minWidth : 70,
							handler : query
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
	var view = new Ext.Viewport({
				layout : 'fit',
				items : [{
							region : "center",
							layout : 'fit',
							title : '培训外出登记列表',
							border : false,
							split : true,
							margins : '0 0 0 0',
							items : [grid]
						}]
			});

});
