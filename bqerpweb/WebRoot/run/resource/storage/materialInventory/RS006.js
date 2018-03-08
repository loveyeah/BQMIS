// 物料盘点表打印
// author:chenshoujiang
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
    Ext.QuickTips.init();   

    // 待盘库位store
    var locationStore = new Ext.data.JsonStore({
        root : 'list',
        url : "resource/getLocationList.action",
        fields : ['locationNo', 'locationName']
    })
    
    locationStore.on('load', function() {
            if(locationStore.getTotalCount() > 0) {
            var recordLocation = locationStore.getAt(0);
            locationStore.remove(recordLocation);
            var record = new Ext.data.Record({
                    locationNo:"",
                    locationName:"&nbsp"
                })
            locationStore.insert(0, record);
            }
    })
    
    // 待盘库位组合框
    var cboStoreLocation = new Ext.form.ComboBox({
        fieldLabel : "待盘库位",
        store : locationStore,
        name:"delayLocation",
        displayField : "locationName",
        valueField : "locationNo",
        disabled:true,
        mode : 'local',
        triggerAction : 'all',
        readOnly : true,
        width:150,
        listeners : {
            select : function() {
                if(cboStoreLocation.getRawValue() == "&nbsp")
                {
                    cboStoreLocation.setRawValue("");
                }
            }
        }
    }); 
            
    // 待盘仓库 DataStore
    var dsDelayStore = new Ext.data.JsonStore({
        root : 'list',
        url : "resource/getWarehouseList.action",
        fields : ['whsNo', 'whsName']
    });
    
    // 待盘仓库
    dsDelayStore.load();
    dsDelayStore.on('load', function() {
        if(dsDelayStore.getTotalCount() > 0) {
            var recordLocation = dsDelayStore.getAt(0);
            dsDelayStore.remove(recordLocation);
            var record = new Ext.data.Record({
                    whsNo:"",
                    whsName:"&nbsp"
                })
            dsDelayStore.insert(0, record);
            }
    })
    
    // 待盘仓库组合框
    var cboDelayStore = new Ext.form.ComboBox({
        fieldLabel : "待盘仓库",
        name : "delayStore",
        width : 150,
        store : dsDelayStore,
        displayField : "whsName",
        valueField : "whsNo",
        mode : 'local',
        triggerAction : 'all',
        readOnly : true,
        listeners : {
            select : function() {
                // 如果待盘仓库数据为空，则待盘库位也为空
                if(cboDelayStore.getRawValue() == "&nbsp")
                {
                    cboDelayStore.setRawValue("");
                    cboStoreLocation.setValue("");
                    cboStoreLocation.setRawValue("");
                    cboStoreLocation.setDisabled(true);
                }
                else if(cboDelayStore.getRawValue() != "&nbsp"){
//                  alert("000");
                    cboStoreLocation.clearValue();
//                  cboStoreLocation.setValue("");
//                  cboStoreLocation.setRawValue("");
                    // 否则检索待盘库位数据
                    locationStore.load({
                        params : {
                            whsNo : cboDelayStore.getValue()
                        }
                    });
//                  alert("111");
                    // 设置为可执行
                    cboStoreLocation.setDisabled(false);
//                  alert("222");
                    
                }
            }
        }
    });
    var root = new Ext.tree.AsyncTreeNode({
                id : '-1',
                name : '物料',
                text : '物料'
            })
    // 待盘物料分类
    var cbtMaterialSort = new Ext.ux.ComboBoxTree({
        fieldLabel : '待盘物料分类',
        id : 'cbtMaterialSort',
        displayField : 'text',
        width : 180,
        valueField : 'id',
        blankText : '请选择',
        emptyText : '请选择',
        readOnly : true,
        tree : {
            xtype : 'treepanel',
            loader : new Ext.tree.TreeLoader({
                        dataUrl : 'resource/getMaterialClass.action'
                    }),
            root : root
        },
        selectNodeModel : 'all'
        });
    
    // 待盘物料textfield
    var txtMaterial = new Ext.form.TextField({
        id : "txtMaterial",
        name:"delayMaterial",
        width:150,
        readOnly : true,
        fieldLabel:'待盘物料',
        listeners : {
            focus : selectMaterial
        }
    }); 
    
        // 判断是否是叶子结点的隐藏域
    var hiddenTxtMaterial = new Ext.form.Hidden({
        id : "hiddenTxtMaterial",
        value:""
    });
   // 物料编码名称隐藏域
    var hiddenMaterialCodeName= new Ext.form.Hidden({
        id : "hiddenMaterialCodeName",
        value:""
    });
    // 物料分类隐藏域
    var hiddenMaterialSortId = new Ext.form.Hidden({
        id : "hiddenMaterialSortId",
        value:""
    });
     // 物料分类隐藏域
    var hiddenMaterialSortName = new Ext.form.Hidden({
        id : "hiddenMaterialSortName",
        value:""
    });
    // 待盘仓库隐藏域
    var hiddenDelayStore = new Ext.form.Hidden({
        id : "hiddenDelayStore",
        value:""
    });
    // 待盘库位隐藏域
    var hiddenDelayLocation = new Ext.form.Hidden({
        id : "hiddenDelayLocation",
        value:""
    });
    // 待盘物料隐藏域
    var hiddenDelayMaterial = new Ext.form.Hidden({
        id : "hiddenDelayMaterial",
        value:""
    });
    // 计划员隐藏域
    var hiddenPlaner = new Ext.form.Hidden({
        id : "hiddenPlaner",
        value:""
    });
    
    // 计划员 DataStore
    var dsPlaner = new Ext.data.JsonStore({
        root : 'root',//modify by ywliu 09/05/19
        url : "resource/getPlaner.action",
        fields : ['planer', 'planerName']
    });
    
    // 待盘仓库
    dsPlaner.load();
    
    // 计划员组合框
    var cboPlanMember = new Ext.form.ComboBox({
        fieldLabel : "计划员",
        id : "cboPlanMember",
        name : "planer",
        width : 150,
        store : dsPlaner,
        displayField : "planerName",
        valueField : "planer",
        mode : 'local',
        triggerAction : 'all',
        readOnly : true,
        listeners : {
            select : function() {
                if(cboPlanMember.getRawValue() == "&nbsp")
                {
                    cboPlanMember.setValue("");
                    cboPlanMember.setRawValue("");
                    
                }
            }
        }
    });
            
    // 查询按钮
    var btnQuery = new Ext.Button({
        text : "查询",
        iconCls : Constants.CLS_QUERY,
        handler : queryMaterial
    });     
    
    // 打印盘点表按钮
    var btnPrint = new Ext.Button({
        text : "打印",
        iconCls : 'print',
        disabled:true,
        handler : printMaterialType
    }); 
       
    // head工具栏
    var headTbar = new Ext.Toolbar({
        region : 'north',
//      border:false,
        items : ['待盘仓库:',cboDelayStore, 
         '&nbsp&nbsp待盘库位:',cboStoreLocation,'&nbsp&nbsp待盘物料分类:',cbtMaterialSort]
    });
       // 定义grid中的数据
    var gridData = new Ext.data.Record.create([{
            // 流水号
            name : 'materialId'
        },{
            // 编码
            name : 'materialNo'
        }, {
            // 名称
            name : 'materialName'
        }, {
            // 规格型号
            name : 'specNo'
        }, {
            // 存货计量单位
            name : 'stockUmId'
        }, {
            // 仓库编码
            name : 'whsNo'
        }, {
            // 仓库名称
            name : 'whsName'
        }, {
            // 库位编码
            name : 'location'
        }, {
            // 库位名称
            name : 'locationName'
         }, {
            // 批号
            name : 'lotNo'
         }, {
            // 账面数量
            name : 'account'
    }]);
    
    // jsonStore
    var gridStore = new Ext.data.JsonStore({
        url : 'resource/getMaterialList.action',
        root : 'list',
        totalProperty : "totalCount",
        sortInfo : {field: "materialNo", direction: "ASC"},
        fields : gridData
    });
    
    // 分页工具栏
    var pagebar = new Ext.PagingToolbar({
        pageSize : Constants.PAGE_SIZE,
        store : gridStore,
        displayInfo : true,
        displayMsg : Constants.DISPLAY_MSG,
        emptyMsg : Constants.EMPTY_MSG
    }); 
    
    //设置只能单选
    var sm = new Ext.grid.RowSelectionModel({singleSelect : true})
            // grid工具栏
    var gridTbar = new Ext.Toolbar({
    	border:false,
        height:25,
        items:['待盘物料:', txtMaterial, 
        	'&nbsp&nbsp&nbsp计&nbsp划&nbsp员&nbsp:', cboPlanMember, '-',btnQuery ,'-', btnPrint,
            hiddenTxtMaterial,hiddenMaterialCodeName,hiddenMaterialSortId,hiddenDelayStore,hiddenDelayLocation
            ,hiddenDelayMaterial,hiddenPlaner,hiddenMaterialSortName
                ]
    });
    // 弹出窗口物料盘点的grid
    var gpMaterial = new Ext.grid.GridPanel({
        region : "center",
        border:false,
        bbar : pagebar,     
        autoExpandColumn : 'materialNo',
        enableColumnHide:true,
        width:800,
        enableColumnMove : false,
        viewConfig : {
            forceFit : true
            // 还会对超出的部分进行缩减，让每一列的尺寸适应GRID的宽度大小，阻止水平滚动条的出现
        },
        store : gridStore,
        columns : [                    
                    // 自动生成行号
                    new Ext.grid.RowNumberer({
                           header : '行号',
                           width:35
                  }),
            {             
                // 流水号
                hidden:true,
                dataIndex : 'materialId'
            },
            {             
                header : "物料编码",
                sortable : true,
                width:50,
                dataIndex : 'materialNo'
            },
            {   
                header : "物料名称",
                sortable : true,
                width:50,
                dataIndex : 'materialName'
            },
            {   
                header : "规格型号",
                sortable : true,
                width:50,
                dataIndex : 'specNo'
            },
            {   
                header : "单位",
                sortable : true,
                width:50,
                dataIndex : 'stockUmId'
            },
            {   
                header : "仓库",
                sortable : true,
                width:50,
                dataIndex : 'whsName'
            },
            {   
                header : "库位",
                sortable : true,
                width:50,
                dataIndex : 'locationName'
            },
            {   
                header : "批号",
                sortable : true,
                width:50,
                dataIndex : 'lotNo',
                renderer:function(value){
                	if(value == "0") {
                		return "";
                	} 
                	return value;
                }
            },
            {   
                header : "账面数量",
                sortable : true,
                width:50,
                dataIndex : 'account',
                align : 'right'
            },
            {   
                header : "实盘数量",
                sortable : true,
                hidden : true,
                width:50,
                dataIndex : ''
            }        
        ],
        tbar : gridTbar,
        sm:sm
    });

    // 加载数据时触发
    gridStore.on('beforeload', function() {
        this.baseParams = {
                materialSortId: hiddenMaterialSortId.getValue(),
                delayStore: hiddenDelayStore.getValue(),
                delayLocation:hiddenDelayLocation.getValue(),
                delayMaterial: hiddenDelayMaterial.getValue(),
                planer:hiddenPlaner.getValue()
        };
    });
    
    // 显示区域
    var layout = new Ext.Viewport({
        layout : 'border',
//        margins : '0 0 0 0',
//        border : false,
        autoHeight : true,
        items : [headTbar,gpMaterial]        
    });
    
    /**
     * 查询按钮按下时
     */
    function queryMaterial() 
    {
        hiddenMaterialSortId.setValue(cbtMaterialSort.getValue());
        hiddenMaterialSortName.setValue(cbtMaterialSort.getRawValue());
        hiddenDelayStore.setValue(cboDelayStore.getValue());
        hiddenDelayLocation.setValue(cboStoreLocation.getValue());
        hiddenDelayMaterial.setValue(hiddenTxtMaterial.getValue());
        hiddenPlaner.setValue(cboPlanMember.getValue());
        hiddenMaterialCodeName.setValue(txtMaterial.getValue());
        // 载入数据
        gridStore.load({
            params : {
                start : 0,
                limit : Constants.PAGE_SIZE
            },
            callback: function()　{
                if(gridStore.getTotalCount() > 0)
                    {
                        // 设置为可用
                        btnPrint.setDisabled(false);
                    }else 
                    {   
                        // 如果检索数据为空，设置打印按钮不可用
                        btnPrint.setDisabled(true);
                    }
            }
        });
    }
    
    /**
     * 物料盘点打印按钮按下时
     */
    function printMaterialType() 
    {
        
        // 调用报表接口 返回参数
        var delayStore1 = hiddenDelayStore.getValue();
        var delayLocation1 = hiddenDelayLocation.getValue();
        var delayMaterial1 = hiddenDelayMaterial.getValue();
        var planer1 = hiddenPlaner.getValue();
        var materialSortId1 = hiddenMaterialSortId.getValue();
        
        // 点击打印盘点表按钮时 把数据返回到点击查询按钮时的状态
        if(hiddenMaterialSortId.getValue() != "")
        {
            var obj = new Object();
            obj.id = hiddenMaterialSortId.getValue();
            obj.text = hiddenMaterialSortName.getValue();
            cbtMaterialSort.setValue(obj);
        }else if(cbtMaterialSort.getValue()!= ""){
            var obj = new Object();
            obj.id = "";
            obj.text = "";
            cbtMaterialSort.setValue(obj);
        }
        if(hiddenDelayStore.getValue() != "")
        {
            cboDelayStore.setValue(hiddenDelayStore.getValue());
            cboStoreLocation.setDisabled(false);
        }else {
            cboDelayStore.setValue("");
            cboDelayStore.setRawValue("");
            cboStoreLocation.setDisabled(false);
        }
        if(hiddenDelayLocation.getValue() != "")
        {
            cboStoreLocation.setValue(hiddenDelayLocation.getValue());
        }else {
            cboStoreLocation.setValue("");
            cboStoreLocation.setRawValue("");
            cboStoreLocation.setDisabled(true);
        }
        if(hiddenMaterialCodeName.getValue() != "")
        {
            txtMaterial.setValue(hiddenMaterialCodeName.getValue());
            hiddenTxtMaterial.setValue(hiddenDelayMaterial.getValue());
        }
        else {
            txtMaterial.setValue("");
            hiddenTxtMaterial.setRawValue("");
        }
        if(hiddenPlaner.getValue() != "")
        {
            cboPlanMember.setValue(hiddenPlaner.getValue());
        }else {
            cboPlanMember.setValue("");
            cboPlanMember.setRawValue("");
        }
        
        Ext.lib.Ajax.request('POST',
                'resource/saveMaterialPrint.action',
                    {
                        success : function(result, request) {
                        if (result.responseText) {
                            var o = eval("(" + result.responseText + ")");
                            var succ = o.is;
                            if(succ == "PRT"){
                                	var materialNo = o.materialNo;
                                	var column = o.column;
                                	Ext.Msg.alert(Constants.SYS_REMIND_MSG,
                                            String.format(Constants.RS006_E_001,materialNo)); 
                            }else if(succ == "true")
                            {
                                	var bookNo = o.bookNo;
                                    Ext.Msg.alert(Constants.SYS_REMIND_MSG,
                                        Constants.COM_I_004)
                                        window.open(application_base_path + "report/webfile/materialCheckPrint.jsp?delayStore="+ delayStore1
                                          + "&delayLocation="+delayLocation1
                                          +"&delayMaterial="+ delayMaterial1
                                          +"&planer="+planer1
                                          +"&materialSortId="+materialSortId1
                                          +"&bookNo="+ bookNo);
    
                            } else {
                                	Ext.Msg.alert(Constants.SYS_REMIND_MSG,
                                            Constants.COM_E_014);
                                }
                            }
                        } 	
                 }, "delayStore=" + delayStore1
                    +"&materialSortId=" + materialSortId1
                    +"&delayMaterial="+delayMaterial1
                    +"&planer="+planer1
                    +"&delayLocation=" + delayLocation1);
        
    }
    /**
     * 去掉右空格
     */
    String.prototype.RTrim = function() 
    {
        return this.replace(/(\s*$)/g, ""); 
    };
    
    /**
     * 选择物料信息表单信息:form
     */
    function selectMaterial() {
        this.blur();
        var mate = window.showModalDialog('../../plan/RP001.jsp', window,'dialogWidth=800px;dialogHeight=550px;status=no');
        if (typeof(mate) != "undefined") {
            // 设置物料编码
            hiddenTxtMaterial.setValue(mate.materialNo);
            // 设置物料名称
            txtMaterial.setValue(mate.materialName);
        }
    }
})