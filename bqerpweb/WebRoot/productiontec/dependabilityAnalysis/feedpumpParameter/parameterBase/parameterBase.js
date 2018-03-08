Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
	flagId = parent.flagId;

	function init() {
		Ext.Ajax.request({
			url : 'productionrec/findParameterById.action',
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

	// ------------定义form---------------

	var feedpumpId = new Ext.form.Hidden({
		id : 'feedpumpId',
		name : 'feed.feedpumpId',
		anchor : "80%"
	});

	var auxiliaryId = new Ext.form.Hidden({
		id : 'auxiliaryId',
		value : flagId,
		name : 'feed.auxiliaryId'
	});

	var exportFlow = new Ext.form.NumberField({
		id : 'exportFlow',
		fieldLabel : '出口流量(T/H)',
		name : 'feed.exportFlow',
		anchor : "80%"
	});

	var exportPressure = new Ext.form.NumberField({
		id : 'exportPressure',
		fieldLabel : '出口压力(MPA)',
		name : 'feed.exportPressure',
		anchor : "80%"
	});

	var ratingSpeed = new Ext.form.NumberField({
		id : 'ratingSpeed',
		fieldLabel : '额定转速(RPM)',
		name : 'feed.ratingSpeed',
		anchor : "80%"
	});

	var ratingEfficiency = new Ext.form.NumberField({
		id : 'ratingEfficiency',
		fieldLabel : '额定效率（%）',
		name : 'feed.ratingEfficiency',
		anchor : "80%"
	});

	var minimumFlow = new Ext.form.NumberField({
		id : 'minimumFlow',
		fieldLabel : '最小流量(T/H)',
		name : 'feed.minimumFlow',
		anchor : "80%"
	});

	var tapFlow = new Ext.form.NumberField({
		id : 'tapFlow',
		fieldLabel : '抽头流量(T/H)',
		name : 'feed.tapFlow',
		anchor : "80%"
	});

	var tapPressure = new Ext.form.NumberField({
		id : 'tapPressure',
		fieldLabel : '抽头压力(MPA)',
		name : 'feed.tapPressure',
		anchor : "80%"
	});

	var cavitationFlow = new Ext.form.NumberField({
		id : 'cavitationFlow',
		fieldLabel : '汽蚀流量(KPA)',
		name : 'feed.cavitationFlow',
		anchor : "80%"
	});

	var adjustWay = new Ext.form.TextField({
		id : 'adjustWay',
		fieldLabel : '调节方式',
		name : 'feed.adjustWay',
		anchor : "80%"
	});

	var speedRangeFrom = new Ext.form.NumberField({
		id : 'speedRangeFrom',
		fieldLabel : '变速范围',
		name : 'feed.speedRangeFrom',
		anchor : "80%"
	});

	var speedRangeTo = new Ext.form.NumberField({
		id : 'speedRangeTo',
		fieldLabel : '到',
		name : 'feed.speedRangeTo',
		anchor : "80%"
	});

	var oldCode = new Ext.form.TextField({
		id : 'oldCode',
		fieldLabel : '原编码',
		name : 'feed.oldCode',
		anchor : "80%"
	});

	var memo = new Ext.form.TextArea({
		id : 'memo',
		fieldLabel : '备注',
		name : 'feed.memo',
		height : 60,
		anchor : "83.5%"
	});

	var prePumpModel = new Ext.form.TextField({
		id : 'prePumpModel',
		fieldLabel : '前置泵型号',
		name : 'feed.prePumpModel',
		anchor : "85%"
	});

	var npsh = new Ext.form.NumberField({
		id : 'npsh',
		fieldLabel : '汽蚀余量(KPA)',
		name : 'feed.npsh',
		anchor : "80%"
	});

	var importPressure = new Ext.form.NumberField({
		id : 'importPressure',
		fieldLabel : '进口水压(MPA)',
		name : 'feed.importPressure',
		anchor : "70%"
	});

	var importTemperature = new Ext.form.NumberField({
		id : 'importTemperature',
		fieldLabel : '进口水温',
		name : 'feed.importTemperature',
		anchor : "70%"
	});

	var pumpRatingSpeed = new Ext.form.NumberField({
		id : 'pumpRatingSpeed',
		fieldLabel : '泵额定转速(RPM)',
		name : 'feed.pumpRatingSpeed',
		anchor : "80%"
	});

	var facoryField = new Ext.form.FieldSet({
		border : true,
		labelWidth : 100,
		title : '前置泵参数',
		region : 'north',
		buttonAlign : 'center',
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
			items : [{
				columnWidth : 0.6,
				layout : 'form',
				items : [prePumpModel]
			}, {
				columnWidth : 0.3,
				layout : 'form',
				items : [npsh]
			}]
		}, {
			layout : 'column',
			items : [{
				columnWidth : 0.3,
				layout : 'form',
				items : [importPressure]
			}, {
				columnWidth : 0.3,
				layout : 'form',
				items : [importTemperature]
			}, {
				columnWidth : 0.3,
				layout : 'form',
				items : [pumpRatingSpeed]
			}]
		}]
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
				items : [feedpumpId]
			}, {
				columnWidth : .5,
				layout : 'form',
				items : [auxiliaryId]
			}]
		}, {
			layout : 'column',
			items : [{
				columnWidth : 0.3,
				layout : 'form',
				items : [exportFlow]
			}, {
				columnWidth : 0.3,
				layout : 'form',
				items : [exportPressure]
			}, {
				columnWidth : 0.3,
				layout : 'form',
				items : [ratingSpeed]
			}]
		}, {
			layout : 'column',
			items : [{
				columnWidth : 0.3,
				layout : 'form',
				items : [ratingEfficiency]
			}, {
				columnWidth : 0.3,
				layout : 'form',
				items : [minimumFlow]
			}, {
				columnWidth : 0.3,
				layout : 'form',
				items : [tapFlow]
			}]
		}, {
			layout : 'column',
			items : [{
				columnWidth : 0.3,
				border : false,
				layout : "form",
				items : [tapPressure]
			}, {
				columnWidth : 0.3,
				border : false,
				layout : "form",
				items : [cavitationFlow]
			}, {
				columnWidth : 0.3,
				border : false,
				layout : "form",
				items : [adjustWay]
			}]
		}, {
			layout : 'column',
			items : [{
				columnWidth : 0.3,
				layout : 'form',
				items : [speedRangeFrom]
			}, {
				columnWidth : 0.3,
				layout : 'form',
				items : [speedRangeTo]
			}, {
				columnWidth : 0.3,
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
		width : 800,
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
			items : [workApplyField, facoryField]
		}]
	})
	var view = new Ext.Viewport({
		id : 'view',
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
			items : [panel]
		}]
	})

	function saveRec() {
		var url = "productionrec/saveFeedpumpParameter.action";
		if (ratingEfficiency.getValue() == null
				|| ratingEfficiency.getValue() == "") {
			Ext.Msg.alert('提示', '额定效率不可为空，请填写！');
			return;
		}
		Ext.Msg.confirm('提示', '确认要保存数据吗？', function(id) {
			if (id == 'yes') {
				panel.getForm().submit({
					method : 'POST',
					url : url,
					success : function(form, action) {
						var o = eval("(" + action.response.responseText + ")");
						Ext.Msg.alert("注意", o.msg);

						if (parent.document.all.iframe1 != null) {
							parent.document.all.iframe1.src = parent.document.all.iframe1.src;
							parent.Ext.getCmp("maintab").setActiveTab(0);
						}
					},
					faliue : function() {
						Ext.Msg.alert('错误', '出现未知错误.');
					}
				})
			}
		});
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
