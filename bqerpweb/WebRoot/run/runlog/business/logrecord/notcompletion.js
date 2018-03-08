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
	var runlogId = getParameter("runlogId");
	var store = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'runlog/findRunLogMainItem.action',
							method : 'post'
						}),
				reader : new Ext.data.JsonReader({
							root : 'list'
						}, [{
									name : 'mainItemCode',
									mapping : 'mainItemCode'
								}, {
									name : 'mainItemName',
									mapping : 'mainItemName'
								}])
			});
	store.load();
	var itemTypeCombo = new Ext.form.ComboBox({
				id : 'itemType-combo',
				fieldLabel : '记事类别',
				store : store,
				displayField : 'mainItemName',
				valueField : 'mainItemCode',
				mode : 'local',
				typeAhead : true,
				forceSelection : true,
				hiddenName : 'runrec.mainItemCode',
				editable : false,
				triggerAction : 'all',
				blankText : '请选择...',
				emptyText : '请选择...',
				allowBlank : false,
				selectOnFocus : true,
				readOnly : true,
				editable : false
			});

	var completionComboBox = new Ext.form.ComboBox({
				id : 'completion-combo',
				fieldLabel : '完成状态',
				store : new Ext.data.SimpleStore({
							fields : ["retrunValue", "displayText"],
							data : [['Y', '已完成'], ['N', '未完成']]
						}),
				valueField : "retrunValue",
				displayField : "displayText",
				mode : 'local',
				forceSelection : true,
				hiddenName : 'runrec.isCompletion',
				editable : false,
				triggerAction : 'all',
				selectOnFocus : true,
				allowBlank : false,
				blankText : '请选择...',
				emptyText : '请选择...'
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
					title : '未完成项',
					autoHeight : true,
					border : true,
					defaults : {
						width : 200
					},
					items : [{
								xtype : 'hidden',
								name : 'runrec.reviewNo'
							}, {
								xtype : 'hidden',
								name : 'runrec.shiftRecordId'
							}, {
								name : 'runrec.runLogId',
								xtype : 'hidden',
								value : runlogId
							}, {
								fieldLabel : '记事时间',
								name : 'runrec.recordTime',
								xtype : 'textfield',
								style : 'cursor:pointer',
								listeners : {
									focus : function() {
										WdatePicker({
													startDate : '%y-%M-01 00:00:00',
													dateFmt : 'yyyy-MM-dd HH:mm:ss',
													alwaysUseStartDate : true
												});
									}
								},
								value : getDate()
							}, {
								xtype : 'textfield',
								fieldLabel : '记事类别',
								name : 'mainItemName',
								readOnly : true
							}, {
								xtype : 'hidden',
								name : 'runrec.mainItemCode',
								readOnly : true
							}, {
								allowBlank : false,
								name : 'runrec.recordBy',
								xtype : 'hidden',
								value : document.getElementById('workCode').value
							}, {
								fieldLabel : '记录人',
								allowBlank : false,
								readOnly : true,
								name : 'recordBy_text',
								xtype : 'textfield',
								value : document.getElementById('workName').value
							}, {
								xtype : 'textarea',
								fieldLabel : '记事内容',
								name : 'runrec.recordContent',
								width : 320,
								height : 100,
								readOnly : true
							}, {
								xtype : 'textarea',
								fieldLabel : '完成情况',
								name : 'runrec.checkMemo',
								width : 320,
								height : 50
							}, {
								allowBlank : false,
								readOnly : true,
								name : 'runrec.checkBy',
								xtype : 'hidden',
								value : document.getElementById('workCode').value
							}],
					buttons : [{
						text : '保存',
						iconCls : 'save',
						handler : function() {
							if (gridForm.getForm().isValid()) {
								gridForm.getForm().submit({
									url : 'runlog/completionRunShiftRecord.action',
									method : 'post',
									waitMsg : '正在保存数据...',
									success : function(form, action) {
										var rec = recGrid.getSelectionModel()
												.getSelected();
										ds.remove(rec);
										win.hide();
										ds.load();
									},
									failure : function(form, action) {
										Ext.Msg.alert("请联系管理员！");
									}
								})
							}
						}
					}, {
						text : '取消',
						iconCls : 'cancel',
						handler : function() {
							win.hide();
						}
					}]
				}]
			});
	// 接口
	function equsel() {

	}
	function equsel() {

	}
	function failjoin() {

	}
	function jobticket() {

	}
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
							url : 'runlog/findNotComlpetionRecord.action?runlogid='
									+ runlogId,
							method : 'post'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : 'total',
							root : 'root'
						}, ShiftRec)
			});
	ds.load();
	// 单击Grid行事件
	var sm = new Ext.grid.RowSelectionModel({
				singleSelect : true
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
			}]);
	// 排序
	colModel.defaultSortable = true;
	var tbar = new Ext.Toolbar({
				items : [{
							id : 'query',
							iconCls : 'query',
							text : "查询",
							handler : query
						}, '-', {
							id : 'btnUpdate',
							text : "标记为完成",
							iconCls : 'update',
							handler : modify
						}]
			});
	var win = new Ext.Window({
				width : 500,
				height : 400,
				closeAction : 'hide',
				items : [gridForm]
			});
	function add() {
		methods = "add";
		gridForm.getForm().reset();
		win.show();
	}
	function query() {
		ds.load({
					params : {
						start : 0,
						limit : 18,
						runlogid : runlogId
					}
				})
	}
	function del() {
		var rec = recGrid.getSelectionModel().getSelected();
		if (rec) {
			if (confirm('是否确定要删除所选的记录?')) {
				Ext.Ajax.request({
					url : 'runlog/deleteRunShiftRecord.action?runrec.shiftRecordId='
							+ rec.get("shiftRecordId"),
					method : 'post',
					success : function(result, request) {
						ds.load();
					},
					failure : function(result, request) {
						Ext.Msg.alert("删除失败请联系管理员！");
					}
				})
			}
		}
	}
	function elserec() {
	}
	function modify() {
		var rec = recGrid.getSelectionModel().getSelected();
		if (rec) {
			gridForm.getForm().reset();
			win.show();
			Ext.get("runrec.shiftRecordId").dom.value = rec
					.get("shift_record_id");
			Ext.get("runrec.runLogId").dom.value = rec.get("run_log_id");
			Ext.get("runrec.recordTime").dom.value = rec.get("record_time")
					.substr(0, 10);
			Ext.get("runrec.recordBy").dom.value = rec.get("record_by");
			Ext.get("runrec.recordContent").dom.value = rec
					.get("record_content");
			Ext.get("runrec.mainItemCode").dom.value = rec
					.get("main_item_code");
			Ext.get("mainItemName").dom.value = rec.get("main_item_name");
			completionComboBox.setValue(rec.get("is_completion"));
		} else {
			alert("请选择需要修改的记录！");
		}
	}
	// function fillFormValue(rec) {
	// }
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
				sm : sm,
				bbar : bbar,
				tbar : tbar,
				border : true,
				autoWidth : true,
				fitToFrame : true,
				border : false
			});
	recGrid.on('rowdblclick', function(grid, rowIndex, e) {
				gridForm.getForm().reset();
				win.show();
				var rec = recGrid.getStore().getAt(rowIndex);
				Ext.get("runrec.shiftRecordId").dom.value = rec
						.get("shift_record_id");
				Ext.get("runrec.runLogId").dom.value = rec.get("run_log_id");
				Ext.get("runrec.recordTime").dom.value = rec.get("record_time")
						.substr(0, 10);
				Ext.get("runrec.recordBy").dom.value = rec.get("record_by");
				Ext.get("runrec.recordContent").dom.value = rec
						.get("record_content");
				Ext.get("runrec.mainItemCode").dom.value = rec
						.get("main_item_code");
				Ext.get("mainItemName").dom.value = rec.get("main_item_name");
				completionComboBox.setValue(rec.get("is_completion"));
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