Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
    Ext.QuickTips.init();
    
    Ext.form.TextField.prototype.width = 180;
    var twiceWd = 453;
    var twoWd = 290;
    var height = 100;
    
    // 员工工号
    var tfEmpCode = new Ext.form.CodeField({
//        id : 'empCode',
    	id : 'newEmpCode',
//        name : 'emp.empCode',
        name : 'emp.newEmpCode',
        fieldLabel : "员工工号",
        maxLength : 20
    });
    
    // 出生日期
    var tfBrithday = new Ext.form.TextField({
        id : 'brithday',
        name : 'emp.brithday',
        style : 'cursor:pointer',
        fieldLabel : "出生日期",
        readOnly : true,
        listeners : {
            focus : function() {
                WdatePicker({
                    startDate : '%y-%M-%d',
                    dateFmt : 'yyyy-MM-dd',
                    onpicked : function() {
                        tfAge.setValue(getAge(this.value));
                    },
                    onclearing:function(){
                        tfAge.setValue('');
                    }
                });
            }
        }
    });
    
    // 选择图片
    var imagePhoto = new Ext.form.TextField({
        id : "imagePhoto",
        labelSeparator : '',
        height : 153,
        style : 'filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale);',
        inputType : 'image'
    });
    // 图片
    var tfPhoto = new Ext.form.TextField({
        id : "photo",
        name : 'photo',
        inputType : 'file',
        labelSeparator : '',
        height : 20,
        initEvents : function(){
        	Ext.form.TextField.prototype.initEvents.apply(this, arguments);
        	var keydown = function(e){
	            e.stopEvent();
	        };
	        this.el.on("keydown", keydown, this);
        }
    });
    
    // 修改照片
    var btnModifyPhto = new Ext.Button({
        text : '修改照片',
        style : 'margin-left:150px;',
        handler : function() {
            var photoDom = Ext.getDom('photo');
            var oldValue = photoDom.value;
            photoDom.click();
            var newValue = photoDom.value;
            if (oldValue != newValue) {
                tfPhoto.fireEvent('change', tfPhoto, newValue, oldValue);
            }
        }
    });
    
    // 员工姓名
    var tfChsName = new Ext.form.TextField({
        id : 'chsName',
        name : 'emp.chsName',
        fieldLabel : "员工姓名",
        readOnly : true
    });
    
    // 年龄
    var tfAge = new Ext.form.TextField({
        id : 'age',
        fieldLabel : "年龄",
        style : 'text-align: right;',
        readOnly : true,
        width : 180 - 30
    });
    var lblAge = new Ext.form.Label({
        id : 'lblAge',
        text : '周岁'
    });
    
    // 英文名
    var tfEnName = new Ext.form.CodeField({
        id : 'enName',
        name : 'emp.enName',
        fieldLabel : "英文名",
        maxLength : 25,
        initEvents : function() {
	        Ext.form.CodeField.superclass.initEvents.call(this);
	        var allowed = /[a-zA-Z 0-9]/;
	        this.stripCharsRe = new RegExp('[^a-zA-Z 0-9]', 'gi');
	        var keyPress = function(e) {
	            var k = e.getKey();
	            if (!Ext.isIE && (e.isSpecialKey() || k == e.BACKSPACE || k == e.DELETE)) {
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
    
    // 性别
    var cbSex = new Ext.form.CmbHRCode({
        id : "sex",
        hiddenName : 'emp.sex',
        fieldLabel : '性别',
        selectOnFocus : true,
        type : '性别'
    });
    
    // 籍贯
    var cbNativePlaceId = new Ext.form.CmbHRBussiness({
        id : "nativePlaceId",
        hiddenName : 'emp.nativePlaceId',
        fieldLabel : '籍贯',
        selectOnFocus : true,
        type : '籍贯'
    });
    
    // 民族
    var cbNationCodeId = new Ext.form.CmbHRBussiness({
        id : "nationCodeId",
        hiddenName : 'emp.nationCodeId',
        fieldLabel : '民族',
        selectOnFocus : true,
        type : '民族'
    });
    
    // 政治面貌
    var cbPoliticsId = new Ext.form.CmbHRBussiness({
        id : "politicsId",
        hiddenName : 'emp.politicsId',
        fieldLabel : '政治面貌',
        selectOnFocus : true,
        type : '政治面貌'
    });
    
    // 婚否状况
    var cbIsWedded = new Ext.form.CmbHRCode({
        id : "isWedded",
        hiddenName : 'emp.isWedded',
        fieldLabel : '婚否状况',
        selectOnFocus : true,
        type : '婚否状况'
    });
    
    // 身份证号
    var tfIdentityCard = new Ext.form.CodeField({
        id : 'identityCard',
        name : 'emp.identityCard',
        fieldLabel : "身份证号",
        maxLength : 18
    });
    
    // 电子邮件
    var tfEMail = new Ext.form.CodeField({
        id : 'eMail',
        name : 'emp.eMail',
        fieldLabel : "电子邮件",
        maxLength : 50,
        initEvents : function() {
	        Ext.form.CodeField.superclass.initEvents.call(this);
	        var allowed = /[-_a-zA-Z0-9@.]/;
	        this.stripCharsRe = new RegExp('[^-_a-zA-Z0-9@.]', 'gi');
	        var keyPress = function(e) {
	            var k = e.getKey();
	            if (!Ext.isIE && (e.isSpecialKey() || k == e.BACKSPACE || k == e.DELETE)) {
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
    
    // 家庭电话
    var tfFamilyTel = new Ext.form.CodeField({
        id : 'familyTel',
        name : 'emp.familyTel',
        fieldLabel : "家庭电话",
        maxLength : 20
    });
    
    // 手机
    var tfMobilePhone = new Ext.form.CodeField({
        id : 'mobilePhone',
        name : 'emp.mobilePhone',
        fieldLabel : "手机",
        maxLength : 20
    });
    
    var panel1 = new Ext.Panel({
    	border : false,
        items : [
            {
                layout : "column",
                border : false,
                items : [{
                    columnWidth : 0.66,
                    layout : "form",
                    border : false,
                    items : [
                    {
                        layout : "column",
                        border : false,
                        items : [{
                                columnWidth : 0.5,
                                layout : "form",
                                border : false,
                                items : [tfEmpCode]
                            }, {
                                columnWidth : 0.5,
                                layout : "form",
                                border : false,
                                items : [tfBrithday]
                            }]
                    }, {
                        layout : "column",
                        border : false,
                        items : [{
                                columnWidth : 0.5,
                                layout : "form",
                                border : false,
                                items : [tfChsName]
                            }, {
                                columnWidth : 0.44,
                                layout : "form",
                                border : false,
                                items : [tfAge]
                            }, {
                                columnWidth : 0.06,
                                layout : 'form',
                                border : false,
                                style : 'padding-top:5;font-size:12;',
                                items : [lblAge]
                            }]
                    }, {
                        layout : "column",
                        border : false,
                        items : [{
                                columnWidth : 0.5,
                                layout : "form",
                                border : false,
                                items : [tfEnName]
                            }, {
                                columnWidth : 0.5,
                                layout : "form",
                                border : false,
                                items : [cbSex]
                            }]
                    }, {
                        layout : "column",
                        border : false,
                        items : [{
                                columnWidth : 0.5,
                                layout : "form",
                                border : false,
                                items : [cbNativePlaceId]
                            }, {
                                columnWidth : 0.5,
                                layout : "form",
                                border : false,
                                items : [cbNationCodeId]
                            }]
                    }, {
                        layout : "column",
                        border : false,
                        items : [{
                                columnWidth : 0.5,
                                layout : "form",
                                border : false,
                                items : [cbPoliticsId]
                            }, {
                                columnWidth : 0.5,
                                layout : "form",
                                border : false,
                                items : [cbIsWedded]
                            }]
                    }, {
                        layout : "column",
                        border : false,
                        items : [{
                                columnWidth : 0.5,
                                layout : "form",
                                border : false,
                                items : [tfIdentityCard]
                            }, {
                                columnWidth : 0.5,
                                layout : "form",
                                border : false,
                                items : [tfEMail]
                            }]
                    }, {
                        layout : "column",
                        border : false,
                        items : [{
                                columnWidth : 0.5,
                                layout : "form",
                                border : false,
                                items : [tfFamilyTel]
                            }, {
                                columnWidth : 0.5,
                                layout : "form",
                                border : false,
                                items : [tfMobilePhone]
                            }]
                    }]
                }, {
                    layout : "form",
                    border : false,
                    items : [imagePhoto, tfPhoto]
                }]
            }
        ]
    
    });
    
    // 邮政编码
    var tfPostalcode = new Ext.form.CodeField({
        id : 'postalcode',
        name : 'emp.postalcode',
        fieldLabel : "邮政编码",
        maxLength : 10
    });
    
    // 家庭住址
    var tfFamilyAddress = new Ext.form.TextField({
        id : 'familyAddress',
        name : 'emp.familyAddress',
        fieldLabel : "家庭住址",
        maxLength : 50,
        width : twiceWd
    });
    
    // 传真
    var tfFax = new Ext.form.CodeField({
        id : 'fax',
        name : 'emp.fax',
        fieldLabel : "传真",
        maxLength : 20
    });
    
    // 办公电话1
    var tfOfficeTel1 = new Ext.form.CodeField({
        id : 'officeTel1',
        name : 'emp.officeTel1',
        fieldLabel : "办公电话1",
        maxLength : 20
    });
    
    // 办公电话2
    var tfOfficeTel2 = new Ext.form.CodeField({
        id : 'officeTel2',
        name : 'emp.officeTel2',
        fieldLabel : "办公电话2",
        maxLength : 20
    });
    
    // 紧急联系人
    var tfInstancyMan = new Ext.form.TextField({
        id : 'instancyMan',
        name : 'emp.instancyMan',
        fieldLabel : "紧急联系人"
    });
    
    // 紧急联系电话
    var tfInstancyTel = new Ext.form.CodeField({
        id : 'instancyTel',
        name : 'emp.instancyTel',
        fieldLabel : "紧急联系电话",
        maxLength : 20
    });
    
    // 档案编号
    var tfArchivesId = new Ext.form.CodeField({
        id : 'archivesId',
        name : 'emp.archivesId',
        fieldLabel : "档案编号",
        maxLength : 50
    });
    
    // 部门Id
    var hideDeptId = new Ext.form.Hidden({
        id : 'deptId'
        // add by liuyi 091221 部门允许修改
        ,name : 'emp.deptId'
    });
    
    // 所属部门
    var tfDeptName = new Ext.form.TextField({
        id : 'deptName',
        name : 'emp.deptName',
        fieldLabel : "所属部门",
        readOnly : true,
        listeners : {
            render : function() {
                Ext.form.TextField.prototype.render.apply(this, arguments);
                
                this.el.on('dblclick', function(){
                	var flagDept = hideDeptId.getValue();
//                	alert(flagDept)
                	var selectedDept = deptSelect();
                	if(selectedDept){
                		tfDeptName.setValue(selectedDept.names)
                		hideDeptId.setValue(selectedDept.ids)
                	}
                	
                	if(flagDept != hideDeptId.getValue())
                	{
                		tfStationId.setValue(null);
                		tfStationName.setValue(null);
                	}
//                	alert(tfDeptName.getValue())
//                	alert(hideDeptId.getValue())
                });
            }
        }
    });
   
    // 工作岗位Id
    var tfStationId = new Ext.form.Hidden({
        id : 'stationId',
        name : 'emp.stationId'
    });
    
    // 工作岗位
    var tfStationName = new Ext.form.TextField({
        id : 'stationName',
        style : 'cursor:pointer',
        fieldLabel : "工作岗位",
        readOnly : true,
        listeners : {
            render : function() {
                Ext.form.TextField.prototype.render.apply(this, arguments);
                
                this.el.on('click', StationNameFocus);
            }
        }
    });
    
    // 位置号
    var tfOrderBy = new Ext.form.TextField({
        id : 'orderBy',
        name : 'emp.orderBy',
        style : 'cursor:pointer;text-align: right;',
        fieldLabel : "位置号",
        readOnly : true,
        listeners : {
            render : function() {
                Ext.form.TextField.prototype.render.apply(this, arguments);
                
                this.el.on('click', orderByFocus);
            }
        }
    });
    
    // 员工身份
    var cbWorkId = new Ext.form.CmbHRBussiness({
        id : "workId",
        hiddenName : 'emp.workId',
        fieldLabel : '员工身份',
        selectOnFocus : true,
        type : '员工身份'
    });
    
    // 员工类别
    var cbEmpTypeId = new Ext.form.CmbHRBussiness({
        id : "empTypeId",
        hiddenName : 'emp.empTypeId',
        fieldLabel : '员工类别',
        selectOnFocus : true,
        type : '员工类别'
    });
    
    // 技术等级
    var cbTechnologyGradeId = new Ext.form.CmbHRBussiness({
        id : "technologyGradeId",
        hiddenName : 'emp.technologyGradeId',
        fieldLabel : '技术等级',
        selectOnFocus : true,
        type : '技术等级'
    });
    
    // 所属工种
    var cbTypeOfWorkId = new Ext.form.CmbHRBussiness({
        id : "typeOfWorkId",
        hiddenName : 'emp.typeOfWorkId',
        fieldLabel : '所属工种',
        selectOnFocus : true,
        type : '工种'
    });
    
    // 劳保工种
    var cbLbWorkId = new Ext.form.CmbHRBussiness({
        id : "lbWorkId",
        hiddenName : 'emp.lbWorkId',
        fieldLabel : '劳保工种',
        selectOnFocus : true,
        type : '劳保工种'
    });
    
    // 技术职称
    var cbTechnologyTitlesId = new Ext.form.CmbHRBussiness({
        id : "technologyTitlesId",
        hiddenName : 'emp.technologyTitlesId',
        fieldLabel : '技术职称',
        selectOnFocus : true,
        type : '技术职称'
    });
    
    // 参加工作日期
    var tfWorkDate = new Ext.form.TextField({
        id : 'workDate',
        name : 'emp.workDate',
        style : 'cursor:pointer',
        fieldLabel : "参加工作日期",
        readOnly : true,
        listeners : {
            focus : function() {
                WdatePicker({
                    startDate : '%y-%M-%d',
                    dateFmt : 'yyyy-MM-dd'
                });
            }
        }
    });
    
    // 进厂日期
    var tfMissionDate = new Ext.form.TextField({
        id : 'missionDate',
        name : 'emp.missionDate',
        style : 'cursor:pointer',
        fieldLabel : "进厂日期",
        readOnly : true,
        listeners : {
            focus : function() {
                WdatePicker({
                    startDate : '%y-%M-%d',
                    dateFmt : 'yyyy-MM-dd'
                });
            }
        }
    });
    
    // 考勤卡号
    var tfTimeCardId = new Ext.form.CodeField({
        id : 'timeCardId',
        name : 'emp.timeCardId',
        fieldLabel : "考勤卡号",
        maxLength : 50
    });
    
    // 工资卡号
    var tfPayCardId = new Ext.form.CodeField({
        id : 'payCardId',
        name : 'emp.payCardId',
        fieldLabel : "工资卡号",
        maxLength : 50
    });
    
    // 参保日期
    var tfSocialInsuranceDate = new Ext.form.TextField({
        id : 'socialInsuranceDate',
        name : 'emp.socialInsuranceDate',
        style : 'cursor:pointer',
        fieldLabel : "参保日期",
        readOnly : true,
        listeners : {
            focus : function() {
                WdatePicker({
                    startDate : '%y-%M-%d',
                    dateFmt : 'yyyy-MM-dd'
                });
            }
        }
    });
    
    // 社保卡号
    var tfSocialInsuranceId = new Ext.form.CodeField({
        id : 'socialInsuranceId',
        name : 'emp.socialInsuranceId',
        fieldLabel : "社保卡号",
        maxLength : 50
    });
    
    // *************modified by liuyi 091201 开始***************************
    // 岗位级别:
		var stationLestore = new Ext.data.Store({
			proxy : new Ext.data.HttpProxy({
				url : 'empInfoManage.action?method=getStationlevel'
			}),
			reader : new Ext.data.JsonReader({
					// id : "plant"
					}, [{
						name : 'id'
					}, {
						name : 'text'
					}])
		});
		stationLestore.load();
	// 标准岗级
	var tfStationGrade = {
		fieldLabel : '标准岗级',
		name : 'stationGrade',
		xtype : 'combo',
		id : 'stationGrade',
		store : stationLestore,
		valueField : "id",
		displayField : "text",
		mode : 'remote',
		typeAhead : true,
		forceSelection : true,
		hiddenName : 'emp.stationGrade',
		editable : false,
		triggerAction : 'all',
//		width : wd,
		blankText : '请选择',
		emptyText : '请选择',
		selectOnFocus : true
	};
	// 执行岗级
	var tfCheckStationGrade = {
		fieldLabel : '执行岗级',
		name : 'checkStationGrade',
		xtype : 'combo',
		id : 'checkStationGrade',
		store : stationLestore,
		valueField : "id",
		displayField : "text",
		mode : 'remote',
		typeAhead : true,
		forceSelection : true,
		hiddenName : 'emp.checkStationGrade',
		editable : false,
		triggerAction : 'all',
//		width : wd,
		blankText : '请选择',
		emptyText : '请选择',
		selectOnFocus : true
	};
	// 薪级store 
	var salaryLevelStore = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
							url : 'empInfoManage.action?method=getSalaryLevelStore'
						}),
				reader : new Ext.data.JsonReader({
						}, [{
							name : 'id'
						}, {
							name : 'text'
						}])
			});
	salaryLevelStore.load();
	// 薪级
	var tfSalaryLevel = {
		fieldLabel : '薪级',
		name : 'salaryLevel',
		xtype : 'combo',
		id : 'salaryLevel',
		store : salaryLevelStore,
		valueField : "id",
		displayField : "text",
		mode : 'local',
		typeAhead : true,
		forceSelection : true,
		hiddenName : 'emp.salaryLevel',
		editable : false,
		triggerAction : 'all',
//		width : wd,
		blankText : '请选择',
		emptyText : '请选择',
		selectOnFocus : true
	};
//    // 执行岗级
//    var tfCheckStationGrade = new Powererp.form.NumField({
//        id : 'checkStationGrade',
//        name : 'emp.checkStationGrade',
//        style : 'text-align: right;',
//        fieldLabel : "执行岗级",
//        maxLength : 10,
//        allowZero : true
//    });
//    
//    // 标准岗级
//    var tfStationGrade = new Powererp.form.NumField({
//        id : 'stationGrade',
//        name : 'emp.stationGrade',
//        style : 'text-align: right;',
//        fieldLabel : "标准岗级",
//        maxLength : 10,
//        allowZero : true
//    });
//    
//    // 薪级
//    var tfSalaryLevel = new Powererp.form.NumField({
//        id : 'salaryLevel',
//        name : 'emp.salaryLevel',
//        style : 'text-align: right;',
//        fieldLabel : "薪级",
//        maxLength : 10,
//        allowZero : true
//    });
    
    // 当前学历
    var cbEducationId = new Ext.form.CmbHRBussiness({
        id : "educationId",
        hiddenName : 'emp.educationId',
        fieldLabel : '当前学历',
        selectOnFocus : true,
        type : '学历'
    });
    
    // 学习专业
    var cbSpecialtyCodeId = new Ext.form.CmbHRBussiness({
        id : "specialtyCodeId",
        hiddenName : 'emp.specialtyCodeId',
        fieldLabel : '学习专业',
        selectOnFocus : true,
        type : '学习专业'
    });
    
    // 学位
    var cbDegreeId = new Ext.form.CmbHRBussiness({
        id : "degreeId",
        hiddenName : 'emp.degreeId',
        fieldLabel : '学位',
        selectOnFocus : true,
        type : '学位'
    });
    
    // 毕业日期
    var tfGraduateDate = new Ext.form.TextField({
        id : 'graduateDate',
        name : 'emp.graduateDate',
        style : 'cursor:pointer',
        fieldLabel : "毕业日期",
        readOnly : true,
        listeners : {
            focus : function() {
                WdatePicker({
                    startDate : '%y-%M-%d',
                    dateFmt : 'yyyy-MM-dd'
                });
            }
        }
    });
    
    // 毕业学校
    var cbSchoolCodeId = new Ext.form.CmbHRBussiness({
        id : "schoolCodeId",
        hiddenName : 'emp.schoolCodeId',
        fieldLabel : '毕业学校',
        selectOnFocus : true,
        type : '学校',
        width : twiceWd
    });
    
    // 是否退转军人
    var raIsVeteran = {
        id : 'isVeteran',
        layout : 'column',
        isFormField : true,
        fieldLabel : '是否退转军人',
        border : false,
        items : [{
            columnWidth : .4,
            border : false,
            items : new Ext.form.Radio({
                id : 'isVeteran1',
                boxLabel : '是',
                name : 'emp.isVeteran',
                inputValue : '1',
                checked : true
            })
        }, {
            columnWidth : .4,
            border : false,
            items : new Ext.form.Radio({
                id : 'isVeteran2',
                boxLabel : '否',
                name : 'emp.isVeteran',
                inputValue : '0'
            })
        }]
    };
    
    // 推荐人
    var tfRecommendMan = new Ext.form.TextField({
        id : 'recommendMan',
        name : 'emp.recommendMan',
        fieldLabel : "推荐人",
        maxLength : 25
    });
    
    // 是否存档
    var raEmpState = {
        id : 'empState',
        layout : 'column',
        isFormField : true,
        fieldLabel : '是否存档',
        border : false,
        items : [{
            columnWidth : .4,
            border : false,
            items : new Ext.form.Radio({
                id : 'empState1',
                boxLabel : '是',
                name : 'emp.empState',
                inputValue : '2',
                checked : true
            })
        }, {
            columnWidth : .4,
            border : false,
            items : new Ext.form.Radio({
                id : 'empState2',
                boxLabel : '否',
                name : 'emp.empState',
                inputValue : '1'
            })
        }]
    };
    
    // 特长
    var tfOneStrongSuit = new Ext.form.TextArea({
        id : 'oneStrongSuit',
        name : 'emp.oneStrongSuit',
        fieldLabel : "特长",
        maxLength : 250,
        width : twoWd,
        height : height
    });
    
    // 备注
    var tfMemo = new Ext.form.TextArea({
        id : 'memo',
        name : 'emp.memo',
        fieldLabel : "备注",
        maxLength : 250,
        width : twoWd,
        height : height
    });
    
    var panel2 = new Ext.Panel({
    	border : false,
        items : [
            {
                layout : "column",
                border : false,
                items : [{
                        columnWidth : 0.33,
                        layout : "form",
                        border : false,
                        items : [tfPostalcode]
                    }, {
                        columnWidth : 0.66,
                        layout : "form",
                        border : false,
                        items : [tfFamilyAddress]
                    }]
            }, {
                layout : "column",
                border : false,
                items : [{
                        columnWidth : 0.33,
                        layout : "form",
                        border : false,
                        items : [tfFax]
                    }, {
                        columnWidth : 0.33,
                        layout : "form",
                        border : false,
                        items : [tfOfficeTel1]
                    }, {
                        columnWidth : 0.33,
                        layout : "form",
                        border : false,
                        items : [tfOfficeTel2]
                    }]
            }, {
                layout : "column",
                border : false,
                items : [{
                        columnWidth : 0.33,
                        layout : "form",
                        border : false,
                        items : [tfInstancyMan]
                    }, {
                        columnWidth : 0.33,
                        layout : "form",
                        border : false,
                        items : [tfInstancyTel]
                    }, {
                        columnWidth : 0.33,
                        layout : "form",
                        border : false,
                        items : [tfArchivesId]
                    }]
            }, {
                layout : "column",
                border : false,
                items : [{
                        columnWidth : 0.33,
                        layout : "form",
                        border : false,
                        items : [hideDeptId, tfDeptName]
                    }, {
                        columnWidth : 0.33,
                        layout : "form",
                        border : false,
                        items : [tfStationId, tfStationName]
                    }, {
                        columnWidth : 0.33,
                        layout : "form",
                        border : false,
                        items : [tfOrderBy]
                    }]
            }, {
                layout : "column",
                border : false,
                items : [{
                        columnWidth : 0.33,
                        layout : "form",
                        border : false,
                        items : [cbWorkId]
                    }, {
                        columnWidth : 0.33,
                        layout : "form",
                        border : false,
                        items : [cbEmpTypeId]
                    }, {
                        columnWidth : 0.33,
                        layout : "form",
                        border : false,
                        items : [cbTechnologyGradeId]
                    }]
            }, {
                layout : "column",
                border : false,
                items : [{
                        columnWidth : 0.33,
                        layout : "form",
                        border : false,
                        items : [cbTypeOfWorkId]
                    }, {
                        columnWidth : 0.33,
                        layout : "form",
                        border : false,
                        items : [cbLbWorkId]
                    }, {
                        columnWidth : 0.33,
                        layout : "form",
                        border : false,
                        items : [cbTechnologyTitlesId]
                    }]
            }, {
                layout : "column",
                border : false,
                items : [{
                        columnWidth : 0.33,
                        layout : "form",
                        border : false,
                        items : [tfWorkDate]
                    }, {
                        columnWidth : 0.33,
                        layout : "form",
                        border : false,
                        items : [tfMissionDate]
                    }, {
                        columnWidth : 0.33,
                        layout : "form",
                        border : false,
                        items : [tfTimeCardId]
                    }]
            }, {
                layout : "column",
                border : false,
                items : [{
                        columnWidth : 0.33,
                        layout : "form",
                        border : false,
                        items : [tfPayCardId]
                    }, {
                        columnWidth : 0.33,
                        layout : "form",
                        border : false,
                        items : [tfSocialInsuranceDate]
                    }, {
                        columnWidth : 0.33,
                        layout : "form",
                        border : false,
                        items : [tfSocialInsuranceId]
                    }]
            }, {
                layout : "column",
                border : false,
                items : [{
                        columnWidth : 0.33,
                        layout : "form",
                        border : false,
                        items : [tfCheckStationGrade]
                    }, {
                        columnWidth : 0.33,
                        layout : "form",
                        border : false,
                        items : [tfStationGrade]
                    }, {
                        columnWidth : 0.33,
                        layout : "form",
                        border : false,
                        items : [tfSalaryLevel]
                    }]
            }, {
                layout : "column",
                border : false,
                items : [{
                        columnWidth : 0.33,
                        layout : "form",
                        border : false,
                        items : [cbEducationId]
                    }, {
                        columnWidth : 0.33,
                        layout : "form",
                        border : false,
                        items : [cbSpecialtyCodeId]
                    }, {
                        columnWidth : 0.33,
                        layout : "form",
                        border : false,
                        items : [cbDegreeId]
                    }]
            }, {
                layout : "column",
                border : false,
                items : [{
                        columnWidth : 0.33,
                        layout : "form",
                        border : false,
                        items : [tfGraduateDate]
                    }, {
                        columnWidth : 0.66,
                        layout : "form",
                        border : false,
                        items : [cbSchoolCodeId]
                    }]
            }, {
                layout : "column",
                border : false,
                items : [{
                        columnWidth : 0.33,
                        layout : "form",
                        border : false,
                        items : [raIsVeteran]
                    }, {
                        columnWidth : 0.33,
                        layout : "form",
                        border : false,
                        items : [tfRecommendMan]
                    }, {
                        columnWidth : 0.33,
                        layout : "form",
                        border : false,
                        items : [raEmpState]
                    }]
            }, {
                layout : "column",
                border : false,
                style : 'padding-top:10px;',
                items : [{
                        columnWidth : 0.465,
                        layout : "form",
                        border : false,
                        items : [tfOneStrongSuit]
                    }, {
                        columnWidth : 0.06
                    }, {
                        columnWidth : 0.465,
                        layout : "form",
                        border : false,
                        items : [tfMemo]
                    }]
        }]
    });
    
    // 上次修改时间
    var hideLastModifiedDate = new Ext.form.Hidden({
        id : 'lastModifiedDate',
        name : 'emp.lastModifiedDate'
    });
    
    // 修改按钮
    var btnModify = new Ext.Button({
        text : Constants.BTN_UPDATE,
        iconCls : Constants.CLS_UPDATE,
        handler : onModify
    });
    
    // 保存按钮
    var btnSave = new Ext.Button({
        text : Constants.BTN_SAVE,
        iconCls : Constants.CLS_SAVE,
        handler : onSave
    });
    
    // 打印按钮
    var btnPrint = new Ext.Button({
        text : '打印员工履历表',
        iconCls : Constants.CLS_PRINT,
        handler : onPrint
    });
    
    // head工具栏
    var headTbar = new Ext.Toolbar({
        region : 'north',
        items : [btnModify, btnSave, '->', btnPrint]
    });
    
    var formPanel = new Ext.FormPanel({
        labelAlign : 'right',
        labelWidth : 80,
        width : 830,
        border : false,
//        bodyStyle : 'padding-top:2;',
        fileUpload : true,
        items : [panel1, panel2, hideLastModifiedDate]
    });
    
    // 设定布局器及面板
    var layout = new Ext.Viewport({
        layout : "border",
        defaults : {autoScroll: true},
        items : [headTbar, {
            region : 'center',
            layout : 'form',
            border : false,
            frame : true,
            items : [formPanel]
        }]
    });
    
    // ==========       处理开始       =============
    var employee = parent.Ext.getCmp('tabPanel').employee;
    // 添加加载员工综合信息时的监听器
    employee.addLoadEmpHandler(loadEmp);
    // 添加加载员工综合信息前的监听器
    employee.addBeforeLoadEmpHandler(beforLoadEmp);
    
    // 员工信息
    function EmployeeInfo() {
        // 出生日期
        this.brithday = "";
        // 图片
        this.photoPath = "";
        // 人员姓名
//        this.chsName = "";
        // 英文名
        this.enName = "";
        // 性别
        this.sex = "";
        // 籍贯
        this.nativePlaceId = "";
        // 民族
        this.nationCodeId = "";
        // 政治面貌
        this.politicsId = "";
        // 身份证
        this.identityCard = "";
        // 婚否状况
        this.isWedded = "";
        // 家庭电话
        this.familyTel = "";
        // 手机
        this.mobilePhone = "";
        // 电子邮件
        this.eMail = "";
        // 邮政编码
        this.postalcode = "";
        // 家庭住址
        this.familyAddress = "";
        // 传真
        this.fax = "";
        // 办公电话1
        this.officeTel1 = "";
        // 办公电话2
        this.officeTel2 = "";
        // 紧急联系人
        this.instancyMan = "";
        // 紧急联系电话
        this.instancyTel = "";
        // 档案编号
        this.archivesId = "";
        // 岗位Id
        this.stationId = "";
        // 显示顺序
        this.orderBy = "";
        // 员工身份Id
        this.workId = "";
        // 员工类别Id
        this.empTypeId = "";
        // 技术等级Id
        this.technologyGradeId = "";
        // 工种Id
        this.typeOfWorkId = "";
        // 劳保工种Id
        this.lbWorkId = "";
        // 技术职称Id
        this.technologyTitlesId = "";
        // 参加工作时间
        this.workDate = "";
        // 入职时间
        this.missionDate = "";
        // 考勤卡号
        this.timeCardId = "";
        // 工资卡号
        this.payCardId = "";
        // 参保时间
        this.socialInsuranceDate = "";
        // 社保卡号
        this.socialInsuranceId = "";
        // 执行岗级
        this.checkStationGrade = "";
        // 标准岗级
        this.stationGrade = "";
        // 薪级
        this.salaryLevel = "";
        // 学历Id
        this.educationId = "";
        // 学习专业Id
        this.specialtyCodeId = "";
        // 学位Id
        this.degreeId = "";
        // 毕业时间
        this.graduateDate = "";
        // 学校编码Id
        this.schoolCodeId = "";
        // 是否退转军人
        this.isVeteran = "";
        // 推荐人
        this.recommendMan = "";
        // 特长
        this.oneStrongSuit = "";
        // 备注
        this.memo = "";
        // 是否存档
        this.empState = "";
        
        if (typeof EmployeeInfo._intialized == 'undefined') {
            EmployeeInfo._intialized = true;
            
            // 清空对象
            EmployeeInfo.prototype.init = function() {
                for (var prop in this) {
                    if (typeof this[prop] != 'function') {
                        this[prop] = "";
                    }
                }
            }
            
            // 加载对象
            EmployeeInfo.prototype.loadData = function(argRecord) {
                var data = argRecord;
                if (argRecord.data) {
                    data = argRecord.data;
                }
                // 清空对象
                this.init();
                for (var prop in this) {
                    if (typeof this[prop] != 'function') {
                        var otherV = data[prop] || data['emp.' + prop];
                        if (otherV === 'undefined'
                            || otherV === 'null'
                            || otherV == null) {
                            otherV = "";
                        }
                        // 执行岗级, 标准岗级, 薪级
	                    if (prop === 'checkStationGrade'
	                    	|| prop == 'stationGrade'
	                    	|| prop == 'salaryLevel') {
	                    	// 删除逗号
	                    	otherV = String(otherV).replace(/,/g, '');
	                	}
                        
                        this[prop] = otherV;
                    }
                }
            }
            
            // 比较对象
            EmployeeInfo.prototype.compare = function(argRecord) {
                var data = argRecord;
                if (argRecord.data) {
                    data = argRecord.data;
                }
                for (var prop in this) {
                    var otherV = data[prop] || data['emp.' + prop];
                    if (otherV === 'undefined'
                        || otherV === 'null'
                        || otherV == null) {
                        otherV = "";
                    }
                    // 执行岗级, 标准岗级, 薪级
                    if (prop === 'checkStationGrade'
                    	|| prop == 'stationGrade'
                    	|| prop == 'salaryLevel') {
                    	// 删除逗号
                    	otherV = String(otherV).replace(/,/g, '');
                	}
                    
                    if (typeof this[prop] != 'function'
                        && this[prop] != otherV) {
//                        alert(prop + ': \"' + this[prop] + '\" \"' + otherV + '\"')
                        return false;
                    }
                }
                return true;
            }
        }
    }
    
    // 员工岗位信息
    function EmployeeExtraData() {
        // 职工岗位ID
        this.empStationId = "";
        // 岗位ID
        this.stationId = "";
        // 人员ID
        this.empId = "";
        // 岗位名称
        this.stationName = "";
        // 是否主岗位
        this.isMainStation = "";
        // 备注
        this.memo = "";
        // 职工岗位上次修改时间
        this.lastModifiedDate = "";
        // DB操作flag
        this.operateFlag = "";
    }
    
    // 初始化员工信息
    var origialEmp = new EmployeeInfo();
    // 初始化员工岗位信息
    var empExtraData = null;
    // 员工岗位上次的信息
    var empLastExtraData = null;
    
    // 图片路径变更
    Ext.get('photo').on('change', function(field, newValue, oldValue) {
            var url = Ext.get('photo').dom.value;
            var image = Ext.get('imagePhoto').dom;
            
            if(Ext.isIE7){
                 image.src = Ext.BLANK_IMAGE_URL;//覆盖原来的图片   
                 image.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src= url; 
            } else {
                 image.src = url;
            }
        }
    );
    
    // 工作岗位
    function StationNameFocus() {
        var args = {};
        if (empLastExtraData) {
        	args = {objects: empLastExtraData};
        } else {
	        // 部门ID
	        args.deptId = hideDeptId.getValue();
	        // 人员ID
	        args.empId = employee.empId;
	        // 第一次
	        args.first = true;
        }
        var object = window.showModalDialog('../../empstationmaintain/PD003.jsp',
                args, 'dialogWidth=700px;dialogHeight=520px;center=yes;help=no;resizable=no;status=no;');
        
        if (object) {
        	var lastEmpExtraData = empExtraData;
            empExtraData = [];
            
        	var station = null;
        	var mainStation = null;
        	var updateCount = 0;
        	var lastStationId = tfStationId.getValue();
        	for (var i = 0; i < object.length; i++) {
        		station = object[i];
    			if (!station.existFlag) {
    				continue;
    			}
        		if (station.isLeft !== 'false' && station.existFlag !== 'D') {
    				continue;
    			}
    			if (station.isLeft === 'false' && station.mainStation === 'Y') {
    				mainStation = station;
    			} else {
    				station.mainStation = 'N';
    			}
    			
    			// 职工岗位对象
    			var objEmpSta = new EmployeeExtraData();
    			empExtraData.push(objEmpSta);
    			// 职工岗位ID
		        objEmpSta.empStationId = station.empStationId;
		        // 岗位ID
		        objEmpSta.stationId = station.stationId;
		        // 人员ID
	        	objEmpSta.empId = employee.empId;
		        // 岗位名称
		        objEmpSta.stationName = station.stationName;
		        // 是否主岗位
		        objEmpSta.isMainStation = station.mainStation;
		        // 备注
		        objEmpSta.memo = station.memo;
		        // 职工岗位上次修改时间
		        objEmpSta.lastModifiedDate = station.lastModifyDate;
		        // DB操作flag
		        objEmpSta.operateFlag = station.existFlag;
		        
		        if (objEmpSta.operateFlag) {
	        		updateCount++;
	        	}
        	}
			
        	if (updateCount < 1) {
        		empExtraData = lastEmpExtraData;
        		return false;	
        	}
            // 设置职工岗位
            empLastExtraData = object;
            if (mainStation) {
            	// 如果主岗位已修改
		        tfStationId.setValue(mainStation.stationId);
	            tfStationName.setValue(mainStation.stationName);
            }
        }
        return false;
    }
    
    // 位置号
    function orderByFocus() {
        var args = {};
        // 部门ID
        args.deptId = hideDeptId.getValue();
        // 人员ID
        args.empId = employee.empId;
        var object = window.showModalDialog('../../../../common/PC001.jsp',
                args, 'dialogWidth=400px;dialogHeight=220px;center=yes;help=no;resizable=no;status=no;');
        
        if (object) {
        	if (typeof object.lastModifiyDate != 'undefined') {
        		// 设置最后修改时间
        		hideLastModifiedDate.setValue(object.lastModifiyDate);
	            // 设置位置号
	            tfOrderBy.setValue(object.orderBy);
	            // 设置初始化员工位置号
	            origialEmp.orderBy = object.orderBy;
        	} else {
	            // 设置位置号
	            tfOrderBy.setValue(object);
	            // 设置初始化员工位置号
	            origialEmp.orderBy = object;
        	}
        }
        return false;
    }
    
    // textField显示时间比较方法
    function compareCurrent(argDateStr1){
        var date1 = Date.parseDate(argDateStr1, 'Y-m-d');
        var date2 = new Date();
        return date1.getTime() < date2.getTime();
    }
    
    // 计算年龄
    function getAge(argDate) {
        var value = argDate;
        if (!value) {
            return '';
        }
        if (value instanceof Date) {
            value = value.dateFormat('Y-m');
        }
        
        var now = new Date();
        var age = now.dateFormat('Y') - Number(value.substring(0, 4));
//        age += Math.ceil((now.dateFormat('m') - Number(value.substring(5, 7))) /12);
        return age;
    }
    
    // 清除所有项目
    function clearAllFields() {
        formPanel.getForm().reset();

        // 清除附件内容
        var domAppend = tfPhoto.el.dom;
        var parent = domAppend.parentNode;
        // 保存
        var domForSave = domAppend.cloneNode();
        // 移除附件控件
        parent.removeChild(domAppend);
        // 再追加控件
        parent.appendChild(domForSave);
        // 应用该控件
        tfPhoto.applyToMarkup(domForSave);

        // 清除图片内容
        domAppend = imagePhoto.el.dom;
        parent = domAppend.parentNode;
        // 保存
        domForSave = domAppend.cloneNode();
        // 移除附件控件
        parent.removeChild(domAppend);
        // 再追加控件
        parent.appendChild(domForSave);
        // 应用该控件
        imagePhoto.applyToMarkup(domForSave);
    	
        // 初始化员工岗位信息
        empExtraData = null;
        empLastExtraData = null;
    }
    
    // 检查画面是否变化
    function checkPageChanged() {
        var formData = formPanel.getForm().getValues();
        formData.photoPath = tfPhoto.getValue();
        
        if (!origialEmp.compare(formData)) {
            return true;
        }
        
        return !!empExtraData;
    }
    
    // 设置画面控件的值
    function setPageDate(record) {
        if (!record.modifiedDate) {
//        	alert(Ext.util.JSON.encode(record))
//        	alert(record.empState)
        	if(record.empState == 3){
        		Ext.Msg.alert('提示信息','该员工已离职！');
        	}
            // 清除所有项目
            clearAllFields();
            // 加载对象
            origialEmp.loadData(record);
            formPanel.getForm().setValues(record);
            // 设置年龄
            tfAge.setValue(getAge(record.brithday));
            if (record.EMail) {
                // 设置电子邮件
                tfEMail.setValue(record.EMail);
                origialEmp.eMail = record.EMail;
            }
            
            // 性别
            cbSex.setValue(record['sex'], true);
            // 籍贯
            cbNativePlaceId.setValue(record['nativePlaceId'], true);
            // 民族
            cbNationCodeId.setValue(record['nationCodeId'], true);
            // 政治面貌
            cbPoliticsId.setValue(record['politicsId'], true);
            // 婚否状况
            cbIsWedded.setValue(record['isWedded'], true);
            // 员工身份
            cbWorkId.setValue(record['workId'], true);
            // 员工类别
            cbEmpTypeId.setValue(record['empTypeId'], true);
            // 技术等级
            cbTechnologyGradeId.setValue(record['technologyGradeId'], true);
            // 所属工种
            cbTypeOfWorkId.setValue(record['typeOfWorkId'], true);
            // 劳保工种
            cbLbWorkId.setValue(record['lbWorkId'], true);
            // 技术职称
            cbTechnologyTitlesId.setValue(record['technologyTitlesId'], true);
            // 当前学历
            cbEducationId.setValue(record['educationId'], true);
            // 学习专业
            cbSpecialtyCodeId.setValue(record['specialtyCodeId'], true);
            // 学位
            cbDegreeId.setValue(record['degreeId'], true);
            // 毕业学校
            cbSchoolCodeId.setValue(record['schoolCodeId'], true);
            
            // 是否退转军人
            if (record['isVeteran'] == '0') {
                Ext.getCmp('isVeteran2').setValue(true);
            } else {
            	origialEmp.isVeteran = '1';
            }
            // 是否存档
            if (record['empState'] != '2') {
                Ext.getCmp('empState2').setValue(true);
                origialEmp.empState = '1';
            } else {
                Ext.getCmp('empState2').setDisabled(true);
            }
        } else {
            // 设置修改时间
            hideLastModifiedDate.setValue(record.modifiedDate);
            
            var formData = formPanel.getForm().getValues();
            formData.photoPath = tfPhoto.getValue();
            // 加载对象
            origialEmp.loadData(formData);
        }
        
        setEnable(false);
        // 修改按钮
        btnModify.setDisabled(false);
        // 打印按钮
        btnPrint.setDisabled(false);
        // 初始化员工岗位信息
        empExtraData = null;
        empLastExtraData = null;
    }
    
    // 提交前设置Combo的值，保证其以Number类型传回服务端
    function setComboValue() {
        // 性别
        if (!cbSex.getValue()) {
            cbSex.setValue('');
        }
        // 籍贯
        if (!cbNativePlaceId.getValue()) {
            cbNativePlaceId.setValue('');
        }
        // 民族
        if (!cbNationCodeId.getValue()) {
            cbNationCodeId.setValue('');
        }
        // 政治面貌
        if (!cbPoliticsId.getValue()) {
            cbPoliticsId.setValue('');
        }
        // 婚否状况
        if (!cbIsWedded.getValue()) {
            cbIsWedded.setValue('');
        }
        // 员工身份
        if (!cbWorkId.getValue()) {
            cbWorkId.setValue('');
        }
        // 员工类别
        if (!cbEmpTypeId.getValue()) {
            cbEmpTypeId.setValue('');
        }
        // 技术等级
        if (!cbTechnologyGradeId.getValue()) {
            cbTechnologyGradeId.setValue('');
        }
        // 所属工种
        if (!cbTypeOfWorkId.getValue()) {
            cbTypeOfWorkId.setValue('');
        }
        // 劳保工种
        if (!cbLbWorkId.getValue()) {
            cbLbWorkId.setValue('');
        }
        // 技术职称
        if (!cbTechnologyTitlesId.getValue()) {
            cbTechnologyTitlesId.setValue('');
        }
        // 当前学历
        if (!cbEducationId.getValue()) {
            cbEducationId.setValue('');
        }
        // 学习专业
        if (!cbSpecialtyCodeId.getValue()) {
            cbSpecialtyCodeId.setValue('');
        }
        // 学位
        if (!cbDegreeId.getValue()) {
            cbDegreeId.setValue('');
        }
        // 毕业学校
        if (!cbSchoolCodeId.getValue()) {
            cbSchoolCodeId.setValue('');
        }
        
        // 标准岗级
//        alert(!Ext.getCmp('stationGrade').getValue())
        if(!Ext.getCmp('stationGrade').getValue())
        {
        	Ext.getCmp('stationGrade').setValue('');
        }
        // 执行岗级
//        alert(!Ext.getCmp('checkStationGrade').getValue())
        if(!Ext.getCmp('checkStationGrade').getValue())
        {
        	Ext.getCmp('checkStationGrade').setValue('');
        }
        // 薪级
//        alert(!Ext.getCmp('salaryLevel').getValue())
        if(!Ext.getCmp('salaryLevel').getValue())
        {
        	Ext.getCmp('salaryLevel').setValue('');
        }
    }
    
    // 加载员工前的处理
    function beforLoadEmp(argEmpCode) {
    	formPanel.getForm().trim();
        if (!btnModify.disabled
            || btnModify.hidden
            || !checkPageChanged()) {
            return true;
        }
        
        return false;
    }
    
    // 加载员工基本信息
    function loadEmp() {
        setEnable(false);
        if (!employee.hasEmpId()) {
            // 清除所有项目
            clearAllFields();
            
            origialEmp.init();
            return;
        }
        
        // 加载数据
        Ext.Ajax.request({
            method : Constants.POST,
            url : 'hr/getEmpMaintBaseInfo.action',
            params : {empId : employee.empId},
            success : function(result, request) {
                var record = eval('(' + result.responseText + ')');
                // 数据库异常
                if(record && typeof record.msg != 'undefined' && record.msg == "SQL") {
                    employee.alert(Constants.ERROR, Constants.COM_E_014);
                    return;
                }
                if (!record) {
                    // 清除所有项目
                    clearAllFields();
                    
                    origialEmp.init();
                    // 设置员工名字
                    employee.changeName('');
                    return;
                }
                // 设置画面控件的值
                setPageDate(record);
                // 设置员工名字
                employee.changeName(record.chsName);
//                alert(Ext.encode(record))
                employee.changeDeptName(record.deptName)
            },
            failure : function() {
                employee.alert(Constants.ERROR, Constants.UNKNOWN_ERR);
            }
        });
        
        // 加载图片
        Ext.get('imagePhoto').dom.src = "hr/getEmpPhotoInfo.action?empId="
            + employee.empId + "&time=" + new Date().getTime();
    }
    
    // 修改按钮处理
    function onModify() {
        setEnable(true);
        // 修改按钮
        btnModify.setDisabled(true);
    }
    
    // Check处理
    function checkFields() {
        var msg = '';
        // 上传图片文件名是否正确
        var photoPath = tfPhoto.getValue();
        if (photoPath && !checkFilePath(photoPath)) {
            msg += String.format(Constants.COM_E_025, photoPath) + '<br/>';
        }
        
        if (msg.length > 0) {
            employee.alert(Constants.ERROR, msg.replace(/<br\/>$/, ''));
            return false;
        }
        return true;
    }
    
    // 保存按钮处理
    function onSave() {
    	if (!checkFields()) {
            return;
        }
        if(tfEmpCode.getValue() == null || tfEmpCode.getValue() == ''){
        	Ext.Msg.alert('提示','工号不可为空！');
        	return;
        }
        employee.confirm(Constants.CONFIRM, Constants.COM_C_001,
            function(buttonobj) {
                if (buttonobj == "yes") {
                    var params = {
                        'emp.empId' : employee.empId,
                        'filePath' : tfPhoto.getValue()
                        // add by liuyi 20100406 新工号
                        ,'newEmpCode' : tfEmpCode.getValue()
                    };
                    if (empExtraData) {
                        params.empStationOperInfo = Ext.encode(empExtraData);
                    }
                    // 提交前设置Combo的值
                    setComboValue();
                    
                    // 保存数据
                    formPanel.getForm().submit({
                        method : Constants.POST,
                        url : 'hr/saveEmpMaintBaseInfo.action',
                        params : params,
                        success : function(form, action) {
                            var o = eval('(' + action.response.responseText + ')');
                            
                            // 排他异常
                            if(o.msg == "U") {
                                employee.alert(Constants.ERROR, Constants.COM_E_015);
                                return;
                            }
                            if (o.msg == Constants.IO_FAILURE || o.msg == Constants.SQL_FAILURE) {
			                    // 操作失败
			                    employee.alert(Constants.ERROR, Constants.COM_E_014);
			                    return;
			                }
                            if (o.msg == Constants.FILE_NOT_EXIST) {
			                    // 文件不存在
			                    employee.alert(Constants.ERROR, String.format(Constants.COM_E_025, tfPhoto.getValue()));
			                    return;
			                }
                            
                            employee.alert(Constants.REMIND, Constants.COM_I_004);
                            // 设置画面控件的值
                            setPageDate(o);
                        }
                    });
                }
            }
        );
    }
    
    // 打印按钮处理
    function onPrint() {
        employee.print();
    }
    
    // 设置Form是否可以编辑
    function setEnable(argFlag) {
        formPanel.getForm().items.each(function(f) {
            var xtype = f.getXType();
            if (f.el.dom && (xtype == 'textfield'
                || xtype == 'CodeField'
                || xtype == 'numfield'
                || xtype == 'textarea'
                || xtype == 'radio'
                || xtype == 'NaturalNumberField'
                || xtype == 'CmbHRBussiness'
                || xtype == 'CmbHRCode'
                || xtype == 'combo')) {
                f.setDisabled(!argFlag);
            }
        });
        
        // 修改照片按钮
        btnModifyPhto.setDisabled(!argFlag);
        // 修改按钮
        btnModify.setDisabled(!argFlag);
        // 保存按钮
        btnSave.setDisabled(!argFlag);
        // 是否存档
        if (origialEmp.empState == '2') {
            Ext.getCmp('empState2').setDisabled(true);
        }
        
        // 员工工号  启用新工号 此处可修改
//        tfEmpCode.setDisabled(true);
        // 人员姓名
        tfChsName.setDisabled(true);
        // 年龄
        tfAge.setDisabled(true);
        // 所属部门
        // modified by liuyi 091221
//        tfDeptName.setDisabled(true);
    }
    // ==========       处理结束       =============
    
    
    // ==========       初期化处理        ===========
    // 打印按钮
    btnPrint.setDisabled(true);
    // 加载员工基本信息
    loadEmp();
    if (employee.editable) {
        // 打印按钮不可用
        btnPrint.setVisible(false);
        // 修改按钮可用
        btnModify.setVisible(true);
        // 保存按钮可用
        btnSave.setVisible(true);
    } else {
        // 打印按钮可用
        btnPrint.setVisible(true);
        // 修改按钮不可用
        btnModify.setVisible(false);
        // 保存按钮不可用
        btnSave.setVisible(false);
    }
    
    // 右键禁用
     document.onkeydown = function()
 {
          if(event.keyCode==116) {
          event.keyCode=0;
          event.returnValue = false;
          }
}
document.oncontextmenu = function() {event.returnValue = false;} 

	function deptSelect() {
		    	var args = {
		    		selectModel : 'single',
		    		rootNode : {
		    			id : '0',
		    			text : '灞桥电厂'
		    		}
		    	};
		    	// 调用画面
				var object = window.showModalDialog(
								'../../../../../comm/jsp/hr/dept/dept.jsp',
								args,
								'dialogWidth='  + Constants.WIDTH_COM_DEPT +
								'px;dialogHeight=' + Constants.HEIGHT_COM_DEPT +
								'px;center=yes;help=no;resizable=no;status=no;');
				// 根据返回值设置画面的值
				if (object) {
					return object;
//					if (typeof(object.names) != "undefined") {
//						this.name = object.names;
//					}
//					if (typeof(object.ids) != "undefined") {
//						this.id = object.ids;
//					}
//					return true;
				} 
		    }
});
