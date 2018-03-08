Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {
    // 新增标识
    var FLAG_SUB_ADD = '0';
    // 修改标识
    var FLAG_SUB_MODIFY = '1';
    // 当前标识
    var flag = "";
    Ext.override(Ext.grid.GridView, {
        // 重写doRender方法
        doRender : function(cs, rs, ds, startRow, colCount, stripe) {
            var ts = this.templates, ct = ts.cell, rt = ts.row, last = colCount - 1;
            var tstyle = 'width:' + this.getTotalWidth() + ';';
            // buffers
            var buf = [], cb, c, p = {}, rp = {
                tstyle : tstyle
            }, r;
            for (var j = 0, len = rs.length; j < len; j++) {
                r = rs[j];
                cb = [];
                var rowIndex = (j + startRow);
                for (var i = 0; i < colCount; i++) {
                    c = cs[i];
                    p.id = c.id;
                    p.css = i == 0 ? 'x-grid3-cell-first ' : (i == last ? 'x-grid3-cell-last ' : '');
                    p.attr = p.cellAttr = "";
                    // 如果该行是统计行并且改列是第一列
                    if (r.data["countType"] == "total" && i == 0) {
                        p.value = "合计";
                    } else {
                        p.value = c.renderer(r.data[c.name], p, r, rowIndex, i, ds);
                    }
                    p.style = c.style;
                    if (p.value == undefined || p.value === "")
                        p.value = "&#160;";
                    if (r.dirty && typeof r.modified[c.name] !== 'undefined') {
                        p.css += ' x-grid3-dirty-cell';
                    }
                    cb[cb.length] = ct.apply(p);
                }
                var alt = [];
                if (stripe && ((rowIndex + 1) % 2 == 0)) {
                    alt[0] = "x-grid3-row-alt";
                }
                if (r.dirty) {
                    alt[1] = " x-grid3-dirty-row";
                }
                rp.cols = colCount;
                if (this.getRowClass) {
                    alt[2] = this.getRowClass(r, rowIndex, rp, ds);
                }
                rp.alt = alt.join(" ");
                rp.cells = cb.join("");
                buf[buf.length] = rt.apply(rp);
            }
            return buf.join("");
        }
    });

    // 自定义的节点类型
    function CustomNode(node) {
        // 当前节点
        this._currentNode = node;
        // 当前父节点
        this._parentNode = node.parentNode;
        if (!this._parentNode) {
            this._parentNode = node;
        }
        // 是否选中节点
        this.isCurrentClick = false;
        if (typeof CustomNode._initialized == "undefined") {
            CustomNode._initialized = true;
            // 设置当前节点
            CustomNode.prototype.setCurrentNode = function(argNode) {
                this._currentNode = argNode;
                this._parentNode = argNode.parentNode;
                if (!this._parentNode) {
                    this._parentNode = argNode;
                }
            }
        }

    }

    // 考勤部门树
    var Tree = Ext.tree;
    var treeLoader = new Tree.TreeLoader({
        dataUrl : 'ca/getAttendanceDeptData.action'
    })

    // 考勤部门树Panel
    var deptTree = new Tree.TreePanel({
        region : 'center',
        animate : true,
        allowDomMove : false,
        autoScroll : true,
        collapsible : true,
        split : true,
        border : false,
        rootVisible : true,
        root : root,
        animate : true,
        enableDD : false,
        containerScroll : true,
        loader : treeLoader
    });

    // 根结点
    var root = new Tree.AsyncTreeNode({
        text : '灞桥电厂',
        isRoot : true,
        id : '0'

    });
    deptTree.on("click", treeClick);
    deptTree.setRootNode(root);

    root.select();
    root.on('load', function(obj) {
         if(obj.firstChild!=null)
         {
           customNode.setCurrentNode(obj.firstChild);
         }
         else
         {
         	
         }
        // 开始画面初始化
        queryStore.load({
            params : {
                deptId : customNode._currentNode.attributes.id
            }
        });
    })
    root.expand();
    var customNode = new CustomNode(root);

    // 数据源--------------------------------
    var MyRecord = Ext.data.Record.create([{
        // 人员ID
        name : 'empId'
    }, {
        // 工号
        name : 'empCode'
    }, {
        // 姓名
        name : 'chsName'
    }, {
        // 性别
        name : 'sex'
    }, {
        // 部门ID
        name : 'deptId'
    }, {
        // 部门名称
        name : 'deptName'
    }]);

    // 定义获取数据源
    var dataProxy = new Ext.data.HttpProxy({
        url : 'ca/getAttendanceEmpInfo.action'
    });

    // 定义格式化数据
    var theReader = new Ext.data.JsonReader({
        root : "list",
        totalProperty : "totalCount",
        sortInfo : {
            field : "empCode",
            direction : "ASC"
        }
    }, MyRecord);

    // 定义封装缓存数据的对象
    var queryStore = new Ext.data.Store({
        // 访问的对象
        proxy : dataProxy,
        // 处理数据的对象
        reader : theReader
    });

    // 定义选择列
    var sm = new Ext.grid.RowSelectionModel({
        singleSelect : true
    });

    // 考勤员工gridPanel
    var recordGrid = new Ext.grid.GridPanel({
        region : "center",
        store : queryStore,
        border:false,
        columns : [
        // 自动行号
        new Ext.grid.RowNumberer({
            header : "行号",
            width : 31
        }), {
            header : "工号",
            sortable : true,
            dataIndex : 'empCode'
        }, {
            header : '姓名',
            sortable : true,
            dataIndex : 'chsName',
            renderer : function(value, cellmeta, record, rowIndex, columnIndex, store) {
                if (record.get("countType") != 'total')
                    return value;
                else if (record.get("countType") == 'total')
                    return (renderNumber(queryStore.getCount() - 1)) + "人"
            }
        }, {
            header : '性别',
            sortable : true,
            dataIndex : 'sex',
            renderer : function(value) {
                // 2009-3-16 郑智鹏  UT-BUG-KQ003-005修改begin
                if (value == "W")
                    return "女";
                else if (value == "M")
                    return "男";
                else
                    return "";
                // 2009-3-16 郑智鹏  UT-BUG-KQ003-005修改end
            }
        }, {
            header : "所属部门",
            sortable : true,
            dataIndex : 'deptName'
        }],
        sm : sm,
        autoScroll : true,
        enableColumnMove : false,
        autoSizeColumns : true,
        viewConfig : {
            forceFit : false
        }
    });

    // 加载数据时触发
    queryStore.on('load', function() {
        if (queryStore.getCount() > 0) {
            addLine();
        }
    })

    /**
     * 左边panel
     */
    var panelLeft = new Ext.Panel({
        region : 'west',
        layout : 'fit',
        title : "考勤部门",
        autoScroll : true,
        enableTabScroll : true,
        containerScroll : true,
        width : 280,
        collapsible : true,
		split : true,
      	border : false,
        style : "border-right:1px solid;border-top:1px solid",
        items : [deptTree]
    });

    /**
     * 右边panel
     */
    var panelRight = new Ext.Panel({
        region : "center",
        layout : 'fit',
        containerScroll : true,
        title : '考勤员工',
        autoScroll : true,
        border : false,
        style : "border-left:1px solid;border-top:1px solid",
        items : [recordGrid]
    });

    // 新增按钮
    var btnAdd = new Ext.Button({
        text : Constants.BTN_ADD,
        iconCls : Constants.CLS_ADD,
        handler : addCheckDept
    });

    // 修改按钮
    var btnModify = new Ext.Button({
        text : Constants.BTN_UPDATE,
        iconCls : Constants.CLS_UPDATE,
        handler : modifyCheckDept
    });

    // 存储弹出窗口信息
    var storeAttendanceDept = new Ext.data.JsonStore({
        url : 'ca/getAttendanceDeptSingleInfo.action',
        root : 'list',
        fields : ['id', 'attendanceDeptId', 'attendanceDeptName', 'attendDepType', 'topCheckDepId', 'topCheckDepName',
        'replaceDepId', 'replaceDepName', 'attendWriterId', 'attendWriterName', 'attendCheckerId', 'attendCheckerName','lastModifyDate']
    })
    storeAttendanceDept.on('load', function() {
        if (storeAttendanceDept.getCount() > 0) {
            hiddenDeptId.setValue(storeAttendanceDept.getAt(0).get('id'));
            hiddenLastModifyDate.setValue(storeAttendanceDept.getAt(0).get('lastModifyDate'));
            // 考勤部门
            txtAttendanceDept.setValue(storeAttendanceDept.getAt(0).get('attendanceDeptName'));
            // 考勤部门类别
            txtAttendanceKind.setValue(storeAttendanceDept.getAt(0).get('attendDepType'));
            if (txtAttendanceKind.getValue() == "3") {
                // 代考勤部门
                storeDept.getAt(0).data['replaceDepId'] = storeAttendanceDept.getAt(0).get('replaceDepId');
                storeDept.getAt(0).data['replaceDepName'] = storeAttendanceDept.getAt(0).get('replaceDepName');
                txtReplaceDep.setValue(storeAttendanceDept.getAt(0).get('replaceDepId'));
                txtReplaceDep.setDisabled(false);
                // 考勤审核人
                txtAttendChecker.setValue("");
                txtAttendChecker.setDisabled(true);
            } else {
                if (txtAttendanceKind.getValue() == "2") {
                    // 考勤审核人
                    storeDept.getAt(0).data['attendCheckerId'] = storeAttendanceDept.getAt(0).get('attendCheckerId');
                    storeDept.getAt(0).data['attendCheckerName'] = storeAttendanceDept.getAt(0)
                    .get('attendCheckerName');
                    txtAttendChecker.setValue(storeAttendanceDept.getAt(0).get('attendCheckerId'));
                    txtAttendChecker.setDisabled(false);
                } else {
                    // 考勤审核人
                    txtAttendChecker.setValue("");
                    txtAttendChecker.setDisabled(true);
                }
                // 代考勤部门
                txtReplaceDep.setValue("");
                txtReplaceDep.setDisabled(true);
            }
            // 上级审核部门
            storeDept.getAt(0).data['topCheckDepId'] = storeAttendanceDept.getAt(0).get('topCheckDepId');
            storeDept.getAt(0).data['topCheckDepName'] = storeAttendanceDept.getAt(0).get('topCheckDepName');
            txtTopCheckDep.setValue(storeAttendanceDept.getAt(0).get('topCheckDepId'));
            // 考勤登记人
            storeDept.getAt(0).data['attendWriterId'] = storeAttendanceDept.getAt(0).get('attendWriterId');
            storeDept.getAt(0).data['attendWriterName'] = storeAttendanceDept.getAt(0).get('attendWriterName');
            txtAttendWriter.setValue(storeAttendanceDept.getAt(0).get('attendWriterId'));
        }
        // 去掉红线
        clearWinInvalid();
    })

    // 删除按钮
    var btnDelete = new Ext.Button({
        text : Constants.BTN_DELETE,
        iconCls : Constants.CLS_DELETE,
        handler : delCheckDept
    });

    // 考勤人员变更按钮
    var btnEmpChange = new Ext.Button({
        text : "考勤人员变更",
        iconCls :Constants.CLS_PERSON_CHANGE,
        handler : empChange
    });

    /**
     * 弹出考勤人员变更画面
     */
    function empChange() {
        winEmp.x = undefined;
        winEmp.y = undefined;
        winEmp.show();
        // 清空部门选择
        txtDeptSelect.setValue("");
       	// 变更按钮不可用
        btnDeptChange.setDisabled(true);
        // 清空人员选择store
        storeEmpLeft.removeAll();
        // 2009-3-16 郑智鹏 UT-BUG-KQ003-006修改begin
        // 恢复初始化排序状态
        storeEmpLeft.sortInfo = null;
        empLeftGrid.getView().removeSortIcon();
        // 恢复初始化排序状态
        storeEmpRight.sortInfo = null;
        empRightGrid.getView().removeSortIcon();
        // 2009-3-16 郑智鹏 UT-BUG-KQ003-006修改end
        // 加载考勤部门人员信息
        storeEmpRight.load({
            params : {
                deptId : customNode._currentNode.attributes.id
            }
        });
    }

    // 主panel
    var panel = new Ext.Panel({
        tbar : [btnAdd, btnModify, btnDelete, btnEmpChange],
        enableTabScroll : true,
        layout : "border",
        border : false,
        frame : false,
        items : [panelLeft, panelRight]
    })
    // 显示区域
    new Ext.Viewport({
        enableTabScroll : true,
        layout : "fit",
        items : [panel]
    });

    // 考勤部门维护表ID
    var hiddenDeptId = new Ext.form.Hidden({
        id : 'hiddenDeptId'
    })
     // 考勤部门维护表ID
    var hiddenLastModifyDate = new Ext.form.Hidden({
        id : 'hiddenLastModifyDate'
    })
    // 考勤部门
    var txtAttendanceDept = new Ext.form.TextField({
        fieldLabel : '考勤部门<font color ="red">*</font>',
        width : 180,
        allowBlank : false,
        maxLength : 50
    })
    
    // 考勤类别
    var txtAttendanceKind = new Ext.form.CmbCACode({
        type : "考勤部门类别",
        width : 180,
        allowBlank : false,
        fieldLabel : '考勤类别<font color ="red">*</font>',
        listeners : {
            select : function() {
                if (txtAttendanceKind.getValue() == "3") {
                    txtReplaceDep.setDisabled(false);
                    // 如果选择的不是考勤审核部门，
                    // 将考勤审核人的信息清空
                    txtAttendChecker.setValue("");
                    txtAttendChecker.clearInvalid();
                    txtAttendChecker.setDisabled(true);
                } else {
                    if (txtAttendanceKind.getValue() == "2") {
                        txtAttendChecker.setDisabled(false);
                        // 如果选择的不是带考勤部门，
	                    // 将带考勤部门的信息清空
	                    txtReplaceDep.setValue("");
	                    txtReplaceDep.clearInvalid();
	                    txtReplaceDep.setDisabled(true);
                    } else if(txtAttendanceKind.getValue() == "1"){
                        // 如果选择的不是考勤审核部门，
                        // 将考勤审核人的信息清空
                        txtAttendChecker.setValue("");
                        txtAttendChecker.clearInvalid();
                        txtAttendChecker.setDisabled(true);
                        // 如果选择的不是带考勤部门，
	                    // 将带考勤部门的信息清空
	                    txtReplaceDep.setValue("");
	                    txtReplaceDep.clearInvalid();
	                    txtReplaceDep.setDisabled(true);
                    }else if(txtAttendanceKind.getValue() == "") {
                    	 // 如果选择的不是考勤审核部门，
                        // 将考勤审核人的信息清空
                        txtAttendChecker.setValue("");
                        txtAttendChecker.clearInvalid();
                        txtAttendChecker.setDisabled(true);
                        // 如果选择的不是带考勤部门，
	                    // 将带考勤部门的信息清空
	                    txtReplaceDep.setValue("");
	                    txtReplaceDep.clearInvalid();
	                    txtReplaceDep.setDisabled(true);
                    }
                    
                }
            }
        }
    });

    // 主页面显示Store
    var storeDept = new Ext.data.JsonStore({
        fields : ['replaceDepId', 'replaceDepName', 'topCheckDepId', 'topCheckDepName', 'attendWriterId',
        'attendCheckerName', 'attendCheckerId', 'attendCheckerName']
    })
    storeDept.insert(0, new Ext.data.Record({
        'replaceDepId' : '',
        'replaceDepName' : '',
        'topCheckDepId' : '',
        'topCheckDepName' : '',
        'attendWriterId' : '',
        'attendCheckerName' : '',
        'attendCheckerId' : '',
        'attendCheckerName' : ''
    }))
    // 代考勤部门
    var txtReplaceDep = new Ext.form.ComboBox({
        fieldLabel : '代考勤部门',
        hiddenName : 'replaceDepId',
        width : 180,
        readOnly : true,
        store : storeDept,
        disabled : true,
        displayField : "replaceDepName",
        valueField : "replaceDepId",
        triggerClass : 'noButtonCombobox',
        listeners : {
            'beforequery' : function(obj) {
                obj.cancel = true;
            }
        }
    });
    txtReplaceDep.onClick(function() {
        var args = {
            selectModel : 'single',
            rootNode : {
                id : '0',
                text : '灞桥电厂'
            },
            onlyLeaf : false
        };
        this.blur();
        var dept = window.showModalDialog('../../../../hr/ca/attendance/attendancerights/attdancedept.jsp', args, 'dialogWidth:'
        + Constants.WIDTH_COM_DEPT + 'px;dialogHeight:' + Constants.HEIGHT_COM_DEPT
        + 'px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
        if (typeof(dept) != "undefined") {
            storeDept.getAt(0).data['replaceDepId'] = dept.ids;
            storeDept.getAt(0).data['replaceDepName'] = dept.names;
            txtReplaceDep.setValue(dept.ids);
        }
    });

    // 上级审核部门
    var txtTopCheckDep = new Ext.form.ComboBox({
        fieldLabel : '上级部门',
//        fieldLabel : '上级审核部门',
        hiddenName : 'topCheckDepId',
        width : 180,
        disabled : true,
        store : storeDept,
        displayField : "topCheckDepName",
        valueField : "topCheckDepId",
        triggerClass : 'noButtonCombobox',
        listeners : {
            'beforequery' : function(obj) {
                obj.cancel = true;
            }
        }
    });

    // 考勤登记人
    var txtAttendWriter = new Ext.form.ComboBox({
        fieldLabel : '考勤登记人<font color ="red">*</font>',
        readOnly : true,
        allowBlank : false,
        store : storeDept,
        width : 180,
        displayField : "attendWriterName",
        valueField : 'attendWriterId',
        mode : 'local',
        triggerAction : 'all',
        triggerClass : 'noButtonCombobox',
        listeners : {
            'beforequery' : function(obj) {
                obj.cancel = true;
            }
        }
    });

    // 考勤登记人的onClick事件
    txtAttendWriter.onClick(function() {
        var args = {
            selectModel : 'single',
            isbanzuuse:'yes',//add by kzhang 20100901
            rootNode : {
                id : '0',
                text : '灞桥电厂'
            },
            onlyLeaf : false
        };
        this.blur();
        var person = window.showModalDialog('../../../../comm/jsp/hr/workerByDept/workerByDept.jsp', args,
        'dialogWidth:' + Constants.WIDTH_COM_EMPLOYEE + 'px;dialogHeight:' + Constants.HEIGHT_COM_EMPLOYEE
        + 'px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
        if (typeof(person) != "undefined") {
            storeDept.getAt(0).data['attendWriterId'] = person.empId;
            storeDept.getAt(0).data['attendWriterName'] = person.workerName;
            txtAttendWriter.setValue(person.empId);
        }
    })

    // 考勤审核人
    var txtAttendChecker = new Ext.form.ComboBox({
        fieldLabel : '考勤审核人<font color ="red">*</font>',
        readOnly : true,
        width : 180,
        allowBlank : false,
        disabled : true,
        store : storeDept,
        displayField : "attendCheckerName",
        valueField : 'attendCheckerId',
        mode : 'local',
        triggerAction : 'all',
        triggerClass : 'noButtonCombobox',
        listeners : {
            'beforequery' : function(obj) {
                obj.cancel = true;
            }
        }
    });

    // 考勤审核人的onClick事件
    txtAttendChecker.onClick(function() {
        var args = {
            selectModel : 'single',
            rootNode : {
                id : '0',
                text : '灞桥电厂'
            },
            onlyLeaf : false
        };
        this.blur();
        var person = window.showModalDialog('../../../../comm/jsp/hr/workerByDept/workerByDept.jsp', args,
        'dialogWidth:' + Constants.WIDTH_COM_EMPLOYEE + 'px;dialogHeight:' + Constants.HEIGHT_COM_EMPLOYEE
        + 'px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
        if (typeof(person) != "undefined") {
            storeDept.getAt(0).data['attendCheckerId'] = person.empId;
            storeDept.getAt(0).data['attendCheckerName'] = person.workerName;
            txtAttendChecker.setValue(person.empId);
        }
    })

    // 考勤部门设置FormPanel
    var formDept = new Ext.form.FormPanel({
        border : false,
        labelAlign : 'right',
        frame : true,
        layout : 'form',
        items : [hiddenDeptId,hiddenLastModifyDate,txtTopCheckDep,txtAttendanceDept, 
//        txtAttendanceKind, txtReplaceDep,  //update by sychen 20100720
        txtAttendWriter
//        , txtAttendWriter, txtAttendChecker //update by sychen 20100720
        ]
    })

    // 保存按钮
    var btnDeptSave = new Ext.Button({
        text : Constants.BTN_SAVE,
        iconCls : Constants.CLS_SAVE,
        handler : saveDeptData
    });

    // 取消按钮
    var btnDeptCancel = new Ext.Button({
        text : Constants.BTN_CANCEL,
        iconCls : Constants.CLS_CANCEL,
        handler : cancelDeptData
    });

    /**
     * 取消考勤部门窗口
     */
    function cancelDeptData() {
		 Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_005, function(buttonobj) {
            // 如果选择是
            if (buttonobj == "yes") {
                // 隐藏弹出窗口
                winDept.hide();
            }
        })
    }
    // 考勤部门设置画面
    var winDept = new Ext.Window({
        width : 350,
        modal : true,
        resizable : false,
        closeAction : 'hide',
        items : [formDept],
        buttonAlign : "center",
        title : '',
        buttons : [btnDeptSave, btnDeptCancel]
    });

    // 定义封装缓存数据的对象
    var storeEmpLeft = new Ext.data.Store({
        // 访问的对象
        proxy : dataProxy,
        // 处理数据的对象
        reader : theReader
    });

    // 定义选择列
    var smLeft = new Ext.grid.RowSelectionModel({
        singleSelect : false
    });

    // 考勤员工gridPanel
    var empLeftGrid = new Ext.grid.GridPanel({
        store : storeEmpLeft,
        height : 260,
        width : 210,
        columns : [
        // 自动行号
        new Ext.grid.RowNumberer({
            header : "行号",
            width : 31
        }), {
            dataIndex : 'empId',
        	hidden:true
        },{
            header : "工号",
            sortable : true,
            width : 58,
            dataIndex : 'empCode'
        }, {
            header : "人员id",
            hidden : true,
            dataIndex : 'empId'
        }, {
            header : '姓名',
            sortable : true,
            width : 60,
            dataIndex : 'chsName',
            renderer : function(value, cellmeta, record, rowIndex, columnIndex, store) {
                if (record.get("countType") != 'total')
                    return value;
                else if (record.get("countType") == 'total')
                    return (renderNumber(storeEmpLeft.getCount() - 1)) + "人"
            }
        }, {
            header : '性别',
            sortable : true,
            width : 35,
            dataIndex : 'sex',
            renderer : function(value) {
                // 2009-3-16 郑智鹏 UT-BUG-KQ003-005修改begin
                if (value == "W")
                    return "女";
                else if (value == "M")
                    return "男";
                else
                    return "";
                // 2009-3-16 郑智鹏  UT-BUG-KQ003-005修改end
            }
        }],
        sm : smLeft,
        autoScroll : true,
        enableColumnMove : false,
        autoSizeColumns : true,
        viewConfig : {
            forceFit : false
        }
    });

    // 加载数据时触发
    storeEmpLeft.on('load', function() {
        if (storeEmpLeft.getCount() > 0) {
            addEmpLeftLine();
        }
    })
    /**
     * 添加共计行
     */
    function addEmpLeftLine() {
        // 原数据个数
        var count = storeEmpLeft.getCount();
        // 统计行
        var record = new MyRecord({
            countType : "total"
        });
        // 停止原来编辑
        empLeftGrid.stopEditing();
        // 插入统计行
        storeEmpLeft.insert(count, record);
        empLeftGrid.getView().refresh();
        totalCount = storeEmpLeft.getCount() - 1;
    }

    // 人员选择的单击事件
    empLeftGrid.on('rowclick', function() {
          var records = empLeftGrid.selModel.getSelections()
        if (records != null) {
        	 for (i = 0; i < records.length; i++) {
                if(records[i].get('countType') == "total") {
        			btnDeptChange.setDisabled(true);
                	break;
                }else if(i == records.length -1) {
                	btnDeptChange.setDisabled(false);
                }
        	}
        }else {
        	btnDeptChange.setDisabled(true);
        }
    })
    
    
    //add by sychen 20100713 
    	var ids = new Array();
	function deleteRecords(){
		var sm = empRightGrid.getSelectionModel();
		var selections = sm.getSelections();
		if (selections.length == 0) {
			Ext.Msg.alert("提示", "请选择要删除的记录！");
		} else {
				for (var i = 0; i < selections.length; i += 1) {
				var member = selections[i];
				if (member.get("empId") != null) {
					ids.push(member.get("empId"));
				}
			}
			Ext.Msg.confirm('提示', '是否确定删除该员工信息？', function(response) {
				if (response == 'yes') {
					Ext.Ajax.request({
								method : 'post',
								url : 'ca/deleteAttendanceDeptId.action',
								params : {
									ids : ids.join(",")
								},
								success : function(action) {
									Ext.Msg.alert("提示", "删除成功！");
									storeEmpRight.reload();
								},
								failure : function() {
									Ext.Msg.alert('错误', '删除时出现未知错误.');
								}
							});
				}
			});
		}
	
	}

	
    	var tbar = new Ext.Toolbar({
				items : [ {
							text : "删除",
							id : 'btndelete',
							iconCls : 'delete',
							handler : deleteRecords
						}]
			});
    //add by sychen 20100713 end
			
    // 定义封装缓存数据的对象
    var storeEmpRight = new Ext.data.Store({
        // 访问的对象
        proxy : dataProxy,
        // 处理数据的对象
        reader : theReader
    });

    // 定义选择列
    //update by sychen 20100713
    var smRight  = new Ext.grid.CheckboxSelectionModel({
				singleSelect : false
			});
//    var smRight = new Ext.grid.RowSelectionModel({
//        singleSelect : true
//    });
    // 考勤员工gridPanel
    var empRightGrid = new Ext.grid.GridPanel({
        region : "center",
        height : 323,
        width : 212,
        store : storeEmpRight,
        tbar : tbar,
        columns : [smRight,
        // 自动行号
        new Ext.grid.RowNumberer({
            header : "行号",
            width : 31
        }) , {
            header : "工号",
            sortable : true,
            width : 50,
            dataIndex : 'empCode'
        }, {
            header : '姓名',
            sortable : true,
            width : 70,
            dataIndex : 'chsName',
            renderer : function(value, cellmeta, record, rowIndex, columnIndex, store) {
                if (record.get("countType") != 'total')
                    return value;
                else if (record.get("countType") == 'total')
                    return (renderNumber(storeEmpRight.getCount() - 1)) + "人"
            }
        }, {
            header : '性别',
            sortable : true,
            width : 35,
            dataIndex : 'sex',
            renderer : function(value) {
            	// 2009-3-16 郑智鹏  UT-BUG-KQ003-005修改begin
                if (value == "W")
                    return "女";
                else if (value == "M")
                    return "男";
                else
                    return "";
                // 2009-3-16 郑智鹏  UT-BUG-KQ003-005修改end
            }
        }],
        sm : smRight,
        autoScroll : true,
        enableColumnMove : false,
        autoSizeColumns : true,
        viewConfig : {
            forceFit : false
        }
    });

    // 加载数据时触发
    storeEmpRight.on('load', function() {
        if (storeEmpRight.getCount() > 0) {
            addEmpRightLine();
        }
    })
    /**
     * 添加共计行
     */
    function addEmpRightLine() {
        // 原数据个数
        var count = storeEmpRight.getCount();
        // 统计行
        var record = new MyRecord({
            countType : "total"
        });
        // 停止原来编辑
        empRightGrid.stopEditing();
        // 插入统计行
        storeEmpRight.insert(count, record);
        empRightGrid.getView().refresh();
        totalCount = storeEmpRight.getCount() - 1;
    }

    // 部门选择(考勤人员变更)
    var txtDeptSelect = new Ext.form.TextField({
        width : 140,
        displayField : 'text',
        valueField : 'id',
        listeners : {
            focus : function() {
                var args = {
                    selectModel : 'single',
                    flag:'yes',
                    rootNode : {
                        id : '0',
                        text : Constants.POWER_NAME
                    },
                    onlyLeaf : false
                };
                this.blur();
                var dept = window.showModalDialog('../../../../comm/jsp/hr/dept/dept.jsp', args, 'dialogWidth:'
                + Constants.WIDTH_COM_DEPT + 'px;dialogHeight:' + Constants.HEIGHT_COM_DEPT
                + 'px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
                if (typeof(dept) != "undefined") {
                    hdnDeptSelect.setValue(dept.ids);
                    txtDeptSelect.setValue(dept.names);
                    storeEmpLeft.load({
                        params : {
                        	// 2009-3-16 郑智鹏  UT-BUG-KQ003-004修改begin
                            deptId : dept.ids,
                            // 通过所属部门检索
                            isAttendanceDeptFlag : "f"
                            // 2009-3-16 郑智鹏  UT-BUG-KQ003-004修改end
                        }
                    });
                }
            }
        }
    });
    // 员工id
    var hdnDeptSelect = new Ext.form.Hidden({
        id : 'DeptSelectID'
    })

    // 变更按钮
    var btnDeptChange = new Ext.Button({
        text : '变更',
        disabled : true,
        iconCls : Constants.CLS_RIGHT_MOVE,
        handler : changeDept
    })

    // 点击变更button
    function changeDept() {
        if (empLeftGrid.selModel.hasSelection()) {
            var records = empLeftGrid.selModel.getSelections();
            // 人员id集
            var empIds = "";
            for (i = 0; i < records.length; i++) {
                if (i != 0)
                    empIds += ",";
                empIds += records[i].get('empId');
            }
            Ext.Ajax.request({
                method : Constants.POST,
                url : 'ca/modifyDeptByEmpId.action',
                params : {
                    empIds : empIds,
                    deptId : customNode._currentNode.attributes.id
                },
                success : function(result, request) {
                    var o = eval("(" + result.responseText + ")");
                    var succ = o.msg;
                    // 如果更新失败，弹出提示
                    if (succ == Constants.SQL_FAILURE) {
                    } else {
                    	Ext.Msg.alert('提示',o.msg)
                    	// 2009-3-16 郑智鹏  UT-BUG-KQ003-004修改begin
                        // 考勤部门人员信息加载
                        storeEmpRight.load({
                            params : {
                                deptId : customNode._currentNode.attributes.id
                            },
                            callback : function() {
                                // 考勤部门人员信息加载
                                queryStore.load({
                                    params : {
                                        deptId : customNode._currentNode.attributes.id
                                    }
                                });
                            }
                        })
                        // 2009-3-16 郑智鹏  UT-BUG-KQ003-004修改end
                    }
                }
            })
        }
    }
    // 部门选择fieldSet
    var fsDeptSelect = new Ext.form.FieldSet({
        frame : false,
        labelAlign : 'right',
        style : "margin:4px;",
        layout : 'column',
        title : '部门选择',
        height : 50,
        width : 235,
        items : [{
            columnWidth : 0.7,
            border : false,
            height : 40,
            items : [txtDeptSelect, hdnDeptSelect]
        }, {
            columnWidth : 0.3,
            border : false,
            items : btnDeptChange
        }]
    })

    // 部门选择Panel
    var panelDeptSelect = new Ext.Panel({
        border : false,
        labelAlign : 'right',
        frame : false,
        layout : 'form',
        items : [fsDeptSelect, {
            xtype : 'fieldset',
            title : '人员选择',
            style : "margin:4px;",
            width : 235,
            height : 290,
            items : empLeftGrid
        }]
    })

    // 考勤人员变更弹出窗口
    var winEmp = new Ext.Window({
        modal : true,
        title : '考勤人员变更',
        resizable : false,
        closeAction : 'hide',
        layout : 'column',
        width : 500,
        height : 400,
        border : false,
        items : [{
            columnWidth : 0.5,
            border : false,
            items : panelDeptSelect
        }, {
            columnWidth : 0.5,
            border : false,
            items : [{
                xtype : 'fieldset',
                style : "margin:4px;",
                title : '考勤部门人员',
                width : 236,
                frame : false,
                height : 353,
                items : empRightGrid
            }]
        }]
    });

    /**
     * 点击树时
     */
    function treeClick(node, e) {
        // 选中节点
        customNode.isCurrentClick = true;
        // 设置为现在的节点
        customNode.setCurrentNode(node);
        // 查询数据
        queryStore.load({
            params : {
                deptId : customNode._currentNode.attributes.id
            }
        });
        node.toggle();
    }

    /**
     * 添加共计行
     */
    function addLine() {
        // 原数据个数
        var count = queryStore.getCount();
        // 统计行
        var record = new MyRecord({
            countType : "total"
        });
        // 停止原来编辑
        recordGrid.stopEditing();
        // 插入统计行
        queryStore.insert(count, record);
        recordGrid.getView().refresh();
        totalCount = queryStore.getCount() - 1;
    }

    /**
     * 新增审核部门
     */
    function addCheckDept() {
    	
     if(customNode._currentNode==null)
     {
     	Ext.Msg.alert("提示","请选择节点");
     	return ;
     	
     }
        // 部门id
        var deptid = customNode._currentNode.attributes.id;
        // 部门名称
        var deptName = customNode._currentNode.attributes.text;
        // 部门类型
        var deptType = customNode._currentNode.attributes.description;
//        if (deptType != 2) {
//            Ext.Msg.alert(Constants.ERROR, Constants.KQ003_E_001);
//        } else {
            winDept.x = undefined;
            winDept.y = undefined;
            winDept.show();
            winDept.setTitle("新增考勤部门设置");
            // 设置考勤类别可用
            txtAttendanceKind.setDisabled(false);
            // 代考勤部门
            txtReplaceDep.setDisabled(true);
            // 考勤审核人
            txtAttendChecker.setDisabled(true);
            // 清空值
            formDept.getForm().reset();
            // 设置标识为新增
            flag = FLAG_SUB_ADD;
            // 设置考勤部门表ID
            hiddenDeptId.setValue("");
            // 考勤部门不可用
            txtAttendanceDept.setDisabled(false);
            // 上级审核部门设置值
            storeDept.getAt(0).data['topCheckDepId'] = deptid;
            storeDept.getAt(0).data['topCheckDepName'] = deptName;
            txtTopCheckDep.setValue(deptid);
//        }
    }

    /**
     * 去掉红线提示
     */
    function clearWinInvalid() {
        // 考勤部门
        txtAttendanceDept.clearInvalid();
        // 考勤部门类别
        txtAttendanceKind.clearInvalid();
        // 代考勤部门
        txtReplaceDep.clearInvalid();
        // 上级审核部门
        txtTopCheckDep.clearInvalid();
        // 考勤登记人
        txtAttendWriter.clearInvalid();
        // 考勤审核人
        txtAttendChecker.clearInvalid();
    }

    /**
     * 修改考勤部门
     */
    function modifyCheckDept() {
        var id = customNode._currentNode.attributes.id;
        // 是否是叶子
        var blnLeaf = customNode._currentNode.attributes.leaf;
        winDept.x = undefined;
        winDept.y = undefined;
        winDept.show();
        winDept.setTitle("修改考勤部门设置");
        // 设置标识为修改
        flag = FLAG_SUB_MODIFY;
        storeAttendanceDept.load({
            params : {
                deptId : id
            }
        })
        // 考勤部门不可用
        txtAttendanceDept.setDisabled(true);
        // 如果是非叶子节点，设置考勤类别为不可用，反子
        if(!blnLeaf)
        	txtAttendanceKind.setDisabled(true);
        else 
        	txtAttendanceKind.setDisabled(false);
    }

    /*
     * 删除考勤部门
     */
    function delCheckDept() {
        // 是否是叶子
        var blnLeaf = customNode._currentNode.attributes.leaf;
        var parentNode = customNode._parentNode;
        var flag = "";
        // 弹出的message
        var msg = "";
        if (!blnLeaf) {
            flag = "0";
            msg += Constants.KQ003_I_001;
        } else {
            flag = "1";
            msg += Constants.COM_C_002;
        }
        Ext.Msg.confirm(Constants.CONFIRM, msg, function(buttonobj) {
            if (buttonobj == "yes") {
                Ext.Ajax.request({
                    method : Constants.POST,
                    url : 'ca/deleteAttendanceDeptInfo.action',
                    params : {
                        deptId : customNode._currentNode.attributes.id,
                        isLeaf : flag
                    },
                    success : function(result, request) {
                        if (result.responseText) {
                            var o = eval("(" + result.responseText + ")");
                            var suc = o.msg;
                            // 如果成功，弹出删除成功
                            if (suc == Constants.SQL_FAILURE) {
                                Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                            } else if (suc == Constants.DATA_USING) {
                                Ext.Msg.alert(Constants.ERROR, Constants.COM_E_015);
                            } else {
                                Ext.Msg.alert(Constants.REMIND, Constants.COM_I_005)
                                if (parentNode.attributes.id != 0) {
                                    customNode.setCurrentNode(parentNode);
                                     // 树形同步显示
                                    customNode._parentNode.reload();
                                    // 刷新考勤人员grid
                                    queryStore.load({
							            params : {
							                deptId : customNode._currentNode.attributes.id
							            }
							        });
                                } else {
                                    customNode.setCurrentNode(parentNode);
                                    parentNode.reload();
                                }
                            }
                        }
                    }
                })
            }
        })
    }

    /**
     * 保存考勤部门
     */
    function saveDeptData() {
        var message = "";
        var messageLength = "";
        var parentNode = customNode._parentNode;
        if (txtAttendanceDept.getValue() != null && txtAttendanceDept.getValue() != "") {
        } else {
            message += String.format(Constants.COM_E_002, "考勤部门") + "</br>";
        }
//        if (txtAttendanceKind.getValue() != null && txtAttendanceKind.getValue() != "") {
//            if (txtAttendanceKind.getValue() == "2") {
//                if (txtAttendChecker.getValue() != null && txtAttendChecker.getValue() != "") {
//                } else {
//                    message += String.format(Constants.COM_E_003, "考勤审核人") + "</br>";
//                }
//            }
//        } else {
//            message += String.format(Constants.COM_E_003, "考勤类别") + "</br>";
//        }
        if (txtAttendWriter.getValue() != null && txtAttendWriter.getValue() != "") {
        } else {
            message += String.format(Constants.COM_E_003, "考勤登记人") + "</br>";
        }
        if (txtAttendanceDept.getValue() != null && txtAttendanceDept.getValue() != "") {
            if (txtAttendanceDept.getValue().toString().length > 50)
                messageLength += "1";
        }
        if (message != "")
            Ext.Msg.alert(Constants.ERROR, message);
        else {
            if (FLAG_SUB_ADD == flag) {
                Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_001, function(buttonobj) {
                    // 如果选择是
                    if (buttonobj == "yes") {
                        if (messageLength != "") {
                        } else {
                            Ext.Ajax.request({
                                method : Constants.POST,
                                url : 'ca/addAttendanceDeptInfo.action',
                                params : {
                                    attendanceDeptName : txtAttendanceDept.getValue(),
//                                    attendDepType : txtAttendanceKind.getValue(),//update by sychen 20100720
//                                    replaceDepId : txtReplaceDep.getValue(),
                                    topCheckDepId : txtTopCheckDep.getValue(),
                                    attendWriterId : txtAttendWriter.getValue()
//                                    attendCheckerId : txtAttendChecker.getValue()
                                },
                                success : function(result, request) {
                                	//add by sychen 20100716
                                var resu = Ext.util.JSON.decode(result.responseText)
                                	if(resu.existFlag == true){
										Ext.Msg.alert("提示", "该考勤部门已存在 ,请重新输入!");
                                	}
                                	else{
                                	//add by sychen 20100716 end 
                                    var o = eval("(" + result.responseText + ")");
                                    var succ = o.msg;
                                    // 如果更新失败，弹出提示
                                    if (succ == Constants.SQL_FAILURE) {
                                        Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                                    } else {
                                        // 树形同步显示
                                    	customNode._parentNode = parentNode;
                                     // 树形同步显示
                                    customNode._parentNode.reload();
                                        Ext.Msg.alert(Constants.REMIND, Constants.COM_I_004, function() {
                                            // 重新加载数据
                                            winDept.hide();
                                        })
                                    }
                                }//add by sychen 20100716 
                            }
                            });
                        }
                    }

                })
            } else if (FLAG_SUB_MODIFY == flag) {
                Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_001, function(buttonobj) {
                    // 如果选择是
                    if (buttonobj == "yes") {
                        if (messageLength != "") {
                        } else {
                            Ext.Ajax.request({
                                method : Constants.POST,
                                url : 'ca/modifyAttendanceDeptInfo.action',
                                params : {
                                    id : hiddenDeptId.getValue(),
                                    attendDepType : txtAttendanceKind.getValue(),
                                    replaceDepId : txtReplaceDep.getValue(),
                                    attendWriterId : txtAttendWriter.getValue(),
                                    attendCheckerId : txtAttendChecker.getValue(),
                                    lastModifyDate:hiddenLastModifyDate.getValue()
                                },
                                success : function(result, request) {
                                    var o = eval("(" + result.responseText + ")");
                                    var succ = o.msg;
                                    // 如果更新失败，弹出提示
                                    if (succ == Constants.SQL_FAILURE) {
                                        Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                                    }else if(succ == Constants.DATA_USING){
                                    	Ext.Msg.alert(Constants.ERROR, Constants.COM_E_015);
                                	}else {
                                        customNode._parentNode.reload();
                                        Ext.Msg.alert(Constants.REMIND, Constants.COM_I_004, function() {
                                            // 重新加载数据
                                            winDept.hide();
                                        })
                                    }
                                }
                            });
                        }
                    }

                })
            }
        }
    }
    
    /**
	 * 数据渲染0位小数
	 */
	function renderNumber(v, argDecimal) {
		if(v===0)return "0";	
		if (v) {
			if (typeof argDecimal != 'number') {
				argDecimal = 0;
			}
			v = Number(v).toFixed(argDecimal);
			var t = '';
			v = String(v);
			while ((t = v.replace(/^(-?[0-9]+)([0-9]{3}.*)$/, '$1,$2')) !== v)
				v = t;

			return v ;
		} else
			return "";
	}
	// 2009-3-16 郑智鹏 UT-BUG-KQ003-006修改begin
    storeEmpLeft.on('beforeload', function() {
        // 恢复初始化排序状态
        storeEmpLeft.sortInfo = null;
        empLeftGrid.getView().removeSortIcon();
    })
    storeEmpRight.on('beforeload', function() {
        // 恢复初始化排序状态
        storeEmpRight.sortInfo = null;
        empRightGrid.getView().removeSortIcon();
    })
    queryStore.on('beforeload', function() {
        // 恢复初始化排序状态
        queryStore.sortInfo = null;
        recordGrid.getView().removeSortIcon();
    })
    // 2009-3-16 郑智鹏 UT-BUG-KQ003-006修改end
})