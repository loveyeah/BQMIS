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
			CurrentDate = CurrentDate + Month ;
//			+ "-";
		} else {
			CurrentDate = CurrentDate + "0" + Month;
//			+ "-";
		}
//		if (Day >= 10) {
//			CurrentDate = CurrentDate + Day;
//		} else {
//			CurrentDate = CurrentDate + "0" + Day;
//		}
		return CurrentDate;
	}
	var enddate = new Date();
	var startdate = enddate.add(Date.MONTH, -1);
	startdate = startdate.getFirstDateOfMonth();
	var sdate = ChangeDateToString(startdate);
	var edate = ChangeDateToString(enddate);

	var fromdate = sdate;
	var enddate = edate;
	
	
	var fromDate = new Ext.form.TextField({
		name : 'startDate',
		value : sdate,
		id : 'fromDate',
		fieldLabel : "执行时间",
		style : 'cursor:pointer',
		cls : 'Wdate',
		anchor : '100%',
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M',
					dateFmt : 'yyyy-MM',
					alwaysUseStartDate : true
				});
			}
		}
	});
	var toDate = new Ext.form.TextField({
		name : 'endDate',
		value : enddate,
		id : 'toDate',
		fieldLabel : "至",
		style : 'cursor:pointer',
		cls : 'Wdate',
		anchor : '80%',
		listeners : {
			focus : function() {
				WdatePicker({
					startDate : '%y-%M',
					dateFmt : 'yyyy-MM',
					alwaysUseStartDate : true
				});
			}
		}
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
	
	// 合作伙伴
	var cliendId = new Ext.form.ComboBox({
		name : 'cliendId',
		id : 'cliendId',
		fieldLabel : '合作伙伴',
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
				id : 'operateName',
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
					var o = window.showModalDialog(url, args,
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
	var btnQuery = new Ext.Button({
		id : 'btnQuery',
		text : '查  询',
		minWidth : 70,
		handler : function() { 
			var _thisTitle = '<center><font color="blue" size="3">大唐灞桥电厂项目合同付款执行情况统计</font><br/><br/>'
	            + fromDate.getValue() + "  至 " +toDate.getValue() +"</center>";
			Grid.setTitle(_thisTitle);
			con_ds.load({
				params : {
					conTypeId : 2,
					startdate : Ext.get("fromDate").dom.value,
					enddate :  Ext.get("toDate").dom.value,
					start : 0,
					limit : 18,
					conNo : Ext.get('conttreesNo').dom.value,
					conName : Ext.get('contractName').dom.value,
					clientName : hidcliendId.getValue(),
					operaterBy : hidoperateBox.getValue()
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
			hidoperateBox.setValue();
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
				if((conId != null) && (conId != ""))
				{
					url = "../../../../manage/projectFact/business/conBaseInfo/conBaseInfo.jsp?id="+conId;				
				}
				var o = window.showModalDialog(url, '','dialogWidth=850px;dialogHeight=700px;status=no');
			}
			else
			{
				Ext.Msg.alert('提示', '请从列表中选择一条记录!');
			}
		}
	});
	var btnPrint = new Ext.Button({
		id : 'btnPrint',
		text : '会签表 ',
		minWidth : 70,
		handler : function() {
 		CheckRptPreview() 
		}
	});
	
					   // 验收票面浏览
	function CheckRptPreview() {

		var selected = Grid.getSelectionModel().getSelections();
		var conId;

		if (selected.length == 0) {
			Ext.Msg.alert("提示", "请选择要浏览的记录！");
		} else {

			var menber = selected[0];
			conId = menber.get('conId');

			var url = "/powerrpt/report/webfile/bqmis/GCContractMeetSign.jsp?conId="
					+ conId;
			window.open(url);

		}
	};
	var btnExport = new Ext.Button({
		id : 'btnExport',
		text : '导  出',
		minWidth : 70,
		handler : function() {
			myexport();
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

	function myexport() {
		Ext.Ajax.request({
			url : 'managecontract/findConPayDetailsList.action',
			success : function(response) {
				var json = eval('(' + response.responseText.trim() + ')');
				var records = json.root;
				// alert(records.length);
				var html = ['<table border=1><tr><th>合同编号</th><th>合同名称</th><th>合作伙伴</th><th>总金额</th><th>币别</th><th>已结算金额</th><th>未结算金额</th><th>经办人</th></tr>'];
				for (var i = 0; i < records.length; i += 1) {
					var rc = records[i];
					html.push('<tr><td>' + rc.conttreesNo + '</td><td>'
							+ rc.contractName + '</td><td>' + rc.clientName
							+ '</td><td>' + rc.actAmount + '</td><td>'
							+ rc.currencyType + '</td><td>' + rc.payedAmount
							+ '</td><td>' + rc.unliquidate + '</td><td>'
							+ rc.operateName + '</td></tr>');
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
				items : [operateBox]
			}, {
				border : false,
				columnWidth : 0.15,
				layout : 'form',
				items : [btnExport]
			}
			, {
				border : false,
				columnWidth : 0.15,
				layout : 'form',
				items : [btnPrint]
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
		name : 'conttreesNo'
	}, {
		name : 'contractName'
	}, {
		name : 'clientName'
	}, {
		name : 'actAmount'
	}, {
		name : 'currencyType'
	},{
		name : 'payedAmount'
	}, {
		name : 'unliquidate'
	}, {
		name : 'operateName'
	},{
		name : 'currencyName'
	}]);
	var con_sm = new Ext.grid.CheckboxSelectionModel({
			 singleSelect : true
	});
	var con_item_cm = new Ext.grid.ColumnModel([con_sm,
			new Ext.grid.RowNumberer({
				//header : '序号',
				width : 20,
				align : 'center'
			}), {
				header : '合同编号',
				dataIndex : 'conttreesNo',
				align : 'center',
				width :140
			}, {
				header : '合同名称',
				dataIndex : 'contractName',
				align : 'center',
				width : 200,
				renderer : function change(val) {
					return ' <span style="white-space:normal;">' + val
							+ ' </span>';
				}
			}, {
				header : '合作伙伴',
				dataIndex : 'clientName',
				align : 'center'
			}, {
				header : '总金额',
				dataIndex : 'actAmount',
				align : 'center'
			}, {
				header : '币别',
				dataIndex : 'currencyName',
				align : 'center'
//				,
//				renderer : function(v) {
//
//					if (v == 1) {
//						return "RMB";
//					}
//					if (v == 2) {
//						return "USD";
//					} else {
//						return "异常";
//					}
//
//				}
			}, {
				header : '已结算金额',
				dataIndex : 'payedAmount',
				width : 120,
				align : 'center',
				renderer: function(v){
				if(v== null){
				return "0";
				}else{
					return v;
				}
				}
			}, {
				header : '未结算金额',
				dataIndex : 'unliquidate',
				width : 120,
				align : 'center'
			}, {
				header : '经办人',
				dataIndex : 'operateName',
				align : 'center'
			}]);
	con_item_cm.defaultSortable = true;
	var con_ds = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : 'managecontract/findConPaymentList.action'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'total',
			root : 'root'
		}, con_item)
	});
	
	con_ds.load({
		params : {
			conTypeId : 2,
			startdate : sdate,
			enddate : enddate,
			start : 0,
			limit : 18
		}
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
	var _title = '<center><font color="blue" size="3">大唐灞桥电厂项目合同付款执行情况统计</font><br/><br/>'
	            + fromdate + "  至 " +enddate +"</center>";
	var Grid = new Ext.grid.GridPanel({
		title:_title,
		ds : con_ds,
		cm : con_item_cm,
		sm : con_sm,
		width : Ext.get('div_lay').getWidth(),
		split : true,
		autoScroll : true,
		bbar : gridbbar,
//	 	tbar : gridtbar,
		border : false,
		viewConfig : {
			forceFit : false
		}
	});
	//显示柱状图
	function displaygraphcolumn() {
		//
		var so = new SWFObject("comm/amchart/amcolumn/amcolumn.swf", "amline", "100%", "100%",
				"8", "#FFFFFF");
		so.addVariable("path", "comm/amchart/amcolumn/");
		so.addVariable("settings_file", escape("comm/amchart/amcolumn/amcolumn_settings.xml"));
		so.addVariable("data_file", escape("system/getElectricColumnOnUnit.action"));
		so.addVariable("preloader_color", "#999999");
		so.write("flashColumn");
	}
	new Ext.Viewport({
		enableTabScroll : true,
		layout : "border",
		items : [{
			region : "north",
			layout : 'fit',
			height : 150,
			border : true,
			split : false,
			margins : '0 0 0 0',
			// 注入表格
			items : [form]
		}, {
			region : "center",
			id:'centerPanel',
			layout : 'fit', 
			//title : _title ,
			border : false,
			collapsible : true,
			split : true,
			margins : '0 0 0 0',
			// 注入表格
			items : [Grid]
		}
		]
	});
})