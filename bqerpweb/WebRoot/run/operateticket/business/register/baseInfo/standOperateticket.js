// 设置树的点击事件
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() { 
	var standOp = new Ext.data.Record.create([{
		name : 'opticketCode'
	}, {
		name : 'operateTaskName'
	}, {
		name : 'specialityCode'
	}]);
	var standOpDs = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'opticket/getStantdOpticktetList.action',
			method : 'post'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalCount',
			root : 'list'
		}, standOp)
	});

	var standOpSm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false,
		listeners : {
			rowselect : function(sm, row, rec) {
			}
		}
	});
	var storeOpSpecial = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'opticket/getOpticketSpeciality.action'
		}),
		reader : new Ext.data.JsonReader({
			root : "list"
		}, [{
			name : 'specialityCode'
		}, {
			name : 'specialityName'
		}])
	});
	storeOpSpecial.on('render', function() {

	})
	storeOpSpecial.on('load', function(e, records) {
		opticketSpecialCbo.setValue(records[0].data.specialityCode);
	});
	storeOpSpecial.load();
	var opticketSpecialCbo = new Ext.form.ComboBox({
		fieldLabel : '专业',
		id : "opticketSpecialCbo",
		store : storeOpSpecial,
		width : 100,
		triggerAction : 'all',
		valueField : "specialityCode",
		displayField : "specialityName",
		mode : 'local',
		selectOnFocus : true,
		readOnly : true
	});
	var optiketTypeCbo = new Ext.form.ComboBox({
		fieldLabel : '类别',
		store : new Ext.data.SimpleStore({
			fields : ["returnValue", "displayText"],
			data : [["", '所有'], ['00', '电气操作票'], ['01', '热机操作票']]
		}),
		valueField : "returnValue",
		displayField : "displayText",
		mode : 'local',
		forceSelection : true,
		// blankText : '所有',
		// emptyText : '所有',
		value : '',
		hiddenName : 'optiketType',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		allowBlank : false,
		name : 'optiketType'
	});
	var standOpCm = new Ext.grid.ColumnModel([standOpSm, {
		id : 'opticketCode',
		header : '操作票编号',
		dataIndex : 'opticketCode',
		sortable : true
	}, {
		header : '操作任务名称',
		dataIndex : 'operateTaskName',
		width : 400
	}, {
		header : '专业',
		dataIndex : 'specialityCode',
		width : 200
	}]);
	// 排序
	standOpCm.defaultSortable = true;
	standOpDs.load({
		params : {
			start : 0,
			limit : 18,
			opticketType : optiketTypeCbo.getValue(),
			specialityCode : opticketSpecialCbo.getValue(),
			opStatus : OpStatus.EngineerReady
		}
	});
	var tbar = new Ext.Toolbar({
		items : ['类别', '-', optiketTypeCbo, '专业', '-', opticketSpecialCbo, {
			id : 'query',
			text : "查询",
			handler : function() {
				Ext.Ajax.request({
					url : 'opticket/getStantdOpticktetList.action',
					method : 'post',
					params : {
						start : 0,
						limit : 18,
						opticketType : optiketTypeCbo.getValue(),
						specialityCode : opticketSpecialCbo.getValue(),
						opStatus : OpStatus.EngineerReady
					},
					success : function(result, request) {
						var gridData = eval('(' + result.responseText + ')');
						standOpDs.loadData(gridData);
					},
					failure : function(result, request) {
						Ext.Msg.alert('失败', '请联系管理员！');
					}
				})
			}
		}]
	});
	var bbar = new Ext.PagingToolbar({
		pageSize : 18,
		store : standOpDs,
		displayInfo : true,
		displayMsg : '共 {2} 条',
		emptyMsg : "没有记录"
	});
	var grid = new Ext.grid.GridPanel({
		id : 'grid',
		ds : standOpDs,
		cm : standOpCm,
		sm : standOpSm,
		bbar : bbar,
		tbar : tbar,
		border : false,
		autoWidth : true,
		// autoHeight : true,
		fitToFrame : true
	});
	standOpDs.on('beforeload', function() {
		Ext.apply(this.baseParams, {
			opticketType : optiketTypeCbo.getValue(),
			specialityCode : opticketSpecialCbo.getValue()
		});
	});
	grid.on('rowdblclick', function(grid, rowIndex, e) {
		var rec = grid.getSelectionModel().getSelected();
		var opCode = rec.get('opticketCode');
		window.returnValue = opCode;
		window.close();
			// Ext.Ajax.request({
			// url : 'opticket/getDetailOpticketByCode.action',
			// method : 'post',
			// params : {
			// opticketCode : opCode
			// },
			// success : function(result, request) {
			// var record = eval('(' + result.responseText + ')');
			// var args = new Object();
			// args.opticketCode = record.get("opticketCode");
			// args.isStandar = record.get("isStandar");
			// args.opticketType = record.get("opticketType");
			// args.operateTaskId = record.get("operateTaskId");
			// args.operateTaskName = record.get("operateTaskName");
			// args.specialityCode = record.get("specialityCode");
			// args.memo = record.get("memo");
			// args.isSingle = record.get("isSingle");
			// window.returnValue = args;
			// window.close();
			//
			// },
			// failure : function(result, request) {
			// Ext.Msg.alert('提示信息', '操作失败！')
			// }
			// })
		});
	var viewport = new Ext.Viewport({
		layout : 'fit',
		autoWidth : true,
		autoHeight : true,
		fitToFrame : true,
		items : [grid]
	});
});