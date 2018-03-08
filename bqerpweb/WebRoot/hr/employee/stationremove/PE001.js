// 画面：档案管理/员工档案/工种维护
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {
    // 系统当前日期
    var sysDate = new Date();
    sysDate = sysDate.format('Y-m-d');
    // 调动类别
    var moveTypeName = "";

    // 调动开始日期
    var txtStartDate = new Ext.form.TextField({
        id : 'startDate',
        name : 'startDate',
        style : 'cursor:pointer',
        width : 80,
        readOnly : true,
        listeners : {
            focus : function() {
                WdatePicker({
                    startDate : '%y-%M-%d',
                    dateFmt : 'yyyy-MM-dd',
                    alwaysUseStartDate : false,
                    isShowClear : true
                });
            }
        }
    });
    // 调动结束日期
    var txtEndDate = new Ext.form.TextField({
        id : 'endDate',
        name : 'endDate',
        style : 'cursor:pointer',
        width : 80,
        readOnly : true,
        listeners : {
            focus : function() {
                WdatePicker({
                    startDate : '%y-%M-%d',
                    dateFmt : 'yyyy-MM-dd',
                    alwaysUseStartDate : false,
                    isShowClear : true
                });
            }
        }
    });
    // 调动前班组
//    var drpOldDept = new Ext.form.CmbHRBussiness({
//        fieldLabel : '调动前班组',
//        type : "调动部门",
//        width : 120,
//        allowBlank : true,
//        id : 'drpOldDept',
//        hiddenName : 'oldDeptId'
//    })
//    // 调动后班组
//    var drpNewDept = new Ext.form.CmbHRBussiness({
//        fieldLabel : '调动前班组',
//        type : "调动部门",
//        width : 120,
//        allowBlank : true,
//        id : 'drpNewDept',
//        hiddenName : 'newDeptId'
//    })
    
    //************************************************************************************************************
    // 主页面显示Store
    var storeEmpMove = new Ext.data.JsonStore({
        fields : ['ssDeptId', 'ssDeptName', 'jdDeptId', 'jdDeptName']
    })
    storeEmpMove.insert(0, new Ext.data.Record({
        'ssDeptId' : '',
        'ssDeptName' : '',
        'jdDeptId' : '',
        'jdDeptName' : ''
    }))
    var drpOldDept = new Ext.form.ComboBox({
    	 id : 'drpOldDept',
        fieldLabel : '调动前班组',
        width : 120,
        hiddenName : 'oldDeptId',
        maxLength : 100,
        anchor : '100%',
        readOnly : true,
        store : storeEmpMove,
        displayField : "ssDeptName",
        valueField : "ssDeptId",
        triggerClass : 'noButtonCombobox',
        listeners : {
            'beforequery' : function(obj) {
                obj.cancel = true;
            }
        }
    });
    drpOldDept.onClick(function() {
        var args = {
            selectModel : 'single',
            rootNode : {
                id : '0',
                text : '灞桥电厂'
            },
            onlyLeaf : false
        };
        this.blur();
        var dept = window.showModalDialog('../../../comm/jsp/hr/dept/dept.jsp', args, 'dialogWidth:'
        + Constants.WIDTH_COM_DEPT + 'px;dialogHeight:' + Constants.HEIGHT_COM_DEPT
        + 'px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
        if (typeof(dept) != "undefined") {
            storeEmpMove.getAt(0).data['ssDeptId'] = dept.ids;
            storeEmpMove.getAt(0).data['ssDeptName'] = dept.names;
            drpOldDept.setValue(dept.ids);
        }
    });
    // 调动后班组
    var drpNewDept = new Ext.form.ComboBox({
    	id : 'drpNewDept',
        fieldLabel : '调动后班组',
        width : 120,
        hiddenName : 'newDeptId',
        maxLength : 100,
        anchor : '100%',
        readOnly : true,
        store : storeEmpMove,
        displayField : "jdDeptName",
        valueField : "jdDeptId",
        triggerClass : 'noButtonCombobox',
        listeners : {
            'beforequery' : function(obj) {
                obj.cancel = true;
            }
        }
    });

    // 借调部门的onClick事件
    drpNewDept.onClick(function() {
        var args = {
            selectModel : 'single',
            rootNode : {
                id : '0',
                text : '灞桥电厂'
            },
            onlyLeaf : false
        };
        this.blur();
        var dept = window.showModalDialog('../../../comm/jsp/hr/dept/dept.jsp', args, 'dialogWidth:'
        + Constants.WIDTH_COM_DEPT + 'px;dialogHeight:' + Constants.HEIGHT_COM_DEPT
        + 'px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
        if (typeof(dept) != "undefined") {
            storeEmpMove.getAt(0).data['jdDeptId'] = dept.ids;
            storeEmpMove.getAt(0).data['jdDeptName'] = dept.names;
            drpNewDept.setValue(dept.ids);
        }
    })
    //************************************************************************************************************
    
    // 查询按钮
    var queryBtn = new Ext.Button({
        id : 'query',
        text : Constants.BTN_QUERY,
        iconCls : Constants.CLS_QUERY,
        handler : queryRecord
    });
    // 新增按钮
    var addBtn = new Ext.Button({
        id : 'add',
        text : Constants.BTN_ADD,
        iconCls : Constants.CLS_ADD,
        handler : addRecord
    });
    // 修改按钮
    var updateBtn = new Ext.Button({
        id : 'update',
        text : Constants.BTN_UPDATE,
        iconCls : Constants.CLS_UPDATE,
        handler : updateRecord
    });
    var headTbar = new Ext.Toolbar({
        region : 'north',
        border : false,
        height : 25,
        items : ["调动日期:", txtStartDate, "~", txtEndDate, "-", "调动前班组:", drpOldDept, "调动后班组:", drpNewDept, "-",
            queryBtn, addBtn, updateBtn]
    });

    // grid中的数据 人员姓名,调动日期,调动前班组名称,调动后班组名称,执行日期,
    // 备注,上次修改日期,人员ID,岗位调动类别,岗位调动单ID
    var runGridList = new Ext.data.Record.create([{
            name : "empName"
        }, {
            name : "removeDate"
        }, {
            name : "oldDepName"
        }, {
            name : "newDepName"
        }, {
            name : "doDate"
        }, {
            name : "memo"
        }, {
            name : "lastModifiedDate"
        }, {
            name : "empId"
        }, {
            name : "empCode"
        }, {
            name : "stationMoveTypeName"
        }, {
            name : "stationremoveid"
        }]);

    // grid中的store
    var runGridStore = new Ext.data.Store({
        proxy : new Ext.data.HttpProxy({
            url : 'hr/getStationRemoveInfoList.action'
        }),
        reader : new Ext.data.JsonReader({
            root : 'list',
            totalProperty : 'totalCount'
        }, runGridList)
    });
    // runGridStore.setDefaultSort('stationremoveid', 'ASC');
    runGridStore.baseParams = {
        startDate : txtStartDate.getValue(),
        endDate : txtEndDate.getValue(),
        oldDeptId : drpOldDept.getValue(),
        newDeptId : drpNewDept.getValue()
    };
    // 初始化时,显示数据
    runGridStore.load({
        params : {
            start : 0,
            limit : Constants.PAGE_SIZE
        }
    });
    // 是否是查询操作
    var queryFlag = false;
    runGridStore.on("load", function() {
        if (runGridStore.getCount() == 0) {
            // 没有检索到任何信息(仅在查询处理后显示)
            if (queryFlag) {
                Ext.Msg.alert(Constants.REMIND, Constants.COM_I_003);
            }
        }
    });

    // 运行执行的Grid主体 (员工姓名,调动日期,调动前班组名称,调动后班组名称,执行日期,
    // 备注,上次修改日期,人员ID,岗位调动类别,岗位调动单ID)
    var runGrid = new Ext.grid.GridPanel({
        store : runGridStore,
        columns : [
            // 自动生成行号
            new Ext.grid.RowNumberer({
                header : '行号',
                width : 35
            }), {
                header : '员工姓名',
                width : 100,
                align : 'left',
                sortable : true,
                dataIndex : 'empName'
            }, {
                header : '调动日期',
                width : 100,
                align : 'left',
                sortable : true,
                dataIndex : 'removeDate'
            }, {
                header : '调动前班组',
                width : 150,
                align : 'left',
                sortable : true,
                dataIndex : 'oldDepName'
            }, {
                header : '调动后班组',
                width : 150,
                align : 'left',
                sortable : true,
                dataIndex : 'newDepName'
            }, {
                header : '执行日期',
                width : 100,
                align : 'left',
                sortable : true,
                dataIndex : 'doDate'
            }, {
                header : '备注',
                width : 200,
                align : 'left',
                sortable : true,
                dataIndex : 'memo'
            }, {
                header : '岗位调动单ID',
                dataIndex : 'stationremoveid',
                hidden : true
            }, {
                header : '岗位调动类别',
                dataIndex : 'stationMoveTypeName',
                hidden : true
            }, {
                header : '人员ID',
                dataIndex : 'empId',
                hidden : true
            }, {
                header : '上次修改日期',
                dataIndex : 'lastModifiedDate',
                hidden : true
            }],
        viewConfig : {
            forceFit : false
        },
        tbar : headTbar,
        // 分页
        bbar : new Ext.PagingToolbar({
            pageSize : Constants.PAGE_SIZE,
            store : runGridStore,
            displayInfo : true,
            displayMsg : Constants.DISPLAY_MSG,
            emptyMsg : Constants.EMPTY_MSG
        }),
        sm : new Ext.grid.RowSelectionModel({
            singleSelect : true
        }),
        frame : false,
        border : false,
        enableColumnHide : true,
        enableColumnMove : false
    });

    // 注册双击事件
    runGrid.on("rowdblclick", updateRecord);
    // runGrid.on('celldblclick',cellDbClick);
    // 备注-弹出窗口
    var memoText = new Ext.form.TextArea({
        id : "memoText",
        maxLength : 127,
        readOnly : true,
        width : 180
    });
    // 弹出画面
    var memoWin = new Ext.Window({
        height : 170,
        width : 350,
        layout : 'fit',
        resizable : false,
        modal : true,
        closeAction : 'hide',
        items : [memoText],
        buttonAlign : "center",
        title : '详细信息查看窗口',
        buttons : [{
                text : Constants.BTN_CLOSE,
                iconCls : Constants.CLS_CANCEL,
                handler : function() {
                    memoWin.hide();
                }
            }]
    });
    // 设定布局器及面板
    new Ext.Viewport({
        enableTabScroll : true,
        layout : "border",
        items : [{
                xtype : "panel",
                region : 'center',
                layout : 'fit',
                border : false,
                items : [runGrid]
            }]
    });

    // 弹出窗口项目
    var THEWIDTH = 180;
    // 定义form中的调动前班组
    // 子页面显示Store
    var beforeStore = new Ext.data.JsonStore({
        fields : ['beforeDeptId', 'beforeDeptName']
    })
    beforeStore.insert(0, new Ext.data.Record({
        'beforeDeptId' : '',
        'beforeDeptName' : ''
    }))
    var drpSubOldDept = new Ext.form.ComboBox({
        fieldLabel : '调动前班组<font color ="red">*</font>',
        valueField : 'id',
        width : THEWIDTH,
        readOnly : true,
        allowBlank : false,
        store : beforeStore,
        id : 'oldDepName',
        displayField : "beforeDeptName",
        valueField : "beforeDeptId",
        triggerClass : 'noButtonCombobox',
        listeners : {
            'beforequery' : function(obj) {
                obj.cancel = true;
            }
        }
    });
    drpSubOldDept.onClick(function() {
        var args = {
            selectModel : 'single',
            rootNode : {
                id : '0',
                text : '灞桥电厂'
            },
            onlyLeaf : false
        };
        this.blur();
        var dept = window.showModalDialog('../../../comm/jsp/hr/dept/dept.jsp', args, 'dialogWidth:'
        + Constants.WIDTH_COM_DEPT + 'px;dialogHeight:' + Constants.HEIGHT_COM_DEPT
        + 'px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
        if (typeof(dept) != "undefined") {
            beforeStore.getAt(0).data['beforeDeptId'] = dept.ids;
            beforeStore.getAt(0).data['beforeDeptName'] = dept.names;
            drpSubOldDept.setValue(dept.ids);
            drpSubOldDeptSelected();
        }
    })

//    var drpSubOldDept = new Ext.form.CmbHRBussiness({
//        fieldLabel : '调动前班组<font color ="red">*</font>',
//        type : "调动部门",
//        width : THEWIDTH,
//        allowBlank : false,
//        id : 'oldDepName',
//        listeners : {
//            select : drpSubOldDeptSelected
//        }
//    })
    // hidSubOldDept, hidSubNewDept, hidEmpId,
    // hidLastModifiedDate, hidStationremoveid
    var hidSubOldDept = {
        id : "hidSubOldDept",
        xtype : "hidden",
        name : "stationRemoveBeen.oldDepId"
    }
    var hidSubNewDept = {
        id : "hidSubNewDept",
        xtype : "hidden",
        name : "stationRemoveBeen.newDepId"
    }
    var hidEmpId = {
        id : "hidEmpId",
        xtype : "hidden",
        name : "stationRemoveBeen.empId"
    }
    var hidLastModifiedDate = {
        id : "lastModifiedDate",
        xtype : "hidden",
        name : "stationRemoveBeen.lastModifiedDate"
    }
    var hidStationremoveid = {
        id : "stationremoveid",
        xtype : "hidden",
        name : "stationRemoveBeen.stationremoveid"
    }
    // 定义form中的调动后班组
    var afterStore = new Ext.data.JsonStore({
        fields : ['afterDeptId', 'afterDeptName']
    })
    afterStore.insert(0, new Ext.data.Record({
        'afterDeptId' : '',
        'afterDeptName' : ''
    }))
    var drpSubNewDept = new Ext.form.ComboBox({
        fieldLabel : '调动后班组<font color ="red">*</font>',
        id : 'newDepName',
        valueField : 'id',
        width : THEWIDTH,
        readOnly : true,
        allowBlank : false,
        store : afterStore,
        displayField : "afterDeptName",
        valueField : "afterDeptId",
        triggerClass : 'noButtonCombobox',
        listeners : {
            'beforequery' : function(obj) {
                obj.cancel = true;
            }
        }
    });
    drpSubNewDept.onClick(function() {
        var args = {
            selectModel : 'single',
            rootNode : {
                id : '0',
                text : '灞桥电厂'
            },
            onlyLeaf : false
        };
        this.blur();
        var dept = window.showModalDialog('../../../comm/jsp/hr/dept/dept.jsp', args, 'dialogWidth:'
        + Constants.WIDTH_COM_DEPT + 'px;dialogHeight:' + Constants.HEIGHT_COM_DEPT
        + 'px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
        if (typeof(dept) != "undefined") {
            afterStore.getAt(0).data['afterDeptId'] = dept.ids;
            afterStore.getAt(0).data['afterDeptName'] = dept.names;
            drpSubNewDept.setValue(dept.ids);
            Ext.get("hidSubNewDept").dom.value = drpSubNewDept.getValue();
           
           CmbAStation.setValue("");
                    CmbAStationLevel.setValue("");
                    storeAStation.load({
                        params : {
                            deptId : dept.ids
                        }
                    })
        }
    })
//    var drpSubNewDept = new Ext.form.CmbHRBussiness({
//        fieldLabel : '调动后班组<font color ="red">*</font>',
//        type : "调动部门",
//        width : THEWIDTH,
//        id : 'newDepName',
//        allowBlank : false,
//        listeners : {
//            select : function() {
//                Ext.get("hidSubNewDept").dom.value = drpSubNewDept.getValue();
//            }
//        }
//    })
    // 定义form中的员工姓名: 与调动前班组ID联动
    // 员工姓名下拉框数据源
    var empNameStore = new Ext.data.JsonStore({
        url : 'hr/getEmpNameListByDepId.action',
        root : 'list',
        fields : [{
                name : 'chsName'
            }, {
                name : 'empId'
            }, {
                name : 'empCode'
            }]
    });

    var drpWorkerName = new Ext.form.ComboBox({
        fieldLabel : '员工姓名<font color ="red">*</font>',
        width : THEWIDTH,
        id : 'empName',
        allowBlank : false,
        triggerAction : 'all',
        store : empNameStore,
        displayField : 'chsName',
        valueField : 'empId',
        mode : 'local',
        readOnly : true,
        listeners : {
            select : drpWorkerNameSelected
        }
    })
    // 定义form中的员工工号: 动态加载
    var txtWorkerId = new Ext.form.TextField({
        id : "empCode",
        fieldLabel : '员工工号',
        width : THEWIDTH,
        disabled : true
    })
    var storeMove = new Ext.data.JsonStore({
        root : 'list',
        url : "hr/getStationMoveTypeList.action",
        fields : ['stationMoveTypeId', 'stationMoveType']
    })
    storeMove.load();
    storeMove.on("load", function() {
        if (storeMove.getCount() > 0) {
            var index = -1;
            index = storeMove.find("stationMoveTypeId", '0');
            if (index > -1) {
                // 通过id取出name，然后设值
                var textName = storeMove.getAt(index).get('stationMoveType');
                moveTypeName = textName;
            }
        }
    });

    // 定义form中的调动类别: 动态加载
    var txtRemoveType = new Ext.form.TextField({
        id : "stationMoveTypeName",
        fieldLabel : '调动类别',
        name : '班组间',
        // TODO
        // value : "班组间",
        width : THEWIDTH,
        disabled : true
    })
    // 定义form中的调动日期
    var txtRemoveDate = new Ext.form.TextField({
        fieldLabel : '调动日期',
        id : 'removeDate',
        name : 'stationRemoveBeen.removeDate',
        style : 'cursor:pointer',
        value : sysDate,
        width : THEWIDTH,
        readOnly : true,
        listeners : {
            focus : function() {
                WdatePicker({
                    startDate : '%y-%M-%d',
                    dateFmt : 'yyyy-MM-dd',
                    alwaysUseStartDate : false,
                    isShowClear : true
                });
            }
        }
    });
    // 定义form中的执行日期
    var txtDoDate = new Ext.form.TextField({
        fieldLabel : '执行日期',
        id : 'doDate',
        name : 'stationRemoveBeen.doDate',
        style : 'cursor:pointer',
        width : THEWIDTH,
        readOnly : true,
        listeners : {
            focus : function() {
                WdatePicker({
                    startDate : '%y-%M-%d',
                    dateFmt : 'yyyy-MM-dd',
                    alwaysUseStartDate : false,
                    isShowClear : true
                });
            }
        }
    });
    // 备注
    var txaMemo = new Ext.form.TextArea({
        id : "memo",
        name : "stationRemoveBeen.memo",
        fieldLabel : "备注",
        maxLength : 127,
        height : 57,
        anchor : '90%',
        width : THEWIDTH - 2.5
    });
    // 定义form中的addFlag:新增：1 修改：0
    var addFlag = {
        id : "addFlag",
        xtype : "hidden",
        value : "",
        name : "addFlag"
    }
    
    // *********************************************************************************
    // 调动前岗位
//    var textBStationName = new Ext.form.ComboBox({
//        fieldLabel : '调动前岗位',
//        width : 125,
//        store : storeSubMove,
//        name : "bStationCode",
//        displayField : "bStationName",
//        valueField : "bStationCode",
//        triggerClass : 'noButtonCombobox',
//        disabled : true,
//        listeners : {
//            'beforequery' : function(obj) {
//                obj.cancel = true;
//            }
//        }
//    });
   
    // 调整前岗位
    var beforeStationName = new Ext.form.TextField({
    	id : 'bStationCode',
    	name : 'bStationCode',
    	disabled : true,
    	width : THEWIDTH,
    	fieldLabel : '调整前岗位'
    })
    var hidBeforeStationId = {
        id : "hidBeforeStationId",
        xtype : "hidden",
        name : "stationRemoveBeen.oldStationId"
    }
    // 调动后岗位tore
    var storeAStation = new Ext.data.JsonStore({
        root : 'list',
        url : "hr/linkDeptStation.action",
        fields : ['stationId', 'stationCode', 'stationName']
    })
    var contractTermData = Ext.data.Record.create([{
            // 岗位id
            name : 'stationId'
        }, {
            // 岗位Code
            name : 'stationCode'
        }, {
            // 岗位名称
            name : 'stationName'
        }]);
    storeAStation.on('load', function() {
        var record = new contractTermData({
            stationId : "",
            stationCode : "",
            stationName : ""
        })
        storeAStation.insert(0, record);
        if (storeAStation.getCount() > 1) {
            CmbAStation.setValue(storeAStation.getAt(1).get('stationId'));
            storeAStationLevel.load({
                params : {
                    stationId : storeAStation.getAt(1).get('stationId')
                }
            });
        } else
            CmbAStation.setValue(storeAStation.getAt(0).get('stationId'));

    })
    // 调动后岗位
    var CmbAStation = new Ext.form.ComboBox({
        fieldLabel : '调动后岗位<font color ="red">*</font>',
        width : THEWIDTH, 
        store : storeAStation,
        name : "stationId",
        displayField : "stationName",
        valueField : "stationId",
        hiddenName : 'stationRemoveBeen.newStationId',
        mode : 'local',
        triggerAction : 'all',
        readOnly : true,
        listeners : {
            select : function() {
                if (CmbAStation.getValue() != "") {
                    CmbAStationLevel.setValue("");
                    storeAStationLevel.load({
                        params : {
                            stationId : CmbAStation.getValue()
                        }
                    })
                }else if(CmbAStation.getValue() == "" ) {
                	CmbAStationLevel.setValue("");
                }
            }
        }
    });
    // 调动前岗位级别
    var textBStationLevelName = new Ext.form.TextField({
        fieldLabel : '调动前岗位级别',
        width : THEWIDTH,
        name : "textBStationLevelName",
        disabled : true,
        id : 'textBStationLevelName'
    });
    // 调动后岗位级别store
    var storeAStationLevel = new Ext.data.JsonStore({
        root : 'list',
        url : "hr/linkStationLevel.action",
        fields : ['stationLevelId', 'stationLevelName']
    })
    var aStationLevelRecord = Ext.data.Record.create([{
            // 岗位级别id
            name : 'stationLevelId'
        }, {
            // 岗位级别名称
            name : 'stationLevelName'
        }]);
    storeAStationLevel.on('load', function() {
        if (storeAStationLevel.getCount() > 0)
            CmbAStationLevel.setValue(storeAStationLevel.getAt(0).get('stationLevelId'));
	})
    // 调动后岗位级别
    var CmbAStationLevel = new Ext.form.ComboBox({
        fieldLabel : '调动后岗位级别',
       	width : THEWIDTH,
        store : storeAStationLevel,
        displayField : "stationLevelName",
        valueField : "stationLevelId",
        mode : 'local',
        triggerAction : 'all',
        disabled : true,
        triggerClass : 'noButtonCombobox',
        listeners : {
            'beforequery' : function(obj) {
                obj.cancel = true;
            }
        }
    });
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
	// 调整前薪级
	var txtOldSalaryG = new Ext.form.ComboBox({
		fieldLabel : '调整前薪级',
		name : 'oldSalaryGrade',
		id : 'oldSalaryGrade',
		store : salaryLevelStore,
		valueField : "id",
		displayField : "text",
		mode : 'local',
		typeAhead : true,
		forceSelection : true,
		hiddenName : 'stationRemoveBeen.oldSalaryGrade',
		editable : false,
		triggerAction : 'all',
		width : THEWIDTH,
		selectOnFocus : true,
		disabled : true,
		hideTrigger : true
	});
    

    // 调整后薪级
    var txtNewSalaryG = new Ext.form.ComboBox({
		fieldLabel : "调整后薪级<font color='red'>*</font>",
		name : 'newSalaryGrade',
		id : 'newSalaryGrade',
		store : salaryLevelStore,
		valueField : "id",
		displayField : "text",
		mode : 'local',
		hiddenName : 'stationRemoveBeen.newSalaryGrade',
		editable : false,
		triggerAction : 'all',
		width : THEWIDTH,
		blankText : '请选择',
		emptyText : '请选择',
		allowBlank : false
	});
	// 调动通知单号
    var textRequisition = new Ext.form.TextField({
        id : "requisition",
        width : THEWIDTH,
        name : 'stationRemoveBeen.requisitionNo',
        allowBlank : false,
        fieldLabel : '调动通知单号<font color ="red">*</font>'
    });
    // *********************************************************************************
    // 定义弹出窗体中的form
    var mypanel = new Ext.FormPanel({
        labelAlign : 'right',
        autoHeight : true,
        layout : "column",
        frame : true,
        items : [{
        	columnWidth : 0.5,
        	layout : 'form',
        	items : [addFlag,drpSubOldDept,drpWorkerName,beforeStationName,textBStationLevelName,txtOldSalaryG, txtRemoveType,txtDoDate]
        },{
        	columnWidth : 0.5,
        	layout : 'form',
        	items : [drpSubNewDept,txtWorkerId,CmbAStation, CmbAStationLevel,txtNewSalaryG,txtRemoveDate,textRequisition]
        },{
        	columnWidth : 1,
        	layout : 'form',
        	items : [txaMemo,hidSubOldDept, hidSubNewDept, hidEmpId, hidLastModifiedDate, hidStationremoveid,hidBeforeStationId]
        }
            ]

    });
    
    // 定义弹出窗体
    var win = new Ext.Window({
        width : 600,
        height : 360,
        buttonAlign : "center",
        items : [mypanel],
        layout : 'fit',
        closeAction : 'hide',
        modal : true,
        resizable : false,
        buttons : [{
                text : Constants.BTN_SAVE,
                iconCls : Constants.CLS_SAVE,
                handler : function() {
                    var myurl = "";
                    if (Ext.get("addFlag").dom.value == "1") {
                        // 新增url
                        myurl = "hr/addStationRemove.action";
                    } else {
                        // 修改url
                        myurl = "hr/updateStationRemove.action";
                    }
                    // 画面check
                    if (Ext.get("addFlag").dom.value == "1") {
                        // 必须输入项check
                        var nullMsg = "";
                        // 调动前班组 必须输入项check
                        if (drpSubOldDept.getValue() == null || drpSubOldDept.getValue() == "") {
                            nullMsg = nullMsg + String.format(Constants.COM_E_003, "调动前班组");
//                            drpSubOldDept.markInvalid();
                        }
                        // 调动后班组 必须输入项check
                        if (drpWorkerName.getValue() == null || drpWorkerName.getValue() == "") {
                            if (nullMsg != "") {
                                nullMsg = nullMsg + "<br/>";
                            }
                            nullMsg = nullMsg + String.format(Constants.COM_E_003, "员工姓名");
//                            drpWorkerName.markInvalid();
                        }
                        // 调动后班组 必须输入项check
                        if (drpSubNewDept.getValue() == null || drpSubNewDept.getValue() == "") {
                            if (nullMsg != "") {
                                nullMsg = nullMsg + "<br/>";
                            }
                            nullMsg = nullMsg + String.format(Constants.COM_E_003, "调动后班组");
//                            drpSubNewDept.markInvalid();
                        }
                        if (nullMsg != "") {
                            Ext.Msg.alert(Constants.ERROR, nullMsg);
                            return;
                        }
                    }

                    Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_001, function(buttonobj) {
                        if (buttonobj == "yes") {
                            if (Ext.get("addFlag").dom.value == "1") {
                                // 调动前班组,调动后班组相同性check
                                if (drpSubOldDept.getValue() == drpSubNewDept.getValue()) {
                                    Ext.Msg.alert(Constants.ERROR, Constants.COM_E_037);
                                    return;
                                }
                            }
                            // 执行日期,调动日期前后check
                            var strStartDate = txtRemoveDate.getValue();
                            var strEndDate = txtDoDate.getValue();
                            if (strStartDate != "" && strEndDate != "") {
                                var dateStart = Date.parseDate(strStartDate, 'Y-m-d');
                                var dateEnd = Date.parseDate(strEndDate, 'Y-m-d');
                                if (dateStart.getTime() > dateEnd.getTime()) {
                                    Ext.Msg.alert(Constants.ERROR, String.format(Constants.COM_E_026, "调动日期", "执行日期"));
                                    return;
                                }
                            }
                            // 画面控件是否红线
                            if (!mypanel.getForm().isValid()) {
                                return;
                            }
                            mypanel.getForm().submit({
                                method : 'POST',
                                url : myurl,
                                success : function(form, action) {
                                    var result = eval("(" + action.response.responseText + ")");
                                    if (result.success) {
                                        if (result.flag == 1) {
                                            // 保存成功
                                            Ext.Msg.alert(Constants.REMIND, Constants.COM_I_004, function() {
                                                // 隐藏弹出窗口
                                                win.hide();
                                            });
                                            // 刷新父画面
                                            runGridStore.load({
                                                params : {
                                                    start : 0,
                                                    limit : Constants.PAGE_SIZE
                                                }
                                            });
                                        } else if (result.flag == 0) {
                                            // 保存失败
                                            Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                                        } else if (result.flag == 2) {
                                            // 保存失败(排他错误)
                                            Ext.Msg.alert(Constants.ERROR, Constants.COM_E_015);
                                        }
                                    }
                                },
                                failure : function() {
                                    // 保存失败
                                    Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                                }
                            });
                        }
                    })
                }
            }, {
                text : Constants.BTN_CANCEL,
                iconCls : Constants.CLS_CANCEL,
                handler : function() {
                    Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_005, function(button, text) {
                        if (button == "yes") {
                            win.hide();
                        }
                    });
                }
            }]
    });
    /**
     * 增加处理
     */
    function addRecord() {
        mypanel.getForm().reset();
        win.setTitle("新增班组人员");
        win.x = undefined;
        win.y = undefined;
        win.show();
        Ext.get("addFlag").dom.value = "1";
        // modify by liuyi 090924 直接将班组间的调动写死
//        txtRemoveType.setValue(moveTypeName);
        txtRemoveType.setValue('班组间');
        drpSubOldDept.setDisabled(false);
        drpSubNewDept.setDisabled(false);
        drpWorkerName.setDisabled(false);
        // 去除员工姓名store
        empNameStore.removeAll();
//        drpWorkerName.setValue("");
    }
    /**
     * 修改处理
     */
    function updateRecord() {
        // 是否有被选项
        if (runGrid.selModel.hasSelection()) {
            var record = runGrid.getSelectionModel().getSelected();
            win.setTitle("修改班组人员");
            win.x = undefined;
            win.y = undefined;
            win.show();
            mypanel.getForm().loadRecord(record);
            txtRemoveType.setValue("班组间");
            Ext.get("addFlag").dom.value = "0";
//            // 如果调动时间为空,设置为系统日期
//            if (txtRemoveDate.getValue() == "") {
//                txtRemoveDate.setValue(sysDate);
//            }
            drpSubOldDept.setDisabled(true);
            drpSubNewDept.setDisabled(true);
            drpWorkerName.setDisabled(true);
        } else {
            Ext.Msg.alert(Constants.REMIND, Constants.COM_I_001);
        }
    }
    /**
     * 查询处理
     */
    function queryRecord() {
        queryFlag = true;
        // if (checkDate()) {
        runGridStore.baseParams = {
            startDate : txtStartDate.getValue(),
            endDate : txtEndDate.getValue(),
            oldDeptId : drpOldDept.getValue(),
            newDeptId : drpNewDept.getValue()
        };
        runGridStore.load({
            params : {
                start : 0,
                limit : Constants.PAGE_SIZE
            }
        });
        // }
    }
    /**
     * 时间的有效性检查
     */
    function checkDate() {
        var strStartDate = Ext.get("startDate").dom.value;
        var strEndDate = Ext.get("endDate").dom.value;
        if (strStartDate != "" && strEndDate != "") {
            var dateStart = Date.parseDate(strStartDate, 'Y-m-d');
            var dateEnd = Date.parseDate(strEndDate, 'Y-m-d');
            if (dateStart.getTime() > dateEnd.getTime()) {
                Ext.Msg.alert(Constants.ERROR, String.format(Constants.COM_E_009, "开始日期", "结束日期"));
                return false;
            } else {
                return true;
            }
        }
        return true;
    }
    /**
     * 员工姓名选择后处理：动态加载员工工号(员工编码）
     */
    function drpWorkerNameSelected(combo, record) {
        txtWorkerId.setValue(record.get('empCode'));
        Ext.get("hidEmpId").dom.value = drpWorkerName.getValue();
    }
    /**
     * 调动前班组选择后处理：动态加载员工姓名
     */
    function drpSubOldDeptSelected() {
        // 调动前班组
        var theOldId = drpSubOldDept.getValue();
        Ext.get("hidSubOldDept").dom.value = theOldId;
        if (theOldId == null) {
            // 去除员工姓名store
            empNameStore.removeAll();
            drpWorkerName.setValue("");
//            drpWorkerName.clearInvalid();
            txtWorkerId.setValue("");
            return;
        }
        empNameStore.load({
            params : {
                deptId : theOldId
            }
        });
        empNameStore.on("load", function(e, records, o) {
            drpWorkerName.setValue("");
//            drpWorkerName.clearInvalid();
            txtWorkerId.setValue("");
        });
    }
    /**
     * 单元格双击处理函数
     */
    function cellDbClick(grid, rowIndex, columnIndex, e) {
        if (rowIndex < runGridStore.getCount()) {
            if (columnIndex == 6) {
                memoText.setValue(runGridStore.getAt(rowIndex).get('memo'));
                memoWin.show();
            } else {
                updateRecord();
            }
        }
    }
});