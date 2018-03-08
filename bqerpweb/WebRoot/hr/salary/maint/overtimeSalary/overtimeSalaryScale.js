Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

Ext.ns("OvertimeSalary.overtimeSalaryScale");
	OvertimeSalary.overtimeSalaryScale = function() {
	
	var sm = new Ext.grid.CheckboxSelectionModel();
	var Storelist = new Ext.data.Record.create([sm, {
		name : 'overtimeSalaryScaleId',
		mapping : 0
	}, {
		name : 'overtimeTypeId',
		mapping : 1
	}, {
		name : 'typeName',
		mapping : 2
	}, {
		name : 'overtimeSalaryScale',
		mapping : 3
	}, {
		name : 'memo',
		mapping : 4
	}]);

	var store = new Ext.data.JsonStore({
		url : 'com/findSalaryScaleList.action',
		root : 'list',
		totalProperty : 'totalCount',
		fields : Storelist
	});
	store.reload({
			params : {
				start : 0,
				limit : 18
			}
	});
	var grid = new Ext.grid.EditorGridPanel({
		store : store,
		layout : 'fit',
		columns : [new Ext.grid.RowNumberer(), {
			header : "ID",
			width : 100,
			sortable : false,
			hidden : true,
			dataIndex : 'overtimeSalaryScaleId'
		}, {
			header : "加班类别",
			width : 100,
			sortable : false,
			dataIndex : 'typeName'
		}, {
			header : "加班工资系数",
			width : 100,
			sortable : false,
			css : CSS_GRID_INPUT_COL,
			dataIndex : 'overtimeSalaryScale',
			editor:new Ext.form.NumberField({
			maxLength : 100,
			allowDecimals : true
			})
		}, {
			header : "备注",
			width : 100,
			id : 'finishNumber',
			sortable : false,
			css : CSS_GRID_INPUT_COL,
			dataIndex : 'memo',
			editor:new Ext.form.TextField()
		}],

		tbar : [{
			id : 'btnReset',
			text : "重置",
			iconCls : 'add',
			handler : resetRecord
		}, '-', {
			id : 'btnSave',
			text : "保存",
			iconCls : 'save',
			handler : saveRecord
		}],
		sm : sm,
		viewConfig : {
			forceFit : true
		}
	});
	
	function resetRecord()
	{
		var ids = new Array();
		for (var i = 0; i < grid.getStore().getCount(); i++) {
				var rec = grid.getStore().getAt(i);
				ids.push(rec.get("overtimeSalaryScaleId"));
		}
		Ext.Msg.confirm("提示", "重置后将清空现有数据,是否确认重置?", function(buttonobj) {
				if (buttonobj == "yes") {
					Ext.Msg.wait("操作进行中...", "请稍候");
					Ext.Ajax.request({
						method : 'post',
						url : 'com/resetReocord.action',
						params : {
							ids : ids.join(',')
						},
						success : function(action) {
							Ext.Msg.alert("提示", "操作成功！");
							store.reload();
						},
						failure : function() {
							Ext.MessageBox.alert('提示', '未知错误！')
						}
					});
				}
			})
	}
	
	var ids = new Array();
	function saveRecord() {
		grid.stopEditing();
		var modifyRec = grid.getStore().getModifiedRecords();
		if (modifyRec.length > 0 || ids.length > 0) {
			if (!confirm("确定要保存修改吗?"))
				return;
			var updateData = new Array();
			for (var i = 0; i < grid.getStore().getCount(); i++) {
				var rec = grid.getStore().getAt(i);
				if (rec.get("overtimeSalaryScale") == null || (rec.get("overtimeSalaryScale") == "" && rec.get("overtimeSalaryScale") != "0")) {
		
					Ext.MessageBox.alert('提示', '加班工资系数不能为空！')
					return
				}
				updateData.push({
							id : rec.get("overtimeSalaryScaleId"),       
							scale : rec.get("overtimeSalaryScale"),
							memo : rec.get("memo")
						});
			}
			Ext.Ajax.request({
				url : 'com/saveRecord.action',
				method : 'post',
				params : {
					isUpdate : Ext.util.JSON.encode(updateData)
				},
				success : function(result, request) {
					var o = eval('(' + result.responseText + ')');
					Ext.MessageBox.alert('提示', o.msg);
					store.rejectChanges();
					store.reload();
				},
				failure : function(result, request) {
					Ext.MessageBox.alert('提示', '未知错误！')
				}
			})
		} else {
			Ext.MessageBox.alert('提示', '没有做任何修改！')
		}
	}
	
	return {
			grid : grid
		}
	};
})