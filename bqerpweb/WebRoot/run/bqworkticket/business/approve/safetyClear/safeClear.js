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
				name : 'model.workticketTypeCode'
			},{
				name : 'isEmergencyText'
			}]);

	// 系统当天日期
	var sd = new Date();
	var ed = new Date();
	// 系统当天前30天的日期
	sd.setDate(sd.getDate() - 30);
	// 系统当天后30天的日期
	ed.setDate(ed.getDate() + 30);

	// 定义获取数据源
	var dataProxy = new Ext.data.HttpProxy({
				url : 'workticket/getSecurityMeasureList.action'
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
	// queryStore.load();

	// 定义类型数据  modify by drdu 090327
	var typeDate = new Ext.data.JsonStore({
				root : 'list',
				//url : "workticket/getWorkTypeList.action",
				url : "bqworkticket/getWorkticketTypeList.action",
				fields : ['workticketTypeCode', 'workticketTypeName']
			});
	typeDate.on('load', function(e, records, o) {
		typeComboBox.setValue(records[0].data.workticketTypeCode);
	});
	typeDate.load();

	// 定义机组或系统数据  modify by drdu 090327
	var systemDate = new Ext.data.JsonStore({
				root : 'list',
				//url : "workticket/getSystemList.action",
				url : "bqworkticket/getSystemList.action",
				fields : ['blockCode', 'blockName']
			});
	systemDate.on('load', function(e, records, o) {
		systemComboBox.setValue(records[0].data.blockCode);
	});
	systemDate.load();

	// 定义安措拆除状态
	var stateDate = new Ext.data.SimpleStore({
				data : [['', '所有状态'], [1, '全部拆除'], [2, '部分拆除'], [3, '未拆除']],
				fields : ['value', 'name']
			});

	// 定义运行专业
	var runProfDate = new Ext.data.JsonStore({
				root : 'list',
				url : "workticket/getRunSpecialList.action",
				fields : ['specialityCode', 'specialityName']
			});
	runProfDate.on('load', function(e, records, o) {
		runComboBox.setValue(records[0].data.specialityCode);
	});
	runProfDate.load();

	// 定义检修专业
	var haulProfDate = new Ext.data.JsonStore({
				root : 'list',
				url : "workticket/getHaulSpecialList.action",
				fields : ['specialityCode', 'specialityName']
			});
	haulProfDate.on('load', function(e, records, o) {
		haulComboBox.setValue(records[0].data.specialityCode);
	});
	haulProfDate.load();

	// -----------------控件定义----------------------------------
	// 定义查询起始时间
	var startDate = new Ext.form.DateField({
				format : 'Y-m-d',
				name : 'startDate',
				id : 'startDate',
				itemCls : 'sex-left',
				clearCls : 'allow-float',
				readOnly : false,
				checked : true,
				emptyText : '请选择',
				width : 85,
				value : sd,
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
				format : 'Y-m-d ',
				name : 'endDate',
				id : 'endDate',
				itemCls : 'sex-left',
				clearCls : 'allow-float',
				readOnly : false,
				checked : true,
				emptyText : '请选择',
				width : 85,
				value : ed,
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

	// 定义类型
	var typeComboBox = new Ext.form.ComboBox({
				id : "typeCob",
				store : typeDate,
				displayField : "workticketTypeName",
				valueField : "workticketTypeCode",
				mode : 'local',
				triggerAction : 'all',
				readOnly : true,
				width : 120
			});

	// 定义所属机组或系统
	var systemComboBox = new Ext.form.ComboBox({
				width : 100,
				id : "systemCob",
				store : systemDate,
				displayField : "blockName",
				valueField : "blockCode",
				mode : 'local',
				triggerAction : 'all',
				readOnly : true
			});

	// 定义安措拆除状态
	var stateComboBox = new Ext.form.ComboBox({
				id : "stateCob",
				store : stateDate,
				displayField : "name",
				valueField : "value",
				mode : 'local',
				triggerAction : 'all',
				hiddenName:'stateComboBox',
				readOnly : true,
				value:'',
				width : 90
			});
//	stateComboBox.on('render', function() {
//		stateComboBox.setValue(stateDate.getAt(0).data["name"])
//	});

	// 定义运行专业
	var runComboBox = new Ext.form.ComboBox({
				id : "runCob",
				store : runProfDate,
				displayField : "specialityName",
				valueField : "specialityCode",
				mode : 'local',
				triggerAction : 'all',
				readOnly : true,
				width : 100
			});

	// 定义检修专业
	var haulComboBox = new Ext.form.ComboBox({
				id : "haulCob",
				store : haulProfDate,
				displayField : "specialityName",
				valueField : "specialityCode",
				mode : 'local',
				triggerAction : 'all',
				readOnly : true,
				width : 130
			});
	// 定义选择列
	var sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true,
				header : '选择',
				id : 'sm',
				width : 35
			});

 	// head工具栏    
    var headTbar=new Ext.Toolbar({
    	region : 'north',
        items:['选择查询时间:', startDate, '~', endDate, '-','工作票类型:',
						typeComboBox, '-', '安措拆除状态:', stateComboBox]
    });
    
    		//----add by fyyang - 090318----
	
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
       var conn = Ext.lib.Ajax.getConnectionObject().conn;
	conn.open("POST", 'workticket/getContentWorkticketByNo.action?workticketNo='
			+ no, false);
	conn.send(null);
	 
	// 成功状态码为200
	if (conn.status == "200") {
		var result = conn.responseText;
	    content=result.substring(1,result.length-1);
	    while(content.indexOf('\\r')!=-1)
	    {
	    
	    	content=content.replace("\\r","");
	    }
	     while(content.indexOf('\\n')!=-1)
	     {
	    content=  content.replace("\\n","<br>");
	     }
	    
	} 
	
				expander.tpl = new Ext.Template(
			//	'<p><font color="blue"><b>工作票号：</b>'+no+'</font> </p><br>', 
				'<p> <br><font color="blue"><b>工作内容：</b><br>'+content+'</font></p>');
			var body = Ext.DomQuery.selectNode(
						'tr:nth(2) div.x-grid3-row-body', row);
				if (this.beforeExpand(record, body, row.rowIndex)) {
					this.state[record.id] = true;
					Ext.fly(row).replaceClass('x-grid3-row-collapsed',
							'x-grid3-row-expanded');
					this.fireEvent('expand', this, record, body, row.rowIndex);
				}
 
    }
   
	// ---------------------------
	// --gridpanel显示格式定义-----开始-------------------
	var grid = new Ext.grid.GridPanel({
				region : "center",
				layout : 'fit',
				store : queryStore,
				columns : [expander,sm, {
							header : "工作编号",
							sortable : true,
							width : 90,
							dataIndex : 'model.workticketNo'
						}, {
							header : "状态",
							sortable : true,
							width : 50,
							dataIndex : 'statusName'
						}, {
							header : "来源",
							sortable : true,
							width : 60,
							dataIndex : 'sourceName'
						}, {
							header : "工作负责人",
							sortable : true,
							width : 70,
							dataIndex : 'chargeByName'
						}, {
							header : "所属部门",
							sortable : true,
							width : 75,
							dataIndex : 'deptName'
						}, {
							header : "计划开始时间",
							sortable : true,
							width : 110,
							dataIndex : 'planStartDate'
						}, {
							header : "计划结束时间",
							sortable : true,
							width : 110,
							dataIndex : 'planEndDate'
						}, {
							header : "所属系统",
							sortable : true,
							width : 60,
							dataIndex : 'blockName'
						}, {
							header : "工作内容",
							sortable : true,
							width : 300,
							hidden:true,
							dataIndex : 'model.workticketContent',
							renderer : function(value) {
								if (value) {
									return value.replace(/\n/g, '<br/>');
								}
								return '';
							}
						}, {
							header : "是否紧急票",
							sortable : true,
							width : 70,
							dataIndex : 'isEmergencyText'
						},{
							header : "工作票类型",
							sortable : true,
							hidden : true,
							dataIndex : 'model.workticketTypeCode'
						}],
				sm : sm,
				autoScroll:true,
				enableColumnMove : false,
				autoSizeColumns : true,
				// 头部工具栏
				tbar : ['运行专业:', runComboBox, '-', "检修专业:", haulComboBox, '-', "所属机组或系统:",
						systemComboBox, '-', {
							text : Constants.BTN_QUERY,
							iconCls : Constants.CLS_QUERY,
							handler : queryRecord
						}],
				bbar : new Ext.PagingToolbar({
							pageSize : Constants.PAGE_SIZE,
							store : queryStore,
							displayInfo : true,
							displayMsg : Constants.DISPLAY_MSG,
							emptyMsg : Constants.EMPTY_MSG
						}),
		 plugins: expander,
		  collapsible: true,
        animCollapse: false
			});
	// --gridpanel显示格式定义-----结束--------------------
	//grid.on("load", queryRecord());
	// 注册双击事件
	grid.on("rowdblclick", edit);

	// 页面加载显示数据
	new Ext.Viewport({
				enableTabScroll : true,
				layout : "border",
				items : [headTbar, grid]
			});

	/**
	 * 查询事件
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
			// 计划开始时间
			startD : startDate.value,
			// 计划结束时间
			endD : endDate.value,
			// 工作票类型
			typeC : typeComboBox.value,
			// 安措拆除状态
			stateC : stateComboBox.value,
			// 运行专业
			runC : runComboBox.value,
			// 检修专业
			haulC : haulComboBox.value,
			// 所属机组和系统
			systemC : systemComboBox.value
		};
		queryStore.load({
					params : {
						start : 0,
						limit : Constants.PAGE_SIZE
					}
				});
	}
	
	function edit() {
		// 获取选择的行
		var record = grid.getSelectionModel().getSelected();
		// 获取工作编号
		var workN = record.get("model.workticketNo");
		// 获取所属机组或系统
		var workType = record.get("blockName");
		if (grid.selModel.hasSelection()) {
			var records = grid.selModel.getSelections();
			var recordslen = records.length;
			if (recordslen > 1) {
				Ext.Msg.alert(Constants.SYS_REMIND_MSG,
						Constants.SELECT_COMPLEX_MSG);
			} else {
				var args = new Object();
	            args.workN =  workN;
	            args.workType =  workType;
	            var object = window.showModalDialog('safeClearDetailList.jsp',
	                args, 'dialogWidth=780px;dialogHeight=540px;center=yes;help=no;resizable=no;status=no;');
			}
		}
	}
	
	//modify by fyyang 
	//页面初始化
	queryRecord();
});