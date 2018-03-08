Ext.onReady(function() {
			// 系统当前时间
			function getMonth() {
				var d, s, t;
				d = new Date();
				s = d.getFullYear().toString(10) + "-";
				t = d.getMonth() + 1;
				s += (t > 9 ? "" : "0") + t;
				return s;
			}
			var guideCode;
			var Record = Ext.data.Record.create([{
						name : 'guideNew.guideId'
					}, {
						name : 'editDate'
					}, {
						name : 'guideNew.guideContent'
					}, {
						name : 'guideNew.referDepcode'
					}, {
						name : 'guideNew.mainDepcode'
					}, {
						name : 'guideNew.otherDepcode'
					}, {
						name : 'guideNew.editBy'
					}, {
						name : 'mainDepName'
					}, {
						name : 'referDepName'
					}, {
						name : 'editByName'
					}, {
						name : 'guideNew.ifComplete'
					}, {
						name : 'guideNew.completeDesc'
					},{
						name : 'guideCode'
					}]);

			var memoText = new Ext.form.TextArea({
						id : "memoText",
						maxLength : 128,
						readOnly : true,
						width : 180
					});
			var win = new Ext.Window({
						height : 170,
						width : 350,
						layout : 'fit',
						modal : true,
						resizable : false,
						closeAction : 'hide',
						items : [memoText],
						buttonAlign : "center",
						title : '临时计划内容',
						buttons : [{
									text : '返回',
									iconCls : 'cancer',
									handler : function() {
										win.hide();
									}
								}]
					});
			var sm = new Ext.grid.RowSelectionModel({
						singleSelect : false
					});

			var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(), {
						header : "",
						dataIndex : "",
						hidden : true
					}, {
						header : '顺序号',
						dataIndex : 'guideCode',
						width : 50
					}, {
						header : "计划内容",
						dataIndex : "guideNew.guideContent",
						width : 150
					}, {
						header : "提出单位",
						dataIndex : "referDepName",
						width : 90
					}, {
						header : "主要落实单位",
						dataIndex : "mainDepName",
						width : 90
					}, {
						header : "其他落实单位",
						dataIndex : "guideNew.otherDepcode",
						width : 90
					}, {
						header : "编制人",
						dataIndex : "editByName",
						width : 50
					}, {
						header : "完成情况",
						dataIndex : "guideNew.ifComplete",
						width : 60,
						renderer : function(v) {
							if (v == 0) {
								return "未完成"
							}
							if (v == 1) {
								return "进行中"
							}
							if (v == 2) {
								return "已完成"
							}
						}
					}, {
						header : "完成说明",
						dataIndex : "guideNew.completeDesc",
						width : 150
					}]);
			var ds = new Ext.data.Store({
						proxy : new Ext.data.HttpProxy({
									url : 'manageplan/getPlanNewsList.action'
								}),
						reader : new Ext.data.JsonReader({
								// totalProperty : "totalProperty",
								// root : "root"
								}, Record)
					});
			//
			var fuzzy = new Ext.form.TextField({
						style : 'cursor:pointer',
						id : "fuzzy",
						name : 'fuzzy',
						readOnly : true,
						anchor : "80%",
						value : getMonth(),
						listeners : {
							focus : function() {
								WdatePicker({
											startDate : '%y-%M',
											alwaysUseStartDate : false,
											dateFmt : 'yyyy-MM',
											onpicked : function() {
											},
											onclearing : function() {
												planStartDate.markInvalid();
											}
										});
							}
						}
					});
			ds.load({
						params : {
							queryString : fuzzy.getValue()
						}
					});
			var stocktbar = new Ext.Toolbar({
						// height : 25,
						items : ["计划月份:", fuzzy, {
									text : "查询",
									iconCls : 'query',
									handler : function() {
										ds.load({
													params : {
														queryString : fuzzy
																.getValue()
													}
												});
									}
								},"(<font color='red'>双击查看内容</font>)"]
					});
			var grid = new Ext.grid.GridPanel({
						ds : ds,
						cm : cm,
						sm : sm,
						tbar : stocktbar,
						autoWidth : true,
						border : true,
						viewConfig : {
							forceFit : false
						}
					});
			grid.on("dblclick", function() {
						var record = grid.getSelectionModel().getSelected();
						var value = record.get('guideNew.guideContent');
						memoText.setValue(value);
						win.x = undefined;
						win.y = undefined;
						win.show();
					})
			var viewport = new Ext.Viewport({
						layout : 'border',
						autoWidth : true,
						autoHeight : true,
						fitToFrame : true,
						items : [{
									region : 'center',
									layout : 'fit',
									items : [grid]
								}]
					});
		})
