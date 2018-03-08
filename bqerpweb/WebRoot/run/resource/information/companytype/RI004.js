// 合作伙伴性质维护
// author:chenshoujiang
FLAG_ZERO = "0";
FLAG_ONE = "1";
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
    Ext.QuickTips.init();   

    // ↓↓*******************合作伙伴性质tab**************************************    
    
    // 合作伙伴性质grid的store
    var clientCharacterStore = new Ext.data.JsonStore({
        url : 'resource/getCompanyTypeDesc.action',
        root : 'list',
        totalProperty : 'totalCount',
        // 开始按合作伙伴性质ID排序
        sortInfo :{field: "characterId", direction: "ASC"},
        fields : [
            // 合作伙伴性质ID
            {name:'characterId'},
            // 合作伙伴性质名称
            {name:'characterName'}
               ]
        });  
        
   	// 载入数据
    clientCharacterStore.load({params:{start: 0, limit : Constants.PAGE_SIZE}});    
        
    //设置只能单选
    var sm = new Ext.grid.RowSelectionModel({singleSelect : true})
    // 公司性质的grid
    var clientCharacterGrid = new Ext.grid.GridPanel({
        region : "center",
        layout : 'fit',
        // 标题不可以移动
        enableColumnMove : false,
        store : clientCharacterStore,
        autoSizeColumns : true,
         viewConfig : {
                    // 还会对超出的部分进行缩减，让每一列的尺寸适应GRID的宽度大小，阻止水平滚动条的出现
                    forceFit : true
                },
        columns : [                    
                    // 自动生成行号
                    new Ext.grid.RowNumberer({
                                header : '行号',
                                width:35
                            }),
            // 合作伙伴性质名称
            {   header : "合作伙伴性质名称",
                dataIndex : 'characterName'
            },
            {
	            hidden : true,
	            // 合作伙伴性质ID
	            dataIndex : "characterId"
            }
        ],
        tbar:[ {
                  text : "新增",
                iconCls : Constants.CLS_ADD,
                handler : function() {
                    flag.value = FLAG_ZERO;
                    // 显示子窗口
                    subWin.show();
                    // 设置子窗口标题
                    subWin.setTitle("新增合作伙伴性质");
                    // 显示增加合作伙伴性质信息窗口
                    subPanel.getForm().reset();
                    }
                },'-',{
                text : Constants.BTN_UPDATE,
                iconCls : Constants.CLS_UPDATE,
                handler : updateRecord
                },'-',{
                text : Constants.BTN_DELETE,
                iconCls : Constants.CLS_DELETE,
                  handler : deleteRecord    
                }
            ],
            sm:sm,
        // 分页
        bbar :  new Ext.PagingToolbar({
					pageSize : Constants.PAGE_SIZE,
					store : clientCharacterStore,
					displayInfo : true,
					displayMsg : Constants.DISPLAY_MSG,
					emptyMsg : Constants.EMPTY_MSG
				})
    });
    
    // 双击进入登记tab
    clientCharacterGrid.on("rowdblclick",updateRecord );  
    
    /**
	 * 点击修改按钮时
	 */
    function updateRecord() {
    	// 设置修改标识
        flag.value = FLAG_ONE;
        if (clientCharacterGrid.selModel.hasSelection()) {
		    var records = clientCharacterGrid.selModel.getSelections();
		    var recordslen = records.length;
		    if(recordslen > 1) {
		            Ext.Msg.alert(Constants.SYS_REMIND_MSG,
		        Constants.SELECT_COMPLEX_MSG);
		    } else {
		        var record = clientCharacterGrid.getSelectionModel().getSelected();
		        // 显示主窗口
		        subWin.show();
		        // 设置子窗口标题
		        subWin.setTitle("修改合作伙伴性质");
		        // 加载该行记录
		        subPanel.getForm().loadRecord(record);
		        // 设置公司性质说明信息
		        characterNameTxt.setValue(record.get("characterName"));
		        hiddenCharacterName.value = record.get("characterName");
		        hiddenCharacterId.value = record.get("characterId");
		    }
		}else 
            {
                Ext.Msg.alert(Constants.SYS_REMIND_MSG,
                        Constants.COM_I_001);
            }
    }
    
    /**
	 * 点击删除按钮时
	 */
    function deleteRecord() {
        // 如果选择了数据的话，先弹出提示
        if (clientCharacterGrid.selModel.hasSelection()) {
        var records = clientCharacterGrid.selModel.getSelections();
        var recordslen = records.length;
        if(recordslen > 1)
        {
            // 如果选择记录大于1，弹出提示：”请选择一行“
                Ext.Msg.alert(Constants.SYS_REMIND_MSG,
            Constants.SELECT_COMPLEX_MSG);
        }else
        {
            var record = clientCharacterGrid.getSelectionModel().getSelected();
                Ext.Msg.confirm(Constants.SYS_REMIND_MSG,Constants.COM_C_002,
                    function(buttonobj) {
                        // 如果选择是
                        if (buttonobj == "yes") {
                            Ext.Ajax.request({
                                method : Constants.POST,
                                waitMsg : Constants.DelMsg,
                                url : 'resource/deleteCompanyType.action',
                                params : {
                                    characterId:record.get("characterId")
                                    },
                                success:function(action) {
                                    var result = eval(action.responseText);
                                    // 如果成功，弹出删除成功
                                    if(result == true)
                                    {
                                        Ext.Msg.alert(Constants.SYS_REMIND_MSG,
                                        Constants.COM_I_005)
                                        // 成功,则重新加载grid里的数据
                                    }
                                    // 重新加载数据
                                    clientCharacterStore.load({params:{start: 0, limit : Constants.PAGE_SIZE}});
                                    }
                                });
                            }
                        });
                }
            } else {
                // 如果没有选择数据，弹出错误提示框
                Ext.Msg.alert(Constants.SYS_REMIND_MSG,
                    Constants.COM_I_001);
        }
    }
                
    // 显示区域
    var layout = new Ext.Viewport({
        layout : 'border',
        enableTabScroll : true,
        items : [clientCharacterGrid]        
    });
    
    // 表示是增加还是修改
    var flag = new Ext.form.Hidden({
        id : "flags",
        value : "0"
    });

    // 合作伙伴性质名称隐藏域
    var hiddenCharacterName = new Ext.form.Hidden({
        id : "hiddenCharacterName"
    });
    
    // 公司性质ID隐藏域
    var hiddenCharacterId = new Ext.form.Hidden({
        id : "hiddenCharacterId"
    });
    
    // ============= 定义弹出画面 ===============
    // =============增加合作伙伴性质名称panel=========
    // 合作伙伴性质名称textfield
    var characterNameTxt = new Ext.form.TextField({
        fieldLabel : '合作伙伴性质名称<font color ="red">*</font>',
        id : "characterNameTxt",
        allowBlank : false,
        maxLength:40,
        width : "100%"
    });
   
    // 增加/修改弹出窗口panel
    var subPanel = new Ext.FormPanel({
        labelAlign:"top",
        frame : true,
        height:150,
        items : [characterNameTxt,hiddenCharacterId]
    });
         
    // 增加/修改弹出窗口
    var subWin = new Ext.Window({
        width : 400,
        height:150,
        buttonAlign : "center",
        resizable : false,
        layout : 'fit',
        modal:true,
        items : [subPanel],
        buttons : [{
                text : Constants.BTN_SAVE,
                iconCls : Constants.CLS_SAVE,
                handler : function() {
	                Ext.Msg.confirm(Constants.SYS_REMIND_MSG,Constants.COM_C_001,
	                    function(buttonobj) {
	                        if (buttonobj == "yes") {
	                            if(characterNameTxt.getValue().RTrim() != "")
                    			{
                    				if(check(characterNameTxt.getValue().RTrim()) <= 80)
                    				{
	                                	// 如果要保存，就先进行check
	                                	Ext.lib.Ajax.request('POST',
	                                    	'resource/checkCompanyType.action',
	                                    	{
	                                       	 	success : function(action) {
	                                            	var result = eval(action.responseText);
	                                            	if(flag.value == FLAG_ZERO)
	                                            	{
	                                                	// 如果为0，则增加
	                                                	addCompanyType(result);
	                                            	}
	                                            	else if(flag.value == FLAG_ONE)
	                                            	{
	                                            		// 如果为1，则修改
	                                               	 	modifyCompanyType(result);
	                                            	}
	                                        	}
	                                    	}, "characterName=" + characterNameTxt.getValue().RTrim());
	                                }
                        		}else 
                        		{
                        			// 如果合作伙伴性质名称为空
									Ext.Msg.alert(Constants.SYS_REMIND_MSG, 
										String.format(Constants.COM_E_002,"合作伙伴性质名称"));
                        		}
                                }
                    		});
                    	}
                	},{
                	text : Constants.BTN_CANCEL,
                	iconCls :Constants.CLS_CANCEL,
                	handler : function() {
                    	subWin.hide();
                	}
        	}],
        closeAction : 'hide'
    })
    
    /**
	 * 增加数据
	 */
    function addCompanyType(result) {
        if(result == false)
        {
            // 如果检索小于0，执行增加操作
            Ext.lib.Ajax.request('POST',
            'resource/addCompanyType.action', {
                success : function(action) {
                    var result = eval(action.responseText);
                    if(result)
                    {
                        // 如果增加成功，弹出保存成功信息
                        Ext.Msg.alert(Constants.SYS_REMIND_MSG,Constants.COM_I_004);
                    }
                    // 隐藏弹出窗口
                    subWin.hide();
                    // 重新加载数据
                    clientCharacterStore.load({params:{start: 0, limit : Constants.PAGE_SIZE}}); 
                }
            }, "characterName=" + characterNameTxt.getValue().RTrim());
        }else 
        {
            // 合作伙伴性质名称检索>0，弹出出错对话框                    
            Ext.Msg.alert(Constants.ERROR, String.format(Constants.COM_E_007,"合作伙伴性质名称"))
        }
    }
    
    /**
	 * 修改公司性质信息说明
	 */
    function modifyCompanyType(result)
    {
        
        if(result == true && hiddenCharacterName.value.RTrim() != characterNameTxt.getValue().RTrim())
        {
                // 如果不一致，弹出信息
                Ext.Msg.alert(Constants.SYS_REMIND_MSG,String.format(Constants.COM_E_007,"合作伙伴性质名称"));
        }else 
        {
            // 如果检索小于0，就执行修改操作
            Ext.lib.Ajax.request('POST',
            	'resource/modifyCompanyType.action', {
                success : function(action) {
                    var result = eval(action.responseText);
                    if(result)
                    {
                        // 如果修改成功的话，弹出成功信息
                        Ext.Msg.alert(Constants.SYS_REMIND_MSG,Constants.COM_I_004);
                    }
                    // 隐藏弹出窗口
                    subWin.hide();
                    // 重新加载数据
                    clientCharacterStore.load({params:{start: 0, limit : Constants.PAGE_SIZE}}); 
                }
            }, "characterName=" + characterNameTxt.getValue().RTrim()
                +"&characterId=" + hiddenCharacterId.value)
        }
    }
    
	/**
     * 去掉右空格
     */ 
   	String.prototype.RTrim = function() 
    {
        return this.replace(/(\s*$)/g, ""); 
    };
      
    /**
     *  check字段的长度
     *  return 字符串单字节的长度
     */
  	function check(str) {   
  		var reg=/[^\x00-\xff]/
  		var s = 0;  
  		var ts;   
  		for (i=0;i<str.length;i++) { 
    		ts=str.substring(i,i+1); 
    		if (reg.test(ts))
    		{
    			s = s + 2;
    		}else {
				s = s + 1;    			
  			}
  		}
  		return s;
	}   

})