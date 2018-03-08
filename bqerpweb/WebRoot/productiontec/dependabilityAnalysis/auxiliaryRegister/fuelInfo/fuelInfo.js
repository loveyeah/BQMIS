Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

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
Ext.onReady(function() {

	var auxiliaryInfo = getParameter("auxiliaryInfo");

	
	var type = parent.type;
	

	function init() {
		var str = "{'data' : " + auxiliaryInfo + "}";
		if (auxiliaryInfo != null && auxiliaryInfo != "") {
			var o = Ext.util.JSON.decode(str);
		form.show();
		form.getForm().loadRecord(o)
		}
	}

	// ------------定义form---------------

	// 辅机ID
	var auxiliaryId = new Ext.form.Hidden({
		id : 'auxiliaryId',
		name : 'info.auxiliaryId'
	})
	// 所属机组
	var blockName = new Ext.form.TriggerField({
				fieldLabel : '所属机组',
				anchor : "80%",
				id : "blockName",
				hiddenName : 'sylbName',
				blankText : '请选择',
				emptyText : '请选择',
				maxLength : 100,
				readOnly : true
			});
	var blockId = new Ext.form.Hidden({
		id : 'blockId',
		name : 'info.blockId'
	});
	blockName.onTriggerClick = blockSelect;
	function blockSelect() {
		var args = {
			selectModel : 'signal',
			notIn : "",
			rootNode : {
				id : '-1',
				text : '合肥电厂'
			}
		}
		var url = "../../blockRegister/blockSelect.jsp";
		var block = window.showModalDialog(url, window,
				'dialogWidth=600px;dialogHeight=400px;status=no');
		if (typeof(block) != "undefined") {
			blockName.setValue(block.blockName);
			blockId.setValue(block.blockId);
			nameplateCapability.setValue(block.nameplateCapability)
		}
	}
	var auxiliaryTypeId = new Ext.form.Hidden({
		id : 'auxiliaryTypeId',
		name : 'info.auxiliaryTypeId',
		value : type
	});
	
	// 机组容量
	var nameplateCapability = new Ext.form.TextField({
		id : 'nameplateCapability',
		fieldLabel : '机组容量',
		name : 'nameplateCapability',
		anchor : "80%",
		readOnly : true
	});
	// 辅机编码
	var auxiliaryCode = new Ext.form.TextField({
		id : 'auxiliaryCode',
		fieldLabel : '辅机编码',
		name : 'info.auxiliaryCode',
		anchor : "80%"
	});

	// 辅机名称
	var auxiliaryName = new Ext.form.TextField({
		id : 'auxiliaryName',
		fieldLabel : '辅机名称',
		name : 'info.auxiliaryName',
		anchor : "80%"
	});

	// 型号
	var auxiliaryModel = new Ext.form.TextField({
		id : 'auxiliaryModel',
		fieldLabel : '型号',
		name : 'info.auxiliaryModel',
		anchor : "80%"
	});

	// 更新号
	var updateNo = new Ext.form.TextField({
		id : 'updateNo',
		fieldLabel : '更新号',
		name : 'info.updateNo',
		anchor : "80%"
	});

	// 投产日期
	var startProDateString = new Ext.form.TextField({
		id : 'startProDateString',
		fieldLabel : '投产时间',
		name : 'startProDateString',
		style : 'cursor:pointer',
		anchor : "80%",
		value : getDate(),
		readOnly : true,
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-%d',
					dateFmt : 'yyyy-MM-dd',
					alwaysUseStartDate : true,
					onpicked : function() {
					},
					onclearing : function() {
						startProDateString.markInvalid();
					}
				});
			}
		}
	});

	// 停统日期
	var stopStatDateString = new Ext.form.TextField({
		id : 'stopStatDateString',
		fieldLabel : '停统日期',
		name : 'stopStatDateString',
		style : 'cursor:pointer',
		anchor : "80%",
		value : getDate(),
		readOnly : true,
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-%d',
					dateFmt : 'yyyy-MM-dd',
					alwaysUseStartDate : true,
					onpicked : function() {
					},
					onclearing : function() {
						stopStatDateString.markInvalid();
					}
				});
			}
		}
	});

	// 出厂日期
	var leaveFactoryDateString = new Ext.form.TextField({
		id : 'leaveFactoryDateString',
		fieldLabel : '出厂日期',
		name : 'leaveFactoryDateString',
		style : 'cursor:pointer',
		anchor : "80%",
		value : getDate(),
		readOnly : true,
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-%d',
					dateFmt : 'yyyy-MM-dd',
					alwaysUseStartDate : true,
					onpicked : function() {
					},
					onclearing : function() {
						leaveFactoryDateString.markInvalid();
					}
				});
			}
		}
	});
	// 统计日期
	var statDateString = new Ext.form.TextField({
		id : 'statDateString',
		fieldLabel : '统计日期',
		name : 'statDateString',
		style : 'cursor:pointer',
		anchor : "80%",
		value : getDate(),
		readOnly : true,
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-%d',
					dateFmt : 'yyyy-MM-dd',
					alwaysUseStartDate : true,
					onpicked : function() {
					},
					onclearing : function() {
						statDateString.markInvalid();
					}
				});
			}
		}
	});

	// 停用日期
	var stopUseDateString = new Ext.form.TextField({
		id : 'stopUseDateString',
		fieldLabel : '停用日期',
		name : 'stopUseDateString',
		style : 'cursor:pointer',
		anchor : "80%",
		value : getDate(),
		readOnly : true,
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-%d',
					dateFmt : 'yyyy-MM-dd',
					alwaysUseStartDate : true,
					onpicked : function() {
					},
					onclearing : function() {
						stopUseDateString.markInvalid();
					}
				});
			}
		}
	});
	//出厂序号
	var leaveFactoryNo = new Ext.form.TextField({
		id : 'leaveFactoryNo',
		fieldLabel : '出厂序号',
		name : 'info.leaveFactoryNo',
		anchor : "80%"
	});
	//厂家编码
	var factoryCode = new Ext.form.TextField({
		id : 'factoryCode',
		fieldLabel : '厂家编码',
		name : 'info.factoryCode',
		anchor : "80%"
	});

	
	//制造厂家
	var produceFactory = new Ext.form.TextField({
		id : 'produceFactory',
		fieldLabel : '制造厂家',
		name : 'info.produceFactory',
		anchor : "80%"
	});

	var fieldSet = new Ext.form.FieldSet({
		border : false,
		labelWidth : 80,
		region : 'north',
		buttonAlign : 'center',
		labelAlign : 'right',
		autoHeight : true,
		frame : true,
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
				items : [auxiliaryId,blockName,blockId,auxiliaryName,auxiliaryModel,
				startProDateString,leaveFactoryDateString,stopUseDateString,factoryCode]
			}, {
				columnWidth : 0.5,
				layout : 'form',
				items : [nameplateCapability,auxiliaryCode,updateNo,stopStatDateString,
				statDateString,leaveFactoryNo,produceFactory]
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
				auxiliaryTypeId.setValue(type);
			}
		}]
	});


	var form = new Ext.form.FormPanel({
		border : false,
		frame : true,
		layout : 'form',
		fileUpload : true,
		items : [fieldSet],
		bodyStyle : {
			'padding-top' : '5px'
		},
		defaults : {
			labelAlign : 'right'
		}
	});

	

	var workApplyViewport = new Ext.Viewport({
		layout : "fit",
		border : false,
		autoScroll : true,
		height : 150,
		items : [form]

	});

	
		
		
	function saveRecord() {
		var url = "productionrec/saveAuxiliaryInfo.action";
		if (blockId.getValue() == null || blockId.getValue() == "") {
			Ext.Msg.alert('提示','所属机组不可为空，请选择！');
			return;
		}
		if (auxiliaryName.getValue() == null || auxiliaryName.getValue() == "") {
			Ext.Msg.alert('提示','辅机名称不可为空，请选择！');
			return;
		}
		form.getForm().submit({
			method : 'POST',
			url : url,
			params : {
				startProDateString : startProDateString.getValue(),
				stopStatDateString : stopStatDateString.getValue(),
				leaveFactoryDateString : leaveFactoryDateString.getValue(),
				statDateString : statDateString.getValue(),
				stopUseDateString : stopUseDateString.getValue(),
				auxiliaryTypeId : auxiliaryTypeId.getValue()
			},
			success : function(form, action) {
				var o = eval("(" + action.response.responseText + ")");
//				Ext.Msg.alert("提示", o.msg);

				if (parent.document.all.iframe1 != null) {
					parent.document.all.iframe1.src = parent.document.all.iframe1.src;
					parent.Ext.getCmp("maintab").setActiveTab(0);
					auxiliaryTypeId.setValue(type);
				}
			},
			faliue : function() {
				Ext.Msg.alert('错误', '出现未知错误.');
			}
		});
	}

	init();
})
