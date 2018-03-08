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
	
	// 磨煤机技术参数ID 1
	var pulverizerId = new Ext.form.Hidden({
		id : 'pulverizerId',
		name : 'param.pulverizerId'
	})
	// 辅机ID 2
	var auxiliaryId = new Ext.form.Hidden({
		id : 'auxiliaryId',
		value : flagId,
		name : 'param.auxiliaryId'
	})
	// 额定出力（T/H）
	var ratingCl = new Ext.form.NumberField({
		id : 'ratingCl',
		name : 'param.ratingCl',
		fieldLabel : '额定出力（T/H）',
		allowNegative : false
	})
	// 额定转速(RPM) 4
	var ratingSpeed = new Ext.form.NumberField({
		id : 'ratingSpeed',
		name : 'param.ratingSpeed',
		fieldLabel : '额定转速(RPM)',
		allowNegative : false
	})
	// 低中高速类别 5
	var speedType = new Ext.form.TextField({
		id : 'speedType',
		name : 'param.speedType',
		fieldLabel : '低中高速类别'
	})
	// 制粉系统类型 6
	var systemType = new Ext.form.TextField({
		id : 'systemType',
		name : 'param.systemType',
		fieldLabel : '制粉系统类型'
	})
	// 原编码 7
	var oldCode = new Ext.form.TextField({
		id : 'oldCode',
		name : 'param.oldCode',
		fieldLabel : '原编码'
	})
	// 备注 8
	var memo = new Ext.form.TextField({
		id : 'memo',
		name : 'param.memo',
		fieldLabel : '备注'
	})
	// 电动机型号 9
	var electromotorNo = new Ext.form.TextField({
		id : 'electromotorNo',
		name : 'param.electromotorNo',
		fieldLabel : '电动机型号',
		width : 454
	})
	// 额定功率(KW) 10
	var ratingPower = new Ext.form.NumberField({
		id : 'ratingPower',
		name : 'param.ratingPower',
		allowNegative : false,
		fieldLabel : '额定功率(KW)'
	})
	// 电动机额定转速（RPM） 11
	var electRatingSpeed = new Ext.form.NumberField({
		id : 'electRatingSpeed',
		name : 'param.electRatingSpeed',
		allowNegative : false,
		fieldLabel : '电动机额定转速（RPM）'
	})
	//额定电压（KV） 12
	var ratingVoltage = new Ext.form.NumberField({
		id : 'ratingVoltage',
		name : 'param.ratingVoltage',
		allowNegative : false,
		fieldLabel : '额定电压（KV）'
	})
	//额定电流（A） 13
	var ratingElectricity = new Ext.form.NumberField({
		id : 'ratingElectricity',
		name : 'param.ratingElectricity',
		allowNegative : false,
		fieldLabel : '额定电流（A）'
	})
	// 变速方式 14connType
	var shiftType = new Ext.form.TextField({
		id : 'shiftType',
		name : 'param.shiftType',
		fieldLabel : '变速方式'
	})
	// 接线方式 19
	var connType = new Ext.form.TextField({
		id : 'connType',
		name : 'param.connType',
		fieldLabel : '接线方式'
	})
	// 制造厂家编码 15
	var factoryCode = new Ext.form.TextField({
		id : 'factoryCode',
		name : 'param.factoryCode',
		fieldLabel : '制造厂家编码'
	})
	//厂家名称 16
	var factoryName = new Ext.form.TextField({
		id : 'factoryName',
		name : 'param.factoryName',
		fieldLabel : '厂家名称'
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
			items : [pulverizerId,auxiliaryId,ratingCl,speedType,oldCode]
		},{
			columnWidth : .5,
			frame : false,
			border : false,
			layout : 'form',
			items : [ratingSpeed,systemType,memo]
		}]
	})
	
	
	var set2 = new Ext.form.FieldSet({
		id : 'set2',
		frame : true,
		border : true,
		layout : 'column',
		title : '电动机参数',
		draggable : false,
		collapsible : true,
		items : [{
			columnWidth : 1,
			frame : false,
			border : false,
			layout : 'form',
			items : [electromotorNo]
		},{
			columnWidth : .45,
			frame : false,
			border : false,
			layout : 'form',
			items : [ratingPower,ratingVoltage,shiftType,factoryCode]
		},{
			columnWidth : .5,
			frame : false,
			border : false,
			layout : 'form',
			labelWidth : 140,
			items : [electRatingSpeed,ratingElectricity,connType,factoryName]
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
			labelWidth : 110,
			items : [set1,set2]
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
		if (ratingCl.getValue() == null || ratingCl.getValue() == '') {
			Ext.Msg.alert('提示', '额定出力不能为空，请输入！');
			return;
		}
		Ext.Msg.confirm('提示', '确认要保存数据吗？', function(id) {
			if (id == 'yes') {
				panel.getForm().submit({
							url : 'productionrec/savePulverizerParam.action',
							method : 'post',
//							params : {
//								manufactureDate : manufactureDate.getValue()
//							},
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
		//		alert(pBlockId)		
		Ext.Ajax.request({
					url : 'productionrec/getPulverizerParamByAuxId.action',
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