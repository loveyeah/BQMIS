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
	
	// 主变压器ID 1
	var transformerId = new Ext.form.Hidden({
		id : 'transformerId',
		name : 'info.transformerId'
	})
	// 机组ID 2
	var blockId = new Ext.form.Hidden({
		id : 'blockId',
		value : pBlockId,
		name : 'info.blockId'
	})
	// 主变压器型号 3
	var transformerModel = new Ext.form.TextField({
		id : 'transformerModel',
		name : 'info.transformerModel',
		fieldLabel : '主变压器型号',
		width : 405
	})
	// 额定容量 4
	var ratedCapacity = new Ext.form.NumberField({
		id : 'ratedCapacity',
		name : 'info.ratedCapacity',
		fieldLabel : '额定容量（kVa）',
		allowNegative : false,
		decimalPrecision : 4
	})
	// 额定电压 5
	var ratedVoltage = new Ext.form.TextField({
		id : 'ratedVoltage',
		name : 'info.ratedVoltage',
		fieldLabel : '额定电压（kV）'
	})
	// 冷却方式 6
	var coolingMethod = new Ext.form.TextField({
		id : 'coolingMethod',
		name : 'info.coolingMethod',
		fieldLabel : '冷却方式'
	})
	
	// 线圈数 7
	var coilNumber = new Ext.form.NumberField({
		id : 'coilNumber',
		name : 'info.coilNumber',
		fieldLabel : '线圈数（匝）',
		allowNegative : false,
		decimalPrecision : 4
	})
	// 接线方式 8
	var connection = new Ext.form.TextField({
		id : 'connection',
		name : 'info.connection',
		fieldLabel : '接线方式'
	})
	// 出厂日期 9
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
	})
	
	// 制造厂家编码 10
	var manufacturerCode = new Ext.form.TextField({
		id : 'manufacturerCode',
		name : 'info.manufacturerCode',
		fieldLabel : '制造厂家编码'
	})
	// 制造厂家名称 11
	var manufacturerName = new Ext.form.TextField({
		id : 'manufacturerName',
		name : 'info.manufacturerName',
		fieldLabel : '制造厂家名称',
		width : 405
	})
	
	
	var panel = new Ext.form.FormPanel({
		id : 'pan',
		autoScroll : true,
		width : 700,
		frame : true,
		border : false,
		layout : 'column',
		labelAlign : 'right',
		labelWidth : 110,
		tbar : [{text : '保存',
					iconCls : 'save',
					handler : saveRec
				}],
		items : [{
			columnWidth : 1,
			frame : false,
			border : false,
			layout : 'form',
			items : [transformerId,blockId,transformerModel]
		},{
			columnWidth : .407,
			frame : false,
			border : false,
			layout : 'form',
			items : [ratedCapacity,coolingMethod,connection]
		},{
			columnWidth : .5,
			frame : false,
			border : false,
			layout : 'form',
			items : [ratedVoltage,coilNumber,manufactureDate]
		},{
			columnWidth : 1,
			frame : false,
			border : false,
			layout : 'form',
			items : [manufacturerCode]
		},{
			columnWidth : 1,
			frame : false,
			border : false,
			layout : 'form',
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
		if (transformerModel.getValue() == null || transformerModel.getValue() == '') {
			Ext.Msg.alert('提示', '主变压器型号不能为空，请输入！');
			return;
		}
		Ext.Msg.confirm('提示', '确认要保存数据吗？', function(id) {
			if (id == 'yes') {
				panel.getForm().submit({
							url : 'productionrec/saveTransformerInfoOrUpdate.action',
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
					url : 'productionrec/getTransformerInfoByBlockId.action',
					method : 'post',
					params : {
						blockId : blockId.getValue()
					},
					success : function(result, request) {
						var obj = eval('(' + result.responseText + ')');
						panel.getForm().loadRecord(obj);
						manufactureDate.setValue(obj.manufactureDate);
						blockId.setValue(pBlockId)
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