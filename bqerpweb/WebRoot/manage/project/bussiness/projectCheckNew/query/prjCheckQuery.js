Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {

			var projectName = new Ext.form.TextField({
						id : 'projectName',
						fieldLabel : '项目名称',
						name : 'projectName',
						width : 100
					})
					
			// 查询按钮
			var queryButton = new Ext.Button({
						id : 'query',
						text : '查询',
						iconCls : 'query',
						handler : function() {
							repStore.baseParams = {
							status : statusValue.getValue(),
							projectName:projectName.getValue()
						}
							repStore.load({
										params : {
											start : 0,
											limit : 18
										}
									});
						}

					})
					
					
					//状态类型
			var statusValue = new Ext.form.ComboBox({
					store : [['', '全部'], ['0', '未上报'], ['1', '已上报'], ['2', '已验收评价'], ['3', '已打印验收单'], ['4', '已签字']],
					value : '',
					//id : 'smartDate',
					name : 'status',
					width:210,
					valueField : "value",
					displayField : "text",
					fieldLabel : "状态选择：",
					mode : 'local',
					readOnly : true,
					anchor : '140%',
					typeAhead : true,
					forceSelection : true,
					editable : false,
					triggerAction : 'all',
					disabled : false,
					selectOnFocus : true
				});
			//详细查看（会签表）
			var detailButton = new Ext.Button({
						text : '详细查看（会签表）',
						iconCls : 'detail',
						handler : function() {
								if (repGrid.getSelectionModel().hasSelection()) {
									var rec = repGrid.getSelectionModel().getSelections();
									var record = repGrid.getSelectionModel().getSelected();
									if (rec.length > 1) {
										Ext.Msg.alert('警告', '请选择一条记录查看！')
									} else {
										var url = "/powerrpt/report/webfile/prjCheck.jsp?checkId="
												+ record.data.checkId+'&backEntryBy='+record.data.backEntryBy;
										window.open(url);
									}
					
								} else {
									Ext.Msg.alert("提示信息", "请选择一条记录！");
								}
						}

					})

		
		

			var datalist = new Ext.data.Record.create([{
						// 开工报告书id
						name : 'checkId',
						mapping : 0
					}, {
						// 工程合同编号
						name : 'contractName',
						mapping : 1
					}, {
						// 项目名称
						name : 'conId',
						mapping : 2
					}, {
						// 编号
						name : 'reportCode',
						mapping : 3
					}, {
						// 开工日期
						name : 'startDate',
						mapping : 4
					}, {
						// 竣工日期
						name : 'endDate',
						mapping : 5
					}, {
						// 承包单位名称
						name : 'contractorName',
						mapping : 6
					}, {
						// 承包单位ID
						name : 'contractorId',
						mapping : 7
					}, {
						// 承包负责人
						name : 'chargeBy',
						mapping : 8
					}, {
						// 发包部门负责人
						name : 'deptChargeBy',
						mapping : 9
					}, {
						// 发包部门工程方负责人验收评价
						name : 'checkAppraise',
						mapping : 10
					}, {
						// 填写人
						name : 'entryBy',
						mapping : 11
					}, {
						// 填写时间
						name : 'entryDate',
						mapping : 12
					}, {
						// 回录人
						name : 'backEntryBy',
						mapping : 13
					}, {
						// 状态
						name : 'status',
						mapping : 15
					}, {
						// 立项id
						name : 'prjId',
						mapping : 16
					}, {
						// 立项名称
						name : 'prjName',
						mapping : 17
					}])

			var repStore = new Ext.data.JsonStore({
						url : 'manageproject/queryNoReportCheck.action',
						root : 'list',
						totalProperty : 'totalCount',
						fields : datalist
					});
			var sm = new Ext.grid.CheckboxSelectionModel();
			var prjSEDate = '';
			var repGrid = new Ext.grid.GridPanel({
						sm : sm,
						ds : repStore,
						columns : [sm, new Ext.grid.RowNumberer(), {
									header : "立项名称",
									width : 75,
									align : "center",
									sortable : true,
									dataIndex : 'prjName'
								},{
									header : "项目名称",
									width : 75,
									align : "center",
									sortable : true,
									dataIndex : 'contractName'
								}, {
									header : "工程开始时间",
									width : 75,
									align : "center",
									hidden:true,
									sortable : true,
									dataIndex : 'startDate',
									renderer : function(v) {
										if (v != null) {
											prjSEDate='';
											prjSEDate+=v.substring(0, 10)+'--';
											return v.substring(0, 10);
											
										} else {
											return v;
										}
									}
								}, {
									header : "工程结束时间",
									width : 75,
									align : "center",
									hidden:true,
									sortable : true,
									dataIndex : 'endDate',
									renderer : function(v) {
										if (v != null) {
											prjSEDate+=v.substring(0, 10);
											return v.substring(0, 10);
											
										} else {
											return v;
										}
									}
								}, {
									header : "工程起始时间",
									width : 75,
									align : "center",
									sortable : true,
									dataIndex : 'seDate',
									renderer : function(v) {
										return prjSEDate;
									}
								},{
									header : "承包单位名称",
									width : 75,
									align : "center",
									sortable : true,
									dataIndex : 'contractorName'
								}, {
									header : "承包方负责人",
									width : 75,
									align : "center",
									sortable : true,
									dataIndex : 'chargeBy'
								}, {
									header : "状态",
									width : 75,
									align : "center",
									sortable : true,
									dataIndex : 'status',
									renderer:function(v){
										if(v=='0'){
											return '未上报';
										}else if(v=='1'){
											return '已上报';
										}else if(v=='2'){
											return '已验收评价';
										}else if(v=='3'){
											return '已打印验收单';
										}else if(v=='4'){
											return '已签字';
										}
										
									}
								}],
						viewConfig : {
							forceFit : true
						},
						frame : true,
						bbar : new Ext.PagingToolbar({
									pageSize : 18,
									store : repStore,
									displayInfo : true,
									displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
									emptyMsg : "没有记录"
								}),
						tbar : [// '开工时间：',start_date,'竣工时间：',end_date,'工程类别：',prj_type,
						'项目名称:',projectName,'状态：',statusValue,queryButton,'-',detailButton
								],
						border : true,
						enableColumnHide : false,
						enableColumnMove : false,
						iconCls : 'icon-grid'
					});
			function queryRecord() {
				repStore.baseParams = {
					status : statusValue.getValue(),
					flag : 'query'
				// start_date:start_date.getValue(),
				// end_date:end_date.getValue(),
				// prj_type:prj_type.getValue()
				}
				repStore.load({
							params : {
								start : 0,
								limit : 18
							}
						});
			}

			queryRecord();

			// 设定布局器及面板
			var layout = new Ext.Viewport({
						layout : "border",
						border : false,
						items : [{
									region : 'center',
									layout : 'fit',
									border : false,
									margins : '1 0 1 1',
									collapsible : true,
									items : [repGrid]

								}]
					});

		});