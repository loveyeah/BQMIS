Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {

			// 工作流序号
			var workFlowNo;
			// 工作流状态
			var signStatus;
			// 计划时间(部门工作计划汇总表主键)
			// var planTime;

			function getDate() {
				var d, s, t;
				d = new Date();
				s = d.getFullYear().toString(10) + "-";
				t = d.getMonth() + 1;
				s += (t > 9 ? "" : "0") + t
				return s;
			}
			function init() {
				Ext.lib.Ajax.request('POST',
						'manageplan/getBpJPlanJobMain.action', {
							success : function(action) {
								var result = eval("(" + action.responseText
										+ ")");
								if (result != null) {
									workFlowNo = result.workFlowNo;
									signStatus = result.signStatus;
									if (signStatus == '2') {
										Ext.getCmp('btnapprove')
												.setDisabled(true)
										con_ds.removeAll();
										Ext.Msg.alert("提示", "已经审批!");
										return;
									} else {
										Ext.getCmp('btnapprove')
												.setDisabled(false)
									}
									con_ds.baseParams = {
										planTime : planTime.getValue(),
										approve : 'Y'
									}
									con_ds.load({
												params : {
													start : 0,
													limit : 18
												}
											})
								} else {
									Ext.Msg.alert("提示", "未生成数据!");
									con_ds.removeAll();
									return;
								}
							}
						}, 'planTime=' + planTime.getValue());
			}

			/** 左边的grid * */
			var con_item = Ext.data.Record.create([{
						name : 'planTime',
						mapping : 0
					}, {
						name : 'editByName',
						mapping : 1
					}, {
						name : 'editDate',
						mapping : 2
					}, {
						name : 'depMainId',
						mapping : 3
					}, {
						name : 'depName',
						mapping : 4
					}, {
						name : 'jobId',
						mapping : 5
					}, {
						name : 'jobContent',
						mapping : 6
					}, {
						name : 'completeDate',
						mapping : 7
					}]);

			var con_sm = new Ext.grid.CheckboxSelectionModel({
						singleSelect : false

					});
			var con_ds = new Ext.data.Store({
						proxy : new Ext.data.HttpProxy({
									url : 'manageplan/getBpPlanDeptModList.action'
								}),
						reader : new Ext.data.JsonReader({
									totalProperty : "totalCount",
									root : "list"
								}, con_item)
					});

			var con_item_cm = new Ext.grid.ColumnModel([con_sm,

			{
						header : '部门',

						dataIndex : 'depName',
						align : 'center'

					}, {
						header : '编辑人员',
						dataIndex : 'editByName',
						align : 'center'
					}, {
						header : '编辑时间',
						dataIndex : 'editDate',
						align : 'center'

					}, {
						header : '工作内容',
						dataIndex : 'jobContent',
						align : 'center'

					}, {
						header : '完成时间',
						dataIndex : 'completeDate',
						align : 'center',
						renderer : function(v) {
							if (v == '0') {
								return '当月完成';
							} else if (v == '1') {
								return '跨越完成';
							} else if (v == '2') {
								return '长期';
							} else
								return '';
						}

					}]);
			con_item_cm.defaultSortable = true;

			var gridbbar = new Ext.PagingToolbar({
						pageSize : 18,
						store : con_ds,
						displayInfo : true,
						displayMsg : "显示第{0}条到{1}条，共{2}条",
						emptyMsg : "没有记录",
						beforePageText : '',
						afterPageText : ""
					});
			// 计划时间
			var planTime = new Ext.form.TextField({
						name : 'planTime',
						fieldLabel : '计划时间',
						listeners : {
							focus : function() {
								WdatePicker({
											startDate : '',
											alwaysUseStartDate : true,
											dateFmt : "yyyy-MM",
											isShowClear : false
										});
								this.blur();
							}
						}

					});
			planTime.setValue(getDate());

			function queryFun() {
				init();

			}
			var query = new Ext.Button({
						text : '查询',
						id : 'butQuery',
						iconCls : 'query',
						handler : queryFun

					})
			var contbar = new Ext.Toolbar({
						items : [planTime, query, {
									text : "审批",
									id : 'btnapprove',
									iconCls : 'approve',
									handler : approve
								}]

					});
			var Grid = new Ext.grid.GridPanel({
						sm : con_sm,
						ds : con_ds,
						cm : con_item_cm,
						height : 425,
						autoScroll : true,
						bbar : gridbbar,
						tbar : contbar,
						border : true
					});

			// 签字
			function approve() {
				if (Grid.store.getCount() == 0) {
					Ext.Msg.alert('提示', '无数据进行审批！');
					return;
				}
				var url = "../SignWorkPlan/approveDeptWorkPlan.jsp";
				var args = new Object();
				args.prjNo = planTime.getValue();
				args.entryId = workFlowNo;
				args.workflowType = "bqDeptJobplangather";
				args.prjTypeId = "";
				args.prjStatus = signStatus;

				var obj = window.showModalDialog(url, args,
						'status:no;dialogWidth=750px;dialogHeight=550px');
				if (obj) {
					workFlowNo = obj.workFlowNo;
					signStatus = obj.signStatus;
					queryFun();
				}

			}

			new Ext.Viewport({
						enableTabScroll : true,
						layout : "border",
						border : false,
						frame : false,
						items : [{
									layout : 'fit',
									border : false,
									frame : false,
									region : "center",
									items : [Grid]
								}]
					});

			queryFun();
		})
