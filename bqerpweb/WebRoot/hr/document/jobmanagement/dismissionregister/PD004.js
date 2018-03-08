// 画面：档案管理/招聘离职管理/员工离职登记
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {
    // 重复标记
    Constants.DATE_REPEAT = "R";
    // 存档
    ONLYSAVE_0 = "0";
    // 保存
    ONLYSAVE_1 = "1";
    // 新增
    ADDFLAG_1 = "1";
    // 修改
    ADDFLAG_0 = "0";
    var lastModDate = "";
    var empLastModDate = "";
    var onlySaveFlag = "";
    var addFlag = "";
    // 修改时：流水号，新增时：离职人员id
    var empidOrDimissionid = "";
    
    
    
    //add by wpzhu 20100816
    
    
    
    function getDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate();
		s += (t > 9 ? "" : "0") + t;
		return s;
	}
    //
    // 系统当前日期
    var sysDate = new Date();
    sysDate = sysDate.format('Y');
    // 年度选择
    var txtYear = new Ext.form.TextField({
        id : 'txtYear',
        name : 'year',
        style : 'cursor:pointer',
        width : 80,
        value : sysDate,
        readOnly : true
    });
    txtYear.onClick(function() {
                WdatePicker({
                    startDate : '%y',
                    dateFmt : 'yyyy',
                    alwaysUseStartDate : false,
                    isShowClear : true,
                    isShowToday : false,
                    onpicked : function() {
                        txtYear.blur();
                    }
                });
            })
    // 部门选择
    var txtDept = new Ext.form.TextField({
        fieldLabel : '部门',
        width : 100,
        valueField : 'id',
        hiddenName : 'dept',
        maxLength : 100,
        anchor : '100%',
        readOnly : true
    });
    txtDept.onClick( function() {
                var args = {
                    selectModel : 'single',
                    rootNode : {
                        id : '0',
                        text : '合肥电厂'
                    },
                    onlyLeaf : false
                };
                this.blur();
                var dept = window.showModalDialog('../../../../comm/jsp/hr/dept/dept.jsp', args,
                'dialogWidth:' + Constants.WIDTH_COM_DEPT +'px;dialogHeight:'
                + Constants.HEIGHT_COM_DEPT + 'px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
                if (typeof(dept) != "undefined") {
                    txtDept.setValue(dept.names);
                    hiddenMrDept.setValue(dept.ids);
                }
            })
    // 隐藏域部门ID
    var hiddenMrDept = new Ext.form.Hidden({
        hiddenName : 'dept'
    })
    // 离职类别选择
    var drpType = new Ext.form.CmbHRBussiness({
        fieldLabel : '离厂类别',
        type : "离职类别",
        width : 100,
        allowBlank : true,
        id : 'drpType',
        hiddenName : 'type'
    })
    
    // 通知单号 add by ywliu 20100618
    var txtAdvicenoteNo = new Ext.form.TextField({
        id : 'txtAdvicenoteNo',
        name : 'advicenoteNo',
        width : 80
    });

    // 查询按钮
    var queryBtn = new Ext.Button({
        id : 'query',
        text : Constants.BTN_QUERY,
        iconCls : Constants.CLS_QUERY,
        handler : queryRecord
    });
    // 新增按钮
    var addBtn = new Ext.Button({
        id : 'add',
        text : Constants.BTN_ADD,
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
        id : "deleteIt",
        text : Constants.BTN_DELETE,
        iconCls : Constants.CLS_DELETE,
        handler : deleteRecord
    })
    // 打印按钮 add by ywliu 20100618
    var printBtn = new Ext.Button({
        text : '打印附件',
        iconCls : Constants.CLS_PRINT,
        disabled : true,
        handler : printHandler
    });
      var printDetailBtn = new Ext.Button({
        text : '打印离场通知单',
        iconCls : Constants.CLS_PRINT,
        disabled : true,
        handler : printDetailHandler
    });
    
    function printDetailHandler()
    {
    	var empids = new Array();	
    			if (runGrid.selModel.hasSelection()) {
				if(runGrid.selModel.getSelections().length > 1){
			
					var selections = runGrid.getSelectionModel().getSelections();
						for (var i = 1; i < selections.length; i += 1) {
				         var member = selections[i];
				         if(selections[0].get('advicenoteNo')!=member.get('advicenoteNo')){
					       Ext.Msg.alert('提示','通知单号不一致，请修改一致后再打印!');
					       return;
				         }
			      }
//					Ext.Msg.alert('提示','请选择其中一条数据!');
//					return;
				}
				var selections = runGrid.getSelectionModel().getSelections();
				for (var i = 0; i < selections.length; i += 1) {
			    var member = selections[i];
				  if (member.get("empId") != null) {
					 empids.push(member.get("empId"));
					 var newEmpids = ","+empids.join(",")+","
				  }
				}
				var record = runGrid.getSelectionModel().getSelected();
				Ext.Msg.confirm(Constants.CONFIRM, '确认要打印吗？', function(
						buttonobj) {
					// 如果选择是
					if (buttonobj == "yes") {
						
							var url="/power/hr/linZhiOrder.action?advicenoteNo="+record.get('advicenoteNo')+"" +
									"&printDate="+record.get('printDate')+"&newEmpids="+newEmpids+"";
							window.open(url);
		
					
//						var empId = record.get('empId');
//						var advicenoteNo = record.get('advicenoteNo');
//						var printDate = record.get('printDate');
//						var newEmpids = ","+empids.join(",")+",";//add by sychen 20100724
//							window.open("/powerrpt/report/webfile/hr/newEmployeeReport.jsp?empId="+empId+"&advicenoteNo="+advicenoteNo+"&printDate="+printDate+"&newEmpids="+newEmpids);
					}
				});
			} else {
				// 如果没有选择数据，弹出错误提示框
				Ext.Msg.alert(Constants.REMIND, Constants.COM_I_001);
			}
			
    	
    }

    // 导出按钮 add by ywliu 20100618
    var exportBtn = new Ext.Button({
        text : Constants.BTN_EXPORT,
        iconCls : Constants.CLS_EXPORT,
        handler : exportHandler
    });
    var headTbar = new Ext.Toolbar({
        region : 'north',
        border : false,
        height : 25,
        items : ["年度:", txtYear, "-", "部门:", txtDept, "-", "离厂类别:", drpType, "-", "通知单号:", txtAdvicenoteNo, "-", queryBtn, addBtn, updateBtn,
            deleteBtn,printBtn,printDetailBtn,exportBtn]
    });

    // grid中的数据 员工工号,员工姓名,员工类别,原工作部门,原工作岗位,
    // 离职类别,离职日期,离职原因,离职后去向,是否存档,备注,(离职人员ID)
    var runGridList = new Ext.data.Record.create([{
            name : "empCode"
        }, {
            name : "empName"
        }, {
            name : "empTypeName"
        }, {
            name : "oldDepName"
        }, {
            name : "oldStationName"
        }, {
            name : "outTypeName"
        }, {
            name : "disMissionDate"
        }, {
            name : "disMissionReason"
        }, {
            name : "whither"
        }, {
            name : "ifSave"
        }, {
            name : "memo"
        }, {
            name : "outTypeId"
        }, {
            name : "lastModifiedDate"
        }, {
            name : "empLastModifiedDate"
        }, {
            // 流水号
            name : "dimissionid"
        }, {
            // 离职人员ID
            name : "empId"
        }, {
            // 员工工号 add by drdu 20100506
            name : "newEmpCode"
        }, {
            // 通知单号 add by ywliu 20100617
            name : "advicenoteNo"
        }, {
            // 止薪日期 add by ywliu 20100617
            name : "stopsalaryDate"
        }, {
            // 登记日期 add by sychen 20100717
            name : "registerDate"
        },
         {
            // 登记日期 add by wpzhu 20100816
            name : "printDate"
        }]);

    // grid中的store
    var runGridStore = new Ext.data.Store({
        proxy : new Ext.data.HttpProxy({
            url : 'hr/getDismissionInfoList.action'
        }),
        reader : new Ext.data.JsonReader({
            root : 'list',
            totalProperty : 'totalCount'
        }, runGridList)
    });
//    runGridStore.setDefaultSort('disMissionDate', 'ASC');
    runGridStore.baseParams = {
        year : txtYear.getValue(),
        deptId : hiddenMrDept.getValue(),
        typeId : drpType.getValue(),
        advicenoteNo : txtAdvicenoteNo.getValue() // add by ywliu 20100618
    };
    // 初始化时,显示数据
    runGridStore.load({
        params : {
            start : 0,
            limit : Constants.PAGE_SIZE
        }
    });
    // 是否是查询操作
    var queryFlag = false;
    runGridStore.on("load", function() {
        if (runGridStore.getCount() == 0) {
            // 没有检索到任何信息(仅在查询处理后显示)
            if (queryFlag) {
                Ext.Msg.alert(Constants.REMIND, Constants.COM_I_003);
            }
        }
        queryFlag = false;
    });
    // add by ywliu 20100617
    var sm = new Ext.grid.CheckboxSelectionModel();

    // 运行执行的Grid主体 (员工工号,员工姓名,员工类别,原工作部门,原工作岗位,
    // 离职类别,离职日期,离职原因,离职后去向,是否存档,备注)
    var runGrid = new Ext.grid.GridPanel({
        store : runGridStore,
        columns : [sm,
            // 自动生成行号
            new Ext.grid.RowNumberer({
                header : '行号',
                width : 35
            })/*, {
                header : '员工工号',
                width : 100,
                align : 'left',
                sortable : true,
                hidden : true,
                dataIndex : 'empCode'
            }*/, {
                header : '工号',
                width : 80,
                align : 'left',
                sortable : true,
                dataIndex : 'newEmpCode'
            }, {
                header : '姓名',
                width : 80,
                align : 'left',
                sortable : true,
                dataIndex : 'empName'
            }/*, {
                header : '员工类别',
                width : 100,
                align : 'left',
                sortable : true,
                dataIndex : 'empTypeName'
            }*/, {
                header : '职务(岗位)',
                width : 100,
                align : 'left',
                sortable : true,
                dataIndex : 'oldStationName'
            }, {
                header : '工作部门',
                width : 150,
                align : 'left',
                sortable : true,
                dataIndex : 'oldDepName'
            }, {
                header : '离厂日期',
                width : 100,
                align : 'left',
                sortable : true,
                dataIndex : 'disMissionDate'
            }, {
                header : '止薪日期',
                width : 100,
                align : 'left',
                sortable : true,
                dataIndex : 'stopsalaryDate'
            }, {
                header : '备注',
                width : 200,
                align : 'left',
                sortable : true,
                dataIndex : 'memo'
            }, {
                header : '通知单号',
                width : 130,
                align : 'left',
                sortable : true,
                dataIndex : 'advicenoteNo',
                renderer:function(value){
                	if(value==null||value=="")
                	{
                		return "";
                	}
                	else
                	{
                	var strYear=new Date().getYear();
                	return "人离字("+strYear+")第"+value+"号";
                	}
                }
            }, {
                header : '离厂类别',
                width : 100,
                align : 'left',
                sortable : true,
                dataIndex : 'outTypeName'
            }/*, {
                header : '离职原因',
                width : 100,
                align : 'left',
                sortable : true,
                dataIndex : 'disMissionReason'
            }, {
                header : '离职后去向',
                width : 100,
                align : 'left',
                sortable : true,
                dataIndex : 'whither'
            }, {
                header : '是否存档',
                width : 100,
                align : 'left',
                sortable : true,
                dataIndex : 'ifSave',
                renderer : function(value) {
                    if (value == "1") {
                        value = "是";
                    } else if (value == "0") {
                        value = "否";
                    } else
                        value = "";
                    return value;
                }
            }*/, {
                header : '流水号',
                dataIndex : 'dimissionid',
                hidden : true
            }, {
                header : '离职人员ID',
                dataIndex : 'empId',
                hidden : true
            }, {
                header : '离职类别id',
                dataIndex : 'outTypeId',
                hidden : true
            }, {
                header : '人员上次修改日期',
                dataIndex : 'empLastModifiedDate',
                hidden : true
            }, {
                header : '上次修改日期',
                dataIndex : 'lastModifiedDate',
                hidden : true
            }
           //add by sychen 20100717
            , {
                header : '登记时间',
                dataIndex : 'registerDate'
            }],
        viewConfig : {
            forceFit : false
        },
        tbar : headTbar,
        // 分页
        bbar : new Ext.PagingToolbar({
            pageSize : Constants.PAGE_SIZE,
            store : runGridStore,
            displayInfo : true,
            displayMsg : Constants.DISPLAY_MSG,
            emptyMsg : Constants.EMPTY_MSG
        }),
        sm : sm,
        frame : false,
        border : false,
        enableColumnHide : true,
        enableColumnMove : false
    });
    // 注册双击事件
    runGrid.on("rowdblclick", updateRecord);
    
    // 注册单击事件 // add by ywliu 20100618
    runGrid.on("rowclick", function(){
    	printBtn.setDisabled(false);
    	printDetailBtn.setDisabled(false);
    });

    // 设定布局器及面板
    new Ext.Viewport({
        enableTabScroll : true,
        layout : "border",
        items : [{
                xtype : "panel",
                region : 'center',
                layout : 'fit',
                border : false,
                items : [runGrid]
            }]
    });

    // 弹出窗口项目
    var THEWIDTH = 180;
    // 定义form中的 隐藏员工ID
    var hidEmpId = {
        id : "hidEmpId",
        xtype : "hidden"
    }
    var hidDimissionid = {
        id : "hidDimissionid",
        xtype : "hidden"
    }
    // 人员基本信息store
    var storeEmpInfoStore = new Ext.data.JsonStore({
        root : 'list',
        url : "hr/getEmpInfoByEmpId.action",
        fields : ['dimissionid', 'empCode', 'empName', 'empTypeName', 'oldDepName', 'oldStationName',
            'empLastModifiedDate']
    })
    storeEmpInfoStore.on('load', function() {
        if (storeEmpInfoStore.getCount() > 0) {
            textEmpType.setValue(storeEmpInfoStore.getAt(0).get('empTypeName'));
            textOldDept.setValue(storeEmpInfoStore.getAt(0).get('oldDepName'));
            textOldStation.setValue(storeEmpInfoStore.getAt(0).get('oldStationName'));
            empLastModDate = storeEmpInfoStore.getAt(0).get('empLastModifiedDate');
        } else {
            textEmpType.setValue("");
            textOldDept.setValue("");
            textOldStation.setValue("");
            empLastModDate = "";
        }
    })
    // 定义form中的员工姓名
    var textEmpName = new Ext.form.TextField({
        id : "empName",
        name : "empName",
        fieldLabel : "员工姓名<font color='red'>*</font>",
        // maxLength : 13,
        allowBlank : false,
        readOnly : true,
        width : 130,
        listeners : {
            focus : function() {
                var args = {
                    selectModel : 'single',
                    rootNode : {
                        id : '0',
                        text : '合肥电厂'
                    },
                    onlyLeaf : false
                };
                this.blur();
                var person = window.showModalDialog('../../../../comm/jsp/hr/workerByDept/workerByDept.jsp', args,
                'dialogWidth:'+ Constants.WIDTH_COM_EMPLOYEE +'px;dialogHeight:' 
                + Constants.HEIGHT_COM_EMPLOYEE +'px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
                if (typeof(person) != "undefined") {
                    textEmpName.setValue(person.workerName);
                    Ext.get("hidEmpId").dom.value = person.empId;
                    textNewEmpCode.setValue(person.newEmpCode);
                    storeEmpInfoStore.load({
                        params : {
                            dimissionid : person.empId
                        }
                    });
                }
            }
        }
    });
    // 定义form中的 员工工号
//    var textEmpCode = new Ext.form.TextField({
//        id : "empCode",
//        width : 130,
//        disabled : true,
//        fieldLabel : '员工工号'
//    })
    var textNewEmpCode = new Ext.form.TextField({
        id : "newEmpCode",
        width : 130,
        disabled : true,
        fieldLabel : '员工工号'
    })
    // 定义form中的 通知单号 add by ywliu 20100617
    var textAdvicenoteNo = new Ext.form.NumberField({
        id : "advicenoteNo",
        width : 130,
        fieldLabel : '通知单号'
    })
    // 定义form中的 员工类别
    var textEmpType = new Ext.form.TextField({
        //id : "outTypeName",
    	// lichensheng 添加
    	id : "empTypeName",
        width : 130,
        disabled : true,
        fieldLabel : '员工类别'
    })
    // 定义form中的 原工作岗位
    var textOldStation = new Ext.form.TextField({
        id : "oldStationName",
        width : 130,
        disabled : true,
        fieldLabel : '工作岗位'
    })
    // 定义form中的 原工作部门
    var textOldDept = new Ext.form.TextField({
        id : "oldDepName",
        width : 130,
        disabled : true,
        fieldLabel : '工作部门'
    })
    // 定义form中的 离厂类别
    var drpOutType = new Ext.form.CmbHRBussiness({
        fieldLabel : "离厂类别",
        type : "离职类别",
        width : 130,
        allowBlank : true,
        id : 'outTypeId',
        hiddenName : 'typeId'
    })
    // 定义form中的 离厂日期
    var txtOutDate = new Ext.form.TextField({
        fieldLabel : '离厂日期<font color="red">*</font>',// modify by ywliu 2009/09/07
        id : 'disMissionDate',
        name : 'disMissionDate',
        style : 'cursor:pointer',
        width : 130,
        allowBlank : false,
        readOnly : true,
        listeners : {
            focus : function() {
                WdatePicker({
                    startDate : '%y-%M-%d',
                    dateFmt : 'yyyy-MM-dd',
                    alwaysUseStartDate : false,
                    isShowClear : true,
                    onpicked : function() {
//                        txtOutDate.clearInvalid();
                    },
                    onclearing : function() {
                        // txtOutDate.markInvalid();
                    }
                });
            }
        }
    });
    // 定义form中的 止薪日期 add by ywliu 20100617
    var txtStopsalaryDate = new Ext.form.TextField({
        fieldLabel : '止薪日期<font color="red">*</font>',// modify by ywliu 2009/09/07
        id : 'stopsalaryDate',
        name : 'stopsalaryDate',
        style : 'cursor:pointer',
        width : 130,
        allowBlank : false,
        readOnly : true,
        listeners : {
            focus : function() {
                WdatePicker({
                    startDate : '%y-%M-%d',
                    dateFmt : 'yyyy-MM-dd',
                    alwaysUseStartDate : false,
                    isShowClear : true,
                    onpicked : function() {
//                        txtOutDate.clearInvalid();
                    },
                    onclearing : function() {
                        // txtOutDate.markInvalid();
                    }
                });
            }
        }
    });
    // 定义form中的 离职原因
    var txaReason = new Ext.form.TextArea({
        id : "disMissionReason",
        width : 343,
        height : 45,
        maxLength : 100,
        fieldLabel : '离职原因'
    });
    // 定义form中的 离职后去向
    var textWhither = new Ext.form.TextField({
        id : "whither",
        width : 346,
        maxLength : 25,
        fieldLabel : '离职后去向'
    })
    // 定义form中的 备注
    var txaMemo = new Ext.form.TextArea({
        id : "memo",
        width : 343,
        height : 50,
        maxLength : 128,
        fieldLabel : '备注'
    });
    // 员工离职表：上次修改时间
    var hiddenLastModifiedDate = new Ext.form.Hidden({
        id : 'lastModifiedDate'
    })
    // 人员信息表：上次修改时间
    var hiddenEmpLastModifiedDate = new Ext.form.Hidden({
        id : 'empLastModifiedDate'
    })

    // modify by ywliu 20100617
    var fs = new Ext.Panel({
        height : "100%",
        width : 450,
        layout : "form",
        border : false,
        buttonAlign : "center",
        style : "padding-top:20px;padding-right:0px;padding-bottom:0px;margin-bottom:0px",
        items : [{
                border : false,
                layout : "column",
                items : [{
                        columnWidth : 0.04,
                        border : false,
                        layout : "form"
                    }, {
                        columnWidth : 0.48,
                        border : false,
                        layout : "form",
                        items : [hidEmpId, hidDimissionid, textNewEmpCode, textEmpName]
                    }, {
                        columnWidth : 0.48,
                        border : false,
                        layout : "form",
                        items : [textAdvicenoteNo, drpOutType, hiddenLastModifiedDate, hiddenEmpLastModifiedDate]//textEmpType
                    }]
            }, {
                border : false,
                layout : "column",
                items : [{
                        columnWidth : 0.04,
                        border : false,
                        layout : "form"
                    }, {
                        columnWidth : 0.48,
                        border : false,
                        layout : "form",
                        items : [textOldDept]
                    }, {
                        columnWidth : 0.48,
                        border : false,
                        layout : "form",
                        items : [textOldStation]
                    }]
            }, {
                border : false,
                layout : "column",
                items : [{
                        columnWidth : 0.04,
                        border : false,
                        layout : "form"
                    }, {
                        columnWidth : 0.48,
                        border : false,
                        layout : "form",
                        items : [txtOutDate]
                    }, {
                        columnWidth : 0.48,
                        border : false,
                        layout : "form",
                        items : [txtStopsalaryDate]
                    }]
            }, {
                border : false,
                layout : "column",
                items : [{
                        columnWidth : 0.04,
                        border : false,
                        layout : "form"
                    }, {
                        columnWidth : 0.96,
                        border : false,
                        layout : "form",
                        items : [txaMemo]//txaReason, textWhither,
                    }]
            }]
    });
    // 增加或修改面板
    var mypanel = new Ext.form.FormPanel({
        id : "form",
        labelAlign : "right",
        labelWidth : 75,
        frame : true,
        items : [fs]
    });

    // 编辑窗口
    var win = new Ext.Window({
        modal : true,
        width : 500,
        height : 320,
        layout : 'fit',
        closeAction : "hide",
        resizable : false,
        autoScroll : true,
        buttonAlign : 'center',
        items : [mypanel],
        buttons : [{
                text : Constants.BTN_SAVE,
                iconCls : Constants.CLS_SAVE,
                handler : saveOnlyRecord
            }/*, {
                text : Constants.SAVEFILE,
                iconCls : Constants.CLS_SAVE,
                handler : saveRecord
            }*/, {
                text : Constants.BTN_CANCEL,
                iconCls : Constants.CLS_CANCEL,
                handler : function() {
                    Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_005, function(button, text) {
                        if (button == "yes") {
                            win.hide();
                        }
                    });
                }
            }]
    });

    /**
     * 查询处理
     */
    function queryRecord() {
        queryFlag = true;
        runGridStore.baseParams = {
            year : txtYear.getValue(),
            deptId : hiddenMrDept.getValue(),
            typeId : drpType.getValue(),
            advicenoteNo : txtAdvicenoteNo.getValue() // add by ywliu 20100618
           ,flag:'deptFlag'//add by sychen 20100716
        };
        runGridStore.load({
            params : {
                start : 0,
                limit : Constants.PAGE_SIZE
            }
        });
    }
    /**
     * 新增处理
     */
    function addRecord() {
        mypanel.getForm().reset();
        win.setTitle("新增员工离职");
        win.x = undefined;
        win.y = undefined;
        //add by drdu 20100629
        Ext.Ajax.request({
			url : 'hr/getAdvicenoteNoNum.action',
			method : 'post',
			success : function(result, request) {
				var json = result.responseText;
				// 将json字符串转换成对象
				var o = eval("(" + json + ")");
				textAdvicenoteNo.setValue(o.message);
			}
		});
        win.show();
        addFlag = ADDFLAG_1;
        textEmpName.setDisabled(false);
    }

    /**
     * 修改处理 modify by ywliu 20100618
     */
    function updateRecord() {
        // 是否有被选项
        if (runGrid.selModel.hasSelection()) {
        	var records = runGrid.selModel.getSelections();
            var recordslen = records.length;
            if (recordslen > 1) {
                Ext.Msg.alert(Constants.REMIND, Constants.COM_I_001);
            } else {
            	var record = runGrid.getSelectionModel().getSelected();
//            if (record.get('ifSave') == '1') {
//                Ext.Msg.alert(Constants.ERROR, Constants.COM_E_034);
//            } else {
                win.setTitle("修改员工离职");
                win.x = undefined;
                win.y = undefined;
                win.show();
                mypanel.getForm().loadRecord(record);
                //lichensheng 添加
                drpOutType.setValue(record.get('outTypeId'),true);
                Ext.get("hidEmpId").dom.value = record.get('empId');
                Ext.get("hidDimissionid").dom.value = record.get('dimissionid');
                lastModDate = record.get('lastModifiedDate');
                empLastModDate = record.get('empLastModifiedDate');
                textEmpName.setDisabled(true);
                addFlag = ADDFLAG_0;
            }
        } else {
            Ext.Msg.alert(Constants.REMIND, Constants.COM_I_001);
        }
    }
    /**
     * 删除处理
     */
    function deleteRecord() {
        // 是否有被选项
        if (runGrid.selModel.hasSelection()) {
            var record = runGrid.getSelectionModel().getSelected();
//            if (record.get('ifSave') == '1') {
//                Ext.Msg.alert(Constants.ERROR, Constants.COM_E_035);
//            } else {
                Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_002, function(buttonobj) {
                    // 如果选择是
                    if (buttonobj == "yes") {
                        Ext.Ajax.request({
                            method : Constants.POST,
                            url : 'hr/deleteDismissionInfo.action',
                            params : {
                                dimissionid : record.get('dimissionid'),
                                lastModifiedDate : record.get('lastModifiedDate')
                            },
                            success : function(result, request) {
                                if (result.responseText) {
                                    var o = eval("(" + result.responseText + ")");
                                    var suc = o.msg;
                                    // 如果成功，弹出删除成功
                                    if (suc == Constants.SQL_FAILURE) {
                                        Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                                    } else if (suc == Constants.DATA_USING) {
                                        Ext.Msg.alert(Constants.ERROR, Constants.COM_E_015);
                                    } else if (suc == Constants.DEL_SUCCESS) {
                                        Ext.Msg.alert(Constants.REMIND, Constants.COM_I_005);
                                        // 成功,则重新加载grid里的数据
                                        // 重新加载数据
                                        runGridStore.baseParams = {
                                            year : txtYear.getValue(),
                                            deptId : hiddenMrDept.getValue(),
                                            typeId : drpType.getValue(),
                                            advicenoteNo : txtAdvicenoteNo.getValue() // add by ywliu 20100618
                                        };
                                        runGridStore.load({
                                            params : {
                                                start : 0,
                                                limit : Constants.PAGE_SIZE
                                            }
                                        });
                                    } else {
                                        Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                                    }
                                }
                            }
                        });
                    }
                })
//            }
        } else {
            Ext.Msg.alert(Constants.REMIND, Constants.COM_I_001);
        }
    }
    /**
     * 保存处理
     */
    function saveOnlyRecord() {
        onlySaveFlag = ONLYSAVE_0;// modify by ywliu 20100617 ONLYSAVE_1
        save(Constants.COM_C_001, Constants.COM_I_004);
    }
    /**
     * 存档处理
     */
    function saveRecord() {
        onlySaveFlag = ONLYSAVE_0;
        save(Constants.COM_C_010, Constants.COM_I_021);
    }
    function save(msgConfirm, magOk) {
        // 画面check
        if (!check()) {
            return;
        }
        if (addFlag == ADDFLAG_0) {
            // 修改
            empidOrDimissionid = Ext.get("hidDimissionid").dom.value;
        } else if (addFlag == ADDFLAG_1) {
            // 新增
            empidOrDimissionid = Ext.get("hidEmpId").dom.value;
        }
        Ext.Msg.confirm(Constants.CONFIRM, msgConfirm, function(buttonobj) {
            // 如果选择是
            if (buttonobj == "yes") {
                // 画面控件是否红线
                if (!mypanel.getForm().isValid()) {
                    return;
                };
                Ext.Ajax.request({
                    method : Constants.POST,
                    url : 'hr/updateDismissionInfo.action',
                    params : {
                        // 流水号,离职类别id,离职日期,离职原因,离职后去向,备注
                        // dimissionid 修改时：流水号，新增时：离职人员id empidOrDimissionid
                        dimissionid : empidOrDimissionid,
                        outTypeId : drpOutType.getValue(),
                        dimissionDate : txtOutDate.getValue(),
                        dimissionReason : txaReason.getValue(),
                        whither : textWhither.getValue(),
                        memo : txaMemo.getValue(),
                        // add by ywliu 20100617
                        stopsalaryDate : txtStopsalaryDate.getValue(),
                        advicenoteNo : textAdvicenoteNo.getValue(),
                        // 上次修改时间
                        lastModifiedDate : lastModDate,
                        empLastModifiedDate : empLastModDate,
                        onlySave : onlySaveFlag,
                        addOrUpdate : addFlag

                    },
                    success : function(result, request) {
                        if (result.responseText) {
                            var o = eval("(" + result.responseText + ")");
                            var suc = o.msg;
                            // 如果成功
                            if (suc == Constants.SQL_FAILURE) {
                                Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014);
                            } else if (suc == Constants.DATA_USING) {
                                Ext.Msg.alert(Constants.ERROR, Constants.COM_E_015);
                            } else if (suc == Constants.DATE_REPEAT) {
                                Ext.Msg.alert(Constants.ERROR, Constants.PD004_E_001);
                            } else if (suc == "success") {
                                Ext.Msg.alert(Constants.REMIND, magOk, function() {
                                    // 隐藏弹出窗口
                                    win.hide();
                                });
                                // 成功,则重新加载grid里的数据
                                // 重新加载数据
                                runGridStore.baseParams = {
                                    year : txtYear.getValue(),
                                    deptId : hiddenMrDept.getValue(),
                                    typeId : drpType.getValue(),
                                    advicenoteNo : txtAdvicenoteNo.getValue() // add by ywliu 20100618
                                };
                                runGridStore.load({
                                    params : {
                                        start : 0,
                                        limit : Constants.PAGE_SIZE
                                    }
                                });
                            } else {
                                Ext.Msg.alert(Constants.ERROR, Constants.COM_E_014)
                            }
                        }
                    }
                });

            }
        })
    }
    /**
     * 画面check
     */
    function check() {
//        drpOutType.clearInvalid();
//        txtOutDate.clearInvalid();
        // 必须输入项check
        var nullMsg = "";
        // 员工姓名 必须输入项check
        if (textEmpName.getValue() == null || textEmpName.getValue() == "") {
            nullMsg = nullMsg + String.format(Constants.COM_E_003, "员工姓名");
//            textEmpName.markInvalid();
        }
        // 只在存档时check
        if (onlySaveFlag == ONLYSAVE_0) {
            // 离职类别 必须输入项check
            if (drpOutType.getValue() == null || drpOutType.getValue() == "") {
                if (nullMsg != "") {
                    nullMsg = nullMsg + "<br/>";
                }
                nullMsg = nullMsg + String.format(Constants.COM_E_003, "离厂类别");
//                drpOutType.markInvalid();
            }
            // 离职日期 必须输入项check
            if (txtOutDate.getValue() == null || txtOutDate.getValue() == "") {
                if (nullMsg != "") {
                    nullMsg = nullMsg + "<br/>";
                }
                nullMsg = nullMsg + String.format(Constants.COM_E_003, "离厂日期");
//                txtOutDate.markInvalid();
            }
        }
        if (nullMsg != "") {
            Ext.Msg.alert(Constants.ERROR, nullMsg);
            return false;
        }
        return true;
    }
    
    /**
     * 打印处理 add by ywliu 20100618
     */
    function printHandler() {
        // 如果选择了数据的话，先弹出提示
			if (runGrid.selModel.hasSelection()) {
				if(runGrid.selModel.getSelections().length > 1){
					Ext.Msg.alert('提示','请选择其中一条数据!');
					return;
				}
				var record = runGrid.getSelectionModel().getSelected();
				Ext.Msg.confirm(Constants.CONFIRM, '确认要打印吗？', function(
						buttonobj) {
					// 如果选择是
					if (buttonobj == "yes") {
						var empId = record.get('empId');
							window.open("/powerrpt/report/webfile/hr/dimissionNote.jsp?empId="+empId);
					}
				});

			} else {
				// 如果没有选择数据，弹出错误提示框
				Ext.Msg.alert(Constants.REMIND, Constants.COM_I_001);
			}
    }

    /**
     * 导出处理 add by ywliu 20100618
     */
    function exportHandler() {
        Ext.Msg.confirm(Constants.CONFIRM, Constants.COM_C_007, function(buttonobj) {
            if (runGrid.selModel.hasSelection()) {
            	var records = runGrid.selModel.getSelections();
	            var recordslen = records.length;
	            if (recordslen > 1) {
					var date = new Date();
	            	var strDate = date.format('y')+"年"+date.format('m')+"月"+date.format('d')+"日";
	            	var recs = runGrid.getSelectionModel().getSelections();
					var html = ['<table border=1><tr><th colspan=13>大唐陕西发电有限公司灞桥热电厂'+date.getYear()+"年"+'职工离厂通知单</th>'];
					html.push('<tr><th rowspan=2>姓名</th><th rowspan=2 colspan=2>职务（岗位）</th><th rowspan=2 colspan=2>工作部门</th><th colspan=3>离厂日期</th><th colspan=3>止薪日期</th><th rowspan=2 colspan=2>备注</th></tr>');
					html.push('<tr><th>年</th><th>月</th><th>日</th><th>年</th><th>月</th><th>日</th></tr>');
					for(var i = 0; i<recs.length; i++){
						var re = recs[i];
						var dateStr = re.get('disMissionDate')
						var stpslryStr = re.get('stopsalaryDate')
						html.push('<tr><td>'+re.get('empName')+'</td><td colspan=2>'+re.get('oldStationName')+'</td>' +
								'<td colspan=2>'+re.get('oldDepName')+'</td><td>'+dateStr.substring(0,4)+'</td>' +
								'<td>'+dateStr.substring(5,7)+'</td><td>'+dateStr.substring(8)+'</td>' +
										'<td>'+stpslryStr.substring(0,4)+'</td><td>'+stpslryStr.substring(5,7)+'</td>' +
												'<td>'+stpslryStr.substring(8)+'</td><td colspan=2>'+re.get('memo')+'</td></tr>');
					}						
					for(var j = 0; j<3; j++){
						html.push('<tr><td></td><td colspan=2></td><td colspan=2></td><td></td><td></td><td></td><td></td><td></td>' +
											'<td></td><td colspan=2></td></tr>')
					}	
					html.push('<tr border=0><th colspan=3 align="left">厂长:</th><th colspan=5 align="left">人力资源部主任:</th><th colspan=5 align="left">制单:</th></tr>');
					html.push('</table>');
					html = html.join(''); // 最后生成的HTML表格
					tableToExcel(html);
	            } else {
	            	var date = new Date();
	            	var strDate = date.format('y')+"年"+date.format('m')+"月"+date.format('d')+"日";
	            	var recs = runGrid.getSelectionModel().getSelections();
					var html = ['<table border=1><tr><th colspan=13>大唐陕西发电有限公司灞桥热电厂职工离厂通知单</th>'];
					html.push('<tr></tr>');
					html.push('<tr><th colspan=3 align="center">查照</th><th colspan=5 align="center">'+strDate+'</th><th colspan=5 align="center">人离字（'+ date.getYear()+'）第'+recs[0].get('advicenoteNo')+'号</th></tr>');
					html.push('<tr><th rowspan=2>姓名</th><th rowspan=2 colspan=2>职务（岗位）</th><th rowspan=2 colspan=2>工作部门</th><th colspan=3>离厂日期</th><th colspan=3>止薪日期</th><th rowspan=2 colspan=2>备注</th></tr>');
					html.push('<tr><th>年</th><th>月</th><th>日</th><th>年</th><th>月</th><th>日</th></tr>');
					var re = recs[0];
					var dateStr = re.get('disMissionDate')
					var stpslryStr = re.get('stopsalaryDate')
					html.push('<tr><td>'+re.get('empName')+'</td><td colspan=2>'+re.get('oldStationName')+'</td>' +
							'<td colspan=2>'+re.get('oldDepName')+'</td><td>'+dateStr.substring(0,4)+'</td>' +
							'<td>'+dateStr.substring(5,7)+'</td><td>'+dateStr.substring(8)+'</td>' +
									'<td>'+stpslryStr.substring(0,4)+'</td><td>'+stpslryStr.substring(5,7)+'</td>' +
											'<td>'+stpslryStr.substring(8)+'</td><td colspan=2>'+re.get('memo')+'</td></tr>');
					for(var j = 0; j<3; j++){
						html.push('<tr><td></td><td colspan=2></td><td colspan=2></td><td></td><td></td><td></td><td></td><td></td>' +
											'<td></td><td colspan=2></td></tr>')
					}	
					html.push('<tr border=0><th colspan=3 align="left">厂长:</th><th colspan=5 align="left">人力资源部主任:</th><th colspan=5 align="left">制单:</th></tr>');
					html.push('</table>');
					html = html.join(''); // 最后生成的HTML表格
					tableToExcel(html);
	            }	
				}else{
					Ext.Msg.alert('提示','请选择要导出的数据！')
				}
        });
    }
    
    /**
     * 将HTML转化为Excel文档
     */
    function tableToExcel(tableHTML){
		window.clipboardData.setData("Text",tableHTML);
		try{
			var ExApp = new ActiveXObject("Excel.Application");
			var ExWBk = ExApp.workbooks.add();
			var ExWSh = ExWBk.worksheets(1);
			ExApp.visible = true;
		}catch(e){
			if(e.number != -2146827859){
				Ext.Msg.alert('提示','您的电脑没有安装Microsoft Excel软件!')
			}
			return false;
		}
		ExWBk.worksheets(1).Paste;
	}
});