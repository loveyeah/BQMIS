Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	flagId = parent.flagId;

	var auxiliaryInfo = getParameter("auxiliaryInfo");

	// ------------定义form---------------

	var precipitationId = new Ext.form.Hidden({
		id : 'precipitationId',
		name : 'para.precipitationId',
		anchor : "70%"
	});

	var auxiliaryId = new Ext.form.Hidden({
		id : 'auxiliaryId',
		value : flagId,
		name : 'para.auxiliaryId'
	});

	var precipitationType = new Ext.form.TextField({
		id : 'precipitationType',
		fieldLabel : '类型',
		name : 'para.precipitationType',
		anchor : "70%"
	});

	var clearDustEffi = new Ext.form.NumberField({
		id : 'clearDustEffi',
		fieldLabel : '除尘效率',
		name : 'para.clearDustEffi',
		anchor : "70%"
	});

	var dustArea = new Ext.form.NumberField({
		id : 'dustArea',
		fieldLabel : '有效集尘面积',
		name : 'para.dustArea',
		anchor : "70%"
	});

	var importAirTpr = new Ext.form.NumberField({
		id : 'importAirTpr',
		fieldLabel : '入口烟气温度',
		name : 'para.importAirTpr',
		anchor : "70%"
	});

	var importAirThickness = new Ext.form.NumberField({
		id : 'importAirThickness',
		fieldLabel : '入口粉尘浓度',
		name : 'para.importAirThickness',
		anchor : "70%"
	});

	var windLeakRate = new Ext.form.NumberField({
		id : 'windLeakRate',
		fieldLabel : '漏风率',
		name : 'para.windLeakRate',
		anchor : "70%"
	});

	var dealAirQuantity = new Ext.form.NumberField({
		id : 'dealAirQuantity',
		fieldLabel : '处理烟气量',
		name : 'para.dealAirQuantity',
		anchor : "70%"
	});

	var maxNegativePressure = new Ext.form.NumberField({
		id : 'maxNegativePressure',
		fieldLabel : '最大负压',
		name : 'para.maxNegativePressure',
		anchor : "70%"
	});

	var resistanceLoss = new Ext.form.NumberField({
		id : 'resistanceLoss',
		fieldLabel : '阻力损失',
		name : 'para.resistanceLoss',
		anchor : "70%"
	});

	var electricFieldNum = new Ext.form.NumberField({
		id : 'electricFieldNum',
		fieldLabel : '电场个数',
		name : 'para.electricFieldNum',
		anchor : "70%"
	});

	var roomNum = new Ext.form.NumberField({
		id : 'roomNum',
		fieldLabel : '室数',
		name : 'para.roomNum',
		anchor : "70%"
	});

	var sameSpace = new Ext.form.NumberField({
		id : 'sameSpace',
		fieldLabel : '同级间距',
		name : 'para.sameSpace',
		anchor : "70%"
	});

	var electricFieldWidth = new Ext.form.NumberField({
		id : 'electricFieldWidth',
		fieldLabel : '电场宽度',
		name : 'para.electricFieldWidth',
		anchor : "70%"
	});

	var electricFieldHeight = new Ext.form.NumberField({
		id : 'electricFieldHeight',
		fieldLabel : '电场高度',
		name : 'para.electricFieldHeight',
		anchor : "70%"
	});

	var electricFieldLength = new Ext.form.NumberField({
		id : 'electricFieldLength',
		fieldLabel : '电场有效长度',
		name : 'para.electricFieldLength',
		anchor : "70%"
	});

	var anodeType = new Ext.form.TextField({
		id : 'anodeType',
		fieldLabel : '阳极放电方式',
		name : 'para.anodeType',
		anchor : "70%"
	});

	var cathodeType = new Ext.form.TextField({
		id : 'cathodeType',
		fieldLabel : '阴极振打方式',
		name : 'para.cathodeType',
		anchor : "70%"
	});

	var oldCode = new Ext.form.TextField({
		id : 'oldCode',
		fieldLabel : '原编码',
		name : 'para.oldCode',
		anchor : "70%"
	});

	var memo = new Ext.form.TextArea({
		id : 'memo',
		fieldLabel : '备注',
		name : 'para.memo',
		height : 60,
		anchor : "84.5%"
	});

	var workApplyField = new Ext.form.FieldSet({
		border : true,
		labelWidth : 100,
		title : '参数',
		region : 'north',
		labelAlign : 'right',
		autoHeight : true,
		style : {
			"margin-top" : "20px",
			"margin-left" : "10px",
			"margin-right" : Ext.isIE6
					? (Ext.isStrict ? "-10px" : "-13px")
					: "0"
		},
		items : [{
			layout : 'column',
			hidden : true,
			items : [{
				columnWidth : .5,
				layout : 'form',
				items : [precipitationId]
			}, {
				columnWidth : .5,
				layout : 'form',
				items : [auxiliaryId]
			}]
		}, {
			layout : 'column',
			items : [{
				columnWidth : .5,
				layout : 'form',
				items : [precipitationType]
			}, {
				columnWidth : .5,
				layout : 'form',
				items : [clearDustEffi]
			}]
		}, {
			layout : 'column',
			items : [{
				columnWidth : .5,
				layout : 'form',
				items : [dustArea]
			}, {
				columnWidth : .5,
				layout : 'form',
				items : [importAirTpr]
			}]
		}, {
			layout : 'column',
			items : [{
				columnWidth : .5,
				border : false,
				layout : "form",
				items : [importAirThickness]
			}, {
				columnWidth : .5,
				border : false,
				layout : "form",
				items : [windLeakRate]
			}]
		}, {
			layout : 'column',
			items : [{
				columnWidth : .5,
				layout : 'form',
				items : [dealAirQuantity]
			}, {
				columnWidth : .5,
				layout : 'form',
				items : [maxNegativePressure]
			}]
		}, {
			layout : 'column',
			items : [{
				columnWidth : .5,
				layout : 'form',
				items : [resistanceLoss]
			}, {
				columnWidth : .5,
				layout : 'form',
				items : [electricFieldNum]
			}]
		}, {
			layout : 'column',
			items : [{
				columnWidth : .5,
				layout : 'form',
				items : [roomNum]
			}, {
				columnWidth : .5,
				layout : 'form',
				items : [sameSpace]
			}]
		}, {
			layout : 'column',
			items : [{
				columnWidth : .5,
				layout : 'form',
				items : [electricFieldWidth]
			}, {
				columnWidth : .5,
				layout : 'form',
				items : [electricFieldHeight]
			}]
		}, {
			layout : 'column',
			items : [{
				columnWidth : .5,
				layout : 'form',
				items : [electricFieldLength]
			}, {
				columnWidth : .5,
				layout : 'form',
				items : [anodeType]
			}]
		}, {
			layout : 'column',
			items : [{
				columnWidth : .5,
				layout : 'form',
				items : [cathodeType]
			}, {
				columnWidth : .5,
				layout : 'form',
				items : [oldCode]
			}]
		}, {
			layout : 'column',
			items : [{
				columnWidth : 1,
				layout : 'form',
				items : [memo]
			}]
		}]
	});

	var panel = new Ext.form.FormPanel({
		id : 'pan',
		autoScroll : true,
		width : 700,
		frame : true,
		border : false,
		layout : 'column',
		labelAlign : 'right',
		tbar : [{
			text : '保存',
			iconCls : 'save',
			handler : saveRec
		}, '-', {
			text : '刷新',
			iconCls : 'reflesh',
			handler : refresh
		}],
		items : [{
			columnWidth : 1,
			frame : false,
			border : false,
			layout : 'form',
			labelWidth : 110,
			items : [workApplyField]
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
		if (precipitationType.getValue() == null
				|| precipitationType.getValue() == '') {
			Ext.Msg.alert('提示', '类型不可为空，请输入！');
			return;
		}
		Ext.Msg.confirm('提示', '确认要保存数据吗？', function(id) {
			if (id == 'yes') {
				panel.getForm().submit({
					url : 'productionrec/savePrecipitationParameter.action',
					method : 'post',
					success : function(result, request) {
						Ext.Msg.alert('提示', '数据保存成功！');
						
						if (parent.document.all.iframe1 != null) {
							parent.document.all.iframe1.src = parent.document.all.iframe1.src;
							parent.Ext.getCmp("maintab").setActiveTab(0);
						}
					},
					failure : function(result, request) {
						Ext.Msg.alert('提示', '数据保存失败！');
					}
				})
			}
		})
	}

	function init() {
		Ext.Ajax.request({
			url : 'productionrec/getPrecipitationParameterById.action',
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

	function refresh() {
		view.render();
	}

	if (flagId == null) {
		panel.hide();
		return;
	} else {
		panel.show();
		init();
	}
})
