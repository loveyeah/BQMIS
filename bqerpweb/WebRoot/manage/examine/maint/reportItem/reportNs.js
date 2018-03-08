Ext.namespace('ReportItem.report');
ReportItem.report = function(choose,config){
	// choose 判断是否为选择用
	// config 组合框的属性设置
	var record = new Ext.data.Record.create([{
		// 报表id
		name : 'reportId',
		mapping : 0
	},{
		// 报表名
		name : 'reportName',
		mapping :1
	},{
		// 报表类型
		name : 'reportType',
		mapping : 2
	}])
	
	var store = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'managexam/findAllReportRec.action'
		}),
		reader : new Ext.data.JsonReader({
			root : 'list',
			totalProperty : 'totalCount'
		},record)
	})
	
//	store.load();
	
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect  : false
	})
	var tyepCombo = new Ext.form.ComboBox({
				id : 'combobox',
				store : new Ext.data.SimpleStore({
					fields : ['value','text'],
					data  : [['1','常规报表'],['2','分主题报表']]
				}),
				displayField  : 'text',
				valueField : 'value',
				mode : 'local',
				triggerAction : 'all',
				value : 1
	})
	var cm = new Ext.grid.ColumnModel([sm,
		new Ext.grid.RowNumberer({
			header : '行号',
			width : 35
		}),{
			header : '报表ID',
			align : 'center',
			dataIndex : 'reportId',
			width : 50
		},{
			header : '报表名称',
			dataIndex : 'reportName',
			width : 300,
			editor : new Ext.form.TextField()
		},{
			header : '报表类型',
			dataIndex : 'reportType',
			editor : tyepCombo,
			renderer : function(v){
				if(v == 1)
					return '常规报表';
				else if(v == 2)
					return '分主题报表';
				else
					return '';
			}
		}
		])
	//增加
	var addBtu = new Ext.Button({
		id : 'addBtu',
		text :'增加',
		iconCls : 'add',
		handler : addFun
	})
	function addFun(){
		var rec = new record({
			reportId : null,
			reportName : null,
			reportType : 1
		})
		var count = store.getCount();
		store.add(rec);
		grid.startEditing(count,2);
	}
	
	// 保存
	var saveBtu = new Ext.Button({
		id : 'saveBtu',
		text  : '保存',
		iconCls : 'save',
		handler : saveFun
	})
	var ids = new Array();
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
		for(var i = 0; i < store.getCount() - 1; i++)
				for(var j = i + 1; j < store.getCount(); j++)
				{
					if(store.getAt(i).get('reportName') == store.getAt(j).get('reportName'))
					{
						Ext.Msg.alert('提示','报表名称不能重复，请修改！');
						return;
					}
				}
		Ext.Msg.confirm('提示','确认要保存数据吗？',function(button){
			if(button == 'yes'){
				var isAdd = new Array();
				var isUpdate = new Array();
				
				for(var i = 0; i < mod.length; i++)
				{
					
					if(mod[i].get('reportId') == null)
						isAdd.push(mod[i].data);
					else
						isUpdate.push(mod[i].data)
				}
//				alert(isAdd.join(','))
//				alert(isUpdate.join(','))
				Ext.Ajax.request({
					url : 'managexam/saveModiReportEntity.action',
					method : 'post',
					params : {
						isAdd : Ext.util.JSON.encode(isAdd),
						isUpdate : Ext.util.JSON.encode(isUpdate),
						ids  : ids.join(',')
					},
					success : function(response,options)
					{
						Ext.Msg.alert('提示','数据保存修改成功！');
						refleshFun()
					},
					failure : function(response,options){
						Ext.Msg.alert('提示','数据保存修改失败！')
					}
				})
			}
		})
	}
	
	// 删除
	var deleteBtu = new Ext.Button({
		id : 'deleteBtu',
		text : '删除',
		iconCls : 'delete',
		handler  : deleteFun
	})
	
	function deleteFun(){
			var selections = sm.getSelections();
			if (selections.length == 0) {
				Ext.Msg.alert('提示', '请选择要删除的数据！')
				return;
			}
			
			for (var i = 0; i < selections.length; i++)
			{
				if(selections[i].get('reportId') != null)
					ids.push(selections[i].get('reportId'))
				
				store.remove(selections[i])
				store.getModifiedRecords().remove(selections[i]);
				grid.getView().refresh();
			}
	}
	
	// 刷新
	var refleshBtu = new Ext.Button({
		id : 'refleshBtu',
		text  : '刷新',
		iconCls : 'reflesh',
		handler : refleshFun
	})
	function refleshFun(){
		store.rejectChanges()
		store.load();
		ids = [];
	}
	
	//××××××××××××××××××××××××××做选择用的各个空间 开始**************************************
	// 确定 用以选择报表
	var confirmBtu = new Ext.Button({
		id : 'confirmBtu',
		text : '确定',
		iconCls : 'confirm',
		handler : selectReport
	})
	
	// 取消
	var cancelBtu = new Ext.Button({
		id : 'cancelBtu',
		text  : '取消',
		iconCls : 'cancer',
		handler : cancelFun
	})
	
	
	// 报表组合框数据源
	var cbStore = new Ext.data.Store({
		reader : new Ext.data.JsonReader({},record)
	})
	var reportCombo =  new Ext.form.ComboBox({
		id : 'reportComboId',
		store : cbStore,
		displayField : 'reportName',
		valueField : 'reportId',
		mode : 'local',
		triggerAction : 'all',
		name : 'reportCombo',
		hiddenName : 'reportCombo',
		fieldLabel : '报表',
		readOnly : true,
		onTriggerClick : function(){
			win.show();
		}
	})
	Ext.apply(reportCombo,config)
	// 组合框设值方法
	function setCbValue(reportId,reportName)
	{
		var d1 = new record({
			reportId : reportId,
			reportName : reportName
		})
		cbStore.removeAll()
		cbStore.add(d1);
		reportCombo.setValue(reportId)
	}
	//××××××××××××××××××××××××××做选择用的各个空间 结束**************************************
	var tbar = new Ext.Toolbar({
		id : 'tbar',
		items : [addBtu,saveBtu,deleteBtu,refleshBtu,confirmBtu,cancelBtu]
	})
	
	var grid = new Ext.grid.EditorGridPanel({
		id : 'grid',
		store : store,
		sm : sm,
		cm : cm,
		tbar : tbar,
		clicksToEdit : 1
	})
	// 选择窗口
	var win = new Ext.Window({
		id : 'win',
		title : '报表选择窗口',
		width : 500,
		height : 600,
		layout : 'fit',
		items : [grid],
		closable : false,
		modal : true
	})
	
	function cancelFun(){
		win.hide()
		return null;
	}
	function selectReport(){
		if(sm.hasSelection())
		{
			if(sm.getSelections().length > 1){
				Ext.Msg.alert('提示','请选择其中一条报表！')
			}else{
				var selected = sm.getSelected();
				setCbValue(selected.get('reportId'),selected.get('reportName'))
				if(choose && choose == true)
					win.hide();
				return selected;
			}
		}
		else{
			Ext.MessageBox.alert('提示','请选择一条报表！');
		}
		if(choose && choose == true)
			win.hide();
		return null;
	}
	refleshFun();
	
	// 是选择时
	if(choose && choose == true)
	{
		addBtu.setVisible(false)
		saveBtu.setVisible(false)
		deleteBtu.setVisible(false)
		confirmBtu.setVisible(true)
		cancelBtu.setVisible(true)
		grid.on('beforeedit',function(){
			return false
		})
	}else{
		addBtu.setVisible(true)
		saveBtu.setVisible(true)
		deleteBtu.setVisible(true)
		confirmBtu.setVisible(false)
		cancelBtu.setVisible(false)
	}
	return {
		grid : grid,
		win : win,
		store  : store,
		reportCombo : reportCombo,
		setCbValue : setCbValue,
		selectReport : selectReport,
		cancelFun : cancelFun,
		confirmBtu : confirmBtu,
		cancelBtu : cancelBtu
	}
}
	