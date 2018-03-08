Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {

	// 自定义函数
	var newEmployee = parent.Ext.getCmp('tabPanel').newEmployee;
	var PD002_MSG = parent.Ext.getCmp('tabPanel').PD002_MSG;
	// 设置加载登记Tab页的监听器
	newEmployee.editTabRegisterHandler = initPage;
	newEmployee.refreshTabRegister = refreshPage;

	var width = 150;
	var count = 0;
	var formRecord = "";
	var flag = false;

	// 保存按钮
	var saveBtn = new Ext.Button({
				text : Constants.BTN_SAVE,
				iconCls : Constants.CLS_SAVE,
				handler : saveHandler
			});

	// 员工工号
	var empCodeTxt = new Ext.form.Hidden({
				id : 'empCode',
				// modified by liuyi 20100406 启用新工号 
				name : 'empRegister.empCode',
				disabled : true,
				fieldLabel : '员工工号',
				width : width
			});
			var newEmpCodeTxt = new Ext.form.TextField({
				id : 'newEmpCodeTxt',
				// modified by liuyi 20100406 启用新工号 
				name : 'empRegister.newEmpCode',
				disabled : true,
				fieldLabel : '员工工号',
				width : width
			});
	// 出生日期
	var birthdayTxt = new Ext.form.TextField({
				id : 'birthday',
				name : 'empRegister.birthday',
				width : width,
				fieldLabel : '出生日期<font color="red">*</font>',
				style : 'cursor:pointer',
				readOnly : true,
				allowBlank : false,
				listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd',
									isShowClear : false,
									onpicked : function() {
										setAge(birthdayTxt.getValue()
												.substring(0, 4));
										birthdayTxt.clearInvalid();
									},
									onclearing : function() {
										birthdayTxt.markInvalid();
										birthdayTxt.setValue("");
									}
								});
					}
				}
			});

	// 性别
	var sexCbo = new Ext.form.CmbHRCode({
				id : 'sex',
				hiddenName : 'empRegister.sex',
				width : width,
				allowBlank : false,
				fieldLabel : '性别<font color="red">*</font>',
				type : '性别'
			});

	// 员工姓名数据
	var empNameData = Ext.data.Record.create([{
				// 员工姓名
				name : 'chsName'
			}, {
				// 员工编码
				name : 'empCode'
			}, {
				// 员工ID
				name : 'empId'
			}, {
				// 上次修改时间
				name : 'lastModifiyDate'
			},{
				name : 'newEmpCode'
			}]);

	// 员工姓名Store
	var empNameStore = new Ext.data.JsonStore({
				url : 'hr/getEmpInfoNewEmployee.action',
				root : 'list',
				fields : empNameData
			});

			
	// 员工姓名下拉框
	var empNameCbo = new Ext.form.ComboBox({
				id : 'chsName',
				fieldLabel : '员工姓名<font color="red">*</font>',
				width : width,
				allowBlank : false,
				triggerAction : 'all',
				store : empNameStore,
				displayField : 'chsName',
				valueField : 'empCode',
				mode : 'local',
				readOnly : true,
				listeners : {
					select : function(cmb, record, index) {
						// 把人员编码设给员工工号
						empCodeTxt.setValue(record.get("empCode"));
						//员工工号（新)
						newEmpCodeTxt.setValue(record.get("newEmpCode"));
						// 把人员姓名设给中文名
						chsNameHidden.setValue(record.get("chsName"));
						// 设置人员ID
						empIdHidden.setValue(record.get("empId"));
						// 设置上次修改时间
						modifiyDateHidden.setValue(record
								.get("lastModifiyDate").replace("T", " "));
					}
				}
			});

	// 隐藏的中文姓名
	var chsNameHidden = new Ext.form.Hidden({
				name : 'empRegister.chsName'
			});

	// 年龄
	var ageTxt = new Ext.form.TextField({
				width : width - 35,
				fieldLabel : '年龄',
				style : 'text-align: right;',
				disabled : true,
				readOnly : true
			});

	// 婚否状况
	var weddedCbo = new Ext.form.CmbHRCode({
				id : 'isWedded',
				hiddenName : 'empRegister.isWedded',
				width : width,
				allowBlank : false,
				fieldLabel : '婚否状况<font color="red">*</font>',
				type : '婚否状况'
			});

	// 周岁
	var ageLabel = new Ext.form.Label({
				text : '周岁'
			});

	// 英文名
	var enNameTxt = new Ext.form.CodeField({
		id : 'enName',
		name : 'empRegister.enName',
		width : width,
		fieldLabel : '英文名',
		maxLength : 25,
		initEvents : function() {
			Ext.form.CodeField.superclass.initEvents.call(this);
			var allowed = /[a-zA-Z 0-9]/;
			this.stripCharsRe = new RegExp('[^a-zA-Z 0-9]', 'gi');
			var keyPress = function(e) {
				var k = e.getKey();
				if (!Ext.isIE
						&& (e.isSpecialKey() || k == e.BACKSPACE || k == e.DELETE)) {
					return;
				}
				var c = e.getCharCode();
				if (!allowed.test(String.fromCharCode(c))) {
					e.stopEvent();
				}
			};
			this.el.on("keypress", keyPress, this);
		}
	});

	// 籍贯
	var nativeCbo = new Ext.form.CmbHRBussiness({
				id : 'nativePlaceId',
				hiddenName : 'empRegister.nativePlaceId',
				width : width,
				allowBlank : false,
				fieldLabel : '籍贯<font color="red">*</font>',
				type : '籍贯'
			});

	// 民族
	var nationCbo = new Ext.form.CmbHRBussiness({
				id : 'nationCodeId',
				hiddenName : 'empRegister.nationCodeId',
				width : width,
				allowBlank : false,
				fieldLabel : '民族<font color="red">*</font>',
				type : '民族'
			});

	// 政治面貌
	var politicsCbo = new Ext.form.CmbHRBussiness({
				id : 'politicsId',
				hiddenName : 'empRegister.politicsId',
				width : width,
				fieldLabel : '政治面貌',
				type : '政治面貌'
			});

	// 身份证号
	var identityCardTxt = new Ext.form.CodeField({
				id : 'identityCard',
				name : 'empRegister.identityCard',
				width : width,
				allowBlank : false,
				fieldLabel : '身份证号<font color="red">*</font>',
				maxLength : 18
			});

	// 所属部门
	var deptTxt = new Ext.form.TextField({
				id : 'deptName',
				fieldLabel : '所属部门<font color="red">*</font>',
				width : width,
				allowBlank : false,
				readOnly : true
			});

	// 所属部门ID
	var hiddenDeptTxt = {
		id : 'deptId',
		name : 'empRegister.deptId',
		xtype : 'hidden',
		value : '',
		readOnly : true,
		hidden : true
	};

	// 选择部门
	deptTxt.onClick(deptSelect);

	// 工作岗位数据
	var stationData = Ext.data.Record.create([{
				// 岗位名称
				name : 'stationName'
			}, {
				// 岗位ID
				name : 'stationId'
			}]);

	// 工作岗位Store
	var stationStore = new Ext.data.JsonStore({
				url : 'hr/getStationByDeptNewEmployee.action',
				root : 'list',
				fields : stationData
			});

	// 工作岗位下拉框
	var stationCbo = new Ext.form.ComboBox({
				id : 'stationId',
				hiddenName : 'empRegister.stationId',
				fieldLabel : '工作岗位<font color="red">*</font>',
				width : width,
				allowBlank : false,
				triggerAction : 'all',
				store : stationStore,
				displayField : 'stationName',
				valueField : 'stationId',
				mode : 'local',
				readOnly : true
			});

	// 员工类别
	var empTypeCbo = new Ext.form.CmbHRBussiness({
				id : 'empTypeId',
				hiddenName : 'empRegister.empTypeId',
				width : width,
				allowBlank : false,
				fieldLabel : '员工类别<font color="red">*</font>',
				type : '员工类别'
			});

	// 进厂类别
	var inTypeCbo = new Ext.form.CmbHRBussiness({
				id : 'inTypeId',
				hiddenName : 'empRegister.inTypeId',
				width : width,
				allowBlank : false,
				fieldLabel : '进厂类别<font color="red">*</font>',
				type : '进厂类别'
			});

	// 进厂日期
	var missionDateTxt = new Ext.form.TextField({
				id : 'missionDate',
				name : 'empRegister.missionDate',
				width : width,
				fieldLabel : '进厂日期<font color="red">*</font>',
				style : 'cursor:pointer',
				readOnly : true,
				allowBlank : false
			});

	missionDateTxt.onClick(function() {
				WdatePicker({
							startDate : '%y-%M-%d',
							dateFmt : 'yyyy-MM-dd',
							isShowClear : false,
							onpicked : function() {
								missionDateTxt.clearInvalid();
							},
							onclearing : function() {
								missionDateTxt.markInvalid();
								missionDateTxt.setValue("");
							}
						});
			});

	// 试用期开始
	var tryoutStartDateTxt = new Ext.form.TextField({
				id : 'tryoutStartDate',
				name : 'empRegister.tryoutStartDate',
				width : width,
				fieldLabel : '试用期开始',
				style : 'cursor:pointer',
				readOnly : true
			});

	tryoutStartDateTxt.onClick(function() {
				WdatePicker({
							startDate : '%y-%M-%d',
							dateFmt : 'yyyy-MM-dd'
						});
			});

	// 试用期结束
	var tryoutEndDateTxt = new Ext.form.TextField({
				id : 'tryoutEndDate',
				name : 'empRegister.tryoutEndDate',
				width : width,
				fieldLabel : '试用期结束',
				style : 'cursor:pointer',
				readOnly : true
			});

	tryoutEndDateTxt.onClick(function() {
				WdatePicker({
							startDate : '%y-%M-%d',
							dateFmt : 'yyyy-MM-dd'
						});
			});

	// 是否存档: 是
	var isSaveYes = new Ext.form.Radio({
				id : 'empState1',
				boxLabel : newEmployee.IF_YES,
				name : 'empRegister.empState',
				inputValue : newEmployee.IF_SAVE_Y
			});

	// 是否存档: 否
	var isSaveNo = new Ext.form.Radio({
				id : 'empState2',
				boxLabel : newEmployee.IF_NO,
				name : 'empRegister.empState',
				inputValue : newEmployee.IF_SAVE_N,
				checked : true
			});

	// 是否存档
	var isSave = {
		id : 'empState',
		layout : 'column',
		isFormField : true,
		fieldLabel : '存档',
		disabled : true,
		border : false,
		items : [{
					columnWidth : .4,
					border : false,
					items : isSaveYes
				}, {
					columnWidth : .4,
					items : isSaveNo
				}]
	};

	// 当前学历
	var educationCbo = new Ext.form.CmbHRBussiness({
				id : 'educationId',
				hiddenName : 'empRegister.educationId',
				width : width,
				fieldLabel : '当前学历',
				type : '学历'
			});

	// 学习专业
	var specialtyCbo = new Ext.form.CmbHRBussiness({
				id : 'specialtyCodeId',
				hiddenName : 'empRegister.specialtyCodeId',
				width : width,
				fieldLabel : '学习专业',
				type : '学习专业'
			});

	// 学位
	var degreeCbo = new Ext.form.CmbHRBussiness({
				id : 'degreeId',
				hiddenName : 'empRegister.degreeId',
				width : width,
				fieldLabel : '学位',
				type : '学位'
			});

	// 毕业日期
	var graduateDateTxt = new Ext.form.TextField({
				id : 'graduateDate',
				name : 'empRegister.graduateDate',
				width : width,
				fieldLabel : '毕业日期',
				style : 'cursor:pointer',
				readOnly : true
			});

	graduateDateTxt.onClick(function() {
				WdatePicker({
							startDate : '%y-%M-%d',
							dateFmt : 'yyyy-MM-dd'
						});
			});

	// 学校
	var schoolCbo = new Ext.form.CmbHRBussiness({
				id : 'schoolCodeId',
				hiddenName : 'empRegister.schoolCodeId',
				width : 395,
				fieldLabel : '毕业学校',
				type : '学校'
			});

	// 是否退转军人: 是
	var isVeteranYes = new Ext.form.Radio({
				id : 'isVeteran1',
				boxLabel : newEmployee.IF_YES,
				name : 'empRegister.isVeteran',
				inputValue : newEmployee.IS_VETERAN_Y
			});

	// 是否退转军人: 否
	var isVeteranNo = new Ext.form.Radio({
				id : 'isVeteran2',
				boxLabel : newEmployee.IF_NO,
				name : 'empRegister.isVeteran',
				inputValue : newEmployee.IS_VETERAN_N,
				checked : true
			});

	// 是否退转军人
	var isVeteran = {
		id : 'isVeteran',
		layout : 'column',
		isFormField : true,
		fieldLabel : '是否退转军人',
		border : false,
		items : [{
					columnWidth : .1,
					border : false,
					items : isVeteranYes
				}, {
					columnWidth : .1,
					items : isVeteranNo
				}]
	};

	// 备注
	var memoTa = new Ext.form.TextArea({
				id : 'memo',
				width : 637,
				height : 100,
				name : 'empRegister.memo',
				fieldLabel : "备注",
				maxLength : 250
			});

	// 人员ID
	var empIdHidden = new Ext.form.Hidden({
				id : 'empId',
				name : 'empRegister.empId'
			});

	// 上次修改时间
	var modifiyDateHidden = new Ext.form.Hidden({
				id : 'lastModifiyDate',
				name : 'empRegister.lastModifiyDate'
			});

	// 新增/修改表示
	var flagHidden = new Ext.form.Hidden({
				id : 'flagHidden',
				name : 'flag'
			});

	// 按钮Toolbar
	var btnTbar = new Ext.Toolbar({
				region : 'north',
				border : false,
				items : [saveBtn]
			});

	var formPanel = new Ext.FormPanel({
				labelAlign : 'right',
				labelWidth : 80,
				width : 740,
				border : false,
				layout : 'form',
				autoScroll : true,
				items : [
				
						// 第一行
						{
					xtype : 'panel',
					border : false,
					layout : 'column',
					items : [{
								columnWidth : 0.33,
								layout : 'form',
								//items : [empCodeTxt,newEmpCodeTxt]
								items : [newEmpCodeTxt]
							}/*, {
								columnWidth : 0.33,
								layout : 'form',
								items : [birthdayTxt]
							}, {
								columnWidth : 0.33,
								layout : 'form',
								items : [sexCbo]
							}*/, {
								columnWidth : 0.01,
								layout : 'form',
								items : [chsNameHidden]
							}]
				},
						// 第二行
						{
							xtype : 'panel',
							border : false,
							layout : 'column',
							items : [{
										columnWidth : 0.33,
										layout : 'form',
										items : [empNameCbo]
									}/*, {
										columnWidth : 0.28,
										layout : 'form',
										items : [ageTxt]
									}, {
										columnWidth : 0.05,
										layout : 'form',
										style : 'padding-top:5;',
										items : [ageLabel]
									}, {
										columnWidth : 0.33,
										layout : 'form',
										items : [weddedCbo]
									}*/]
						}/*,
						// 第三行
						{
							xtype : 'panel',
							border : false,
							layout : 'column',
							items : [{
										columnWidth : 0.33,
										layout : 'form',
										items : [enNameTxt]
									}, {
										columnWidth : 0.33,
										layout : 'form',
										items : [nativeCbo]
									}, {
										columnWidth : 0.33,
										layout : 'form',
										items : [nationCbo]
									}]
						},
						// 第四行
						{
							xtype : 'panel',
							border : false,
							layout : 'column',
							items : [{
										columnWidth : 0.33,
										layout : 'form',
										items : [politicsCbo]
									}, {
										columnWidth : 0.33,
										layout : 'form',
										items : [identityCardTxt]
									}]
						}*/,
						// 第五行
						{
							xtype : 'panel',
							border : false,
							layout : 'column',
							items : [{
										columnWidth : 0.33,
										layout : 'form',
										items : [deptTxt]
									}, {
										columnWidth : 0.33,
										layout : 'form',
										items : [stationCbo]
									}, {
										columnWidth : 0.33,
										items : [hiddenDeptTxt]
									}]
						},
						// 第六行
						{
							xtype : 'panel',
							border : false,
							layout : 'column',
							items : [/*{
										columnWidth : 0.33,
										layout : 'form',
										items : [empTypeCbo]
									}, {
										columnWidth : 0.33,
										layout : 'form',
										items : [inTypeCbo]
									}, */{
										columnWidth : 0.33,
										layout : 'form',
										items : [missionDateTxt]
									}]
						}/*,
						// 第七行
						{
							xtype : 'panel',
							border : false,
							layout : 'column',
							items : [{
										columnWidth : 0.33,
										layout : 'form',
										items : [tryoutStartDateTxt]
									}, {
										columnWidth : 0.33,
										layout : 'form',
										items : [tryoutEndDateTxt]
									}, {
										columnWidth : 0.33,
										layout : 'form',
										items : [isSave]
									}]
						},
						// 第八行
						{
							xtype : 'panel',
							border : false,
							layout : 'column',
							items : [{
										columnWidth : 0.33,
										layout : 'form',
										items : [educationCbo]
									}, {
										columnWidth : 0.33,
										layout : 'form',
										items : [specialtyCbo]
									}, {
										columnWidth : 0.33,
										layout : 'form',
										items : [degreeCbo]
									}]
						},
						// 第九行
						{
							xtype : 'panel',
							border : false,
							layout : 'column',
							items : [{
										columnWidth : 0.33,
										layout : 'form',
										items : [graduateDateTxt]
									}, {
										columnWidth : 0.66,
										layout : 'form',
										items : [schoolCbo]
									}]
						},
						// 第十行
						{
							columnWidth : 0.33,
							layout : 'form',
							items : [isVeteran]
						}*/,
						// 第十一行
						{
							columnWidth : 0.99,
							layout : 'form',
							items : [memoTa]
						}, empIdHidden, modifiyDateHidden]
			});

	// 设定布局器及面板
	new Ext.Viewport({
				enableTabScroll : true,
				defaults : {
					autoScroll : true
				},
				layout : 'border',
				items : [btnTbar, {
							region : 'center',
							layout : 'form',
							frame : true,
							border : false,
							items : [formPanel]
						}]
			});

	/**
	 * 选择部门
	 */
	function deptSelect() {
		if (newEmployee.deptSelect()) {
			stationCbo.clearValue();
			// 根据返回值设置画面的值
			deptTxt.setValue(newEmployee.name);
			Ext.get("deptId").dom.value = newEmployee.id;
			stationStore.load({
						params : {
							deptIdForm : newEmployee.id
						},
						callback : function() {
							stationStore.insert(0, new stationData({
												stationName : '',
												stationId : ''
											}));
						}
					})
		}
	}

	/**
	 * 保存处理
	 */
	function saveHandler() {
		if (checkForm()) {
			PD002_MSG.confirm(Constants.NOTICE_CONFIRM, Constants.COM_C_001,
					function(buttonobj) {
						// 确认保存
						if (buttonobj == "yes") {
							if (formPanel.getForm().isValid()
									&& checkDate(birthdayTxt.getValue(),
											new Date().format('Y-m-d'), "出生日期",
											"系统日期")
									&& checkInDate(missionDateTxt.getValue(),
											new Date().format('Y-m-d'))
									&& checkDate(tryoutStartDateTxt.getValue(),
											tryoutEndDateTxt.getValue(),
											"试用期开始", "试用期结束")) {
								setComboValue();
								var params = {
									'empRegister.empCode' : empCodeTxt
											.getValue(),
									'flag' : flagHidden.getValue()
								};
								formPanel.getForm().submit({
									method : Constants.POST,
									url : 'hr/saveNewEmployee.action',
									params : params,
									success : function(form, action) {
										if (action.response.responseText) {
											var o = eval("("
													+ action.response.responseText
													+ ")");
											var succ = o.msg;
											if (succ == Constants.DATE_FAILURE) {
												PD002_MSG.alert(Constants.ERROR,
														Constants.COM_E_023);
											} else if (succ == Constants.SQL_FAILURE) {
												PD002_MSG.alert(Constants.ERROR,
														Constants.COM_E_014);
											} else if (succ == Constants.DATA_USING) {
												PD002_MSG.alert(Constants.ERROR,
														Constants.COM_E_015);
											} else {
												PD002_MSG.alert(Constants.REMIND,
														Constants.COM_I_004);
												formRecord = "";
												refreshPage();
												newEmployee.refreshTabQuery();
											}
										}
									},
									failure : function() {
										PD002_MSG.alert(Constants.ERROR,
												Constants.COM_E_014);
									}
								})
							}
						}
					});
		}
	}

	/**
	 * 检查form
	 */
	function checkForm() {
		var msg = "";
		msg += checkInput(birthdayTxt, "出生日期", newEmployee.TYPE_SELECT);
		msg += checkInput(sexCbo, "性别", newEmployee.TYPE_SELECT);
		msg += checkInput(empNameCbo, "员工姓名", newEmployee.TYPE_SELECT);
		msg += checkInput(weddedCbo, "婚否状况", newEmployee.TYPE_SELECT);
		msg += checkInput(nativeCbo, "籍贯", newEmployee.TYPE_SELECT);
		msg += checkInput(nationCbo, "民族", newEmployee.TYPE_SELECT);
		msg += checkInput(identityCardTxt, "身份证号", newEmployee.TYPE_INPUT);
		msg += checkInput(deptTxt, "所属部门", newEmployee.TYPE_SELECT);
		msg += checkInput(stationCbo, "工作岗位", newEmployee.TYPE_SELECT);
		msg += checkInput(empTypeCbo, "员工类别", newEmployee.TYPE_SELECT);
		msg += checkInput(inTypeCbo, "进厂类别", newEmployee.TYPE_SELECT);
		msg += checkInput(missionDateTxt, "进厂日期", newEmployee.TYPE_SELECT);
		if (msg != "") {
			PD002_MSG.alert(Constants.ERROR, msg);
			return false;
		}
		return true;
	}

	/**
	 * 检查控件的值是否为空
	 */
	function checkInput(component, name, type) {
		var message = "";
		if (component.getValue() == "" || component.getValue() == null) {
			if (type == newEmployee.TYPE_INPUT) {
				message += String.format(Constants.COM_E_002, name);
			} else if (type == newEmployee.TYPE_SELECT) {
				message += String.format(Constants.COM_E_003, name);
			}
			message += "</BR>";
		}
		return message;
	}

	/**
	 * 检查时间范围是否合法
	 */
	function checkDate(startDate, endDate, string1, string2) {
		if (startDate == "" || endDate == "") {
			return true;
		} else {
			var start = Date.parseDate(startDate, 'Y-m-d');
			var end = Date.parseDate(endDate, 'Y-m-d');
			if (start.getTime() >= end.getTime()) {
				PD002_MSG.alert(Constants.ERROR, String.format(
								Constants.COM_E_006, string1, string2));
				return false;
			}
			return true;
		}
	}

	/**
	 * 检查进厂时间范围是否合法
	 */
	function checkInDate(startDate, endDate) {
		if (startDate == "" || endDate == "") {
			return true;
		} else {
			var start = Date.parseDate(startDate, 'Y-m-d');
			var end = Date.parseDate(endDate, 'Y-m-d');
			if (start.getTime() > end.getTime()) {
				PD002_MSG.alert(Constants.ERROR, String.format(
								Constants.COM_E_026, '进厂日期', '系统日期'));
				return false;
			}
			return true;
		}
	}

	/**
	 * 初始化画面
	 */
	function initPage(argEmpInfo) {
		 formPanel.getForm().trim();
		 if(flag) {
		 	flag = false;
		 	if(formPanel.getForm().isDirty()) {
		 		PD002_MSG.confirm(Constants.CONFIRM, Constants.COM_C_004,
					function(buttonobj) {
						if (buttonobj == "yes") {
							initData(argEmpInfo);
						} else {
							newEmployee.toTabQuery();
						}
					})
		 	} else {
			initData(argEmpInfo);
		}
		 }else if (formRecord!=""&&Ext.encode(formRecord) != ""
				&& !(Ext.encode(formRecord) == Ext.encode(formPanel.getForm()
						.getValues()))) {
			PD002_MSG.confirm(Constants.CONFIRM, Constants.COM_C_004,
					function(buttonobj) {
						if (buttonobj == "yes") {
							initData(argEmpInfo);
						} else {
							newEmployee.toTabQuery();
						}
					})
		} else {
			initData(argEmpInfo);
		}

	}

	function initData(argEmpInfo) {
		count = 0;
		refreshPage();
		if (argEmpInfo) {
			if (newEmployee.flag == newEmployee.ADD) {

			} else if (newEmployee.flag == newEmployee.UPDATE) {
				// form加载数据
				newEmpCodeTxt.setValue(argEmpInfo.newEmpCode)
				formPanel.getForm().loadRecord({
							data : argEmpInfo
						});
				flagHidden.setValue(newEmployee.UPDATE);
				// 加载岗位数据
				stationStore.load({
							params : {
								deptIdForm : argEmpInfo.deptId
							},
							callback : function() {
								stationStore.insert(0, new stationData({
													stationName : '',
													stationId : ''
												}));
								stationCbo.collapse();
								// 设置岗位的值
								setComponentValue(stationCbo,
										argEmpInfo.stationId);
								formPanel.getForm().clearInvalid();
								formRecord = getFormValues(++count);
							}
						});
				// 设置隐藏的中文名
				chsNameHidden.setValue(argEmpInfo.chsName);
				// 设置comboBox的值
				setComponentValue(sexCbo, argEmpInfo.sex);
				setComponentValue(weddedCbo, argEmpInfo.isWedded);
				nativeCbo.store.on('load', function() {
							setComponentValue(nativeCbo,
									argEmpInfo.nativePlaceId);
							formPanel.getForm().clearInvalid();
							formRecord = getFormValues(++count);
						}, this, {
							single : true
						});
				nationCbo.store.on('load', function() {
							setComponentValue(nationCbo,
									argEmpInfo.nationCodeId);
							formPanel.getForm().clearInvalid();
							formRecord = getFormValues(++count);
						}, this, {
							single : true
						});
				politicsCbo.store.on('load', function() {
							setComponentValue(politicsCbo,
									argEmpInfo.politicsId);
							formPanel.getForm().clearInvalid();
							formRecord = getFormValues(++count);
						}, this, {
							single : true
						});
				empTypeCbo.store.on('load', function() {
							setComponentValue(empTypeCbo, argEmpInfo.empTypeId);
							formPanel.getForm().clearInvalid();
							formRecord = getFormValues(++count);
						}, this, {
							single : true
						});
				inTypeCbo.store.on('load', function() {
							setComponentValue(inTypeCbo, argEmpInfo.inTypeId);
							formPanel.getForm().clearInvalid();
							formRecord = getFormValues(++count);
						}, this, {
							single : true
						});
				educationCbo.store.on('load', function() {
							setComponentValue(educationCbo,
									argEmpInfo.educationId);
							formPanel.getForm().clearInvalid();
							formRecord = getFormValues(++count);
						}, this, {
							single : true
						});
				specialtyCbo.store.on('load', function() {
					setComponentValue(specialtyCbo, argEmpInfo.specialtyCodeId);
					formPanel.getForm().clearInvalid();
					formRecord = getFormValues(++count);
				}, this, {
					single : true
				});
				degreeCbo.store.on('load', function() {
							setComponentValue(degreeCbo, argEmpInfo.degreeId);
							formPanel.getForm().clearInvalid();
							formRecord = getFormValues(++count);
						}, this, {
							single : true
						});
				schoolCbo.store.on('load', function() {
							setComponentValue(schoolCbo,
									argEmpInfo.schoolCodeId);
							formPanel.getForm().clearInvalid();
							formRecord = getFormValues(++count);
						}, this, {
							single : true
						});
				// 计算年龄
				if (argEmpInfo.birthday != "" && argEmpInfo.birthday != null) {
					setAge(argEmpInfo.birthday.substring(0, 4));
				}
				// 设置是否存档的选中状态
				if (argEmpInfo.empState == newEmployee.EMP_STATE_2) {
					isSaveYes.setValue(true);
				} else {
					isSaveNo.setValue(true);
				}
				// 设置是否退转军人的选中状态
				if (argEmpInfo.isVeteran == newEmployee.IS_VETERAN_Y) {
					isVeteranYes.setValue(true);
				} else if (argEmpInfo.isVeteran == newEmployee.IS_VETERAN_N) {
					isVeteranNo.setValue(true);
				}
				// 员工姓名不可用
				empNameCbo.setDisabled(true);
				formPanel.getForm().clearInvalid();
				formRecord = getFormValues(count);
			}
		} else {
			flag = true;
		}
	}

	function getFormValues(count) {
		if (count == 10) {
			return formPanel.getForm().getValues();
		} else {
			return "";
		}
	}

	/**
	 * 初始化ComboBox
	 */
	function initComboBox() {
		sexCbo.collapse();
		weddedCbo.collapse();
		setComboBox(nationCbo);
		setComboBox(nativeCbo);
		setComboBox(politicsCbo);
		setComboBox(empTypeCbo);
		setComboBox(inTypeCbo);
		setComboBox(educationCbo);
		setComboBox(specialtyCbo);
		setComboBox(degreeCbo);
		setComboBox(schoolCbo);
	}

	/**
	 * 设置ComboBox
	 */
	function setComboBox(combo) {
		combo.store.removeAll();
		combo.store.load({
					callback : function() {
						combo.collapse();
					}
				});

	}

	/**
	 * 刷新Tab页面
	 */
	function refreshPage() {
		// 重置form
		formPanel.getForm().reset();
		// 重置comboBox
		initComboBox();
		stationStore.removeAll();
		stationCbo.collapse();
		empNameCbo.setDisabled(false);
		flagHidden.setValue(newEmployee.ADD);
		empNameStore.load({
					callback : function() {
						empNameStore.insert(0, new empNameData({
											chsName : '',
											empCode : '',
											empId : '',
											lastModifiyDate : ''
										}));
						empNameCbo.collapse();
						formPanel.getForm().clearInvalid();
					}
				});
		formPanel.getForm().clearInvalid();
	}

	/**
	 * 设置comboBox的值
	 */
	function setComponentValue(component, value) {
		if (value == null) {
			value = "";
		}
		component.setValue(value, true);
	}

	/**
	 * 计算并设置年龄
	 */
	function setAge(birthdayYear) {
		var year = new Date().getYear().toString();
		ageTxt.setValue(year * 1 - birthdayYear * 1);
	}

	/**
	 * 提交前设置Combo的值
	 */
	function setComboValue() {
		// 政治面貌
		if (!politicsCbo.getValue()) {
			politicsCbo.setValue('');
		}
		// 当前学历
		if (!educationCbo.getValue()) {
			educationCbo.setValue('');
		}
		// 学习专业
		if (!specialtyCbo.getValue()) {
			specialtyCbo.setValue('');
		}
		// 学位
		if (!degreeCbo.getValue()) {
			degreeCbo.setValue('');
		}
		// 毕业学校
		if (!schoolCbo.getValue()) {
			schoolCbo.setValue('');
		}
	}

	initPage(newEmployee.empInfo);
});