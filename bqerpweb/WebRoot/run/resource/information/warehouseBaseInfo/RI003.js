Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
// 常量 仓库删除出错信息
Constants.RI003_E_001 = "当前仓库的物料不为空，不能删除。";

Ext.onReady(function() {
    Ext.QuickTips.init();   

    // ↓↓*******************仓库基础资料查询tab**************************************

    // 仓库grid的store
    var wareHouseStore = new Ext.data.JsonStore({
        url : 'resource/getWarehouseInfoList.action',
        root : 'list',
        totalProperty : 'totalCount',
        fields : [
            // 流水号
            {name:'whsId'},
            // 仓库编号
            {name: 'whsNo'}, 
            // 仓库名称
            {name: 'whsName'},
            // 联系人
            {name: 'contactMan'},
            // 联系电话
            {name: 'tel'},
            // 地址            
            {name: 'address'},
            // 仓库描述
            {name: 'whsDesc'},
            // 传真号
            {name:'fax'},
            // 可分配仓库
            {name:'isAllocatableWhs'},
            // 是否寄存库
            {name : 'isInspect'},
             // 是否计成本仓库
            {name : 'isCost'},
            // 上次修改时间
            {name : 'lastModifiedDate'}]
        });
    // 载入数据
    wareHouseStore.load({params:{start: 0, limit : Constants.PAGE_SIZE}} );
    // 查询字符串(仓库编码/名称)
    var fuzzy = new Ext.form.TextField({
        id : "fuzzy",
        emptyText : "仓库编码/名称",
        name : "fuzzy"
    }); 
     // before load事件,传递查询字符串作为参数
     wareHouseStore.on('beforeload', function() {
                Ext.apply(this.baseParams, {
                            fuzzy : fuzzy.getValue()
                        });
            });
    // 当没有数据时确认按钮不可用
    wareHouseStore.on('load',function(store,records){
        if(store.getCount() == 0){
            Ext.getCmp('btnOk').setDisabled(true);
        }else{
            Ext.getCmp('btnOk').setDisabled(false);
        }
   });
    // 仓库grid
    var wareHouseGrid = new Ext.grid.GridPanel({
        region : "center",
        layout : 'fit',
        // 标题不可以移动
        enableColumnMove : false,
        store : wareHouseStore,
        // 单选
        sm : new Ext.grid.RowSelectionModel({singleSelect : true}),
        columns : [
            new Ext.grid.RowNumberer({header:"行号",width : 35}),
            // 仓库编码
            {   header : "仓库编码",
                width : 40,
                sortable : true,
                defaultSortable : true,
                dataIndex : 'whsNo'
            },
            // 仓库名称
            {   header : "仓库名称",
                width : 100,
                sortable : true,
                dataIndex : 'whsName'
            },
            // 联系人
            {   header : "联系人",
                width : 40,
                sortable : true,
                renderer : function(val) {
						if (val) {
							var url = "resource/findPersonNameByCode.action?workerCode="
									+ val
							var conn = Ext.lib.Ajax.getConnectionObject().conn;
							conn.open("POST", url, false);
							conn.send(null);
							if (conn.status == "200") {
								return conn.responseText;
							} else {
                                return "";
							}
						}else{
                            return "";
                        }
					},
                dataIndex : 'contactMan'
            },
            // 联系电话
            {   header : "联系电话",
                width : 40,
                sortable : true,
                dataIndex : 'tel'
            },
            // 仓库地址
            {   header : "仓库地址",
                width : 100,
                sortable : true,
                dataIndex : 'address'
            },
            // 仓库描述
            {   header : "仓库描述",
                width : 150,
                sortable : true,
                dataIndex : 'whsDesc'            
            }
        ],
        viewConfig : {
            forceFit : true
        },
        tbar : [
            fuzzy,"-",
            {
                text : "模糊查询",
                iconCls : Constants.CLS_QUERY,
                handler : function() {
                    wareHouseStore.load({params:{start: 0, limit : Constants.PAGE_SIZE}} );
                }
            },"-",
            {
                text : "确认",
                iconCls : Constants.CLS_OK,
                id : "btnOk",
                handler : function() {
                    // 如果没有选择，弹出提示信息
                    if(!wareHouseGrid.selModel.hasSelection()){
                        Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_001);
                        return;
                    }
                    // 进入登记页面
                    registerWarehouse();
                }
            }],
        // 分页
        bbar : new Ext.PagingToolbar({
            pageSize : Constants.PAGE_SIZE,
            store : wareHouseStore,
            displayInfo : true,
            displayMsg : Constants.DISPLAY_MSG,
            emptyMsg : Constants.EMPTY_MSG
        })
    });
    // 双击进入登记tab
    wareHouseGrid.on("rowdblclick", registerWarehouse);   
    // 查询tab
    var queryPanel = new Ext.Panel({
        layout : 'fit',
        title : '查询',
        items : [wareHouseGrid]
    });
    // ↑↑*******************仓库基础资料查询tab**************************************    
    
    // 保存仓库的原始数据，用来判断数据有没有变更
    var objFormDatas = null; 
    // 保存原始库位信息，用来判断数据有没有改变
    var objLocationDatas =[];
    // 保存已删除的库位的编号
    var deleteLocationIds = [];
    // ↓↓*******************仓库基础资料登记tab**************************************
    // 增加按钮
    var btnAdd = new Ext.Button({
        text : '新增',
        iconCls : Constants.CLS_ADD,
        handler : addWarehouseInfo
    });
    // 删除按钮
    var btnDelete = new Ext.Button({
        text : Constants.BTN_DELETE,
        iconCls : Constants.CLS_DELETE,
        disabled : true,
        handler : deleteWarehouse
    });
    // 保存按钮
    var btnSave = new Ext.Button({
        text : Constants.BTN_SAVE,
        iconCls : Constants.CLS_SAVE,
        handler : saveWarehouseHandler
    });    
    // 仓库流水号
    var hdnWhsId = new Ext.form.Hidden({
        id: "whsId",
        name: "whs.whsId"
    });
    // 仓库编码
    var txtWhsNo = new Ext.form.TextField({
        fieldLabel : '仓库编码<font color="red">*</font>' ,
        isFormField : true,
        allowBlank : false,
        maxLength : 10,
        codeField : "yes",
        style :{'ime-mode' : 'disabled'},
        id : "whsNo",
        name : "whs.whsNo",
        anchor : '100%'
    });
    // 上次修改时间
    var hdnLastModifiedDate = new Ext.form.Hidden({
        id : "lastModifiedDate",
        name : "whs.lastModifiedDate"
    }); 
    // 仓库名称
    var txtWhsName = new Ext.form.TextField({
        fieldLabel : '仓库名称<font color="red">*</font>' ,
        allowBlank : false,
        maxLength : 50,
        id : "whsName",
        name : "whs.whsName",
        anchor : '100%'       
    });
    // 第一行
    var secondLine = new Ext.Panel({
        border : false,
        height : 30,
        layout : "column",
        style : "padding-top:5px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
        anchor : '100%',
        items:[{
               columnWidth : 0.2,
               layout : "form",
               border : false,
               items : [txtWhsNo, hdnWhsId]
               },{
               columnWidth : 0.8,                                     
               layout : "form",
               border : false,
               items : [txtWhsName, hdnLastModifiedDate]
               }]
    });
    // 联系电话
    var txtTel = new Ext.form.TextField({
        fieldLabel : '联系电话' ,        
        id : "tel",
        maxLength : 18,
        style :{ 'ime-mode' : 'disabled'},
        name : "whs.tel",
        anchor : '100%'
    });
    // 传真号码
    var txtFax = new Ext.form.TextField({
        fieldLabel : '传真号码' ,        
        id : "fax",
        maxLength : 18,
        style :{'ime-mode' : 'disabled'},
        name : "whs.fax",
        anchor : '100%'
    });
    // 联系人
    var txtContactMan = new Ext.form.TextField({
        fieldLabel : '联系人' ,        
        id : "contactManName",
        readOnly : true,
        anchor : '100%'
    });
   // 联系人的单击事件
    txtContactMan.onClick(popupSelect);
    // 隐藏的联系人
    var hiddenTxtContactMan = new Ext.form.Hidden({
        id : 'contactMan',        
        readOnly : true,       
        name : "whs.contactMan"
    });
// 显示人员选择画面
    function popupSelect() {
        this.blur();
        var args = new Object();
        args.selectModel = "single";
        args.rootNode = {
            id : '-1',
            text : '合肥电厂'
        };
        var object = window.showModalDialog(
                        '../../../../comm/jsp/hr/workerByDept/workerByDept.jsp',
                        args,
                        'dialogWidth=800px;dialogHeight=520px;center=yes;help=no;resizable=no;status=no;');
        if (object) {
            if (typeof(object.workerCode) != "undefined") 
               hiddenTxtContactMan.setValue(object.workerCode);
            if (typeof(object.workerName) != "undefined")
                txtContactMan.setValue(object.workerName);
        } else {
            hiddenTxtContactMan.setValue("");
            txtContactMan.setValue("");
        }
    }
    // 第二行
    var thirdLine = new Ext.Panel({
        border : false,
        height : 30,
        layout : "column",
        style : "padding-top:0px;padding-bottom:0px;margin-bottom:0px",
        anchor : '100%',
        items:[{
               columnWidth : 0.2,
               layout : "form",
               border : false,
               items : [txtTel]
               },{
               columnWidth : 0.2,
               layout : "form",
               border : false,
               items : [txtFax]
               },{
               columnWidth : 0.2,
               layout : "form",
               border : false,
               items : [txtContactMan, hiddenTxtContactMan]
               },{
               columnWidth : 0.4, 
               border : false
               }]
    });
    // 可分配仓库
    var chkIsAllocatableWhs = new Ext.form.Checkbox({
        boxLabel :'可分配仓库',
        id : 'isAllocatableWhs', 
        isFormField:true,
        name : "whs.isAllocatableWhs",
        hideLabel : true,
        inputValue : Constants.CHECKED_VALUE
    });
    // 是否寄存库
    var chkIsInspect = new Ext.form.Checkbox({
        boxLabel :'是否寄存库',
        isFormField:true,
        id : "isInspect",
        name : "whs.isInspect",
        hideLabel : true,
        inputValue : Constants.CHECKED_VALUE
    });
    // 是否计成本仓库
    var chkIsCost= new Ext.form.Checkbox({
        boxLabel :'是否计成本仓库',
        id : "isCost",
        name : "whs.isCost",
        isFormField:true,
        hideLabel : true,
        inputValue : Constants.CHECKED_VALUE
    });
    // 第三行
    var forthLine = new Ext.Panel({
        border : false,
        hideLabel : true,
        style : "padding-top:0px;padding-bottom:0px;margin-bottom:0px",
        height : 20,
        isFormField:true,
        layout : "column",
        anchor : '100%',
        items:[{
               columnWidth : 0.12,
               layout : "form",
               border : false,
               items : [chkIsAllocatableWhs]
               },{
               columnWidth : 0.12,
               layout : "form",
               border : false,
               items : [chkIsInspect]
               },{
               columnWidth : 0.14,
               layout : "form",
               border : false,
               items : [chkIsCost]
               },{
               columnWidth : 0.62, 
               border : false
               }]
    });
    // 仓库地址
    var txtAddress = new Ext.form.TextField({
        fieldLabel : '仓库地址' ,
        height : 22,
        id : "address",
        maxLength : 50,        
        name : "whs.address",
        anchor : '100%'
    });
    // 仓库描述
    var txtWhsDesc = new Ext.form.TextField({
        fieldLabel : '仓库描述' , 
        height : 22,
        id : "whsDesc",
        maxLength : 100,
        name : "whs.whsDesc",
        anchor : '100%'
    });
    
    // 增加库位按钮
    var btnAddLocation = new Ext.Button({
        text : "新增库位",
        iconCls : Constants.CLS_ADD,
        handler : addLacation
    });
    // 删除库位按钮
    var btnDeleteLocation = new Ext.Button({
        text : "删除库位",
        iconCls : Constants.CLS_DELETE,
        handler : deleteLocation
    });  
    // 库位记录
    var location = Ext.data.Record.create([
            // 流水号
            {name: 'locationId'},
            // 库位号
            {name: 'locationNo'}, 
            // 库位名称
            {name: 'locationName'},
            // 库位描述
            {name: 'locationDesc'},
            // 是否默认库位
            {name: 'isDefault'},
            // 上次修改时间
            {name : 'lastModifiedDate'},
            // 用于判断记录是新增加的还是从数据库中读出来的
            {name:'isNewRecord'}]);
        // 库位grid的store
        var locationStore = new Ext.data.JsonStore({
            url : 'resource/getLocationListByWhsNo.action',
            root : 'list',
            totalProperty : 'totalCount',
            fields : location
        });
        locationStore.on('load',function(){
            // 保存库位信息
            objLocationDatas = getLocationList();
        })
        // 传递参数
        locationStore.on('beforeload',function(){
            // 仓库号
            Ext.apply(this.baseParams, {
                     whsNo:Ext.get("whsNo").dom.value
             });
        });
        // 是否默认库位列 显示为checkbox
        var isDefaultLocation = new Ext.grid.CheckColumn({
            header : "是否默认库位",
            dataIndex:"isDefault",
            width: 40
        });
        
        // 库位grid
        var locationGrid = new Ext.grid.EditorGridPanel({
            region : "center",
            layout : 'fit',
            height : 290,
            isFormField : false,
            border : true,
            // 单选
            sm : new Ext.grid.RowSelectionModel({singleSelect : true}),
            tbar : [btnAddLocation,"-", btnDeleteLocation],
            anchor : "0",
            // 标题不可以移动
            enableColumnMove : false,
            // 单击修改
            clicksToEdit : 1,
            store : locationStore,
            columns : [
                new Ext.grid.RowNumberer({header:"行号",width : 35}),
                // 库位号
                {   header : "库位号<font color='red'>*</font>",
                    width : 30,
                    sortable : true,
                    defaultSortable : true,
            	    css:CSS_GRID_INPUT_COL,
                    editor : new Ext.form.TextField({
                       	maxLength : 10,
                        codeField : "yes",
                        style :{'ime-mode' : 'disabled' }
                    }),
                    dataIndex : 'locationNo'
                },
                // 库位名称
                {   header : "库位名称<font color='red'>*</font>",
                    width : 100,
            	    css:CSS_GRID_INPUT_COL,
                    editor : new Ext.form.TextField({
                        maxLength : 50
                    }),
                    sortable : true,
                    dataIndex : 'locationName'
                },
                // 库位描述
                {   header : "库位描述",
                    width : 200,
            	    css:CSS_GRID_INPUT_COL,
                    editor : new Ext.form.TextField({
                    	maxLength : 100
                    }),
                    sortable : true,
                    dataIndex : 'locationDesc'
                },
                // 是否默认库位
                isDefaultLocation],
            autoSizeColumns : true,
            plugins :[isDefaultLocation],
            // 分页
			bbar : new Ext.PagingToolbar({
					pageSize : Constants.PAGE_SIZE,
					store : locationStore,
					displayInfo : true,
					displayMsg : Constants.DISPLAY_MSG,
					emptyMsg : Constants.EMPTY_MSG
			}),
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
        height : 165,
        anchor :"0",
        labelPad : 5,
        labelWidth : 65,
        border : false,
        items:[secondLine, thirdLine, forthLine, txtAddress, txtWhsDesc]
    });
    // 登记tab
    var registerPanel = new Ext.Panel({
        layout : 'border',        
        title : '登记',
        margins : '0 0 0 0',
        border : false,
        items :[formPanel, locationGrid]
    });
    // ↑↑*******************仓库基础资料登记tab**************************************
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
    /**
     * 双击查询grid记录，进入登记tab
     */
    function registerWarehouse(){
        // 选择的记录
        var record = wareHouseGrid.getSelectionModel().getSelected(); 
        // 转到登记tab
        tabPanel.setActiveTab(1);
        // 判断登记页面要不要保存
        if(isWareHouseChanged()|| isLocationChanged()){
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
        deleteLocationIds = [];
        objFormDatas = [];
        // 传递仓库数据
        formPanel.getForm().loadRecord(record);
        // 是否分配仓库
        if(record.get('isAllocatableWhs') == Constants.CHECKED_VALUE){
            chkIsAllocatableWhs.setValue('true');
        }
        // 是否计成本仓库
        if(record.get('isCost') == Constants.CHECKED_VALUE){
            chkIsCost.setValue(true);
        }
        // 是否寄存库
        if(record.get('isInspect') == Constants.CHECKED_VALUE){
            chkIsInspect.setValue(true);
        }
        // 查找人员名称
        if (hiddenTxtContactMan.getValue()) {
			var url = "resource/findPersonNameByCode.action?workerCode="
					+ hiddenTxtContactMan.getValue();
			var conn = Ext.lib.Ajax.getConnectionObject().conn;
			conn.open("POST", url, false);
			conn.send(null);
			if (conn.status == "200") {
				txtContactMan.setValue(conn.responseText);
			} else {
				txtContactMan.setValue("");
			}
		}else{
            txtContactMan.setValue("");
        }
        // 保存表单数据
        objFormDatas = formPanel.getForm().getValues();
        objFormDatas['isAllocatableWhs'] = chkIsAllocatableWhs.getValue();
        objFormDatas['isCost'] = chkIsCost.getValue();
        objFormDatas['isInspect'] = chkIsInspect.getValue();

        // 修改时，仓库编码不允许改      
        Ext.get("whsNo").dom.readOnly = true;
        // 删除按钮可用
        btnDelete.setDisabled(false);

        // 载入仓库对应的库位
        locationStore.load({params:{start: 0, limit : Constants.PAGE_SIZE}} );
    }
    /**
     * 判断库位信息有没有改变
     */
    function isLocationChanged() {
        // 新库位记录
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
			// 库位号
			if (oldRecs[i]['locationNo'] != objLocationDatas[i]['locationNo'])
			{return true};
			// 库位名称
			if (oldRecs[i]['locationName'] != objLocationDatas[i]['locationName'])
			{return true};
			// 库位描述
			if (oldRecs[i]['locationDesc'] != objLocationDatas[i]['locationDesc'])
			{return true};
			// 是否默认库位
			if (oldRecs[i]['isDefault'] != objLocationDatas[i]['isDefault'])
			{return true};
		}
		return false;
    }
    /**
	 * 判断仓库信息有没有变化
	 */
    function isWareHouseChanged(){
        // 获取现在的表单值
        var objForm = formPanel.getForm().getValues();
        objForm['isAllocatableWhs'] = chkIsAllocatableWhs.getValue();
        objForm['isCost'] = chkIsCost.getValue();
        objForm['isInspect'] = chkIsInspect.getValue();
        // 循环判断
        for(var prop in objFormDatas){
            if(objForm[prop] != objFormDatas[prop]){                
                return true;
            }
        }        
        // 增加仓库时判断画面有无输入
        if(objFormDatas == null ){
            if(objForm['isAllocatableWhs'] || objForm['isCost'] || objForm['isInspect'])
                return true;
            if(objForm['whs.whsId'] || objForm['whs.whsNo'] || objForm['whs.whsName'] || 
                objForm['whs.tel'] ||  objForm['whs.fax'] || objForm['whs.contactMan'] || 
                objForm['whs.address'] || objForm['whs.whsDesc']){
                    return true
                }
            
        }
        return false;
    }
    /**
     * 增加库位 
     */
    function addLacation() {
        // 新记录
        var record = new location({
            // 库位号
            locationNo:null,
            // 库位名称
            locationName:null,
            // 库位描述
            locationDesc:null,
            // 是否默认库位
            isDefault:"N",
            // 新增加的记录
            isNewRecord: true
        });
        // 原数据个数
        var count = locationStore.getCount();
        // 停止原来编辑
        locationGrid.stopEditing();
        // 插入新数据
        locationStore.insert(count,record);
        locationGrid.getView().refresh();
        locationStore.totalLength = locationStore.getTotalCount() + 1;
        locationGrid.getBottomToolbar().updateInfo();
        // 开始编辑新记录第一列
        locationGrid.startEditing(count,1);
    }
    /**
     * 删除库位
     */
    function deleteLocation() {
        var record = locationGrid.selModel.getSelected();
        if(locationGrid.selModel.hasSelection()){
            // 如果选中一行则删除
			locationStore.remove(record);
            locationStore.totalLength = locationStore.getTotalCount() - 1;
			locationGrid.getView().refresh();
            locationGrid.getBottomToolbar().updateInfo();
			// 如果不是新增加的记录,保存删除的流水号
			if (!record.get('isNewRecord')) {
				deleteLocationIds.push({
                    'locationId' : record.get('locationId'),
                    'lastModifiedDate' : record.get('lastModifiedDate')
                 });
			}                              
      
        }else {
            // 否则弹出提示信息
            Ext.Msg.alert(Constants.SYS_REMIND_MSG,'&nbsp&nbsp&nbsp'+Constants.COM_I_001);
            
        }
    }
    /**
	 * 删除仓库信息
	 */
    function deleteWarehouse() {
        // 仓库流水号
        var whsId = hdnWhsId.getValue(); 
        // 仓库号
        var whsNo = txtWhsNo.getValue();
        // 弹出删除确认信息
       Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_C_002,
            function(buttonobj) {
                if (buttonobj == "yes") {
                     // 判断仓库能否被删除
                    Ext.lib.Ajax.request('POST',
                            'resource/isWarehouseCanRemove.action', {
                                success : function(action) {
                                    var result = eval(action.responseText);
                                    if (result) {
                                         // 删除
                                        Ext.lib.Ajax.request('POST',
                                             'resource/deleteWarehouse.action',
                                             {success : function(action) {
                                                  var result = eval("(" + action.responseText + ")");
                                                  if(result.success){
                                                        Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_005); 
                                                        // 初始化
                                                       clearRegisterTab();
                                                  }else{
                                                        Ext.Msg.alert(Constants.SYS_REMIND_MSG, result.msg);
                                                  }
                                                },
                                                failure :function(){
                                                    Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_E_014);
                                                }
                                              }, 'whsId=' + whsId + "&lastModifiedDate=" + hdnLastModifiedDate.getValue());
                                    }else{
                                        // 物料数不为零，不能删除
                                        Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.RI003_E_001);
                                    }
                                }
                            }, 'whsNo=' + whsNo);
                }
            });
       
    }
    /**
     * 仓库保存按钮处理
     */
    function saveWarehouseHandler() {
        // 信息没有改变直接返回
        if(!isWareHouseChanged() && !isLocationChanged()){
            Ext.Msg.alert(Constants.SYS_REMIND_MSG,Constants.COM_I_006);
            return;
        }
        Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_C_001,function(buttonobj){
                if (buttonobj == "yes") {   
                    if(isformCanSave()){
                        // 保存
                        saveWarehouseInfo();                    
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
        // 仓库号和仓库编码不可以为空
        if(!txtWhsNo.getValue()){
            msg += String.format(Constants.COM_E_002, '仓库编码') ;
        }
        if( !txtWhsName.getValue()){
            msg += "<br/>" + String.format(Constants.COM_E_002, '仓库名称');
        }
        // 库位号和库位编码不可以为空
        msg += checkLocationNoAndName();
      if(msg){
                Ext.Msg.alert(Constants.SYS_REMIND_MSG, msg);
                return false;
      }
      if(!formPanel.getForm().isValid()) return false;                        
        // 库位信息没有改变，不需要保存
        if(isLocationChanged()){
           // 检查是否重名
           msg = isLocationRepeat();
           if(msg){
                 Ext.Msg.alert(Constants.SYS_REMIND_MSG, msg);
                return false;
           }
        }   
        return true;
    }
    /**
     * 保存仓库信息
     */
    function saveWarehouseInfo() { 
        // 表单提交
        formPanel.getForm().submit({
            url : 'resource/saveWarehouseAndLocation.action',
            method : Constants.POST,
            params : {
                // 仓库信息是否更改
                isWhsChanged: isWareHouseChanged(),
                // 库位信息是否更改
                isLocationChanged : isLocationChanged(),
                // 新增加的库位记录
                newLocation: Ext.util.JSON.encode(getLocationList(true)),
                // 修改过的库位db记录
                dbLocation : Ext.util.JSON.encode(getLocationList()),
                // 删除的库位id集
                deleteLocationIds : Ext.util.JSON.encode(deleteLocationIds)
            },
            success : function(form ,action){            	
            	var result = eval("(" + action.response.responseText +")");            	
            	
                if(result.success){
                    // 刷新页面
                    refreshWarehouseInfo();
                    Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_004);
            	}else{
            		Ext.Msg.alert(Constants.SYS_REMIND_MSG, result.msg);
            	}
            },
            failure : function(form ,action){
                var result = eval("(" + action.response.responseText +")");
                if(result){
                    Ext.Msg.alert(Constants.SYS_REMIND_MSG, result.msg);
                }else{
                  Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_E_014);
                }
            } 
        });                           
    }
    /**
     * 增加仓库信息
     */
    function addWarehouseInfo() {
    	if(isWareHouseChanged()|| isLocationChanged()){
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
     * 获取所有库位信息，db已存在的和新增加的数据分开保存
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
     * 检测库位是否有重复
     */
    function isLocationRepeat(){
        var msg = '';//Constants.COM_E_007;
        var noErrFlg = 0;
        var nameErrFlg = 0;
        for(var i = 0; i < locationStore.getCount(); i++){
            for(var j = i+1; j < locationStore.getCount(); j++){
                if(locationStore.getAt(i).get('locationNo')==locationStore.getAt(j).get('locationNo')){                    
                    //return msg.replace("{0}","库位号");
                    noErrFlg = 1;
                }
                if(locationStore.getAt(i).get('locationName')==locationStore.getAt(j).get('locationName')){
                    //return msg.replace("{0}","库位名称");
                    nameErrFlg = 1;
                }
            }
            if(noErrFlg == 1 && nameErrFlg == 1){
                break;
            }
        }  
        if(noErrFlg == 1){
            msg +="<br/>" + String.format(Constants.COM_E_007, "库位号");
        }
        if(nameErrFlg == 1){
            msg +="<br/>" + String.format(Constants.COM_E_007, "库位名称");
        }
        return msg;
    }
    /**
     * 拷贝库位信息
     */
    function cloneLocationRecord(record) {
        var objClone = new Object();
        // 拷贝属性
        // 流水号
        objClone['locationId'] = record['locationId'];
        // 库位号
        objClone['locationNo'] = record['locationNo'];
         // 库位名称
        objClone['locationName'] = record['locationName'];
         // 库位描述
        objClone['locationDesc'] = record['locationDesc'];
        // 是否默认库位
        objClone['isDefault'] = record['isDefault'];
        // 上次修改时间
        objClone['lastModifiedDate'] = record['lastModifiedDate'];
        return objClone;         
    }
    /**
     * 按照locationId排序库位信息
     */
    function sortLocationsById(records){
    	var intLen = records.length;
    	var temp = null;
    	for(var i= intLen -1; i > 1; i--){
    		for(var j = 0; j < i; j++){
    			if(records[j].locationId > records[j + 1].locationId) {
    				temp = records[j];
    				records[j] = records[j + 1];
    				records[j + 1] = temp;
    			}
    		}
    	}
    }
    /**
     * 库位主文件关键字段check
     */
    function checkLocationNoAndName() {
       var msg ="";
       var noErrorFlag = 0;
       var nameErrorFlag = 0;
        // 循环
        for(var index  = 0; index < locationStore.getCount(); index ++) {
            // 记录
            var record = locationStore.getAt(index).data;
            if(!record.locationNo) {
                noErrorFlag = 1;
            }
            if(!record.locationName){  
                nameErrorFlag = 1;
            }
            if(noErrorFlag == 1 && nameErrorFlag == 1){
                break;
            }
        }
        if(noErrorFlag == 1){
            msg +="<br/>" + String.format(Constants.COM_E_002, '库位号');    
        }
        if(nameErrorFlag == 1){
            msg +="<br/>" + String.format(Constants.COM_E_002, '库位名称');    
        }
        
        return msg;
    }
    /**
     * 清空登记页面的数据refresh
     */
    function clearRegisterTab() {        
        // 查询tab刷新 
        wareHouseStore.load({params:{start: 0, limit : Constants.PAGE_SIZE}} );
        // form表单清空
        formPanel.getForm().reset();
        // 库位grid清空
        locationStore.removeAll();
        locationStore.totalLength = 0;
        locationGrid.getView().refresh();
        locationGrid.getBottomToolbar().updateInfo();
        // 保存的表单数据删除
        objFormDatas = null;
        // 保存的已删除库位流水号清空
        deleteLocationIds = [];
        // 保存的库位信息清空
        objLocationDatas = [];
        // 仓库编号可编辑
        Ext.get("whsNo").dom.readOnly = false;
        // 删除按钮不可用
        btnDelete.setDisabled(true);
    }
    /**
     * 重新记录仓库信息表单原始值
     */
    function refreshWarehouseInfo() {
        // 保存的已删除库位流水号清空
        deleteLocationIds = [];
        // 保存表单数据
        objFormDatas = formPanel.getForm().getValues();
        objFormDatas['isAllocatableWhs'] = chkIsAllocatableWhs.getValue();
        objFormDatas['isCost'] = chkIsCost.getValue();
        objFormDatas['isInspect'] = chkIsInspect.getValue();
       // 新增的记录保存最成功后，获得其流水号
        Ext.lib.Ajax.request('POST',
                        'resource/getWarehouseInfoByWhsNo.action',
                        {
                            success : function(action) {
                                    var result = eval("(" +action.responseText +")");
                                    if(result){
                                        
                                        objFormDatas['whs.whsId'] = result.whsId;
                                        objFormDatas['whs.lastModifiedDate'] = result.lastModifiedDate;
                                        hdnWhsId.setValue(result.whsId);
                                        hdnLastModifiedDate.setValue(result.lastModifiedDate);
                                    }
                                }
                         },
                         'whsNo=' + txtWhsNo.getValue());
        // 仓库编码不允许改
        Ext.get("whsNo").dom.readOnly = true;
        // 删除按钮可用
        btnDelete.setDisabled(false);
        // 查询tab刷新
        wareHouseStore.load({params:{start: 0, limit : Constants.PAGE_SIZE}} );
        // 载入仓库对应的库位, 参数 仓库号
        locationStore.load({params:{start: 0, limit : Constants.PAGE_SIZE}});
        // 清空修改信息
        locationStore.modified = [];
    }
    /**
     * 获得仓库信息
     * @param whsNo 仓库号
     */
    function getWarehouseInfo(whsNo){
        var record = null;        
        for(var i = 0; i < wareHouseStore.getCount(); i++) {
                record = wareHouseStore.getAt(i);
                if(whsNo == record.get('whsNo')) {
                    return record;
                }
        }
        return null;
    }
    // 库位grid beforeedit 事件
    locationGrid.on('beforeedit', locationBeforeEdit);
    /**
     * 库位grid beforeedit事件处理函数
     */
    function locationBeforeEdit(e){
        // 获取store
        var store = e.grid.getStore();
        // 获取当前记录
        var record = store.getAt(e.row); 
        // 编辑列的字段名
        var fieldName = e.grid.getColumnModel().getDataIndex(e.column);
        // 库位号
        if("locationNo" == fieldName) {
          // db中原有记录的库位号不可编辑
          if(!record.get('isNewRecord')){
            e.cancel = true;
            return;
          }
        }
    }
});

// ↓↓********************grid插件，用来显示一行checkbox***********************
Ext.grid.CheckColumn = function(config){
    Ext.apply(this, config);
    if(!this.id){
        this.id = Ext.id();
    }
    this.renderer = this.renderer.createDelegate(this);
};

Ext.grid.CheckColumn.prototype ={
    init : function(grid){
        this.grid = grid;
        this.grid.on('render', function(){
            var view = this.grid.getView();
            view.mainBody.on('mousedown', this.onMouseDown, this);
        }, this);
    },

    onMouseDown : function(e, t){
        if(t.className && t.className.indexOf('x-grid3-cc-'+this.id) != -1){
            e.stopEvent();
            var index = this.grid.getView().findRowIndex(t);
            var record = this.grid.store.getAt(index);
            if (record.get('isDefault') == 'N') {
				var rec;
                // 判断是不是已经有默认库位
				for (var i = 0; i < this.grid.store.getCount(); i++) {
					rec = this.grid.store.getAt(i);
					if (rec.get('isDefault') == 'Y') {
						return;
					}
				}
            }
            if(record.data[this.dataIndex] == Constants.CHECKED_VALUE){
                record.set(this.dataIndex, Constants.UNCHECKED_VALUE);
            }else{
                record.set(this.dataIndex, Constants.CHECKED_VALUE);    
            }            
        }
    },

    renderer : function(v, p, record){
        p.css += ' x-grid3-check-col-td'; 
        return '<div class="x-grid3-check-col'+(v=='Y'?'-on':'')+' x-grid3-cc-'+this.id+'">&#160;</div>';
    }
};
