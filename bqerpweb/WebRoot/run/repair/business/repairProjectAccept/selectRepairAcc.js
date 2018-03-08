Ext.onReady(function() {
			var arg = window.dialogArguments
			var workflowStatus = arg.workflowStatus;
			function getYear() {
				var d, s, t;
				d = new Date();
				s = d.getFullYear().toString(10);
				return s;
			}
			function getTime() {
				var d, s, t;
				d = new Date();
				s = d.getFullYear().toString(10) + "-";
				t = d.getMonth() + 1;
				s += (t > 9 ? "" : "0") + t + "-";
				t = d.getDate();
				s += (t > 9 ? "" : "0") + t + " ";
				return s;
			}

			var year = new Ext.form.TextField({
						style : 'cursor:pointer',
						name : 'year',
						fieldLabel : '年度',
						readOnly : true,
						width : 80,
						value : getYear(),
						listeners : {
							focus : function() {
								WdatePicker({
											startDate : '%y',
											alwaysUseStartDate : false,
											dateFmt : 'yyyy',
											isShowClear : false,
											onpicked : function(v) {
												store.reload();
												this.blur();
											}
										});
							}
						}
					});

			// 检修类别
			var storeRepairType = new Ext.data.Store({
						proxy : new Ext.data.HttpProxy({
									url : 'manageplan/getRepairType.action'
								}),
						reader : new Ext.data.JsonReader({
									root : 'list'
								}, [{
											name : 'repairTypeId',
											mapping : 0
										}, {
											name : 'repairTypeName',
											mapping : 1
										}])
					});

			var cbxRepairType = new Ext.form.ComboBox({
						id : 'cbxRepairType',
						fieldLabel : "检修类别",
						store : storeRepairType,
						displayField : "repairTypeName",
						valueField : "repairTypeId",
						hiddenName : 'main.repairTypeId',
						mode : 'remote',
						triggerAction : 'all',
						value : '',
						readOnly : true,
						width : 90
					})
			storeRepairType.on('load', function(e, records) {
						var myrecord = new Ext.data.Record({
									repairTypeName : '所有',
									repairTypeId : ''
								});
						storeRepairType.insert(0, myrecord);
						cbxRepairType.setValue('');
					})
			// 版本
			var edition = new Ext.form.TextField({
						id : '',
						name : '',
						width : 80
					})
			var sm = new Ext.grid.CheckboxSelectionModel({
						singleSelect : true
					});
			// grid列表数据源

			var Record = new Ext.data.Record.create([sm, {

						name : 'specialityName',
						mapping : 0
					}, {
						name : 'repairProjectName',
						mapping : 1
					}, {
						name : 'workingCharge',
						mapping : 2
					}, {
						name : 'workingChargeName',
						mapping : 3
					}, {
						name : 'workingMenbers',
						mapping : 4
					}, {
						name : 'startDate',
						mapping : 5
					}, {
						name : 'endDate',
						mapping : 6
					}, {
						name : 'acceptanceLevel',
						mapping : 7
					}, {
						name : 'specialityId',
						mapping : 8
					}, {
						name : 'projectMainYear',
						mapping : 9
					}, {
						name : 'repairTypeId',
						mapping : 10
					}, {
						name : 'repairTypeName',
						mapping : 11
					}, {
						name : 'version',
						mapping : 12
					}]);

			var gridTbar = new Ext.Toolbar({
						items : ['年度', year, '-', '检修类别', cbxRepairType, '-',
								'版本', edition, {
									id : 'querybtn',
									text : "查询",
									iconCls : 'query',
									tabIndex : 1,
									handler : queryRepairTask
								}, {
									id : 'confirmBtu',
									text : '确定',
									iconCls : 'confirm',
									handler : confirmFun
								}]
					});

			var store = new Ext.data.JsonStore({
						url : 'manageplan/getRepairItemList.action',
						root : 'list',
						totalProperty : 'totalCount',
						fields : Record
					});
			// 分页工具栏
			var pagebar = new Ext.PagingToolbar({
						pageSize : 18,
						store : store,
						displayInfo : true,
						displayMsg : "一共 {2} 条",
						emptyMsg : "没有记录"
					});
			// 页面的Grid主体
			var repairTaskgrid = new Ext.grid.EditorGridPanel({
						store : store,
						layout : 'fit',
						columns : [sm, new Ext.grid.RowNumberer({
											header : '行号',
											width : 50
										}),// 选择框
								{
									header : '验收级别',
									dataIndex : 'acceptanceLevel',
									align : 'left',
									hidden : true,
									width : 80
								}, {
									header : '专业',
									dataIndex : 'specialityName',
									align : 'left',
									width : 80
								}, {
									header : '专业',
									dataIndex : 'specialityId',
									align : 'left',
									hidden : true,
									width : 80
								}, {
									header : '项目名称',
									dataIndex : 'repairProjectName',
									align : 'left',
									width : 80
								}, {
									header : '工作负责人ID',
									dataIndex : 'workingCharge',
									align : 'left',
									hidden : true,
									width : 80
								}, {
									header : '工作负责人',
									dataIndex : 'workingChargeName',
									align : 'left',
									width : 80
								}, {
									header : '工作成员',
									dataIndex : 'workingMenbers',
									align : 'left',
									width : 80
								}, {
									header : '计划开始时间',
									dataIndex : 'startDate',
									align : 'left',
									width : 140
								}, {
									header : '计划结束时间',
									dataIndex : 'endDate',
									align : 'left',
									width : 140
								}, {
									header : '年度',
									dataIndex : 'projectMainYear',
									align : 'left',
									width : 80
								}, {
									header : '检修类别',
									dataIndex : 'repairTypeName',
									align : 'left',
									width : 150
								}, {
									header : '版本',
									dataIndex : 'version',
									align : 'left',
									width : 100
								}],

						sm : sm, // 选择框的选择
						tbar : gridTbar,
						bbar : pagebar,
						clicksToEdit : 1,
						viewConfig : {
							forceFit : true
						}
					});
			repairTaskgrid.on("dblclick", confirmFun);
			store.on("beforeload", function() {
						Ext.apply(this.baseParams, {
									year : year.getValue(),
									cbxRepairType : cbxRepairType.getValue(),
									edition : edition.getValue(),
									workflowStatus : workflowStatus
								});
					});
			store.load({
						params : {
							start : 0,
							limit : 18
						}
					});

			function queryRepairTask() {
				store.reload();
			}

			function confirmFun() {
				if (repairTaskgrid.selModel.hasSelection()) {
					var records = repairTaskgrid.selModel.getSelections();
					var recordslen = records.length;
					if (recordslen > 1) {
						Ext.Msg.alert("系统提示信息", "请选择其中一项！");
					} else {
						var record = repairTaskgrid.getSelectionModel()
								.getSelected();
						var repair = new Object();
						repair.specialityName = record.get("specialityName");
						repair.repairProjectName = record
								.get("repairProjectName");
						repair.workingCharge = record.get("workingCharge");
						repair.workingChargeName = record
								.get("workingChargeName");
						repair.workingMenbers = record.get("workingMenbers");
						repair.startDate = record.get("startDate");
						repair.endDate = record.get("endDate");
						repair.acceptanceLevel = record.get("acceptanceLevel");
						repair.specialityId = record.get("specialityId");
						window.returnValue = repair;
						window.close();
					}
				} else {
					Ext.Msg.alert("提示", "请先选择行!");
				}
			}

			new Ext.Viewport({
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
									items : [repairTaskgrid]
								}]
					});

		})