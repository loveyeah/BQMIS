Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	var strt = 0;
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
	}, {
		name : 'model.workticketStausId'
	}, {
		name : 'model.entryBy'
	}, {
		name : 'repairSpecailName'
	}]);

	// 定义获取数据源
	var dataProxy = new Ext.data.HttpProxy({
		url : 'workticket/getStandardApproveList.action'
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
		url : "workticket/getQueryWorkticketType.action",
		fields : ['workticketTypeCode', 'workticketTypeName']
	});
	typeDate.on('load', function(e, records) {
		typeComboBox.setValue(records[0].data.workticketTypeCode);
	});
	typeDate.load();

	// 定义机组或系统数据
	var systemDate = new Ext.data.JsonStore({
		root : 'list',
		url : "workticket/getQueryEquBlock.action",
		fields : ['blockCode', 'blockName']
	});
	systemDate.on('load', function(e, records) {
		systemComboBox.setValue(records[0].data.blockCode);
	});
	systemDate.load();

	// 定义状态数据
	var stateDate = new Ext.data.JsonStore({
		root : 'list',
		url : "workticket/getStandardTypeForApprove.action",
		fields : ['workticketStausId', 'workticketStatusName']
	});

	stateDate.on('load', function(e, records) {
		stateComboBox.setValue(records[0].data.workticketStausId);
	});
	stateDate.load();

	// 定义类型
	var typeComboBox = new Ext.form.ComboBox({
		id : "typeCob",
		store : typeDate,
		displayField : "workticketTypeName",
		valueField : "workticketTypeCode",
		mode : 'local',
		triggerAction : 'all',
		width : 130,
		readOnly : true
	});
	// typeComboBox.on("change",function(){alert();});

	// 定义所属机组或系统
	var systemComboBox = new Ext.form.ComboBox({
		id : "systemCob",
		store : systemDate,
		displayField : "blockName",
		valueField : "blockCode",
		mode : 'local',
		width : 100,
		triggerAction : 'all',
		readOnly : true
	});

	// 定义状态
	var stateComboBox = new Ext.form.ComboBox({
		id : "stateCob",
		store : stateDate,
		displayField : "workticketStatusName",
		valueField : "workticketStausId",
		mode : 'local',
		width : 100,
		triggerAction : 'all',
		readOnly : true
	});
	// 检修专业
	var storeRepairSpecail = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'workticket/getDetailRepairSpecialityType.action'
		}),
		reader : new Ext.data.JsonReader({
			root : 'list'
		}, [{
			name : 'specialityCode',
			mapping : 'specialityCode'
		}, {
			name : 'specialityName',
			mapping : 'specialityName'
		}])
	});
	storeRepairSpecail.load();
	var cbxRepairSpecail = new Ext.form.ComboBox({
		id : 'repairSpecailCode',
		fieldLabel : "检修专业:",
		store : storeRepairSpecail,
		displayField : "specialityName",
		valueField : "specialityCode",
		hiddenName : 'repairCode',
		mode : 'local',
		triggerAction : 'all',
		value : '',
		readOnly : true,
		width : 80
	});

	storeRepairSpecail.on('load', function(e, records) {
		var myrecord = new Ext.data.Record({
			specialityName : '所有',
			specialityCode : ''
		});
		storeRepairSpecail.insert(0, myrecord);
		cbxRepairSpecail.setValue('');
	});

	// 定义选择列
	var sm = new Ext.grid.CheckboxSelectionModel({
		//singleSelect : true,
		header : '选择',
		width : 35,
		id : 'sm'
	});
	// ----add by fyyang - 090318----

	var expander = new Ext.grid.RowExpander({
		tpl : new Ext.Template(
				// '<p><b>工作票号:</b> </p><br>',
				'<p><b>工作内容：</b></p>'),
		expandRow : mygridExpend
	});
	function mygridExpend(row) {
		if (typeof row == 'number') {
			row = this.grid.view.getRow(row);
		}
		var record = this.grid.store.getAt(row.rowIndex);
		var no = record.get("model.workticketNo");
		var content = "";
		// -----------------------
		Ext.Ajax.request({
			url : 'workticket/getContentWorkticketByNo.action',
			params : {
				workticketNo : no
			},
			method : 'post',
			success : function(result, request) {
				var content = result.responseText;
				content = content.substring(1, content.length - 1);
				while (content.indexOf('\\r') != -1) {

					content = content.replace("\\r", "");
				}
				while (content.indexOf('\\n') != -1) {
					content = content.replace("\\n", "<br>");
				}
				expander.tpl = new Ext.Template('<p> <br><font color="blue"><b>工作内容：</b><br>'
								+ content + '</font></p>');
				var body = Ext.DomQuery.selectNode(
						'tr:nth(2) div.x-grid3-row-body', row);
				if (expander.beforeExpand(record, body, row.rowIndex)) {
					expander.state[record.id] = true;
					Ext.fly(row).replaceClass('x-grid3-row-collapsed',
							'x-grid3-row-expanded');
					expander.fireEvent('expand', expander, record, body,
							row.rowIndex);
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
		region : "center",
		layout : 'fit',
		store : queryStore,
		viewConfig : {
			forceFit : true
		},
		columns : [sm, {
			header : "工作编号",
			width : 150,
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
			width : 80,
			sortable : true,
			dataIndex : 'statusName'
		}, {
			header : "来源",
			width : 100,
			sortable : true,
			dataIndex : 'sourceName'
		}, {
			header : "工作负责人",
			width : 70,
			sortable : false,
			dataIndex : 'chargeByName',
			hidden : true
		}, {
			header : "所属部门",
			width : 75,
			sortable : true,
			dataIndex : 'deptName',
			hidden : true
		}, {
			header : "计划开始时间",
			width : 110,
			sortable : true,
			dataIndex : 'planStartDate',
			hidden : true
		}, {
			header : "计划结束时间",
			width : 110,
			sortable : true,
			dataIndex : 'planEndDate',
			hidden : true
		}, {
			header : "所属系统",
			width : 100,
			sortable : true,
			dataIndex : 'blockName'
		}, {
			header : "工作内容",
			width : 360,
			hidden : true,
			sortable : true,
			dataIndex : 'model.workticketContent',
			renderer : function(value) {
				if (value) {
					return value.replace(/\n/g, '<br/>');
				}
				return '';
			}
		}, {
			header : "是否紧急票",
			width : 70,
			sortable : true,
			dataIndex : 'isEmergencyText',
			hidden : true
		}, {
			header : "检修专业",
			width : 70,
			sortable : true,
			dataIndex : 'repairSpecailName'
		}, {
			header : "填写人",
			width : 70,
			sortable : true,
			dataIndex : 'model.entryBy'
		}],
		sm : sm,
		// 头部工具栏
		tbar : ['类型:', typeComboBox, '所属机组或系统:', systemComboBox, '状态:',
				stateComboBox, '检修专业：', cbxRepairSpecail, {
					text : Constants.BTN_QUERY,
					iconCls : Constants.CLS_QUERY,
					handler : queryRecord
				},
				{
					text:'批量审批',
					iconCls : 'write',
					handler:multiApprove
				}
				],
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
		queryStore.baseParams = {
			startD : "",
			endD : "",
			typeC : typeComboBox.value,
			systemC : systemComboBox.value,
			stateC : stateComboBox.value,
			repairC : cbxRepairSpecail.getValue()
		};
		queryStore.load({
			params : {
				start : strt,
				limit : Constants.PAGE_SIZE
			}
		});
		strt = 0
	}

	grid.on("load", queryRecord());

	/**
	 * 双击grid事件
	 */
	function edit() {
		var record = grid.getSelectionModel().getSelected();
		var workticketNo = record.get('model.workticketNo');
		Ext.Ajax.request({
			url : 'workticket/getStandardApproveUrl.action',
			method : 'POST',
			params : {
				workticketNo : workticketNo
			},
			success : function(result, request) {
				var obj = eval("(" + result.responseText + ")");
				if (obj.flag == "0") {
					Ext.Msg.alert("提示", obj.msg);
				} else {
					var url = "../../../../../" + obj.url;
					var args = new Object();
					args.workticketNo = record.get("model.workticketNo");
					args.workFlowNo = record.get("model.workFlowNo");
					args.workticketTypeCode = record
							.get("model.workticketTypeCode");
					args.firelevelId = obj.firelevelId;
					if (record.get("model.workticketStausId") == 2) {
						args.title = "两票审核小组审批";
					}
					if (record.get("model.workticketStausId") == 6) {
						args.title = "安环部审批";
					}

					window.showModalDialog(url, args, 'status:no;dialogWidth='
							+ window.screen.width + ';dialogHeight='
							+ window.screen.height + ';resizable:yes;');
					strt = grid.getBottomToolbar().cursor;
					queryRecord();
				}
			},
			failure : function(result, request) {
				Ext.Msg.alert(Constants.UNKNOWN_ERR);
			}

		});
	}
	
	function multiApprove()
	{
		var sm = grid.getSelectionModel();
		var selected = sm.getSelections();
		var nos="";
		var no="";
		var workFlowNo="";
	
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要审批的记录！");
		} else {
			 var status=selected[0].get("model.workticketStausId");
                      
				for (var i = 0; i < selected.length; i++) {
					
					if(nos=="") nos=selected[i].get('model.workticketNo');
					else nos =nos+ ","+selected[i].get('model.workticketNo');
					no = selected[i].get('model.workticketNo');
					workFlowNo=selected[i].get('model.workFlowNo');
					if(selected[i].get("model.workticketStausId")!=status)
					{
						Ext.Msg.alert("提示", "请选择同一状态的标准工作票！");
						return ;
					}
					
				}
		         var url="../multiApproveSign/multiSign.jsp";
		         var args=new Object();
		        
		         args.workticketNo = no;
				 args.workFlowNo = workFlowNo;
		         args.nos=nos;
                  window.showModalDialog(url, args, 'status:no;dialogWidth='
							+ window.screen.width + ';dialogHeight='
							+ window.screen.height + ';resizable:yes;');
					strt = grid.getBottomToolbar().cursor;
					queryRecord();
	
		}
	}
});