Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {
    // ↓↓********** 弹出窗口*******↓↓//
    // 组件默认宽度
    var width = 240;
    // 新增或修改
    var flag = '0';
    
    // id隐藏域
    var hiddenId = new Ext.form.Hidden({
        id: "hiddenId",
        name: "hiddenId"
    });
    
    // 编码隐藏域       
    var hiddenNo = new Ext.form.Hidden({
        id: "hiddenNo",
        name: "hiddenNo"
    });
    
    // 名称隐藏域
    var hiddenName = new Ext.form.Hidden({
        id: "hiddenName",
        name: "hiddenName"
    });
    
    // 描述隐藏域
    var hiddenDesc = new Ext.form.Hidden({
        id: "hiddenDesc",
        name: "hiddenDesc"
    });        
    // 编码
    var currencyNoField = new Ext.form.TextField({
        fieldLabel : "编码<font color='red'>*</font>",
        id : "currencyNo",
        name : "currencyNo",
        codeField : "yes",
        style :{
                    'ime-mode' : 'disabled'
                },
        allowBlank : false,
        width : width,
        maxLength : 10
    });

    // 名称
    var currencyNameField = new Ext.form.TextField({
        fieldLabel : "名称<font color='red'>*</font>",
        id : "currencyName",
        allowBlank : false,
        name : "currencyName",
        width : width,
        maxLength : 50
    });

    // 描述
    var currencyDescField = new Ext.form.TextArea({
        fieldLabel : "描述  ",
        id : "currencyDesc",
        name : "currencyDesc",
        width : 237,
        height : 90,
        maxLength : 100
    });
    
    // panel
    var myPaner = new Ext.FormPanel({
        frame : true,
        labelWidth : 80,
        labelAlign : 'right',
        defaultType : "textfield",
        items : [currencyNoField, currencyNameField,currencyDescField]
    });
    
    // 弹出窗口
    var win = new Ext.Window({
        width : 400,
        height : 225,
        modal : true,
        buttonAlign : "center",
        resizable : false,
        items : [myPaner],
        title : '',
        buttons : [{
            // 保存按钮
            text : Constants.BTN_SAVE,
            iconCls : Constants.CLS_SAVE,
            handler : function() {
            // 如果进行的是更新操作且数据无改变
		    Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_C_001,
		         function(buttonObj) {
		              if (buttonObj == "yes") {
		                    	// 判断数据是否为空
            	            if( isNotNull()&& strlen(Ext.get("currencyNo").dom.value.RTrim())< 11 && Ext.get("currencyName").dom.value.RTrim().length< 51 && Ext.get("currencyDesc").dom.value.RTrim().length< 101) {
	            
		                        var myurl = "";
		                        var currencyIdText = "";
		                       // record = moneyGrid.getSelectionModel().getSelected();
		                        var currencyNoText = Ext.get("currencyNo").dom.value;
		                        var currencyNameText = Ext.get("currencyName").dom.value;
		                        var currencyDescText = Ext.get("currencyDesc").dom.value.replace(/(^\s*)/g, "").replace(/(\s*$)/g, "");
		                    
		                        // 更新数据操作
		                        if (flag == '0' ) {
		                            // 更新时取得grid流水号
		                             currencyIdText = hiddenId.getValue();
		                            myurl = "resource/updateCurrency.action";
		                        } else {
		                        // 添加数据操作
		                            myurl = "resource/addCurrency.action";
		                            currencyIdText = "";
		                        }
		                        Ext.Ajax.request({
		                            method : 'POST',
		                            url : myurl,
		                            success : function(result, request) {
		                            	var f = eval('(' + result.responseText + ')');
		                                if(f == '0'){
		                                	// 编码已经存在
		                                	Ext.Msg.alert(Constants.SYS_REMIND_MSG,String.format(Constants.COM_E_007,"编码"));
		                                } else if(f=='1') {
		                                	// 名称已经存在
		                                	Ext.Msg.alert(Constants.SYS_REMIND_MSG,String.format(Constants.COM_E_007,"名称"));
		                                } else if(f=='2') {
		                                	// 保存成功
		                                	win.hide();
		                                	Ext.Msg.alert(Constants.SYS_REMIND_MSG,Constants.COM_I_004);
		                                } 
		                                
		                                searchStore.load({
		                                    params : {
		                                        start : 0,
		                                        limit : Constants.PAGE_SIZE
		                                    }
		                                });
		                                
		                            },
		                            failure : function() {
		                                Ext.Msg.alert(Constants.SYS_REMIND_MSG,
		                                        Constants.COM_E_014);
		                                win.hide();
		                            },
		                            params : {
		                            	// 向后台传递参数
		                                currencyIdValue :currencyIdText,
		                                currencyNoValue : currencyNoText,
		                                currencyNameValue : currencyNameText,
		                                currencyDescValue : currencyDescText
		                            
		                            }
		                       });
		                    }
		              }
		          });
		
	            	
            	}
        }, {
            // 取消按钮
            text : Constants.BTN_CANCEL,
            iconCls : Constants.CLS_CANCEL,
            handler : function() {
                win.hide();
            }
        }],
        layout : 'fit',
        closeAction : 'hide'
    });
    // ↑↑********弹出窗口*********↑↑//
    
    // ↓↓********** 主画面*******↓↓//
    
    // 增加按钮
    var addBtn = new Ext.Button({
        id : 'add',
        text : "新增",
        iconCls : Constants.CLS_ADD,
        handler :addHander
    });
    
    // 修改按钮
    var editBtn = new Ext.Button({
        id : 'edit',
        text : "修改",
        iconCls : Constants.CLS_UPDATE,
        handler : editMsg
    });
    
    // 删除按钮
    var deleteBtn = new Ext.Button({
        id : 'delete',
        text : "删除",
        iconCls : Constants.CLS_DELETE,
        handler : function() {
        }
    });
    
    // grid中的数据
    var moneyGridList = new Ext.data.Record.create([
        {
            name : 'currencyNo'
        }, {
            name : 'currencyName'
        }, {
            name : 'currencyDesc'
        },{
            name : 'currencyId'
        }
    ]);

    // grid的store
    var searchStore = new Ext.data.JsonStore({
        url : 'resource/searchCurrency.action',
        root : 'list',
        totalProperty : 'totalCount',
        fields : moneyGridList
    });
    
    // 排序
    searchStore.setDefaultSort('currencyNo','ASC');
    searchStore.load({
        params : {
            start : 0,
            limit : Constants.PAGE_SIZE
        }
    });
    
    // 分页工具栏
    var pagebar = new Ext.PagingToolbar({
        pageSize : Constants.PAGE_SIZE,
        store : searchStore,
        displayInfo : true,
        displayMsg : Constants.DISPLAY_MSG,
        emptyMsg : Constants.EMPTY_MSG
    });
            
    // 运行执行的Grid主体
    var moneyGrid = new Ext.grid.GridPanel({
        store : searchStore,
        columns : [
                    new Ext.grid.RowNumberer({
                        header : '行号',
                        width : 35
                    }),{
                    header : "编码",
                    width : 60,
                    sortable : true,
                    dataIndex : 'currencyNo'
                }, {
                    header : "名称",
                    width : 60,
                    sortable : true,
                    dataIndex : 'currencyName'
                }, {
                    header : "描述",
                    width : 200,
                    sortable : true,
                    dataIndex : 'currencyDesc',
                    renderer : function (value) {
                        if (value) {
                            return value.replace(/\n/g, '<br/>');
                        }
                        return '';
                    }
                },{
                    header : "流水号",
                    hidden : true,
                    sortable : true,
                    dataIndex : 'currencyId'                
                }],
        viewConfig : {
            forceFit : true
        },
        sm: new Ext.grid.RowSelectionModel({singleSelect:true}),
        tbar : [addBtn,'-', editBtn,'-',deleteBtn],
        bbar : pagebar,
        frame : false,
        border : false,
        enableColumnMove : false,
        autoExpandColumn : 2

    });
    
    // 设定布局器及面板
    var layout = new Ext.Viewport({
        layout : "border",
        border : false,
        items : [{
                    title : "",
                    region : 'center',
                    layout : 'fit',
                    border : false,
                    margins : '0 0 0 0',
                    split : true,
                    collapsible : false,
                    items : [moneyGrid]
                }]
    });
    
    // 双击编辑
    moneyGrid.on("rowdblclick", editMsg);
    
    // ↑↑********** 主画面*******↑↑//
    
    // ↓↓*********处理***********↓↓//
    // 编辑处理
    function editMsg() {
        flag ='0';
        // 判断是否已选择了数据
        if (moneyGrid.selModel.hasSelection()) {
            var record = moneyGrid.getSelectionModel().getSelected();
                win.setTitle("修改币别");
                currencyNoField.readOnly = true;
                win.show();
                // 设置隐藏域的值
                Ext.get("currencyNo").dom.readOnly = true;
                myPaner.getForm().loadRecord(record);
               
                hiddenId.setValue(record.get("currencyId"));
                hiddenNo.setValue(record.get("currencyNo"));
				hiddenName.setValue(record.get("currencyName"));
				hiddenDesc.setValue(record.get("currencyDesc"));
        }else {
            Ext.Msg.alert(Constants.SYS_REMIND_MSG,
                    Constants.COM_I_001);
        }
    }
    
    // 判断数据是否为空
    function isNotNull() {
    	var bFlag = false;
    	var msg = "";
    	// 判断数据是否为空
    	if ( currencyNoField.getValue() == "" ) {
    		bFlag = true;
    		msg += String.format(Constants.COM_E_002,"编码");
    	}
    	if (currencyNameField.getValue() == "") {
    		bFlag = true;
    		msg += "<br/>" + String.format(Constants.COM_E_002, '名称');
    	}
    	if(bFlag) {
    		Ext.Msg.alert(Constants.SYS_REMIND_MSG, msg);
    		return false;
    	}
    	return true;
    }
    

    
    // 增加处理
    function addHander() {
        flag = '1'
        myPaner.getForm().reset();
        win.setTitle("新增币别")
        win.show();
        Ext.get("currencyNo").dom.readOnly = false;
    }
    
    // 删除处理
    deleteBtn.handler = function() {
    	// 判断是否已选择了数据
        if (moneyGrid.selModel.hasSelection()) {
            var record = moneyGrid.getSelectionModel().getSelected();
            var currencyIdValue = record.get("currencyId");
            Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_C_002,
            function(buttonObj) {
                if (buttonObj == "yes") {
                    Ext.lib.Ajax.request('POST',
                            'resource/deleteCurrency.action', {
                                success : function(action) {
                                    Ext.Msg.alert(
                                        Constants.SYS_REMIND_MSG,
                                        Constants.DEL_SUCCESS+"&nbsp&nbsp&nbsp&nbsp")
                                    searchStore.load({
                                        params : {
                                            start : 0,
                                            limit : Constants.PAGE_SIZE
                                        }
                                    });
                                },
                                failure : function() {
                                    Ext.Msg.alert(Constants.ERROR,
                                            Constants.DEL_ERROR+"&nbsp&nbsp&nbsp&nbsp");
                                }
                            }, 'currencyIdValue=' + currencyIdValue);
                       }
                   });
        } else {
            Ext.Msg.alert(Constants.SYS_REMIND_MSG,Constants.COM_I_001);
        }

    }
    // 去掉右空格
	String.prototype.RTrim = function() {
		return this.replace(/(\s*$)/g, "");
	};
	
	 /**
     * 取得str的长度
     */
    function strlen(str)
    {
        var i;  
        var len;            
        len = 0; 
        for (i=0;i<str.length;i++)
        {
            if (str.charCodeAt(i)>255)
                len+=2;
            else
                len++;
        }
        return len;    
    }
});