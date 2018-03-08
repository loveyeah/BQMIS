Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {

	// 分页时每页显示记录条数
	var PAGE_SIZE = Constants.PAGE_SIZE;

	// 页面跳转时使用
	var register = parent.Ext.getCmp('tabPanel').register;
	// 数据变更时更新Grid中的数据
	register.workticketChangeHandler = gridReload;
	// ↓↓********** 主画面*******↓↓//

	// 工作票类型检索

	var storeCbx = new Ext.data.JsonStore({
		root : 'list',
		url : "workticket/getWorkticketTypeForReport.action",
		fields : ['workticketTypeCode', 'workticketTypeName']
	})
	storeCbx.load();
	storeCbx.on('load', function(e, records, o) {
		worktiketTypeCbo.setValue(records[0].data.workticketTypeCode);
	});

	// 工作票类型组合框
	var worktiketTypeCbo = new Ext.form.ComboBox({
		id : "worktiketTypeCbo",
		name : 'worktiketTypeCbo',
		allowBlank : true,
		triggerAction : 'all',
		store : storeCbx,
		width : 130,
		displayField : "workticketTypeName",
		valueField : "workticketTypeCode",
		mode : 'local',
		readOnly : true
	});

	// 工作票状态下拉框数据源
	//modify by fyyang 081230
	var workticketStatusStore = new Ext.data.JsonStore({
		//url : 'workticket/getDetailWorkticketStatusRef.action',
		url : 'workticket/getStandardTypeForReport.action',
		root : 'list',
		fields : [{
			name : 'workticketStatusName'
		}, {
			name : 'workticketStausId'
		}]
	})
	workticketStatusStore.load();
	workticketStatusStore.on('load', function(e, records, o) {
		workticketStatusCbo.setValue(records[0].data.workticketStausId);
	});

	// 工作票状态下拉框
	var workticketStatusCbo = new Ext.form.ComboBox({
		allowBlank : true,
		triggerAction : 'all',
		store : workticketStatusStore,
		displayField : 'workticketStatusName',
		valueField : 'workticketStausId',
		mode : 'local',
		width : 100,
		allowBlank : true,
		width : 100,
		readOnly : true
	})

	// 所属机组或系统检索
	var equBlockStore = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'workticket/getEquBlock.action'
		}),
		reader : new Ext.data.JsonReader({
			id : "name"
		}, [{
			name : 'blockCode'
		}, {
			name : 'blockName'
		}])
	});
	equBlockStore.load();
	equBlockStore.on('load', function(e, records, o) {
		equBlock.setValue(records[0].data.blockCode);
	});

	// 所属机组或系统组合框
	var equBlock = new Ext.form.ComboBox({
		store : equBlockStore,
		valueField : "blockCode",
		displayField : "blockName",
		width : 160,
		mode : 'local',
		triggerAction : 'all',
		hiddenName : 'permissionDept',
		selectOnFocus : true,
		allowBlank : true,
		readOnly : true
	});

	// head工具栏
	var headTbar = new Ext.Toolbar({
		region : 'north',
		items : ["工作票种类:", worktiketTypeCbo, "所属机组或系统:", equBlock, "状态:",
				workticketStatusCbo, {
					id : 'btnReflesh',
					text : "查询",
					iconCls : 'query',
					handler : searchBtn
				}]
	});

	// grid工具栏
	var gridTbar = new Ext.Toolbar({
		items : [{
			id : 'btnWrite',
			text : "新增",
			iconCls:'add',
			handler : writeBtn
		}, '-', {
			id : 'btnUpdate',
			text : "修改",
			iconCls:'update',
			handler : modifyBtn
		}, '-', {
			id : 'btnDelete',
			text : "删除",
			iconCls:'delete',
			handler : deleteBtn
		}, '-', {
			id : 'btnScan',
			text : "票面浏览",
			iconCls:'pdfview',
			handler : function() {
				workticketPrint();
			}
		}, '-', {
			id : 'btnReport',
			text : "上报",
			iconCls:'upcommit',
			handler : reportBtn
		}, '-', {
			id : 'btnLookfor',
			text : "查看流程",
			iconCls:'view',
			handler : function() {
				var sm = rungrid.getSelectionModel();
				var selected = sm.getSelections();
				var workticketNo;
				var workticketCode;
				if (selected.length == 0) {
					Ext.Msg.alert(Constants.SYS_REMIND_MSG,
							"请选择要浏览的记录");
				} else {
					var menber = selected[0];
					workticketNo = menber.get('model.workticketNo');

					workticketCode = menber.get('model.workticketTypeCode');
					var entryId = menber.get('model.workFlowNo');
					var url = "";
					if (entryId == null || entryId == "") {
						url = "/power/workflow/manager/flowshow/flowshow.jsp?flowCode=bqStandWorkticket";
					} else {
						url = "/power/workflow/manager/show/show.jsp?entryId="
								+ entryId;
					}

					window.open(url);
				}
			}
		}]
	});

	// 选择列
	var smCSM = new Ext.grid.CheckboxSelectionModel({
		header : "选择",
		id : "check",
		width : 35,
		singleSelect : true
	});

	// grid中的数据bean
	var rungridlist = new Ext.data.Record.create([{
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
		name : 'model.workticketTypeCode'
	}, {
		name : 'model.workticketStausId'
	}, {
		name : 'model.workFlowNo'
	}, {
		name : 'model.firelevelId'
	}]);

	// grid的store
	var queryStore = new Ext.data.JsonStore({
		url : 'workticket/getWorkticketApproveList.action',
		root : 'list',
		totalProperty : 'totalCount',
		fields : rungridlist
	});

	// 分页工具栏
	var pagebar = new Ext.PagingToolbar({
		pageSize : PAGE_SIZE,
		store : queryStore,
		displayInfo : true,
		displayMsg : Constants.DISPLAY_MSG,
		emptyMsg : Constants.EMPTY_MSG
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
	// 页面的Grid主体
	var rungrid = new Ext.grid.GridPanel({
		region : 'center',
		store : queryStore,
	    viewConfig : {
		forceFit : true 
		},
		columns : [smCSM, {
			header : "工作票编号",
			width : 140,
			sortable : true,
			dataIndex : 'model.workticketNo'
		}, {
			header : "工作内容",
			width : 300,
			sortable : true,
			//hidden:true,
			dataIndex : 'model.workticketContent',
			renderer:function(value, metadata, record){ 
			metadata.attr = 'style="white-space:normal;"'; 
			return value;  
	}
	}, {
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
			sortable : true,
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
			width : 115,
			sortable : true,
			dataIndex : 'planStartDate',
			hidden : true
		}, {
			header : "计划结束时间",
			width : 115,
			sortable : true,
			dataIndex : 'planEndDate',
			hidden : true
		}, {
			header : "所属系统",
			width : 100,
			sortable : true,
			dataIndex : 'blockName'
		}, {
			header : "是否紧急票",
			width : 70,
			sortable : true,
			dataIndex : 'isEmergencyText',
			hidden : true
		}, {
			header : "工作票类型",
			width : 5,
			align : "center",
			sortable : true,
			hidden : true,
			dataIndex : 'model.workticketTypeCode'
		}, {
			header : "工作票状态ID",
			sortable : true,
			hidden : true,
			dataIndex : 'model.workticketStausId'
		}],
		tbar : gridTbar,
		bbar : pagebar,
		sm : smCSM,
		frame : false,
		border : false,
		autoWidth : true,
		enableColumnHide : true,
		enableColumnMove : false
		
	});
	// 双击事件
	rungrid.on('rowdblclick', modifyBtn);

	// 主框架
	var viewport = new Ext.Viewport({
		layout : 'border',
		items : [headTbar, rungrid],
		defaults : {
			autoScroll : true
		}
	});

	var register = parent.Ext.getCmp('tabPanel').register;
	// ↑↑********** 主画面*******↑↑//

	// ↓↓*********处理***********↓↓//

	// 查询处理
	function searchBtn() {

		gridFresh();
	}

	// 删除处理
	function deleteBtn() {
		var sm = rungrid.getSelectionModel();
		var selected = sm.getSelections();
		var workticketNo;
		var isReport;
		if (selected.length == 0) {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG,
					Constants.SELECT_NULL_DEL_MSG);
		} else {
			var menber = selected[0];
			workticketNo = menber.get('model.workticketNo');
			isReport = menber.get('model.workticketStausId');
			if (isReport != Constants.WORKTICKET_STAUS_ID_NotReport && isReport != "5") {
				Ext.Msg.alert(Constants.SYS_REMIND_MSG, "该记录已上报，不能删除");
			} else {
				// 弹出确认删除的信息
				Ext.Msg.confirm(Constants.BTN_DELETE,
						funcDelMsgById(workticketNo), function(buttonobj) {
							if (buttonobj == "yes") {
								Ext.lib.Ajax.request('POST',
										'workticket/deleteWorkticketNo.action',
										{
											success : function(action) {
												Ext.Msg
														.alert(
																Constants.SYS_REMIND_MSG,
																Constants.DEL_SUCCESS);
												gridFresh();
												// 删除工作票
												register
														.deleteWorkticket(workticketNo);
											},
											failure : function() {
												Ext.Msg.alert(Constants.ERROR,
														Constants.UNKNOWN_ERR);
											}
										}, 'workticketNo=' + workticketNo);
							}
						});
			}
		}
	}

	// 上报处理
	function reportBtn() {
		var sm = rungrid.getSelectionModel();
		var selected = sm.getSelections();
		var workticketNo;
		var isReport;
		if (selected.length == 0) {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG,
					Constants.SELECT_NULL_REPORT_MSG);
		} else {
			var menber = selected[0];
			workticketNo = menber.get('model.workticketNo');
			isReport = menber.get('model.workticketStausId');
			if (isReport != "1" && isReport != "5") {
				Ext.Msg.alert(Constants.SYS_REMIND_MSG, "只有未上报和已退回的票允许上报");
			} else {

				var args = new Object();
				args.workticketNo = workticketNo;
				args.entryId = menber.get('model.workFlowNo');
				var check = window
						.showModalDialog(
								'../reportSign/reportSign.jsp',
								args,
								'dialogWidth=700px;dialogHeight=500px;center=yes;help=no;resizable=no;status=no;');
				 if(check)
                {
               gridFresh();
                register.add();
				 var tabPanel = parent.Ext.getCmp('tabPanel');
				tabPanel.setActiveTab('tabReport');
                }
			}
		}
	}

	// 填写处理
	function writeBtn() {
		register.add();
	}

	// 修改处理
	function modifyBtn() {
		var sm = rungrid.getSelectionModel();
		var selected = sm.getSelections();
		var workticketNo;
		if (selected.length == 0) {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG,
					Constants.SELECT_NULL_UPDATE_MSG);
		} else {
			var menber = selected[0];
			workticketNo = menber.get('model.workticketNo');
			register.workFlowNo = menber.get('model.workFlowNo');
			register.firelevelId=menber.get('model.firelevelId');
			// 编辑工作票信息
			register.edit(workticketNo);
		}
	}

	// 刷新Grid中的数据
	function gridFresh() {
		queryStore.baseParams = {
			sdate : "",
			edate : "",
			workticketTypeCode : worktiketTypeCbo.value,
			equAttributeCode : equBlock.value,
			workticketStausId : workticketStatusCbo.value,
			isStandard : 'Y'

		};

		queryStore.load({
			params : {
				start : 0,
				limit : PAGE_SIZE
			}
		});
	}

	// 重新加载Grid中的数据
	function gridReload() {
		queryStore.reload();
	}

	// 票面浏览
	function workticketPrint() {
		var sm = rungrid.getSelectionModel();
		var selected = sm.getSelections();
		var workticketNo;
		var workticketCode;
		if (selected.length == 0) {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG,
					Constants.SELECT_NULL_REPORT_MSG);
		} else {

			var menber = selected[0];
			workticketNo = menber.get('model.workticketNo');
			workticketCode = menber.get('model.workticketTypeCode');
            var fireLevelId= menber.get('model.firelevelId'); 
				var strReportAdds = getReportUrl(workticketCode,workticketNo,fireLevelId);
				
				if (strReportAdds !="") {
					window.open(strReportAdds);
				} else {
					Ext.Msg.alert('提示',"目前没有该种工作票票面预览");
				}
			
		}
	}

	// ↑↑*********处理***********↑↑//
	// 页面初始化
	gridFresh();
});