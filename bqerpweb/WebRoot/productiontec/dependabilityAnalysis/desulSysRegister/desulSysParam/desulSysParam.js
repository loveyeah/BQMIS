Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	
	flagId = parent.flagId;
	function getDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDay();
		s += (t > 9 ? "" : "0") + t;
		return s;
	}
	
	// 脱硫系统ID 1
	var desulfurizationId = new Ext.form.Hidden({
		id : 'desulfurizationId',
		name : 'param.desulfurizationId'
	})
	// 辅机ID 2
	var auxiliaryId = new Ext.form.Hidden({
		id : 'auxiliaryId',
		value : flagId,
		name : 'param.auxiliaryId'
	})
	// 脱硫系统类型3
	var desulfurizeType = new Ext.form.TextField({
		id : 'desulfurizeType',
		name : 'param.desulfurizeType',
		fieldLabel : '脱硫系统类型',
		allowNegative : false
	})
	// SO2脱除率 4
	var so2Rate = new Ext.form.NumberField({
		id : 'so2Rate',
		name : 'param.so2Rate',
		fieldLabel : 'SO2脱除率（%）',
		allowNegative : false
	})
	// 石灰石耗量 5
	var limestoneWeight = new Ext.form.NumberField({
		id : 'limestoneWeight',
		name : 'param.limestoneWeight',
		fieldLabel : '石灰石耗量（kg/h）',
		allowNegative : false
	})
	// 电能消耗量 6
	var electricityCapacity = new Ext.form.NumberField({
		id : 'electricityCapacity',
		name : 'param.electricityCapacity',
		fieldLabel : '电能消耗量（kw）',
		allowNegative : false
	})
	// 热量消耗 7
	var heatConsume = new Ext.form.NumberField({
		id : 'heatConsume',
		name : 'param.heatConsume',
		fieldLabel : '热量消耗（kJ/s）',
		allowNegative : false
	})
	// 硫钙比 8
	var sulfurAutuniteRate = new Ext.form.NumberField({
		id : 'sulfurAutuniteRate',
		name : 'param.sulfurAutuniteRate',
		fieldLabel : '硫钙比（%）',
		allowNegative : false
	})
	// 液气比 9
	var liquidGasRate = new Ext.form.NumberField({
		id : 'liquidGasRate',
		name : 'param.liquidGasRate',
		fieldLabel : '液气比（%）',
		allowNegative : false
	})
	// 进口烟气温度 10
	var importAirTpr = new Ext.form.NumberField({
		id : 'importAirTpr',
		name : 'param.importAirTpr',
		fieldLabel : '进口烟气温度（°C）',
		allowNegative : false
	})
	// 出口烟气温度 11
	var exporttAirTpr = new Ext.form.NumberField({
		id : 'exporttAirTpr',
		name : 'param.exporttAirTpr',
		fieldLabel : '出口烟气温度（°C）',
		allowNegative : false
	})
	// 进口烟尘浓度 12
	var importDustThickness = new Ext.form.NumberField({
		id : 'importDustThickness',
		name : 'param.importDustThickness',
		fieldLabel : '进口烟尘浓度（g/㎥）',
		allowNegative : false
	})
	// 出口烟尘浓度 13
	var exporttDustThickness = new Ext.form.NumberField({
		id : 'exporttDustThickness',
		name : 'param.exporttDustThickness',
		fieldLabel : '出口烟尘浓度（g/㎥）',
		allowNegative : false
	})
	// 增压风机电动机功率 14
	var electPower = new Ext.form.NumberField({
		id : 'electPower',
		name : 'param.electPower',
		fieldLabel : '增压风机电动机功率（kw）',
		allowNegative : false
	})
	// 工艺水量 15
	var craftworkWater = new Ext.form.NumberField({
		id : 'craftworkWater',
		name : 'param.craftworkWater',
		fieldLabel : '工艺水量（㎥/h）',
		allowNegative : false
	})
	// 废水量 16
	var wasteWater = new Ext.form.NumberField({
		id : 'wasteWater',
		name : 'param.wasteWater',
		fieldLabel : '废水量（㎥/h）',
		allowNegative : false
	})
		// 原编码 17
	var oldCode = new Ext.form.TextField({
		id : 'oldCode',
		name : 'param.oldCode',
		fieldLabel : '原编码'
	})
	// 备注 18
	var memo = new Ext.form.TextField({
		id : 'memo',
		name : 'param.memo',
		fieldLabel : '备注'
	})
	
	
	var set1 = new Ext.form.FieldSet({
		id : 'set1',
		frame : true,
		border : true,
		layout : 'column',
		title : '参数',
		draggable : false,
		collapsible : true,
		items : [{
			columnWidth : .5,
			frame : false,
			border : false,
			layout : 'form',
			labelWidth : 170,
			items : [desulfurizationId,auxiliaryId,desulfurizeType,
			so2Rate,electricityCapacity,sulfurAutuniteRate,
				importAirTpr,importDustThickness,electPower,wasteWater]
		},{
			columnWidth : .5,
			frame : false,
			border : false,
			layout : 'form',
			labelWidth : 130,
			items : [oldCode,limestoneWeight,heatConsume,liquidGasRate,
			exporttAirTpr,exporttDustThickness,craftworkWater,memo]
		}]
	})
	
	
	var panel = new Ext.form.FormPanel({
		id : 'pan',
		autoScroll : true,
		width : 700,
		frame : true,
		border : false,
		layout : 'column',
		labelAlign : 'right',
		tbar : [{text : '保存',
					iconCls : 'save',
					handler : saveRec
				},'-',{
					text : '刷新',
					iconCls : 'reflesh',
					handler : refresh
				}],
		items : [{
			columnWidth : 1,
			frame : false,
			border : false,
			layout : 'form',
			items : [set1]
		}]
	})
	
	var view = new Ext.Viewport({
		id : 'view',
		enableTabScroll : true,
				layout : "border",
				border : false,
				frame : false,				
				items : [{
//							layout : 'fit',
							border : false,
							frame : false,
							region : "center",
							autoScroll : true,
							items : [panel]
						}]
	})
	
	function saveRec() {
		if (desulfurizeType.getValue() == null || desulfurizeType.getValue() == '') {
			Ext.Msg.alert('提示', '脱硫系统类型不能为空，请输入！');
			return;
		}
		Ext.Msg.confirm('提示', '确认要保存数据吗？', function(id) {
			if (id == 'yes') {
				panel.getForm().submit({
							url : 'productionrec/saveDesulfurizationParam.action',
							method : 'post',
							success : function(result, request) {
								Ext.Msg.alert('提示', '数据保存成功！');
								initForm()
							},
							failure : function(result, request) {
								Ext.Msg.alert('提示', '数据保存失败！');
							}
						})
			}
		})
	}
	function refresh()
	{
		view.render();
	}
	function initForm() {
		Ext.Ajax.request({
					url : 'productionrec/getDesulfurizationParamByAuxId.action',
					method : 'post',
					params : {
						auxiliaryId : flagId
					},
					success : function(result, request) {
						var obj = eval('(' + result.responseText + ')');
						panel.getForm().loadRecord(obj);
						auxiliaryId.setValue(flagId)
					},
					failure : function(result, request) {
						panel.getForm().reset();
					}
				})
	}
	if (flagId == null) {
		panel.hide();
		return;
	}
	else {
		panel.show();
		initForm();
	}
})