Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function(){
	
	// 自定义函数
    var newEmployee = parent.Ext.getCmp('tabPanel').newEmployee;
    var PD002_MSG = parent.Ext.getCmp('tabPanel').PD002_MSG;
    // 设置刷新查询Tab页的监听器
    newEmployee.refreshTabQueryHandler = refreshData;
	
    // 当前时间
	var date = new Date();
	date = date.format('Y');
	
	// 时间选择
    var dateTxt = new Ext.form.TextField({
        id : 'date',
        style : 'cursor:pointer',
        readOnly : true,
        width : 100,
        value : date
    });
    
    dateTxt.onClick(function() {
        WdatePicker({
            startDate : '%y',
            dateFmt : 'yyyy',
            isShowToday : false
        });
    });
    
    // 所属部门
    var deptTxt = new Ext.form.TextField({
    	id : 'deptTxt',
    	width : 100,
    	allowBlank : true,
    	readOnly : true
    });
    
    // 所属部门ID
    var hiddenDeptTxt = {
		id : 'deptId',
		xtype : 'hidden',
		value : '',
		readOnly : true,
		hidden : true
	};
    
    // 选择部门
    deptTxt.onClick(deptSelect);
    
    // 是否存档
	var isSaveCbo = new Ext.form.CmbHRCode({
		id : 'isSave',
		width : 50,
		allowBlank : true,
		type : '存档'
	});
    
	// 查询按钮
    var queryBtn = new Ext.Button({
    	text : Constants.BTN_QUERY,
    	iconCls : Constants.CLS_QUERY,
    	handler : queryHandler
    });
    
    // 新增按钮
    var addBtn = new Ext.Button({
    	text : Constants.BTN_ADD,
    	iconCls : Constants.CLS_ADD,
    	handler : addHandler
    });
    
    // 修改按钮
    var updateBtn = new Ext.Button({
    	text : Constants.BTN_UPDATE,
    	iconCls : Constants.CLS_UPDATE,
    	handler : updateHandler
    });
    
    // 删除按钮
    var deleteBtn = new Ext.Button({
    	text : Constants.BTN_DELETE,
    	iconCls : Constants.CLS_DELETE,
    	handler : deleteHandler
    });
    
	// 定义grid中的数据
    var gridData = new Ext.data.Record.create([
    // 人员基本信息表
    {
    	// 员工ID
    	name : 'empId'
    }, {
    	// 员工编码
    	name : 'empCode'
    }, {
    	// 出生日期
    	name : 'birthday'
    }, {
    	// 性别
    	name : 'sex'
    }, {
    	// 中文名
    	name : 'chsName'
    }, {
    	// 婚否状况
    	name : 'isWedded'
    }, {
    	// 英文名
    	name : 'enName'
    }, {
    	// 身份证
    	name : 'identityCard'
    }, {
    	// 入职时间
    	name : 'missionDate'
    }, {
    	// 试用期开始日期
    	name : 'tryoutStartDate'
    }, {
    	// 试用期终止日期
    	name : 'tryoutEndDate'
    }, {
    	// 员工状态
    	name : 'empState'
    }, {
    	// 毕业时间
    	name : 'graduateDate'
    }, {
    	// 是否退转军人
    	name : 'isVeteran'
    }, {
    	// 员工描述
    	name : 'memo'
    }, {
    	// 上次修改时间
    	name : 'lastModifiyDate'
    },
    // 籍贯编码表
    {
    	// 籍贯编码ID
    	name : 'nativePlaceId'
    },
    // 民族编码表
    {
    	// 民族ID
    	name : 'nationCodeId'
    },
    // 政治面貌表
    {
    	// 政治面貌ID
    	name : 'politicsId'
    },
    // 部门设置表
    {
    	// 部门名称
    	name : 'deptName'
    }, {
    	// 部门ID
    	name : 'deptId'
    },
    // 岗位设置表
    {
    	// 岗位名称
    	name : 'stationName'
    }, {
    	// 岗位ID
    	name : 'stationId'
    },
    // 员工类别设置
    {
    	// 员工类别ID
    	name : 'empTypeId'
    }, {
    	// 员工类别名称
    	name : 'empTypeName'
    },
    // 进厂类别维护
    {
    	// 进厂类别ID
    	name : 'inTypeId'
    }, {
    	// 进厂类别
    	name : 'inType'
    },
    // 学历编码
    {
    	// 学历ID
    	name : 'educationId'
    },
    // 学习专业编码
    {
    	// 学习专业编码ID
    	name : 'specialtyCodeId'
    },
    // 学位编码
    {
    	// 学位ID
    	name : 'degreeId'
    },
    // 学校编码
    {
    	// 学校编码ID
    	name : 'schoolCodeId'
    }
    ,{// 新工号
    	name : 'newEmpCode'
    }
    ]);
    
    var gridStore = new Ext.data.JsonStore({
        url : 'hr/getNewEmployeeQuery.action',
        root : 'list',
        totalProperty : 'totalCount',
        sortInfo : {field: "empCode", direction: "ASC"},
        fields : gridData
  	});
  	
  	// 一览grid
  	var grid = new Ext.grid.GridPanel({
  		autoWidth : true,
  		store : gridStore,
  		region: 'center',
  		sm : new Ext.grid.RowSelectionModel({
  			// 单选
  			singleSelect : true
  		}),
  		columns : [
  			new Ext.grid.RowNumberer({
  				header : '行号',
  				width : 35
  			}), {
  				header : '员工工号',
  				width : 80,
                sortable : true,
                hidden : true,
                dataIndex : 'empCode'
  			}, {
  				header : '员工工号',
  				width : 80,
                sortable : true,
                dataIndex : 'newEmpCode'
  			}, {
  				header : '员工姓名',
  				width : 100,
                sortable : true,
                dataIndex : 'chsName'
  			}, {
  				header : '所属部门',
  				width : 100,
                sortable : true,
                dataIndex : 'deptName'
  			}, {
  				header : '工作岗位',
  				width : 100,
                sortable : true,
                dataIndex : 'stationName'
  			}/*, {
  				header : '身份证号',
  				width : 100,
                sortable : true,
                dataIndex : 'identityCard'
  			}, {
  				header : '员工类别',
  				width : 80,
                sortable : true,
                dataIndex : 'empTypeName'
  			}, {
  				header : '进厂类别',
  				width : 80,
                sortable : true,
                dataIndex : 'inType'
  			}, {
  				header : '试用期开始',
  				width : 100,
                sortable : true,
                dataIndex : 'tryoutStartDate'
  			}, {
  				header : '试用期结束',
  				width : 100,
                sortable : true,
                dataIndex : 'tryoutEndDate'
  			}, {
  				header : '是否存档',
  				width : 80,
                sortable : true,
                dataIndex : 'empState',
                renderer : empState
  			}*/, {
  				header : '备注',
  				width : 150,
                sortable : true,
                dataIndex : 'memo'
  			}],
  		tbar : ['年度:', dateTxt, '-', '所属部门:', deptTxt, hiddenDeptTxt,
  				'-'/*, '是否存档:', isSaveCbo, '-'*/,
  				queryBtn, addBtn, updateBtn, deleteBtn],
  		bbar : new Ext.PagingToolbar({
            pageSize : Constants.PAGE_SIZE,
            store : gridStore,
            displayInfo : true,
            displayMsg : Constants.DISPLAY_MSG,
            emptyMsg : Constants.EMPTY_MSG
        }),
        frame : false,
        border : false,
        enableColumnHide : true,
        enableColumnMove : false
  	});
  	
  	// 单击一行事件
    grid.on('rowdblclick', updateHandler);
	
  	// 设定布局器及面板
	new Ext.Viewport({   
		enableTabScroll : true,
        layout : "border",
        items : [grid]
    });
    
    // 当前日期
  	var date = new Date().format('Y');
  	// 加载数据
  	loadStore(date, true);
  	// 加载登记Tab页
  	newEmployee.refreshTabRegister();
    
    /**
     * 选择部门
     */
    function deptSelect() {
    	if(newEmployee.deptSelect()) {
//	    	newEmployee.deptSelect();
	    	// 根据返回值设置画面的值
	    	deptTxt.setValue(newEmployee.name);
	    	Ext.get("deptId").dom.value = newEmployee.id;
    	}
    }
    
    /**
     * 查询处理
     */
    function queryHandler() {
    	loadStore(dateTxt.getValue(), false);
    }
    
    /**
     * 加载数据
     */
    function loadStore(date, flag) {
    	// 查询基本条件
		gridStore.baseParams = {
			date : date,
			deptIdQuery : Ext.get("deptId").dom.value,
			isSave : isSaveCbo.getValue(),
			flag:'deptFlag'//add by sychen 20100716
	  	};
	  	// 查询
		gridStore.load({
			params : {
				start : 0,
				limit : Constants.PAGE_SIZE
			},
			callback : function() {
				if(!flag) {
					if(gridStore.getCount() == 0) {
						PD002_MSG.alert(Constants.REMIND, Constants.COM_I_003);
					}
				}
			}
		});
    }
    
    /**
     * 新增处理
     */
    function addHandler() {
    	newEmployee.flag = newEmployee.ADD;
		newEmployee.editTabRegister();
    }
    
    /**
     * 修改处理
     */
    function updateHandler() {
    	var selected = grid.selModel.getSelections();
    	var record = selected[0];
    	if (selected.length < 1) {
			PD002_MSG.alert(Constants.REMIND, Constants.COM_I_001);
		} else if(selected[0].get("empState") == newEmployee.EMP_STATE_2) {
			PD002_MSG.alert(Constants.ERROR, Constants.COM_E_034);
		} else {
			newEmployee.flag = newEmployee.UPDATE;
			newEmployee.editTabRegister(selected[0].data);
		}
    }
    
    /**
     * 删除处理
     */
    function deleteHandler() {
    	var selected = grid.selModel.getSelections();
    	if (selected.length < 1) {
			PD002_MSG.alert(Constants.REMIND, Constants.COM_I_001);
		} else if(selected[0].get("empState") == newEmployee.EMP_STATE_2) {
			PD002_MSG.alert(Constants.ERROR, Constants.COM_E_035);
		} else {
			PD002_MSG.confirm(Constants.NOTICE_CONFIRM, Constants.COM_C_002, function(
				buttonobj) {
				// 确认保存
				if (buttonobj == "yes") {
					Ext.lib.Ajax.request(Constants.POST,
						'hr/deleteNewEmployee.action', {
						success : function(result, request) {
                        	if (result.responseText) {
	                            var o = eval("(" + result.responseText + ")");
	                            var succ = o.msg;
	                            if(succ == Constants.DATE_FAILURE){
                                	PD002_MSG.alert(Constants.ERROR,
                                    	Constants.COM_E_023); 
	                            } else if(succ == Constants.SQL_FAILURE){
                                	PD002_MSG.alert(Constants.ERROR,
                                    	Constants.COM_E_014); 
	                            } else if(succ == Constants.DATA_USING){
                                	PD002_MSG.alert(Constants.ERROR,
                                    	Constants.COM_E_015); 
	                            } else {
                                	PD002_MSG.alert(Constants.REMIND,
                                    	Constants.COM_I_005);
                                    refreshData();
		                    		if(typeof newEmployee.refreshTabRegister == 'function') {
		                    			newEmployee.refreshTabRegister();
		                    		}
	                            }
	                        }
						},
						failure : function() {
							PD002_MSG.alert(Constants.ERROR,
	                        	Constants.DEL_ERROR); 
						}
					}, 'empId=' + selected[0].data.empId
						+'&lastModifiyDate=' + selected[0].data.lastModifiyDate);
				}
			});
		}
    }
    
    /**
     * 员工状态
     */
    function empState(value) {
		if(value == newEmployee.EMP_STATE_2) {
    		return newEmployee.IF_YES;
    	} else {
    		return newEmployee.IF_NO;
    	}
    }
    
    /**
     * 刷新页面数据
     */
    function refreshData() {
    	loadStore(dateTxt.getValue(), false);
    }
});