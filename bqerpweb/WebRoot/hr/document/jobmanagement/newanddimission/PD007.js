Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
    Ext.QuickTips.init();
    
    // 总tabpanel
    var tabPanel = new Ext.TabPanel({
        renderTo : document.body,
        activeTab : 0,
        tabPosition : 'bottom',
        id : "tabPanel",
        plain : true,
        defaults : {
            autoScroll : true
        },
        frame : false,
        border : false,
        items : [/*{
            id : 'tabTotal',
            title : '统计',
            html : "<iframe name='tabTotal' src='hr/document/jobmanagement/newanddimission/total/PD007_total.jsp' style='width:100%;height:100%;border:0px;'></iframe>"
        },*/ {
            id : 'tabNew',
            title : '新进员工',
             html : "<iframe name='tabNew' src='hr/document/jobmanagement/newanddimission/new/newEmployeeQuery.jsp' style='width:100%;height:100%;border:0px;'></iframe>"
            //html : "<iframe name='tabNew' src='hr/document/jobmanagement/newanddimission/new/PD007_new.jsp' style='width:100%;height:100%;border:0px;'></iframe>"
        }, {
            id : 'tabDimission',
            title : '离职员工',
            html : "<iframe name='tabDimission' src='hr/document/jobmanagement/newanddimission/dimission/PD007_dimission.jsp' style='width:100%;height:100%;border:0px;'></iframe>"
        }]
    });

    // 设定布局器及面板
    var layout = new Ext.Viewport({
        layout : "border",
        border : false,
        items : [{
            region : 'center',
            layout : 'fit',
            border : false,
            split : true,
            collapsible : false,
            items : [tabPanel]
        }]
    });
    
    function ContractQuery() {
        // 开始日期
        this.startDate = "";
        // 结束日期
        this.endDate = "";
        // 部门编码
        this.deptCode = "";
        // 部门编码
        this.deptId = "";
        // 变更前部门编码
        this.deptBeforeCode = "";
        // 变更后部门编码
        this.deptAfterCode = "";
        // 合同期限
        this.contractTerm = "";
        // 合同形式
        this.contractType = "";
        // 合同到期月份
        this.duetoTime = "";
        // 终止类别
        this.stopType = "";
        // 部门名称
        this.name = "";
        // 部门ID
        this.id = "";
        
        // 查看附件
        ContractQuery.prototype.checkFile =
            function(contractId, fileOriger, mode) {
                var args = new Object();
                args.contractId = contractId;
                args.fileOriger = fileOriger;
                args.mode = mode;
                window.showModalDialog(
                    '../../../../hr/common/PC002.jsp',
                    args,
                    'dialogWidth=800px;dialogHeight=520px;center=yes;help=no;resizable=no;status=no;');
            }
            
        // 判断姓名和上一行是否相同
        ContractQuery.prototype.checkName =
            function checkName(value, cellmeta, record, rowIndex, columnIndex, store) {
                if(rowIndex == 0) {
                    return value;   
                } else {
                    var recordFirst = store.getAt(rowIndex - 1);
                    if(recordFirst.get("deptName") == record.get("deptName")) {
                        return "";
                    } else {
                        return value;   
                    }
                }
            }
            
        // 查看附件
        ContractQuery.prototype.invoice =
            function invoice(value, cellmeta, record) {
                if(value != "" && value != null) {
                    return "<a href='#' onclick='checkFile();return false;'>查看附件</a>";
                } else {
                    return "-";
                }
            }
            
        // 选择部门
        ContractQuery.prototype.deptSelect =
            function deptSelect() {
                var args = {
                    selectModel : 'single',
                    rootNode : {
                        id : '0',
                        text : '灞桥电厂'
                    }
                };
                // 调用画面
                var object = window.showModalDialog(
                                '../../../../comm/jsp/hr/dept/dept.jsp',
                                args,
                                'dialogWidth=' + Constants.WIDTH_COM_DEPT + 'px;' +
                                'dialogHeight=' + Constants.HEIGHT_COM_DEPT + 'px;' +
                                'center=yes;help=no;resizable=no;status=no;');
                // 根据返回值设置画面的值
                if (object) {
                    if (typeof(object.names) != "undefined") {
                        this.name = object.names;
                    }
                    if (typeof(object.ids) != "undefined") {
                        this.id = object.ids;
                    }
                }
            }
            
        // 弹出窗口，查看备注
        ContractQuery.prototype.showWin =
            function showWin(value) {
            // 备注
            var taShowMemo = new Ext.form.TextArea({
                id : "taShowMemo",
                maxLength : 250,
                width : 180,
                value : value,
                disabled : true
            });
            
            // 弹出画面
            var win = new Ext.Window({
                height : 170,
                width : 350,
                layout : 'fit',
                resizable : false,
                modal  : true,
                closeAction : 'hide',
                items : [taShowMemo],
                buttonAlign : "center",
                title : '详细信息查看窗口',
                buttons : [{
                    text : Constants.BTN_CLOSE,
                    iconCls : Constants.CLS_CANCEL,
                    handler : function() {
                        win.hide();
                    }
                }]
            });
            win.x=undefined;
            win.y=undefined;
            win.show();
            win.on('show', function() {
                taShowMemo.focus(true, 100);
            });
            win.on('hide', function() {
                taShowMemo.setValue("");
            });
        }
    }
    
    var contractQuery = new ContractQuery();
    Ext.getCmp('tabPanel').contractQuery = contractQuery; 
});
