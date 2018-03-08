Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function(){
	
	var arg = window.dialogArguments;
	
	var selectModel = arg ? arg.selectModel : 'multiple';
	// 默认为单选
	var isSingleSelect = !(selectModel == "multiple");
	
	// 采购员数据
	var buyerData = Ext.data.Record.create([
        // 人员编码
        {name: 'buyer'},
        // 采购员姓名
        {name: 'buyerName'},
        // 手机号
        {name: 'mobileNo'},
        // 固定电话
        {name: 'telephone'},
        // 传真
        {name: 'fax'},
        // 标识
        // Y: 从数据库中取出
        // N: 运行时增加
        {name: 'flag'}
    ]);

     var buyerStore = new Ext.data.JsonStore({
        url : 'resource/getPurchaserList.action',
        root : 'list',
        sortInfo :{field: "buyer", direction: "ASC"},
        fields : buyerData
  	});
  	
    //载入数据
    buyerStore.load();
    
    // 选择列
	var sm = new Ext.grid.RowSelectionModel({singleSelect : isSingleSelect});
	// 人员信息选择
	var txtBuyer = new Ext.form.TriggerField({
		allowBlank : false,
		maxLength : 30,
		readOnly : true
	});
	
	// 采购员Grid
	var buyerGrid = new Ext.grid.EditorGridPanel({
		region : 'center',
		layout : 'fit',
		height : 200,
		width : 800,
		isFormField : false,
		// 单击修改
        clicksToEdit : 1,
        store : buyerStore,
        sm : sm,
        columns : [new Ext.grid.RowNumberer({header:"行号",width : 35}),
        	{   header : "人员编码<font color='red'>*</font>",
                width : 30,
                sortable : true,
                defaultSortable : true,
            	css:CSS_GRID_INPUT_COL,
//                editor : txtBuyer,
                dataIndex : 'buyer'
            }, {
                header : '采购员姓名',
                width : 40,
                sortable : true,
                dataIndex : 'buyerName'
            }, {
                header : '手机号',
                width : 30,
                sortable : true,
                dataIndex : 'mobileNo'
            }, {
                header : '固定电话',
                width : 30,
                sortable : true,
                dataIndex : 'telephone'
            }, {
                header : '传真',
                width : 30,
                sortable : true,
                dataIndex : 'fax'
            }],
        viewConfig : {
            forceFit : true
        },
        tbar:['采购员选择',
        	{text:'确定',
        	 iconCls:'save',
        	 handler:chooseWorker},
        	 {
        	 	text:'清除',
        	 	iconCls:'cancer',
        	 	handler:function()
        	 	{
        	 		var emp=new Object();
        	 		emp.names="";
        	 		emp.codes="";
        	 		window.returnValue = emp;
			         window.close();
        	 	}
        	 }
        	],
       autoSizeColumns : true,
       frame : false,
       autoScroll : true,
       border : false,
       enableColumnHide : true,
       enableColumnMove : false
    });
	
	// 设置布局及面板
	new Ext.Viewport({
		layout : 'border',
		auotHeight : true,
		items : [{
			region : 'center',
			layout : 'fit',
			height : 200,
			items : [buyerGrid]
		}]
	})
	if (isSingleSelect) {
		buyerGrid.on("rowdblclick", function() {
			chooseWorker();
		});
	}
	
	function chooseWorker() {
		// 单选
		if (!isSingleSelect) {
			var record = buyerGrid.getSelectionModel().getSelected();
			if (typeof(record) != "object") {
				Ext.Msg.alert("提示", "请选择人员!");
				return false;
			}
			var ro = record.data;
			window.returnValue = ro;
			window.close();
		}
		// 多选
		else {
			var selectNodes = buyerGrid.getSelectionModel().getSelections();
			if (selectNodes.length == 0) {
				Ext.Msg.alert("提示", "请选择人员!");
				return false;
			}
			var ros = new Array();
			var workerCodes = new Array();
			var workerNames = new Array();
			for (var i = 0; i < selectNodes.length; i++) {
				var record = selectNodes[i].data;
				workerCodes.push(record.buyer);
				workerNames.push(record.buyerName);
				ros.push(record);
			}
			window.returnValue = {
				list : ros,
				codes : workerCodes.join(","),
				names : workerNames.join(",")
			};
			window.close();
		}
	}
	
});