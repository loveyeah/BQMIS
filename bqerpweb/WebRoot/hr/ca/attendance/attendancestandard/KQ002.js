Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
    var dd = new Date();
    dd = dd.format('Y');
    var rootId = "";
    var selectId = "";
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
        dataUrl : 'ca/getAttendanceDeptInfo.action'
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
        rootVisible : false,
        root : root,
        animate : true,
        enableDD : false,
        containerScroll : true,
        loader : treeLoader
    });

    // 根结点
    var root = new Tree.AsyncTreeNode({
        text : '灞桥热电厂',
        isRoot : true,
        // modify by liuyi 090930
        id : '-1'
//        id : '0'

    });
    deptTree.on("click", treeClick);
    deptTree.setRootNode(root);
    root.select();
    root.on('load', function(obj) {
        customNode.setCurrentNode(obj.firstChild);
        // 根节点ID
        rootId = customNode._currentNode.attributes.id;
        // 设置tbar的考勤年份
        attedanceYearBar.setValue(attendanceYear.getValue());
        // 设置tbar的考勤部门
        attedanceDeptBar.setValue(customNode._currentNode.attributes.text);
        // 开始画面初始化
        queryStore.load({
            params : {
                attendanceYear : attendanceYear.getValue(),
                attendanceDeptId : customNode._currentNode.attributes.id,
                attendanceDeptName : customNode._currentNode.attributes.text,
                isRoot:'Y'
            }
        });
        // 设置考勤部门id
        selectId = customNode._currentNode.attributes.id;
    })
    root.expand();

    var customNode = new CustomNode(root);
    /**
     * 点击树时
     */
    function treeClick(node, e) {
        // 选中节点
        customNode.isCurrentClick = true;
        // 设置为现在的节点
        customNode.setCurrentNode(node);
        node.toggle();
    }

    // 定义考勤年份
    var attendanceYear = new Ext.form.TextField({
        id : 'startDate',
        style : 'cursor:pointer',
        readOnly : true,
        width:120,
        value : dd,
        listeners : {
            focus : function() {
                WdatePicker({
                    startDate : '%y',
                    dateFmt : 'yyyy',
                    alwaysUseStartDate : false,
                    isShowToday : false,
                    isShowClear : false
                });
                this.blur();
            }
        }
    });
    // 考勤年份
    var attedanceYearBar = new Ext.form.TextField({
    	width:120,
        disabled : true
    })

    // 考勤部门
    var attedanceDeptBar = new Ext.form.TextField({
    	width:120,
        disabled : true
    })

    // 包含子部门
    var containLeaf = new Ext.form.Checkbox({
        id : 'Y',
        inputValue : Constants.CHECKED_VALUE
    })

    // 查询按钮
    var btnQuery = new Ext.Button({
        text : Constants.BTN_QUERY,
        iconCls : Constants.CLS_QUERY,
        handler : queryAttendanceStandard
    });

    // 行复制按钮
    var btnCCopy = new Ext.Button({
        text : "行复制",
         iconCls : Constants.CLS_LINE_COPY,
        handler : cCopyData
    });

    // 保存按钮
    var btnSave = new Ext.Button({
        text : Constants.BTN_SAVE,
        iconCls : Constants.CLS_SAVE,
        handler : saveData
    });

    // 数据源--------------------------------
    var MyRecord = Ext.data.Record.create([{
            // 考勤标准ID
            name : 'attendancestandardid'
        }, {
            // 考勤年份
            name : 'attendanceYear'
        }, {
            // 考勤月份
            name : 'attendanceMonth'
        }, {
            // 考勤部门ID
            name : 'attendanceDeptId'
        }, {
            // 考勤部门名称
            name : 'attendanceDeptName'
        }, {
            // 开始日期
            name : 'startDate'
        }, {
            // 结束日期
            name : 'endDate'
        }, {
            // 标准天数
            name : 'standardDay'
        }, {
            // 上午上班时间
            name : 'amBegingTime'
        }, {
            // 上午下班时间
            name : 'amEndTime'
        }, {
            // 下午上班时间
            name : 'pmBegingTime'
        }, {
            // 下午下班时间
            name : 'pmEndTime'
        }, {
            // 标准出勤时间
            name : 'standardTime'
        }, {
            // 上次修改日期
            name : 'lastModifiyDate'
        }]);

    // 定义获取数据源
    var dataProxy = new Ext.data.HttpProxy({
        url : 'ca/getAttendanceStandardInfo.action'
    });

    // 定义格式化数据
    var theReader = new Ext.data.JsonReader({
        root : "list",
        totalProperty : "totalCount",
        sortInfo : {
            field : "attendanceMonth",
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

    // grid Tbar
    var gridTbar = new Ext.Toolbar({
        border : false,
        height : 25,
        items : ['考勤年份:', attedanceYearBar, '考勤部门:', attedanceDeptBar, '-', containLeaf, '保存时包含子部门']
    });

    // gridPanel
    var recordGrid = new Ext.grid.EditorGridPanel({
        region : "center",
        store : queryStore,
        border : false,
        clicksToEdit : 1,
        columns : [{
                header : "考勤年份",
                sortable : false,
                hidden : true,
                dataIndex : 'attendanceYear'
            }, {
                header : '考勤部门',
                sortable : false,
                hidden : true,
                dataIndex : 'attendanceDeptName'
            }, {
                header : '月份',
                sortable : false,
                width : 40,
                dataIndex : 'attendanceMonth'

            }, {
                header : "开始日期<font color ='red'>*</font>",
                sortable : false,
                dataIndex : 'startDate',
                editor : new Ext.form.TextField({
                    format : 'Y-m-d',
                    itemCls : 'sex-left',
                    readOnly : true,
                    clearCls : 'allow-float',
                    checked : true,
                    width : 100,
                    anchor : '100%',
                    style : 'cursor:pointer',
                    listeners : {
                        focus : function() {
                            WdatePicker({
                                // 时间格式
                                startDate : '%y-%M-%d',
                                dateFmt : 'yyyy-MM-dd',
                                alwaysUseStartDate : false,
                                isShowClear : false,
                                onclearing : function() {
                                    recordGrid.getSelectionModel().getSelected().set("startDate", "")
                                },
                                onpicked : getStartDate
                            });
                        }
                    }
                })
            }, {
                header : "结束日期<font color ='red'>*</font>",
                sortable : false,
                dataIndex : 'endDate',
                editor : new Ext.form.TextField({
                    format : 'Y-m-d',
                    itemCls : 'sex-left',
                    readOnly : true,
                    clearCls : 'allow-float',
                    checked : true,
                    width : 100,
                    anchor : '100%',
                    style : 'cursor:pointer',
                    listeners : {
                        focus : function() {
                            WdatePicker({
                                // 时间格式
                                startDate : '%y-%M-%d',
                                dateFmt : 'yyyy-MM-dd',
                                alwaysUseStartDate : false,
                                isShowClear : false,
                                onclearing : function() {
                                    recordGrid.getSelectionModel().getSelected().set("endDate", "")
                                },
                                onpicked : getEndDate
                            });
                        }
                    }
                })
            }, {
                header : "标准天数",
                sortable : false,
                renderer : renderNumber,
                align : 'right',
                editor : new Powererp.form.NumField({
                    allowNegative : false,
                    maxValue : 99999999999.99,
                    maxLength : 14,
                    decimalPrecision : 2,
                    padding : 2,
                    minValue : 0,
                    allowDecimals : true
                }),
                dataIndex : 'standardDay'
            }, {
                header : "上午上班时间<font color ='red'>*</font>",
                sortable : false,
                dataIndex : 'amBegingTime',
                width : 100,
                editor : new Ext.form.TextField({
                    format : 'HH:mm',
                    itemCls : 'sex-left',
                    readOnly : true,
                    clearCls : 'allow-float',
                    checked : true,
                    width : 100,
                    anchor : '100%',
                    style : 'cursor:pointer',
                    listeners : {
                        focus : function() {
                            // by 王韵
                            // UT-BUG-KQ002-019
                            this.blur();
                            WdatePicker({
                                // 时间格式
                                startDate : 'HH:mm',
                                dateFmt : 'HH:mm',
                                alwaysUseStartDate : false,
                                isShowClear : false,
                                onclearing : function() {
                                    recordGrid.getSelectionModel().getSelected().set("amBegingTime", "")
                                },
                                onpicked : getAmBegingTime
                            });
                        }
                    }
                })
            }, {

                header : "上午下班时间<font color ='red'>*</font>",
                sortable : false,
                dataIndex : 'amEndTime',
                editor : new Ext.form.TextField({
                    format : 'HH:mm',
                    itemCls : 'sex-left',
                    readOnly : true,
                    clearCls : 'allow-float',
                    checked : true,
                    width : 100,
                    anchor : '100%',
                    style : 'cursor:pointer',
                    listeners : {
                        focus : function() {
                            // by 王韵
                            // UT-BUG-KQ002-019
                            this.blur();
                            WdatePicker({
                                // 时间格式
                                startDate : 'HH:mm',
                                dateFmt : 'HH:mm',
                                alwaysUseStartDate : false,
                                isShowClear : false,
                                onclearing : function() {
                                    recordGrid.getSelectionModel().getSelected().set("amEndTime", "")
                                },
                                onpicked : getAmEndTime
                            });
                        }
                    }
                })
            }, {
                header : "下午上班时间<font color ='red'>*</font>",
                sortable : false,
                dataIndex : 'pmBegingTime',
                editor : new Ext.form.TextField({
                    format : 'HH:mm',
                    itemCls : 'sex-left',
                    readOnly : true,
                    clearCls : 'allow-float',
                    checked : true,
                    width : 100,
                    anchor : '100%',
                    style : 'cursor:pointer',
                    listeners : {
                        focus : function() {
                            // by 王韵
                            // UT-BUG-KQ002-019
                            this.blur();
                            WdatePicker({
                                // 时间格式
                                startDate : 'HH:mm',
                                dateFmt : 'HH:mm',
                                alwaysUseStartDate : false,
                                isShowClear : false,
                                onclearing : function() {
                                    recordGrid.getSelectionModel().getSelected().set("pmBegingTime", "")
                                },
                                onpicked : getPmBegingTime
                            });
                        }
                    }
                })
            }, {
                header : "下午下班时间<font color ='red'>*</font>",
                sortable : false,
                dataIndex : 'pmEndTime',
                editor : new Ext.form.TextField({
                    format : 'HH:mm',
                    itemCls : 'sex-left',
                    readOnly : true,
                    clearCls : 'allow-float',
                    checked : true,
                    width : 100,
                    anchor : '100%',
                    style : 'cursor:pointer',
                    listeners : {
                        focus : function() {
                            // by 王韵
                            // UT-BUG-KQ002-019
                            this.blur();
                            WdatePicker({
                                // 时间格式
                                startDate : 'HH:mm',
                                dateFmt : 'HH:mm',
                                isShowClear : false,
                                alwaysUseStartDate : false,
                                onclearing : function() {
                                    recordGrid.getSelectionModel().getSelected().set("pmEndTime", "")
                                },
                                onpicked : getPmEndTime
                            });
                        }
                    }
                })
            }, {
                header : "行政班标准出勤时间<font color ='red'>*</font>",
                sortable : false,
                width : 120,
                renderer : renderNumber2,
                align : 'right',
                editor : new Powererp.form.NumField({
                    allowNegative : false,
                    maxValue : 99999999999.9,
                    decimalPrecision : 1,
                    maxLength : 13,
                    padding : 1,
                    minValue : 0,
                    allowDecimals : true
                }),
                dataIndex : 'standardTime'
            }],
        sm : sm,
        autoScroll : true,
        enableColumnMove : false,
        autoSizeColumns : true,
        viewConfig : {
            forceFit : false
        },
        // 头部工具栏
        tbar : gridTbar
    });
    // 判断是否是第一行
    recordGrid.on('beforeEdit', function(obj) {
        var record = obj.record;
        var field = obj.field;
        if (field === "standardTime") {
            if (record.get("attendanceYear") != null && record.get("attendanceYear") != "") {
                obj.cancel = false;
                return;
            } else {
                obj.cancel = true;
                return;
            }
        }
        // 如果选择的不是根节点，结束日期不可修改
        if (field === "endDate") {
            if (selectId != rootId) {
                obj.cancel = true;
                return;
            } else {
                obj.cancel = false;
                return;
            }
        }
        // 如果选择的不是根节点，开始日期不可修改
        if (field === "startDate") {
            if (selectId != rootId) {
                obj.cancel = true;
                return;
            } else {
                obj.cancel = false;
                return;
            }
        }
        // 如果选择的不是根节点，标准天数不可修改
        if (field === "standardDay") {
            if (selectId != rootId) {
                obj.cancel = true;
                return;
            } else {
                obj.cancel = false;
                return;
            }
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
        border : false,
        collapsible : true,
		split : true,
        style : "border-right:1px solid;border-top:1px solid",
        items : [deptTree]
    });

    /**
     * 右边panel
     */
    var panelRight = new Ext.Panel({
        region : "center",
        layout : 'fit',
        border : false,
        style : "border-left:1px solid;border-top:1px solid",
        enableTabScroll : true,
        containerScroll : true,
        items : [recordGrid]
    });

    // 总panel
    var panel = new Ext.Panel({
        tbar : ['考勤年份<font color ="red">*</font>:', attendanceYear, '-', btnQuery, btnCCopy, btnSave],
        enableTabScroll : true,
        border : false,
        layout : "border",
        items : [panelLeft, panelRight]
    })

    // 显示区域
    new Ext.Viewport({
        enableTabScroll : true,
        layout : "fit",
        border : false,
        items : [panel]
    });

    /**
     * 点击查询按钮时
     */
    function queryAttendanceStandard() {
        // 选择的部门id
        var id = customNode._currentNode.attributes.id;
        // 选择的部门名称
        var name = customNode._currentNode.text;
        // 如果没有选中节点，弹出提示信息
        if (id != null && id != "") {
            // 设置tbar的考勤年份
            attedanceYearBar.setValue(attendanceYear.getValue());
            // 设置tbar的考勤部门
            attedanceDeptBar.setValue(name);
            if (id != rootId) {
		        queryStore.load({
		            params : {
		                attendanceYear : attendanceYear.getValue(),
		                attendanceDeptId : id,
		                attendanceDeptName : name,
		                isRoot:'N'
		            }
		        });
            }else {
            	queryStore.load({
		            params : {
		                attendanceYear : attendanceYear.getValue(),
		                attendanceDeptId : id,
		                attendanceDeptName : name,
		                isRoot:'Y'
		            }
		        });
            }
            // 设置查询的考勤部门id
            selectId = id;
        } else
            Ext.Msg.alert(Constants.REMIND, Constants.KQ002_I_001);
    }

    /**
     * 数据渲染二位小树
     */
    function renderNumber(v, argDecimal) {
        if (v === 0)
            return "0.00";
        if (v) {
            if (typeof argDecimal != 'number') {
                argDecimal = 2;
            }
            v = Number(v).toFixed(argDecimal);
            var t = '';
            v = String(v);
            while ((t = v.replace(/^(-?[0-9]+)([0-9]{3}.*)$/, '$1,$2')) !== v)
                v = t;

            return v;
        } else
            return "";
    }

    /**
     * 数据渲染一位小树
     */
    function renderNumber2(v, argDecimal) {
        if (v === 0)
            return "0.0";
        if (v) {
            if (typeof argDecimal != 'number') {
                argDecimal = 1;
            }
            v = Number(v).toFixed(argDecimal);
            var t = '';
            v = String(v);
            while ((t = v.replace(/^(-?[0-9]+)([0-9]{3}.*)$/, '$1,$2')) !== v)
                v = t;

            return v;
        } else
            return "";
    }

    /**
     * 开始日期
     */
    function getStartDate() {
        var startDate = this.value;
        recordGrid.getSelectionModel().getSelected().set("startDate", startDate);

    }
    /**
     * 结束日期
     */
    function getEndDate() {
        var endDate = this.value;
        recordGrid.getSelectionModel().getSelected().set("endDate", endDate);
    }

    /**
     * 上午上班时间
     */
    function getAmBegingTime() {
        var amBegingTime = this.value;
        recordGrid.getSelectionModel().getSelected().set("amBegingTime", amBegingTime);
        if (recordGrid.getSelectionModel().getSelected().get("attendanceYear") != null
        && recordGrid.getSelectionModel().getSelected().get("attendanceYear") != "") {
            setStandardTime();
        }

    }
    /**
     * 上午下班时间
     */
    function getAmEndTime() {
        var amEndTime = this.value;
        recordGrid.getSelectionModel().getSelected().set("amEndTime", amEndTime);
        if (recordGrid.getSelectionModel().getSelected().get("attendanceYear") != null
        && recordGrid.getSelectionModel().getSelected().get("attendanceYear") != "") {
            setStandardTime();
        }
    }
    /**
     * 下午上班时间
     */
    function getPmBegingTime() {
        var pmBegingTime = this.value;
        recordGrid.getSelectionModel().getSelected().set("pmBegingTime", pmBegingTime);
        if (recordGrid.getSelectionModel().getSelected().get("attendanceYear") != null
        && recordGrid.getSelectionModel().getSelected().get("attendanceYear") != "") {
            setStandardTime();
        }
    }
    /**
     * 下午下班时间
     */
    function getPmEndTime() {
        var pmEndTime = this.value;
        recordGrid.getSelectionModel().getSelected().set("pmEndTime", pmEndTime);
        if (recordGrid.getSelectionModel().getSelected().get("attendanceYear") != null
        && recordGrid.getSelectionModel().getSelected().get("attendanceYear") != "") {
            setStandardTime();
        }
    }
    /**
     * 获取标准时间
     */
    function setStandardTime() {
    	// 如果时间都不为空的时候，算标准出勤时间
        if (recordGrid.getSelectionModel().getSelected().get("amBegingTime") != null
        && recordGrid.getSelectionModel().getSelected().get("amBegingTime") != ""
        && recordGrid.getSelectionModel().getSelected().get("amEndTime") != null
        && recordGrid.getSelectionModel().getSelected().get("amEndTime") != ""
        && recordGrid.getSelectionModel().getSelected().get("pmBegingTime") != null
        && recordGrid.getSelectionModel().getSelected().get("pmBegingTime") != ""
        && recordGrid.getSelectionModel().getSelected().get("pmEndTime") != null
        && recordGrid.getSelectionModel().getSelected().get("pmEndTime") != "") {
            var amBegingTime = recordGrid.getSelectionModel().getSelected().get("amBegingTime");
            var amEndTime = recordGrid.getSelectionModel().getSelected().get("amEndTime");
            var pmBegingTime = recordGrid.getSelectionModel().getSelected().get("pmBegingTime");
            var pmEndTime = recordGrid.getSelectionModel().getSelected().get("pmEndTime");
            var am = parseInt(trimZeroBefore(amEndTime)) - parseInt(trimZeroBefore(amBegingTime))
            + (parseInt(trimZeroAfter(amEndTime)) - parseInt(trimZeroAfter(amBegingTime))) / 60;
            var pm = parseInt(trimZeroBefore(pmEndTime)) - parseInt(trimZeroBefore(pmBegingTime))
            + (parseInt(trimZeroAfter(pmEndTime)) - parseInt(trimZeroAfter(pmBegingTime))) / 60;
            if (am + pm >= 0) {
                recordGrid.getSelectionModel().getSelected().set("standardTime", renderNumber2(am + pm, 1));
            } else {
                recordGrid.getSelectionModel().getSelected().set("standardTime", "");
            }
        } else {
            recordGrid.getSelectionModel().getSelected().set("standardTime", "");
        }
    }

    /**
     * 获取时间中的分
     */
    function trimZeroBefore(data) {
    	var value = data.substr(0, data.toString().indexOf(":")).toString()
        if (value.toString().length === 2) {
            if (value.substr(0, 1).toString() == "0") {
                return value.substr(1, 2)
            }
        }
        return value;
    }
    
    /**
     * 获取0时间中的秒
     */
    function trimZeroAfter(data) {
    	var value = data.substr(data.toString().indexOf(":")+1,data.toString().length).toString()
        if (value.toString().length === 2) {
            if (value.substr(0, 1).toString() == "0") {
                return value.substr(1, 2)
            }
        }
        return value;
    }
    /**
     * 点击行复制按钮时
     */
    function cCopyData() {
        for (i = 1; i < queryStore.getCount(); i++) {
//        	alert(queryStore.getAt(0).get('standardTime'));
            // 上午上班时间
            if (queryStore.getAt(0).get('amBegingTime') != '') {
                queryStore.getAt(i).set('amBegingTime', queryStore.getAt(0).get('amBegingTime'));
            }
            // 上午上班时间
            if (queryStore.getAt(0).get('amEndTime') != '') {
                queryStore.getAt(i).set('amEndTime', queryStore.getAt(0).get('amEndTime'));
            }
            // 下午上班时间
            if (queryStore.getAt(0).get('pmBegingTime') != '') {
                queryStore.getAt(i).set('pmBegingTime', queryStore.getAt(0).get('pmBegingTime'));
            }
            // 下午下班时间
            if (queryStore.getAt(0).get('pmEndTime') != '') {
                queryStore.getAt(i).set('pmEndTime', queryStore.getAt(0).get('pmEndTime'));
            }
            if(queryStore.getAt(0).get('standardTime') != ''){
            	queryStore.getAt(i).set('standardTime', queryStore.getAt(0).get('standardTime'));
            }
        }
    }

    /**
     * 复制需要传到后台的值
     */
    function cloneLocationRecord(record) {
        var objClone = new Object();
        objClone['attendancestandardid'] = record['attendancestandardid'];
        objClone['attendanceYear'] = record['attendanceYear'];
        objClone['attendanceMonth'] = record['attendanceMonth'];
        objClone['attendanceDeptId'] = record['attendanceDeptId'];
        objClone['startDate'] = record['startDate'];
        objClone['endDate'] = record['endDate'];
        objClone['standardDay'] = record['standardDay'];
        objClone['amBegingTime'] = record['amBegingTime'];
        objClone['amEndTime'] = record['amEndTime'];
        objClone['pmBegingTime'] = record['pmBegingTime'];
        objClone['pmEndTime'] = record['pmEndTime'];
        objClone['standardTime'] = record['standardTime'];
        objClone['lastModifiyDate'] = record['lastModifiyDate'];
        return objClone;
    }

    /**
     * check是否为空值
     */
    function checkFull() {
        var msg = "";
        var flagStartDate = "";
        var flagEndDate = "";
        var flagAmBegingTime = "";
        var flagAmEndTime = "";
        var flagPmBegingTime = "";
        var flagPmEndTime = "";

        for (index = 0; index < queryStore.getCount(); index++) {
            var startDate = queryStore.getAt(index).get('startDate');
            var endDate = queryStore.getAt(index).get('endDate');
            var amBegingTime = queryStore.getAt(index).get('amBegingTime');
            var amEndTime = queryStore.getAt(index).get('amEndTime');
            var pmBegingTime = queryStore.getAt(index).get('pmBegingTime');
            var pmEndTime = queryStore.getAt(index).get('pmEndTime');
            var standardTime = queryStore.getAt(index).get('standardTime');
            if (flagStartDate != "1") {
                if (!checkNull(startDate)) {
                    msg += String.format(Constants.COM_E_003, "开始日期") + "</br>";
                    flagStartDate = "1"
                }
            }
            if (flagEndDate != "1") {
                if (!checkNull(endDate)) {
                    msg += String.format(Constants.COM_E_003, "结束日期") + "</br>";
                    flagEndDate = "1";
                }
            }
            if (flagAmBegingTime != "1") {
                if (!checkNull(amBegingTime)) {
                    msg += String.format(Constants.COM_E_003, "上午上班时间") + "</br>";
                    flagAmBegingTime = "1";
                }
            }
            if (flagAmEndTime != "1") {
                if (!checkNull(amEndTime)) {
                    msg += String.format(Constants.COM_E_003, "上午下班时间") + "</br>";
                    flagAmEndTime = "1";
                }
            }
            if (flagPmBegingTime != "1") {
                if (!checkNull(pmBegingTime)) {
                    msg += String.format(Constants.COM_E_003, "下午上班时间") + "</br>";
                    flagPmBegingTime = "1";
                }
            }
            if (flagPmEndTime != "1") {
                if (!checkNull(pmEndTime)) {
                    msg += String.format(Constants.COM_E_003, "下午下班时间") + "</br>";
                    flagPmEndTime = "1";
                }
            }
            if (index == 0 && !checkNull(standardTime))
                msg += String.format(Constants.COM_E_002, "行政班标准出勤时间") + "</br>";
        }
        return msg;
    }

    /**
     * check日期的大小
     */
    function checkSize() {
        var msg = "";
        var flag1 = "0";
        var flag2 = "0";
        var flag3 = "0";
        var flag4 = "0";
        for (index = 0; index < queryStore.getCount(); index++) {
            var startDate = queryStore.getAt(index).get('startDate');
            var endDate = queryStore.getAt(index).get('endDate');
            var amBegingTime = queryStore.getAt(index).get('amBegingTime');
            var amEndTime = queryStore.getAt(index).get('amEndTime');
            var pmBegingTime = queryStore.getAt(index).get('pmBegingTime');
            var pmEndTime = queryStore.getAt(index).get('pmEndTime');
            var standardTime = queryStore.getAt(index).get('standardTime');
            if (flag1 != "1") {
                if (!checkDate(startDate, endDate)) {
                    msg += String.format(Constants.COM_E_026, "开始日期", "结束日期") + "</br>";
                    flag1 = "1";
                }
            }
            if (flag2 != "1") {
                if (!checkHourMinXD(amEndTime, pmBegingTime)) {
                    msg += String.format(Constants.COM_E_026, "上午下班时间", "下午上班时间") + "</br>";
                    flag2 = "1";
                }
            }
            if (flag3 != "1") {
                if (!checkHourMinX(amBegingTime, amEndTime)) {
                    msg += String.format(Constants.COM_E_006, "上午上班时间", "上午下班时间") + "</br>";
                    flag3 = "1";
                }
            }
            if (flag4 != "1") {
                if (!checkHourMinX(pmBegingTime, pmEndTime)) {
                    msg += String.format(Constants.COM_E_006, "下午上班时间", "下午下班时间") + "</br>";
                    flag4 = "1";
                }
            }
        }
        return msg;
    }

    /**
     * check字符串是否为null和“”
     */
    function checkNull(data) {
        if (data != null && data != "")
            return true;
        else if (data === 0)
            return true;
        else
            return false;
    }
    /**
     * 点保存按钮时触发
     */
    function saveData() {

    	 // 选择的部门id
        var id = selectId
//        customNode._currentNode.attributes.id;
        /**是否是根节点*/
        var isRoot = ""; 
        var records = new Array();
        // 循环收集gird值
        for (index = 0; index < queryStore.getCount(); index++) {
            var record = queryStore.getAt(index).data;
            records.push(cloneLocationRecord(record))
        }
        // 标识是否包含子部门
        var flag = "";
        // 弹出的确认message
        var message = "";
        // 如果选中
        if (containLeaf.checked) {
            flag = "1";
            message = Constants.KQ002_C_001;
        } else {
            flag = "0";
            message = Constants.COM_C_001;
        }
         if (id != rootId) {
         	isRoot = "N";
         }else {
         	isRoot = "Y";
         }
        if (checkFull() != "") {
            Ext.Msg.alert(Constants.ERROR, checkFull());
        } else {
            Ext.Msg.confirm(Constants.CONFIRM, message, function(buttonobj) {
                // 如果选择是
                if (buttonobj == "yes") {
                    if (checkSize() != "") {
                        Ext.Msg.alert(Constants.ERROR, checkSize());
                    } else {
                        Ext.Ajax.request({
                            method : Constants.POST,
                            url : 'ca/saveAttendanceStandard.action',
                            params : {
                                flag : flag,
                                isRoot:isRoot,
                                records : Ext.util.JSON.encode(records)
                            },
                            success : function(result, request) {
                                var o = eval("(" + result.responseText + ")");
                                var succ = o.msg;
                                // 如果更新失败，弹出提示
                                if (succ == Constants.SQL_FAILURE) {
                                    Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                                } else {
                                    Ext.Msg.alert(Constants.REMIND, Constants.COM_I_004)
                                    // 重新加载数据
                                    queryAttendanceStandard();
                                }
                            }
                        });
                    }
                }
            })
        }
    }

    /**
     * 日期的有效性检查
     */
    function checkDate(strStartDate, strEndDate) {
        if (strStartDate != "" && strEndDate != "") {
            var dateStart = Date.parseDate(strStartDate, 'Y-m-d');
            var dateEnd = Date.parseDate(strEndDate, 'Y-m-d');
            if (dateStart.getTime() > dateEnd.getTime()) {
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    /**
     * 时间的有效性检查 小于
     */
    function checkHourMinX(strStartDate, strEndDate) {
        if (strStartDate != "" && strEndDate != "") {
            if (parseInt(trimZeroBefore(strStartDate)) - parseInt(trimZeroBefore(strEndDate)) < 0) {
                return true;
            } else if (parseInt(trimZeroBefore(strStartDate)) - parseInt(trimZeroBefore(strEndDate)) == 0) {
                if (parseInt(trimZeroAfter(strStartDate)) - parseInt(trimZeroAfter(strEndDate)) < 0) {
                    return true;
                } else
                    return false;
            } else
                return false;
        }
        return true;
    }

    /**
     * 时间的有效性检查 小于等于
     */
    function checkHourMinXD(strStartDate, strEndDate) {
        if (strStartDate != "" && strEndDate != "") {
            if (parseInt(trimZeroBefore(strStartDate)) - parseInt(trimZeroBefore(strEndDate)) < 0) {
                return true;
            } else if (parseInt(trimZeroBefore(strStartDate)) - parseInt(trimZeroBefore(strEndDate)) == 0) {
                if (parseInt(trimZeroAfter(strStartDate)) - parseInt(trimZeroAfter(strEndDate)) <= 0) {
                    return true;
                } else
                    return false;
            } else
                return false;
        }
        return true;
    }
})
