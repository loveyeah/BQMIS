Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	
	pBlockId = parent.pBlockId;
	function getDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate();
		s += (t > 9 ? "" : "0") + t;
		return s;
	}
	
	// 汽轮机ID 1
	var turbineId = new Ext.form.Hidden({
		id : 'turbineId',
		name : 'info.turbineId'
	})
	// 机组ID 2
	var blockId = new Ext.form.Hidden({
		id : 'blockId',
		value : pBlockId,
		name : 'info.blockId'
	})
	// 汽轮机型号 3
	var turbineModel = new Ext.form.TextField({
		id : 'turbineModel',
		name : 'info.turbineModel',
		fieldLabel : '汽轮机型号',
		width : 400
	})
	// 汽轮机型式 4
	var turbineType = new Ext.form.TextField({
		id : 'turbineType',
		name : 'info.turbineType',
		fieldLabel : '汽轮机型式',
		width : 200
	})
	// 额定功率 5
	var rated = new Ext.form.NumberField({
		id : 'rated',
		name : 'info.rated',
		fieldLabel : '额定功率（MW）',
		allowNegative : false,
		decimalPrecision : 4,
		width : 200
	})
	// 最大连续功率 6
	var maximumContinuousPower = new Ext.form.NumberField({
		id : 'maximumContinuousPower',
		name : 'info.maximumContinuousPower',
		fieldLabel : '最大连续功率（MW）',
		allowNegative : false,
		decimalPrecision : 2,
		width : 200
	})
	// 额定转速 7
	var ratedSpeed = new Ext.form.NumberField({
		id : 'ratedSpeed',
		name : 'info.ratedSpeed',
		fieldLabel : '额定转速（rpm）',
		allowNegative : false,
		decimalPrecision : 2,
		width : 200
	})
	// 主蒸汽压力 8
	var mainsteamPressure = new Ext.form.NumberField({
		id : 'mainsteamPressure',
		name : 'info.mainsteamPressure',
		fieldLabel : '主蒸汽压力（MPa）',
		allowNegative : false,
		decimalPrecision : 2,
		width : 200
	})
	// 主蒸汽温度 9
	var mainsteamTemperature = new Ext.form.NumberField({
		id : 'mainsteamTemperature',
		name : 'info.mainsteamTemperature',
		fieldLabel : '主蒸汽温度（°C）',
		allowNegative : false,
		decimalPrecision : 2,
		width : 200
	})
	// 再蒸汽压力 10
	var resteamPressure = new Ext.form.NumberField({
		id : 'resteamPressure',
		name : 'info.resteamPressure',
		fieldLabel : '再蒸汽压力（MPa）',
		allowNegative : false,
		decimalPrecision : 2,
		width : 200
	})
	// 再蒸汽温度 11
	var resteamTemperature = new Ext.form.NumberField({
		id : 'resteamTemperature',
		name : 'info.resteamTemperature',
		fieldLabel : '再蒸汽温度（°C）',
		allowNegative : false,
		decimalPrecision : 2,
		width : 200
	})
	// 排气压力 12
	var exhaustPressure = new Ext.form.NumberField({
		id : 'exhaustPressure',
		name : 'info.exhaustPressure',
		fieldLabel : '排气压力（MPa）',
		allowNegative : false,
		decimalPrecision : 2,
		width : 200
	})
	// 循环水进口温度 13
	var waterInletTemperature = new Ext.form.NumberField({
		id : 'waterInletTemperature',
		name : 'info.waterInletTemperature',
		fieldLabel : '循环水进口温度（°C）',
		allowNegative : false,
		decimalPrecision : 2,
		width : 200
	})
	// 设计热耗 14
	var heatConsumption = new Ext.form.NumberField({
		id : 'heatConsumption',
		name : 'info.heatConsumption',
		fieldLabel : '设计热耗（kj/kW.h）',
		allowNegative : false,
		decimalPrecision : 2,
		width : 200
	})
	// 出厂日期 15
	var manufactureDate = new Ext.form.TextField({
				fieldLabel : "出厂日期",
				id : 'manufactureDate',
				readOnly : true,
				width : 200,
				name : 'manufactureDate',
				value : getDate(),
				listeners : {
					focus : function() {
						WdatePicker({
									alwaysUseStartDate : true,
									dateFmt : "yyyy-MM-dd"
								});
						this.blur();
					}
				}
			});
	// 制造厂家编码 24
	var manufacturerCode = new Ext.form.TextField({
		id : 'manufacturerCode',
		name : 'info.manufacturerCode',
		fieldLabel : '制造厂家编码',
		width : 200
	})
	// 制造厂家名称 25
	var manufacturerName = new Ext.form.TextField({
		id : 'manufacturerName',
		name : 'info.manufacturerName',
		fieldLabel : '制造厂家名称',
		width : 400
	})
	
	var panel = new Ext.form.FormPanel({
		id : 'pan',
		autoScroll : true,
		width : 900,
		frame : true,
		border : false,
		layout : "column",
		labelAlign : 'right',
		tbar : [{text : '保存',
					iconCls : 'save',
					handler : saveRec
				}],
		items : [{
			columnWidth : 1,
			frame : false,
			border : false,
			layout : 'form'
		},{
			columnWidth : 1,
			frame : false,
			border : false,
			layout : 'form',
			labelWidth : 240,
			items : [turbineId,blockId,turbineModel]
		},{
			columnWidth : 1,
			frame : false,
			border : false,
			layout : 'form',
			labelWidth : 240,
			items : [turbineType,rated]
		},{
			columnWidth : 1,
			frame : false,
			border : false,
			layout : 'form',
			labelWidth : 240,
			items : [maximumContinuousPower,ratedSpeed,mainsteamPressure]
		},{
			columnWidth : 1,
			frame : false,
			border : false,
			layout : 'form',
			labelWidth : 240,
			items : [mainsteamTemperature,resteamPressure,resteamTemperature,exhaustPressure]
		},{
			columnWidth : 1,
			frame : false,
			border : false,
			layout : 'form',
			labelWidth : 240,
			items : [waterInletTemperature,heatConsumption,manufactureDate]
		},{
			columnWidth : 1,
			frame : false,
			border : false,
			layout : 'form',
			labelWidth : 240,
			items : [manufacturerCode]
		},{
			columnWidth : 1,
			frame : false,
			border : false,
			layout : 'form',
			labelWidth : 240,
			items : [manufacturerName]
		}]
	})
	
	var view = new Ext.Viewport({
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
		if (turbineModel.getValue() == null || turbineModel.getValue() == '') {
			Ext.Msg.alert('提示', '汽轮机型号不能为空，请输入！');
			return;
		}
		Ext.Msg.confirm('提示', '确认要保存数据吗？', function(id) {
			if (id == 'yes') {
				panel.getForm().submit({
							url : 'productionrec/saveTurbineInfoOrUpdate.action',
							method : 'post',
							params : {
								manufactureDate : manufactureDate.getValue()
							},
							success : function(result, request) {
								Ext.Msg.alert('提示', '数据保存成功！');
							},
							failure : function(result, request) {
								Ext.Msg.alert('提示', '数据保存失败！');
							}
						})
			}
		})

	}
	
	function initForm() {
		//		alert(pBlockId)		
		Ext.Ajax.request({
					url : 'productionrec/getTurbineInfoByBlockId.action',
					method : 'post',
					params : {
						blockId : blockId.getValue()
					},
					success : function(result, request) {
						var obj = eval('(' + result.responseText + ')');
						panel.getForm().loadRecord(obj);
						manufactureDate.setValue(obj.manufactureDate);
						blockId.setValue(pBlockId);
					},
					failure : function(result, request) {
						panel.getForm().reset();
					}
				})
	}
	if (pBlockId == null) {
		panel.hide();
		return;
	}

	else {
		panel.show();
		initForm();
	}
	
})