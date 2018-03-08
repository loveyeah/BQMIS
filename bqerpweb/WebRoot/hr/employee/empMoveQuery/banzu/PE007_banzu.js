/**
 * 员工调动查询（班组调动查询tab）
 * @author 黄维杰
 * @since 2009-02-13
 */
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function(){
    // 自定义函数    
    var empMoveQuery = parent.Ext.getCmp('tabPanel').empMoveQuery;
    /** 选择班组（1.调动前班组；2.调动后班组） */
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
    
    // 调动前班组
    var drpOldDept = new Ext.form.CmbHRBussiness({
        fieldLabel : '调动前班组',
        type : "调动部门",
        width : 120,
        allowBlank : true,
        id : 'drpOldDept',
        hiddenName : 'oldDeptId'
    })
    // 调动后班组
    var drpNewDept = new Ext.form.CmbHRBussiness({
        fieldLabel : '调动前班组',
        type : "调动部门",
        width : 120,
        allowBlank : true,
        id : 'drpNewDept',
        hiddenName : 'newDeptId'
    })
    
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
        items : ['调动日期:', startDate, '~', endDate, '-',
                  '调动前班组:',drpOldDept,
                  '调动后班组:',drpNewDept
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
        // 调动前班组
        name : 'deptNameFirst'
    }, {
        // 调动后班组
        name : 'deptNameSecond'
    }, {
        // 执行日期
        name : 'doDate'
    }, {
        // 备注
        name : 'memoRemove'
    }]);
    
    /**  */
    var gridStore = new Ext.data.JsonStore({
        url : 'hr/getBanZuList.action',
        root : 'list',
        totalProperty : 'totalCount',
        fields : gridData
      });
      
      /** pagingbar */
      var pgBar = new Ext.PagingToolbar({
            pageSize : Constants.PAGE_SIZE,
            store : gridStore,
            displayInfo : true,
            displayMsg : Constants.DISPLAY_MSG,
            emptyMsg : Constants.EMPTY_MSG
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
                  header : '调动前班组',
                  width : 140,
                sortable : true,
                dataIndex : 'deptNameFirst'
              }, {
                  header : '调动后班组',
                  width : 140,
                sortable : true,
                dataIndex : 'deptNameSecond'
              }, {
                  header : '执行日期',
                  width : 80,
                sortable : true,
                dataIndex : 'doDate'
              }, {
                  header : '备注',
                  width : 180,
                sortable : true,
                dataIndex : 'memoRemove'
              }],
          bbar : pgBar,
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
     * 查询处理
     */
    function queryHandler() {
        // 查询基本条件
        gridStore.baseParams = {
            startDate : startDate.getValue(),
            endDate : endDate.getValue(),
            deptBFCode : drpOldDept.getValue(),
            deptAFCode : drpNewDept.getValue()
          };
          // 保存查询条件
          queryObj.startDate = startDate.getValue();
          queryObj.endDate = endDate.getValue();
          queryObj.deptBFCode = drpOldDept.getValue(),
          queryObj.deptAFCode = drpNewDept.getValue()
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
        window.open("../../../../report/webfile/hr/PE007_stationRemove.jsp?removeDateFrom=" + queryObj.startDate
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
                        var urlAction = "hr/banzuExportFile.action";
                        document.all.blankFrame.src = urlAction;
                    }
                })
    }
});