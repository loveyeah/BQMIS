Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	// ============== 定义grid ===============
	var MyRecord = Ext.data.Record.create([{
		name : 'model.workticketNo'
	}, {
		name : 'statusName'
	}, {
		name : 'sourceName'
	}, {
		name : 'chargeByName'
	}, {
		name : 'deptName'
	}, {
		name : 'planStartDate'
	}, {
		name : 'planEndDate'
	}, {
		name : 'blockName'
	}, {
		name : 'model.workticketContent'
	}, {
		name : 'isEmergencyText'
	}, {
		name : 'model.workFlowNo'
	}, {
		name : 'model.workticketTypeCode'
	},{
		name : 'model.workticketStausId'
	},
	{ 
	name:'model.firelevelId'},
		{
		name : 'model.isEmergency'
	},
	{name:'model.equAttributeCode'}
	
	]);

	// 定义获取数据源
	var dataProxy = new Ext.data.HttpProxy({
		url : 'workticket/findEndWorkticketList.action'
	});

	// 定义格式化数据
	var theReader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	}, MyRecord);

	// 定义封装缓存数据的对象
	var queryStore = new Ext.data.Store({
		// 访问的对象
		proxy : dataProxy,
		// 处理数据的对象
		reader : theReader
	});

	// 定义类型数据
	var typeDate = new Ext.data.JsonStore({
		root : 'list',
		url : "bqworkticket/getWorkticketTypeList.action",
		fields : ['workticketTypeCode', 'workticketTypeName']
	});
	typeDate.on('load', function(e, records) {
		typeComboBox.setValue(records[0].data.workticketTypeCode);
	});
	typeDate.load();

	// 定义机组或系统数据
	var systemDate = new Ext.data.JsonStore({
		root : 'list',
		url : "bqworkticket/getSystemList.action",
		fields : ['blockCode', 'blockName']
	});
	systemDate.on('load', function(e, records) {
		systemComboBox.setValue(records[0].data.blockCode);
	});
	systemDate.load();



	// 系统当天日期
	var sd = new Date();
	var ed = new Date();
	// 系统当天前30天的日期
	sd.setDate(sd.getDate() - 30);
	// 系统当天后30天的日期
	ed.setDate(ed.getDate() + 30);

	// 定义查询起始时间
	var startDate = new Ext.form.DateField({
		format : 'Y-m-d',
		name : 'startDate',
		id : 'startDate',
		itemCls : 'sex-left',
		clearCls : 'allow-float',
		value : sd,
		readOnly : false,
		checked : true,
		emptyText : '请选择',
		width : 85,
		listeners : {
			change : function() {
				// 判断输入的开始时间是否大于结束时间
				if (startDate.value != null && endDate.value != null) {
					if (Date.parseDate(startDate.value, "Y-m-d") > endDate
							.parseDate(endDate.value, "Y-m-d")) {
						Ext.Msg.alert(Constants.SYS_REMIND_MSG,
								"输入的起始时间大于结束时间，请重新输入！");
						return false;
					}
				}
			}
		}
	});

	// 定义查询结束时间
	var endDate = new Ext.form.DateField({
		format : 'Y-m-d',
		name : 'endDate',
		id : 'endDate',
		itemCls : 'sex-left',
		clearCls : 'allow-float',
		value : ed,
		readOnly : false,
		checked : true,
		emptyText : '请选择',
		width : 85,
		listeners : {
			change : function() {
				// 判断输入的开始时间是否大于结束时间
				if (startDate.value != null && endDate.value != null) {
					if (Date.parseDate(startDate.value, "Y-m-d") > endDate
							.parseDate(endDate.value, "Y-m-d")) {
						Ext.Msg.alert(Constants.SYS_REMIND_MSG,
								"输入的起始时间大于结束时间，请重新输入！");
						return false;
					}
				}
			}
		}
	});

	// 定义类型    modify by drdu  090327
	var typeComboBox = new Ext.form.ComboBox({
		id : "typeCob",
		store : typeDate,
		displayField : "workticketTypeName",
		valueField : "workticketTypeCode",
		mode : 'local',
		triggerAction : 'all',
		width : 120,
		readOnly : true
	});

	// 定义所属机组或系统
	var systemComboBox = new Ext.form.ComboBox({
		id : "systemCob",
		store : systemDate,
		displayField : "blockName",
		valueField : "blockCode",
		mode : 'local',
		width : 140,
		triggerAction : 'all',
		readOnly : true
	});

	//add by fyyang 090513 工作内容		
			   var txtContent = new Ext.form.TextField({
				name : 'txtContent' ,
				width:320
			});
			var txtNoContent = new Ext.form.TextField({
				name : 'txtNoContent' ,
				width:135
			});

	// 定义选择列
	var sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true,
		header : '选择',
		width : 35,
		id : 'sm'
	});

		//----add by fyyang - 090311----
	
	   var expander = new  Ext.grid.RowExpander({
        tpl : new Ext.Template(
        //    '<p><b>工作票号:</b> </p><br>',
            '<p><b>工作内容：</b></p>'
        ),
        expandRow:mygridExpend
    });
    
    function  mygridExpend(row)
    {
    	   if(typeof row == 'number'){
            row = this.grid.view.getRow(row);
        }
        var record = this.grid.store.getAt(row.rowIndex);
       var no=record.get("model.workticketNo");
       var content="";
       //-----------------------
       Ext.Ajax.request({
				url : 'workticket/getContentWorkticketByNo.action',
				params : {
					workticketNo :  no 
				},
				method : 'post',
				success : function(result, request) {
					var content=result.responseText;
					content=content.substring(1,content.length-1);
				    while(content.indexOf('\\r')!=-1)
				    {
				    
				    	content=content.replace("\\r","");
				    }
				     while(content.indexOf('\\n')!=-1)
				     {
				    content=  content.replace("\\n","<br>"); 
				     } 
				     expander.tpl = new Ext.Template(
						'<p> <br><font color="blue"><b>工作内容：</b><br>'+content+'</font></p>');
					var body = Ext.DomQuery.selectNode(
								'tr:nth(2) div.x-grid3-row-body', row);
						if (expander.beforeExpand(record, body, row.rowIndex)) {
							expander.state[record.id] = true;
							Ext.fly(row).replaceClass('x-grid3-row-collapsed',
									'x-grid3-row-expanded');
							expander.fireEvent('expand', expander, record, body, row.rowIndex);
						}
			
				},
				failure : function(result, request) {
					Ext.MessageBox.alert('错误', '操作失败!');
				}
			});

 
    }
   
	// ---------------------------
	// --gridpanel显示格式定义-----开始-------------------
	var grid = new Ext.grid.GridPanel({
			renderTo:'mygrid',
		region : "center",
		layout : 'fit',
		store : queryStore,
		  viewConfig : {
		forceFit : true 
		},
		columns : [{
			header : "工作编号",
			width : 220,
			sortable : true,
			dataIndex : 'model.workticketNo'
		},  {
			header : "工作内容",
			width : 300,
			sortable : true,
			//hidden:true,
			dataIndex : 'model.workticketContent',
			renderer:function(value, metadata, record){ 
			metadata.attr = 'style="white-space:normal;"'; 
			return value;  
	}
	},{
			header : "状态",
			sortable : true,
			dataIndex : 'statusName'
		}, {
			header : "来源",
			sortable : true,
			dataIndex : 'sourceName'
		}, {
			header : "工作负责人",
			sortable : false,
			dataIndex : 'chargeByName'
		}, {
			header : "所属部门",
			sortable : true,
			dataIndex : 'deptName'
		}, {
			header : "计划开始时间",
		//	width : 110,
			sortable : true,
			dataIndex : 'planStartDate'
		}, {
			header : "计划结束时间",
		//	width : 110,
			sortable : true,
			dataIndex : 'planEndDate'
		}, {
			header : "所属系统",
			sortable : true,
			dataIndex : 'blockName'
		},  {
			header : "是否紧急票",
		//	width : 90,
			sortable : true,
			dataIndex : 'isEmergencyText'
		}],
		sm : sm,
		// 头部工具栏
		tbar : ['时间:', startDate, '~', endDate, '-','类型:', typeComboBox,'-',
				'所属机组或系统:', systemComboBox],
		bbar : new Ext.PagingToolbar({
			pageSize : Constants.PAGE_SIZE,
			store : queryStore,
			displayInfo : true,
			displayMsg : Constants.DISPLAY_MSG,
			emptyMsg : Constants.EMPTY_MSG
		}),
		enableColumnMove : false
	});
	
	  
	// --gridpanel显示格式定义-----结束--------------------

	 new Ext.Toolbar({  
            renderTo:grid.tbar
            ,items:['工作内容：',txtContent,'-','工作票编号：',txtNoContent,'-', {
					text : Constants.BTN_QUERY,
					iconCls : Constants.CLS_QUERY,
					handler : queryRecord
				}]  
        }); 
	// 注册双击事件
	grid.on("rowdblclick", edit);

	// 页面加载显示数据
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		items : [grid]
	});

	/**
	 * 查询
	 */
	function queryRecord() {
		// 判断输入的开始时间是否大于结束时间
		if (startDate.value != null && endDate.value != null) {
			if (Date.parseDate(startDate.value, "Y-m-d") > endDate.parseDate(
					endDate.value, "Y-m-d")) {
				Ext.Msg.alert(Constants.SYS_REMIND_MSG, "输入的起始时间大于结束时间，请重新输入！");
				return false;
			}
		}
		queryStore.baseParams = {
			startD : startDate.value,
			endD : endDate.value,
			typeC : typeComboBox.value,
			systemC : systemComboBox.value,
			content:txtContent.getValue(),
			ticketNo:txtNoContent.getValue()
		};
		queryStore.load({
			params : {
				start : 0,
				limit : Constants.PAGE_SIZE
			}
		});
	}

	grid.on("load", queryRecord());

	/**
	 * 双击grid事件
	 */
	function edit() {
			var record = grid.getSelectionModel().getSelected();
		var workticketNo = record.get('model.workticketNo');
		var firelevelId=record.get("model.firelevelId");
		Ext.Msg.confirm('提示',
						'确定由'+workticketNo+'生成标准票吗？', function(buttonobj) {
							if (buttonobj == "yes") {
								
 Ext.Ajax.request({
				url : 'workticket/createStandardByEndNo.action',
				params : {
					workticketNo :  workticketNo 
				},
				method : 'post',
				success : function(result, request) {
			       
			    
			       	var obj=new Object();
			       	obj.workticketNo=result.responseText.substring(1,result.responseText.length-1);
			       	obj.fireLevelId=firelevelId;
			       	window.returnValue=obj;
			       	window.close();
			       
				},
				failure : function(result, request) {
					Ext.MessageBox.alert('错误', '操作失败!');
				}
			});

							}
						}
						);
		
	

	}
});