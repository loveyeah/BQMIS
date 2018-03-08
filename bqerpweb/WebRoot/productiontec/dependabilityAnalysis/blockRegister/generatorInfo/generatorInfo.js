Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var pBlockId = parent.pBlockId;
	// 机组id
	var blockId = new Ext.form.Hidden({
				id : 'blockId',
				name : 'pgInfo.blockId'
			})
	// 发电信息id
	var generatorId = new Ext.form.Hidden({
				id : 'generatorId',
				name : 'pgInfo.generatorId'
			})
	// 发电机型号
	var generatorType = new Ext.form.TextField({
				id : 'generatorType',
				name : 'pgInfo.generatorType',
				fieldLabel : '发电机型号',
				anchor : '100%'
			})
	// 额定容量(MW)
	var ratedCapacity = new Ext.form.NumberField({
				id : 'ratedCapacity',
				name : 'pgInfo.ratedCapacity',
				fieldLabel : '额定容量(MW)',
				decimalPrecision : 4,
				allowNegative : false,
				anchor : '100%'
			})

	// 最大出力(MW)
	var maximumOutput = new Ext.form.NumberField({
				id : 'maximumOutput',
				name : 'pgInfo.maximumOutput',
				fieldLabel : '最大出力(MW)',
				decimalPrecision : 4,
				allowNegative : false,
				anchor : '100%'
			})

	// 额定转速(rpm)
	var ratedSpeed = new Ext.form.NumberField({
				id : 'ratedSpeed',
				name : 'pgInfo.ratedSpeed',
				fieldLabel : '额定转速(rpm)',
				decimalPrecision : 4,
				allowNegative : false,
				anchor : '100%'
			})

	// 出口电压(KV)
	var exportVoltage = new Ext.form.NumberField({
				id : 'exportVoltage',
				name : 'pgInfo.exportVoltage',
				fieldLabel : '出口电压(KV)',
				decimalPrecision : 4,
				allowNegative : false,
				anchor : '100%'
			})

	// 定子额定电流(A)
	var statorRatedCurrent = new Ext.form.NumberField({
				id : 'statorRatedCurrent',
				name : 'pgInfo.statorRatedCurrent',
				fieldLabel : '定子额定电流(A)',
				decimalPrecision : 4,
				allowNegative : false,
				anchor : '100%'
			})

	// 转子额定电流(A)
	var ratedRotor = new Ext.form.NumberField({
				id : 'ratedRotor',
				name : 'pgInfo.ratedRotor',
				fieldLabel : '转子额定电流(A)',
				decimalPrecision : 4,
				allowNegative : false,
				anchor : '100%'
			})

	// 额定功率因素(A)
	var ratedPowerFactor = new Ext.form.NumberField({
				id : 'ratedPowerFactor',
				name : 'pgInfo.ratedPowerFactor',
				fieldLabel : '额定功率因素(A)',
				decimalPrecision : 4,
				allowNegative : false,
				anchor : '100%'
			})

	// 设计效率(%)
	var designEfficiency = new Ext.form.NumberField({
				id : 'designEfficiency',
				name : 'pgInfo.designEfficiency',
				fieldLabel : '设计效率(%)',
				decimalPrecision : 4,
				allowNegative : false,
				anchor : '100%'
			})

	// 冷却方式
	var coolingMethod = new Ext.form.TextField({
				id : 'coolingMethod',
				// store : [],
				name : 'pgInfo.coolingMethod',
				fieldLabel : '冷却方式',
				anchor : '100%'
			})

	// 出厂日期
	var manufactureDate = new Ext.form.DateField({
				id : 'manufactureDate',
				store : [],
				name : 'pgInfo.manufactureDate',
				fieldLabel : '出厂日期',
				anchor : '100%'
			})

	// 制造厂家编码
	var manufacturerCode = new Ext.form.TextField({
				id : 'manufacturerCode',
				name : 'pgInfo.manufacturerCode',
				fieldLabel : '制造厂家编码',
				anchor : '100%'
			})

	// 额定氢压(kPA)
	var ratedHydrogenPressure = new Ext.form.NumberField({
				id : 'ratedHydrogenPressure',
				name : 'pgInfo.ratedHydrogenPressure',
				fieldLabel : '额定氢压(kPA)',
				decimalPrecision : 4,
				allowNegative : false,
				anchor : '100%'
			})

	// 制造厂家名称
	var manufacturerName = new Ext.form.TextField({
				id : 'manufacturerName',
				name : 'pgInfo.manufacturerName',
				fieldLabel : '制造厂家名称',
				anchor : '100%'
			})

	var formPanel = new Ext.form.FormPanel({
				id : 'formPanel',
				autoScroll : true,
				width : "100%",
				frame : true,
				border : false,
				layout : 'column',
				labelAlign : 'right',
				labelWidth : 110,
				tbar : [{
							text : '保存',
							iconCls : 'save',
							handler : saveInfo
						}],
				items : [{
							layout : 'form',
							columnWidth : 1,
							border : false,
							items : [blockId, generatorId, generatorType]
						}, {
							layout : 'form',
							columnWidth : .5,
							border : false,
							items : [ratedCapacity, maximumOutput]

						}, {
							layout : 'form',
							columnWidth : .5,
							border : false,
							items : [ratedSpeed, exportVoltage]
						}, {
							layout : 'form',
							columnWidth : .5,
							border : false,
							items : [statorRatedCurrent, ratedRotor]
						}, {
							layout : 'form',
							columnWidth : .5,
							border : false,
							items : [ratedPowerFactor, designEfficiency]
						}, {
							layout : 'form',
							columnWidth : .5,
							border : false,
							items : [coolingMethod, manufactureDate]
						}, {
							layout : 'form',
							columnWidth : .5,
							border : false,
							items : [manufacturerCode, ratedHydrogenPressure]
						}, {
							layout : 'form',
							columnWidth : 1,
							border : false,
							items : [manufacturerName]
						}]
			})
	function saveInfo() {
		if (!formPanel.getForm().isValid()) {
			return false;
		}
		formPanel.getForm().submit({
					url : 'productionrec/saveGeneratorInfo.action',
					method : 'POST',
					success : function(form, action) {
						var o = eval('(' + action.response.responseText + ')');
						generatorId.setValue(o.generatorId);
						Ext.Msg.alert('提示', '保存成功');
					},
					failure : function() {
						Ext.Msg.alert('提示', '操作失败，请联系管理员！');
					}
				})
	}

	if (pBlockId != null) {
		blockId.setValue(pBlockId);
		Ext.Ajax.request({
					url : 'productionrec/getGeneratorInfo.action',
					method : 'post',
					params : {
						pBlockId : pBlockId
					},
					success : function(result, request) {
						str = result.responseText;
						var o = eval('(' + str.substring(1, str.length - 1)
								+ ')');
						generatorType.setValue(o.generatorType);
						generatorId.setValue(o.generatorId);
						blockId.setValue(o.blockId);
						ratedCapacity.setValue(o.ratedCapacity);
						maximumOutput.setValue(o.maximumOutput);
						ratedSpeed.setValue(o.ratedSpeed);
						exportVoltage.setValue(o.exportVoltage);
						statorRatedCurrent.setValue(o.statorRatedCurrent);
						ratedRotor.setValue(o.ratedRotor);
						ratedPowerFactor.setValue(o.ratedPowerFactor);
						designEfficiency.setValue(o.designEfficiency);
						coolingMethod.setValue(o.coolingMethod);
						manufactureDate.setValue(o.manufactureDate.substring(0,
								10));
						manufacturerCode.setValue(o.manufacturerCode);
						ratedHydrogenPressure.setValue(o.ratedHydrogenPressure);
						manufacturerName.setValue(o.manufacturerName);
					}
				})
	}
	var view = new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				border : false,
				frame : false,
				items : [{
							// layout : 'fit',
							border : false,
							frame : false,
							region : "center",
							autoScroll : true,
							items : [formPanel]
						}]
			});
})