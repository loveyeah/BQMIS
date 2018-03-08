// 选择任务单页面 
    Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
	Ext.onReady(function() {
		
		var taskTypeCode=window.dialogArguments.typeCode;
    //==============  定义grid ===============
     Ext.QuickTips.init(); 
     
    function renderDate(value) {
        if (!value) return "";
        
        var reDate = /\d{4}\-\d{2}\-\d{2}/gi;
        var reTime = /\d{2}:\d{2}:\d{2}/gi;
        
        var strDate = value.match(reDate);
        var strTime = value.match(reTime);
        if (!strDate) return "";
        return strTime ? strDate + " " + strTime : strDate;
    }
    
    // ID号
    var ID = new Ext.form.TextField({
        id : "ID",
        name : "taskID",
        width : 80,
        maxLength :25
    });
     
    // 任务单NO号
    var taskNo = new Ext.form.TextField({
        id : "taskNo",
        name : "taskNo",
        width : 80,
        maxLength :25
    });
    
    // 任务单名称
    var taskName = new Ext.form.TextField({
        id : "taskName",
        name : "taskName",
        width : 80,
        maxLength :25
    });
    
    // 任务单接受人
    var receiver = new Ext.form.TextField({
        id : "receiver",
        name : "receiver",
        width : 80,
        maxLength :25
    });
    
     // 任务单接受人
    var receiveDate = new Ext.form.TextField({
        id : "receiveDate",
        name : "receiveDate",
        width : 80,
        maxLength :25
    });
    
//     // 任务单类型
//    var taskType = new Ext.form.TextField({
//        id : "taskType",
//        name : "taskType",
//        width : 80,
//        maxLength :25
//    });
    
    // 任务单数据来源
    var MyRecord = Ext.data.Record.create([
        {name : 'id'},
        {name : 'taskType'},
        {name : 'taskNo'},
        {name : 'taskName'},
        {name : 'receiver'},
        {name : 'receiveDate'},
    ]);

    var dataProxy = new Ext.data.HttpProxy({
        url:'workticket/getTaskList.action'
    });

    var theReader = new Ext.data.JsonReader({
        root : "list",
        totalProperty : "totalCount"
    }, MyRecord);

    var store = new Ext.data.Store({
        proxy : dataProxy,
        reader : theReader
    });
   
    // grid列显示模式
    var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({
            	header : '行号',
    			sortable : true,
    			width : 40
        	}), {
        header : 'ID',
        sortable : true,
        dataIndex : 'id',
        hidden: true
    }, {
        header : '任务单号',
        sortable : true,
        dataIndex : 'taskNo'
    },{
        header : '任务单名称',
        sortable : true,
        dataIndex : 'taskName'
    },{
        header : '任务单接受人',
        sortable : true,
        dataIndex : 'receiver'
    },{
        header : '任务单接受时间',
        sortable : true,
        dataIndex : 'receiveDate',
        renderer : renderDate
    }
//    ,{
//        header : '任务类型',
//        sortable : true,
//        dataIndex : 'taskType'
//    }
    ]);
    
    // 显示主体
    var grid = new Ext.grid.GridPanel({
        region : "center",
        layout : 'fit',
        store : store,
        cm : cm,
        enableColumnMove : false ,
        autoSizeColumns : true,
        viewConfig : {
            forceFit : true
        },
        sm : new Ext.grid.RowSelectionModel({
            singleSelect : true
        }),

        tbar : ['模糊查找(任务单号):', taskNo, '-',{
                text : Constants.BTN_QUERY,
                iconCls :Constants.CLS_QUERY,
                handler : queryRecord
            }],
        // 分页
        bbar : new Ext.PagingToolbar({
            pageSize : Constants.PAGE_SIZE,
            store : store,
            displayInfo : true,
            displayMsg : Constants.DISPLAY_MSG,
            emptyMsg : Constants.EMPTY_MSG
        })
    });



    new Ext.Viewport({
        enableTabScroll : true,   
        layout : "border",
        items : [{
            xtype : "panel",
            region : 'center',
            layout : 'fit',
            border : false,
            items : [grid]
        }]
    });
    
    // 双击grid执行操作
    grid.on("rowdblclick", returnRecord);
    
        
     /* 查询 */
    function queryRecord() {
        var noText = taskNo.getValue();
//        var typeText = taskType.value;
//        if (!typeText) {
////            Ext.Msg.alert(Constants.SYS_REMIND_MSG, "所属部门选择不能为空！")
//        	typeText = '0';
//        }
        store.baseParams = {
            taskNo : noText,
            taskType : taskTypeCode
        };
        store.load({
            params : {
                start : 0,
                limit : Constants.PAGE_SIZE
            }
        });

    }
    
    /* 返回一条记录 */
     function returnRecord() {
        // 得到该行记录
        var record;
        record = grid.getSelectionModel().getSelected();
        if (record != null) {
            var object = new Object();
            object.taskNo = record.data.taskNo;
//            object.empCode = record.data.empCode;
            window.returnValue = object;
            window.close();
        }
    }
    queryRecord();
    });