Ext.onReady(function() {
	// 定义树结构
	var _deptId = 0;

	
		var wd = 150;
		// 员工基本信息
		// 第一列
		// 人员ID
		var empId = {
			id : "empId",
			value : id,
			xtype : "hidden",
			fieldLabel : '人员ID',
			name : 'empinfo.empId',
			width : wd,
			readOnly : true

		}
		// 中文名

		var chsName = {
			id : "chsName",
			xtype : "textfield",
			fieldLabel : "中文名<font color='red'>*</font>",
			name : 'empinfo.chsName',
			width : wd,
			allowBlank : false

		}

		// 英文名

		var enName = {
			id : "enName",
			xtype : "textfield",
			fieldLabel : '英文名',
			name : 'empinfo.enName',
			width : wd

		}

		// 出身日期
		var brithday = {
			id : 'brithday',
			xtype : "datefield",
			fieldLabel : '出生日期',
			name : 'empinfo.brithday',
			format : 'Y-m-d',
			width : wd,
			readOnly : true
				// anchor:'30%'
		};

		// 民族
		var comStore = new Ext.data.Store({
			proxy : new Ext.data.HttpProxy({
				url : 'empInfoManage.action?method=getnation'
			}),
			reader : new Ext.data.JsonReader({
					// id : "plant"
					}, [{
						name : 'id'
					}, {
						name : 'text'
					}])
		});
		comStore.load();
		var nationId = {
			xtype : 'combo',
			id : 'nationId',
			fieldLabel : '民族',
			store : comStore,
			valueField : "id",
			displayField : "text",
			mode : 'remote',
			typeAhead : true,
			forceSelection : true,
			hiddenName : 'empinfo.nationId',
			editable : false,
			triggerAction : 'all',
			width : wd,
			blankText : '请选择',
			emptyText : '请选择',
			selectOnFocus : true

		};
		// 政治面貌
		var politicsstore = new Ext.data.Store({
			proxy : new Ext.data.HttpProxy({
				url : 'empInfoManage.action?method=getpolitics'
			}),
			reader : new Ext.data.JsonReader({
					// id : "plant"
					}, [{
						name : 'id'
					}, {
						name : 'text'
					}])
		});
		politicsstore.load();
		var politicsId = {
			fieldLabel : '政治面貌',
			name : 'empinfo.politicsId',
			xtype : 'combo',
			id : 'politicsId',
			store : politicsstore,
			valueField : "id",
			displayField : "text",
			mode : 'remote',
			typeAhead : true,
			forceSelection : true,
			hiddenName : 'empinfo.politicsId',
			editable : false,
			triggerAction : 'all',
			width : wd,
			blankText : '请选择',
			emptyText : '请选择',
			selectOnFocus : true

		};

		// 性别
		var sex = {
			id : 'sex',
			layout : 'column',
			isFormField : true,
			fieldLabel : '性别',
			border : false,
			items : [{
				columnWidth : .3,
				border : false,
				items : new Ext.form.Radio({
					id : 'sex1',
					boxLabel : '男',

					name : 'empinfo.sex',
					inputValue : 'M',
					checked : true
				})
			}, {

				columnWidth : .3,
				border : false,

				items : new Ext.form.Radio({
					id : 'sex2',
					boxLabel : '女',
					name : 'empinfo.sex',
					inputValue : 'W'
				})
			}]
		};

		// 手机
		var mobilePhone = {
			id : "mobilePhone",
			xtype : "textfield",
			fieldLabel : '手机',
			width : wd,
			name : 'empinfo.mobilePhone'
		};

		// 办公电话1
		var officeTel1 = {
			id : "officeTel1",
			xtype : "textfield",
			fieldLabel : '办公电话1',
			width : wd,
			name : 'empinfo.officeTel1'
		};

		// 办公电话2

		var officeTel2 = {
			id : "officeTel2",
			xtype : "textfield",
			fieldLabel : '办公电话2',
			width : wd,
			name : 'empinfo.officeTel2'
		};

		// 身份证:
		var identityCard = {
			id : "identityCard",
			xtype : "textfield",
			fieldLabel : "身份证<font color='red'>*</font>",
			name : 'empinfo.identityCard',
			width : wd,
			allowBlank : false
		};

		// 第二列

		// 员工工号

		var empCode = {
			id : "empCode",
			xtype : "textfield",
			fieldLabel : "员工工号<font color='red'>*</font>",
			name : 'empinfo.empCode',
			width : wd,
			allowBlank : false
		};

		// 档案编号:
		var archivesId = {
			id : "archivesId",
			xtype : "textfield",
			fieldLabel : '档案编号',
			width : wd,
			name : 'empinfo.archivesId'
		};

		// 检索码:
		var retrieveCode = {
			id : "retrieveCode",
			xtype : "textfield",
			fieldLabel : '检索码',
			width : wd,
			name : 'empinfo.retrieveCode'
		};

		// 籍贯:

		var nativestore = new Ext.data.Store({
			proxy : new Ext.data.HttpProxy({
				url : 'empInfoManage.action?method=getnativeplace'
			}),
			reader : new Ext.data.JsonReader({
					// id : "plant"
					}, [{
						name : 'id'
					}, {
						name : 'text'
					}])
		});
		nativestore.load();
		var nativePlaceId = {
			fieldLabel : '籍贯',
			name : 'empinfo.nativePlaceId',
			xtype : 'combo',
			id : 'nativePlaceId',
			store : nativestore,
			valueField : "id",
			displayField : "text",
			mode : 'remote',
			typeAhead : true,
			forceSelection : true,
			hiddenName : 'empinfo.nativePlaceId',
			editable : false,
			triggerAction : 'all',
			width : wd,
			blankText : '请选择',
			emptyText : '请选择',
			selectOnFocus : true
		};

		// 婚姻状况:
		var isWedded = {
			id : 'isWedded',
			layout : 'column',
			isFormField : true,
			fieldLabel : '婚姻状况',
			border : false,
			items : [{
				columnWidth : .3,
				border : false,
				items : new Ext.form.Radio({
					id : 'isWedded1',
					boxLabel : '未婚',

					name : 'empinfo.isWedded',
					inputValue : '0',
					checked : true
				})
			}, {

				columnWidth : .3,
				border : false,

				items : new Ext.form.Radio({
					id : 'isWedded2',
					boxLabel : '已婚',
					name : 'empinfo.isWedded',
					inputValue : '1'
				})
			}]
		};

		// 照片:
		var photo = {
			id : "photo",
			xtype : "textfield",
			inputType : 'file',
			fieldLabel : '照片',
			width : wd,
			name : 'photo'
		};

		var imagephoto = {
			id : "imagephoto",
			xtype : "textfield",
			fieldLabel : '照片',
			hideLabel : true,
			width : 200,
			height : 240,
			autoCreate : {
				tag : 'input',
				type : 'image',
				src : '/power/comm/images/UnknowBody.jpg',
				style : 'filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale);',
				name : 'imagephoto' 
			}  
		};

		// 联系电话:
		var instancyTel = {
			id : "instancyTel",
			xtype : "textfield",
			fieldLabel : '联系电话',
			width : wd,
			name : 'empinfo.instancyTel'
		};

		// 家庭住址:
		var familyAddress = {
			id : "familyAddress",
			xtype : "textfield",
			fieldLabel : '家庭住址',
			width : wd,
			name : 'empinfo.familyAddress'
		};

		// 电子邮件:
		var EMail = {
			id : "EMail",
			xtype : "textfield",
			fieldLabel : '电子邮件',
			width : wd,
			name : 'empinfo.EMail'
		};

		// 员工企业信息
		// 第一列

		var deptId = new Ext.ux.ComboBoxTree({
			fieldLabel : '所属部门',
			id : 'deptId',
			displayField : 'text',
			valueField : 'id',
			hiddenName : 'empinfo.deptId',
			blankText : '请选择',
			emptyText : '请选择',
			// value:{id:'0',text:'合肥电厂',attributes:{description:'deptName'}},
			tree : {
				xtype : 'treepanel',
				loader : new Ext.tree.TreeLoader({
					dataUrl : 'empInfoManage.action?method=getDep'
				}),
				root : new Ext.tree.AsyncTreeNode({
					id : '0',
					name : '合肥电厂',
					text : '合肥电厂'
				})
			},
			selectNodeModel : 'all'
		})

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
		var stationLevel = {
			fieldLabel : '岗位级别',
			name : 'empinfo.stationLevel',
			xtype : 'combo',
			id : 'stationLevel',
			store : stationLestore,
			valueField : "id",
			displayField : "text",
			mode : 'remote',
			typeAhead : true,
			forceSelection : true,
			hiddenName : 'empinfo.stationLevel',
			editable : false,
			triggerAction : 'all',
			width : wd,
			blankText : '请选择',
			emptyText : '请选择',
			selectOnFocus : true
		};
		// 岗位分位:
		var gradation = {
			id : "gradation",
			display : 'none',
			xtype : "numberfield",
			fieldLabel : '岗位分位',
			width : wd,
			name : 'empinfo.gradation'
		};
		// 岗位编号:
		var empStationId = {
			id : "empStationId",
			xtype : "numberfield",
			fieldLabel : '岗位编号',
			width : wd,
			name : 'empinfo.empStationId'
		};

		// 技术等级

		var technologyGradestore = new Ext.data.Store({
			proxy : new Ext.data.HttpProxy({
				url : 'empInfoManage.action?method=getTechnologylevel'
			}),
			reader : new Ext.data.JsonReader({
					// id : "plant"
					}, [{
						name : 'id'
					}, {
						name : 'text'
					}])
		});
		technologyGradestore.load();
		var technologyGradeId = {
			fieldLabel : '技术等级',
			name : 'empinfo.technologyGradeId',
			xtype : 'combo',
			id : 'technologyGradeId',
			store : technologyGradestore,
			valueField : "id",
			displayField : "text",
			mode : 'remote',
			typeAhead : true,
			forceSelection : true,
			hiddenName : 'empinfo.technologyGradeId',
			editable : false,
			triggerAction : 'all',
			width : wd,
			blankText : '请选择',
			emptyText : '请选择',
			selectOnFocus : true
		};

		// 入职时间
		var missionDate = {
			id : 'missionDate',
			xtype : "datefield",
			fieldLabel : '入职时间',
			name : 'empinfo.missionDate',
			format : 'Y-m-d',
			width : wd,
			readOnly : true
				// anchor:'30%'
		};

		// 离职时间
		var dimissionDate = {
			id : 'dimissionDate',
			xtype : "datefield",
			fieldLabel : '离职时间',
			name : 'empinfo.dimissionDate',
			format : 'Y-m-d',
			width : wd,
			readOnly : true
				// anchor:'30%'
		};
		// 工作时间
		var workDate = {
			id : 'workDate',
			xtype : "datefield",
			fieldLabel : '工作时间',
			name : 'empinfo.workDate',
			format : 'Y-m-d',
			width : wd,
			readOnly : true
				// anchor:'30%'
		};

		// 考勤卡号:
		var timeCardId = {
			id : "timeCardId",
			xtype : "textfield",
			fieldLabel : '考勤卡号',
			width : wd,
			name : 'empinfo.timeCardId'
		};

		// 参保时间
		var socialInsuranceDate = {
			id : 'socialInsuranceDate',
			xtype : "datefield",
			fieldLabel : '参保时间',
			name : 'empinfo.socialInsuranceDate',
			format : 'Y-m-d',
			width : wd,
			readOnly : true
				// anchor:'30%'
		};

		// 第二列

		// 工作岗位
		var empStationstore = new Ext.data.Store({
			proxy : new Ext.data.HttpProxy({
				url : 'empInfoManage.action?method=getstation'
			}),
			reader : new Ext.data.JsonReader({
					// id : "plant"
					}, [{
						name : 'id'
					}, {
						name : 'text'
					}])
		});
		empStationstore.load();
		var stationId = {
			fieldLabel : "工作岗位<font color='red'>*</font>",
			name : 'empinfo.stationId',
			xtype : 'combo',
			editable : true,
			allowBlank : false,
			id : 'stationId',
			store : empStationstore,
			valueField : "id",
			displayField : "text",
			mode : 'local',
			typeAhead : true,
			forceSelection : true,
			hiddenName : 'empinfo.stationId',
			triggerAction : 'all',
			selectOnFocus : true,
			width : wd,
			blankText : '请选择',
			emptyText : '请选择',
			allowBlank : false
		};

		// 工种
		var typeOfWorkstore = new Ext.data.Store({
			proxy : new Ext.data.HttpProxy({
				url : 'empInfoManage.action?method=gettypeofwork'
			}),
			reader : new Ext.data.JsonReader({
					// id : "plant"
					}, [{
						name : 'id'
					}, {
						name : 'text'
					}])
		});
		typeOfWorkstore.load();
		var typeOfWorkId = {
			fieldLabel : '工种',
			name : 'empinfo.typeOfWorkId',
			xtype : 'combo',
			id : 'typeOfWorkId',
			store : typeOfWorkstore,
			valueField : "id",
			displayField : "text",
			mode : 'remote',
			typeAhead : true,
			forceSelection : true,
			hiddenName : 'empinfo.typeOfWorkId',
			editable : false,
			triggerAction : 'all',
			width : wd,
			blankText : '请选择',
			emptyText : '请选择',
			selectOnFocus : true
		};

		// 员工类别
		var empTypstore = new Ext.data.Store({
			proxy : new Ext.data.HttpProxy({
				url : 'empInfoManage.action?method=getemptype'
			}),
			reader : new Ext.data.JsonReader({
					// id : "plant"
					}, [{
						name : 'id'
					}, {
						name : 'text'
					}])
		});
		empTypstore.load();
		var empTypeId = {
			fieldLabel : '员工类别',
			name : 'empinfo.empTypeId',
			xtype : 'combo',
			id : 'empTypeId',
			store : empTypstore,
			valueField : "id",
			displayField : "text",
			mode : 'remote',
			typeAhead : true,
			forceSelection : true,
			hiddenName : 'empinfo.empTypeId',
			editable : false,
			triggerAction : 'all',
			width : wd,
			blankText : '请选择',
			emptyText : '请选择',
			selectOnFocus : true
		};

		// 技术职称
		var technologystore = new Ext.data.Store({
			proxy : new Ext.data.HttpProxy({
				url : 'empInfoManage.action?method=getTechnology'
			}),
			reader : new Ext.data.JsonReader({
					// id : "plant"
					}, [{
						name : 'id'
					}, {
						name : 'text'
					}])
		});
		technologystore.load();
		var technologyTitlesTypeId = {
			fieldLabel : '技术职称',
			name : 'empinfo.technologyTitlesTypeId',
			xtype : 'combo',
			id : 'technologyTitlesTypeId',
			store : technologystore,
			valueField : "id",
			displayField : "text",
			mode : 'remote',
			typeAhead : true,
			forceSelection : true,
			hiddenName : 'empinfo.technologyTitlesTypeId',
			editable : false,
			triggerAction : 'all',
			width : wd,
			blankText : '请选择',
			emptyText : '请选择',
			selectOnFocus : true
		};
		// 传真:
		var fax = {
			id : "fax",
			xtype : "textfield",
			fieldLabel : '传真',
			width : wd,
			name : 'empinfo.fax'
		};

		// 邮政编码:
		var postalcode = {
			id : "postalcode",
			xtype : "textfield",
			fieldLabel : '邮政编码',
			width : wd,
			name : 'empinfo.postalcode'
		};

		// 家庭电话:
		var familyTel = {
			id : "familyTel",
			xtype : "textfield",
			fieldLabel : '家庭电话',
			width : wd,
			name : 'empinfo.familyTel'
		};

		// 社保卡号:
		var socialInsuranceId = {
			id : "socialInsuranceId",
			xtype : "textfield",
			fieldLabel : '社保卡号',
			width : wd,
			name : 'empinfo.socialInsuranceId'
		};

		// 工资卡号:
		var payCardId = {
			id : "payCardId",
			xtype : "textfield",
			fieldLabel : '工资卡号',
			width : wd,
			name : 'empinfo.payCardId'
		};

		// 联系人:
		var instancyMan = {
			id : "instancyMan",
			xtype : "textfield",
			fieldLabel : '联系人',
			width : wd,
			name : 'empinfo.instancyMan'
		};

		// 第一列

		// 毕业院校:
		var graduateSchool = {
			id : "graduateSchool",
			xtype : "textfield",
			fieldLabel : '毕业院校',
			width : wd,
			name : 'empinfo.graduateSchool'
		};

		// 毕业时间
		var graduateDate = {
			id : 'graduateDate',
			xtype : "datefield",
			fieldLabel : '毕业时间',
			name : 'empinfo.graduateDate',
			format : 'Y-m-d',
			width : wd,
			readOnly : true
				// anchor:'30%'
		};

		// 学位
		var degreestore = new Ext.data.Store({
			proxy : new Ext.data.HttpProxy({
				url : 'empInfoManage.action?method=getdegree'
			}),
			reader : new Ext.data.JsonReader({
					// id : "plant"
					}, [{
						name : 'id'
					}, {
						name : 'text'
					}])
		});
		degreestore.load();
		var degreeId = {
			fieldLabel : '学位',
			name : 'empinfo.degreeId',
			xtype : 'combo',
			id : 'degreeId',
			store : degreestore,
			valueField : "id",
			displayField : "text",
			mode : 'remote',
			typeAhead : true,
			forceSelection : true,
			hiddenName : 'empinfo.degreeId',
			editable : false,
			triggerAction : 'all',
			width : wd,
			blankText : '请选择',
			emptyText : '请选择',
			selectOnFocus : true

		};

		// 员工状态
		var empState = {
			id : 'empState',
			layout : 'column',
			isFormField : true,
			fieldLabel : '员工状态',
			border : false,
			items : [{
				columnWidth : .3,
				border : false,
				items : new Ext.form.Radio({
					id : 'empState1',
					boxLabel : '在册',

					name : 'empinfo.empState',
					inputValue : 'U',
					checked : true
				})
			}, {

				columnWidth : .3,
				border : false,

				items : new Ext.form.Radio({
					id : 'empState2',
					boxLabel : '离职',
					name : 'empinfo.empState',
					inputValue : 'N'
				})
			}, {

				columnWidth : .3,
				border : false,

				items : new Ext.form.Radio({
					id : 'empState3',
					boxLabel : '注销',
					name : 'empinfo.empState',
					inputValue : 'L'
				})
			}]
		};
		// ------

		// 退转军人
		var isVeteran = {
			id : 'isVeteran',
			layout : 'column',
			isFormField : true,
			fieldLabel : '退转军人',
			border : false,
			items : [{
				columnWidth : .3,
				border : false,
				items : new Ext.form.Radio({
					id : 'isVeteran1',
					boxLabel : '是',

					name : 'empinfo.isVeteran',
					inputValue : '1'

				})
			}, {

				columnWidth : .3,
				border : false,

				items : new Ext.form.Radio({
					id : 'isVeteran2',
					boxLabel : '否',
					name : 'empinfo.isVeteran',
					inputValue : '0',
					checked : true
				})
			}]
		};

		// QQ:
		var qq = {
			id : "qq",
			xtype : "textfield",
			fieldLabel : 'QQ',
			width : wd,
			name : 'empinfo.qq'
		};
		// 显示顺序:
		var orderBy = {
			id : "orderBy",
			xtype : "numberfield",
			fieldLabel : '显示顺序',
			width : wd,
			name : 'empinfo.orderBy'
		};

		// 特长:
		var oneStrongSuit = {
			id : "oneStrongSuit",
			xtype : "textarea",
			fieldLabel : '特长',
			width : wd,
			name : 'empinfo.oneStrongSuit'
		};

		// 创建者:
		var createBy = {
			id : "createBy",
			xtype : "numberfield",
			fieldLabel : '创建者',
			width : wd,
			name : 'empinfo.createBy',
			readOnly : true,
			hidden:true
		};

		// 修改者:
		var lastModifiyBy = {
			id : "lastModifiyBy",
			xtype : "numberfield",
			fieldLabel : '修改者',
			width : wd,
			name : 'empinfo.lastModifiyBy',
			readOnly : true,
			hidden:true
		};

		// 第二列

		// 学历
		var educationstore = new Ext.data.Store({
			proxy : new Ext.data.HttpProxy({
				url : 'empInfoManage.action?method=geteducation'
			}),
			reader : new Ext.data.JsonReader({
					// id : "plant"
					}, [{
						name : 'id'
					}, {
						name : 'text'
					}])
		});
		educationstore.load();

		var educationId = {
			fieldLabel : '学历',
			name : 'empinfo.educationId',
			xtype : 'combo',
			id : 'educationId',
			store : educationstore,
			valueField : "id",
			displayField : "text",
			mode : 'remote',
			typeAhead : true,
			forceSelection : true,
			hiddenName : 'empinfo.educationId',
			editable : false,
			triggerAction : 'all',
			width : wd,
			blankText : '请选择',
			emptyText : '请选择',
			selectOnFocus : true

		};

		// 专业:
		var speciality = {
			id : "speciality",
			xtype : "textfield",
			fieldLabel : '专业',
			width : wd,
			name : 'empinfo.speciality'
		};

		// 协理单位
		var Unitstore = new Ext.data.Store({
			proxy : new Ext.data.HttpProxy({
				url : 'empInfoManage.action?method=getunits'
			}),
			reader : new Ext.data.JsonReader({
					// id : "plant"
					}, [{
						name : 'id'
					}, {
						name : 'text'
					}])
		});
		Unitstore.load();
		var assistantManagerUnitsId = {
			fieldLabel : '协理单位',
			name : 'empinfo.assistantManagerUnitsId',
			xtype : 'combo',
			id : 'assistantManagerUnitsId',
			store : Unitstore,
			valueField : "id",
			displayField : "text",
			mode : 'remote',
			typeAhead : true,
			forceSelection : true,
			hiddenName : 'empinfo.assistantManagerUnitsId',
			editable : false,
			triggerAction : 'all',
			width : wd,
			blankText : '请选择',
			emptyText : '请选择',
			selectOnFocus : true
		};

		// 是否离退休

		var isRetired = {
			id : 'isRetired',
			layout : 'column',
			isFormField : true,
			fieldLabel : '是否离退休',
			border : false,
			items : [{
				columnWidth : .3,
				border : false,
				items : new Ext.form.Radio({
					id : 'isRetired1',
					boxLabel : '是',

					name : 'empinfo.isRetired',
					inputValue : '1'

				})
			}, {

				columnWidth : .3,
				border : false,

				items : new Ext.form.Radio({
					id : 'isRetired2',
					boxLabel : '否',
					name : 'empinfo.isRetired',
					inputValue : '0',
					checked : true
				})
			}]
		};

		// 是否外借
		var isBorrow = {
			id : 'isBorrow',
			layout : 'column',
			isFormField : true,
			fieldLabel : '是否外借',
			border : false,
			items : [{
				columnWidth : .3,
				border : false,
				items : new Ext.form.Radio({
					id : 'isBorrow1',
					boxLabel : '是',

					name : 'empinfo.isBorrow',
					inputValue : '1'

				})
			}, {

				columnWidth : .3,
				border : false,

				items : new Ext.form.Radio({
					id : 'isBorrow2',
					boxLabel : '否',
					name : 'empinfo.isBorrow',
					inputValue : '0',
					checked : true
				})
			}]
		};

		// MSN:
		var msn = {
			id : "msn",
			xtype : "textfield",
			fieldLabel : 'MSN',
			width : wd,
			name : 'empinfo.msn'
		};
		// 推荐人:
		var recommendMan = {
			id : "recommendMan",
			xtype : "textfield",
			fieldLabel : '推荐人',
			width : wd,
			name : 'empinfo.recommendMan'
		};

		// 员工描述:
		var memo = {
			id : "memo",
			xtype : "textarea",
			fieldLabel : '员工描述',
			width : wd,
			name : 'empinfo.memo'
		};

	
		
		// 创建时间
		var createDate = {
			id : 'createDate',
			xtype : "datefield",
			fieldLabel : '创建时间',
			name : 'empinfo.createDate',
			format : 'Y-m-d',
			width : wd,
			readOnly : true,
			hidden:true
				// anchor:'30%'
		};

		// 修改时间
		var lastModifiyDate = {
			id : 'lastModifiyDate',
			xtype : "datefield",
			fieldLabel : '修改时间',
			name : 'empinfo.lastModifiyDate',
			format : 'Y-m-d',
			width : wd,
			readOnly : true,
			hidden:true
				// anchor:'30%'
		};

		
		var panel4 = new Ext.form.FormPanel({
			id :'tab4',
			frame:true,
			title:'员工履历和社会关系',
			labelWidth:60, 
			items:[
			{
			id:'curriculumVitae',
			name :'empinfo.curriculumVitae',
			isFormField:true,
			xtype:'textarea',
			fieldLabel : '员工履历',
			width:400,
			height:150
			
		},{ 
			id:'societyInfo',
			name:'empinfo.societyInfo',
			isFormField:true,
			xtype:'textarea',
			fieldLabel:'社会关系',
			width:400,
			height:150 
		}
			//curriculumVitae,societyInfo
			
			]
		});
		
		var panel1 = new Ext.FormPanel({
			// labelAlign: 'top',
			id : 'tab1',
			frame : true,
			title : '员工基本信息',
			// url:'./member_manage.jsp?command=insert',
			labelWidth : 60,
			items : [{
				layout : 'column',// 该FormPanel的layout布局模式为列模式(column),包含2列
				items : [{// 第一列
					columnWidth : 0.35,
					layout : 'form',
					items : [empId, chsName, enName, brithday, nationId,
							politicsId, sex, mobilePhone, officeTel1,
							officeTel2]
				}, {	// 第二列
					columnWidth : 0.35,
					layout : 'form',
					items : [empCode, archivesId, retrieveCode, nativePlaceId,
							identityCard, isWedded, instancyTel, familyAddress,
							EMail]
				}, {
					columnWidth : 0.3,
					rowspan : 9,
					layout : 'form',
					items : [photo, imagephoto]
				}]

			}]
		});

		var panel2 = new Ext.FormPanel({

			id : 'tab2',
			frame : true,
			title : '员工企业信息',
			items : [{
				layout : 'column',// 该FormPanel的layout布局模式为列模式(column),包含2列
				items : [{// 第一列
					columnWidth : 0.5,
					layout : 'form',
					items : [deptId, stationLevel, gradation, empStationId,
							technologyGradeId, missionDate, dimissionDate,
							workDate, timeCardId, socialInsuranceDate]
				}, {	// 第二列
					columnWidth : 0.5,
					layout : 'form',
					items : [stationId, typeOfWorkId, empTypeId,
							technologyTitlesTypeId, fax, postalcode, familyTel,
							socialInsuranceId, payCardId, instancyMan]
				}]
			}]
		});

		var panel3 = new Ext.FormPanel({

			id : 'tab3',
			frame : true,
			title : '员工简历信息',
			items : [{
				layout : 'column',// 该FormPanel的layout布局模式为列模式(column),包含2列
				items : [{// 第一列
					columnWidth : 0.5,
					layout : 'form',
					items : [graduateSchool, graduateDate, degreeId, empState,
							isVeteran, qq, orderBy, oneStrongSuit]
				}, {	// 第二列
					columnWidth : 0.5,
					layout : 'form',
					items : [educationId, speciality, assistantManagerUnitsId,
							isRetired, isBorrow, msn, recommendMan, memo]
				}]
			}]
		});

		var tabpanel = new Ext.TabPanel({
			title : 'mytab',
			activeTab : 0,
			autoScroll : true,
			layoutOnTabChange : true,
			// deferredRender:false,
			width : 750,
			height : 370,
			tabPosition:'bottom',
			items : [panel1, panel2, panel3,panel4]

		});

		
		
		// return tabpanel;
		// tabpanel.getActiveTab(0);
		// --------图片显示----------------------------
		tabpanel.getItem('tab1').on('render', function(f) {
			tabpanel.getItem('tab1').form.findField('photo').on('render',
					function() {
						Ext.get('photo').on('change',
								function(field, newValue, oldValue) {
									var url = Ext.get('photo').dom.value;
									Ext.get('imagephoto').dom.src = url;

								});
					});

		})

		// ---------------------------

		
		
		
		
		
		
		
//---------
	function checkinput() {
		var str = '';

		if (Ext.get("chsName").dom.value == "") {
			str = str + '"中文名"';
		}
		if (Ext.get("empCode").dom.value == "") {
			str = str + '"员工工号"';
		}
		if (Ext.get("identityCard").dom.value == "") {
			str = str + '"身份证"';
		} else {
			if (!(Ext.get("identityCard").dom.value.length == 18 || Ext
					.get("identityCard").dom.value.length == 15)) {
				Ext.Msg.alert("注意", "请输入正确的身份证号");
				return false;
			}
		}

		if (Ext.getCmp('deptId').value == null) {
			str = str + '"所属部门"';
		}

		if (Ext.getCmp("stationId").value == null) {
			str = str + '"工作岗位"';
		}
		if (str != "") {
			str = "请填写" + str + "后再保存";
			Ext.Msg.alert("注意", str);
			return false;
		}
		return true;

	}

	function createaddwin(mypanel, op) {
		var mytxt = "增加成功";
		var buttontext = "保存";
		if (op == "update") {
			mytxt = "修改成功";
			buttontext = "修改";
		}

		var win = new Ext.Window({
			width : 750,
			height : 450,
			buttonAlign : "center",
			items : mypanel,
			modal : true,
			layout : 'fit',
			buttons : [{
				text : buttontext,
				handler : function() {  
					if (checkinput()) { 
						// 保存tab1mypanel.getActiveTab().form.submit
						mypanel.setActiveTab(0);
						mypanel.getItem('tab1').form.submit({
							method : 'POST',
							url : 'empInfoManage.action?file='
									+ Ext.get("photo").dom.value + '&method='
									+ op,
							success : function(form, action) { 
								var o = eval("(" + action.response.responseText + ")");
								var id = o.id;  
								if (id == '0') {
									Ext.Msg.alert("注意", "此工号已存在，请重新输入");
								} else {
									// 保存tab2
									mypanel.setActiveTab(1);
									mypanel.getItem('tab2').form.submit({
										method : 'POST',
										url : 'empInfoManage.action?id=' + id
												+ '&method=tab2' + op,
										success : function(form, action) { 
											mypanel.setActiveTab(2);
											// 保存tab3
											mypanel.getItem('tab3').form
													.submit({
														method : 'POST',
														url : 'empInfoManage.action?id='
																+ id
																+ '&method=tab3'
																+ op,
														success : function(
																form, action) {
															mypanel.setActiveTab(3);
															mypanel.getItem('tab4').form.submit({
																method:'POST',
																url:'empInfoManage.action?id='
																	+ id
																	+ '&method=tab4'
																	+ op,
																params:{
																	'empinfo.curriculumVitae':Ext.get('empinfo.curriculumVitae').dom.value,
																	'empinfo.societyInfo':Ext.get('empinfo.societyInfo').dom.value
																},
																success:function(form,action)
																{
																	var o = eval("("
																	+ action.response.responseText
																	+ ")");
															if (o.msg == "操作成功") {
																Ext.Msg.alert(
																		"注意",
																		mytxt);
																}
																}
															}); 
															win.close();
															grid.store.reload();
														},
														faliue : function() {
															Ext.Msg
																	.alert(
																			'错误',
																			'tab3出现错误.');
														}

													});

										},
										faliue : function() {
											Ext.Msg.alert('错误', 'tab2出现错误.');
										}

									});

								} 
							},
							faliue : function() {
								Ext.Msg.alert('错误', '出现未知错误.');
							}
						});
					}
				}
			}, {
				text : '取消',
				handler : function() {
					win.close();
				}
			}]

		});
		return win;

	}
	// 增加
	function addRecord() {

		if (!myaddpanel) {
			var myaddpanel = createupdatepanel("增加", '自动生成');

		}
		if (!win) {
			var win = createaddwin(myaddpanel, "add");
		}

		win.show();

		// myaddpanel.form.loadRecord("id:自动生成");

	};

	// 修改
	function updateRecord() {
		if (grid.selModel.hasSelection()) { 
			var records = grid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert("系统提示信息", "请选择其中一项进行编辑！");
			} else {
				var record = grid.getSelectionModel().getSelected(); 
				if (!myaddpanel) {
					var myaddpanel = createupdatepanel("修改", record
							.get("empId")); 
				}
				if (!win) {
					var win = createaddwin(myaddpanel, "update");
				} 
				win.show();  
				/////////////////////wzhyan modify 2009-01-08 数据只加载一次/////////////////////
				var url = "empInfoManage.action?method=getdata&id="+record.get("empId");
			 
				var conn = Ext.lib.Ajax.getConnectionObject().conn;
				conn.open("POST", url, false);
				conn.send(null);
				// 成功状态码为200
				if (conn.status == "200") {
					var o = eval('(' + conn.responseText + ')');
					// 加载tab1数据
					myaddpanel.getItem('tab1').form.loadRecord(o);
					if (o.data.sex == "M") {
						Ext.getCmp('sex1').setValue(true);
					}
					if (o.data.sex == "W") {
						Ext.getCmp('sex2').setValue(true);
					}
					if (o.data.isWedded == "0") {
						Ext.getCmp('isWedded1').setValue(true);
					}
					if (o.data.isWedded == "1") {
						Ext.getCmp('isWedded2').setValue(true);
					}
					var cd = new Date();
					Ext.get('imagephoto').dom.src = "empInfoManage.action?method=test&id="+ record.get("empId") + "&time=" + cd.getTime();
//					Ext.get('imagephoto').dom.onerror = function() { 
//						this.src = '/power/comm/images/UnknowBody.jpg';
//					}
					// 加载tab2
					myaddpanel.setActiveTab(1);
					myaddpanel.getItem('tab2').form.loadRecord(o);
					Ext.getCmp('deptId').setValue(o.data.deptId);
					Ext.form.ComboBox.superclass.setValue.call(Ext
							.getCmp('deptId'), o.deptname);
					// 加载tab3
					myaddpanel.setActiveTab(2);
					myaddpanel.getItem('tab3').form.loadRecord(o);
					if (o.data.empState == "U") {
						Ext.getCmp('empState1').setValue(true);
					}
					if (o.data.empState == "N") {
						Ext.getCmp('empState2').setValue(true);
					}
					if (o.data.empState == "L") {
						Ext.getCmp('empState3').setValue(true);
					}
					if (o.data.isVeteran == "1") {
						Ext.getCmp('isVeteran1').setValue(true);
					}
					if (o.data.isVeteran == "0") {
						Ext.getCmp('isVeteran2').setValue(true);
					}
					if (o.data.isRetired == "1") {
						Ext.getCmp('isRetired1').setValue(true);
					}
					if (o.data.isRetired == "0") {
						Ext.getCmp('isRetired2').setValue(true);
					}
					if (o.data.isBorrow == "1") {
						Ext.getCmp('isBorrow1').setValue(true);
					}
					if (o.data.isBorrow == "0") {
						Ext.getCmp('isBorrow2').setValue(true);
					}
					// 加载tab4
					myaddpanel.setActiveTab(3);
					myaddpanel.getItem('tab4').form.loadRecord(o);
					myaddpanel.setActiveTab(0);
				}
				
			}
		} else {
			Ext.Msg.alert("提示", "请先选择要编辑的行!");
		}
	}
	var viewport = Ext.Viewport({
			layout:'border',
			border:false,
			items:[tabpanel]
		});
		alert();
	// -----删除----------
});