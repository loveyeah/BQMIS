/**
 * 员工调动查询（员工调动查询tab）
 * @author 黄维杰
 * @since 2009-02-17
 */
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function(){
    // 自定义函数    
    var empMoveQuery = parent.Ext.getCmp('tabPanel').empMoveQuery;
    /** 选择部门（1.调动前部门；2.调动后部门） */
    var deptChoose = "";
    
    /** 查询条件Object */
    var queryObj = new Object();
    
    /** 开始时间选择 */
    var startDate = new Ext.form.TextField({
        id : 'startDate',
        style : 'cursor:pointer',
        readOnly : true,
        width : 100,
        listeners : {
            focus : function() {
                WdatePicker({
                    startDate : '%y-%M-%d',
                    dateFmt : 'yyyy-MM-dd',
                    alwaysUseStartDate : false
                });
                this.blur();
            }
        }
    });
    
    /** 结束时间选择 */
    var endDate = new Ext.form.TextField({
        id : 'endDate',
        style : 'cursor:pointer',
        readOnly : true,
        width : 100,
        listeners : {
            focus : function() {
                WdatePicker({
                    startDate : '%y-%M-%d',
                    dateFmt : 'yyyy-MM-dd',
                    alwaysUseStartDate : false
                });
                this.blur();
            }
        }
    });
    
    /** 单据状态 */
    var statusField = new Ext.form.CmbHRCode({
        fieldLabel : '单据状态',
        type : "单据状态",
        width : 75,
        hidden : true,
        activeitem : 0
    });
    
    /** 调动前部门 */
    var deptBFTxt = new Ext.form.TextField({
        id : 'deptBFTxt',
        width : 100,
        allowBlank : true,
        readOnly : true
    });
    
    /** 调动前部门Code */
    var hidDeptBFTxt = {
        id : 'deptBFCode',
        xtype : 'hidden',
        value : '',
        readOnly : true,
        hidden : true
    };
    
    /** 调动后部门 */
    var deptAFTxt = new Ext.form.TextField({
        id : 'deptAFTxt',
        width : 100,
        allowBlank : true,
        readOnly : true
    });
    
    /** 调动后部门Code */
    var hidDeptAFTxt = {
        id : 'deptAFCode',
        xtype : 'hidden',
        value : '',
        readOnly : true,
        hidden : true
    };
    
    /** 选择部门 */
    deptBFTxt.onClick(function() {
        deptChoose = 1;
        deptSelect();
    });
    deptAFTxt.onClick(function() {
        deptChoose = 2;
        deptSelect();
    });
    
    /** 查询按钮 */
    var queryBtn = new Ext.Button({
        text : Constants.BTN_QUERY,
        iconCls : Constants.CLS_QUERY,
        handler : queryHandler
    });
    
    /** 打印按钮 */
    var printBtn = new Ext.Button({
        text : Constants.BTN_PRINT,
        iconCls : Constants.CLS_PRINT,
        disabled : true,
        handler : printHandler
    });
    
    /** 导出按钮 */
    var exportBtn = new Ext.Button({
        text : Constants.BTN_EXPORT,
        iconCls : Constants.CLS_EXPORT,
        disabled : true,
        handler : exportHandler
    });
    
    /** 查询条件Toolbar */
    var queryTbar = new Ext.Toolbar({
        region : 'north',
        border : true,
        height : 25,
        items : ['调动日期:', startDate, '~', endDate, 
                  statusField,'-',
                  '调动前部门:', deptBFTxt, hidDeptBFTxt, 
                  '调动后部门:', deptAFTxt, hidDeptAFTxt
//                  ,
//                  '合同有效期:', contractTermCbo, '-',
//                  '终止类别:', stopTypeCbo
          ]
    });
    
    /** 按钮Toolbar */
    var btnTbar = new Ext.Toolbar({
        region : 'center',
        border : true,
        height : 25,
        items : [queryBtn, printBtn, exportBtn]
    });
    
    /** panel */
    var panelTbar = new Ext.Panel({
        region : 'north',
        border : false,
        height : 51,
        layout : 'border',
        items : [queryTbar, btnTbar]
    });
    
    /** 定义grid中的数据 */
    var gridData = new Ext.data.Record.create([{
        // 员工姓名
        name : 'chsName'
    }, {
        // 调动日期
        name : 'removeDate'
    }, {
        // 起薪日期
        name : 'do2Date'
    }, {
        // 调动通知单号
        name : 'requisitionNo'
    }, {
        // 调动前部门
        name : 'deptNameFirst'
    }, {
        // 调动后部门
        name : 'deptNameSecond'
    }, {
        // 调动前岗位
        name : 'stationNameBefore'
    }, {
        // 调动后岗位
        name : 'stationNameAfter'
    }, {
        // 调动前岗位级别
        name : 'stationLevelNameBefore'
    }, {
        // 调动后岗位级别
        name : 'stationLevelNameAfter'
    }, {
        // 单据状态
        name : 'dcmState'
    }, {
        // 备注
        name : 'memoRemove'
    }]);
    
    /**  */
    var gridStore = new Ext.data.JsonStore({
        url : 'hr/getEmpMoveList.action',
        root : 'list',
        totalProperty : 'totalCount',
        fields : gridData
      });
      
      /** 一览grid */
      var grid = new Ext.grid.GridPanel({
          autoWidth : true,
          store : gridStore,
          region: 'center',
          sm : new Ext.grid.RowSelectionModel({
              // 单选
              singleSelect : true
          }),
          columns : [
              new Ext.grid.RowNumberer({
                  header : '行号',
                  width : 35
              }), {
                  header : '员工姓名',
                  width : 80,
                sortable : true,
                dataIndex : 'chsName'
              }, {
                  header : '调动日期',
                  width : 80,
                sortable : true,
                dataIndex : 'removeDate'
              }, {
                  header : '起薪日期',
                  width : 80,
                sortable : true,
                dataIndex : 'do2Date'
              }, {
                  header : '调动通知单号',
                  width : 100,
                sortable : true,
                dataIndex : 'requisitionNo'
              }, {
                  header : '调动前部门',
                  width : 150,
                sortable : true,
                dataIndex : 'deptNameFirst'
              }, {
                  header : '调动后部门',
                  width : 150,
                sortable : true,
                dataIndex : 'deptNameSecond'
              }, {
                  header : '调动前岗位',
                  width : 100,
                sortable : true,
                dataIndex : 'stationNameBefore'
              }, {
                  header : '调动后岗位',
                  width : 100,
                sortable : true,
                dataIndex : 'stationNameAfter'
              }, {
                  header : '调动前岗位级别',
                  width : 100,
                sortable : true,
                dataIndex : 'stationLevelNameBefore'
              }, {
                  header : '调动后岗位级别',
                  width : 100,
                sortable : true,
                dataIndex : 'stationLevelNameAfter'
              }, {
                  header : '单据状态',
                  width : 80,
                  hidden : true,
                sortable : true,
                dataIndex : 'dcmState'
              }, {
                  header : '备注',
                  width : 200,
                sortable : true,
                dataIndex : 'memoRemove'
              }],
          bbar : new Ext.PagingToolbar({
            pageSize : Constants.PAGE_SIZE,
            store : gridStore,
            displayInfo : true,
            displayMsg : Constants.DISPLAY_MSG,
            emptyMsg : Constants.EMPTY_MSG
        }),
        viewConfig : {
            forceFit : false
        },
        frame : false,
        border : false,
        enableColumnHide : true,
        enableColumnMove : false
      });
      
      // 响应备注栏的双击弹出详细查看窗口
      grid.on("celldblclick", function(grid, rowIndex, columnIndex, e){
        // 获取当前记录
        var record = gridStore.getAt(rowIndex);
        // 编辑列的字段名
        var fieldName = grid.getColumnModel().getDataIndex(columnIndex);
        if("memoRemove" == fieldName) {
          empMoveQuery.showWin(record.get("memoRemove"));
        }
      })
    
      // 设定布局器及面板
    new Ext.Viewport({   
        enableTabScroll : true,
        layout : "border",
        items : [panelTbar, grid]
    });
    
    /**
     * 选择部门
     */
    function deptSelect() {
    	empMoveQuery.deptSelect();
    			// 根据返回值设置画面的值
		if(deptChoose == "1") {
			deptBFTxt.setValue(empMoveQuery.name);
			Ext.get("deptBFCode").dom.value = empMoveQuery.id;
		} else if(deptChoose == "2") {
			deptAFTxt.setValue(empMoveQuery.name);
			Ext.get("deptAFCode").dom.value = empMoveQuery.id;
		}
    }
    
    /**
     * 查询处理
     */
    function queryHandler() {
        // 查询基本条件
        gridStore.baseParams = {
            startDate : startDate.getValue(),
            endDate : endDate.getValue(),
            dcmState : statusField.getValue(),
            deptBFCode : Ext.get("deptBFCode").dom.value,
            deptAFCode : Ext.get("deptAFCode").dom.value
          };
          // 保存查询条件
          queryObj.startDate = startDate.getValue();
          queryObj.endDate = endDate.getValue();
        queryObj.dcmState = statusField.getValue(),
          queryObj.deptBFCode = Ext.get("deptBFCode").dom.value;
          queryObj.deptAFCode = Ext.get("deptAFCode").dom.value;
          // 查询
        gridStore.load({
            params : {
                start : 0,
                limit : Constants.PAGE_SIZE
            },
            callback : function() {
                if(gridStore.getCount() == 0) {
                    printBtn.setDisabled(true);
                    exportBtn.setDisabled(true);
                    Ext.Msg.alert(Constants.REMIND, Constants.COM_I_003);
                } else {
                    printBtn.setDisabled(false);
                    exportBtn.setDisabled(false);
                }
            }
        });
    }
    
    /**
     * 打印处理
     */
    function printHandler() {
        window.open("../../../../report/webfile/hr/PE007_workerRemove.jsp?docStatus="
                + queryObj.dcmState
                + "&removeDateFrom=" + queryObj.startDate
                + "&removeDateTo=" + queryObj.endDate
                + "&removeDeptFrom=" + queryObj.deptBFCode
                + "&removeDeptTo=" + queryObj.deptAFCode);
    }
    /**
     * 导出处理
     */
    function exportHandler() {
        Ext.Msg.confirm(Constants.CONFIRM,
                Constants.COM_C_007, function(buttonobj) {
                    if (buttonobj == 'yes') {
                        var urlAction = "hr/removeExportFile.action";
                        document.all.blankFrame.src = urlAction;
                    }
                })
    }
});