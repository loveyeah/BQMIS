// 画面：定期工作安排
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {
    // ↓↓********** 主画面*******↓↓//
    var strMethod;
    // 分页时每页显示记录条数
    var PAGE_SIZE = Constants.PAGE_SIZE;
    // 删除明细的Id 
    var deletePars = new Array();
    // 删除明细的更新时间
    var deleteTime = new Array();
    // 定期工作维护id
    var strId = "";
    // 增加按钮
    var addBtn = new Ext.Button({
                id : 'add',
                text : "新增",
                iconCls : Constants.CLS_ADD,
                handler : addRecord
            });
            
    // 修改按钮
    var updateBtn = new Ext.Button({
                id : 'update',
                text : Constants.BTN_UPDATE,
                iconCls : Constants.CLS_UPDATE,
                handler : updateRecord
            });
            
    // 删除按钮
    var deleteBtn = new Ext.Button({
                id : 'delete',
                text : Constants.BTN_DELETE,
                iconCls : Constants.CLS_DELETE,
                handler : deleteRecord
            });
    // Grid用toolbar
    var tbar = new Ext.Toolbar([addBtn,updateBtn,deleteBtn]);
    
    //Grid选择模式
    var sm = new Ext.grid.RowSelectionModel({
                            singleSelect : true
                        });
                        
    // grid中的数据
    var infoGridList = new Ext.data.Record.create([{
              /**序号*/
                name : 'id'
            }, {
                /**工作项目名称*/
                name : 'workItemName'
            }, {
                /**工作项目编码*/
                name : 'workItemCode'
            }, {
                /**子类别编码*/
                name : 'subWorkTypeCode'
            }, {
                /**节假日是否工作*/
                name : 'ifWeekEnd'
            }, {
                /**开始时间*/
                name : 'startTime'
            }, {
                /**开始时间*/
                name : 'newStartTime'
            },{
                /**周期类别*/
                name : 'workRangeType'
            },  {
                /**工作说明*/    
                name : 'workExplain'
            }, {
                /**子类别名称*/    
                name : 'subWorkTypeName'
            }, {
                /**修改时间*/        
                name : 'updateTime'
            }]);

    // grid中的store
    var infoGridStore = new Ext.data.Store({
                proxy : new Ext.data.HttpProxy({
                            url : 'administration/findRegularWorkInfo.action'
                        }),
                reader : new Ext.data.JsonReader({
                            root : 'list',
                            totalProperty : 'totalCount'
                        }, infoGridList)
            });
     // 初始化时,显示所有数据
     Ext.Ajax.request({
                url : "administration/findRegularWorkInfo.action",
                method : "post",
                 params : {
                    start : 0,
                    limit : PAGE_SIZE
                },
                success : function(result, request) {
                    var data = eval("(" + result.responseText + ")");
                    infoGridStore.loadData(data);
                    if(data.msg == "SQL"){
                    	  Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
                                                MessageConstants.COM_E_014);
                    } else if(data.msg == "DATA"){
                    	 Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
                                                MessageConstants.COM_E_023);
                    }
                }
            });
                        
    // Grid用PagingToolbar
    var bbar = new Ext.PagingToolbar({
                            pageSize : PAGE_SIZE,
                            store : infoGridStore,
                            displayInfo : true,
                            displayMsg : MessageConstants.DISPLAY_MSG,
                            emptyMsg : MessageConstants.EMPTY_MSG
                        });
    // Grid用columnModel
        var colms = new Ext.grid.ColumnModel([
                        // 自动生成行号
                        new Ext.grid.RowNumberer({
                                    header : '行号',
                                    width : 35
                                }), {
                            header : '工作名称',
                            width : 130,
                            align : 'left',
                            sortable : true,
                            dataIndex : 'workItemName'
                        }, {
                            header : '类别',
                            width : 130,
                            align : 'left',
                            sortable : true,
                            dataIndex : 'subWorkTypeName'
                        }, {
                            header : '计划开始时间',
                            width :150,
                            align : 'left',
                            sortable : true,
                            dataIndex : 'startTime'
                        }]);
    // 运行执行的Grid主体
    var infoGrid = new Ext.grid.GridPanel({
                store : infoGridStore,
                cm : colms,
                tbar : tbar,
                // 分页
                bbar : bbar,
                sm : sm,
                frame : false,
                border : false,
                model : 'local',
                enableColumnHide : true,
                enableColumnMove : false
            });

    // 注册双击事件
    infoGrid.on("rowdblclick", updateRecord);

    // 设定布局器及面板
    new Ext.Viewport({
                enableTabScroll : true,
                layout : "fit",
                items : [infoGrid]
            });

    // ↑↑********** 主画面*******↑↑//

    // ↓↓*****弹出窗口*******↓↓//

    //隐藏id
    var hiddenID = {
        id : 'id',
        xtype : 'hidden',
        readOnly : true,
        hidden : true,
        name : 'entity.id'
    };
    //更新时间
    var hiddenUpdateTime = {
        id : 'updateTime',
        xtype : 'hidden',
        readOnly : true,
        hidden : true,
        name : 'entity.updateTime'
    };
    //隐藏工作项目类别
    var hiddenWorkItemCode = {
        id : 'id',
        xtype : 'hidden',
        readOnly : true,
        hidden : true,
        name : 'entity.workItemCode'
    };
    // 定义form中的工作名称
    var txtWorkName = new Ext.form.TextField({
                id : "workItemName",
                fieldLabel : "工作名称<font color='red'>*</font>",
                isFormField : true,
                allowBlank : false,
                width : 120,
                maxLength : 25,
                anchor : '100%',
                name : 'entity.workItemName'
            });
    // 定义form中的计划开始时间
    var txtWorkStartTime = new Ext.form.TextField({
                id : "workDate",
                fieldLabel : "计划开始时间",
                isFormField : true,
                readOnly : true,
                style : 'cursor:pointer',
                width : 120,
                name : 'entity.newStartTime',
                listeners : {
                    focus : function() {
                        WdatePicker({
                                    dateFmt : 'yyyy-MM-dd HH:mm'
                                });
                    }
                }
            });
    // 第一行
    var firstLine = new Ext.Panel({
        border : false,
        layout : "column",
        style : "padding-top:5px;padding-right:5px;padding-bottom:0px;margin-bottom:0px",
//        anchor : '100%',
        items : [{
                    columnWidth : 0.5,
                    layout : "form",
                    border : false,
                    items : [txtWorkName]
                }, {
                    columnWidth : 0.5,
                    layout : "form",
                    border : false,
                    items : [txtWorkStartTime]
                },{
                    columnWidth : 0.5,
                    layout : "form",
                    border : false,
                    items : [hiddenID]
                }]
    });    
    // 定义form中的类别
    var drpWorkKind = new Ext.form.CmbSubWorkType({
        fieldLabel : "类别<font color='red'>*</font>",
        name : 'entity.subWorkTypeCode',
        allowBlank : false,
        id : 'workKind',
        triggerAction : 'all',
        width : 120
    });
    drpWorkKind.store.load();
    // 定义form中的节假日是否工作
    var drpIfWork = new Ext.form.CmbIsFlg({
        fieldLabel : '节假日是否工作',
        width : 120,
        name : 'entity.ifWeekEnd'
    });
    // 第二行
    var secondLine = new Ext.Panel({
        border : false,
        layout : "column",
        style : "padding-top:5px;padding-right:5px;padding-bottom:0px;margin-bottom:0px",
//        anchor : '100%',
        items : [{
                    columnWidth : 0.5,
                    layout : "form",
                    border : false,
                    items : [drpWorkKind]
                },
                {
                    columnWidth : 0.5,
                    layout : "form",
                    border : false,
                    items : [drpIfWork]
                }]
    });
    // 定义form中的周期类别
    var drpCycleKind = new Ext.form.ComboBox({
                fieldLabel : '周期类别',
                name : 'entity.workRangeType',
                id : 'workRangeType',
                width : 120,
                allowBlank : true,
                forceSelection : true,
                triggerAction : 'all',
                mode : 'local',
                readOnly : true,
                displayField : 'text',
                valueField : 'value',
                store : new Ext.data.JsonStore({
                            fields : ['value', 'text'],
                            data : [{
                                        value : '0',
                                        text : '没有设置'
                                    }, {
                                        value : '1',
                                        text : '每日'
                                    }, {
                                        value : '2',
                                        text : '隔日'
                                    }, {
                                        value : '3',
                                        text : '每周'
                                    }, {
                                        value : '4',
                                        text : '隔周'
                                    }, {
                                        value : '5',
                                        text : '每月'
                                    }, {
                                        value : '6',
                                        text : '隔月'
                                    }, {
                                        value : '7',
                                        text : '隔N天'
                                    }]
                        })});
    drpCycleKind.on('select',selectCycleKind);
    // 第三行
    var thirdLine = new Ext.Panel({
        border : false,
        layout : "column",
        style : "padding-top:5px;padding-right:5px;padding-bottom:0px;margin-bottom:0px",
//        anchor : '100%',
        items : [{
                    columnWidth : 0.5,
                    layout : "form",
                    border : false,
                    items : [drpCycleKind]
                },
                {
                    columnWidth : 0.5,
                    layout : "form",
                    border : false
                }]
    });

    // 定义form中的具体工作内容
    var txtMemo = new Ext.form.TextArea({
                id : "memo",
                fieldLabel : "具体工作内容",
                maxLength : 100,
                height : 66,
                anchor : '99%',
                name : 'entity.workExplain'
            });
    // 第四行
    var fourLine = new Ext.Panel({
        border : false,
        layout : "column",
        style : "padding-top:5px;padding-right:5px;padding-bottom:0px;margin-bottom:0px",
//        anchor : '100%',
        items : [{
                    columnWidth : 1,
                    layout : "form",
                    border : false,
                    items : [txtMemo]
                }]
    });

    // 周期详细Grid用增加明细按钮
    var addParBtn = new Ext.Button({
        id : 'addPar',
        text : '新增明细',
        handler : addPar
    })
    // 周期详细Grid用删除明细按钮
    var deleteParBtn = new Ext.Button({
        id : 'delPar',
        text : '删除明细',
        handler : deletePar
    })
    // 周期详细Grid用toolbar
    var partbar = new Ext.Toolbar([addParBtn,deleteParBtn]);
    //周期详细Grid用周期号
    var cycleNo = new Ext.form.NumberField({
                id : 'cycleNo',
                isFormField : true,
                minValue : 1,
                maxValue : 9999999999,
                maxLength : 10,
                allowNegative : false,
                allowDecimals  :false,
                allowBlank : false,
                anchor : '100%', 
                 style :{
                    'ime-mode' : 'disabled'
                },
                initEvents : function(){
		        	Ext.form.TriggerField.prototype.initEvents.call(this);
		        	var keyPress = function(e){
			            var k = e.getKey();
			            if(!Ext.isIE && (e.isSpecialKey() || k == e.BACKSPACE || k == e.DELETE || k == e.ESC || k == 9 )){
			                return;
			            }
			            var c = e.getCharCode();
			            var allowed = "0123456789";
			            if(allowed.indexOf(String.fromCharCode(c)) === -1){
			                e.stopEvent();
			            }
			        };
			        this.el.on("keypress", keyPress, this);
		        },
                listeners : {
                	'blur':cycleNoCheck
                }
            })
    //周期详细Grid用备注
    var parMemo = new Ext.form.TextField({
                id : 'parMemo',
                isFormField : true,
                maxLength : 50,
                anchor : '100%'
            });
    parMemo.onDblClick(showMemoWin);
    // 周期详细Grid用data
    var cycleData = new Ext.data.Record.create([{
                name : 'id'
            }, {
                name : 'rangeNumber'
            }, {
                name : 'memo'
            }, {
                name : 'workitemCode'
            }, {
                name : 'updateTime'
            }]);
    // 周期详细Grid用columnModel
    var columnls = new Ext.grid.ColumnModel([
        new Ext.grid.RowNumberer({
            header : "行号",
            width : 35
        }),{
        header : "周期号<font color='red'>*</font>",
        width : 120,
        align : 'left',
        sortable : true,
        editor : cycleNo,
        dataIndex : 'rangeNumber'
    },{
        header : "备注",
        width : 180,
        align : 'left',
        sortable : true,
        editor : parMemo,
        dataIndex : 'memo'
    }])
    //周期详细Grid用stroe
    var parStore = new Ext.data.JsonStore({
        url : 'administration/findCycleParInfo.action',
        root : 'list',
        fields : cycleData
    })
    var sm = new Ext.grid.RowSelectionModel({
        singleSelect : true
    });
    sm.onEditorKey = new Function();
    //周期详细Grid
    var parGrid = new Ext.grid.EditorGridPanel({
        fitToPanel : true,
        enableColumnMove : false,
        store : parStore,
        width : 468,
        sm : new Ext.grid.RowSelectionModel({
        singleSelect : true,
        onEditorKey : new Function()
        }),
        cm : columnls,
        clicksToEdit : 1,
        tbar : partbar,
        height : 136,
        autoSizeColumns : true,
        border : false
    })
    var validFlag = true;
    var rowIndex = -1;
    
    parGrid.on('bodyscroll', function() {return false;});
    parGrid.on('cellclick', function(grid, row) {
    	checkCellClick(row);
    	// 设置合法标志符
    	if (!validFlag) {
    		return false;
    	}
    	rowIndex = row;
    	return true;
    });
  
    // 设置合法标志符
    function checkCellClick(row) {
    	if (rowIndex == -1) {
    		validFlag = true;
    		return;
    	}
    	var workRangeType = drpCycleKind.getValue();
    	var cycleNoValue = Ext.getCmp('cycleNo').getValue();
    	if (!parGrid.editing) {
    		cycleNoValue = parStore.getAt(row).data.rangeNumber;
    	}
    	if (!cycleNoValue) {
    		validFlag = parGrid.editing ? false : true;
    		return;
    	}
    	if (workRangeType == "3" || workRangeType == "4") {
			if ( cycleNoValue <= 0 || cycleNoValue > 7) {
    			validFlag = false;
    			return;
			}
    	}
    	if (workRangeType == "5" || workRangeType == "6") {
			if ( cycleNoValue <= 0 || cycleNoValue > 31) {
    			validFlag = false;
    			return;
			}
    	}
    	if(workRangeType == "7"){
		    if(cycleNoValue > 365) {
    			validFlag = false;
    			return;
			}
    	}
    	if (0 == cycleNoValue) {
    		validFlag = false;
			return;
    	}
    	
		var m = 0;
		for (var i = 0; i < parStore.getCount(); i++) {
			if (row === i) {
				m++;
				continue;
			}
			if (cycleNoValue == parStore.getAt(i).data.rangeNumber) {
				m++;
			}
		}
        if (m > 1) {
        	validFlag = false;
            return false;
        }
        
    	validFlag = true;
    }
    
    
    parGrid.on('headerclick', function(grid, row) {
    	// 设置合法标志符
    	checkCellClick(rowIndex);
    	if (!validFlag) {
    		return false;
    	}
    	
    	var workRangeType = drpCycleKind.getValue();
    	var record = parStore.getAt(rowIndex);
    	if (!record) {
    		return;
    	}
    	var cycleNoValue = Ext.getCmp('cycleNo').getValue();
    	if (!parGrid.editing) {
    		return;
    	} else if (rowIndex === 0) {
    		tempCycleNoValue = cycleNoValue;
    	} else {
    		tempCycleNoValue = undefined;
    	}
    	record.set('rangeNumber', cycleNoValue);
//    	if (workRangeType == "3" || workRangeType == "4") {
//			if (record.data.memo == null || record.data.memo == "") {
//				if (cycleNoValue == CodeConstants.CYCLE_TYPE_1) {
//					record.set('memo', "星期一");
//				}
//				if (cycleNoValue == CodeConstants.CYCLE_TYPE_2) {
//					record.set('memo',"星期二");
//				}
//				if (cycleNoValue == CodeConstants.CYCLE_TYPE_3) {
//					record.set('memo',"星期三");
//				}
//				if (cycleNoValue == CodeConstants.CYCLE_TYPE_4) {
//					record.set('memo',"星期四");
//				}
//				if (cycleNoValue == CodeConstants.CYCLE_TYPE_5) {
//					record.set('memo',"星期五");
//				}
//				if (cycleNoValue == CodeConstants.CYCLE_TYPE_6) {
//					record.set('memo', "星期六");
//				}
//				if (cycleNoValue == CodeConstants.CYCLE_TYPE_7) {
//					record.set('memo', "星期日");
//				}
//			}
//		}
    	
    	rowIndex = -1;
    	return true;
    });
    // 定义上面的panel
    var uppanel = new Ext.Panel({
                labelAlign : 'right',
                autoHeight : true,
                border : false,
                frame : true,
                width : 468,
                items : [firstLine,secondLine,thirdLine,fourLine]
            });
    // 定义弹出窗体中的form
    var mypanel = new Ext.FormPanel({
    	        id : "form",
                labelAlign : 'right',
                autoHeight : true,
                frame : true,
                autoScroll :false,
                width : 482,
                items : [uppanel,parGrid]
            });
    // 定义弹出窗体
    var win = new Ext.Window({
                width : 513,
                height : 320,
                title : "",
                buttonAlign : "center",
                items : [mypanel],
                layout : 'form',
                closeAction : 'hide',
                autoScroll :true,
                modal : true,
                resizable : false,
                buttons : [{
                            text : Constants.BTN_SAVE,
                            iconCls : Constants.CLS_SAVE,
                            handler : confirmRecord

                        }, {
                            text : Constants.BTN_CANCEL,
                            iconCls : Constants.CLS_CANCEL,
                            handler : hideChildWin
                        }]
            });
  
    win.on('show', function() {
    	rowIndex = -1;
    });
    // ↑↑********弹出窗口*********↑↑//
	// ↓↓*******************查看备注************************
    // 备注
    var memoText = new Ext.form.TextArea({
         id : "memoText",
         maxLength : 50,
         isFormField : true,
         width : 180,
         hideLabel : true
    });
    // 弹出画面
     var winMemo = new Ext.Window({
        height : 170,
        width : 350,
        layout : 'fit',
        modal:true,
        resizable : false,
        closeAction : 'hide',
        items : [memoText],
        buttonAlign : "center",
        title : '详细信息录入窗口',
        buttons : [{
					text : Constants.BTN_CONFIRM,
				    iconCls : Constants.CLS_OK,
					id : "back",
					handler : function() {
						var record = parGrid.getSelectionModel().getSelected();
						var tempValue = memoText.getValue().replace(/\n/g, '');
						if(tempValue.length>50){
							return;
						}
						record.data.memo = tempValue;
						parGrid.getView().refresh();
						winMemo.hide();
						validFlag = true;
                        rowIndex = -1;
					}
				},{
					text : Constants.BTN_CANCEL,
				    iconCls : Constants.CLS_CANCEL,
					id : "cancel",
					handler : function() {
						winMemo.hide();
						validFlag = true;
                        rowIndex = -1;
					}
				}]
    }); 
    // ↓↓*******************************处理****************************************
    /**
     * 增加函数
     */
    function addRecord() {
        strMethod = "add";
        validFlag = true;
        rowIndex = -1;
        var drpWorkKindCount = drpWorkKind.store.getCount();
        win.show();
             if (drpWorkKindCount <= 0) {
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
					String.format(MessageConstants.COM_I_013, "类别"),function() {
						records = new Array();
						deletePars = new Array();
						deleteTime = new Array();
						parStore.removeAll();
						mypanel.getForm().reset();
						win.hide();
					});
		}
        mypanel.getForm().reset();
        getCurrentDate();
        drpIfWork.setValue('N');
        drpCycleKind.setValue('0');
        win.setTitle("新增定期工作安排");
        addParBtn.disable();
        deleteParBtn.disable();
        parStore.removeAll();
    }
    /**
     * 增加明细按钮压下的操作
     */
    function addPar() {
        var newRecord = new cycleData({
                    rangeNumber : "",
                    memo : "",
                    isNewRecord : true
                });
        // 原数据个数
        var count = parStore.getCount();
        validFlag = true;
        rowIndex = -1;
        var workRangeType = drpCycleKind.getValue();
        // 插入新数据
        //[周期类别]为每周或隔周,总行数不能大于7
        if(workRangeType == "3" || workRangeType == "4"){
            if(count >= 7){
                Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, String.format(
                                MessageConstants.AW003_E_003));
                                return false;
            }
        }
        //[周期类别]为每月或隔月,总行数不能大于31
        if(workRangeType == "5" || workRangeType == "6"){
            if(count >=31){
                Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, String.format(
                                MessageConstants.AW003_E_003));
                                return false;
            }
        }
        //[周期类别]为隔n天,总行数不能大于1
        if(workRangeType == "7"){
            if(count >=1){
                Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, String.format(
                                MessageConstants.AW003_E_003));
                                return false;
            }
        }
        parStore.insert(count, newRecord);
        parGrid.getView().refresh();
    }
    /**
     * 删除明细按钮压下的操作
     */
    
    function deletePar() {
    	validFlag = true;
    	rowIndex = -1;
		if (parGrid.selModel.hasSelection()) {
			Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG,
					MessageConstants.COM_C_002, function(buttonobj) {
						if (buttonobj == 'yes') {
							var rec = parGrid.selModel.getSelected();
							if (parStore.indexOf(rec) <= parStore.getCount()) {
								// 如果选中一行则删除
								parStore.remove(rec);
								parGrid.getView().refresh();
								// 如果不是新增加的记录,保存删除的序号
								if (!rec.get('isNewRecord')) {
									deletePars.push(rec.get('id'));
									deleteTime.push(rec.get('updateTime'));
								}
							}
						}
					})
		} else {
			// 否则弹出提示信息
			Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
					MessageConstants.COM_I_001);
		}
	}
	/**
    * 显示备注窗口
    */
   function showMemoWin(){
   	    var record = parGrid.getSelectionModel().getSelected();
   	    memoText.setValue(parMemo.getValue());
        winMemo.show(record);
                
    }

    /**
     * 周期类别改变的情况
     */
    
    function selectCycleKind() {
    	validFlag = true;
    	rowIndex = -1;
            if (parStore.getCount() > 0) {
                for (var i = 0; i < parStore.getCount(); i++) {
                    deletePars.push(parStore.getAt(i).get('id'));
                    deleteTime.push(parStore.getAt(i).get('updateTime'));
                }
            }
        parStore.removeAll();
        parGrid.getView().refresh();
        var workRangeType = drpCycleKind.getValue();
        // 更具工作周期类别判断增加、删除明细按钮是否可用
        if (workRangeType == "0" || workRangeType == "1"
                || workRangeType == "2") {
            addParBtn.disable();
            deleteParBtn.disable();
        } else {
            addParBtn.enable();
            deleteParBtn.enable();
        }
        
    }
    // 焦点离开周期号check
    function cycleNoCheck(){
        var record = parStore.getAt(rowIndex);
        var intm = parStore.indexOf(record);
        var workRangeType = drpCycleKind.getValue();
        var cycleNoValue = this.getValue();
		
        if (typeof tempCycleNoValue != 'undefined' || !record) {
        	return;
        }
        var tempScope = this;
        function cycleNoFocus(){
            tempScope.focus.call(tempScope,10);
        }
        
        if (!cycleNoValue) {
			Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, String.format(
							MessageConstants.COM_E_019, "周期号"),
					cycleNoFocus);
			validFlag = false;
			return false;
		}
       
        if (workRangeType == "3" || workRangeType == "4") {
        	// add by ywliu 20091027
        	if (cycleNoValue == CodeConstants.CYCLE_TYPE_1) {
				record.set('memo', "星期一");
			}
			if (cycleNoValue == CodeConstants.CYCLE_TYPE_2) {
				record.set('memo',"星期二");
			}
			if (cycleNoValue == CodeConstants.CYCLE_TYPE_3) {
				record.set('memo',"星期三");
			}
			if (cycleNoValue == CodeConstants.CYCLE_TYPE_4) {
				record.set('memo',"星期四");
			}
			if (cycleNoValue == CodeConstants.CYCLE_TYPE_5) {
				record.set('memo',"星期五");
			}
			if (cycleNoValue == CodeConstants.CYCLE_TYPE_6) {
				record.set('memo', "星期六");
			}
			if (cycleNoValue == CodeConstants.CYCLE_TYPE_7) {
				record.set('memo', "星期日");
			}
			// add by ywliu 20091027 End	
			if ( cycleNoValue <= 0 || cycleNoValue > 7) {
				Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, String.format(
								MessageConstants.AW003_E_001, Ext
										.get('workRangeType').dom.value),
						cycleNoFocus);
				validFlag = false;
				return false;
			}
		} else if (workRangeType == "5" || workRangeType == "6") {
			if ( cycleNoValue <= 0 || cycleNoValue > 31) {
				Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, String.format(
								MessageConstants.AW003_E_001, Ext
										.get('workRangeType').dom.value),
						cycleNoFocus);
				validFlag = false;
				return false;
			}
		}else if(workRangeType == "7"){
			  if(cycleNoValue > 365){
			  	Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, String.format(
								MessageConstants.AW003_E_001, Ext
										.get('workRangeType').dom.value),
						cycleNoFocus);
				validFlag = false;
				return false;
			  }
              if (0 == cycleNoValue) {
				Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, String.format(
								MessageConstants.AW003_E_001, Ext
										.get('workRangeType').dom.value),
						cycleNoFocus);
				validFlag = false;
				return false;
			}
        }
        var count = parStore.getCount();
		
    	var m = 0;
		for (var i = 0; i < count; i++) {
			if (rowIndex === i) {
				m++;
				continue;
			}
			if (cycleNoValue == parStore.getAt(i).data.rangeNumber) {
				m++;
			}
		}
        if (m > 1) {
			Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, String.format(
							MessageConstants.AW003_E_002, Ext
									.get('workRangeType').dom.value),
					cycleNoFocus);
			validFlag = false;
			return false;
		} else {
            	validFlag = true;
            }
        if (workRangeType == "3" || workRangeType == "4") {
			if (record.data.memo == null || record.data.memo == "") {
				if (cycleNoValue == CodeConstants.CYCLE_TYPE_1) {
					record.set('memo', "星期一");
				}
				if (cycleNoValue == CodeConstants.CYCLE_TYPE_2) {
					record.set('memo',"星期二");
				}
				if (cycleNoValue == CodeConstants.CYCLE_TYPE_3) {
					record.set('memo',"星期三");
				}
				if (cycleNoValue == CodeConstants.CYCLE_TYPE_4) {
					record.set('memo',"星期四");
				}
				if (cycleNoValue == CodeConstants.CYCLE_TYPE_5) {
					record.set('memo',"星期五");
				}
				if (cycleNoValue == CodeConstants.CYCLE_TYPE_6) {
					record.set('memo', "星期六");
				}
				if (cycleNoValue == CodeConstants.CYCLE_TYPE_7) {
					record.set('memo', "星期日");
				}
			}
		}
    }
    var records = new Array();
    function getWorkArrangeDetailList() {
        for(var i = 0;i< parStore.getCount();i++){
            records.push(Ext.util.JSON.encode(parStore.getAt(i).data));    
        }
        return records;
    }
    /**
     * 保存button压下的操作
     */
    function confirmRecord() {
    	 if (checkform()) {
        Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG, MessageConstants.COM_C_001,
                function(buttonobj) {
                    if (buttonobj == 'yes') {
                    	if(!txtMemo.isValid()){
                    		return;
                    	}
                        var myurl = "";
                        if (strMethod == "add") {
                           
                                myurl = 'administration/saveWorkParrange.action';
                                mypanel.getForm().submit({
                                    method : Constants.POST,
                                    url : myurl,
                                    params : {
                                        records : getWorkArrangeDetailList(),
                                        deletePars : deletePars,
                                        deleteTime : deleteTime,
                                        ifWeekEnd : drpIfWork.getValue(),
                                        subWorktypeCode : drpWorkKind.getValue(),
                                        workRangeType : drpCycleKind.getValue(),
                                        strStartTime : txtWorkStartTime.getValue()
                                    },
                                    success : function(form, action) {
                                        var result = eval('('
                                                + action.response.responseText
                                                + ')');
                                        // 显示成功信息
                                        if (result.msg == "1") {
											Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
															MessageConstants.COM_I_004,
															function() {
																infoGridStore
																		.load({
																			params : {
																				start : 0,
																				limit : PAGE_SIZE
																			}
																		});
																infoGrid.getView().refresh();
																records = new Array();
																deletePars = new Array();
																deleteTime = new Array();
																parStore.removeAll();
																mypanel.getForm().reset();
																win.hide();
															});
										} else if (result.msg == Constants.SQL_FAILURE) {
											Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
													MessageConstants.COM_E_014);
										} else if (result.msg == Constants.DATA_FAILURE) {
											Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
													MessageConstants.COM_E_023);
										}
										}
                                });
                                return;
                        }
                        if (strMethod == "update") {
                                myurl = 'administration/saveWorkParrange.action';
                                mypanel.getForm().submit({
                                    method : Constants.POST,
                                    url : myurl,
                                    params : {
										records : getWorkArrangeDetailList(),
										deletePars : deletePars,
										deleteTime : deleteTime,
										ifWeekEnd : drpIfWork.getValue(),
										subWorktypeCode : drpWorkKind
												.getValue(),
										workRangeType : drpCycleKind.getValue(),
										strId : hiddenID.value,
										strWorkItemCode : hiddenWorkItemCode.value,
										strUpdateTime : hiddenUpdateTime.value,
										strStartTime : txtWorkStartTime
												.getValue()
									},
                                    success : function(form, action) {
                                        var result = eval('('
                                                + action.response.responseText
                                                + ')');
                                        // 保存成功
                                        if (result.msg == '1') {
											Ext.Msg.alert(
													MessageConstants.SYS_REMIND_MSG,
													MessageConstants.COM_I_004,
													function() {
														// parGrid重新装在
														infoGridStore.load({
															params : {
																start : 0,
																limit : PAGE_SIZE
															}
														});
														infoGrid.getView()
																.refresh();
														records = new Array();
														deletePars = new Array();
														deleteTime = new Array();
														parStore.removeAll();
														mypanel.getForm().reset();
														win.hide();
													});
										} else if (result.msg == Constants.DATA_USING) {
											Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
													MessageConstants.COM_I_002);
										}else if (result.msg == Constants.SQL_FAILURE) {
											Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
													MessageConstants.COM_E_014);
										} else if(result.msg == Constants.DATA_FAILURE){
										Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
													MessageConstants.COM_E_023);
										}
                                    }
                                });
                        } 
                    }
                });}
    }

    /**
	 * 
	 * 在增加数据前的非空检查
	 */
    function checkform() {
    	// 工作名称必须入力check
        var workName = txtWorkName.getValue();
        if (workName == "" || workName == null) {
			Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, String.format(
							MessageConstants.COM_E_002, '工作名称'));
							return false;
		}
		// 工作类别必须选额check
        var workKind = drpWorkKind.getValue();
        if (null == workKind || workKind == "") {
			Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, String.format(
							MessageConstants.COM_E_002, '类别'));
							return false;
		}
		// 周期号必须入力check
		var count = parStore.getCount();
			for (var i = 0; i < count; i++) {
			if (parStore.getAt(i).data.rangeNumber == null
					|| parStore.getAt(i).data.rangeNumber == "") {
				Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, String.format(
								MessageConstants.COM_E_002, '周期号'));
				return false;
			}
		}
        var parStoreTotal = parStore.getCount();
        var workRangeType = drpCycleKind.getValue();
        if (workRangeType == "3" || workRangeType == "4"
                || workRangeType == "5" || workRangeType == "6"
                || workRangeType == "7") {
            if (parStoreTotal < 1) {
                Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG, String.format(
                                MessageConstants.COM_E_002, '周期号'));
                                return false;
            }
        }
        return true;
    }
    /**
     * 修改函数
     */
    function updateRecord() {
        var rec = infoGrid.getSelectionModel().getSelected();
        if (!rec) {
            Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG, MessageConstants.COM_I_001);
            return false;
        } else {
			strMethod = "update";
			win.setTitle("修改定期工作安排");
			parStore.removeAll();
			var drpWorkKindCount = drpWorkKind.store.getCount();
			win.show();
			if (drpWorkKindCount <= 0) {
				Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,String.format(
											MessageConstants.COM_I_013, "类别"),function() {
							records = new Array();
							deletePars = new Array();
							deleteTime = new Array();
							parStore.removeAll();
							mypanel.getForm().reset();
							win.hide();
						});

			}
			mypanel.getForm().reset();
			strId = rec.data.id;
			hiddenWorkItemCode.value = rec.data.workItemCode;
			hiddenID.value = rec.data.id;
			hiddenUpdateTime.value = rec.data.updateTime;
			txtWorkName.setValue(rec.data.workItemName);
			txtWorkStartTime.setValue(rec.data.startTime);
			var intDrpCount = drpWorkKind.store.getCount();
			for (i = 0; i < intDrpCount; i++) {
				var workCode = drpWorkKind.store.getAt(i).data.subWorktypeCode;
				if (workCode == rec.data.subWorkTypeCode) {
					drpWorkKind.setValue(rec.data.subWorkTypeCode);
				}
			}
			drpIfWork.setValue(rec.data.ifWeekEnd);
			drpCycleKind.setValue(rec.data.workRangeType);
			txtMemo.setValue(rec.data.workExplain);
			var workRangeType = drpCycleKind.getValue();
			// 更具工作周期类别判断增加、删除、取消明细按钮是否可用
			if (workRangeType == "0" || workRangeType == "1"
					|| workRangeType == "2") {
				addParBtn.disable();
				deleteParBtn.disable();
			} else {
				addParBtn.enable();
				deleteParBtn.enable();
			}
			// 取得工作明细数据
			parStore.baseParams = {
				strWorkItemCode : rec.data.workItemCode
			};
			parStore.load({
						params : {
							start : 0,
							limit : 4
						}
					})
		}
    }
    /**
	 * 删除函数
	 */
    function deleteRecord() {
        var rec = infoGrid.getSelectionModel().getSelected();
        if (!rec) {
            Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG, MessageConstants.COM_I_001);
        }else {
            var id = rec.get('id');
            var updateTime = rec.get('updateTime');
            Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG, MessageConstants.COM_C_002,
                    function(buttonobj) {
                        if (buttonobj == 'yes') {
                            // 刪除
                            Ext.Ajax.request({
                                method : Constants.POST,
                                url : 'administration/delRegularWork.action',
                                params : {
                                    strRec : Ext.util.JSON.encode(rec.data)
                                },
                                success : function(result, request) {
                                    var o = eval("(" + result.responseText
                                            + ")");
                                    // 排他
                                    if (o.msg == Constants.DATA_USING) {
                                        Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
                                                MessageConstants.COM_I_002);
                                    } else if(o.msg == '1'){
                                        Ext.Msg.alert(MessageConstants.SYS_REMIND_MSG,
                                                MessageConstants.COM_I_005,
												function() {
													infoGridStore.load({
																params : {
																	start : 0,
																	limit : PAGE_SIZE
																}
															});
													infoGrid.getView()
															.refresh();
												});
                                        
                                    } else if (o.msg == Constants.SQL_FAILURE){
                                        Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
                                            MessageConstants.COM_E_014);
                                    }else if (o.msg == Constants.DATE_FAILURE){
                                        Ext.Msg.alert(MessageConstants.SYS_ERROR_MSG,
                                            MessageConstants.COM_E_023);
                                    }
                                }
                            });
                        }
                    });
        } 
    }
    /**
     * 点击取消按钮
     */
    function hideChildWin() {
        Ext.Msg.confirm(MessageConstants.SYS_CONFIRM_MSG, MessageConstants.COM_C_005, function(
                        buttonobj) {
                    if (buttonobj == 'yes') {
                        records = new Array();
                        deletePars = new Array();
                        deleteTime = new Array();
                        parStore.removeAll();
                        mypanel.getForm().reset();
                        win.hide();
                    }
                })

    }
    /**
     * 获取当前日期并赋值给日期
     */
    function getCurrentDate() {
        var curr_time = new Date();
        txtWorkStartTime.setValue(curr_time.format("Y-m-d H:i"));
    }

});