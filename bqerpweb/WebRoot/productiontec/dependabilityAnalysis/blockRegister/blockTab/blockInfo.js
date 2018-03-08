Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

function getDate() {
	var d, s, t;
	d = new Date();
	s = d.getFullYear().toString(10) + "-";
	t = d.getMonth() + 1;
	s += (t > 9 ? "" : "0") + t + "-";
	t = d.getDate();
	s += (t > 9 ? "" : "0") + t + " ";
	t = d.getHours();
	s += (t > 9 ? "" : "0") + t + ":";
	t = d.getMinutes();
	s += (t > 9 ? "" : "0") + t + ":";
	t = d.getSeconds();
	s += (t > 9 ? "" : "0") + t;
	return s;
}
Ext.onReady(function() {

	var blockInfo = getParameter("blockInfo");

	// if(blockInfo == "" ) {
	// Ext.MessageBox.alert('提示', '请从列表中选择一条数据！');
	// return;
	// }

	function init() {
		if (blockInfo != null && blockInfo != "") {
			var o = Ext.util.JSON.decode(blockInfo);

			blockId.setValue(o.blockId);
			blockCode.setValue(o.blockCode != null ? o.blockCode : "");
			blockName.setValue(o.blockName != null ? o.blockName : "");
			holdingCompany.setValue(o.holdingCompany != null
					? o.holdingCompany
					: "");
			manageCompany.setValue(o.manageCompany != null
					? o.manageCompany
					: "");
			nameplateCapability.setValue(o.nameplateCapability != null
					? o.nameplateCapability
					: "");
			attemperCompany.setValue(o.attemperCompany != null
					? o.attemperCompany
					: "");
			belongGrid.setValue(o.belongGrid != null ? o.belongGrid : "");
			blockType.setValue(o.blockType != null ? o.blockType : "");
			fuelName.setValue(o.fuelName != null ? o.fuelName : "");
			productionDate.setValue(o.productionDate != null
					? o.productionDate
					: "");
			statDate.setValue(o.statDate != null ? o.statDate : "");
			stopStatDate.setValue(o.stopStatDate != null ? o.stopStatDate : "");
			holdingPercent.setValue(o.holdingPercent != null
					? o.holdingPercent
					: "");
			boilerFactory.setValue(o.boilerFactory != null
					? o.boilerFactory
					: "");
			turbineFactory.setValue(o.turbineFactory != null
					? o.turbineFactory
					: "");
			generatorFactory.setValue(o.generatorFactory != null
					? o.generatorFactory
					: "");
			transformerFactory.setValue(o.transformerFactory != null
					? o.transformerFactory
					: "");

		}
	}

	// ------------定义form---------------

	var blockId = new Ext.form.TextField({
		id : 'blockId',
		name : 'block.blockId',
		anchor : "80%",
		hidden : true
	});

	var blockCode = new Ext.form.TextField({
		id : 'blockCode',
		fieldLabel : '机组编码',
		name : 'block.blockCode',
		anchor : "80%"
	});

	var blockName = new Ext.form.TextField({
		id : 'blockName',
		fieldLabel : '机组名称',
		name : 'block.blockName',
		anchor : "80%"
	});

	var holdingCompany = new Ext.form.TextField({
		id : 'holdingCompany',
		fieldLabel : '控股单位',
		name : 'block.holdingCompany',
		anchor : "80%"
	});

	var holdingPercent = new Ext.form.NumberField({
		id : 'holdingPercent',
		fieldLabel : '控股比例',
		name : 'block.holdingPercent',
		anchor : "80%"
	});

	var manageCompany = new Ext.form.TextField({
		id : 'manageCompany',
		fieldLabel : '管理单位',
		name : 'block.manageCompany',
		anchor : "80%"
	});

	var nameplateCapability = new Ext.form.NumberField({
		id : 'nameplateCapability',
		fieldLabel : '铭牌容量（MW）',
		name : 'block.nameplateCapability',
		anchor : "80%"
	});

	var attemperCompany = new Ext.form.TextField({
		id : 'attemperCompany',
		fieldLabel : '调度单位',
		name : 'block.attemperCompany',
		anchor : "80%"
	});

	var belongGrid = new Ext.form.TextField({
		id : 'belongGrid',
		fieldLabel : '所属电网',
		name : 'block.belongGrid',
		anchor : "80%"
	});

	var blockType = new Ext.form.TextField({
		id : 'blockType',
		fieldLabel : '机组类型',
		name : 'block.blockType',
		anchor : "80%"
	});

	var fuelName = new Ext.form.TextField({
		id : 'fuelName',
		fieldLabel : '燃料名称',
		name : 'block.fuelName',
		anchor : "80%"
	});

	var productionDate = new Ext.form.TextField({
		id : 'productionDate',
		fieldLabel : '投产时间',
		name : 'block.productionDate',
		style : 'cursor:pointer',
		anchor : "80%",
		value : getDate(),
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-%d 00:00:00',
					dateFmt : 'yyyy-MM-dd HH:mm:ss',
					alwaysUseStartDate : true,
					onpicked : function() {
					},
					onclearing : function() {
						productionDate.markInvalid();
					}
				});
			}
		}
	});

	var statDate = new Ext.form.TextField({
		id : 'statDate',
		fieldLabel : '统计时间',
		name : 'block.statDate',
		style : 'cursor:pointer',
		anchor : "80%",
		value : getDate(),
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-%d 00:00:00',
					dateFmt : 'yyyy-MM-dd HH:mm:ss',
					alwaysUseStartDate : true,
					onpicked : function() {
					},
					onclearing : function() {
						statDate.markInvalid();
					}
				});
			}
		}
	});

	var stopStatDate = new Ext.form.TextField({
		id : 'stopStatDate',
		fieldLabel : '停计时间',
		name : 'block.stopStatDate',
		style : 'cursor:pointer',
		anchor : "80%",
		value : getDate(),
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-%d 00:00:00',
					dateFmt : 'yyyy-MM-dd HH:mm:ss',
					alwaysUseStartDate : true,
					onpicked : function() {
					},
					onclearing : function() {
						stopStatDate.markInvalid();
					}
				});
			}
		}
	});

	var boilerFactory = new Ext.form.TextField({
		id : 'boilerFactory',
		fieldLabel : '锅炉',
		readOnly : true,
		anchor : "80%"
	});

	var turbineFactory = new Ext.form.TextField({
		id : 'turbineFactory',
		fieldLabel : '汽轮机',
		readOnly : true,
		anchor : "80%"
	});

	var generatorFactory = new Ext.form.TextField({
		id : 'generatorFactory',
		fieldLabel : '发电机',
		readOnly : true,
		anchor : "80%"
	});

	var transformerFactory = new Ext.form.TextField({
		id : 'transformerFactory',
		fieldLabel : '主变压器',
		readOnly : true,
		anchor : "80%"
	});

	var facoryField = new Ext.form.FieldSet({
		border : true,
		labelWidth : 80,
		title : '设备制造厂家',
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
				columnWidth : 0.5,
				layout : 'form',
				items : [boilerFactory]
			}, {
				columnWidth : 0.5,
				layout : 'form',
				items : [turbineFactory]
			}]
		}, {
			layout : 'column',
			items : [{
				columnWidth : 0.5,
				layout : 'form',
				items : [generatorFactory]
			}, {
				columnWidth : 0.5,
				layout : 'form',
				items : [transformerFactory]
			}]
		}],
		buttons : [{
			text : '保存',
			iconCls : 'save',
			handler : saveRecord
		}, {
			text : '清空',
			iconCls : 'cancer',
			handler : function() {
				form.getForm().reset();
			}
		}]
	});

	var workApplyField = new Ext.Panel({
		border : true,
		labelWidth : 100,
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
				columnWidth : 1,
				layout : 'form',
				items : [blockId]
			}]
		}, {
			layout : 'column',
			items : [{
				columnWidth : 0.5,
				layout : 'form',
				items : [blockCode]
			}, {
				columnWidth : 0.5,
				layout : 'form',
				items : [blockName]
			}]
		}, {
			layout : 'column',
			items : [{
				columnWidth : 0.5,
				layout : 'form',
				items : [holdingCompany]
			}, {
				columnWidth : 0.5,
				layout : 'form',
				items : [holdingPercent]
			}]
		}, {
			layout : 'column',
			items : [{
				columnWidth : 0.5,
				border : false,
				layout : "form",
				items : [manageCompany]
			}, {
				columnWidth : 0.5,
				border : false,
				layout : "form",
				items : [nameplateCapability]
			}]
		}, {
			layout : 'column',
			items : [{
				columnWidth : 0.5,
				layout : 'form',
				items : [attemperCompany]
			}, {
				columnWidth : 0.5,
				layout : 'form',
				items : [belongGrid]
			}]
		}, {
			layout : 'column',
			items : [{
				columnWidth : 0.5,
				layout : 'form',
				items : [blockType]
			}, {
				columnWidth : 0.5,
				layout : 'form',
				items : [fuelName]
			}]
		}, {
			layout : 'column',
			items : [{
				columnWidth : 0.5,
				layout : 'form',
				items : [productionDate]
			}, {
				columnWidth : 0.5,
				layout : 'form',
				items : [statDate]
			}]
		}, {
			layout : 'column',
			items : [{
				columnWidth : 0.5,
				layout : 'form',
				items : [stopStatDate]
			}]
		}]
	});

	var form = new Ext.form.FormPanel({
		border : false,
		frame : true,
		layout : 'form',
		fileUpload : true,
		items : [workApplyField, facoryField],
		bodyStyle : {
			'padding-top' : '5px'
		},
		defaults : {
			labelAlign : 'right'
		}
	});

	var workApply = new Ext.form.FieldSet({
		border : true,
		labelWidth : 80,
		labelAlign : 'right',
		layout : 'form',
		title : '机组信息',
		autoHeight : true,
		items : [form]
	});

	var workApplyViewport = new Ext.Viewport({
		layout : "fit",
		border : false,
		autoScroll : true,
		height : 150,
		items : [workApply]

	});

	function checkInput() {
		var msg = "";
		if (blockCode.getValue() == "") {
			msg = "机组编码)";
		}
		if (msg != "") {
			Ext.Msg.alert("提示", "请输入" + msg);
			return false
		} else {
			return true;
		}
	}

	function saveRecord() {
		var url = "";
		if (Ext.get("blockId").dom.value == ""
				|| Ext.get("blockId").dom.value == null) {
			url = "productionrec/addKkxBlockInfo.action"
		} else {
			url = "productionrec/updateKkxBlockInfo.action"
		}
		if (!checkInput())
			return;
		form.getForm().submit({
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
		});
	}

	init();
})
