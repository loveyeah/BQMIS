Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
// 除去假别以外的列数
Constants.OTHER_COLUMN_NUM = 8
// 第一列假别的列数
Constants.FIRST_VACATION_HEADER = 4
// 每个假别占多少列
Constants.VACATION_SORT_NUMBER = 3
Ext.QuickTips.init();
Ext.onReady(function() {
    // 保存grid初始化时的假别一共多少列
    var vacationTypeLength = 0;
    // 保存原始grid数据信息
    var objGridInitDatas = [];
    
    // 当前人员所在部门data
    var empDeptData =  new Ext.data.Record.create([{
        name : 'deptName'
    },{
        name : 'deptId'
    }]);
    // 当前人员所在部门store
    var baseInfoStore = new Ext.data.JsonStore({
        url : 'ca/getEmpOfDeptRecords.action',
        root : 'list',              
        fields : empDeptData   
    });
    var baseLoad = false;

    // 是否选择下级部门
    var check = new Ext.form.Checkbox({
        boxLabel : '包括下级部门 ',
        inputValue : Constants.CHECKED_VALUE
    })
    check.setValue(true);
    // 查询按钮
    var queryBtn = new Ext.Button({
        id : 'query',
        text : Constants.BTN_QUERY,
        iconCls : Constants.CLS_QUERY,
        handler : searchYearPlan
    });
    // 保存按钮
    var saveBtn = new Ext.Button({
        id : 'save',
        text : Constants.BTN_SAVE,
        iconCls : Constants.CLS_SAVE,
        handler : saveHander
    });
    // 上报按钮
    var reportBtn = new Ext.Button({
        id : 'report',
        text : Constants.BTN_REPOET,
        iconCls : Constants.CLS_REPOET,
        handler : reportHander
    });
    // 系统当前日期
    var sysDate = new Date();
    sysDate = sysDate.format('Y');
    // 年度选择
    var txtYear = new Ext.form.TextField({
        id : 'year',
        name : 'year',
        style : 'cursor:pointer',
        width : 80,
        value : sysDate,
        readOnly : true
    });
    txtYear.onClick(function() {
        WdatePicker({
            startDate : '%y',
            dateFmt : 'yyyy',
            alwaysUseStartDate : false,
            isShowClear : false,
            isShowToday : false
        });

    })
    // 部门选择
    var txtDept = new Ext.form.TextField({
        fieldLabel : '部门',
        width : 100,
        valueField : 'id',
        hiddenName : 'dept',
        maxLength : 100,
        anchor : '100%',
        readOnly : true
    });
    txtDept.onClick(function() {
        var args = {
            selectModel : 'single',
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
            txtDept.setValue(dept.names);
            hiddenMrDept.setValue(dept.ids);
        }
    })
    // 隐藏域部门ID
    var hiddenMrDept = new Ext.form.Hidden({
        hiddenName : 'deptId'
    })
    // 头标题
    var headTbar = new Ext.Toolbar({
        region : 'north',
        border : false,
        height : 25,
        items : ["年度<font color='red'>*</font>:", txtYear, "-", "部门:", txtDept,"-", check, "&nbsp", "-", queryBtn,
            saveBtn, reportBtn]
    });
   

    // grid主体
    var grid1 = new Ext.grid.EditorGridPanel({
        id:'grid',
        enableColumnMove : false,
        autoSizeColumns : true,
        autoSizeHeaders : false,
        renderTo : document.body,
        height : 400,
        width : 500,
        // 单击修改
        clicksToEdit : 1,
        // 单选
        sm : new Ext.grid.RowSelectionModel({
            singleSelect : true
        }),
        cm : new Ext.grid.ColumnModel([]),
        // 响应editGrid的编辑后事件，改变盈亏数量
        listeners : {
            // 编辑后触发事件
            afteredit : function(e) {
                // 获取当前记录
                var record = e.grid.getStore().getAt(e.row);
                // 获得标注考勤时间
                var recordStandardTime = record.get("standardTime");
                // 计算最新时长 e.value 当前天数
                var hoursValue = e.value * recordStandardTime;
                // 得到后一列时长的列标题
                var hoursHeader = e.grid.getColumnModel().getDataIndex(e.column + 1);
                // 设置时长
                record.set(hoursHeader, hoursValue.toFixed(1));
            },
            beforeedit : function(e) {
                var record = e.grid.getStore().getAt(e.row);
                // 获得审批状态的值
                var signStateValue = record.get("signState");
                // 若审批状态为已上报或已终结，则不允许编辑
                if ( signStateValue == Constants.ALREADY_REPORT || signStateValue == Constants.ALREADY_OVER ) {
                    e.cancel = true;
                    return;
                }
                if ( editForDateFlag != true ) {
                    e.cancel = true;
                    return;
                }
            }
        },
        store : new Ext.data.JsonStore({
            url : 'ca/searchYearPlanList.action'
        }),
        tbar : headTbar,
        plugins : [new Ext.ux.plugins.GroupHeaderGrid({
            rows : []
        })]
    });

    grid1.getStore().on('metachange', function(store, meta) {
        grid1.getColumnModel().rows.length = 0;
        if (meta.rows) {
            // 若有双层表头,则设置
            grid1.getColumnModel().rows.push(meta.rows);
        }
        // 记录下假别一共多少列
        vacationTypeLength = meta.fields.length - Constants.OTHER_COLUMN_NUM;
        // 添加editor,renderer属性
        var item;
        for (var i = 0; i < meta.fields.length; i++) {
            item = meta.fields[i];
            item.dataIndex = item.name;
            var test = item.name;
            if (item.editor) {
                // 解析editor
                item.editor = new Ext.form.NumberField({
                    allowNegative : false,
                    isFormField : true,
                    decimalPrecision : 2,
                    maxValue : 9999999999.99,
                    maxLength : 19,
                    allowBlank : false,
                    style : 'text-align:right',
                    onRender : function(ct, position) {
                        Ext.form.NumberField.superclass.onRender.call(this, ct, position);
                        // 限制允许输入最大长度
                        if (this.maxLength && this.maxLength != Number.MAX_VALUE) {
                            this.el.dom.maxLength = this.maxLength;
                        }
                    }
                });
            }
            if (item.renderer) {
                // 解析renderer
                item.renderer = eval('(' + item.renderer + ')');
            }
            if(item.css ) {
            	item.css = eval('(' + item.css + ')');
            }
        }
        grid1.getColumnModel().setConfig(meta.fields, true);
        grid1.getColumnModel().setColumnHeader(0, '行号');
    });
    
    // 初始化数据
    baseLoadInit();
    function baseLoadInit() {
        baseInfoStore.load({
            callback : function() {
                if (baseInfoStore.getCount() > 0) {
                    // 设置部门
                    var recordEmpDept = baseInfoStore.getAt(0);
                    // 设置部门值
                    txtDept.setValue(recordEmpDept.get("deptName"));
                    hiddenMrDept.setValue(recordEmpDept.get("deptId"));
                    baseLoad = true;
                    if (baseLoad) {
                    	// 初始化grid
                        loadDataInit();
                    }
                } else {
                    // 初始化grid
                    loadDataInit();
                }
            }
        });
    }
    
    //创建等待读取数据时的等待框
    var mask=new Ext.LoadMask(Ext.getBody(),{msg:'Loading...'});
    mask.show();    //显示

    // 控制天数是否可编辑
    var editForDateFlag = true;
    grid1.getStore().on('load', function() {
        if (grid1.getStore().getCount() == 0) {
            Ext.Msg.alert(Constants.REMIND, Constants.COM_I_003);
        }
        if (Ext.get('year').dom.value != sysDate) {
            // 点击查询后且年份不是当前年份的情况下
        	editForDateFlag = false;
        } else {
        	editForDateFlag = true;
        }
        if (Ext.get('year').dom.value == sysDate && grid1.getStore().getCount() > 0) {
            Ext.get('save').dom.disabled = false;
            Ext.get('report').dom.disabled = false;
        } else {
            Ext.get('save').dom.disabled = true;
            Ext.get('report').dom.disabled = true;
        }
       
        // 保存grid原始数据
        objGridInitDatas = getRecordInitList(true);
        mask.hide();
    })
    // 设定布局器及面板
    new Ext.Viewport({
        enableTabScroll : true,
        layout : "border",
        items : [{
                xtype : "panel",
                region : 'center',
                layout : 'fit',
                border : false,
                items : [grid1]
            }]
    });

    /**
     * 按条件搜索
     */
    function searchYearPlan() {
        // 判断grid中的数据有没有修改
        if (isGridChanged()) {
            // 数据有变动时
            Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_004, function(buttonobj) {
                if (buttonobj == "yes") {
                    // 创建等待读取数据时的等待框
                    var mask = new Ext.LoadMask(Ext.getBody(), {
                        msg : 'Loading...'
                    });
                    mask.show(); // 显示
                    // 画面初始化
                    loadData();
                }
            });
        } else {
            // 创建等待读取数据时的等待框
            var mask = new Ext.LoadMask(Ext.getBody(), {
                msg : 'Loading...'
            });
            mask.show(); //显示
            // 若数据无变化，画面初始化
            loadData();
        }
    }
    /**
     * 保存数据
     */
    function saveHander() {
        Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_001, function(buttonobj) {
            if (buttonobj == "yes") {
                // 保存
                saveInfo();
            }
        });
    }
    /**
     * 保存年初计划登记信息
     */
    function saveInfo() {
        Ext.Msg.wait("正在保存数据,请等候...", "Please Wait");
        Ext.Ajax.request({
            method : 'POST',
            url : 'ca/saveYearPlanByVacation.action',
            // 保存成功
            success : function(result, request) {
                // 重新加载数据
                loadData();
                Ext.Msg.alert(Constants.REMIND, Constants.COM_I_004);
            },
            // 保存失败
            failure : function() {
                Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
            },
            params : {
                // 向后台传递参数
                colmunNum : vacationTypeLength/3,
                // 得到grid中所有的数据
                updateRecords : Ext.util.JSON.encode(getRecordInitList(true)),
                // 获得年份
                year : sysDate
            }
        });
    }
    /**
     * 上报数据
     */
    function reportHander() {
        Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_006, function(buttonobj) {
            if (buttonobj == "yes") {
                if (isGridChanged() || isNewCons()) {
                    Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_W_002, function(buttonobj) {
                        if (buttonobj == "yes") {
                            // 保存并上报
                            saveAndReport(1);
                        }
                    });
                } else {
                    saveAndReport(0);
                }
            }
        });
    }
    /***
     * 保存并上报数据
     */
    function saveAndReport(flag) {
        Ext.Msg.wait("正在上报数据,请等候...", "Please Wait");
        Ext.Ajax.request({
            method : 'POST',
            url : 'ca/reportYearPlanRecords.action',
            success : function(result, request) {
                // 重新加载数据
                loadData();
                Ext.Msg.alert(Constants.REMIND, Constants.COM_I_007);
            },
            failure : function() {
                Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
            },
            params : {
                // 向后台传递参数
                colmunNum : vacationTypeLength/3,
                // 得到grid中所有的数据
                updateRecords : Ext.util.JSON.encode(getRecordInitList(true)),
                // 获得年份
                year : sysDate,
                // 0上报，1保存并上报
                reportFlag : flag
            }
        });
    }
    /**
     * 判断有没有处于new的状态的数据
     */
    function isNewCons() {
        // 循环读取记录
        for (var index = 0; index < grid1.getStore().getCount(); index ++) {
            // 读取每条记录
            var record = grid1.getStore().getAt(index).data;
            // 察看是否存在新记录
            if (record.isNew == "true") {
                // 存在返回true
                return true;
            }
        }
        return false;
    }
    /**
     * 判断grid中的数据有没有改变
     */
    function isGridChanged() {
        // 读取现在grid的数据
        var newRecs = getRecordInitList(true);
        sortByEmpId(newRecs);
        sortByEmpId(objGridInitDatas)
        
        // 遍历每条记录
        for (var i = 0; i < newRecs.length; i++) {
            // 比较每条记录的天数是否发生了变化
            for (j = 0; j < vacationTypeLength / 3; j++) {
                // 比较新旧记录的天数值，如果不一样则返回true
                if (newRecs[i]['days'+j] != objGridInitDatas[i]['days'+j]) {
                    return true
                };
            }
        }
        // 数据没有发生变化，返回false
        return false;
    }

    /**
     * 初始化grid
     */
    function loadDataInit() {
        grid1.getStore().load({
            params : {
                // 年份
                year : txtYear.getValue(),
                // 部门id
                deptId : hiddenMrDept.getValue(),
                // 是否选择子部门标志
                selectP : check.getValue()
            }
        });
    }
    /**
     * 读取数据源
     */
    function loadData() {
        var flag = false;
        if (check.checked == true) {
            check.setValue(true);
            flag = true;
        }
        grid1.getStore().load({
            params : {
                // 年份
                year : txtYear.getValue(),
                // 部门id
                deptId : hiddenMrDept.getValue(),
                // 是否选择子部门标志
                selectP : check.checked
            }

        });
    }
    
    /**
     * 
     * 获取grid中的数据
     */
    function getRecordInitList(isAll) {
        var records = new Array();
        // 循环
        for (var index = 0; index < grid1.getStore().getCount(); index++) {
            var record = grid1.getStore().getAt(index).data;
            // 所有记录
            records.push(copyAllRecords(record));
        }
        return records;
    }
    
    /**
     * 
     * 拷贝grid中所有数据信息
     * 
     */
    function copyAllRecords(record) {
        var index = 0;
        var objClone = new Object();
        // 取得除行号一列的所有的值
        var columnNum = Constants.OTHER_COLUMN_NUM+vacationTypeLength;
        for (i = 1; i < Constants.OTHER_COLUMN_NUM+vacationTypeLength ; i++) {
            // 得到每列的header
            var fieldName = grid1.getColumnModel().getDataIndex(i);
            // 记录所有天数的原始值
            objClone[fieldName] = record[fieldName];
        }
        return objClone;
    }
    
    /**
     * 转化审批状态
     */
    function statesFormat(value) {
        switch (value) {
            case '0' : {
                return Constants.NOT_REPORT_MESSAGE;
                break;
            }
            case '1' : {
                return Constants.ALREADY_REPORT_MESSAGE;
                break;
            }
            case '2' : {
                return Constants.ALREADY_OVER_MESSAGE;
                break;
            }
            case '3' : {
                return Constants.ALREADY_RETURN_MESSAGE;
                break;
            }
            default : {
                return;
            }
        }
    }
  
    /**
     * 按照人员id排序
     */
    function sortByEmpId(records){
        var intLen = records.length;
        var temp = null;
        for(var i= intLen -1; i > 0; i--){
            for(var j = 0; j < i; j++){
                if(records[j].empId > records[j + 1].empId) {
                    temp = records[j];
                    records[j] = records[j + 1];
                    records[j + 1] = temp;
                }
            }
        }
    }
    
    /** 设置行号不可排序，且不随其他列而变化*/
    function numbererW(v, p, record, rowIndex){
        if(this.rowspan){
            p.cellAttr = 'rowspan="'+this.rowspan+'"';
        }
        return rowIndex+1;
    }
    /**
     * 三位一隔，并保留2位小数
     */
    function numberFormat2(value) {
        value = String(value);
        // 整数部分
        var whole = value;
        if (whole == null || whole == "") {
            whole = "0";
        }
        // 小数部分
        var sub = ".00";
        // 如果有小数
        if (value.indexOf(".") > 0) {
            whole = value.substring(0, value.indexOf("."));
            sub = value.substring(value.indexOf("."), value.length);
            sub = sub + "00";
            if (sub.length > 3) {
                sub = sub.substring(0, 3);
            }
        }
        var r = /(\d+)(\d{3})/;
        while (r.test(whole)) {
            whole = whole.replace(r, '$1' + ',' + '$2');
        }
        v = whole + sub;
        return v;
    }
    /**
     * 三位一隔，并保留1位小数
     */
    function numberFormat1(value) {
        value = String(value);
        // 整数部分
        var whole = value;
        if (whole == null || whole == "") {
            whole = "0";
        }
        // 小数部分
        var sub = ".0";
        // 如果有小数
        if (value.indexOf(".") > 0) {
            whole = value.substring(0, value.indexOf("."));
            sub = value.substring(value.indexOf("."), value.length);
            sub = sub + "0";
            if (sub.length > 2) {
                sub = sub.substring(0, 2);
            }
        }
        var r = /(\d+)(\d{3})/;
        while (r.test(whole)) {
            whole = whole.replace(r, '$1' + ',' + '$2');
        }
        v = whole + sub;
        return v;
    }
});