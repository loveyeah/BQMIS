Ext.namespace('ReportItem.block')
ReportItem.block = function(){
	var report;
	var blockRec = new Ext.data.Record.create([{
		// 机组id
		name : 'blockId',
		mapping : 0
	},{// 报表id
		name : 'reportId',
		mapping : 1
	},{//机组名称
		name : 'blockName',
		mapping : 2
	},{// 显示顺序
		name : 'displayNo',
		mapping : 3 
	}// 4：报表名称 5：报表类型
	])
	var store = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'managexam/getReportBlockList.action'
		}),
		reader : new Ext.data.JsonReader({
			root : 'list',
			totalProperty : 'totalCount'
		},blockRec)
	})
	
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false
	})

	var cm = new Ext.grid.ColumnModel([sm,new Ext.grid.RowNumberer({
		header : '行号',
		width : 35
	}),{
		header : '机组名称',
		width : 300,
		dataIndex : 'blockName',
		editor : new Ext.form.TextField()
	},{
		header : '显示顺序',
		dataIndex : 'displayNo',
		editor : new Ext.form.NumberField({
			allowDecimals : false,
			allowNegative : false
		})
	}])
	var disText = new Ext.form.TextField({
				id : 'disText',
				disabled : true
			})
	var grid = new Ext.grid.EditorGridPanel({
				id : 'grid',
				layout : 'fit',
				border : false,
				frame : true,
				store : store,
				clicksToEdit : 1,
				sm : sm,
				cm : cm,
				tbar : ['报表名称：', disText,
						 {
							id : 'addBtu',
							text : '新增',
							iconCls : 'add',
							handler : addFun
						}, {
							id : 'saveBtu',
							text : '保存',
							iconCls : 'save',
							handler : saveFun
						}, {
							id : 'deleteBtu',
							text : '删除',
							iconCls : 'delete',
							handler : deleteFun
						}, {
							id : 'queryBtu',
							text : '刷新',
							iconCls : 'query',
							handler : queryFun
						}]
			});
			
	function addFun() {
		var rec = new blockRec({
			blockId : null,
			reportId : report.get('reportId'),
			blockName : null,
			displayNo : null
		})
		var count = store.getCount();
		store.add(rec);
		grid.startEditing(count,2);
	}
	var ids = new Array();
	function queryFun(){
		store.rejectChanges()
		store.baseParams = {
			reportId : report.get('reportId')
		}
		store.load();
		ids = [];
	}
	
	function deleteFun()
	{
		var selections = sm.getSelections();
		if (selections.length == 0) {
			Ext.Msg.alert('提示', '请选择要删除的数据！')
			return;
		}

		for (var i = 0; i < selections.length; i++) {
			if (selections[i].get('blockId') != null)
				ids.push(selections[i].get('blockId'))
			store.remove(selections[i])
			store.getModifiedRecords().remove(selections[i]);
			grid.getView().refresh();
		}

	}
	
	function saveFun(){
		if(store.getCount() == 0 && ids.length == 0){
			Ext.Msg.alert('提示','无数据进行保存！');
			return;
		}
		var mod = store.getModifiedRecords();
		if(mod.length == 0 && ids.length == 0)
		{
			Ext.Msg.alert('提示','数据未改变！');
			return
		}
		for(var i = 0; i < store.getCount(); i++)
		{
			if (store.getAt(i).get('blockName') == null
					|| store.getAt(i).get('blockName') == "") {
				Ext.Msg.alert('提示', '机组名称不可为空！');
				return;
			}
			for (var j = i + 1; j < store.getCount(); j++) {
				if (store.getAt(i).get('blockName') == store.getAt(j)
						.get('blockName')) {
					Ext.Msg.alert('提示', '机组名称不能重复，请修改！');
					return;
				}
			}
		}
		Ext.Msg.confirm('提示','确认要保存数据吗？',function(button){
			if(button == 'yes'){
				var isAdd = new Array();
				var isUpdate = new Array();
				
				for(var i = 0; i < mod.length; i++)
				{
					
					if(mod[i].get('blockId') == null)
						isAdd.push(mod[i].data);
					else
						isUpdate.push(mod[i].data)
				}
				Ext.Ajax.request({
					url : 'managexam/saveReportBlockModi.action',
					method : 'post',
					params : {
						isAdd : Ext.util.JSON.encode(isAdd),
						isUpdate : Ext.util.JSON.encode(isUpdate),
						ids  : ids.join(',')
					},
					success : function(response,options)
					{
						Ext.Msg.alert('提示','数据保存修改成功！');
						queryFun()
					},
					failure : function(response,options){
						Ext.Msg.alert('提示','数据保存修改失败！')
					}
				})
			}
		})
	}
	return {
		grid : grid,
		setReport : function(record) {
			report = record;
//			reportId.setValue(record.get('reportId'));
			disText.setValue(record.get('reportName'));
			queryFun();
		}
	}
}