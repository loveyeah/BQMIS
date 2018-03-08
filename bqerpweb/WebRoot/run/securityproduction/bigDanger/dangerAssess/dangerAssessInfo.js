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
	

	
	var bview;
		var btnView = new Ext.Button({
				id : 'btnView',
				text : '查看',
				handler : function() {
					window.open(bview);
				}
			});
	
	
  //名称关键字
	var fuzzy = new Ext.form.TextField({
		name : 'name',
		xtype : 'textfield'
	});
	
	//类别下拉框
	
	var sort = new Ext.form.ComboBox({
		store : [['', '全部'],['0', '评估标准'], ['1', '评估报告']],
		value : '',
		//id : 'smartDate',
		name : 'sort',
		valueField : "value",
		displayField : "text",
		fieldLabel : "类别",
		//hideLabel : true,
		mode : 'local',
		readOnly : true,
		anchor : '90%',
		typeAhead : true,
		forceSelection : true,
		//hiddenName : 'model.smartDate',
		editable : false,
		triggerAction : 'all',
		disabled : false,
		//width : 65,
		selectOnFocus : true
	});
	
	// 查询按钮
	var westbtnref = new Ext.Button({
				text : '查询',
				iconCls : 'query',
				handler : function() {
					queryRecord();
				}
			});

	var westsm = new Ext.grid.CheckboxSelectionModel();
	// 左边列表中的数据
	
			
				var datalist = new Ext.data.Record.create([

	{			id:'assessId',
				name : 'assessId'
			}, {
				name : 'fileName'
			},{
				name:'assessSort'
			},{
				name : 'annex'
			}, {
				name : 'entryBy'
			}, {
				name : 'entryDate'
			}, {
				name : 'isUse'
			}, {
				name : 'enterpriseCode'
			}]);

	var westgrids = new Ext.data.JsonStore({
				url : 'security/findDangerList.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : datalist
			});

	// 左边列表
	var westgrid = new Ext.grid.GridPanel({
				ds : westgrids,
				columns : [westsm, new Ext.grid.RowNumberer(), {
							header : "名称",
							width : 75,
							//align : "center",
							sortable : true,
							dataIndex : 'fileName'
						}, {
							header : "类别",
							width : 75,
							//align : "center",
							sortable : false,
							dataIndex : 'assessSort',
							renderer:function(v){
								if(v=='0')
								return "评估标准";
								if(v=='1')
								return "评估报告";
							} 
						}, {
							header : "查看",
							width : 75,
							sortable : true,
							dataIndex : 'annex',
							renderer:function(v){
								if(v !=null && v !='')
								{ 
									var s =  '<a href="#" onclick="window.open(\''+v+'\');return  false;">[查看]</a>';
									return s;
								}
							} 
						},{
							header : "上传人",
							width : 75,
							sortable : true,
							dataIndex : 'entryBy'
							
			
		                  },{
							header : "上传日期",
							width : 75,
							sortable : true,
							dataIndex : 'entryDate',
								renderer:function(v){
								if(v !=null && v !='')
								{ 
									return (v.replace('T',' ').substring(0,10));
								}
							}
		                  }],
				viewConfig : {
			                 forceFit : true
		           },
				tbar : ['名称：',fuzzy,'类别：',sort,westbtnref, {
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
				name:fuzzy.getValue(),
				sort:sort.getValue(),
			    reportType : reType,
				timeType : tiType
		}
		westgrids.load({
			params : {
				name:fuzzy.getValue(),
				sort:sort.getValue(),
				start : 0,
				limit : 18				
			}
		});
	}
	
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
			queryRecord();
});