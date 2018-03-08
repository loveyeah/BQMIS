Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
			// 系统当前时间
			function getDate() {
				var d, s, t;
				d = new Date();
				s = d.getFullYear().toString(10) + "-";
				t = d.getMonth() + 1;
				s += (t > 9 ? "" : "0") + t;
				return s;
			}
			// 计划时间
			var planTime = new Ext.form.TextField({
						name : 'planTime',
						fieldLabel : '计划时间',
						allowBlank : false,
						listeners : {
							focus : function() {
								WdatePicker({
											startDate : '',
											alwaysUseStartDate : true,
											dateFmt : "yyyy-MM"
										});
							}
						}
					});
			planTime.setValue(getDate());
			// 计划主题
			var url = "manageplan/getPlanTopic.action";
			var conn = Ext.lib.Ajax.getConnectionObject().conn;
			conn.open("POST", url, false);
			conn.send(null);
			var search_data4 = eval('(' + conn.responseText + ')');
			var planTopic = new Ext.form.ComboBox({
						id : "planTopic",
						name : "planTopic",
						hiddenName : "topicCode",
						fieldLabel : "计划主题",
						triggerAction : 'all',
						store : new Ext.data.SimpleStore({
									fields : ["name", "id"],
									data : search_data4
								}),
						displayField : "name",
						valueField : "id",
						emptyText : '请选择...',
						mode : 'local',
						readOnly : true,
						allowBlank : false,
						anchor : "90%"
					});

			function query() {
				if (planTime.getValue() == null || planTime.getValue() == "") {
					Ext.Msg.alert('提示', '请选择计划时间！')
				} else {
					if (planTopic.getValue() == null
							|| planTopic.getValue() == "") {
						Ext.Msg.alert('提示', '请选择计划主题！')
					} else {
						store.load({
									params : {
										planTime : planTime.getValue(),
										topicCode : Ext.get("topicCode").dom.value,
										
										start : 0,
										limit : 18
									}
								})
					}
				}

			}
			// 查询按钮
			var tbar = new Ext.Toolbar({
						items : ["计划时间：", planTime, '-', "计划主题：", planTopic,
								'-', {
									id : 'btnQuery',
									text : "查询",
									iconCls : 'query',
									handler : function() {
										query();
									}
								}]
					});
	var MyRecord = Ext.data.Record.create([{
				name : 'topicItemId'
			}, {
				name : 'reportId'
			}, {
				name : 'itemCode'
			}, {
				name : 'planValue'
			}, {
				name : 'lastValue'
			}, {
				name : 'itemName'
			}, {
				name : 'unitName'
			}]);


			var dataProxy = new Ext.data.HttpProxy({
						url : 'manageplan/queryBpJPlanTopicItemList.action '
					});
			var theReader = new Ext.data.JsonReader({
						root : "list",
						totalProperty : "totalCount"
					}, MyRecord);
			var store = new Ext.data.Store({
						proxy : dataProxy,
						reader : theReader
					});
			var sm = new Ext.grid.CheckboxSelectionModel();

			var number = new Ext.grid.RowNumberer({
						header : "",
						align : 'left'
					})
			// 定义grid
			var grid = new Ext.grid.GridPanel({
						store : store,
						height : 590,
						columns : [sm, number, {
									header : '指标编码',
									sortable : false,
									dataIndex : 'itemCode'
								}, {
									header : '指标名称',
									sortable : false,
									dataIndex : 'itemName'
								}, {
									header : '计量单位',
									sortable : false,
									dataIndex : 'unitName'
								}, {
									header : '计划值',
									sortable : false,
									dataIndex : 'planValue'
								}, {
									header : '上年同期',
									sortable : false,
									dataIndex : 'lastValue'
								}],
						sm : sm,
						tbar : tbar
					});

			/** 右边的grid * */
			new Ext.Viewport({
						enableTabScroll : true,
						layout : "border",
						border : false,
						frame : false,
						items : [{
									region : "center",
									border : false,
									frame : false,
									items : [grid]
								}]
					});
		})
