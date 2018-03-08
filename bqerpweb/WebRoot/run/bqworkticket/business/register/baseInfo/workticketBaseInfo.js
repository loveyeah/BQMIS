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
// var tempContentStoreData = null;
var tempMemberStoreData = null;
Ext.onReady(function() {

	var existWorkticketNo = "";

	var register = parent.Ext.getCmp('tabPanel').register;
	register.loadRecord = getPageData;
	register.addRecord = pageNoDataInitial;
	var myDangerId = "";// 工作票危险点内容id
	var standticketNo = "";

	// add by fyyang 090216
	// 缺陷单号 ，当工作票来源是缺陷时缺陷单号可填
	var flag = "N";
	Ext.Ajax.request({
		url : 'comm/getPamValue.action',
		method : 'post',
		params : {
			pamNo : 'ISAUTO'
		},
		success : function(result, request) {
			flag = result.responseText;
			if (flag == "N") {
				applyNo.setDisabled(true);
			}
		},
		failure : function(result, request) {
			Ext.Msg.alert('提示信息', '操作失败！')
		}
	})
	var failureCode = new Ext.form.ComboBox({
		fieldLabel : '缺陷单号',
		name : 'workticketBaseInfo.failureCode',
		id : 'failureCode',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		mode : 'remote',
		hiddenName : 'workticketBaseInfo.failureCode',
		editable : false,
		triggerAction : 'all',
		readOnly : true,
		onTriggerClick : function() {
			if (!failureCode.disabled) {
				var url = "../../../../../equ/bqfailure/query/failureSelect.jsp";
				var obj = window
						.showModalDialog(
								url,
								null,
								'dialogWidth:700px;dialogHeight:500px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
				// var no = window
				// .showModalDialog(
				// url,
				// null,
				// 'dialogWidth:700px;dialogHeight:500px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
				if (obj != null) {
					var no = obj.ro;
					var failcontent=obj.failcontent;
					var content=txtWorkContent.getValue()
					if (no != null) {
						failureCode.setValue(no);
						if(failcontent!=null){
							txtWorkContent.setValue(content+failcontent+"消缺");
						}					
					}
				}

			}
		},
		anchor : "85%",
		disabled : true
	});

	// 需要退出热工保护或自动装置名称
	var autoDeviceName = new Ext.form.TextArea({
		id : 'autoDeviceName',
		fieldLabel : "需要退出热工保护或自动装置名称",
		// xtype : 'textarea',
		name : 'workticketBaseInfo.autoDeviceName',
		value : '',
		height : 50,
		anchor : "95%",
		disabled : true
	});

	// 工作位置/地点
	var locationName = new Ext.form.TriggerField({
		fieldLabel : '工作地点',
		id : "locationName",
		name : "workticketBaseInfo.locationName",
		allowBlank : false,
		onTriggerClick : locationSelect,
		anchor : "85%"
	});

	/** 区域选择处理 */
	function locationSelect() {
		if (cbxChargeBySystem.getValue() == "") {
			Ext.Msg.alert("提示", "请选择所属机组或区域");
			return;
		}
		var url = "selectLocation.jsp?op=many&blockCode="
				+ Ext.get("workticketBaseInfo.equAttributeCode").dom.value;
		var location = window.showModalDialog(url, window,
				'dialogWidth=700px;dialogHeight=500px;status=no');
		if (typeof(location) != "undefined") {
			// 设置设备名
			locationName.setValue(location.name);
		}
	}

	// //隐藏字段
	// //工单号
	// var woCode = new Ext.form.Hidden({
	// fieldLabel : '工单号',
	// name : 'workticketBaseInfo.woCode',
	// // xtype : 'combo',
	// id : 'woCode',
	// store : new Ext.data.SimpleStore({
	// fields : ['id', 'name'],
	// data : [[]]
	// }),
	// mode : 'remote',
	// //hiddenName : 'workticketBaseInfo.dangerCondition',
	// //editable : false,
	// triggerAction : 'all',
	// // readOnly : true,
	// onTriggerClick :function(){ },
	// anchor : "85%",
	// disabled : true
	// });
	//	
	//	
	//	
	//	
	// 任务单号
	var applyNo = new Ext.form.ComboBox({
		fieldLabel : '任务单号',
		name : 'workticketBaseInfo.applyNo',
		// xtype : 'combo',
		id : 'applyNo',
		store : new Ext.data.SimpleStore({
			fields : ['id', 'name'],
			data : [[]]
		}),
		allowBlank : false,
		mode : 'remote',
		// hiddenName : 'workticketBaseInfo.dangerCondition',
		// editable : false,
		triggerAction : 'all',
		// readOnly : true,
		onTriggerClick : function() {
			if (Ext.get('applyNo').dom.disabled) {
			} else {
				var args = new Object();
				args.typeCode = "1";
				var url = "/power/comm/taskselect/taskListSelect.jsp";
				var no = window.showModalDialog(url, args,
						'dialogWidth=700px;dialogHeight=500px;status=no');
				if (typeof(no) != "undefined") {
					applyNo.setValue(no.taskNo);
				}
			}

		},
		anchor : "85%"
	});

	// ------add by fyyang --------
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
		anchor : "85%",
		onTriggerClick : function() {
		}
	});

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

		anchor : "85%"
	});

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
							'dialogWidth=700px;dialogHeight=400px;center=yes;help=no;resizable=no;status=no;');
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
	// ----------------------------

	// 工作票编号
	var txtWorkticketNo = new Ext.form.TextField({
		id : 'workticketNo',
		fieldLabel : "工作票编号",
		xtype : 'textfield',
		value : '',
		emptyText : Constants.AUTO_CREATE,
		readOnly : true,
		anchor : "85%"
	});
	// 是否紧急要
	// var isEmergency = new Ext.form.Checkbox({
	var isEmergency = new Ext.form.Hidden({
		id : 'isEmergency',
		boxLabel : "是否紧急票",
		hideLabel : true,
		anchor : "95%",
		listeners : {
			check : isEmergencyCheck
		}
	})
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
		anchor : "85%",
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
		anchor : "85%",
		listeners : {
			select : workticketSourceSelected
		}
	})

	function workticketSourceSelected() {
		if (cbxWorkticketSource.getValue() == "1") {
			// 来源是缺陷时
			failureCode.enable();
			applyNo.setDisabled(true);
		} else {
			failureCode.disable();
			applyNo.setDisabled(false);
		}

	}

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
		anchor : "85%"
	})
	// 工作负责人
	var triggerChargeBy = new Ext.form.TriggerField({
		id : 'chargebyname',
		fieldLabel : "工作负责人<font color='red'>*</font>",
		onTriggerClick : chargeBySelect,
		value : '',
		readOnly : true,
		anchor : "85%"
	})
	// 监工
	var txtSupervisor = new Ext.form.TriggerField({
		// var txtSupervisor = new Ext.form.Hidden({
		id : 'watchername',
		fieldLabel : "工作监护人<font color='red'>*</font>",
		value : '',
		onTriggerClick : supervisorSelect,
		readOnly : true,
		anchor : "85%"
	})
	// ---------add by fyyang 090523----------
	// 总人数
	var totalCount = new Ext.form.TextField({
		id : "workticketBaseInfo.memberCount",
		name : "workticketBaseInfo.memberCount",
		fieldLabel : "总人数",
		readOnly : true,
		value : 1,
		labelAlign : 'left',
		width : 40
	});
	// 工作班成员
	var workMembers = new Ext.form.TriggerField({
		id : 'workticketBaseInfo.members',
		name : 'workticketBaseInfo.members',
		fieldLabel : "工作班成员<font color='red'>*</font>",
		// emptyText:'人员名称以逗号分隔',
		readOnly : false,
		allowBank:false,
		onTriggerClick : selectWorkMember,
		height : 50,
		anchor : "80%"
	});
	workMembers.on("change", function() {
		var members = workMembers.getValue();
		totalCount.setValue(members.split(",").length + 1);
	});
	// 选择工作班成员
	function selectWorkMember() {
		var workCharger = Ext.get("workticketBaseInfo.chargeBy").dom.value;

		if (workCharger == "") {
			Ext.Msg.alert("提示", "请先选择工作负责人!");
			return;
		}
		var url = "workticketMemberSelectWindow.jsp";
		var args = new Object();
		args.workticketNo = txtWorkticketNo.getValue();
		args.chargeDept = Ext.get('workticketBaseInfo.chargeDept').dom.value;
		args.storeData = tempMemberStoreData;
		this.blur();
		var selectedMembers = window
				.showModalDialog(
						url,
						args,
						'dialogWidth=700px;dialogHeight=520px;center=yes;help=no;resizable=no;status=no;');

		if (typeof(selectedMembers) != "undefined") {
			workMembers.setValue(selectedMembers.names);
			totalCount.setValue(selectedMembers.quantity);
			tempMemberStoreData = selectedMembers.storeData;
		}
	}

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
			//add by fyyang 090729
			if (Ext.get("workticketBaseInfo.workticketTypeCode").dom.value == "4")
			{
				if(Ext.get("workticketBaseInfo.firelevelId").dom.value=="")
				{
					Ext.Msg.alert("提示", "请先选择动火级别！");
				    return;
				}
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
	// 所属部门
	var txtChargeDept = new Ext.form.TextField({
		id : 'deptname',
		fieldLabel : "所属班组",
		value : '',
		readOnly : true,
		disabled : true,
		anchor : "85%"
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
		anchor : "85%"
	})
	// 工作条件
	var cbxWorkCondition = new Ext.form.TextField({
		id : 'conditionName',
		fieldLabel : "工作条件",
		// xtype : 'textfield',
		name : 'workticketBaseInfo.conditionName',
		value : '',
		disabled : true,
		anchor : "85%"
	});

	// 所属机组或区域
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
	storeChargeBySystem.load();
	// storeChargeBySystem.load();
	var cbxChargeBySystem = new Ext.form.ComboBox({
		id : 'equAttributeCode',
		fieldLabel : "所属机组或区域<font color='red'>*</font>",
		store : storeChargeBySystem,
		displayField : "blockName",
		valueField : "blockCode",
		hiddenName : 'workticketBaseInfo.equAttributeCode',
		mode : 'local',
		triggerAction : 'all',
		value : '',
		readOnly : true,
		anchor : "85%"
			// ,
			// listeners : {
			// select : function() {
			// locationName.setValue(" ");
			// }
			// }
	});
	// 计划开始时间
	var dfPlanStartDate = new Ext.form.TextField({
		id : 'planStartDate',
		fieldLabel : "计划开始时间",
		name : 'workticketBaseInfo.planStartDate',
		style : 'cursor:pointer',
		anchor : "85%",
		value : getDate(),
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M-%d 00:00:00',
					dateFmt : 'yyyy-MM-dd HH:mm:ss',
					alwaysUseStartDate : true,
					onclearing : function() {
						alert(' 不允许清空!');
						return true;
					}
				});
			}
		}
	})
	// 计划结束时间
	var dfPlanEndDate = new Ext.form.TextField({
		id : 'planEndDate',
		fieldLabel : "计划结束时间",
		name : 'workticketBaseInfo.planEndDate',
		style : 'cursor:pointer',
		anchor : "85%",
		value : getDate(),
		listeners : {
			focus : function() {
				var pkr = WdatePicker({
					startDate : '%y-%M-%d 00:00:00',
					dateFmt : 'yyyy-MM-dd HH:mm:ss',
					alwaysUseStartDate : true,
					onclearing : function() {
						alert(' 不允许清空!');
						return true;
					}
				});
			}
		}
	})
	// 备注
	var txtRemark = {
		id : 'workticketMemo',
		fieldLabel : "备注",
		xtype : 'textarea',
		name : 'workticketBaseInfo.workticketMemo',
		value : '',
		height : 50,
		anchor : "95%"
	}
	// 关联主票
	var txtRefWorkticketNo = new Ext.form.TriggerField({
		id : 'refWorkticketNo',
		fieldLabel : "关联主票",
		name : 'workticketBaseInfo.refWorkticketNo',
		value : '',
		onTriggerClick : refWorkticketNoSelect,
		disabled : true,
		anchor : "85%"
	})
	// 动火执行人
	// 隐藏动火执行人 add by fyyang 090306
	// var txtFireticketExecuteBy = new Ext.form.TriggerField({
	var txtFireticketExecuteBy = new Ext.form.Hidden({
		id : 'firetickerexecutebyname',
		fieldLabel : '动火执行人',
		value : '',
		onTriggerClick : FireticketExecuteBySelect,
		disabled : true,
		readOnly : true,
		anchor : "85%"
	})
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
		disabled : false,
		value : '',
		readOnly : true,
		anchor : "85%"
	})
	// 选择图片
	var imagephoto = new Ext.form.TextField({
		id : "workticketMap",
		fieldLabel : '选择图片',
		height : 160,
		name : 'workticketBaseInfo.workticketMap',
		autoCreate : {
			tag : 'input',
			type : 'image',
			src : 'comm/images/powererp.jpg',
			style : 'filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale);',
			name : 'imagephoto'
		},
		inputType : 'image',
		anchor : "85%"
	});
	// 图片
	var photo = new Ext.form.TextField({
		id : "photo",
		inputType : 'file',
		hideLabel : true,
		hidden : true,
		name : 'photo',
		width : 300,
		height : 22,
		listeners : {
			show : function() {
				baseInfoField.setHeight(590)
			},
			hide : function() {
				baseInfoField.setHeight(440);
			}
		}
	});
	// 修改
	var btnModify = new Ext.Button({
		text : Constants.BTN_UPDATE,
		iconCls : 'update',
		hidden : true,
		handler : updateWorkticket

	})
	// 下一步
	var btnNextStep = new Ext.Button({
		text : Constants.NEXT_STEP,
		iconCls : 'nextStep',
		handler : nextStep
	})
	// modify by fyyang
	var bbar = new Ext.Toolbar({
		items : [new Ext.Toolbar.Fill(), btnModify, btnNextStep]
	});
	var cbxMainEqu = new Ext.form.TriggerField({
		fieldLabel : "主设备<font color='red'>*</font>",
		id : "mainEqu",
		name : "workticketBaseInfo.mainEquName",
		onTriggerClick : mainEquSelect,
		anchor : "85%"
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

	var baseInfoField = new Ext.form.FieldSet({
		// height : 440,
		autoHeight : true,
		style : {
			"padding-top" : '5'
		},
		bodyStyle : Ext.isIE ? 'padding:0 0 5px 15px;' : 'padding:10px 15px;',
		anchor : '-20',
		border : false,
		buttonAlign : 'center',
		items : [
				// 工作票编号
				{
					layout : "column",
					border : false,
					items : [{
						columnWidth : 0.5,
						layout : "form",
						border : false,
						items : [{
							id : 'hideWorkticketNo',
							name : 'workticketBaseInfo.workticketNo',
							value : '',
							xtype : 'hidden'
						}, txtWorkticketNo]
					}, {
						columnWidth : 0.5,
						layout : "form",
						border : false,
						items : [{
							id : 'hideMainEquCode',
							name : 'workticketBaseInfo.mainEquCode',
							value : '',
							xtype : 'hidden'
						}, cbxMainEqu
						// {
						// name : 'workticketBaseInfo.isEmergency',
						// xtype : 'hidden'
						// }, isEmergency
						]
					}]
				},
				// 工作票种类和检修专业
				{
					layout : "column",
					border : false,
					items : [{
						columnWidth : 0.5,
						layout : "form",
						border : false,
						items : [cbxWorkticketType]
					}, {
						columnWidth : 0.5,
						layout : "form",
						border : false,
						items : [cbxRepairSpecail]
					}]
				},
				// 工作票来源failureCode
				{
					layout : "column",
					border : false,
					items : [{
						columnWidth : 0.5,
						layout : "form",
						border : false,
						items : [cbxWorkticketSource]
					}, {
						columnWidth : 0.5,
						layout : "form",
						border : false,
						items : [failureCode]
					}]
				},

				// 工作负责人
				{
					layout : "column",
					border : false,
					items : [{
						columnWidth : 0.5,
						layout : "form",
						border : false,
						items : [{
							id : 'chargeBy',
							xtype : 'hidden',
							name : 'workticketBaseInfo.chargeBy'
						}, triggerChargeBy]
					}, {
						columnWidth : 0.5,
						layout : "form",
						border : false,
						items : [{
							id : 'watcher',
							xtype : "hidden",
							name : 'workticketBaseInfo.watcher'
						}, {
							id : 'chargeDept',
							xtype : 'hidden',
							name : 'workticketBaseInfo.chargeDept'
						}, txtChargeDept]
					}]
				},
				// 监护人 任务单号
				{
					layout : "column",
					border : false,
					items : [{
						columnWidth : 0.5,
						layout : "form",
						border : false,
						items : [txtSupervisor]
					}, {
						columnWidth : 0.5,
						layout : "form",
						border : false,
						items : [applyNo]
					}]
				},

				// 接收专业 所属机组或系统
				{
					layout : "column",
					border : false,
					items : [{
						columnWidth : 0.5,
						layout : "form",
						border : false,
						items : [cbxReceiveSpecail]
					}, {
						columnWidth : 0.5,
						layout : "form",
						border : false,
						items : [cbxWorkCondition]
					}]
				},
				// 工作条件 工作地点
				{
					layout : "column",
					border : false,
					items : [{
						columnWidth : 0.5,
						layout : "form",
						border : false,
						items : [cbxChargeBySystem]
					}, {
						columnWidth : 0.5,
						layout : "form",
						border : false,
						items : [locationName]
					}]
				},

				// 危险点
				{
					layout : "column",
					border : false,
					items : [{
						columnWidth : 0.5,
						layout : "form",
						border : false,
						items : [dangerCondition]
					}, {
						columnWidth : 0.5,
						layout : "form",
						border : false,
						items : [dangerType]
					}]
				},
				// 计划开始时间
				{
					layout : "column",
					border : false,
					items : [{
						columnWidth : 0.5,
						layout : "form",
						border : false,
						items : [dfPlanStartDate]
					}, {
						columnWidth : 0.5,
						layout : "form",
						border : false,
						items : [dfPlanEndDate]
					}]
				},
				// 工作班成员
				{
					layout : "column",
					border : false,
					items : [{
						columnWidth : 0.6,
						layout : "form",
						border : false,
						items : [workMembers]
					}, {
						columnWidth : 0.4,
						layout : 'form',
						// border: true,
						items : [totalCount]
					}]

				},
				// //需要退出热工保护或自动装置名称
				{
					layout : "column",
					border : false,
					items : [{
						columnWidth : 0.97,
						layout : "form",
						border : false,
						items : [autoDeviceName]
					}]
				},
				// 工作内容
				{
					layout : "column",
					border : false,
					items : [{
						columnWidth : 0.77,
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

				},
				// 备注
				{
					layout : "column",
					border : false,
					items : [{
						columnWidth : 0.97,
						layout : "form",
						border : false,
						items : [txtRemark]
					}]
				},
				// // 申请单号
				// {
				// layout : "column",
				// border : false,
				// items : [{
				// columnWidth : 0.5,
				// layout : "form",
				// border : false,
				// items : [applyNo]
				// }]
				// },
				// 关联主票
				{
					layout : "column",
					border : false,
					items : [{
						columnWidth : 0.5,
						layout : "form",
						border : false,
						items : [txtRefWorkticketNo]
					}, {
						columnWidth : 0.5,
						layout : "form",
						border : false,
						items : [cbxFireLevel]
					}]
				},
				// 动火执行人
				{
					layout : "column",
					border : false,
					items : [{
						columnWidth : 0.5,
						layout : "form",
						border : false,
						items : [{
							id : 'fireticketExecuteBy',
							xtype : 'hidden',
							name : 'workticketBaseInfo.fireticketExecuteBy'
						}, txtFireticketExecuteBy]
					}]
				},

				// 选择图片
				{
					layout : "column",
					border : false,
					items : [{
						columnWidth : 0.5,
						layout : "form",
						border : false,
						items : [imagephoto]
					}, {
						columnWidth : 0.4,
						layout : "form",
						border : false,
						items : [photo]

					}]
				}]
			// buttons : [btnModify, btnNextStep]
	});

	var baseInfoFormPanel = new Ext.FormPanel({
		region : 'center',
		// height : 500,
		border : false,
		frame : true,
		fileUpload : true,
		bbar : bbar,
		items : [baseInfoField],
		bodyStyle : {
			'padding-top' : '5px'
		},
		defaults : {
			labelAlign : 'right'
		},
		listeners : {
			render : formInitial
		}
	});
	// 页面初始化处理
	function formInitial() {

		// 图片选择后，显示图片
		baseInfoFormPanel.getForm().findField('photo').on('render', function() {

			Ext.get('photo').on('change', function(field, newValue, oldValue) {

				var url = Ext.get('photo').dom.value;
				var image = Ext.get('imagephoto').dom;
				if (Ext.isIE7) {
					image.src = Ext.BLANK_IMAGE_URL;// 覆盖原来的图片
					image.filters
							.item("DXImageTransform.Microsoft.AlphaImageLoader").src = url;
				} else {
					Ext.get('imagephoto').dom.src = url;
				}
			});
		});
	}

	// 底部工具条
	var topbar = new Ext.Toolbar({
		region : 'north',
		// height : 100,
		items : [new Ext.Toolbar.Fill(), {
			id : 'btnCreateByStandard',
			text : '调用标准票',
			handler : function() {
				
					if (Ext.get("workticketBaseInfo.workticketTypeCode").dom.value == "") {
				Ext.Msg.alert("提示", "请先选择工作票类型！");
				return;
			}
			//add by fyyang 090729
			if (Ext.get("workticketBaseInfo.workticketTypeCode").dom.value == "4")
			{
				if(Ext.get("workticketBaseInfo.firelevelId").dom.value=="")
				{
					Ext.Msg.alert("提示", "请先选择动火级别！");
				    return;
				}
			}
		
				
				
				var object = window
						.showModalDialog(
								'../createByStandard/standardSelect.jsp',
								{
									mainEquCode : Ext.getCmp("hideMainEquCode")
											.getValue(),
									mainEquName : cbxMainEqu.getValue(),
									fireLevel:Ext.get("workticketBaseInfo.firelevelId").dom.value,
									workticketTypeCode: Ext.get("workticketBaseInfo.workticketTypeCode").dom.value
								},
								'dialogWidth=800px;dialogHeight=400px;center=yes;help=no;resizable=no;status=no;');
				if (typeof(object) == 'object') {
					standticketNo = object.standticketNo;
					register.workticketNo = standticketNo;
					getDataByStandard(object.standticketNo);
					register._checkWorkticketNo();
				}

			}
		}]
	});

	// 由标准票生成时初始化数据
	function getDataByStandard(workticketNo) {
		getPageData(workticketNo);

		// pageNoDataInitial();
		// var dangertypeid="";
		// Ext.lib.Ajax.request('POST',
		// 'workticket/getDetailWorkticketBaseInfoByNo.action', {
		// success : function(result, request) {
		// var record = eval('(' + result.responseText + ')');
		// baseInfoFormPanel.getForm().loadRecord(record);
		//                     
		// Ext.get("workticketNo").dom.value="自动生成";
		// // 工作票种类，来源，检修专业不能修改
		// cbxWorkticketType.disable();
		// cbxWorkticketSource.disable();
		// cbxRepairSpecail.disable();
		// cbxReceiveSpecail.disable(); //接收专业
		// cbxChargeBySystem.disable(); //所属机组或系统
		// cbxWorkCondition.disable();
		// locationName.disable();
		// dangerType.disable();
		// dangerCondition.disable();
		// // 根据工作票种类来判断哪些是可用的
		// workticketTypeSelected();
		// // 动火级别
		// if (record.data.firelevelId != null) {
		// Ext.get('firelevelId').dom.value = record.data.firelevelId;
		// }
		//                   
		//
		// var ticketTypeCode=cbxWorkticketType.getValue();
		// if (ticketTypeCode == "1") {
		// // 选择“电气一种票”时
		// if (photo.hidden) {
		// showField(photo);
		// }
		// showField(imagephoto);
		// } else if (ticketTypeCode == "2") {
		// // 选择“电气二种票“时
		// cbxWorkCondition.enable();
		// } else if (ticketTypeCode == "3") {
		// // 选择“热力机械票”时
		// } else if (ticketTypeCode == "4") {
		// // 选择“动火工作票”时
		// txtRefWorkticketNo.enable();
		// txtFireticketExecuteBy.enable();
		// cbxFireLevel.enable();
		// } else if (ticketTypeCode == "5") {
		// // 选择“热控工作票”时
		// //update by drdu 090305
		// // cbxWorkCondition.enable();
		// //add by fyyang
		// autoDeviceName.enable();
		// }
		//                   
		// //-----设置时间默认值-----
		// dfPlanStartDate.setValue(getDate());
		// dfPlanEndDate.setValue(getDate());
		// //--------缺陷来源-----
		// if(cbxWorkticketSource.getValue()=="1")
		// {
		// failureCode.enable();
		// }
		// else
		// {
		// failureCode.disable();
		// }
		// // 设置所属机组或系统
		// register.equAttributeCode =
		// Ext.getCmp('equAttributeCode').getValue();
		// // 设置工作票种类/工作票类型编码
		// register.workticketTypeCode =
		// Ext.getCmp('workticketTypeCode').getValue();
		// //add----
		// if(record.data.dangerType!=null)
		// {
		// dangertypeid=record.data.dangerType;
		// }
		// // 汉字姓名,部门名称读取
		// Ext.lib.Ajax.request('POST',
		// 'workticket/getDetailTicketInfoByCode.action', {
		// success : function(result, request) {
		// var record = eval('(' + result.responseText + ')');
		//                       
		// if(record.data.dangerTypeName!=null)
		// {
		// Ext.getCmp('dangerType').setValue(dangertypeid);
		// Ext.form.ComboBox.superclass.setValue.call(Ext
		// .getCmp('dangerType'),record.data.dangerTypeName);
		// }
		//                        
		// },
		// failure : function() {
		// Ext.Msg.alert(Constants.ERROR, Constants.OPERATE_ERROR_MSG);
		// }
		// }, 'workticketNo=' + workticketNo);
		// //----------
		//                    
		// },
		// failure : function() {
		// btnModify.setVisible(false);
		// Ext.Msg.alert(Constants.ERROR, Constants.OPERATE_ERROR_MSG);
		// }
		// }, 'workticketNo=' + workticketNo);

	}

	var baseInfoViewport = new Ext.Viewport({
		// height : 500,
		layout : "border",
		border : false,
		items : [topbar, baseInfoFormPanel],
		defaults : {
			autoScroll : true
		}
	}).show();

	// ---------------add by fyyang 090523-----------------

	// txtWorkContent.getEl().on("dblclick",function(){
	// var args = new Object();
	// var storeData = null;
	//		
	// if(txtWorkticketNo.getValue() == null || txtWorkticketNo.getValue() ==
	// "")
	// {
	// storeData = tempContentStoreData;
	// }
	// args.storeData = storeData;
	// args.workticketNo = txtWorkticketNo.getValue();
	// args.workticketTypeCode = cbxWorkticketType.getValue();
	// args.locationName = locationName.getValue();
	// args.blockCode = cbxChargeBySystem.getValue();
	// alert(storeData);
	// alert( txtWorkticketNo.getValue());
	// var ro =
	// window.showModalDialog('workticketContent.jsp',args,'dialogWidth=750px;dialogHeight=400px;center=yes;help=no;resizable=no;status=no;');
	// if(typeof(ro) !="undefined")
	// {
	// txtWorkContent.setValue(ro.str);
	// tempContentStoreData =ro.storeData;
	//		
	//									
	// }
	// });
	// --------------------add end---------------------------

	// 页面初始化为修改时，页面数据读取
	function getPageData(workticketNo) {
		pageNoDataInitial();
		topbar.setDisabled(true);
		var dangertypeid = "";
		Ext.lib.Ajax.request('POST',
				'workticket/getDetailTicketInfoByCode.action', {
					success : function(result, request) {
						var record = eval('(' + result.responseText + ')');
						// -------------add by fyyang ----------------------
						// 工作班成员,及总数

						workMembers.setValue(record.data.model.members);
						totalCount.setValue(record.data.model.memberCount);
						txtWorkContent
								.setValue(record.data.model.workticketContent);
						// ----------------------------------------------------
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
						// 缺陷单号
						if (record.data.model.failureCode != null
								&& record.data.model.failureCode != "null") {
							failureCode.setValue(record.data.model.failureCode);
						}
						// 工作负责人
						Ext.get('workticketBaseInfo.chargeBy').dom.value = record.data.model.chargeBy;
						Ext.get("chargebyname").dom.value = record.data.chargeByName;
						// 监工
						if (record.data.model.watcher != null) {
							Ext.get('workticketBaseInfo.watcher').dom.value = record.data.model.watcher;
							Ext.get('watchername').dom.value = record.data.watcherName;
						}
						// 所属部门
						Ext.get('workticketBaseInfo.chargeDept').dom.value = record.data.model.chargeDept;
						Ext.get('deptname').dom.value = record.data.deptName;
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
						// 任务单号
						if (record.data.model.applyNo != null) {
							applyNo.setValue(record.data.model.applyNo);

						}
						// if (record.data.model.mainEquCode != null) {
						Ext.getCmp("hideMainEquCode")
								.setValue(record.data.model.mainEquCode);
						cbxMainEqu.setValue(record.data.model.mainEquName);
						// }

						// 备注 workticketMemo
						if (record.data.model.workticketMemo != null) {
							Ext.get("workticketMemo").dom.value = record.data.model.workticketMemo;
						}
						// 时间格式化
						Ext.get("planStartDate").dom.value = renderDate(record.data.model.planStartDate);
						Ext.get("planEndDate").dom.value = renderDate(record.data.model.planEndDate);
						// //动火执行人
						// Ext.get('workticketBaseInfo.fireticketExecuteBy').dom.value
						// = record.data.model.fireticketExecuteBy;
						// if (record.data.fireTickerExeByName != null) {
						// Ext.get('firetickerexecutebyname').dom.value =
						// record.data.fireTickerExeByName;
						// }

						// -----------显示处理--------------------
						// 缺陷单号是否可用
						if (Ext.get("workticketBaseInfo.sourceId").dom.value == 1) {
							failureCode.enable();
						} else {
							failureCode.disable();
						}

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
						// 需要退出热工保护或自动装置名称
						if (record.data.model.autoDeviceName != null) {
							autoDeviceName
									.setValue(record.data.model.autoDeviceName);
						}
						// 关联主票 txtRefWorkticketNo
						if (record.data.model.refWorkticketNo != null
								&& record.data.model.refWorkticketNo != 0) {
							txtRefWorkticketNo
									.setValue(record.data.model.refWorkticketNo);
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
						// 工作票种类，工作负责人不能修改
						cbxWorkticketType.disable();
						// cbxRepairSpecail.disable();
						triggerChargeBy.disable();
						txtChargeDept.disable();
						//cbxFireLevel.disable();

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
						register.isStandard = record.data.model.isStandard;
						register.isCreateByStandard = record.data.model.isCreatebyStand;
						// 如果是由标准票生成
						if (record.data.model.isCreatebyStand == "Y"
								|| record.data.model.isStandard == "Y") {

							cbxWorkticketType.disable();
							// cbxWorkticketSource.disable();
							// cbxRepairSpecail.disable();
							// cbxReceiveSpecail.disable(); // 接收专业
							// cbxChargeBySystem.disable(); // 所属机组或系统
							// cbxWorkCondition.disable();
							// locationName.disable();
							// dangerType.disable();
							// dangerCondition.disable();
							if (record.data.model.isStandard == "Y") {
								Ext.get("workticketNo").dom.value = "自动生成";
								dfPlanStartDate.setValue(getDate());
								dfPlanEndDate.setValue(getDate());
								triggerChargeBy.enable();
								txtChargeDept.enable();
								btnModify.setVisible(false);
								Ext.get("workticketBaseInfo.workticketNo").dom.value = "";
								topbar.setDisabled(false);
							}

						}

					},
					failure : function() {
						Ext.Msg.alert(Constants.ERROR,
								Constants.OPERATE_ERROR_MSG);
					}
				}, 'workticketNo=' + workticketNo);

		// 电气一种票图片读取
		Ext.get('imagephoto').dom.src = "workticket/getDetailMapByWorkticketNo.action?workticketNo="
				+ workticketNo + "&time=" + new Date().getTime();

		//        
		// Ext.lib.Ajax.request('POST',
		// 'workticket/getDetailWorkticketBaseInfoByNo.action', {
		// success : function(result, request) {
		// var record = eval('(' + result.responseText + ')');
		// baseInfoFormPanel.getForm().loadRecord(record);
		// Ext.get("workticketBaseInfo.workticketNo").dom.value =
		// record.data.workticketNo;
		// // 工作负责人,监工,动火执行人,所属部门的id设置
		// Ext.get('workticketBaseInfo.chargeBy').dom.value =
		// record.data.chargeBy;
		// Ext.get('workticketBaseInfo.watcher').dom.value =
		// record.data.watcher;
		// Ext.get('workticketBaseInfo.chargeDept').dom.value =
		// record.data.chargeDept;
		// Ext.get('workticketBaseInfo.fireticketExecuteBy').dom.value =
		// record.data.fireticketExecuteBy;
		// // 时间格式化
		// Ext.get("planStartDate").dom.value =
		// renderDate(record.data.planStartDate);
		// Ext.get("planEndDate").dom.value =
		// renderDate(record.data.planEndDate);
		// if (record.data.isEmergency == 'Y') {
		// Ext.get("isEmergency").dom.checked = true;
		// }
		// // ----------add by fyyang 090309 --------------------
		// //危险点
		// if(dangerCondition.getValue()=="null")
		// {
		// dangerCondition.setValue("");
		// }
		//                    
		// //add by fyyang
		// //缺陷单号是否可用
		// if(failureCode.getValue()!="")
		// {
		// failureCode.enable();
		// }
		// else
		// {
		// failureCode.disable();
		// }
		//						
		// //电二票时工作条件可以用
		// if (cbxWorkticketType.value == "2")
		// {
		// cbxWorkCondition.enable();
		// }
		// //关联主票、动火级别、动火执行人可以修改
		// if (cbxWorkticketType.value == "4")
		// {
		// // 关联主票
		// txtRefWorkticketNo.enable();
		// // 动火票级别
		// cbxFireLevel.enable();
		// }
		//							
		//							
		//              
		// //---------------------------------------------------------
		// // 工作票种类，检修专业不能修改
		// cbxWorkticketType.disable();
		// cbxRepairSpecail.disable();
		// // 根据工作票种类来判断哪些是可用的
		// workticketTypeSelected();
		// // 动火级别
		// if (record.data.firelevelId != null) {
		// Ext.get('firelevelId').dom.value = record.data.firelevelId;
		// }
		// // 关联主票
		// if (record.data.refWorkticketNo != null) {
		// // edit by qzhang-->关联主票的票号如果是‘0’不显示
		// if(record.data.refWorkticketNo == "0"){
		// Ext.get('refWorkticketNo').dom.value = "";
		// }else{
		// Ext.get('refWorkticketNo').dom.value = record.data.refWorkticketNo;
		// }
		//                        
		// }
		// // 控制"选择图片"项只有在“电气一种票”种类下才显示
		// if (cbxWorkticketType.value == "1") {
		// photo.setVisible(true);
		// showField(imagephoto);
		// } else {
		// photo.setVisible(false);
		// hideField(imagephoto);
		// }
		// // 控制修改按钮显示
		// btnModify.setVisible(true);
		// // 设置工作票编号
		// register.workticketNo = Ext.get('workticketNo').dom.value;
		// // 设置所属机组或系统
		// register.equAttributeCode =
		// Ext.getCmp('equAttributeCode').getValue();
		// // 设置工作票种类/工作票类型编码
		// register.workticketTypeCode =
		// Ext.getCmp('workticketTypeCode').getValue();
		// //add----
		// if(record.data.dangerType!=null)
		// {
		// dangertypeid=record.data.dangerType;
		// }
		//                     
		// // 汉字姓名,部门名称读取
		// Ext.lib.Ajax.request('POST',
		// 'workticket/getDetailTicketInfoByCode.action', {
		// success : function(result, request) {
		// var record = eval('(' + result.responseText + ')');
		// // 工作负责人
		// Ext.get("chargebyname").dom.value = record.data.chargeByName;
		// // 监工
		// Ext.get('watchername').dom.value = record.data.watcherName;
		// // 所属部门
		// Ext.get('deptname').dom.value = record.data.deptName;
		// // 动火执行人
		// if (record.data.fireTickerExeByName != null) {
		// Ext.get('firetickerexecutebyname').dom.value =
		// record.data.fireTickerExeByName;
		// }
		// if(record.data.dangerTypeName!=null)
		// {
		// Ext.getCmp('dangerType').setValue(dangertypeid);
		// Ext.form.ComboBox.superclass.setValue.call(Ext
		// .getCmp('dangerType'),record.data.dangerTypeName);
		// }
		// else
		// {
		// //add by fyyang 090309
		// Ext.getCmp('dangerType').setValue("");
		// Ext.form.ComboBox.superclass.setValue.call(Ext
		// .getCmp('dangerType'),"");
		// }
		//                        
		//
		// //动火票种类
		//							
		// cbxFireLevel.setValue(cbxFireLevel.getValue());
		//                        
		// },
		// failure : function() {
		// Ext.Msg.alert(Constants.ERROR, Constants.OPERATE_ERROR_MSG);
		// }
		// }, 'workticketNo=' + workticketNo);
		// //----------
		//                    
		// },
		// failure : function() {
		// btnModify.setVisible(false);
		// Ext.Msg.alert(Constants.ERROR, Constants.OPERATE_ERROR_MSG);
		// }
		// }, 'workticketNo=' + workticketNo);

		// // 电气一种票图片读取
		// Ext.get('imagephoto').dom.src =
		// "workticket/getDetailMapByWorkticketNo.action?workticketNo="
		// + workticketNo + "&time=" + new Date().getTime();

	}

	// -----add by fyyang - 090306 获取默认工号-姓名-
	function getWorkCode() {
		Ext.lib.Ajax.request('POST', 'comm/getCurrentSessionEmployee.action', {
			success : function(action) {
				var result = eval("(" + action.responseText + ")");
				if (result.workerCode) {
					// 设定工作负责人为登录人
					Ext.get("workticketBaseInfo.chargeBy").dom.value = result.workerCode;
					Ext.get("chargebyname").dom.value = result.workerName
							? result.workerName
							: '';

					// Ext.get("workticketBaseInfo.watcher").dom.value =
					// result.workerCode;
					// Ext.get("watchername").dom.value = result.workerName
					// ? result.workerName
					// : '';

					Ext.get('workticketBaseInfo.chargeDept').dom.value = result.deptCode;
					Ext.get('deptname').dom.value = result.deptName
							? result.deptName
							: '';
				}
			}
		});
	}

	// 画面恢复到初始没有数据的状态
	function pageNoDataInitial() {

		baseInfoFormPanel.getForm().reset();
		Ext.get("workticketBaseInfo.workticketNo").dom.value = '';
		// 工作负责人,监工,动火执行人,所属部门的id设置
		Ext.get('workticketBaseInfo.chargeBy').dom.value = '';
		Ext.get('workticketBaseInfo.watcher').dom.value = '';
		Ext.get('workticketBaseInfo.chargeDept').dom.value = '';
		Ext.get('workticketBaseInfo.fireticketExecuteBy').dom.value = '';

		// add by fyyang

		getWorkCode();
		// 所属部门
		txtChargeDept.disable();
		// 工作条件
		cbxWorkCondition.disable();
		// 关联主票
		txtRefWorkticketNo.disable();
		// 动火执行人
		txtFireticketExecuteBy.disable();
		// 动火票级别
		//cbxFireLevel.disable();
		// 工作票种类
		cbxWorkticketType.enable();

		triggerChargeBy.enable();
		txtChargeDept.enable();
		// 工作票来源
		cbxWorkticketSource.enable();
		// 检修专业
		cbxRepairSpecail.enable();

		// add by fyyang 090217
		cbxReceiveSpecail.enable(); // 接收专业
		cbxChargeBySystem.enable(); // 所属机组或系统
		// cbxWorkCondition.enable();
		locationName.enable();
		dangerType.enable();
		dangerCondition.enable();
		// ------------------------
		if (!photo.hidden) {
			hideField(photo);
		}
		hideField(imagephoto);
		// 控制修改按钮显示
		btnModify.setVisible(false);

		if (photo.el && photo.el.dom) {

			var imagephotoDom = imagephoto.el.dom;
			var imagephotoClone = imagephotoDom.cloneNode();
			var prt = imagephotoDom.parentNode;
			prt.removeChild(imagephotoDom);
			prt.appendChild(imagephotoClone);

			imagephoto.applyToMarkup(imagephotoClone);

			// 清除附件内容
			var domAppend = photo.el.dom;
			var parent = domAppend.parentNode;

			// 保存
			var domForSave = domAppend.cloneNode();
			// 移除附件控件
			parent.removeChild(domAppend);
			// 再追加控件
			parent.appendChild(domForSave);
			// 应用该控件
			photo.applyToMarkup(domForSave);

		}

		Ext.get("imagephoto").dom.src = "comm/images/powererp.jpg";
		topbar.setDisabled(false);
	}

	// 工作票种类选择后，联动处理
	function workticketTypeSelected() {
		if (register.workticketNo) {
			return;
		}
		var ticketTypeCode = "";
		// ticketTypeCode = cbxWorkticketType.value;
		ticketTypeCode = Ext.get("workticketBaseInfo.workticketTypeCode").dom.value;

		// 工作负责人
		triggerChargeBy.setValue("");
		getWorkCode();
		// 所属部门
		Ext.get("deptname").dom.value = "";
		txtChargeDept.disable();
		// 工作条件
		// cbxWorkCondition.clearValue();
		cbxWorkCondition.setValue("");
		cbxWorkCondition.disable();
		// 关联主票
		txtRefWorkticketNo.setValue("");
		txtRefWorkticketNo.disable();
		// 动火执行人
		txtFireticketExecuteBy.setValue("");
		txtFireticketExecuteBy.disable();
		// 动火票级别
		cbxFireLevel.clearValue();
		//cbxFireLevel.disable();
		// 图片隐藏
		photo.setVisible(false);
		hideField(imagephoto);
		// ======================================
		autoDeviceName.disable();
		if (ticketTypeCode == "") {
			return;
		} else {
			triggerChargeBy.enable();
			txtChargeDept.enable();
			if (ticketTypeCode == "1") {
				// 选择“电气一种票”时
				if (photo.hidden) {
					showField(photo);
				}
				showField(imagephoto);
			} else if (ticketTypeCode == "2") {
				// 选择“电气二种票“时
				cbxWorkCondition.enable();
			} else if (ticketTypeCode == "3") {
				// 选择“热力机械票”时
			} else if (ticketTypeCode == "4") {
				// 选择“动火工作票”时
				txtRefWorkticketNo.enable();
				txtFireticketExecuteBy.enable();
				cbxFireLevel.enable();
			} else if (ticketTypeCode == "5") {
				// add by fyyang
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

	// 检查计划结束时间是否大于计划开始时间
	function endDateCheck() {
		var startDate = Ext.get("planStartDate").dom.value;
		var endDate = Ext.get("planEndDate").dom.value;
		var res = compareDateStr(startDate, endDate);
		if (res) {
			return false;
		}
		return true;
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

	// 工作负责人选择
	function chargeBySelect() {

		if (cbxWorkticketType.value == "") {
			Ext.Msg.alert("提示", "请先选择工作票种类");
		} else {
			triggerChargeBy.blur();
			var args = new Object();
			// 工作票类型名称
			// args.workticketTypeName = cbxWorkticketType.lastSelectionText;
			args.workticketTypeName = Ext.get("workticketTypeCode").dom.value; // modify
			args.watcher = Ext.get("workticketBaseInfo.watcher").dom.value;
			var object = window
					.showModalDialog(
							'workticketChargeBySelect.jsp',
							args,
							'dialogWidth=550px;dialogHeight=350px;center=yes;help=no;resizable=no;status=no;');
			if (typeof(object) == 'object') {
				Ext.get("workticketBaseInfo.chargeBy").dom.value = object.empCode;
				Ext.get("chargebyname").dom.value = object.chsName
						? object.chsName
						: '';

				Ext.get('workticketBaseInfo.chargeDept').dom.value = object.deptCode;
				Ext.get('deptname').dom.value = object.deptName
						? object.deptName
						: '';
			}
		}
	}

	// 监工选择
	function supervisorSelect() {
		if (Ext.get("workticketBaseInfo.chargeBy").dom.value == "") {
			Ext.Msg.alert("提示", "请先选择工作负责人！");
			return;

		}
		var obj = new Object;
		obj.chargeBy = Ext.get("workticketBaseInfo.chargeBy").dom.value;
		var object = window
				.showModalDialog(
						'watcherBySelect.jsp',
						obj,
						'dialogWidth=500px;dialogHeight=300px;center=yes;help=no;resizable=no;status=no;');
		if (typeof(object) == 'object') {

			Ext.get("workticketBaseInfo.watcher").dom.value = object.workerCode;
			Ext.get("watchername").dom.value = object.workerName;
		}
	}

	// 关联主票选择
	function refWorkticketNoSelect() {
		if (!txtRefWorkticketNo.disabled) {
			txtRefWorkticketNo.blur();
			var object = window
					.showModalDialog(
							'refWorkticket.jsp',
							null,
							'dialogWidth=800px;dialogHeight=500px;center=yes;help=no;resizable=no;status=no;');
			if (typeof(object) == 'object') {
				txtRefWorkticketNo.setValue(object.refWorkticketNo);
			}
		}
	}

	// 动火执行人选择
	function FireticketExecuteBySelect() {
		if (!txtFireticketExecuteBy.disabled) {
			if (Ext.get("chargebyname").dom.value == "") {
				Ext.Msg.alert("提示", Constants.SELECT_CHARGE_BY_MSG);
			} else {
				txtFireticketExecuteBy.blur();
				var object = window
						.showModalDialog(
								'fireChargebySelect.jsp',
								null,
								'dialogWidth=550px;dialogHeight=350px;center=yes;help=no;resizable=no;status=no;');
				if (typeof(object) == 'object') {
					strChargeBy = Ext.get("chargebyname").dom.value;
					if (strChargeBy == object.chsName) {
						Ext.Msg.alert("提示", "动火票执行人不能与工作负责人相同");
						txtFireticketExecuteBy.setValue("");
						return;
					}
					Ext.get("fireticketExecuteBy").dom.value = object.empCode;
					txtFireticketExecuteBy.setValue(object.chsName);
				}
			}
		}
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
		//工作班成员
		var memberr = Ext.get("workticketBaseInfo.members").dom.value;
		// 工作票来源
		var ticketSource = Ext.get("sourceId").dom.value;
		// 检修专业
		var repairSpecail = Ext.get("repairSpecailCode").dom.value;
		// 工作负责人
		var chargeBy = Ext.get("chargebyname").dom.value;
		// 监工
		// var watcher = Ext.get("watchername").dom.value;
		// 接收专业
		var permissionDept = Ext.get("permissionDept").dom.value;
		if (cbxWorkCondition.disabled == false) {
			// 工作条件
			var condition = Ext.get("conditionName").dom.value;
		}
		// 所属机组或系统
		var equAttribute = Ext.get("equAttributeCode").dom.value;
		// 弹出信息
		var strMsg = "你还需要填入下列项目,然后才能保存:";
		var msg = "";
		if (!Ext.get('applyNo').dom.disabled) {
			if (Ext.get('applyNo').dom.value == "") {
				msg += "{任务单号},";
			}
		}
		if (ticketType == '') {
			msg += "{工作票种类},";
		}
		if (memberr == '') {
			msg += "{工作班成员},";
		}
		if (ticketSource == '') {
			msg += "{工作票来源},";
		}
		if (repairSpecail == '') {
			msg += "{检修专业},";
		}
		if (chargeBy == '') {
			msg += "{工作负责人},";
		}
		// if (watcher == '') {
		// msg += "{监工},";
		// }
		if (permissionDept == '') {
			msg += "{接收专业},";
		}
		if (!cbxWorkCondition.disabled && condition == '') {
			msg += "{工作条件},";
		}
		if (locationName.getValue() == "") {
			msg += "{工作地点},";
		}
		if (equAttribute == '') {
			msg += "{所属机组或区域}";
		}
		if (txtWorkContent.getValue() == null
				|| txtWorkContent.getValue() == '') {
			msg += "{工作内容}";
		}
		if (Ext.get('workticketBaseInfo.workticketTypeCode').dom.value == "4") {
			if (txtRefWorkticketNo.getValue() == "") {
				msg += "{关联主票}";
			}
			if (cbxFireLevel.getValue() == "") {
				msg += "{动火级别}";
			}
		}
		if (cbxMainEqu.getValue() == null || cbxMainEqu.getValue() == '') {
			msg += "{主设备}";
		}
		if (msg != '') {
			Ext.Msg.alert(Constants.NOTICE, strMsg + msg);
			return false;
		} else if (!endDateCheck()) {
			Ext.Msg.alert(Constants.NOTICE, "计划结束时间必须大于计划开始时间");
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
		if (Ext.get("workticketBaseInfo.sourceId").dom.value == "1") {
			failureCode.enable();
			applyNo.setDisabled(true);
		} else {
			failureCode.disable();
			applyNo.setDisabled(false);
		}
		Ext.get("workticketBaseInfo.dangerCondition").dom.value = dangerCondition
				.getValue();
		var fileName = '';
		fileName = Ext.get("photo").dom.value;
		if (checkInput()) {
			// if (isEmergency.checked) {
			// Ext.get("workticketBaseInfo.isEmergency").dom.value = "Y";
			// } else {
			// Ext.get("workticketBaseInfo.isEmergency").dom.value = "N";
			// }
			baseInfoFormPanel.getForm().submit({
				method : Constants.POST,
				params : {
					file : fileName,
					dangerId : myDangerId
				},
				url : 'workticket/updateDetailWorkticketBaseInfo.action',
				success : function(form, action) {
					// 设置所属机组或系统
					register.setEquAttributeCode(Ext.getCmp('equAttributeCode')
							.getValue());
					// add by fyyang 090306 设置工作负责人部门
					register.chargeDept = Ext
							.get('workticketBaseInfo.chargeDept').dom.value;
					// 更新上报列表
					register.updateReportList();
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
		var fileName = '';
		fileName = Ext.get("photo").dom.value;
		if (checkInput()) {
			Ext.Msg.wait("正在保存数据,请等待...");
			// if (isEmergency.checked) {
			// Ext.get("workticketBaseInfo.isEmergency").dom.value = "Y";
			// } else {
			// Ext.get("workticketBaseInfo.isEmergency").dom.value = "N";
			// }
			// 工作班成员处理 tempMemberStoreData
			if (tempMemberStoreData != null) {
				for (var i = 0; i < tempMemberStoreData.length; i++) {
					if (tempMemberStoreData[i].actortypename == "正式员工") {
						tempMemberStoreData[i].actortypename = "1";
					}

					if (tempMemberStoreData[i].actortypename == "临时员工") {
						tempMemberStoreData[i].actortypename = "2";
					}

				}
			}

			// ---------------------------------
			baseInfoFormPanel.getForm().submit({
				method : Constants.POST,
				params : {
					file : fileName,
					isStandard : 'N', // 非标准票
					dangerId : myDangerId,
					standticketNo : standticketNo,
					// contentRecords:
					// Ext.util.JSON.encode(tempContentStoreData),
					existWorkticketNo : existWorkticketNo,
					memberRecords : Ext.util.JSON.encode(tempMemberStoreData)
				},
				url : 'workticket/addDetailWorkticketBaseInfo.action',
				success : function(form, action) {
					Ext.Msg.hide();
					var o = eval("(" + action.response.responseText + ")");
					var strWorkticketNo = o.workticketNo;
					// 成功后画面上显示工作票编号
					Ext.get('workticketNo').dom.value = strWorkticketNo;
					Ext.get('workticketBaseInfo.workticketNo').dom.value = strWorkticketNo;
					// 成功后修改按钮出现，调用标准票按钮不可用
					btnModify.setVisible(true);
					topbar.setDisabled(true);
					// 工作票种类，工作负责人不能修改
					cbxWorkticketType.disable();
					triggerChargeBy.disable();
					txtChargeDept.disable();
					//cbxFireLevel.disable();
					myDangerId = "";
					standticketNo = "";
					tempMemberStoreData = null;
					// tempContentStoreData=null;
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
		register.chargeDept = Ext.get('workticketBaseInfo.chargeDept').dom.value; // add
		// by
		// fyyang
		// 090306
		// 所属班组
		// add by fyyang 090216
		register.locationName = locationName.getValue();
		register.firelevel = cbxFireLevel.getValue();
		// 设置工作票编号
		register.workticketNo = Ext.get('workticketBaseInfo.workticketNo').dom.value;
		// 设置所属机组或系统
		register.equAttributeCode = Ext
				.get('workticketBaseInfo.equAttributeCode').dom.value;
		// 设置工作票种类/工作票类型编码
		register.workticketTypeCode = Ext
				.get('workticketBaseInfo.workticketTypeCode').dom.value;
		if (register.isStandard == "Y") {
			register.isStandard = "N";
			register.isCreateByStandard = "Y";
		}

		// 迁移到下一个页面
		register.toNext();
	}
	// 初始化隐藏选择照片
	hideField(imagephoto);
	// add by fyyang 090306 工作负责人为默认登录的人
	getWorkCode();
});