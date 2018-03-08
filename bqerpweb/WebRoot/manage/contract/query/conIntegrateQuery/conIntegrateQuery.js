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
		emptyText : '请选择',
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
		//store : [['999999', '测试员'], ['000000', 'xx']],
		id : 'operateName',
		//name : 'operateBy',
		//valueField : "value",
		//displayField : "text",
		mode : 'local',
		typeAhead : true,
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
	});
	var statusBox = new Ext.form.ComboBox({
		fieldLabel : '合同状态',
		store : [['0', '登记'], ['1', '执行'], ['2', '变更'], ['3', '解除'],['4', '履行终止'],
				[null, '全部']],
		id : 'exeFlag',
		name : 'execFlagName',
		valueField : "value",
		displayField : "text",
		mode : 'local',
		typeAhead : true,
		forceSelection : true,
		hiddenName : 'execFlagName',
		editable : false,
		triggerAction : 'all',
		selectOnFocus : true,
		emptyText : '请选择',
		anchor : '100%'
	});
	var fileStatusBox = new Ext.form.ComboBox({
		fieldLabel : '归档状态',
		store : [['DRF', '未归档'], ['PRE', '预归档'], ['OK', '已归档'], ['BAK', '被退回'],
				[null, '全部']],
		id : 'fileStatue',
		name : 'fileStatueName',
		valueField : "value",
		displayField : "text",
		mode : 'local',
		typeAhead : true,
		forceSelection : true,
		hiddenName : 'fileStatueName',
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
			var _thisTitle = '<center><font color="blue" size="3">灞桥热电厂采购合同综合统计</font><br/><br/>'
	            + fromDate.value + "  至 " +toDate.value +"</center>";
			Grid.setTitle(_thisTitle);
			
			var sd = Ext.get("fromDate").dom.value;
			var ed = Ext.get("toDate").dom.value;
			if(sd>ed)
			{
				Ext.Msg.alert('提示','选择后一日期应比前一日期大！');
				return false;
			}
			con_ds.load({
				params : {
					startdate : Ext.get("fromDate").dom.value,
					enddate : Ext.get("toDate").dom.value,
					start : 0,
					limit : 18,
					conNo : Ext.get('conttreesNo').dom.value,
					conName : Ext.get('contractName').dom.value,
					clientName : hidcliendId.getValue(),
					operaterBy : hidoperateBox.getValue(),
					status : statusBox.getValue(),
					fileStatus :fileStatusBox.getValue(),
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
			if(selrows.length > 0)
			{
				var record = Grid.getSelectionModel().getSelected();
				var url;
				var conId=record.data.conId;
				var conModify = record.data.conModify;
				var conModifyId =record.data.conModifyId;
				if((conModifyId != null) && (conModifyId != "") && (conModify=="有"))
				{
					url="../../../../manage/contract/business/conBaseInfo/conInfo.jsp?conId="+conId+"&conModifyId="+conModifyId;	
				}
				else
				{
					url = "../../../../manage/contract/business/conBaseInfo/conBaseInfo.jsp?id="+conId;				
				}
				var o = window.showModalDialog(url, '',
						'dialogWidth=850px;dialogHeight=700px;status=no');
			}
			else
			{
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
	var btnPrint = new Ext.Button({
		id : 'btnPrint',
		text : '打 印',
		minWidth : 70,
		handler : function() {
		CheckRptPreview()
		}
	});
	
	var btnProxy = new Ext.Button({
		id : 'btnProxy',
		text : '委托书',
		minWidth : 70,
		handler : function() {
			var selected = Grid.getSelectionModel().getSelections();
			var conId;
			if (selected.length == 0) {
				Ext.Msg.alert("提示", "请选择要浏览的记录！");
			} else {
				var menber = selected[0];
				conId = menber.get('conId');
				var url = "/powerrpt/report/webfile/bqmis/Proxyreport.jsp?conId="
						+ conId;
				window.open(url);
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
			url : 'managecontract/finConIntegrateList.action?conTypeId='+1,
			success : function(response) {
				var json = eval('(' + response.responseText.trim() + ')');
				var records = json.root;
				 //alert(json.root);
				var html = ['<table border=1><tr><th>合同编号</th><th>合同名称</th><th>供应商</th><th>有无总金额</th><th>总金额</th><th>币别</th><th>有无变更</th><th>合同状态</th><th>归档状态</th><th>经办人</th><th>经办部门</th><th>履行开始时间</th><th>履行结束时间</th></tr>'];
				for (var i = 0; i < records.length; i += 1) {
					var rc = records[i];
					html.push('<tr><td>' + rc.conttreesNo + '</td><td>'
							+ rc.contractName + '</td><td>' + rc.clientName
							+ '</td><td>' + rc.isSum + '</td><td>'
							+ rc.actAmount + '</td><td>' + rc.currencyName
							+ '</td><td>' + rc.conModify + '</td><td>'
							+ rc.exeFlag + '</td><td>' + rc.fileStatue
							+ '</td><td>' + rc.operateName + '</td><td>'
							+ rc.operateDeptName + '</td><td>' + rc.startDate
							+ '</td><td>' + rc.endDate + '</td></tr>');
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
				items : [cliendId]
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
				items : [btnPrint]
			},{
				border : false,
				columnWidth : 0.15,
				layout : 'form',
			    items : [btnProxy]
			}]
		}
//		, {
//			layout : 'column',
//			border : false,
//			items : [{
//				border : false,
//				columnWidth : 0.3,
//				layout : 'form',
//				items : [statusBox]
//			},{
//				border : false,
//				columnWidth : 0.32,
//				layout : 'form',
//				items : [fileStatusBox]
//			}]
//		}
		]
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
		name : 'conModify'
	}, {
		name : 'exeFlag'
	}, {
		name : 'fileStatue'
	}, {
		name : 'operateName'
	}, {
		name : 'operateDeptName'
	}, {
		name : 'startDate'
	}, {
		name : 'endDate'
	}, {
		name : 'conModifyId'
	},{
		name : 'currencyName'
	}]);
	var con_sm = new Ext.grid.CheckboxSelectionModel({
			 singleSelect : true
	});
	var con_item_cm = new Ext.grid.ColumnModel([con_sm,
			new Ext.grid.RowNumberer({
			//	header : '序号',
				width : 20,
				align : 'center'
			}), {
				header : '合同编号',
				dataIndex : 'conttreesNo',
				width :130,
				align : 'left'
			}, {
				header : '合同名称',
				dataIndex : 'contractName',
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
			}, {
				header : '合同状态',
				dataIndex : 'exeFlag',
				align : 'center'
//				renderer : function(v) {
//					if (v == 0) {
//						return "登记";
//					} 
//					if (v == 1) {
//						return "执行";
//					}
//					if (v == 2) {
//						return "变更";
//					}
//					if (v == 3) {
//						return "解除";
//					}
//					if (v == 4) {
//						return "履行终止";
//					}
//					return v;
//				}
			},
				{
				header : '归档状态',
				dataIndex : 'fileStatue',
				align : 'center'
//				renderer : function(s) {
//					if (s == 'DRF') {
//						return "未归档";
//					} 
//					if (s == 'PRE') {
//						return "预归档";
//					}
//					if (s == 'OK') {
//						return "已归档";
//					}
//					if (s == 'BAK') {
//						return "被退回";
//					}
//					return s;
//				} 
			}, 
				{
				header : '经办人',
				dataIndex : 'operateName',
				align : 'center'
			}, {
				header : '经办部门',
				dataIndex : 'operateDeptName',
				align : 'center'
			}, {
				header : '履行开始时间',
				dataIndex : 'startDate',
				width : 120,
				align : 'center'
			}, {
				header : '履行结束时间',
				dataIndex : 'endDate',
				width : 120,
				align : 'center'
			}, {
				dataIndex : 'conModifyId',
				width : 120,
				align : 'center',
				hidden: true
			}]);
	con_item_cm.defaultSortable = true;
	var con_ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'managecontract/finConIntegrateList.action'
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
			startdate : Ext.get("fromDate").dom.value,
			enddate : Ext.get("toDate").dom.value,
			conNo : Ext.get('conttreesNo').dom.value,
			conName : Ext.get('contractName').dom.value,
			clientName : hidcliendId.getValue(),
			operaterBy : hidoperateBox.getValue(),
			status : statusBox.getValue(),
			fileStatus :fileStatusBox.getValue(),
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
	var _title = '<center><font color="blue" size="3">灞桥热电厂采购合同综合统计</font><br/><br/>'
	            + sdate + "  至 " +edate +"</center>";
	var Grid = new Ext.grid.GridPanel({
		title : _title ,
		ds : con_ds,
		cm : con_item_cm,
		sm : con_sm,
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
			
			border : false,
			split : true,
			margins : '0 0 0 0',
			// 注入表格
			items : [Grid]
		}]
	});

})