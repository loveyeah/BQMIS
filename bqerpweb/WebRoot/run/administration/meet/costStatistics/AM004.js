Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
    Ext.QuickTips.init();
    Ext.override(Ext.grid.GridView, {
            // 重写doRender方法
            doRender : function(cs, rs, ds, startRow, colCount, stripe){
                var ts = this.templates, ct = ts.cell, rt = ts.row, last = colCount-1;
                var tstyle = 'width:'+this.getTotalWidth()+';';
                // buffers
                var buf = [], cb, c, p = {}, rp = {tstyle: tstyle}, r;
                for(var j = 0, len = rs.length; j < len; j++){
                    r = rs[j]; cb = [];
                    var rowIndex = (j+startRow);
                    for(var i = 0; i < colCount; i++){
                        c = cs[i];
                        p.id = c.id;
                        p.css = i == 0 ? 'x-grid3-cell-first ' : (i == last ? 'x-grid3-cell-last ' : '');
                        p.attr = p.cellAttr = "";
                        // 如果该行是统计行并且改列是第一列
                        if(r.data["countType"] == "total"&&i==0){
                            p.value = "合计";
                        }else{
                            p.value = c.renderer(r.data[c.name], p, r, rowIndex, i, ds);
                        }
                        p.style = c.style;
                        if(p.value == undefined || p.value === "") p.value = "&#160;";
                        if(r.dirty && typeof r.modified[c.name] !== 'undefined'){
                            p.css += ' x-grid3-dirty-cell';
                        }
                        cb[cb.length] = ct.apply(p);
                    }
                    var alt = [];   
                    if(stripe && ((rowIndex+1) % 2 == 0)){
                        alt[0] = "x-grid3-row-alt";
                    }
                    if(r.dirty){
                        alt[1] = " x-grid3-dirty-row";
                    }
                    rp.cols = colCount;
                    if(this.getRowClass){
                        alt[2] = this.getRowClass(r, rowIndex, rp, ds);
                    }
                    rp.alt = alt.join(" ");
                    rp.cells = cb.join("");
                    buf[buf.length] =  rt.apply(rp);
                }
                return buf.join("");
            }
        });
    /**
     * 按条件查询
     */
    function findByFuzzy(){
        if(checkTime()){
            storeMain.baseParams = {
                    dteStartDate : meetDateForm.getValue(),
                    dteEndDate : meetDateTo.getValue()
                };
            storeMain.load();
        }
    }
    /**
     * 查询结束时插入统计行
     */
    function addLine() {
        // 统计行
        var record = new recordMain({

            countType : "total"
        });
        // 原数据个数
        var count = storeMain.getCount();
        storeMain.insert(count, record);
        gridMain.getView().refresh();
    };
    /**
     * 导出按钮按下时 
     */
    function exportOut()
    {
        // 弹出窗口
        Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG,MessageConstants.COM_C_007,
            function(buttonobj) {
                // 如果选是的话，调用共同导出方法
                if (buttonobj == "yes") {
                    document.all.blankFrame.src = "administration/exportMeetCostFile.action";
                }
            })
    }

    /**
     * 开始时间与结束时间比较
     */
    function checkTime() {
        var startDate = meetDateForm.getValue();
        var endDate = meetDateTo.getValue();
        if (startDate != "" && endDate != "") {
            var res = compareDateStr(startDate, endDate);
            if (res) {
                Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, String.format(
                                MessageConstants.COM_E_009, "开始日期", "结束日期"));
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
     * 小数格式化
     */
    function renderNumber(v, argDecimal) {
        if (v) {
            if (typeof argDecimal != 'number') {
                argDecimal = 2;
            }
            v = Number(v).toFixed(argDecimal);
            var t = '';
            v = String(v);
            while ((t = v.replace(/^(-?[0-9]+)([0-9]{3}.*)$/, '$1,$2')) !== v)
                v = t;
            
            return v+"元";
        } else
            return "0.00元";
    }
        // 系统当天日期
    var sd = new Date();
    var ed = new Date();
    // 系统当天前15天的日期
    sd.setDate(sd.getDate() - 15);
    sd = sd.format('Y-m-d');
    // 系统当天日期
    ed.setDate(ed.getDate()); 
    ed = ed.format('Y-m-d');
    // 开始时间
    var meetDateForm = new Ext.form.TextField({
        itemCls : 'sex-left',
        readOnly : true,
        clearCls : 'allow-float',
        checked : true,
        width : 108,
        value : sd,
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
    // 结束时间
    var meetDateTo = new Ext.form.TextField({
        readOnly : true,
        checked : true,
        width : 108,
        value : ed,
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
    // 查询按钮
    var btnQuery = new Ext.Toolbar.Button({
        text : Constants.BTN_QUERY,
        iconCls : Constants.CLS_QUERY,
        handler : findByFuzzy
    });
    // 导出按钮
    var btnLogout = new Ext.Toolbar.Button({
        text : Constants.BTN_EXPORT,
        disabled : true,
        iconCls : Constants.CLS_EXPORT,
        handler : exportOut
    });
    // 上部panel
    var headTbar = new Ext.Toolbar({
        region : 'north',
        border:false,
        items:['会议开始时间:','从', meetDateForm, '到', meetDateTo, '-', btnQuery,btnLogout]
    });
    // 数据集定义
    var recordMain = new Ext.data.Record.create([{
        // 申请单号
        name : 'meetId'
    },{
        // 申请部门名
        name : 'deptName'
    },{
        // 申请人名
        name : 'name'
    },{
        // 接待日期
        name : 'startMeetDate'
    },{
        // 会议名称
        name : 'meetName'
    },{
        // 就餐费用
        name : 'repastTotal'    
    },{
        // 用烟费用
        name : 'cigTotal'
    },{
        // 用酒费用
        name : 'wineTotal'
    },{
        // 住宿费用
        name : 'roomTotal'
    },{
        // 实际费用
        name : 'payout'
    },{
        // 超支
        name : 'balance'
    },{
        name : 'budpayInall'
    }]);
    var storeMain = new Ext.data.JsonStore({
            url : 'administration/getMeetCost.action',
            root : 'list',
            totalProperty : 'totalCount',
            fields : recordMain,
                            listeners : {
                    load : function(ds, records, o) {
                        if (storeMain.getCount() < 1) {
                            Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
                                    MessageConstants.COM_I_003);
                            btnLogout.disable();
                        } else {
                            btnLogout.enable();
                            addLine();
                        }
                    },
                    loadexception : function(ds, records, o) {
                        var o = eval("(" + o.responseText + ")");
                        if(o.msg!=null){
                            var succ = o.msg;
                            if (succ == Constants.SQL_FAILURE) {
                                Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
                                        MessageConstants.COM_E_014);
                            } else if (succ == Constants.IO_FAILURE) {
                                Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
                                        MessageConstants.COM_E_022);
                            } else if (succ == Constants.DATE_FAILURE){
                                Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
                                        MessageConstants.COM_E_019);
                            }
                        }
                    }

                }
        });
    var gridMain = new Ext.grid.GridPanel({
            region : "center",
            layout : 'fit',
            border : false,
            enableColumnMove : false,
            // 单选
            sm : new Ext.grid.RowSelectionModel({
                        singleSelect : true
                    }),
            store : storeMain,
            tbar : headTbar,
            columns : [new Ext.grid.RowNumberer({
                                header : "行号",
                                width : 35
                            }),
                    // 申请单号
                    {
                        header : "会议申请单",
                        width : 100,
                        sortable : true,
                        dataIndex : 'meetId'
                    },
                    // 申请人
                    {
                        header : "申请人",
                        width : 80,
                        sortable : true,
                        dataIndex : 'name'
                    },
                    // 申请部门
                    {
                        header : "申请部门",
                        dataIndex : "deptName",
                        sortable : true,
                        width : 100
                    },

                    // 就餐费用（元）
                    {
                        header : "就餐费用",
                        width : 100,
                        sortable : true,
                        align : 'right',
                        renderer:function(value, cellmeta, record, rowIndex,
                            columnIndex, store){    
                            // 如果不是最后一行
                            if (rowIndex < store.getCount() - 1) {
                                return renderNumber(value);
                            // 如果是最后一行    
							} else {
							     var totalSum = 0;
							     // 对该列除最后一个单元格以为求和
							     for (var i = 0; i < store.getCount() - 1; i++) {
							          totalSum +=store.getAt(i).get('repastTotal')*1;
							     }
							     return renderNumber(totalSum);
							}
                        },
                        dataIndex : 'repastTotal'
                    },
                    // 用烟费用（元）
                    {
                        header : "用烟费用",
                        width : 100,
                        sortable : true,
                        align : 'right',
                        renderer:function(value, cellmeta, record, rowIndex,
                            columnIndex, store){                            
                            // 如果不是最后一行
                            if (rowIndex < store.getCount() - 1) {
                                return renderNumber(value);                     
                            // 如果是最后一行   
                            } else {
                                var totalSum = 0;
                                // 对该列除最后一个单元格以为求和
                                for (var i = 0; i < store.getCount() - 1; i++) {
                                    totalSum += store.getAt(i).get('cigTotal')*1;
                                }
                                return renderNumber(totalSum);
                            }
                        },
                        dataIndex : 'cigTotal'
                    },
                    // 用酒费用（元）
                    {
                        header : "用酒费用",
                        width : 100,
                        sortable : true,
                        align : 'right',
                        renderer:function(value, cellmeta, record, rowIndex,
                            columnIndex, store){                            
                            // 如果不是最后一行
                            if (rowIndex < store.getCount() - 1) {
                                return renderNumber(value);                     
                            // 如果是最后一行   
                            } else {
                                var totalSum = 0;
                                // 对该列除最后一个单元格以为求和
                                for (var i = 0; i < store.getCount() - 1; i++) {
                                    totalSum += store.getAt(i).get('wineTotal')*1;
                                }
                                return renderNumber(totalSum);
                            }
                        },
                        dataIndex : 'wineTotal'
                    },
                    // 住宿费用（元）
                    {
                        header : "住宿费用",
                        width : 100,
                        sortable : true,
                        align : 'right',
                        renderer:function(value, cellmeta, record, rowIndex,
                            columnIndex, store){                            
                            // 如果不是最后一行
                            if (rowIndex < store.getCount() - 1) {
                                return renderNumber(value);                     
                            // 如果是最后一行   
                            } else {
                                var totalSum = 0;
                                // 对该列除最后一个单元格以为求和
                                for (var i = 0; i < store.getCount() - 1; i++) {
                                    totalSum += store.getAt(i).get('roomTotal')*1;
                                }
                                return renderNumber(totalSum);
                            }
                        },
                        dataIndex : 'roomTotal'
                    },
                    // 预算费用（元）
                    {
                        header : "预算费用汇总",
                        width : 100,
                        sortable : true,
                        align : 'right',
                        renderer:function(value, cellmeta, record, rowIndex,
                            columnIndex, store){ 
                            // 如果不是最后行
                            if (rowIndex < store.getCount() - 1) {
                                return renderNumber(value);
                            // 如果是最后一行   
                            } else {
                                totalSum = 0;
                                // 对该列除最后一个单元格以为求和
                                for (var i = 0; i < store.getCount() - 1; i++) {
                                    totalSum+=store.getAt(i).get('budpayInall')*1;
                                }
                                return renderNumber(totalSum);
                            }
                        },
                        dataIndex : 'budpayInall'
                    },                    
                    // 实际费用（元）
                    {
                        header : "实际费用汇总",
                        width : 100,
                        sortable : true,
                        align : 'right',
                        renderer:function(value, cellmeta, record, rowIndex,
                            columnIndex, store){ 
                            // 如果不是最后行
                            if (rowIndex < store.getCount() - 1) {
                                return renderNumber(value);
                            // 如果是最后一行   
                            } else {
                                totalSum = 0;
                                // 对该列除最后一个单元格以为求和
                                for (var i = 0; i < store.getCount() - 1; i++) {
                                    totalSum+=store.getAt(i).get('payout')*1;
                                }
                                return renderNumber(totalSum);
                            }
                        },
                        dataIndex : 'payout'
                    },
                    // 是否超支
                    {
                        header : "是否超支",
                        width : 80,
                        sortable : true,
                        renderer: function(v){
                            if(v>0){
                                return "是"
                            }else if(v<=0){
                                return "否"
                            }else{
                                return ""
                            }
                        },
                        dataIndex : 'balance'
                    },
                    // 会议开始时间
                    {
                        header : "会议开始时间",
                        width : 100,
                        sortable : true,
                        dataIndex : 'startMeetDate'
                    },
                    // 会议名称
                    {
                        header : "会议名称",
                        width : 100,
                        sortable : true,
                        dataIndex : 'meetName'
                    }
                    ]
        });
    // 禁止选中最后行
    gridMain.on('rowmousedown', function(grid, rowIndex, e) {
        if (rowIndex<storeMain.getCount()-1) {
            return true;
        }
        return false;
    });
    var layout = new Ext.Viewport({
                layout : 'fit',
                margins : '0 0 0 0',
                border : false,
                items : [gridMain]
            });
})
