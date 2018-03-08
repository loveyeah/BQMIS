	
Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.QuickTips.init();
Ext.onReady(function() {
	
		// 检查时间输入
	function checkDate() {
		var strStartDate = Ext.get("startDate").dom.value;
		var strEndDate = Ext.get("endDate").dom.value;
		if ((strStartDate.length == 10 || strStartDate.length == 0)
				&& (strEndDate.length == 10 || strEndDate.length == 0)) {
			if ((strStartDate.length == 0) && (strEndDate.length == 0)) {
				startDate.setValue("");
				endDate.setValue("");
				return true;
			} else if (strStartDate.length == 0) {
				startDate.setValue("");
				if ((Date.parseDate(strStartDate, 'Y-m-d')) != "undefined") {
					return true;
				} else {
					Ext.Msg.alert(Constants.NOTICE, "日期格式不正确！");
					return false;
				}
			} else if (strEndDate.length == 0) {
				endDate.setValue("");
				if ((Date.parseDate(strEndDate, 'Y-m-d')) != "undefined") {
					return true;
				} else {
					Ext.Msg.alert(Constants.NOTICE, "日期格式不正确！");
					return false;
				}
			} else {
				var dateStart = Date.parseDate(strStartDate, 'Y-m-d');
				var dateEnd = Date.parseDate(strEndDate, 'Y-m-d');
				if (dateStart != "undefined" && dateEnd != "undefined") {
					if (dateStart.getTime() > dateEnd.getTime()) {
						Ext.Msg.alert(Constants.NOTICE, "开始时间必须小于结束时间！");
						return false;
					} else {
						return true;
					}
				} else {
					Ext.Msg.alert(Constants.NOTICE, "日期格式不正确！");
					return false;
				}
			}
		} else {
			Ext.Msg.alert(Constants.NOTICE, "日期格式不正确！");
			return false;
		}
	}
	// 系统当天日期
	var sd = new Date();
	var ed = new Date();
	// 系统当天前30天的日期
	sd.setDate(sd.getDate() - 30);
	// 系统当天后30天的日期
	ed.setDate(ed.getDate());

	// 开始时间选择
	var startDate = new Ext.form.DateField({
		id : 'startDate',
		width : 100,
		allowBlank : true,
		readOnly : false,
		value : sd,
		format : 'Y-m-d'
	})
	startDate.setValue(sd);
	startDate.on('change', checkDate);

	// 结束时间选择
	var endDate = new Ext.form.DateField({
		id : 'endDate',
		width : 100,
		allowBlank : true,
		readOnly : false,
		value : ed,
		format : 'Y-m-d'
	})
	endDate.setValue(ed);
	endDate.on('change', checkDate);
	
		var btnSelect = new Ext.Button({
		id : "btnSelect",
		text : "...",
		name : "first",
		handler : function() {
			var args = {
				selectModel : 'single',
				notIn : "'999999'",
				rootNode : {
					id : '-1',
					text : '安庆电厂'
				}
			};
			var obj = window
					.showModalDialog(
							'../../../comm/jsp/hr/workerByDept/workerByDept2.jsp',
							args,
							'dialogWidth=600px;dialogHeight=420px;center=yes;help=no;resizable=no;status=no;');
			if (typeof(obj) == 'object') {
				Ext.get("order.supervisor").dom.value = obj.workerCode;
				Ext.get("supervisorName").dom.value = obj.workerName;
			}
		}
	});
	
		var content = new Ext.form.FieldSet({
		title : '检修台帐编辑',
		height : '100%',
		layout : 'form',
		buttonAlign : 'center',
		items : [{
			id : 'order.id',
			name : 'order.id',
			xtype : 'hidden',
			readOnly : true,
			allowBlank : true,
			anchor : '85%'
		}, {
			id : 'order.failureCode',
			name : 'order.failureCode',
			xtype : 'hidden',
			readOnly : true,
			allowBlank : true,
			anchor : '85%'
		}, {
			id : 'txtequcode',
			name : 'order.attributeCode',
			xtype : 'textfield',
			fieldLabel : '系统/设备',
			readOnly : true,
			allowBlank : true,
			anchor : '85%'
		}, {
			id : 'txtequname',
			name : 'order.equName',
			xtype : 'textfield',
			fieldLabel : '系统/设备名称',
			readOnly : true,
			allowBlank : true,
			anchor : '85%'
		}, {
			id : 'order.checkAttr',
			name : 'order.checkAttr',
			xtype : 'textfield',
			fieldLabel : '检修性质',
			readOnly : false,
			allowBlank : false,
			anchor : '85%'
		}, {
			id : 'order.preContent',
			name : 'order.preContent',
			xtype : 'textarea',
			fieldLabel : '修前情况',
			height : '40',
			readOnly : false,
			allowBlank : false,
			anchor : '84.35%'
		}, {
			id : 'order.description',
			name : 'order.description',
			xtype : 'textarea',
			fieldLabel : '检修情况',
			height : '40',
			readOnly : false,
			allowBlank : false,
			anchor : '84.35%'
		}, {
			id : 'order.parameters',
			name : 'order.parameters',
			xtype : 'textarea',
			fieldLabel : '修后技术参数',
			height : '40',
			readOnly : false,
			allowBlank : true,
			anchor : '84.35%'
		}, {
			id : 'order.problem',
			name : 'order.problem',
			xtype : 'textarea',
			fieldLabel : '存在问题',
			height : '40',
			readOnly : false,
			allowBlank : true,
			anchor : '84.35%'
		}, {
			id : 'order.spareParts',
			name : 'order.spareParts',
			xtype : 'textarea',
			fieldLabel : '更换的备品备件',
			height : '40',
			readOnly : false,
			allowBlank : true,
			anchor : '84.35%'
		}, {
			layout : 'column',
			border : false,
			anchor : '90%',
			items : [{
				layout : 'form',
				labelWidth : 100,
				columnWidth : .5,
				border : false,
				items : [{
					xtype : 'datefield',
					format : 'Y-m-d',
					fieldLabel : '实际开始时间',
					name : 'order.startDate',
					id : 'startDate',
					itemCls : 'sex-left',
					clearCls : 'allow-float',
					readOnly : true,
					checked : true,
					anchor : '90%',
					allowBlank : false
				}]
			}, {
				layout : 'form',
				labelWidth : 100,
				columnWidth : .5,
				border : false,
				items : [{
					xtype : 'datefield',
					format : 'Y-m-d',
					fieldLabel : '实际结束时间',
					name : 'order.endDate',
					id : 'endDate',
					itemCls : 'sex-left',
					clearCls : 'allow-float',
					readOnly : true,
					checked : true,
					anchor : '90%',
					allowBlank : false
				}]
			}]
		}, {
			layout : 'column',
			border : false,
			anchor : '90%',
			items : [{
				layout : 'form',
				labelWidth : 100,
				columnWidth : .6,
				border : false,
				items : [{
					id : 'supervisorName',
					name : 'supervisorName',
					xtype : 'textfield',
					fieldLabel : '负责人',
					readOnly : true,
					allowBlank : false,
					anchor : '95%'
				}]
			}, {
				layout : 'form',
				labelWidth : 100,
				columnWidth : .2,
				border : false,
				items : [btnSelect]
			}]
		}, {
			id : 'order.supervisor',
			name : 'order.supervisor',
			xtype : 'hidden',
			value : '999999',
			fieldLabel : '负责人',
			readOnly : false,
			allowBlank : true,
			anchor : '85%'
		}, {
			id : 'order.participants',
			name : 'order.participants',
			xtype : 'textarea',
			fieldLabel : '参加人',
			height : '40',
			readOnly : false,
			allowBlank : true,
			anchor : '84.35%'
		}],
		buttons : [{
			text : '保存',
			iconCls : 'save',
			handler : function() {
				if (!form.getForm().isValid()) {
					return false;
				}
				var url = "";
				if (Ext.get('order.id').dom.value == "") {
					url = 'equstandard/addMaintenance.action';
				} else {
					url = 'equstandard/updateMaintenance.action';
				}
				form.getForm().submit({
					url : url,
					method : 'post',
//					params : {
//						filePath : Ext.get("annex").dom.value
//					},
					success : function(form, action) {
						var message = eval('(' + action.response.responseText
								+ ')');
						Ext.Msg.alert("成功", message.Msg);
						querydata();
						win.hide();
					},
					failure : function() {
						Ext.Msg.alert('错误', '操作失败！.');
					}
				})
			}
		}, {
			text : '取消',
			iconCls : 'cancer',
			handler : function() {
				win.hide();
			}
		}]
	});
		var form = new Ext.form.FormPanel({
		bodyStyle : "padding:5px 5px 0",
		labelAlign : 'right',
		labelWidth : 100,
		autoHeight : true,
		region : 'center',
		border : false,
		items : [content]
	});

	var win = new Ext.Window({
		// title : '检修台帐编辑',
		autoHeight : true,
		modal : true,
		width : 600,
		closeAction : 'hide',
		items : [form]
	})
	// -----------设备维修列表--------------------
	var record = Ext.data.Record.create([{
		name : 'order.id'
	}, {
		name : 'order.failureCode'
	}, {
		name : 'order.attributeCode'
	}, {
		name : 'order.equName'
	}, {
		name : 'order.checkAttr'
	}, {
		name : 'order.preContent'
	}, {
		name : 'order.description'
	}, {
		name : 'order.parameters'
	}, {
		name : 'order.problem'
	}, {
		name : 'order.spareParts'
	}, {
		name : 'startDate'
	}, {
		name : 'endDate'
	}, {
		name : 'order.supervisor'
	}, {
		name : 'supervisorName'
	}, {
		name : 'order.participants'
	},{name:'order.workFlowNo'},
	{name:'order.status'},
	{name:'reportDate'}]);

	var xproxy = new Ext.data.HttpProxy({
		url : 'equstandard/findMaintenanceApproveList.action'
	});

	var xreader = new Ext.data.JsonReader({
		root : "list",
		totalProperty : "totalCount"
	}, record);

	var censtore = new Ext.data.Store({
		proxy : xproxy,
		reader : xreader
	});
	// 分页

	var equcode = new Ext.form.TextField({
		id : "equcode",
		name : "equcode",
		readOnly : true
	});
	var equname = new Ext.form.TextField({
		id : "equname",
		name : "equname",
		width : '400',
		readOnly : true
	});
	var cenGrid = new Ext.grid.GridPanel({
		store : censtore,
		autoHeght : true,
		id : 'cenGrid',
		loadMask : {
			msg : '读取数据中 ...'
		},
		columns : [new Ext.grid.RowNumberer(), {
			header : "ID",
			sortable : true,
			dataIndex : 'order.id',
			hidden : true
		}, {
			header : "修前情况",
			width : 100,
			dataIndex : 'order.preContent',
			sortable : true
		}, {
			header : "实际开始日期",
			width : 120,
			dataIndex : 'startDate',
			sortable : true
		}, {
			header : "负责人",
			width : 100,
			dataIndex : 'supervisorName',
			sortable : true
		}, {
			header : "检修性质",
			width : 100,
			dataIndex : 'order.checkAttr',
			sortable : true
		}, {
			header : "检修情况",
			width : 100,
			dataIndex : 'order.description',
			sortable : true
		}, {
			header : "存在问题",
			width : 100,
			dataIndex : 'order.problem',
			sortable : true
		}, {
			header : "更换备品备件",
			width : 100,
			dataIndex : 'order.spareParts',
			sortable : true
		}, {
			header : "修后技术参数",
			width : 100,
			dataIndex : 'order.parameters',
			sortable : true
		},
		{
		  
		  header : "状态",
			width : 100,
			dataIndex : 'order.status',
			sortable : true,
			renderer:function(value)
			{
			  return	getStatusName(value);
			}
		},{
		   
			header : "上报时间",
			width : 100,
			dataIndex : 'reportDate',
			sortable : true
		}],
		tbar : new Ext.Toolbar({
			items : ['上报时间：',startDate,'~',endDate,'-',{
				text : '查询',
				iconCls : "reflesh",
				handler : querydata
			},'-', {
				text : "修改",
				iconCls : "update",
				handler : update
			}, '-', {
				text : "删除",
				iconCls : "delete",
				handler : del
			},
			'-', {
				text : "审批",
				iconCls : "approve",
				handler : edit
			}
			]
		}),
		bbar : new Ext.PagingToolbar({
			pageSize : 18,
			store : censtore,
			displayInfo : true,
			displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
			emptyMsg : "没有记录"
		})
	});
	function querydata() {
		
		censtore.baseParams = {
			startReportDate : Ext.get("startDate").dom.value,
			EndReportDate: Ext.get("endDate").dom.value
		};
		censtore.load({
			params : {
				start : 0,
				limit : 18
			}
		});
	}
	
	
	cenGrid.on('rowdblclick',edit);
	/**
	 * 双击grid事件
	 */
	function edit() {
		var record = cenGrid.getSelectionModel().getSelected();
		if(record==null)
		{
		 Ext.Msg.alert("提示","请选择要审批的记录");
		  return;
		}
		var workFlowNo = record.get('order.workFlowNo');
		Ext.Ajax.request({
			url : 'MAINTWorkflow.do?action=getCurrentStepsInfo',
			method : 'POST',
			params : {
				entryId : workFlowNo
			},
			success : function(result, request) {
				var obj = eval("(" + result.responseText + ")");
			    var url="";
				if (obj[0].url==null||obj[0].url=="") {
					url="approveSign.jsp";
				} else {
					 url = "../../../../" + obj[0].url; 
					
					
					
				}
				var args = new Object();
				args.busiNo = record.get('order.id');
					args.entryId = record.get("order.workFlowNo"); 
					args.flowCode="";
					
					args.title="点检长审批";
					window.showModalDialog(url, args,
							'status:no;dialogWidth=750px;dialogHeight=550px');
					querydata();
			},
			failure : function(result, request) {
				Ext.Msg.alert("提示","错误");
			}

		});
	}
	
	function update() {
		var selections = cenGrid.getSelections();
		if (selections.length > 0) {
			win.show();
			form.getForm().reset();
			var record = cenGrid.getSelectionModel().getSelected();
			form.getForm().loadRecord(record);
		} else {
			Ext.Msg.alert('提示', '请从列表中选择需要修改的记录！');
		}
	}
	function del() {
		var selections = cenGrid.getSelections();
		if (selections.length > 0) {
			var record = cenGrid.getSelectionModel().getSelected();
			Ext.Msg.confirm('提示', '删除的数据您将不能恢复,确定要删除吗?', function(b) {
				if (b == 'yes') {
					Ext.Ajax.request({
						url : "MAINTWorkflow.do?action=mangerDelete",
						method : 'post',
						params : {
							busiType : 'equRepair',
							entryId : record.get("order.workFlowNo"),
							busiNo : record.get('order.id')
						},
						success : function(result, request) {
							Ext.Msg.alert("提示", "删除成功!");
							querydata();
							
						},
						failure : function() {
							Ext.Msg.alert("提示", "操作失败!");
						}
					});
				}
			})
		} else {
			Ext.Msg.alert('提示', '请从列表中选择需要删除的记录！');
		}
		
		
	}
	
	
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "fit",
		items : [cenGrid]
	});
	
	function getStatusName(status)
	{
	  	if(status=="1") return "已上报";
	  	else if(status=="2") return "已审批结束";
	  	else if(status=="3") return "已退回";
	  	else return "未上报";
	}
	
	 querydata();
	
	});
