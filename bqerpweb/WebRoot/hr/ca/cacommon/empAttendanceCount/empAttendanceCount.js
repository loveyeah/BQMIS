Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {
    Ext.QuickTips.init();

   function getCurrentMonth()
   {
   		var date = new Date();
   		var y = date.getFullYear();
   		var m = date.getMonth() + 1;
   		
   		var val = y + "-" + (m > 9 ? m : "0" + m);
   		return val;
   }

    // 选择模式
    var sm = new Ext.grid.RowSelectionModel({
        singleSelect : true
    });
	
    var empRecord = new Ext.data.Record.create([{
    	name : 'empId'
    },{
    	name : 'empName'
    },{
    	name : 'deptId'
    },{
    	name : 'deptName'
    },{
    	name : 'attendanceDeptId'
    },{
    	name : 'attendanceDeptName'
    },{
    	name : 'month'
    },{
    	name : 'sickLeavlCount'
    },{
    	name : 'absentCount'
    },{
    	name : 'eventCount'
    },{
    	name : 'eveningAddCount'
    },{
    	name : 'weekendAddCount'
    },{
    	name : 'holidayAddCount'
    },{
    	name : 'otherTimeCount'
    }])
    var empStore = new Ext.data.JsonStore({
        url : 'ca/getEmpAttendanceRecordList.action',
        root : 'list',
        totalProperty : 'totalCount',
        fields : empRecord
    })

  

    // 查询button
    var btnSearch = new Ext.Button({
        text : Constants.BTN_QUERY,
        iconCls : Constants.CLS_QUERY,
        handler : search
    });

     // 销假时间
    var month = new Ext.form.TextField({
        style : 'cursor:pointer',
        readOnly : true,
        value : getCurrentMonth(),
        listeners : {
            focus : function() {
                WdatePicker({
                    // 时间格式
                    startDate : '%y-%M',
                    dateFmt : 'yyyy-MM',
                    alwaysUseStartDate : false,
                    isShowClear : false
                });
               this.blur()
            }
        }
    });
   var deptNs = new Power.dept()

    // toolbar
    var headTbar = new Ext.Toolbar({
        region : 'north',
        border : false,
        items : ['&nbsp部门:',deptNs.combo,'月份：',month,'-', btnSearch]
    });

    var colms = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
            header : "行号",
            width : 35
        }), {
            header : '员工Id',
            sortable : true,
            hidden : true,
            dataIndex : 'empId'
        }, {
            header : '员工',
            sortable : true,
            dataIndex : 'empName'
        }, {
            header : '员工部门Id',
            sortable : true,
            hidden : true,
            dataIndex : 'deptId'
        }, {
            header : '员工部门',
            sortable : true,
            dataIndex : 'deptName'
        }, {
            header : '员工考勤部门Id',
            sortable : true,
            hidden : true,
            dataIndex : 'attendanceDeptId'
        }, {
            header : '员工考勤部门',
            sortable : true,
            dataIndex : 'attendanceDeptName'
        }, {
            header : '月份',
            sortable : true,
            dataIndex : 'month'
        }, {
            header : '病假',
            sortable : true,
            dataIndex : 'sickLeavlCount'
        }, {
            header : '旷工',
            sortable : true,
            dataIndex : 'absentCount'
        }, {
            header : '事假',
            sortable : true,
            dataIndex : 'eventCount'
        }, {
            header : '晚上加班',
            sortable : true,
            dataIndex : 'eveningAddCount'
        }, {
            header : '周末加班',
            sortable : true,
            dataIndex : 'weekendAddCount'
        }, {
            header : '节假日加班',
            sortable : true,
            dataIndex : 'holidayAddCount'
        }, {
            header : '其他请假时间',
            sortable : true,
            dataIndex : 'otherTimeCount'
        }]);


    empStore.on('beforeload', function() {
        this.baseParams = {
            month : month.getValue(),
            deptId : deptNs.combo.getValue()
        };
    })

    // 员工请假信息grid
    var grid = new Ext.grid.GridPanel({
        sm : sm,
        layout : 'fit',
        region : 'center',
        border : false,
        cm : colms,
        store : empStore,
        tbar : headTbar,
        bbar : new Ext.PagingToolbar({
          pageSize : 18,
				store : empStore,
				displayInfo : true,
				displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
				emptyMsg : "没有记录",
				beforePageText : '页',
				afterPageText : "共{0}"
        }),
        enableColumnMove : false
    });

    function search()
    {
    	if(deptNs.combo.getValue() != null && deptNs.combo.getValue() != '')
    	{
    		empStore.load({
    			params : {
    				start : 0,
    				limit : 18
    			}
    		})
    	}
    }

  

    // 显示区域
    var view = new Ext.Viewport({
        enableTabScroll : true,
        autoScroll : true,
        layout : "fit",
        items : [grid]
    })
})
