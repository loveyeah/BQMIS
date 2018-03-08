Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	
	// 系统当前时间
	function getDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate();
		s += (t > 9 ? "" : "0") + t + " ";

		return s;
	}
	

	// 从session取登录人编码姓名部门相关信息
	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
					success : function(action) {
						var result = eval("(" + action.responseText + ")");
						if (result.workerCode) {
							// 设定默认工号，赋给全局变量
							fillByCode.setValue(result.workerCode);
							entryBy.setValue(result.workerCode);

						}
					}
				});
	}
	var bview;
		var btnView = new Ext.Button({
				id : 'btnView',
				text : '查看',
				handler : function() {
					window.open(bview);
				}
			});
	// 标题
	

	
	var dangerName = new Ext.form.TextField({
		id : "dangerName",
		inputType : "text",
		fieldLabel : '危险源名称',
		
		name : 'dangerName' 
	})


//状态类型
var statusValue = new Ext.form.ComboBox({
		store : [['0', '全部'], ['1', '已分发'], ['2', '已签字'], ['3', '已上报'], ['4', '已确定']],
		value : '0',
		//id : 'smartDate',
		name : 'status',
		valueField : "value",
		displayField : "text",
		fieldLabel : "状态选择：",
		mode : 'local',
		readOnly : true,
		anchor : '90%',
		typeAhead : true,
		forceSelection : true,
		editable : false,
		triggerAction : 'all',
		disabled : false,
		selectOnFocus : true
	});
	
  //年度关键字
	
	var year = new Ext.form.TextField({
				fieldLabel : "年度",
				readOnly : true,
				anchor : '90%',
				//name : 'model.year',
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : new Date().getFullYear()
											.toString(),
									alwaysUseStartDate : true,
									dateFmt : "yyyy"

								});
						this.blur();
					}
				},
				value : new Date().getFullYear()
			});
	
	




	
	// 查询按钮
	var westbtnref = new Ext.Button({
				text : '查询',
				iconCls : 'query',
				handler : function() {
					queryRecord();
				}
			});
	var sep = new Ext.Button({
		text:'-'
	});

	var westsm = new Ext.grid.CheckboxSelectionModel();
	// 左边列表中的数据
	
			
				var datalist = new Ext.data.Record.create([

	{			
				name : 'dangerId'
			}, {
				name : 'dangerYear'
			},{
				name:'dangerName'
			},{
				name : 'finishDate'
			}, {
				name : 'assessDept'
			}, {
				name : 'chargeBy'
			}, {
				name : 'memo'
			}, {
				name : 'workFlowNo'
			}, {
				name : 'status'
			}, {
				name : 'orderBy'
			}, {
				name : 'DValue'
			}, {
				name : 'd1Value'
			}, {
				id:'valueLevel',
				name : 'valueLevel'
			}, {
				name : 'annex'
			}]);

	var westgrids = new Ext.data.JsonStore({
				url : 'security/queryDangerReport.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : datalist
			});

	// 左边列表
	var westgrid = new Ext.grid.GridPanel({
				ds : westgrids,
				clicksToEdit:2,
				columns : [westsm, new Ext.grid.RowNumberer(), {
							header : "危险源名称",
							width : 75,
							//align : "center",
							sortable : true,
							dataIndex : 'dangerName'
						}, {
							header : "完成时间",
							width : 75,
							//align : "center",
							sortable : false,
							dataIndex : 'finishDate',
							renderer:function(v){
								return v.substring(0,10);
							}
							
						},{
							header : "D值",
							width : 75,
							sortable : true,
							dataIndex : 'DValue',
							editor : new Ext.form.TextField({
								allowBlank : true,
								id : 'DValue'
							})

			
		                  },{
							header : "D1值",
							width : 75,
							sortable : true,
							dataIndex : 'd1Value',
								editor : new Ext.form.TextField({
								allowBlank : true,
								id : 'd1Value'
							})
			
		                  },{
							header : "级别",
							width : 75,
							sortable : true,
							dataIndex : 'valueLevel',
							renderer:function(v){
								if(v=='1'){
									return '1级';					
								}if(v=='2'){
									return '2级';					
								}if(v=='3'){
									return '3级';					
								}if(v=='4'){
									return '4级';					
								}
							},
								editor : new Ext.form.ComboBox({
									store : [['1', '1级'],['2', '2级'], ['3', '3级'],['4', '4级']],
									value : '',
									name : 'valueLevel',
									valueField : "value",
									displayField : "text",
									fieldLabel : "级别",
									mode : 'local',
									readOnly : true,
									anchor : '90%',
									typeAhead : true,
									forceSelection : true,
									editable : false,
									triggerAction : 'all',
									disabled : false,
									selectOnFocus : true
								})
			
		                  },{
							header : "评估部门",
							width : 75,
							sortable : true,
							dataIndex : 'assessDept'
		                  },{
							header : "责任人",
							width : 75,
							sortable : true,
							dataIndex : 'chargeBy'
		                  },{
							header : "备注",
							width : 75,
							sortable : true,
							dataIndex : 'memo'
		                  },{
							header : "状态",
							width : 75,
							sortable : true,
							dataIndex : 'status',
							renderer:function(v){
								if(v=='1'){
									return '已分发';
								}else if(v=='2'){
									return '已签字';
								}else if(v=='3'){
									return '已上报';
								}else if(v=='4'){
									return '已确定';
								}
							
							}
		                  },{
							header : "附件",
							width : 75,
							sortable : true,
							dataIndex : 'annex',
							renderer:function(v){
								if(v !=null && v !='')
								{ 
									var s =  '<a href="#" onclick="window.open(\''+v+'\');return  false;">[查看]</a>';
									return s;
								}else{
									return '没有附件';
								}
							} 
						}],
				viewConfig : {
			                 forceFit : true
		           },
				tbar : ['年度：',year,'危险源名称：',dangerName,'状态:',statusValue,westbtnref, {
							xtype : "tbseparator"
						}],
				sm : westsm,
				frame : true,
				bbar : new Ext.PagingToolbar({
							pageSize : 18,
							store : westgrids,
							displayInfo : true,
							displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
							emptyMsg : "没有记录"
						}),
				border : true,
				enableColumnHide : false,
				enableColumnMove : false,
				iconCls : 'icon-grid'
			});

	// westgrid 的事件
	function queryRecord()
	{
		
		westgrids.baseParams = {
				dangerName:dangerName.getValue(),
				status:statusValue.getValue(),
				year:year.getValue(),
			    reportType : reType,
				timeType : tiType
		}
		westgrids.load({
			params : {
				dangerName:dangerName.getValue(),
				status:statusValue.getValue(),
				year:year.getValue(),
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
							items : [westgrid]

						}]
			});
			
});