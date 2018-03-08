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
			}
			,{
				name : 'model.checkResult'
			}
			]);

	var westgrids = new Ext.data.JsonStore({
				url : 'productionrec/findAccountByMonth.action',
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
		s += (t > 9 ? "" : "0") + t;

		return s;
	}
	var query = new Ext.form.TextField({
				id : 'argFuzzy',
				fieldLabel : "按月份查询",
				hideLabel : false,
				emptyText : '按月份查询..',
				name : 'argFuzzy',
				width : 150,
				value : getDate(),
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '',
									alwaysUseStartDate : true,
									dateFmt : "yyyy-MM"

								});
						this.blur();
					}

				}
			});
	function fuzzyQuery() {
		westgrids.baseParams = {
			fuzzy : query.getValue(),
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
	// 左边列表
	var westgrid = new Ext.grid.GridPanel({
				ds : westgrids,
				columns : [new Ext.grid.RowNumberer(), {
							header : "仪器仪表名称",
							sortable : true,
							dataIndex : 'model.names'
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
							header : "量程",
							sortable : false,
							dataIndex : 'model.userRange'
						}, {
							header : "型号",
							sortable : false,
							dataIndex : 'model.sizes'
						}, {
							header : "生产厂商",
							sortable : false,
							dataIndex : 'model.factory'
						}, {
							header : "出厂日期",
							sortable : false,
							dataIndex : 'model.outFactoryDate',
							renderer : rendererFun
						}, {
							header : "出厂编号",
							sortable : false,
							dataIndex : 'model.outFactoryNo'
						}, {
							header : "购买日期",
							sortable : false,
							dataIndex : 'model.buyDate',
							renderer : rendererFun
						}, {
							header : "投用日期",
							sortable : false,
							dataIndex : 'model.useDate',
							renderer : rendererFun
						}, {
							header : "负责人",
							sortable : false,
							dataIndex : 'chargerName'
						}, {
							header : "部门",
							sortable : false,
							dataIndex : 'depName'
						}, {
							header : "校验周期(月)",
							sortable : false,
							dataIndex : 'model.testCycle'
						}, {
							header : "校验机构(部门)",
							sortable : false,
							dataIndex : 'model.checkDeptCode'
						}, {
							header : "上次检验时间",
							sortable : false,
							dataIndex : 'model.lastCheckDate',
							renderer : rendererFun
						}, {
							header : "使用状态",
							sortable : false,
							dataIndex : 'model.ifUsed',
							renderer : function(v){
								if(v == 'Y'){
									return '是';
								}else if(v == 'N'){
									return '否';
								}else 
									return;
							}
						}, {
							header : "计划检验时间",
							sortable : false,
							dataIndex : 'model.nextCheckDate',
							renderer : rendererFun
						}, {
							header : "备注",
							sortable : false,
							dataIndex : 'model.memo'
						}],
				tbar : ['计划检验时间(月份)：', query, querybtn],

				viewConfig : {
					forceFit : false
				},
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
	function   rendererFun(value)
	{
		if (value != null && value != "") {
			var myYear = value.substring(0, 4);
			var myMonth = value.substring(5, 7);
			var myDay = value.substring(8, 10);
			return myYear + '-' + myMonth + '-' + myDay;
		} else {
			return ""
		}
	}
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