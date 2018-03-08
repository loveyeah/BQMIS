// 设置树的点击事件
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
			var recordid = getParameter("recordid");

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
					}]);
			var ds = new Ext.data.Store({
						proxy : new Ext.data.HttpProxy({
									url : 'runlog/reviewNotCompletion.action',
									method : 'post'
								}),
						reader : new Ext.data.JsonReader({
									totalProperty : 'total',
									root : 'root'
								}, ShiftRec)
					});
			ds.load({
						params : {
							start : 0,
							limit : 10,
							recordid : recordid
						}
					});
			var colModel = new Ext.grid.ColumnModel([
					new Ext.grid.CheckboxSelectionModel(), {
						id : 'shift_record_id',
						header : 'ID',
						sortable : true,
						dataIndex : 'shift_record_id',
						hidden : true
					}, {
						id : 'runLogId',
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
					}, {
						header : '完成状态',
						dataIndex : 'is_completion',
						renderer : function(val) {
							if (val == 'Y') {
								return '已完成';
							}
							if (val == 'N') {
								return '未完成';
							}
						}
					}, {
						header : '完成情况',
						dataIndex : 'check_memo',
						width : 100
					}]);
			// 排序
			colModel.defaultSortable = true;
			var bbar = new Ext.PagingToolbar({
						pageSize : 10,
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
						bbar : bbar,
						fitToFrame : true,
						border : false,
						width : 200
					});
			var viewport = new Ext.Viewport({
						layout : 'fit',
						autoWidth : true,
						autoHeight : true,
						fitToFrame : true,
						items : [recGrid]
					});
			setTimeout(function() {
						Ext.get('loading').remove();
						Ext.get('loading-mask').fadeOut({
									remove : true
								});
					}, 250);
		});