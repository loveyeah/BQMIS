Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var storeChargeBySystem = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'workticket/getDetailEquList.action'
						}),
				reader : new Ext.data.JsonReader({
							root : 'list'
						}, [{
									name : 'blockCode',
									mapping : 'blockCode'
								}, {
									name : 'blockName',
									mapping : 'blockName'
								}])
			});
	storeChargeBySystem.load();
	/* 上面的grid */
	var info_reclist = Ext.data.Record.create([{
				name : 'blockInfoId'
			}, {
				name : 'blockCode'
			}, {
				name : 'mgCode'
			}, {
				name : 'capacity'
			}, {
				name : 'fuelName'
			}, {
				name : 'businessServiceDate'
			}, {
				name : 'statBeginDate'
			}, {
				name : 'boilerName'
			}, {
				name : 'steamerMachine'
			}, {
				name : 'generationName'
			}, {
				name : 'primaryTransformer'
			}, {
				name : 'mgType'
			}]);
	var info_store = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'productionrec/getBlockInfoList.action'

						}),
				reader : new Ext.data.JsonReader({
							root : 'list',
							totalProperty : 'totalCount'
						}, info_reclist)

			});
	
	var info_cm = new Ext.grid.ColumnModel([ {
				header : '机组',
				dataIndex : 'blockCode',
				align : 'center',
				renderer : function(v) {
					var recIndex = storeChargeBySystem.find("blockCode", v);
					return storeChargeBySystem.getAt(recIndex).get("blockName");
				}
				
			}, {
				header : '局、厂机组编码',
				dataIndex : 'mgCode',
				align : 'center'
			}, {
				header : '铭牌容量(MW)',
				dataIndex : 'capacity',
				align : 'center'
			}, {
				header : '燃料名称',
				dataIndex : 'fuelName',
				align : 'center'
			}, {
				header : '商业运行时间',
				dataIndex : 'businessServiceDate',
				align : 'center',
				renderer : function(v){
					return v.substring(0,10)+" "+v.substring(11,19) 
			}
			}, {
				header : '开始统计时间',
				dataIndex : 'statBeginDate',
				align : 'center',
				renderer : function(v){
					return v.substring(0,10)+" "+v.substring(11,19) 
			}
			}, {
				header : '锅炉/核岛/燃气燃烧设备',
				dataIndex : 'boilerName',
				align : 'center'
			}, {
				header : '汽轮机/水轮机/燃气轮机',
				dataIndex : 'steamerMachine',
				align : 'center'
			}, {
				header : '发电机/电动机',
				dataIndex : 'generationName',
				align : 'center'
			}, {
				header : '主变压器',
				dataIndex : 'primaryTransformer',
				align : 'center'
			}, {
				header : '机组类型',
				dataIndex : 'mgType',
				align : 'center'
			}]);

	var info_grid = new Ext.grid.GridPanel({
				ds : info_store,
				title : '机组注册内容表',
				height : 300,
				viewConfig : {
			                 forceFit : true
		           },
				cm : info_cm

			})
	/* 中间的grid */
	var con_item = Ext.data.Record.create([{
				name : 'sjlrId'
			}, {
				name : 'blockCode'
			}, {
				name : 'jzztId'
			}, {
				name : 'startDate'
			}, {
				name : 'endDate'
			}, {
				name : 'keepTime'
			}, {
				name : 'reduceExert'
			}, {
				name : 'stopTimes'
			}, {
				name : 'successTimes'
			}, {
				name : 'failureTimes'
			}, {
				name : 'repairMandays'
			}, {
				name : 'repairCost'
			}, {
				name : 'stopReason'
			}]);

	var con_ds = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'productionrec/getBlockDependList.action'
						}),
				reader : new Ext.data.JsonReader({
							totalCount : "totalCount",
							root : "list"
						}, con_item)
			});
	
	var con_item_cm = new Ext.grid.ColumnModel([{
				header : '机组',
				dataIndex : 'blockCode',
				align : 'center',
				renderer : function(v) {
					var recIndex = storeChargeBySystem.find("blockCode", v);
					return storeChargeBySystem.getAt(recIndex).get("blockName");
				}
			}, {
				header : '事件开始时间',
				dataIndex : 'startDate',
				align : 'center',
				renderer : function(v) {
					return v.substring(0, 10) + " " + v.substring(11, 19)
				}
			}, {
				header : '事件结束时间',
				dataIndex : 'endDate',
				align : 'center',
				renderer : function(v) {
					return v.substring(0, 10) + " " + v.substring(11, 19)
				}
			}, {
				header : '事件状态',
				dataIndex : 'jzztId',
				align : 'center'
			}, {
				header : '事件持续时间',
				dataIndex : 'keepTime',

				align : 'center'
			}, {
				header : '降低出力',
				dataIndex : 'reduceExert',
				align : 'center'
			}, {
				header : '停运次数',
				dataIndex : 'stopTimes',
				align : 'center'
			}, {
				header : '启动成功次数',
				dataIndex : 'successTimes',

				align : 'center'
			}, {
				header : '启动失败次数',
				dataIndex : 'failureTimes',

				align : 'center'
			}, {
				header : '检修工日',
				dataIndex : 'repairMandays',
				align : 'center'
			}, {
				header : '检修费用',
				dataIndex : 'repairCost',
				align : 'center'
			}, {
				header : '事件原因',
				dataIndex : 'stopReason',
				align : 'center'
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

	var Grid = new Ext.grid.GridPanel({

				ds : con_ds,
				cm : con_item_cm,
				viewConfig : {
			                 forceFit : true
		           },
				height : 300,
				title : '机组月度事件报表',
				// autoScroll : true,

				// bbar : gridbbar,
				// tbar : contbar,
				border : false
			});
	/* 以上为中间grid */

	/* 下面的grid */

	var stat_reclist = Ext.data.Record.create([{
				name : 'kkxybId'
			}, {
				name : 'month'
			}, {
				name : 'blockCode'
			}, {
				name : 'fdl'
			}, {
				name : 'uth'
			}, {
				name : 'ph'
			}, {
				name : 'undh'
			}, {
				name : 'sh'
			}, {
				name : 'rh'
			}, {
				name : 'pot'
			}, {
				name : 'poh'
			}, {
				name : 'uot'
			}, {
				name : 'uoh'
			}, {
				name : 'fot'
			}, {
				name : 'foh'
			}, {
				name : 'for1'
			}, {
				name : 'eaf'
			}, {
				name : 'exr'
			}, {
				name : 'pof'
			}, {
				name : 'uof'
			}, {
				name : 'fof'
			}, {
				name : 'af'
			}, {
				name : 'sf'
			}, {
				name : 'udf'
			}, {
				name : 'utf'
			}, {
				name : 'uor'
			}, {
				name : 'foor'
			}, {}, {
				name : 'mttpo'
			}, {
				name : 'cah'
			}, {
				name : 'mttuo'
			}, {
				name : 'eundh'
			}])

	var stat_Store = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : ''
						}),
				reader : new Ext.data.JsonReader({
							root : 'list'
						}, stat_reclist)
			})

	var stat_cm1 = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(), {
				header : '机组',
				dataIndex : 'blockCode'
			}, {
				header : '发电',
				dataIndex : 'fdl'
			}, {
				header : '利用小时UTH',
				dataIndex : 'uth'
			}, {
				header : '统计期间小时PH',
				dataIndex : 'ph'
			}, {
				header : '降出力小时UNDH',
				dataIndex : 'undh'
			}, {
				header : '运行小时SH',
				dataIndex : 'sh'
			}, {
				header : '备用小时RH',
				dataIndex : 'rh'
			}, {
				header : '计划停运次数POT',
				dataIndex : 'pot'
			}, {
				header : '计划停运小时POH',
				dataIndex : 'poh'
			}, {
				header : '非计划停运次数UOT',
				dataIndex : 'uot'
			}

	]);
	var stat_cm2 = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(), {
				header : '非计划停运小时UOH',
				dataIndex : 'uoh'
			}, {
				header : '强迫停运次数FOT',
				dataIndex : 'fot'
			}, {
				header : '强迫停运小时FOH',
				dataIndex : 'foh'
			}, {
				header : '强迫停运率FOR1',
				dataIndex : 'for1'
			}, {
				header : '等效可用系数EAF',
				dataIndex : 'erf'
			}, {
				header : '暴露率EXR',
				dataIndex : 'exr'
			}, {
				header : '计划停运系数POF',
				dataIndex : 'pof'
			}, {
				header : '非计划停运系数UOF',
				dataIndex : 'uof'
			}, {
				header : '强迫停运系数FOF',
				dataIndex : 'fof'
			}

	]);
	var stat_cm3 = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(), {
				header : '可用系数AF',
				dataIndex : 'af'
			}, {
				header : '运行系数SF',
				dataIndex : 'sf'
			}, {
				header : '降低出力系数UDF',
				dataIndex : 'udf'
			}, {
				header : '利用系数UTF',
				dataIndex : 'utf'
			}, {
				header : '非计划停运率UOR',
				dataIndex : 'uor'
			}, {
				header : '强迫停运发生率FOOR',
				dataIndex : 'foor'
			}, {
				header : '平均计划停运间隔时间MTTPO',
				dataIndex : 'mttpo'
			}, {
				header : '平均非计划停运间隔时间MTTUO',
				dataIndex : 'mttuo'
			}, {
				header : '平均连续可用小时CAH',
				dataIndex : 'cah'
			}

	]);

	var stat_Grid1 = new Ext.grid.GridPanel({

				ds : stat_Store,
				height : 300,
				title : "机组主要指标表",
				viewConfig : {
			                 forceFit : true
		           },
				cm : stat_cm1
			})
	var stat_Grid2 = new Ext.grid.GridPanel({

				height : 300,
				ds : stat_Store,
				title : "",
				viewConfig : {
			                 forceFit : true
		           },
				cm : stat_cm2
			})
	var stat_Grid3 = new Ext.grid.GridPanel({

				height : 300,
				ds : stat_Store,
				title : "",
				viewConfig : {
			                 forceFit : true
		           },
				cm : stat_cm3
			})

	/* 以上为下面的grid */

	/* 查询控件 */

	// 系统当前时间
	function getDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t;

		return s;
	}
	var queryField = new Ext.form.TextField({
				id : 'argFuzzy',
				fieldLabel : "月份",

				// labelStyle :
				// "label-align:right;text-align:right;width:40;",

				name : 'argFuzzy',
				width : 100,
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
	var queryBtn = new Ext.Button({
				text : '查询',
				handler : fuzzyQuery
			})
	var computeBtn = new Ext.Button({
				text : '计算',
				handler : compute
			})

	function fuzzyQuery() {
		info_store.load({
					params : {
						start : 0,
						limit : 18
					}
				});
		con_ds.load({
					params : {
						start : 0,
						limit : 18
					}
				});

	};

	function compute() {
		stat_Store.load({
					params : {
						start : 0,
						limit : 18
					}
				});
	}
	fuzzyQuery();
	var queryForm = new Ext.FormPanel({
				autoWidth : true,
				labelAlign : 'right',
				labelWidth : 40,

				autoHeight : true,
				layout : 'column',
				items : [{

							columnWidth : .2,
							border : false, //

							layout : 'form',
							items : [queryField]
						}, {

							border : false,
							columnWidth : .05,

							items : [queryBtn]
						}, {

							border : false,

							columnWidth : .03
						}, {

							border : false,
							columnWidth : .05,
							items : [computeBtn]
						}]
			});

	var panel = new Ext.Panel({
		width : Ext.getBody().getViewSize().width,
		autoHeight : true,
		border : false,
		// autoScroll : true,
		// layout : 'border',
		split : true,

		items : [queryForm, info_grid, Grid, stat_Grid1, stat_Grid2, stat_Grid3]

	})

	panel.render("treepanel");

});