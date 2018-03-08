Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
			// 将时间转成字符串格式
			function ChangeDateToString(DateIn) {
				var Year = 0;
				var Month = 0;
				var Day = 0;
				var CurrentDate = "";
				// 初始化时间
				Year = DateIn.getYear();
				Month = DateIn.getMonth() + 1;
				Day = DateIn.getDate();
				CurrentDate = Year + "-";
				if (Month >= 10) {
					CurrentDate = CurrentDate + Month + "-";
				} else {
					CurrentDate = CurrentDate + "0" + Month + "-";
				}
				if (Day >= 10) {
					CurrentDate = CurrentDate + Day;
				} else {
					CurrentDate = CurrentDate + "0" + Day;
				}
				return CurrentDate;
			}

			// 系统当前时间
			var enddate = new Date();
			// // 系统到现在前3个月
			var d = new Date();
			d.setMonth(d.getMonth() - 3);
			var sdate = ChangeDateToString(d);
			var edate = ChangeDateToString(enddate);

			var startdate = new Ext.form.TextField({
						style : 'cursor:pointer',
						id : "startdate",
						name : 'startdate',
						readOnly : true,
						anchor : "90%",
						// allowBlank : true,
						value : sdate,
						fieldLabel : '项目年份',
						listeners : {
							focus : function() {
								WdatePicker({
											startDate : '%y%M%d',
											alwaysUseStartDate : false,
											dateFmt : 'yyyy-MM-dd',
											onpicked : function() {
												startdate.clearInvalid();
											},
											onclearing : function() {
												startdate.markInvalid();
											}
										});
							}
						}
					});

			var enddate = new Ext.form.TextField({
						style : 'cursor:pointer',
						id : "enddate",
						name : 'enddate',
						readOnly : true,
						anchor : "90%",
						// allowBlank : true,
						value : edate,
						fieldLabel : '项目年份',
						listeners : {
							focus : function() {
								WdatePicker({
											startDate : '%y%M%d',
											alwaysUseStartDate : false,
											dateFmt : 'yyyy-MM-dd',
											onpicked : function() {
												enddate.clearInvalid();
											},
											onclearing : function() {
												enddate.markInvalid();
											}
										});
							}
						}
					});

			// 按钮
			var btnQuery = new Ext.Button({
						id : 'btnQuery',
						text : '查  询',
						iconCls : 'query',
						minWidth : 80,
						handler : function() {
							con_ds.reload({
										params : {
											startDate : startdate.getValue(),
											endDate : enddate.getValue(),
											start : 0,
											limit : 18
										}
									});
						}
					});

			// 删除按钮
			var btnDel = new Ext.Button({
						id : 'btnDel',
						iconCls : 'delete',
						text : '删  除',
						minWidth : 80,
						handler : function() {
							var selected = Grid.getSelectionModel()
									.getSelected();
							if (selected) {
								if (selected.get("prMain.status") == 1
										|| selected.get("prMain.status") == 2) {
									Ext.Msg.alert('提示', '该状态不可删除!');
									return false;
								}
								Ext.Ajax.request({
											waitMsg : '删除中,请稍后...',
											url : 'equplan/delPlanRepair.action',
											params : {
												repairId : selected
														.get("prMain.repairId")
											},
											success : function(result, request) {
												con_ds.load({
															params : {
																startDate : startdate
																		.getValue(),
																endDate : enddate
																		.getValue(),
																start : 0,
																limit : 18
															}
														});
											},
											failure : function() {
												Ext.Msg.alert('提示信息',
														'服务器错误,请稍候重试!')
											}
										});

							} else {
								Ext.Msg.alert('提示信息', '请选择要删除的项目记录!')
							}
						}
					});

			var btnnew = new Ext.Button({
						id : 'btnnew',
						iconCls : 'add',
						text : '新建',
						minWidth : 80,
						handler : function() {
							parent.myRecord = "";
							parent.repair = "";
							// var repairId = "";
							// parent.edit(repairId);
							parent.Ext.getCmp("maintab").setActiveTab(1);
							var _url2 = "equ/planrepair/planRegister/baseInfo.jsp";
							parent.document.all.iframe2.src = _url2;
						}
					});
			var con_item = Ext.data.Record.create([{
						name : 'prMain.repairId'
					}, {
						name : 'prMain.repairCode'
					}, {
						name : 'prMain.repairName'
					}, {
						name : 'prMain.planTypeId'
					}, {
						name : 'prMain.planStartTime'
					}, {
						name : 'prMain.planEndTime'
					}, {
						name : 'prMain.fare'
					}, {
						name : 'prMain.fareSoruce'
					}, {
						name : 'prMain.content'
					}, {
						name : 'prMain.remark'
					}, {
						name : 'prMain.fillBy'
					}, {
						name : 'prMain.fillDate'
					}, {
						name : 'prMain.status'
					}, {
						name : 'fareSoruceName'
					}, {
						name : 'fillByName'
					}, {
						name : 'fillDate'
					}, {
						name : 'planEndTime'
					}, {
						name : 'planStartTime'
					}, {
						name : 'planTypeName'
					}, {
						name : 'statusName'
					}]);
			// var con_sm = new Ext.grid.CheckboxSelectionModel({
			// singleSelect : true
			//
			// });
			var con_ds = new Ext.data.Store({
						proxy : new Ext.data.HttpProxy({
									url : 'equplan/getPlanRepairList.action'
								}),
						reader : new Ext.data.JsonReader({
									totalProperty : "totalCount",
									root : "list"
								}, con_item)
					});
			con_ds.load({
						params : {
							startDate : startdate.getValue(),
							endDate : enddate.getValue(),
							start : 0,
							limit : 18
						}
					})

			con_ds.on('beforeload', function() {
						Ext.apply(this.baseParams, {
									startDate : startdate.getValue(),
									endDate : enddate.getValue(),
									start : 0,
									limit : 18
								});
					});
			var con_item_cm = new Ext.grid.ColumnModel([
					new Ext.grid.RowNumberer({
								header : '序号',
								width : 50,
								align : 'center'
							}),

					{
						header : '计划检修名称',
						dataIndex : 'prMain.repairName',
						align : 'center'

					}, {
						header : '计划检修编号',
						dataIndex : 'prMain.repairCode',
						align : 'center'
					}, {
						header : '计划类别',
						dataIndex : 'planTypeName',
						align : 'center'
					}, {
						header : '计划开始时间',
						dataIndex : 'planEndTime',
						align : 'center'
						// renderer : function chargeIt(v) {}
				}	, {
						header : '状态',
						dataIndex : 'statusName',
						align : 'center'
					}]);
			con_item_cm.defaultSortable = true;

			var gridbbar = new Ext.PagingToolbar({
						pageSize : 18,
						store : con_ds,
						displayInfo : true,
						displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
						emptyMsg : "没有记录",
						beforePageText : '页',
						afterPageText : "共{0}"
					});
			var Grid = new Ext.grid.GridPanel({
						ds : con_ds,
						cm : con_item_cm,
						width : Ext.get('div_lay').getWidth(),
						split : true,
						autoScroll : true,
						bbar : gridbbar,
						tbar : ['登记时间', '-', startdate, '至', enddate, '-',
								btnQuery, '-', btnnew, '-', btnDel],
						border : false,
						viewConfig : {
							forceFit : true
						}
					});
			Grid.on('rowdblclick', modifyBtn);
			function modifyBtn() {
				var rec = Grid.getSelectionModel().getSelected();
				if (rec) {
					parent.myRecord = rec;
					var repairId = rec.get('prMain.repairId');
					parent.repair = repairId;
					parent.Ext.getCmp("maintab").setActiveTab(1);
					var _url2 = "equ/planrepair/planRegister/baseInfo.jsp";
					parent.document.all.iframe2.src = _url2;
				}
			}
			new Ext.Viewport({
						enableTabScroll : true,
						layout : "border",
						items : [{
									region : "center",
									layout : 'fit',
									border : false,
									split : true,
									margins : '0 0 0 0',
									items : [Grid]
								}]
					});

		});