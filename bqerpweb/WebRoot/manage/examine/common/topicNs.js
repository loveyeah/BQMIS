Ext.namespace('Power.topic');
Power.topic = function(config) {
	
	function numberFormat(value) {
		if (value == null || value == '')
			return '0%';
		else {
			value = (value - 0) * 100;
			value = value.toString();
			if (value.indexOf(".") > 0) {
				value = value.substring(0, value.indexOf("."));
			}
			return value + "%";
		}
	}
	var topic = new Ext.data.Record.create([{
		name : 'topicId',
		mapping : 0,
		type : 'int'
	},{
		name : 'topicName',
		mapping : 1,
		type : 'string'
	}, {
		name : 'coefficient',
		mapping : 2,
		type : 'float'
	}, {
		name : 'displayNo',
		mapping : 3,
		type : 'int'
	}
	]);
	var ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'managexam/findAllTopic.action'
		}),
		reader : new Ext.data.JsonReader({
			root : 'list',
			totalProperty : 'totalCount'
		}, topic)
	});

//	var fuuzy = new Ext.form.TextField({
//		id : "fuzzy",
//		name : "fuzzy",
//		listeners : {
//			specialkey : function(field, e) {
//				if (e.getKey() == Ext.EventObject.ENTER) {
//					ds.load({
//						params : {
//							start : 0,
//							limit : bbar.pageSize
//						}
//					});
//				}
//			}
//		}
//	});
	var bbar = new Ext.PagingToolbar({
		pageSize : 18,
		store : ds,
		displayInfo : true,
		displayMsg : "显示第{0}条到{1}条，共{2}条",
		emptyMsg : "没有记录"
	});
	
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true
	});
	var cm = new Ext.grid.ColumnModel([sm,
		new Ext.grid.RowNumberer({
			header : "行号",
			width : 31
		}),{
		header : '主题id',
		dataIndex : 'topicId',
		hidden : true
	}, {
		header : '考核主题',
		dataIndex : 'topicName',
		align : 'right',
		width : 100
	}, {
		header : '分配系数',
		dataIndex : 'coefficient',
		align : 'right',
		width : 110,
		renderer : numberFormat
	}, {
		header : '显示顺序',
		dataIndex : 'displayNo',
		align : 'center',
		width : 110
	}]);
	var btnConfirm = new Ext.Button({
		text : '确定', 
		id:'topic_confirm',
		iconCls : 'confirm',
		handler : function() {
			if(grid.getSelectionModel().hasSelection()){
			var record = grid.getSelectionModel().getSelected();
			setValue(record.get("topicId"), record.get("topicName"));
			win.hide();
		}}
	});
	ds.load({
		params : {
			start : 0,
			limit : bbar.pageSize
		}
	});

//	ds.on("beforeload", function() {
//		Ext.apply(this.baseParams, {
//			queryKey : fuuzy.getValue()
//		});
//	});
	var btnCancel = new Ext.Button({
		text : '取消', 
		id:'topic_cancel',
		iconCls : 'cancer',
		handler : function() {
			setValue(null, null);
			win.hide();
		}
	});
	var tbar = new Ext.Toolbar({
		items : [
//		Power.component.unit.measure_unit_name, fuuzy,
//		new Ext.Button({
//			text : Constant.query,
//			iconCls : 'query',
//			handler : function() {
//				ds.load({
//					params : {
//						start : 0,
//						limit : bbar.pageSize
//					}
//				});
//			}
//		}), '-'
		 btnConfirm,btnCancel]
	});
	var grid = new Ext.grid.GridPanel({
		tbar : tbar,
		bbar : bbar,
		width : 400,
		height : 350,
		sm : sm,
		cm : cm,
		ds : ds,
//		listeners:{
//			'rowdblclick':function(grid, rowIndex, e){
//				Ext.get("topic_confirm").dom.click();
//			}
//		},
		viewConfig : {
			forceFit : true
		}
	});
	var win = new Ext.Window({
		closeAction : 'hide',
		title : '考核主题',
		modal : true,
		width : 500,
		height : 400,
		layout : 'fit',
		items : [grid]
	});
	var cbStore = new Ext.data.Store({
		reader : new Ext.data.JsonReader({}, topic)
	}); 
	var combo = new Ext.form.ComboBox({
		fieldLabel : '考核主题',
		store : cbStore,
		mode : 'local',
		hiddenName :'topicId',
		name : 'topicId',
		width: 180,
		valueField : 'topicId',
		displayField : 'topicName',
		editable : true,
		triggerAction : 'all',
		forceSelection:true,
		readOnly : true,
		onTriggerClick : function() {
			win.show();
		}
	});
	Ext.apply(combo, config);
	function setValue(topicId, topicName) {
		var d1 = new topic({
			topicId : topicId,
			topicName : topicName
		});
		cbStore.removeAll();
		cbStore.add(d1);
		combo.setValue(topicId);
	}
	function getValue() {
		if (grid.getSelectionModel().hasSelection()) {
			var record = grid.getSelectionModel().getSelected();
			if (typeof(record) != "undefined") {
				var ro = new Object();
				ro.topicId = record.get("topicId");
				ro.topicName = record.get("topicName");
				return ro;
			} else {
				return null;
			}
		}
	}
	return {
		ds : ds,
		grid : grid,
		win : win,
		btnConfirm : btnConfirm,
		btnCancel : btnCancel,
		combo : combo,
		setValue : setValue,
		getValue : getValue
	}
}