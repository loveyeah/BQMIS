Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
// 把数量用逗号分隔
function divide(value) {
    var svalue = value + "";
    var decimal = "";
    var negative = false;
    var tempV = "";
    // 如果有小数
    if (svalue.indexOf(".") > 0) {
        decimal = svalue.substring(svalue.indexOf("."), svalue.length);
    }
    // 如果是负数
    if (svalue.indexOf("-") >= 0) {
        negative = true;
        svalue = svalue.substring(1, svalue.length);
    }
    tempV = svalue.substring(0, svalue.length - decimal.length);
    svalue = "";
    while (Div(tempV, 1000) > 0) {
        var temp = Div(tempV, 1000);
        var oddment = tempV - temp * 1000;
        var soddment = "";
        tempV = Div(tempV, 1000);
        soddment += (0 == Div(oddment, 100)) ? "0" : Div(oddment, 100);
        oddment -= Div(oddment, 100) * 100;
        soddment += (0 == Div(oddment, 10)) ? "0" : Div(oddment, 10);
        oddment -= Div(oddment, 10) * 10;
        soddment += (0 == Div(oddment, 1)) ? "0" : Div(oddment, 1);
        oddment -= Div(oddment, 1) * 1;
        svalue = soddment + "," + svalue;
    }
    svalue = tempV + "," + svalue;
    svalue = svalue.substring(0, svalue.length - 1);
    svalue += decimal;
    if (true == negative) {
        svalue = "-" + svalue;
    }
    return svalue;
}
// 整除
function Div(exp1, exp2) {
    var n1 = Math.round(exp1); //四舍五入
    var n2 = Math.round(exp2); //四舍五入

    var rslt = n1 / n2; //除

    if (rslt >= 0) {
        rslt = Math.floor(rslt); //返回值为小于等于其数值参数的最大整数值。
    } else {
        rslt = Math.ceil(rslt); //返回值为大于等于其数字参数的最小整数。
    }

    return rslt;
}
Ext.onReady(function() {
    Ext.QuickTips.init();   
    // ↓↓*******************替代物料查询tab**************************************
    
    // 物料grid的store
    var materialStore = new Ext.data.JsonStore({
        url : 'resource/getMaterialListForPart.action',
        root : 'list',
        totalProperty : 'totalCount',
        fields : [
            // 物料编码
            {name:'materialNo'},
            // 物料名称
            {name: 'materialName'}, 
            // 物料分类
            {name: 'materialClassName'},
            // 规格型号
            {name: 'specNo'},
            // 材质参数
            {name: 'parameter'},
            // 物料流水号            
            {name: 'materialId'}
            ]
        });
    // 载入数据
    materialStore.load({params:{start: 0, limit : Constants.PAGE_SIZE}} );
    
    // 监听load事件
    // 当没有数据时确认按钮不可用
    //materialStore.on('load',function(store,records){
    //    if(store.getCount() == 0){
    //        Ext.getCmp('btnOk').setDisabled(true);
    //    }else{
    //        Ext.getCmp('btnOk').setDisabled(false);
    //    };
    //}); 隐藏确认按钮 modify ywliu 090717
   
    // 查询字符串(物料编码/名称)
    var fuzzy = new Ext.form.TextField({
        id : "fuzzy",
        name : "fuzzy",
        width : 200,
        emptyText :'物料编码/名称'
    }); 
     // before load事件,传递查询字符串作为参数
     materialStore.on('beforeload', function() {
        Ext.apply(this.baseParams, {
                    fuzzy : fuzzy.getValue()
                });
    });
    // 物料grid
    var materialPanel = new Ext.grid.GridPanel({
        region : "center",
        layout : 'fit',
        // 标题不可以移动
        enableColumnMove : false,
        store : materialStore,
        // 单选
        sm : new Ext.grid.RowSelectionModel({singleSelect : true}),
        columns : [
            new Ext.grid.RowNumberer({header:"行号",width : 35}),
            // 物料编码
            {   header : "物料编码",
                width : 40,
                sortable : true,
                defaultSortable : true,
                dataIndex : 'materialNo'
            },
            // 物料名称
            {   header : "物料名称",
                width : 100,
                sortable : true,
                dataIndex : 'materialName'
            },
            // 物料分类
            {   header : "物料分类",
                width : 40,
                sortable : true,
                dataIndex : 'materialClassName' 
            },
            // 规格型号
            {   header : "规格型号",
                width : 40,
                sortable : true,
                dataIndex : 'specNo'
            },
            // 材质/参数
            {   header : "材质/参数",
                width : 100,
                sortable : true,
                dataIndex : 'parameter'
            },
            // 物料流水号
            {   header : "物料流水号",
                hidden : true,                
                dataIndex : 'materialId'            
            }
        ],
        viewConfig : {
            forceFit : true
        },
        tbar : [
            fuzzy,'-',
            {
                text : "模糊查询",
                handler : function() {
                    materialStore.load({params:{start: 0, limit : Constants.PAGE_SIZE}} );
                }
            }], // ,'-',{text : "确认",id : "btnOk",handler : function() {// 如果没有选择，弹出提示信息
                    //if(!materialPanel.selModel.hasSelection()){Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_001);
                    //return;}进入登记页面registerMaterial();}} // 隐藏页面的“确认”按钮 modify by ywliu 090717
        // 分页
        bbar : new Ext.PagingToolbar({
            pageSize : Constants.PAGE_SIZE,
            store : materialStore,
            displayInfo : true,
            displayMsg : Constants.DISPLAY_MSG,
            emptyMsg : Constants.EMPTY_MSG
        })
    });
    // 双击进入登记tab
    materialPanel.on("rowdblclick", registerMaterial);   
    // 查询tab
    var queryPanel = new Ext.Panel({
        layout : 'fit',
        title : '查询',
        items : [materialPanel]
    });
    // ↑↑*******************替代物料查询tab**************************************    
    
    // 保存物料的原始数据，用来判断数据是否是新增
    var objFormDatas = null; 
    // 保存原始物料信息，用来判断数据有没有改变
    var objLocationDatas =[];
    // 保存已删除的替代物料的流水号
    var deleteLocationIds = [];
    // 新增标志位
    var bFlag = true;
    // 新增消息标志位
    var msgFlag = false;
    // ↓↓*******************替代物料登记tab**************************************
    // 增加按钮
    var btnAdd = new Ext.Button({
        text : '新增',
        iconCls : Constants.CLS_ADD,
        handler : addAlertMaterial
    });
    // 删除按钮
    var btnDelete = new Ext.Button({
        text : Constants.BTN_DELETE,
        iconCls : Constants.CLS_DELETE,
        disabled : true,
        handler : deleteAlertMaterial
    });
    // 保存按钮
    var btnSave = new Ext.Button({
        text : Constants.BTN_SAVE,
        iconCls : Constants.CLS_SAVE,
        handler : saveMaterialHandler
    });    
    // 替代物料流水号
    var hdnAltMatId = new Ext.form.Hidden({
        id: "materialId",
        name: "materialId"
    });

    // 物料编码
    var txtMaterialNo = new Ext.form.TextField({
        fieldLabel :"物料编码<font color='red'>*</font>",        
        isFormField : true,
        allowBlank : false,
        readOnly : true,
        maxLength : 30,
        id : "materialNo",
        name : "materialNo",
        anchor : '100%',
        width:100,
        listeners : {
            focus : selectMaterial
        }
    });
     
    // 物料名称
    var txtMaterialName = new Ext.form.TextField({
        fieldLabel : '&nbsp&nbsp&nbsp物料名称' ,
        readOnly : true,
        id : "materialName",
        name : "materialName",
        anchor : '100%'       
    });
    // 第二行
    var secondLine = new Ext.Panel({
        border : false,
        height : 30,
        layout : "column",
        style : "padding-top:5px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
        anchor : '100%',
        items:[{
               columnWidth : 0.3,
               layout : "form",
               border : false,
               items : [txtMaterialNo, hdnAltMatId]
               },{
               columnWidth : 0.55,                                     
               layout : "form",
               border : false,
               items : [txtMaterialName]
               }]
    });
    

    
    // 增加替代物料按钮
    var btnAddLocation = new Ext.Button({
        text : "新增替代物料",
        iconCls : Constants.CLS_ADD,
        handler : addLacation
    });
    // 删除替代物料按钮
    var btnDeleteLocation = new Ext.Button({
        text : "删除替代物料",
        iconCls : Constants.CLS_DELETE,
        handler : deleteLocation
    });  
    // 替代物料记录
    var material = Ext.data.Record.create([
            // 替代物料流水号
            {name: 'alternateMaterialId'},
            // 替代物料.替代物料id
            {name: 'alterMaterialId'},
            // 替代物料编码
            {name: 'materialNo'},
            // 替代物料名称
            {name: 'materialName'}, 
            // 相对替换数量
            {name: 'qty'},
            // 优先级
            {name: 'priority'},
            // 有效开始时间
            {name: 'effectiveDate'},
            // 有效结束时间
            {name: 'discontinueDate'},
            // 用于判断记录是新增加的还是从数据库中读出来的
            {name:'isNewRecord'}]);
            
        // 替代物料grid的store
        var locationStore = new Ext.data.JsonStore({
            url : 'resource/getAlertMaterialList.action',
            root : 'list',
            totalProperty : 'totalCount',
            fields : material
        });
        // 传递参数
        locationStore.on('beforeload',function(){
            // 物料主文件流水号（物料Id号）
            Ext.apply(this.baseParams, {
                     materialId:Ext.get("materialId").dom.value
             });
        });
        // 有效开始时间
        var effictDate = new Ext.form.TextField({
                id : 'effictDate',
                readOnly:true,
                style : 'cursor:pointer',
                width : 180,
                listeners : {
                    focus : function() {
                        WdatePicker({
                            dateFmt : 'yyyy-MM-dd',
                            onclearing : function() {
                                materialGrid.getSelectionModel().getSelected().set(
                                "effectiveDate", "");
                            },
                            onpicked : function() {
                                materialGrid.getSelectionModel().getSelected().set(
                                "effectiveDate", Ext.get("effictDate").dom.value);
                            }
                        });
                    }
                }
            });
    
        // 有效结束时间
        var dateTo = new Ext.form.TextField({
                id : 'dateTo',
                readOnly:true,
                style : 'cursor:pointer',
                width : 180,
                listeners : {
                    focus : function() {
                        WdatePicker({
                            dateFmt : 'yyyy-MM-dd',
                            onclearing : function() {
                                materialGrid.getSelectionModel().getSelected().set(
                                "discontinueDate", "");
                            },
                            onpicked : function() {
                                materialGrid.getSelectionModel().getSelected().set(
                                "discontinueDate", Ext.get("dateTo").dom.value);
                            }
                        });
                    }
                }
        });

        // 替代物料grid
        var materialGrid = new Ext.grid.EditorGridPanel({
            region : "center",
            layout : 'fit',
            height : 480,
            isFormField : false,
            border : true,
            // 单选
            sm : new Ext.grid.RowSelectionModel({singleSelect : true}),
            tbar : [btnAddLocation,'-', btnDeleteLocation],
            anchor : "100%",
            // 标题不可以移动
            enableColumnMove : false,
            // 单击修改
            clicksToEdit : 1,
            store : locationStore,
            columns : [
                new Ext.grid.RowNumberer({header:"行号",width : 35}),
                // 替代物料编码
                {   header : "替代物料编码<font color='red'>*</font>",
                    width : 155,
                    sortable : true,
                    defaultSortable : true,
            		css:CSS_GRID_INPUT_COL,
//                    editor : new Ext.form.TextField({
//                        maxLength : 30,
//                        style :{'ime-mode' : 'disabled' },
//                        readOnly : true
//                    }),
                    dataIndex : 'materialNo'
                },
                // 替代物料名称
                {   header : "替代物料名称",
                    width : 150,
                    sortable : true,
                    dataIndex : 'materialName'
                },
                // 相对替换数量
                {   header : "相对替换数量<font color='red'>*</font>",
                    width : 100,
                    align : 'right',
                    renderer : function(value) {
                                if (("" == value || null == value) && !("0" == value)) {
                                    return "";
                                } else {
                                    return divide(value);
                           }
                     },
            		css:CSS_GRID_INPUT_COL,
                    editor : new Ext.form.NumberField({
                                maxValue : 99999999999.9999,
                                decimalPrecision:4,
                                minValue : 1,
                                allowNegative : false
                    }),
                    sortable : true,
                    dataIndex : 'qty'
                },
                 // 优先级
                {   header : "优先级<font color='red'>*</font>",
                    width : 100,
                    align : 'right',
                    renderer : function(value) {
                                if (("" == value || null == value) && !("0" == value)) {
                                    return "";
                                } else {
                                    return divide(value);
                           }
                     },
            		css:CSS_GRID_INPUT_COL,
                    editor : new Ext.form.NumberField({
                        allowNegative : false,
                        maxValue : 9999999999,
                        decimalPrecision:0
                    }),
                    sortable : true,
                    dataIndex : 'priority'
                },
                 // 有效开始日期
                {   header : "有效开始日期",
                    width : 150,
                    css:CSS_GRID_INPUT_COL,
                    editor : effictDate,
                    sortable : true,
                    dataIndex : 'effectiveDate'
                },
                 // 有效结束日期
                {   header : "有效截止日期",
                    width : 150,
            		css:CSS_GRID_INPUT_COL,
                    editor : dateTo,
                    sortable : true,
                    dataIndex : 'discontinueDate'
                },
                // 替代物料流水号
                {   
                    header : "替代物料流水号",
                    hidden :true,
                    dataIndex : 'alternateMaterialId'
                },
                // 替代物料.替代物料id
                {   
                    header :"替代物料id",
                    hidden :true,
                    dataIndex : 'alterMaterialId'
                },
                {   
                    dataIndex : 'isNewRecord',
                    hidden :true
                }],
            autoSizeColumns : true,
            // 5/7/09 9:18 不进行分页  yiliu
//            // 分页
//            bbar : new Ext.PagingToolbar({
//                    pageSize : Constants.PAGE_SIZE,
//                    store : locationStore,
//                    displayInfo : true,
//                    displayMsg : Constants.DISPLAY_MSG,
//                    emptyMsg : Constants.EMPTY_MSG
//            }),
            viewConfig : {
                forceFit : true
            }
    });
    
    // 表单panel
    var formPanel = new Ext.FormPanel({
        tbar : [btnAdd,'-', btnDelete,'-', btnSave],
        labelAlign : 'right',
        margins : '0 0 0 0',
        region : "north",
        height : 60,
        anchor :"0",
        labelPad : 15,
        labelWidth : 70,
        border : false,
        items:[secondLine]
    });
    // 登记tab
    var registerPanel = new Ext.Panel({
        layout : 'border',        
        title : '登记',
        margins : '0 0 0 0',
        border : false,
        items :[formPanel,materialGrid]
    });
    // ↑↑*******************替代物料登记tab**************************************
    // tabPanel
    var tabPanel = new Ext.TabPanel({
        activeTab : 0,
        margins : '0 0 0 0',
        tabPosition : 'bottom',
        autoScroll : true,
        items : [queryPanel, registerPanel]
     });
 
    
    // 显示区域
    var layout = new Ext.Viewport({
        layout : 'fit',
        margins : '0 0 0 0',
        region : 'center',
        border : false,
        items : [tabPanel]        
    });
    //  ↓↓*******************************处理****************************************
   
     // 替代物料点击事件
    materialGrid.on('cellclick', choseEdit);
     /**
     * 清空登记页面的数据refresh
     */
    function clearRegisterTab() {  
        bFlag = true;
        // 查询tab刷新 
        materialStore.load({params:{start: 0, limit : Constants.PAGE_SIZE}} );
        // form表单清空
        formPanel.getForm().reset();
        // 替代物料grid清空
        locationStore.removeAll();
        // 判断是否是新增
        objFormDatas = null;
        // 保存的已删除替代物料流水号清空
        deleteLocationIds = [];
        // 保存的替代物料信息清空
        objLocationDatas = [];
        // 删除按钮不可用
        btnDelete.setDisabled(true);
    }
     /**
     * 删除替代物料信息
     */
    function deleteAlertMaterial() {
        // 物料id
        var materialId = hdnAltMatId.getValue(); 
       
        // 弹出删除确认信息
        Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_C_002,
             function(buttonobj) {
                if (buttonobj == "yes") {
                   // 删除
                   Ext.lib.Ajax.request('POST','resource/deleteMaterial.action',
                         {success : function(action) {
                               Ext.Msg.alert(Constants.SYS_REMIND_MSG,Constants.DEL_SUCCESS);
                               // 初始化
                               clearRegisterTab()
                             }
                     }, 'materialId=' + materialId);
                }
         });
    }
    /**
     * 增加物料信息
     */        
    function addAlertMaterial() {
        if (isAlterMaterialChanged() || isLocationChanged()) {
            Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_C_004,
                function(buttonobj) {
                    if (buttonobj == "yes") {
                        // 画面初始化
                        clearRegisterTab();
                    }
                    // == no 放弃
            });
        } else {
            // 画面初始化
            clearRegisterTab();
        }
    }    
    /**
     * 检查计划结束时间是否大于计划开始时间
     */ 
    function endDateCheck(record){
        var startDate =  record.effectiveDate;
        var endDate = record.discontinueDate;
        if (startDate > endDate
                && startDate != null) {
            return false;
        } else {
            return true;

        }
    }
    /**
     * textField显示时间比较方法
     */ 
    function compareDate(argDate1, argDate2) {
        return argDate1.getTime() >argDate2.getTime();
    }

    /**
     * textField显示时间比较方法
     */ 
    function compareDateStr(argDateStr1, argDateStr2){
        var date1 = Date.parseDate(argDateStr1, 'Y-m-d ');
        var date2 = Date.parseDate(argDateStr2, 'Y-m-d ');
        return compareDate(date1, date2);
    }
    
    /**
     * 双击查询grid记录，进入登记tab
     */
    function registerMaterial(){
        // 选择的记录
        var record = materialPanel.getSelectionModel().getSelected(); 
        // 转到登记tab
        tabPanel.setActiveTab(1);
        hdnAltMatId.setValue(record.get('materialId'));
        // 判断登记页面要不要保存
        if(isAlterMaterialChanged()|| isLocationChanged()){
            Ext.Msg.confirm(Constants.SYS_REMIND_MSG,Constants.COM_C_004,function(buttonobj){
                if (buttonobj == "yes") {
                    // 画面初始化
                    loadRegisterData(record);
                }
                // == no 放弃
            });         
        }else {
            // 画面初始化
            loadRegisterData(record);    
        }       
    }
    
     /**
     * 判断物料信息有没有改变
     */
    function isAlterMaterialChanged() {
       // 获取现在的表单值
       var objForm = formPanel.getForm().getValues();
       if( objFormDatas == null || objForm['materialId'] == hdnAltMatId.getValue() ) {
              return false
       } 
       return true;
    }
    
    /** 判断物料信息是否有所输入 没有改变返回false*/
    function isAlterMaterialChangedForNew() {
       // 获取现在的表单值
       var objForm = formPanel.getForm().getValues();
       objForm['materialId'] = hdnAltMatId.getValue();
       if(objFormDatas!= null && objForm['materialId'] == objFormDatas['materialId']){
            return false;
       }
       
       // 增加时 判断画面有无输入
        if(objFormDatas == null ){
            if(objForm['materialId']!=null && objForm['materialId']!=="") {
                return false;
            }
        }
        return true;     
    }
    /** 判断要报什么message,false:画面数据未修改，true：请增加明细*/
    function checkMsg() {
        // 新替代物料记录
        var newRecs = getLocationList(true);
         // 原db记录
        var oldRecs = getLocationList();
        // 如果有新增记录返回true
        if(newRecs.length == 0 && oldRecs.length == 0){
        	 if(hdnAltMatId.getValue()!=""&& hdnAltMatId.getValue()!=null)
                 return true;
        }
        return false;
    }
    
    function test(obj) {
        var temp = '';
        for(var prop in obj) {
            temp += prop + ': ' + obj[prop] + '\n'
        }
        alert(temp);
    }
     /**
     * 判断替代物料信息有没有改变
     */
    function isLocationChanged() {
        // 新替代物料记录
        var newRecs = getLocationList(true);
        // 如果有新增记录返回true
        if(newRecs.length > 0){
            return true;
        }
        
        // 原db记录
        var oldRecs = getLocationList();
        // 长度不同,有被删除的记录，返回true
        if(oldRecs.length != objLocationDatas.length){
            return true;
        }
        // 按流水号排序
        sortLocationsById(oldRecs);
        sortLocationsById(objLocationDatas);
        for (var i = 0; i < oldRecs.length; i++) {
            // 编号
            if (oldRecs[i]['materialNo'] != objLocationDatas[i]['materialNo'])
            {return true};
            // 名称
            if (oldRecs[i]['materialName'] != objLocationDatas[i]['materialName'])
            {return true};
            // 相对数量
            if (oldRecs[i]['qty'] != objLocationDatas[i]['qty'])
            {return true};
            // 优先级
            if ((oldRecs[i]['priority'] != objLocationDatas[i]['priority']) || ("" == oldRecs[i]['priority'])&&("0" != oldRecs[i]['priority']))
            {return true};
            // 有效开始日期
            if (oldRecs[i]['effectiveDate'] != objLocationDatas[i]['effectiveDate'])
            {return true};
            // 有效截止日期
            if (oldRecs[i]['discontinueDate'] != objLocationDatas[i]['discontinueDate'])
            {return true};
        }
        return false;
    }
    /**
     * 按照alternateMaterialId排序替代物料信息
     */
    function sortLocationsById(records){
        var intLen = records.length;
        var temp = null;
        for(var i= intLen -1; i > 1; i--){
            for(var j = 0; j < i; j++){
                if(records[j].alternateMaterialId > records[j + 1].alternateMaterialId) {
                    temp = records[j];
                    records[j] = records[j + 1];
                    records[j + 1] = temp;
                }
            }
        }
    }
    /**
     * 获取所有替代物料信息，db已存在的和新增加的数据分开保存
     */
    function getLocationList( isNew) {
        //记录
        var records = new Array();
        var blnFlag = isNew;
        // 循环
        for(var index  = 0; index < locationStore.getCount(); index ++) {
            var record = locationStore.getAt(index).data;
            if(isNew){
                // 新记录
                if(record.isNewRecord){
                    records.push(cloneLocationRecord(record));
                }
            }else {
                // db中原有记录
                if(!record.isNewRecord){
                    records.push(cloneLocationRecord(record));
                }
            }
        }
        return records;
    }
     /**
     * 拷贝替代物料信息
     */
    function cloneLocationRecord(record) {
        var objClone = new Object();
        // 拷贝属性
        // 替代物料流水号
        objClone['alternateMaterialId'] = record['alternateMaterialId'];
        // 替代物料.替代物料id
        objClone['alterMaterialId'] = record['alterMaterialId'];
         // 替代物料编码
        objClone['materialNo'] = record['materialNo'];
         // 替代物料名称
        objClone['materialName'] = record['materialName'];
        // 相对替换数量
        objClone['qty'] = record['qty'];
         // 优先级
        objClone['priority'] = record['priority'];
         // 有效开始时间
        objClone['effectiveDate'] = record['effectiveDate'];
         // 有效结束时间
        objClone['discontinueDate'] = record['discontinueDate'];
        return objClone;         
    }
     /**
     * 加载登记tab的数据
     * @param record 查询页面gird记录
     */
    function loadRegisterData(record) {
        bFlag = false;
        // 传递替代物料数据
        formPanel.getForm().loadRecord(record);
       
        // 保存表单数据
        objFormDatas = formPanel.getForm().getValues();
        objFormDatas['materialId'] = hdnAltMatId.getValue();
      
        // 删除按钮可用
        btnDelete.setDisabled(false);
        // 载入物料对应的替代物料
        locationStore.load({params:{start: 0, limit : Constants.PAGE_SIZE}} );
        locationStore.on('load',function(){
            // 保存替代物料信息
            objLocationDatas = getLocationList();
        })
    }
    
     /**
     * 增加替代物料
     */
    function addLacation() {
        // 新记录
        var record = new material({
            // 流水号
            alternateMaterialId : null,
            // 替代物料.物料id
            alterMaterialId: null,
            // 替代物料编码
            materialNo : null,
            // 替代物料名称
            materialName : null,
            // 相对替换数量
            qty : "1",
            // 优先级
            priority : "0",
            // 有效开始日期
            effectiveDate : null,
            // 有效结束日期
            discontinueDate : null,
            // 是否是新增记录
            isNewRecord : true
        });
        
         // 原数据个数
        var count = locationStore.getCount();
        if (count == null) {
            count = 0;
        }
        // 停止原来编辑
        materialGrid.stopEditing();
        // 插入新数据
        locationStore.insert(count,record);
        locationStore.totalLength = locationStore.getTotalCount() + 1;
        locationStore.commitChanges();
        materialGrid.getView().refresh();
//        materialGrid.getBottomToolbar().updateInfo();
        // 开始编辑新记录第一列
        materialGrid.startEditing(count,1);
    }
    /**
     * 删除替代物料
     */
    function deleteLocation() {
        var record = materialGrid.selModel.getSelected();
        if(materialGrid.selModel.hasSelection()){
            // 如果选中一行则删除
            locationStore.remove(record);
            locationStore.totalLength = locationStore.getTotalCount() - 1;
            materialGrid.getView().refresh();
            materialGrid.getBottomToolbar().updateInfo();
            // 如果不是新增加的记录,保存删除的流水号
            if (!record.get('isNewRecord')) {
                deleteLocationIds.push(record.get('alternateMaterialId'));
            }                              
      
        }else {
            // 否则弹出提示信息
            Ext.Msg.alert(Constants.SYS_REMIND_MSG,'&nbsp&nbsp&nbsp'+Constants.COM_I_001);
            
        }
    }
    /**
     * 替代物料保存按钮处理
     */
    function saveMaterialHandler() {
        // 信息没有改变直接返回
        if(!isAlterMaterialChanged() && !isLocationChanged()){
            //画面数据未修改，新增时要判断替代物料明细是否有数据
            if(bFlag) {
                msgFlag = checkMsg();
            }
            //msgFlag为false时提示画面数据未修改
            if(!msgFlag) {
                Ext.Msg.alert(Constants.SYS_REMIND_MSG,Constants.COM_I_006);
                return;
            }
        }      
       
        Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_C_001,function(buttonobj){
            if (buttonobj == "yes") {
                if(isformCanSave()){
                    // 保存
                   saveMaterialInfo(refreshMaterialInfo);
                }
                return;
            }
        });     
    }
    /**
     * 表单check
     */
    function isformCanSave() {
        // 新增无替代物料时，
        if( msgFlag ) {
                Ext.Msg.alert(Constants.SYS_REMIND_MSG,Constants.COM_E_018);
                msgFlag = false;
                return false;
        }
        
        // 替代物料信息没有改变，不需要保存
        if(isLocationChanged()){
            // 替代物料编码，相对替换数量，优先级，时间
            var msg = checkIsNull();
            if(msg){
                
                Ext.Msg.alert(Constants.SYS_REMIND_MSG, msg);
                return false;
            }
            
           // 替代物料编码check
            var error = isMaterialRepeat();
            if(error){
                 Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.RI011_E_002);
                 return false;
            }
            var error = isDateRepeat();
            if(error){
                 Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.RI011_E_001);
                 return false;
            }
        }
        // 物料编码，替代物料编码，相对替换数量,优先级
        if( (!txtMaterialNo.getValue()) ){
            return false;
        }
        
        return true;
    }
    /**
     * 检测替代物料是否有重复
     */
    function isMaterialRepeat(){
        for(var i = 0; i < locationStore.getCount(); i++){
             if(locationStore.getAt(i).get('materialNo')==txtMaterialNo.getValue()) {
                 return true;
             }
        }  
        return false;
    }
  
     /**
     * 检测替代物料日期是否有重复
     */
    function isDateRepeat(){
        var dateErrFlg = false;
        for (var i = 0; i < locationStore.getCount(); i++) {
            for (var j = i + 1; j < locationStore.getCount(); j++) {
                if (locationStore.getAt(i).get('materialNo') == locationStore.getAt(j).get('materialNo')) {
                    var startDateI = checkDateIsNull(locationStore.getAt(i).get('effectiveDate'),true);
                    var startDateJ = checkDateIsNull(locationStore.getAt(j).get('effectiveDate'),true);
                    var endDateI = checkDateIsNull(locationStore.getAt(i).get('discontinueDate'),false);
                    var endDateJ = checkDateIsNull(locationStore.getAt(j).get('discontinueDate'),false);
                    if ((startDateI <=startDateJ) && (startDateJ <=endDateI)) 
                        dateErrFlg = true;
                    if ((startDateI <=endDateJ) && (endDateJ <= endDateI))
                        dateErrFlg = true;
                    if ((endDateI>= startDateJ) && (endDateI <= endDateJ)) 
                        dateErrFlg = true;
                    if (dateErrFlg) {
                            break;
                    }
                }  
            }
        }
        return dateErrFlg;
    
    }
      /**
     * 检查日期是否为空
     */
    function checkDateIsNull(date,isStart){
      var msg;
      if(isStart) {
         msg = "1900-01-01"
      }else {
          msg = "2099-12-31";
      }
      if(date) {
          return date;
      }else {
          return msg;
      }
    }
    /**
     * 关键字段check
     */
    function checkIsNull() {
        var msg = "";
        var noError = false;
        var qtyError = false;
        var priError = false;
        var startError = false;
        var endError = false;
        var compareError = false;
        // 判断物料编码为空
        if(!txtMaterialNo.getValue()) {
              msg += String.format(Constants.COM_E_002, '物料编码') ;
        }
       // 循环
        for(var index  = 0; index < locationStore.getCount(); index ++) {
            // 记录
            var record = locationStore.getAt(index).data;
            if(!record.materialNo ) {
                noError = true;
            }
            if(!record.qty){  
                qtyError = true;
            }
            if(!record.priority && !("0" == record.priority)){  
                 priError = true;
            }
            if(!record.effectiveDate){  
                 startError = true;
            }  
            if(!record.discontinueDate){  
                 endError = true;
            }
            if( !startError && !endError && record.effectiveDate&&record.discontinueDate&&!endDateCheck(record)) {  
                 compareError = true;
            }
            if( noError &&    qtyError &&    priError  && compareError) {
                break;
            }
        }
         if(noError ){
            msg += "<br/>" + String.format(Constants.COM_E_002, '替代物料编码') ;
        }
         if(qtyError ){
            msg += "<br/>" + String.format(Constants.COM_E_002, '相对替换数量');
        }
         if(priError){
            msg += "<br/>" + String.format(Constants.COM_E_002, '优先级');
        }

         if(compareError ){
                msg += "<br/>" + String.format(Constants.COM_E_009,"有效开始日期","有效截止日期");
        }
        return msg;
    }
     /**
     * 保存物料信息
     * @param func 保存成功后的操作
     * @param params 操作的参数
     */
    function saveMaterialInfo(func,params) {       
        // 表单提交
        formPanel.getForm().submit({
            url : 'resource/updateAlertMaterial.action',
            method : Constants.POST,
            params : {
                // 物料Id
                materialId : hdnAltMatId.getValue(),
                // 替代物料信息是否更改
                isLocationChanged : isLocationChanged(),
                // 新增加的替代物料记录
                newLocation: Ext.util.JSON.encode(getLocationList(true)),
                // 修改过的替代物料db记录
                dbLocation : Ext.util.JSON.encode(getLocationList()),
                // 删除的替代物料id集
                deleteLocationIds : deleteLocationIds.join(',')
            },
            success : function(form ,action){                
                var result = eval("(" + action.response.responseText +")"); 
                if(result.success){
                    Ext.Msg.alert(Constants.SYS_REMIND_MSG, '&nbsp&nbsp&nbsp'+Constants.COM_I_004);
                    refreshMaterialInfo();
                    // 刷新页面
                    if(params){
                         func.apply(this, [params]);
                    }else{
                        func.apply(this);
                    }
                   
                }else {
                    Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_E_014);
                }
            },
            failure : function(form ,action){
                Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_E_014);
            } 
        });                           
    }
        
     /**
     * 重新记录物料信息表单原始值
     */
    function refreshMaterialInfo() {
        // 保存表单数据
        objFormDatas = formPanel.getForm().getValues();
        objFormDatas['materialId'] = hdnAltMatId.getValue();
        // 删除按钮可用
        btnDelete.setDisabled(false);
        // 查询tab刷新
        materialStore.load({params:{start: 0, limit : Constants.PAGE_SIZE}} );
        // 载入物料对应的替代物料, 参数 物料号
        locationStore.load({params:{start: 0, limit : Constants.PAGE_SIZE}});
        // 清空修改信息
        locationStore.modified = [];
        // 物料编码不可用
        bFlag = false;
    }
    
    // 添加物料grid beforeedit事件
    materialGrid.on('beforeedit', locationBeforeEdit);
    /**
     * grid beforeedit事件处理函数
     */
    function locationBeforeEdit(e){
        // 获取store
        var store = e.grid.getStore();
        // 获取当前记录
        var record = store.getAt(e.row); 
        // 编辑列的字段名
        var fieldName = e.grid.getColumnModel().getDataIndex(e.column);
        // 库位号
        if("materialNo" == fieldName) {
          // db中原有记录不可编辑
          if(!record.get('isNewRecord')){
            e.cancel = true;
            return;
          }
        }
    }
    /**
     * 当且仅当点击物料单元格时弹出物料选择窗口
     */
    function choseEdit(grid, rowIndex, columnIndex, e) {
        if (rowIndex < grid.getStore().getCount()) {
            var record = grid.getStore().getAt(rowIndex);
            var fieldName = grid.getColumnModel().getDataIndex(columnIndex);
            if (fieldName == "materialNo") {
                if(record.get('isNewRecord')) {
                    var mate = window.showModalDialog('../../plan/RP001.jsp', window,'dialogWidth=800px;dialogHeight=550px;status=no');
                    if (typeof(mate) != "undefined") {
                        record.set('materialNo', mate.materialNo);
                        record.set('alterMaterialId', mate.materialId);
                        record.set('materialName', mate.materialName);
                    }
                } 
            }
        }
    }
    /**
     * 选择物料信息表单信息:form
     */
    function selectMaterial() {
        if(bFlag) {        
            var mate = window.showModalDialog('../../plan/RP001.jsp', window,'dialogWidth=800px;dialogHeight=550px;status=no');
            if (typeof(mate) != "undefined") {
                // 设置物料id
                hdnAltMatId.setValue(mate.materialId);
                // 设置物料编码
                txtMaterialNo.setValue(mate.materialNo);
                // 设置物料名称
                txtMaterialName.setValue(mate.materialName);
                // 载入物料对应的替代物料, 参数 物料号
                locationStore.load({params:{start: 0, limit : Constants.PAGE_SIZE}});
            }
            Ext.get("materialName").focus();
        }
    }
 });