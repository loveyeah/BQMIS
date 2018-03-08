Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {

	
	// 定义grid
	var MyRecord = Ext.data.Record.create([{
				// 竣工验收ID
				name : 'checkId'
			}, {
				// 工程ID
				name : 'conId'
			}, {
				// 工程名称
				name : 'conName'
			}, {
				// 工程编号
				name : 'reportCode'
			}, {
				// 工程造价
				name : 'projectPrice'
			}, {
				// 施工单位ID
				name : 'contractorId'
			}, {
				// 施工单位名称
				name : 'contractorName'
			}, {
				// 工程地点
				name : 'proLocation'
			}, {
				// 开始日期
				name : 'startDate'
			}, {
				// 结束日期
				name : 'endDate'
			}, {
				// 验收日期
				name : 'checkDate'
			}, {
				// 工程量
				name : 'quantities'
			}, {
				// 填写人
				name : 'entryBy'
			}, {
				// 填写时间
				name : 'entryDate'
			}, {
				// 工程简要内容
				name : 'projectContent'
			}, {
				// 竣工情况说明
				name : 'endInfo'
			}, {
				// 验收结论意见
				name : 'checkText'

			}, {
				// 质量评级
				name : 'qualityAssess'
			}, {
				// 建设单位负责人
				name : 'constructChargeBy'

			}, {
				// 施工单位负责人
				name : 'contractorChargeBy'
			}, {
				// 回录人
				name : 'backEntryBy'
			}, {
				// 回录时间
				name : 'backEntryDate'
			},{
				 // 是否已补录
				name : 'isBackEntry'
			}, {
				// 企业编码
				name : 'enterpriseCode'
			}, {
				//是否使用
				name : 'isUse'
			}, {
				//prjid
				name : 'prjId'
			}]);
	var dataProxy = new Ext.data.HttpProxy(

	{
				url : 'manageproject/findPrjEndCheckList.action'
			});
	var theReader = new Ext.data.JsonReader({
				root : "list",
				totalProperty : "totalCount"
			}, MyRecord);
	var store = new Ext.data.Store({
				proxy : dataProxy,
				reader : theReader
			});
			
//store.load();
	// 分页
	var fuzzy = new Ext.form.TextField({
				id : "fuzzy",
				name : "fuzzy"
			});

	var sm = new Ext.grid.CheckboxSelectionModel({
			// singleSelect : true
			});

	var grid = new Ext.grid.GridPanel({
		region : "center",
		store : store,
		viewConfig : {
			forceFit : false
		},
		loadMask : {
			msg : '<img src="comm/images/extanim32.gif" width="32" height="32" style="margin-right: 8px;" align="absmiddle" />读取数据中 ...'
		},
		columns : [sm,new Ext.grid.RowNumberer(),{
					header : "立项名称",
					width : 100,
					sortable : true,
					dataIndex : 'prjId',
					renderer:function(v){
						if(v!=''&&v!=null){
							var s = v.split(',');
							return s[1];
						}else{
							return v;
						}
						
					}
				},{
					header : "竣工验收ID",
					width : 100,
					sortable : true,
					dataIndex : 'checkId',
					hidden : true
				},{
					header : "工程ID",
					width : 100,
					sortable : true,
					dataIndex : 'conId',
					hidden : true
				},{
					header : "合同名称",
					width : 200,
					sortable : true,
					dataIndex : 'conName'
				},{
					header : "施工单位ID",
					width : 80,
					sortable : true,
					dataIndex : 'contractorId',
					hidden: true
				},{
					header : "乙方单位",
					width : 200,
					sortable : true,
					dataIndex : 'contractorName'
				},{
					header : "工程编号",
					width : 200,
					sortable : true,
					dataIndex : 'reportCode',
					hidden: true
				},{
					header : "预算费用",
					width : 80,
					sortable : true,
					dataIndex : 'projectPrice'
				},{
					header : "开工时间",
					width : 150,
					sortable : true,
					dataIndex : 'startDate',
					//add by ypan 20100913
					renderer : function(v) {
				if (v != null) {
					return v.substring(0, 10);
				} else {
					return v;
				}
			}
				},{
					header : "竣工时间",
					width : 150,
					sortable : true,
					dataIndex : 'endDate',
					//add by ypan 20100913
					renderer : function(v) {
				if (v != null) {
					return v.substring(0, 10);
				} else {
					return v;
				}
			}
				},{
					header : "工程简要内容",
					width : 220,
					sortable : true,
					dataIndex : 'projectContent'
				},{
					header : "竣工情况说明",
					width : 200,
					sortable : true,
					dataIndex : 'endInfo',
					hidden : true
				},{
					header : "验收结论意见",
					width : 200,
					sortable : true,
					dataIndex : 'checkText',
					hidden : true
				},{
					header : "质量评级",
					width : 200,
					sortable : true,
					dataIndex : 'qualityAssess',
					hidden : true
				},{
					header : "建设单位负责人",
					width : 200,
					sortable : true,
					dataIndex : 'constructChargeBy',
					hidden : true
				},{
					header : "施工单位负责人",
					width : 200,
					sortable : true,
					dataIndex : 'contractorChargeBy',
					hidden : true
				},{
					header : "回录人",
					width : 200,
					sortable : true,
					dataIndex : 'backEntryBy',
					hidden : true
				}
				],
		sm : sm,
		tbar : ['合同名称',{
					id : 'temp.conName',
					name : 'temp.conName',
					xtype : 'textfield'
				}, '-', '乙方单位', {
					id : 'temp.contractorName',
					name : 'temp.contractorName',
					xtype : 'textfield'
				}, {
					text : "查询",
					iconCls : 'query',
					handler : queryRecord
				},  '-', {
					text : "会签表查看",
					iconCls : 'print',
					handler:function(){
								if (grid.getSelectionModel().hasSelection()) {
													var rec = grid.getSelectionModel().getSelections();
													var record = grid.getSelectionModel().getSelected();
													if(rec.length>1){
														Ext.Msg.alert('警告','请选择一条记录查看！')
													}else{
														var url = "/powerrpt/report/webfile/projectEndCheck.jsp?checkId="+record.data.checkId+"&backEntryBy="+record.data.backEntryBy;
														window.open(url);
													}
													
												} else {
													Ext.Msg.alert("提示信息", "请选择一条记录！");
												}	
					}
				}
		],
		// 分页
		bbar : new Ext.PagingToolbar({
					pageSize : 18,
					store : store,
					displayInfo : true,
					displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
					emptyMsg : "没有记录"
				})
	});
	
	function queryRecord() {
		store.baseParams = {
							conName :  Ext.get("temp.conName").dom.value,
							contractorName : Ext.get("temp.contractorName").dom.value,
							flag : 'query'
	}
		store.load({
					params : {
						start : 0,
						limit : 18
					}
				});
	}

	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				items : [grid]
			});

	queryRecord();


});