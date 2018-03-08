// 合同续签管理
// author : sufeiyu
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.form.TextField.prototype.width = 180;

Ext.onReady(function() {
	// 声明变量。
	var today = "";
	// 劳动合同id
	var contractId = '';
	// 附件来源
	var fileOriger = '';
	// 选中员工ID
	var strEmpId = "";
	// 是否续签
	var isContinue = "N";
	// 控件类型: 输入
	var TYPE_INPUT = "I";
	// 控件类型: 选择
	var TYPE_SELECT = "S";
	// 判断是否点过了“合同续签”按钮，如是，则再点没用
	var ifClick = false;

	Ext.QuickTips.init();
	dateInit();

	// 合同续签
	var btnContinue = new Ext.Button({
		text : "合同续签",
		iconCls : Constants.CLS_CONTRACT_CONTINUE,
		handler : contractC
	})

	// 确认
	var btnConfirm = new Ext.Button({
		text : Constants.BTN_CONFIRM,
		iconCls : Constants.CLS_OK,
		handler : confirm
	})

	// 取消
	var btnCancel = new Ext.Button({
		text : Constants.BTN_CANCEL,
		iconCls : Constants.CLS_CANCEL,
		handler : cancel
	})

	// 按钮toolbar
	var tbrButton = new Ext.Toolbar({
		anchor : '100%',
		items : [btnContinue, btnConfirm, btnCancel]
	});

	//************************左侧树形结构**************************//
	// 定义树结构
	var _deptId;
	var Tree = Ext.tree;
	var treeLoader = new Tree.TreeLoader({
		dataUrl : 'hr/getDeptEmpTreeList.action'
	})
	// 部门人员tree
	var treeEmployee = new Tree.TreePanel({
		animate : true,
		allowDomMove : false,
		autoScroll : true,
		containerScroll : true,
		collapsible : true,
		split : true,
		// frame : false,
		border : false,
		rootVisible : false,
		root : root,
		animate : true,
		enableDD : false,
		border : false,
		containerScroll : true,
		loader : treeLoader
	});
	// 定义根节点
	var root = new Tree.AsyncTreeNode({
		text : '皖能合肥电厂',
		isRoot : true,
		id : '0'

	});
	treeEmployee.on("click", treeClick);
	treeEmployee.setRootNode(root);
	// root.expand();
	root.select();

	//************************左侧树形结构**************************//

	//************************右侧上方Form*************************//
	var contractRecord = new Ext.data.Record.create([{
		name : 'workcontractid'
	}, {
		name : 'empId'
	}, {
		name : 'empName'
	}, {
		name : 'deptId'
	}, {
		name : 'deptName'
	}, {
		name : 'stationId'
	}, {
		name : 'stationName'
	}, {
		name : 'wrokContractNo'
	}, {
		name : 'fristDepId'
	}, {
		name : 'fristDepName'
	}, {
		name : 'fristAddrest'
	}, {
		name : 'contractTermId'
	}, {
		name : 'workSignDate'
	}, {
		name : 'startDate'
	}, {
		name : 'endDate'
	}, {
		name : 'ifExecute'
	}, {
		name : 'contractContinueMark'
	}, {
		name : 'memo'
	}, {
		name : 'insertby'
	}, {
		name : 'insertdate'
	}, {
		name : 'enterpriseCode'
	}, {
		name : 'isUse'
	}, {
		name : 'lastModifiedBy'
	}, {
		name : 'lastModifiedDate'
	}])
	// 所有合同信息和人员信息的json阅读器
	var contractInfoReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	}, contractRecord)
	var contractInfoStore = new Ext.data.Store({
		reader : contractInfoReader
	});

	// 合同编号
	var txtContractNo = new Ext.form.TextField({
		fieldLabel : "合同编码",
		width : 120,
		allowBlank : false,
		anchor : '100%',
		disabled : true,
		id : 'wrokContractNo',
		maxLength : 6
	});

	// 合同ID
	var hidContractId = new Ext.form.Hidden({
		id : 'workcontractid'
	});

	// 上次修改时间
	var hidLastModifiedDate = new Ext.form.Hidden({
		id : 'lastModifiedDate'
	});

	// 定义合同有效期的store
	var storeTerm = new Ext.data.JsonStore({
		root : 'list',
		url : "hr/getContractTerm.action",
		fields : ['contractTermId', 'contractTerm']
	})
	storeTerm.load({
		params : {
			start : -1,
			limit : -1
		},
		callback : function() {
			storeTerm.insert(0, new Ext.data.Record({
				contractTerm : '',
				contractTermId : ''
			}));
		}
	});

	// 有效期
	var cmbTerm = new Ext.form.ComboBox({
		fieldLabel : '有效期',
		anchor : '100%',
		allowBlank : false,
		id : 'contractTermId',
		disabled : true,
		triggerAction : 'all',
		store : storeTerm,
		mode : 'local',
		displayField : 'contractTerm',
		valueField : 'contractTermId'
	});

	// 第一行
	var firstLine = new Ext.form.FieldSet({
		layout : 'column',
		anchor : '100%',
		style : "padding-top:0px;padding-bottom:0px;border:0px;margin:0px;",
		border : false,
		items : [{
			columnWidth : 0.5,
			border : false,
			layout : 'form',
			items : [txtContractNo, hidContractId, hidLastModifiedDate]
		}, {
			columnWidth : 0.5,
			border : false,
			layout : 'form',
			items : [cmbTerm]
		}]
	});

	// 甲方部门
	var cmbDeptA = new Ext.form.TextField({
		fieldLabel : '甲方部门',
		id : 'fristDepName',
		anchor : '100%',
		disabled : true
	});

	// 第二行
	var secondLine = new Ext.form.FieldSet({
		layout : 'column',
		anchor : '100%',
		style : "padding-top:0px;padding-bottom:0px;border:0px;margin:0px;",
		border : false,
		items : [{
			columnWidth : 1,
			border : false,
			layout : 'form',
			items : [cmbDeptA]
		}]
	});

	// 甲方地址
	var txtAddress = new Ext.form.TextField({
		fieldLabel : '甲方地址',
		disabled : true,
		witdh : 240,
		anchor : '100%',
		maxLength : 15,
		id : 'fristAddrest'
	});

	// 第三行
	var thirdLine = new Ext.form.FieldSet({
		layout : 'column',
		anchor : '100%',
		style : "padding-top:0px;padding-bottom:0px;border:0px;margin:0px;",
		border : false,
		items : [{
			columnWidth : 1,
			border : false,
			layout : 'form',
			items : [txtAddress]
		}]
	});

	// 员工姓名
	var txtName = new Ext.form.TextField({
		fieldLabel : "员工姓名",
		disabled : true,
		witdh : 120,
		anchor : '100%',
		id : 'empName'
	});

	// 合同形式
	var txtContactForm = new Ext.form.CmbHRCode({
		id : 'contractContinueMark',
		fieldLabel : "合同形式",
		anchor : '100%',
		triggerClass : 'noButtonCombobox',
		type : '劳动合同形式',
		readOnly : true,
		disabled : true
	});

	// 第四行
	var fourthLine = new Ext.form.FieldSet({
		layout : 'column',
		anchor : '100%',
		border : false,
		style : "padding-top:0px;padding-bottom:0px;border:0px;margin:0px;",
		items : [{
			columnWidth : 0.5,
			border : false,
			layout : 'form',
			items : [txtName]
		}, {
			columnWidth : 0.5,
			border : false,
			layout : 'form',
			items : [txtContactForm]
		}]
	});

	// 所属部门
	var txtDeptOf = new Ext.form.TextField({
		fieldLabel : "所属部门",
		disabled : true,
		anchor : '100%',
		id : 'deptName'
	});

	// 岗位
	var txtStation = new Ext.form.TextField({
		fieldLabel : "岗位",
		disabled : true,
		anchor : '100%',
		id : 'stationName'
	});

	// 第五行
	var fifthLine = new Ext.form.FieldSet({
		layout : 'column',
		anchor : '100%',
		border : false,
		style : "padding-top:0px;padding-bottom:0px;border:0px;margin:0px;",
		items : [{
			columnWidth : 0.5,
			border : false,
			layout : 'form',
			items : [txtDeptOf]
		}, {
			columnWidth : 0.5,
			border : false,
			layout : 'form',
			items : [txtStation]
		}]
	});

	// 签字日期
	var dteSign = new Ext.form.TextField({
		disabled : true,
		fieldLabel : '签字日期',
		width : 240,
		anchor : '50%',
		id : 'workSignDate'
	});

	// 第六行
	var sixthLine = new Ext.form.FieldSet({
		layout : 'column',
		anchor : '100%',
		border : false,
		style : "padding-top:0px;padding-bottom:0px;border:0px;margin:0px;",
		items : [{
			columnWidth : 1,
			border : false,
			layout : 'form',
			items : [dteSign]
		}]
	});

	// 开始日期
	var dteStart = new Ext.form.TextField({
		disabled : true,
		fieldLabel : '开始日期',
		anchor : '100%',
		width : 120,
		id : 'startDate'
	});

	// 结束日期
	var dteEnd = new Ext.form.TextField({
		disabled : true,
		fieldLabel : '结束日期',
		anchor : '100%',
		width : 120,
		id : 'endDate'
	});

	// 第七行
	var seventhLine = new Ext.form.FieldSet({
		layout : 'column',
		anchor : '100%',
		border : false,
		style : "padding-top:0px;padding-bottom:0px;border:0px;margin:0px;",
		items : [{
			columnWidth : 0.5,
			border : false,
			layout : 'form',
			items : [dteStart]
		}, {
			columnWidth : 0.5,
			border : false,
			layout : 'form',
			items : [dteEnd]
		}]
	});

	var formOld = new Ext.form.FormPanel({
		id : 'old',
		labelAlign : 'right',
		labelWidth : 65,
		border : false,
		width : 550,
		style : "padding-top:0px;padding-bottom:0px;border:0px;margin:0px;",
		layout : 'form',
		items : [firstLine, secondLine, thirdLine, fourthLine, fifthLine,
				sixthLine, seventhLine]
	})

	// 右上form
	var formInfoU = new Ext.form.FieldSet({
		border : true,
		title : '合同信息',
		labelAlign : 'right',
		labelWidth : 65,
		frame : false,
		style : "margin:5px;",
		items : [formOld]
	});
	//************************右侧上方Form*************************//

	//************************右侧下方Form*************************//
	// 续签日期
	var dteSignC = new Ext.form.TextField({
		readOnly : true,
		checked : true,
		fieldLabel : '续签日期<font color ="red">*</font>',
		width : 240,
		anchor : '100%',
		id : 'ContinueDate',
		name : 'bean.workSignDate',
		style : 'cursor:pointer',
		listeners : {
			focus : function() {
				WdatePicker({
					// 时间格式
					startDate : '%y-%M-%d',
					dateFmt : 'yyyy-MM-dd',
					alwaysUseStartDate : false,
					isShowClear : false
				});
			}
		}
	});

	// 合同形式
	var txtContactFormC = new Ext.form.CmbHRCode({
		id : 'contractContinueMarkC',
		fieldLabel : "合同形式",
		anchor : '100%',
		triggerClass : 'noButtonCombobox',
		type : '劳动合同形式',
		readOnly : true,
		disabled : true
	});

	// 第一行
	var firstLineD = new Ext.form.FieldSet({
		layout : 'column',
		anchor : '100%',
		border : false,
		style : "padding-top:0px;padding-bottom:0px;border:0px;margin:0px;",
		items : [{
			columnWidth : 0.5,
			border : false,
			layout : 'form',
			items : [dteSignC]
		}, {
			columnWidth : 0.5,
			border : false,
			layout : 'form',
			items : [txtContactFormC]
		}]
	});

	// 开始日期
	var dteStartC = new Ext.form.TextField({
		readOnly : true,
		checked : true,
		fieldLabel : '开始日期<font color ="red">*</font>',
		anchor : '100%',
		width : 120,
		id : 'startDateC',
		name : 'bean.startDate',
		style : 'cursor:pointer',
		listeners : {
			focus : function() {
				WdatePicker({
					// 时间格式
					startDate : '%y-%M-%d',
					dateFmt : 'yyyy-MM-dd',
					alwaysUseStartDate : false,
					isShowClear : false,
					onpicked : function() {//add by drdu 090924
						if (dteEndC.getValue() != "") {
							if (dteStartC.getValue() == ""
									|| dteStartC.getValue() > dteEndC
											.getValue()) {
								Ext.Msg.alert("提示", "必须小于结束日期");
								dteStartC.setValue("");
								return;
							}
							dteStartC.clearInvalid();
						}
					},
					onclearing : function() {
						startDate.markInvalid();
					}
				});
			}
		}
	});

	// 结束日期
	var dteEndC = new Ext.form.TextField({
		readOnly : true,
		checked : true,
		fieldLabel : '结束日期<font color ="red">*</font>',
		anchor : '100%',
		width : 120,
		id : 'endDateC',
		name : 'bean.endDate',
		style : 'cursor:pointer',
		listeners : {
			focus : function() {
				WdatePicker({
					// 时间格式
					startDate : '%y-%M-%d',
					dateFmt : 'yyyy-MM-dd',
					alwaysUseStartDate : false,
					isShowClear : false,
					onpicked : function() {//add by drdu 090924
						if (dteStartC.getValue() == ""
								|| dteStartC.getValue() > dteEndC.getValue()) {
							Ext.Msg.alert("提示", "必须大于开始日期");
							dteEndC.setValue("")
							return;
						}
						dteEndC.clearInvalid();
					},
					onclearing : function() {
						dteEndC.markInvalid();
					}
				});
			}
		}
	});

	// 第二行
	var secondLineD = new Ext.form.FieldSet({
		layout : 'column',
		anchor : '100%',
		border : false,
		style : "padding-top:0px;padding-bottom:0px;border:0px;margin:0px;",
		items : [{
			columnWidth : 0.5,
			border : false,
			layout : 'form',
			items : [dteStartC]
		}, {
			columnWidth : 0.5,
			border : false,
			layout : 'form',
			items : [dteEndC]
		}]
	});

	// 数据源--------------------------------
	var recordWorker = Ext.data.Record.create([{
		name : 'deptIdC'
	}, {
		name : 'deptNameC'
	}, {
		name : 'stationIdC'
	}, {
		name : 'stationNameC'
	}]);

	// 定义获取数据源
	var proxyWorker = new Ext.data.HttpProxy({
		url : 'hr/getDeptAndStation.action'
	});

	// 定义格式化数据
	var readerWorker = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	}, recordWorker);

	// 定义封装缓存数据的对象
	var storeWorker = new Ext.data.Store({
		// 访问的对象
		proxy : proxyWorker,
		// 处理数据的对象
		reader : readerWorker
	});

	// 所属部门
	var txtDeptOfC = new Ext.form.ComboBox({
		fieldLabel : "所属部门",
		store : storeWorker,
		readOnly : true,
		disabled : true,
		anchor : '100%',
		id : 'deptIdC',
		hiddenName : 'bean.deptId',
		displayField : 'deptNameC',
		valueField : 'deptIdC',
		triggerClass : 'noButtonCombobox',
		listeners : {
			'beforequery' : function(obj) {
				obj.cancel = true;
			}
		}
	});

	// 岗位
	var txtStationC = new Ext.form.ComboBox({
		fieldLabel : "岗位",
		disabled : false,
		store : storeWorker,
		anchor : '100%',
		id : 'stationIdC',
		hiddenName : 'bean.stationId',
		displayField : 'stationNameC',
		valueField : 'stationIdC',
		triggerClass : 'noButtonCombobox',
		listeners : {
			'beforequery' : function(obj) {
				obj.cancel = true;
			}
		}
	});

	// 第三行
	var thirdLineD = new Ext.form.FieldSet({
		layout : 'column',
		anchor : '100%',
		border : false,
		style : "padding-top:0px;padding-bottom:0px;border:0px;margin:0px;",
		items : [{
			columnWidth : 0.5,
			border : false,
			layout : 'form',
			items : [txtDeptOfC]
		}, {
			columnWidth : 0.5,
			border : false,
			layout : 'form',
			items : [txtStationC]
		}]
	});

	// 定义合同有效期的store
	var storeTermC = new Ext.data.JsonStore({
		root : 'list',
		url : "hr/getContractTerm.action",
		fields : ['contractTermId', 'contractTerm']
	})
	storeTermC.load({
		params : {
			start : -1,
			limit : -1
		},
		callback : function() {
			storeTermC.insert(0, new Ext.data.Record({
				contractTermC : '',
				contractTermIdC : ''
			}));
		}
	});

	// 有效期
	var cmbTermC = new Ext.form.ComboBox({
		fieldLabel : '有效期<font color ="red">*</font>',
		anchor : '50%',
		allowBlank : false,
		id : 'contractTermIdC',
		hiddenName : 'bean.contractTermId',
		readOnly : true,
		triggerAction : 'all',
		store : storeTerm,
		mode : 'local',
		displayField : 'contractTerm',
		valueField : 'contractTermId'
	});

	// 第四行
	var fourthLineD = new Ext.form.FieldSet({
		layout : 'column',
		anchor : '100%',
		border : false,
		style : "padding-top:0px;padding-bottom:0px;border:0px;margin:0px;",
		items : [{
			columnWidth : 1,
			border : false,
			layout : 'form',
			items : [cmbTermC]
		}]
	});

	// 备注
	var txaMemo = new Ext.form.TextArea({
		fieldLabel : '备注',
		anchor : '99.5%',
		maxLength : 127,
		id : 'memo',
		name : 'bean.memo'
	});

	// 第五行
	var fifthLineD = new Ext.form.FieldSet({
		layout : 'column',
		anchor : '100%',
		border : false,
		style : "padding-top:0px;padding-bottom:0px;border:0px;margin:0px;",
		items : [{
			columnWidth : 1,
			border : false,
			layout : 'form',
			items : [txaMemo]
		}]
	});

	// 附件控件+gird
	// 附件
	var tfAppend = new Ext.form.TextField({
		inputType : 'file',
		name : 'fileUpload',
		disabled : true,
		fieldLabel : "合同附件",
		width : 200
	});

	// 上传附件按钮
	var btnUploadFile = new Ext.Button({
		text : "上传附件",
		disabled : true,
		iconCls : Constants.CLS_UPLOAD,
		align : 'center'
	});
	var uploadform = new Ext.FormPanel({
		layout : 'column',
		frame : false,
		style : "border:0px",
		fileUpload : true,
		items : [tfAppend, btnUploadFile]
	})

	// 附件一览grid
	var cmQuestFile = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
		header : "行号",
		width : 35
	}), {
		header : "文件名",
		sortable : true,
		dataIndex : 'fileName',
		renderer : showFile
	}, {
		header : "删除附件",
		sortable : true,
		width : 65,
		dataIndex : 'fileName',
		renderer : dele
	}]);
	// 定义选择列
	var smQuestFile = new Ext.grid.RowSelectionModel({
		singleSelect : true
	});

	// 定义封装缓存数据的对象
	var storeQuestFile = new Ext.data.JsonStore({
		url : 'hr/getContractAppendFileDatas.action',
		root : 'list',
		totalProperty : 'totalCount',
		fields : [
				// 流水号
				'fileId',
				// 文件名
				'fileName',
				// 更新日时
				'lastModifiyDate']
	});
	var gridQuestFile = new AppendFilePanel({
		// 显示工具栏
		// 是否可编辑
		readOnly : false,
		height : 113,
		width : 540,
		// store
		store : storeQuestFile,
		// 下载方法名字
		downloadFuncName : 'downloadFile',
		// 上传文件url
		uploadUrl : 'hr/uploadContractAppendFile.action',
		// 上传文件时参数
		uploadParams : {
			workcontractid : contractId,
			fileOriger : fileOriger
		},
		// 删除url
		deleteUrl : 'hr/deleteContractAppendFile.action',
		// 工具栏按钮
		showlabel : true
	});
	// load时的参数
	gridQuestFile.on('beforeupload', function() {
		var fileOriger1;
		if (contractInfoStore.getAt(0).data['contractContinueMark'] == '0') {
			fileOriger1 = '2';
		} else {
			fileOriger1 = '2';
		}
		Ext.apply(this.uploadParams, {
			workcontractid : contractInfoStore.getAt(0).data['workcontractid'],
			fileOriger : fileOriger1

		});
	})

	var formContinue = new Ext.form.FormPanel({
		id : 'continue',
		name : 'bean',
		labelAlign : 'right',
		width : 550,
		style : "padding-top:0px;padding-bottom:0px;border:0px;margin:0px;",
		labelWidth : 65,
		border : false,
		items : [firstLineD, secondLineD, thirdLineD, fourthLineD, fifthLineD]
	})

	// 右下form
	var formInfoD = new Ext.form.FieldSet({
		border : true,
		title : '续签信息',
		labelAlign : 'right',
		labelWidth : 65,
		frame : false,
		style : "padding-bottom:10px;margin:5px;",
		items : [formContinue, gridQuestFile]
	});
	//************************右侧下方Form*************************//

	// 左边的panel
	var leftPanel = new Ext.Panel({
		region : 'west',
		layout : 'fit',
		width : '20%',
		border : true,
		frame : false,
		autoScroll : true,
		containerScroll : true,
		collapsible : true,
		split : true,
		items : [treeEmployee]
	});

	// 右边的panel
	var rightPanel = new Ext.Panel({
		region : "center",
		border : true,
		frame : false,
		width : 300,
		height : 543,
		layout : 'form',
		items : [formInfoU, formInfoD]
	});

	// 整个页面的panel
	var allPanel = new Ext.Panel({
		layout : 'border',
		tbar : tbrButton,
		defaults : {
			autoScroll : true
		},
		border : false,
		items : [leftPanel, rightPanel]
	});

	// 显示画面
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		style : "padding-top:0px;padding-bottom:0px;border:0px;margin:0px;",
		border : false,
		items : [allPanel]
	});

	//--------------------------方法定义开始------------------------------------

	init();

	/**
	 * 点击树时
	 */
	function treeClick(node, e) {
		 e.stopEvent();
		if (node.isLeaf()) {
			formContinue.getForm().trim();
			if (isContinue == "Y") {
				if (isChange()) {
					Ext.MessageBox.confirm(Constants.CONFIRM,
							Constants.COM_C_004, function(buttonobj) {
								if (buttonobj == "yes") {
									// 设置为没点过
									ifClick = false;
									findByEmpId(node.id)
								}
							})
				} else {
					// 设置为没点过
					ifClick = false;
					findByEmpId(node.id);
				}
			} else {
				// 设置为没点过
				ifClick = false;
				findByEmpId(node.id);
			}
		}
	}

	/**
	 * 根据员工Id查找合同信息
	 */
	function findByEmpId(empId) {
		strEmpId = empId;
		Ext.Ajax.request({
			url : 'hr/getContractByEId.action',
			method : Constants.POST,
			params : {
				// 员工ID
				strEmpId : empId
			},
			success : function(action) {
				var result = eval("(" + action.responseText + ")");
				if (result.msg != undefined
						&& result.msg == Constants.SQL_FAILURE) {
					// 失败时，弹出提示
					Ext.Msg.alert(Constants.REMIND, Constants.COM_I_014);
				} else {
					contractInfoStore.loadData(result);
					checkIsSign(contractInfoStore);
				}
			}
		});
	}

	/**
	 * 判断是否签合同
	 */
	function checkIsSign(obj) {
		if (obj.getCount() >= 1 && obj.getAt(0).data['workcontractid'] != ""
				&& obj.getAt(0).data['ifExecute'] == 1) {
			return signYes(obj);
		} else {
			return signNo(obj);
		}
	}

	/**
	 * 签合同的操作
	 */
	function signYes(obj) {
		if (obj.getCount() >= 1) {
			btnContinue.setDisabled(false);
			formOld.getForm().loadRecord(obj.getAt(0));
			cmbTerm.setValue(obj.getAt(0).data.contractTermId, true);
			// fangjihu
			formOld.getForm().clearInvalid();
			if (obj.getAt(0).data['workcontractid'] != null
					&& obj.getAt(0).data['workcontractid'] != "") {
				var fileOriger1;
				if (obj.getAt(0).data['contractContinueMark'] == '0') {
					fileOriger1 = '2';
				} else {
					fileOriger1 = '2';
				}
				gridQuestFile.enable();
				gridQuestFile.clearAppendFileValue();
				storeQuestFile.load({
					params : {
						workcontractid : obj.getAt(0).data['workcontractid'],
						fileOriger : fileOriger1
					}

				})
			}
			// 二次续签，则把上次续签的信息输出到续签信息的panel中
			if (1 == obj.getAt(0).data['contractContinueMark']) {
				dteSignC.setValue(obj.getAt(0).data['workSignDate']);
				txtContactFormC
						.setValue(obj.getAt(0).data['contractContinueMark']);
				dteStartC.setValue(obj.getAt(0).data['startDate']);
				dteEndC.setValue(obj.getAt(0).data['endDate']);
				txtDeptOfC.setValue(obj.getAt(0).data['deptName']);
				txtStationC.setValue(obj.getAt(0).data['stationName']);
				cmbTermC.setValue(obj.getAt(0).data['contractTermId']);
				txaMemo.setValue(obj.getAt(0).data['memo']);
			} else {
				// 清空上次续签合同的信息
				dteSignC.setValue("");
				txtContactFormC.setValue("");
				dteStartC.setValue("");
				dteEndC.setValue("");
				txtDeptOfC.setValue("");
				txtStationC.setValue("");
				cmbTermC.setValue("");
				txaMemo.setValue("");
				// 参照（浏览）和上传附件设为不可用
				gridQuestFile.disable();
			}
			setDisabled(formContinue, false);
			btnCancel.setDisabled(true);
			btnConfirm.setDisabled(true);
			isContinue = "N";
			return true;
		}
	}

	/**
	 * 没有签合同的操作
	 */
	function signNo(obj) {
		// formInfo.getForm().reset();
		formOld.getForm().loadRecord(new contractRecord({
			"contractContinueMark" : "",
			"contractTermId" : "",
			"deptId" : "",
			"deptName" : "",
			"empId" : "",
			"empName" : "",
			"endDate" : "",
			"enterpriseCode" : " ",
			"fristAddrest" : "",
			"fristDepId" : "",
			"fristDepName" : "",
			"ifExecute" : "",
			"insertby" : "",
			"insertdate" : "",
			"isUse" : "",
			"lastModifiedBy" : "",
			"lastModifiedDate" : "",
			"memo" : "",
			"startDate" : "",
			"stationId" : "",
			"stationName" : "",
			"workSignDate" : "",
			"workcontractid" : "",
			"wrokContractNo" : ""
		}));
		gridQuestFile.disable();
		gridQuestFile.clearAppendFileValue();
		btnContinue.setDisabled(true);
		storeQuestFile.removeAll();
		formContinue.getForm().reset();
		setDisabled(formContinue, false);
		btnCancel.setDisabled(true);
		btnConfirm.setDisabled(true);
		isContinue = "N";
		return false;

	}

	/**
	 * 附件名格式
	 */
	function showFile(value, cellmeta, record) {
		if (value != "") {
			var fileId = record.get("fileId");
			var download = 'downloadFile("' + fileId + '");return false;';
			return "<a href='#' onclick='" + download + "'>" + value + "</a>";
		} else {
			return "";
		}
	}

	/**
	 * 下载文件
	 */
	downloadFile = function(id) {
		document.all.blankFrame.src = "hr/downloadContractAppendFile.action?fileId="
				+ id;
	}

	/**
	 * 设置Form是否可以编辑
	 */
	function setDisabled(form, argFlag) {
		form.getForm().clearInvalid();
		form.getForm().items.each(function(f) {
			var xtype = f.getXType();
			if (f.el.dom
					&& (xtype == 'textfield' || xtype == 'numfield'
							|| xtype == 'textarea' || xtype == 'radio'
							|| xtype == 'combo' || xtype == 'combotree' || xtype == 'CmbHRCode')) {
				f.setDisabled(!argFlag);
			}
		});
	}

	/**
	 * 删除文件的格式化
	 */
	function dele(value) {
		// 文件删除函数
		if (value) {
			return "<img src='comm/ext/tool/dialog_close_btn.gif'>";
		}
		return "<a href='#'  onclick= 'deleteAppendHandler();return false;'>"
				+ "<img src='comm/ext/tool/dialog_close_btn.gif'></a>";
	}

	/**
	 * 主画面不为空判断
	 */
	function checkNullMain() {
		var msg = ""
		if (txtContractNo.getValue() == "") {
			msg += String.format(Constants.COM_E_002, '合同编号') + "<br />";
		}
		if (cmbTerm.getValue() == "") {
			msg += String.format(Constants.COM_E_003, '有效期') + "<br />";
		}
		if (cmbDeptA.getValue() == "") {
			msg += String.format(Constants.COM_E_003, '甲方部门') + "<br />";
		}
		if (dteSign.getValue() == "") {
			msg += String.format(Constants.COM_E_003, '签字日期') + "<br />";
		}
		if (dteStart.getValue() == "") {
			msg += String.format(Constants.COM_E_003, '开始日期') + "<br />";
		}
		if (dteEnd.getValue() == "") {
			msg += String.format(Constants.COM_E_003, '结束日期');
		}
		if (msg != "") {
			Ext.MessageBox.alert(Constants.ERROR, msg);
			return false;
		}
		return true;
	}

	/**
	 * 开始时间与结束时间比较
	 */
	function checkTime(from, to) {
		var startDate = from.getValue();
		var endDate = to.getValue();
		if (startDate != "" && endDate != "") {
			var res = compareDateStr(startDate, endDate);
			if (res) {
				Ext.Msg.alert(Constants.ERROR, String.format(
						Constants.COM_E_005, "结束日期", "开始日期"));
				return false;
			}
		}
		return true;
	}
	/**
	 * textField显示时间比较方法
	 */
	function compareDate(argDate1, argDate2) {
		return argDate1.getTime() > argDate2.getTime();
	}
	/**
	 * textField显示时间比较方法
	 */
	function compareDateStr(argDateStr1, argDateStr2) {
		var date1 = Date.parseDate(argDateStr1, 'Y-m-d');
		var date2 = Date.parseDate(argDateStr2, 'Y-m-d');

		return compareDate(date1, date2);
	}

	/**
	 * 合同续签按钮操作
	 */
	function contractC() {
		if (!ifClick) {
			ifClick = true;
			gridQuestFile.clearAppendFileValue();
			btnConfirm.setDisabled(false);
			btnCancel.setDisabled(false);
			setDisabled(formContinue, true);
			formContinue.getForm().reset();
			dteSignC.setValue(today);
			txtContactFormC.disable();
			txtContactFormC.setValue("1");
			storeWorker.baseParams = {
				strEmpId : strEmpId
			}
			storeWorker.load();
			storeWorker.on('load', function() {
				if (storeWorker.getAt(0) != null) {
					formContinue.getForm().loadRecord(storeWorker.getAt(0));
				}
			})
			txtDeptOfC.setDisabled(true);
			txtStationC.setDisabled(true);
			storeQuestFile.removeAll();
			gridQuestFile.disable();
			isContinue = "Y";
		}
	}

	/**
	 * 确认按钮操作
	 */
	function confirm() {
		// 必须输入项check
		if (checkForm()) {
			Ext.Msg.confirm(Constants.BTN_CONFIRM, Constants.COM_C_001,
					function(buttonobj) {
						if (buttonobj == 'yes') {
							if (txaMemo.isValid()) {
								if (checkTime(dteStartC, dteEndC)) {
									// 设置为没点过
									ifClick = false;
									// 提交表单
									formContinue.getForm().submit({
										params : {
											// 上次修改时间
											strLastModifiedDate : Ext
													.get('lastModifiedDate').dom.value,
											// 修改记录
											strWorkcontractid : Ext
													.get('workcontractid').dom.value,
											// 所属部门（因为控件为disabled，所以无法submit，只好手动传值）
											strDeptId : txtDeptOfC.getValue(),
											// 所属岗位
											strStationId : txtStationC
													.getValue()
										},
										method : Constants.POST,
										url : 'hr/continueConstract.action',
										success : function(form, action) {
											var o = eval("("
													+ action.response.responseText
													+ ")");
											// 排他
											if (o.msg == Constants.DATA_USING) {
												Ext.Msg.alert(Constants.ERROR,
														Constants.COM_E_015);
												return;
											}
											// Sql错误
											if (o.msg == Constants.SQL_FAILURE) {
												Ext.Msg.alert(Constants.ERROR,
														Constants.COM_E_014);
												return;
											}
											// IO错误
											if (o.msg == Constants.IO_FAILURE) {
												Ext.Msg.alert(Constants.ERROR,
														Constants.COM_E_022);
												return;
											}
											// 数据格式化错误
											if (o.msg == Constants.DATE_FAILURE) {
												Ext.Msg.alert(Constants.ERROR,
														Constants.COM_E_023);
												return;
											} else {
												// 成功
												Ext.Msg.alert(Constants.REMIND,
														Constants.COM_I_004);
												findByEmpId(strEmpId);
											}
										},
										failure : function() {
										}
									})
								}
							}
						}
					})
		}
	}

	/**
	 * 检查form
	 */
	function checkForm() {
		var msg = "";
		msg += checkInput(dteSignC, "续签日期", TYPE_SELECT);
		msg += checkInput(dteStartC, "开始日期", TYPE_SELECT);
		msg += checkInput(dteEndC, "结束日期", TYPE_SELECT);
		msg += checkInput(cmbTermC, "有效期", TYPE_SELECT);
		if (msg != "") {
			// 把最后一个换行去掉
			msg = msg.substring(0, msg.length - 5);
			Ext.Msg.alert(Constants.ERROR, msg);
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
			if (type == TYPE_INPUT) {
				message += String.format(Constants.COM_E_002, name);
			} else if (type == TYPE_SELECT) {
				message += String.format(Constants.COM_E_003, name);
			}
			message += "</BR>";
		}
		return message;
	}

	/**
	 * 取消按钮操作
	 */
	function cancel() {
		Ext.MessageBox.confirm(Constants.CONFIRM, Constants.COM_C_005,
				function(buttonobj) {
					if ("yes" == buttonobj) {
						formContinue.getForm().reset();
						dteSignC.setValue(today);
						txtContactFormC.disable();
						txtContactFormC.setValue("1");
						storeWorker.baseParams = {
							strEmpId : strEmpId
						}
						storeWorker.load();
						storeWorker.on('load', function() {
							if (storeWorker.getAt(0) != null) {
								formContinue.getForm().loadRecord(storeWorker
										.getAt(0));
							}
						})
						txtDeptOfC.setDisabled(true);
						txtStationC.setDisabled(true);
						gridQuestFile.disable();
					}
				})
	}

	/**
	 * 初期处理
	 */
	function init() {
		btnContinue.setDisabled(true);
		btnConfirm.setDisabled(true);
		btnCancel.setDisabled(true);
		setDisabled(formContinue, false);
		gridQuestFile.disable();
		gridQuestFile.clearAppendFileValue();
	}

	/**
	 * 日期初值
	 */
	function dateInit() {
		today = new Date();
		today = dateFormat(today);
	}

	function dateFormat(value) {
		var year;
		var month;
		var day;
		day = value.getDate();
		if (day < 10) {
			day = '0' + day;
		}
		month = value.getMonth() + 1;
		if (month < 10) {
			month = '0' + month;
		}
		year = value.getYear();
		value = year + "-" + month + "-" + day;
		return value;
	}

	/**
	 * 是否修改
	 */
	function isChange() {
		var flag = false;

		if (today != dteSignC.getValue()) {
			return true;
		}

		if ("" != dteStartC.getValue()) {
			return true;
		}

		if ("" != dteEndC.getValue()) {
			return true;
		}

		if ("" != cmbTermC.getValue()) {
			return true;
		}

		if ("" != txaMemo.getValue()) {
			return true;
		}

		return flag;
	}
});
