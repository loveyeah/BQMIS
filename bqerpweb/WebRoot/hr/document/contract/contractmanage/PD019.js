Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {

    Ext.form.TextField.prototype.width = 180;
    Ext.form.TextField.prototype.disabled = true;

    // 合同ID
    var cotractId = null;

    // 终止劳动合同id
    var contractId = '';
    // 附件来源
	var fileOriger = '3';

    var empName = '';
    var empId = '';
    // bug edit by fangjihu
    var changeDateTxtCopy="";
    var endInfoTypeCopy="";
    var reasonLiftCopy="";
    var fileGoWhereCopy="";
    var relationInsureCopy="";
    var txtAreaEndMemoCopy="";
    // bug edit by fangjihu

    // 合同终止按钮
    var contractChangeBtn = new Ext.Button({
        text : '合同终止',
        disabled : true,
        iconCls : Constants.CLS_CONTRACT_END,
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
            name : 'workcontractid'
        }, {
            name : 'workContractNo'
        }, {
            name : 'contractTerm'
        }, {
            name : 'fristDept'
        }, {
            name : 'fristAddrest'
        }, {
            name : 'chsName'
        }, {
            name : 'contractContinueMark'
        }, {
            name : 'suoDept'
        }, {
            name : 'stationName'
        }, {
            name : 'signDate'
        }, {
            name : 'startDate'
        }, {
            name : 'endDate'
        }, {
            name : 'ifExecute'
        }, {
            name : 'empId'
        }, {
            name : 'deptId'
        }, {
            name : 'stationId'
        }, {
            name : 'isUse'
        }, {
            name : 'empInfoStation'
        }, {
            name : 'empInfoDept'
        },{
        	name : 'contractTermId'
        }]);

    // 合同信息store
    var contractInfoStore = new Ext.data.JsonStore({
        url : 'hr/findByEmpId.action',
        root : 'list',
        fields : contractData
    });

    // 终止信息数据
    var contractEndData = new Ext.data.Record.create([{
            name : 'contractstopid'
        }, {
            name : 'workcontractid'
        }, {
            name : 'realEndTime'
        }, {
            name : 'contractStopType'
        }, {
            name : 'workContractNo'
        }, {
            name : 'releaseReason'
        }, {
            name : 'startDate'
        }, {
            name : 'endDate'
        }, {
            name : 'contractTermCode'
        }, {
            name : 'dossierDirection'
        }, {
            name : 'societyInsuranceDirection'
        }, {
            name : 'memo'
        }, {
            name : 'insertby'
        }, {
            name : 'insertdate'
        }, {
            name : 'isUse'
        }, {
            name : 'lastModifiedBy'
        }, {
            name : 'lastModifiedDate'
        }, {
            name : 'empId'
        }, {
            name : 'deptId'
        }, {
            name : 'stationId'
        }, {
        	// add by ywliu 20100611
            name : 'contractTerminatedCode'
        }])

    // 终止信息store
    var changeInfoStore = new Ext.data.JsonStore({
        url : 'hr/findByContractId.action',
        root : 'list',
        fields : contractEndData
    });
    changeInfoStore.on('load',function(){
    contractId  = changeInfoStore.getAt(0).get("contractstopid");
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

    // 是否使用
    var isUseTxt = new Ext.form.Hidden({
        id : 'isUse',
        value : ''
    });

    // 合同编码
    var contractNoTxt = new Ext.form.TextField({
        id : 'workContractNo',
        fieldLabel : '合同编码'
    });

    // 合同有效期
    var contractTermCmb = new Ext.form.TextField({
        id : 'contractTerm',
        fieldLabel : '有效期',
        triggerAction : 'all',
        disabled : true,
        anchor : '98%',
        readOnly : true,
        mode : 'local',
        displayField : 'contractTerm',
        valueField : 'contractTermId'
    });

    // 甲方部门
    var firstDeptTxt = new Ext.form.TextField({
        id : 'fristDept',
        fieldLabel : '甲方部门',
        anchor : '99%',
        width : 453
    });

    // 甲方部门ID
    var hiddenFirstDeptTxt = new Ext.form.Hidden({
        id : 'deptId',
        value : ''
    });

    // 甲方地址
    var firstAddressTxt = new Ext.form.TextField({
        id : 'fristAddrest',
        fieldLabel : '甲方地址',
        anchor : '99%',
        width : 453
    });

    // 人员姓名
    var empNameTxt = new Ext.form.TextField({
        id : 'chsName',
        fieldLabel : '员工姓名'
    });

    // 合同形式
    var contractTypeTxt = new Ext.form.CmbHRCode({
        id : 'contractContinueMark',
        fieldLabel : '合同形式',
        triggerClass : 'noButtonCombobox',
        anchor : '98.5%',
        type : '劳动合同形式'
    });

    // 所属部门
    var deptTxt = new Ext.form.TextField({
        id : 'suoDept',
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
        anchor : '98.5%',
        fieldLabel : '岗位'
    });

    // 岗位ID
    var hiddenStationTxt = new Ext.form.Hidden({
        id : 'stationId',
        value : ''
    });

    // 签字日期
    var signDateTxt = new Ext.form.TextField({
        id : 'signDate',
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
        anchor : '99%',
        fieldLabel : '结束日期'
    });
    // 人员基本信息表里的岗位ID
    var hiddenEmpStationTxt = new Ext.form.Hidden({
//        name : 'empInfoStation'
    	name : 'stationId'
//        value : ''
    });

    // 人员基本信息表里的部门ID
    var hiddenEmpDeptTxt = new Ext.form.Hidden({
        name : 'empInfoDept',
        value : ''
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
                items : [contractNoTxt, workContractIdTxt, lastModifiedDateTxt, empIdTxt, isUseTxt, hiddenEmpDeptTxt,
                    hiddenEmpStationTxt]
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
                        items : [contractTypeTxt]
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

    // 终止日期
    var changeDateTxt = new Ext.form.TextField({
        id : 'changeDate',
        name : 'realEndTime',
        fieldLabel : '终止日期<font color="red">*</font>',
        style : 'cursor:pointer',
        readOnly : true,
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

    // 终止类别
    var endInfoType = new Ext.form.CmbHRCode({
        id : 'contractStopType',
        width : 110,
        anchor : '99%',
        allowBlank:false,
        readOnly : true,
        fieldLabel : '终止类别<font color="red">*</font>',
        type : '劳动合同终止类别'
    });
    
    // 合同解除文号
    var terminatedCode = new Ext.form.TextField({
        id : 'terminatedCode',
        fieldLabel : '合同解除文号',
        anchor : '99%',
        maxLength : 15,
        labelAlign : 'right',
        name : 'terminatedCode'
    })

    // 解除原因
    var reasonLift = new Ext.form.TextArea({
        id : 'releaseReason',
        fieldLabel : '解除原因',
        anchor : '99%',
        maxLength : 15,
        height : 30,
        labelAlign : 'right'
    })

    // 档案转移去向
    var fileGoWhere = new Ext.form.TextField({
        id : 'dossierDirection',
        name : 'dossierDirection',
        fieldLabel : '档案转移去向',
        maxLength : 25,
        height : 30,
        labelWidth : 150,
        anchor : '99.5%',
        id : 'endGoWher'
    });

    // 社会保险关系
    var relationInsure = new Ext.form.TextField({
        id : 'societyInsuranceDirection',
        name : 'societyInsuranceDirection',
        fieldLabel : '社会保险关系转移去向',
        labelWidth : 150,
        maxLength : 25,
        height : 30,
        anchor : '99.5%',
        id : 'endRelation'
    });

    // 备注
    var txtAreaEndMemo = new Ext.form.TextArea({
        id : 'memo',
        name : 'memo',
        fieldLabel : '备注',
        anchor : '99%',
        maxLength : 127,
        labelAlign : 'right'
    })

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
     storeQuestFile.on('beforeload', function() {
        Ext.apply(this.baseParams, {
             workcontractid : contractId,
             fileOriger : fileOriger
        });
        })
    

    var gridQuestFile = new AppendFilePanel({
        // 是否可编辑
        readOnly : false,
        height : 113,
        width : 545,
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
            workcontractid : contractId,
            fileOriger : '3'

        });
    });

    // 终止信息FormPanel
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
                    items : [endInfoType]
                }]
        },
        	// 第二行 add by ywliu 20100611
            {
                xtype : 'panel',
                border : false,
                layout : 'column',
                items : [{
                        columnWidth : 1,
                        layout : 'form',
                        border : false,
                        items : [terminatedCode]
                    }]
            },
            // 第三行
            {
                xtype : 'panel',
                border : false,
                layout : 'column',
                items : [{
                        columnWidth : 1,
                        layout : 'form',
                        border : false,
                        items : [reasonLift]
                    }]
            },
            // 第四行
            {
                xtype : 'panel',
                border : false,
                layout : 'column',
                items : [{
                        columnWidth : 1,
                        layout : 'form',
                        border : false,
                        items : [fileGoWhere]
                    }]
            },
            // 第五行
            {
                xtype : 'panel',
                border : false,
                layout : 'form',
                items : [relationInsure]
            },
            // 第六行
            {
                xtype : 'panel',
                border : false,
                layout : 'form',
                items : [txtAreaEndMemo]
            }]
    });

    // 合同信息FieldSet
    var contractInfoFs = new Ext.form.FieldSet({
        title : '合同信息',
        border : true,
        style : "margin:5px;",
        items : [contractInfoForm]
    });

    // 终止信息FieldSet
    var changeInfoFs = new Ext.form.FieldSet({
        title : '终止信息',
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
        height : 600,
        border : false,
        fileUpload : true,
        items : [contractInfoFs, changeInfoFs]
    });

    // 总的panel
    var fullPanel = new Ext.Panel({
        layout : 'border',
        border : false,
        tbar : btnToolbar,
        defaults : {
            autoScroll : true
        },
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
        defaults : {
            autoScroll : true
        },
        items : [fullPanel]
    });

    // 合同附件panel不可用
    gridQuestFile.disable();

    /**
     * 合同终止处理
     */
    function contractChangeHandler() {
        if (changeDateTxt.getValue() == "") {
            changeDateTxt.setValue((new Date()).format('Y-m-d'));
            confirmBtn.setDisabled(false);
            cancelBtn.setDisabled(false);
            setChangeDisable(false);
            // bug edit by fangjihu
            changeDateTxtCopy= changeDateTxt.getValue();
            // bug edit by fangjihu 
        }
    }

    /**
     * 确认处理
     */
    function confirmHandler() {
        Ext.Msg.confirm(Constants.NOTICE_CONFIRM, Constants.PD019_C_001, function(buttonobj) {
            // 确认保存
            if (buttonobj == "yes") {
                if (!reasonLift.isValid() || !fileGoWhere.isValid() || !relationInsure.isValid()
                || !txtAreaEndMemo.isValid()) {
                    return;
                }

                // 判断开始日期，终止日期
                if (!checkDate(startDateTxt.getValue(), changeDateTxt.getValue(), "开始日期", "终止日期")) {
                    return;
                }
                if (endInfoType.getValue() == "") {
                    Ext.Msg.alert(Constants.ERROR, Constants.PD019_E_001);
                    return;
                }
                var params = {
                    contractId : contractInfoStore.getAt(0).get("workcontractid"),
                    empId : contractInfoStore.getAt(0).get("empId"),
                    startDate : contractInfoStore.getAt(0).get("startDate"),
                    contractTermCode: contractInfoStore.getAt(0).get("contractTermId"),
                    endDate : endDateTxt.getValue(),
                    deptId : hiddenEmpDeptTxt.getValue(),
                    stationId : hiddenEmpStationTxt.getValue(),
                    contractStopType : endInfoType.getValue(),
                    realEndTime : changeDateTxt.getValue(),
                    releaseReason : reasonLift.getValue(),
                    dossierDirection : fileGoWhere.getValue(),
                    societyInsuranceDirection : relationInsure.getValue(),
                    memo : txtAreaEndMemo.getValue(),
                    terminatedCode : terminatedCode.getValue() // add by ywliu 20100612
                };
                Ext.Ajax.request({
                    method : Constants.POST,
                    url : 'hr/endContract.action',
                    params : params,
                    success : function(action) {
                        var o = eval("(" + action.responseText + ")");
                        var succ = o.msg;
                        if (succ == Constants.SQL_FAILURE)
                            Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                        else if (succ == Constants.DATA_USING)
                            Ext.Msg.alert(Constants.ERROR, Constants.COM_E_015);
                        else {
                        	
                            Ext.Msg.alert(Constants.REMIND, Constants.PD019_I_001, function() {
                            changeInfoStore.load({
                            	params : {
                                	contractId : contractInfoStore.getAt(0).get("workcontractid")
                           	 	},
           				 callback : function() {
            	 				contractId  = changeInfoStore.getAt(0).get("contractstopid");
                                gridQuestFile.setDisabled(false);
                                contractChangeBtn.setDisabled(true);
                                confirmBtn.setDisabled(true);
                                cancelBtn.setDisabled(true);
                                setChangeDisable(true);
                                copyValue();
            	}
            })
                            });
                        }
                    },
                    failure : function() {
                        Ext.Msg.alert(Constants.ERROR, Constants.UNKNOWN_ERR);
                    }
                })
            }
        })
    }

    /**
     * 取消处理
     */
    function cancelHandler() {
        Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_005, function(buttonobj) {
            if (buttonobj == 'yes'){
            changeInfoForm.getForm().reset();
            changeDateTxt.setValue((new Date()).format('Y-m-d'));
            copyValue();
            }
        })
    }

    /**
     * 树的点击事件
     */
    function treeClick(node, e) {
    	 e.stopEvent();
        var str = node.text;
        empName = str.substring(0, str.indexOf("("));
        empId = node.id;
        // 叶子节点为员工
        if (node.isLeaf()) {
           if (!judgeDataIsModified()) {
                Ext.MessageBox.confirm(Constants.CONFIRM, Constants.COM_C_004, function(buttonobj) {
                    if (buttonobj == "yes") {
                        setChangeDisable(true);
                        contractChangeBtn.setDisabled(true);
                        confirmBtn.setDisabled(true);
                        cancelBtn.setDisabled(true);
                        findByEmpId(node.id)
                    }
                })
            } else {
                setChangeDisable(true);
                contractChangeBtn.setDisabled(true);
                confirmBtn.setDisabled(true);
                cancelBtn.setDisabled(true);
                findByEmpId(node.id)
            }}
    }
    // bug edit by fangjihu
    function judgeDataIsModified(){
    	if(changeDateTxt.getValue()!=changeDateTxtCopy){
    		return false;
    	}
    	if(endInfoType.getValue()!=endInfoTypeCopy){
    		return false;
    	}
    	if(reasonLift.getValue()!=reasonLiftCopy){
    		return false;
    	}
    	if(fileGoWhere.getValue()!=fileGoWhereCopy){
    		return false;
    	}
    	if(relationInsure.getValue()!=relationInsureCopy){
    		return false;
    	}
    	if(txtAreaEndMemo.getValue()!=txtAreaEndMemoCopy){
    		return false;
    	}
    	return true;
    	
    }
    function copyValue(){
    	changeDateTxtCopy= changeDateTxt.getValue();
        endInfoTypeCopy = endInfoType.getValue();
        reasonLiftCopy = reasonLift.getValue();
        fileGoWhereCopy = fileGoWhere.getValue();
        relationInsureCopy = relationInsure.getValue();
        txtAreaEndMemoCopy = txtAreaEndMemo.getValue();
    }
    function resetCopyValue(){
    	changeDateTxtCopy= "";
        endInfoTypeCopy = "";
        reasonLiftCopy = "";
        fileGoWhereCopy = "";
        relationInsureCopy = "";
        txtAreaEndMemoCopy = "";
    }
    // bug edit by fangjihu

    /**
     * 根据员工Id查找合同信息
     */
    function findByEmpId(empId) {
    	resetCopyValue();
        init();
        contractInfoForm.getForm().reset();
        changeInfoForm.getForm().reset();
        storeQuestFile.removeAll();
        gridQuestFile.clearAppendFileValue();
        contractInfoStore.load({
            params : {
                empId : empId
            },
            callback : function() {
                if (contractInfoStore.getCount() > 0) {
                    // 检索到合同信息
                    var record = contractInfoStore.getAt(0);
                    cotractId = record.get("workcontractid");
                    contractInfoForm.getForm().loadRecord(record);
                    hiddenEmpStationTxt.setValue(record.get('stationId'))
                    if (record.get("isUse") == 'N') {
                        changeInfoStore.load({
                            params : {
                                contractId : record.get("workcontractid")
                            },
                            callback : function() {
                                // 合同是否终止
                                if (changeInfoStore.getCount() > 0) {
                                    changeInfoForm.getForm().loadRecord(changeInfoStore.getAt(0));
                                    changeDateTxt.setValue(changeDateTxt.getValue().substring(0, 10));
                                    // add by ywliu 20100611
                    				terminatedCode.setValue(changeInfoStore.getAt(0).data.contractTerminatedCode);
                                    contractId  = changeInfoStore.getAt(0).data.contractstopid;
                                    // bug edit by fangjihu
                                    copyValue();
                                    // bug edit by fangjihu
                                    storeQuestFile.load({
                                        params : {
                                            workcontractid : contractId,
                                            fileOriger : '3'
                                        }
                                    });
                                    gridQuestFile.setDisabled(false);
                                }
                            }
                        })
                    } else {
                        contractChangeBtn.setDisabled(false);
                    }
                } else {
                    // 初始化
                    init();
                }
            }
        });
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
     * 下载文件
     */
    downloadFile = function(id) {
        document.all.blankFrame.src = "hr/downloadContractAppendFile.action?fileId=" + id;
    }

    /**
     * 初始化
     */
    function init() {
        initContractInfo();
        initChangeInfo();
    }

    /**
     * 初始化合同信息
     */
    function initContractInfo() {
        // 清空所有数据
        contractInfoStore.removeAll();
        // 清空form
        contractInfoForm.getForm().reset();
        // 按钮不可用
        contractChangeBtn.setDisabled(true);
        confirmBtn.setDisabled(true);
        cancelBtn.setDisabled(true);
    }

    /**
     * 初始化终止信息
     */
    function initChangeInfo() {
        // 清空所有数据
        changeInfoStore.removeAll();
        // 清空form
        changeInfoForm.getForm().reset();
        // 清空附件Panel
        gridQuestFile.clearAppendFileValue();
        // 合同附件panel不可用
        gridQuestFile.disable();
        // 变更区域控件不可用
        Ext.form.TextField.prototype.disabled = true;
    }

    /**
     * 终止信息区域可用
     */
    function setChangeDisable(flag) {
        changeDateTxt.setDisabled(flag);
        endInfoType.setDisabled(flag);
        reasonLift.setDisabled(flag);
        fileGoWhere.setDisabled(flag);
        relationInsure.setDisabled(flag);
        txtAreaEndMemo.setDisabled(flag);
        // add by ywliu 20100611
        terminatedCode.setDisabled(flag);
    }

    /**
     * 设置控件的值
     */
    function setComponentValue(component, value) {
        if (value == null) {
            value = "";
        }
        component.setValue(value, true);
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
            if (start.getTime() > end.getTime()) {
                Ext.Msg.alert(Constants.ERROR, String.format(Constants.COM_E_009, string1, string2));
                return false;
            }
            return true;
        }
    }
});
