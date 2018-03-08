Ext.namespace('Power.unit'); 
Power.unit = function(config) {
	var Unit = new Ext.data.Record.create([{
		name : 'unitId'
	}, {
		name : 'unitName'
	}, {
		name : 'unitAlias'
	}]);
	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'comm/findUnitList.action'
		}),
		reader : new Ext.data.JsonReader({
			root : 'list',
			totalProperty : 'totalCount'
		}, Unit)
	});

	var fuuzy = new Ext.form.TextField({
		id : "fuzzy",
		name : "fuzzy",
		listeners : {
			specialkey : function(field, e) {
				if (e.getKey() == Ext.EventObject.ENTER) {
					ds.load({
						params : {
							start : 0,
							limit : bbar.pageSize
						}
					});
				}
			}
		}
	});
	var bbar = new Ext.PagingToolbar({
				pageSize : 18,
				store : ds,
				displayInfo : true,
				displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
				emptyMsg : "没有记录",
				beforePageText : '页',
				afterPageText : "共{0}"
			});
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true
	});
	var cm = new Ext.grid.ColumnModel([sm,{
		header : '单位名称',
		sortable : true,
		dataIndex : 'unitName'
	}, {
		header : '单位别名',
		sortable : true,
		dataIndex : 'unitAlias'
	}]);
	var btnConfirm = new Ext.Button({
		text : '确定', 
		id:'unit_confirm',
		iconCls : 'confirm',
		handler : function() {
//			var ro = getValue();
//			setValue(ro.unitId, ro.unitName);
			win.hide();
		}
	});
	ds.load({
		params : {
			start : 0,
			limit : bbar.pageSize
		}
	});

	ds.on("beforeload", function() {
		Ext.apply(this.baseParams, {
			queryKey : fuuzy.getValue()
		});
	});
	var tbar = new Ext.Toolbar({
		items : ['单位名称', fuuzy, new Ext.Button({
			text : '查询',
			iconCls : 'query',
			handler : function() {
				ds.load({
					params : {
						start : 0,
						limit : bbar.pageSize
					}
				});
			}
		}), '-', btnConfirm,'-',{
			text:'取消',
			iconCls:'cancer',
			handler:function(){
				win.hide();
			}
		}]
	});
	var grid = new Ext.grid.GridPanel({
		tbar : tbar,
		bbar : bbar,
		width : 400,
		height : 350,
		sm : sm,
		cm : cm,
		ds : ds,
		listeners:{
			'rowdblclick':function(grid, rowIndex, e){
				Ext.get("unit_confirm").dom.click();
			}
		},
		viewConfig : {
			forceFit : true
		}
	});
	var win = new Ext.Window({
		closeAction : 'hide',
		title : '选择单位',
		modal : true,
		width : 500,
		height : 400,
		layout : 'fit',
		items : [grid]
	});
	var cbStore = new Ext.data.Store({
		reader : new Ext.data.JsonReader({}, Unit)
	}); 
	var combo = new Ext.form.ComboBox({
		fieldLabel : '选择单位',
		store : cbStore,
		mode : 'local',
		hiddenName :'unit',
		name : 'unit',
		width: 180,
		valueField : 'unitId',
		displayField : 'unitName',
		editable : true,
		triggerAction : 'all',
		forceSelection:true,
		readOnly : true, 
		listeners:(config&&config.listeners)?config.listeners:{},
		onTriggerClick : function() {
			win.show();
		}
	});
	Ext.apply(combo, config);
	 
	function setValue(unitId, unitName) {
		var d1 = new Unit({
			unitId : unitId,
			unitName : unitName
		});
		cbStore.removeAll();
		cbStore.add(d1);
		combo.setValue(unitId);
	}
	function getValue() {
		var record = grid.getSelectionModel().getSelected();
		if (typeof(record) != "undefined") {
			var ro = new Object();
			ro.unitId = record.get("unitId");
			ro.unitName = record.get("unitName");
			return ro;
		} else {
			return null;
		}
	}
	return {
		ds : ds,
		grid : grid,
		win : win,
		btnConfirm : btnConfirm,
		combo : combo,
		setValue : setValue,
		getValue : getValue
	}
}