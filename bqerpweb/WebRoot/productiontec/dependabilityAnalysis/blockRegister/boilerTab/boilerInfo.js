Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	
	pBlockId = parent.pBlockId;
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
	
	// 锅炉ID 1
	var boilerId = new Ext.form.Hidden({
		id : 'boilerId',
		name : 'info.boilerId'
	})
	// 机组ID 2
	var blockId = new Ext.form.Hidden({
		id : 'blockId',
		value : pBlockId,
		name : 'info.blockId'
	})
	// 锅炉型号 3
	var boilerModel = new Ext.form.TextField({
		id : 'boilerModel',
		name : 'info.boilerModel',
		fieldLabel : '锅炉型号',
		width : 401
	})
	// 额定蒸发量 4
	var evaporation = new Ext.form.NumberField({
		id : 'evaporation',
		name : 'info.evaporation',
		fieldLabel : '额定蒸发量（t/h）',
		allowNegative : false,
		decimalPrecision : 4
	})
	// 锅炉型式 5
	var boilerType = new Ext.form.TextField({
		id : 'boilerType',
		name : 'info.boilerType',
		fieldLabel : '锅炉型式'
	})
	// 炉膛结构 6
	var furnaceStructure = new Ext.form.TextField({
		id : 'furnaceStructure',
		name : 'info.furnaceStructure',
		fieldLabel : '炉膛结构'
	})
	// 燃用燃料 7
	var fuelName = new Ext.form.TextField({
		id : 'fuelName',
		name : 'info.fuelName',
		fieldLabel : '燃用燃料'
	})
	// 燃烧方式 8
	var combustionMethod = new Ext.form.TextField({
		id : 'combustionMethod',
		name : 'info.combustionMethod',
		fieldLabel : '燃烧方式'
	})
	// 排渣方式 9
	var slagMethod = new Ext.form.TextField({
		id : 'slagMethod',
		name : 'info.slagMethod',
		fieldLabel : '排渣方式'
	})
	// 工质流动方式 10
	var flowMethod = new Ext.form.TextField({
		id : 'flowMethod',
		name : 'info.flowMethod',
		fieldLabel : '工质流动方式'
	})
	// 空气预热器型式 11
	var airPreheaterType = new Ext.form.TextField({
		id : 'airPreheaterType',
		name : 'info.airPreheaterType',
		fieldLabel : '空气预热器型式'
	})
	// 设计效率 12
	var efficiency = new Ext.form.NumberField({
		id : 'efficiency',
		name : 'info.efficiency',
		fieldLabel : '设计效率（%）',
		allowNegative : false,
		decimalPrecision : 4
	})
	// 出厂日期 13
	var manufactureDate = new Ext.form.TextField({
				fieldLabel : "出厂日期",
				id : 'manufactureDate',
				readOnly : true,
//				width : 100,
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
	// 主蒸汽温度 14
	var mainsteamTemperature = new Ext.form.NumberField({
		id : 'mainsteamTemperature',
		name : 'info.mainsteamTemperature',
		fieldLabel : '主蒸汽温度（°C）',
		allowNegative : false,
		decimalPrecision : 4
	})
	// 主蒸汽压力 15
	var mainsteamPressure = new Ext.form.NumberField({
		id : 'mainsteamPressure',
		name : 'info.mainsteamPressure',
		fieldLabel : '主蒸汽压力（MPa）',
		allowNegative : false,
		decimalPrecision : 4
	})
	// 再蒸汽流量 16
	var resteamFlow = new Ext.form.NumberField({
		id : 'resteamFlow',
		name : 'info.resteamFlow',
		fieldLabel : '流量（t/h）',
		allowNegative : false,
		decimalPrecision : 4,
		hideLabel : true,
		labelSeparater : ''
	})
	// 再蒸汽入口温度 17
	var resteamInTemperature = new Ext.form.NumberField({
		id : 'resteamInTemperature',
		name : 'info.resteamInTemperature',
		fieldLabel : '入口温度（°C）',
		allowNegative : false,
		decimalPrecision : 4,
		hideLabel : true,
		labelSeparater : ''
	})
	// 再蒸汽入口压力 18
	var resteamInPressure = new Ext.form.NumberField({
		id : 'resteamInPressure',
		name : 'info.resteamInPressure',
		fieldLabel : '入口压力（MPa）',
		allowNegative : false,
		decimalPrecision : 4,
		hideLabel : true,
		labelSeparater : ''
	})
	// 再蒸汽出口温度 19
	var resteamOutTemperature = new Ext.form.NumberField({
		id : 'resteamOutTemperature',
		name : 'info.resteamOutTemperature',
		fieldLabel : '出口温度（°C）',
		allowNegative : false,
		decimalPrecision : 4,
		hideLabel : true,
		labelSeparater : ''
	})
	// 再蒸汽出口压力 20
	var resteamOutPressure = new Ext.form.NumberField({
		id : 'resteamOutPressure',
		name : 'info.resteamOutPressure',
		fieldLabel : '出口压力（MPa）',
		allowNegative : false,
		decimalPrecision : 4,
		hideLabel : true,
		labelSeparater : ''
	})
	// 再蒸汽 fieldSet
	var set1 = new Ext.form.FieldSet({
		id : 'set1',
		frame : true,
		border : true,
		layout : 'column',
		title : '再蒸汽',
		draggable : false,
		collapsible : true,
		items : [{
			columnWidth : .2,
			frame : false,
			border : false,
			layout : 'form',
			items : [new Ext.form.Label({
				text : '流量（t/h）:',
				style : "padding-top:0px;padding-right:0px;padding-bottom:0px;margin-bottom:0px"
			}),resteamFlow]
		},{
			columnWidth : .2,
			frame : false,
			border : false,
			layout : 'form',
			items : [new Ext.form.Label({
				text : '入口温度（°C）:'
			}),resteamInTemperature]
		},{
			columnWidth : .2,
			frame : false,
			border : false,
			layout : 'form',
			items : [new Ext.form.Label({
				text : '入口压力（MPa）:'
			}),resteamInPressure]
		},{
			columnWidth : .2,
			frame : false,
			border : false,
			layout : 'form',
			items : [new Ext.form.Label({
				text : '出口温度（°C）:'
			}),resteamOutTemperature]
		},{
			columnWidth : .2,
			frame : false,
			border : false,
			layout : 'form',
			items : [new Ext.form.Label({
				text : '出口压力（MPa）:'
			}),resteamOutPressure]
		}]
	})
	
	// 给水温度 21
	var waterTemperature = new Ext.form.NumberField({
		id : 'waterTemperature',
		name : 'info.waterTemperature',
		fieldLabel : '给水温度（°C）',
		allowNegative : false,
		decimalPrecision : 4
	})
	//排烟温度 22
	var smokeTemperature = new Ext.form.NumberField({
		id : 'smokeTemperature',
		name : 'info.smokeTemperature',
		fieldLabel : '排烟温度（°C）',
		allowNegative : false,
		decimalPrecision : 4
	})
	//热风温度 23
	var windTemperature = new Ext.form.NumberField({
		id : 'windTemperature',
		name : 'info.windTemperature',
		fieldLabel : '热风温度（°C）',
		allowNegative : false,
		decimalPrecision : 4
	})
	// 制造厂家编码 24
	var manufacturerCode = new Ext.form.TextField({
		id : 'manufacturerCode',
		name : 'info.manufacturerCode',
		fieldLabel : '制造厂家编码'
	})
	// 制造厂家名称 25
	var manufacturerName = new Ext.form.TextField({
		id : 'manufacturerName',
		name : 'info.manufacturerName',
		fieldLabel : '制造厂家名称',
		width : 391
	})
	// 设计煤质Q 26
	var designCoalQ = new Ext.form.TextField({
		id : 'designCoalQ',
		name : 'info.designCoalQ',
		fieldLabel : '设计煤质Q',
		hideLabel : true,
		labelSeparater : '',
		width : 80
	})
	// 设计煤质V 27
	var designCoalV = new Ext.form.TextField({
		id : 'designCoalV',
		name : 'info.designCoalV',
		fieldLabel : '设计煤质V',
		hideLabel : true,
		labelSeparater : '',
		width : 80
	})
	// 设计煤质M 28
	var designCoalM = new Ext.form.TextField({
		id : 'designCoalM',
		name : 'info.designCoalM',
		fieldLabel : '设计煤质M',
		hideLabel : true,
		labelSeparater : '',
		width : 80
	})
	// 设计煤质A 29
	var designCoalA = new Ext.form.TextField({
		id : 'designCoalA',
		name : 'info.designCoalA',
		fieldLabel : '设计煤质A',
		hideLabel : true,
		labelSeparater : '',
		width : 80
	})
	// 设计煤质S 30
	var designCoalS = new Ext.form.TextField({
		id : 'designCoalS',
		name : 'info.designCoalS',
		fieldLabel : '设计煤质S',
		hideLabel : true,
		labelSeparater : '',
		width : 80
	})
	// 设计煤质T1 31
	var designCoalT1 = new Ext.form.TextField({
		id : 'designCoalT1',
		name : 'info.designCoalT1',
		fieldLabel : '设计煤质T1',
		hideLabel : true,
		labelSeparater : '',
		width : 80
	})
	// 设计煤质T3 32
	var designCoalT3 = new Ext.form.TextField({
		id : 'designCoalT3',
		name : 'info.designCoalT3',
		fieldLabel : '设计煤质T3',
		hideLabel : true,
		labelSeparater : '',
		width : 80
	})
	// 设计煤质 fieldSet
	var set2 = new Ext.form.FieldSet({
		id : 'set2',
		frame : true,
		border : true,
		layout : 'column',
		title : '设计煤质',
		draggable : false,
		collapsible : true,
		items : [{
			columnWidth : .14,
			frame : false,
			border : false,
			layout : 'form',
			items : [new Ext.form.Label({
				text : 'Q'
			}),designCoalQ]
		},{
			columnWidth : .14,
			frame : false,
			border : false,
			layout : 'form',
			items : [new Ext.form.Label({
				text : 'V'
			}),designCoalV]
		},{
			columnWidth : .14,
			frame : false,
			border : false,
			layout : 'form',
			items : [new Ext.form.Label({
				text : 'M'
			}),designCoalM]
		},{
			columnWidth : .14,
			frame : false,
			border : false,
			layout : 'form',
			items : [new Ext.form.Label({
				text : 'A'
			}),designCoalA]
		},{
			columnWidth : .14,
			frame : false,
			border : false,
			layout : 'form',
			items : [new Ext.form.Label({
				text : 'S'
			}),designCoalS]
		},{
			columnWidth : .14,
			frame : false,
			border : false,
			layout : 'form',
			items : [new Ext.form.Label({
				text : 'T1'
			}),designCoalT1]
		},{
			columnWidth : .14,
			frame : false,
			border : false,
			layout : 'form',
			items : [new Ext.form.Label({
				text : 'T3'
			}),designCoalT3]
		}]
	})
	
	var panel = new Ext.form.FormPanel({
		id : 'pan',
		autoScroll : true,
		width : 900,
		frame : true,
		border : false,
		layout : 'column',
		labelAlign : 'right',
		tbar : [{text : '保存',
					iconCls : 'save',
					handler : saveRec
				}],
		items : [{
			columnWidth : .59,
			frame : false,
			border : false,
			layout : 'form',
			labelWidth : 110,
			items : [boilerId,blockId,boilerModel]
		},{
			columnWidth : .3,
			frame : false,
			border : false,
			layout : 'form',
			labelWidth : 110,
			items : [evaporation]
		},{
			columnWidth : .3,
			frame : false,
			border : false,
			layout : 'form',
			labelWidth : 110,
			items : [boilerType,combustionMethod,airPreheaterType,mainsteamTemperature]
		},{
			columnWidth : .3,
			frame : false,
			border : false,
			layout : 'form',
			labelWidth : 120,
			items : [furnaceStructure,slagMethod,efficiency,mainsteamPressure]
		},{
			columnWidth : .3,
			frame : false,
			border : false,
			layout : 'form',
			items : [fuelName,flowMethod,manufactureDate]
		},{
			columnWidth : 1,
			frame : false,
			border : false,
			layout : 'form',
			items : [set1]
		},{
			columnWidth : .3,
			frame : false,
			border : false,
			layout : 'form',
			items : [waterTemperature]
		},{
			columnWidth : .3,
			frame : false,
			border : false,
			layout : 'form',
			items : [smokeTemperature]
		},{
			columnWidth : .3,
			frame : false,
			border : false,
			layout : 'form',
			items : [windTemperature]
		},{
			columnWidth : .3,
			frame : false,
			border : false,
			layout : 'form',
			items : [manufacturerCode]
		},{
			columnWidth : .6,
			frame : false,
			border : false,
			layout : 'form',
			items : [manufacturerName]
		},{
			columnWidth : 1,
			frame : false,
			border : false,
			layout : 'form',
			items : [set2]
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
		if (boilerModel.getValue() == null || boilerModel.getValue() == '') {
			Ext.Msg.alert('提示', '锅炉型号不能为空，请输入！');
			return;
		}
		Ext.Msg.confirm('提示', '确认要保存数据吗？', function(id) {
			if (id == 'yes') {
				panel.getForm().submit({
							url : 'productionrec/saveBoilderInfoOrUpdate.action',
							method : 'post',
							params : {
								manufactureDate : manufactureDate.getValue()
							},
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
	
	function initForm() {
		//		alert(pBlockId)		
		Ext.Ajax.request({
					url : 'productionrec/getBoilerInfoByBlockId.action',
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