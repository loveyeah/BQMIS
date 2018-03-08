Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	function ChangeDateToString(DateIn) {
		var Year = 0;
		var Month = 0;
		var Day = 0;
		var CurrentDate = "";
		// 初始化时间
		Year = DateIn.getYear();
		Month = DateIn.getMonth() + 1;
		Day = DateIn.getDate();
		CurrentDate = Year + "-";
		if (Month >= 10) {
			CurrentDate = CurrentDate + Month + "-";
		} else {
			CurrentDate = CurrentDate + "0" + Month + "-";
		}
		if (Day >= 10) {
			CurrentDate = CurrentDate + Day;
		} else {
			CurrentDate = CurrentDate + "0" + Day;
		}
		return CurrentDate;
	}
	var enddate = new Date();
	var startdate = enddate.add(Date.MONTH, -1);
	startdate = startdate.getFirstDateOfMonth();
	var sdate = ChangeDateToString(startdate);
	var edate = ChangeDateToString(enddate);
	var fromDate = new Ext.form.DateField({
		format : 'Y-m-d',
		name : 'startDate',
		id : 'fromDate',
		itemCls : 'sex-left',
		clearCls : 'allow-float',
		checked : true,
		fieldLabel : "登记日期",
		allowBlank : false,
		readOnly : true,
		value : sdate,
		emptyText : '请选择',
		anchor : '100%'
	});
	var toDate = new Ext.form.DateField({
		format : 'Y-m-d',
		name : 'endDate',
		value : edate,
		id : 'toDate',
		itemCls : 'sex-left',
		clearCls : 'allow-float',
		fieldLabel : "至",
		allowBlank : false,
		readOnly : true,
		emptyText : '请选择',
		anchor : '80%'
	});
	var conttreesNo = new Ext.form.TextField({
		id : 'conttreesNo',
		xtype : "textfield",
		fieldLabel : "合同编号",
		name : 'conttreesNo',
		anchor : '100%'
	});
	var contractName = new Ext.form.TextField({
		id : 'contractName',
		xtype : "textfield",
		fieldLabel : "合同名称",
		name : 'contractName',
		anchor : '80%'
	});
	
	// 供应商
	var cliendId = new Ext.form.ComboBox({
		name : 'cliendId',
		id : 'cliendId',
		fieldLabel : '供应商',
		mode : 'remote',
		editable : false,
		anchor : '100%',
		triggerAction : 'all',
		onTriggerClick : function() {
			var url = "../../../../manage/client/business/clientSelect/clientSelect.jsp?approveFlag=2";
			var client = window
					.showModalDialog(
							url,
							null,
							'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');

			if (typeof(client) != "undefined") {
				cliendId.setValue(client.clientName);
				hidcliendId.setValue(client.cliendId);
			}
		}
	});
	var hidcliendId = new Ext.form.Hidden({
				id : "hidcliendBox",
				name : "con.cliendId"
			})
	var operateBox = new Ext.form.ComboBox({
		fieldLabel : '经办人',
		//store : [['999999', '测试员'], ['000000', 'xx'],[null, '全部']],
		id : 'operateName',
		//name : 'operateBy',
		//valueField : "value",
		//displayField : "text",
		mode : 'local',
		//typeAhead : true,
		forceSelection : true,
		hiddenName : 'operateByxx',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		emptyText : '请选择',
		anchor : '80%',
		onTriggerClick : function(e) {
									var url = "../../../../comm/jsp/hr/workerByDept/workerByDept2.jsp";
									var args = {
										selectModel : 'single',
										notIn : "",
										rootNode : {
											id : '-1',
											text : '灞桥电厂'
										}
									}
									var o = window
											.showModalDialog(url, args,
													'dialogWidth=650px;dialogHeight=500px;status=no');
									if (typeof(o) == "object") {
										operateBox.setValue(o.workerName);
										hidoperateBox.setValue(o.workerCode);
									}
			}
	});
	var hidoperateBox = new Ext.form.Hidden({
		id : 'hidoperateBox',
		name : 'operateByxx'
	})
	var statusBox = new Ext.form.ComboBox({
		fieldLabel : '签字状态',
		store : [['0', '未上报'], ['1', '待会签'], ['2', '已会签'], ['3', '退回'],
				[null, '全部']],
		id : 'workflowStatusName',
		name : 'workflowStatus',
		valueField : "value",
		displayField : "text",
		mode : 'local',
		typeAhead : true,
		forceSelection : true,
		hiddenName : 'workflowStatus',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		emptyText : '请选择',
		anchor : '100%'
	});

	var btnQuery = new Ext.Button({
		id : 'btnQuery',
		text : '查  询',
		minWidth : 70,
		handler : function() {
			var ftime = Ext.get('fromDate').dom.value;
			var ttime = Ext.get('toDate').dom.value;
			if (ftime > ttime) {
				Ext.Msg.alert('提示', '选择后一日期应比前一日期大！');
				return false;
			}
			con_ds.load({
				params : {
					startdate : Ext.get('fromDate').dom.value,
					enddate : Ext.get('toDate').dom.value,
					start : 0,
					limit : 18,
					conNo : Ext.get('conttreesNo').dom.value,
					conName : Ext.get('contractName').dom.value,
					clientName : hidcliendId.getValue(),
					operaterBy : hidoperateBox.getValue(),
					status : statusBox.getValue(),
					conTypeId : 1
				}
			});
		}
	});
	var btnClear = new Ext.Button({
		id : 'btnClear',
		text : '清除条件',
		minWidth : 70,
		handler : function() {
			form.getForm().reset();
			hidcliendId.setValue();
		}
	});
	var btnInfo = new Ext.Button({
		id : 'btnInfo',
		text : '合同信息',
		minWidth : 70,
		handler : function() {
			var selrows = Grid.getSelectionModel().getSelections();
			if (selrows.length > 0) {
				var record = Grid.getSelectionModel().getSelected();
				var url;
				var conId = record.data.conId;
				var conModifyId = record.data.conModifyId;
				if ((conModifyId != null) && (conModifyId != "")) {
					url = "../../../../manage/contract/business/conBaseInfo/conInfo.jsp?conId="
							+ conId + "&conModifyId=" + conModifyId;
				} else {
					url = "../../../../manage/contract/business/conBaseInfo/conBaseInfo.jsp?id="
							+ conId;
				}
				var o = window.showModalDialog(url, '',
						'dialogWidth=800px;dialogHeight=800px;status=no');
			} else {
				Ext.Msg.alert('提示', '请从列表中选择一条记录!');
			}
		}
	});
	var btnSign = new Ext.Button({
		id : 'btnSign',
		text : '会签表 ',
		minWidth : 70,
		handler : function() {
		CheckRptPreview()
		}
	});
	
//});
		// 会签票面浏览
	function CheckRptPreview() {
		var selected = Grid.getSelectionModel().getSelections();
		var conId;
		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要浏览的记录！");
		} else {
			var menber = selected[0];
			conId = menber.get('conId');
			var url = "/powerrpt/report/webfile/bqmis/CGConMeetSign.jsp?conId="
					+ conId;
			window.open(url);
		}

	};
	var btnExport = new Ext.Button({
		id : 'btnExport',
		text : '导  出',
		minWidth : 70,
		handler : function() {
			myExport();
		}
	});
	var btnApprove = new Ext.Button({
		id : 'btnApprove',
		text : '审批查询',
		minWidth : 70,
		handler : function() {
			var selrows = Grid.getSelectionModel().getSelections();
				if (selrows.length > 0) {
					var entryId = selrows[0].data.workflowNo;
					if (entryId == null || entryId == "") {
						url = "/power/workflow/manager/flowshow/flowshow.jsp?flowCode="
									+ "bqCGContract";
							window.open(url);
					} else {
						var url = "/power/workflow/manager/show/show.jsp?entryId="
								+ entryId;
						window.open(url);
					}
				} else {
					Ext.Msg.alert('提示', '请从列表中选择一条记录！');
				}
			}
	});
	
	// 导出
	function tableToExcel(tableHTML) {
		window.clipboardData.setData("Text", tableHTML);
		try {
			var ExApp = new ActiveXObject("Excel.Application");
			var ExWBk = ExApp.workbooks.add();
			var ExWSh = ExWBk.worksheets(1);
			ExApp.DisplayAlerts = false;
			ExApp.visible = true;
		} catch (e) {
			if (e.number != -2146827859)
				alert("您的电脑没有安装Microsoft Excel软件！");
			return false;
		}
		ExWBk.worksheets(1).Paste;
	}
	function myExport() {
		Ext.Ajax.request({
			url : 'managecontract/getAllConList.action?conTypeId='+1,
			success : function(response) {
				var json = eval('(' + response.responseText.trim() + ')');
				var records = json.root;
				// alert(records.length);
				var html = ['<table border=1><tr><th>会签状态</th><th>合同编号</th><th>合同名称</th><th>供应商</th><th>有无总金额</th><th>总金额</th><th>币别</th><th>有无变更</th><th>经办人</th><th>经办部门</th><th>会签开始时间</th><th>会签结束时间</th></tr>'];
				for (var i = 0; i < records.length; i += 1) {
					var rc = records[i];
					html.push('<tr><td>' + rc.workflowSta + '</td><td>'
							+ rc.conttreesNo + '</td><td>' + rc.contractName
							+ '</td><td>' + rc.clientName + '</td><td>'
							+ rc.isSum + '</td><td>' + rc.actAmount
							+ '</td><td>' + rc.currencyName + '</td><td>'
							+ rc.conModify + '</td><td>' + rc.operateName
							+ '</td><td>' + rc.operateDeptName + '</td><td>'
							+ rc.signStartDate + '</td><td>' + rc.signEndDate
							+ '</td></tr>');
				}
				html.push('</table>');
				html = html.join(''); // 最后生成的HTML表格
				// alert(html);
				tableToExcel(html);
			},
			failure : function(response) {
				Ext.Msg.alert('信息', '失败');
			}
		});
	}
	var content = new Ext.form.FieldSet({
		title : '查询条件',
		height : '100%',
		collapsible : true,
		layout : 'form',
		items : [{
			layout : 'column',
			border : false,
			items : [{
				border : false,
				columnWidth : 0.3,
				layout : 'form',
				items : [fromDate]
			}, {
				border : false,
				columnWidth : 0.4,
				layout : 'form',
				items : [toDate]
			}, {
				border : false,
				columnWidth : 0.15,
				align : 'center',
				layout : 'form',
				items : [btnQuery]
			}, {
				border : false,
				columnWidth : 0.15,
				align : 'center',
				layout : 'form',
				items : [btnClear]
			}]
		}, {
			layout : 'column',
			border : false,
			items : [{
				border : false,
				columnWidth : 0.3,
				layout : 'form',
				items : [conttreesNo]
			}, {
				border : false,
				columnWidth : 0.4,
				layout : 'form',
				items : [contractName]
			}, {
				border : false,
				columnWidth : 0.15,
				layout : 'form',
				items : [btnInfo]
			}, {
				border : false,
				columnWidth : 0.15,
				layout : 'form',
				items : [btnSign]
			}]
		}, {
			layout : 'column',
			border : false,
			items : [{
				border : false,
				columnWidth : 0.3,
				layout : 'form',
				items : [cliendId,hidcliendId]
			}, {
				border : false,
				columnWidth : 0.4,
				layout : 'form',
				items : [operateBox,hidoperateBox]
			}, {
				border : false,
				columnWidth : 0.15,
				layout : 'form',
				items : [btnExport]
			}, {
				border : false,
				columnWidth : 0.15,
				layout : 'form',
				items : [btnApprove]
			}]
		}, {
			layout : 'column',
			border : false,
			items : [{
				border : false,
				columnWidth : 0.3,
				layout : 'form',
				items : [statusBox]
			}]
		}]
	});
	var form = new Ext.form.FormPanel({
		bodyStyle : "padding:5px 5px 0",
		labelAlign : 'right',
		id : 'shift-form',
		labelWidth : 80,
		autoHeight : true,
		region : 'center',
		border : false,
		items : [content]
	});


	/*-----------------------------------------------------------------*/
	var con_item = Ext.data.Record.create([{
		name : 'conId'
	}, {
		name : 'workflowSta'
	}, {
		name : 'conttreesNo'
	}, {
		name : 'contractName'
	}, {
		name : 'clientName'
	}, {
		name : 'isSum'
	}, {
		name : 'actAmount'
	}, {
		name : 'currencyType'
	}, {
		name : 'operateName'
	}, {
		name : 'operateDeptName'
	}, {
		name : 'signStartDate'
	}, {
		name : 'signEndDate'
	}, {
		name : 'conModify'
	}, {
		name : 'workflowNo'
	},{
		name : 'currencyName'
	}]);
	var con_sm = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true
	});
	var con_item_cm = new Ext.grid.ColumnModel([con_sm,
			new Ext.grid.RowNumberer({
				header : '序号',
				width : 50,
				align : 'center'
			}), {
				header : 'ID',
				dataIndex : 'conId',
				hidden : true,
				align : 'center'
			}, {
				header : '会签状态',
				dataIndex : 'workflowSta',
				align : 'center'
//				renderer : function(v) {
//					if (v == 0) {
//						return "未上报";
//					}
//					if (v == 1) {
//						return "待会签";
//					}
//					if (v == 2) {
//						return "已会签";
//					}
//					if (v == 3) {
//						return "退回";
//					}
//					return v;
//				}
			}, {
				header : '合同编号',
				dataIndex : 'conttreesNo',
				align : 'center',
				width : 140
			}, {
				header : '合同名称',
				dataIndex : 'contractName',
				align : 'center',
				width :200,
				renderer : function change(val) {
				return ' <span style="white-space:normal;">' + val + ' </span>';
		}
			}, {
				header : '供应商',
				dataIndex : 'clientName',
				align : 'center'
			}, {
				header : '有无总金额',
				dataIndex : 'isSum',
				align : 'center'
//				renderer : function(v) {
//					if (v == "Y") {
//						return "有";
//					} else {
//						return "无";
//					}
//
//				}
			}, {
				header : '总金额',
				dataIndex : 'actAmount',
				align : 'center'
			}, {
				header : '币别',
				dataIndex : 'currencyName',
				align : 'center'
				
			}, {
				header : '有无变更',
				dataIndex : 'conModify',
				align : 'center'
//				renderer : function(v) {
//					if (v != null && v != "") {
//						return "有";
//					} else {
//						return "无";
//					}
//
//				}
			}, {
				header : '经办人',
				dataIndex : 'operateName',
				align : 'center'
			}, {
				header : '经办部门',
				dataIndex : 'operateDeptName',
				align : 'center'
			}, {
				header : '会签开始时间',
				dataIndex : 'signStartDate',
				width : 120,
				align : 'center'
			}, {
				header : '会签结束时间',
				dataIndex : 'signEndDate',
				width : 120,
				align : 'center'
			}]);
	con_item_cm.defaultSortable = true;
	var con_ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'managecontract/getAllConList.action'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'total',
			root : 'root'
		}, con_item)
	});
	con_ds.load({
		params : {
			startdate : sdate,
			enddate : edate,
			start : 0,
			limit : 18,
			conTypeId : 1
		}
	});
	con_ds.on('beforeload', function() {
		Ext.apply(this.baseParams, {
					    startdate : Ext.get('fromDate').dom.value,
						enddate : Ext.get('toDate').dom.value,
						//conNo : Ext.get('conttreesNo').dom.value,
						//conName : Ext.get('contractName').dom.value,
						//clientName : Ext.get('cliendId').dom.value,
						//operaterBy : operateBox.getValue(),
						//status : statusBox.getValue(),
						conTypeId : 1
				});
	});
	
	var gridbbar = new Ext.PagingToolbar({
		pageSize : 18,
		store : con_ds,
		displayInfo : true,
		displayMsg : "显示第 {0} 条到 {1} 条记录，一共 {2} 条",
		emptyMsg : "没有记录",
		beforePageText : '页',
		afterPageText : "共{0}"
	});
	var Grid = new Ext.grid.GridPanel({
		ds : con_ds,
		cm : con_item_cm,
		sm : con_sm,
		// title : '合同列表',
		width : Ext.get('div_lay').getWidth(),
		split : true,
		autoScroll : true,
		bbar : gridbbar,
		// tbar : contbar,
		border : false,
		viewConfig : {
			forceFit : false
		}
	});

//	Grid.on("rowcontextmenu", function(g, i, e) {
//		e.stopEvent();
//		var record = Grid.getStore().getAt(i);
//		// 右键菜单
//		var menu = new Ext.menu.Menu({
//			id : 'mainMenu',
//			items : [new Ext.menu.Item({
//				text : '票面浏览',
//				handler : function() {
//					var conId = record.get("conId");
//					var workflowStatus = record.get("workflowStatus");
//					var strReportAdd = "";
//					if (workflowStatus == "0" || workflowStatus == "1"
//							|| workflowStatus == "2") {
//						strReportAdd = "/powerrpt/report/webfile/contract.jsp?conId="
//								+ conId;
//					} else {
//						strReportAdd = "E";
//					}
//					if (strReportAdd != Constants.BirtNull) {
//						window.open(strReportAdd);
//					}
//					// if (strReportAdd !="E") {
//					//						
//					// alert(strAdds);
//					// strAdds="/powerrpt/report/webfile/ElectricTwo.jsp?no=H12009010001";
//					// //window.open(strAdds);
//					//						
//					// } else {
//					// Ext.Msg.alert("目前没有该种工作票票面预览");
//					// }
//					// contractPrint(record);
//				}
//			}), new Ext.menu.Item({
//				text : '图形展示',
//				handler : function() {
//					alert();
//					// var entryId = record.get("model.workFlowNo");
//					// var url = "";
//					// if(entryId == "" || entryId == null)
//					// {
//					// var workticketCode =
//					// record.get('model.workticketTypeCode');
//					// var flowCode = getFlowCode(workticketCode);
//					// url =
//					// "/power/workflow/manager/flowshow/flowshow.jsp?flowCode="+flowCode;
//					// }
//					// else
//					// {
//					// url =
//					// "/power/workflow/manager/show/show.jsp?entryId="+entryId;
//					// }
//					// window.open(url);
//				}
//			}), new Ext.menu.Item({
//
//				text : '查看审批记录',
//				handler : function() {
//					// var entryId = record.get("model.workFlowNo");
//					// if(entryId != "" && entryId != null)
//					// {
//					// var url =
//					// "/power/workflow/manager/approveInfo/approveInfo.jsp?entryId="
//					// + entryId;
//					// window.showModalDialog(
//					// url,
//					// null,
//					// "dialogWidth:650px;dialogHeight:400px;;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;");
//					// }
//					// else{
//					// Ext.Msg.alert("提示","流程尚未启动");
//					// }
//				}
//			})]
//		});
//		var coords = e.getXY();
//		menu.showAt([coords[0], coords[1]]);
//	});

	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		items : [{
			region : "north",
			layout : 'fit',
			height : 150,
			border : true,
			split : true,
			margins : '0 0 0 0',
			// 注入表格
			items : [form]
		}, {
			region : "center",
			layout : 'fit',
			title : '合同列表',
			border : false,
			collapsible : true,
			split : true,
			margins : '0 0 0 0',
			// 注入表格
			items : [Grid]
		}]
	});
//		Grid.on('rowclick', function(){
//		 var rec = Grid.getSelectionModel().getSelected();
//		  if(rec.get("actAmount")<20000){
//		  btnApprove.setDisabled(true);
//		  btnSign.setDisabled(true);
//		  } else{
//		  btnApprove.setDisabled(false);
//		  btnSign.setDisabled(false);
//		  }
//		  })

})