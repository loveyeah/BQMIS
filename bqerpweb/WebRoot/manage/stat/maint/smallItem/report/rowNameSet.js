Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var args = window.dialogArguments;
	var reportId = args.reportId;
	var reportName = args.reportName; 
	var rec = Ext.data.Record.create([{
		name : 'rowDatatypeId'
	}, {
		name : 'reportId'
	}, {
		name : 'rowDatatypeName'
	},{
		name : 'isItem'
	}, {
		name : 'orderBy'
	}]);

	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
		 url : 'manager/findSmallItemReportRowTypeList.action'
		}),
		reader : new Ext.data.JsonReader({
//			totalProperty : 'totalCount',
//			root : 'list'
		}, rec)
	});

	/* 设置每一行的选择框 */
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false
	});

	var rn = new Ext.grid.RowNumberer({

	});
	var isItemBox = new Ext.form.ComboBox({
		// fieldLabel : '数据类型',
		store : new Ext.data.SimpleStore({
			fields : ['value', 'text'],
			data : [['1', '是'], ['0', '否']]
		}),
		id : 'dataType',
		name : 'dataTypeText',
		valueField : "value",
		displayField : "text",
		mode : 'local',
		value : "0",
		typeAhead : true,
		forceSelection : true,
		hiddenName : 'model.dataType',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true 
	});
	var cm = new Ext.grid.ColumnModel([sm, rn, {
		header : '行设置名称',
		width : 80,
		dataIndex : 'rowDatatypeName',
		align : 'left',
		editor : new Ext.form.TextField({
			allowBlank : false
		})
	},{
		header : '是否指标',
		dataIndex : 'isItem',
		width:40,
		align:'center',
		renderer:function(v)
		{
			return (v == "0")?"否":"是";
		},
		editor:isItemBox
	}, {
		header : '排序',
		dataIndex : 'orderBy',
		width : 40,
		align : 'left',
		editor : new Ext.form.NumberField({
			allowBlank : false,
			maxLength : 10,
			maxLengthText : '最多输入10个数字！'
		})
	}]);
	ds.load({
		params : {
			reportId : reportId
		}
	});
	// 增加
	function addRecords() {
		var currentRecord = grid.getSelectionModel().getSelected();
		var rowNo = ds.indexOf(currentRecord);
		var count = ds.getCount();
		// var currentIndex = currentRecord ? rowNo : count;
		var o = new rec({
			'rowDatatypeId' : '',
			'reportId' : reportId,
			'rowDatatypeName' : '',
			'isItem':'1',
			'orderBy' : ''
		});

		grid.stopEditing();
		ds.insert(count, o);
		sm.selectRow(count);
		grid.startEditing(count, 2);
	}

	// 删除记录
	var ids = new Array();
	function deleteRecords() {
		grid.stopEditing();
		var sm = grid.getSelectionModel();
		var selected = sm.getSelections();
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
			for (var i = 0; i < selected.length; i += 1) {
				var member = selected[i];
				if (member.get("rowDatatypeId") != null
						&& member.get("rowDatatypeId") != "") {
					ids.push(member.get("rowDatatypeId"));
				}
				grid.getStore().remove(member);
				grid.getStore().getModifiedRecords().remove(member);
			}
		}
	}
	// 保存
	function save() {
		grid.stopEditing();
		var modifyRec = grid.getStore().getModifiedRecords();
		if (modifyRec.length > 0 || ids.length > 0) {
			Ext.Msg.confirm('提示', '确定要保存修改吗?', function(b) {
				if (b == "yes") {
					var updateData = new Array();
					for (var i = 0; i < modifyRec.length; i++) {
						if (modifyRec[i].data.rowDatatypeName == "") {
							Ext.MessageBox.alert('提示信息', '行设置名称不能为空！')
							return
						}
						updateData.push(modifyRec[i].data);
					}
					Ext.Ajax.request({
						url : 'manager/smallItemReportRowTypeMaint.action',
						method : 'post',
						params : {
							isUpdate : Ext.util.JSON.encode(updateData),
							isDelete : ids.join(",")
						},
						success : function(result, request) {
							var o = eval('(' + result.responseText + ')');
							Ext.MessageBox.alert('提示信息', o.msg);
							ds.rejectChanges();
							ids = [];
							ds.reload();
						},
						failure : function(result, request) {
							Ext.MessageBox.alert('提示信息', '操作失败！')
						}
					})
				}
			})
		} else {
			Ext.MessageBox.alert('提示信息', '没有做任何修改！')
		}
	}
	// 取消
	function cancer() {
		var modifyRec = ds.getModifiedRecords();
		if (modifyRec.length > 0 || ids.length > 0) {
			Ext.Msg.confirm('提示', '确定要放弃修改吗?', function(b) {
				if (b == "yes") {
					ds.reload();
					ds.rejectChanges();
					ids = [];
				}
			})
		} else {
			ds.reload();
			ds.rejectChanges();
			ids = [];
		}
	}

	var tbar = new Ext.Toolbar({
		items : [{
			id : 'btnAdd',
			iconCls : 'add',
			text : "新增",
			handler : addRecords

		}, '-', {
			id : 'btnDelete',
			iconCls : 'delete',
			text : "删除",
			handler : deleteRecords

		}, '-', {
			id : 'btnCancer',
			iconCls : 'cancer',
			text : "取消",
			handler : cancer

		}, '-', {
			id : 'btnSave',
			iconCls : 'save',
			text : "保存修改",
			handler : save
		}]

	});
	/* 创建表格 */
	var grid = new Ext.grid.EditorGridPanel({
		title : "<"+reportName+">行设置",
		ds : ds,
		cm : cm,
		sm : sm,
		tbar : tbar,
		viewConfig : {
			forceFit : true
		},
		autoWidth : true,
		border : false,
		clicksToEdit : 1// 单击一次编辑
	});

	// 设定布局器及面板
	var viewport = new Ext.Viewport({
		layout : 'fit',
		fitToFrame : true,
		items : [grid]
	}); 
})