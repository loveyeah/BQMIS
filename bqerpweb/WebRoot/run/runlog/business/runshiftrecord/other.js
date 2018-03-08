// 设置树的点击事件
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
function getDate() {
	var d, s, t;
	d = new Date();
	s = d.getFullYear().toString(10) + "-";
	t = d.getMonth() + 1;
	s += (t > 9 ? "" : "0") + t + "-";
	t = d.getDate();
	// s += (t > 9 ? "" : "0") + t;

	s += (t > 9 ? "" : "0") + t + " ";
	t = d.getHours();
	s += (t > 9 ? "" : "0") + t + ":";
	t = d.getMinutes();
	s += (t > 9 ? "" : "0") + t + ":";
	t = d.getSeconds();
	s += (t > 9 ? "" : "0") + t;

	return s;
}
Ext.onReady(function() {
	var runlogId = getParameter("runlogid");

	var unitStore = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'runlog/findUintProfessionList.action'
						}),
				reader : new Ext.data.JsonReader({
							id : "unit"
						}, [{
									name : 'specialityCode'
								}, {
									name : 'specialityName'
								}])
			});
	unitStore.load();

	var unitBox = new Ext.form.ComboBox({
				fieldLabel : '所属专业',
				id : 'unitBox',
				store : unitStore,
				valueField : "specialityCode",
				displayField : "specialityName",
				mode : 'local',
				triggerAction : 'all',
				forceSelection : true,
				hiddenName : 'specialityCode',
				editable : false,
				allowBlank : false,
				selectOnFocus : true,
				name : 'specialityCode',
				width : 150
			});
	unitStore.on("load", function(xx, records, o) {
				unitBox.setValue(records[0].data.specialityCode);
			});

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
							url : 'runlog/otherRecord.action',
							method : 'post'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : 'total',
							root : 'root'
						}, ShiftRec)
			});
	ds.on('beforeload', function() {
				Ext.apply(this.baseParams, {
							specialcode : unitBox.getValue(),
							runlogid : runlogId
						});
			});
	ds.load({
				params : {
					start : 0,
					limit : 18
				}
			});
	// 单击Grid行事件
	var sm = new Ext.grid.CheckboxSelectionModel();
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
			}]);
	// 排序
	colModel.defaultSortable = true;
	var tbar = new Ext.Toolbar({
		items : [{
					id : 'query',
					iconCls : 'query',
					text : "查询",
					handler : query
				}, '-', unitBox, '-', {
					id : 'elserec',
					text : "导入到值长记事",
					iconCls : 'import',
					handler : function() {
						var rec = recGrid.getSelectionModel().getSelections();
						if (rec.length > 0) {
							var ids = "";
							for (i = 0; i < rec.length; i++) {
								ids = ids + rec[i].get("shift_record_id") + ",";
							}
							ids = ids.substr(0, ids.length - 1);
							Ext.Ajax.request({
										url : 'runlog/impRecord.action',
										method : 'post',
										params : {
											ids : ids,
											runlogid : runlogId
										},
										success : function(form, action) {
											var obj = new Object();
											window.returnValue = obj;
											window.close();
										},
										failure : function() {
											Ext.Msg.alert("导入失败,请联系管理员！");
										}
									});
						} else {
							alert("请选择要导入的记录！");
						}
					}
				}]
	});
	var win = new Ext.Window({
				width : 500,
				height : 330,
				closeAction : 'hide'
			});
	function query() {
		if (unitBox.getValue() != 'ZZ') {
			ds.load({
						params : {
							start : 0,
							limit : 15
						}
					})
		}
	}
	var bbar = new Ext.PagingToolbar({
				pageSize : 15,
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
				sm : sm,
				bbar : bbar,
				tbar : tbar,
				border : true,
				width : 200,
				autoScroll : true,
				fitToFrame : true,
				border : false
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