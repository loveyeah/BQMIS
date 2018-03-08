Ext.namespace('ReportItem.item')
ReportItem.item = function() {
	var report;
	// 指标数据
	var itemRec = new Ext.data.Record.create([{
				// 指标ID
				name : 'itemId',
				mapping : 0
			}, {// 报表ID
				name : 'reportId',
				mapping : 1
			}, {// 指标编码
				name : 'itemCode',
				mapping : 2
			}, {// 指标名称
				name : 'itemName',
				mapping : 3
			}, {// 计量单位Id
				name : 'unitId',
				mapping : 4
			}, {// 指标别名
				name : 'alias',
				mapping : 5
			}, {// 主题ID
				name : 'topicId',
				mapping : 6
			}, {// 时间类别
				name : 'timeType',
				mapping : 7
			}, {// 显示顺序
				name : 'displayNo',
				mapping : 8
			}
//			, {// 报表名称
//				name : 'reportName',
//				mapping : 9
//			}, {// 报表类型
//				name : 'reportType',
//				mapping : 10
//			}
			, {// 单位名称
				name : 'unitName',
				mapping : 11
			}, {// 主题名称
				name : 'topicName',
				mapping : 12
			}
//			, {// 主题显示顺序
//				name : 'topicDisplayNo',
//				mapping : 13
//			}
			])
	// 指标store
	var store = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'managexam/getAllReportItemList.action'
						}),
				reader : new Ext.data.JsonReader({
							root : 'list',
							totalProperty : 'totalCount'
						}, itemRec)
			})

	var operatorItem = new Power.operateItem();
	// 指标编码 operatorItem.combo

	// 指标id
	var reportId = new Ext.form.Hidden({
				id : 'reportId'
			})
	// 计量单位 unitNs.combo
	var unitNs = new Power.unit()
	unitNs.btnConfirm.on("click", function() {
				if (grid.getSelectionModel().hasSelection()) {
					var rec = grid.getSelectionModel().getSelected();
					var aUnit = unitNs.getValue();
					if (aUnit) {
						rec.set('unitId', aUnit.unitId);
						rec.set('unitName', aUnit.unitName);
					}
				}
			});
	// 显示顺序
	var displayNo = new Ext.form.NumberField({
				id : 'displayNo',
				name : 'item.displayNo',
				fieldLabel : '显示顺序',
				allowDecimals : false,
				allowNegative : false
			})
	// 所属主题 topicNs.combo
	var topicNs = new Power.topic()
	topicNs.btnConfirm.on('click', function() {
				if (grid.getSelectionModel().hasSelection()) {
					var rec = grid.getSelectionModel().getSelected()
					var aTopic = topicNs.getValue()
					if (aTopic) {
						rec.set('topicId', aTopic.topicId)
						rec.set('topicName', aTopic.topicName)
					}
				}
			})
	// 时间类型
	var timeTypeStore = new Ext.data.SimpleStore({
				fields : ['value', 'text'],
				data : [['1', '月指标'], ['2', '年指标']]
			})
	var timeType = new Ext.form.ComboBox({
				id : 'timeType',
				name : 'item.timeType',
				hiddenName : 'item.timeType',
				fieldLabel : '时间类型',
				mode : 'local',
				displayField : 'text',
				valueField : 'value',
				store : timeTypeStore,
				triggerAction : 'all',
				readOnly : true
			})
	// 选择模式
	var sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : false
			})

	// 列模式
	var cm = new Ext.grid.ColumnModel([sm, new Ext.grid.RowNumberer({
						header : '行号',
						width : 35
					}), {
				header : '指标编码',
				dataIndex : 'itemCode'
			}, {
				header : '指标名称',
				width : 150,
				dataIndex : 'itemName'
			}, {
				header : '指标别名',
				dataIndex : 'alias',
				width : 150,
				editor : new Ext.form.TextField(),
				renderer : function(v){
					return "<pre>"+v+"</pre>";
				}
			}, {
				header : '计量单位',
				dataIndex : 'unitName',
				editor : unitNs.combo
			}, {
				header : '所属主题',
				dataIndex : 'topicName',
				editor : topicNs.combo
			}, {
				header : '时间类型',
				dataIndex : 'timeType',
				editor : timeType,
				renderer : function(v) {
					if (v == 1)
						return '月指标';
					else if (v == 2)
						return '年指标';
					else
						return '';
				}
			}, {
				header : '显示顺序',
				dataIndex : 'displayNo',
				editor : displayNo
			}]);

	var disText = new Ext.form.TextField({
				id : 'disText',
				disabled : true
			})
	var queryTheme = new Power.topic({
				width : 100
			});
	var queryText = new Ext.form.TextField({
				emptyText : '指标编码/别名模糊查询'
			})
	
	var themeLabel = new Ext.form.Label({
		id : 'themeLabel',
		text : '主题：'
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
				tbar : ['报表名称：', disText, '-', themeLabel, queryTheme.combo,
						queryText, {
							id : 'queryBtu',
							text : '查询',
							iconCls : 'query',
							handler : queryFun
						}, {
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
						}]
			});

	operatorItem.confirmBtu.on('click', function() {
				var rvo = operatorItem.saveRecords();
				if (typeof(rvo) != "undefined" && rvo.length > 0) {
					var i;
					for (i = 0; i < rvo.length; i++) {
						var count = store.getCount();
						var currentIndex = count;
						var o = new itemRec({
									'itemId' : null,
									'reportId' : reportId.getValue(),
									'itemCode' : null,
									'itemName' : null,
									'unitId' : null,
									'alias' : null,
									'topicId' : null,
									'topicName' : null,
									'timeType' : 1,
									'displayNo' : null
								});
						grid.stopEditing();
						store.insert(currentIndex, o);
						sm.selectRow(currentIndex);
						o.set("itemCode", rvo[i].itemCode);
						o.set("itemName", rvo[i].itemName);
						o.set("alias", rvo[i].itemName);
						o.set("unitId", rvo[i].unitId);
						o.set("unitName", rvo[i].unitName);
						// grid.startEditing(currentIndex, 3);
					}
					resetLine();
				}
			})
	// 指标选择
	function addFun() {
		operatorItem.win.show();
	}
	var ids = new Array();
	function queryFun(){
		store.rejectChanges()
		store.baseParams = {
			reportId : reportId.getValue(),
			theme : queryTheme.combo.getValue(),
			queryText : queryText.getValue()
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
			if (selections[i].get('itemId') != null)
				ids.push(selections[i].get('itemId'))
			store.remove(selections[i])
			store.getModifiedRecords().remove(selections[i]);
			grid.getView().refresh();
			resetLine();
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
			if(report.get('reportType') == 2)
			{
				if(store.getAt(i).get('topicId') == null)
				{
					Ext.Msg.alert('提示','所属主题不能为空！');
					return;
				}
			}
				for(var j = i + 1; j < store.getCount(); j++)
				{
					if(report.get('reportType') == 1
						&& store.getAt(i).get('itemCode') == store.getAt(j).get('itemCode'))
					{
						Ext.Msg.alert('提示','指标不能重复，请修改！');
						return;
					}
					else if(report.get('reportType') == 2&&
					store.getAt(i).get('topicId') == store.getAt(j).get('topicId')
					&& store.getAt(i).get('itemCode') == store.getAt(j).get('itemCode'))
					{
						Ext.Msg.alert('提示','指标在同一主题下不能重复，请修改！');
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
					
					if(mod[i].get('itemId') == null)
						isAdd.push(mod[i].data);
					else
						isUpdate.push(mod[i].data)
				}
				Ext.Ajax.request({
					url : 'managexam/saveReportItemModi.action',
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
	function resetLine() {
		for (var j = 0; j < store.getCount(); j++) {
			var temp = store.getAt(j);
			temp.set("displayNo", j + 1);
		}
	}
	grid.on("rowcontextmenu", function(g, i, e) {
		e.stopEvent();
		var _store = grid.getStore();
		var menu = new Ext.menu.Menu({
			id : 'fuctionMenu',
			items : [new Ext.menu.Item({
				text : '移至顶行',
				iconCls : 'priorStep',
				handler : function() {
					if (i != 0) {
						var record = _store.getAt(i);
						_store.remove(record);
						_store.insert(0, record);
						resetLine();
					}
				}
			}), new Ext.menu.Item({
				text : '上移',
				iconCls : 'priorStep',
				handler : function() {
					if (i != 0) {
						var record = _store.getAt(i);
						_store.remove(record);
						_store.insert(i - 1, record);
						resetLine();
					}
				}
			}), new Ext.menu.Item({
				text : '下移',
				iconCls : 'nextStep',
				handler : function() {
					if ((i + 1) != _store.getCount()) {
						var record = _store.getAt(i);
						_store.remove(record);
						_store.insert(i + 1, record);
						resetLine();
					}
				}
			}), new Ext.menu.Item({
				text : '移至最后',
				iconCls : 'nextStep',
				handler : function() {
					if ((i + 1) != _store.getCount()) {
						var record = _store.getAt(i);
						_store.remove(record);
						_store.insert(_store.getCount(), record);
						resetLine();
					}
				}
			})]
		});
		var coords = e.getXY();
		menu.showAt([coords[0], coords[1]]);
	});
	return {
		grid : grid,
		setReport : function(record) {
			report = record;
			reportId.setValue(record.get('reportId'));
			disText.setValue(record.get('reportName'));
			if (record.get('reportType') == 1) {
				cm.setHidden(6, true);
				cm.setHidden(7, true);
				themeLabel.setVisible(false);
				queryTheme.combo.setValue(null,null)
				queryTheme.combo.setVisible(false)
			} else {
				cm.setHidden(6, false);
				cm.setHidden(7, false);
				themeLabel.setVisible(true);
				queryTheme.setValue(null,null)
				queryTheme.combo.setVisible(true)
			}
			queryFun()
		}
	}
}