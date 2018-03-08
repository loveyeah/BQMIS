Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {
	
	// 设置控件宽度
	Ext.form.TextField.prototype.width = 180;
	// 设置控件不可用
	Ext.form.TextField.prototype.disabled = true;
	
	// 字符串: 新签合同
	var STRING_CONTRACT_TYPE_NEW = "新签合同";
	// 字符串: 续签合同
	var STRING_CONTRACT_TYPE_CONTINUE = "续签合同";
	// 劳动合同形式: 新签合同
	var CONTRACT_TYPE_NEW = "0";
	// 劳动合同形式: 续签合同
	var CONTRACT_TYPE_CONTINUE = "1";
	// 控件类型: 输入
	var TYPE_INPUT = "I";
	// 控件类型: 选择
	var TYPE_SELECT = "S";
	
    // 劳动合同id
    var contractId = '';
    // 附件来源
    var fileOriger = '';
    // 人员姓名
    var empName = '';
    // 人员ID
    var empId = '';
    // 当前日期
    var today = '';
    // 是否变更
    var isChange = false;
    var clickCount = 0;
    
    // 初始化日期
    dateInit();
    
    // 合同变更按钮
    var contractChangeBtn = new Ext.Button({
    	text : Constants.BTN_CONTRACT_CHANGE,
    	iconCls : Constants.CLS_CONTRACT_CHANGE,
    	disabled : true,
    	handler : contractChangeHandler
    });
    
    // 确认按钮
    var confirmBtn = new Ext.Button({
    	text : Constants.BTN_CONFIRM,
    	iconCls : Constants.CLS_OK,
    	disabled : true,
    	handler : confirmHandler
    });
    
    // 取消按钮
    var cancelBtn = new Ext.Button({
    	text : Constants.BTN_CANCEL,
    	iconCls : Constants.CLS_CANCEL,
    	disabled : true,
    	handler : cancelHandler
    });
    
    // 按钮Toolbar
    var btnToolbar = new Ext.Toolbar({
    	region : 'north',
		border : true,
		height : 25,
		items : [contractChangeBtn, confirmBtn, cancelBtn]
    });
    
    // 合同数据
    var contractData = new Ext.data.Record.create([{
        name : 'deptName'
    }, {
        name : 'newDeptName'
    }, {
        name : 'deptNameFirst'
    }, {
        name : 'stationName'
    }, {
        name : 'stationId'
    }, {
        name : 'empId'
    }, {
        name : 'workContractId'
    }, {
        name : 'workContractNo'
    }, {
        name : 'firstAddress'
    }, {
        name : 'contractContinueMark'
    }, {
        name : 'workSignDate'
    }, {
        name : 'startDate'
    }, {
        name : 'endDate'
    }, {
        name : 'contractTermId'
    }, {
        name : 'deptId'
    }, {
        name : 'firstDeptId'
    }, {
        name : 'lastModifiedDate'
    }, {
        name : 'newDeptId'
    }, {
        name : 'newStationId'
    }, {
        name : 'newContractTermId'
    }, {
        name : 'changeDate'
    }, {
        name : 'newStartDate'
    }, {
        name : 'newEndDate'
    }, {
        name : 'changeReason'
    }, {
        name : 'memo'
    }, {
        name : 'lastModifiedDateChange'
    }, {
    	name : 'contractChangeId'
    }, {
    	name : 'newStationName'
    }]);
    
    // 基本信息data
    var baseInfoData =  new Ext.data.Record.create([{
        name : 'newDeptName'
    }, {
        name : 'newStationId'
    }, {
        name : 'newDeptId'
    }]);
    
    // 合同信息store
    var contractInfoStore = new Ext.data.JsonStore({
    	url : 'hr/getContractInfoForChange.action',
		root : 'list',				
		fields : contractData	
    });
    
    // 变更信息store
    var changeInfoStore = new Ext.data.JsonStore({
    	url : 'hr/getContractChangeInfo.action',
		root : 'list',				
		fields : contractData	
    });
    
    // 基本信息store
    var baseInfoStore = new Ext.data.JsonStore({
    	url : 'hr/getBaseInfoForChange.action',
		root : 'list',				
		fields : baseInfoData	
    });
    
    // 上次修改时间
    var lastModifiedDateTxt = new Ext.form.Hidden({
    	id : 'lastModifiedDate',
		value : ''
	});
	
	// 劳动合同Id
    var workContractIdTxt = new Ext.form.Hidden({
    	id : 'workContractId',
		value : ''
	});
	
	// 人员Id
    var empIdTxt = new Ext.form.Hidden({
    	id : 'empId',
		value : ''
	});
	
    // 合同编码
    var contractNoTxt = new Ext.form.TextField({
    	id : 'workContractNo',
    	fieldLabel : '合同编码'
    });
    
    // 合同有效期store
    var contractTermStore = new Ext.data.JsonStore({
        root : 'list',
        url : "hr/getContractTermForChange.action",
        fields : ['contractTermId', 'contractTerm']
    });
    
    // 加载数据
    contractTermStore.load({
        callback : function() {
            contractTermStore.insert(0, new Ext.data.Record({
                contractTerm : '',
                contractTermId : ''
            }));
        }
    });
    
    // 合同有效期ComboBox
    var contractTermCmb = new Ext.form.ComboBox({
        fieldLabel : '有效期',
        triggerAction : 'all',
        disabled : true,
        readOnly : true,
        store : contractTermStore,
        mode : 'local',
        displayField : 'contractTerm',
        valueField : 'contractTermId'
    });
    
    // 甲方部门
    var firstDeptTxt = new Ext.form.TextField({
    	id : 'deptNameFirst',
    	fieldLabel : '甲方部门',
    	width : 453
    });
    
    // 甲方部门ID
    var hiddenFirstDeptTxt = new Ext.form.Hidden({
    	id : 'firstDeptId',
		value : ''
	});
    
	// 甲方地址
    var firstAddressTxt = new Ext.form.TextField({
    	id : 'firstAddress',
    	fieldLabel : '甲方地址',
    	width : 453
    });
    
    // 人员姓名
    var empNameTxt = new Ext.form.TextField({
    	fieldLabel : '员工姓名'
    });
    
    // 合同形式
    var contractTypeTxt = new Ext.form.TextField({
    	fieldLabel : '合同形式'
    });
    
    // 合同形式
    var hiddenContractTypeTxt = new Ext.form.Hidden({
    	id : 'contractContinueMark',
    	value : ''
    });
    
    // 所属部门
    var deptTxt = new Ext.form.TextField({
    	id : 'deptName',
    	fieldLabel : '所属部门'
    });
    
    // 所属部门ID
    var hiddenDeptTxt = new Ext.form.Hidden({
    	id : 'deptId',
		value : ''
	});
    
    // 岗位
    var stationTxt = new Ext.form.TextField({
    	id : 'stationName',
    	fieldLabel : '岗位'
    });
    
     // 岗位ID
    var hiddenStationTxt = new Ext.form.Hidden({
    	id : 'stationId',
		value : ''
	});
    
    // 签字日期
    var signDateTxt = new Ext.form.TextField({
    	id : 'workSignDate',
    	fieldLabel : '签字日期'
    });
    
    // 开始日期
    var startDateTxt = new Ext.form.TextField({
    	id : 'startDate',
    	fieldLabel : '开始日期'
    });
    
    // 结束日期
    var endDateTxt = new Ext.form.TextField({
    	id : 'endDate',
    	fieldLabel : '结束日期'
    });
    
    // 合同信息FormPanel
    var contractInfoForm = new Ext.form.FormPanel({
		border : false,
		frame : false,
		labelWidth : 80,
		labelAlign : 'right',
		layout : 'form',
		items : [
		// 第一行
		{
			xtype : 'panel',
    		border : false,
    		layout : 'column',
    		items : [{
                columnWidth : 0.5,
                layout : 'form',
                border : false,
                items : [contractNoTxt, workContractIdTxt,
                	lastModifiedDateTxt, empIdTxt]
            }, {
                columnWidth : 0.5,
                layout : 'form',
                border : false,
                items : [contractTermCmb]
            }]
		},
		// 第二行
		{
			xtype : 'panel',
    		border : false,
    		layout : 'form',
    		items : [firstDeptTxt, hiddenFirstDeptTxt]
		},
		// 第三行
		{
			xtype : 'panel',
    		border : false,
    		layout : 'form',
    		items : [firstAddressTxt]
		},
		// 第四行
		{
			xtype : 'panel',
    		border : false,
    		layout : 'column',
    		items : [{
                columnWidth : 0.5,
                layout : 'form',
                border : false,
                items : [empNameTxt]
            }, {
                columnWidth : 0.5,
                layout : 'form',
                border : false,
                items : [contractTypeTxt, hiddenContractTypeTxt]
            }]
		},
		// 第五行
		{
			xtype : 'panel',
    		border : false,
    		layout : 'column',
    		items : [{
                columnWidth : 0.5,
                layout : 'form',
                border : false,
                items : [deptTxt, hiddenDeptTxt]
            }, {
                columnWidth : 0.5,
                layout : 'form',
                border : false,
                items : [stationTxt, hiddenStationTxt]
            }]
		},
		// 第六行
		{
			xtype : 'panel',
    		border : false,
    		layout : 'form',
    		items : [signDateTxt]
		},
		// 第七行
		{
			xtype : 'panel',
    		border : false,
    		layout : 'column',
    		items : [{
                columnWidth : 0.5,
                layout : 'form',
                border : false,
                items : [startDateTxt]
            }, {
                columnWidth : 0.5,
                layout : 'form',
                border : false,
                items : [endDateTxt]
            }]
		}]
	});
	
	// 变更日期
    var changeDateTxt = new Ext.form.TextField({
    	id : 'changeDate',
    	name : 'contractChange.changeDate',
    	fieldLabel : '变更日期<font color="red">*</font>',
    	style : 'cursor:pointer',
        readOnly : true
    });
    
    changeDateTxt.onClick(function() {
    	WdatePicker({
            startDate : '%y-%M-%d',
            dateFmt : 'yyyy-MM-dd',
            isShowClear : false,
            onpicked: function() {
            	changeDateTxt.clearInvalid();
            },
            onclearing:function(){
            	changeDateTxt.markInvalid();
            	changeDateTxt.setValue("");
            }
        });
    });
    
    // 合同有效期store
    var contractTermChangeStore = new Ext.data.JsonStore({
        root : 'list',
        url : "hr/getContractTermForChange.action",
        fields : ['contractTermId', 'contractTerm']
    });
    
    // 加载数据
    contractTermChangeStore.load({
        callback : function() {
            contractTermChangeStore.insert(0, new Ext.data.Record({
                contractTerm : '',
                contractTermId : ''
            }));
        }
    });
    
    // 合同有效期ComboBox
    var contractTermChangeCmb = new Ext.form.ComboBox({
    	id : 'newContractTermId',
    	hiddenName : 'contractChange.newContractTermId',
        fieldLabel : '有效期<font color ="red">*</font>',
        triggerAction : 'all',
        disabled : true,
        readOnly : true,
        store : contractTermChangeStore,
        mode : 'local',
        displayField : 'contractTerm',
        valueField : 'contractTermId'
    });
    
    
    // 开始日期
    var startDateChangeTxt = new Ext.form.TextField({
    	id : 'newStartDate',
    	name : 'contractChange.newStartDate',
    	fieldLabel : '开始日期<font color="red">*</font>',
    	style : 'cursor:pointer',
        readOnly : true
    });
    
    startDateChangeTxt.onClick(function() {
    	WdatePicker({
            startDate : '%y-%M-%d',
            dateFmt : 'yyyy-MM-dd',
            isShowClear : false,
            onpicked: function() {//add by drdu 090924
            		if (endDateChangeTxt.getValue() != "") {
					if (startDateChangeTxt.getValue() == ""
							|| startDateChangeTxt.getValue() > endDateChangeTxt.getValue()) {
						Ext.Msg.alert("提示", "必须小于结束日期");
						startDateChangeTxt.setValue("");
						return;
					}
					startDateChangeTxt.clearInvalid();
				}
            },
            onclearing:function(){
            	startDateChangeTxt.markInvalid();
            	startDateChangeTxt.setValue("");
            }
        });
    });
    
    // 结束日期
    var endDateChangeTxt = new Ext.form.TextField({
    	id : 'newEndDate',
    	name : 'contractChange.newEndDate',
    	fieldLabel : '结束日期<font color="red">*</font>',
    	style : 'cursor:pointer',
        readOnly : true
    });
    
    endDateChangeTxt.onClick(function() {
    	WdatePicker({
            startDate : '%y-%M-%d',
            dateFmt : 'yyyy-MM-dd',
            isShowClear : false,
            onpicked: function() {//add by drdu 090924
            	if (startDateChangeTxt.getValue() == ""
						|| startDateChangeTxt.getValue() > endDateChangeTxt.getValue()) {
					Ext.Msg.alert("提示", "必须大于开始日期");
					endDateChangeTxt.setValue("")
					return;
				}
            	endDateChangeTxt.clearInvalid();
            },
            onclearing:function(){
            	endDateChangeTxt.markInvalid();
            	endDateChangeTxt.setValue("");
            }
        });
    });
    
    // 所属部门
    var deptChangeTxt = new Ext.form.TextField({
    	id : 'newDeptName',
    	readOnly : true,
    	fieldLabel : '所属部门<font color="red">*</font>'
    });
    
    // 所属部门ID
    var hiddenDeptChangeTxt = new Ext.form.Hidden({
    	id : 'newDeptId',
		name : 'contractChange.newDeptId'
	});
	
	// 选择部门
    deptChangeTxt.onClick(deptSelect);
    
    // 岗位store
    var stationChangeStore = new Ext.data.JsonStore({
        root : 'list',
        url : "hr/getStationByDeptForChange.action",
        fields : ['stationName', 'stationId']
    });
    
    // 岗位ComboBox
    var stationChangeCmb = new Ext.form.ComboBox({
    	hiddenName : 'contractChange.newStationId',
        fieldLabel : '岗位<font color="red">*</font>',
        disabled : true,
        readOnly : true,
        triggerAction : 'all',
        store : stationChangeStore,
        mode : 'local',
        displayField : 'stationName',
        valueField : 'stationId'
    });
    
    // 变更原因
    var changeReasonTa = new Ext.form.TextArea({
        id : 'changeReason',
        name : 'contractChange.changeReason',
    	width : 453,
    	height : 35,
        fieldLabel : "变更原因",
        maxLength : 127
    });
    
    // 备注
    var memoTa = new Ext.form.TextArea({
        id : 'memo',
        name : 'contractChange.memo',
    	width : 453,
    	height : 35,
        fieldLabel : "备注",
        maxLength : 127
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
        // 是否可编辑
        readOnly : false,
        height : 113,
        width : 543,
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
        Ext.apply(this.uploadParams, {
            workcontractid : changeInfoStore.getAt(0).data['contractChangeId'],
            fileOriger : '1'

        });
    });
    
    // 变更信息FormPanel
    var changeInfoForm = new Ext.form.FormPanel({
		border : false,
		labelWidth : 80,
		style : "padding-bottom:10px;",
		labelAlign : 'right',
		layout : 'form',
		items : [
		// 第一行
		{
			xtype : 'panel',
    		border : false,
    		layout : 'column',
    		items : [{
                columnWidth : 0.5,
                layout : 'form',
                border : false,
                items : [changeDateTxt]
            }, {
                columnWidth : 0.5,
                layout : 'form',
                border : false,
                items : [contractTermChangeCmb]
            }]
		},
		// 第二行
		{
			xtype : 'panel',
    		border : false,
    		layout : 'column',
    		items : [{
                columnWidth : 0.5,
                layout : 'form',
                border : false,
                items : [startDateChangeTxt]
            }, {
                columnWidth : 0.5,
                border : false,
                layout : 'form',
                items : [endDateChangeTxt]
            }]
		},
		// 第三行
		{
			xtype : 'panel',
    		border : false,
    		layout : 'column',
    		items : [{
                columnWidth : 0.5,
                layout : 'form',
                border : false,
                items : [deptChangeTxt, hiddenDeptChangeTxt]
            }, {
                columnWidth : 0.5,
                layout : 'form',
                border : false,
                items : [stationChangeCmb]
            }]
		},
		// 第四行
		{
			xtype : 'panel',
    		border : false,
    		layout : 'form',
    		items : [changeReasonTa]
		},
		// 第五行
		{
			xtype : 'panel',
    		border : false,
    		layout : 'form',
    		items : [memoTa]
		}]
	});
	
	// 合同信息FieldSet
	var contractInfoFs = new Ext.form.FieldSet({
		title : '合同信息',
		border : true,
		style : "margin:5px;",
		items : [contractInfoForm]
	});
	
	// 变更信息FieldSet
	var changeInfoFs = new Ext.form.FieldSet({
		title : '变更信息',
		border : true,
		style : "padding-bottom:10px;margin:5px;",
		items : [changeInfoForm, gridQuestFile]
	});
    
   var rootNode = new Ext.tree.AsyncTreeNode({
        text : Constants.POWER_NAME,
        id : '0',
        isRoot : true
    });
    
    var treePanel = new Ext.tree.TreePanel({
        root : rootNode,
        border : false,
        rootVisible : false,
        containerScroll : true,
        autoScroll : true,
        loader : new Ext.tree.TreeLoader({
            dataUrl : "hr/getDeptEmpTreeList.action"
        })
    });

    treePanel.on("click", treeClick);
    rootNode.expand();
    
    // 左边的panel
    var leftPanel = new Ext.Panel({
        region : 'west',
        layout : 'fit',
        width : '20%',
        frame : false,
        autoScroll : true,
        containerScroll : true,
        collapsible : true,
        split : true,
        items : [treePanel]
    });
    
    // 右边的panel
    var rightPanel = new Ext.Panel({
    	layout : 'form',
    	frame : false,
    	width : 580,
    	height : 543,
    	border : false,
    	fileUpload : true,
    	items : [contractInfoFs, changeInfoFs]
    });
    
    // 总的panel
    var fullPanel = new Ext.Panel({
    	layout : 'border',
    	border : false,
    	tbar : btnToolbar,
    	defaults : {autoScroll: true},
    	items : [leftPanel, {
    		region : 'center',
    		layout : 'form',
            frame : false,
            border : true,
            items : [rightPanel]
    	}]
    });
    
    // 设定布局器及面板
    new Ext.Viewport({
        layout : "fit",
    	defaults : {autoScroll: true},
        items : [fullPanel]
    });
    
    // 合同附件panel不可用
    gridQuestFile.disable();
    
    /* ======================= 处理开始 ==================== */
    
    /**
     * 合同变更处理
     */
    function contractChangeHandler() {
    	clickCount++;
    	if(clickCount == 1) {
	    	// 设置确认按钮可用
	    	confirmBtn.setDisabled(false);
	    	// 设置取消按钮可用
	    	cancelBtn.setDisabled(false);
	    	// 设置变更信息区域可用
	    	setChangeDisable(false);
	    	// 设置变更信息区域的值
	    	resetChangeInfo(true);
	    	// 清空附件panel
	    	storeQuestFile.removeAll();
	    	gridQuestFile.clearAppendFileValue();
	    	// 合同附件panel不可用
	    	gridQuestFile.disable();
    	}
    }
    
    /**
     * 确认处理
     */
    function confirmHandler() {
    	if(checkForm()) {
    		Ext.Msg.confirm(Constants.NOTICE_CONFIRM, Constants.COM_C_001, function(
				buttonobj) {
				// 确认保存
				if (buttonobj == "yes") {
					if(changeInfoForm.getForm().isValid()
						&& checkDate(startDateChangeTxt.getValue(), endDateChangeTxt.getValue(),
    					"结束日期", "开始日期")) {
						// 合同信息区域参数
						var params = {
							'contractChange.workContractId' : workContractIdTxt.getValue(),
							'contractChange.empId' : empIdTxt.getValue(),
							'contractChange.deptId' : hiddenDeptTxt.getValue(),
							'contractChange.stationId' : hiddenStationTxt.getValue(),
							'contractChange.contractTermId' : contractTermCmb.getValue(),
							'contractChange.startDate' : startDateTxt.getValue(),
							'contractChange.endDate' : endDateTxt.getValue(),
							'contractChange.lastModifiedDate' : lastModifiedDateTxt.getValue()
						};
						// 有效期
				        if (!contractTermChangeCmb.getValue()) {
				            contractTermChangeCmb.setValue('');
				        }
						// 岗位
				        if (!stationChangeCmb.getValue()) {
				            stationChangeCmb.setValue('');
				        }
						changeInfoForm.getForm().submit({
							method : Constants.POST,
							url : 'hr/saveContractChange.action',
							params : params,
							success : function(form, action) {
				                    if (action.response.responseText) {
				                    var o = eval("(" + action.response.responseText + ")");
				                    var succ = o.msg;
		                            if(succ == Constants.DATE_FAILURE){
	                                	Ext.Msg.alert(Constants.ERROR,
	                                    	Constants.COM_E_023); 
		                            } else if(succ == Constants.SQL_FAILURE){
	                                	Ext.Msg.alert(Constants.ERROR,
	                                    	Constants.COM_E_014); 
		                            } else if(succ == Constants.DATA_USING){
	                                	Ext.Msg.alert(Constants.ERROR,
	                                    	Constants.COM_E_015); 
		                            } else {
	                                	Ext.Msg.alert(Constants.REMIND,
	                                    	Constants.COM_I_004);
	                                    // 刷新页面
	                                    findByEmpId(empIdTxt.getValue());
	                                    // 变更区域不可用
	                                    setChangeDisable(true);
	                                    // 按钮不可用
	                                    contractChangeBtn.setDisabled(true);
	                                    clickCount = 0;
	                                    confirmBtn.setDisabled(true);
	                                    cancelBtn.setDisabled(true);
		                            }
		                        }
							},
							failure : function() {
								Ext.Msg.alert(Constants.ERROR,
		                        	Constants.COM_E_014); 
							}
						})
					}
				}
			});
    	}
    }
    
    /**
     * 取消处理
     */
    function cancelHandler() {
    	Ext.MessageBox.confirm(Constants.CONFIRM, Constants.COM_C_005, function(buttonobj) {
            if (buttonobj == "yes") {
            	// 合同信息区域清空
    			resetChangeInfo(false);
            }
        })
    }
    
    /**
     * 树的点击事件
     */
    function treeClick(node, e) {
    	 e.stopEvent();
    	// 人员姓名
    	var str = node.text;
        empName = str.substring(0, str.indexOf("("));
        // 人员ID
        empId = node.id;
    	if (node.isLeaf()) {
    		changeInfoForm.getForm().trim();
    		if(isChange) {
	            if (ifChanged()) {
	                Ext.MessageBox.confirm(Constants.CONFIRM, Constants.COM_C_004, function(buttonobj) {
	                    if (buttonobj == "yes") {
	                    	// 重置合同变更区域
	                    	resetChangeInfo(true);
	                    	// 合同变更区域不可用
	                    	setChangeDisable(true);
	                    	// 根据人员ID查找
	                        findByEmpId(node.id)
	                    }
	                })
	            } else {
	            	// 合同变更区域不可用
	                setChangeDisable(true);
	                findByEmpId(node.id);
	            }
    		} else {
                findByEmpId(node.id);
            }
        }
    }
    
    /**
     * 根据员工Id查找合同信息
     */
    function findByEmpId(empId) {
    	isChange = false;
    	confirmBtn.setDisabled(true);
    	cancelBtn.setDisabled(true);
    	// 合同信息查询
        contractInfoStore.load({
        	params : {
        		empId : empId
        	},
        	callback : function() {
        		if(contractInfoStore.getCount() > 0) {
        			// 加载合同信息
    				loadContractInfo();
    				// 变更按钮可用
    				contractChangeBtn.setDisabled(false);
    				clickCount = 0;
        			// 判断是否有变更信息
        			hasChangeInfo(empId);
        		} else {
        			// 画面初始化
        			init();
        		}
        	}
        });
    }
    
    /**
     * 根据人员Id判断是否有变更信息
     */
    function hasChangeInfo(empId) {
    	changeInfoStore.load({
    		params : {
    			empId : empId,
    			workContractId : contractInfoStore.getAt(0).get("workContractId")
    		},
    		callback : function() {
    			// 有变更信息
    			if(changeInfoStore.getCount() > 0) {
    				contractChangeBtn.setDisabled(false);
    				clickCount = 0;
    				// 加载变更信息
    				loadChangeInfo();
    			} else {
    				// 清空form
    				changeInfoForm.getForm().reset();
    				// 清空附件panel
			    	storeQuestFile.removeAll();
			    	gridQuestFile.clearAppendFileValue();
			    	// 合同附件panel不可用
			    	gridQuestFile.disable();
    			}
    		}
    	});
    }
    
    /**
     * 加载合同信息
     */
    function loadContractInfo() {
    	// 重置form
    	contractInfoForm.getForm().reset();
    	var record = contractInfoStore.getAt(0);
    	// 加载数据
    	contractInfoForm.getForm().loadRecord(record);
    	// 设置有效期
    	setComponentValue(contractTermCmb, record.get("contractTermId"), true);
    	// 设置合同形式
    	var contractType = record.get("contractContinueMark");
    	if(contractType == CONTRACT_TYPE_NEW) {
    		setComponentValue(contractTypeTxt, STRING_CONTRACT_TYPE_NEW);
    	} else if(contractType == CONTRACT_TYPE_CONTINUE) {
    		setComponentValue(contractTypeTxt, STRING_CONTRACT_TYPE_CONTINUE);
    	}
    	// 设置部门
    	setComponentValue(deptTxt, record.get("deptName"));
    	// 设置姓名
    	empNameTxt.setValue(empName);
    	// 设置人员Id
    	empIdTxt.setValue(empId);
    }
    
    /**
     * 加载变更信息
     */
    function loadChangeInfo() {
    	// 重置form
    	changeInfoForm.getForm().reset();
    	// 加载数据
    	var recordChange = changeInfoStore.getAt(0);
    	changeInfoForm.getForm().loadRecord(recordChange);
	    // 设置岗位
		setStationCmb(recordChange, false);
	    // 设置有效期
	    setComponentValue(contractTermChangeCmb, recordChange.get("newContractTermId"), true);
    	changeInfoForm.getForm().clearInvalid();
    	// 合同附件加载
    	var record = changeInfoStore.getAt(0);
    	if (record.get('contractChangeId') != null && record.get('contractChangeId') != "") {
            gridQuestFile.enable();
            gridQuestFile.clearAppendFileValue();
            storeQuestFile.load({
                params : {
                    workcontractid : record.get('contractChangeId'),
                    fileOriger : '1'
                }
            });
        }
    }
    
    /**
     * 下载文件
     */
    downloadFile = function(id) {
        document.all.blankFrame.src = "hr/downloadContractAppendFile.action?fileId=" + id;
    }
    
    /**
     * 初始化
     */
    function init() {
    	isChange = false;
    	// 清空form
    	contractInfoForm.getForm().reset();
    	changeInfoForm.getForm().reset();
    	// 变更信息区域不可用
    	setChangeDisable(true);
    	// 清空附件panel
    	storeQuestFile.removeAll();
    	gridQuestFile.clearAppendFileValue();
    	// 合同附件panel不可用
    	gridQuestFile.disable();
    	// 设置按钮不可用
    	contractChangeBtn.setDisabled(true);
    	clickCount = 0;
    	confirmBtn.setDisabled(true);
    	cancelBtn.setDisabled(true);
    }
    
    /**
     * 变更信息区域是否可用
     */
    function setChangeDisable(flag) {
    	changeDateTxt.setDisabled(flag);
    	contractTermChangeCmb.setDisabled(flag);
    	startDateChangeTxt.setDisabled(flag);
    	endDateChangeTxt.setDisabled(flag);
    	deptChangeTxt.setDisabled(flag);
    	stationChangeCmb.setDisabled(flag);
    	changeReasonTa.setDisabled(flag);
    	memoTa.setDisabled(flag);
    }
    
    /**
     * 设置控件的值
     */
    function setComponentValue(component, value, flag) {
    	if(value == null) {
    		value = "";
    	}
    	if(flag) {
    		component.setValue(value, true);
    	} else {
    		component.setValue(value);
    	}
    }
    
     /**
     * 选择部门
     */
    function deptSelect() {
    	var args = {
    		selectModel : 'single',
    		rootNode : {
    			id : '0',
    			text : Constants.POWER_NAME
    		}
    	};
    	// 调用画面
		var object = window.showModalDialog(
						'../../../../comm/jsp/hr/dept/dept.jsp',
						args,
						'dialogWidth='  + Constants.WIDTH_COM_DEPT +
						'px;dialogHeight=' + Constants.HEIGHT_COM_DEPT +
						'px;center=yes;help=no;resizable=no;status=no;');
		// 根据返回值设置画面的值
		if (object) {
			stationChangeCmb.clearValue();
			if (typeof(object.names) != "undefined") {
				deptChangeTxt.setValue(object.names);
			}
			if (typeof(object.ids) != "undefined") {
				hiddenDeptChangeTxt.setValue(object.ids);
			}
		}
    	// 加载数据
	    stationChangeStore.load({
	    	params : {
	    		deptId : hiddenDeptChangeTxt.getValue()
	    	},
	        callback : function() {
	            stationChangeStore.insert(0, new Ext.data.Record({
	                stationId : '',
	                stationName : ''
	            }));
	        }
	    });
    }
    
    /**
     * 重置合同变更区域
     */
    function resetChangeInfo(flag) {
    	isChange = true;
    	// 合同变更区域清空
    	changeInfoForm.getForm().reset();
    	if(flag) {
    		baseInfoStore.load({
    			params : {
    				empId : empIdTxt.getValue()	
    			},
    			callback : function() {
    				if(baseInfoStore.getCount() > 0) {
	    				// 加载数据
	    				changeInfoForm.getForm().loadRecord(baseInfoStore.getAt(0));
	    				// 设置岗位
			    		setStationCmb(baseInfoStore.getAt(0), true);
    				}
    			}
    		});	
    	} else {
    		if(baseInfoStore.getCount() > 0) {
	    		// 加载数据
	    		changeInfoForm.getForm().loadRecord(baseInfoStore.getAt(0));
	    		// 设置岗位
			    setStationCmb(baseInfoStore.getAt(0), true);
    		}
    	}
    	// 设置变更时间
    	changeDateTxt.setValue(today);
    }
    
    /**
     * 设置岗位
     */
    function setStationCmb(record, flag) {
    	// 加载岗位数据
	    stationChangeStore.load({
	    	params : {
	    		deptId : record.get("newDeptId")
	    	},
	        callback : function() {
	            stationChangeStore.insert(0, new Ext.data.Record({
	                stationId : '',
	                stationName : ''
	            }));
	            if(flag) {
		            var flagStation = false;
		            for(var i = 0; i < stationChangeStore.getCount(); i++) {
		            	var recordStation = stationChangeStore.getAt(i);
		            	if(recordStation.get("stationId") == record.get("newStationId")) {
		            		flagStation = true;
		            		break;
		            	} else {
		            		flagStation = false;
		            	}
		            }
		            if(flagStation) {
		            	stationChangeCmb.setValue(record.get("newStationId"));
		            } else {
		            	stationChangeCmb.setValue("");
		            	baseInfoStore.getAt(0).set("newStationId", "");
		            }
	            } else {
	            	// 设置岗位
	            	setComponentValue(stationChangeCmb, record.get("newStationId"), true);
	            	if(stationChangeCmb.getValue() == "") {
	            		stationChangeCmb.setRawValue(record.get("newStationName"));
	            	}
	            }
	        }
	    });
    }
    
    /**
     * 检查form
     */
    function checkForm() {
    	var msg = "";
    	msg += checkInput(changeDateTxt, "变更日期", TYPE_SELECT);
    	msg += checkInput(contractTermChangeCmb, "有效期", TYPE_SELECT);
    	msg += checkInput(startDateChangeTxt, "开始日期", TYPE_SELECT);
    	msg += checkInput(endDateChangeTxt, "结束日期", TYPE_SELECT);
    	msg += checkInput(deptChangeTxt, "所属部门", TYPE_SELECT);
    	msg += checkInput(stationChangeCmb, "岗位", TYPE_SELECT);
    	if(msg != "") {
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
    	if(component.getValue() == "" || component.getValue() == null) {
    		if(type == TYPE_INPUT) {
    			message += String.format(Constants.COM_E_002, name);
    		} else if(type == TYPE_SELECT) {
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
    	if(startDate == "" || endDate == "") {
    		return true;
    	} else {
    		var start = Date.parseDate(startDate, 'Y-m-d');
	    	var end = Date.parseDate(endDate, 'Y-m-d');
	    	if(start.getTime() > end.getTime()) {
	    		Ext.Msg.alert(Constants.ERROR, String.format(Constants.COM_E_005, string1, string2));
	    		return false;
	    	}
	    	return true;
    	}
    }
    
    /**
     * 判断变更信息区域是否被更改过
     */
    function ifChanged() {
    	var flag = false;
		// 变更时间
		if (today != changeDateTxt.getValue()) {
			return true;
		}
		// 有效期
		if(!("" == contractTermChangeCmb.getValue()
			|| null == contractTermChangeCmb.getValue())) {
		    return true;
		}
		// 开始时间
		if ("" != startDateChangeTxt.getValue()) {
			return true;
		}
		// 结束时间
		if ("" != endDateChangeTxt.getValue()) {
			return true;
		}
		if(baseInfoStore.getCount() > 0) {
			var record = baseInfoStore.getAt(0);
			// 部门
			if(record.get("newDeptName") != deptChangeTxt.getValue()) {
			    return true;
			}
			// 岗位
			if(record.get("newStationId") != stationChangeCmb.getValue()) {
			    return true;
			}
		}
		// 变更原因
		if("" != changeReasonTa.getValue()) {
		    return true;
		}
		// 备注
		if("" != memoTa.getValue()) {
		    return true;
		}
		
		return flag;
    }
    
    /**
     * 日期初值
     */
	function dateInit() {
		today = new Date();
		today = dateFormat(today);
	}
	
	/**
	 * 格式日期
	 */
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
});
