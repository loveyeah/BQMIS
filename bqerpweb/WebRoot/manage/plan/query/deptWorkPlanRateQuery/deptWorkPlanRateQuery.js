Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	// 取当前时间
	function getDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "";
		// t = d.getDate();
		// s += (t > 9 ? "" : "0") + t + " ";
		// t = d.getHours();
		// s += (t > 9 ? "" : "0") + t + ":";
		// t = d.getMinutes();
		// s += (t > 9 ? "" : "0") + t
		// + ":";
		// t = d.getSeconds();
		// s += (t > 9 ? "" : "0") + t;
		return s;
	}
	// 计划时间
	var planTime = new Ext.form.TextField({

				name : 'planTime',
				fieldLabel : '计划时间',
				listeners : {
					focus : function() {
						WdatePicker({
							startDate : '',
							alwaysUseStartDate : true,
							dateFmt : "yyyy-MM"
								// ,
								// onpicked : function() {
								// year.clearInvalid();
								//
								// },
								// onclearing : function() {
								// year.markInvalid();
								//
								// }
							});
					}
				}

			});
	planTime.setValue(getDate());
	function query() {
		store.load({
					params : {

						planTime : planTime.getValue()
						// ,
						// start : 0,
						// limit : 18

					}
				});

	}

	// 增加
	function addRecord() {
		var unfinishedtotal = 0;
		var partfinishedtotal = 0;
		var finishPlantotal = 0;
		var count = store.getCount();

		var currentIndex = count;
		for (var i = 0; i < count; i++) {
			unfinishedtotal += store.getAt(i).data.unfinished;
			partfinishedtotal += store.getAt(i).data.partfinished;
			finishPlantotal += store.getAt(i).data.finishPlan;
		}
		var o = new MyRecord({

			'editDepName' : '共计',
			'unfinished' : unfinishedtotal,
			'partfinished' : partfinishedtotal,
			'finishPlan' : finishPlantotal,
			'totalPlan' : unfinishedtotal + partfinishedtotal + finishPlantotal,
			'finishRate' : (unfinishedtotal + partfinishedtotal + finishPlantotal) == 0
					? 0
					: finishPlantotal
							/ (unfinishedtotal + partfinishedtotal + finishPlantotal)

		});

		grid.stopEditing();
		store.insert(currentIndex, o);

	}
	var query = new Ext.Button({
				id : 'btnQuery',
				text : '查询',
				iconCls : 'query',
				handler : query

			})

	// var myform = new Ext.form.FormPanel({
	// bodyStyle : "padding: 20,10,20,20",
	// layout : 'column',
	// items : [{
	// columnWidth : '.25',
	// layout : 'form',
	// border : false,
	// labelWidth : 60,
	// items : [planTime]
	// },
	//
	// {
	// columnWidth : '.1',
	// layout : 'form',
	// border : false,
	// labelWidth : 60,
	// items : [query]
	// }
	//
	// ]
	//
	// })

	var MyRecord = Ext.data.Record.create([{
				name : 'editDepName'
			}, {
				name : 'unfinished'
			}, {
				name : 'partfinished'
			}, {
				name : 'finishPlan'
			}, {
				name : 'totalPlan'
			}, {
				name : 'finishRate'
			}]);

	var dataProxy = new Ext.data.HttpProxy({
				url : 'manageplan/getBpJPlanJobDepDetailStat.action '
			});

	var theReader = new Ext.data.JsonReader({
				root : "list",
				totalProperty : "totalCount"

			}, MyRecord);

	var store = new Ext.data.Store({

				proxy : dataProxy,

				reader : theReader

			});
	store.on('load', addRecord);
	var sm = new Ext.grid.CheckboxSelectionModel();

	number = new Ext.grid.RowNumberer({
				header : "",
				align : 'left'
			})

	// 定义grid

	var grid = new Ext.grid.GridPanel({
		// region : "west",
		store : store,
		layout : 'fit',
		autoScroll : true,
		// width:'0.5',
		columns : [

				sm, // 选择框
				number, {

					header : "部门",

					sortable : false,
					dataIndex : 'editDepName'

				}, {

					header : "未完成",

					sortable : false,
					dataIndex : 'unfinished'

				}, {

					header : "部分完成",

					sortable : false,
					dataIndex : 'partfinished'

				}, {
					width : 300,
					header : "全部完成",

					sortable : false,
					dataIndex : 'finishPlan'

				}, {

					header : "共计",
					dataIndex : 'totalPlan',
					sortable : false,
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {

						var subSum = record.data.unfinished
								+ record.data.partfinished
								+ record.data.finishPlan;
						return subSum;

					},
					dataIndex : 'totalPlan'
				}, {
					width : 300,
					header : "完成率",

					sortable : false,
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						var subSum = 0;
						if ((record.data.partfinished + record.data.finishPlan) != 0) {
							subSum = record.data.finishPlan
									/ (record.data.unfinished
											+ record.data.partfinished + record.data.finishPlan);
						}
						return subSum;

					},
					dataIndex : 'finishRate'
				}],
		tbar : ["计划时间:", planTime, '-', query],
		sm : sm
			// , // 选择框的选择 Shorthand for
			// // selModel（selectModel）
			// // 分页
			// bbar : new Ext.PagingToolbar({
			// pageSize : 18,
			// store : store,
			// displayInfo : true,
			// displayMsg : "显示第{0}条到{1}条，共{2}条",
			// emptyMsg : "没有记录",
			// beforePageText : '',
			// afterPageText : ""
			// })
	});
	/** 右边的grid * */

	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				border : false,
				frame : false,
				items : [
						// {
						// bodyStyle : "padding: 20,10,20,20",
						// layout : 'fit',
						// border : false,
						// frame : false,
						// region : "north",
						// height : 100,
						// items : [myform]
						// },
						{
					// bodyStyle : "padding: 20,20,20,0",
					region : "center",
					border : false,
					frame : false,
					layout : 'fit',
					height : '50%',
					// width : '50%',
					items : [grid]
				}]
			});

})
