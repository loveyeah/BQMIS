Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
    // 劳动合同id
    var contractId = '';
    // 附件来源
    var fileOriger = '';
    // 新签
    var NEW_SIGN = 0;
    // 续签
    var CONTINUE_SIGN = 2;
    // 定义弹出附件画面的function
    showWindow = function(id, mark) {
        var args = {
            mode : 'single',
            contractId : id,
            fileOriger : mark
        };
        var person = window.showModalDialog('../../../common/PC002.jsp', args,
        'dialogWidth:506px;dialogHeight:396px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
    }

    Ext.QuickTips.init();
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
        }, {
        	// add by ywliu 20100611
            name : 'owner'
        }, {
        	// add by ywliu 20100611
            name : 'signedInstitutions'
        }, {
        	// add by ywliu 20100611
            name : 'contractPeriod'
        }, {
        	// add by ywliu 20100611
            name : 'laborType'
        }, {
        	// add by ywliu 20100611
            name : 'contractType'
        }])
    // 所有合同信息和人员信息的json阅读器
    var contractInfoReader = new Ext.data.JsonReader({
        root : "list",
        totalProperty : "totalCount"
    }, contractRecord)
    var contractInfoStore = new Ext.data.Store({
        reader : contractInfoReader
    });

    // 新增合同
    var btnAddContract = new Ext.Button({
        text : "新增合同",
        disabled : true,
        iconCls : Constants.CLS_ADD,
        handler : function() {
        	var oldRecord = contractInfoStore.getAt(0);
        	var record = new contractRecord({
        		'deptId' : oldRecord.get('deptId'),
	        	'empName' : oldRecord.get('empName'),
	        	'empId' : oldRecord.get('empId'),
	        	'deptName' : oldRecord.get('deptName'),
	        	'stationId' : oldRecord.get('stationId'),
	        	'stationName' : oldRecord.get('stationName'),
	        	'contractContinueMark' : oldRecord.get('contractContinueMark')
        	});
        	contractInfoStore.removeAll();
        	contractInfoStore.add(record);
            signYes(contractInfoStore);
        }
    });
    // 保存合同
    var btnSaveContract = new Ext.Button({
        text : "保存合同",
        disabled : true,
        iconCls : Constants.CLS_SAVE,
        handler : function() {
            if (checkNullMain()) {
            	Ext.MessageBox.confirm(Constants.CONFIRM, Constants.COM_C_001, function(buttonobj) {
                if (buttonobj == "yes") {
                        if (formInfo.getForm().isValid()) {
                            if (checkTime(dteStart, dteEnd)) {
                                var sendUrl = '';
                                if (hidContractId.getValue() != "") {
                                    sendUrl = 'hr/updateContract.action'
                                } else {
                                    sendUrl = 'hr/saveContract.action'
                                }
                                formInfo.getForm().submit({
                                    method : 'POST',
                                    params : {
                                        'bean.empName' : txtName.getRawValue(),
                                        'bean.empId' : txtName.getValue(),
                                        'bean.contractContinueMark' : txtContactForm.getValue(),
                                        'bean.deptId' : txtDeptOf.getValue(),
                                        'bean.stationId' : txtStation.getValue()
                                    },
                                    url : sendUrl,
                                    success : function(form, action) {
                                        var o = eval("(" + action.response.responseText + ")");
                                        // 排他
                                        if (o.msg == "S") {
                                            // 成功
                                            Ext.Msg.alert(Constants.REMIND, Constants.COM_I_004);
                                            findByEmpId(contractInfoStore.getAt(0).data['empId']);
                                            // 排他
                                        } else if (o.msg == Constants.DATA_USING) {
                                            Ext.Msg.alert(Constants.ERROR, Constants.COM_E_015);
                                            return;
                                            // Sql错误
                                        } else if (o.msg == Constants.SQL_FAILURE) {
                                            Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                                            return;
                                            // IO错误
                                        } else if (o.msg == Constants.IO_FAILURE) {
                                            Ext.Msg.alert(Constants.ERROR, Constants.COM_E_022);
                                            return;
                                            // 数据格式化错误
                                        } else if (o.msg == Constants.DATE_FAILURE) {
                                            Ext.Msg.alert(Constants.ERROR, Constants.COM_E_023);
                                            return;
                                        } else if (o.msg == "X") {
                                            Ext.Msg.alert(Constants.REMIND, String.format(Constants.PD016_E_001,
                                            o.empName));
                                            findByEmpId(txtName.getValue());
                                            return;
                                        }
                                    }

                                });
                            }
                        }
                    }
                })
            }
        }
    });
    // 批处理
    var btnBatDo = new Ext.Button({
        text : "批处理",
        iconCls : Constants.CLS_BAT,
        handler : function() {
            btnAddWorker.setDisabled(false);
            btnDeleteWorker.setDisabled(false);
            btnSignWorker.setDisabled(true);
            win.x = undefined;
            win.y = undefined;
            storeWorker.removeAll();
            setDisabled(formInfoWin, true);
            gridQuestFileWin.on('beforeedit', function(obj) {
                obj.cancel = false;
            })
            win.show();
            formInfoWin.getForm().reset();
        }
    });

    /**
     * 根据员工Id查找合同信息
     */
     //alert(0)
    function findByEmpId(empId) {
        Ext.Ajax.request({
            url : 'hr/getContractByEmpId.action',
            method : Constants.POST,
            params : {
                // 员工ID
                empId : empId
            },
            success : function(action) {
                var result = eval("(" + action.responseText + ")");
                if (result.msg != undefined && result.msg == Constants.SQL_FAILURE) {
                    // 失败时，弹出提示
                    Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
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
        if (obj.getCount() == 1 && obj.getAt(0).data['workcontractid'] != "" && obj.getAt(0).data['ifExecute'] == '1') {
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
            setDisabled(formInfo, true);
            btnAddContract.disable();
            btnSaveContract.enable();
            formInfo.getForm().loadRecord(obj.getAt(0));
            txtContactForm.setValue('0');
            txtStation.setRawValue(obj.getAt(0).get("stationName"));
            formInfo.getForm().clearInvalid();
            if (obj.getAt(0).data['workcontractid'] != null && obj.getAt(0).data['workcontractid'] != "") {
                gridQuestFile.enable();
                gridQuestFile.clearAppendFileValue();
		    	if (0 == obj.getAt(0).data['contractContinueMark']) {
		    		fileOriger = NEW_SIGN;
		    	} else if (1 == obj.getAt(0).data['contractContinueMark']) {
		    		fileOriger = CONTINUE_SIGN;
		    	}
                storeQuestFile.load({
                    params : {
                        workcontractid : obj.getAt(0).data['workcontractid'],
                        fileOriger : fileOriger
                    }

                })
            }
            return true;
        }
    }
    /**
     * 没有签合同的操作
     */
    function signNo(obj) {
        formInfo.getForm().loadRecord(new contractRecord({
            "contractContinueMark" : "",
            "contractTermId" : "",
            "deptId" : "",
            "deptName" : "",
            "empId" : "",
            "empName" : "",
            "endDate" : "",
            "enterpriseCode" : " ",
            "fristDepId":"",
            "fristDepName":"",
            "fristAddrest" : "",
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
        setDisabled(formInfo, false);
        gridQuestFile.disable();
        gridQuestFile.clearAppendFileValue();
        btnAddContract.enable();
        storeQuestFile.removeAll();
        return false;

    }
    // 按钮toolbar
    var tbrButton = new Ext.Toolbar({
        anchor : '100%',
        items : [btnAddContract, btnSaveContract, btnBatDo]
    });

    // 定义根节点： 皖能合肥电厂（不是“合肥电厂”）
    var root = new Ext.tree.AsyncTreeNode({
        text : Constants.POWER_NAME,
        isRoot : true,
        id : '0'
    });

    // 定义加载器

    var treeLoader = new Ext.tree.TreeLoader({
        dataUrl : 'hr/getDeptEmpTreeList.action'
    })

    // 定义部门树

    var treeEmployee = new Ext.tree.TreePanel({
        autoScroll : true,
        containerScroll : true,
        collapsible : true,
        split : true,
        border : false,
        rootVisible : false,
        root : root,
        animate : true,
        enableDD : false,
        loader : treeLoader

    });

    treeEmployee.on("click", treeClick);
    treeEmployee.setRootNode(root);
    root.select();

    /**
     * 点击树时
     */
    function treeClick(node, e) {
    	 e.stopEvent();
        if (node.isLeaf()) {
        	//fangjihu
        	btnAddContract.setDisabled(true);
        	btnSaveContract.setDisabled(true);
        	formInfo.getForm().trim();
            if (formInfo.getForm().isDirty()) {
                Ext.MessageBox.confirm(Constants.CONFIRM, Constants.COM_C_004, function(buttonobj) {
                    if (buttonobj == "yes") {
                        findByEmpId(node.id)
                    }
                })
            } else {
                findByEmpId(node.id);
            }
        }
        
    }
    // 合同编码
    var txtContractNo = new Ext.form.CodeField({
        fieldLabel : "合同编码<font color ='red'>*</font>",
        width : 120,
        allowBlank : false,
        anchor : '100%',
        id : 'wrokContractNo',
        maxLength : 6,
        name : 'bean.wrokContractNo'

    });
    var hidContractId = new Ext.form.Hidden({
        id : 'workcontractid',
        name : 'bean.workcontractid'
    });
    var hidLastModifiedDate = new Ext.form.Hidden({
        id : 'lastModifiedDate',
        name : 'bean.lastModifiedDate'
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
        fieldLabel : '有效期<font color ="red">*</font>',
        anchor : '100%',
        allowBlank : false,
        id : 'contractTermId',
        hiddenName : 'bean.contractTermId',
        readOnly : true,
        triggerAction : 'all',
        store : storeTerm,
        mode : 'local',
        valueNotFoundText : '',
        displayField : 'contractTerm',
        valueField : 'contractTermId'
    });
    // 第一行
    var firstLine = new Ext.form.FieldSet({
        layout : 'column',
        anchor : '100%',
        style : "padding-top:15px;padding-bottom:0px;border:0px;margin:0px;",
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
    
    // 甲方 add by ywliu 20100611
    var txtOwner = new Ext.form.TextField({
    	id : 'txtOwner',
    	fieldLabel : '甲方',
    	anchor : '100%',
    	name : 'bean.owner'
    });
    
    // 甲方部门
    var txtDeptA = new Ext.form.TextField({
    	id : 'fristDepName',
    	fieldLabel : '甲方部门<font color ="red">*</font>',
    	anchor : '100%',
    	readOnly : true,
    	allowBlank : false
    });
    
    // 甲方部门ID
    var txtDeptAHidden = new Ext.form.Hidden({
		id : 'fristDepId',
		name : 'bean.fristDepId',
		value : ''
	});
	
	// 选择部门
	txtDeptA.onClick(function() {
		deptSelect("M");
	});
    
    // 第二行
    var secondLine = new Ext.form.FieldSet({
        layout : 'column',
        anchor : '100%',
        style : "padding-top:8px;padding-bottom:0px;border:0px;margin:0px;",
        border : false,
        items : [{
                columnWidth : 1,
                border : false,
                layout : 'form',
                items : [txtOwner]
            },{
                columnWidth : 1,
                border : false,
                layout : 'form',
                items : [txtDeptA, txtDeptAHidden]
            }]
    });
    // 甲方地址
    var txtAddress = new Ext.form.TextField({
        fieldLabel : '甲方地址',
        witdh : 240,
        anchor : '100%',
        maxLength : 15,
        id : 'fristAddrest',
        name : 'bean.fristAddrest'
    });
    
    // 签订机构 add by ywliu 20100611
    var txtSignedInstitutions = new Ext.form.TextField({
        fieldLabel : '签订机构',
        witdh : 240,
        anchor : '100%',
        maxLength : 15,
        id : 'signedInstitutions',
        name : 'bean.signedInstitutions'
    });
    // 第三行
    var thirdLine = new Ext.form.FieldSet({
        layout : 'column',
        anchor : '100%',
        style : "padding-top:8px;padding-bottom:0px;border:0px;margin:0px;",
        border : false,
        items : [{
                columnWidth : 1,
                border : false,
                layout : 'form',
                items : [txtAddress]
            },{
                columnWidth : 1,
                border : false,
                layout : 'form',
                items : [txtSignedInstitutions]
            }]
    });
    // 人员姓名
    var txtName = new Ext.form.ComboBox({
        fieldLabel : "员工姓名",
        store : contractInfoStore,
        displayField : 'empName',
        valueField : 'empId',
        disabled : true,
        witdh : 120,
        readOnly : true,
        valueNotFoundText : '',
        anchor : '100%',
        id : 'empId',
        hiddenName : 'bean.empId',
        triggerClass : 'noButtonCombobox',
        listeners : {
            'beforequery' : function(obj) {
                obj.cancel = true;
            }
        }
    });
    // 合同形式
    var txtContactForm = new Ext.form.CmbHRCode({
        hiddenName : 'bean.contractContinueMark',
        id : 'contractContinueMark',
        triggerClass : 'noButtonCombobox',
        fieldLabel : "合同形式",
        anchor : '100%',
        type : '劳动合同形式',
        readOnly : true,
        disabled : true,
        listeners : {
            'beforequery' : function(obj) {
                obj.cancel = true;
            }
        }
    });
    // txtContactForm.setValue('0');
    // 第四行
    var fourthLine = new Ext.form.FieldSet({
        layout : 'column',
        anchor : '100%',
        border : false,
        style : "padding-top:8px;padding-bottom:0px;border:0px;margin:0px;",
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
    var txtDeptOf = new Ext.form.ComboBox({
        fieldLabel : "所属部门",
        store : contractInfoStore,
        readOnly : true,
        disabled : true,
        anchor : '100%',
        valueNotFoundText : '',
        id : 'deptId',
        hiddenName : 'bean.deptId',
        displayField : 'deptName',
        valueField : 'deptId',
        triggerClass : 'noButtonCombobox',
        listeners : {
            'beforequery' : function(obj) {
                obj.cancel = true;
            }
        }
    });
    // 岗位
    var txtStation = new Ext.form.ComboBox({
        fieldLabel : "岗位",
        disabled : false,
        store : contractInfoStore,
        anchor : '100%',
        id : 'stationId',
        valueNotFoundText : '',
        hiddenName : 'bean.stationId',
        displayField : 'stationName',
        valueField : 'stationId',
        triggerClass : 'noButtonCombobox',
        listeners : {
            'beforequery' : function(obj) {
                obj.cancel = true;
            }
        }
    });
    
    // 用工形式
    var txtLaborType = new Ext.form.ComboBox({
        fieldLabel : "用工形式",
        disabled : false,
        store : new Ext.data.SimpleStore({fields:['laborType','laborName'],data:[['1','临时工'],['2','培训工'],['3','合同工'],['4','劳务派遣'],['5','其它']]}),
        anchor : '100%',
        id : 'laborType',
        valueNotFoundText : '',
        hiddenName : 'bean.laborType',
        displayField : 'laborName',
        valueField : 'laborType',
        triggerAction : 'all',
        mode: 'local'
    });
    
    // 合同类别
    var txtContractType = new Ext.form.ComboBox({
        fieldLabel : "合同类别",
        disabled : false,
        store : new Ext.data.SimpleStore({fields:['contractType','contractName'],data:[['1','固定期限合同'],['2','无固定期限合同'],['3','完成一定工作期限合同']]}),
        anchor : '100%',
        id : 'contractType',
        valueNotFoundText : '',
        hiddenName : 'bean.contractType',
        displayField : 'contractName',
        valueField : 'contractType',
        triggerAction : 'all',
        mode: 'local'
    });
    // 第五行
    var fifthLine = new Ext.form.FieldSet({
        layout : 'column',
        anchor : '100%',
        border : false,
        style : "padding-top:8px;padding-bottom:0px;border:0px;margin:0px;",
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
            },{
                columnWidth : 0.5,
                border : false,
                layout : 'form',
                items : [txtLaborType]
            }, {
                columnWidth : 0.5,
                border : false,
                layout : 'form',
                items : [txtContractType]
            }]
    });
    // 签字日期
    var dteSign = new Ext.form.TextField({
        readOnly : true,
        checked : true,
        fieldLabel : '签字日期<font color ="red">*</font>',
        width : 240,
        anchor : '100%',
        id : 'workSignDate',
        name : 'bean.workSignDate',
        style : 'cursor:pointer'
    });
    
    dteSign.onClick(function() {
    	WdatePicker({
            // 时间格式
            startDate : '%y-%M-%d',
            dateFmt : 'yyyy-MM-dd',
            alwaysUseStartDate : false,
            isShowClear : false
        });
    });
    
    // 签字日期 add by ywliu 20100611
    var contractPeriod = new Ext.form.TextField({
        fieldLabel : '合同期限',
        width : 240,
        anchor : '100%',
        id : 'contractPeriod',
        name : 'bean.contractPeriod'
    });
    
    // 第六行
    var sixthLine = new Ext.form.FieldSet({
        layout : 'column',
        anchor : '100%',
        border : false,
        style : "padding-top:8px;padding-bottom:0px;border:0px;margin:0px;",
        items : [{
                columnWidth : 0.5,
                border : false,
                layout : 'form',
                items : [dteSign]
            },{
                columnWidth : 0.5,
                border : false,
                layout : 'form',
                items : [contractPeriod]
            }]
    });
    
    // 开始日期
    var dteStart = new Ext.form.TextField({
        readOnly : true,
        checked : true,
        fieldLabel : '开始日期<font color ="red">*</font>',
        anchor : '100%',
        width : 120,
        id : 'startDate',
        name : 'bean.startDate',
        style : 'cursor:pointer'
    });
    
    dteStart.onClick(function() {
		WdatePicker({
			// 时间格式
			startDate : '%y-%M-%d',
			dateFmt : 'yyyy-MM-dd',
			alwaysUseStartDate : false,
			isShowClear : false,
			onpicked : function() {//add by drdu 090924
				if (dteEnd.getValue() != "") {
					if (dteStart.getValue() == ""
							|| dteStart.getValue() > dteEnd.getValue()) {
						Ext.Msg.alert("提示", "必须小于结束日期");
						dteStart.setValue("");
						return;
					}
					dteStart.clearInvalid();
				}
			},
			onclearing : function() {
				startDate.markInvalid();
			}
		});
	});
    
    // 结束日期
    var dteEnd = new Ext.form.TextField({
        readOnly : true,
        checked : true,
        fieldLabel : '结束日期<font color ="red">*</font>',
        anchor : '100%',
        width : 120,
        id : 'endDate',
        name : 'bean.endDate',
        style : 'cursor:pointer'
    });
    
    dteEnd.onClick(function() {
    	WdatePicker({
            // 时间格式
            startDate : '%y-%M-%d',
            dateFmt : 'yyyy-MM-dd',
            alwaysUseStartDate : false,
            isShowClear : false,
            onpicked : function() {//add by drdu 090924
				if (dteStart.getValue() == ""
						|| dteStart.getValue() > dteEnd.getValue()) {
					Ext.Msg.alert("提示", "必须大于开始日期");
					dteEnd.setValue("")
					return;
				}
				dteEnd.clearInvalid();
			},
			onclearing : function() {
				endDate.markInvalid();
			}
        });
    });
    
    // 第七行
    var seventhLine = new Ext.form.FieldSet({
        layout : 'column',
        anchor : '100%',
        border : false,
        style : "padding-top:8px;padding-bottom:0px;border:0px;margin:0px;",
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
    // 备注
    var txaMemo = new Ext.form.TextArea({
        fieldLabel : '备注',
        anchor : '99.5%',
        maxLength : 127,
        height:60,
        id : 'memo',
        name : 'bean.memo'
    });
    // 第八行
    var eightLine = new Ext.form.FieldSet({
        layout : 'column',
        anchor : '100%',
        border : false,
        style : "padding-top:8px;padding-bottom:15px;border:0px;margin:0px;",
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
        frame : true,
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
        height : 150,
        width : 543,
        autoScroll : true,
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
    	if (0 == contractInfoStore.getAt(0).data['contractContinueMark']) {
    		fileOriger = NEW_SIGN;
    	} else if (1 == contractInfoStore.getAt(0).data['contractContinueMark']) {
    		fileOriger = CONTINUE_SIGN;
    	}
        Ext.apply(this.uploadParams, {
            workcontractid : contractInfoStore.getAt(0).data['workcontractid'],
            fileOriger : fileOriger

        });
    })

    // 右边panel
    var formInfo = new Ext.FormPanel({
        layout : 'form',
        border : false,
        labelAlign : 'right',
        labelWidth : 80,
        frame : false,
        trackResetOnLoad : true,
        style : "padding-top:0px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
        items : [firstLine, secondLine, thirdLine, fourthLine, fifthLine, sixthLine, seventhLine, eightLine]
    });

    var changeInfoFs = new Ext.form.FieldSet({
        border : false,
        style : "padding-bottom:10px;margin:5px;",
        items : [formInfo, gridQuestFile]
    });

    // 新增员工
    var btnAddWorker = new Ext.Button({
        text : "新增员工",
        iconCls : Constants.CLS_ADD,
        handler : function() {
            var args = {
                selectModel : 'single',
                rootNode : {
                    id : '0',
                    text : '合肥电厂'
                },
                onlyLeaf : false
            };
            var person = window.showModalDialog('../../../../comm/jsp/hr/workerByDept/workerByDept.jsp', args,
            'dialogWidth:' + Constants.WIDTH_COM_EMPLOYEE +
            'px;dialogHeight:' + Constants.HEIGHT_COM_EMPLOYEE +
            'px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
            if (typeof(person) != "undefined") {
                Ext.Ajax.request({
                    url : 'hr/getContractByEmpId.action',
                    method : Constants.POST,
                    params : {
                        // 员工ID
                        empId : person.empId
                    },
                    success : function(action) {
                        var result = eval("(" + action.responseText + ")");
                        if (result.msg != undefined && result.msg == Constants.SQL_FAILURE) {
                            // 失败时，弹出提示
                            Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                        } else {
                            contractInfoStore.loadData(result);
                            if (!checkIsSigned(contractInfoStore)) {
                                if (storeWorker.getCount() == 0) {
                                    storeWorker.insert(storeWorker.getCount(), new recordWorker({
                                        'empId' : person.empId,
                                        'empName' : person.workerName,
                                        'deptId' : person.deptId,
                                        'deptName' : person.deptName,
                                        'stationId' : person.stationId,
                                        'stationName' : person.stationName,
                                        'wrokContractNo' : '',
                                        'workcontractid' : ''
                                    }));
                                    gridQuestFileWin.getView().refresh();
                                    btnSignWorker.setDisabled(false);
                                } else {
                                    for (i = 0; i < storeWorker.getCount(); i++) {
                                        if (storeWorker.getAt(i).data['empId'] != person.empId) {
                                            if (i == storeWorker.getCount() - 1) {
                                                storeWorker.insert(storeWorker.getCount(), new recordWorker({
                                                    'empId' : person.empId,
                                                    'empName' : person.workerName,
                                                    'deptId' : person.deptId,
                                                    'deptName' : person.deptName,
                                                    'stationId' : person.stationId,
                                                    'stationName' : person.stationName,
                                                    'wrokContractNo' : '',
                                                    'workcontractid' : ''
                                                }));
                                                gridQuestFileWin.getView().refresh();
                                                btnSignWorker.setDisabled(false);
                                                break;
                                            } else {
                                                continue;
                                            }
                                        } else {
                                            Ext.Msg.alert(Constants.ERROR, Constants.PD016_E_002);
                                            break;
                                        }
                                    }
                                }
                            } else {
                                Ext.MessageBox.alert(Constants.ERROR, person.workerName + "已经签约");
                            }
                        }
                    }
                });

            }
        }
    });

    /**
     * 判断是否签合同
     */
    function checkIsSigned(obj) {
        if (obj.getCount() == 1 && obj.getAt(0).data['workcontractid'] != "" && obj.getAt(0).data['ifExecute'] == 1) {
            return true;
        } else {
            return false;
        }
    }
    // 删除员工
    var btnDeleteWorker = new Ext.Button({
        text : "删除员工",
        iconCls : Constants.CLS_DELETE,
        handler : function() {
            deleteWorker();
        }
    });
    // 签订合同
    var btnSignWorker = new Ext.Button({
        text : "签订合同",
        iconCls : Constants.CLS_SAVE,
        handler : function() {
            if (checkNullWin()) {
            	Ext.MessageBox.confirm(Constants.CONFIRM, Constants.COM_C_001, function(buttonobj) {
	                if (buttonobj == "yes") {
	                	if (formInfoWin.getForm().isValid()) {
	                        if (checkTime(dteStartWin, dteEndWin)) {
	                            batSignContract();
	                        }
	                     }
	                 }
	             });
            }
        }
    });
    // 按钮toolbar
    var tbrButtonWin = new Ext.Toolbar({
        anchor : '100%',
        items : [btnAddWorker, btnDeleteWorker, btnSignWorker]
    });

    // 合同形式
    var txtContactFormWin = new Ext.form.CmbHRCode({
        id : 'contractTypeWin',
        fieldLabel : "合同形式",
        anchor : '100%',
        triggerClass : 'noButtonCombobox',
        type : '劳动合同形式',
        readOnly : true,
        disabled : true
    });

    txtContactFormWin.setValue('0');
    txtContactFormWin.on('beforequery', function(obj) {
        obj.cancel = true;
    });
    // 有效期
    var cmbTermWin = new Ext.form.ComboBox({
        fieldLabel : '有效期<font color ="red">*</font>',
        anchor : '100%',
        allowBlank : false,
        readOnly : true,
        triggerAction : 'all',
        store : storeTerm,
        mode : 'local',
        name : 'contractTermIdWin',
        displayField : 'contractTerm',
        valueField : 'contractTermId'
    });
    
    // 甲方部门
    var txtDeptAWin = new Ext.form.TextField({
    	fieldLabel : '甲方部门<font color ="red">*</font>',
    	anchor : '100%',
    	readOnly : true,
    	allowBlank : false
    });
    
    // 甲方部门ID
    var txtDeptAWinHidden = new Ext.form.Hidden({
		value : ''
	});
	
	// 选择部门
	txtDeptAWin.onClick(function() {
		deptSelect("S");
	});
	
    // 甲方地址
    var txtAddressWin = new Ext.form.TextField({
        fieldLabel : '甲方地址',
        witdh : 240,
        maxLength : 15,
        anchor : '100%'
    });
    
    // 签字日期
    var dteSignWin = new Ext.form.TextField({
        readOnly : true,
        checked : true,
        fieldLabel : '签字日期<font color ="red">*</font>',
        anchor : '100%',
        style : 'cursor:pointer',
        	listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd',
									alwaysUseStartDate : false,
									isShowClear : false
								});
					}
				}
    });
    
//    dteSignWin.onClick(function() {
//    	WdatePicker({
//            // 时间格式
//            startDate : '%y-%M-%d',
//            dateFmt : 'yyyy-MM-dd',
//            alwaysUseStartDate : false,
//            isShowClear : false
//        });
//    });
    
    // 开始日期
    var dteStartWin = new Ext.form.TextField({
        readOnly : true,
        checked : true,
        fieldLabel : '开始日期<font color ="red">*</font>',
        anchor : '100%',
        style : 'cursor:pointer',
        	listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd',
									alwaysUseStartDate : false,
									isShowClear : false
								});
					}
				}
    });
    
//    dteStartWin.onClick(function() {
//    	WdatePicker({
//            // 时间格式
//            startDate : '%y-%M-%d',
//            dateFmt : 'yyyy-MM-dd',
//            alwaysUseStartDate : false,
//            isShowClear : false
//        });
//    });
    
    // 结束日期
    var dteEndWin = new Ext.form.TextField({
        readOnly : true,
        checked : true,
        fieldLabel : '结束日期<font color ="red">*</font>',
        anchor : '100%',
        style : 'cursor:pointer',
        	listeners : {
					focus : function() {
						WdatePicker({
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd',
									alwaysUseStartDate : false,
									isShowClear : false
								});
					}
				}
    });
    
//    dteEndWin.onClick(function() {
//    	WdatePicker({
//            // 时间格式
//            startDate : '%y-%M-%d',
//            dateFmt : 'yyyy-MM-dd',
//            alwaysUseStartDate : false,
//            isShowClear : false
//        });
//    });
//    
    // 备注
    var txaMemoWin = new Ext.form.TextArea({
        fieldLabel : '备注',
        anchor : '99%',
        height : 70,
        maxLength : 127,
        name : ''
    });
    // 附件上传一览panel
    // 右边panel
    var formInfoWin = new Ext.FormPanel({
        layout : 'form',
        border : false,
        labelAlign : 'right',
        labelWidth : 65,
        frame : false,
        style : "padding-top:5px;padding-right:5px;padding-bottom:5px;margin-bottom:0px",
        items : [txtContactFormWin, cmbTermWin, txtDeptAWin, txtDeptAWinHidden, txtAddressWin, dteSignWin, dteStartWin, dteEndWin,
            txaMemoWin]
    });
    // 附件控件+gird
    // 附件

    /**
     * 判断是否是正确的文件路径
     */
    function checkFilePath(filePath) {
        if (!filePath)
            return false;
        return /^(\w:|\\\\)/.test(String.escape(filePath));
    }

    // 人员信息grid
    var cmQuestFileWin = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
            header : "行号",
            width : 35
        }), {
            header : "姓名",
            sortable : true,
            width : 65,
            dataIndex : 'empName'
        }, {
            hidden : true,
            dataIndex : 'empId'
        }, {
            header : "所属部门",
            sortable : true,
            width : 65,
            dataIndex : 'deptName'
        }, {
            hidden : true,
            dataIndex : 'deptId'
        }, {
            header : "岗位",
            sortable : true,
            width : 65,
            dataIndex : 'stationName'
        }, {
            hidden : true,
            dataIndex : 'stationId'
        }, {
            header : "合同编码<font color ='red'>*</font>",
            sortable : true,
            width : 65,
            dataIndex : 'wrokContractNo',
            editor : new Ext.form.CodeField({
                maxLength : 6
            })
        }, {
            header : "附件",
            sortable : true,
            width : 65,
            dataIndex : 'workcontractid',
            renderer : function(v, cellmeta, record) {
                if (v != null && v != '') {
                    var workcontractid = record.get("workcontractid");
                    var strfileOriger = record.get("contractContinueMark");
			    	if (0 == strfileOriger) {
			    		fileOriger = NEW_SIGN;
			    	} else if (1 == strfileOriger) {
			    		fileOriger = CONTINUE_SIGN;
			    	}
                    var showWindow = 'showWindow("' + workcontractid + '","' + fileOriger + '");return false;';
                    return "<a href='#' onclick='" + showWindow + "'>查看附件</a>";
                } else {
                    return "—"
                }
            }
        }]);
    // 定义选择列
    var smQuestFileWin = new Ext.grid.RowSelectionModel({
        singleSelect : true
    });

    // 数据源--------------------------------
    var recordWorker = Ext.data.Record.create([{
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
            name : 'workcontractid'
        }, {
            name : 'contractContinueMark'
        }]);

    // 定义格式化数据
    var readWorker = new Ext.data.JsonReader({
        root : "list",
        totalProperty : "totalCount"
    }, recordWorker);

    // 定义封装缓存数据的对象
    var storeWorker = new Ext.data.Store({
        // 处理数据的对象
        reader : readWorker
    });
    var gridQuestFileWin = new Ext.grid.EditorGridPanel({
        store : storeWorker,
        cm : cmQuestFileWin,
        sm : smQuestFileWin,
        autoSizeColumns : true,
        clicksToEdit : 1,
        enableColumnMove : false
    })
    // 左边的panel
    var leftPanelWin = new Ext.Panel({
        region : 'west',
        layout : 'fit',
        width : '55%',
        anchor : '100%',
        border : false,
        items : [gridQuestFileWin]
    });
    // 右边的panel
    var rightPanelWin = new Ext.Panel({
        region : "center",
        border : false,
        layout : 'fit',
        style : "padding-top:0px;padding-right:0px;padding-bottom:0px;margin-bottom:0px;color:#000000;",
        items : [formInfoWin]
    });
    // 整个页面的panel
    var allPanelWin = new Ext.Panel({
        layout : 'border',
        tbar : tbrButtonWin,
        border : false,
        items : [leftPanelWin, rightPanelWin]
    });
    var win = new Ext.Window({
        border : false,
        title : '合同登录批处理',
        width : 500,
        height : 320,
        layout : 'fit',
        closeAction : 'hide',
        modal : true,
        resizable : false,
        items : [allPanelWin]
    });
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
        items : [treeEmployee]
    });
    // 右边的panel
    var rightPanel = new Ext.Panel({
        layout : 'form',
        frame : false,
        width : 580,
        height : 540,
        border : false,
        fileUpload : true,
        items : [changeInfoFs]
    });
    // 总的panel
    var fullPanel = new Ext.Panel({
        layout : 'border',
        border : false,
        tbar : tbrButton,
        defaults : {
            autoScroll : true
        },
        items : [leftPanel, {
                region : 'center',
                layout : 'form',
                frame : false,
                border : true,
                autoScroll : true,
                items : [rightPanel]
            }]
    });

    // 设定布局器及面板
    new Ext.Viewport({
        layout : "fit",
        defaults : {
            autoScroll : true
        },
        items : [fullPanel]
    });

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
        document.all.blankFrame.src = "hr/downloadContractAppendFile.action?fileId=" + id;
    }

    /**
     * 设置Form是否可以编辑
     */
    function setDisabled(form, argFlag) {
        form.getForm().clearInvalid();
        form.getForm().items.each(function(f) {
            var xtype = f.getXType();
            if (f.el.dom
            && (xtype == 'textfield' || xtype == 'numfield' || xtype == 'textarea' || xtype == 'radio'
            || xtype == 'combo' || xtype == 'combotree' || xtype == 'CmbHRCode') || xtype == 'CodeField') {
                f.setDisabled(!argFlag);
            }
        });
        // 下面是画面上总不可编辑的控件
        txtName.setDisabled(true);
        txtContactForm.setDisabled(true);
        txtDeptOf.setDisabled(true);
        txtStation.setDisabled(true);
        txtContactFormWin.setDisabled(true);
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
     * 删除一行员工
     */
    function deleteWorker() {
        var rec = gridQuestFileWin.getSelectionModel().getSelected();
        // 如果有选中行
        if (rec) {
            storeWorker.remove(rec);
            // 如果gird没有数据，保存合同禁用
            if (storeWorker.getCount() <= 0) {
                btnSignWorker.setDisabled(true);
            }
            // 刷新grid
            gridQuestFileWin.getView().refresh();
        } else {
            Ext.Msg.alert(Constants.REMIND, Constants.COM_I_001);
        }
    }

    /**
     * 批处理保存劳动合同
     */
    function batSignContract() {

        var records = new Array();
        // 循环收集gird值
        for (index = 0; index < storeWorker.getCount(); index++) {
            var record = storeWorker.getAt(index).data;
            records.push(cloneLocationRecord(record))
        }
        // 发送ajax请求，保存劳动合同
        Ext.Ajax.request({
            url : 'hr/saveContractBat.action',
            method : Constants.POST,
            params : {
                // 员工ID
                workers : Ext.util.JSON.encode(records),
                'bean.contractTermId' : cmbTermWin.getValue(),
                'bean.fristDepId' : txtDeptAWinHidden.getValue(),
                'bean.fristAddrest' : txtAddressWin.getValue(),
                'bean.workSignDate' : dteSignWin.getValue(),
                'bean.startDate' : dteStartWin.getValue(),
                'bean.endDate' : dteEndWin.getValue(),
                'bean.memo' : txaMemoWin.getValue()
            },
            success : function(action) {
                var o = eval("(" + action.responseText + ")");
                if (o.msg == "S") {
                    // 成功
                    Ext.Msg.alert(Constants.REMIND, Constants.COM_I_004);
                    saveBatSuccess();
                    // 排他
                } else if (o.msg == Constants.DATA_USING) {
                    Ext.Msg.alert(Constants.ERROR, Constants.COM_E_015);
                    return;
                    // Sql错误
                } else if (o.msg == Constants.SQL_FAILURE) {
                    Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                    return;
                    // IO错误
                } else if (o.msg == Constants.IO_FAILURE) {
                    Ext.Msg.alert(Constants.ERROR, Constants.COM_E_022);
                    return;
                    // 数据格式化错误
                } else if (o.msg == Constants.DATE_FAILURE) {
                    Ext.Msg.alert(Constants.ERROR, Constants.COM_E_023);
                    return;
                } else if (o.msg == "X") {
                    var msgName = "";
                    for (i = 0; i < o.empName.length; i++) {
                        for (index = 0; index < storeWorker.getCount(); index++) {
                            if (o.empName[i] == storeWorker.getAt(index).data['empId']) {
                                msgName += String.format(Constants.PD016_E_001,
                                storeWorker.getAt(index).data['empName'])
                                + "<br />"
                            }
                        }
                    }
                    Ext.Msg.alert(Constants.ERROR, msgName);
                    return;
                }
            }
        });
    }
    /**
     * 复制需要传到后台的值
     */
    function cloneLocationRecord(record) {
        var objClone = new Object();
        objClone['empId'] = record['empId'];
        objClone['deptId'] = record['deptId'];
        objClone['stationId'] = record['stationId'];
        objClone['wrokContractNo'] = record['wrokContractNo'];
        return objClone;
    }
    /**
     * 批处理保存成功后
     */
    function saveBatSuccess() {
        var records = new Array();
        // 循环收集gird值
        for (index = 0; index < storeWorker.getCount(); index++) {
            var record = storeWorker.getAt(index).data;
            records.push(cloneLocationRecord(record))
        }
        Ext.Ajax.request({
            url : 'hr/getContractByEmpIds.action',
            method : Constants.POST,
            params : {
                // 员工ID
                workers : Ext.util.JSON.encode(records)
            },
            success : function(action) {
                var result = eval("(" + action.responseText + ")");
                if (result.msg != undefined && result.msg == Constants.SQL_FAILURE) {
                    // 失败时，弹出提示
                    Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                } else {
                    storeWorker.loadData(result);
                    setDisabled(formInfoWin, false);
                    btnDeleteWorker.setDisabled(true);
                    btnAddWorker.setDisabled(true);
                    btnSignWorker.setDisabled(true);
                    // 禁用grid的编辑事件
                    gridQuestFileWin.on('beforeedit', function(obj) {
                        obj.cancel = true
                    })

                }
            }
        });
    }

    /**
     * 主画面不为空判断
     */
    function checkNullMain() {
        var msg = ""
        if (txtContractNo.getValue() == "") {
            msg += String.format(Constants.COM_E_002, '合同编码') + "<br />";
        }
        if ((cmbTerm.getValue() == "")
        	|| (cmbTerm.getValue() == null)) {
            msg += String.format(Constants.COM_E_003, '有效期') + "<br />";
        }
        if (txtDeptA.getValue() == "") {
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
     * 批处理不为空的控件判断
     */
    function checkNullWin() {
        var msg = ""
        for (index = 0; index < storeWorker.getCount(); index++) {
            if (storeWorker.getAt(index).data['wrokContractNo'] == "") {
                msg += String.format(Constants.COM_E_017, "员工", storeWorker.getAt(index).data['empName'], "合同编码")
                + "<br />";
            }
        }
        if ((cmbTermWin.getValue() == "")
        	|| (cmbTermWin.getValue() == null)) {
            msg += String.format(Constants.COM_E_003, '有效期') + "<br />";
        }
        if (txtDeptAWin.getValue() == "") {
            msg += String.format(Constants.COM_E_003, '甲方部门') + "<br />";
        }
        if (dteSignWin.getValue() == "") {
            msg += String.format(Constants.COM_E_003, '签字日期') + "<br />";
        }
        if (dteStartWin.getValue() == "") {
            msg += String.format(Constants.COM_E_003, '开始日期') + "<br />";
        }
        if (dteEndWin.getValue() == "") {
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
                Ext.Msg.alert(Constants.ERROR, String.format(Constants.COM_E_005, "结束日期", "开始日期"));
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
     * 选择部门
     */
    function deptSelect(flag) {
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
			if (typeof(object.names) != "undefined") {
				if(flag == "M") {
					txtDeptA.setValue(object.names);
				} else if(flag == "S") {
					txtDeptAWin.setValue(object.names);
				}
			}
			if (typeof(object.ids) != "undefined") {
				if(flag == "M") {
					txtDeptAHidden.setValue(object.ids);
				} else if(flag == "S") {
					txtDeptAWinHidden.setValue(object.ids);
				}
			}
		}
    }
    
    setDisabled(formInfo, false);
    gridQuestFile.disable();
    gridQuestFile.clearAppendFileValue();
});
