Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
    Ext.QuickTips.init();   
    // ↓↓*******************事务作用及原因查询tab**************************************
     // 事务作用及原因store
    var transStore = new Ext.data.JsonStore({
        url : 'resource/getTransList.action',
        root : 'list',
        totalProperty : 'totalCount',
        fields : [
         // 事务编码
         {name:'transCode'},
        // 事务名称
         {name:'transName'},
        // 事务说明
         {name:'transDesc'},
          // 影响期初
         {name:'isOpenBalance'},
         // 影响接收
         {name:'isReceive'},
         // 影响调整
         {name:'isAdjust'},
         // 影响出货
         {name:'isIssues'},
         // 影响预留
         {name:'isReserved'},
         // 影响暂收
         {name:'isInspection'},
         // 影响销售金额
         {name:'isSaleAmount'},
         // 影响账目成本
         {name:'isEntryCost'},
         // 影响采购成本
         {name:'isPoCost'},
         // 影响调整成本
         {name:'isAjustCost'},
         // 影响实际成本
         {name:'isActualCost'},
         //  需要验证采购单
         {name:'isCheckPo'},
         // 影响采购单到货数量
         {name:'isPoQuantity'},
         // 影响工单
         {name:'isShopOrder'},
         // 是否需要验证工单
         {name:'isCheckShopOrder'},
         // 是否影响工单发料
         {name:'isShopOrderIssue'},
        // 流水号
         {name:'transId'}
            ]
        });
    // 载入数据
    transStore.load({params:{start: 0, limit : Constants.PAGE_SIZE}} );
    
    // 监听load事件
    // 当没有数据时确认按钮不可用
    //transStore.on('load',function(store,records){
    //    if(store.getCount() == 0){
    //        Ext.getCmp('btnOk').setDisabled(true);
    //    }else{
    //        Ext.getCmp('btnOk').setDisabled(false);
    //    };
    //});隐藏确认按钮 modify ywliu 090717
    
    
    
    // 查询字符串(物料编码/名称)
    var fuzzy = new Ext.form.TextField({
        id : "fuzzy",
        name : "fuzzy",
        width : 200,
        emptyText :'事务编码/名称'
    }); 
     // before load事件,传递查询字符串作为参数
    transStore.on('beforeload', function() {
        Ext.apply(this.baseParams, {
                    fuzzy : fuzzy.getValue()
                });
    });
    // 事务作用及原因grid
    var transPanel = new Ext.grid.GridPanel({
        region : "center",
        layout : 'fit',
        // 标题不可以移动
        enableColumnMove : false,
        store : transStore,
        // 单选
        sm : new Ext.grid.RowSelectionModel({singleSelect : true}),
        columns : [
            new Ext.grid.RowNumberer({header:"行号",width : 35}),
            // 事务编码
            {   header : "事务编码",
                width : 40,
                sortable : true,
                defaultSortable : true,
                dataIndex : 'transCode'
            },
            // 事务名称
            {   header : "事务名称",
                width : 100,
                sortable : true,
                dataIndex : 'transName'
            },
            // 事务说明
            {   header : "事务说明",
                width : 40,
                sortable : true,
                dataIndex : 'transDesc'
            },
              // 影响期初
            {   header : "影响期初",
                hidden : true,                
                dataIndex : 'isOpenBalance'            
            }, 
             // 影响接收
            {   header : "影响接收",
                hidden : true,                
                dataIndex : 'isReceive'            
            },
             // 影响调整
            {   header : "影响调整",
                hidden : true,                
                dataIndex : 'isAdjust'            
            },
             // 影响出货
            {   header : "影响出货",
                hidden : true,                
                dataIndex : 'isIssues'            
            },
             // 影响预留
            {   header : "影响预留",
                hidden : true,                
                dataIndex : 'isReserved'            
            },
             // 影响暂收
            {   header : "影响暂收",
                hidden : true,                
                dataIndex : 'isInspection'            
            },
             // 影响销售金额
            {   header : "影响销售金额",
                hidden : true,                
                dataIndex : 'isSaleAmount'            
            },
             // 影响账目成本
            {   header : "影响账目成本",
                hidden : true,                
                dataIndex : 'isEntryCost'            
            },
             // 影响采购成本
            {   header : "影响采购成本",
                hidden : true,                
                dataIndex : 'isPoCost'            
            },
             // 影响调整成本
            {   header : "影响调整成本",
                hidden : true,                
                dataIndex : 'isAjustCost'            
            },
             // 影响实际成本
            {   header : "影响实际成本",
                hidden : true,                
                dataIndex : 'isActualCost'            
            },
             //  需要验证采购单
            {   header : " 需要验证采购单",
                hidden : true,                
                dataIndex : 'isCheckPo'            
            },
            // 影响采购单到货数量
            {   header : "影响采购单到货数量",
                hidden : true,                
                dataIndex : 'isPoQuantity'            
            },
             // 影响工单
            {   header : "影响工单",
                hidden : true,                
                dataIndex : 'isShopOrder'            
            },
             // 是否需要验证工单
            {   header : "是否需要验证工单",
                hidden : true,                
                dataIndex : 'isCheckShopOrder'            
            },
           // 是否影响工单发料
            {   header : "是否影响工单发料",
                hidden : true,                
                dataIndex : 'isShopOrderIssue'            
            },
            // 流水号
            {   header : "流水号",
                hidden : true,                
                dataIndex : 'transId'            
            }
        ],
        viewConfig : {
            forceFit : true
        },
        tbar : [
            fuzzy,'-',
            {
                text : "模糊查询",
                iconCls : Constants.CLS_QUERY,
                handler : function() {
                    transStore.load({params:{start: 0, limit : Constants.PAGE_SIZE}} );
                }
            }],// ,'-',{text : "确认",iconCls : Constants.CLS_OK,id : "btnOk",handler : function() {
                    // 如果没有选择，弹出提示信息if(!transPanel.selModel.hasSelection()){
                        //Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_001);return;}
            		// 进入登记页面//registerTrans();}} 隐藏页面的“确认”按钮 modify by ywliu 090717
        // 分页
        bbar : new Ext.PagingToolbar({
            pageSize : Constants.PAGE_SIZE,
            store : transStore,
            displayInfo : true,
            displayMsg : Constants.DISPLAY_MSG,
            emptyMsg : Constants.EMPTY_MSG
        })
    });
    // 双击进入登记tab
    transPanel.on("rowdblclick", registerTrans);   
    // 查询tab
    var queryPanel = new Ext.Panel({
        layout : 'fit',
        title : '查询',
        items : [transPanel]
    });
    // ↑↑*******************事务作用及原因查询tab**************************************
    
    // 保存事务作用的原始数据，用来判断数据是否是新增
    var objFormDatas = null; 
    // 保存事务原因码的原始数据，用来判断数据有没有改变
    var objReasonDatas =[];
    // 保存已删除的事务原因码的流水号
    var deleteReasonIds = [];
    
    // ↓↓*******************事务作用及原因登记tab**************************************
    //******************TOBAR**********************************************************
    // 增加按钮
    var btnAdd = new Ext.Button({
        text : '新增',
        iconCls : Constants.CLS_ADD,
        handler : addTransHander
    });
    // 删除按钮
    var btnDelete = new Ext.Button({
        text : Constants.BTN_DELETE,
        iconCls : Constants.CLS_DELETE,
        disabled : true,
        handler : deleteTransHander
    });
    // 保存按钮
    var btnSave = new Ext.Button({
        text : Constants.BTN_SAVE,
        iconCls : Constants.CLS_SAVE,
        handler : saveTransHandler
    });    
    //******************TOBAR**********************************************************
    
    //******************TEXTFIELD**********************************************************
    // 事务编码
    var txtTransNo = new Ext.form.TextField({
        fieldLabel : '事务编码<font color="red">*</font>' ,
        isFormField : true,
        maxLength : 6,
        codeField : "yes",
        style :{
         'ime-mode' : 'disabled'
        },
        id : "transCode",
        name : "trans.transCode",
        anchor : '100%',
        allowBlank : false
    });
    // 事务名称
    var txtTransName = new Ext.form.TextField({
        fieldLabel : '&nbsp&nbsp事务名称<font color="red">*</font>' ,
        maxLength : 50,
        id : "transName",
        name : "trans.transName",
        anchor : '100%',
        allowBlank : false
    });
    // 事务说明
    var txtTransDesc = new Ext.form.TextField({
        fieldLabel : '&nbsp&nbsp&nbsp事务说明' ,
        maxLength : 100,
        id : "transDesc",
        name : "trans.transDesc",
        anchor : '100%'       
    });
     // 事务原因码id
    var hdnTransId = new Ext.form.Hidden({
        id: "transId",
        name: "trans.transId"
    });
    //******************TEXTFIELD**********************************************************
    
    
    
    //******************CHECKBOX 1*************6*****************************************
    //  影响期初
    var chkIsOpenBalance = new Ext.form.Checkbox({
        boxLabel :'影响期初',
        isFormField:true,
        id : "isOpenBalance",
        name : "trans.isOpenBalance",
        hideLabel : true,
        inputValue : Constants.CHECKED_VALUE
    });
    // 影响接收
    var chkIsReceive= new Ext.form.Checkbox({
        boxLabel :'影响接收',
        id : "isReceive",
        name : "trans.isReceive",
        isFormField:true,
        hideLabel : true,
        inputValue : Constants.CHECKED_VALUE
    });
      // 影响调整
    var chkIsAdjust= new Ext.form.Checkbox({
        boxLabel :'影响调整',
        id : "isAdjust",
        name : "trans.isAdjust",
        isFormField:true,
        hideLabel : true,
        inputValue : Constants.CHECKED_VALUE
    });
      // 影响出货
    var chkIsIssues= new Ext.form.Checkbox({
        boxLabel :'影响出货',
        id : "isIssues",
        name : "trans.isIssues",
        isFormField:true,
        hideLabel : true,
        inputValue : Constants.CHECKED_VALUE
    });
      // 影响预留
    var chkIsReserved= new Ext.form.Checkbox({
        boxLabel :'影响预留',
        id : "isReserved",
        name : "trans.isReserved",
        isFormField:true,
        hideLabel : true,
        inputValue : Constants.CHECKED_VALUE
    });
      // 影响暂收
    var chkIsInspection= new Ext.form.Checkbox({
        boxLabel :'影响暂收',
        id : "isInspection",
        name : "trans.isInspection",
        isFormField:true,
        hideLabel : true,
        inputValue : Constants.CHECKED_VALUE
    });
    
    //******************CHECKBOX 1**********************************************************
   
    //******************CHECKBOX 2**********************6************************************
    // 影响销售金额
    var chkIsSaleAmount = new Ext.form.Checkbox({
        boxLabel :'影响销售金额',
        isFormField:true,
        id : "isSaleAmount",
        name : "trans.isSaleAmount",
        hideLabel : true,
        inputValue : Constants.CHECKED_VALUE
    });
    // 影响账目成本
    var chkIsEntryCost= new Ext.form.Checkbox({
        boxLabel :'影响账目成本',
        id : "isEntryCost",
        name : "trans.isEntryCost",
        isFormField:true,
        hideLabel : true,
        inputValue : Constants.CHECKED_VALUE
    });
      // 影响采购成本
    var chkIsPoCost= new Ext.form.Checkbox({
        boxLabel :'影响采购成本',
        id : "isPoCost",
        name : "trans.isPoCost",
        isFormField:true,
        hideLabel : true,
        inputValue : Constants.CHECKED_VALUE
    });
      // 影响调整成本
    var chkIsAjustCost = new Ext.form.Checkbox({
        boxLabel :'影响调整成本',
        id : "isAjustCost",
        name : "trans.isAjustCost",
        isFormField:true,
        hideLabel : true,
        inputValue : Constants.CHECKED_VALUE
    });
      // 影响实际成本
    var chkIsActualCost= new Ext.form.Checkbox({
        boxLabel :'影响实际成本',
        id : "isActualCost",
        name : "trans.isActualCost",
        isFormField:true,
        hideLabel : true,
        inputValue : Constants.CHECKED_VALUE
    });
      // 需要验证采购单
    var chkIsCheckPo= new Ext.form.Checkbox({
        boxLabel :'需要验证采购单',
        id : "isCheckPo",
        name : "trans.isCheckPo",
        isFormField:true,
        hideLabel : true,
        inputValue : Constants.CHECKED_VALUE
    });
    //******************CHECKBOX 2**********************6************************************
    
    //******************CHECKBOX 3**********************4************************************
     // 影响采购单到货数量
    var chkIsPoQuantity= new Ext.form.Checkbox({
        boxLabel :'影响采购单到货数量',
        id : "isPoQuantity",
        name : "trans.isPoQuantity",
        isFormField:true,
        hideLabel : true,
        inputValue : Constants.CHECKED_VALUE
    });
      // 影响工单
    var chkIsShopOrder= new Ext.form.Checkbox({
        boxLabel :'影响工单',
        id : "isShopOrder",
        name : "trans.isShopOrder",
        isFormField:true,
        hideLabel : true,
        inputValue : Constants.CHECKED_VALUE
    });
      // 是否需要验证工单
    var chkIsCheckShopOrder = new Ext.form.Checkbox({
        boxLabel :'需要验证工单',
        id : "isCheckShopOrder",
        name : "trans.isCheckShopOrder",
        isFormField:true,
        hideLabel : true,
        inputValue : Constants.CHECKED_VALUE
    });
      // 是否影响工单发料
    var chkIsShopOrderIssue= new Ext.form.Checkbox({
        boxLabel :'影响工单发料',
        id : "isShopOrderIssue",
        name : "trans.isShopOrderIssue",
        isFormField:true,
        hideLabel : true,
        inputValue : Constants.CHECKED_VALUE
    });
    //******************CHECKBOX 3**********************4************************************
    
    // 第二行
    var secondLine = new Ext.Panel({
        border : false,
        height : 40,
        width :800,
        layout : "column",
        style : "padding-top:5px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
        items:[
               { // 事务编码
               columnWidth : 0.2,
               layout : "form",
               border : false,
               items : [txtTransNo,hdnTransId]
               },{ // 事务名称
               columnWidth : 0.3,                                     
               layout : "form",
               border : false,
               items : [txtTransName]
               },{ // 事务说明
               columnWidth : 0.4,                                     
               layout : "form",
               border : false,
               items : [txtTransDesc]
               }]
    });
    // 第三行
    var thirdLine = new Ext.Panel({
        border : false,
        hideLabel : true,
        width :800,
        style : "padding-top:0px;padding-bottom:0px;margin-bottom:0px",
        height : 20,
        isFormField:true,
        layout : "column",
        items:[{//  影响期初
               columnWidth : 0.20,
               layout : "form",
               border : false,
               items : [chkIsOpenBalance]
               },{// 影响接收
               columnWidth : 0.16,
               layout : "form",
               border : false,
               items : [chkIsReceive]
               },{// 影响调整
               columnWidth : 0.16,
               layout : "form",
               border : false,
               items : [chkIsAdjust]
               },{// 影响出货
               columnWidth : 0.16,
               layout : "form",
               border : false,
               items : [chkIsIssues]
               },{// 影响预留
               columnWidth : 0.16,
               layout : "form",
               border : false,
               items : [chkIsReserved]
               },{ // 影响暂收
               columnWidth : 0.16,
               layout : "form",
               border : false,
               items : [chkIsInspection]
               }]
    });
     // 第四行
    var fourLine = new Ext.Panel({
        border : false,
        hideLabel : true,
        style : "padding-top:0px;padding-bottom:0px;margin-bottom:0px",
        height : 20,
        width :800,
        isFormField:true,
        layout : "column",
        items:[{//  影响销售金额
               columnWidth : 0.20,
               layout : "form",
               border : false,
               items : [chkIsSaleAmount]
               },{// 影响账目成本
               columnWidth : 0.16,
               layout : "form",
               border : false,
               items : [chkIsEntryCost]
               },{// 影响采购成本
               columnWidth : 0.16,
               layout : "form",
               border : false,
               items : [chkIsPoCost]
               },{// 影响调整成本
               columnWidth : 0.16,
               layout : "form",
               border : false,
               items : [chkIsAjustCost]
               },{// 影响实际成本
               columnWidth : 0.16,
               layout : "form",
               border : false,
               items : [chkIsActualCost]
               },{ // 需要验证采购单
               columnWidth : 0.16,
               layout : "form",
               border : false,
               items : [chkIsCheckPo]
               }]
    });
    //  第五行
    var fiveLine = new Ext.Panel({
        border : false,
        hideLabel : true,
        width :800,
        style : "padding-top:0px;padding-bottom:0px;margin-bottom:0px",
        height : 20,
        isFormField:true,
        layout : "column",
        items:[{// 影响采购单到货数量
               columnWidth : 0.20,
               layout : "form",
               border : false,
               items : [chkIsPoQuantity]
               },{// 影响工单
               columnWidth : 0.16,
               layout : "form",
               border : false,
               items : [chkIsShopOrder]
               },{// 是否需要验证工单
               columnWidth : 0.16,
               layout : "form",
               border : false,
               items : [chkIsCheckShopOrder]
               },{// 是否影响工单发料
               columnWidth : 0.16,
               layout : "form",
               border : false,
               items : [chkIsShopOrderIssue]
               }]
    });
    // 事务原因码记录
    var reason = Ext.data.Record.create([
            // 事务原因码
            {name: 'reasonCode'},
            // 事务原因名称
            {name: 'reasonName'}, 
            // 流水号
            {name: 'reasonId'}, 
            // 用于判断记录是新增加的还是从数据库中读出来的
            {name:'isNewRecord'}]);
            
    // 事务原因码grid的store
    var reasonStore = new Ext.data.JsonStore({
        url : 'resource/getReasonList.action',
        root : 'list',
        totalProperty : 'totalCount',
        fields : reason
    });
    
    // 传递参数
    reasonStore.on('beforeload',function(){
        // 事务流水号
         Ext.apply(this.baseParams, {
                     transId:Ext.get("transId").dom.value
             });
    });
    
    // 增加事务原因
    var btnAddReason = new Ext.Button({
        text : "新增事务原因",
        iconCls : Constants.CLS_ADD,
        handler : addReason
    });
    
    // 删除事务原因
    var btnDeleteReason = new Ext.Button({
        text : "删除事务原因",
        iconCls : Constants.CLS_DELETE,
        handler : deleteReason
    });   
        
    // reasonGrid
    var reasonGrid = new Ext.grid.EditorGridPanel({
        region : "center",
        isFormField : false,
        border : false,
        store : reasonStore,
        // 单选
        sm : new Ext.grid.RowSelectionModel({singleSelect : true}),
        tbar : [btnAddReason, '-',btnDeleteReason],
        //anchor : "100%",
        // 标题不可以移动
        enableColumnMove : false,
        // 单击修改
        clicksToEdit : 1,
        columns : [
            new Ext.grid.RowNumberer({header:"行号",width : 35}),
            // 事务原因码
            {   header : "事务原因码<font color='red'>*</font>",
                width : 40,
                sortable : true,
                defaultSortable : true,
            	css:CSS_GRID_INPUT_COL,
                editor : new Ext.form.TextField({
                       maxLength : 10,
                       codeField : "yes"
                }),
                dataIndex : 'reasonCode'
            },
            // 事务原因名称
            {   header : "事务原因名称<font color='red'>*</font>",
                width : 150,
            	css:CSS_GRID_INPUT_COL,
                editor : new Ext.form.TextField({
                    maxLength : 50
                }),
                sortable : true,
                dataIndex : 'reasonName'
            },
            // 流水号
            {   header : "流水号",
                hidden : true,
                sortable : true,
                dataIndex : 'reasonId'
            },
            // 用于判断记录是新增加的还是从数据库中读出来的
            {   
                hidden : true,
                sortable : true,
                dataIndex : 'isNewRecord'
            }],
        autoSizeColumns : true,
        // 5/7/09 不进行分页 yiliu
//        // 分页
//        bbar : new Ext.PagingToolbar({
//                pageSize : Constants.PAGE_SIZE,
//                store : reasonStore,
//                displayInfo : true,
//                displayMsg : Constants.DISPLAY_MSG,
//                emptyMsg : Constants.EMPTY_MSG
//        }),
        viewConfig : {
            forceFit : true
        }
    });
    
  
    // 表单panel
    var formPanel = new Ext.FormPanel({
        region : 'north',
        border : false,
        tbar : [btnAdd,'-', btnDelete,'-', btnSave],
        labelAlign : 'right',
        height : 150,
        labelPad : 20,
        labelWidth : 70,
        width :800,
        items:[secondLine,thirdLine,fourLine,fiveLine]
    });

      // 登记tab
    var registerPanel = new Ext.Panel({
        layout : 'border',    
        title : '登记',
        margins : '0 0 0 0',
        border : false,
        defaults:{autoScroll: true},
        items :[formPanel,reasonGrid]
    });
    
    // ↑↑*******************事务作用及原因登记tab**************************************
     // tabPanel
    var tabPanel = new Ext.TabPanel({
        activeTab : 0,
        margins : '0 0 0 0',
        tabPosition : 'bottom',
        layoutOnTabChange:true,
        defaults:{autoScroll: true},
        items : [queryPanel, registerPanel]
     });
 
    
    // 显示区域
    var layout = new Ext.Viewport({
        layout : 'fit',
        margins : '0 0 0 0',
        region : 'center',
        border : false,
        defaults:{autoScroll: true},
        items : [tabPanel]        
    });
    
    //  ↓↓*******************************处理****************************************
    // 登记grid beforeedit 事件
    reasonGrid.on('beforeedit', reasonBeforeEdit);
     /**
     * reasonGrid beforeedit事件处理函数
     */
    function reasonBeforeEdit(e){
        // 获取store
        var store = e.grid.getStore();
        // 获取当前记录
        var record = store.getAt(e.row); 
        // 编辑列的字段名
        var fieldName = e.grid.getColumnModel().getDataIndex(e.column);
        // 事务原因码
        if("reasonCode" == fieldName) {
          // db中原有记录的库位号不可编辑
          if(!record.get('isNewRecord')){
            e.cancel = true;
            return;
          }
        }
    }
    
    /**
     * 双击查询grid记录，进入登记tab
     */
    function registerTrans(){
        // 选择的记录
        var record = transPanel.getSelectionModel().getSelected(); 
        // 转到登记tab
        tabPanel.setActiveTab(1);
        // 判断登记页面要不要保存
        if( isReasonChanged()||isFormChanged() ) {
            Ext.Msg.confirm(Constants.SYS_REMIND_MSG,Constants.COM_C_004,function(buttonobj){
                if (buttonobj == "yes") {
                    // 画面初始化
                    loadRegisterData(record);
                }
            });    
        }else {
            // 画面初始化
            loadRegisterData(record);
        }
    }
    
    /**
     * 加载登记tab的数据
     * @param record 查询页面gird记录
     */
    function loadRegisterData(record) {
        // 传递替代物料数据
        formPanel.getForm().loadRecord(record);
        // 初始化checkbox
        initCheckbox(record);
        // 保存form最原始数据
        saveFormOldValue();
        // 修改时，事务编码不可编辑
        Ext.get("transCode").dom.readOnly = true;
        // 删除按钮可用
        btnDelete.setDisabled(false);
        // 载入数据
        reasonStore.load({params:{start: 0, limit : Constants.PAGE_SIZE}} );

        reasonStore.on('load',function(){
        // 保存事务原因码最原始数据
        objReasonDatas = getReasonList();
        })
    }
    /**
     * 
     * 初始化checkbox
     */
    function initCheckbox(record) {
        // 第一行checkbox
        // 影响期初
         if(record.get('isOpenBalance') == Constants.CHECKED_VALUE){
            chkIsOpenBalance.setValue('true');
        }
        // 影响接收
        if(record.get('isReceive') == Constants.CHECKED_VALUE){
            chkIsReceive.setValue(true);
        }
        // 影响调整
        if(record.get('isAdjust') == Constants.CHECKED_VALUE){
            chkIsAdjust.setValue(true);
        }
        // 影响出货
        if(record.get('isIssues') == Constants.CHECKED_VALUE){
            chkIsIssues.setValue(true);
        }
        // 影响预留
        if(record.get('isReserved') == Constants.CHECKED_VALUE){
            chkIsReserved.setValue(true);
        }
        // 影响暂收
        if(record.get('isInspection') == Constants.CHECKED_VALUE){
            chkIsInspection.setValue(true);
        }
        
        // 第二行checkbox
        // 影响销售金额
        if (record.get('isSaleAmount') == Constants.CHECKED_VALUE) {
            chkIsSaleAmount.setValue(true);
        }
        // 影响账目成本
        if (record.get('isEntryCost') == Constants.CHECKED_VALUE) {
            chkIsEntryCost.setValue(true);
        }
        // 影响采购成本
        if (record.get('isPoCost') == Constants.CHECKED_VALUE) {
            chkIsPoCost.setValue(true);
        }
        // 影响调整成本
        if (record.get('isAjustCost') == Constants.CHECKED_VALUE) {
            chkIsAjustCost.setValue(true);
        }
        // 影响实际成本
        if (record.get('isActualCost') == Constants.CHECKED_VALUE) {
            chkIsActualCost.setValue(true);
        }
        // 需要验证采购单
        if (record.get('isCheckPo') == Constants.CHECKED_VALUE) {
            chkIsCheckPo.setValue(true);
        }
        
        // 第三行
        // 影响采购单到货数量
        if (record.get('isPoQuantity') == Constants.CHECKED_VALUE) {
            chkIsPoQuantity.setValue(true);
        }
        // 影响工单
        if (record.get('isShopOrder') == Constants.CHECKED_VALUE) {
            chkIsShopOrder.setValue(true);
        }
        // 是否需要验证工单
        if (record.get('isCheckShopOrder') == Constants.CHECKED_VALUE) {
            chkIsCheckShopOrder.setValue(true);
        }
        // 是否影响工单发料
        if (record.get('isShopOrderIssue') == Constants.CHECKED_VALUE) {
            chkIsShopOrderIssue.setValue(true);
        }
        
    }
    
    /**
     * 保存表单的值
     * */ 
    function saveFormOldValue() {
        objFormDatas = formPanel.getForm().getValues();
        objFormDatas['transCode'] = txtTransNo.getValue();
        objFormDatas['transName'] = txtTransName.getValue();
        objFormDatas['transDesc'] = txtTransDesc.getValue();
        
        objFormDatas['isOpenBalance'] = chkIsOpenBalance.getValue();
        objFormDatas['isReceive'] = chkIsReceive.getValue();
        objFormDatas['isAdjust'] = chkIsAdjust.getValue();
        objFormDatas['isIssues'] = chkIsIssues.getValue();
        objFormDatas['isReserved'] = chkIsReserved.getValue();
        objFormDatas['isInspection'] = chkIsInspection.getValue();
        
        objFormDatas['isSaleAmount'] = chkIsSaleAmount.getValue();
        objFormDatas['isEntryCost'] = chkIsEntryCost.getValue();
        objFormDatas['isPoCost'] = chkIsPoCost.getValue();
        objFormDatas['isAjustCost'] = chkIsAjustCost.getValue();
        objFormDatas['isActualCost'] = chkIsActualCost.getValue();
        objFormDatas['isCheckPo'] = chkIsCheckPo.getValue();
        
        objFormDatas['isPoQuantity'] = chkIsPoQuantity.getValue();
        objFormDatas['isShopOrder'] = chkIsShopOrder.getValue();
        objFormDatas['isCheckShopOrder'] = chkIsCheckShopOrder.getValue();
        objFormDatas['isShopOrderIssue'] = chkIsShopOrderIssue.getValue();
    }
    
    /**
     * 
     * 判断form信息有没有变化
     * 
     */
    function isFormChanged(){
        // 获取现在的表单值
        var objForm = formPanel.getForm().getValues();
        objForm['transCode'] = txtTransNo.getValue();
        objForm['transName'] = txtTransName.getValue();
        objForm['transDesc'] = txtTransDesc.getValue();
        
        objForm['isOpenBalance'] = chkIsOpenBalance.getValue();
        objForm['isReceive'] = chkIsReceive.getValue();
        objForm['isAdjust'] = chkIsAdjust.getValue();
        objForm['isIssues'] = chkIsIssues.getValue();
        objForm['isReserved'] = chkIsReserved.getValue();
        objForm['isInspection'] = chkIsInspection.getValue();
        
        objForm['isSaleAmount'] = chkIsSaleAmount.getValue();
        objForm['isEntryCost'] = chkIsEntryCost.getValue();
        objForm['isPoCost'] = chkIsPoCost.getValue();
        objForm['isAjustCost'] = chkIsAjustCost.getValue();
        objForm['isActualCost'] = chkIsActualCost.getValue();
        objForm['isCheckPo'] = chkIsCheckPo.getValue();
        
        objForm['isPoQuantity'] = chkIsPoQuantity.getValue();
        objForm['isShopOrder'] = chkIsShopOrder.getValue();
        objForm['isCheckShopOrder'] = chkIsCheckShopOrder.getValue();
        objForm['isShopOrderIssue'] = chkIsShopOrderIssue.getValue();
        
        // 循环判断
        for(var prop in objFormDatas){
            if(objForm[prop] != objFormDatas[prop]){  
                return true;
            }
        }        
        // 增加事务作用时 判断画面有无输入
        if(objFormDatas == null ){
            if(objForm['transCode'] || objForm['transName'] || objForm['transDesc'])
                return true;
            if(objForm['isOpenBalance'] || objForm['isReceive'] || objForm['isAdjust']||objForm['isIssues'] || objForm['isReserved'] || objForm['isInspection'])
                return true;
            if(objForm['isSaleAmount'] || objForm['isEntryCost'] || objForm['isPoCost']||objForm['isAjustCost'] || objForm['isActualCost'] || objForm['isCheckPo'])
            return true;
            if(objForm['isPoQuantity'] || objForm['isShopOrder'] || objForm['isCheckShopOrder']|| objForm['isShopOrderIssue'])
            return true;
        }
        return false;
    }
   /**
     * 判断事务原因码信息有没有改变
     */
    function isReasonChanged() {
        // 搜索是否有新增加的事务原因码
        var newRecs = getReasonList(true);
        // 如果有新增记录返回true
        if(newRecs.length > 0){
            return true;
        }
        
        // 搜索从数据库取得的事务原因码记录
        var oldRecs = getReasonList();
        // 若长度不同,则有被删除的记录，返回true
        if(oldRecs.length != objReasonDatas.length){
            return true;
        }
        // 按流水号排序
        sortReasonById(oldRecs);
        sortReasonById(objReasonDatas);
        for (var i = 0; i < oldRecs.length; i++) {
            // 事务原因码
            if (oldRecs[i]['reasonCode'] != objReasonDatas[i]['reasonCode'])
            {return true};
            // 事务原因名称
            if (oldRecs[i]['reasonName'] != objReasonDatas[i]['reasonName'])
            {return true};
        }
        return false;
    }
    /**
     * 按照reasonId排序事务原因码信息
     */
    function sortReasonById(records){
        var intLen = records.length;
        var temp = null;
        for(var i= intLen -1; i > 1; i--){
            for(var j = 0; j < i; j++){
                if(records[j].reasonId > records[j + 1].reasonId) {
                    temp = records[j];
                    records[j] = records[j + 1];
                    records[j + 1] = temp;
                }
            }
        }
    }
    /**
     * 
     * 增加事务原因码信息
     */
    function addReason() {
        // 新记录
        var record = new reason({
            // 事务原因码
            reasonCode : null,
            // 事务原因名称
            reasonName : null, 
            // 流水号
            reasonId : null,
            // 用于判断记录是新增加的还是从数据库中读出来的
            isNewRecord :true
        });
        // 原数据个数
        var count = reasonStore.getCount();
        // 原数据个数
		if (count == null) {
			count = 0;
		}
        // 停止原来编辑
        reasonGrid.stopEditing();
        // 插入新数据
        reasonStore.insert(count,record);
        reasonStore.totalLength = reasonStore.getTotalCount() + 1;
		reasonStore.commitChanges();
        reasonGrid.getView().refresh();
//        reasonGrid.getBottomToolbar().updateInfo();
        // 开始编辑新记录第一列
        reasonGrid.startEditing(count,1);
    }
    
    /**
     * 
     * 删除事务原因码信息
     */
    function deleteReason() {
        var record = reasonGrid.selModel.getSelected();
        if(reasonGrid.selModel.hasSelection()){
            // 如果选中一行则删除
            reasonStore.remove(record);
            reasonStore.totalLength = reasonStore.getTotalCount() - 1;
            reasonGrid.getView().refresh();
            reasonGrid.getBottomToolbar().updateInfo();
            // 如果不是新增加的记录,保存删除的流水号
            if (!record.get('isNewRecord')) {
                deleteReasonIds.push(record.get('reasonId'));
            }                              
      
        }else {
            // 否则弹出提示信息
            Ext.Msg.alert(Constants.SYS_REMIND_MSG,'&nbsp&nbsp&nbsp'+Constants.COM_I_001);
            
        }
    }
    /**
     * 
     * 获取所有物料原因码信息，db已存在的和新增加的数据分开保存
     * 
     */
    function getReasonList( isNew) {
        //记录
        var records = new Array();
        var blnFlag = isNew;
        // 循环
        for(var index  = 0; index < reasonStore.getCount(); index ++) {
            var record = reasonStore.getAt(index).data;
            if(isNew){
                // 新记录
                if(record.isNewRecord){
                    records.push(cloneReasonRecord(record));
                }
            }else {
                // db中原有记录
                if(!record.isNewRecord){
                    records.push(cloneReasonRecord(record));
                }
            }
        }
        return records;
    }
    
    /**
     * 
     * 拷贝事务原因码信息
     * 
     */
    function cloneReasonRecord(record) {
        var objClone = new Object();
        // 拷贝属性
        // 事务原因码
        objClone['reasonCode'] = record['reasonCode'];
        // 事务原因名称
        objClone['reasonName'] = record['reasonName'];
         // 流水号
        objClone['reasonId'] = record['reasonId'];
        return objClone;         
    }
    
    /**
     * saveTransHandler
     * 清空登记页面的数据refresh
     */
    function clearRegisterTab() {        
        // 查询tab刷新 
        transStore.load({params:{start: 0, limit : Constants.PAGE_SIZE}} );
        // form表单清空
        formPanel.getForm().reset();
        // 库位grid清空
        reasonStore.removeAll();
        // 保存的表单数据删除
        objFormDatas = null;
        // 保存的已删除事务原因码流水号清空
        deleteReasonIds = [];
        // 保存事务原因码的原始数据信息清空
        objReasonDatas = [];
        // 事务编码可编辑
        Ext.get("transCode").dom.readOnly = false;
        // 删除按钮不可用
        btnDelete.setDisabled(true);
    }
    
    /**
     * 
     * 增加事务作用信息按钮处理
     */
    function addTransHander() {
        if( isReasonChanged()||isFormChanged() ){
            Ext.Msg.confirm(Constants.SYS_REMIND_MSG,Constants.COM_C_004,function(buttonobj){
                if (buttonobj == "yes") {
                    // 画面初始化
                    clearRegisterTab();  
                }
            });          
        }else {
            // 画面初始化
            clearRegisterTab();    
        }            
    }
    /**
     * 保存事务作用信息按钮处理
     */
    function saveTransHandler() {
    	 // 信息没有改变直接返回
        if(!isReasonChanged() && !isFormChanged()){
            Ext.Msg.alert(Constants.SYS_REMIND_MSG,Constants.COM_I_006);
            return;
        }
        Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_C_001,function(buttonobj){
            if (buttonobj == "yes") {
            	 if(isformCanSave()){
                     // 保存
                     saveTransInfo(refreshTransInfo);
                 }
                  return;
            }            
        });  
    }
     /**
     * 表单check
     */
    function isformCanSave() {
        var msg = "";      
        if(!formPanel.getForm().isValid()) return false;
        // 事务编码和事务名称不可以为空
        if(!txtTransNo.getValue()){
            msg += String.format(Constants.COM_E_002, '事务编码') ;
        }
        if( !txtTransName.getValue()){
            msg += "<br/>" + String.format(Constants.COM_E_002, '事务名称');
        }
        // 如果事务原因码有改变
        if(isReasonChanged()) {
            // 事务原因码是否为空的check
            msg += checkReasonNo();
            if(msg){
                Ext.Msg.alert(Constants.SYS_REMIND_MSG, msg);
                return false;
            }
            // 事务原因码和事务原因名称的重复check
            msg = isReasonRepeat();
        }
        if(msg){
             Ext.Msg.alert(Constants.SYS_REMIND_MSG, msg);
            return false;
        }
       
        return true;
    }
    /**
     * 检测库位是否有重复
     */
    function isReasonRepeat(){
        var msg = "";
        var noErrFlg = 0;
        var nameErrFlg = 0;
        for(var i = 0; i < reasonStore.getCount(); i++){
            for(var j = i+1; j < reasonStore.getCount(); j++){
                if(reasonStore.getAt(i).get('reasonCode')==reasonStore.getAt(j).get('reasonCode')){                    
                    noErrFlg = 1;
                }
                if(reasonStore.getAt(i).get('reasonName')!=null && reasonStore.getAt(j).get('reasonName')!=null) {
	                if(reasonStore.getAt(i).get('reasonName')==reasonStore.getAt(j).get('reasonName')){
	                    nameErrFlg = 1;
	                }
                }
            }
        }  
        if(noErrFlg == 1){
            msg +="<br/>" + String.format(Constants.COM_E_007, "事务原因码");
        }
        if(nameErrFlg == 1){
            msg +="<br/>" + String.format(Constants.COM_E_007, "事务原因名称");
        }
        return msg;
    }
    /**
     * 事务原因码主文件关键字段check
     */
    function checkReasonNo() {
    	var msg = "";
    	var codeError = 0;
    	var nameError = 0;
        // 循环
        for(var index  = 0; index < reasonStore.getCount(); index ++) {
            // 记录
            var record = reasonStore.getAt(index).data;
            if(!record.reasonCode ) {
            	codeError = 1;
            }
            if(!record.reasonName ) {
            	nameError = 1;
            }
        }
        if(codeError == 1)
            msg ="<br/>" + String.format(Constants.COM_E_002, '事务原因码'); 
        if(nameError ==1 )
            msg +="<br/>" + String.format(Constants.COM_E_002, '事务原因名称');
        return msg;
    }
     /**
     * 保存事务作用信息
     * @param func 保存成功后的操作
     * @param params 操作的参数
     */
    function saveTransInfo(func,params) { 
        // 表单提交
        formPanel.getForm().submit({
            url : 'resource/saveTransAndReason.action',
            method : Constants.POST,
            params : {
                // form信息是否更改
                isFormChanged: isFormChanged(),
                // 事务原因信息是否更改
                isReasonChanged : isReasonChanged(),
                // 新增加的事务原因
                newReason : Ext.util.JSON.encode(getReasonList(true)),
                // 修改过的事务原因db记录
                dbReason : Ext.util.JSON.encode(getReasonList()),
                // 删除的事务原因id集
                deleteReasonIds : deleteReasonIds.join(',')
            },
            success : function(form ,action){                
                var result = eval("(" + action.response.responseText +")");   
                if(result.success){
                    Ext.Msg.alert(Constants.SYS_REMIND_MSG, '&nbsp&nbsp&nbsp'+Constants.COM_I_004);
                    // 刷新页面
                    if(params){
                         func.apply(this, [params]);
                    }else{
                        func.apply(this);
                    }
                   
                }else{
                    Ext.Msg.alert(Constants.SYS_REMIND_MSG, result.msg);
                }
            },
            failure : function(form ,action){
            		var result = eval("(" + action.response.responseText +")");   
                    Ext.Msg.alert(Constants.SYS_REMIND_MSG, result.msg);
            } 
        });                           
    }
    
    /**
     * 删除事务作用信息
     */
    function deleteTransHander() {
          
        // 弹出删除确认信息
        Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_C_002,
             function(buttonobj) {
                if (buttonobj == "yes") {
                   // 删除
                   Ext.lib.Ajax.request('POST', 'resource/deleteTrans.action', {
                        success : function(action) {
                            Ext.Msg.alert(Constants.SYS_REMIND_MSG,
                                    Constants.COM_I_005);
                            // 初始化
                            clearRegisterTab();
                        },
                        failure : function(form, action) {
                            Ext.Msg.alert(Constants.SYS_REMIND_MSG, '删除失败');
                        }
                   },
                        'transId=' + hdnTransId.getValue());
                }
         });
         
    }
    /**
     * 
     * 重新记录事务作用信息表单原始值
     */
    function refreshTransInfo() {        
        // 保存表单数据
        saveFormOldValue();
        if(!objFormDatas['trans.transId']){
            // 新增的记录保存最成功后，获得其流水号
            Ext.lib.Ajax.request('POST',
                        'resource/getTransIdByTransNo.action',
                        {
                            success : function(action) {
                                    var result = eval("(" +action.responseText +")");
                                    if(result){
                                        objFormDatas['trans.transId'] = result.transId;
                                        hdnTransId.setValue(result.transId);
                                        // 初始化grid
                                        initGrid();
                                    }
                                }
                         },
                         'transCode=' + txtTransNo.getValue());
        }
        initGrid();
        // 事务作用编码不允许改
        Ext.get("transCode").dom.readOnly = true;
        // 删除按钮可用
        btnDelete.setDisabled(false);
    }
    
    /** 初始化Grid*/
    function initGrid() {
         // 查询tab刷新
        transStore.load({params:{start: 0, limit : Constants.PAGE_SIZE}} );
        // 载入事务作用相对应的事务原因信息
        reasonStore.load({params:{start: 0, limit : Constants.PAGE_SIZE}});
        // 清空修改信息
        reasonStore.modified = [];
    }
 });