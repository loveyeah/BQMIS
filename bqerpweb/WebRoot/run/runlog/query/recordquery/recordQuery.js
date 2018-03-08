// 设置树的点击事件
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

			var specialcode = parent.document.getElementById("specialityCode").value;
			var fromdate = parent.document.getElementById("fromDate").value;
			var todate = parent.document.getElementById("toDate").value;
			var ShiftRec = new Ext.data.Record.create([{
						name : 'run_logno'
					}, {
						name : 'shift_record_id'
					}, {
						name : 'run_log_id'
					}, {
						name : 'main_item_code'
					}, {
						name : 'main_item_name'
					}, {
						name : 'record_content'
					}, {
						name : 'is_completion'
					}, {
						name : 'file_path'
					}, {
						name : 'record_by'
					}, {
						name : 'record_by_name'
					}, {
						name : 'record_time'
					}, {
						name : 'check_memo'
					}, {
						name : 'check_time'
					}, {
						name : 'is_use'
					}, {
						name : 'enterprise_code'
					}, {
						name : 'review_type'
					}, {
						name : 'review_no'
					}, {
						name : 'shift_name'
					}]);
			var ds = new Ext.data.GroupingStore({
						proxy : new Ext.data.HttpProxy({
									url : 'runlog/queryShiftRecord.action',
									method : 'post'
								}),
						reader : new Ext.data.JsonReader({
									totalProperty : 'total',
									root : 'root'
								}, ShiftRec),
						groupField : 'name',
						groupOnSort : true,
						sortInfo : {
							field : 'run_logno',
							direction : "DESC"
						}
					});

			ds.on('beforeload', function() {
						Ext.apply(this.baseParams, {
									specialcode : specialcode,
									fromDate : fromdate,
									toDate : todate
								});
					});
			ds.load({
						params : {
							start : 0,
							limit : 18
						}
					});
			var colModel = new Ext.grid.ColumnModel([
					new Ext.grid.RowNumberer(), {
						header : 'ID',
						sortable : true,
						dataIndex : 'shift_record_id',
						hidden : true
					}, {
						header : '值班日志ID',
						sortable : true,
						dataIndex : 'run_log_id',
						hidden : true
					}, {
						header : '运行日志号',
						dataIndex : 'run_logno'
					}, {
						header : '记录时间',
						dataIndex : 'record_time'
					}, {
						dataIndex : 'main_item_code',
						hidden : true
					}, {
						header : '记事类别',
						dataIndex : 'main_item_name'
					}, {
						header : '记录内容',
						dataIndex : 'record_content',
						width : 300,
						renderer : function change(val) {
							return ' <span style="white-space:normal;">' + val
									+ ' </span>';
						}
					}, {
						header : '记录人',
						dataIndex : 'record_by_name'
					}]);
			// 排序
			colModel.defaultSortable = true;
			var bbar = new Ext.PagingToolbar({
						pageSize : 18,
						store : ds,
						displayInfo : true,
						displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
						emptyMsg : "没有记录",
						beforePageText : '页',
						afterPageText : "共{0}"
					});
			var recGrid = new Ext.grid.GridPanel({
						ds : ds,
						cm : colModel,
						width : 200,
						bbar : bbar,
						border : false,
						view : new Ext.grid.GroupingView({
									forceFit : true,
									sortAscText : '正序',
									sortDescText : '倒序',
									columnsText : '列显示/隐藏',
									groupByText : '依本列分组',
									showGroupsText : '分组显示',
									groupTextTpl : '{[values.rs[0].data.run_logno.substr(0,4)+"年"+values.rs[0].data.run_logno.substr(4,2)+"月"+values.rs[0].data.run_logno.substr(6,2)+"日"+values.rs[0].data.shift_name]}' 
								})
					});

			var gridForm = new Ext.FormPanel({
						id : 'rec-form',
						frame : true,
						autoWidth : true,
						autoHeight : true,
						align : 'center',
						labelAlign : 'left',
						items : [{
									xtype : 'fieldset',
									labelAlign : 'right',
									title : '值班记事内容',
									autoHeight : true,
									border : true,
									defaults : {
										width : 200
									},
									items : [{
												xtype : 'textarea',
												fieldLabel : '记事内容',
												id : 'recordContentId',
												name : 'recordContent',
												allowBlank : false,
												readOnly : true,
												width : 320,
												height : 220
											}]
								}]
					});

			var win = new Ext.Window({
						width : 500,
						height : 280,
						closeAction : 'hide',
						modal : true,
						items : [gridForm]
					});

			recGrid.on('rowdblclick', function(grid, rowIndex, e) {
						win.show();
						var rec = recGrid.getSelectionModel().getSelected();
						document.getElementById("recordContentId").value = rec
								.get("record_content");
					});
			var viewport = new Ext.Viewport({
						layout : 'fit',
						items : [recGrid]
					});
			setTimeout(function() {
						Ext.get('loading').remove();
						Ext.get('loading-mask').fadeOut({
									remove : true
								});
					}, 250);
		});