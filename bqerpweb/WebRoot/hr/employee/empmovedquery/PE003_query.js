Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.form.Label.prototype.setText = function(argText) {
    this.el.dom.innerHTML = argText;
}
Ext.onReady(function() {
    Ext.QuickTips.init();
    var flag;
    // 系统当天日期
    var dd = new Date();
    var qx = new Date();
    dd = dd.format('Y-m-d');
    qx = qx.format('Y-m-d');

    // 定义查询起始时间
    var startDate = new Ext.form.TextField({
        width : 90,
        name : 'startDate',
        style : 'cursor:pointer',
        readOnly : true,
        listeners : {
            focus : function() {
                WdatePicker({
                    startDate : '%y-%M-%d',
                    dateFmt : 'yyyy-MM-dd',
                    alwaysUseStartDate : false,
                    isShowClear : true
                });
            }
        }
    });
    // 定义查询结束时间
    var endDate = new Ext.form.TextField({
        width : 90,
        id : 'endDate',
        name : 'endDate',
        style : 'cursor:pointer',
        readOnly : true,
        listeners : {
            focus : function() {
                WdatePicker({
                    startDate : '%y-%M-%d',
                    dateFmt : 'yyyy-MM-dd',
                    alwaysUseStartDate : false,
                    isShowClear : true
                });
            }
        }
    });

    // 单据状态
    var cmbStatus = new Ext.form.CmbHRCode({
        id : "cmbStatus",
        type : "单据状态",
        width : 70,
//        value : '0',
        value : '',
        hidden : true,
        name : 'dcmStatus'
    });

    // 调动前部门
    var txtBeforeDept = new Ext.form.TextField({
        fieldLabel : '部门选择',
        width : 110,
        valueField : 'id',
        hiddenName : 'mrDept',
        maxLength : 100,
        anchor : '100%',
        readOnly : true,
        listeners : {
            focus : function() {
                var args = {
                    selectModel : 'single',
                    rootNode : {
                        id : '0',
                        text : '灞桥电厂'
                    },
                    onlyLeaf : false
                };
                this.blur();
                var dept = window.showModalDialog('../../../comm/jsp/hr/dept/dept.jsp', args,
                'dialogWidth:'+ Constants.WIDTH_COM_DEPT+ 'px;dialogHeight:'+ Constants.HEIGHT_COM_DEPT+ 'px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
                if (typeof(dept) != "undefined") {
                    txtBeforeDept.setValue(dept.names);
                    hiddenBeforeDept.setValue(dept.ids);
                    hiddenBeforeDeptIsBanzu.setValue(dept.isBanzus)
                }
            }
        }
    });
    // 隐藏域调动前部门ID
    var hiddenBeforeDept = new Ext.form.Hidden({
        name : 'depCode'
    })
    // 隐藏域调动前部门是否班组
    var hiddenBeforeDeptIsBanzu = new Ext.form.Hidden({
        id : 'hiddenBeforeDeptIsBanzu'
    })

    // 调动后部门
    var txtAfterDept = new Ext.form.TextField({
        fieldLabel : '部门选择',
        width : 110,
        valueField : 'id',
        hiddenName : 'mrDept',
        maxLength : 100,
        anchor : '100%',
        readOnly : true,
        listeners : {
            focus : function() {
                var args = {
                    selectModel : 'single',
                    rootNode : {
                        id : '0',
                        text : '灞桥电厂'
                    },
                    onlyLeaf : false
                };
                this.blur();
                var dept = window.showModalDialog('../../../comm/jsp/hr/dept/dept.jsp', args,
                'dialogWidth:'+ Constants.WIDTH_COM_DEPT+ 'px;dialogHeight:'+ Constants.HEIGHT_COM_DEPT+ 'px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
                if (typeof(dept) != "undefined") {
                    txtAfterDept.setValue(dept.names);
                    hiddenAfterDept.setValue(dept.ids);
                    hiddenAfterDeptIsBanzu.setValue(dept.isBanzus)
                }
            }
        }
    });
    // 隐藏域调动后部门ID
    var hiddenAfterDept = new Ext.form.Hidden({
        name : 'depCode'
    })
    // 隐藏域调动后部门是否班组
    var hiddenAfterDeptIsBanzu = new Ext.form.Hidden({
        id : 'hiddenAfterDeptIsBanzu'
    })

    // 查询按钮
    var btnQuery = new Ext.Button({
        text : Constants.BTN_QUERY,
        iconCls : Constants.CLS_QUERY,
        handler : queryEmpMoveData
    });
    function queryCheck(){
    	if(moveType.getValue() == 0 && moveType.getValue() != ''){
    		var msg = new Array();
    		if(hiddenBeforeDept.getValue() != null && hiddenBeforeDept.getValue() != '')
    		{
				if (hiddenBeforeDeptIsBanzu.getValue() != 1) {
					msg.push('调动前不为班组!')
				}
			}
			if (hiddenAfterDept.getValue() != null
					&& hiddenAfterDept.getValue() != '') {
				if (hiddenAfterDeptIsBanzu.getValue() != 1) {
					msg.push('调动后不为班组!')
				}
			}
			if(msg.length > 0){
				Ext.Msg.alert('提示',msg.join('<br>'))
				return false;
			}
    	}else if(moveType.getValue() == 1){
    		var msg = new Array();
    		if(hiddenBeforeDept.getValue() != null && hiddenBeforeDept.getValue() != '')
    		{
				if (hiddenBeforeDeptIsBanzu.getValue() == 1) {
					msg.push('调动前不为部门!')
				}
			}
			if (hiddenAfterDept.getValue() != null
					&& hiddenAfterDept.getValue() != '') {
				if (hiddenAfterDeptIsBanzu.getValue() == 1) {
					msg.push('调动后不为部门!')
				}
			}
			if(msg.length > 0){
				Ext.Msg.alert('提示',msg.join('<br>'))
				return false;
			}
    	}
    	return true;
    }
    /**
     * 查询按钮按下时
     */
    function queryEmpMoveData() {
    	if(queryCheck())
        	loadData(true);
    }


    // 数据源--------------------------------
    var MyRecord = Ext.data.Record.create([{
            /** 员工姓名 */
            name : 'chsName'
        }, {
            /** 人员ID */
            name : 'empId'
        }, {
            /** 岗位调动单ID */
            name : 'stationRemoveId'
        }, {
            /** 岗位调动类别ID */
            name : 'stationMoveTypeId'
        }, {
            /** 岗位调动类别 */
            name : 'stationMoveType'
        }, {
            /** 调动日期 */
            name : 'removeDate'
        }, {
            /** 起薪日期 */
            name : 'doDate2'
        }, {
            /** 调动通知单号 */
            name : 'requisition'
        }, {
        	name:'bfdeptId'
        }, {
            /** 调动前部门 */
            name : 'bfDeptName'
        }, {
            /** 调动后部门 */
            name : 'afDeptName'
        }, {
            name : 'afDeptId'
        },{
			name:'bfStationId'        	
        },{
            /** 调动前岗位 */
            name : 'bfStationName'
        }, {
            /** 调动后岗位 */
            name : 'afStationName'
        }, {
            name : 'afStationId'
        },{
        	name:'bfStationLevel'
        }
        , {
            /** 调动前岗位级别 */
            name : 'bfStationLevelName'
        }, {
            /** 调动后岗位级别 */
            name : 'afStationLevelName'
        }, {
            name : 'afStationLevel'
        }, {
            /** 执行岗级 */
            name : 'checkStationGrade'
        }, {
            /** 标准岗级 */
            name : 'stationGrade'
        }, {
            /** 薪级 */
            name : 'salaryLevel'
        }, {
            /** 单据状态 */
            name : 'dcmState'
        }, {
            /** 原因 */
            name : 'reason'
        }, {
            /** 备注 */
            name : 'memo'
        }, {
            name : 'lastModifiedDate'
        }]);
    // 定义获取数据源
    var dataProxy = new Ext.data.HttpProxy({
        url : 'hr/getEmpMoveDeclareInfo.action'
    });

    // 定义格式化数据
    var theReader = new Ext.data.JsonReader({
        root : "list",
        totalProperty : "totalCount",
        sortInfo : {
            field : "stationRemoveId",
            direction : "ASC"
        }
    }, MyRecord);
    // 定义封装缓存数据的对象
    var queryStore = new Ext.data.Store({
        // 访问的对象
        proxy : dataProxy,
        // 处理数据的对象
        reader : theReader
    });

    // 定义选择列
    var sm = new Ext.grid.RowSelectionModel({
        singleSelect : true
    });
    
    // 选择类型
    var moveTypeStore = new Ext.data.SimpleStore({
    	data : [['',''],['0','班组间'],['1','部门间'],['2','借调']],
    	fields : ['id','text']
    })
    var moveType = new Ext.form.ComboBox({
    	id : 'moveType',
    	store : moveTypeStore,
    	displayField : 'text',
    	valueField : 'id',
    	mode : 'local',
    	width : 70,
    	triggerAction : 'all',
    	readOnly : true,
    	value : ''
    })
    var queryNo = new Ext.form.TextField({
    	id : 'queryNo'
    })
    var headTbar = new Ext.Toolbar({
        region : 'north',
        border : false,
        height : 25,
        items : ['调动日期:', startDate, '~', endDate, '-','调动类型：',moveType, cmbStatus, "-", '调动前部门:', txtBeforeDept, '调动后部门:',
            txtAfterDept,'-','通知单编号:',queryNo]
    });
    var gridTbar = new Ext.Toolbar({
        border : false,
        height : 25,
        items : [btnQuery]
    });
    var recordGrid = new Ext.grid.GridPanel({
        region : "center",
        store : queryStore,
        columns : [
            // 自动行号
            new Ext.grid.RowNumberer({
                header : "行号",
                width : 31
            }), {
                header : "员工姓名",
                sortable : true,
                dataIndex : 'chsName'
            }, {
                header : '员工ID',
                hidden : true,
                dataIndex : 'empId'

            }, {
                header : "调动日期",
                sortable : true,
                dataIndex : 'removeDate'
            }, {
                header : "起薪日期",
                sortable : true,
                dataIndex : 'doDate2'
            }, {
                header : "调动通知单号",
                sortable : true,
                dataIndex : 'requisition'
            }, {
                header : "调动前部门",
                sortable : true,
                dataIndex : 'bfDeptName'
            }, {

                header : "调动后部门",
                sortable : true,
                dataIndex : 'afDeptName'
            }, {
                header : "调动前岗位",
                sortable : true,
                dataIndex : 'bfStationName'
            }, {
                header : "调动后岗位",
                sortable : true,
                dataIndex : 'afStationName'
            }, {
                header : "调动前岗位级别",
                sortable : true,
                dataIndex : 'bfStationLevelName'
            }, {
                header : "调动后岗位级别",
                sortable : true,
                dataIndex : 'afStationLevelName'
            }, {
                header : "单据状态",
                sortable : true,
                hidden : true,
                dataIndex : 'dcmState',
                renderer : function(value) {
                    if (value == "0")
                        return '未上报';
                    else if (value == "1")
                        return '已上报'
                    else if (value == "2")
                        return '已终结'
                    else if (value == "3")
                        return '已退回'
                }
            }, {
                header : "原因",
                sortable : true,
                dataIndex : 'reason'
            }, {
                header : "备注",
                sortable : true,
                dataIndex : 'memo'
            }],
        sm : sm,
        autoScroll : true,
        enableColumnMove : false,
        autoSizeColumns : true,
        viewConfig : {
            forceFit : false
        },
        // 头部工具栏
        tbar : gridTbar,
        // 分页
        bbar : new Ext.PagingToolbar({
            pageSize : Constants.PAGE_SIZE,
            store : queryStore,
            displayInfo : true,
            displayMsg : Constants.DISPLAY_MSG,
            emptyMsg : Constants.EMPTY_MSG
        })
    });

     queryStore.on('beforeload', function() {
     	this.baseParams = {
	     		startDate : startDate.getValue(),
                endDate : endDate.getValue(),
                beforeDeptCode : hiddenBeforeDept.getValue(),
                afterDeptCode : hiddenAfterDept.getValue(),
                dcmStatus : cmbStatus.getValue(),
                moveType : moveType.getValue(),
                queryNo : queryNo.getValue()
     };
     })
     queryStore.load({
        params : {
            start : 0,
            limit : Constants.PAGE_SIZE,
            flag : 0
        }
    });   

    // 显示区域
    var layout = new Ext.Viewport({
        layout : 'border',
        autoHeight : true,
        items : [headTbar, recordGrid]
    });

    function loadData(flag) {
        queryStore.load({
            params : {
                start : 0,
                limit : Constants.PAGE_SIZE,
                flag : 1
            },
            callback : function() {
                if (flag) {
                    if (queryStore.getCount() <= 0) {
                        Ext.Msg.alert(Constants.REMIND, Constants.COM_I_003);
                    }
                }
            }
        })
    }

})