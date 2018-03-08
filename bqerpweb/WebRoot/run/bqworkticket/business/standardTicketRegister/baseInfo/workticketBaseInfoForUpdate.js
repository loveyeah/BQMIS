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

	var existWorkticketNo = "";

	var register = parent.Ext.getCmp('tabPanel').register;
	register.loadRecord = getPageData;
	register.addRecord = pageNoDataInitial;
	var myDangerId = "";// 工作票危险点内容id

	// add by fyyang 090416
	var cbxMainEqu = new Ext.form.TriggerField({
		fieldLabel : '主设备',
		id : "mainEqu",
		name : "workticketBaseInfo.mainEquName",
		onTriggerClick : mainEquSelect,
		anchor : "50%"
	});
	function mainEquSelect() {
		var url = "../../../../../comm/jsp/equselect/selectAttribute.jsp?";
		url += "op=one";
		var equ = window.showModalDialog(url, window,
				'dialogWidth=600px;dialogHeight=400px;status=no');
		if (typeof(equ) != "undefined") {
			var name = equ.name;
			var code = equ.code;
			cbxMainEqu.setValue(name);
			Ext.getCmp("hideMainEquCode").setValue(code);
			// 设置所属系统
			// cbxChargeBySystem.setValue(code.substring(0,2) );
		}
	}

	// 需要退出热工保护或自动装置名称 add by ywliu 090508
	var autoDeviceName = new Ext.form.TextArea({
		id : 'autoDeviceName',
		fieldLabel : "需要退出热工保护或自动装置名称",
		// xtype : 'textarea',
		name : 'workticketBaseInfo.autoDeviceName',
		value : '',
		height : 50,
		anchor : "80%",
		disabled : true
	});

	// -------add by fyyang 090217-----
	// 工作位置/地点
	var locationName = new Ext.form.TriggerField({
		fieldLabel : '工作地点<font color="red">*</font>',
		id : "locationName",
		name : "workticketBaseInfo.locationName",
		allowBlank : false,
		onTriggerClick : locationSelect,
		anchor : "50%"
	});

	/** 区域选择处理 */
	function locationSelect() {
		if (cbxChargeBySystem.getValue() == "") {
			Ext.Msg.alert("提示", "请选择所属机组或系统");
			return;
		}
		var url = "selectLocation.jsp?op=many&blockCode="
				+ Ext.get("workticketBaseInfo.equAttributeCode").dom.value;;
		var location = window.showModalDialog(url, window,
				'dialogWidth=400px;dialogHeight=300px;status=no');
		if (typeof(location) != "undefined") {
			// 设置设备名
			locationName.setValue(location.name);
		}
	}
	// -----------------------------------

	// 工作票编号
	var txtWorkticketNo = new Ext.form.TextField({
		id : 'workticketNo',
		fieldLabel : "工作票编号",
		xtype : 'textfield',
		value : '',
		emptyText : Constants.AUTO_CREATE,
		readOnly : true,
		anchor : "50%"
	});

	// 工作票种类
	var storeWorkticketType = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'workticket/getDetailWorkticketTypeName.action'
		}),
		reader : new Ext.data.JsonReader({
			root : 'list'
		}, [{
			name : 'workticketTypeCode',
			mapping : 'workticketTypeCode'
		}, {
			name : 'workticketTypeName',
			mapping : 'workticketTypeName'
		}])
	});
	// storeWorkticketType.load();
	var cbxWorkticketType = new Ext.form.ComboBox({
		id : 'workticketTypeCode',
		fieldLabel : "工作票种类<font color='red'>*</font>",
		store : storeWorkticketType,
		displayField : "workticketTypeName",
		valueField : "workticketTypeCode",
		hiddenName : 'workticketBaseInfo.workticketTypeCode',
		mode : 'remote',
		triggerAction : 'all',
		value : '',
		readOnly : true,
		anchor : "50%",
		listeners : {
			select : workticketTypeSelected
		}
	})
	// 工作票来源
	var storeWorkticketSource = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'workticket/getDetailWorkticketSource.action'
		}),
		reader : new Ext.data.JsonReader({
			root : 'list'
		}, [{
			name : 'sourceId',
			mapping : 'sourceId'
		}, {
			name : 'sourceName',
			mapping : 'sourceName'
		}])
	});
	// storeWorkticketSource.load();
	var cbxWorkticketSource = new Ext.form.ComboBox({
		id : 'sourceId',
		fieldLabel : "工作票来源<font color='red'>*</font>",
		store : storeWorkticketSource,
		displayField : "sourceName",
		valueField : "sourceId",
		hiddenName : 'workticketBaseInfo.sourceId',
		mode : 'remote',
		triggerAction : 'all',
		value : '',
		readOnly : true,
		anchor : "50%"
	})

	// 检修专业
	var storeRepairSpecail = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'workticket/getDetailRepairSpecialityType.action'
		}),
		reader : new Ext.data.JsonReader({
			root : 'list'
		}, [{
			name : 'specialityCode',
			mapping : 'specialityCode'
		}, {
			name : 'specialityName',
			mapping : 'specialityName'
		}])
	});
	// storeRepairSpecail.load();
	var cbxRepairSpecail = new Ext.form.ComboBox({
		id : 'repairSpecailCode',
		fieldLabel : "检修专业<font color='red'>*</font>",
		store : storeRepairSpecail,
		displayField : "specialityName",
		valueField : "specialityCode",
		hiddenName : 'workticketBaseInfo.repairSpecailCode',
		mode : 'remote',
		triggerAction : 'all',
		value : '',
		readOnly : true,
		anchor : "50%"
	})

	// 接收专业
	var storeReceiveSpecail = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'workticket/getDetailReceiveSpecialityType.action'
		}),
		reader : new Ext.data.JsonReader({
			root : 'list'
		}, [{
			name : 'specialityCode',
			mapping : 'specialityCode'
		}, {
			name : 'specialityName',
			mapping : 'specialityName'
		}])
	});
	// storeReceiveSpecail.load();
	var cbxReceiveSpecail = new Ext.form.ComboBox({
		id : 'permissionDept',
		fieldLabel : "接收专业<font color='red'>*</font>",
		store : storeReceiveSpecail,
		displayField : "specialityName",
		valueField : "specialityCode",
		hiddenName : 'workticketBaseInfo.permissionDept',
		mode : 'remote',
		triggerAction : 'all',
		value : '',
		readOnly : true,
		anchor : "50%"
	})
	// 工作条件
	var cbxWorkCondition = new Ext.form.TextField({
		id : 'conditionName',
		fieldLabel : "工作条件",
		// xtype : 'textfield',
		name : 'workticketBaseInfo.conditionName',
		value : '',
		disabled : true,
		anchor : "50%"
	});
	// var storeWorkCondition = new Ext.data.Store({
	// proxy : new Ext.data.HttpProxy({
	// url : 'workticket/getDetailWorkCondition.action'
	// }),
	// reader : new Ext.data.JsonReader({
	// root : 'list'
	// }, [{
	// name : 'conditionId',
	// mapping : 'conditionId'
	// }, {
	// name : 'conditionName',
	// mapping : 'conditionName'
	// }])
	// });
	// storeWorkCondition.load();
	// var cbxWorkCondition = new Ext.form.ComboBox({
	// id : 'conditionId',
	// fieldLabel : "工作条件",
	// store : storeWorkCondition,
	// displayField : "conditionName",
	// valueField : "conditionId",
	// hiddenName : 'workticketBaseInfo.conditionId',
	// mode : 'local',
	// triggerAction : 'all',
	// disabled : true,
	// value : '',
	// readOnly : true,
	// anchor : "50%"
	// })

	// 所属机组或系统
	var storeChargeBySystem = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'workticket/getDetailEquList.action'
		}),
		reader : new Ext.data.JsonReader({
			root : 'list'
		}, [{
			name : 'blockCode',
			mapping : 'blockCode'
		}, {
			name : 'blockName',
			mapping : 'blockName'
		}])
	});
	// storeChargeBySystem.load();
	var cbxChargeBySystem = new Ext.form.ComboBox({
		id : 'equAttributeCode',
		fieldLabel : "所属机组或系统<font color='red'>*</font>",
		store : storeChargeBySystem,
		displayField : "blockName",
		valueField : "blockCode",
		hiddenName : 'workticketBaseInfo.equAttributeCode',
		mode : 'remote',
		triggerAction : 'all',
		value : '',
		readOnly : true,
		anchor : "50%",
		listeners : {
			select : function() {
				locationName.setValue(" ");
			}
		}
	})

	// 危险点类型
	var dangerType = new Ext.form.ComboBox({
		fieldLabel : '危险点分析类别',
		name : 'dangerType',
		id : 'dangerType',
		anchor : "80%",
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		hiddenName : 'workticketBaseInfo.dangerType',
		editable : false,
		triggerAction : 'all',
		readOnly : true,
		anchor : "50%",
		onTriggerClick : function() {
		}
	});
	// var dangerType = {
	// fieldLabel : '危险点分析类别',
	// name : 'dangerType',
	// xtype : 'combo',
	// id : 'dangerType',
	// anchor : "80%",
	// store : new Ext.data.SimpleStore({
	// fields : ['id', 'name'],
	// data : [[]]
	// }),
	// mode : 'remote',
	// hiddenName : 'workticketBaseInfo.dangerType',
	// editable : false,
	// triggerAction : 'all',
	// readOnly : true,
	// anchor : "50%",
	// onTriggerClick : function() {
	// }
	// };

	// 危险点内容
	var dangerCondition = new Ext.form.ComboBox({
		fieldLabel : '危险点工作内容',
		name : 'dangerCondition',
		id : 'dangerCondition',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		hiddenName : 'workticketBaseInfo.dangerCondition',
		// editable : false,
		triggerAction : 'all',
		// readOnly : true,
		onTriggerClick : dangerConditionSelect,

		anchor : "50%"
	});

	// var dangerCondition = {
	// fieldLabel : '危险点工作内容',
	// name : 'workticketBaseInfo.dangerCondition',
	// xtype : 'combo',
	// id : 'dangerCondition',
	// anchor : "80%",
	// store : new Ext.data.SimpleStore({
	// fields : ['id', 'name'],
	// data : [[]]
	// }),
	// mode : 'remote',
	// hiddenName : 'workticketBaseInfo.dangerCondition',
	// //editable : false,
	// triggerAction : 'all',
	// // readOnly : true,
	// anchor : "50%",
	// onTriggerClick :dangerConditionSelect,
	// readOnly : true,
	// anchor : "50%"
	// };

	function dangerConditionSelect() {
		if (cbxWorkticketType.getValue() == "") {
			Ext.Msg.alert("提示", "请选择工作票类型");
		} else {
			var args = new Object();
			args.workticketType = Ext
					.get("workticketBaseInfo.workticketTypeCode").dom.value;
			var danger = window
					.showModalDialog(
							'../../standardTicketRegister/baseInfo/dangerSelect.jsp',
							args,
							'dialogWidth=700px;dialogHeight=500px;center=yes;help=no;resizable=no;status=no;');
			if (typeof(danger) == 'object') {

				myDangerId = danger.id;
				Ext.getCmp('dangerCondition').setValue(danger.name);
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('dangerCondition'), danger.name);
				// Ext.get("dangerType").dom.value=danger.type;
				Ext.getCmp('dangerType').setValue(danger.type);
				Ext.form.ComboBox.superclass.setValue.call(Ext
						.getCmp('dangerType'), danger.typeName);
			}

		}
	}

	// 备注
	var txtRemark = {
		id : 'workticketMemo',
		fieldLabel : "备注",
		xtype : 'textarea',
		name : 'workticketBaseInfo.workticketMemo',
		value : '',
		height : 50,
		anchor : "80%"
	}

	// 动火票级别
	var storeFireLevel = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'workticket/getDetailFireLevel.action'
		}),
		reader : new Ext.data.JsonReader({
			root : 'list'
		}, [{
			name : 'firelevelId',
			mapping : 'firelevelId'
		}, {
			name : 'firelevelName',
			mapping : 'firelevelName'
		}])
	});
	// storeFireLevel.load();
	var cbxFireLevel = new Ext.form.ComboBox({
		id : 'firelevelId',
		fieldLabel : '动火票级别',
		store : storeFireLevel,
		displayField : "firelevelName",
		valueField : "firelevelId",
		hiddenName : 'workticketBaseInfo.firelevelId',
		mode : 'remote',
		triggerAction : 'all',
		disabled : true,
		value : '',
		readOnly : true,
		anchor : "50%"
	})

	// ---------add by fyyang 090523----------

	// 工作内容
	var txtWorkContent = new Ext.form.TextArea({
		id : 'workticketBaseInfo.workticketContent',
		name : 'workticketBaseInfo.workticketContent',
		fieldLabel : "工作内容",
		// style:'cursor:hand;',
		// readOnly:true,
		// emptyText : '双击填写工作内容',
		allowBlank : false,
		xtype : 'textarea',
		height : 60,
		anchor : "95%"
	});

	var btnCreateByHisWorkticket = new Ext.Button({
		text : '由现有票生成',
		// iconCls:'nextStep',
		handler : function() {
			if (Ext.get("workticketBaseInfo.workticketTypeCode").dom.value == "") {
				Ext.Msg.alert("提示", "请先选择工作票类型！");
				return;
			}
			var args = new Object();
			args.fireLevel=Ext.get("workticketBaseInfo.firelevelId").dom.value; //add by fyyang 090729
			args.workticketNo = txtWorkticketNo.getValue();
			args.workticketTypeCode = Ext
					.get("workticketBaseInfo.workticketTypeCode").dom.value;
			var object = window
					.showModalDialog(
							'selectHisWorkticket.jsp',
							args,
							'dialogWidth=800px;dialogHeight=500px;center=yes;help=no;resizable=no;status=no;');
			if (typeof(object) != "undefined") {
				existWorkticketNo = object.hisWorkticketNo;
				if (txtWorkticketNo.getValue() == null
						|| txtWorkticketNo.getValue() == "") {

					txtWorkContent.setValue(object.workticketContent);
					// Ext.lib.Ajax.request('POST',
					// 'workticket/getContentWorkticketList.action', {
					// success : function(result, request) {
					// var record = eval('(' + result.responseText
					// + ')');
					// var contentRec = [];
					// if (tempContentStoreData != null) {
					// contentRec = tempContentStoreData;
					// }
					//
					// for (var i = 0; i < record.list.length; i++) {
					// var data = record.list[i];
					// data.id = null;
					// data.workticketNo = null;
					// contentRec.push(data);
					// }
					// tempContentStoreData = contentRec;
					//
					// txtWorkContent
					// .setValue(Workticket
					// .spellContentByArray(tempContentStoreData));
					// },
					// failure : function() {
					// Ext.Msg.alert(Constants.ERROR,
					// Constants.OPERATE_ERROR_MSG);
					// }
					// }, 'workticketNo=' + object.hisWorkticketNo);
					register.existTicketNo = object.hisWorkticketNo;
					register.workticketTypeCode = cbxWorkticketType.getValue();
					// register._changeWorkticketNo.apply(register);
				} else {
					register.refreshSafty();
					txtWorkContent.setValue(object.workticketContent);
				}
			}
		}
	});
	// -------------------------add end--------------------

	// 修改
	var btnModify = new Ext.Button({
		text : Constants.BTN_UPDATE,
		hidden : true,
		iconCls : 'update',
		handler : updateWorkticket

	})
	// 下一步
	var btnNextStep = new Ext.Button({
		text : Constants.NEXT_STEP,
		iconCls : 'nextStep',
		handler : nextStep
	})
	var baseInfoField = new Ext.form.FieldSet({
		// height : 440,
		autoHeight : true,
		style : {
			"padding-top" : '0',
			"padding-left" : '60'
		},
		bodyStyle : Ext.isIE ? 'padding:0 0 5px 15px;' : 'padding:5px 15px;',
		// width : 780,
		anchor : '-20',
		border : false,
		buttonAlign : 'center',
		items : [
				// 工作票编号
				{

					id : 'hideWorkticketNo',
					name : 'workticketBaseInfo.workticketNo',
					value : '',
					xtype : 'hidden'
				}, txtWorkticketNo,
				cbxWorkticketType,
				// add
				{
					id : 'hideMainEquCode',
					name : 'workticketBaseInfo.mainEquCode',
					value : '',
					xtype : 'hidden'
				}, cbxMainEqu, cbxWorkticketSource, cbxRepairSpecail,
				cbxReceiveSpecail, cbxWorkCondition, cbxChargeBySystem,
				locationName, dangerCondition, dangerType,
				// //需要退出热工保护或自动装置名称 add by ywliu 090508
				autoDeviceName,
				// {
				// layout : "column",
				// border : false,
				// items : [{
				// columnWidth : 0.97,
				// layout : "form",
				// border : false,
				// items : [autoDeviceName]
				// }]
				// },
				// 工作内容
				{
					layout : "column",
					border : false,
					items : [{
						columnWidth : 0.70,
						layout : "form",
						border : false,
						items : [txtWorkContent]
					}, {
						columnWidth : 0.2,
						layout : "form",
						border : false,
						height : 60,
						items : [btnCreateByHisWorkticket]

					}]

				}, txtRemark, cbxFireLevel

		]
			// ,
			// buttons : [btnModify, btnNextStep]
	});
	var bbar = new Ext.Toolbar({
		height : 20,
		items : [new Ext.Toolbar.Fill(), btnModify, btnNextStep]
	});
	// 底部工具条
	var topbar = new Ext.Toolbar({
		region : 'north',
		// height : 100,
		items : [new Ext.Toolbar.Fill(), {
			text : '由终结票生成',
			handler : function() {
				var object = window
						.showModalDialog(
								'endTicketSelect.jsp',
								null,
								'dialogWidth=750px;dialogHeight=400px;center=yes;help=no;resizable=no;status=no;');
				if (typeof(object) == 'object') {
					topbar.setDisabled(true);
					workticketNo = object.workticketNo;
					register.workFlowNo = '';
					register.firelevelId = object.fireLevelId;
					// 编辑工作票信息
					register.edit(workticketNo);
				}

			}
		}]
	})

	var baseInfoFormPanel = new Ext.FormPanel({
		border : false,
		frame : true,
		fileUpload : true,
		tbar : topbar,
		bbar : bbar,
		items : [baseInfoField],
		bodyStyle : {
			'padding-top' : '5px'
		},
		defaults : {
			labelAlign : 'right'
		}
	});

	var baseInfoViewport = new Ext.Viewport({
		// height : 600,
		layout : "fit",
		border : false,
		items : [baseInfoFormPanel],
		defaults : {
			autoScroll : true
		}
	}).show();

	// 页面初始化为修改时，页面数据读取
	function getPageData(workticketNo) {

		var dangertypeid = "";
		pageNoDataInitial();
		topbar.setDisabled(true);
		Ext.lib.Ajax.request('POST',
				'workticket/getDetailTicketInfoByCode.action', {
					success : function(result, request) {
						var record = eval('(' + result.responseText + ')');
						txtWorkContent
								.setValue(record.data.model.workticketContent);
						// 工作票号
						Ext.get("workticketNo").dom.value = record.data.model.workticketNo;
						Ext.get("workticketBaseInfo.workticketNo").dom.value = record.data.model.workticketNo;
						// 工作票种类

						Ext.getCmp('workticketTypeCode')
								.setValue(record.data.model.workticketTypeCode);
						Ext.form.ComboBox.superclass.setValue.call(Ext
								.getCmp('workticketTypeCode'),
								record.data.workticketTypeName);

						// 检修专业
						Ext.getCmp('repairSpecailCode')
								.setValue(record.data.model.repairSpecailCode);
						Ext.form.ComboBox.superclass.setValue.call(Ext
								.getCmp('repairSpecailCode'),
								record.data.repairSpecailName);
						// 来源
						Ext.getCmp('sourceId')
								.setValue(record.data.model.sourceId);
						Ext.form.ComboBox.superclass.setValue.call(Ext
								.getCmp('sourceId'), record.data.sourceName);

						// 接收专业
						Ext.getCmp('permissionDept')
								.setValue(record.data.model.permissionDept);
						Ext.form.ComboBox.superclass.setValue.call(Ext
								.getCmp('permissionDept'),
								record.data.recieveSpecailName);
						// 所属机组或系统
						Ext.getCmp('equAttributeCode')
								.setValue(record.data.model.equAttributeCode);
						Ext.form.ComboBox.superclass.setValue.call(Ext
								.getCmp('equAttributeCode'),
								record.data.blockName);

						// 工作地点
						if (record.data.model.locationName != null) {
							locationName
									.setValue(record.data.model.locationName);
						}
						// 危险点内容
						if (record.data.model.dangerCondition != null
								&& record.data.model.dangerCondition != "null") {
							dangerCondition
									.setValue(record.data.model.dangerCondition);
						}
						// 危险点类型
						if (record.data.dangerTypeName != null) {
							Ext.getCmp('dangerType').setValue(dangertypeid);
							Ext.form.ComboBox.superclass.setValue.call(Ext
									.getCmp('dangerType'),
									record.data.dangerTypeName);
						} else {

							Ext.getCmp('dangerType').setValue("");
							Ext.form.ComboBox.superclass.setValue.call(Ext
									.getCmp('dangerType'), "");
						}

						// 主设备
//						if (record.data.model.mainEquCode != null) {
							Ext.getCmp("hideMainEquCode")
									.setValue(record.data.model.mainEquCode);
							cbxMainEqu.setValue(record.data.model.mainEquName);
//						}
						// 备注 workticketMemo
						if (record.data.model.workticketMemo != null) {
							Ext.get("workticketMemo").dom.value = record.data.model.workticketMemo;
						}

						// -----------显示处理--------------------

						// 工作票种类，检修专业不能修改
						cbxWorkticketType.disable();
						// cbxRepairSpecail.disable();
						cbxFireLevel.disable();

						// 控制修改按钮显示
						btnModify.setVisible(true);
						// 根据工作票种类来判断哪些是可用的
						register.workticketNo = null;
						workticketTypeSelected();
						// 以下字段设置是否可用后再赋值
						// 工作条件conditionName

						if (record.data.model.conditionName != null) {
							Ext.get("conditionName").dom.value = record.data.model.conditionName;

						}
						// 需要退出热工保护或自动装置名称 add by ywliu 090508
						if (record.data.model.autoDeviceName != null) {
							autoDeviceName
									.setValue(record.data.model.autoDeviceName);
						}
						// 动火票级别 firelevelId
						if (record.data.model.firelevelId != null) {
							Ext.getCmp('firelevelId')
									.setValue(record.data.model.firelevelId);
							if (record.data.model.firelevelId == 1) {
								Ext.form.ComboBox.superclass.setValue.call(Ext
										.getCmp('firelevelId'), "一级动火");
							}
							if (record.data.model.firelevelId == 2) {
								Ext.form.ComboBox.superclass.setValue.call(Ext
										.getCmp('firelevelId'), "二级动火");
							}
						}

						// 设置工作票编号
						register.workticketNo = Ext.get('workticketNo').dom.value;
						// 设置所属机组或系统
						register.equAttributeCode = Ext
								.get('workticketBaseInfo.equAttributeCode').dom.value;
						// 设置工作票种类/工作票类型编码
						register.workticketTypeCode = Ext
								.get('workticketBaseInfo.workticketTypeCode').dom.value;

						if (record.data.dangerType != null) {
							dangertypeid = record.data.dangerType;
						}

					},
					failure : function() {
						Ext.Msg.alert(Constants.ERROR,
								Constants.OPERATE_ERROR_MSG);
					}
				}, 'workticketNo=' + workticketNo);

		// Ext.lib.Ajax.request('POST',
		// 'workticket/getDetailWorkticketBaseInfoByNo.action', {
		// success : function(result, request) {
		// var record = eval('(' + result.responseText + ')');
		// baseInfoFormPanel.getForm().loadRecord(record);
		// Ext.get("workticketBaseInfo.workticketNo").dom.value =
		// record.data.workticketNo;
		//
		// // 工作票种类，来源，检修专业不能修改
		// cbxWorkticketType.disable();
		// cbxWorkticketSource.disable();
		// cbxRepairSpecail.disable();
		// // 根据工作票种类来判断哪些是可用的
		// workticketTypeSelected();
		// // 动火级别
		// if (record.data.firelevelId != null) {
		// Ext.get('firelevelId').dom.value = record.data.firelevelId;
		// }
		// if(record.data.dangerType!=null)
		// {
		// dangertypeid=record.data.dangerType;
		// }
		//                     
		//
		// // 控制修改按钮显示
		// btnModify.setVisible(true);
		// // 设置工作票编号
		// register.workticketNo = Ext.get('workticketNo').dom.value;
		// // 设置工作票种类/工作票类型编码
		// register.workticketTypeCode =
		// Ext.getCmp('workticketTypeCode').getValue();
		// //设置所属机组或系统
		// register.equAttributeCode =
		// Ext.getCmp('equAttributeCode').getValue();
		//                
		// // 汉字姓名,部门名称读取
		// Ext.lib.Ajax.request('POST',
		// 'workticket/getDetailTicketInfoByCode.action', {
		// success : function(result, request) {
		// var record = eval('(' + result.responseText + ')');
		// if(record.data.dangerTypeName!=null)
		// {
		// Ext.getCmp('dangerType').setValue(dangertypeid);
		// Ext.form.ComboBox.superclass.setValue.call(Ext
		// .getCmp('dangerType'),record.data.dangerTypeName);
		// // Ext.get("dangerType").dom.value=record.data.dangerTypeName;
		// }
		//                        
		// },
		// failure : function() {
		// Ext.Msg.alert(Constants.ERROR, Constants.OPERATE_ERROR_MSG);
		// }
		// }, 'workticketNo=' + workticketNo);
		//                    
		//                    
		//                
		//                
		//                
		// },
		// failure : function() {
		// btnModify.setVisible(false);
		// Ext.Msg.alert(Constants.ERROR, Constants.OPERATE_ERROR_MSG);
		// }
		// }, 'workticketNo=' + workticketNo);

	}

	// 画面恢复到初始没有数据的状态
	function pageNoDataInitial() {

		baseInfoFormPanel.getForm().reset();
		Ext.get("workticketBaseInfo.workticketNo").dom.value = '';

		// 工作条件
		cbxWorkCondition.disable();
		// 动火票级别
		cbxFireLevel.disable();
		// 工作票种类
		cbxWorkticketType.enable();
		// 工作票来源
		cbxWorkticketSource.enable();
		// 检修专业
		cbxRepairSpecail.enable();
		// 控制修改按钮显示
		btnModify.setVisible(false);
		myDangerId = "";
		topbar.setDisabled(false);

	}

	// 工作票种类选择后，联动处理
	function workticketTypeSelected() {

		if (register.workticketNo) {
			return;
		}

		var ticketTypeCode = "";
		ticketTypeCode = Ext.get("workticketBaseInfo.workticketTypeCode").dom.value;

		// 工作条件
		cbxWorkCondition.setValue("");
		cbxWorkCondition.disable();

		// 动火票级别
		cbxFireLevel.clearValue();
		cbxFireLevel.disable();
		// 需要退出热工保护或自动装置名称 add by ywliu 090508
		autoDeviceName.disable();
		if (ticketTypeCode == "") {
			return;
		} else {
			if (ticketTypeCode == "1") {
				// 选择“电气一种票”时
			} else if (ticketTypeCode == "2") {
				// 选择“电气二种票“时
				cbxWorkCondition.enable();
			} else if (ticketTypeCode == "3") {
				// 选择“热力机械票”时
			} else if (ticketTypeCode == "4") {
				// 选择“动火工作票”时
				cbxFireLevel.enable();
			} else if (ticketTypeCode == "5") {
				// 选择“热控工作票”时
				cbxWorkCondition.enable();
				// 需要退出热工保护或自动装置名称 add by ywliu 090508
				autoDeviceName.enable();
			}
		}
	}

	// textField隐藏
	function hideField(field) {
		field.disable();
		field.hide();
		// hide label
		field.getEl().up('.x-form-item').setDisplayed(false);
	}

	// textField显示
	function showField(field) {
		field.enable();
		field.show();
		// show label
		field.getEl().up('.x-form-item').setDisplayed(true);
	}

	// 时间去t处理
	function renderDate(value) {
		if (!value)
			return "";
		var reDate = /\d{4}\-\d{2}\-\d{2}/gi;
		var reTime = /\d{2}:\d{2}:\d{2}/gi;
		var strDate = value.match(reDate);
		var strTime = value.match(reTime);
		if (!strDate)
			return "";
		strTime = strTime ? strTime : '00:00:00';
		return strDate + " " + strTime;
	}

	// textField显示时间比较方法
	function compareDate(argDate1, argDate2) {
		return argDate1.getTime() >= argDate2.getTime();
	}

	// textField显示时间比较方法
	function compareDateStr(argDateStr1, argDateStr2) {
		var date1 = Date.parseDate(argDateStr1, 'Y-m-d H:i:s');
		var date2 = Date.parseDate(argDateStr2, 'Y-m-d H:i:s');
		return compareDate(date1, date2);
	}

	// 是否紧急票判断
	function isEmergencyCheck() {
		if (isEmergency.checked) {
			Ext.Msg.confirm(Constants.NOTICE, Constants.IS_EMERGENCY, function(
					buttonobj) {
				if (buttonobj != "yes") {
					// 不是紧急票
					isEmergency.checked = false;
					isEmergency.reset();
				}
			});
		}
	}

	// 画面入力检查
	function checkInput() {
		// 工作票种类
		var ticketType = Ext.get("workticketTypeCode").dom.value;
		// 工作票来源
		var ticketSource = Ext.get("sourceId").dom.value;
		// 检修专业
		var repairSpecail = Ext.get("repairSpecailCode").dom.value;
		// 所属机组或系统
		var equAttribute = Ext.get("equAttributeCode").dom.value;
		// 接收专业
		var permissionDept = Ext.get("permissionDept").dom.value;
		if (cbxWorkCondition.disabled == false) {
			// 工作条件
			var condition = Ext.get("conditionName").dom.value;
		}

		// 弹出信息
		var strMsg = "你还需要填入下列项目,然后才能保存:";
		var msg = "";
		if (ticketType == '') {
			msg += "{工作票种类},";
		}
		if (ticketSource == '') {
			msg += "{工作票来源},";
		}
		if (repairSpecail == '') {
			msg += "{检修专业},";
		}

		if (permissionDept == '') {
			msg += "{接收专业},";
		}
		if (!cbxWorkCondition.disabled && condition == '') {
			msg += "{工作条件},";
		}
		if (equAttribute == '') {
			msg += "{所属机组或系统},";
		}
		if (locationName.getValue() == "") {
			msg += "{工作地点}";
		}
		if (Ext.get("workticketBaseInfo.workticketTypeCode").dom.value == 4
				&& cbxFireLevel.getValue() == "") {
			msg += "{动火票级别}";
		}
		if (msg != '') {
			Ext.Msg.alert(Constants.NOTICE, strMsg + msg);
			return false;
		}
		return true;
	}

	// 增加时，下一步按钮创建一条新的工作票记录
	function nextStep() {
		if (Ext.get("hideWorkticketNo").dom.value == "") {
			addRecord();
		} else {
			nextTab();
		}
	}

	// 修改时，修改按钮修改工作票的基本信息
	function updateWorkticket() {
		Ext.get("workticketBaseInfo.dangerCondition").dom.value = dangerCondition
				.getValue();
		if (checkInput()) {

			baseInfoFormPanel.getForm().submit({
				method : Constants.POST,
				params : {
					file : "",
					dangerId : myDangerId
				},
				url : 'workticket/updateDetailWorkticketBaseInfo.action',
				success : function(form, action) {
					// 设置所属机组或系统
					register.equAttributeCode = Ext.getCmp('equAttributeCode')
							.getValue();
					// 更新上报列表
					register.updateReportList();
					// register.refreshDanger();
					var o = eval("(" + action.response.responseText + ")");
					Ext.Msg.alert(Constants.NOTICE, o.msg);
					myDangerId = "";
				},
				faliue : function() {
					Ext.Msg.alert(Constants.ERROR, Constants.UNKNOWN_ERR);
				}
			});
		}
	}

	// 增加一条工作票信息
	function addRecord() {
		// 危险点内容可写
		Ext.get("workticketBaseInfo.dangerCondition").dom.value = dangerCondition
				.getValue();
		if (checkInput()) {

			baseInfoFormPanel.getForm().submit({
				method : Constants.POST,
				params : {
					file : "",
					isStandard : 'Y', // 标准票
					dangerId : myDangerId,
					existWorkticketNo : existWorkticketNo
				},
				url : 'workticket/addDetailWorkticketBaseInfo.action',
				success : function(form, action) {

					var o = eval("(" + action.response.responseText + ")");
					var strWorkticketNo = o.workticketNo;
					// 成功后画面上显示工作票编号
					Ext.get('workticketNo').dom.value = strWorkticketNo;
					Ext.get('workticketBaseInfo.workticketNo').dom.value = strWorkticketNo;
					// 成功后修改按钮出现
					btnModify.setVisible(true);
					topbar.setDisabled(true);
					// 工作票种类，来源，检修专业不能修改
					cbxWorkticketType.disable();
					cbxFireLevel.disable();
					// cbxWorkticketSource.disable();
					// cbxRepairSpecail.disable();
					myDangerId = "";
					// 从第一个页面迁移到第二个页面
					nextTab();
				},
				faliue : function() {
					Ext.Msg.alert(Constants.ERROR, Constants.UNKNOWN_ERR);
				}
			});
		}
	}

	// 修改时，下一步按钮激活下一个tab页
	function nextTab() {
		// 设置工作票编号
		register.workticketNo = Ext.get('workticketNo').dom.value;

		// 设置所属机组或系统
		register.equAttributeCode = Ext.getCmp('equAttributeCode').getValue();
		// 设置工作票种类/工作票类型编码
		register.workticketTypeCode = Ext
				.get('workticketBaseInfo.workticketTypeCode').dom.value;
		// 迁移到下一个页面
		register.locationName = locationName.getValue();
		register.toNext();
	}
	getPageData(register.workticketNo)
});