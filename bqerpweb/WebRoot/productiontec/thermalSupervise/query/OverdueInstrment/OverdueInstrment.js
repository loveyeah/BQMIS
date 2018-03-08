Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {

	// 左边列表中的数据
	var datalist = new Ext.data.Record.create([

	{
				name : 'model.regulatorId'
			}, {
				name : 'model.names'
			}, {
				name : 'model.yqyblbId'
			}, {
				name : 'model.yqybdjId'
			}, {
				name : 'model.yqybjdId'
			}, {
				name : 'model.userRange'
			}, {
				name : 'model.sizes'
			}, {
				name : 'model.testCycle'
			}, {
				name : 'model.factory'
			}, {
				name : 'model.outFactoryDate'
			}, {
				name : 'model.outFactoryNo'
			}, {
				name : 'model.buyDate'
			}, {
				name : 'model.useDate'
			}, {
				name : 'model.depCode'
			}, {
				name : 'model.charger'
			}, {
				name : 'model.ifUsed'
			}, {
				name : 'model.memo'
			}, {
				name : 'model.lastCheckDate'
			}, {
				name : 'model.nextCheckDate'
			}, {
				name : 'model.jdzyId'
			}, {
				name : 'depName'

			}, {
				name : 'chargerName'
			}, {
				name : 'nameOfSort'
			}, {
				name : 'yqybjdName'
			}, {
				name : 'yqybdjName'
			}
			// add by liuyi 20100512 
			,{
				name : 'model.grade'
			},{
				name : 'model.precision'
			},{
				name : 'model.checkDeptCode'
			},{
				name : 'model.mainParam'
			},{
				name : 'model.checkResult'
			}
			]);

	var westgrids = new Ext.data.JsonStore({
				url : 'productionrec/findOverdueTestRecord.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : datalist
			});
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
	var query = new Ext.form.TextField({
				id : 'argFuzzy',
				fieldLabel : "按月份查询",
				hideLabel : false,
				emptyText : '仪器名称..',
				name : 'argFuzzy',
				width : 150
			});
	function fuzzyQuery() {
		westgrids.baseParams = {
			date : getDate(),
					names : query.getValue(),
					jdzyId : JDZY_ID
		}
		westgrids.load({
					params : {
					start : 0,
					limit : 18
				}
				});
	};

	// 查询时Enter
	document.onkeydown = function() {
		if (event.keyCode == 13 && document.getElementById('argFuzzy')) {
			fuzzyQuery();

		}
	}
	var querybtn = new Ext.Button({
				iconCls : 'query',
				text : '查询',
				handler : function() {
					fuzzyQuery();
				}
			})
	//westgrids.load({
	//			params : {
	//				start : 0,
	//				limit : 18,
	//				date : getDate(),
	//				names : query.getValue(),
	//				jdzyId : JDZY_ID
	//			}
	//		});

	// 左边列表
	var westgrid = new Ext.grid.GridPanel({
				ds : westgrids,
				columns : [new Ext.grid.RowNumberer(), {
							header : "仪器仪表名称",

							// align : "center",
							sortable : true,

							dataIndex : 'model.names'
						}, {
							header : "规格型号",

							sortable : false,

							dataIndex : 'model.sizes'
						}, {
							header : "类别",

							sortable : false,

							dataIndex : 'nameOfSort'
						}, {
							header : "等级",

							sortable : false,

							dataIndex : 'model.grade'
						}, {
							header : "精度",

							sortable : false,

							dataIndex : 'model.precision'
						}, {
							header : "检验周期",

							sortable : false,

							dataIndex : 'model.testCycle'
						}, {
							header : "上次检验时间",

							sortable : false,

							dataIndex : 'model.lastCheckDate',
							renderer : function(value) {

								if (value != null && value != "") {

									var myYear = value.substring(0, 4);
									var myMonth = value.substring(5, 7);

									var myDay = value.substring(8, 10);
									return myYear + '-' + myMonth + '-' + myDay;
								} else {
									return ""
								}
							}
						}, {
							header : "计划检验时间",

							sortable : false,

							dataIndex : 'model.nextCheckDate',
							renderer : function(value) {

								if (value != null && value != "") {

									var myYear = value.substring(0, 4);
									var myMonth = value.substring(5, 7);

									var myDay = value.substring(8, 10);
									return myYear + '-' + myMonth + '-' + myDay;
								} else {
									return ""
								}
							}
						}],

				viewConfig : {
					forceFit : true
				},
				frame : true,
				tbar:[query,querybtn],
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
	fuzzyQuery();
	// 设定布局器及面板
	var layout = new Ext.Viewport({
				layout : "border",
				border : false,
				items : [{
							title : "",
							region : 'center',
							layout : 'fit',
							border : false,
							margins : '1 0 1 1',
							split : true,
							collapsible : true,
							items : [westgrid]

						}]
			});
});