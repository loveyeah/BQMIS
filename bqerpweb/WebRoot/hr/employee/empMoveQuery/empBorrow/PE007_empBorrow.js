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
    
    /** 是否已回 */
    var ifBackField = new Ext.form.CmbHRCode({
        fieldLabel : '是否已回',
        type : "是否已回",
        width : 75,
        activeitem : 0
    });
    
    /** 单据状态 */
    var statusField = new Ext.form.CmbHRCode({
        fieldLabel : '单据状态',
        type : "单据状态",
        width : 75,
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
        items : ['借调日期:', startDate, '~', endDate, '-',
                '是否已回:', ifBackField,'-',
                  '单据状态:', statusField
          ]
    });
    
    /** 按钮Toolbar */
    var btnTbar = new Ext.Toolbar({
        region : 'center',
        border : true,
        height : 25,
        items : [
                  '所属部门:', deptBFTxt, hidDeptBFTxt, '-',
                  '借调部门:', deptAFTxt, hidDeptAFTxt, '-',
                  queryBtn, printBtn, exportBtn]
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
        // 员工工号
        name : 'empCode'
    },{
        // 员工姓名
        name : 'chsName'
    }, {
        // 所属部门
        name : 'deptNameFirst'
    }, {
        // 所在岗位
        name : 'stationNameBefore'
    }, {
        // 借调部门
        name : 'deptNameSecond'
    }, {
        // 开始日期
        name : 'startDate'
    }, {
        // 结束日期
        name : 'endDate'
    }, {
        // 是否已回
        name : 'ifBack'
    }, {
        // 单据状态
        name : 'dcmStatus'
    }, {
        // 备注
        name : 'memoBorrow'
    }]);
    
    /** Store数据集 */
    var gridStore = new Ext.data.JsonStore({
        url : 'hr/getEmpBorrowList.action',
        root : 'list',
        totalProperty : 'totalCount',
        fields : gridData
      });
      
      var checkColumn = new Ext.grid.CheckColumn({
                header : '已回',
                dataIndex : 'ifBack',
                width : 50,
                sortable : true
            });
      /** 一览grid */
      var grid = new Ext.grid.GridPanel({
          autoWidth : true,
          store : gridStore,
          plugins : [checkColumn],
          region: 'center',
          sm : new Ext.grid.RowSelectionModel({
              // 单选
              singleSelect : true
          }),
          columns : [
              new Ext.grid.RowNumberer({
                  header : '行号',
                  width : 35
              }),{
                header : '员工工号',
                  width : 80,
                sortable : true,
                dataIndex : 'empCode'
            }, {
                  header : '员工姓名',
                  width : 80,
                sortable : true,
                dataIndex : 'chsName'
              }, {
                  header : '所属部门',
                  width : 150,
                sortable : true,
                dataIndex : 'deptNameFirst'
              }, {
                  header : '所在岗位',
                  width : 100,
                sortable : true,
                dataIndex : 'stationNameBefore'
              }, {
                  header : '借调部门',
                  width : 150,
                sortable : true,
                dataIndex : 'deptNameSecond'
              }, {
                  header : '开始日期',
                  width : 80,
                sortable : true,
                dataIndex : 'startDate'
              }, {
                  header : '结束日期',
                  width : 80,
                sortable : true,
                dataIndex : 'endDate'
              }, checkColumn
              , {
                  header : '单据状态',
                  width : 80,
                sortable : true,
                dataIndex : 'dcmStatus'
              }, {
                  header : '备注',
                  width : 200,
                sortable : true,
                dataIndex : 'memoBorrow'
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
        if("memoBorrow" == fieldName) {
          empMoveQuery.showWin(record.get("memoBorrow"));
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
            ifBack : ifBackField.getValue(),
            dcmStatus : statusField.getValue(),
            deptBFCode : Ext.get("deptBFCode").dom.value,
            deptAFCode : Ext.get("deptAFCode").dom.value
          };
          // 保存查询条件
          queryObj.startDate = startDate.getValue();
          queryObj.endDate = endDate.getValue();
        queryObj.ifBack = ifBackField.getValue(),
        queryObj.dcmStatus = statusField.getValue(),
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
        window.open("../../../../report/webfile/hr/PE007_workerBorrow.jsp?"
                + "rentDateFrom=" + queryObj.startDate
                + "&rentDateTo=" + queryObj.endDate
                + "&isReturn=" + queryObj.ifBack
                + "&docStatus=" + queryObj.dcmStatus
                + "&rentDeptFrom=" + queryObj.deptBFCode
                + "&rentDeptTo=" + queryObj.deptAFCode);
    }
    /**
     * 导出处理
     */
    function exportHandler() {
        Ext.Msg.confirm(Constants.CONFIRM,
                Constants.COM_C_007, function(buttonobj) {
                    if (buttonobj == 'yes') {
                        var urlAction = "hr/borrowExportFile.action";
                        document.all.blankFrame.src = urlAction;
                    }
                })
        }
    
});

// 是否已回的checkbox控件
Ext.grid.CheckColumn = function(config) {
    Ext.apply(this, config);
    if (!this.id) {
        this.id = Ext.id();
    }
    this.renderer = this.renderer.createDelegate(this);
};

Ext.grid.CheckColumn.prototype = {
    init : function(grid) {
        this.grid = grid;
        this.grid.on('render', function() {
                    var view = this.grid.getView();
                }, this);
    },
    // 是则选中，否则不选中
    renderer : function(v, p, record) {
        v = v ? v : false;
        if (v != null) {
            if (v.toString() == "是") {
                v = true;
            } else if (v.toString() == "否") {
                v = false;
            }
        } else {
            v = false;
        }
        p.css += ' x-grid3-check-col-td';
        var sm = this.grid.getSelectionModel();
        return '<div class="x-grid3-check-col' + (v ? '-on' : '')
                + ' x-grid3-cc-' + this.id + '">&#160;</div>';
    }
};